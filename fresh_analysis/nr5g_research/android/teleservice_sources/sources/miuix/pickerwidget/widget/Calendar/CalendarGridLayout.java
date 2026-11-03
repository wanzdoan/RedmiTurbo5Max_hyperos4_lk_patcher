package miuix.pickerwidget.widget.Calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import miuix.core.util.MiuixUIUtils;
import miuix.pickerwidget.R$dimen;
import miuix.pickerwidget.R$id;
import miuix.pickerwidget.R$layout;
import miuix.pickerwidget.R$style;
import miuix.pickerwidget.R$styleable;
import miuix.pickerwidget.date.Calendar;
import miuix.view.DensityChangedHelper;

/* JADX INFO: loaded from: classes2.dex */
class CalendarGridLayout extends GridLayout {
    private Calendar mCalendar;
    private int mChildPaddingIfNotLunarMode;
    private int mColumnCount;
    private int mDay;
    private int mFirstWeekDayInMonth;
    private boolean mHasBound;
    private CalendarDatePickerHelper mHelper;
    private int mLargeRowGap;
    private int mLastBoundDay;
    private int mLastBoundHighlightColor;
    private boolean mLastBoundLunarMode;
    private int mLastBoundMonth;
    private int mLastBoundYear;
    private boolean mLunarMode;
    private int mMaxRowCount;
    private int mMediumRowGap;
    private int mMonth;
    private int mRowGap;
    private View mSelectedChild;
    private int mSmallRowGap;
    private boolean mSpecialHoliday;
    private int mYear;

    public CalendarGridLayout(Context context) {
        this(context, null);
    }

