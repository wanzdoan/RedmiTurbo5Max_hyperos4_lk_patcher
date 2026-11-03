package com.android.phone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.telephony.Phone;
import java.text.SimpleDateFormat;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyCallbackModeService extends Service {
    public static final boolean IS_OP12 = "OP12".equals(SystemProperties.get("persist.vendor.operator.optr", "OM"));
    private NotificationManager mNotificationManager = null;
    private CountDownTimer mTimer = null;
    private long mTimeLeft = 0;
    private Phone mPhone = null;
    private boolean mInEmergencyCall = false;
    private Handler mHandler = new Handler() { // from class: com.android.phone.EmergencyCallbackModeService.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            EmergencyCallbackModeService.this.resetEcmTimer((AsyncResult) message.obj);
        }
    };
    private BroadcastReceiver mEcmReceiver = new BroadcastReceiver() { // from class: com.android.phone.EmergencyCallbackModeService.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED")) {
                if (intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false)) {
                    return;
                }
                EmergencyCallbackModeService.this.stopSelf();
            } else if (intent.getAction().equals("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS")) {
                context.startActivityAsUser(new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS").setFlags(268435456), UserHandle.CURRENT);
            }
        }
    };
    private final IBinder mBinder = new LocalBinder();

    @Override // android.app.Service
    public void onCreate() {
        Phone phoneInEcm = PhoneGlobals.getInstance().getPhoneInEcm();
        if (phoneInEcm == null || phoneInEcm.getImsPhone() == null) {
            Log.e("EmergencyCallbackModeService", "Error! Emergency Callback Mode not supported for " + phoneInEcm);
            stopSelf();
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        intentFilter.addAction("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS");
        registerReceiver(this.mEcmReceiver, intentFilter);
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        this.mPhone = phoneInEcm;
        phoneInEcm.registerForEcmTimerReset(this.mHandler, 1, (Object) null);
        startTimerNotification();
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (this.mPhone != null) {
            unregisterReceiver(this.mEcmReceiver);
            this.mPhone.unregisterForEcmTimerReset(this.mHandler);
            this.mNotificationManager.cancelAsUser(null, R.string.phone_in_ecm_notification_title, UserHandle.ALL);
            this.mTimer.cancel();
        }
        if (this.mNotificationManager == null) {
            this.mNotificationManager = (NotificationManager) getSystemService("notification");
        }
        this.mNotificationManager.cancel(R.string.phone_in_ecm_notification_title);
    }

    private void startTimerNotification() {
        EmergencyCallbackModeService emergencyCallbackModeService;
        long jLongValue = ((Long) TelephonyProperties.ecm_exit_timer().orElse(300000L)).longValue();
        showNotification(jLongValue);
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            emergencyCallbackModeService = this;
        } else {
            emergencyCallbackModeService = this;
            emergencyCallbackModeService.mTimer = new CountDownTimer(jLongValue, 1000L) { // from class: com.android.phone.EmergencyCallbackModeService.3
                @Override // android.os.CountDownTimer
                public void onFinish() {
                }

                @Override // android.os.CountDownTimer
                public void onTick(long j) {
                    EmergencyCallbackModeService.this.mTimeLeft = j;
                }
            };
        }
        emergencyCallbackModeService.mTimer.start();
    }

    private void showNotification(long j) {
        int i;
        String string;
        int i2;
        Phone imsPhone = this.mPhone.getImsPhone();
        if (!this.mPhone.isInEcm() && (imsPhone == null || !imsPhone.isInEcm())) {
            Log.i("EmergencyCallbackModeService", "Asked to show notification but not in ECM mode");
            CountDownTimer countDownTimer = this.mTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                return;
            }
            return;
        }
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setOngoing(true);
        builder.setPriority(1);
        builder.setSmallIcon(R.drawable.ic_emergency_callback_mode);
        builder.setTicker(getText(R.string.phone_entered_ecm_text));
        builder.setContentTitle(getText(R.string.phone_in_ecm_notification_title));
        builder.setColor(getResources().getColor(com.android.phone.common.R$color.dialer_theme_color));
        Intent intent = new Intent(this, (Class<?>) EmergencyCallbackModeExitDialog.class);
        intent.setAction("com.android.phone.action.ACTION_SHOW_ECM_EXIT_DIALOG");
        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 67108864));
        if (this.mInEmergencyCall) {
            if ((imsPhone != null && imsPhone.isInImsEcm()) || IS_OP12) {
                i2 = R.string.phone_in_ecm_call_notification_text_without_data_restriction_hint;
            } else {
                i2 = R.string.phone_in_ecm_call_notification_text;
            }
            string = getText(i2).toString();
        } else {
            long jCurrentTimeMillis = j + System.currentTimeMillis();
            builder.setShowWhen(true);
            builder.setChronometerCountDown(true);
            builder.setUsesChronometer(true);
            builder.setWhen(jCurrentTimeMillis);
            String str = SimpleDateFormat.getTimeInstance(3).format(Long.valueOf(jCurrentTimeMillis));
            Resources resources = getResources();
            if ((imsPhone != null && imsPhone.isInImsEcm()) || IS_OP12) {
                i = R.string.phone_in_ecm_notification_complete_time_without_data_restriction_hint;
            } else {
                i = R.string.phone_in_ecm_notification_complete_time;
            }
            string = resources.getString(i, str);
        }
        builder.setContentText(string);
        builder.setChannelId("alert");
        this.mNotificationManager.notifyAsUser(null, R.string.phone_in_ecm_notification_title, builder.build(), UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetEcmTimer(AsyncResult asyncResult) {
        if (((Boolean) asyncResult.result).booleanValue()) {
            this.mInEmergencyCall = true;
            this.mTimer.cancel();
            showNotification(0L);
        } else {
            this.mInEmergencyCall = false;
            startTimerNotification();
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        EmergencyCallbackModeService getService() {
            return EmergencyCallbackModeService.this;
        }
    }

    public long getEmergencyCallbackModeTimeout() {
        return this.mTimeLeft;
    }

    public boolean getEmergencyCallbackModeCallState() {
        return this.mInEmergencyCall;
    }
}
