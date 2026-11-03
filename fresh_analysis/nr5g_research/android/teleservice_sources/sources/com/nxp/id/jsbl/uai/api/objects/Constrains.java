package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1IA5String;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import com.nxp.id.tp.common.coding.asn1.Asn1String;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Constrains implements Asn1Encodable {
    private String key;
    private final List properties;

    public Constrains(String str, List list) {
        Utils.checkParamNotNull(str);
        Utils.checkParamNotNull(list);
        this.key = str;
        this.properties = new LinkedList(list);
    }

    public static Constrains decodeFromDER(Asn1Encodable asn1Encodable) throws UAIException {
        Utils.checkParamNotNull(asn1Encodable);
        if (!(asn1Encodable instanceof Asn1Sequence)) {
            throw new UAIException(String.format("Element was expected to be %s but was %s", "Asn1Sequence", asn1Encodable.getClass()));
        }
        LinkedList linkedList = new LinkedList();
        Asn1Sequence<Asn1Encodable> asn1Sequence = (Asn1Sequence) asn1Encodable;
        boolean z = true;
        String string = null;
        for (Asn1Encodable asn1Encodable2 : asn1Sequence) {
            if (z) {
                z = false;
                string = ((Asn1String) asn1Sequence.get(0)).getString();
            } else {
                linkedList.add(Constrain.decodeFromDER(asn1Encodable2));
            }
        }
        return new Constrains(string, linkedList);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return buildObject().encode();
    }

    private Asn1Sequence buildObject() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new Asn1IA5String(getKey()));
        linkedList.addAll(getConstrainsList());
        return new Asn1Sequence(linkedList);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Constrains m4764clone() {
        return new Constrains(getKey(), getConstrainsList());
    }

    public List getConstrainsList() {
        return this.properties;
    }

    public String toString() {
        return toString(0);
    }

    public String getKey() {
        return this.key;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        String strRepeat = StringUtil.repeat(' ', i);
        StringBuilder sb = new StringBuilder();
        sb.append(strRepeat);
        sb.append("Constrains\n");
        sb.append(strRepeat);
        sb.append("  Tag [");
        sb.append(this.key);
        sb.append("]\n");
        Iterator it = this.properties.iterator();
        while (it.hasNext()) {
            sb.append(((Constrain) it.next()).toString(i + 2));
            sb.append("\n");
        }
        sb.append(strRepeat);
        sb.append("End of Constrains");
        return sb.toString();
    }
}
