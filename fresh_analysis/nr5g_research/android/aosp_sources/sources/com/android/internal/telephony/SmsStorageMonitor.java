package com.android.internal.telephony;

import android.R;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.SubscriptionManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class SmsStorageMonitor extends Handler {
    protected static final int WAKE_LOCK_TIMEOUT = 5000;
    protected final CommandsInterface mCi;
    protected final Context mContext;
    private final FeatureFlags mFeatureFlags;
    protected Phone mPhone;
    private final BroadcastReceiver mResultReceiver;
    protected PowerManager.WakeLock mWakeLock;
    private int mMaxRetryCount = 1;
    private int mRetryDelay = 5000;
    private int mRetryCount = 0;
    private boolean mIsWaitingResponse = false;
    private boolean mNeedNewReporting = false;
    private boolean mIsMemoryStatusReportingFailed = false;
    private boolean mPendingIccFullNotify = false;
    boolean mStorageAvailable = true;
    boolean mInitialStorageAvailableStatus = true;
    private boolean mMemoryStatusOverrideFlag = false;

    public SmsStorageMonitor(Phone phone, FeatureFlags featureFlags) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.SmsStorageMonitor.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.DEVICE_STORAGE_FULL".equals(action) || "android.intent.action.DEVICE_STORAGE_NOT_FULL".equals(action)) {
                    SmsStorageMonitor.this.mStorageAvailable = !"android.intent.action.DEVICE_STORAGE_FULL".equals(action);
                    SmsStorageMonitor smsStorageMonitor = SmsStorageMonitor.this;
                    smsStorageMonitor.sendMessage(smsStorageMonitor.obtainMessage(2));
                    return;
                }
                if ("android.intent.action.BOOT_COMPLETED".equals(action) && SmsStorageMonitor.this.mPendingIccFullNotify) {
                    SmsStorageMonitor.this.handleIccFull();
                }
            }
        };
        this.mResultReceiver = broadcastReceiver;
        this.mPhone = phone;
        this.mFeatureFlags = featureFlags;
        Context context = phone.getContext();
        this.mContext = context;
        CommandsInterface commandsInterface = phone.mCi;
        this.mCi = commandsInterface;
        createWakelock();
        commandsInterface.setOnIccSmsFull(this, 1, null);
        commandsInterface.registerForOn(this, 5, null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_FULL");
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_NOT_FULL");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void sendMemoryStatusOverride(boolean z) {
        if (!this.mMemoryStatusOverrideFlag) {
            this.mInitialStorageAvailableStatus = this.mStorageAvailable;
            this.mMemoryStatusOverrideFlag = true;
        }
        this.mStorageAvailable = z;
        if (z) {
            sendMessage(obtainMessage(2));
        }
    }

    public void clearMemoryStatusOverride() {
        this.mStorageAvailable = this.mInitialStorageAvailableStatus;
        this.mMemoryStatusOverrideFlag = false;
    }

    public void setMaxRetries(int i) {
        this.mMaxRetryCount = i;
    }

    public void setRetryDelayInMillis(int i) {
        this.mRetryDelay = i;
    }

    public void dispose() {
        this.mCi.unSetOnIccSmsFull(this);
        this.mCi.unregisterForOn(this);
        this.mContext.unregisterReceiver(this.mResultReceiver);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        boolean z = this.mStorageAvailable;
        int i = message.what;
        if (i == 1) {
            handleIccFull();
            return;
        }
        if (i == 2) {
            if (this.mIsWaitingResponse) {
                Rlog.v("SmsStorageMonitor1", "EVENT_REPORT_MEMORY_STATUS - deferred");
                this.mNeedNewReporting = true;
                return;
            }
            Rlog.v("SmsStorageMonitor1", "EVENT_REPORT_MEMORY_STATUS - report sms memory status".concat(z ? "(not full)" : "(full)"));
            removeMessages(4);
            this.mIsMemoryStatusReportingFailed = false;
            this.mRetryCount = 0;
            sendMemoryStatusReport(z);
            return;
        }
        if (i != 3) {
            if (i == 4) {
                Rlog.v("SmsStorageMonitor1", "EVENT_RETRY_MEMORY_STATUS_REPORTING - retry".concat(z ? "(not full)" : "(full)"));
                sendMemoryStatusReport(z);
                return;
            } else {
                if (i == 5) {
                    if (this.mIsMemoryStatusReportingFailed) {
                        Rlog.v("SmsStorageMonitor1", "EVENT_RADIO_ON - report failed sms memory status".concat(z ? "(not full)" : "(full)"));
                        sendMemoryStatusReport(z);
                        return;
                    }
                    return;
                }
                if (i == 101 && this.mPendingIccFullNotify) {
                    handleIccFull();
                    return;
                }
                return;
            }
        }
        AsyncResult asyncResult = (AsyncResult) message.obj;
        this.mIsWaitingResponse = false;
        Rlog.v("SmsStorageMonitor1", "EVENT_REPORT_MEMORY_STATUS_DONE - ".concat(asyncResult.exception == null ? "succeeded" : "failed"));
        if (this.mNeedNewReporting) {
            Rlog.v("SmsStorageMonitor1", "EVENT_REPORT_MEMORY_STATUS_DONE - report again now".concat(z ? "(not full)" : "(full)"));
            this.mNeedNewReporting = false;
            this.mRetryCount = 0;
            sendMemoryStatusReport(z);
            return;
        }
        if (asyncResult.exception != null) {
            int i2 = this.mRetryCount;
            this.mRetryCount = i2 + 1;
            if (i2 < this.mMaxRetryCount) {
                Rlog.v("SmsStorageMonitor1", "EVENT_REPORT_MEMORY_STATUS_DONE - retry in " + this.mRetryDelay + "ms");
                sendMessageDelayed(obtainMessage(4), (long) this.mRetryDelay);
                return;
            }
            Rlog.v("SmsStorageMonitor1", "EVENT_REPORT_MEMORY_STATUS_DONE - no retry anymore(pended)");
            this.mRetryCount = 0;
            this.mIsMemoryStatusReportingFailed = true;
            return;
        }
        this.mRetryCount = 0;
        this.mIsMemoryStatusReportingFailed = false;
    }

    private void sendMemoryStatusReport(boolean z) {
        IccSmsInterfaceManager iccSmsInterfaceManager;
        this.mIsWaitingResponse = true;
        if (this.mContext.getResources().getBoolean(R.bool.config_useAttentionLight) && (iccSmsInterfaceManager = this.mPhone.getIccSmsInterfaceManager()) != null) {
            Rlog.d("SmsStorageMonitor1", "sendMemoryStatusReport: smsIfcMngr is available");
            if (iccSmsInterfaceManager.mDispatchersController.isIms() && z) {
                iccSmsInterfaceManager.mDispatchersController.reportSmsMemoryStatus(obtainMessage(3));
            }
        }
        this.mCi.reportSmsMemoryStatus(z, obtainMessage(3));
    }

    private void createWakelock() {
        PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) this.mContext.getSystemService("power")).newWakeLock(1, "SmsStorageMonitor");
        this.mWakeLock = wakeLockNewWakeLock;
        wakeLockNewWakeLock.setReferenceCounted(true);
    }

    public void handleIccFull() {
        if (!((UserManager) this.mContext.getSystemService("user")).isUserUnlocked()) {
            this.mPendingIccFullNotify = true;
            Rlog.d("SmsStorageMonitor1", "too early, wait for boot complete to send broadcast");
            return;
        }
        ComponentName defaultSimFullApplicationAsUser = SmsApplication.getDefaultSimFullApplicationAsUser(this.mContext, false, TelephonyUtils.getSubscriptionUserHandle(this.mContext, this.mPhone.getSubId()));
        if (defaultSimFullApplicationAsUser == null) {
            this.mPendingIccFullNotify = true;
            Rlog.d("SmsStorageMonitor1", "ComponentName is NULL");
            sendMessageDelayed(obtainMessage(101), 5000L);
        } else {
            this.mPendingIccFullNotify = false;
            Intent intent = new Intent("android.provider.Telephony.SIM_FULL");
            intent.setComponent(defaultSimFullApplicationAsUser);
            this.mWakeLock.acquire(5000L);
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.RECEIVE_SMS");
        }
    }

    public boolean isStorageAvailable() {
        return this.mStorageAvailable;
    }
}
