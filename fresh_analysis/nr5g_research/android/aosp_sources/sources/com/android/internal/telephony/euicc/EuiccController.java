package com.android.internal.telephony.euicc;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.compat.CompatChanges;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.service.euicc.DownloadSubscriptionResult;
import android.service.euicc.GetDefaultDownloadableSubscriptionListResult;
import android.service.euicc.GetDownloadableSubscriptionMetadataResult;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.telephony.AnomalyReporter;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyFrameworkInitializer;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;
import android.telephony.UiccSlotInfo;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CarrierPrivilegesTracker;
import com.android.internal.telephony.CarrierResolver;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SmsController$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.util.TelephonyUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class EuiccController extends IEuiccController.Stub {
    static final String EXTRA_OPERATION = "operation";
    private static EuiccController sInstance;
    private final AppOpsManager mAppOpsManager;
    private final EuiccConnector mConnector;
    private final Context mContext;
    private final FeatureFlags mFeatureFlags;
    private final PackageManager mPackageManager;
    private List mPsimConversionSupportedCarrierIds;
    private final SubscriptionManager mSubscriptionManager;
    private List mSupportedCountries;
    private final TelephonyManager mTelephonyManager;
    private List mUnsupportedCountries;
    private final int mVendorApiLevel;

    public static EuiccController init(Context context, FeatureFlags featureFlags) {
        synchronized (EuiccController.class) {
            try {
                if (sInstance != null) {
                    Log.wtf("EuiccController", "init() called multiple times! sInstance = " + sInstance);
                } else {
                    sInstance = new EuiccController(context, featureFlags);
                    TelephonyFrameworkInitializer.getTelephonyServiceManager().getEuiccControllerService().register(sInstance);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return sInstance;
    }

    public static EuiccController get() {
        if (sInstance == null) {
            synchronized (EuiccController.class) {
                try {
                    if (sInstance == null) {
                        throw new IllegalStateException("get() called before init()");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private EuiccController(Context context, FeatureFlags featureFlags) {
        this(context, new EuiccConnector(context), featureFlags);
    }

    public EuiccController(Context context, EuiccConnector euiccConnector, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mConnector = euiccConnector;
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mAppOpsManager = (AppOpsManager) context.getSystemService("appops");
        this.mPackageManager = context.getPackageManager();
        this.mFeatureFlags = featureFlags;
        this.mVendorApiLevel = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
    }

    public void continueOperation(int i, Intent intent, Bundle bundle) {
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to continue operation");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            EuiccOperation euiccOperation = (EuiccOperation) intent.getParcelableExtra(EXTRA_OPERATION);
            if (euiccOperation == null) {
                throw new IllegalArgumentException("Invalid resolution intent");
            }
            PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_CALLBACK_INTENT");
            boolean booleanExtra = intent.getBooleanExtra("android.service.euicc.extra.RESOLUTION_USE_PORT_INDEX", false);
            bundle.putBoolean("android.service.euicc.extra.RESOLUTION_USE_PORT_INDEX", booleanExtra);
            if (!"android.service.euicc.action.RESOLVE_NO_PRIVILEGES".equals(intent.getAction()) || !bundle.containsKey("android.service.euicc.extra.RESOLUTION_PORT_INDEX")) {
                bundle.putInt("android.service.euicc.extra.RESOLUTION_PORT_INDEX", intent.getIntExtra("android.service.euicc.extra.RESOLUTION_PORT_INDEX", 0));
            }
            Log.i("EuiccController", " continueOperation portIndex: " + bundle.getInt("android.service.euicc.extra.RESOLUTION_PORT_INDEX") + " usePortIndex: " + booleanExtra);
            euiccOperation.continueOperation(i, bundle, pendingIntent);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public String getEid(int i, String str) {
        boolean zCallerCanReadPhoneStatePrivileged = callerCanReadPhoneStatePrivileged();
        try {
            this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            if (!zCallerCanReadPhoneStatePrivileged) {
                try {
                    if (!canManageSubscriptionOnTargetSim(i, str, false, -1)) {
                        throw new SecurityException("Must have carrier privileges on subscription to read EID for cardId=" + i);
                    }
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    throw th;
                }
            }
            String strBlockingGetEidFromEuiccService = blockingGetEidFromEuiccService(i);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return strBlockingGetEidFromEuiccService;
        } catch (SecurityException e) {
            EventLog.writeEvent(1397638484, "159062405", -1, "Missing UID checking");
            throw e;
        }
    }

    public long getAvailableMemoryInBytes(int i, String str) {
        boolean zCallerCanReadPhoneStatePrivileged = callerCanReadPhoneStatePrivileged();
        boolean zCallerCanReadPhoneState = callerCanReadPhoneState();
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (!zCallerCanReadPhoneStatePrivileged && !zCallerCanReadPhoneState) {
            try {
                if (!canManageSubscriptionOnTargetSim(i, str, false, -1)) {
                    throw new SecurityException("Must have READ_PHONE_STATE permission or READ_PRIVILEGED_PHONE_STATE permission or carrier privileges to read the available memory forcardId=" + i);
                }
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        long jBlockingGetAvailableMemoryInBytesFromEuiccService = blockingGetAvailableMemoryInBytesFromEuiccService(i);
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return jBlockingGetAvailableMemoryInBytesFromEuiccService;
    }

    public int getOtaStatus(int i) {
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to get OTA status");
            return 0;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return blockingGetOtaStatusFromEuiccService(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void startOtaUpdatingIfNecessary() {
        startOtaUpdatingIfNecessary(this.mTelephonyManager.getCardIdForDefaultEuicc());
    }

    public void startOtaUpdatingIfNecessary(int i) {
        this.mConnector.startOtaIfNecessary(i, new EuiccConnector.OtaStatusChangedCallback() { // from class: com.android.internal.telephony.euicc.EuiccController.1
            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.OtaStatusChangedCallback
            public void onOtaStatusChanged(int i2) {
                EuiccController.this.sendOtaStatusChangedBroadcast();
            }
        });
    }

    public void getDownloadableSubscriptionMetadata(int i, DownloadableSubscription downloadableSubscription, String str, PendingIntent pendingIntent) {
        getDownloadableSubscriptionMetadata(i, downloadableSubscription, false, str, pendingIntent);
    }

    public void setSupportedCountries(boolean z, List<String> list) {
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to set supported countries");
        } else if (z) {
            this.mSupportedCountries = list;
        } else {
            this.mUnsupportedCountries = list;
        }
    }

    public List<String> getSupportedCountries(boolean z) {
        List<String> list;
        List<String> list2;
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to get supported countries");
            return null;
        }
        if (!z || (list2 = this.mSupportedCountries) == null) {
            return (z || (list = this.mUnsupportedCountries) == null) ? Collections.EMPTY_LIST : list;
        }
        return list2;
    }

    public boolean isSupportedCountry(String str) {
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to check if the country is supported");
            return false;
        }
        List list = this.mSupportedCountries;
        if (list == null || list.isEmpty()) {
            Log.i("EuiccController", "Using deny list unsupportedCountries=" + this.mUnsupportedCountries);
            return !isEsimUnsupportedCountry(str);
        }
        Log.i("EuiccController", "Using allow list supportedCountries=" + this.mSupportedCountries);
        return isEsimSupportedCountry(str);
    }

    private boolean isEsimSupportedCountry(String str) {
        if (this.mSupportedCountries == null || TextUtils.isEmpty(str)) {
            return true;
        }
        return this.mSupportedCountries.contains(str);
    }

    private boolean isEsimUnsupportedCountry(String str) {
        if (this.mUnsupportedCountries == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return this.mUnsupportedCountries.contains(str);
    }

    void getDownloadableSubscriptionMetadata(int i, DownloadableSubscription downloadableSubscription, boolean z, String str, PendingIntent pendingIntent) {
        Log.d("EuiccController", " getDownloadableSubscriptionMetadata callingPackage: " + str);
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to get metadata");
            return;
        }
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mConnector.getDownloadableSubscriptionMetadata(i, 0, downloadableSubscription, false, z, new GetMetadataCommandCallback(jClearCallingIdentity, downloadableSubscription, str, pendingIntent));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    class GetMetadataCommandCallback implements EuiccConnector.GetMetadataCommandCallback {
        protected final PendingIntent mCallbackIntent;
        protected final String mCallingPackage;
        protected final long mCallingToken;
        protected final DownloadableSubscription mSubscription;

        GetMetadataCommandCallback(long j, DownloadableSubscription downloadableSubscription, String str, PendingIntent pendingIntent) {
            this.mCallingToken = j;
            this.mSubscription = downloadableSubscription;
            this.mCallingPackage = str;
            this.mCallbackIntent = pendingIntent;
        }

        @Override // com.android.internal.telephony.euicc.EuiccConnector.GetMetadataCommandCallback
        public void onGetMetadataComplete(int i, GetDownloadableSubscriptionMetadataResult getDownloadableSubscriptionMetadataResult) {
            int i2;
            Intent intent = new Intent();
            int result = getDownloadableSubscriptionMetadataResult.getResult();
            if (result == -1) {
                EuiccController.this.addResolutionIntentWithPort(intent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", this.mCallingPackage, 0, false, getOperationForDeactivateSim(), i, 0, false);
                i2 = 1;
            } else if (result == 0) {
                intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTION", getDownloadableSubscriptionMetadataResult.getDownloadableSubscription());
                i2 = 0;
            } else {
                EuiccController.this.addExtrasToResultIntent(intent, getDownloadableSubscriptionMetadataResult.getResult());
                i2 = 2;
            }
            EuiccController.this.sendResult(this.mCallbackIntent, i2, intent);
        }

        @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
        public void onEuiccServiceUnavailable() {
            EuiccController.this.sendResult(this.mCallbackIntent, 2, null);
        }

        protected EuiccOperation getOperationForDeactivateSim() {
            return EuiccOperation.forGetMetadataDeactivateSim(this.mCallingToken, this.mSubscription, this.mCallingPackage);
        }
    }

    public void downloadSubscription(int i, DownloadableSubscription downloadableSubscription, boolean z, String str, Bundle bundle, PendingIntent pendingIntent) {
        downloadSubscription(i, z ? -1 : 0, downloadableSubscription, z, str, false, bundle, pendingIntent);
    }

    Pair decodeSmdxSubjectAndReasonCode(int i) {
        Stack stack = new Stack();
        for (int i2 = 0; i2 < 6; i2++) {
            stack.push(Integer.valueOf(i & 15));
            i >>>= 4;
        }
        return Pair.create((stack.pop() + "." + stack.pop() + "." + stack.pop()).replaceAll("^(0\\.)*", ""), (stack.pop() + "." + stack.pop() + "." + stack.pop()).replaceAll("^(0\\.)*", ""));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addExtrasToResultIntent(Intent intent, int i) {
        int i2 = i >>> 24;
        intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DETAILED_CODE", i);
        intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_OPERATION_CODE", i2);
        if (i2 == 10) {
            Pair pairDecodeSmdxSubjectAndReasonCode = decodeSmdxSubjectAndReasonCode(i);
            String str = (String) pairDecodeSmdxSubjectAndReasonCode.first;
            String str2 = (String) pairDecodeSmdxSubjectAndReasonCode.second;
            intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_SMDX_SUBJECT_CODE", str);
            intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_SMDX_REASON_CODE", str2);
            return;
        }
        intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_ERROR_CODE", 16777215 & i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r13v10 */
    /* JADX WARN: Type inference failed for: r13v11 */
    /* JADX WARN: Type inference failed for: r13v12 */
    /* JADX WARN: Type inference failed for: r13v13 */
    /* JADX WARN: Type inference failed for: r13v14 */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v5, types: [long] */
    /* JADX WARN: Type inference failed for: r13v6 */
    /* JADX WARN: Type inference failed for: r13v7 */
    /* JADX WARN: Type inference failed for: r13v8, types: [long] */
    /* JADX WARN: Type inference failed for: r13v9 */
    /* JADX WARN: Type inference failed for: r14v0, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r18v0, types: [com.android.internal.telephony.euicc.EuiccController] */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v4, types: [boolean] */
    /* JADX WARN: Type inference failed for: r3v5 */
    void downloadSubscription(int i, int i2, DownloadableSubscription downloadableSubscription, boolean z, String str, boolean z2, Bundle bundle, PendingIntent pendingIntent) {
        int resolvedPortIndexForSubscriptionSwitch;
        ?? r13;
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        boolean zCallerCanManageDevicePolicyManagedSubscriptions = callerCanManageDevicePolicyManagedSubscriptions(str);
        if (zCallerCanManageDevicePolicyManagedSubscriptions && z && !shouldAllowSwitchAfterDownload(str)) {
            sendResult(pendingIntent, 2, null);
            return;
        }
        if (((UserManager) this.mContext.getSystemService(UserManager.class)).hasUserRestriction("no_sim_globally") && !zCallerCanManageDevicePolicyManagedSubscriptions) {
            sendResult(pendingIntent, 2, null);
            return;
        }
        boolean zIsCompatChangeEnabled = isCompatChangeEnabled(str, 224562872L);
        boolean zCallerCanWriteEmbeddedSubscriptions = callerCanWriteEmbeddedSubscriptions();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        ?? r14 = 0;
        r14 = 0;
        r14 = 0;
        if (z) {
            resolvedPortIndexForSubscriptionSwitch = i2;
            if (resolvedPortIndexForSubscriptionSwitch == -1) {
                if (zIsCompatChangeEnabled) {
                    try {
                        resolvedPortIndexForSubscriptionSwitch = getResolvedPortIndexForSubscriptionSwitch(i);
                    } catch (Throwable th) {
                        th = th;
                        r13 = jClearCallingIdentity;
                        Binder.restoreCallingIdentity(r13);
                        throw th;
                    }
                } else {
                    resolvedPortIndexForSubscriptionSwitch = 0;
                }
                if (resolvedPortIndexForSubscriptionSwitch == -1) {
                    r14 = 1;
                }
            }
        } else {
            resolvedPortIndexForSubscriptionSwitch = i2;
        }
        ?? sb = new StringBuilder(" downloadSubscription cardId: ");
        sb.append(i);
        sb.append(" switchAfterDownload: ");
        sb.append(z);
        sb.append(" portIndex: ");
        sb.append(resolvedPortIndexForSubscriptionSwitch);
        sb.append(" forceDeactivateSim: ");
        ?? r3 = z2;
        sb.append(r3);
        sb.append(" callingPackage: ");
        sb.append(str);
        sb.append(" isConsentNeededToResolvePortIndex: ");
        sb.append(r14);
        sb.append(" shouldResolvePortIndex:");
        sb.append(zIsCompatChangeEnabled);
        sb.append(" callerHasAdminPrivileges:");
        sb.append(zCallerCanManageDevicePolicyManagedSubscriptions);
        Log.d("EuiccController", sb.toString());
        try {
            if (r14 == 0 && (zCallerCanWriteEmbeddedSubscriptions || zCallerCanManageDevicePolicyManagedSubscriptions)) {
                try {
                    downloadSubscriptionPrivileged(i, resolvedPortIndexForSubscriptionSwitch, jClearCallingIdentity, downloadableSubscription, z, r3, str, bundle, pendingIntent, zCallerCanManageDevicePolicyManagedSubscriptions, getCurrentEmbeddedSubscriptionIds(i));
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return;
                } catch (Throwable th2) {
                    th = th2;
                    r3 = jClearCallingIdentity;
                    r13 = r3;
                    Binder.restoreCallingIdentity(r13);
                    throw th;
                }
            }
            int i3 = resolvedPortIndexForSubscriptionSwitch;
            try {
                if (r14 != 0 || !canManageSubscriptionOnTargetSim(i, str, true, i3)) {
                    long j = jClearCallingIdentity;
                    Log.i("EuiccController", "Caller can't manage subscription on target SIM or  User consent is required for resolving port index. Ask user's consent first");
                    Intent intent = new Intent();
                    addResolutionIntentWithPort(intent, "android.service.euicc.action.RESOLVE_NO_PRIVILEGES", str, 0, false, EuiccOperation.forDownloadNoPrivilegesOrDeactivateSimCheckMetadata(j, downloadableSubscription, z, str), i, i3, z);
                    sendResult(pendingIntent, 1, intent);
                    r14 = j;
                } else {
                    try {
                        r14 = jClearCallingIdentity;
                        this.mConnector.getDownloadableSubscriptionMetadata(i, i3, downloadableSubscription, z, z2, new DownloadSubscriptionGetMetadataCommandCallback(jClearCallingIdentity, downloadableSubscription, z, str, z2, pendingIntent, false, i3));
                    } catch (Throwable th3) {
                        th = th3;
                        r13 = jClearCallingIdentity;
                        Binder.restoreCallingIdentity(r13);
                        throw th;
                    }
                }
                Binder.restoreCallingIdentity(r14);
            } catch (Throwable th4) {
                th = th4;
                r13 = r14;
            }
        } catch (Throwable th5) {
            th = th5;
        }
    }

    class DownloadSubscriptionGetMetadataCommandCallback extends GetMetadataCommandCallback {
        private final boolean mForceDeactivateSim;
        private final int mPortIndex;
        private final boolean mSwitchAfterDownload;
        private final boolean mWithUserConsent;

        DownloadSubscriptionGetMetadataCommandCallback(long j, DownloadableSubscription downloadableSubscription, boolean z, String str, boolean z2, PendingIntent pendingIntent, boolean z3, int i) {
            super(j, downloadableSubscription, str, pendingIntent);
            this.mSwitchAfterDownload = z;
            this.mForceDeactivateSim = z2;
            this.mWithUserConsent = z3;
            this.mPortIndex = i;
        }

        @Override // com.android.internal.telephony.euicc.EuiccController.GetMetadataCommandCallback, com.android.internal.telephony.euicc.EuiccConnector.GetMetadataCommandCallback
        public void onGetMetadataComplete(int i, GetDownloadableSubscriptionMetadataResult getDownloadableSubscriptionMetadataResult) {
            DownloadableSubscription downloadableSubscription = getDownloadableSubscriptionMetadataResult.getDownloadableSubscription();
            if (this.mWithUserConsent) {
                if (getDownloadableSubscriptionMetadataResult.getResult() != 0) {
                    super.onGetMetadataComplete(i, getDownloadableSubscriptionMetadataResult);
                    return;
                }
                boolean zCallerCanManageDevicePolicyManagedSubscriptions = EuiccController.this.callerCanManageDevicePolicyManagedSubscriptions(this.mCallingPackage, this.mCallingToken);
                if (EuiccController.this.checkCarrierPrivilegeInMetadata(downloadableSubscription, this.mCallingPackage) || zCallerCanManageDevicePolicyManagedSubscriptions) {
                    EuiccController euiccController = EuiccController.this;
                    euiccController.downloadSubscriptionPrivileged(i, this.mPortIndex, this.mCallingToken, downloadableSubscription, this.mSwitchAfterDownload, this.mForceDeactivateSim, this.mCallingPackage, null, this.mCallbackIntent, zCallerCanManageDevicePolicyManagedSubscriptions, euiccController.getCurrentEmbeddedSubscriptionIds(i));
                    return;
                } else {
                    Log.e("EuiccController", "Caller does not have carrier privilege in metadata and is does not have admin privileges, mCallingPackage=" + this.mCallingPackage);
                    EuiccController.this.sendResult(this.mCallbackIntent, 2, null);
                    return;
                }
            }
            if (getDownloadableSubscriptionMetadataResult.getResult() == -1) {
                Intent intent = new Intent();
                EuiccController euiccController2 = EuiccController.this;
                String str = this.mCallingPackage;
                euiccController2.addResolutionIntentWithPort(intent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", str, 0, false, EuiccOperation.forDownloadNoPrivilegesOrDeactivateSimCheckMetadata(this.mCallingToken, this.mSubscription, this.mSwitchAfterDownload, str), i, this.mPortIndex, this.mSwitchAfterDownload);
                EuiccController.this.sendResult(this.mCallbackIntent, 1, intent);
                return;
            }
            if (getDownloadableSubscriptionMetadataResult.getResult() != 0) {
                super.onGetMetadataComplete(i, getDownloadableSubscriptionMetadataResult);
            } else if (EuiccController.this.checkCarrierPrivilegeInMetadata(downloadableSubscription, this.mCallingPackage)) {
                EuiccController.this.downloadSubscriptionPrivileged(i, this.mPortIndex, this.mCallingToken, downloadableSubscription, this.mSwitchAfterDownload, this.mForceDeactivateSim, this.mCallingPackage, null, this.mCallbackIntent);
            } else {
                Log.e("EuiccController", "Caller is not permitted to download this profile per metadata");
                EuiccController.this.sendResult(this.mCallbackIntent, 2, null);
            }
        }
    }

    void downloadSubscriptionPrivilegedCheckMetadata(int i, int i2, long j, DownloadableSubscription downloadableSubscription, boolean z, boolean z2, String str, Bundle bundle, PendingIntent pendingIntent) {
        Log.d("EuiccController", " downloadSubscriptionPrivilegedCheckMetadata cardId: " + i + " switchAfterDownload: " + z + " portIndex: " + i2 + " forceDeactivateSim: " + z2);
        this.mConnector.getDownloadableSubscriptionMetadata(i, i2, downloadableSubscription, z, z2, new DownloadSubscriptionGetMetadataCommandCallback(j, downloadableSubscription, z, str, z2, pendingIntent, true, i2));
    }

    void downloadSubscriptionPrivileged(int i, int i2, long j, DownloadableSubscription downloadableSubscription, boolean z, boolean z2, String str, Bundle bundle, PendingIntent pendingIntent) {
        downloadSubscriptionPrivileged(i, i2, j, downloadableSubscription, z, z2, str, bundle, pendingIntent, false, new ArraySet());
    }

    void downloadSubscriptionPrivileged(final int i, final int i2, final long j, final DownloadableSubscription downloadableSubscription, final boolean z, boolean z2, final String str, Bundle bundle, final PendingIntent pendingIntent, final boolean z3, final Set set) {
        this.mConnector.downloadSubscription(i, i2, downloadableSubscription, z, z2, addCallingPackageToBundle(bundle, str), new EuiccConnector.DownloadCommandCallback() { // from class: com.android.internal.telephony.euicc.EuiccController.2
            @Override // com.android.internal.telephony.euicc.EuiccConnector.DownloadCommandCallback
            public void onDownloadComplete(DownloadSubscriptionResult downloadSubscriptionResult) {
                Intent intent = new Intent();
                int result = downloadSubscriptionResult.getResult();
                int i3 = 1;
                if (result == -2) {
                    boolean z4 = !TextUtils.isEmpty(downloadableSubscription.getConfirmationCode());
                    int resolvableErrors = downloadSubscriptionResult.getResolvableErrors();
                    EuiccController euiccController = EuiccController.this;
                    if (resolvableErrors != 0) {
                        euiccController.addResolutionIntentWithPort(intent, "android.service.euicc.action.RESOLVE_RESOLVABLE_ERRORS", str, downloadSubscriptionResult.getResolvableErrors(), z4, EuiccOperation.forDownloadResolvableErrors(j, downloadableSubscription, z, str, downloadSubscriptionResult.getResolvableErrors()), i, i2, z);
                    } else {
                        String str2 = str;
                        euiccController.addResolutionIntentWithPort(intent, "android.service.euicc.action.RESOLVE_CONFIRMATION_CODE", str2, 0, z4, EuiccOperation.forDownloadConfirmationCode(j, downloadableSubscription, z, str2), i, i2, z);
                    }
                } else if (result != -1) {
                    EuiccController euiccController2 = EuiccController.this;
                    if (result == 0) {
                        Settings.Global.putInt(euiccController2.mContext.getContentResolver(), "euicc_provisioned", 1);
                        intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTION", downloadableSubscription);
                        if (!z) {
                            EuiccController.this.refreshSubscriptionsAndSendResult(pendingIntent, 0, intent, z3, str, i, set);
                            return;
                        }
                        if (z3) {
                            intent = intent;
                            EuiccController.this.refreshSubscriptionsOwnership(true, str, i, set);
                        }
                        intent = intent;
                        i3 = 0;
                    } else {
                        euiccController2.addExtrasToResultIntent(intent, downloadSubscriptionResult.getResult());
                        i3 = 2;
                    }
                } else {
                    EuiccController euiccController3 = EuiccController.this;
                    String str3 = str;
                    euiccController3.addResolutionIntentWithPort(intent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", str3, 0, false, EuiccOperation.forDownloadDeactivateSim(j, downloadableSubscription, z, str3), i, i2, z);
                }
                EuiccController.this.sendResult(pendingIntent, i3, intent);
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
                EuiccController.this.sendResult(pendingIntent, 2, null);
            }
        });
    }

    private static Bundle addCallingPackageToBundle(Bundle bundle, String str) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("android.service.euicc.extra.PACKAGE_NAME", str);
        return bundle;
    }

    public GetEuiccProfileInfoListResult blockingGetEuiccProfileInfoList(int i) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference atomicReference = new AtomicReference();
        this.mConnector.getEuiccProfileInfoList(i, new EuiccConnector.GetEuiccProfileInfoListCommandCallback(this) { // from class: com.android.internal.telephony.euicc.EuiccController.3
            @Override // com.android.internal.telephony.euicc.EuiccConnector.GetEuiccProfileInfoListCommandCallback
            public void onListComplete(GetEuiccProfileInfoListResult getEuiccProfileInfoListResult) {
                atomicReference.set(getEuiccProfileInfoListResult);
                countDownLatch.countDown();
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Log.e("EuiccController", "blockingGetEuiccInfoFromEuiccService got InterruptedException e: " + e);
            Thread.currentThread().interrupt();
        }
        return (GetEuiccProfileInfoListResult) atomicReference.get();
    }

    public void getDefaultDownloadableSubscriptionList(int i, String str, PendingIntent pendingIntent) {
        getDefaultDownloadableSubscriptionList(i, false, str, pendingIntent);
    }

    void getDefaultDownloadableSubscriptionList(int i, boolean z, String str, PendingIntent pendingIntent) {
        Log.d("EuiccController", " getDefaultDownloadableSubscriptionList callingPackage: " + str);
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to get default list");
            return;
        }
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mConnector.getDefaultDownloadableSubscriptionList(i, z, new GetDefaultListCommandCallback(jClearCallingIdentity, str, pendingIntent));
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    class GetDefaultListCommandCallback implements EuiccConnector.GetDefaultListCommandCallback {
        final PendingIntent mCallbackIntent;
        final String mCallingPackage;
        final long mCallingToken;

        GetDefaultListCommandCallback(long j, String str, PendingIntent pendingIntent) {
            this.mCallingToken = j;
            this.mCallingPackage = str;
            this.mCallbackIntent = pendingIntent;
        }

        @Override // com.android.internal.telephony.euicc.EuiccConnector.GetDefaultListCommandCallback
        public void onGetDefaultListComplete(int i, GetDefaultDownloadableSubscriptionListResult getDefaultDownloadableSubscriptionListResult) {
            int i2;
            Intent intent = new Intent();
            int result = getDefaultDownloadableSubscriptionListResult.getResult();
            if (result == -1) {
                EuiccController euiccController = EuiccController.this;
                String str = this.mCallingPackage;
                euiccController.addResolutionIntentWithPort(intent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", str, 0, false, EuiccOperation.forGetDefaultListDeactivateSim(this.mCallingToken, str), i, 0, false);
                i2 = 1;
            } else if (result == 0) {
                List downloadableSubscriptions = getDefaultDownloadableSubscriptionListResult.getDownloadableSubscriptions();
                if (downloadableSubscriptions != null && downloadableSubscriptions.size() > 0) {
                    intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTIONS", (Parcelable[]) downloadableSubscriptions.toArray(new DownloadableSubscription[downloadableSubscriptions.size()]));
                }
                i2 = 0;
            } else {
                EuiccController.this.addExtrasToResultIntent(intent, getDefaultDownloadableSubscriptionListResult.getResult());
                i2 = 2;
            }
            EuiccController.this.sendResult(this.mCallbackIntent, i2, intent);
        }

        @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
        public void onEuiccServiceUnavailable() {
            EuiccController.this.sendResult(this.mCallbackIntent, 2, null);
        }
    }

    public EuiccInfo getEuiccInfo(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return blockingGetEuiccInfoFromEuiccService(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void deleteSubscription(int i, int i2, String str, PendingIntent pendingIntent) {
        boolean zCallerCanWriteEmbeddedSubscriptions = callerCanWriteEmbeddedSubscriptions();
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        boolean zCallerCanManageDevicePolicyManagedSubscriptions = callerCanManageDevicePolicyManagedSubscriptions(str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfo subscriptionForSubscriptionId = getSubscriptionForSubscriptionId(i2);
            if (subscriptionForSubscriptionId == null) {
                Log.e("EuiccController", "Cannot delete nonexistent subscription: " + i2);
                sendResult(pendingIntent, 2, null);
                return;
            }
            boolean z = zCallerCanManageDevicePolicyManagedSubscriptions && isSubscriptionDevicePolicyManaged(subscriptionForSubscriptionId, str);
            if (!zCallerCanWriteEmbeddedSubscriptions && !canManageSubscription(subscriptionForSubscriptionId, str) && !z) {
                Log.e("EuiccController", "No permissions to delete subscription: " + i2);
                sendResult(pendingIntent, 2, null);
                return;
            }
            deleteSubscriptionPrivileged(i, subscriptionForSubscriptionId.getIccId(), pendingIntent);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    void deleteSubscriptionPrivileged(int i, String str, final PendingIntent pendingIntent) {
        this.mConnector.deleteSubscription(i, str, new EuiccConnector.DeleteCommandCallback() { // from class: com.android.internal.telephony.euicc.EuiccController.4
            @Override // com.android.internal.telephony.euicc.EuiccConnector.DeleteCommandCallback
            public void onDeleteComplete(int i2) {
                Intent intent = new Intent();
                EuiccController euiccController = EuiccController.this;
                if (i2 == 0) {
                    euiccController.refreshSubscriptionsAndSendResult(pendingIntent, 0, intent);
                } else {
                    euiccController.addExtrasToResultIntent(intent, i2);
                    EuiccController.this.sendResult(pendingIntent, 2, intent);
                }
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
                EuiccController.this.sendResult(pendingIntent, 2, null);
            }
        });
    }

    public void switchToSubscription(int i, int i2, String str, PendingIntent pendingIntent) {
        switchToSubscription(i, i2, 0, false, str, pendingIntent, false);
    }

    public void switchToSubscriptionWithPort(int i, int i2, int i3, String str, PendingIntent pendingIntent) {
        switchToSubscription(i, i2, i3, false, str, pendingIntent, true);
    }

    void switchToSubscription(int i, int i2, int i3, boolean z, String str, PendingIntent pendingIntent, boolean z2) {
        long j;
        boolean z3;
        String iccId;
        boolean z4;
        int resolvedPortIndexForSubscriptionSwitch = i3;
        boolean z5 = z2;
        boolean zCallerCanWriteEmbeddedSubscriptions = callerCanWriteEmbeddedSubscriptions();
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        boolean zIsCompatChangeEnabled = isCompatChangeEnabled(str, 224562872L);
        StringBuilder sb = new StringBuilder(" subId: ");
        sb.append(i2);
        sb.append(" portIndex: ");
        sb.append(resolvedPortIndexForSubscriptionSwitch);
        sb.append(" forceDeactivateSim: ");
        boolean z6 = z;
        sb.append(z6);
        sb.append(" usePortIndex: ");
        sb.append(z5);
        sb.append(" callingPackage: ");
        sb.append(str);
        sb.append(" shouldResolvePortIndex: ");
        sb.append(zIsCompatChangeEnabled);
        Log.d("EuiccController", sb.toString());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (zCallerCanWriteEmbeddedSubscriptions) {
            z6 = true;
        }
        boolean z7 = false;
        try {
            if (i2 == -1) {
                if (!z5) {
                    resolvedPortIndexForSubscriptionSwitch = getResolvedPortIndexForDisableSubscription(i, str, zCallerCanWriteEmbeddedSubscriptions);
                    if (resolvedPortIndexForSubscriptionSwitch == -1) {
                        Log.e("EuiccController", "Disable is not permitted: no active subscription or cannot manage subscription");
                        sendResult(pendingIntent, 2, null);
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                    z5 = true;
                }
                if (!zCallerCanWriteEmbeddedSubscriptions && !canManageActiveSubscriptionOnTargetSim(i, str, z5, resolvedPortIndexForSubscriptionSwitch)) {
                    Log.e("EuiccController", "Not permitted to switch to empty subscription");
                    sendResult(pendingIntent, 2, null);
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return;
                }
                z4 = z5;
                z3 = true;
                iccId = null;
            } else {
                SubscriptionInfo subscriptionForSubscriptionId = getSubscriptionForSubscriptionId(i2);
                if (subscriptionForSubscriptionId == null) {
                    Log.e("EuiccController", "Cannot switch to nonexistent sub: " + i2);
                    sendResult(pendingIntent, 2, null);
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return;
                }
                if (!zCallerCanWriteEmbeddedSubscriptions) {
                    if (!canManageSubscription(subscriptionForSubscriptionId, str)) {
                        Log.e("EuiccController", "Not permitted to switch to sub: " + i2);
                        sendResult(pendingIntent, 2, null);
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                    z3 = canManageSubscriptionOnTargetSim(i, str, z5, resolvedPortIndexForSubscriptionSwitch);
                }
                iccId = subscriptionForSubscriptionId.getIccId();
                if (!z5) {
                    resolvedPortIndexForSubscriptionSwitch = zIsCompatChangeEnabled ? getResolvedPortIndexForSubscriptionSwitch(i) : 0;
                    z7 = resolvedPortIndexForSubscriptionSwitch == -1;
                    Log.d("EuiccController", " Resolved portIndex: " + resolvedPortIndexForSubscriptionSwitch);
                    z4 = true;
                } else {
                    if (!isTargetPortIndexValid(i, resolvedPortIndexForSubscriptionSwitch)) {
                        Log.e("EuiccController", "Not permitted to switch to invalid portIndex");
                        Intent intent = new Intent();
                        intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_ERROR_CODE", 10017);
                        sendResult(pendingIntent, 2, intent);
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return;
                    }
                    z4 = z5;
                }
            }
            if (!z3 || z7) {
                boolean z8 = z4;
                j = jClearCallingIdentity;
                try {
                    Intent intent2 = new Intent();
                    addResolutionIntent(intent2, "android.service.euicc.action.RESOLVE_NO_PRIVILEGES", str, 0, false, EuiccOperation.forSwitchNoPrivileges(j, i2, str), i, resolvedPortIndexForSubscriptionSwitch, z8, i2);
                    sendResult(pendingIntent, 1, intent2);
                    Binder.restoreCallingIdentity(j);
                    return;
                } catch (Throwable th) {
                    th = th;
                }
            } else {
                try {
                    switchToSubscriptionPrivileged(i, resolvedPortIndexForSubscriptionSwitch, jClearCallingIdentity, i2, iccId, z6, str, pendingIntent, z4);
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return;
                } catch (Throwable th2) {
                    th = th2;
                    j = jClearCallingIdentity;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            j = jClearCallingIdentity;
        }
        Binder.restoreCallingIdentity(j);
        throw th;
    }

    public int getResolvedPortIndexForDisableSubscription(int i, String str, boolean z) {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList(false);
        if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() != 0) {
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                if (i == -1 || subscriptionInfo.getCardId() == i) {
                    if (subscriptionInfo.isEmbedded() && (z || canManageSubscription(subscriptionInfo, str))) {
                        return subscriptionInfo.getPortIndex();
                    }
                }
            }
        }
        return -1;
    }

    public int getResolvedPortIndexForSubscriptionSwitch(int i) {
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex;
        int slotIndexFromCardId = getSlotIndexFromCardId(i);
        UiccSlot uiccSlot = UiccController.getInstance().getUiccSlot(slotIndexFromCardId);
        int i2 = 0;
        if (uiccSlot == null) {
            Log.d("EuiccController", "Switch to inactive slot, return default port index. slotIndex: " + slotIndexFromCardId);
            return 0;
        }
        if (!uiccSlot.isMultipleEnabledProfileSupported()) {
            Log.d("EuiccController", "Multiple enabled profiles is not supported, return default port index");
            return 0;
        }
        boolean z = getRemovableNonEuiccSlot() != null && getRemovableNonEuiccSlot().isActive();
        if (this.mTelephonyManager.getActiveModemCount() == 1) {
            if (z) {
                return 0;
            }
            int[] portList = uiccSlot.getPortList();
            int length = portList.length;
            while (i2 < length) {
                int i3 = portList[i2];
                if (uiccSlot.isPortActive(i3)) {
                    return i3;
                }
                i2++;
            }
            return -1;
        }
        int[] portList2 = uiccSlot.getPortList();
        int length2 = portList2.length;
        while (i2 < length2) {
            int i4 = portList2[i2];
            if (uiccSlot.isPortActive(i4) && ((activeSubscriptionInfoForSimSlotIndex = this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(uiccSlot.getPhoneIdFromPortIndex(i4))) == null || activeSubscriptionInfoForSimSlotIndex.isOpportunistic() || activeSubscriptionInfoForSimSlotIndex.getProfileClass() == 1)) {
                return i4;
            }
            i2++;
        }
        if (!z || isRemovalNonEuiccSlotHasActiveSubscription()) {
            return -1;
        }
        return getNextAvailableInActivePortIndex(uiccSlot);
    }

    private boolean isTargetPortIndexValid(int i, int i2) {
        if (i2 < 0) {
            Log.e("EuiccController", "Invalid portIndex: " + i2);
            return false;
        }
        UiccSlot uiccSlot = UiccController.getInstance().getUiccSlot(getSlotIndexFromCardId(i));
        if (uiccSlot == null || uiccSlot.getPortList().length == 0 || i2 >= uiccSlot.getPortList().length) {
            Log.e("EuiccController", "Invalid portIndex");
            return false;
        }
        if (this.mTelephonyManager.getActiveModemCount() == 1) {
            for (int i3 : uiccSlot.getPortList()) {
                if (uiccSlot.isPortActive(i3) && i3 != i2) {
                    Log.e("EuiccController", "In SS Mode, slot already has active port on portIndex " + i3 + " , reject the switch request to portIndex " + i2);
                    return false;
                }
            }
        } else if (this.mTelephonyManager.getActiveModemCount() == 2) {
            if (isRemovalNonEuiccSlotHasActiveSubscription()) {
                for (int i4 : uiccSlot.getPortList()) {
                    if (uiccSlot.isPortActive(i4) && this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(uiccSlot.getPhoneIdFromPortIndex(i4)) == null && i4 != i2) {
                        Log.e("EuiccController", "In DSDS Mode, pSim has active subscription, eSim has empty active port on portIndex " + i4 + " , reject the switch request to portIndex " + i2);
                        return false;
                    }
                }
            }
        } else if (this.mTelephonyManager.getActiveModemCount() >= 3 && isRemovalNonEuiccSlotHasActiveSubscription() && (!uiccSlot.isPortActive(i2) || this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(uiccSlot.getPhoneIdFromPortIndex(i2)) != null)) {
            Log.e("EuiccController", "In TSTS Mode, pSim has active subscription but targetPortIndex is inactive or has active subscription , reject the switch request to portIndex " + i2);
            return false;
        }
        return true;
    }

    private int getNextAvailableInActivePortIndex(UiccSlot uiccSlot) {
        if (uiccSlot == null) {
            return -1;
        }
        for (int i : uiccSlot.getPortList()) {
            if (!uiccSlot.isPortActive(i)) {
                return i;
            }
        }
        return -1;
    }

    private int getSlotIndexFromCardId(int i) {
        UiccSlotInfo[] uiccSlotsInfo = this.mTelephonyManager.getUiccSlotsInfo();
        if (uiccSlotsInfo == null || uiccSlotsInfo.length == 0) {
            Log.e("EuiccController", "UiccSlotInfo is null or empty");
            return -1;
        }
        String strConvertToCardString = UiccController.getInstance().convertToCardString(i);
        for (int i2 = 0; i2 < uiccSlotsInfo.length; i2++) {
            if (uiccSlotsInfo[i2] == null) {
                AnomalyReporter.reportAnomaly(UUID.fromString("e9517acf-e1a1-455f-9231-1b5515a0d0eb"), "EuiccController: Found UiccSlotInfo Null object.");
            }
            UiccSlotInfo uiccSlotInfo = uiccSlotsInfo[i2];
            if (IccUtils.compareIgnoreTrailingFs(strConvertToCardString, uiccSlotInfo != null ? uiccSlotInfo.getCardId() : null)) {
                return i2;
            }
        }
        Log.i("EuiccController", "No UiccSlotInfo found for cardId: " + i);
        return -1;
    }

    private boolean isRemovalNonEuiccSlotHasActiveSubscription() {
        UiccSlot removableNonEuiccSlot = getRemovableNonEuiccSlot();
        if (removableNonEuiccSlot != null) {
            for (int i : removableNonEuiccSlot.getPortList()) {
                if (removableNonEuiccSlot.isPortActive(i) && this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(removableNonEuiccSlot.getPhoneIdFromPortIndex(i)) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private UiccSlot getRemovableNonEuiccSlot() {
        UiccSlot[] uiccSlots = UiccController.getInstance().getUiccSlots();
        if (uiccSlots == null) {
            return null;
        }
        for (int i = 0; i < uiccSlots.length; i++) {
            UiccSlot uiccSlot = uiccSlots[i];
            if (uiccSlot != null && uiccSlot.isRemovable() && !uiccSlots[i].isEuicc()) {
                return uiccSlots[i];
            }
        }
        return null;
    }

    void switchToSubscriptionPrivileged(int i, int i2, long j, int i3, boolean z, String str, PendingIntent pendingIntent, boolean z2) {
        SubscriptionInfo subscriptionForSubscriptionId = getSubscriptionForSubscriptionId(i3);
        switchToSubscriptionPrivileged(i, i2, j, i3, subscriptionForSubscriptionId != null ? subscriptionForSubscriptionId.getIccId() : null, z, str, pendingIntent, z2);
    }

    void switchToSubscriptionPrivileged(final int i, final int i2, final long j, final int i3, String str, boolean z, final String str2, final PendingIntent pendingIntent, final boolean z2) {
        this.mConnector.switchToSubscription(i, i2, str, z, new EuiccConnector.SwitchCommandCallback() { // from class: com.android.internal.telephony.euicc.EuiccController.5
            @Override // com.android.internal.telephony.euicc.EuiccConnector.SwitchCommandCallback
            public void onSwitchComplete(int i4) {
                int i5;
                Intent intent = new Intent();
                if (i4 == -1) {
                    EuiccController euiccController = EuiccController.this;
                    String str3 = str2;
                    euiccController.addResolutionIntent(intent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", str3, 0, false, EuiccOperation.forSwitchDeactivateSim(j, i3, str3), i, i2, z2, i3);
                    i5 = 1;
                } else if (i4 != 0) {
                    EuiccController.this.addExtrasToResultIntent(intent, i4);
                    i5 = 2;
                } else {
                    i5 = 0;
                }
                EuiccController.this.sendResult(pendingIntent, i5, intent);
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
                EuiccController.this.sendResult(pendingIntent, 2, null);
            }
        }, z2);
    }

    public void updateSubscriptionNickname(int i, int i2, String str, String str2, final PendingIntent pendingIntent) {
        boolean zCallerCanWriteEmbeddedSubscriptions = callerCanWriteEmbeddedSubscriptions();
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str2);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionInfo subscriptionForSubscriptionId = getSubscriptionForSubscriptionId(i2);
            if (subscriptionForSubscriptionId == null) {
                Log.e("EuiccController", "Cannot update nickname to nonexistent sub: " + i2);
                sendResult(pendingIntent, 2, null);
                return;
            }
            if (!zCallerCanWriteEmbeddedSubscriptions && !canManageSubscription(subscriptionForSubscriptionId, str2)) {
                Log.e("EuiccController", "No permissions: " + i2);
                sendResult(pendingIntent, 2, null);
                return;
            }
            this.mConnector.updateSubscriptionNickname(i, subscriptionForSubscriptionId.getIccId(), str, new EuiccConnector.UpdateNicknameCommandCallback() { // from class: com.android.internal.telephony.euicc.EuiccController.6
                @Override // com.android.internal.telephony.euicc.EuiccConnector.UpdateNicknameCommandCallback
                public void onUpdateNicknameComplete(int i3) {
                    Intent intent = new Intent();
                    EuiccController euiccController = EuiccController.this;
                    if (i3 == 0) {
                        euiccController.refreshSubscriptionsAndSendResult(pendingIntent, 0, intent);
                    } else {
                        euiccController.addExtrasToResultIntent(intent, i3);
                        EuiccController.this.sendResult(pendingIntent, 2, intent);
                    }
                }

                @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
                public void onEuiccServiceUnavailable() {
                    EuiccController.this.sendResult(pendingIntent, 2, null);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void eraseSubscriptions(int i, final PendingIntent pendingIntent) {
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to erase subscriptions");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mConnector.eraseSubscriptions(i, new EuiccConnector.EraseCommandCallback() { // from class: com.android.internal.telephony.euicc.EuiccController.7
                @Override // com.android.internal.telephony.euicc.EuiccConnector.EraseCommandCallback
                public void onEraseComplete(int i2) {
                    Intent intent = new Intent();
                    EuiccController euiccController = EuiccController.this;
                    if (i2 == 0) {
                        euiccController.refreshSubscriptionsAndSendResult(pendingIntent, 0, intent);
                    } else {
                        euiccController.addExtrasToResultIntent(intent, i2);
                        EuiccController.this.sendResult(pendingIntent, 2, intent);
                    }
                }

                @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
                public void onEuiccServiceUnavailable() {
                    EuiccController.this.sendResult(pendingIntent, 2, null);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void eraseSubscriptionsWithOptions(int i, int i2, final PendingIntent pendingIntent) {
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to erase subscriptions");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mConnector.eraseSubscriptionsWithOptions(i, i2, new EuiccConnector.EraseCommandCallback() { // from class: com.android.internal.telephony.euicc.EuiccController.8
                @Override // com.android.internal.telephony.euicc.EuiccConnector.EraseCommandCallback
                public void onEraseComplete(int i3) {
                    Intent intent = new Intent();
                    EuiccController euiccController = EuiccController.this;
                    if (i3 == 0) {
                        euiccController.refreshSubscriptionsAndSendResult(pendingIntent, 0, intent);
                    } else {
                        euiccController.addExtrasToResultIntent(intent, i3);
                        EuiccController.this.sendResult(pendingIntent, 2, intent);
                    }
                }

                @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
                public void onEuiccServiceUnavailable() {
                    EuiccController.this.sendResult(pendingIntent, 2, null);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void retainSubscriptionsForFactoryReset(int i, final PendingIntent pendingIntent) {
        this.mContext.enforceCallingPermission("android.permission.MASTER_CLEAR", "Must have MASTER_CLEAR to retain subscriptions for factory reset");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mConnector.retainSubscriptions(i, new EuiccConnector.RetainSubscriptionsCommandCallback() { // from class: com.android.internal.telephony.euicc.EuiccController.9
                @Override // com.android.internal.telephony.euicc.EuiccConnector.RetainSubscriptionsCommandCallback
                public void onRetainSubscriptionsComplete(int i2) {
                    int i3;
                    Intent intent = new Intent();
                    if (i2 != 0) {
                        EuiccController.this.addExtrasToResultIntent(intent, i2);
                        i3 = 2;
                    } else {
                        i3 = 0;
                    }
                    EuiccController.this.sendResult(pendingIntent, i3, intent);
                }

                @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
                public void onEuiccServiceUnavailable() {
                    EuiccController.this.sendResult(pendingIntent, 2, null);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void refreshSubscriptionsAndSendResult(PendingIntent pendingIntent, int i, Intent intent) {
        refreshSubscriptionsAndSendResult(pendingIntent, i, intent, false, "", -1, new ArraySet());
    }

    public void refreshSubscriptionsAndSendResult(final PendingIntent pendingIntent, final int i, final Intent intent, final boolean z, final String str, final int i2, final Set<Integer> set) {
        SubscriptionManagerService.getInstance().updateEmbeddedSubscriptions(List.of(Integer.valueOf(this.mTelephonyManager.getCardIdForDefaultEuicc())), new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$refreshSubscriptionsAndSendResult$0(z, str, i2, set, pendingIntent, i, intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshSubscriptionsAndSendResult$0(boolean z, String str, int i, Set set, PendingIntent pendingIntent, int i2, Intent intent) {
        refreshSubscriptionsOwnership(z, str, i, set);
        sendResult(pendingIntent, i2, intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSubscriptionsOwnership(boolean z, String str, int i, Set set) {
        if (z) {
            Set currentEmbeddedSubscriptionIds = getCurrentEmbeddedSubscriptionIds(i);
            currentEmbeddedSubscriptionIds.removeAll(set);
            Iterator it = currentEmbeddedSubscriptionIds.iterator();
            while (it.hasNext()) {
                SubscriptionManagerService.getInstance().setGroupOwner(((Integer) it.next()).intValue(), str);
            }
        }
    }

    public void sendResult(PendingIntent pendingIntent, int i, Intent intent) {
        try {
            pendingIntent.send(this.mContext, i, intent);
        } catch (PendingIntent.CanceledException unused) {
        }
    }

    public void addResolutionIntentWithPort(Intent intent, String str, String str2, int i, boolean z, EuiccOperation euiccOperation, int i2, int i3, boolean z2) {
        addResolutionIntent(intent, str, str2, i, z, euiccOperation, i2, i3, z2, -1);
    }

    public void addResolutionIntent(Intent intent, String str, String str2, int i, boolean z, EuiccOperation euiccOperation, int i2, int i3, boolean z2, int i4) {
        Intent intent2 = new Intent("android.telephony.euicc.action.RESOLVE_ERROR");
        intent2.setPackage("com.android.phone");
        intent2.setComponent(new ComponentName("com.android.phone", "com.android.phone.euicc.EuiccResolutionUiDispatcherActivity"));
        intent2.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_ACTION", str);
        intent2.putExtra("android.service.euicc.extra.RESOLUTION_CALLING_PACKAGE", str2);
        intent2.putExtra("android.service.euicc.extra.RESOLVABLE_ERRORS", i);
        intent2.putExtra("android.service.euicc.extra.RESOLUTION_CARD_ID", i2);
        intent2.putExtra("android.service.euicc.extra.RESOLUTION_SUBSCRIPTION_ID", i4);
        intent2.putExtra("android.service.euicc.extra.RESOLUTION_PORT_INDEX", i3);
        intent2.putExtra("android.service.euicc.extra.RESOLUTION_USE_PORT_INDEX", z2);
        intent2.putExtra("android.service.euicc.extra.RESOLUTION_CONFIRMATION_CODE_RETRIED", z);
        intent2.putExtra(EXTRA_OPERATION, euiccOperation);
        intent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_INTENT", PendingIntent.getActivity(this.mContext, 0, intent2, 1107296256));
    }

    public void dump(FileDescriptor fileDescriptor, final PrintWriter printWriter, String[] strArr) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.DUMP", "Requires DUMP");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        printWriter.println("===== BEGIN EUICC CLINIC =====");
        try {
            try {
                printWriter.println("===== EUICC CONNECTOR =====");
                this.mConnector.dump(fileDescriptor, printWriter, strArr);
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                this.mConnector.dumpEuiccService(new EuiccConnector.DumpEuiccServiceCommandCallback(this) { // from class: com.android.internal.telephony.euicc.EuiccController.10
                    @Override // com.android.internal.telephony.euicc.EuiccConnector.DumpEuiccServiceCommandCallback
                    public void onDumpEuiccServiceComplete(String str) {
                        printWriter.println("===== EUICC SERVICE =====");
                        printWriter.println(str);
                        countDownLatch.countDown();
                    }

                    @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
                    public void onEuiccServiceUnavailable() {
                        printWriter.println("===== EUICC SERVICE UNAVAILABLE =====");
                        countDownLatch.countDown();
                    }
                });
                if (!countDownLatch.await(5L, TimeUnit.SECONDS)) {
                    printWriter.println("===== EUICC SERVICE TIMEOUT =====");
                }
            } catch (InterruptedException unused) {
                printWriter.println("===== EUICC SERVICE INTERRUPTED =====");
            }
        } finally {
            printWriter.println("===== END EUICC CLINIC =====");
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void sendOtaStatusChangedBroadcast() {
        Intent intent = new Intent("android.telephony.euicc.action.OTA_STATUS_CHANGED");
        ComponentInfo componentInfoFindBestComponent = EuiccConnector.findBestComponent(this.mContext.getPackageManager());
        if (componentInfoFindBestComponent != null) {
            intent.setPackage(componentInfoFindBestComponent.packageName);
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS");
    }

    private SubscriptionInfo getSubscriptionForSubscriptionId(int i) {
        List availableSubscriptionInfoList = this.mSubscriptionManager.getAvailableSubscriptionInfoList();
        int size = availableSubscriptionInfoList != null ? availableSubscriptionInfoList.size() : 0;
        for (int i2 = 0; i2 < size; i2++) {
            SubscriptionInfo subscriptionInfo = (SubscriptionInfo) availableSubscriptionInfoList.get(i2);
            if (i == subscriptionInfo.getSubscriptionId()) {
                return subscriptionInfo;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set getCurrentEmbeddedSubscriptionIds(int i) {
        List availableSubscriptionInfoList = this.mSubscriptionManager.getAvailableSubscriptionInfoList();
        int size = availableSubscriptionInfoList != null ? availableSubscriptionInfoList.size() : 0;
        ArraySet arraySet = new ArraySet();
        for (int i2 = 0; i2 < size; i2++) {
            SubscriptionInfo subscriptionInfo = (SubscriptionInfo) availableSubscriptionInfoList.get(i2);
            if (subscriptionInfo.isEmbedded() && subscriptionInfo.getCardId() == i) {
                arraySet.add(Integer.valueOf(subscriptionInfo.getSubscriptionId()));
            }
        }
        return arraySet;
    }

    private String blockingGetEidFromEuiccService(int i) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference atomicReference = new AtomicReference();
        this.mConnector.getEid(i, new EuiccConnector.GetEidCommandCallback(this) { // from class: com.android.internal.telephony.euicc.EuiccController.11
            @Override // com.android.internal.telephony.euicc.EuiccConnector.GetEidCommandCallback
            public void onGetEidComplete(String str) {
                atomicReference.set(str);
                countDownLatch.countDown();
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
                countDownLatch.countDown();
            }
        });
        return (String) awaitResult(countDownLatch, atomicReference);
    }

    private long blockingGetAvailableMemoryInBytesFromEuiccService(int i) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference atomicReference = new AtomicReference(-1L);
        final AtomicReference atomicReference2 = new AtomicReference();
        this.mConnector.getAvailableMemoryInBytes(i, new EuiccConnector.GetAvailableMemoryInBytesCommandCallback(this) { // from class: com.android.internal.telephony.euicc.EuiccController.12
            @Override // com.android.internal.telephony.euicc.EuiccConnector.GetAvailableMemoryInBytesCommandCallback
            public void onGetAvailableMemoryInBytesComplete(long j) {
                atomicReference.set(Long.valueOf(j));
                countDownLatch.countDown();
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.GetAvailableMemoryInBytesCommandCallback
            public void onUnsupportedOperationExceptionComplete(String str) {
                atomicReference2.set(new UnsupportedOperationException(str));
                countDownLatch.countDown();
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
                countDownLatch.countDown();
            }
        });
        try {
            return ((Long) awaitResultOrException(countDownLatch, atomicReference, atomicReference2)).longValue();
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Exception e2) {
            throw new UnsupportedOperationException(e2);
        }
    }

    private int blockingGetOtaStatusFromEuiccService(int i) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference atomicReference = new AtomicReference(5);
        this.mConnector.getOtaStatus(i, new EuiccConnector.GetOtaStatusCommandCallback(this) { // from class: com.android.internal.telephony.euicc.EuiccController.13
            @Override // com.android.internal.telephony.euicc.EuiccConnector.GetOtaStatusCommandCallback
            public void onGetOtaStatusComplete(int i2) {
                atomicReference.set(Integer.valueOf(i2));
                countDownLatch.countDown();
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
                countDownLatch.countDown();
            }
        });
        return ((Integer) awaitResult(countDownLatch, atomicReference)).intValue();
    }

    private EuiccInfo blockingGetEuiccInfoFromEuiccService(int i) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference atomicReference = new AtomicReference();
        this.mConnector.getEuiccInfo(i, new EuiccConnector.GetEuiccInfoCommandCallback(this) { // from class: com.android.internal.telephony.euicc.EuiccController.14
            @Override // com.android.internal.telephony.euicc.EuiccConnector.GetEuiccInfoCommandCallback
            public void onGetEuiccInfoComplete(EuiccInfo euiccInfo) {
                atomicReference.set(euiccInfo);
                countDownLatch.countDown();
            }

            @Override // com.android.internal.telephony.euicc.EuiccConnector.BaseEuiccCommandCallback
            public void onEuiccServiceUnavailable() {
                countDownLatch.countDown();
            }
        });
        return (EuiccInfo) awaitResult(countDownLatch, atomicReference);
    }

    private static Object awaitResult(CountDownLatch countDownLatch, AtomicReference atomicReference) {
        try {
            countDownLatch.await();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
        return atomicReference.get();
    }

    private static Object awaitResultOrException(CountDownLatch countDownLatch, AtomicReference atomicReference, AtomicReference atomicReference2) throws Exception {
        try {
            countDownLatch.await();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
        if (atomicReference2.get() != null) {
            throw ((Exception) atomicReference2.get());
        }
        return atomicReference.get();
    }

    public boolean checkCarrierPrivilegeInMetadata(DownloadableSubscription downloadableSubscription, String str) {
        return isPrivilegedFromUiccAccessRules(downloadableSubscription, str);
    }

    private boolean isPrivilegedFromUiccAccessRules(DownloadableSubscription downloadableSubscription, String str) {
        List accessRules = downloadableSubscription.getAccessRules();
        UiccAccessRule[] uiccAccessRuleArr = accessRules != null ? (UiccAccessRule[]) accessRules.toArray(new UiccAccessRule[accessRules.size()]) : null;
        if (uiccAccessRuleArr == null) {
            Log.e("EuiccController", "No access rules but caller is unprivileged");
            return false;
        }
        try {
            PackageInfo packageInfo = this.mPackageManager.getPackageInfo(str, 134217728);
            for (UiccAccessRule uiccAccessRule : uiccAccessRuleArr) {
                if (uiccAccessRule.getCarrierPrivilegeStatus(packageInfo) == 1) {
                    Log.i("EuiccController", "Calling package has carrier privilege to this profile");
                    return true;
                }
            }
            Log.e("EuiccController", "Calling package doesn't have carrier privilege to this profile");
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("EuiccController", "Calling package valid but gone");
            return false;
        }
    }

    public boolean isPrivilegedFromCarrierIdentifier(DownloadableSubscription downloadableSubscription, String str) {
        CarrierPrivilegesTracker carrierPrivilegesTracker;
        if (downloadableSubscription.getCarrierIdentifier() == null) {
            return false;
        }
        int carrierIdFromIdentifier = CarrierResolver.getCarrierIdFromIdentifier(this.mContext, downloadableSubscription.getCarrierIdentifier(), true);
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getCarrierId() == carrierIdFromIdentifier && (carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker()) != null && 1 == carrierPrivilegesTracker.getCarrierPrivilegeStatusForPackage(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean supportMultiActiveSlots() {
        return this.mTelephonyManager.getSupportedModemCount() > 1;
    }

    private boolean canManageActiveSubscriptionOnTargetSim(int i, String str, boolean z, int i2) {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList(false);
        if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() != 0) {
            for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                if (i == -1 || subscriptionInfo.getCardId() == i) {
                    if (subscriptionInfo.isEmbedded() && (!z || subscriptionInfo.getPortIndex() == i2)) {
                        if (canManageSubscription(subscriptionInfo, str)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean canManageSubscriptionOnTargetSim(final int i, String str, final boolean z, final int i2) {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList(false);
        if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() != 0) {
            if (supportMultiActiveSlots()) {
                List<UiccCardInfo> uiccCardsInfo = this.mTelephonyManager.getUiccCardsInfo();
                if (uiccCardsInfo != null && !uiccCardsInfo.isEmpty()) {
                    for (UiccCardInfo uiccCardInfo : uiccCardsInfo) {
                        if (uiccCardInfo != null && uiccCardInfo.getCardId() == i && uiccCardInfo.isEuicc()) {
                            if (activeSubscriptionInfoList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.euicc.EuiccController$$ExternalSyntheticLambda1
                                @Override // java.util.function.Predicate
                                public final boolean test(Object obj) {
                                    return EuiccController.m1027$r8$lambda$OCX8naQrMDGWkMETbVcRigzrc(i, z, i2, (SubscriptionInfo) obj);
                                }
                            })) {
                                for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                                    if (subscriptionInfo.isEmbedded() && subscriptionInfo.getCardId() == i && (!z || subscriptionInfo.getPortIndex() == i2)) {
                                        if (canManageSubscription(subscriptionInfo, str)) {
                                            return true;
                                        }
                                    }
                                }
                                Log.i("EuiccController", "canManageSubscriptionOnTargetSim cannot manage embedded subscription");
                                return false;
                            }
                            long jClearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                return this.mTelephonyManager.checkCarrierPrivilegesForPackageAnyPhone(str) == 1;
                            } finally {
                                Binder.restoreCallingIdentity(jClearCallingIdentity);
                            }
                        }
                    }
                    Log.i("EuiccController", "The target SIM is not an eUICC.");
                }
                return false;
            }
            for (SubscriptionInfo subscriptionInfo2 : activeSubscriptionInfoList) {
                if (subscriptionInfo2.isEmbedded() && canManageSubscription(subscriptionInfo2, str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: renamed from: $r8$lambda$OCX8naQrMDGWkMET-bVcRig-zrc, reason: not valid java name */
    public static /* synthetic */ boolean m1027$r8$lambda$OCX8naQrMDGWkMETbVcRigzrc(int i, boolean z, int i2, SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo.isEmbedded() && subscriptionInfo.getCardId() == i) {
            return !z || subscriptionInfo.getPortIndex() == i2;
        }
        return false;
    }

    private boolean callerCanReadPhoneStatePrivileged() {
        return this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") == 0;
    }

    private boolean callerCanReadPhoneState() {
        return this.mContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0;
    }

    private boolean callerCanWriteEmbeddedSubscriptions() {
        return this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS") == 0;
    }

    private DevicePolicyManager retrieveDevicePolicyManagerFromUserContext(UserHandle userHandle) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Context context = this.mContext;
            return (DevicePolicyManager) context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).getSystemService(DevicePolicyManager.class);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("EuiccController", "Unknown package name");
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private boolean isSubscriptionDevicePolicyManaged(SubscriptionInfo subscriptionInfo, String str) {
        DevicePolicyManager devicePolicyManager = getDevicePolicyManager();
        return devicePolicyManager != null && devicePolicyManager.isSubscriptionEnterpriseManaged(subscriptionInfo, str);
    }

    private boolean callerCanManageDevicePolicyManagedSubscriptions(String str) {
        DevicePolicyManager devicePolicyManager = getDevicePolicyManager();
        return (devicePolicyManager != null && (devicePolicyManager.isProfileOwnerApp(str) || devicePolicyManager.isDeviceOwnerApp(str))) || this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_DEVICE_POLICY_MANAGED_SUBSCRIPTIONS") == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean callerCanManageDevicePolicyManagedSubscriptions(String str, long j) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Binder.restoreCallingIdentity(j);
            return callerCanManageDevicePolicyManagedSubscriptions(str);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private boolean shouldAllowSwitchAfterDownload(String str) {
        DevicePolicyManager devicePolicyManager = getDevicePolicyManager();
        if (devicePolicyManager == null) {
            return false;
        }
        if (devicePolicyManager.isDeviceOwnerApp(str)) {
            return true;
        }
        return devicePolicyManager.isProfileOwnerApp(str) && devicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    private DevicePolicyManager getDevicePolicyManager() {
        DevicePolicyManager devicePolicyManagerRetrieveDevicePolicyManagerFromUserContext = retrieveDevicePolicyManagerFromUserContext(Binder.getCallingUserHandle());
        if (devicePolicyManagerRetrieveDevicePolicyManagerFromUserContext == null) {
            Log.w("EuiccController", "Unable to get device policy manager");
        }
        return devicePolicyManagerRetrieveDevicePolicyManagerFromUserContext;
    }

    public boolean isSimPortAvailable(int i, int i2, String str) {
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        enforceTelephonyFeatureWithException(str, "isSimPortAvailable");
        boolean zIsCompatChangeEnabled = isCompatChangeEnabled(str, 240273417L);
        boolean zCallerCanWriteEmbeddedSubscriptions = callerCanWriteEmbeddedSubscriptions();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Iterator<UiccCardInfo> it = this.mTelephonyManager.getUiccCardsInfo().iterator();
            while (true) {
                boolean z = false;
                if (!it.hasNext()) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return false;
                }
                UiccCardInfo next = it.next();
                if (next != null && next.getCardId() == i) {
                    if (next.isEuicc() && i2 != -1 && i2 < next.getPorts().size()) {
                        for (UiccPortInfo uiccPortInfo : next.getPorts()) {
                            if (uiccPortInfo != null && uiccPortInfo.getPortIndex() == i2) {
                                if (!uiccPortInfo.isActive()) {
                                    if (!zIsCompatChangeEnabled) {
                                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                                        return false;
                                    }
                                    boolean z2 = getRemovableNonEuiccSlot() != null && getRemovableNonEuiccSlot().isActive();
                                    boolean z3 = this.mTelephonyManager.checkCarrierPrivilegesForPackageAnyPhone(str) == 1;
                                    if (this.mTelephonyManager.isMultiSimEnabled() && z2 && !isRemovalNonEuiccSlotHasActiveSubscription() && (z3 || zCallerCanWriteEmbeddedSubscriptions)) {
                                        z = true;
                                    }
                                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                                    return z;
                                }
                                if (TextUtils.isEmpty(uiccPortInfo.getIccId())) {
                                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                                    return true;
                                }
                                UiccPort uiccPortForSlot = UiccController.getInstance().getUiccPortForSlot(next.getPhysicalSlotIndex(), i2);
                                if (uiccPortForSlot != null && uiccPortForSlot.getUiccProfile() != null && uiccPortForSlot.getUiccProfile().isEmptyProfile()) {
                                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                                    return true;
                                }
                                Phone phone = PhoneFactory.getPhone(uiccPortInfo.getLogicalSlotIndex());
                                if (phone == null) {
                                    Log.e("EuiccController", "Invalid logical slot: " + uiccPortInfo.getLogicalSlotIndex());
                                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                                    return false;
                                }
                                CarrierPrivilegesTracker carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker();
                                if (carrierPrivilegesTracker == null) {
                                    Log.e("EuiccController", "No CarrierPrivilegesTracker");
                                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                                    return false;
                                }
                                z = carrierPrivilegesTracker.getCarrierPrivilegeStatusForPackage(str) == 1;
                                Binder.restoreCallingIdentity(jClearCallingIdentity);
                                return z;
                            }
                        }
                    }
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return false;
                }
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean hasCarrierPrivilegesForPackageOnAnyPhone(String str) {
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mTelephonyManager.checkCarrierPrivilegesForPackageAnyPhone(str) == 1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isCompatChangeEnabled(String str, long j) {
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        boolean zIsChangeEnabled = CompatChanges.isChangeEnabled(j, str, Binder.getCallingUserHandle());
        Log.i("EuiccController", "isCompatChangeEnabled changeId: " + j + " changeEnabled: " + zIsChangeEnabled);
        return zIsChangeEnabled;
    }

    public void setPsimConversionSupportedCarriers(int[] iArr) {
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to set pSIM conversion supported carriers");
        } else {
            this.mPsimConversionSupportedCarrierIds = (List) Arrays.stream(iArr).boxed().collect(Collectors.toList());
        }
    }

    public boolean isPsimConversionSupported(int i) {
        if (!callerCanWriteEmbeddedSubscriptions()) {
            SmsController$$ExternalSyntheticBUOutline0.m("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to check if the carrier is supported pSIM conversion");
            return false;
        }
        List list = this.mPsimConversionSupportedCarrierIds;
        if (list == null || list.isEmpty()) {
            return false;
        }
        return this.mPsimConversionSupportedCarrierIds.contains(Integer.valueOf(i));
    }

    private void enforceTelephonyFeatureWithException(String str, String str2) {
        if (SystemProperties.getBoolean("ro.radio.noril", false)) {
            return;
        }
        TelephonyUtils.enforceTelephonyFeatureWithException(str, this.mPackageManager, this.mVendorApiLevel, "android.hardware.telephony.euicc", str2);
    }

    private boolean canManageSubscription(SubscriptionInfo subscriptionInfo, String str) {
        boolean zIsHeadlessSystemUserMode = UserManager.isHeadlessSystemUserMode();
        SubscriptionManager subscriptionManager = this.mSubscriptionManager;
        if (zIsHeadlessSystemUserMode) {
            return subscriptionManager.canManageSubscriptionAsUser(subscriptionInfo, str, UserHandle.of(ActivityManager.getCurrentUser()));
        }
        return subscriptionManager.canManageSubscription(subscriptionInfo, str);
    }
}
