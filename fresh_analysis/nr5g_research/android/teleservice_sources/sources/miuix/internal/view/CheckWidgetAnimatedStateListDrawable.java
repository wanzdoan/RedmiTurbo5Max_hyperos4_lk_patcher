package miuix.internal.view;

import android.content.res.Resources;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CheckWidgetAnimatedStateListDrawable extends AnimatedStateListDrawable {
    private static final String TAG = CheckWidgetAnimatedStateListDrawable.class.getName();
    protected CheckWidgetConstantState mCheckWidgetConstantState;

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return true;
    }

    protected abstract CheckWidgetConstantState newCheckWidgetConstantState();

    public CheckWidgetAnimatedStateListDrawable() {
        this.mCheckWidgetConstantState = newCheckWidgetConstantState();
    }

    protected CheckWidgetAnimatedStateListDrawable(Resources resources, Resources.Theme theme, CheckWidgetConstantState checkWidgetConstantState) {
        Drawable drawableNewDrawable;
        if (checkWidgetConstantState != null) {
            if (resources == null) {
                drawableNewDrawable = checkWidgetConstantState.mParent.newDrawable();
            } else {
                Drawable.ConstantState constantState = checkWidgetConstantState.mParent;
                if (theme == null) {
                    drawableNewDrawable = constantState.newDrawable(resources);
                } else {
                    drawableNewDrawable = constantState.newDrawable(resources, theme);
                }
            }
            if (drawableNewDrawable != null) {
                checkWidgetConstantState.mParent = drawableNewDrawable.getConstantState();
            }
            setConstantState((DrawableContainer.DrawableContainerState) checkWidgetConstantState.mParent);
            onStateChange(getState());
            jumpToCurrentState();
            CheckWidgetConstantState checkWidgetConstantState2 = this.mCheckWidgetConstantState;
            checkWidgetConstantState2.grayColor = checkWidgetConstantState.grayColor;
            checkWidgetConstantState2.blackColor = checkWidgetConstantState.blackColor;
            checkWidgetConstantState2.backGroundColor = checkWidgetConstantState.backGroundColor;
            checkWidgetConstantState2.touchAnimEnable = checkWidgetConstantState.touchAnimEnable;
            return;
        }
        Log.e(TAG, "checkWidgetConstantState is null ,but it can't be null", null);
    }

    @Override // android.graphics.drawable.AnimatedStateListDrawable, android.graphics.drawable.StateListDrawable, android.graphics.drawable.DrawableContainer
    protected void setConstantState(DrawableContainer.DrawableContainerState drawableContainerState) {
        super.setConstantState(drawableContainerState);
        if (this.mCheckWidgetConstantState == null) {
            this.mCheckWidgetConstantState = newCheckWidgetConstantState();
        }
        this.mCheckWidgetConstantState.mParent = drawableContainerState;
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mCheckWidgetConstantState;
    }

    public abstract class CheckWidgetConstantState extends Drawable.ConstantState {
        int backGroundColor;
        int backgroundDisableAlpha;
        int backgroundNormalAlpha;
        int blackColor;
        int grayColor;
        Drawable.ConstantState mParent;
        int strokeColor;
        int strokeDisableAlpha;
        int strokeNormalAlpha;
        int strokeStyle;
        float strokeWidth;
        boolean touchAnimEnable;

        protected abstract Drawable newAnimatedStateListDrawable(Resources resources, Resources.Theme theme, CheckWidgetConstantState checkWidgetConstantState);

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            if (this.mParent == null) {
                return null;
            }
            return newAnimatedStateListDrawable(null, null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            if (this.mParent == null) {
                return null;
            }
            return newAnimatedStateListDrawable(resources, null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            if (this.mParent == null) {
                return null;
            }
            return newAnimatedStateListDrawable(resources, theme, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            Drawable.ConstantState constantState = this.mParent;
            if (constantState == null) {
                return -1;
            }
            return constantState.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            Drawable.ConstantState constantState = this.mParent;
            if (constantState == null) {
                return false;
            }
            return constantState.canApplyTheme();
        }
    }
}
