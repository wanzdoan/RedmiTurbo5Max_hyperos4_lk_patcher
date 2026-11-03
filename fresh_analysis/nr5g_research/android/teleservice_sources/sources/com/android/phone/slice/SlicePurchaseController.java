package com.android.phone.slice;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.NetworkSlicingConfig;
import android.telephony.data.RouteSelectionDescriptor;
import android.telephony.data.TrafficDescriptor;
import android.telephony.data.UrspRule;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.IntPredicate;

/* JADX INFO: loaded from: classes.dex */
public class SlicePurchaseController extends Handler {
    private static final ComponentName SLICE_PURCHASE_APP_COMPONENT_NAME = ComponentName.unflattenFromString("com.android.carrierdefaultapp/.SlicePurchaseBroadcastReceiver");
    private static Map sInstances = new HashMap();
    private int mDailyCount;
    private final FeatureFlags mFeatureFlags;
    private boolean mIsSlicingUpsellEnabled;
    private LocalDate mLocalDate;
    private int mMonthlyCount;
    private final Map mPendingPurchaseCapabilities;
    private final Set mPendingSetupCapabilities;
    private final Phone mPhone;
    private final Map mSlicePurchaseControllerBroadcastReceivers;
    private NetworkSlicingConfig mSlicingConfig;
    private final Set mThrottledCapabilities;

    class SlicePurchaseControllerBroadcastReceiver extends BroadcastReceiver {
        private final int mCapability;

        SlicePurchaseControllerBroadcastReceiver(int i) {
            this.mCapability = i;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            SlicePurchaseController.this.logd("SlicePurchaseControllerBroadcastReceiver(" + TelephonyManager.convertPremiumCapabilityToString(this.mCapability) + ") received action: " + action);
            int intExtra = intent.getIntExtra("com.android.phone.slice.extra.PHONE_ID", -1);
            int intExtra2 = intent.getIntExtra("com.android.phone.slice.extra.PREMIUM_CAPABILITY", -1);
            if (SlicePurchaseController.getInstance(intExtra) == null) {
                SlicePurchaseController.this.reportAnomaly("ced79f1a-8ac0-4260-8cf3-08b54c0494f3", "SlicePurchaseControllerBroadcastReceiver( " + TelephonyManager.convertPremiumCapabilityToString(this.mCapability) + ") received invalid phoneId: " + intExtra);
            }
            if (intExtra2 != this.mCapability) {
                SlicePurchaseController.this.logd("SlicePurchaseControllerBroadcastReceiver(" + TelephonyManager.convertPremiumCapabilityToString(this.mCapability) + ") ignoring intent for capability " + TelephonyManager.convertPremiumCapabilityToString(intExtra2));
                return;
            }
            action.hashCode();
            switch (action) {
                case "com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_SUCCESS":
                    SlicePurchaseController.getInstance(intExtra).onCarrierSuccess(intExtra2, intent.getLongExtra("com.android.phone.slice.extra.PURCHASE_DURATION", 0L));
                    break;
                case "com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_CARRIER_ERROR":
                    SlicePurchaseController.getInstance(intExtra).onCarrierError(intExtra2, intent.getIntExtra("com.android.phone.slice.extra.FAILURE_CODE", 0), intent.getStringExtra("com.android.phone.slice.extra.FAILURE_REASON"));
                    break;
                case "com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_CANCELED":
                    SlicePurchaseController.this.logd("Slice purchase application canceled for capability: " + TelephonyManager.convertPremiumCapabilityToString(intExtra2));
                    SlicePurchaseController.getInstance(intExtra).handlePurchaseResult(intExtra2, 6, true);
                    break;
                case "com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOT_DEFAULT_DATA_SUBSCRIPTION":
                    SlicePurchaseController.this.logd("Purchase premium capability request was not made on the default data subscription for capability: " + TelephonyManager.convertPremiumCapabilityToString(intExtra2));
                    SlicePurchaseController.getInstance(intExtra).handlePurchaseResult(intExtra2, 14, false);
                    break;
                case "com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOTIFICATION_SHOWN":
                    SlicePurchaseController.getInstance(intExtra).onNotificationShown();
                    break;
                case "com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_REQUEST_FAILED":
                    SlicePurchaseController.this.logd("Purchase premium capability request failed for capability: " + TelephonyManager.convertPremiumCapabilityToString(intExtra2));
                    SlicePurchaseController.getInstance(intExtra).handlePurchaseResult(intExtra2, 11, false);
                    break;
                case "com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOTIFICATIONS_DISABLED":
                    SlicePurchaseController.this.logd("Slice purchase application unable to show notification for capability: " + TelephonyManager.convertPremiumCapabilityToString(intExtra2) + " because the user has disabled notifications.");
                    FeatureFlags unused = SlicePurchaseController.this.mFeatureFlags;
                    SlicePurchaseController.getInstance(intExtra).handlePurchaseResult(intExtra2, 16, true);
                    break;
                default:
                    SlicePurchaseController.this.reportAnomaly("0197efb0-dab1-4b0a-abaf-ac9336ec7923", "SlicePurchaseControllerBroadcastReceiver(" + TelephonyManager.convertPremiumCapabilityToString(this.mCapability) + ") received unknown action: " + action);
                    break;
            }
        }
    }

