package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1BitString extends Asn1Structure {
    public static final HexString TAG = HexString.fromInteger(3);
    private HexString unused;

    public static Asn1BitString parse(HexString hexString) {
        try {
            return new Asn1BitString(Asn1Structure.unwrap(TAG, hexString)[0], true);
        } catch (IllegalArgumentException e) {
            throw new InvalidAsn1CodingException(e);
        }
    }

    public Asn1BitString(HexString hexString) {
        this(hexString, (byte) 0);
    }

    public Asn1BitString(HexString hexString, byte b) {
        super(TAG, hexString);
        this.unused = HexString.zero(1);
        if (hexString.isEmpty() && b != 0) {
            Folme$$ExternalSyntheticBUOutline0.m("Empty data, non-empty pad bits!");
            throw null;
        }
        if (b > 7 || b < 0) {
            Folme$$ExternalSyntheticBUOutline0.m("Pad bits invalid");
            throw null;
        }
        this.unused = HexString.fromByte(b);
    }

    private Asn1BitString(HexString hexString, boolean z) {
        super(TAG, hexString.substring(1));
        this.unused = HexString.zero(1);
        if (z) {
            this.unused = hexString.substring(0, 1);
            if (hexString.substring(1).isEmpty() && this.unused.toInteger() != 0) {
                Folme$$ExternalSyntheticBUOutline0.m("Empty data, non-empty pad bits!");
                throw null;
            }
            if (this.unused.toInteger() > 7 || this.unused.toInteger() < 0) {
                Folme$$ExternalSyntheticBUOutline0.m("Pad bits invalid");
                throw null;
            }
        }
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Structure, com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return getTag().append(Asn1Structure.getDerLength(getPayloadLength() + this.unused.length())).append(this.unused).append(getValue());
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("BIT STRING (len=" + getValue().length() + ") [");
        sb.append(getValue());
        sb.append(" (");
        sb.append(this.unused.toInteger());
        sb.append(" padbits)]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1BitString m4779clone() {
        Asn1BitString asn1BitString = new Asn1BitString(getValue());
        asn1BitString.unused = this.unused;
        return asn1BitString;
    }
}
