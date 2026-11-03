package com.xiaomi.onetrack.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.provider.Settings;
import android.text.TextUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DeviceUtil {
    private static Method n;
    private static String o;
    private static String p;
    private static String s;
    private static Boolean t;

    public static List c(Context context) {
        return null;
    }

    static {
        try {
            n = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
        } catch (Throwable th) {
            r.b("DeviceUtil", "sGetProp init failed ex: " + th.getMessage());
        }
        t = null;
    }

    public static String a(Context context) {
        return "";
    }

    public static String b(Context context) {
        if (!TextUtils.isEmpty(o)) {
            return o;
        }
        if (GAIDClient.b(context)) {
            return "";
        }
        String strA = GAIDClient.a(context);
        if (TextUtils.isEmpty(strA)) {
            return "";
        }
        o = strA;
        return strA;
    }

    public static void a() {
        o = null;
    }

    private static String a(String str) {
        try {
            Method method = n;
            if (method != null) {
                return String.valueOf(method.invoke(null, str));
            }
        } catch (Throwable th) {
            r.a("DeviceUtil", "getProp failed ex: " + th.getMessage());
        }
        return null;
    }

    abstract class GAIDClient {
        static String a(Context context) {
            if (!c(context)) {
                r.a("GAIDClient", "Google play service is not available");
                return "";
            }
            AdvertisingConnection advertisingConnection = new AdvertisingConnection();
            try {
                Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage("com.google.android.gms");
                if (context.bindService(intent, advertisingConnection, 1)) {
                    String strA = new a(advertisingConnection.a()).a();
                    context.unbindService(advertisingConnection);
                    return strA;
                }
            } catch (Throwable th) {
                try {
                    r.a("GAIDClient", "Query Google ADID failed: " + th.getMessage());
                } finally {
                    context.unbindService(advertisingConnection);
                }
            }
            return "";
        }

        static boolean b(Context context) {
            if (!c(context)) {
                r.a("GAIDClient", "Google play service is not available");
                return false;
            }
            AdvertisingConnection advertisingConnection = new AdvertisingConnection();
            try {
                Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage("com.google.android.gms");
                if (context.bindService(intent, advertisingConnection, 1)) {
                    boolean zA = new a(advertisingConnection.a()).a(true);
                    context.unbindService(advertisingConnection);
                    return zA;
                }
            } catch (Throwable th) {
                try {
                    r.a("GAIDClient", "Query Google isLimitAdTrackingEnabled failed :" + th.getMessage());
                } finally {
                    context.unbindService(advertisingConnection);
                }
            }
            return false;
        }

        private static boolean c(Context context) {
            try {
                context.getPackageManager().getPackageInfo("com.android.vending", 16384);
                return true;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }

        final class AdvertisingConnection implements ServiceConnection {
            private boolean b;
            private IBinder c;

            private AdvertisingConnection() {
                this.b = false;
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                synchronized (this) {
                    this.c = iBinder;
                    notifyAll();
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                this.b = true;
                this.c = null;
            }

            public IBinder a() {
                IBinder iBinder = this.c;
                if (iBinder != null) {
                    return iBinder;
                }
                if (iBinder == null && !this.b) {
                    synchronized (this) {
                        try {
                            wait(30000L);
                            if (this.c == null) {
                                throw new InterruptedException("Not connect or connect timeout to google play service");
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }
                return this.c;
            }
        }

        final class a implements IInterface {
            private IBinder a;

            public a(IBinder iBinder) {
                this.a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.a;
            }

            public String a() {
                if (this.a == null) {
                    return "";
                }
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                    this.a.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            public boolean a(boolean z) {
                if (this.a == null) {
                    return false;
                }
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.a.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }

    public static String d(Context context) {
        return "";
    }

    public static String b() {
        return Build.MODEL;
    }

    public static String c() {
        return a("ro.product.marketname");
    }

    public static String d() {
        return Build.MANUFACTURER;
    }

    public static String f(Context context) {
        try {
            if (ab.b(p)) {
                return p;
            }
            String string = Settings.System.getString(context.getContentResolver(), com.ot.pubsub.a.a.A);
            if (ab.b(string) && !"0000000000000000".equals(string)) {
                p = string;
                return string;
            }
            return "";
        } catch (Throwable th) {
            r.b("DeviceUtil", "getandroid d throwable:" + th.getMessage());
            return "";
        }
    }

    public static boolean g() {
        if (t == null) {
            String strB = b();
            if (ab.b(strB)) {
                String upperCase = strB.toUpperCase();
                if (upperCase.contains("MITV") || upperCase.contains("MIBOX") || upperCase.contains("PROJECTOR")) {
                    Boolean bool = Boolean.TRUE;
                    t = bool;
                    return bool.booleanValue();
                }
            }
            t = Boolean.FALSE;
        }
        return t.booleanValue();
    }

    public static String h() {
        if (!TextUtils.isEmpty(s)) {
            return s;
        }
        String strB = a.b();
        s = strB;
        if (!TextUtils.isEmpty(strB)) {
            return s;
        }
        if (i()) {
            s = "MI_PAD";
            return "MI_PAD";
        }
        if (j()) {
            s = "MI_WIFI_SPEAKER";
            return "MI_WIFI_SPEAKER";
        }
        if (s.b()) {
            s = "MI_PHONE";
            return "MI_PHONE";
        }
        s = "OTHER";
        return "OTHER";
    }

    private static boolean i() {
        try {
            return ((Boolean) Class.forName("miui.os.Build").getField("IS_TABLET").get(null)).booleanValue();
        } catch (Throwable th) {
            r.c("DeviceUtil", "get IS_TABLET from miui.os.Build exception: " + th.getMessage());
            try {
                return l.a("is_pad", false);
            } catch (Throwable th2) {
                r.c("DeviceUtil", "get is_pad from FeatureParser exception: " + th2.getMessage());
                return false;
            }
        }
    }

    abstract class a {
        /* JADX INFO: Access modifiers changed from: private */
        public static String b() {
            try {
                Class<?> cls = Class.forName("mitv.common.ConfigurationManager");
                Class[] clsArr = new Class[0];
                Class[] clsArr2 = new Class[0];
                int i = Integer.parseInt(String.valueOf(cls.getMethod("getProductCategory", null).invoke(cls.getMethod("getInstance", null).invoke(cls, null), null)));
                Class<?> cls2 = Class.forName("mitv.tv.TvContext");
                if (i == Integer.parseInt(String.valueOf(a(cls2, "PRODUCT_CATEGORY_MITV")))) {
                    return "MI_TV";
                }
                if (i == Integer.parseInt(String.valueOf(a(cls2, "PRODUCT_CATEGORY_MIBOX")))) {
                    return "MI_BOX";
                }
                if (i == Integer.parseInt(String.valueOf(a(cls2, "PRODUCT_CATEGORY_MITVBOX")))) {
                    return "MI_TVBOX";
                }
                if (i != Integer.parseInt(String.valueOf(a(cls2, "PRODUCT_CATEGORY_MIPROJECTOR")))) {
                    return "";
                }
                return "MI_PROJECTOR";
            } catch (Throwable th) {
                r.c("DeviceUtil", "getMiTvProductCategory exception: " + th.getMessage());
                return "";
            }
        }

        private static Object a(Class cls, String str) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField.get(null);
            } catch (Throwable th) {
                r.c("DeviceUtil", "getStaticVariableValue exception: " + th.getMessage());
                return null;
            }
        }
    }

    private static boolean j() {
        String strA = ad.a("ro.product.brand");
        return !TextUtils.isEmpty(strA) && strA.contains("XiaoAiTongXue");
    }
}
