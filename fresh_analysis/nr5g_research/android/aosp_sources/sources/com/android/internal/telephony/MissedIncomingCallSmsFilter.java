package com.android.internal.telephony;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/* JADX INFO: loaded from: classes.dex */
public class MissedIncomingCallSmsFilter {
    private static final ComponentName PSTN_CONNECTION_SERVICE_COMPONENT = new ComponentName("com.android.phone", "com.android.services.telephony.TelephonyConnectionService");
    private static final String TAG = "MissedIncomingCallSmsFilter";
    private PersistableBundle mCarrierConfig;
    private final Phone mPhone;

    public MissedIncomingCallSmsFilter(Phone phone) {
        this.mPhone = phone;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager != null) {
            this.mCarrierConfig = carrierConfigManager.getConfigForSubId(phone.getSubId());
        }
    }

    public boolean filter(byte[][] bArr, String str) {
        PersistableBundle persistableBundle;
        String[] stringArray;
        SmsMessage smsMessageCreateFromPdu;
        if (bArr.length != 1 || (persistableBundle = this.mCarrierConfig) == null || (stringArray = persistableBundle.getStringArray("missed_incoming_call_sms_originator_string_array")) == null || (smsMessageCreateFromPdu = SmsMessage.createFromPdu(bArr[0], str)) == null || TextUtils.isEmpty(smsMessageCreateFromPdu.getOriginatingAddress()) || !Arrays.asList(stringArray).contains(smsMessageCreateFromPdu.getOriginatingAddress())) {
            return false;
        }
        return processSms(smsMessageCreateFromPdu);
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [java.time.ZonedDateTime] */
    private long getEpochTime(String str, String str2, String str3, String str4, String str5) {
        LocalDateTime localDateTime;
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        if (TextUtils.isEmpty(str)) {
            str = Integer.toString(localDateTimeNow.getYear());
        }
        do {
            localDateTime = LocalDateTime.parse(str + str2 + str3 + str4 + str5, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            str = Integer.toString(Integer.parseInt(str) + (-1));
        } while (localDateTime.isAfter(localDateTimeNow));
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /* JADX WARN: Code duplicated, block: B:38:0x0080 A[Catch: Exception -> 0x008c, TRY_LEAVE, TryCatch #3 {Exception -> 0x008c, blocks: (B:36:0x0078, B:38:0x0080), top: B:63:0x0078 }] */
    /* JADX WARN: Code duplicated, block: B:63:0x0078 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private boolean processSms(SmsMessage smsMessage) {
        String strGroup;
        String strGroup2;
        String strGroup3;
        long jCurrentTimeMillis;
        String strGroup4;
        String str;
        String str2;
        String str3;
        String strGroup5;
        String[] stringArray = this.mCarrierConfig.getStringArray("missed_incoming_call_sms_pattern_string_array");
        boolean z = false;
        if (stringArray == null || stringArray.length == 0) {
            Rlog.w(TAG, "Missed incoming call SMS pattern is not configured!");
            return false;
        }
        String[] strArrSplitCalls = splitCalls(smsMessage.getMessageBody());
        if (strArrSplitCalls != null && strArrSplitCalls.length > 0) {
            boolean z2 = false;
            for (String str4 : strArrSplitCalls) {
                for (String str5 : stringArray) {
                    try {
                        Matcher matcher = Pattern.compile(str5, 33).matcher(str4);
                        if (matcher.find()) {
                            String strGroup6 = null;
                            try {
                                strGroup = matcher.group("month");
                                try {
                                    try {
                                        strGroup2 = matcher.group("day");
                                        try {
                                            strGroup3 = matcher.group("hour");
                                            try {
                                                strGroup4 = matcher.group("minute");
                                                jCurrentTimeMillis = 0;
                                            } catch (IllegalArgumentException unused) {
                                                jCurrentTimeMillis = System.currentTimeMillis();
                                                strGroup4 = null;
                                            }
                                        } catch (IllegalArgumentException unused2) {
                                            strGroup3 = null;
                                        }
                                    } catch (IllegalArgumentException unused3) {
                                        strGroup2 = null;
                                        strGroup3 = strGroup2;
                                        jCurrentTimeMillis = System.currentTimeMillis();
                                        strGroup4 = null;
                                        str = strGroup3;
                                        str2 = strGroup2;
                                        str3 = strGroup;
                                        strGroup5 = matcher.group("year");
                                        if (jCurrentTimeMillis == 0) {
                                            try {
                                                jCurrentTimeMillis = getEpochTime(strGroup5, str3, str2, str, strGroup4);
                                                if (jCurrentTimeMillis == 0) {
                                                    Rlog.e(TAG, "Can't get the time. Use the current time.");
                                                    jCurrentTimeMillis = System.currentTimeMillis();
                                                }
                                            } catch (Exception unused4) {
                                                Rlog.e(TAG, "Can't get the time for missed incoming call");
                                            }
                                        }
                                        long j = jCurrentTimeMillis;
                                        strGroup6 = matcher.group("callerId");
                                        createMissedIncomingCallEvent(j, strGroup6);
                                        z2 = true;
                                        break;
                                    }
                                    strGroup5 = matcher.group("year");
                                } catch (IllegalArgumentException unused5) {
                                    strGroup5 = null;
                                }
                            } catch (IllegalArgumentException unused6) {
                                strGroup = null;
                                strGroup2 = null;
                            }
                            str = strGroup3;
                            str2 = strGroup2;
                            str3 = strGroup;
                            if (jCurrentTimeMillis == 0) {
                                jCurrentTimeMillis = getEpochTime(strGroup5, str3, str2, str, strGroup4);
                                if (jCurrentTimeMillis == 0) {
                                    Rlog.e(TAG, "Can't get the time. Use the current time.");
                                    jCurrentTimeMillis = System.currentTimeMillis();
                                }
                            }
                            long j2 = jCurrentTimeMillis;
                            try {
                                strGroup6 = matcher.group("callerId");
                            } catch (IllegalArgumentException unused7) {
                                Rlog.d(TAG, "Caller id is not provided or can't be parsed.");
                            }
                            createMissedIncomingCallEvent(j2, strGroup6);
                            z2 = true;
                            break;
                        }
                    } catch (PatternSyntaxException e) {
                        Rlog.w(TAG, "Configuration error. Unexpected missed incoming call sms pattern: " + str5 + ", e=" + e);
                    }
                }
            }
            z = z2;
        }
        if (!z) {
            Rlog.d(TAG, "SMS did not match any missed incoming call SMS pattern.");
        }
        return z;
    }

    private String[] splitCalls(String str) {
        if (str == null) {
            return null;
        }
        String[] strArrSplit = str.split("(\\n|\\s\\n)(\\n|\\s\\n)");
        String str2 = TAG;
        StringBuilder sb = new StringBuilder("splitTheMultipleCalls no of calls = ");
        sb.append(strArrSplit != null ? strArrSplit.length : 0);
        Rlog.d(str2, sb.toString());
        return strArrSplit;
    }

    private void createMissedIncomingCallEvent(long j, String str) {
        TelecomManager telecomManager = (TelecomManager) this.mPhone.getContext().getSystemService("telecom");
        if (telecomManager != null) {
            Bundle bundle = new Bundle();
            if (str != null) {
                bundle.putParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS", Uri.fromParts("tel", str, null));
            }
            bundle.putLong("android.telephony.extra.CALL_CREATED_EPOCH_TIME_MILLIS", j);
            telecomManager.addNewIncomingCall(makePstnPhoneAccountHandle(this.mPhone), bundle);
        }
    }

    private PhoneAccountHandle makePstnPhoneAccountHandle(Phone phone) {
        UserHandle subscriptionUserHandle = ((SubscriptionManager) phone.getContext().getSystemService("telephony_subscription_service")).getSubscriptionUserHandle(phone.getSubId());
        if (subscriptionUserHandle != null) {
            return new PhoneAccountHandle(PSTN_CONNECTION_SERVICE_COMPONENT, String.valueOf(phone.getSubId()), subscriptionUserHandle);
        }
        return new PhoneAccountHandle(PSTN_CONNECTION_SERVICE_COMPONENT, String.valueOf(phone.getSubId()));
    }
}
