package miuix.pickerwidget.widget.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import miuix.pickerwidget.R$dimen;

/* JADX INFO: loaded from: classes2.dex */
class CalendarSingleDateContainer extends LinearLayout {
    private int mMaxWidth;

    public CalendarSingleDateContainer(Context context) {
        super(context);
        getMaxWidth(context);
    }

    public CalendarSingleDateContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getMaxWidth(context);
    }

    public CalendarSingleDateContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        getMaxWidth(context);
    }

    public CalendarSingleDateContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        getMaxWidth(context);
    }

    private void getMaxWidth(Context context) {
        this.mMaxWidth = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_calendar_grid_layout_child_container_width);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int measuredHeight = getMeasuredHeight();
        int i3 = this.mMaxWidth;
        if (size > i3) {
            size = i3;
        }
        if (measuredHeight < size) {
            measuredHeight = size;
        }
        setMeasuredDimension(size, measuredHeight);
    }
}
