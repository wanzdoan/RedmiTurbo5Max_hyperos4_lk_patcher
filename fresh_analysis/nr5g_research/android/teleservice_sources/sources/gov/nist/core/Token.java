package gov.nist.core;

/* JADX INFO: loaded from: classes2.dex */
public class Token {
    protected int tokenType;
    protected String tokenValue;

    public String getTokenValue() {
        return this.tokenValue;
    }

    public String toString() {
        return "tokenValue = " + this.tokenValue + "/tokenType = " + this.tokenType;
    }
}
