package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;

/* JADX INFO: loaded from: classes.dex */
class ItemTouchHelper$RecoverAnimation implements Animator.AnimatorListener {
    boolean mEnded;
    private float mFraction;
    final ValueAnimator mValueAnimator;
    final RecyclerView.ViewHolder mViewHolder;

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
    }

    public void setFraction(float f) {
        this.mFraction = f;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        if (!this.mEnded) {
            this.mViewHolder.setIsRecyclable(true);
        }
        this.mEnded = true;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
        setFraction(1.0f);
    }
}
