package com.xiaomi.onetrack.a.b;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.r;

/* JADX INFO: loaded from: classes2.dex */
public class a {
    private int e;
    private String f;
    private long g;
    private String h = "";
    private String i = "";
    private String j = "";
    private int k = 0;
    private String l = "";
    private int m = 0;
    private int n = 0;

    public String a() {
        return this.j;
    }

    public void a(String str) {
        this.j = str;
    }

    public int b() {
        return this.e;
    }

    public void a(int i) {
        this.e = i;
    }

    public String c() {
        return this.f;
    }

    public void b(String str) {
        this.f = str;
    }

    public long d() {
        return this.g;
    }

    public void a(long j) {
        this.g = j;
    }

    public String e() {
        return this.h;
    }

    public void c(String str) {
        this.h = str;
    }

    public String f() {
        return this.i;
    }

    public void d(String str) {
        this.i = str;
    }

    public void b(int i) {
        this.k = i;
    }

    public int h() {
        return this.n;
    }

    public void c(int i) {
        this.n = i;
    }

    public int i() {
        return this.m;
    }

    public void d(int i) {
        this.m = i;
    }

    public String j() {
        return this.l;
    }

    public void e(String str) {
        this.l = str;
    }

    public boolean k() {
        try {
            return (TextUtils.isEmpty(this.f) || TextUtils.isEmpty(this.h) || TextUtils.isEmpty(this.i)) ? false : true;
        } catch (Exception e) {
            r.a("AdMonitor", "check AdMonitor isValid error:" + e.getMessage());
            return false;
        }
    }
}
