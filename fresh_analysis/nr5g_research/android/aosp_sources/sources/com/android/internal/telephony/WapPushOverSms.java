package com.android.internal.telephony;

import android.R;
import android.app.BroadcastOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerWhitelistManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import com.google.android.mms.pdu.GenericPdu;
import com.google.android.mms.pdu.NotificationInd;
import com.google.android.mms.pdu.PduParser;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class WapPushOverSms implements ServiceConnection {
    protected final Context mContext;
    protected final FeatureFlags mFeatureFlags;
    protected PowerWhitelistManager mPowerWhitelistManager;
    private UserManager mUserManager;
    protected volatile IWapPushManager mWapPushManager;
    protected String mWapPushManagerPackage;
    private volatile boolean mBindingInProgress = false;
    private final Object mBindLock = new Object();

    private boolean bindWapPushManagerService(Context context) {
        Intent intent = new Intent(IWapPushManager.class.getName());
        ComponentName componentNameResolveSystemService = resolveSystemService(context.getPackageManager(), intent);
        intent.setComponent(componentNameResolveSystemService);
        if (componentNameResolveSystemService == null || !context.bindService(intent, this, 1)) {
            Rlog.e("WAP PUSH", "bindService() for wappush manager failed");
            return false;
        }
        synchronized (this) {
            this.mWapPushManagerPackage = componentNameResolveSystemService.getPackageName();
        }
        return true;
    }

    private static ComponentName resolveSystemService(PackageManager packageManager, Intent intent) {
        List<ResolveInfo> listQueryIntentServices = packageManager.queryIntentServices(intent, 1048576);
        ComponentName componentName = null;
        if (listQueryIntentServices == null) {
            return null;
        }
        int i = 0;
        while (i < listQueryIntentServices.size()) {
            ServiceInfo serviceInfo = listQueryIntentServices.get(i).serviceInfo;
            ComponentName componentName2 = new ComponentName(serviceInfo.applicationInfo.packageName, serviceInfo.name);
            if (componentName != null) {
                throw new IllegalStateException("Multiple system services handle " + intent + ": " + componentName + ", " + componentName2);
            }
            i++;
            componentName = componentName2;
        }
        return componentName;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this.mBindLock) {
            this.mWapPushManager = IWapPushManager.Stub.asInterface(iBinder);
            this.mBindingInProgress = false;
            this.mBindLock.notifyAll();
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        synchronized (this.mBindLock) {
            this.mWapPushManager = null;
            this.mBindingInProgress = false;
        }
    }

    public WapPushOverSms(Context context, FeatureFlags featureFlags) {
        this.mFeatureFlags = featureFlags;
        this.mContext = context;
        this.mPowerWhitelistManager = (PowerWhitelistManager) context.getSystemService(PowerWhitelistManager.class);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public void dispose() {
        synchronized (this.mBindLock) {
            if (this.mWapPushManager != null || this.mBindingInProgress) {
                try {
                    this.mContext.unbindService(this);
                } catch (IllegalArgumentException unused) {
                }
                this.mWapPushManager = null;
                this.mBindingInProgress = false;
                this.mBindLock.notifyAll();
            }
        }
    }

    protected DecodedResult decodeWapPdu(byte[] bArr, InboundSmsHandler inboundSmsHandler) {
        String str;
        String str2;
        int i;
        int i2;
        byte[] bArr2;
        GenericPdu genericPdu;
        DecodedResult decodedResult = new DecodedResult(this);
        try {
            int i3 = bArr[0] & 255;
            int i4 = bArr[1] & 255;
            int phoneId = inboundSmsHandler.getPhone().getPhoneId();
            if (i4 == 6 || i4 == 7) {
                i = i3;
                i2 = 2;
            } else {
                int integer = this.mContext.getResources().getInteger(R.integer.kg_security_flipper_weight);
                if (integer != -1) {
                    int i5 = integer + 1;
                    i = bArr[integer] & 255;
                    i2 = integer + 2;
                    i4 = bArr[i5] & 255;
                    if (i4 != 6 && i4 != 7) {
                        decodedResult.statusCode = 1;
                        return decodedResult;
                    }
                } else {
                    decodedResult.statusCode = 1;
                    return decodedResult;
                }
            }
            WspTypeDecoder wspTypeDecoderMakeWspTypeDecoder = TelephonyComponentFactory.getInstance().inject(WspTypeDecoder.class.getName()).makeWspTypeDecoder(bArr);
            if (!wspTypeDecoderMakeWspTypeDecoder.decodeUintvarInteger(i2)) {
                decodedResult.statusCode = 2;
                return decodedResult;
            }
            int i6 = i4;
            int value32 = (int) wspTypeDecoderMakeWspTypeDecoder.getValue32();
            int decodedDataLength = i2 + wspTypeDecoderMakeWspTypeDecoder.getDecodedDataLength();
            if (!wspTypeDecoderMakeWspTypeDecoder.decodeContentType(decodedDataLength)) {
                decodedResult.statusCode = 2;
                return decodedResult;
            }
            String valueString = wspTypeDecoderMakeWspTypeDecoder.getValueString();
            long value33 = wspTypeDecoderMakeWspTypeDecoder.getValue32();
            int decodedDataLength2 = decodedDataLength + wspTypeDecoderMakeWspTypeDecoder.getDecodedDataLength();
            byte[] bArr3 = new byte[value32];
            System.arraycopy(bArr, decodedDataLength, bArr3, 0, value32);
            if (valueString == null || !valueString.equals(WspTypeDecoder.CONTENT_TYPE_B_PUSH_CO)) {
                int i7 = decodedDataLength + value32;
                int length = bArr.length - i7;
                bArr2 = new byte[length];
                System.arraycopy(bArr, i7, bArr2, 0, length);
            } else {
                bArr2 = bArr;
            }
            int subscriptionId = SubscriptionManager.getSubscriptionId(phoneId);
            if (!SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
                subscriptionId = SmsManager.getDefaultSmsSubscriptionId();
            }
            int i8 = subscriptionId;
            try {
                genericPdu = new PduParser(bArr2, shouldParseContentDisposition(i8)).parse();
            } catch (Exception e) {
                Rlog.e("WAP PUSH", "Unable to parse PDU: " + e.toString());
                genericPdu = null;
            }
            if (genericPdu == null || genericPdu.getMessageType() != 130) {
                str = "WAP PUSH";
            } else {
                NotificationInd notificationInd = (NotificationInd) genericPdu;
                str = "WAP PUSH";
                try {
                    WapPushCache.putWapMessageSize(notificationInd.getContentLocation(), notificationInd.getTransactionId(), notificationInd.getMessageSize());
                    if (notificationInd.getFrom() != null && MiuiSmsControllerStub.get(inboundSmsHandler.getPhone().getPhoneId()).isBlocked(inboundSmsHandler.getPhone(), notificationInd.getFrom().getString(), null, 1000L, BlockChecker.isBlocked(this.mContext, notificationInd.getFrom().getString(), null))) {
                        decodedResult.statusCode = 1;
                        return decodedResult;
                    }
                } catch (ArrayIndexOutOfBoundsException e2) {
                    e = e2;
                    str2 = str;
                    Rlog.e(str2, "ignoring dispatchWapPdu() array index exception: " + e);
                    decodedResult.statusCode = 2;
                }
            }
            if (wspTypeDecoderMakeWspTypeDecoder.seekXWapApplicationId(decodedDataLength2, (decodedDataLength2 + value32) - 1)) {
                wspTypeDecoderMakeWspTypeDecoder.decodeXWapApplicationId((int) wspTypeDecoderMakeWspTypeDecoder.getValue32());
                String valueString2 = wspTypeDecoderMakeWspTypeDecoder.getValueString();
                if (valueString2 == null) {
                    valueString2 = Integer.toString((int) wspTypeDecoderMakeWspTypeDecoder.getValue32());
                }
                decodedResult.wapAppId = valueString2;
                decodedResult.contentType = valueString == null ? Long.toString(value33) : valueString;
            }
            decodedResult.subId = i8;
            decodedResult.phoneId = phoneId;
            decodedResult.parsedPdu = genericPdu;
            decodedResult.mimeType = valueString;
            decodedResult.transactionId = i;
            decodedResult.pduType = i6;
            decodedResult.header = bArr3;
            decodedResult.intentData = bArr2;
            decodedResult.contentTypeParameters = wspTypeDecoderMakeWspTypeDecoder.getContentParameters();
            decodedResult.statusCode = -1;
            str2 = str;
            Rlog.d(str2, "mimeType: " + decodedResult.mimeType);
            return decodedResult;
        } catch (ArrayIndexOutOfBoundsException e3) {
            e = e3;
            str = "WAP PUSH";
        }
        str2 = str;
        Rlog.e(str2, "ignoring dispatchWapPdu() array index exception: " + e);
        decodedResult.statusCode = 2;
        Rlog.d(str2, "mimeType: " + decodedResult.mimeType);
        return decodedResult;
    }

    private boolean ensureWapPushManagerBound() {
        if (this.mWapPushManager != null) {
            return true;
        }
        synchronized (this.mBindLock) {
            try {
                if (this.mWapPushManager != null) {
                    return true;
                }
                if (this.mBindingInProgress) {
                    try {
                        this.mBindLock.wait(5000L);
                        return this.mWapPushManager != null;
                    } catch (InterruptedException unused) {
                        Rlog.e("WAP PUSH", "Interrupted while waiting for service binding");
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
                this.mBindingInProgress = true;
                if (!bindWapPushManagerService(this.mContext)) {
                    this.mBindingInProgress = false;
                    return false;
                }
                try {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    while (this.mWapPushManager == null && this.mBindingInProgress) {
                        long jCurrentTimeMillis2 = 5000 - (System.currentTimeMillis() - jCurrentTimeMillis);
                        if (jCurrentTimeMillis2 <= 0) {
                            Rlog.e("WAP PUSH", "Timeout waiting for WapPushManager service connection");
                            this.mBindingInProgress = false;
                            return false;
                        }
                        this.mBindLock.wait(jCurrentTimeMillis2);
                    }
                    return this.mWapPushManager != null;
                } catch (InterruptedException unused2) {
                    Rlog.e("WAP PUSH", "Interrupted while waiting for service connection");
                    Thread.currentThread().interrupt();
                    this.mBindingInProgress = false;
                    return false;
                }
                throw th;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int dispatchWapPdu(byte[] bArr, InboundSmsHandler.SmsBroadcastReceiver smsBroadcastReceiver, InboundSmsHandler inboundSmsHandler, String str, int i, long j) {
        IWapPushManager iWapPushManager;
        Bundle bundle;
        DecodedResult decodedResultDecodeWapPdu = decodeWapPdu(bArr, inboundSmsHandler);
        int i2 = decodedResultDecodeWapPdu.statusCode;
        if (i2 != -1) {
            return i2;
        }
        if (decodedResultDecodeWapPdu.wapAppId != null) {
            try {
                if (ensureWapPushManagerBound() && (iWapPushManager = this.mWapPushManager) != null) {
                    synchronized (this) {
                        this.mPowerWhitelistManager.whitelistAppTemporarilyForEvent(this.mWapPushManagerPackage, 2, 315, "mms-mgr");
                    }
                    Intent intent = new Intent();
                    intent.putExtra("transactionId", decodedResultDecodeWapPdu.transactionId);
                    intent.putExtra("pduType", decodedResultDecodeWapPdu.pduType);
                    intent.putExtra("header", decodedResultDecodeWapPdu.header);
                    intent.putExtra(ConfigProviderAdaptor.DOMAIN_DATA, decodedResultDecodeWapPdu.intentData);
                    intent.putExtra("contentTypeParameters", decodedResultDecodeWapPdu.contentTypeParameters);
                    SubscriptionManager.putPhoneIdAndSubIdExtra(intent, decodedResultDecodeWapPdu.phoneId);
                    if (!TextUtils.isEmpty(str)) {
                        intent.putExtra("address", str);
                    }
                    int iProcessMessage = iWapPushManager.processMessage(decodedResultDecodeWapPdu.wapAppId, decodedResultDecodeWapPdu.contentType, intent);
                    if ((iProcessMessage & 1) > 0 && (iProcessMessage & 32768) == 0) {
                        return 1;
                    }
                }
            } catch (RemoteException | IllegalStateException unused) {
            }
        }
        if (decodedResultDecodeWapPdu.mimeType == null) {
            return 2;
        }
        Intent intent2 = new Intent("android.provider.Telephony.WAP_PUSH_DELIVER");
        intent2.setType(decodedResultDecodeWapPdu.mimeType);
        intent2.putExtra("transactionId", decodedResultDecodeWapPdu.transactionId);
        intent2.putExtra("pduType", decodedResultDecodeWapPdu.pduType);
        intent2.putExtra("header", decodedResultDecodeWapPdu.header);
        intent2.putExtra(ConfigProviderAdaptor.DOMAIN_DATA, decodedResultDecodeWapPdu.intentData);
        intent2.putExtra("contentTypeParameters", decodedResultDecodeWapPdu.contentTypeParameters);
        if (!TextUtils.isEmpty(str)) {
            intent2.putExtra("address", str);
        }
        if (j != 0) {
            intent2.putExtra("messageId", j);
        }
        UserHandle subscriptionUserHandle = TelephonyUtils.getSubscriptionUserHandle(this.mContext, i);
        if (subscriptionUserHandle == null) {
            subscriptionUserHandle = this.mUserManager.getMainUser();
        }
        ComponentName defaultMmsApplicationAsUser = SmsApplication.getDefaultMmsApplicationAsUser(this.mContext, true, subscriptionUserHandle);
        if (defaultMmsApplicationAsUser != null) {
            intent2.setComponent(defaultMmsApplicationAsUser);
            long jWhitelistAppTemporarilyForEvent = this.mPowerWhitelistManager.whitelistAppTemporarilyForEvent(defaultMmsApplicationAsUser.getPackageName(), 2, 315, "mms-app");
            BroadcastOptions broadcastOptionsMakeBasic = BroadcastOptions.makeBasic();
            broadcastOptionsMakeBasic.setTemporaryAppAllowlist(jWhitelistAppTemporarilyForEvent, 0, 315, "");
            bundle = broadcastOptionsMakeBasic.toBundle();
        } else {
            bundle = null;
        }
        inboundSmsHandler.dispatchIntent(intent2, getPermissionForType(decodedResultDecodeWapPdu.mimeType), getAppOpsStringPermissionForIntent(decodedResultDecodeWapPdu.mimeType), bundle, smsBroadcastReceiver, subscriptionUserHandle, i);
        return -1;
    }

    public boolean isWapPushForMms(byte[] bArr, InboundSmsHandler inboundSmsHandler) {
        DecodedResult decodedResultDecodeWapPdu = decodeWapPdu(bArr, inboundSmsHandler);
        return decodedResultDecodeWapPdu.statusCode == -1 && WspTypeDecoder.CONTENT_TYPE_B_MMS.equals(decodedResultDecodeWapPdu.mimeType);
    }

    protected static boolean shouldParseContentDisposition(int i) {
        return SmsManager.getSmsManagerForSubscriptionId(i).getCarrierConfigValues().getBoolean("supportMmsContentDisposition", true);
    }

    public static String getPermissionForType(String str) {
        if (WspTypeDecoder.CONTENT_TYPE_B_MMS.equals(str)) {
            return "android.permission.RECEIVE_MMS";
        }
        return "android.permission.RECEIVE_WAP_PUSH";
    }

    public static String getAppOpsStringPermissionForIntent(String str) {
        if (WspTypeDecoder.CONTENT_TYPE_B_MMS.equals(str)) {
            return "android:receive_mms";
        }
        return "android:receive_wap_push";
    }

    protected final class DecodedResult {
        public String contentType;
        public HashMap<String, String> contentTypeParameters;
        public byte[] header;
        public byte[] intentData;
        public String mimeType;
        GenericPdu parsedPdu;
        public int pduType;
        public int phoneId;
        public int statusCode;
        public int subId;
        public int transactionId;
        public String wapAppId;

        protected DecodedResult(WapPushOverSms wapPushOverSms) {
        }
    }
}
