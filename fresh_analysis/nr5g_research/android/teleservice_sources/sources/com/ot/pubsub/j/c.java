package com.ot.pubsub.j;

import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class c {
    private static final String a = "%s/v1/projects/%s/topics/%s:publish";
    private static final String b = "https://pubsub.googleapis.com";
    private static final String c = "UTF-8";
    private static volatile int d = 1;
    private static final String e = "UploaderDispense";

    public static void a(a aVar) {
        try {
            for (Map.Entry entry : c(aVar.a).entrySet()) {
                String str = (String) entry.getKey();
                List list = (List) entry.getValue();
                if (list != null && list.size() != 0) {
                    for (Map.Entry entry2 : d(list).entrySet()) {
                        String str2 = (String) entry2.getKey();
                        List list2 = (List) entry2.getValue();
                        if (list2 != null && list2.size() != 0) {
                            list2.removeAll(b(str, str2, list2));
                            c(str, str2, list2);
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }

    public static void b(a aVar) {
        try {
            for (Map.Entry entry : c(aVar.a).entrySet()) {
                String str = (String) entry.getKey();
                List list = (List) entry.getValue();
                if (list != null && list.size() != 0) {
                    for (Map.Entry entry2 : d(list).entrySet()) {
                        String str2 = (String) entry2.getKey();
                        List list2 = (List) entry2.getValue();
                        if (list2 != null && list2.size() != 0) {
                            a(str, str2, list2);
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }

    private static void a(String str, String str2, List list) throws Throwable {
        if (list == null || list.size() <= 0) {
            return;
        }
        d.b bVarD = d(str, str2, b(list));
        com.ot.pubsub.util.j.a(e, "uploadGzipEncryptData success:" + bVarD.a);
        if (bVarD.a) {
            com.ot.pubsub.util.j.a(e, "uploadGzipEncryptData deleted:" + com.ot.pubsub.g.f.a().a(list));
            return;
        }
        if (bVarD.b) {
            com.ot.pubsub.util.j.a(e, "uploadGzipEncryptData deleted:" + com.ot.pubsub.g.f.a().b(list));
        }
    }

    private static List b(String str, String str2, List list) throws Throwable {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            com.ot.pubsub.d.c cVar = (com.ot.pubsub.d.c) it.next();
            if (cVar != null && cVar.h() == d) {
                arrayList.add(cVar);
            }
        }
        if (arrayList.size() > 0) {
            d.b bVarD = d(str, str2, b(arrayList));
            com.ot.pubsub.util.j.a(e, "uploadGzipEncryptData success:" + bVarD.a);
            if (bVarD.a) {
                com.ot.pubsub.util.j.a(e, "uploadGzipEncryptData deleted:" + com.ot.pubsub.g.c.a().a(arrayList));
                return arrayList;
            }
            if (bVarD.b) {
                com.ot.pubsub.util.j.a(e, "uploadGzipEncryptData deleted:" + com.ot.pubsub.g.c.a().b(arrayList));
            }
        }
        return arrayList;
    }

    private static List c(String str, String str2, List list) throws Throwable {
        if (list != null && list.size() > 0) {
            d.b bVarD = d(str, str2, list);
            com.ot.pubsub.util.j.a(e, "uploadData success:" + bVarD.a);
            if (bVarD.a) {
                com.ot.pubsub.util.j.a(e, "upload deleted:" + com.ot.pubsub.g.c.a().a(list));
                return list;
            }
            if (bVarD.b) {
                com.ot.pubsub.util.j.a(e, "upload deleted:" + com.ot.pubsub.g.c.a().b(list));
            }
        }
        return list;
    }

    private static String a(List list) {
        if (list == null) {
            return "";
        }
        try {
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                com.ot.pubsub.d.c cVar = (com.ot.pubsub.d.c) it.next();
                JSONObject jSONObject2 = new JSONObject();
                Map mapF = cVar.f();
                if (mapF != null) {
                    JSONObject jSONObject3 = new JSONObject();
                    for (Map.Entry entry : mapF.entrySet()) {
                        if (!TextUtils.isEmpty((CharSequence) entry.getKey())) {
                            jSONObject3.put((String) entry.getKey(), entry.getValue());
                        }
                    }
                    jSONObject2.put(com.ot.pubsub.g.i.h, jSONObject3);
                }
                jSONObject2.put("data", cVar.e());
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("messages", jSONArray);
            return jSONObject.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    private static List b(List list) throws Throwable {
        if (list == null || list.size() == 0) {
            com.ot.pubsub.util.j.a(e, "messages is empty, return null");
            return null;
        }
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        String str = com.ot.pubsub.c.f.a().b()[1];
        String str2 = com.ot.pubsub.c.f.a().b()[0];
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                com.ot.pubsub.d.c cVar = (com.ot.pubsub.d.c) it.next();
                Map mapF = cVar.f();
                if (mapF != null) {
                    if (map.get(mapF) == null) {
                        map.put(mapF, new JSONArray());
                    }
                    ((JSONArray) map.get(mapF)).put(cVar.e());
                }
            }
            for (Map.Entry entry : map.entrySet()) {
                Map map2 = (Map) entry.getKey();
                map2.put(com.ot.pubsub.util.a.a, "true");
                map2.put(com.ot.pubsub.util.a.b, str);
                JSONArray jSONArray = (JSONArray) entry.getValue();
                com.ot.pubsub.util.j.a(e, "zip before : " + jSONArray.toString().length());
                byte[] bArrA = a(jSONArray.toString());
                com.ot.pubsub.util.j.a(e, "zip after : " + bArrA.length);
                arrayList.add(new com.ot.pubsub.d.c(-1L, null, null, com.ot.pubsub.c.c.b(a(bArrA, str2)), map2, 1, -1L));
            }
            return arrayList;
        } catch (Exception e2) {
            com.ot.pubsub.util.j.a(e, "sortByAttribute error: " + e2.getMessage());
            return arrayList;
        }
    }

    private static Map c(List list) {
        HashMap map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            com.ot.pubsub.d.c cVar = (com.ot.pubsub.d.c) list.get(i);
            String strC = cVar.c();
            if (map.get(strC) == null) {
                map.put(strC, new ArrayList());
            }
            ((List) map.get(strC)).add(cVar);
        }
        return map;
    }

    private static Map d(List list) {
        HashMap map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            com.ot.pubsub.d.c cVar = (com.ot.pubsub.d.c) list.get(i);
            String strD = cVar.d();
            if (map.get(strD) == null) {
                map.put(strD, new ArrayList());
            }
            ((List) map.get(strD)).add(cVar);
        }
        return map;
    }

    private static d.b d(String str, String str2, List list) throws Throwable {
        try {
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && list != null && list.size() != 0) {
                String strA = com.ot.pubsub.i.b.b.a().a(str);
                if (TextUtils.isEmpty(strA)) {
                    return new d.b(false, false);
                }
                com.ot.pubsub.i.a aVarA = com.ot.pubsub.i.a.b.a(String.format(a, b, str, str2), a(list), strA);
                if (aVarA != null) {
                    int i = aVarA.d;
                    if (i == 401) {
                        com.ot.pubsub.i.b.b.a().b(str);
                    } else if (i == 404) {
                        if ("NOT_FOUND".equals(aVarA.g) && !TextUtils.isEmpty(aVarA.f) && (aVarA.f.contains(str) || aVarA.f.contains(str2))) {
                            return new d.b(false, true);
                        }
                    } else if (i == 200) {
                        return new d.b(true, false);
                    }
                }
                return new d.b(false, false);
            }
            return new d.b(false, false);
        } catch (Exception e2) {
            com.ot.pubsub.util.j.b(e, "Exception while uploading ", e2);
        }
    }

    private static byte[] a(String str) throws Throwable {
        GZIPOutputStream gZIPOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(str.getBytes(c).length);
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                try {
                    try {
                        gZIPOutputStream.write(str.getBytes(c));
                        gZIPOutputStream.finish();
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        com.ot.pubsub.util.i.a((OutputStream) byteArrayOutputStream);
                        com.ot.pubsub.util.i.a((OutputStream) gZIPOutputStream);
                        return byteArray;
                    } catch (Exception e2) {
                        e = e2;
                        com.ot.pubsub.util.j.b(e, " zipData failed! " + e.toString());
                        com.ot.pubsub.util.i.a((OutputStream) byteArrayOutputStream);
                        com.ot.pubsub.util.i.a((OutputStream) gZIPOutputStream);
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    byteArrayOutputStream2 = byteArrayOutputStream;
                    com.ot.pubsub.util.i.a((OutputStream) byteArrayOutputStream2);
                    com.ot.pubsub.util.i.a((OutputStream) gZIPOutputStream);
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                gZIPOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                gZIPOutputStream = null;
                byteArrayOutputStream2 = byteArrayOutputStream;
                com.ot.pubsub.util.i.a((OutputStream) byteArrayOutputStream2);
                com.ot.pubsub.util.i.a((OutputStream) gZIPOutputStream);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            byteArrayOutputStream = null;
            gZIPOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            gZIPOutputStream = null;
            com.ot.pubsub.util.i.a((OutputStream) byteArrayOutputStream2);
            com.ot.pubsub.util.i.a((OutputStream) gZIPOutputStream);
            throw th;
        }
    }

    private static byte[] a(byte[] bArr, String str) {
        if (bArr == null) {
            com.ot.pubsub.util.j.b(e, "content is null");
            return null;
        }
        return com.ot.pubsub.c.a.a(bArr, com.ot.pubsub.c.c.a(str));
    }
}
