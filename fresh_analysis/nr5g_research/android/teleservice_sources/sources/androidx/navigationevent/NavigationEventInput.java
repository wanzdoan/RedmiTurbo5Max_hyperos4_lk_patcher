package androidx.navigationevent;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;

/* JADX INFO: compiled from: NavigationEventInput.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class NavigationEventInput {
    private NavigationEventDispatcher dispatcher;
    private boolean isPredictiveBackInProgress;

    protected void onAdded(NavigationEventDispatcher navigationEventDispatcher) {
        navigationEventDispatcher.getClass();
    }

    protected void onHasEnabledHandlersChanged(boolean z) {
    }

    protected void onHistoryChanged(NavigationEventHistory navigationEventHistory) {
        navigationEventHistory.getClass();
    }

    public final NavigationEventDispatcher getDispatcher$navigationevent() {
        return this.dispatcher;
    }

    public final void setDispatcher$navigationevent(NavigationEventDispatcher navigationEventDispatcher) {
        this.dispatcher = navigationEventDispatcher;
    }

    public final void doOnAdded$navigationevent(NavigationEventDispatcher navigationEventDispatcher) {
        navigationEventDispatcher.getClass();
        onAdded(navigationEventDispatcher);
    }

    public final void doOnHasEnabledHandlersChanged$navigationevent(boolean z) {
        onHasEnabledHandlersChanged(z);
    }

    public final void doOnHistoryChanged$navigationevent(NavigationEventHistory navigationEventHistory) {
        navigationEventHistory.getClass();
        onHistoryChanged(navigationEventHistory);
    }

    protected final void dispatchOnBackStarted(NavigationEvent navigationEvent) {
        navigationEvent.getClass();
        NavigationEventDispatcher navigationEventDispatcher = this.dispatcher;
        if (navigationEventDispatcher == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("This input is not added to any dispatcher.");
        } else {
            if (this.isPredictiveBackInProgress) {
                return;
            }
            navigationEventDispatcher.dispatchOnStarted$navigationevent(this, -1, navigationEvent);
            this.isPredictiveBackInProgress = true;
        }
    }

    protected final void dispatchOnBackProgressed(NavigationEvent navigationEvent) {
        navigationEvent.getClass();
        NavigationEventDispatcher navigationEventDispatcher = this.dispatcher;
        if (navigationEventDispatcher == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("This input is not added to any dispatcher.");
        } else if (this.isPredictiveBackInProgress) {
            navigationEventDispatcher.dispatchOnProgressed$navigationevent(this, -1, navigationEvent);
        }
    }

    protected final void dispatchOnBackCancelled() {
        NavigationEventDispatcher navigationEventDispatcher = this.dispatcher;
        if (navigationEventDispatcher == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("This input is not added to any dispatcher.");
            return;
        }
        if (!this.isPredictiveBackInProgress) {
            navigationEventDispatcher.dispatchOnStarted$navigationevent(this, -1, null);
        }
        navigationEventDispatcher.dispatchOnCancelled$navigationevent(this, -1);
        this.isPredictiveBackInProgress = false;
    }

    protected final void dispatchOnBackCompleted() {
        NavigationEventDispatcher navigationEventDispatcher = this.dispatcher;
        if (navigationEventDispatcher == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("This input is not added to any dispatcher.");
            return;
        }
        if (!this.isPredictiveBackInProgress) {
            navigationEventDispatcher.dispatchOnStarted$navigationevent(this, -1, null);
        }
        navigationEventDispatcher.dispatchOnCompleted$navigationevent(this, -1);
        this.isPredictiveBackInProgress = false;
    }
}
