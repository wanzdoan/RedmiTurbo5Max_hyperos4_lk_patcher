package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1Enumerated extends Asn1Structure {
    public static final HexString TAG = HexString.fromInteger(10);

    public static Asn1Enumerated parse(HexString hexString) {
        return new Asn1Enumerated(Asn1Structure.unwrap(TAG, hexString)[0]);
    }

    public Asn1Enumerated(HexString hexString) {
        super(TAG, hexString);
    }

    public Asn1Enumerated(int i) {
        this(HexString.fromInteger(i));
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("ENUMERATED (len=" + getValue().length() + ") [");
        sb.append(getValue());
        sb.append("]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1Enumerated m4781clone() {
        return new Asn1Enumerated(getValue());
    }
}
