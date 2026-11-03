package com.xiaomi.onetrack.c;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes2.dex */
public class z {
    private static z b;

    public static z a() {
        if (b == null) {
            b();
        }
        return b;
    }

    public static void b() {
        if (b == null) {
            synchronized (z.class) {
                try {
                    if (b == null) {
                        b = new z();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private z() {
    }

    public void a(String str, boolean z) {
        if (z != com.xiaomi.onetrack.util.s.b(str)) {
            if (z) {
                c();
            } else {
                d();
            }
        }
    }

    public void c() {
        c.a(new aa(this));
    }

    public void d() {
        c.a(new ab(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(ac acVar) {
        Iterator it = acVar.a.iterator();
        ArrayList arrayList = new ArrayList();
        while (it.hasNext()) {
            y yVar = (y) it.next();
            String strB = yVar.b();
            com.xiaomi.onetrack.b.p pVarB = com.xiaomi.onetrack.b.q.a().b(strB);
            if (pVarB == null || TextUtils.isEmpty(pVarB.b)) {
                arrayList.add(Long.valueOf(yVar.a()));
                it.remove();
            } else {
                yVar.c(pVarB.b);
                yVar.d(com.xiaomi.onetrack.util.a.a(strB));
                yVar.a(com.xiaomi.onetrack.util.a.b());
            }
        }
        acVar.c.removeAll(arrayList);
    }
}
