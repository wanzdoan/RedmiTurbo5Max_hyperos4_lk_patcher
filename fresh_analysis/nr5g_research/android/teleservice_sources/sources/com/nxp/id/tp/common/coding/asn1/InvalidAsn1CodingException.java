package com.nxp.id.tp.common.coding.asn1;

/* JADX INFO: loaded from: classes.dex */
public final class InvalidAsn1CodingException extends RuntimeException {
    private static final long serialVersionUID = -1329119267973349474L;

    public InvalidAsn1CodingException(String str) {
        super(str);
    }

    public InvalidAsn1CodingException(String str, Throwable th) {
        super(str, th);
    }

    public InvalidAsn1CodingException(Throwable th) {
        super(th);
    }
}
