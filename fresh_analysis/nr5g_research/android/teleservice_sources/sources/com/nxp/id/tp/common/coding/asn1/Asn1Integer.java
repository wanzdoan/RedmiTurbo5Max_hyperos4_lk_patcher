package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1Integer extends Asn1Structure {
    public static final HexString TAG = HexString.fromInteger(2);

    public static Asn1Integer parse(HexString hexString) {
        return new Asn1Integer(Asn1Structure.unwrap(TAG, hexString)[0]);
    }

    public Asn1Integer(HexString hexString) {
        super(TAG, hexString);
        if (this.value.testBit(0)) {
            this.value = this.value.prepend(HexString.zero(1));
        }
    }

    public Asn1Integer(int i) {
        this(HexString.fromInteger(i));
    }

    public Asn1Integer(BigInteger bigInteger) {
        this(HexString.fromBigInteger(bigInteger));
    }

    public int getInteger() {
        return getValue().toInteger();
    }

    public BigInteger getBigInteger() {
        return getValue().toBigInteger();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("INTEGER (len=" + getValue().length() + ") [");
        sb.append(getValue());
        sb.append("]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1Integer m4786clone() {
        return new Asn1Integer(getValue());
    }
}
