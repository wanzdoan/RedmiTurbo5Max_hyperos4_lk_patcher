package kotlinx.coroutines.android;

import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;
import java.lang.reflect.InvocationTargetException;
import kotlin.Result;
import kotlin.ResultKt;

/* JADX INFO: compiled from: HandlerDispatcher.kt */
/* JADX INFO: loaded from: classes2.dex */
public abstract class HandlerDispatcherKt {
    public static final HandlerDispatcher Main;
    private static volatile Choreographer choreographer;

    public static final Handler asHandler(Looper looper, boolean z) throws IllegalAccessException, InvocationTargetException {
        looper.getClass();
        if (!z) {
            return new Handler(looper);
        }
        Object objInvoke = Handler.class.getDeclaredMethod("createAsync", Looper.class).invoke(null, looper);
        objInvoke.getClass();
        return (Handler) objInvoke;
    }

    static {
        Object objM5422constructorimpl;
        try {
            Result.Companion companion = Result.Companion;
            Looper mainLooper = Looper.getMainLooper();
            mainLooper.getClass();
            objM5422constructorimpl = Result.m5422constructorimpl(new HandlerContext(asHandler(mainLooper, true), null, 2, null));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            objM5422constructorimpl = Result.m5422constructorimpl(ResultKt.createFailure(th));
        }
        Main = (HandlerDispatcher) (Result.m5423isFailureimpl(objM5422constructorimpl) ? null : objM5422constructorimpl);
    }
}
