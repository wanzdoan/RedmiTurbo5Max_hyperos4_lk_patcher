package miui.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class ReflectBuilderUtil {

    public static class ReflAgent {
        private Class mClass;
        private Object mObject;
        private Object mResult;

        private ReflAgent() {
        }

        public static ReflAgent getClass(String clsStr) {
            ReflAgent reflAgent = new ReflAgent();
            try {
                reflAgent.mClass = Class.forName(clsStr);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return reflAgent;
        }

        public static ReflAgent getClass(Context context, String packageName, String classStr) {
            ReflAgent reflAgent = new ReflAgent();
            try {
                Context c = context.createPackageContext(packageName, 3);
                reflAgent.mClass = c.getClassLoader().loadClass(classStr);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }
            return reflAgent;
        }

        public static ReflAgent getFiveGManagerClass(Context context) {
            return getClass(context, "com.android.phone", "com.android.phone.FiveGManager");
        }

        public static ReflAgent getMiuiEsimManagerClass(Context context) {
            return getClass(context, "com.android.phone", "com.android.phone.MiuiEsimManager");
        }

        public static ReflAgent getMiuiDsdaManagerClass(Context context) {
            return getClass(context, "com.android.phone", "com.android.phone.dsda.MiuiDsdaManager");
        }

        public static ReflAgent getFiveGCloudControllerClass(Context context) {
            return getClass(context, "com.android.phone", "com.android.phone.FiveGCloudController");
        }

        public static ReflAgent getMiuiPhoneUtilsClass(Context context) {
            return getClass(context, "com.android.phone", "com.android.phone.MiuiPhoneUtils");
        }

        public static ReflAgent getObject(Object obj) {
            ReflAgent reflAgent = new ReflAgent();
            if (obj != null) {
                reflAgent.mObject = obj;
                reflAgent.mClass = obj.getClass();
            }
            return reflAgent;
        }

        public ReflAgent newObject(Class<?>[] parameterTypes, Object... values) {
            if (this.mClass != null) {
                try {
                    this.mObject = this.mClass.getConstructor(parameterTypes).newInstance(values);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                } catch (InvocationTargetException e4) {
                    e4.printStackTrace();
                }
            }
            return this;
        }

        public ReflAgent call(String method, Class<?>[] parameterTypes, Object... values) {
            if (this.mObject != null) {
                try {
                    this.mResult = ReflectBuilderUtil.callObjectMethod(this.mObject, method, parameterTypes, values);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e2) {
                    e2.printStackTrace();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                }
            }
            return this;
        }

        public ReflAgent callStatic(String method, Class<?>[] parameterTypes, Object... values) {
            if (this.mClass != null) {
                try {
                    this.mResult = ReflectBuilderUtil.callStaticObjectMethod(this.mClass, method, parameterTypes, values);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e2) {
                    e2.printStackTrace();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                }
            }
            return this;
        }

        public ReflAgent getStaticFiled(String field) {
            if (this.mClass != null) {
                try {
                    this.mResult = ReflectBuilderUtil.getStaticObjectField(this.mClass, field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                }
            }
            return this;
        }

        public ReflAgent getObjectFiled(String field) {
            if (this.mObject != null) {
                try {
                    this.mResult = ReflectBuilderUtil.getObjectField(this.mObject, field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                }
            }
            return this;
        }

        public ReflAgent setResultToSelf() {
            this.mObject = this.mResult;
            this.mResult = null;
            return this;
        }

        public String stringResult() {
            if (this.mResult == null) {
                return null;
            }
            return this.mResult.toString();
        }

        public boolean booleanResult() {
            if (this.mResult == null) {
                return false;
            }
            return ((Boolean) this.mResult).booleanValue();
        }

        public int intResult() {
            if (this.mResult == null) {
                return 0;
            }
            return ((Integer) this.mResult).intValue();
        }

        public long longResult() {
            if (this.mResult == null) {
                return 0L;
            }
            return ((Long) this.mResult).longValue();
        }

        public Intent intentResult() {
            if (this.mResult == null) {
                return null;
            }
            return (Intent) this.mResult;
        }

        public Pair pairResult() {
            if (this.mResult == null) {
                return null;
            }
            return (Pair) this.mResult;
        }

        public Object resultObject() {
            if (this.mResult == null) {
                return null;
            }
            return this.mResult;
        }

        public Object getObject() {
            return this.mObject;
        }
    }

    public static <T> T callObjectMethod(Object obj, Class<T> cls, String str, Class<?>[] clsArr, Object... objArr) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Method declaredMethod = obj.getClass().getDeclaredMethod(str, clsArr);
        declaredMethod.setAccessible(true);
        return (T) declaredMethod.invoke(obj, objArr);
    }

    public static Object callObjectMethod(Object target, String method, Class<?>[] parameterTypes, Object... values) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Method declaredMethod = target.getClass().getDeclaredMethod(method, parameterTypes);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(target, values);
    }

    public static Object callObjectMethod(Class cls, Object target, String method, Class<?>[] parameterTypes, Object... values) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Method declaredMethod = cls.getDeclaredMethod(method, parameterTypes);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(target, values);
    }

    public static Object callObjectMethod(Object target, String method, Class<?> clazz, Class<?>[] parameterTypes, Object... values) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Method declaredMethod = clazz.getDeclaredMethod(method, parameterTypes);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(target, values);
    }

    public static Object callStaticObjectMethod(Class<?> clazz, String method, Class<?>[] parameterTypes, Object... values) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Method declaredMethod = clazz.getDeclaredMethod(method, parameterTypes);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(null, values);
    }

    public static void setObjectField(Object target, String field, Object value) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field declaredField = target.getClass().getDeclaredField(field);
        declaredField.setAccessible(true);
        declaredField.set(target, value);
    }

    public static Object getObjectField(Object target, String field) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field declaredField = target.getClass().getDeclaredField(field);
        declaredField.setAccessible(true);
        return declaredField.get(target);
    }

    public static <T> T getObjectField(Object obj, String str, Class<T> cls) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field declaredField = obj.getClass().getDeclaredField(str);
        declaredField.setAccessible(true);
        return (T) declaredField.get(obj);
    }

    public static void setStaticObjectField(Class<?> clazz, String field, Object value) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field declaredField = clazz.getDeclaredField(field);
        declaredField.setAccessible(true);
        declaredField.set(null, value);
    }

    public static Object getStaticObjectField(Class<?> clazz, String field) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field declaredField = clazz.getDeclaredField(field);
        declaredField.setAccessible(true);
        return declaredField.get(null);
    }

    public static <T> T getStaticObjectField(Class<?> cls, String str, Class<T> cls2) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return (T) declaredField.get(null);
    }

    public static Object newObject(Class<?> clazz, Class<?>[] parameterTypes, Object... values) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        if (clazz != null) {
            return clazz.getConstructor(parameterTypes).newInstance(values);
        }
        return null;
    }
}
