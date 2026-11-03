package com.android.phone;

import android.app.BroadcastOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.phone.settings.MobileNetworkSettings;
import com.android.phone.settings.VoicemailSettingsActivity;
import com.android.services.telephony.MiTelephonyServiceStub;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class NotificationMgr {
    private static final boolean DBG;
    private static final String LOG_TAG = "NotificationMgr";
    private static NotificationMgr sInstance;
    private PhoneGlobals mApp;
    private Context mContext;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private UserManager mUserManager;
    private final long VOICEMAIL_ALLOW_LIST_DURATION_MILLIS = 10000;
    private SparseArray mSelectedUnavailableNotify = new SparseArray();
    private Set mLimitedSimFunctionNotify = new HashSet();
    private ArrayMap mMwiVisible = new ArrayMap();
    private ArrayMap mLastMwiCountSent = new ArrayMap();
    private ArrayMap mLastMwiVoicemailNumber = new ArrayMap();
    private SparseArray mPreviousServiceState = new SparseArray();
    private SparseArray mRoamingDomesticStateChange = new SparseArray();
    private SparseArray mOOSTimestamp = new SparseArray();
    private SparseArray mPendingEventCounter = new SparseArray();
    private SparseArray mSelectedNetworkOperatorName = new SparseArray();
    private final Handler mHandler = new Handler() { // from class: com.android.phone.NotificationMgr.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            int iIntValue = ((Integer) message.obj).intValue();
            TelephonyManager telephonyManagerCreateForSubscriptionId = NotificationMgr.this.mTelephonyManager.createForSubscriptionId(iIntValue);
            if (telephonyManagerCreateForSubscriptionId.getServiceState() != null) {
                NotificationMgr.this.shouldShowNotification(telephonyManagerCreateForSubscriptionId.getServiceState().getState(), iIntValue);
            }
        }
    };
    private final FeatureFlags mFeatureFlags = new FeatureFlagsImpl();

    private int getNotificationId(int i, int i2) {
        return i + (i2 * 50);
    }

    static {
        DBG = SystemProperties.getInt("ro.debuggable", 0) == 1;
    }

    NotificationMgr(PhoneGlobals phoneGlobals) {
        this.mApp = phoneGlobals;
        this.mContext = phoneGlobals;
        this.mUserManager = (UserManager) phoneGlobals.getSystemService("user");
        this.mSubscriptionManager = SubscriptionManager.from(this.mContext);
        this.mTelephonyManager = (TelephonyManager) phoneGlobals.getSystemService("phone");
    }

    static NotificationMgr init(PhoneGlobals phoneGlobals) {
        NotificationMgr notificationMgr;
        synchronized (NotificationMgr.class) {
            try {
                if (sInstance == null) {
                    sInstance = new NotificationMgr(phoneGlobals);
                } else {
                    Log.wtf(LOG_TAG, "init() called multiple times!  sInstance = " + sInstance);
                }
                notificationMgr = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return notificationMgr;
    }

    void refreshMwi(int i) {
        boolean zBooleanValue;
        if (i == -1 && this.mMwiVisible.keySet().size() == 1) {
            Iterator it = this.mMwiVisible.keySet().iterator();
            if (!it.hasNext()) {
                return;
            } else {
                i = ((Integer) it.next()).intValue();
            }
        }
        if (this.mMwiVisible.containsKey(Integer.valueOf(i)) && (zBooleanValue = ((Boolean) this.mMwiVisible.get(Integer.valueOf(i))).booleanValue())) {
            log("refreshMwi: subId=" + i);
            updateMwi(i, zBooleanValue, true);
        }
    }

    void updateMwi(int i, boolean z) {
        updateMwi(i, z, false);
    }

    void updateMwi(int i, boolean z, boolean z2) {
        String str;
        int i2;
        String str2;
        Integer num;
        Intent intent;
        String string;
        PendingIntent pendingIntent;
        boolean z3;
        Phone phone;
        String str3;
        boolean z4 = z2;
        if (!PhoneGlobals.sVoiceCapable) {
            Log.w(LOG_TAG, "Called updateMwi() on non-voice-capable device! Ignoring...");
            return;
        }
        Phone phone2 = PhoneGlobals.getPhone(i);
        boolean zContainsKey = this.mMwiVisible.containsKey(Integer.valueOf(i));
        boolean z5 = zContainsKey && ((Boolean) this.mMwiVisible.get(Integer.valueOf(i))).booleanValue();
        this.mMwiVisible.put(Integer.valueOf(i), Boolean.valueOf(z));
        boolean z6 = (zContainsKey && z5 == z) ? false : true;
        String str4 = LOG_TAG;
        StringBuilder sb = new StringBuilder("updateMwi(): subId:");
        sb.append(i);
        sb.append(" isRefresh:");
        sb.append(z4);
        sb.append(" state:");
        if (zContainsKey) {
            str = z5 ? "Y" : "N";
        } else {
            str = "unset";
        }
        sb.append(str);
        sb.append("->");
        sb.append(z ? "Y" : "N");
        sb.append(" (changed:");
        sb.append(z6 ? "Y" : "N");
        sb.append(")");
        Log.i(str4, sb.toString());
        if (!z) {
            this.mSubscriptionManager.getSubscriptionUserHandle(i);
            for (UserHandle userHandle : getUsersExcludeDying()) {
                if (!hasUserRestriction("no_outgoing_calls", userHandle) && !this.mUserManager.isProfile(userHandle.getIdentifier()) && !maybeSendVoicemailNotificationUsingDefaultDialer(phone2, 0, null, null, false, userHandle, z2)) {
                    Log.i(LOG_TAG, "notifyMwi: cancel userHandle=" + userHandle);
                    cancelAsUser(Integer.toString(i), getNotificationId(3, i), userHandle);
                }
            }
            return;
        }
        if (phone2 == null) {
            Log.w(str4, "Found null phone for: " + i);
            return;
        }
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(i);
        if (activeSubscriptionInfo == null) {
            Log.w(str4, "Found null subscription info for: " + i);
            return;
        }
        if (this.mTelephonyManager.getPhoneCount() > 1) {
            i2 = phone2.getPhoneId() == 0 ? R.drawable.stat_notify_voicemail_sub1 : R.drawable.stat_notify_voicemail_sub2;
        } else {
            i2 = android.R.drawable.stat_notify_voicemail;
        }
        int i3 = i2;
        String string2 = this.mContext.getString(R.string.notification_voicemail_title);
        String voiceMailNumber = phone2.getVoiceMailNumber();
        if (TextUtils.isEmpty(voiceMailNumber) && !phone2.getIccRecordsLoaded()) {
            Log.i(str4, "updateMwi - Null/empty vm number: SIM records not loaded (yet)...");
            return;
        }
        if (com.android.internal.telephony.TelephonyCapabilities.supportsVoiceMessageCount(phone2)) {
            Integer numValueOf = Integer.valueOf(phone2.getVoiceMessageCount());
            str2 = String.format(this.mContext.getString(R.string.notification_voicemail_title_count), numValueOf);
            num = numValueOf;
        } else {
            str2 = string2;
            num = null;
        }
        PhoneAccountHandle phoneAccountHandleMakePstnPhoneAccountHandle = PhoneUtils.makePstnPhoneAccountHandle(phone2);
        boolean zIsEmpty = TextUtils.isEmpty(voiceMailNumber);
        Context context = this.mContext;
        if (zIsEmpty) {
            string = context.getString(R.string.notification_voicemail_no_vm_number);
            intent = new Intent("com.android.phone.CallFeaturesSetting.ADD_VOICEMAIL");
            intent.putExtra("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", i);
            if (MiTelephonyServiceStub.get().putSubscriptionIdExtra(intent, i)) {
                try {
                    intent.setClass(this.mContext, Class.forName("com.android.phone.settings.VoicemailSetting"));
                } catch (Exception e) {
                    log("Exception: " + e);
                }
            } else {
                intent.setClass(this.mContext, VoicemailSettingsActivity.class);
            }
        } else {
            String str5 = String.format(context.getString(R.string.notification_voicemail_text_format), PhoneNumberUtils.formatNumber(voiceMailNumber));
            intent = new Intent("android.intent.action.CALL", Uri.fromParts("voicemail", voiceMailNumber, null));
            intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleMakePstnPhoneAccountHandle);
            string = str5;
        }
        String str6 = string + MiTelephonyServiceStub.get().getSimCardDesp(this.mContext, phone2.getPhoneId());
        PendingIntent activity = PendingIntent.getActivity(this.mContext, i, intent, 67108864);
        Resources resources = this.mContext.getResources();
        PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(i);
        Phone phone3 = phone2;
        Notification.Builder builder = new Notification.Builder(this.mContext);
        String str7 = voiceMailNumber;
        builder.setSmallIcon(i3).setWhen(System.currentTimeMillis()).setColor(activeSubscriptionInfo.getIconTint()).setContentTitle(str2).setContentText(str6).setContentIntent(activity).setColor(resources.getColor(com.android.phone.common.R$color.dialer_theme_color)).setAutoCancel(true).setOngoing(carrierConfigForSubId.getBoolean("voicemail_notification_persistent_bool")).setChannelId("voiceMail").setOnlyAlertOnce(z4);
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            builder.setLargeIcon(MiTelephonyServiceStub.get().getVoiceMailStatusIcon(this.mContext));
        }
        Notification notificationBuild = builder.build();
        notificationBuild.extraNotification.setCustomizedIcon(true);
        for (UserHandle userHandle2 : getUsersExcludeDying()) {
            if (hasUserRestriction("no_outgoing_calls", userHandle2) || this.mUserManager.isProfile(userHandle2.getIdentifier())) {
                pendingIntent = activity;
                z3 = zIsEmpty;
                phone = phone3;
                str3 = str7;
            } else {
                pendingIntent = activity;
                z3 = zIsEmpty;
                phone = phone3;
                str3 = str7;
                if (!maybeSendVoicemailNotificationUsingDefaultDialer(phone, num, str3, pendingIntent, z3, userHandle2, z4)) {
                    Log.i(LOG_TAG, "updateMwi: notify userHandle=" + userHandle2);
                    notifyAsUser(Integer.toString(i), getNotificationId(3, i), notificationBuild, userHandle2);
                }
            }
            z4 = z2;
            phone3 = phone;
            str7 = str3;
            activity = pendingIntent;
            zIsEmpty = z3;
        }
    }

    private List getUsersExcludeDying() {
        long[] serialNumbersOfUsers = this.mUserManager.getSerialNumbersOfUsers(true);
        ArrayList arrayList = new ArrayList(serialNumbersOfUsers.length);
        for (long j : serialNumbersOfUsers) {
            UserHandle userForSerialNumber = this.mUserManager.getUserForSerialNumber(j);
            if (userForSerialNumber != null) {
                arrayList.add(userForSerialNumber);
            }
        }
        return arrayList;
    }

    private boolean hasUserRestriction(String str, UserHandle userHandle) {
        List userRestrictionSources = this.mUserManager.getUserRestrictionSources(str, userHandle);
        return (userRestrictionSources == null || userRestrictionSources.isEmpty()) ? false : true;
    }

    private boolean maybeSendVoicemailNotificationUsingDefaultDialer(Phone phone, Integer num, String str, PendingIntent pendingIntent, boolean z, UserHandle userHandle, boolean z2) {
        String str2;
        if (shouldManageNotificationThroughDefaultDialer(userHandle)) {
            int subId = phone == null ? -1 : phone.getSubId();
            boolean zContainsKey = this.mLastMwiCountSent.containsKey(Integer.valueOf(subId));
            Object obj = zContainsKey ? (Integer) this.mLastMwiCountSent.get(Integer.valueOf(subId)) : null;
            boolean z3 = (zContainsKey && Objects.equals(obj, num)) ? false : true;
            this.mLastMwiCountSent.put(Integer.valueOf(subId), num);
            boolean zEquals = Objects.equals(str, (String) this.mLastMwiVoicemailNumber.get(Integer.valueOf(subId)));
            boolean z4 = !zEquals;
            this.mLastMwiVoicemailNumber.put(Integer.valueOf(subId), str);
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder("maybeSendVoicemailNotificationUsingDefaultDialer: count: ");
            if (!zContainsKey) {
                obj = "undef";
            }
            sb.append(obj);
            sb.append("->");
            sb.append(num);
            sb.append(" (changed=");
            sb.append(z3);
            sb.append("), voicemailNumberChanged=");
            sb.append(z4);
            Log.i(str3, sb.toString());
            Intent showVoicemailIntentForDefaultDialer = getShowVoicemailIntentForDefaultDialer(userHandle);
            if (!z3 && z2 && zEquals) {
                Log.i(str3, "maybeSendVoicemailNotificationUsingDefaultDialer: skip bcast to:" + showVoicemailIntentForDefaultDialer.getPackage() + ", user:" + userHandle);
                return true;
            }
            showVoicemailIntentForDefaultDialer.setFlags(268435456);
            showVoicemailIntentForDefaultDialer.setAction("android.telephony.action.SHOW_VOICEMAIL_NOTIFICATION");
            showVoicemailIntentForDefaultDialer.putExtra("android.telephony.extra.PHONE_ACCOUNT_HANDLE", PhoneUtils.makePstnPhoneAccountHandle(phone));
            showVoicemailIntentForDefaultDialer.putExtra("android.telephony.extra.IS_REFRESH", z2);
            if (num != null) {
                showVoicemailIntentForDefaultDialer.putExtra("android.telephony.extra.NOTIFICATION_COUNT", num);
            }
            if (num == null || num.intValue() > 0) {
                if (!TextUtils.isEmpty(str)) {
                    showVoicemailIntentForDefaultDialer.putExtra("android.telephony.extra.VOICEMAIL_NUMBER", str);
                }
                if (pendingIntent != null) {
                    if (z) {
                        str2 = "android.telephony.extra.LAUNCH_VOICEMAIL_SETTINGS_INTENT";
                    } else {
                        str2 = "android.telephony.extra.CALL_VOICEMAIL_INTENT";
                    }
                    showVoicemailIntentForDefaultDialer.putExtra(str2, pendingIntent);
                }
            }
            BroadcastOptions broadcastOptionsMakeBasic = BroadcastOptions.makeBasic();
            broadcastOptionsMakeBasic.setTemporaryAppWhitelistDuration(10000L);
            Log.i(str3, "maybeSendVoicemailNotificationUsingDefaultDialer: send via Dialer:" + showVoicemailIntentForDefaultDialer.getPackage() + ", user:" + userHandle);
            this.mContext.sendBroadcastAsUser(showVoicemailIntentForDefaultDialer, userHandle, "android.permission.READ_PHONE_STATE", broadcastOptionsMakeBasic.toBundle());
            return true;
        }
        Log.i(LOG_TAG, "maybeSendVoicemailNotificationUsingDefaultDialer: not using dialer ; user:" + userHandle);
        return false;
    }

    private Intent getShowVoicemailIntentForDefaultDialer(UserHandle userHandle) {
        return new Intent("android.telephony.action.SHOW_VOICEMAIL_NOTIFICATION").setPackage(((TelecomManager) this.mContext.getSystemService(TelecomManager.class)).getDefaultDialerPackage(userHandle));
    }

    private boolean shouldManageNotificationThroughDefaultDialer(UserHandle userHandle) {
        Intent showVoicemailIntentForDefaultDialer = getShowVoicemailIntentForDefaultDialer(userHandle);
        return showVoicemailIntentForDefaultDialer != null && this.mContext.createContextAsUser(userHandle, 0).getPackageManager().queryBroadcastReceivers(showVoicemailIntentForDefaultDialer, 0).size() > 0;
    }

    void updateCfi(int i, boolean z) {
        updateCfi(i, z, false);
    }

    void updateCfi(int i, boolean z, boolean z2) {
        String string;
        StringBuilder sb = new StringBuilder("updateCfi: subId= ");
        sb.append(i);
        sb.append(", visible=");
        sb.append(z ? "Y" : "N");
        logi(sb.toString());
        Phone phone = PhoneGlobals.getPhone(i);
        if (phone == null) {
            Log.w(LOG_TAG, "updateCfi: phone is null, returning...");
            return;
        }
        int phoneId = phone.getPhoneId();
        if (z) {
            SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(i);
            if (activeSubscriptionInfo == null) {
                Log.w(LOG_TAG, "Found null subscription info for: " + i);
                return;
            }
            int i2 = R.drawable.stat_sys_phone_call_forward;
            if (this.mTelephonyManager.getPhoneCount() > 1) {
                if (SubscriptionManager.getSlotIndex(i) == 0) {
                    i2 = R.drawable.stat_sys_phone_call_forward_sub1;
                } else {
                    i2 = R.drawable.stat_sys_phone_call_forward_sub2;
                }
                if (activeSubscriptionInfo.getDisplayName() != null) {
                    string = activeSubscriptionInfo.getDisplayName().toString();
                } else {
                    string = this.mContext.getString(R.string.labelCF);
                }
            } else {
                string = this.mContext.getString(R.string.labelCF);
            }
            if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                string = this.mContext.getString(MiTelephonyServiceStub.get().getMiuiResId(R.string.labelCF));
            }
            Notification.Builder onlyAlertOnce = new Notification.Builder(this.mContext).setSmallIcon(i2).setColor(activeSubscriptionInfo.getIconTint()).setContentTitle(string).setContentText(MiTelephonyServiceStub.get().getCallForwardNotiContentText(this.mContext, phoneId)).setShowWhen(false).setOngoing(true).setChannelId("callForwardNew").setOnlyAlertOnce(z2);
            Intent intent = new Intent("android.telecom.action.SHOW_CALL_SETTINGS");
            intent.addFlags(335544320);
            intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.CallFeaturesSetting");
            SubscriptionInfoHelper.addExtrasToIntent(intent, this.mSubscriptionManager.getActiveSubscriptionInfo(i));
            onlyAlertOnce.setContentIntent(PendingIntent.getActivity(this.mContext, i, intent, 67108864));
            notifyAsUser(Integer.toString(i), getNotificationId(4, i), onlyAlertOnce.build(), UserHandle.ALL);
            return;
        }
        for (UserHandle userHandle : getUsersExcludeDying()) {
            if (!this.mUserManager.isManagedProfile(userHandle.getIdentifier())) {
                cancelAsUser(Integer.toString(i), getNotificationId(4, i), userHandle);
            }
        }
    }

    void showDataRoamingNotification(int i, boolean z) {
        Intent intent;
        int i2;
        int i3;
        if (DBG) {
            StringBuilder sb = new StringBuilder("showDataRoamingNotification() roaming ");
            sb.append(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF);
            sb.append(" on subId ");
            sb.append(i);
            log(sb.toString());
        }
        if (MiTelephonyServiceStub.get().isNoNotificationOnDomesticRoaming() && MiTelephonyServiceStub.get().isDomesticRoamingEnable(this.mContext)) {
            return;
        }
        try {
            if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                String str = MobileNetworkSettings.TAG;
                intent = new Intent(this.mContext, (Class<?>) MobileNetworkSettings.class);
            } else {
                intent = new Intent("android.settings.DATA_ROAMING_SETTINGS");
            }
        } catch (Exception e) {
            log("Exception: " + e);
            intent = null;
        }
        intent.putExtra("android.provider.extra.SUB_ID", i);
        PendingIntent activity = PendingIntent.getActivity(this.mContext, i, intent, 67108864);
        Context context = this.mContext;
        if (z) {
            i2 = R.string.roaming_on_notification_title;
        } else {
            i2 = R.string.roaming_notification_title;
        }
        CharSequence text = context.getText(i2);
        Context context2 = this.mContext;
        if (z) {
            i3 = R.string.roaming_enabled_message;
        } else {
            i3 = R.string.roaming_reenable_message;
        }
        CharSequence text2 = context2.getText(i3);
        notifyAsUser(null, 5, new Notification.BigTextStyle(new Notification.Builder(this.mContext).setSmallIcon(android.R.drawable.stat_sys_warning).setContentTitle(text).setColor(this.mContext.getResources().getColor(com.android.phone.common.R$color.dialer_theme_color)).setContentText(text2).setChannelId("mobileDataAlertNew").setContentIntent(activity)).bigText(text2).build(), UserHandle.ALL);
    }

    private void notifyAsUser(String str, int i, Notification notification, UserHandle userHandle) {
        try {
            Context context = this.mContext;
            ((NotificationManager) context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).getSystemService("notification")).notify(str, i, notification);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "unable to notify for user " + userHandle);
            e.printStackTrace();
        }
    }

    private void cancelAsUser(String str, int i, UserHandle userHandle) {
        try {
            Context context = this.mContext;
            ((NotificationManager) context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).getSystemService("notification")).cancel(str, i);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "unable to cancel for user " + userHandle);
            e.printStackTrace();
        }
    }

    void hideDataRoamingNotification() {
        if (DBG) {
            log("hideDataRoamingNotification()...");
        }
        cancelAsUser(null, 5, UserHandle.ALL);
    }

    public void showLimitedSimFunctionWarningNotification(int i, String str) {
        String str2;
        boolean z = DBG;
        if (z) {
            log("showLimitedSimFunctionWarningNotification carrier: " + str + " subId: " + i);
        }
        if (this.mLimitedSimFunctionNotify.contains(Integer.valueOf(i))) {
            if (z) {
                log("showLimitedSimFunctionWarningNotification, not display again if already displayed");
                return;
            }
            return;
        }
        PendingIntent activity = PendingIntent.getActivity(this.mContext, 0, new Intent("android.settings.MOBILE_NETWORK_LIST"), 67108864);
        String line1Number = null;
        for (SubscriptionInfo subscriptionInfo : ((SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service")).getActiveSubscriptionInfoList(false)) {
            if (subscriptionInfo.getSubscriptionId() != i) {
                line1Number = this.mTelephonyManager.getLine1Number(subscriptionInfo.getSubscriptionId());
            }
        }
        boolean zIsEmpty = TextUtils.isEmpty(line1Number);
        Context context = this.mContext;
        if (zIsEmpty) {
            str2 = String.format(context.getText(R.string.limited_sim_function_notification_message).toString(), str);
        } else {
            str2 = String.format(context.getText(R.string.limited_sim_function_with_phone_num_notification_message).toString(), str, line1Number);
        }
        notifyAsUser(Integer.toString(i), 7, new Notification.BigTextStyle(new Notification.Builder(this.mContext).setSmallIcon(R.drawable.ic_sim_card).setContentTitle(this.mContext.getText(R.string.limited_sim_function_notification_title)).setContentText(str2).setOnlyAlertOnce(true).setOngoing(true).setChannelId("simHighPriority").setContentIntent(activity)).bigText(str2).build(), UserHandle.ALL);
        this.mLimitedSimFunctionNotify.add(Integer.valueOf(i));
    }

    public void dismissLimitedSimFunctionWarningNotification(int i) {
        if (DBG) {
            log("dismissLimitedSimFunctionWarningNotification subId: " + i);
        }
        Set set = this.mLimitedSimFunctionNotify;
        if (i == -1) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                cancelAsUser(Integer.toString(((Integer) it.next()).intValue()), 7, UserHandle.ALL);
            }
            this.mLimitedSimFunctionNotify.clear();
            return;
        }
        if (set.contains(Integer.valueOf(i))) {
            cancelAsUser(Integer.toString(i), 7, UserHandle.ALL);
            this.mLimitedSimFunctionNotify.remove(Integer.valueOf(i));
        }
    }

    public void dismissLimitedSimFunctionWarningNotificationForInactiveSubs() {
        if (DBG) {
            log("dismissLimitedSimFunctionWarningNotificationForInactiveSubs");
        }
        this.mLimitedSimFunctionNotify.removeIf(new Predicate() { // from class: com.android.phone.NotificationMgr$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$dismissLimitedSimFunctionWarningNotificationForInactiveSubs$0((Integer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$dismissLimitedSimFunctionWarningNotificationForInactiveSubs$0(Integer num) {
        if (this.mSubscriptionManager.isActiveSubId(num.intValue())) {
            return false;
        }
        cancelAsUser(Integer.toString(num.intValue()), 7, UserHandle.ALL);
        return true;
    }

    private void showNetworkSelection(String str, int i) {
        if (DBG) {
            log("showNetworkSelection(" + str + ")...");
        }
        if (!TextUtils.isEmpty(str)) {
            str = String.format(" (%s)", str);
        }
        Notification.Builder channelId = new Notification.Builder(this.mContext).setSmallIcon(android.R.drawable.stat_sys_warning).setContentTitle(this.mContext.getString(R.string.notification_network_selection_title)).setContentText(this.mContext.getString(R.string.notification_network_selection_text, str)).setShowWhen(false).setOngoing(true).setChannelId("alert");
        Phone phone = PhoneGlobals.getPhone(i);
        if (phone == null) {
            Log.w(LOG_TAG, "updateCfi: phone is null, returning...");
            return;
        }
        int phoneId = phone.getPhoneId();
        MiTelephonyServiceStub.get().configNetworkSelectionNotification(this.mContext, channelId, phoneId);
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(335544320);
        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.NetworkSetting"));
        TelephonyBaseUtilsStub.putSlotIdPhoneIdAndSubIdExtra(intent, phoneId, phoneId, i);
        intent.putExtra("android.provider.extra.SUB_ID", i);
        channelId.setContentIntent(PendingIntent.getActivity(this.mContext, i, intent, 67108864));
        notifyAsUser(Integer.toString(i), 6, channelId.build(), UserHandle.ALL);
        this.mSelectedUnavailableNotify.put(i, Boolean.TRUE);
    }

    private void cancelNetworkSelection(int i) {
        if (DBG) {
            log("cancelNetworkSelection()...");
        }
        cancelAsUser(Integer.toString(i), 6, UserHandle.ALL);
    }

    void updateNetworkSelection(int i, int i2) {
        boolean isManualSelection;
        int phoneId = SubscriptionManager.getPhoneId(i2);
        Phone phone = SubscriptionManager.isValidPhoneId(phoneId) ? PhoneFactory.getPhone(phoneId) : PhoneFactory.getDefaultPhone();
        if (com.android.internal.telephony.TelephonyCapabilities.supportsNetworkSelection(phone)) {
            if (SubscriptionManager.isValidSubscriptionId(i2) && this.mSubscriptionManager.isActiveSubId(i2)) {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
                String string = defaultSharedPreferences.getString("network_selection_name_key" + i2, "");
                if (TextUtils.isEmpty(string)) {
                    string = defaultSharedPreferences.getString("network_selection_key" + i2, "");
                }
                if (!this.mContext.getResources().getBoolean(17892131)) {
                    isManualSelection = !TextUtils.isEmpty(string);
                } else {
                    isManualSelection = phone.getServiceStateTracker().mSS.getIsManualSelection();
                }
                if (DBG) {
                    StringBuilder sb = new StringBuilder("updateNetworkSelection()...state = ");
                    sb.append(i);
                    sb.append(" new network ");
                    sb.append(isManualSelection ? string : "");
                    log(sb.toString());
                }
                if (isManualSelection && isSubscriptionVisibleToUser(this.mSubscriptionManager.getActiveSubscriptionInfo(i2))) {
                    this.mSelectedNetworkOperatorName.put(i2, string);
                    shouldShowNotification(i, i2);
                    return;
                } else {
                    dismissNetworkSelectionNotification(i2);
                    clearUpNetworkSelectionNotificationParam(i2);
                    return;
                }
            }
            if (DBG) {
                log("updateNetworkSelection()... state = " + i + " not updating network due to invalid subId " + i2);
            }
            dismissNetworkSelectionNotificationForInactiveSubId();
        }
    }

    private boolean isSubscriptionVisibleToUser(SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo != null) {
            return !subscriptionInfo.isOpportunistic() || subscriptionInfo.getGroupUuid() == null;
        }
        return false;
    }

    private void dismissNetworkSelectionNotification(int i) {
        if (((Boolean) this.mSelectedUnavailableNotify.get(i, Boolean.FALSE)).booleanValue()) {
            cancelNetworkSelection(i);
            this.mSelectedUnavailableNotify.remove(i);
        }
    }

    public void dismissNetworkSelectionNotificationForInactiveSubId() {
        for (int i = 0; i < this.mSelectedUnavailableNotify.size(); i++) {
            int iKeyAt = this.mSelectedUnavailableNotify.keyAt(i);
            if (!this.mSubscriptionManager.isActiveSubId(iKeyAt)) {
                dismissNetworkSelectionNotification(iKeyAt);
                clearUpNetworkSelectionNotificationParam(iKeyAt);
            }
        }
    }

    private void log(String str) {
        Log.d(LOG_TAG, str);
    }

    private void logi(String str) {
        Log.i(LOG_TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shouldShowNotification(int i, int i2) {
        if (!shouldDisplayNetworkSelectOptions(i2)) {
            logi("Carrier configs refuse to show network selection not available notification");
            return;
        }
        if (this.mSubscriptionManager.getActiveSubscriptionInfo(i2) == null) {
            log("Found null subscription info for: " + i2);
            return;
        }
        if (i == 1 && ((Boolean) this.mRoamingDomesticStateChange.get(i2, Boolean.FALSE)).booleanValue()) {
            log("ignore Roaming Domestic for play");
            return;
        }
        if (i == 1) {
            if (((Integer) this.mPreviousServiceState.get(i2, -1)).intValue() != 1) {
                this.mOOSTimestamp.put(i2, Long.valueOf(getTimeStamp()));
            }
            if (getTimeStamp() - ((Long) this.mOOSTimestamp.get(i2, 0L)).longValue() >= 10000 || ((Integer) this.mPendingEventCounter.get(i2, 0)).intValue() > 10) {
                showNetworkSelection((String) this.mSelectedNetworkOperatorName.get(i2), i2);
                clearUpNetworkSelectionNotificationParam(i2);
            } else {
                startPendingNetworkSelectionNotification(i2);
            }
        } else {
            dismissNetworkSelectionNotification(i2);
        }
        if (i != 3) {
            updateDomesticRoaming(i, i2);
        }
        this.mPreviousServiceState.put(i2, Integer.valueOf(i));
        if (DBG) {
            log("shouldShowNotification()... subId = " + i2 + " serviceState = " + i + " mOOSTimestamp = " + this.mOOSTimestamp + " mPendingEventCounter = " + this.mPendingEventCounter + " mRoamingDomesticStateChange=" + this.mRoamingDomesticStateChange);
        }
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0041  */
    private void updateDomesticRoaming(int i, int i2) {
        boolean z;
        if (MiTelephonyServiceStub.get().isSupportDomesticRoaming() && SubscriptionManager.isValidSubscriptionId(i2)) {
            Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i2));
            String operatorNumeric = phone.getOperatorNumeric();
            ServiceState serviceState = phone.getServiceStateTracker().mSS;
            if ("26006".equals(operatorNumeric) && serviceState != null && serviceState.getState() == 0) {
                z = true;
                if (serviceState.getDataRoamingType() != 2 && serviceState.getDataRoamingType() != 1) {
                    z = false;
                }
            } else {
                z = false;
            }
            this.mRoamingDomesticStateChange.put(i2, Boolean.valueOf(z));
            log("mRoamingDomesticStateChange = " + this.mRoamingDomesticStateChange + ",newSS =" + serviceState);
        }
    }

    private boolean shouldDisplayNetworkSelectOptions(int i) {
        TelephonyManager telephonyManagerCreateForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(i);
        PersistableBundle configForSubId = ((CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class)).getConfigForSubId(i);
        if (i == -1 || configForSubId == null || !configForSubId.getBoolean("operator_selection_expand_bool") || configForSubId.getBoolean("hide_carrier_network_settings_bool") || (configForSubId.getBoolean("csp_enabled_bool") && !telephonyManagerCreateForSubscriptionId.isManualNetworkSelectionAllowed())) {
            return false;
        }
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(i);
        if (activeSubscriptionInfo != null && activeSubscriptionInfo.isPrivateNetwork()) {
            return false;
        }
        if (isWorldMode(configForSubId)) {
            int networkTypeFromRaf = RadioAccessFamily.getNetworkTypeFromRaf((int) telephonyManagerCreateForSubscriptionId.getAllowedNetworkTypesForReason(0));
            if (networkTypeFromRaf == 8 || shouldSpeciallyUpdateGsmCdma(telephonyManagerCreateForSubscriptionId, configForSubId)) {
                return false;
            }
            if (networkTypeFromRaf == 9) {
                return true;
            }
        }
        return isGsmBasicOptions(telephonyManagerCreateForSubscriptionId, configForSubId);
    }

    private static boolean isWorldMode(PersistableBundle persistableBundle) {
        return persistableBundle.getBoolean("world_mode_enabled_bool");
    }

    private static boolean shouldSpeciallyUpdateGsmCdma(TelephonyManager telephonyManager, PersistableBundle persistableBundle) {
        if (!isWorldMode(persistableBundle)) {
            return false;
        }
        int networkTypeFromRaf = RadioAccessFamily.getNetworkTypeFromRaf((int) telephonyManager.getAllowedNetworkTypesForReason(0));
        return (networkTypeFromRaf == 17 || networkTypeFromRaf == 20 || networkTypeFromRaf == 15 || networkTypeFromRaf == 19 || networkTypeFromRaf == 22 || networkTypeFromRaf == 10) && !isTdscdmaSupported(telephonyManager, persistableBundle);
    }

    private static boolean isTdscdmaSupported(TelephonyManager telephonyManager, PersistableBundle persistableBundle) {
        if (persistableBundle.getBoolean("support_tdscdma_bool")) {
            return true;
        }
        String[] stringArray = persistableBundle.getStringArray("support_tdscdma_roaming_networks_string_array");
        if (stringArray == null) {
            return false;
        }
        ServiceState serviceState = telephonyManager.getServiceState();
        String operatorNumeric = serviceState != null ? serviceState.getOperatorNumeric() : null;
        if (operatorNumeric == null) {
            return false;
        }
        for (String str : stringArray) {
            if (operatorNumeric.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isGsmBasicOptions(TelephonyManager telephonyManager, PersistableBundle persistableBundle) {
        return (!persistableBundle.getBoolean("hide_carrier_network_settings_bool") && persistableBundle.getBoolean("world_phone_bool")) || telephonyManager.getPhoneType() == 1;
    }

    private void startPendingNetworkSelectionNotification(int i) {
        if (this.mHandler.hasMessages(1, Integer.valueOf(i))) {
            return;
        }
        if (DBG) {
            log("startPendingNetworkSelectionNotification: subId = " + i);
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1, Integer.valueOf(i)), 10000L);
        SparseArray sparseArray = this.mPendingEventCounter;
        sparseArray.put(i, Integer.valueOf(((Integer) sparseArray.get(i, 0)).intValue() + 1));
    }

    private void clearUpNetworkSelectionNotificationParam(int i) {
        if (this.mHandler.hasMessages(1, Integer.valueOf(i))) {
            this.mHandler.removeMessages(1, Integer.valueOf(i));
        }
        this.mPreviousServiceState.remove(i);
        this.mOOSTimestamp.remove(i);
        this.mPendingEventCounter.remove(i);
        this.mSelectedNetworkOperatorName.remove(i);
    }

    public long getTimeStamp() {
        return SystemClock.elapsedRealtime();
    }
}
