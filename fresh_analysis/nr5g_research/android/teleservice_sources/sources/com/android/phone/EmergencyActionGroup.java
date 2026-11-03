package com.android.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyActionGroup extends FrameLayout implements View.OnClickListener {
    private final Interpolator mFastOutLinearInInterpolator;
    private final Runnable mHideRunnable;
    private boolean mHiding;
    private View mLastRevealed;
    private View mLaunchHint;
    private final View.OnLayoutChangeListener mLayoutChangeListener;
    private MotionEvent mPendingTouchEvent;
    private final Runnable mRippleRunnable;
    private View mRippleView;
    private ViewGroup mSelectedContainer;
    private TextView mSelectedLabel;

    public EmergencyActionGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.android.phone.EmergencyActionGroup.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                EmergencyActionGroup.this.decreaseAutoSizeMinTextSize(view);
            }
        };
        this.mHideRunnable = new Runnable() { // from class: com.android.phone.EmergencyActionGroup.4
            @Override // java.lang.Runnable
            public void run() {
                if (EmergencyActionGroup.this.isAttachedToWindow()) {
                    EmergencyActionGroup.this.hideTheButton();
                }
            }
        };
        this.mRippleRunnable = new Runnable() { // from class: com.android.phone.EmergencyActionGroup.5
            @Override // java.lang.Runnable
            public void run() {
                if (EmergencyActionGroup.this.isAttachedToWindow()) {
                    EmergencyActionGroup.this.startRipple();
                }
            }
        };
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_linear_in);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.selected_container);
        this.mSelectedContainer = viewGroup;
        viewGroup.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.selected_label);
        this.mSelectedLabel = textView;
        textView.addOnLayoutChangeListener(this.mLayoutChangeListener);
        this.mRippleView = findViewById(R.id.ripple_view);
        View viewFindViewById = findViewById(R.id.launch_hint);
        this.mLaunchHint = viewFindViewById;
        viewFindViewById.addOnLayoutChangeListener(this.mLayoutChangeListener);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i == 0) {
            setupAssistActions();
        }
    }

    public void onPreTouchEvent(MotionEvent motionEvent) {
        this.mPendingTouchEvent = motionEvent;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (this.mPendingTouchEvent == motionEvent && zDispatchTouchEvent) {
            this.mPendingTouchEvent = null;
        }
        return zDispatchTouchEvent;
    }

    public void onPostTouchEvent(MotionEvent motionEvent) {
        if (this.mPendingTouchEvent != null) {
            hideTheButton();
        }
        this.mPendingTouchEvent = null;
    }

    private void setupAssistActions() {
        int i;
        int[] iArr = {R.id.action1, R.id.action2, R.id.action3};
        List listResolveAssistPackageAndQueryActivities = EmergencyAssistanceHelper.resolveAssistPackageAndQueryActivities(getContext());
        for (int i2 = 0; i2 < 3; i2++) {
            Button button = (Button) findViewById(iArr[i2]);
            button.setOnClickListener(this);
            if (listResolveAssistPackageAndQueryActivities == null || listResolveAssistPackageAndQueryActivities.size() <= i2 || listResolveAssistPackageAndQueryActivities.get(i2) == null) {
                i = 8;
            } else {
                ResolveInfo resolveInfo = (ResolveInfo) listResolveAssistPackageAndQueryActivities.get(i2);
                button.setTag(R.id.tag_intent, new Intent(EmergencyAssistanceHelper.getIntentAction()).setComponent(EmergencyAssistanceHelper.getComponentName(resolveInfo)));
                button.setText(resolveInfo.loadLabel(getContext().getPackageManager()));
                i = 0;
            }
            button.setVisibility(i);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Intent intent = (Intent) view.getTag(R.id.tag_intent);
        if (view.getId() == R.id.action1 || view.getId() == R.id.action2 || view.getId() == R.id.action3) {
            if (((AccessibilityManager) getContext().getSystemService("accessibility")).isTouchExplorationEnabled()) {
                getContext().startActivity(intent);
                return;
            } else {
                revealTheButton(view);
                return;
            }
        }
        if (view.getId() != R.id.selected_container || this.mHiding) {
            return;
        }
        getContext().startActivity(intent);
    }

    private void revealTheButton(View view) {
        this.mSelectedLabel.setAutoSizeTextTypeWithDefaults(1);
        this.mSelectedLabel.requestLayout();
        this.mLaunchHint.requestLayout();
        this.mSelectedContainer.setVisibility(0);
        int left = view.getLeft() + (view.getWidth() / 2);
        int top = view.getTop() + (view.getHeight() / 2);
        ViewGroup viewGroup = this.mSelectedContainer;
        Animator animatorCreateCircularReveal = ViewAnimationUtils.createCircularReveal(viewGroup, left, top, 0.0f, Math.max(left, viewGroup.getWidth() - left) + Math.max(top, this.mSelectedContainer.getHeight() - top));
        animatorCreateCircularReveal.start();
        animateHintText(this.mSelectedLabel, view, animatorCreateCircularReveal);
        animateHintText(this.mLaunchHint, view, animatorCreateCircularReveal);
        ViewGroup viewGroup2 = this.mSelectedContainer;
        int i = R.id.tag_intent;
        viewGroup2.setTag(i, view.getTag(i));
        this.mLastRevealed = view;
        postDelayed(this.mHideRunnable, 3000L);
        postDelayed(this.mRippleRunnable, 500L);
        this.mSelectedContainer.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decreaseAutoSizeMinTextSize(View view) {
        if (view == null || !(view instanceof TextView)) {
            return;
        }
        TextView textView = (TextView) view;
        textView.setEllipsize(TextUtils.TruncateAt.END);
        Layout layout = textView.getLayout();
        if (layout != null) {
            int ellipsisCount = layout.getEllipsisCount(textView.getMaxLines() - 1);
            int autoSizeMaxTextSize = textView.getAutoSizeMaxTextSize();
            Log.i("EmergencyActionGroup", "ellipsisCount: " + ellipsisCount + "， autoSizeMaxTextSize:" + autoSizeMaxTextSize);
            if (ellipsisCount <= 0 || autoSizeMaxTextSize <= 8) {
                return;
            }
            textView.setAutoSizeTextTypeUniformWithConfiguration(8, textView.getAutoSizeMaxTextSize(), textView.getAutoSizeStepGranularity(), 2);
            textView.setGravity(17);
        }
    }

    private void animateHintText(View view, View view2, Animator animator) {
        view.setTranslationX(((view2.getLeft() + (view2.getWidth() / 2)) - (this.mSelectedContainer.getWidth() / 2)) / 5);
        view.animate().setDuration(animator.getDuration() / 3).setStartDelay(animator.getDuration() / 5).translationX(0.0f).setInterpolator(this.mFastOutLinearInInterpolator).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideTheButton() {
        if (this.mHiding || this.mSelectedContainer.getVisibility() != 0) {
            return;
        }
        this.mHiding = true;
        removeCallbacks(this.mHideRunnable);
        View view = this.mLastRevealed;
        int left = view.getLeft() + (view.getWidth() / 2);
        int top = view.getTop() + (view.getHeight() / 2);
        ViewGroup viewGroup = this.mSelectedContainer;
        Animator animatorCreateCircularReveal = ViewAnimationUtils.createCircularReveal(viewGroup, left, top, Math.max(left, viewGroup.getWidth() - left) + Math.max(top, this.mSelectedContainer.getHeight() - top), 0.0f);
        animatorCreateCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.android.phone.EmergencyActionGroup.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                EmergencyActionGroup.this.mSelectedContainer.setVisibility(4);
                EmergencyActionGroup emergencyActionGroup = EmergencyActionGroup.this;
                emergencyActionGroup.removeCallbacks(emergencyActionGroup.mRippleRunnable);
                EmergencyActionGroup.this.mHiding = false;
            }
        });
        animatorCreateCircularReveal.start();
        if (this.mSelectedContainer.isFocused()) {
            view.requestFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRipple() {
        final View view = this.mRippleView;
        view.animate().cancel();
        view.setVisibility(0);
        Animator animatorCreateCircularReveal = ViewAnimationUtils.createCircularReveal(view, view.getLeft() + (view.getWidth() / 2), view.getTop() + (view.getHeight() / 2), 0.0f, view.getWidth() / 2);
        animatorCreateCircularReveal.setDuration(600L);
        animatorCreateCircularReveal.start();
        view.setAlpha(0.0f);
        view.animate().alpha(1.0f).setDuration(300L).withEndAction(new Runnable() { // from class: com.android.phone.EmergencyActionGroup.3
            @Override // java.lang.Runnable
            public void run() {
                view.animate().alpha(0.0f).setDuration(300L).withEndAction(new Runnable() { // from class: com.android.phone.EmergencyActionGroup.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        view.setVisibility(4);
                        EmergencyActionGroup emergencyActionGroup = EmergencyActionGroup.this;
                        emergencyActionGroup.postDelayed(emergencyActionGroup.mRippleRunnable, 1000L);
                    }
                }).start();
            }
        }).start();
    }
}
