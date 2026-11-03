package com.android.internal.telephony.ntnphone;

import android.net.InetAddresses;
import android.net.LinkAddress;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.text.TextUtils;
import com.android.internal.telephony.DriverCall;
import com.android.internal.telephony.RIL$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.ntndata.TestDataCallResult;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccSlotPortMapping;
import com.android.telephony.Rlog;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import vendor.xiaomi.hardware.satellite.AntInfoSignalStrength;
import vendor.xiaomi.hardware.satellite.AppStatus;
import vendor.xiaomi.hardware.satellite.Call;
import vendor.xiaomi.hardware.satellite.CardStatus;
import vendor.xiaomi.hardware.satellite.DataProfileInfo;
import vendor.xiaomi.hardware.satellite.HLS_ERROR_INFO;
import vendor.xiaomi.hardware.satellite.NtnSignalStrength;
import vendor.xiaomi.hardware.satellite.SatelliteCapability;
import vendor.xiaomi.hardware.satellite.SetupDataCallResult;
import vendor.xiaomi.hardware.satellite.SmsSendResult;
import vendor.xiaomi.hardware.satellite.SmsStatusReport;
import vendor.xiaomi.hardware.satellite.VoiceRegStateResult;

/* JADX INFO: loaded from: classes.dex */
public class NtnRILUtils {
    public static final int CHANNEL_STATE_ACTIVE = 1;
    public static final int CHANNEL_STATE_IDLE = 0;
    public static final String NTN_CDC_MESSAGE_DATA = "byte_array";
    public static final String NTN_CDC_MESSAGE_ID = "message_id";
    public static final int PROTOCOL_IP = 0;
    public static final int PROTOCOL_IPV4V6 = 2;
    public static final int PROTOCOL_IPV6 = 1;
    public static final int PROTOCOL_NON_IP = 4;
    public static final int PROTOCOL_PPP = 3;
    public static final int PROTOCOL_UNKNOWN = -1;
    public static final int PROTOCOL_UNSTRUCTURED = 5;

    public static int convertBooleanEnableToInt(boolean z) {
        return z ? 1 : 0;
    }

    public static String convertNullToEmptyString(String str) {
        return str != null ? str : "";
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

    public static String ntnRequestToString(int i) {
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
                return "GET_ICCID";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SAR_MODE /* 147 */:
                return "GET_SAR_MODE";
            case 148:
                return "SET_SAR_MODE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMSI /* 149 */:
                return "REQUEST_SUPPLY_IMSI";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_SIM_AUTH_RESPONSE /* 150 */:
                return "REQUEST_SUPPLY_SIM_AUTH_RESPONSE";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_UPDATE_SIM_PHONEBOOK_RECORD /* 151 */:
                return "UPDATE_SIM_PHONEBOOK_RECORD";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_DEVICE_IMEI /* 152 */:
                return "DEVICE_IMEI";
            case 153:
                return "REQUEST_SUPPLY_GPS";
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMEI /* 154 */:
                return "REQUEST_SUPPLY_IMEI";
            default:
                switch (i) {
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_STACK_ERROR_EVENT_NOTIFY /* 157 */:
                        return "SET_STACK_ERROR_EVENT_NOTIFY";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_CPID /* 158 */:
                        return "GET_CPID";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RF_MODE /* 159 */:
                        return "SET_RF_MODE";
                    default:
                        switch (i) {
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SAT_BEAM /* 164 */:
                                return "SET_SAT_BEAM";
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_PRIORITY_FREQUENCY_SEARCH /* 165 */:
                                return "SET_PRIORITY_FREQUENCY_SEARCH";
                            case NtnRILConstants.MI_NTN_RIL_QUERY_CURRENT_SATELLITE_CAPABILITY /* 166 */:
                                return "QUERY_CURRENT_SATELLITE_CAPABILITY";
                            case NtnRILConstants.MI_NTN_RIL_SET_SATELLITE_SIM_POWER /* 167 */:
                                return "SET_SATELLITE_SIM_POWER";
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
                                        return "POWER_SWITCH";
                                    case 244:
                                        return "SET_ANTENNA_STATE";
                                    case 245:
                                        return "GET_ANTENNA_STATE";
                                    case 246:
                                        return "SET_ANTENNA_REPORT_MODE";
                                    default:
                                        switch (i) {
                                            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ENHANCED_RADIO_CAPABILITY /* 600 */:
                                                return "RIL_REQUEST_GET_ENHANCED_RADIO_CAPABILITY";
                                            case NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_PA_TEMPERATURE /* 601 */:
                                                return "SEND_PA_TEMPERATURE";
                                            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SCREEN_STATE /* 602 */:
                                                return "SET_SCREEN_STATE";
                                            case NtnRILConstants.MI_NTN_RIL_REQUEST_CUST_FACT_ATCMD /* 603 */:
                                                return "CUST_FACT_ATCMD";
                                            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SPEECH_BITRATE /* 604 */:
                                                return "GET_SPEECH_BITRATE";
                                            default:
                                                return "<unknown request " + i + ">";
                                        }
                                }
                        }
                }
        }
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

