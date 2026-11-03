package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.Util;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class Asn1Container extends Asn1Structure implements Iterable {
    protected List members;

    public Asn1Container(HexString hexString, Asn1Encodable... asn1EncodableArr) {
        this(hexString, Util.wrapInList(asn1EncodableArr));
    }

    public Asn1Container(HexString hexString, List list) {
        super(hexString, HexString.EMPTY);
        this.members = list;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            this.value = this.value.append(((Asn1Encodable) it.next()).encode());
        }
    }

    public List getChildren() {
        return this.members;
    }

    public void add(Asn1Encodable asn1Encodable) {
        this.members.add(asn1Encodable);
        this.value = this.value.append(asn1Encodable.encode());
    }

    public Asn1Encodable get(int i) {
        return (Asn1Encodable) this.members.get(i);
    }

    public int size() {
        return this.members.size();
    }

    @Override // java.lang.Iterable
    public Iterator iterator() {
        return this.members.iterator();
    }
}
