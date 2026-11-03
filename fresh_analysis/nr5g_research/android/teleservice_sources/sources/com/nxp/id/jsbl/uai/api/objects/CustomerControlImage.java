package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.APDUUAI;
import com.nxp.id.jsbl.uai.api.util.JSBLExceptionExternal;
import com.nxp.id.jsbl.uai.api.util.UAIAPIException;
import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1OctetString;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CustomerControlImage implements Asn1Encodable {
    private Integer corruptedLen;
    private boolean injectSignature;
    private HexString privateKey;
    private HexString signature;
    private final CustomerControlImageData signedSection;
    private boolean skipSignature;

    private CustomerControlImage(CustomerControlImageData customerControlImageData, HexString hexString, HexString hexString2, boolean z, Integer num, boolean z2) {
        this.skipSignature = false;
        this.corruptedLen = null;
        this.injectSignature = false;
        this.signedSection = customerControlImageData.m4766clone();
        if (hexString != null) {
            this.privateKey = hexString.m4777clone();
        }
        if (hexString2 != null) {
            this.signature = hexString2.m4777clone();
        }
        this.skipSignature = z;
        this.corruptedLen = num;
        this.injectSignature = z2;
    }

    public CustomerControlImage(CustomerControlImageData customerControlImageData, HexString hexString, HexString hexString2) throws UAIException {
        this.skipSignature = false;
        this.corruptedLen = null;
        this.injectSignature = false;
        Utils.checkParamNotNull(customerControlImageData);
        if ((hexString == null && hexString2 == null) || (hexString != null && hexString2 != null)) {
            throw new UAIException("Exactly one of privateKey and signedHash has to be defined!");
        }
        this.signedSection = customerControlImageData.m4766clone();
        this.signature = hexString2.m4777clone();
    }

    public static CustomerControlImage decodeFromDER(Asn1Encodable asn1Encodable) {
        Utils.checkParamNotNull(asn1Encodable);
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        return new CustomerControlImage(CustomerControlImageData.decodeFromDER(asn1Sequence.get(0)), null, asn1Sequence.get(1).getValue());
    }

    public List toApdu(boolean z) throws UAIAPIException {
        try {
            return toApdu(APDUUAI.outputSyntax.APDU, z);
        } catch (JSBLExceptionExternal e) {
            throw new UAIAPIException(e.getMessage(), e);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof CustomerControlImage) {
            return encode().equals(((CustomerControlImage) obj).encode());
        }
        return false;
    }

    public String toString() {
        return toString(0);
    }

    public List toApdu(APDUUAI.outputSyntax outputsyntax, boolean z) {
        return APDUUAI.byteArrayToUAIAPDU(encode().toByteArray(), outputsyntax, (byte) -86, z);
    }

    private Asn1Sequence buildObject() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(this.signedSection);
        if (!this.skipSignature) {
            linkedList.add(new Asn1OctetString(this.signature));
        }
        return new Asn1Sequence(linkedList);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        HexString hexStringEncode = buildObject().encode();
        Integer num = this.corruptedLen;
        return num != null ? hexStringEncode.replaceAt(1, HexString.fromByte(num.byteValue())) : hexStringEncode;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        String strRepeat = StringUtil.repeat(' ', i);
        return strRepeat + "CustomerControlImage\n" + this.signedSection.toString(i + 2) + "\n" + strRepeat + "  Signature [" + this.signature + "]\n" + strRepeat + "End of CustomerControlImage";
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CustomerControlImage m4765clone() {
        return new CustomerControlImage(this.signedSection, this.privateKey, this.signature, this.skipSignature, this.corruptedLen, this.injectSignature);
    }
}
