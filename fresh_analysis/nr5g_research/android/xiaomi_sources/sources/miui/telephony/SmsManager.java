package miui.telephony;

import android.app.PendingIntent;
import android.telephony.SmsMessage;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class SmsManager {
    private static final int INVALID_SIM_SLOT_INDEX = -1;
    private int mSlotId;

    static class Holder {
        private static SmsManager sDefaultInstance;
        private static SmsManager[] sInstance = new SmsManager[TelephonyManager.getDefault().getPhoneCount()];

        Holder() {
        }

        static {
            sDefaultInstance = new SmsManager();
            for (int i = 0; i < sInstance.length; i++) {
                sInstance[i] = new SmsManager(i);
            }
        }
    }

    public static SmsManager getDefault() {
        return Holder.sDefaultInstance;
    }

    public static SmsManager getDefault(int slotId) {
        if (SubscriptionManager.isRealSlotId(slotId)) {
            return Holder.sInstance[slotId];
        }
        return getDefault();
    }

    private SmsManager() {
        this.mSlotId = -1;
    }

    private SmsManager(int slotId) {
        this.mSlotId = -1;
        if (SubscriptionManager.isValidSlotId(slotId)) {
            this.mSlotId = slotId;
        }
    }

    public ArrayList<String> divideMessage(String text) {
        return getSmsManager().divideMessage(text);
    }

    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        getSmsManager().sendMultipartTextMessage(destinationAddress, scAddress, parts, sentIntents, deliveryIntents);
    }

    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, int priority, boolean isExpectMore, int validityPeriod) {
        getSmsManager().sendMultipartTextMessage(destinationAddress, scAddress, parts, sentIntents, deliveryIntents);
    }

    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        getSmsManager().sendTextMessage(destinationAddress, scAddress, text, sentIntent, deliveryIntent);
    }

    public boolean copyMessageToIcc(byte[] smsc, byte[] pdu, int status) {
        return getSmsManager().copyMessageToIcc(smsc, pdu, status);
    }

    public boolean deleteMessageFromIcc(int messageIndex) {
        return getSmsManager().deleteMessageFromIcc(messageIndex);
    }

    public ArrayList<SmsMessage> getAllMessagesFromIcc() {
        return getSmsManager().getAllMessagesFromIcc();
    }

    private android.telephony.SmsManager getSmsManager() {
        if (this.mSlotId == -1) {
            return android.telephony.SmsManager.getDefault();
        }
        return android.telephony.SmsManager.getSmsManagerForSubscriptionId(SubscriptionManager.getDefault().getSubscriptionIdForSlot(this.mSlotId));
    }
}
