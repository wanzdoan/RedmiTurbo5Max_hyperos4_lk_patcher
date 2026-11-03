package androidx.navigationevent;

import androidx.collection.MutableOrderedScatterSet;
import androidx.collection.OrderedScatterSetKt;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.ArrayDeque;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/* JADX INFO: compiled from: NavigationEventProcessor.kt */
/* JADX INFO: loaded from: classes.dex */
public final class NavigationEventProcessor {
    private final MutableStateFlow _history;
    private final MutableStateFlow _transitionState;
    private final ArrayDeque defaultHandlers;
    private final MutableOrderedScatterSet defaultInputs;
    private boolean hasEnabledAnyHandlers;
    private boolean hasEnabledDefaultHandlers;
    private boolean hasEnabledOverlayHandlers;
    private final StateFlow history;
    private int inProgressDirection;
    private NavigationEventHandler inProgressHandler;
    private NavigationEventInput inProgressInput;
    private final ArrayDeque overlayHandlers;
    private final MutableOrderedScatterSet overlayInputs;
    private final StateFlow transitionState;
    private final MutableOrderedScatterSet unspecifiedInputs;

    public NavigationEventProcessor() {
        MutableStateFlow MutableStateFlow = StateFlowKt.MutableStateFlow(NavigationEventTransitionState.Idle.INSTANCE);
        this._transitionState = MutableStateFlow;
        this.transitionState = FlowKt.asStateFlow(MutableStateFlow);
        MutableStateFlow MutableStateFlow2 = StateFlowKt.MutableStateFlow(new NavigationEventHistory());
        this._history = MutableStateFlow2;
        this.history = FlowKt.asStateFlow(MutableStateFlow2);
        this.overlayHandlers = new ArrayDeque();
        this.defaultHandlers = new ArrayDeque();
        this.unspecifiedInputs = OrderedScatterSetKt.mutableOrderedScatterSetOf();
        this.defaultInputs = OrderedScatterSetKt.mutableOrderedScatterSetOf();
        this.overlayInputs = OrderedScatterSetKt.mutableOrderedScatterSetOf();
    }

    public final void refreshEnabledHandlers() {
        boolean z;
        boolean z2;
        ArrayDeque arrayDeque = this.overlayHandlers;
        if (arrayDeque == null || !arrayDeque.isEmpty()) {
            Iterator<E> it = arrayDeque.iterator();
            while (true) {
                if (it.hasNext()) {
                    NavigationEventHandler navigationEventHandler = (NavigationEventHandler) it.next();
                    if (navigationEventHandler.isBackEnabled() || navigationEventHandler.isForwardEnabled()) {
                        z = true;
                    }
                } else {
                    z = false;
                }
            }
        } else {
            z = false;
        }
        ArrayDeque arrayDeque2 = this.defaultHandlers;
        if (arrayDeque2 == null || !arrayDeque2.isEmpty()) {
            Iterator<E> it2 = arrayDeque2.iterator();
            while (true) {
                if (it2.hasNext()) {
                    NavigationEventHandler navigationEventHandler2 = (NavigationEventHandler) it2.next();
                    if (navigationEventHandler2.isBackEnabled() || navigationEventHandler2.isForwardEnabled()) {
                        z2 = true;
                    }
                } else {
                    z2 = false;
                }
            }
        } else {
            z2 = false;
        }
        boolean z3 = z || z2;
        boolean z4 = this.hasEnabledOverlayHandlers != z;
        boolean z5 = this.hasEnabledDefaultHandlers != z2;
        boolean z6 = this.hasEnabledAnyHandlers != z3;
        if (z4) {
            MutableOrderedScatterSet mutableOrderedScatterSet = this.overlayInputs;
            Object[] objArr = mutableOrderedScatterSet.elements;
            long[] jArr = mutableOrderedScatterSet.nodes;
            int i = mutableOrderedScatterSet.tail;
            while (i != Integer.MAX_VALUE) {
                int i2 = (int) ((jArr[i] >> 31) & 2147483647L);
                ((NavigationEventInput) objArr[i]).doOnHasEnabledHandlersChanged$navigationevent(z);
                i = i2;
            }
        }
        if (z5) {
            MutableOrderedScatterSet mutableOrderedScatterSet2 = this.defaultInputs;
            Object[] objArr2 = mutableOrderedScatterSet2.elements;
            long[] jArr2 = mutableOrderedScatterSet2.nodes;
            int i3 = mutableOrderedScatterSet2.tail;
            while (i3 != Integer.MAX_VALUE) {
                int i4 = (int) ((jArr2[i3] >> 31) & 2147483647L);
                ((NavigationEventInput) objArr2[i3]).doOnHasEnabledHandlersChanged$navigationevent(z2);
                i3 = i4;
            }
        }
        if (z6) {
            MutableOrderedScatterSet mutableOrderedScatterSet3 = this.unspecifiedInputs;
            Object[] objArr3 = mutableOrderedScatterSet3.elements;
            long[] jArr3 = mutableOrderedScatterSet3.nodes;
            int i5 = mutableOrderedScatterSet3.tail;
            while (i5 != Integer.MAX_VALUE) {
                int i6 = (int) ((jArr3[i5] >> 31) & 2147483647L);
                ((NavigationEventInput) objArr3[i5]).doOnHasEnabledHandlersChanged$navigationevent(z3);
                i5 = i6;
            }
        }
        this.hasEnabledOverlayHandlers = z;
        this.hasEnabledDefaultHandlers = z2;
        this.hasEnabledAnyHandlers = z3;
        NavigationEventHandler navigationEventHandlerResolveEnabledHandler$default = this.inProgressHandler;
        if (navigationEventHandlerResolveEnabledHandler$default == null) {
            navigationEventHandlerResolveEnabledHandler$default = resolveEnabledHandler$default(this, 0, 1, null);
        }
        updateEnabledHandlerInfo$navigationevent(navigationEventHandlerResolveEnabledHandler$default);
    }

