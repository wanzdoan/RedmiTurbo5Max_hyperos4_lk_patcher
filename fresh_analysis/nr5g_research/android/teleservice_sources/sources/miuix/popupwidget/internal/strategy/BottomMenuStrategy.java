package miuix.popupwidget.internal.strategy;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import miuix.popupwidget.R$dimen;

/* JADX INFO: loaded from: classes2.dex */
public class BottomMenuStrategy extends PopupWindowStrategy {
    private boolean mAdjust = false;
    private final int mOffsetYPx;

    public BottomMenuStrategy(Context context) {
        this.mOffsetYPx = context.getResources().getDimensionPixelSize(R$dimen.miuix_popup_bottom_menu_vertical_offset);
    }

    @Override // miuix.popupwidget.internal.strategy.IPopupWindowStrategy
    public int getSafeMarginResId() {
        return R$dimen.miuix_popup_window_bottom_menu_safe_margin;
    }

    @Override // miuix.popupwidget.internal.strategy.PopupWindowStrategy, miuix.popupwidget.internal.strategy.IPopupWindowStrategy
    public void measureContentSize(PopupWindowSpec popupWindowSpec) {
        super.measureContentSize(popupWindowSpec);
        if (!this.mAdjust || popupWindowSpec.mItemViewBounds == null) {
            return;
        }
        popupWindowSpec.mFinalPopupHeight += 70;
        popupWindowSpec.mFinalPopupWidth += 70;
    }

    @Override // miuix.popupwidget.internal.strategy.PopupWindowStrategy, miuix.popupwidget.internal.strategy.IPopupWindowStrategy
    public int getXInWindow(PopupWindowSpec popupWindowSpec) {
        if (!this.mAdjust) {
            return super.getXInWindow(popupWindowSpec);
        }
        int absoluteGravity = Gravity.getAbsoluteGravity(popupWindowSpec.mGravity, popupWindowSpec.layoutDirection) & 7;
        if (absoluteGravity == 1) {
            return getXInWindowAlignCenterHorizontalAdjust(popupWindowSpec);
        }
        if (absoluteGravity == 5) {
            return getXInWindowAlignRightAdjust(popupWindowSpec);
        }
        return getXInWindowAlignLeftAdjust(popupWindowSpec);
    }

    private int getXInWindowAlignCenterHorizontalAdjust(PopupWindowSpec popupWindowSpec) {
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect rect2 = popupWindowSpec.mDecorViewBounds;
        Rect rect3 = popupWindowSpec.mSafeInsets;
        int i = popupWindowSpec.mFinalPopupWidth;
        int iCenterX = rect.centerX() - (i / 2);
        int i2 = iCenterX + i;
        int i3 = rect2.right;
        int i4 = rect3.right;
        if (i2 > (i3 - i4) + 35) {
            iCenterX = ((i3 - i4) + 35) - i;
        }
        int i5 = rect2.left;
        int i6 = rect3.left;
        if (iCenterX < (i5 + i6) - 35) {
            iCenterX = (i5 + i6) - 35;
        }
        if (iCenterX + i > (i3 - i4) + 35) {
            i = ((i3 - i4) + 35) - iCenterX;
        }
        popupWindowSpec.mFinalPopupWidth = i;
        return iCenterX;
    }

    private int getXInWindowAlignLeftAdjust(PopupWindowSpec popupWindowSpec) {
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect rect2 = popupWindowSpec.mDecorViewBounds;
        Rect rect3 = popupWindowSpec.mSafeInsets;
        int i = popupWindowSpec.mFinalPopupWidth;
        int i2 = rect.left - 35;
        int i3 = rect2.left;
        int i4 = rect3.left;
        if (i2 < (i3 + i4) - 35) {
            i2 = (i3 + i4) - 35;
        }
        int i5 = i2 + i;
        int i6 = rect2.right;
        int i7 = rect3.right;
        if (i5 > (i6 - i7) + 35) {
            i5 = (i6 - i7) + 35;
        }
        int i8 = i5 - i;
        if (i8 >= (i3 + i4) - 35) {
            return i8;
        }
        int i9 = (i3 + i4) - 35;
        popupWindowSpec.mFinalPopupWidth = i5 - i9;
        return i9;
    }

    private int getXInWindowAlignRightAdjust(PopupWindowSpec popupWindowSpec) {
        Rect rect = popupWindowSpec.mAnchorViewBounds;
        Rect rect2 = popupWindowSpec.mDecorViewBounds;
        Rect rect3 = popupWindowSpec.mSafeInsets;
        int i = popupWindowSpec.mFinalPopupWidth;
        int i2 = rect.right + 35;
        int i3 = rect2.right;
        int i4 = rect3.right;
        if (i2 > (i3 - i4) + 35) {
            i2 = (i3 - i4) + 35;
        }
        int i5 = i2 - i;
        int i6 = rect2.left;
        int i7 = rect3.left;
        if (i5 < (i6 + i7) - 35) {
            i5 = (i6 + i7) - 35;
        }
        if (i5 + i > (i3 - i4) + 35) {
            i = ((i3 - i4) + 35) - i5;
        }
        popupWindowSpec.mFinalPopupWidth = i;
        return i5;
    }

    @Override // miuix.popupwidget.internal.strategy.PopupWindowStrategy, miuix.popupwidget.internal.strategy.IPopupWindowStrategy
    public int getYInWindow(PopupWindowSpec popupWindowSpec) {
        int i = popupWindowSpec.mAnchorViewBounds.top;
        int i2 = i - popupWindowSpec.mFinalPopupHeight;
        int i3 = this.mOffsetYPx;
        int i4 = i2 - i3;
        int i5 = popupWindowSpec.mDecorViewBounds.top + popupWindowSpec.mSafeInsets.top;
        if (i4 >= i5) {
            return i4;
        }
        int i6 = (i - i5) - i3;
        if (i6 > 0) {
            popupWindowSpec.mFinalPopupHeight = i6;
        }
        return i5;
    }
}
