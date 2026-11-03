package miuix.reflect;

import com.ot.pubsub.a.b;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ReflectionHelper {
    private static Class[] PRIMITIVE_CLASSES;
    static HashMap PRIMITIVE_TYPE;
    private static String[] SIGNATURE_OF_PRIMTIVE_CLASSES;
    private static Map sConstructorCache;
    private static Map sFieldCache;
    private static Method sForNameMethod;
    private static Method sGetDeclaredConstructorMethod;
    private static Method sGetDeclaredFieldMethod;
    private static Method sGetDeclaredMethodMethod;
    private static Method sGetMethod;
    private static Method sInvokeMethod;
    private static Map sMethodCache;
    private static Method sNewInstanceMethod;
    private static Method sSetAccessibleMethod;
    private static Method sSetMethod;

    static {
        HashMap map = new HashMap();
        PRIMITIVE_TYPE = map;
        Class cls = Byte.TYPE;
        map.put("byte", cls);
        HashMap map2 = PRIMITIVE_TYPE;
        Class cls2 = Short.TYPE;
        map2.put("short", cls2);
        HashMap map3 = PRIMITIVE_TYPE;
        Class cls3 = Integer.TYPE;
        map3.put("int", cls3);
        HashMap map4 = PRIMITIVE_TYPE;
        Class cls4 = Long.TYPE;
        map4.put("long", cls4);
        HashMap map5 = PRIMITIVE_TYPE;
        Class cls5 = Character.TYPE;
        map5.put("char", cls5);
        HashMap map6 = PRIMITIVE_TYPE;
        Class cls6 = Boolean.TYPE;
        map6.put("boolean", cls6);
        HashMap map7 = PRIMITIVE_TYPE;
        Class cls7 = Float.TYPE;
        map7.put("float", cls7);
        HashMap map8 = PRIMITIVE_TYPE;
        Class cls8 = Double.TYPE;
        map8.put("double", cls8);
        PRIMITIVE_TYPE.put("byte[]", byte[].class);
        PRIMITIVE_TYPE.put("short[]", short[].class);
        PRIMITIVE_TYPE.put("int[]", int[].class);
        PRIMITIVE_TYPE.put("long[]", long[].class);
        PRIMITIVE_TYPE.put("char[]", char[].class);
        PRIMITIVE_TYPE.put("boolean[]", boolean[].class);
        PRIMITIVE_TYPE.put("float[]", float[].class);
        PRIMITIVE_TYPE.put("double[]", double[].class);
        PRIMITIVE_CLASSES = new Class[]{cls6, cls, cls5, cls2, cls3, cls4, cls7, cls8, Void.TYPE};
        SIGNATURE_OF_PRIMTIVE_CLASSES = new String[]{"Z", b.a, "C", "S", "I", "J", "F", "D", "V"};
        sMethodCache = new HashMap();
        sFieldCache = new HashMap();
        sConstructorCache = new HashMap();
        sInvokeMethod = null;
        sGetDeclaredFieldMethod = null;
        sGetDeclaredMethodMethod = null;
        sSetAccessibleMethod = null;
        sGetDeclaredConstructorMethod = null;
        sNewInstanceMethod = null;
        sForNameMethod = null;
        sSetMethod = null;
        sGetMethod = null;
    }

    private static Object invokeInternal(Object obj, Object... objArr) {
        if (sInvokeMethod == null) {
            sInvokeMethod = Method.class.getMethod("invoke", Object.class, Object[].class);
        }
        return sInvokeMethod.invoke(obj, objArr);
    }

    private static Field getDeclaredFieldInternal(Object obj, String str) {
        if (sGetDeclaredFieldMethod == null) {
            sGetDeclaredFieldMethod = Class.class.getMethod("getDeclaredField", String.class);
        }
        return (Field) sGetDeclaredFieldMethod.invoke(obj, str);
    }

    private static Method getDeclaredMethodInternal(Object obj, String str, Class... clsArr) {
        if (sGetDeclaredMethodMethod == null) {
            sGetDeclaredMethodMethod = Class.class.getMethod("getDeclaredMethod", String.class, Class[].class);
        }
        return (Method) sGetDeclaredMethodMethod.invoke(obj, str, clsArr);
    }

    private static void setAccessibleInternal(Object obj, boolean z) throws IllegalAccessException, InvocationTargetException {
        if (sSetAccessibleMethod == null) {
            sSetAccessibleMethod = AccessibleObject.class.getMethod("setAccessible", Boolean.TYPE);
        }
        sSetAccessibleMethod.invoke(obj, Boolean.valueOf(z));
    }

    private static Constructor getDeclaredConstructorInternal(Object obj, Class... clsArr) {
        if (sGetDeclaredConstructorMethod == null) {
            sGetDeclaredConstructorMethod = Class.class.getMethod("getDeclaredConstructor", Class[].class);
        }
        return (Constructor) sGetDeclaredConstructorMethod.invoke(obj, clsArr);
    }

    private static Object newInstanceInternal(Object obj, Object... objArr) {
        if (sNewInstanceMethod == null) {
            sNewInstanceMethod = Constructor.class.getMethod("newInstance", Object[].class);
        }
        return sNewInstanceMethod.invoke(obj, objArr);
    }

    private static void setInternal(Object obj, Object obj2, Object obj3) throws IllegalAccessException, InvocationTargetException {
        if (sSetMethod == null) {
            sSetMethod = Field.class.getMethod("set", Object.class, Object.class);
        }
        sSetMethod.invoke(obj, obj2, obj3);
    }

    private static Object getInternal(Object obj, Object obj2) {
        if (sGetMethod == null) {
            sGetMethod = Field.class.getMethod("get", Object.class);
        }
        return sGetMethod.invoke(obj, obj2);
    }

    public static void invoke(Class cls, Object obj, String str, Class[] clsArr, Object... objArr) throws IllegalAccessException, InvocationTargetException {
        Method method = getMethod(cls, str, clsArr);
        if (method != null) {
            invokeInternal(method, obj, objArr);
        }
    }

    public static Object invokeObject(Class cls, Object obj, String str, Class[] clsArr, Object... objArr) throws IllegalAccessException, InvocationTargetException {
        Method method = getMethod(cls, str, clsArr);
        if (method != null) {
            return invokeInternal(method, obj, objArr);
        }
        return null;
    }

    public static Field getField(Class cls, String str) throws IllegalAccessException, InvocationTargetException {
        String strGenerateFieldCacheKey = generateFieldCacheKey(cls, str);
        Field field = (Field) sFieldCache.get(strGenerateFieldCacheKey);
        if (field != null) {
            return field;
        }
        Field declaredFieldInternal = getDeclaredFieldInternal(cls, str);
        setAccessibleInternal(declaredFieldInternal, true);
        sFieldCache.put(strGenerateFieldCacheKey, declaredFieldInternal);
        return declaredFieldInternal;
    }

    public static void setFieldValue(Class cls, Object obj, String str, Object obj2) throws IllegalAccessException, InvocationTargetException {
        Field field = getField(cls, str);
        if (field != null) {
            setInternal(field, obj, obj2);
        }
    }

    public static Object getFieldValue(Class cls, Object obj, String str) throws IllegalAccessException, InvocationTargetException {
        Field field = getField(cls, str);
        if (field == null) {
            return null;
        }
        return getInternal(field, obj);
    }

    public static Method getMethod(Class cls, String str, Class... clsArr) throws IllegalAccessException, InvocationTargetException {
        String strGenerateMethodCacheKey = generateMethodCacheKey(cls, str, clsArr);
        Method method = (Method) sMethodCache.get(strGenerateMethodCacheKey);
        if (method != null) {
            return method;
        }
        Method declaredMethodInternal = getDeclaredMethodInternal(cls, str, clsArr);
        setAccessibleInternal(declaredMethodInternal, true);
        sMethodCache.put(strGenerateMethodCacheKey, declaredMethodInternal);
        return declaredMethodInternal;
    }

    public static Object getConstructorInstance(Class cls, Class[] clsArr, Object... objArr) throws IllegalAccessException, InvocationTargetException {
        Constructor constructor = getConstructor(cls, clsArr);
        if (constructor == null) {
            return null;
        }
        return newInstanceInternal(constructor, objArr);
    }

    public static Constructor getConstructor(Class cls, Class... clsArr) throws IllegalAccessException, InvocationTargetException {
        String strGenerateConstructorCacheKey = generateConstructorCacheKey(cls, clsArr);
        Constructor constructor = (Constructor) sConstructorCache.get(strGenerateConstructorCacheKey);
        if (constructor != null) {
            return constructor;
        }
        Constructor declaredConstructorInternal = getDeclaredConstructorInternal(cls, clsArr);
        setAccessibleInternal(declaredConstructorInternal, true);
        sConstructorCache.put(strGenerateConstructorCacheKey, declaredConstructorInternal);
        return declaredConstructorInternal;
    }

    private static String generateConstructorCacheKey(Class cls, Class... clsArr) {
        return cls.toString() + "/" + Arrays.toString(clsArr);
    }

    private static String generateMethodCacheKey(Class cls, String str, Class[] clsArr) {
        return cls.toString() + "/" + str + "/" + Arrays.toString(clsArr);
    }

    private static String generateFieldCacheKey(Class cls, String str) {
        return cls.toString() + "/" + str;
    }
}
