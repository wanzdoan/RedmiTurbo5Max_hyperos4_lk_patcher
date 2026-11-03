package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Asn1TaggedContainer extends Asn1Container {
    public static Asn1TaggedContainer parse(HexString hexString) {
        HexString tag = Asn1Structure.getTag(hexString);
        HexString[] hexStringArrUnwrap = Asn1Structure.unwrap(hexString);
        if (!hexStringArrUnwrap[1].isEmpty()) {
            throw new InvalidAsn1CodingException("Unexpected Data at the end of the sequence");
        }
        ArrayList arrayList = new ArrayList();
        hexStringArrUnwrap[1] = hexStringArrUnwrap[0];
        while (!hexStringArrUnwrap[1].isEmpty()) {
            hexStringArrUnwrap = Asn1Structure.removeTlv(hexStringArrUnwrap[1]);
            arrayList.add(Asn1Parser.parse(hexStringArrUnwrap[0]));
        }
        return new Asn1TaggedContainer(tag, arrayList);
    }

    public Asn1TaggedContainer(HexString hexString, List list) {
        super(hexString, list);
    }

    public Asn1TaggedContainer(HexString hexString) {
        super(hexString, new Asn1Encodable[0]);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        String strRepeat = StringUtil.repeat(' ', i);
        StringBuilder sb = new StringBuilder();
        sb.append(strRepeat);
        sb.append("TAGGED CONTAINER (tag=");
        sb.append(getTag());
        sb.append("; len=" + getValue().length() + ") [");
        sb.append(Util.NEWLINE);
        Iterator it = this.members.iterator();
        while (it.hasNext()) {
            sb.append(((Asn1Encodable) it.next()).toString(i + 2));
            sb.append(Util.NEWLINE);
        }
        sb.append(strRepeat);
        sb.append("]");
        return sb.toString();
    }

    @Override // 
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Asn1TaggedContainer mo4792clone() {
        ArrayList arrayList = new ArrayList(this.members.size());
        Iterator it = this.members.iterator();
        while (it.hasNext()) {
            arrayList.add(((Asn1Encodable) it.next()).m4793clone());
        }
        return new Asn1TaggedContainer(getTag(), arrayList);
    }
}
