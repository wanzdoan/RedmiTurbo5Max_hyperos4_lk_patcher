package gov.nist.javax.sip.header;

import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;
import gov.nist.core.NameValue;
import java.text.ParseException;
import javax.sip.address.URI;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AuthenticationHeader extends ParametersHeader {
    protected String scheme;

    public AuthenticationHeader(String str) {
        super(str);
        this.parameters.setSeparator(",");
        this.scheme = "Digest";
    }

    public void setParameter(String str, String str2) throws ParseException {
        NameValue nameValue = this.parameters.getNameValue(str.toLowerCase());
        if (nameValue == null) {
            NameValue nameValue2 = new NameValue(str, str2);
            if (str.equalsIgnoreCase("qop") || str.equalsIgnoreCase("realm") || str.equalsIgnoreCase("cnonce") || str.equalsIgnoreCase("nonce") || str.equalsIgnoreCase("username") || str.equalsIgnoreCase("domain") || str.equalsIgnoreCase("opaque") || str.equalsIgnoreCase("nextnonce") || str.equalsIgnoreCase("uri") || str.equalsIgnoreCase("response") || str.equalsIgnoreCase("ik") || str.equalsIgnoreCase("ck") || str.equalsIgnoreCase("integrity-protected")) {
                if (!(this instanceof Authorization) || !str.equalsIgnoreCase("qop")) {
                    nameValue2.setQuotedValue();
                }
                if (str2 == null) {
                    GenericObject$$ExternalSyntheticBUOutline0.m("null value");
                    return;
                } else if (str2.startsWith("\"")) {
                    throw new ParseException(str2.concat(" : Unexpected DOUBLE_QUOTE"), 0);
                }
            }
            super.setParameter(nameValue2);
            return;
        }
        nameValue.setValueAsObject(str2);
    }

    @Override // gov.nist.javax.sip.header.SIPHeader
    public String encodeBody() {
        this.parameters.setSeparator(",");
        return this.scheme + " " + this.parameters.encode();
    }

    public void setScheme(String str) {
        this.scheme = str;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setRealm(String str) throws ParseException {
        if (str == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("JAIN-SIP Exception,  AuthenticationHeader, setRealm(), The realm parameter is null");
        } else {
            setParameter("realm", str);
        }
    }

    public String getRealm() {
        return getParameter("realm");
    }

    public void setNonce(String str) throws ParseException {
        if (str == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("JAIN-SIP Exception,  AuthenticationHeader, setNonce(), The nonce parameter is null");
        } else {
            setParameter("nonce", str);
        }
    }

    public String getNonce() {
        return getParameter("nonce");
    }

    public void setURI(URI uri) {
        if (uri != null) {
            NameValue nameValue = new NameValue("uri", uri);
            nameValue.setQuotedValue();
            this.parameters.set(nameValue);
            return;
        }
        GenericObject$$ExternalSyntheticBUOutline0.m("Null URI");
    }

    public void setAlgorithm(String str) throws ParseException {
        if (str == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("null arg");
        } else {
            setParameter("algorithm", str);
        }
    }

    public String getAlgorithm() {
        return getParameter("algorithm");
    }

    public void setQop(String str) throws ParseException {
        if (str == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("null arg");
        } else {
            setParameter("qop", str);
        }
    }

    public String getQop() {
        return getParameter("qop");
    }

    public void setOpaque(String str) throws ParseException {
        if (str == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("null arg");
        } else {
            setParameter("opaque", str);
        }
    }

    public String getOpaque() {
        return getParameter("opaque");
    }

    public void setCNonce(String str) throws ParseException {
        setParameter("cnonce", str);
    }

    public void setNonceCount(int i) throws ParseException {
        if (i < 0) {
            throw new ParseException("bad value", 0);
        }
        String hexString = Integer.toHexString(i);
        setParameter("nc", "00000000".substring(0, 8 - hexString.length()).concat(hexString));
    }

    public void setResponse(String str) throws ParseException {
        if (str == null) {
            GenericObject$$ExternalSyntheticBUOutline0.m("Null parameter");
        } else {
            setParameter("response", str);
        }
    }

    public void setUsername(String str) throws ParseException {
        setParameter("username", str);
    }
}
