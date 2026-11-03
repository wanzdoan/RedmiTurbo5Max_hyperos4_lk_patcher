package androidx.savedstate.internal;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.os.Bundle;
import androidx.collection.MutableScatterMap;
import androidx.collection.ScatterMapKt;
import androidx.core.os.BundleKt;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry$$ExternalSyntheticBUOutline0;
import androidx.savedstate.SavedStateReader;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import androidx.savedstate.SavedStateWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: SavedStateRegistryImpl.kt */
/* JADX INFO: loaded from: classes.dex */
public final class SavedStateRegistryImpl {
    private static final Companion Companion = new Companion(null);
    private boolean attached;
    private boolean isAllowingSavingState;
    private boolean isRestored;
    private final MutableScatterMap keyToProviders;
    private final SynchronizedObject lock;
    private final Function0 onAttach;
    private final SavedStateRegistryOwner owner;
    private Bundle restoredState;

    public SavedStateRegistryImpl(SavedStateRegistryOwner savedStateRegistryOwner, Function0 function0) {
        savedStateRegistryOwner.getClass();
        function0.getClass();
        this.owner = savedStateRegistryOwner;
        this.onAttach = function0;
        this.lock = new SynchronizedObject();
        this.keyToProviders = ScatterMapKt.mutableScatterMapOf();
        this.isAllowingSavingState = true;
    }

    public final boolean isAllowingSavingState$savedstate() {
        return this.isAllowingSavingState;
    }

    /* JADX WARN: Code duplicated, block: B:29:0x00b3 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:30:0x00b5 A[Catch: all -> 0x00ab, LOOP:0: B:18:0x0073->B:30:0x00b5, LOOP_END, TryCatch #0 {all -> 0x00ab, blocks: (B:15:0x0063, B:18:0x0073, B:20:0x0083, B:22:0x008f, B:24:0x0098, B:27:0x00ad, B:30:0x00b5, B:31:0x00b8), top: B:39:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:42:0x00b8 A[EDGE_INSN: B:42:0x00b8->B:31:0x00b8 BREAK  A[LOOP:0: B:18:0x0073->B:30:0x00b5], SYNTHETIC] */
    public final void performSave$savedstate(Bundle bundle) {
        Pair[] pairArr;
        bundle.getClass();
        Map mapEmptyMap = MapsKt.emptyMap();
        if (mapEmptyMap.isEmpty()) {
            pairArr = new Pair[0];
        } else {
            ArrayList arrayList = new ArrayList(mapEmptyMap.size());
            for (Map.Entry entry : mapEmptyMap.entrySet()) {
                arrayList.add(TuplesKt.to((String) entry.getKey(), entry.getValue()));
            }
            pairArr = (Pair[]) arrayList.toArray(new Pair[0]);
        }
        Bundle bundleBundleOf = BundleKt.bundleOf((Pair[]) Arrays.copyOf(pairArr, pairArr.length));
        Bundle bundleM41constructorimpl = SavedStateWriter.m41constructorimpl(bundleBundleOf);
        Bundle bundle2 = this.restoredState;
        if (bundle2 != null) {
            SavedStateWriter.m42putAllimpl(bundleM41constructorimpl, bundle2);
        }
        synchronized (this.lock) {
            try {
                MutableScatterMap mutableScatterMap = this.keyToProviders;
                Object[] objArr = mutableScatterMap.keys;
                Object[] objArr2 = mutableScatterMap.values;
                long[] jArr = mutableScatterMap.metadata;
                int length = jArr.length - 2;
                if (length >= 0) {
                    int i = 0;
                    while (true) {
                        long j = jArr[i];
                        if ((((~j) << 7) & j & (-9187201950435737472L)) == -9187201950435737472L) {
                            if (i != length) {
                                break;
                                break;
                            }
                            i++;
                        } else {
                            int i2 = 8 - ((~(i - length)) >>> 31);
                            for (int i3 = 0; i3 < i2; i3++) {
                                if ((255 & j) < 128) {
                                    int i4 = (i << 3) + i3;
                                    SavedStateWriter.m43putSavedStateimpl(bundleM41constructorimpl, (String) objArr[i4], ((SavedStateRegistry.SavedStateProvider) objArr2[i4]).saveState());
                                }
                                j >>= 8;
                            }
                            if (i2 != 8) {
                                break;
                            } else if (i != length) {
                                break;
                            } else {
                                i++;
                            }
                        }
                    }
                }
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th) {
                throw th;
            }
        }
        if (SavedStateReader.m39isEmptyimpl(SavedStateReader.m34constructorimpl(bundleBundleOf))) {
            return;
        }
        SavedStateWriter.m43putSavedStateimpl(SavedStateWriter.m41constructorimpl(bundle), "androidx.lifecycle.BundlableSavedStateRegistry.key", bundleBundleOf);
    }

