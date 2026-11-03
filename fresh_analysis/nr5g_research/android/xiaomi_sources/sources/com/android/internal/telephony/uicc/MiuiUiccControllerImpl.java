package com.android.internal.telephony.uicc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.FdnUtils;
import com.android.internal.telephony.MiuiIccPhoneBookInterfaceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.MiuiTelephony;
import miui.telephony.ServiceProviderUtils;

/* JADX INFO: loaded from: classes.dex */
public class MiuiUiccControllerImpl implements MiuiUiccControllerStub.IMiuiUiccController {
    private static final String ACTION_ICC_REFRESH = "miui.intent.action.ACTION_ICC_REFRESH";
    private static final String AID = "aid";
    private static final String EFID = "efid";
    private static final int EF_ADN_SET1 = 20282;
    private static final int EF_ADN_SET2 = 20283;
    private static final int EVENT_UPDATE_DISPLAY_NAME = 1;
    private static final String LOG_TAG = "MiuiUiccControllerImpl";
    private static final String PHONE_ID = "phone_id";
    private static final String REFRESH_RESULT = "refresh_result";
    private static final String SIM_CONTACT_INIT_RECEIVER = "com.android.providers.contacts/com.miui.providers.contacts.sim.SimContactInitReceiver";
    private Context mContext;
    private MiuiIccPhoneBookInterfaceManager mMiuiIccPhoneBookMgr;
    private Phone mPhone;
    private TelephonyManager mTelephonyManager;
    private static final String[] MCCMNC_CODES_HAVING_2DIGITS_MNC = {"46002"};
    private static String[][] sSpnOverrideArr = {new String[]{"45406", "SmarTone HK"}, new String[]{"46605", "GT 4G"}, new String[]{"52505", "StarHub"}};
    private MiuiAdnRecordCache[] mMiuiAdnRecordCaches = new MiuiAdnRecordCache[8];
    private int mPhoneId = -1;
    private Handler mHandler = new Handler() { // from class: com.android.internal.telephony.uicc.MiuiUiccControllerImpl.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MiuiUiccControllerImpl.this.updateDisplayNameByLocaleChange();
                    MiuiUiccControllerImpl.this.updateNetworkNameByLocaleChange();
                    break;
            }
        }
    };
    private final BroadcastReceiver mLocaleChangedReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.uicc.MiuiUiccControllerImpl.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.LOCALE_CHANGED")) {
                MiuiUiccControllerImpl.this.mHandler.sendMessage(MiuiUiccControllerImpl.this.mHandler.obtainMessage(1));
            }
        }
    };

    public void init(Phone phone, Object... arg) {
        int phoneId = phone.getPhoneId();
        this.mContext = phone.getContext();
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        if (isValidPhoneId(phoneId)) {
            this.mPhone = phone;
            this.mPhoneId = phoneId;
            this.mMiuiIccPhoneBookMgr = new MiuiIccPhoneBookInterfaceManager(phone);
        }
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, phone.getPhoneId());
        this.mContext.registerReceiver(this.mLocaleChangedReceiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"), 2);
        logi(phoneId, "init MiuiUiccControllerImpl");
    }

    public void dispose(Phone phone) {
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, phone.getPhoneId());
        this.mContext.unregisterReceiver(this.mLocaleChangedReceiver);
    }

    public void updateIccRecords(int phoneId, IccRecords records) {
        IccCardApplicationStatus.AppType appType;
        logi(phoneId, "update icc records");
        if (isValidPhoneId(phoneId)) {
            if (records == null) {
                this.mMiuiIccPhoneBookMgr.updateIccRecords(records);
                return;
            }
            Phone phone = PhoneFactory.getPhone(phoneId);
            UiccCardApplication uiccCardApp = getUiccCardApplication();
            if (phone != null && uiccCardApp != null && (appType = phone.getCurrentUiccAppType()) != IccCardApplicationStatus.AppType.APPTYPE_ISIM && appType != IccCardApplicationStatus.AppType.APPTYPE_UNKNOWN) {
                initIccRecord(phoneId, uiccCardApp, phone.getIccFileHandler(), new Object[0]);
            }
            this.mMiuiIccPhoneBookMgr.updateIccRecords(records);
        }
    }

    public void initIccRecord(int phoneId, UiccCardApplication app, IccFileHandler fh, Object... arg) {
        if (app == null || fh == null) {
            return;
        }
        int appIndex = getApplicationIndex(app);
        if (isValidPhoneId(phoneId) && isValidApplicationIndex(appIndex) && this.mMiuiAdnRecordCaches[appIndex] == null) {
            this.mMiuiAdnRecordCaches[appIndex] = new MiuiAdnRecordCache(fh);
        }
    }

    public ArrayList<AdnRecord> getRecordsIfLoaded(Phone phone, IccRecords iccRecords, int efid) {
        ArrayList<MiuiAdnRecord> miuiAdnRecords;
        MiuiAdnRecordCache adnRecordCache = getMiuiAdnCache(phone, iccRecords);
        if (adnRecordCache == null || (miuiAdnRecords = adnRecordCache.getRecordsIfLoaded(efid)) == null || miuiAdnRecords.size() == 0) {
            return null;
        }
        ArrayList<AdnRecord> adnRecords = new ArrayList<>();
        for (MiuiAdnRecord miuiAdnRecord : miuiAdnRecords) {
            adnRecords.add(miuiAdnRecord.getAdnRecord());
        }
        return adnRecords;
    }

    public MiuiAdnRecordCache getMiuiAdnCache(Phone phone, IccRecords records) {
        if (phone == null || records == null) {
            return null;
        }
        int phoneId = phone.getPhoneId();
        int appIndex = getApplicationIndex(records.mParentApp);
        logi(phoneId, "getMiuiAdnCache phoneId: " + phoneId + ", appIndex: " + appIndex + ", records: " + records);
        if (!isValidPhoneId(phoneId) || !isValidApplicationIndex(appIndex)) {
            return null;
        }
        return this.mMiuiAdnRecordCaches[appIndex];
    }

    public void onSimRefresh(int phoneId, UiccProfile uiccprofile, UiccSlot uiccSlot) {
        if (uiccprofile != null && uiccSlot != null && uiccSlot.isEuicc()) {
            logi(phoneId, "onSimRefresh isEuicc");
            uiccprofile.mLastReportedNumOfUiccApplications = 0;
        }
    }

    public void resetIccRecord(int phoneId, UiccCardApplication app) {
        if (app == null) {
            return;
        }
        int appIndex = getApplicationIndex(app);
        if (isValidPhoneId(phoneId) && isValidApplicationIndex(appIndex) && this.mMiuiAdnRecordCaches[appIndex] != null) {
            this.mMiuiAdnRecordCaches[appIndex].reset();
            this.mMiuiAdnRecordCaches[appIndex] = null;
        }
    }

    public void disposeIccRecord(int phoneId, UiccCardApplication app) {
        if (app == null) {
            return;
        }
        int appIndex = getApplicationIndex(app);
        if (isValidPhoneId(phoneId) && isValidApplicationIndex(appIndex) && this.mMiuiAdnRecordCaches[appIndex] != null) {
            this.mMiuiAdnRecordCaches[appIndex].dispose();
        }
    }

    public int getMncLength(int phoneId, String imsi, String[] mccmncCodesArray) {
        log(phoneId, "getOperatorNumeric: setting mMncLength = 2");
        if (imsi.length() < 6) {
            return 2;
        }
        String mccmncCode = imsi.substring(0, 6);
        log(phoneId, "getOperatorNumeric: mccmncCode = " + mccmncCode);
        for (String mccmnc : mccmncCodesArray) {
            if (mccmnc.equals(mccmncCode)) {
                log(phoneId, "getOperatorNumeric: setting mMncLength = 3 ");
                return 3;
            }
        }
        return 2;
    }

    public int getRecoveryMncLength(int phoneId, String imsi, int mncLength) {
        if (mncLength == 3 && imsi.length() >= 6) {
            String mccmncCode = imsi.substring(0, 6);
            for (String mccmnc : MCCMNC_CODES_HAVING_2DIGITS_MNC) {
                if (mccmncCode.startsWith(mccmnc)) {
                    log(phoneId, "getRecoveryMncLength: setting mMncLength = 2 ");
                    return 2;
                }
            }
            return mncLength;
        }
        return mncLength;
    }

    public String resetIccid(int phoneId, String iccid, byte[] data) {
        return miui.telephony.TelephonyManager.isCustForKrOps() ? iccid : IccUtils.bchToString(data, 0, data.length);
    }

    private int getApplicationIndex(UiccCardApplication app) {
        return app.getUiccProfile().getSubscriptionAppIndex(app.getType());
    }

    private boolean isValidPhoneId(int phoneId) {
        return SubscriptionManager.isValidPhoneId(phoneId);
    }

    private boolean isValidApplicationIndex(int index) {
        return index >= 0 && index < 8;
    }

    private void log(int phoneId, String msg) {
        Rlog.d(LOG_TAG, "[" + phoneId + "] " + msg);
    }

    private void loge(int phoneId, String msg) {
        Rlog.e(LOG_TAG, "[" + phoneId + "] " + msg);
    }

    private void logi(int phoneId, String msg) {
        Rlog.i(LOG_TAG, "[" + phoneId + "] " + msg);
    }

    public MiuiIccPhoneBookInterfaceManager getMiuiIccPhoneBookInterfaceManager() {
        return this.mMiuiIccPhoneBookMgr;
    }

    public boolean mvnoMatchesCustomized(int phoneId, IccRecords r, String mvnoType, String mvnoMatchData) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "is_match_customized_mvno", false);
    }

    private UiccCardApplication getUiccCardApplication() {
        if (this.mPhone.getPhoneType() == 1) {
            return UiccController.getInstance().getUiccCardApplication(this.mPhone.getPhoneId(), 1);
        }
        return UiccController.getInstance().getUiccCardApplication(this.mPhone.getPhoneId(), 2);
    }

    public String onOperatorNumericOrNameSet(int slotId, String prop, String value) {
        return ServiceProviderUtils.onOperatorNumericOrNameSet(slotId, prop, value);
    }

    public void setTelephonyProperty(int slotId, String prop, String value) {
        ServiceProviderUtils.setTelephonyProperty(slotId, prop, value);
    }

    public String overrideSpnFromConfig(int phoneId, String carrierName, IccRecords records) {
        return MiuiUiccCommonUtils.overrideSpnFromConfig(phoneId, carrierName, records);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayNameByLocaleChange() {
        boolean zIsValidPhoneId = isValidPhoneId(this.mPhoneId);
        int i = this.mPhoneId;
        if (!zIsValidPhoneId) {
            log(i, "updateDisplayNameByLocaleChange is not valid");
            return;
        }
        int subId = SubscriptionManager.getSubscriptionId(i);
        SubscriptionInfo subInfo = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfo(subId);
        if (subInfo == null || TextUtils.isEmpty(subInfo.getDisplayName())) {
            log(this.mPhoneId, "subInfo is null or displayName is empty");
            return;
        }
        if (subInfo.getDisplayNameSource() == 2) {
            log(this.mPhoneId, "name source is NAME_SOURCE_USER_INPUT");
            return;
        }
        if (subInfo.getDisplayNameSource() < 0 || subInfo.getDisplayNameSource() > 4) {
            log(this.mPhoneId, "name source is invalid");
            return;
        }
        String displayName = subInfo.getDisplayName().toString();
        String newDisplayName = onOperatorNumericOrNameSet(this.mPhoneId, "gsm.sim.operator.alpha", displayName);
        if (TextUtils.equals(displayName, newDisplayName)) {
            log(this.mPhoneId, "displayName has not changed.");
        } else {
            this.mTelephonyManager.setSimOperatorNameForPhone(this.mPhoneId, newDisplayName);
            SubscriptionManager.from(this.mContext).setDisplayName(newDisplayName, subId, subInfo.getDisplayNameSource());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkNameByLocaleChange() {
        if (!isValidPhoneId(this.mPhoneId)) {
            log(this.mPhoneId, "updateNetworkNameByLocaleChange is not valid");
            return;
        }
        String operatorAlpha = this.mPhone.getServiceState().getOperatorAlpha();
        if (!TextUtils.isEmpty(operatorAlpha)) {
            String value = onOperatorNumericOrNameSet(this.mPhoneId, "gsm.operator.alpha", operatorAlpha);
            this.mTelephonyManager.setNetworkOperatorNameForPhone(this.mPhoneId, value);
        }
    }

    public boolean resetIccRecordFileUpdate(int phoneId, UiccCardApplication app, int efid) {
        if (app == null) {
            return false;
        }
        boolean resetIccRecord = HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "config_reset_icc_record", false);
        if (resetIccRecord && (efid == 28474 || efid == 20272 || efid == 28489 || efid == EF_ADN_SET1 || efid == EF_ADN_SET2)) {
            int appIndex = getApplicationIndex(app);
            if (isValidPhoneId(phoneId) && isValidApplicationIndex(appIndex) && this.mMiuiAdnRecordCaches[appIndex] != null) {
                this.mMiuiAdnRecordCaches[appIndex].reset();
                return true;
            }
        }
        return false;
    }

    public void broadcastIccRefresh(int phoneId, IccRefreshResponse iccRefreshState) {
        boolean broadcastIccRefresh = HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "config_broadcast_icc_refresh", false);
        if (broadcastIccRefresh) {
            Intent intent = new Intent(ACTION_ICC_REFRESH);
            intent.putExtra("phone_id", phoneId);
            intent.putExtra(REFRESH_RESULT, iccRefreshState.refreshResult);
            intent.putExtra(EFID, iccRefreshState.efId);
            intent.putExtra(AID, iccRefreshState.aid);
            log(phoneId, "broadcastIccRefresh with refreshResult: " + iccRefreshState.refreshResult + ",efId: " + iccRefreshState.efId + ",aid: " + iccRefreshState.aid);
            intent.setComponent(ComponentName.unflattenFromString(SIM_CONTACT_INIT_RECEIVER));
            this.mContext.sendBroadcast(intent);
        }
    }

    public boolean isNumberBlockedByFDNMiui(int phoneId, String dialStr, String defaultCountryIso) {
        IccRecords iccRecords;
        try {
            Phone phone = PhoneFactory.getPhone(phoneId);
            if (TelephonyBaseUtilsStub.isMiuiRom() && phone != null && FdnUtils.isFdnEnabled(phoneId)) {
                Rlog.i(LOG_TAG, "miui fdn check");
                UiccCardApplication app = getUiccCardApplication(phoneId);
                if (app == null || (iccRecords = app.getIccRecords()) == null) {
                    return false;
                }
                ArrayList<AdnRecord> fdnList = getRecordsIfLoaded(phone, iccRecords, 28475);
                boolean res = !FdnUtils.isFDN(dialStr, defaultCountryIso, fdnList);
                Rlog.i(LOG_TAG, "miui fdn blocked:" + res);
                return res;
            }
            return false;
        } catch (IllegalStateException ex1) {
            Rlog.e(LOG_TAG, "isNumberBlockedByFDNMiui: " + ex1);
            return false;
        } catch (Exception ex2) {
            Rlog.e(LOG_TAG, "isNumberBlockedByFDNMiui: " + ex2);
            return false;
        }
    }

    private static UiccCardApplication getUiccCardApplication(int phoneId) {
        UiccProfile uiccProfile = UiccController.getInstance().getUiccProfileForPhone(phoneId);
        if (uiccProfile == null) {
            return null;
        }
        return uiccProfile.getApplication(1);
    }

    public String lookupOperatorName(String numeric, int phoneId, boolean desireLongName) {
        SpnOverride spnOverride = SpnOverride.getInstance();
        String operName = ServiceProviderUtils.get(numeric, phoneId, desireLongName);
        Rlog.d(LOG_TAG, "lookupOperatorName, ServiceProviderUtils operName = " + operName);
        if (operName == null && spnOverride != null && spnOverride.containsCarrier(numeric)) {
            operName = spnOverride.getSpn(numeric);
            Rlog.d(LOG_TAG, "lookupOperatorName, SpnOverride operName = " + operName);
        }
        if (TextUtils.isEmpty(operName)) {
            for (String[] numeric_spn : sSpnOverrideArr) {
                if (numeric_spn[0].equals(numeric)) {
                    return numeric_spn[1];
                }
            }
        }
        return operName;
    }
}
