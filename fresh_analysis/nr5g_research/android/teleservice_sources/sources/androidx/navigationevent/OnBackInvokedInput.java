package androidx.navigationevent;

import android.window.BackEvent;
import android.window.OnBackAnimationCallback;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: OnBackInvokedInput.android.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class OnBackInvokedInput extends NavigationEventInput {
    private boolean backInvokedCallbackRegistered;
    private final OnBackInvokedCallback onBackInvokedCallback;
    private final int onBackInvokedCallbackPriority;
    private final OnBackInvokedDispatcher onBackInvokedDispatcher;

    public /* synthetic */ OnBackInvokedInput(OnBackInvokedDispatcher onBackInvokedDispatcher, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(onBackInvokedDispatcher, i);
    }

    private OnBackInvokedInput(OnBackInvokedDispatcher onBackInvokedDispatcher, int i) {
        this.onBackInvokedDispatcher = onBackInvokedDispatcher;
        this.onBackInvokedCallbackPriority = i;
        this.onBackInvokedCallback = createOnBackAnimationCallback();
    }

    @Override // androidx.navigationevent.NavigationEventInput
    protected void onHasEnabledHandlersChanged(boolean z) {
        updateBackInvokedCallbackState(z);
    }

    private final void updateBackInvokedCallbackState(boolean z) {
        if (z && !this.backInvokedCallbackRegistered) {
            this.onBackInvokedDispatcher.registerOnBackInvokedCallback(this.onBackInvokedCallbackPriority, this.onBackInvokedCallback);
            this.backInvokedCallbackRegistered = true;
        } else {
            if (z || !this.backInvokedCallbackRegistered) {
                return;
            }
            this.onBackInvokedDispatcher.unregisterOnBackInvokedCallback(this.onBackInvokedCallback);
            this.backInvokedCallbackRegistered = false;
        }
    }

    private final OnBackInvokedCallback createOnBackAnimationCallback() {
        return new OnBackAnimationCallback() { // from class: androidx.navigationevent.OnBackInvokedInput.createOnBackAnimationCallback.1
            @Override // android.window.OnBackAnimationCallback
            public void onBackStarted(BackEvent backEvent) {
                backEvent.getClass();
                OnBackInvokedInput.this.dispatchOnBackStarted(NavigationEventKt.toNavigationEvent(backEvent));
            }

            @Override // android.window.OnBackAnimationCallback
            public void onBackProgressed(BackEvent backEvent) {
                backEvent.getClass();
                OnBackInvokedInput.this.dispatchOnBackProgressed(NavigationEventKt.toNavigationEvent(backEvent));
            }

            @Override // android.window.OnBackInvokedCallback
            public void onBackInvoked() {
                OnBackInvokedInput.this.dispatchOnBackCompleted();
            }

            @Override // android.window.OnBackAnimationCallback
            public void onBackCancelled() {
                OnBackInvokedInput.this.dispatchOnBackCancelled();
            }
        };
    }
}
