package com.android.internal.telephony;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.NotificationChannelController;
import com.android.telephony.Rlog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class CarrierServiceStateTracker extends Handler {
    public static final String ACTION_NEVER_ASK_AGAIN = "com.android.internal.telephony.action.SILENCE_WIFI_CALLING_NOTIFICATION";
    protected static final int CARRIER_EVENT_BASE = 100;
    protected static final int CARRIER_EVENT_DATA_DEREGISTRATION = 104;
    protected static final int CARRIER_EVENT_DATA_REGISTRATION = 103;
    protected static final int CARRIER_EVENT_IMS_CAPABILITIES_CHANGED = 105;
    protected static final int CARRIER_EVENT_VOICE_DEREGISTRATION = 102;
    protected static final int CARRIER_EVENT_VOICE_REGISTRATION = 101;
    public static final String EMERGENCY_NOTIFICATION_TAG = "EmergencyNetworkNotification";
    public static final int NOTIFICATION_EMERGENCY_NETWORK = 1001;
    public static final int NOTIFICATION_PREF_NETWORK = 1000;
    public static final String PREF_NETWORK_NOTIFICATION_TAG = "PrefNetworkNotification";
    public final NotificationActionReceiver mActionReceiver;
    private long mAllowedNetworkType;
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    private final FeatureFlags mFeatureFlags;
    private Phone mPhone;
    private ServiceStateTracker mSST;
    private TelephonyManager mTelephonyManager;
    private final Map mNotificationTypeMap = new HashMap();
    private int mPreviousSubId = -1;

    public interface NotificationType {
        int getDelay();

        Notification.Builder getNotificationBuilder();

        int getNotificationId();

        String getNotificationTag();

        int getTypeId();

        boolean isEnabled();

        boolean sendMessage();

        void setDelay(PersistableBundle persistableBundle);

        void setEnabled(PersistableBundle persistableBundle);
    }

    private boolean checkSupportedBitmask(long j, long j2) {
        return (j2 & j) == j2;
    }

    public class AllowedNetworkTypesListener extends TelephonyCallback implements TelephonyCallback.AllowedNetworkTypesListener {
        public AllowedNetworkTypesListener() {
        }

        public void onAllowedNetworkTypesChanged(int i, long j) {
            if (i == 0 && CarrierServiceStateTracker.this.mAllowedNetworkType != j) {
                CarrierServiceStateTracker.this.mAllowedNetworkType = j;
                CarrierServiceStateTracker.this.handleAllowedNetworkTypeChanged();
            }
        }
    }

    public CarrierServiceStateTracker(Phone phone, ServiceStateTracker serviceStateTracker, FeatureFlags featureFlags) {
        NotificationActionReceiver notificationActionReceiver = new NotificationActionReceiver();
        this.mActionReceiver = notificationActionReceiver;
        this.mAllowedNetworkType = -1L;
        this.mFeatureFlags = featureFlags;
        this.mPhone = phone;
        this.mSST = serviceStateTracker;
        this.mTelephonyManager = ((TelephonyManager) phone.getContext().getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mPhone.getSubId());
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(this.mPhone.getContext().getMainExecutor(), new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.CarrierServiceStateTracker$$ExternalSyntheticLambda0
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        SubscriptionManager.from(this.mPhone.getContext()).addOnSubscriptionsChangedListener(new SubscriptionManager.OnSubscriptionsChangedListener(getLooper()) { // from class: com.android.internal.telephony.CarrierServiceStateTracker.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                int subId = CarrierServiceStateTracker.this.mPhone.getSubId();
                if (CarrierServiceStateTracker.this.mPreviousSubId != subId) {
                    CarrierServiceStateTracker.this.mPreviousSubId = subId;
                    CarrierServiceStateTracker carrierServiceStateTracker = CarrierServiceStateTracker.this;
                    carrierServiceStateTracker.mTelephonyManager = carrierServiceStateTracker.mTelephonyManager.createForSubscriptionId(CarrierServiceStateTracker.this.mPhone.getSubId());
                    CarrierServiceStateTracker.this.registerAllowedNetworkTypesListener();
                }
            }
        });
        if (!this.mPhone.getContext().getPackageManager().hasSystemFeature("android.hardware.type.watch")) {
            registerNotificationTypes();
        }
        this.mAllowedNetworkType = RadioAccessFamily.getNetworkTypeFromRaf((int) this.mPhone.getAllowedNetworkTypes(0));
        this.mAllowedNetworkTypesListener = new AllowedNetworkTypesListener();
        registerAllowedNetworkTypesListener();
        this.mPhone.getContext().registerReceiver(notificationActionReceiver, new IntentFilter(ACTION_NEVER_ASK_AGAIN), 4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        if (i != this.mPhone.getPhoneId()) {
            return;
        }
        Rlog.d("CSST", "onCarrierConfigChanged: slotIndex=" + i + ", subId=" + i2 + ", carrierId=" + i3);
        PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mPhone.getContext(), this.mPhone.getSubId(), new String[]{"emergency_notification_delay_int", "network_notification_delay_int", "hide_preferred_network_type_bool"});
        if (carrierConfigSubset.isEmpty()) {
            return;
        }
        Iterator it = this.mNotificationTypeMap.entrySet().iterator();
        while (it.hasNext()) {
            NotificationType notificationType = (NotificationType) ((Map.Entry) it.next()).getValue();
            notificationType.setDelay(carrierConfigSubset);
            notificationType.setEnabled(carrierConfigSubset);
        }
        handleConfigChanges();
    }

    public AllowedNetworkTypesListener getAllowedNetworkTypesChangedListener() {
        return this.mAllowedNetworkTypesListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerAllowedNetworkTypesListener() {
        TelephonyManager telephonyManager;
        int subId = this.mPhone.getSubId();
        unregisterAllowedNetworkTypesListener();
        if (!SubscriptionManager.isValidSubscriptionId(subId) || (telephonyManager = this.mTelephonyManager) == null) {
            return;
        }
        telephonyManager.registerTelephonyCallback(new HandlerExecutor(this), this.mAllowedNetworkTypesListener);
    }

    private void unregisterAllowedNetworkTypesListener() {
        this.mTelephonyManager.unregisterTelephonyCallback(this.mAllowedNetworkTypesListener);
    }

    public Map<Integer, NotificationType> getNotificationTypeMap() {
        return this.mNotificationTypeMap;
    }

    private void registerNotificationTypes() {
        this.mNotificationTypeMap.put(1000, new PrefNetworkNotification(1000));
        this.mNotificationTypeMap.put(1001, new EmergencyNetworkNotification(1001));
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i != 1000 && i != 1001) {
            switch (i) {
                case 101:
                case 102:
                case 103:
                case 104:
                    handleConfigChanges();
                    break;
                case 105:
                    handleImsCapabilitiesChanged();
                    break;
            }
            return;
        }
        Rlog.d("CSST", "sending notification after delay: " + message.what);
        NotificationType notificationType = (NotificationType) this.mNotificationTypeMap.get(Integer.valueOf(message.what));
        if (notificationType != null) {
            sendNotification(notificationType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPhoneStillRegistered() {
        ServiceState serviceState = this.mSST.mSS;
        return serviceState == null || serviceState.getState() == 0 || this.mSST.mSS.getDataRegistrationState() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPhoneRegisteredForWifiCalling() {
        Rlog.d("CSST", "isPhoneRegisteredForWifiCalling: " + this.mPhone.isWifiCallingEnabled());
        return this.mPhone.isWifiCallingEnabled();
    }

    public boolean isRadioOffOrAirplaneMode() {
        try {
            return (this.mSST.isRadioOn() && Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "airplane_mode_on", 0) == 0) ? false : true;
        } catch (Exception unused) {
            Rlog.e("CSST", "Unable to get AIRPLACE_MODE_ON.");
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGlobalMode() {
        try {
            int iCalculatePreferredNetworkType = PhoneFactory.calculatePreferredNetworkType(this.mPhone.getPhoneId());
            if (isNrSupported()) {
                return iCalculatePreferredNetworkType == RadioAccessFamily.getRafFromNetworkType(27);
            }
            return iCalculatePreferredNetworkType == RadioAccessFamily.getRafFromNetworkType(10);
        } catch (Exception unused) {
            Rlog.e("CSST", "Unable to get PREFERRED_NETWORK_MODE.");
            return true;
        }
    }

    private boolean isNrSupported() {
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).createForSubscriptionId(this.mPhone.getSubId());
        boolean zIsCarrierConfigEnableNr = isCarrierConfigEnableNr();
        boolean zCheckSupportedBitmask = checkSupportedBitmask(telephonyManagerCreateForSubscriptionId.getSupportedRadioAccessFamily(), 524288L);
        boolean zCheckSupportedBitmask2 = checkSupportedBitmask(telephonyManagerCreateForSubscriptionId.getAllowedNetworkTypesForReason(2), 524288L);
        Rlog.i("CSST", "isNrSupported:  carrierConfigEnabled: " + zIsCarrierConfigEnableNr + ", AccessFamilySupported: " + zCheckSupportedBitmask + ", isNrNetworkTypeAllowed: " + zCheckSupportedBitmask2);
        return zIsCarrierConfigEnableNr && zCheckSupportedBitmask && zCheckSupportedBitmask2;
    }

    private boolean isCarrierConfigEnableNr() {
        PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mPhone.getContext(), this.mPhone.getSubId(), new String[]{"carrier_nr_availabilities_int_array"});
        if (carrierConfigSubset.isEmpty()) {
            Rlog.e("CSST", "isCarrierConfigEnableNr: Cannot get config " + this.mPhone.getSubId());
            return false;
        }
        return !ArrayUtils.isEmpty(carrierConfigSubset.getIntArray("carrier_nr_availabilities_int_array"));
    }

    private void handleConfigChanges() {
        Iterator it = this.mNotificationTypeMap.entrySet().iterator();
        while (it.hasNext()) {
            evaluateSendingMessageOrCancelNotification((NotificationType) ((Map.Entry) it.next()).getValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAllowedNetworkTypeChanged() {
        NotificationType notificationType = (NotificationType) this.mNotificationTypeMap.get(1000);
        if (notificationType != null) {
            evaluateSendingMessageOrCancelNotification(notificationType);
        }
    }

    private void handleImsCapabilitiesChanged() {
        NotificationType notificationType = (NotificationType) this.mNotificationTypeMap.get(1001);
        if (notificationType != null) {
            evaluateSendingMessageOrCancelNotification(notificationType);
        }
    }

    private void evaluateSendingMessageOrCancelNotification(NotificationType notificationType) {
        if (evaluateSendingMessage(notificationType)) {
            Message messageObtainMessage = obtainMessage(notificationType.getTypeId(), null);
            Rlog.i("CSST", "starting timer for notifications." + notificationType.getTypeId());
            sendMessageDelayed(messageObtainMessage, (long) getDelay(notificationType));
            return;
        }
        cancelNotification(notificationType);
        Rlog.i("CSST", "canceling notifications: " + notificationType.getTypeId());
    }

    public boolean evaluateSendingMessage(NotificationType notificationType) {
        return notificationType.sendMessage();
    }

    public int getDelay(NotificationType notificationType) {
        return notificationType.getDelay();
    }

    public Notification.Builder getNotificationBuilder(NotificationType notificationType) {
        return notificationType.getNotificationBuilder();
    }

    public NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService("notification");
    }

    public void sendNotification(NotificationType notificationType) {
        Context context = this.mPhone.getContext();
        if (evaluateSendingMessage(notificationType)) {
            if (shouldSilenceEmrgNetNotif(notificationType, context)) {
                Rlog.i("CSST", "sendNotification: silencing NOTIFICATION_EMERGENCY_NETWORK");
                return;
            }
            Notification.Builder notificationBuilder = getNotificationBuilder(notificationType);
            notificationBuilder.setWhen(System.currentTimeMillis()).setShowWhen(true).setAutoCancel(true).setSmallIcon(R.drawable.stat_sys_warning).setColor(context.getResources().getColor(R.color.system_notification_accent_color));
            getNotificationManager(context).notify(notificationType.getNotificationTag(), notificationType.getNotificationId(), notificationBuilder.build());
        }
    }

    private boolean shouldSilenceEmrgNetNotif(NotificationType notificationType, Context context) {
        return notificationType.getTypeId() == 1001 && PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ACTION_NEVER_ASK_AGAIN, false);
    }

    public void cancelNotification(NotificationType notificationType) {
        Context context = this.mPhone.getContext();
        removeMessages(notificationType.getTypeId());
        getNotificationManager(context).cancel(notificationType.getNotificationTag(), notificationType.getNotificationId());
    }

    public void dispose() {
        unregisterAllowedNetworkTypesListener();
    }

    public class PrefNetworkNotification implements NotificationType {
        private int mDelay = -1;
        private boolean mEnabled = false;
        private final int mTypeId;

        PrefNetworkNotification(int i) {
            this.mTypeId = i;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public void setDelay(PersistableBundle persistableBundle) {
            if (persistableBundle == null) {
                Rlog.e("CSST", "bundle is null");
                return;
            }
            this.mDelay = persistableBundle.getInt("network_notification_delay_int");
            Rlog.i("CSST", "reading time to delay notification pref network: " + this.mDelay);
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public int getDelay() {
            return this.mDelay;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public boolean isEnabled() {
            return this.mEnabled;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public void setEnabled(PersistableBundle persistableBundle) {
            if (persistableBundle == null) {
                Rlog.e("CSST", "bundle is null");
                return;
            }
            this.mEnabled = !persistableBundle.getBoolean("hide_preferred_network_type_bool");
            Rlog.i("CSST", "reading enabled notification pref network: " + this.mEnabled);
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public int getTypeId() {
            return this.mTypeId;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public int getNotificationId() {
            return CarrierServiceStateTracker.this.mPhone.getSubId();
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public String getNotificationTag() {
            return CarrierServiceStateTracker.PREF_NETWORK_NOTIFICATION_TAG;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public boolean sendMessage() {
            Rlog.i("CSST", "PrefNetworkNotification: sendMessage() w/values: ," + this.mEnabled + "," + CarrierServiceStateTracker.this.isPhoneStillRegistered() + "," + this.mDelay + "," + CarrierServiceStateTracker.this.isGlobalMode() + "," + CarrierServiceStateTracker.this.mSST.isRadioOn());
            return (!this.mEnabled || this.mDelay == -1 || CarrierServiceStateTracker.this.isPhoneStillRegistered() || CarrierServiceStateTracker.this.isGlobalMode() || CarrierServiceStateTracker.this.isRadioOffOrAirplaneMode()) ? false : true;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public Notification.Builder getNotificationBuilder() {
            Context context = CarrierServiceStateTracker.this.mPhone.getContext();
            Intent intent = new Intent("android.settings.DATA_ROAMING_SETTINGS");
            intent.putExtra("expandable", true);
            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 1140850688);
            Resources resourcesForSubId = SubscriptionManager.getResourcesForSubId(context, CarrierServiceStateTracker.this.mPhone.getSubId());
            CharSequence text = resourcesForSubId.getText(R.string.PERSOSUBSTATE_RUIM_CORPORATE_PUK_SUCCESS);
            CharSequence text2 = resourcesForSubId.getText(R.string.PERSOSUBSTATE_RUIM_CORPORATE_PUK_IN_PROGRESS);
            return new Notification.Builder(context).setContentTitle(text).setStyle(new Notification.BigTextStyle().bigText(text2)).setContentText(text2).setChannelId(NotificationChannelController.CHANNEL_ID_ALERT).setContentIntent(activity);
        }
    }

    public class EmergencyNetworkNotification implements NotificationType {
        private int mDelay = -1;
        private final int mTypeId;

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public boolean isEnabled() {
            return true;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public void setEnabled(PersistableBundle persistableBundle) {
        }

        EmergencyNetworkNotification(int i) {
            this.mTypeId = i;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public void setDelay(PersistableBundle persistableBundle) {
            if (persistableBundle == null) {
                Rlog.e("CSST", "bundle is null");
                return;
            }
            this.mDelay = persistableBundle.getInt("emergency_notification_delay_int");
            Rlog.i("CSST", "reading time to delay notification emergency: " + this.mDelay);
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public int getDelay() {
            return this.mDelay;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public int getTypeId() {
            return this.mTypeId;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public int getNotificationId() {
            return CarrierServiceStateTracker.this.mPhone.getSubId();
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public String getNotificationTag() {
            return CarrierServiceStateTracker.EMERGENCY_NOTIFICATION_TAG;
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public boolean sendMessage() {
            Rlog.i("CSST", "EmergencyNetworkNotification: sendMessage() w/values: ," + this.mDelay + "," + CarrierServiceStateTracker.this.isPhoneRegisteredForWifiCalling() + "," + CarrierServiceStateTracker.this.mSST.isRadioOn());
            return this.mDelay != -1 && CarrierServiceStateTracker.this.isPhoneRegisteredForWifiCalling();
        }

        @Override // com.android.internal.telephony.CarrierServiceStateTracker.NotificationType
        public Notification.Builder getNotificationBuilder() {
            Context context = CarrierServiceStateTracker.this.mPhone.getContext();
            Resources resourcesForSubId = SubscriptionManager.getResourcesForSubId(context, CarrierServiceStateTracker.this.mPhone.getSubId());
            CharSequence text = resourcesForSubId.getText(R.string.NetworkPreferenceSwitchTitle);
            CharSequence text2 = resourcesForSubId.getText(R.string.NetworkPreferenceSwitchSummary);
            FeatureFlags unused = CarrierServiceStateTracker.this.mFeatureFlags;
            return new Notification.Builder(context).setContentTitle(text).setStyle(new Notification.BigTextStyle().bigText(text2)).setContentText(text2).setOngoing(true).setActions(createDoNotShowAgainAction(context)).setChannelId(NotificationChannelController.CHANNEL_ID_WFC);
        }

        private Notification.Action createDoNotShowAgainAction(Context context) {
            CharSequence text;
            Intent intent = new Intent(CarrierServiceStateTracker.ACTION_NEVER_ASK_AGAIN);
            intent.addFlags(268435456);
            if (CarrierServiceStateTracker.this.mPhone != null && CarrierServiceStateTracker.this.mPhone.getContext() != null && !CarrierServiceStateTracker.this.mPhone.getContext().getPackageName().isEmpty()) {
                intent.setPackage(CarrierServiceStateTracker.this.mPhone.getContext().getPackageName());
            }
            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 67108864);
            if (context == null) {
                text = "Do Not Ask Again";
            } else {
                text = context.getText(R.string.global_action_screenshot);
            }
            return new Notification.Action.Builder((Icon) null, text, broadcast).build();
        }
    }

    public class NotificationActionReceiver extends BroadcastReceiver {
        public NotificationActionReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CarrierServiceStateTracker.ACTION_NEVER_ASK_AGAIN)) {
                Rlog.i("CSST", "NotificationActionReceiver: ACTION_NEVER_ASK_AGAIN");
                dismissEmergencyCallingNotification();
                SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editorEdit.putBoolean(CarrierServiceStateTracker.ACTION_NEVER_ASK_AGAIN, true);
                editorEdit.apply();
                context.unregisterReceiver(CarrierServiceStateTracker.this.mActionReceiver);
            }
        }

        private void dismissEmergencyCallingNotification() {
            FeatureFlags unused = CarrierServiceStateTracker.this.mFeatureFlags;
            try {
                NotificationType notificationType = (NotificationType) CarrierServiceStateTracker.this.mNotificationTypeMap.get(1001);
                if (notificationType != null) {
                    CarrierServiceStateTracker.this.cancelNotification(notificationType);
                }
            } catch (Exception e) {
                Rlog.e("CSST", "dismissEmergencyCallingNotification", e);
            }
        }
    }
}
