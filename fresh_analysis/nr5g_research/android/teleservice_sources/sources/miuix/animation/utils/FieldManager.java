package miuix.animation.utils;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import android.util.ArrayMap;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class FieldManager {
    static final String GET = "get";
    static final String SET = "set";
    Map mMethodMap = new ArrayMap();
    Map mFieldMap = new ArrayMap();

    class MethodInfo {
        Method method;

        MethodInfo() {
        }
    }

    class FieldInfo {
        Field field;

        FieldInfo() {
        }
    }

    public synchronized Object getField(Object obj, String str, Class cls) {
        if (obj != null && str != null) {
            try {
                if (str.length() != 0) {
                    MethodInfo method = (MethodInfo) this.mMethodMap.get(str);
                    if (method == null) {
                        method = getMethod(obj, getMethodName(str, GET), this.mMethodMap, new Class[0]);
                    }
                    Method method2 = method.method;
                    if (method2 != null) {
                        return retToClz(invokeMethod(obj, method2, new Object[0]), cls);
                    }
                    FieldInfo field = (FieldInfo) this.mFieldMap.get(str);
                    if (field == null) {
                        field = getField(obj, str, cls, this.mFieldMap);
                    }
                    Field field2 = field.field;
                    if (field2 == null) {
                        return null;
                    }
                    return getValueByField(obj, field2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return null;
    }

    public synchronized boolean setField(Object obj, String str, Class cls, Object obj2) {
        if (obj != null && str != null) {
            try {
                if (str.length() != 0) {
                    MethodInfo method = (MethodInfo) this.mMethodMap.get(str);
                    if (method == null) {
                        method = getMethod(obj, getMethodName(str, SET), this.mMethodMap, cls);
                    }
                    Method method2 = method.method;
                    if (method2 != null) {
                        invokeMethod(obj, method2, obj2);
                        return true;
                    }
                    FieldInfo field = (FieldInfo) this.mFieldMap.get(str);
                    if (field == null) {
                        field = getField(obj, str, cls, this.mFieldMap);
                    }
                    Field field2 = field.field;
                    if (field2 == null) {
                        return false;
                    }
                    setValueByField(obj, field2, obj2);
                    return true;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return false;
    }

    static Object retToClz(Object obj, Class cls) {
        if (!(obj instanceof Number)) {
            return null;
        }
        Number number = (Number) obj;
        if (cls == Float.class || cls == Float.TYPE) {
            return Float.valueOf(number.floatValue());
        }
        if (cls == Integer.class || cls == Integer.TYPE) {
            return Integer.valueOf(number.intValue());
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("getPropertyValue, clz must be float or int instead of ", cls);
        return null;
    }

    static String getMethodName(String str, String str2) {
        return str2 + Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    static Object getValueByField(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (Exception unused) {
            return null;
        }
    }

    static void setValueByField(Object obj, Field field, Object obj2) {
        try {
            field.set(obj, obj2);
        } catch (Exception unused) {
        }
    }

    static MethodInfo getMethod(Object obj, String str, Map map, Class... clsArr) {
        MethodInfo methodInfo = (MethodInfo) map.get(str);
        if (methodInfo != null) {
            return methodInfo;
        }
        MethodInfo methodInfo2 = new MethodInfo();
        methodInfo2.method = getMethod(obj, str, clsArr);
        map.put(str, methodInfo2);
        return methodInfo2;
    }

    static Method getMethod(Object obj, String str, Class... clsArr) {
        try {
            try {
                Method declaredMethod = obj.getClass().getDeclaredMethod(str, clsArr);
                declaredMethod.setAccessible(true);
                return declaredMethod;
            } catch (NoSuchMethodException unused) {
                return obj.getClass().getMethod(str, clsArr);
            }
        } catch (NoSuchMethodException unused2) {
            return null;
        }
    }

    static FieldInfo getField(Object obj, String str, Class cls, Map map) {
        FieldInfo fieldInfo = (FieldInfo) map.get(str);
        if (fieldInfo != null) {
            return fieldInfo;
        }
        FieldInfo fieldInfo2 = new FieldInfo();
        fieldInfo2.field = getFieldByType(obj, str, cls);
        map.put(str, fieldInfo2);
        return fieldInfo2;
    }

    static Field getFieldByType(Object obj, String str, Class cls) {
        Field field;
        try {
            field = obj.getClass().getDeclaredField(str);
            try {
                field.setAccessible(true);
            } catch (NoSuchFieldException unused) {
                try {
                    field = obj.getClass().getField(str);
                } catch (NoSuchFieldException unused2) {
                }
            }
        } catch (NoSuchFieldException unused3) {
            field = null;
        }
        if (field == null || field.getType() == cls) {
            return field;
        }
        return null;
    }

    static Object invokeMethod(Object obj, Method method, Object... objArr) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(obj, objArr);
        } catch (Exception e) {
            Log.d(CommonUtils.TAG, "ValueProperty.invokeMethod failed, " + method.getName(), e);
            return null;
        }
    }
}
