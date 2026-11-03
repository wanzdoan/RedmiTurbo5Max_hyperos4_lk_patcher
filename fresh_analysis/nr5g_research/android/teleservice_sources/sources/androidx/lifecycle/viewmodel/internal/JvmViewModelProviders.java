package androidx.lifecycle.viewmodel.internal;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.ChildHelper$$ExternalSyntheticBUOutline0;
import androidx.savedstate.Recreator$$ExternalSyntheticBUOutline0;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/* JADX INFO: compiled from: JvmViewModelProviders.kt */
/* JADX INFO: loaded from: classes.dex */
public final class JvmViewModelProviders {
    public static final JvmViewModelProviders INSTANCE = new JvmViewModelProviders();

    private JvmViewModelProviders() {
    }

    public final ViewModel createViewModel(Class cls) throws InvocationTargetException {
        cls.getClass();
        try {
            Class[] clsArr = new Class[0];
            Constructor declaredConstructor = cls.getDeclaredConstructor(null);
            if (!Modifier.isPublic(declaredConstructor.getModifiers())) {
                ChildHelper$$ExternalSyntheticBUOutline0.m("Cannot create an instance of ", cls);
                return null;
            }
            try {
                Object objNewInstance = declaredConstructor.newInstance(null);
                objNewInstance.getClass();
                return (ViewModel) objNewInstance;
            } catch (IllegalAccessException e) {
                Recreator$$ExternalSyntheticBUOutline0.m("Cannot create an instance of ", cls, e);
                return null;
            } catch (InstantiationException e2) {
                Recreator$$ExternalSyntheticBUOutline0.m("Cannot create an instance of ", cls, e2);
                return null;
            }
        } catch (NoSuchMethodException e3) {
            Recreator$$ExternalSyntheticBUOutline0.m("Cannot create an instance of ", cls, e3);
            return null;
        }
    }
}
