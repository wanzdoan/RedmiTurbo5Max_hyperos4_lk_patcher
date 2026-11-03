package com.nxp.id.tp.common.coding.asn1;

import com.nxp.id.tp.common.HexString;

/* JADX INFO: loaded from: classes.dex */
public abstract class Asn1String extends Asn1Structure {
    public abstract String getString();

    public Asn1String(HexString hexString, HexString hexString2) {
        super(hexString, hexString2);
    }
}
