package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1IA5String extends Asn1String {
    public static final HexString TAG = HexString.fromInteger(22);

    public static Asn1IA5String parse(HexString hexString) {
        return new Asn1IA5String(Asn1Structure.unwrap(TAG, hexString)[0]);
    }

    public Asn1IA5String(String str) {
        super(TAG, HexString.fromAsciiString(str));
    }

    public Asn1IA5String(HexString hexString) {
        super(TAG, hexString);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1String
    public String getString() {
        return getValue().toAsciiString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("IA5 STRING (len=" + getValue().length() + ") [");
        sb.append(getString());
        sb.append("]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1IA5String m4784clone() {
        return new Asn1IA5String(getValue());
    }
}
