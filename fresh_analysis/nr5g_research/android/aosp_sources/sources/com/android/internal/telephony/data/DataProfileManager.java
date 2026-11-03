package com.android.internal.telephony.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Telephony;
import android.telephony.AnomalyReporter;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataProfile;
import android.telephony.data.TrafficDescriptor;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.LruCache;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperDataManager;
import com.android.internal.telephony.IHyperFrameworkTelephonyManager;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class DataProfileManager extends Handler {
    protected static final int EVENT_SIM_REFRESH = 3;
    protected volatile List<DataProfile> mAllDataProfiles;
    protected final DataConfigManager mDataConfigManager;
    protected final DataNetworkController mDataNetworkController;
    protected final Set<DataProfileManagerCallback> mDataProfileManagerCallbacks;
    private final FeatureFlags mFeatureFlags;
    IHyperDataManager mIHyperDataManager;
    private boolean mInSimRefresh;
    protected DataProfile mInitialAttachDataProfile;
    private final LruCache mLastInternetDataProfiles;
    private final LocalLog mLocalLog;
    protected final String mLogTag;
    protected final Phone mPhone;
    protected DataProfile mPreferredDataProfile;
    protected int mPreferredDataProfileSetId;
    private boolean mUpdateDataProfilesLoaded;
    private final DataServiceManager mWwanDataServiceManager;

    protected void appendExtraDataProfiles(List<DataProfile> list) {
    }

    protected void filterDataProfilesWithRadioCapability(List<DataProfile> list) {
    }

    protected void finalizeDataProfiles() {
    }

    protected boolean isUseDataService() {
        return true;
    }

    protected void mtkOnDataServiceBound(int i) {
    }

    public static abstract class DataProfileManagerCallback extends DataCallback {
        public abstract void onDataProfilesChanged();

        public DataProfileManagerCallback(Executor executor) {
            super(executor);
        }
    }

    public DataProfileManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, Looper looper, FeatureFlags featureFlags, DataProfileManagerCallback dataProfileManagerCallback) {
        super(looper);
        this.mLocalLog = new LocalLog(128);
        this.mUpdateDataProfilesLoaded = false;
        this.mAllDataProfiles = Collections.EMPTY_LIST;
        this.mInitialAttachDataProfile = null;
        this.mPreferredDataProfile = null;
        this.mLastInternetDataProfiles = new LruCache(CallFailCause.RADIO_UPLINK_FAILURE);
        this.mPreferredDataProfileSetId = 0;
        ArraySet arraySet = new ArraySet();
        this.mDataProfileManagerCallbacks = arraySet;
        this.mInSimRefresh = false;
        this.mPhone = phone;
        this.mLogTag = "DPM-" + phone.getPhoneId();
        this.mFeatureFlags = featureFlags;
        this.mDataNetworkController = dataNetworkController;
        this.mWwanDataServiceManager = dataServiceManager;
        this.mDataConfigManager = dataNetworkController.getDataConfigManager();
        arraySet.add(dataProfileManagerCallback);
        registerAllEvents();
        this.mIHyperDataManager = (IHyperDataManager) HyperTelephonyFactory.getModuleFromCache(phone.getPhoneId(), IHyperDataManager.DEFAULT);
    }

    protected void registerAllEvents() {
        this.mDataNetworkController.registerDataNetworkControllerCallback(new DataNetworkController.DataNetworkControllerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda1
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.DataProfileManager.1
            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onConnectedInternetDataNetworksChanged(Set<DataNetwork> set) {
                if (set.isEmpty()) {
                    return;
                }
                DataProfileManager.this.onInternetDataNetworkConnected(set);
            }

            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onDataServiceBound(int i) {
                DataProfileManager.this.mtkOnDataServiceBound(i);
            }
        });
        this.mDataConfigManager.registerCallback(new DataConfigManager.DataConfigManagerCallback(new Executor() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda1
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.DataProfileManager.2
            @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
            public void onCarrierConfigChanged() {
                DataProfileManager.this.onCarrierConfigUpdated();
            }
        });
        this.mPhone.getContext().getContentResolver().registerContentObserver(Telephony.Carriers.CONTENT_URI, true, new ContentObserver(this) { // from class: com.android.internal.telephony.data.DataProfileManager.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                DataProfileManager.this.sendEmptyMessage(2);
            }
        });
        this.mPhone.mCi.registerForIccRefresh(this, 3, null);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 2) {
            log("Update data profiles due to APN db updated.");
            this.mInSimRefresh = false;
            updateDataProfiles(false);
            return;
        }
        if (i == 3) {
            log("Update data profiles due to SIM refresh.");
            this.mInSimRefresh = true;
            updateDataProfiles(true ^ this.mDataConfigManager.allowClearInitialAttachDataProfile());
        } else {
            if (i == 4) {
                int i2 = message.arg1;
                log("Clearing last internet data profiles cache for subId " + i2 + ".");
                this.mLastInternetDataProfiles.remove(Integer.valueOf(i2));
                return;
            }
            loge("Unexpected event " + message);
        }
    }

    public void clearLastInternetDataProfiles(int i) {
        sendMessage(obtainMessage(4, i, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigUpdated() {
        log("Update data profiles due to carrier config updated.");
        this.mInSimRefresh = false;
        updateDataProfiles(!this.mDataConfigManager.allowClearInitialAttachDataProfile());
    }

    protected DataProfile getEnterpriseDataProfile() {
        Cursor cursorQuery = this.mPhone.getContext().getContentResolver().query(Telephony.Carriers.DPC_URI, null, null, null, null);
        if (cursorQuery == null) {
            loge("Cannot access APN database through telephony provider.");
            return null;
        }
        DataProfile dataProfileBuild = null;
        while (cursorQuery.moveToNext()) {
            ApnSetting apnSettingMakeApnSetting = ApnSetting.makeApnSetting(cursorQuery);
            if (apnSettingMakeApnSetting != null) {
                dataProfileBuild = new DataProfile.Builder().setApnSetting(apnSettingMakeApnSetting).setTrafficDescriptor(new TrafficDescriptor(apnSettingMakeApnSetting.getApnName(), null)).setPreferred(false).build();
                if (dataProfileBuild.canSatisfy(29)) {
                    break;
                }
            }
        }
        cursorQuery.close();
        return dataProfileBuild;
    }

    protected void updateDataProfiles(boolean z) {
        DataProfile enterpriseDataProfile;
        ArrayList arrayList = new ArrayList();
        boolean z2 = false;
        if (isApnQueryable()) {
            Cursor apnListCursor = getApnListCursor();
            if (apnListCursor == null) {
                loge("Cannot access APN database through telephony provider.");
                return;
            }
            boolean zCanHandleType = false;
            while (apnListCursor.moveToNext()) {
                ApnSetting apnSettingMakeApnSetting = ApnSetting.makeApnSetting(apnListCursor);
                if (apnSettingMakeApnSetting != null) {
                    DataProfile dataProfileBuild = new DataProfile.Builder().setApnSetting(apnSettingMakeApnSetting).setTrafficDescriptor(new TrafficDescriptor(apnSettingMakeApnSetting.getApnName(), null)).setPreferred(false).build();
                    arrayList.add(dataProfileBuild);
                    log("Added " + dataProfileBuild);
                    zCanHandleType |= apnSettingMakeApnSetting.canHandleType(17);
                    if (this.mDataConfigManager.isApnConfigAnomalyReportEnabled() && apnSettingMakeApnSetting.getEditedStatus() == 0) {
                        checkApnSetting(apnSettingMakeApnSetting);
                    }
                }
            }
            apnListCursor.close();
            if (!zCanHandleType && !arrayList.isEmpty() && this.mDataConfigManager.isApnConfigAnomalyReportEnabled()) {
                reportAnomaly("Carrier doesn't support internet.", "9af73e18-b523-4dc5-adab-363eb6613305");
            }
        }
        appendExtraDataProfiles(arrayList);
        if (IccCardConstants.State.LOADED.equals(this.mPhone.getIccCard().getState()) && arrayList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataProfile) obj).canSatisfy(4);
            }
        }).findFirst().orElse(null) == null && this.mDataConfigManager.isDefaultImsApnEnabled()) {
            if (isMtkImsDataSupport()) {
                arrayList.add(new DataProfile.Builder().setApnSetting(buildMTKDefaultApnSetting("DEFAULT IMS", "ims", 64)).setTrafficDescriptor(new TrafficDescriptor("ims", null)).build());
                log("Added default MTK IMS data profile.");
            } else {
                arrayList.add(new DataProfile.Builder().setApnSetting(buildDefaultApnSetting("DEFAULT IMS", "ims", 64)).setTrafficDescriptor(new TrafficDescriptor("ims", null)).build());
                log("Added default IMS data profile.");
            }
        }
        if (arrayList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataProfile) obj).canSatisfy(29);
            }
        }).findFirst().orElse(null) == null && (enterpriseDataProfile = getEnterpriseDataProfile()) != null) {
            arrayList.add(enterpriseDataProfile);
            log("Added enterprise profile " + enterpriseDataProfile);
        }
        if (arrayList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataProfile) obj).canSatisfy(10);
            }
        }).findFirst().orElse(null) == null) {
            if (isMtkImsDataSupport()) {
                arrayList.add(new DataProfile.Builder().setApnSetting(buildMTKDefaultApnSetting("DEFAULT EIMS", "", NtnRILConstants.MI_NTN_OEM_ERROR_12)).setTrafficDescriptor(new TrafficDescriptor("", null)).build());
                log("Added default MTK EIMS data profile.");
            } else {
                arrayList.add(new DataProfile.Builder().setApnSetting(buildDefaultApnSetting("DEFAULT EIMS", "sos", NtnRILConstants.MI_NTN_OEM_ERROR_12)).setTrafficDescriptor(new TrafficDescriptor("sos", null)).build());
                log("Added default EIMS data profile.");
            }
        }
        dedupeDataProfiles(arrayList);
        if (this.mDataConfigManager.isApnConfigAnomalyReportEnabled()) {
            checkDataProfiles(arrayList);
        }
        List<DataProfile> listUpdateDataProfiles = this.mIHyperDataManager.updateDataProfiles(arrayList);
        log("Found " + listUpdateDataProfiles.size() + " data profiles. profiles = " + listUpdateDataProfiles);
        boolean z3 = true;
        if (this.mAllDataProfiles.size() != listUpdateDataProfiles.size() || !this.mAllDataProfiles.containsAll(listUpdateDataProfiles)) {
            log("Data profiles changed.");
            this.mAllDataProfiles = Collections.unmodifiableList(new ArrayList(listUpdateDataProfiles));
            z2 = true;
        }
        boolean zUpdatePreferredDataProfile = updatePreferredDataProfile() | z2;
        int preferredDataProfileSetId = getPreferredDataProfileSetId();
        if (preferredDataProfileSetId != this.mPreferredDataProfileSetId) {
            logl("Changed preferred data profile set id to " + preferredDataProfileSetId);
            this.mPreferredDataProfileSetId = preferredDataProfileSetId;
        } else {
            z3 = zUpdatePreferredDataProfile;
        }
        finalizeDataProfiles();
        updateDataProfilesAtModem();
        updateInitialAttachDataProfileAtModem(z);
        if (z3) {
            this.mDataProfileManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda14
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DataProfileManager.m830$r8$lambda$4EK8xfqi3qEElC5zrMmqJT2jUo((DataProfileManager.DataProfileManagerCallback) obj);
                }
            });
        }
        if (TelephonyBaseUtilsStub.isAutoDevice()) {
            if (this.mInSimRefresh) {
                log("Skip notify during SIM refresh");
                return;
            }
            ArrayList arrayList2 = new ArrayList();
            for (DataProfile dataProfile : this.mAllDataProfiles) {
                if (dataProfile.getApnSetting() != null) {
                    arrayList2.add(dataProfile.getApnSetting());
                }
            }
            MiuiDataControllerStub.get(this.mPhone.getPhoneId()).onAllApnProfileChanged(this.mPhone, arrayList2);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$4EK-8xfqi3qEElC5zrMmqJT2jUo, reason: not valid java name */
    public static /* synthetic */ void m830$r8$lambda$4EK8xfqi3qEElC5zrMmqJT2jUo(final DataProfileManagerCallback dataProfileManagerCallback) {
        Objects.requireNonNull(dataProfileManagerCallback);
        dataProfileManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                dataProfileManagerCallback.onDataProfilesChanged();
            }
        });
    }

    protected int getPreferredDataProfileSetId() {
        Cursor cursorQuery = this.mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.PREFERRED_APN_SET_URI, String.valueOf(this.mPhone.getSubId())), new String[]{"apn_set_id"}, null, null, null);
        int i = 0;
        if (cursorQuery == null) {
            log("getPreferredDataProfileSetId: cursor is null");
            return 0;
        }
        if (cursorQuery.getCount() < 1) {
            loge("getPreferredDataProfileSetId: no APNs found");
        } else {
            cursorQuery.moveToFirst();
            i = cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("apn_set_id"));
        }
        cursorQuery.close();
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInternetDataNetworkConnected(Set set) {
        DataProfile dataProfile = (DataProfile) set.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda25
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$onInternetDataNetworkConnected$4((DataNetwork) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda26
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((DataNetwork) obj).getDataProfile();
            }
        }).min(Comparator.comparingLong(new DataProfileManager$$ExternalSyntheticLambda20())).orElse(null);
        if (dataProfile != null) {
            this.mLastInternetDataProfiles.put(Integer.valueOf(this.mPhone.getSubId()), dataProfile);
        }
        if (dataProfile == null || dataProfile.equals(this.mPreferredDataProfile)) {
            return;
        }
        logv("onInternetDataNetworkConnected: defaultProfile=" + dataProfile + " previous preferredDataProfile=" + this.mPreferredDataProfile + " internetNetworks=" + set);
        setPreferredDataProfile(dataProfile);
        updateDataProfiles(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onInternetDataNetworkConnected$4(DataNetwork dataNetwork) {
        return this.mPreferredDataProfile == null || canPreferredDataProfileSatisfy(dataNetwork.getAttachedNetworkRequestList());
    }

    private DataProfile getPreferredDataProfileFromDb() {
        Cursor cursorQuery = this.mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.PREFERRED_APN_URI, String.valueOf(this.mPhone.getSubId())), null, null, null, "name ASC");
        DataProfile dataProfileOrElse = null;
        if (cursorQuery != null) {
            if (cursorQuery.getCount() > 0) {
                cursorQuery.moveToFirst();
                final int i = cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("_id"));
                dataProfileOrElse = this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda27
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return DataProfileManager.m836$r8$lambda$cdQftTWGx7Je4AOwxnpskIzauM(i, (DataProfile) obj);
                    }
                }).findFirst().orElse(null);
            }
            cursorQuery.close();
        }
        log("getPreferredDataProfileFromDb: " + dataProfileOrElse);
        return dataProfileOrElse;
    }

    /* JADX INFO: renamed from: $r8$lambda$cdQft-TWGx7Je4AOwxnpskIzauM, reason: not valid java name */
    public static /* synthetic */ boolean m836$r8$lambda$cdQftTWGx7Je4AOwxnpskIzauM(int i, DataProfile dataProfile) {
        return dataProfile.getApnSetting() != null && dataProfile.getApnSetting().getId() == i;
    }

    private DataProfile getPreferredDataProfileFromConfig() {
        final String defaultPreferredApn = this.mDataConfigManager.getDefaultPreferredApn();
        if (TextUtils.isEmpty(defaultPreferredApn)) {
            return null;
        }
        return this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda24
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataProfileManager.$r8$lambda$up7IlUW73wh1c6TCPADyE6AWYeY(defaultPreferredApn, (DataProfile) obj);
            }
        }).findFirst().orElse(null);
    }

    public static /* synthetic */ boolean $r8$lambda$up7IlUW73wh1c6TCPADyE6AWYeY(String str, DataProfile dataProfile) {
        return dataProfile.getApnSetting() != null && str.equals(dataProfile.getApnSetting().getApnName());
    }

    private void setPreferredDataProfile(DataProfile dataProfile) {
        logl("setPreferredDataProfile: " + dataProfile);
        Uri uriWithAppendedPath = Uri.withAppendedPath(Telephony.Carriers.PREFERRED_APN_URI, Long.toString((long) this.mPhone.getSubId()));
        ContentResolver contentResolver = this.mPhone.getContext().getContentResolver();
        contentResolver.delete(uriWithAppendedPath, null, null);
        if (dataProfile == null || dataProfile.getApnSetting() == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("apn_id", Integer.valueOf(dataProfile.getApnSetting().getId()));
        contentResolver.insert(uriWithAppendedPath, contentValues);
    }

    public void setDataProfileUsedTime(DataProfile dataProfile, long j) {
        for (DataProfile dataProfile2 : this.mAllDataProfiles) {
            if (dataProfile2.getApnSetting() != null && dataProfile2.getApnSetting().equals(dataProfile.getApnSetting())) {
                dataProfile2.setLastSetupTimestamp(j);
            }
        }
    }

    protected boolean updatePreferredDataProfile() {
        final int subId = this.mPhone.getSubId();
        DataProfile dataProfileOrElse = null;
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            DataProfile preferredDataProfileFromDb = getPreferredDataProfileFromDb();
            if (preferredDataProfileFromDb != null) {
                dataProfileOrElse = preferredDataProfileFromDb;
            } else {
                preferredDataProfileFromDb = getPreferredDataProfileFromConfig();
                if (preferredDataProfileFromDb != null) {
                    setPreferredDataProfile(preferredDataProfileFromDb);
                    dataProfileOrElse = preferredDataProfileFromDb;
                } else {
                    dataProfileOrElse = !((IHyperFrameworkTelephonyManager) HyperTelephonyFactory.getInstance().initModule(IHyperFrameworkTelephonyManager.DEFAULT, new Object[0])).isPayStateSupported(this.mPhone.getContext(), this.mPhone.getSubId()) ? this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return this.f$0.lambda$updatePreferredDataProfile$7(subId, (DataProfile) obj);
                        }
                    }).findFirst().orElse(null) : preferredDataProfileFromDb;
                    if (dataProfileOrElse != null) {
                        log("updatePreferredDataProfile: preferredDB is empty and no carrier default configured, setting preferred to be prev internet DP.");
                        setPreferredDataProfile(dataProfileOrElse);
                    }
                }
            }
        }
        for (DataProfile dataProfile : this.mAllDataProfiles) {
            dataProfile.setPreferred(dataProfile.equals(dataProfileOrElse));
        }
        if (Objects.equals(this.mPreferredDataProfile, dataProfileOrElse)) {
            return false;
        }
        this.mPreferredDataProfile = dataProfileOrElse;
        logl("Changed preferred data profile to " + this.mPreferredDataProfile);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePreferredDataProfile$7(int i, DataProfile dataProfile) {
        return lambda$isDataProfileCompatible$17(dataProfile, (DataProfile) this.mLastInternetDataProfiles.get(Integer.valueOf(i)));
    }

    protected void updateInitialAttachDataProfileAtModem(boolean z) {
        List<DataProfile> list = this.mAllDataProfiles.stream().sorted(Comparator.comparing(new Function() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$updateInitialAttachDataProfileAtModem$8((DataProfile) obj);
            }
        })).toList();
        Iterator<Integer> it = this.mDataConfigManager.getAllowedInitialAttachApnTypes().iterator();
        DataProfile dataProfileOrElse = null;
        while (it.hasNext()) {
            final int iIntValue = it.next().intValue();
            dataProfileOrElse = list.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((DataProfile) obj).canSatisfy(DataUtils.apnTypeToNetworkCapability(iIntValue));
                }
            }).findFirst().orElse(null);
            if (dataProfileOrElse != null) {
                break;
            }
        }
        if (z || !Objects.equals(this.mInitialAttachDataProfile, dataProfileOrElse)) {
            this.mInitialAttachDataProfile = dataProfileOrElse;
            logl("Initial attach data profile updated as " + this.mInitialAttachDataProfile + " or forceUpdateIa= " + z);
            if ((this.mInitialAttachDataProfile != null || this.mDataConfigManager.allowClearInitialAttachDataProfile()) && !MiuiDataControllerStub.get(this.mPhone.getPhoneId()).setInitialAttachApn(this.mPhone, this.mWwanDataServiceManager, this.mInitialAttachDataProfile)) {
                if (isUseDataService()) {
                    this.mWwanDataServiceManager.setInitialAttachApn(this.mInitialAttachDataProfile, this.mPhone.getServiceState().getDataRoamingFromRegistration(), null);
                } else {
                    log("updateInitialAttachDataProfileAtModem: invoke RIL interface directly.");
                    this.mPhone.mCi.setInitialAttachApn(this.mInitialAttachDataProfile, null);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$updateInitialAttachDataProfileAtModem$8(DataProfile dataProfile) {
        return Boolean.valueOf(!dataProfile.equals(this.mPreferredDataProfile));
    }

    protected void updateDataProfilesAtModem() {
        log("updateDataProfilesAtModem: set " + this.mAllDataProfiles.size() + " data profiles.");
        if (isUseDataService()) {
            this.mWwanDataServiceManager.setDataProfile(this.mAllDataProfiles, this.mPhone.getServiceState().getDataRoamingFromRegistration(), null);
        } else {
            log("updateDataProfilesAtModem: invoke RIL interface directly.");
            this.mPhone.mCi.setDataProfile((DataProfile[]) this.mAllDataProfiles.toArray(new DataProfile[this.mAllDataProfiles.size()]), null);
        }
    }

    protected ApnSetting buildDefaultApnSetting(String str, String str2, int i) {
        return new ApnSetting.Builder().setEntryName(str).setProtocol(2).setRoamingProtocol(2).setApnName(str2).setApnTypeBitmask(i).setCarrierEnabled(true).setApnSetId(-1).build();
    }

    protected ApnSetting buildMTKDefaultApnSetting(String str, String str2, int i) {
        return new ApnSetting.Builder().setEntryName(str).setProtocol(2).setRoamingProtocol(2).setApnName(str2).setApnTypeBitmask(i).setCarrierEnabled(true).setApnSetId(-1).buildWithoutCheck();
    }

    public DataProfile getDataProfileForNetworkRequest(final TelephonyNetworkRequest telephonyNetworkRequest, int i, boolean z, boolean z2, boolean z3) {
        ApnSetting dataProfileForNetworkRequest;
        TrafficDescriptor.OsAppId osAppId;
        if (telephonyNetworkRequest.hasAttribute(1)) {
            int simStateForSlotIndex = TelephonyManager.getSimStateForSlotIndex(this.mPhone.getPhoneId());
            DataProfile dataProfileOrElse = this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda8
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((DataProfile) obj).canSatisfy(telephonyNetworkRequest.getCapabilities());
                }
            }).findFirst().orElse(null);
            log("simState= " + simStateForSlotIndex + ", existProfile=" + dataProfileOrElse + ", mUpdateDataProfilesLoaded =" + this.mUpdateDataProfilesLoaded);
            if (dataProfileOrElse == null && simStateForSlotIndex == 10 && !this.mUpdateDataProfilesLoaded) {
                this.mUpdateDataProfilesLoaded = true;
                updateDataProfiles(true);
            }
            dataProfileForNetworkRequest = MiuiDataControllerStub.get(this.mPhone.getPhoneId()).getDataProfileForNetworkRequest(this.mPhone, this.mAllDataProfiles, getApnSettingForNetworkRequest(telephonyNetworkRequest, i, z, z2, z3), telephonyNetworkRequest);
        } else {
            dataProfileForNetworkRequest = null;
        }
        HalVersion halVersion = this.mPhone.getHalVersion();
        if (halVersion != null && halVersion.lessOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            for (DataProfile dataProfile : this.mAllDataProfiles) {
                if (Objects.equals(dataProfileForNetworkRequest, dataProfile.getApnSetting())) {
                    return dataProfile;
                }
            }
            return null;
        }
        TrafficDescriptor.Builder builder = new TrafficDescriptor.Builder();
        if (telephonyNetworkRequest.hasAttribute(2) && dataProfileForNetworkRequest != null) {
            builder.setDataNetworkName(dataProfileForNetworkRequest.getApnName());
        }
        if (telephonyNetworkRequest.hasAttribute(4) && (osAppId = telephonyNetworkRequest.getOsAppId()) != null) {
            builder.setOsAppId(osAppId.getBytes());
        }
        if (telephonyNetworkRequest.hasAttribute(8)) {
            int iNetworkCapabilityToConnectionCapability = this.mDataConfigManager.networkCapabilityToConnectionCapability(telephonyNetworkRequest.getHighestPrioritySupportedNetworkCapability());
            if (iNetworkCapabilityToConnectionCapability != 0) {
                builder.setConnectionCapability(iNetworkCapabilityToConnectionCapability);
            }
        }
        try {
            TrafficDescriptor trafficDescriptorBuild = builder.build();
            if (this.mDataConfigManager.isApnMatchedRequired(telephonyNetworkRequest.getHighestPrioritySupportedNetworkCapability()) && trafficDescriptorBuild.getDataNetworkName() == null && trafficDescriptorBuild.getOsAppId() == null && trafficDescriptorBuild.getConnectionCapability() != 0) {
                return null;
            }
            for (DataProfile dataProfile2 : this.mAllDataProfiles) {
                if (Objects.equals(dataProfileForNetworkRequest, dataProfile2.getApnSetting())) {
                    return new DataProfile.Builder(dataProfile2).setTrafficDescriptor(trafficDescriptorBuild).build();
                }
            }
            DataProfile.Builder builder2 = new DataProfile.Builder();
            if (dataProfileForNetworkRequest != null) {
                builder2.setApnSetting(dataProfileForNetworkRequest);
            }
            builder2.setTrafficDescriptor(trafficDescriptorBuild);
            DataProfile dataProfileBuild = builder2.build();
            log("Created data profile " + dataProfileBuild + " for " + telephonyNetworkRequest);
            return dataProfileBuild;
        } catch (IllegalArgumentException unused) {
            log("Unable to find a data profile for " + telephonyNetworkRequest);
            return null;
        }
    }

    private ApnSetting getApnSettingForNetworkRequest(final TelephonyNetworkRequest telephonyNetworkRequest, final int i, final boolean z, final boolean z2, final boolean z3) {
        DataProfile dataProfile;
        if (!telephonyNetworkRequest.hasAttribute(1)) {
            loge("Network request does not have APN setting attribute.");
            return null;
        }
        if (!z2 && !MiuiDataControllerStub.get(this.mPhone.getPhoneId()).isNotNeedPreferredApn(this.mPhone, telephonyNetworkRequest) && (dataProfile = this.mPreferredDataProfile) != null && telephonyNetworkRequest.canBeSatisfiedBy(dataProfile) && this.mPreferredDataProfile.getApnSetting() != null && this.mPreferredDataProfile.getApnSetting().canSupportNetworkType(i) && ((z && this.mPreferredDataProfile.getApnSetting().isForInfrastructure(2)) || (!z && this.mPreferredDataProfile.getApnSetting().isForInfrastructure(1)))) {
            if (z3 || !this.mPreferredDataProfile.getApnSetting().getPermanentFailed()) {
                return this.mPreferredDataProfile.getApnSetting();
            }
            log("The preferred data profile is permanently failed. Only condition based retry can happen.");
            return null;
        }
        List<DataProfile> list = (List) this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return telephonyNetworkRequest.canBeSatisfiedBy((DataProfile) obj);
            }
        }).sorted(Comparator.comparing(new Function() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda19
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Long.valueOf(((DataProfile) obj).getLastSetupTimestamp());
            }
        })).collect(Collectors.toList());
        if (isMtkImsDataSupport() && list.size() == 0 && telephonyNetworkRequest.hasCapability(10)) {
            list.add(new DataProfile.Builder().setApnSetting(buildMTKDefaultApnSetting("DEFAULT EIMS", "", NtnRILConstants.MI_NTN_OEM_ERROR_12)).setTrafficDescriptor(new TrafficDescriptor("", null)).build());
            this.mAllDataProfiles = Collections.unmodifiableList(new ArrayList(list));
            log("For ECC w/o SIM requirement,Added default EIMS data profile.");
            return this.mAllDataProfiles.get(0).getApnSetting();
        }
        if (MiuiDataControllerStub.get(this.mPhone.getPhoneId()).isNotNeedPreferredApn(this.mPhone, telephonyNetworkRequest)) {
            list = (List) list.stream().sorted(Comparator.comparingLong(new DataProfileManager$$ExternalSyntheticLambda20())).collect(Collectors.toList());
        }
        for (DataProfile dataProfile2 : list) {
            logv("Satisfied profile: " + dataProfile2 + ", last setup=" + DataUtils.elapsedTimeToString(dataProfile2.getLastSetupTimestamp()));
        }
        if (list.isEmpty()) {
            log("Can't find any data profile that can satisfy " + telephonyNetworkRequest);
            return null;
        }
        MiuiDataControllerStub.get(this.mPhone.getPhoneId()).filterSpecialDunApn(this.mPhone, list, this.mPreferredDataProfile, telephonyNetworkRequest);
        List list2 = (List) list.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda21
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getApnSettingForNetworkRequest$11(i, telephonyNetworkRequest, z2, z, (DataProfile) obj);
            }
        }).collect(Collectors.toList());
        if (list2.isEmpty()) {
            log("Can't find any data profile for network type " + TelephonyManager.getNetworkTypeName(i) + " and infrastructure for " + NetworkRegistrationInfo.isNonTerrestrialNetworkToString(z));
            return null;
        }
        List list3 = (List) list2.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda22
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getApnSettingForNetworkRequest$12((DataProfile) obj);
            }
        }).collect(Collectors.toList());
        if (list3.isEmpty()) {
            log("Can't find any data profile has APN set id matched. mPreferredDataProfileSetId=" + this.mPreferredDataProfileSetId);
            return null;
        }
        List list4 = (List) list3.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataProfileManager.$r8$lambda$G8mXlnoF77tT8dTVCGnAJV4Siu8(z3, (DataProfile) obj);
            }
        }).collect(Collectors.toList());
        if (list4.isEmpty()) {
            log("The suitable data profiles are all in permanent failed state.");
            return null;
        }
        if (z2) {
            log("Found esim bootstrap provisioning data profile for network request: " + ((DataProfile) list4.get(0)).getApnSetting());
        }
        return ((DataProfile) list4.get(0)).getApnSetting();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getApnSettingForNetworkRequest$11(int i, TelephonyNetworkRequest telephonyNetworkRequest, boolean z, boolean z2, DataProfile dataProfile) {
        if (dataProfile.getApnSetting() == null) {
            return false;
        }
        if (!dataProfile.getApnSetting().canSupportNetworkType(i)) {
            if (isMtkImsDataSupport()) {
                int preferredTransportByNetworkCapability = this.mPhone.getAccessNetworksManager().getPreferredTransportByNetworkCapability(telephonyNetworkRequest.getHighestPriorityApnTypeNetworkCapability());
                if (preferredTransportByNetworkCapability == 1 && dataProfile.canSatisfy(10) && i == 0 && dataProfile.getApnSetting().canSupportNetworkType(13)) {
                    log("WWAN: pass the eims request of unknown rat");
                } else if (preferredTransportByNetworkCapability == 2 && ((dataProfile.canSatisfy(10) || dataProfile.canSatisfy(0)) && i == 0 && dataProfile.getApnSetting().canSupportNetworkType(18))) {
                    log("WLAN: pass the eims and mms request of unknown rat");
                }
            }
            return false;
        }
        if (z != dataProfile.getApnSetting().isEsimBootstrapProvisioning()) {
            return false;
        }
        if (!z2 || dataProfile.getApnSetting().isForInfrastructure(2)) {
            return z2 || dataProfile.getApnSetting().isForInfrastructure(1);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getApnSettingForNetworkRequest$12(DataProfile dataProfile) {
        if (dataProfile.getApnSetting() != null) {
            return dataProfile.getApnSetting().getApnSetId() == -1 || dataProfile.getApnSetting().getApnSetId() == this.mPreferredDataProfileSetId;
        }
        return false;
    }

    public static /* synthetic */ boolean $r8$lambda$G8mXlnoF77tT8dTVCGnAJV4Siu8(boolean z, DataProfile dataProfile) {
        if (z) {
            return true;
        }
        return (dataProfile.getApnSetting() == null || dataProfile.getApnSetting().getPermanentFailed()) ? false : true;
    }

    public boolean isDataProfilePreferred(DataProfile dataProfile) {
        return lambda$isDataProfileCompatible$17(dataProfile, this.mPreferredDataProfile);
    }

    public boolean canPreferredDataProfileSatisfy(DataNetworkController.NetworkRequestList networkRequestList) {
        return this.mPreferredDataProfile != null && networkRequestList.stream().allMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$canPreferredDataProfileSatisfy$14((TelephonyNetworkRequest) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$canPreferredDataProfileSatisfy$14(TelephonyNetworkRequest telephonyNetworkRequest) {
        return telephonyNetworkRequest.canBeSatisfiedBy(this.mPreferredDataProfile);
    }

    public boolean isTetheringDataProfileExisting(int i) {
        return ((this.mDataConfigManager.isTetheringProfileDisabledForRoaming() && this.mPhone.getServiceState().getDataRoaming()) || MiuiDataControllerStub.get(this.mPhone.getPhoneId()).ignoreTetheringDataProfile(this.mPhone) || getDataProfileForNetworkRequest(new TelephonyNetworkRequest(new NetworkRequest.Builder().addCapability(2).build(), this.mPhone, this.mFeatureFlags), i, this.mPhone.getServiceState().isUsingNonTerrestrialNetwork(), this.mDataNetworkController.isEsimBootStrapProvisioningActivated(), true) == null) ? false : true;
    }

    protected void dedupeDataProfiles(List<DataProfile> list) {
        int i = 0;
        while (i < list.size() - 1) {
            DataProfile dataProfile = list.get(i);
            int i2 = i + 1;
            int i3 = i2;
            while (i3 < list.size()) {
                DataProfile dataProfile2 = list.get(i3);
                DataProfile dataProfileMergeDataProfiles = mergeDataProfiles(dataProfile, dataProfile2);
                if (dataProfileMergeDataProfiles != null) {
                    log("Created a merged profile " + dataProfileMergeDataProfiles + " from " + dataProfile + " and " + dataProfile2);
                    StringBuilder sb = new StringBuilder("Merging data profiles will not be supported anymore. Please directly configure the merged profile ");
                    sb.append(dataProfileMergeDataProfiles);
                    sb.append(" in the APN config.");
                    loge(sb.toString());
                    list.set(i, dataProfileMergeDataProfiles);
                    list.remove(i3);
                    dataProfile = dataProfileMergeDataProfiles;
                } else {
                    i3++;
                }
            }
            i = i2;
        }
    }

    private void checkApnSetting(ApnSetting apnSetting) {
        if (apnSetting.canHandleType(2) && apnSetting.getEditedStatus() == 0) {
            if (apnSetting.getMmsc() == null) {
                reportAnomaly("MMS is supported but no MMSC configured " + apnSetting, "9af73e18-b523-4dc5-adab-19d86c6a3685");
            } else if (!apnSetting.getMmsc().toString().matches("^https?:\\/\\/.+")) {
                reportAnomaly("Apn config mmsc should start with http but is " + apnSetting.getMmsc(), "9af73e18-b523-4dc5-adab-ec754d959d4d");
            }
            if (TextUtils.isEmpty(apnSetting.getMmsProxyAddressAsString()) || !apnSetting.getMmsProxyAddressAsString().matches("^https?:\\/\\/.+")) {
                return;
            }
            reportAnomaly("Apn config mmsc_proxy should NOT start with http but is " + apnSetting.getMmsc(), "9af73e18-b523-4dc5-adab-ec754d959d4d");
        }
    }

    protected void checkDataProfiles(List<DataProfile> list) {
        for (int i = 0; i < list.size(); i++) {
            ApnSetting apnSetting = list.get(i).getApnSetting();
            if (apnSetting != null && apnSetting.getEditedStatus() == 0 && 0 != apnSetting.getLingeringNetworkTypeBitmask() && (((long) apnSetting.getNetworkTypeBitmask()) | apnSetting.getLingeringNetworkTypeBitmask()) != apnSetting.getLingeringNetworkTypeBitmask()) {
                reportAnomaly("Apn[" + apnSetting.getApnName() + "] network " + TelephonyManager.convertNetworkTypeBitmaskToString(apnSetting.getNetworkTypeBitmask()) + " should be a subset of the lingering network " + TelephonyManager.convertNetworkTypeBitmaskToString(apnSetting.getLingeringNetworkTypeBitmask()), "9af73e18-b523-4dc5-adab-4bb24355d838");
            }
        }
    }

    private static DataProfile mergeDataProfiles(DataProfile dataProfile, DataProfile dataProfile2) {
        Objects.requireNonNull(dataProfile);
        Objects.requireNonNull(dataProfile2);
        if (!Objects.equals(dataProfile.getTrafficDescriptor(), dataProfile2.getTrafficDescriptor()) || dataProfile.getApnSetting() == null || dataProfile2.getApnSetting() == null || !dataProfile.getApnSetting().similar(dataProfile2.getApnSetting())) {
            return null;
        }
        ApnSetting apnSetting = dataProfile.getApnSetting();
        ApnSetting apnSetting2 = dataProfile2.getApnSetting();
        ApnSetting.Builder builder = new ApnSetting.Builder();
        builder.setId(apnSetting.getId());
        builder.setEntryName(apnSetting.getEntryName());
        if (apnSetting2.canHandleType(17) && !apnSetting.canHandleType(17)) {
            builder.setId(apnSetting2.getId());
            builder.setEntryName(apnSetting2.getEntryName());
        }
        builder.setProxyAddress(TextUtils.isEmpty(apnSetting2.getProxyAddressAsString()) ? apnSetting.getProxyAddressAsString() : apnSetting2.getProxyAddressAsString());
        builder.setProxyPort(apnSetting2.getProxyPort() == -1 ? apnSetting.getProxyPort() : apnSetting2.getProxyPort());
        builder.setMmsc(apnSetting2.getMmsc() == null ? apnSetting.getMmsc() : apnSetting2.getMmsc());
        builder.setMmsProxyAddress(TextUtils.isEmpty(apnSetting2.getMmsProxyAddressAsString()) ? apnSetting.getMmsProxyAddressAsString() : apnSetting2.getMmsProxyAddressAsString());
        builder.setMmsProxyPort(apnSetting2.getMmsProxyPort() == -1 ? apnSetting.getMmsProxyPort() : apnSetting2.getMmsProxyPort());
        builder.setUser(TextUtils.isEmpty(apnSetting2.getUser()) ? apnSetting.getUser() : apnSetting2.getUser());
        builder.setPassword(TextUtils.isEmpty(apnSetting2.getPassword()) ? apnSetting.getPassword() : apnSetting2.getPassword());
        builder.setAuthType(apnSetting2.getAuthType() == -1 ? apnSetting.getAuthType() : apnSetting2.getAuthType());
        builder.setApnTypeBitmask(apnSetting.getApnTypeBitmask() | apnSetting2.getApnTypeBitmask());
        builder.setMtuV4(apnSetting2.getMtuV4() <= 0 ? apnSetting.getMtuV4() : apnSetting2.getMtuV4());
        builder.setMtuV6(apnSetting2.getMtuV6() <= 0 ? apnSetting.getMtuV6() : apnSetting2.getMtuV6());
        builder.setMvnoType(apnSetting.getMvnoType());
        builder.setMvnoMatchData(apnSetting.getMvnoMatchData());
        builder.setApnName(apnSetting.getApnName());
        builder.setOperatorNumeric(apnSetting.getOperatorNumeric());
        builder.setProtocol(apnSetting.getProtocol());
        builder.setRoamingProtocol(apnSetting.getRoamingProtocol());
        builder.setCarrierEnabled(apnSetting.isEnabled());
        builder.setNetworkTypeBitmask(apnSetting.getNetworkTypeBitmask());
        builder.setLingeringNetworkTypeBitmask(apnSetting.getLingeringNetworkTypeBitmask());
        builder.setProfileId(apnSetting.getProfileId());
        builder.setPersistent(apnSetting.isPersistent());
        builder.setMaxConns(apnSetting.getMaxConns());
        builder.setWaitTime(apnSetting.getWaitTime());
        builder.setMaxConnsTime(apnSetting.getMaxConnsTime());
        builder.setApnSetId(apnSetting.getApnSetId());
        builder.setCarrierId(apnSetting.getCarrierId());
        builder.setSkip464Xlat(apnSetting.getSkip464Xlat());
        builder.setAlwaysOn(apnSetting.isAlwaysOn());
        builder.setInfrastructureBitmask(apnSetting.getInfrastructureBitmask());
        builder.setEsimBootstrapProvisioning(apnSetting.isEsimBootstrapProvisioning());
        return new DataProfile.Builder().setApnSetting(builder.build()).setTrafficDescriptor(dataProfile.getTrafficDescriptor()).build();
    }

    public void clearAllDataProfilePermanentFailures() {
        this.mAllDataProfiles.stream().map(new Function() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((DataProfile) obj).getApnSetting();
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((ApnSetting) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ApnSetting) obj).setPermanentFailed(false);
            }
        });
    }

    public boolean isDataProfileCompatible(final DataProfile dataProfile) {
        if (dataProfile.getApnSetting() != null || dataProfile.getTrafficDescriptor() == null) {
            return this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$isDataProfileCompatible$16((DataProfile) obj);
                }
            }).anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda10
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return this.f$0.lambda$isDataProfileCompatible$17(dataProfile, (DataProfile) obj);
                }
            });
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isDataProfileCompatible$16(DataProfile dataProfile) {
        if (dataProfile.getApnSetting() != null) {
            return dataProfile.getApnSetting().getApnSetId() == -1 || dataProfile.getApnSetting().getApnSetId() == this.mPreferredDataProfileSetId;
        }
        return false;
    }

    /* JADX INFO: renamed from: areDataProfilesSharingApn, reason: merged with bridge method [inline-methods] */
    public boolean lambda$isDataProfileCompatible$17(DataProfile dataProfile, DataProfile dataProfile2) {
        return (dataProfile == null || dataProfile2 == null || dataProfile.getApnSetting() == null || !dataProfile.getApnSetting().equals(dataProfile2.getApnSetting(), this.mPhone.getServiceState().getDataRoamingFromRegistration())) ? false : true;
    }

    public void registerCallback(DataProfileManagerCallback dataProfileManagerCallback) {
        this.mDataProfileManagerCallbacks.add(dataProfileManagerCallback);
    }

    public void unregisterCallback(DataProfileManagerCallback dataProfileManagerCallback) {
        this.mDataProfileManagerCallbacks.remove(dataProfileManagerCallback);
    }

    protected void reportAnomaly(String str, String str2) {
        logl(str);
        AnomalyReporter.reportAnomaly(UUID.fromString(str2), str, this.mPhone.getCarrierId());
    }

    private boolean isMtkImsDataSupport() {
        return "0".equals(SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0")) || "1".equals(SystemProperties.get("ro.mtk.ims.data.feature_support"));
    }

    protected boolean isApnQueryable() {
        return this.mDataConfigManager.isConfigCarrierSpecific();
    }

    protected Cursor getApnListCursor() {
        return this.mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.SIM_APN_URI, "filtered/subId/" + this.mPhone.getSubId()), null, null, null, "_id");
    }

    protected void log(String str) {
        Rlog.d(this.mLogTag, str);
    }

    protected void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    private void logv(String str) {
        Rlog.v(this.mLogTag, str);
    }

    protected void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        final AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(DataProfileManager.class.getSimpleName() + "-" + this.mPhone.getPhoneId() + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("Data profiles for the current carrier:");
        androidUtilIndentingPrintWriter.increaseIndent();
        for (DataProfile dataProfile : this.mAllDataProfiles) {
            androidUtilIndentingPrintWriter.print(dataProfile);
            androidUtilIndentingPrintWriter.println(", last setup time: " + DataUtils.elapsedTimeToString(dataProfile.getLastSetupTimestamp()));
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Preferred data profile=" + this.mPreferredDataProfile);
        androidUtilIndentingPrintWriter.println("Preferred data profile from db=" + getPreferredDataProfileFromDb());
        androidUtilIndentingPrintWriter.println("Preferred data profile from config=" + getPreferredDataProfileFromConfig());
        androidUtilIndentingPrintWriter.println("Preferred data profile set id=" + this.mPreferredDataProfileSetId);
        androidUtilIndentingPrintWriter.println("Last internet data profile for=");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLastInternetDataProfiles.snapshot().forEach(new BiConsumer() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                androidUtilIndentingPrintWriter.println(((Integer) obj) + ":" + ((DataProfile) obj2));
            }
        });
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Initial attach data profile=" + this.mInitialAttachDataProfile);
        androidUtilIndentingPrintWriter.println("isTetheringDataProfileExisting=" + isTetheringDataProfileExisting(13));
        androidUtilIndentingPrintWriter.println("Permanent failed profiles=");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataProfileManager.m834$r8$lambda$XxcVfmNXThT5FVVw4ZH2Gvbn9Q((DataProfile) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.internal.telephony.data.DataProfileManager$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                androidUtilIndentingPrintWriter.println((DataProfile) obj);
            }
        });
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("Local logs:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: renamed from: $r8$lambda$XxcVfmNXThT5FVVw4Z-H2Gvbn9Q, reason: not valid java name */
    public static /* synthetic */ boolean m834$r8$lambda$XxcVfmNXThT5FVVw4ZH2Gvbn9Q(DataProfile dataProfile) {
        return dataProfile.getApnSetting() != null && dataProfile.getApnSetting().getPermanentFailed();
    }
}
