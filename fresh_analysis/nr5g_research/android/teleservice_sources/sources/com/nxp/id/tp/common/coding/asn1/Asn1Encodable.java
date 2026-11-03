package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;

/* JADX INFO: loaded from: classes.dex */
public interface Asn1Encodable extends Cloneable {
    Asn1Encodable clone();

    HexString encode();

    HexString getValue();

    String toString(int i);
}
