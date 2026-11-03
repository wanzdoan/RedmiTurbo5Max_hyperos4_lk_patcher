package miuix.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DensityChangedHelper {
    public static void updateTextSizeDefaultUnit(TextView textView, float f) {
        textView.setTextSize(textView.getTextSizeUnit(), f);
    }

    public static void updateTextSizeSpUnit(TextView textView, float f) {
        textView.setTextSize(2, f);
    }

    public static void updateTextSizeDpUnit(TextView textView, float f) {
        textView.setTextSize(1, f);
    }

    public static void updateViewPadding(View view, float f) {
        view.setPadding((int) (view.getPaddingLeft() * f), (int) (view.getPaddingTop() * f), (int) (view.getPaddingRight() * f), (int) (view.getPaddingBottom() * f));
    }

    public static void updateViewSize(View view, float f) {
        boolean z;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int i = layoutParams.width;
        boolean z2 = true;
        if (i > 0) {
            layoutParams.width = (int) (i * f);
            z = true;
        } else {
            z = false;
        }
        int i2 = layoutParams.height;
        if (i2 > 0) {
            layoutParams.height = (int) (i2 * f);
        } else {
            z2 = z;
        }
        if (z2) {
            view.setLayoutParams(layoutParams);
        }
    }

    public static void updateViewMargin(View view, float f) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.leftMargin = (int) (marginLayoutParams.leftMargin * f);
            marginLayoutParams.topMargin = (int) (marginLayoutParams.topMargin * f);
            marginLayoutParams.rightMargin = (int) (marginLayoutParams.rightMargin * f);
            marginLayoutParams.bottomMargin = (int) (marginLayoutParams.bottomMargin * f);
            view.setLayoutParams(marginLayoutParams);
        }
    }
}
