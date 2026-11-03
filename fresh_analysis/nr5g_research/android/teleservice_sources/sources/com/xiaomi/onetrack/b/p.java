package com.xiaomi.onetrack.b;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class p {
    public String a;
    public String b;
    public String c;
    public List d = new ArrayList();
    public String e;
    public long f;
    private com.xiaomi.onetrack.i.a g;

    public com.xiaomi.onetrack.i.a a() {
        com.xiaomi.onetrack.i.a aVar = this.g;
        if (aVar != null) {
            return aVar;
        }
        if (!TextUtils.isEmpty(this.e)) {
            this.g = com.xiaomi.onetrack.i.a.a(this.e);
        }
        return this.g;
    }

    public void a(com.xiaomi.onetrack.i.a aVar) {
        if (aVar != null) {
            this.g = aVar;
            if (TextUtils.isEmpty(aVar.g)) {
                return;
            }
            this.e = aVar.g;
        }
    }

    public void b() {
        this.g = null;
        this.e = "";
    }
}
