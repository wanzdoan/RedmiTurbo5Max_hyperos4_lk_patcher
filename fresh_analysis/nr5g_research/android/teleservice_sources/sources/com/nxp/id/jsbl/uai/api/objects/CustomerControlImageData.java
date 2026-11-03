package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1IA5String;
import com.nxp.id.tp.common.coding.asn1.Asn1Integer;
import com.nxp.id.tp.common.coding.asn1.Asn1OctetString;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;

/* JADX INFO: loaded from: classes.dex */
public class CustomerControlImageData implements Asn1Encodable {
    private final HexString JCOPControlImageHash;
    private Constrains constrains;
    private int version;

    public CustomerControlImageData(int i, HexString hexString, Constrains constrains) {
        this.version = i;
        this.JCOPControlImageHash = hexString.m4777clone();
        if (constrains != null) {
            this.constrains = constrains.m4766clone();
        } else {
            this.constrains = null;
        }
    }

    public static CustomerControlImageData decodeFromDER(Asn1Encodable asn1Encodable) throws UAIException {
        Utils.checkParamNotNull(asn1Encodable);
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        String string = ((Asn1IA5String) asn1Sequence.get(0)).getString();
        if (!string.equals("CCI")) {
            DeliveryPackage$$ExternalSyntheticBUOutline0.m("CustomerControlImageData Tag did not match expected value. Expected CCI, but was ", string);
            return null;
        }
        return new CustomerControlImageData(((Asn1Integer) asn1Sequence.get(1)).getInteger(), asn1Sequence.get(2).getValue(), asn1Sequence.size() > 3 ? Constrains.decodeFromDER(asn1Sequence.get(3)) : null);
    }

    public String toString() {
        return toString(0);
    }

    private Asn1Sequence buildObject() {
        Asn1Sequence asn1Sequence = new Asn1Sequence();
        asn1Sequence.add(new Asn1IA5String("CCI"));
        asn1Sequence.add(new Asn1Integer(this.version));
        asn1Sequence.add(new Asn1OctetString(this.JCOPControlImageHash));
        Constrains constrains = this.constrains;
        if (constrains != null) {
            asn1Sequence.add(constrains);
        }
        return asn1Sequence;
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
    public String toString(int i) {
        String strRepeat = StringUtil.repeat(' ', i);
        StringBuilder sb = new StringBuilder();
        sb.append(strRepeat);
        sb.append("CustomerControlImageData\n");
        sb.append(strRepeat);
        sb.append("  Tag CCI\n");
        sb.append(strRepeat);
        sb.append("  Version ");
        sb.append(this.version);
        sb.append("\n");
        sb.append(strRepeat);
        sb.append("  JCOPControlImage hash [");
        sb.append(this.JCOPControlImageHash);
        sb.append("]\n");
        Constrains constrains = this.constrains;
        if (constrains != null) {
            sb.append(constrains.toString(i + 2));
            sb.append("\n");
        }
        sb.append(strRepeat);
        sb.append("End of CustomerControlImageData");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CustomerControlImageData m4766clone() {
        return new CustomerControlImageData(this.version, this.JCOPControlImageHash, this.constrains);
    }
}
