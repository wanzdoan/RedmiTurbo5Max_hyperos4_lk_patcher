package miuix.appcompat.internal.view.menu.action.layout;

/* JADX INFO: loaded from: classes2.dex */
public interface LayoutStrategy {
    int[] calculateItemWidths(LayoutTier layoutTier, int i, int i2);

    int calculateMenuHeight(LayoutTier layoutTier, int i);

    int calculateMenuWidth(LayoutTier layoutTier, int i, int i2);

    LayoutTier determineLayoutTier(float[] fArr);
}
