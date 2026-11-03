package miuix.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import miuix.appcompat.app.CalendarDateTimePickerPanel;

/* JADX INFO: loaded from: classes2.dex */
public class StretchableCalendarPanelPreference extends BasePreference {
    private boolean mCalendarExpanded;
    private boolean mCalendarFixedWidth;
    private boolean mLunarChecked;
    private CalendarDateTimePickerPanel.OnDateChangedListener mOnDateChangedListener;
    private CalendarDateTimePickerPanel.OnTimeChangedListener mOnTimeChangedListener;
    private CalendarDateTimePickerPanel mPanel;
    private int mSavedDay;
    private int mSavedHour;
    private int mSavedMinute;
    private int mSavedMonth;
    private int mSavedYear;
    private boolean mShowCalendar;
    private boolean mShowLunar;
    private boolean mShowTimePicker;
    private boolean mTimeExpanded;
    private String mTitleText;

    public StretchableCalendarPanelPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowLunar = true;
        this.mShowCalendar = true;
        this.mShowTimePicker = true;
        this.mCalendarFixedWidth = false;
        this.mCalendarExpanded = false;
        this.mTimeExpanded = false;
        this.mLunarChecked = false;
        this.mSavedYear = -1;
        this.mSavedMonth = -1;
        this.mSavedDay = -1;
        this.mSavedHour = -1;
        this.mSavedMinute = -1;
        setTouchAnimationEnable(false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.StretchableCalendarPanelPreference, i, 0);
        this.mTitleText = typedArrayObtainStyledAttributes.getString(R$styleable.StretchableCalendarPanelPreference_panel_title);
        this.mShowLunar = typedArrayObtainStyledAttributes.getBoolean(R$styleable.StretchableCalendarPanelPreference_show_lunar_switch, true);
        this.mShowCalendar = typedArrayObtainStyledAttributes.getBoolean(R$styleable.StretchableCalendarPanelPreference_show_calendar, true);
        this.mShowTimePicker = typedArrayObtainStyledAttributes.getBoolean(R$styleable.StretchableCalendarPanelPreference_show_time_picker, true);
        this.mCalendarFixedWidth = typedArrayObtainStyledAttributes.getBoolean(R$styleable.StretchableCalendarPanelPreference_calendar_fixed_width, false);
        typedArrayObtainStyledAttributes.recycle();
    }

    public StretchableCalendarPanelPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.stretchableCalendarPanelPreferenceStyle);
    }

    @Override // miuix.preference.BasePreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        CalendarDateTimePickerPanel calendarDateTimePickerPanel = (CalendarDateTimePickerPanel) preferenceViewHolder.itemView;
        this.mPanel = calendarDateTimePickerPanel;
        calendarDateTimePickerPanel.resetState();
        this.mPanel.setTitleText(this.mTitleText);
        this.mPanel.setLunarSwitchVisible(this.mShowLunar);
        this.mPanel.setCalendarVisible(this.mShowCalendar);
        this.mPanel.setTimePickerVisible(this.mShowTimePicker);
        this.mPanel.setCalendarFixedWidth(this.mCalendarFixedWidth);
        if (this.mShowLunar && this.mLunarChecked) {
            this.mPanel.setLunarChecked(true);
        }
        int i = this.mSavedYear;
        if (i > 0) {
            this.mPanel.setDate(i, this.mSavedMonth, this.mSavedDay);
        }
        int i2 = this.mSavedHour;
        if (i2 >= 0) {
            this.mPanel.setTime(i2, this.mSavedMinute);
        }
        this.mPanel.restoreExpandState(this.mCalendarExpanded, this.mTimeExpanded);
        this.mPanel.setOnDateChangedListener(new CalendarDateTimePickerPanel.OnDateChangedListener() { // from class: miuix.preference.StretchableCalendarPanelPreference$$ExternalSyntheticLambda0
            @Override // miuix.appcompat.app.CalendarDateTimePickerPanel.OnDateChangedListener
            public final void onDateChanged(CalendarDateTimePickerPanel calendarDateTimePickerPanel2, int i3, int i4, int i5, String str) {
                StretchableCalendarPanelPreference.m5483$r8$lambda$8nqgK2lqRisVIW3fnCysvApNQ(this.f$0, calendarDateTimePickerPanel2, i3, i4, i5, str);
            }
        });
        this.mPanel.setOnTimeChangedListener(new CalendarDateTimePickerPanel.OnTimeChangedListener() { // from class: miuix.preference.StretchableCalendarPanelPreference$$ExternalSyntheticLambda1
            @Override // miuix.appcompat.app.CalendarDateTimePickerPanel.OnTimeChangedListener
            public final void onTimeChanged(CalendarDateTimePickerPanel calendarDateTimePickerPanel2, int i3, int i4) {
                StretchableCalendarPanelPreference.$r8$lambda$KuCrkJU16CL9ckVjWLajsU_xqLQ(this.f$0, calendarDateTimePickerPanel2, i3, i4);
            }
        });
        this.mPanel.setOnExpandStateChangedListener(new CalendarDateTimePickerPanel.OnExpandStateChangedListener() { // from class: miuix.preference.StretchableCalendarPanelPreference$$ExternalSyntheticLambda2
            @Override // miuix.appcompat.app.CalendarDateTimePickerPanel.OnExpandStateChangedListener
            public final void onExpandStateChanged(boolean z, boolean z2) {
                StretchableCalendarPanelPreference.m5484$r8$lambda$MmQQqd43U_c_xLYEvaskf20LpA(this.f$0, z, z2);
            }
        });
        this.mPanel.setOnLunarCheckedChangeListener(new CalendarDateTimePickerPanel.OnLunarCheckedChangeListener() { // from class: miuix.preference.StretchableCalendarPanelPreference$$ExternalSyntheticLambda3
            @Override // miuix.appcompat.app.CalendarDateTimePickerPanel.OnLunarCheckedChangeListener
            public final void onLunarCheckedChanged(boolean z) {
                this.f$0.mLunarChecked = z;
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$8nqgK2l-qRisVI-W3fnCysvApNQ, reason: not valid java name */
    public static /* synthetic */ void m5483$r8$lambda$8nqgK2lqRisVIW3fnCysvApNQ(StretchableCalendarPanelPreference stretchableCalendarPanelPreference, CalendarDateTimePickerPanel calendarDateTimePickerPanel, int i, int i2, int i3, String str) {
        stretchableCalendarPanelPreference.mSavedYear = i;
        stretchableCalendarPanelPreference.mSavedMonth = i2;
        stretchableCalendarPanelPreference.mSavedDay = i3;
        CalendarDateTimePickerPanel.OnDateChangedListener onDateChangedListener = stretchableCalendarPanelPreference.mOnDateChangedListener;
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(calendarDateTimePickerPanel, i, i2, i3, str);
        }
    }

    public static /* synthetic */ void $r8$lambda$KuCrkJU16CL9ckVjWLajsU_xqLQ(StretchableCalendarPanelPreference stretchableCalendarPanelPreference, CalendarDateTimePickerPanel calendarDateTimePickerPanel, int i, int i2) {
        stretchableCalendarPanelPreference.mSavedHour = i;
        stretchableCalendarPanelPreference.mSavedMinute = i2;
        CalendarDateTimePickerPanel.OnTimeChangedListener onTimeChangedListener = stretchableCalendarPanelPreference.mOnTimeChangedListener;
        if (onTimeChangedListener != null) {
            onTimeChangedListener.onTimeChanged(calendarDateTimePickerPanel, i, i2);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$MmQQqd43U_c_xL-YEvaskf20LpA, reason: not valid java name */
    public static /* synthetic */ void m5484$r8$lambda$MmQQqd43U_c_xLYEvaskf20LpA(StretchableCalendarPanelPreference stretchableCalendarPanelPreference, boolean z, boolean z2) {
        stretchableCalendarPanelPreference.mCalendarExpanded = z;
        stretchableCalendarPanelPreference.mTimeExpanded = z2;
    }
}
