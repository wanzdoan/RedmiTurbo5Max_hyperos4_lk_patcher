package miuix.appcompat.app.strategy;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import miuix.appcompat.app.DialogContract;

/* JADX INFO: loaded from: classes2.dex */
public class DialogPanelBehaviorImpl implements IDialogPanelBehavior {
    @Override // miuix.appcompat.app.strategy.IDialogPanelBehavior
    public boolean shouldLimitPanelWidth(int i) {
        return i >= 404;
    }

    @Override // miuix.appcompat.app.strategy.IDialogPanelBehavior
    public boolean isLandscapeWindow(DialogContract.OrientationSpec orientationSpec) {
        if (orientationSpec.mMarkLandscape) {
            return true;
        }
        if (orientationSpec.mInFreeFrom) {
            return judgeLandscape(orientationSpec.mWindowSize, orientationSpec.mScreenOrientation);
        }
        if (orientationSpec.mScreenOrientation != 2) {
            return false;
        }
        if (orientationSpec.mIsCarWithScreen || orientationSpec.mIsSynergy) {
            Point point = orientationSpec.mRealScreenSize;
            return point.x > point.y;
        }
        Point point2 = orientationSpec.mUsableWindowSizeDp;
        int i = point2.x;
        return i >= 404 && i > point2.y;
    }

    private boolean judgeLandscape(Point point, int i) {
        int i2 = point.x;
        int i3 = point.y;
        if (i2 > i3) {
            return true;
        }
        return i2 >= i3 && i == 2;
    }

    @Override // miuix.appcompat.app.strategy.IDialogPanelBehavior
    public int calcPanelPosition(DialogContract.PanelPosSpec panelPosSpec, DialogContract.DimensConfig dimensConfig, Rect rect) {
        int i;
        int i2;
        Rect rect2 = rect == null ? new Rect() : rect;
        int iMax = Math.max(panelPosSpec.mRootViewSizeX, panelPosSpec.mRootViewWidth);
        boolean z = panelPosSpec.mRootViewPaddingLeft + panelPosSpec.mRootViewPaddingRight > 0;
        int i3 = panelPosSpec.mDesignedPanelWidth;
        int iCalcDesignedWidthMargin = calcDesignedWidthMargin(dimensConfig, panelPosSpec.mUsableWindowWidthDp);
        if (i3 == -1) {
            i3 = panelPosSpec.mUsableWindowWidth - (iCalcDesignedWidthMargin * 2);
        }
        int i4 = panelPosSpec.mIsFlipTiny ? dimensConfig.widthSmallMargin : dimensConfig.extraImeMargin;
        int iMax2 = Math.max(panelPosSpec.mBoundInsets.top, i4);
        Rect rect3 = panelPosSpec.mBoundInsets;
        int i5 = rect3.left;
        int i6 = rect3.right;
        int i7 = (i5 + i6) / 2;
        int i8 = (iMax - i3) / 2;
        boolean z2 = i8 < i5 || i8 < i6;
        if (panelPosSpec.mIsDebugMode) {
            Log.d("IPanelBehavior", "calcPanelPosition: panelPosSpec = " + panelPosSpec);
            Log.d("IPanelBehavior", "calcPanelPosition: avoidMoved = " + i7);
            Log.d("IPanelBehavior", "calcPanelPosition: horizontalMargin = " + iCalcDesignedWidthMargin);
            Log.d("IPanelBehavior", "calcPanelPosition: centerBlankSpace = " + i8);
            Log.d("IPanelBehavior", "calcPanelPosition: widthSmallMargin = " + dimensConfig.widthSmallMargin);
        }
        if (i7 == 0 || !z2 || z) {
            i = iCalcDesignedWidthMargin;
            i2 = i;
        } else {
            Rect rect4 = panelPosSpec.mBoundInsets;
            int i9 = rect4.left;
            int i10 = rect4.right;
            if (i9 > i10) {
                i = i7 + iCalcDesignedWidthMargin;
                i2 = iCalcDesignedWidthMargin;
            } else if (i9 < i10) {
                i2 = i7 + iCalcDesignedWidthMargin;
                i = iCalcDesignedWidthMargin;
            } else {
                i = iCalcDesignedWidthMargin;
                i2 = i;
            }
            if (panelPosSpec.mIsDebugMode) {
                Log.d("IPanelBehavior", "calcPanelPosition: leftMargin = " + i);
                Log.d("IPanelBehavior", "calcPanelPosition: rightMargin = " + i2);
                Log.d("IPanelBehavior", "calcPanelPosition: realRootViewWidth = " + iMax);
            }
        }
        boolean z3 = i8 < i7;
        if (z3) {
            i3 = panelPosSpec.mUsableWindowWidth - (iCalcDesignedWidthMargin * 2);
        }
        if (panelPosSpec.mIsDebugMode) {
            Log.d("IPanelBehavior", "calcPanelPosition: isOverflow = " + z3);
            Log.d("IPanelBehavior", "calcPanelPosition: panelWidth = " + i3);
        }
        rect2.left = i;
        rect2.top = iMax2;
        rect2.right = i2;
        rect2.bottom = i4;
        return i3;
    }

    @Override // miuix.appcompat.app.strategy.IDialogPanelBehavior
    public int calcDesignedPanelWidth(DialogContract.PanelWidthSpec panelWidthSpec, DialogContract.DimensConfig dimensConfig) {
        int i;
        boolean zShouldLimitPanelWidth = shouldLimitPanelWidth(panelWidthSpec.mUsableWindowWidthDp);
        if (panelWidthSpec.mUseLandscapeLayout) {
            i = dimensConfig.panelMaxWidthLand;
        } else if (zShouldLimitPanelWidth) {
            i = dimensConfig.panelMaxWidth;
        } else if (panelWidthSpec.mIsLandscapeWindow) {
            i = panelWidthSpec.mMarkLandscapeWindow ? dimensConfig.fakeLandScreenMinorSize : panelWidthSpec.mScreenMinorSize;
        } else {
            i = -1;
        }
        if (i != -1 && panelWidthSpec.mIsCarWithScreen) {
            i = (int) (i * 0.8f);
        }
        if (panelWidthSpec.mIsDebugMode) {
            Log.d("IPanelBehavior", "calcDesignedPanelWidth: panelWidthSpec = " + panelWidthSpec);
            Log.d("IPanelBehavior", "calcDesignedPanelWidth: shouldLimitPanelLimit = " + zShouldLimitPanelWidth);
            Log.d("IPanelBehavior", "calcDesignedPanelWidth: panelWidth = " + i);
        }
        return i;
    }

    @Override // miuix.appcompat.app.strategy.IDialogPanelBehavior
    public int calcDesignedWidthMargin(DialogContract.DimensConfig dimensConfig, int i) {
        if (i < 360) {
            return dimensConfig.widthSmallMargin;
        }
        if (i <= 404) {
            return dimensConfig.widthMargin;
        }
        return 0;
    }
}
