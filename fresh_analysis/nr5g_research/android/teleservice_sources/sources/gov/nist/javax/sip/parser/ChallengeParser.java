package gov.nist.javax.sip.parser;

import gov.nist.core.ParserCore;
import gov.nist.core.Token;
import gov.nist.javax.sip.header.AuthenticationHeader;
import java.text.ParseException;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ChallengeParser extends HeaderParser {
    protected ChallengeParser(String str) {
        super(str);
    }

    protected void parseParameter(AuthenticationHeader authenticationHeader) {
        if (ParserCore.debug) {
            dbg_enter("parseParameter");
        }
        try {
            authenticationHeader.setParameter(nameValue('='));
        } finally {
            if (ParserCore.debug) {
                dbg_leave("parseParameter");
            }
        }
    }

    public void parse(AuthenticationHeader authenticationHeader) throws ParseException {
        this.lexer.SPorHT();
        this.lexer.match(4095);
        Token nextToken = this.lexer.getNextToken();
        this.lexer.SPorHT();
        authenticationHeader.setScheme(nextToken.getTokenValue());
        while (this.lexer.lookAhead(0) != '\n') {
            parseParameter(authenticationHeader);
            this.lexer.SPorHT();
            char cLookAhead = this.lexer.lookAhead(0);
            if (cLookAhead == '\n' || cLookAhead == 0) {
                return;
            }
            this.lexer.match(44);
            this.lexer.SPorHT();
        }
    }
}
