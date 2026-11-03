package com.xiaomi.onetrack.a.c;

import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public abstract class c {
    public static void a() {
        ArrayList arrayList;
        try {
            if (b()) {
                r.a("AdMonitorUploader", "即将读取数据库并上传数据");
                int i = 0;
                while (i <= 20) {
                    com.xiaomi.onetrack.a.c.a().d();
                    a aVarB = com.xiaomi.onetrack.a.c.a().b();
                    if (aVarB != null && (arrayList = aVarB.b) != null && arrayList.size() > 0) {
                        ArrayList arrayList2 = new ArrayList();
                        ArrayList arrayList3 = new ArrayList();
                        ArrayList arrayList4 = aVarB.b;
                        int size = arrayList4.size();
                        int i2 = 0;
                        while (i2 < size) {
                            Object obj = arrayList4.get(i2);
                            i2++;
                            com.xiaomi.onetrack.a.b.a aVar = (com.xiaomi.onetrack.a.b.a) obj;
                            boolean zA = com.xiaomi.onetrack.g.b.a(aVar);
                            int iB = aVar.b();
                            if (zA) {
                                arrayList2.add(Integer.valueOf(iB));
                            } else {
                                arrayList3.add(Integer.valueOf(iB));
                            }
                        }
                        if (arrayList2.size() > 0) {
                            com.xiaomi.onetrack.a.c.a().a(arrayList2);
                        }
                        if (arrayList3.size() > 0) {
                            com.xiaomi.onetrack.a.c.a().b(arrayList3);
                        }
                        i++;
                        if (aVarB.c) {
                            r.a("AdMonitorUploader", "No more ad monitor records");
                            return;
                        }
                    }
                    r.a("AdMonitorUploader", "满足条件的adMonitor记录为空，即将返回");
                    return;
                }
            }
        } catch (Throwable th) {
            r.a("AdMonitorUploader", "uploadData Throwable:" + th.getMessage());
        }
    }

    private static boolean b() {
        if (s.a("AdMonitorUploader")) {
            r.a("AdMonitorUploader", "the device is not provisioned, stop poll!");
            return false;
        }
        if (!com.xiaomi.onetrack.g.c.a()) {
            r.a("AdMonitorUploader", "network is unconnected, stop poll!");
            return false;
        }
        if (com.xiaomi.onetrack.a.c.a().e() != 0) {
            return true;
        }
        r.a("AdMonitorUploader", "no data remain in db, stop poll!");
        return false;
    }
}
