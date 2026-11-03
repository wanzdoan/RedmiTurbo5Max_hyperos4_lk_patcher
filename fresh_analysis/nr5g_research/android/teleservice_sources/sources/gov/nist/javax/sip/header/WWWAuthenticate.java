package gov.nist.javax.sip.header;

import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class WWWAuthenticate extends AuthenticationHeader implements Cloneable, Serializable {
    private static final long serialVersionUID = 115378648697363486L;

    public WWWAuthenticate() {
        super("WWW-Authenticate");
    }
}
