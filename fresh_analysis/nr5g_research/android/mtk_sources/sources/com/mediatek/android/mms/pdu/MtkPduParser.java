package com.mediatek.android.mms.pdu;

import android.util.Log;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPart;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import mediatek.telephony.MtkSmsManager;

/* JADX INFO: loaded from: classes.dex */
public class MtkPduParser extends PduParser {
    private static final String LOG_TAG = "MtkPduParser";

    public MtkPduParser(byte[] pduDataStream, boolean parseContentDisposition) {
        super(pduDataStream, parseContentDisposition);
    }

    protected boolean parseExtPartHeader(ByteArrayInputStream pduDataStream, PduPart part, int header, int startPos, int length) {
        Log.d(LOG_TAG, "parseExtPartHeader, header = " + header);
        switch (header) {
            case 167:
                byte[] transferEncoding = parseWapString(pduDataStream, 0);
                if (transferEncoding != null) {
                    part.setContentTransferEncoding(transferEncoding);
                    return true;
                }
                return true;
            default:
                return false;
        }
    }

    protected static boolean parseExtContentTypeParams(ByteArrayInputStream pduDataStream, HashMap<Integer, Object> map, Integer length, int param, int startPos) {
        Log.d(LOG_TAG, "parseExtPartHeader, param = " + param);
        switch (param) {
            case 134:
            case 152:
                byte[] fileName = parseWapString(pduDataStream, 0);
                if (fileName != null && map != null) {
                    map.put(152, fileName);
                }
                return true;
            case 140:
            case 155:
                byte[] comment = parseWapString(pduDataStream, 0);
                if (comment != null && map != null) {
                    map.put(155, comment);
                }
                return true;
            case 141:
            case 156:
                byte[] domain = parseWapString(pduDataStream, 0);
                if (domain != null && map != null) {
                    map.put(156, domain);
                }
                return true;
            case MtkSmsManager.VALIDITY_PERIOD_TWELVE_HOURS /* 143 */:
            case 157:
                byte[] path = parseWapString(pduDataStream, 0);
                if (path != null && map != null) {
                    map.put(157, path);
                }
                return true;
            default:
                return false;
        }
    }
}