    public final Bundle consumeRestoredStateForKey(String str) {
        str.getClass();
        if (!this.isRestored) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("You can 'consumeRestoredStateForKey' only after the corresponding component has moved to the 'CREATED' state");
            return null;
        }
        Bundle bundle = this.restoredState;
        if (bundle == null) {
            return null;
        }
        Bundle bundleM34constructorimpl = SavedStateReader.m34constructorimpl(bundle);
        Bundle bundleM36getSavedStateimpl = SavedStateReader.m35containsimpl(bundleM34constructorimpl, str) ? SavedStateReader.m36getSavedStateimpl(bundleM34constructorimpl, str) : null;
        SavedStateWriter.m45removeimpl(SavedStateWriter.m41constructorimpl(bundle), str);
        if (SavedStateReader.m39isEmptyimpl(SavedStateReader.m34constructorimpl(bundle))) {
            this.restoredState = null;
        }
        return bundleM36getSavedStateimpl;
    }

    public final void registerSavedStateProvider(String str, SavedStateRegistry.SavedStateProvider savedStateProvider) {
        str.getClass();
        savedStateProvider.getClass();
        synchronized (this.lock) {
            if (!this.keyToProviders.contains(str)) {
                this.keyToProviders.set(str, savedStateProvider);
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalArgumentException("SavedStateProvider with the given key is already registered");
            }
        }
    }

    public final SavedStateRegistry.SavedStateProvider getSavedStateProvider(String str) {
        SavedStateRegistry.SavedStateProvider savedStateProvider;
        str.getClass();
        synchronized (this.lock) {
            savedStateProvider = (SavedStateRegistry.SavedStateProvider) this.keyToProviders.get(str);
        }
        return savedStateProvider;
    }

    public final void performAttach() {
        if (this.owner.getLifecycle().getCurrentState() != Lifecycle.State.INITIALIZED) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Restarter must be created only during owner's initialization stage");
        } else {
            if (this.attached) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("SavedStateRegistry was already attached.");
                return;
            }
            this.onAttach.invoke();
            this.owner.getLifecycle().addObserver(new LifecycleEventObserver() { // from class: androidx.savedstate.internal.SavedStateRegistryImpl$$ExternalSyntheticLambda0
                @Override // androidx.lifecycle.LifecycleEventObserver
                public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                    SavedStateRegistryImpl.m46$r8$lambda$5QegvSvQ4YauZ4gWTh7H_1UcBY(this.f$0, lifecycleOwner, event);
                }
            });
            this.attached = true;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$5QegvSvQ4Y-auZ4gWTh7H_1UcBY, reason: not valid java name */
    public static void m46$r8$lambda$5QegvSvQ4YauZ4gWTh7H_1UcBY(SavedStateRegistryImpl savedStateRegistryImpl, LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        lifecycleOwner.getClass();
        event.getClass();
        if (event == Lifecycle.Event.ON_START) {
            savedStateRegistryImpl.isAllowingSavingState = true;
        } else if (event == Lifecycle.Event.ON_STOP) {
            savedStateRegistryImpl.isAllowingSavingState = false;
        }
    }

    public final void performRestore$savedstate(Bundle bundle) {
        if (!this.attached) {
            performAttach();
        }
        if (this.owner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            LifecycleRegistry$$ExternalSyntheticBUOutline0.m("performRestore cannot be called when owner is ", this.owner.getLifecycle().getCurrentState());
            return;
        }
        if (this.isRestored) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("SavedStateRegistry was already restored.");
            return;
        }
        Bundle bundleM36getSavedStateimpl = null;
        if (bundle != null) {
            Bundle bundleM34constructorimpl = SavedStateReader.m34constructorimpl(bundle);
            if (SavedStateReader.m35containsimpl(bundleM34constructorimpl, "androidx.lifecycle.BundlableSavedStateRegistry.key")) {
                bundleM36getSavedStateimpl = SavedStateReader.m36getSavedStateimpl(bundleM34constructorimpl, "androidx.lifecycle.BundlableSavedStateRegistry.key");
            }
        }
        this.restoredState = bundleM36getSavedStateimpl;
        this.isRestored = true;
    }

    /* JADX INFO: compiled from: SavedStateRegistryImpl.kt */
    final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
