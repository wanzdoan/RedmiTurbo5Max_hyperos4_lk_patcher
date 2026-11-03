package com.ot.pubsub.d;

import android.text.TextUtils;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class c {
    private long a;
    private String b;
    private String c;
    private String d;
    private Map e;
    private long f;
    private int g;

    public c(long j, String str, String str2, String str3, Map map, int i, long j2) {
        this.a = j;
        this.b = str;
        this.c = str2;
        this.d = str3;
        this.e = map;
        this.g = i;
        this.f = j2;
    }

    public boolean a() {
        return (TextUtils.isEmpty(this.b) || TextUtils.isEmpty(this.c) || TextUtils.isEmpty(this.d)) ? false : true;
    }

    public long b() {
        return this.a;
    }

    public void a(long j) {
        this.a = j;
    }

    public String c() {
        return this.b;
    }

    public void a(String str) {
        this.b = str;
    }

    public String d() {
        return this.c;
    }

    public void b(String str) {
        this.c = str;
    }

    public String e() {
        return this.d;
    }

    public void c(String str) {
        this.d = str;
    }

    public Map f() {
        return this.e;
    }

    public void a(Map map) {
        this.e = map;
    }

    public long g() {
        return this.f;
    }

    public void b(long j) {
        this.f = this.f;
    }

    public int h() {
        return this.g;
    }

    public void a(int i) {
        this.g = i;
    }

    public String toString() {
        return "PSMessage{mId=" + this.a + ", mProjectID='" + this.b + "', mTopic='" + this.c + "', mData='" + this.d + "', mAttributes=" + this.e + ", mGzipAndEncrypt=" + this.g + ", mTimestamp=" + this.f + '}';
    }
}
