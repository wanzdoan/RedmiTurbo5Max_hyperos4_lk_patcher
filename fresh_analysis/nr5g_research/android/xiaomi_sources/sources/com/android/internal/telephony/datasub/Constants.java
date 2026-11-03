package com.android.internal.telephony.datasub;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Constants {
    public static final int NETWORK_CLASS_2_G = 1;
    public static final int NETWORK_CLASS_3_G = 2;
    public static final int NETWORK_CLASS_4_G = 3;
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NETWORK_MODE_CDMA = 4;
    public static final int NETWORK_MODE_CDMA_NO_EVDO = 5;
    public static final int NETWORK_MODE_EVDO_NO_CDMA = 6;
    public static final int NETWORK_MODE_GLOBAL = 7;
    public static final int NETWORK_MODE_GSM_ONLY = 1;
    public static final int NETWORK_MODE_GSM_UMTS = 3;
    public static final int NETWORK_MODE_TDSCDMA_CDMA_EVDO_GSM_WCDMA = 21;
    public static final int NETWORK_MODE_TDSCDMA_GSM = 16;
    public static final int NETWORK_MODE_TDSCDMA_GSM_WCDMA = 18;
    public static final int NETWORK_MODE_TDSCDMA_ONLY = 13;
    public static final int NETWORK_MODE_TDSCDMA_WCDMA = 14;
    public static final int NETWORK_MODE_WCDMA_ONLY = 2;
    public static final int NETWORK_MODE_WCDMA_PREF = 0;
    public static final int NETWORK_TYPE_LTE = 13;
    public static final int NETWORK_TYPE_LTE_CA = 19;
    public static final int NETWORK_TYPE_NR = 20;
    public static final int NETWORK_TYPE_NR_CA = 30;
    public static final List<String> sFailure = new ArrayList();

    static {
        sFailure.add("OPERATOR_BARRED");
        sFailure.add("MISSING_UNKNOWN_APN");
        sFailure.add("UNKNOWN_PDP_ADDRESS_TYPE");
        sFailure.add("USER_AUTHENTICATION");
        sFailure.add("ACTIVATION_REJECT_GGSN");
        sFailure.add("SERVICE_OPTION_NOT_SUPPORTED");
        sFailure.add("SERVICE_OPTION_NOT_SUBSCRIBED");
        sFailure.add("NSAPI_IN_USE");
        sFailure.add("ONLY_IPV4_ALLOWED");
        sFailure.add("ONLY_IPV6_ALLOWED");
        sFailure.add("PROTOCOL_ERRORS");
        sFailure.add("RADIO_POWER_OFF");
        sFailure.add("TETHERED_CALL_ACTIVE");
        sFailure.add("RADIO_NOT_AVAILABLE");
        sFailure.add("UNACCEPTABLE_NETWORK_PARAMETER");
        sFailure.add("SIGNAL_LOST");
        sFailure.add("DUPLICATE_CID");
        sFailure.add("MATCH_ALL_RULE_NOT_ALLOWED");
        sFailure.add("ALL_MATCHING_RULES_FAILED");
        sFailure.add("NO_RETRY_FAILURE");
    }
}
