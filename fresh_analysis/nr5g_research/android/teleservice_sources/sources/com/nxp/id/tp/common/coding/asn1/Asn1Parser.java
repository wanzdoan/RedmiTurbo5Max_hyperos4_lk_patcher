package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;

/* JADX INFO: loaded from: classes.dex */
public abstract class Asn1Parser {
    public static Asn1Encodable parse(HexString hexString) {
        return parse(hexString, true);
    }

    public static Asn1Encodable parse(HexString hexString, boolean z) {
        if (hexString == null) {
            throw new InvalidAsn1CodingException("Input may not be null");
        }
        HexString tag = Asn1Structure.getTag(hexString);
        int iAt = tag.at(0) & 224;
        if (iAt == 160 || iAt == 96 || iAt == 224) {
            return Asn1TaggedContainer.parse(hexString);
        }
        if ((tag.at(0) & 128) != 0 || (tag.at(0) & 64) != 0) {
            return Asn1ImplicitlyTaggedElement.parse(hexString);
        }
        int integer = tag.toInteger();
        if (integer == 10) {
            return Asn1Enumerated.parse(hexString);
        }
        if (integer == 27) {
            return Asn1GeneralString.parse(hexString);
        }
        if (integer == 30) {
            return Asn1BMPString.parse(hexString);
        }
        if (integer == 18) {
            return Asn1NumericString.parse(hexString);
        }
        if (integer == 19) {
            return Asn1PrintableString.parse(hexString);
        }
        if (integer == 48) {
            return Asn1Sequence.parse(hexString, z);
        }
        if (integer != 49) {
            switch (integer) {
                case 1:
                    return Asn1Boolean.parse(hexString);
                case 2:
                    return Asn1Integer.parse(hexString);
                case 3:
                    return Asn1BitString.parse(hexString);
                case 4:
                    return Asn1OctetString.parse(hexString);
                case 5:
                    return Asn1Null.parse(hexString);
                case 6:
                    return Asn1Oid.parse(hexString);
                default:
                    switch (integer) {
                        case 22:
                            return Asn1IA5String.parse(hexString);
                        case 23:
                            return Asn1UTCTime.parse(hexString);
                        case 24:
                            return Asn1GeneralizedTime.parse(hexString);
                        default:
                            throw new InvalidAsn1CodingException("Unknown tag '" + tag + "' or unimplemented type");
                    }
            }
        }
        return Asn1Set.parse(hexString);
    }
}
