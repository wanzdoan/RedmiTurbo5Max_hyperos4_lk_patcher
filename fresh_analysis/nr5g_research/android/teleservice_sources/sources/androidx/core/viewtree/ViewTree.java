package androidx.core.viewtree;

import android.view.View;
import android.view.ViewParent;

/* JADX INFO: compiled from: ViewTree.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class ViewTree {
    public static final void setViewTreeDisjointParent(View view, ViewParent viewParent) {
        view.getClass();
        view.setTag(R$id.view_tree_disjoint_parent, viewParent);
    }
}
