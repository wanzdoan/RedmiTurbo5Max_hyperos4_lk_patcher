package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1BMPString extends Asn1String {
    public static final HexString TAG = HexString.fromInteger(30);

    public static Asn1BMPString parse(HexString hexString) {
        return new Asn1BMPString(Asn1Structure.unwrap(TAG, hexString)[0]);
    }

    public Asn1BMPString(HexString hexString) {
        super(TAG, hexString);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1String
    public String getString() {
        return toAsciiString(getValue());
    }

    private static String toAsciiString(HexString hexString) {
        byte[] byteArray = hexString.toByteArray();
        int length = byteArray.length / 2;
        char[] cArr = new char[length];
        for (int i = 0; i != length; i++) {
            int i2 = i * 2;
            cArr[i] = (char) ((byteArray[i2 + 1] & TransitionInfo.INIT) | (byteArray[i2] << 8));
        }
        return String.valueOf(cArr);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("BMP STRING (len=" + getValue().length() + ") [");
        sb.append(getString());
        sb.append("]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1BMPString m4778clone() {
        return new Asn1BMPString(getValue());
    }
}
