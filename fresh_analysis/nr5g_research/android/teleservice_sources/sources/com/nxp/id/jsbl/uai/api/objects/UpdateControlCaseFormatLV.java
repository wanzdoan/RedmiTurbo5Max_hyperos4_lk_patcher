package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1OctetString;
import com.ot.pubsub.c.d$$ExternalSyntheticBUOutline0;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public class UpdateControlCaseFormatLV extends UpdateControlCase implements Asn1Encodable {
    public UpdateControlCaseFormatLV(List list, List list2, List list3, List list4, Integer num, HexString hexString) {
        super(list, list2, list3, list4, num, hexString);
    }

    public static UpdateControlCaseFormatLV decodeFromDER(Asn1Encodable asn1Encodable) throws UAIException {
        Utils.checkParamNotNull(asn1Encodable);
        if (!(asn1Encodable instanceof Asn1OctetString)) {
            throw new UAIException(String.format("Element was expected to be %s but was %s", "Asn1OctetString", asn1Encodable.getClass()));
        }
        byte[] byteArray = asn1Encodable.getValue().toByteArray();
        LinkedList linkedList = new LinkedList();
        int intListFromLVArray = readIntListFromLVArray(byteArray, 0, linkedList);
        LinkedList linkedList2 = new LinkedList();
        int intListFromLVArray2 = readIntListFromLVArray(byteArray, intListFromLVArray, linkedList2);
        LinkedList linkedList3 = new LinkedList();
        int intListFromLVArray3 = readIntListFromLVArray(byteArray, intListFromLVArray2, linkedList3);
        LinkedList linkedList4 = new LinkedList();
        int intListFromLVArray4 = readIntListFromLVArray(byteArray, intListFromLVArray3, linkedList4);
        int i = byteArray[intListFromLVArray4 + 1] & TransitionInfo.INIT;
        byte b = byteArray[intListFromLVArray4 + 2];
        HexString hexStringSubstring = HexString.fromByteArray(byteArray).substring(intListFromLVArray4 + 3);
        if (hexStringSubstring.length() != b) {
            throw new UAIException(String.format("Length of hash array does not match with length of data. Should be %d but is %d", Integer.valueOf(b), Integer.valueOf(hexStringSubstring.length())));
        }
        return new UpdateControlCaseFormatLV(linkedList, linkedList2, linkedList3, linkedList4, Integer.valueOf(i), hexStringSubstring);
    }

    private static int readIntListFromLVArray(byte[] bArr, int i, List list) {
        int i2 = i + 1;
        int i3 = bArr[i] & TransitionInfo.INIT;
        for (int i4 = 0; i4 < i3; i4 += 2) {
            int i5 = i2 + 1;
            int i6 = bArr[i2] << 8;
            i2 += 2;
            list.add(Integer.valueOf(((bArr[i5] & TransitionInfo.INIT) | i6) & 65535));
        }
        return i2;
    }

    private static HexString encodeHexStringToLVArray(HexString hexString) {
        if (hexString.length() > 255) {
            d$$ExternalSyntheticBUOutline0.m("Unable to encode to LV as length for a field is over one byte.");
            return null;
        }
        return hexString.prepend(HexString.fromByte((byte) hexString.length()));
    }

    private static HexString encodeIntListToTwoByteLVArray(List list) {
        HexString hexStringAppend = HexString.EMPTY;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            HexString hexStringFromInteger = HexString.fromInteger(((Integer) it.next()).intValue());
            if (hexStringFromInteger.length() < 2) {
                hexStringFromInteger = HexString.fromString("00").append(hexStringFromInteger);
            }
            hexStringAppend = hexStringAppend.append(hexStringFromInteger);
        }
        return encodeHexStringToLVArray(hexStringAppend);
    }

    private Asn1OctetString buildObject() {
        return new Asn1OctetString(HexString.EMPTY.append(encodeIntListToTwoByteLVArray(getOSID())).append(encodeIntListToTwoByteLVArray(getRSN())).append(encodeIntListToTwoByteLVArray(getCSN())).append(encodeIntListToTwoByteLVArray(getFSN())).append(encodeHexStringToLVArray(HexString.fromInteger(getActionIndex().intValue()))).append(encodeHexStringToLVArray(getHash())));
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return buildObject().encode();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public UpdateControlCaseFormatLV m4772clone() {
        return new UpdateControlCaseFormatLV(getOSID(), getRSN(), getCSN(), getFSN(), getActionIndex(), getHash());
    }
}
