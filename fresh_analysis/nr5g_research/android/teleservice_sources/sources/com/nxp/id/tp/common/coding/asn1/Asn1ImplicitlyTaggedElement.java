package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;

/* JADX INFO: loaded from: classes.dex */
public final class Asn1ImplicitlyTaggedElement extends Asn1Structure {
    public static Asn1ImplicitlyTaggedElement parse(HexString hexString) {
        HexString tag = Asn1Structure.getTag(hexString);
        if ((tag.at(0) & 128) == 0 && (tag.at(0) & 64) == 0) {
            throw new InvalidAsn1CodingException("Not a context-specific tag");
        }
        return new Asn1ImplicitlyTaggedElement(tag, Asn1Structure.unwrap(tag, hexString)[0]);
    }

    public Asn1ImplicitlyTaggedElement(HexString hexString, HexString hexString2) {
        super(hexString, hexString2);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.repeat(' ', i));
        sb.append("TAGGED ELEMENT (tag=");
        sb.append(getTag());
        sb.append(";len=" + getValue().length() + ") [");
        sb.append(getValue());
        sb.append("]");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1ImplicitlyTaggedElement m4785clone() {
        return new Asn1ImplicitlyTaggedElement(getTag(), getValue());
    }
}
