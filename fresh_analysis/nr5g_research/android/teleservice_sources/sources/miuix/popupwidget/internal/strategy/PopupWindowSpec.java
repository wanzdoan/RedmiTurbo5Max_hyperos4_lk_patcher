package miuix.popupwidget.internal.strategy;

import android.graphics.Rect;
import miuix.reflect.Reflects$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public class PopupWindowSpec implements Cloneable {
    public int layoutDirection;
    public int mContentHeight;
    public int mContentWidth;
    public int mFinalPopupHeight;
    public int mFinalPopupWidth;
    public int[][] mItemViewBounds;
    public int mMaxHeight;
    public int mMaxWidth;
    public int mMinHeight;
    public int mMinWidth;
    public boolean mOffsetXSet;
    public boolean mOffsetYSet;
    public int mPaddingBottom;
    public int mPaddingLeft;
    public int mPaddingRight;
    public int mPaddingTop;
    public Rect mSafeInsets;
    public int mUserOffsetX;
    public int mUserOffsetY;
    public int mGravity = 8388661;
    public Rect mContentViewBounds = new Rect();
    public Rect mDecorViewBounds = new Rect();
    public Rect mAnchorViewBounds = new Rect();

    public String toString() {
        return "PopupWindowSpec{mMaxWidth=" + this.mMaxWidth + ", mMinWidth=" + this.mMinWidth + ", mMaxHeight=" + this.mMaxHeight + ", mMinHeight=" + this.mMinHeight + ", mContentWidth=" + this.mContentWidth + ", mContentHeight=" + this.mContentHeight + ", mFinalPopupWidth=" + this.mFinalPopupWidth + ", mFinalPopupHeight=" + this.mFinalPopupHeight + ", mGravity=" + this.mGravity + ", mUserOffsetX=" + this.mUserOffsetX + ", mUserOffsetY=" + this.mUserOffsetY + ", mOffsetXSet=" + this.mOffsetXSet + ", mOffsetYSet=" + this.mOffsetYSet + ", mItemViewBounds=" + convertItemViewBounds(this.mItemViewBounds) + ", mDecorViewBounds=" + this.mDecorViewBounds.flattenToString() + ", mAnchorViewBounds=" + this.mAnchorViewBounds.flattenToString() + ", mSafeInsets=" + this.mSafeInsets.flattenToString() + ", layoutDirection=" + this.layoutDirection + ", mPaddingTop=" + this.mPaddingTop + ", mPaddingBottom=" + this.mPaddingBottom + ", mPaddingLeft=" + this.mPaddingLeft + ", mPaddingRight=" + this.mPaddingRight + '}';
    }

    public PopupWindowSpec clone() {
        try {
            PopupWindowSpec popupWindowSpec = (PopupWindowSpec) super.clone();
            popupWindowSpec.mMaxWidth = this.mMaxWidth;
            popupWindowSpec.mMinWidth = this.mMinWidth;
            popupWindowSpec.mMaxHeight = this.mMaxHeight;
            popupWindowSpec.mMinHeight = this.mMinHeight;
            popupWindowSpec.mContentWidth = this.mContentWidth;
            popupWindowSpec.mContentHeight = this.mContentHeight;
            popupWindowSpec.mFinalPopupWidth = this.mFinalPopupWidth;
            popupWindowSpec.mFinalPopupHeight = this.mFinalPopupHeight;
            popupWindowSpec.mGravity = this.mGravity;
            popupWindowSpec.mUserOffsetX = this.mUserOffsetX;
            popupWindowSpec.mUserOffsetY = this.mUserOffsetY;
            popupWindowSpec.mOffsetXSet = this.mOffsetXSet;
            popupWindowSpec.mOffsetYSet = this.mOffsetYSet;
            popupWindowSpec.mItemViewBounds = this.mItemViewBounds;
            Rect rect = this.mContentViewBounds;
            popupWindowSpec.mContentViewBounds = new Rect(rect.left, rect.top, rect.right, rect.bottom);
            Rect rect2 = this.mDecorViewBounds;
            popupWindowSpec.mDecorViewBounds = new Rect(rect2.left, rect2.top, rect2.right, rect2.bottom);
            Rect rect3 = this.mAnchorViewBounds;
            popupWindowSpec.mAnchorViewBounds = new Rect(rect3.left, rect3.top, rect3.right, rect3.bottom);
            popupWindowSpec.mSafeInsets = this.mSafeInsets;
            popupWindowSpec.layoutDirection = this.layoutDirection;
            popupWindowSpec.mPaddingTop = this.mPaddingTop;
            popupWindowSpec.mPaddingBottom = this.mPaddingBottom;
            popupWindowSpec.mPaddingLeft = this.mPaddingLeft;
            popupWindowSpec.mPaddingRight = this.mPaddingRight;
            return popupWindowSpec;
        } catch (CloneNotSupportedException e) {
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        }
    }

    private static String convertItemViewBounds(int[][] iArr) {
        if (iArr != null) {
            StringBuilder sb = new StringBuilder();
            for (int[] iArr2 : iArr) {
                sb.append(String.format("{%d, %d},", Integer.valueOf(iArr2[0]), Integer.valueOf(iArr2[1])));
            }
            return sb.toString();
        }
        return "null";
    }
}
