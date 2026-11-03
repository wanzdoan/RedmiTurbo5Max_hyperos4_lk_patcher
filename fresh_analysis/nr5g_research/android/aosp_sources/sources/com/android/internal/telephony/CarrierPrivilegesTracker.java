package com.android.internal.telephony;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.content.pm.UserInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyRegistryManager;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.util.WorkerThread;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class CarrierPrivilegesTracker extends Handler {
    private static final long CLEAR_UICC_RULES_DELAY_MILLIS = 0;
    private static final String TAG = "CarrierPrivilegesTracker";
    private final Map mCachedUids;
    private final CarrierConfigManager mCarrierConfigManager;
    private final List mCarrierConfigRules;
    private long mClearUiccRulesUptimeMillis;
    private final Context mContext;
    private volatile Handler mCurrentHandler;
    private final FeatureFlags mFeatureFlags;
    private final Map mInstalledPackageCertHashes;
    private final BroadcastReceiver mIntentReceiver;
    private final LocalLog mLocalLog;
    private final PackageManager mPackageManager;
    private final Phone mPhone;
    private PrivilegedPackageInfo mPrivilegedPackageInfo;
    private final ReadWriteLock mPrivilegedPackageInfoLock;
    private boolean mSimIsReadyButNotLoaded;
    private final TelephonyManager mTelephonyManager;
    private final TelephonyRegistryManager mTelephonyRegistryManager;
    private String mTestOverrideCarrierServicePackage;
    private List mTestOverrideRules;
    private final List mUiccRules;
    private final UserManager mUserManager;

    /* JADX INFO: Access modifiers changed from: private */
    static final class PrivilegedPackageInfo {
        final Pair mCarrierService;
        final Set mPackageNames;
        final Set mUids;

        PrivilegedPackageInfo() {
            Set set = Collections.EMPTY_SET;
            this.mPackageNames = set;
            this.mUids = set;
            this.mCarrierService = new Pair(null, -1);
        }

        PrivilegedPackageInfo(Set set, Set set2, Pair pair) {
            this.mPackageNames = set;
            this.mUids = set2;
            this.mCarrierService = pair;
        }

        public String toString() {
            return "{packageNames=" + CarrierPrivilegesTracker.getObfuscatedPackages(this.mPackageNames, new Function() { // from class: com.android.internal.telephony.CarrierPrivilegesTracker$PrivilegedPackageInfo$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return Rlog.pii(CarrierPrivilegesTracker.TAG, (String) obj);
                }
            }) + ", uids=" + this.mUids + ", carrierServicePackageName=" + Rlog.pii(CarrierPrivilegesTracker.TAG, this.mCarrierService.first) + ", carrierServiceUid=" + this.mCarrierService.second + "}";
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PrivilegedPackageInfo)) {
                return false;
            }
            PrivilegedPackageInfo privilegedPackageInfo = (PrivilegedPackageInfo) obj;
            return this.mPackageNames.equals(privilegedPackageInfo.mPackageNames) && this.mUids.equals(privilegedPackageInfo.mUids) && this.mCarrierService.equals(privilegedPackageInfo.mCarrierService);
        }

        public int hashCode() {
            return Objects.hash(this.mPackageNames, this.mUids, this.mCarrierService);
        }
    }

    public CarrierPrivilegesTracker(Looper looper, Phone phone, Context context, FeatureFlags featureFlags) {
        super(looper);
        this.mLocalLog = new LocalLog(64);
        this.mCarrierConfigRules = new ArrayList();
        this.mUiccRules = new ArrayList();
        this.mTestOverrideRules = null;
        this.mTestOverrideCarrierServicePackage = null;
        this.mInstalledPackageCertHashes = new ArrayMap();
        this.mCachedUids = new ArrayMap();
        this.mPrivilegedPackageInfoLock = new ReentrantReadWriteLock();
        this.mPrivilegedPackageInfo = new PrivilegedPackageInfo();
        this.mClearUiccRulesUptimeMillis = -1L;
        this.mSimIsReadyButNotLoaded = false;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.CarrierPrivilegesTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                action.hashCode();
                boolean z = true;
                boolean z2 = false;
                switch (action) {
                    case "android.telephony.action.SIM_APPLICATION_STATE_CHANGED":
                    case "android.telephony.action.SIM_CARD_STATE_CHANGED":
                        Bundle extras = intent.getExtras();
                        int i = extras.getInt("android.telephony.extra.SIM_STATE", 0);
                        int i2 = extras.getInt("phone", -1);
                        if (i == 1 || i == 6 || i == 5 || i == 10) {
                            CarrierPrivilegesTracker.this.mCurrentHandler.sendMessage(CarrierPrivilegesTracker.this.mCurrentHandler.obtainMessage(4, i2, i));
                            break;
                        }
                        break;
                    case "android.intent.action.PACKAGE_REPLACED":
                    case "android.intent.action.PACKAGE_CHANGED":
                    case "android.intent.action.PACKAGE_REMOVED":
                    case "android.intent.action.PACKAGE_ADDED":
                        Uri data = intent.getData();
                        String schemeSpecificPart = data != null ? data.getSchemeSpecificPart() : null;
                        if (TextUtils.isEmpty(schemeSpecificPart)) {
                            Rlog.e(CarrierPrivilegesTracker.TAG, "Failed to get package from Intent");
                            break;
                        } else {
                            boolean zEquals = action.equals("android.intent.action.PACKAGE_REMOVED");
                            try {
                                z2 = action.equals("android.intent.action.PACKAGE_CHANGED") && CarrierPrivilegesTracker.this.getApplicationEnabledSetting(schemeSpecificPart) == 3;
                                z = false;
                            } catch (IllegalArgumentException unused) {
                                Rlog.w(CarrierPrivilegesTracker.TAG, "Package does not exist: " + schemeSpecificPart);
                            }
                            CarrierPrivilegesTracker.this.mCurrentHandler.sendMessage(CarrierPrivilegesTracker.this.mCurrentHandler.obtainMessage((zEquals || z2 || z) ? 6 : 5, schemeSpecificPart));
                            break;
                        }
                        break;
                }
            }
        };
        this.mIntentReceiver = broadcastReceiver;
        this.mPhone = phone;
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService("user");
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        this.mCarrierConfigManager = carrierConfigManager;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mTelephonyRegistryManager = (TelephonyRegistryManager) context.getSystemService("telephony_registry");
        this.mCurrentHandler = new Handler(WorkerThread.get().getLooper()) { // from class: com.android.internal.telephony.CarrierPrivilegesTracker.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                CarrierPrivilegesTracker carrierPrivilegesTracker = CarrierPrivilegesTracker.this;
                if (i == 7) {
                    carrierPrivilegesTracker.handleInitializeTracker();
                    CarrierPrivilegesTracker.this.sendEmptyMessage(12);
                    if (hasMessagesOrCallbacks()) {
                        return;
                    }
                    CarrierPrivilegesTracker carrierPrivilegesTracker2 = CarrierPrivilegesTracker.this;
                    carrierPrivilegesTracker2.mCurrentHandler = carrierPrivilegesTracker2;
                    return;
                }
                Message messageObtainMessage = carrierPrivilegesTracker.obtainMessage();
                messageObtainMessage.copyFrom(message);
                messageObtainMessage.sendToTarget();
                if (hasMessagesOrCallbacks()) {
                    return;
                }
                CarrierPrivilegesTracker carrierPrivilegesTracker3 = CarrierPrivilegesTracker.this;
                carrierPrivilegesTracker3.mCurrentHandler = carrierPrivilegesTracker3;
            }
        };
        this.mCurrentHandler.sendMessage(obtainMessage(7));
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda1
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$1(i, i2, i3, i4);
                }
            });
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        context.registerReceiverAsUser(broadcastReceiver, UserHandle.of(ActivityManager.getCurrentUser()), intentFilter, null, null);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter2.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter2.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter2.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter2.addDataScheme("package");
        context.registerReceiverAsUser(broadcastReceiver, UserHandle.of(ActivityManager.getCurrentUser()), intentFilter2, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(final int i, final int i2, int i3, int i4) {
        this.mCurrentHandler.post(new Runnable() { // from class: com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(i2, i);
            }
        });
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 4:
                handleSimStateChanged(message.arg1, message.arg2);
                return;
            case 5:
                handlePackageAddedReplacedOrChanged((String) message.obj);
                return;
            case 6:
                handlePackageRemovedOrDisabledByUser((String) message.obj);
                return;
            case 7:
            default:
                Rlog.e(TAG, "Received unknown msg type: " + message.what);
                return;
            case 8:
                handleSetTestOverrideRules((String) message.obj);
                return;
            case 9:
                handleClearUiccRules();
                return;
            case 10:
                handleUiccAccessRulesLoaded();
                return;
            case 11:
                handleSetTestOverrideCarrierServicePackage((String) message.obj);
                return;
            case 12:
                synchronized (this) {
                    break;
                }
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleCarrierConfigUpdated, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0(int i, int i2) {
        if (i2 != this.mPhone.getPhoneId()) {
            return;
        }
        List carrierConfigRules = Collections.EMPTY_LIST;
        if (i != -1) {
            carrierConfigRules = getCarrierConfigRules(i);
        }
        this.mLocalLog.log("CarrierConfigUpdated: subId=" + i + " slotIndex=" + i2 + " updated CarrierConfig rules=" + carrierConfigRules);
        maybeUpdateRulesAndNotifyRegistrants(this.mCarrierConfigRules, carrierConfigRules);
    }

    private List getCarrierConfigRules(int i) {
        PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mContext, i, new String[]{"carrier_certificate_string_array"});
        if (this.mCarrierConfigManager == null || !CarrierConfigManager.isConfigForIdentifiedCarrier(carrierConfigSubset)) {
            return Collections.EMPTY_LIST;
        }
        String[] stringArray = carrierConfigSubset.getStringArray("carrier_certificate_string_array");
        if (stringArray == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(UiccAccessRule.decodeRulesFromCarrierConfig(stringArray));
    }

    private void handleSimStateChanged(int i, int i2) {
        if (i != this.mPhone.getPhoneId()) {
            return;
        }
        String str = TAG;
        Rlog.d(str, "handleSimStateChanged: slotId=" + i + " simState=" + i2);
        List list = Collections.EMPTY_LIST;
        this.mPrivilegedPackageInfoLock.writeLock().lock();
        try {
            this.mSimIsReadyButNotLoaded = i2 == 5;
            this.mPrivilegedPackageInfoLock.writeLock().unlock();
            if (i2 == 10) {
                Rlog.d(str, "handleSimStateChanged: SIM fully loaded.");
                handleUiccAccessRulesLoaded();
                return;
            }
            if (!this.mUiccRules.isEmpty() && this.mClearUiccRulesUptimeMillis == -1) {
                long jUptimeMillis = SystemClock.uptimeMillis();
                long j = CLEAR_UICC_RULES_DELAY_MILLIS;
                this.mClearUiccRulesUptimeMillis = jUptimeMillis + j;
                this.mCurrentHandler.sendMessageAtTime(obtainMessage(9), this.mClearUiccRulesUptimeMillis);
                Rlog.d(str, "handleSimStateChanged: SIM is gone, simState=" + TelephonyManager.simStateToString(i2) + ". Delay " + (j / 1000) + " seconds to clear UICC rules.");
                return;
            }
            Rlog.d(str, "handleSimStateChanged: Ignore SIM gone event while UiccRules is empty or waiting to be emptied.");
        } catch (Throwable th) {
            this.mPrivilegedPackageInfoLock.writeLock().unlock();
            throw th;
        }
    }

    private void handleUiccAccessRulesLoaded() {
        this.mClearUiccRulesUptimeMillis = -1L;
        removeMessages(9);
        List simRules = getSimRules();
        this.mLocalLog.log("UiccAccessRules loaded: updated SIM-loaded rules=" + simRules);
        maybeUpdateRulesAndNotifyRegistrants(this.mUiccRules, simRules);
    }

    public void onUiccAccessRulesLoaded() {
        this.mCurrentHandler.sendEmptyMessage(10);
    }

    private void handleClearUiccRules() {
        this.mClearUiccRulesUptimeMillis = -1L;
        removeMessages(9);
        maybeUpdateRulesAndNotifyRegistrants(this.mUiccRules, Collections.EMPTY_LIST);
    }

    private List getSimRules() {
        if (!this.mTelephonyManager.hasIccCard(this.mPhone.getPhoneId())) {
            return Collections.EMPTY_LIST;
        }
        UiccPort uiccPort = this.mPhone.getUiccPort();
        if (uiccPort == null) {
            Rlog.w(TAG, "Null UiccPort, but hasIccCard was present for phoneId " + this.mPhone.getPhoneId());
            return Collections.EMPTY_LIST;
        }
        UiccProfile uiccProfile = uiccPort.getUiccProfile();
        if (uiccProfile == null) {
            Rlog.w(TAG, "Null UiccProfile, but hasIccCard was true for phoneId " + this.mPhone.getPhoneId());
            return Collections.EMPTY_LIST;
        }
        return uiccProfile.getCarrierPrivilegeAccessRules();
    }

    private PackageInfo getPackageInfoForPackage(String str) {
        if (str == null) {
            return null;
        }
        try {
            return this.mPackageManager.getPackageInfoAsUser(str, 671121408, ActivityManager.getCurrentUser());
        } catch (PackageManager.NameNotFoundException e) {
            Rlog.w(TAG, "Error getting installed package: ".concat(str), e);
            return null;
        }
    }

    private void handlePackageAddedReplacedOrChanged(String str) {
        PackageInfo packageInfoForPackage = getPackageInfoForPackage(str);
        if (packageInfoForPackage == null) {
            return;
        }
        updateCertHashHashesForPackage(packageInfoForPackage);
        getUidsForPackage(packageInfoForPackage.packageName, true);
        maybeUpdatePrivilegedPackagesAndNotifyRegistrants();
    }

    private void updateCertHashHashesForPackage(PackageInfo packageInfo) {
        ArraySet arraySet = new ArraySet(2);
        for (Signature signature : UiccAccessRule.getSignatures(packageInfo)) {
            arraySet.add(Integer.valueOf(UiccAccessRule.getCertificateHashHashCode(UiccAccessRule.getCertHash(signature, "SHA-1"))));
            arraySet.add(Integer.valueOf(UiccAccessRule.getCertificateHashHashCode(UiccAccessRule.getCertHash(signature, "SHA-256"))));
        }
        this.mInstalledPackageCertHashes.put(packageInfo.packageName, arraySet);
    }

    private Set getCertsForPackage(String str) {
        PackageInfo packageInfoForPackage = getPackageInfoForPackage(str);
        if (packageInfoForPackage == null) {
            return Collections.EMPTY_SET;
        }
        List<Signature> signatures = UiccAccessRule.getSignatures(packageInfoForPackage);
        ArraySet arraySet = new ArraySet(2);
        for (Signature signature : signatures) {
            arraySet.add(UiccAccessRule.getCertHash(signature, "SHA-1"));
            arraySet.add(UiccAccessRule.getCertHash(signature, "SHA-256"));
        }
        return arraySet;
    }

    private void handlePackageRemovedOrDisabledByUser(String str) {
        if (str == null) {
            return;
        }
        if (this.mInstalledPackageCertHashes.remove(str) == null || this.mCachedUids.remove(str) == null) {
            Rlog.e(TAG, "Unknown package was uninstalled or disabled by user: ".concat(str));
        } else {
            maybeUpdatePrivilegedPackagesAndNotifyRegistrants();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitializeTracker() {
        this.mCarrierConfigRules.addAll(getCarrierConfigRules(this.mPhone.getSubId()));
        this.mUiccRules.addAll(getSimRules());
        refreshInstalledPackageCache();
        maybeUpdatePrivilegedPackagesAndNotifyRegistrants();
        this.mLocalLog.log("Initializing state: CarrierConfig rules=" + this.mCarrierConfigRules + " SIM-loaded rules=" + this.mUiccRules);
    }

    private void refreshInstalledPackageCache() {
        for (PackageInfo packageInfo : this.mPackageManager.getInstalledPackagesAsUser(671121408, ActivityManager.getCurrentUser())) {
            updateCertHashHashesForPackage(packageInfo);
            getUidsForPackage(packageInfo.packageName, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getObfuscatedPackages(Collection collection, Function function) {
        StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            stringJoiner.add((CharSequence) function.apply(it.next()));
        }
        return stringJoiner.toString();
    }

    private void maybeUpdateRulesAndNotifyRegistrants(List list, List list2) {
        if (list.equals(list2)) {
            return;
        }
        list.clear();
        list.addAll(list2);
        maybeUpdatePrivilegedPackagesAndNotifyRegistrants();
    }

    private void maybeUpdatePrivilegedPackagesAndNotifyRegistrants() {
        PrivilegedPackageInfo currentPrivilegedPackagesForAllUsers = getCurrentPrivilegedPackagesForAllUsers();
        this.mPrivilegedPackageInfoLock.readLock().lock();
        try {
            if (!this.mPrivilegedPackageInfo.equals(currentPrivilegedPackagesForAllUsers)) {
                this.mLocalLog.log("Privileged packages info changed. New state = " + currentPrivilegedPackagesForAllUsers);
                boolean zEquals = currentPrivilegedPackagesForAllUsers.mPackageNames.equals(this.mPrivilegedPackageInfo.mPackageNames);
                boolean zEquals2 = currentPrivilegedPackagesForAllUsers.mUids.equals(this.mPrivilegedPackageInfo.mUids);
                boolean zEquals3 = currentPrivilegedPackagesForAllUsers.mCarrierService.equals(this.mPrivilegedPackageInfo.mCarrierService);
                this.mPrivilegedPackageInfoLock.readLock().unlock();
                this.mPrivilegedPackageInfoLock.writeLock().lock();
                try {
                    this.mPrivilegedPackageInfo = currentPrivilegedPackagesForAllUsers;
                    this.mPrivilegedPackageInfoLock.writeLock().unlock();
                    this.mPrivilegedPackageInfoLock.readLock().lock();
                    if (!zEquals || !zEquals2) {
                        try {
                            this.mTelephonyRegistryManager.notifyCarrierPrivilegesChanged(this.mPhone.getPhoneId(), Collections.unmodifiableSet(this.mPrivilegedPackageInfo.mPackageNames), Collections.unmodifiableSet(this.mPrivilegedPackageInfo.mUids));
                        } catch (Throwable th) {
                            this.mPrivilegedPackageInfoLock.readLock().unlock();
                            throw th;
                        }
                    }
                    if (!zEquals3) {
                        TelephonyRegistryManager telephonyRegistryManager = this.mTelephonyRegistryManager;
                        int phoneId = this.mPhone.getPhoneId();
                        Pair pair = this.mPrivilegedPackageInfo.mCarrierService;
                        telephonyRegistryManager.notifyCarrierServiceChanged(phoneId, (String) pair.first, ((Integer) pair.second).intValue());
                    }
                    this.mPrivilegedPackageInfoLock.readLock().unlock();
                    CarrierAppUtils.disableCarrierAppsUntilPrivileged(this.mContext.getOpPackageName(), this.mTelephonyManager, ActivityManager.getCurrentUser(), this.mContext);
                    return;
                } catch (Throwable th2) {
                    this.mPrivilegedPackageInfoLock.writeLock().unlock();
                    throw th2;
                }
            }
            this.mPrivilegedPackageInfoLock.readLock().unlock();
        } catch (Throwable th3) {
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            throw th3;
        }
    }

    private PrivilegedPackageInfo getCurrentPrivilegedPackagesForAllUsers() {
        ArraySet arraySet = new ArraySet();
        ArraySet arraySet2 = new ArraySet();
        ArraySet arraySet3 = new ArraySet();
        int i = 0;
        int i2 = 0;
        for (Map.Entry entry : this.mInstalledPackageCertHashes.entrySet()) {
            if (isPackageMaybePrivileged((String) entry.getKey(), (Set) entry.getValue())) {
                int packagePrivilegedStatus = getPackagePrivilegedStatus((String) entry.getKey(), getCertsForPackage((String) entry.getKey()));
                if (packagePrivilegedStatus == 1) {
                    i2++;
                } else if (packagePrivilegedStatus == 2 || packagePrivilegedStatus == 3) {
                    arraySet.add((String) entry.getKey());
                    i++;
                }
                arraySet2.add((String) entry.getKey());
                arraySet3.addAll(getUidsForPackage((String) entry.getKey(), false));
            }
        }
        TelephonyStatsLog.write(1252, this.mPhone.getSpecificCarrierId(), i, i2);
        return new PrivilegedPackageInfo(arraySet2, arraySet3, getCarrierService(arraySet));
    }

    private boolean isPackageMaybePrivileged(String str, Set set) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Integer num = (Integer) it.next();
            List list = this.mTestOverrideRules;
            if (list != null) {
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    if (((UiccAccessRule) it2.next()).hasMatchingCertificateHashHashAndPackageName(num.intValue(), str)) {
                        return true;
                    }
                }
            } else {
                Iterator it3 = this.mUiccRules.iterator();
                while (it3.hasNext()) {
                    if (((UiccAccessRule) it3.next()).hasMatchingCertificateHashHashAndPackageName(num.intValue(), str)) {
                        return true;
                    }
                }
                Iterator it4 = this.mCarrierConfigRules.iterator();
                while (it4.hasNext()) {
                    if (((UiccAccessRule) it4.next()).hasMatchingCertificateHashHashAndPackageName(num.intValue(), str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int getPackagePrivilegedStatus(String str, Set set) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            byte[] bArr = (byte[]) it.next();
            List list = this.mTestOverrideRules;
            if (list != null) {
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    if (((UiccAccessRule) it2.next()).hasMatchingCertificateHashAndPackageName(bArr, str)) {
                        return 2;
                    }
                }
            } else {
                Iterator it3 = this.mUiccRules.iterator();
                while (it3.hasNext()) {
                    if (((UiccAccessRule) it3.next()).hasMatchingCertificateHashAndPackageName(bArr, str)) {
                        return 2;
                    }
                }
                Iterator it4 = this.mCarrierConfigRules.iterator();
                while (it4.hasNext()) {
                    if (((UiccAccessRule) it4.next()).hasMatchingCertificateHashAndPackageName(bArr, str)) {
                        return str.equals(this.mTestOverrideCarrierServicePackage) ? 3 : 1;
                    }
                }
            }
        }
        return 0;
    }

    private Set getUidsForPackage(String str, boolean z) {
        if (z) {
            this.mCachedUids.remove(str);
        }
        if (this.mCachedUids.containsKey(str)) {
            return (Set) this.mCachedUids.get(str);
        }
        ArraySet arraySet = new ArraySet(1);
        Iterator it = this.mUserManager.getUsers().iterator();
        while (it.hasNext()) {
            int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
            try {
                arraySet.add(Integer.valueOf(this.mPackageManager.getPackageUidAsUser(str, identifier)));
            } catch (PackageManager.NameNotFoundException unused) {
                Rlog.e(TAG, "Unable to find uid for package " + str + " and user " + identifier);
            }
        }
        this.mCachedUids.put(str, arraySet);
        return arraySet;
    }

    private int getPackageUid(String str) {
        try {
            return this.mPackageManager.getPackageUidAsUser(str, ActivityManager.getCurrentUser());
        } catch (PackageManager.NameNotFoundException unused) {
            Rlog.e(TAG, "Unable to find uid for package " + str);
            return -1;
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("CarrierPrivilegesTracker - phoneId: " + this.mPhone.getPhoneId());
        printWriter.println("CarrierPrivilegesTracker - Log Begin ----");
        this.mLocalLog.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("CarrierPrivilegesTracker - Log End ----");
        this.mPrivilegedPackageInfoLock.readLock().lock();
        try {
            printWriter.println("CarrierPrivilegesTracker - Privileged package info: " + this.mPrivilegedPackageInfo);
            printWriter.println("mSimIsReadyButNotLoaded: " + this.mSimIsReadyButNotLoaded);
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            printWriter.println("CarrierPrivilegesTracker - Test-override rules: " + this.mTestOverrideRules);
            printWriter.println("CarrierPrivilegesTracker - SIM-loaded rules: " + this.mUiccRules);
            printWriter.println("CarrierPrivilegesTracker - Carrier config rules: " + this.mCarrierConfigRules);
            printWriter.println("mClearUiccRulesUptimeMillis: " + this.mClearUiccRulesUptimeMillis);
        } catch (Throwable th) {
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            throw th;
        }
    }

    public void setTestOverrideCarrierPrivilegeRules(String str) {
        this.mCurrentHandler.sendMessage(obtainMessage(8, str));
    }

    public void setTestOverrideCarrierServicePackage(String str) {
        this.mCurrentHandler.sendMessage(obtainMessage(11, str));
    }

    private void handleSetTestOverrideCarrierServicePackage(String str) {
        this.mTestOverrideCarrierServicePackage = str;
        refreshInstalledPackageCache();
        maybeUpdatePrivilegedPackagesAndNotifyRegistrants();
    }

    private void handleSetTestOverrideRules(String str) {
        if (str == null) {
            this.mTestOverrideRules = null;
        } else if (str.isEmpty()) {
            this.mTestOverrideRules = Collections.EMPTY_LIST;
        } else {
            this.mTestOverrideRules = Arrays.asList(UiccAccessRule.decodeRulesFromCarrierConfig(new String[]{str}));
            refreshInstalledPackageCache();
        }
        maybeUpdatePrivilegedPackagesAndNotifyRegistrants();
    }

    public int getCarrierPrivilegeStatusForPackage(String str) {
        if (str == null) {
            return 0;
        }
        this.mPrivilegedPackageInfoLock.readLock().lock();
        try {
            if (!this.mSimIsReadyButNotLoaded) {
                boolean zContains = this.mPrivilegedPackageInfo.mPackageNames.contains(str);
                ReadWriteLock readWriteLock = this.mPrivilegedPackageInfoLock;
                if (zContains) {
                    readWriteLock.readLock().unlock();
                    return 1;
                }
                readWriteLock.readLock().unlock();
                return 0;
            }
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            return -1;
        } catch (Throwable th) {
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            throw th;
        }
    }

    public Set<String> getPackagesWithCarrierPrivileges() {
        this.mPrivilegedPackageInfoLock.readLock().lock();
        try {
            return this.mSimIsReadyButNotLoaded ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.mPrivilegedPackageInfo.mPackageNames);
        } finally {
            this.mPrivilegedPackageInfoLock.readLock().unlock();
        }
    }

    public int getCarrierPrivilegeStatusForUid(int i) {
        this.mPrivilegedPackageInfoLock.readLock().lock();
        try {
            if (!this.mSimIsReadyButNotLoaded) {
                boolean zContains = this.mPrivilegedPackageInfo.mUids.contains(Integer.valueOf(i));
                ReadWriteLock readWriteLock = this.mPrivilegedPackageInfoLock;
                if (zContains) {
                    readWriteLock.readLock().unlock();
                    return 1;
                }
                readWriteLock.readLock().unlock();
                return 0;
            }
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            return -1;
        } catch (Throwable th) {
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            throw th;
        }
    }

    public String getCarrierServicePackageName() {
        this.mPrivilegedPackageInfoLock.readLock().lock();
        try {
            if (!this.mSimIsReadyButNotLoaded) {
                return (String) this.mPrivilegedPackageInfo.mCarrierService.first;
            }
            return null;
        } finally {
            this.mPrivilegedPackageInfoLock.readLock().unlock();
        }
    }

    public int getCarrierServicePackageUid() {
        this.mPrivilegedPackageInfoLock.readLock().lock();
        try {
            if (!this.mSimIsReadyButNotLoaded) {
                return ((Integer) this.mPrivilegedPackageInfo.mCarrierService.second).intValue();
            }
            return -1;
        } finally {
            this.mPrivilegedPackageInfoLock.readLock().unlock();
        }
    }

    public List<String> getCarrierPackageNamesForIntent(Intent intent) {
        this.mPrivilegedPackageInfoLock.readLock().lock();
        try {
            if (this.mSimIsReadyButNotLoaded) {
                List<String> list = Collections.EMPTY_LIST;
                this.mPrivilegedPackageInfoLock.readLock().unlock();
                return list;
            }
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            ArrayList arrayList = new ArrayList();
            int i = 0;
            arrayList.addAll(this.mPackageManager.queryBroadcastReceiversAsUser(intent, 0, ActivityManager.getCurrentUser()));
            arrayList.addAll(this.mPackageManager.queryIntentActivitiesAsUser(intent, 0, ActivityManager.getCurrentUser()));
            arrayList.addAll(this.mPackageManager.queryIntentServicesAsUser(intent, 0, ActivityManager.getCurrentUser()));
            arrayList.addAll(this.mPackageManager.queryIntentContentProvidersAsUser(intent, 0, ActivityManager.getCurrentUser()));
            this.mPrivilegedPackageInfoLock.readLock().lock();
            try {
                if (!this.mSimIsReadyButNotLoaded) {
                    ArraySet arraySet = new ArraySet();
                    int size = arrayList.size();
                    while (i < size) {
                        Object obj = arrayList.get(i);
                        i++;
                        String packageName = getPackageName((ResolveInfo) obj);
                        if (packageName != null && 1 == getCarrierPrivilegeStatusForPackage(packageName)) {
                            arraySet.add(packageName);
                        }
                    }
                    ArrayList arrayList2 = new ArrayList(arraySet);
                    this.mPrivilegedPackageInfoLock.readLock().unlock();
                    return arrayList2;
                }
                List<String> list2 = Collections.EMPTY_LIST;
                this.mPrivilegedPackageInfoLock.readLock().unlock();
                return list2;
            } catch (Throwable th) {
                this.mPrivilegedPackageInfoLock.readLock().unlock();
                throw th;
            }
        } catch (Throwable th2) {
            this.mPrivilegedPackageInfoLock.readLock().unlock();
            throw th2;
        }
    }

    private static String getPackageName(ResolveInfo resolveInfo) {
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        if (activityInfo != null) {
            return activityInfo.packageName;
        }
        ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        if (serviceInfo != null) {
            return serviceInfo.packageName;
        }
        ProviderInfo providerInfo = resolveInfo.providerInfo;
        if (providerInfo != null) {
            return providerInfo.packageName;
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0043  */
    /* JADX WARN: Code duplicated, block: B:20:0x004e  */
    private Pair getCarrierService(Set set) {
        String packageName;
        Iterator it = this.mPackageManager.queryIntentServicesAsUser(new Intent("android.service.carrier.CarrierService"), 0, ActivityManager.getCurrentUser()).iterator();
        while (it.hasNext()) {
            packageName = getPackageName((ResolveInfo) it.next());
            String str = this.mTestOverrideCarrierServicePackage;
            if (str == null || str.equals(packageName)) {
                if (!TelephonyBaseUtilsStub.isPlatformCarrierConfigPackage(packageName) && set.contains(packageName)) {
                    if (packageName == null) {
                        return new Pair(null, -1);
                    }
                    return new Pair(packageName, Integer.valueOf(getPackageUid(packageName)));
                }
            }
        }
        packageName = null;
        if (packageName == null) {
            return new Pair(null, -1);
        }
        return new Pair(packageName, Integer.valueOf(getPackageUid(packageName)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getApplicationEnabledSetting(String str) {
        return this.mContext.createContextAsUser(UserHandle.of(ActivityManager.getCurrentUser()), 0).getPackageManager().getApplicationEnabledSetting(str);
    }
}
