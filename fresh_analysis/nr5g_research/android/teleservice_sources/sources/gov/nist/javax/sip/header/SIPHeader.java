package gov.nist.javax.sip.header;

import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SIPHeader extends SIPObject implements Cloneable, Serializable {
    protected String headerName;

    protected abstract String encodeBody();

    protected SIPHeader(String str) {
        this.headerName = str;
    }

    @Override // gov.nist.core.GenericObject
    public String encode() {
        return encode(new StringBuffer()).toString();
    }

    @Override // gov.nist.core.GenericObject
    public StringBuffer encode(StringBuffer stringBuffer) {
        stringBuffer.append(this.headerName);
        stringBuffer.append(":");
        stringBuffer.append(" ");
        encodeBody(stringBuffer);
        stringBuffer.append("\r\n");
        return stringBuffer;
    }

    protected StringBuffer encodeBody(StringBuffer stringBuffer) {
        stringBuffer.append(encodeBody());
        return stringBuffer;
    }

    public int hashCode() {
        return this.headerName.hashCode();
    }

    public final String toString() {
        return encode();
    }
}
