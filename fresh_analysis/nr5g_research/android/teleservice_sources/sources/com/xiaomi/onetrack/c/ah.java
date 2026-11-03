package com.xiaomi.onetrack.c;

import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.xiaomi.onetrack.api.ar;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class ah {
    private ae g;

    private ah() {
        d();
    }

    abstract class a {
        private static final ah a = new ah();
    }

    public static ah a() {
        return a.a;
    }

    public synchronized void a(int i, boolean z) {
        try {
            ae aeVar = this.g;
            if (aeVar != null) {
                aeVar.a(i, z);
            } else {
                com.xiaomi.onetrack.util.r.b("UploaderEngine", "*** impossible, upload timer should not be null");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void a(boolean z) {
        ae aeVar = this.g;
        if (aeVar != null) {
            aeVar.a(z);
        } else {
            com.xiaomi.onetrack.util.r.b("UploaderEngine", "*** impossible, upload timer should not be null");
        }
    }

    public boolean a(int i) {
        com.xiaomi.onetrack.util.r.a("UploaderEngine", "即将读取数据库并上传数据");
        if (ar.h) {
            return c(i);
        }
        return b(i);
    }

    private boolean c(int i) {
        i iVarA;
        if (!b()) {
            return true;
        }
        do {
            iVarA = d.a().a(i);
            if (iVarA == null) {
                com.xiaomi.onetrack.util.r.a("UploaderEngine", "满足条件的记录为空，即将返回, priority=" + i);
                return true;
            }
            ArrayList arrayList = iVarA.c;
            boolean zA = a(iVarA.a);
            com.xiaomi.onetrack.util.r.a("UploaderEngine", "upload success:" + zA);
            if (!zA) {
                return false;
            }
            a(iVarA.e);
            if (d.a().a(arrayList) == 0) {
                com.xiaomi.onetrack.util.r.b("UploaderEngine", "delete DB failed!", new Throwable());
            }
            return true;
        } while (!iVarA.d);
        com.xiaomi.onetrack.util.r.a("UploaderEngine", "No more records for prio=" + i);
        return true;
    }

    private void a(ArrayMap arrayMap) {
        if (arrayMap != null) {
            try {
                if (arrayMap.size() > 0) {
                    for (String str : arrayMap.keySet()) {
                        com.xiaomi.onetrack.h.d.b.a(str, "", "up", "", ((Integer) arrayMap.get(str)).intValue());
                    }
                }
            } catch (Exception e) {
                com.xiaomi.onetrack.util.r.b("UploaderEngine", "recordUploadSuccessStatsToDb exception ", e);
            }
        }
    }

    public boolean b(int i) {
        ArrayList arrayList;
        try {
            if (!c()) {
                return false;
            }
            int i2 = 100;
            int i3 = 0;
            while (true) {
                if (com.xiaomi.onetrack.util.r.a) {
                    i2 = 1000;
                }
                if (i3 >= i2) {
                    return true;
                }
                q qVarA = n.a().a(i);
                if (qVarA == null || (arrayList = qVarA.a) == null || arrayList.size() == 0) {
                    com.xiaomi.onetrack.util.r.a("UploaderEngine", "满足条件的记录为空，即将返回");
                } else {
                    com.xiaomi.onetrack.i.a aVarB = com.xiaomi.onetrack.b.q.a().b();
                    if (aVarB != null && !TextUtils.isEmpty(aVarB.d)) {
                        r.a(qVarA, aVarB);
                        if (qVarA.d) {
                            com.xiaomi.onetrack.util.r.a("UploaderEngine", "No more records ");
                        } else {
                            i3++;
                        }
                    }
                    com.xiaomi.onetrack.util.r.a("UploaderEngine", "PollingUploader upload token is null");
                    return false;
                }
                return true;
            }
        } catch (Exception unused) {
        }
    }

    private boolean b() {
        if (com.xiaomi.onetrack.b.o.a() && com.xiaomi.onetrack.g.c.a() && !com.xiaomi.onetrack.b.o.b()) {
            return true;
        }
        com.xiaomi.onetrack.util.r.a("UploaderEngine", "不用处理消息, available=" + com.xiaomi.onetrack.b.o.a() + ", 是否有网=" + com.xiaomi.onetrack.g.c.a() + ", 数据库是否为空=" + com.xiaomi.onetrack.b.o.b());
        return false;
    }

    private boolean c() {
        if (com.xiaomi.onetrack.g.c.a() && !com.xiaomi.onetrack.util.s.a("UploaderEngine") && !n.a().e()) {
            return true;
        }
        com.xiaomi.onetrack.util.r.a("UploaderEngine", "ps data not match the upload status，即将返回");
        return false;
    }

    private boolean a(JSONArray jSONArray) {
        try {
            String strB = com.xiaomi.onetrack.util.z.a().b();
            String string = jSONArray.toString();
            com.xiaomi.onetrack.util.r.a("UploaderEngine", " payload:" + string);
            byte[] bArrA = a(a(string), com.xiaomi.onetrack.d.f.a().b()[0]);
            com.xiaomi.onetrack.util.r.a("UploaderEngine", "before zip and encrypt, len=" + string.length() + ", after=" + bArrA.length);
            String strA = com.xiaomi.onetrack.g.b.a(strB, bArrA);
            StringBuilder sb = new StringBuilder("sendDataToServer response: ");
            sb.append(strA);
            com.xiaomi.onetrack.util.r.a("UploaderEngine", sb.toString());
            if (TextUtils.isEmpty(strA)) {
                return false;
            }
            return b(strA);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("UploaderEngine", "Exception while uploading ", e);
            return false;
        }
    }

    private void d() {
        HandlerThread handlerThread = new HandlerThread("onetrack_uploader_worker");
        handlerThread.start();
        this.g = new ae(handlerThread.getLooper());
    }

    protected static byte[] a(String str) throws Throwable {
        GZIPOutputStream gZIPOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(str.getBytes("UTF-8").length);
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                try {
                    try {
                        gZIPOutputStream.write(str.getBytes("UTF-8"));
                        gZIPOutputStream.finish();
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        com.xiaomi.onetrack.util.o.a((OutputStream) byteArrayOutputStream);
                        com.xiaomi.onetrack.util.o.a((OutputStream) gZIPOutputStream);
                        return byteArray;
                    } catch (Exception e) {
                        e = e;
                        com.xiaomi.onetrack.util.r.b("UploaderEngine", " zipData failed! " + e.toString());
                        com.xiaomi.onetrack.util.o.a((OutputStream) byteArrayOutputStream);
                        com.xiaomi.onetrack.util.o.a((OutputStream) gZIPOutputStream);
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    byteArrayOutputStream2 = byteArrayOutputStream;
                    com.xiaomi.onetrack.util.o.a((OutputStream) byteArrayOutputStream2);
                    com.xiaomi.onetrack.util.o.a((OutputStream) gZIPOutputStream);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                gZIPOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                gZIPOutputStream = null;
                byteArrayOutputStream2 = byteArrayOutputStream;
                com.xiaomi.onetrack.util.o.a((OutputStream) byteArrayOutputStream2);
                com.xiaomi.onetrack.util.o.a((OutputStream) gZIPOutputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            byteArrayOutputStream = null;
            gZIPOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            gZIPOutputStream = null;
            com.xiaomi.onetrack.util.o.a((OutputStream) byteArrayOutputStream2);
            com.xiaomi.onetrack.util.o.a((OutputStream) gZIPOutputStream);
            throw th;
        }
    }

    protected static byte[] a(byte[] bArr, String str) {
        if (bArr == null) {
            com.xiaomi.onetrack.util.r.b("UploaderEngine", "content is null");
            return null;
        }
        return com.xiaomi.onetrack.d.a.a(bArr, com.xiaomi.onetrack.d.c.a(str));
    }

    private boolean b(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            int iOptInt = jSONObject.optInt(com.ot.pubsub.i.a.a.d);
            if (iOptInt == 0) {
                com.xiaomi.onetrack.util.r.a("UploaderEngine", "成功发送数据到服务端");
                com.xiaomi.onetrack.b.a.a().a(jSONObject);
                return true;
            }
            if (iOptInt == -3) {
                com.xiaomi.onetrack.util.r.b("UploaderEngine", "signature expired, will update");
                com.xiaomi.onetrack.d.f.a().c();
                return false;
            }
            com.xiaomi.onetrack.util.r.b("UploaderEngine", "Error: status code=" + iOptInt);
            return false;
        } catch (Exception e) {
            com.xiaomi.onetrack.util.r.b("UploaderEngine", "parseUploadingResult exception ", e);
            return false;
        }
    }
}
