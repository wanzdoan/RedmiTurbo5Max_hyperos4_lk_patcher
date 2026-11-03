package miuix.animation.utils;

import android.util.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* JADX INFO: loaded from: classes2.dex */
public class StyleComposer {
    private static final String TAG = "StyleComposer";

    public interface IInterceptor {
        Object onMethod(Method method, Object[] objArr, Object... objArr2);

        boolean shouldIntercept(Method method, Object[] objArr);
    }

    public static Object compose(Class cls, Object... objArr) {
        return compose(cls, null, objArr);
    }

    public static Object compose(final Class cls, final IInterceptor iInterceptor, final Object... objArr) {
        Object objNewProxyInstance = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() { // from class: miuix.animation.utils.StyleComposer.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object obj, Method method, Object[] objArr2) {
                Object objOnMethod;
                IInterceptor iInterceptor2 = iInterceptor;
                if (iInterceptor2 != null && iInterceptor2.shouldIntercept(method, objArr2)) {
                    objOnMethod = iInterceptor.onMethod(method, objArr2, objArr);
                } else {
                    Object objInvoke = null;
                    for (Object obj2 : objArr) {
                        try {
                            objInvoke = method.invoke(obj2, objArr2);
                        } catch (Exception e) {
                            Log.w(StyleComposer.TAG, "failed to invoke " + method + " for " + obj2, e.getCause());
                        }
                    }
                    objOnMethod = objInvoke;
                }
                if (objOnMethod != null) {
                    Object[] objArr3 = objArr;
                    if (objOnMethod == objArr3[objArr3.length - 1]) {
                        return cls.cast(obj);
                    }
                }
                return objOnMethod;
            }
        });
        if (cls.isInstance(objNewProxyInstance)) {
            return cls.cast(objNewProxyInstance);
        }
        return null;
    }
}
