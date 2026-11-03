package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.analytics.TelephonyAnalytics;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.telephony.Rlog;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SmsBroadcastUndelivered {
    protected static final boolean DBG = true;
    protected static final long DEFAULT_PARTIAL_SEGMENT_EXPIRE_AGE = 604800000;
    private static SmsBroadcastUndelivered instance;
    private final BroadcastReceiver mBroadcastReceiver;
    private final FeatureFlags mFeatureFlags;
    protected final ContentResolver mResolver;
    private final UserManager mUserManager;
    private static final String[] PDU_PENDING_MESSAGE_PROJECTION = {"pdu", "sequence", "destination_port", "date", "reference_number", "count", "address", "_id", "message_body", "display_originating_addr", "sub_id"};
    static final Map PDU_PENDING_MESSAGE_PROJECTION_INDEX_MAPPING = Map.ofEntries(Map.entry(0, 0), Map.entry(1, 1), Map.entry(2, 2), Map.entry(3, 3), Map.entry(4, 4), Map.entry(5, 5), Map.entry(6, 6), Map.entry(7, 7), Map.entry(8, 8), Map.entry(9, 9), Map.entry(11, 10));
    private static Class sMtkSmsBroadcastUndelivered = getMtkSmsBroadcastUndelivered();

    private class ScanRawTableThread extends Thread {
        private final Context context;

        private ScanRawTableThread(Context context) {
            this.context = context;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            SmsBroadcastUndelivered.scanRawTable(this.context, System.currentTimeMillis() - SmsBroadcastUndelivered.this.getUndeliveredSmsExpirationTime(this.context));
            InboundSmsHandler.cancelNewMessageNotification(this.context);
        }
    }

    public static void initialize(Context context, GsmInboundSmsHandler gsmInboundSmsHandler, CdmaInboundSmsHandler cdmaInboundSmsHandler, FeatureFlags featureFlags) {
        if (instance == null) {
            instance = new SmsBroadcastUndelivered(context, featureFlags);
        }
        if (gsmInboundSmsHandler != null) {
            gsmInboundSmsHandler.sendMessage(6);
        }
        if (cdmaInboundSmsHandler != null) {
            cdmaInboundSmsHandler.sendMessage(6);
        }
    }

    protected SmsBroadcastUndelivered(Context context, FeatureFlags featureFlags) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.SmsBroadcastUndelivered.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Rlog.d("SmsBroadcastUndelivered", "Received broadcast " + intent.getAction());
                if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) && intent.getIntExtra("android.intent.extra.user_handle", -10000) == SmsBroadcastUndelivered.this.getMainUser().getIdentifier()) {
                    new ScanRawTableThread(context2).start();
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mResolver = context.getContentResolver();
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        this.mUserManager = userManager;
        this.mFeatureFlags = featureFlags;
        UserHandle mainUser = getMainUser();
        if (userManager.isUserUnlocked(mainUser)) {
            new ScanRawTableThread(context).start();
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        context.registerReceiverAsUser(broadcastReceiver, mainUser, intentFilter, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserHandle getMainUser() {
        UserHandle mainUser = this.mUserManager.getMainUser();
        return mainUser != null ? mainUser : UserHandle.SYSTEM;
    }

    /* JADX WARN: Code duplicated, block: B:66:0x01c6  */
    static void scanRawTable(Context context, long j) {
        Cursor cursor;
        StringBuilder sb;
        TelephonyAnalytics.SmsMmsAnalytics smsMmsAnalytics;
        Class cls = sMtkSmsBroadcastUndelivered;
        if (cls != null) {
            try {
                Method declaredMethod = cls.getDeclaredMethod("scanRawTable", Context.class, Long.TYPE);
                Object[] objArr = {context, Long.valueOf(j)};
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(null, objArr);
                return;
            } catch (Exception unused) {
                Rlog.e("SmsBroadcastUndelivered", "No MtkSmsBroadcastUndelivered! Used AOSP for instead!");
            }
        }
        Rlog.d("SmsBroadcastUndelivered", "scanning raw table for undelivered messages");
        long jNanoTime = System.nanoTime();
        ContentResolver contentResolver = context.getContentResolver();
        HashMap map = new HashMap(4);
        HashSet<SmsReferenceKey> hashSet = new HashSet(4);
        try {
            Cursor cursorQuery = contentResolver.query(InboundSmsHandler.sRawUri, PDU_PENDING_MESSAGE_PROJECTION, "deleted = 0", null, null);
            try {
                if (cursorQuery == null) {
                    Rlog.e("SmsBroadcastUndelivered", "error getting pending message cursor");
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    Rlog.d("SmsBroadcastUndelivered", "finished scanning raw table in " + ((System.nanoTime() - jNanoTime) / TimeUtils.NANOS_PER_MS) + " ms");
                    return;
                }
                boolean zIsCurrentFormat3gpp2 = InboundSmsHandler.isCurrentFormat3gpp2();
                while (cursorQuery.moveToNext()) {
                    try {
                        InboundSmsTracker inboundSmsTrackerMakeInboundSmsTracker = TelephonyComponentFactory.getInstance().inject(InboundSmsTracker.class.getName()).makeInboundSmsTracker(context, cursorQuery, zIsCurrentFormat3gpp2);
                        if (inboundSmsTrackerMakeInboundSmsTracker.getMessageCount() == 1) {
                            broadcastSms(inboundSmsTrackerMakeInboundSmsTracker);
                        } else {
                            SmsReferenceKey smsReferenceKey = new SmsReferenceKey(inboundSmsTrackerMakeInboundSmsTracker);
                            Integer num = (Integer) map.get(smsReferenceKey);
                            if (num == null) {
                                map.put(smsReferenceKey, 1);
                                if (inboundSmsTrackerMakeInboundSmsTracker.getTimestamp() < j) {
                                    hashSet.add(smsReferenceKey);
                                }
                            } else {
                                int iIntValue = num.intValue() + 1;
                                if (iIntValue == inboundSmsTrackerMakeInboundSmsTracker.getMessageCount()) {
                                    Rlog.d("SmsBroadcastUndelivered", "found complete multi-part message");
                                    broadcastSms(inboundSmsTrackerMakeInboundSmsTracker);
                                    hashSet.remove(smsReferenceKey);
                                } else {
                                    map.put(smsReferenceKey, Integer.valueOf(iIntValue));
                                }
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        Rlog.e("SmsBroadcastUndelivered", "error loading SmsTracker: " + e);
                    }
                }
                Phone phone = PhoneFactory.getPhone(0);
                for (SmsReferenceKey smsReferenceKey2 : hashSet) {
                    int iDelete = contentResolver.delete(InboundSmsHandler.sRawUriPermanentDelete, smsReferenceKey2.getDeleteWhere(), smsReferenceKey2.getDeleteWhereArgs());
                    if (iDelete == 0) {
                        Rlog.e("SmsBroadcastUndelivered", "No rows were deleted from raw table!");
                    } else {
                        Rlog.d("SmsBroadcastUndelivered", "Deleted " + iDelete + " rows from raw table for incomplete " + smsReferenceKey2.mMessageCount + " part message");
                    }
                    if (iDelete > 0 && phone != null) {
                        phone.getSmsStats().onDroppedIncomingMultipartSms(smsReferenceKey2.mIs3gpp2, iDelete, smsReferenceKey2.mMessageCount, TelephonyManager.from(context).isEmergencyNumber(smsReferenceKey2.mAddress), 0);
                        TelephonyAnalytics telephonyAnalytics = phone.getTelephonyAnalytics();
                        if (telephonyAnalytics != null && (smsMmsAnalytics = telephonyAnalytics.getSmsMmsAnalytics()) != null) {
                            smsMmsAnalytics.onDroppedIncomingMultipartSms();
                        }
                    }
                }
                cursorQuery.close();
                sb = new StringBuilder("finished scanning raw table in ");
                sb.append((System.nanoTime() - jNanoTime) / TimeUtils.NANOS_PER_MS);
                sb.append(" ms");
                Rlog.d("SmsBroadcastUndelivered", sb.toString());
            } catch (SQLException e2) {
                e = e2;
                cursor = cursorQuery;
                try {
                    Rlog.e("SmsBroadcastUndelivered", "error reading pending SMS messages", e);
                    if (cursor != null) {
                        cursor.close();
                    }
                    sb = new StringBuilder("finished scanning raw table in ");
                } catch (Throwable th) {
                    th = th;
                    if (cursor != null) {
                        cursor.close();
                    }
                    Rlog.d("SmsBroadcastUndelivered", "finished scanning raw table in " + ((System.nanoTime() - jNanoTime) / TimeUtils.NANOS_PER_MS) + " ms");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = cursorQuery;
                if (cursor != null) {
                    cursor.close();
                }
                Rlog.d("SmsBroadcastUndelivered", "finished scanning raw table in " + ((System.nanoTime() - jNanoTime) / TimeUtils.NANOS_PER_MS) + " ms");
                throw th;
            }
        } catch (SQLException e3) {
            e = e3;
            cursor = null;
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
        }
    }

    private static void broadcastSms(InboundSmsTracker inboundSmsTracker) {
        int subId = inboundSmsTracker.getSubId();
        int phoneId = SubscriptionManagerService.getInstance().getPhoneId(subId);
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            Rlog.e("SmsBroadcastUndelivered", "broadcastSms: ignoring message; no phone found for subId " + subId);
            return;
        }
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null) {
            Rlog.e("SmsBroadcastUndelivered", "broadcastSms: ignoring message; no phone found for subId " + subId + " phoneId " + phoneId);
            return;
        }
        InboundSmsHandler inboundSmsHandler = phone.getInboundSmsHandler(inboundSmsTracker.is3gpp2());
        if (inboundSmsHandler != null) {
            inboundSmsHandler.sendMessage(2, inboundSmsTracker);
            return;
        }
        Rlog.e("SmsBroadcastUndelivered", "null handler for " + inboundSmsTracker.getFormat() + " format, can't deliver.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getUndeliveredSmsExpirationTime(Context context) {
        int defaultSmsSubscriptionId = SubscriptionManager.getDefaultSmsSubscriptionId();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(defaultSmsSubscriptionId) : null;
        return configForSubId != null ? configForSubId.getLong("undelivered_sms_message_expiration_time", DEFAULT_PARTIAL_SEGMENT_EXPIRE_AGE) : DEFAULT_PARTIAL_SEGMENT_EXPIRE_AGE;
    }

    private static Class getMtkSmsBroadcastUndelivered() {
        if (!SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
            return null;
        }
        try {
            return Class.forName("com.mediatek.internal.telephony.MtkSmsBroadcastUndelivered");
        } catch (Exception unused) {
            Rlog.e("SmsBroadcastUndelivered", "No MtkSmsBroadcastUndeliveredClass! Used AOSP for instead!");
            return null;
        }
    }

    private static class SmsReferenceKey {
        final String mAddress;
        final String mFormat;
        final boolean mIs3gpp2;
        final int mMessageCount;
        final String mQuery;
        final int mReferenceNumber;

        SmsReferenceKey(InboundSmsTracker inboundSmsTracker) {
            this.mAddress = inboundSmsTracker.getAddress();
            this.mReferenceNumber = inboundSmsTracker.getReferenceNumber();
            this.mMessageCount = inboundSmsTracker.getMessageCount();
            this.mQuery = inboundSmsTracker.getQueryForSegments();
            this.mIs3gpp2 = inboundSmsTracker.is3gpp2();
            this.mFormat = inboundSmsTracker.getFormat();
        }

        String[] getDeleteWhereArgs() {
            return new String[]{this.mAddress, Integer.toString(this.mReferenceNumber), Integer.toString(this.mMessageCount)};
        }

        String getDeleteWhere() {
            return this.mQuery;
        }

        public int hashCode() {
            return (((this.mReferenceNumber * 31) + this.mMessageCount) * 31) + this.mAddress.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof SmsReferenceKey) {
                SmsReferenceKey smsReferenceKey = (SmsReferenceKey) obj;
                if (smsReferenceKey.mAddress.equals(this.mAddress) && smsReferenceKey.mReferenceNumber == this.mReferenceNumber && smsReferenceKey.mMessageCount == this.mMessageCount) {
                    return true;
                }
            }
            return false;
        }
    }
}
