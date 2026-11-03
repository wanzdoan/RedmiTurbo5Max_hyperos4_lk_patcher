package com.xiaomi.onetrack.c;

import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class m {
    private long a;
    private String b;
    private String c;
    private String d;
    private Map e;
    private long f;

    public m(long j, String str, String str2, String str3, Map map, long j2) {
        this.a = j;
        this.b = str;
        this.c = str2;
        this.d = str3;
        this.e = map;
        this.f = j2;
    }

    public long b() {
        return this.a;
    }

    public String c() {
        return this.b;
    }

    public String d() {
        return this.c;
    }

    public String e() {
        return this.d;
    }

    public Map f() {
        return this.e;
    }

    public long g() {
        return this.f;
    }

    public String toString() {
        return "PSMessage{mId=" + this.a + ", mProjectID='" + this.b + "', mTopic='" + this.c + "', mData='" + this.d + "', mAttributes=" + this.e + ", mTimestamp=" + this.f + '}';
    }
}
