package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1Null extends Asn1Structure {
    public static final HexString TAG = HexString.fromInteger(5);
    public static final Asn1Null INSTANCE = new Asn1Null();

    public static Asn1Null parse(HexString hexString) {
        if (!Asn1Structure.unwrap(TAG, hexString)[0].isEmpty()) {
            throw new InvalidAsn1CodingException("Null with value");
        }
        return INSTANCE;
    }

    private Asn1Null() {
        super(TAG, HexString.EMPTY);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("NULL (len=" + getValue().length() + ")");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1Null m4787clone() {
        return INSTANCE;
    }
}
