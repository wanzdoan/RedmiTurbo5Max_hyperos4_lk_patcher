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
public class JCOPControlImageData implements Asn1Encodable {
    private final Constrains constrains;
    private boolean errInjectSkipUpdateControlData;
    private final UpdateControlData updateControlData;
    private final int version;

    private JCOPControlImageData(int i, UpdateControlData updateControlData, Constrains constrains, boolean z) {
        this.errInjectSkipUpdateControlData = false;
        this.version = i;
        this.updateControlData = updateControlData.m4775clone();
        if (constrains != null) {
            this.constrains = constrains.m4775clone();
        } else {
            this.constrains = null;
        }
        this.errInjectSkipUpdateControlData = z;
    }

    public JCOPControlImageData(int i, UpdateControlData updateControlData, Constrains constrains) {
        this.errInjectSkipUpdateControlData = false;
        Utils.checkParamNotNull(updateControlData);
        this.version = i;
        this.updateControlData = updateControlData.m4775clone();
        if (constrains == null) {
            this.constrains = null;
        } else {
            this.constrains = constrains.m4775clone();
        }
    }

    public static JCOPControlImageData decodeFromDER(Asn1Encodable asn1Encodable) {
        Utils.checkParamNotNull(asn1Encodable);
        if (!(asn1Encodable instanceof Asn1Sequence)) {
            throw new UAIException(String.format("Element was expected to be %s but was %s", "Asn1Sequence", asn1Encodable.getClass()));
        }
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        String string = ((Asn1IA5String) asn1Sequence.get(0)).getString();
        if (!string.equals("JCI")) {
            DeliveryPackage$$ExternalSyntheticBUOutline0.m("JCOPControlImageData Tag did not match expected value. Expected JCI, but was ", string);
            return null;
        }
        return new JCOPControlImageData(((Asn1Integer) asn1Sequence.get(1)).getInteger(), UpdateControlData.decodeFromDER(asn1Sequence.get(2)), asn1Sequence.size() > 3 ? Constrains.decodeFromDER(asn1Sequence.get(3)) : null);
    }

    public String toString() {
        return toString(0);
    }

    public UpdateControlData getUpdateControlData() {
        return this.updateControlData;
    }

    public Asn1Sequence buildObject() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new Asn1IA5String("JCI"));
        linkedList.add(new Asn1Integer(this.version));
        if (!this.errInjectSkipUpdateControlData) {
            linkedList.add(this.updateControlData);
        }
        Constrains constrains = this.constrains;
        if (constrains != null) {
            linkedList.add(constrains);
        }
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
        String str;
        String strRepeat = StringUtil.repeat(' ', i);
        StringBuilder sb = new StringBuilder();
        sb.append(strRepeat);
        sb.append("JCOPControlImageData\n");
        sb.append(strRepeat);
        sb.append("  Tag JCI\n");
        sb.append(strRepeat);
        sb.append("  Version ");
        sb.append(this.version);
        sb.append("\n");
        int i2 = i + 2;
        sb.append(this.updateControlData.toString(i2));
        sb.append("\n");
        if (this.constrains == null) {
            str = "";
        } else {
            str = this.constrains.toString(i2) + "\n";
        }
        sb.append(str);
        sb.append(strRepeat);
        sb.append("End of JCOPControlImageData");
        return sb.toString();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public JCOPControlImageData m4771clone() {
        return new JCOPControlImageData(this.version, this.updateControlData, this.constrains, this.errInjectSkipUpdateControlData);
    }
}
