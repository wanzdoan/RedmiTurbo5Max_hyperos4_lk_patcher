package com.xiaomi.onetrack.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes2.dex */
public abstract class d {
    public static byte[] a(Object obj) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream objectOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    try {
                        objectOutputStream.writeObject(obj);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        o.a((OutputStream) byteArrayOutputStream);
                        o.a((OutputStream) objectOutputStream);
                        return byteArray;
                    } catch (IOException e) {
                        e = e;
                        r.b("DataConvertUtil", "serialize error:" + e.getMessage());
                        o.a((OutputStream) byteArrayOutputStream);
                        o.a((OutputStream) objectOutputStream);
                        return null;
                    }
                } catch (IOException e2) {
                    e = e2;
                    objectOutputStream = null;
                } catch (Throwable th) {
                    th = th;
                    byteArrayOutputStream2 = byteArrayOutputStream;
                    o.a((OutputStream) byteArrayOutputStream2);
                    o.a((OutputStream) 0);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e3) {
            e = e3;
            byteArrayOutputStream = null;
            objectOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            o.a((OutputStream) byteArrayOutputStream2);
            o.a((OutputStream) 0);
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
                        o.a((InputStream) byteArrayInputStream);
                        o.a((InputStream) objectInputStream);
                        return object;
                    } catch (Exception e) {
                        e = e;
                        r.b("DataConvertUtil", "deserialize error:" + e.getMessage());
                        o.a((InputStream) byteArrayInputStream);
                        o.a((InputStream) objectInputStream);
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    byteArrayInputStream2 = byteArrayInputStream;
                    o.a((InputStream) byteArrayInputStream2);
                    o.a((InputStream) objectInputStream);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                objectInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                objectInputStream = null;
                byteArrayInputStream2 = byteArrayInputStream;
                o.a((InputStream) byteArrayInputStream2);
                o.a((InputStream) objectInputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            objectInputStream = null;
            byteArrayInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            objectInputStream = null;
            o.a((InputStream) byteArrayInputStream2);
            o.a((InputStream) objectInputStream);
            throw th;
        }
    }
}
