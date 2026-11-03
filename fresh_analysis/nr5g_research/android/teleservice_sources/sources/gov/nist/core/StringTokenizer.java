package gov.nist.core;

import java.text.ParseException;

/* JADX INFO: loaded from: classes2.dex */
public abstract class StringTokenizer {
    protected String buffer;
    protected int bufferLen;
    protected int ptr = 0;

    public StringTokenizer(String str) {
        this.buffer = str;
        this.bufferLen = str.length();
    }

    public boolean hasMoreChars() {
        return this.ptr < this.bufferLen;
    }

    public static boolean isAlpha(char c) {
        if (c <= 127) {
            return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
        }
        return Character.isLowerCase(c) || Character.isUpperCase(c);
    }

    public static boolean isDigit(char c) {
        if (c <= 127) {
            return c <= '9' && c >= '0';
        }
        return Character.isDigit(c);
    }

    public static boolean isAlphaDigit(char c) {
        if (c <= 127) {
            return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c <= '9' && c >= '0');
        }
        return Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c);
    }

    public char lookAhead(int i) {
        try {
            return this.buffer.charAt(this.ptr + i);
        } catch (IndexOutOfBoundsException unused) {
            return (char) 0;
        }
    }

    public char getNextChar() throws ParseException {
        int i = this.ptr;
        int i2 = this.bufferLen;
        String str = this.buffer;
        if (i >= i2) {
            throw new ParseException(str + " getNextChar: End of buffer", this.ptr);
        }
        this.ptr = i + 1;
        return str.charAt(i);
    }

    public void consume(int i) {
        this.ptr += i;
    }
}
