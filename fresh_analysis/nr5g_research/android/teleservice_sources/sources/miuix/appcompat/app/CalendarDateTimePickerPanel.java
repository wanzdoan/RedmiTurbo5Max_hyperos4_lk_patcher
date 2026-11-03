package miuix.appcompat.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.MarginLayoutParamsCompat;
import java.util.Calendar;
import java.util.Locale;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$styleable;
import miuix.pickerwidget.widget.Calendar.CalendarDatePicker;
import miuix.pickerwidget.widget.TimePicker;
import miuix.slidingwidget.widget.SlidingButton;

/* JADX INFO: loaded from: classes2.dex */
public class CalendarDateTimePickerPanel extends LinearLayout {
    private LinearLayout calendarContainer;
    private CalendarDatePicker calendarDatePicker;
    private TextView headerCalendar;
    private View headerRoot;
    private TextView headerTime;
    private TextView headerTitle;
    private boolean isCalendarExpanded;
    private boolean isTimeExpanded;
    private int lastExpandedPanel;
    private View lunarSwitchContainer;
    private OnDateChangedListener onDateChangedListener;
    private OnExpandStateChangedListener onExpandStateChangedListener;
    private OnLunarCheckedChangeListener onLunarCheckedChangeListener;
    private OnTimeChangedListener onTimeChangedListener;
    private SlidingButton slidingButton;
    private LinearLayout timeContainer;
    private TimePicker timePicker;

    public interface OnDateChangedListener {
        void onDateChanged(CalendarDateTimePickerPanel calendarDateTimePickerPanel, int i, int i2, int i3, String str);
    }

    public interface OnExpandStateChangedListener {
        void onExpandStateChanged(boolean z, boolean z2);
    }

    public interface OnLunarCheckedChangeListener {
        void onLunarCheckedChanged(boolean z);
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(CalendarDateTimePickerPanel calendarDateTimePickerPanel, int i, int i2);
    }

    public CalendarDateTimePickerPanel(Context context) {
        this(context, null);
    }

