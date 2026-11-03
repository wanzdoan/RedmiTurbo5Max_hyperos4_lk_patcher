package miuix.appcompat.internal.view.menu.action.layout;

import android.content.Context;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public class MultiItemLayoutStrategy implements LayoutStrategy {
    private final Context mContext;

    public MultiItemLayoutStrategy(Context context) {
        if (context == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Context cannot be null");
            throw null;
        }
        this.mContext = context;
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public LayoutTier determineLayoutTier(float[] fArr) {
        if (fArr == null) {
            Folme$$ExternalSyntheticBUOutline0.m("textWidths cannot be null");
            return null;
        }
        if (fArr.length < 4 || fArr.length > 5) {
            Folme$$ExternalSyntheticBUOutline0.m("textWidths must have 4 or 5 elements for MultiItemLayoutStrategy");
            return null;
        }
        return LayoutTier.NORMAL;
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public int calculateMenuWidth(LayoutTier layoutTier, int i, int i2) {
        if (layoutTier == null) {
            Folme$$ExternalSyntheticBUOutline0.m("LayoutTier cannot be null");
            return 0;
        }
        if (i < 4 || i > 5) {
            Folme$$ExternalSyntheticBUOutline0.m("itemCount must be 4 or 5 for MultiItemLayoutStrategy");
            return 0;
        }
        return i2 - (dp2px(24.0f) * 2);
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public int[] calculateItemWidths(LayoutTier layoutTier, int i, int i2) {
        if (layoutTier == null) {
            Folme$$ExternalSyntheticBUOutline0.m("LayoutTier cannot be null");
            return null;
        }
        if (i2 < 4 || i2 > 5) {
            Folme$$ExternalSyntheticBUOutline0.m("itemCount must be 4 or 5 for MultiItemLayoutStrategy");
            return null;
        }
        int[] iArr = new int[i2];
        int iDp2px = (i - ((dp2px(3.0f) * 2) + ((i2 + 1) * dp2px(10.0f)))) / i2;
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = iDp2px;
        }
        return iArr;
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public int calculateMenuHeight(LayoutTier layoutTier, int i) {
        if (layoutTier != null) {
            return i;
        }
        Folme$$ExternalSyntheticBUOutline0.m("LayoutTier cannot be null");
        return 0;
    }

    public boolean needHeightIncrease(float[] fArr, int[] iArr) {
        if (fArr == null || iArr == null || fArr.length != iArr.length) {
            return false;
        }
        for (int i = 0; i < fArr.length; i++) {
            if (dp2px(fArr[i]) > iArr[i]) {
                return true;
            }
        }
        return false;
    }

    protected int dp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
