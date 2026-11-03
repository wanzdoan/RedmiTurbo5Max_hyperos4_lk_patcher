package com.nxp.osu;

/* JADX INFO: loaded from: classes.dex */
public class EuiccCmdParserMockRsp {
    private static final byte[] CCI_CHECK = {-95, -86};
    private static final byte[] JCI_CHECK = {-95, -69};
    private static final byte[] DGI_HEADER = {-33, 92};
    static final byte[] RQST_RESET = {-128, -33, -17, 0, 0};
    static final byte[] RQST_UAIINFO = {-128, -54, 0, -2, 2, -33, 67};
    static final byte[] RQST_CONFIG_ID = {-128, -54, 0, -2, 2, -33, 35};
    static final byte[] RQST_TRIGGER_INITIAL_STEP = {79, 112, -128, 19, 4, 74, 85, 65, 73};
    static final byte[] RQST_TRIGGER_OSU = {79, 112, -128, 19, 4, -34, -83, -66, -17};
    private static final byte[] RESP_SUCCESS = {-112, 0};
    private static final byte[] RESP_INTEG_CHECK = {-33, 92, 0, -112, 0};
    private static byte[] RESP_UAIINFO = {-2, -127, -77, -33, 67, -127, -81, -128, 1, 0, -127, 2, 54, 60, -126, 2, 54, 60, -125, 2, 54, 60, -124, 2, 54, 60, -123, 2, 0, 90, -122, 24, 7, 4, 17, 34, -8, -19, -53, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -121, 1, -36, -120, 32, -79, 29, 82, -98, 110, -34, 76, -66, 112, 83, 13, -55, 36, 124, -40, 13, -68, 95, 58, 92, -18, 1, -59, 27, 76, 73, 78, -83, -5, -40, -8, -23, -119, 32, -8, -97, 48, -20, 125, -28, 119, 123, 124, -87, -29, 74, -92, -46, -8, -117, 100, -61, -100, -91, 19, -37, 52, -83, -124, 57, 34, 76, 107, 46, 10, -84, -118, 1, -91, -117, 2, 34, 34, -116, 32, -95, -114, -2, 32, -45, -111, 102, 76, 116, -10, -98, -111, 103, -119, -62, 110, -31, 99, 31, -78, -57, 125, -62, 36, 96, 61, 67, -28, 65, -39, 61, -68, -115, 2, 0, 0, -114, 2, 0, 0, -112, 1, 0, -112, 1, 0, -112, 0};
    private static byte[] RESP_CONFIG_ID = {-2, 7, -33, 35, 4, 0, 1, 2, 3, -112, 0};
    private boolean mOsuTriggered = false;
    private int mCSNIndex = 12;
    private int mRSNIndex = 16;
    private int mFSNIndex = 20;
    private int mPSNIndex = 24;
    private int mOsIdIndex = 28;
    private int mStateIdIndex = 132;
    private int mBaseSN = 13884;
    private int mCSN = 13884;
    private int mRSN = 13884;
    private int mFSN = 13884;
    private int mPSN = 13884;
    private int mOsId = 90;
    private int mStateId = 23130;
}
