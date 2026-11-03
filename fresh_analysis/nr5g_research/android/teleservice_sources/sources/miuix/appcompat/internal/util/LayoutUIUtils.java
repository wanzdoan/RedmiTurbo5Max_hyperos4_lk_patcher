package miuix.appcompat.internal.util;

import android.R;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import miuix.appcompat.R$dimen;

/* JADX INFO: loaded from: classes2.dex */
public abstract class LayoutUIUtils {
    public static void resetSearchModeStubInputTextSize(Resources resources, View view, int i) {
        TextView textView;
        if (view == null || resources == null || (textView = (TextView) view.findViewById(R.id.input)) == null) {
            return;
        }
        float dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.miuix_appcompat_search_edit_text_size);
        float f = resources.getDisplayMetrics().density;
        float f2 = dimensionPixelSize / f;
        float textSize = textView.getTextSize() / f;
        float f3 = i;
        if (textSize <= f3 || f2 <= f3) {
            return;
        }
        textView.setTextSize(1, f3);
    }
}
