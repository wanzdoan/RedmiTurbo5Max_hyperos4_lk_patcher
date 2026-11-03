package com.android.internal.telephony;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.telecom.PhoneAccountHandle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.VisualVoicemailSms;
import android.telephony.VisualVoicemailSmsFilterSettings;
import android.util.ArrayMap;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class VisualVoicemailSmsFilter {
    private static final PhoneAccountHandleConverter DEFAULT_PHONE_ACCOUNT_HANDLE_CONVERTER;
    private static final ComponentName PSTN_CONNECTION_SERVICE_COMPONENT = new ComponentName("com.android.phone", "com.android.services.telephony.TelephonyConnectionService");
    private static Map sPatterns;
    private static PhoneAccountHandleConverter sPhoneAccountHandleConverter;

    public interface PhoneAccountHandleConverter {
        PhoneAccountHandle fromSubId(int i, Context context);
    }

    static {
        PhoneAccountHandleConverter phoneAccountHandleConverter = new PhoneAccountHandleConverter() { // from class: com.android.internal.telephony.VisualVoicemailSmsFilter.1
            @Override // com.android.internal.telephony.VisualVoicemailSmsFilter.PhoneAccountHandleConverter
            public PhoneAccountHandle fromSubId(int i, Context context) {
                int phoneId;
                if (!SubscriptionManager.isValidSubscriptionId(i) || (phoneId = SubscriptionManager.getPhoneId(i)) == -1) {
                    return null;
                }
                UserHandle subscriptionUserHandle = ((SubscriptionManager) context.getSystemService("telephony_subscription_service")).getSubscriptionUserHandle(i);
                if (subscriptionUserHandle != null) {
                    return new PhoneAccountHandle(VisualVoicemailSmsFilter.PSTN_CONNECTION_SERVICE_COMPONENT, Integer.toString(PhoneFactory.getPhone(phoneId).getSubId()), subscriptionUserHandle);
                }
                return new PhoneAccountHandle(VisualVoicemailSmsFilter.PSTN_CONNECTION_SERVICE_COMPONENT, Integer.toString(PhoneFactory.getPhone(phoneId).getSubId()));
            }
        };
        DEFAULT_PHONE_ACCOUNT_HANDLE_CONVERTER = phoneAccountHandleConverter;
        sPhoneAccountHandleConverter = phoneAccountHandleConverter;
    }

    private static class FullMessage {
        public SmsMessage firstMessage;
        public String fullMessageBody;

        private FullMessage() {
        }
    }

    public static boolean filter(Context context, byte[][] bArr, String str, int i, int i2) {
        VisualVoicemailSmsFilterSettings activeVisualVoicemailSmsFilterSettings = ((TelephonyManager) context.getSystemService("phone")).getActiveVisualVoicemailSmsFilterSettings(i2);
        if (activeVisualVoicemailSmsFilterSettings == null) {
            FullMessage fullMessage = getFullMessage(bArr, str);
            if (fullMessage == null || !messageBodyMatchesVvmPattern(context, i2, fullMessage.fullMessageBody)) {
                return false;
            }
            Log.e("VvmSmsFilter", "SMS matching VVM format received but the filter not been set yet");
            return true;
        }
        PhoneAccountHandle phoneAccountHandleFromSubId = sPhoneAccountHandleConverter.fromSubId(i2, context);
        if (phoneAccountHandleFromSubId == null) {
            Log.e("VvmSmsFilter", "Unable to convert subId " + i2 + " to PhoneAccountHandle");
            return false;
        }
        String str2 = activeVisualVoicemailSmsFilterSettings.clientPrefix;
        FullMessage fullMessage2 = getFullMessage(bArr, str);
        if (fullMessage2 == null) {
            Log.i("VvmSmsFilter", "Unparsable SMS received");
            String asciiPduMessage = parseAsciiPduMessage(bArr);
            VisualVoicemailSmsParser.WrappedMessageData alternativeFormat = VisualVoicemailSmsParser.parseAlternativeFormat(asciiPduMessage);
            if (alternativeFormat == null) {
                Log.i("VvmSmsFilter", "Attempt to parse ascii PDU");
                alternativeFormat = VisualVoicemailSmsParser.parse(str2, asciiPduMessage);
            }
            if (alternativeFormat != null) {
                sendVvmSmsBroadcast(context, activeVisualVoicemailSmsFilterSettings, phoneAccountHandleFromSubId, alternativeFormat, null);
            }
            return false;
        }
        String str3 = fullMessage2.fullMessageBody;
        VisualVoicemailSmsParser.WrappedMessageData wrappedMessageData = VisualVoicemailSmsParser.parse(str2, str3);
        if (wrappedMessageData != null) {
            int i3 = activeVisualVoicemailSmsFilterSettings.destinationPort;
            if (i3 == -2) {
                if (i == -1) {
                    Log.i("VvmSmsFilter", "SMS matching VVM format received but is not a DATA SMS");
                    return false;
                }
            } else if (i3 != -1 && i3 != i) {
                Log.i("VvmSmsFilter", "SMS matching VVM format received but is not directed to port " + activeVisualVoicemailSmsFilterSettings.destinationPort);
                return false;
            }
            if (!activeVisualVoicemailSmsFilterSettings.originatingNumbers.isEmpty() && !isSmsFromNumbers(fullMessage2.firstMessage, activeVisualVoicemailSmsFilterSettings.originatingNumbers)) {
                Log.i("VvmSmsFilter", "SMS matching VVM format received but is not from originating numbers");
                return false;
            }
            sendVvmSmsBroadcast(context, activeVisualVoicemailSmsFilterSettings, phoneAccountHandleFromSubId, wrappedMessageData, null);
            return true;
        }
        if (!messageBodyMatchesVvmPattern(context, i2, str3)) {
            return false;
        }
        Log.w("VvmSmsFilter", "SMS matches pattern but has illegal format, still dropping as VVM SMS");
        sendVvmSmsBroadcast(context, activeVisualVoicemailSmsFilterSettings, phoneAccountHandleFromSubId, null, str3);
        return true;
    }

    private static boolean messageBodyMatchesVvmPattern(Context context, int i, String str) {
        buildPatternsMap(context);
        List<Pattern> list = (List) sPatterns.get(((TelephonyManager) context.getSystemService(TelephonyManager.class)).getSimOperator(i));
        if (list != null && !list.isEmpty()) {
            for (Pattern pattern : list) {
                if (pattern.matcher(str).matches()) {
                    Log.w("VvmSmsFilter", "Incoming SMS matches pattern " + pattern);
                    return true;
                }
            }
        }
        return false;
    }

    public static void setPhoneAccountHandleConverterForTest(PhoneAccountHandleConverter phoneAccountHandleConverter) {
        if (phoneAccountHandleConverter == null) {
            sPhoneAccountHandleConverter = DEFAULT_PHONE_ACCOUNT_HANDLE_CONVERTER;
        } else {
            sPhoneAccountHandleConverter = phoneAccountHandleConverter;
        }
    }

    private static void buildPatternsMap(Context context) {
        if (sPatterns != null) {
            return;
        }
        sPatterns = new ArrayMap();
        for (String str : context.getResources().getStringArray(R.array.vendor_required_apps_managed_profile)) {
            String[] strArrSplit = str.split(";")[0].split(",");
            Pattern patternCompile = Pattern.compile(str.split(";")[1]);
            for (String str2 : strArrSplit) {
                if (!sPatterns.containsKey(str2)) {
                    sPatterns.put(str2, new ArrayList());
                }
                ((List) sPatterns.get(str2)).add(patternCompile);
            }
        }
    }

    private static void sendVvmSmsBroadcast(Context context, VisualVoicemailSmsFilterSettings visualVoicemailSmsFilterSettings, PhoneAccountHandle phoneAccountHandle, VisualVoicemailSmsParser.WrappedMessageData wrappedMessageData, String str) {
        Log.i("VvmSmsFilter", "VVM SMS received");
        Intent intent = new Intent("com.android.internal.provider.action.VOICEMAIL_SMS_RECEIVED");
        VisualVoicemailSms.Builder builder = new VisualVoicemailSms.Builder();
        if (wrappedMessageData != null) {
            builder.setPrefix(wrappedMessageData.prefix);
            builder.setFields(wrappedMessageData.fields);
        }
        if (str != null) {
            builder.setMessageBody(str);
        }
        builder.setPhoneAccountHandle(phoneAccountHandle);
        intent.putExtra("android.provider.extra.VOICEMAIL_SMS", builder.build());
        intent.putExtra("android.provider.extra.TARGET_PACAKGE", visualVoicemailSmsFilterSettings.packageName);
        intent.setPackage("com.android.phone");
        context.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    private static FullMessage getFullMessage(byte[][] bArr, String str) {
        FullMessage fullMessage = new FullMessage();
        StringBuilder sb = new StringBuilder();
        CharsetDecoder charsetDecoderNewDecoder = StandardCharsets.UTF_8.newDecoder();
        for (byte[] bArr2 : bArr) {
            SmsMessage smsMessageCreateFromPdu = SmsMessage.createFromPdu(bArr2, str);
            if (smsMessageCreateFromPdu == null) {
                return null;
            }
            if (fullMessage.firstMessage == null) {
                fullMessage.firstMessage = smsMessageCreateFromPdu;
            }
            String messageBody = smsMessageCreateFromPdu.getMessageBody();
            if ((messageBody == null || (smsMessageCreateFromPdu.is3gpp() && smsMessageCreateFromPdu.getReceivedEncodingType() == 2)) && smsMessageCreateFromPdu.getUserData() != null) {
                Log.d("VvmSmsFilter", "getFullMessage decode using UTF-8");
                try {
                    messageBody = charsetDecoderNewDecoder.decode(ByteBuffer.wrap(smsMessageCreateFromPdu.getUserData())).toString();
                } catch (CharacterCodingException e) {
                    Log.e("VvmSmsFilter", "getFullMessage: got CharacterCodingException when decoding with UTF-8, e = " + e);
                    return null;
                }
            }
            if (messageBody != null) {
                sb.append(messageBody);
            }
        }
        fullMessage.fullMessageBody = sb.toString();
        return fullMessage;
    }

    private static String parseAsciiPduMessage(byte[][] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte[] bArr2 : bArr) {
            sb.append(new String(bArr2, StandardCharsets.US_ASCII));
        }
        return sb.toString();
    }

    private static boolean isSmsFromNumbers(SmsMessage smsMessage, List list) {
        if (smsMessage == null) {
            Log.e("VvmSmsFilter", "Unable to create SmsMessage from PDU, cannot determine originating number");
            return false;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (PhoneNumberUtils.compare((String) it.next(), smsMessage.getOriginatingAddress())) {
                return true;
            }
        }
        return false;
    }
}
