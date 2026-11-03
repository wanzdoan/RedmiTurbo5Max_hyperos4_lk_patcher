package com.android.internal.telephony;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.TelephonyServiceManager;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyFrameworkInitializer;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.MiuiUiccControllerStub;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class SmsController extends ISmsImplBase {
    private static SmsController sInstance;
    private final Context mContext;
    private final FeatureFlags mFlags;
    private final PackageManager mPackageManager;
    private final int mVendorApiLevel = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
    private IHyperTeleSmsManager mIHyperTeleSmsManager = (IHyperTeleSmsManager) HyperTelephonyFactory.getInstance().initModule(IHyperTeleSmsManager.DEFAULT, new Object[0]);

    public static SmsController init(Context context, FeatureFlags featureFlags) {
        synchronized (SmsController.class) {
            try {
                if (sInstance == null) {
                    sInstance = new SmsController(context, featureFlags);
                    TelephonyServiceManager.ServiceRegisterer smsServiceRegisterer = TelephonyFrameworkInitializer.getTelephonyServiceManager().getSmsServiceRegisterer();
                    if (smsServiceRegisterer.get() == null) {
                        smsServiceRegisterer.register(sInstance);
                    }
                } else {
                    Log.wtf("SmsController", "SmsController already initialized");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return sInstance;
    }

    public SmsController(Context context, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mFlags = featureFlags;
        this.mPackageManager = context.getPackageManager();
    }

    private Phone getPhone(int i) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        return phone == null ? PhoneFactory.getDefaultPhone() : phone;
    }

    private SmsPermissions getSmsPermissions(int i) {
        Phone phone = getPhone(i);
        Context context = this.mContext;
        return new SmsPermissions(phone, context, (AppOpsManager) context.getSystemService("appops"));
    }

    public boolean updateMessageOnIccEfForSubscriber(int i, String str, int i2, int i3, byte[] bArr) {
        if (str == null) {
            str = getCallingPackage();
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.updateMessageOnIccEf(str, i2, i3, bArr);
        }
        Rlog.e("SmsController", "updateMessageOnIccEfForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        return false;
    }

    public boolean copyMessageToIccEfForSubscriber(int i, String str, int i2, byte[] bArr, byte[] bArr2) {
        if (str == null) {
            str = getCallingPackage();
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.copyMessageToIccEf(str, i2, bArr, bArr2);
        }
        Rlog.e("SmsController", "copyMessageToIccEfForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        return false;
    }

    public List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int i, String str) {
        if (str == null) {
            str = getCallingPackage();
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.getAllMessagesFromIccEf(str);
        }
        Rlog.e("SmsController", "getAllMessagesFromIccEfForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        return null;
    }

    @Deprecated
    public void sendDataForSubscriber(int i, String str, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        sendDataForSubscriber(i, str, null, str2, str3, i2, bArr, pendingIntent, pendingIntent2);
    }

    public void sendDataForSubscriber(int i, String str, String str2, String str3, String str4, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        if (str == null) {
            str = getCallingPackage();
        }
        String str5 = str;
        UserHandle callingUserHandle = Binder.getCallingUserHandle();
        int callingUid = Binder.getCallingUid();
        Rlog.d("SmsController", "sendDataForSubscriber caller=" + str5);
        if (!TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mContext, i, callingUserHandle, str3)) {
            TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(this.mContext, i, callingUid, str5);
            sendErrorInPendingIntent(pendingIntent, 33);
            return;
        }
        if (isNumberBlockedByFDN(i, str3, str5)) {
            sendErrorInPendingIntent(pendingIntent, 6);
            return;
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.sendData(str5, callingUserHandle.getIdentifier(), str2, str3, str4, i2, bArr, pendingIntent, pendingIntent2, callingUid);
            return;
        }
        Rlog.e("SmsController", "sendDataForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        sendErrorInPendingIntent(pendingIntent, 1);
    }

    public void sendRawPduForSubscriber(int i, String str, String str2, String str3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.SEND_SMS", "Sending SMS message");
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "Sending SMS message");
        if (str == null) {
            str = getCallingPackage();
        }
        String str4 = str;
        if (bArr == null) {
            Rlog.e("SmsController", "sendRawPduForSubscriber: pdu is null");
            sendErrorInPendingIntent(pendingIntent, 3);
            return;
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.sendRawPdu(str4, Binder.getCallingUserHandle().getIdentifier(), str2, str3, bArr, pendingIntent, pendingIntent2, Binder.getCallingUid());
            return;
        }
        Rlog.e("SmsController", "sendRawPduForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        sendErrorInPendingIntent(pendingIntent, 1);
    }

    private void sendDataForSubscriberWithSelfPermissionsInternal(int i, String str, int i2, String str2, String str3, String str4, int i3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i4) {
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.sendDataWithSelfPermissions(str, i2, str2, str3, str4, i3, bArr, pendingIntent, pendingIntent2, z, i4);
            return;
        }
        Rlog.e("SmsController", "sendText iccSmsIntMgr is null for Subscription: " + i);
        sendErrorInPendingIntent(pendingIntent, 1);
    }

    private String getCallingPackage() {
        String callingPackage = TelephonyCommonUtilsStub.get().getCallingPackage();
        if (!TextUtils.isEmpty(callingPackage)) {
            return callingPackage;
        }
        String[] packagesForUid = this.mContext.createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager().getPackagesForUid(Binder.getCallingUid());
        if (packagesForUid == null || packagesForUid.length == 0) {
            return "";
        }
        return packagesForUid[0];
    }

    public void sendTextForSubscriber(int i, String str, String str2, String str3, String str4, String str5, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, long j) {
        sendTextForSubscriber(i, str, str2, str3, str4, str5, pendingIntent, pendingIntent2, z, j, false, false);
    }

    public void sendTextForSubscriber(int i, String str, String str2, String str3, String str4, String str5, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, long j, boolean z2, boolean z3) {
        String callingPackage = str == null ? getCallingPackage() : str;
        UserHandle callingUserHandle = Binder.getCallingUserHandle();
        int callingUid = Binder.getCallingUid();
        Rlog.d("SmsController", "sendTextForSubscriber caller=" + callingPackage);
        if ((z2 || z3) && this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0) {
            SmsController$$ExternalSyntheticBUOutline0.m("Requires MODIFY_PHONE_STATE permission.");
            return;
        }
        if (!getSmsPermissions(i).checkCallingCanSendText(z, callingPackage, str2, "Sending SMS message")) {
            sendErrorInPendingIntent(pendingIntent, 1);
            return;
        }
        String str6 = callingPackage;
        if (MiuiSmsControllerStub.get(getPhone(i).getPhoneId()).startConsentUiIfNeeded(this.mContext, str6, i, str2, str3, str4, str5, pendingIntent, pendingIntent2, z, j)) {
            return;
        }
        boolean z4 = this.mContext.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0;
        Rlog.d("SmsController", "sendTextForSubscriber: caller has INTERACT_ACROSS_USERS_FULL? " + z4);
        if (!z4 && !TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mContext, i, Binder.getCallingUserHandle(), str3)) {
            TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(this.mContext, i, Binder.getCallingUid(), str6);
            sendErrorInPendingIntent(pendingIntent, 33);
            return;
        }
        if (!z2 && isNumberBlockedByFDN(i, str3, str6)) {
            sendErrorInPendingIntent(pendingIntent, 6);
            return;
        }
        enforceTelephonyFeatureWithException(str6, "sendTextForSubscriber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfo subscriptionInfo = getSubscriptionInfo(i);
            if (isBluetoothSubscription(subscriptionInfo)) {
                sendBluetoothText(subscriptionInfo, str3, str5, pendingIntent, pendingIntent2);
            } else {
                sendIccText(i, str6, callingUserHandle.getIdentifier(), str3, str4, str5, pendingIntent, pendingIntent2, z, j, z3, callingUid);
            }
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private boolean isBluetoothSubscription(SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo != null && subscriptionInfo.getSubscriptionType() == 1;
    }

    private void sendBluetoothText(SubscriptionInfo subscriptionInfo, String str, String str2, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        new BtSmsInterfaceManager().sendText(this.mContext, str, str2, pendingIntent, pendingIntent2, subscriptionInfo);
    }

    private void sendIccText(int i, String str, int i2, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, long j, boolean z2, int i3) {
        Rlog.d("SmsController", "sendTextForSubscriber iccSmsIntMgr Subscription: " + i + " " + formatCrossStackMessageId(j));
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.sendText(str, i2, str2, str3, str4, pendingIntent, pendingIntent2, z, j, z2, i3);
            return;
        }
        Rlog.e("SmsController", "sendTextForSubscriber iccSmsIntMgr is null for Subscription: " + i + " " + formatCrossStackMessageId(j));
        sendErrorInPendingIntent(pendingIntent, 1);
    }

    private void sendTextForSubscriberWithSelfPermissionsInternal(int i, String str, int i2, String str2, String str3, String str4, String str5, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, boolean z2, int i3) {
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.sendTextWithSelfPermissions(str, i2, str2, str3, str4, str5, pendingIntent, pendingIntent2, z, z2, i3);
            return;
        }
        Rlog.e("SmsController", "sendText iccSmsIntMgr is null for Subscription: " + i);
        sendErrorInPendingIntent(pendingIntent, 1);
    }

    public void sendTextForSubscriberWithOptions(int i, String str, String str2, String str3, String str4, String str5, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, boolean z2, int i3) {
        String callingPackage = str == null ? getCallingPackage() : str;
        UserHandle callingUserHandle = Binder.getCallingUserHandle();
        int callingUid = Binder.getCallingUid();
        Rlog.d("SmsController", "sendTextForSubscriberWithOptions caller=" + callingPackage);
        if (!TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mContext, i, Binder.getCallingUserHandle(), str3)) {
            TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(this.mContext, i, callingUid, callingPackage);
            sendErrorInPendingIntent(pendingIntent, 33);
            return;
        }
        if (isNumberBlockedByFDN(i, str3, callingPackage)) {
            sendErrorInPendingIntent(pendingIntent, 6);
            return;
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.sendTextWithOptions(callingPackage, callingUserHandle.getIdentifier(), str2, str3, str4, str5, pendingIntent, pendingIntent2, z, i2, z2, i3, callingUid);
            return;
        }
        Rlog.e("SmsController", "sendTextWithOptions iccSmsIntMgr is null for Subscription: " + i);
        sendErrorInPendingIntent(pendingIntent, 1);
    }

    public void sendMultipartTextForSubscriber(int i, String str, String str2, String str3, String str4, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, long j) {
        String callingPackage = getCallingPackage();
        UserHandle callingUserHandle = Binder.getCallingUserHandle();
        int callingUid = Binder.getCallingUid();
        Rlog.d("SmsController", "sendMultipartTextForSubscriber caller=" + callingPackage);
        if (!TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mContext, i, Binder.getCallingUserHandle(), str3)) {
            TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(this.mContext, i, callingUid, callingPackage);
            sendErrorInPendingIntents(list2, 33);
            return;
        }
        enforceTelephonyFeatureWithException(callingPackage, "sendMultipartTextForSubscriber");
        if (isNumberBlockedByFDN(i, str3, callingPackage)) {
            sendErrorInPendingIntents(list2, 6);
            return;
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.sendMultipartText(callingPackage, callingUserHandle.getIdentifier(), str2, str3, str4, list, list2, list3, z, j, callingUid);
            return;
        }
        Rlog.e("SmsController", "sendMultipartTextForSubscriber iccSmsIntMgr is null for Subscription: " + i + " " + formatCrossStackMessageId(j));
        sendErrorInPendingIntents(list2, 1);
    }

    public void sendMultipartTextForSubscriberWithOptions(int i, String str, String str2, String str3, String str4, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, int i2, boolean z2, int i3) {
        String callingPackage = str == null ? getCallingPackage() : str;
        UserHandle callingUserHandle = Binder.getCallingUserHandle();
        int callingUid = Binder.getCallingUid();
        Rlog.d("SmsController", "sendMultipartTextForSubscriberWithOptions caller=" + callingPackage);
        if (!TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mContext, i, Binder.getCallingUserHandle(), str3)) {
            TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(this.mContext, i, callingUid, callingPackage);
            sendErrorInPendingIntents(list2, 33);
            return;
        }
        if (isNumberBlockedByFDN(i, str3, callingPackage)) {
            sendErrorInPendingIntents(list2, 6);
            return;
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.sendMultipartTextWithOptions(callingPackage, callingUserHandle.getIdentifier(), str2, str3, str4, list, list2, list3, z, i2, z2, i3, 0L, callingUid);
            return;
        }
        Rlog.e("SmsController", "sendMultipartTextWithOptions iccSmsIntMgr is null for Subscription: " + i);
        sendErrorInPendingIntents(list2, 1);
    }

    public boolean enableCellBroadcastForSubscriber(int i, int i2, int i3) {
        return enableCellBroadcastRangeForSubscriber(i, i2, i2, i3);
    }

    public boolean enableCellBroadcastRangeForSubscriber(int i, int i2, int i3, int i4) {
        enforceTelephonyFeatureWithException(getCallingPackage(), "enableCellBroadcastRangeForSubscriber");
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.enableCellBroadcastRange(i2, i3, i4);
        }
        Rlog.e("SmsController", "enableCellBroadcastRangeForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        return false;
    }

    public boolean disableCellBroadcastForSubscriber(int i, int i2, int i3) {
        return disableCellBroadcastRangeForSubscriber(i, i2, i2, i3);
    }

    public boolean disableCellBroadcastRangeForSubscriber(int i, int i2, int i3, int i4) {
        enforceTelephonyFeatureWithException(getCallingPackage(), "disableCellBroadcastRangeForSubscriber");
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.disableCellBroadcastRange(i2, i3, i4);
        }
        Rlog.e("SmsController", "disableCellBroadcastRangeForSubscriber iccSmsIntMgr is null for Subscription:" + i);
        return false;
    }

    public int getPremiumSmsPermission(String str) {
        enforceTelephonyFeatureWithException(str, "getPremiumSmsPermission");
        return getPremiumSmsPermissionForSubscriber(getPreferredSmsSubscription(), str);
    }

    public int getPremiumSmsPermissionForSubscriber(int i, String str) {
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.getPremiumSmsPermission(str);
        }
        Rlog.e("SmsController", "getPremiumSmsPermissionForSubscriber iccSmsIntMgr is null");
        return 0;
    }

    public void setPremiumSmsPermission(String str, int i) {
        enforceTelephonyFeatureWithException(str, "setPremiumSmsPermission");
        setPremiumSmsPermissionForSubscriber(getPreferredSmsSubscription(), str, i);
    }

    public void setPremiumSmsPermissionForSubscriber(int i, String str, int i2) {
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.setPremiumSmsPermission(str, i2);
        } else {
            Rlog.e("SmsController", "setPremiumSmsPermissionForSubscriber iccSmsIntMgr is null");
        }
    }

    public boolean isImsSmsSupportedForSubscriber(int i) {
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.isImsSmsSupported();
        }
        Rlog.e("SmsController", "isImsSmsSupportedForSubscriber iccSmsIntMgr is null");
        return false;
    }

    public boolean isSmsSimPickActivityNeeded(int i) {
        Context applicationContext = this.mContext.getApplicationContext();
        ActivityManager activityManager = (ActivityManager) applicationContext.getSystemService(ActivityManager.class);
        if (activityManager == null || activityManager.getUidImportance(Binder.getCallingUid()) != 100) {
            Rlog.d("SmsController", "isSmsSimPickActivityNeeded: calling process not foreground. Suppressing activity.");
            return false;
        }
        enforceTelephonyFeatureWithException(getCallingPackage(), "isSmsSimPickActivityNeeded");
        TelephonyManager telephonyManager = (TelephonyManager) applicationContext.getSystemService("phone");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            List<SubscriptionInfo> activeSubscriptionInfoList = SubscriptionManager.from(applicationContext).getActiveSubscriptionInfoList();
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            if (activeSubscriptionInfoList != null) {
                int size = activeSubscriptionInfoList.size();
                for (int i2 = 0; i2 < size; i2++) {
                    SubscriptionInfo subscriptionInfo = activeSubscriptionInfoList.get(i2);
                    if (subscriptionInfo != null && subscriptionInfo.getSubscriptionId() == i) {
                        return false;
                    }
                }
                if (size > 1 && telephonyManager.getSimCount() > 1) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public String getImsSmsFormatForSubscriber(int i) {
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.getImsSmsFormat();
        }
        Rlog.e("SmsController", "getImsSmsFormatForSubscriber iccSmsIntMgr is null");
        return null;
    }

    public void injectSmsPduForSubscriber(int i, byte[] bArr, String str, PendingIntent pendingIntent) {
        enforceTelephonyFeatureWithException(getCallingPackage(), "injectSmsPduForSubscriber");
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.injectSmsPdu(bArr, str, pendingIntent);
        } else {
            Rlog.e("SmsController", "injectSmsPduForSubscriber iccSmsIntMgr is null");
            sendErrorInPendingIntent(pendingIntent, 2);
        }
    }

    public int getPreferredSmsSubscription() {
        int defaultSmsSubId = SubscriptionManagerService.getInstance().getDefaultSmsSubId();
        if (SubscriptionManager.isValidSubscriptionId(defaultSmsSubId)) {
            return defaultSmsSubId;
        }
        enforceTelephonyFeatureWithException(getCallingPackage(), "getPreferredSmsSubscription");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int[] activeSubIdList = SubscriptionManagerService.getInstance().getActiveSubIdList(true);
            if (activeSubIdList.length == 1) {
                return activeSubIdList[0];
            }
            return -1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isSMSPromptEnabled() {
        return PhoneFactory.isSMSPromptEnabled();
    }

    public void sendStoredText(int i, String str, String str2, Uri uri, String str3, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws Throwable {
        logStoredMessageApiUsage(false);
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        UserHandle callingUserHandle = Binder.getCallingUserHandle();
        int callingUid = Binder.getCallingUid();
        String callingPackage = getCallingPackage();
        Rlog.d("SmsController", "sendStoredText caller=" + callingPackage);
        if (TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mContext, i, callingUserHandle, getDestAddress(iccSmsInterfaceManager, uri))) {
            if (iccSmsInterfaceManager != null) {
                iccSmsInterfaceManager.sendStoredText(callingPackage, callingUserHandle.getIdentifier(), str2, uri, str3, pendingIntent, pendingIntent2, callingUid);
                return;
            }
            Rlog.e("SmsController", "sendStoredText iccSmsIntMgr is null for subscription: " + i);
            sendErrorInPendingIntent(pendingIntent, 1);
            return;
        }
        TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(this.mContext, i, callingUid, callingPackage);
        Rlog.d("SmsController", "sendStoredText: user is not associated with subscription subId=" + i + " callingUser=" + callingUserHandle);
        sendErrorInPendingIntent(pendingIntent, 33);
    }

    public void sendStoredMultipartText(int i, String str, String str2, Uri uri, String str3, List<PendingIntent> list, List<PendingIntent> list2) throws Throwable {
        logStoredMessageApiUsage(true);
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        UserHandle callingUserHandle = Binder.getCallingUserHandle();
        int callingUid = Binder.getCallingUid();
        String callingPackage = getCallingPackage();
        Rlog.d("SmsController", "sendStoredMultipartText caller=" + callingPackage);
        if (TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mContext, i, callingUserHandle, getDestAddress(iccSmsInterfaceManager, uri))) {
            if (iccSmsInterfaceManager != null) {
                iccSmsInterfaceManager.sendStoredMultipartText(callingPackage, callingUserHandle.getIdentifier(), str2, uri, str3, list, list2, callingUid);
                return;
            }
            Rlog.e("SmsController", "sendStoredMultipartText iccSmsIntMgr is null for subscription: " + i);
            sendErrorInPendingIntents(list, 1);
            return;
        }
        TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(this.mContext, i, callingUid, callingPackage);
        Rlog.d("SmsController", "sendStoredMultipartText: user is not associated with subscription subId=" + i + " callingUser=" + callingUserHandle);
        sendErrorInPendingIntents(list, 33);
    }

    private String getDestAddress(IccSmsInterfaceManager iccSmsInterfaceManager, Uri uri) throws Throwable {
        if (iccSmsInterfaceManager == null) {
            Rlog.d("SmsController", "getDestAddress - iccSmsIntMgr is null");
            return null;
        }
        String[] strArrLoadTextAndAddress = iccSmsInterfaceManager.loadTextAndAddress(this.mContext.getContentResolver(), uri);
        if (strArrLoadTextAndAddress == null) {
            Rlog.d("SmsController", "getDestAddress - textAndAddress is null");
            return null;
        }
        return strArrLoadTextAndAddress[1];
    }

    public Bundle getCarrierConfigValuesForSubscriber(int i) {
        enforceTelephonyFeatureWithException(getCallingPackage(), "getCarrierConfigValuesForSubscriber");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getMmsConfig(((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfigForSubId(i));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private static Bundle getMmsConfig(BaseBundle baseBundle) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("enabledTransID", baseBundle.getBoolean("enabledTransID"));
        bundle.putBoolean("enabledMMS", baseBundle.getBoolean("enabledMMS"));
        bundle.putBoolean("enableGroupMms", baseBundle.getBoolean("enableGroupMms"));
        bundle.putBoolean("enabledNotifyWapMMSC", baseBundle.getBoolean("enabledNotifyWapMMSC"));
        bundle.putBoolean("aliasEnabled", baseBundle.getBoolean("aliasEnabled"));
        bundle.putBoolean("allowAttachAudio", baseBundle.getBoolean("allowAttachAudio"));
        bundle.putBoolean("enableMultipartSMS", baseBundle.getBoolean("enableMultipartSMS"));
        bundle.putBoolean("enableSMSDeliveryReports", baseBundle.getBoolean("enableSMSDeliveryReports"));
        bundle.putBoolean("supportMmsContentDisposition", baseBundle.getBoolean("supportMmsContentDisposition"));
        bundle.putBoolean("sendMultipartSmsAsSeparateMessages", baseBundle.getBoolean("sendMultipartSmsAsSeparateMessages"));
        bundle.putBoolean("enableMMSReadReports", baseBundle.getBoolean("enableMMSReadReports"));
        bundle.putBoolean("enableMMSDeliveryReports", baseBundle.getBoolean("enableMMSDeliveryReports"));
        bundle.putBoolean("mmsCloseConnection", baseBundle.getBoolean("mmsCloseConnection"));
        bundle.putInt("maxMessageSize", baseBundle.getInt("maxMessageSize"));
        bundle.putInt("maxImageWidth", baseBundle.getInt("maxImageWidth"));
        bundle.putInt("maxImageHeight", baseBundle.getInt("maxImageHeight"));
        bundle.putInt("recipientLimit", baseBundle.getInt("recipientLimit"));
        bundle.putInt("aliasMinChars", baseBundle.getInt("aliasMinChars"));
        bundle.putInt("aliasMaxChars", baseBundle.getInt("aliasMaxChars"));
        bundle.putInt("smsToMmsTextThreshold", baseBundle.getInt("smsToMmsTextThreshold"));
        bundle.putInt("smsToMmsTextLengthThreshold", baseBundle.getInt("smsToMmsTextLengthThreshold"));
        bundle.putInt("maxMessageTextSize", baseBundle.getInt("maxMessageTextSize"));
        bundle.putInt("maxSubjectLength", baseBundle.getInt("maxSubjectLength"));
        bundle.putInt("httpSocketTimeout", baseBundle.getInt("httpSocketTimeout"));
        bundle.putString("uaProfTagName", baseBundle.getString("uaProfTagName"));
        bundle.putString("userAgent", baseBundle.getString("userAgent"));
        bundle.putString("uaProfUrl", baseBundle.getString("uaProfUrl"));
        bundle.putString("httpParams", baseBundle.getString("httpParams"));
        bundle.putString("emailGatewayNumber", baseBundle.getString("emailGatewayNumber"));
        bundle.putString("naiSuffix", baseBundle.getString("naiSuffix"));
        bundle.putBoolean("config_cellBroadcastAppLinks", baseBundle.getBoolean("config_cellBroadcastAppLinks"));
        bundle.putBoolean("supportHttpCharsetHeader", baseBundle.getBoolean("supportHttpCharsetHeader"));
        bundle.putInt("mms_max_ntn_payload_size_bytes_int", baseBundle.getInt("mms_max_ntn_payload_size_bytes_int"));
        bundle.putInt("mms_network_release_timeout_millis_int", baseBundle.getInt("mms_network_release_timeout_millis_int"));
        return bundle;
    }

    public String createAppSpecificSmsTokenWithPackageInfo(int i, String str, String str2, PendingIntent pendingIntent) {
        if (str == null) {
            str = getCallingPackage();
        }
        enforceTelephonyFeatureWithException(str, "createAppSpecificSmsTokenWithPackageInfo");
        return getPhone(i).getAppSmsManager().createAppSpecificSmsTokenWithPackageInfo(i, str, str2, pendingIntent);
    }

    public String createAppSpecificSmsToken(int i, String str, PendingIntent pendingIntent) {
        if (str == null) {
            str = getCallingPackage();
        }
        enforceTelephonyFeatureWithException(str, "createAppSpecificSmsToken");
        return getPhone(i).getAppSmsManager().createAppSpecificSmsToken(str, pendingIntent);
    }

    public void setStorageMonitorMemoryStatusOverride(int i, boolean z) {
        Phone phone = getPhone(i);
        if (phone == null) {
            Rlog.e("SmsController", "Phone Object is Null");
            return;
        }
        if (phone.getContext().checkPermission("android.permission.MODIFY_PHONE_STATE", Binder.getCallingPid(), Binder.getCallingUid()) != 0) {
            SmsController$$ExternalSyntheticBUOutline0.m("setStorageMonitorMemoryStatusOverride needs MODIFY_PHONE_STATE");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            phone.mSmsStorageMonitor.sendMemoryStatusOverride(z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void clearStorageMonitorMemoryStatusOverride(int i) {
        Phone phone = getPhone(i);
        if (phone == null) {
            Rlog.e("SmsController", "Phone Object is Null");
            return;
        }
        if (phone.getContext().checkPermission("android.permission.MODIFY_PHONE_STATE", Binder.getCallingPid(), Binder.getCallingUid()) != 0) {
            SmsController$$ExternalSyntheticBUOutline0.m("clearStorageMonitorMemoryStatusOverride needs MODIFY_PHONE_STATE");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            phone.mSmsStorageMonitor.clearMemoryStatusOverride();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int checkSmsShortCodeDestination(int i, String str, String str2, String str3, String str4) {
        if (str == null) {
            str = getCallingPackage();
        }
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(getPhone(i).getContext(), i, str, str2, "checkSmsShortCodeDestination")) {
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getPhone(i).mSmsUsageMonitor.checkDestination(str3, str4);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void sendVisualVoicemailSmsForSubscriber(String str, int i, String str2, int i2, String str3, int i3, String str4, PendingIntent pendingIntent) {
        Rlog.d("SmsController", "sendVisualVoicemailSmsForSubscriber caller=" + str);
        if (getPhone(i2).isInEcm()) {
            Rlog.d("SmsController", "sendVisualVoicemailSmsForSubscriber: Do not send non-emergency SMS in ECBM as it forces device to exit ECBM.");
            return;
        }
        int callingUid = Binder.getCallingUid();
        if (!TelephonyPermissions.checkSubscriptionAssociatedWithUser(this.mContext, i2, Binder.getCallingUserHandle(), str3)) {
            TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(this.mContext, i2, callingUid, str);
            sendErrorInPendingIntent(pendingIntent, 33);
        } else if (i3 == 0) {
            sendTextForSubscriberWithSelfPermissionsInternal(i2, str, i, str2, str3, null, str4, pendingIntent, null, false, true, callingUid);
        } else {
            sendDataForSubscriberWithSelfPermissionsInternal(i2, str, i, str2, str3, null, (short) i3, str4.getBytes(StandardCharsets.UTF_8), pendingIntent, null, true, callingUid);
        }
    }

    public String getSmscAddressFromIccEfForSubscriber(int i, String str) {
        if (str == null) {
            str = getCallingPackage();
        }
        enforceTelephonyFeatureWithException(str, "getSmscAddressFromIccEfForSubscriber");
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.getSmscAddressFromIccEf(str);
        }
        Rlog.e("SmsController", "getSmscAddressFromIccEfForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        return null;
    }

    public boolean setSmscAddressOnIccEfForSubscriber(String str, int i, String str2) {
        if (str2 == null) {
            str2 = getCallingPackage();
        }
        enforceTelephonyFeatureWithException(str2, "setSmscAddressOnIccEfForSubscriber");
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.setSmscAddressOnIccEf(str2, str);
        }
        Rlog.e("SmsController", "setSmscAddressOnIccEfForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        return false;
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (TelephonyUtils.checkDumpPermission(this.mContext, "SmsController", printWriter)) {
            IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "    ");
            for (Phone phone : PhoneFactory.getPhones()) {
                int subId = phone.getSubId();
                indentingPrintWriter.println(String.format("SmsManager for subId = %d:", Integer.valueOf(subId)));
                indentingPrintWriter.increaseIndent();
                if (getIccSmsInterfaceManager(subId) != null) {
                    getIccSmsInterfaceManager(subId).dump(fileDescriptor, indentingPrintWriter, strArr);
                }
                indentingPrintWriter.decreaseIndent();
            }
            indentingPrintWriter.flush();
        }
    }

    private void sendErrorInPendingIntent(PendingIntent pendingIntent, int i) {
        if (pendingIntent != null) {
            try {
                pendingIntent.send(i);
            } catch (PendingIntent.CanceledException unused) {
            }
        }
    }

    private void sendErrorInPendingIntents(List<PendingIntent> list, int i) {
        if (list == null) {
            return;
        }
        Iterator<PendingIntent> it = list.iterator();
        while (it.hasNext()) {
            sendErrorInPendingIntent(it.next(), i);
        }
    }

    private IccSmsInterfaceManager getIccSmsInterfaceManager(int i) {
        return getPhone(i).getIccSmsInterfaceManager();
    }

    private SubscriptionInfo getSubscriptionInfo(int i) {
        return ((SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service")).getActiveSubscriptionInfo(i);
    }

    public int getSmsCapacityOnIccForSubscriber(int i) {
        enforceTelephonyFeatureWithException(getCallingPackage(), "getSmsCapacityOnIccForSubscriber");
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            return iccSmsInterfaceManager.getSmsCapacityOnIcc(getCallingPackage(), null);
        }
        Rlog.e("SmsController", "iccSmsIntMgr is null for  subId: " + i);
        return 0;
    }

    public boolean resetAllCellBroadcastRanges(int i) {
        enforceTelephonyFeatureWithException(getCallingPackage(), "resetAllCellBroadcastRanges");
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            iccSmsInterfaceManager.resetAllCellBroadcastRanges();
            return true;
        }
        Rlog.e("SmsController", "iccSmsIntMgr is null for  subId: " + i);
        return false;
    }

    public static String formatCrossStackMessageId(long j) {
        return "{x-message-id:" + j + "}";
    }

    public boolean isNumberBlockedByFDN(int i, String str, String str2) {
        int phoneId = SubscriptionManager.getPhoneId(i);
        if (!FdnUtils.isFdnEnabled(phoneId) || !TelephonyCapabilities.supportsTelephonyCalling(this.mFlags, this.mContext)) {
            return false;
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        if (telephonyManager != null && telephonyManager.isEmergencyNumber(str)) {
            return false;
        }
        String simOperator = telephonyManager.getSimOperator(i);
        if ("334020".equals(simOperator) || "33420".equals(simOperator)) {
            Rlog.d("SmsController", "Skip FDN check for Telcel operator");
            return false;
        }
        if (this.mIHyperTeleSmsManager.isFdnBlockDisabled(this.mContext, i)) {
            return false;
        }
        String upperCase = telephonyManager.getSimCountryIso().toUpperCase(Locale.ENGLISH);
        if (FdnUtils.isNumberBlockedByFDN(phoneId, str, upperCase)) {
            try {
                if (MiuiUiccControllerStub.get(phoneId).isNumberBlockedByFDNMiui(phoneId, str, upperCase)) {
                    return true;
                }
            } catch (Exception e) {
                Rlog.e("SmsController", "IllegalStateException， ex:" + e);
                return true;
            }
        }
        IccSmsInterfaceManager iccSmsInterfaceManager = getIccSmsInterfaceManager(i);
        if (iccSmsInterfaceManager != null) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                String smscAddressFromIccEf = iccSmsInterfaceManager.getSmscAddressFromIccEf(str2);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                try {
                    if (!MiuiUiccControllerStub.get(phoneId).isNumberBlockedByFDNMiui(phoneId, smscAddressFromIccEf, upperCase)) {
                        return false;
                    }
                } catch (Exception e2) {
                    Rlog.e("SmsController", "IllegalStateException， ex:" + e2);
                }
                return FdnUtils.isNumberBlockedByFDN(phoneId, smscAddressFromIccEf, upperCase);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        Rlog.e("SmsController", "getSmscAddressFromIccEfForSubscriber iccSmsIntMgr is null for Subscription: " + i);
        return true;
    }

    public long getWapMessageSize(String str) {
        return WapPushCache.getWapMessageSize(str.getBytes(StandardCharsets.ISO_8859_1));
    }

    private void enforceTelephonyFeatureWithException(String str, String str2) {
        if (SystemProperties.getBoolean("ro.radio.noril", false)) {
            return;
        }
        TelephonyUtils.enforceTelephonyFeatureWithException(str, this.mPackageManager, this.mVendorApiLevel, "android.hardware.telephony.messaging", str2);
    }

    private void logStoredMessageApiUsage(boolean z) {
        TelephonyStatsLog.write(1350, Binder.getCallingUid(), z ? 2 : 1);
    }
}
