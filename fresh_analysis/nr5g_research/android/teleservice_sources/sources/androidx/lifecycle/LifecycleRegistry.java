package androidx.lifecycle;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline1;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/* JADX INFO: compiled from: LifecycleRegistry.kt */
/* JADX INFO: loaded from: classes.dex */
public class LifecycleRegistry extends Lifecycle {
    public static final Companion Companion = new Companion(null);
    private final MutableStateFlow _currentStateFlow;
    private int addingObserverCounter;
    private final boolean enforceMainThread;
    private boolean handlingEvent;
    private final WeakReference lifecycleOwner;
    private boolean newEventOccurred;
    private FastSafeIterableMap observerMap;
    private List parentStates;
    private Lifecycle.State state;

    public /* synthetic */ LifecycleRegistry(LifecycleOwner lifecycleOwner, boolean z, DefaultConstructorMarker defaultConstructorMarker) {
        this(lifecycleOwner, z);
    }

    public static final LifecycleRegistry createUnsafe(LifecycleOwner lifecycleOwner) {
        return Companion.createUnsafe(lifecycleOwner);
    }

    private LifecycleRegistry(LifecycleOwner lifecycleOwner, boolean z) {
        this.enforceMainThread = z;
        this.observerMap = new FastSafeIterableMap();
        this.lifecycleOwner = new WeakReference(lifecycleOwner);
        this.parentStates = new ArrayList();
        Lifecycle.State state = Lifecycle.State.INITIALIZED;
        this.state = state;
        this._currentStateFlow = StateFlowKt.MutableStateFlow(state);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public LifecycleRegistry(LifecycleOwner lifecycleOwner) {
        this(lifecycleOwner, true);
        lifecycleOwner.getClass();
    }

    @Override // androidx.lifecycle.Lifecycle
    public Lifecycle.State getCurrentState() {
        return this.state;
    }

    public void setCurrentState(Lifecycle.State state) {
        state.getClass();
        enforceMainThreadIfNeeded("setCurrentState");
        moveToState(state);
    }

    public void handleLifecycleEvent(Lifecycle.Event event) {
        event.getClass();
        enforceMainThreadIfNeeded("handleLifecycleEvent");
        moveToState(event.getTargetState());
    }

    private final void moveToState(Lifecycle.State state) {
        if (this.state == state) {
            return;
        }
        LifecycleRegistryKt.checkLifecycleStateTransition((LifecycleOwner) this.lifecycleOwner.get(), this.state, state);
        this.state = state;
        if (this.handlingEvent || this.addingObserverCounter != 0) {
            this.newEventOccurred = true;
            return;
        }
        this.handlingEvent = true;
        sync();
        this.handlingEvent = false;
        if (this.state == Lifecycle.State.DESTROYED) {
            this.observerMap = new FastSafeIterableMap();
        }
    }

    private final boolean isSynced() {
        if (this.observerMap.size() == 0) {
            return true;
        }
        Lifecycle.State state = ((ObserverWithState) this.observerMap.first().getValue()).getState();
        Lifecycle.State state2 = ((ObserverWithState) this.observerMap.last().getValue()).getState();
        return state == state2 && this.state == state2;
    }

    private final Lifecycle.State calculateTargetState(LifecycleObserver lifecycleObserver) {
        ObserverWithState observerWithState;
        Map.Entry entryCeil = this.observerMap.ceil(lifecycleObserver);
        Lifecycle.State state = null;
        Lifecycle.State state2 = (entryCeil == null || (observerWithState = (ObserverWithState) entryCeil.getValue()) == null) ? null : observerWithState.getState();
        if (!this.parentStates.isEmpty()) {
            List list = this.parentStates;
            state = (Lifecycle.State) list.get(list.size() - 1);
        }
        return LifecycleRegistryKt.min(LifecycleRegistryKt.min(this.state, state2), state);
    }

    @Override // androidx.lifecycle.Lifecycle
    public void addObserver(LifecycleObserver lifecycleObserver) {
        LifecycleOwner lifecycleOwner;
        lifecycleObserver.getClass();
        enforceMainThreadIfNeeded("addObserver");
        Lifecycle.State state = this.state;
        Lifecycle.State state2 = Lifecycle.State.DESTROYED;
        if (state != state2) {
            state2 = Lifecycle.State.INITIALIZED;
        }
        ObserverWithState observerWithState = new ObserverWithState(lifecycleObserver, state2);
        if (((ObserverWithState) this.observerMap.putIfAbsent(lifecycleObserver, observerWithState)) == null && (lifecycleOwner = (LifecycleOwner) this.lifecycleOwner.get()) != null) {
            boolean z = this.addingObserverCounter != 0 || this.handlingEvent;
            Lifecycle.State stateCalculateTargetState = calculateTargetState(lifecycleObserver);
            this.addingObserverCounter++;
            while (observerWithState.getState().compareTo(stateCalculateTargetState) < 0 && this.observerMap.contains(lifecycleObserver)) {
                this.parentStates.add(observerWithState.getState());
                Lifecycle.Event eventUpFrom = Lifecycle.Event.Companion.upFrom(observerWithState.getState());
                if (eventUpFrom == null) {
                    OrtUtil$$ExternalSyntheticBUOutline1.m("no event up from ", observerWithState.getState());
                    return;
                } else {
                    observerWithState.dispatchEvent(lifecycleOwner, eventUpFrom);
                    CollectionsKt.removeLastOrNull(this.parentStates);
                    stateCalculateTargetState = calculateTargetState(lifecycleObserver);
                }
            }
            if (!z) {
                sync();
            }
            this.addingObserverCounter--;
        }
    }

    @Override // androidx.lifecycle.Lifecycle
    public void removeObserver(LifecycleObserver lifecycleObserver) {
        lifecycleObserver.getClass();
        enforceMainThreadIfNeeded("removeObserver");
        this.observerMap.remove(lifecycleObserver);
    }

    private final void forwardPass(final LifecycleOwner lifecycleOwner) {
        this.observerMap.forEachWithAdditions(new Function1() { // from class: androidx.lifecycle.LifecycleRegistry$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LifecycleRegistry.m30$r8$lambda$sIRNHHyujojKOxRELgp4WNMal8(this.f$0, lifecycleOwner, (Map.Entry) obj);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$sIRNHHyujojK-OxRELgp4WNMal8, reason: not valid java name */
    public static Unit m30$r8$lambda$sIRNHHyujojKOxRELgp4WNMal8(LifecycleRegistry lifecycleRegistry, LifecycleOwner lifecycleOwner, Map.Entry entry) {
        entry.getClass();
        LifecycleObserver lifecycleObserver = (LifecycleObserver) entry.getKey();
        ObserverWithState observerWithState = (ObserverWithState) entry.getValue();
        while (observerWithState.getState().compareTo(lifecycleRegistry.state) < 0 && !lifecycleRegistry.newEventOccurred && lifecycleRegistry.observerMap.contains(lifecycleObserver)) {
            lifecycleRegistry.parentStates.add(observerWithState.getState());
            Lifecycle.Event eventUpFrom = Lifecycle.Event.Companion.upFrom(observerWithState.getState());
            if (eventUpFrom == null) {
                LifecycleRegistry$$ExternalSyntheticBUOutline0.m("no event up from ", observerWithState.getState());
                return null;
            }
            observerWithState.dispatchEvent(lifecycleOwner, eventUpFrom);
            CollectionsKt.removeLastOrNull(lifecycleRegistry.parentStates);
        }
        return Unit.INSTANCE;
    }

    private final void backwardPass(final LifecycleOwner lifecycleOwner) {
        this.observerMap.forEachReversed(new Function1() { // from class: androidx.lifecycle.LifecycleRegistry$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LifecycleRegistry.m29$r8$lambda$0D5eFGnUA40HVp9uW0LVcCRwjE(this.f$0, lifecycleOwner, (Map.Entry) obj);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$0D5eFGnUA40HVp-9uW0LVcCRwjE, reason: not valid java name */
    public static Unit m29$r8$lambda$0D5eFGnUA40HVp9uW0LVcCRwjE(LifecycleRegistry lifecycleRegistry, LifecycleOwner lifecycleOwner, Map.Entry entry) {
        entry.getClass();
        LifecycleObserver lifecycleObserver = (LifecycleObserver) entry.getKey();
        ObserverWithState observerWithState = (ObserverWithState) entry.getValue();
        while (observerWithState.getState().compareTo(lifecycleRegistry.state) > 0 && !lifecycleRegistry.newEventOccurred && lifecycleRegistry.observerMap.contains(lifecycleObserver)) {
            Lifecycle.Event eventDownFrom = Lifecycle.Event.Companion.downFrom(observerWithState.getState());
            if (eventDownFrom == null) {
                LifecycleRegistry$$ExternalSyntheticBUOutline0.m("no event down from ", observerWithState.getState());
                return null;
            }
            lifecycleRegistry.parentStates.add(eventDownFrom.getTargetState());
            observerWithState.dispatchEvent(lifecycleOwner, eventDownFrom);
            CollectionsKt.removeLastOrNull(lifecycleRegistry.parentStates);
        }
        return Unit.INSTANCE;
    }

    private final void sync() {
        Object obj = this.lifecycleOwner.get();
        if (obj == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("LifecycleOwner of this LifecycleRegistry is already garbage collected. It is too late to change lifecycle state.");
            return;
        }
        LifecycleOwner lifecycleOwner = (LifecycleOwner) obj;
        while (!isSynced()) {
            this.newEventOccurred = false;
            if (this.state.compareTo(((ObserverWithState) this.observerMap.first().getValue()).getState()) < 0) {
                backwardPass(lifecycleOwner);
            }
            Map.Entry entryLastOrNull = this.observerMap.lastOrNull();
            if (!this.newEventOccurred && entryLastOrNull != null && this.state.compareTo(((ObserverWithState) entryLastOrNull.getValue()).getState()) > 0) {
                forwardPass(lifecycleOwner);
            }
        }
        this.newEventOccurred = false;
        this._currentStateFlow.setValue(getCurrentState());
    }

    private final void enforceMainThreadIfNeeded(String str) {
        if (!this.enforceMainThread || LifecycleRegistry_androidKt.isMainThread()) {
            return;
        }
        throw new IllegalStateException(("Method " + str + " must be called on the main thread").toString());
    }

    /* JADX INFO: compiled from: LifecycleRegistry.kt */
    public final class ObserverWithState {
        private LifecycleEventObserver lifecycleObserver;
        private Lifecycle.State state;

        public ObserverWithState(LifecycleObserver lifecycleObserver, Lifecycle.State state) {
            state.getClass();
            this.state = state;
            lifecycleObserver.getClass();
            this.lifecycleObserver = Lifecycling.lifecycleEventObserver(lifecycleObserver);
        }

        public final Lifecycle.State getState() {
            return this.state;
        }

        public final void dispatchEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            event.getClass();
            Lifecycle.State targetState = event.getTargetState();
            this.state = LifecycleRegistryKt.min(this.state, targetState);
            LifecycleEventObserver lifecycleEventObserver = this.lifecycleObserver;
            lifecycleOwner.getClass();
            lifecycleEventObserver.onStateChanged(lifecycleOwner, event);
            this.state = targetState;
        }
    }

    /* JADX INFO: compiled from: LifecycleRegistry.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final LifecycleRegistry createUnsafe(LifecycleOwner lifecycleOwner) {
            lifecycleOwner.getClass();
            return new LifecycleRegistry(lifecycleOwner, false, null);
        }
    }
}