    public final void updateEnabledHandlerInfo$navigationevent(NavigationEventHandler navigationEventHandler) {
        NavigationEventHistory navigationEventHistory;
        NavigationEventHandler navigationEventHandlerResolveEnabledHandler$default = this.inProgressHandler;
        if (navigationEventHandlerResolveEnabledHandler$default == null) {
            navigationEventHandlerResolveEnabledHandler$default = resolveEnabledHandler$default(this, 0, 1, null);
        }
        if (Intrinsics.areEqual(navigationEventHandlerResolveEnabledHandler$default, navigationEventHandler)) {
            if (navigationEventHandlerResolveEnabledHandler$default == null) {
                navigationEventHistory = new NavigationEventHistory();
            } else {
                navigationEventHistory = new NavigationEventHistory(navigationEventHandlerResolveEnabledHandler$default.getCurrentInfo(), resolveCombinedBackInfo(), navigationEventHandlerResolveEnabledHandler$default.getForwardInfo());
            }
            if (Intrinsics.areEqual((NavigationEventHistory) this._history.getValue(), navigationEventHistory)) {
                return;
            }
            this._history.setValue(navigationEventHistory);
            MutableOrderedScatterSet mutableOrderedScatterSet = this.overlayInputs;
            Object[] objArr = mutableOrderedScatterSet.elements;
            long[] jArr = mutableOrderedScatterSet.nodes;
            int i = mutableOrderedScatterSet.tail;
            while (i != Integer.MAX_VALUE) {
                int i2 = (int) (2147483647L & (jArr[i] >> 31));
                ((NavigationEventInput) objArr[i]).doOnHistoryChanged$navigationevent(navigationEventHistory);
                i = i2;
            }
            MutableOrderedScatterSet mutableOrderedScatterSet2 = this.defaultInputs;
            Object[] objArr2 = mutableOrderedScatterSet2.elements;
            long[] jArr2 = mutableOrderedScatterSet2.nodes;
            int i3 = mutableOrderedScatterSet2.tail;
            while (i3 != Integer.MAX_VALUE) {
                int i4 = (int) ((jArr2[i3] >> 31) & 2147483647L);
                ((NavigationEventInput) objArr2[i3]).doOnHistoryChanged$navigationevent(navigationEventHistory);
                i3 = i4;
            }
            MutableOrderedScatterSet mutableOrderedScatterSet3 = this.unspecifiedInputs;
            Object[] objArr3 = mutableOrderedScatterSet3.elements;
            long[] jArr3 = mutableOrderedScatterSet3.nodes;
            int i5 = mutableOrderedScatterSet3.tail;
            while (i5 != Integer.MAX_VALUE) {
                int i6 = (int) ((jArr3[i5] >> 31) & 2147483647L);
                ((NavigationEventInput) objArr3[i5]).doOnHistoryChanged$navigationevent(navigationEventHistory);
                i5 = i6;
            }
        }
    }

