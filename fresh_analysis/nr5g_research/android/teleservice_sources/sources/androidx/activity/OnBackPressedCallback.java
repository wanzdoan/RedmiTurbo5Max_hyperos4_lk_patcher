package androidx.activity;

import androidx.navigationevent.NavigationEvent;
import androidx.navigationevent.NavigationEventHandler;
import androidx.navigationevent.NavigationEventInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: compiled from: OnBackPressedCallback.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class OnBackPressedCallback {
    private boolean isEnabled;
    private final List eventHandlers = new ArrayList();
    private final CopyOnWriteArrayList closeables = new CopyOnWriteArrayList();

    public void handleOnBackCancelled() {
    }

    public abstract void handleOnBackPressed();

    public void handleOnBackProgressed(BackEventCompat backEventCompat) {
        backEventCompat.getClass();
    }

    public void handleOnBackStarted(BackEventCompat backEventCompat) {
        backEventCompat.getClass();
    }

    public OnBackPressedCallback(boolean z) {
        this.isEnabled = z;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }

    public final void setEnabled(boolean z) {
        this.isEnabled = z;
        for (OnBackPressedEventHandler onBackPressedEventHandler : this.eventHandlers) {
            onBackPressedEventHandler.setBackEnabled(onBackPressedEventHandler.isLifecycleActive() && z);
        }
    }

    public final void remove() throws Exception {
        Iterator it = this.closeables.iterator();
        it.getClass();
        while (it.hasNext()) {
            ((AutoCloseable) it.next()).close();
        }
        this.closeables.clear();
        Iterator it2 = this.eventHandlers.iterator();
        while (it2.hasNext()) {
            ((OnBackPressedEventHandler) it2.next()).remove();
        }
        this.eventHandlers.clear();
    }

    public final void addCloseable$activity(AutoCloseable autoCloseable) {
        autoCloseable.getClass();
        this.closeables.add(autoCloseable);
    }

    public final OnBackPressedEventHandler createNavigationEventHandler$activity(NavigationEventInfo navigationEventInfo) {
        navigationEventInfo.getClass();
        OnBackPressedEventHandler onBackPressedEventHandler = new OnBackPressedEventHandler(this, navigationEventInfo);
        this.eventHandlers.add(onBackPressedEventHandler);
        return onBackPressedEventHandler;
    }

    /* JADX INFO: compiled from: OnBackPressedCallback.kt */
    public final class OnBackPressedEventHandler extends NavigationEventHandler {
        private boolean isLifecycleActive;
        private final OnBackPressedCallback onBackPressedCallback;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OnBackPressedEventHandler(OnBackPressedCallback onBackPressedCallback, NavigationEventInfo navigationEventInfo) {
            super(navigationEventInfo, onBackPressedCallback.isEnabled());
            onBackPressedCallback.getClass();
            navigationEventInfo.getClass();
            this.onBackPressedCallback = onBackPressedCallback;
            this.isLifecycleActive = true;
        }

        public final boolean isLifecycleActive() {
            return this.isLifecycleActive;
        }

        public final void setLifecycleActive(boolean z) {
            this.isLifecycleActive = z;
            setBackEnabled(z && this.onBackPressedCallback.isEnabled());
        }

        @Override // androidx.navigationevent.NavigationEventHandler
        protected void onBackStarted(NavigationEvent navigationEvent) {
            navigationEvent.getClass();
            this.onBackPressedCallback.handleOnBackStarted(new BackEventCompat(navigationEvent));
        }

        @Override // androidx.navigationevent.NavigationEventHandler
        protected void onBackProgressed(NavigationEvent navigationEvent) {
            navigationEvent.getClass();
            this.onBackPressedCallback.handleOnBackProgressed(new BackEventCompat(navigationEvent));
        }

        @Override // androidx.navigationevent.NavigationEventHandler
        protected void onBackCompleted() {
            this.onBackPressedCallback.handleOnBackPressed();
        }

        @Override // androidx.navigationevent.NavigationEventHandler
        protected void onBackCancelled() {
            this.onBackPressedCallback.handleOnBackCancelled();
        }
    }
}
