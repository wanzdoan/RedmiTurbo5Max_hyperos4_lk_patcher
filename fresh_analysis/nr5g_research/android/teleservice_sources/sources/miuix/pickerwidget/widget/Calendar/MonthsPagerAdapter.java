package miuix.pickerwidget.widget.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import miuix.pickerwidget.R$layout;
import miuix.pickerwidget.date.Calendar;

/* JADX INFO: loaded from: classes2.dex */
class MonthsPagerAdapter extends RecyclerView.Adapter {
    private int mEndYear;
    private final LayoutInflater mInflater;
    private long mMaxDateTimeMills;
    private long mMinDateTimeMills;
    private OnDayClickListener mOnDayClickListener;
    private int mStartYear;
    private int mHighlightColor = 0;
    private boolean mLunarMode = false;
    private Calendar mCalendar = new Calendar();
    private int[] mYearAndMonth = new int[2];
    private int mCurrentDayOfMonth = this.mCalendar.get(9);

    interface OnDayClickListener {
        void onDayClick(CalendarGridLayout calendarGridLayout, View view, MonthsPagerAdapter monthsPagerAdapter);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MonthsPagerAdapter(Context context, long j, long j2, OnDayClickListener onDayClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mMinDateTimeMills = j;
        this.mMaxDateTimeMills = j2;
        this.mStartYear = this.mCalendar.setTimeInMillis(j).get(1);
        this.mEndYear = this.mCalendar.setTimeInMillis(j2).get(1);
        this.mOnDayClickListener = onDayClickListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public MonthViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MonthViewHolder(this.mInflater.inflate(R$layout.miuix_appcompat_calendar_date_picker_grid_layout, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(MonthViewHolder monthViewHolder, int i) {
        int[] yearAndMonth = getYearAndMonth(i);
        monthViewHolder.mGridLayout.bind(yearAndMonth[0], yearAndMonth[1], this.mCurrentDayOfMonth, this.mLunarMode, this.mHighlightColor);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return ((this.mEndYear - this.mStartYear) + 1) * 12;
    }

    int getPosition(int i, int i2) {
        return ((i - this.mStartYear) * 12) + i2;
    }

    int[] getYearAndMonth(int i) {
        int[] iArr = this.mYearAndMonth;
        iArr[0] = this.mStartYear + (i / 12);
        iArr[1] = i % 12;
        return iArr;
    }

    void notifyCurrentDayChanged(int i) {
        if (this.mCurrentDayOfMonth != i) {
            this.mCurrentDayOfMonth = i;
            notifyDataSetChanged();
        }
    }

    void notifyLunarModeChanged(boolean z) {
        if (this.mLunarMode != z) {
            this.mLunarMode = z;
            notifyDataSetChanged();
        }
    }

    boolean isLunarMode() {
        return this.mLunarMode;
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {
        final CalendarGridLayout mGridLayout;

        public MonthViewHolder(View view) {
            super(view);
            this.mGridLayout = (CalendarGridLayout) view;
            for (int i = 0; i < this.mGridLayout.getChildCount(); i++) {
                this.mGridLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() { // from class: miuix.pickerwidget.widget.Calendar.MonthsPagerAdapter$MonthViewHolder$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        MonthsPagerAdapter.MonthViewHolder.$r8$lambda$knpFiENJx_7QZjNV6X5DvFK1eyU(this.f$0, view2);
                    }
                });
            }
        }

        public static /* synthetic */ void $r8$lambda$knpFiENJx_7QZjNV6X5DvFK1eyU(MonthViewHolder monthViewHolder, View view) {
            if (MonthsPagerAdapter.this.mOnDayClickListener != null) {
                MonthsPagerAdapter.this.mOnDayClickListener.onDayClick(monthViewHolder.mGridLayout, view, MonthsPagerAdapter.this);
            }
        }
    }
}
