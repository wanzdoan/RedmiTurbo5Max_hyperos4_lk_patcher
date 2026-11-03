package miuix.appcompat.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.MarginLayoutParamsCompat;
import java.util.Calendar;
import java.util.Locale;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$styleable;
import miuix.pickerwidget.widget.DatePicker;
import miuix.pickerwidget.widget.TimePicker;

/* JADX INFO: loaded from: classes2.dex */
public class DatePickerPanel extends LinearLayout {
    private DatePicker datePicker;
    private LinearLayout datePickerContainer;
    private TextView headerDate;
    private View headerRoot;
    private TextView headerTime;
    private TextView headerTitle;
    private boolean isDateExpanded;
    private boolean isTimeExpanded;
    private int lastExpandedPanel;
    private OnDateChangedListener onDateChangedListener;
    private OnExpandStateChangedListener onExpandStateChangedListener;
    private OnTimeChangedListener onTimeChangedListener;
    private LinearLayout timeContainer;
    private TimePicker timePicker;

    public interface OnDateChangedListener {
        void onDateChanged(DatePickerPanel datePickerPanel, int i, int i2, int i3);
    }

    public interface OnExpandStateChangedListener {
        void onExpandStateChanged(boolean z, boolean z2);
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(DatePickerPanel datePickerPanel, int i, int i2);
    }

    public DatePickerPanel(Context context) {
        this(context, null);
    }