    public static String ntnResponseToString(int i) {
        if (i == 1900) {
            return "UNSOL_RESPONSE_CPST_REPORT";
        }
        if (i == 1901) {
            return "UNSOL_RESPONSE_TRACE_REPORT";
        }
        if (i != 1903) {
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
                            return "UNSOL_RESPONSE_IMSI_QUERY";
                        case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SIM_AUTHENTICATION /* 1054 */:
                            return "UNSOL_RESPONSE_SIM_AUTHENTICATION";
                        case NtnRILConstants.MI_NTN_RIL_UNSOL_SLICING_CONFIG_CHANGED /* 1055 */:
                            return "UNSOL_SLICING_CONFIG_CHANGED";
                        case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_SNR_REPORT /* 1056 */:
                            return "UNSOL_RESPONSE_SNR_REPORT";
                        case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_TXPWR_REPORT /* 1057 */:
                            return "UNSOL_RESPONSE_TXPWR_REPORT";
                        case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_GPS_QUERY /* 1058 */:
                            return "UNSOL_RESPONSE_GPS_QUERY";
                        case NtnRILConstants.MI_NTN_RIL_UNSOL_RESPONSE_IMEI_QUERY /* 1059 */:
                            return "UNSOL_RESPONSE_IMEI_QUERY";
                        default:
                            switch (i) {
                                case NtnRILConstants.MI_NTN_RIL_UNSOL_MIN_GPS_DISTANCE /* 1062 */:
                                    return "UNSOL_RESPONSE_MIN_GPS_DISTANCE";
                                case NtnRILConstants.MI_NTN_RIL_UNSOL_VOICE_CONTROL_OPEN /* 1063 */:
                                    return "UNSOL_VOICE_CONTROL_OPEN";
                                case NtnRILConstants.MI_NTN_RIL_UNSOL_TTLOG_INFO /* 1064 */:
                                    return "UNSOL_TTLOG_INFO";
                                case NtnRILConstants.MI_NTN_RIL_UNSOL_POS_INFO /* 1065 */:
                                    return "UNSOL_NTN_POS_INFO";
                                case NtnRILConstants.MI_NTN_RIL_UNSOL_CHANNEL_STATE_INFO /* 1066 */:
                                    return "MI_NTN_RIL_UNSOL_CHANNEL_STATE_INFO";
                                default:
                                    switch (i) {
                                        case NtnRILConstants.MI_NTN_RIL_UNSOL_HIGH_PENETRATION_STATE_CHANGED /* 1078 */:
                                            return "UNSOL_HIGH_PENETRATION_STATE_CHANGED";
                                        case NtnRILConstants.MI_NTN_RIL_UNSOL_ANT_INFO_SIGNAL_STRENGTH /* 1079 */:
                                            return "UNSOL_ANT_INFO_SIGNAL_STRENGTH";
                                        case NtnRILConstants.MI_NTN_RIL_UNSOL_OTA_STATE_CHANGED /* 1080 */:
                                            return "UNSOL_OTA_STATE_CHANGED";
                                        case NtnRILConstants.MI_NTN_RIL_UNSOL_CDC_MESSAGE /* 1081 */:
                                            return "UNSOL_CDC_MESSAGE";
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
                                                default:
                                                    return "<unknown response " + i + ">";
                                            }
                                    }
                            }
                    }
            }
        }
        return "UNSOL_RESPONSE_NEW_SMS_SEND_RESULT";
    }

    public static CellSignalStrengthGsm convertHalGsmSignalStrength(NtnSignalStrength ntnSignalStrength) {
        CellSignalStrengthGsm cellSignalStrengthGsm = new CellSignalStrengthGsm(ntnSignalStrength.signalStrength, ntnSignalStrength.bitErrorRate, ntnSignalStrength.timingAdvance, true);
        if (cellSignalStrengthGsm.getRssi() == Integer.MAX_VALUE) {
            cellSignalStrengthGsm.setDefaultValues();
            cellSignalStrengthGsm.updateLevel(null, null);
        }
        return cellSignalStrengthGsm;
    }

    public static SignalStrength convertHalSignalStrength(Object obj) {
        if (obj != null && (obj instanceof vendor.xiaomi.hardware.satellite.SignalStrength)) {
            return new SignalStrength(new CellSignalStrengthCdma(), convertHalGsmSignalStrength(((vendor.xiaomi.hardware.satellite.SignalStrength) obj).ntn), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(), new CellSignalStrengthNr());
        }
        return null;
    }

    public static NtnDriverCall convertToNtnDriverCall(Call call) {
        NtnDriverCall ntnDriverCall = new NtnDriverCall();
        if (call != null) {
            ntnDriverCall.state = NtnDriverCall.stateFromCLCCForNtn(call.state);
            ntnDriverCall.index = call.index;
            ntnDriverCall.TOA = call.toa;
            ntnDriverCall.isMpty = call.isMpty;
            ntnDriverCall.isMT = call.isMT;
            ntnDriverCall.als = call.als;
            ntnDriverCall.isVoice = call.isVoice;
            ntnDriverCall.isVoicePrivacy = call.isVoicePrivacy;
            ntnDriverCall.number = call.number;
            ntnDriverCall.numberPresentation = DriverCall.presentationFromCLIP(call.numberPresentation);
            ntnDriverCall.name = call.name;
            ntnDriverCall.namePresentation = DriverCall.presentationFromCLIP(call.namePresentation);
            ntnDriverCall.number = PhoneNumberUtils.stringFromStringAndTOA(ntnDriverCall.number, ntnDriverCall.TOA);
        }
        return ntnDriverCall;
    }

    public static NtnSmsStatusReport convertToNtnSmsStatusReport(SmsStatusReport smsStatusReport) {
        NtnSmsStatusReport ntnSmsStatusReport = new NtnSmsStatusReport();
        if (smsStatusReport != null) {
            ntnSmsStatusReport.messageRef = smsStatusReport.messageRef;
            ntnSmsStatusReport.status = smsStatusReport.status;
            ntnSmsStatusReport.tpStatus = smsStatusReport.tpStatus;
        }
        return ntnSmsStatusReport;
    }

    public static NtnSmsSendResult convertToNtnSmsSendResult(SmsSendResult smsSendResult) {
        NtnSmsSendResult ntnSmsSendResult = new NtnSmsSendResult();
        if (smsSendResult != null) {
            ntnSmsSendResult.messageRef = smsSendResult.messageRef;
            ntnSmsSendResult.status = smsSendResult.status;
            ntnSmsSendResult.error = smsSendResult.error;
        }
        return ntnSmsSendResult;
    }

    public static NtnTtlogInfo convertToNtnTtlogInfo(vendor.xiaomi.hardware.satellite.NtnTtlogInfo ntnTtlogInfo) {
        NtnTtlogInfo ntnTtlogInfo2 = new NtnTtlogInfo();
        if (ntnTtlogInfo != null) {
            ntnTtlogInfo2.sat_id = ntnTtlogInfo.sat_id;
            ntnTtlogInfo2.rTotal = ntnTtlogInfo.rTotal;
            ntnTtlogInfo2.rError = ntnTtlogInfo.rError;
            ntnTtlogInfo2.foffset = ntnTtlogInfo.foffset;
            ntnTtlogInfo2.tx_valid = ntnTtlogInfo.tx_valid;
            ntnTtlogInfo2.txp_setted = ntnTtlogInfo.txp_setted;
            ntnTtlogInfo2.txp_real = ntnTtlogInfo.txp_real;
            ntnTtlogInfo2.tempeture = ntnTtlogInfo.tempeture;
        }
        return ntnTtlogInfo2;
    }

    public static HlsErrorInfo convertToHlsErrorInfo(HLS_ERROR_INFO hls_error_info) {
        HlsErrorInfo hlsErrorInfo = new HlsErrorInfo();
        if (hls_error_info != null) {
            hlsErrorInfo.class_info = hls_error_info.class_info;
            hlsErrorInfo.error_cause = hls_error_info.error_cause;
        }
        return hlsErrorInfo;
    }

    public static NtnPosInfo convertToNtnPosInfo(vendor.xiaomi.hardware.satellite.NtnPosInfo ntnPosInfo) {
        NtnPosInfo ntnPosInfo2 = new NtnPosInfo();
        if (ntnPosInfo != null) {
            ntnPosInfo2.long_beam = ntnPosInfo.long_beam;
            ntnPosInfo2.lat_beam = ntnPosInfo.lat_beam;
            ntnPosInfo2.beam_id = ntnPosInfo.beam_id;
        }
        return ntnPosInfo2;
    }

    public static SatelliteConfig convertToNtnSatelliteCap(SatelliteCapability satelliteCapability) {
        SatelliteConfig satelliteConfig = new SatelliteConfig();
        if (satelliteCapability != null) {
            satelliteConfig.satelliteType = SatelliteConfig.typeFromConfig(satelliteCapability.satelliteType);
            satelliteConfig.chipType = SatelliteConfig.chipFromConfig(satelliteCapability.chipType);
            satelliteConfig.capability = satelliteCapability.capability;
        }
        return satelliteConfig;
    }

    public static String buildVoiceRegStateResultforNTN(VoiceRegStateResult voiceRegStateResult) {
        return "{.regState = " + voiceRegStateResult.regState + ", .rat = " + voiceRegStateResult.rat + ", .cssSupported = " + voiceRegStateResult.cssSupported + ", .roamingIndicator = " + voiceRegStateResult.roamingIndicator + ", .systemIsInPrl = " + voiceRegStateResult.systemIsInPrl + ", .defaultRoamingIndicator = " + voiceRegStateResult.defaultRoamingIndicator + ", .reasonForDenial = " + voiceRegStateResult.reasonForDenial + ", .cellIdentity = {...}}";
    }

    public static IccCardStatus convertHalCardStatus(CardStatus cardStatus) {
        IccCardStatus iccCardStatus = new IccCardStatus();
        iccCardStatus.setCardState(cardStatus.cardState);
        iccCardStatus.setUniversalPinState(cardStatus.universalPinState);
        iccCardStatus.mGsmUmtsSubscriptionAppIndex = cardStatus.gsmUmtsSubscriptionAppIndex;
        iccCardStatus.mCdmaSubscriptionAppIndex = cardStatus.cdmaSubscriptionAppIndex;
        iccCardStatus.mImsSubscriptionAppIndex = cardStatus.imsSubscriptionAppIndex;
        iccCardStatus.atr = convertNullToEmptyString(cardStatus.atr);
        iccCardStatus.iccid = convertNullToEmptyString(cardStatus.iccid);
        int iMin = Math.min(cardStatus.applications.length, 8);
        iccCardStatus.mApplications = new IccCardApplicationStatus[iMin];
        for (int i = 0; i < iMin; i++) {
            AppStatus appStatus = cardStatus.applications[i];
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
        iccCardStatus.mSlotPortMapping = new IccSlotPortMapping();
        return iccCardStatus;
    }

    public static DataProfileInfo convertToHalDataProfile(DataProfile dataProfile) {
        DataProfileInfo dataProfileInfo = new DataProfileInfo();
        dataProfileInfo.profileId = dataProfile.getProfileId();
        dataProfileInfo.apn = dataProfile.getApn();
        dataProfileInfo.protocol = convertIntProtocolToString(dataProfile.getProtocolType());
        dataProfileInfo.roamingProtocol = convertIntProtocolToString(dataProfile.getRoamingProtocolType());
        dataProfileInfo.authType = dataProfile.getAuthType();
        dataProfileInfo.user = convertNullToEmptyString(dataProfile.getUserName());
        dataProfileInfo.password = convertNullToEmptyString(dataProfile.getPassword());
        dataProfileInfo.type = dataProfile.getType();
        dataProfileInfo.maxConnsTime = dataProfile.getMaxConnectionsTime();
        dataProfileInfo.maxConns = dataProfile.getMaxConnections();
        dataProfileInfo.waitTime = dataProfile.getWaitTime();
        dataProfileInfo.enabled = convertBooleanEnableToInt(dataProfile.isEnabled());
        dataProfileInfo.bearerBitmask = ServiceState.convertNetworkTypeBitmaskToBearerBitmask(dataProfile.getBearerBitmask()) << 1;
        dataProfileInfo.mtu = dataProfile.getMtuV4();
        dataProfileInfo.mvnoType = 0;
        dataProfileInfo.mvnoMatchData = "";
        return dataProfileInfo;
    }

    public static String convertIntProtocolToString(int i) {
        switch (i) {
            case -1:
                return SatelliteConstants.DEFAULT_PLMN;
            case 0:
                return "IP";
            case 1:
                return "IPV6";
            case 2:
                return "IPV4V6";
            case 3:
                return "PPP";
            case 4:
                return "NON_IP";
            case 5:
                return "UNSTRUCTURED";
            default:
                return "PROTOCOL_IPV4V6";
        }
    }

    public static DataCallResponse convertHalDataCallResult(Object obj) {
        int i;
        if (obj == null) {
            return null;
        }
        SetupDataCallResult setupDataCallResult = (SetupDataCallResult) obj;
        int i2 = setupDataCallResult.status;
        long j = setupDataCallResult.suggestedRetryTime;
        int i3 = setupDataCallResult.cid;
        int i4 = setupDataCallResult.active;
        int protocolIntFromString = ApnSetting.getProtocolIntFromString(setupDataCallResult.type);
        String str = setupDataCallResult.ifname;
        int i5 = setupDataCallResult.mtu;
        Rlog.e("NtnRILUtils", "cause = " + i2 + ", suggestedRetryTime = " + j + ", cid = " + i3 + ", active = " + i4 + ", protocolType = " + protocolIntFromString + ", ifname = " + str + ", mtu = " + i5);
        ArrayList arrayList = new ArrayList();
        String str2 = setupDataCallResult.addresses;
        if (str2 != null) {
            String strTrim = str2.trim();
            try {
                Rlog.e("NtnRILUtils", "addresses = " + strTrim);
                arrayList.add(convertToLinkAddress(setupDataCallResult.addresses));
            } catch (IllegalArgumentException e) {
                Rlog.e("NtnRILUtils", "Unknown addresses: " + strTrim, e);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        String str3 = setupDataCallResult.dnses;
        if (str3 != null) {
            String[] strArrSplit = str3.trim().split(" ");
            int length = strArrSplit.length;
            int i6 = 0;
            while (i6 < length) {
                String[] strArr = strArrSplit;
                String strTrim2 = strArrSplit[i6].trim();
                try {
                    StringBuilder sb = new StringBuilder();
                    i = length;
                    try {
                        sb.append("dns = ");
                        sb.append(strTrim2);
                        Rlog.e("NtnRILUtils", sb.toString());
                        if (!TextUtils.isEmpty(strTrim2)) {
                            arrayList2.add(InetAddresses.parseNumericAddress(strTrim2));
                        }
                    } catch (IllegalArgumentException e2) {
                        e = e2;
                        Rlog.e("NtnRILUtils", "Unknown dns: " + strTrim2, e);
                    }
                } catch (IllegalArgumentException e3) {
                    e = e3;
                    i = length;
                }
                i6++;
                strArrSplit = strArr;
                length = i;
            }
        }
        ArrayList arrayList3 = new ArrayList();
        if (setupDataCallResult.gateways != null) {
            Rlog.e("NtnRILUtils", "gateways = " + setupDataCallResult.gateways);
            String strTrim3 = setupDataCallResult.gateways.trim();
            try {
                arrayList3.add(InetAddresses.parseNumericAddress(strTrim3));
            } catch (IllegalArgumentException e4) {
                Rlog.e("NtnRILUtils", "Unknown gateway: " + strTrim3, e4);
            }
        }
        ArrayList arrayList4 = new ArrayList();
        if (setupDataCallResult.pcscf != null) {
            Rlog.e("NtnRILUtils", "pcscf = " + setupDataCallResult.pcscf);
            String strTrim4 = setupDataCallResult.pcscf.trim();
            try {
                arrayList4.add(InetAddresses.parseNumericAddress(strTrim4));
            } catch (IllegalArgumentException e5) {
                Rlog.e("NtnRILUtils", "Unknown pcscf: " + strTrim4, e5);
            }
        }
        return new DataCallResponse.Builder().setCause(i2).setRetryDurationMillis(j).setId(i3).setLinkStatus(i4).setProtocolType(protocolIntFromString).setInterfaceName(str).setAddresses(arrayList).setDnsAddresses(arrayList2).setGatewayAddresses(arrayList3).setPcscfAddresses(arrayList4).setMtu(i5).setMtuV4(i5).setMtuV6(i5).build();
    }

    public static DataCallResponse convertHalTestDataCallResult(Object obj) {
        if (obj == null) {
            return null;
        }
        TestDataCallResult testDataCallResult = (TestDataCallResult) obj;
        int i = testDataCallResult.status;
        long j = testDataCallResult.suggestedRetryTime;
        int i2 = testDataCallResult.cid;
        int i3 = testDataCallResult.active;
        int protocolIntFromString = ApnSetting.getProtocolIntFromString(testDataCallResult.type);
        String str = testDataCallResult.ifname;
        int i4 = testDataCallResult.mtu;
        ArrayList arrayList = new ArrayList();
        String str2 = testDataCallResult.addresses;
        if (str2 != null) {
            arrayList.add(convertToLinkAddress(str2));
        }
        ArrayList arrayList2 = new ArrayList();
        String str3 = testDataCallResult.dnses;
        if (str3 != null) {
            String strTrim = str3.trim();
            try {
                arrayList2.add(InetAddresses.parseNumericAddress(strTrim));
            } catch (IllegalArgumentException e) {
                Rlog.e("NtnRILUtils", "Unknown dns: " + strTrim, e);
            }
        }
        ArrayList arrayList3 = new ArrayList();
        String str4 = testDataCallResult.gateways;
        if (str4 != null) {
            String strTrim2 = str4.trim();
            try {
                arrayList3.add(InetAddresses.parseNumericAddress(strTrim2));
            } catch (IllegalArgumentException e2) {
                Rlog.e("NtnRILUtils", "Unknown gateway: " + strTrim2, e2);
            }
        }
        ArrayList arrayList4 = new ArrayList();
        String str5 = testDataCallResult.pcscf;
        if (str5 != null) {
            String strTrim3 = str5.trim();
            try {
                arrayList4.add(InetAddresses.parseNumericAddress(strTrim3));
            } catch (IllegalArgumentException e3) {
                Rlog.e("NtnRILUtils", "Unknown pcscf: " + strTrim3, e3);
            }
        }
        return new DataCallResponse.Builder().setCause(i).setRetryDurationMillis(j).setId(i2).setLinkStatus(i3).setProtocolType(protocolIntFromString).setInterfaceName(str).setAddresses(arrayList).setDnsAddresses(arrayList2).setGatewayAddresses(arrayList3).setPcscfAddresses(arrayList4).setMtu(i4).setMtuV4(i4).setMtuV6(i4).build();
    }

    private static LinkAddress convertToLinkAddress(String str) {
        return convertToLinkAddress(str, 0, -1L, -1L);
    }

    private static LinkAddress convertToLinkAddress(String str, int i, long j, long j2) {
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
        return new LinkAddress(inetAddress, i2, i, 0, j, j2);
    }

    public static int convertToHalTxAntEnableType(NtnAntConstants.TxAntEnableType txAntEnableType) {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$TxAntEnableType[txAntEnableType.ordinal()];
        if (i != 1) {
            return i != 2 ? -1 : 1;
        }
        return 0;
    }

    public static int convertToHalRxAntEnableType(NtnAntConstants.RxAntEnableType rxAntEnableType) {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntEnableType[rxAntEnableType.ordinal()];
        if (i == 1) {
            return 0;
        }
        if (i != 2) {
            return i != 3 ? -1 : 2;
        }
        return 1;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.ntnphone.NtnRILUtils$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntEnableType;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntReportMode;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$TxAntEnableType;

        static {
            int[] iArr = new int[NtnAntConstants.RxAntReportMode.values().length];
            $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntReportMode = iArr;
            try {
                iArr[NtnAntConstants.RxAntReportMode.RX_ANT1.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntReportMode[NtnAntConstants.RxAntReportMode.RX_ANT2.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntReportMode[NtnAntConstants.RxAntReportMode.RX_ANT_COMBINED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[NtnAntConstants.RxAntEnableType.values().length];
            $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntEnableType = iArr2;
            try {
                iArr2[NtnAntConstants.RxAntEnableType.RX_ANT1.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntEnableType[NtnAntConstants.RxAntEnableType.RX_ANT2.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntEnableType[NtnAntConstants.RxAntEnableType.RX_ANT_COMBINED.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr3 = new int[NtnAntConstants.TxAntEnableType.values().length];
            $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$TxAntEnableType = iArr3;
            try {
                iArr3[NtnAntConstants.TxAntEnableType.TX_ANT1.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$TxAntEnableType[NtnAntConstants.TxAntEnableType.TX_ANT2.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    public static int convertToHalRxAntReportMode(NtnAntConstants.RxAntReportMode rxAntReportMode) {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$ntnphone$NtnAntConstants$RxAntReportMode[rxAntReportMode.ordinal()];
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

    public static NtnAntConstants.TxAntEnableType convertHalTxAntEnableType(int i) {
        if (i == 0) {
            return NtnAntConstants.TxAntEnableType.TX_ANT1;
        }
        if (i == 1) {
            return NtnAntConstants.TxAntEnableType.TX_ANT2;
        }
        return NtnAntConstants.TxAntEnableType.TX_UNSET;
    }

    public static NtnAntConstants.RxAntEnableType convertHalRxAntEnableType(int i) {
        if (i == 0) {
            return NtnAntConstants.RxAntEnableType.RX_ANT1;
        }
        if (i == 1) {
            return NtnAntConstants.RxAntEnableType.RX_ANT2;
        }
        if (i == 2) {
            return NtnAntConstants.RxAntEnableType.RX_ANT_COMBINED;
        }
        return NtnAntConstants.RxAntEnableType.RX_UNSET;
    }

    public static NtnAntConstants.RxAntReportMode convertHalRxAntReportMode(int i) {
        if (i == 1) {
            return NtnAntConstants.RxAntReportMode.RX_ANT1;
        }
        if (i == 2) {
            return NtnAntConstants.RxAntReportMode.RX_ANT2;
        }
        if (i == 3) {
            return NtnAntConstants.RxAntReportMode.RX_ANT_COMBINED;
        }
        return NtnAntConstants.RxAntReportMode.RX_CLOSE;
    }

    public static ArrayList<AntIdentitySignalStrength> convertHalAntInfoSignalStrength(Object obj) {
        ArrayList<AntIdentitySignalStrength> arrayList = new ArrayList<>();
        if (obj != null && (obj instanceof AntInfoSignalStrength)) {
            vendor.xiaomi.hardware.satellite.AntIdentitySignalStrength[] antIdentitySignalStrengthArr = ((AntInfoSignalStrength) obj).antInfo;
            for (int i = 0; i < antIdentitySignalStrengthArr.length; i++) {
                vendor.xiaomi.hardware.satellite.AntIdentitySignalStrength antIdentitySignalStrength = antIdentitySignalStrengthArr[i];
                arrayList.add(new AntIdentitySignalStrength(i, antIdentitySignalStrength.rssi, antIdentitySignalStrength.snr / 10.0f));
            }
        }
        return arrayList;
    }
}
