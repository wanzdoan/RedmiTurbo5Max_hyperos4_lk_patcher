package androidx.lifecycle;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.os.Bundle;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import kotlin.text.CharsKt;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: compiled from: SavedStateHandleSupport.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class SavedStateHandleSupport {
    public static final CreationExtras.Key DEFAULT_ARGS_KEY;
    public static final CreationExtras.Key SAVED_STATE_REGISTRY_OWNER_KEY;
    public static final CreationExtras.Key VIEW_MODEL_STORE_OWNER_KEY;

    public static final void enableSavedStateHandles(SavedStateRegistryOwner savedStateRegistryOwner) {
        savedStateRegistryOwner.getClass();
        Lifecycle.State currentState = savedStateRegistryOwner.getLifecycle().getCurrentState();
        if (currentState != Lifecycle.State.INITIALIZED && currentState != Lifecycle.State.CREATED) {
            throw new IllegalArgumentException(("Failed to enable `SavedStateHandle` for `" + savedStateRegistryOwner + "`. The `Lifecycle.State` must be `INITIALIZED` or `CREATED`, but was `" + currentState + "`. You must call `enableSavedStateHandles()` before the `Lifecycle.State` moves to `STARTED`.").toString());
        }
        if (savedStateRegistryOwner.getSavedStateRegistry().getSavedStateProvider("androidx.lifecycle.internal.SavedStateHandlesProvider") == null) {
            SavedStateHandlesProvider savedStateHandlesProvider = new SavedStateHandlesProvider(savedStateRegistryOwner.getSavedStateRegistry(), (ViewModelStoreOwner) savedStateRegistryOwner);
            savedStateRegistryOwner.getSavedStateRegistry().registerSavedStateProvider("androidx.lifecycle.internal.SavedStateHandlesProvider", savedStateHandlesProvider);
            savedStateRegistryOwner.getLifecycle().addObserver(new SavedStateHandleAttacher(savedStateHandlesProvider));
        }
    }

    private static final SavedStateHandle createSavedStateHandle(SavedStateRegistryOwner savedStateRegistryOwner, ViewModelStoreOwner viewModelStoreOwner, String str, Bundle bundle) {
        SavedStateHandlesProvider savedStateHandlesProvider = getSavedStateHandlesProvider(savedStateRegistryOwner);
        SavedStateHandlesVM savedStateHandlesVM = getSavedStateHandlesVM(viewModelStoreOwner);
        SavedStateHandle savedStateHandle = (SavedStateHandle) savedStateHandlesVM.getHandles().get(str);
        if (savedStateHandle != null) {
            return savedStateHandle;
        }
        SavedStateHandle savedStateHandleCreateHandle = SavedStateHandle.Companion.createHandle(savedStateHandlesProvider.consumeRestoredStateForKey(str), bundle);
        savedStateHandlesVM.getHandles().put(str, savedStateHandleCreateHandle);
        return savedStateHandleCreateHandle;
    }

    public static final SavedStateHandle createSavedStateHandle(CreationExtras creationExtras) {
        creationExtras.getClass();
        SavedStateRegistryOwner savedStateRegistryOwner = (SavedStateRegistryOwner) creationExtras.get(SAVED_STATE_REGISTRY_OWNER_KEY);
        if (savedStateRegistryOwner == null) {
            Folme$$ExternalSyntheticBUOutline0.m("CreationExtras must have a value by `SAVED_STATE_REGISTRY_OWNER_KEY`");
            return null;
        }
        ViewModelStoreOwner viewModelStoreOwner = (ViewModelStoreOwner) creationExtras.get(VIEW_MODEL_STORE_OWNER_KEY);
        if (viewModelStoreOwner == null) {
            Folme$$ExternalSyntheticBUOutline0.m("CreationExtras must have a value by `VIEW_MODEL_STORE_OWNER_KEY`");
            return null;
        }
        Bundle bundle = (Bundle) creationExtras.get(DEFAULT_ARGS_KEY);
        String str = (String) creationExtras.get(ViewModelProvider.VIEW_MODEL_KEY);
        if (str == null) {
            Folme$$ExternalSyntheticBUOutline0.m("CreationExtras must have a value by `VIEW_MODEL_KEY`");
            return null;
        }
        return createSavedStateHandle(savedStateRegistryOwner, viewModelStoreOwner, str, bundle);
    }

    public static final SavedStateHandlesVM getSavedStateHandlesVM(ViewModelStoreOwner viewModelStoreOwner) {
        viewModelStoreOwner.getClass();
        return (SavedStateHandlesVM) ViewModelProvider.Companion.create$default(ViewModelProvider.Companion, viewModelStoreOwner, new ViewModelProvider.Factory() { // from class: androidx.lifecycle.SavedStateHandleSupport$savedStateHandlesVM$1
            @Override // androidx.lifecycle.ViewModelProvider.Factory
            public ViewModel create(KClass kClass, CreationExtras creationExtras) {
                kClass.getClass();
                creationExtras.getClass();
                return new SavedStateHandlesVM();
            }
        }, null, 4, null).get("androidx.lifecycle.internal.SavedStateHandlesVM", Reflection.getOrCreateKotlinClass(SavedStateHandlesVM.class));
    }

    public static final SavedStateHandlesProvider getSavedStateHandlesProvider(SavedStateRegistryOwner savedStateRegistryOwner) {
        savedStateRegistryOwner.getClass();
        SavedStateRegistry.SavedStateProvider savedStateProvider = savedStateRegistryOwner.getSavedStateRegistry().getSavedStateProvider("androidx.lifecycle.internal.SavedStateHandlesProvider");
        SavedStateHandlesProvider savedStateHandlesProvider = savedStateProvider instanceof SavedStateHandlesProvider ? (SavedStateHandlesProvider) savedStateProvider : null;
        if (savedStateHandlesProvider != null) {
            return savedStateHandlesProvider;
        }
        OrtUtil$$ExternalSyntheticBUOutline0.m("enableSavedStateHandles() wasn't called prior to createSavedStateHandle() call");
        return null;
    }

    static {
        CreationExtras.Companion companion = CreationExtras.Companion;
        SAVED_STATE_REGISTRY_OWNER_KEY = new CreationExtras.Key() { // from class: androidx.lifecycle.SavedStateHandleSupport$special$$inlined$Key$1
            public String toString() {
                String string = Integer.toString(hashCode(), CharsKt.checkRadix(16));
                string.getClass();
                return "CreationExtras.Key@" + string + '<' + Reflection.getOrCreateKotlinClass(SavedStateRegistryOwner.class).getSimpleName() + '>';
            }
        };
        VIEW_MODEL_STORE_OWNER_KEY = new CreationExtras.Key() { // from class: androidx.lifecycle.SavedStateHandleSupport$special$$inlined$Key$2
            public String toString() {
                String string = Integer.toString(hashCode(), CharsKt.checkRadix(16));
                string.getClass();
                return "CreationExtras.Key@" + string + '<' + Reflection.getOrCreateKotlinClass(ViewModelStoreOwner.class).getSimpleName() + '>';
            }
        };
        DEFAULT_ARGS_KEY = new CreationExtras.Key() { // from class: androidx.lifecycle.SavedStateHandleSupport$special$$inlined$Key$3
            public String toString() {
                String string = Integer.toString(hashCode(), CharsKt.checkRadix(16));
                string.getClass();
                return "CreationExtras.Key@" + string + '<' + Reflection.getOrCreateKotlinClass(Bundle.class).getSimpleName() + '>';
            }
        };
    }
}
