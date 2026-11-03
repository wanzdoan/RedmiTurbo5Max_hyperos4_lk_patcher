package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelableException;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.os.TelephonyServiceManager;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyFrameworkInitializer;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IsimRecords;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class PhoneSubInfoController extends IPhoneSubInfo.Stub {
    private static PhoneSubInfoController sInstance;
    private AppOpsManager mAppOps;
    private final Context mContext;
    private FeatureFlags mFeatureFlags;
    private PackageManager mPackageManager;
    private final int mVendorApiLevel;

    /* JADX INFO: Access modifiers changed from: private */
    interface CallPhoneMethodHelper<T> {
        T callMethod(Phone phone);
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface PermissionCheckHelper {
        boolean checkPermission(Context context, int i, String str, String str2, String str3);
    }

    public static PhoneSubInfoController init(Context context) {
        synchronized (PhoneSubInfoController.class) {
            try {
                if (sInstance == null) {
                    sInstance = new PhoneSubInfoController(context);
                    TelephonyServiceManager.ServiceRegisterer phoneSubServiceRegisterer = TelephonyFrameworkInitializer.getTelephonyServiceManager().getPhoneSubServiceRegisterer();
                    if (phoneSubServiceRegisterer.get() == null) {
                        phoneSubServiceRegisterer.register(sInstance);
                    }
                } else {
                    Log.wtf("PhoneSubInfoController", "PhoneSubInfoController is already initialized.");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return sInstance;
    }

    public PhoneSubInfoController(Context context) {
        this(context, new FeatureFlagsImpl());
    }

    public PhoneSubInfoController(Context context, FeatureFlags featureFlags) {
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mFeatureFlags = featureFlags;
        this.mVendorApiLevel = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
    }

    @Deprecated
    public String getDeviceId(String str) {
        return getDeviceIdWithFeature(str, null);
    }

    public String getDeviceIdWithFeature(String str, String str2) {
        return getDeviceIdForPhone(SubscriptionManager.getPhoneId(getDefaultSubscription()), str, str2);
    }

    public String getDeviceIdForPhone(int i, String str, String str2) {
        enforceCallingPackageUidMatched(str);
        return (String) callPhoneMethodForPhoneIdWithReadDeviceIdentifiersCheck(i, str, str2, "getDeviceId", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda11
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return phone.getDeviceId();
            }
        });
    }

    public String getNaiForSubscriber(int i, final String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadSubscriberIdentifiersCheck(i, str, str2, "getNai", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda17
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getNaiForSubscriber$1(str, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getNaiForSubscriber$1(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getNaiForSubscriber");
        return phone.getNai();
    }

    public String getImeiForSubscriber(int i, String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadDeviceIdentifiersCheck(i, str, str2, "getImei", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda21
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return phone.getImei();
            }
        });
    }

    public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int i, final int i2, final String str) {
        return (ImsiEncryptionInfo) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getCarrierInfoForImsiEncryption", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda20
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getCarrierInfoForImsiEncryption$3(str, i2, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ImsiEncryptionInfo lambda$getCarrierInfoForImsiEncryption$3(String str, int i, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getCarrierInfoForImsiEncryption");
        return phone.getCarrierInfoForImsiEncryption(i, true);
    }

    public void setCarrierInfoForImsiEncryption(int i, String str, final ImsiEncryptionInfo imsiEncryptionInfo) {
        callPhoneMethodForSubIdWithModifyCheck(i, str, "setCarrierInfoForImsiEncryption", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda5
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return PhoneSubInfoController.$r8$lambda$e3Bsu0mofAg6jQzKS_cpG8XojnA(imsiEncryptionInfo, phone);
            }
        });
    }

    public static /* synthetic */ Object $r8$lambda$e3Bsu0mofAg6jQzKS_cpG8XojnA(ImsiEncryptionInfo imsiEncryptionInfo, Phone phone) {
        phone.setCarrierInfoForImsiEncryption(imsiEncryptionInfo, true);
        return null;
    }

    public void resetCarrierKeysForImsiEncryption(int i, final String str) {
        callPhoneMethodForSubIdWithModifyCheck(i, str, "resetCarrierKeysForImsiEncryption", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda15
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$resetCarrierKeysForImsiEncryption$5(str, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object lambda$resetCarrierKeysForImsiEncryption$5(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "resetCarrierKeysForImsiEncryption");
        phone.resetCarrierKeysForImsiEncryption();
        return null;
    }

    public String getDeviceSvn(String str, String str2) {
        return getDeviceSvnUsingSubId(getDefaultSubscription(), str, str2);
    }

    public String getDeviceSvnUsingSubId(int i, String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadCheck(i, str, str2, "getDeviceSvn", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda18
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return phone.getDeviceSvn();
            }
        });
    }

    @Deprecated
    public String getSubscriberId(String str) {
        TelephonyCommonUtilsStub.get().localLog("getSubscriberId", " callingPackage = " + str);
        return getSubscriberIdWithFeature(str, null);
    }

    public String getSubscriberIdWithFeature(String str, String str2) {
        return getSubscriberIdForSubscriber(getDefaultSubscription(), str, str2);
    }

    public String getSubscriberIdForSubscriber(int i, final String str, String str2) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            boolean zIsActiveSubId = SubscriptionManagerService.getInstance().isActiveSubId(i, str, str2);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            if (zIsActiveSubId) {
                return (String) callPhoneMethodForSubIdWithReadSubscriberIdentifiersCheck(i, str, str2, "getSubscriberIdForSubscriber", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda27
                    @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
                    public final Object callMethod(Phone phone) {
                        return this.f$0.lambda$getSubscriberIdForSubscriber$7(str, phone);
                    }
                });
            }
            if (!TelephonyPermissions.checkCallingOrSelfReadSubscriberIdentifiers(this.mContext, i, str, str2, "getSubscriberIdForSubscriber")) {
                return null;
            }
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getSubscriberIdForSubscriber");
            long jClearCallingIdentity2 = Binder.clearCallingIdentity();
            try {
                SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(i);
                if (subscriptionInfoInternal == null || TextUtils.isEmpty(subscriptionInfoInternal.getImsi())) {
                    return null;
                }
                return subscriptionInfoInternal.getImsi();
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity2);
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getSubscriberIdForSubscriber$7(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getSubscriberIdForSubscriber");
        return phone.getSubscriberId();
    }

    @Deprecated
    public String getIccSerialNumber(String str) {
        return getIccSerialNumberWithFeature(str, null);
    }

    public String getIccSerialNumberWithFeature(String str, String str2) {
        return getIccSerialNumberForSubscriber(getDefaultSubscription(), str, str2);
    }

    public String getIccSerialNumberForSubscriber(int i, final String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadSubscriberIdentifiersCheck(i, str, str2, "getIccSerialNumber", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda10
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getIccSerialNumberForSubscriber$8(str, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getIccSerialNumberForSubscriber$8(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getIccSerialNumberForSubscriber");
        return phone.getIccSerialNumber();
    }

    public String getLine1Number(String str, String str2) {
        return getLine1NumberForSubscriber(getDefaultSubscription(), str, str2);
    }

    public String getLine1NumberForSubscriber(int i, final String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadPhoneNumberCheck(i, str, str2, "getLine1Number", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda12
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getLine1NumberForSubscriber$9(str, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getLine1NumberForSubscriber$9(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getLine1NumberForSubscriber");
        return phone.getLine1Number();
    }

    public String getLine1AlphaTag(String str, String str2) {
        return getLine1AlphaTagForSubscriber(getDefaultSubscription(), str, str2);
    }

    public String getLine1AlphaTagForSubscriber(int i, String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadCheck(i, str, str2, "getLine1AlphaTag", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda9
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return phone.getLine1AlphaTag();
            }
        });
    }

    public String getMsisdn(String str, String str2) {
        return getMsisdnForSubscriber(getDefaultSubscription(), str, str2);
    }

    public String getMsisdnForSubscriber(int i, String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadPhoneNumberCheck(i, str, str2, "getMsisdn", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda29
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return phone.getMsisdn();
            }
        });
    }

    public String getVoiceMailNumber(String str, String str2) {
        return getVoiceMailNumberForSubscriber(getDefaultSubscription(), str, str2);
    }

    public String getVoiceMailNumberForSubscriber(int i, final String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadCheck(i, str, str2, "getVoiceMailNumber", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda7
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getVoiceMailNumberForSubscriber$12(str, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getVoiceMailNumberForSubscriber$12(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "getVoiceMailNumberForSubscriber");
        return PhoneNumberUtils.extractNetworkPortion(phone.getVoiceMailNumber());
    }

    public String getVoiceMailAlphaTag(String str, String str2) {
        return getVoiceMailAlphaTagForSubscriber(getDefaultSubscription(), str, str2);
    }

    public String getVoiceMailAlphaTagForSubscriber(int i, final String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadCheck(i, str, str2, "getVoiceMailAlphaTag", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda28
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getVoiceMailAlphaTagForSubscriber$13(str, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getVoiceMailAlphaTagForSubscriber$13(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.calling", "getVoiceMailAlphaTagForSubscriber");
        return phone.getVoiceMailAlphaTag();
    }

    private Phone getPhone(int i) {
        int phoneId = SubscriptionManager.getPhoneId(i);
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            return PhoneFactory.getPhone(phoneId);
        }
        return null;
    }

    private void enforceCallingPackageUidMatched(String str) {
        try {
            this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        } catch (SecurityException e) {
            EventLog.writeEvent(1397638484, "188677422", Integer.valueOf(Binder.getCallingUid()));
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enforceIccSimChallengeResponsePermission(Context context, int i, String str, String str2, String str3) {
        if (TelephonyPermissions.checkCallingOrSelfUseIccAuthWithDeviceIdentifier(context, str, str2, str3)) {
            logStackTrace("granted by UseIccAuthWithDeviceId");
            return true;
        }
        try {
            try {
                enforceUseIccAuthPermissionOrCarrierPrivilege(i, str3);
                logStackTrace("granted by UseIccAuth or Carrier");
                return true;
            } catch (SecurityException unused) {
                enforcePrivilegedPermissionOrCarrierPrivilege(i, str3);
                logStackTrace("granted by privPermissionOrCarrier");
                return true;
            }
        } catch (SecurityException e) {
            logStackTrace("not granted");
            throw e;
        }
    }

    private void logStackTrace(String str) {
        Log.e("PhoneSubInfoController", str + " callingUID:" + Binder.getCallingUid(), new Exception());
    }

    private void enforceUseIccAuthPermissionOrCarrierPrivilege(int i, String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.USE_ICC_AUTH") == 0) {
            return;
        }
        TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mContext, i, str);
    }

    private void enforcePrivilegedPermissionOrCarrierPrivilege(int i, String str) {
        if (TelephonyCommonUtilsStub.get().checkCallingOrSelfPermissionGranted(Binder.getCallingUid()) || this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") == 0) {
            return;
        }
        TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mContext, i, str);
    }

    private void enforceModifyPermission() {
        if (TelephonyCommonUtilsStub.get().checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", "Requires MODIFY_PHONE_STATE");
    }

    private int getDefaultSubscription() {
        return PhoneFactory.getDefaultSubscription();
    }

    public String getIsimImpi(int i) {
        return (String) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getIsimImpi", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda6
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return PhoneSubInfoController.$r8$lambda$QLcP_6KbklBCVBOzZ24lrh0w_D0(phone);
            }
        });
    }

    public static /* synthetic */ String $r8$lambda$QLcP_6KbklBCVBOzZ24lrh0w_D0(Phone phone) {
        IsimRecords isimRecords = phone.getIsimRecords();
        if (isimRecords != null) {
            return isimRecords.getIsimImpi();
        }
        return null;
    }

    public String getImsPrivateUserIdentity(int i, String str, String str2) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            RILUtils$$ExternalSyntheticBUOutline0.m("Invalid SubscriptionID  = ", i);
            return null;
        }
        if (!TelephonyPermissions.checkCallingOrSelfUseIccAuthWithDeviceIdentifier(this.mContext, str, str2, "getImsPrivateUserIdentity")) {
            SmsController$$ExternalSyntheticBUOutline0.m("No permissions to the caller");
            return null;
        }
        IsimRecords isimRecords = getPhone(i).getIsimRecords();
        if (isimRecords != null) {
            return isimRecords.getIsimImpi();
        }
        SomeArgs$$ExternalSyntheticBUOutline0.m("ISIM is not loaded");
        return null;
    }

    public String getIsimDomain(int i) {
        return (String) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getIsimDomain", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda4
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getIsimDomain$15(phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getIsimDomain$15(Phone phone) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getIsimDomain");
        IsimRecords isimRecords = phone.getIsimRecords();
        if (isimRecords != null) {
            return isimRecords.getIsimDomain();
        }
        return null;
    }

    public String[] getIsimImpu(int i) {
        return (String[]) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getIsimImpu", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda0
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return PhoneSubInfoController.m304$r8$lambda$D3ob_ZCvMFQW49byqEBuMpBFpA(phone);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$D3ob_ZCvMF-QW49byqEBuMpBFpA, reason: not valid java name */
    public static /* synthetic */ String[] m304$r8$lambda$D3ob_ZCvMFQW49byqEBuMpBFpA(Phone phone) {
        IsimRecords isimRecords = phone.getIsimRecords();
        if (isimRecords != null) {
            return isimRecords.getIsimImpu();
        }
        return null;
    }

    public List<Uri> getImsPublicUserIdentities(int i, String str) {
        String[] isimImpu;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            return new ArrayList();
        }
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mContext, i, "getImsPublicUserIdentities");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getImsPublicUserIdentities");
        IsimRecords isimRecords = getPhone(i).getIsimRecords();
        if (isimRecords != null && (isimImpu = isimRecords.getIsimImpu()) != null) {
            ArrayList arrayList = new ArrayList();
            for (String str2 : isimImpu) {
                if (str2 != null && str2.trim().length() > 0) {
                    arrayList.add(Uri.parse(str2));
                }
            }
            return arrayList;
        }
        SomeArgs$$ExternalSyntheticBUOutline0.m("ISIM is not loaded");
        return null;
    }

    public String getIsimIst(int i) throws RemoteException {
        return (String) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getIsimIst", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda19
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getIsimIst$17(phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getIsimIst$17(Phone phone) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getIsimIst");
        IsimRecords isimRecords = phone.getIsimRecords();
        if (isimRecords != null) {
            return isimRecords.getIsimIst();
        }
        return null;
    }

    public String[] getIsimPcscf(int i) throws RemoteException {
        return (String[]) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getIsimPcscf", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda23
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return PhoneSubInfoController.$r8$lambda$yGfwbPXQanq8iyiiJ92d39dODO8(phone);
            }
        });
    }

    public static /* synthetic */ String[] $r8$lambda$yGfwbPXQanq8iyiiJ92d39dODO8(Phone phone) {
        IsimRecords isimRecords = phone.getIsimRecords();
        if (isimRecords != null) {
            return isimRecords.getIsimPcscf();
        }
        return null;
    }

    public List<String> getImsPcscfAddresses(int i, String str) {
        String[] isimPcscf;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            RILUtils$$ExternalSyntheticBUOutline0.m("Invalid subscription: ", i);
            return null;
        }
        TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mContext, i, "getImsPcscfAddresses");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getImsPcscfAddresses");
        IsimRecords isimRecords = getPhone(i).getIsimRecords();
        if (isimRecords != null && (isimPcscf = isimRecords.getIsimPcscf()) != null) {
            return (List) Arrays.stream(isimPcscf).filter(new Predicate() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return PhoneSubInfoController.m314$r8$lambda$y6vfTbTgyTx85O07i6k4TL3vn8((String) obj);
                }
            }).map(new Function() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((String) obj).trim();
                }
            }).filter(new Predicate() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return PhoneSubInfoController.$r8$lambda$ziWz92ypg1tgwvLGNew_bzl7hPg((String) obj);
                }
            }).collect(Collectors.toList());
        }
        SomeArgs$$ExternalSyntheticBUOutline0.m("ISIM is not loaded");
        return null;
    }

    /* JADX INFO: renamed from: $r8$lambda$y6vfTbT-gyTx85O07i6k4TL3vn8, reason: not valid java name */
    public static /* synthetic */ boolean m314$r8$lambda$y6vfTbTgyTx85O07i6k4TL3vn8(String str) {
        return str != null;
    }

    public static /* synthetic */ boolean $r8$lambda$ziWz92ypg1tgwvLGNew_bzl7hPg(String str) {
        return str.length() > 0;
    }

    public String getSimServiceTable(int i, final int i2) throws RemoteException {
        return (String) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getSimServiceTable", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda30
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getSimServiceTable$22(i2, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getSimServiceTable$22(int i, Phone phone) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getSimServiceTable");
        UiccPort uiccPort = phone.getUiccPort();
        if (uiccPort == null || uiccPort.getUiccProfile() == null) {
            loge("getSimServiceTable(): uiccPort or uiccProfile is null");
            return null;
        }
        UiccCardApplication applicationByType = uiccPort.getUiccProfile().getApplicationByType(i);
        if (applicationByType == null) {
            loge("getSimServiceTable(): no app with specified apptype=" + i);
            return null;
        }
        return ((SIMRecords) applicationByType.getIccRecords()).getSimServiceTable();
    }

    public String getIccSimChallengeResponse(int i, final int i2, final int i3, final String str, final String str2, String str3) throws RemoteException {
        return (String) callPhoneMethodWithPermissionCheck(i, str2, str3, "getIccSimChallengeResponse", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda13
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getIccSimChallengeResponse$23(str2, i2, i3, str, phone);
            }
        }, new PermissionCheckHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda14
            @Override // com.android.internal.telephony.PhoneSubInfoController.PermissionCheckHelper
            public final boolean checkPermission(Context context, int i4, String str4, String str5, String str6) {
                return this.f$0.enforceIccSimChallengeResponsePermission(context, i4, str4, str5, str6);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getIccSimChallengeResponse$23(String str, int i, int i2, String str2, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getIccSimChallengeResponse");
        UiccPort uiccPort = phone.getUiccPort();
        if (uiccPort == null) {
            loge("getIccSimChallengeResponse() uiccPort is null");
            return null;
        }
        UiccCardApplication applicationByType = uiccPort.getApplicationByType(i);
        if (applicationByType == null) {
            loge("getIccSimChallengeResponse() no app with specified type -- " + i);
            return null;
        }
        loge("getIccSimChallengeResponse() found app " + applicationByType.getAid() + " specified type -- " + i);
        if (i2 != 128 && i2 != 129 && i2 != 132 && i2 != 133) {
            loge("getIccSimChallengeResponse() unsupported authType: " + i2);
            return null;
        }
        return applicationByType.getIccRecords().getIccSimChallengeResponse(i2, str2);
    }

    public String getGroupIdLevel1ForSubscriber(int i, final String str, String str2) {
        return (String) callPhoneMethodForSubIdWithReadCheck(i, str, str2, "getGroupIdLevel1", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda22
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getGroupIdLevel1ForSubscriber$24(str, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getGroupIdLevel1ForSubscriber$24(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getGroupIdLevel1ForSubscriber");
        return phone.getGroupIdLevel1();
    }

    public String getGroupIdLevel2ForSubscriber(int i, final String str, String str2) {
        return (String) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getGroupIdLevel2", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda8
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getGroupIdLevel2ForSubscriber$25(str, phone);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getGroupIdLevel2ForSubscriber$25(String str, Phone phone) {
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getGroupIdLevel2ForSubscriber");
        return phone.getGroupIdLevel2();
    }

    public void getUiccIari(int i, int i2, String str, ResultReceiver resultReceiver) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            callbackUiccIari(resultReceiver, null, new IllegalArgumentException("Invalid subscription: " + i));
            return;
        }
        try {
            TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mContext, i, "getUiccIari");
            enforceTelephonyFeatureWithException(str, "android.hardware.telephony.subscription", "getUiccIari");
            Phone phone = getPhone(i);
            if (phone == null) {
                loge("getUiccIari(): phone is null");
                callbackUiccIari(resultReceiver, Collections.EMPTY_LIST, null);
                return;
            }
            UiccPort uiccPort = phone.getUiccPort();
            if (uiccPort == null || uiccPort.getUiccProfile() == null) {
                loge("getUiccIari(): uiccPort or uiccProfile is null");
                callbackUiccIari(resultReceiver, Collections.EMPTY_LIST, null);
                return;
            }
            UiccCardApplication applicationByType = uiccPort.getUiccProfile().getApplicationByType(i2);
            if (applicationByType == null) {
                loge("getUiccIari(): no app with specified apptype=" + i2);
                callbackUiccIari(resultReceiver, Collections.EMPTY_LIST, null);
                return;
            }
            String[] uiccIari = applicationByType.getIccRecords().getUiccIari();
            if (uiccIari == null) {
                loge("getUiccIari(): IARI list is null");
                callbackUiccIari(resultReceiver, Collections.EMPTY_LIST, null);
            } else {
                callbackUiccIari(resultReceiver, (List) Arrays.stream(uiccIari).filter(new Predicate() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda24
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return PhoneSubInfoController.m308$r8$lambda$LwOUQymFrVO3F8Z5IZDCEwiX2k((String) obj);
                    }
                }).map(new Function() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda25
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((String) obj).trim();
                    }
                }).filter(new Predicate() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda26
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return PhoneSubInfoController.m312$r8$lambda$lNUzrwN3fi8kH2F5arMxxJrd10((String) obj);
                    }
                }).collect(Collectors.toList()), null);
            }
        } catch (SecurityException | UnsupportedOperationException e) {
            callbackUiccIari(resultReceiver, null, e);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$LwOU-QymFrVO3F8Z5IZDCEwiX2k, reason: not valid java name */
    public static /* synthetic */ boolean m308$r8$lambda$LwOUQymFrVO3F8Z5IZDCEwiX2k(String str) {
        return str != null;
    }

    /* JADX INFO: renamed from: $r8$lambda$lNUzrw-N3fi8kH2F5arMxxJrd10, reason: not valid java name */
    public static /* synthetic */ boolean m312$r8$lambda$lNUzrwN3fi8kH2F5arMxxJrd10(String str) {
        return str.length() > 0;
    }

    private void callbackUiccIari(ResultReceiver resultReceiver, List list, Exception exc) {
        Bundle bundle = new Bundle();
        if (exc != null) {
            bundle.putParcelable("uicc_iari_exception", new ParcelableException(exc));
        } else {
            bundle.putStringArrayList("uicc_iari_list", list == null ? new ArrayList<>(Collections.EMPTY_LIST) : new ArrayList<>(list));
        }
        resultReceiver.send(0, bundle);
    }

    private Object callPhoneMethodWithPermissionCheck(int i, String str, String str2, String str3, CallPhoneMethodHelper callPhoneMethodHelper, PermissionCheckHelper permissionCheckHelper) {
        if (!permissionCheckHelper.checkPermission(this.mContext, i, str, str2, str3)) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = getPhone(i);
            if (phone != null) {
                return callPhoneMethodHelper.callMethod(phone);
            }
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private Object callPhoneMethodForSubIdWithReadCheck(int i, String str, String str2, String str3, CallPhoneMethodHelper callPhoneMethodHelper) {
        return callPhoneMethodWithPermissionCheck(i, str, str2, str3, callPhoneMethodHelper, new PermissionCheckHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda33
            @Override // com.android.internal.telephony.PhoneSubInfoController.PermissionCheckHelper
            public final boolean checkPermission(Context context, int i2, String str4, String str5, String str6) {
                return TelephonyPermissions.checkCallingOrSelfReadPhoneState(context, i2, str4, str5, str6);
            }
        });
    }

    private Object callPhoneMethodForSubIdWithReadDeviceIdentifiersCheck(int i, String str, String str2, String str3, CallPhoneMethodHelper callPhoneMethodHelper) {
        return callPhoneMethodWithPermissionCheck(i, str, str2, str3, callPhoneMethodHelper, new PermissionCheckHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda31
            @Override // com.android.internal.telephony.PhoneSubInfoController.PermissionCheckHelper
            public final boolean checkPermission(Context context, int i2, String str4, String str5, String str6) {
                return TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, i2, str4, str5, str6);
            }
        });
    }

    private Object callPhoneMethodForSubIdWithReadSubscriberIdentifiersCheck(int i, String str, String str2, String str3, CallPhoneMethodHelper callPhoneMethodHelper) {
        return callPhoneMethodWithPermissionCheck(i, str, str2, str3, callPhoneMethodHelper, new PermissionCheckHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda34
            @Override // com.android.internal.telephony.PhoneSubInfoController.PermissionCheckHelper
            public final boolean checkPermission(Context context, int i2, String str4, String str5, String str6) {
                return TelephonyPermissions.checkCallingOrSelfReadSubscriberIdentifiers(context, i2, str4, str5, str6);
            }
        });
    }

    private Object callPhoneMethodForSubIdWithPrivilegedCheck(int i, final String str, CallPhoneMethodHelper callPhoneMethodHelper) {
        return callPhoneMethodWithPermissionCheck(i, null, null, str, callPhoneMethodHelper, new PermissionCheckHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda35
            @Override // com.android.internal.telephony.PhoneSubInfoController.PermissionCheckHelper
            public final boolean checkPermission(Context context, int i2, String str2, String str3, String str4) {
                return this.f$0.lambda$callPhoneMethodForSubIdWithPrivilegedCheck$32(str, context, i2, str2, str3, str4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$callPhoneMethodForSubIdWithPrivilegedCheck$32(String str, Context context, int i, String str2, String str3, String str4) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", str);
        return true;
    }

    private Object callPhoneMethodForSubIdWithModifyCheck(int i, String str, String str2, CallPhoneMethodHelper callPhoneMethodHelper) {
        return callPhoneMethodWithPermissionCheck(i, null, null, str2, callPhoneMethodHelper, new PermissionCheckHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda32
            @Override // com.android.internal.telephony.PhoneSubInfoController.PermissionCheckHelper
            public final boolean checkPermission(Context context, int i2, String str3, String str4, String str5) {
                return this.f$0.lambda$callPhoneMethodForSubIdWithModifyCheck$33(context, i2, str3, str4, str5);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$callPhoneMethodForSubIdWithModifyCheck$33(Context context, int i, String str, String str2, String str3) {
        enforceModifyPermission();
        return true;
    }

    private Object callPhoneMethodForSubIdWithReadPhoneNumberCheck(int i, String str, String str2, String str3, CallPhoneMethodHelper callPhoneMethodHelper) {
        return callPhoneMethodWithPermissionCheck(i, str, str2, str3, callPhoneMethodHelper, new PermissionCheckHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda36
            @Override // com.android.internal.telephony.PhoneSubInfoController.PermissionCheckHelper
            public final boolean checkPermission(Context context, int i2, String str4, String str5, String str6) {
                return TelephonyPermissions.checkCallingOrSelfReadPhoneNumber(context, i2, str4, str5, str6);
            }
        });
    }

    private Object callPhoneMethodForPhoneIdWithReadDeviceIdentifiersCheck(int i, String str, String str2, String str3, CallPhoneMethodHelper callPhoneMethodHelper) {
        if (!SubscriptionManager.isValidPhoneId(i)) {
            i = 0;
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || !TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(this.mContext, phone.getSubId(), str, str2, str3)) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return callPhoneMethodHelper.callMethod(phone);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public Uri getSmscIdentity(int i, final int i2) throws RemoteException {
        Uri uri = (Uri) callPhoneMethodForSubIdWithPrivilegedCheck(i, "getSmscIdentity", new CallPhoneMethodHelper() { // from class: com.android.internal.telephony.PhoneSubInfoController$$ExternalSyntheticLambda16
            @Override // com.android.internal.telephony.PhoneSubInfoController.CallPhoneMethodHelper
            public final Object callMethod(Phone phone) {
                return this.f$0.lambda$getSmscIdentity$35(i2, phone);
            }
        });
        if (uri != null) {
            return uri;
        }
        SomeArgs$$ExternalSyntheticBUOutline0.m("Telephony service error");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Uri lambda$getSmscIdentity$35(int i, Phone phone) {
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.subscription", "getSmscIdentity");
        try {
            UiccCardApplication applicationByType = phone.getUiccPort().getUiccProfile().getApplicationByType(i);
            String smscIdentity = applicationByType != null ? applicationByType.getIccRecords().getSmscIdentity() : null;
            if (TextUtils.isEmpty(smscIdentity)) {
                return Uri.EMPTY;
            }
            return Uri.parse(smscIdentity);
        } catch (NullPointerException e) {
            Rlog.e("PhoneSubInfoController", "getSmscIdentity(): Exception = " + e);
            return null;
        }
    }

    private String getCurrentPackageName() {
        String[] packagesForUid;
        PackageManager packageManager = this.mContext.createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager();
        if (packageManager == null || (packagesForUid = packageManager.getPackagesForUid(Binder.getCallingUid())) == null) {
            return null;
        }
        return packagesForUid[0];
    }

    private void enforceTelephonyFeatureWithException(String str, String str2, String str3) {
        if (SystemProperties.getBoolean("ro.radio.noril", false)) {
            return;
        }
        TelephonyUtils.enforceTelephonyFeatureWithException(str, this.mPackageManager, this.mVendorApiLevel, str2, str3);
    }

    private void loge(String str) {
        Rlog.e("PhoneSubInfoController", str);
    }
}
