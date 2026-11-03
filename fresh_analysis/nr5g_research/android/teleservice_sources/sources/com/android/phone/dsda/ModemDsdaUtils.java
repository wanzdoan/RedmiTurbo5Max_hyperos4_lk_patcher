package com.android.phone.dsda;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.xiaomi.mirilhook.MiRilHookCallback;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class ModemDsdaUtils {
    private static Class mMiRilHookClass;
    private static Object mMiRilHookObj;
    private static Class mQcRilHookCallbackClass;
    private static Object mQcRilHookCallbackObj;
    private static ModemDsdaUtils sInstance;
    private Context mContext;
    protected Handler mHandler = new Handler() { // from class: com.android.phone.dsda.ModemDsdaUtils.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemDsdaUtils.log("handleMessage msg id: " + message.what);
            if (message.what == 0) {
                ModemDsdaUtils.log("EVENT_QCRIL_HOOK_READY");
                ModemDsdaUtils.this.mQcrilHookReady = true;
                EventHubManager.getInstance().postEvent(new TelephonyEvent.RilEvent("ril_ready", true));
                return;
            }
            ModemDsdaUtils.log("Not support this msg");
        }
    };
    private boolean mQcrilHookReady;

    private ModemDsdaUtils(Context context) {
        this.mQcrilHookReady = false;
        this.mContext = context;
        if (new File("/system_ext/framework/xiaomi-modem-common.jar").exists()) {
            try {
                Class clsLoadClass = new PathClassLoader("/system_ext/framework/xiaomi-modem-common.jar", getClass().getClassLoader()).loadClass("com.xiaomi.mirilhook.MiRilHook");
                mMiRilHookClass = clsLoadClass;
                if (clsLoadClass == null) {
                    log("hook loading failed.");
                    return;
                }
                mQcRilHookCallbackClass = MiRilHookCallback.class;
                mQcRilHookCallbackObj = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{mQcRilHookCallbackClass}, new QcrilHookCbHandler());
                Constructor constructor = mMiRilHookClass.getConstructor(Context.class, mQcRilHookCallbackClass, String.class);
                if (constructor != null) {
                    mMiRilHookObj = constructor.newInstance(context, mQcRilHookCallbackObj, context.getPackageName());
                    return;
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        log("hook jar doesn't exist.");
        this.mQcrilHookReady = false;
    }

    public static synchronized ModemDsdaUtils init(Context context) {
        try {
            if (sInstance == null && MiuiPhoneUtils.isDeviceDsdaSupportedByQcom()) {
                sInstance = new ModemDsdaUtils(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public boolean isDsdaEnabled() {
        return onHookDsdaCurrentStatusGetSync();
    }

    public boolean setDsdaEnabled(boolean z) {
        log("setDsdaEnabled enabled : " + z);
        if (onHookCommonMsgSync(86, z ? 1 : 0) != null) {
            return true;
        }
        log("failed to update mbn config.");
        return false;
    }

    public boolean setDsdaCapPropertyEnabled(boolean z) {
        log("setDsdaCapPropertyEnabled enabled : " + z);
        if (onHookPropSetSync("persist.vendor.radio.dsda_cap", z ? "1" : "0") != null) {
            return true;
        }
        log("failed to set dsda prop.");
        return false;
    }

    public boolean onHookDsdaCurrentStatusGetSync() {
        Class cls = mMiRilHookClass;
        boolean zBooleanValue = false;
        if (cls != null && mMiRilHookObj != null && this.mQcrilHookReady) {
            try {
                Class[] clsArr = new Class[0];
                Method declaredMethod = cls.getDeclaredMethod("onHookDsdaCurrentStatusGetSync", null);
                declaredMethod.setAccessible(true);
                zBooleanValue = ((Boolean) declaredMethod.invoke(mMiRilHookObj, null)).booleanValue();
            } catch (Exception e) {
                log("onHookDsdaCurrentStatusGetSync Exception: " + e);
            }
        }
        log("onHookDsdaCurrentStatusGetSync ret = " + zBooleanValue);
        return zBooleanValue;
    }

    public ByteBuffer onHookPropSetSync(String str, String str2) {
        ByteBuffer byteBuffer;
        Class cls = mMiRilHookClass;
        if (cls == null || mMiRilHookObj == null || !this.mQcrilHookReady) {
            byteBuffer = null;
        } else {
            try {
                Method declaredMethod = cls.getDeclaredMethod("onHookPropSetSync", String.class, String.class);
                declaredMethod.setAccessible(true);
                byteBuffer = (ByteBuffer) declaredMethod.invoke(mMiRilHookObj, str, str2);
            } catch (Exception e) {
                log("onHookPropSetSync Exception: " + e);
                byteBuffer = null;
            }
        }
        log("onHookPropSetSync ret = " + byteBuffer);
        return byteBuffer;
    }

    public ByteBuffer onHookCommonMsgSync(int i, int i2) {
        ByteBuffer byteBuffer;
        Class cls = mMiRilHookClass;
        if (cls == null || mMiRilHookObj == null || !this.mQcrilHookReady) {
            byteBuffer = null;
        } else {
            try {
                Class<?> cls2 = Integer.TYPE;
                Method declaredMethod = cls.getDeclaredMethod("onHookCommonMsgSync", cls2, cls2);
                declaredMethod.setAccessible(true);
                byteBuffer = (ByteBuffer) declaredMethod.invoke(mMiRilHookObj, Integer.valueOf(i), Integer.valueOf(i2));
            } catch (Exception e) {
                log("onHookCommonMsgSync Exception: " + e);
                byteBuffer = null;
            }
        }
        log("onHookCommonMsgSync ret = " + byteBuffer);
        return byteBuffer;
    }

    class QcrilHookCbHandler implements InvocationHandler {
        QcrilHookCbHandler() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            if ("onQcRilHookReady".equals(method.getName())) {
                ModemDsdaUtils.log("QcRilHook is Ready");
                ModemDsdaUtils.this.mHandler.sendEmptyMessage(0);
                return null;
            }
            if (!"onQcRilHookDisconnected".equals(method.getName())) {
                return null;
            }
            ModemDsdaUtils.log("QcRilHook is disconnected");
            ModemDsdaUtils.this.mQcrilHookReady = false;
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("ModemDsdaUtils", str);
    }
}
