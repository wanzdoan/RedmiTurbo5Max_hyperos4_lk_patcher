package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1PrintableString extends Asn1String {
    public static final HexString TAG = HexString.fromInteger(19);

    public static Asn1PrintableString parse(HexString hexString) {
        return new Asn1PrintableString(Asn1Structure.unwrap(TAG, hexString)[0]);
    }

    public Asn1PrintableString(HexString hexString) {
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
        sb.append("PRINTABLE STRING (len=" + getValue().length() + ") [");
        sb.append(getString());
        sb.append("]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1PrintableString m4791clone() {
        return new Asn1PrintableString(getValue());
    }
}
