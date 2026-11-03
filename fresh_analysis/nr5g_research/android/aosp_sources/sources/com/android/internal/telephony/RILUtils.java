package com.android.internal.telephony;

import android.hardware.radio.V1_0.AppStatus;
import android.hardware.radio.V1_0.Carrier;
import android.hardware.radio.V1_0.CdmaSignalStrength;
import android.hardware.radio.V1_0.CdmaSmsMessage;
import android.hardware.radio.V1_0.CdmaSmsSubaddress;
import android.hardware.radio.V1_0.Dial;
import android.hardware.radio.V1_0.EvdoSignalStrength;
import android.hardware.radio.V1_0.GsmSignalStrength;
import android.hardware.radio.V1_0.GsmSmsMessage;
import android.hardware.radio.V1_0.HardwareConfigModem;
import android.hardware.radio.V1_0.HardwareConfigSim;
import android.hardware.radio.V1_0.LteSignalStrength;
import android.hardware.radio.V1_0.SimApdu;
import android.hardware.radio.V1_0.UusInfo;
import android.hardware.radio.V1_2.CellIdentityOperatorNames;
import android.hardware.radio.V1_2.CellInfoCdma;
import android.hardware.radio.V1_2.CellInfoGsm;
import android.hardware.radio.V1_2.CellInfoTdscdma;
import android.hardware.radio.V1_2.CellInfoWcdma;
import android.hardware.radio.V1_2.TdscdmaSignalStrength;
import android.hardware.radio.V1_2.WcdmaSignalStrength;
import android.hardware.radio.V1_4.CellInfoLte;
import android.hardware.radio.V1_4.CellInfoNr;
import android.hardware.radio.V1_4.DataProfileInfo;
import android.hardware.radio.V1_4.NrSignalStrength;
import android.hardware.radio.V1_4.SetupDataCallResult;
import android.hardware.radio.V1_5.CardStatus;
import android.hardware.radio.V1_5.LinkAddress;
import android.hardware.radio.V1_5.OptionalCsgInfo;
import android.hardware.radio.V1_5.SignalThresholdInfo;
import android.hardware.radio.V1_6.EpsQos;
import android.hardware.radio.V1_6.MaybePort;
import android.hardware.radio.V1_6.NrQos;
import android.hardware.radio.V1_6.OptionalDnn;
import android.hardware.radio.V1_6.OptionalOsAppId;
import android.hardware.radio.V1_6.OptionalSliceInfo;
import android.hardware.radio.V1_6.OptionalTrafficDescriptor;
import android.hardware.radio.V1_6.OsAppId;
import android.hardware.radio.V1_6.PhonebookCapacity;
import android.hardware.radio.V1_6.PhonebookRecordInfo;
import android.hardware.radio.V1_6.PortRange;
import android.hardware.radio.V1_6.QosBandwidth;
import android.hardware.radio.V1_6.QosFilter;
import android.hardware.radio.V1_6.QosSession;
import android.hardware.radio.V1_6.RouteSelectionDescriptor;
import android.hardware.radio.V1_6.SliceInfo;
import android.hardware.radio.V1_6.SlicingConfig;
import android.hardware.radio.config.SimPortInfo;
import android.hardware.radio.config.SimSlotStatus;
import android.hardware.radio.config.SlotPortMapping;
import android.hardware.radio.data.ImsDataNetworkInfo;
import android.hardware.radio.data.QosFilterIpsecSpi;
import android.hardware.radio.data.QosFilterIpv6FlowLabel;
import android.hardware.radio.data.QosFilterTypeOfService;
import android.hardware.radio.ims.ImsCall;
import android.hardware.radio.ims.ImsService;
import android.hardware.radio.ims.SrvccCall;
import android.hardware.radio.messaging.CdmaSmsAddress;
import android.hardware.radio.network.BarringTypeSpecificInfo;
import android.hardware.radio.network.EmergencyNetworkScanTrigger;
import android.hardware.radio.network.EmergencyRegResult;
import android.hardware.radio.network.OperatorInfo;
import android.hardware.radio.network.RadioAccessSpecifierBands;
import android.hardware.radio.sim.CarrierInfo;
import android.hardware.radio.sim.Plmn;
import android.net.InetAddresses;
import android.net.LinkProperties;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.service.carrier.CarrierIdentifier;
import android.telephony.AnomalyReporter;
import android.telephony.BarringInfo;
import android.telephony.CellConfigLte;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.CellularIdentifierDisclosure;
import android.telephony.ClosedSubscriberGroupInfo;
import android.telephony.EmergencyRegistrationResult;
import android.telephony.LinkCapacityEstimate;
import android.telephony.ModemInfo;
import android.telephony.NetworkSecurityEvent;
import android.telephony.PhoneCapability;
import android.telephony.PhoneNumberUtils;
import android.telephony.RadioAccessSpecifier;
import android.telephony.SecurityAlgorithmUpdate;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.UiccSlotMapping;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.NetworkSlicingConfig;
import android.telephony.data.Qos;
import android.telephony.data.QosBearerFilter;
import android.telephony.data.QosBearerSession;
import android.telephony.data.TrafficDescriptor;
import android.telephony.data.UrspRule;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.SparseArray;
import com.android.internal.telephony.cat.ComprehensionTlv;
import com.android.internal.telephony.cat.ComprehensionTlvTag;
import com.android.internal.telephony.cdma.SmsMessage;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.imsphone.ImsCallInfo;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.uicc.AdnCapacity;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccSimPortInfo;
import com.android.internal.telephony.uicc.IccSlotPortMapping;
import com.android.internal.telephony.uicc.IccSlotStatus;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.PortUtils;
import com.android.internal.telephony.uicc.SimPhonebookRecord;
import com.android.internal.telephony.uicc.SimTypeInfo;
import com.android.telephony.Rlog;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class RILUtils {
    public static final String ANOMALY_HAL_SPECIFIER_HAS_BAD_ACCESS_NETWORK_VALUES_MSG = "RadioAccessSpecifier has bad RAN type, possibly using HIDL values or unsupported CDMA2000";
    public static final String ANOMALY_HAL_SPECIFIER_USES_PADDING_VALUES_MSG = "RadioAccessSpecifier channels arrays contained padding values";
    public static final int CDMA_BROADCAST_SMS_NO_OF_SERVICE_CATEGORIES = 31;
    public static final int CDMA_BSI_NO_OF_INTS_STRUCT = 3;
    public static final String RADIO_POWER_FAILURE_BUGREPORT_UUID = "316f3801-fa21-4954-a42f-0041eada3b31";
    public static final String RADIO_POWER_FAILURE_NO_RF_CALIBRATION_UUID = "316f3801-fa21-4954-a42f-0041eada3b33";
    public static final String RADIO_POWER_FAILURE_RF_HARDWARE_ISSUE_UUID = "316f3801-fa21-4954-a42f-0041eada3b32";
    public static final UUID ANOMALY_HAL_SPECIFIER_HAS_BAD_ACCESS_NETWORK_VALUES_UUID = UUID.fromString("b87c4f08-b24d-4f81-8179-5479903f35c6");
    public static final UUID ANOMALY_HAL_SPECIFIER_USES_PADDING_VALUES_UUID = UUID.fromString("dae80057-b69c-455e-91ec-2c898f0e2566");
    private static final Set WRAPPER_CLASSES = new HashSet(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class));

    public static int convertAidlSimLockMultiSimPolicy(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            default:
                return 0;
        }
    }

    public static int convertEmergencyScanType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    public static int convertHalCellConnectionStatus(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return -1;
            }
        }
        return i2;
    }

    public static int convertHalConnectionFailureReason(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            default:
                return 65535;
        }
    }

    public static int convertHalDeregistrationReason(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    return 0;
                }
            }
        }
        return i2;
    }

    public static int convertHalKeepaliveStatusCode(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return -1;
            }
        }
        return i2;
    }

    private static int convertHalNetworkSecurityAlertCategory(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            default:
                return 0;
        }
    }

    private static int convertHalNetworkSecurityAlertStatus(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    i2 = 4;
                    if (i != 4) {
                        i2 = 5;
                        if (i != 5) {
                            return 0;
                        }
                    }
                }
            }
        }
        return i2;
    }

    private static int convertHalNetworkSecurityReasonCode(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            case 21:
                return 21;
            case 22:
                return 22;
            case 23:
                return 23;
            case 24:
                return 24;
            case 25:
                return 25;
            case 26:
                return 26;
            case 27:
                return 27;
            default:
                return 0;
        }
    }

    public static int convertHalNetworkTypeBitMask(int i) {
        int i2 = (65536 & i) != 0 ? 32768 : 0;
        if ((i & 2) != 0) {
            i2 = (int) (((long) i2) | 1);
        }
        if ((i & 4) != 0) {
            i2 = (int) (((long) i2) | 2);
        }
        if ((i & NtnRILConstants.MI_NTN_RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE) != 0) {
            i2 = (int) (((long) i2) | 256);
        }
        if ((i & NtnRILConstants.MI_NTN_OEM_ERROR_12) != 0) {
            i2 = (int) (((long) i2) | 128);
        }
        if ((i & 2048) != 0) {
            i2 = (int) (((long) i2) | 512);
        }
        if ((32768 & i) != 0) {
            i2 = (int) (((long) i2) | 16384);
        }
        if ((i & 8) != 0) {
            i2 = (int) (((long) i2) | 4);
        }
        if ((131072 & i) != 0) {
            i2 = (int) (((long) i2) | 65536);
        }
        if ((i & 16384) != 0) {
            i2 = (int) (((long) i2) | 4096);
        }
        if ((524288 & i) != 0) {
            i2 = (int) (((long) i2) | 262144);
        }
        if ((1048576 & i) != 0) {
            i2 = (int) (((long) i2) | 524288);
        }
        if ((i & InboundSmsTracker.DEST_PORT_FLAG_3GPP2) != 0) {
            i2 = (int) (((long) i2) | 131072);
        }
        if (i2 == 0) {
            return 0;
        }
        return i2;
    }

    public static int convertHalRadioAccessNetworks(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    if (i != 4) {
                        return i != 5 ? 0 : 4;
                    }
                    return 6;
                }
            }
        }
        return i2;
    }

    public static int convertHalRegState(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        if (i == 5) {
                            return 5;
                        }
                        switch (i) {
                            case 12:
                                break;
                            case 13:
                                break;
                            case 14:
                                break;
                            default:
                                return 0;
                        }
                    }
                    return 4;
                }
                return 3;
            }
        }
        return i2;
    }

    private static int convertHalSliceStatus(byte b) {
        int i = 1;
        if (b != 1) {
            i = 2;
            if (b != 2) {
                i = 3;
                if (b != 3) {
                    i = 4;
                    if (b != 4) {
                        i = 5;
                        if (b != 5) {
                            return 0;
                        }
                    }
                }
            }
        }
        return i;
    }

    public static int convertImsCapability(int i) {
        int i2 = (i & 1) > 0 ? 1 : 0;
        if ((i & 2) > 0) {
            i2 |= 2;
        }
        if ((i & 4) > 0) {
            i2 |= 4;
        }
        return (i & 8) > 0 ? i2 | 8 : i2;
    }

    public static int convertImsRegistrationTech(int i) {
        if (i == 0) {
            return 3;
        }
        if (i == 1 || i == 2) {
            return 5;
        }
        if (i != 3) {
            return i != 4 ? 0 : 2;
        }
        return 6;
    }

    public static int convertImsTrafficDirection(int i) {
        return i != 0 ? 1 : 0;
    }

    public static int convertImsTrafficType(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    i2 = 4;
                    if (i != 4) {
                        i2 = 5;
                        if (i != 5) {
                            return 6;
                        }
                    }
                }
            }
        }
        return i2;
    }

    public static int convertToHalAccessNetwork(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return 0;
        }
    }

    public static int convertToHalAccessNetworkAidl(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return 0;
        }
    }

    public static int convertToHalRadioAccessFamily(int i) {
        long j = i;
        int i2 = (32768 & j) != 0 ? InboundSmsTracker.DEST_PORT_FLAG_NO_PORT : 0;
        if ((1 & j) != 0) {
            i2 |= 2;
        }
        if ((2 & j) != 0) {
            i2 |= 4;
        }
        if ((256 & j) != 0) {
            i2 |= NtnRILConstants.MI_NTN_RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE;
        }
        if ((128 & j) != 0) {
            i2 |= NtnRILConstants.MI_NTN_OEM_ERROR_12;
        }
        if ((512 & j) != 0) {
            i2 |= 2048;
        }
        if ((16384 & j) != 0) {
            i2 |= 32768;
        }
        if ((4 & j) != 0) {
            i2 |= 8;
        }
        if ((65536 & j) != 0) {
            i2 |= InboundSmsTracker.DEST_PORT_FLAG_3GPP;
        }
        if ((131072 & j) != 0) {
            i2 |= InboundSmsTracker.DEST_PORT_FLAG_3GPP2;
        }
        if ((4096 & j) != 0) {
            i2 |= 16384;
        }
        if ((262144 & j) != 0) {
            i2 |= InboundSmsTracker.DEST_PORT_FLAG_3GPP2_WAP_PDU;
        }
        if ((j & 524288) != 0) {
            i2 |= 1048576;
        }
        if (i2 == 0) {
            return 1;
        }
        return i2;
    }

    public static int convertToHalRadioAccessFamilyAidl(int i) {
        long j = i;
        int i2 = (32768 & j) != 0 ? InboundSmsTracker.DEST_PORT_FLAG_NO_PORT : 0;
        if ((1 & j) != 0) {
            i2 |= 2;
        }
        if ((2 & j) != 0) {
            i2 |= 4;
        }
        if ((256 & j) != 0) {
            i2 |= NtnRILConstants.MI_NTN_RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE;
        }
        if ((128 & j) != 0) {
            i2 |= NtnRILConstants.MI_NTN_OEM_ERROR_12;
        }
        if ((512 & j) != 0) {
            i2 |= 2048;
        }
        if ((16384 & j) != 0) {
            i2 |= 32768;
        }
        if ((4 & j) != 0) {
            i2 |= 8;
        }
        if ((65536 & j) != 0) {
            i2 |= InboundSmsTracker.DEST_PORT_FLAG_3GPP;
        }
        if ((131072 & j) != 0) {
            i2 |= InboundSmsTracker.DEST_PORT_FLAG_3GPP2;
        }
        if ((4096 & j) != 0 || (262144 & j) != 0) {
            i2 |= 16384;
        }
        if ((j & 524288) != 0) {
            i2 |= 1048576;
        }
        if (i2 == 0) {
            return 1;
        }
        return i2;
    }

    public static int convertToHalRadioAccessNetworks(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    if (i != 4) {
                        return i != 6 ? 0 : 4;
                    }
                    return 5;
                }
            }
        }
        return i2;
    }

    public static int convertToHalResetNvType(int i) {
        return i == 1 ? 0 : -1;
    }

    public static int convertToHalResetNvTypeAidl(int i) {
        return i == 1 ? 0 : -1;
    }

    public static int convertToHalSimLockMultiSimPolicy(int i) {
        return i != 1 ? 0 : 1;
    }

    public static int convertToHalSimLockMultiSimPolicyAidl(int i) {
        return i != 1 ? 0 : 1;
    }

    public static int convertToHalSmsWriteArgsStatus(int i) {
        int i2 = i & 7;
        if (i2 == 3) {
            return 0;
        }
        if (i2 != 5) {
            return i2 != 7 ? 1 : 2;
        }
        return 3;
    }

    public static int convertToHalSmsWriteArgsStatusAidl(int i) {
        int i2 = i & 7;
        if (i2 == 3) {
            return 0;
        }
        if (i2 != 5) {
            return i2 != 7 ? 1 : 2;
        }
        return 3;
    }

    private static int convertToImsServiceTypeAidl(int i) {
        if (i == 0) {
            return 1;
        }
        if (i != 1) {
            return i != 3 ? 0 : 3;
        }
        return 2;
    }

    private static int getAidlMappedSimType(int i) {
        if (i == -1) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    public static int convertToHalPersoType(IccCardApplicationStatus.PersoSubState persoSubState) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[persoSubState.ordinal()]) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            case 21:
                return 21;
            case 22:
                return 22;
            case 23:
                return 23;
            case 24:
                return 24;
            case 25:
                return 25;
            case 26:
                return 26;
            case 27:
                return 27;
            case 28:
                return 28;
            case 29:
                return 29;
            case 30:
                return 30;
            case 31:
                return 31;
            case 32:
                return 32;
            case 33:
                return 33;
            case 34:
                return 34;
            default:
                return 0;
        }
    }

    public static int convertToHalPersoTypeAidl(IccCardApplicationStatus.PersoSubState persoSubState) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[persoSubState.ordinal()]) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            case 21:
                return 21;
            case 22:
                return 22;
            case 23:
                return 23;
            case 24:
                return 24;
            case 25:
                return 25;
            case 26:
                return 26;
            case 27:
                return 27;
            case 28:
                return 28;
            case 29:
                return 29;
            case 30:
                return 30;
            case 31:
                return 31;
            case 32:
                return 32;
            case 33:
                return 33;
            case 34:
                return 34;
            default:
                return 0;
        }
    }

    public static GsmSmsMessage convertToHalGsmSmsMessage(String str, String str2) {
        GsmSmsMessage gsmSmsMessage = new GsmSmsMessage();
        if (str == null) {
            str = "";
        }
        gsmSmsMessage.smscPdu = str;
        if (str2 == null) {
            str2 = "";
        }
        gsmSmsMessage.pdu = str2;
        return gsmSmsMessage;
    }

    public static android.hardware.radio.messaging.GsmSmsMessage convertToHalGsmSmsMessageAidl(String str, String str2) {
        android.hardware.radio.messaging.GsmSmsMessage gsmSmsMessage = new android.hardware.radio.messaging.GsmSmsMessage();
        gsmSmsMessage.smscPdu = convertNullToEmptyString(str);
        gsmSmsMessage.pdu = convertNullToEmptyString(str2);
        return gsmSmsMessage;
    }

    public static CdmaSmsMessage convertToHalCdmaSmsMessage(byte[] bArr) {
        CdmaSmsMessage cdmaSmsMessage = new CdmaSmsMessage();
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        try {
            cdmaSmsMessage.teleserviceId = dataInputStream.readInt();
            boolean z = true;
            cdmaSmsMessage.isServicePresent = ((byte) dataInputStream.readInt()) == 1;
            cdmaSmsMessage.serviceCategory = dataInputStream.readInt();
            cdmaSmsMessage.address.digitMode = dataInputStream.read();
            cdmaSmsMessage.address.numberMode = dataInputStream.read();
            cdmaSmsMessage.address.numberType = dataInputStream.read();
            cdmaSmsMessage.address.numberPlan = dataInputStream.read();
            byte b = (byte) dataInputStream.read();
            for (int i = 0; i < b; i++) {
                cdmaSmsMessage.address.digits.add(Byte.valueOf(dataInputStream.readByte()));
            }
            cdmaSmsMessage.subAddress.subaddressType = dataInputStream.read();
            CdmaSmsSubaddress cdmaSmsSubaddress = cdmaSmsMessage.subAddress;
            if (((byte) dataInputStream.read()) != 1) {
                z = false;
            }
            cdmaSmsSubaddress.odd = z;
            byte b2 = (byte) dataInputStream.read();
            for (int i2 = 0; i2 < b2; i2++) {
                cdmaSmsMessage.subAddress.digits.add(Byte.valueOf(dataInputStream.readByte()));
            }
            int i3 = dataInputStream.read();
            for (int i4 = 0; i4 < i3; i4++) {
                cdmaSmsMessage.bearerData.add(Byte.valueOf(dataInputStream.readByte()));
            }
        } catch (IOException unused) {
        }
        return cdmaSmsMessage;
    }

    public static android.hardware.radio.messaging.CdmaSmsMessage convertToHalCdmaSmsMessageAidl(byte[] bArr) {
        android.hardware.radio.messaging.CdmaSmsMessage cdmaSmsMessage = new android.hardware.radio.messaging.CdmaSmsMessage();
        cdmaSmsMessage.address = new CdmaSmsAddress();
        cdmaSmsMessage.subAddress = new android.hardware.radio.messaging.CdmaSmsSubaddress();
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        try {
            cdmaSmsMessage.teleserviceId = dataInputStream.readInt();
            boolean z = true;
            cdmaSmsMessage.isServicePresent = ((byte) dataInputStream.readInt()) == 1;
            cdmaSmsMessage.serviceCategory = dataInputStream.readInt();
            cdmaSmsMessage.address.digitMode = dataInputStream.read();
            cdmaSmsMessage.address.isNumberModeDataNetwork = dataInputStream.read() == 1;
            cdmaSmsMessage.address.numberType = dataInputStream.read();
            cdmaSmsMessage.address.numberPlan = dataInputStream.read();
            int i = (byte) dataInputStream.read();
            byte[] bArr2 = new byte[i];
            for (int i2 = 0; i2 < i; i2++) {
                bArr2[i2] = dataInputStream.readByte();
            }
            cdmaSmsMessage.address.digits = bArr2;
            cdmaSmsMessage.subAddress.subaddressType = dataInputStream.read();
            android.hardware.radio.messaging.CdmaSmsSubaddress cdmaSmsSubaddress = cdmaSmsMessage.subAddress;
            if (((byte) dataInputStream.read()) != 1) {
                z = false;
            }
            cdmaSmsSubaddress.odd = z;
            int i3 = (byte) dataInputStream.read();
            byte[] bArr3 = new byte[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                bArr3[i4] = dataInputStream.readByte();
            }
            cdmaSmsMessage.subAddress.digits = bArr3;
            int i5 = dataInputStream.read();
            byte[] bArr4 = new byte[i5];
            for (int i6 = 0; i6 < i5; i6++) {
                bArr4[i6] = dataInputStream.readByte();
            }
            cdmaSmsMessage.bearerData = bArr4;
        } catch (IOException unused) {
        }
        return cdmaSmsMessage;
    }

    public static SmsMessage convertHalCdmaSmsMessage(CdmaSmsMessage cdmaSmsMessage) {
        SmsEnvelope smsEnvelope = new SmsEnvelope();
        com.android.internal.telephony.cdma.sms.CdmaSmsAddress cdmaSmsAddress = new com.android.internal.telephony.cdma.sms.CdmaSmsAddress();
        com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress cdmaSmsSubaddress = new com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress();
        int i = cdmaSmsMessage.teleserviceId;
        smsEnvelope.teleService = i;
        if (cdmaSmsMessage.isServicePresent) {
            smsEnvelope.messageType = 1;
        } else if (i == 0) {
            smsEnvelope.messageType = 2;
        } else {
            smsEnvelope.messageType = 0;
        }
        smsEnvelope.serviceCategory = cdmaSmsMessage.serviceCategory;
        android.hardware.radio.V1_0.CdmaSmsAddress cdmaSmsAddress2 = cdmaSmsMessage.address;
        int i2 = cdmaSmsAddress2.digitMode;
        cdmaSmsAddress.digitMode = (byte) (i2 & 255);
        cdmaSmsAddress.numberMode = (byte) (cdmaSmsAddress2.numberMode & 255);
        cdmaSmsAddress.ton = cdmaSmsAddress2.numberType;
        cdmaSmsAddress.numberPlan = (byte) (cdmaSmsAddress2.numberPlan & 255);
        int size = (byte) cdmaSmsAddress2.digits.size();
        cdmaSmsAddress.numberOfDigits = size;
        byte[] bArr = new byte[size];
        for (int i3 = 0; i3 < size; i3++) {
            byte bByteValue = ((Byte) cdmaSmsMessage.address.digits.get(i3)).byteValue();
            bArr[i3] = bByteValue;
            if (i2 == 0) {
                bArr[i3] = SmsMessage.convertDtmfToAscii(bByteValue);
            }
        }
        cdmaSmsAddress.origBytes = bArr;
        CdmaSmsSubaddress cdmaSmsSubaddress2 = cdmaSmsMessage.subAddress;
        cdmaSmsSubaddress.type = cdmaSmsSubaddress2.subaddressType;
        cdmaSmsSubaddress.odd = cdmaSmsSubaddress2.odd ? (byte) 1 : (byte) 0;
        int size2 = (byte) cdmaSmsSubaddress2.digits.size();
        if (size2 < 0) {
            size2 = 0;
        }
        byte[] bArr2 = new byte[size2];
        for (int i4 = 0; i4 < size2; i4++) {
            bArr2[i4] = ((Byte) cdmaSmsMessage.subAddress.digits.get(i4)).byteValue();
        }
        cdmaSmsSubaddress.origBytes = bArr2;
        int size3 = cdmaSmsMessage.bearerData.size();
        if (size3 < 0) {
            size3 = 0;
        }
        byte[] bArr3 = new byte[size3];
        for (int i5 = 0; i5 < size3; i5++) {
            bArr3[i5] = ((Byte) cdmaSmsMessage.bearerData.get(i5)).byteValue();
        }
        smsEnvelope.bearerData = bArr3;
        smsEnvelope.origAddress = cdmaSmsAddress;
        smsEnvelope.origSubaddress = cdmaSmsSubaddress;
        return new SmsMessage(cdmaSmsAddress, smsEnvelope);
    }

    public static SmsMessage convertHalCdmaSmsMessage(android.hardware.radio.messaging.CdmaSmsMessage cdmaSmsMessage) {
        SmsEnvelope smsEnvelope = new SmsEnvelope();
        com.android.internal.telephony.cdma.sms.CdmaSmsAddress cdmaSmsAddress = new com.android.internal.telephony.cdma.sms.CdmaSmsAddress();
        com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress cdmaSmsSubaddress = new com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress();
        CdmaSmsAddress cdmaSmsAddress2 = cdmaSmsMessage.address;
        int i = cdmaSmsAddress2.digitMode;
        cdmaSmsAddress.digitMode = (byte) (i & 255);
        cdmaSmsAddress.numberMode = (byte) ((cdmaSmsAddress2.isNumberModeDataNetwork ? 1 : 0) & 255);
        cdmaSmsAddress.ton = cdmaSmsAddress2.numberType;
        cdmaSmsAddress.numberPlan = (byte) (cdmaSmsAddress2.numberPlan & 255);
        byte[] bArr = cdmaSmsAddress2.digits;
        cdmaSmsAddress.numberOfDigits = bArr.length;
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            byte b = cdmaSmsMessage.address.digits[i2];
            bArr2[i2] = b;
            if (i == 0) {
                bArr2[i2] = SmsMessage.convertDtmfToAscii(b);
            }
        }
        cdmaSmsAddress.origBytes = bArr2;
        android.hardware.radio.messaging.CdmaSmsSubaddress cdmaSmsSubaddress2 = cdmaSmsMessage.subAddress;
        cdmaSmsSubaddress.type = cdmaSmsSubaddress2.subaddressType;
        cdmaSmsSubaddress.odd = cdmaSmsSubaddress2.odd ? (byte) 1 : (byte) 0;
        cdmaSmsSubaddress.origBytes = cdmaSmsSubaddress2.digits;
        int i3 = cdmaSmsMessage.teleserviceId;
        smsEnvelope.teleService = i3;
        if (cdmaSmsMessage.isServicePresent) {
            smsEnvelope.messageType = 1;
        } else if (i3 == 0) {
            smsEnvelope.messageType = 2;
        } else {
            smsEnvelope.messageType = 0;
        }
        smsEnvelope.serviceCategory = cdmaSmsMessage.serviceCategory;
        smsEnvelope.bearerData = cdmaSmsMessage.bearerData;
        smsEnvelope.origAddress = cdmaSmsAddress;
        smsEnvelope.origSubaddress = cdmaSmsSubaddress;
        return new SmsMessage(cdmaSmsAddress, smsEnvelope);
    }

    public static DataProfileInfo convertToHalDataProfile14(DataProfile dataProfile) {
        DataProfileInfo dataProfileInfo = new DataProfileInfo();
        dataProfileInfo.apn = dataProfile.getApn();
        dataProfileInfo.protocol = dataProfile.getProtocolType();
        dataProfileInfo.roamingProtocol = dataProfile.getRoamingProtocolType();
        dataProfileInfo.authType = dataProfile.getAuthType();
        dataProfileInfo.user = TextUtils.emptyIfNull(dataProfile.getUserName());
        dataProfileInfo.password = TextUtils.emptyIfNull(dataProfile.getPassword());
        dataProfileInfo.type = dataProfile.getType();
        dataProfileInfo.maxConnsTime = dataProfile.getMaxConnectionsTime();
        dataProfileInfo.maxConns = dataProfile.getMaxConnections();
        dataProfileInfo.waitTime = dataProfile.getWaitTime();
        dataProfileInfo.enabled = dataProfile.isEnabled();
        dataProfileInfo.supportedApnTypesBitmap = dataProfile.getSupportedApnTypesBitmask();
        dataProfileInfo.bearerBitmap = ServiceState.convertNetworkTypeBitmaskToBearerBitmask(dataProfile.getBearerBitmask()) << 1;
        dataProfileInfo.mtu = dataProfile.getMtuV4();
        dataProfileInfo.persistent = dataProfile.isPersistent();
        dataProfileInfo.preferred = dataProfile.isPreferred();
        dataProfileInfo.profileId = dataProfileInfo.persistent ? dataProfile.getProfileId() : -1;
        return dataProfileInfo;
    }

    public static android.hardware.radio.V1_5.DataProfileInfo convertToHalDataProfile15(DataProfile dataProfile) {
        android.hardware.radio.V1_5.DataProfileInfo dataProfileInfo = new android.hardware.radio.V1_5.DataProfileInfo();
        dataProfileInfo.apn = dataProfile.getApn();
        dataProfileInfo.protocol = dataProfile.getProtocolType();
        dataProfileInfo.roamingProtocol = dataProfile.getRoamingProtocolType();
        dataProfileInfo.authType = dataProfile.getAuthType();
        dataProfileInfo.user = TextUtils.emptyIfNull(dataProfile.getUserName());
        dataProfileInfo.password = TextUtils.emptyIfNull(dataProfile.getPassword());
        dataProfileInfo.type = dataProfile.getType();
        dataProfileInfo.maxConnsTime = dataProfile.getMaxConnectionsTime();
        dataProfileInfo.maxConns = dataProfile.getMaxConnections();
        dataProfileInfo.waitTime = dataProfile.getWaitTime();
        dataProfileInfo.enabled = dataProfile.isEnabled();
        dataProfileInfo.supportedApnTypesBitmap = dataProfile.getSupportedApnTypesBitmask();
        dataProfileInfo.bearerBitmap = ServiceState.convertNetworkTypeBitmaskToBearerBitmask(dataProfile.getBearerBitmask()) << 1;
        dataProfileInfo.mtuV4 = dataProfile.getMtuV4();
        dataProfileInfo.mtuV6 = dataProfile.getMtuV6();
        dataProfileInfo.persistent = dataProfile.isPersistent();
        dataProfileInfo.preferred = dataProfile.isPreferred();
        dataProfileInfo.profileId = dataProfileInfo.persistent ? dataProfile.getProfileId() : -1;
        return dataProfileInfo;
    }

    public static android.hardware.radio.data.DataProfileInfo convertToHalDataProfile(DataProfile dataProfile, HalVersion halVersion) {
        if (dataProfile == null) {
            return null;
        }
        android.hardware.radio.data.DataProfileInfo dataProfileInfo = new android.hardware.radio.data.DataProfileInfo();
        dataProfileInfo.apn = dataProfile.getApn();
        dataProfileInfo.protocol = dataProfile.getProtocolType();
        dataProfileInfo.roamingProtocol = dataProfile.getRoamingProtocolType();
        dataProfileInfo.authType = dataProfile.getAuthType();
        dataProfileInfo.user = convertNullToEmptyString(dataProfile.getUserName());
        dataProfileInfo.password = convertNullToEmptyString(dataProfile.getPassword());
        dataProfileInfo.type = dataProfile.getType();
        dataProfileInfo.maxConnsTime = dataProfile.getMaxConnectionsTime();
        dataProfileInfo.maxConns = dataProfile.getMaxConnections();
        dataProfileInfo.waitTime = dataProfile.getWaitTime();
        dataProfileInfo.enabled = dataProfile.isEnabled();
        dataProfileInfo.supportedApnTypesBitmap = dataProfile.getSupportedApnTypesBitmask();
        dataProfileInfo.bearerBitmap = ServiceState.convertNetworkTypeBitmaskToBearerBitmask(dataProfile.getBearerBitmask()) << 1;
        dataProfileInfo.mtuV4 = dataProfile.getMtuV4();
        dataProfileInfo.mtuV6 = dataProfile.getMtuV6();
        dataProfileInfo.persistent = dataProfile.isPersistent();
        dataProfileInfo.preferred = dataProfile.isPreferred();
        dataProfileInfo.alwaysOn = false;
        dataProfileInfo.infrastructureBitmap = 1;
        if (dataProfile.getApnSetting() != null) {
            dataProfileInfo.alwaysOn = dataProfile.getApnSetting().isAlwaysOn();
            dataProfileInfo.infrastructureBitmap = dataProfile.getApnSetting().getInfrastructureBitmask();
        }
        dataProfileInfo.trafficDescriptor = convertToHalTrafficDescriptorAidl(dataProfile.getTrafficDescriptor(), halVersion);
        dataProfileInfo.profileId = dataProfileInfo.persistent ? dataProfile.getProfileId() : -1;
        return dataProfileInfo;
    }

    public static DataProfile convertToDataProfile(android.hardware.radio.data.DataProfileInfo dataProfileInfo) {
        TrafficDescriptor trafficDescriptorConvertHalTrafficDescriptor;
        ApnSetting apnSettingBuild = new ApnSetting.Builder().setEntryName(dataProfileInfo.apn).setApnName(dataProfileInfo.apn).setApnTypeBitmask(dataProfileInfo.supportedApnTypesBitmap).setAuthType(dataProfileInfo.authType).setMaxConnsTime(dataProfileInfo.maxConnsTime).setMaxConns(dataProfileInfo.maxConns).setWaitTime(dataProfileInfo.waitTime).setCarrierEnabled(dataProfileInfo.enabled).setModemCognitive(dataProfileInfo.persistent).setMtuV4(dataProfileInfo.mtuV4).setMtuV6(dataProfileInfo.mtuV6).setNetworkTypeBitmask(ServiceState.convertBearerBitmaskToNetworkTypeBitmask(dataProfileInfo.bearerBitmap) >> 1).setProfileId(dataProfileInfo.profileId).setPassword(dataProfileInfo.password).setProtocol(dataProfileInfo.protocol).setRoamingProtocol(dataProfileInfo.roamingProtocol).setUser(dataProfileInfo.user).setAlwaysOn(dataProfileInfo.alwaysOn).setInfrastructureBitmask(dataProfileInfo.infrastructureBitmap).build();
        try {
            trafficDescriptorConvertHalTrafficDescriptor = convertHalTrafficDescriptor(dataProfileInfo.trafficDescriptor);
        } catch (IllegalArgumentException e) {
            loge("convertToDataProfile: Failed to convert traffic descriptor. e=" + e);
            trafficDescriptorConvertHalTrafficDescriptor = null;
        }
        return new DataProfile.Builder().setType(dataProfileInfo.type).setPreferred(dataProfileInfo.preferred).setTrafficDescriptor(trafficDescriptorConvertHalTrafficDescriptor).setApnSetting(apnSettingBuild).build();
    }

    public static OptionalSliceInfo convertToHalSliceInfo(NetworkSliceInfo networkSliceInfo) {
        OptionalSliceInfo optionalSliceInfo = new OptionalSliceInfo();
        if (networkSliceInfo == null) {
            return optionalSliceInfo;
        }
        SliceInfo sliceInfo = new SliceInfo();
        sliceInfo.sst = (byte) networkSliceInfo.getSliceServiceType();
        sliceInfo.mappedHplmnSst = (byte) networkSliceInfo.getMappedHplmnSliceServiceType();
        sliceInfo.sliceDifferentiator = networkSliceInfo.getSliceDifferentiator();
        sliceInfo.mappedHplmnSD = networkSliceInfo.getMappedHplmnSliceDifferentiator();
        optionalSliceInfo.value(sliceInfo);
        return optionalSliceInfo;
    }

    public static android.hardware.radio.data.SliceInfo convertToHalSliceInfoAidl(NetworkSliceInfo networkSliceInfo) {
        if (networkSliceInfo == null) {
            return null;
        }
        android.hardware.radio.data.SliceInfo sliceInfo = new android.hardware.radio.data.SliceInfo();
        sliceInfo.sliceServiceType = (byte) networkSliceInfo.getSliceServiceType();
        sliceInfo.mappedHplmnSst = (byte) networkSliceInfo.getMappedHplmnSliceServiceType();
        sliceInfo.sliceDifferentiator = networkSliceInfo.getSliceDifferentiator();
        sliceInfo.mappedHplmnSd = networkSliceInfo.getMappedHplmnSliceDifferentiator();
        return sliceInfo;
    }

    public static OptionalTrafficDescriptor convertToHalTrafficDescriptor(TrafficDescriptor trafficDescriptor) {
        OptionalTrafficDescriptor optionalTrafficDescriptor = new OptionalTrafficDescriptor();
        if (trafficDescriptor == null) {
            return optionalTrafficDescriptor;
        }
        android.hardware.radio.V1_6.TrafficDescriptor trafficDescriptor2 = new android.hardware.radio.V1_6.TrafficDescriptor();
        OptionalDnn optionalDnn = new OptionalDnn();
        if (trafficDescriptor.getDataNetworkName() != null) {
            optionalDnn.value(trafficDescriptor.getDataNetworkName());
        }
        trafficDescriptor2.dnn = optionalDnn;
        OptionalOsAppId optionalOsAppId = new OptionalOsAppId();
        if (trafficDescriptor.getOsAppId() != null) {
            OsAppId osAppId = new OsAppId();
            osAppId.osAppId = primitiveArrayToArrayList(trafficDescriptor.getOsAppId());
            optionalOsAppId.value(osAppId);
        }
        trafficDescriptor2.osAppId = optionalOsAppId;
        optionalTrafficDescriptor.value(trafficDescriptor2);
        return optionalTrafficDescriptor;
    }

    public static android.hardware.radio.data.TrafficDescriptor convertToHalTrafficDescriptorAidl(TrafficDescriptor trafficDescriptor, HalVersion halVersion) {
        if (trafficDescriptor == null) {
            return new android.hardware.radio.data.TrafficDescriptor();
        }
        android.hardware.radio.data.TrafficDescriptor trafficDescriptor2 = new android.hardware.radio.data.TrafficDescriptor();
        trafficDescriptor2.dnn = trafficDescriptor.getDataNetworkName();
        if (trafficDescriptor.getOsAppId() == null) {
            trafficDescriptor2.osAppId = null;
        } else {
            android.hardware.radio.data.OsAppId osAppId = new android.hardware.radio.data.OsAppId();
            osAppId.osAppId = trafficDescriptor.getOsAppId();
            trafficDescriptor2.osAppId = osAppId;
        }
        if (halVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_2_4)) {
            trafficDescriptor2.connectionCapability = (byte) (trafficDescriptor.getConnectionCapability() & 255);
        }
        return trafficDescriptor2;
    }

    public static ArrayList<LinkAddress> convertToHalLinkProperties15(LinkProperties linkProperties) {
        ArrayList<LinkAddress> arrayList = new ArrayList<>();
        if (linkProperties != null) {
            for (android.net.LinkAddress linkAddress : linkProperties.getAllLinkAddresses()) {
                LinkAddress linkAddress2 = new LinkAddress();
                linkAddress2.address = linkAddress.getAddress().getHostAddress();
                linkAddress2.properties = linkAddress.getFlags();
                linkAddress2.deprecationTime = linkAddress.getDeprecationTime();
                linkAddress2.expirationTime = linkAddress.getExpirationTime();
                arrayList.add(linkAddress2);
            }
        }
        return arrayList;
    }

    public static android.hardware.radio.data.LinkAddress[] convertToHalLinkProperties(LinkProperties linkProperties) {
        if (linkProperties == null) {
            return new android.hardware.radio.data.LinkAddress[0];
        }
        android.hardware.radio.data.LinkAddress[] linkAddressArr = new android.hardware.radio.data.LinkAddress[linkProperties.getAllLinkAddresses().size()];
        for (int i = 0; i < linkProperties.getAllLinkAddresses().size(); i++) {
            android.net.LinkAddress linkAddress = (android.net.LinkAddress) linkProperties.getAllLinkAddresses().get(i);
            android.hardware.radio.data.LinkAddress linkAddress2 = new android.hardware.radio.data.LinkAddress();
            linkAddress2.address = linkAddress.getAddress().getHostAddress();
            linkAddress2.addressProperties = linkAddress.getFlags();
            linkAddress2.deprecationTime = linkAddress.getDeprecationTime();
            linkAddress2.expirationTime = linkAddress.getExpirationTime();
            linkAddressArr[i] = linkAddress2;
        }
        return linkAddressArr;
    }

    public static RadioAccessSpecifier convertHalRadioAccessSpecifier(android.hardware.radio.V1_5.RadioAccessSpecifier radioAccessSpecifier) {
        if (radioAccessSpecifier == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        byte discriminator = radioAccessSpecifier.bands.getDiscriminator();
        if (discriminator == 0) {
            arrayList = radioAccessSpecifier.bands.geranBands();
        } else if (discriminator == 1) {
            arrayList = radioAccessSpecifier.bands.utranBands();
        } else if (discriminator == 2) {
            arrayList = radioAccessSpecifier.bands.eutranBands();
        } else if (discriminator == 3) {
            arrayList = radioAccessSpecifier.bands.ngranBands();
        }
        return new RadioAccessSpecifier(convertHalRadioAccessNetworks(radioAccessSpecifier.radioAccessNetwork), arrayList.stream().mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray(), radioAccessSpecifier.channels.stream().mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray());
    }

    public static RadioAccessSpecifier convertHalRadioAccessSpecifier(android.hardware.radio.network.RadioAccessSpecifier radioAccessSpecifier) {
        int[] geranBands = null;
        if (radioAccessSpecifier == null) {
            return null;
        }
        int tag = radioAccessSpecifier.bands.getTag();
        int i = 1;
        if (tag != 1) {
            i = 2;
            if (tag != 2) {
                i = 3;
                if (tag == 3) {
                    geranBands = radioAccessSpecifier.bands.getEutranBands();
                } else if (tag != 4) {
                    i = 0;
                } else {
                    geranBands = radioAccessSpecifier.bands.getNgranBands();
                    i = 6;
                }
            } else {
                geranBands = radioAccessSpecifier.bands.getUtranBands();
            }
        } else {
            geranBands = radioAccessSpecifier.bands.getGeranBands();
        }
        if (radioAccessSpecifier.accessNetwork != i) {
            AnomalyReporter.reportAnomaly(ANOMALY_HAL_SPECIFIER_HAS_BAD_ACCESS_NETWORK_VALUES_UUID, ANOMALY_HAL_SPECIFIER_HAS_BAD_ACCESS_NETWORK_VALUES_MSG);
        }
        return new RadioAccessSpecifier(i, geranBands, sanitizeChannels(radioAccessSpecifier.channels));
    }

    public static android.hardware.radio.V1_1.RadioAccessSpecifier convertToHalRadioAccessSpecifier11(RadioAccessSpecifier radioAccessSpecifier) {
        android.hardware.radio.V1_1.RadioAccessSpecifier radioAccessSpecifier2 = new android.hardware.radio.V1_1.RadioAccessSpecifier();
        radioAccessSpecifier2.radioAccessNetwork = radioAccessSpecifier.getRadioAccessNetwork();
        ArrayList arrayList = new ArrayList();
        if (radioAccessSpecifier.getBands() != null) {
            for (int i : radioAccessSpecifier.getBands()) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        int radioAccessNetwork = radioAccessSpecifier.getRadioAccessNetwork();
        if (radioAccessNetwork == 1) {
            radioAccessSpecifier2.geranBands = arrayList;
        } else if (radioAccessNetwork == 2) {
            radioAccessSpecifier2.utranBands = arrayList;
        } else {
            if (radioAccessNetwork != 3) {
                return null;
            }
            radioAccessSpecifier2.eutranBands = arrayList;
        }
        if (radioAccessSpecifier.getChannels() != null) {
            for (int i2 : radioAccessSpecifier.getChannels()) {
                radioAccessSpecifier2.channels.add(Integer.valueOf(i2));
            }
        }
        return radioAccessSpecifier2;
    }

    public static android.hardware.radio.V1_5.RadioAccessSpecifier convertToHalRadioAccessSpecifier15(RadioAccessSpecifier radioAccessSpecifier) {
        android.hardware.radio.V1_5.RadioAccessSpecifier radioAccessSpecifier2 = new android.hardware.radio.V1_5.RadioAccessSpecifier();
        android.hardware.radio.V1_5.RadioAccessSpecifier.Bands bands = new android.hardware.radio.V1_5.RadioAccessSpecifier.Bands();
        radioAccessSpecifier2.radioAccessNetwork = convertToHalRadioAccessNetworks(radioAccessSpecifier.getRadioAccessNetwork());
        ArrayList arrayList = new ArrayList();
        if (radioAccessSpecifier.getBands() != null) {
            for (int i : radioAccessSpecifier.getBands()) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        int radioAccessNetwork = radioAccessSpecifier.getRadioAccessNetwork();
        if (radioAccessNetwork == 1) {
            bands.geranBands(arrayList);
        } else if (radioAccessNetwork == 2) {
            bands.utranBands(arrayList);
        } else if (radioAccessNetwork == 3) {
            bands.eutranBands(arrayList);
        } else {
            if (radioAccessNetwork != 6) {
                return null;
            }
            bands.ngranBands(arrayList);
        }
        radioAccessSpecifier2.bands = bands;
        if (radioAccessSpecifier.getChannels() != null) {
            for (int i2 : radioAccessSpecifier.getChannels()) {
                radioAccessSpecifier2.channels.add(Integer.valueOf(i2));
            }
        }
        return radioAccessSpecifier2;
    }

    public static android.hardware.radio.network.RadioAccessSpecifier convertToHalRadioAccessSpecifierAidl(RadioAccessSpecifier radioAccessSpecifier) {
        int[] iArr;
        int[] iArr2;
        android.hardware.radio.network.RadioAccessSpecifier radioAccessSpecifier2 = new android.hardware.radio.network.RadioAccessSpecifier();
        RadioAccessSpecifierBands radioAccessSpecifierBands = new RadioAccessSpecifierBands();
        radioAccessSpecifier2.accessNetwork = convertToHalAccessNetworkAidl(radioAccessSpecifier.getRadioAccessNetwork());
        if (radioAccessSpecifier.getBands() != null) {
            iArr = new int[radioAccessSpecifier.getBands().length];
            for (int i = 0; i < radioAccessSpecifier.getBands().length; i++) {
                iArr[i] = radioAccessSpecifier.getBands()[i];
            }
        } else {
            iArr = new int[0];
        }
        int radioAccessNetwork = radioAccessSpecifier.getRadioAccessNetwork();
        if (radioAccessNetwork == 1) {
            radioAccessSpecifierBands.setGeranBands(iArr);
        } else if (radioAccessNetwork == 2) {
            radioAccessSpecifierBands.setUtranBands(iArr);
        } else if (radioAccessNetwork == 3) {
            radioAccessSpecifierBands.setEutranBands(iArr);
        } else {
            if (radioAccessNetwork != 6) {
                return null;
            }
            radioAccessSpecifierBands.setNgranBands(iArr);
        }
        radioAccessSpecifier2.bands = radioAccessSpecifierBands;
        if (radioAccessSpecifier.getChannels() != null) {
            iArr2 = new int[radioAccessSpecifier.getChannels().length];
            for (int i2 = 0; i2 < radioAccessSpecifier.getChannels().length; i2++) {
                iArr2[i2] = radioAccessSpecifier.getChannels()[i2];
            }
        } else {
            iArr2 = new int[0];
        }
        radioAccessSpecifier2.channels = iArr2;
        return radioAccessSpecifier2;
    }

    public static String convertToCensoredTerminalResponse(String str) {
        try {
            byte[] bArrHexStringToBytes = IccUtils.hexStringToBytes(str);
            if (bArrHexStringToBytes != null) {
                int valueIndex = 0;
                for (ComprehensionTlv comprehensionTlv : ComprehensionTlv.decodeMany(bArrHexStringToBytes, 0)) {
                    if (ComprehensionTlvTag.TEXT_STRING.value() == comprehensionTlv.getTag()) {
                        byte[] bArrCopyOfRange = Arrays.copyOfRange(comprehensionTlv.getRawValue(), valueIndex, comprehensionTlv.getValueIndex() + comprehensionTlv.getLength());
                        Locale locale = Locale.ROOT;
                        str = str.toLowerCase(locale).replace(IccUtils.bytesToHexString(bArrCopyOfRange).toLowerCase(locale), "********");
                    }
                    valueIndex = comprehensionTlv.getValueIndex() + comprehensionTlv.getLength();
                }
            }
            return str;
        } catch (Exception unused) {
            return null;
        }
    }

    private static int[] sanitizeChannels(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return new int[0];
        }
        int i = 0;
        for (int i2 : iArr) {
            if (i2 >= 0) {
                i++;
            }
        }
        if (i == iArr.length) {
            return iArr;
        }
        AnomalyReporter.reportAnomaly(ANOMALY_HAL_SPECIFIER_USES_PADDING_VALUES_UUID, ANOMALY_HAL_SPECIFIER_USES_PADDING_VALUES_MSG);
        if (i == 0) {
            return new int[0];
        }
        int[] iArr2 = new int[i];
        int i3 = 0;
        for (int i4 : iArr) {
            if (i4 >= 0) {
                iArr2[i3] = i4;
                i3++;
            }
        }
        return iArr2;
    }

    public static SimApdu convertToHalSimApdu(int i, int i2, int i3, int i4, int i5, int i6, String str) {
        SimApdu simApdu = new SimApdu();
        simApdu.sessionId = i;
        simApdu.cla = i2;
        simApdu.instruction = i3;
        simApdu.p1 = i4;
        simApdu.p2 = i5;
        simApdu.p3 = i6;
        simApdu.data = convertNullToEmptyString(str);
        return simApdu;
    }

    public static android.hardware.radio.sim.SimApdu convertToHalSimApduAidl(int i, int i2, int i3, int i4, int i5, int i6, String str, boolean z, HalVersion halVersion) {
        android.hardware.radio.sim.SimApdu simApdu = new android.hardware.radio.sim.SimApdu();
        simApdu.sessionId = i;
        simApdu.cla = i2;
        simApdu.instruction = i3;
        simApdu.p1 = i4;
        simApdu.p2 = i5;
        simApdu.p3 = i6;
        simApdu.data = convertNullToEmptyString(str);
        if (halVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_2_1)) {
            simApdu.isEs10 = z;
        }
        return simApdu;
    }

    public static ArrayList<Carrier> convertToHalCarrierRestrictionList(List<CarrierIdentifier> list) {
        int i;
        String gid2;
        ArrayList<Carrier> arrayList = new ArrayList<>();
        for (CarrierIdentifier carrierIdentifier : list) {
            Carrier carrier = new Carrier();
            carrier.mcc = convertNullToEmptyString(carrierIdentifier.getMcc());
            carrier.mnc = convertNullToEmptyString(carrierIdentifier.getMnc());
            if (!TextUtils.isEmpty(carrierIdentifier.getSpn())) {
                gid2 = carrierIdentifier.getSpn();
                i = 1;
            } else if (!TextUtils.isEmpty(carrierIdentifier.getImsi())) {
                gid2 = carrierIdentifier.getImsi();
                i = 2;
            } else if (!TextUtils.isEmpty(carrierIdentifier.getGid1())) {
                gid2 = carrierIdentifier.getGid1();
                i = 3;
            } else if (TextUtils.isEmpty(carrierIdentifier.getGid2())) {
                i = 0;
                gid2 = null;
            } else {
                gid2 = carrierIdentifier.getGid2();
                i = 4;
            }
            carrier.matchType = i;
            carrier.matchData = convertNullToEmptyString(gid2);
            arrayList.add(carrier);
        }
        return arrayList;
    }

    public static android.hardware.radio.sim.Carrier[] convertToHalCarrierRestrictionListAidl(List<CarrierIdentifier> list) {
        String gid2;
        int i;
        android.hardware.radio.sim.Carrier[] carrierArr = new android.hardware.radio.sim.Carrier[list.size()];
        for (int i2 = 0; i2 < list.size(); i2++) {
            CarrierIdentifier carrierIdentifier = list.get(i2);
            android.hardware.radio.sim.Carrier carrier = new android.hardware.radio.sim.Carrier();
            carrier.mcc = convertNullToEmptyString(carrierIdentifier.getMcc());
            carrier.mnc = convertNullToEmptyString(carrierIdentifier.getMnc());
            if (!TextUtils.isEmpty(carrierIdentifier.getSpn())) {
                gid2 = carrierIdentifier.getSpn();
                i = 1;
            } else if (!TextUtils.isEmpty(carrierIdentifier.getImsi())) {
                gid2 = carrierIdentifier.getImsi();
                i = 2;
            } else if (!TextUtils.isEmpty(carrierIdentifier.getGid1())) {
                gid2 = carrierIdentifier.getGid1();
                i = 3;
            } else if (TextUtils.isEmpty(carrierIdentifier.getGid2())) {
                gid2 = null;
                i = 0;
            } else {
                gid2 = carrierIdentifier.getGid2();
                i = 4;
            }
            carrier.matchType = i;
            carrier.matchData = convertNullToEmptyString(gid2);
            carrierArr[i2] = carrier;
        }
        return carrierArr;
    }

    public static CarrierInfo[] convertToHalCarrierInfoListAidl(List<CarrierIdentifier> list) {
        CarrierInfo[] carrierInfoArr = new CarrierInfo[list.size()];
        for (int i = 0; i < list.size(); i++) {
            CarrierIdentifier carrierIdentifier = list.get(i);
            CarrierInfo carrierInfo = new CarrierInfo();
            carrierInfo.mcc = convertNullToEmptyString(carrierIdentifier.getMcc());
            carrierInfo.mnc = convertNullToEmptyString(carrierIdentifier.getMnc());
            carrierInfo.spn = carrierIdentifier.getSpn();
            carrierInfo.imsiPrefix = carrierIdentifier.getImsi();
            carrierInfo.gid1 = carrierIdentifier.getGid1();
            carrierInfo.gid2 = carrierIdentifier.getGid2();
            carrierInfoArr[i] = carrierInfo;
        }
        return carrierInfoArr;
    }

    public static Dial convertToHalDial(String str, int i, UUSInfo uUSInfo) {
        Dial dial = new Dial();
        dial.address = convertNullToEmptyString(str);
        dial.clir = i;
        if (uUSInfo != null) {
            UusInfo uusInfo = new UusInfo();
            uusInfo.uusType = uUSInfo.getType();
            uusInfo.uusDcs = uUSInfo.getDcs();
            uusInfo.uusData = new String(uUSInfo.getUserData());
            dial.uusInfo.add(uusInfo);
        }
        return dial;
    }

    public static android.hardware.radio.voice.Dial convertToHalDialAidl(String str, int i, UUSInfo uUSInfo) {
        android.hardware.radio.voice.Dial dial = new android.hardware.radio.voice.Dial();
        dial.address = convertNullToEmptyString(str);
        dial.clir = i;
        if (uUSInfo != null) {
            android.hardware.radio.voice.UusInfo uusInfo = new android.hardware.radio.voice.UusInfo();
            uusInfo.uusType = uUSInfo.getType();
            uusInfo.uusDcs = uUSInfo.getDcs();
            uusInfo.uusData = new String(uUSInfo.getUserData());
            dial.uusInfo = new android.hardware.radio.voice.UusInfo[]{uusInfo};
            return dial;
        }
        dial.uusInfo = new android.hardware.radio.voice.UusInfo[0];
        return dial;
    }

    public static SignalThresholdInfo convertToHalSignalThresholdInfo(android.telephony.SignalThresholdInfo signalThresholdInfo) {
        SignalThresholdInfo signalThresholdInfo2 = new SignalThresholdInfo();
        signalThresholdInfo2.signalMeasurement = signalThresholdInfo.getSignalMeasurementType();
        signalThresholdInfo2.hysteresisMs = signalThresholdInfo.getHysteresisMs();
        signalThresholdInfo2.hysteresisDb = signalThresholdInfo.getHysteresisDb();
        signalThresholdInfo2.thresholds = primitiveArrayToArrayList(signalThresholdInfo.getThresholds());
        signalThresholdInfo2.isEnabled = signalThresholdInfo.isEnabled();
        return signalThresholdInfo2;
    }

    public static android.hardware.radio.network.SignalThresholdInfo convertToHalSignalThresholdInfoAidl(android.telephony.SignalThresholdInfo signalThresholdInfo) {
        android.hardware.radio.network.SignalThresholdInfo signalThresholdInfo2 = new android.hardware.radio.network.SignalThresholdInfo();
        signalThresholdInfo2.signalMeasurement = signalThresholdInfo.getSignalMeasurementType();
        signalThresholdInfo2.hysteresisMs = signalThresholdInfo.getHysteresisMs();
        signalThresholdInfo2.hysteresisDb = signalThresholdInfo.getHysteresisDb();
        signalThresholdInfo2.thresholds = signalThresholdInfo.getThresholds();
        signalThresholdInfo2.isEnabled = signalThresholdInfo.isEnabled();
        signalThresholdInfo2.ran = signalThresholdInfo.getRadioAccessNetworkType();
        return signalThresholdInfo2;
    }

    public static ArrayList<HardwareConfig> convertHalHardwareConfigList(ArrayList<android.hardware.radio.V1_0.HardwareConfig> arrayList) {
        HardwareConfig hardwareConfig;
        ArrayList<HardwareConfig> arrayList2 = new ArrayList<>(arrayList.size());
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            android.hardware.radio.V1_0.HardwareConfig hardwareConfig2 = arrayList.get(i);
            i++;
            android.hardware.radio.V1_0.HardwareConfig hardwareConfig3 = hardwareConfig2;
            int i2 = hardwareConfig3.type;
            if (i2 == 0) {
                HardwareConfig hardwareConfig4 = new HardwareConfig(i2);
                HardwareConfigModem hardwareConfigModem = (HardwareConfigModem) hardwareConfig3.modem.get(0);
                hardwareConfig4.assignModem(hardwareConfig3.uuid, hardwareConfig3.state, hardwareConfigModem.rilModel, hardwareConfigModem.rat, hardwareConfigModem.maxVoice, hardwareConfigModem.maxData, hardwareConfigModem.maxStandby);
                hardwareConfig = hardwareConfig4;
            } else if (i2 == 1) {
                hardwareConfig = new HardwareConfig(i2);
                hardwareConfig.assignSim(hardwareConfig3.uuid, hardwareConfig3.state, ((HardwareConfigSim) hardwareConfig3.sim.get(0)).modemUuid);
            } else {
                RIL$$ExternalSyntheticBUOutline0.m("RIL_REQUEST_GET_HARDWARE_CONFIG invalid hardware type:", i2);
                return null;
            }
            arrayList2.add(hardwareConfig);
        }
        return arrayList2;
    }

    public static ArrayList<HardwareConfig> convertHalHardwareConfigList(android.hardware.radio.modem.HardwareConfig[] hardwareConfigArr) {
        HardwareConfig hardwareConfig;
        ArrayList<HardwareConfig> arrayList = new ArrayList<>(hardwareConfigArr.length);
        for (android.hardware.radio.modem.HardwareConfig hardwareConfig2 : hardwareConfigArr) {
            int i = hardwareConfig2.type;
            if (i == 0) {
                HardwareConfig hardwareConfig3 = new HardwareConfig(i);
                android.hardware.radio.modem.HardwareConfigModem hardwareConfigModem = hardwareConfig2.modem[0];
                hardwareConfig3.assignModem(hardwareConfig2.uuid, hardwareConfig2.state, hardwareConfigModem.rilModel, hardwareConfigModem.rat, hardwareConfigModem.maxVoiceCalls, hardwareConfigModem.maxDataCalls, hardwareConfigModem.maxStandby);
                hardwareConfig = hardwareConfig3;
            } else if (i == 1) {
                hardwareConfig = new HardwareConfig(i);
                hardwareConfig.assignSim(hardwareConfig2.uuid, hardwareConfig2.state, hardwareConfig2.sim[0].modemUuid);
            } else {
                RIL$$ExternalSyntheticBUOutline0.m("RIL_REQUEST_GET_HARDWARE_CONFIG invalid hardware type:", i);
                return null;
            }
            arrayList.add(hardwareConfig);
        }
        return arrayList;
    }

    public static RadioCapability convertHalRadioCapability(android.hardware.radio.V1_0.RadioCapability radioCapability, RIL ril) {
        int i = radioCapability.session;
        int i2 = radioCapability.phase;
        int iConvertHalNetworkTypeBitMask = convertHalNetworkTypeBitMask(radioCapability.raf);
        String str = radioCapability.logicalModemUuid;
        int i3 = radioCapability.status;
        ril.riljLog("convertHalRadioCapability: session=" + i + ", phase=" + i2 + ", rat=" + iConvertHalNetworkTypeBitMask + ", logicModemUuid=" + str + ", status=" + i3 + ", rcRil.raf=" + radioCapability.raf);
        return new RadioCapability(ril.mPhoneId.intValue(), i, i2, iConvertHalNetworkTypeBitMask, str, i3);
    }

    public static RadioCapability convertHalRadioCapability(android.hardware.radio.modem.RadioCapability radioCapability, RIL ril) {
        int i = radioCapability.session;
        int i2 = radioCapability.phase;
        int iConvertHalNetworkTypeBitMask = convertHalNetworkTypeBitMask(radioCapability.raf);
        String str = radioCapability.logicalModemUuid;
        int i3 = radioCapability.status;
        ril.riljLog("convertHalRadioCapability: session=" + i + ", phase=" + i2 + ", rat=" + iConvertHalNetworkTypeBitMask + ", logicModemUuid=" + str + ", status=" + i3 + ", rcRil.raf=" + radioCapability.raf);
        return new RadioCapability(ril.mPhoneId.intValue(), i, i2, iConvertHalNetworkTypeBitMask, str, i3);
    }

    public static List<LinkCapacityEstimate> convertHalLinkCapacityEstimate(Object obj) {
        int i;
        int i2;
        ArrayList arrayList = new ArrayList();
        if (obj != null) {
            if (obj instanceof android.hardware.radio.V1_2.LinkCapacityEstimate) {
                android.hardware.radio.V1_2.LinkCapacityEstimate linkCapacityEstimate = (android.hardware.radio.V1_2.LinkCapacityEstimate) obj;
                arrayList.add(new LinkCapacityEstimate(2, linkCapacityEstimate.downlinkCapacityKbps, linkCapacityEstimate.uplinkCapacityKbps));
                return arrayList;
            }
            if (obj instanceof android.hardware.radio.V1_6.LinkCapacityEstimate) {
                android.hardware.radio.V1_6.LinkCapacityEstimate linkCapacityEstimate2 = (android.hardware.radio.V1_6.LinkCapacityEstimate) obj;
                int i3 = linkCapacityEstimate2.downlinkCapacityKbps;
                int i4 = linkCapacityEstimate2.uplinkCapacityKbps;
                if (i3 != -1 && (i2 = linkCapacityEstimate2.secondaryDownlinkCapacityKbps) != -1) {
                    i3 -= i2;
                }
                if (i4 != -1 && (i = linkCapacityEstimate2.secondaryUplinkCapacityKbps) != -1) {
                    i4 -= i;
                }
                arrayList.add(new LinkCapacityEstimate(0, i3, i4));
                arrayList.add(new LinkCapacityEstimate(1, linkCapacityEstimate2.secondaryDownlinkCapacityKbps, linkCapacityEstimate2.secondaryUplinkCapacityKbps));
            }
        }
        return arrayList;
    }

    public static List<LinkCapacityEstimate> convertHalLinkCapacityEstimate(android.hardware.radio.network.LinkCapacityEstimate linkCapacityEstimate) {
        int i;
        int i2;
        ArrayList arrayList = new ArrayList();
        int i3 = linkCapacityEstimate.downlinkCapacityKbps;
        int i4 = linkCapacityEstimate.uplinkCapacityKbps;
        if (i3 != -1 && (i2 = linkCapacityEstimate.secondaryDownlinkCapacityKbps) != -1) {
            i3 -= i2;
        }
        if (i4 != -1 && (i = linkCapacityEstimate.secondaryUplinkCapacityKbps) != -1) {
            i4 -= i;
        }
        arrayList.add(new LinkCapacityEstimate(0, i3, i4));
        arrayList.add(new LinkCapacityEstimate(1, linkCapacityEstimate.secondaryDownlinkCapacityKbps, linkCapacityEstimate.secondaryUplinkCapacityKbps));
        return arrayList;
    }

    public static ArrayList<CellInfo> convertHalCellInfoList(ArrayList<Object> arrayList) {
        ArrayList<CellInfo> arrayList2 = new ArrayList<>(arrayList.size());
        if (!arrayList.isEmpty()) {
            long jElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                arrayList2.add(convertHalCellInfo(obj, jElapsedRealtimeNanos));
            }
        }
        return arrayList2;
    }

    public static ArrayList<CellInfo> convertHalCellInfoList(android.hardware.radio.network.CellInfo[] cellInfoArr) {
        ArrayList<CellInfo> arrayList = new ArrayList<>(cellInfoArr.length);
        if (cellInfoArr.length != 0) {
            long jElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
            for (android.hardware.radio.network.CellInfo cellInfo : cellInfoArr) {
                arrayList.add(convertHalCellInfo(cellInfo, jElapsedRealtimeNanos));
            }
        }
        return arrayList;
    }

    private static CellInfo convertHalCellInfo(Object obj, long j) {
        int i;
        boolean z;
        CellIdentityGsm cellIdentityGsmConvertHalCellIdentityGsm;
        CellSignalStrengthGsm cellSignalStrengthGsmConvertHalGsmSignalStrength;
        CellIdentityWcdma cellIdentityWcdmaConvertHalCellIdentityWcdma;
        CellConfigLte cellConfigLteConvertHalWcdmaSignalStrength;
        CellSignalStrengthTdscdma cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength;
        CellConfigLte cellConfigLte;
        CellSignalStrengthNr cellSignalStrengthNrConvertHalNrSignalStrength;
        CellIdentityGsm cellIdentityGsm;
        CellIdentityCdma cellIdentityCdmaConvertHalCellIdentityCdma;
        CellIdentityLte cellIdentityLte;
        CellIdentityWcdma cellIdentityWcdma;
        CellSignalStrengthLte cellSignalStrengthLte;
        CellIdentityNr cellIdentityNr;
        CellConfigLte cellConfigLte2;
        CellIdentityTdscdma cellIdentityTdscdma;
        CellIdentityLte cellIdentityLteConvertHalCellIdentityLte;
        CellSignalStrengthLte cellSignalStrengthLteConvertHalLteSignalStrength;
        CellConfigLte cellConfigLte3;
        CellIdentityCdma cellIdentityCdmaConvertHalCellIdentityCdma2;
        CellConfigLte cellConfigLteConvertHalCdmaSignalStrength;
        CellConfigLte cellConfigLte4;
        CellSignalStrengthGsm cellSignalStrengthGsm;
        CellIdentityGsm cellIdentityGsm2;
        boolean z2;
        CellConfigLte cellConfigLte5;
        CellConfigLte cellConfigLte6;
        CellConfigLte cellConfigLteConvertHalWcdmaSignalStrength2;
        CellSignalStrengthTdscdma cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2;
        CellSignalStrengthNr cellSignalStrengthNrConvertHalNrSignalStrength2;
        CellSignalStrengthGsm cellSignalStrengthGsmConvertHalGsmSignalStrength2;
        CellConfigLte cellConfigLte7;
        CellIdentityGsm cellIdentityGsm3;
        CellConfigLte cellConfigLteConvertHalCdmaSignalStrength2;
        if (obj == null) {
            return null;
        }
        char c = 6;
        char c2 = 2;
        if (obj instanceof android.hardware.radio.V1_4.CellInfo) {
            android.hardware.radio.V1_4.CellInfo cellInfo = (android.hardware.radio.V1_4.CellInfo) obj;
            i = cellInfo.connectionStatus;
            boolean z3 = cellInfo.isRegistered;
            byte discriminator = cellInfo.info.getDiscriminator();
            if (discriminator == 0) {
                CellInfoGsm cellInfoGsmGsm = cellInfo.info.gsm();
                CellIdentityGsm cellIdentityGsmConvertHalCellIdentityGsm2 = convertHalCellIdentityGsm(cellInfoGsmGsm.cellIdentityGsm);
                cellSignalStrengthGsmConvertHalGsmSignalStrength2 = convertHalGsmSignalStrength(cellInfoGsmGsm.signalStrengthGsm);
                cellConfigLteConvertHalWcdmaSignalStrength2 = null;
                cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2 = null;
                cellSignalStrengthNrConvertHalNrSignalStrength2 = null;
                cellIdentityCdmaConvertHalCellIdentityCdma = null;
                cellIdentityLte = null;
                cellIdentityWcdma = null;
                cellIdentityTdscdma = null;
                cellSignalStrengthLte = null;
                cellIdentityNr = null;
                cellConfigLte7 = null;
                c2 = 1;
                cellIdentityGsm3 = cellIdentityGsmConvertHalCellIdentityGsm2;
                cellConfigLteConvertHalCdmaSignalStrength2 = null;
            } else if (discriminator == 1) {
                CellInfoCdma cellInfoCdmaCdma = cellInfo.info.cdma();
                cellConfigLteConvertHalWcdmaSignalStrength2 = null;
                cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2 = null;
                cellSignalStrengthNrConvertHalNrSignalStrength2 = null;
                cellIdentityGsm3 = null;
                cellIdentityLte = null;
                cellIdentityWcdma = null;
                cellIdentityTdscdma = null;
                cellSignalStrengthLte = null;
                cellIdentityNr = null;
                cellConfigLte7 = null;
                cellIdentityCdmaConvertHalCellIdentityCdma = convertHalCellIdentityCdma(cellInfoCdmaCdma.cellIdentityCdma);
                cellConfigLteConvertHalCdmaSignalStrength2 = convertHalCdmaSignalStrength(cellInfoCdmaCdma.signalStrengthCdma, cellInfoCdmaCdma.signalStrengthEvdo);
                cellSignalStrengthGsmConvertHalGsmSignalStrength2 = null;
            } else if (discriminator != 2) {
                if (discriminator == 3) {
                    CellInfoTdscdma cellInfoTdscdmaTdscdma = cellInfo.info.tdscdma();
                    CellIdentityTdscdma cellIdentityTdscdmaConvertHalCellIdentityTdscdma = convertHalCellIdentityTdscdma(cellInfoTdscdmaTdscdma.cellIdentityTdscdma);
                    cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2 = convertHalTdscdmaSignalStrength(cellInfoTdscdmaTdscdma.signalStrengthTdscdma);
                    cellSignalStrengthGsmConvertHalGsmSignalStrength2 = null;
                    cellSignalStrengthNrConvertHalNrSignalStrength2 = null;
                    cellIdentityGsm3 = null;
                    cellIdentityCdmaConvertHalCellIdentityCdma = null;
                    cellIdentityLte = null;
                    cellIdentityWcdma = null;
                    cellSignalStrengthLte = null;
                    cellIdentityNr = null;
                    cellConfigLte7 = null;
                    cellIdentityTdscdma = cellIdentityTdscdmaConvertHalCellIdentityTdscdma;
                    c2 = 5;
                } else if (discriminator == 4) {
                    CellInfoLte cellInfoLteLte = cellInfo.info.lte();
                    CellIdentityLte cellIdentityLteConvertHalCellIdentityLte2 = convertHalCellIdentityLte(cellInfoLteLte.base.cellIdentityLte);
                    CellSignalStrengthLte cellSignalStrengthLteConvertHalLteSignalStrength2 = convertHalLteSignalStrength(cellInfoLteLte.base.signalStrengthLte);
                    CellConfigLte cellConfigLte8 = new CellConfigLte(cellInfoLteLte.cellConfig.isEndcAvailable);
                    cellSignalStrengthGsmConvertHalGsmSignalStrength2 = null;
                    cellIdentityGsm3 = null;
                    cellIdentityCdmaConvertHalCellIdentityCdma = null;
                    cellIdentityWcdma = null;
                    cellIdentityTdscdma = null;
                    cellIdentityNr = null;
                    cellIdentityLte = cellIdentityLteConvertHalCellIdentityLte2;
                    cellSignalStrengthLte = cellSignalStrengthLteConvertHalLteSignalStrength2;
                    cellConfigLte7 = cellConfigLte8;
                    c2 = 3;
                    cellConfigLteConvertHalCdmaSignalStrength2 = null;
                    cellConfigLteConvertHalWcdmaSignalStrength2 = null;
                    cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2 = null;
                    cellSignalStrengthNrConvertHalNrSignalStrength2 = null;
                } else {
                    if (discriminator != 5) {
                        return null;
                    }
                    CellInfoNr cellInfoNrNr = cellInfo.info.nr();
                    CellIdentityNr cellIdentityNrConvertHalCellIdentityNr = convertHalCellIdentityNr(cellInfoNrNr.cellidentity);
                    cellSignalStrengthNrConvertHalNrSignalStrength2 = convertHalNrSignalStrength(cellInfoNrNr.signalStrength);
                    cellSignalStrengthGsmConvertHalGsmSignalStrength2 = null;
                    cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2 = null;
                    cellIdentityGsm3 = null;
                    cellIdentityCdmaConvertHalCellIdentityCdma = null;
                    cellIdentityLte = null;
                    cellIdentityWcdma = null;
                    cellIdentityTdscdma = null;
                    cellSignalStrengthLte = null;
                    cellConfigLte7 = null;
                    c2 = 6;
                    cellIdentityNr = cellIdentityNrConvertHalCellIdentityNr;
                }
                cellConfigLteConvertHalCdmaSignalStrength2 = cellConfigLte7;
                cellConfigLteConvertHalWcdmaSignalStrength2 = cellConfigLteConvertHalCdmaSignalStrength2;
            } else {
                CellInfoWcdma cellInfoWcdmaWcdma = cellInfo.info.wcdma();
                CellIdentityWcdma cellIdentityWcdmaConvertHalCellIdentityWcdma2 = convertHalCellIdentityWcdma(cellInfoWcdmaWcdma.cellIdentityWcdma);
                cellConfigLteConvertHalWcdmaSignalStrength2 = convertHalWcdmaSignalStrength(cellInfoWcdmaWcdma.signalStrengthWcdma);
                cellSignalStrengthGsmConvertHalGsmSignalStrength2 = null;
                cellSignalStrengthNrConvertHalNrSignalStrength2 = null;
                cellIdentityGsm3 = null;
                cellIdentityCdmaConvertHalCellIdentityCdma = null;
                cellIdentityLte = null;
                cellIdentityTdscdma = null;
                cellSignalStrengthLte = null;
                cellIdentityNr = null;
                cellConfigLte7 = null;
                cellIdentityWcdma = cellIdentityWcdmaConvertHalCellIdentityWcdma2;
                c2 = 4;
                cellConfigLteConvertHalCdmaSignalStrength2 = null;
                cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2 = null;
            }
            cellSignalStrengthGsm = cellSignalStrengthGsmConvertHalGsmSignalStrength2;
            cellIdentityGsm2 = cellIdentityGsm3;
            z2 = z3;
            cellConfigLte5 = cellConfigLte7;
            cellConfigLte6 = cellConfigLteConvertHalCdmaSignalStrength2;
        } else {
            if (obj instanceof android.hardware.radio.V1_5.CellInfo) {
                android.hardware.radio.V1_5.CellInfo cellInfo2 = (android.hardware.radio.V1_5.CellInfo) obj;
                i = cellInfo2.connectionStatus;
                z = cellInfo2.registered;
                byte discriminator2 = cellInfo2.ratSpecificInfo.getDiscriminator();
                if (discriminator2 == 0) {
                    android.hardware.radio.V1_5.CellInfoGsm cellInfoGsmGsm2 = cellInfo2.ratSpecificInfo.gsm();
                    cellIdentityGsmConvertHalCellIdentityGsm = convertHalCellIdentityGsm(cellInfoGsmGsm2.cellIdentityGsm);
                    cellSignalStrengthGsmConvertHalGsmSignalStrength = convertHalGsmSignalStrength(cellInfoGsmGsm2.signalStrengthGsm);
                    cellIdentityGsm = cellIdentityGsmConvertHalCellIdentityGsm;
                    c = 1;
                    cellConfigLte4 = null;
                    cellConfigLte = null;
                    cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                    cellSignalStrengthNrConvertHalNrSignalStrength = null;
                    cellIdentityCdmaConvertHalCellIdentityCdma = null;
                    cellIdentityLte = null;
                    cellIdentityWcdma = null;
                    cellIdentityTdscdma = null;
                    cellSignalStrengthLte = null;
                    cellIdentityNr = null;
                    cellConfigLte2 = null;
                } else if (discriminator2 != 1) {
                    if (discriminator2 == 2) {
                        android.hardware.radio.V1_5.CellInfoTdscdma cellInfoTdscdmaTdscdma2 = cellInfo2.ratSpecificInfo.tdscdma();
                        CellIdentityTdscdma cellIdentityTdscdmaConvertHalCellIdentityTdscdma2 = convertHalCellIdentityTdscdma(cellInfoTdscdmaTdscdma2.cellIdentityTdscdma);
                        cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = convertHalTdscdmaSignalStrength(cellInfoTdscdmaTdscdma2.signalStrengthTdscdma);
                        cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                        cellConfigLte = null;
                        cellSignalStrengthNrConvertHalNrSignalStrength = null;
                        cellIdentityGsm = null;
                        cellIdentityCdmaConvertHalCellIdentityCdma = null;
                        cellIdentityLte = null;
                        cellIdentityWcdma = null;
                        cellSignalStrengthLte = null;
                        cellIdentityNr = null;
                        cellConfigLte2 = null;
                        cellIdentityTdscdma = cellIdentityTdscdmaConvertHalCellIdentityTdscdma2;
                        c = 5;
                    } else if (discriminator2 == 3) {
                        android.hardware.radio.V1_5.CellInfoLte cellInfoLteLte2 = cellInfo2.ratSpecificInfo.lte();
                        cellIdentityLteConvertHalCellIdentityLte = convertHalCellIdentityLte(cellInfoLteLte2.cellIdentityLte);
                        cellSignalStrengthLteConvertHalLteSignalStrength = convertHalLteSignalStrength(cellInfoLteLte2.signalStrengthLte);
                        cellConfigLte3 = new CellConfigLte();
                        cellSignalStrengthLte = cellSignalStrengthLteConvertHalLteSignalStrength;
                        cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                        cellConfigLte = null;
                        cellSignalStrengthNrConvertHalNrSignalStrength = null;
                        cellIdentityGsm = null;
                        cellIdentityCdmaConvertHalCellIdentityCdma = null;
                        cellIdentityWcdma = null;
                        cellIdentityTdscdma = null;
                        cellIdentityNr = null;
                        cellIdentityLte = cellIdentityLteConvertHalCellIdentityLte;
                        cellConfigLte2 = cellConfigLte3;
                        c = 3;
                        cellConfigLte4 = null;
                        cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                    } else if (discriminator2 == 4) {
                        android.hardware.radio.V1_5.CellInfoNr cellInfoNrNr2 = cellInfo2.ratSpecificInfo.nr();
                        CellIdentityNr cellIdentityNrConvertHalCellIdentityNr2 = convertHalCellIdentityNr(cellInfoNrNr2.cellIdentityNr);
                        cellSignalStrengthNrConvertHalNrSignalStrength = convertHalNrSignalStrength(cellInfoNrNr2.signalStrengthNr);
                        cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                        cellConfigLte = null;
                        cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                        cellIdentityGsm = null;
                        cellIdentityCdmaConvertHalCellIdentityCdma = null;
                        cellIdentityLte = null;
                        cellIdentityWcdma = null;
                        cellIdentityTdscdma = null;
                        cellSignalStrengthLte = null;
                        cellConfigLte2 = null;
                        cellIdentityNr = cellIdentityNrConvertHalCellIdentityNr2;
                    } else {
                        if (discriminator2 != 5) {
                            return null;
                        }
                        CellInfoCdma cellInfoCdmaCdma2 = cellInfo2.ratSpecificInfo.cdma();
                        cellIdentityCdmaConvertHalCellIdentityCdma2 = convertHalCellIdentityCdma(cellInfoCdmaCdma2.cellIdentityCdma);
                        cellConfigLteConvertHalCdmaSignalStrength = convertHalCdmaSignalStrength(cellInfoCdmaCdma2.signalStrengthCdma, cellInfoCdmaCdma2.signalStrengthEvdo);
                        cellConfigLte4 = cellConfigLteConvertHalCdmaSignalStrength;
                        cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                        cellConfigLte = null;
                        cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                        cellIdentityGsm = null;
                        cellIdentityLte = null;
                        cellIdentityWcdma = null;
                        cellIdentityTdscdma = null;
                        cellSignalStrengthLte = null;
                        cellIdentityNr = null;
                        cellConfigLte2 = null;
                        cellIdentityCdmaConvertHalCellIdentityCdma = cellIdentityCdmaConvertHalCellIdentityCdma2;
                        c = 2;
                        cellSignalStrengthNrConvertHalNrSignalStrength = null;
                    }
                    cellConfigLte4 = cellConfigLte2;
                } else {
                    android.hardware.radio.V1_5.CellInfoWcdma cellInfoWcdmaWcdma2 = cellInfo2.ratSpecificInfo.wcdma();
                    cellIdentityWcdmaConvertHalCellIdentityWcdma = convertHalCellIdentityWcdma(cellInfoWcdmaWcdma2.cellIdentityWcdma);
                    cellConfigLteConvertHalWcdmaSignalStrength = convertHalWcdmaSignalStrength(cellInfoWcdmaWcdma2.signalStrengthWcdma);
                    cellConfigLte4 = null;
                    cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                    cellSignalStrengthNrConvertHalNrSignalStrength = null;
                    cellIdentityGsm = null;
                    cellIdentityCdmaConvertHalCellIdentityCdma = null;
                    cellIdentityLte = null;
                    cellIdentityTdscdma = null;
                    cellSignalStrengthLte = null;
                    cellIdentityNr = null;
                    cellConfigLte2 = null;
                    cellIdentityWcdma = cellIdentityWcdmaConvertHalCellIdentityWcdma;
                    c = 4;
                    cellConfigLte = cellConfigLteConvertHalWcdmaSignalStrength;
                    cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                }
            } else {
                if (!(obj instanceof android.hardware.radio.V1_6.CellInfo)) {
                    return null;
                }
                android.hardware.radio.V1_6.CellInfo cellInfo3 = (android.hardware.radio.V1_6.CellInfo) obj;
                i = cellInfo3.connectionStatus;
                z = cellInfo3.registered;
                byte discriminator3 = cellInfo3.ratSpecificInfo.getDiscriminator();
                if (discriminator3 == 0) {
                    android.hardware.radio.V1_5.CellInfoGsm cellInfoGsmGsm3 = cellInfo3.ratSpecificInfo.gsm();
                    cellIdentityGsmConvertHalCellIdentityGsm = convertHalCellIdentityGsm(cellInfoGsmGsm3.cellIdentityGsm);
                    cellSignalStrengthGsmConvertHalGsmSignalStrength = convertHalGsmSignalStrength(cellInfoGsmGsm3.signalStrengthGsm);
                    cellIdentityGsm = cellIdentityGsmConvertHalCellIdentityGsm;
                    c = 1;
                    cellConfigLte4 = null;
                    cellConfigLte = null;
                    cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                    cellSignalStrengthNrConvertHalNrSignalStrength = null;
                    cellIdentityCdmaConvertHalCellIdentityCdma = null;
                    cellIdentityLte = null;
                    cellIdentityWcdma = null;
                    cellIdentityTdscdma = null;
                    cellSignalStrengthLte = null;
                    cellIdentityNr = null;
                    cellConfigLte2 = null;
                } else if (discriminator3 != 1) {
                    if (discriminator3 == 2) {
                        android.hardware.radio.V1_5.CellInfoTdscdma cellInfoTdscdmaTdscdma3 = cellInfo3.ratSpecificInfo.tdscdma();
                        CellIdentityTdscdma cellIdentityTdscdmaConvertHalCellIdentityTdscdma3 = convertHalCellIdentityTdscdma(cellInfoTdscdmaTdscdma3.cellIdentityTdscdma);
                        cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = convertHalTdscdmaSignalStrength(cellInfoTdscdmaTdscdma3.signalStrengthTdscdma);
                        cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                        cellConfigLte = null;
                        cellSignalStrengthNrConvertHalNrSignalStrength = null;
                        cellIdentityGsm = null;
                        cellIdentityCdmaConvertHalCellIdentityCdma = null;
                        cellIdentityLte = null;
                        cellIdentityWcdma = null;
                        cellSignalStrengthLte = null;
                        cellIdentityNr = null;
                        cellConfigLte2 = null;
                        cellIdentityTdscdma = cellIdentityTdscdmaConvertHalCellIdentityTdscdma3;
                        c = 5;
                    } else if (discriminator3 == 3) {
                        android.hardware.radio.V1_6.CellInfoLte cellInfoLteLte3 = cellInfo3.ratSpecificInfo.lte();
                        cellIdentityLteConvertHalCellIdentityLte = convertHalCellIdentityLte(cellInfoLteLte3.cellIdentityLte);
                        cellSignalStrengthLteConvertHalLteSignalStrength = convertHalLteSignalStrength(cellInfoLteLte3.signalStrengthLte);
                        cellConfigLte3 = new CellConfigLte();
                        cellSignalStrengthLte = cellSignalStrengthLteConvertHalLteSignalStrength;
                        cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                        cellConfigLte = null;
                        cellSignalStrengthNrConvertHalNrSignalStrength = null;
                        cellIdentityGsm = null;
                        cellIdentityCdmaConvertHalCellIdentityCdma = null;
                        cellIdentityWcdma = null;
                        cellIdentityTdscdma = null;
                        cellIdentityNr = null;
                        cellIdentityLte = cellIdentityLteConvertHalCellIdentityLte;
                        cellConfigLte2 = cellConfigLte3;
                        c = 3;
                        cellConfigLte4 = null;
                        cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                    } else if (discriminator3 == 4) {
                        android.hardware.radio.V1_6.CellInfoNr cellInfoNrNr3 = cellInfo3.ratSpecificInfo.nr();
                        CellIdentityNr cellIdentityNrConvertHalCellIdentityNr3 = convertHalCellIdentityNr(cellInfoNrNr3.cellIdentityNr);
                        cellSignalStrengthNrConvertHalNrSignalStrength = convertHalNrSignalStrength(cellInfoNrNr3.signalStrengthNr);
                        cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                        cellConfigLte = null;
                        cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                        cellIdentityGsm = null;
                        cellIdentityCdmaConvertHalCellIdentityCdma = null;
                        cellIdentityLte = null;
                        cellIdentityWcdma = null;
                        cellIdentityTdscdma = null;
                        cellSignalStrengthLte = null;
                        cellConfigLte2 = null;
                        cellIdentityNr = cellIdentityNrConvertHalCellIdentityNr3;
                    } else {
                        if (discriminator3 != 5) {
                            return null;
                        }
                        CellInfoCdma cellInfoCdmaCdma3 = cellInfo3.ratSpecificInfo.cdma();
                        cellIdentityCdmaConvertHalCellIdentityCdma2 = convertHalCellIdentityCdma(cellInfoCdmaCdma3.cellIdentityCdma);
                        cellConfigLteConvertHalCdmaSignalStrength = convertHalCdmaSignalStrength(cellInfoCdmaCdma3.signalStrengthCdma, cellInfoCdmaCdma3.signalStrengthEvdo);
                        cellConfigLte4 = cellConfigLteConvertHalCdmaSignalStrength;
                        cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                        cellConfigLte = null;
                        cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                        cellIdentityGsm = null;
                        cellIdentityLte = null;
                        cellIdentityWcdma = null;
                        cellIdentityTdscdma = null;
                        cellSignalStrengthLte = null;
                        cellIdentityNr = null;
                        cellConfigLte2 = null;
                        cellIdentityCdmaConvertHalCellIdentityCdma = cellIdentityCdmaConvertHalCellIdentityCdma2;
                        c = 2;
                        cellSignalStrengthNrConvertHalNrSignalStrength = null;
                    }
                    cellConfigLte4 = cellConfigLte2;
                } else {
                    android.hardware.radio.V1_5.CellInfoWcdma cellInfoWcdmaWcdma3 = cellInfo3.ratSpecificInfo.wcdma();
                    cellIdentityWcdmaConvertHalCellIdentityWcdma = convertHalCellIdentityWcdma(cellInfoWcdmaWcdma3.cellIdentityWcdma);
                    cellConfigLteConvertHalWcdmaSignalStrength = convertHalWcdmaSignalStrength(cellInfoWcdmaWcdma3.signalStrengthWcdma);
                    cellConfigLte4 = null;
                    cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength = null;
                    cellSignalStrengthNrConvertHalNrSignalStrength = null;
                    cellIdentityGsm = null;
                    cellIdentityCdmaConvertHalCellIdentityCdma = null;
                    cellIdentityLte = null;
                    cellIdentityTdscdma = null;
                    cellSignalStrengthLte = null;
                    cellIdentityNr = null;
                    cellConfigLte2 = null;
                    cellIdentityWcdma = cellIdentityWcdmaConvertHalCellIdentityWcdma;
                    c = 4;
                    cellConfigLte = cellConfigLteConvertHalWcdmaSignalStrength;
                    cellSignalStrengthGsmConvertHalGsmSignalStrength = null;
                }
            }
            cellSignalStrengthGsm = cellSignalStrengthGsmConvertHalGsmSignalStrength;
            cellIdentityGsm2 = cellIdentityGsm;
            z2 = z;
            cellConfigLte5 = cellConfigLte2;
            cellConfigLte6 = cellConfigLte4;
            cellConfigLteConvertHalWcdmaSignalStrength2 = cellConfigLte;
            cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2 = cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength;
            cellSignalStrengthNrConvertHalNrSignalStrength2 = cellSignalStrengthNrConvertHalNrSignalStrength;
            c2 = c;
        }
        int i2 = i;
        switch (c2) {
            case 1:
                return new android.telephony.CellInfoGsm(i2, z2, j, cellIdentityGsm2, cellSignalStrengthGsm);
            case 2:
                return new android.telephony.CellInfoCdma(i2, z2, j, cellIdentityCdmaConvertHalCellIdentityCdma, cellConfigLte6);
            case 3:
                return new android.telephony.CellInfoLte(i2, z2, j, cellIdentityLte, cellSignalStrengthLte, cellConfigLte5);
            case 4:
                return new android.telephony.CellInfoWcdma(i2, z2, j, cellIdentityWcdma, cellConfigLteConvertHalWcdmaSignalStrength2);
            case 5:
                return new android.telephony.CellInfoTdscdma(i2, z2, j, cellIdentityTdscdma, cellSignalStrengthTdscdmaConvertHalTdscdmaSignalStrength2);
            case 6:
                return new android.telephony.CellInfoNr(i2, z2, j, cellIdentityNr, cellSignalStrengthNrConvertHalNrSignalStrength2);
            default:
                return null;
        }
    }

    private static CellInfo convertHalCellInfo(android.hardware.radio.network.CellInfo cellInfo, long j) {
        if (cellInfo == null) {
            return null;
        }
        int i = cellInfo.connectionStatus;
        boolean z = cellInfo.registered;
        int tag = cellInfo.ratSpecificInfo.getTag();
        if (tag == 0) {
            android.hardware.radio.network.CellInfoGsm gsm = cellInfo.ratSpecificInfo.getGsm();
            return new android.telephony.CellInfoGsm(i, z, j, convertHalCellIdentityGsm(gsm.cellIdentityGsm), convertHalGsmSignalStrength(gsm.signalStrengthGsm));
        }
        if (tag == 1) {
            android.hardware.radio.network.CellInfoWcdma wcdma = cellInfo.ratSpecificInfo.getWcdma();
            return new android.telephony.CellInfoWcdma(i, z, j, convertHalCellIdentityWcdma(wcdma.cellIdentityWcdma), convertHalWcdmaSignalStrength(wcdma.signalStrengthWcdma));
        }
        if (tag == 2) {
            android.hardware.radio.network.CellInfoTdscdma tdscdma = cellInfo.ratSpecificInfo.getTdscdma();
            return new android.telephony.CellInfoTdscdma(i, z, j, convertHalCellIdentityTdscdma(tdscdma.cellIdentityTdscdma), convertHalTdscdmaSignalStrength(tdscdma.signalStrengthTdscdma));
        }
        if (tag == 3) {
            android.hardware.radio.network.CellInfoLte lte = cellInfo.ratSpecificInfo.getLte();
            return new android.telephony.CellInfoLte(i, z, j, convertHalCellIdentityLte(lte.cellIdentityLte), convertHalLteSignalStrength(lte.signalStrengthLte), new CellConfigLte());
        }
        if (tag == 4) {
            android.hardware.radio.network.CellInfoNr nr = cellInfo.ratSpecificInfo.getNr();
            return new android.telephony.CellInfoNr(i, z, j, convertHalCellIdentityNr(nr.cellIdentityNr), convertHalNrSignalStrength(nr.signalStrengthNr));
        }
        if (tag != 5) {
            return null;
        }
        android.hardware.radio.network.CellInfoCdma cdma = cellInfo.ratSpecificInfo.getCdma();
        return new android.telephony.CellInfoCdma(i, z, j, convertHalCellIdentityCdma(cdma.cellIdentityCdma), convertHalCdmaSignalStrength(cdma.signalStrengthCdma, cdma.signalStrengthEvdo));
    }

    public static CellIdentity convertHalCellIdentity(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof android.hardware.radio.V1_2.CellIdentity) {
            android.hardware.radio.V1_2.CellIdentity cellIdentity = (android.hardware.radio.V1_2.CellIdentity) obj;
            int i = cellIdentity.cellInfoType;
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            if (cellIdentity.cellIdentityWcdma.size() == 1) {
                                return convertHalCellIdentityWcdma(cellIdentity.cellIdentityWcdma.get(0));
                            }
                        } else if (i == 5 && cellIdentity.cellIdentityTdscdma.size() == 1) {
                            return convertHalCellIdentityTdscdma(cellIdentity.cellIdentityTdscdma.get(0));
                        }
                    } else if (cellIdentity.cellIdentityLte.size() == 1) {
                        return convertHalCellIdentityLte(cellIdentity.cellIdentityLte.get(0));
                    }
                } else if (cellIdentity.cellIdentityCdma.size() == 1) {
                    return convertHalCellIdentityCdma(cellIdentity.cellIdentityCdma.get(0));
                }
            } else if (cellIdentity.cellIdentityGsm.size() == 1) {
                return convertHalCellIdentityGsm(cellIdentity.cellIdentityGsm.get(0));
            }
        } else if (obj instanceof android.hardware.radio.V1_5.CellIdentity) {
            android.hardware.radio.V1_5.CellIdentity cellIdentity2 = (android.hardware.radio.V1_5.CellIdentity) obj;
            switch (cellIdentity2.getDiscriminator()) {
                case 1:
                    return convertHalCellIdentityGsm(cellIdentity2.gsm());
                case 2:
                    return convertHalCellIdentityWcdma(cellIdentity2.wcdma());
                case 3:
                    return convertHalCellIdentityTdscdma(cellIdentity2.tdscdma());
                case 4:
                    return convertHalCellIdentityCdma(cellIdentity2.cdma());
                case 5:
                    return convertHalCellIdentityLte(cellIdentity2.lte());
                case 6:
                    return convertHalCellIdentityNr(cellIdentity2.nr());
            }
        }
        return null;
    }

    public static CellIdentity convertHalCellIdentity(android.hardware.radio.network.CellIdentity cellIdentity) {
        if (cellIdentity == null) {
            return null;
        }
        switch (cellIdentity.getTag()) {
            case 1:
                return convertHalCellIdentityGsm(cellIdentity.getGsm());
            case 2:
                return convertHalCellIdentityWcdma(cellIdentity.getWcdma());
            case 3:
                return convertHalCellIdentityTdscdma(cellIdentity.getTdscdma());
            case 4:
                return convertHalCellIdentityCdma(cellIdentity.getCdma());
            case 5:
                return convertHalCellIdentityLte(cellIdentity.getLte());
            case 6:
                return convertHalCellIdentityNr(cellIdentity.getNr());
            default:
                return null;
        }
    }

    public static CellIdentityGsm convertHalCellIdentityGsm(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof android.hardware.radio.V1_2.CellIdentityGsm) {
            android.hardware.radio.V1_2.CellIdentityGsm cellIdentityGsm = (android.hardware.radio.V1_2.CellIdentityGsm) obj;
            android.hardware.radio.V1_0.CellIdentityGsm cellIdentityGsm2 = cellIdentityGsm.base;
            int i = cellIdentityGsm2.lac;
            int i2 = cellIdentityGsm2.cid;
            int i3 = cellIdentityGsm2.arfcn;
            byte b = cellIdentityGsm2.bsic;
            byte b2 = b == -1 ? (byte) 2147483647 : b;
            String str = cellIdentityGsm2.mcc;
            String str2 = cellIdentityGsm2.mnc;
            CellIdentityOperatorNames cellIdentityOperatorNames = cellIdentityGsm.operatorNames;
            return new CellIdentityGsm(i, i2, i3, b2, str, str2, cellIdentityOperatorNames.alphaLong, cellIdentityOperatorNames.alphaShort, new ArraySet());
        }
        if (!(obj instanceof android.hardware.radio.V1_5.CellIdentityGsm)) {
            return null;
        }
        android.hardware.radio.V1_5.CellIdentityGsm cellIdentityGsm3 = (android.hardware.radio.V1_5.CellIdentityGsm) obj;
        android.hardware.radio.V1_2.CellIdentityGsm cellIdentityGsm4 = cellIdentityGsm3.base;
        android.hardware.radio.V1_0.CellIdentityGsm cellIdentityGsm5 = cellIdentityGsm4.base;
        int i4 = cellIdentityGsm5.lac;
        int i5 = cellIdentityGsm5.cid;
        int i6 = cellIdentityGsm5.arfcn;
        byte b3 = cellIdentityGsm5.bsic;
        if (b3 == -1) {
            b3 = 2147483647;
        }
        String str3 = cellIdentityGsm5.mcc;
        String str4 = cellIdentityGsm5.mnc;
        CellIdentityOperatorNames cellIdentityOperatorNames2 = cellIdentityGsm4.operatorNames;
        return new CellIdentityGsm(i4, i5, i6, b3, str3, str4, cellIdentityOperatorNames2.alphaLong, cellIdentityOperatorNames2.alphaShort, cellIdentityGsm3.additionalPlmns);
    }

    public static CellIdentityGsm convertHalCellIdentityGsm(android.hardware.radio.network.CellIdentityGsm cellIdentityGsm) {
        int i = cellIdentityGsm.lac;
        int i2 = cellIdentityGsm.cid;
        int i3 = cellIdentityGsm.arfcn;
        byte b = cellIdentityGsm.bsic;
        if (b == -1) {
            b = 2147483647;
        }
        String str = cellIdentityGsm.mcc;
        String str2 = cellIdentityGsm.mnc;
        OperatorInfo operatorInfo = cellIdentityGsm.operatorNames;
        return new CellIdentityGsm(i, i2, i3, b, str, str2, operatorInfo.alphaLong, operatorInfo.alphaShort, new ArraySet());
    }

    public static CellIdentityCdma convertHalCellIdentityCdma(Object obj) {
        if (obj == null || !(obj instanceof android.hardware.radio.V1_2.CellIdentityCdma)) {
            return null;
        }
        android.hardware.radio.V1_2.CellIdentityCdma cellIdentityCdma = (android.hardware.radio.V1_2.CellIdentityCdma) obj;
        android.hardware.radio.V1_0.CellIdentityCdma cellIdentityCdma2 = cellIdentityCdma.base;
        int i = cellIdentityCdma2.networkId;
        int i2 = cellIdentityCdma2.systemId;
        int i3 = cellIdentityCdma2.baseStationId;
        int i4 = cellIdentityCdma2.longitude;
        int i5 = cellIdentityCdma2.latitude;
        CellIdentityOperatorNames cellIdentityOperatorNames = cellIdentityCdma.operatorNames;
        return new CellIdentityCdma(i, i2, i3, i4, i5, cellIdentityOperatorNames.alphaLong, cellIdentityOperatorNames.alphaShort);
    }

    public static CellIdentityCdma convertHalCellIdentityCdma(android.hardware.radio.network.CellIdentityCdma cellIdentityCdma) {
        int i = cellIdentityCdma.networkId;
        int i2 = cellIdentityCdma.systemId;
        int i3 = cellIdentityCdma.baseStationId;
        int i4 = cellIdentityCdma.longitude;
        int i5 = cellIdentityCdma.latitude;
        OperatorInfo operatorInfo = cellIdentityCdma.operatorNames;
        return new CellIdentityCdma(i, i2, i3, i4, i5, operatorInfo.alphaLong, operatorInfo.alphaShort);
    }

    public static CellIdentityLte convertHalCellIdentityLte(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof android.hardware.radio.V1_2.CellIdentityLte) {
            android.hardware.radio.V1_2.CellIdentityLte cellIdentityLte = (android.hardware.radio.V1_2.CellIdentityLte) obj;
            android.hardware.radio.V1_0.CellIdentityLte cellIdentityLte2 = cellIdentityLte.base;
            int i = cellIdentityLte.bandwidth;
            String str = cellIdentityLte2.mcc;
            String str2 = cellIdentityLte2.mnc;
            CellIdentityOperatorNames cellIdentityOperatorNames = cellIdentityLte.operatorNames;
            return new CellIdentityLte(cellIdentityLte2.ci, cellIdentityLte2.pci, cellIdentityLte2.tac, cellIdentityLte2.earfcn, new int[0], i, str, str2, cellIdentityOperatorNames.alphaLong, cellIdentityOperatorNames.alphaShort, new ArraySet(), null);
        }
        if (!(obj instanceof android.hardware.radio.V1_5.CellIdentityLte)) {
            return null;
        }
        android.hardware.radio.V1_5.CellIdentityLte cellIdentityLte3 = (android.hardware.radio.V1_5.CellIdentityLte) obj;
        android.hardware.radio.V1_0.CellIdentityLte cellIdentityLte4 = cellIdentityLte3.base.base;
        int i2 = cellIdentityLte4.ci;
        int i3 = cellIdentityLte4.pci;
        int i4 = cellIdentityLte4.tac;
        int i5 = cellIdentityLte4.earfcn;
        int[] array = cellIdentityLte3.bands.stream().mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
        android.hardware.radio.V1_2.CellIdentityLte cellIdentityLte5 = cellIdentityLte3.base;
        int i6 = cellIdentityLte5.bandwidth;
        android.hardware.radio.V1_0.CellIdentityLte cellIdentityLte6 = cellIdentityLte5.base;
        String str3 = cellIdentityLte6.mcc;
        String str4 = cellIdentityLte6.mnc;
        CellIdentityOperatorNames cellIdentityOperatorNames2 = cellIdentityLte5.operatorNames;
        return new CellIdentityLte(i2, i3, i4, i5, array, i6, str3, str4, cellIdentityOperatorNames2.alphaLong, cellIdentityOperatorNames2.alphaShort, cellIdentityLte3.additionalPlmns, convertHalClosedSubscriberGroupInfo(cellIdentityLte3.optionalCsgInfo));
    }

    public static CellIdentityLte convertHalCellIdentityLte(android.hardware.radio.network.CellIdentityLte cellIdentityLte) {
        int i = cellIdentityLte.ci;
        int i2 = cellIdentityLte.pci;
        int i3 = cellIdentityLte.tac;
        int i4 = cellIdentityLte.earfcn;
        int[] iArr = cellIdentityLte.bands;
        int i5 = cellIdentityLte.bandwidth;
        String str = cellIdentityLte.mcc;
        String str2 = cellIdentityLte.mnc;
        OperatorInfo operatorInfo = cellIdentityLte.operatorNames;
        return new CellIdentityLte(i, i2, i3, i4, iArr, i5, str, str2, operatorInfo.alphaLong, operatorInfo.alphaShort, primitiveArrayToArrayList(cellIdentityLte.additionalPlmns), convertHalClosedSubscriberGroupInfo(cellIdentityLte.csgInfo));
    }

    public static CellIdentityWcdma convertHalCellIdentityWcdma(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof android.hardware.radio.V1_2.CellIdentityWcdma) {
            android.hardware.radio.V1_2.CellIdentityWcdma cellIdentityWcdma = (android.hardware.radio.V1_2.CellIdentityWcdma) obj;
            android.hardware.radio.V1_0.CellIdentityWcdma cellIdentityWcdma2 = cellIdentityWcdma.base;
            int i = cellIdentityWcdma2.lac;
            int i2 = cellIdentityWcdma2.cid;
            int i3 = cellIdentityWcdma2.psc;
            int i4 = cellIdentityWcdma2.uarfcn;
            String str = cellIdentityWcdma2.mcc;
            String str2 = cellIdentityWcdma2.mnc;
            CellIdentityOperatorNames cellIdentityOperatorNames = cellIdentityWcdma.operatorNames;
            return new CellIdentityWcdma(i, i2, i3, i4, str, str2, cellIdentityOperatorNames.alphaLong, cellIdentityOperatorNames.alphaShort, new ArraySet(), null);
        }
        if (!(obj instanceof android.hardware.radio.V1_5.CellIdentityWcdma)) {
            return null;
        }
        android.hardware.radio.V1_5.CellIdentityWcdma cellIdentityWcdma3 = (android.hardware.radio.V1_5.CellIdentityWcdma) obj;
        android.hardware.radio.V1_2.CellIdentityWcdma cellIdentityWcdma4 = cellIdentityWcdma3.base;
        android.hardware.radio.V1_0.CellIdentityWcdma cellIdentityWcdma5 = cellIdentityWcdma4.base;
        int i5 = cellIdentityWcdma5.lac;
        int i6 = cellIdentityWcdma5.cid;
        int i7 = cellIdentityWcdma5.psc;
        int i8 = cellIdentityWcdma5.uarfcn;
        String str3 = cellIdentityWcdma5.mcc;
        String str4 = cellIdentityWcdma5.mnc;
        CellIdentityOperatorNames cellIdentityOperatorNames2 = cellIdentityWcdma4.operatorNames;
        return new CellIdentityWcdma(i5, i6, i7, i8, str3, str4, cellIdentityOperatorNames2.alphaLong, cellIdentityOperatorNames2.alphaShort, cellIdentityWcdma3.additionalPlmns, convertHalClosedSubscriberGroupInfo(cellIdentityWcdma3.optionalCsgInfo));
    }

    public static CellIdentityWcdma convertHalCellIdentityWcdma(android.hardware.radio.network.CellIdentityWcdma cellIdentityWcdma) {
        int i = cellIdentityWcdma.lac;
        int i2 = cellIdentityWcdma.cid;
        int i3 = cellIdentityWcdma.psc;
        int i4 = cellIdentityWcdma.uarfcn;
        String str = cellIdentityWcdma.mcc;
        String str2 = cellIdentityWcdma.mnc;
        OperatorInfo operatorInfo = cellIdentityWcdma.operatorNames;
        return new CellIdentityWcdma(i, i2, i3, i4, str, str2, operatorInfo.alphaLong, operatorInfo.alphaShort, primitiveArrayToArrayList(cellIdentityWcdma.additionalPlmns), convertHalClosedSubscriberGroupInfo(cellIdentityWcdma.csgInfo));
    }

    public static CellIdentityTdscdma convertHalCellIdentityTdscdma(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof android.hardware.radio.V1_2.CellIdentityTdscdma) {
            android.hardware.radio.V1_2.CellIdentityTdscdma cellIdentityTdscdma = (android.hardware.radio.V1_2.CellIdentityTdscdma) obj;
            android.hardware.radio.V1_0.CellIdentityTdscdma cellIdentityTdscdma2 = cellIdentityTdscdma.base;
            String str = cellIdentityTdscdma2.mcc;
            String str2 = cellIdentityTdscdma2.mnc;
            int i = cellIdentityTdscdma2.lac;
            int i2 = cellIdentityTdscdma2.cid;
            int i3 = cellIdentityTdscdma2.cpid;
            int i4 = cellIdentityTdscdma.uarfcn;
            CellIdentityOperatorNames cellIdentityOperatorNames = cellIdentityTdscdma.operatorNames;
            return new CellIdentityTdscdma(str, str2, i, i2, i3, i4, cellIdentityOperatorNames.alphaLong, cellIdentityOperatorNames.alphaShort, Collections.EMPTY_LIST, null);
        }
        if (!(obj instanceof android.hardware.radio.V1_5.CellIdentityTdscdma)) {
            return null;
        }
        android.hardware.radio.V1_5.CellIdentityTdscdma cellIdentityTdscdma3 = (android.hardware.radio.V1_5.CellIdentityTdscdma) obj;
        android.hardware.radio.V1_2.CellIdentityTdscdma cellIdentityTdscdma4 = cellIdentityTdscdma3.base;
        android.hardware.radio.V1_0.CellIdentityTdscdma cellIdentityTdscdma5 = cellIdentityTdscdma4.base;
        String str3 = cellIdentityTdscdma5.mcc;
        String str4 = cellIdentityTdscdma5.mnc;
        int i5 = cellIdentityTdscdma5.lac;
        int i6 = cellIdentityTdscdma5.cid;
        int i7 = cellIdentityTdscdma5.cpid;
        int i8 = cellIdentityTdscdma4.uarfcn;
        CellIdentityOperatorNames cellIdentityOperatorNames2 = cellIdentityTdscdma4.operatorNames;
        return new CellIdentityTdscdma(str3, str4, i5, i6, i7, i8, cellIdentityOperatorNames2.alphaLong, cellIdentityOperatorNames2.alphaShort, cellIdentityTdscdma3.additionalPlmns, convertHalClosedSubscriberGroupInfo(cellIdentityTdscdma3.optionalCsgInfo));
    }

    public static CellIdentityTdscdma convertHalCellIdentityTdscdma(android.hardware.radio.network.CellIdentityTdscdma cellIdentityTdscdma) {
        String str = cellIdentityTdscdma.mcc;
        String str2 = cellIdentityTdscdma.mnc;
        int i = cellIdentityTdscdma.lac;
        int i2 = cellIdentityTdscdma.cid;
        int i3 = cellIdentityTdscdma.cpid;
        int i4 = cellIdentityTdscdma.uarfcn;
        OperatorInfo operatorInfo = cellIdentityTdscdma.operatorNames;
        return new CellIdentityTdscdma(str, str2, i, i2, i3, i4, operatorInfo.alphaLong, operatorInfo.alphaShort, primitiveArrayToArrayList(cellIdentityTdscdma.additionalPlmns), convertHalClosedSubscriberGroupInfo(cellIdentityTdscdma.csgInfo));
    }

    public static CellIdentityNr convertHalCellIdentityNr(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof android.hardware.radio.V1_4.CellIdentityNr) {
            android.hardware.radio.V1_4.CellIdentityNr cellIdentityNr = (android.hardware.radio.V1_4.CellIdentityNr) obj;
            String str = cellIdentityNr.mcc;
            String str2 = cellIdentityNr.mnc;
            long j = cellIdentityNr.nci;
            CellIdentityOperatorNames cellIdentityOperatorNames = cellIdentityNr.operatorNames;
            return new CellIdentityNr(cellIdentityNr.pci, cellIdentityNr.tac, cellIdentityNr.nrarfcn, new int[0], str, str2, j, cellIdentityOperatorNames.alphaLong, cellIdentityOperatorNames.alphaShort, new ArraySet());
        }
        if (!(obj instanceof android.hardware.radio.V1_5.CellIdentityNr)) {
            return null;
        }
        android.hardware.radio.V1_5.CellIdentityNr cellIdentityNr2 = (android.hardware.radio.V1_5.CellIdentityNr) obj;
        android.hardware.radio.V1_4.CellIdentityNr cellIdentityNr3 = cellIdentityNr2.base;
        int i = cellIdentityNr3.pci;
        int i2 = cellIdentityNr3.tac;
        int i3 = cellIdentityNr3.nrarfcn;
        int[] array = cellIdentityNr2.bands.stream().mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
        android.hardware.radio.V1_4.CellIdentityNr cellIdentityNr4 = cellIdentityNr2.base;
        String str3 = cellIdentityNr4.mcc;
        String str4 = cellIdentityNr4.mnc;
        long j2 = cellIdentityNr4.nci;
        CellIdentityOperatorNames cellIdentityOperatorNames2 = cellIdentityNr4.operatorNames;
        return new CellIdentityNr(i, i2, i3, array, str3, str4, j2, cellIdentityOperatorNames2.alphaLong, cellIdentityOperatorNames2.alphaShort, cellIdentityNr2.additionalPlmns);
    }

    public static CellIdentityNr convertHalCellIdentityNr(android.hardware.radio.network.CellIdentityNr cellIdentityNr) {
        int i = cellIdentityNr.pci;
        int i2 = cellIdentityNr.tac;
        int i3 = cellIdentityNr.nrarfcn;
        int[] iArr = cellIdentityNr.bands;
        String str = cellIdentityNr.mcc;
        String str2 = cellIdentityNr.mnc;
        long j = cellIdentityNr.nci;
        OperatorInfo operatorInfo = cellIdentityNr.operatorNames;
        return new CellIdentityNr(i, i2, i3, iArr, str, str2, j, operatorInfo.alphaLong, operatorInfo.alphaShort, primitiveArrayToArrayList(cellIdentityNr.additionalPlmns));
    }

    public static SignalStrength convertHalSignalStrength(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof android.hardware.radio.V1_4.SignalStrength) {
            android.hardware.radio.V1_4.SignalStrength signalStrength = (android.hardware.radio.V1_4.SignalStrength) obj;
            return new SignalStrength(convertHalCdmaSignalStrength(signalStrength.cdma, signalStrength.evdo), convertHalGsmSignalStrength(signalStrength.gsm), convertHalWcdmaSignalStrength(signalStrength.wcdma), convertHalTdscdmaSignalStrength(signalStrength.tdscdma), convertHalLteSignalStrength(signalStrength.lte), convertHalNrSignalStrength(signalStrength.nr));
        }
        if (!(obj instanceof android.hardware.radio.V1_6.SignalStrength)) {
            return null;
        }
        android.hardware.radio.V1_6.SignalStrength signalStrength2 = (android.hardware.radio.V1_6.SignalStrength) obj;
        return new SignalStrength(convertHalCdmaSignalStrength(signalStrength2.cdma, signalStrength2.evdo), convertHalGsmSignalStrength(signalStrength2.gsm), convertHalWcdmaSignalStrength(signalStrength2.wcdma), convertHalTdscdmaSignalStrength(signalStrength2.tdscdma), convertHalLteSignalStrength(signalStrength2.lte), convertHalNrSignalStrength(signalStrength2.nr));
    }

    public static SignalStrength convertHalSignalStrength(android.hardware.radio.network.SignalStrength signalStrength) {
        return new SignalStrength(convertHalCdmaSignalStrength(signalStrength.cdma, signalStrength.evdo), convertHalGsmSignalStrength(signalStrength.gsm), convertHalWcdmaSignalStrength(signalStrength.wcdma), convertHalTdscdmaSignalStrength(signalStrength.tdscdma), convertHalLteSignalStrength(signalStrength.lte), convertHalNrSignalStrength(signalStrength.nr));
    }

    public static CellSignalStrengthGsm convertHalGsmSignalStrength(GsmSignalStrength gsmSignalStrength) {
        if (gsmSignalStrength == null) {
            return new CellSignalStrengthGsm();
        }
        CellSignalStrengthGsm cellSignalStrengthGsm = new CellSignalStrengthGsm(CellSignalStrength.getRssiDbmFromAsu(gsmSignalStrength.signalStrength), gsmSignalStrength.bitErrorRate, gsmSignalStrength.timingAdvance);
        if (cellSignalStrengthGsm.getRssi() == Integer.MAX_VALUE) {
            cellSignalStrengthGsm.setDefaultValues();
            cellSignalStrengthGsm.updateLevel(null, null);
        }
        return cellSignalStrengthGsm;
    }

    public static CellSignalStrengthGsm convertHalGsmSignalStrength(android.hardware.radio.network.GsmSignalStrength gsmSignalStrength) {
        if (gsmSignalStrength == null) {
            return new CellSignalStrengthGsm();
        }
        CellSignalStrengthGsm cellSignalStrengthGsm = new CellSignalStrengthGsm(CellSignalStrength.getRssiDbmFromAsu(gsmSignalStrength.signalStrength), gsmSignalStrength.bitErrorRate, gsmSignalStrength.timingAdvance);
        if (cellSignalStrengthGsm.getRssi() == Integer.MAX_VALUE) {
            cellSignalStrengthGsm.setDefaultValues();
            cellSignalStrengthGsm.updateLevel(null, null);
        }
        return cellSignalStrengthGsm;
    }

    public static CellSignalStrengthCdma convertHalCdmaSignalStrength(CdmaSignalStrength cdmaSignalStrength, EvdoSignalStrength evdoSignalStrength) {
        if (cdmaSignalStrength == null || evdoSignalStrength == null) {
            return new CellSignalStrengthCdma();
        }
        return new CellSignalStrengthCdma(-cdmaSignalStrength.dbm, -cdmaSignalStrength.ecio, -evdoSignalStrength.dbm, -evdoSignalStrength.ecio, evdoSignalStrength.signalNoiseRatio);
    }

    public static CellSignalStrengthCdma convertHalCdmaSignalStrength(android.hardware.radio.network.CdmaSignalStrength cdmaSignalStrength, android.hardware.radio.network.EvdoSignalStrength evdoSignalStrength) {
        if (cdmaSignalStrength == null || evdoSignalStrength == null) {
            return new CellSignalStrengthCdma();
        }
        return new CellSignalStrengthCdma(-cdmaSignalStrength.dbm, -cdmaSignalStrength.ecio, -evdoSignalStrength.dbm, -evdoSignalStrength.ecio, evdoSignalStrength.signalNoiseRatio);
    }

    public static CellSignalStrengthLte convertHalLteSignalStrength(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof LteSignalStrength) {
            LteSignalStrength lteSignalStrength = (LteSignalStrength) obj;
            int iConvertRssiAsuToDBm = CellSignalStrengthLte.convertRssiAsuToDBm(lteSignalStrength.signalStrength);
            int i = lteSignalStrength.rsrp;
            if (i != Integer.MAX_VALUE) {
                i = -i;
            }
            int i2 = i;
            int i3 = lteSignalStrength.rsrq;
            if (i3 != Integer.MAX_VALUE) {
                i3 = -i3;
            }
            return new CellSignalStrengthLte(iConvertRssiAsuToDBm, i2, i3, CellSignalStrengthLte.convertRssnrUnitFromTenDbToDB(lteSignalStrength.rssnr), lteSignalStrength.cqi, lteSignalStrength.timingAdvance);
        }
        if (!(obj instanceof android.hardware.radio.V1_6.LteSignalStrength)) {
            return null;
        }
        android.hardware.radio.V1_6.LteSignalStrength lteSignalStrength2 = (android.hardware.radio.V1_6.LteSignalStrength) obj;
        int iConvertRssiAsuToDBm2 = CellSignalStrengthLte.convertRssiAsuToDBm(lteSignalStrength2.base.signalStrength);
        LteSignalStrength lteSignalStrength3 = lteSignalStrength2.base;
        int i4 = lteSignalStrength3.rsrp;
        if (i4 != Integer.MAX_VALUE) {
            i4 = -i4;
        }
        int i5 = i4;
        int i6 = lteSignalStrength3.rsrq;
        if (i6 != Integer.MAX_VALUE) {
            i6 = -i6;
        }
        int iConvertRssnrUnitFromTenDbToDB = CellSignalStrengthLte.convertRssnrUnitFromTenDbToDB(lteSignalStrength3.rssnr);
        int i7 = lteSignalStrength2.cqiTableIndex;
        LteSignalStrength lteSignalStrength4 = lteSignalStrength2.base;
        return new CellSignalStrengthLte(iConvertRssiAsuToDBm2, i5, i6, iConvertRssnrUnitFromTenDbToDB, i7, lteSignalStrength4.cqi, lteSignalStrength4.timingAdvance);
    }

    public static CellSignalStrengthLte convertHalLteSignalStrength(android.hardware.radio.network.LteSignalStrength lteSignalStrength) {
        int iConvertRssiAsuToDBm = CellSignalStrengthLte.convertRssiAsuToDBm(lteSignalStrength.signalStrength);
        int i = lteSignalStrength.rsrp;
        if (i != Integer.MAX_VALUE) {
            i = -i;
        }
        int i2 = lteSignalStrength.rsrq;
        if (i2 != Integer.MAX_VALUE) {
            i2 = -i2;
        }
        return new CellSignalStrengthLte(iConvertRssiAsuToDBm, i, i2, CellSignalStrengthLte.convertRssnrUnitFromTenDbToDB(lteSignalStrength.rssnr), lteSignalStrength.cqiTableIndex, lteSignalStrength.cqi, lteSignalStrength.timingAdvance);
    }

    public static CellSignalStrengthWcdma convertHalWcdmaSignalStrength(Object obj) {
        if (obj == null) {
            return null;
        }
        WcdmaSignalStrength wcdmaSignalStrength = (WcdmaSignalStrength) obj;
        CellSignalStrengthWcdma cellSignalStrengthWcdma = new CellSignalStrengthWcdma(CellSignalStrength.getRssiDbmFromAsu(wcdmaSignalStrength.base.signalStrength), wcdmaSignalStrength.base.bitErrorRate, CellSignalStrength.getRscpDbmFromAsu(wcdmaSignalStrength.rscp), CellSignalStrength.getEcNoDbFromAsu(wcdmaSignalStrength.ecno));
        if (cellSignalStrengthWcdma.getRssi() == Integer.MAX_VALUE && cellSignalStrengthWcdma.getRscp() == Integer.MAX_VALUE) {
            cellSignalStrengthWcdma.setDefaultValues();
            cellSignalStrengthWcdma.updateLevel(null, null);
        }
        return cellSignalStrengthWcdma;
    }

    public static CellSignalStrengthWcdma convertHalWcdmaSignalStrength(android.hardware.radio.network.WcdmaSignalStrength wcdmaSignalStrength) {
        CellSignalStrengthWcdma cellSignalStrengthWcdma = new CellSignalStrengthWcdma(CellSignalStrength.getRssiDbmFromAsu(wcdmaSignalStrength.signalStrength), wcdmaSignalStrength.bitErrorRate, CellSignalStrength.getRscpDbmFromAsu(wcdmaSignalStrength.rscp), CellSignalStrength.getEcNoDbFromAsu(wcdmaSignalStrength.ecno));
        if (cellSignalStrengthWcdma.getRssi() == Integer.MAX_VALUE && cellSignalStrengthWcdma.getRscp() == Integer.MAX_VALUE) {
            cellSignalStrengthWcdma.setDefaultValues();
            cellSignalStrengthWcdma.updateLevel(null, null);
        }
        return cellSignalStrengthWcdma;
    }

    public static CellSignalStrengthTdscdma convertHalTdscdmaSignalStrength(Object obj) {
        if (obj == null) {
            return null;
        }
        TdscdmaSignalStrength tdscdmaSignalStrength = (TdscdmaSignalStrength) obj;
        CellSignalStrengthTdscdma cellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(CellSignalStrength.getRssiDbmFromAsu(tdscdmaSignalStrength.signalStrength), tdscdmaSignalStrength.bitErrorRate, CellSignalStrength.getRscpDbmFromAsu(tdscdmaSignalStrength.rscp));
        if (cellSignalStrengthTdscdma.getRssi() == Integer.MAX_VALUE && cellSignalStrengthTdscdma.getRscp() == Integer.MAX_VALUE) {
            cellSignalStrengthTdscdma.setDefaultValues();
            cellSignalStrengthTdscdma.updateLevel(null, null);
        }
        return cellSignalStrengthTdscdma;
    }

    public static CellSignalStrengthTdscdma convertHalTdscdmaSignalStrength(android.hardware.radio.network.TdscdmaSignalStrength tdscdmaSignalStrength) {
        CellSignalStrengthTdscdma cellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(CellSignalStrength.getRssiDbmFromAsu(tdscdmaSignalStrength.signalStrength), tdscdmaSignalStrength.bitErrorRate, CellSignalStrength.getRscpDbmFromAsu(tdscdmaSignalStrength.rscp));
        if (cellSignalStrengthTdscdma.getRssi() == Integer.MAX_VALUE && cellSignalStrengthTdscdma.getRscp() == Integer.MAX_VALUE) {
            cellSignalStrengthTdscdma.setDefaultValues();
            cellSignalStrengthTdscdma.updateLevel(null, null);
        }
        return cellSignalStrengthTdscdma;
    }

    public static CellSignalStrengthNr convertHalNrSignalStrength(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof NrSignalStrength) {
            NrSignalStrength nrSignalStrength = (NrSignalStrength) obj;
            return new CellSignalStrengthNr(CellSignalStrengthNr.flip(nrSignalStrength.csiRsrp), CellSignalStrengthNr.flip(nrSignalStrength.csiRsrq), nrSignalStrength.csiSinr, CellSignalStrengthNr.flip(nrSignalStrength.ssRsrp), CellSignalStrengthNr.flip(nrSignalStrength.ssRsrq), nrSignalStrength.ssSinr);
        }
        if (!(obj instanceof android.hardware.radio.V1_6.NrSignalStrength)) {
            return null;
        }
        android.hardware.radio.V1_6.NrSignalStrength nrSignalStrength2 = (android.hardware.radio.V1_6.NrSignalStrength) obj;
        int iFlip = CellSignalStrengthNr.flip(nrSignalStrength2.base.csiRsrp);
        int iFlip2 = CellSignalStrengthNr.flip(nrSignalStrength2.base.csiRsrq);
        NrSignalStrength nrSignalStrength3 = nrSignalStrength2.base;
        return new CellSignalStrengthNr(iFlip, iFlip2, nrSignalStrength3.csiSinr, nrSignalStrength2.csiCqiTableIndex, nrSignalStrength2.csiCqiReport, CellSignalStrengthNr.flip(nrSignalStrength3.ssRsrp), CellSignalStrengthNr.flip(nrSignalStrength2.base.ssRsrq), nrSignalStrength2.base.ssSinr, Integer.MAX_VALUE);
    }

    public static CellSignalStrengthNr convertHalNrSignalStrength(android.hardware.radio.network.NrSignalStrength nrSignalStrength) {
        return new CellSignalStrengthNr(CellSignalStrengthNr.flip(nrSignalStrength.csiRsrp), CellSignalStrengthNr.flip(nrSignalStrength.csiRsrq), nrSignalStrength.csiSinr, nrSignalStrength.csiCqiTableIndex, primitiveArrayToArrayList(nrSignalStrength.csiCqiReport), CellSignalStrengthNr.flip(nrSignalStrength.ssRsrp), CellSignalStrengthNr.flip(nrSignalStrength.ssRsrq), nrSignalStrength.ssSinr, nrSignalStrength.timingAdvance);
    }

    private static ClosedSubscriberGroupInfo convertHalClosedSubscriberGroupInfo(OptionalCsgInfo optionalCsgInfo) {
        android.hardware.radio.V1_5.ClosedSubscriberGroupInfo closedSubscriberGroupInfoCsgInfo = optionalCsgInfo.getDiscriminator() == 1 ? optionalCsgInfo.csgInfo() : null;
        if (closedSubscriberGroupInfoCsgInfo == null) {
            return null;
        }
        return new ClosedSubscriberGroupInfo(closedSubscriberGroupInfoCsgInfo.csgIndication, closedSubscriberGroupInfoCsgInfo.homeNodebName, closedSubscriberGroupInfoCsgInfo.csgIdentity);
    }

    private static ClosedSubscriberGroupInfo convertHalClosedSubscriberGroupInfo(android.hardware.radio.network.ClosedSubscriberGroupInfo closedSubscriberGroupInfo) {
        if (closedSubscriberGroupInfo == null) {
            return null;
        }
        return new ClosedSubscriberGroupInfo(closedSubscriberGroupInfo.csgIndication, closedSubscriberGroupInfo.homeNodebName, closedSubscriberGroupInfo.csgIdentity);
    }

    public static SparseArray<BarringInfo.BarringServiceInfo> convertHalBarringInfoList(List<android.hardware.radio.V1_5.BarringInfo> list) {
        SparseArray<BarringInfo.BarringServiceInfo> sparseArray = new SparseArray<>();
        for (android.hardware.radio.V1_5.BarringInfo barringInfo : list) {
            if (barringInfo.barringType == 1) {
                if (barringInfo.barringTypeSpecificInfo.getDiscriminator() == 1) {
                    android.hardware.radio.V1_5.BarringInfo.BarringTypeSpecificInfo.Conditional conditional = barringInfo.barringTypeSpecificInfo.conditional();
                    sparseArray.put(barringInfo.serviceType, new BarringInfo.BarringServiceInfo(barringInfo.barringType, conditional.isBarred, conditional.factor, conditional.timeSeconds));
                }
            } else {
                sparseArray.put(barringInfo.serviceType, new BarringInfo.BarringServiceInfo(barringInfo.barringType, false, 0, 0));
            }
        }
        return sparseArray;
    }

    public static SparseArray<BarringInfo.BarringServiceInfo> convertHalBarringInfoList(android.hardware.radio.network.BarringInfo[] barringInfoArr) {
        SparseArray<BarringInfo.BarringServiceInfo> sparseArray = new SparseArray<>();
        for (android.hardware.radio.network.BarringInfo barringInfo : barringInfoArr) {
            if (barringInfo.barringType == 1) {
                if (barringInfo.barringTypeSpecificInfo != null) {
                    int i = barringInfo.serviceType;
                    int i2 = barringInfo.barringType;
                    BarringTypeSpecificInfo barringTypeSpecificInfo = barringInfo.barringTypeSpecificInfo;
                    sparseArray.put(i, new BarringInfo.BarringServiceInfo(i2, barringTypeSpecificInfo.isBarred, barringTypeSpecificInfo.factor, barringTypeSpecificInfo.timeSeconds));
                }
            } else {
                sparseArray.put(barringInfo.serviceType, new BarringInfo.BarringServiceInfo(barringInfo.barringType, false, 0, 0));
            }
        }
        return sparseArray;
    }

    private static int[] convertHalNetworkSecurityReasonCodeList(int[] iArr) {
        if (iArr == null) {
            return new int[0];
        }
        int[] iArr2 = new int[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr2[i] = convertHalNetworkSecurityReasonCode(iArr[i]);
        }
        return iArr2;
    }

    public static Set<NetworkSecurityEvent> convertHalNetworkSecurityEventList(android.hardware.radio.network.NetworkSecurityEvent[] networkSecurityEventArr) {
        HashSet hashSet = new HashSet();
        if (networkSecurityEventArr != null) {
            for (android.hardware.radio.network.NetworkSecurityEvent networkSecurityEvent : networkSecurityEventArr) {
                hashSet.add(convertHalNetworkSecurityEvent(networkSecurityEvent));
            }
        }
        return hashSet;
    }

    public static NetworkSecurityEvent convertHalNetworkSecurityEvent(android.hardware.radio.network.NetworkSecurityEvent networkSecurityEvent) {
        if (networkSecurityEvent == null) {
            return null;
        }
        return new NetworkSecurityEvent(convertHalNetworkSecurityAlertCategory(networkSecurityEvent.alertCategory), convertHalNetworkSecurityAlertStatus(networkSecurityEvent.alertStatus), convertHalNetworkSecurityReasonCodeList(networkSecurityEvent.reasonCodes), networkSecurityEvent.cellId, networkSecurityEvent.physicalCellId, networkSecurityEvent.arfcn, networkSecurityEvent.plmn, networkSecurityEvent.rat, networkSecurityEvent.isEmergency);
    }

    private static android.net.LinkAddress convertToLinkAddress(String str) {
        return convertToLinkAddress(str, 0, -1L, -1L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.net.LinkAddress convertToLinkAddress(String str, int i, long j, long j2) {
        InetAddress numericAddress;
        int i2;
        int i3;
        String strTrim = str.trim();
        try {
            String[] strArrSplit = strTrim.split("/", 2);
            numericAddress = InetAddresses.parseNumericAddress(strArrSplit[0]);
            try {
                if (strArrSplit.length == 1) {
                    i3 = numericAddress instanceof Inet4Address ? 32 : 128;
                } else {
                    i3 = strArrSplit.length == 2 ? Integer.parseInt(strArrSplit[1]) : -1;
                }
                i2 = i3;
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException | NumberFormatException unused) {
                i2 = -1;
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException | NumberFormatException unused2) {
            numericAddress = null;
        }
        InetAddress inetAddress = numericAddress;
        if (inetAddress == null || i2 == -1) {
            RILUtils$$ExternalSyntheticBUOutline1.m("Invalid link address ", strTrim);
            return null;
        }
        return new android.net.LinkAddress(inetAddress, i2, i, 0, j, j2);
    }

    /* JADX WARN: Code duplicated, block: B:34:0x01ee  */
    /* JADX WARN: Code duplicated, block: B:36:0x01f4  */
    /* JADX WARN: Code duplicated, block: B:44:0x022e  */
    /* JADX WARN: Code duplicated, block: B:46:0x0232  */
    /* JADX WARN: Code duplicated, block: B:54:0x026a  */
    /* JADX WARN: Code duplicated, block: B:56:0x026e  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r18v1 */
    /* JADX WARN: Type inference failed for: r18v3 */
    /* JADX WARN: Type inference failed for: r3v15, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r3v8, types: [java.util.List] */
    public static DataCallResponse convertHalDataCallResult(Object obj) {
        NetworkSliceInfo networkSliceInfoConvertHalSliceInfo;
        String[] strArr;
        int iMax;
        int i;
        int i2;
        int i3;
        Qos qos;
        String[] strArr2;
        String str;
        int i4;
        int i5;
        long j;
        int i6;
        String[] strArr3;
        List list;
        int i7;
        byte b;
        ?? r18;
        int i8;
        long j2;
        int i9;
        int i10;
        int i11;
        String str2;
        String[] strArr4;
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        int length;
        int i12;
        String strTrim;
        int length2;
        int i13;
        String strTrim2;
        int length3;
        String[] strArr5;
        int i14;
        String strTrim3;
        if (obj == null) {
            return null;
        }
        List arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        int i15 = 0;
        if (obj instanceof SetupDataCallResult) {
            SetupDataCallResult setupDataCallResult = (SetupDataCallResult) obj;
            i8 = setupDataCallResult.cause;
            j2 = setupDataCallResult.suggestedRetryTime;
            i9 = setupDataCallResult.cid;
            i10 = setupDataCallResult.active;
            i11 = setupDataCallResult.type;
            str2 = setupDataCallResult.ifname;
            String[] strArr6 = (String[]) setupDataCallResult.addresses.toArray(new String[0]);
            strArr4 = (String[]) setupDataCallResult.dnses.toArray(new String[0]);
            networkSliceInfoConvertHalSliceInfo = null;
            String[] strArr7 = (String[]) setupDataCallResult.gateways.toArray(new String[0]);
            String[] strArr8 = (String[]) setupDataCallResult.pcscf.toArray(new String[0]);
            int i16 = setupDataCallResult.mtu;
            if (strArr6 != null) {
                int length4 = strArr6.length;
                while (i15 < length4) {
                    arrayList4.add(convertToLinkAddress(strArr6[i15]));
                    i15++;
                    i16 = i16;
                }
            }
            i2 = i16;
            iMax = i2;
            i = iMax;
            i3 = 0;
            b = 0;
            strArr = strArr8;
            qos = null;
            strArr2 = strArr7;
        } else {
            networkSliceInfoConvertHalSliceInfo = null;
            if (obj instanceof android.hardware.radio.V1_5.SetupDataCallResult) {
                android.hardware.radio.V1_5.SetupDataCallResult setupDataCallResult2 = (android.hardware.radio.V1_5.SetupDataCallResult) obj;
                i8 = setupDataCallResult2.cause;
                j2 = setupDataCallResult2.suggestedRetryTime;
                i9 = setupDataCallResult2.cid;
                i10 = setupDataCallResult2.active;
                i11 = setupDataCallResult2.type;
                str2 = setupDataCallResult2.ifname;
                arrayList4 = (List) setupDataCallResult2.addresses.stream().map(new Function() { // from class: com.android.internal.telephony.RILUtils$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        LinkAddress linkAddress = (LinkAddress) obj2;
                        return RILUtils.convertToLinkAddress(linkAddress.address, linkAddress.properties, linkAddress.deprecationTime, linkAddress.expirationTime);
                    }
                }).collect(Collectors.toList());
                strArr4 = (String[]) setupDataCallResult2.dnses.toArray(new String[0]);
                strArr2 = (String[]) setupDataCallResult2.gateways.toArray(new String[0]);
                String[] strArr9 = (String[]) setupDataCallResult2.pcscf.toArray(new String[0]);
                iMax = Math.max(setupDataCallResult2.mtuV4, setupDataCallResult2.mtuV6);
                i = setupDataCallResult2.mtuV4;
                strArr = strArr9;
                qos = null;
                i3 = 0;
                b = 0;
                i2 = setupDataCallResult2.mtuV6;
            } else {
                if (!(obj instanceof android.hardware.radio.V1_6.SetupDataCallResult)) {
                    loge("Unsupported SetupDataCallResult " + obj);
                    return null;
                }
                android.hardware.radio.V1_6.SetupDataCallResult setupDataCallResult3 = (android.hardware.radio.V1_6.SetupDataCallResult) obj;
                int i17 = setupDataCallResult3.cause;
                long j3 = setupDataCallResult3.suggestedRetryTime;
                int i18 = setupDataCallResult3.cid;
                int i19 = setupDataCallResult3.active;
                int i20 = setupDataCallResult3.type;
                String str3 = setupDataCallResult3.ifname;
                List list2 = (List) setupDataCallResult3.addresses.stream().map(new Function() { // from class: com.android.internal.telephony.RILUtils$$ExternalSyntheticLambda3
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        LinkAddress linkAddress = (LinkAddress) obj2;
                        return RILUtils.convertToLinkAddress(linkAddress.address, linkAddress.properties, linkAddress.deprecationTime, linkAddress.expirationTime);
                    }
                }).collect(Collectors.toList());
                String[] strArr10 = (String[]) setupDataCallResult3.dnses.toArray(new String[0]);
                String[] strArr11 = (String[]) setupDataCallResult3.gateways.toArray(new String[0]);
                strArr = (String[]) setupDataCallResult3.pcscf.toArray(new String[0]);
                iMax = Math.max(setupDataCallResult3.mtuV4, setupDataCallResult3.mtuV6);
                i = setupDataCallResult3.mtuV4;
                i2 = setupDataCallResult3.mtuV6;
                byte b2 = setupDataCallResult3.handoverFailureMode;
                int i21 = setupDataCallResult3.pduSessionId;
                Qos qosConvertHalQos = convertHalQos(setupDataCallResult3.defaultQos);
                List list3 = (List) setupDataCallResult3.qosSessions.stream().map(new Function() { // from class: com.android.internal.telephony.RILUtils$$ExternalSyntheticLambda4
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        return RILUtils.convertHalQosBearerSession((QosSession) obj2);
                    }
                }).collect(Collectors.toList());
                networkSliceInfoConvertHalSliceInfo = setupDataCallResult3.sliceInfo.getDiscriminator() != 0 ? convertHalSliceInfo(setupDataCallResult3.sliceInfo.value()) : null;
                ArrayList arrayList7 = setupDataCallResult3.trafficDescriptors;
                int size = arrayList7.size();
                int i22 = 0;
                while (i22 < size) {
                    int i23 = i22 + 1;
                    try {
                        arrayList6.add(convertHalTrafficDescriptor((android.hardware.radio.V1_6.TrafficDescriptor) arrayList7.get(i22)));
                    } catch (IllegalArgumentException e) {
                        loge("convertHalDataCallResult: Failed to convert traffic descriptor. e=" + e);
                    }
                    size = size;
                    i22 = i23;
                    arrayList7 = arrayList7;
                }
                i3 = i21;
                qos = qosConvertHalQos;
                strArr2 = strArr11;
                arrayList6 = arrayList6;
                str = str3;
                i4 = i18;
                i5 = i17;
                j = j3;
                i6 = i20;
                strArr3 = strArr10;
                list = list2;
                i7 = i19;
                b = b2;
                r18 = list3;
            }
            arrayList = new ArrayList();
            int i24 = i2;
            int i25 = i;
            if (strArr3 != null) {
                length3 = strArr3.length;
                strArr5 = strArr3;
                i14 = 0;
                while (i14 < length3) {
                    int i26 = i14;
                    strTrim3 = strArr5[i14].trim();
                    try {
                        arrayList.add(InetAddresses.parseNumericAddress(strTrim3));
                    } catch (IllegalArgumentException e2) {
                        Rlog.e("RILUtils", "Unknown dns: " + strTrim3, e2);
                    }
                    i14 = i26 + 1;
                    length3 = length3;
                    iMax = iMax;
                }
            }
            int i27 = iMax;
            arrayList2 = new ArrayList();
            if (strArr2 != null) {
                i13 = 0;
                for (length2 = strArr2.length; i13 < length2; length2 = length2) {
                    String[] strArr12 = strArr2;
                    strTrim2 = strArr2[i13].trim();
                    try {
                        arrayList2.add(InetAddresses.parseNumericAddress(strTrim2));
                    } catch (IllegalArgumentException e3) {
                        Rlog.e("RILUtils", "Unknown gateway: " + strTrim2, e3);
                    }
                    i13++;
                    strArr2 = strArr12;
                }
            }
            arrayList3 = new ArrayList();
            if (strArr != null) {
                i12 = 0;
                for (length = strArr.length; i12 < length; length = length) {
                    String[] strArr13 = strArr;
                    strTrim = strArr[i12].trim();
                    try {
                        arrayList3.add(InetAddresses.parseNumericAddress(strTrim));
                    } catch (IllegalArgumentException e4) {
                        Rlog.e("RILUtils", "Unknown pcscf: " + strTrim, e4);
                    }
                    i12++;
                    strArr = strArr13;
                }
            }
            return new DataCallResponse.Builder().setCause(i5).setRetryDurationMillis(j).setId(i4).setLinkStatus(i7).setProtocolType(i6).setInterfaceName(str).setAddresses(list).setDnsAddresses(arrayList).setGatewayAddresses(arrayList2).setPcscfAddresses(arrayList3).setMtu(i27).setMtuV4(i25).setMtuV6(i24).setHandoverFailureMode(b).setPduSessionId(i3).setDefaultQos(qos).setQosBearerSessions((List) r18).setSliceInfo(networkSliceInfoConvertHalSliceInfo).setTrafficDescriptors(arrayList6).build();
        }
        long j4 = j2;
        list = arrayList4;
        i6 = i11;
        str = str2;
        strArr3 = strArr4;
        i4 = i9;
        i7 = i10;
        i5 = i8;
        j = j4;
        r18 = arrayList5;
        arrayList = new ArrayList();
        int i28 = i2;
        int i29 = i;
        if (strArr3 != null) {
            length3 = strArr3.length;
            strArr5 = strArr3;
            i14 = 0;
            while (i14 < length3) {
                int i210 = i14;
                strTrim3 = strArr5[i14].trim();
                arrayList.add(InetAddresses.parseNumericAddress(strTrim3));
                i14 = i210 + 1;
                length3 = length3;
                iMax = iMax;
            }
        }
        int i211 = iMax;
        arrayList2 = new ArrayList();
        if (strArr2 != null) {
            i13 = 0;
            while (i13 < length2) {
                String[] strArr14 = strArr2;
                strTrim2 = strArr2[i13].trim();
                arrayList2.add(InetAddresses.parseNumericAddress(strTrim2));
                i13++;
                strArr2 = strArr14;
            }
        }
        arrayList3 = new ArrayList();
        if (strArr != null) {
            i12 = 0;
            while (i12 < length) {
                String[] strArr15 = strArr;
                strTrim = strArr[i12].trim();
                arrayList3.add(InetAddresses.parseNumericAddress(strTrim));
                i12++;
                strArr = strArr15;
            }
        }
        return new DataCallResponse.Builder().setCause(i5).setRetryDurationMillis(j).setId(i4).setLinkStatus(i7).setProtocolType(i6).setInterfaceName(str).setAddresses(list).setDnsAddresses(arrayList).setGatewayAddresses(arrayList2).setPcscfAddresses(arrayList3).setMtu(i211).setMtuV4(i29).setMtuV6(i28).setHandoverFailureMode(b).setPduSessionId(i3).setDefaultQos(qos).setQosBearerSessions((List) r18).setSliceInfo(networkSliceInfoConvertHalSliceInfo).setTrafficDescriptors(arrayList6).build();
    }

    public static DataCallResponse convertHalDataCallResult(android.hardware.radio.data.SetupDataCallResult setupDataCallResult) {
        if (setupDataCallResult == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        android.hardware.radio.data.LinkAddress[] linkAddressArr = setupDataCallResult.addresses;
        if (linkAddressArr != null) {
            for (android.hardware.radio.data.LinkAddress linkAddress : linkAddressArr) {
                arrayList.add(convertToLinkAddress(linkAddress.address, linkAddress.addressProperties, linkAddress.deprecationTime, linkAddress.expirationTime));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        String[] strArr = setupDataCallResult.dnses;
        if (strArr != null) {
            for (String str : strArr) {
                String strTrim = str.trim();
                try {
                    arrayList2.add(InetAddresses.parseNumericAddress(strTrim));
                } catch (IllegalArgumentException e) {
                    Rlog.e("RILUtils", "Unknown dns: " + strTrim, e);
                }
            }
        }
        ArrayList arrayList3 = new ArrayList();
        String[] strArr2 = setupDataCallResult.gateways;
        if (strArr2 != null) {
            for (String str2 : strArr2) {
                String strTrim2 = str2.trim();
                try {
                    arrayList3.add(InetAddresses.parseNumericAddress(strTrim2));
                } catch (IllegalArgumentException e2) {
                    Rlog.e("RILUtils", "Unknown gateway: " + strTrim2, e2);
                }
            }
        }
        ArrayList arrayList4 = new ArrayList();
        String[] strArr3 = setupDataCallResult.pcscf;
        if (strArr3 != null) {
            for (String str3 : strArr3) {
                String strTrim3 = str3.trim();
                try {
                    arrayList4.add(InetAddresses.parseNumericAddress(strTrim3));
                } catch (IllegalArgumentException e3) {
                    Rlog.e("RILUtils", "Unknown pcscf: " + strTrim3, e3);
                }
            }
        }
        ArrayList arrayList5 = new ArrayList();
        android.hardware.radio.data.QosSession[] qosSessionArr = setupDataCallResult.qosSessions;
        if (qosSessionArr != null) {
            for (android.hardware.radio.data.QosSession qosSession : qosSessionArr) {
                arrayList5.add(convertHalQosBearerSession(qosSession));
            }
        }
        ArrayList arrayList6 = new ArrayList();
        android.hardware.radio.data.TrafficDescriptor[] trafficDescriptorArr = setupDataCallResult.trafficDescriptors;
        if (trafficDescriptorArr != null) {
            for (android.hardware.radio.data.TrafficDescriptor trafficDescriptor : trafficDescriptorArr) {
                try {
                    arrayList6.add(convertHalTrafficDescriptor(trafficDescriptor));
                } catch (IllegalArgumentException e4) {
                    loge("convertHalDataCallResult: Failed to convert traffic descriptor. e=" + e4);
                }
            }
        }
        setupDataCallResult.mtuV4 = normalizeMtu(setupDataCallResult.mtuV4);
        setupDataCallResult.mtuV6 = normalizeMtu(setupDataCallResult.mtuV6);
        DataCallResponse.Builder qosBearerSessions = new DataCallResponse.Builder().setCause(setupDataCallResult.cause).setRetryDurationMillis(setupDataCallResult.suggestedRetryTime).setId(setupDataCallResult.cid).setLinkStatus(setupDataCallResult.active).setProtocolType(setupDataCallResult.type).setInterfaceName(setupDataCallResult.ifname).setAddresses(arrayList).setDnsAddresses(arrayList2).setGatewayAddresses(arrayList3).setPcscfAddresses(arrayList4).setMtu(Math.max(setupDataCallResult.mtuV4, setupDataCallResult.mtuV6)).setMtuV4(setupDataCallResult.mtuV4).setMtuV6(setupDataCallResult.mtuV6).setHandoverFailureMode(setupDataCallResult.handoverFailureMode).setPduSessionId(setupDataCallResult.pduSessionId).setDefaultQos(convertHalQos(setupDataCallResult.defaultQos)).setQosBearerSessions(arrayList5);
        android.hardware.radio.data.SliceInfo sliceInfo = setupDataCallResult.sliceInfo;
        return qosBearerSessions.setSliceInfo(sliceInfo != null ? convertHalSliceInfo(sliceInfo) : null).setTrafficDescriptors(arrayList6).build();
    }

    private static int normalizeMtu(int i) {
        return Math.min(i, 65535) & (-2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NetworkSliceInfo convertHalSliceInfo(SliceInfo sliceInfo) {
        NetworkSliceInfo.Builder status = new NetworkSliceInfo.Builder().setSliceServiceType(sliceInfo.sst).setMappedHplmnSliceServiceType(sliceInfo.mappedHplmnSst).setStatus(convertHalSliceStatus(sliceInfo.status));
        int i = sliceInfo.sliceDifferentiator;
        if (i != -1) {
            status.setSliceDifferentiator(i).setMappedHplmnSliceDifferentiator(sliceInfo.mappedHplmnSD);
        }
        return status.build();
    }

    private static NetworkSliceInfo convertHalSliceInfo(android.hardware.radio.data.SliceInfo sliceInfo) {
        NetworkSliceInfo.Builder status = new NetworkSliceInfo.Builder().setSliceServiceType(sliceInfo.sliceServiceType).setMappedHplmnSliceServiceType(sliceInfo.mappedHplmnSst).setStatus(convertHalSliceStatus(sliceInfo.status));
        int i = sliceInfo.sliceDifferentiator;
        if (i != -1) {
            status.setSliceDifferentiator(i).setMappedHplmnSliceDifferentiator(sliceInfo.mappedHplmnSd);
        }
        return status.build();
    }

    private static TrafficDescriptor convertHalTrafficDescriptor(android.hardware.radio.V1_6.TrafficDescriptor trafficDescriptor) {
        String strValue = trafficDescriptor.dnn.getDiscriminator() == 0 ? null : trafficDescriptor.dnn.value();
        byte[] bArrArrayListToPrimitiveArray = trafficDescriptor.osAppId.getDiscriminator() != 0 ? arrayListToPrimitiveArray(trafficDescriptor.osAppId.value().osAppId) : null;
        TrafficDescriptor.Builder builder = new TrafficDescriptor.Builder();
        if (strValue != null) {
            builder.setDataNetworkName(strValue);
        }
        if (bArrArrayListToPrimitiveArray != null) {
            builder.setOsAppId(bArrArrayListToPrimitiveArray);
        }
        return builder.build();
    }

    private static TrafficDescriptor convertHalTrafficDescriptor(android.hardware.radio.data.TrafficDescriptor trafficDescriptor) {
        String str = trafficDescriptor.dnn;
        android.hardware.radio.data.OsAppId osAppId = trafficDescriptor.osAppId;
        byte[] bArr = osAppId == null ? null : osAppId.osAppId;
        TrafficDescriptor.Builder builder = new TrafficDescriptor.Builder();
        if (str != null) {
            builder.setDataNetworkName(str);
        }
        if (bArr != null) {
            builder.setOsAppId(bArr);
        }
        byte b = trafficDescriptor.connectionCapability;
        if (b != 0) {
            builder.setConnectionCapability(b & 255);
        }
        return builder.build();
    }

    /* JADX INFO: renamed from: $r8$lambda$MT-2XX3MvGTYa2Jdb1l8oNH5h0k, reason: not valid java name */
    public static /* synthetic */ UrspRule m389$r8$lambda$MT2XX3MvGTYa2Jdb1l8oNH5h0k(android.hardware.radio.V1_6.UrspRule urspRule) {
        return new UrspRule(urspRule.precedence, (List) urspRule.trafficDescriptors.stream().map(new Function() { // from class: com.android.internal.telephony.RILUtils$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return RILUtils.$r8$lambda$P096LQC0s39VamjJFG9j66WXKWc((android.hardware.radio.V1_6.TrafficDescriptor) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.RILUtils$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((TrafficDescriptor) obj);
            }
        }).collect(Collectors.toList()), (List) urspRule.routeSelectionDescriptor.stream().map(new Function() { // from class: com.android.internal.telephony.RILUtils$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return RILUtils.$r8$lambda$GUTKvErD4I1WFow2UTwnumSYxNg((RouteSelectionDescriptor) obj);
            }
        }).collect(Collectors.toList()));
    }

    public static NetworkSlicingConfig convertHalSlicingConfig(SlicingConfig slicingConfig) {
        return new NetworkSlicingConfig((List) slicingConfig.urspRules.stream().map(new Function() { // from class: com.android.internal.telephony.RILUtils$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return RILUtils.m389$r8$lambda$MT2XX3MvGTYa2Jdb1l8oNH5h0k((android.hardware.radio.V1_6.UrspRule) obj);
            }
        }).collect(Collectors.toList()), (List) slicingConfig.sliceInfo.stream().map(new RILUtils$$ExternalSyntheticLambda7()).collect(Collectors.toList()));
    }

    public static /* synthetic */ TrafficDescriptor $r8$lambda$P096LQC0s39VamjJFG9j66WXKWc(android.hardware.radio.V1_6.TrafficDescriptor trafficDescriptor) {
        try {
            return convertHalTrafficDescriptor(trafficDescriptor);
        } catch (IllegalArgumentException e) {
            loge("convertHalSlicingConfig: Failed to convert traffic descriptor. e=" + e);
            return null;
        }
    }

    public static /* synthetic */ android.telephony.data.RouteSelectionDescriptor $r8$lambda$GUTKvErD4I1WFow2UTwnumSYxNg(RouteSelectionDescriptor routeSelectionDescriptor) {
        return new android.telephony.data.RouteSelectionDescriptor(routeSelectionDescriptor.precedence, routeSelectionDescriptor.sessionType.value(), routeSelectionDescriptor.sscMode.value(), (List) routeSelectionDescriptor.sliceInfo.stream().map(new RILUtils$$ExternalSyntheticLambda7()).collect(Collectors.toList()), routeSelectionDescriptor.dnn);
    }

    public static NetworkSlicingConfig convertHalSlicingConfig(android.hardware.radio.data.SlicingConfig slicingConfig) {
        ArrayList arrayList = new ArrayList();
        android.hardware.radio.data.UrspRule[] urspRuleArr = slicingConfig.urspRules;
        int length = urspRuleArr.length;
        int i = 0;
        while (i < length) {
            android.hardware.radio.data.UrspRule urspRule = urspRuleArr[i];
            ArrayList arrayList2 = new ArrayList();
            for (android.hardware.radio.data.TrafficDescriptor trafficDescriptor : urspRule.trafficDescriptors) {
                try {
                    arrayList2.add(convertHalTrafficDescriptor(trafficDescriptor));
                } catch (IllegalArgumentException e) {
                    loge("convertHalTrafficDescriptor: " + e);
                }
            }
            ArrayList arrayList3 = new ArrayList();
            android.hardware.radio.data.RouteSelectionDescriptor[] routeSelectionDescriptorArr = urspRule.routeSelectionDescriptor;
            int length2 = routeSelectionDescriptorArr.length;
            int i2 = 0;
            while (i2 < length2) {
                android.hardware.radio.data.RouteSelectionDescriptor routeSelectionDescriptor = routeSelectionDescriptorArr[i2];
                ArrayList arrayList4 = new ArrayList();
                android.hardware.radio.data.SliceInfo[] sliceInfoArr = routeSelectionDescriptor.sliceInfo;
                int length3 = sliceInfoArr.length;
                int i3 = 0;
                while (i3 < length3) {
                    arrayList4.add(convertHalSliceInfo(sliceInfoArr[i3]));
                    i3++;
                    urspRuleArr = urspRuleArr;
                }
                arrayList3.add(new android.telephony.data.RouteSelectionDescriptor(routeSelectionDescriptor.precedence, routeSelectionDescriptor.sessionType, routeSelectionDescriptor.sscMode, arrayList4, primitiveArrayToArrayList(routeSelectionDescriptor.dnn)));
                i2++;
                urspRuleArr = urspRuleArr;
            }
            arrayList.add(new UrspRule(urspRule.precedence, arrayList2, arrayList3));
            i++;
            urspRuleArr = urspRuleArr;
        }
        ArrayList arrayList5 = new ArrayList();
        for (android.hardware.radio.data.SliceInfo sliceInfo : slicingConfig.sliceInfo) {
            arrayList5.add(convertHalSliceInfo(sliceInfo));
        }
        return new NetworkSlicingConfig(arrayList, arrayList5);
    }

    private static Qos.QosBandwidth convertHalQosBandwidth(QosBandwidth qosBandwidth) {
        return new Qos.QosBandwidth(qosBandwidth.maxBitrateKbps, qosBandwidth.guaranteedBitrateKbps);
    }

    private static Qos.QosBandwidth convertHalQosBandwidth(android.hardware.radio.data.QosBandwidth qosBandwidth) {
        return new Qos.QosBandwidth(qosBandwidth.maxBitrateKbps, qosBandwidth.guaranteedBitrateKbps);
    }

    private static Qos convertHalQos(android.hardware.radio.V1_6.Qos qos) {
        if (qos == null) {
            return null;
        }
        byte discriminator = qos.getDiscriminator();
        if (discriminator == 1) {
            EpsQos epsQosEps = qos.eps();
            return new android.telephony.data.EpsQos(convertHalQosBandwidth(epsQosEps.downlink), convertHalQosBandwidth(epsQosEps.uplink), epsQosEps.qci);
        }
        if (discriminator != 2) {
            return null;
        }
        NrQos nrQosNr = qos.nr();
        return new android.telephony.data.NrQos(convertHalQosBandwidth(nrQosNr.downlink), convertHalQosBandwidth(nrQosNr.uplink), nrQosNr.qfi, nrQosNr.fiveQi, nrQosNr.averagingWindowMs);
    }

    private static Qos convertHalQos(android.hardware.radio.data.Qos qos) {
        if (qos == null) {
            return null;
        }
        int tag = qos.getTag();
        if (tag == 1) {
            android.hardware.radio.data.EpsQos eps = qos.getEps();
            return new android.telephony.data.EpsQos(convertHalQosBandwidth(eps.downlink), convertHalQosBandwidth(eps.uplink), eps.qci);
        }
        if (tag != 2) {
            return null;
        }
        android.hardware.radio.data.NrQos nr = qos.getNr();
        int i = nr.averagingWindowMillis;
        if (i == -1) {
            i = nr.averagingWindowMs;
        }
        return new android.telephony.data.NrQos(convertHalQosBandwidth(nr.downlink), convertHalQosBandwidth(nr.uplink), nr.qfi, nr.fiveQi, i);
    }

    private static QosBearerFilter convertHalQosBearerFilter(QosFilter qosFilter) {
        QosBearerFilter.PortRange portRange;
        ArrayList arrayList = new ArrayList();
        String[] strArr = (String[]) qosFilter.localAddresses.toArray(new String[0]);
        if (strArr != null) {
            for (String str : strArr) {
                arrayList.add(convertToLinkAddress(str));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        String[] strArr2 = (String[]) qosFilter.remoteAddresses.toArray(new String[0]);
        if (strArr2 != null) {
            for (String str2 : strArr2) {
                arrayList2.add(convertToLinkAddress(str2));
            }
        }
        MaybePort maybePort = qosFilter.localPort;
        QosBearerFilter.PortRange portRange2 = null;
        if (maybePort == null || maybePort.getDiscriminator() != 1) {
            portRange = null;
        } else {
            PortRange portRangeRange = qosFilter.localPort.range();
            portRange = new QosBearerFilter.PortRange(portRangeRange.start, portRangeRange.end);
        }
        MaybePort maybePort2 = qosFilter.remotePort;
        if (maybePort2 != null && maybePort2.getDiscriminator() == 1) {
            PortRange portRangeRange2 = qosFilter.remotePort.range();
            portRange2 = new QosBearerFilter.PortRange(portRangeRange2.start, portRangeRange2.end);
        }
        QosFilter.TypeOfService typeOfService = qosFilter.tos;
        byte bValue = (typeOfService == null || typeOfService.getDiscriminator() != 1) ? (byte) -1 : qosFilter.tos.value();
        QosFilter.Ipv6FlowLabel ipv6FlowLabel = qosFilter.flowLabel;
        long jValue = -1;
        long jValue2 = (ipv6FlowLabel == null || ipv6FlowLabel.getDiscriminator() != 1) ? -1L : qosFilter.flowLabel.value();
        QosFilter.IpsecSpi ipsecSpi = qosFilter.spi;
        if (ipsecSpi != null && ipsecSpi.getDiscriminator() == 1) {
            jValue = qosFilter.spi.value();
        }
        return new QosBearerFilter(arrayList, arrayList2, portRange, portRange2, qosFilter.protocol, bValue, jValue2, jValue, qosFilter.direction, qosFilter.precedence);
    }

    private static QosBearerFilter convertHalQosBearerFilter(android.hardware.radio.data.QosFilter qosFilter) {
        QosBearerFilter.PortRange portRange;
        ArrayList arrayList = new ArrayList();
        String[] strArr = qosFilter.localAddresses;
        if (strArr != null) {
            for (String str : strArr) {
                arrayList.add(convertToLinkAddress(str));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        String[] strArr2 = qosFilter.remoteAddresses;
        if (strArr2 != null) {
            for (String str2 : strArr2) {
                arrayList2.add(convertToLinkAddress(str2));
            }
        }
        QosBearerFilter.PortRange portRange2 = null;
        if (qosFilter.localPort != null) {
            android.hardware.radio.data.PortRange portRange3 = qosFilter.localPort;
            portRange = new QosBearerFilter.PortRange(portRange3.start, portRange3.end);
        } else {
            portRange = null;
        }
        if (qosFilter.remotePort != null) {
            android.hardware.radio.data.PortRange portRange4 = qosFilter.remotePort;
            portRange2 = new QosBearerFilter.PortRange(portRange4.start, portRange4.end);
        }
        QosBearerFilter.PortRange portRange5 = portRange2;
        QosFilterTypeOfService qosFilterTypeOfService = qosFilter.tos;
        int i = (qosFilterTypeOfService == null || qosFilterTypeOfService.getTag() != 1) ? -1 : 1;
        QosFilterIpv6FlowLabel qosFilterIpv6FlowLabel = qosFilter.flowLabel;
        long j = 1;
        long j2 = -1;
        if (qosFilterIpv6FlowLabel == null || qosFilterIpv6FlowLabel.getTag() != 1) {
            j = -1;
        }
        QosFilterIpsecSpi qosFilterIpsecSpi = qosFilter.spi;
        if (qosFilterIpsecSpi != null && qosFilterIpsecSpi.getTag() == 1) {
            j2 = 1;
        }
        return new QosBearerFilter(arrayList, arrayList2, portRange, portRange5, qosFilter.protocol, i, j, j2, qosFilter.direction, qosFilter.precedence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static QosBearerSession convertHalQosBearerSession(QosSession qosSession) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = qosSession.qosFilters;
        if (arrayList2 != null) {
            int size = arrayList2.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList2.get(i);
                i++;
                arrayList.add(convertHalQosBearerFilter((QosFilter) obj));
            }
        }
        return new QosBearerSession(qosSession.qosSessionId, convertHalQos(qosSession.qos), arrayList);
    }

    private static QosBearerSession convertHalQosBearerSession(android.hardware.radio.data.QosSession qosSession) {
        ArrayList arrayList = new ArrayList();
        android.hardware.radio.data.QosFilter[] qosFilterArr = qosSession.qosFilters;
        if (qosFilterArr != null) {
            for (android.hardware.radio.data.QosFilter qosFilter : qosFilterArr) {
                arrayList.add(convertHalQosBearerFilter(qosFilter));
            }
        }
        return new QosBearerSession(qosSession.qosSessionId, convertHalQos(qosSession.qos), arrayList);
    }

    public static ArrayList<DataCallResponse> convertHalDataCallResultList(List<? extends Object> list) {
        ArrayList<DataCallResponse> arrayList = new ArrayList<>(list.size());
        Iterator<? extends Object> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(convertHalDataCallResult(it.next()));
        }
        return arrayList;
    }

    public static ArrayList<DataCallResponse> convertHalDataCallResultList(android.hardware.radio.data.SetupDataCallResult[] setupDataCallResultArr) {
        ArrayList<DataCallResponse> arrayList = new ArrayList<>(setupDataCallResultArr.length);
        for (android.hardware.radio.data.SetupDataCallResult setupDataCallResult : setupDataCallResultArr) {
            arrayList.add(convertHalDataCallResult(setupDataCallResult));
        }
        return arrayList;
    }

    public static int convertHalRadioState(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 2;
        }
        if (i == 10) {
            return 1;
        }
        RIL$$ExternalSyntheticBUOutline0.m("Unrecognized RadioState: ", i);
        return 0;
    }

    public static DriverCall convertToDriverCall(Object obj) {
        android.hardware.radio.V1_0.Call call;
        android.hardware.radio.V1_6.Call call2;
        android.hardware.radio.V1_2.Call call3;
        DriverCall driverCall = new DriverCall();
        if (obj instanceof android.hardware.radio.V1_6.Call) {
            call2 = (android.hardware.radio.V1_6.Call) obj;
            call3 = call2.base;
            call = call3.base;
        } else {
            call = null;
            if (obj instanceof android.hardware.radio.V1_2.Call) {
                call3 = (android.hardware.radio.V1_2.Call) obj;
                call = call3.base;
                call2 = null;
            } else {
                call2 = null;
                call3 = null;
            }
        }
        if (call != null) {
            driverCall.state = DriverCall.stateFromCLCC(call.state);
            driverCall.index = call.index;
            driverCall.TOA = call.toa;
            driverCall.isMpty = call.isMpty;
            driverCall.isMT = call.isMT;
            driverCall.als = call.als;
            driverCall.isVoice = call.isVoice;
            driverCall.isVoicePrivacy = call.isVoicePrivacy;
            driverCall.number = call.number;
            driverCall.numberPresentation = DriverCall.presentationFromCLIP(call.numberPresentation);
            driverCall.name = call.name;
            driverCall.namePresentation = DriverCall.presentationFromCLIP(call.namePresentation);
            if (call.uusInfo.size() == 1) {
                UUSInfo uUSInfo = new UUSInfo();
                driverCall.uusInfo = uUSInfo;
                uUSInfo.setType(((UusInfo) call.uusInfo.get(0)).uusType);
                driverCall.uusInfo.setDcs(((UusInfo) call.uusInfo.get(0)).uusDcs);
                if (!TextUtils.isEmpty(((UusInfo) call.uusInfo.get(0)).uusData)) {
                    driverCall.uusInfo.setUserData(((UusInfo) call.uusInfo.get(0)).uusData.getBytes());
                }
            }
            driverCall.number = PhoneNumberUtils.stringFromStringAndTOA(driverCall.number, driverCall.TOA);
        }
        if (call3 != null) {
            driverCall.audioQuality = call3.audioQuality;
        }
        if (call2 != null) {
            driverCall.forwardedNumber = call2.forwardedNumber;
        }
        return driverCall;
    }

    public static DriverCall convertToDriverCall(android.hardware.radio.voice.Call call) {
        DriverCall driverCall = new DriverCall();
        driverCall.state = DriverCall.stateFromCLCC(call.state);
        driverCall.index = call.index;
        driverCall.TOA = call.toa;
        driverCall.isMpty = call.isMpty;
        driverCall.isMT = call.isMT;
        driverCall.als = call.als;
        driverCall.isVoice = call.isVoice;
        driverCall.isVoicePrivacy = call.isVoicePrivacy;
        driverCall.number = call.number;
        driverCall.numberPresentation = DriverCall.presentationFromCLIP(call.numberPresentation);
        driverCall.name = call.name;
        driverCall.namePresentation = DriverCall.presentationFromCLIP(call.namePresentation);
        if (call.uusInfo.length == 1) {
            UUSInfo uUSInfo = new UUSInfo();
            driverCall.uusInfo = uUSInfo;
            uUSInfo.setType(call.uusInfo[0].uusType);
            driverCall.uusInfo.setDcs(call.uusInfo[0].uusDcs);
            if (!TextUtils.isEmpty(call.uusInfo[0].uusData)) {
                driverCall.uusInfo.setUserData(call.uusInfo[0].uusData.getBytes());
            }
        }
        driverCall.number = PhoneNumberUtils.stringFromStringAndTOA(driverCall.number, driverCall.TOA);
        driverCall.audioQuality = call.audioQuality;
        driverCall.forwardedNumber = call.forwardedNumber;
        return driverCall;
    }

    public static String convertHalOperatorStatus(int i) {
        if (i == 0) {
            return "unknown";
        }
        if (i == 1) {
            return "available";
        }
        if (i == 2) {
            return "current";
        }
        if (i == 3) {
            return "forbidden";
        }
        return "";
    }

    public static List<CarrierIdentifier> convertHalCarrierList(List<Carrier> list) {
        String str;
        String str2;
        String str3;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            String str4 = list.get(i).mcc;
            String str5 = list.get(i).mnc;
            int i2 = list.get(i).matchType;
            String str6 = list.get(i).matchData;
            String str7 = null;
            if (i2 == 1) {
                str = str6;
                str2 = null;
            } else {
                if (i2 == 2) {
                    str = null;
                    str2 = null;
                    str3 = null;
                    str7 = str6;
                } else if (i2 == 3) {
                    str2 = str6;
                    str = null;
                    str3 = null;
                } else if (i2 == 4) {
                    str3 = str6;
                    str = null;
                    str2 = null;
                } else {
                    str = null;
                    str2 = null;
                }
                arrayList.add(new CarrierIdentifier(str4, str5, str, str7, str2, str3));
            }
            str3 = str2;
            arrayList.add(new CarrierIdentifier(str4, str5, str, str7, str2, str3));
        }
        return arrayList;
    }

    public static List<CarrierIdentifier> convertHalCarrierList(android.hardware.radio.sim.Carrier[] carrierArr) {
        String str;
        String str2;
        String str3;
        ArrayList arrayList = new ArrayList();
        if (carrierArr != null) {
            for (android.hardware.radio.sim.Carrier carrier : carrierArr) {
                String str4 = carrier.mcc;
                String str5 = carrier.mnc;
                int i = carrier.matchType;
                String str6 = carrier.matchData;
                String str7 = null;
                if (i == 1) {
                    str = str6;
                    str2 = null;
                } else {
                    if (i == 2) {
                        str = null;
                        str2 = null;
                        str3 = null;
                        str7 = str6;
                    } else if (i == 3) {
                        str2 = str6;
                        str = null;
                        str3 = null;
                    } else if (i == 4) {
                        str3 = str6;
                        str = null;
                        str2 = null;
                    } else {
                        str = null;
                        str2 = null;
                    }
                    arrayList.add(new CarrierIdentifier(str4, str5, str, str7, str2, str3));
                }
                str3 = str2;
                arrayList.add(new CarrierIdentifier(str4, str5, str, str7, str2, str3));
            }
        }
        return arrayList;
    }

    public static List<android.telephony.CarrierInfo> convertAidlCarrierInfoList(CarrierInfo[] carrierInfoArr) {
        CarrierInfo[] carrierInfoArr2 = carrierInfoArr;
        ArrayList arrayList = new ArrayList();
        if (carrierInfoArr2 == null) {
            loge("convertAidlCarrierInfoList received NULL carrierInfos");
            return arrayList;
        }
        int i = 0;
        while (i < carrierInfoArr2.length) {
            CarrierInfo carrierInfo = carrierInfoArr2[i];
            String str = carrierInfo.mcc;
            String str2 = carrierInfo.mnc;
            String str3 = carrierInfo.spn;
            String str4 = carrierInfo.gid1;
            String str5 = carrierInfo.gid2;
            String str6 = carrierInfo.imsiPrefix;
            String str7 = carrierInfo.iccid;
            String str8 = carrierInfo.impi;
            List list = carrierInfo.ehplmn;
            ArrayList arrayList2 = new ArrayList();
            if (list != null) {
                for (int i2 = 0; i2 < list.size(); i2++) {
                    arrayList2.add(((Plmn) list.get(i2)).mcc + "," + ((Plmn) list.get(i2)).mnc);
                }
            } else {
                loge("convertAidlCarrierInfoList ehplmList is NULL");
            }
            arrayList.add(new android.telephony.CarrierInfo(str, str2, str3, str4, str5, str6, str7, str8, arrayList2));
            i++;
            carrierInfoArr2 = carrierInfoArr;
        }
        return arrayList;
    }

    public static List<CarrierIdentifier> convertAidlCarrierInfoListToHalCarrierList(CarrierInfo[] carrierInfoArr) {
        ArrayList arrayList = new ArrayList();
        if (carrierInfoArr != null) {
            for (CarrierInfo carrierInfo : carrierInfoArr) {
                arrayList.add(new CarrierIdentifier(carrierInfo.mcc, carrierInfo.mnc, carrierInfo.spn, carrierInfo.imsiPrefix, carrierInfo.gid1, carrierInfo.gid2));
            }
        }
        return arrayList;
    }

    public static IccCardStatus convertHalCardStatus(Object obj) {
        CardStatus cardStatus;
        android.hardware.radio.V1_0.CardStatus cardStatus2;
        if (obj instanceof CardStatus) {
            cardStatus = (CardStatus) obj;
            cardStatus2 = cardStatus.base.base.base;
        } else if (obj instanceof android.hardware.radio.V1_0.CardStatus) {
            cardStatus2 = (android.hardware.radio.V1_0.CardStatus) obj;
            cardStatus = null;
        } else {
            cardStatus = null;
            cardStatus2 = null;
        }
        IccCardStatus iccCardStatus = new IccCardStatus();
        if (cardStatus2 != null) {
            iccCardStatus.setCardState(cardStatus2.cardState);
            iccCardStatus.setUniversalPinState(cardStatus2.universalPinState);
            iccCardStatus.mGsmUmtsSubscriptionAppIndex = cardStatus2.gsmUmtsSubscriptionAppIndex;
            iccCardStatus.mCdmaSubscriptionAppIndex = -1;
            iccCardStatus.mImsSubscriptionAppIndex = cardStatus2.imsSubscriptionAppIndex;
            int size = cardStatus2.applications.size();
            if (size > 8) {
                size = 8;
            }
            iccCardStatus.mApplications = new IccCardApplicationStatus[size];
            for (int i = 0; i < size; i++) {
                AppStatus appStatus = (AppStatus) cardStatus2.applications.get(i);
                IccCardApplicationStatus iccCardApplicationStatus = new IccCardApplicationStatus();
                iccCardApplicationStatus.app_type = iccCardApplicationStatus.AppTypeFromRILInt(appStatus.appType);
                iccCardApplicationStatus.app_state = iccCardApplicationStatus.AppStateFromRILInt(appStatus.appState);
                iccCardApplicationStatus.perso_substate = iccCardApplicationStatus.PersoSubstateFromRILInt(appStatus.persoSubstate);
                iccCardApplicationStatus.aid = appStatus.aidPtr;
                iccCardApplicationStatus.app_label = appStatus.appLabelPtr;
                iccCardApplicationStatus.pin1_replaced = appStatus.pin1Replaced != 0;
                iccCardApplicationStatus.pin1 = iccCardApplicationStatus.PinStateFromRILInt(appStatus.pin1);
                iccCardApplicationStatus.pin2 = iccCardApplicationStatus.PinStateFromRILInt(appStatus.pin2);
                iccCardStatus.mApplications[i] = iccCardApplicationStatus;
            }
        }
        if (cardStatus != null) {
            IccSlotPortMapping iccSlotPortMapping = new IccSlotPortMapping();
            android.hardware.radio.V1_4.CardStatus cardStatus3 = cardStatus.base;
            android.hardware.radio.V1_2.CardStatus cardStatus4 = cardStatus3.base;
            iccSlotPortMapping.mPhysicalSlotIndex = cardStatus4.physicalSlotId;
            iccCardStatus.mSlotPortMapping = iccSlotPortMapping;
            iccCardStatus.atr = cardStatus4.atr;
            iccCardStatus.iccid = cardStatus4.iccid;
            iccCardStatus.eid = cardStatus3.eid;
            int size2 = cardStatus.applications.size();
            int i2 = size2 <= 8 ? size2 : 8;
            iccCardStatus.mApplications = new IccCardApplicationStatus[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                android.hardware.radio.V1_5.AppStatus appStatus2 = (android.hardware.radio.V1_5.AppStatus) cardStatus.applications.get(i3);
                IccCardApplicationStatus iccCardApplicationStatus2 = new IccCardApplicationStatus();
                iccCardApplicationStatus2.app_type = iccCardApplicationStatus2.AppTypeFromRILInt(appStatus2.base.appType);
                iccCardApplicationStatus2.app_state = iccCardApplicationStatus2.AppStateFromRILInt(appStatus2.base.appState);
                iccCardApplicationStatus2.perso_substate = iccCardApplicationStatus2.PersoSubstateFromRILInt(appStatus2.persoSubstate);
                AppStatus appStatus3 = appStatus2.base;
                iccCardApplicationStatus2.aid = appStatus3.aidPtr;
                iccCardApplicationStatus2.app_label = appStatus3.appLabelPtr;
                iccCardApplicationStatus2.pin1_replaced = appStatus3.pin1Replaced != 0;
                iccCardApplicationStatus2.pin1 = iccCardApplicationStatus2.PinStateFromRILInt(appStatus3.pin1);
                iccCardApplicationStatus2.pin2 = iccCardApplicationStatus2.PinStateFromRILInt(appStatus2.base.pin2);
                iccCardStatus.mApplications[i3] = iccCardApplicationStatus2;
            }
        }
        return iccCardStatus;
    }

    public static IccCardStatus convertHalCardStatus(android.hardware.radio.sim.CardStatus cardStatus) {
        IccCardStatus iccCardStatus = new IccCardStatus();
        iccCardStatus.setCardState(cardStatus.cardState);
        iccCardStatus.setMultipleEnabledProfilesMode(cardStatus.supportedMepMode);
        iccCardStatus.setUniversalPinState(cardStatus.universalPinState);
        iccCardStatus.mGsmUmtsSubscriptionAppIndex = cardStatus.gsmUmtsSubscriptionAppIndex;
        iccCardStatus.mCdmaSubscriptionAppIndex = -1;
        iccCardStatus.mImsSubscriptionAppIndex = cardStatus.imsSubscriptionAppIndex;
        iccCardStatus.atr = cardStatus.atr;
        iccCardStatus.iccid = cardStatus.iccid;
        iccCardStatus.eid = cardStatus.eid;
        int iMin = Math.min(cardStatus.applications.length, 8);
        iccCardStatus.mApplications = new IccCardApplicationStatus[iMin];
        for (int i = 0; i < iMin; i++) {
            android.hardware.radio.sim.AppStatus appStatus = cardStatus.applications[i];
            IccCardApplicationStatus iccCardApplicationStatus = new IccCardApplicationStatus();
            iccCardApplicationStatus.app_type = iccCardApplicationStatus.AppTypeFromRILInt(appStatus.appType);
            iccCardApplicationStatus.app_state = iccCardApplicationStatus.AppStateFromRILInt(appStatus.appState);
            iccCardApplicationStatus.perso_substate = iccCardApplicationStatus.PersoSubstateFromRILInt(appStatus.persoSubstate);
            iccCardApplicationStatus.aid = appStatus.aidPtr;
            iccCardApplicationStatus.app_label = appStatus.appLabelPtr;
            iccCardApplicationStatus.pin1_replaced = appStatus.pin1Replaced;
            iccCardApplicationStatus.pin1 = iccCardApplicationStatus.PinStateFromRILInt(appStatus.pin1);
            iccCardApplicationStatus.pin2 = iccCardApplicationStatus.PinStateFromRILInt(appStatus.pin2);
            iccCardStatus.mApplications[i] = iccCardApplicationStatus;
        }
        IccSlotPortMapping iccSlotPortMapping = new IccSlotPortMapping();
        SlotPortMapping slotPortMapping = cardStatus.slotMap;
        int i2 = slotPortMapping.physicalSlotId;
        iccSlotPortMapping.mPhysicalSlotIndex = i2;
        iccSlotPortMapping.mPortIndex = PortUtils.convertFromHalPortIndex(i2, slotPortMapping.portId, iccCardStatus.mCardState, iccCardStatus.mSupportedMepMode);
        iccCardStatus.mSlotPortMapping = iccSlotPortMapping;
        return iccCardStatus;
    }

    public static AdnCapacity convertHalPhonebookCapacity(PhonebookCapacity phonebookCapacity) {
        if (phonebookCapacity != null) {
            return new AdnCapacity(phonebookCapacity.maxAdnRecords, phonebookCapacity.usedAdnRecords, phonebookCapacity.maxEmailRecords, phonebookCapacity.usedEmailRecords, phonebookCapacity.maxAdditionalNumberRecords, phonebookCapacity.usedAdditionalNumberRecords, phonebookCapacity.maxNameLen, phonebookCapacity.maxNumberLen, phonebookCapacity.maxEmailLen, phonebookCapacity.maxAdditionalNumberLen);
        }
        return null;
    }

    public static AdnCapacity convertHalPhonebookCapacity(android.hardware.radio.sim.PhonebookCapacity phonebookCapacity) {
        if (phonebookCapacity != null) {
            return new AdnCapacity(phonebookCapacity.maxAdnRecords, phonebookCapacity.usedAdnRecords, phonebookCapacity.maxEmailRecords, phonebookCapacity.usedEmailRecords, phonebookCapacity.maxAdditionalNumberRecords, phonebookCapacity.usedAdditionalNumberRecords, phonebookCapacity.maxNameLen, phonebookCapacity.maxNumberLen, phonebookCapacity.maxEmailLen, phonebookCapacity.maxAdditionalNumberLen);
        }
        return null;
    }

    public static SimPhonebookRecord convertHalPhonebookRecordInfo(PhonebookRecordInfo phonebookRecordInfo) {
        ArrayList arrayList = phonebookRecordInfo.emails;
        String[] strArr = arrayList == null ? null : (String[]) arrayList.toArray(new String[arrayList.size()]);
        ArrayList arrayList2 = phonebookRecordInfo.additionalNumbers;
        return new SimPhonebookRecord(phonebookRecordInfo.recordId, phonebookRecordInfo.name, phonebookRecordInfo.number, strArr, arrayList2 != null ? (String[]) arrayList2.toArray(new String[arrayList2.size()]) : null);
    }

    public static SimPhonebookRecord convertHalPhonebookRecordInfo(android.hardware.radio.sim.PhonebookRecordInfo phonebookRecordInfo) {
        return new SimPhonebookRecord(phonebookRecordInfo.recordId, phonebookRecordInfo.name, phonebookRecordInfo.number, phonebookRecordInfo.emails, phonebookRecordInfo.additionalNumbers);
    }

    public static PhonebookRecordInfo convertToHalPhonebookRecordInfo(SimPhonebookRecord simPhonebookRecord) {
        if (simPhonebookRecord != null) {
            return simPhonebookRecord.toPhonebookRecordInfo();
        }
        return null;
    }

    public static android.hardware.radio.sim.PhonebookRecordInfo convertToHalPhonebookRecordInfoAidl(SimPhonebookRecord simPhonebookRecord) {
        if (simPhonebookRecord != null) {
            return simPhonebookRecord.toPhonebookRecordInfoAidl();
        }
        return new android.hardware.radio.sim.PhonebookRecordInfo();
    }

    public static ArrayList<IccSlotStatus> convertHalSlotStatus(Object obj) {
        ArrayList<IccSlotStatus> arrayList = new ArrayList<>();
        try {
            for (SimSlotStatus simSlotStatus : (SimSlotStatus[]) obj) {
                IccSlotStatus iccSlotStatus = new IccSlotStatus();
                iccSlotStatus.setCardState(simSlotStatus.cardState);
                int length = simSlotStatus.portInfo.length;
                iccSlotStatus.mSimPortInfos = new IccSimPortInfo[length];
                for (int i = 0; i < length; i++) {
                    IccSimPortInfo iccSimPortInfo = new IccSimPortInfo();
                    SimPortInfo simPortInfo = simSlotStatus.portInfo[i];
                    iccSimPortInfo.mIccId = simPortInfo.iccId;
                    boolean z = simPortInfo.portActive;
                    iccSimPortInfo.mLogicalSlotIndex = z ? simPortInfo.logicalSlotId : -1;
                    iccSimPortInfo.mPortActive = z;
                    iccSlotStatus.mSimPortInfos[i] = iccSimPortInfo;
                }
                iccSlotStatus.atr = simSlotStatus.atr;
                iccSlotStatus.eid = simSlotStatus.eid;
                iccSlotStatus.setMultipleEnabledProfilesMode(simSlotStatus.supportedMepMode);
                arrayList.add(iccSlotStatus);
            }
        } catch (ClassCastException unused) {
            try {
                try {
                    ArrayList arrayList2 = (ArrayList) obj;
                    int size = arrayList2.size();
                    int i2 = 0;
                    while (i2 < size) {
                        Object obj2 = arrayList2.get(i2);
                        i2++;
                        android.hardware.radio.config.V1_2.SimSlotStatus simSlotStatus2 = (android.hardware.radio.config.V1_2.SimSlotStatus) obj2;
                        IccSlotStatus iccSlotStatus2 = new IccSlotStatus();
                        iccSlotStatus2.setCardState(simSlotStatus2.base.cardState);
                        iccSlotStatus2.mSimPortInfos = new IccSimPortInfo[1];
                        IccSimPortInfo iccSimPortInfo2 = new IccSimPortInfo();
                        android.hardware.radio.config.V1_0.SimSlotStatus simSlotStatus3 = simSlotStatus2.base;
                        iccSimPortInfo2.mIccId = simSlotStatus3.iccid;
                        boolean z2 = simSlotStatus3.slotState == 1;
                        iccSimPortInfo2.mPortActive = z2;
                        iccSimPortInfo2.mLogicalSlotIndex = z2 ? simSlotStatus3.logicalSlotId : -1;
                        iccSlotStatus2.mSimPortInfos[0] = iccSimPortInfo2;
                        iccSlotStatus2.atr = simSlotStatus3.atr;
                        iccSlotStatus2.eid = simSlotStatus2.eid;
                        arrayList.add(iccSlotStatus2);
                    }
                } catch (ClassCastException unused2) {
                    ArrayList arrayList3 = (ArrayList) obj;
                    int size2 = arrayList3.size();
                    int i3 = 0;
                    while (i3 < size2) {
                        Object obj3 = arrayList3.get(i3);
                        i3++;
                        android.hardware.radio.config.V1_0.SimSlotStatus simSlotStatus4 = (android.hardware.radio.config.V1_0.SimSlotStatus) obj3;
                        IccSlotStatus iccSlotStatus3 = new IccSlotStatus();
                        iccSlotStatus3.setCardState(simSlotStatus4.cardState);
                        iccSlotStatus3.mSimPortInfos = new IccSimPortInfo[1];
                        IccSimPortInfo iccSimPortInfo3 = new IccSimPortInfo();
                        iccSimPortInfo3.mIccId = simSlotStatus4.iccid;
                        boolean z3 = simSlotStatus4.slotState == 1;
                        iccSimPortInfo3.mPortActive = z3;
                        iccSimPortInfo3.mLogicalSlotIndex = z3 ? simSlotStatus4.logicalSlotId : -1;
                        iccSlotStatus3.mSimPortInfos[0] = iccSimPortInfo3;
                        iccSlotStatus3.atr = simSlotStatus4.atr;
                        arrayList.add(iccSlotStatus3);
                    }
                }
            } catch (ClassCastException unused3) {
            }
        }
        return arrayList;
    }

    public static SlotPortMapping[] convertSimSlotsMapping(List<UiccSlotMapping> list) {
        SlotPortMapping[] slotPortMappingArr = new SlotPortMapping[list.size()];
        for (UiccSlotMapping uiccSlotMapping : list) {
            int logicalSlotIndex = uiccSlotMapping.getLogicalSlotIndex();
            SlotPortMapping slotPortMapping = new SlotPortMapping();
            slotPortMappingArr[logicalSlotIndex] = slotPortMapping;
            slotPortMapping.physicalSlotId = uiccSlotMapping.getPhysicalSlotIndex();
            slotPortMappingArr[logicalSlotIndex].portId = PortUtils.convertToHalPortIndex(uiccSlotMapping.getPhysicalSlotIndex(), uiccSlotMapping.getPortIndex());
        }
        return slotPortMappingArr;
    }

    public static ArrayList<Integer> convertSlotMappingToList(List<UiccSlotMapping> list) {
        int[] iArr = new int[list.size()];
        for (UiccSlotMapping uiccSlotMapping : list) {
            iArr[uiccSlotMapping.getLogicalSlotIndex()] = uiccSlotMapping.getPhysicalSlotIndex();
        }
        return primitiveArrayToArrayList(iArr);
    }

    public static PhoneCapability convertHalPhoneCapability(int[] iArr, Object obj) {
        byte b;
        boolean z;
        byte b2;
        byte b3;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        if (obj instanceof android.hardware.radio.config.PhoneCapability) {
            android.hardware.radio.config.PhoneCapability phoneCapability = (android.hardware.radio.config.PhoneCapability) obj;
            b2 = phoneCapability.maxActiveData;
            b3 = phoneCapability.maxActiveVoice;
            if (b3 == -1) {
                b3 = phoneCapability.maxActiveInternetData;
            }
            z = phoneCapability.isInternetLingeringSupported;
            byte[] bArr = phoneCapability.logicalModemIds;
            int length = bArr.length;
            while (i < length) {
                arrayList.add(new ModemInfo(bArr[i]));
                i++;
            }
        } else {
            if (obj instanceof android.hardware.radio.config.V1_1.PhoneCapability) {
                android.hardware.radio.config.V1_1.PhoneCapability phoneCapability2 = (android.hardware.radio.config.V1_1.PhoneCapability) obj;
                b2 = phoneCapability2.maxActiveData;
                b3 = phoneCapability2.maxActiveInternetData;
                z = phoneCapability2.isInternetLingeringSupported;
                ArrayList arrayList2 = phoneCapability2.logicalModemList;
                int size = arrayList2.size();
                while (i < size) {
                    Object obj2 = arrayList2.get(i);
                    i++;
                    arrayList.add(new ModemInfo(((android.hardware.radio.config.V1_1.ModemInfo) obj2).modemId));
                }
            } else {
                b = 0;
                z = false;
            }
            return new PhoneCapability(i, b, arrayList, z, iArr);
        }
        i = b3;
        b = b2;
        return new PhoneCapability(i, b, arrayList, z, iArr);
    }

    public static EmergencyNetworkScanTrigger convertEmergencyNetworkScanTrigger(int[] iArr, int i) {
        int[] iArr2 = new int[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr2[i2] = convertToHalAccessNetworkAidl(iArr[i2]);
        }
        EmergencyNetworkScanTrigger emergencyNetworkScanTrigger = new EmergencyNetworkScanTrigger();
        emergencyNetworkScanTrigger.accessNetwork = iArr2;
        emergencyNetworkScanTrigger.scanType = convertEmergencyScanType(i);
        return emergencyNetworkScanTrigger;
    }

    public static EmergencyRegistrationResult convertHalEmergencyRegResult(EmergencyRegResult emergencyRegResult) {
        int i = emergencyRegResult.accessNetwork;
        int iConvertHalRegState = convertHalRegState(emergencyRegResult.regState);
        int i2 = emergencyRegResult.emcDomain;
        boolean z = emergencyRegResult.isVopsSupported;
        boolean z2 = emergencyRegResult.isEmcBearerSupported;
        byte b = emergencyRegResult.nwProvidedEmc;
        byte b2 = emergencyRegResult.nwProvidedEmf;
        String str = emergencyRegResult.mcc;
        String str2 = emergencyRegResult.mnc;
        return new EmergencyRegistrationResult(i, iConvertHalRegState, i2, z, z2, b, b2, str, str2, getCountryCodeForMccMnc(str, str2));
    }

    private static String getCountryCodeForMccMnc(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = ServiceStateTracker.INVALID_MCC;
        }
        return MccTable.geoCountryCodeForMccMnc(MccTable.MccMnc.fromOperatorNumeric(TextUtils.concat(str, str2).toString()));
    }

    public static String accessNetworkTypesToString(int[] iArr) {
        int length = iArr.length;
        StringBuilder sb = new StringBuilder("{");
        if (length > 0) {
            sb.append((String) Arrays.stream(iArr).mapToObj(new IntFunction() { // from class: com.android.internal.telephony.RILUtils$$ExternalSyntheticLambda5
                @Override // java.util.function.IntFunction
                public final Object apply(int i) {
                    return RILUtils.accessNetworkTypeToString(i);
                }
            }).collect(Collectors.joining(",")));
        }
        sb.append("}");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String accessNetworkTypeToString(int i) {
        switch (i) {
            case 0:
                return SatelliteConstants.DEFAULT_PLMN;
            case 1:
                return "GERAN";
            case 2:
                return "UTRAN";
            case 3:
                return "EUTRAN";
            case 4:
                return "CDMA2000";
            case 5:
                return "IWLAN";
            case 6:
                return "NGRAN";
            default:
                return Integer.toString(i);
        }
    }

    public static String scanTypeToString(int i) {
        if (i == 1) {
            return "LIMITED_SERVICE";
        }
        if (i == 2) {
            return "FULL_SERVICE";
        }
        return "NO_PREFERENCE";
    }

    public static ImsService[] convertImsServices(Set<Integer> set, Set<Integer> set2) {
        int size = set.size() + set2.size();
        if (size == 0) {
            return new ImsService[0];
        }
        ImsService[] imsServiceArr = new ImsService[size];
        int i = 0;
        for (Integer num : set) {
            ImsService imsService = new ImsService();
            imsServiceArr[i] = imsService;
            imsService.serviceType = convertToImsServiceTypeAidl(num.intValue());
            imsServiceArr[i].roamingType = 0;
            i++;
        }
        for (Integer num2 : set2) {
            ImsService imsService2 = new ImsService();
            imsServiceArr[i] = imsService2;
            imsService2.serviceType = convertToImsServiceTypeAidl(num2.intValue());
            imsServiceArr[i].roamingType = 1;
            i++;
        }
        return imsServiceArr;
    }

    public static ImsDataNetworkInfo convertToHalImsDataNetworkInfo(int i, int i2, int i3, int i4) {
        ImsDataNetworkInfo imsDataNetworkInfo = new ImsDataNetworkInfo();
        imsDataNetworkInfo.accessNetwork = i;
        imsDataNetworkInfo.dataNetworkState = convertToHalDataNetworkState(i2);
        imsDataNetworkInfo.physicalTransportType = i3;
        imsDataNetworkInfo.physicalNetworkModemId = i4;
        return imsDataNetworkInfo;
    }

    public static int convertToHalDataNetworkState(int i) {
        if (i == -1) {
            return -1;
        }
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                if (i == 4) {
                    return 4;
                }
                RILUtils$$ExternalSyntheticBUOutline0.m("Unsupported data state: ", i);
                return 0;
            }
        }
        return i2;
    }

    public static void appendPrimitiveArrayToArrayList(byte[] bArr, ArrayList<Byte> arrayList) {
        for (byte b : bArr) {
            arrayList.add(Byte.valueOf(b));
        }
    }

    public static ArrayList<Byte> primitiveArrayToArrayList(byte[] bArr) {
        ArrayList<Byte> arrayList = new ArrayList<>(bArr.length);
        for (byte b : bArr) {
            arrayList.add(Byte.valueOf(b));
        }
        return arrayList;
    }

    public static ArrayList<Integer> primitiveArrayToArrayList(int[] iArr) {
        ArrayList<Integer> arrayList = new ArrayList<>(iArr.length);
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }

    public static ArrayList<String> primitiveArrayToArrayList(String[] strArr) {
        return new ArrayList<>(Arrays.asList(strArr));
    }

    public static byte[] arrayListToPrimitiveArray(ArrayList<Byte> arrayList) {
        int size = arrayList.size();
        byte[] bArr = new byte[size];
        for (int i = 0; i < size; i++) {
            bArr[i] = arrayList.get(i).byteValue();
        }
        return bArr;
    }

    public static String convertNullToEmptyString(String str) {
        return str != null ? str : "";
    }

    public static String getRedactedPin(String str) {
        if (TextUtils.isEmpty(str)) {
            return "[]";
        }
        return "[" + "*".repeat(str.length()) + ']';
    }

    public static String setupDataReasonToString(int i) {
        if (i == 0) {
            return SatelliteConstants.DEFAULT_PLMN;
        }
        if (i == 1) {
            return "NORMAL";
        }
        if (i == 3) {
            return "HANDOVER";
        }
        return "UNKNOWN(" + i + ")";
    }

    public static String deactivateDataReasonToString(int i) {
        if (i == 0) {
            return SatelliteConstants.DEFAULT_PLMN;
        }
        if (i == 1) {
            return "NORMAL";
        }
        if (i == 2) {
            return "SHUTDOWN";
        }
        if (i == 3) {
            return "HANDOVER";
        }
        return "UNKNOWN(" + i + ")";
    }

    public static String requestToString(int i) {
        switch (i) {
            case 1:
                return "GET_SIM_STATUS";
            case 2:
                return "ENTER_SIM_PIN";
            case 3:
                return "ENTER_SIM_PUK";
            case 4:
                return "ENTER_SIM_PIN2";
            case 5:
                return "ENTER_SIM_PUK2";
            case 6:
                return "CHANGE_SIM_PIN";
            case 7:
                return "CHANGE_SIM_PIN2";
            case 8:
                return "ENTER_NETWORK_DEPERSONALIZATION";
            case 9:
                return "GET_CURRENT_CALLS";
            case 10:
                return "DIAL";
            case 11:
                return "GET_IMSI";
            case 12:
                return "HANGUP";
            case 13:
                return "HANGUP_WAITING_OR_BACKGROUND";
            case 14:
                return "HANGUP_FOREGROUND_RESUME_BACKGROUND";
            case 15:
                return "REQUEST_SWITCH_WAITING_OR_HOLDING_AND_ACTIVE";
            case 16:
                return "CONFERENCE";
            case 17:
                return "UDUB";
            case 18:
                return "LAST_CALL_FAIL_CAUSE";
            case 19:
                return "SIGNAL_STRENGTH";
            case 20:
                return "VOICE_REGISTRATION_STATE";
            case 21:
                return "DATA_REGISTRATION_STATE";
            case 22:
                return "OPERATOR";
            case 23:
                return "RADIO_POWER";
            case 24:
                return "DTMF";
            case 25:
                return "SEND_SMS";
            case 26:
                return "SEND_SMS_EXPECT_MORE";
            case 27:
                return "SETUP_DATA_CALL";
            case 28:
                return "SIM_IO";
            case 29:
                return "SEND_USSD";
            case 30:
                return "CANCEL_USSD";
            case 31:
                return "GET_CLIR";
            case 32:
                return "SET_CLIR";
            case 33:
                return "QUERY_CALL_FORWARD_STATUS";
            case 34:
                return "SET_CALL_FORWARD";
            case 35:
                return "QUERY_CALL_WAITING";
            case 36:
                return "SET_CALL_WAITING";
            case 37:
                return "SMS_ACKNOWLEDGE";
            case 38:
                return "GET_IMEI";
            case 39:
                return "GET_IMEISV";
            case 40:
                return "ANSWER";
            case 41:
                return "DEACTIVATE_DATA_CALL";
            case 42:
                return "QUERY_FACILITY_LOCK";
            case 43:
                return "SET_FACILITY_LOCK";
            case 44:
                return "CHANGE_BARRING_PASSWORD";
            case 45:
                return "QUERY_NETWORK_SELECTION_MODE";
            case 46:
                return "SET_NETWORK_SELECTION_AUTOMATIC";
            case 47:
                return "SET_NETWORK_SELECTION_MANUAL";
            case 48:
                return "QUERY_AVAILABLE_NETWORKS ";
            case 49:
                return "DTMF_START";
            case 50:
                return "DTMF_STOP";
            case 51:
                return "BASEBAND_VERSION";
            case 52:
                return "SEPARATE_CONNECTION";
            case 53:
                return "SET_MUTE";
            case 54:
                return "GET_MUTE";
            case 55:
                return "QUERY_CLIP";
            case 56:
                return "LAST_DATA_CALL_FAIL_CAUSE";
            case 57:
                return "DATA_CALL_LIST";
            case 58:
                return "RESET_RADIO";
            case 59:
                return "OEM_HOOK_RAW";
            case 60:
                return "OEM_HOOK_STRINGS";
            case 61:
                return "SCREEN_STATE";
            case 62:
                return "SET_SUPP_SVC_NOTIFICATION";
            case 63:
                return "WRITE_SMS_TO_SIM";
            case 64:
                return "DELETE_SMS_ON_SIM";
            case 65:
                return "SET_BAND_MODE";
            case 66:
                return "QUERY_AVAILABLE_BAND_MODE";
            case 67:
                return "STK_GET_PROFILE";
            case 68:
                return "STK_SET_PROFILE";
            case 69:
                return "STK_SEND_ENVELOPE_COMMAND";
            case 70:
                return "STK_SEND_TERMINAL_RESPONSE";
            case 71:
                return "STK_HANDLE_CALL_SETUP_REQUESTED_FROM_SIM";
            case 72:
                return "EXPLICIT_CALL_TRANSFER";
            case 73:
                return "SET_PREFERRED_NETWORK_TYPE";
            case 74:
                return "GET_PREFERRED_NETWORK_TYPE";
            case 75:
                return "GET_NEIGHBORING_CELL_IDS";
            case 76:
                return "SET_LOCATION_UPDATES";
            case 77:
                return "CDMA_SET_SUBSCRIPTION_SOURCE";
            case 78:
                return "CDMA_SET_ROAMING_PREFERENCE";
            case 79:
                return "CDMA_QUERY_ROAMING_PREFERENCE";
            case 80:
                return "SET_TTY_MODE";
            case 81:
                return "QUERY_TTY_MODE";
            case 82:
                return "CDMA_SET_PREFERRED_VOICE_PRIVACY_MODE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_QUERY_PREFERRED_VOICE_PRIVACY_MODE /* 83 */:
                return "CDMA_QUERY_PREFERRED_VOICE_PRIVACY_MODE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_FLASH /* 84 */:
                return "CDMA_FLASH";
            case 85:
                return "CDMA_BURST_DTMF";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_VALIDATE_AND_WRITE_AKEY /* 86 */:
                return "CDMA_VALIDATE_AND_WRITE_AKEY";
            case 87:
                return "CDMA_SEND_SMS";
            case 88:
                return "CDMA_SMS_ACKNOWLEDGE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GSM_GET_BROADCAST_CONFIG /* 89 */:
                return "GSM_GET_BROADCAST_CONFIG";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GSM_SET_BROADCAST_CONFIG /* 90 */:
                return "GSM_SET_BROADCAST_CONFIG";
            case 91:
                return "GSM_BROADCAST_ACTIVATION";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_BROADCAST_CONFIG /* 92 */:
                return "CDMA_GET_BROADCAST_CONFIG";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_SET_BROADCAST_CONFIG /* 93 */:
                return "CDMA_SET_BROADCAST_CONFIG";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_BROADCAST_ACTIVATION /* 94 */:
                return "CDMA_BROADCAST_ACTIVATION";
            case 95:
                return "CDMA_SUBSCRIPTION";
            case 96:
                return "CDMA_WRITE_SMS_TO_RUIM";
            case 97:
                return "CDMA_DELETE_SMS_ON_RUIM";
            case 98:
                return "DEVICE_IDENTITY";
            case 99:
                return "EXIT_EMERGENCY_CALLBACK_MODE";
            case 100:
                return "GET_SMSC_ADDRESS";
            case 101:
                return "SET_SMSC_ADDRESS";
            case 102:
                return "REPORT_SMS_MEMORY_STATUS";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                return "REPORT_STK_SERVICE_IS_RUNNING";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                return "CDMA_GET_SUBSCRIPTION_SOURCE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION /* 105 */:
                return "ISIM_AUTHENTICATION";
            case 106:
                return "ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS /* 107 */:
                return "STK_SEND_ENVELOPE_WITH_STATUS";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH /* 108 */:
                return "VOICE_RADIO_TECH";
            case 109:
                return "GET_CELL_INFO_LIST";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE /* 110 */:
                return "SET_CELL_INFO_LIST_RATE";
            case 111:
                return "SET_INITIAL_ATTACH_APN";
            case 112:
                return "IMS_REGISTRATION_STATE";
            case 113:
                return "IMS_SEND_SMS";
            case 114:
                return "SIM_TRANSMIT_APDU_BASIC";
            case 115:
                return "SIM_OPEN_CHANNEL";
            case 116:
                return "SIM_CLOSE_CHANNEL";
            case 117:
                return "SIM_TRANSMIT_APDU_CHANNEL";
            case 118:
                return "NV_READ_ITEM";
            case 119:
                return "NV_WRITE_ITEM";
            case 120:
                return "NV_WRITE_CDMA_PRL";
            case 121:
                return "NV_RESET_CONFIG";
            case 122:
                return "SET_UICC_SUBSCRIPTION";
            case 123:
                return "ALLOW_DATA";
            case 124:
                return "GET_HARDWARE_CONFIG";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SIM_AUTHENTICATION /* 125 */:
                return "SIM_AUTHENTICATION";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_DC_RT_INFO /* 126 */:
                return "GET_DC_RT_INFO";
            case 127:
                return "SET_DC_RT_INFO_RATE";
            case 128:
                return "SET_DATA_PROFILE";
            case 129:
                return "SHUTDOWN";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY /* 130 */:
                return "GET_RADIO_CAPABILITY";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY /* 131 */:
                return "SET_RADIO_CAPABILITY";
            case 132:
                return "START_LCE";
            case 133:
                return "STOP_LCE";
            case 134:
                return "PULL_LCEDATA";
            case 135:
                return "GET_ACTIVITY_INFO";
            case 136:
                return "SET_ALLOWED_CARRIERS";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ALLOWED_CARRIERS /* 137 */:
                return "GET_ALLOWED_CARRIERS";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_DEVICE_STATE /* 138 */:
                return "SEND_DEVICE_STATE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOLICITED_RESPONSE_FILTER /* 139 */:
                return "SET_UNSOLICITED_RESPONSE_FILTER";
            case 140:
                return "SET_SIM_CARD_POWER";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_CARRIER_INFO_IMSI_ENCRYPTION /* 141 */:
                return "SET_CARRIER_INFO_IMSI_ENCRYPTION";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_START_NETWORK_SCAN /* 142 */:
                return "START_NETWORK_SCAN";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_NETWORK_SCAN /* 143 */:
                return "STOP_NETWORK_SCAN";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                return "START_KEEPALIVE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_KEEPALIVE /* 145 */:
                return "STOP_KEEPALIVE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ICCID /* 146 */:
                return "ENABLE_MODEM";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SAR_MODE /* 147 */:
                return "GET_MODEM_STATUS";
            case 148:
                return "CDMA_SEND_SMS_EXPECT_MORE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMSI /* 149 */:
                return "GET_SIM_PHONEBOOK_CAPACITY";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_SIM_AUTH_RESPONSE /* 150 */:
                return "GET_SIM_PHONEBOOK_RECORDS";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_UPDATE_SIM_PHONEBOOK_RECORD /* 151 */:
                return "UPDATE_SIM_PHONEBOOK_RECORD";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_DEVICE_IMEI /* 152 */:
                return "DEVICE_IMEI";
            default:
                switch (i) {
                    case 200:
                        return "GET_SLOT_STATUS";
                    case 201:
                        return "SET_LOGICAL_TO_PHYSICAL_SLOT_MAPPING";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA /* 202 */:
                        return "SET_SIGNAL_STRENGTH_REPORTING_CRITERIA";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_LINK_CAPACITY_REPORTING_CRITERIA /* 203 */:
                        return "SET_LINK_CAPACITY_REPORTING_CRITERIA";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_PREFERRED_DATA_MODEM /* 204 */:
                        return "SET_PREFERRED_DATA_MODEM";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_EMERGENCY_DIAL /* 205 */:
                        return "EMERGENCY_DIAL";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_PHONE_CAPABILITY /* 206 */:
                        return "GET_PHONE_CAPABILITY";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SWITCH_DUAL_SIM_CONFIG /* 207 */:
                        return "SWITCH_DUAL_SIM_CONFIG";
                    case 208:
                        return "ENABLE_UICC_APPLICATIONS";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_UICC_APPLICATIONS_ENABLEMENT /* 209 */:
                        return "GET_UICC_APPLICATIONS_ENABLEMENT";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SYSTEM_SELECTION_CHANNELS /* 210 */:
                        return "SET_SYSTEM_SELECTION_CHANNELS";
                    case 211:
                        return "GET_BARRING_INFO";
                    case 212:
                        return "ENTER_SIM_DEPERSONALIZATION";
                    case 213:
                        return "ENABLE_NR_DUAL_CONNECTIVITY";
                    case 214:
                        return "IS_NR_DUAL_CONNECTIVITY_ENABLED";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_ALLOCATE_PDU_SESSION_ID /* 215 */:
                        return "ALLOCATE_PDU_SESSION_ID";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_RELEASE_PDU_SESSION_ID /* 216 */:
                        return "RELEASE_PDU_SESSION_ID";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_START_HANDOVER /* 217 */:
                        return "START_HANDOVER";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_CANCEL_HANDOVER /* 218 */:
                        return "CANCEL_HANDOVER";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SYSTEM_SELECTION_CHANNELS /* 219 */:
                        return "GET_SYSTEM_SELECTION_CHANNELS";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_HAL_DEVICE_CAPABILITIES /* 220 */:
                        return "GET_HAL_DEVICE_CAPABILITIES";
                    case 221:
                        return "SET_DATA_THROTTLING";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_ALLOWED_NETWORK_TYPES_BITMAP /* 222 */:
                        return "SET_ALLOWED_NETWORK_TYPES_BITMAP";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ALLOWED_NETWORK_TYPES_BITMAP /* 223 */:
                        return "GET_ALLOWED_NETWORK_TYPES_BITMAP";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SLICING_CONFIG /* 224 */:
                        return "GET_SLICING_CONFIG";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_ENABLE_VONR /* 225 */:
                        return "ENABLE_VONR";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_IS_VONR_ENABLED /* 226 */:
                        return "IS_VONR_ENABLED";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_USAGE_SETTING /* 227 */:
                        return "SET_USAGE_SETTING";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_USAGE_SETTING /* 228 */:
                        return "GET_USAGE_SETTING";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_EMERGENCY_MODE /* 229 */:
                        return "SET_EMERGENCY_MODE";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_TRIGGER_EMERGENCY_NETWORK_SCAN /* 230 */:
                        return "TRIGGER_EMERGENCY_NETWORK_SCAN";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_CANCEL_EMERGENCY_NETWORK_SCAN /* 231 */:
                        return "CANCEL_EMERGENCY_NETWORK_SCAN";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_EXIT_EMERGENCY_MODE /* 232 */:
                        return "EXIT_EMERGENCY_MODE";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SRVCC_CALL_INFO /* 233 */:
                        return "SET_SRVCC_CALL_INFO";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_UPDATE_IMS_REGISTRATION_INFO /* 234 */:
                        return "UPDATE_IMS_REGISTRATION_INFO";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_START_IMS_TRAFFIC /* 235 */:
                        return "START_IMS_TRAFFIC";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_IMS_TRAFFIC /* 236 */:
                        return "STOP_IMS_TRAFFIC";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_ANBR_QUERY /* 237 */:
                        return "SEND_ANBR_QUERY";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_TRIGGER_EPS_FALLBACK /* 238 */:
                        return "TRIGGER_EPS_FALLBACK";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_NULL_CIPHER_AND_INTEGRITY_ENABLED /* 239 */:
                        return "SET_NULL_CIPHER_AND_INTEGRITY_ENABLED";
                    case 240:
                        return "UPDATE_IMS_CALL_STATUS";
                    case 241:
                        return "SET_N1_MODE_ENABLED";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_IS_N1_MODE_ENABLED /* 242 */:
                        return "IS_N1_MODE_ENABLED";
                    case 243:
                        return "SET_LOCATION_PRIVACY_SETTING";
                    case 244:
                        return "GET_LOCATION_PRIVACY_SETTING";
                    case 245:
                        return "IS_NULL_CIPHER_AND_INTEGRITY_ENABLED";
                    case 246:
                        return "IS_CELLULAR_IDENTIFIER_DISCLOSED_ENABLED";
                    case CallFailCause.RADIO_OFF /* 247 */:
                        return "SET_CELLULAR_IDENTIFIER_DISCLOSED_ENABLED";
                    case 248:
                        return "SET_SECURITY_ALGORITHMS_UPDATED_ENABLED";
                    case CallFailCause.NO_VALID_SIM /* 249 */:
                        return "IS_SECURITY_ALGORITHMS_UPDATED_ENABLED";
                    case CallFailCause.RADIO_INTERNAL_ERROR /* 250 */:
                        return "GET_SIMULTANEOUS_CALLING_SUPPORT";
                    case CallFailCause.NETWORK_RESP_TIMEOUT /* 251 */:
                        return "SET_SATELLITE_PLMN";
                    case CallFailCause.NETWORK_REJECT /* 252 */:
                        return "SET_SATELLITE_ENABLED_FOR_CARRIER";
                    case CallFailCause.RADIO_ACCESS_FAILURE /* 253 */:
                        return "IS_SATELLITE_ENABLED_FOR_CARRIER";
                    case CallFailCause.RADIO_LINK_FAILURE /* 254 */:
                        return "SET_USER_DATA_ENABLED";
                    case 255:
                        return "SET_USER_DATA_ROAMING_ENABLED";
                    case CallFailCause.RADIO_UPLINK_FAILURE /* 256 */:
                        return "UPDATE_ALLOWED_IMS_SERVICES";
                    case CallFailCause.RADIO_SETUP_FAILURE /* 257 */:
                        return "NOTIFY_IMS_DATA_NETWORK";
                    case CallFailCause.RADIO_RELEASE_NORMAL /* 258 */:
                        return "SET_SIM_TYPE";
                    case CallFailCause.RADIO_RELEASE_ABNORMAL /* 259 */:
                        return "GET_SIM_TYPE_INFO";
                    case CallFailCause.ACCESS_CLASS_BLOCKED /* 260 */:
                        return "GET_SUPPORTED_NETWORK_ALERT_CATEGORIES";
                    case CallFailCause.NETWORK_DETACH /* 261 */:
                        return "SET_SATELLITE_NETWORK_INFO";
                    case 262:
                        return "START_PRIORITIZED_NETWORK_SCAN";
                    case 263:
                        return "STOP_PRIORITIZED_NETWORK_SCAN";
                    case 264:
                        return "REBOOT_MODEM";
                    default:
                        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
                            try {
                                return (String) Class.forName("com.mediatek.internal.telephony.MtkRIL").getDeclaredMethod("requestToStringEx", Integer.class).invoke(null, Integer.valueOf(i));
                            } catch (Exception unused) {
                                return "<unknown request " + i + ">";
                            }
                        }
                        return "<unknown request " + i + ">";
                }
        }
    }

    public static String responseToString(int i) {
        switch (i) {
            case 1000:
                return "UNSOL_RESPONSE_RADIO_STATE_CHANGED";
            case 1001:
                return "UNSOL_RESPONSE_CALL_STATE_CHANGED";
            case 1002:
                return "UNSOL_RESPONSE_NETWORK_STATE_CHANGED";
            case 1003:
                return "UNSOL_RESPONSE_NEW_SMS";
            case 1004:
                return "UNSOL_RESPONSE_NEW_SMS_STATUS_REPORT";
            case 1005:
                return "UNSOL_RESPONSE_NEW_SMS_ON_SIM";
            case 1006:
                return "UNSOL_ON_USSD";
            case 1007:
                return "UNSOL_ON_USSD_REQUEST";
            case 1008:
                return "UNSOL_NITZ_TIME_RECEIVED";
            case 1009:
                return "UNSOL_SIGNAL_STRENGTH";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_DATA_CALL_LIST_CHANGED /* 1010 */:
                return "UNSOL_DATA_CALL_LIST_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_SUPP_SVC_NOTIFICATION /* 1011 */:
                return "UNSOL_SUPP_SVC_NOTIFICATION";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_STK_SESSION_END /* 1012 */:
                return "UNSOL_STK_SESSION_END";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_STK_PROACTIVE_COMMAND /* 1013 */:
                return "UNSOL_STK_PROACTIVE_COMMAND";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_STK_EVENT_NOTIFY /* 1014 */:
                return "UNSOL_STK_EVENT_NOTIFY";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_STK_CALL_SETUP /* 1015 */:
                return "UNSOL_STK_CALL_SETUP";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_SMS_STORAGE_FULL /* 1016 */:
                return "UNSOL_SIM_SMS_STORAGE_FULL";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_REFRESH /* 1017 */:
                return "UNSOL_SIM_REFRESH";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CALL_RING /* 1018 */:
                return "UNSOL_CALL_RING";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED /* 1019 */:
                return "UNSOL_RESPONSE_SIM_STATUS_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_CDMA_NEW_SMS /* 1020 */:
                return "UNSOL_RESPONSE_CDMA_NEW_SMS";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS /* 1021 */:
                return "UNSOL_RESPONSE_NEW_BROADCAST_SMS";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL /* 1022 */:
                return "UNSOL_CDMA_RUIM_SMS_STORAGE_FULL";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RESTRICTED_STATE_CHANGED /* 1023 */:
                return "UNSOL_RESTRICTED_STATE_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE /* 1024 */:
                return "UNSOL_ENTER_EMERGENCY_CALLBACK_MODE";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CDMA_CALL_WAITING /* 1025 */:
                return "UNSOL_CDMA_CALL_WAITING";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CDMA_OTA_PROVISION_STATUS /* 1026 */:
                return "UNSOL_CDMA_OTA_PROVISION_STATUS";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CDMA_INFO_REC /* 1027 */:
                return "UNSOL_CDMA_INFO_REC";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_OEM_HOOK_RAW /* 1028 */:
                return "UNSOL_OEM_HOOK_RAW";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RINGBACK_TONE /* 1029 */:
                return "UNSOL_RINGBACK_TONE";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RESEND_INCALL_MUTE /* 1030 */:
                return "UNSOL_RESEND_INCALL_MUTE";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED /* 1031 */:
                return "UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CDMA_PRL_CHANGED /* 1032 */:
                return "UNSOL_CDMA_PRL_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE /* 1033 */:
                return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RIL_CONNECTED /* 1034 */:
                return "UNSOL_RIL_CONNECTED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_VOICE_RADIO_TECH_CHANGED /* 1035 */:
                return "UNSOL_VOICE_RADIO_TECH_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CELL_INFO_LIST /* 1036 */:
                return "UNSOL_CELL_INFO_LIST";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED /* 1037 */:
                return "UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_UICC_SUBSCRIPTION_STATUS_CHANGED /* 1038 */:
                return "UNSOL_UICC_SUBSCRIPTION_STATUS_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_SRVCC_STATE_NOTIFY /* 1039 */:
                return "UNSOL_SRVCC_STATE_NOTIFY";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_HARDWARE_CONFIG_CHANGED /* 1040 */:
                return "UNSOL_HARDWARE_CONFIG_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_DC_RT_INFO_CHANGED /* 1041 */:
                return "UNSOL_DC_RT_INFO_CHANGED";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_RADIO_CAPABILITY /* 1042 */:
                return "UNSOL_RADIO_CAPABILITY";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_ON_SS /* 1043 */:
                return "UNSOL_ON_SS";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_STK_CC_ALPHA_NOTIFY /* 1044 */:
                return "UNSOL_STK_CC_ALPHA_NOTIFY";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_LCEDATA_RECV /* 1045 */:
                return "UNSOL_LCE_INFO_RECV";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_PCO_DATA /* 1046 */:
                return "UNSOL_PCO_DATA";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_MODEM_RESTART /* 1047 */:
                return "UNSOL_MODEM_RESTART";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_CARRIER_INFO_IMSI_ENCRYPTION /* 1048 */:
                return "UNSOL_CARRIER_INFO_IMSI_ENCRYPTION";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_NETWORK_SCAN_RESULT /* 1049 */:
                return "UNSOL_NETWORK_SCAN_RESULT";
            case NtnRILConstants.MI_NTN_RIL_UNSOL_KEEPALIVE_STATUS /* 1050 */:
                return "UNSOL_KEEPALIVE_STATUS";
            default:
                switch (i) {
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_UNTHROTTLE_APN /* 1052 */:
                        return "UNSOL_UNTHROTTLE_APN";
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMSI_QUERY /* 1053 */:
                        return "UNSOL_RESPONSE_SIM_PHONEBOOK_CHANGED";
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_AUTHENTICATION /* 1054 */:
                        return "UNSOL_RESPONSE_SIM_PHONEBOOK_RECORDS_RECEIVED";
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_SLICING_CONFIG_CHANGED /* 1055 */:
                        return "UNSOL_SLICING_CONFIG_CHANGED";
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SNR_REPORT /* 1056 */:
                        return "UNSOL_CELLULAR_IDENTIFIER_DISCLOSED";
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_TXPWR_REPORT /* 1057 */:
                        return "UNSOL_SECURITY_ALGORITHMS_UPDATED";
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_GPS_QUERY /* 1058 */:
                        return "UNSOL_DISPLAY_NETWORK_TYPE_CHANGED";
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMEI_QUERY /* 1059 */:
                        return "UNSOL_NETWORK_SECURITY_EVENTS";
                    case NtnRILConstants.MI_NTN_RIL_UNSOL_STACK_ERROR_EVENT_NOTIFY /* 1060 */:
                        return "UNSOL_PRIORITIZED_SCAN_MODE_CHANGED";
                    default:
                        switch (i) {
                            case 1100:
                                return "UNSOL_ICC_SLOT_STATUS";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_PHYSICAL_CHANNEL_CONFIG /* 1101 */:
                                return "UNSOL_PHYSICAL_CHANNEL_CONFIG";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_EMERGENCY_NUMBER_LIST /* 1102 */:
                                return "UNSOL_EMERGENCY_NUMBER_LIST";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_UICC_APPLICATIONS_ENABLEMENT_CHANGED /* 1103 */:
                                return "UNSOL_UICC_APPLICATIONS_ENABLEMENT_CHANGED";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_REGISTRATION_FAILED /* 1104 */:
                                return "UNSOL_REGISTRATION_FAILED";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_BARRING_INFO_CHANGED /* 1105 */:
                                return "UNSOL_BARRING_INFO_CHANGED";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_EMERGENCY_NETWORK_SCAN_RESULT /* 1106 */:
                                return "UNSOL_EMERGENCY_NETWORK_SCAN_RESULT";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_TRIGGER_IMS_DEREGISTRATION /* 1107 */:
                                return "UNSOL_TRIGGER_IMS_DEREGISTRATION";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_CONNECTION_SETUP_FAILURE /* 1108 */:
                                return "UNSOL_CONNECTION_SETUP_FAILURE";
                            case NtnRILConstants.MI_NTN_RIL_UNSOL_NOTIFY_ANBR /* 1109 */:
                                return "UNSOL_NOTIFY_ANBR";
                            case 1110:
                                return "UNSOL_IMEI_MAPPING_CHANGED";
                            case 1111:
                                return "UNSOL_SIMULTANEOUS_CALLING_SUPPORT_CHANGED";
                            case 1112:
                                return "UNSOL_DATA_CALL_LIST_UPDATED";
                            default:
                                if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
                                    try {
                                        return (String) Class.forName("com.mediatek.internal.telephony.MtkRIL").getDeclaredMethod("responseToStringEx", Integer.class).invoke(null, Integer.valueOf(i));
                                    } catch (Exception unused) {
                                    }
                                }
                                return "<unknown response>";
                        }
                }
        }
    }

    public static Set<String> getCaps(HalVersion halVersion, boolean z) {
        HashSet hashSet = new HashSet();
        if (halVersion.equals(RIL.RADIO_HAL_VERSION_UNKNOWN)) {
            loge("Radio Hal Version is UNKNOWN!");
        }
        logd("Radio Hal Version = " + halVersion.toString());
        if (halVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6)) {
            hashSet.add("CAPABILITY_USES_ALLOWED_NETWORK_TYPES_BITMASK");
            logd("CAPABILITY_USES_ALLOWED_NETWORK_TYPES_BITMASK");
            if (!z) {
                hashSet.add("CAPABILITY_SECONDARY_LINK_BANDWIDTH_VISIBLE");
                logd("CAPABILITY_SECONDARY_LINK_BANDWIDTH_VISIBLE");
                hashSet.add("CAPABILITY_NR_DUAL_CONNECTIVITY_CONFIGURATION_AVAILABLE");
                logd("CAPABILITY_NR_DUAL_CONNECTIVITY_CONFIGURATION_AVAILABLE");
                hashSet.add("CAPABILITY_THERMAL_MITIGATION_DATA_THROTTLING");
                logd("CAPABILITY_THERMAL_MITIGATION_DATA_THROTTLING");
                hashSet.add("CAPABILITY_SLICING_CONFIG_SUPPORTED");
                logd("CAPABILITY_SLICING_CONFIG_SUPPORTED");
                hashSet.add("CAPABILITY_PHYSICAL_CHANNEL_CONFIG_1_6_SUPPORTED");
                logd("CAPABILITY_PHYSICAL_CHANNEL_CONFIG_1_6_SUPPORTED");
            } else {
                hashSet.add("CAPABILITY_SIM_PHONEBOOK_IN_MODEM");
                logd("CAPABILITY_SIM_PHONEBOOK_IN_MODEM");
            }
        }
        TelephonyCommonUtilsStub.get().filterRadioCapabilities(hashSet);
        return hashSet;
    }

    private static boolean isPrimitiveOrWrapper(Class cls) {
        return cls.isPrimitive() || WRAPPER_CLASSES.contains(cls);
    }

    public static String convertToString(Object obj) {
        int iIntValue;
        String str;
        Object obj2;
        boolean z;
        Object objInvoke = null;
        int i = 0;
        try {
            Class[] clsArr = new Class[0];
            if (obj.getClass().getMethod("toString", null).getDeclaringClass() == Object.class) {
                if (!isPrimitiveOrWrapper(obj.getClass()) && !(obj instanceof ArrayList)) {
                    if (obj.getClass().isArray()) {
                        StringBuilder sb = new StringBuilder("[");
                        if (isPrimitiveOrWrapper(obj.getClass().getComponentType())) {
                            z = false;
                            while (i < Array.getLength(obj)) {
                                sb.append(convertToString(Array.get(obj, i)));
                                sb.append(", ");
                                i++;
                                z = true;
                            }
                        } else {
                            Object[] objArr = (Object[]) obj;
                            int length = objArr.length;
                            boolean z2 = false;
                            while (i < length) {
                                sb.append(convertToString(objArr[i]));
                                sb.append(", ");
                                i++;
                                z2 = true;
                            }
                            z = z2;
                        }
                        if (z) {
                            sb.delete(sb.length() - 2, sb.length());
                        }
                        sb.append("]");
                        return sb.toString();
                    }
                    StringBuilder sb2 = new StringBuilder(obj.getClass().getSimpleName());
                    sb2.append("{");
                    Field[] declaredFields = obj.getClass().getDeclaredFields();
                    try {
                        Class[] clsArr2 = new Class[0];
                        iIntValue = ((Integer) obj.getClass().getDeclaredMethod("getTag", null).invoke(obj, null)).intValue();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        loge(e.toString());
                        iIntValue = -1;
                    } catch (NoSuchMethodException unused) {
                        iIntValue = -1;
                    }
                    if (iIntValue != -1) {
                        try {
                            Method declaredMethod = obj.getClass().getDeclaredMethod("_tagString", Integer.TYPE);
                            declaredMethod.setAccessible(true);
                            str = (String) declaredMethod.invoke(obj, Integer.valueOf(iIntValue));
                        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                            loge(e2.toString());
                            str = null;
                        }
                        if (str != null) {
                            sb2.append(str);
                            sb2.append("=");
                            try {
                                Class[] clsArr3 = new Class[0];
                                objInvoke = obj.getClass().getDeclaredMethod("get" + str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1), null).invoke(obj, null);
                            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e3) {
                                loge(e3.toString());
                            }
                            if (objInvoke != null) {
                                sb2.append(convertToString(objInvoke));
                            }
                        }
                    } else {
                        int length2 = declaredFields.length;
                        boolean z3 = false;
                        while (i < length2) {
                            Field field = declaredFields[i];
                            if (!Modifier.isStatic(field.getModifiers())) {
                                sb2.append(field.getName());
                                sb2.append("=");
                                try {
                                    obj2 = field.get(obj);
                                } catch (IllegalAccessException e4) {
                                    loge(e4.toString());
                                    obj2 = null;
                                }
                                if (obj2 != null) {
                                    sb2.append(convertToString(obj2));
                                    sb2.append(", ");
                                    z3 = true;
                                }
                            }
                            i++;
                        }
                        if (z3) {
                            sb2.delete(sb2.length() - 2, sb2.length());
                        }
                    }
                    sb2.append("}");
                    return sb2.toString();
                }
            }
        } catch (NoSuchMethodException e5) {
            loge(e5.toString());
        }
        return obj.toString();
    }

    public static SrvccCall[] convertToHalSrvccCall(SrvccConnection[] srvccConnectionArr) {
        int i = 0;
        if (srvccConnectionArr == null) {
            return new SrvccCall[0];
        }
        int length = srvccConnectionArr.length;
        SrvccCall[] srvccCallArr = new SrvccCall[length];
        while (i < length) {
            SrvccCall srvccCall = new SrvccCall();
            srvccCallArr[i] = srvccCall;
            int i2 = i + 1;
            srvccCall.index = i2;
            srvccCall.callType = convertSrvccCallType(srvccConnectionArr[i].getType());
            srvccCallArr[i].callState = convertCallState(srvccConnectionArr[i].getState());
            srvccCallArr[i].callSubstate = convertSrvccCallSubState(srvccConnectionArr[i].getSubState());
            srvccCallArr[i].ringbackToneType = convertSrvccCallRingbackToneType(srvccConnectionArr[i].getRingbackToneType());
            srvccCallArr[i].isMpty = srvccConnectionArr[i].isMultiParty();
            srvccCallArr[i].isMT = srvccConnectionArr[i].isIncoming();
            srvccCallArr[i].number = TextUtils.emptyIfNull(srvccConnectionArr[i].getNumber());
            srvccCallArr[i].numPresentation = convertPresentation(srvccConnectionArr[i].getNumberPresentation());
            srvccCallArr[i].name = TextUtils.emptyIfNull(srvccConnectionArr[i].getName());
            srvccCallArr[i].namePresentation = convertPresentation(srvccConnectionArr[i].getNamePresentation());
            i = i2;
        }
        return srvccCallArr;
    }

    public static int convertSrvccCallType(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        RIL$$ExternalSyntheticBUOutline0.m("illegal call type ", i);
        return 0;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.RILUtils$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$Call$State;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState;

        static {
            int[] iArr = new int[Call.State.values().length];
            $SwitchMap$com$android$internal$telephony$Call$State = iArr;
            try {
                iArr[Call.State.ACTIVE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.HOLDING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.DIALING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.ALERTING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.INCOMING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.WAITING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.DISCONNECTING.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            int[] iArr2 = new int[IccCardApplicationStatus.PersoSubState.values().length];
            $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState = iArr2;
            try {
                iArr2[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_IN_PROGRESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_READY.ordinal()] = 2;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK.ordinal()] = 3;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK_SUBSET.ordinal()] = 4;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_CORPORATE.ordinal()] = 5;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SERVICE_PROVIDER.ordinal()] = 6;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM.ordinal()] = 7;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK_PUK.ordinal()] = 8;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK_SUBSET_PUK.ordinal()] = 9;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_CORPORATE_PUK.ordinal()] = 10;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SERVICE_PROVIDER_PUK.ordinal()] = 11;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM_PUK.ordinal()] = 12;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_NETWORK1.ordinal()] = 13;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_NETWORK2.ordinal()] = 14;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_HRPD.ordinal()] = 15;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_CORPORATE.ordinal()] = 16;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_SERVICE_PROVIDER.ordinal()] = 17;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_RUIM.ordinal()] = 18;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_NETWORK1_PUK.ordinal()] = 19;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_NETWORK2_PUK.ordinal()] = 20;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_HRPD_PUK.ordinal()] = 21;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_CORPORATE_PUK.ordinal()] = 22;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_SERVICE_PROVIDER_PUK.ordinal()] = 23;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_RUIM_RUIM_PUK.ordinal()] = 24;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SPN.ordinal()] = 25;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SPN_PUK.ordinal()] = 26;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SP_EHPLMN.ordinal()] = 27;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SP_EHPLMN_PUK.ordinal()] = 28;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_ICCID.ordinal()] = 29;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_ICCID_PUK.ordinal()] = 30;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_IMPI.ordinal()] = 31;
            } catch (NoSuchFieldError unused38) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_IMPI_PUK.ordinal()] = 32;
            } catch (NoSuchFieldError unused39) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NS_SP.ordinal()] = 33;
            } catch (NoSuchFieldError unused40) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NS_SP_PUK.ordinal()] = 34;
            } catch (NoSuchFieldError unused41) {
            }
        }
    }

    public static int convertCallState(Call.State state) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$Call$State[state.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            default:
                Call$$ExternalSyntheticBUOutline0.m("illegal state ", state);
                return 0;
        }
    }

    public static int convertSrvccCallSubState(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        RIL$$ExternalSyntheticBUOutline0.m("illegal substate ", i);
        return 0;
    }

    public static int convertSrvccCallRingbackToneType(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        RIL$$ExternalSyntheticBUOutline0.m("illegal ringback tone type ", i);
        return 0;
    }

    public static int convertPresentation(int i) {
        if (i == 1) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 2;
        }
        if (i == 4) {
            return 3;
        }
        RIL$$ExternalSyntheticBUOutline0.m("illegal presentation ", i);
        return 0;
    }

    public static int convertImsRegistrationState(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 2) {
            return 1;
        }
        RIL$$ExternalSyntheticBUOutline0.m("illegal state ", i);
        return 0;
    }

    public static ImsCall[] convertImsCallInfo(List<ImsCallInfo> list) {
        if (list == null) {
            return new ImsCall[0];
        }
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2) != null) {
                i++;
            }
        }
        if (i == 0) {
            return new ImsCall[0];
        }
        ImsCall[] imsCallArr = new ImsCall[i];
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            ImsCallInfo imsCallInfo = list.get(i4);
            if (imsCallInfo != null) {
                ImsCall imsCall = new ImsCall();
                imsCallArr[i3] = imsCall;
                imsCall.index = imsCallInfo.getIndex();
                imsCallArr[i3].callState = convertToHalImsCallState(imsCallInfo.getCallState());
                imsCallArr[i3].callType = imsCallInfo.isEmergencyCall() ? 1 : 0;
                imsCallArr[i3].accessNetwork = convertToHalAccessNetworkAidl(imsCallInfo.getCallRadioTech());
                imsCallArr[i3].direction = !imsCallInfo.isIncoming() ? 1 : 0;
                imsCallArr[i3].isHeldByRemote = imsCallInfo.isHeldByRemote();
                i3++;
            }
        }
        return imsCallArr;
    }

    private static int convertToHalImsCallState(Call.State state) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$Call$State[state.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            default:
                return 7;
        }
    }

    public static CellularIdentifierDisclosure convertCellularIdentifierDisclosure(android.hardware.radio.network.CellularIdentifierDisclosure cellularIdentifierDisclosure) {
        if (cellularIdentifierDisclosure == null) {
            return null;
        }
        return new CellularIdentifierDisclosure(cellularIdentifierDisclosure.protocolMessage, cellularIdentifierDisclosure.identifier, cellularIdentifierDisclosure.plmn, cellularIdentifierDisclosure.isEmergency);
    }

    public static SecurityAlgorithmUpdate convertSecurityAlgorithmUpdate(android.hardware.radio.network.SecurityAlgorithmUpdate securityAlgorithmUpdate) {
        if (securityAlgorithmUpdate == null) {
            return null;
        }
        return new SecurityAlgorithmUpdate(securityAlgorithmUpdate.connectionEvent, securityAlgorithmUpdate.encryption, securityAlgorithmUpdate.integrity, securityAlgorithmUpdate.isUnprotectedEmergency);
    }

    public static ArrayList<SimTypeInfo> convertAidlSimTypeInfo(android.hardware.radio.config.SimTypeInfo[] simTypeInfoArr) {
        ArrayList<SimTypeInfo> arrayList = new ArrayList<>();
        if (simTypeInfoArr == null || simTypeInfoArr.length == 0) {
            loge("convertAidlSimTypeInfo received NULL or empty simTypeInfos");
            return arrayList;
        }
        logd("convertAidlSimTypeInfo: " + Arrays.toString(simTypeInfoArr));
        for (int i = 0; i < simTypeInfoArr.length; i++) {
            if (simTypeInfoArr[i] == null) {
                loge("convertAidlSimTypeInfo received NULL SimTypeInfo");
            } else {
                SimTypeInfo simTypeInfo = new SimTypeInfo();
                simTypeInfo.mPhysicalSlotIndex = i;
                android.hardware.radio.config.SimTypeInfo simTypeInfo2 = simTypeInfoArr[i];
                simTypeInfo.mSupportedSimTypes = simTypeInfo2.supportedSimTypes;
                simTypeInfo.setCurrentSimType(simTypeInfo2.currentSimType);
                arrayList.add(simTypeInfo);
            }
        }
        return arrayList;
    }

    public static int[] convertToAidlSimTypes(int[] iArr) {
        int[] iArr2 = new int[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr2[i] = getAidlMappedSimType(iArr[i]);
        }
        return iArr2;
    }

    private static void logd(String str) {
        Rlog.d("RILUtils", str);
    }

    private static void loge(String str) {
        Rlog.e("RILUtils", str);
    }
}
