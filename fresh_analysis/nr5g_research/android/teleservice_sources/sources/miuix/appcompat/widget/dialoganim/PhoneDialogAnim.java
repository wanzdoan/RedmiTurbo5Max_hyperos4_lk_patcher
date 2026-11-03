package miuix.appcompat.widget.dialoganim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.WindowInsetsController;
import android.view.animation.DecelerateInterpolator;
import androidx.core.view.WindowInsetsCompat;
import java.lang.ref.WeakReference;
import java.util.Collection;
import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.IFolme;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ValueProperty;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.widget.DialogAnimHelper;
import miuix.core.util.MiuixTraceUtils;
import miuix.core.util.MiuixUIUtils;
import miuix.core.util.SystemProperties;
import miuix.core.util.WindowUtils;
import miuix.internal.util.AnimHelper;

/* JADX INFO: loaded from: classes2.dex */
public class PhoneDialogAnim implements IDialogAnim {
    private static final ValueProperty DIALOG_ANIM_Y = new ValueProperty("animY", 1.0f) { // from class: miuix.appcompat.widget.dialoganim.PhoneDialogAnim.1
        @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
        public float getValue(PhoneDialogAnim phoneDialogAnim) {
            return phoneDialogAnim.mAnimY;
        }

        @Override // miuix.animation.property.ValueProperty, miuix.animation.property.FloatProperty
        public void setValue(PhoneDialogAnim phoneDialogAnim, float f) {
            phoneDialogAnim.mAnimY = f;
        }
    };
    private static WeakReference sValueAnimatorWeakRef;
    private float mAnimY;
    private IFolme mAnimator;
    private WindowInsetsAnimationController mWindowInsetsAnimationController;
    private boolean mIsDebugMode = false;
    private boolean mDiscardImeAnimEnabled = false;
    private int mImeHeight = 0;

    public PhoneDialogAnim() {
        isDebugEnabled();
    }

    private boolean isDebugEnabled() {
        String str = "";
        try {
            String str2 = SystemProperties.get("log.tag.alertdialog.ime.debug.enable");
            if (str2 != null) {
                str = str2;
            }
        } catch (Exception e) {
            Log.i("PhoneDialogAnim", "can not access property log.tag.alertdialog.ime.enable, debug mode disabled", e);
        }
        boolean zEquals = TextUtils.equals("true", str);
        this.mIsDebugMode = zEquals;
        return zEquals;
    }

    public void setDiscardImeAnimEnabled(boolean z) {
        this.mDiscardImeAnimEnabled = z;
    }

    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void cancelAnimator() {
        ValueAnimator valueAnimator;
        WeakReference weakReference = sValueAnimatorWeakRef;
        if (weakReference != null && (valueAnimator = (ValueAnimator) weakReference.get()) != null) {
            valueAnimator.cancel();
        }
        IFolme iFolme = this.mAnimator;
        if (iFolme != null) {
            iFolme.cancel();
        }
    }

    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void executeDismissAnim(View view, View view2, DialogAnimHelper.OnDismiss onDismiss) {
        if ("hide".equals(view.getTag())) {
            return;
        }
        dismissPanel(view, onDismiss);
        DimAnimator.dismiss(view2);
    }

