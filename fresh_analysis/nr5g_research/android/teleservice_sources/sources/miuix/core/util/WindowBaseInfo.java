package miuix.core.util;

import android.graphics.Point;

/* JADX INFO: loaded from: classes2.dex */
public class WindowBaseInfo {
    public float windowDensity;
    public int windowMode;
    public int windowType;
    public boolean sizeDirty = true;
    public boolean modeDirty = true;
    public Point windowSize = new Point();
    public Point windowSizeDp = new Point();

    public boolean isDirty() {
        return this.sizeDirty || this.modeDirty;
    }
}
