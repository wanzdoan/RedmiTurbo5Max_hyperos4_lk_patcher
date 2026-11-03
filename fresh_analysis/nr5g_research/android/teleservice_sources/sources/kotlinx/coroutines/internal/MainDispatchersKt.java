package kotlinx.coroutines.internal;

import java.util.List;
import kotlin.KotlinNothingValueException;
import kotlinx.coroutines.MainCoroutineDispatcher;

/* JADX INFO: compiled from: MainDispatchers.kt */
/* JADX INFO: loaded from: classes2.dex */
public abstract class MainDispatchersKt {
    public static final MainCoroutineDispatcher tryCreateDispatcher(MainDispatcherFactory mainDispatcherFactory, List list) throws Throwable {
        mainDispatcherFactory.getClass();
        list.getClass();
        try {
            return mainDispatcherFactory.createDispatcher(list);
        } catch (Throwable th) {
            createMissingDispatcher(th, mainDispatcherFactory.hintOnError());
            return null;
        }
    }

    static /* synthetic */ MissingMainCoroutineDispatcher createMissingDispatcher$default(Throwable th, String str, int i, Object obj) throws Throwable {
        if ((i & 1) != 0) {
            th = null;
        }
        if ((i & 2) != 0) {
            str = null;
        }
        createMissingDispatcher(th, str);
        return null;
    }

    private static final MissingMainCoroutineDispatcher createMissingDispatcher(Throwable th, String str) throws Throwable {
        if (th != null) {
            throw th;
        }
        throwMissingMainDispatcherException();
        throw new KotlinNothingValueException();
    }

    public static final Void throwMissingMainDispatcherException() {
        throw new IllegalStateException("Module with the Main dispatcher is missing. Add dependency providing the Main dispatcher, e.g. 'kotlinx-coroutines-android' and ensure it has the same version as 'kotlinx-coroutines-core'");
    }
}
