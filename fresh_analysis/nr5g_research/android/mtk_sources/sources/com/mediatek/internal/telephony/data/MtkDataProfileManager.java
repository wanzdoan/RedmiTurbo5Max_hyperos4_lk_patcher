package com.mediatek.internal.telephony.data;

import android.R;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Telephony;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataProfile;
import android.telephony.data.TrafficDescriptor;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataProfileManager;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.MiuiDataControllerStub;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.uicc.MtkIccUtilsEx;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import mediatek.telephony.MtkTelephony;

/* JADX INFO: loaded from: classes.dex */
public class MtkDataProfileManager extends DataProfileManager {
    private static final int BASE = 100;
    private static final boolean DBG = true;
    private static final int EVENT_CHECK_FDN_LIST = 105;
    private static final int EVENT_FDN_CHANGED = 103;
    private static final int EVENT_FDN_ENABLE_CHANGED = 106;
    private static final int EVENT_ICC_CHANGED = 101;
    private static final int EVENT_PLMN_DATA = 102;
    private static final int EVENT_RADIO_NOT_AVAILABLE = 107;
    private static final int EVENT_RECORDS_OVERRIDE = 104;
    private static final String FDN_CONTENT_URI = "content://icc/fdn";
    private static final String FDN_CONTENT_URI_WITH_SUB_ID = "content://icc/fdn/subId/";
    private static final String FDN_FOR_ALLOW_DATA = "*99#";
    private static final String LOG_TAG = "MtkDPM";
    private static final int MAX_COUNT_DATA_PROFILE = 16;
    private static final boolean VDBG = Build.IS_ENG;
    private static final boolean mLimitDataProfilesCapability = MtkDataHelper.hasLimitDataProfilesCapability();
    private String mCurrentOperatorNumeric;
    private boolean mFdnConstraint;
    private IccRecords mIccRecords;
    private final BroadcastReceiver mIntentReceiverEx;
    private boolean mIsFdnChecked;
    private boolean mIsFdnEnableStateChangedRegistered;
    private boolean mIsMatchFdnForAllowData;
    private boolean mIsPhbStateChangedIntentRegistered;
    private boolean mIsRecordsOverride;
    private int mMtuFromResource;
    private final DpmOnSubscriptionsChangedListener mOnSubscriptionsChangedListener;
    private BroadcastReceiver mPhbStateChangedIntentReceiver;
    private int mPhoneType;
    private PlmnMvnoData mPlmnMvnoData;
    private final SettingsObserver mSettingsObserver;
    private SubscriptionManager mSubscriptionManager;
    private Handler mWorkerHandler;

    private class DpmOnSubscriptionsChangedListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        private final AtomicInteger mCurrentSubId;