    public static synchronized SlicePurchaseController getInstance(Phone phone, FeatureFlags featureFlags) {
        int phoneId;
        try {
            phoneId = phone.getPhoneId();
            if (sInstances.get(Integer.valueOf(phoneId)) == null) {
                HandlerThread handlerThread = new HandlerThread("SlicePurchaseController");
                handlerThread.start();
                sInstances.put(Integer.valueOf(phoneId), new SlicePurchaseController(phone, featureFlags, handlerThread.getLooper()));
            }
        } catch (Throwable th) {
            throw th;
        }
        return (SlicePurchaseController) sInstances.get(Integer.valueOf(phoneId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SlicePurchaseController getInstance(int i) {
        return (SlicePurchaseController) sInstances.get(Integer.valueOf(i));
    }

    public SlicePurchaseController(Phone phone, FeatureFlags featureFlags, Looper looper) {
        super(looper);
        this.mPendingSetupCapabilities = new HashSet();
        this.mThrottledCapabilities = new HashSet();
        this.mPendingPurchaseCapabilities = new HashMap();
        this.mSlicePurchaseControllerBroadcastReceivers = new HashMap();
        this.mPhone = phone;
        this.mFeatureFlags = featureFlags;
        phone.mCi.registerForSlicingConfigChanged(this, 2, (Object) null);
        this.mIsSlicingUpsellEnabled = DeviceConfig.getBoolean("telephony", "enable_slicing_upsell", false);
        DeviceConfig.addOnPropertiesChangedListener("telephony", new ProfileInstallReceiver$$ExternalSyntheticLambda0(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.phone.slice.SlicePurchaseController$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                this.f$0.lambda$new$0(properties);
            }
        });
        updateNotificationCounts();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        if (TextUtils.equals("telephony", properties.getNamespace())) {
            sendEmptyMessage(6);
        }
    }

    public void setLocalDate(LocalDate localDate) {
        this.mLocalDate = localDate;
    }

    public void setSlicingConfig(NetworkSlicingConfig networkSlicingConfig) {
        this.mSlicingConfig = networkSlicingConfig;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                Integer num = (Integer) message.obj;
                logd("EVENT_PURCHASE_UNTHROTTLED: for capability " + TelephonyManager.convertPremiumCapabilityToString(num.intValue()));
                this.mThrottledCapabilities.remove(num);
                break;
            case 2:
                NetworkSlicingConfig networkSlicingConfig = (NetworkSlicingConfig) ((AsyncResult) message.obj).result;
                logd("EVENT_SLICING_CONFIG_CHANGED: previous= " + this.mSlicingConfig);
                logd("EVENT_SLICING_CONFIG_CHANGED: current= " + networkSlicingConfig);
                this.mSlicingConfig = networkSlicingConfig;
                onSlicingConfigChanged();
                break;
            case 3:
                int iIntValue = ((Integer) message.obj).intValue();
                logd("EVENT_START_SLICE_PURCHASE_APP: " + TelephonyManager.convertPremiumCapabilityToString(iIntValue));
                onStartSlicePurchaseApplication(iIntValue);
                break;
            case 4:
                int iIntValue2 = ((Integer) message.obj).intValue();
                logd("EVENT_PURCHASE_TIMEOUT: for capability " + TelephonyManager.convertPremiumCapabilityToString(iIntValue2));
                onTimeout(iIntValue2);
                break;
            case 5:
                int iIntValue3 = ((Integer) message.obj).intValue();
                logd("EVENT_SETUP_TIMEOUT: for capability " + TelephonyManager.convertPremiumCapabilityToString(iIntValue3));
                onSetupTimeout(iIntValue3);
                break;
            case 6:
                boolean z = DeviceConfig.getBoolean("telephony", "enable_slicing_upsell", false);
                if (z != this.mIsSlicingUpsellEnabled) {
                    logd("EVENT_DEVICE_CONFIG_CHANGED: from " + this.mIsSlicingUpsellEnabled + " to " + z);
                    this.mIsSlicingUpsellEnabled = z;
                }
                break;
            default:
                loge("Unknown event: " + message.obj);
                break;
        }
    }

    public boolean isPremiumCapabilityAvailableForPurchase(int i) {
        if (!arePremiumCapabilitiesSupportedByDevice()) {
            logd("Premium capabilities unsupported by the device.");
            return false;
        }
        if (!isPremiumCapabilitySupportedByCarrier(i)) {
            logd("Premium capability " + TelephonyManager.convertPremiumCapabilityToString(i) + " unsupported by the carrier.");
            return false;
        }
        if (!isDefaultDataSub()) {
            logd("Premium capability " + TelephonyManager.convertPremiumCapabilityToString(i) + " unavailable on the non-default data subscription.");
            return false;
        }
        logd("Premium capability " + TelephonyManager.convertPremiumCapabilityToString(i) + " is available for purchase.");
        return true;
    }

    public synchronized void purchasePremiumCapability(int i, Message message) {
        logd("purchasePremiumCapability: " + TelephonyManager.convertPremiumCapabilityToString(i));
        if (!arePremiumCapabilitiesSupportedByDevice()) {
            sendPurchaseResult(i, 10, message);
            return;
        }
        if (!isPremiumCapabilitySupportedByCarrier(i)) {
            sendPurchaseResult(i, 7, message);
            return;
        }
        if (!isDefaultDataSub()) {
            sendPurchaseResult(i, 14, message);
            return;
        }
        if (isSlicingConfigActive(i)) {
            sendPurchaseResult(i, 3, message);
            return;
        }
        if (this.mPendingSetupCapabilities.contains(Integer.valueOf(i))) {
            sendPurchaseResult(i, 15, message);
            return;
        }
        if (this.mThrottledCapabilities.contains(Integer.valueOf(i))) {
            sendPurchaseResult(i, 2, message);
            return;
        }
        if (!isNetworkAvailable()) {
            sendPurchaseResult(i, 12, message);
        } else if (this.mPendingPurchaseCapabilities.containsKey(Integer.valueOf(i))) {
            sendPurchaseResult(i, 4, message);
        } else {
            this.mPendingPurchaseCapabilities.put(Integer.valueOf(i), message);
            sendMessage(obtainMessage(3, Integer.valueOf(i)));
        }
    }

    private void sendPurchaseResult(int i, int i2, Message message) {
        logd("Purchase result for capability " + TelephonyManager.convertPremiumCapabilityToString(i) + ": " + TelephonyManager.convertPurchaseResultToString(i2));
        AsyncResult.forMessage(message, Integer.valueOf(i2), (Throwable) null);
        message.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePurchaseResult(int i, int i2, boolean z) {
        SlicePurchaseControllerBroadcastReceiver slicePurchaseControllerBroadcastReceiver = (SlicePurchaseControllerBroadcastReceiver) this.mSlicePurchaseControllerBroadcastReceivers.remove(Integer.valueOf(i));
        if (slicePurchaseControllerBroadcastReceiver != null) {
            this.mPhone.getContext().unregisterReceiver(slicePurchaseControllerBroadcastReceiver);
        }
        removeMessages(4, Integer.valueOf(i));
        if (z) {
            throttleCapability(i, getThrottleDuration(i2));
        }
        sendPurchaseResult(i, i2, (Message) this.mPendingPurchaseCapabilities.remove(Integer.valueOf(i)));
    }

    private void throttleCapability(int i, long j) {
        if (this.mThrottledCapabilities.contains(Integer.valueOf(i))) {
            reportAnomaly("15574927-e2e2-4593-99d4-2f340d22b383", TelephonyManager.convertPremiumCapabilityToString(i) + " is already throttled.");
            return;
        }
        if (j > 0) {
            logd("Throttle purchase requests for capability " + TelephonyManager.convertPremiumCapabilityToString(i) + " for " + (j / RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) + " minutes.");
            this.mThrottledCapabilities.add(Integer.valueOf(i));
            sendMessageDelayed(obtainMessage(1, Integer.valueOf(i)), j);
        }
    }

    private void onSlicingConfigChanged() {
        int i = new int[]{34}[0];
        if (isSlicingConfigActive(i) && hasMessages(5, Integer.valueOf(i))) {
            logd("Successfully set up slicing configuration for " + TelephonyManager.convertPremiumCapabilityToString(i));
            this.mPendingSetupCapabilities.remove(Integer.valueOf(i));
            removeMessages(5, Integer.valueOf(i));
        }
    }

    public PremiumNetworkEntitlementApi getPremiumNetworkEntitlementApi() {
        return new PremiumNetworkEntitlementApi(this.mPhone, getCarrierConfigs());
    }

    private void onStartSlicePurchaseApplication(int i) {
        updateNotificationCounts();
        if (this.mMonthlyCount >= getCarrierConfigs().getInt("premium_capability_maximum_monthly_notification_count_int") || this.mDailyCount >= getCarrierConfigs().getInt("premium_capability_maximum_daily_notification_count_int")) {
            logd("Reached maximum number of performance boost notifications.");
            handlePurchaseResult(i, 2, false);
            return;
        }
        PremiumNetworkEntitlementResponse premiumNetworkEntitlementResponseCheckEntitlementStatus = getPremiumNetworkEntitlementApi().checkEntitlementStatus(i);
        if (premiumNetworkEntitlementResponseCheckEntitlementStatus == null || premiumNetworkEntitlementResponseCheckEntitlementStatus.isInvalidResponse()) {
            loge("Invalid response for entitlement check: " + premiumNetworkEntitlementResponseCheckEntitlementStatus);
            handlePurchaseResult(i, 8, true);
            return;
        }
        if (!premiumNetworkEntitlementResponseCheckEntitlementStatus.isPremiumNetworkCapabilityAllowed()) {
            loge("Entitlement Check: Not allowed.");
            handlePurchaseResult(i, 13, true);
            return;
        }
        if (premiumNetworkEntitlementResponseCheckEntitlementStatus.isAlreadyPurchased()) {
            logd("Entitlement Check: Already purchased/provisioned.");
            handlePurchaseResult(i, 3, true);
            return;
        }
        if (premiumNetworkEntitlementResponseCheckEntitlementStatus.isProvisioningInProgress()) {
            logd("Entitlement Check: In progress.");
            handlePurchaseResult(i, 4, true);
            return;
        }
        String purchaseUrl = getPurchaseUrl(premiumNetworkEntitlementResponseCheckEntitlementStatus);
        String simOperator = getSimOperator();
        if (TextUtils.isEmpty(purchaseUrl) || TextUtils.isEmpty(simOperator)) {
            handlePurchaseResult(i, 7, false);
            return;
        }
        long j = getCarrierConfigs().getLong("premium_capability_notification_display_timeout_millis_long");
        logd("Start purchase timeout for " + TelephonyManager.convertPremiumCapabilityToString(i) + " for " + (j / RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) + " minutes.");
        sendMessageDelayed(obtainMessage(4, Integer.valueOf(i)), j);
        Intent intent = new Intent("com.android.phone.slice.action.START_SLICE_PURCHASE_APP");
        intent.setComponent(SLICE_PURCHASE_APP_COMPONENT_NAME);
        intent.putExtra("com.android.phone.slice.extra.PHONE_ID", this.mPhone.getPhoneId());
        intent.putExtra("com.android.phone.slice.extra.SUB_ID", this.mPhone.getSubId());
        intent.putExtra("com.android.phone.slice.extra.PREMIUM_CAPABILITY", i);
        intent.putExtra("com.android.phone.slice.extra.PURCHASE_URL", purchaseUrl);
        intent.putExtra("com.android.phone.slice.extra.CARRIER", simOperator);
        intent.putExtra("com.android.phone.slice.extra.USER_DATA", premiumNetworkEntitlementResponseCheckEntitlementStatus.mServiceFlowUserData);
        intent.putExtra("com.android.phone.slice.extra.CONTENTS_TYPE", premiumNetworkEntitlementResponseCheckEntitlementStatus.mServiceFlowContentsType);
        intent.putExtra("com.android.phone.slice.extra.INTENT_CANCELED", createPendingIntent("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_CANCELED", i, false));
        intent.putExtra("com.android.phone.slice.extra.INTENT_CARRIER_ERROR", createPendingIntent("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_CARRIER_ERROR", i, true));
        intent.putExtra("com.android.phone.slice.extra.INTENT_REQUEST_FAILED", createPendingIntent("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_REQUEST_FAILED", i, false));
        intent.putExtra("com.android.phone.slice.extra.INTENT_NOT_DEFAULT_DATA_SUBSCRIPTION", createPendingIntent("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOT_DEFAULT_DATA_SUBSCRIPTION", i, false));
        intent.putExtra("com.android.phone.slice.extra.INTENT_NOTIFICATIONS_DISABLED", createPendingIntent("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOTIFICATIONS_DISABLED", i, false));
        intent.putExtra("com.android.phone.slice.extra.INTENT_SUCCESS", createPendingIntent("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_SUCCESS", i, true));
        intent.putExtra("com.android.phone.slice.extra.NOTIFICATION_SHOWN", createPendingIntent("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOTIFICATION_SHOWN", i, false));
        logd("Broadcasting start intent to SlicePurchaseBroadcastReceiver.");
        this.mPhone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
        this.mSlicePurchaseControllerBroadcastReceivers.put(Integer.valueOf(i), new SlicePurchaseControllerBroadcastReceiver(i));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_CANCELED");
        intentFilter.addAction("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_CARRIER_ERROR");
        intentFilter.addAction("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_REQUEST_FAILED");
        intentFilter.addAction("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOT_DEFAULT_DATA_SUBSCRIPTION");
        intentFilter.addAction("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOTIFICATIONS_DISABLED");
        intentFilter.addAction("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_SUCCESS");
        intentFilter.addAction("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_NOTIFICATION_SHOWN");
        this.mPhone.getContext().registerReceiver((BroadcastReceiver) this.mSlicePurchaseControllerBroadcastReceivers.get(Integer.valueOf(i)), intentFilter, 4);
    }

    public String getPurchaseUrl(PremiumNetworkEntitlementResponse premiumNetworkEntitlementResponse) {
        String string = premiumNetworkEntitlementResponse.mServiceFlowURL;
        if (!isUrlValid(string)) {
            string = getCarrierConfigs().getString("premium_capability_purchase_url_string");
            if (!isUrlValid(string)) {
                return "";
            }
        }
        return string;
    }

    public String getSimOperator() {
        if (this.mPhone.getPhoneId() < TelephonyProperties.icc_operator_alpha().size()) {
            return (String) TelephonyProperties.icc_operator_alpha().get(this.mPhone.getPhoneId());
        }
        return null;
    }

    public PendingIntent createPendingIntent(String str, int i, boolean z) {
        Intent intent = new Intent(str);
        intent.putExtra("com.android.phone.slice.extra.PHONE_ID", this.mPhone.getPhoneId());
        intent.putExtra("com.android.phone.slice.extra.PREMIUM_CAPABILITY", i);
        intent.setPackage(this.mPhone.getContext().getPackageName());
        return PendingIntent.getBroadcast(this.mPhone.getContext(), i, intent, (z ? 33554432 : 67108864) | 268435456);
    }

    private void onTimeout(int i) {
        logd("onTimeout: " + TelephonyManager.convertPremiumCapabilityToString(i));
        Intent intent = new Intent("com.android.phone.slice.action.SLICE_PURCHASE_APP_RESPONSE_TIMEOUT");
        intent.setComponent(SLICE_PURCHASE_APP_COMPONENT_NAME);
        intent.putExtra("com.android.phone.slice.extra.PHONE_ID", this.mPhone.getPhoneId());
        intent.putExtra("com.android.phone.slice.extra.PREMIUM_CAPABILITY", i);
        logd("Broadcasting timeout intent to SlicePurchaseBroadcastReceiver.");
        this.mPhone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
        handlePurchaseResult(i, 9, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierError(int i, int i2, String str) {
        logd("Carrier error for capability: " + TelephonyManager.convertPremiumCapabilityToString(i) + " with code: " + convertFailureCodeToString(i2) + " and reason: " + str);
        if (i2 == 0 && !TextUtils.isEmpty(str)) {
            reportAnomaly("76943b23-4415-400c-9855-b534fc4fc62c", "Failure code needs to be added for: " + str);
        }
        handlePurchaseResult(i, 8, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierSuccess(int i, long j) {
        String str;
        StringBuilder sb = new StringBuilder("Successfully purchased premium capability ");
        sb.append(TelephonyManager.convertPremiumCapabilityToString(i));
        if (j > 0) {
            str = " for " + (j / RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) + " minutes.";
        } else {
            str = ".";
        }
        sb.append(str);
        logd(sb.toString());
        this.mPendingSetupCapabilities.add(Integer.valueOf(i));
        long j2 = getCarrierConfigs().getLong("premium_capability_network_setup_time_millis_long");
        logd("Waiting " + (j2 / RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) + " minutes for the network to set up the slicing configuration.");
        sendMessageDelayed(obtainMessage(5, Integer.valueOf(i)), j2);
        handlePurchaseResult(i, 1, false);
    }

    private void onSetupTimeout(int i) {
        logd("onSetupTimeout: " + TelephonyManager.convertPremiumCapabilityToString(i));
        this.mPendingSetupCapabilities.remove(Integer.valueOf(i));
        if (isSlicingConfigActive(i)) {
            return;
        }
        reportAnomaly("12eeffbf-08f8-40ed-9a00-d344199552fc", "Failed to set up slicing configuration for capability " + TelephonyManager.convertPremiumCapabilityToString(i) + " within the time specified.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNotificationShown() {
        SharedPreferences sharedPreferences = this.mPhone.getContext().getSharedPreferences("performance_boost_notification_preferences", 0);
        this.mDailyCount = sharedPreferences.getInt("daily_notification_count" + this.mPhone.getPhoneId(), 0) + 1;
        this.mMonthlyCount = sharedPreferences.getInt("monthly_notification_count" + this.mPhone.getPhoneId(), 0) + 1;
        logd("Performance boost notification was shown " + this.mDailyCount + " times today and " + this.mMonthlyCount + " times this month.");
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder("daily_notification_count");
        sb.append(this.mPhone.getPhoneId());
        editorEdit.putInt(sb.toString(), this.mDailyCount);
        editorEdit.putInt("monthly_notification_count" + this.mPhone.getPhoneId(), this.mMonthlyCount);
        editorEdit.apply();
    }

    public void updateNotificationCounts() {
        SharedPreferences sharedPreferences = this.mPhone.getContext().getSharedPreferences("performance_boost_notification_preferences", 0);
        this.mDailyCount = sharedPreferences.getInt("daily_notification_count" + this.mPhone.getPhoneId(), 0);
        this.mMonthlyCount = sharedPreferences.getInt("monthly_notification_count" + this.mPhone.getPhoneId(), 0);
        if (this.mLocalDate == null) {
            this.mLocalDate = LocalDate.now(ZoneId.of("UTC"));
        }
        boolean z = true;
        LocalDate localDateOf = LocalDate.of(1, 1, 1);
        String string = sharedPreferences.getString("notification_count_last_reset_date" + this.mPhone.getPhoneId(), "");
        if (!TextUtils.isEmpty(string)) {
            try {
                localDateOf = LocalDate.parse(string);
            } catch (DateTimeParseException e) {
                loge("Error parsing LocalDate from SharedPreferences: " + e);
            }
        }
        logd("updateNotificationCounts: mDailyCount=" + this.mDailyCount + ", mMonthlyCount=" + this.mMonthlyCount + ", mLocalDate=" + this.mLocalDate + ", lastLocalDate=" + localDateOf);
        if (localDateOf.getYear() == this.mLocalDate.getYear() && localDateOf.getMonthValue() == this.mLocalDate.getMonthValue()) {
            z = false;
        }
        if (z || localDateOf.getDayOfMonth() != this.mLocalDate.getDayOfMonth()) {
            StringBuilder sb = new StringBuilder("Resetting daily");
            sb.append(z ? " and monthly" : "");
            sb.append(" notification count.");
            logd(sb.toString());
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            if (z) {
                this.mMonthlyCount = 0;
                editorEdit.putInt("monthly_notification_count" + this.mPhone.getPhoneId(), this.mMonthlyCount);
            }
            this.mDailyCount = 0;
            editorEdit.putInt("daily_notification_count" + this.mPhone.getPhoneId(), this.mDailyCount);
            editorEdit.putString("notification_count_last_reset_date" + this.mPhone.getPhoneId(), this.mLocalDate.toString());
            editorEdit.apply();
        }
    }

    private PersistableBundle getCarrierConfigs() {
        return ((CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class)).getConfigForSubId(this.mPhone.getSubId());
    }

    private long getThrottleDuration(int i) {
        if (i == 6 || i == 9 || i == 16) {
            return getCarrierConfigs().getLong("premium_capability_notification_backoff_hysteresis_time_millis_long");
        }
        if (i == 13 || i == 8) {
            return getCarrierConfigs().getLong("premium_capability_purchase_condition_backoff_hysteresis_time_millis_long");
        }
        return 0L;
    }

    private boolean isPremiumCapabilitySupportedByCarrier(final int i) {
        int[] intArray = getCarrierConfigs().getIntArray("supported_premium_capabilities_int_array");
        if (intArray == null) {
            logd("No premium capabilities are supported by the carrier.");
            return false;
        }
        return Arrays.stream(intArray).anyMatch(new IntPredicate() { // from class: com.android.phone.slice.SlicePurchaseController$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i2) {
                return SlicePurchaseController.m3746$r8$lambda$VuqscN6FqBLSb54chbLUv0KwFM(i, i2);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$Vuqsc-N6FqBLSb54chbLUv0KwFM, reason: not valid java name */
    public static /* synthetic */ boolean m3746$r8$lambda$VuqscN6FqBLSb54chbLUv0KwFM(int i, int i2) {
        return i2 == i;
    }

    private boolean isUrlValid(String str) {
        if (!URLUtil.isValidUrl(str)) {
            loge("Invalid URL: " + str);
            return false;
        }
        if (URLUtil.isAssetUrl(str) && !str.equals("file:///android_asset/slice_purchase_test.html")) {
            loge("Invalid asset: ".concat(str));
            return false;
        }
        try {
            new URL(str).toURI();
            logd("Valid URL: " + str);
            return true;
        } catch (MalformedURLException | URISyntaxException unused) {
            loge("Invalid URI: " + str);
            return false;
        }
    }

    private boolean arePremiumCapabilitiesSupportedByDevice() {
        if ((((long) this.mPhone.getCachedAllowedNetworkTypesBitmask()) & 524288) == 0) {
            logd("Premium capabilities unsupported because NR is not allowed on the device.");
            return false;
        }
        if (!this.mIsSlicingUpsellEnabled) {
            logd("Premium capabilities unsupported because slicing upsell is disabled on the device.");
        }
        return this.mIsSlicingUpsellEnabled;
    }

    private boolean isDefaultDataSub() {
        return this.mPhone.getSubId() == SubscriptionManager.getDefaultDataSubscriptionId();
    }

    public boolean isSlicingConfigActive(int i) {
        NetworkSlicingConfig networkSlicingConfig = this.mSlicingConfig;
        if (networkSlicingConfig == null) {
            return false;
        }
        for (UrspRule urspRule : networkSlicingConfig.getUrspRules()) {
            Iterator<TrafficDescriptor> it = urspRule.getTrafficDescriptors().iterator();
            while (it.hasNext()) {
                if (new TrafficDescriptor.OsAppId(it.next().getOsAppId()).getAppId().equals(getAppId(i))) {
                    Iterator<RouteSelectionDescriptor> it2 = urspRule.getRouteSelectionDescriptor().iterator();
                    while (it2.hasNext()) {
                        for (NetworkSliceInfo networkSliceInfo : it2.next().getSliceInfo()) {
                            if (networkSliceInfo.getStatus() == 2 && getSliceServiceTypes(i).contains(Integer.valueOf(networkSliceInfo.getSliceServiceType()))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static String getAppId(int i) {
        if (i == 34) {
            return "PRIORITIZE_LATENCY";
        }
        return "";
    }

    public static Set getSliceServiceTypes(int i) {
        HashSet hashSet = new HashSet();
        if (i == 34) {
            hashSet.add(1);
            hashSet.add(2);
            return hashSet;
        }
        hashSet.add(0);
        return hashSet;
    }

    private boolean isNetworkAvailable() {
        if (this.mPhone.getServiceState().getDataRoaming()) {
            logd("Network unavailable because device is roaming.");
            return false;
        }
        if (!this.mPhone.getDataSettingsManager().isDataEnabledForReason(0)) {
            logd("Network unavailable because user data is disabled.");
            return false;
        }
        int dataNetworkType = this.mPhone.getServiceState().getDataNetworkType();
        if (dataNetworkType == 13 || dataNetworkType == 19) {
            return getCarrierConfigs().getBoolean("premium_capability_supported_on_lte_bool");
        }
        return dataNetworkType == 20;
    }

    private static String convertFailureCodeToString(int i) {
        if (i == 0) {
            return "UNKNOWN";
        }
        if (i == 1) {
            return "CARRIER_URL_UNAVAILABLE";
        }
        if (i == 2) {
            return "AUTHENTICATION_FAILED";
        }
        if (i == 3) {
            return "PAYMENT_FAILED";
        }
        if (i == 4) {
            return "NO_USER_DATA";
        }
        return "UNKNOWN(" + i + ")";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportAnomaly(String str, String str2) {
        loge(str2);
        AnomalyReporter.reportAnomaly(UUID.fromString(str), str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d("SlicePurchaseController-" + this.mPhone.getPhoneId(), str);
    }

    private void loge(String str) {
        Log.e("SlicePurchaseController-" + this.mPhone.getPhoneId(), str);
    }
}
