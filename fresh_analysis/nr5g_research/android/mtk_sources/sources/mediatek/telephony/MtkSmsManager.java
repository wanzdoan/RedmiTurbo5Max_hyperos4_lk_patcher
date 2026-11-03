package mediatek.telephony;

import android.app.ActivityThread;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.internal.telephony.ISms;
import com.android.internal.telephony.SmsRawData;
import com.mediatek.internal.telephony.IMtkSms;
import com.mediatek.internal.telephony.MtkIccSmsStorageStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class MtkSmsManager {
    public static final byte ERROR_CODE_GENERIC_ERROR = 1;
    public static final byte ERROR_CODE_NO_ERROR = 0;
    public static final byte ERROR_CODE_NO_SUPPORT_SC_ADDR = 2;
    public static final String EXTRA_PARAMS_ENCODING_TYPE = "encoding_type";
    public static final String EXTRA_PARAMS_VALIDITY_PERIOD = "validity_period";
    public static final String GET_SC_ADDRESS_KEY_ADDRESS = "scAddress";
    public static final String GET_SC_ADDRESS_KEY_RESULT = "errorCode";
    public static final int RESULT_ERROR_INVALID_ADDRESS = 8;
    public static final int RESULT_ERROR_SIM_MEM_FULL = 7;
    public static final int RESULT_ERROR_SUCCESS = 0;
    private static final int SMS_PICK = 2;
    private static final String TAG = "MtkSmsManager";
    public static final int VALIDITY_PERIOD_MAX_DURATION = 255;
    public static final int VALIDITY_PERIOD_NO_DURATION = -1;
    public static final int VALIDITY_PERIOD_ONE_DAY = 167;
    public static final int VALIDITY_PERIOD_ONE_HOUR = 11;
    public static final int VALIDITY_PERIOD_SIX_HOURS = 71;
    public static final int VALIDITY_PERIOD_TWELVE_HOURS = 143;
    private int mSubId;
    private static final int DEFAULT_SUBSCRIPTION_ID = -1002;
    private static final MtkSmsManager sInstance = new MtkSmsManager(DEFAULT_SUBSCRIPTION_ID);
    private static final Object sLockObject = new Object();
    private static final Map<Integer, MtkSmsManager> sSubInstances = new ArrayMap();
    private static String DIALOG_TYPE_KEY = "dialog_type";

    public static MtkSmsManager getDefault() {
        return sInstance;
    }

    public static MtkSmsManager getSmsManagerForSubscriptionId(int subId) {
        MtkSmsManager smsManager;
        synchronized (sLockObject) {
            smsManager = sSubInstances.get(Integer.valueOf(subId));
            if (smsManager == null) {
                smsManager = new MtkSmsManager(subId);
                sSubInstances.put(Integer.valueOf(subId), smsManager);
            }
        }
        return smsManager;
    }

    private MtkSmsManager(int subId) {
        this.mSubId = subId;
    }

    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, true, null, 0L);
    }

    private void sendTextMessageInternal(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForCarrierApp, String attributionTag, long messageId) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        try {
            ISms iccISms = getISmsService();
            iccISms.sendTextForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), attributionTag, destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessageForCarrierApp, messageId);
        } catch (RemoteException e) {
            Rlog.d(TAG, "sendTextMessage, RemoteException!");
        }
    }

    public void sendTextMessageWithoutPersisting(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, false, null, 0L);
    }

    public ArrayList<MtkSmsMessage> getAllMessagesFromIcc() {
        Rlog.d(TAG, "getAllMessagesFromIcc");
        List<SmsRawData> records = null;
        try {
            ISms iccISms = getISmsService();
            if (iccISms != null) {
                records = iccISms.getAllMessagesFromIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName());
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "getAllMessagesFromIcc, RemoteException!");
        }
        return createMessageListFromRawRecords(records);
    }

    private ArrayList<MtkSmsMessage> createMessageListFromRawRecords(List<SmsRawData> records) {
        ArrayList<MtkSmsMessage> messages = new ArrayList<>();
        Rlog.d(TAG, "createMessageListFromRawRecords");
        if (records != null) {
            int count = records.size();
            for (int i = 0; i < count; i++) {
                SmsRawData data = records.get(i);
                if (data != null) {
                    Context context = ActivityThread.currentApplication().getApplicationContext();
                    int activePhone = TelephonyManager.from(context).getCurrentPhoneType(this.mSubId);
                    String phoneType = 2 == activePhone ? "3gpp2" : "3gpp";
                    Rlog.d(TAG, "phoneType: " + phoneType);
                    MtkSmsMessage sms = MtkSmsMessage.createFromEfRecord(i + 1, data.getBytes(), phoneType);
                    if (sms != null) {
                        messages.add(sms);
                    }
                }
            }
            Rlog.d(TAG, "actual sms count is " + count);
        } else {
            Rlog.d(TAG, "fail to parse SIM sms, records is null");
        }
        return messages;
    }

    public ArrayList<MtkSmsMessage> getAllMessagesFromIccEfByMode(int mode) throws RemoteException {
        Rlog.d(TAG, "getAllMessagesFromIcc, mode=" + mode);
        List<SmsRawData> records = null;
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms != null) {
                records = iccISms.getAllMessagesFromIccEfByModeForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), mode);
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException!");
        }
        int sz = 0;
        if (records != null) {
            sz = records.size();
        }
        for (int i = 0; i < sz; i++) {
            SmsRawData record = records.get(i);
            if (record != null) {
                byte[] data = record.getBytes();
                int index = i + 1;
                if ((data[0] & 255) == 3) {
                    Rlog.d(TAG, "index[" + index + "] is STATUS_ON_ICC_READ");
                    SmsManager manager = SmsManager.getSmsManagerForSubscriptionId(this.mSubId);
                    boolean ret = manager.updateMessageOnIcc(index, 1, data);
                    if (ret) {
                        Rlog.d(TAG, "update index[" + index + "] to STATUS_ON_ICC_READ");
                    } else {
                        Rlog.d(TAG, "fail to update message status");
                    }
                }
            }
        }
        return createMessageListFromRawRecordsByMode(getSubscriptionId(), records, mode);
    }

    public int copyTextMessageToIccCard(String scAddress, String address, List<String> text, int status, long timestamp) {
        Rlog.d(TAG, "copyTextMessageToIccCard");
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms == null) {
                return 1;
            }
            int result = iccISms.copyTextMessageToIccCardForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), scAddress, address, text, status, timestamp);
            return result;
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException!");
            return 1;
        }
    }

    public void sendDataMessage(String destinationAddress, String scAddress, short destinationPort, short originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        Rlog.d(TAG, "sendDataMessage");
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (!isValidParameters(destinationAddress, "send_data", sentIntent)) {
            return;
        }
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Invalid message data");
        }
        try {
            IMtkSms iccISms = getIMtkSmsServiceOrThrow();
            if (iccISms != null) {
                iccISms.sendDataWithOriginalPortForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, destinationPort & 65535, originalPort & 65535, data, sentIntent, deliveryIntent);
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException!");
        }
    }

    public void sendTextMessageWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        Rlog.d(TAG, "sendTextMessageWithEncodingType, encoding=" + encodingType);
        if (TextUtils.isEmpty(destAddr)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (!isValidParameters(destAddr, text, sentIntent)) {
            Rlog.d(TAG, "the parameters are invalid");
            return;
        }
        try {
            IMtkSms iccISms = getIMtkSmsServiceOrThrow();
            if (iccISms != null) {
                iccISms.sendTextWithEncodingTypeForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent, true);
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
        }
    }

    public void sendMultipartTextMessageWithEncodingType(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        PendingIntent sentIntent;
        PendingIntent deliveryIntent;
        Rlog.d(TAG, "sendMultipartTextMessageWithEncodingType, encoding=" + encodingType);
        if (TextUtils.isEmpty(destAddr)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (!isValidParameters(destAddr, parts, sentIntents)) {
            Rlog.d(TAG, "invalid parameters for multipart message");
            return;
        }
        if (parts != null && parts.size() > 1) {
            try {
                IMtkSms iccISms = getIMtkSmsServiceOrThrow();
                if (iccISms != null) {
                    try {
                        iccISms.sendMultipartTextWithEncodingTypeForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), destAddr, scAddr, parts, encodingType, sentIntents, deliveryIntents, true);
                        return;
                    } catch (RemoteException e) {
                    }
                } else {
                    return;
                }
            } catch (RemoteException e2) {
            }
            Rlog.d(TAG, "RemoteException");
            return;
        }
        if (sentIntents != null && sentIntents.size() > 0) {
            PendingIntent sentIntent2 = sentIntents.get(0);
            sentIntent = sentIntent2;
        } else {
            sentIntent = null;
        }
        Rlog.d(TAG, "get sentIntent: " + sentIntent);
        if (deliveryIntents != null && deliveryIntents.size() > 0) {
            PendingIntent deliveryIntent2 = deliveryIntents.get(0);
            deliveryIntent = deliveryIntent2;
        } else {
            deliveryIntent = null;
        }
        Rlog.d(TAG, "send single message");
        if (parts != null) {
            Rlog.d(TAG, "parts.size = " + parts.size());
        }
        String text = (parts == null || parts.size() == 0) ? "" : parts.get(0);
        Rlog.d(TAG, "pass encoding type " + encodingType);
        sendTextMessageWithEncodingType(destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent);
    }

    public ArrayList<String> divideMessage(String text, int encodingType) {
        Rlog.d(TAG, "divideMessage, encoding = " + encodingType);
        ArrayList<String> ret = MtkSmsMessage.fragmentText(text, encodingType);
        Rlog.d(TAG, "divideMessage: size = " + ret.size());
        return ret;
    }

    public MtkSimSmsInsertStatus insertTextMessageToIccCard(String scAddress, String address, List<String> text, int status, long timestamp) throws RemoteException {
        Rlog.d(TAG, "insertTextMessageToIccCard");
        MtkSimSmsInsertStatus ret = null;
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms != null) {
                ret = iccISms.insertTextMessageToIccCardForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), scAddress, address, text, status, timestamp);
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
        }
        Rlog.d(TAG, ret != null ? "insert Text " + ret.indexInIcc : "insert Text null");
        return ret;
    }

    public MtkSimSmsInsertStatus insertRawMessageToIccCard(int status, byte[] pdu, byte[] smsc) throws RemoteException {
        Rlog.d(TAG, "insertRawMessageToIccCard");
        MtkSimSmsInsertStatus ret = null;
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms != null) {
                try {
                    ret = iccISms.insertRawMessageToIccCardForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), status, pdu, smsc);
                } catch (RemoteException e) {
                    Rlog.d(TAG, "RemoteException");
                }
            }
        } catch (RemoteException e2) {
        }
        Rlog.d(TAG, ret != null ? "insert Raw " + ret.indexInIcc : "insert Raw null");
        return ret;
    }

    public void sendTextMessageWithExtraParams(String destAddr, String scAddr, String text, Bundle extraParams, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        Rlog.d(TAG, "sendTextMessageWithExtraParams");
        if (TextUtils.isEmpty(destAddr)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (!isValidParameters(destAddr, text, sentIntent)) {
            return;
        }
        if (extraParams == null) {
            Rlog.d(TAG, "bundle is null");
            return;
        }
        try {
            IMtkSms iccISms = getIMtkSmsServiceOrThrow();
            if (iccISms != null) {
                iccISms.sendTextWithExtraParamsForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), destAddr, scAddr, text, extraParams, sentIntent, deliveryIntent, true);
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
        }
    }

    public void sendMultipartTextMessageWithExtraParams(String destAddr, String scAddr, ArrayList<String> parts, Bundle extraParams, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        PendingIntent sentIntent;
        PendingIntent deliveryIntent;
        Rlog.d(TAG, "sendMultipartTextMessageWithExtraParams");
        if (TextUtils.isEmpty(destAddr)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (!isValidParameters(destAddr, parts, sentIntents)) {
            return;
        }
        if (extraParams == null) {
            Rlog.d(TAG, "bundle is null");
            return;
        }
        if (parts == null || parts.size() <= 1) {
            if (sentIntents != null && sentIntents.size() > 0) {
                PendingIntent sentIntent2 = sentIntents.get(0);
                sentIntent = sentIntent2;
            } else {
                sentIntent = null;
            }
            if (deliveryIntents != null && deliveryIntents.size() > 0) {
                PendingIntent deliveryIntent2 = deliveryIntents.get(0);
                deliveryIntent = deliveryIntent2;
            } else {
                deliveryIntent = null;
            }
            String text = (parts == null || parts.size() == 0) ? "" : parts.get(0);
            sendTextMessageWithExtraParams(destAddr, scAddr, text, extraParams, sentIntent, deliveryIntent);
            return;
        }
        try {
            IMtkSms iccISms = getIMtkSmsServiceOrThrow();
            if (iccISms != null) {
                try {
                    iccISms.sendMultipartTextWithExtraParamsForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), destAddr, scAddr, parts, extraParams, sentIntents, deliveryIntents, true);
                    return;
                } catch (RemoteException e) {
                }
            } else {
                return;
            }
        } catch (RemoteException e2) {
        }
        Rlog.d(TAG, "RemoteException");
    }

    public MtkSmsParameters getSmsParameters() {
        Rlog.d(TAG, "getSmsParameters");
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms == null) {
                return null;
            }
            return iccISms.getSmsParametersForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
            Rlog.d(TAG, "fail to get MtkSmsParameters");
            return null;
        }
    }

    public boolean setSmsParameters(MtkSmsParameters params) {
        Rlog.d(TAG, "setSmsParameters");
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms == null) {
                return false;
            }
            return iccISms.setSmsParametersForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), params);
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
            return false;
        }
    }

    public int copySmsToIcc(byte[] smsc, byte[] pdu, int status) throws RemoteException {
        int[] index;
        Rlog.d(TAG, "copySmsToIcc");
        MtkSimSmsInsertStatus smsStatus = insertRawMessageToIccCard(status, pdu, smsc);
        if (smsStatus == null || (index = smsStatus.getIndex()) == null || index.length <= 0) {
            return -1;
        }
        return index[0];
    }

    public boolean updateSmsOnSimReadStatus(int index, boolean read) throws RemoteException {
        Rlog.d(TAG, "updateSmsOnSimReadStatus");
        SmsRawData record = null;
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms != null) {
                record = iccISms.getMessageFromIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), index);
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
        }
        if (record != null) {
            byte[] rawData = record.getBytes();
            int status = rawData[0] & 255;
            Rlog.d(TAG, "sms status is " + status);
            if (status != 3 && status != 1) {
                Rlog.d(TAG, "non-delivery sms " + status);
                return false;
            }
            if ((status == 3 && !read) || (status == 1 && read)) {
                Rlog.d(TAG, "no need to update status");
                return true;
            }
            Rlog.d(TAG, "update sms status as " + read);
            if (read) {
            }
            SmsManager manager = SmsManager.getSmsManagerForSubscriptionId(this.mSubId);
            boolean success = manager.updateMessageOnIcc(index, 1, rawData);
            return success;
        }
        Rlog.d(TAG, "record is null");
        return false;
    }

    public void setSmsMemoryStatus(boolean status) {
        Rlog.d(TAG, "setSmsMemoryStatus");
        try {
            IMtkSms iccISms = getIMtkSmsServiceOrThrow();
            if (iccISms != null) {
                iccISms.setSmsMemoryStatusForSubscriber(getSubscriptionId(), status);
            }
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
        }
    }

    public MtkIccSmsStorageStatus getSmsSimMemoryStatus() {
        Rlog.d(TAG, "getSmsSimMemoryStatus");
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms != null) {
                return iccISms.getSmsSimMemoryStatusForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName());
            }
            return null;
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
            return null;
        }
    }

    private static boolean isValidParameters(String destinationAddress, String text, PendingIntent sentIntent) {
        ArrayList<PendingIntent> sentIntents = new ArrayList<>();
        ArrayList<String> parts = new ArrayList<>();
        sentIntents.add(sentIntent);
        parts.add(text);
        return isValidParameters(destinationAddress, parts, sentIntents);
    }

    private static boolean isValidParameters(String destinationAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents) {
        if (parts == null || parts.size() == 0) {
            return true;
        }
        if (!isValidSmsDestinationAddress(destinationAddress)) {
            for (int i = 0; i < sentIntents.size(); i++) {
                PendingIntent sentIntent = sentIntents.get(i);
                if (sentIntent != null) {
                    try {
                        sentIntent.send(1);
                    } catch (PendingIntent.CanceledException e) {
                    }
                }
            }
            Rlog.d(TAG, "Invalid destinationAddress");
            return false;
        }
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (parts != null && parts.size() >= 1) {
            return true;
        }
        throw new IllegalArgumentException("Invalid message body");
    }

    private static boolean isValidSmsDestinationAddress(String da) {
        String encodeAddress = PhoneNumberUtils.extractNetworkPortion(da);
        if (encodeAddress == null) {
            return true;
        }
        return true ^ encodeAddress.isEmpty();
    }

    private static ArrayList<MtkSmsMessage> createMessageListFromRawRecordsByMode(int subId, List<SmsRawData> records, int mode) {
        MtkSmsMessage singleSms;
        Rlog.d(TAG, "createMessageListFromRawRecordsByMode");
        ArrayList<MtkSmsMessage> msg = null;
        if (records != null) {
            int count = records.size();
            msg = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                SmsRawData data = records.get(i);
                if (data != null && (singleSms = createFromEfRecordByMode(subId, i + 1, data.getBytes(), mode)) != null) {
                    msg.add(singleSms);
                }
            }
            Rlog.d(TAG, "actual sms count is " + msg.size());
        } else {
            Rlog.d(TAG, "fail to parse SIM sms, records is null");
        }
        return msg;
    }

    private static MtkSmsMessage createFromEfRecordByMode(int subId, int index, byte[] data, int mode) {
        MtkSmsMessage sms = null;
        if (mode != 2) {
            sms = MtkSmsMessage.createFromEfRecord(index, data, "3gpp");
        }
        if (sms != null) {
            sms.setSubId(subId);
        }
        return sms;
    }

    public int getSubscriptionId() {
        int subId = this.mSubId == DEFAULT_SUBSCRIPTION_ID ? SmsManager.getDefaultSmsSubscriptionId() : this.mSubId;
        Context context = ActivityThread.currentApplication().getApplicationContext();
        try {
            ISms iccISms = getISmsService();
            if (iccISms != null) {
                iccISms.isSmsSimPickActivityNeeded(subId);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "Exception in getSubscriptionId");
        }
        if (0 != 0) {
            Rlog.d(TAG, "getSubscriptionId isSmsSimPickActivityNeeded is true");
            Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.sim.SimDialogActivity");
            intent.addFlags(268435456);
            intent.putExtra(DIALOG_TYPE_KEY, 2);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                Rlog.e(TAG, "Unable to launch Settings application.");
            }
        }
        return subId;
    }

    private static IMtkSms getIMtkSmsServiceOrThrow() {
        IMtkSms iccISms = getIMtkSmsService();
        if (iccISms == null) {
            throw new UnsupportedOperationException("SmsEx is not supported");
        }
        return iccISms;
    }

    private static IMtkSms getIMtkSmsService() {
        return IMtkSms.Stub.asInterface(ServiceManager.getService("imtksms"));
    }

    private static ISms getISmsServiceOrThrow() {
        ISms iccISms = getISmsService();
        if (iccISms == null) {
            throw new UnsupportedOperationException("Sms is not supported");
        }
        return iccISms;
    }

    private static ISms getISmsService() {
        return ISms.Stub.asInterface(ServiceManager.getService("isms"));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean queryCellBroadcastSmsActivation() {
        String str = TAG;
        Rlog.d(TAG, "queryCellBroadcastSmsActivation");
        Rlog.d(TAG, "subId=" + getSubscriptionId());
        boolean z = false;
        try {
            IMtkSms iMtkSmsService = getIMtkSmsService();
            if (iMtkSmsService != null) {
                boolean zQueryCellBroadcastSmsActivationForSubscriber = iMtkSmsService.queryCellBroadcastSmsActivationForSubscriber(getSubscriptionId());
                z = zQueryCellBroadcastSmsActivationForSubscriber;
                str = zQueryCellBroadcastSmsActivationForSubscriber;
            } else {
                Rlog.d(TAG, "fail to get sms service");
                z = false;
                str = str;
            }
        } catch (RemoteException e) {
            Rlog.d(str, "RemoteException!");
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v7, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v9 */
    public boolean activateCellBroadcastSms(boolean z) {
        String str = "activateCellBroadcastSms activate : " + z + ", sub = " + getSubscriptionId();
        boolean zActivateCellBroadcastSmsForSubscriber = TAG;
        Rlog.d(TAG, str);
        try {
            IMtkSms iMtkSmsService = getIMtkSmsService();
            if (iMtkSmsService == null) {
                Rlog.d(TAG, "fail to get sms service, maybe phone is initializing");
                zActivateCellBroadcastSmsForSubscriber = 0;
            } else {
                zActivateCellBroadcastSmsForSubscriber = iMtkSmsService.activateCellBroadcastSmsForSubscriber(getSubscriptionId(), z);
            }
            return zActivateCellBroadcastSmsForSubscriber;
        } catch (RemoteException e) {
            Rlog.d(zActivateCellBroadcastSmsForSubscriber, "fail to activate CB");
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean removeCellBroadcastMsg(int i, int i2) {
        String str = "RemoveCellBroadcastMsg, subId=" + getSubscriptionId();
        String str2 = TAG;
        Rlog.d(TAG, str);
        boolean z = false;
        try {
            IMtkSms iMtkSmsService = getIMtkSmsService();
            if (iMtkSmsService != null) {
                boolean zRemoveCellBroadcastMsgForSubscriber = iMtkSmsService.removeCellBroadcastMsgForSubscriber(getSubscriptionId(), i, i2);
                z = zRemoveCellBroadcastMsgForSubscriber;
                str2 = zRemoveCellBroadcastMsgForSubscriber;
            } else {
                Rlog.d(TAG, "fail to get sms service");
                z = false;
                str2 = str2;
            }
        } catch (RemoteException e) {
            Rlog.d(str2, "RemoveCellBroadcastMsg, RemoteException!");
        }
        return z;
    }

    public String getCellBroadcastRanges() {
        String str = "getCellBroadcastRanges, subId=" + getSubscriptionId();
        String cellBroadcastRangesForSubscriber = TAG;
        Rlog.d(TAG, str);
        String configs = "";
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms != null) {
                cellBroadcastRangesForSubscriber = iccISms.getCellBroadcastRangesForSubscriber(getSubscriptionId());
                configs = cellBroadcastRangesForSubscriber;
            } else {
                Rlog.d(TAG, "fail to get sms service");
            }
        } catch (RemoteException e) {
            Rlog.d(cellBroadcastRangesForSubscriber, "RemoteException");
        }
        return configs;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setCellBroadcastLang(String str) {
        String str2 = "setCellBroadcastLang, subId=" + getSubscriptionId();
        String str3 = TAG;
        Rlog.d(TAG, str2);
        boolean z = false;
        z = false;
        try {
            IMtkSms iMtkSmsService = getIMtkSmsService();
            if (iMtkSmsService != null) {
                boolean cellBroadcastLangsForSubscriber = iMtkSmsService.setCellBroadcastLangsForSubscriber(getSubscriptionId(), str);
                z = cellBroadcastLangsForSubscriber;
                str3 = cellBroadcastLangsForSubscriber;
            } else {
                Rlog.d(TAG, "fail to get sms service");
                str3 = str3;
            }
        } catch (RemoteException e) {
            Rlog.d(str3, "RemoteException");
        }
        return z;
    }

    public String getCellBroadcastLang() {
        String str = "getCellBroadcastLang, subId=" + getSubscriptionId();
        String cellBroadcastLangsForSubscriber = TAG;
        Rlog.d(TAG, str);
        String langs = "";
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms != null) {
                cellBroadcastLangsForSubscriber = iccISms.getCellBroadcastLangsForSubscriber(getSubscriptionId());
                langs = cellBroadcastLangsForSubscriber;
            } else {
                Rlog.d(TAG, "fail to get sms service");
            }
        } catch (RemoteException e) {
            Rlog.d(cellBroadcastLangsForSubscriber, "RemoteException");
        }
        return langs;
    }

    public boolean setEtwsConfig(int mode) {
        Rlog.d(TAG, "setEtwsConfig, mode=" + mode);
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms == null) {
                return false;
            }
            boolean ret = iccISms.setEtwsConfigForSubscriber(getSubscriptionId(), mode);
            return ret;
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
            return false;
        }
    }

    public String getScAddress() {
        Rlog.d(TAG, "getScAddress");
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms == null) {
                return null;
            }
            String address = iccISms.getScAddressForSubscriber(getSubscriptionId());
            return address;
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
            return null;
        }
    }

    public Bundle getScAddressWithErroCode() {
        Rlog.d(TAG, "getScAddressWithErroCode");
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms == null) {
                return null;
            }
            Bundle response = iccISms.getScAddressWithErrorCodeForSubscriber(getSubscriptionId());
            return response;
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
            return null;
        }
    }

    public boolean setScAddress(String address) {
        Rlog.d(TAG, "setScAddress");
        try {
            IMtkSms iccISms = getIMtkSmsService();
            if (iccISms == null) {
                return false;
            }
            boolean ret = iccISms.setScAddressForSubscriber(getSubscriptionId(), address);
            return ret;
        } catch (RemoteException e) {
            Rlog.d(TAG, "RemoteException");
            return false;
        }
    }

    public boolean isImsSmsSupported() {
        boolean boSupported = false;
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boSupported = iccISms.isImsSmsSupportedForSubscriber(getSubscriptionId());
            Rlog.d(TAG, "isImsSmsSupported " + boSupported);
            return boSupported;
        } catch (RemoteException e) {
            return boSupported;
        }
    }

    public String getImsSmsFormat() {
        String format = "unknown";
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return "unknown";
            }
            format = iccISms.getImsSmsFormatForSubscriber(getSubscriptionId());
            Rlog.d(TAG, "getImsSmsFormat " + format);
            return format;
        } catch (RemoteException e) {
            return format;
        }
    }

    public ArrayList<String> divideMessage(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }
        return MtkSmsMessage.fragmentText(text);
    }

    public void sendDataMessage(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Invalid message data");
        }
        try {
            ISms iccISms = getISmsServiceOrThrow();
            iccISms.sendDataForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), (String) null, destinationAddress, scAddress, destinationPort & 65535, data, sentIntent, deliveryIntent);
        } catch (RemoteException e) {
        }
    }

    public static boolean checkSimPickActivityNeeded(boolean needed) {
        return false;
    }
}
