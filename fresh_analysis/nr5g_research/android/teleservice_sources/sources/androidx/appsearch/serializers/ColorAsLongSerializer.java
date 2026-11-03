package androidx.appsearch.serializers;

import android.graphics.Color;

/* JADX INFO: loaded from: classes.dex */
public class ColorAsLongSerializer {
    public long serialize(Color color) {
        return color.toArgb();
    }
}
