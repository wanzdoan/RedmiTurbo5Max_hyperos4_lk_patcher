package com.android.internal.telephony;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyFrameworkInitializer;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.utils.MiCloudSmsCmd;
import com.android.internal.telephony.utils.MiuiSmsCheckerUtils;
import com.android.internal.telephony.utils.NtnBoxOptimization;
import com.android.internal.telephony.utils.NtnUtils;
import com.android.internal.util.ArrayUtils;
import com.miui.enterprise.PhoneHelper;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ProcessManager;
import miui.process.RunningProcessInfo;
import miui.provider.ExtraTelephony;
import miui.security.ISecurityManager;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.MiuiSmsOptimization;
import miui.telephony.MiuiSmsOptimizationController;
import miui.telephony.MiuiSmsQueue;
import miui.telephony.MiuiSmsSmscOptimization;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSmsControllerImpl implements MiuiSmsControllerStub.IMiuiSmsController {
    public static final String ACTION_SEND_SMS_RESULT_EVENT = "com.anrdoid.internal.action.SEND_SMS_RESULT_EVENT";
    private static final String ACTION_SEND_SMS_VERIFY = "miui.intent.action.SEND_SMS_VERIFY";
    private static final String CT_AUTO_ADRESS = "10659401";
    private static final String CT_PACKAGENAME = "com.xiaomi.registration";
    private static final String CT_PACKAGENAME_OLDER = "com.qualcomm.qti.autoregistration";
    private static final String KEY_CALLING_ATTRIBUTION_TAG = "callingAttributionTag";
    private static final String KEY_CALLING_PACKAGE = "callingPackage";
    private static final String KEY_CALLING_PID = "callingPid";
    private static final String KEY_CALLING_UID = "callingUid";
    private static final String KEY_DELIVERY_INTENT = "deliveryIntent";
    private static final String KEY_DEST_ADDR = "destAddr";
    private static final String KEY_MESSAGE_ID = "messageId";
    private static final String KEY_PERSIST_MESSAGE_FOR_NON_DEFAULT_SMS_APP = "persistMessageForNonDefaultSmsApp";
    private static final String KEY_SC_ADDR = "scAddr";
    private static final String KEY_SENT_INTENT = "sentIntent";
    private static final String KEY_SUB_ID = "subId";
    private static final String KEY_TEXT = "text";
    private static final String LOG_TAG = "MiuiSmsControllerImpl";
    private static final int MESSAGE_TYPE_SENT = 2;
    private static final String PRIVACY_TAG = "PrivacyTestMode";
    private static final String PROPERTY_CTA_PRIVACY_TEST_MODE = "persist.miui.cta.privacy_test_mode";
    private static final String TAG = "MiuiSmsController";
    private static final String VERIFY_CLASS = "com.miui.permcenter.ctaverify.SendSmsVerificationActivity";
    private static final String VERIFY_PACKAGE = "com.miui.securitycenter";
    private static final DateTimeFormatter sFormatter = DateTimeFormatter.ofPattern("MM月d日 HH:mm:ss");
    private NtnUtils mNtnUtils;
    private Phone mPhone;
    private SmsDispatchersController mSmsController;
    private MiuiSmsQueue mSmsQueue = null;
    private MiuiSmsOptimizationController mMiuiSmsOptimizationController = null;

    public void init(Phone phone, SmsDispatchersController smsController, Object... arg) {
        this.mPhone = phone;
        this.mSmsController = smsController;
        if (this.mSmsQueue == null) {
            this.mSmsQueue = new MiuiSmsQueue(smsController, this);
        }
        if (this.mMiuiSmsOptimizationController == null) {
            log("create mMiuiSmsOptimizationController ");
            this.mMiuiSmsOptimizationController = new MiuiSmsOptimizationController(phone, smsController);
        }
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, phone.getPhoneId());
    }

    public void dispose(Phone phone) {
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, phone.getPhoneId());
    }

    public PackageInfo getCallerAppInfo(SMSDispatcher smsDispatcher, HashMap<String, Object> data, PackageInfo appInfoByUid) {
        return SmsDispatcherProcessor.getCallerAppInfo(smsDispatcher, data, appInfoByUid);
    }

    public boolean shouldCheckSmsUsage(Phone phone, String packageName) {
        return SmsDispatcherProcessor.shouldCheckSmsUsage(packageName);
    }

    public SmsMessage.SubmitPdu getSubmitPdu(Phone phone, String scAddress, String destAddr, byte[] data, boolean statusReportRequested, SmsMessageBase.SubmitPduBase pdu, int destPort, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        return SmsDispatcherProcessor.getSubmitPdu(scAddress, destAddr, data, statusReportRequested, pdu, destPort, sentIntent, deliveryIntent);
    }

    public String reMapSmscNumber(Phone phone, String scAddr) {
        return SmsDispatcherProcessor.reMapSmscNumber(phone, scAddr);
    }

    public void processNextSms(Phone phone, SmsDispatchersController smsController) {
        if (this.mSmsQueue != null) {
            this.mSmsQueue.processNextSms();
        }
    }

    public void enqueueOrSendSms(Phone phone, SMSDispatcher.SmsTracker tracker, SmsDispatchersController smsController) {
        if (this.mSmsQueue != null) {
            this.mSmsQueue.enqueueOrSendSms(tracker);
        }
    }

    public void enableSynced(Phone phone, SmsDispatchersController smsController, boolean value) {
        if (this.mSmsQueue != null) {
            this.mSmsQueue.enableSynced(value);
        }
    }

    public boolean carrierDisableSendSmsOverIms(Phone phone) {
        return SmsDispatcherProcessor.carrierDisableSendSmsOverIms(phone);
    }

    public boolean beforeDispatchIntent(InboundSmsHandler inboundSmsHandler, Intent intent, String permission, int appOp, String deleteWhere, String[] deleteWhereArgs) {
        return InboundSmsProcessor.beforeDispatchIntent(inboundSmsHandler, intent, permission, appOp, deleteWhere, deleteWhereArgs);
    }

    public boolean isWapPushForMms(byte[] pdu, InboundSmsHandler handler, InboundSmsTracker tracker, WapPushOverSms wapPush) {
        return InboundSmsProcessor.isWapPushForMms(pdu, handler, tracker, wapPush);
    }

    public boolean isCTAutoRegMsg(Phone phone, SmsMessage sms) {
        return InboundSmsProcessor.isCTAutoRegMsg(phone, sms);
    }

    public boolean isNeedSendCsSMS(Phone phone, SMSDispatcher.SmsTracker tracker, String format) {
        if (CT_AUTO_ADRESS.equals(tracker.mDestAddress) && !TelephonyManagerEx.getDefault().isSatelliteEnabled()) {
            if (CT_PACKAGENAME.equals(tracker.getAppPackageName()) || CT_PACKAGENAME_OLDER.equals(tracker.getAppPackageName())) {
                Rlog.d(LOG_TAG, "remove ct autoregist over cs!");
                return false;
            }
            return true;
        }
        return true;
    }

    public boolean startConsentUiIfNeeded(Context context, String callingPackage, int subId, String callingAttributionTag, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, long messageId) {
        boolean z;
        List<String> packages = HyperOSCustFeatureResolve.getStringArray("config_send_sm_no_check_packages");
        if (!Build.IS_INTERNATIONAL_BUILD && !ArrayUtils.contains(packages, callingPackage)) {
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            boolean isTrustedSource = UserHandle.getAppId(callingUid) < 10000;
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(callingPackage, 0);
                if (!isTrustedSource) {
                    isTrustedSource = (info == null || info.applicationInfo == null || (!info.applicationInfo.isSystemApp() && !info.applicationInfo.isSignedWithPlatformKey() && !callingPackage.equals(Telephony.Sms.getDefaultSmsPackage(context)))) ? false : true;
                }
                try {
                    if (!isPrivacyTestModeEnabled(callingUid)) {
                        z = true;
                    } else {
                        try {
                            RunningProcessInfo processInfo = ProcessManager.getRunningProcessInfoByPid(callingPid);
                            String label = "";
                            if (info != null && info.applicationInfo != null) {
                                label = info.applicationInfo.loadLabel(context.getPackageManager()).toString();
                            }
                            String processName = processInfo == null ? callingPackage : processInfo.mProcessName;
                            String current = sFormatter.format(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                            z = true;
                            try {
                                Log.i(PRIVACY_TAG, current + "<" + label + ">[" + processName + "][" + callingPackage + "]:[sendTextForSubscriber]" + context.getResources().getString(286196127) + ".." + destAddr);
                            } catch (RemoteException e) {
                                e = e;
                                e.printStackTrace();
                            }
                        } catch (RemoteException e2) {
                            e = e2;
                            z = true;
                        }
                    }
                    if (!isTrustedSource) {
                        Intent intent = new Intent(ACTION_SEND_SMS_VERIFY);
                        intent.setComponent(new ComponentName(VERIFY_PACKAGE, VERIFY_CLASS));
                        try {
                            intent.putExtra(KEY_SUB_ID, subId);
                            intent.putExtra(KEY_CALLING_PACKAGE, callingPackage);
                            intent.putExtra(KEY_CALLING_UID, callingUid);
                            intent.putExtra(KEY_CALLING_PID, callingPid);
                            try {
                                intent.putExtra(KEY_CALLING_ATTRIBUTION_TAG, callingAttributionTag);
                                intent.putExtra(KEY_DEST_ADDR, destAddr);
                                try {
                                    intent.putExtra(KEY_SC_ADDR, scAddr);
                                    try {
                                        intent.putExtra(KEY_TEXT, text);
                                        try {
                                            intent.putExtra(KEY_SENT_INTENT, sentIntent);
                                            try {
                                                intent.putExtra(KEY_DELIVERY_INTENT, deliveryIntent);
                                                intent.putExtra(KEY_PERSIST_MESSAGE_FOR_NON_DEFAULT_SMS_APP, persistMessageForNonDefaultSmsApp);
                                                intent.putExtra(KEY_MESSAGE_ID, messageId);
                                                intent.addFlags(268435456);
                                                if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                                                    try {
                                                        context.startActivity(intent);
                                                        return z;
                                                    } catch (Exception e3) {
                                                        e = e3;
                                                    }
                                                } else {
                                                    return false;
                                                }
                                            } catch (Exception e4) {
                                                e = e4;
                                            }
                                        } catch (Exception e5) {
                                            e = e5;
                                        }
                                    } catch (Exception e6) {
                                        e = e6;
                                    }
                                } catch (Exception e7) {
                                    e = e7;
                                }
                            } catch (Exception e8) {
                                e = e8;
                            }
                        } catch (Exception e9) {
                            e = e9;
                        }
                    } else {
                        return false;
                    }
                } catch (Exception e10) {
                    e = e10;
                }
            } catch (Exception e11) {
                e = e11;
            }
            Log.e(TAG, "calling package not found!", e);
            return false;
        }
        return false;
    }

    public boolean isPrivacyTestModeEnabled(int uid) {
        return Build.IS_DEBUGGABLE && UserHandle.getAppId(uid) > 2000 && SystemProperties.getBoolean(PROPERTY_CTA_PRIVACY_TEST_MODE, false);
    }

    public String setSmscAddress(Context context, int subid, String smsc) {
        if (!SubscriptionManager.isValidSubscriptionId(subid)) {
            return smsc;
        }
        boolean mNeedAddressType = CarrierConfigManagerEx.getBooleanConfigForSubId(context, subid, "config_smsc_no_need_show_international_number");
        log("mNeedAddressType: " + mNeedAddressType);
        if (mNeedAddressType && smsc != null && !smsc.isEmpty() && smsc.charAt(0) != '\"' && !smsc.contains(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE)) {
            String str = SystemProperties.get("persist.radio.support.smsc" + subid, "");
            if (!str.isEmpty()) {
                return "\"" + smsc + "\"," + str;
            }
            return smsc;
        }
        return smsc;
    }

    public String getSmscAddressResponse(RIL ril, String smsc, Object... arg) {
        int indexOfComma;
        int[] subId = SubscriptionManager.getSubId(ril.mPhoneId.intValue());
        if (SubscriptionManager.isValidSubscriptionId(subId[0])) {
            boolean mNeedAddressType = CarrierConfigManagerEx.getBooleanConfigForSubId(ril.mContext, subId[0], "config_smsc_no_need_show_international_number");
            log("mNeedAddressType: " + mNeedAddressType);
            if (mNeedAddressType && smsc != null && !smsc.isEmpty() && (indexOfComma = smsc.indexOf(44)) > 0) {
                String address = smsc.substring(indexOfComma + 1).trim();
                if (address.equals("145") || address.equals("129")) {
                    String smsc2 = smsc.substring(0, indexOfComma).trim().replaceAll("\"", "");
                    SystemProperties.set("persist.radio.support.smsc" + subId[0], address);
                    return smsc2;
                }
                return smsc;
            }
            return smsc;
        }
        return smsc;
    }

    public boolean isBlocked(Phone phone, String phoneNumber, Bundle extras, long timeout, boolean defVal) {
        return MiuiSmsCheckerUtils.isBlocked(phone.getContext(), phoneNumber, extras, timeout);
    }

    public boolean isBlocked(Phone phone, String phoneNumber, boolean defVal) {
        return MiuiSmsCheckerUtils.isBlocked(phone.getContext(), phoneNumber);
    }

    public boolean smsSendRestricted(Phone phone, String destAddr) {
        return PhoneHelper.smsSendRestricted(phone.getContext(), destAddr) || PhoneHelperStub.getInstance().isSMSSendRestriction(destAddr);
    }

    public void sendSmsDotBroadcast(Phone phone, SMSDispatcher.SmsTracker tracker, String smscAddress, int errorCode, boolean isSuccess) {
        String saveSmsc = SystemProperties.get("persist.radio.support.sendsmsc" + phone.getSubId(), "");
        Intent intent = new Intent(ACTION_SEND_SMS_RESULT_EVENT);
        intent.addFlags(268435456);
        intent.putExtra("sms_packagename", tracker.getAppPackageName());
        intent.putExtra("is_Sent_SMS_Success", isSuccess);
        intent.putExtra("error_Code", errorCode);
        intent.putExtra("phone_id", phone.getPhoneId());
        intent.putExtra("smsc_Address", smscAddress);
        intent.putExtra("save_Smsc", saveSmsc);
        intent.putExtra("sms_Optimization_Status", tracker.mSmsOptimizationStatus);
        intent.putExtra("sms_Optimization_List", smsOptimizationListToString(tracker.mSmsOptimizationList));
        log("sendSmsCompleteDotBroadcast packageName = " + tracker.getAppPackageName());
        phone.getContext().sendBroadcast(intent);
    }

    public String smsOptimizationListToString(ArrayList<Object> smsOptimizationList) {
        if (smsOptimizationList == null || smsOptimizationList.isEmpty()) {
            return "[]";
        }
        String smsOptimizationStr = "[";
        for (int i = 0; i < smsOptimizationList.size(); i++) {
            MiuiSmsOptimization smsOptimization = (MiuiSmsOptimization) smsOptimizationList.get(i);
            String className = smsOptimization.getClass().getName();
            int lastDotIndex = className.lastIndexOf(".");
            smsOptimizationStr = smsOptimizationStr + className.substring(lastDotIndex + 1) + ".status = " + smsOptimization.mStatus + ";";
        }
        return smsOptimizationStr + "]";
    }

    public int miuiSmsSendOptimizationStatus(Phone phone, SMSDispatcher.SmsTracker tracker, Map<Integer, SMSDispatcher.SmsTracker> mapTrackers, String nowSmscAddress, boolean isIms, boolean isSuccessed, int reasonCode, SmsDispatchersController smsDispatchersController) {
        if (!MiuiSmsOptimizationController.isSupportOptimization(phone)) {
            sendSmsDotBroadcast(phone, tracker, nowSmscAddress, reasonCode, isSuccessed);
            return tracker.mSmsOptimizationStatus;
        }
        if (isSuccessed && tracker.mSmsOptimizationStatus == 0) {
            this.mMiuiSmsOptimizationController.initOptimization(phone, nowSmscAddress, isIms);
            sendSmsDotBroadcast(phone, tracker, nowSmscAddress, reasonCode, isSuccessed);
            return tracker.mSmsOptimizationStatus;
        }
        int oldStatus = tracker.mSmsOptimizationStatus;
        switch (tracker.mSmsOptimizationStatus) {
            case 0:
                this.mMiuiSmsOptimizationController.startMiuiSmsOptimization(phone, mapTrackers, nowSmscAddress, isIms, tracker);
                for (int i = 0; i < tracker.mSmsOptimizationList.size(); i++) {
                    if (((MiuiSmsOptimization) tracker.mSmsOptimizationList.get(i)).mStatus != 0) {
                        tracker.mSmsOptimizationStatus = 1;
                    }
                    break;
                }
                break;
            case 2:
                sendSmsDotBroadcast(phone, tracker, nowSmscAddress, reasonCode, isSuccessed);
                this.mMiuiSmsOptimizationController.smsSendOptimizationFinish(phone, tracker, nowSmscAddress, isSuccessed);
                break;
        }
        if (tracker.mSmsOptimizationStatus == 0) {
            sendSmsDotBroadcast(phone, tracker, nowSmscAddress, reasonCode, isSuccessed);
        }
        log("miuiSmsOptimizationStatus status = " + tracker.mSmsOptimizationStatus + ", oldStatus = " + oldStatus);
        return tracker.mSmsOptimizationStatus;
    }

    public boolean isNeedSendCsRetry(Phone phone, SMSDispatcher.SmsTracker tracker, int errorCode, boolean usingImsRetry) {
        boolean isNotSupportCS = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_miui_sms_not_support_cs_optimization");
        if (!usingImsRetry && isNotSupportCS) {
            sendSmsDotBroadcast(phone, tracker, "", errorCode, false);
            log("isNeedSendCsRetry retryUsingImsService = " + usingImsRetry);
            return false;
        }
        return true;
    }

    public String replaceSmscAddr(Phone phone, String nowSmsc, String scAddr) {
        return MiuiSmsSmscOptimization.replaceSmscAddr(phone, nowSmsc, scAddr);
    }

    public boolean containsSmscOptimization(Phone phone, SMSDispatcher.SmsTracker tracker) {
        if (this.mMiuiSmsOptimizationController != null) {
            return this.mMiuiSmsOptimizationController.containsSmscOptimization(phone, tracker);
        }
        return false;
    }

    public boolean isSmsSaOptimization() {
        if (this.mMiuiSmsOptimizationController != null) {
            return this.mMiuiSmsOptimizationController.isSmsSaOptimization();
        }
        return false;
    }

    public void sendSms(SMSDispatcher smsDispatcher, SMSDispatcher.SmsTracker tracker) {
        if (smsDispatcher == null || tracker == null) {
            return;
        }
        smsDispatcher.sendSms(tracker);
    }

    public SMSDispatcher getNtnSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
        if (this.mNtnUtils == null) {
            this.mNtnUtils = new NtnUtils();
        }
        return this.mNtnUtils.getNtnSmsDispatcher(phone, smsDispatchersController);
    }

    public boolean ntnInboundSmsHandlerIsWaiting(Phone phone) {
        if (this.mNtnUtils == null) {
            this.mNtnUtils = new NtnUtils();
        }
        return this.mNtnUtils.ntnInboundSmsHandlerIsWaiting(phone);
    }

    public void newNtnBoxSms() {
        if (TelephonyManagerEx.getDefault().isSatelliteBox() && NtnBoxOptimization.getInstance() != null) {
            NtnBoxOptimization.getInstance().onReceiveNewSms();
        }
    }

    private void log(String s) {
        Rlog.d("MiuiSmsControllerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    private void loge(String s) {
        Rlog.e("MiuiSmsControllerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    public static class InboundSmsProcessor {
        private static final int ENCODING_16BIT = 3;
        private static final int EVENT_BROADCAST_COMPLETE = 3;
        private static final int KRKT_PID = 83;
        private static final int KRKT_PORT = 49697;
        private static final String TAG = "InboundSmsProcessor";
        private static Pattern sIndiaSpPattern = Pattern.compile("^[A-Za-z][A-Za-z][-]\\S{6}$");
        private static final int[] sLguPorts = {49198, 79175, 49200, 49201, 49202, 49203, 49204, 49425};

        private InboundSmsProcessor() {
        }

        static boolean writeMessageToInbox(Context context, Intent intent) {
            boolean isServiceNumber;
            if (context == null || intent == null || intent.getExtras() == null || !"android.provider.Telephony.SMS_DELIVER".equals(intent.getAction())) {
                Log.d(TAG, "writeMessageToInbox args is wrong");
                return 0;
            }
            try {
                try {
                    ComponentName cn = SmsApplication.getDefaultSmsApplication(context, false);
                    if (cn != null && !"com.android.mms".equals(cn.getPackageName())) {
                        Log.d(TAG, "default sms application is " + cn.getPackageName());
                        Log.d(TAG, "writeMessageToInbox finally");
                        return false;
                    }
                    android.telephony.SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    try {
                        if (msgs == null || msgs.length < 1) {
                            Log.d(TAG, "Failed to parse SMS pdu");
                            Log.d(TAG, "writeMessageToInbox finally");
                            return 0;
                        }
                        int subId = intent.getExtras().getInt("subscription", -1);
                        if (subId < 0) {
                            Log.e(TAG, "writeMessageToInbox subId < 0");
                            Log.d(TAG, "writeMessageToInbox finally");
                            return false;
                        }
                        android.telephony.SmsMessage sms = msgs[0];
                        if (sms == null || sms.mWrappedSmsMessage == null) {
                            Log.e(TAG, "writeMessageToInbox: sms == null or sms.mWrappedSmsMessage == null");
                            Log.d(TAG, "writeMessageToInbox finally");
                            return 0;
                        }
                        if (TextUtils.isEmpty(sms.getDisplayOriginatingAddress())) {
                            Log.e(TAG, "writeMessageToInbox address is empty");
                            Log.d(TAG, "writeMessageToInbox finally");
                            return false;
                        }
                        if (isDuplicate(context, intent.getExtras().getInt("phone", 0), msgs, subId)) {
                            Log.v(TAG, "duplicate sms");
                            Log.d(TAG, "writeMessageToInbox finally");
                            return true;
                        }
                        if (sms.getDisplayMessageBody() == null) {
                            Log.v(TAG, "display message body is null");
                            Log.d(TAG, "writeMessageToInbox finally");
                            return false;
                        }
                        if (sms.getMessageClass() == android.telephony.SmsMessage.MessageClass.CLASS_0) {
                            Log.v(TAG, "message class is 0");
                            Log.d(TAG, "writeMessageToInbox finally");
                            return false;
                        }
                        if (sms.isReplace()) {
                            Log.v(TAG, "sms is replace");
                            Log.d(TAG, "writeMessageToInbox finally");
                            return false;
                        }
                        ContentValues values = parseSmsMessage(context, msgs);
                        values.put("sim_id", Long.valueOf(subId));
                        String address = values.getAsString("address");
                        if (TextUtils.isEmpty(address)) {
                            isServiceNumber = false;
                        } else if (isIndiaServiceNumber(address)) {
                            isServiceNumber = true;
                        } else {
                            boolean isServiceNumber2 = intent.getExtras().getBoolean("miui.intent.SERVICE_NUMBER", false);
                            isServiceNumber = !isServiceNumber2 ? ExtraTelephony.isServiceNumber(address) : isServiceNumber2;
                        }
                        if (isServiceNumber) {
                            values.put("advanced_seen", (Integer) 1);
                        } else {
                            values.put("advanced_seen", (Integer) 0);
                        }
                        int blockType = intent.getIntExtra("blockType", 0);
                        int blockType2 = ExtraTelephony.getRealBlockType(blockType);
                        boolean urlSuspicious = ExtraTelephony.isURLFlagRisky(blockType);
                        if (blockType2 == 8) {
                            values.put("url_risky_type", (Integer) 3);
                        } else if (urlSuspicious) {
                            values.put("url_risky_type", (Integer) 1);
                        }
                        long token = Binder.clearCallingIdentity();
                        try {
                            try {
                                Uri uri = context.getContentResolver().insert(Telephony.Sms.Inbox.CONTENT_URI.buildUpon().appendQueryParameter("no_notify", "1").build(), values);
                                if (uri != null) {
                                    Log.d(TAG, "writeMessageToInbox uri is not null");
                                }
                                Log.d(TAG, "writeMessageToInbox insert uri is successfully");
                                intent.putExtra("msg_uri", uri.toString());
                                Binder.restoreCallingIdentity(token);
                                Log.d(TAG, "writeMessageToInbox finally");
                                return true;
                            } catch (Throwable th) {
                                Binder.restoreCallingIdentity(token);
                                throw th;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to persist inbox msgs", e);
                            Binder.restoreCallingIdentity(token);
                            Log.d(TAG, "writeMessageToInbox finally");
                            return 0;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        Log.e(TAG, "writeMessageToInbox injector is ", e);
                        Log.d(TAG, "writeMessageToInbox finally");
                        return 0;
                    }
                } catch (Exception e3) {
                    e = e3;
                }
            } catch (Throwable th2) {
                Log.d(TAG, "writeMessageToInbox finally");
                throw th2;
            }
        }

        private static ContentValues parseSmsMessage(Context context, android.telephony.SmsMessage[] smsMessageArr) {
            String strReplaceFormFeeds;
            android.telephony.SmsMessage smsMessage = smsMessageArr[0];
            ContentValues contentValues = new ContentValues();
            contentValues.put(XmPhoneConstants.ERROR_CODE, (Integer) 0);
            String displayOriginatingAddress = smsMessage.getDisplayOriginatingAddress();
            if (PhoneHelperStub.getInstance().isNumberEncryptEnabled()) {
                displayOriginatingAddress = PhoneHelperStub.getInstance().getEncryptPhoneNumber(displayOriginatingAddress);
            }
            contentValues.put("address", displayOriginatingAddress);
            contentValues.put("date_sent", Long.valueOf(smsMessage.getTimestampMillis()));
            GregorianCalendar gregorianCalendar = new GregorianCalendar(2011, 8, 18);
            GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
            long jCurrentTimeMillis = System.currentTimeMillis();
            gregorianCalendar2.setTimeInMillis(jCurrentTimeMillis);
            if (gregorianCalendar2.before(gregorianCalendar)) {
                jCurrentTimeMillis = smsMessage.getTimestampMillis();
            }
            contentValues.put("date", Long.valueOf(jCurrentTimeMillis));
            contentValues.put("protocol", Integer.valueOf(smsMessage.getProtocolIdentifier()));
            contentValues.put("seen", (Integer) 0);
            contentValues.put("read", (Integer) 0);
            String pseudoSubject = smsMessage.getPseudoSubject();
            if (!TextUtils.isEmpty(pseudoSubject)) {
                contentValues.put("subject", pseudoSubject);
            }
            contentValues.put("reply_path_present", Integer.valueOf(smsMessage.isReplyPathPresent() ? 1 : 0));
            contentValues.put("service_center", smsMessage.getServiceCenterAddress());
            Long.valueOf(0L);
            if (!TextUtils.isEmpty(displayOriginatingAddress)) {
                contentValues.put("thread_id", Long.valueOf(Telephony.Threads.getOrCreateThreadId(context, displayOriginatingAddress)));
            } else {
                Log.v(TAG, "address is empty");
            }
            if (smsMessageArr.length == 1) {
                strReplaceFormFeeds = replaceFormFeeds(smsMessage.getDisplayMessageBody());
            } else {
                strReplaceFormFeeds = replaceFormFeeds(buildMessageString(smsMessageArr));
            }
            contentValues.put("body", strReplaceFormFeeds);
            return contentValues;
        }

        private static String replaceFormFeeds(String s) {
            return s == null ? "" : s.replace('\f', '\n');
        }

        private static String buildMessageString(android.telephony.SmsMessage[] msgs) {
            byte[] userData;
            StringBuilder body = new StringBuilder();
            int length = 0;
            boolean needCombinByByte = true;
            for (android.telephony.SmsMessage m : msgs) {
                if (m == null || m.mWrappedSmsMessage == null) {
                    Log.v(TAG, "buildMessageString m or mWrappedSmsMessage is null");
                } else {
                    if (needCombinByByte && m.getEncodingType() != 3) {
                        needCombinByByte = false;
                    }
                    if (needCombinByByte && m.mWrappedSmsMessage != null && (userData = m.getUserData()) != null) {
                        length += userData.length;
                    }
                    if (!needCombinByByte && m.mWrappedSmsMessage != null) {
                        body.append(m.getDisplayMessageBody());
                    }
                }
            }
            Log.v(TAG, "buildMessageString needCombinByByte is " + needCombinByByte);
            if (needCombinByByte) {
                byte[] newbuf = new byte[length];
                int pos = 0;
                for (android.telephony.SmsMessage m2 : msgs) {
                    if (m2 != null && m2.mWrappedSmsMessage != null) {
                        byte[] userData2 = m2.getUserData();
                        int l = userData2.length;
                        System.arraycopy(userData2, 0, newbuf, pos, l);
                        pos += l;
                    }
                }
                try {
                    String result = new String(newbuf, "utf-16");
                    return result;
                } catch (Exception e) {
                    Log.e(TAG, "buildMessageString: new string utf-16 error");
                    return null;
                }
            }
            return body.toString();
        }

        private static boolean matchIndiaSpPattern(String number) {
            if (sIndiaSpPattern == null) {
                return false;
            }
            Matcher m = sIndiaSpPattern.matcher(number);
            boolean result = m.matches();
            return result;
        }

        private static boolean isIndiaServiceNumber(String number) {
            if (!Build.IS_INTERNATIONAL_BUILD || !Build.checkRegion("IN")) {
                return false;
            }
            boolean isIndiaSP = matchIndiaSpPattern(number);
            if (!isIndiaSP) {
                return !number.startsWith("+91") && 2 < number.length() && number.length() < 8;
            }
            return isIndiaSP;
        }

        private static boolean isDuplicate(Context context, int slotId, android.telephony.SmsMessage[] msgs, int subId) {
            android.telephony.SmsMessage sms = msgs[0];
            if (sms.getMessageClass() == android.telephony.SmsMessage.MessageClass.CLASS_0 || sms.getMessageClass() == android.telephony.SmsMessage.MessageClass.CLASS_2) {
                return false;
            }
            String originatingAddress = sms.getDisplayOriginatingAddress();
            if (TextUtils.isEmpty(originatingAddress)) {
                return false;
            }
            long timestamp = sms.getTimestampMillis();
            if (timestamp <= 0) {
                return false;
            }
            String body = buildMessageString(msgs);
            if (TextUtils.isEmpty(body)) {
                return false;
            }
            String carrierService = "";
            try {
                ITelephony phone = ITelephony.Stub.asInterface(TelephonyFrameworkInitializer.getTelephonyServiceManager().getTelephonyServiceRegisterer().get());
                try {
                    carrierService = phone.getBoundImsServicePackage(slotId, true, 1);
                } catch (Exception e) {
                    e = e;
                    Log.e(TAG, "isDuplicate, exception is ", e);
                }
            } catch (Exception e2) {
                e = e2;
            }
            if ("com.google.android.gts.imsservice".equals(carrierService) || SystemProperties.getBoolean("persist.radio.allow_mock_modem", false)) {
                Log.w(TAG, "here is GTS/CTS running, ignore whether duplicate or not!");
                return false;
            }
            String[] projection = {MiuiIccProvider.STR_ID, "address"};
            Cursor cursor = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, projection, "address=? AND date_sent=" + timestamp + " AND body=? AND mx_status=0 AND sim_id=?", new String[]{originatingAddress, body, Integer.toString(subId)}, null);
            if (cursor != null) {
                try {
                    try {
                        if (cursor.moveToFirst()) {
                            long id = cursor.getLong(0);
                            Log.w(TAG, "receive duplicated message from msg id = " + id);
                            return true;
                        }
                    } catch (Exception e3) {
                        Log.e(TAG, "isDuplicate query is ", e3);
                    }
                } finally {
                    cursor.close();
                }
            }
            return false;
        }

        static boolean beforeDispatchIntent(final InboundSmsHandler inboundSmsHandler, final Intent intent, final String permission, final int appOp, String deleteWhere, String[] deleteWhereArgs) throws Exception {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            FutureTask<Boolean> futureTask = new FutureTask<>(new Callable<Boolean>() { // from class: com.android.internal.telephony.MiuiSmsControllerImpl.InboundSmsProcessor.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Boolean call() {
                    boolean res = InboundSmsProcessor.beforeDispatchIntentInt(inboundSmsHandler, intent, permission, appOp);
                    return new Boolean(res);
                }
            });
            executor.execute(futureTask);
            try {
                try {
                    Boolean result = futureTask.get(1500L, TimeUnit.MILLISECONDS);
                    if (result != null && result.booleanValue()) {
                        try {
                            int rows = inboundSmsHandler.mContext.getContentResolver().delete(InboundSmsHandler.sRawUri, deleteWhere, deleteWhereArgs);
                            Log.d(TAG, "delete blocked sms from raw table rows: " + rows);
                            inboundSmsHandler.sendMessage(3);
                            Log.v(TAG, "beforeDispatchIntent result is true");
                            try {
                                executor.shutdown();
                            } catch (Exception e) {
                                Log.e(TAG, "beforeDispatchIntent shutdown exp ", e);
                            }
                            return true;
                        } catch (Exception e2) {
                            Log.e(TAG, "beforeDispatchIntent exp is ", e2);
                            Log.v(TAG, "beforeDispatchIntent result is false");
                        }
                    }
                } catch (Throwable e3) {
                    try {
                        executor.shutdown();
                        throw e3;
                    } catch (Exception e4) {
                        Log.e(TAG, "beforeDispatchIntent shutdown exp ", e4);
                        throw e3;
                    }
                }
            } catch (Exception e5) {
                Log.e(TAG, "beforeDispatchIntent exp is ", e5);
                try {
                    futureTask.cancel(true);
                } catch (Exception e1) {
                    Log.e(TAG, "beforeDispatchIntent futureTask.cancel exp ", e1);
                }
            }
            try {
                executor.shutdown();
            } catch (Exception e6) {
                Log.e(TAG, "beforeDispatchIntent shutdown exp ", e6);
            }
            Log.v(TAG, "beforeDispatchIntent result is false");
            if (writeMessageToInbox(inboundSmsHandler.mContext, intent)) {
                String uriStr = (String) intent.getExtras().get("msg_uri");
                if (TextUtils.isEmpty(uriStr)) {
                    try {
                        int rows2 = inboundSmsHandler.mContext.getContentResolver().delete(InboundSmsHandler.sRawUri, deleteWhere, deleteWhereArgs);
                        Log.d(TAG, "delete blocked sms from raw table rows: " + rows2);
                        inboundSmsHandler.sendMessage(3);
                        Log.v(TAG, "writeMessageToInbox result is true");
                        return true;
                    } catch (Exception e7) {
                        Log.e(TAG, "writeMessageToInbox exp is ", e7);
                    }
                }
            }
            Log.v(TAG, "writeMessageToInbox result is false");
            return false;
        }

        public static void maybeSendBroadcastForLgu(Context context, SmsMessageBase sms, int destPort) {
            if (!TelephonyManager.isCustForKrLgu()) {
                return;
            }
            try {
                if (isLguServicePort(destPort)) {
                    Intent lguServiceIntent = new Intent("com.lgt.action.APM_SMS_RECEIVED");
                    lguServiceIntent.setFlags(16777216);
                    lguServiceIntent.putExtra("tid", destPort);
                    byte[] originalText = GsmAlphabet.stringToGsm8BitPacked(sms.getMessageBody());
                    String message = new String(originalText, Charset.forName("UTF-8"));
                    lguServiceIntent.putExtra("body", message);
                    lguServiceIntent.putExtra("payload", message);
                    context.sendBroadcast(lguServiceIntent);
                    Log.i(TAG, "maybeSendBroadcastForLgu APM_SMS_RECEIVED intent sent successfully.");
                }
            } catch (Exception e) {
                Log.e(TAG, " maybeSendBroadcastForLgu error ", e);
            }
        }

        private static boolean isLguServicePort(int destPort) {
            for (int lguPort : sLguPorts) {
                if (destPort == lguPort) {
                    return true;
                }
            }
            return false;
        }

        /* JADX WARN: Code duplicated, block: B:31:0x00a0  */
        /* JADX WARN: Code duplicated, block: B:39:0x00d6  */
        /* JADX WARN: Code duplicated, block: B:43:0x00e9  */
        /* JADX WARN: Code duplicated, block: B:51:0x00d9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        static boolean beforeDispatchIntentInt(InboundSmsHandler inboundSmsHandler, Intent intent, String permission, int appOp) {
            Intent intent2;
            String address;
            boolean isServiceNumber;
            ISecurityManager securityManager;
            android.telephony.SmsMessage[] messages;
            String action = intent.getAction();
            boolean smsBlocked = false;
            addIntentFlagForKrKtCust(inboundSmsHandler.mContext, intent);
            if (!"android.provider.Telephony.SMS_DELIVER".equals(action)) {
                intent2 = intent;
            } else {
                try {
                    Object[] pdusArray = (Object[]) intent.getExtras().get("pdus");
                    String format = (String) intent.getExtras().get("format");
                    byte[][] pdus = new byte[pdusArray.length][];
                    for (int i = 0; i < pdusArray.length; i++) {
                        try {
                            byte[] part = (byte[]) pdusArray[i];
                            pdus[i] = part;
                        } catch (Exception e) {
                            e = e;
                            intent2 = intent;
                            Log.d(TAG, "Exception before InboundSmsDispatcher: " + e.toString());
                            address = "";
                            isServiceNumber = false;
                            messages = Telephony.Sms.Intents.getMessagesFromIntent(intent2);
                            if (messages != null) {
                                address = messages[0].getOriginatingAddress();
                                if (ExtraTelephony.isServiceNumber(address)) {
                                    isServiceNumber = true;
                                }
                            }
                            intent2.putExtra("miui.intent.SERVICE_NUMBER", isServiceNumber);
                            Log.v(TAG, "beforeDispatchIntentInt isServiceNumber is " + isServiceNumber);
                            if (!PhoneHelper.smsReceiveRestricted(inboundSmsHandler.mContext, address)) {
                                smsBlocked = true;
                            } else {
                                smsBlocked = true;
                            }
                            if (!smsBlocked) {
                                try {
                                    securityManager = ISecurityManager.Stub.asInterface(ServiceManager.getService("security"));
                                    if (securityManager.checkSmsBlocked(intent2)) {
                                        smsBlocked = true;
                                    }
                                } catch (Exception e2) {
                                    Log.e(TAG, "beforeDispatchIntentInt exp is ", e2);
                                }
                            }
                            Log.v(TAG, "beforeDispatchIntentInt smsBlocked is " + smsBlocked);
                            return smsBlocked;
                        }
                    }
                    int slotId = intent.getExtras().getInt("phone", 0);
                    int subId = intent.getExtras().getInt("subscription", -1);
                    intent2 = intent;
                    try {
                        if (checkSmsCmd(intent2, pdus, format, inboundSmsHandler.mContext, slotId, subId)) {
                            Log.d(TAG, "checkSmsCmd is true, intercept this sms.");
                            smsBlocked = true;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        Log.d(TAG, "Exception before InboundSmsDispatcher: " + e.toString());
                    }
                } catch (Exception e4) {
                    e = e4;
                    intent2 = intent;
                }
            }
            address = "";
            isServiceNumber = false;
            try {
                messages = Telephony.Sms.Intents.getMessagesFromIntent(intent2);
                if (messages != null && messages.length > 0) {
                    address = messages[0].getOriginatingAddress();
                    if (ExtraTelephony.isServiceNumber(address)) {
                        isServiceNumber = true;
                    }
                }
            } catch (Exception e5) {
                Log.e(TAG, " ServiceNumber parse error ", e5);
            }
            intent2.putExtra("miui.intent.SERVICE_NUMBER", isServiceNumber);
            Log.v(TAG, "beforeDispatchIntentInt isServiceNumber is " + isServiceNumber);
            if (!PhoneHelper.smsReceiveRestricted(inboundSmsHandler.mContext, address) || PhoneHelperStub.getInstance().isSMSReceiveRestriction(address)) {
                smsBlocked = true;
            }
            if (!smsBlocked) {
                securityManager = ISecurityManager.Stub.asInterface(ServiceManager.getService("security"));
                if (securityManager.checkSmsBlocked(intent2)) {
                    smsBlocked = true;
                }
            }
            Log.v(TAG, "beforeDispatchIntentInt smsBlocked is " + smsBlocked);
            return smsBlocked;
        }

        static boolean checkSmsCmd(Intent oriIntent, byte[][] pdus, String format, Context context, int slotId, int subId) {
            android.telephony.SmsMessage msg;
            if (pdus == null || pdus[0] == null || (msg = android.telephony.SmsMessage.createFromPdu(pdus[0], format)) == null) {
                return false;
            }
            String address = msg.getOriginatingAddress();
            StringBuilder body = new StringBuilder();
            for (byte[] bArr : pdus) {
                android.telephony.SmsMessage msg2 = android.telephony.SmsMessage.createFromPdu(bArr, format);
                if (msg2 != null) {
                    body.append(msg2.getDisplayMessageBody());
                }
            }
            return MiCloudSmsCmd.checkSmsCmd(oriIntent, context, address, body.toString(), slotId, subId) || MiCloudSmsCmd.checkAndDispatchActivationSms(context, slotId, address, body.toString());
        }

        static boolean isWapPushForMms(byte[] pdu, InboundSmsHandler handler, InboundSmsTracker tracker, WapPushOverSms wapPush) {
            try {
                if (wapPush.isWapPushForMms(pdu, handler)) {
                    Log.v(TAG, "isWapPushForMms");
                    return true;
                }
                return false;
            } catch (Exception ex) {
                Log.e(TAG, "ex:" + ex);
                return false;
            }
        }

        public static boolean isCTAutoRegMsg(Phone phone, SmsMessage sms) {
            String address = sms.getDisplayOriginatingAddress();
            if (MiuiSmsControllerImpl.CT_AUTO_ADRESS.equals(address) || "+8610659401".equals(address)) {
                Log.d(TAG, "handleAutoRegMessage receive.");
                sendCTAutoRegReceipt(phone, sms, MiuiSmsControllerImpl.CT_PACKAGENAME);
                sendCTAutoRegReceipt(phone, sms, MiuiSmsControllerImpl.CT_PACKAGENAME_OLDER);
                return true;
            }
            return false;
        }

        private static void sendCTAutoRegReceipt(Phone phone, SmsMessage sms, String targetPackage) {
            Intent i = new Intent("com.xiaomi.autoreg.receivesms");
            i.setPackage(targetPackage);
            i.addFlags(268435488);
            i.putExtra("pdu", sms.getPdu());
            i.putExtra("phoneId", phone.getPhoneId());
            phone.getContext().sendBroadcast(i);
        }

        private static void addIntentFlagForKrKtCust(Context context, Intent intent) {
            if (!TelephonyManager.isCustForKrKt() || !"android.intent.action.DATA_SMS_RECEIVED".equals(intent.getAction())) {
                return;
            }
            try {
                android.telephony.SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                if (messages == null || messages.length <= 0) {
                    Log.i(TAG, "addIntentFlagForKrKtCust smsMessage is null");
                    return;
                }
                android.telephony.SmsMessage smsMessage = messages[0];
                SmsMessageBase smsMessageBase = smsMessage.mWrappedSmsMessage;
                SmsHeader smsHeader = smsMessageBase.getUserDataHeader();
                if (smsHeader == null || smsHeader.portAddrs == null) {
                    Log.i(TAG, "addIntentFlagForKrKtCust smsHeader:" + smsHeader);
                } else {
                    int destPort = smsHeader.portAddrs.destPort;
                    Log.i(TAG, "addIntentFlagForKrKtCust destPort:" + destPort);
                    Log.i(TAG, "addIntentFlagForKrKtCust protocol:" + smsMessage.getProtocolIdentifier());
                    if (KRKT_PORT == destPort && KRKT_PID == smsMessage.getProtocolIdentifier()) {
                        intent.addFlags(32);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, " addIntentFlagForKrKtCust error ", e);
            }
        }
    }

    public static class SmsDispatcherProcessor {
        public static final String CALLING_PACKAGE_NAME_KEY = "calling_package_name";
        private static final String DTAG_RO_DEFAULT_SMSC = "+40766000510";
        private static final String TAG = "SmsDispatcherProcessor";
        private static String SMS_SEND_ACTION = "AUTO_REG_SMS_SEND";
        private static String SMS_DELIVERED_ACTION = "AUTO_REG_SMS_DELIVERED";
        private static final String[] DTAG_RO_MCCMNC = {"22603", "22606"};

        private SmsDispatcherProcessor() {
        }

        public static PackageInfo getCallerAppInfo(SMSDispatcher smsDispatcher, HashMap<String, Object> data, PackageInfo appInfoByUid) {
            String callingPkgName = (String) data.get(CALLING_PACKAGE_NAME_KEY);
            if (callingPkgName != null) {
                data.remove(CALLING_PACKAGE_NAME_KEY);
                try {
                    return smsDispatcher.mContext.getPackageManager().getPackageInfo(callingPkgName, 64);
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
            return appInfoByUid;
        }

        public static boolean shouldCheckSmsUsage(String packageName) {
            return ("com.android.mms".equals(packageName) || "com.android.settings".equals(packageName) || MiuiSmsControllerImpl.CT_PACKAGENAME_OLDER.equals(packageName)) ? false : true;
        }

        public static SmsMessage.SubmitPdu getSubmitPdu(String scAddress, String destAddr, byte[] data, boolean statusReportRequested, SmsMessageBase.SubmitPduBase pdu, int destPort, PendingIntent sentIntent, PendingIntent deliveryIntent) {
            if (destPort != 0 || !MiuiSmsControllerImpl.CT_AUTO_ADRESS.equals(destAddr) || deliveryIntent == null) {
                return null;
            }
            Log.d(TAG, "sentIntent action = " + sentIntent.getIntent().getAction());
            Log.d(TAG, "deliveryIntent action = " + deliveryIntent.getIntent().getAction());
            if (!SMS_SEND_ACTION.equals(sentIntent.getIntent().getAction()) || !SMS_DELIVERED_ACTION.equals(deliveryIntent.getIntent().getAction())) {
                return null;
            }
            Log.d(TAG, "get SubmitPdu for auto regist data.length = " + data.length + " bytes");
            if (data.length + 1 > 140) {
                Log.e(TAG, "data length is too long,SMS data.length = " + data.length + " bytes");
                return null;
            }
            SmsMessage.SubmitPdu ret = new SmsMessage.SubmitPdu();
            ByteArrayOutputStream bo = getSubmitPduHead(scAddress, destAddr, (byte) 1, statusReportRequested, ret);
            bo.write(4);
            bo.write(data.length);
            bo.write(data, 0, data.length);
            ret.encodedMessage = bo.toByteArray();
            return ret;
        }

        private static ByteArrayOutputStream getSubmitPduHead(String scAddress, String destinationAddress, byte mtiByte, boolean statusReportRequested, SmsMessage.SubmitPdu ret) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream(180);
            if (scAddress == null) {
                ret.encodedScAddress = null;
            } else {
                ret.encodedScAddress = PhoneNumberUtils.networkPortionToCalledPartyBCDWithLength(scAddress);
            }
            if (statusReportRequested) {
                mtiByte = (byte) (mtiByte | 32);
                Log.d(TAG, "SMS status report requested");
            }
            bo.write(mtiByte);
            bo.write(0);
            byte[] daBytes = PhoneNumberUtils.networkPortionToCalledPartyBCD(destinationAddress);
            if (daBytes == null) {
                return null;
            }
            bo.write(((daBytes.length - 1) * 2) - ((daBytes[daBytes.length - 1] & 240) != 240 ? 0 : 1));
            bo.write(daBytes, 0, daBytes.length);
            bo.write(0);
            return bo;
        }

        public static String reMapSmscNumber(Phone phone, String scAddr) {
            String mMccmnc = phone.getOperatorNumeric();
            Log.i(TAG, "scAddr = " + scAddr + " ; mMccmnc = " + mMccmnc);
            if (phone != null && !TextUtils.isEmpty(mMccmnc)) {
                for (String mccmnc : DTAG_RO_MCCMNC) {
                    if (mccmnc.equals(mMccmnc) && (TextUtils.isEmpty(scAddr) || "0766000510".equals(scAddr))) {
                        Log.i(TAG, "return default SMSC .");
                        return DTAG_RO_DEFAULT_SMSC;
                    }
                }
            }
            if (!TextUtils.isEmpty(scAddr)) {
                return scAddr;
            }
            String smsc = HyperOSCustFeatureResolve.getStringForPhoneId(phone.getPhoneId(), "config_default_smsc", scAddr);
            return smsc;
        }

        public static boolean carrierDisableSendSmsOverIms(Phone phone) {
            boolean config = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_disable_send_sms_over_ims");
            return config;
        }
    }
}
