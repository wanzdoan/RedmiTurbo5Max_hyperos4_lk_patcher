package androidx.navigationevent;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: NavigationEvent.kt */
/* JADX INFO: loaded from: classes.dex */
public final class NavigationEvent {
    public static final Companion Companion = new Companion(null);
    private final long frameTimeMillis;
    private final float progress;
    private final int swipeEdge;
    private final float touchX;
    private final float touchY;

    public NavigationEvent(int i, float f, float f2, float f3, long j) {
        this.swipeEdge = i;
        this.progress = f;
        this.touchX = f2;
        this.touchY = f3;
        this.frameTimeMillis = j;
    }

    public final int getSwipeEdge() {
        return this.swipeEdge;
    }

    public final float getProgress() {
        return this.progress;
    }

    public final float getTouchX() {
        return this.touchX;
    }

    public final float getTouchY() {
        return this.touchY;
    }

    public final long getFrameTimeMillis() {
        return this.frameTimeMillis;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && NavigationEvent.class == obj.getClass()) {
            NavigationEvent navigationEvent = (NavigationEvent) obj;
            return this.touchX == navigationEvent.touchX && this.touchY == navigationEvent.touchY && this.progress == navigationEvent.progress && this.swipeEdge == navigationEvent.swipeEdge && this.frameTimeMillis == navigationEvent.frameTimeMillis;
        }
        return false;
    }

    public int hashCode() {
        return (((((((Float.hashCode(this.touchX) * 31) + Float.hashCode(this.touchY)) * 31) + Float.hashCode(this.progress)) * 31) + Integer.hashCode(this.swipeEdge)) * 31) + Long.hashCode(this.frameTimeMillis);
    }

    public String toString() {
        return "NavigationEvent(touchX=" + this.touchX + ", touchY=" + this.touchY + ", progress=" + this.progress + ", swipeEdge=" + this.swipeEdge + ", frameTimeMillis=" + this.frameTimeMillis + ')';
    }

    /* JADX INFO: compiled from: NavigationEvent.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
