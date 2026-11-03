package com.android.internal.telephony;

import android.R;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.data.DataSettingsManager;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.ArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class MultiSimSettingController extends Handler {
    public static final int EVENT_MULTI_SIM_CONFIG_CHANGED = 8;
    public static final int EVENT_RADIO_STATE_CHANGED = 9;
    protected static final int EVENT_SUBSCRIPTION_INFO_CHANGED = 4;
    protected static MultiSimSettingController sInstance;
    private int mActiveModemCount;
    private int mCallbacksCount;
    private int[] mCarrierConfigLoadedSubIds;
    protected final Context mContext;
    private int mConvertedPsimSubId;
    private final FeatureFlags mFeatureFlags;
    private final boolean mIsAskEverytimeSupportedForSms;
    private boolean mNeedSetDefaultData;
    private boolean mNeedSetDefaultSms;
    private boolean mNeedSetDefaultVoice;
    private final SettingsObserver mSettingsObserver;
    protected List<Integer> mPrimarySubList = new ArrayList();
    protected boolean mSubInfoInitialized = false;
    protected boolean mInitialHandling = true;
    protected final SubscriptionManagerService mSubscriptionManagerService = SubscriptionManagerService.getInstance();

    /* JADX INFO: Access modifiers changed from: private */
    interface UpdateDefaultAction {
        void update(int i);
    }

    private boolean isUserVisibleChange(int i) {
        return i == 1 || i == 2 || i == 3;
    }

    protected boolean carrierConfigReallyLoaded() {
        return true;
    }

    protected void filterOutClosedSim(List<SubscriptionInfo> list) {
    }

    protected boolean ignoreSetDefaultDataForDataEnabled() {
        return false;
    }

    protected boolean skipSpecificClosedSim(int i) {
        return false;
    }

    protected boolean specificPreprocessForSubList(List<SubscriptionInfo> list) {
        return false;
    }

    private static class DataSettingsControllerCallback extends DataSettingsManager.DataSettingsManagerCallback {
        private final Phone mPhone;

        DataSettingsControllerCallback(Phone phone, Executor executor) {
            super(executor);
            this.mPhone = phone;
        }

        @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
        public void onUserDataEnabledChanged(boolean z, String str) {
            int subId = this.mPhone.getSubId();
            if (!SubscriptionManager.isValidSubscriptionId(subId) || MultiSimSettingController.getInstance().mContext.getOpPackageName().equals(str)) {
                return;
            }
            MultiSimSettingController.getInstance().notifyUserDataEnabled(subId, z);
        }

        @Override // com.android.internal.telephony.data.DataSettingsManager.DataSettingsManagerCallback
        public void onDataRoamingEnabledChanged(boolean z) {
            if (SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
                MultiSimSettingController.getInstance().notifyRoamingDataEnabled(this.mPhone.getSubId(), z);
            }
        }
    }

    public static MultiSimSettingController getInstance() {
        MultiSimSettingController multiSimSettingController;
        synchronized (MultiSimSettingController.class) {
            try {
                if (sInstance == null) {
                    Log.wtf("MultiSimSettingController", "getInstance null");
                }
                multiSimSettingController = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return multiSimSettingController;
    }

    public static MultiSimSettingController init(Context context, FeatureFlags featureFlags) {
        MultiSimSettingController multiSimSettingController;
        synchronized (MultiSimSettingController.class) {
            try {
                if (sInstance != null) {
                    Log.wtf("MultiSimSettingController", "init() called multiple times!  sInstance = " + sInstance);
                } else {
                    sInstance = new MultiSimSettingController(context, featureFlags);
                }
                multiSimSettingController = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return multiSimSettingController;
    }

    public MultiSimSettingController(Context context, FeatureFlags featureFlags) {
        this.mContext = context;
        SettingsObserver settingsObserver = new SettingsObserver(context, this);
        this.mSettingsObserver = settingsObserver;
        this.mFeatureFlags = featureFlags;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        int[] iArr = new int[telephonyManager.getSupportedModemCount()];
        this.mCarrierConfigLoadedSubIds = iArr;
        Arrays.fill(iArr, -1);
        this.mActiveModemCount = telephonyManager.getActiveModemCount();
        PhoneConfigurationManager.registerForMultiSimConfigChange(this, 8, null);
        settingsObserver.observe(Settings.Global.getUriFor("device_provisioned"), 10);
        this.mIsAskEverytimeSupportedForSms = context.getResources().getBoolean(R.bool.config_useCurrentRotationOnRotationLockChange);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new MultiSimSettingController$$ExternalSyntheticLambda4(this), new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.MultiSimSettingController$$ExternalSyntheticLambda5
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        this.mConvertedPsimSubId = getConvertedPsimSubscriptionId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        onCarrierConfigChanged(i, i2);
    }

    protected boolean hasCalling() {
        return TelephonyCapabilities.supportsTelephonyCalling(this.mFeatureFlags, this.mContext);
    }

    protected boolean hasData() {
        return TelephonyCapabilities.supportsTelephonyData(this.mFeatureFlags, this.mContext);
    }

    protected boolean hasMessaging() {
        return TelephonyCapabilities.supportsTelephonyMessaging(this.mFeatureFlags, this.mContext);
    }

    public void notifyUserDataEnabled(int i, boolean z) {
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            obtainMessage(1, i, z ? 1 : 0).sendToTarget();
        }
    }

    public void notifyRoamingDataEnabled(int i, boolean z) {
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            obtainMessage(2, i, z ? 1 : 0).sendToTarget();
        }
    }

    public void notifyAllSubscriptionLoaded() {
        obtainMessage(3).sendToTarget();
    }

    public void notifySubscriptionInfoChanged() {
        log("notifySubscriptionInfoChanged");
        obtainMessage(4).sendToTarget();
    }

    public void notifySubscriptionGroupChanged(ParcelUuid parcelUuid) {
        obtainMessage(5, parcelUuid).sendToTarget();
    }

    public void notifyDefaultDataSubChanged() {
        obtainMessage(6).sendToTarget();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                onUserDataEnabled(message.arg1, message.arg2 != 0, true);
                break;
            case 2:
                onRoamingDataEnabled(message.arg1, message.arg2 != 0);
                break;
            case 3:
                onAllSubscriptionsLoaded();
                break;
            case 4:
                onSubscriptionsChanged();
                break;
            case 5:
                onSubscriptionGroupChanged((ParcelUuid) message.obj);
                break;
            case 6:
                onDefaultDataSettingChanged();
                break;
            case 8:
                onMultiSimConfigChanged(((Integer) ((AsyncResult) message.obj).result).intValue());
                break;
            case 9:
                for (int i = 0; i < this.mActiveModemCount; i++) {
                    Phone phone = PhoneFactory.getPhone(i);
                    if (phone != null && phone.mCi.getRadioState() == 2) {
                        log("Radio unavailable on phone " + i + ", clearing sub info initialized flag");
                        this.mSubInfoInitialized = false;
                    }
                    break;
                }
                break;
            case 10:
                disableDataForNonDefaultNonOpportunisticSubscriptions();
                break;
        }
    }

    private void onUserDataEnabled(int i, boolean z, boolean z2) {
        log("[onUserDataEnabled] subId=" + i + " enable=" + z + " setDefaultData=" + z2);
        setUserDataEnabledForGroup(i, z);
        if (ignoreSetDefaultDataForDataEnabled()) {
            return;
        }
        SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(i);
        if (this.mSubscriptionManagerService.getDefaultDataSubId() == i || subscriptionInfo == null || subscriptionInfo.isOpportunistic() || !z || !subscriptionInfo.isActive() || !z2) {
            return;
        }
        Settings.Global.putInt(this.mContext.getContentResolver(), "user_preferred_data_sub", i);
        this.mSubscriptionManagerService.setDefaultDataSubId(i);
    }

    private void onRoamingDataEnabled(int i, boolean z) {
        log("onRoamingDataEnabled");
        setRoamingDataEnabledForGroup(i, z);
        this.mSubscriptionManagerService.setDataRoaming(z ? 1 : 0, i);
    }

    private void onAllSubscriptionsLoaded() {
        log("onAllSubscriptionsLoaded: mSubInfoInitialized=" + this.mSubInfoInitialized);
        if (!this.mSubInfoInitialized) {
            this.mSubInfoInitialized = true;
            this.mConvertedPsimSubId = getConvertedPsimSubscriptionId();
            for (Phone phone : PhoneFactory.getPhones()) {
                phone.mCi.registerForRadioStateChanged(this, 9, null);
            }
            reEvaluateAll();
        }
        registerDataSettingsControllerCallbackAsNeeded();
    }

    private void onSubscriptionsChanged() {
        log("onSubscriptionsChanged");
        reEvaluateAll();
    }

    public void onPhoneRemoved() {
        log("onPhoneRemoved");
        if (Looper.myLooper() != getLooper()) {
            Phone$$ExternalSyntheticBUOutline1.m("This method must be called from the same looper as MultiSimSettingController.");
        } else {
            reEvaluateAll();
        }
    }

    private void onCarrierConfigChanged(int i, int i2) {
        CarrierConfigManager carrierConfigManager;
        log("onCarrierConfigChanged phoneId " + i + " subId " + i2);
        if (!SubscriptionManager.isValidPhoneId(i)) {
            loge("Carrier config change with invalid phoneId " + i);
        } else if (!SubscriptionManager.isValidSubscriptionId(i2) || ((carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class)) != null && CarrierConfigManager.isConfigForIdentifiedCarrier(carrierConfigManager.getConfigForSubId(i2)))) {
            this.mCarrierConfigLoadedSubIds[i] = i2;
            reEvaluateAll();
        }
    }

    public boolean isCarrierConfigLoadedForAllSub() {
        for (int i : this.mSubscriptionManagerService.getActiveSubIdList(false)) {
            int[] iArr = this.mCarrierConfigLoadedSubIds;
            int length = iArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    if (skipSpecificClosedSim(i)) {
                        break;
                    }
                    log("Carrier config subId " + i + " is not loaded.");
                    return false;
                }
                if (iArr[i2] == i) {
                    break;
                }
                i2++;
            }
        }
        return true;
    }

    protected void onMultiSimConfigChanged(int i) {
        this.mActiveModemCount = i;
        log("onMultiSimConfigChanged: current ActiveModemCount=" + this.mActiveModemCount);
        while (true) {
            int[] iArr = this.mCarrierConfigLoadedSubIds;
            if (i >= iArr.length) {
                break;
            }
            iArr[i] = -1;
            i++;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.mCi.registerForRadioStateChanged(this, 9, null);
        }
        registerDataSettingsControllerCallbackAsNeeded();
    }

    private boolean isReadyToReevaluate() {
        boolean zIsCarrierConfigLoadedForAllSub = isCarrierConfigLoadedForAllSub();
        SatelliteController satelliteController = SatelliteController.getInstance();
        boolean zIsSatelliteEnabledOrBeingEnabled = satelliteController != null ? satelliteController.isSatelliteEnabledOrBeingEnabled() : false;
        log("isReadyToReevaluate: subInfoInitialized=" + this.mSubInfoInitialized + ", carrierConfigsLoaded=" + zIsCarrierConfigLoadedForAllSub + ", satelliteEnabledOrBeingEnabled=" + zIsSatelliteEnabledOrBeingEnabled);
        return this.mSubInfoInitialized && zIsCarrierConfigLoadedForAllSub && !zIsSatelliteEnabledOrBeingEnabled;
    }

    protected void reEvaluateAll() {
        if (isReadyToReevaluate()) {
            updateDefaults();
            disableDataForNonDefaultNonOpportunisticSubscriptions();
            deactivateGroupedOpportunisticSubscriptionIfNeeded();
        }
    }

    private void onDefaultDataSettingChanged() {
        log("onDefaultDataSettingChanged");
        disableDataForNonDefaultNonOpportunisticSubscriptions();
    }

    private void onSubscriptionGroupChanged(ParcelUuid parcelUuid) {
        boolean z;
        log("onSubscriptionGroupChanged");
        List<SubscriptionInfo> subscriptionsInGroup = this.mSubscriptionManagerService.getSubscriptionsInGroup(parcelUuid, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        if (subscriptionsInGroup == null || subscriptionsInGroup.isEmpty()) {
            return;
        }
        boolean z2 = false;
        int subscriptionId = subscriptionsInGroup.get(0).getSubscriptionId();
        for (SubscriptionInfo subscriptionInfo : subscriptionsInGroup) {
            int subscriptionId2 = subscriptionInfo.getSubscriptionId();
            if (subscriptionInfo.isActive() && !subscriptionInfo.isOpportunistic()) {
                subscriptionId = subscriptionId2;
                break;
            }
        }
        log("refSubId is " + subscriptionId);
        try {
            z = GlobalSettingsHelper.getBoolean(this.mContext, "mobile_data", TelephonyCommonUtilsStub.get().isMiuiRom() ? SubscriptionManager.getPhoneId(subscriptionId) : subscriptionId);
        } catch (Settings.SettingNotFoundException unused) {
            z = GlobalSettingsHelper.getBoolean(this.mContext, "mobile_data", -1, false);
        }
        boolean z3 = true;
        Iterator<SubscriptionInfo> it = this.mSubscriptionManagerService.getActiveSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), true).iterator();
        while (it.hasNext()) {
            if (!parcelUuid.equals(it.next().getGroupUuid())) {
                z3 = false;
                break;
            }
        }
        onUserDataEnabled(subscriptionId, z, z3);
        try {
            z2 = GlobalSettingsHelper.getBoolean(this.mContext, "data_roaming", subscriptionId);
            onRoamingDataEnabled(subscriptionId, z2);
        } catch (Settings.SettingNotFoundException unused2) {
            onRoamingDataEnabled(subscriptionId, GlobalSettingsHelper.getBoolean(this.mContext, "data_roaming", -1, z2));
        }
        this.mSubscriptionManagerService.syncGroupedSetting(subscriptionId);
    }

    protected void updateDefaults() {
        boolean zUpdateDefaultValue;
        boolean zUpdateDefaultValue2;
        log("updateDefaults");
        if (!TelephonyCommonUtilsStub.get().isMiuiRom() && isReadyToReevaluate()) {
            List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManagerService.getActiveSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), true);
            filterOutClosedSim(activeSubscriptionInfoList);
            if (ArrayUtils.isEmpty(activeSubscriptionInfoList)) {
                this.mPrimarySubList.clear();
                log("updateDefaults: No active sub. Setting default to INVALID sub.");
                this.mSubscriptionManagerService.setDefaultDataSubId(-1);
                this.mSubscriptionManagerService.setDefaultVoiceSubId(-1);
                this.mSubscriptionManagerService.setDefaultSmsSubId(-1);
                return;
            }
            if (specificPreprocessForSubList(activeSubscriptionInfoList)) {
                return;
            }
            int iUpdatePrimarySubListAndGetChangeType = updatePrimarySubListAndGetChangeType(activeSubscriptionInfoList);
            log("updateDefaultValues: change: " + iUpdatePrimarySubListAndGetChangeType);
            if (iUpdatePrimarySubListAndGetChangeType == 0 && carrierConfigReallyLoaded()) {
                return;
            }
            int size = this.mPrimarySubList.size();
            List<Integer> list = this.mPrimarySubList;
            boolean zUpdateDefaultValue3 = false;
            if (size == 1) {
                int iIntValue = list.get(0).intValue();
                log("updateDefaultValues: to only primary sub " + iIntValue);
                if (hasData()) {
                    this.mSubscriptionManagerService.setDefaultDataSubId(iIntValue);
                }
                if (hasCalling()) {
                    this.mSubscriptionManagerService.setDefaultVoiceSubId(iIntValue);
                }
                if (hasMessaging()) {
                    this.mSubscriptionManagerService.setDefaultSmsSubId(iIntValue);
                }
                if (this.mSubscriptionManagerService.isEsimBootStrapProvisioningActivated()) {
                    return;
                }
                sendDefaultSubConfirmedNotification((iUpdatePrimarySubListAndGetChangeType != 2 || this.mActiveModemCount == 1) ? 5 : 4, iIntValue);
                return;
            }
            log("updateDefaultValues: records: " + list);
            if (hasData()) {
                log("updateDefaultValues: Update default data subscription");
                List<Integer> list2 = this.mPrimarySubList;
                int defaultDataSubId = this.mSubscriptionManagerService.getDefaultDataSubId();
                final SubscriptionManagerService subscriptionManagerService = this.mSubscriptionManagerService;
                Objects.requireNonNull(subscriptionManagerService);
                zUpdateDefaultValue = updateDefaultValue(list2, defaultDataSubId, new UpdateDefaultAction() { // from class: com.android.internal.telephony.MultiSimSettingController$$ExternalSyntheticLambda1
                    @Override // com.android.internal.telephony.MultiSimSettingController.UpdateDefaultAction
                    public final void update(int i) {
                        subscriptionManagerService.setDefaultDataSubId(i);
                    }
                });
            } else {
                zUpdateDefaultValue = false;
            }
            if (hasCalling()) {
                log("updateDefaultValues: Update default voice subscription");
                List<Integer> list3 = this.mPrimarySubList;
                int defaultVoiceSubId = this.mSubscriptionManagerService.getDefaultVoiceSubId();
                final SubscriptionManagerService subscriptionManagerService2 = this.mSubscriptionManagerService;
                Objects.requireNonNull(subscriptionManagerService2);
                zUpdateDefaultValue2 = updateDefaultValue(list3, defaultVoiceSubId, new UpdateDefaultAction() { // from class: com.android.internal.telephony.MultiSimSettingController$$ExternalSyntheticLambda2
                    @Override // com.android.internal.telephony.MultiSimSettingController.UpdateDefaultAction
                    public final void update(int i) {
                        subscriptionManagerService2.setDefaultVoiceSubId(i);
                    }
                });
            } else {
                zUpdateDefaultValue2 = false;
            }
            if (hasMessaging()) {
                log("updateDefaultValues: Update default sms subscription");
                List<Integer> list4 = this.mPrimarySubList;
                int defaultSmsSubId = this.mSubscriptionManagerService.getDefaultSmsSubId();
                final SubscriptionManagerService subscriptionManagerService3 = this.mSubscriptionManagerService;
                Objects.requireNonNull(subscriptionManagerService3);
                zUpdateDefaultValue3 = updateDefaultValue(list4, defaultSmsSubId, new UpdateDefaultAction() { // from class: com.android.internal.telephony.MultiSimSettingController$$ExternalSyntheticLambda3
                    @Override // com.android.internal.telephony.MultiSimSettingController.UpdateDefaultAction
                    public final void update(int i) {
                        subscriptionManagerService3.setDefaultSmsSubId(i);
                    }
                }, this.mIsAskEverytimeSupportedForSms);
            }
            if (!this.mContext.getResources().getBoolean(17892082)) {
                if (setDefaultForPsimConversionChanged(iUpdatePrimarySubListAndGetChangeType, zUpdateDefaultValue, zUpdateDefaultValue2, zUpdateDefaultValue3)) {
                    return;
                }
                sendSubChangeNotificationIfNeeded(iUpdatePrimarySubListAndGetChangeType, zUpdateDefaultValue, zUpdateDefaultValue2, zUpdateDefaultValue3);
                return;
            }
            updateUserPreferences(this.mPrimarySubList, zUpdateDefaultValue, zUpdateDefaultValue2, zUpdateDefaultValue3);
        }
    }

    private int updatePrimarySubListAndGetChangeType(List list) {
        List<Integer> list2 = this.mPrimarySubList;
        List<Integer> list3 = (List) list.stream().filter(new Predicate() { // from class: com.android.internal.telephony.MultiSimSettingController$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MultiSimSettingController.$r8$lambda$4UrN2ZMe4T1KPCzRacrWAwrB38Q((SubscriptionInfo) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.MultiSimSettingController$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MultiSimSettingController.m192$r8$lambda$p0_pBQTGuAkxSR0bHlJP69Q5l4((SubscriptionInfo) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.MultiSimSettingController$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((SubscriptionInfo) obj).getSubscriptionId());
            }
        }).collect(Collectors.toList());
        this.mPrimarySubList = list3;
        if (this.mInitialHandling) {
            this.mInitialHandling = false;
            return 6;
        }
        if (list3.equals(list2)) {
            return 0;
        }
        if (this.mPrimarySubList.size() > list2.size()) {
            return 1;
        }
        if (this.mPrimarySubList.size() == list2.size()) {
            Iterator<Integer> it = this.mPrimarySubList.iterator();
            while (it.hasNext()) {
                int iIntValue = it.next().intValue();
                Iterator<Integer> it2 = list2.iterator();
                while (it2.hasNext()) {
                    if (areSubscriptionsInSameGroup(iIntValue, it2.next().intValue())) {
                    }
                }
                return 3;
            }
            return 4;
        }
        for (Integer num : list2) {
            int iIntValue2 = num.intValue();
            if (!this.mPrimarySubList.contains(num)) {
                SubscriptionInfo subscriptionInfo = this.mSubscriptionManagerService.getSubscriptionInfo(iIntValue2);
                if (subscriptionInfo == null || !subscriptionInfo.isActive()) {
                    Iterator<Integer> it3 = this.mPrimarySubList.iterator();
                    while (it3.hasNext()) {
                        if (areSubscriptionsInSameGroup(it3.next().intValue(), iIntValue2)) {
                            return 7;
                        }
                    }
                    return 2;
                }
                if (!subscriptionInfo.isOpportunistic()) {
                    loge("[updatePrimarySubListAndGetChangeType]: missing active primary subId " + iIntValue2);
                }
            }
        }
        return 5;
    }

    public static /* synthetic */ boolean $r8$lambda$4UrN2ZMe4T1KPCzRacrWAwrB38Q(SubscriptionInfo subscriptionInfo) {
        return !subscriptionInfo.isOpportunistic();
    }

    /* JADX INFO: renamed from: $r8$lambda$p0_pBQTGu-AkxSR0bHlJP69Q5l4, reason: not valid java name */
    public static /* synthetic */ boolean m192$r8$lambda$p0_pBQTGuAkxSR0bHlJP69Q5l4(SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.getProfileClass() != 1;
    }

    private void sendDefaultSubConfirmedNotification(int i, int i2) {
        Intent intent = new Intent();
        intent.setAction("android.telephony.action.PRIMARY_SUBSCRIPTION_LIST_CHANGED");
        intent.setClassName("com.android.settings", "com.android.settings.sim.SimSelectNotification");
        intent.putExtra("android.telephony.extra.DEFAULT_SUBSCRIPTION_SELECT_TYPE", i);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_ID", i2);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    private void sendSubChangeNotificationIfNeeded(int i, boolean z, boolean z2, boolean z3) {
        if (this.mSubscriptionManagerService.isEsimBootStrapProvisioningActivated()) {
            log("esim bootstrap activation in progress, skip notification");
            return;
        }
        int simSelectDialogType = getSimSelectDialogType(i, z, z2, z3);
        log("sendSubChangeNotificationIfNeeded: simSelectDialogType=" + simSelectDialogType);
        SimCombinationWarningParams simCombinationWarningParams = getSimCombinationWarningParams(i);
        if (simSelectDialogType == 0 && simCombinationWarningParams.mWarningType == 0) {
            return;
        }
        log("[sendSubChangeNotificationIfNeeded] showing dialog type " + simSelectDialogType);
        log("[sendSubChangeNotificationIfNeeded] showing sim warning " + simCombinationWarningParams.mWarningType);
        Intent intent = new Intent();
        intent.setAction("android.telephony.action.PRIMARY_SUBSCRIPTION_LIST_CHANGED");
        intent.setClassName("com.android.settings", "com.android.settings.sim.SimSelectNotification");
        intent.addFlags(268435456);
        intent.putExtra("android.telephony.extra.DEFAULT_SUBSCRIPTION_SELECT_TYPE", simSelectDialogType);
        if (simSelectDialogType == 4) {
            intent.putExtra("android.telephony.extra.SUBSCRIPTION_ID", this.mPrimarySubList.get(0));
        }
        intent.putExtra("android.telephony.extra.SIM_COMBINATION_WARNING_TYPE", simCombinationWarningParams.mWarningType);
        if (simCombinationWarningParams.mWarningType == 1) {
            intent.putExtra("android.telephony.extra.SIM_COMBINATION_NAMES", simCombinationWarningParams.mSimNames);
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    private boolean setDefaultForPsimConversionChanged(int i, boolean z, boolean z2, boolean z3) {
        if (this.mSubscriptionManagerService.isEsimBootStrapProvisioningActivated()) {
            log("esim bootstrap activation in progress, skip notification");
            return false;
        }
        int simSelectDialogType = getSimSelectDialogType(i, z, z2, z3);
        SimCombinationWarningParams simCombinationWarningParams = getSimCombinationWarningParams(i);
        log("[setDefaultForPsimConversionChanged]showing dialog type:" + simSelectDialogType);
        if (simSelectDialogType != 0 || simCombinationWarningParams.mWarningType != 0) {
            log("[setDefaultForPsimConversionChanged]Converted pSIM:" + this.mConvertedPsimSubId);
            int convertedPsimSubscriptionId = getConvertedPsimSubscriptionId();
            if (convertedPsimSubscriptionId != -1 && convertedPsimSubscriptionId != this.mConvertedPsimSubId) {
                if (simSelectDialogType == 4) {
                    if (this.mSubscriptionManagerService.getDefaultVoiceSubId() == -1) {
                        this.mNeedSetDefaultVoice = true;
                    }
                    if (this.mSubscriptionManagerService.getDefaultSmsSubId() == -1) {
                        this.mNeedSetDefaultSms = true;
                    }
                    if (this.mSubscriptionManagerService.getDefaultDataSubId() == -1) {
                        this.mNeedSetDefaultData = true;
                    }
                    log("select type all, set preferred SIM :" + this.mPrimarySubList.get(0));
                    this.mSubscriptionManagerService.setDefaultVoiceSubId(this.mPrimarySubList.get(0).intValue());
                    this.mSubscriptionManagerService.setDefaultSmsSubId(this.mPrimarySubList.get(0).intValue());
                    this.mSubscriptionManagerService.setDefaultDataSubId(this.mPrimarySubList.get(0).intValue());
                    return true;
                }
                if (simSelectDialogType == 1) {
                    int iIntValue = this.mPrimarySubList.get(0).intValue();
                    log("need to set voice:" + this.mNeedSetDefaultVoice + ", sms:" + this.mNeedSetDefaultSms + ", data:" + this.mNeedSetDefaultData);
                    if (this.mNeedSetDefaultVoice) {
                        log("set preferred call, subId:" + iIntValue);
                        this.mSubscriptionManagerService.setDefaultVoiceSubId(iIntValue);
                        this.mNeedSetDefaultVoice = false;
                    }
                    if (this.mNeedSetDefaultSms) {
                        log("set preferred sms, subId:" + iIntValue);
                        this.mSubscriptionManagerService.setDefaultSmsSubId(iIntValue);
                        this.mNeedSetDefaultSms = false;
                    }
                    if (this.mNeedSetDefaultData) {
                        log("set preferred data, subId:" + iIntValue);
                        this.mSubscriptionManagerService.setDefaultDataSubId(iIntValue);
                        this.mNeedSetDefaultData = false;
                    }
                    this.mConvertedPsimSubId = convertedPsimSubscriptionId;
                    log("set converted pSIM subId:" + this.mConvertedPsimSubId);
                    return true;
                }
            }
        }
        return false;
    }

    private int getConvertedPsimSubscriptionId() {
        int subscriptionId = -1;
        for (SubscriptionInfo subscriptionInfo : this.mSubscriptionManagerService.getAvailableSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag())) {
            if (!subscriptionInfo.isEmbedded() && subscriptionInfo.getTransferStatus() == 2) {
                subscriptionId = subscriptionInfo.getSubscriptionId();
            }
        }
        log("getConvertedPsimSubscriptionId: convertedSubId=" + subscriptionId);
        return subscriptionId;
    }

    private int getSimSelectDialogType(int i, boolean z, boolean z2, boolean z3) {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0) {
            if (this.mPrimarySubList.size() == 1 && i == 2 && (!z || !z3 || !z2)) {
                return 4;
            }
            if (this.mPrimarySubList.size() > 1 && (isUserVisibleChange(i) || (i == 6 && !z))) {
                return 1;
            }
        }
        return 0;
    }

    public class SimCombinationWarningParams {
        String mSimNames;
        int mWarningType = 0;

        public SimCombinationWarningParams(MultiSimSettingController multiSimSettingController) {
        }
    }

    protected SimCombinationWarningParams getSimCombinationWarningParams(int i) {
        SimCombinationWarningParams simCombinationWarningParams = new SimCombinationWarningParams(this);
        if (this.mPrimarySubList.size() <= 1) {
            return simCombinationWarningParams;
        }
        isUserVisibleChange(i);
        return simCombinationWarningParams;
    }

    protected void disableDataForNonDefaultNonOpportunisticSubscriptions() {
        if (isReadyToReevaluate()) {
            int defaultDataSubId = this.mSubscriptionManagerService.getDefaultDataSubId();
            for (Phone phone : PhoneFactory.getPhones()) {
                SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(phone.getSubId());
                boolean z = subscriptionInfoInternal != null && subscriptionInfoInternal.isOpportunistic();
                if (phone.getSubId() != defaultDataSubId && SubscriptionManager.isValidSubscriptionId(phone.getSubId()) && !z && phone.isUserDataEnabled() && !areSubscriptionsInSameGroup(defaultDataSubId, phone.getSubId())) {
                    log("setting data to false on " + phone.getSubId());
                    phone.getDataSettingsManager().setDataEnabled(0, false, this.mContext.getOpPackageName());
                }
            }
        }
    }

    private boolean areSubscriptionsInSameGroup(int i, int i2) {
        if (SubscriptionManager.isUsableSubscriptionId(i) && SubscriptionManager.isUsableSubscriptionId(i2)) {
            if (i == i2) {
                return true;
            }
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
            SubscriptionInfoInternal subscriptionInfoInternal2 = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i2);
            if (subscriptionInfoInternal != null && subscriptionInfoInternal2 != null && !TextUtils.isEmpty(subscriptionInfoInternal.getGroupUuid()) && subscriptionInfoInternal.getGroupUuid().equals(subscriptionInfoInternal2.getGroupUuid())) {
                return true;
            }
        }
        return false;
    }

    protected void setUserDataEnabledForGroup(int i, boolean z) {
        log("setUserDataEnabledForGroup subId " + i + " enable " + z);
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        List<SubscriptionInfo> subscriptionsInGroup = (subscriptionInfoInternal == null || subscriptionInfoInternal.getGroupUuid().isEmpty()) ? null : this.mSubscriptionManagerService.getSubscriptionsInGroup(ParcelUuid.fromString(subscriptionInfoInternal.getGroupUuid()), this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        if (subscriptionsInGroup == null) {
            return;
        }
        for (SubscriptionInfo subscriptionInfo : subscriptionsInGroup) {
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            if (subscriptionInfo.isActive()) {
                Phone phone = PhoneFactory.getPhone(this.mSubscriptionManagerService.getPhoneId(subscriptionId));
                if (phone != null) {
                    phone.getDataSettingsManager().setDataEnabled(0, z, this.mContext.getOpPackageName());
                }
            } else {
                if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                    subscriptionId = SubscriptionManager.getPhoneId(subscriptionId);
                }
                GlobalSettingsHelper.setBoolean(this.mContext, "mobile_data", subscriptionId, z);
            }
        }
    }

    private void setRoamingDataEnabledForGroup(int i, boolean z) {
        List<SubscriptionInfo> subscriptionsInGroup;
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal == null || subscriptionInfoInternal.getGroupUuid().isEmpty() || (subscriptionsInGroup = SubscriptionManagerService.getInstance().getSubscriptionsInGroup(ParcelUuid.fromString(subscriptionInfoInternal.getGroupUuid()), this.mContext.getOpPackageName(), this.mContext.getAttributionTag())) == null) {
            return;
        }
        Iterator<SubscriptionInfo> it = subscriptionsInGroup.iterator();
        while (it.hasNext()) {
            GlobalSettingsHelper.setBoolean(this.mContext, "data_roaming", it.next().getSubscriptionId(), z);
        }
    }

    private boolean updateDefaultValue(List list, int i, UpdateDefaultAction updateDefaultAction) {
        return updateDefaultValue(list, i, updateDefaultAction, true);
    }

    private boolean updateDefaultValue(List list, int i, UpdateDefaultAction updateDefaultAction, boolean z) {
        int i2 = -1;
        if (list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                int iIntValue = ((Integer) it.next()).intValue();
                log("[updateDefaultValue] Record.id: " + iIntValue);
                if (areSubscriptionsInSameGroup(iIntValue, i) || (!z && i == -1)) {
                    log("[updateDefaultValue] updates to subId=" + iIntValue);
                    i2 = iIntValue;
                    break;
                }
            }
        }
        if (i != i2) {
            log("[updateDefaultValue: subId] from " + i + " to " + i2);
            updateDefaultAction.update(i2);
        }
        return SubscriptionManager.isValidSubscriptionId(i2);
    }

    private void deactivateGroupedOpportunisticSubscriptionIfNeeded() {
        List<SubscriptionInfo> list = (List) this.mSubscriptionManagerService.getAllSubInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag()).stream().filter(new Predicate() { // from class: com.android.internal.telephony.MultiSimSettingController$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SubscriptionInfo) obj).isOpportunistic();
            }
        }).collect(Collectors.toList());
        if (ArrayUtils.isEmpty(list)) {
            return;
        }
        for (SubscriptionInfo subscriptionInfo : list) {
            if (subscriptionInfo.isGroupDisabled() && subscriptionInfo.isActive()) {
                log("deactivateGroupedOpportunisticSubscriptionIfNeeded: Deactivating grouped opportunistic subscription " + subscriptionInfo.getSubscriptionId());
                deactivateSubscription(subscriptionInfo);
            }
        }
    }

    private void deactivateSubscription(SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo.isEmbedded()) {
            log("[deactivateSubscription] eSIM profile " + subscriptionInfo.getSubscriptionId());
            ((EuiccManager) this.mContext.getSystemService("euicc")).switchToSubscription(-1, subscriptionInfo.getPortIndex(), PendingIntent.getService(this.mContext, 0, new Intent(), 67108864));
        }
    }

    private void updateUserPreferences(List list, boolean z, boolean z2, boolean z3) {
        if (list.isEmpty() || this.mSubscriptionManagerService.getActiveSubInfoCountMax() == 1) {
            return;
        }
        if (!isRadioAvailableOnAllSubs()) {
            log("Radio is in Invalid state, Ignore Updating User Preference!!!");
            return;
        }
        int defaultDataSubId = this.mSubscriptionManagerService.getDefaultDataSubId();
        log("updateUserPreferences:  dds = " + defaultDataSubId + " voice = " + this.mSubscriptionManagerService.getDefaultVoiceSubId() + " sms = " + this.mSubscriptionManagerService.getDefaultSmsSubId());
        int iIntValue = ((Integer) list.get(0)).intValue();
        if (hasMessaging() && list.size() == 1 && !z3) {
            this.mSubscriptionManagerService.setDefaultSmsSubId(iIntValue);
        }
        if (hasCalling() && list.size() == 1 && !z2) {
            this.mSubscriptionManagerService.setDefaultVoiceSubId(iIntValue);
        }
        int userPrefDataSubIdFromDB = getUserPrefDataSubIdFromDB();
        log("User pref subId = " + userPrefDataSubIdFromDB + " current dds " + defaultDataSubId + " next active subId " + iIntValue + " smart dds enabled false");
        if (hasData()) {
            if (list.contains(Integer.valueOf(userPrefDataSubIdFromDB)) && SubscriptionManager.isValidSubscriptionId(userPrefDataSubIdFromDB) && defaultDataSubId != userPrefDataSubIdFromDB) {
                this.mSubscriptionManagerService.setDefaultDataSubId(userPrefDataSubIdFromDB);
            } else if (!z) {
                this.mSubscriptionManagerService.setDefaultDataSubId(iIntValue);
            }
        }
        log("updateUserPreferences: after dds = " + this.mSubscriptionManagerService.getDefaultDataSubId() + " voice = " + this.mSubscriptionManagerService.getDefaultVoiceSubId() + " sms = " + this.mSubscriptionManagerService.getDefaultSmsSubId());
    }

    private int getUserPrefDataSubIdFromDB() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "user_preferred_data_sub", -1);
    }

    private boolean isRadioAvailableOnAllSubs() {
        for (int i = 0; i < this.mActiveModemCount; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null && (phone.mCi.getRadioState() == 2 || phone.isShuttingDown())) {
                return false;
            }
        }
        return true;
    }

    private void registerDataSettingsControllerCallbackAsNeeded() {
        Phone[] phones = PhoneFactory.getPhones();
        for (int i = this.mCallbacksCount; i < phones.length; i++) {
            phones[i].getDataSettingsManager().registerCallback(new DataSettingsControllerCallback(phones[i], new MultiSimSettingController$$ExternalSyntheticLambda4(this)));
        }
        this.mCallbacksCount = phones.length;
    }

    protected void log(String str) {
        Log.d("MultiSimSettingController", str);
    }

    protected void loge(String str) {
        Log.e("MultiSimSettingController", str);
    }
}
