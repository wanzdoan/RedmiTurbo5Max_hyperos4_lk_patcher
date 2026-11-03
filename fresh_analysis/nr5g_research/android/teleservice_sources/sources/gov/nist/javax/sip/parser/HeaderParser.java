package gov.nist.javax.sip.parser;

import java.text.ParseException;

/* JADX INFO: loaded from: classes2.dex */
public abstract class HeaderParser extends Parser {
    protected HeaderParser(String str) {
        this.lexer = new Lexer("command_keywordLexer", str);
    }

    protected void headerName(int i) throws ParseException {
        this.lexer.match(i);
        this.lexer.SPorHT();
        this.lexer.match(58);
        this.lexer.SPorHT();
    }
}
