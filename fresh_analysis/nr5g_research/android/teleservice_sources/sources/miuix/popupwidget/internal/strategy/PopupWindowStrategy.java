package miuix.popupwidget.internal.strategy;

import android.graphics.Rect;
import android.view.Gravity;

/* JADX INFO: loaded from: classes2.dex */
public class PopupWindowStrategy implements IPopupWindowStrategy {
    @Override // miuix.popupwidget.internal.strategy.IPopupWindowStrategy
    public void measureContentSize(PopupWindowSpec popupWindowSpec) {
        int[][] iArr = popupWindowSpec.mItemViewBounds;
        if (iArr != null) {
            int i = popupWindowSpec.mMaxWidth;
            int i2 = popupWindowSpec.mMaxHeight;
            int i3 = 0;
            int iMax = 0;
            for (int[] iArr2 : iArr) {
                int i4 = iArr2[0];
                int i5 = iArr2[1];
                if (i4 > i) {
                    i4 = i;
                }
                iMax = Math.max(i4, iMax);
                i3 += i5;
            }
            popupWindowSpec.mContentHeight = i3;
            if (i3 <= i2) {
                i2 = i3;
            }
            popupWindowSpec.mFinalPopupHeight = i2 + popupWindowSpec.mPaddingTop + popupWindowSpec.mPaddingBottom;
            int iMax2 = Math.max(Math.min(i, iMax + popupWindowSpec.mPaddingLeft + popupWindowSpec.mPaddingRight), popupWindowSpec.mMinWidth);
            popupWindowSpec.mContentWidth = iMax2;
            popupWindowSpec.mFinalPopupWidth = iMax2;
            return;
        }
        Rect rect = popupWindowSpec.mContentViewBounds;
        popupWindowSpec.mContentHeight = rect.height();
        popupWindowSpec.mFinalPopupWidth = rect.width();
        popupWindowSpec.mFinalPopupHeight = rect.height();
    }

    @Override // miuix.popupwidget.internal.strategy.IPopupWindowStrategy
    public boolean isNeedScroll(int i, PopupWindowSpec popupWindowSpec) {
        int i2 = popupWindowSpec.mContentHeight;
        return i2 > i || i2 > popupWindowSpec.mMaxHeight;
    }

    @Override // miuix.popupwidget.internal.strategy.IPopupWindowStrategy
    public int getXInWindow(PopupWindowSpec popupWindowSpec) {
        int absoluteGravity = Gravity.getAbsoluteGravity(popupWindowSpec.mGravity, popupWindowSpec.layoutDirection) & 7;
        if (absoluteGravity == 1) {
            return getXInWindowAlignCenterHorizontal(popupWindowSpec);
        }
        if (absoluteGravity == 5) {
            return getXInWindowAlightRight(popupWindowSpec);
        }
        return getXInWindowAlignLeft(popupWindowSpec);
    }

    private int getXInWindowAlignCenterHorizontal(PopupWindowSpec popupWindowSpec) {
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect rect2 = popupWindowSpec.mDecorViewBounds;
        Rect rect3 = popupWindowSpec.mSafeInsets;
        int i = popupWindowSpec.mFinalPopupWidth;
        int iCenterX = rect.centerX() - (i / 2);
        int i2 = iCenterX + i;
        int i3 = rect2.right;
        int i4 = rect3.right;
        if (i2 > i3 - i4) {
            iCenterX = (i3 - i4) - i;
        }
        int i5 = rect2.left;
        int i6 = rect3.left;
        if (iCenterX < i5 + i6) {
            iCenterX = i5 + i6;
        }
        if (iCenterX + i > i3 - i4) {
            i = (i3 - i4) - iCenterX;
        }
        popupWindowSpec.mFinalPopupWidth = i;
        return iCenterX;
    }

    private int getXInWindowAlignLeft(PopupWindowSpec popupWindowSpec) {
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect rect2 = popupWindowSpec.mDecorViewBounds;
        Rect rect3 = popupWindowSpec.mSafeInsets;
        int i = popupWindowSpec.mFinalPopupWidth;
        int i2 = rect.left;
        int i3 = rect2.left;
        int i4 = rect3.left;
        if (i2 < i3 + i4) {
            i2 = i3 + i4;
        }
        int i5 = i2 + i;
        int i6 = rect2.right;
        int i7 = rect3.right;
        if (i5 > i6 - i7) {
            i5 = i6 - i7;
        }
        int i8 = i5 - i;
        if (i8 >= i3 + i4) {
            return i8;
        }
        int i9 = i3 + i4;
        popupWindowSpec.mFinalPopupWidth = i5 - i9;
        return i9;
    }

