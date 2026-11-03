package com.xiaomi.onetrack.c;

import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class y {
    private long a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private byte[] g;
    private int h;
    private Map i;
    private long j;

    public y(long j, String str, String str2, String str3, String str4, String str5, byte[] bArr, Map map, long j2, int i) {
        this.a = j;
        this.e = str;
        this.f = str2;
        this.b = str3;
        this.c = str4;
        this.d = str5;
        this.g = bArr;
        this.i = map;
        this.j = j2;
        this.h = i;
    }

    public long a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }

    public String d() {
        return this.e;
    }

    public void c(String str) {
        this.e = str;
    }

    public String e() {
        return this.f;
    }

    public void d(String str) {
        this.f = str;
    }

    public byte[] f() {
        return this.g;
    }

    public int g() {
        return this.h;
    }

    public Map h() {
        return this.i;
    }

    public void a(Map map) {
        this.i = map;
    }

    public long i() {
        return this.j;
    }

    public String j() {
        return this.d;
    }

    public String toString() {
        return "TransformDBData{mId=" + this.a + ", mAppId='" + this.b + "', mPackageName='" + this.c + "', mEventName='" + this.d + "', mProjectID='" + this.e + "', mTopic='" + this.f + "', mData='" + this.g + "', mPriority='" + this.h + "', mAttributes=" + this.i + ", mTimestamp=" + this.j + '}';
    }
}
