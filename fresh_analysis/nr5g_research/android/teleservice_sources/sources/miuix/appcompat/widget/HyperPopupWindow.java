package miuix.appcompat.widget;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Collection;
import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.FolmeObject;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$string;
import miuix.appcompat.view.menu.HyperBaseAdapter;
import miuix.appcompat.view.menu.HyperMenuAdapter;
import miuix.appcompat.view.menu.HyperMenuContract$HyperMenuItem;
import miuix.appcompat.view.menu.HyperMenuContract$HyperMenuTextItem;
import miuix.appcompat.view.menu.HyperSecondaryAdapter;
import miuix.core.util.HyperBloomStrokeUtils;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MaterialDayNightConfig;
import miuix.core.util.MiShadowUtils;
import miuix.core.util.MiuiBlurUtils;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.ViewUtils;
import miuix.popupwidget.internal.strategy.IPopupWindowStrategy;
import miuix.popupwidget.internal.strategy.PopupWindowSpec;
import miuix.popupwidget.internal.strategy.PopupWindowStrategy;
import miuix.popupwidget.internal.util.SinglePopControl;
import miuix.popupwidget.widget.PopupAnimHelper;
import miuix.popupwidget.widget.PopupAnimationListener;
import miuix.popupwidget.widget.PopupWindow;
import miuix.smooth.SmoothContainerDrawable2;
import miuix.smooth.SmoothFrameLayout2;
import miuix.theme.token.ShadowToken;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;
import miuix.view.ITransformableView;

/* JADX INFO: loaded from: classes2.dex */
public class HyperPopupWindow extends PopupWindow implements ITransformableView {
    private int mAnimationExtensionMargin;
    private FrameLayout mClipView;
    private int mCollapseEndOffset;
    private ViewGroup mContainer;
    private final float mCornerRadius;
    private boolean mEnableSecondaryMenu;
    private int mExpandAnchorSubtitleHeight;
    private int mExpandMainTop;
    private View mFocusedMainPopupItemView;
    private int mGravity;
    private ClipLayout mInnerClip;
    private final PopupContentHolder mMainPopContentHolder;
    protected IPopupWindowStrategy mMainPopupStrategy;
    private int mMenuBlurOptionType;
    private OnMenuItemClickListener mMenuItemClickListener;
    private Rect mRootBounds;
    private PopupContentHolder mSecondaryContentHolder;
    private IPopupWindowStrategy mSecondaryPopupStrategy;
    private SubtitleAnimHelper mSubtitleAnimHelper;
    private final int mVerticalPadding;

    public interface OnMenuItemClickListener {
        void onMenuItemClick(MenuItem menuItem);
    }

    @Override // miuix.view.ITransformableView
    public void applyTransformElevation(View view) {
    }

    public HyperPopupWindow(Context context) {
        this(context, null);
    }

