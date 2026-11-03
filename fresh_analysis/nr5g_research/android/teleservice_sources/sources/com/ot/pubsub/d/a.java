package com.ot.pubsub.d;

import android.text.TextUtils;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private long a;
    private String b;
    private String c;
    private Map d;

    public a(long j, String str, String str2, Map map) {
        this.a = j;
        this.b = str;
        this.c = str2;
        this.d = map;
    }

    public boolean a() {
        return (TextUtils.isEmpty(this.b) || TextUtils.isEmpty(this.c)) ? false : true;
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

    public Map e() {
        return this.d;
    }

    public void a(Map map) {
        this.d = map;
    }

    public String toString() {
        return "Message{mId=" + this.a + ", mTopic='" + this.b + "', mData='" + this.c + "', mAttributes=" + this.d + '}';
    }
}
