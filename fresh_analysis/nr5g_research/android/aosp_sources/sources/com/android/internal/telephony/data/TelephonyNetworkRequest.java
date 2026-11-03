package com.android.internal.telephony.data;

import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.os.SystemClock;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataProfile;
import android.telephony.data.TrafficDescriptor;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperFrameworkTelephonyManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyNetworkRequest {
    public static final int CAPABILITY_ATTRIBUTE_APN_SETTING = 1;
    private static final Map CAPABILITY_ATTRIBUTE_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry(0, 11), new AbstractMap.SimpleImmutableEntry(1, 11), new AbstractMap.SimpleImmutableEntry(2, 3), new AbstractMap.SimpleImmutableEntry(3, 3), new AbstractMap.SimpleImmutableEntry(4, 11), new AbstractMap.SimpleImmutableEntry(5, 7), new AbstractMap.SimpleImmutableEntry(9, 3), new AbstractMap.SimpleImmutableEntry(10, 3), new AbstractMap.SimpleImmutableEntry(12, 11), new AbstractMap.SimpleImmutableEntry(23, 3), new AbstractMap.SimpleImmutableEntry(29, 7), new AbstractMap.SimpleImmutableEntry(30, 3), new AbstractMap.SimpleImmutableEntry(31, 3), new AbstractMap.SimpleImmutableEntry(55, 3), new AbstractMap.SimpleImmutableEntry(56, 3), new AbstractMap.SimpleImmutableEntry(57, 3), new AbstractMap.SimpleImmutableEntry(58, 3), new AbstractMap.SimpleImmutableEntry(34, 12), new AbstractMap.SimpleImmutableEntry(35, 12), new AbstractMap.SimpleImmutableEntry(8, 3), new AbstractMap.SimpleImmutableEntry(38, 12));
    public static final int CAPABILITY_ATTRIBUTE_NONE = 0;
    public static final int CAPABILITY_ATTRIBUTE_TRAFFIC_DESCRIPTOR_CONNECTION_CAPABILITY = 8;
    public static final int CAPABILITY_ATTRIBUTE_TRAFFIC_DESCRIPTOR_DNN = 2;
    public static final int CAPABILITY_ATTRIBUTE_TRAFFIC_DESCRIPTOR_OS_APP_ID = 4;
    public static final int REQUEST_STATE_SATISFIED = 1;
    public static final int REQUEST_STATE_UNSATISFIED = 0;
    private DataNetwork mAttachedDataNetwork;
    private int mCapabilitiesAttributes;
    private final long mCreatedTimeMillis;
    private DataConfigManager mDataConfigManager;
    private DataEvaluation mEvaluation;
    private final FeatureFlags mFeatureFlags;
    private final NetworkRequest mNativeNetworkRequest;
    private int mPriority;
    private int mState;

    public TelephonyNetworkRequest(NetworkRequest networkRequest, Phone phone, FeatureFlags featureFlags) {
        this(networkRequest, featureFlags);
        this.mDataConfigManager = phone.getDataNetworkController().getDataConfigManager();
        updateCapabilitiesAttributes();
        updatePriority();
    }

    public TelephonyNetworkRequest(NetworkRequest networkRequest, FeatureFlags featureFlags) {
        this.mNativeNetworkRequest = networkRequest;
        this.mFeatureFlags = featureFlags;
        updateCapabilitiesAttributes();
        this.mPriority = 0;
        this.mAttachedDataNetwork = null;
        this.mState = 0;
        this.mCreatedTimeMillis = SystemClock.elapsedRealtime();
    }

    public void updateDataConfig(DataConfigManager dataConfigManager) {
        this.mDataConfigManager = dataConfigManager;
        updateCapabilitiesAttributes();
        updatePriority();
    }

    private void updateCapabilitiesAttributes() {
        int iIntValue = 0;
        for (int i : this.mNativeNetworkRequest.getCapabilities()) {
            iIntValue |= ((Integer) CAPABILITY_ATTRIBUTE_MAP.getOrDefault(Integer.valueOf(i), 0)).intValue();
            DataConfigManager dataConfigManager = this.mDataConfigManager;
            if (dataConfigManager != null && dataConfigManager.networkCapabilityToConnectionCapability(i) != 0) {
                iIntValue |= 8;
            }
        }
        this.mCapabilitiesAttributes = iIntValue;
    }

    public NetworkSpecifier getNetworkSpecifier() {
        return this.mNativeNetworkRequest.getNetworkSpecifier();
    }

    public int[] getCapabilities() {
        return this.mNativeNetworkRequest.getCapabilities();
    }

    public boolean hasCapability(int i) {
        return this.mNativeNetworkRequest.hasCapability(i);
    }

    public int[] getTransportTypes() {
        return this.mNativeNetworkRequest.getTransportTypes();
    }

    public boolean hasTransport(int i) {
        return this.mNativeNetworkRequest.hasTransport(i);
    }

    public boolean canBeSatisfiedBy(NetworkCapabilities networkCapabilities) {
        return this.mNativeNetworkRequest.canBeSatisfiedBy(networkCapabilities);
    }

    public boolean hasAttribute(int i) {
        return (this.mCapabilitiesAttributes & i) == i;
    }

    public boolean canBeSatisfiedBy(DataProfile dataProfile) {
        IHyperFrameworkTelephonyManager iHyperFrameworkTelephonyManager = (IHyperFrameworkTelephonyManager) HyperTelephonyFactory.getInstance().initModule(IHyperFrameworkTelephonyManager.DEFAULT, new Object[0]);
        if (iHyperFrameworkTelephonyManager.canFqdnBeSatisfiedBy(dataProfile) || iHyperFrameworkTelephonyManager.canIpDescriptorBeSatisfiedBy(dataProfile)) {
            return true;
        }
        if (hasAttribute(4) && getOsAppId() != null && ((dataProfile.getTrafficDescriptor() != null && Arrays.equals(getOsAppId().getBytes(), dataProfile.getTrafficDescriptor().getOsAppId())) || iHyperFrameworkTelephonyManager.canBeSatisfiedBy(dataProfile))) {
            return true;
        }
        if (hasAttribute(8) && dataProfile.getTrafficDescriptor() != null) {
            int highestPrioritySupportedNetworkCapability = getHighestPrioritySupportedNetworkCapability();
            DataConfigManager dataConfigManager = this.mDataConfigManager;
            int iNetworkCapabilityToConnectionCapability = dataConfigManager != null ? dataConfigManager.networkCapabilityToConnectionCapability(highestPrioritySupportedNetworkCapability) : 0;
            if (iNetworkCapabilityToConnectionCapability != 0 && dataProfile.getTrafficDescriptor().getConnectionCapability() == iNetworkCapabilityToConnectionCapability) {
                return true;
            }
        }
        if ((!hasAttribute(1) && !hasAttribute(2)) || dataProfile.getApnSetting() == null) {
            return false;
        }
        if (this.mNativeNetworkRequest.hasTransport(0) && !this.mNativeNetworkRequest.hasTransport(10)) {
            if (this.mDataConfigManager != null) {
                IntStream intStreamStream = Arrays.stream(getCapabilities());
                Set<Integer> forcedCellularTransportCapabilities = this.mDataConfigManager.getForcedCellularTransportCapabilities();
                Objects.requireNonNull(forcedCellularTransportCapabilities);
                if (intStreamStream.noneMatch(new DataNetworkController$$ExternalSyntheticLambda20(forcedCellularTransportCapabilities)) && !dataProfile.getApnSetting().isForInfrastructure(1)) {
                    return false;
                }
            }
        } else if (this.mNativeNetworkRequest.hasTransport(10) && !this.mNativeNetworkRequest.hasTransport(0) && !dataProfile.getApnSetting().isForInfrastructure(2)) {
            return false;
        }
        List list = (List) Arrays.stream(getCapabilities()).boxed().map(new Function() { // from class: com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(DataUtils.networkCapabilityToApnType(((Integer) obj).intValue()));
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyNetworkRequest.$r8$lambda$caix5lRLRYWuoTi_4dC08O3KLYM((Integer) obj);
            }
        }).collect(Collectors.toList());
        if (list.contains(16384)) {
            list.remove((Object) 17);
        }
        if (list.contains(2) && dataProfile.getApnSetting().getEditedStatus() == 1 && dataProfile.getApnSetting().getMmsc() == null) {
            return false;
        }
        Stream stream = list.stream();
        final ApnSetting apnSetting = dataProfile.getApnSetting();
        Objects.requireNonNull(apnSetting);
        return stream.allMatch(new Predicate() { // from class: com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return apnSetting.canHandleType(((Integer) obj).intValue());
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$caix5lRLRYWuoTi_4dC08O3KLYM(Integer num) {
        return num.intValue() != 0;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public void updatePriority() {
        if (this.mDataConfigManager != null) {
            IntStream intStreamStream = Arrays.stream(this.mNativeNetworkRequest.getCapabilities());
            final DataConfigManager dataConfigManager = this.mDataConfigManager;
            Objects.requireNonNull(dataConfigManager);
            this.mPriority = intStreamStream.map(new IntUnaryOperator() { // from class: com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda8
                @Override // java.util.function.IntUnaryOperator
                public final int applyAsInt(int i) {
                    return dataConfigManager.getNetworkCapabilityPriority(i);
                }
            }).max().orElse(0);
        }
    }

    public int getHighestPriorityApnTypeNetworkCapability() {
        if (!hasAttribute(1) || this.mDataConfigManager == null) {
            return -1;
        }
        Stream<Integer> streamFilter = Arrays.stream(getCapabilities()).boxed().filter(new Predicate() { // from class: com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyNetworkRequest.$r8$lambda$6fzZqIg42q63ZK3Bgq0ufbPHjT8((Integer) obj);
            }
        });
        DataConfigManager dataConfigManager = this.mDataConfigManager;
        Objects.requireNonNull(dataConfigManager);
        return streamFilter.max(Comparator.comparingInt(new DataNetwork$$ExternalSyntheticLambda1(dataConfigManager))).orElse(-1).intValue();
    }

    public static /* synthetic */ boolean $r8$lambda$6fzZqIg42q63ZK3Bgq0ufbPHjT8(Integer num) {
        return DataUtils.networkCapabilityToApnType(num.intValue()) != 0;
    }

    public int getHighestPrioritySupportedNetworkCapability() {
        if (this.mDataConfigManager == null) {
            return -1;
        }
        Stream<Integer> streamFilter = Arrays.stream(getCapabilities()).boxed().filter(new Predicate() { // from class: com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getHighestPrioritySupportedNetworkCapability$2((Integer) obj);
            }
        });
        DataConfigManager dataConfigManager = this.mDataConfigManager;
        Objects.requireNonNull(dataConfigManager);
        return streamFilter.max(Comparator.comparingInt(new DataNetwork$$ExternalSyntheticLambda1(dataConfigManager))).orElse(-1).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getHighestPrioritySupportedNetworkCapability$2(Integer num) {
        return CAPABILITY_ATTRIBUTE_MAP.containsKey(num) || this.mDataConfigManager.networkCapabilityToConnectionCapability(num.intValue()) != 0;
    }

    public static List<Integer> getAllSupportedNetworkCapabilities() {
        HalVersion halVersion = PhoneFactory.getDefaultPhone().getHalVersion();
        final boolean z = halVersion != null && halVersion.lessOrEqual(RIL.RADIO_HAL_VERSION_1_5);
        return CAPABILITY_ATTRIBUTE_MAP.entrySet().stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyNetworkRequest.m906$r8$lambda$cXHEmnEkOHMn0WpxnbrXd8ws0(z, (Map.Entry) obj);
            }
        }).map(new TelephonyNetworkRequest$$ExternalSyntheticLambda1()).toList();
    }

    /* JADX INFO: renamed from: $r8$lambda$-cXHEmnEkOHMn0Wpxnb-rXd8ws0, reason: not valid java name */
    public static /* synthetic */ boolean m906$r8$lambda$cXHEmnEkOHMn0WpxnbrXd8ws0(boolean z, Map.Entry entry) {
        return (z && (((Integer) entry.getValue()).intValue() & 1) == 0) ? false : true;
    }

    public NetworkRequest getNativeNetworkRequest() {
        return this.mNativeNetworkRequest;
    }

    public void setAttachedNetwork(DataNetwork dataNetwork) {
        this.mAttachedDataNetwork = dataNetwork;
    }

    public DataNetwork getAttachedNetwork() {
        return this.mAttachedDataNetwork;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public int getState() {
        return this.mState;
    }

    public void setEvaluation(DataEvaluation dataEvaluation) {
        this.mEvaluation = dataEvaluation;
    }

    public int getCapabilityDifferentiator() {
        if (hasCapability(29)) {
            int[] enterpriseIds = this.mNativeNetworkRequest.getEnterpriseIds();
            if (enterpriseIds.length > 0) {
                return enterpriseIds[0];
            }
        }
        return 0;
    }

    public TrafficDescriptor.OsAppId getOsAppId() {
        int iIntValue;
        if (!hasAttribute(4) || (iIntValue = Arrays.stream(getCapabilities()).boxed().filter(new Predicate() { // from class: com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyNetworkRequest.$r8$lambda$vSVRfyOU_VLu9E7OdRfKUGmg3tc((Integer) obj);
            }
        }).findFirst().orElse(-1).intValue()) == -1) {
            return null;
        }
        int capabilityDifferentiator = getCapabilityDifferentiator();
        if (capabilityDifferentiator > 0) {
            return new TrafficDescriptor.OsAppId(TrafficDescriptor.OsAppId.ANDROID_OS_ID, DataUtils.networkCapabilityToString(iIntValue), capabilityDifferentiator);
        }
        return new TrafficDescriptor.OsAppId(TrafficDescriptor.OsAppId.ANDROID_OS_ID, DataUtils.networkCapabilityToString(iIntValue));
    }

    public static /* synthetic */ boolean $r8$lambda$vSVRfyOU_VLu9E7OdRfKUGmg3tc(Integer num) {
        return (((Integer) CAPABILITY_ATTRIBUTE_MAP.getOrDefault(num, 0)).intValue() & 4) != 0;
    }

    private static String requestStateToString(int i) {
        if (i == 0) {
            return "UNSATISFIED";
        }
        if (i == 1) {
            return "SATISFIED";
        }
        return "UNKNOWN(" + i + ")";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(this.mNativeNetworkRequest);
        sb.append(", mPriority=");
        sb.append(this.mPriority);
        sb.append(", state=");
        sb.append(requestStateToString(this.mState));
        sb.append(", mAttachedDataNetwork=");
        DataNetwork dataNetwork = this.mAttachedDataNetwork;
        sb.append(dataNetwork != null ? dataNetwork.name() : null);
        sb.append(", created time=");
        sb.append(DataUtils.elapsedTimeToString(this.mCreatedTimeMillis));
        sb.append(", evaluation result=");
        sb.append(this.mEvaluation);
        sb.append("]");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.mNativeNetworkRequest.equals(((TelephonyNetworkRequest) obj).mNativeNetworkRequest);
    }

    public int hashCode() {
        return this.mNativeNetworkRequest.hashCode();
    }
}