    public CalendarDateTimePickerPanel(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CalendarDateTimePickerPanel(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isCalendarExpanded = false;
        this.isTimeExpanded = false;
        this.lastExpandedPanel = 0;
        setOrientation(1);
        initView(context);
        initListeners();
        initAttr(context, attributeSet);
        updateDateTimeViews();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R$layout.miuix_appcompat_calendar_date_time_picker_panel_layout, (ViewGroup) this, true);
        this.headerTitle = (TextView) findViewById(R$id.header_title);
        this.headerCalendar = (TextView) findViewById(R$id.header_calendar);
        this.headerTime = (TextView) findViewById(R$id.header_time);
        this.headerRoot = findViewById(R$id.header_root);
        this.calendarContainer = (LinearLayout) findViewById(R$id.calendar_container);
        this.calendarDatePicker = (CalendarDatePicker) findViewById(R$id.calendar_date_picker);
        this.lunarSwitchContainer = findViewById(R$id.lunar_switch_container);
        this.slidingButton = (SlidingButton) findViewById(R$id.slidingButton);
        this.timeContainer = (LinearLayout) findViewById(R$id.time_container);
        TimePicker timePicker = (TimePicker) findViewById(R$id.time_picker);
        this.timePicker = timePicker;
        timePicker.set24HourView(Boolean.TRUE);
        this.headerRoot.post(new Runnable() { // from class: miuix.appcompat.app.CalendarDateTimePickerPanel$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.updateHeaderMaxWidths();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeaderMaxWidths() {
        this.headerTitle.setMaxWidth(Integer.MAX_VALUE);
        this.headerCalendar.setMaxWidth(Integer.MAX_VALUE);
        this.headerTime.setMaxWidth(Integer.MAX_VALUE);
        this.headerRoot.post(new Runnable() { // from class: miuix.appcompat.app.CalendarDateTimePickerPanel$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CalendarDateTimePickerPanel.$r8$lambda$1C6GT3QuPeTqG7Q9tfL_SnnJzrQ(this.f$0);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$1C6GT3QuPeTqG7Q9tfL_SnnJzrQ(CalendarDateTimePickerPanel calendarDateTimePickerPanel) {
        int width = calendarDateTimePickerPanel.headerRoot.getWidth();
        int paddingStart = calendarDateTimePickerPanel.headerRoot.getPaddingStart();
        int paddingEnd = calendarDateTimePickerPanel.headerRoot.getPaddingEnd();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) calendarDateTimePickerPanel.headerTitle.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) calendarDateTimePickerPanel.headerCalendar.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) calendarDateTimePickerPanel.headerTime.getLayoutParams();
        int marginStart = width - (((((((paddingStart + paddingEnd) + MarginLayoutParamsCompat.getMarginStart(marginLayoutParams)) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams)) + MarginLayoutParamsCompat.getMarginStart(marginLayoutParams2)) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams2)) + MarginLayoutParamsCompat.getMarginStart(marginLayoutParams3)) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams3));
        if (marginStart <= 0) {
            return;
        }
        calendarDateTimePickerPanel.headerTitle.setMaxWidth((marginStart * 3) / 10);
        calendarDateTimePickerPanel.headerCalendar.setMaxWidth((marginStart * 5) / 10);
        calendarDateTimePickerPanel.headerTime.setMaxWidth((marginStart * 2) / 10);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateHeaderMaxWidths();
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            updateDateTimeViews();
        }
    }

    private void initListeners() {
        this.headerRoot.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.CalendarDateTimePickerPanel$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CalendarDateTimePickerPanel.m5442$r8$lambda$sN1rPwj3vCz6XubGZOA0FCI5MM(this.f$0, view);
            }
        });
        this.headerCalendar.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.CalendarDateTimePickerPanel$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.toggleCalendar();
            }
        });
        this.headerTime.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.CalendarDateTimePickerPanel$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.toggleTimePicker();
            }
        });
        this.slidingButton.setOnPerformCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: miuix.appcompat.app.CalendarDateTimePickerPanel$$ExternalSyntheticLambda5
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                CalendarDateTimePickerPanel.$r8$lambda$ksUXtBzRlSenIi3PCrSgmW1knRA(this.f$0, compoundButton, z);
            }
        });
        this.calendarDatePicker.setOnDateChangedListener(new CalendarDatePicker.OnDateChangedListener() { // from class: miuix.appcompat.app.CalendarDateTimePickerPanel$$ExternalSyntheticLambda6
            @Override // miuix.pickerwidget.widget.Calendar.CalendarDatePicker.OnDateChangedListener
            public final void onDateChanged(CalendarDatePicker calendarDatePicker, int i, int i2, int i3, String str) {
                CalendarDateTimePickerPanel.$r8$lambda$8WcpJ16H1Cj4Ce4ClNcWRGRfD9s(this.f$0, calendarDatePicker, i, i2, i3, str);
            }
        });
        this.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() { // from class: miuix.appcompat.app.CalendarDateTimePickerPanel$$ExternalSyntheticLambda7
            @Override // miuix.pickerwidget.widget.TimePicker.OnTimeChangedListener
            public final void onTimeChanged(TimePicker timePicker, int i, int i2) {
                CalendarDateTimePickerPanel.$r8$lambda$8MUJJD_YbptXwaAVJQjSgPukxA0(this.f$0, timePicker, i, i2);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$sN1rPwj3v-Cz6XubGZOA0FCI5MM, reason: not valid java name */
    public static /* synthetic */ void m5442$r8$lambda$sN1rPwj3vCz6XubGZOA0FCI5MM(CalendarDateTimePickerPanel calendarDateTimePickerPanel, View view) {
        boolean z = calendarDateTimePickerPanel.isCalendarExpanded;
        if (z || calendarDateTimePickerPanel.isTimeExpanded) {
            calendarDateTimePickerPanel.lastExpandedPanel = z ? 1 : 2;
            calendarDateTimePickerPanel.collapseAll();
            return;
        }
        if (calendarDateTimePickerPanel.lastExpandedPanel == 2) {
            if (calendarDateTimePickerPanel.headerTime.getVisibility() == 0) {
                calendarDateTimePickerPanel.toggleTimePicker();
                return;
            } else {
                if (calendarDateTimePickerPanel.headerCalendar.getVisibility() == 0) {
                    calendarDateTimePickerPanel.toggleCalendar();
                    return;
                }
                return;
            }
        }
        if (calendarDateTimePickerPanel.headerCalendar.getVisibility() == 0) {
            calendarDateTimePickerPanel.toggleCalendar();
        } else if (calendarDateTimePickerPanel.headerTime.getVisibility() == 0) {
            calendarDateTimePickerPanel.toggleTimePicker();
        }
    }

    public static /* synthetic */ void $r8$lambda$ksUXtBzRlSenIi3PCrSgmW1knRA(CalendarDateTimePickerPanel calendarDateTimePickerPanel, CompoundButton compoundButton, boolean z) {
        calendarDateTimePickerPanel.calendarDatePicker.setLunarMode(z);
        int year = calendarDateTimePickerPanel.calendarDatePicker.getYear();
        int month = calendarDateTimePickerPanel.calendarDatePicker.getMonth();
        int dayOfMonth = calendarDateTimePickerPanel.calendarDatePicker.getDayOfMonth();
        String fullLunarMessage = z ? calendarDateTimePickerPanel.getFullLunarMessage(year, month, dayOfMonth) : null;
        calendarDateTimePickerPanel.updateHeaderCalendarText(year, month, dayOfMonth, fullLunarMessage);
        OnLunarCheckedChangeListener onLunarCheckedChangeListener = calendarDateTimePickerPanel.onLunarCheckedChangeListener;
        if (onLunarCheckedChangeListener != null) {
            onLunarCheckedChangeListener.onLunarCheckedChanged(z);
        }
        OnDateChangedListener onDateChangedListener = calendarDateTimePickerPanel.onDateChangedListener;
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(calendarDateTimePickerPanel, year, month, dayOfMonth, fullLunarMessage);
        }
    }

    public static /* synthetic */ void $r8$lambda$8WcpJ16H1Cj4Ce4ClNcWRGRfD9s(CalendarDateTimePickerPanel calendarDateTimePickerPanel, CalendarDatePicker calendarDatePicker, int i, int i2, int i3, String str) {
        String fullLunarMessage = calendarDateTimePickerPanel.slidingButton.isChecked() ? calendarDateTimePickerPanel.getFullLunarMessage(i, i2, i3) : null;
        calendarDateTimePickerPanel.updateHeaderCalendarText(i, i2, i3, fullLunarMessage);
        OnDateChangedListener onDateChangedListener = calendarDateTimePickerPanel.onDateChangedListener;
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(calendarDateTimePickerPanel, i, i2, i3, fullLunarMessage);
        }
    }

    public static /* synthetic */ void $r8$lambda$8MUJJD_YbptXwaAVJQjSgPukxA0(CalendarDateTimePickerPanel calendarDateTimePickerPanel, TimePicker timePicker, int i, int i2) {
        calendarDateTimePickerPanel.headerTime.setText(String.format(Locale.ROOT, "%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2)));
        OnTimeChangedListener onTimeChangedListener = calendarDateTimePickerPanel.onTimeChangedListener;
        if (onTimeChangedListener != null) {
            onTimeChangedListener.onTimeChanged(calendarDateTimePickerPanel, i, i2);
        }
    }

    private void updateHeaderCalendarText(int i, int i2, int i3, String str) {
        String str2 = String.format(Locale.ROOT, "%04d/%02d/%02d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3));
        boolean zIsEmpty = TextUtils.isEmpty(str);
        TextView textView = this.headerCalendar;
        if (!zIsEmpty) {
            textView.setText(str);
        } else {
            textView.setText(str2);
        }
    }

    private void initAttr(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CalendarDatePickerPanel);
        try {
            String string = typedArrayObtainStyledAttributes.getString(R$styleable.CalendarDatePickerPanel_titleText);
            if (!TextUtils.isEmpty(string)) {
                this.headerTitle.setText(string);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.CalendarDatePickerPanel_showLunarSwitch)) {
                setLunarSwitchVisible(typedArrayObtainStyledAttributes.getBoolean(R$styleable.CalendarDatePickerPanel_showLunarSwitch, true));
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.CalendarDatePickerPanel_showCalendar)) {
                setCalendarVisible(typedArrayObtainStyledAttributes.getBoolean(R$styleable.CalendarDatePickerPanel_showCalendar, true));
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.CalendarDatePickerPanel_showTimePicker)) {
                setTimePickerVisible(typedArrayObtainStyledAttributes.getBoolean(R$styleable.CalendarDatePickerPanel_showTimePicker, true));
            }
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public void toggleCalendar() {
        if (this.isTimeExpanded && this.timePicker.isScrolling()) {
            return;
        }
        if (this.isTimeExpanded) {
            this.isTimeExpanded = false;
            this.headerTime.setSelected(false);
            this.timeContainer.setVisibility(8);
        }
        boolean z = !this.isCalendarExpanded;
        this.isCalendarExpanded = z;
        this.headerCalendar.setSelected(z);
        this.calendarContainer.setVisibility(this.isCalendarExpanded ? 0 : 8);
        if (this.isCalendarExpanded) {
            boolean zIsChecked = this.slidingButton.isChecked();
            if (this.calendarDatePicker.isLunarMode() != zIsChecked) {
                this.calendarDatePicker.setLunarMode(zIsChecked);
            }
        } else {
            this.lastExpandedPanel = 1;
        }
        notifyExpandStateChanged();
    }

    public void toggleTimePicker() {
        if (this.isTimeExpanded && this.timePicker.isScrolling()) {
            return;
        }
        if (this.isCalendarExpanded) {
            this.isCalendarExpanded = false;
            this.headerCalendar.setSelected(false);
            this.calendarContainer.setVisibility(8);
        }
        boolean z = !this.isTimeExpanded;
        this.isTimeExpanded = z;
        this.headerTime.setSelected(z);
        this.timeContainer.setVisibility(this.isTimeExpanded ? 0 : 8);
        if (!this.isTimeExpanded) {
            this.lastExpandedPanel = 2;
        }
        notifyExpandStateChanged();
    }

    private void collapseAll() {
        if (this.isTimeExpanded && this.timePicker.isScrolling()) {
            return;
        }
        if (this.isCalendarExpanded) {
            this.isCalendarExpanded = false;
            this.headerCalendar.setSelected(false);
            this.calendarContainer.setVisibility(8);
        }
        if (this.isTimeExpanded) {
            this.isTimeExpanded = false;
            this.headerTime.setSelected(false);
            this.timeContainer.setVisibility(8);
        }
        notifyExpandStateChanged();
    }

    public void resetState() {
        if (this.isCalendarExpanded) {
            this.isCalendarExpanded = false;
            this.headerCalendar.setSelected(false);
            this.calendarContainer.setVisibility(8);
        }
        if (this.isTimeExpanded) {
            this.isTimeExpanded = false;
            this.headerTime.setSelected(false);
            this.timeContainer.setVisibility(8);
        }
        this.lastExpandedPanel = 0;
        if (this.slidingButton.isChecked()) {
            this.slidingButton.setChecked(false);
            this.calendarDatePicker.setLunarMode(false);
        }
    }

    public void setLunarChecked(boolean z) {
        this.slidingButton.setChecked(z);
        this.calendarDatePicker.setLunarMode(z);
    }

    public void restoreExpandState(boolean z, boolean z2) {
        if (z && this.headerCalendar.getVisibility() == 0) {
            this.isCalendarExpanded = true;
            this.headerCalendar.setSelected(true);
            this.calendarContainer.setVisibility(0);
        }
        if (z2 && this.headerTime.getVisibility() == 0) {
            this.isTimeExpanded = true;
            this.headerTime.setSelected(true);
            this.timeContainer.setVisibility(0);
        }
    }

    private void updateDateTimeViews() {
        int year = this.calendarDatePicker.getYear();
        int month = this.calendarDatePicker.getMonth();
        int dayOfMonth = this.calendarDatePicker.getDayOfMonth();
        updateHeaderCalendarText(year, month, dayOfMonth, this.slidingButton.isChecked() ? getFullLunarMessage(year, month, dayOfMonth) : null);
        Calendar calendar = Calendar.getInstance();
        this.headerTime.setText(String.format(Locale.ROOT, "%02d:%02d", Integer.valueOf(calendar.get(11)), Integer.valueOf(calendar.get(12))));
    }

    public void setTitleText(String str) {
        this.headerTitle.setText(str);
    }

    public String getFullLunarMessage(int i, int i2, int i3) {
        return this.calendarDatePicker.getFullLunarMessage(i, i2, i3);
    }

    public void setDate(int i, int i2, int i3) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i2, i3);
        this.calendarDatePicker.setDate(calendar.getTimeInMillis(), false);
        updateHeaderCalendarText(i, i2, i3, this.slidingButton.isChecked() ? getFullLunarMessage(i, i2, i3) : null);
    }

    public void setTime(int i, int i2) {
        this.timePicker.setCurrentHour(Integer.valueOf(i));
        this.timePicker.setCurrentMinute(Integer.valueOf(i2));
        this.headerTime.setText(String.format(Locale.ROOT, "%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2)));
    }

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.onDateChangedListener = onDateChangedListener;
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.onTimeChangedListener = onTimeChangedListener;
    }

    public void setOnExpandStateChangedListener(OnExpandStateChangedListener onExpandStateChangedListener) {
        this.onExpandStateChangedListener = onExpandStateChangedListener;
    }

    public void setOnLunarCheckedChangeListener(OnLunarCheckedChangeListener onLunarCheckedChangeListener) {
        this.onLunarCheckedChangeListener = onLunarCheckedChangeListener;
    }

    private void notifyExpandStateChanged() {
        OnExpandStateChangedListener onExpandStateChangedListener = this.onExpandStateChangedListener;
        if (onExpandStateChangedListener != null) {
            onExpandStateChangedListener.onExpandStateChanged(this.isCalendarExpanded, this.isTimeExpanded);
        }
    }

    public void setLunarSwitchVisible(boolean z) {
        this.lunarSwitchContainer.setVisibility(z ? 0 : 8);
        if (z || !this.slidingButton.isChecked()) {
            return;
        }
        this.slidingButton.setChecked(false);
        this.calendarDatePicker.setLunarMode(false);
    }

    public void setCalendarVisible(boolean z) {
        this.headerCalendar.setVisibility(z ? 0 : 8);
        if (z || !this.isCalendarExpanded) {
            return;
        }
        this.isCalendarExpanded = false;
        this.headerCalendar.setSelected(false);
        this.calendarContainer.setVisibility(8);
    }

    public void setTimePickerVisible(boolean z) {
        this.headerTime.setVisibility(z ? 0 : 8);
        if (z || !this.isTimeExpanded) {
            return;
        }
        this.isTimeExpanded = false;
        this.headerTime.setSelected(false);
        this.timeContainer.setVisibility(8);
    }

    public void setCalendarFixedWidth(boolean z) {
        CalendarDatePicker calendarDatePicker = this.calendarDatePicker;
        if (calendarDatePicker != null) {
            calendarDatePicker.setFixedWidth(z);
        }
    }
}
