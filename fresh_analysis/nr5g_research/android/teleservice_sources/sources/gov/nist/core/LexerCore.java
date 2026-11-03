package gov.nist.core;

import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHookBase;
import java.text.ParseException;
import java.util.Hashtable;
import miuix.preference.flexible.AbstractBaseTemplate;

/* JADX INFO: loaded from: classes2.dex */
public abstract class LexerCore extends StringTokenizer {
    protected static final Hashtable globalSymbolTable = new Hashtable();
    protected static final Hashtable lexerTables = new Hashtable();
    protected Hashtable currentLexer;
    protected String currentLexerName;
    protected Token currentMatch;

    protected void addKeyword(String str, int i) {
        Integer numValueOf = Integer.valueOf(i);
        this.currentLexer.put(str, numValueOf);
        Hashtable hashtable = globalSymbolTable;
        if (hashtable.containsKey(numValueOf)) {
            return;
        }
        hashtable.put(numValueOf, str);
    }

    protected Hashtable addLexer(String str) {
        Hashtable hashtable = lexerTables;
        Hashtable hashtable2 = (Hashtable) hashtable.get(str);
        this.currentLexer = hashtable2;
        if (hashtable2 == null) {
            Hashtable hashtable3 = new Hashtable();
            this.currentLexer = hashtable3;
            hashtable.put(str, hashtable3);
        }
        return this.currentLexer;
    }

    public LexerCore(String str, String str2) {
        super(str2);
        this.currentLexerName = str;
    }

    public String getNextId() {
        return ttoken();
    }

    public Token getNextToken() {
        return this.currentMatch;
    }

    public Token match(int i) throws ParseException {
        if (Debug.parserDebug) {
            Debug.println("match " + i);
        }
        if (i <= 2048 || i >= 4096) {
            if (i > 4096) {
                char cLookAhead = lookAhead(0);
                if (i == 4098) {
                    if (!StringTokenizer.isDigit(cLookAhead)) {
                        throw new ParseException(this.buffer + "\nExpecting DIGIT", this.ptr);
                    }
                    Token token = new Token();
                    this.currentMatch = token;
                    token.tokenValue = String.valueOf(cLookAhead);
                    this.currentMatch.tokenType = i;
                    consume(1);
                } else if (i == 4099) {
                    if (!StringTokenizer.isAlpha(cLookAhead)) {
                        throw new ParseException(this.buffer + "\nExpecting ALPHA", this.ptr);
                    }
                    Token token2 = new Token();
                    this.currentMatch = token2;
                    token2.tokenValue = String.valueOf(cLookAhead);
                    this.currentMatch.tokenType = i;
                    consume(1);
                }
            } else {
                char c = (char) i;
                char cLookAhead2 = lookAhead(0);
                if (cLookAhead2 == c) {
                    consume(1);
                } else {
                    throw new ParseException(this.buffer + "\nExpecting  >>>" + c + "<<< got >>>" + cLookAhead2 + "<<<", this.ptr);
                }
            }
        } else if (i == 4095) {
            if (!startsId()) {
                throw new ParseException(this.buffer + "\nID expected", this.ptr);
            }
            String nextId = getNextId();
            Token token3 = new Token();
            this.currentMatch = token3;
            token3.tokenValue = nextId;
            token3.tokenType = 4095;
        } else if (i == 4094) {
            if (!startsSafeToken()) {
                throw new ParseException(this.buffer + "\nID expected", this.ptr);
            }
            String strTtokenSafe = ttokenSafe();
            Token token4 = new Token();
            this.currentMatch = token4;
            token4.tokenValue = strTtokenSafe;
            token4.tokenType = 4094;
        } else {
            String nextId2 = getNextId();
            Integer num = (Integer) this.currentLexer.get(nextId2.toUpperCase());
            if (num == null || num.intValue() != i) {
                throw new ParseException(this.buffer + "\nUnexpected Token : " + nextId2, this.ptr);
            }
            Token token5 = new Token();
            this.currentMatch = token5;
            token5.tokenValue = nextId2;
            token5.tokenType = i;
        }
        return this.currentMatch;
    }

    public void SPorHT() {
        try {
            char cLookAhead = lookAhead(0);
            while (true) {
                if (cLookAhead != ' ' && cLookAhead != '\t') {
                    return;
                }
                consume(1);
                cLookAhead = lookAhead(0);
            }
        } catch (ParseException unused) {
        }
    }

    public static final boolean isTokenChar(char c) {
        return StringTokenizer.isAlphaDigit(c) || c == '!' || c == '%' || c == '\'' || c == '~' || c == '*' || c == '+' || c == '-' || c == '.' || c == '_' || c == '`';
    }