    public CalendarGridLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CalendarGridLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, R$style.Widget_CalendarGridLayout);
    }

    public CalendarGridLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mRowGap = 0;
        this.mHasBound = false;
        this.mLastBoundHighlightColor = 0;
        init(context, attributeSet, i, i2);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CalendarGridLayout, i, i2);
        this.mLunarMode = typedArrayObtainStyledAttributes.getBoolean(R$styleable.CalendarGridLayout_lunarMode, false);
        typedArrayObtainStyledAttributes.recycle();
        this.mMaxRowCount = getRowCount();
        this.mColumnCount = getColumnCount();
        this.mHelper = new CalendarDatePickerHelper(context);
        this.mCalendar = new Calendar();
        this.mSmallRowGap = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_calendar_grid_layout_row_gap_small);
        this.mMediumRowGap = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_calendar_grid_layout_row_gap_medium);
        this.mLargeRowGap = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_calendar_grid_layout_row_gap_large);
        this.mChildPaddingIfNotLunarMode = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_calendar_grid_layout_child_padding);
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(context);
        for (int i3 = 0; i3 < this.mMaxRowCount; i3++) {
            for (int i4 = 0; i4 < this.mColumnCount; i4++) {
                layoutInflaterFrom.inflate(R$layout.miuix_appcompat_calendar_grid_child_layout, (ViewGroup) this, true);
            }
        }
    }

    private void selectChild(int i, int i2) {
        selectChild(getChildAt(i, i2));
    }

    void selectChild(View view) {
        if (view == null || view.isSelected()) {
            return;
        }
        View view2 = this.mSelectedChild;
        if (view2 != null) {
            view2.setSelected(false);
        }
        view.setSelected(true);
        this.mSelectedChild = view;
    }

    @Override // android.widget.GridLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(View.MeasureSpec.getSize(i), (this.mMaxRowCount * getChildAt(0).getMeasuredHeight()) + ((this.mMaxRowCount - 1) * this.mSmallRowGap));
    }

    private View getChildAt(int i, int i2) {
        return getChildAt((i * this.mColumnCount) + i2);
    }

    private void setDate(int i, int i2, int i3, int i4) {
        Context context = getContext();
        View childAt = getChildAt(i, i2);
        TextView textView = (TextView) childAt.findViewById(R$id.calendar_date_picker_date);
        TextView textView2 = (TextView) childAt.findViewById(R$id.calendar_date_picker_lunar_message);
        if (i4 == 0) {
            textView.setText((CharSequence) null);
            childAt.setEnabled(false);
            childAt.setImportantForAccessibility(2);
        } else {
            textView.setText(String.valueOf(i4));
            childAt.setEnabled(true);
            childAt.setImportantForAccessibility(1);
        }
        int fontLevel = MiuixUIUtils.getFontLevel(context);
        DensityChangedHelper.updateTextSizeDpUnit(textView, fontLevel == 2 ? 20 : 18);
        long timeMillis = getTimeMillis(this.mYear, i3, i4);
        String specialHoliday = getSpecialHoliday(i, i2, this.mFirstWeekDayInMonth, this.mCalendar);
        boolean zIsEmpty = TextUtils.isEmpty(specialHoliday);
        this.mSpecialHoliday = !zIsEmpty;
        if (zIsEmpty && i3 >= 0 && i4 > 0) {
            specialHoliday = getLunarMessage(this.mYear, i3, i4);
        }
        textView2.setText(specialHoliday);
        String dayContentDescription = getDayContentDescription(timeMillis);
        if (!TextUtils.isEmpty(specialHoliday) && this.mLunarMode) {
            if (!isHoliday()) {
                specialHoliday = getChineseDate(specialHoliday);
            }
            dayContentDescription = dayContentDescription + specialHoliday;
        }
        childAt.setContentDescription(dayContentDescription);
        DensityChangedHelper.updateTextSizeDpUnit(textView2, fontLevel == 2 ? 12 : 10);
    }

    private void refresh(int i, int i2, int i3) {
        this.mYear = i;
        this.mMonth = i2;
        this.mFirstWeekDayInMonth = getFirstWeekDayInMonth(i, i2);
        int iDaysInMonth = this.mCalendar.daysInMonth(i, i2);
        int iMin = Math.min(i3, iDaysInMonth);
        this.mDay = iMin;
        int i4 = 1;
        for (int i5 = 0; i5 < this.mColumnCount; i5++) {
            if (i5 < this.mFirstWeekDayInMonth) {
                setDate(0, i5, 0, 0);
            } else {
                setDate(0, i5, i2, i4);
                int i6 = i4 + 1;
                if (i4 == iMin) {
                    selectChild(0, i5);
                }
                i4 = i6;
            }
            notifyChildLunarModeMaybeChanged(0, i5);
        }
        int i7 = 1;
        for (int i8 = 1; i8 < this.mMaxRowCount; i8++) {
            boolean z = false;
            for (int i9 = 0; i9 < this.mColumnCount; i9++) {
                if (i4 > iDaysInMonth) {
                    setDate(i8, i9, 0, 0);
                } else {
                    setDate(i8, i9, i2, i4);
                    int i10 = i4 + 1;
                    if (i4 == iMin) {
                        selectChild(i8, i9);
                    }
                    i4 = i10;
                    z = true;
                }
                notifyChildLunarModeMaybeChanged(i8, i9);
            }
            if (z) {
                i7++;
            }
        }
        adjustRowGap(i7);
    }

    void updateCurrentDate(int i, int i2, int i3) {
        this.mYear = i;
        this.mMonth = i2;
        this.mDay = i3;
    }

    private void adjustRowGap(int i) {
        int i2;
        if (i == 4) {
            i2 = this.mLargeRowGap;
        } else if (i == 5) {
            i2 = this.mMediumRowGap;
        } else {
            i2 = this.mSmallRowGap;
        }
        if (this.mRowGap == i2) {
            return;
        }
        int i3 = 0;
        while (i3 < this.mMaxRowCount) {
            for (int i4 = 0; i4 < this.mColumnCount; i4++) {
                View childAt = getChildAt(i3, i4);
                if (i3 < i) {
                    childAt.setVisibility(0);
                    if (i4 == 0) {
                        setChildMarginBottom(childAt, i3 == i + (-1) ? 0 : i2);
                    }
                } else {
                    childAt.setVisibility(8);
                }
            }
            i3++;
        }
        this.mRowGap = i2;
    }

    private void applyHighlightColorInternal(int i) {
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            Drawable selectedStateDrawable = getSelectedStateDrawable(getChildAt(i2).findViewById(R$id.calendar_date_picker_single_date_container));
            if (selectedStateDrawable instanceof GradientDrawable) {
                ((GradientDrawable) selectedStateDrawable).setColor(i);
            }
        }
    }

    void bind(int i, int i2, int i3, boolean z, int i4) {
        boolean z2 = this.mHasBound;
        boolean z3 = (z2 && this.mLastBoundHighlightColor == i4) ? false : true;
        boolean z4 = (z2 && this.mLastBoundYear == i && this.mLastBoundMonth == i2 && this.mLastBoundDay == i3) ? false : true;
        boolean z5 = (z2 && this.mLastBoundLunarMode == z) ? false : true;
        if (z3 || z4 || z5) {
            if (z3 && i4 != 0) {
                applyHighlightColorInternal(i4);
            }
            if (z4 || z5) {
                this.mLunarMode = z;
                this.mYear = i;
                this.mMonth = i2;
                this.mDay = i3;
                refresh(i, i2, i3);
            }
            this.mHasBound = true;
            this.mLastBoundYear = i;
            this.mLastBoundMonth = i2;
            this.mLastBoundDay = i3;
            this.mLastBoundLunarMode = z;
            this.mLastBoundHighlightColor = i4;
        }
    }

    private String getDayContentDescription(long j) {
        return AccessibilityUtils.getYearMonthDayOfWeekDay(j);
    }

    private String getSpecialHoliday(int i, int i2, int i3, Calendar calendar) {
        return SpecialHolidayParser.parseSpecialHoliday(i, i2, i3, calendar);
    }

    private long getTimeMillis(int i, int i2, int i3) {
        return this.mCalendar.set(1, i).set(5, i2).set(9, i3).getTimeInMillis();
    }

    private String getChineseDate(String str) {
        return String.format("农历%d月%s", Integer.valueOf(this.mCalendar.get(6) + 1), str);
    }

    private boolean isHoliday() {
        return this.mHelper.isLunarHoliday() || this.mHelper.isSolarHoliday() || this.mSpecialHoliday;
    }

    private void notifyChildLunarModeMaybeChanged(int i, int i2) {
        View childAt = getChildAt(i, i2);
        int i3 = this.mLunarMode ? 0 : this.mChildPaddingIfNotLunarMode;
        View viewFindViewById = childAt.findViewById(R$id.calendar_date_picker_lunar_message);
        View viewFindViewById2 = childAt.findViewById(R$id.calendar_date_picker_child_container);
        if (viewFindViewById != null) {
            viewFindViewById.setVisibility(this.mLunarMode ? 0 : 8);
        }
        if (viewFindViewById2 != null) {
            viewFindViewById2.setPadding(i3, 0, i3, 0);
        }
    }

    private void setChildMarginBottom(View view, int i) {
        this.mHelper.setChildMarginBottom(view, i);
    }

    private int getFirstWeekDayInMonth(int i, int i2) {
        return this.mHelper.getFirstWeekDayInMonth(i, i2);
    }

    String getLunarMessage(int i, int i2, int i3) {
        return this.mHelper.getLunarMessage(i, i2, i3);
    }

    private Drawable getSelectedStateDrawable(View view) {
        return this.mHelper.getSelectedStateDrawable(view);
    }
}
