package gov.nist.core;

import java.text.ParseException;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ParserCore {
    public static final boolean debug = Debug.parserDebug;
    static int nesting_level;
    protected LexerCore lexer;

    protected NameValue nameValue(char c) {
        boolean z;
        boolean z2 = debug;
        if (z2) {
            dbg_enter("nameValue");
        }
        try {
            this.lexer.match(4095);
            Token nextToken = this.lexer.getNextToken();
            this.lexer.SPorHT();
            try {
                String strQuotedString = "";
                boolean z3 = true;
                if (this.lexer.lookAhead(0) == c) {
                    this.lexer.consume(1);
                    this.lexer.SPorHT();
                    char cLookAhead = this.lexer.lookAhead(0);
                    LexerCore lexerCore = this.lexer;
                    if (cLookAhead == '\"') {
                        strQuotedString = lexerCore.quotedString();
                        z = true;
                        z3 = false;
                    } else {
                        lexerCore.match(4095);
                        String str = this.lexer.getNextToken().tokenValue;
                        if (str == null) {
                            z = false;
                        } else {
                            strQuotedString = str;
                            z = false;
                            z3 = false;
                        }
                    }
                    NameValue nameValue = new NameValue(nextToken.tokenValue, strQuotedString, z3);
                    if (z) {
                        nameValue.setQuotedValue();
                    }
                    if (z2) {
                        dbg_leave("nameValue");
                    }
                    return nameValue;
                }
                NameValue nameValue2 = new NameValue(nextToken.tokenValue, "", true);
                if (z2) {
                    dbg_leave("nameValue");
                }
                return nameValue2;
            } catch (ParseException unused) {
                NameValue nameValue3 = new NameValue(nextToken.tokenValue, null, false);
                if (debug) {
                    dbg_leave("nameValue");
                }
                return nameValue3;
            }
        } catch (Throwable th) {
            if (debug) {
                dbg_leave("nameValue");
            }
            throw th;
        }
    }

    protected void dbg_enter(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < nesting_level; i++) {
            stringBuffer.append(">");
        }
        if (debug) {
            System.out.println(((Object) stringBuffer) + str + "\nlexer buffer = \n" + this.lexer.getRest());
        }
        nesting_level++;
    }

    protected void dbg_leave(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < nesting_level; i++) {
            stringBuffer.append("<");
        }
        if (debug) {
            System.out.println(((Object) stringBuffer) + str + "\nlexer buffer = \n" + this.lexer.getRest());
        }
        nesting_level--;
    }
}
