package androidx.navigationevent;

import android.window.BackEvent;

/* JADX INFO: compiled from: NavigationEvent.android.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class NavigationEventKt {
    public static final NavigationEvent toNavigationEvent(BackEvent backEvent) {
        backEvent.getClass();
        float touchX = backEvent.getTouchX();
        float touchY = backEvent.getTouchY();
        return new NavigationEvent(backEvent.getSwipeEdge(), backEvent.getProgress(), touchX, touchY, backEvent.getFrameTimeMillis());
    }
}
