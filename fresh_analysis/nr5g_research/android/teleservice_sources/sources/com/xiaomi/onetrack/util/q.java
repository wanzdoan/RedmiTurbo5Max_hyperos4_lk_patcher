package com.xiaomi.onetrack.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import java.util.UUID;

/* JADX INFO: loaded from: classes2.dex */
public class q {
    private static final String a = "q";
    private static volatile q f;
    private static String g;
    private static String j;
    private boolean k = false;
    private final Context h = com.xiaomi.onetrack.f.a.a();
    private final Context i = com.xiaomi.onetrack.f.a.b();

    public static q a() {
        if (f == null) {
            synchronized (q.class) {
                try {
                    if (f == null) {
                        f = new q();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return f;
    }

    private q() {
        j = com.xiaomi.onetrack.f.a.e();
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
        ac.e(g);
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
                ac.e(strD);
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
            ac.e(g);
        } else {
            g = strD;
        }
        return g;
    }

    private String c() {
        String string = null;
        try {
            Uri.Builder builderBuildUpon = Uri.parse("content://com.miui.analytics.OneTrackProvider/insId").buildUpon();
            builderBuildUpon.appendQueryParameter("pkg", j);
            builderBuildUpon.appendQueryParameter("sign", com.xiaomi.onetrack.d.a.a("insId" + j));
            Cursor cursorQuery = this.i.getContentResolver().query(builderBuildUpon.build(), null, null, null, null);
            if (cursorQuery != null) {
                while (cursorQuery.moveToNext()) {
                    string = cursorQuery.getString(0);
                }
                cursorQuery.close();
            }
            return string;
        } catch (Exception e) {
            r.a(a, "getRemoteCacheInstanceId e", e);
            return string;
        }
    }

    private void b(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            Uri uri = Uri.parse("content://com.miui.analytics.OneTrackProvider/insId");
            ContentValues contentValues = new ContentValues();
            contentValues.put(j, str);
            this.i.getContentResolver().insert(uri, contentValues);
        } catch (Exception e) {
            ac.e(str);
            r.a(a, "setRemoteCacheInstanceId e", e);
        }
    }

    private String d() {
        String strA = ac.a(this.h);
        if (TextUtils.isEmpty(strA)) {
            return ac.j();
        }
        ac.e(strA);
        return strA;
    }
}
