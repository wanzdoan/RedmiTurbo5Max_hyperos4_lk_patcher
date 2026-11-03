package com.android.internal.telephony.utils;

import android.hardware.radio.V1_0.DataRegStateResult;
import android.hardware.radio.V1_0.RegState;
import android.hardware.radio.V1_0.VoiceRegStateResult;
import android.hardware.radio.V1_5.RegStateResult;
import android.hardware.radio.data.SetupDataCallResult;
import android.telephony.data.DataCallResponse;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class MiuiRILUtils {
    private static final String LOG_TAG = "MiuiRILUtils";

    private MiuiRILUtils() {
    }

    public static void processForCustSingleSimDevice(IccCardStatus status, Integer index) {
        if (status != null && index != null && TelephonyManager.isCustSingleSimDevice() && index.intValue() == 1 && status.mCardState != IccCardStatus.CardState.CARDSTATE_ABSENT) {
            status.setCardState(IccCardStatus.CardState.CARDSTATE_ABSENT.ordinal());
            status.setUniversalPinState(IccCardStatus.PinState.PINSTATE_UNKNOWN.ordinal());
            status.mGsmUmtsSubscriptionAppIndex = -1;
            status.mCdmaSubscriptionAppIndex = -1;
            status.mImsSubscriptionAppIndex = -1;
            status.mApplications = new IccCardApplicationStatus[0];
        }
    }

    public static String[] handlePii(int request, String[] strings) {
        if (PhoneDebug.VDBG || strings == null) {
            return strings;
        }
        if (request == 20 || request == 21) {
            if (strings.length >= 3) {
                String[] clone = new String[strings.length];
                System.arraycopy(strings, 0, clone, 0, strings.length);
                clone[1] = hidePartialChars(clone[1]);
                clone[2] = hidePartialChars(clone[2]);
                if (request == 20) {
                    if (strings.length >= 5) {
                        clone[4] = hidePartialChars(clone[4]);
                    }
                    if (strings.length >= 6) {
                        clone[5] = hidePartialChars(clone[5]);
                    }
                    if (strings.length >= 7) {
                        clone[6] = hidePartialChars(clone[6]);
                    }
                    if (strings.length >= 9) {
                        clone[8] = hidePartialChars(clone[8]);
                    }
                    if (strings.length >= 10) {
                        clone[9] = hidePartialChars(clone[9]);
                    }
                    if (strings.length >= 15) {
                        clone[14] = hidePartialChars(clone[14]);
                    }
                } else if (request == 21) {
                    if (strings.length >= 7) {
                        clone[6] = hidePartialChars(clone[6]);
                    }
                    if (strings.length >= 8) {
                        clone[7] = hidePartialChars(clone[7]);
                    }
                    if (strings.length >= 9) {
                        clone[8] = hidePartialChars(clone[8]);
                    }
                    if (strings.length >= 10) {
                        clone[9] = hidePartialChars(clone[9]);
                    }
                    if (strings.length >= 11) {
                        clone[10] = hidePartialChars(clone[10]);
                    }
                }
                return clone;
            }
        } else if (request == 95) {
            String[] clone2 = new String[strings.length];
            System.arraycopy(strings, 0, clone2, 0, strings.length);
            if (strings.length >= 1) {
                clone2[0] = TelephonyUtils.pii(clone2[0]);
            }
            if (strings.length >= 2) {
                clone2[1] = "*";
            }
            if (strings.length >= 3) {
                clone2[2] = "*";
            }
            if (strings.length >= 4) {
                clone2[3] = TelephonyUtils.pii(clone2[3]);
            }
            return clone2;
        }
        return strings;
    }

    public static String handlePii(int request, Object result) {
        ArrayList<DataCallResponse> response;
        if (result == null) {
            return "";
        }
        if (PhoneDebug.VDBG) {
            return result.toString();
        }
        if (request == 20) {
            if (result instanceof VoiceRegStateResult) {
                return buildVoiceRegStateResultforV1_0((VoiceRegStateResult) result);
            }
            if (result instanceof android.hardware.radio.V1_2.VoiceRegStateResult) {
                return buildVoiceRegStateResultforV1_2((android.hardware.radio.V1_2.VoiceRegStateResult) result);
            }
            if (result instanceof RegStateResult) {
                return buildRegStateResultforV1_5((RegStateResult) result);
            }
            if (result instanceof android.hardware.radio.V1_6.RegStateResult) {
                return buildRegStateResultforV1_6((android.hardware.radio.V1_6.RegStateResult) result);
            }
            if (result instanceof android.hardware.radio.network.RegStateResult) {
                return buildRegStateResultforT((android.hardware.radio.network.RegStateResult) result);
            }
        } else if (request == 21) {
            if (result instanceof DataRegStateResult) {
                return buildDataRegStateResultforV1_0((DataRegStateResult) result);
            }
            if (result instanceof android.hardware.radio.V1_2.DataRegStateResult) {
                return buildDataRegStateResultforV1_2((android.hardware.radio.V1_2.DataRegStateResult) result);
            }
            if (result instanceof android.hardware.radio.V1_4.DataRegStateResult) {
                return buildDataRegStateResultforV1_4((android.hardware.radio.V1_4.DataRegStateResult) result);
            }
            if (result instanceof RegStateResult) {
                return buildRegStateResultforV1_5((RegStateResult) result);
            }
            if (result instanceof android.hardware.radio.V1_6.RegStateResult) {
                return buildRegStateResultforV1_6((android.hardware.radio.V1_6.RegStateResult) result);
            }
            if (result instanceof android.hardware.radio.network.RegStateResult) {
                return buildRegStateResultforT((android.hardware.radio.network.RegStateResult) result);
            }
        } else if (request == 1010) {
            if (result.getClass().isArray()) {
                response = RILUtils.convertHalDataCallResultList((SetupDataCallResult[]) result);
            } else {
                response = RILUtils.convertHalDataCallResultList((List) result);
            }
            return response.toString();
        }
        return result.toString();
    }

    private static String hidePartialChars(String s) {
        if (s != null && s.length() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i < s.length() / 2) {
                    sb.append(s.charAt(i));
                } else {
                    sb.append("*");
                }
            }
            return sb.toString();
        }
        return s;
    }

    private static String buildVoiceRegStateResultforV1_0(VoiceRegStateResult ret) {
        return "{.regState = " + RegState.toString(ret.regState) + ", .rat = " + ret.rat + ", .cssSupported = " + ret.cssSupported + ", .roamingIndicator = " + ret.roamingIndicator + ", .systemIsInPrl = " + ret.systemIsInPrl + ", .defaultRoamingIndicator = " + ret.defaultRoamingIndicator + ", .reasonForDenial = " + ret.reasonForDenial + ", .cellIdentity = {...}}";
    }

    private static String buildVoiceRegStateResultforV1_2(android.hardware.radio.V1_2.VoiceRegStateResult ret) {
        return "{.regState = " + RegState.toString(ret.regState) + ", .rat = " + ret.rat + ", .cssSupported = " + ret.cssSupported + ", .roamingIndicator = " + ret.roamingIndicator + ", .systemIsInPrl = " + ret.systemIsInPrl + ", .defaultRoamingIndicator = " + ret.defaultRoamingIndicator + ", .reasonForDenial = " + ret.reasonForDenial + ", .cellIdentity = {...}}";
    }

    private static String buildDataRegStateResultforV1_0(DataRegStateResult ret) {
        return "{.regState = " + RegState.toString(ret.regState) + ", .rat = " + ret.rat + ", .reasonDataDenied = " + ret.reasonDataDenied + ", .maxDataCalls = " + ret.maxDataCalls + ", .cellIdentity = {...}}";
    }

    private static String buildDataRegStateResultforV1_2(android.hardware.radio.V1_2.DataRegStateResult ret) {
        return "{.regState = " + RegState.toString(ret.regState) + ", .rat = " + ret.rat + ", .reasonDataDenied = " + ret.reasonDataDenied + ", .maxDataCalls = " + ret.maxDataCalls + ", .cellIdentity = {...}}";
    }

    private static String buildDataRegStateResultforV1_4(android.hardware.radio.V1_4.DataRegStateResult ret) {
        return "{.regState = " + RegState.toString(ret.base.regState) + ", .rat = " + ret.base.rat + ", .reasonDataDenied = " + ret.base.reasonDataDenied + ", .maxDataCalls = " + ret.base.maxDataCalls + ", .cellIdentity = {...}}";
    }

    private static String buildRegStateResultforV1_5(RegStateResult ret) {
        return "{.regState = " + RegState.toString(ret.regState) + ", .rat = " + ret.rat + ", .reasonForDenial = " + ret.reasonForDenial + ", .registeredPlmn = " + ret.registeredPlmn + ", .cellIdentity = {...}, .accessTechnologySpecificInfo = " + ret.accessTechnologySpecificInfo + "}";
    }

    private static String buildRegStateResultforV1_6(android.hardware.radio.V1_6.RegStateResult ret) {
        return "{.regState = " + RegState.toString(ret.regState) + ", .rat = " + ret.rat + ", .reasonForDenial = " + ret.reasonForDenial + ", .registeredPlmn = " + ret.registeredPlmn + ", .cellIdentity = {...}, .accessTechnologySpecificInfo = " + ret.accessTechnologySpecificInfo + "}";
    }

    private static String buildRegStateResultforT(android.hardware.radio.network.RegStateResult ret) {
        return "{.regState = " + RegState.toString(ret.regState) + ", .rat = " + ret.rat + ", .reasonForDenial = " + ret.reasonForDenial + ", .registeredPlmn = " + ret.registeredPlmn + ", .cellIdentity = {...}, .accessTechnologySpecificInfo = " + ret.accessTechnologySpecificInfo + "}";
    }
}
