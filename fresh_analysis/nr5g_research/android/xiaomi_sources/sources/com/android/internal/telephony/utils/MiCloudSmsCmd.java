package com.android.internal.telephony.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class MiCloudSmsCmd {
    private static final String ACTIVATION_SMS_PREFIX = "AC/";
    private static final String ACTIVATION_SMS_PREFIX_2 = "ACT/";
    private static final String EXTRA_FIND_DEVICE_INTERCEPT_SMS_INTENT = "android.intent.extra.finddevice.intercept_sms_intent";
    private static final String EXTRA_FIND_DEVICE_V2_COMMAND = "android.intent.extra.finddevice.command";
    private static final String EXTRA_FIND_DEVICE_V2_COMMAND_TYPE = "android.intent.extra.finddevice.command_type";
    private static final String EXTRA_FIND_DEVICE_V2_COMMAND_TYPE_SMS = "sms";
    private static final String EXTRA_FROM_ADDRESS = "android.intent.extra.from_address";
    private static final String EXTRA_FROM_SLOT_ID = "android.intent.extra.from_slotId";
    private static final String EXTRA_FROM_SUB_ID = "android.intent.extra.from_subId";
    private static final String FIND_DEVICE_V2_RECEIVE_COMMAND_PERMISSION = "miui.cloud.finddevice.RECEIVE_COMMAND";
    private static final Intent INTENT_FIND_DEVICE_V2_CMD_RECEIVER = new Intent();
    private static final String SMS_CMD_HEADER = "mfc";
    private static final String SMS_CMD_HEADER_V1 = "mf";
    private static final String SMS_CMD_TAIL = "##";
    private static final String SMS_CMD_TAIL_V1 = "#";
    private static final String TAG = "MiCloudSmsCmd";
    public static final String TYPE_LOCATION = "l";
    private static boolean[] sHexChars;

    static {
        INTENT_FIND_DEVICE_V2_CMD_RECEIVER.setComponent(new ComponentName("com.xiaomi.finddevice", "com.xiaomi.finddevice.v2.command.CommandReceiver"));
        sHexChars = new boolean[123];
        for (char c = 'A'; c <= 'Z'; c = (char) (c + 1)) {
            sHexChars[c] = true;
        }
        for (char c2 = 'a'; c2 <= 'z'; c2 = (char) (c2 + 1)) {
            sHexChars[c2] = true;
        }
        for (char c3 = '0'; c3 <= '9'; c3 = (char) (c3 + 1)) {
            sHexChars[c3] = true;
        }
    }

    private MiCloudSmsCmd() {
    }

    public static boolean checkSmsCmd(Intent oriIntent, Context context, String address, String body, int slotId, int subId) {
        if (!TextUtils.isEmpty(body)) {
            if (body.indexOf(SMS_CMD_HEADER) >= 0 || body.indexOf(SMS_CMD_HEADER_V1) >= 0) {
                if (body.indexOf(SMS_CMD_TAIL) < 0 && body.indexOf(SMS_CMD_TAIL_V1) < 0) {
                    return false;
                }
                transferToFindDevice(oriIntent, context, address, body, slotId, subId);
                return true;
            }
            return false;
        }
        return false;
    }

    private static void transferToFindDevice(Intent oriIntent, Context context, String address, String body, int slotId, int subId) {
        Intent intent = new Intent(INTENT_FIND_DEVICE_V2_CMD_RECEIVER);
        intent.putExtra(EXTRA_FIND_DEVICE_V2_COMMAND_TYPE, EXTRA_FIND_DEVICE_V2_COMMAND_TYPE_SMS);
        intent.putExtra(EXTRA_FROM_ADDRESS, address);
        intent.putExtra(EXTRA_FROM_SLOT_ID, slotId);
        intent.putExtra(EXTRA_FROM_SUB_ID, subId);
        intent.putExtra(EXTRA_FIND_DEVICE_V2_COMMAND, body);
        if (oriIntent != null) {
            intent.putExtra(EXTRA_FIND_DEVICE_INTERCEPT_SMS_INTENT, oriIntent);
        }
        context.sendBroadcast(intent, FIND_DEVICE_V2_RECEIVE_COMMAND_PERMISSION);
    }

    public static boolean checkAndDispatchActivationSms(Context context, int simIndex, String address, String body) {
        int pos = body.indexOf(ACTIVATION_SMS_PREFIX);
        if (pos == -1) {
            return checkAndDispatchActivationSms2(context, simIndex, address, body);
        }
        Log.v(TAG, "checkAndDispatchActivationSms: The message looks like an activation");
        int vkey1Start = ACTIVATION_SMS_PREFIX.length() + pos;
        int colonStart = vkey1Start + 32;
        int msgIdStart = colonStart + 1;
        int msgIdEnd = msgIdStart + 11;
        if (body.length() < msgIdEnd) {
            Log.v(TAG, "checkAndDispatchActivationSms: length check failed, " + body.length() + " < " + msgIdEnd);
            return false;
        }
        if (!isStrHex(body, vkey1Start, colonStart)) {
            Log.v(TAG, "checkAndDispatchActivationSms: left hex check failed");
            return false;
        }
        if (body.charAt(colonStart) != ':') {
            Log.v(TAG, "checkAndDispatchActivationSms: colon check failed");
            return false;
        }
        if (!isStrHex(body, msgIdStart, msgIdEnd)) {
            Log.v(TAG, "checkAndDispatchActivationSms: right hex check failed");
            return false;
        }
        Log.v(TAG, "checkAndDispatchActivationSms: activation SMS acknowledged, broadcasting...");
        Intent intent = new Intent("com.xiaomi.action.ACTIVATION_SMS_RECEIVED");
        intent.putExtra("extra_sim_index", simIndex);
        intent.putExtra("extra_address", address);
        intent.putExtra("extra_msg_id", body.substring(msgIdStart, msgIdEnd));
        intent.putExtra("extra_vkey1", body.substring(vkey1Start, colonStart));
        intent.setPackage("com.xiaomi.simactivate.service");
        context.sendBroadcastAsUser(intent, getAllUser());
        return true;
    }

    private static boolean checkAndDispatchActivationSms2(Context context, int simIndex, String address, String body) {
        int pos = body.indexOf(ACTIVATION_SMS_PREFIX_2);
        if (pos == -1) {
            return false;
        }
        Log.v(TAG, "checkAndDispatchActivationSmsi2: The message looks like an activation");
        int keyStart = ACTIVATION_SMS_PREFIX_2.length() + pos;
        int colonStart = keyStart + 32;
        int msgIdStart = colonStart + 1;
        int msgIdEnd = msgIdStart + 11;
        if (body.length() < msgIdEnd) {
            Log.v(TAG, "checkAndDispatchActivationSms2: length check failed, " + body.length() + " < " + msgIdEnd);
            return false;
        }
        if (!isStrHex(body, keyStart, colonStart)) {
            Log.v(TAG, "checkAndDispatchActivationSms2: left hex check failed");
            return false;
        }
        if (body.charAt(colonStart) != ':') {
            Log.v(TAG, "checkAndDispatchActivationSms2: colon check failed");
            return false;
        }
        if (!isStrHex(body, msgIdStart, msgIdEnd)) {
            Log.v(TAG, "checkAndDispatchActivationSms2: right hex check failed");
            return false;
        }
        Log.v(TAG, "checkAndDispatchActivationSms2: activation SMS acknowledged, broadcasting...");
        Intent intent = new Intent("com.xiaomi.action.ACTIVATION_SMS_2_RECEIVED");
        intent.putExtra("extra_sim_index", simIndex);
        intent.putExtra("extra_address", address);
        intent.putExtra("extra_sms", body);
        intent.setPackage("com.xiaomi.simactivate.service");
        context.sendBroadcastAsUser(intent, getAllUser());
        return true;
    }

    private static UserHandle getAllUser() {
        try {
            return (UserHandle) UserHandle.class.getField("ALL").get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("will not reach here", e);
        } catch (NoSuchFieldException e2) {
            throw new IllegalStateException("will not reach here", e2);
        }
    }

    private static boolean isStrHex(String s, int begin, int end) {
        for (int i = begin; i < end; i++) {
            char ch = s.charAt(i);
            if (ch >= sHexChars.length || !sHexChars[ch]) {
                return false;
            }
        }
        return true;
    }
}