    public boolean startsId() {
        try {
            return isTokenChar(lookAhead(0));
        } catch (ParseException unused) {
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:30:0x003e A[FALL_THROUGH, RETURN] */
    public boolean startsSafeToken() {
        try {
            char cLookAhead = lookAhead(0);
            if (!StringTokenizer.isAlphaDigit(cLookAhead) && cLookAhead != '\'' && cLookAhead != '=' && cLookAhead != '[' && cLookAhead != '*' && cLookAhead != '+' && cLookAhead != ':' && cLookAhead != ';' && cLookAhead != '?' && cLookAhead != '@') {
                switch (cLookAhead) {
                    default:
                        switch (cLookAhead) {
                            default:
                                switch (cLookAhead) {
                                    default:
                                        switch (cLookAhead) {
                                            case '{':
                                            case '|':
                                            case '}':
                                            case '~':
                                                break;
                                            default:
                                                return false;
                                        }
                                    case ']':
                                    case '^':
                                    case '_':
                                    case '`':
                                        return true;
                                }
                            case AbstractBaseTemplate.BIG_ICON_WIDTH_THRESHOLD_DP /* 45 */:
                            case '.':
                            case ModemServiceMoniter.EVENT_OEMHOOK_CALL_STATUS_NOTIFY /* 47 */:
                                return true;
                        }
                    case '!':
                    case '\"':
                    case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                    case '$':
                    case '%':
                        return true;
                }
            }
            return true;
        } catch (ParseException unused) {
            return false;
        }
    }

    public String ttoken() {
        int i = this.ptr;
        while (hasMoreChars() && isTokenChar(lookAhead(0))) {
            try {
                consume(1);
            } catch (ParseException unused) {
                return null;
            }
        }
        return this.buffer.substring(i, this.ptr);
    }

    /* JADX WARN: Code duplicated, block: B:29:0x0045 A[Catch: ParseException -> 0x0052, FALL_THROUGH, TryCatch #0 {ParseException -> 0x0052, blocks: (B:3:0x0002, B:5:0x0008, B:7:0x0014, B:24:0x0038, B:25:0x003b, B:26:0x003e, B:27:0x0041, B:29:0x0045, B:30:0x0049), top: B:34:0x0002 }] */
    public String ttokenSafe() {
        int i = this.ptr;
        while (hasMoreChars()) {
            try {
                char cLookAhead = lookAhead(0);
                if (StringTokenizer.isAlphaDigit(cLookAhead)) {
                    consume(1);
                } else {
                    if (cLookAhead != '\'' && cLookAhead != '[' && cLookAhead != '*' && cLookAhead != '+' && cLookAhead != ':' && cLookAhead != ';' && cLookAhead != '?' && cLookAhead != '@') {
                        switch (cLookAhead) {
                            default:
                                switch (cLookAhead) {
                                    default:
                                        switch (cLookAhead) {
                                            default:
                                                switch (cLookAhead) {
                                                    case '{':
                                                    case '|':
                                                    case '}':
                                                    case '~':
                                                        break;
                                                    default:
                                                        return this.buffer.substring(i, this.ptr);
                                                }
                                            case ']':
                                            case '^':
                                            case '_':
                                            case '`':
                                                consume(1);
                                                break;
                                        }
                                    case AbstractBaseTemplate.BIG_ICON_WIDTH_THRESHOLD_DP /* 45 */:
                                    case '.':
                                    case ModemServiceMoniter.EVENT_OEMHOOK_CALL_STATUS_NOTIFY /* 47 */:
                                        consume(1);
                                        break;
                                }
                            case '!':
                            case '\"':
                            case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                            case '$':
                            case '%':
                                consume(1);
                                break;
                        }
                    }
                    consume(1);
                }
            } catch (ParseException unused) {
                return null;
            }
        }
        return this.buffer.substring(i, this.ptr);
    }

    public String quotedString() throws ParseException {
        int i = this.ptr + 1;
        if (lookAhead(0) != '\"') {
            return null;
        }
        consume(1);
        while (true) {
            char nextChar = getNextChar();
            if (nextChar == '\"') {
                return this.buffer.substring(i, this.ptr - 1);
            }
            if (nextChar == 0) {
                throw new ParseException(this.buffer + " :unexpected EOL", this.ptr);
            }
            if (nextChar == '\\') {
                consume(1);
            }
        }
    }

    public String getRest() {
        if (this.ptr >= this.buffer.length()) {
            return null;
        }
        return this.buffer.substring(this.ptr);
    }
}
