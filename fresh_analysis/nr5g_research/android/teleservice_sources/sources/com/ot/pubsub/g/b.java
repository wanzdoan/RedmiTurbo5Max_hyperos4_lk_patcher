package com.ot.pubsub.g;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.ot.pubsub.util.t;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class b {
    private static final String a = "b";
    private static final String b = "content://com.miui.analytics.OneTrackProvider/insId";
    private static final String c = "insId";
    private static final String d = "pkg";
    private static final String e = "sign";
    private static volatile b f;
    private static String g;
    private static String j;
    private boolean k = false;
    private final Context h = com.ot.pubsub.util.b.a();
    private final Context i = com.ot.pubsub.util.b.b();

    public static b a() {
        if (f == null) {
            synchronized (b.class) {
                try {
                    if (f == null) {
                        f = new b();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return f;
    }

    private b() {
        j = com.ot.pubsub.util.b.e();
    }

    public void a(Boolean bool) {
        this.k = bool.booleanValue();
    }

    public void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        g = str;
        if (this.k) {
            b(str);
        }
        t.e(g);
    }

    public String b() {
        String strD;
        if (!TextUtils.isEmpty(g)) {
            return g;
        }
        if (this.k) {
            strD = c();
            String strD2 = d();
            if (TextUtils.isEmpty(strD) && !TextUtils.isEmpty(strD2)) {
                b(strD2);
                strD = strD2;
            } else if (!TextUtils.isEmpty(strD) && TextUtils.isEmpty(strD2)) {
                t.e(strD);
            }
        } else {
            strD = d();
        }
        if (TextUtils.isEmpty(strD)) {
            String string = UUID.randomUUID().toString();
            g = string;
            if (this.k) {
                b(string);
            }
            t.e(g);
        } else {
            g = strD;
        }
        return g;
    }

    private String c() {
        String string = null;
        try {
            Uri.Builder builderBuildUpon = Uri.parse(b).buildUpon();
            builderBuildUpon.appendQueryParameter(d, j);
            builderBuildUpon.appendQueryParameter(e, com.ot.pubsub.c.a.a(c + j));
            Cursor cursorQuery = this.i.getContentResolver().query(builderBuildUpon.build(), null, null, null, null);
            if (cursorQuery != null) {
                while (cursorQuery.moveToNext()) {
                    string = cursorQuery.getString(0);
                }
                cursorQuery.close();
            }
            return string;
        } catch (Throwable th) {
            com.ot.pubsub.util.j.a(a, "getRemoteCacheInstanceId e", th.getMessage());
            return string;
        }
    }

    private void b(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            Uri uri = Uri.parse(b);
            ContentValues contentValues = new ContentValues();
            contentValues.put(j, str);
            this.i.getContentResolver().insert(uri, contentValues);
        } catch (Exception e2) {
            t.e(str);
            com.ot.pubsub.util.j.a(a, "setRemoteCacheInstanceId e", e2);
        }
    }

    private String d() {
        String strA = t.a(this.h);
        if (TextUtils.isEmpty(strA)) {
            return t.f();
        }
        t.e(strA);
        return strA;
    }
}
