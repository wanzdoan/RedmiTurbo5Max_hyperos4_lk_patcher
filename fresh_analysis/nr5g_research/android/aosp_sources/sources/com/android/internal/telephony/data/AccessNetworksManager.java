package com.android.internal.telephony.data;

import android.R;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.telephony.AccessNetworkConstants;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataServiceCallback;
import android.telephony.data.IQualifiedNetworksService;
import android.telephony.data.IQualifiedNetworksServiceCallback;
import android.telephony.data.ThrottleStatus;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda1;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class AccessNetworksManager extends Handler {
    public static final String IWLAN_OPERATION_MODE_AP_ASSISTED = "AP-assisted";
    public static final String IWLAN_OPERATION_MODE_DEFAULT = "default";
    public static final String IWLAN_OPERATION_MODE_LEGACY = "legacy";
    public static final int[] SUPPORTED_APN_TYPES = {17, 2, 32, 64, 128, 4, NtnRILConstants.MI_NTN_OEM_ERROR_12, 2048, 8};
    public static final String SYSTEM_PROPERTIES_IWLAN_OPERATION_MODE = "ro.telephony.iwlan_operation_mode";
    private final Set mAccessNetworksManagerCallbacks;
    private final UUID mAnomalyUUID;
    private final SparseArray mApnTypeToQnsChangeNetworkCounter;
    private final SparseArray mAvailableNetworks;
    private final int[] mAvailableTransports;
    private final CarrierConfigManager mCarrierConfigManager;
    private DataConfigManager mDataConfigManager;
    private AccessNetworksManagerDeathRecipient mDeathRecipient;
    private final FeatureFlags mFeatureFlags;
    private IQualifiedNetworksService mIQualifiedNetworksService;
    private String mLastBoundPackageName;
    private final LocalLog mLocalLog;
    private final String mLogTag;
    private final Phone mPhone;
    private final Map mPreferredTransports;
    private final RegistrantList mQualifiedNetworksChangedRegistrants;
    private QualifiedNetworksServiceConnection mServiceConnection;
    private String mTargetBindingPackageName;

    private static int getTransportFromAccessNetwork(int i) {
        return i == 5 ? 2 : 1;
    }

    public boolean isInLegacyMode() {
        return false;
    }

    public static class QualifiedNetworks {
        public final int apnType;
        public final int[] qualifiedNetworks;

        public QualifiedNetworks(int i, int[] iArr) {
            this.apnType = i;
            this.qualifiedNetworks = Arrays.stream(iArr).boxed().filter(new Predicate() { // from class: com.android.internal.telephony.data.AccessNetworksManager$QualifiedNetworks$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DataUtils.isValidAccessNetwork(((Integer) obj).intValue());
                }
            }).mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
        }

        public String toString() {
            return "[QualifiedNetworks: apnType=" + ApnSetting.getApnTypeString(this.apnType) + ", networks=" + ((String) Arrays.stream(this.qualifiedNetworks).mapToObj(new AccessNetworksManager$QualifiedNetworks$$ExternalSyntheticLambda0()).collect(Collectors.joining(","))) + "]";
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            onEmergencyDataNetworkPreferredTransportChanged(((Integer) ((AsyncResult) message.obj).result).intValue());
        } else {
            if (i == 2) {
                unbindService();
                return;
            }
            loge("Unexpected event " + message.what);
        }
    }

    private synchronized void unbindService() {
        IQualifiedNetworksService iQualifiedNetworksService = this.mIQualifiedNetworksService;
        if (iQualifiedNetworksService != null && iQualifiedNetworksService.asBinder().isBinderAlive() && !SubscriptionManager.isValidPhoneId(this.mPhone.getPhoneId())) {
            log("unbind the service as invalid phone or phoneId. " + this.mPhone.getPhoneId());
            try {
                this.mIQualifiedNetworksService.removeNetworkAvailabilityProvider(this.mPhone.getPhoneId());
            } catch (RemoteException e) {
                loge("Cannot remove network availability updater. " + e);
            }
            if (this.mServiceConnection != null) {
                this.mPhone.getContext().unbindService(this.mServiceConnection);
                this.mServiceConnection = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDisconnectOrDied() {
        IQualifiedNetworksService iQualifiedNetworksService = this.mIQualifiedNetworksService;
        if (iQualifiedNetworksService == null || this.mDeathRecipient == null) {
            return;
        }
        try {
            iQualifiedNetworksService.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
        } catch (Exception e) {
            loge("Exception. " + e);
        }
        this.mIQualifiedNetworksService = null;
        this.mDeathRecipient = null;
    }

    private class AccessNetworksManagerDeathRecipient implements IBinder.DeathRecipient {
        private AccessNetworksManagerDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = "Qualified network service " + AccessNetworksManager.this.mLastBoundPackageName + " died.";
            AccessNetworksManager.this.mApnTypeToQnsChangeNetworkCounter.clear();
            AccessNetworksManager.this.loge(str);
            AnomalyReporter.reportAnomaly(AccessNetworksManager.this.mAnomalyUUID, str, AccessNetworksManager.this.mPhone.getCarrierId());
            AccessNetworksManager.this.handleDisconnectOrDied();
        }
    }

    private final class QualifiedNetworksServiceConnection implements ServiceConnection {
        private QualifiedNetworksServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AccessNetworksManager.this.mIQualifiedNetworksService = IQualifiedNetworksService.Stub.asInterface(iBinder);
            AccessNetworksManager accessNetworksManager = AccessNetworksManager.this;
            accessNetworksManager.mDeathRecipient = new AccessNetworksManagerDeathRecipient();
            AccessNetworksManager accessNetworksManager2 = AccessNetworksManager.this;
            accessNetworksManager2.mLastBoundPackageName = accessNetworksManager2.getQualifiedNetworksServicePackageName();
            try {
                iBinder.linkToDeath(AccessNetworksManager.this.mDeathRecipient, 0);
                AccessNetworksManager.this.mIQualifiedNetworksService.createNetworkAvailabilityProvider(AccessNetworksManager.this.mPhone.getPhoneId(), new QualifiedNetworksServiceCallback());
            } catch (RemoteException e) {
                AccessNetworksManager.this.loge("Remote exception. " + e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            AccessNetworksManager.this.mTargetBindingPackageName = null;
            AccessNetworksManager.this.handleDisconnectOrDied();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class QualifiedNetworksServiceCallback extends IQualifiedNetworksServiceCallback.Stub {
        private QualifiedNetworksServiceCallback() {
        }

        public void onQualifiedNetworkTypesChanged(int i, int[] iArr) {
            AccessNetworksManager accessNetworksManager = AccessNetworksManager.this;
            if (iArr == null) {
                accessNetworksManager.loge("onQualifiedNetworkTypesChanged: Ignored null input.");
                return;
            }
            accessNetworksManager.log("onQualifiedNetworkTypesChanged: apnTypes = [" + ApnSetting.getApnTypesStringFromBitmask(i) + "], networks = [" + ((String) Arrays.stream(iArr).mapToObj(new AccessNetworksManager$QualifiedNetworks$$ExternalSyntheticLambda0()).collect(Collectors.joining(","))) + "]");
            handleQualifiedNetworksChanged(i, iArr, false);
        }

        private void handleQualifiedNetworksChanged(int i, int[] iArr, final boolean z) {
            if (Arrays.stream(iArr).anyMatch(new IntPredicate() { // from class: com.android.internal.telephony.data.AccessNetworksManager$QualifiedNetworksServiceCallback$$ExternalSyntheticLambda1
                @Override // java.util.function.IntPredicate
                public final boolean test(int i2) {
                    return AccessNetworksManager.QualifiedNetworksServiceCallback.$r8$lambda$_Nr8PqOEokXAXom7HMUsgwJZaMw(i2);
                }
            })) {
                AccessNetworksManager.this.loge("Invalid access networks " + Arrays.toString(iArr));
                if (AccessNetworksManager.this.mDataConfigManager == null || !AccessNetworksManager.this.mDataConfigManager.isInvalidQnsParamAnomalyReportEnabled()) {
                    return;
                }
                AccessNetworksManager.this.reportAnomaly("QNS requested invalid Network Type", "3e89a3df-3524-45fa-b5f2-0fb0e4c77ec4");
                return;
            }
            ArrayList arrayList = new ArrayList();
            int i2 = 0;
            for (final int i3 : AccessNetworksManager.SUPPORTED_APN_TYPES) {
                if ((i & i3) == i3) {
                    if (AccessNetworksManager.this.mDataConfigManager != null) {
                        i2 |= i3;
                    }
                    if (AccessNetworksManager.this.mAvailableNetworks.get(i3) != null && Arrays.equals((int[]) AccessNetworksManager.this.mAvailableNetworks.get(i3), iArr)) {
                        AccessNetworksManager.this.log("Available networks for " + ApnSetting.getApnTypesStringFromBitmask(i3) + " not changed.");
                    } else {
                        int length = iArr.length;
                        AccessNetworksManager accessNetworksManager = AccessNetworksManager.this;
                        if (length == 0) {
                            accessNetworksManager.mAvailableNetworks.remove(i3);
                            if (AccessNetworksManager.this.getPreferredTransport(i3) == 2) {
                                AccessNetworksManager.this.mPreferredTransports.put(Integer.valueOf(i3), 1);
                                AccessNetworksManager.this.mAccessNetworksManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.AccessNetworksManager$QualifiedNetworksServiceCallback$$ExternalSyntheticLambda2
                                    @Override // java.util.function.Consumer
                                    public final void accept(Object obj) {
                                        AccessNetworksManager.AccessNetworksManagerCallback accessNetworksManagerCallback = (AccessNetworksManager.AccessNetworksManagerCallback) obj;
                                        accessNetworksManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.AccessNetworksManager$QualifiedNetworksServiceCallback$$ExternalSyntheticLambda3
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                accessNetworksManagerCallback.onPreferredTransportChanged(DataUtils.apnTypeToNetworkCapability(i), z);
                                            }
                                        });
                                    }
                                });
                            }
                        } else {
                            accessNetworksManager.mAvailableNetworks.put(i3, iArr);
                            arrayList.add(new QualifiedNetworks(i3, iArr));
                        }
                    }
                }
            }
            if (i2 != i && AccessNetworksManager.this.mDataConfigManager != null && AccessNetworksManager.this.mDataConfigManager.isInvalidQnsParamAnomalyReportEnabled()) {
                AccessNetworksManager.this.reportAnomaly("QNS requested unsupported APN Types:" + Integer.toBinaryString(i ^ i2), "3e89a3df-3524-45fa-b5f2-0fb0e4c77ec5");
            }
            if (arrayList.isEmpty()) {
                return;
            }
            AccessNetworksManager.this.setPreferredTransports(arrayList, z);
            AccessNetworksManager.this.mQualifiedNetworksChangedRegistrants.notifyResult(arrayList);
        }

        public static /* synthetic */ boolean $r8$lambda$_Nr8PqOEokXAXom7HMUsgwJZaMw(int i) {
            return !DataUtils.isValidAccessNetwork(i);
        }

        public void onNetworkValidationRequested(int i, final IIntegerConsumer iIntegerConsumer) {
            DataNetworkController dataNetworkController = AccessNetworksManager.this.mPhone.getDataNetworkController();
            AccessNetworksManager.this.log("onNetworkValidationRequested: networkCapability = [" + DataUtils.networkCapabilityToString(i) + "]");
            dataNetworkController.requestNetworkValidation(i, new Consumer() { // from class: com.android.internal.telephony.data.AccessNetworksManager$QualifiedNetworksServiceCallback$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$onNetworkValidationRequested$3(iIntegerConsumer, (Integer) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNetworkValidationRequested$3(IIntegerConsumer iIntegerConsumer, Integer num) {
            try {
                AccessNetworksManager.this.log("onNetworkValidationRequestDone:" + DataServiceCallback.resultCodeToString(num.intValue()));
                iIntegerConsumer.accept(num.intValue());
            } catch (RemoteException e) {
                AccessNetworksManager.this.loge("onNetworkValidationRequestDone RemoteException" + e);
            }
        }

        public void onReconnectQualifiedNetworkType(int i, int i2) {
            AccessNetworksManager.this.log("onReconnectQualifiedNetworkType: apnTypes = [" + ApnSetting.getApnTypesStringFromBitmask(i) + "], networks = [" + AccessNetworkConstants.AccessNetworkType.toString(i2) + "]");
            handleQualifiedNetworksChanged(i, new int[]{i2}, true);
        }
    }

    private void onEmergencyDataNetworkPreferredTransportChanged(int i) {
        try {
            logl("onEmergencyDataNetworkPreferredTransportChanged: " + AccessNetworkConstants.transportTypeToString(i));
            IQualifiedNetworksService iQualifiedNetworksService = this.mIQualifiedNetworksService;
            if (iQualifiedNetworksService != null) {
                iQualifiedNetworksService.reportEmergencyDataNetworkPreferredTransportChanged(this.mPhone.getPhoneId(), i);
            }
        } catch (Exception e) {
            loge("onEmergencyDataNetworkPreferredTransportChanged: ", e);
        }
    }

    public static abstract class AccessNetworksManagerCallback extends DataCallback {
        public abstract void onPreferredTransportChanged(int i, boolean z);

        public AccessNetworksManagerCallback(Executor executor) {
            super(executor);
        }
    }

    public AccessNetworksManager(Phone phone, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mLocalLog = new LocalLog(64);
        this.mAnomalyUUID = UUID.fromString("c2d1a639-00e2-4561-9619-6acf37d90590");
        this.mAvailableNetworks = new SparseArray();
        this.mQualifiedNetworksChangedRegistrants = new RegistrantList();
        this.mPreferredTransports = new ConcurrentHashMap();
        this.mAccessNetworksManagerCallbacks = new ArraySet();
        this.mPhone = phone;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        this.mCarrierConfigManager = carrierConfigManager;
        this.mLogTag = "ANM-" + phone.getPhoneId();
        this.mApnTypeToQnsChangeNetworkCounter = new SparseArray();
        this.mFeatureFlags = featureFlags;
        if (isInLegacyMode()) {
            log("operates in legacy mode.");
            this.mAvailableTransports = new int[]{1};
        } else {
            log("operates in AP-assisted mode.");
            this.mAvailableTransports = new int[]{1, 2};
            if (carrierConfigManager != null) {
                carrierConfigManager.registerCarrierConfigChangeListener(new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.data.AccessNetworksManager$$ExternalSyntheticLambda0
                    @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                    public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                        this.f$0.lambda$new$0(i, i2, i3, i4);
                    }
                });
            }
            bindQualifiedNetworksService();
        }
        post(new Runnable() { // from class: com.android.internal.telephony.data.AccessNetworksManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1();
            }
        });
        PhoneConfigurationManager.registerForMultiSimConfigChange(this, 2, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        if (i != this.mPhone.getPhoneId()) {
            return;
        }
        bindQualifiedNetworksService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mPhone.getDataNetworkController().getDataRetryManager().registerCallback(new DataRetryManager.DataRetryManagerCallback(new Executor() { // from class: com.android.internal.telephony.data.AccessNetworksManager$$ExternalSyntheticLambda3
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.AccessNetworksManager.1
            @Override // com.android.internal.telephony.data.DataRetryManager.DataRetryManagerCallback
            public void onThrottleStatusChanged(List<ThrottleStatus> list) {
                try {
                    AccessNetworksManager.this.logl("onThrottleStatusChanged: " + list);
                    if (AccessNetworksManager.this.mIQualifiedNetworksService != null) {
                        AccessNetworksManager.this.mIQualifiedNetworksService.reportThrottleStatusChanged(AccessNetworksManager.this.mPhone.getPhoneId(), list);
                    }
                } catch (Exception e) {
                    AccessNetworksManager.this.loge("onThrottleStatusChanged: ", e);
                }
            }
        });
        DataConfigManager dataConfigManager = this.mPhone.getDataNetworkController().getDataConfigManager();
        this.mDataConfigManager = dataConfigManager;
        dataConfigManager.registerCallback(new DataConfigManager.DataConfigManagerCallback(new Executor() { // from class: com.android.internal.telephony.data.AccessNetworksManager$$ExternalSyntheticLambda3
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.AccessNetworksManager.2
            @Override // com.android.internal.telephony.data.DataConfigManager.DataConfigManagerCallback
            public void onDeviceConfigChanged() {
                AccessNetworksManager.this.mApnTypeToQnsChangeNetworkCounter.clear();
            }
        });
        this.mPhone.registerForEmergencyDomainSelected(this, 1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportAnomaly(String str, String str2) {
        logl(str);
        AnomalyReporter.reportAnomaly(UUID.fromString(str2), str, this.mPhone.getCarrierId());
    }

    private void bindQualifiedNetworksService() {
        post(new Runnable() { // from class: com.android.internal.telephony.data.AccessNetworksManager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$bindQualifiedNetworksService$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindQualifiedNetworksService$2() {
        Intent component;
        String qualifiedNetworksServicePackageName = getQualifiedNetworksServicePackageName();
        String qualifiedNetworksServiceClassName = getQualifiedNetworksServiceClassName();
        if (TextUtils.isEmpty(qualifiedNetworksServicePackageName)) {
            loge("Can't find the binding package");
            return;
        }
        if (TextUtils.isEmpty(qualifiedNetworksServiceClassName)) {
            component = new Intent("android.telephony.data.QualifiedNetworksService");
            component.setPackage(qualifiedNetworksServicePackageName);
        } else {
            component = new Intent("android.telephony.data.QualifiedNetworksService").setComponent(new ComponentName(qualifiedNetworksServicePackageName, qualifiedNetworksServiceClassName));
        }
        if (TextUtils.equals(qualifiedNetworksServicePackageName, this.mTargetBindingPackageName)) {
            return;
        }
        IQualifiedNetworksService iQualifiedNetworksService = this.mIQualifiedNetworksService;
        if (iQualifiedNetworksService != null && iQualifiedNetworksService.asBinder().isBinderAlive()) {
            try {
                this.mIQualifiedNetworksService.removeNetworkAvailabilityProvider(this.mPhone.getPhoneId());
            } catch (RemoteException e) {
                loge("Cannot remove network availability updater. " + e);
            }
            if (this.mServiceConnection != null) {
                this.mPhone.getContext().unbindService(this.mServiceConnection);
            }
        }
        try {
            this.mServiceConnection = new QualifiedNetworksServiceConnection();
            log("bind to " + qualifiedNetworksServicePackageName);
            if (!this.mPhone.getContext().bindService(component, this.mServiceConnection, 1)) {
                loge("Cannot bind to the qualified networks service.");
            } else {
                this.mTargetBindingPackageName = qualifiedNetworksServicePackageName;
            }
        } catch (Exception e2) {
            loge("Cannot bind to the qualified networks service. Exception: " + e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getQualifiedNetworksServicePackageName() {
        String string = this.mPhone.getContext().getResources().getString(R.string.disable_accessibility_shortcut);
        try {
            PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(this.mPhone.getSubId(), "carrier_qualified_networks_service_package_override_string");
            if (!configForSubId.isEmpty()) {
                String string2 = configForSubId.getString("carrier_qualified_networks_service_package_override_string");
                if (!TextUtils.isEmpty(string2)) {
                    return string2;
                }
            }
            return string;
        } catch (RuntimeException unused) {
            loge("Carrier config loader is not available.");
            return string;
        }
    }

    private String getQualifiedNetworksServiceClassName() {
        String string = this.mPhone.getContext().getResources().getString(R.string.dial_number_using);
        try {
            PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(this.mPhone.getSubId(), "carrier_qualified_networks_service_class_override_string");
            if (!configForSubId.isEmpty()) {
                String string2 = configForSubId.getString("carrier_qualified_networks_service_class_override_string");
                if (!TextUtils.isEmpty(string2)) {
                    return string2;
                }
            }
            return string;
        } catch (RuntimeException unused) {
            loge("Carrier config loader is not available.");
            return string;
        }
    }

    private List getQualifiedNetworksList() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mAvailableNetworks.size(); i++) {
            arrayList.add(new QualifiedNetworks(this.mAvailableNetworks.keyAt(i), (int[]) this.mAvailableNetworks.valueAt(i)));
        }
        return arrayList;
    }

    public void registerForQualifiedNetworksChanged(Handler handler, int i) {
        if (handler != null) {
            Registrant registrant = new Registrant(handler, i, null);
            this.mQualifiedNetworksChangedRegistrants.add(registrant);
            if (this.mAvailableNetworks.size() != 0) {
                registrant.notifyResult(getQualifiedNetworksList());
            }
        }
    }

    public synchronized int[] getAvailableTransports() {
        return this.mAvailableTransports;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPreferredTransports(List list, final boolean z) {
        int transportFromAccessNetwork;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            final QualifiedNetworks qualifiedNetworks = (QualifiedNetworks) it.next();
            int[] iArr = qualifiedNetworks.qualifiedNetworks;
            if (iArr.length > 0 && getPreferredTransport(qualifiedNetworks.apnType) != (transportFromAccessNetwork = getTransportFromAccessNetwork(iArr[0]))) {
                this.mPreferredTransports.put(Integer.valueOf(qualifiedNetworks.apnType), Integer.valueOf(transportFromAccessNetwork));
                this.mAccessNetworksManagerCallbacks.forEach(new Consumer() { // from class: com.android.internal.telephony.data.AccessNetworksManager$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AccessNetworksManager.AccessNetworksManagerCallback accessNetworksManagerCallback = (AccessNetworksManager.AccessNetworksManagerCallback) obj;
                        accessNetworksManagerCallback.invokeFromExecutor(new Runnable() { // from class: com.android.internal.telephony.data.AccessNetworksManager$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                accessNetworksManagerCallback.onPreferredTransportChanged(DataUtils.apnTypeToNetworkCapability(qualifiedNetworks.apnType), z);
                            }
                        });
                    }
                });
                StringBuilder sb = new StringBuilder("setPreferredTransports: apnType=");
                sb.append(ApnSetting.getApnTypeString(qualifiedNetworks.apnType));
                sb.append(", transport=");
                sb.append(AccessNetworkConstants.transportTypeToString(transportFromAccessNetwork));
                sb.append(z ? ", forceReconnect:true" : "");
                logl(sb.toString());
            }
        }
    }

    public int getPreferredTransport(int i) {
        if (isInLegacyMode() || this.mPreferredTransports.get(Integer.valueOf(i)) == null) {
            return 1;
        }
        return ((Integer) this.mPreferredTransports.get(Integer.valueOf(i))).intValue();
    }

    public int getPreferredTransportByNetworkCapability(int i) {
        int iNetworkCapabilityToApnType = DataUtils.networkCapabilityToApnType(i);
        if (iNetworkCapabilityToApnType == 0) {
            return 1;
        }
        return getPreferredTransport(iNetworkCapabilityToApnType);
    }

    public boolean isAnyApnOnIwlan() {
        for (int i : SUPPORTED_APN_TYPES) {
            if (getPreferredTransport(i) == 2) {
                return true;
            }
        }
        return false;
    }

    public void unregisterForQualifiedNetworksChanged(Handler handler) {
        if (handler != null) {
            this.mQualifiedNetworksChangedRegistrants.remove(handler);
        }
    }

    public void registerCallback(AccessNetworksManagerCallback accessNetworksManagerCallback) {
        this.mAccessNetworksManagerCallbacks.add(accessNetworksManagerCallback);
    }

    public void unregisterCallback(AccessNetworksManagerCallback accessNetworksManagerCallback) {
        this.mAccessNetworksManagerCallbacks.remove(accessNetworksManagerCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d(this.mLogTag, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str, Exception exc) {
        Rlog.e(this.mLogTag, str, exc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(AccessNetworksManager.class.getSimpleName() + "-" + this.mPhone.getPhoneId() + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("preferred transports=");
        androidUtilIndentingPrintWriter.increaseIndent();
        for (int i : SUPPORTED_APN_TYPES) {
            androidUtilIndentingPrintWriter.println(ApnSetting.getApnTypeString(i) + ": " + AccessNetworkConstants.transportTypeToString(getPreferredTransport(i)));
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("isInLegacy=" + isInLegacyMode());
        androidUtilIndentingPrintWriter.println("IWLAN operation mode=" + SystemProperties.get(SYSTEM_PROPERTIES_IWLAN_OPERATION_MODE));
        androidUtilIndentingPrintWriter.println("Local logs=");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.flush();
    }
}
