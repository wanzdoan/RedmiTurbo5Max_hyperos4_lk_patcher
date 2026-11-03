package miuix.appcompat.internal.view.menu.action.layout;

import android.content.Context;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public class WideMultiItemLayoutStrategy extends MultiItemLayoutStrategy {
    public WideMultiItemLayoutStrategy(Context context) {
        super(context);
    }

    @Override // miuix.appcompat.internal.view.menu.action.layout.MultiItemLayoutStrategy, miuix.appcompat.internal.view.menu.action.layout.LayoutStrategy
    public int calculateMenuWidth(LayoutTier layoutTier, int i, int i2) {
        if (layoutTier == null) {
            Folme$$ExternalSyntheticBUOutline0.m("LayoutTier cannot be null");
            return 0;
        }
        if (i < 4 || i > 5) {
            Folme$$ExternalSyntheticBUOutline0.m("itemCount must be 4 or 5 for WideMultiItemLayoutStrategy");
            return 0;
        }
        return dp2px(344.0f);
    }
}
