package miuix.appcompat.widget;

import android.R;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import miuix.appcompat.view.menu.HyperMenuContract$HyperMenuItem;
import miuix.appcompat.view.menu.HyperMenuContract$HyperMenuTextItem;
import miuix.appcompat.view.menu.HyperSecondaryAdapter;

/* JADX INFO: loaded from: classes2.dex */
public class SubtitleAnimHelper {
    private final HyperSecondaryAdapter mAdapter;
    private PopupHeightAnimator mAnimator;
    private int mBaseContainerHeight;
    private int mBaseSubtitleHeight;
    private Rect mBoundsRect;
    private FrameLayout mClipView;
    private View mContentView;
    private PopupHeightAnimator.IHeightAnimationListener mIHeightAnimationListener;
    private int mInitialBoundsTop;
    private int mInitialContainerHeight;
    private HyperPopupWindow.ClipLayout mInnerClip;
    private final ListView mListView;
    private Rect mMainBoundsRect;
    private boolean mIsAnimating = false;
    private final Rect mTempUnionRect = new Rect();
    private final int[] mTempBounds = new int[2];
    private int mSafeBottomY = Integer.MAX_VALUE;
    private int mSafeTopY = 0;
    private int mRootBoundsTop = 0;

    public SubtitleAnimHelper(HyperSecondaryAdapter hyperSecondaryAdapter, ListView listView) {
        this.mAdapter = hyperSecondaryAdapter;
        this.mListView = listView;
    }

    public void bindContainerViews(Rect rect, Rect rect2, FrameLayout frameLayout, HyperPopupWindow.ClipLayout clipLayout, View view) {
        this.mBoundsRect = rect;
        this.mMainBoundsRect = rect2;
        this.mClipView = frameLayout;
        this.mInnerClip = clipLayout;
        this.mContentView = view;
        this.mInitialBoundsTop = rect.top;
        this.mInitialContainerHeight = rect.height();
    }

    public void setSafeBottomY(int i) {
        this.mSafeBottomY = i;
    }

    public void setSafeTopY(int i) {
        this.mSafeTopY = i;
    }

    public void setRootBoundsTop(int i) {
        this.mRootBoundsTop = i;
    }

    public int getCurrentSubtitleHeight() {
        View viewFindViewById;
        PopupHeightAnimator popupHeightAnimator;
        if (this.mIsAnimating && (popupHeightAnimator = this.mAnimator) != null) {
            return Math.round(popupHeightAnimator.getCurrentHeight());
        }
        View headerView = this.mAdapter.getHeaderView();
        if (headerView == null || (viewFindViewById = headerView.findViewById(R.id.text2)) == null || viewFindViewById.getVisibility() != 0) {
            return 0;
        }
        return viewFindViewById.getHeight();
    }