    public HyperPopupWindow(Context context, View view) {
        super(context, view);
        this.mEnableSecondaryMenu = true;
        this.mAnimationExtensionMargin = 35;
        this.mGravity = 0;
        this.mMenuBlurOptionType = 0;
        this.mCollapseEndOffset = 0;
        this.mExpandAnchorSubtitleHeight = 0;
        setAutoDismiss(true);
        PopupWindowStrategy popupWindowStrategy = new PopupWindowStrategy();
        this.mMainPopupStrategy = popupWindowStrategy;
        this.mMainPopContentHolder = new PopupContentHolder(this.mContext, popupWindowStrategy);
        this.mCornerRadius = this.mContext.getResources().getDimension(R$dimen.miuix_appcompat_drop_down_menu_radius);
        this.mVerticalPadding = this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_popup_window_vertical_padding);
    }

    @Override // miuix.view.ITransformableView
    public View getContainerView() {
        return this.mMainPopContentHolder.mContentView;
    }

    @Override // android.widget.PopupWindow, miuix.view.ITransformableView
    public View getContentView() {
        return this.mMainPopContentHolder.mContentView.getChildAt(0);
    }

    @Override // miuix.view.ITransformableView
    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    @Override // miuix.view.ITransformableView
    public void setCornerRadius(float f) {
        this.mMainPopContentHolder.mContentView.setCornerRadius(f);
    }

    @Override // miuix.view.ITransformableView
    public View getElevationView() {
        return this.mMainPopContentHolder.mContentView;
    }

    @Override // miuix.popupwidget.widget.PopupWindow
    public void setAdapter(ListAdapter listAdapter) {
        this.mMainPopContentHolder.setAdapter(listAdapter);
    }

    @Override // miuix.popupwidget.widget.PopupWindow
    protected void prepareContentView() {
        FrameLayout frameLayout = this.mRootView;
        int i = this.mAnimationExtensionMargin;
        frameLayout.setPadding(i, i, i, i);
        super.prepareContentView();
    }

    @Override // android.widget.PopupWindow
    public void setClippingEnabled(boolean z) {
        FrameLayout frameLayout = this.mRootView;
        if (frameLayout != null) {
            if (z) {
                this.mAnimationExtensionMargin = 0;
            } else {
                this.mAnimationExtensionMargin = 35;
            }
            int i = this.mAnimationExtensionMargin;
            frameLayout.setPadding(i, i, i, i);
        }
        super.setClippingEnabled(z);
    }

    @Override // miuix.popupwidget.widget.PopupWindow
    public void show(View view) {
        setAnchorView(view);
        updatePopupWindowSpec(this.mPopupWindowSpec);
        this.mRootBounds = getRootBounds();
        if (this.mContainer == null) {
            this.mContainer = new FrameLayout(this.mContext);
            this.mContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            superSetContentViewWithoutClip(this.mContainer);
            this.mContainer.setLayoutDirection(0);
            this.mContainer.setClipChildren(false);
            this.mContainer.setClipToPadding(false);
            ((ViewGroup) this.mContainer.getParent()).setClipChildren(false);
            ((ViewGroup) this.mContainer.getParent()).setClipToPadding(false);
        }
        this.mMainPopContentHolder.mPopupWindowSpec = this.mPopupWindowSpec;
        this.mMainPopContentHolder.inflate();
        this.mMainPopContentHolder.setMenuListAccessibilityDelegate();
        this.mMainPopContentHolder.show(view, this.mContainer, this.mRootBounds, false);
        this.mMainPopContentHolder.setItemClickListener(new AnonymousClass1());
        if (this.mEnableSecondaryMenu) {
            int iWidth = this.mRootBounds.width();
            int iHeight = this.mRootBounds.height();
            setWidth(iWidth + (this.mAnimationExtensionMargin * 2));
            setHeight(iHeight + (this.mAnimationExtensionMargin * 2) + 80);
            Rect rect = this.mRootBounds;
            int i = rect.left;
            int i2 = this.mAnimationExtensionMargin;
            showAtLocation(view, 0, i - i2, rect.top - i2, this.mMainPopContentHolder.mBoundsRect);
            return;
        }
        Rect rect2 = this.mMainPopContentHolder.mBoundsRect;
        int iWidth2 = rect2.width();
        int iHeight2 = rect2.height();
        setWidth(iWidth2 + (this.mAnimationExtensionMargin * 2));
        setHeight(iHeight2 + (this.mAnimationExtensionMargin * 2) + 80);
        int i3 = rect2.left;
        int i4 = this.mAnimationExtensionMargin;
        showAtLocation(view, 0, i3 - i4, rect2.top - i4, rect2);
    }

    /* JADX INFO: renamed from: miuix.appcompat.widget.HyperPopupWindow$1, reason: invalid class name */
    class AnonymousClass1 implements AdapterView.OnItemClickListener {
        AnonymousClass1() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView adapterView, View view, int i, long j) {
            BaseAdapter secondaryAdapterByItemId;
            if (HyperPopupWindow.this.mMainPopContentHolder.mAdapter instanceof HyperMenuAdapter) {
                secondaryAdapterByItemId = ((HyperMenuAdapter) HyperPopupWindow.this.mMainPopContentHolder.mAdapter).getSecondaryAdapterByItemId(j);
                HyperPopupWindow hyperPopupWindow = HyperPopupWindow.this;
                HyperMenuContract$HyperMenuTextItem textItem = hyperPopupWindow.getTextItem(hyperPopupWindow.mMainPopContentHolder.mAdapter, i);
                if (textItem != null && !textItem.isExpandable) {
                    ((HyperMenuAdapter) HyperPopupWindow.this.mMainPopContentHolder.mAdapter).resumePrimaryItemClickStatus((int) j, i);
                }
            } else {
                secondaryAdapterByItemId = null;
            }
            if (HyperPopupWindow.this.mSecondaryContentHolder == null) {
                if (secondaryAdapterByItemId != null) {
                    expandAndHandleSecondaryItemClick(secondaryAdapterByItemId, view);
                } else {
                    handlePrimaryItemClick(i);
                }
                HyperPopupWindow.this.mContainer.findViewById(R$id.mask).setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.widget.HyperPopupWindow$1$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        HyperPopupWindow.this.collapseSecondaryMenu();
                    }
                });
            }
        }

        private void expandAndHandleSecondaryItemClick(final ListAdapter listAdapter, View view) {
            HyperPopupWindow.this.mSecondaryPopupStrategy = new SecondaryPopupWindowStrategy();
            HyperPopupWindow.this.expandSecondaryMenu(view, listAdapter);
            HyperPopupWindow.this.mSecondaryContentHolder.setItemClickListener(new AdapterView.OnItemClickListener() { // from class: miuix.appcompat.widget.HyperPopupWindow$1$$ExternalSyntheticLambda1
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view2, int i, long j) {
                    HyperPopupWindow.AnonymousClass1.$r8$lambda$ssu0yP6O1iIB_5mAnlvyw6pPoNM(this.f$0, listAdapter, adapterView, view2, i, j);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$ssu0yP6O1iIB_5mAnlvyw6pPoNM(AnonymousClass1 anonymousClass1, ListAdapter listAdapter, AdapterView adapterView, View view, int i, long j) {
            HyperMenuContract$HyperMenuTextItem textItem;
            HyperMenuContract$HyperMenuTextItem textItem2;
            anonymousClass1.getClass();
            if (view.getId() == R$id.tag_secondary_popup_menu_item_head) {
                HyperPopupWindow.this.collapseSecondaryMenu();
                return;
            }
            if ((listAdapter instanceof HyperSecondaryAdapter) && (textItem2 = HyperPopupWindow.this.getTextItem(listAdapter, i)) != null && !textItem2.isHeaderItem) {
                ((HyperSecondaryAdapter) listAdapter).resumeSecondaryItemClickStatus((int) j);
            }
            MenuItem menuItem = (MenuItem) listAdapter.getItem(i);
            if (HyperPopupWindow.this.mMenuItemClickListener != null) {
                HyperPopupWindow.this.mMenuItemClickListener.onMenuItemClick(menuItem);
            }
            if ((listAdapter instanceof HyperBaseAdapter) && ((HyperBaseAdapter) listAdapter).isMultiSelectEnabled() && menuItem != null && menuItem.isCheckable() && (textItem = HyperPopupWindow.this.getTextItem(listAdapter, i)) != null && !textItem.isHeaderItem && HyperPopupWindow.this.mSubtitleAnimHelper != null) {
                HyperPopupWindow.this.syncPrimarySubtitleData();
                final int currentSubtitleHeight = HyperPopupWindow.this.mSubtitleAnimHelper.getCurrentSubtitleHeight();
                final int iHeight = HyperPopupWindow.this.mMainPopContentHolder.mBoundsRect.height();
                final boolean zIsPrimaryMenuAtMaxLimit = HyperPopupWindow.this.isPrimaryMenuAtMaxLimit();
                HyperPopupWindow.this.mSubtitleAnimHelper.setIHeightAnimationListener(new PopupHeightAnimator.IHeightAnimationListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.1.1
                    private boolean mStarted = false;

                    @Override // miuix.appcompat.widget.PopupHeightAnimator.IHeightAnimationListener
                    public void onAnimationHeightUpdate(int i2) {
                        if (!this.mStarted) {
                            this.mStarted = true;
                            if (HyperPopupWindow.this.mMainPopContentHolder.mAdapter instanceof HyperBaseAdapter) {
                                ((HyperBaseAdapter) HyperPopupWindow.this.mMainPopContentHolder.mAdapter).setHeightAnimating(true);
                            }
                        }
                        HyperPopupWindow.this.syncPrimarySubtitleToAnchorView(i2);
                        HyperPopupWindow.this.updatePrimaryMenuHeightByDelta(i2 - currentSubtitleHeight, iHeight, zIsPrimaryMenuAtMaxLimit);
                    }

                    @Override // miuix.appcompat.widget.PopupHeightAnimator.IHeightAnimationListener
                    public void onAnimationEnd() {
                        if (HyperPopupWindow.this.mMainPopContentHolder.mAdapter instanceof HyperBaseAdapter) {
                            ((HyperBaseAdapter) HyperPopupWindow.this.mMainPopContentHolder.mAdapter).setHeightAnimating(false);
                        }
                    }
                });
                HyperPopupWindow.this.mSubtitleAnimHelper.onHeightChanged(currentSubtitleHeight);
                return;
            }
            if (menuItem == null || !menuItem.isEnabled() || !HyperPopupWindow.this.isShowing() || ((PopupWindow) HyperPopupWindow.this).mIsTransitioningToDismiss) {
                return;
            }
            HyperPopupWindow.this.dismiss();
        }

        private void handlePrimaryItemClick(int i) {
            MenuItem menuItem = HyperPopupWindow.this.mMainPopContentHolder.mAdapter != null ? (MenuItem) HyperPopupWindow.this.mMainPopContentHolder.mAdapter.getItem(i) : null;
            if (HyperPopupWindow.this.mMenuItemClickListener != null) {
                HyperPopupWindow.this.mMenuItemClickListener.onMenuItemClick(menuItem);
            }
            HyperPopupWindow hyperPopupWindow = HyperPopupWindow.this;
            HyperMenuContract$HyperMenuTextItem textItem = hyperPopupWindow.getTextItem(hyperPopupWindow.mMainPopContentHolder.mAdapter, i);
            if ((textItem == null || !textItem.isMultiSelect || textItem.isExpandable) && menuItem != null && menuItem.isEnabled() && HyperPopupWindow.this.isShowing() && !((PopupWindow) HyperPopupWindow.this).mIsTransitioningToDismiss) {
                HyperPopupWindow.this.dismiss();
            }
        }
    }

    @Override // android.widget.PopupWindow
    public void update() {
        this.mMainPopContentHolder.update();
    }

    @Override // miuix.popupwidget.widget.PopupWindow
    protected void updateLocation(View view) {
        this.mMainPopContentHolder.update();
    }

    @Override // miuix.popupwidget.widget.PopupWindow
    public void setPopupWindowStrategy(IPopupWindowStrategy iPopupWindowStrategy) {
        this.mMainPopupStrategy = iPopupWindowStrategy;
        this.mMainPopContentHolder.mPopupWindowStrategy = iPopupWindowStrategy;
        updateMinSafeInset();
    }

    public void setSecondaryMenuEnabled(boolean z) {
        this.mEnableSecondaryMenu = z;
    }

    public void setMenuBlurOptionType(int i) {
        this.mMenuBlurOptionType = i;
    }

    private void showAtLocation(View view, int i, int i2, int i3, Rect rect) {
        prepareWindowElevation();
        Rect rect2 = new Rect();
        view.getGlobalVisibleRect(rect2);
        int width = getWidth();
        int height = getHeight();
        if (rect == null) {
            rect = new Rect();
            rect.set(i2, i3, width + i2, height + i3);
        }
        int iComputeGravity = computeGravity(rect2, rect, 0, view.getLayoutDirection());
        this.mGravity = iComputeGravity;
        showWithAnim(iComputeGravity);
        if (!isShowing()) {
            HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_BUTTON_SMALL, HapticFeedbackConstants.MIUI_POPUP_NORMAL);
        }
        superShowAtLocation(view, i, i2, i3);
        this.mRootView.setElevation(0.0f);
        SinglePopControl.showPop(this.mContext, this);
    }

    @Override // miuix.popupwidget.widget.PopupWindow
    protected void showWithAnim(int i) {
        View anchor = getAnchor();
        if (anchor == null) {
            return;
        }
        if (!this.mWindowAnimationEnabled && this.mAnimHelper == null) {
            PopupAnimHelper popupAnimHelper = new PopupAnimHelper((View) this.mContentView.findViewById(R$id.miuix_popupwidget_popup_anim_container).getParent(), anchor, this);
            this.mAnimHelper = popupAnimHelper;
            popupAnimHelper.setCrossWindow(true);
            this.mAnimHelper.addPopupAnimationListener(new PopupAnimationListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.2
                @Override // miuix.popupwidget.widget.PopupAnimationListener, miuix.popupwidget.widget.IPopupAnimationListener
                public void onForwardAnimationEnd() {
                    ((PopupWindow) HyperPopupWindow.this).mIsTransitioningToShown = false;
                }
            });
        }
        enableItemClick();
        super.showWithAnim(i);
    }

    private void prepareWindowElevation() {
        if (shouldSetElevation()) {
            setElevation(this.mElevation);
        }
        prepareWindowElevation(this.mMainPopContentHolder.mContentView, this.mElevation + this.mElevationExtra);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuItemClickListener = onMenuItemClickListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HyperMenuContract$HyperMenuTextItem getTextItem(Object obj, int i) {
        HyperMenuContract$HyperMenuItem hyperMenuItem = obj instanceof HyperBaseAdapter ? ((HyperBaseAdapter) obj).getHyperMenuItem(i) : null;
        if (hyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) {
            return (HyperMenuContract$HyperMenuTextItem) hyperMenuItem;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void expandSecondaryMenu(View view, ListAdapter listAdapter) {
        toBackAnim();
        doExpandAnimation(view, listAdapter);
        disableMainMenuAccessibility(this.mMainPopContentHolder.mContentView);
        announceForSecondaryMenu(this.mContext.getResources().getString(R$string.miuix_appcompat_accessibility_expand_state));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void collapseSecondaryMenu() {
        if (this.mSecondaryContentHolder == null) {
            return;
        }
        this.mCollapseEndOffset = 0;
        syncMultiSelectSubtitlesBeforeCollapse();
        toFrontAnim();
        doCollapseAnimation();
        this.mSecondaryContentHolder = null;
        SubtitleAnimHelper subtitleAnimHelper = this.mSubtitleAnimHelper;
        if (subtitleAnimHelper != null) {
            subtitleAnimHelper.release();
            this.mSubtitleAnimHelper = null;
        }
        enableMainMenuAccessibility(this.mMainPopContentHolder.mContentView);
        announceForSecondaryMenu(this.mContext.getResources().getString(R$string.miuix_appcompat_accessibility_collapse_state));
    }

    private void syncMultiSelectSubtitlesBeforeCollapse() {
        if (this.mSecondaryContentHolder.mAdapter instanceof HyperBaseAdapter) {
            HyperBaseAdapter hyperBaseAdapter = (HyperBaseAdapter) this.mSecondaryContentHolder.mAdapter;
            if (hyperBaseAdapter.isMultiSelectEnabled() && hyperBaseAdapter.getCount() != 0) {
                HyperMenuContract$HyperMenuItem hyperMenuItem = hyperBaseAdapter.getHyperMenuItem(0);
                if (!(hyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) || ((HyperMenuContract$HyperMenuTextItem) hyperMenuItem).showSubtitle) {
                    int i = this.mExpandAnchorSubtitleHeight;
                    int i2 = this.mMainPopContentHolder.mBoundsRect.top;
                    syncPrimarySubtitles();
                    int i3 = this.mMainPopContentHolder.mBoundsRect.top;
                    int iCalculateCollapseEndOffset = calculateCollapseEndOffset(i);
                    this.mCollapseEndOffset = iCalculateCollapseEndOffset;
                    int i4 = i3 - i2;
                    if (i4 != 0) {
                        this.mCollapseEndOffset = iCalculateCollapseEndOffset + i4;
                        if (this.mClipView == null || this.mSecondaryContentHolder == null) {
                            return;
                        }
                        Rect unionBounds = getUnionBounds(this.mMainPopContentHolder.mBoundsRect, this.mSecondaryContentHolder.mBoundsRect);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mClipView.getLayoutParams();
                        layoutParams.width = unionBounds.width();
                        layoutParams.height = unionBounds.height();
                        int i5 = unionBounds.left;
                        Rect rect = this.mRootBounds;
                        layoutParams.leftMargin = i5 - rect.left;
                        layoutParams.topMargin = unionBounds.top - rect.top;
                        this.mClipView.setLayoutParams(layoutParams);
                        ClipLayout clipLayout = this.mInnerClip;
                        if (clipLayout != null) {
                            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) clipLayout.getLayoutParams();
                            layoutParams2.width = unionBounds.width();
                            layoutParams2.height = unionBounds.height();
                            this.mInnerClip.setLayoutParams(layoutParams2);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncPrimarySubtitleData() {
        int positionForView;
        if (this.mSecondaryContentHolder == null || this.mMainPopContentHolder.mAdapter == null || !(this.mMainPopContentHolder.mAdapter instanceof HyperMenuAdapter)) {
            return;
        }
        HyperMenuAdapter hyperMenuAdapter = (HyperMenuAdapter) this.mMainPopContentHolder.mAdapter;
        View view = this.mSecondaryContentHolder.mSecondaryMenuAnchorView;
        if (view == null) {
            return;
        }
        for (int i = 0; i < hyperMenuAdapter.getCount(); i++) {
            HyperMenuContract$HyperMenuItem hyperMenuItem = hyperMenuAdapter.getHyperMenuItem(i);
            if ((hyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) && hyperMenuItem.getMenuItem() != null) {
                HyperMenuContract$HyperMenuTextItem hyperMenuContract$HyperMenuTextItem = (HyperMenuContract$HyperMenuTextItem) hyperMenuItem;
                if (hyperMenuContract$HyperMenuTextItem.isExpandable && hyperMenuContract$HyperMenuTextItem.showSubtitle) {
                    hyperMenuContract$HyperMenuTextItem.setSubtitle(hyperMenuItem.getMenuItem().getSubtitle());
                }
            }
        }
        View viewFindViewById = view.findViewById(R.id.text2);
        if (!(viewFindViewById instanceof TextView) || (positionForView = this.mMainPopContentHolder.mListView.getPositionForView(view) - this.mMainPopContentHolder.mListView.getHeaderViewsCount()) < 0 || positionForView >= hyperMenuAdapter.getCount()) {
            return;
        }
        HyperMenuContract$HyperMenuItem hyperMenuItem2 = hyperMenuAdapter.getHyperMenuItem(positionForView);
        if (hyperMenuItem2 instanceof HyperMenuContract$HyperMenuTextItem) {
            CharSequence subtitle = ((HyperMenuContract$HyperMenuTextItem) hyperMenuItem2).getSubtitle();
            if (!TextUtils.isEmpty(subtitle)) {
                ((TextView) viewFindViewById).setText(subtitle);
            } else {
                ((TextView) viewFindViewById).setText("");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncPrimarySubtitleToAnchorView(int i) {
        View view;
        View viewFindViewById;
        PopupContentHolder popupContentHolder = this.mSecondaryContentHolder;
        if (popupContentHolder == null || (view = popupContentHolder.mSecondaryMenuAnchorView) == null || (viewFindViewById = view.findViewById(R.id.text2)) == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = viewFindViewById.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = i;
            viewFindViewById.setLayoutParams(layoutParams);
        }
        if (i > 0 && viewFindViewById.getVisibility() != 0) {
            viewFindViewById.setVisibility(0);
        } else {
            if (i > 0 || viewFindViewById.getVisibility() == 8) {
                return;
            }
            viewFindViewById.setVisibility(8);
        }
    }

    private void syncPrimarySubtitles() {
        if (this.mMainPopContentHolder.mAdapter instanceof HyperMenuAdapter) {
            ((HyperMenuAdapter) this.mMainPopContentHolder.mAdapter).notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPrimaryMenuAtMaxLimit() {
        int iHeight = this.mMainPopContentHolder.mBoundsRect.height();
        PopupWindowSpec popupWindowSpec = this.mMainPopContentHolder.mPopupWindowSpec;
        if (popupWindowSpec == null) {
            return false;
        }
        popupWindowSpec.mItemViewBounds = getItemViewBounds(this.mMainPopContentHolder.mAdapter, this.mMainPopContentHolder.mListView, this.mContext, popupWindowSpec.mMaxWidth, this.mMainPopContentHolder.mBoundsRect.width());
        IPopupWindowStrategy iPopupWindowStrategy = this.mMainPopupStrategy;
        if (iPopupWindowStrategy != null) {
            iPopupWindowStrategy.measureContentSize(popupWindowSpec);
        }
        return popupWindowSpec.mFinalPopupHeight >= iHeight;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePrimaryMenuHeightByDelta(int i, int i2, boolean z) {
        int i3 = i2 + i;
        if (i3 < 0) {
            i3 = 0;
        }
        if (i >= 0 || !z) {
            PopupWindowSpec popupWindowSpec = this.mMainPopContentHolder.mPopupWindowSpec;
            Rect rect = popupWindowSpec != null ? popupWindowSpec.mSafeInsets : null;
            Rect rect2 = popupWindowSpec != null ? popupWindowSpec.mDecorViewBounds : null;
            int i4 = this.mGravity & 112;
            PopupContentHolder popupContentHolder = this.mMainPopContentHolder;
            if (i4 == 80) {
                int i5 = popupContentHolder.mBoundsRect.bottom;
                int i6 = i5 - i3;
                if (rect2 != null) {
                    int i7 = rect2.top + (rect != null ? rect.top : 0);
                    if (i6 < i7) {
                        i3 = i5 - i7;
                        i6 = i7;
                    }
                }
                this.mMainPopContentHolder.mBoundsRect.top = i6;
                ViewGroup.LayoutParams layoutParams = this.mMainPopContentHolder.mContentView.getLayoutParams();
                if (layoutParams instanceof FrameLayout.LayoutParams) {
                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
                    layoutParams2.height = i3;
                    layoutParams2.topMargin = i6 - this.mRootBounds.top;
                    this.mMainPopContentHolder.mContentView.setLayoutParams(layoutParams2);
                    this.mMainPopContentHolder.mContentView.setPivotY(i3);
                    return;
                }
                return;
            }
            int i8 = popupContentHolder.mBoundsRect.top;
            if (rect2 != null) {
                int i9 = (rect2.bottom - (rect != null ? rect.bottom : 0)) - i8;
                if (i9 > 0 && i3 > i9) {
                    i3 = i9;
                }
            }
            this.mMainPopContentHolder.mBoundsRect.bottom = i8 + i3;
            ViewGroup.LayoutParams layoutParams3 = this.mMainPopContentHolder.mContentView.getLayoutParams();
            if (layoutParams3 != null) {
                layoutParams3.height = i3;
                this.mMainPopContentHolder.mContentView.setLayoutParams(layoutParams3);
            }
        }
    }

    private int calculateCollapseEndOffset(int i) {
        PopupContentHolder popupContentHolder;
        View childAt;
        int i2 = this.mMainPopContentHolder.mBoundsRect.top - this.mExpandMainTop;
        if (i > 0 && (popupContentHolder = this.mSecondaryContentHolder) != null && (popupContentHolder.mAdapter instanceof HyperBaseAdapter) && this.mSecondaryContentHolder.mAdapter.getCount() != 0) {
            HyperMenuContract$HyperMenuItem hyperMenuItem = ((HyperBaseAdapter) this.mSecondaryContentHolder.mAdapter).getHyperMenuItem(0);
            if ((hyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) && TextUtils.isEmpty(((HyperMenuContract$HyperMenuTextItem) hyperMenuItem).getSubtitle())) {
                this.mSecondaryContentHolder.mPopupWindowSpec.mAnchorViewBounds.bottom -= i;
                ListView listView = this.mMainPopContentHolder.mListView;
                if (listView != null) {
                    int height = 0;
                    for (int i3 = 0; i3 < listView.getChildCount(); i3++) {
                        height += listView.getChildAt(i3).getHeight();
                    }
                    if (height > (listView.getHeight() - listView.getPaddingTop()) - listView.getPaddingBottom()) {
                        boolean z = listView.getLastVisiblePosition() >= (listView.getAdapter() != null ? listView.getAdapter().getCount() - 1 : 0);
                        if (z && (childAt = listView.getChildAt(listView.getChildCount() - 1)) != null) {
                            z = childAt.getBottom() <= listView.getHeight() - listView.getPaddingBottom();
                        }
                        if (!z) {
                            i = 0;
                        }
                        return i + i2;
                    }
                }
            }
        }
        return i2;
    }

    private void enableMainMenuAccessibility(View view) {
        if (view != null) {
            view.setImportantForAccessibility(1);
        }
        View view2 = this.mFocusedMainPopupItemView;
        if (view2 != null) {
            view2.post(new Runnable() { // from class: miuix.appcompat.widget.HyperPopupWindow$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.mFocusedMainPopupItemView.sendAccessibilityEvent(8);
                }
            });
        }
    }

    private void disableMainMenuAccessibility(View view) {
        if (view != null) {
            view.setImportantForAccessibility(4);
        }
    }

    private void announceForSecondaryMenu(String str) {
        if (this.mMainPopContentHolder.mContentView != null) {
            this.mMainPopContentHolder.mContentView.announceForAccessibility(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[][] getItemViewBounds(ListAdapter listAdapter, ViewGroup viewGroup, Context context, int i, int i2) {
        View viewFindViewById;
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE);
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        int count = listAdapter.getCount();
        int[][] iArr = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, count, 2);
        ViewGroup frameLayout = viewGroup;
        View view = null;
        for (int i3 = 0; i3 < count; i3++) {
            if (frameLayout == null) {
                frameLayout = new FrameLayout(context);
            }
            view = listAdapter.getView(i3, view, frameLayout);
            char c = '\b';
            if ((listAdapter instanceof HyperBaseAdapter) && listAdapter.getItemViewType(i3) == 0) {
                viewFindViewById = view.findViewById(R.id.text2);
                if (viewFindViewById != null && viewFindViewById.getVisibility() == 0) {
                    viewFindViewById.setVisibility(8);
                    c = 0;
                }
            } else {
                viewFindViewById = null;
            }
            view.measure(iMakeMeasureSpec, iMakeMeasureSpec2);
            int measuredWidth = view.getMeasuredWidth();
            if (i2 != -1) {
                measuredWidth = Math.max(measuredWidth, i2);
            }
            if (viewFindViewById != null && c == 0) {
                viewFindViewById.setVisibility(0);
                view.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), iMakeMeasureSpec2);
            }
            int[] iArr2 = iArr[i3];
            iArr2[0] = measuredWidth;
            iArr2[1] = view.getMeasuredHeight();
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getRootBounds() {
        Rect rect = new Rect();
        PopupWindowSpec popupWindowSpecClone = this.mPopupWindowSpec.clone();
        Rect rect2 = popupWindowSpecClone.mDecorViewBounds;
        int i = rect2.left;
        Rect rect3 = popupWindowSpecClone.mSafeInsets;
        rect.set(i + rect3.left, rect2.top + rect3.top, rect2.right - rect3.right, rect2.bottom - rect3.bottom);
        return rect;
    }

    @Override // miuix.popupwidget.widget.PopupWindow
    protected void dismissWithAnim() {
        this.mRootView.setClickable(false);
        disableItemClick();
        PopupAnimHelper popupAnimHelper = this.mAnimHelper;
        if (popupAnimHelper != null) {
            this.mIsTransitioningToDismiss = true;
            if (this.mSecondaryContentHolder != null) {
                doCollapse();
                toFrontAnim();
            }
            this.mAnimHelper.addPopupAnimationListener(new PopupAnimationListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.3
                @Override // miuix.popupwidget.widget.PopupAnimationListener, miuix.popupwidget.widget.IPopupAnimationListener
                public void onReverseAnimationEnd() {
                    ((PopupWindow) HyperPopupWindow.this).mAnimHelper.removePopupAnimationListener(this);
                    HyperPopupWindow.this.realDismiss();
                    ((PopupWindow) HyperPopupWindow.this).mIsTransitioningToDismiss = false;
                }
            });
            this.mAnimHelper.dismiss();
            return;
        }
        if (this.mSecondaryContentHolder == null || popupAnimHelper == null) {
            super.dismissWithAnim();
        } else {
            this.mIsTransitioningToDismiss = true;
            dismissWithAnimForSecondaryMenu();
        }
    }

    private void disableItemClick() {
        if (this.mMainPopContentHolder.mListView != null) {
            this.mMainPopContentHolder.mListView.setEnabled(false);
        }
        PopupContentHolder popupContentHolder = this.mSecondaryContentHolder;
        if (popupContentHolder == null || popupContentHolder.mListView == null) {
            return;
        }
        this.mSecondaryContentHolder.mListView.setEnabled(false);
    }

    private void enableItemClick() {
        if (this.mMainPopContentHolder.mListView != null) {
            this.mMainPopContentHolder.mListView.setEnabled(true);
        }
        PopupContentHolder popupContentHolder = this.mSecondaryContentHolder;
        if (popupContentHolder == null || popupContentHolder.mListView == null) {
            return;
        }
        this.mSecondaryContentHolder.mListView.setEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.popupwidget.widget.PopupWindow
    public void realDismiss() {
        PopupAnimHelper popupAnimHelper = this.mAnimHelper;
        if (popupAnimHelper != null) {
            popupAnimHelper.resetFromContentView();
        }
        SubtitleAnimHelper subtitleAnimHelper = this.mSubtitleAnimHelper;
        if (subtitleAnimHelper != null) {
            subtitleAnimHelper.release();
            this.mSubtitleAnimHelper = null;
        }
        super.realDismiss();
    }

    private void dismissWithAnimForSecondaryMenu() {
        Float fValueOf = Float.valueOf(0.5f);
        this.mCollapseEndOffset = 0;
        Rect unionBounds = getUnionBounds(this.mMainPopContentHolder.mBoundsRect, this.mSecondaryContentHolder.mBoundsRect);
        int left = this.mClipView.getLeft();
        int top = this.mClipView.getTop();
        this.mRootView.setLayoutDirection(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mContainer.getLayoutParams();
        layoutParams.width = unionBounds.width();
        layoutParams.height = unionBounds.height();
        layoutParams.leftMargin += left;
        layoutParams.topMargin += top;
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mMainPopContentHolder.mContentView.getLayoutParams();
        layoutParams2.leftMargin -= left;
        layoutParams2.topMargin -= top;
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.mClipView.getLayoutParams();
        layoutParams3.leftMargin -= left;
        layoutParams3.topMargin -= top;
        this.mContainer.requestLayout();
        doCollapseAnimation();
        AnimConfig animConfigAddListeners = new AnimConfig().setEase(FolmeEase.linear(150L)).addListeners(new TransitionListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.4
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                HyperPopupWindow.this.realDismiss();
                ((PopupWindow) HyperPopupWindow.this).mIsTransitioningToDismiss = false;
            }
        });
        int animGravity = this.mAnimHelper.getAnimGravity();
        int absoluteGravity = Gravity.getAbsoluteGravity(animGravity, this.mContainer.getLayoutDirection()) & 7;
        int absoluteGravity2 = Gravity.getAbsoluteGravity(animGravity, this.mContainer.getLayoutDirection()) & 112;
        ViewGroup viewGroup = this.mContainer;
        if (absoluteGravity == 3) {
            viewGroup.setPivotX(0.0f);
        } else {
            viewGroup.setPivotX(unionBounds.width());
        }
        ViewGroup viewGroup2 = this.mContainer;
        if (absoluteGravity2 == 48) {
            viewGroup2.setPivotY(0.0f);
        } else {
            viewGroup2.setPivotY(unionBounds.height());
        }
        Folme.use((View) this.mContainer).to(ViewProperty.ALPHA, Float.valueOf(0.0f), animConfigAddListeners);
        Folme.use((View) this.mContainer).to(ViewProperty.SCALE_X, fValueOf, ViewProperty.SCALE_Y, fValueOf);
    }

    protected void doExpandAnimation(View view, ListAdapter listAdapter) {
        this.mExpandMainTop = this.mMainPopContentHolder.mBoundsRect.top;
        this.mExpandAnchorSubtitleHeight = 0;
        View viewFindViewById = view.findViewById(R.id.text2);
        if (viewFindViewById != null && viewFindViewById.getVisibility() == 0) {
            this.mExpandAnchorSubtitleHeight = viewFindViewById.getHeight();
        }
        PopupWindowSpec popupWindowSpecClone = this.mPopupWindowSpec.clone();
        ViewUtils.getBoundsInWindow(view, popupWindowSpecClone.mAnchorViewBounds);
        Rect rect = popupWindowSpecClone.mAnchorViewBounds;
        int i = rect.left;
        Rect rect2 = this.mRootBounds;
        rect.left = i + rect2.left;
        int i2 = rect.right;
        int i3 = rect2.left;
        rect.right = i2 + i3;
        rect.top += rect2.top;
        int i4 = rect.bottom;
        int i5 = rect2.top;
        rect.bottom = i4 + i5;
        popupWindowSpecClone.mDecorViewBounds.set(i3, i5, rect2.right, rect2.bottom);
        PopupContentHolder popupContentHolder = new PopupContentHolder(this.mContext, listAdapter, this.mSecondaryPopupStrategy, popupWindowSpecClone);
        this.mSecondaryContentHolder = popupContentHolder;
        popupContentHolder.inflate();
        this.mSecondaryContentHolder.setMinWidth(this.mMainPopContentHolder.mContentView.getWidth());
        this.mSecondaryContentHolder.show(view, this.mContainer, this.mRootBounds, true);
        if (listAdapter instanceof HyperSecondaryAdapter) {
            SubtitleAnimHelper subtitleAnimHelper = new SubtitleAnimHelper((HyperSecondaryAdapter) listAdapter, this.mSecondaryContentHolder.mListView);
            this.mSubtitleAnimHelper = subtitleAnimHelper;
            subtitleAnimHelper.bindContainerViews(this.mSecondaryContentHolder.mBoundsRect, this.mMainPopContentHolder.mBoundsRect, this.mClipView, this.mInnerClip, this.mSecondaryContentHolder.mContentView);
            PopupWindowSpec popupWindowSpec = this.mPopupWindowSpec;
            Rect rect3 = popupWindowSpec.mSafeInsets;
            Rect rect4 = popupWindowSpec.mDecorViewBounds;
            int i6 = rect4.bottom - (rect3 != null ? rect3.bottom : 0);
            int i7 = rect4.top;
            int i8 = rect3 != null ? rect3.top : 0;
            this.mSubtitleAnimHelper.setSafeBottomY(i6);
            this.mSubtitleAnimHelper.setSafeTopY(i7 + i8);
            this.mSubtitleAnimHelper.setRootBoundsTop(this.mRootBounds.top);
        }
    }

    private void doCollapse() {
        Float fValueOf = Float.valueOf(0.2f);
        SmoothFrameLayout2 smoothFrameLayout2 = this.mSecondaryContentHolder.mContentView;
        int i = this.mGravity;
        int i2 = i & 7;
        int i3 = i & 112;
        if (i2 == 3) {
            smoothFrameLayout2.setPivotX(0.0f);
        } else {
            smoothFrameLayout2.setPivotX(smoothFrameLayout2.getWidth());
        }
        if (i3 == 48) {
            smoothFrameLayout2.setPivotY(0.0f);
        } else {
            smoothFrameLayout2.setPivotY(smoothFrameLayout2.getHeight());
        }
        AnimConfig ease = new AnimConfig().setEase(FolmeEase.spring(0.8f, 0.28f));
        ViewProperty viewProperty = ViewProperty.ALPHA;
        Folme.use((View) smoothFrameLayout2).to(ViewProperty.SCALE_X, fValueOf, ViewProperty.SCALE_Y, fValueOf, viewProperty, Float.valueOf(0.0f), ease.setSpecial(viewProperty, FolmeEase.linear(80L), new float[0]));
        ClipLayout clipLayout = this.mInnerClip;
        if (clipLayout != null) {
            clipLayout.clearElevation();
        }
    }

    protected void doCollapseAnimation() {
        PopupWindowSpec popupWindowSpec = this.mSecondaryContentHolder.mPopupWindowSpec;
        final SmoothFrameLayout2 smoothFrameLayout2 = this.mSecondaryContentHolder.mContentView;
        final ViewBounds viewBounds = this.mSecondaryContentHolder.mViewBounds;
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect unionBounds = getUnionBounds(this.mMainPopContentHolder.mBoundsRect, this.mSecondaryContentHolder.mBoundsRect);
        final int left = smoothFrameLayout2.getLeft();
        final int top = smoothFrameLayout2.getTop();
        final int right = smoothFrameLayout2.getRight();
        final int bottom = smoothFrameLayout2.getBottom();
        int i = rect.left;
        int i2 = unionBounds.left;
        final int i3 = i - i2;
        int i4 = rect.top;
        int i5 = unionBounds.top;
        int i6 = this.mVerticalPadding;
        int i7 = this.mCollapseEndOffset;
        final int i8 = ((i4 - i5) - i6) + i7;
        final int i9 = rect.right - i2;
        final int i10 = (rect.bottom - i5) + i6 + i7;
        this.mSecondaryContentHolder.mListView.setScrollBarStyle(0);
        this.mSecondaryContentHolder.mIsInAnimation = true;
        if (viewBounds.folme() == null) {
            return;
        }
        AnimConfig animConfigAddListeners = new AnimConfig().addListeners(new TransitionListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.5
            private WeakReference headerViewRef;

            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                View viewFindViewById = ((PopupWindow) HyperPopupWindow.this).mContentView.findViewById(R$id.tag_secondary_popup_menu_item_head);
                if (viewFindViewById != null) {
                    this.headerViewRef = new WeakReference(viewFindViewById);
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection collection) {
                super.onUpdate(obj, collection);
                UpdateInfo updateInfoFindByName = UpdateInfo.findByName(collection, "fraction");
                if (updateInfoFindByName != null) {
                    float floatValue = updateInfoFindByName.getFloatValue();
                    if (floatValue >= 1.0f) {
                        viewBounds.folme().end();
                    }
                    int i11 = left;
                    int i12 = (int) (i11 + ((i3 - i11) * floatValue));
                    int i13 = top;
                    int i14 = (int) (i13 + ((i8 - i13) * floatValue));
                    int i15 = right;
                    int i16 = (int) (i15 + ((i9 - i15) * floatValue));
                    int i17 = bottom;
                    HyperPopupWindow.this.mInnerClip.setClipBounds(i12, i14, i16, (int) (i17 + ((i10 - i17) * floatValue)));
                    HyperPopupWindow.this.mInnerClip.refreshClipPath();
                    viewBounds.updateContentLocation(i12, i14);
                    WeakReference weakReference = this.headerViewRef;
                    if (weakReference != null) {
                        viewBounds.updateHeaderViewWidth((View) weakReference.get(), i16 - i12);
                    }
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                HyperPopupWindow.this.mContainer.removeView(smoothFrameLayout2);
                HyperPopupWindow.this.mContainer.removeView(HyperPopupWindow.this.mInnerClip);
                HyperPopupWindow.this.mContainer.removeView(HyperPopupWindow.this.mClipView);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onCancel(Object obj) {
                super.onCancel(obj);
                HyperPopupWindow.this.mContainer.removeView(smoothFrameLayout2);
                HyperPopupWindow.this.mContainer.removeView(HyperPopupWindow.this.mInnerClip);
                HyperPopupWindow.this.mContainer.removeView(HyperPopupWindow.this.mClipView);
            }
        });
        animConfigAddListeners.setEase(FolmeEase.spring(0.95f, 0.2f));
        animConfigAddListeners.setSpecial(ViewBounds.ARROW_ROTATION_PROPERTY, FolmeEase.spring(0.95f, 0.3f), new float[0]);
        viewBounds.folme().resetTo("fraction", Float.valueOf(0.0f));
        viewBounds.folme().to(new AnimState().add("fraction", 1.0f).add(ViewBounds.ARROW_ROTATION_PROPERTY, 0.0d), animConfigAddListeners);
        this.mInnerClip.clearElevation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Rect getUnionBounds(Rect rect, Rect rect2) {
        Rect rect3 = new Rect();
        rect3.left = Math.min(rect.left, rect2.left);
        rect3.top = Math.min(rect.top, rect2.top);
        rect3.right = Math.max(rect.right, rect2.right);
        rect3.bottom = Math.max(rect.bottom, rect2.bottom);
        return rect3;
    }

    public class PopupContentHolder {
        private ListAdapter mAdapter;
        private SmoothFrameLayout2 mContentView;
        private Context mContext;
        private ListView mListView;
        private AdapterView.OnItemClickListener mOnItemClickListener;
        private PopupWindowSpec mPopupWindowSpec;
        private IPopupWindowStrategy mPopupWindowStrategy;
        private View mSecondaryMenuAnchorView;
        private ViewBounds mViewBounds;
        private int mMinWidth = -1;
        private final Rect mBoundsRect = new Rect();
        private boolean mIsInAnimation = false;

        public PopupContentHolder(Context context, IPopupWindowStrategy iPopupWindowStrategy) {
            this.mContext = context;
            this.mPopupWindowStrategy = iPopupWindowStrategy;
        }

        public PopupContentHolder(Context context, ListAdapter listAdapter, IPopupWindowStrategy iPopupWindowStrategy, PopupWindowSpec popupWindowSpec) {
            this.mContext = context;
            this.mAdapter = listAdapter;
            this.mPopupWindowStrategy = iPopupWindowStrategy;
            this.mPopupWindowSpec = popupWindowSpec;
        }

        void setAdapter(ListAdapter listAdapter) {
            this.mAdapter = listAdapter;
        }

        void setItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        void inflate() {
            if (this.mContentView == null) {
                this.mContentView = (SmoothFrameLayout2) LayoutInflater.from(this.mContext).inflate(R$layout.miuix_appcompat_hyper_popup_list, (ViewGroup) null);
                Drawable drawableResolveDrawable = AttributeResolver.resolveDrawable(this.mContext, R$attr.immersionWindowBackground);
                if (drawableResolveDrawable instanceof SmoothContainerDrawable2) {
                    ((SmoothContainerDrawable2) drawableResolveDrawable).setCornerRadius(HyperPopupWindow.this.mCornerRadius);
                }
                if (drawableResolveDrawable != null) {
                    this.mContentView.setBackground(drawableResolveDrawable);
                }
                final View viewFindViewById = this.mContentView.findViewById(R$id.miuix_popupwidget_popup_anim_container);
                this.mContentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.PopupContentHolder.1
                    @Override // android.view.View.OnLayoutChangeListener
                    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                        boolean z = !PopupContentHolder.this.mIsInAnimation && (PopupContentHolder.this.mAdapter != null ? PopupContentHolder.this.mPopupWindowStrategy.isNeedScroll(i4 - i2, PopupContentHolder.this.mPopupWindowSpec) : true);
                        viewFindViewById.setEnabled(z);
                        PopupContentHolder.this.mListView.setVerticalScrollBarEnabled(z);
                    }
                });
                this.mContentView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.PopupContentHolder.2
                    @Override // android.view.View.OnAttachStateChangeListener
                    public void onViewDetachedFromWindow(View view) {
                    }

                    @Override // android.view.View.OnAttachStateChangeListener
                    public void onViewAttachedToWindow(View view) {
                        MiuiBlurUtils.setMiBlurWinType(PopupContentHolder.this.mContentView, 1);
                    }
                });
                HyperPopupWindow.this.prepareMaterial(this.mContentView);
            }
            ListView listView = (ListView) this.mContentView.findViewById(R.id.list);
            this.mListView = listView;
            if (listView != null) {
                listView.setOnTouchListener(new AnonymousClass3());
                this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: miuix.appcompat.widget.HyperPopupWindow$PopupContentHolder$$ExternalSyntheticLambda0
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                        HyperPopupWindow.PopupContentHolder.m5459$r8$lambda$R18aSfmmuo_dHF0gnY7pVHtyq8(this.f$0, adapterView, view, i, j);
                    }
                });
                this.mListView.setAdapter(this.mAdapter);
            }
        }

        /* JADX INFO: renamed from: miuix.appcompat.widget.HyperPopupWindow$PopupContentHolder$3, reason: invalid class name */
        class AnonymousClass3 implements View.OnTouchListener {
            int lastIndex = -1;

            AnonymousClass3() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(final View view, MotionEvent motionEvent) {
                int firstVisiblePosition;
                int i;
                View childAt;
                int iPointToPosition = PopupContentHolder.this.mListView.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
                int action = motionEvent.getAction();
                if (action != 0) {
                    if (action == 1 || action == 3 || action == 6) {
                        this.lastIndex = -1;
                        PopupContentHolder.this.mListView.postDelayed(new Runnable() { // from class: miuix.appcompat.widget.HyperPopupWindow$PopupContentHolder$3$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                HyperPopupWindow.PopupContentHolder.AnonymousClass3.m5460$r8$lambda$iHchZUzWEstqA2Wc7doUvi2NwY(view);
                            }
                        }, ViewConfiguration.getPressedStateDuration());
                    }
                } else if (iPointToPosition != -1 && (firstVisiblePosition = iPointToPosition - PopupContentHolder.this.mListView.getFirstVisiblePosition()) != (i = this.lastIndex)) {
                    if (i != -1 && (childAt = PopupContentHolder.this.mListView.getChildAt(this.lastIndex)) != null) {
                        childAt.setPressed(false);
                    }
                    PopupContentHolder.this.mListView.getChildAt(firstVisiblePosition).setPressed(true);
                    this.lastIndex = firstVisiblePosition;
                }
                return false;
            }

            /* JADX INFO: renamed from: $r8$lambda$iHchZUzWEstqA2W-c7doUvi2NwY, reason: not valid java name */
            public static /* synthetic */ void m5460$r8$lambda$iHchZUzWEstqA2Wc7doUvi2NwY(View view) {
                if (view instanceof ViewGroup) {
                    try {
                        int childCount = ((ViewGroup) view).getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            ((ViewGroup) view).getChildAt(i).setPressed(false);
                        }
                    } catch (Exception e) {
                        Log.e("HyperPopupWindow", "list onTouch error " + e);
                    }
                }
            }
        }

        /* JADX INFO: renamed from: $r8$lambda$R18aSfmmuo-_dHF0gnY7pVHtyq8, reason: not valid java name */
        public static /* synthetic */ void m5459$r8$lambda$R18aSfmmuo_dHF0gnY7pVHtyq8(PopupContentHolder popupContentHolder, AdapterView adapterView, View view, int i, long j) {
            int headerViewsCount = i - popupContentHolder.mListView.getHeaderViewsCount();
            if (popupContentHolder.mOnItemClickListener == null || headerViewsCount < 0 || headerViewsCount >= popupContentHolder.mAdapter.getCount()) {
                return;
            }
            popupContentHolder.mOnItemClickListener.onItemClick(adapterView, view, headerViewsCount, j);
        }

        boolean show(View view, ViewGroup viewGroup, Rect rect, boolean z) {
            this.mSecondaryMenuAnchorView = view;
            PopupWindowSpec popupWindowSpec = this.mPopupWindowSpec;
            Rect rect2 = popupWindowSpec.mAnchorViewBounds;
            if (z) {
                rect2.left -= HyperPopupWindow.this.mAnimationExtensionMargin;
                rect2.top -= HyperPopupWindow.this.mAnimationExtensionMargin;
                rect2.right -= HyperPopupWindow.this.mAnimationExtensionMargin;
                rect2.bottom -= HyperPopupWindow.this.mAnimationExtensionMargin;
            }
            popupWindowSpec.mItemViewBounds = HyperPopupWindow.getItemViewBounds(this.mAdapter, this.mListView, this.mContext, popupWindowSpec.mMaxWidth, this.mMinWidth);
            popupWindowSpec.mPaddingTop = HyperPopupWindow.this.mVerticalPadding;
            popupWindowSpec.mPaddingBottom = HyperPopupWindow.this.mVerticalPadding;
            this.mPopupWindowStrategy.measureContentSize(popupWindowSpec);
            int xInWindow = this.mPopupWindowStrategy.getXInWindow(popupWindowSpec);
            int yInWindow = this.mPopupWindowStrategy.getYInWindow(popupWindowSpec);
            int i = popupWindowSpec.mFinalPopupWidth;
            int i2 = popupWindowSpec.mFinalPopupHeight;
            int i3 = xInWindow + i;
            int i4 = yInWindow + i2;
            this.mBoundsRect.set(xInWindow, yInWindow, i3, i4);
            if (HyperPopupWindow.this.mEnableSecondaryMenu) {
                HyperPopupWindow.this.offsetRootBounds(popupWindowSpec, xInWindow, yInWindow, i, i2);
            }
            if (z) {
                Rect unionBounds = HyperPopupWindow.getUnionBounds(HyperPopupWindow.this.mMainPopContentHolder.mBoundsRect, this.mBoundsRect);
                HyperPopupWindow.this.mClipView = new FrameLayout(this.mContext);
                HyperPopupWindow.this.mClipView.setBackgroundColor(0);
                HyperPopupWindow.this.mClipView.setTranslationZ(((PopupWindow) HyperPopupWindow.this).mElevation + (((PopupWindow) HyperPopupWindow.this).mElevationExtra * 2));
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(unionBounds.width(), unionBounds.height());
                layoutParams.leftMargin = unionBounds.left - rect.left;
                layoutParams.topMargin = unionBounds.top - rect.top;
                HyperPopupWindow.this.mClipView.setLayoutParams(layoutParams);
                viewGroup.addView(HyperPopupWindow.this.mClipView);
                final int i5 = rect2.left - unionBounds.left;
                final int i6 = (rect2.top - unionBounds.top) - HyperPopupWindow.this.mVerticalPadding;
                final int i7 = rect2.right - unionBounds.left;
                final int i8 = (rect2.bottom - unionBounds.top) + HyperPopupWindow.this.mVerticalPadding;
                int i9 = unionBounds.left;
                final int i10 = xInWindow - i9;
                int i11 = unionBounds.top;
                final int i12 = yInWindow - i11;
                final int i13 = i3 - i9;
                final int i14 = i4 - i11;
                this.mContentView.setLayoutParams(new FrameLayout.LayoutParams(i13 - i10, i14 - i12));
                ClipLayout clipLayout = new ClipLayout(HyperPopupWindow.this, this.mContext);
                clipLayout.setLayoutParams(new FrameLayout.LayoutParams(unionBounds.width(), unionBounds.height()));
                clipLayout.setBackgroundColor(0);
                clipLayout.prepareElevation();
                clipLayout.setClipBounds(i5, i6, i7, i8);
                clipLayout.setRadius(HyperPopupWindow.this.mCornerRadius);
                clipLayout.refreshClipPath();
                clipLayout.addView(this.mContentView);
                HyperPopupWindow.this.mClipView.addView(clipLayout);
                HyperPopupWindow.this.mInnerClip = clipLayout;
                this.mViewBounds = new ViewBounds(this.mContentView);
                HyperPopupWindow.this.mMainPopContentHolder.mIsInAnimation = true;
                HyperPopupWindow.this.mSecondaryContentHolder.mIsInAnimation = true;
                this.mContentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.PopupContentHolder.4
                    @Override // android.view.ViewTreeObserver.OnPreDrawListener
                    public boolean onPreDraw() {
                        PopupContentHolder.this.mContentView.getViewTreeObserver().removeOnPreDrawListener(this);
                        final View viewFindViewById = PopupContentHolder.this.mContentView.findViewById(R$id.tag_secondary_popup_menu_item_head);
                        if (viewFindViewById == null) {
                            return false;
                        }
                        viewFindViewById.sendAccessibilityEvent(8);
                        AnimConfig animConfigAddListeners = new AnimConfig().addListeners(new TransitionListener() { // from class: miuix.appcompat.widget.HyperPopupWindow.PopupContentHolder.4.1
                            @Override // miuix.animation.listener.TransitionListener
                            public void onUpdate(Object obj, Collection collection) {
                                super.onUpdate(obj, collection);
                                UpdateInfo updateInfoFindByName = UpdateInfo.findByName(collection, "fraction");
                                if (updateInfoFindByName != null) {
                                    float floatValue = updateInfoFindByName.getFloatValue();
                                    AnonymousClass4 anonymousClass4 = AnonymousClass4.this;
                                    int i15 = i5;
                                    int i16 = (int) (i15 + ((i10 - i15) * floatValue));
                                    int i17 = i6;
                                    int i18 = (int) (i17 + ((i12 - i17) * floatValue));
                                    int i19 = i7;
                                    int i20 = (int) (i19 + ((i13 - i19) * floatValue));
                                    int i21 = i8;
                                    HyperPopupWindow.this.mInnerClip.setClipBounds(i16, i18, i20, (int) (i21 + ((i14 - i21) * floatValue)));
                                    HyperPopupWindow.this.mInnerClip.refreshClipPath();
                                    PopupContentHolder.this.mViewBounds.updateContentLocation(i16, i18);
                                    PopupContentHolder.this.mViewBounds.updateHeaderViewWidth(viewFindViewById, i20 - i16);
                                }
                            }

                            @Override // miuix.animation.listener.TransitionListener
                            public void onComplete(Object obj) {
                                super.onComplete(obj);
                                HyperPopupWindow.this.mMainPopContentHolder.mIsInAnimation = false;
                                if (HyperPopupWindow.this.mSecondaryContentHolder != null) {
                                    HyperPopupWindow.this.mSecondaryContentHolder.mIsInAnimation = false;
                                }
                            }

                            @Override // miuix.animation.listener.TransitionListener
                            public void onCancel(Object obj) {
                                super.onCancel(obj);
                                HyperPopupWindow.this.mMainPopContentHolder.mIsInAnimation = false;
                                if (HyperPopupWindow.this.mSecondaryContentHolder != null) {
                                    HyperPopupWindow.this.mSecondaryContentHolder.mIsInAnimation = false;
                                }
                            }
                        });
                        animConfigAddListeners.setSpecial(ViewBounds.ARROW_ROTATION_PROPERTY, FolmeEase.spring(0.95f, 0.2f), new float[0]).setSpecial(ViewBounds.CORNER_PROPERTY, FolmeEase.spring(0.97f, 0.2f), new float[0]);
                        Folme.use((FolmeObject) PopupContentHolder.this.mViewBounds);
                        float cornerRadius = PopupContentHolder.this.mViewBounds.getCornerRadius();
                        float f = HyperPopupWindow.this.mCornerRadius;
                        PopupContentHolder.this.mContentView.setCornerRadius(cornerRadius);
                        AnimState animStateAdd = new AnimState().add("fraction", 1.0f).add(ViewBounds.CORNER_PROPERTY, f).add(ViewBounds.ARROW_ROTATION_PROPERTY, PopupContentHolder.this.mContentView.getLayoutDirection() == 1 ? 90.0f : -90.0f);
                        PopupContentHolder.this.mViewBounds.folme().resetTo("fraction", Float.valueOf(0.0f));
                        PopupContentHolder.this.mViewBounds.folme().to(animStateAdd, animConfigAddListeners);
                        HyperPopupWindow.this.mInnerClip.setElevation(80.0f);
                        return false;
                    }
                });
            } else {
                this.mContentView.setPivotX(i3 / 2 > rect2.centerX() ? 0.0f : i);
                this.mContentView.setPivotY(i4 / 2 <= rect2.centerY() ? i2 : 0.0f);
                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(i, i2);
                if (HyperPopupWindow.this.mEnableSecondaryMenu) {
                    layoutParams2.leftMargin = xInWindow - rect.left;
                    layoutParams2.topMargin = yInWindow - rect.top;
                }
                this.mContentView.setLayoutParams(layoutParams2);
                HyperPopupWindow hyperPopupWindow = HyperPopupWindow.this;
                hyperPopupWindow.safeAddView(hyperPopupWindow.mContainer, this.mContentView);
            }
            return true;
        }

        boolean update() {
            PopupWindowSpec popupWindowSpec = this.mPopupWindowSpec;
            HyperPopupWindow hyperPopupWindow = HyperPopupWindow.this;
            hyperPopupWindow.mRootBounds = hyperPopupWindow.getRootBounds();
            popupWindowSpec.mItemViewBounds = HyperPopupWindow.getItemViewBounds(this.mAdapter, this.mListView, this.mContext, popupWindowSpec.mMaxWidth, this.mMinWidth);
            this.mPopupWindowStrategy.measureContentSize(popupWindowSpec);
            int xInWindow = this.mPopupWindowStrategy.getXInWindow(popupWindowSpec);
            int yInWindow = this.mPopupWindowStrategy.getYInWindow(popupWindowSpec);
            int i = popupWindowSpec.mFinalPopupWidth;
            int i2 = popupWindowSpec.mFinalPopupHeight;
            this.mBoundsRect.set(xInWindow, yInWindow, xInWindow + i, yInWindow + i2);
            if (HyperPopupWindow.this.mEnableSecondaryMenu) {
                HyperPopupWindow.this.offsetRootBounds(popupWindowSpec, xInWindow, yInWindow, i, i2);
                int iWidth = HyperPopupWindow.this.mRootBounds.width();
                int iHeight = HyperPopupWindow.this.mRootBounds.height();
                HyperPopupWindow.this.setWidth(iWidth);
                HyperPopupWindow.this.setHeight(iHeight);
                HyperPopupWindow hyperPopupWindow2 = HyperPopupWindow.this;
                hyperPopupWindow2.update(hyperPopupWindow2.mRootBounds.left, HyperPopupWindow.this.mRootBounds.top, this.mBoundsRect.width(), this.mBoundsRect.height());
                return true;
            }
            int iWidth2 = this.mBoundsRect.width();
            int iHeight2 = this.mBoundsRect.height();
            HyperPopupWindow.this.setWidth(iWidth2);
            HyperPopupWindow.this.setHeight(iHeight2);
            HyperPopupWindow hyperPopupWindow3 = HyperPopupWindow.this;
            Rect rect = this.mBoundsRect;
            hyperPopupWindow3.update(rect.left, rect.top, rect.width(), this.mBoundsRect.height());
            return true;
        }

        protected void setMinWidth(int i) {
            this.mMinWidth = i;
        }

        protected void setMenuListAccessibilityDelegate() {
            ListView listView = this.mListView;
            if (listView == null) {
                return;
            }
            ViewCompat.setAccessibilityDelegate(listView, new AccessibilityDelegateCompat() { // from class: miuix.appcompat.widget.HyperPopupWindow.PopupContentHolder.5
                @Override // androidx.core.view.AccessibilityDelegateCompat
                public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
                    if (accessibilityEvent.getEventType() == 32768) {
                        HyperPopupWindow.this.mFocusedMainPopupItemView = view;
                    }
                    return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void safeAddView(ViewGroup viewGroup, View view) {
        if (viewGroup == null || view == null) {
            return;
        }
        safeAddView(viewGroup, view, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareMaterial(View view) {
        boolean zIsPassWindowBlurWhitelisted = MiuiBlurUtils.isPassWindowBlurWhitelisted(this.mContentView, this.mContext.getPackageName());
        if (!HyperMaterialUtils.isFeatureEnable(this.mContext) || !isMaterialEnabled() || !zIsPassWindowBlurWhitelisted) {
            clearMaterial(view);
        } else {
            applyMaterial(view);
        }
    }

    private void clearMaterial(View view) {
        MiuiBlurUtils.setPassWindowBlurEnabled(view, false);
        MiuiBlurUtils.clearBackgroundBlur(view);
        MiuiBlurUtils.setViewBlurMode(view, 0);
        MiuiBlurUtils.clearBackgroundBlendConfig(view);
        ShadowToken shadowToken = ShadowToken.Regular;
        MiShadowUtils.setMiShadow(view, shadowToken.color, shadowToken.offsetX, shadowToken.offsetY, shadowToken.radius);
        Drawable background = view.getBackground();
        if (background != null) {
            background.setAlpha(255);
        }
    }

    private void applyMaterial(View view) {
        Drawable background = view.getBackground();
        if (background != null) {
            background.setAlpha(0);
        }
        MaterialConfig materialConfig = MaterialDayNightConfig.create(PopupWindow.PopupView_Glass_Material).get(!ViewUtils.isNightMode(this.mContext));
        if (materialConfig != null) {
            float f = this.mContext.getResources().getDisplayMetrics().density;
            MiuiBlurUtils.setPassWindowBlurEnabled(view, true);
            applyMaterialBlur(view, materialConfig, f);
            applyMaterialColorBlend(view, materialConfig);
            applyMaterialBloomStroke(view, materialConfig);
            applyMaterialShadow(view, materialConfig);
        }
    }

    private void applyMaterialBlur(View view, MaterialConfig materialConfig, float f) {
        int i;
        if (materialConfig.getBlurConfig() == null || (i = materialConfig.getBlurConfig().blurRadius) <= 0) {
            return;
        }
        MiuiBlurUtils.setBackgroundOnlyBlur(view, (int) ((i * f) + 0.5f));
    }

    private void applyMaterialColorBlend(View view, MaterialConfig materialConfig) {
        MiuiBlurUtils.setViewBlurMode(view, 1);
        if (materialConfig.getColorBlendConfig() != null) {
            MiuiBlurUtils.setBackgroundBlendConfig(view, materialConfig.getColorBlendConfig().blendColors, materialConfig.getColorBlendConfig().blendModes);
        }
    }

    private void applyMaterialBloomStroke(View view, MaterialConfig materialConfig) {
        if (materialConfig.getBloomStrokeConfig() != null) {
            HyperBloomStrokeUtils.setBloomStrokeConfig(view, materialConfig.getBloomStrokeConfig());
        }
    }

    private void applyMaterialShadow(View view, MaterialConfig materialConfig) {
        if (materialConfig.getShadowConfig() != null) {
            MiShadowUtils.setShadowConfig(view, materialConfig.getShadowConfig());
        }
    }

    private void safeAddView(ViewGroup viewGroup, View view, int i) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        if (viewGroup != null) {
            viewGroup.addView(view, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void offsetRootBounds(PopupWindowSpec popupWindowSpec, int i, int i2, int i3, int i4) {
        Rect rect = this.mRootBounds;
        int i5 = popupWindowSpec.mMaxWidth;
        int i6 = popupWindowSpec.mMaxHeight + i2;
        int i7 = rect.bottom;
        if (i6 > i7) {
            rect.top = i2 + (i7 - i6);
        } else {
            rect.top = i2;
            rect.bottom = i6;
        }
        int absoluteGravity = Gravity.getAbsoluteGravity(popupWindowSpec.mGravity, popupWindowSpec.layoutDirection) & 7;
        if (absoluteGravity != 1) {
            if (absoluteGravity == 5) {
                rect.right = i3 + i;
                rect.left = Math.max(i - i5, rect.left);
            } else {
                rect.left = i;
                rect.right = Math.min(i + i5, rect.right);
            }
        }
    }

    private void toBackAnim() {
        SmoothFrameLayout2 smoothFrameLayout2 = this.mMainPopContentHolder.mContentView;
        View viewFindViewById = smoothFrameLayout2.findViewById(R$id.mask);
        IStateStyle iStateStyleState = Folme.use((View) smoothFrameLayout2).state();
        ViewProperty viewProperty = ViewProperty.SCALE_X;
        Float fValueOf = Float.valueOf(0.95f);
        iStateStyleState.to(viewProperty, fValueOf, ViewProperty.SCALE_Y, fValueOf, ViewBounds.sOpenConfig);
        if (viewFindViewById != null) {
            Folme.use(viewFindViewById).state().to(ViewProperty.AUTO_ALPHA, Float.valueOf(1.0f), ViewBounds.sOpenConfig);
        }
    }

    private void toFrontAnim() {
        SmoothFrameLayout2 smoothFrameLayout2 = this.mMainPopContentHolder.mContentView;
        View viewFindViewById = smoothFrameLayout2.findViewById(R$id.mask);
        IStateStyle iStateStyleState = Folme.use((View) smoothFrameLayout2).state();
        ViewProperty viewProperty = ViewProperty.SCALE_X;
        Float fValueOf = Float.valueOf(1.0f);
        iStateStyleState.to(viewProperty, fValueOf, ViewProperty.SCALE_Y, fValueOf, ViewBounds.sCloseConfig);
        if (viewFindViewById != null) {
            Folme.use(viewFindViewById).state().to(ViewProperty.AUTO_ALPHA, Float.valueOf(0.0f), ViewBounds.sCloseConfig);
        }
    }

    public class ViewBounds implements FolmeObject {
        private Folme.ObjectFolmeImpl mFolmeAnimator;
        private WeakReference mHeaderArrowView;
        private WeakReference mView;
        private static final FloatProperty ARROW_ROTATION_PROPERTY = new FloatProperty("arrowRotation") { // from class: miuix.appcompat.widget.HyperPopupWindow.ViewBounds.1
            @Override // miuix.animation.property.FloatProperty
            public float getValue(ViewBounds viewBounds) {
                return viewBounds.getArrowRotation();
            }

            @Override // miuix.animation.property.FloatProperty
            public void setValue(ViewBounds viewBounds, float f) {
                viewBounds.setArrowRotation(f);
            }
        };
        private static final FloatProperty CORNER_PROPERTY = new FloatProperty("corner") { // from class: miuix.appcompat.widget.HyperPopupWindow.ViewBounds.2
            @Override // miuix.animation.property.FloatProperty
            public float getValue(ViewBounds viewBounds) {
                return viewBounds.getCornerRadius();
            }

            @Override // miuix.animation.property.FloatProperty
            public void setValue(ViewBounds viewBounds, float f) {
                viewBounds.setCornerRadius(f);
            }
        };
        private static final AnimConfig sOpenConfig = new AnimConfig();
        private static final AnimConfig sCloseConfig = new AnimConfig().setEase(-2, 0.95f, 0.2f);
        private float mCornerRadius = 0.0f;
        private float mArrowRotation = 0.0f;

        ViewBounds(View view) {
            this.mView = new WeakReference(view);
        }

        public void setCornerRadius(float f) {
            this.mCornerRadius = f;
            View view = (View) this.mView.get();
            Drawable background = view.getBackground();
            if (view instanceof SmoothFrameLayout2) {
                ((SmoothFrameLayout2) view).setCornerRadius(this.mCornerRadius);
            }
            if (background instanceof SmoothContainerDrawable2) {
                ((SmoothContainerDrawable2) background).setCornerRadius(this.mCornerRadius);
            }
        }

        public float getCornerRadius() {
            return this.mCornerRadius;
        }

        public void setArrowRotation(float f) {
            this.mArrowRotation = f;
            WeakReference weakReference = this.mHeaderArrowView;
            if (weakReference == null || weakReference.get() == null) {
                if (this.mView.get() == null) {
                    return;
                }
                View viewFindViewById = ((View) this.mView.get()).findViewById(R$id.tag_secondary_popup_menu_item_head).findViewById(R$id.arrow);
                this.mHeaderArrowView = new WeakReference(viewFindViewById);
                viewFindViewById.setPivotX(viewFindViewById.getWidth() / 2.0f);
                viewFindViewById.setPivotY(viewFindViewById.getHeight() / 2.0f);
            }
            ((View) this.mHeaderArrowView.get()).setRotation(f);
        }

        public float getArrowRotation() {
            return this.mArrowRotation;
        }

        public void updateHeaderViewWidth(View view, int i) {
            if (view == null) {
                return;
            }
            ((AbsListView.LayoutParams) view.getLayoutParams()).width = i;
            view.requestLayout();
        }

        public void updateContentLocation(int i, int i2) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ((View) this.mView.get()).getLayoutParams();
            layoutParams.leftMargin = i;
            layoutParams.topMargin = i2;
        }

        @Override // miuix.animation.FolmeObject
        public void setFolmeImpl(Folme.ObjectFolmeImpl objectFolmeImpl) {
            this.mFolmeAnimator = objectFolmeImpl;
        }

        @Override // miuix.animation.FolmeObject
        public Folme.ObjectFolmeImpl folme() {
            return this.mFolmeAnimator;
        }
    }

    class SecondaryPopupWindowStrategy extends PopupWindowStrategy {
        SecondaryPopupWindowStrategy() {
        }

        @Override // miuix.popupwidget.internal.strategy.PopupWindowStrategy, miuix.popupwidget.internal.strategy.IPopupWindowStrategy
        public int getXInWindow(PopupWindowSpec popupWindowSpec) {
            int i;
            int i2;
            Rect rect = popupWindowSpec.mAnchorViewBounds;
            Rect rect2 = popupWindowSpec.mDecorViewBounds;
            int i3 = popupWindowSpec.mFinalPopupWidth;
            if (popupWindowSpec.layoutDirection == 1) {
                i2 = rect.right;
                i = i2 - i3;
                int i4 = rect2.left;
                if (i < i4) {
                    i2 = i4 + i3;
                    i = i4;
                }
                int i5 = rect2.right;
                if (i2 > i5) {
                    i2 = i5;
                }
            } else {
                int i6 = rect.left;
                int i7 = i6 + i3;
                int i8 = rect2.right;
                if (i7 > i8) {
                    i = i8 - i3;
                    i2 = i8;
                } else {
                    i = i6;
                    i2 = i7;
                }
                int i9 = rect2.left;
                if (i < i9) {
                    i = i9;
                }
            }
            popupWindowSpec.mFinalPopupWidth = i2 - i;
            return i;
        }

        @Override // miuix.popupwidget.internal.strategy.PopupWindowStrategy, miuix.popupwidget.internal.strategy.IPopupWindowStrategy
        public int getYInWindow(PopupWindowSpec popupWindowSpec) {
            Rect rect = popupWindowSpec.mAnchorViewBounds;
            Rect rect2 = popupWindowSpec.mDecorViewBounds;
            int i = popupWindowSpec.mFinalPopupHeight;
            int i2 = rect.top;
            int i3 = i2 + i;
            int i4 = rect2.bottom;
            if (i3 < i4) {
                return i2;
            }
            int i5 = i4 - i;
            int i6 = rect2.top;
            if (i5 >= i6) {
                return i5;
            }
            popupWindowSpec.mFinalPopupHeight = i4 - i6;
            return i6;
        }
    }

    public class ClipLayout extends FrameLayout {
        private OnBackInvokedCallback backCallBack;
        OnBackInvokedDispatcher dispatcher;
        private int mBottom;
        private final Path mClipPath;
        private final RectF mClipRoundRect;
        private boolean mIsClip;
        private int mLeft;
        private float mRadius;
        private int mRight;
        private int mTop;
        private boolean shouldSetElevation;

        @Override // android.view.ViewGroup
        public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
            return false;
        }

        public ClipLayout(HyperPopupWindow hyperPopupWindow, Context context) {
            this(hyperPopupWindow, context, null);
        }

        public ClipLayout(HyperPopupWindow hyperPopupWindow, Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public ClipLayout(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            this.mIsClip = false;
            this.mClipRoundRect = new RectF();
            this.mClipPath = new Path();
        }

        public void prepareElevation() {
            this.shouldSetElevation = true;
            setOutlineProvider(new ViewOutlineProvider() { // from class: miuix.appcompat.widget.HyperPopupWindow.ClipLayout.1
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(ClipLayout.this.mLeft, ClipLayout.this.mTop, ClipLayout.this.mRight, ClipLayout.this.mBottom, ClipLayout.this.mRadius);
                }
            });
        }

        @Override // android.view.View
        public void setElevation(float f) {
            if (MiShadowUtils.SUPPORT_MI_SHADOW) {
                ShadowToken shadowToken = ShadowToken.Regular;
                MiShadowUtils.setMiShadow(this, shadowToken.color, shadowToken.offsetX, shadowToken.offsetY, shadowToken.radius);
            } else {
                super.setElevation(f);
            }
        }

        public void clearElevation() {
            if (MiShadowUtils.SUPPORT_MI_SHADOW) {
                MiShadowUtils.clearMiShadow(this);
            } else {
                super.setElevation(0.0f);
            }
        }

        public void setRadius(float f) {
            this.mRadius = f;
        }

        public void setClipBounds(int i, int i2, int i3, int i4) {
            this.mClipRoundRect.set(i, i2, i3, i4);
            if (this.shouldSetElevation) {
                this.mLeft = i;
                this.mTop = i2;
                this.mRight = i3;
                this.mBottom = i4;
                invalidateOutline();
            }
        }

        public void refreshClipPath() {
            this.mClipPath.reset();
            Path path = this.mClipPath;
            RectF rectF = this.mClipRoundRect;
            float f = this.mRadius;
            path.addRoundRect(rectF, f, f, Path.Direction.CW);
            this.mIsClip = true;
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            if (this.mIsClip) {
                canvas.clipPath(this.mClipPath);
            }
            super.draw(canvas);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.dispatcher = findOnBackInvokedDispatcher();
            final HyperPopupWindow hyperPopupWindow = HyperPopupWindow.this;
            OnBackInvokedCallback onBackInvokedCallback = new OnBackInvokedCallback() { // from class: miuix.appcompat.widget.HyperPopupWindow$ClipLayout$$ExternalSyntheticLambda0
                @Override // android.window.OnBackInvokedCallback
                public final void onBackInvoked() {
                    hyperPopupWindow.collapseSecondaryMenu();
                }
            };
            this.backCallBack = onBackInvokedCallback;
            OnBackInvokedDispatcher onBackInvokedDispatcher = this.dispatcher;
            if (onBackInvokedDispatcher != null) {
                onBackInvokedDispatcher.registerOnBackInvokedCallback(AnimState.VIEW_SIZE, onBackInvokedCallback);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            OnBackInvokedDispatcher onBackInvokedDispatcher = this.dispatcher;
            if (onBackInvokedDispatcher != null) {
                onBackInvokedDispatcher.unregisterOnBackInvokedCallback(this.backCallBack);
            }
        }
    }
}
