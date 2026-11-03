package miuix.appcompat.internal.view.menu.action.layout;

import android.content.Context;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public class ThreeItemLayoutStrategy implements LayoutStrategy {
    private final Context mContext;

    public ThreeItemLayoutStrategy(Context context) {
        if (context == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Context cannot be null");
            throw null;
        }
        this.mContext = context;
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public LayoutTier determineLayoutTier(float[] fArr) {
        if (fArr == null || fArr.length != 3) {
            Folme$$ExternalSyntheticBUOutline0.m("textWidths must be an array of length 3");
            return null;
        }
        for (float f : fArr) {
            if (f > 99.0f) {
                return LayoutTier.SEVERE_OVERFLOW;
            }
        }
        for (float f2 : fArr) {
            if (f2 >= 68.0f) {
                return LayoutTier.SLIGHT_OVERFLOW;
            }
        }
        return LayoutTier.NORMAL;
    }

    /* JADX INFO: renamed from: miuix.appcompat.internal.view.menu.action.layout.ThreeItemLayoutStrategy$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miuix$appcompat$internal$view$menu$action$layout$LayoutTier;

        static {
            int[] iArr = new int[LayoutTier.values().length];
            $SwitchMap$miuix$appcompat$internal$view$menu$action$layout$LayoutTier = iArr;
            try {
                iArr[LayoutTier.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miuix$appcompat$internal$view$menu$action$layout$LayoutTier[LayoutTier.SLIGHT_OVERFLOW.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$miuix$appcompat$internal$view$menu$action$layout$LayoutTier[LayoutTier.SEVERE_OVERFLOW.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public int calculateMenuWidth(LayoutTier layoutTier, int i, int i2) {
        if (layoutTier == null) {
            Folme$$ExternalSyntheticBUOutline0.m("LayoutTier cannot be null");
            return 0;
        }
        if (i != 3) {
            Folme$$ExternalSyntheticBUOutline0.m("itemCount must be 3 for ThreeItemLayoutStrategy");
            return 0;
        }
        int i3 = AnonymousClass1.$SwitchMap$miuix$appcompat$internal$view$menu$action$layout$LayoutTier[layoutTier.ordinal()];
        if (i3 != 1) {
            return (i3 == 2 || i3 == 3) ? i2 - (dp2px(24.0f) * 2) : i2;
        }
        return dp2px(252.0f);
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public int[] calculateItemWidths(LayoutTier layoutTier, int i, int i2) {
        if (layoutTier == null) {
            Folme$$ExternalSyntheticBUOutline0.m("LayoutTier cannot be null");
            return null;
        }
        if (i2 != 3) {
            Folme$$ExternalSyntheticBUOutline0.m("itemCount must be 3 for ThreeItemLayoutStrategy");
            return null;
        }
        int[] iArr = new int[i2];
        int iDp2px = dp2px(3.0f);
        int iDp2px2 = dp2px(88.0f);
        int iDp2px3 = dp2px(10.0f);
        int i3 = AnonymousClass1.$SwitchMap$miuix$appcompat$internal$view$menu$action$layout$LayoutTier[layoutTier.ordinal()];
        int i4 = 0;
        if (i3 == 1) {
            while (i4 < i2) {
                iArr[i4] = iDp2px2;
                i4++;
            }
        } else if (i3 == 2 || i3 == 3) {
            int i5 = (i - ((iDp2px * 2) + (iDp2px3 * 4))) / 3;
            while (i4 < i2) {
                iArr[i4] = i5;
                i4++;
            }
        }
        return iArr;
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public int calculateMenuHeight(LayoutTier layoutTier, int i) {
        if (layoutTier != null) {
            return layoutTier == LayoutTier.SEVERE_OVERFLOW ? i + dp2px(20.0f) : i;
        }
        Folme$$ExternalSyntheticBUOutline0.m("LayoutTier cannot be null");
        return 0;
    }

    private int dp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