    public void onHeightChanged(int i) {
        View headerView = this.mAdapter.getHeaderView();
        if (headerView == null) {
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        View viewFindViewById = headerView.findViewById(R.id.text2);
        if (viewFindViewById == null) {
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        refreshNonHeaderItems();
        CharSequence headerSubtitle = getHeaderSubtitle();
        int iMeasureNaturalHeight = 0;
        if (!TextUtils.isEmpty(headerSubtitle)) {
            ((TextView) viewFindViewById).setText(headerSubtitle);
            viewFindViewById.setVisibility(0);
            iMeasureNaturalHeight = measureNaturalHeight(viewFindViewById, headerView.getWidth());
        }
        if (i == iMeasureNaturalHeight) {
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        Rect rect = this.mBoundsRect;
        if (rect != null) {
            this.mBaseContainerHeight = rect.height();
            this.mBaseSubtitleHeight = i;
        }
        startAnimation(viewFindViewById, i, iMeasureNaturalHeight);
    }

    public void release() {
        this.mAnimator = null;
        this.mBoundsRect = null;
        this.mMainBoundsRect = null;
        this.mClipView = null;
        this.mInnerClip = null;
        this.mContentView = null;
        this.mIHeightAnimationListener = null;
    }

    private void refreshNonHeaderItems() {
        View childAt;
        int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        int childCount = this.mListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            int i2 = firstVisiblePosition + i;
            if (i2 >= 2 && i2 < this.mAdapter.getCount() && (childAt = this.mListView.getChildAt(i)) != null) {
                this.mAdapter.getView(i2, childAt, this.mListView);
            }
        }
    }

    private CharSequence getHeaderSubtitle() {
        if (this.mAdapter.getCount() == 0) {
            return null;
        }
        HyperMenuContract$HyperMenuItem hyperMenuItem = this.mAdapter.getHyperMenuItem(0);
        if (hyperMenuItem instanceof HyperMenuContract$HyperMenuTextItem) {
            return ((HyperMenuContract$HyperMenuTextItem) hyperMenuItem).getSubtitle();
        }
        return null;
    }

    private int measureNaturalHeight(View view, int i) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int i2 = layoutParams.height;
        layoutParams.height = -2;
        view.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        int measuredHeight = view.getMeasuredHeight();
        layoutParams.height = i2;
        return measuredHeight;
    }

    private void startAnimation(final View view, int i, int i2) {
        if (this.mAnimator == null) {
            this.mAnimator = new PopupHeightAnimator();
        }
        this.mIsAnimating = true;
        this.mAdapter.setHeightAnimating(true);
        view.setVisibility(0);
        applySubtitleHeight(view, i);
        this.mAnimator.setHeight(i);
        this.mAnimator.setAnimationListener(new PopupHeightAnimator.IHeightAnimationListener() { // from class: miuix.appcompat.widget.SubtitleAnimHelper.1
            @Override // miuix.appcompat.widget.PopupHeightAnimator.IHeightAnimationListener
            public void onAnimationHeightUpdate(int i3) {
                SubtitleAnimHelper.this.applySubtitleHeight(view, i3);
                SubtitleAnimHelper.this.updateContainerBounds(i3);
                if (SubtitleAnimHelper.this.mIHeightAnimationListener != null) {
                    SubtitleAnimHelper.this.mIHeightAnimationListener.onAnimationHeightUpdate(i3);
                }
            }

            @Override // miuix.appcompat.widget.PopupHeightAnimator.IHeightAnimationListener
            public void onAnimationEnd() {
                if (SubtitleAnimHelper.this.mIHeightAnimationListener != null) {
                    SubtitleAnimHelper.this.mIHeightAnimationListener.onAnimationEnd();
                }
                SubtitleAnimHelper.this.finishAnimation();
            }
        });
        this.mAnimator.animateTo(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateContainerBounds(int i) {
        if (this.mBoundsRect == null || this.mMainBoundsRect == null) {
            return;
        }
        int i2 = i - this.mBaseSubtitleHeight;
        int[] iArrCalculateNewBounds = calculateNewBounds(this.mBaseContainerHeight + i2, i2);
        int i3 = iArrCalculateNewBounds[0];
        int i4 = iArrCalculateNewBounds[1];
        Rect rect = this.mBoundsRect;
        rect.top = i3;
        rect.bottom = i4;
        applyLayoutChanges(i3, i4 - i3);
    }

    private int[] calculateNewBounds(int i, int i2) {
        int i3 = this.mInitialBoundsTop;
        int i4 = this.mInitialContainerHeight;
        int i5 = this.mSafeBottomY - (i3 + i4);
        int i6 = i3 - this.mSafeTopY;
        int i7 = i5 + i6;
        if (i7 <= 0) {
            int[] iArr = this.mTempBounds;
            Rect rect = this.mBoundsRect;
            iArr[0] = rect.top;
            iArr[1] = rect.bottom;
            return iArr;
        }
        int i8 = i - i4;
        if (i2 < 0 && i7 > 0) {
            return calculateShrinkBounds(i, i5, i6);
        }
        if (i8 <= 0) {
            int[] iArr2 = this.mTempBounds;
            iArr2[0] = i3;
            iArr2[1] = i3 + i;
            return iArr2;
        }
        return calculateExpandBounds(i8, i5, i6);
    }

    private int[] calculateExpandBounds(int i, int i2, int i3) {
        int iMin = Math.min(i, i2);
        int iMin2 = Math.min(i - iMin, i3);
        int i4 = this.mInitialContainerHeight + iMin + iMin2;
        int i5 = this.mInitialBoundsTop - iMin2;
        int[] iArr = this.mTempBounds;
        iArr[0] = i5;
        iArr[1] = i5 + i4;
        return iArr;
    }

    private int[] calculateShrinkBounds(int i, int i2, int i3) {
        int i4 = i - this.mInitialContainerHeight;
        if (i4 <= 0) {
            int[] iArr = this.mTempBounds;
            int i5 = this.mInitialBoundsTop;
            iArr[0] = i5;
            iArr[1] = i5 + i;
            return iArr;
        }
        return calculateExpandBounds(i4, i2, i3);
    }

    private void applyLayoutChanges(int i, int i2) {
        boolean z = (this.mClipView == null || this.mInnerClip == null) ? false : true;
        Rect unionBounds = z ? getUnionBounds(this.mMainBoundsRect, this.mBoundsRect) : null;
        if (z) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mClipView.getLayoutParams();
            layoutParams.width = unionBounds.width();
            layoutParams.height = unionBounds.height();
            layoutParams.topMargin = unionBounds.top - this.mRootBoundsTop;
            this.mClipView.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mInnerClip.getLayoutParams();
            layoutParams2.width = unionBounds.width();
            layoutParams2.height = unionBounds.height();
            this.mInnerClip.setLayoutParams(layoutParams2);
        }
        View view = this.mContentView;
        if (view != null) {
            ViewGroup.LayoutParams layoutParams3 = view.getLayoutParams();
            if (layoutParams3 instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) layoutParams3;
                layoutParams4.height = i2;
                if (z) {
                    layoutParams4.topMargin = i - unionBounds.top;
                    layoutParams4.leftMargin = this.mBoundsRect.left - unionBounds.left;
                }
                this.mContentView.setLayoutParams(layoutParams4);
            }
        }
        if (z) {
            Rect rect = this.mBoundsRect;
            int i3 = rect.left - unionBounds.left;
            int i4 = i - unionBounds.top;
            this.mInnerClip.setClipBounds(i3, i4, rect.width() + i3, i2 + i4);
            this.mInnerClip.refreshClipPath();
        }
    }

    private Rect getUnionBounds(Rect rect, Rect rect2) {
        this.mTempUnionRect.left = Math.min(rect.left, rect2.left);
        this.mTempUnionRect.top = Math.min(rect.top, rect2.top);
        this.mTempUnionRect.right = Math.max(rect.right, rect2.right);
        this.mTempUnionRect.bottom = Math.max(rect.bottom, rect2.bottom);
        return this.mTempUnionRect;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishAnimation() {
        this.mIHeightAnimationListener = null;
        this.mIsAnimating = false;
        this.mAdapter.setHeightAnimating(false);
        this.mAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applySubtitleHeight(View view, int i) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || layoutParams.height == i) {
            return;
        }
        layoutParams.height = i;
        view.setLayoutParams(layoutParams);
    }

    public void setIHeightAnimationListener(PopupHeightAnimator.IHeightAnimationListener iHeightAnimationListener) {
        this.mIHeightAnimationListener = iHeightAnimationListener;
    }
}
