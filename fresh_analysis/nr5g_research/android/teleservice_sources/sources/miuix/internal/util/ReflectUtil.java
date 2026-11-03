package miuix.internal.util;

import android.util.Log;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ReflectUtil {
    public static Object callStaticObjectMethod(Class cls, Class cls2, String str, Class[] clsArr, Object... objArr) {
        if (cls == null) {
            return null;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(null, objArr);
        } catch (Exception e) {
            Log.e("ReflectUtil", "Failed to call static method:" + str, e);
            return null;
        }
    }

    public static Class getClass(String str) {
        try {
            return Class.forName(str);
        } catch (Exception e) {
            Log.e("ReflectUtil", "Cant find class " + str, e);
            return null;
        }
    }
}
