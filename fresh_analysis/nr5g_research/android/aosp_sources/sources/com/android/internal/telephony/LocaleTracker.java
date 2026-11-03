package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.telephony.CellInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.timezone.MobileCountries;
import android.timezone.TelephonyNetworkFinder;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class LocaleTracker extends Handler {
    public static final Set<String> TEST_MCCS = new HashSet<String>() { // from class: com.android.internal.telephony.LocaleTracker.1
        {
            add("001");
            add("002");
            add("003");
            add("004");
            add("005");
            add("006");
            add("007");
            add("008");
            add("009");
            add("010");
            add("011");
            add("012");
        }
    };
    private static TelephonyNetworkFinder sTelephonyNetworkFinder;
    private final BroadcastReceiver mBroadcastReceiver;
    private List mCellInfoList;
    private final CountryDetectionListener mCountryDetectionListener;
    private String mCountryOverride;
    private String mCurrentCountryIso;
    private int mFailCellInfoCount;
    private final FeatureFlags mFeatureFlags;
    private boolean mIsTracking;
    private MobileCountries mLastKnownMobileCountries;
    private int mLastServiceState;
    private final LocalLog mLocalLog;
    private final NitzStateMachine mNitzStateMachine;
    private String mOperatorNumeric;
    private final Phone mPhone;
    private int mSimState;
    private String mTag;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(String str) {
        synchronized (LocaleTracker.class) {
            try {
                if (Objects.equals(str, this.mCurrentCountryIso)) {
                    return;
                }
                String str2 = "updateLocale: Change the current country to \"" + str + "\", countryIsoDebugInfo = mCountryDetectionListener, mCellInfoList = " + this.mCellInfoList;
                log(str2);
                this.mLocalLog.log(str2);
                this.mCurrentCountryIso = str;
                updateLastKnownCountryIso(str);
                updateTelephonyProperties();
                updateTelephonyCountryDetector(str);
                sendBroadcastCountryIsoChanged(str);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                this.mPhone.requestCellInfoUpdate(null, obtainMessage(5));
                break;
            case 2:
                onServiceStateChanged((ServiceState) ((AsyncResult) message.obj).result);
                break;
            case 3:
                onSimCardStateChanged(message.arg1);
                break;
            case 4:
                processCellInfo((AsyncResult) message.obj);
                List list = this.mCellInfoList;
                if (list != null && list.size() > 0) {
                    requestNextCellInfo(true);
                    break;
                }
                break;
            case 5:
                processCellInfo((AsyncResult) message.obj);
                List list2 = this.mCellInfoList;
                requestNextCellInfo(list2 != null && list2.size() > 0);
                break;
            case 6:
                updateOperatorNumericImmediate("");
                updateTrackingStatus();
                break;
            case 7:
                this.mCountryOverride = (String) message.obj;
                updateLocale();
                break;
            default:
                LocaleTracker$$ExternalSyntheticBUOutline0.m("Unexpected message arrives. msg = ", message.what);
                break;
        }
    }

    public LocaleTracker(Phone phone, NitzStateMachine nitzStateMachine, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mLastServiceState = 3;
        this.mIsTracking = false;
        this.mLocalLog = new LocalLog(32, false);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.LocaleTracker.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(intent.getAction())) {
                    if (intent.getIntExtra("phone", 0) == LocaleTracker.this.mPhone.getPhoneId()) {
                        LocaleTracker.this.obtainMessage(3, intent.getIntExtra("android.telephony.extra.SIM_STATE", 0), 0).sendToTarget();
                    }
                } else if ("com.android.internal.telephony.action.COUNTRY_OVERRIDE".equals(intent.getAction())) {
                    String stringExtra = intent.getStringExtra("country");
                    if (intent.getBooleanExtra("reset", false)) {
                        stringExtra = null;
                    }
                    LocaleTracker.this.log("Received country override: " + stringExtra);
                    LocaleTracker.this.obtainMessage(7, stringExtra).sendToTarget();
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        CountryDetectionListener countryDetectionListener = new CountryDetectionListener() { // from class: com.android.internal.telephony.LocaleTracker$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.CountryDetectionListener
            public final void onCountryDetected(String str) {
                this.f$0.lambda$new$0(str);
            }
        };
        this.mCountryDetectionListener = countryDetectionListener;
        this.mPhone = phone;
        this.mNitzStateMachine = nitzStateMachine;
        this.mSimState = 0;
        this.mTag = LocaleTracker.class.getSimpleName() + "-" + phone.getPhoneId();
        this.mFeatureFlags = featureFlags;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        if (TelephonyUtils.IS_DEBUGGABLE) {
            intentFilter.addAction("com.android.internal.telephony.action.COUNTRY_OVERRIDE");
        }
        phone.getContext().registerReceiver(broadcastReceiver, intentFilter);
        phone.registerForServiceStateChanged(this, 2, null);
        phone.registerForCellInfo(this, 4, null);
        nitzStateMachine.registerCountryDetection(countryDetectionListener);
    }

    public String getCurrentCountry() {
        String str = this.mCurrentCountryIso;
        return str != null ? str : "";
    }

    private String getMccFromCellInfo() {
        String str = null;
        if (this.mCellInfoList != null) {
            HashMap map = new HashMap();
            Iterator it = this.mCellInfoList.iterator();
            int i = 0;
            while (it.hasNext()) {
                String mccString = ((CellInfo) it.next()).getCellIdentity().getMccString();
                if (mccString != null) {
                    int iIntValue = map.containsKey(mccString) ? 1 + ((Integer) map.get(mccString)).intValue() : 1;
                    map.put(mccString, Integer.valueOf(iIntValue));
                    if (iIntValue > i) {
                        str = mccString;
                        i = iIntValue;
                    }
                }
            }
        }
        return str;
    }

    private MccTable.MccMnc getMccMncFromCellInfo(String str) {
        MccTable.MccMnc mccMnc = null;
        if (this.mCellInfoList != null) {
            HashMap map = new HashMap();
            int i = 0;
            for (CellInfo cellInfo : this.mCellInfoList) {
                String mccString = cellInfo.getCellIdentity().getMccString();
                if (Objects.equals(mccString, str)) {
                    MccTable.MccMnc mccMnc2 = new MccTable.MccMnc(mccString, cellInfo.getCellIdentity().getMncString());
                    int iIntValue = map.containsKey(mccMnc2) ? 1 + ((Integer) map.get(mccMnc2)).intValue() : 1;
                    map.put(mccMnc2, Integer.valueOf(iIntValue));
                    if (iIntValue > i) {
                        i = iIntValue;
                        mccMnc = mccMnc2;
                    }
                }
            }
        }
        return mccMnc;
    }

    private void onSimCardStateChanged(int i) {
        this.mSimState = i;
        updateLocale();
        updateTrackingStatus();
    }

    private void onServiceStateChanged(ServiceState serviceState) {
        this.mLastServiceState = serviceState.getState();
        updateLocale();
        updateTrackingStatus();
    }

    public void updateOperatorNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            if (hasMessages(6)) {
                return;
            }
            sendMessageDelayed(obtainMessage(6), 600000L);
        } else {
            removeMessages(6);
            updateOperatorNumericImmediate(str);
        }
    }

    private void updateOperatorNumericImmediate(String str) {
        if (str.equals(this.mOperatorNumeric)) {
            return;
        }
        String str2 = "Operator numeric changes to \"" + str + "\"";
        log(str2);
        this.mLocalLog.log(str2);
        this.mOperatorNumeric = str;
        updateLocale();
    }

    private void processCellInfo(AsyncResult asyncResult) {
        if (asyncResult == null || asyncResult.exception != null) {
            this.mCellInfoList = null;
            return;
        }
        List list = (List) asyncResult.result;
        log("processCellInfo: cell info=" + list);
        this.mCellInfoList = list;
        updateLocale();
    }

    private void requestNextCellInfo(boolean z) {
        if (this.mIsTracking) {
            removeMessages(1);
            if (z) {
                resetCellInfoRetry();
                removeMessages(4);
                removeMessages(5);
                sendMessageDelayed(obtainMessage(1), 600000L);
                return;
            }
            int i = this.mFailCellInfoCount + 1;
            this.mFailCellInfoCount = i;
            long cellInfoDelayTime = getCellInfoDelayTime(i);
            log("Can't get cell info. Try again in " + (cellInfoDelayTime / 1000) + " secs.");
            sendMessageDelayed(obtainMessage(1), cellInfoDelayTime);
        }
    }

    public static long getCellInfoDelayTime(int i) {
        return Math.min(Math.max(((long) Math.pow(2.0d, Math.min(i, 30) - 1)) * 2000, 2000L), 600000L);
    }

    private void resetCellInfoRetry() {
        this.mFailCellInfoCount = 0;
        removeMessages(1);
    }

    private void updateTrackingStatus() {
        int i;
        if ((this.mSimState == 1 || TextUtils.isEmpty(this.mOperatorNumeric)) && ((i = this.mLastServiceState) == 1 || i == 2)) {
            startTracking();
        } else {
            stopTracking();
        }
    }

    private void stopTracking() {
        if (this.mIsTracking) {
            this.mIsTracking = false;
            log("Stopping LocaleTracker");
            this.mLocalLog.log("Stopping LocaleTracker");
            this.mCellInfoList = null;
            resetCellInfoRetry();
        }
    }

    private void startTracking() {
        if (this.mIsTracking) {
            return;
        }
        this.mLocalLog.log("Starting LocaleTracker");
        log("Starting LocaleTracker");
        this.mIsTracking = true;
        sendMessage(obtainMessage(1));
    }

    private synchronized void updateLocale() {
        MobileCountries mobileCountriesCreateForTest;
        MccTable.MccMnc mccMncFromCellInfo;
        MobileCountries mobileCountriesCreateTestCell;
        String str;
        String mccFromCellInfo;
        try {
            String str2 = "empty as default";
            MobileCountries mobileCountries = null;
            if (TextUtils.isEmpty(this.mOperatorNumeric)) {
                mobileCountriesCreateForTest = null;
                mccMncFromCellInfo = null;
            } else {
                mccMncFromCellInfo = MccTable.MccMnc.fromOperatorNumeric(this.mOperatorNumeric);
                if (mccMncFromCellInfo != null) {
                    mobileCountriesCreateForTest = getMobileCountriesFromNetworkId(mccMncFromCellInfo);
                    str2 = "OperatorNumeric(" + this.mOperatorNumeric + "): getMobileCountriesFromNetworkId(\"" + mccMncFromCellInfo + "\")";
                } else {
                    loge("updateLocale: Can't get country from operator numeric. mOperatorNumeric = " + this.mOperatorNumeric);
                    mobileCountriesCreateForTest = null;
                }
            }
            if (mobileCountriesCreateForTest == null && (mccFromCellInfo = getMccFromCellInfo()) != null) {
                mccMncFromCellInfo = getMccMncFromCellInfo(mccFromCellInfo);
                if (mccMncFromCellInfo != null) {
                    mobileCountriesCreateForTest = getMobileCountriesFromNetworkId(mccMncFromCellInfo);
                    str2 = "CellInfo: getMobileCountriesFromNetworkId(" + mccMncFromCellInfo + ")";
                } else {
                    mccMncFromCellInfo = new MccTable.MccMnc(mccFromCellInfo, null);
                    mobileCountriesCreateForTest = getMobileCountriesFromMcc(mccFromCellInfo);
                    str2 = "CellInfo: getMobileCountriesFromMcc(\"" + mccFromCellInfo + "\")";
                }
            }
            if (mccMncFromCellInfo != null && (str = this.mCountryOverride) != null) {
                mobileCountriesCreateForTest = MobileCountries.createForTest(mccMncFromCellInfo.mcc, mccMncFromCellInfo.mnc, Set.of(str), this.mCountryOverride);
                str2 = "mCountryOverride = \"" + this.mCountryOverride + "\"";
            }
            if (this.mPhone.isRadioOn()) {
                mobileCountries = mobileCountriesCreateForTest;
            } else {
                str2 = "radio off";
                this.mCellInfoList = null;
            }
            log("updateLocale: mobileCountries = " + mobileCountries + ", countryIsoDebugInfo = " + str2);
            boolean z = true;
            if (this.mCurrentCountryIso == null || !Objects.equals(mobileCountries, this.mLastKnownMobileCountries)) {
                String str3 = "updateLocale: Updating the mobile country list to \"" + mobileCountries + "\", countryIsoDebugInfo = " + str2 + ", mCellInfoList = " + this.mCellInfoList;
                log(str3);
                this.mLocalLog.log(str3);
                this.mLastKnownMobileCountries = mobileCountries;
                if (mobileCountries != null && mobileCountries.getCountryIsoCodes().size() == 1) {
                    this.mCountryDetectionListener.onCountryDetected(mobileCountries.getDefaultCountryIsoCode());
                } else {
                    this.mCountryDetectionListener.onCountryDetected("");
                }
            }
            if (TextUtils.isEmpty(this.mOperatorNumeric) || !this.mOperatorNumeric.startsWith("001")) {
                z = false;
                mobileCountriesCreateTestCell = mobileCountries;
            } else {
                mobileCountriesCreateTestCell = MobileCountries.createTestCell("001");
                str2 = "Test cell: " + this.mOperatorNumeric;
            }
            log("updateLocale: timeZoneCountryIso = " + mobileCountriesCreateTestCell + ", timeZoneCountryIsoDebugInfo = " + str2);
            if (mobileCountries == null && !z) {
                this.mNitzStateMachine.handleCountryUnavailable();
            } else {
                this.mNitzStateMachine.handleMobileCountriesDetected(mobileCountriesCreateTestCell);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public boolean isTracking() {
        return this.mIsTracking;
    }

    private void updateLastKnownCountryIso(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.mPhone.getContext().getSharedPreferences("last_known_country_iso", 0).edit();
        editorEdit.putString("last_known_country_iso", str);
        editorEdit.commit();
        log("update country iso in sharedPrefs " + str);
    }

    private void updateTelephonyProperties() {
        int phoneId = this.mPhone.getPhoneId();
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            ArrayList arrayList = new ArrayList(TelephonyProperties.operator_iso_country());
            while (arrayList.size() <= phoneId) {
                arrayList.add(null);
            }
            arrayList.set(phoneId, this.mCurrentCountryIso);
            TelephonyProperties.operator_iso_country(arrayList);
        }
    }

    private void updateTelephonyCountryDetector(String str) {
        TelephonyCountryDetector.getInstance(this.mPhone.getContext(), this.mFeatureFlags).onNetworkCountryCodeChanged(this.mPhone, str);
        HyperTelephonyFactory.getInstance();
        ((IHyperTimeZoneDetector) HyperTelephonyFactory.getModuleFromCache(this.mPhone.getPhoneId(), IHyperTimeZoneDetector.DEFAULT)).onNetworkCountryCodeChanged(str);
    }

    private void sendBroadcastCountryIsoChanged(String str) {
        Intent intent = new Intent("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        intent.putExtra("android.telephony.extra.NETWORK_COUNTRY", str);
        intent.putExtra("android.telephony.extra.LAST_KNOWN_NETWORK_COUNTRY", getLastKnownCountryIso());
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        this.mPhone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public String getLastKnownCountryIso() {
        return this.mPhone.getContext().getSharedPreferences("last_known_country_iso", 0).getString("last_known_country_iso", "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d(this.mTag, str);
    }

    private void loge(String str) {
        Rlog.e(this.mTag, str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        printWriter.println("LocaleTracker-" + this.mPhone.getPhoneId() + ":");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mIsTracking = " + this.mIsTracking);
        indentingPrintWriter.println("mOperatorNumeric = " + this.mOperatorNumeric);
        indentingPrintWriter.println("mSimState = " + this.mSimState);
        indentingPrintWriter.println("mCellInfoList = " + this.mCellInfoList);
        indentingPrintWriter.println("mCurrentCountryIso = " + this.mCurrentCountryIso);
        indentingPrintWriter.println("mLastKnownMobileCountries = " + this.mLastKnownMobileCountries);
        indentingPrintWriter.println("mFailCellInfoCount = " + this.mFailCellInfoCount);
        indentingPrintWriter.println("Local logs:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.flush();
    }

    public String getCountryOverride() {
        return this.mCountryOverride;
    }

    private static MobileCountries getMobileCountriesFromNetworkId(MccTable.MccMnc mccMnc) {
        MobileCountries mobileCountriesFromMccMnc = mccMnc.mnc != null ? getMobileCountriesFromMccMnc(mccMnc) : null;
        return mobileCountriesFromMccMnc == null ? getMobileCountriesFromMcc(mccMnc.mcc) : mobileCountriesFromMccMnc;
    }

    private static MobileCountries getMobileCountriesFromMccMnc(MccTable.MccMnc mccMnc) {
        TelephonyNetworkFinder telephonyNetworkFinder;
        synchronized (LocaleTracker.class) {
            try {
                telephonyNetworkFinder = sTelephonyNetworkFinder;
                if (telephonyNetworkFinder == null) {
                    telephonyNetworkFinder = TelephonyNetworkFinder.getInstance();
                    sTelephonyNetworkFinder = telephonyNetworkFinder;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (telephonyNetworkFinder == null) {
            return null;
        }
        return telephonyNetworkFinder.findCountriesByMccMnc(mccMnc.mcc, mccMnc.mnc);
    }

    private static MobileCountries getMobileCountriesFromMcc(String str) {
        TelephonyNetworkFinder telephonyNetworkFinder;
        synchronized (LocaleTracker.class) {
            try {
                telephonyNetworkFinder = sTelephonyNetworkFinder;
                if (telephonyNetworkFinder == null) {
                    telephonyNetworkFinder = TelephonyNetworkFinder.getInstance();
                    sTelephonyNetworkFinder = telephonyNetworkFinder;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (telephonyNetworkFinder == null) {
            return null;
        }
        return telephonyNetworkFinder.findCountriesByMcc(str);
    }
}
