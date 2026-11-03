package com.nxp.id.jsbl.uai.api.util;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1Integer;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import java.math.BigInteger;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class EcdsaSigValue implements Asn1Encodable {
    public static byte COMPONENT_SIZE = 16;
    public static byte COMPONENT_SIZE_ECC384 = 24;
    private final BigInteger r;
    private final BigInteger s;

    public EcdsaSigValue(BigInteger bigInteger, BigInteger bigInteger2) {
        this.r = bigInteger;
        this.s = bigInteger2;
    }

    public static EcdsaSigValue decodeFromDER(Asn1Encodable asn1Encodable) {
        Utils.checkParamNotNull(asn1Encodable);
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        return new EcdsaSigValue(((Asn1Integer) asn1Sequence.get(0)).getBigInteger(), ((Asn1Integer) asn1Sequence.get(1)).getBigInteger());
    }

    public String toString() {
        return toString(0);
    }

    public BigInteger getR() {
        return this.r;
    }

    public BigInteger getS() {
        return this.s;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        return StringUtil.repeat(' ', i) + "EcdsaSigValue [r: " + this.r + ", s: " + this.s + "]\n";
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public EcdsaSigValue m4776clone() {
        return new EcdsaSigValue(this.r, this.s);
    }

    private Asn1Sequence buildObject() {
        return new Asn1Sequence(new LinkedList() { // from class: com.nxp.id.jsbl.uai.api.util.EcdsaSigValue.1
            private static final long serialVersionUID = 1;

            {
                add(0, new Asn1Integer(EcdsaSigValue.this.r));
                add(1, new Asn1Integer(EcdsaSigValue.this.s));
            }
        });
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return buildObject().encode();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }
}
