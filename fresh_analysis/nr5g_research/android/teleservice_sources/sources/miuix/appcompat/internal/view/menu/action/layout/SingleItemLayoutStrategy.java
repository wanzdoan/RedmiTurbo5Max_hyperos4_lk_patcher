package miuix.appcompat.internal.view.menu.action.layout;

import android.content.Context;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public class SingleItemLayoutStrategy implements LayoutStrategy {
    private final Context mContext;
    private float mTextWidthDp;

    public SingleItemLayoutStrategy(Context context) {
        this.mContext = context;
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public LayoutTier determineLayoutTier(float[] fArr) {
        if (fArr == null || fArr.length != 1) {
            Folme$$ExternalSyntheticBUOutline0.m("textWidths must be an array of length 1");
            return null;
        }
        float f = fArr[0];
        this.mTextWidthDp = f;
        if (f > 318.0f) {
            return LayoutTier.SEVERE_OVERFLOW;
        }
        if (f >= 74.0f) {
            return LayoutTier.SLIGHT_OVERFLOW;
        }
        return LayoutTier.NORMAL;
    }

    /* JADX INFO: renamed from: miuix.appcompat.internal.view.menu.action.layout.SingleItemLayoutStrategy$1, reason: invalid class name */
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
        if (i != 1) {
            Folme$$ExternalSyntheticBUOutline0.m("itemCount must be 1 for SingleItemLayoutStrategy");
            return 0;
        }
        int i3 = AnonymousClass1.$SwitchMap$miuix$appcompat$internal$view$menu$action$layout$LayoutTier[layoutTier.ordinal()];
        if (i3 == 1) {
            return dp2px(114.0f);
        }
        if (i3 != 2) {
            return i3 != 3 ? i2 : Math.max(0, i2 - (dp2px(24.0f) * 2));
        }
        return Math.min(dp2px(this.mTextWidthDp + 2.0f + 6.0f + 20.0f), Math.max(0, i2 - (dp2px(24.0f) * 2)));
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public int[] calculateItemWidths(LayoutTier layoutTier, int i, int i2) {
        if (layoutTier == null) {
            Folme$$ExternalSyntheticBUOutline0.m("LayoutTier cannot be null");
            return null;
        }
        if (i2 != 1) {
            Folme$$ExternalSyntheticBUOutline0.m("itemCount must be 1 for SingleItemLayoutStrategy");
            return null;
        }
        int[] iArr = new int[i2];
        int iDp2px = dp2px(3.0f);
        int iDp2px2 = dp2px(10.0f);
        int i3 = AnonymousClass1.$SwitchMap$miuix$appcompat$internal$view$menu$action$layout$LayoutTier[layoutTier.ordinal()];
        if (i3 == 1) {
            iArr[0] = dp2px(88.0f);
            return iArr;
        }
        if (i3 != 2 && i3 != 3) {
            return iArr;
        }
        iArr[0] = (i - (iDp2px * 2)) - (iDp2px2 * 2);
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

    private int dp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
