package androidx.lifecycle;

import android.view.View;
import androidx.lifecycle.runtime.R$id;

/* JADX INFO: compiled from: ViewTreeLifecycleOwner.android.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class ViewTreeLifecycleOwner {
    public static final void set(View view, LifecycleOwner lifecycleOwner) {
        view.getClass();
        view.setTag(R$id.view_tree_lifecycle_owner, lifecycleOwner);
    }
}
