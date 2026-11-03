package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class AppSmsManager {
    private static final long TIMEOUT_MILLIS = 300000;
    private final Context mContext;
    private final Object mLock = new Object();
    private final SecureRandom mRandom = new SecureRandom();
    private final Map mTokenMap = new ArrayMap();
    private final Map mPackageMap = new ArrayMap();

    public AppSmsManager(Context context) {
        this.mContext = context;
    }

    public String createAppSpecificSmsToken(String str, PendingIntent pendingIntent) {
        ((AppOpsManager) this.mContext.getSystemService("appops")).checkPackage(Binder.getCallingUid(), str);
        String strGenerateNonce = generateNonce();
        synchronized (this.mLock) {
            try {
                if (this.mPackageMap.containsKey(str)) {
                    removeRequestLocked((AppRequestInfo) this.mPackageMap.get(str));
                }
                addRequestLocked(new AppRequestInfo(this, str, pendingIntent, strGenerateNonce));
            } catch (Throwable th) {
                throw th;
            }
        }
        return strGenerateNonce;
    }

    public String createAppSpecificSmsTokenWithPackageInfo(int i, String str, String str2, PendingIntent pendingIntent) {
        if (TextUtils.isEmpty(str)) {
            Phone$$ExternalSyntheticBUOutline0.m("callingPackageName cannot be null or empty.");
            return null;
        }
        Objects.requireNonNull(pendingIntent, "intent cannot be null");
        ((AppOpsManager) this.mContext.getSystemService("appops")).checkPackage(Binder.getCallingUid(), str);
        String strGenerateToken = PackageBasedTokenUtil.generateToken(this.mContext, str);
        if (strGenerateToken == null) {
            return strGenerateToken;
        }
        synchronized (this.mLock) {
            try {
                if (this.mPackageMap.containsKey(str)) {
                    removeRequestLocked((AppRequestInfo) this.mPackageMap.get(str));
                }
                addRequestLocked(new AppRequestInfo(this, str, pendingIntent, strGenerateToken, str2, i, true));
            } catch (Throwable th) {
                throw th;
            }
        }
        return strGenerateToken;
    }

    public boolean handleSmsReceivedIntent(Intent intent) {
        if (intent.getAction() != "android.provider.Telephony.SMS_DELIVER") {
            Log.wtf("AppSmsManager", "Got intent with incorrect action: " + intent.getAction());
            return false;
        }
        synchronized (this.mLock) {
            try {
                removeExpiredTokenLocked();
                String strExtractMessage = extractMessage(intent);
                if (TextUtils.isEmpty(strExtractMessage)) {
                    return false;
                }
                AppRequestInfo appRequestInfoFindAppRequestInfoSmsIntentLocked = findAppRequestInfoSmsIntentLocked(strExtractMessage);
                if (appRequestInfoFindAppRequestInfoSmsIntentLocked == null) {
                    return false;
                }
                try {
                    appRequestInfoFindAppRequestInfoSmsIntentLocked.pendingIntent.send(this.mContext, 0, new Intent().putExtras(intent.getExtras()).putExtra("android.telephony.extra.STATUS", 0).putExtra("android.telephony.extra.SMS_MESSAGE", strExtractMessage).putExtra("android.telephony.extra.SIM_SUBSCRIPTION_ID", appRequestInfoFindAppRequestInfoSmsIntentLocked.subId).addFlags(2097152));
                    removeRequestLocked(appRequestInfoFindAppRequestInfoSmsIntentLocked);
                    return true;
                } catch (PendingIntent.CanceledException unused) {
                    removeRequestLocked(appRequestInfoFindAppRequestInfoSmsIntentLocked);
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void removeExpiredTokenLocked() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        Iterator it = this.mTokenMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            AppRequestInfo appRequestInfo = (AppRequestInfo) entry.getValue();
            if (appRequestInfo.packageBasedToken && jCurrentTimeMillis - TIMEOUT_MILLIS > appRequestInfo.timestamp) {
                try {
                    appRequestInfo.pendingIntent.send(this.mContext, 0, new Intent().putExtra("android.telephony.extra.STATUS", 1).addFlags(2097152));
                } catch (PendingIntent.CanceledException unused) {
                }
                this.mPackageMap.remove(((AppRequestInfo) entry.getValue()).packageName);
                it.remove();
            }
        }
    }

    private String extractMessage(Intent intent) {
        SmsMessage[] messagesFromIntent = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if (messagesFromIntent == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (SmsMessage smsMessage : messagesFromIntent) {
            if (smsMessage != null && smsMessage.getMessageBody() != null) {
                sb.append(smsMessage.getMessageBody());
            }
        }
        return sb.toString();
    }

    private AppRequestInfo findAppRequestInfoSmsIntentLocked(String str) {
        for (String str2 : this.mTokenMap.keySet()) {
            if (str.trim().contains(str2) && hasPrefix(str2, str)) {
                return (AppRequestInfo) this.mTokenMap.get(str2);
            }
        }
        return null;
    }

    private String generateNonce() {
        byte[] bArr = new byte[8];
        this.mRandom.nextBytes(bArr);
        return Base64.encodeToString(bArr, 11);
    }

    private boolean hasPrefix(String str, String str2) {
        AppRequestInfo appRequestInfo = (AppRequestInfo) this.mTokenMap.get(str);
        if (TextUtils.isEmpty(appRequestInfo.prefixes)) {
            return true;
        }
        for (String str3 : appRequestInfo.prefixes.split(",")) {
            if (str2.startsWith(str3)) {
                return true;
            }
        }
        return false;
    }

    private void removeRequestLocked(AppRequestInfo appRequestInfo) {
        this.mTokenMap.remove(appRequestInfo.token);
        this.mPackageMap.remove(appRequestInfo.packageName);
    }

    private void addRequestLocked(AppRequestInfo appRequestInfo) {
        this.mTokenMap.put(appRequestInfo.token, appRequestInfo);
        this.mPackageMap.put(appRequestInfo.packageName, appRequestInfo);
    }

    private final class AppRequestInfo {
        public final boolean packageBasedToken;
        public final String packageName;
        public final PendingIntent pendingIntent;
        public final String prefixes;
        public final int subId;
        public final long timestamp;
        public final String token;

        AppRequestInfo(AppSmsManager appSmsManager, String str, PendingIntent pendingIntent, String str2) {
            this(appSmsManager, str, pendingIntent, str2, null, -1, false);
        }

        AppRequestInfo(AppSmsManager appSmsManager, String str, PendingIntent pendingIntent, String str2, String str3, int i, boolean z) {
            this.packageName = str;
            this.pendingIntent = pendingIntent;
            this.token = str2;
            this.timestamp = System.currentTimeMillis();
            this.prefixes = str3;
            this.subId = i;
            this.packageBasedToken = z;
        }
    }
}
