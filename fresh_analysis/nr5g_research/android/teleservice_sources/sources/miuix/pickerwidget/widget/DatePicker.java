package miuix.pickerwidget.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import miuix.pickerwidget.R$attr;
import miuix.pickerwidget.R$id;
import miuix.pickerwidget.R$layout;
import miuix.pickerwidget.R$string;
import miuix.pickerwidget.R$style;
import miuix.pickerwidget.R$styleable;
import miuix.pickerwidget.date.Calendar;
import miuix.pickerwidget.date.CalendarFormatSymbols;
import miuix.pickerwidget.date.DateUtils;

/* JADX INFO: loaded from: classes2.dex */
public class DatePicker extends FrameLayout {
    private static final String LOG_TAG = DatePicker.class.getSimpleName();
    private static String[] sChineseDays;
    private static String sChineseLeapMonthMark;
    private static String[] sChineseLeapYearMonths;
    private static String[] sChineseMonths;
    private int mColumnSpacing;
    private Calendar mCurrentDate;
    private Locale mCurrentLocale;
    private final DateFormat mDateFormat;
    private char[] mDateFormatOrder;
    private final NumberPicker mDaySpinner;
    private boolean mIsEnabled;
    private boolean mIsLunarMode;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private final NumberPicker mMonthSpinner;
    private int mNumberOfMonths;
    private OnDateChangedListener mOnDateChangedListener;
    private String[] mShortMonths;
    private final LinearLayout mSpinners;
    private Calendar mTempDate;
    private final NumberPicker mYearSpinner;