    private void dismissPanel(View view, DialogAnimHelper.OnDismiss onDismiss) {
        if (view == null) {
            return;
        }
        int height = view.getHeight() + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).bottomMargin;
        if (AnimHelper.isDialogDebugInAndroidUIThreadEnabled()) {
            ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ViewProperty.TRANSLATION_Y, view.getTranslationY(), height));
            objectAnimatorOfPropertyValuesHolder.setInterpolator(new DecelerateInterpolator(1.5f));
            objectAnimatorOfPropertyValuesHolder.addListener(new WeakRefDismissOnAndroidUIListener(view, onDismiss));
            objectAnimatorOfPropertyValuesHolder.setDuration(200L);
            objectAnimatorOfPropertyValuesHolder.start();
            return;
        }
        AnimConfig animConfig = new AnimConfig();
        animConfig.setEase(FolmeEase.decelerate(1.5f, 200L));
        animConfig.addListeners(new WeakRefDismissListener(view, onDismiss));
        animConfig.addListeners(new WeakRefUpdateListener(view));
        if (this.mAnimator == null) {
            this.mAnimator = Folme.use(this);
        }
        if (view.getTranslationY() < 0.0f) {
            this.mAnimator.to(DIALOG_ANIM_Y, Float.valueOf(height), animConfig);
            WindowInsetsController windowInsetsController = view.getWindowInsetsController();
            if (windowInsetsController == null || MiuixUIUtils.isInMultiWindowMode(view.getContext())) {
                return;
            }
            setupImeAnimation(windowInsetsController, view, animConfig, height);
            return;
        }
        Folme.use(view).to(ViewProperty.TRANSLATION_Y, Integer.valueOf(height), animConfig);
    }

    @Override // miuix.appcompat.widget.dialoganim.IDialogAnim
    public void executeShowAnim(final View view, View view2, final boolean z, final AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener) {
        if ("show".equals(view.getTag())) {
            return;
        }
        this.mImeHeight = 0;
        final int i = ((ViewGroup.MarginLayoutParams) view2.getLayoutParams()).bottomMargin;
        if (view.getScaleX() != 1.0f) {
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
        final AnimLayoutChangeListener animLayoutChangeListener = new AnimLayoutChangeListener(view, view2) { // from class: miuix.appcompat.widget.dialoganim.PhoneDialogAnim.2
            @Override // miuix.appcompat.widget.dialoganim.PhoneDialogAnim.AnimLayoutChangeListener, android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view3, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
                Insets insets;
                super.onLayoutChange(view3, i2, i3, i4, i5, i6, i7, i8, i9);
                WindowInsets rootWindowInsets = view3.getRootWindowInsets();
                if (rootWindowInsets != null) {
                    boolean zIsVisible = rootWindowInsets.isVisible(WindowInsets.Type.ime());
                    insets = rootWindowInsets.getInsets(WindowInsets.Type.ime());
                    Insets insets2 = rootWindowInsets.getInsets(WindowInsets.Type.navigationBars());
                    if (zIsVisible) {
                        PhoneDialogAnim.this.mImeHeight = insets.bottom - insets2.bottom;
                    }
                } else {
                    insets = null;
                }
                Context context = view3.getContext();
                if (isInMultiScreenMode(context) && isInMultiScreenBottom(context)) {
                    updateDimBgMargin(i + (insets != null ? insets.bottom : 0));
                }
            }
        };
        if (view.getHeight() > 0) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: miuix.appcompat.widget.dialoganim.PhoneDialogAnim.3
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view3, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
                    view3.removeOnLayoutChangeListener(this);
                    Rect contentViewMargins = PhoneDialogAnim.this.getContentViewMargins(view);
                    if (PhoneDialogAnim.this.mIsDebugMode) {
                        Log.i("PhoneDialogAnim", "onLayoutChange: contentView.height > 0, contentViewMargins: " + contentViewMargins);
                    }
                    int height = view.getHeight() + contentViewMargins.bottom;
                    PhoneDialogAnim.relayoutView(view3, height, false);
                    PhoneDialogAnim.this.doExecuteShowAnim(view3, height, 0, z, onDialogShowAnimListener, animLayoutChangeListener);
                    view3.setVisibility(0);
                }
            });
            view.setVisibility(4);
            view.setAlpha(1.0f);
        } else {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: miuix.appcompat.widget.dialoganim.PhoneDialogAnim.4
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view3, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
                    view3.removeOnLayoutChangeListener(this);
                    Rect contentViewMargins = PhoneDialogAnim.this.getContentViewMargins(view3);
                    if (PhoneDialogAnim.this.mIsDebugMode) {
                        Log.i("PhoneDialogAnim", "onLayoutChange: contentView.height <= 0, contentViewMargins: " + contentViewMargins);
                    }
                    int i10 = (i5 - i3) + contentViewMargins.bottom;
                    PhoneDialogAnim.relayoutView(view3, i10, false);
                    PhoneDialogAnim.this.doExecuteShowAnim(view3, i10, 0, z, onDialogShowAnimListener, animLayoutChangeListener);
                }
            });
        }
        DimAnimator.show(view2);
    }

    private void setupImeAnimation(WindowInsetsController windowInsetsController, final View view, final AnimConfig animConfig, final int i) {
        windowInsetsController.controlWindowInsetsAnimation(WindowInsetsCompat.Type.ime(), -1L, null, null, new WindowInsetsAnimationControlListener() { // from class: miuix.appcompat.widget.dialoganim.PhoneDialogAnim.5
            @Override // android.view.WindowInsetsAnimationControlListener
            public void onReady(WindowInsetsAnimationController windowInsetsAnimationController, int i2) {
                if (PhoneDialogAnim.this.mAnimator != null) {
                    PhoneDialogAnim.this.mAnimator.cancel();
                }
                PhoneDialogAnim.this.mWindowInsetsAnimationController = windowInsetsAnimationController;
                final Insets shownStateInsets = PhoneDialogAnim.this.mWindowInsetsAnimationController.getShownStateInsets();
                final Insets hiddenStateInsets = PhoneDialogAnim.this.mWindowInsetsAnimationController.getHiddenStateInsets();
                final float translationY = view.getTranslationY();
                float f = i;
                animConfig.addListeners(new TransitionListener() { // from class: miuix.appcompat.widget.dialoganim.PhoneDialogAnim.5.1
                    @Override // miuix.animation.listener.TransitionListener
                    public void onUpdate(Object obj, Collection collection) {
                        UpdateInfo updateInfoFindBy;
                        if (PhoneDialogAnim.this.mWindowInsetsAnimationController == null || (updateInfoFindBy = UpdateInfo.findBy(collection, ViewProperty.TRANSLATION_Y)) == null) {
                            return;
                        }
                        float floatValue = updateInfoFindBy.getFloatValue();
                        float f2 = floatValue - translationY;
                        int i3 = shownStateInsets.bottom;
                        if (f2 > i3 || i3 == 0 || floatValue >= 0.0f) {
                            if (PhoneDialogAnim.this.mWindowInsetsAnimationController.isFinished()) {
                                return;
                            }
                            PhoneDialogAnim.this.mWindowInsetsAnimationController.finish(false);
                        } else {
                            float fMax = Math.max(1.0f - (f2 / i3), 0.0f);
                            Insets insets = shownStateInsets;
                            int i4 = insets.left;
                            Insets insets2 = hiddenStateInsets;
                            PhoneDialogAnim.this.mWindowInsetsAnimationController.setInsetsAndAlpha(Insets.of((int) (((i4 - insets2.left) * fMax) + 0.5f), (int) (((insets.top - insets2.top) * fMax) + 0.5f), (int) (((insets.right - insets2.right) * fMax) + 0.5f), (int) (((insets.bottom - insets2.bottom) * fMax) + 0.5f)), 1.0f, fMax);
                        }
                    }

                    @Override // miuix.animation.listener.TransitionListener
                    public void onComplete(Object obj) {
                        animConfig.removeListeners(this);
                    }

                    @Override // miuix.animation.listener.TransitionListener
                    public void onCancel(Object obj) {
                        animConfig.removeListeners(this);
                    }
                });
                IFolme iFolmeUse = Folme.use(view);
                ViewProperty viewProperty = ViewProperty.TRANSLATION_Y;
                iFolmeUse.setTo(viewProperty, Float.valueOf(translationY)).to(viewProperty, Float.valueOf(f), animConfig);
            }

            @Override // android.view.WindowInsetsAnimationControlListener
            public void onFinished(WindowInsetsAnimationController windowInsetsAnimationController) {
                PhoneDialogAnim.this.mWindowInsetsAnimationController = null;
            }

            @Override // android.view.WindowInsetsAnimationControlListener
            public void onCancelled(WindowInsetsAnimationController windowInsetsAnimationController) {
                if (PhoneDialogAnim.this.mWindowInsetsAnimationController == null) {
                    Folme.use(view).to(ViewProperty.TRANSLATION_Y, Integer.valueOf(i), animConfig);
                }
                PhoneDialogAnim.this.mWindowInsetsAnimationController = null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doExecuteShowAnim(View view, int i, int i2, boolean z, AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener, View.OnLayoutChangeListener onLayoutChangeListener) {
        if (AnimHelper.isDialogDebugInAndroidUIThreadEnabled()) {
            executeShowAnimAndroidUIThread(i, i2, new WeakRefShowOnAndroidUIListener(onDialogShowAnimListener, onLayoutChangeListener, view, 0), new WeakRefUpdateOnAndroidUIListener(view, z));
            return;
        }
        AnimConfig animConfig = new AnimConfig();
        animConfig.setEase(EaseManager.getStyle(-2, 0.82f, 0.3f));
        animConfig.addListeners(new WeakRefShowListener(onDialogShowAnimListener, onLayoutChangeListener, view, 0));
        if (this.mAnimator == null) {
            this.mAnimator = Folme.use(this);
        }
        IFolme iFolme = this.mAnimator;
        ValueProperty valueProperty = DIALOG_ANIM_Y;
        iFolme.setTo(valueProperty, Float.valueOf(i)).to(valueProperty, Float.valueOf(i2), animConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getContentViewMargins(View view) {
        Rect rect = new Rect();
        if (view == null) {
            rect.left = 0;
            rect.top = 0;
            rect.right = 0;
            rect.bottom = 0;
            return rect;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            rect.left = marginLayoutParams.leftMargin;
            rect.top = marginLayoutParams.topMargin;
            rect.right = marginLayoutParams.rightMargin;
            rect.bottom = marginLayoutParams.bottomMargin;
        }
        return rect;
    }

    private void executeShowAnimAndroidUIThread(int i, int i2, WeakRefShowOnAndroidUIListener weakRefShowOnAndroidUIListener, WeakRefUpdateOnAndroidUIListener weakRefUpdateOnAndroidUIListener) {
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(i, i2);
        valueAnimatorOfInt.setDuration(350L);
        valueAnimatorOfInt.setInterpolator(EaseManager.getInterpolator(0, 0.82f, 0.3f));
        valueAnimatorOfInt.addUpdateListener(weakRefUpdateOnAndroidUIListener);
        valueAnimatorOfInt.addListener(weakRefShowOnAndroidUIListener);
        valueAnimatorOfInt.start();
        sValueAnimatorWeakRef = new WeakReference(valueAnimatorOfInt);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void relayoutView(View view, int i, boolean z) {
        if (z) {
            view.animate().cancel();
            view.animate().setDuration(100L).translationY(i).start();
        } else {
            view.animate().cancel();
            view.setTranslationY(i);
        }
    }

    class WeakRefDismissOnAndroidUIListener implements Animator.AnimatorListener {
        WeakReference mOnDismiss;
        int mTraceCookie = MiuixTraceUtils.generateUniqueCookie();
        WeakReference mView;

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        WeakRefDismissOnAndroidUIListener(View view, DialogAnimHelper.OnDismiss onDismiss) {
            this.mOnDismiss = new WeakReference(onDismiss);
            this.mView = new WeakReference(view);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            MiuixTraceUtils.beginAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag("hide");
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag(null);
            }
            DialogAnimHelper.OnDismiss onDismiss = (DialogAnimHelper.OnDismiss) this.mOnDismiss.get();
            if (onDismiss != null) {
                onDismiss.end();
            } else {
                Log.d("PhoneDialogAnim", "onComplete mOnDismiss get null");
            }
            MiuixTraceUtils.endAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag(null);
            }
            DialogAnimHelper.OnDismiss onDismiss = (DialogAnimHelper.OnDismiss) this.mOnDismiss.get();
            if (onDismiss != null) {
                onDismiss.end();
            } else {
                Log.d("PhoneDialogAnim", "onCancel mOnDismiss get null");
            }
            MiuixTraceUtils.endAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
        }
    }

    class WeakRefShowOnAndroidUIListener extends AnimatorListenerAdapter {
        int mEndTranslateY;
        View.OnLayoutChangeListener mOnLayoutChange;
        WeakReference mOnShow;
        int mTraceCookie = MiuixTraceUtils.generateUniqueCookie();
        WeakReference mView;

        WeakRefShowOnAndroidUIListener(AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener, View.OnLayoutChangeListener onLayoutChangeListener, View view, int i) {
            this.mOnShow = new WeakReference(onDialogShowAnimListener);
            this.mOnLayoutChange = onLayoutChangeListener;
            this.mView = new WeakReference(view);
            this.mEndTranslateY = i;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator, boolean z) {
            MiuixTraceUtils.beginAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag("show");
                View.OnLayoutChangeListener onLayoutChangeListener = this.mOnLayoutChange;
                if (onLayoutChangeListener != null) {
                    view.addOnLayoutChangeListener(onLayoutChangeListener);
                }
            }
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = (AlertDialog.OnDialogShowAnimListener) this.mOnShow.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimStart();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            WindowInsets rootWindowInsets;
            super.onAnimationEnd(animator);
            done();
            View view = (View) this.mView.get();
            if (view != null && (rootWindowInsets = view.getRootWindowInsets()) != null) {
                boolean zIsVisible = rootWindowInsets.isVisible(WindowInsets.Type.ime());
                Insets insets = rootWindowInsets.getInsets(WindowInsets.Type.ime());
                Insets insets2 = rootWindowInsets.getInsets(WindowInsets.Type.navigationBars());
                if (!zIsVisible || PhoneDialogAnim.this.mDiscardImeAnimEnabled) {
                    PhoneDialogAnim.this.mImeHeight = 0;
                } else {
                    PhoneDialogAnim.this.mImeHeight = insets.bottom - insets2.bottom;
                }
                if (PhoneDialogAnim.this.mIsDebugMode) {
                    Log.d("PhoneDialogAnim", "onAnimationEnd: isImeVisible = " + zIsVisible + ", mImeHeight = " + PhoneDialogAnim.this.mImeHeight);
                    StringBuilder sb = new StringBuilder("onAnimationEnd: imeInsets = ");
                    sb.append(insets);
                    Log.d("PhoneDialogAnim", sb.toString());
                    Log.d("PhoneDialogAnim", "onAnimationEnd: navigationBarInsets = " + insets2);
                    Log.d("PhoneDialogAnim", "onAnimationEnd: newValue = " + (this.mEndTranslateY - PhoneDialogAnim.this.mImeHeight));
                    Log.d("PhoneDialogAnim", "onAnimationEnd: mDiscardImeAnimEnabled = " + PhoneDialogAnim.this.mDiscardImeAnimEnabled);
                }
                PhoneDialogAnim.relayoutView(view, this.mEndTranslateY - PhoneDialogAnim.this.mImeHeight, true);
            }
            this.mOnShow.clear();
            this.mView.clear();
            MiuixTraceUtils.endAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            done();
            View view = (View) this.mView.get();
            if (view != null) {
                PhoneDialogAnim.relayoutView(view, this.mEndTranslateY, true);
            }
            this.mOnShow.clear();
            this.mView.clear();
            MiuixTraceUtils.endAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
        }

        private void done() {
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag(null);
                View.OnLayoutChangeListener onLayoutChangeListener = this.mOnLayoutChange;
                if (onLayoutChangeListener != null) {
                    view.removeOnLayoutChangeListener(onLayoutChangeListener);
                    this.mOnLayoutChange = null;
                }
            }
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = (AlertDialog.OnDialogShowAnimListener) this.mOnShow.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimComplete();
            }
            if (PhoneDialogAnim.sValueAnimatorWeakRef != null) {
                PhoneDialogAnim.sValueAnimatorWeakRef.clear();
                WeakReference unused = PhoneDialogAnim.sValueAnimatorWeakRef = null;
            }
        }
    }

    class WeakRefUpdateOnAndroidUIListener implements ValueAnimator.AnimatorUpdateListener {
        boolean mIsLandscape;
        WeakReference mView;

        WeakRefUpdateOnAndroidUIListener(View view, boolean z) {
            this.mView = new WeakReference(view);
            this.mIsLandscape = z;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            View view = (View) this.mView.get();
            if (view == null) {
                return;
            }
            if ("hide".equals(view.getTag())) {
                valueAnimator.cancel();
                return;
            }
            WindowInsets rootWindowInsets = view.getRootWindowInsets();
            if (rootWindowInsets != null) {
                boolean zIsVisible = rootWindowInsets.isVisible(WindowInsets.Type.ime());
                Insets insets = rootWindowInsets.getInsets(WindowInsets.Type.ime());
                Insets insets2 = rootWindowInsets.getInsets(WindowInsets.Type.navigationBars());
                if (!zIsVisible || PhoneDialogAnim.this.mDiscardImeAnimEnabled) {
                    PhoneDialogAnim.this.mImeHeight = 0;
                } else {
                    PhoneDialogAnim.this.mImeHeight = insets.bottom - insets2.bottom;
                }
                if (PhoneDialogAnim.this.mIsDebugMode) {
                    Log.d("PhoneDialogAnim", "onAnimationUpdate: isImeVisible = " + zIsVisible + ", mImeHeight = " + PhoneDialogAnim.this.mImeHeight);
                    StringBuilder sb = new StringBuilder("onAnimationUpdate: imeInsets = ");
                    sb.append(insets);
                    Log.d("PhoneDialogAnim", sb.toString());
                    Log.d("PhoneDialogAnim", "onAnimationUpdate: navigationBarInsets = " + insets2);
                    Log.d("PhoneDialogAnim", "onAnimationUpdate: mDiscardImeAnimEnabled = " + PhoneDialogAnim.this.mDiscardImeAnimEnabled);
                }
            }
            int iIntValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (PhoneDialogAnim.this.mIsDebugMode) {
                Log.d("PhoneDialogAnim", "onAnimationUpdate: newValue = " + (iIntValue - PhoneDialogAnim.this.mImeHeight));
            }
            PhoneDialogAnim.relayoutView(view, iIntValue - PhoneDialogAnim.this.mImeHeight, false);
        }
    }

    class WeakRefShowListener extends TransitionListener {
        int mEndTranslateY;
        View.OnLayoutChangeListener mOnLayoutChange;
        WeakReference mOnShow;
        int mTraceCookie = MiuixTraceUtils.generateUniqueCookie();
        WeakReference mView;

        WeakRefShowListener(AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener, View.OnLayoutChangeListener onLayoutChangeListener, View view, int i) {
            this.mOnShow = new WeakReference(onDialogShowAnimListener);
            this.mOnLayoutChange = onLayoutChangeListener;
            this.mView = new WeakReference(view);
            this.mEndTranslateY = i;
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            MiuixTraceUtils.beginAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag("show");
                View.OnLayoutChangeListener onLayoutChangeListener = this.mOnLayoutChange;
                if (onLayoutChangeListener != null) {
                    view.addOnLayoutChangeListener(onLayoutChangeListener);
                }
            }
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = (AlertDialog.OnDialogShowAnimListener) this.mOnShow.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimStart();
            }
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection collection) {
            View view = (View) this.mView.get();
            if (view == null) {
                return;
            }
            WindowInsets rootWindowInsets = view.getRootWindowInsets();
            if (rootWindowInsets != null) {
                boolean zIsVisible = rootWindowInsets.isVisible(WindowInsets.Type.ime());
                Insets insets = rootWindowInsets.getInsets(WindowInsets.Type.ime());
                Insets insets2 = rootWindowInsets.getInsets(WindowInsets.Type.navigationBars());
                if (!zIsVisible || PhoneDialogAnim.this.mDiscardImeAnimEnabled) {
                    PhoneDialogAnim.this.mImeHeight = 0;
                } else {
                    PhoneDialogAnim.this.mImeHeight = insets.bottom - insets2.bottom;
                }
                if (PhoneDialogAnim.this.mIsDebugMode) {
                    Log.d("PhoneDialogAnim", "onAnimationEnd: isImeVisible = " + zIsVisible + ", mImeHeight = " + PhoneDialogAnim.this.mImeHeight);
                    StringBuilder sb = new StringBuilder("onAnimationEnd: imeInsets = ");
                    sb.append(insets);
                    Log.d("PhoneDialogAnim", sb.toString());
                    Log.d("PhoneDialogAnim", "onAnimationEnd: navigationBarInsets = " + insets2);
                    Log.d("PhoneDialogAnim", "onAnimationEnd: newValue = " + this.mEndTranslateY);
                }
            }
            UpdateInfo updateInfoFindBy = UpdateInfo.findBy(collection, PhoneDialogAnim.DIALOG_ANIM_Y);
            if (updateInfoFindBy != null) {
                view.setTranslationY(updateInfoFindBy.getFloatValue() - PhoneDialogAnim.this.mImeHeight);
            }
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            WindowInsets rootWindowInsets;
            done();
            View view = (View) this.mView.get();
            if (view != null && (rootWindowInsets = view.getRootWindowInsets()) != null) {
                boolean zIsVisible = rootWindowInsets.isVisible(WindowInsets.Type.ime());
                Insets insets = rootWindowInsets.getInsets(WindowInsets.Type.ime());
                Insets insets2 = rootWindowInsets.getInsets(WindowInsets.Type.navigationBars());
                PhoneDialogAnim phoneDialogAnim = PhoneDialogAnim.this;
                if (zIsVisible) {
                    phoneDialogAnim.mImeHeight = insets.bottom - insets2.bottom;
                } else {
                    phoneDialogAnim.mImeHeight = 0;
                }
                if (PhoneDialogAnim.this.mIsDebugMode) {
                    Log.d("PhoneDialogAnim", "onAnimationEnd: isImeVisible = " + zIsVisible + ", mImeHeight = " + PhoneDialogAnim.this.mImeHeight);
                    StringBuilder sb = new StringBuilder("onAnimationEnd: imeInsets = ");
                    sb.append(insets);
                    Log.d("PhoneDialogAnim", sb.toString());
                    Log.d("PhoneDialogAnim", "onAnimationEnd: navigationBarInsets = " + insets2);
                    Log.d("PhoneDialogAnim", "onAnimationEnd: newValue = " + this.mEndTranslateY);
                }
            }
            this.mOnShow.clear();
            this.mView.clear();
            MiuixTraceUtils.endAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            done();
            this.mOnShow.clear();
            this.mView.clear();
            MiuixTraceUtils.endAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
        }

        private void done() {
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag(null);
                View.OnLayoutChangeListener onLayoutChangeListener = this.mOnLayoutChange;
                if (onLayoutChangeListener != null) {
                    view.removeOnLayoutChangeListener(onLayoutChangeListener);
                    this.mOnLayoutChange = null;
                }
            }
            AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener = (AlertDialog.OnDialogShowAnimListener) this.mOnShow.get();
            if (onDialogShowAnimListener != null) {
                onDialogShowAnimListener.onShowAnimComplete();
            }
        }
    }

    class WeakRefDismissListener extends TransitionListener {
        WeakReference mOnDismiss;
        int mTraceCookie = MiuixTraceUtils.generateUniqueCookie();
        WeakReference mView;

        WeakRefDismissListener(View view, DialogAnimHelper.OnDismiss onDismiss) {
            this.mOnDismiss = new WeakReference(onDismiss);
            this.mView = new WeakReference(view);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            MiuixTraceUtils.beginAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag("hide");
            }
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag(null);
            }
            DialogAnimHelper.OnDismiss onDismiss = (DialogAnimHelper.OnDismiss) this.mOnDismiss.get();
            if (onDismiss != null) {
                onDismiss.end();
            } else {
                Log.d("PhoneDialogAnim", "onComplete mOnDismiss get null");
            }
            MiuixTraceUtils.endAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            View view = (View) this.mView.get();
            if (view != null) {
                view.setTag(null);
            }
            MiuixTraceUtils.endAsyncTrace("MIUIX_Widget_Animation", this.mTraceCookie);
        }
    }

    class WeakRefUpdateListener extends TransitionListener {
        WeakReference mView;

        WeakRefUpdateListener(View view) {
            this.mView = new WeakReference(view);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onUpdate(Object obj, Collection collection) {
            View view = (View) this.mView.get();
            if (view == null) {
                return;
            }
            WindowInsets rootWindowInsets = view.getRootWindowInsets();
            if (rootWindowInsets != null) {
                boolean zIsVisible = rootWindowInsets.isVisible(WindowInsets.Type.ime());
                Insets insets = rootWindowInsets.getInsets(WindowInsets.Type.ime());
                Insets insets2 = rootWindowInsets.getInsets(WindowInsets.Type.navigationBars());
                if (!zIsVisible || PhoneDialogAnim.this.mDiscardImeAnimEnabled) {
                    PhoneDialogAnim.this.mImeHeight = 0;
                } else {
                    PhoneDialogAnim.this.mImeHeight = insets.bottom - insets2.bottom;
                }
                if (PhoneDialogAnim.this.mIsDebugMode) {
                    Log.d("PhoneDialogAnim", "onAnimationEnd: isImeVisible = " + zIsVisible + ", mImeHeight = " + PhoneDialogAnim.this.mImeHeight);
                    StringBuilder sb = new StringBuilder("onAnimationEnd: imeInsets = ");
                    sb.append(insets);
                    Log.d("PhoneDialogAnim", sb.toString());
                    Log.d("PhoneDialogAnim", "onAnimationEnd: navigationBarInsets = " + insets2);
                }
            }
            UpdateInfo updateInfoFindBy = UpdateInfo.findBy(collection, PhoneDialogAnim.DIALOG_ANIM_Y);
            if (updateInfoFindBy != null) {
                view.setTranslationY(updateInfoFindBy.getFloatValue() - PhoneDialogAnim.this.mImeHeight);
            }
        }
    }

    abstract class AnimLayoutChangeListener implements View.OnLayoutChangeListener {
        final WeakReference wkDecorView;
        final WeakReference wkDimBgView;
        final Rect windowVisibleFrame = new Rect();
        final Point screenSize = new Point();

        public AnimLayoutChangeListener(View view, View view2) {
            this.wkDecorView = new WeakReference(view.getRootView());
            this.wkDimBgView = new WeakReference(view2);
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            View view2 = (View) this.wkDecorView.get();
            if (view2 != null) {
                view2.getWindowVisibleDisplayFrame(this.windowVisibleFrame);
            }
        }

        public void updateDimBgMargin(int i) {
            View view = (View) this.wkDimBgView.get();
            if (view != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                if (marginLayoutParams.bottomMargin != i) {
                    marginLayoutParams.bottomMargin = i;
                    view.setLayoutParams(marginLayoutParams);
                }
            }
        }

        public boolean isInMultiScreenMode(Context context) {
            return MiuixUIUtils.isInMultiWindowMode(context) && !MiuixUIUtils.isFreeformMode(context);
        }

        public boolean isInMultiScreenBottom(Context context) {
            WindowUtils.getDisplay(context).getRealSize(this.screenSize);
            Rect rect = this.windowVisibleFrame;
            if (rect.left == 0) {
                int i = rect.right;
                Point point = this.screenSize;
                if (i == point.x) {
                    if (rect.top >= ((int) (point.y * 0.2f))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
