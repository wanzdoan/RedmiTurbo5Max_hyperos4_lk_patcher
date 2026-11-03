package androidx.navigationevent;

import android.view.View;

/* JADX INFO: compiled from: ViewTreeNavigationEventDispatcherOwner.android.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class ViewTreeNavigationEventDispatcherOwner {
    public static final void set(View view, NavigationEventDispatcherOwner navigationEventDispatcherOwner) {
        view.getClass();
        view.setTag(R$id.view_tree_navigation_event_dispatcher_owner, navigationEventDispatcherOwner);
    }
}
