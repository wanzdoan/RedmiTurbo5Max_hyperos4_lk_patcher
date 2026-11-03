package androidx.navigationevent;

import android.window.OnBackInvokedDispatcher;
import miuix.animation.controller.AnimState;

/* JADX INFO: compiled from: OnBackInvokedInput.android.kt */
/* JADX INFO: loaded from: classes.dex */
public final class OnBackInvokedOverlayInput extends OnBackInvokedInput {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OnBackInvokedOverlayInput(OnBackInvokedDispatcher onBackInvokedDispatcher) {
        super(onBackInvokedDispatcher, AnimState.VIEW_SIZE, null);
        onBackInvokedDispatcher.getClass();
    }
}
