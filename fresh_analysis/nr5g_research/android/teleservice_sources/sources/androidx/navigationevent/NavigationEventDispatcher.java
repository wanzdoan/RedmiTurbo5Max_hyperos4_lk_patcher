package androidx.navigationevent;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import androidx.collection.MutableOrderedScatterSet;
import androidx.collection.OrderedScatterSetKt;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: NavigationEventDispatcher.kt */
/* JADX INFO: loaded from: classes.dex */
public final class NavigationEventDispatcher {
    public static final Companion Companion = new Companion(null);
    private final MutableOrderedScatterSet childDispatchers;
    private final MutableOrderedScatterSet handlers;
    private final MutableOrderedScatterSet inputs;
    private boolean isDisposed;
    private boolean isEnabled;
    private final OnBackCompletedFallback onBackCompletedFallback;
    private NavigationEventDispatcher parent;
    private final NavigationEventProcessor sharedProcessor;

    private NavigationEventDispatcher(NavigationEventDispatcher navigationEventDispatcher, OnBackCompletedFallback onBackCompletedFallback, OnForwardCompletedFallback onForwardCompletedFallback) {
        NavigationEventProcessor navigationEventProcessor;
        this.parent = navigationEventDispatcher;
        this.onBackCompletedFallback = onBackCompletedFallback;
        this.isEnabled = true;
        this.sharedProcessor = (navigationEventDispatcher == null || (navigationEventProcessor = navigationEventDispatcher.sharedProcessor) == null) ? new NavigationEventProcessor() : navigationEventProcessor;
        this.childDispatchers = OrderedScatterSetKt.mutableOrderedScatterSetOf();
        this.handlers = OrderedScatterSetKt.mutableOrderedScatterSetOf();
        this.inputs = OrderedScatterSetKt.mutableOrderedScatterSetOf();
        NavigationEventDispatcher navigationEventDispatcher2 = this.parent;
        if (navigationEventDispatcher2 != null) {
            navigationEventDispatcher2.childDispatchers.plusAssign(this);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public NavigationEventDispatcher(OnBackCompletedFallback onBackCompletedFallback) {
        this(null, onBackCompletedFallback, null);
        onBackCompletedFallback.getClass();
    }

    private final boolean isDisposed() {
        NavigationEventDispatcher navigationEventDispatcher = this.parent;
        if (navigationEventDispatcher == null || !navigationEventDispatcher.isDisposed()) {
            return this.isDisposed;
        }
        return true;
    }

    public final boolean isEnabled() {
        NavigationEventDispatcher navigationEventDispatcher = this.parent;
        if (navigationEventDispatcher == null || navigationEventDispatcher.isEnabled()) {
            return this.isEnabled;
        }
        return false;
    }

    public final NavigationEventProcessor getSharedProcessor$navigationevent() {
        return this.sharedProcessor;
    }

    public static /* synthetic */ void addHandler$default(NavigationEventDispatcher navigationEventDispatcher, NavigationEventHandler navigationEventHandler, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 1;
        }
        navigationEventDispatcher.addHandler(navigationEventHandler, i);
    }

    public final void addHandler(NavigationEventHandler navigationEventHandler, int i) {
        navigationEventHandler.getClass();
        checkInvariants();
        if (this.handlers.add(navigationEventHandler)) {
            this.sharedProcessor.addHandler(this, navigationEventHandler, i);
        }
    }

    public final void removeHandler$navigationevent(NavigationEventHandler navigationEventHandler) {
        navigationEventHandler.getClass();
        if (this.handlers.remove(navigationEventHandler)) {
            this.sharedProcessor.removeHandler(navigationEventHandler);
        }
    }

    public final void addInput(NavigationEventInput navigationEventInput) {
        navigationEventInput.getClass();
        checkInvariants();
        if (this.inputs.add(navigationEventInput)) {
            this.sharedProcessor.addInput(this, navigationEventInput, -1);
        }
    }

    public final void addInput(NavigationEventInput navigationEventInput, int i) {
        navigationEventInput.getClass();
        checkInvariants();
        if (i != 1 && i != 0) {
            throw new IllegalArgumentException(("Unsupported priority value: " + i).toString());
        }
        if (this.inputs.add(navigationEventInput)) {
            this.sharedProcessor.addInput(this, navigationEventInput, i);
        }
    }

    public final void dispatchOnStarted$navigationevent(NavigationEventInput navigationEventInput, int i, NavigationEvent navigationEvent) {
        navigationEventInput.getClass();
        checkInvariants();
        if (isEnabled()) {
            this.sharedProcessor.dispatchOnStarted(navigationEventInput, i, navigationEvent);
        }
    }

    public final void dispatchOnProgressed$navigationevent(NavigationEventInput navigationEventInput, int i, NavigationEvent navigationEvent) {
        navigationEventInput.getClass();
        navigationEvent.getClass();
        checkInvariants();
        if (isEnabled()) {
            this.sharedProcessor.dispatchOnProgressed(navigationEventInput, i, navigationEvent);
        }
    }

    public final void dispatchOnCompleted$navigationevent(NavigationEventInput navigationEventInput, int i) {
        navigationEventInput.getClass();
        checkInvariants();
        if (isEnabled()) {
            this.sharedProcessor.dispatchOnCompleted(navigationEventInput, i, this.onBackCompletedFallback, null);
        }
    }

    public final void dispatchOnCancelled$navigationevent(NavigationEventInput navigationEventInput, int i) {
        navigationEventInput.getClass();
        checkInvariants();
        if (isEnabled()) {
            this.sharedProcessor.dispatchOnCancelled(navigationEventInput, i);
        }
    }

    private final void checkInvariants() {
        if (isDisposed()) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("This NavigationEventDispatcher has already been disposed and cannot be used.");
        }
    }

    /* JADX INFO: compiled from: NavigationEventDispatcher.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