        private DpmOnSubscriptionsChangedListener() {
            this.mCurrentSubId = new AtomicInteger(-1);
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            int subId = MtkDataProfileManager.this.mPhone.getSubId();
            MtkDataProfileManager.this.log("onSubscriptionsChanged: subId = " + subId + ", mPreviousSubId = " + this.mCurrentSubId);
            boolean zIsValidSubscriptionId = SubscriptionManager.isValidSubscriptionId(subId);
            AtomicInteger atomicInteger = this.mCurrentSubId;
            if (zIsValidSubscriptionId) {
                if (atomicInteger.getAndSet(subId) != subId) {
                    MtkDataProfileManager.this.onSubIdReady();
                }
            } else if (SubscriptionManager.isValidSubscriptionId(atomicInteger.get())) {
                this.mCurrentSubId.set(-1);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkDataProfileManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, Looper looper, FeatureFlags featureFlags, DataProfileManager.DataProfileManagerCallback callback) {
        super(phone, dataNetworkController, dataServiceManager, looper, featureFlags, callback);
        this.mPhoneType = 0;
        this.mIccRecords = null;
        this.mIsRecordsOverride = false;
        this.mCurrentOperatorNumeric = null;
        this.mPlmnMvnoData = null;
        this.mMtuFromResource = 0;
        this.mFdnConstraint = false;
        this.mOnSubscriptionsChangedListener = new DpmOnSubscriptionsChangedListener();
        this.mIntentReceiverEx = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                MtkDataProfileManager.this.log("mIntentReceiverEx onReceive: action=" + action);
                int phoneId = intent.getIntExtra("phone", -1);
                if (phoneId != MtkDataProfileManager.this.mPhone.getPhoneId()) {
                    return;
                }
                if (action.equals("android.intent.action.RADIO_TECHNOLOGY")) {
                    String operatorNumeric = MtkDataProfileManager.this.mtkGetOperatorNumeric();
                    MtkDataProfileManager.this.log("ACTION_RADIO_TECHNOLOGY_CHANGED: mCurrentOperatorNumeric = " + MtkDataProfileManager.this.mCurrentOperatorNumeric + ", curOperatorNumeric = " + operatorNumeric);
                    if (!TextUtils.isEmpty(MtkDataProfileManager.this.mCurrentOperatorNumeric) && !TextUtils.isEmpty(operatorNumeric) && !TextUtils.equals(MtkDataProfileManager.this.mCurrentOperatorNumeric, operatorNumeric)) {
                        MtkDataProfileManager.this.onSubIdReady();
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    String simState = intent.getStringExtra("ss");
                    if ("READY".equals(simState) || "LOADED".equals(simState)) {
                        MtkDataProfileManager.this.registerForFdnEnableChanged();
                        return;
                    } else {
                        MtkDataProfileManager.this.unregisterForFdnEnableChanged();
                        return;
                    }
                }
                boolean zEquals = action.equals("android.telephony.action.CARRIER_CONFIG_CHANGED");
                MtkDataProfileManager mtkDataProfileManager = MtkDataProfileManager.this;
                if (zEquals) {
                    if (mtkDataProfileManager.mPhone.getPhoneId() != intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1) || intent.getBooleanExtra("android.telephony.extra.REBROADCAST_ON_UNLOCK", false)) {
                        return;
                    }
                    MtkDataProfileManager.this.registerForFdnEnableChanged();
                    MtkDataProfileManager.this.registerFdnContentObserver();
                    return;
                }
                mtkDataProfileManager.log("onReceive: Unknown action=" + action);
            }
        };
        this.mIsFdnChecked = false;
        this.mIsMatchFdnForAllowData = false;
        this.mIsPhbStateChangedIntentRegistered = false;
        this.mIsFdnEnableStateChangedRegistered = false;
        this.mPhbStateChangedIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                MtkDataProfileManager.this.log("onReceive: action=" + action);
                if (action.equals("mediatek.intent.action.PHB_STATE_CHANGED")) {
                    boolean bPhbReady = intent.getBooleanExtra("ready", false);
                    MtkDataProfileManager.this.log("bPhbReady: " + bPhbReady);
                    if (bPhbReady) {
                        MtkDataProfileManager.this.onFdnChanged();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.RADIO_TECHNOLOGY");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mPhone.getContext().registerReceiver(this.mIntentReceiverEx, filter, null, this.mPhone, 2);
        this.mSubscriptionManager = SubscriptionManager.from(this.mPhone.getContext());
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
        UiccController.getInstance().registerForIccChanged(this, 101, (Object) null);
        createWorkerHandler();
        this.mSettingsObserver = new SettingsObserver(this.mPhone.getContext(), this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void registerAllEvents() {
        super.registerAllEvents();
        this.mPhone.mCi.registerForPlmnData(this, 102, null);
        this.mPhone.mCi.registerForNotAvailable(this, EVENT_RADIO_NOT_AVAILABLE, (Object) null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void handleMessage(Message msg) {
        if (VDBG) {
            log("handleMessage msg=" + msg);
        }
        switch (msg.what) {
            case 101:
                IccRecords newIccRecords = UiccController.getInstance().getIccRecords(this.mPhone.getPhoneId(), 1);
                if (this.mIccRecords != newIccRecords) {
                    if (this.mIccRecords != null) {
                        log("Removing stale icc objects.");
                        this.mIccRecords.unregisterForRecordsOverride(this);
                        this.mIccRecords = null;
                    }
                    if (newIccRecords != null) {
                        log("new Icc object");
                        this.mIsRecordsOverride = false;
                        newIccRecords.registerForRecordsOverride(this, 104, (Object) null);
                        this.mIccRecords = newIccRecords;
                        return;
                    }
                    return;
                }
                return;
            case 102:
                log("EVENT_PLMN_DATA");
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar != null && ar.result != null) {
                    PlmnMvnoData tempPlmnMvnoData = (PlmnMvnoData) ar.result;
                    boolean changed = isPlmnMvnoChanged(this.mPlmnMvnoData, tempPlmnMvnoData);
                    this.mPlmnMvnoData = tempPlmnMvnoData;
                    log("mPlmnMvnoData = " + this.mPlmnMvnoData + " changed = " + changed);
                    if (changed && SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
                        onSubIdReady();
                        return;
                    }
                    return;
                }
                return;
            case 103:
                break;
            case 104:
                log("EVENT_RECORDS_OVERRIDE");
                this.mIsRecordsOverride = DBG;
                return;
            case 105:
            default:
                super.handleMessage(msg);
                return;
            case EVENT_FDN_ENABLE_CHANGED /* 106 */:
                log("EVENT_FDN_ENABLE_CHANGED");
                break;
            case EVENT_RADIO_NOT_AVAILABLE /* 107 */:
                log("EVENT_RADIO_NOT_AVAILABLE");
                reset();
                return;
        }
        onFdnChanged();
    }

    protected boolean isApnQueryable() {
        if (isFrameworkApnQueryable()) {
            return DBG;
        }
        String operator = mtkGetOperatorNumeric();
        if (SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId()) && !TextUtils.isEmpty(operator)) {
            return DBG;
        }
        log("isApnQueryable: ignore sim not ready no operator numeric");
        return false;
    }

    protected Cursor getApnListCursor() {
        if (isFrameworkApnQueryable()) {
            log("getApnListCursor, carrier ok use original flow.");
            return super.getApnListCursor();
        }
        String mccmnc = mtkGetOperatorNumeric();
        if (mccmnc == null) {
            log("getApnListCursor, mccmnc is null!");
            return null;
        }
        String selection = "numeric = '" + mccmnc + "'";
        log("getApnListCursor: selection=" + selection);
        Cursor ret = this.mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "filtered"), null, selection, null, "_id");
        if (ret == null) {
            loge("getApnListCursor: cursor is null!");
            return null;
        }
        String[] columnNames = ret.getColumnNames();
        MatrixCursor mvnoCursor = new MatrixCursor(columnNames);
        MatrixCursor mnoCursor = new MatrixCursor(columnNames);
        int numericIndex = ret.getColumnIndex(MtkTelephony.Carriers.NUMERIC);
        int mvnoIndex = ret.getColumnIndex(MtkTelephony.Carriers.MVNO_TYPE);
        int mvnoDataIndex = ret.getColumnIndex(MtkTelephony.Carriers.MVNO_MATCH_DATA);
        while (ret.moveToNext()) {
            List<String> data = new ArrayList<>();
            boolean isMNOAPN = false;
            for (String column : columnNames) {
                data.add(ret.getString(ret.getColumnIndex(column)));
            }
            boolean isCurrentSimOperator = false;
            if (!TextUtils.isEmpty(ret.getString(numericIndex)) && !TextUtils.isEmpty(ret.getString(mvnoIndex)) && !TextUtils.isEmpty(ret.getString(mvnoDataIndex))) {
                isCurrentSimOperator = ret.getString(numericIndex).equals(mccmnc) && isMvnoMatches(ApnSetting.getMvnoTypeIntFromString(ret.getString(mvnoIndex)), ret.getString(mvnoDataIndex));
            }
            boolean isMVNOAPN = isCurrentSimOperator;
            if (!TextUtils.isEmpty(ret.getString(numericIndex)) && ret.getString(numericIndex).equals(mccmnc) && TextUtils.isEmpty(ret.getString(mvnoIndex))) {
                isMNOAPN = true;
            }
            if (isMVNOAPN) {
                mvnoCursor.addRow(data);
            } else if (isMNOAPN) {
                mnoCursor.addRow(data);
            }
        }
        ret.close();
        if (mvnoCursor.getCount() > 0) {
            log("match MVNO APN: " + mvnoCursor.getCount());
            mnoCursor.close();
            return mvnoCursor;
        }
        if (mnoCursor.getCount() > 0) {
            log("match MNO APN: " + mnoCursor.getCount());
            mvnoCursor.close();
            return mnoCursor;
        }
        mvnoCursor.close();
        log("getApnListCursor: filtered cursor is empty.");
        return mnoCursor;
    }

    protected void finalizeDataProfiles() {
        this.mCurrentOperatorNumeric = mtkGetOperatorNumeric();
        sortAndLimitDataProfiles();
    }

    protected void appendExtraDataProfiles(List<DataProfile> profiles) {
        for (DataProfile dp : profiles) {
            ApnSetting apn = dp.getApnSetting();
            if (apn != null) {
                apn.setPersistent(DBG);
                if (apn.canHandleType(17) && ExternalSimManager.isNonDsdaRemoteSimSupport()) {
                    apn.setApnTypeBitmask(apn.getApnTypeBitmask() | 4096);
                }
            }
        }
        DataProfile dataProfile = profiles.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataProfile) obj).canSatisfy(10);
            }
        }).findFirst().orElse(null);
        if (dataProfile == null) {
            profiles.add(new DataProfile.Builder().setApnSetting(buildDefaultApnSetting("DEFAULT EIMS", "", 512)).setTrafficDescriptor(new TrafficDescriptor("", null)).build());
            log("Added default EIMS data profile.");
        }
        DataProfile dataProfile2 = profiles.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataProfile) obj).canSatisfy(4);
            }
        }).findFirst().orElse(null);
        if (dataProfile2 == null) {
            profiles.add(new DataProfile.Builder().setApnSetting(buildDefaultApnSetting("DEFAULT IMS", "ims", 64)).setTrafficDescriptor(new TrafficDescriptor("ims", null)).build());
            log("Added default IMS data profile.");
        }
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).addIAApnSetting(this.mPhone, mtkGetOperatorNumeric(), profiles);
    }

    protected ApnSetting buildDefaultApnSetting(String entry, String apn, int apnTypeBitmask) {
        return new ApnSetting.Builder().setEntryName(entry).setProtocol(2).setRoamingProtocol(2).setApnName(apn).setApnTypeBitmask(apnTypeBitmask).setCarrierEnabled(DBG).setApnSetId(-1).buildWithoutCheck();
    }

    private boolean isPlmnMvnoChanged(PlmnMvnoData oldValue, PlmnMvnoData newValue) {
        if (oldValue != null && newValue != null && TextUtils.equals(oldValue.getGsmNumeric(), newValue.getGsmNumeric()) && TextUtils.equals(oldValue.getCdmaNumeric(), newValue.getCdmaNumeric()) && TextUtils.equals(oldValue.getGsmSpn(), newValue.getGsmSpn()) && TextUtils.equals(oldValue.getCdmaSpn(), newValue.getCdmaSpn()) && TextUtils.equals(oldValue.getGsmImsi(), newValue.getGsmImsi()) && TextUtils.equals(oldValue.getCdmaImsi(), newValue.getCdmaImsi()) && TextUtils.equals(oldValue.getGid1(), newValue.getGid1()) && TextUtils.equals(oldValue.getImpi(), newValue.getImpi())) {
            return false;
        }
        return DBG;
    }

    private boolean updatePhoneType() {
        int tempPhoneType = this.mPhoneType;
        this.mPhoneType = 1;
        if (MtkDataHelper.isCdma3GCard(this.mPhone.getPhoneId())) {
            this.mPhoneType = 2;
        }
        if (MtkDataHelper.isCdma3GDualModeCard(this.mPhone.getPhoneId())) {
            this.mPhoneType = this.mPhone.getPhoneType();
        }
        if (tempPhoneType != this.mPhoneType) {
            return DBG;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String mtkGetOperatorNumeric() {
        String operatorNumeric;
        String aospNumeric = this.mPhone.getOperatorNumeric();
        if (this.mDataConfigManager.isConfigCarrierSpecific() && !TextUtils.isEmpty(aospNumeric)) {
            log("mtkGetOperatorNumeric: carrier config is ready return " + aospNumeric);
            return aospNumeric;
        }
        int i = this.mPhoneType;
        PlmnMvnoData plmnMvnoData = this.mPlmnMvnoData;
        if (i == 2) {
            operatorNumeric = plmnMvnoData != null ? this.mPlmnMvnoData.getCdmaNumeric() : "";
        } else {
            operatorNumeric = plmnMvnoData != null ? this.mPlmnMvnoData.getGsmNumeric() : "";
        }
        log("mtkGetOperatorNumeric: phone type = " + this.mPhoneType + " [1:GSM,2:CDMA], operator from phone = " + aospNumeric + ", operator from RIL = " + operatorNumeric);
        if (TextUtils.isEmpty(operatorNumeric) && !TextUtils.isEmpty(aospNumeric)) {
            log("mtkGetOperatorNumeric: maybe wrong phone type, update it");
            if (updatePhoneType()) {
                int i2 = this.mPhoneType;
                PlmnMvnoData plmnMvnoData2 = this.mPlmnMvnoData;
                if (i2 == 2) {
                    String operatorNumeric2 = plmnMvnoData2 != null ? this.mPlmnMvnoData.getCdmaNumeric() : "";
                    return operatorNumeric2;
                }
                String operatorNumeric3 = plmnMvnoData2 != null ? this.mPlmnMvnoData.getGsmNumeric() : "";
                return operatorNumeric3;
            }
            return operatorNumeric;
        }
        return operatorNumeric;
    }

    public String getImsi() {
        if (this.mIsRecordsOverride || this.mPlmnMvnoData == null) {
            return null;
        }
        int i = this.mPhoneType;
        PlmnMvnoData plmnMvnoData = this.mPlmnMvnoData;
        if (i == 1) {
            return plmnMvnoData.getGsmImsi();
        }
        return plmnMvnoData.getCdmaImsi();
    }

    public String getIccid() {
        MtkUiccController ctrl = (MtkUiccController) UiccController.getInstance();
        return ctrl != null ? ctrl.getIccid(this.mPhone.getPhoneId()) : "";
    }

    private boolean isMvnoMatches(int mvnoType, String mvnoMatchData) {
        String strSpn;
        log("mvnoMatchData=" + mvnoMatchData);
        String strHexSpn = "";
        if (mvnoType == 0) {
            int i = this.mPhoneType;
            PlmnMvnoData plmnMvnoData = this.mPlmnMvnoData;
            if (i == 1) {
                if (plmnMvnoData != null) {
                    strHexSpn = this.mPlmnMvnoData.getGsmSpn();
                }
            } else if (plmnMvnoData != null) {
                strHexSpn = this.mPlmnMvnoData.getCdmaSpn();
            }
            if (strHexSpn.length() == 0) {
                return false;
            }
            if (this.mPhoneType == 1) {
                strSpn = MtkIccUtilsEx.parseSpnToString(1, IccUtils.hexStringToBytes(strHexSpn));
            } else {
                strSpn = MtkIccUtilsEx.parseSpnToString(2, IccUtils.hexStringToBytes(strHexSpn));
            }
            log("strSpn=" + strSpn);
            if (strSpn != null && strSpn.equalsIgnoreCase(mvnoMatchData)) {
                return DBG;
            }
        } else if (mvnoType != 1) {
            if (mvnoType == 2) {
                strHexSpn = this.mPlmnMvnoData != null ? this.mPlmnMvnoData.getGid1() : "";
                log("gid1=" + strHexSpn);
                int mvno_match_data_length = mvnoMatchData.length();
                if (strHexSpn != null && strHexSpn.length() >= mvno_match_data_length && strHexSpn.substring(0, mvno_match_data_length).equalsIgnoreCase(mvnoMatchData)) {
                    return DBG;
                }
            } else if (mvnoType == 3 && MtkDataHelper.iccidMatches(mvnoMatchData, getIccid())) {
                return DBG;
            }
        } else {
            int i2 = this.mPhoneType;
            PlmnMvnoData plmnMvnoData2 = this.mPlmnMvnoData;
            if (i2 == 1) {
                if (plmnMvnoData2 != null) {
                    strHexSpn = this.mPlmnMvnoData.getGsmImsi();
                }
            } else if (plmnMvnoData2 != null) {
                strHexSpn = this.mPlmnMvnoData.getCdmaImsi();
            }
            if (strHexSpn != null && MtkDataHelper.imsiMatches(mvnoMatchData, strHexSpn)) {
                return DBG;
            }
        }
        return false;
    }

    private void getMtuFromResource() {
        String operator = mtkGetOperatorNumeric();
        int mcc = 0;
        int mnc = 0;
        if (operator != null && operator.length() > 3) {
            try {
                mcc = Integer.parseInt(operator.substring(0, 3));
                mnc = Integer.parseInt(operator.substring(3, operator.length()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                loge("operator numeric is invalid");
            }
        }
        new Configuration();
        Configuration configuration = this.mPhone.getContext().getResources().getConfiguration();
        configuration.mcc = mcc;
        configuration.mnc = mnc == 0 ? 65535 : mnc;
        Context resc = this.mPhone.getContext().createConfigurationContext(configuration);
        Resources resource = resc.getResources();
        if (resource != null) {
            this.mMtuFromResource = resource.getInteger(R.integer.config_pinnerWebviewPinBytes);
            log("getMtuFromResource: mcc = " + mcc + ", mnc = " + mnc + ", mtu = " + this.mMtuFromResource);
        }
    }

    public int getMtu() {
        return this.mMtuFromResource;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSubIdReady() {
        updatePhoneType();
        log("onSubIdReady mPhoneType = " + this.mPhoneType + " [1:GSM,2:CDMA]");
        String operatorNumeric = mtkGetOperatorNumeric();
        if (TextUtils.isEmpty(operatorNumeric)) {
            log("onSubIdReady: empty operator numeric, return");
            return;
        }
        if (MtkDataHelper.isCdma3GDualModeCard(this.mPhone.getPhoneId())) {
            MtkDataNetworkController mtkDnc = (MtkDataNetworkController) this.mPhone.getDataNetworkController();
            if (mtkDnc == null) {
                loge("onSubIdReady: MtkDataNetworkController is null, return");
                return;
            }
            boolean isAnyConnected = mtkDnc.isAnyDataNetworkConnected();
            if (isAnyConnected && this.mAllDataProfiles.size() != 0) {
                DataProfile df = (DataProfile) this.mAllDataProfiles.get(0);
                String numeric = df.getApnSetting().getOperatorNumeric();
                if (numeric.length() > 0 && !numeric.equals(operatorNumeric)) {
                    log("CDMA 3G dual mode card numeric change, clean up.");
                    mtkDnc.tearDownAllDataNetworks(2);
                }
            }
        }
        this.mFdnConstraint = false;
        checkFdnState();
        getMtuFromResource();
        updateDataProfiles(DBG);
    }

    private void sortAndLimitDataProfiles() {
        if (this.mAllDataProfiles.size() <= 16) {
            return;
        }
        if (mLimitDataProfilesCapability && this.mPhone.getHalVersion(1).greaterOrEqual(RIL.RADIO_HAL_VERSION_2_1)) {
            return;
        }
        logw("sortAndLimitDataProfiles Too many APNs(" + this.mAllDataProfiles.size() + "), it is recommended to remove some to ensure the count does not exceed 16");
        List<DataProfile> dps = (List) this.mAllDataProfiles.stream().sorted(Comparator.comparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$sortAndLimitDataProfiles$2((DataProfile) obj);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(7) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(4) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(10) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(9) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(31) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda11
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(2) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda12
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(0) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(true ^ ((DataProfile) obj).canSatisfy(1));
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda14
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(3) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(5) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(8) ^ MtkDataProfileManager.DBG);
            }
        }).thenComparing(new Function() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((DataProfile) obj).canSatisfy(12) ^ MtkDataProfileManager.DBG);
            }
        })).limit(16L).collect(Collectors.toList());
        this.mAllDataProfiles = Collections.unmodifiableList(new ArrayList(dps));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$sortAndLimitDataProfiles$2(DataProfile dp) {
        return Boolean.valueOf(dp.equals(this.mPreferredDataProfile) ^ DBG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void registerForFdnEnableChanged() {
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            log("registerForFdnEnableChanged(), fdn config disabled, skip register");
        } else {
            if (this.mIsFdnEnableStateChangedRegistered) {
                log("registerForFdnEnableChanged(), already registered, skip register");
                return;
            }
            log("registerForFdnEnableChanged(), register for FDN enable changed");
            this.mPhone.getIccCard().registerForFdnChanged(this, EVENT_FDN_ENABLE_CHANGED, (Object) null);
            this.mIsFdnEnableStateChangedRegistered = DBG;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void unregisterForFdnEnableChanged() {
        if (!this.mIsFdnEnableStateChangedRegistered) {
            log("unregisterForFdnEnableChanged(), already unregistered, skip unregister");
            return;
        }
        log("unregisterForFdnEnableChanged(), unregister for FDN enable changed");
        this.mPhone.getIccCard().unregisterForFdnChanged(this);
        this.mIsFdnEnableStateChangedRegistered = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerFdnContentObserver() {
        Uri fdnContentUri;
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            log("registerFdnContentObserver(), fdn config disabled, skip register");
            return;
        }
        log("registerFdnContentObserver(), register for FDN content observer");
        if (SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
            fdnContentUri = Uri.parse(FDN_CONTENT_URI_WITH_SUB_ID + this.mPhone.getSubId());
        } else {
            fdnContentUri = Uri.parse(FDN_CONTENT_URI);
        }
        this.mSettingsObserver.observe(fdnContentUri, 103);
    }

    private void checkFdnState() {
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            return;
        }
        IMtkTelephonyEx telephonyEx = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
        if (telephonyEx == null) {
            loge("checkFdnState(), get telephonyEx failed!!");
            return;
        }
        try {
            boolean bFdnEnabled = telephonyEx.isFdnEnabled(this.mPhone.getSubId());
            log("checkFdnState(), bFdnEnabled = " + bFdnEnabled);
            if (bFdnEnabled) {
                if (this.mIsFdnChecked) {
                    log("checkFdnState(), match FDN for allow data = " + this.mIsMatchFdnForAllowData);
                    return;
                }
                boolean bPhbReady = telephonyEx.isPhbReady(this.mPhone.getSubId());
                log("checkFdnState(), bPhbReady = " + bPhbReady);
                if (bPhbReady) {
                    this.mWorkerHandler.sendMessage(obtainMessage(105, Boolean.valueOf(bFdnEnabled)));
                } else if (!this.mIsPhbStateChangedIntentRegistered) {
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("mediatek.intent.action.PHB_STATE_CHANGED");
                    this.mPhone.getContext().registerReceiver(this.mPhbStateChangedIntentReceiver, filter, 2);
                    this.mIsPhbStateChangedIntentRegistered = DBG;
                }
                return;
            }
            if (this.mIsPhbStateChangedIntentRegistered) {
                this.mIsPhbStateChangedIntentRegistered = false;
                this.mPhone.getContext().unregisterReceiver(this.mPhbStateChangedIntentReceiver);
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFdnChanged() {
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            log("onFdnChanged(), fdn config disabled, skip onFdnChanged");
            return;
        }
        log("onFdnChanged()");
        boolean bFdnEnabled = false;
        boolean bPhbReady = false;
        IMtkTelephonyEx telephonyEx = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
        if (telephonyEx != null) {
            try {
                bFdnEnabled = telephonyEx.isFdnEnabled(this.mPhone.getSubId());
                bPhbReady = telephonyEx.isPhbReady(this.mPhone.getSubId());
                log("onFdnChanged(), bFdnEnabled = " + bFdnEnabled + ", bPhbReady = " + bPhbReady);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        } else {
            loge("onFdnChanged(), get telephonyEx failed!!");
        }
        if (bPhbReady) {
            this.mWorkerHandler.sendMessage(obtainMessage(105, Boolean.valueOf(bFdnEnabled)));
        } else if (!this.mIsPhbStateChangedIntentRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("mediatek.intent.action.PHB_STATE_CHANGED");
            this.mPhone.getContext().registerReceiver(this.mPhbStateChangedIntentReceiver, filter, 2);
            this.mIsPhbStateChangedIntentRegistered = DBG;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanOrSetupDataConnByCheckFdn(boolean bFdnEnabled) {
        Uri uriFdn;
        log("cleanOrSetupDataConnByCheckFdn(), bFdnEnabled:" + bFdnEnabled);
        if (bFdnEnabled) {
            if (SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
                uriFdn = Uri.parse(FDN_CONTENT_URI_WITH_SUB_ID + this.mPhone.getSubId());
            } else {
                uriFdn = Uri.parse(FDN_CONTENT_URI);
            }
            ContentResolver cr = this.mPhone.getContext().getContentResolver();
            Cursor cursor = cr.query(uriFdn, new String[]{MtkTelephony.SmsCb.CbChannel.NUMBER}, null, null, null);
            this.mIsMatchFdnForAllowData = false;
            if (cursor != null) {
                this.mIsFdnChecked = DBG;
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    do {
                        String strFdnNumber = cursor.getString(cursor.getColumnIndexOrThrow(MtkTelephony.SmsCb.CbChannel.NUMBER));
                        if (strFdnNumber.equals(FDN_FOR_ALLOW_DATA)) {
                            this.mIsMatchFdnForAllowData = DBG;
                            break;
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            this.mFdnConstraint = true ^ this.mIsMatchFdnForAllowData;
        } else {
            this.mFdnConstraint = false;
        }
        ((MtkDataNetworkController) this.mDataNetworkController).fdnStateChanged();
    }

    protected boolean isUseDataService() {
        return false;
    }

    protected void mtkOnDataServiceBound(int transport) {
        if (transport == 1) {
            log("mtkOnDataServiceBound(), update data profiles");
            updateDataProfiles(DBG);
        }
    }

    public boolean isApnLoaded() {
        if (this.mCurrentOperatorNumeric != null) {
            return DBG;
        }
        return false;
    }

    public boolean isFdnConstraint() {
        return this.mFdnConstraint;
    }

    public void reset() {
        this.mAllDataProfiles = Collections.emptyList();
        this.mPreferredDataProfile = null;
        this.mPreferredDataProfileSetId = 0;
        this.mPlmnMvnoData = null;
        this.mMtuFromResource = 0;
        this.mPhoneType = 0;
        this.mCurrentOperatorNumeric = null;
        this.mInitialAttachDataProfile = null;
    }

    private void createWorkerHandler() {
        if (this.mWorkerHandler == null) {
            Thread thread = new Thread() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Looper.prepare();
                    MtkDataProfileManager.this.mWorkerHandler = new WorkerHandler();
                    Looper.loop();
                }
            };
            thread.start();
        }
    }

    private class WorkerHandler extends Handler {
        private WorkerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 105:
                    boolean bFdnEnabled = ((Boolean) msg.obj).booleanValue();
                    MtkDataProfileManager.this.cleanOrSetupDataConnByCheckFdn(bFdnEnabled);
                    break;
            }
        }
    }

    private boolean isMccMncLoaded() {
        TelephonyManager tm = ((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).createForSubscriptionId(this.mPhone.getSubId());
        String mccmnc = tm.getSimOperator();
        log("isMccMncLoaded: getSimOperator=" + mccmnc);
        return TextUtils.isEmpty(mccmnc) ^ DBG;
    }

    private boolean isFrameworkApnQueryable() {
        if (super.isApnQueryable() && isMccMncLoaded()) {
            return DBG;
        }
        return false;
    }

    protected void log(String s) {
        Rlog.d(LOG_TAG, this.mLogTag + ": " + s);
    }

    protected void loge(String s) {
        Rlog.e(LOG_TAG, this.mLogTag + ": " + s);
    }

    protected void logw(String s) {
        Rlog.w(LOG_TAG, this.mLogTag + ": " + s);
    }
}
