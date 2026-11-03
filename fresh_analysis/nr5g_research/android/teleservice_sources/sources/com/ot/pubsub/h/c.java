package com.ot.pubsub.h;

import com.ot.pubsub.b.h;
import com.ot.pubsub.util.j;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class c extends a {
    private static final String e = "CustomMessage";

    public c(String str, String str2, String str3, String str4) {
        try {
            a(str);
            c(str3);
            b(str2);
            b(System.currentTimeMillis());
            a(new JSONObject(str4));
            a(h.a().a(str, str3, com.ot.pubsub.b.a.d, 1));
        } catch (Exception e2) {
            j.a(e, "CustomMessage error:" + e2.toString());
        }
    }
}
