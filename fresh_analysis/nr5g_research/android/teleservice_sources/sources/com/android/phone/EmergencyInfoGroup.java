package com.android.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.android.internal.telephony.UserIcons;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyInfoGroup extends FrameLayout implements View.OnClickListener {
    private static final int[] ICON_VIEWS = {R.id.emergency_info_image, R.id.confirmed_emergency_info_image};
    private final Runnable mCancelSelectedButtonRunnable;
    private boolean mConfirmViewHiding;
    private View mEmergencyInfoButton;
    private View mEmergencyInfoConfirmButton;
    private TextView mEmergencyInfoName;
    private MotionEvent mPendingTouchEvent;

    public EmergencyInfoGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCancelSelectedButtonRunnable = new Runnable() { // from class: com.android.phone.EmergencyInfoGroup.2
            @Override // java.lang.Runnable
            public void run() {
                if (EmergencyInfoGroup.this.isAttachedToWindow()) {
                    EmergencyInfoGroup.this.hideSelectedButton();
                }
            }
        };
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mEmergencyInfoButton = findViewById(R.id.emergency_info_view);
        this.mEmergencyInfoName = (TextView) findViewById(R.id.emergency_info_name);
        this.mEmergencyInfoConfirmButton = findViewById(R.id.emergency_info_confirm_view);
        this.mEmergencyInfoButton.setOnClickListener(this);
        this.mEmergencyInfoConfirmButton.setOnClickListener(this);
        this.mConfirmViewHiding = true;
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i == 0) {
            setupButtonInfo();
        }
    }

    private void setupButtonInfo() {
        boolean z;
        List listResolveAssistPackageAndQueryActivities = EmergencyAssistanceHelper.resolveAssistPackageAndQueryActivities(getContext());
        if (listResolveAssistPackageAndQueryActivities == null || listResolveAssistPackageAndQueryActivities.size() <= 0) {
            z = false;
        } else {
            setTag(R.id.tag_intent, new Intent(EmergencyAssistanceHelper.getIntentAction()).setPackage(((ResolveInfo) listResolveAssistPackageAndQueryActivities.get(0)).activityInfo.packageName));
            setUserIcon();
            z = true;
        }
        this.mEmergencyInfoName.setText(getUserName());
        setVisibility(z ? 0 : 8);
    }

    private void setUserIcon() {
        for (int i : ICON_VIEWS) {
            ((ImageView) findViewById(i)).setImageDrawable(getCircularUserIcon());
        }
    }

    private Drawable getCircularUserIcon() {
        Bitmap userIcon = ((UserManager) getContext().getSystemService("user")).getUserIcon();
        if (userIcon == null) {
            userIcon = UserIcons.convertToBitmap(UserIcons.getDefaultUserIcon(getContext().getResources(), UserHandle.myUserId(), false));
        }
        RoundedBitmapDrawable roundedBitmapDrawableCreate = RoundedBitmapDrawableFactory.create(getContext().getResources(), userIcon);
        roundedBitmapDrawableCreate.setCircular(true);
        return roundedBitmapDrawableCreate;
    }

    private CharSequence getUserName() {
        String userName = ((UserManager) getContext().getSystemService("user")).getUserName();
        return TextUtils.isEmpty(userName) ? getContext().getText(R.string.emergency_information_owner_hint) : userName;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (this.mPendingTouchEvent == motionEvent && zDispatchTouchEvent) {
            this.mPendingTouchEvent = null;
        }
        return zDispatchTouchEvent;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.emergency_info_view) {
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
        this.mEmergencyInfoConfirmButton.setVisibility(0);
        int left = this.mEmergencyInfoButton.getLeft() + (this.mEmergencyInfoButton.getWidth() / 2);
        int top = this.mEmergencyInfoButton.getTop() + (this.mEmergencyInfoButton.getHeight() / 2);
        View view = this.mEmergencyInfoConfirmButton;
        ViewAnimationUtils.createCircularReveal(view, left, top, 0.0f, Math.max(left, view.getWidth() - left) + Math.max(top, this.mEmergencyInfoConfirmButton.getHeight() - top)).start();
        postDelayed(this.mCancelSelectedButtonRunnable, 3000L);
        this.mEmergencyInfoConfirmButton.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideSelectedButton() {
        if (this.mConfirmViewHiding || this.mEmergencyInfoConfirmButton.getVisibility() != 0) {
            return;
        }
        this.mConfirmViewHiding = true;
        removeCallbacks(this.mCancelSelectedButtonRunnable);
        int left = this.mEmergencyInfoConfirmButton.getLeft() + (this.mEmergencyInfoConfirmButton.getWidth() / 2);
        int top = this.mEmergencyInfoConfirmButton.getTop() + (this.mEmergencyInfoConfirmButton.getHeight() / 2);
        Animator animatorCreateCircularReveal = ViewAnimationUtils.createCircularReveal(this.mEmergencyInfoConfirmButton, left, top, Math.max(left, this.mEmergencyInfoButton.getWidth() - left) + Math.max(top, this.mEmergencyInfoButton.getHeight() - top), 0.0f);
        animatorCreateCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.android.phone.EmergencyInfoGroup.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                EmergencyInfoGroup.this.mEmergencyInfoConfirmButton.setVisibility(4);
            }
        });
        animatorCreateCircularReveal.start();
        this.mEmergencyInfoButton.requestFocus();
    }
}
