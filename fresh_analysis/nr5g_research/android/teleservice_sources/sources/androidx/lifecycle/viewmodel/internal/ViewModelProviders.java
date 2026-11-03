package androidx.lifecycle.viewmodel.internal;

import androidx.lifecycle.ViewModel;
import kotlin.reflect.KClass;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: compiled from: ViewModelProviders.kt */
/* JADX INFO: loaded from: classes.dex */
public final class ViewModelProviders {
    public static final ViewModelProviders INSTANCE = new ViewModelProviders();

    private ViewModelProviders() {
    }

    public final String getDefaultKey$lifecycle_viewmodel(KClass kClass) {
        kClass.getClass();
        String canonicalName = CanonicalName_jvmKt.getCanonicalName(kClass);
        if (canonicalName == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Local and anonymous classes can not be ViewModels");
            return null;
        }
        return "androidx.lifecycle.ViewModelProvider.DefaultKey:".concat(canonicalName);
    }

    public final ViewModel unsupportedCreateViewModel$lifecycle_viewmodel() {
        throw new UnsupportedOperationException("`Factory.create(String, CreationExtras)` is not implemented. You may need to override the method and provide a custom implementation. Note that using `Factory.create(String)` is not supported and considered an error.");
    }
}
