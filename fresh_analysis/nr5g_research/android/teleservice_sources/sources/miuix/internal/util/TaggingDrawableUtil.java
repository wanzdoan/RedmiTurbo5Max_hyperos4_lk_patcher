package miuix.internal.util;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import miuix.appcompat.R$dimen;
import miuix.internal.graphics.drawable.TaggingDrawable;

/* JADX INFO: loaded from: classes2.dex */
public abstract class TaggingDrawableUtil {
    private static int mPaddingSmall = -1;
    private static final int[] STATES_TAGS = {R.attr.state_single, R.attr.state_first, R.attr.state_middle, R.attr.state_last};
    private static final int[] STATE_SET_SINGLE = {R.attr.state_single};
    private static final int[] STATE_SET_FIRST = {R.attr.state_first};
    private static final int[] STATE_SET_MIDDLE = {R.attr.state_middle};
    private static final int[] STATE_SET_LAST = {R.attr.state_last};

    public static void updateItemBackground(View view, int i, int i2) {
        updateBackgroundState(view, i, i2);
        updateItemPadding(view, i, i2);
    }

    public static void updateBackgroundState(View view, int i, int i2) {
        int[] iArr;
        if (view == null || i2 == 0) {
            return;
        }
        Drawable background = view.getBackground();
        if ((background instanceof StateListDrawable) && TaggingDrawable.containsTagState((StateListDrawable) background, STATES_TAGS)) {
            TaggingDrawable taggingDrawable = new TaggingDrawable(background);
            view.setBackground(taggingDrawable);
            background = taggingDrawable;
        }
        if (background instanceof TaggingDrawable) {
            TaggingDrawable taggingDrawable2 = (TaggingDrawable) background;
            if (i2 == 1) {
                iArr = STATE_SET_SINGLE;
            } else if (i == 0) {
                iArr = STATE_SET_FIRST;
            } else if (i == i2 - 1) {
                iArr = STATE_SET_LAST;
            } else {
                iArr = STATE_SET_MIDDLE;
            }
            taggingDrawable2.setTaggingState(iArr);
        }
    }

    public static void updateItemPadding(View view, int i, int i2) {
        if (view == null || i2 == 0) {
            return;
        }
        Context context = view.getContext();
        int paddingStart = view.getPaddingStart();
        view.getPaddingTop();
        int paddingEnd = view.getPaddingEnd();
        view.getPaddingBottom();
        int dimen = getDimen(context, R$dimen.miuix_appcompat_drop_down_item_min_height);
        if (mPaddingSmall == -1) {
            mPaddingSmall = getDimen(context, R$dimen.miuix_appcompat_drop_down_menu_padding_small);
        }
        int i3 = mPaddingSmall;
        view.setMinimumHeight(dimen);
        view.setPaddingRelative(paddingStart, i3, paddingEnd, i3);
    }

    private static int getDimen(Context context, int i) {
        return context.getResources().getDimensionPixelSize(i);
    }
}
