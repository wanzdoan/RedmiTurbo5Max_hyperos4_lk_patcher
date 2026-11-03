package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Asn1Sequence extends Asn1TaggedContainer {
    public static final HexString TAG = HexString.fromInteger(48);

    public static Asn1Sequence parse(HexString hexString, boolean z) {
        HexString[] hexStringArrUnwrap = Asn1Structure.unwrap(TAG, hexString, z);
        if (!hexStringArrUnwrap[1].isEmpty()) {
            throw new InvalidAsn1CodingException("Unexpected Data at the end of the sequence");
        }
        ArrayList arrayList = new ArrayList();
        hexStringArrUnwrap[1] = hexStringArrUnwrap[0];
        while (!hexStringArrUnwrap[1].isEmpty()) {
            hexStringArrUnwrap = Asn1Structure.removeTlv(hexStringArrUnwrap[1]);
            arrayList.add(Asn1Parser.parse(hexStringArrUnwrap[0]));
        }
        return new Asn1Sequence(arrayList);
    }

    public Asn1Sequence(List list) {
        super(TAG, list);
    }

    public Asn1Sequence() {
        super(TAG);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1TaggedContainer, com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        String strRepeat = StringUtil.repeat(' ', i);
        StringBuilder sb = new StringBuilder();
        sb.append(strRepeat);
        sb.append("SEQUENCE (len=" + getValue().length() + ") [");
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

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1TaggedContainer
    /* JADX INFO: renamed from: clone */
    public Asn1Sequence mo4792clone() {
        ArrayList arrayList = new ArrayList(this.members.size());
        Iterator it = this.members.iterator();
        while (it.hasNext()) {
            arrayList.add(((Asn1Encodable) it.next()).mo4792clone());
        }
        return new Asn1Sequence(arrayList);
    }
}
