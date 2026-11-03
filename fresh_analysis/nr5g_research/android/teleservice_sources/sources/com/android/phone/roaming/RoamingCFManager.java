package com.android.phone.roaming;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.roaming.utils.RoamingUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.HashMap;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class RoamingCFManager {
    private static boolean DEBUG = false;
    private static volatile RoamingCFManager sInstance;
    private final HashMap mLastNetworkNumeric = new HashMap();
    private Context mContext = PhoneGlobals.getInstance();
    private final Handler mHandler = new RoamingCFHandler();
    private NotificationManager mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");

    private int getNotificationId(int i, int i2) {
        return i + (i2 * 50);
    }

    public static RoamingCFManager getInstance() {
        if (sInstance == null) {
            synchronized (RoamingCFManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new RoamingCFManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private RoamingCFManager() {
    }

    class RoamingCFHandler extends Handler {
        public RoamingCFHandler() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d("RoamingCFManager", "handleMessage: " + message.what);
            if (message.what != 0) {
                return;
            }
            RoamingCFManager.this.updateCFUNotification(message.arg1, (ServiceState) message.obj);
        }
    }

    public void processServiceState(int i, ServiceState serviceState) {
        Phone phone = PhoneFactory.getPhone(i);
        if (!SubscriptionManager.isValidPhoneId(i) || phone == null || serviceState == null || phone.getServiceStateTracker() == null || phone.getServiceStateTracker().mSS == null || phone.getServiceStateTracker().mSS.getVoiceRegState() != 0) {
            return;
        }
        String operatorMcc = RoamingUtils.getOperatorMcc(phone.getServiceStateTracker().mSS.getOperatorNumeric());
        if (TextUtils.isEmpty(operatorMcc) || TextUtils.equals((CharSequence) this.mLastNetworkNumeric.get(Integer.valueOf(i)), operatorMcc)) {
            return;
        }
        Log.d("RoamingCFManager", "Sim " + i + " service state changed from " + ((String) this.mLastNetworkNumeric.get(Integer.valueOf(i))) + " to " + operatorMcc);
        this.mLastNetworkNumeric.put(Integer.valueOf(i), operatorMcc);
        if (this.mHandler.hasMessages(0)) {
            this.mHandler.removeMessages(0);
        }
        Message messageObtainMessage = this.mHandler.obtainMessage(0);
        messageObtainMessage.arg1 = i;
        messageObtainMessage.obj = serviceState;
        this.mHandler.sendMessageDelayed(messageObtainMessage, 120000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCFUNotification(int i, ServiceState serviceState) {
        Log.d("RoamingCFManager", "Updating CFU notification for sim: " + i);
        boolean voiceRoaming = serviceState.getVoiceRoaming();
        boolean cFUIndicator = RoamingUtils.getCFUIndicator(i);
        boolean z = serviceState.getVoiceRegState() == 0;
        Log.d("RoamingCFManager", "Updating CFU notification for sim: " + i + " ,isPhoneRoaming - " + voiceRoaming + " , isPhoneCFUEnabled - " + cFUIndicator + " ,isPhoneInService - " + z);
        if (MiuiPhoneUtils.getInsertedSimCount() == 2) {
            int i2 = i == 0 ? 1 : 0;
            boolean voiceRoaming2 = RoamingUtils.getVoiceRoaming(i2);
            boolean cFUIndicator2 = RoamingUtils.getCFUIndicator(i2);
            boolean zIsVoiceInService = RoamingUtils.isVoiceInService(i2);
            Log.d("RoamingCFManager", "Updating CFU notification for sim: " + i + " ,isSubPhoneRoaming - " + voiceRoaming2 + " , isSubPhoneCFUEnabled - " + cFUIndicator2 + " ,isSubPhoneInService - " + zIsVoiceInService);
            if (voiceRoaming && !voiceRoaming2) {
                if (!cFUIndicator && !cFUIndicator2 && zIsVoiceInService) {
                    sendCFUNotification(i, true, getNotificationContentTitle(i, i2, true));
                    return;
                } else {
                    if (!cFUIndicator && cFUIndicator2 && RoamingUtils.getRoamingCFUStatusBySlot(i2)) {
                        sendCFUNotification(i2, true, getNotificationContentTitle(i2, false));
                        return;
                    }
                    return;
                }
            }
            if (voiceRoaming || !voiceRoaming2) {
                if (voiceRoaming || voiceRoaming2) {
                    return;
                }
                if (!cFUIndicator) {
                    i = i2;
                }
                if ((cFUIndicator || cFUIndicator2) && RoamingUtils.getRoamingCFUStatusBySlot(i)) {
                    sendCFUNotification(i, true, getNotificationContentTitle(i, false));
                    return;
                }
                return;
            }
            if (!cFUIndicator && !cFUIndicator2 && z) {
                sendCFUNotification(i2, true, getNotificationContentTitle(i2, i, true));
                return;
            } else {
                if (cFUIndicator && !cFUIndicator2 && RoamingUtils.getRoamingCFUStatusBySlot(i)) {
                    sendCFUNotification(i, true, getNotificationContentTitle(i, false));
                    return;
                }
                return;
            }
        }
        if (!voiceRoaming && cFUIndicator && RoamingUtils.getRoamingCFUStatusBySlot(i)) {
            sendCFUNotification(i, true, getNotificationContentTitle(i, false));
        }
    }

    public void updateRoamingCFUStatus(Phone phone, int i, int i2, String str, Throwable th) {
        if (RoamingUtils.isRoamingOptimizationSupported(this.mContext)) {
            int phoneId = phone.getPhoneId();
            int i3 = phoneId == 0 ? 1 : 0;
            int exceptionCode = RoamingUtils.getExceptionCode(th);
            String phoneNumber = RoamingUtils.getPhoneNumber(phoneId);
            if (i == 0) {
                if ((i2 == 1 || i2 == 3) && phone.getServiceState() != null && phone.getServiceState().getVoiceRoaming() && !RoamingUtils.getVoiceRoaming(i3)) {
                    if (th == null) {
                        RoamingUtils.saveRoamingCFUStatus(phone.getSubId(), true, phoneNumber);
                        sendCFUNotification(phoneId, false, null);
                    }
                    Log.d("RoamingCFManager", "Enable CFU for sim: " + phone.getPhoneId() + ", error code: " + exceptionCode);
                    RoamingUtils.trackEvent(phoneId, "enable_roaming_cf", exceptionCode);
                    return;
                }
                if (i2 == 0 && RoamingUtils.getRoamingCFUStatusBySub(phone.getSubId())) {
                    if (th == null) {
                        RoamingUtils.saveRoamingCFUStatus(phone.getSubId(), false, "");
                        sendCFUNotification(phoneId, false, null);
                    }
                    Log.d("RoamingCFManager", "Disable CFU for sim: " + phone.getPhoneId() + ", error code: " + exceptionCode);
                    RoamingUtils.trackEvent(phoneId, "disable_roaming_cf", exceptionCode);
                }
            }
        }
    }

    public void onIccStatusChanged(int i, String str) {
        if ("LOADED".equals(str)) {
            this.mLastNetworkNumeric.remove(Integer.valueOf(i));
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                processServiceState(i, phone.getServiceState());
                return;
            }
            return;
        }
        if ("ABSENT".equals(str)) {
            sendCFUNotification(i, false, null);
        }
    }

    private void sendCFUNotification(int i, boolean z, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (z) {
            cancelCFUNotification(i == 0 ? 1 : 0);
            Notification.Builder channelId = new Notification.Builder(this.mContext).setSmallIcon(R.drawable.stat_sys_phone_call_forward).setContentTitle(this.mContext.getString(R.string.cfu_notification_title)).setContentText(str).setShowWhen(false).setOngoing(false).setAutoCancel(true).setChannelId("callForwardNew");
            Intent intent = new Intent("android.telecom.action.SHOW_CALL_SETTINGS");
            intent.addFlags(335544320);
            intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.CallForwardActivity");
            SubscriptionManager.putSlotIdExtra(intent, i);
            channelId.setContentIntent(PendingIntent.getActivity(this.mContext, i, intent, 67108864));
            this.mNotificationManager.notifyAsUser("roamingCallForward", getNotificationId(6, i + 1), channelId.build(), UserHandle.CURRENT);
            Log.d("RoamingCFManager", "Show call forward notification for sim " + i);
            return;
        }
        cancelCFUNotification(i);
    }

    private void cancelCFUNotification(int i) {
        this.mNotificationManager.cancelAsUser("roamingCallForward", getNotificationId(6, i + 1), UserHandle.CURRENT);
        Log.d("RoamingCFManager", "Hide call forward notification for sim " + i);
    }

    private String getNotificationContentTitle(int i, boolean z) {
        return getNotificationContentTitle(-1, i, z);
    }

    private String getNotificationContentTitle(int i, int i2, boolean z) {
        String str;
        String str2;
        if (z) {
            str = "record_enable_roaming_cf_notification";
        } else {
            str = "record_disable_roaming_cf_notification";
        }
        if (RoamingUtils.hasRoamingCFNotifyRecord(this.mContext, str) && !DEBUG) {
            return null;
        }
        if (z) {
            str2 = "notification_enable_roaming_cf";
        } else {
            str2 = "notification_disable_roaming_cf";
        }
        RoamingUtils.trackEvent(i, str2, -1);
        RoamingUtils.updateRoamingCFNotifyRecord(this.mContext, str);
        Context context = this.mContext;
        if (z) {
            return context.getString(R.string.enable_cfu_notification_summary, Integer.valueOf(i + 1), Integer.valueOf(i2 + 1));
        }
        return context.getString(R.string.disable_cfu_notification_summary, Integer.valueOf(i2 + 1));
    }

    public void setDebug(boolean z) {
        Log.i("RoamingCFManager", "Enable debug.");
        DEBUG = z;
    }
}
