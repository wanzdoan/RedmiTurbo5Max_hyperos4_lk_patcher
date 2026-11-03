package com.android.internal.telephony;

import com.android.internal.telephony.ntnphone.NtnRILConstants;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class WspTypeDecoder {
    public static final String CONTENT_TYPE_B_MMS = "application/vnd.wap.mms-message";
    public static final String CONTENT_TYPE_B_PUSH_CO = "application/vnd.wap.coc";
    public static final String CONTENT_TYPE_B_PUSH_SYNCML_NOTI = "application/vnd.syncml.notification";
    public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 47;
    public static final int PDU_TYPE_CONFIRMED_PUSH = 7;
    public static final int PDU_TYPE_PUSH = 6;
    protected static final int WAP_PDU_LENGTH_QUOTE = 31;
    private static final HashMap WELL_KNOWN_MIME_TYPES;
    private static final HashMap WELL_KNOWN_PARAMETERS;
    HashMap mContentParameters;
    protected int mDataLength;
    protected String mStringValue;
    protected long mUnsigned32bit;
    protected byte[] mWspData;

    static {
        HashMap map = new HashMap();
        WELL_KNOWN_MIME_TYPES = map;
        HashMap map2 = new HashMap();
        WELL_KNOWN_PARAMETERS = map2;
        map.put(0, "*/*");
        map.put(1, "text/*");
        map.put(2, "text/html");
        map.put(3, "text/plain");
        map.put(4, "text/x-hdml");
        map.put(5, "text/x-ttml");
        map.put(6, "text/x-vCalendar");
        map.put(7, "text/x-vCard");
        map.put(8, "text/vnd.wap.wml");
        map.put(9, "text/vnd.wap.wmlscript");
        map.put(10, "text/vnd.wap.wta-event");
        map.put(11, "multipart/*");
        map.put(12, "multipart/mixed");
        map.put(13, "multipart/form-data");
        map.put(14, "multipart/byterantes");
        map.put(15, "multipart/alternative");
        map.put(16, "application/*");
        map.put(17, "application/java-vm");
        map.put(18, "application/x-www-form-urlencoded");
        map.put(19, "application/x-hdmlc");
        map.put(20, "application/vnd.wap.wmlc");
        map.put(21, "application/vnd.wap.wmlscriptc");
        map.put(22, "application/vnd.wap.wta-eventc");
        map.put(23, "application/vnd.wap.uaprof");
        map.put(24, "application/vnd.wap.wtls-ca-certificate");
        map.put(25, "application/vnd.wap.wtls-user-certificate");
        map.put(26, "application/x-x509-ca-cert");
        map.put(27, "application/x-x509-user-cert");
        map.put(28, "image/*");
        map.put(29, "image/gif");
        map.put(30, "image/jpeg");
        map.put(31, "image/tiff");
        map.put(32, "image/png");
        map.put(33, "image/vnd.wap.wbmp");
        map.put(34, "application/vnd.wap.multipart.*");
        map.put(35, "application/vnd.wap.multipart.mixed");
        map.put(36, "application/vnd.wap.multipart.form-data");
        map.put(37, "application/vnd.wap.multipart.byteranges");
        map.put(38, "application/vnd.wap.multipart.alternative");
        map.put(39, "application/xml");
        map.put(40, "text/xml");
        map.put(41, "application/vnd.wap.wbxml");
        map.put(42, "application/x-x968-cross-cert");
        map.put(43, "application/x-x968-ca-cert");
        map.put(44, "application/x-x968-user-cert");
        map.put(45, "text/vnd.wap.si");
        map.put(46, "application/vnd.wap.sic");
        map.put(47, "text/vnd.wap.sl");
        map.put(48, "application/vnd.wap.slc");
        map.put(49, "text/vnd.wap.co");
        map.put(50, CONTENT_TYPE_B_PUSH_CO);
        map.put(51, "application/vnd.wap.multipart.related");
        map.put(52, "application/vnd.wap.sia");
        map.put(53, "text/vnd.wap.connectivity-xml");
        map.put(54, "application/vnd.wap.connectivity-wbxml");
        map.put(55, "application/pkcs7-mime");
        map.put(56, "application/vnd.wap.hashed-certificate");
        map.put(57, "application/vnd.wap.signed-certificate");
        map.put(58, "application/vnd.wap.cert-response");
        map.put(59, "application/xhtml+xml");
        map.put(60, "application/wml+xml");
        map.put(61, "text/css");
        map.put(62, CONTENT_TYPE_B_MMS);
        map.put(63, "application/vnd.wap.rollover-certificate");
        map.put(64, "application/vnd.wap.locc+wbxml");
        map.put(65, "application/vnd.wap.loc+xml");
        map.put(66, "application/vnd.syncml.dm+wbxml");
        map.put(67, "application/vnd.syncml.dm+xml");
        map.put(68, CONTENT_TYPE_B_PUSH_SYNCML_NOTI);
        map.put(69, "application/vnd.wap.xhtml+xml");
        map.put(70, "application/vnd.wv.csp.cir");
        map.put(71, "application/vnd.oma.dd+xml");
        map.put(72, "application/vnd.oma.drm.message");
        map.put(73, "application/vnd.oma.drm.content");
        map.put(74, "application/vnd.oma.drm.rights+xml");
        map.put(75, "application/vnd.oma.drm.rights+wbxml");
        map.put(76, "application/vnd.wv.csp+xml");
        map.put(77, "application/vnd.wv.csp+wbxml");
        map.put(78, "application/vnd.syncml.ds.notification");
        map.put(79, "audio/*");
        map.put(80, "video/*");
        map.put(81, "application/vnd.oma.dd2+xml");
        map.put(82, "application/mikey");
        map.put(83, "application/vnd.oma.dcd");
        map.put(84, "application/vnd.oma.dcdc");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_13), "application/vnd.uplanet.cacheop-wbxml");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_14), "application/vnd.uplanet.signal");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_15), "application/vnd.uplanet.alert-wbxml");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_16), "application/vnd.uplanet.list-wbxml");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_17), "application/vnd.uplanet.listcmd-wbxml");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_18), "application/vnd.uplanet.channel-wbxml");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_19), "application/vnd.uplanet.provisioning-status-uri");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_20), "x-wap.multipart/vnd.uplanet.header-set");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_21), "application/vnd.uplanet.bearer-choice-wbxml");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_22), "application/vnd.phonecom.mmc-wbxml");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_23), "application/vnd.nokia.syncset+wbxml");
        map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_24), "image/x-up-wpng");
        map.put(768, "application/iota.mmc-wbxml");
        map.put(769, "application/iota.mmc-xml");
        map.put(770, "application/vnd.syncml+xml");
        map.put(771, "application/vnd.syncml+wbxml");
        map.put(772, "text/vnd.wap.emn+xml");
        map.put(773, "text/calendar");
        map.put(774, "application/vnd.omads-email+xml");
        map.put(775, "application/vnd.omads-file+xml");
        map.put(776, "application/vnd.omads-folder+xml");
        map.put(777, "text/directory;profile=vCard");
        map.put(778, "application/vnd.wap.emn+wbxml");
        map.put(779, "application/vnd.nokia.ipdc-purchase-response");
        map.put(780, "application/vnd.motorola.screen3+xml");
        map.put(781, "application/vnd.motorola.screen3+gzip");
        map.put(782, "application/vnd.cmcc.setting+wbxml");
        map.put(783, "application/vnd.cmcc.bombing+wbxml");
        map.put(784, "application/vnd.docomo.pf");
        map.put(785, "application/vnd.docomo.ub");
        map.put(786, "application/vnd.omaloc-supl-init");
        map.put(787, "application/vnd.oma.group-usage-list+xml");
        map.put(788, "application/oma-directory+xml");
        map.put(789, "application/vnd.docomo.pf2");
        map.put(790, "application/vnd.oma.drm.roap-trigger+wbxml");
        map.put(791, "application/vnd.sbm.mid2");
        map.put(792, "application/vnd.wmf.bootstrap");
        map.put(793, "application/vnc.cmcc.dcd+xml");
        map.put(794, "application/vnd.sbm.cid");
        map.put(795, "application/vnd.oma.bcast.provisioningtrigger");
        map2.put(0, "Q");
        map2.put(1, "Charset");
        map2.put(2, "Level");
        map2.put(3, "Type");
        map2.put(7, "Differences");
        map2.put(8, "Padding");
        map2.put(9, "Type");
        map2.put(14, "Max-Age");
        map2.put(16, "Secure");
        map2.put(17, "SEC");
        map2.put(18, "MAC");
        map2.put(19, "Creation-date");
        map2.put(20, "Modification-date");
        map2.put(21, "Read-date");
        map2.put(22, "Size");
        map2.put(23, "Name");
        map2.put(24, "Filename");
        map2.put(25, "Start");
        map2.put(26, "Start-info");
        map2.put(27, "Comment");
        map2.put(28, "Domain");
        map2.put(29, "Path");
    }

    public WspTypeDecoder(byte[] bArr) {
        this.mWspData = bArr;
    }

    public boolean decodeTextString(int i) {
        byte[] bArr;
        int i2 = i;
        while (true) {
            bArr = this.mWspData;
            if (bArr[i2] == 0) {
                break;
            }
            i2++;
        }
        int i3 = i2 - i;
        this.mDataLength = i3 + 1;
        if (bArr[i] == 127) {
            this.mStringValue = new String(bArr, i + 1, i3 - 1);
        } else {
            this.mStringValue = new String(bArr, i, i3);
        }
        return true;
    }

    public boolean decodeTokenText(int i) {
        int i2 = i;
        while (true) {
            byte[] bArr = this.mWspData;
            if (bArr[i2] == 0) {
                int i3 = i2 - i;
                this.mDataLength = i3 + 1;
                this.mStringValue = new String(bArr, i, i3);
                return true;
            }
            i2++;
        }
    }

    public boolean decodeShortInteger(int i) {
        byte b = this.mWspData[i];
        if ((b & 128) == 0) {
            return false;
        }
        this.mUnsigned32bit = b & 127;
        this.mDataLength = 1;
        return true;
    }

    public boolean decodeLongInteger(int i) {
        int i2 = this.mWspData[i] & 255;
        if (i2 > 30) {
            return false;
        }
        this.mUnsigned32bit = 0L;
        for (int i3 = 1; i3 <= i2; i3++) {
            this.mUnsigned32bit = (this.mUnsigned32bit << 8) | ((long) (this.mWspData[i + i3] & 255));
        }
        this.mDataLength = i2 + 1;
        return true;
    }

    public boolean decodeIntegerValue(int i) {
        if (decodeShortInteger(i)) {
            return true;
        }
        return decodeLongInteger(i);
    }

    public boolean decodeUintvarInteger(int i) {
        this.mUnsigned32bit = 0L;
        int i2 = i;
        while (true) {
            byte b = this.mWspData[i2];
            if ((b & 128) == 0) {
                this.mUnsigned32bit = (this.mUnsigned32bit << 7) | ((long) (b & 127));
                this.mDataLength = (i2 - i) + 1;
                return true;
            }
            if (i2 - i >= 4) {
                return false;
            }
            this.mUnsigned32bit = (this.mUnsigned32bit << 7) | ((long) (b & 127));
            i2++;
        }
    }

    public boolean decodeValueLength(int i) {
        byte b = this.mWspData[i];
        if ((b & 255) > 31) {
            return false;
        }
        if (b < 31) {
            this.mUnsigned32bit = b;
            this.mDataLength = 1;
        } else {
            decodeUintvarInteger(i + 1);
            this.mDataLength++;
        }
        return true;
    }

    public boolean decodeExtensionMedia(int i) {
        this.mDataLength = 0;
        this.mStringValue = null;
        int length = this.mWspData.length;
        boolean z = i < length;
        int i2 = i;
        while (i2 < length && this.mWspData[i2] != 0) {
            i2++;
        }
        int i3 = i2 - i;
        this.mDataLength = i3 + 1;
        this.mStringValue = new String(this.mWspData, i, i3);
        return z;
    }

    public boolean decodeConstrainedEncoding(int i) {
        if (decodeShortInteger(i)) {
            this.mStringValue = null;
            return true;
        }
        return decodeExtensionMedia(i);
    }

    public boolean decodeContentType(int i) {
        this.mContentParameters = new HashMap();
        try {
            if (!decodeValueLength(i)) {
                boolean zDecodeConstrainedEncoding = decodeConstrainedEncoding(i);
                if (zDecodeConstrainedEncoding) {
                    expandWellKnownMimeType();
                }
                return zDecodeConstrainedEncoding;
            }
            int i2 = (int) this.mUnsigned32bit;
            int decodedDataLength = getDecodedDataLength();
            int i3 = i + decodedDataLength;
            if (decodeIntegerValue(i3)) {
                int i4 = this.mDataLength + decodedDataLength;
                this.mDataLength = i4;
                this.mStringValue = null;
                expandWellKnownMimeType();
                long j = this.mUnsigned32bit;
                String str = this.mStringValue;
                int i5 = this.mDataLength;
                if (!readContentParameters(i + i5, i2 - (i5 - decodedDataLength), 0)) {
                    return false;
                }
                this.mDataLength += i4;
                this.mUnsigned32bit = j;
                this.mStringValue = str;
                return true;
            }
            if (decodeExtensionMedia(i3)) {
                int i6 = this.mDataLength + decodedDataLength;
                this.mDataLength = i6;
                expandWellKnownMimeType();
                long j2 = this.mUnsigned32bit;
                String str2 = this.mStringValue;
                int i7 = this.mDataLength;
                if (readContentParameters(i + i7, i2 - (i7 - decodedDataLength), 0)) {
                    this.mDataLength += i6;
                    this.mUnsigned32bit = j2;
                    this.mStringValue = str2;
                    return true;
                }
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException unused) {
        }
    }

    private boolean readContentParameters(int i, int i2, int i3) {
        int i4;
        String str;
        int i5;
        String strSubstring;
        if (i2 > 0) {
            byte b = this.mWspData[i];
            if ((b & 128) == 0 && b > 31) {
                decodeTokenText(i);
                str = this.mStringValue;
                i4 = this.mDataLength;
            } else {
                if (!decodeIntegerValue(i)) {
                    return false;
                }
                int i6 = this.mDataLength;
                int i7 = (int) this.mUnsigned32bit;
                String str2 = (String) WELL_KNOWN_PARAMETERS.get(Integer.valueOf(i7));
                if (str2 == null) {
                    str2 = "unassigned/0x" + Long.toHexString(i7);
                }
                if (i7 == 0) {
                    if (!decodeUintvarInteger(i + i6)) {
                        return false;
                    }
                    int i8 = i6 + this.mDataLength;
                    this.mContentParameters.put(str2, String.valueOf(this.mUnsigned32bit));
                    return readContentParameters(i + i8, i2 - i8, i3 + i8);
                }
                i4 = i6;
                str = str2;
            }
            int i9 = i + i4;
            if (decodeNoValue(i9)) {
                i5 = i4 + this.mDataLength;
                strSubstring = null;
            } else if (decodeIntegerValue(i9)) {
                i5 = i4 + this.mDataLength;
                strSubstring = String.valueOf((int) this.mUnsigned32bit);
            } else {
                decodeTokenText(i9);
                i5 = i4 + this.mDataLength;
                String str3 = this.mStringValue;
                strSubstring = str3.startsWith("\"") ? str3.substring(1) : str3;
            }
            this.mContentParameters.put(str, strSubstring);
            return readContentParameters(i + i5, i2 - i5, i3 + i5);
        }
        this.mDataLength = i3;
        return true;
    }

    private boolean decodeNoValue(int i) {
        if (this.mWspData[i] != 0) {
            return false;
        }
        this.mDataLength = 1;
        return true;
    }

    private void expandWellKnownMimeType() {
        if (this.mStringValue == null) {
            this.mStringValue = (String) WELL_KNOWN_MIME_TYPES.get(Integer.valueOf((int) this.mUnsigned32bit));
        } else {
            this.mUnsigned32bit = -1L;
        }
    }

    public boolean decodeContentLength(int i) {
        return decodeIntegerValue(i);
    }

    public boolean decodeContentLocation(int i) {
        return decodeTextString(i);
    }

    public boolean decodeXWapApplicationId(int i) {
        if (decodeIntegerValue(i)) {
            this.mStringValue = null;
            return true;
        }
        return decodeTextString(i);
    }

    public boolean seekXWapApplicationId(int i, int i2) {
        int decodedDataLength;
        while (i <= i2) {
            try {
                if (decodeIntegerValue(i)) {
                    if (((int) getValue32()) == 47) {
                        this.mUnsigned32bit = i + 1;
                        return true;
                    }
                } else if (!decodeTextString(i)) {
                    return false;
                }
                int decodedDataLength2 = i + getDecodedDataLength();
                if (decodedDataLength2 > i2) {
                    return false;
                }
                byte b = this.mWspData[decodedDataLength2];
                if (b < 0 || b > 30) {
                    if (b == 31) {
                        decodedDataLength2++;
                        if (decodedDataLength2 >= i2 || !decodeUintvarInteger(decodedDataLength2)) {
                            return false;
                        }
                        decodedDataLength = getDecodedDataLength();
                    } else if (31 >= b || b > 127) {
                        i = decodedDataLength2 + 1;
                    } else {
                        if (!decodeTextString(decodedDataLength2)) {
                            return false;
                        }
                        decodedDataLength = getDecodedDataLength();
                    }
                    i = decodedDataLength2 + decodedDataLength;
                } else {
                    i = decodedDataLength2 + b + 1;
                }
            } catch (ArrayIndexOutOfBoundsException unused) {
            }
        }
        return false;
    }

    public boolean decodeXWapContentURI(int i) {
        return decodeTextString(i);
    }

    public boolean decodeXWapInitiatorURI(int i) {
        return decodeTextString(i);
    }

    public int getDecodedDataLength() {
        return this.mDataLength;
    }

    public long getValue32() {
        return this.mUnsigned32bit;
    }

    public String getValueString() {
        return this.mStringValue;
    }

    public HashMap<String, String> getContentParameters() {
        return this.mContentParameters;
    }
}
