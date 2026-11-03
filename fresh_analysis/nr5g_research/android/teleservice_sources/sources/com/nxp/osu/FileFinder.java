package com.nxp.osu;

import android.content.Context;
import android.util.Log;
import com.nxp.id.tp.common.HexString;

/* JADX INFO: loaded from: classes.dex */
public abstract class FileFinder {
    private static byte[] SUCCESS_RESP = {-112, 0};
    private static byte CONFIG_ID_SIZE = 4;
    private static final byte[] OSUTrigger = {79, 112, -128, 19, 4, -34, -83, -66, -17};
    private static final byte[] SystemAID = {-46, 118, 0, 0, -123, 65, 0, 0, 0, 0, 32, 0};

    public static OSUInfo getInfo(Context context) {
        byte[] bArr = OSUTrigger;
        try {
            OSUAPPOmapiChannel oSUAPPOmapiChannel = OSUAPPOmapiChannel.getInstance(context);
            if (oSUAPPOmapiChannel == null) {
                Log.e("OSU-FileFinder", "Not able to Access the transport");
                return null;
            }
            if (!validateResponse(oSUAPPOmapiChannel.open(), SUCCESS_RESP)) {
                Log.e("OSU-FileFinder", "Not able to open the transport");
                return null;
            }
            String chipInfo = getChipInfo(oSUAPPOmapiChannel);
            if (chipInfo == null) {
                Log.e("OSU-FileFinder", "Failed to get Chip Info");
                return null;
            }
            if (chipInfo.equals("SN300")) {
                bArr = SystemAID;
            }
            if (!validateResponse(oSUAPPOmapiChannel.openChannel(bArr), SUCCESS_RESP)) {
                Log.e("OSU-FileFinder", "Not able to open the channel");
                return null;
            }
            byte[] bArrSend = oSUAPPOmapiChannel.send(new byte[]{-128, -54, 0, -2, 2, -33, 35});
            byte[] confIDResponse = parseConfIDResponse(bArrSend);
            if (confIDResponse == null) {
                oSUAPPOmapiChannel.close();
                Log.e("OSU-FileFinder", "Failed to get Configuration ID response");
                return null;
            }
            Log.d("OSU-FileFinder", "Get Config ID Resp : " + HexString.fromByteArray(bArrSend, 0));
            byte[] bArrSend2 = oSUAPPOmapiChannel.send(new byte[]{-128, -54, 0, -2, 2, -33, 67});
            oSUAPPOmapiChannel.close();
            if (bArrSend2 != null && validateResponse(bArrSend2, SUCCESS_RESP)) {
                UAIQueryResponse uAIQueryResponse = UAIQueryResponse.parse(bArrSend2);
                if (uAIQueryResponse == null) {
                    Log.e("OSU-FileFinder", "Failed to parse uaiQuery");
                    return null;
                }
                String str = chipInfo + "_" + String.format("%X", Integer.valueOf(uAIQueryResponse.RSN)) + "_" + HexString.fromByteArray(confIDResponse, 0);
                Log.d("OSU-FileFinder", "File Pattern to be Search: ".concat(str));
                return new OSUInfo(str, confIDResponse, uAIQueryResponse);
            }
            Log.e("OSU-FileFinder", "Failed to get UAIquery response");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] parseConfIDResponse(byte[] bArr) {
        if (bArr == null || !validateResponse(bArr, SUCCESS_RESP)) {
            Log.e("OSU-FileFinder", "Failed to get Configuration ID response");
            return null;
        }
        byte[] bArr2 = new byte[CONFIG_ID_SIZE];
        byte[] bArr3 = {-2, 7, -33, 35, 4};
        short s = 2;
        while (s < 5) {
            try {
                if (bArr3[s] != bArr[s]) {
                    Log.e("OSU-FileFinder", "Invalid Configuration ID response");
                    return null;
                }
                s = (short) (s + 1);
            } catch (Exception e) {
                e.printStackTrace();
                return bArr2;
            }
        }
        System.arraycopy(bArr, s, bArr2, 0, CONFIG_ID_SIZE);
        return bArr2;
    }

    private static boolean validateResponse(byte[] bArr, byte[] bArr2) {
        if (bArr != null && bArr.length >= 2 && bArr2 != null && bArr2.length == 2) {
            return bArr2[1] == bArr[bArr.length - 1] && bArr2[0] == bArr[bArr.length - 2];
        }
        Log.e("OSU-FileFinder", "Invalid Response to Check");
        return false;
    }

    private static String getChipInfo(OSUAPPOmapiChannel oSUAPPOmapiChannel) {
        byte[] aTRInfo;
        if (oSUAPPOmapiChannel == null) {
            return null;
        }
        try {
            aTRInfo = oSUAPPOmapiChannel.getATRInfo();
        } catch (Exception unused) {
            aTRInfo = null;
        }
        if (aTRInfo != null && aTRInfo.length >= 5) {
            byte b = aTRInfo[4];
            if (b >= 16 && b < 32) {
                return "SN110";
            }
            if (b >= 32 && b < 48) {
                return "SN220";
            }
            if (b >= 48) {
                return "SN300";
            }
        }
        return null;
    }
}
