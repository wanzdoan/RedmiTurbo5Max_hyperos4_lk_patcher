package com.mediatek.internal.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.InboundSmsTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SmsBroadcastUndelivered;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.mediatek.internal.telephony.util.MtkSmsCommonUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsBroadcastUndelivered extends SmsBroadcastUndelivered {
    private static final String[] PDU_PENDING_MESSAGE_PROJECTION = {"pdu", "sequence", "destination_port", "date", "reference_number", "count", "address", "_id", "message_body", "display_originating_addr", "sub_id"};
    private static final String TAG = "MtkSmsBroadcastUndelivered";
    private static MtkSmsBroadcastUndelivered instance;

    public static void initialize(Context context, GsmInboundSmsHandler gsmInboundSmsHandler, CdmaInboundSmsHandler cdmaInboundSmsHandler, FeatureFlags featureFlags) {
        if (instance == null) {
            Rlog.d(TAG, " call initialize");
            instance = new MtkSmsBroadcastUndelivered(context, gsmInboundSmsHandler, cdmaInboundSmsHandler, featureFlags);
        }
        if (gsmInboundSmsHandler != null) {
            gsmInboundSmsHandler.sendMessage(6);
        }
        if (cdmaInboundSmsHandler != null) {
            cdmaInboundSmsHandler.sendMessage(6);
        }
    }

    private MtkSmsBroadcastUndelivered(Context context, GsmInboundSmsHandler gsmInboundSmsHandler, CdmaInboundSmsHandler cdmaInboundSmsHandler, FeatureFlags featureFlags) {
        super(context, featureFlags);
    }

    public static void scanRawTable(Context context, long oldMessageTimestamp) throws Throwable {
        scanRawTable(context, false, oldMessageTimestamp);
        scanRawTable(context, true, oldMessageTimestamp);
    }

    /* JADX WARN: Code duplicated, block: B:74:0x01e7  */
    private static void scanRawTable(Context context, boolean isCurrentFormat3gpp2, long oldMessageTimestamp) throws Throwable {
        StringBuilder sb;
        isCurrentFormat3gpp2 = isCurrentFormat3gpp2;
        Rlog.d(TAG, "scanning raw table for undelivered messages");
        long startTime = System.nanoTime();
        ContentResolver contentResolver = context.getContentResolver();
        HashMap<SmsReferenceKey, Integer> multiPartReceivedCount = new HashMap<>(4);
        HashSet<SmsReferenceKey> oldMultiPartMessages = new HashSet<>(4);
        Cursor cursor = null;
        try {
            try {
                String nonDeleteWhere = "deleted = 0" + (isCurrentFormat3gpp2 ? MtkSmsCommonUtil.SQL_3GPP2_SMS : MtkSmsCommonUtil.SQL_3GPP_SMS);
                cursor = contentResolver.query(InboundSmsHandler.sRawUri, PDU_PENDING_MESSAGE_PROJECTION, nonDeleteWhere, null, null);
                if (cursor == null) {
                    Rlog.e(TAG, "error getting pending message cursor");
                    if (cursor != null) {
                        cursor.close();
                    }
                    Rlog.d(TAG, "finished scanning raw table in " + ((System.nanoTime() - startTime) / 1000000) + " ms");
                    return;
                }
                while (cursor.moveToNext()) {
                    try {
                        TelephonyComponentFactory telephonyComponentFactory = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName());
                        try {
                            try {
                                MtkInboundSmsTracker tracker = (MtkInboundSmsTracker) telephonyComponentFactory.makeInboundSmsTracker(context, cursor, isCurrentFormat3gpp2);
                                if (tracker.getMessageCount() == 1) {
                                    broadcastSms(tracker);
                                } else {
                                    SmsReferenceKey reference = new SmsReferenceKey(tracker);
                                    Integer receivedCount = multiPartReceivedCount.get(reference);
                                    if (receivedCount == null) {
                                        multiPartReceivedCount.put(reference, 1);
                                        if (tracker.getTimestamp() < oldMessageTimestamp) {
                                            oldMultiPartMessages.add(reference);
                                        }
                                    } else {
                                        int newCount = receivedCount.intValue() + 1;
                                        if (newCount == tracker.getMessageCount()) {
                                            Rlog.d(TAG, "found complete multi-part message");
                                            broadcastSms(tracker);
                                            oldMultiPartMessages.remove(reference);
                                        } else {
                                            multiPartReceivedCount.put(reference, Integer.valueOf(newCount));
                                        }
                                    }
                                }
                            } catch (IllegalArgumentException e) {
                                e = e;
                                Rlog.e(TAG, "error loading SmsTracker: " + e);
                            }
                        } catch (SQLException e2) {
                            e = e2;
                            Rlog.e(TAG, "error reading pending SMS messages", e);
                            if (cursor != null) {
                                cursor.close();
                            }
                            sb = new StringBuilder();
                            Rlog.d(TAG, sb.append("finished scanning raw table in ").append((System.nanoTime() - startTime) / 1000000).append(" ms").toString());
                        }
                    } catch (IllegalArgumentException e3) {
                        e = e3;
                    }
                }
                Phone phone = PhoneFactory.getPhone(0);
                for (Iterator<SmsReferenceKey> it = oldMultiPartMessages.iterator(); it.hasNext(); it = it) {
                    SmsReferenceKey message = it.next();
                    String where = MtkSmsCommonUtil.SELECT_BY_REFERENCE + (isCurrentFormat3gpp2 ? MtkSmsCommonUtil.SQL_3GPP2_SMS : MtkSmsCommonUtil.SQL_3GPP_SMS);
                    Phone phone2 = phone;
                    int rows = contentResolver.delete(InboundSmsHandler.sRawUriPermanentDelete, where, message.getDeleteWhereArgs());
                    if (rows == 0) {
                        Rlog.e(TAG, "No rows were deleted from raw table!");
                    } else {
                        Rlog.d(TAG, "Deleted " + rows + " rows from raw table for incomplete " + message.mMessageCount + " part message");
                    }
                    if (rows > 0 && phone2 != null) {
                        phone2.getSmsStats().onDroppedIncomingMultipartSms(message.mIs3gpp2, rows, message.mMessageCount, TelephonyManager.from(context).isEmergencyNumber(message.mAddress), 0);
                    }
                    phone = phone2;
                }
                if (cursor != null) {
                    cursor.close();
                }
                sb = new StringBuilder();
                Rlog.d(TAG, sb.append("finished scanning raw table in ").append((System.nanoTime() - startTime) / 1000000).append(" ms").toString());
            } catch (Throwable th) {
                th = th;
                if (0 != 0) {
                    cursor.close();
                }
                Rlog.d(TAG, "finished scanning raw table in " + ((System.nanoTime() - startTime) / 1000000) + " ms");
                throw th;
            }
        } catch (SQLException e4) {
            e = e4;
        } catch (Throwable th2) {
            th = th2;
            if (0 != 0) {
                cursor.close();
            }
            Rlog.d(TAG, "finished scanning raw table in " + ((System.nanoTime() - startTime) / 1000000) + " ms");
            throw th;
        }
    }

    private static void broadcastSms(InboundSmsTracker tracker) {
        int subId = tracker.getSubId();
        int phoneId = SubscriptionManagerService.getInstance().getPhoneId(subId);
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            Rlog.e(TAG, "broadcastSms: ignoring message; no phone found for subId " + subId);
            return;
        }
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null) {
            Rlog.e(TAG, "broadcastSms: ignoring message; no phone found for subId " + subId + " phoneId " + phoneId);
            return;
        }
        InboundSmsHandler handler = phone.getInboundSmsHandler(tracker.is3gpp2());
        if (handler != null) {
            handler.sendMessage(2, tracker);
        } else {
            Rlog.e(TAG, "null handler for " + tracker.getFormat() + " format, can't deliver.");
        }
    }

    private static class SmsReferenceKey {
        final String mAddress;
        final String mFormat;
        final boolean mIs3gpp2;
        final int mMessageCount;
        final String mQuery;
        final int mReferenceNumber;
        final long mSubId;

        SmsReferenceKey(MtkInboundSmsTracker tracker) {
            this.mAddress = tracker.getAddress();
            this.mReferenceNumber = tracker.getReferenceNumber();
            this.mMessageCount = tracker.getMessageCount();
            this.mQuery = tracker.getQueryForSegments();
            this.mIs3gpp2 = tracker.is3gpp2();
            this.mFormat = tracker.getFormat();
            this.mSubId = tracker.getSubId();
        }

        String[] getDeleteWhereArgs() {
            return new String[]{this.mAddress, Integer.toString(this.mReferenceNumber), Integer.toString(this.mMessageCount), Long.toString(this.mSubId)};
        }

        public int hashCode() {
            return (((((int) this.mSubId) * 63) + (this.mReferenceNumber * 31) + this.mMessageCount) * 31) + this.mAddress.hashCode();
        }

        public boolean equals(Object o) {
            if (!(o instanceof SmsReferenceKey)) {
                return false;
            }
            SmsReferenceKey other = (SmsReferenceKey) o;
            return other.mAddress.equals(this.mAddress) && other.mReferenceNumber == this.mReferenceNumber && other.mMessageCount == this.mMessageCount && other.mSubId == this.mSubId;
        }
    }
}
