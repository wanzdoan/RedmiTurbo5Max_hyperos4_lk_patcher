package miui.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.PreciseDataConnectionState;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.InternalTelephonyUtils;
import com.android.internal.telephony.MiuiNetworkControllerImpl;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import java.util.ArrayList;
import java.util.List;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDisplayInfoController {
    private static final int DELAY_AFTER_CALL_END_TIMER = 5000;
    private static final int EVENT_DELAY_AFTER_CALL_END = 2;
    private static final int EVENT_DELAY_NOTIFY_VOLTE = 3;
    private static final int EVENT_PRECISE_CALL_STATE_CHANGED = 1;
    private static final String MULTISIM_DUALDATA_STATUS = "ril.multisim.dualdata_status";
    private static final String SATELLITE_DATA_ENABLED = "satellite_data_enabled";
    private static final int SATELLITE_DATA_OFF = 0;
    private static final int SATELLITE_DATA_ON = 1;
    private static final String TAG = "MiuiDisplayInfoController";
    private static long mDelayVolteCount;
    private static boolean[] mIsDelayUpdatePhones;
    private static boolean[] mIsDelayUpdatePhonesForEcall;
    private static boolean[] mIsDelayUpdateVolte;
    private static int mLastCallSlotId = -1;
    private static long mValidDelayVolteCount;
    private static volatile MiuiDisplayInfoController sInstance;
    private PhoneConstants.State mCallState = PhoneConstants.State.IDLE;
    private int mCallPhoneId = -1;
    private int mNonCallPhoneId = -1;
    private List<CallStateListener> mCallStateListener = new ArrayList(1);
    private final Handler mHandler = new Handler(Looper.myLooper()) { // from class: miui.telephony.MiuiDisplayInfoController.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    Phone phone = (Phone) ar.result;
                    Rlog.d(MiuiDisplayInfoController.TAG, "EVENT_PRECISE_CALL_STATE_CHANGED");
                    MiuiDisplayInfoController.this.handleCallStateChanged(phone);
                    break;
                case 2:
                    Rlog.d(MiuiDisplayInfoController.TAG, "EVENT_DELAY_AFTER_CALL_END");
                    MiuiDisplayInfoController.this.handleCallStateEnd();
                    break;
                case 3:
                    Rlog.d(MiuiDisplayInfoController.TAG, "EVENT_DELAY_NOTIFY_VOLTE");
                    MiuiDisplayInfoController.mLastCallSlotId = -1;
                    for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
                        if (MiuiDisplayInfoController.mIsDelayUpdateVolte[i]) {
                            MiuiDisplayInfoController.this.notifyDelayVolte(i, false);
                        }
                    }
                    break;
            }
        }
    };

    public interface CallStateListener {
        void onCallStateChange(int i, boolean z);
    }

    public void addCallStateListener(CallStateListener l) {
        if (l != null) {
            this.mCallStateListener.add(l);
        }
    }

    public void removeCallStateListener(CallStateListener l) {
        if (l != null) {
            this.mCallStateListener.remove(l);
        }
    }

    private void notifyCallStateChange(int phoneId, boolean isDelay) {
        for (CallStateListener l : this.mCallStateListener) {
            l.onCallStateChange(phoneId, isDelay);
        }
    }

    private MiuiDisplayInfoController() {
        init();
    }

    public static MiuiDisplayInfoController getInstance() {
        if (sInstance == null) {
            synchronized (MiuiDisplayInfoController.class) {
                if (sInstance == null) {
                    sInstance = new MiuiDisplayInfoController();
                }
            }
        }
        return sInstance;
    }

    private void init() {
        Rlog.d(TAG, "init");
        mIsDelayUpdatePhones = new boolean[MiuiTelephony.PHONE_COUNT];
        mIsDelayUpdateVolte = new boolean[MiuiTelephony.PHONE_COUNT];
        mIsDelayUpdatePhonesForEcall = new boolean[MiuiTelephony.PHONE_COUNT];
        for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
            mIsDelayUpdatePhones[i] = false;
            mIsDelayUpdateVolte[i] = false;
            mIsDelayUpdatePhonesForEcall[i] = false;
        }
        CallManager.getInstance(MiuiTelephony.getInstance().getContext()).registerForPreciseCallStateChanged(this.mHandler, 1, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCallStateChanged(Phone phone) {
        if (phone == null || !SubscriptionManager.isValidPhoneId(phone.getPhoneId())) {
            return;
        }
        boolean isHasDelay = false;
        for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
            isHasDelay = isHasDelay || mIsDelayUpdatePhones[i];
        }
        Rlog.d(TAG, "handleCallStateChanged: " + mIsDelayUpdatePhones[phone.getPhoneId()] + ",isHasDelay=" + isHasDelay + ",phone.getState(): " + phone.getState());
        if (isHasDelay && isAllPhonesInIdle()) {
            if (this.mHandler.hasMessages(2)) {
                this.mHandler.removeMessages(2);
            }
            this.mHandler.sendEmptyMessageDelayed(2, 5000L);
        }
        handleDelayVolteInCall(phone);
    }

    public static boolean isAllPhonesInIdle() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getState() != PhoneConstants.State.IDLE) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCallStateEnd() {
        for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
            Rlog.d(TAG, "handleCallStateEnd, will update UI for phone" + i);
            if (mIsDelayUpdatePhones[i]) {
                recoverySimServiceNotify(i);
            }
        }
    }

    private void recoverySimServiceNotify(int phoneId) {
        if (SubscriptionManager.isValidPhoneId(phoneId) && mIsDelayUpdatePhones[phoneId]) {
            Phone phone = PhoneFactory.getPhone(phoneId);
            if (phone != null) {
                MiuiNetworkControllerImpl.Processor.notifyServiceStateChanged(phone);
                phone.notifySignalStrength();
                phone.getServiceStateTracker().updateCarrierDisplayName();
                if (phone.getDisplayInfoController() != null) {
                    phone.getDisplayInfoController().updateTelephonyDisplayInfo();
                }
            }
            mIsDelayUpdatePhones[phoneId] = false;
            mIsDelayUpdatePhonesForEcall[phoneId] = false;
        }
    }

    private boolean isCallStart(Phone phone) {
        return PhoneConstants.State.IDLE != phone.getState() && PhoneConstants.State.IDLE == this.mCallState;
    }

    private boolean isCallEnd(Phone phone) {
        return PhoneConstants.State.IDLE == phone.getState() && PhoneConstants.State.IDLE != this.mCallState;
    }

    public static int getOtherSlotId(int slotId) {
        for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
            if (i != slotId) {
                return i;
            }
        }
        return -1;
    }

    private int getInsertedSimCount() {
        int insertedSimCount = 0;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getIccCard() != null && phone.getIccCard().hasIccCard()) {
                insertedSimCount++;
            }
        }
        return insertedSimCount;
    }

    public boolean shouldDelayForSpecialScenario(Phone phone) {
        ServiceStateTracker sst;
        MiuiNetworkControllerImpl networkImpl;
        if (phone == null || !SubscriptionManager.isValidPhoneId(phone.getPhoneId()) || (sst = phone.getServiceStateTracker()) == null || phone.getSignalStrengthController() == null || phone.getSignalStrengthController().getSignalStrength() == null) {
            return false;
        }
        if (sst.getCombinedRegState(sst.mSS) != 1 && MiuiNetworkControllerImpl.Processor.getShouldDelayForSmoothOOS(phone.getPhoneId()) && (networkImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, phone.getPhoneId())) != null) {
            networkImpl.cancelOOSSmooth();
        }
        if (sst.getCombinedRegState(sst.mSS) == 0 && phone.getSignalStrengthController().getSignalStrength().getMiuiLevel() > 0) {
            mIsDelayUpdatePhones[phone.getPhoneId()] = false;
            mIsDelayUpdatePhonesForEcall[phone.getPhoneId()] = false;
            return false;
        }
        Rlog.d(TAG, "service state:" + sst.getCombinedRegState(sst.mSS) + "miui signal level" + phone.getSignalStrengthController().getSignalStrength().getMiuiLevel() + ", phoneId = " + phone.getPhoneId());
        if (this.mHandler.hasMessages(2) && mIsDelayUpdatePhones[phone.getPhoneId()]) {
            Rlog.d(TAG, "skip to update OOS to UI util time out...sst.getPhoneId()=" + phone.getPhoneId());
            return true;
        }
        if ((InternalTelephonyUtils.isEmergencyCall(phone) && ((sst.getCombinedRegState(sst.mSS) == 1 || phone.getSignalStrengthController().getSignalStrength().getMiuiLevel() == 0) && phone.getState() != PhoneConstants.State.IDLE && phone.getIccCard() != null && phone.getIccCard().hasIccCard())) || (mIsDelayUpdatePhones[phone.getPhoneId()] && mIsDelayUpdatePhonesForEcall[phone.getPhoneId()])) {
            mIsDelayUpdatePhones[phone.getPhoneId()] = true;
            mIsDelayUpdatePhonesForEcall[phone.getPhoneId()] = true;
            Rlog.d(TAG, "skip to update OOS to UI due to CSFB during Ecall...sst.getPhoneId()=" + phone.getPhoneId());
            return true;
        }
        if (isIdleOnOtherPhone(phone.getPhoneId()) || phone.getState() != PhoneConstants.State.IDLE || phone.getIccCard() == null || !phone.getIccCard().hasIccCard()) {
            boolean airplaneModeOn = Settings.System.getInt(phone.getContext().getContentResolver(), "airplane_mode_on", 0) == 1;
            int simState = TelephonyManager.getSimStateForSlotIndex(phone.getPhoneId());
            if (sst.getCombinedRegState(sst.mSS) == 1 && isAllPhonesInIdle() && !isChinaTestBuild() && isSupportOOSSmoothFeature() && !airplaneModeOn && simState == 10) {
                MiuiNetworkControllerImpl networkImpl2 = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, phone.getPhoneId());
                if (MiuiNetworkControllerImpl.Processor.getShouldDelayForSmoothOOS(phone.getPhoneId()) || (networkImpl2 != null && networkImpl2.handleOOSSmooth())) {
                    Rlog.d(TAG, "OOS Smoothing Signal Strength...phoneId=" + phone.getPhoneId());
                    return true;
                }
            }
            return false;
        }
        mIsDelayUpdatePhones[phone.getPhoneId()] = true;
        Rlog.d(TAG, "skip to update OOS to UI due to noncall sim sst.getPhoneId()=" + phone.getPhoneId());
        return true;
    }

    public static boolean isChinaTestBuild() {
        if (Build.IS_CM_CUSTOMIZATION_TEST || Build.IS_CU_CUSTOMIZATION_TEST || Build.IS_CT_CUSTOMIZATION_TEST || Build.IS_CTS_BUILD) {
            return true;
        }
        return false;
    }

    public static boolean isIdleOnOtherPhone(int phoneId) {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getState() != PhoneConstants.State.IDLE && phone.getPhoneId() != phoneId) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSupportOOSSmoothFeature() {
        boolean isSupportOosSmooth = HyperOSCustFeatureResolve.getBoolean("config_support_oos_smooth_v2", false);
        return isSupportOosSmooth && !Build.IS_INTERNATIONAL_BUILD;
    }

    public int convertDataConnectionState(Phone phone, PreciseDataConnectionState preciseState) {
        boolean isDsdaOrDsdalike = false;
        boolean isSatelliteDataEnabled = Settings.System.getInt(phone.getContext().getContentResolver(), SATELLITE_DATA_ENABLED, 0) == 1;
        if (isSatelliteDataEnabled) {
            Rlog.d(TAG, "preciseState.getState()" + preciseState.getState());
            return 0;
        }
        if (preciseState.getApnSetting() != null) {
            int apnTypes = preciseState.getApnSetting().getApnTypeBitmask();
            if ((apnTypes & 17) == 0 && (!InternalTelephonyUtils.shouldShowMmsDataIcon(phone) || (apnTypes & 2) == 0)) {
                return Integer.MAX_VALUE;
            }
            if (InternalTelephonyUtils.shouldShowMmsDataIcon(phone) && phone.getDataSettingsManager().isDataEnabled() && (apnTypes & 2) != 0 && (apnTypes & 17) == 0) {
                return Integer.MAX_VALUE;
            }
        }
        boolean isDualDataConnected = SystemProperties.getInt(MULTISIM_DUALDATA_STATUS, 0) == 1;
        int activeDataSubId = SubscriptionManager.getActiveDataSubscriptionId();
        boolean otherPhoneIsInCall = CallManager.getInstance(phone.getContext()).getState() != PhoneConstants.State.IDLE && CallManager.getInstance(phone.getContext()).getState(phone.getSubId()) == PhoneConstants.State.IDLE;
        boolean isDsdaFullOrTxsConcurrencyState = TelephonyManagerEx.getDefault().isDsdaFullOrTxsConcurrencyState();
        boolean isVoiceLinkRegistered = false;
        int otherSlotId = getOtherSlotId(phone.getPhoneId());
        if (SubscriptionManager.isValidPhoneId(otherSlotId)) {
            isVoiceLinkRegistered = TelephonyManagerEx.getDefault().isVoiceLinkRegistered(otherSlotId);
        }
        if (MiuiDataControllerImpl.Processor.isCurrentDSDAState() || isDsdaFullOrTxsConcurrencyState || isVoiceLinkRegistered || ((MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) && !isDataSuspended(phone))) {
            isDsdaOrDsdalike = true;
        }
        Rlog.d(TAG, "activeDataSubId = " + activeDataSubId + ", otherPhoneIsInCall = " + otherPhoneIsInCall + ", isDsdaFullOrTxsConcurrencyState = " + isDsdaFullOrTxsConcurrencyState + ", isVoiceLinkRegistered = " + isVoiceLinkRegistered + ", isDsdaOrDsdalike = " + isDsdaOrDsdalike + ", isDualDataConnected = " + isDualDataConnected + ", subId = " + phone.getSubId() + ", phoneId = " + phone.getPhoneId());
        if (!phone.getDataSettingsManager().isDataEnabled() || (otherPhoneIsInCall && !isDsdaOrDsdalike)) {
            if (!InternalTelephonyUtils.shouldShowMmsDataIcon(phone) || !isMmsNetworkConnected(phone) || otherPhoneIsInCall) {
                return 0;
            }
            return 2;
        }
        if ((!SubscriptionManager.isValidSubscriptionId(activeDataSubId) || phone.getSubId() != activeDataSubId) && !isDualDataConnected) {
            return 0;
        }
        boolean isForceSkipFromSst = MiuiNetworkControllerImpl.Processor.isSkipDataConnectionNotifier(phone.getPhoneId());
        if (isForceSkipFromSst && preciseState.getState() != 2) {
            Rlog.d(TAG, "skip notifyDataConnection() due to isForceSkipFromSst = " + isForceSkipFromSst + " , phoneId = " + phone.getPhoneId());
            return Integer.MAX_VALUE;
        }
        if (preciseState.getNetworkType() == 0 && ((!MiuiTelephony.IS_MTK && !MiuiTelephony.IS_XRING) || preciseState.getState() != 2)) {
            return 0;
        }
        boolean isInternetNetworkConnected = isInternetNetworkConnected(phone);
        boolean isDataSuspended = isDataSuspended(phone);
        Rlog.d(TAG, "isInternetNetworkConnected = " + isInternetNetworkConnected + ", isDataSuspended = " + isDataSuspended + ", phoneId = " + phone.getPhoneId());
        if (isDataSuspended && !isDsdaOrDsdalike) {
            return 3;
        }
        if (!isInternetNetworkConnected) {
            return 0;
        }
        return 2;
    }

    private boolean isInternetNetworkConnected(Phone phone) {
        MiuiDataControllerImpl dataNetworkImpl = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
        return (dataNetworkImpl == null || dataNetworkImpl.findActiveInternetDataNetwork() == null) ? false : true;
    }

    private boolean isDataSuspended(Phone phone) {
        DataNetwork dataNetwork;
        MiuiDataControllerImpl dataNetworkImpl = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
        return (dataNetworkImpl == null || (dataNetwork = dataNetworkImpl.findActiveInternetDataNetwork()) == null || !dataNetwork.isSuspended()) ? false : true;
    }

    private boolean isMmsNetworkConnected(Phone phone) {
        MiuiDataControllerImpl dataNetworkImpl = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
        return (dataNetworkImpl == null || dataNetworkImpl.getActiveDataNetworkByCapability(0) == null) ? false : true;
    }

    public void handleDelayVolteInCall(Phone phone) {
        if (phone == null || !SubscriptionManager.isValidPhoneId(phone.getPhoneId())) {
            return;
        }
        if (phone.getState() != PhoneConstants.State.IDLE) {
            mLastCallSlotId = phone.getPhoneId();
        }
        this.mHandler.removeMessages(3);
        if (phone.getState() != PhoneConstants.State.IDLE && mIsDelayUpdateVolte[phone.getPhoneId()]) {
            notifyDelayVolte(phone.getPhoneId(), false);
        } else if (isAllPhonesInIdle()) {
            this.mHandler.sendEmptyMessageDelayed(3, 5000L);
        }
    }

    public void updateVolteStatus(boolean isVolte, boolean isOldVolte, int phoneId) {
        if (mIsDelayUpdateVolte[phoneId] && isVolte) {
            mValidDelayVolteCount++;
            notifyDelayVolte(phoneId, false);
            return;
        }
        if (!mIsDelayUpdateVolte[phoneId] && isOldVolte && !isVolte) {
            if ((!isAllPhonesInIdle() && PhoneFactory.getPhone(phoneId).getState() == PhoneConstants.State.IDLE) || (this.mHandler.hasMessages(3) && mLastCallSlotId != -1 && mLastCallSlotId != phoneId)) {
                notifyDelayVolte(phoneId, true);
                mDelayVolteCount++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDelayVolte(int phoneId, boolean isDelay) {
        mIsDelayUpdateVolte[phoneId] = isDelay;
        notifyCallStateChange(phoneId, isDelay);
    }

    public static String getVolteDelayOptimizationCountInCall() {
        String result = "delayVolteCount = " + mDelayVolteCount + ", validDelayVolteCount = " + mValidDelayVolteCount;
        mValidDelayVolteCount = 0L;
        mDelayVolteCount = 0L;
        return result;
    }
}
