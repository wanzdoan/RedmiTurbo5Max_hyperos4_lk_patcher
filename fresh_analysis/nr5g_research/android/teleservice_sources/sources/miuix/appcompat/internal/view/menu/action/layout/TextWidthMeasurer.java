package miuix.appcompat.internal.view.menu.action.layout;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import miuix.appcompat.internal.view.menu.action.ActionMenuItemView;
import miuix.core.util.MiuixUIUtils;

/* JADX INFO: loaded from: classes2.dex */
public class TextWidthMeasurer {
    private final Context context;
    private final Paint paint;

    public TextWidthMeasurer(Context context) {
        this.context = context;
        TextPaint textPaint = new TextPaint();
        this.paint = textPaint;
        textPaint.setAntiAlias(true);
    }

    public float measureMenuItemTextWidth(ActionMenuItemView actionMenuItemView) {
        CharSequence text;
        TextView textViewFindTextView = findTextView(actionMenuItemView);
        if (textViewFindTextView == null || (text = textViewFindTextView.getText()) == null || text.length() == 0) {
            return 0.0f;
        }
        return MiuixUIUtils.px2dp(this.context, textViewFindTextView.getPaint().measureText(text.toString()));
    }

    public float measureViewTextWidth(View view) {
        CharSequence text;
        TextView textViewFindTextView = findTextView(view);
        if (textViewFindTextView == null || (text = textViewFindTextView.getText()) == null || text.length() == 0) {
            return 0.0f;
        }
        return MiuixUIUtils.px2dp(this.context, textViewFindTextView.getPaint().measureText(text.toString()));
    }

    private TextView findTextView(View view) {
        TextView textViewFindTextView;
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof TextView) {
                return (TextView) childAt;
            }
            if ((childAt instanceof ViewGroup) && (textViewFindTextView = findTextView(childAt)) != null) {
                return textViewFindTextView;
            }
        }
        return null;
    }
}
