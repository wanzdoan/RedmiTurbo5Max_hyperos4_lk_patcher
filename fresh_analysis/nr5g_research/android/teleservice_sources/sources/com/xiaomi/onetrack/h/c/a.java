package com.xiaomi.onetrack.h.c;

import java.util.HashMap;

/* JADX INFO: loaded from: classes2.dex */
public class a {
    HashMap a;
    private String b;
    private int c;
    private HashMap d;

    private a(C0021a c0021a) {
        this.b = c0021a.a;
        this.c = c0021a.b;
        this.a = c0021a.c;
        this.d = c0021a.d;
    }

    public String a() {
        return this.b;
    }

    public int b() {
        return this.c;
    }

    public HashMap c() {
        return this.a;
    }

    public HashMap d() {
        return this.d;
    }

    /* JADX INFO: renamed from: com.xiaomi.onetrack.h.c.a$a, reason: collision with other inner class name */
    public class C0021a {
        private String a;
        private int b;
        private HashMap c;
        private HashMap d;

        public C0021a a(String str) {
            this.a = str;
            return this;
        }

        public C0021a a(int i) {
            this.b = i;
            return this;
        }

        public C0021a a(HashMap map) {
            this.c = map;
            return this;
        }

        public C0021a b(HashMap map) {
            this.d = map;
            return this;
        }

        public a a() {
            return new a(this);
        }
    }
}