    public interface OnDateChangedListener {
        void onDateChanged(DatePicker datePicker, int i, int i2, int i3, boolean z);
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.datePickerStyle);
    }

    public DatePicker(Context context, AttributeSet attributeSet, int i) {
        int i2;
        String str;
        boolean z;
        super(context, attributeSet, i);
        this.mDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        this.mIsEnabled = true;
        this.mIsLunarMode = false;
        initChineseDaysIfNeeded();
        this.mTempDate = new Calendar();
        this.mMinDate = new Calendar();
        this.mMaxDate = new Calendar();
        this.mCurrentDate = new Calendar();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DatePicker, i, R$style.Widget_DatePicker);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(R$styleable.DatePicker_spinnersShown, true);
        int i3 = typedArrayObtainStyledAttributes.getInt(R$styleable.DatePicker_startYear, 1900);
        int i4 = typedArrayObtainStyledAttributes.getInt(R$styleable.DatePicker_endYear, 2100);
        int i5 = typedArrayObtainStyledAttributes.getInt(R$styleable.DatePicker_selectorWheelAppearance, 0);
        String string = typedArrayObtainStyledAttributes.getString(R$styleable.DatePicker_minDate);
        String string2 = typedArrayObtainStyledAttributes.getString(R$styleable.DatePicker_maxDate);
        int i6 = R$layout.miuix_appcompat_date_picker;
        this.mIsLunarMode = typedArrayObtainStyledAttributes.getBoolean(R$styleable.DatePicker_lunarCalendar, false);
        this.mColumnSpacing = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.DatePicker_columnSpacing, 0);
        boolean z3 = typedArrayObtainStyledAttributes.getBoolean(R$styleable.DatePicker_showYear, true);
        boolean z4 = typedArrayObtainStyledAttributes.getBoolean(R$styleable.DatePicker_showMonth, true);
        boolean z5 = typedArrayObtainStyledAttributes.getBoolean(R$styleable.DatePicker_showDay, true);
        typedArrayObtainStyledAttributes.recycle();
        setCurrentLocale(Locale.getDefault());
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(i6, (ViewGroup) this, true);
        NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() { // from class: miuix.pickerwidget.widget.DatePicker.1
            @Override // miuix.pickerwidget.widget.NumberPicker.OnValueChangeListener
            public void onValueChange(NumberPicker numberPicker, int i7, int i8) {
                DatePicker.this.mTempDate.setSafeTimeInMillis(DatePicker.this.mCurrentDate.getTimeInMillis(), DatePicker.this.mIsLunarMode);
                NumberPicker numberPicker2 = DatePicker.this.mDaySpinner;
                DatePicker datePicker = DatePicker.this;
                if (numberPicker == numberPicker2) {
                    datePicker.mTempDate.add(DatePicker.this.mIsLunarMode ? 10 : 9, i8 - i7);
                } else {
                    NumberPicker numberPicker3 = datePicker.mMonthSpinner;
                    DatePicker datePicker2 = DatePicker.this;
                    if (numberPicker == numberPicker3) {
                        datePicker2.mTempDate.add(DatePicker.this.mIsLunarMode ? 6 : 5, i8 - i7);
                    } else if (numberPicker == datePicker2.mYearSpinner) {
                        DatePicker.this.mTempDate.set(DatePicker.this.mIsLunarMode ? 2 : 1, i8);
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                DatePicker datePicker3 = DatePicker.this;
                datePicker3.setDate(datePicker3.mTempDate.get(1), DatePicker.this.mTempDate.get(5), DatePicker.this.mTempDate.get(9));
                if (numberPicker == DatePicker.this.mYearSpinner) {
                    DatePicker.this.resetMonthsDisplayedValues();
                }
                DatePicker.this.updateSpinners();
                DatePicker.this.notifyDateChanged();
            }
        };
        this.mSpinners = (LinearLayout) findViewById(R$id.pickers);
        NumberPicker numberPicker = (NumberPicker) findViewById(R$id.day);
        this.mDaySpinner = numberPicker;
        numberPicker.setOnLongPressUpdateInterval(100L);
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        if (!z5) {
            numberPicker.setVisibility(8);
        }
        NumberPicker numberPicker2 = (NumberPicker) findViewById(R$id.month);
        this.mMonthSpinner = numberPicker2;
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(this.mNumberOfMonths - 1);
        numberPicker2.setDisplayedValues(this.mShortMonths);
        numberPicker2.setOnLongPressUpdateInterval(200L);
        numberPicker2.setOnValueChangedListener(onValueChangeListener);
        if (!z4) {
            numberPicker2.setVisibility(8);
        }
        NumberPicker numberPicker3 = (NumberPicker) findViewById(R$id.year);
        this.mYearSpinner = numberPicker3;
        numberPicker3.setOnLongPressUpdateInterval(100L);
        numberPicker3.setOnValueChangedListener(onValueChangeListener);
        if (!z3) {
            numberPicker3.setVisibility(8);
        }
        updateFormatters();
        if (!z2) {
            i2 = 1;
            setSpinnersShown(true);
        } else {
            i2 = 1;
            setSpinnersShown(z2);
        }
        this.mCurrentDate.setSafeTimeInMillis(System.currentTimeMillis(), this.mIsLunarMode);
        init(this.mCurrentDate.get(i2), this.mCurrentDate.get(5), this.mCurrentDate.get(9), null);
        this.mTempDate.setSafeTimeInMillis(0L, this.mIsLunarMode);
        boolean zIsEmpty = TextUtils.isEmpty(string);
        Calendar calendar = this.mTempDate;
        if (!zIsEmpty) {
            if (parseDate(string, calendar)) {
                str = string2;
            } else {
                str = string2;
                this.mTempDate.set(i3, 0, 1, 12, 0, 0, 0);
            }
        } else {
            str = string2;
            if (!parseDate("1/31/1900", calendar)) {
                this.mTempDate.set(i3, 0, 1, 12, 0, 0, 0);
            }
        }
        setMinDate(this.mTempDate.getTimeInMillis());
        this.mTempDate.setSafeTimeInMillis(0L, this.mIsLunarMode);
        boolean zIsEmpty2 = TextUtils.isEmpty(str);
        Calendar calendar2 = this.mTempDate;
        if (!zIsEmpty2) {
            if (!parseDate(str, calendar2)) {
                this.mTempDate.set(i4, 11, 31, 12, 0, 0, 0);
            }
        } else {
            calendar2.set(i4, 11, 31, 12, 0, 0, 0);
        }
        setMaxDate(this.mTempDate.getTimeInMillis());
        reorderSpinners();
        if (getImportantForAccessibility() == 0) {
            z = true;
            setImportantForAccessibility(1);
        } else {
            z = true;
        }
        if (i5 != 0) {
            setFiveLinesAppearance(z);
        }
    }

    private void initChineseDaysIfNeeded() {
        String[] strArr;
        if (sChineseDays == null) {
            sChineseDays = CalendarFormatSymbols.getOrCreate(getContext()).getChineseDays();
        }
        if (sChineseMonths == null) {
            sChineseMonths = CalendarFormatSymbols.getOrCreate(getContext()).getChineseMonths();
            Resources resources = getContext().getResources();
            int i = 0;
            while (true) {
                strArr = sChineseMonths;
                if (i >= strArr.length) {
                    break;
                }
                StringBuilder sb = new StringBuilder();
                String[] strArr2 = sChineseMonths;
                sb.append(strArr2[i]);
                sb.append(resources.getString(R$string.chinese_month));
                strArr2[i] = sb.toString();
                i++;
            }
            sChineseLeapYearMonths = new String[strArr.length + 1];
        }
        if (sChineseLeapMonthMark == null) {
            sChineseLeapMonthMark = CalendarFormatSymbols.getOrCreate(getContext()).getChineseLeapMonths()[1];
        }
    }

    private void updateFormatters() {
        NumberPicker numberPicker = this.mDaySpinner;
        if (numberPicker == null || this.mYearSpinner == null) {
            return;
        }
        numberPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        this.mYearSpinner.setFormatter(new NumberPicker.NumberFormatter());
    }

    public long getMinDate() {
        return this.mMinDate.getTimeInMillis();
    }

    public void setMinDate(long j) {
        this.mTempDate.setSafeTimeInMillis(j, this.mIsLunarMode);
        if (this.mTempDate.get(1) == this.mMinDate.get(1) && this.mTempDate.get(12) == this.mMinDate.get(12)) {
            return;
        }
        this.mMinDate.setSafeTimeInMillis(j, this.mIsLunarMode);
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate.setSafeTimeInMillis(this.mMinDate.getTimeInMillis(), this.mIsLunarMode);
            resetMonthsDisplayedValues();
        }
        updateSpinners();
    }

    public long getMaxDate() {
        return this.mMaxDate.getTimeInMillis();
    }

    public void setMaxDate(long j) {
        this.mTempDate.setSafeTimeInMillis(j, this.mIsLunarMode);
        if (this.mTempDate.get(1) == this.mMaxDate.get(1) && this.mTempDate.get(12) == this.mMaxDate.get(12)) {
            return;
        }
        this.mMaxDate.setSafeTimeInMillis(j, this.mIsLunarMode);
        if (this.mCurrentDate.after(this.mMaxDate)) {
            this.mCurrentDate.setSafeTimeInMillis(this.mMaxDate.getTimeInMillis(), this.mIsLunarMode);
            resetMonthsDisplayedValues();
        }
        updateSpinners();
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        if (this.mIsEnabled == z) {
            return;
        }
        super.setEnabled(z);
        this.mDaySpinner.setEnabled(z);
        this.mMonthSpinner.setEnabled(z);
        this.mYearSpinner.setEnabled(z);
        this.mIsEnabled = z;
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.getText().add(DateUtils.formatDateTime(getContext(), this.mCurrentDate.getTimeInMillis(), 896));
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(DatePicker.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(DatePicker.class.getName());
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setCurrentLocale(configuration.locale);
    }

    public void setSpinnersShown(boolean z) {
        this.mSpinners.setVisibility(z ? 0 : 8);
    }

    private void setCurrentLocale(Locale locale) {
        if (locale.equals(this.mCurrentLocale)) {
            return;
        }
        this.mCurrentLocale = locale;
        this.mNumberOfMonths = this.mTempDate.getActualMaximum(5) + 1;
        resetMonthsDisplayedValues();
        updateFormatters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetMonthsDisplayedValues() {
        int i = 0;
        if (this.mIsLunarMode) {
            int chineseLeapMonth = this.mCurrentDate.getChineseLeapMonth();
            if (chineseLeapMonth < 0) {
                this.mShortMonths = sChineseMonths;
                return;
            }
            String[] strArr = sChineseLeapYearMonths;
            this.mShortMonths = strArr;
            int i2 = chineseLeapMonth + 1;
            System.arraycopy(sChineseMonths, 0, strArr, 0, i2);
            String[] strArr2 = sChineseMonths;
            System.arraycopy(strArr2, chineseLeapMonth, this.mShortMonths, i2, strArr2.length - chineseLeapMonth);
            this.mShortMonths[i2] = sChineseLeapMonthMark + this.mShortMonths[i2];
            return;
        }
        if ("en".equals(this.mCurrentLocale.getLanguage().toLowerCase())) {
            this.mShortMonths = CalendarFormatSymbols.getOrCreate(getContext()).getShortMonths();
            return;
        }
        this.mShortMonths = new String[12];
        while (true) {
            String[] strArr3 = this.mShortMonths;
            if (i >= strArr3.length) {
                return;
            }
            int i3 = i + 1;
            strArr3[i] = NumberPicker.TWO_DIGIT_FORMATTER.format(i3);
            i = i3;
        }
    }

    private void reorderSpinners() {
        NumberPicker numberPicker;
        this.mSpinners.removeAllViews();
        char[] dateFormatOrder = this.mDateFormatOrder;
        if (dateFormatOrder == null) {
            dateFormatOrder = android.text.format.DateFormat.getDateFormatOrder(getContext());
        }
        int length = dateFormatOrder.length;
        int i = 0;
        while (i < length) {
            char c = dateFormatOrder[i];
            if (c == 'M') {
                numberPicker = this.mMonthSpinner;
            } else if (c == 'd') {
                numberPicker = this.mDaySpinner;
            } else if (c == 'y') {
                numberPicker = this.mYearSpinner;
            } else {
                throw new IllegalArgumentException();
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) numberPicker.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(-2, -2);
            }
            layoutParams.weight = 0.0f;
            layoutParams.width = -2;
            layoutParams.setMarginStart(i > 0 ? this.mColumnSpacing : 0);
            numberPicker.setLayoutParams(layoutParams);
            this.mSpinners.addView(numberPicker);
            setImeOptions(numberPicker, length, i);
            i++;
        }
    }

    public void updateDate(int i, int i2, int i3) {
        if (isNewDate(i, i2, i3)) {
            setDate(i, i2, i3);
            updateSpinners();
            notifyDateChanged();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.mCurrentDate.get(1), this.mCurrentDate.get(5), this.mCurrentDate.get(9), this.mIsLunarMode);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setDate(savedState.mYear, savedState.mMonth, savedState.mDay);
        if (this.mIsLunarMode != savedState.mIsLunar) {
            this.mIsLunarMode = savedState.mIsLunar;
            resetMonthsDisplayedValues();
        }
        updateSpinners();
    }

    public void init(int i, int i2, int i3, OnDateChangedListener onDateChangedListener) {
        setDate(i, i2, i3);
        updateSpinners();
        this.mOnDateChangedListener = onDateChangedListener;
    }

    private boolean parseDate(String str, Calendar calendar) {
        try {
            calendar.setSafeTimeInMillis(this.mDateFormat.parse(str).getTime(), this.mIsLunarMode);
            return true;
        } catch (ParseException unused) {
            Log.w(LOG_TAG, "Date: " + str + " not in format: MM/dd/yyyy");
            return false;
        }
    }

    private boolean isNewDate(int i, int i2, int i3) {
        return (this.mCurrentDate.get(1) == i && this.mCurrentDate.get(5) == i3 && this.mCurrentDate.get(9) == i2) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDate(int i, int i2, int i3) {
        this.mCurrentDate.set(i, i2, i3, 12, 0, 0, 0);
        boolean zBefore = this.mCurrentDate.before(this.mMinDate);
        Calendar calendar = this.mCurrentDate;
        if (zBefore) {
            calendar.setSafeTimeInMillis(this.mMinDate.getTimeInMillis(), this.mIsLunarMode);
        } else if (calendar.after(this.mMaxDate)) {
            this.mCurrentDate.setSafeTimeInMillis(this.mMaxDate.getTimeInMillis(), this.mIsLunarMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpinners() {
        int actualMaximum;
        int i;
        int i2;
        boolean z = this.mIsLunarMode;
        NumberPicker numberPicker = this.mDaySpinner;
        if (z) {
            numberPicker.setLabel(null);
            this.mMonthSpinner.setLabel(null);
            this.mYearSpinner.setLabel(null);
        } else {
            numberPicker.setLabel(getContext().getString(R$string.date_picker_label_day));
            this.mMonthSpinner.setLabel(getContext().getString(R$string.date_picker_label_month));
            this.mYearSpinner.setLabel(getContext().getString(R$string.date_picker_label_year));
        }
        this.mDaySpinner.setDisplayedValues(null);
        this.mDaySpinner.setMinValue(1);
        NumberPicker numberPicker2 = this.mDaySpinner;
        boolean z2 = this.mIsLunarMode;
        Calendar calendar = this.mCurrentDate;
        if (z2) {
            actualMaximum = calendar.getActualMaximum(10);
        } else {
            actualMaximum = calendar.getActualMaximum(9);
        }
        numberPicker2.setMaxValue(actualMaximum);
        this.mDaySpinner.setWrapSelectorWheel(true);
        this.mMonthSpinner.setDisplayedValues(null);
        this.mMonthSpinner.setMinValue(0);
        NumberPicker numberPicker3 = this.mMonthSpinner;
        int i3 = 11;
        if (this.mIsLunarMode && this.mCurrentDate.getChineseLeapMonth() >= 0) {
            i3 = 12;
        }
        numberPicker3.setMaxValue(i3);
        this.mMonthSpinner.setWrapSelectorWheel(true);
        int i4 = this.mIsLunarMode ? 2 : 1;
        if (this.mCurrentDate.get(i4) == this.mMinDate.get(i4)) {
            this.mMonthSpinner.setMinValue(getMonthDisplayValueIndex(this.mMinDate, this.mIsLunarMode));
            this.mMonthSpinner.setWrapSelectorWheel(false);
            int i5 = this.mIsLunarMode ? 6 : 5;
            if (this.mCurrentDate.get(i5) == this.mMinDate.get(i5)) {
                NumberPicker numberPicker4 = this.mDaySpinner;
                boolean z3 = this.mIsLunarMode;
                Calendar calendar2 = this.mMinDate;
                if (z3) {
                    i2 = calendar2.get(10);
                } else {
                    i2 = calendar2.get(9);
                }
                numberPicker4.setMinValue(i2);
                this.mDaySpinner.setWrapSelectorWheel(false);
            }
        }
        if (this.mCurrentDate.get(i4) == this.mMaxDate.get(i4)) {
            this.mMonthSpinner.setMaxValue(getMonthDisplayValueIndex(this.mMaxDate, this.mIsLunarMode));
            this.mMonthSpinner.setWrapSelectorWheel(false);
            this.mMonthSpinner.setDisplayedValues(null);
            int i6 = this.mIsLunarMode ? 6 : 5;
            if (this.mCurrentDate.get(i6) == this.mMaxDate.get(i6)) {
                NumberPicker numberPicker5 = this.mDaySpinner;
                boolean z4 = this.mIsLunarMode;
                Calendar calendar3 = this.mMaxDate;
                if (z4) {
                    i = calendar3.get(10);
                } else {
                    i = calendar3.get(9);
                }
                numberPicker5.setMaxValue(i);
                this.mDaySpinner.setWrapSelectorWheel(false);
            }
        }
        this.mMonthSpinner.setDisplayedValues((String[]) Arrays.copyOfRange(this.mShortMonths, this.mMonthSpinner.getMinValue(), this.mShortMonths.length));
        if (this.mIsLunarMode) {
            this.mDaySpinner.setDisplayedValues((String[]) Arrays.copyOfRange(sChineseDays, this.mDaySpinner.getMinValue() - 1, sChineseDays.length));
        }
        int i7 = isLunarMode() ? 2 : 1;
        this.mYearSpinner.setMinValue(this.mMinDate.get(i7));
        this.mYearSpinner.setMaxValue(this.mMaxDate.get(i7));
        this.mYearSpinner.setWrapSelectorWheel(false);
        boolean z5 = this.mIsLunarMode;
        NumberPicker numberPicker6 = this.mYearSpinner;
        if (z5) {
            numberPicker6.setValue(this.mCurrentDate.get(2));
            this.mMonthSpinner.setValue(getMonthDisplayValueIndex(this.mCurrentDate, true));
            this.mDaySpinner.setValue(this.mCurrentDate.get(10));
        } else {
            numberPicker6.setValue(this.mCurrentDate.get(1));
            this.mMonthSpinner.setValue(this.mCurrentDate.get(5));
            this.mDaySpinner.setValue(this.mCurrentDate.get(9));
        }
    }

    private int getMonthDisplayValueIndex(Calendar calendar, boolean z) {
        if (z) {
            int i = calendar.get(6);
            int chineseLeapMonth = calendar.getChineseLeapMonth();
            if (chineseLeapMonth >= 0) {
                return (calendar.isChineseLeapMonth() || i > chineseLeapMonth) ? i + 1 : i;
            }
            return i;
        }
        return calendar.get(5);
    }

    public int getYear() {
        return this.mCurrentDate.get(this.mIsLunarMode ? 2 : 1);
    }

    public int getMonth() {
        boolean z = this.mIsLunarMode;
        Calendar calendar = this.mCurrentDate;
        if (z) {
            boolean zIsChineseLeapMonth = calendar.isChineseLeapMonth();
            Calendar calendar2 = this.mCurrentDate;
            return zIsChineseLeapMonth ? calendar2.get(6) + 12 : calendar2.get(6);
        }
        return calendar.get(5);
    }

    public int getDayOfMonth() {
        return this.mCurrentDate.get(this.mIsLunarMode ? 10 : 9);
    }

    public boolean isLunarMode() {
        return this.mIsLunarMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDateChanged() {
        sendAccessibilityEvent(4);
        OnDateChangedListener onDateChangedListener = this.mOnDateChangedListener;
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(this, getYear(), getMonth(), getDayOfMonth(), this.mIsLunarMode);
        }
    }

    private void setImeOptions(NumberPicker numberPicker, int i, int i2) {
        ((TextView) numberPicker.findViewById(R$id.number_picker_input)).setImeOptions(i2 < i + (-1) ? 5 : 6);
    }

    public void setFiveLinesAppearance(boolean z) {
        NumberPicker numberPicker = this.mYearSpinner;
        if (numberPicker != null) {
            numberPicker.setFiveLinesAppearance(z);
        }
        NumberPicker numberPicker2 = this.mMonthSpinner;
        if (numberPicker2 != null) {
            numberPicker2.setFiveLinesAppearance(z);
        }
        NumberPicker numberPicker3 = this.mDaySpinner;
        if (numberPicker3 != null) {
            numberPicker3.setFiveLinesAppearance(z);
        }
    }

    class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: miuix.pickerwidget.widget.DatePicker.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        private final int mDay;
        private final boolean mIsLunar;
        private final int mMonth;
        private final int mYear;

        private SavedState(Parcelable parcelable, int i, int i2, int i3, boolean z) {
            super(parcelable);
            this.mYear = i;
            this.mMonth = i2;
            this.mDay = i3;
            this.mIsLunar = z;
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mYear = parcel.readInt();
            this.mMonth = parcel.readInt();
            this.mDay = parcel.readInt();
            this.mIsLunar = parcel.readInt() == 1;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mYear);
            parcel.writeInt(this.mMonth);
            parcel.writeInt(this.mDay);
            parcel.writeInt(this.mIsLunar ? 1 : 0);
        }
    }
}
