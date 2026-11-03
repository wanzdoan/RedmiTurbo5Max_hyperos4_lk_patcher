package androidx.savedstate;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.os.Bundle;
import androidx.savedstate.internal.SavedStateRegistryImpl;

/* JADX INFO: compiled from: SavedStateRegistry.android.kt */
/* JADX INFO: loaded from: classes.dex */
public final class SavedStateRegistry {
    private final SavedStateRegistryImpl impl;
    private Recreator.SavedStateProvider recreatorProvider;

    /* JADX INFO: compiled from: SavedStateRegistry.android.kt */
    public interface AutoRecreated {
        void onRecreated(SavedStateRegistryOwner savedStateRegistryOwner);
    }

    /* JADX INFO: compiled from: SavedStateRegistry.android.kt */
    public interface SavedStateProvider {
        Bundle saveState();
    }

    public SavedStateRegistry(SavedStateRegistryImpl savedStateRegistryImpl) {
        savedStateRegistryImpl.getClass();
        this.impl = savedStateRegistryImpl;
    }

    public final Bundle consumeRestoredStateForKey(String str) {
        str.getClass();
        return this.impl.consumeRestoredStateForKey(str);
    }

    public final void registerSavedStateProvider(String str, SavedStateProvider savedStateProvider) {
        str.getClass();
        savedStateProvider.getClass();
        this.impl.registerSavedStateProvider(str, savedStateProvider);
    }

    public final SavedStateProvider getSavedStateProvider(String str) {
        str.getClass();
        return this.impl.getSavedStateProvider(str);
    }

    public final void runOnNextRecreation(Class cls) {
        cls.getClass();
        if (!this.impl.isAllowingSavingState$savedstate()) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Can not perform this action after onSaveInstanceState");
            return;
        }
        Recreator.SavedStateProvider savedStateProvider = this.recreatorProvider;
        if (savedStateProvider == null) {
            savedStateProvider = new Recreator.SavedStateProvider(this);
        }
        this.recreatorProvider = savedStateProvider;
        try {
            Class[] clsArr = new Class[0];
            cls.getDeclaredConstructor(null);
            Recreator.SavedStateProvider savedStateProvider2 = this.recreatorProvider;
            if (savedStateProvider2 != null) {
                savedStateProvider2.add(cls.getName());
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class " + cls.getSimpleName() + " must have default constructor in order to be automatically recreated", e);
        }
    }
}
