package miuix.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Reflects {
    public static Method getMethod(Class cls, String str, Class... clsArr) {
        try {
            Method method = cls.getMethod(str, clsArr);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        }
    }

    public static Field getField(Class cls, String str) {
        try {
            Field field = cls.getField(str);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        }
    }

    public static Field getDeclaredField(Class cls, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (NoSuchFieldException e) {
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        }
    }

    public static Object get(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        }
    }

    public static Object invoke(Object obj, Method method, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        } catch (InvocationTargetException e2) {
            Reflects$$ExternalSyntheticBUOutline0.m(e2);
            return null;
        }
    }

    public static Class forName(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        }
    }
}
