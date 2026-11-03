package miui.telephony;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsController;
import com.android.internal.telephony.SmsDispatchersController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSmsOptimizationController extends Handler {
    protected static final int EVENT_SEND_RETRY = 3;
    private static final long INTERVAL_RESCHEDUAL = 60000;
    private static final int SLOT_NUM = 2;
    private static final String TAG = "MiuiSMSOptimizationController";
    private AlarmManager mAlarmManager;
    protected MiuiSmsSaFallBackOptimization mMiuiSmsSaFallBackOptimization;
    protected MiuiSmsSmscOptimization mMiuiSmsSmscOptimization;
    protected Phone mPhone;
    protected SmsDispatchersController mSmsDispatchersController;
    public boolean misSmsOptimization = false;
    private long mStartTime = 0;
    private long mEndTime = 0;
    private boolean mAlarmActive = false;
    protected Map<Integer, SMSDispatcher.SmsTracker> mMapTrackers = new ConcurrentHashMap();
    private Map<Long, SMSDispatcher.SmsTracker> mSingleSmsTrackers = new ConcurrentHashMap();
    public ArrayList<MiuiSmsOptimization> mSmsOptimizationControllerList = new ArrayList<>();
    private IntentFilter mIntentfilter = new IntentFilter();
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: miui.telephony.MiuiSmsOptimizationController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            int i = SubscriptionManager.INVALID_SLOT_ID;
            if (action.equals(CommenUtils.ACTION_GET_VERIFICATION_CODE)) {
                String appPackageName = intent.getStringExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_PACKAGENAME);
                String appName = intent.getStringExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_APP_NAME);
                String phoneNumber = intent.getStringExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_NUMBER);
                int slotId = MiuiSmsOptimizationController.this.checkoutGetVerficationCodeSlotid(phoneNumber);
                Log.d(MiuiSmsOptimizationController.TAG, "ACTION_GET_VERIFICATION_CODE packageName = " + appPackageName + ", appName = " + appName + ", time = " + System.currentTimeMillis() + ", slotId = " + slotId);
                if (slotId == SubscriptionManager.INVALID_SLOT_ID) {
                    Log.w(MiuiSmsOptimizationController.TAG, "ACTION_GET_VERIFICATION_CODE can not match slotId for number");
                    return;
                } else {
                    if (slotId == MiuiSmsOptimizationController.this.mPhone.getPhoneId() && !MiuiSmsOptimizationController.this.mAlarmActive) {
                        MiuiSmsOptimizationController.this.mStartTime = System.currentTimeMillis();
                        MiuiSmsOptimizationController.this.startAlarm(slotId, appName, appPackageName);
                        return;
                    }
                    return;
                }
            }
            if (action.equals(CommenUtils.ACTION_RECEIVE_VERIFICATION_CODE)) {
                String appName2 = intent.getStringExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_APP_NAME);
                int phoneId = intent.getIntExtra("phone_id", -1);
                String appPackageName2 = intent.getStringExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_PACKAGENAME);
                if (phoneId == MiuiSmsOptimizationController.this.mPhone.getPhoneId()) {
                    MiuiSmsOptimizationController.this.mEndTime = System.currentTimeMillis();
                    long longTime = 0;
                    if (MiuiSmsOptimizationController.this.mAlarmActive) {
                        longTime = (MiuiSmsOptimizationController.this.mEndTime - MiuiSmsOptimizationController.this.mStartTime) / 1000;
                    }
                    Log.d(MiuiSmsOptimizationController.TAG, "ACTION_RECEIVE_VERIFICATION_CODE packageName = " + appPackageName2 + ", appName = " + appName2 + ", phoneId = " + phoneId + ", mStartTime = " + MiuiSmsOptimizationController.this.mStartTime + ", mEndTime = " + MiuiSmsOptimizationController.this.mEndTime + ", times = " + longTime + "s");
                    MiuiSmsOptimizationController.this.sendVerificationCodeBroadcast(phoneId, true, appName2, appPackageName2, longTime);
                    MiuiSmsOptimizationController.this.cancelAlarm();
                    return;
                }
                Log.d(MiuiSmsOptimizationController.TAG, "ACTION_RECEIVE_VERIFICATION_CODE ignored, phoneId = " + phoneId + ", mPhone = " + MiuiSmsOptimizationController.this.mPhone.getPhoneId());
                return;
            }
            if (action.equals(CommenUtils.ACTION_ALARM_VERIFICATION_CODE)) {
                int phoneId2 = intent.getIntExtra("phone_id", -1);
                String appPackageName3 = intent.getStringExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_PACKAGENAME);
                String appName3 = intent.getStringExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_APP_NAME);
                if (phoneId2 == MiuiSmsOptimizationController.this.mPhone.getPhoneId()) {
                    MiuiSmsOptimizationController.this.sendVerificationCodeBroadcast(phoneId2, false, appName3, appPackageName3, 60L);
                } else {
                    Log.d(MiuiSmsOptimizationController.TAG, "ACTION_ALARM_VERIFICATION_CODE ignored, phoneId = " + phoneId2 + ", mPhone = " + MiuiSmsOptimizationController.this.mPhone.getPhoneId());
                }
                MiuiSmsOptimizationController.this.cancelAlarm();
                Log.d(MiuiSmsOptimizationController.TAG, "ACTION_ALARM_VERIFICATION_CODE timeout");
            }
        }
    };

    public MiuiSmsOptimizationController(Phone phone, SmsDispatchersController smsDispatchersController) {
        this.mPhone = phone;
        this.mSmsDispatchersController = smsDispatchersController;
        this.mMiuiSmsSmscOptimization = new MiuiSmsSmscOptimization(phone, this);
        this.mMiuiSmsSaFallBackOptimization = new MiuiSmsSaFallBackOptimization(phone, this);
        this.mSmsOptimizationControllerList.add(this.mMiuiSmsSmscOptimization);
        this.mSmsOptimizationControllerList.add(this.mMiuiSmsSaFallBackOptimization);
        this.mAlarmManager = (AlarmManager) this.mPhone.getContext().getSystemService("alarm");
        registerReceiver();
    }

    public void initOptimization(Phone phone, String nowSmscAddress, boolean isIms) {
        for (int i = 0; i < this.mSmsOptimizationControllerList.size(); i++) {
            this.mSmsOptimizationControllerList.get(i).init(phone, nowSmscAddress, isIms);
        }
    }

    public void startMiuiSmsOptimization(Phone phone, Map<Integer, SMSDispatcher.SmsTracker> mapTrackers, String nowSmscAddress, boolean isIms, SMSDispatcher.SmsTracker tracker) {
        if (mapTrackers != null) {
            this.mMapTrackers = mapTrackers;
        }
        for (int i = 0; i < this.mSmsOptimizationControllerList.size(); i++) {
            MiuiSmsOptimization smsOptimization = this.mSmsOptimizationControllerList.get(i);
            if (smsOptimization.isNeedAndstartOptimization(phone, nowSmscAddress, isIms, tracker)) {
                Log.d(TAG, "SMS optimization isNeed = " + smsOptimization.getClass().getName());
                tracker.mSmsOptimizationList.add(smsOptimization);
            }
        }
        if (tracker.mSmsOptimizationList.isEmpty()) {
            return;
        }
        if (mapTrackers == null && tracker != null) {
            this.mSingleSmsTrackers.put(Long.valueOf(tracker.mMessageId), tracker);
        }
        checkIsReadyForSendSmsRetry(tracker);
    }

    private void checkIsReadyForSendSmsRetry(SMSDispatcher.SmsTracker tracker) {
        boolean allOptimizationsReady = true;
        for (int i = 0; i < this.mSmsOptimizationControllerList.size(); i++) {
            if (this.mSmsOptimizationControllerList.get(i).mStatus == 1) {
                allOptimizationsReady = false;
                break;
            }
        }
        Log.d(TAG, "SMS optimization allOptimizationsReady = " + allOptimizationsReady);
        if (allOptimizationsReady) {
            sendSmsOptimizationRetry(tracker);
        }
    }

    protected void onOptimizationReady(String optimizationName) {
        checkIsReadyForSendSmsRetry(null);
    }

    private void sendSmsOptimizationRetry(SMSDispatcher.SmsTracker tracker) {
        if (tracker != null) {
            sendMessageDelayed(obtainMessage(3, tracker), 2000L);
            return;
        }
        if (!this.mMapTrackers.isEmpty()) {
            for (SMSDispatcher.SmsTracker smsTracker : this.mMapTrackers.values()) {
                if (smsTracker.mSmsOptimizationStatus == 1) {
                    sendMessageDelayed(obtainMessage(3, smsTracker), 2000L);
                }
            }
        }
        if (!this.mSingleSmsTrackers.isEmpty()) {
            for (SMSDispatcher.SmsTracker smsTracker2 : this.mSingleSmsTrackers.values()) {
                if (smsTracker2.mSmsOptimizationStatus == 1) {
                    sendMessageDelayed(obtainMessage(3, smsTracker2), 2000L);
                }
            }
        }
    }

    public void smsSendOptimizationFinish(Phone phone, SMSDispatcher.SmsTracker tracker, String nowSmscAddress, boolean isSuccessed) {
        if (tracker != null && isLastSmsTracker(tracker)) {
            for (int i = 0; i < tracker.mSmsOptimizationList.size(); i++) {
                ((MiuiSmsOptimization) tracker.mSmsOptimizationList.get(i)).onFinish(phone, nowSmscAddress, isSuccessed);
            }
        }
    }

    private void sendRetrySms(SMSDispatcher.SmsTracker tracker) {
        if (this.mSmsDispatchersController != null) {
            this.mSmsDispatchersController.sendRetrySms(tracker);
            tracker.mSmsOptimizationStatus = 2;
        } else {
            Log.e(TAG, this.mSmsDispatchersController + " is null. Retry failed " + SmsController.formatCrossStackMessageId(tracker.mMessageId));
        }
        this.mSingleSmsTrackers.remove(Long.valueOf(tracker.mMessageId));
    }

    private void registerReceiver() {
        this.mIntentfilter.addAction(CommenUtils.ACTION_RECEIVE_VERIFICATION_CODE);
        this.mIntentfilter.addAction(CommenUtils.ACTION_GET_VERIFICATION_CODE);
        this.mIntentfilter.addAction(CommenUtils.ACTION_ALARM_VERIFICATION_CODE);
        this.mPhone.getContext().registerReceiver(this.mIntentReceiver, this.mIntentfilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkoutGetVerficationCodeSlotid(String number) {
        for (int i = 0; i < 2; i++) {
            SubscriptionInfo subInfo = getSubInfo(i);
            if (subInfo != null) {
                boolean isVirtualSim = SubscriptionManager.INVALID_SLOT_ID == subInfo.getSlotId();
                if (isVirtualSim) {
                    continue;
                } else {
                    TelephonyManager tm = ((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).createForSubscriptionId(subInfo.getSubscriptionId());
                    String phoneNumber = tm.getLine1Number(subInfo.getSubscriptionId());
                    if ("".equals(phoneNumber)) {
                        phoneNumber = subInfo.getDisplayNumber();
                    }
                    if (PhoneNumberUtils.compareLoosely(number, phoneNumber)) {
                        Log.d(TAG, " phoneNumber equals");
                        return i;
                    }
                }
            }
        }
        int i2 = SubscriptionManager.INVALID_SLOT_ID;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendVerificationCodeBroadcast(int phoneId, boolean isSuccess, String appName, String appPackageName, long longTime) {
        Intent intent = new Intent(CommenUtils.ACTION_VERIFICATION_CODE);
        intent.addFlags(268435456);
        intent.putExtra("phone_id", phoneId);
        intent.putExtra(CommenUtils.KEY_SMS_IS_VERIFICATION_SUCCESS, isSuccess);
        intent.putExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_APP_NAME, appName);
        intent.putExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_PACKAGENAME, appPackageName);
        intent.putExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_TIME, longTime);
        Log.d(TAG, "sendSmsCompleteDotBroadcast");
        this.mPhone.getContext().sendBroadcast(intent);
    }

    private SubscriptionInfo getSubInfo(int slotId) {
        List<SubscriptionInfo> simInfoRecordList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        for (SubscriptionInfo info : simInfoRecordList) {
            if (info.getSlotId() == slotId) {
                return info;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAlarm(int slotId, String appName, String appPackageName) {
        Log.d(TAG, "Get verification code start alarm");
        Intent intent = new Intent(CommenUtils.ACTION_ALARM_VERIFICATION_CODE);
        intent.addFlags(16777216);
        intent.putExtra("phone_id", slotId);
        intent.putExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_APP_NAME, appName);
        intent.putExtra(CommenUtils.KEY_SMS_VERIFICATION_CODE_PACKAGENAME, appPackageName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mPhone.getContext(), 0, intent, 201326592);
        this.mAlarmManager.set(0, System.currentTimeMillis() + INTERVAL_RESCHEDUAL, pendingIntent);
        this.mAlarmActive = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAlarm() {
        this.mAlarmActive = false;
        PendingIntent pi = PendingIntent.getBroadcast(this.mPhone.getContext(), 0, new Intent(CommenUtils.ACTION_ALARM_VERIFICATION_CODE), 603979776);
        if (pi != null) {
            Log.d(TAG, "Get verification code cancel alarm");
            this.mAlarmManager.cancel(pi);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 3:
                sendRetrySms((SMSDispatcher.SmsTracker) msg.obj);
                break;
        }
    }

    private boolean isLastSmsTracker(SMSDispatcher.SmsTracker tracker) {
        if (tracker == null || tracker.mSmsHeader == null) {
            return true;
        }
        boolean isSinglePartOrLastPart = true;
        if (this.mMapTrackers != null && !this.mMapTrackers.isEmpty()) {
            for (SMSDispatcher.SmsTracker smsTracker : this.mMapTrackers.values()) {
                if (smsTracker != tracker) {
                    isSinglePartOrLastPart = false;
                }
            }
        }
        Log.d(TAG, " isSinglePartOrLastPart = " + isSinglePartOrLastPart);
        return isSinglePartOrLastPart;
    }

    public void optimizationTimeOut(SMSDispatcher.SmsTracker tracker) {
        boolean isSendRetry = false;
        for (int i = 0; i < tracker.mSmsOptimizationList.size(); i++) {
            if (((MiuiSmsOptimization) tracker.mSmsOptimizationList.get(i)).mStatus == 2 && tracker.mSmsOptimizationStatus == 1) {
                isSendRetry = true;
            }
        }
        Log.d(TAG, "optimizationTimeOut isSendRetry = " + isSendRetry);
        if (isSendRetry) {
            sendRetrySms(tracker);
            return;
        }
        smsSendOptimizationFinish(this.mPhone, tracker, "", false);
        tracker.onFailed(this.mPhone.getContext(), 1, -1);
        this.mSingleSmsTrackers.remove(Long.valueOf(tracker.mMessageId));
    }

    public boolean isSmsSaOptimization() {
        return this.misSmsOptimization;
    }

    public static boolean isSupportOptimization(Phone phone) {
        return MiuiSmsOptimization.isSupportOptimization(phone);
    }

    public boolean containsSmscOptimization(Phone phone, SMSDispatcher.SmsTracker tracker) {
        if (this.mMiuiSmsSmscOptimization == null) {
            return false;
        }
        return this.mMiuiSmsSmscOptimization.containsSmscOptimization(phone, tracker);
    }
}
