package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.xiaomi.internal.telephony.DynamicSarService;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public abstract class Asn1Structure implements Asn1Encodable {
    protected HexString tag;
    protected HexString value;

    public Asn1Structure(HexString hexString, HexString hexString2) {
        this.tag = hexString;
        this.value = hexString2;
    }

    public int getPayloadLength() {
        return this.value.length();
    }

    public HexString getTag() {
        return this.tag;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return this.value;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return this.tag.append(getDerLength(this.value.length())).append(this.value);
    }

    public String toString() {
        return toString(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof Asn1Structure)) {
            Asn1Structure asn1Structure = (Asn1Structure) obj;
            if (this.tag.equals(asn1Structure.tag) && this.value.length() == asn1Structure.value.length() && this.value.equals(asn1Structure.value)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.value.hashCode() ^ (this.tag.hashCode() ^ this.value.length());
    }

    static HexString[] unwrap(HexString hexString) {
        try {
            int length = getTag(hexString).length();
            int i = length + 1;
            int integer = hexString.substring(length, i).toInteger();
            int i2 = 1;
            if (integer >= 128) {
                i2 = 1 + (integer & 15);
                integer = hexString.substring(i, length + i2).toInteger();
            }
            int i3 = length + i2;
            int i4 = integer + i3;
            return new HexString[]{hexString.substring(i3, i4), hexString.substring(i4)};
        } catch (StringIndexOutOfBoundsException unused) {
            throw new InvalidAsn1CodingException("Invalid BER coding. Data: " + hexString);
        }
    }

    static HexString[] unwrap(HexString hexString, HexString hexString2) {
        return unwrap(hexString, hexString2, true);
    }

    static HexString[] unwrap(HexString hexString, HexString hexString2, boolean z) {
        if (!hexString2.startsWith(hexString)) {
            throw new InvalidAsn1CodingException("Input did not start with expected tag " + hexString.toString() + ". Data: " + hexString2);
        }
        try {
            int length = hexString.length();
            int i = length + 1;
            int integer = hexString2.substring(length, i).toInteger();
            int i2 = 1;
            if (integer >= 128) {
                i2 = 1 + (integer & 15);
                integer = hexString2.substring(i, length + i2).toInteger();
            }
            int i3 = length + i2;
            if (i3 == hexString2.length()) {
                HexString hexString3 = HexString.EMPTY;
                return new HexString[]{hexString3, hexString3};
            }
            if (z) {
                int i4 = integer + i3;
                return new HexString[]{hexString2.substring(i3, i4), hexString2.substring(i4)};
            }
            return new HexString[]{hexString2.substring(i3, integer + i3), HexString.fromString("")};
        } catch (StringIndexOutOfBoundsException unused) {
            throw new InvalidAsn1CodingException("Invalid BER coding. Data: " + hexString2);
        }
    }

    static HexString[] removeTlv(HexString hexString) {
        try {
            int length = getTag(hexString).length();
            int i = length + 1;
            int integer = hexString.substring(length, i).toInteger();
            int i2 = 1;
            if (integer >= 128) {
                i2 = 1 + (integer & 15);
                integer = hexString.substring(i, length + i2).toInteger();
            }
            int i3 = length + i2 + integer;
            return new HexString[]{hexString.substring(0, i3), hexString.substring(i3)};
        } catch (StringIndexOutOfBoundsException unused) {
            throw new InvalidAsn1CodingException("Invalid BER coding. Data: " + hexString);
        }
    }

    static HexString getDerLength(int i) {
        HexString hexStringFromInteger = HexString.fromInteger(i);
        return (hexStringFromInteger.length() > 1 || (hexStringFromInteger.at(0) & TransitionInfo.INIT) > 127) ? HexString.fromInteger(hexStringFromInteger.length() + DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY).append(hexStringFromInteger) : hexStringFromInteger;
    }

    static HexString getTag(HexString hexString) {
        if ((hexString.at(0) & 31) != 31) {
            return hexString.substring(0, 1);
        }
        int i = 1;
        while ((hexString.at(i) & 128) == 128 && i < hexString.length()) {
            i++;
        }
        return hexString.substring(0, i + 1);
    }
}
