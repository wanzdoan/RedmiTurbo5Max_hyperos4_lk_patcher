package com.android.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyShortcutButton extends FrameLayout implements View.OnClickListener {
    private static final int[] ICON_VIEWS = {R.id.phone_type_icon, R.id.confirmed_phone_type_icon};
    private View mCallNumberInfoView;
    private final Runnable mCancelSelectedButtonRunnable;
    private View mConfirmView;
    private boolean mConfirmViewHiding;
    private MotionEvent mPendingTouchEvent;
    private TextView mPhoneCallHint;
    private TextView mPhoneNumber;
    private TextView mPhoneTypeDescription;

    public EmergencyShortcutButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCancelSelectedButtonRunnable = new Runnable() { // from class: com.android.phone.EmergencyShortcutButton.2
            @Override // java.lang.Runnable
            public void run() {
                if (EmergencyShortcutButton.this.isAttachedToWindow()) {
                    EmergencyShortcutButton.this.hideSelectedButton();
                }
            }
        };
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (this.mPendingTouchEvent == motionEvent && zDispatchTouchEvent) {
            this.mPendingTouchEvent = null;
        }
        return zDispatchTouchEvent;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mCallNumberInfoView = findViewById(R.id.emergency_call_number_info_view);
        this.mConfirmView = findViewById(R.id.emergency_call_confirm_view);
        this.mCallNumberInfoView.setOnClickListener(this);
        this.mConfirmView.setOnClickListener(this);
        this.mPhoneNumber = (TextView) this.mCallNumberInfoView.findViewById(R.id.phone_number);
        this.mPhoneTypeDescription = (TextView) this.mCallNumberInfoView.findViewById(R.id.phone_number_description);
        this.mPhoneCallHint = (TextView) this.mConfirmView.findViewById(R.id.phone_call_hint);
        this.mConfirmViewHiding = true;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.emergency_call_number_info_view) {
            if (((AccessibilityManager) getContext().getSystemService("accessibility")).isTouchExplorationEnabled()) {
                return;
            }
            revealSelectedButton();
        } else {
            view.getId();
            int i = R.id.ALT;
        }
    }

    private void revealSelectedButton() {
        this.mConfirmViewHiding = false;
        this.mConfirmView.setVisibility(0);
        int left = this.mCallNumberInfoView.getLeft() + (this.mCallNumberInfoView.getWidth() / 2);
        int top = this.mCallNumberInfoView.getTop() + (this.mCallNumberInfoView.getHeight() / 2);
        View view = this.mConfirmView;
        ViewAnimationUtils.createCircularReveal(view, left, top, 0.0f, Math.max(left, view.getWidth() - left) + Math.max(top, this.mConfirmView.getHeight() - top)).start();
        postDelayed(this.mCancelSelectedButtonRunnable, 3000L);
        this.mConfirmView.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideSelectedButton() {
        if (this.mConfirmViewHiding || this.mConfirmView.getVisibility() != 0) {
            return;
        }
        this.mConfirmViewHiding = true;
        removeCallbacks(this.mCancelSelectedButtonRunnable);
        int left = this.mConfirmView.getLeft() + (this.mConfirmView.getWidth() / 2);
        int top = this.mConfirmView.getTop() + (this.mConfirmView.getHeight() / 2);
        Animator animatorCreateCircularReveal = ViewAnimationUtils.createCircularReveal(this.mConfirmView, left, top, Math.max(left, this.mCallNumberInfoView.getWidth() - left) + Math.max(top, this.mCallNumberInfoView.getHeight() - top), 0.0f);
        animatorCreateCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.android.phone.EmergencyShortcutButton.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                EmergencyShortcutButton.this.mConfirmView.setVisibility(4);
            }
        });
        animatorCreateCircularReveal.start();
        this.mCallNumberInfoView.requestFocus();
    }
}
