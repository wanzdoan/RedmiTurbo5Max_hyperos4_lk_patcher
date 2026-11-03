package androidx.savedstate;

import android.view.View;

/* JADX INFO: compiled from: ViewTreeSavedStateRegistryOwner.android.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class ViewTreeSavedStateRegistryOwner {
    public static final void set(View view, SavedStateRegistryOwner savedStateRegistryOwner) {
        view.getClass();
        view.setTag(R$id.view_tree_saved_state_registry_owner, savedStateRegistryOwner);
    }
}
