package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1IA5String;
import com.nxp.id.tp.common.coding.asn1.Asn1Integer;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class DeliveryPackage implements Asn1Encodable {
    private final UpdateScripts imageBinaryData;
    private final JCOPControlImage jci;
    private final int version;

    public DeliveryPackage(int i, JCOPControlImage jCOPControlImage, UpdateScripts updateScripts) {
        this.version = i;
        this.jci = jCOPControlImage.m4775clone();
        this.imageBinaryData = updateScripts.m4775clone();
    }

    public static DeliveryPackage decodeFromDER(Asn1Encodable asn1Encodable) throws UAIException {
        Utils.checkParamNotNull(asn1Encodable);
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        String string = ((Asn1IA5String) asn1Sequence.get(0)).getString();
        if (!string.equals("DPO")) {
            DeliveryPackage$$ExternalSyntheticBUOutline0.m("DeliveryPackageObject Tag did not match expected value. Expected DPO, but was ", string);
            return null;
        }
        return new DeliveryPackage(((Asn1Integer) asn1Sequence.get(1)).getInteger(), JCOPControlImage.decodeFromDER(asn1Sequence.get(2)), UpdateScripts.decodeFromDER(asn1Sequence.get(3)));
    }

    public boolean equals(Object obj) {
        if (obj instanceof DeliveryPackage) {
            return encode().equals(((DeliveryPackage) obj).encode());
        }
        return false;
    }

    public String toString() {
        return toString(0);
    }

    public JCOPControlImage getJCOPControlImage() {
        return this.jci;
    }

    public UpdateScripts getUpdateScripts() {
        return this.imageBinaryData;
    }

    private Asn1Sequence buildObject() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(0, new Asn1IA5String("DPO"));
        linkedList.add(1, new Asn1Integer(this.version));
        linkedList.add(2, this.jci);
        linkedList.add(3, this.imageBinaryData);
        return new Asn1Sequence(linkedList);
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
        sb.append("DeliveryPackage\n");
        sb.append(strRepeat);
        sb.append("  Tag: DPO\n");
        sb.append(strRepeat);
        sb.append("  Version: ");
        sb.append(this.version);
        sb.append("\n");
        int i2 = i + 2;
        sb.append(this.jci.toString(i2));
        sb.append("\n");
        sb.append(this.imageBinaryData.toString(i2));
        sb.append("\n");
        sb.append(strRepeat);
        sb.append("End of DeliveryPackage");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public DeliveryPackage m4767clone() {
        return new DeliveryPackage(this.version, this.jci, this.imageBinaryData);
    }
}