    public DatePickerPanel(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DatePickerPanel(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isDateExpanded = false;
        this.isTimeExpanded = false;
        this.lastExpandedPanel = 0;
        setOrientation(1);
        initView(context);
        initListeners();
        initAttr(context, attributeSet);
        updateDateTimeViews();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R$layout.miuix_appcompat_date_picker_panel_layout, (ViewGroup) this, true);
        this.headerTitle = (TextView) findViewById(R$id.header_title);
        this.headerDate = (TextView) findViewById(R$id.header_date);
        this.headerTime = (TextView) findViewById(R$id.header_time);
        this.headerRoot = findViewById(R$id.header_root);
        this.datePickerContainer = (LinearLayout) findViewById(R$id.date_picker_container);
        this.datePicker = (DatePicker) findViewById(R$id.date_picker);
        this.timeContainer = (LinearLayout) findViewById(R$id.time_container);
        TimePicker timePicker = (TimePicker) findViewById(R$id.time_picker);
        this.timePicker = timePicker;
        timePicker.set24HourView(Boolean.TRUE);
        this.headerRoot.post(new Runnable() { // from class: miuix.appcompat.app.DatePickerPanel$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.updateHeaderMaxWidths();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeaderMaxWidths() {
        this.headerTitle.setMaxWidth(Integer.MAX_VALUE);
        this.headerDate.setMaxWidth(Integer.MAX_VALUE);
        this.headerTime.setMaxWidth(Integer.MAX_VALUE);
        this.headerRoot.post(new Runnable() { // from class: miuix.appcompat.app.DatePickerPanel$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                DatePickerPanel.$r8$lambda$1PuThRrDq3_lsr3v8NHhjBOANEM(this.f$0);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$1PuThRrDq3_lsr3v8NHhjBOANEM(DatePickerPanel datePickerPanel) {
        int width = datePickerPanel.headerRoot.getWidth();
        int paddingStart = datePickerPanel.headerRoot.getPaddingStart();
        int paddingEnd = datePickerPanel.headerRoot.getPaddingEnd();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) datePickerPanel.headerTitle.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) datePickerPanel.headerDate.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) datePickerPanel.headerTime.getLayoutParams();
        int marginStart = width - (((((((paddingStart + paddingEnd) + MarginLayoutParamsCompat.getMarginStart(marginLayoutParams)) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams)) + MarginLayoutParamsCompat.getMarginStart(marginLayoutParams2)) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams2)) + MarginLayoutParamsCompat.getMarginStart(marginLayoutParams3)) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams3));
        if (marginStart <= 0) {
            return;
        }
        datePickerPanel.headerTitle.setMaxWidth((marginStart * 3) / 10);
        datePickerPanel.headerDate.setMaxWidth((marginStart * 5) / 10);
        datePickerPanel.headerTime.setMaxWidth((marginStart * 2) / 10);
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
        this.headerRoot.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.DatePickerPanel$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DatePickerPanel.$r8$lambda$_U9NGJPgiKRNpRsivO6XQFOkIRo(this.f$0, view);
            }
        });
        this.headerDate.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.DatePickerPanel$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.toggleDatePicker();
            }
        });
        this.headerTime.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.DatePickerPanel$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.toggleTimePicker();
            }
        });
        DatePicker datePicker = this.datePicker;
        datePicker.init(datePicker.getYear(), this.datePicker.getMonth(), this.datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() { // from class: miuix.appcompat.app.DatePickerPanel$$ExternalSyntheticLambda3
            @Override // miuix.pickerwidget.widget.DatePicker.OnDateChangedListener
            public final void onDateChanged(DatePicker datePicker2, int i, int i2, int i3, boolean z) {
                DatePickerPanel.$r8$lambda$cO1_FKMTAatx4HvcvTHBCUwSbv0(this.f$0, datePicker2, i, i2, i3, z);
            }
        });
        this.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() { // from class: miuix.appcompat.app.DatePickerPanel$$ExternalSyntheticLambda4
            @Override // miuix.pickerwidget.widget.TimePicker.OnTimeChangedListener
            public final void onTimeChanged(TimePicker timePicker, int i, int i2) {
                DatePickerPanel.$r8$lambda$mc90_utGin75lRewtrkRIuqGTIo(this.f$0, timePicker, i, i2);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$_U9NGJPgiKRNpRsivO6XQFOkIRo(DatePickerPanel datePickerPanel, View view) {
        boolean z = datePickerPanel.isDateExpanded;
        if (z || datePickerPanel.isTimeExpanded) {
            datePickerPanel.lastExpandedPanel = z ? 1 : 2;
            datePickerPanel.collapseAll();
            return;
        }
        if (datePickerPanel.lastExpandedPanel == 2) {
            if (datePickerPanel.headerTime.getVisibility() == 0) {
                datePickerPanel.toggleTimePicker();
                return;
            } else {
                if (datePickerPanel.headerDate.getVisibility() == 0) {
                    datePickerPanel.toggleDatePicker();
                    return;
                }
                return;
            }
        }
        if (datePickerPanel.headerDate.getVisibility() == 0) {
            datePickerPanel.toggleDatePicker();
        } else if (datePickerPanel.headerTime.getVisibility() == 0) {
            datePickerPanel.toggleTimePicker();
        }
    }

    public static /* synthetic */ void $r8$lambda$cO1_FKMTAatx4HvcvTHBCUwSbv0(DatePickerPanel datePickerPanel, DatePicker datePicker, int i, int i2, int i3, boolean z) {
        datePickerPanel.updateHeaderDateText(i, i2, i3);
        OnDateChangedListener onDateChangedListener = datePickerPanel.onDateChangedListener;
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(datePickerPanel, i, i2, i3);
        }
    }

    public static /* synthetic */ void $r8$lambda$mc90_utGin75lRewtrkRIuqGTIo(DatePickerPanel datePickerPanel, TimePicker timePicker, int i, int i2) {
        datePickerPanel.headerTime.setText(String.format(Locale.ROOT, "%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2)));
        OnTimeChangedListener onTimeChangedListener = datePickerPanel.onTimeChangedListener;
        if (onTimeChangedListener != null) {
            onTimeChangedListener.onTimeChanged(datePickerPanel, i, i2);
        }
    }

    private void updateHeaderDateText(int i, int i2, int i3) {
        this.headerDate.setText(String.format(Locale.ROOT, "%04d/%02d/%02d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
    }

    private void initAttr(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DatePickerPanel);
        try {
            String string = typedArrayObtainStyledAttributes.getString(R$styleable.DatePickerPanel_dppTitleText);
            if (!TextUtils.isEmpty(string)) {
                this.headerTitle.setText(string);
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.DatePickerPanel_dppShowDatePicker)) {
                setDatePickerVisible(typedArrayObtainStyledAttributes.getBoolean(R$styleable.DatePickerPanel_dppShowDatePicker, true));
            }
            if (typedArrayObtainStyledAttributes.hasValue(R$styleable.DatePickerPanel_dppShowTimePicker)) {
                setTimePickerVisible(typedArrayObtainStyledAttributes.getBoolean(R$styleable.DatePickerPanel_dppShowTimePicker, true));
            }
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public void toggleDatePicker() {
        if (this.isTimeExpanded && this.timePicker.isScrolling()) {
            return;
        }
        if (this.isTimeExpanded) {
            this.isTimeExpanded = false;
            this.headerTime.setSelected(false);
            this.timeContainer.setVisibility(8);
        }
        boolean z = !this.isDateExpanded;
        this.isDateExpanded = z;
        this.headerDate.setSelected(z);
        this.datePickerContainer.setVisibility(this.isDateExpanded ? 0 : 8);
        if (!this.isDateExpanded) {
            this.lastExpandedPanel = 1;
        }
        notifyExpandStateChanged();
    }

    public void toggleTimePicker() {
        if (this.isTimeExpanded && this.timePicker.isScrolling()) {
            return;
        }
        if (this.isDateExpanded) {
            this.isDateExpanded = false;
            this.headerDate.setSelected(false);
            this.datePickerContainer.setVisibility(8);
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
        if (this.isDateExpanded) {
            this.isDateExpanded = false;
            this.headerDate.setSelected(false);
            this.datePickerContainer.setVisibility(8);
        }
        if (this.isTimeExpanded) {
            this.isTimeExpanded = false;
            this.headerTime.setSelected(false);
            this.timeContainer.setVisibility(8);
        }
        notifyExpandStateChanged();
    }

    public void resetState() {
        if (this.isDateExpanded) {
            this.isDateExpanded = false;
            this.headerDate.setSelected(false);
            this.datePickerContainer.setVisibility(8);
        }
        if (this.isTimeExpanded) {
            this.isTimeExpanded = false;
            this.headerTime.setSelected(false);
            this.timeContainer.setVisibility(8);
        }
        this.lastExpandedPanel = 0;
    }

    public void restoreExpandState(boolean z, boolean z2) {
        if (z && this.headerDate.getVisibility() == 0) {
            this.isDateExpanded = true;
            this.headerDate.setSelected(true);
            this.datePickerContainer.setVisibility(0);
        }
        if (z2 && this.headerTime.getVisibility() == 0) {
            this.isTimeExpanded = true;
            this.headerTime.setSelected(true);
            this.timeContainer.setVisibility(0);
        }
    }

    private void updateDateTimeViews() {
        updateHeaderDateText(this.datePicker.getYear(), this.datePicker.getMonth(), this.datePicker.getDayOfMonth());
        Calendar calendar = Calendar.getInstance();
        this.headerTime.setText(String.format(Locale.ROOT, "%02d:%02d", Integer.valueOf(calendar.get(11)), Integer.valueOf(calendar.get(12))));
    }

    public void setTitleText(String str) {
        this.headerTitle.setText(str);
    }

    public void setDate(int i, int i2, int i3) {
        this.datePicker.updateDate(i, i2, i3);
        updateHeaderDateText(i, i2, i3);
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

    private void notifyExpandStateChanged() {
        OnExpandStateChangedListener onExpandStateChangedListener = this.onExpandStateChangedListener;
        if (onExpandStateChangedListener != null) {
            onExpandStateChangedListener.onExpandStateChanged(this.isDateExpanded, this.isTimeExpanded);
        }
    }

    public void setDatePickerVisible(boolean z) {
        this.headerDate.setVisibility(z ? 0 : 8);
        if (z || !this.isDateExpanded) {
            return;
        }
        this.isDateExpanded = false;
        this.headerDate.setSelected(false);
        this.datePickerContainer.setVisibility(8);
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
}
