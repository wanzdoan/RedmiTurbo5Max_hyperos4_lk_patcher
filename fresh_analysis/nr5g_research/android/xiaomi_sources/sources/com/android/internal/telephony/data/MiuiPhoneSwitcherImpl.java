package com.android.internal.telephony.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.internal.hidden_from_bootclasspath.xiaomi.platform.flags.Flags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.datasub.SmartDualSimSwitch;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.net.module.util.BitUtils;
import com.android.telephony.Rlog;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPhoneSwitcherImpl extends Handler implements MiuiPhoneSwitcherStub.IMiuiPhoneSwitcher {
    private static final String ACTION_MSIM_VOICE_CAPABILITY_CHANGED = "org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED";
    private static final int EVENT_DSDA_STATE_CHANGED = 1012;
    private static final int EVENT_PRECISE_CALL_STATE_CHANGED = 109;
    private static final int EVENT_TEMP_DATA_EVALUATE = 1010;
    private static final int EVENT_VICE_SLOT_VOLTE_DATA_ENABLED = 1013;
    private static final String LOG_TAG = "MiuiPhoneSwitcherImpl";
    private static final String MULTISIM_DUALDATA_STATUS = "ril.multisim.dualdata_status";
    private static final String PERMISSION_MSIM_VOICE_CAPABILITY_CHANGED = "com.qti.permission.RECEIVE_MSIM_VOICE_CAPABILITY_CHANGED";
    private static final int STATE_DSDA = 3;
    private static final String VICE_SLOT_VOLTE_DATA_ENABLED = "vice_slot_volte_data_enabled";
    private final BroadcastReceiver mConcurrentCallsReceiver;
    private Context mContext;
    private PhoneSwitcher mPhoneSwitcher;
    private SubscriptionManager mSm;
    private SmartDualSimSwitch mSmartDualSim;
    private int mTempDataPhoneId;
    private boolean mTempDataSwitching;
    private boolean mViceSlotVolteDataEnabled;
    ContentObserver viceSlotVolteDataObserver;

    public MiuiPhoneSwitcherImpl() {
        super(Looper.getMainLooper());
        this.mSmartDualSim = null;
        this.mTempDataPhoneId = -1;
        this.mTempDataSwitching = false;
        this.mViceSlotVolteDataEnabled = false;
        this.mConcurrentCallsReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.data.MiuiPhoneSwitcherImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(MiuiPhoneSwitcherImpl.ACTION_MSIM_VOICE_CAPABILITY_CHANGED)) {
                    int capability = SystemProperties.getInt("ril.multisim.voice_capability", 0);
                    MiuiPhoneSwitcherImpl.logi("BroadcastReceiver dsda capability:" + capability);
                    MiuiPhoneSwitcherImpl.this.sendMessage(MiuiPhoneSwitcherImpl.this.obtainMessage(MiuiPhoneSwitcherImpl.EVENT_DSDA_STATE_CHANGED));
                    MiuiPhoneSwitcherImpl.this.handleActiveDataSubChanged();
                }
            }
        };
        this.viceSlotVolteDataObserver = new ContentObserver(null) { // from class: com.android.internal.telephony.data.MiuiPhoneSwitcherImpl.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                if (uri == null || !"vice_slot_volte_data_enabled".equals(uri.getLastPathSegment())) {
                    return;
                }
                MiuiPhoneSwitcherImpl.this.mViceSlotVolteDataEnabled = Settings.Global.getInt(MiuiPhoneSwitcherImpl.this.mContext.getContentResolver(), "vice_slot_volte_data_enabled", 0) != 0;
                MiuiPhoneSwitcherImpl.logi("Vice slot volte data change, mViceSlotVolteDataEnabled :" + MiuiPhoneSwitcherImpl.this.mViceSlotVolteDataEnabled);
                if (MiuiPhoneSwitcherImpl.this.mViceSlotVolteDataEnabled) {
                    MiuiPhoneSwitcherImpl.this.sendMessage(MiuiPhoneSwitcherImpl.this.obtainMessage(MiuiPhoneSwitcherImpl.EVENT_VICE_SLOT_VOLTE_DATA_ENABLED));
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logi(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    private static void loge(String msg) {
        Rlog.e(LOG_TAG, msg);
    }

    public void init(Context context, Object... arg) {
        this.mContext = context;
        if (arg.length > 0 && (arg[0] instanceof PhoneSwitcher)) {
            this.mPhoneSwitcher = (PhoneSwitcher) arg[0];
        }
        this.mSm = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        SmartDualSimSwitch.makeSmartDualSimSwitch(context, PhoneFactory.getPhones());
        this.mSmartDualSim = SmartDualSimSwitch.getInstance();
        if (this.mSmartDualSim != null && this.mPhoneSwitcher != null) {
            this.mSmartDualSim.registerReEvaluateEvent(this.mPhoneSwitcher, EVENT_TEMP_DATA_EVALUATE, null);
        }
        context.registerReceiver(this.mConcurrentCallsReceiver, new IntentFilter(ACTION_MSIM_VOICE_CAPABILITY_CHANGED), PERMISSION_MSIM_VOICE_CAPABILITY_CHANGED, this, 2);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("vice_slot_volte_data_enabled"), false, this.viceSlotVolteDataObserver);
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, Integer.MAX_VALUE);
    }

    public void dispose() {
        if (this.mSmartDualSim != null) {
            this.mSmartDualSim.unregisterReEvaluateEvent(this.mPhoneSwitcher);
        }
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, Integer.MAX_VALUE);
    }

    private boolean isTempDdsSwitchEnabled() {
        if (this.mSmartDualSim != null) {
            return this.mSmartDualSim.isTempDdsSwitchEnabled();
        }
        return false;
    }

    public void onDdsSwitchResponse() {
        int activeSubId = SubscriptionManager.getActiveDataSubscriptionId();
        int activePhoneId = SubscriptionManager.getPhoneId(activeSubId);
        logi("onDdsSwitchResponse PhoneId -> " + activePhoneId + " SubId -> " + activeSubId);
        handleActiveDataSubChanged();
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_TEMP_DATA_EVALUATE /* 1010 */:
                onTempDataEvaluate(msg);
                break;
            case EVENT_DSDA_STATE_CHANGED /* 1012 */:
            case EVENT_VICE_SLOT_VOLTE_DATA_ENABLED /* 1013 */:
                PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
                if (phoneSwitcher != null) {
                    logi("EVENT_DSDA_STATE_CHANGED or EVENT_VICE_SLOT_VOLTE_DATA_ENABLED is going");
                    phoneSwitcher.sendMessage(phoneSwitcher.obtainMessage(EVENT_PRECISE_CALL_STATE_CHANGED));
                } else {
                    loge("PhoneSwitcher is null!");
                }
                break;
        }
    }

    private void onTempDataEvaluate(Message msg) {
        logi("EVENT_TEMP_DATA_EVALUATE");
        if (this.mPhoneSwitcher == null) {
            return;
        }
        AsyncResult ar = (AsyncResult) msg.obj;
        if (ar.exception == null && ar.result != null) {
            this.mTempDataPhoneId = ((Integer) ar.result).intValue();
        } else {
            logi("Unexpected exception on EVENT_TEMP_DATA_EVALUATE");
            this.mTempDataPhoneId = -1;
        }
        logi("EVENT_TEMP_DATA_EVALUATE mTempDataPhoneId=" + this.mTempDataPhoneId);
        if (this.mTempDataPhoneId != -1 && this.mPhoneSwitcher != null) {
            this.mPhoneSwitcher.onEvaluate(false, "SMARTDUALSIM_EVALUATE");
        }
    }

    public boolean updatePreferredDataPhoneId() {
        if (this.mPhoneSwitcher == null) {
            return false;
        }
        boolean isTempDdsSwitchEnabled = isTempDdsSwitchEnabled();
        logi("updatePreferredDataPhoneId  TempDataPhoneId=" + this.mTempDataPhoneId + ",isTempDdsSwitchEnabled=" + isTempDdsSwitchEnabled);
        if ((MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) && MiuiDataControllerImpl.Processor.isCurrentDSDAState() && !isTempDdsSwitchEnabled) {
            logi("updatePreferredDataPhoneId Mtk isCurrentDSDAState is true but isTempDdsSwitchEnabled is false");
            this.mTempDataSwitching = false;
            return false;
        }
        Phone tempDataPhone = findPhoneById(this.mTempDataPhoneId);
        Phone voicePhone = findPhoneById(this.mPhoneSwitcher.mPhoneIdInVoiceCall);
        if (this.mPhoneSwitcher.mEmergencyOverride != null && findPhoneById(this.mPhoneSwitcher.mEmergencyOverride.mPhoneId) != null) {
            logi("updatePreferredDataPhoneId: preferred data overridden for emergency. phoneId = " + this.mPhoneSwitcher.mEmergencyOverride.mPhoneId);
            this.mPhoneSwitcher.mPreferredDataPhoneId = this.mPhoneSwitcher.mEmergencyOverride.mPhoneId;
        } else if (voicePhone == null || !voicePhone.getDataSettingsManager().isDataEnabled(17)) {
            if (tempDataPhone != null && tempDataPhone.getDataSettingsManager().isDataEnabled(17) && isTempDdsSwitchEnabled) {
                logi("updatePreferredDataPhoneId: preferred data overridden for smartdualsim:mpreferred=" + this.mPhoneSwitcher.mPreferredDataPhoneId + ",temp=" + this.mTempDataPhoneId);
                this.mPhoneSwitcher.mPreferredDataPhoneId = this.mTempDataPhoneId;
                this.mTempDataSwitching = true;
            } else {
                int subId = getSubIdForDefaultNetworkRequests();
                int phoneId = -1;
                if (SubscriptionManager.isUsableSubIdValue(subId)) {
                    for (int i = 0; i < this.mPhoneSwitcher.mActiveModemCount; i++) {
                        if (this.mPhoneSwitcher.mPhoneSubscriptions[i] == subId) {
                            phoneId = i;
                            break;
                        }
                    }
                }
                this.mPhoneSwitcher.mPreferredDataPhoneId = phoneId;
                this.mTempDataSwitching = false;
            }
        } else {
            logi("updatePreferredDataPhoneId:,mPreferredDataPhoneId=" + this.mPhoneSwitcher.mPreferredDataPhoneId + ", mPhoneIdInVoiceCall=" + this.mPhoneSwitcher.mPhoneIdInVoiceCall);
            ServiceState voiceServiceState = voicePhone.getServiceState();
            logi("isVoiceOverCellularImsEnabled = " + voicePhone.isVoiceOverCellularImsEnabled());
            if (this.mPhoneSwitcher.mPhoneIdInVoiceCall != this.mPhoneSwitcher.mPreferredDataPhoneId && voicePhone != null && voicePhone.isImsRegistered() && voiceServiceState != null && (ServiceState.isPsOnlyTech(voiceServiceState.getRilDataRadioTechnology()) || voicePhone.isVoiceOverCellularImsEnabled())) {
                this.mPhoneSwitcher.mPreferredDataPhoneId = this.mPhoneSwitcher.mPhoneIdInVoiceCall;
                this.mTempDataSwitching = true;
            }
        }
        this.mPhoneSwitcher.mPreferredDataSubId.set(SubscriptionManager.getSubscriptionId(this.mPhoneSwitcher.mPreferredDataPhoneId));
        return true;
    }

    public void onNeedNetworkFor(TelephonyNetworkRequest networkRequest, Phone phone) {
        int defaultSub = SubscriptionManager.getActiveDataSubscriptionId();
        boolean isDualDataConnected = SystemProperties.getInt(MULTISIM_DUALDATA_STATUS, 0) == 1;
        if (phone.getSubId() != defaultSub && !isDualDataConnected) {
            NetworkRequest netRequest = networkRequest.getNativeNetworkRequest();
            Map<String, String> map = new HashMap<>();
            if (netRequest.hasCapability(12) || !netRequest.hasCapability(4)) {
                String networkInfoStringBuilder = networkInfoStringBuilder(netRequest);
                localLog("onNeedNetworkFor", networkInfoStringBuilder);
                long networkCapabilities = BitUtils.packBits(netRequest.getCapabilities());
                map.put("RequestorPackageName", netRequest.getRequestorPackageName());
                map.put("NetworkCapabilities", Long.toString(networkCapabilities));
                TelephonyManagerEx.getDefault().recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "ViceNetworkRequest", map);
            }
        }
    }

    public boolean onNetworkNeeded(TelephonyNetworkRequest networkRequest) {
        NetworkRequest nativeNetworkRequest = networkRequest.getNativeNetworkRequest();
        String packageName = nativeNetworkRequest.getRequestorPackageName();
        boolean isSatelliteTransport = nativeNetworkRequest.hasTransport(10);
        if (isSatelliteTransport && "com.android.phone".equals(packageName)) {
            return true;
        }
        return false;
    }

    private String networkInfoStringBuilder(NetworkRequest netRequest) {
        StringBuilder sb = new StringBuilder("[");
        sb.append("RequestorPkg: ");
        sb.append(netRequest.getRequestorPackageName());
        Pattern pattern = Pattern.compile("Capabilities: (.*?)(?:\\s|$|,|\\])", 2);
        Matcher matcher = pattern.matcher(netRequest.toString());
        if (matcher.find()) {
            sb.append(", Capabilities: ");
            sb.append(matcher.group(1));
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean getTempDataSwitching() {
        logi("getTempDataSwitching:,MIUITempDataSwitching =" + this.mTempDataSwitching);
        return this.mTempDataSwitching;
    }

    public boolean isPhoneInVoiceCall(Phone phone, boolean defVal) {
        if (phone == null) {
            return false;
        }
        if (!MiuiTelephony.IS_QCOM && !Flags.sprdEnabled()) {
            return defVal;
        }
        boolean isViceVolteDataEnabled = Settings.Global.getInt(this.mContext.getContentResolver(), "vice_slot_volte_data_enabled", 0) != 0;
        Phone vicePhone = getVicePhone();
        if (vicePhone != null) {
            boolean isRoaming = vicePhone.getServiceState().getRoaming();
            Rlog.d(LOG_TAG, "isRoaming: " + isRoaming);
            if (isRoaming) {
                return false;
            }
        }
        Rlog.d(LOG_TAG, "isViceVolteDataEnabled: " + isViceVolteDataEnabled + ", isDsda: " + MiuiDataControllerImpl.Processor.isCurrentDSDAState() + ", isDsdaFC: " + TelephonyManagerEx.getDefault().isDsdaFullConcurrencyState() + ", isAtLeastQcDE30: " + isAtLeastQcDE30() + ", isVicePhoneInCall: " + isVicePhoneInVoiceCall());
        if (!isViceVolteDataEnabled) {
            return false;
        }
        int defaultDataSubId = SubscriptionManager.getDefaultDataSubscriptionId();
        if (this.mPhoneSwitcher.getActiveDataSubId() == defaultDataSubId && isAtLeastQcDE30() && MiuiDataControllerImpl.Processor.isCurrentDSDAState() && TelephonyManagerEx.getDefault().isDsdaFullConcurrencyState()) {
            return false;
        }
        if (defaultDataSubId == phone.getSubId() && isVicePhoneInVoiceCall() && ((MiuiDataControllerImpl.Processor.isPlatformDE20() || isAtLeastQcDE30()) && MiuiDataControllerImpl.Processor.isCurrentDSDAState())) {
            Rlog.d(LOG_TAG, "phone.getPhoneId(): " + phone.getPhoneId() + " return false");
            return false;
        }
        if (phone.getSubId() != defaultDataSubId && phone.getSubId() != -1 && phone.getSubId() != Integer.MAX_VALUE && this.mPhoneSwitcher.getActiveDataSubId() == defaultDataSubId && isDefaultPhoneInVoiceCall() && ((MiuiDataControllerImpl.Processor.isPlatformDE20() || isAtLeastQcDE30()) && MiuiDataControllerImpl.Processor.isCurrentDSDAState())) {
            Rlog.d(LOG_TAG, "phone.getPhoneId(): " + phone.getPhoneId() + " return false");
            return false;
        }
        Rlog.d(LOG_TAG, "phoneid: " + phone.getPhoneId() + ", foreground call state: " + phone.getForegroundCall().getState() + ", background call state: " + phone.getBackgroundCall().getState());
        return phone.getForegroundCall().getState() == Call.State.ACTIVE || phone.getForegroundCall().getState() == Call.State.ALERTING || phone.getBackgroundCall().getState() == Call.State.HOLDING || phone.getBackgroundCall().getState() == Call.State.ACTIVE || phone.getRingingCall().getState().isAlive();
    }

    private boolean isAtLeastQcDE30() {
        boolean isSupport = HyperOSCustFeatureResolve.getBoolean("isAtLeastQcDE30", false);
        return isSupport || MiuiDataControllerImpl.Processor.isPlatformDE30() || MiuiDataControllerImpl.Processor.isPlatformDE50() || MiuiDataControllerImpl.Processor.isPlatformDE70() || MiuiDataControllerImpl.Processor.isPlatformDE90();
    }

    private Phone getVicePhone() {
        int defaultSub = SubscriptionManager.getDefaultDataSubscriptionId();
        Phone[] phones = PhoneFactory.getPhones();
        for (Phone phone : phones) {
            if (phone.getSubId() != defaultSub) {
                return phone;
            }
        }
        Rlog.e(LOG_TAG, "phone not initialized");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleActiveDataSubChanged() {
        DataNetwork dataNetwork;
        if (this.mPhoneSwitcher == null) {
            return;
        }
        for (int i = 0; i < this.mPhoneSwitcher.mActiveModemCount; i++) {
            DataNetworkController dataController = null;
            MiuiDataControllerImpl miuiDataController = null;
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                dataController = phone.getDataNetworkController();
                miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, i);
            }
            if (phone != null && dataController != null && miuiDataController != null && (dataNetwork = miuiDataController.findActiveInternetDataNetwork()) != null) {
                int dataConnectionState = 2;
                if (phone.getSubId() != SubscriptionManager.getActiveDataSubscriptionId()) {
                    dataConnectionState = 0;
                }
                phone.notifyDataConnection(new PreciseDataConnectionState.Builder().setTransportType(dataNetwork.getTransport()).setState(dataConnectionState).setApnSetting(dataNetwork.getDataProfile().getApnSetting()).setNetworkType(miuiDataController.getDataNetworkType(dataNetwork.getTransport())).setLinkProperties(dataNetwork.getLinkProperties()).build());
            }
        }
    }

    private Phone findPhoneById(int phoneId) {
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            return null;
        }
        return PhoneFactory.getPhone(phoneId);
    }

    private int getSubIdForDefaultNetworkRequests() {
        if (this.mPhoneSwitcher == null) {
            return -1;
        }
        int autoSelectedDataSubId = this.mPhoneSwitcher.getAutoSelectedDataSubId();
        if (this.mSm.isActiveSubId(autoSelectedDataSubId)) {
            return autoSelectedDataSubId;
        }
        return this.mPhoneSwitcher.mPrimaryDataSubId;
    }

    private boolean isVicePhoneInVoiceCall() {
        Phone[] phones = PhoneFactory.getPhones();
        if (phones.length < 2) {
            return false;
        }
        for (Phone phone : phones) {
            if (phone.getSubId() != -1 && phone.getSubId() != Integer.MAX_VALUE && phone.getSubId() != SubscriptionManager.getDefaultDataSubscriptionId()) {
                ImsPhone imsPhone = phone.getImsPhone();
                if (imsPhone == null) {
                    return false;
                }
                return imsPhone.getForegroundCall().getState() == Call.State.ACTIVE || imsPhone.getForegroundCall().getState() == Call.State.ALERTING || imsPhone.getBackgroundCall().getState() == Call.State.HOLDING || imsPhone.getBackgroundCall().getState() == Call.State.ACTIVE || imsPhone.getRingingCall().getState().isAlive();
            }
        }
        return false;
    }

    private boolean isDefaultPhoneInVoiceCall() {
        Phone[] phones = PhoneFactory.getPhones();
        if (phones.length < 2) {
            return false;
        }
        for (Phone phone : phones) {
            if (phone.getSubId() == SubscriptionManager.getDefaultDataSubscriptionId()) {
                ImsPhone imsPhone = phone.getImsPhone();
                if (imsPhone == null) {
                    return false;
                }
                return imsPhone.getForegroundCall().getState() == Call.State.ACTIVE || imsPhone.getForegroundCall().getState() == Call.State.ALERTING || imsPhone.getBackgroundCall().getState() == Call.State.HOLDING || imsPhone.getBackgroundCall().getState() == Call.State.ACTIVE || imsPhone.getRingingCall().getState().isAlive();
            }
        }
        return false;
    }

    private void localLog(String key, String log) {
        StringBuilder sb;
        String key2 = "MiuiPhoneSwitcherImpl." + key;
        try {
            PhoneFactory.addLocalLog(key2, 30);
            sb = new StringBuilder();
        } catch (IllegalArgumentException e) {
            sb = new StringBuilder();
        } finally {
            PhoneFactory.localLog(key2, "callingUid = " + Binder.getCallingUid() + " callingPid = " + Binder.getCallingPid() + log);
        }
        StringBuilder sbAppend = sb.append("callingUid = ");
    }
}
