package com.android.services.telephony.relay.phonecall.message;

import com.google.gson.Gson;

/* JADX INFO: loaded from: classes.dex */
public class GsonUtil {
    private static Gson sGson;

    private GsonUtil() {
    }

    public static synchronized Gson getInstance() {
        try {
            if (sGson == null) {
                sGson = new Gson();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sGson;
    }

    public static String toJson(Object obj) {
        return getInstance().toJson(obj);
    }

    public static Object fromJson(String str, Class cls) {
        try {
            return getInstance().fromJson(str, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
