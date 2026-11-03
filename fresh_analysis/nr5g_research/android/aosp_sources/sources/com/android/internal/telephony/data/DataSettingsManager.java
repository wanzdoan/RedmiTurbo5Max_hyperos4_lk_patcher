package com.android.internal.telephony.data;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.GlobalSettingsHelper;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.SimultaneousCallingTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.TelephonyCapabilities;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.metrics.DeviceTelephonyPropertiesStats;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class DataSettingsManager extends Handler {
    public static final int EVENT_MULTI_SIM_CONFIG_CHANGED = 1;
    private final DataConfigManager mDataConfigManager;
    private final Map mDataEnabledSettings;
    private final DataNetworkController mDataNetworkController;
    private final SparseArray mDataServiceManagers;
    private final Set mDataSettingsManagerCallbacks;
    private Set mEnabledMobileDataPolicy;
    private final FeatureFlags mFeatureFlags;
    public Handler mHandler;
    private boolean mInitialized;
    private boolean mIsDataEnabled;
    private final LocalLog mLocalLog;
    private final String mLogTag;
    protected final Phone mPhone;
    private final ContentResolver mResolver;
    private final SettingsObserver mSettingsObserver;
    protected int mSubId;

    public static class DataSettingsManagerCallback extends DataCallback {
        public void onDataEnabledChanged(boolean z, int i, String str) {
        }

        public void onDataEnabledOverrideChanged(boolean z, int i) {
        }

        public void onDataRoamingEnabledChanged(boolean z) {
        }

        public void onUserDataEnabledChanged(boolean z, String str) {
        }

        public DataSettingsManagerCallback(Executor executor) {
            super(executor);
        }
    }

    public DataSettingsManager(Phone phone, DataNetworkController dataNetworkController, SparseArray<DataServiceManager> sparseArray, FeatureFlags featureFlags, Looper looper, DataSettingsManagerCallback dataSettingsManagerCallback) {
        super(looper);
        this.mLocalLog = new LocalLog(128);
        this.mEnabledMobileDataPolicy = new HashSet();
        this.mDataSettingsManagerCallbacks = new ArraySet();
        ArrayMap arrayMap = new ArrayMap();
        this.mDataEnabledSettings = arrayMap;
        this.mInitialized = false;
        this.mHandler = new Handler() { // from class: com.android.internal.telephony.data.DataSettingsManager.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                DataSettingsManager.this.log("mHandler: " + message.what);
                if (message.what != 1) {
                    return;
                }
                DataSettingsManager.this.updateDataEnabledAndNotify(0);
            }
        };
        this.mPhone = phone;
        Objects.requireNonNull(featureFlags);
        this.mFeatureFlags = featureFlags;
        this.mLogTag = "DSMGR-" + phone.getPhoneId();
        log("DataSettingsManager created.");
        this.mSubId = phone.getSubId();
        this.mResolver = phone.getContext().getContentResolver();
        registerCallback(dataSettingsManagerCallback);
        this.mDataNetworkController = dataNetworkController;
        this.mDataConfigManager = dataNetworkController.getDataConfigManager();
        this.mDataServiceManagers = sparseArray;
        refreshEnabledMobileDataPolicy();
        this.mSettingsObserver = new SettingsObserver(phone.getContext(), this);
        Boolean bool = Boolean.TRUE;
        arrayMap.put(1, bool);
        arrayMap.put(2, bool);
        arrayMap.put(3, bool);
        PhoneConfigurationManager.registerForMultiSimConfigChange(this.mHandler, 1, null);
        sendEmptyMessage(11);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 2) {
            updateDataEnabledAndNotify(4);
            return;
        }
        if (i != 5) {
            if (i == 6) {
                setDataRoamingEnabledInternal(((Boolean) message.obj).booleanValue());
                setDataRoamingFromUserAction();
                return;
            }
            if (i == 7) {
                onSetMobileDataPolicy(message.arg1, message.arg2 == 1);
                return;
            }
            switch (i) {
                case 9:
                case 10:
                    updateDataEnabledAndNotify(-1);
                    notifyDataServiceUserDataEnabled(isUserDataEnabled());
                    break;
                case 11:
                    onInitialize();
                    break;
                default:
                    loge("Unknown msg.what: " + message.what);
                    break;
            }
            return;
        }
        String str = (String) message.obj;
        boolean z = message.arg2 == 1;
        int i2 = message.arg1;
        if (i2 == 0) {
            setUserDataEnabled(z, str);
            return;
        }
        if (i2 == 1) {
            setPolicyDataEnabled(z, str);
            return;
        }
        if (i2 == 2) {
            setCarrierDataEnabled(z, str);
        } else {
            if (i2 == 3) {
                setThermalDataEnabled(z, str);
                return;
            }
            log("Cannot set data enabled for reason: " + dataEnabledChangedReasonToString(message.arg1));
        }
    }

    private boolean hasCalling() {
        return TelephonyCapabilities.supportsTelephonyCalling(this.mFeatureFlags, this.mPhone.getContext());
    }

    private void onInitialize() {
        this.mDataConfigManager.registerCallback(new DataConfigManager.DataConfigManagerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda5
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.DataSettingsManager.2
            @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
            public void onCarrierConfigChanged() {
                if (DataSettingsManager.this.mDataConfigManager.isConfigCarrierSpecific()) {
                    DataSettingsManager.this.setDefaultDataRoamingEnabled();
                }
            }
        });
        this.mSettingsObserver.observe(Settings.Global.getUriFor("device_provisioned"), 9);
        this.mSettingsObserver.observe(Settings.Global.getUriFor("device_provisioning_mobile_data"), 10);
        if (hasCalling()) {
            this.mPhone.getCallTracker().registerForVoiceCallStarted(this, 2, null);
            this.mPhone.getCallTracker().registerForVoiceCallEnded(this, 2, null);
        }
        if (hasCalling() && this.mPhone.getImsPhone() != null) {
            this.mPhone.getImsPhone().getCallTracker().registerForVoiceCallStarted(this, 2, null);
            this.mPhone.getImsPhone().getCallTracker().registerForVoiceCallEnded(this, 2, null);
        }
        for (final Phone phone : PhoneFactory.getPhones()) {
            if (phone.getPhoneId() != this.mPhone.getPhoneId()) {
                phone.getDataSettingsManager().registerCallback(new DataSettingsManagerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda5
                    @Override // java.util.concurrent.Executor
                    public final void execute(Runnable runnable) {
                        this.f$0.post(runnable);
                    }
                }) { // from class: com.android.internal.telephony.data.DataSettingsManager.3
                    @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
                    public void onUserDataEnabledChanged(boolean z, String str) {
                        DataSettingsManager.this.log("phone " + phone.getPhoneId() + " onUserDataEnabledChanged " + z + " by " + str + ", reevaluating mobile data policies");
                        DataSettingsManager.this.updateDataEnabledAndNotify(4);
                    }
                });
            }
        }
        SubscriptionManagerService.getInstance().registerCallback(new SubscriptionManagerService.SubscriptionManagerServiceCallback(new Executor() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda5
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.DataSettingsManager.4
            @Override // com.android.internal.telephony.subscription.SubscriptionManagerService.SubscriptionManagerServiceCallback
            public void onDefaultDataSubscriptionChanged(int i) {
                DataSettingsManager dataSettingsManager = DataSettingsManager.this;
                dataSettingsManager.log((i == dataSettingsManager.mSubId ? "Became" : "Not").concat(" default data sub, reevaluating mobile data policies"));
                DataSettingsManager.this.updateDataEnabledAndNotify(4);
            }

            @Override // com.android.internal.telephony.subscription.SubscriptionManagerService.SubscriptionManagerServiceCallback
            public void onSubscriptionChanged(int i) {
                DataSettingsManager.this.handleSubscriptionChanged(i);
            }
        });
        updateDataEnabledAndNotify(-1);
        notifyDataServiceUserDataEnabled(isUserDataEnabled());
        notifyDataServiceUserDataRoamingEnabled(isDataRoamingEnabled());
        this.mDataNetworkController.registerDataNetworkControllerCallback(new DataNetworkController.DataNetworkControllerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda5
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.DataSettingsManager.5
            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onSimStateChanged(int i) {
                if (i == 10) {
                    DataSettingsManager dataSettingsManager = DataSettingsManager.this;
                    dataSettingsManager.notifyDataServiceUserDataEnabled(dataSettingsManager.isUserDataEnabled());
                    DataSettingsManager dataSettingsManager2 = DataSettingsManager.this;
                    dataSettingsManager2.notifyDataServiceUserDataRoamingEnabled(dataSettingsManager2.isDataRoamingEnabled());
                }
            }

            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onDataServiceBound(int i) {
                DataServiceManager dataServiceManager = (DataServiceManager) DataSettingsManager.this.mDataServiceManagers.get(i);
                dataServiceManager.notifyUserDataEnabled(DataSettingsManager.this.isUserDataEnabled(), null);
                dataServiceManager.notifyUserDataRoamingEnabled(DataSettingsManager.this.isDataRoamingEnabled(), null);
            }
        });
    }

    protected void handleSubscriptionChanged(int i) {
        int subId = this.mPhone.getSubId();
        int i2 = this.mSubId;
        if (i2 == subId) {
            if (i == i2) {
                log("handleSubscriptionChanged: " + i + " reevaluating data enabled");
                updateDataEnabledAndNotify(4);
                return;
            }
            return;
        }
        log("handleSubscriptionChanged: phone sub id changed from " + this.mSubId + " to " + subId);
        this.mSubId = subId;
        refreshEnabledMobileDataPolicy();
        updateDataEnabledAndNotify(0, this.mPhone.getContext().getOpPackageName(), SubscriptionManager.isValidSubscriptionId(this.mSubId));
        this.mPhone.notifyUserMobileDataStateChanged(isUserDataEnabled());
    }

    public void setDataEnabled(int i, boolean z, String str) {
        obtainMessage(5, i, z ? 1 : 0, str).sendToTarget();
    }

    public boolean isDataEnabledForReason(int i) {
        if (i == 0) {
            return isUserDataEnabled();
        }
        return ((Boolean) this.mDataEnabledSettings.get(Integer.valueOf(i))).booleanValue();
    }

    protected void updateStatusForSubIdChanged() {
        refreshEnabledMobileDataPolicy();
        updateDataEnabledAndNotify(0, this.mPhone.getContext().getOpPackageName(), SubscriptionManager.isValidSubscriptionId(this.mSubId));
        this.mPhone.notifyUserMobileDataStateChanged(isUserDataEnabled());
    }

    public void updateDataEnabledAndNotify(int i) {
        updateDataEnabledAndNotify(i, this.mPhone.getContext().getOpPackageName(), false);
    }

    private void updateDataEnabledAndNotify(int i, String str, boolean z) {
        boolean z2 = this.mIsDataEnabled;
        this.mIsDataEnabled = isDataEnabled(255);
        log("mIsDataEnabled=" + this.mIsDataEnabled + ", prevDataEnabled=" + z2);
        boolean z3 = this.mInitialized;
        if (z3 && !z && z2 == this.mIsDataEnabled) {
            return;
        }
        if (!z3) {
            this.mInitialized = true;
        }
        notifyDataEnabledChanged(this.mIsDataEnabled, i, str);
    }

    protected void notifyDataServiceUserDataEnabled(boolean z) {
        log("notifyDataServiceUserDataEnabled: enabled=" + z);
        for (int i = 0; i < this.mDataServiceManagers.size(); i++) {
            ((DataServiceManager) this.mDataServiceManagers.valueAt(i)).notifyUserDataEnabled(z, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDataServiceUserDataRoamingEnabled(boolean z) {
        log("notifyDataServiceUserDataRoamingEnabled: enabled=" + z);
        for (int i = 0; i < this.mDataServiceManagers.size(); i++) {
            ((DataServiceManager) this.mDataServiceManagers.valueAt(i)).notifyUserDataRoamingEnabled(z, null);
        }
    }

    private boolean isProvisioningDataEnabled() {
        if (TelephonyBaseUtilsStub.isAutoDevice()) {
            log("skip provision check for auto device");
            return true;
        }
        String str = SystemProperties.get("ro.com.android.prov_mobiledata", "false");
        int i = Settings.Global.getInt(this.mResolver, "device_provisioning_mobile_data", "true".equalsIgnoreCase(str) ? 1 : 0);
        boolean z = i != 0;
        log("getDataEnabled during provisioning retVal=" + z + " - (" + str + ", " + i + ")");
        return z;
    }

    public boolean isDataEnabled() {
        return this.mIsDataEnabled;
    }

    public boolean isDataInitialized() {
        return this.mInitialized;
    }

    public boolean isDataEnabled(int i) {
        if (TelephonyBaseUtilsStub.isAutoDevice() && TelephonyMultiInternetStub.get().isMultiInternetApnTypes(new int[]{i}, this.mPhone.getPhoneId())) {
            return isUserDataEnabled() && TelephonyMultiInternetStub.get().isDataEnabledForApn(i, this.mPhone.getPhoneId());
        }
        if (Settings.Global.getInt(this.mResolver, "device_provisioned", 0) == 0) {
            return isProvisioningDataEnabled();
        }
        return (isUserDataEnabled() || isDataEnabledOverriddenForApn(i)) && ((Boolean) this.mDataEnabledSettings.get(1)).booleanValue() && ((Boolean) this.mDataEnabledSettings.get(2)).booleanValue() && ((Boolean) this.mDataEnabledSettings.get(3)).booleanValue();
    }

    private boolean isStandAloneOpportunistic(int i) {
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(i);
        return subscriptionInfoInternal != null && subscriptionInfoInternal.isOpportunistic() && TextUtils.isEmpty(subscriptionInfoInternal.getGroupUuid());
    }

    private void setUserDataEnabled(final boolean z, final String str) {
        if (!isStandAloneOpportunistic(this.mSubId) || z) {
            boolean z2 = GlobalSettingsHelper.setInt(this.mPhone.getContext(), "mobile_data", (TelephonyCommonUtilsStub.get().isMiuiRom() || TelephonyBaseUtilsStub.isAutoDevice()) ? this.mPhone.getPhoneId() : this.mSubId, z ? 1 : 0);
            TelephonyCommonUtilsStub.get().setMobileData(this.mPhone.getContext(), z);
            logl("Set user data enabled to " + z + " on sub " + this.mSubId + ", changed=" + z2 + ", callingPackage=" + str);
            if (z2) {
                logl("UserDataEnabled changed to " + z);
                this.mPhone.notifyUserMobileDataStateChanged(z);
                this.mDataSettingsManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DataSettingsManager.DataSettingsManagerCallback dataSettingsManagerCallback = (DataSettingsManager.DataSettingsManagerCallback) obj;
                        dataSettingsManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                dataSettingsManagerCallback.onUserDataEnabledChanged(z, str);
                            }
                        });
                    }
                });
                updateDataEnabledAndNotify(0, str, false);
                notifyDataServiceUserDataEnabled(z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUserDataEnabled() {
        if (Settings.Global.getInt(this.mResolver, "device_provisioned", 0) == 0) {
            return isProvisioningDataEnabled();
        }
        if (isStandAloneOpportunistic(this.mSubId)) {
            return true;
        }
        return GlobalSettingsHelper.getBoolean(this.mPhone.getContext(), "mobile_data", (TelephonyBaseUtilsStub.isMiuiRom() || TelephonyBaseUtilsStub.isAutoDevice()) ? this.mPhone.getPhoneId() : this.mSubId, TelephonyProperties.mobile_data().orElse(Boolean.TRUE).booleanValue());
    }

    private void setPolicyDataEnabled(boolean z, String str) {
        if (((Boolean) this.mDataEnabledSettings.get(1)).booleanValue() != z) {
            logl("PolicyDataEnabled changed to " + z + ", callingPackage=" + str);
            this.mDataEnabledSettings.put(1, Boolean.valueOf(z));
            updateDataEnabledAndNotify(1, str, false);
        }
    }

    private void setCarrierDataEnabled(boolean z, String str) {
        if (((Boolean) this.mDataEnabledSettings.get(2)).booleanValue() != z) {
            logl("CarrierDataEnabled changed to " + z + ", callingPackage=" + str);
            this.mDataEnabledSettings.put(2, Boolean.valueOf(z));
            updateDataEnabledAndNotify(2, str, false);
        }
    }

    private void setThermalDataEnabled(boolean z, String str) {
        if (((Boolean) this.mDataEnabledSettings.get(3)).booleanValue() != z) {
            logl("ThermalDataEnabled changed to " + z + ", callingPackage=" + str);
            this.mDataEnabledSettings.put(3, Boolean.valueOf(z));
            updateDataEnabledAndNotify(3, str, false);
        }
    }

    public void setDataRoamingEnabled(boolean z) {
        obtainMessage(6, Boolean.valueOf(z)).sendToTarget();
    }

    private void setDataRoamingEnabledInternal(final boolean z) {
        boolean z2 = GlobalSettingsHelper.setBoolean(this.mPhone.getContext(), "data_roaming", this.mSubId, z);
        logl("DataRoamingEnabled set to " + z + " on sub " + this.mSubId + ", changed=" + z2);
        if (z2) {
            this.mDataSettingsManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DataSettingsManager.DataSettingsManagerCallback dataSettingsManagerCallback = (DataSettingsManager.DataSettingsManagerCallback) obj;
                    dataSettingsManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            dataSettingsManagerCallback.onDataRoamingEnabledChanged(z);
                        }
                    });
                }
            });
            notifyDataServiceUserDataRoamingEnabled(z);
        }
    }

    public boolean isDataRoamingEnabled() {
        return MiuiDataControllerStub.get(this.mPhone.getPhoneId()).isDataRoamingEnabled(this.mPhone) || GlobalSettingsHelper.getBoolean(this.mPhone.getContext(), "data_roaming", this.mSubId, isDefaultDataRoamingEnabled());
    }

    public boolean isDefaultDataRoamingEnabled() {
        return "true".equalsIgnoreCase(SystemProperties.get("ro.com.android.dataroaming", "false")) || this.mPhone.getDataNetworkController().getDataConfigManager().isDataRoamingEnabledByDefault();
    }

    public void setDefaultDataRoamingEnabled() {
        if (isDataRoamingFromUserAction()) {
            return;
        }
        setDataRoamingEnabledInternal(isDefaultDataRoamingEnabled());
    }

    private boolean isDataRoamingFromUserAction() {
        String str = Phone.DATA_ROAMING_IS_USER_SETTING_KEY + this.mPhone.getSubId();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mPhone.getContext());
        if (!defaultSharedPreferences.contains(str)) {
            if (defaultSharedPreferences.contains(Phone.DATA_ROAMING_IS_USER_SETTING_KEY)) {
                log("Reusing previous roaming from user action value for backwards compatibility.");
                defaultSharedPreferences.edit().putBoolean(str, true).commit();
            } else {
                log("Clearing roaming from user action value for new or upgrading devices.");
                defaultSharedPreferences.edit().putBoolean(str, false).commit();
            }
        }
        boolean z = defaultSharedPreferences.getBoolean(str, true);
        log("isDataRoamingFromUserAction: key=" + str + ", isUserSetting=" + z);
        return z;
    }

    private void setDataRoamingFromUserAction() {
        String str = Phone.DATA_ROAMING_IS_USER_SETTING_KEY + this.mPhone.getSubId();
        log("setDataRoamingFromUserAction: key=".concat(str));
        PreferenceManager.getDefaultSharedPreferences(this.mPhone.getContext()).edit().putBoolean(str, true).commit();
    }

    private void refreshEnabledMobileDataPolicy() {
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(this.mSubId);
        if (subscriptionInfoInternal != null) {
            this.mEnabledMobileDataPolicy = getMobileDataPolicyEnabled(subscriptionInfoInternal.getEnabledMobileDataPolicies());
        }
    }

    public boolean isMobileDataPolicyEnabled(int i) {
        return this.mEnabledMobileDataPolicy.contains(Integer.valueOf(i));
    }

    public void setMobileDataPolicy(int i, boolean z) {
        obtainMessage(7, i, z ? 1 : 0).sendToTarget();
    }

    private void onSetMobileDataPolicy(int i, boolean z) {
        if (z == isMobileDataPolicyEnabled(i)) {
            return;
        }
        metricsRecordSetMobileDataPolicy(i);
        Set set = this.mEnabledMobileDataPolicy;
        if (z) {
            set.add(Integer.valueOf(i));
        } else {
            set.remove(Integer.valueOf(i));
        }
        SubscriptionManagerService.getInstance().setEnabledMobileDataPolicies(this.mSubId, (String) this.mEnabledMobileDataPolicy.stream().map(new SimultaneousCallingTracker$$ExternalSyntheticLambda0()).collect(Collectors.joining(",")));
        logl(TelephonyUtils.mobileDataPolicyToString(i) + " changed to " + z);
        updateDataEnabledAndNotify(4);
        notifyDataEnabledOverrideChanged(z, i);
    }

    private void metricsRecordSetMobileDataPolicy(int i) {
        if (i == 3) {
            DeviceTelephonyPropertiesStats.recordAutoDataSwitchFeatureToggle();
        }
    }

    public boolean isRecoveryOnBadNetworkEnabled() {
        return Settings.Global.getInt(this.mResolver, "data_stall_recovery_on_bad_network", 1) == 1;
    }

    private void notifyDataEnabledChanged(final boolean z, final int i, final String str) {
        logl("notifyDataEnabledChanged: enabled=" + z + ", reason=" + dataEnabledChangedReasonToString(i) + ", callingPackage=" + str);
        this.mDataSettingsManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DataSettingsManager.DataSettingsManagerCallback dataSettingsManagerCallback = (DataSettingsManager.DataSettingsManagerCallback) obj;
                dataSettingsManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        dataSettingsManagerCallback.onDataEnabledChanged(z, i, str);
                    }
                });
            }
        });
        this.mPhone.notifyDataEnabled(z, i);
    }

    private void notifyDataEnabledOverrideChanged(final boolean z, final int i) {
        logl("notifyDataEnabledOverrideChanged: enabled=" + z);
        this.mDataSettingsManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DataSettingsManager.DataSettingsManagerCallback dataSettingsManagerCallback = (DataSettingsManager.DataSettingsManagerCallback) obj;
                dataSettingsManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        dataSettingsManagerCallback.onDataEnabledOverrideChanged(z, i);
                    }
                });
            }
        });
    }

    public Set<Integer> getMobileDataPolicyEnabled(String str) {
        int policyFrom;
        HashSet hashSet = new HashSet();
        for (String str2 : str.trim().split("\\s*,\\s*")) {
            if (!TextUtils.isEmpty(str2) && (policyFrom = parsePolicyFrom(str2)) != -1) {
                hashSet.add(Integer.valueOf(policyFrom));
            }
        }
        return hashSet;
    }

    private int parsePolicyFrom(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            loge("parsePolicyFrom: invalid mobile data policy format: " + str);
            return -1;
        }
    }

    private boolean isDataEnabledOverriddenForApn(int i) {
        boolean z = isMobileDataPolicyEnabled(2) && i == 2;
        boolean z2 = this.mPhone.getSubId() != SubscriptionManagerService.getInstance().getDefaultDataSubId();
        Phone phone = PhoneFactory.getPhone(SubscriptionManagerService.getInstance().getPhoneId(SubscriptionManagerService.getInstance().getDefaultDataSubId()));
        boolean z3 = phone != null && phone.isUserDataEnabled();
        if (isMobileDataPolicyEnabled(1)) {
            z |= z2 && z3 && this.mPhone.getState() != PhoneConstants.State.IDLE;
        }
        if (isMobileDataPolicyEnabled(3)) {
            return z | (z2 && z3);
        }
        return z;
    }

    public void registerCallback(DataSettingsManagerCallback dataSettingsManagerCallback) {
        this.mDataSettingsManagerCallbacks.add(dataSettingsManagerCallback);
    }

    public void unregisterCallback(DataSettingsManagerCallback dataSettingsManagerCallback) {
        this.mDataSettingsManagerCallbacks.remove(dataSettingsManagerCallback);
    }

    private static String dataEnabledChangedReasonToString(int i) {
        if (i == 0) {
            return "USER";
        }
        if (i == 1) {
            return "POLICY";
        }
        if (i == 2) {
            return "CARRIER";
        }
        if (i == 3) {
            return "THERMAL";
        }
        if (i == 4) {
            return "OVERRIDE";
        }
        return SatelliteConstants.DEFAULT_PLMN;
    }

    @Override // android.os.Handler
    public String toString() {
        return "[isUserDataEnabled=" + isUserDataEnabled() + ", isProvisioningDataEnabled=" + isProvisioningDataEnabled() + ", mIsDataEnabled=" + this.mIsDataEnabled + ", mDataEnabledSettings=" + this.mDataEnabledSettings + ", mEnabledMobileDataPolicy=" + ((String) this.mEnabledMobileDataPolicy.stream().map(new DataSettingsManager$$ExternalSyntheticLambda1()).collect(Collectors.joining(","))) + "]";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d(this.mLogTag, str);
    }

    private void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    private void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(DataSettingsManager.class.getSimpleName() + "-" + this.mPhone.getPhoneId() + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        StringBuilder sb = new StringBuilder("mIsDataEnabled=");
        sb.append(this.mIsDataEnabled);
        androidUtilIndentingPrintWriter.println(sb.toString());
        androidUtilIndentingPrintWriter.println("isDataEnabled(internet)=" + isDataEnabled(17));
        androidUtilIndentingPrintWriter.println("isDataEnabled(mms)=" + isDataEnabled(2));
        androidUtilIndentingPrintWriter.println("isUserDataEnabled=" + isUserDataEnabled());
        androidUtilIndentingPrintWriter.println("isDataRoamingEnabled=" + isDataRoamingEnabled());
        androidUtilIndentingPrintWriter.println("isDefaultDataRoamingEnabled=" + isDefaultDataRoamingEnabled());
        androidUtilIndentingPrintWriter.println("isDataRoamingFromUserAction=" + isDataRoamingFromUserAction());
        androidUtilIndentingPrintWriter.println("device_provisioned=" + Settings.Global.getInt(this.mResolver, "device_provisioned", 0));
        androidUtilIndentingPrintWriter.println("isProvisioningDataEnabled=" + isProvisioningDataEnabled());
        androidUtilIndentingPrintWriter.println("data_stall_recovery_on_bad_network=" + Settings.Global.getInt(this.mResolver, "data_stall_recovery_on_bad_network", 1));
        androidUtilIndentingPrintWriter.println("mDataEnabledSettings=" + ((String) this.mDataEnabledSettings.entrySet().stream().map(new Function() { // from class: com.android.internal.telephony.data.DataSettingsManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return DataSettingsManager.$r8$lambda$ngl0PFSCMfW75YYqA6AqgIC_Ago((Map.Entry) obj);
            }
        }).collect(Collectors.joining(", "))));
        androidUtilIndentingPrintWriter.println("mEnabledMobileDataPolicy=" + ((String) this.mEnabledMobileDataPolicy.stream().map(new DataSettingsManager$$ExternalSyntheticLambda1()).collect(Collectors.joining(","))));
        androidUtilIndentingPrintWriter.println("Local logs:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
    }

    public static /* synthetic */ String $r8$lambda$ngl0PFSCMfW75YYqA6AqgIC_Ago(Map.Entry entry) {
        return dataEnabledChangedReasonToString(((Integer) entry.getKey()).intValue()) + "=" + entry.getValue();
    }
}
