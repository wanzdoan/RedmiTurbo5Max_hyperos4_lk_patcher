package com.ot.pubsub.util;

import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class p {
    private static final String a = "PermissionUtil";
    private static final String b = "android.permission.READ_PRIVILEGED_PHONE_STATE";
    private static Set c;

    static {
        try {
            HashSet hashSet = new HashSet();
            c = hashSet;
            hashSet.add("android");
            c.add("com.miui.analytics");
            c.add("com.miui.cit");
            c.add("com.xiaomi.finddevice");
            c.add("com.miui.securitycenter");
            c.add("com.android.settings");
            c.add("com.android.vending");
            c.add("com.google.android.gms");
            c.add("com.xiaomi.factory.mmi");
            c.add("com.miui.qr");
            c.add("com.android.contacts");
            c.add("com.qualcomm.qti.autoregistration");
            c.add("com.miui.tsmclient");
            c.add("com.miui.sekeytool");
            c.add("com.android.updater");
            if (!"cn_chinamobile".equals(u.a("ro.miui.cust_variant")) && !"cn_chinatelecom".equals(u.a("ro.miui.cust_variant"))) {
                return;
            }
            c.add("com.mobiletools.systemhelper");
            c.add("com.miui.dmregservice");
        } catch (Exception e) {
            Log.e(a, "static initializer: " + e.toString());
        }
    }

    public static boolean a(Context context, String str) {
        return b(context, str);
    }

    private static boolean a(String str) {
        Set set;
        return (TextUtils.isEmpty(str) || (set = c) == null || !set.contains(str)) ? false : true;
    }

    public static boolean b(Context context, String str) {
        return context.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }

    public static boolean a(Context context) {
        if (a()) {
            return a(b.e()) && b(context, b);
        }
        return b(context, b);
    }

    private static boolean a() {
        try {
            return l.a() && !l.h() && "1".equals(u.a("ro.miui.restrict_imei"));
        } catch (Exception e) {
            j.b(a, "isRestrictIMEI " + e.toString());
            return false;
        }
    }

    public static boolean b(Context context) {
        return b(context, b);
    }
}
