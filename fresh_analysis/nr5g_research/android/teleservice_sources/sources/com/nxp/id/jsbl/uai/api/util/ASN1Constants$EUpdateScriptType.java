package com.nxp.id.jsbl.uai.api.util;

/* JADX INFO: loaded from: classes.dex */
public enum ASN1Constants$EUpdateScriptType {
    IMAGE("image", 1),
    PREFLIGHT("preflight", 0),
    POSTFLIGHT("postflight", 2),
    IGNORE("ignore", 1),
    IMAGE_NO_RESET("image_no_reset", 3);

    private final int derEnumValue;
    private final String fileType;

    ASN1Constants$EUpdateScriptType(String str, int i) {
        this.fileType = str;
        this.derEnumValue = i;
    }

    public int getDerEnumValue() {
        return this.derEnumValue;
    }
}
