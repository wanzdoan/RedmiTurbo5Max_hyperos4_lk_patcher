package com.ot.pubsub.util;

import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class c {
    private static final String a = "DataConvertUtil";

    public static String a(Map map) {
        if (map != null && !map.isEmpty()) {
            try {
                return new JSONObject(map).toString();
            } catch (Exception e) {
                j.b(a, "map to json error:" + e.getMessage());
            }
        }
        return "";
    }

    public static Map a(String str) {
        if (TextUtils.isEmpty(str)) {
            return new HashMap();
        }
        HashMap map = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                map.put(next, jSONObject.optString(next));
            }
            return map;
        } catch (Exception e) {
            j.b(a, "json to map error:" + e.getMessage());
            return map;
        }
    }

    public static byte[] a(Object obj) throws Throwable {
        ObjectOutputStream objectOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                try {
                    try {
                        objectOutputStream.writeObject(obj);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        i.a((OutputStream) byteArrayOutputStream);
                        i.a((OutputStream) objectOutputStream);
                        return byteArray;
                    } catch (IOException e) {
                        e = e;
                        j.b(a, "serialize error:" + e.getMessage());
                        i.a((OutputStream) byteArrayOutputStream);
                        i.a((OutputStream) objectOutputStream);
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    byteArrayOutputStream2 = byteArrayOutputStream;
                    i.a((OutputStream) byteArrayOutputStream2);
                    i.a((OutputStream) objectOutputStream);
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                objectOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                objectOutputStream = null;
                byteArrayOutputStream2 = byteArrayOutputStream;
                i.a((OutputStream) byteArrayOutputStream2);
                i.a((OutputStream) objectOutputStream);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            byteArrayOutputStream = null;
            objectOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            objectOutputStream = null;
            i.a((OutputStream) byteArrayOutputStream2);
            i.a((OutputStream) objectOutputStream);
            throw th;
        }
    }

    public static Object a(byte[] bArr) throws Throwable {
        ObjectInputStream objectInputStream;
        ByteArrayInputStream byteArrayInputStream;
        ByteArrayInputStream byteArrayInputStream2 = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
            try {
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                try {
                    try {
                        Object object = objectInputStream.readObject();
                        i.a((InputStream) byteArrayInputStream);
                        i.a((InputStream) objectInputStream);
                        return object;
                    } catch (Exception e) {
                        e = e;
                        j.b(a, "deserialize error:" + e.getMessage());
                        i.a((InputStream) byteArrayInputStream);
                        i.a((InputStream) objectInputStream);
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    byteArrayInputStream2 = byteArrayInputStream;
                    i.a((InputStream) byteArrayInputStream2);
                    i.a((InputStream) objectInputStream);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                objectInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                objectInputStream = null;
                byteArrayInputStream2 = byteArrayInputStream;
                i.a((InputStream) byteArrayInputStream2);
                i.a((InputStream) objectInputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            objectInputStream = null;
            byteArrayInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            objectInputStream = null;
            i.a((InputStream) byteArrayInputStream2);
            i.a((InputStream) objectInputStream);
            throw th;
        }
    }
}
