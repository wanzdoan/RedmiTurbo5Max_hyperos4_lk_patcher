package gov.nist.javax.sip.header;

import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class Authorization extends AuthenticationHeader implements Cloneable, Serializable {
    private static final long serialVersionUID = -8897770321892281348L;

    public Authorization() {
        super("Authorization");
    }
}
