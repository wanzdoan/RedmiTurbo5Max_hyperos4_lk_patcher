package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1Boolean extends Asn1Structure {
    public static final HexString TAG = HexString.fromInteger(1);

    public static Asn1Boolean parse(HexString hexString) {
        byte bAt = Asn1Structure.unwrap(TAG, hexString)[0].at(0);
        if (bAt == -1) {
            return new Asn1Boolean(true);
        }
        if (bAt == 0) {
            return new Asn1Boolean(false);
        }
        throw new InvalidAsn1CodingException("Invalid boolean value");
    }

    public Asn1Boolean(boolean z) {
        super(TAG, z ? HexString.fromByte((byte) -1) : HexString.fromByte((byte) 0));
    }

    public boolean isTrue() {
        return getValue().equals(HexString.fromByte((byte) -1));
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("BOOLEAN (len=" + getValue().length() + ") [");
        if (((byte) getValue().toInteger()) == -1) {
            sb.append("TRUE");
        } else {
            sb.append("FALSE");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1Boolean m4780clone() {
        return new Asn1Boolean(isTrue());
    }
}
