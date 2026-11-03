package miuix.appcompat.internal.app.widget;

import android.R;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.appcompat.R$bool;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBarTransitionListener;
import miuix.appcompat.internal.view.menu.action.ActionMenuPresenter;
import miuix.appcompat.internal.view.menu.action.ActionMenuView;
import miuix.appcompat.internal.view.menu.action.EndActionMenuPresenter;
import miuix.core.util.MiuiBlurUtils;
import miuix.internal.util.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
abstract class AbsActionBarView extends ViewGroup {
    public static final ViewProperty SELF_BLUR = new ViewProperty("self_blur", 1.0f) { // from class: miuix.appcompat.internal.app.widget.AbsActionBarView.2
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setTag(R$id.miuix_animation_tag_view_self_blur, Float.valueOf(f));
            int i = (int) f;
            if (MiuiBlurUtils.setSelfBlur(view, i, null, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY)) {
                return;
            }
            MiuiBlurUtils.setSelfBlur(view, i, null);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            Object tag = view.getTag(R$id.miuix_animation_tag_view_self_blur);
            if (tag == null) {
                return 0.0f;
            }
            return ((Float) tag).floatValue();
        }
    };
    List mActionBarTransitionListeners;
    protected ActionMenuPresenter mActionMenuPresenter;
    protected TransitionListener mAnimConfigListener;
    int mBottomMenuMode;
    protected AnimConfig mCollapseAnimHideConfig;
    protected AnimConfig mCollapseAnimShowConfig;
    protected boolean mCollapseTitleColorTransitEnable;
    protected boolean mEndActionMenuEnable;
    int mExpandState;
    int mExpandStateBeforeResizing;
    int mExpandStateOnLayout;
    protected boolean mExpandTitleColorTransitEnable;
    protected AnimConfig mHideProcessConfig;
    protected boolean mHyperActionMenuEnable;
    protected boolean mHyperSplitMenuEnabled;
    int mInnerExpandState;
    protected boolean mIsInWideMode;
    float mLastProcess;
    protected int mMaxActionMenuItemCount;
    protected ActionMenuView mMenuView;
    protected AnimConfig mMovableAnimNormalConfig;
    protected AnimConfig mMovableAnimShowConfig;
    protected Rect mPendingInset;
    private boolean mResizable;
    protected AnimConfig mShowProcessConfig;
    protected boolean mSplitActionBarEnable;
    protected ActionBarContainer mSplitView;
    protected boolean mSplitWhenNarrow;
    protected int mSubtitlePaddingV;
    protected boolean mTitleClickable;
    protected int mTitleMaxHeight;
    protected int mTitleMinHeight;
    protected int mTitlePaddingV;
    protected int mUserExpandState;
    protected boolean mUserSetExpandState;
    protected View.OnClickListener mUserSubTitleClickListener;

    int getActionBarStyle() {
        return R.attr.actionBarStyle;
    }

    protected void onAnimatedExpandStateChanged(int i, int i2) {
    }

    protected abstract void onExpandStateChanged(int i, int i2);

    public abstract void refreshBottomMenu();

    AbsActionBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    AbsActionBarView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mInnerExpandState = 2;
        this.mExpandStateBeforeResizing = 2;
        this.mExpandState = 2;
        this.mExpandStateOnLayout = 2;
        this.mResizable = true;
        this.mTitleClickable = false;
        this.mLastProcess = 0.0f;
        this.mBottomMenuMode = 2;
        this.mMaxActionMenuItemCount = Integer.MIN_VALUE;
        this.mIsInWideMode = false;
        this.mAnimConfigListener = new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.AbsActionBarView.1
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                super.onBegin(obj);
                List list = AbsActionBarView.this.mActionBarTransitionListeners;
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        ((ActionBarTransitionListener) it.next()).onTransitionBegin(obj);
                    }
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection collection) {
                super.onUpdate(obj, collection);
                List list = AbsActionBarView.this.mActionBarTransitionListeners;
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        ((ActionBarTransitionListener) it.next()).onTransitionUpdate(obj, collection);
                    }
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                List list = AbsActionBarView.this.mActionBarTransitionListeners;
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        ((ActionBarTransitionListener) it.next()).onTransitionComplete(obj);
                    }
                }
            }
        };
        this.mUserSubTitleClickListener = null;
        this.mUserSetExpandState = false;
        this.mUserExpandState = -1;
        this.mTitlePaddingV = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_action_bar_title_collapse_padding_vertical);
        this.mSubtitlePaddingV = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_action_bar_subtitle_collapse_padding_vertical);
        this.mCollapseAnimShowConfig = new AnimConfig().setEase(-2, 1.0f, 0.3f);
        this.mShowProcessConfig = new AnimConfig().setEase(-2, 1.0f, 0.3f).addListeners(this.mAnimConfigListener);
        this.mCollapseAnimHideConfig = new AnimConfig().setEase(-2, 1.0f, 0.15f);
        this.mHideProcessConfig = new AnimConfig().setEase(-2, 1.0f, 0.15f).addListeners(this.mAnimConfigListener);
        this.mMovableAnimShowConfig = new AnimConfig().setEase(-2, 1.0f, 0.6f);
        this.mMovableAnimNormalConfig = new AnimConfig().setEase(-2, 1.0f, 0.6f).setSpecial(ViewProperty.ALPHA, FolmeEase.spring(1.0f, 0.15f), new float[0]).setSpecial(SELF_BLUR, FolmeEase.spring(1.0f, 0.15f), new float[0]);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionBar, R.attr.actionBarStyle, 0);
        int i2 = typedArrayObtainStyledAttributes.getInt(R$styleable.ActionBar_expandState, 2);
        boolean z = typedArrayObtainStyledAttributes.getBoolean(R$styleable.ActionBar_resizable, true);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(R$styleable.ActionBar_titleClickable, false);
        typedArrayObtainStyledAttributes.recycle();
        if (isUserSetExpandState()) {
            int i3 = this.mUserExpandState;
            this.mInnerExpandState = i3;
            this.mExpandState = i3;
        } else if (i2 == 0) {
            this.mInnerExpandState = 0;
            this.mExpandState = 0;
        } else {
            this.mInnerExpandState = 2;
            this.mExpandState = 2;
        }
        this.mResizable = z;
        this.mTitleClickable = z2;
    }

    void bindActionBarTransitionListeners(List list) {
        this.mActionBarTransitionListeners = list;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mSplitWhenNarrow) {
            setSplitActionBar(getContext().getResources().getBoolean(R$bool.abc_split_action_bar_is_narrow));
        }
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.onConfigurationChanged(configuration);
        }
    }

    public void setSplitActionBar(boolean z) {
        this.mSplitActionBarEnable = z;
    }

    public void setSplitWhenNarrow(boolean z) {
        this.mSplitWhenNarrow = z;
    }

    public void setSplitView(ActionBarContainer actionBarContainer) {
        this.mSplitView = actionBarContainer;
    }

    public ActionMenuView getActionMenuView() {
        return this.mMenuView;
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        if (i != getVisibility()) {
            super.setVisibility(i);
        }
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }

    public void postShowOverflowMenu() {
        post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.AbsActionBarView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.showOverflowMenu();
            }
        });
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu(true);
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    public boolean isOverflowReserved() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowReserved();
    }

    protected int measureChildView(View view, int i, int i2, int i3) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), i2);
        return Math.max(0, (i - view.getMeasuredWidth()) - i3);
    }

    protected int positionChild(View view, int i, int i2, int i3) {
        return positionChild(view, i, i2, i3, true);
    }

    protected int positionChild(View view, int i, int i2, int i3, boolean z) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = i2 + ((i3 - measuredHeight) / 2);
        if (!z) {
            i4 = (this.mTitleMinHeight - measuredHeight) / 2;
        }
        int i5 = i4;
        ViewUtils.layoutChildView(this, view, i, i5, i + measuredWidth, i5 + measuredHeight);
        return measuredWidth;
    }

    protected int positionChildWithOffset(View view, int i, int i2, int i3, boolean z, int i4) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i5 = i2 + ((i3 - measuredHeight) / 2);
        if (!z) {
            i5 = (this.mTitleMinHeight - measuredHeight) / 2;
        }
        int i6 = i5;
        ViewUtils.layoutChildView(this, view, i + i4, i6, i + measuredWidth + i4, i6 + measuredHeight);
        return measuredWidth + i4;
    }

    protected int positionChildInverse(View view, int i, int i2, int i3) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = (this.mTitleMinHeight - measuredHeight) / 2;
        ViewUtils.layoutChildView(this, view, i - measuredWidth, i4, i, i4 + measuredHeight);
        return measuredWidth;
    }

    public boolean isUserSetExpandState() {
        return this.mUserSetExpandState;
    }

    public int getExpandState() {
        return this.mExpandState;
    }

    public void setExpandState(int i) {
        setExpandState(i, false, false);
    }

    public void setExpandState(int i, boolean z, boolean z2) {
        int i2;
        if ((this.mResizable || z2) && (i2 = this.mInnerExpandState) != i) {
            if (z) {
                onAnimatedExpandStateChanged(i2, i);
                return;
            }
            if (i == 3) {
                this.mExpandStateBeforeResizing = this.mExpandState;
            }
            this.mInnerExpandState = i;
            if (i == 0) {
                this.mExpandState = 0;
            } else if (i == 1) {
                this.mExpandState = 1;
            } else if (i == 2) {
                this.mExpandState = 2;
            }
            onExpandStateChanged(i2, i);
            this.mExpandStateOnLayout = this.mExpandState;
            requestLayout();
        }
    }

    public void setResizable(boolean z) {
        this.mResizable = z;
    }

    public boolean isResizable() {
        return this.mResizable;
    }

    public void setBottomMenuMode(int i) {
        this.mBottomMenuMode = i;
    }

    public void setPendingInset(Rect rect) {
        Rect rect2;
        if (rect == null) {
            return;
        }
        boolean z = this.mMenuView != null && ((rect2 = this.mPendingInset) == null || rect2.bottom != rect.bottom);
        if (this.mPendingInset == null) {
            this.mPendingInset = new Rect();
        }
        this.mPendingInset.set(rect);
        if (z) {
            refreshBottomMenu();
        }
    }

    public class CollapseView {
        private float mAlpha;
        private boolean mDetached;
        private List mViews = new ArrayList();
        private boolean mIsAcceptAlphaChange = true;

        protected CollapseView() {
        }

        public void attachViews(View view) {
            if (this.mViews.contains(view)) {
                return;
            }
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: miuix.appcompat.internal.app.widget.AbsActionBarView.CollapseView.1
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view2) {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view2) {
                    Folme.clean(view2);
                }
            });
            this.mViews.add(view);
        }

        public void detachView(View view) {
            if (view == null || !this.mViews.contains(view)) {
                return;
            }
            this.mViews.remove(view);
        }

        public void setAlpha(float f) {
            if (this.mDetached) {
                return;
            }
            this.mAlpha = f;
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                Folme.useAt((View) it.next()).state().setTo(ViewProperty.ALPHA, Float.valueOf(f));
            }
        }

        public void setAcceptAlphaChange(boolean z) {
            this.mIsAcceptAlphaChange = z;
        }

        public void setVisibility(int i) {
            for (View view : this.mViews) {
                view.setVisibility(i);
                if (i != 0) {
                    view.clearFocus();
                }
            }
        }

        public void setAnimFrom(float f, int i, int i2) {
            setAnimFrom(f, i, i2, false);
        }

        public void setAnimFrom(float f, int i, int i2, boolean z) {
            if (this.mDetached) {
                return;
            }
            AnimState animStateAdd = new AnimState("from").add(ViewProperty.ALPHA, this.mIsAcceptAlphaChange ? f : this.mAlpha).add(ViewProperty.TRANSLATION_X, i).add(ViewProperty.TRANSLATION_Y, i2).add(AbsActionBarView.SELF_BLUR, (1.0f - (this.mIsAcceptAlphaChange ? f : this.mAlpha)) * 25.0f);
            for (View view : this.mViews) {
                if (z) {
                    view.setAlpha(f);
                    view.setTranslationX(i);
                    view.setTranslationY(i2);
                }
                if (view.isAttachedToWindow()) {
                    Folme.useAt(view).state().setTo(animStateAdd);
                }
            }
        }

        public void animTo(float f, int i, int i2, AnimConfig animConfig) {
            if (this.mDetached) {
                return;
            }
            if (!this.mIsAcceptAlphaChange) {
                f = this.mAlpha;
            }
            AnimState animStateAdd = new AnimState("to").add(ViewProperty.ALPHA, f).add(ViewProperty.TRANSLATION_X, i).add(ViewProperty.TRANSLATION_Y, i2).add(AbsActionBarView.SELF_BLUR, (1.0f - f) * 25.0f);
            for (View view : this.mViews) {
                if (view.isAttachedToWindow() && (view.getAlpha() != f || view.getTranslationX() != i || view.getTranslationY() != i2)) {
                    Folme.useAt(view).state().to(animStateAdd, animConfig);
                }
            }
        }

        public void onShow() {
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                ((View) it.next()).setEnabled(true);
            }
        }

        public void onHide() {
            for (View view : this.mViews) {
                view.clearFocus();
                view.setEnabled(false);
                view.setVisibility(4);
            }
        }

        public void onDetachedFromWindow() {
            this.mDetached = true;
            Iterator it = this.mViews.iterator();
            while (it.hasNext()) {
                Folme.clean((View) it.next());
            }
        }

        public void onAttachedToWindow() {
            this.mDetached = false;
        }
    }

    protected void setActionMenuItemLimit(int i) {
        this.mMaxActionMenuItemCount = i;
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter == null || (actionMenuPresenter instanceof EndActionMenuPresenter)) {
            return;
        }
        actionMenuPresenter.setItemLimit(i);
    }
}
