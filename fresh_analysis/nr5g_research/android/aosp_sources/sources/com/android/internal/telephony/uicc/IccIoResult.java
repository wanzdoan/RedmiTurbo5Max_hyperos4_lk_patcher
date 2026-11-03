package com.android.internal.telephony.uicc;

import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.util.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class IccIoResult {
    public byte[] payload;
    public int sw1;
    public int sw2;

    private String getErrorString() {
        int i = this.sw1;
        if (i == 152) {
            int i2 = this.sw2;
            if (i2 == 2) {
                return "no CHV initialized";
            }
            if (i2 == 4) {
                return "access condition not fulfilled/unsuccessful CHV verification, at least one attempt left/unsuccessful UNBLOCK CHV verification, at least one attempt left/authentication failed";
            }
            if (i2 == 8) {
                return "in contradiction with CHV status";
            }
            if (i2 == 16) {
                return "in contradiction with invalidation status";
            }
            if (i2 == 64) {
                return "unsuccessful CHV verification, no attempt left/unsuccessful UNBLOCK CHV verification, no attempt left/CHV blocked/UNBLOCK CHV blocked";
            }
            if (i2 == 80) {
                return "increase cannot be performed, Max value reached";
            }
            if (i2 == 98) {
                return "authentication error, application specific";
            }
            switch (i2) {
                case 100:
                    return "authentication error, security context not supported";
                case 101:
                    return "key freshness failure";
                case 102:
                    return "authentication error, no memory space available";
                case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                    return "authentication error, no memory space available in EF_MUK";
                default:
                    return "unknown";
            }
        }
        if (i == 158 || i == 159) {
            return null;
        }
        switch (i) {
            case 97:
                return this.sw2 + " more response bytes available";
            case 98:
                int i3 = this.sw2;
                if (i3 == 0) {
                    return "no information given, state of non volatile memory unchanged";
                }
                switch (i3) {
                    case 129:
                        return "part of returned data may be corrupted";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY /* 130 */:
                        return "end of file/record reached before reading Le bytes";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY /* 131 */:
                        return "selected file invalidated";
                    case 132:
                        return "selected file in termination state";
                    default:
                        switch (i3) {
                            case 241:
                                return "more data available";
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_IS_N1_MODE_ENABLED /* 242 */:
                                return "more data available and proactive command pending";
                            case 243:
                                return "response data available";
                            default:
                                return "unknown";
                        }
                }
            case 99:
                int i4 = this.sw2;
                if ((i4 >> 4) != 12) {
                    if (i4 == 241) {
                        return "more data expected";
                    }
                    if (i4 == 242) {
                        return "more data expected and proactive command pending";
                    }
                    return "unknown";
                }
                int i5 = i4 & 15;
                return "command successful but after using an internal update retry routine " + i5 + " times, or verification failed, " + i5 + " retries remaining";
            case 100:
                if (this.sw2 == 0) {
                    return "no information given, state of non-volatile memory unchanged";
                }
                return "unknown";
            case 101:
                int i6 = this.sw2;
                if (i6 != 0) {
                    return i6 != 129 ? "unknown" : "memory problem";
                }
                return "no information given, state of non-volatile memory changed";
            default:
                switch (i) {
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                        return this.sw2 != 0 ? "the interpretation of this status word is command dependent" : "incorrect parameter P3";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                        int i7 = this.sw2;
                        if (i7 == 0) {
                            return "no information given";
                        }
                        if (i7 == 129) {
                            return "logical channel not supported";
                        }
                        if (i7 == 130) {
                            return "secure messaging not supported";
                        }
                        return "unknown";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION /* 105 */:
                        int i8 = this.sw2;
                        if (i8 == 0) {
                            return "no information given";
                        }
                        if (i8 != 137) {
                            switch (i8) {
                                case 129:
                                    return "command incompatible with file structure";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY /* 130 */:
                                    return "security status not satisfied";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY /* 131 */:
                                    return "authentication/PIN method blocked";
                                case 132:
                                    return "referenced data invalidated";
                                case 133:
                                    return "conditions of use not satisfied";
                                case 134:
                                    return "command not allowed (no EF selected)";
                                default:
                                    return "unknown";
                            }
                        }
                        return "command not allowed - secure channel - security not satisfied";
                    case 106:
                        switch (this.sw2) {
                            case 128:
                                return "incorrect parameters in the data field";
                            case 129:
                                return "function not supported";
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY /* 130 */:
                                return "file not found";
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY /* 131 */:
                                return "record not found";
                            case 132:
                                return "not enough memory space";
                            case 133:
                            default:
                                return "unknown";
                            case 134:
                                return "incorrect parameters P1 to P2";
                            case 135:
                                return "lc inconsistent with P1 to P2";
                            case 136:
                                return "referenced data not found";
                        }
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS /* 107 */:
                        return "incorrect parameter P1 or P2";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH /* 108 */:
                        return "wrong length, retry with " + this.sw2;
                    case 109:
                        return "unknown instruction code given in the command";
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE /* 110 */:
                        return "wrong instruction class given in the command";
                    case 111:
                        return this.sw2 != 0 ? "the interpretation of this status word is command dependent" : "technical problem with no diagnostic given";
                    default:
                        switch (i) {
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE /* 144 */:
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_KEEPALIVE /* 145 */:
                                return null;
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ICCID /* 146 */:
                                int i9 = this.sw2;
                                if ((i9 >> 4) == 0) {
                                    return "command successful but after using an internal update retry routine";
                                }
                                return i9 != 64 ? "unknown" : "memory problem";
                            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SAR_MODE /* 147 */:
                                if (this.sw2 == 0) {
                                    return "SIM Application Toolkit is busy. Command cannot be executed at present, further normal commands are allowed";
                                }
                                return "unknown";
                            case 148:
                                int i10 = this.sw2;
                                if (i10 == 0) {
                                    return "no EF selected";
                                }
                                if (i10 == 2) {
                                    return "out f range (invalid address)";
                                }
                                if (i10 == 4) {
                                    return "file ID not found/pattern not found";
                                }
                                if (i10 == 8) {
                                    return "file is inconsistent with the command";
                                }
                                return "unknown";
                            default:
                                return "unknown";
                        }
                }
        }
    }

    public IccIoResult(int i, int i2, byte[] bArr) {
        this.sw1 = i;
        this.sw2 = i2;
        this.payload = bArr;
    }

    public IccIoResult(int i, int i2, String str) {
        this(i, i2, IccUtils.hexStringToBytes(str));
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder("IccIoResult sw1:0x");
        sb.append(Integer.toHexString(this.sw1));
        sb.append(" sw2:0x");
        sb.append(Integer.toHexString(this.sw2));
        sb.append(" Payload: ");
        sb.append(TelephonyUtils.IS_DEBUGGABLE ? IccUtils.bytesToHexString(this.payload) : "*******");
        if (success()) {
            str = "";
        } else {
            str = " Error: " + getErrorString();
        }
        sb.append(str);
        return sb.toString();
    }

    public boolean success() {
        int i = this.sw1;
        return i == 144 || i == 145 || i == 158 || i == 159;
    }

    public IccException getException() {
        if (success()) {
            return null;
        }
        if (this.sw1 == 148) {
            if (this.sw2 == 8) {
                return new IccFileTypeMismatch();
            }
            return new IccFileNotFound();
        }
        return new IccException("sw1:" + this.sw1 + " sw2:" + this.sw2);
    }
}
