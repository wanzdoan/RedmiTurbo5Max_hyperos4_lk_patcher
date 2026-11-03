package androidx.navigationevent;

import java.util.List;
import kotlin.collections.CollectionsKt;

/* JADX INFO: compiled from: NavigationEventHandler.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class NavigationEventHandler {
    private List backInfo;
    private NavigationEventInfo currentInfo;
    private NavigationEventDispatcher dispatcher;
    private List forwardInfo;
    private boolean isBackEnabled;
    private boolean isForwardEnabled;
    private NavigationEventTransitionState transitionState;

    protected abstract void onBackCancelled();

    protected abstract void onBackCompleted();

    protected abstract void onBackProgressed(NavigationEvent navigationEvent);

    protected abstract void onBackStarted(NavigationEvent navigationEvent);

    protected void onForwardCancelled() {
    }

    protected void onForwardProgressed(NavigationEvent navigationEvent) {
        navigationEvent.getClass();
    }

    protected void onForwardStarted(NavigationEvent navigationEvent) {
        navigationEvent.getClass();
    }

    public NavigationEventHandler(NavigationEventInfo navigationEventInfo, boolean z, boolean z2) {
        navigationEventInfo.getClass();
        this.currentInfo = navigationEventInfo;
        this.backInfo = CollectionsKt.emptyList();
        this.forwardInfo = CollectionsKt.emptyList();
        this.transitionState = NavigationEventTransitionState.Idle.INSTANCE;
        this.isBackEnabled = z;
        this.isForwardEnabled = z2;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public NavigationEventHandler(NavigationEventInfo navigationEventInfo, boolean z) {
        this(navigationEventInfo, z, false);
        navigationEventInfo.getClass();
    }

    public final NavigationEventInfo getCurrentInfo() {
        return this.currentInfo;
    }

    public final List getBackInfo() {
        return this.backInfo;
    }

    public final List getForwardInfo() {
        return this.forwardInfo;
    }

    public final boolean isBackEnabled() {
        NavigationEventDispatcher navigationEventDispatcher = this.dispatcher;
        if (navigationEventDispatcher == null || navigationEventDispatcher.isEnabled()) {
            return this.isBackEnabled;
        }
        return false;
    }

    public final void setBackEnabled(boolean z) {
        NavigationEventProcessor sharedProcessor$navigationevent;
        if (this.isBackEnabled == z) {
            return;
        }
        this.isBackEnabled = z;
        NavigationEventDispatcher navigationEventDispatcher = this.dispatcher;
        if (navigationEventDispatcher == null || (sharedProcessor$navigationevent = navigationEventDispatcher.getSharedProcessor$navigationevent()) == null) {
            return;
        }
        sharedProcessor$navigationevent.refreshEnabledHandlers();
    }

    public final boolean isForwardEnabled() {
        NavigationEventDispatcher navigationEventDispatcher = this.dispatcher;
        if (navigationEventDispatcher == null || navigationEventDispatcher.isEnabled()) {
            return this.isForwardEnabled;
        }
        return false;
    }

    public final NavigationEventDispatcher getDispatcher$navigationevent() {
        return this.dispatcher;
    }

    public final void setDispatcher$navigationevent(NavigationEventDispatcher navigationEventDispatcher) {
        this.dispatcher = navigationEventDispatcher;
    }

    public final void remove() {
        NavigationEventDispatcher navigationEventDispatcher = this.dispatcher;
        if (navigationEventDispatcher != null) {
            navigationEventDispatcher.removeHandler$navigationevent(this);
        }
    }

    public final void doOnBackStarted$navigationevent(NavigationEvent navigationEvent) {
        navigationEvent.getClass();
        this.transitionState = new NavigationEventTransitionState.InProgress(navigationEvent, -1);
        onBackStarted(navigationEvent);
    }

    public final void doOnBackProgressed$navigationevent(NavigationEvent navigationEvent) {
        navigationEvent.getClass();
        this.transitionState = new NavigationEventTransitionState.InProgress(navigationEvent, -1);
        onBackProgressed(navigationEvent);
    }

    public final void doOnBackCompleted$navigationevent() {
        this.transitionState = NavigationEventTransitionState.Idle.INSTANCE;
        onBackCompleted();
    }

    public final void doOnBackCancelled$navigationevent() {
        this.transitionState = NavigationEventTransitionState.Idle.INSTANCE;
        onBackCancelled();
    }

    public final void doOnForwardStarted$navigationevent(NavigationEvent navigationEvent) {
        navigationEvent.getClass();
        this.transitionState = new NavigationEventTransitionState.InProgress(navigationEvent, 1);
        onForwardStarted(navigationEvent);
    }

    public final void doOnForwardProgressed$navigationevent(NavigationEvent navigationEvent) {
        navigationEvent.getClass();
        this.transitionState = new NavigationEventTransitionState.InProgress(navigationEvent, 1);
        onForwardProgressed(navigationEvent);
    }

    public final void doOnForwardCompleted$navigationevent() {
        this.transitionState = NavigationEventTransitionState.Idle.INSTANCE;
        onForwardCompleted();
    }

    protected void onForwardCompleted() {
        throw new UnsupportedOperationException("A handler that receives a 'forwardCompleted' event must override 'onForwardCompleted()' to handle the callback.");
    }

    public final void doOnForwardCancelled$navigationevent() {
        this.transitionState = NavigationEventTransitionState.Idle.INSTANCE;
        onForwardCancelled();
    }
}
