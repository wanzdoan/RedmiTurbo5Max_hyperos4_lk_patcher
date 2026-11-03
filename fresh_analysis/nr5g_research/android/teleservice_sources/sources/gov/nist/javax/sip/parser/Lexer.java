package gov.nist.javax.sip.parser;

import gov.nist.core.LexerCore;
import gov.nist.javax.sip.address.GenericURI;
import java.util.Hashtable;

/* JADX INFO: loaded from: classes2.dex */
public class Lexer extends LexerCore {
    public Lexer(String str, String str2) {
        super(str, str2);
        selectLexer(str);
    }

    public void selectLexer(String str) {
        Hashtable hashtable = LexerCore.lexerTables;
        synchronized (hashtable) {
            try {
                Hashtable hashtable2 = (Hashtable) hashtable.get(str);
                this.currentLexer = hashtable2;
                this.currentLexerName = str;
                if (hashtable2 == null) {
                    addLexer(str);
                    if (str.equals("method_keywordLexer")) {
                        addKeyword("REGISTER", 2052);
                        addKeyword("ACK", 2054);
                        addKeyword("OPTIONS", 2056);
                        addKeyword("BYE", 2055);
                        addKeyword("INVITE", 2053);
                        addKeyword(GenericURI.SIP.toUpperCase(), 2051);
                        addKeyword(GenericURI.SIPS.toUpperCase(), 2136);
                        addKeyword("SUBSCRIBE", 2101);
                        addKeyword("NOTIFY", 2102);
                        addKeyword("MESSAGE", 2118);
                        addKeyword("PUBLISH", 2115);
                    } else if (str.equals("command_keywordLexer")) {
                        addKeyword("Error-Info".toUpperCase(), 2058);
                        addKeyword("Allow-Events".toUpperCase(), 2113);
                        addKeyword("Authentication-Info".toUpperCase(), 2112);
                        addKeyword("Event".toUpperCase(), 2111);
                        addKeyword("Min-Expires".toUpperCase(), 2110);
                        addKeyword("RSeq".toUpperCase(), 2108);
                        addKeyword("RAck".toUpperCase(), 2109);
                        addKeyword("Reason".toUpperCase(), 2107);
                        addKeyword("Reply-To".toUpperCase(), 2106);
                        addKeyword("Subscription-State".toUpperCase(), 2104);
                        addKeyword("Timestamp".toUpperCase(), 2103);
                        addKeyword("In-Reply-To".toUpperCase(), 2059);
                        addKeyword("MIME-Version".toUpperCase(), 2060);
                        addKeyword("Alert-Info".toUpperCase(), 2061);
                        addKeyword("From".toUpperCase(), 2062);
                        addKeyword("To".toUpperCase(), 2063);
                        addKeyword("Refer-To".toUpperCase(), 2114);
                        addKeyword("Via".toUpperCase(), 2064);
                        addKeyword("User-Agent".toUpperCase(), 2065);
                        addKeyword("Server".toUpperCase(), 2066);
                        addKeyword("Accept-Encoding".toUpperCase(), 2067);
                        addKeyword("Accept".toUpperCase(), 2068);
                        addKeyword("Allow".toUpperCase(), 2069);
                        addKeyword("Route".toUpperCase(), 2070);
                        addKeyword("Authorization".toUpperCase(), 2071);
                        addKeyword("Proxy-Authorization".toUpperCase(), 2072);
                        addKeyword("Retry-After".toUpperCase(), 2073);
                        addKeyword("Proxy-Require".toUpperCase(), 2074);
                        addKeyword("Content-Language".toUpperCase(), 2075);
                        addKeyword("Unsupported".toUpperCase(), 2076);
                        addKeyword("Supported".toUpperCase(), 2068);
                        addKeyword("Warning".toUpperCase(), 2078);
                        addKeyword("Max-Forwards".toUpperCase(), 2079);
                        addKeyword("Date".toUpperCase(), 2080);
                        addKeyword("Priority".toUpperCase(), 2081);
                        addKeyword("Proxy-Authenticate".toUpperCase(), 2082);
                        addKeyword("Content-Encoding".toUpperCase(), 2083);
                        addKeyword("Content-Length".toUpperCase(), 2084);
                        addKeyword("Subject".toUpperCase(), 2085);
                        addKeyword("Content-Type".toUpperCase(), 2086);
                        addKeyword("Contact".toUpperCase(), 2087);
                        addKeyword("Call-ID".toUpperCase(), 2088);
                        addKeyword("Require".toUpperCase(), 2089);
                        addKeyword("Expires".toUpperCase(), 2090);
                        addKeyword("Record-Route".toUpperCase(), 2092);
                        addKeyword("Organization".toUpperCase(), 2093);
                        addKeyword("CSeq".toUpperCase(), 2094);
                        addKeyword("Accept-Language".toUpperCase(), 2095);
                        addKeyword("WWW-Authenticate".toUpperCase(), 2096);
                        addKeyword("Call-Info".toUpperCase(), 2099);
                        addKeyword("Content-Disposition".toUpperCase(), 2100);
                        addKeyword("K".toUpperCase(), 2068);
                        addKeyword("C".toUpperCase(), 2086);
                        addKeyword("E".toUpperCase(), 2083);
                        addKeyword("F".toUpperCase(), 2062);
                        addKeyword("I".toUpperCase(), 2088);
                        addKeyword("M".toUpperCase(), 2087);
                        addKeyword("L".toUpperCase(), 2084);
                        addKeyword("S".toUpperCase(), 2085);
                        addKeyword("T".toUpperCase(), 2063);
                        addKeyword("U".toUpperCase(), 2113);
                        addKeyword("V".toUpperCase(), 2064);
                        addKeyword("R".toUpperCase(), 2114);
                        addKeyword("O".toUpperCase(), 2111);
                        addKeyword("X".toUpperCase(), 2133);
                        addKeyword("SIP-ETag".toUpperCase(), 2116);
                        addKeyword("SIP-If-Match".toUpperCase(), 2117);
                        addKeyword("Session-Expires".toUpperCase(), 2133);
                        addKeyword("Min-SE".toUpperCase(), 2134);
                        addKeyword("Referred-By".toUpperCase(), 2132);
                        addKeyword("Replaces".toUpperCase(), 2135);
                        addKeyword("Join".toUpperCase(), 2140);
                        addKeyword("Path".toUpperCase(), 2119);
                        addKeyword("Service-Route".toUpperCase(), 2120);
                        addKeyword("P-Asserted-Identity".toUpperCase(), 2121);
                        addKeyword("P-Preferred-Identity".toUpperCase(), 2122);
                        addKeyword("Privacy".toUpperCase(), 2126);
                        addKeyword("P-Called-Party-ID".toUpperCase(), 2128);
                        addKeyword("P-Associated-URI".toUpperCase(), 2129);
                        addKeyword("P-Visited-Network-ID".toUpperCase(), 2123);
                        addKeyword("P-Charging-Function-Addresses".toUpperCase(), 2124);
                        addKeyword("P-Charging-Vector".toUpperCase(), 2125);
                        addKeyword("P-Access-Network-Info".toUpperCase(), 2127);
                        addKeyword("P-Media-Authorization".toUpperCase(), 2130);
                        addKeyword("Security-Server".toUpperCase(), 2137);
                        addKeyword("Security-Verify".toUpperCase(), 2139);
                        addKeyword("Security-Client".toUpperCase(), 2138);
                        addKeyword("P-User-Database".toUpperCase(), 2141);
                        addKeyword("P-Profile-Key".toUpperCase(), 2142);
                        addKeyword("P-Served-User".toUpperCase(), 2143);
                        addKeyword("P-Preferred-Service".toUpperCase(), 2144);
                        addKeyword("P-Asserted-Service".toUpperCase(), 2145);
                        addKeyword("References".toUpperCase(), 2146);
                    } else if (str.equals("status_lineLexer") || str.equals("request_lineLexer")) {
                        addKeyword(GenericURI.SIP.toUpperCase(), 2051);
                    } else if (str.equals("sip_urlLexer")) {
                        addKeyword(GenericURI.TEL.toUpperCase(), 2105);
                        addKeyword(GenericURI.SIP.toUpperCase(), 2051);
                        addKeyword(GenericURI.SIPS.toUpperCase(), 2136);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
