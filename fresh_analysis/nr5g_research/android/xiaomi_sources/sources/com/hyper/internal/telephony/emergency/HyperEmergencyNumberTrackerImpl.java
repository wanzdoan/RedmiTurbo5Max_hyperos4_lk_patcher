package com.hyper.internal.telephony.emergency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RegistrantList;
import android.telephony.CellIdentity;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.emergency.EmergencyNumber;
import android.text.TextUtils;
import com.android.internal.telephony.IHyperEmergencyNumberTracker;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class HyperEmergencyNumberTrackerImpl implements IHyperEmergencyNumberTracker {
    private static final long CLOUD_ECC_DEBOUNCE_MS = 1000;
    private static final boolean DBG = true;
    private static final int EVENT_CLOUD_ECC_CHANGED = 5;
    private static final int EVENT_ICC_CHANGED = 1;
    private static final int EVENT_LOCATED_PLMN_CHANGED = 4;
    private static final int EVENT_SERVICE_STATE_CHANGED = 2;
    private static final int EVENT_UNSOL_CELL_INFO = 3;
    private static final int HIGH_PRIORITY_SOURCES = 3;
    private static final String KEY_DEPRECATED_NUMBERS = "deprecatedNumbers";
    private static final String KEY_PHONE_ID = "phoneId";
    private static final String KEY_PLMN = "plmn";
    private static final String KEY_RESULT = "emergencyNumbers";
    private static final String KEY_SIM_INSERT = "isSimInsert";
    private static final String LOG_TAG = "HyperEmergencyNumberTrackerImpl";
    private static final String METHOD_GET_DEPRECATED_NUMBERS = "getDeprecatedNumbers";
    private static final String METHOD_GET_EMERGENCY_NUMBER = "getEmergencyNumber";
    private Handler mHandler;
    private final Phone mPhone;
    private final int mSlotIndex;
    private final UiccController mUiccController;
    private static final Uri ECC_PROVIDER_URI = Uri.parse("content://com.xiaomi.ecc.provider");
    private static final Uri CLOUD_DATA_URI = Uri.parse("content://com.xiaomi.ecc.provider/cloud_data");
    private final RegistrantList mEmergencyNumberListRegistrants = new RegistrantList();
    private EccState mCachedState = null;
    private volatile Set<String> mCachedDeprecatedNumbers = new HashSet();
    private String mLocatedPlmn = null;
    private String mLastRegPlmn = null;
    private String mLastSimPlmn = null;

    private static class EccState {
        final boolean isPinLocked;
        final boolean isSimInserted;
        final String regPlmn;
        final String simPlmn;

        EccState(String regPlmn, String simPlmn, boolean isSimInserted, boolean isPinLocked) {
            this.regPlmn = regPlmn;
            this.simPlmn = simPlmn;
            this.isSimInserted = isSimInserted;
            this.isPinLocked = isPinLocked;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return HyperEmergencyNumberTrackerImpl.DBG;
            }
            if (!(o instanceof EccState)) {
                return false;
            }
            EccState that = (EccState) o;
            if (this.isSimInserted == that.isSimInserted && this.isPinLocked == that.isPinLocked && Objects.equals(this.regPlmn, that.regPlmn) && Objects.equals(this.simPlmn, that.simPlmn)) {
                return HyperEmergencyNumberTrackerImpl.DBG;
            }
            return false;
        }

        public String toString() {
            return "EccState{regPlmn=" + this.regPlmn + ", simPlmn=" + this.simPlmn + ", isSimInserted=" + this.isSimInserted + ", isPinLocked=" + this.isPinLocked + "}";
        }
    }

    public HyperEmergencyNumberTrackerImpl(Phone phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone cannot be null");
        }
        this.mPhone = phone;
        this.mSlotIndex = phone.getPhoneId();
        HandlerThread handlerThread = new HandlerThread(LOG_TAG);
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper()) { // from class: com.hyper.internal.telephony.emergency.HyperEmergencyNumberTrackerImpl.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                HyperEmergencyNumberTrackerImpl.this.logd("handleMessage: what=" + msg.what);
                switch (msg.what) {
                    case 1:
                        AsyncResult ar = (AsyncResult) msg.obj;
                        if (ar != null && ar.result != null && ((Integer) ar.result).intValue() == HyperEmergencyNumberTrackerImpl.this.mSlotIndex) {
                            HyperEmergencyNumberTrackerImpl.this.updateEmergencyNumbers();
                            break;
                        }
                        break;
                    case 2:
                    case 3:
                        HyperEmergencyNumberTrackerImpl.this.updateEmergencyNumbers();
                        break;
                    case 4:
                        String locatedPlmn = (String) msg.obj;
                        if (!TextUtils.isEmpty(locatedPlmn)) {
                            HyperEmergencyNumberTrackerImpl.this.mLocatedPlmn = locatedPlmn;
                        }
                        HyperEmergencyNumberTrackerImpl.this.updateEmergencyNumbers();
                        break;
                    case 5:
                        HyperEmergencyNumberTrackerImpl.this.updateEmergencyNumbers(HyperEmergencyNumberTrackerImpl.DBG);
                        break;
                    default:
                        HyperEmergencyNumberTrackerImpl.this.loge("Unknown message: " + msg.what);
                        break;
                }
            }
        };
        this.mUiccController = UiccController.getInstance();
        if (this.mUiccController != null) {
            this.mUiccController.registerForIccChanged(this.mHandler, 1, (Object) null);
        }
        if (this.mPhone.getServiceStateTracker() != null) {
            this.mPhone.getServiceStateTracker().registerForServiceStateChanged(this.mHandler, 2, (Object) null);
        }
        this.mPhone.registerForCellInfo(this.mHandler, 3, (Object) null);
        IntentFilter filter = new IntentFilter("com.mediatek.intent.action.LOCATED_PLMN_CHANGED");
        this.mPhone.getContext().registerReceiver(new BroadcastReceiver() { // from class: com.hyper.internal.telephony.emergency.HyperEmergencyNumberTrackerImpl.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                int phoneId = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
                if (phoneId == HyperEmergencyNumberTrackerImpl.this.mSlotIndex) {
                    String locatedPlmn = intent.getStringExtra("android.telephony.extra.PLMN");
                    HyperEmergencyNumberTrackerImpl.this.mHandler.obtainMessage(4, locatedPlmn).sendToTarget();
                }
            }
        }, filter, null, this.mHandler, 2);
        this.mPhone.getContext().getContentResolver().registerContentObserver(CLOUD_DATA_URI, false, new ContentObserver(this.mHandler) { // from class: com.hyper.internal.telephony.emergency.HyperEmergencyNumberTrackerImpl.3
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                HyperEmergencyNumberTrackerImpl.this.logd("CloudEcc ContentObserver onChange");
                HyperEmergencyNumberTrackerImpl.this.mHandler.removeMessages(5);
                HyperEmergencyNumberTrackerImpl.this.mHandler.sendEmptyMessageDelayed(5, 1000L);
            }
        });
    }

    public void registerForEmergencyNumberListChanged(Handler handler, int what, Object obj) {
        this.mEmergencyNumberListRegistrants.addUnique(handler, what, obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmergencyNumbers() {
        updateEmergencyNumbers(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmergencyNumbers(boolean forceUpdate) {
        EccState currentState = new EccState(getRegPlmn(), getSimPlmn(), isSimInserted(), isPinLocked());
        logd("updateEmergencyNumbers: current=" + currentState + ", mLastSimPlmn=" + this.mLastSimPlmn + ", mLastRegPlmn=" + this.mLastRegPlmn + ", forceUpdate=" + forceUpdate);
        if (!forceUpdate && currentState.equals(this.mCachedState)) {
            return;
        }
        this.mCachedState = currentState;
        if (!TextUtils.isEmpty(currentState.regPlmn)) {
            this.mLastRegPlmn = currentState.regPlmn;
        }
        if (!TextUtils.isEmpty(currentState.simPlmn)) {
            this.mLastSimPlmn = currentState.simPlmn;
        }
        boolean effectiveSimInsert = (!currentState.isSimInserted || currentState.isPinLocked) ? false : DBG;
        List<EmergencyNumber> emergencyNumbers = matchEmergencyNumbers(currentState.regPlmn, currentState.simPlmn, this.mLastSimPlmn, this.mLastRegPlmn, effectiveSimInsert);
        if (emergencyNumbers != null && !emergencyNumbers.isEmpty()) {
            logd("updateEmergencyNumbers: count=" + emergencyNumbers.size());
        } else {
            loge("updateEmergencyNumbers: emergencyNumbers is empty");
        }
        notifyEmergencyNumberChanged(emergencyNumbers);
    }

    private List<EmergencyNumber> matchEmergencyNumbers(String regPlmn, String simPlmn, String lastSimPlmn, String lastRegPlmn, boolean isSimInsert) {
        String plmn = selectBestPlmn(regPlmn, simPlmn, lastSimPlmn, lastRegPlmn, isSimInsert);
        if (TextUtils.isEmpty(plmn)) {
            this.mCachedDeprecatedNumbers = new HashSet();
            return new ArrayList();
        }
        this.mCachedDeprecatedNumbers = queryDeprecatedFromProvider(plmn, isSimInsert);
        return queryFromProvider(plmn, isSimInsert);
    }

    private String selectBestPlmn(String regPlmn, String simPlmn, String lastSimPlmn, String lastRegPlmn, boolean isSimInsert) {
        if (isSimInsert) {
            if (!TextUtils.isEmpty(regPlmn)) {
                return regPlmn;
            }
            if (TextUtils.isEmpty(simPlmn)) {
                return null;
            }
            return simPlmn;
        }
        if (!TextUtils.isEmpty(regPlmn)) {
            return regPlmn;
        }
        if (!TextUtils.isEmpty(lastSimPlmn)) {
            return lastSimPlmn;
        }
        if (TextUtils.isEmpty(lastRegPlmn)) {
            return null;
        }
        return lastRegPlmn;
    }

    private String getBestPlmn() {
        return selectBestPlmn(getRegPlmn(), getSimPlmn(), this.mLastSimPlmn, this.mLastRegPlmn, (!isSimInserted() || isPinLocked()) ? false : DBG);
    }

    private List<EmergencyNumber> queryFromProvider(String plmn, boolean isSimInsert) {
        List<EmergencyNumber> list;
        List<EmergencyNumber> numbers = new ArrayList<>();
        try {
            Bundle args = new Bundle();
            args.putInt(KEY_PHONE_ID, this.mSlotIndex);
            args.putString(KEY_PLMN, plmn);
            args.putBoolean(KEY_SIM_INSERT, isSimInsert);
            Bundle result = this.mPhone.getContext().getContentResolver().call(ECC_PROVIDER_URI, METHOD_GET_EMERGENCY_NUMBER, (String) null, args);
            if (result != null && (list = result.getParcelableArrayList(KEY_RESULT)) != null) {
                numbers.addAll(list);
            }
        } catch (Exception e) {
            loge("queryFromProvider: " + e.getMessage());
        }
        return numbers;
    }

    public List<EmergencyNumber> filterDeprecatedEmergencyNumbers(List<EmergencyNumber> emergencyNumberList) {
        final Set<String> deprecated = this.mCachedDeprecatedNumbers;
        if (deprecated.isEmpty()) {
            return emergencyNumberList;
        }
        logd("filterDeprecatedEmergencyNumbers: deprecated=" + deprecated);
        List<EmergencyNumber> filtered = new ArrayList<>(emergencyNumberList);
        filtered.removeIf(new Predicate() { // from class: com.hyper.internal.telephony.emergency.HyperEmergencyNumberTrackerImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return HyperEmergencyNumberTrackerImpl.lambda$filterDeprecatedEmergencyNumbers$0(deprecated, (EmergencyNumber) obj);
            }
        });
        return filtered;
    }

    static /* synthetic */ boolean lambda$filterDeprecatedEmergencyNumbers$0(Set deprecated, EmergencyNumber en) {
        if (deprecated.contains(en.getNumber()) && (en.getEmergencyNumberSourceBitmask() & 3) == 0) {
            return DBG;
        }
        return false;
    }

    private Set<String> queryDeprecatedFromProvider(String plmn, boolean isSimInsert) {
        List<String> list;
        Set<String> deprecated = new HashSet<>();
        if (TextUtils.isEmpty(plmn)) {
            return deprecated;
        }
        try {
            Bundle args = new Bundle();
            args.putInt(KEY_PHONE_ID, this.mSlotIndex);
            args.putString(KEY_PLMN, plmn);
            args.putBoolean(KEY_SIM_INSERT, isSimInsert);
            Bundle result = this.mPhone.getContext().getContentResolver().call(ECC_PROVIDER_URI, METHOD_GET_DEPRECATED_NUMBERS, (String) null, args);
            if (result != null && (list = result.getStringArrayList(KEY_DEPRECATED_NUMBERS)) != null) {
                deprecated.addAll(list);
            }
        } catch (Exception e) {
            loge("queryDeprecatedFromProvider: " + e.getMessage());
        }
        return deprecated;
    }

    private void notifyEmergencyNumberChanged(List<EmergencyNumber> emergencyNumbers) {
        this.mEmergencyNumberListRegistrants.notifyRegistrants(new AsyncResult((Object) null, emergencyNumbers, (Throwable) null));
    }

    private String getRegPlmn() {
        String plmn = "";
        ServiceState ss = this.mPhone.getServiceState();
        if (ss != null && !TextUtils.isEmpty(ss.getOperatorNumeric())) {
            plmn = ss.getOperatorNumeric();
            logd("getRegPlmn: use ssPlmn=" + plmn);
        } else {
            CellIdentity cellIdentity = this.mPhone.getCurrentCellIdentity();
            if (cellIdentity != null && !TextUtils.isEmpty(cellIdentity.getPlmn())) {
                plmn = cellIdentity.getPlmn();
                logd("getRegPlmn: use cellIdentityPlmn =" + plmn);
            }
        }
        if (!TextUtils.isEmpty(this.mLocatedPlmn)) {
            logd("getRegPlmn: use locatedPlmn =" + this.mLocatedPlmn);
            return this.mLocatedPlmn;
        }
        return plmn;
    }

    private boolean isSimInserted() {
        if (getCardState() == IccCardStatus.CardState.CARDSTATE_PRESENT) {
            return DBG;
        }
        return false;
    }

    private IccCardStatus.CardState getCardState() {
        UiccCard card;
        if (this.mUiccController == null) {
            return IccCardStatus.CardState.CARDSTATE_ABSENT;
        }
        UiccSlot uiccSlot = this.mUiccController.getUiccSlotForPhone(this.mSlotIndex);
        if (uiccSlot != null && (card = uiccSlot.getUiccCard()) != null) {
            return card.getCardState();
        }
        return IccCardStatus.CardState.CARDSTATE_ABSENT;
    }

    private boolean isPinLocked() {
        IccCardConstants.State state;
        if (this.mPhone.getIccCard() == null || (state = this.mPhone.getIccCard().getState()) == null) {
            return false;
        }
        if (state.isPinLocked() || state == IccCardConstants.State.PERM_DISABLED) {
            return DBG;
        }
        return false;
    }

    private String getSimPlmn() {
        String mccMnc = this.mPhone.getOperatorNumeric();
        if (mccMnc != null && mccMnc.length() >= 5) {
            return mccMnc;
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String log) {
        Rlog.d("HyperEmergencyNumberTrackerImpl[" + this.mSlotIndex + "]", log);
    }

    private void logv(String log) {
        Rlog.v("HyperEmergencyNumberTrackerImpl[" + this.mSlotIndex + "]", log);
    }

    private void logw(String log) {
        Rlog.w("HyperEmergencyNumberTrackerImpl[" + this.mSlotIndex + "]", log);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String log) {
        Rlog.e("HyperEmergencyNumberTrackerImpl[" + this.mSlotIndex + "]", log);
    }
}
