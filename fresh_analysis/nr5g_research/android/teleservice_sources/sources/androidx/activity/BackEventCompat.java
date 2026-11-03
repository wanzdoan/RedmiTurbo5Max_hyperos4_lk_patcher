package androidx.activity;

import androidx.navigationevent.NavigationEvent;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: BackEventCompat.kt */
/* JADX INFO: loaded from: classes.dex */
public final class BackEventCompat {
    public static final Companion Companion = new Companion(null);
    private final long frameTimeMillis;
    private final float progress;
    private final int swipeEdge;
    private final float touchX;
    private final float touchY;

    public BackEventCompat(float f, float f2, float f3, int i) {
        this(f, f2, f3, i, 0L, 16, null);
    }

    public BackEventCompat(float f, float f2, float f3, int i, long j) {
        this.touchX = f;
        this.touchY = f2;
        this.progress = f3;
        this.swipeEdge = i;
        this.frameTimeMillis = j;
    }

    public /* synthetic */ BackEventCompat(float f, float f2, float f3, int i, long j, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(f, f2, f3, i, (i2 & 16) != 0 ? 0L : j);
    }

    public final float getProgress() {
        return this.progress;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public BackEventCompat(NavigationEvent navigationEvent) {
        this(navigationEvent.getTouchX(), navigationEvent.getTouchY(), navigationEvent.getProgress(), navigationEvent.getSwipeEdge(), navigationEvent.getFrameTimeMillis());
        navigationEvent.getClass();
    }

    public final NavigationEvent toNavigationEvent() {
        float f = this.touchX;
        float f2 = this.touchY;
        return new NavigationEvent(this.swipeEdge, this.progress, f, f2, this.frameTimeMillis);
    }

    public String toString() {
        return "BackEventCompat(touchX=" + this.touchX + ", touchY=" + this.touchY + ", progress=" + this.progress + ", swipeEdge=" + this.swipeEdge + ", frameTimeMillis=" + this.frameTimeMillis + ')';
    }

    /* JADX INFO: compiled from: BackEventCompat.kt */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