    private int getXInWindowAlightRight(PopupWindowSpec popupWindowSpec) {
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect rect2 = popupWindowSpec.mDecorViewBounds;
        Rect rect3 = popupWindowSpec.mSafeInsets;
        int i = popupWindowSpec.mFinalPopupWidth;
        int i2 = rect.right;
        int i3 = rect2.right;
        int i4 = rect3.right;
        if (i2 > i3 - i4) {
            i2 = i3 - i4;
        }
        int i5 = i2 - i;
        int i6 = rect2.left;
        int i7 = rect3.left;
        if (i5 < i6 + i7) {
            i5 = i6 + i7;
        }
        if (i5 + i > i3 - i4) {
            i = (i3 - i4) - i5;
        }
        popupWindowSpec.mFinalPopupWidth = i;
        return i5;
    }

    @Override // miuix.popupwidget.internal.strategy.IPopupWindowStrategy
    public int getYInWindow(PopupWindowSpec popupWindowSpec) {
        if ((popupWindowSpec.mGravity & 112) == 48) {
            return getYInWindowAlignTop(popupWindowSpec);
        }
        return getYInWindowAlignBottom(popupWindowSpec);
    }

    private int getYInWindowAlignTop(PopupWindowSpec popupWindowSpec) {
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect rect2 = popupWindowSpec.mDecorViewBounds;
        Rect rect3 = popupWindowSpec.mSafeInsets;
        int i = popupWindowSpec.mFinalPopupHeight;
        int i2 = rect.top;
        int i3 = rect2.top;
        int i4 = rect3.top;
        int i5 = i2 < i3 + i4 ? i3 + i4 : i2;
        int i6 = i5 + i;
        int i7 = rect2.bottom;
        int i8 = rect3.bottom;
        if (i6 < i7 - i8) {
            return i5;
        }
        if (i7 - i2 >= i2 - i3) {
            int iMin = (i7 - i8) - i5;
            if (iMin < popupWindowSpec.mMinHeight) {
                iMin = Math.min(i, (rect2.height() - rect3.top) - rect3.bottom);
                i5 = (rect2.bottom - rect3.bottom) - iMin;
            }
            popupWindowSpec.mFinalPopupHeight = iMin;
            return i5;
        }
        int iMin2 = Math.min(i, (i2 - i3) - i4);
        if (iMin2 < popupWindowSpec.mMinHeight) {
            iMin2 = Math.min(i, (rect2.height() - rect3.top) - rect3.bottom);
        }
        int i9 = rect.top - iMin2;
        popupWindowSpec.mFinalPopupHeight = iMin2;
        return i9;
    }

    private int getYInWindowAlignBottom(PopupWindowSpec popupWindowSpec) {
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect rect2 = popupWindowSpec.mDecorViewBounds;
        Rect rect3 = popupWindowSpec.mSafeInsets;
        int i = popupWindowSpec.mFinalPopupHeight;
        int i2 = rect.bottom;
        int i3 = rect2.top;
        int i4 = rect3.top;
        if (i2 < i3 + i4) {
            i2 = i3 + i4;
        }
        int i5 = i2 + i;
        int i6 = rect2.bottom;
        int i7 = rect3.bottom;
        if (i5 < i6 - i7) {
            return i2;
        }
        int i8 = rect.top;
        if (i6 - i8 >= i8 - i3) {
            int iMin = (i6 - i7) - i2;
            if (iMin < popupWindowSpec.mMinHeight) {
                iMin = Math.min(i, (rect2.height() - rect3.top) - rect3.bottom);
                i2 = (rect2.bottom - rect3.bottom) - iMin;
            }
            popupWindowSpec.mFinalPopupHeight = iMin;
            return i2;
        }
        int iMin2 = Math.min(i, (i8 - i3) - i4);
        if (iMin2 < popupWindowSpec.mMinHeight) {
            iMin2 = Math.min(i, (rect2.height() - rect3.top) - rect3.bottom);
        }
        popupWindowSpec.mFinalPopupHeight = iMin2;
        return rect.top - iMin2;
    }
}