    public final void addHandler(NavigationEventDispatcher navigationEventDispatcher, NavigationEventHandler navigationEventHandler, int i) {
        navigationEventDispatcher.getClass();
        navigationEventHandler.getClass();
        if (navigationEventHandler.getDispatcher$navigationevent() != null) {
            throw new IllegalArgumentException(("Handler '" + navigationEventHandler + "' is already registered with a dispatcher").toString());
        }
        if (i == 0) {
            this.overlayHandlers.addFirst(navigationEventHandler);
        } else if (i == 1) {
            this.defaultHandlers.addFirst(navigationEventHandler);
        } else {
            HexString$$ExternalSyntheticBUOutline1.m("Unsupported priority value: ", i);
            return;
        }
        navigationEventHandler.setDispatcher$navigationevent(navigationEventDispatcher);
        refreshEnabledHandlers();
    }

    public final void removeHandler(NavigationEventHandler navigationEventHandler) {
        navigationEventHandler.getClass();
        if (Intrinsics.areEqual(navigationEventHandler, this.inProgressHandler)) {
            int i = this.inProgressDirection;
            if (i == -1) {
                navigationEventHandler.doOnBackCancelled$navigationevent();
            } else if (i == 1) {
                navigationEventHandler.doOnForwardCancelled$navigationevent();
            }
            this.inProgressHandler = null;
            this.inProgressDirection = 0;
            this.inProgressInput = null;
        }
        this.overlayHandlers.remove(navigationEventHandler);
        this.defaultHandlers.remove(navigationEventHandler);
        navigationEventHandler.setDispatcher$navigationevent(null);
        refreshEnabledHandlers();
    }

