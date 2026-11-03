package gov.nist.javax.sip.parser;

import gov.nist.core.ParserCore;
import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.header.WWWAuthenticate;

/* JADX INFO: loaded from: classes2.dex */
public class WWWAuthenticateParser extends ChallengeParser {
    public WWWAuthenticateParser(String str) {
        super(str);
    }

    public SIPHeader parse() {
        if (ParserCore.debug) {
            dbg_enter("parse");
        }
        try {
            headerName(2096);
            WWWAuthenticate wWWAuthenticate = new WWWAuthenticate();
            super.parse(wWWAuthenticate);
            return wWWAuthenticate;
        } finally {
            if (ParserCore.debug) {
                dbg_leave("parse");
            }
        }
    }
}
