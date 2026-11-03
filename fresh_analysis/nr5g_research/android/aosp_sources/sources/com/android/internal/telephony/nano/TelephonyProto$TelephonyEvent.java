package com.android.internal.telephony.nano;

import android.telephony.gsm.SmsMessage;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.protobuf.nano.CodedInputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.CodedOutputByteBufferNano;
import com.android.internal.telephony.protobuf.nano.ExtendableMessageNano;
import com.android.internal.telephony.protobuf.nano.InternalNano;
import com.android.internal.telephony.protobuf.nano.InvalidProtocolBufferNanoException;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.protobuf.nano.WireFormatNano;
import com.android.internal.telephony.util.DnsPacket;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class TelephonyProto$TelephonyEvent extends ExtendableMessageNano<TelephonyProto$TelephonyEvent> {
    private static volatile TelephonyProto$TelephonyEvent[] _emptyArray;
    public TelephonyProto$ActiveSubscriptionInfo activeSubscriptionInfo;
    public CarrierIdMatching carrierIdMatching;
    public CarrierKeyChange carrierKeyChange;
    public TelephonyProto$RilDataCall[] dataCalls;
    public int dataStallAction;
    public DataSwitch dataSwitch;
    public RilDeactivateDataCall deactivateDataCall;
    public int emergencyNumberDatabaseVersion;
    public int enabledModemBitmap;
    public int error;
    public TelephonyProto$ImsCapabilities imsCapabilities;
    public TelephonyProto$ImsConnectionState imsConnectionState;
    public ModemRestart modemRestart;
    public NetworkCapabilitiesInfo networkCapabilities;
    public int networkValidationState;
    public long nitzTimestampMillis;
    public OnDemandDataSwitch onDemandDataSwitch;
    public int phoneId;
    public int radioState;
    public TelephonyProto$TelephonyServiceState serviceState;
    public TelephonyProto$TelephonySettings settings;
    public RilSetupDataCall setupDataCall;
    public RilSetupDataCallResponse setupDataCallResponse;
    public int signalStrength;
    public int[] simState;
    public long timestampMillis;
    public int type;
    public TelephonyProto$EmergencyNumberInfo updatedEmergencyNumber;

    public interface ApnType {
        public static final int APN_TYPE_CBS = 8;
        public static final int APN_TYPE_DEFAULT = 1;
        public static final int APN_TYPE_DUN = 4;
        public static final int APN_TYPE_EMERGENCY = 10;
        public static final int APN_TYPE_FOTA = 6;
        public static final int APN_TYPE_HIPRI = 5;
        public static final int APN_TYPE_IA = 9;
        public static final int APN_TYPE_IMS = 7;
        public static final int APN_TYPE_MMS = 2;
        public static final int APN_TYPE_SUPL = 3;
        public static final int APN_TYPE_UNKNOWN = 0;
    }

    public interface EventState {
        public static final int EVENT_STATE_END = 2;
        public static final int EVENT_STATE_START = 1;
        public static final int EVENT_STATE_UNKNOWN = 0;
    }

    public interface NetworkValidationState {
        public static final int NETWORK_VALIDATION_STATE_AVAILABLE = 1;
        public static final int NETWORK_VALIDATION_STATE_FAILED = 2;
        public static final int NETWORK_VALIDATION_STATE_PASSED = 3;
        public static final int NETWORK_VALIDATION_STATE_UNKNOWN = 0;
    }

    public interface RadioState {
        public static final int RADIO_STATE_OFF = 1;
        public static final int RADIO_STATE_ON = 2;
        public static final int RADIO_STATE_UNAVAILABLE = 3;
        public static final int RADIO_STATE_UNKNOWN = 0;
    }

    public interface Type {
        public static final int ACTIVE_SUBSCRIPTION_INFO_CHANGED = 19;
        public static final int CARRIER_ID_MATCHING = 13;
        public static final int CARRIER_KEY_CHANGED = 14;
        public static final int DATA_CALL_DEACTIVATE = 8;
        public static final int DATA_CALL_DEACTIVATE_RESPONSE = 9;
        public static final int DATA_CALL_LIST_CHANGED = 7;
        public static final int DATA_CALL_SETUP = 5;
        public static final int DATA_CALL_SETUP_RESPONSE = 6;
        public static final int DATA_STALL_ACTION = 10;
        public static final int DATA_SWITCH = 15;
        public static final int EMERGENCY_NUMBER_REPORT = 21;
        public static final int ENABLED_MODEM_CHANGED = 20;
        public static final int IMS_CAPABILITIES_CHANGED = 4;
        public static final int IMS_CONNECTION_STATE_CHANGED = 3;
        public static final int MODEM_RESTART = 11;
        public static final int NETWORK_CAPABILITIES_CHANGED = 22;
        public static final int NETWORK_VALIDATE = 16;
        public static final int NITZ_TIME = 12;
        public static final int ON_DEMAND_DATA_SWITCH = 17;
        public static final int RADIO_STATE_CHANGED = 24;
        public static final int RIL_SERVICE_STATE_CHANGED = 2;
        public static final int SETTINGS_CHANGED = 1;
        public static final int SIGNAL_STRENGTH = 23;
        public static final int SIM_STATE_CHANGED = 18;
        public static final int UNKNOWN = 0;
    }

    public static final class DataSwitch extends ExtendableMessageNano<DataSwitch> {
        private static volatile DataSwitch[] _emptyArray;
        public int reason;
        public int state;

        public interface Reason {
            public static final int DATA_SWITCH_REASON_AUTO = 4;
            public static final int DATA_SWITCH_REASON_CBRS = 3;
            public static final int DATA_SWITCH_REASON_IN_CALL = 2;
            public static final int DATA_SWITCH_REASON_MANUAL = 1;
            public static final int DATA_SWITCH_REASON_UNKNOWN = 0;
        }

        public static DataSwitch[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new DataSwitch[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public DataSwitch() {
            clear();
        }

        public DataSwitch clear() {
            this.reason = 0;
            this.state = 0;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.reason;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            int i2 = this.state;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeInt32(2, i2);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.reason;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            int i2 = this.state;
            return i2 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, i2) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public DataSwitch mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    break;
                }
                if (tag == 8) {
                    int int32 = codedInputByteBufferNano.readInt32();
                    if (int32 == 0 || int32 == 1 || int32 == 2 || int32 == 3 || int32 == 4) {
                        this.reason = int32;
                    }
                } else if (tag != 16) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        break;
                    }
                } else {
                    int int33 = codedInputByteBufferNano.readInt32();
                    if (int33 == 0 || int33 == 1 || int33 == 2) {
                        this.state = int33;
                    }
                }
            }
            return this;
        }

        public static DataSwitch parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (DataSwitch) MessageNano.mergeFrom(new DataSwitch(), bArr);
        }

        public static DataSwitch parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new DataSwitch().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class OnDemandDataSwitch extends ExtendableMessageNano<OnDemandDataSwitch> {
        private static volatile OnDemandDataSwitch[] _emptyArray;
        public int apn;
        public int state;

        public static OnDemandDataSwitch[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new OnDemandDataSwitch[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public OnDemandDataSwitch() {
            clear();
        }

        public OnDemandDataSwitch clear() {
            this.apn = 0;
            this.state = 0;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.apn;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            int i2 = this.state;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeInt32(2, i2);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.apn;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            int i2 = this.state;
            return i2 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, i2) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public OnDemandDataSwitch mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int int32 = codedInputByteBufferNano.readInt32();
                        switch (int32) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                                this.apn = int32;
                                break;
                        }
                    } else if (tag != 16) {
                        if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        }
                    } else {
                        int int33 = codedInputByteBufferNano.readInt32();
                        if (int33 == 0 || int33 == 1 || int33 == 2) {
                            this.state = int33;
                        }
                    }
                }
            }
            return this;
        }

        public static OnDemandDataSwitch parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (OnDemandDataSwitch) MessageNano.mergeFrom(new OnDemandDataSwitch(), bArr);
        }

        public static OnDemandDataSwitch parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new OnDemandDataSwitch().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class RilSetupDataCall extends ExtendableMessageNano<RilSetupDataCall> {
        private static volatile RilSetupDataCall[] _emptyArray;
        public String apn;
        public int dataProfile;
        public int rat;
        public int type;

        public interface RilDataProfile {
            public static final int RIL_DATA_PROFILE_CBS = 5;
            public static final int RIL_DATA_PROFILE_DEFAULT = 1;
            public static final int RIL_DATA_PROFILE_FOTA = 4;
            public static final int RIL_DATA_PROFILE_IMS = 3;
            public static final int RIL_DATA_PROFILE_INVALID = 7;
            public static final int RIL_DATA_PROFILE_OEM_BASE = 6;
            public static final int RIL_DATA_PROFILE_TETHERED = 2;
            public static final int RIL_DATA_UNKNOWN = 0;
        }

        public static RilSetupDataCall[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new RilSetupDataCall[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public RilSetupDataCall() {
            clear();
        }

        public RilSetupDataCall clear() {
            this.rat = -1;
            this.dataProfile = 0;
            this.apn = "";
            this.type = 0;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.rat;
            if (i != -1) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            int i2 = this.dataProfile;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeInt32(2, i2);
            }
            if (!this.apn.equals("")) {
                codedOutputByteBufferNano.writeString(3, this.apn);
            }
            int i3 = this.type;
            if (i3 != 0) {
                codedOutputByteBufferNano.writeInt32(4, i3);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.rat;
            if (i != -1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            int i2 = this.dataProfile;
            if (i2 != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
            }
            if (!this.apn.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.apn);
            }
            int i3 = this.type;
            return i3 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(4, i3) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public RilSetupDataCall mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int int32 = codedInputByteBufferNano.readInt32();
                        switch (int32) {
                            case -1:
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                                this.rat = int32;
                                break;
                        }
                    } else if (tag == 16) {
                        int int33 = codedInputByteBufferNano.readInt32();
                        switch (int33) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                this.dataProfile = int33;
                                break;
                        }
                    } else if (tag == 26) {
                        this.apn = codedInputByteBufferNano.readString();
                    } else if (tag != 32) {
                        if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        }
                    } else {
                        int int34 = codedInputByteBufferNano.readInt32();
                        switch (int34) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                this.type = int34;
                                break;
                        }
                    }
                }
            }
            return this;
        }

        public static RilSetupDataCall parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (RilSetupDataCall) MessageNano.mergeFrom(new RilSetupDataCall(), bArr);
        }

        public static RilSetupDataCall parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new RilSetupDataCall().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class RilSetupDataCallResponse extends ExtendableMessageNano<RilSetupDataCallResponse> {
        private static volatile RilSetupDataCallResponse[] _emptyArray;
        public TelephonyProto$RilDataCall call;
        public int status;
        public int suggestedRetryTimeMillis;

        public interface RilDataCallFailCause {
            public static final int PDP_FAIL_ACCESS_ATTEMPT_ALREADY_IN_PROGRESS = 2219;
            public static final int PDP_FAIL_ACCESS_BLOCK = 2087;
            public static final int PDP_FAIL_ACCESS_BLOCK_ALL = 2088;
            public static final int PDP_FAIL_ACCESS_CLASS_DSAC_REJECTION = 2108;
            public static final int PDP_FAIL_ACCESS_CONTROL_LIST_CHECK_FAILURE = 2128;
            public static final int PDP_FAIL_ACTIVATION_REJECTED_BCM_VIOLATION = 48;
            public static final int PDP_FAIL_ACTIVATION_REJECT_GGSN = 30;
            public static final int PDP_FAIL_ACTIVATION_REJECT_UNSPECIFIED = 31;
            public static final int PDP_FAIL_APN_DISABLED = 2045;
            public static final int PDP_FAIL_APN_DISALLOWED_ON_ROAMING = 2059;
            public static final int PDP_FAIL_APN_MISMATCH = 2054;
            public static final int PDP_FAIL_APN_PARAMETERS_CHANGED = 2060;
            public static final int PDP_FAIL_APN_PENDING_HANDOVER = 2041;
            public static final int PDP_FAIL_APN_TYPE_CONFLICT = 112;
            public static final int PDP_FAIL_AUTH_FAILURE_ON_EMERGENCY_CALL = 122;
            public static final int PDP_FAIL_BEARER_HANDLING_NOT_SUPPORTED = 60;
            public static final int PDP_FAIL_CALL_DISALLOWED_IN_ROAMING = 2068;
            public static final int PDP_FAIL_CALL_PREEMPT_BY_EMERGENCY_APN = 127;
            public static final int PDP_FAIL_CANNOT_ENCODE_OTA_MESSAGE = 2159;
            public static final int PDP_FAIL_CDMA_ALERT_STOP = 2077;
            public static final int PDP_FAIL_CDMA_INCOMING_CALL = 2076;
            public static final int PDP_FAIL_CDMA_INTERCEPT = 2073;
            public static final int PDP_FAIL_CDMA_LOCK = 2072;
            public static final int PDP_FAIL_CDMA_RELEASE_DUE_TO_SO_REJECTION = 2075;
            public static final int PDP_FAIL_CDMA_REORDER = 2074;
            public static final int PDP_FAIL_CDMA_RETRY_ORDER = 2086;
            public static final int PDP_FAIL_CHANNEL_ACQUISITION_FAILURE = 2078;
            public static final int PDP_FAIL_CLOSE_IN_PROGRESS = 2030;
            public static final int PDP_FAIL_COLLISION_WITH_NETWORK_INITIATED_REQUEST = 56;
            public static final int PDP_FAIL_COMPANION_IFACE_IN_USE = 118;
            public static final int PDP_FAIL_CONCURRENT_SERVICES_INCOMPATIBLE = 2083;
            public static final int PDP_FAIL_CONCURRENT_SERVICES_NOT_ALLOWED = 2091;
            public static final int PDP_FAIL_CONCURRENT_SERVICE_NOT_SUPPORTED_BY_BASE_STATION = 2080;
            public static final int PDP_FAIL_CONDITIONAL_IE_ERROR = 100;
            public static final int PDP_FAIL_CONGESTION = 2106;
            public static final int PDP_FAIL_CONNECTION_RELEASED = 2113;
            public static final int PDP_FAIL_CS_DOMAIN_NOT_AVAILABLE = 2181;
            public static final int PDP_FAIL_CS_FALLBACK_CALL_ESTABLISHMENT_NOT_ALLOWED = 2188;
            public static final int PDP_FAIL_DATA_PLAN_EXPIRED = 2198;
            public static final int PDP_FAIL_DATA_REGISTRATION_FAIL = -2;
            public static final int PDP_FAIL_DATA_ROAMING_SETTINGS_DISABLED = 2064;
            public static final int PDP_FAIL_DATA_SETTINGS_DISABLED = 2063;
            public static final int PDP_FAIL_DBM_OR_SMS_IN_PROGRESS = 2211;
            public static final int PDP_FAIL_DDS_SWITCHED = 2065;
            public static final int PDP_FAIL_DDS_SWITCH_IN_PROGRESS = 2067;
            public static final int PDP_FAIL_DRB_RELEASED_BY_RRC = 2112;
            public static final int PDP_FAIL_DS_EXPLICIT_DEACTIVATION = 2125;
            public static final int PDP_FAIL_DUAL_SWITCH = 2227;
            public static final int PDP_FAIL_DUN_CALL_DISALLOWED = 2056;
            public static final int PDP_FAIL_DUPLICATE_BEARER_ID = 2118;
            public static final int PDP_FAIL_EHRPD_TO_HRPD_FALLBACK = 2049;
            public static final int PDP_FAIL_EMBMS_NOT_ENABLED = 2193;
            public static final int PDP_FAIL_EMBMS_REGULAR_DEACTIVATION = 2195;
            public static final int PDP_FAIL_EMERGENCY_IFACE_ONLY = 116;
            public static final int PDP_FAIL_EMERGENCY_MODE = 2221;
            public static final int PDP_FAIL_EMM_ACCESS_BARRED = 115;
            public static final int PDP_FAIL_EMM_ACCESS_BARRED_INFINITE_RETRY = 121;
            public static final int PDP_FAIL_EMM_ATTACH_FAILED = 2115;
            public static final int PDP_FAIL_EMM_ATTACH_STARTED = 2116;
            public static final int PDP_FAIL_EMM_DETACHED = 2114;
            public static final int PDP_FAIL_EMM_T3417_EXPIRED = 2130;
            public static final int PDP_FAIL_EMM_T3417_EXT_EXPIRED = 2131;
            public static final int PDP_FAIL_EPS_SERVICES_AND_NON_EPS_SERVICES_NOT_ALLOWED = 2178;
            public static final int PDP_FAIL_EPS_SERVICES_NOT_ALLOWED_IN_PLMN = 2179;
            public static final int PDP_FAIL_ERROR_UNSPECIFIED = 65535;
            public static final int PDP_FAIL_ESM_BAD_OTA_MESSAGE = 2122;
            public static final int PDP_FAIL_ESM_BEARER_DEACTIVATED_TO_SYNC_WITH_NETWORK = 2120;
            public static final int PDP_FAIL_ESM_COLLISION_SCENARIOS = 2119;
            public static final int PDP_FAIL_ESM_CONTEXT_TRANSFERRED_DUE_TO_IRAT = 2124;
            public static final int PDP_FAIL_ESM_DOWNLOAD_SERVER_REJECTED_THE_CALL = 2123;
            public static final int PDP_FAIL_ESM_FAILURE = 2182;
            public static final int PDP_FAIL_ESM_INFO_NOT_RECEIVED = 53;
            public static final int PDP_FAIL_ESM_LOCAL_CAUSE_NONE = 2126;
            public static final int PDP_FAIL_ESM_NW_ACTIVATED_DED_BEARER_WITH_ID_OF_DEF_BEARER = 2121;
            public static final int PDP_FAIL_ESM_PROCEDURE_TIME_OUT = 2155;
            public static final int PDP_FAIL_ESM_UNKNOWN_EPS_BEARER_CONTEXT = 2111;
            public static final int PDP_FAIL_EVDO_CONNECTION_DENY_BY_BILLING_OR_AUTHENTICATION_FAILURE = 2201;
            public static final int PDP_FAIL_EVDO_CONNECTION_DENY_BY_GENERAL_OR_NETWORK_BUSY = 2200;
            public static final int PDP_FAIL_EVDO_HDR_CHANGED = 2202;
            public static final int PDP_FAIL_EVDO_HDR_CONNECTION_SETUP_TIMEOUT = 2206;
            public static final int PDP_FAIL_EVDO_HDR_EXITED = 2203;
            public static final int PDP_FAIL_EVDO_HDR_NO_SESSION = 2204;
            public static final int PDP_FAIL_EVDO_USING_GPS_FIX_INSTEAD_OF_HDR_CALL = 2205;
            public static final int PDP_FAIL_FADE = 2217;
            public static final int PDP_FAIL_FAILED_TO_ACQUIRE_COLOCATED_HDR = 2207;
            public static final int PDP_FAIL_FEATURE_NOT_SUPP = 40;
            public static final int PDP_FAIL_FILTER_SEMANTIC_ERROR = 44;
            public static final int PDP_FAIL_FILTER_SYTAX_ERROR = 45;
            public static final int PDP_FAIL_FORBIDDEN_APN_NAME = 2066;
            public static final int PDP_FAIL_GPRS_SERVICES_AND_NON_GPRS_SERVICES_NOT_ALLOWED = 2097;
            public static final int PDP_FAIL_GPRS_SERVICES_NOT_ALLOWED = 2098;
            public static final int PDP_FAIL_GPRS_SERVICES_NOT_ALLOWED_IN_THIS_PLMN = 2103;
            public static final int PDP_FAIL_HANDOFF_PREFERENCE_CHANGED = 2251;
            public static final int PDP_FAIL_HDR_ACCESS_FAILURE = 2213;
            public static final int PDP_FAIL_HDR_FADE = 2212;
            public static final int PDP_FAIL_HDR_NO_LOCK_GRANTED = 2210;
            public static final int PDP_FAIL_IFACE_AND_POL_FAMILY_MISMATCH = 120;
            public static final int PDP_FAIL_IFACE_MISMATCH = 117;
            public static final int PDP_FAIL_ILLEGAL_ME = 2096;
            public static final int PDP_FAIL_ILLEGAL_MS = 2095;
            public static final int PDP_FAIL_IMEI_NOT_ACCEPTED = 2177;
            public static final int PDP_FAIL_IMPLICITLY_DETACHED = 2100;
            public static final int PDP_FAIL_IMSI_UNKNOWN_IN_HOME_SUBSCRIBER_SERVER = 2176;
            public static final int PDP_FAIL_INCOMING_CALL_REJECTED = 2092;
            public static final int PDP_FAIL_INSUFFICIENT_RESOURCES = 26;
            public static final int PDP_FAIL_INTERFACE_IN_USE = 2058;
            public static final int PDP_FAIL_INTERNAL_CALL_PREEMPT_BY_HIGH_PRIO_APN = 114;
            public static final int PDP_FAIL_INTERNAL_EPC_NONEPC_TRANSITION = 2057;
            public static final int PDP_FAIL_INVALID_CONNECTION_ID = 2156;
            public static final int PDP_FAIL_INVALID_DNS_ADDR = 123;
            public static final int PDP_FAIL_INVALID_EMM_STATE = 2190;
            public static final int PDP_FAIL_INVALID_MANDATORY_INFO = 96;
            public static final int PDP_FAIL_INVALID_MODE = 2223;
            public static final int PDP_FAIL_INVALID_PCSCF_ADDR = 113;
            public static final int PDP_FAIL_INVALID_PCSCF_OR_DNS_ADDRESS = 124;
            public static final int PDP_FAIL_INVALID_PRIMARY_NSAPI = 2158;
            public static final int PDP_FAIL_INVALID_SIM_STATE = 2224;
            public static final int PDP_FAIL_INVALID_TRANSACTION_ID = 81;
            public static final int PDP_FAIL_IPV6_ADDRESS_TRANSFER_FAILED = 2047;
            public static final int PDP_FAIL_IPV6_PREFIX_UNAVAILABLE = 2250;
            public static final int PDP_FAIL_IP_ADDRESS_MISMATCH = 119;
            public static final int PDP_FAIL_IP_VERSION_MISMATCH = 2055;
            public static final int PDP_FAIL_IRAT_HANDOVER_FAILED = 2194;
            public static final int PDP_FAIL_IS707B_MAX_ACCESS_PROBES = 2089;
            public static final int PDP_FAIL_LIMITED_TO_IPV4 = 2234;
            public static final int PDP_FAIL_LIMITED_TO_IPV6 = 2235;
            public static final int PDP_FAIL_LLC_SNDCP = 25;
            public static final int PDP_FAIL_LOCAL_END = 2215;
            public static final int PDP_FAIL_LOCATION_AREA_NOT_ALLOWED = 2102;
            public static final int PDP_FAIL_LOWER_LAYER_REGISTRATION_FAILURE = 2197;
            public static final int PDP_FAIL_LOW_POWER_MODE_OR_POWERING_DOWN = 2044;
            public static final int PDP_FAIL_LTE_NAS_SERVICE_REQUEST_FAILED = 2117;
            public static final int PDP_FAIL_LTE_THROTTLING_NOT_REQUIRED = 2127;
            public static final int PDP_FAIL_MAC_FAILURE = 2183;
            public static final int PDP_FAIL_MAXIMIUM_NSAPIS_EXCEEDED = 2157;
            public static final int PDP_FAIL_MAXINUM_SIZE_OF_L2_MESSAGE_EXCEEDED = 2166;
            public static final int PDP_FAIL_MAX_ACCESS_PROBE = 2079;
            public static final int PDP_FAIL_MAX_ACTIVE_PDP_CONTEXT_REACHED = 65;
            public static final int PDP_FAIL_MAX_IPV4_CONNECTIONS = 2052;
            public static final int PDP_FAIL_MAX_IPV6_CONNECTIONS = 2053;
            public static final int PDP_FAIL_MAX_PPP_INACTIVITY_TIMER_EXPIRED = 2046;
            public static final int PDP_FAIL_MESSAGE_INCORRECT_SEMANTIC = 95;
            public static final int PDP_FAIL_MESSAGE_TYPE_UNSUPPORTED = 97;
            public static final int PDP_FAIL_MIP_CONFIG_FAILURE = 2050;
            public static final int PDP_FAIL_MIP_FA_ADMIN_PROHIBITED = 2001;
            public static final int PDP_FAIL_MIP_FA_DELIVERY_STYLE_NOT_SUPPORTED = 2012;
            public static final int PDP_FAIL_MIP_FA_ENCAPSULATION_UNAVAILABLE = 2008;
            public static final int PDP_FAIL_MIP_FA_HOME_AGENT_AUTHENTICATION_FAILURE = 2004;
            public static final int PDP_FAIL_MIP_FA_INSUFFICIENT_RESOURCES = 2002;
            public static final int PDP_FAIL_MIP_FA_MALFORMED_REPLY = 2007;
            public static final int PDP_FAIL_MIP_FA_MALFORMED_REQUEST = 2006;
            public static final int PDP_FAIL_MIP_FA_MISSING_CHALLENGE = 2017;
            public static final int PDP_FAIL_MIP_FA_MISSING_HOME_ADDRESS = 2015;
            public static final int PDP_FAIL_MIP_FA_MISSING_HOME_AGENT = 2014;
            public static final int PDP_FAIL_MIP_FA_MISSING_NAI = 2013;
            public static final int PDP_FAIL_MIP_FA_MOBILE_NODE_AUTHENTICATION_FAILURE = 2003;
            public static final int PDP_FAIL_MIP_FA_REASON_UNSPECIFIED = 2000;
            public static final int PDP_FAIL_MIP_FA_REQUESTED_LIFETIME_TOO_LONG = 2005;
            public static final int PDP_FAIL_MIP_FA_REVERSE_TUNNEL_IS_MANDATORY = 2011;
            public static final int PDP_FAIL_MIP_FA_REVERSE_TUNNEL_UNAVAILABLE = 2010;
            public static final int PDP_FAIL_MIP_FA_STALE_CHALLENGE = 2018;
            public static final int PDP_FAIL_MIP_FA_UNKNOWN_CHALLENGE = 2016;
            public static final int PDP_FAIL_MIP_FA_VJ_HEADER_COMPRESSION_UNAVAILABLE = 2009;
            public static final int PDP_FAIL_MIP_HA_ADMIN_PROHIBITED = 2020;
            public static final int PDP_FAIL_MIP_HA_ENCAPSULATION_UNAVAILABLE = 2029;
            public static final int PDP_FAIL_MIP_HA_FOREIGN_AGENT_AUTHENTICATION_FAILURE = 2023;
            public static final int PDP_FAIL_MIP_HA_INSUFFICIENT_RESOURCES = 2021;
            public static final int PDP_FAIL_MIP_HA_MALFORMED_REQUEST = 2025;
            public static final int PDP_FAIL_MIP_HA_MOBILE_NODE_AUTHENTICATION_FAILURE = 2022;
            public static final int PDP_FAIL_MIP_HA_REASON_UNSPECIFIED = 2019;
            public static final int PDP_FAIL_MIP_HA_REGISTRATION_ID_MISMATCH = 2024;
            public static final int PDP_FAIL_MIP_HA_REVERSE_TUNNEL_IS_MANDATORY = 2028;
            public static final int PDP_FAIL_MIP_HA_REVERSE_TUNNEL_UNAVAILABLE = 2027;
            public static final int PDP_FAIL_MIP_HA_UNKNOWN_HOME_AGENT_ADDRESS = 2026;
            public static final int PDP_FAIL_MISSING_UKNOWN_APN = 27;
            public static final int PDP_FAIL_MODEM_APP_PREEMPTED = 2032;
            public static final int PDP_FAIL_MODEM_RESTART = 2037;
            public static final int PDP_FAIL_MSC_TEMPORARILY_NOT_REACHABLE = 2180;
            public static final int PDP_FAIL_MSG_AND_PROTOCOL_STATE_UNCOMPATIBLE = 101;
            public static final int PDP_FAIL_MSG_TYPE_NONCOMPATIBLE_STATE = 98;
            public static final int PDP_FAIL_MS_IDENTITY_CANNOT_BE_DERIVED_BY_THE_NETWORK = 2099;
            public static final int PDP_FAIL_MULTIPLE_PDP_CALL_NOT_ALLOWED = 2192;
            public static final int PDP_FAIL_MULTI_CONN_TO_SAME_PDN_NOT_ALLOWED = 55;
            public static final int PDP_FAIL_NAS_LAYER_FAILURE = 2191;
            public static final int PDP_FAIL_NAS_REQUEST_REJECTED_BY_NETWORK = 2167;
            public static final int PDP_FAIL_NAS_SIGNALLING = 14;
            public static final int PDP_FAIL_NETWORK_FAILURE = 38;
            public static final int PDP_FAIL_NETWORK_INITIATED_DETACH_NO_AUTO_REATTACH = 2154;
            public static final int PDP_FAIL_NETWORK_INITIATED_DETACH_WITH_AUTO_REATTACH = 2153;
            public static final int PDP_FAIL_NETWORK_INITIATED_TERMINATION = 2031;
            public static final int PDP_FAIL_NONE = 1;
            public static final int PDP_FAIL_NON_IP_NOT_SUPPORTED = 2069;
            public static final int PDP_FAIL_NORMAL_RELEASE = 2218;
            public static final int PDP_FAIL_NO_CDMA_SERVICE = 2084;
            public static final int PDP_FAIL_NO_COLLOCATED_HDR = 2225;
            public static final int PDP_FAIL_NO_EPS_BEARER_CONTEXT_ACTIVATED = 2189;
            public static final int PDP_FAIL_NO_GPRS_CONTEXT = 2094;
            public static final int PDP_FAIL_NO_HYBRID_HDR_SERVICE = 2209;
            public static final int PDP_FAIL_NO_PDP_CONTEXT_ACTIVATED = 2107;
            public static final int PDP_FAIL_NO_RESPONSE_FROM_BASE_STATION = 2081;
            public static final int PDP_FAIL_NO_SERVICE = 2216;
            public static final int PDP_FAIL_NO_SERVICE_ON_GATEWAY = 2093;
            public static final int PDP_FAIL_NSAPI_IN_USE = 35;
            public static final int PDP_FAIL_NULL_APN_DISALLOWED = 2061;
            public static final int PDP_FAIL_ONLY_IPV4V6_ALLOWED = 57;
            public static final int PDP_FAIL_ONLY_IPV4_ALLOWED = 50;
            public static final int PDP_FAIL_ONLY_IPV6_ALLOWED = 51;
            public static final int PDP_FAIL_ONLY_NON_IP_ALLOWED = 58;
            public static final int PDP_FAIL_ONLY_SINGLE_BEARER_ALLOWED = 52;
            public static final int PDP_FAIL_OPERATOR_BARRED = 8;
            public static final int PDP_FAIL_OTASP_COMMIT_IN_PROGRESS = 2208;
            public static final int PDP_FAIL_PDN_CONN_DOES_NOT_EXIST = 54;
            public static final int PDP_FAIL_PDN_INACTIVITY_TIMER_EXPIRED = 2051;
            public static final int PDP_FAIL_PDN_IPV4_CALL_DISALLOWED = 2033;
            public static final int PDP_FAIL_PDN_IPV4_CALL_THROTTLED = 2034;
            public static final int PDP_FAIL_PDN_IPV6_CALL_DISALLOWED = 2035;
            public static final int PDP_FAIL_PDN_IPV6_CALL_THROTTLED = 2036;
            public static final int PDP_FAIL_PDN_NON_IP_CALL_DISALLOWED = 2071;
            public static final int PDP_FAIL_PDN_NON_IP_CALL_THROTTLED = 2070;
            public static final int PDP_FAIL_PDP_ACTIVATE_MAX_RETRY_FAILED = 2109;
            public static final int PDP_FAIL_PDP_DUPLICATE = 2104;
            public static final int PDP_FAIL_PDP_ESTABLISH_TIMEOUT_EXPIRED = 2161;
            public static final int PDP_FAIL_PDP_INACTIVE_TIMEOUT_EXPIRED = 2163;
            public static final int PDP_FAIL_PDP_LOWERLAYER_ERROR = 2164;
            public static final int PDP_FAIL_PDP_MODIFY_COLLISION = 2165;
            public static final int PDP_FAIL_PDP_MODIFY_TIMEOUT_EXPIRED = 2162;
            public static final int PDP_FAIL_PDP_PPP_NOT_SUPPORTED = 2038;
            public static final int PDP_FAIL_PDP_WITHOUT_ACTIVE_TFT = 46;
            public static final int PDP_FAIL_PHONE_IN_USE = 2222;
            public static final int PDP_FAIL_PHYSICAL_LINK_CLOSE_IN_PROGRESS = 2040;
            public static final int PDP_FAIL_PLMN_NOT_ALLOWED = 2101;
            public static final int PDP_FAIL_PPP_AUTH_FAILURE = 2229;
            public static final int PDP_FAIL_PPP_CHAP_FAILURE = 2232;
            public static final int PDP_FAIL_PPP_CLOSE_IN_PROGRESS = 2233;
            public static final int PDP_FAIL_PPP_OPTION_MISMATCH = 2230;
            public static final int PDP_FAIL_PPP_PAP_FAILURE = 2231;
            public static final int PDP_FAIL_PPP_TIMEOUT = 2228;
            public static final int PDP_FAIL_PREF_RADIO_TECH_CHANGED = -4;
            public static final int PDP_FAIL_PROFILE_BEARER_INCOMPATIBLE = 2042;
            public static final int PDP_FAIL_PROTOCOL_ERRORS = 111;
            public static final int PDP_FAIL_QOS_NOT_ACCEPTED = 37;
            public static final int PDP_FAIL_RADIO_ACCESS_BEARER_FAILURE = 2110;
            public static final int PDP_FAIL_RADIO_ACCESS_BEARER_SETUP_FAILURE = 2160;
            public static final int PDP_FAIL_RADIO_POWER_OFF = -5;
            public static final int PDP_FAIL_REDIRECTION_OR_HANDOFF_IN_PROGRESS = 2220;
            public static final int PDP_FAIL_REGULAR_DEACTIVATION = 36;
            public static final int PDP_FAIL_REJECTED_BY_BASE_STATION = 2082;
            public static final int PDP_FAIL_RRC_CONNECTION_ABORTED_AFTER_HANDOVER = 2173;
            public static final int PDP_FAIL_RRC_CONNECTION_ABORTED_AFTER_IRAT_CELL_CHANGE = 2174;
            public static final int PDP_FAIL_RRC_CONNECTION_ABORTED_DUE_TO_IRAT_CHANGE = 2171;
            public static final int PDP_FAIL_RRC_CONNECTION_ABORTED_DURING_IRAT_CELL_CHANGE = 2175;
            public static final int PDP_FAIL_RRC_CONNECTION_ABORT_REQUEST = 2151;
            public static final int PDP_FAIL_RRC_CONNECTION_ACCESS_BARRED = 2139;
            public static final int PDP_FAIL_RRC_CONNECTION_ACCESS_STRATUM_FAILURE = 2137;
            public static final int PDP_FAIL_RRC_CONNECTION_ANOTHER_PROCEDURE_IN_PROGRESS = 2138;
            public static final int PDP_FAIL_RRC_CONNECTION_CELL_NOT_CAMPED = 2144;
            public static final int PDP_FAIL_RRC_CONNECTION_CELL_RESELECTION = 2140;
            public static final int PDP_FAIL_RRC_CONNECTION_CONFIG_FAILURE = 2141;
            public static final int PDP_FAIL_RRC_CONNECTION_INVALID_REQUEST = 2168;
            public static final int PDP_FAIL_RRC_CONNECTION_LINK_FAILURE = 2143;
            public static final int PDP_FAIL_RRC_CONNECTION_NORMAL_RELEASE = 2147;
            public static final int PDP_FAIL_RRC_CONNECTION_OUT_OF_SERVICE_DURING_CELL_REGISTER = 2150;
            public static final int PDP_FAIL_RRC_CONNECTION_RADIO_LINK_FAILURE = 2148;
            public static final int PDP_FAIL_RRC_CONNECTION_REESTABLISHMENT_FAILURE = 2149;
            public static final int PDP_FAIL_RRC_CONNECTION_REJECT_BY_NETWORK = 2146;
            public static final int PDP_FAIL_RRC_CONNECTION_RELEASED_SECURITY_NOT_ACTIVE = 2172;
            public static final int PDP_FAIL_RRC_CONNECTION_RF_UNAVAILABLE = 2170;
            public static final int PDP_FAIL_RRC_CONNECTION_SYSTEM_INFORMATION_BLOCK_READ_ERROR = 2152;
            public static final int PDP_FAIL_RRC_CONNECTION_SYSTEM_INTERVAL_FAILURE = 2145;
            public static final int PDP_FAIL_RRC_CONNECTION_TIMER_EXPIRED = 2142;
            public static final int PDP_FAIL_RRC_CONNECTION_TRACKING_AREA_ID_CHANGED = 2169;
            public static final int PDP_FAIL_RRC_UPLINK_CONNECTION_RELEASE = 2134;
            public static final int PDP_FAIL_RRC_UPLINK_DATA_TRANSMISSION_FAILURE = 2132;
            public static final int PDP_FAIL_RRC_UPLINK_DELIVERY_FAILED_DUE_TO_HANDOVER = 2133;
            public static final int PDP_FAIL_RRC_UPLINK_ERROR_REQUEST_FROM_NAS = 2136;
            public static final int PDP_FAIL_RRC_UPLINK_RADIO_LINK_FAILURE = 2135;
            public static final int PDP_FAIL_RUIM_NOT_PRESENT = 2085;
            public static final int PDP_FAIL_SECURITY_MODE_REJECTED = 2186;
            public static final int PDP_FAIL_SERVICE_NOT_ALLOWED_ON_PLMN = 2129;
            public static final int PDP_FAIL_SERVICE_OPTION_NOT_SUBSCRIBED = 33;
            public static final int PDP_FAIL_SERVICE_OPTION_NOT_SUPPORTED = 32;
            public static final int PDP_FAIL_SERVICE_OPTION_OUT_OF_ORDER = 34;
            public static final int PDP_FAIL_SIGNAL_LOST = -3;
            public static final int PDP_FAIL_SIM_CARD_CHANGED = 2043;
            public static final int PDP_FAIL_SYNCHRONIZATION_FAILURE = 2184;
            public static final int PDP_FAIL_TEST_LOOPBACK_REGULAR_DEACTIVATION = 2196;
            public static final int PDP_FAIL_TETHERED_CALL_ACTIVE = -6;
            public static final int PDP_FAIL_TFT_SEMANTIC_ERROR = 41;
            public static final int PDP_FAIL_TFT_SYTAX_ERROR = 42;
            public static final int PDP_FAIL_THERMAL_EMERGENCY = 2090;
            public static final int PDP_FAIL_THERMAL_MITIGATION = 2062;
            public static final int PDP_FAIL_TRAT_SWAP_FAILED = 2048;
            public static final int PDP_FAIL_UE_INITIATED_DETACH_OR_DISCONNECT = 128;
            public static final int PDP_FAIL_UE_IS_ENTERING_POWERSAVE_MODE = 2226;
            public static final int PDP_FAIL_UE_RAT_CHANGE = 2105;
            public static final int PDP_FAIL_UE_SECURITY_CAPABILITIES_MISMATCH = 2185;
            public static final int PDP_FAIL_UMTS_HANDOVER_TO_IWLAN = 2199;
            public static final int PDP_FAIL_UMTS_REACTIVATION_REQ = 39;
            public static final int PDP_FAIL_UNACCEPTABLE_NON_EPS_AUTHENTICATION = 2187;
            public static final int PDP_FAIL_UNKNOWN = 0;
            public static final int PDP_FAIL_UNKNOWN_INFO_ELEMENT = 99;
            public static final int PDP_FAIL_UNKNOWN_PDP_ADDRESS_TYPE = 28;
            public static final int PDP_FAIL_UNKNOWN_PDP_CONTEXT = 43;
            public static final int PDP_FAIL_UNPREFERRED_RAT = 2039;
            public static final int PDP_FAIL_UNSUPPORTED_1X_PREV = 2214;
            public static final int PDP_FAIL_UNSUPPORTED_APN_IN_CURRENT_PLMN = 66;
            public static final int PDP_FAIL_UNSUPPORTED_QCI_VALUE = 59;
            public static final int PDP_FAIL_USER_AUTHENTICATION = 29;
            public static final int PDP_FAIL_VOICE_REGISTRATION_FAIL = -1;
            public static final int PDP_FAIL_VSNCP_ADMINISTRATIVELY_PROHIBITED = 2245;
            public static final int PDP_FAIL_VSNCP_APN_UNATHORIZED = 2238;
            public static final int PDP_FAIL_VSNCP_GEN_ERROR = 2237;
            public static final int PDP_FAIL_VSNCP_INSUFFICIENT_PARAMETERS = 2243;
            public static final int PDP_FAIL_VSNCP_NO_PDN_GATEWAY_ADDRESS = 2240;
            public static final int PDP_FAIL_VSNCP_PDN_EXISTS_FOR_THIS_APN = 2248;
            public static final int PDP_FAIL_VSNCP_PDN_GATEWAY_REJECT = 2242;
            public static final int PDP_FAIL_VSNCP_PDN_GATEWAY_UNREACHABLE = 2241;
            public static final int PDP_FAIL_VSNCP_PDN_ID_IN_USE = 2246;
            public static final int PDP_FAIL_VSNCP_PDN_LIMIT_EXCEEDED = 2239;
            public static final int PDP_FAIL_VSNCP_RECONNECT_NOT_ALLOWED = 2249;
            public static final int PDP_FAIL_VSNCP_RESOURCE_UNAVAILABLE = 2244;
            public static final int PDP_FAIL_VSNCP_SUBSCRIBER_LIMITATION = 2247;
            public static final int PDP_FAIL_VSNCP_TIMEOUT = 2236;
        }

        public static RilSetupDataCallResponse[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new RilSetupDataCallResponse[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public RilSetupDataCallResponse() {
            clear();
        }

        public RilSetupDataCallResponse clear() {
            this.status = 0;
            this.suggestedRetryTimeMillis = 0;
            this.call = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.status;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            int i2 = this.suggestedRetryTimeMillis;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeInt32(2, i2);
            }
            TelephonyProto$RilDataCall telephonyProto$RilDataCall = this.call;
            if (telephonyProto$RilDataCall != null) {
                codedOutputByteBufferNano.writeMessage(3, telephonyProto$RilDataCall);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.status;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            int i2 = this.suggestedRetryTimeMillis;
            if (i2 != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
            }
            TelephonyProto$RilDataCall telephonyProto$RilDataCall = this.call;
            return telephonyProto$RilDataCall != null ? iComputeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(3, telephonyProto$RilDataCall) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public RilSetupDataCallResponse mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag != 0) {
                    if (tag == 8) {
                        int int32 = codedInputByteBufferNano.readInt32();
                        if (int32 != 8 && int32 != 14 && int32 != 48 && int32 != 81 && int32 != 65535 && int32 != 65 && int32 != 66 && int32 != 127 && int32 != 128) {
                            switch (int32) {
                                case -6:
                                case -5:
                                case -4:
                                case -3:
                                case -2:
                                case -1:
                                case 0:
                                case 1:
                                    break;
                                default:
                                    switch (int32) {
                                        case 25:
                                        case 26:
                                        case 27:
                                        case 28:
                                        case 29:
                                        case 30:
                                        case 31:
                                        case 32:
                                        case 33:
                                        case 34:
                                        case 35:
                                        case 36:
                                        case 37:
                                        case 38:
                                        case 39:
                                        case 40:
                                        case 41:
                                        case 42:
                                        case 43:
                                        case 44:
                                        case 45:
                                        case 46:
                                            break;
                                        default:
                                            switch (int32) {
                                                case 50:
                                                case 51:
                                                case 52:
                                                case 53:
                                                case 54:
                                                case 55:
                                                case 56:
                                                case 57:
                                                case 58:
                                                case 59:
                                                case 60:
                                                    break;
                                                default:
                                                    switch (int32) {
                                                        case 95:
                                                        case 96:
                                                        case 97:
                                                        case 98:
                                                        case 99:
                                                        case 100:
                                                        case 101:
                                                            break;
                                                        default:
                                                            switch (int32) {
                                                                case 111:
                                                                case 112:
                                                                case 113:
                                                                case 114:
                                                                case 115:
                                                                case 116:
                                                                case 117:
                                                                case 118:
                                                                case 119:
                                                                case 120:
                                                                case 121:
                                                                case 122:
                                                                case 123:
                                                                case 124:
                                                                    break;
                                                                default:
                                                                    switch (int32) {
                                                                        case 2000:
                                                                        case 2001:
                                                                        case 2002:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_MOBILE_NODE_AUTHENTICATION_FAILURE /* 2003 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_HOME_AGENT_AUTHENTICATION_FAILURE /* 2004 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_REQUESTED_LIFETIME_TOO_LONG /* 2005 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_MALFORMED_REQUEST /* 2006 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_MALFORMED_REPLY /* 2007 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_ENCAPSULATION_UNAVAILABLE /* 2008 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_VJ_HEADER_COMPRESSION_UNAVAILABLE /* 2009 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_REVERSE_TUNNEL_UNAVAILABLE /* 2010 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_REVERSE_TUNNEL_IS_MANDATORY /* 2011 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_DELIVERY_STYLE_NOT_SUPPORTED /* 2012 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_MISSING_NAI /* 2013 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_MISSING_HOME_AGENT /* 2014 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_MISSING_HOME_ADDRESS /* 2015 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_UNKNOWN_CHALLENGE /* 2016 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_MISSING_CHALLENGE /* 2017 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_FA_STALE_CHALLENGE /* 2018 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_REASON_UNSPECIFIED /* 2019 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_ADMIN_PROHIBITED /* 2020 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_INSUFFICIENT_RESOURCES /* 2021 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_MOBILE_NODE_AUTHENTICATION_FAILURE /* 2022 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_FOREIGN_AGENT_AUTHENTICATION_FAILURE /* 2023 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_REGISTRATION_ID_MISMATCH /* 2024 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_MALFORMED_REQUEST /* 2025 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_UNKNOWN_HOME_AGENT_ADDRESS /* 2026 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_REVERSE_TUNNEL_UNAVAILABLE /* 2027 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_REVERSE_TUNNEL_IS_MANDATORY /* 2028 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_HA_ENCAPSULATION_UNAVAILABLE /* 2029 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CLOSE_IN_PROGRESS /* 2030 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NETWORK_INITIATED_TERMINATION /* 2031 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MODEM_APP_PREEMPTED /* 2032 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDN_IPV4_CALL_DISALLOWED /* 2033 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDN_IPV4_CALL_THROTTLED /* 2034 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDN_IPV6_CALL_DISALLOWED /* 2035 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDN_IPV6_CALL_THROTTLED /* 2036 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MODEM_RESTART /* 2037 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDP_PPP_NOT_SUPPORTED /* 2038 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_UNPREFERRED_RAT /* 2039 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PHYSICAL_LINK_CLOSE_IN_PROGRESS /* 2040 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_APN_PENDING_HANDOVER /* 2041 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PROFILE_BEARER_INCOMPATIBLE /* 2042 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_SIM_CARD_CHANGED /* 2043 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_LOW_POWER_MODE_OR_POWERING_DOWN /* 2044 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_APN_DISABLED /* 2045 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MAX_PPP_INACTIVITY_TIMER_EXPIRED /* 2046 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_IPV6_ADDRESS_TRANSFER_FAILED /* 2047 */:
                                                                        case 2048:
                                                                        case RilDataCallFailCause.PDP_FAIL_EHRPD_TO_HRPD_FALLBACK /* 2049 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MIP_CONFIG_FAILURE /* 2050 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDN_INACTIVITY_TIMER_EXPIRED /* 2051 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MAX_IPV4_CONNECTIONS /* 2052 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MAX_IPV6_CONNECTIONS /* 2053 */:
                                                                        case 2054:
                                                                        case RilDataCallFailCause.PDP_FAIL_IP_VERSION_MISMATCH /* 2055 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DUN_CALL_DISALLOWED /* 2056 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_INTERNAL_EPC_NONEPC_TRANSITION /* 2057 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_INTERFACE_IN_USE /* 2058 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_APN_DISALLOWED_ON_ROAMING /* 2059 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_APN_PARAMETERS_CHANGED /* 2060 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NULL_APN_DISALLOWED /* 2061 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_THERMAL_MITIGATION /* 2062 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DATA_SETTINGS_DISABLED /* 2063 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DATA_ROAMING_SETTINGS_DISABLED /* 2064 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DDS_SWITCHED /* 2065 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_FORBIDDEN_APN_NAME /* 2066 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DDS_SWITCH_IN_PROGRESS /* 2067 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CALL_DISALLOWED_IN_ROAMING /* 2068 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NON_IP_NOT_SUPPORTED /* 2069 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDN_NON_IP_CALL_THROTTLED /* 2070 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDN_NON_IP_CALL_DISALLOWED /* 2071 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CDMA_LOCK /* 2072 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CDMA_INTERCEPT /* 2073 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CDMA_REORDER /* 2074 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CDMA_RELEASE_DUE_TO_SO_REJECTION /* 2075 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CDMA_INCOMING_CALL /* 2076 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CDMA_ALERT_STOP /* 2077 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CHANNEL_ACQUISITION_FAILURE /* 2078 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MAX_ACCESS_PROBE /* 2079 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CONCURRENT_SERVICE_NOT_SUPPORTED_BY_BASE_STATION /* 2080 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_RESPONSE_FROM_BASE_STATION /* 2081 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_REJECTED_BY_BASE_STATION /* 2082 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CONCURRENT_SERVICES_INCOMPATIBLE /* 2083 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_CDMA_SERVICE /* 2084 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RUIM_NOT_PRESENT /* 2085 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CDMA_RETRY_ORDER /* 2086 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ACCESS_BLOCK /* 2087 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ACCESS_BLOCK_ALL /* 2088 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_IS707B_MAX_ACCESS_PROBES /* 2089 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_THERMAL_EMERGENCY /* 2090 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CONCURRENT_SERVICES_NOT_ALLOWED /* 2091 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_INCOMING_CALL_REJECTED /* 2092 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_SERVICE_ON_GATEWAY /* 2093 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_GPRS_CONTEXT /* 2094 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ILLEGAL_MS /* 2095 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ILLEGAL_ME /* 2096 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_GPRS_SERVICES_AND_NON_GPRS_SERVICES_NOT_ALLOWED /* 2097 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_GPRS_SERVICES_NOT_ALLOWED /* 2098 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MS_IDENTITY_CANNOT_BE_DERIVED_BY_THE_NETWORK /* 2099 */:
                                                                        case 2100:
                                                                        case 2101:
                                                                        case 2102:
                                                                        case RilDataCallFailCause.PDP_FAIL_GPRS_SERVICES_NOT_ALLOWED_IN_THIS_PLMN /* 2103 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDP_DUPLICATE /* 2104 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_UE_RAT_CHANGE /* 2105 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CONGESTION /* 2106 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_PDP_CONTEXT_ACTIVATED /* 2107 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ACCESS_CLASS_DSAC_REJECTION /* 2108 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDP_ACTIVATE_MAX_RETRY_FAILED /* 2109 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RADIO_ACCESS_BEARER_FAILURE /* 2110 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_UNKNOWN_EPS_BEARER_CONTEXT /* 2111 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DRB_RELEASED_BY_RRC /* 2112 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CONNECTION_RELEASED /* 2113 */:
                                                                        case 2114:
                                                                        case RilDataCallFailCause.PDP_FAIL_EMM_ATTACH_FAILED /* 2115 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EMM_ATTACH_STARTED /* 2116 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_LTE_NAS_SERVICE_REQUEST_FAILED /* 2117 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DUPLICATE_BEARER_ID /* 2118 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_COLLISION_SCENARIOS /* 2119 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_BEARER_DEACTIVATED_TO_SYNC_WITH_NETWORK /* 2120 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_NW_ACTIVATED_DED_BEARER_WITH_ID_OF_DEF_BEARER /* 2121 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_BAD_OTA_MESSAGE /* 2122 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_DOWNLOAD_SERVER_REJECTED_THE_CALL /* 2123 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_CONTEXT_TRANSFERRED_DUE_TO_IRAT /* 2124 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DS_EXPLICIT_DEACTIVATION /* 2125 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_LOCAL_CAUSE_NONE /* 2126 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_LTE_THROTTLING_NOT_REQUIRED /* 2127 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ACCESS_CONTROL_LIST_CHECK_FAILURE /* 2128 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_SERVICE_NOT_ALLOWED_ON_PLMN /* 2129 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EMM_T3417_EXPIRED /* 2130 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EMM_T3417_EXT_EXPIRED /* 2131 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_UPLINK_DATA_TRANSMISSION_FAILURE /* 2132 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_UPLINK_DELIVERY_FAILED_DUE_TO_HANDOVER /* 2133 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_UPLINK_CONNECTION_RELEASE /* 2134 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_UPLINK_RADIO_LINK_FAILURE /* 2135 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_UPLINK_ERROR_REQUEST_FROM_NAS /* 2136 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_ACCESS_STRATUM_FAILURE /* 2137 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_ANOTHER_PROCEDURE_IN_PROGRESS /* 2138 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_ACCESS_BARRED /* 2139 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_CELL_RESELECTION /* 2140 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_CONFIG_FAILURE /* 2141 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_TIMER_EXPIRED /* 2142 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_LINK_FAILURE /* 2143 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_CELL_NOT_CAMPED /* 2144 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_SYSTEM_INTERVAL_FAILURE /* 2145 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_REJECT_BY_NETWORK /* 2146 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_NORMAL_RELEASE /* 2147 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_RADIO_LINK_FAILURE /* 2148 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_REESTABLISHMENT_FAILURE /* 2149 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_OUT_OF_SERVICE_DURING_CELL_REGISTER /* 2150 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_ABORT_REQUEST /* 2151 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_SYSTEM_INFORMATION_BLOCK_READ_ERROR /* 2152 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NETWORK_INITIATED_DETACH_WITH_AUTO_REATTACH /* 2153 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NETWORK_INITIATED_DETACH_NO_AUTO_REATTACH /* 2154 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_PROCEDURE_TIME_OUT /* 2155 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_INVALID_CONNECTION_ID /* 2156 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MAXIMIUM_NSAPIS_EXCEEDED /* 2157 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_INVALID_PRIMARY_NSAPI /* 2158 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CANNOT_ENCODE_OTA_MESSAGE /* 2159 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RADIO_ACCESS_BEARER_SETUP_FAILURE /* 2160 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDP_ESTABLISH_TIMEOUT_EXPIRED /* 2161 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDP_MODIFY_TIMEOUT_EXPIRED /* 2162 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDP_INACTIVE_TIMEOUT_EXPIRED /* 2163 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDP_LOWERLAYER_ERROR /* 2164 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PDP_MODIFY_COLLISION /* 2165 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MAXINUM_SIZE_OF_L2_MESSAGE_EXCEEDED /* 2166 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NAS_REQUEST_REJECTED_BY_NETWORK /* 2167 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_INVALID_REQUEST /* 2168 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_TRACKING_AREA_ID_CHANGED /* 2169 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_RF_UNAVAILABLE /* 2170 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_ABORTED_DUE_TO_IRAT_CHANGE /* 2171 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_RELEASED_SECURITY_NOT_ACTIVE /* 2172 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_ABORTED_AFTER_HANDOVER /* 2173 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_ABORTED_AFTER_IRAT_CELL_CHANGE /* 2174 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_RRC_CONNECTION_ABORTED_DURING_IRAT_CELL_CHANGE /* 2175 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_IMSI_UNKNOWN_IN_HOME_SUBSCRIBER_SERVER /* 2176 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_IMEI_NOT_ACCEPTED /* 2177 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EPS_SERVICES_AND_NON_EPS_SERVICES_NOT_ALLOWED /* 2178 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EPS_SERVICES_NOT_ALLOWED_IN_PLMN /* 2179 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MSC_TEMPORARILY_NOT_REACHABLE /* 2180 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CS_DOMAIN_NOT_AVAILABLE /* 2181 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ESM_FAILURE /* 2182 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MAC_FAILURE /* 2183 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_SYNCHRONIZATION_FAILURE /* 2184 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_UE_SECURITY_CAPABILITIES_MISMATCH /* 2185 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_SECURITY_MODE_REJECTED /* 2186 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_UNACCEPTABLE_NON_EPS_AUTHENTICATION /* 2187 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_CS_FALLBACK_CALL_ESTABLISHMENT_NOT_ALLOWED /* 2188 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_EPS_BEARER_CONTEXT_ACTIVATED /* 2189 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_INVALID_EMM_STATE /* 2190 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NAS_LAYER_FAILURE /* 2191 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_MULTIPLE_PDP_CALL_NOT_ALLOWED /* 2192 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EMBMS_NOT_ENABLED /* 2193 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_IRAT_HANDOVER_FAILED /* 2194 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EMBMS_REGULAR_DEACTIVATION /* 2195 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_TEST_LOOPBACK_REGULAR_DEACTIVATION /* 2196 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_LOWER_LAYER_REGISTRATION_FAILURE /* 2197 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DATA_PLAN_EXPIRED /* 2198 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_UMTS_HANDOVER_TO_IWLAN /* 2199 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EVDO_CONNECTION_DENY_BY_GENERAL_OR_NETWORK_BUSY /* 2200 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EVDO_CONNECTION_DENY_BY_BILLING_OR_AUTHENTICATION_FAILURE /* 2201 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EVDO_HDR_CHANGED /* 2202 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EVDO_HDR_EXITED /* 2203 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EVDO_HDR_NO_SESSION /* 2204 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EVDO_USING_GPS_FIX_INSTEAD_OF_HDR_CALL /* 2205 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EVDO_HDR_CONNECTION_SETUP_TIMEOUT /* 2206 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_FAILED_TO_ACQUIRE_COLOCATED_HDR /* 2207 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_OTASP_COMMIT_IN_PROGRESS /* 2208 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_HYBRID_HDR_SERVICE /* 2209 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_HDR_NO_LOCK_GRANTED /* 2210 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DBM_OR_SMS_IN_PROGRESS /* 2211 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_HDR_FADE /* 2212 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_HDR_ACCESS_FAILURE /* 2213 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_UNSUPPORTED_1X_PREV /* 2214 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_LOCAL_END /* 2215 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_SERVICE /* 2216 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_FADE /* 2217 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NORMAL_RELEASE /* 2218 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_ACCESS_ATTEMPT_ALREADY_IN_PROGRESS /* 2219 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_REDIRECTION_OR_HANDOFF_IN_PROGRESS /* 2220 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_EMERGENCY_MODE /* 2221 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PHONE_IN_USE /* 2222 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_INVALID_MODE /* 2223 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_INVALID_SIM_STATE /* 2224 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_NO_COLLOCATED_HDR /* 2225 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_UE_IS_ENTERING_POWERSAVE_MODE /* 2226 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_DUAL_SWITCH /* 2227 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PPP_TIMEOUT /* 2228 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PPP_AUTH_FAILURE /* 2229 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PPP_OPTION_MISMATCH /* 2230 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PPP_PAP_FAILURE /* 2231 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PPP_CHAP_FAILURE /* 2232 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_PPP_CLOSE_IN_PROGRESS /* 2233 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_LIMITED_TO_IPV4 /* 2234 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_LIMITED_TO_IPV6 /* 2235 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_TIMEOUT /* 2236 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_GEN_ERROR /* 2237 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_APN_UNATHORIZED /* 2238 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_PDN_LIMIT_EXCEEDED /* 2239 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_NO_PDN_GATEWAY_ADDRESS /* 2240 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_PDN_GATEWAY_UNREACHABLE /* 2241 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_PDN_GATEWAY_REJECT /* 2242 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_INSUFFICIENT_PARAMETERS /* 2243 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_RESOURCE_UNAVAILABLE /* 2244 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_ADMINISTRATIVELY_PROHIBITED /* 2245 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_PDN_ID_IN_USE /* 2246 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_SUBSCRIBER_LIMITATION /* 2247 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_PDN_EXISTS_FOR_THIS_APN /* 2248 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_VSNCP_RECONNECT_NOT_ALLOWED /* 2249 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_IPV6_PREFIX_UNAVAILABLE /* 2250 */:
                                                                        case RilDataCallFailCause.PDP_FAIL_HANDOFF_PREFERENCE_CHANGED /* 2251 */:
                                                                            break;
                                                                        default:
                                                                            continue;
                                                                    }
                                                                    break;
                                                            }
                                                            break;
                                                    }
                                                    break;
                                            }
                                            break;
                                    }
                                    break;
                            }
                        }
                        this.status = int32;
                    } else if (tag == 16) {
                        this.suggestedRetryTimeMillis = codedInputByteBufferNano.readInt32();
                    } else if (tag != 26) {
                        if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        }
                    } else {
                        if (this.call == null) {
                            this.call = new TelephonyProto$RilDataCall();
                        }
                        codedInputByteBufferNano.readMessage(this.call);
                    }
                }
            }
            return this;
        }

        public static RilSetupDataCallResponse parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (RilSetupDataCallResponse) MessageNano.mergeFrom(new RilSetupDataCallResponse(), bArr);
        }

        public static RilSetupDataCallResponse parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new RilSetupDataCallResponse().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class CarrierKeyChange extends ExtendableMessageNano<CarrierKeyChange> {
        private static volatile CarrierKeyChange[] _emptyArray;
        public boolean isDownloadSuccessful;
        public int keyType;

        public interface KeyType {
            public static final int EPDG = 2;
            public static final int UNKNOWN = 0;
            public static final int WLAN = 1;
        }

        public static CarrierKeyChange[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new CarrierKeyChange[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public CarrierKeyChange() {
            clear();
        }

        public CarrierKeyChange clear() {
            this.keyType = 0;
            this.isDownloadSuccessful = false;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.keyType;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            boolean z = this.isDownloadSuccessful;
            if (z) {
                codedOutputByteBufferNano.writeBool(2, z);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.keyType;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            boolean z = this.isDownloadSuccessful;
            return z ? iComputeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(2, z) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public CarrierKeyChange mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    break;
                }
                if (tag == 8) {
                    int int32 = codedInputByteBufferNano.readInt32();
                    if (int32 == 0 || int32 == 1 || int32 == 2) {
                        this.keyType = int32;
                    }
                } else if (tag != 16) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        break;
                    }
                } else {
                    this.isDownloadSuccessful = codedInputByteBufferNano.readBool();
                }
            }
            return this;
        }

        public static CarrierKeyChange parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (CarrierKeyChange) MessageNano.mergeFrom(new CarrierKeyChange(), bArr);
        }

        public static CarrierKeyChange parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new CarrierKeyChange().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class RilDeactivateDataCall extends ExtendableMessageNano<RilDeactivateDataCall> {
        private static volatile RilDeactivateDataCall[] _emptyArray;
        public int cid;
        public int reason;

        public interface DeactivateReason {
            public static final int DEACTIVATE_REASON_HANDOVER = 4;
            public static final int DEACTIVATE_REASON_NONE = 1;
            public static final int DEACTIVATE_REASON_PDP_RESET = 3;
            public static final int DEACTIVATE_REASON_RADIO_OFF = 2;
            public static final int DEACTIVATE_REASON_UNKNOWN = 0;
        }

        public static RilDeactivateDataCall[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new RilDeactivateDataCall[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public RilDeactivateDataCall() {
            clear();
        }

        public RilDeactivateDataCall clear() {
            this.cid = 0;
            this.reason = 0;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.cid;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            int i2 = this.reason;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeInt32(2, i2);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.cid;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            int i2 = this.reason;
            return i2 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, i2) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public RilDeactivateDataCall mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    break;
                }
                if (tag == 8) {
                    this.cid = codedInputByteBufferNano.readInt32();
                } else if (tag != 16) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        break;
                    }
                } else {
                    int int32 = codedInputByteBufferNano.readInt32();
                    if (int32 == 0 || int32 == 1 || int32 == 2 || int32 == 3 || int32 == 4) {
                        this.reason = int32;
                    }
                }
            }
            return this;
        }

        public static RilDeactivateDataCall parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (RilDeactivateDataCall) MessageNano.mergeFrom(new RilDeactivateDataCall(), bArr);
        }

        public static RilDeactivateDataCall parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new RilDeactivateDataCall().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class ModemRestart extends ExtendableMessageNano<ModemRestart> {
        private static volatile ModemRestart[] _emptyArray;
        public String basebandVersion;
        public String reason;

        public static ModemRestart[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new ModemRestart[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public ModemRestart() {
            clear();
        }

        public ModemRestart clear() {
            this.basebandVersion = "";
            this.reason = "";
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (!this.basebandVersion.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.basebandVersion);
            }
            if (!this.reason.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.reason);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (!this.basebandVersion.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.basebandVersion);
            }
            return !this.reason.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.reason) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public ModemRestart mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    break;
                }
                if (tag == 10) {
                    this.basebandVersion = codedInputByteBufferNano.readString();
                } else if (tag != 18) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        break;
                    }
                } else {
                    this.reason = codedInputByteBufferNano.readString();
                }
            }
            return this;
        }

        public static ModemRestart parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (ModemRestart) MessageNano.mergeFrom(new ModemRestart(), bArr);
        }

        public static ModemRestart parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new ModemRestart().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class CarrierIdMatching extends ExtendableMessageNano<CarrierIdMatching> {
        private static volatile CarrierIdMatching[] _emptyArray;
        public int cidTableVersion;
        public CarrierIdMatchingResult result;

        public static CarrierIdMatching[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new CarrierIdMatching[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public CarrierIdMatching() {
            clear();
        }

        public CarrierIdMatching clear() {
            this.cidTableVersion = 0;
            this.result = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.cidTableVersion;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            CarrierIdMatchingResult carrierIdMatchingResult = this.result;
            if (carrierIdMatchingResult != null) {
                codedOutputByteBufferNano.writeMessage(2, carrierIdMatchingResult);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.cidTableVersion;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            CarrierIdMatchingResult carrierIdMatchingResult = this.result;
            return carrierIdMatchingResult != null ? iComputeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, carrierIdMatchingResult) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public CarrierIdMatching mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    break;
                }
                if (tag == 8) {
                    this.cidTableVersion = codedInputByteBufferNano.readInt32();
                } else if (tag != 18) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        break;
                    }
                } else {
                    if (this.result == null) {
                        this.result = new CarrierIdMatchingResult();
                    }
                    codedInputByteBufferNano.readMessage(this.result);
                }
            }
            return this;
        }

        public static CarrierIdMatching parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (CarrierIdMatching) MessageNano.mergeFrom(new CarrierIdMatching(), bArr);
        }

        public static CarrierIdMatching parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new CarrierIdMatching().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class CarrierIdMatchingResult extends ExtendableMessageNano<CarrierIdMatchingResult> {
        private static volatile CarrierIdMatchingResult[] _emptyArray;
        public int carrierId;
        public String gid1;
        public String gid2;
        public String iccidPrefix;
        public String imsiPrefix;
        public String mccmnc;
        public String pnn;
        public String preferApn;
        public String[] privilegeAccessRule;
        public String spn;
        public String unknownGid1;
        public String unknownMccmnc;

        public static CarrierIdMatchingResult[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new CarrierIdMatchingResult[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public CarrierIdMatchingResult() {
            clear();
        }

        public CarrierIdMatchingResult clear() {
            this.carrierId = 0;
            this.unknownGid1 = "";
            this.unknownMccmnc = "";
            this.mccmnc = "";
            this.gid1 = "";
            this.gid2 = "";
            this.spn = "";
            this.pnn = "";
            this.iccidPrefix = "";
            this.imsiPrefix = "";
            this.privilegeAccessRule = WireFormatNano.EMPTY_STRING_ARRAY;
            this.preferApn = "";
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            int i = this.carrierId;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(1, i);
            }
            if (!this.unknownGid1.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.unknownGid1);
            }
            if (!this.unknownMccmnc.equals("")) {
                codedOutputByteBufferNano.writeString(3, this.unknownMccmnc);
            }
            if (!this.mccmnc.equals("")) {
                codedOutputByteBufferNano.writeString(4, this.mccmnc);
            }
            if (!this.gid1.equals("")) {
                codedOutputByteBufferNano.writeString(5, this.gid1);
            }
            if (!this.gid2.equals("")) {
                codedOutputByteBufferNano.writeString(6, this.gid2);
            }
            if (!this.spn.equals("")) {
                codedOutputByteBufferNano.writeString(7, this.spn);
            }
            if (!this.pnn.equals("")) {
                codedOutputByteBufferNano.writeString(8, this.pnn);
            }
            if (!this.iccidPrefix.equals("")) {
                codedOutputByteBufferNano.writeString(9, this.iccidPrefix);
            }
            if (!this.imsiPrefix.equals("")) {
                codedOutputByteBufferNano.writeString(10, this.imsiPrefix);
            }
            String[] strArr = this.privilegeAccessRule;
            if (strArr != null && strArr.length > 0) {
                int i2 = 0;
                while (true) {
                    String[] strArr2 = this.privilegeAccessRule;
                    if (i2 >= strArr2.length) {
                        break;
                    }
                    String str = strArr2[i2];
                    if (str != null) {
                        codedOutputByteBufferNano.writeString(11, str);
                    }
                    i2++;
                }
            }
            if (!this.preferApn.equals("")) {
                codedOutputByteBufferNano.writeString(12, this.preferApn);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            int i = this.carrierId;
            if (i != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
            }
            if (!this.unknownGid1.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.unknownGid1);
            }
            if (!this.unknownMccmnc.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.unknownMccmnc);
            }
            if (!this.mccmnc.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(4, this.mccmnc);
            }
            if (!this.gid1.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(5, this.gid1);
            }
            if (!this.gid2.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(6, this.gid2);
            }
            if (!this.spn.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(7, this.spn);
            }
            if (!this.pnn.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(8, this.pnn);
            }
            if (!this.iccidPrefix.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(9, this.iccidPrefix);
            }
            if (!this.imsiPrefix.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(10, this.imsiPrefix);
            }
            String[] strArr = this.privilegeAccessRule;
            if (strArr != null && strArr.length > 0) {
                int i2 = 0;
                int iComputeStringSizeNoTag = 0;
                int i3 = 0;
                while (true) {
                    String[] strArr2 = this.privilegeAccessRule;
                    if (i2 >= strArr2.length) {
                        break;
                    }
                    String str = strArr2[i2];
                    if (str != null) {
                        i3++;
                        iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                    }
                    i2++;
                }
                iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag + i3;
            }
            return !this.preferApn.equals("") ? iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(12, this.preferApn) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public CarrierIdMatchingResult mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    case 0:
                        break;
                    case 8:
                        this.carrierId = codedInputByteBufferNano.readInt32();
                        break;
                    case 18:
                        this.unknownGid1 = codedInputByteBufferNano.readString();
                        break;
                    case 26:
                        this.unknownMccmnc = codedInputByteBufferNano.readString();
                        break;
                    case 34:
                        this.mccmnc = codedInputByteBufferNano.readString();
                        break;
                    case 42:
                        this.gid1 = codedInputByteBufferNano.readString();
                        break;
                    case 50:
                        this.gid2 = codedInputByteBufferNano.readString();
                        break;
                    case 58:
                        this.spn = codedInputByteBufferNano.readString();
                        break;
                    case 66:
                        this.pnn = codedInputByteBufferNano.readString();
                        break;
                    case 74:
                        this.iccidPrefix = codedInputByteBufferNano.readString();
                        break;
                    case 82:
                        this.imsiPrefix = codedInputByteBufferNano.readString();
                        break;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GSM_SET_BROADCAST_CONFIG /* 90 */:
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 90);
                        String[] strArr = this.privilegeAccessRule;
                        int length = strArr == null ? 0 : strArr.length;
                        int i = repeatedFieldArrayLength + length;
                        String[] strArr2 = new String[i];
                        if (length != 0) {
                            System.arraycopy(strArr, 0, strArr2, 0, length);
                        }
                        while (length < i - 1) {
                            strArr2[length] = codedInputByteBufferNano.readString();
                            codedInputByteBufferNano.readTag();
                            length++;
                        }
                        strArr2[length] = codedInputByteBufferNano.readString();
                        this.privilegeAccessRule = strArr2;
                        break;
                    case 98:
                        this.preferApn = codedInputByteBufferNano.readString();
                        break;
                    default:
                        if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        }
                        break;
                }
            }
            return this;
        }

        public static CarrierIdMatchingResult parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (CarrierIdMatchingResult) MessageNano.mergeFrom(new CarrierIdMatchingResult(), bArr);
        }

        public static CarrierIdMatchingResult parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new CarrierIdMatchingResult().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static final class NetworkCapabilitiesInfo extends ExtendableMessageNano<NetworkCapabilitiesInfo> {
        private static volatile NetworkCapabilitiesInfo[] _emptyArray;
        public boolean isNetworkUnmetered;

        public static NetworkCapabilitiesInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    try {
                        if (_emptyArray == null) {
                            _emptyArray = new NetworkCapabilitiesInfo[0];
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return _emptyArray;
        }

        public NetworkCapabilitiesInfo() {
            clear();
        }

        public NetworkCapabilitiesInfo clear() {
            this.isNetworkUnmetered = false;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            boolean z = this.isNetworkUnmetered;
            if (z) {
                codedOutputByteBufferNano.writeBool(1, z);
            }
            super.writeTo(codedOutputByteBufferNano);
        }

        @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            boolean z = this.isNetworkUnmetered;
            return z ? iComputeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(1, z) : iComputeSerializedSize;
        }

        @Override // com.android.internal.telephony.protobuf.nano.MessageNano
        public NetworkCapabilitiesInfo mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    break;
                }
                if (tag != 8) {
                    if (!storeUnknownField(codedInputByteBufferNano, tag)) {
                        break;
                    }
                } else {
                    this.isNetworkUnmetered = codedInputByteBufferNano.readBool();
                }
            }
            return this;
        }

        public static NetworkCapabilitiesInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
            return (NetworkCapabilitiesInfo) MessageNano.mergeFrom(new NetworkCapabilitiesInfo(), bArr);
        }

        public static NetworkCapabilitiesInfo parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new NetworkCapabilitiesInfo().mergeFrom(codedInputByteBufferNano);
        }
    }

    public static TelephonyProto$TelephonyEvent[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                try {
                    if (_emptyArray == null) {
                        _emptyArray = new TelephonyProto$TelephonyEvent[0];
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return _emptyArray;
    }

    public TelephonyProto$TelephonyEvent() {
        clear();
    }

    public TelephonyProto$TelephonyEvent clear() {
        this.timestampMillis = 0L;
        this.phoneId = 0;
        this.type = 0;
        this.settings = null;
        this.serviceState = null;
        this.imsConnectionState = null;
        this.imsCapabilities = null;
        this.dataCalls = TelephonyProto$RilDataCall.emptyArray();
        this.error = 0;
        this.setupDataCall = null;
        this.setupDataCallResponse = null;
        this.deactivateDataCall = null;
        this.dataStallAction = 0;
        this.modemRestart = null;
        this.nitzTimestampMillis = 0L;
        this.carrierIdMatching = null;
        this.carrierKeyChange = null;
        this.dataSwitch = null;
        this.networkValidationState = 0;
        this.onDemandDataSwitch = null;
        this.simState = WireFormatNano.EMPTY_INT_ARRAY;
        this.activeSubscriptionInfo = null;
        this.enabledModemBitmap = 0;
        this.updatedEmergencyNumber = null;
        this.networkCapabilities = null;
        this.signalStrength = 0;
        this.emergencyNumberDatabaseVersion = 0;
        this.radioState = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.timestampMillis;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(1, j);
        }
        int i = this.phoneId;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(2, i);
        }
        int i2 = this.type;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i2);
        }
        TelephonyProto$TelephonySettings telephonyProto$TelephonySettings = this.settings;
        if (telephonyProto$TelephonySettings != null) {
            codedOutputByteBufferNano.writeMessage(4, telephonyProto$TelephonySettings);
        }
        TelephonyProto$TelephonyServiceState telephonyProto$TelephonyServiceState = this.serviceState;
        if (telephonyProto$TelephonyServiceState != null) {
            codedOutputByteBufferNano.writeMessage(5, telephonyProto$TelephonyServiceState);
        }
        TelephonyProto$ImsConnectionState telephonyProto$ImsConnectionState = this.imsConnectionState;
        if (telephonyProto$ImsConnectionState != null) {
            codedOutputByteBufferNano.writeMessage(6, telephonyProto$ImsConnectionState);
        }
        TelephonyProto$ImsCapabilities telephonyProto$ImsCapabilities = this.imsCapabilities;
        if (telephonyProto$ImsCapabilities != null) {
            codedOutputByteBufferNano.writeMessage(7, telephonyProto$ImsCapabilities);
        }
        TelephonyProto$RilDataCall[] telephonyProto$RilDataCallArr = this.dataCalls;
        int i3 = 0;
        if (telephonyProto$RilDataCallArr != null && telephonyProto$RilDataCallArr.length > 0) {
            int i4 = 0;
            while (true) {
                TelephonyProto$RilDataCall[] telephonyProto$RilDataCallArr2 = this.dataCalls;
                if (i4 >= telephonyProto$RilDataCallArr2.length) {
                    break;
                }
                TelephonyProto$RilDataCall telephonyProto$RilDataCall = telephonyProto$RilDataCallArr2[i4];
                if (telephonyProto$RilDataCall != null) {
                    codedOutputByteBufferNano.writeMessage(8, telephonyProto$RilDataCall);
                }
                i4++;
            }
        }
        int i5 = this.error;
        if (i5 != 0) {
            codedOutputByteBufferNano.writeInt32(9, i5);
        }
        RilSetupDataCall rilSetupDataCall = this.setupDataCall;
        if (rilSetupDataCall != null) {
            codedOutputByteBufferNano.writeMessage(10, rilSetupDataCall);
        }
        RilSetupDataCallResponse rilSetupDataCallResponse = this.setupDataCallResponse;
        if (rilSetupDataCallResponse != null) {
            codedOutputByteBufferNano.writeMessage(11, rilSetupDataCallResponse);
        }
        RilDeactivateDataCall rilDeactivateDataCall = this.deactivateDataCall;
        if (rilDeactivateDataCall != null) {
            codedOutputByteBufferNano.writeMessage(12, rilDeactivateDataCall);
        }
        int i6 = this.dataStallAction;
        if (i6 != 0) {
            codedOutputByteBufferNano.writeInt32(13, i6);
        }
        ModemRestart modemRestart = this.modemRestart;
        if (modemRestart != null) {
            codedOutputByteBufferNano.writeMessage(14, modemRestart);
        }
        long j2 = this.nitzTimestampMillis;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(15, j2);
        }
        CarrierIdMatching carrierIdMatching = this.carrierIdMatching;
        if (carrierIdMatching != null) {
            codedOutputByteBufferNano.writeMessage(16, carrierIdMatching);
        }
        CarrierKeyChange carrierKeyChange = this.carrierKeyChange;
        if (carrierKeyChange != null) {
            codedOutputByteBufferNano.writeMessage(17, carrierKeyChange);
        }
        DataSwitch dataSwitch = this.dataSwitch;
        if (dataSwitch != null) {
            codedOutputByteBufferNano.writeMessage(19, dataSwitch);
        }
        int i7 = this.networkValidationState;
        if (i7 != 0) {
            codedOutputByteBufferNano.writeInt32(20, i7);
        }
        OnDemandDataSwitch onDemandDataSwitch = this.onDemandDataSwitch;
        if (onDemandDataSwitch != null) {
            codedOutputByteBufferNano.writeMessage(21, onDemandDataSwitch);
        }
        int[] iArr = this.simState;
        if (iArr != null && iArr.length > 0) {
            while (true) {
                int[] iArr2 = this.simState;
                if (i3 >= iArr2.length) {
                    break;
                }
                codedOutputByteBufferNano.writeInt32(22, iArr2[i3]);
                i3++;
            }
        }
        TelephonyProto$ActiveSubscriptionInfo telephonyProto$ActiveSubscriptionInfo = this.activeSubscriptionInfo;
        if (telephonyProto$ActiveSubscriptionInfo != null) {
            codedOutputByteBufferNano.writeMessage(23, telephonyProto$ActiveSubscriptionInfo);
        }
        int i8 = this.enabledModemBitmap;
        if (i8 != 0) {
            codedOutputByteBufferNano.writeInt32(24, i8);
        }
        TelephonyProto$EmergencyNumberInfo telephonyProto$EmergencyNumberInfo = this.updatedEmergencyNumber;
        if (telephonyProto$EmergencyNumberInfo != null) {
            codedOutputByteBufferNano.writeMessage(25, telephonyProto$EmergencyNumberInfo);
        }
        NetworkCapabilitiesInfo networkCapabilitiesInfo = this.networkCapabilities;
        if (networkCapabilitiesInfo != null) {
            codedOutputByteBufferNano.writeMessage(26, networkCapabilitiesInfo);
        }
        int i9 = this.signalStrength;
        if (i9 != 0) {
            codedOutputByteBufferNano.writeInt32(27, i9);
        }
        int i10 = this.emergencyNumberDatabaseVersion;
        if (i10 != 0) {
            codedOutputByteBufferNano.writeInt32(28, i10);
        }
        int i11 = this.radioState;
        if (i11 != 0) {
            codedOutputByteBufferNano.writeInt32(29, i11);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    @Override // com.android.internal.telephony.protobuf.nano.ExtendableMessageNano, com.android.internal.telephony.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int[] iArr;
        int iComputeSerializedSize = super.computeSerializedSize();
        long j = this.timestampMillis;
        if (j != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(1, j);
        }
        int i = this.phoneId;
        if (i != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i);
        }
        int i2 = this.type;
        if (i2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i2);
        }
        TelephonyProto$TelephonySettings telephonyProto$TelephonySettings = this.settings;
        if (telephonyProto$TelephonySettings != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(4, telephonyProto$TelephonySettings);
        }
        TelephonyProto$TelephonyServiceState telephonyProto$TelephonyServiceState = this.serviceState;
        if (telephonyProto$TelephonyServiceState != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(5, telephonyProto$TelephonyServiceState);
        }
        TelephonyProto$ImsConnectionState telephonyProto$ImsConnectionState = this.imsConnectionState;
        if (telephonyProto$ImsConnectionState != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(6, telephonyProto$ImsConnectionState);
        }
        TelephonyProto$ImsCapabilities telephonyProto$ImsCapabilities = this.imsCapabilities;
        if (telephonyProto$ImsCapabilities != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(7, telephonyProto$ImsCapabilities);
        }
        TelephonyProto$RilDataCall[] telephonyProto$RilDataCallArr = this.dataCalls;
        int i3 = 0;
        if (telephonyProto$RilDataCallArr != null && telephonyProto$RilDataCallArr.length > 0) {
            int i4 = 0;
            while (true) {
                TelephonyProto$RilDataCall[] telephonyProto$RilDataCallArr2 = this.dataCalls;
                if (i4 >= telephonyProto$RilDataCallArr2.length) {
                    break;
                }
                TelephonyProto$RilDataCall telephonyProto$RilDataCall = telephonyProto$RilDataCallArr2[i4];
                if (telephonyProto$RilDataCall != null) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(8, telephonyProto$RilDataCall);
                }
                i4++;
            }
        }
        int i5 = this.error;
        if (i5 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, i5);
        }
        RilSetupDataCall rilSetupDataCall = this.setupDataCall;
        if (rilSetupDataCall != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(10, rilSetupDataCall);
        }
        RilSetupDataCallResponse rilSetupDataCallResponse = this.setupDataCallResponse;
        if (rilSetupDataCallResponse != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(11, rilSetupDataCallResponse);
        }
        RilDeactivateDataCall rilDeactivateDataCall = this.deactivateDataCall;
        if (rilDeactivateDataCall != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(12, rilDeactivateDataCall);
        }
        int i6 = this.dataStallAction;
        if (i6 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, i6);
        }
        ModemRestart modemRestart = this.modemRestart;
        if (modemRestart != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(14, modemRestart);
        }
        long j2 = this.nitzTimestampMillis;
        if (j2 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(15, j2);
        }
        CarrierIdMatching carrierIdMatching = this.carrierIdMatching;
        if (carrierIdMatching != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(16, carrierIdMatching);
        }
        CarrierKeyChange carrierKeyChange = this.carrierKeyChange;
        if (carrierKeyChange != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(17, carrierKeyChange);
        }
        DataSwitch dataSwitch = this.dataSwitch;
        if (dataSwitch != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(19, dataSwitch);
        }
        int i7 = this.networkValidationState;
        if (i7 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(20, i7);
        }
        OnDemandDataSwitch onDemandDataSwitch = this.onDemandDataSwitch;
        if (onDemandDataSwitch != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(21, onDemandDataSwitch);
        }
        int[] iArr2 = this.simState;
        if (iArr2 != null && iArr2.length > 0) {
            int iComputeInt32SizeNoTag = 0;
            while (true) {
                iArr = this.simState;
                if (i3 >= iArr.length) {
                    break;
                }
                iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(iArr[i3]);
                i3++;
            }
            iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag + (iArr.length * 2);
        }
        TelephonyProto$ActiveSubscriptionInfo telephonyProto$ActiveSubscriptionInfo = this.activeSubscriptionInfo;
        if (telephonyProto$ActiveSubscriptionInfo != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(23, telephonyProto$ActiveSubscriptionInfo);
        }
        int i8 = this.enabledModemBitmap;
        if (i8 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(24, i8);
        }
        TelephonyProto$EmergencyNumberInfo telephonyProto$EmergencyNumberInfo = this.updatedEmergencyNumber;
        if (telephonyProto$EmergencyNumberInfo != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(25, telephonyProto$EmergencyNumberInfo);
        }
        NetworkCapabilitiesInfo networkCapabilitiesInfo = this.networkCapabilities;
        if (networkCapabilitiesInfo != null) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(26, networkCapabilitiesInfo);
        }
        int i9 = this.signalStrength;
        if (i9 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(27, i9);
        }
        int i10 = this.emergencyNumberDatabaseVersion;
        if (i10 != 0) {
            iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(28, i10);
        }
        int i11 = this.radioState;
        return i11 != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(29, i11) : iComputeSerializedSize;
    }

    @Override // com.android.internal.telephony.protobuf.nano.MessageNano
    public TelephonyProto$TelephonyEvent mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int tag = codedInputByteBufferNano.readTag();
            switch (tag) {
                case 0:
                    break;
                case 8:
                    this.timestampMillis = codedInputByteBufferNano.readInt64();
                    break;
                case 16:
                    this.phoneId = codedInputByteBufferNano.readInt32();
                    break;
                case 24:
                    int int32 = codedInputByteBufferNano.readInt32();
                    switch (int32) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                            this.type = int32;
                            break;
                    }
                    break;
                case 34:
                    if (this.settings == null) {
                        this.settings = new TelephonyProto$TelephonySettings();
                    }
                    codedInputByteBufferNano.readMessage(this.settings);
                    break;
                case 42:
                    if (this.serviceState == null) {
                        this.serviceState = new TelephonyProto$TelephonyServiceState();
                    }
                    codedInputByteBufferNano.readMessage(this.serviceState);
                    break;
                case 50:
                    if (this.imsConnectionState == null) {
                        this.imsConnectionState = new TelephonyProto$ImsConnectionState();
                    }
                    codedInputByteBufferNano.readMessage(this.imsConnectionState);
                    break;
                case 58:
                    if (this.imsCapabilities == null) {
                        this.imsCapabilities = new TelephonyProto$ImsCapabilities();
                    }
                    codedInputByteBufferNano.readMessage(this.imsCapabilities);
                    break;
                case 66:
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 66);
                    TelephonyProto$RilDataCall[] telephonyProto$RilDataCallArr = this.dataCalls;
                    int length = telephonyProto$RilDataCallArr == null ? 0 : telephonyProto$RilDataCallArr.length;
                    int i = repeatedFieldArrayLength + length;
                    TelephonyProto$RilDataCall[] telephonyProto$RilDataCallArr2 = new TelephonyProto$RilDataCall[i];
                    if (length != 0) {
                        System.arraycopy(telephonyProto$RilDataCallArr, 0, telephonyProto$RilDataCallArr2, 0, length);
                    }
                    while (length < i - 1) {
                        TelephonyProto$RilDataCall telephonyProto$RilDataCall = new TelephonyProto$RilDataCall();
                        telephonyProto$RilDataCallArr2[length] = telephonyProto$RilDataCall;
                        codedInputByteBufferNano.readMessage(telephonyProto$RilDataCall);
                        codedInputByteBufferNano.readTag();
                        length++;
                    }
                    TelephonyProto$RilDataCall telephonyProto$RilDataCall2 = new TelephonyProto$RilDataCall();
                    telephonyProto$RilDataCallArr2[length] = telephonyProto$RilDataCall2;
                    codedInputByteBufferNano.readMessage(telephonyProto$RilDataCall2);
                    this.dataCalls = telephonyProto$RilDataCallArr2;
                    break;
                case 72:
                    int int33 = codedInputByteBufferNano.readInt32();
                    switch (int33) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                            this.error = int33;
                            break;
                        default:
                            switch (int33) {
                                case 36:
                                case 37:
                                case 38:
                                case 39:
                                case 40:
                                case 41:
                                case 42:
                                case 43:
                                case 44:
                                case 45:
                                case 46:
                                case 47:
                                case 48:
                                case 49:
                                case 50:
                                case 51:
                                case 52:
                                case 53:
                                case 54:
                                case 55:
                                case 56:
                                case 57:
                                case 58:
                                case 59:
                                case 60:
                                case 61:
                                case 62:
                                case 63:
                                case 64:
                                case 65:
                                case 66:
                                case 67:
                                    break;
                                default:
                                    continue;
                            }
                            this.error = int33;
                            break;
                    }
                    break;
                case 82:
                    if (this.setupDataCall == null) {
                        this.setupDataCall = new RilSetupDataCall();
                    }
                    codedInputByteBufferNano.readMessage(this.setupDataCall);
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_GSM_SET_BROADCAST_CONFIG /* 90 */:
                    if (this.setupDataCallResponse == null) {
                        this.setupDataCallResponse = new RilSetupDataCallResponse();
                    }
                    codedInputByteBufferNano.readMessage(this.setupDataCallResponse);
                    break;
                case 98:
                    if (this.deactivateDataCall == null) {
                        this.deactivateDataCall = new RilDeactivateDataCall();
                    }
                    codedInputByteBufferNano.readMessage(this.deactivateDataCall);
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                    this.dataStallAction = codedInputByteBufferNano.readInt32();
                    break;
                case 114:
                    if (this.modemRestart == null) {
                        this.modemRestart = new ModemRestart();
                    }
                    codedInputByteBufferNano.readMessage(this.modemRestart);
                    break;
                case 120:
                    this.nitzTimestampMillis = codedInputByteBufferNano.readInt64();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY /* 130 */:
                    if (this.carrierIdMatching == null) {
                        this.carrierIdMatching = new CarrierIdMatching();
                    }
                    codedInputByteBufferNano.readMessage(this.carrierIdMatching);
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_DEVICE_STATE /* 138 */:
                    if (this.carrierKeyChange == null) {
                        this.carrierKeyChange = new CarrierKeyChange();
                    }
                    codedInputByteBufferNano.readMessage(this.carrierKeyChange);
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMEI /* 154 */:
                    if (this.dataSwitch == null) {
                        this.dataSwitch = new DataSwitch();
                    }
                    codedInputByteBufferNano.readMessage(this.dataSwitch);
                    break;
                case SmsMessage.MAX_USER_DATA_SEPTETS /* 160 */:
                    int int34 = codedInputByteBufferNano.readInt32();
                    if (int34 == 0 || int34 == 1 || int34 == 2 || int34 == 3) {
                        this.networkValidationState = int34;
                    }
                    break;
                case 170:
                    if (this.onDemandDataSwitch == null) {
                        this.onDemandDataSwitch = new OnDemandDataSwitch();
                    }
                    codedInputByteBufferNano.readMessage(this.onDemandDataSwitch);
                    break;
                case 176:
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 176);
                    int[] iArr = new int[repeatedFieldArrayLength2];
                    int i2 = 0;
                    for (int i3 = 0; i3 < repeatedFieldArrayLength2; i3++) {
                        if (i3 != 0) {
                            codedInputByteBufferNano.readTag();
                        }
                        int int35 = codedInputByteBufferNano.readInt32();
                        if (int35 == 0 || int35 == 1 || int35 == 2) {
                            iArr[i2] = int35;
                            i2++;
                        }
                    }
                    if (i2 != 0) {
                        int[] iArr2 = this.simState;
                        int length2 = iArr2 == null ? 0 : iArr2.length;
                        if (length2 == 0 && i2 == repeatedFieldArrayLength2) {
                            this.simState = iArr;
                        } else {
                            int[] iArr3 = new int[length2 + i2];
                            if (length2 != 0) {
                                System.arraycopy(iArr2, 0, iArr3, 0, length2);
                            }
                            System.arraycopy(iArr, 0, iArr3, length2, i2);
                            this.simState = iArr3;
                        }
                    }
                    break;
                case 178:
                    int iPushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                    int position = codedInputByteBufferNano.getPosition();
                    int i4 = 0;
                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                        int int36 = codedInputByteBufferNano.readInt32();
                        if (int36 == 0 || int36 == 1 || int36 == 2) {
                            i4++;
                        }
                    }
                    if (i4 != 0) {
                        codedInputByteBufferNano.rewindToPosition(position);
                        int[] iArr4 = this.simState;
                        int length3 = iArr4 == null ? 0 : iArr4.length;
                        int[] iArr5 = new int[i4 + length3];
                        if (length3 != 0) {
                            System.arraycopy(iArr4, 0, iArr5, 0, length3);
                        }
                        while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                            int int37 = codedInputByteBufferNano.readInt32();
                            if (int37 == 0 || int37 == 1 || int37 == 2) {
                                iArr5[length3] = int37;
                                length3++;
                            }
                        }
                        this.simState = iArr5;
                    }
                    codedInputByteBufferNano.popLimit(iPushLimit);
                    break;
                case 186:
                    if (this.activeSubscriptionInfo == null) {
                        this.activeSubscriptionInfo = new TelephonyProto$ActiveSubscriptionInfo();
                    }
                    codedInputByteBufferNano.readMessage(this.activeSubscriptionInfo);
                    break;
                case DnsPacket.DnsRecord.NAME_COMPRESSION /* 192 */:
                    this.enabledModemBitmap = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA /* 202 */:
                    if (this.updatedEmergencyNumber == null) {
                        this.updatedEmergencyNumber = new TelephonyProto$EmergencyNumberInfo();
                    }
                    codedInputByteBufferNano.readMessage(this.updatedEmergencyNumber);
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SYSTEM_SELECTION_CHANNELS /* 210 */:
                    if (this.networkCapabilities == null) {
                        this.networkCapabilities = new NetworkCapabilitiesInfo();
                    }
                    codedInputByteBufferNano.readMessage(this.networkCapabilities);
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_RELEASE_PDU_SESSION_ID /* 216 */:
                    this.signalStrength = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SLICING_CONFIG /* 224 */:
                    this.emergencyNumberDatabaseVersion = codedInputByteBufferNano.readInt32();
                    break;
                case NtnRILConstants.MI_NTN_RIL_REQUEST_EXIT_EMERGENCY_MODE /* 232 */:
                    int int38 = codedInputByteBufferNano.readInt32();
                    if (int38 == 0 || int38 == 1 || int38 == 2 || int38 == 3) {
                        this.radioState = int38;
                    }
                    break;
                default:
                    if (storeUnknownField(codedInputByteBufferNano, tag)) {
                    }
                    break;
            }
        }
        return this;
    }

    public static TelephonyProto$TelephonyEvent parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (TelephonyProto$TelephonyEvent) MessageNano.mergeFrom(new TelephonyProto$TelephonyEvent(), bArr);
    }

    public static TelephonyProto$TelephonyEvent parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new TelephonyProto$TelephonyEvent().mergeFrom(codedInputByteBufferNano);
    }
}