    public final void addInput(NavigationEventDispatcher navigationEventDispatcher, NavigationEventInput navigationEventInput, int i) {
        MutableOrderedScatterSet mutableOrderedScatterSet;
        boolean z;
        navigationEventDispatcher.getClass();
        navigationEventInput.getClass();
        if (navigationEventInput.getDispatcher$navigationevent() != null) {
            StringBuilder sb = new StringBuilder("Input '");
            sb.append(navigationEventInput);
            NavigationEventDispatcher dispatcher$navigationevent = navigationEventInput.getDispatcher$navigationevent();
            sb.append("' is already added to dispatcher ");
            sb.append(dispatcher$navigationevent);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString().toString());
        }
        if (i == 0) {
            mutableOrderedScatterSet = this.overlayInputs;
        } else if (i == 1) {
            mutableOrderedScatterSet = this.defaultInputs;
        } else {
            mutableOrderedScatterSet = this.unspecifiedInputs;
        }
        mutableOrderedScatterSet.plusAssign(navigationEventInput);
        navigationEventInput.setDispatcher$navigationevent(navigationEventDispatcher);
        navigationEventInput.doOnAdded$navigationevent(navigationEventDispatcher);
        navigationEventInput.doOnHistoryChanged$navigationevent((NavigationEventHistory) this.history.getValue());
        if (i == 0) {
            z = this.hasEnabledOverlayHandlers;
        } else if (i == 1) {
            z = this.hasEnabledDefaultHandlers;
        } else {
            z = this.hasEnabledAnyHandlers;
        }
        navigationEventInput.doOnHasEnabledHandlersChanged$navigationevent(z);
    }

    public final void dispatchOnStarted(NavigationEventInput navigationEventInput, int i, NavigationEvent navigationEvent) {
        navigationEventInput.getClass();
        if (this.inProgressDirection != 0) {
            return;
        }
        NavigationEventHandler navigationEventHandlerResolveEnabledHandler = resolveEnabledHandler(i);
        this.inProgressHandler = navigationEventHandlerResolveEnabledHandler;
        this.inProgressDirection = i;
        this.inProgressInput = navigationEventInput;
        if (navigationEvent != null) {
            if (i != -1) {
                if (i == 1 && navigationEventHandlerResolveEnabledHandler != null) {
                    navigationEventHandlerResolveEnabledHandler.doOnForwardStarted$navigationevent(navigationEvent);
                }
            } else if (navigationEventHandlerResolveEnabledHandler != null) {
                navigationEventHandlerResolveEnabledHandler.doOnBackStarted$navigationevent(navigationEvent);
            }
            this._transitionState.setValue(new NavigationEventTransitionState.InProgress(navigationEvent, i));
        }
    }

    public final void dispatchOnProgressed(NavigationEventInput navigationEventInput, int i, NavigationEvent navigationEvent) {
        navigationEventInput.getClass();
        navigationEvent.getClass();
        if (Intrinsics.areEqual(navigationEventInput, this.inProgressInput) && i == this.inProgressDirection) {
            NavigationEventHandler navigationEventHandlerResolveEnabledHandler = this.inProgressHandler;
            if (navigationEventHandlerResolveEnabledHandler == null) {
                navigationEventHandlerResolveEnabledHandler = resolveEnabledHandler(i);
            }
            if (i != -1) {
                if (i == 1 && navigationEventHandlerResolveEnabledHandler != null) {
                    navigationEventHandlerResolveEnabledHandler.doOnForwardProgressed$navigationevent(navigationEvent);
                }
            } else if (navigationEventHandlerResolveEnabledHandler != null) {
                navigationEventHandlerResolveEnabledHandler.doOnBackProgressed$navigationevent(navigationEvent);
            }
            this._transitionState.setValue(new NavigationEventTransitionState.InProgress(navigationEvent, i));
        }
    }

    public final void dispatchOnCompleted(NavigationEventInput navigationEventInput, int i, OnBackCompletedFallback onBackCompletedFallback, OnForwardCompletedFallback onForwardCompletedFallback) {
        navigationEventInput.getClass();
        if (Intrinsics.areEqual(navigationEventInput, this.inProgressInput) && i == this.inProgressDirection) {
            NavigationEventHandler navigationEventHandlerResolveEnabledHandler = this.inProgressHandler;
            if (navigationEventHandlerResolveEnabledHandler == null) {
                navigationEventHandlerResolveEnabledHandler = resolveEnabledHandler(i);
            }
            this.inProgressHandler = null;
            this.inProgressDirection = 0;
            this.inProgressInput = null;
            if (i != -1) {
                if (i == 1) {
                    if (navigationEventHandlerResolveEnabledHandler != null) {
                        navigationEventHandlerResolveEnabledHandler.doOnForwardCompleted$navigationevent();
                    } else if (onForwardCompletedFallback != null) {
                        onForwardCompletedFallback.onForwardCompletedFallback();
                    }
                }
            } else if (navigationEventHandlerResolveEnabledHandler != null) {
                navigationEventHandlerResolveEnabledHandler.doOnBackCompleted$navigationevent();
            } else if (onBackCompletedFallback != null) {
                onBackCompletedFallback.onBackCompletedFallback();
            }
            this._transitionState.setValue(NavigationEventTransitionState.Idle.INSTANCE);
        }
    }

    public final void dispatchOnCancelled(NavigationEventInput navigationEventInput, int i) {
        navigationEventInput.getClass();
        if (Intrinsics.areEqual(navigationEventInput, this.inProgressInput) && i == this.inProgressDirection) {
            NavigationEventHandler navigationEventHandlerResolveEnabledHandler = this.inProgressHandler;
            if (navigationEventHandlerResolveEnabledHandler == null) {
                navigationEventHandlerResolveEnabledHandler = resolveEnabledHandler(i);
            }
            this.inProgressHandler = null;
            this.inProgressDirection = 0;
            this.inProgressInput = null;
            if (i != -1) {
                if (i == 1 && navigationEventHandlerResolveEnabledHandler != null) {
                    navigationEventHandlerResolveEnabledHandler.doOnForwardCancelled$navigationevent();
                }
            } else if (navigationEventHandlerResolveEnabledHandler != null) {
                navigationEventHandlerResolveEnabledHandler.doOnBackCancelled$navigationevent();
            }
            this._transitionState.setValue(NavigationEventTransitionState.Idle.INSTANCE);
        }
    }

    static /* synthetic */ NavigationEventHandler resolveEnabledHandler$default(NavigationEventProcessor navigationEventProcessor, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return navigationEventProcessor.resolveEnabledHandler(i);
    }

    private final NavigationEventHandler resolveEnabledHandler(int i) {
        Object next;
        Object next2;
        NavigationEventHandler navigationEventHandler;
        Object next3;
        Object obj = null;
        if (i == -1) {
            Iterator<E> it = this.overlayHandlers.iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!((NavigationEventHandler) next).isBackEnabled());
            NavigationEventHandler navigationEventHandler2 = (NavigationEventHandler) next;
            if (navigationEventHandler2 != null) {
                return navigationEventHandler2;
            }
            for (Object obj2 : this.defaultHandlers) {
                if (((NavigationEventHandler) obj2).isBackEnabled()) {
                    obj = obj2;
                    break;
                }
            }
            return (NavigationEventHandler) obj;
        }
        if (i != 0) {
            if (i != 1) {
                throw new IllegalStateException(("Unsupported direction: '" + i + "'.").toString());
            }
            Iterator<E> it2 = this.overlayHandlers.iterator();
            do {
                if (!it2.hasNext()) {
                    next3 = null;
                    break;
                }
                next3 = it2.next();
            } while (!((NavigationEventHandler) next3).isForwardEnabled());
            NavigationEventHandler navigationEventHandler3 = (NavigationEventHandler) next3;
            if (navigationEventHandler3 != null) {
                return navigationEventHandler3;
            }
            for (Object obj3 : this.defaultHandlers) {
                if (((NavigationEventHandler) obj3).isForwardEnabled()) {
                    obj = obj3;
                    break;
                }
            }
            return (NavigationEventHandler) obj;
        }
        Iterator<E> it3 = this.overlayHandlers.iterator();
        do {
            if (!it3.hasNext()) {
                next2 = null;
                break;
            }
            next2 = it3.next();
            navigationEventHandler = (NavigationEventHandler) next2;
            if (navigationEventHandler.isBackEnabled()) {
                break;
            }
        } while (!navigationEventHandler.isForwardEnabled());
        NavigationEventHandler navigationEventHandler4 = (NavigationEventHandler) next2;
        if (navigationEventHandler4 != null) {
            return navigationEventHandler4;
        }
        for (Object obj4 : this.defaultHandlers) {
            NavigationEventHandler navigationEventHandler5 = (NavigationEventHandler) obj4;
            if (navigationEventHandler5.isBackEnabled() || navigationEventHandler5.isForwardEnabled()) {
                obj = obj4;
                break;
            }
        }
        return (NavigationEventHandler) obj;
    }

    private final List resolveCombinedBackInfo() {
        ArrayList arrayList = new ArrayList();
        for (NavigationEventHandler navigationEventHandler : this.overlayHandlers) {
            if (navigationEventHandler.isBackEnabled() && !navigationEventHandler.getBackInfo().isEmpty()) {
                arrayList.addAll(navigationEventHandler.getBackInfo());
            }
        }
        for (NavigationEventHandler navigationEventHandler2 : this.defaultHandlers) {
            if (navigationEventHandler2.isBackEnabled() && !navigationEventHandler2.getBackInfo().isEmpty()) {
                arrayList.addAll(navigationEventHandler2.getBackInfo());
            }
        }
        return arrayList;
    }
}
