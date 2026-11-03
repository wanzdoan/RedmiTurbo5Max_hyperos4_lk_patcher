package com.google.common.base;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Joiner {
    private final String separator;

    /* JADX INFO: renamed from: com.google.common.base.Joiner$1, reason: invalid class name */
    abstract class AnonymousClass1 extends Joiner {
    }

    public static Joiner on(String str) {
        return new Joiner(str);
    }

    public static Joiner on(char c) {
        return new Joiner(String.valueOf(c));
    }

    private Joiner(String str) {
        str.getClass();
        this.separator = str;
    }

    public Appendable appendTo(Appendable appendable, Iterator it) throws IOException {
        appendable.getClass();
        if (it.hasNext()) {
            appendable.append(toString(it.next()));
            while (it.hasNext()) {
                appendable.append(this.separator);
                appendable.append(toString(it.next()));
            }
        }
        return appendable;
    }

    public final StringBuilder appendTo(StringBuilder sb, Iterator it) {
        try {
            appendTo((Appendable) sb, it);
            return sb;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public final String join(Iterable iterable) {
        return join(iterable.iterator());
    }

    public final String join(Iterator it) {
        return appendTo(new StringBuilder(), it).toString();
    }

    public MapJoiner withKeyValueSeparator(String str) {
        return new MapJoiner(this, str, null);
    }

    public final class MapJoiner {
        private final Joiner joiner;
        private final String keyValueSeparator;

        /* synthetic */ MapJoiner(Joiner joiner, String str, AnonymousClass1 anonymousClass1) {
            this(joiner, str);
        }

        private MapJoiner(Joiner joiner, String str) {
            this.joiner = joiner;
            str.getClass();
            this.keyValueSeparator = str;
        }

        public Appendable appendTo(Appendable appendable, Iterator it) throws IOException {
            appendable.getClass();
            if (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                appendable.append(this.joiner.toString(entry.getKey()));
                appendable.append(this.keyValueSeparator);
                appendable.append(this.joiner.toString(entry.getValue()));
                while (it.hasNext()) {
                    appendable.append(this.joiner.separator);
                    Map.Entry entry2 = (Map.Entry) it.next();
                    appendable.append(this.joiner.toString(entry2.getKey()));
                    appendable.append(this.keyValueSeparator);
                    appendable.append(this.joiner.toString(entry2.getValue()));
                }
            }
            return appendable;
        }

        public StringBuilder appendTo(StringBuilder sb, Iterable iterable) {
            return appendTo(sb, iterable.iterator());
        }

        public StringBuilder appendTo(StringBuilder sb, Iterator it) {
            try {
                appendTo((Appendable) sb, it);
                return sb;
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
    }

    CharSequence toString(Object obj) {
        java.util.Objects.requireNonNull(obj);
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }
}
