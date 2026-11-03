package androidx.navigationevent;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: NavigationEventTransitionState.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class NavigationEventTransitionState {
    public static final Companion Companion = new Companion(null);

    public /* synthetic */ NavigationEventTransitionState(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    private NavigationEventTransitionState() {
    }

    /* JADX INFO: compiled from: NavigationEventTransitionState.kt */
    public final class Idle extends NavigationEventTransitionState {
        public static final Idle INSTANCE = new Idle();

        private Idle() {
            super(null);
        }

        public String toString() {
            return "Idle()";
        }
    }

    /* JADX INFO: compiled from: NavigationEventTransitionState.kt */
    public final class InProgress extends NavigationEventTransitionState {
        private final int direction;
        private final NavigationEvent latestEvent;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public InProgress(NavigationEvent navigationEvent, int i) {
            super(null);
            navigationEvent.getClass();
            this.latestEvent = navigationEvent;
            this.direction = i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || InProgress.class != obj.getClass()) {
                return false;
            }
            InProgress inProgress = (InProgress) obj;
            return this.direction == inProgress.direction && Intrinsics.areEqual(this.latestEvent, inProgress.latestEvent);
        }

        public int hashCode() {
            return (this.direction * 31) + this.latestEvent.hashCode();
        }

        public String toString() {
            return "InProgress(latestEvent=" + this.latestEvent + ", direction=" + this.direction + ')';
        }
    }

    /* JADX INFO: compiled from: NavigationEventTransitionState.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
