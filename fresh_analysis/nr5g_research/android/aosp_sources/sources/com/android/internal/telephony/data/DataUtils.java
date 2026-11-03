package com.android.internal.telephony.data;

import android.os.SystemClock;
import android.telephony.TelephonyBaseUtilsStub;
import android.util.ArrayMap;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.InboundSmsTracker;
import com.android.internal.telephony.NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.util.NetworkStackConstants;
import com.android.telephony.Rlog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class DataUtils {
    public static final int NET_CAPABILITY_PRIORITIZE_UNIFIED_COMMUNICATIONS = 38;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static int apnTypeToNetworkCapability(int i) {
        switch (i) {
            case 2:
                return 0;
            case 4:
                return 1;
            case 8:
                return 2;
            case 17:
                return 12;
            case 32:
                return 3;
            case 64:
                return 4;
            case 128:
                return 5;
            case CallFailCause.RADIO_UPLINK_FAILURE /* 256 */:
                return 7;
            case NtnRILConstants.MI_NTN_OEM_ERROR_12 /* 512 */:
                return 10;
            case NtnRILConstants.MI_NTN_RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE /* 1024 */:
                return 23;
            case 2048:
                return 9;
            case 4096:
                return 30;
            case NetworkStackConstants.IPV4_FLAG_MF /* 8192 */:
                return 31;
            case 16384:
                return 29;
            case 32768:
                return 8;
            case InboundSmsTracker.DEST_PORT_FLAG_NO_PORT /* 65536 */:
                return 22;
            case InboundSmsTracker.DEST_PORT_FLAG_3GPP /* 131072 */:
                return 26;
            case InboundSmsTracker.DEST_PORT_FLAG_3GPP2 /* 262144 */:
                return 55;
            case InboundSmsTracker.DEST_PORT_FLAG_3GPP2_WAP_PDU /* 524288 */:
                return 56;
            case 1048576:
                return 57;
            case 2097152:
                return 58;
            default:
                return -1;
        }
    }

    public static int getTargetTransport(int i) {
        return i == 1 ? 2 : 1;
    }

    public static boolean isValidAccessNetwork(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    public static int networkCapabilityToApnType(int i) {
        if (i == 0) {
            return 2;
        }
        if (i == 1) {
            return 4;
        }
        if (i == 2) {
            return 8;
        }
        if (i == 3) {
            return 32;
        }
        if (i == 4) {
            return 64;
        }
        if (i == 5) {
            return 128;
        }
        if (i == 12) {
            return 17;
        }
        if (i == 26) {
            return InboundSmsTracker.DEST_PORT_FLAG_3GPP;
        }
        if (i == 22) {
            return InboundSmsTracker.DEST_PORT_FLAG_NO_PORT;
        }
        if (i == 23) {
            return NtnRILConstants.MI_NTN_RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE;
        }
        switch (i) {
            case 7:
                return CallFailCause.RADIO_UPLINK_FAILURE;
            case 8:
                return 32768;
            case 9:
                return 2048;
            case 10:
                return NtnRILConstants.MI_NTN_OEM_ERROR_12;
            default:
                switch (i) {
                    case 29:
                        return 16384;
                    case 30:
                        return 4096;
                    case 31:
                        return NetworkStackConstants.IPV4_FLAG_MF;
                    default:
                        switch (i) {
                            case 55:
                                return InboundSmsTracker.DEST_PORT_FLAG_3GPP2;
                            case 56:
                                return InboundSmsTracker.DEST_PORT_FLAG_3GPP2_WAP_PDU;
                            case 57:
                                return 1048576;
                            case 58:
                                return 2097152;
                            default:
                                return 0;
                        }
                }
        }
    }

    public static int networkTypeToAccessNetworkType(int i) {
        switch (i) {
            case 1:
            case 2:
            case 16:
                return 1;
            case 3:
            case 8:
            case 9:
            case 10:
            case 15:
            case 17:
                return 2;
            case 4:
            case 5:
            case 6:
            case 7:
            case 12:
            case 14:
                return 4;
            case 11:
            default:
                return 0;
            case 13:
            case 19:
                return 3;
            case 18:
                return 5;
            case 20:
                return 6;
        }
    }

    public static int getNetworkCapabilityFromString(String str) {
        String upperCase = str.toUpperCase(Locale.ROOT);
        upperCase.hashCode();
        switch (upperCase) {
            case "PRIORITIZE_BANDWIDTH":
                return 35;
            case "ENTERPRISE":
                return 29;
            case "BIP":
                return 31;
            case "CBS":
                return 5;
            case "DUN":
                return 2;
            case "IMS":
                return 4;
            case "MCX":
                return 23;
            case "MMS":
                return 0;
            case "RCS":
                return 8;
            case "EIMS":
                return 10;
            case "FOTA":
                return 3;
            case "SUPL":
                return 1;
            case "VSIM":
                return 30;
            case "XCAP":
                return 9;
            case "PRIORITIZE_UNIFIED_COMMUNICATIONS":
                return 38;
            case "PRIORITIZE_LATENCY":
                return 34;
            case "CAP_MICAR_APN1":
                return 55;
            case "CAP_MICAR_APN2":
                return 56;
            case "CAP_MICAR_APN3":
                return 57;
            case "CAP_MICAR_APN4":
                return 58;
            case "INTERNET":
                return 12;
            default:
                loge("Illegal network capability: ".concat(str));
                return -1;
        }
    }

    public static Set<Integer> getNetworkCapabilitiesFromString(String str) {
        if (TelephonyBaseUtilsStub.isAutoDevice()) {
            if (!str.matches("(\\s*\\w+\\s*)(\\|\\s*\\w+\\s*)*")) {
                return Collections.singleton(-1);
            }
        } else if (!str.matches("(\\s*[a-zA-Z_]+\\s*)(\\|\\s*[a-zA-Z_]+\\s*)*")) {
            return Collections.singleton(-1);
        }
        return (Set) Arrays.stream(str.split("\\s*\\|\\s*")).map(new DataNetworkController$HandoverRule$$ExternalSyntheticLambda1()).map(new DataConfigManager$$ExternalSyntheticLambda21()).collect(Collectors.toSet());
    }

    public static String networkCapabilityToString(int i) {
        if (i == 37) {
            return "NOT_BANDWIDTH_CONSTRAINED";
        }
        if (i != 38) {
            switch (i) {
                case 0:
                    return "MMS";
                case 1:
                    return "SUPL";
                case 2:
                    return "DUN";
                case 3:
                    return "FOTA";
                case 4:
                    return "IMS";
                case 5:
                    return "CBS";
                case 6:
                    return "WIFI_P2P";
                case 7:
                    return "IA";
                case 8:
                    return "RCS";
                case 9:
                    return "XCAP";
                case 10:
                    return "EIMS";
                case 11:
                    return "NOT_METERED";
                case 12:
                    return "INTERNET";
                case 13:
                    return "NOT_RESTRICTED";
                case 14:
                    return "TRUSTED";
                case 15:
                    return "NOT_VPN";
                case 16:
                    return "VALIDATED";
                case 17:
                    return "CAPTIVE_PORTAL";
                case 18:
                    return "NOT_ROAMING";
                case 19:
                    return "FOREGROUND";
                case 20:
                    return "NOT_CONGESTED";
                case 21:
                    return "NOT_SUSPENDED";
                case 22:
                    return "OEM_PAID";
                case 23:
                    return "MCX";
                case 24:
                    return "PARTIAL_CONNECTIVITY";
                case 25:
                    return "TEMPORARILY_NOT_METERED";
                case 26:
                    return "OEM_PRIVATE";
                case 27:
                    return "VEHICLE_INTERNAL";
                case 28:
                    return "NOT_VCN_MANAGED";
                case 29:
                    return "ENTERPRISE";
                case 30:
                    return "VSIM";
                case 31:
                    return "BIP";
                case 32:
                    return "HEAD_UNIT";
                case 33:
                    return "MMTEL";
                case 34:
                    return "PRIORITIZE_LATENCY";
                case 35:
                    return "PRIORITIZE_BANDWIDTH";
                default:
                    switch (i) {
                        case 55:
                            return "CAP_MICAR_APN1";
                        case 56:
                            return "CAP_MICAR_APN2";
                        case 57:
                            return "CAP_MICAR_APN3";
                        case 58:
                            return "CAP_MICAR_APN4";
                        default:
                            loge("Unknown network capability(" + i + ")");
                            return "Unknown(" + i + ")";
                    }
            }
        }
        return "PRIORITIZE_UNIFIED_COMMUNICATIONS";
    }

    public static String connectivityTransportsToString(int[] iArr) {
        return (String) Arrays.stream(iArr).mapToObj(new IntFunction() { // from class: com.android.internal.telephony.data.DataUtils$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return DataUtils.connectivityTransportToString(i);
            }
        }).collect(Collectors.joining("|"));
    }

    public static String connectivityTransportToString(int i) {
        switch (i) {
            case 0:
                return "CELLULAR";
            case 1:
                return "WIFI";
            case 2:
                return "BLUETOOTH";
            case 3:
                return "ETHERNET";
            case 4:
                return "VPN";
            case 5:
                return "WIFI_AWARE";
            case 6:
                return "LOWPAN";
            case 7:
                return "TEST";
            case 8:
                return "USB";
            case 9:
                return "THREAD";
            case 10:
                return "SATELLITE";
            default:
                return "Unknown(" + i + ")";
        }
    }

    public static String networkCapabilitiesToString(Collection<Integer> collection) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        return "[" + ((String) collection.stream().map(new DataConfigManager$$ExternalSyntheticLambda3()).collect(Collectors.joining("|"))) + "]";
    }

    public static String networkCapabilitiesToString(int[] iArr) {
        if (iArr == null) {
            return "";
        }
        return "[" + ((String) Arrays.stream(iArr).mapToObj(new IntFunction() { // from class: com.android.internal.telephony.data.DataUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return DataUtils.networkCapabilityToString(i);
            }
        }).collect(Collectors.joining("|"))) + "]";
    }

    public static String validationStatusToString(int i) {
        if (i == 1) {
            return "VALID";
        }
        if (i == 2) {
            return "INVALID";
        }
        loge("Unknown validation status(" + i + ")");
        return "UNKNOWN(" + i + ")";
    }

    public static String elapsedTimeToString(long j) {
        if (j != 0) {
            return systemTimeToString((System.currentTimeMillis() - SystemClock.elapsedRealtime()) + j);
        }
        return "never";
    }

    public static String systemTimeToString(long j) {
        return j != 0 ? TIME_FORMAT.format(Long.valueOf(j)) : "never";
    }

    public static String imsFeatureToString(int i) {
        if (i == 1) {
            return "MMTEL";
        }
        if (i == 2) {
            return "RCS";
        }
        loge("Unknown IMS feature(" + i + ")");
        return "Unknown(" + i + ")";
    }

    public static List<DataNetworkController.NetworkRequestList> getGroupedNetworkRequestList(DataNetworkController.NetworkRequestList networkRequestList, FeatureFlags featureFlags) {
        ArrayList arrayList = new ArrayList();
        ArrayMap arrayMap = new ArrayMap();
        for (TelephonyNetworkRequest telephonyNetworkRequest : networkRequestList) {
            ((DataNetworkController.NetworkRequestList) arrayMap.computeIfAbsent(new Record((Set) Arrays.stream(telephonyNetworkRequest.getCapabilities()).boxed().collect(Collectors.toSet()), (Set) Arrays.stream(telephonyNetworkRequest.getNativeNetworkRequest().getEnterpriseIds()).boxed().collect(Collectors.toSet()), (Set) Arrays.stream(telephonyNetworkRequest.getTransportTypes()).boxed().collect(Collectors.toSet())) { // from class: com.android.internal.telephony.data.DataUtils.1NetworkCapabilitiesKey
                private final Set caps;
                private final Set enterpriseIds;
                private final Set transportTypes;

                private /* synthetic */ boolean $record$equals(Object obj) {
                    if (!(obj instanceof C1NetworkCapabilitiesKey)) {
                        return false;
                    }
                    C1NetworkCapabilitiesKey c1NetworkCapabilitiesKey = (C1NetworkCapabilitiesKey) obj;
                    return Objects.equals(this.caps, c1NetworkCapabilitiesKey.caps) && Objects.equals(this.enterpriseIds, c1NetworkCapabilitiesKey.enterpriseIds) && Objects.equals(this.transportTypes, c1NetworkCapabilitiesKey.transportTypes);
                }

                private /* synthetic */ Object[] $record$getFieldsAsObjects() {
                    return new Object[]{this.caps, this.enterpriseIds, this.transportTypes};
                }

                {
                    this.caps = set;
                    this.enterpriseIds = set;
                    this.transportTypes = set;
                }

                public Set<Integer> caps() {
                    return this.caps;
                }

                public Set<Integer> enterpriseIds() {
                    return this.enterpriseIds;
                }

                @Override // java.lang.Record
                public final boolean equals(Object obj) {
                    return $record$equals(obj);
                }

                @Override // java.lang.Record
                public final int hashCode() {
                    return DataUtils$1NetworkCapabilitiesKey$$ExternalSyntheticRecord0.m(this.caps, this.enterpriseIds, this.transportTypes);
                }

                @Override // java.lang.Record
                public final String toString() {
                    return NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), C1NetworkCapabilitiesKey.class, "caps;enterpriseIds;transportTypes");
                }

                public Set<Integer> transportTypes() {
                    return this.transportTypes;
                }
            }, new Function() { // from class: com.android.internal.telephony.data.DataUtils$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return DataUtils.$r8$lambda$c8l4SU8uiuonOy3hd23ZRSrh7C8((DataUtils.C1NetworkCapabilitiesKey) obj);
                }
            })).add(telephonyNetworkRequest);
        }
        arrayList.addAll(arrayMap.values());
        return (List) arrayList.stream().sorted(new Comparator() { // from class: com.android.internal.telephony.data.DataUtils$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Integer.compare(((DataNetworkController.NetworkRequestList) obj2).get(0).getPriority(), ((DataNetworkController.NetworkRequestList) obj).get(0).getPriority());
            }
        }).collect(Collectors.toList());
    }

    public static /* synthetic */ DataNetworkController.NetworkRequestList $r8$lambda$c8l4SU8uiuonOy3hd23ZRSrh7C8(C1NetworkCapabilitiesKey c1NetworkCapabilitiesKey) {
        return new DataNetworkController.NetworkRequestList();
    }

    public static String linkStatusToString(int i) {
        if (i == -1) {
            return SatelliteConstants.DEFAULT_PLMN;
        }
        if (i == 0) {
            return "INACTIVE";
        }
        if (i == 1) {
            return "DORMANT";
        }
        if (i == 2) {
            return "ACTIVE";
        }
        loge("Unknown link status(" + i + ")");
        return "UNKNOWN(" + i + ")";
    }

    public static String dataActivityToString(int i) {
        if (i == 0) {
            return "NONE";
        }
        if (i == 1) {
            return "IN";
        }
        if (i == 2) {
            return "OUT";
        }
        if (i == 3) {
            return "INOUT";
        }
        if (i == 4) {
            return "DORMANT";
        }
        loge("Unknown data activity(" + i + ")");
        return "UNKNOWN(" + i + ")";
    }

    private static void loge(String str) {
        Rlog.e("DataUtils", str);
    }

    public static boolean isValidDataMode(int i) {
        if (i >= 0 && 2 >= i) {
            return true;
        }
        loge("Invalid data mode: " + i + ". It's not within the allowed range of data mode: 0 - 2");
        return false;
    }
}
