package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Boolean;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1IA5String;
import com.nxp.id.tp.common.coding.asn1.Asn1Integer;
import com.nxp.id.tp.common.coding.asn1.Asn1OctetString;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import com.nxp.id.tp.common.coding.asn1.Asn1String;
import com.nxp.id.tp.common.coding.asn1.Asn1TaggedContainer;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class Constrain implements Asn1Encodable {
    private String key;
    private Object value;

    private Constrain(String str, Object obj) {
        Utils.checkParamNotNull(obj);
        this.key = str;
        this.value = obj;
    }

    public static Constrain decodeFromDER(Asn1Encodable asn1Encodable) throws UAIException {
        Object value;
        Utils.checkParamNotNull(asn1Encodable);
        if (!(asn1Encodable instanceof Asn1TaggedContainer)) {
            throw new UAIException(String.format("Element was expected to be %s but was %s", "Asn1TaggedContainer", asn1Encodable.getClass()));
        }
        Asn1Sequence asn1Sequence = (Asn1Sequence) asn1Encodable;
        String string = ((Asn1String) asn1Sequence.get(0)).getString();
        Asn1Encodable asn1Encodable2 = asn1Sequence.get(1);
        if (asn1Encodable2 instanceof Asn1Integer) {
            value = Integer.valueOf(((Asn1Integer) asn1Encodable2).getInteger());
        } else if (asn1Encodable2 instanceof Asn1Boolean) {
            value = Boolean.valueOf(((Asn1Boolean) asn1Encodable2).isTrue());
        } else if (asn1Encodable2 instanceof Asn1OctetString) {
            value = asn1Encodable2.getValue();
        } else {
            throw new UAIException("Unsupported property type: specified: " + asn1Encodable2.getClass());
        }
        return new Constrain(string, value);
    }

    public String toString() {
        return toString(0);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        String string = this.value.toString();
        if (this.value instanceof Integer) {
            string = string + " (0x" + Utils.integerToHexString(((Integer) this.value).intValue()) + ")";
        }
        String strRepeat = StringUtil.repeat(' ', i);
        return ((("" + strRepeat + "Constrain\n") + strRepeat + "  Tag [" + this.key + "]\n") + strRepeat + "  Value [" + string + "]\n") + strRepeat + "End of Constrain";
    }

    private Asn1Sequence buildObject() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new Asn1IA5String(this.key));
        Object obj = this.value;
        if (obj instanceof Integer) {
            linkedList.add(new Asn1Integer(((Integer) this.value).intValue()));
        } else if (obj instanceof Boolean) {
            linkedList.add(new Asn1Boolean(((Boolean) this.value).booleanValue()));
        } else if (obj instanceof HexString) {
            linkedList.add(new Asn1OctetString((HexString) this.value));
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
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Constrain m4763clone() {
        return new Constrain(this.key, this.value);
    }
}
