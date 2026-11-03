package androidx.activity;

import android.window.OnBackInvokedDispatcher;
import androidx.core.util.Consumer;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigationevent.NavigationEvent;
import androidx.navigationevent.NavigationEventDispatcher;
import androidx.navigationevent.NavigationEventInput;
import androidx.navigationevent.OnBackCompletedFallback;
import androidx.navigationevent.OnBackInvokedDefaultInput;
import androidx.navigationevent.OnBackInvokedOverlayInput;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;

/* JADX INFO: compiled from: OnBackPressedDispatcher.kt */
/* JADX INFO: loaded from: classes.dex */
public final class OnBackPressedDispatcher {
    private final Lazy eventInput$delegate;
    private final Runnable fallbackOnBackPressed;
    private boolean hasEnabledCallbacks;
    private final Consumer onHasEnabledCallbacksChanged;

    public OnBackPressedDispatcher(Runnable runnable, Consumer consumer) {
        this.fallbackOnBackPressed = runnable;
        this.onHasEnabledCallbacksChanged = consumer;
        this.eventInput$delegate = LazyKt.lazy(new Function0() { // from class: androidx.activity.OnBackPressedDispatcher$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return OnBackPressedDispatcher.$r8$lambda$Ce6hmwzpZeR4nJ6cSxwnPFSCXfY(this.f$0);
            }
        });
    }

    public static OnBackPressedEventInput $r8$lambda$Ce6hmwzpZeR4nJ6cSxwnPFSCXfY(OnBackPressedDispatcher onBackPressedDispatcher) {
        return onBackPressedDispatcher.new OnBackPressedEventInput();
    }

    private final OnBackPressedEventInput getEventInput() {
        return (OnBackPressedEventInput) this.eventInput$delegate.getValue();
    }

    public final NavigationEventDispatcher getEventDispatcher$activity() {
        return getEventInput().getDispatcher();
    }

    public OnBackPressedDispatcher(Runnable runnable) {
        this(runnable, null);
    }

    public final void setOnBackInvokedDispatcher(OnBackInvokedDispatcher onBackInvokedDispatcher) {
        onBackInvokedDispatcher.getClass();
        getEventDispatcher$activity().addInput(new OnBackInvokedDefaultInput(onBackInvokedDispatcher), 1);
        getEventDispatcher$activity().addInput(new OnBackInvokedOverlayInput(onBackInvokedDispatcher), 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void addCallback(OnBackPressedCallback onBackPressedCallback) {
        onBackPressedCallback.getClass();
        NavigationEventDispatcher.addHandler$default(getEventDispatcher$activity(), onBackPressedCallback.createNavigationEventHandler$activity(new OnBackPressedCallbackInfo(onBackPressedCallback, null, 2, 0 == true ? 1 : 0)), 0, 2, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v3, types: [androidx.activity.OnBackPressedDispatcher$addCallback$observer$1, androidx.lifecycle.LifecycleObserver] */
    public final void addCallback(LifecycleOwner lifecycleOwner, OnBackPressedCallback onBackPressedCallback) {
        lifecycleOwner.getClass();
        onBackPressedCallback.getClass();
        final Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        final OnBackPressedCallback.OnBackPressedEventHandler onBackPressedEventHandlerCreateNavigationEventHandler$activity = onBackPressedCallback.createNavigationEventHandler$activity(new OnBackPressedCallbackInfo(onBackPressedCallback, lifecycleOwner));
        if (ActivityFlags.isOnBackPressedLifecycleOrderMaintained) {
            onBackPressedEventHandlerCreateNavigationEventHandler$activity.setLifecycleActive(false);
            NavigationEventDispatcher.addHandler$default(getEventDispatcher$activity(), onBackPressedEventHandlerCreateNavigationEventHandler$activity, 0, 2, null);
        }
        final ?? r1 = new LifecycleEventObserver() { // from class: androidx.activity.OnBackPressedDispatcher$addCallback$observer$1

            /* JADX INFO: compiled from: OnBackPressedDispatcher.kt */
            public abstract /* synthetic */ class WhenMappings {
                public static final /* synthetic */ int[] $EnumSwitchMapping$0;

                static {
                    int[] iArr = new int[Lifecycle.Event.values().length];
                    try {
                        iArr[Lifecycle.Event.ON_START.ordinal()] = 1;
                    } catch (NoSuchFieldError unused) {
                    }
                    try {
                        iArr[Lifecycle.Event.ON_STOP.ordinal()] = 2;
                    } catch (NoSuchFieldError unused2) {
                    }
                    try {
                        iArr[Lifecycle.Event.ON_DESTROY.ordinal()] = 3;
                    } catch (NoSuchFieldError unused3) {
                    }
                    $EnumSwitchMapping$0 = iArr;
                }
            }

            @Override // androidx.lifecycle.LifecycleEventObserver
            public void onStateChanged(LifecycleOwner lifecycleOwner2, Lifecycle.Event event) {
                lifecycleOwner2.getClass();
                event.getClass();
                int i = WhenMappings.$EnumSwitchMapping$0[event.ordinal()];
                if (i == 1) {
                    if (ActivityFlags.isOnBackPressedLifecycleOrderMaintained) {
                        onBackPressedEventHandlerCreateNavigationEventHandler$activity.setLifecycleActive(true);
                        return;
                    } else {
                        NavigationEventDispatcher.addHandler$default(this.getEventDispatcher$activity(), onBackPressedEventHandlerCreateNavigationEventHandler$activity, 0, 2, null);
                        return;
                    }
                }
                if (i != 2) {
                    if (i != 3) {
                        return;
                    }
                    onBackPressedEventHandlerCreateNavigationEventHandler$activity.remove();
                    lifecycle.removeObserver(this);
                    return;
                }
                boolean z = ActivityFlags.isOnBackPressedLifecycleOrderMaintained;
                OnBackPressedCallback.OnBackPressedEventHandler onBackPressedEventHandler = onBackPressedEventHandlerCreateNavigationEventHandler$activity;
                if (z) {
                    onBackPressedEventHandler.setLifecycleActive(false);
                } else {
                    onBackPressedEventHandler.remove();
                }
            }
        };
        lifecycle.addObserver(r1);
        onBackPressedCallback.addCloseable$activity(new AutoCloseable() { // from class: androidx.activity.OnBackPressedDispatcher$$ExternalSyntheticLambda1
            @Override // java.lang.AutoCloseable
            public final void close() {
                lifecycle.removeObserver(r1);
            }
        });
    }

    public final void dispatchOnBackStarted(BackEventCompat backEventCompat) {
        backEventCompat.getClass();
        getEventInput().backStarted(backEventCompat.toNavigationEvent());
    }

    public final void dispatchOnBackProgressed(BackEventCompat backEventCompat) {
        backEventCompat.getClass();
        getEventInput().backProgressed(backEventCompat.toNavigationEvent());
    }

    public final void onBackPressed() {
        getEventInput().backCompleted();
    }

    public final void dispatchOnBackCancelled() {
        getEventInput().backCancelled();
    }

    /* JADX INFO: compiled from: OnBackPressedDispatcher.kt */
    final class OnBackPressedEventInput extends NavigationEventInput {
        private final NavigationEventDispatcher dispatcher;

        public OnBackPressedEventInput() {
            NavigationEventDispatcher navigationEventDispatcher = new NavigationEventDispatcher(new OnBackCompletedFallback() { // from class: androidx.activity.OnBackPressedDispatcher$OnBackPressedEventInput$$ExternalSyntheticLambda0
                @Override // androidx.navigationevent.OnBackCompletedFallback
                public final void onBackCompletedFallback() {
                    OnBackPressedDispatcher.OnBackPressedEventInput.m7$r8$lambda$dPEOltVNxwFUffOSXiaKZsPUiI(onBackPressedDispatcher);
                }
            });
            navigationEventDispatcher.addInput(this);
            this.dispatcher = navigationEventDispatcher;
        }

        public final NavigationEventDispatcher getDispatcher() {
            return this.dispatcher;
        }

        /* JADX INFO: renamed from: $r8$lambda$dPEOltV-NxwFUffOSXiaKZsPUiI, reason: not valid java name */
        public static void m7$r8$lambda$dPEOltVNxwFUffOSXiaKZsPUiI(OnBackPressedDispatcher onBackPressedDispatcher) {
            Runnable runnable = onBackPressedDispatcher.fallbackOnBackPressed;
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override // androidx.navigationevent.NavigationEventInput
        protected void onHasEnabledHandlersChanged(boolean z) {
            OnBackPressedDispatcher.this.hasEnabledCallbacks = z;
            Consumer consumer = OnBackPressedDispatcher.this.onHasEnabledCallbacksChanged;
            if (consumer != null) {
                consumer.accept(Boolean.valueOf(z));
            }
        }

        public final void backStarted(NavigationEvent navigationEvent) {
            navigationEvent.getClass();
            dispatchOnBackStarted(navigationEvent);
        }

        public final void backProgressed(NavigationEvent navigationEvent) {
            navigationEvent.getClass();
            dispatchOnBackProgressed(navigationEvent);
        }

        public final void backCancelled() {
            dispatchOnBackCancelled();
        }

        public final void backCompleted() {
            dispatchOnBackCompleted();
        }
    }
}
