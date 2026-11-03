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
import java.util.Locale;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$styleable;
import miuix.pickerwidget.widget.NumberPicker;

/* JADX INFO: loaded from: classes2.dex */
public class NumberPickerPanel extends LinearLayout {
    private View headerRoot;
    private TextView headerTitle;
    private TextView headerValue;
    private boolean isExpanded;
    private Runnable mConfirmRunnable;
    private NumberPicker numberPicker;
    private OnExpandStateChangedListener onExpandStateChangedListener;
    private OnValueChangedListener onValueChangedListener;
    private OnValueConfirmedListener onValueConfirmedListener;
    private LinearLayout pickerContainer;
    private String pickerLabel;
    private String unitText;

    public interface OnExpandStateChangedListener {
        void onExpandStateChanged(boolean z);
    }

    public interface OnValueChangedListener {
        void onValueChanged(NumberPickerPanel numberPickerPanel, int i);
    }

    public interface OnValueConfirmedListener {
        void onValueConfirmed(NumberPickerPanel numberPickerPanel, int i);
    }

    public NumberPickerPanel(Context context) {
        this(context, null);
    }

    public NumberPickerPanel(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NumberPickerPanel(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isExpanded = false;
        setOrientation(1);
        initView(context);
        initListeners();
        initAttr(context, attributeSet);
        updateHeaderValueText();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R$layout.miuix_appcompat_number_picker_panel_layout, (ViewGroup) this, true);
        this.headerRoot = findViewById(R$id.header_root);
        this.headerTitle = (TextView) findViewById(R$id.header_title);
        this.headerValue = (TextView) findViewById(R$id.header_value);
        this.pickerContainer = (LinearLayout) findViewById(R$id.picker_container);
        NumberPicker numberPicker = (NumberPicker) findViewById(R$id.number_picker);
        this.numberPicker = numberPicker;
        numberPicker.setMinValue(1);
        this.numberPicker.setMaxValue(10);
        this.pickerContainer.setVisibility(8);
        this.headerRoot.post(new Runnable() { // from class: miuix.appcompat.app.NumberPickerPanel$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.updateHeaderMaxWidths();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeaderMaxWidths() {
        this.headerTitle.setMaxWidth(Integer.MAX_VALUE);
        this.headerValue.setMaxWidth(Integer.MAX_VALUE);
        this.headerRoot.post(new Runnable() { // from class: miuix.appcompat.app.NumberPickerPanel$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                NumberPickerPanel.m5446$r8$lambda$SOQTHzJrKAB6yzK4caX9PSFPs(this.f$0);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$S-OQ-THzJrKAB6yzK4caX9PSFPs, reason: not valid java name */
    public static /* synthetic */ void m5446$r8$lambda$SOQTHzJrKAB6yzK4caX9PSFPs(NumberPickerPanel numberPickerPanel) {
        int width = numberPickerPanel.headerRoot.getWidth();
        int paddingStart = numberPickerPanel.headerRoot.getPaddingStart();
        int paddingEnd = numberPickerPanel.headerRoot.getPaddingEnd();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) numberPickerPanel.headerTitle.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) numberPickerPanel.headerValue.getLayoutParams();
        int marginStart = width - (((((paddingStart + paddingEnd) + MarginLayoutParamsCompat.getMarginStart(marginLayoutParams)) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams)) + MarginLayoutParamsCompat.getMarginStart(marginLayoutParams2)) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams2));
        if (marginStart <= 0) {
            return;
        }
        numberPickerPanel.headerTitle.setMaxWidth((marginStart * 2) / 3);
        numberPickerPanel.headerValue.setMaxWidth(marginStart / 3);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateHeaderMaxWidths();
    }

    private void initListeners() {
        this.headerRoot.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.NumberPickerPanel$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.toggle();
            }
        });
        this.headerValue.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.NumberPickerPanel$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.toggle();
            }
        });
        this.numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { // from class: miuix.appcompat.app.NumberPickerPanel$$ExternalSyntheticLambda2
            @Override // miuix.pickerwidget.widget.NumberPicker.OnValueChangeListener
            public final void onValueChange(NumberPicker numberPicker, int i, int i2) {
                NumberPickerPanel.m5447$r8$lambda$uobHtws7wokoeooiSJ0snFULuE(this.f$0, numberPicker, i, i2);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$uobHtws7wokoeooiSJ0snFULu-E, reason: not valid java name */
    public static /* synthetic */ void m5447$r8$lambda$uobHtws7wokoeooiSJ0snFULuE(final NumberPickerPanel numberPickerPanel, NumberPicker numberPicker, int i, int i2) {
        numberPickerPanel.updateHeaderValueText();
        OnValueChangedListener onValueChangedListener = numberPickerPanel.onValueChangedListener;
        if (onValueChangedListener != null) {
            onValueChangedListener.onValueChanged(numberPickerPanel, i2);
        }
        if (numberPickerPanel.onValueConfirmedListener != null) {
            Runnable runnable = numberPickerPanel.mConfirmRunnable;
            if (runnable != null) {
                numberPickerPanel.numberPicker.removeCallbacks(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: miuix.appcompat.app.NumberPickerPanel$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    NumberPickerPanel.m5445$r8$lambda$FfD7_ZwLZCsCYS52QvuCMhHpZA(this.f$0);
                }
            };
            numberPickerPanel.mConfirmRunnable = runnable2;
            numberPickerPanel.numberPicker.postDelayed(runnable2, 300L);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$FfD7_ZwLZCsCY-S52QvuCMhHpZA, reason: not valid java name */
    public static /* synthetic */ void m5445$r8$lambda$FfD7_ZwLZCsCYS52QvuCMhHpZA(NumberPickerPanel numberPickerPanel) {
        OnValueConfirmedListener onValueConfirmedListener = numberPickerPanel.onValueConfirmedListener;
        if (onValueConfirmedListener != null) {
            onValueConfirmedListener.onValueConfirmed(numberPickerPanel, numberPickerPanel.numberPicker.getValue());
        }
    }

    private void initAttr(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.NumberPickerPanel);
        try {
            String string = typedArrayObtainStyledAttributes.getString(R$styleable.NumberPickerPanel_nppTitleText);
            if (!TextUtils.isEmpty(string)) {
                this.headerTitle.setText(string);
            }
            this.unitText = typedArrayObtainStyledAttributes.getString(R$styleable.NumberPickerPanel_nppUnitText);
            String string2 = typedArrayObtainStyledAttributes.getString(R$styleable.NumberPickerPanel_nppPickerLabel);
            this.pickerLabel = string2;
            if (!TextUtils.isEmpty(string2)) {
                this.numberPicker.setLabel(this.pickerLabel);
            }
            int i = typedArrayObtainStyledAttributes.getInt(R$styleable.NumberPickerPanel_nppMinValue, 1);
            int i2 = typedArrayObtainStyledAttributes.getInt(R$styleable.NumberPickerPanel_nppMaxValue, 10);
            int i3 = typedArrayObtainStyledAttributes.getInt(R$styleable.NumberPickerPanel_nppDefaultValue, i);
            this.numberPicker.setMinValue(i);
            this.numberPicker.setMaxValue(i2);
            this.numberPicker.setValue(i3);
            updateHeaderValueText();
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggle() {
        boolean z = this.isExpanded;
        this.isExpanded = !z;
        this.pickerContainer.setVisibility(!z ? 0 : 8);
        this.headerValue.setSelected(this.isExpanded);
        OnExpandStateChangedListener onExpandStateChangedListener = this.onExpandStateChangedListener;
        if (onExpandStateChangedListener != null) {
            onExpandStateChangedListener.onExpandStateChanged(this.isExpanded);
        }
    }

    private void updateHeaderValueText() {
        int value = this.numberPicker.getValue();
        boolean zIsEmpty = TextUtils.isEmpty(this.unitText);
        TextView textView = this.headerValue;
        if (!zIsEmpty) {
            textView.setText(String.format(Locale.ROOT, "%d%s", Integer.valueOf(value), this.unitText));
        } else {
            textView.setText(String.valueOf(value));
        }
    }

    public void resetState() {
        this.isExpanded = false;
        this.headerValue.setSelected(false);
        this.pickerContainer.setVisibility(8);
    }

    public void setValue(int i) {
        this.numberPicker.setValue(i);
        updateHeaderValueText();
    }

    public void setMinValue(int i) {
        this.numberPicker.setMinValue(i);
    }

    public void setMaxValue(int i) {
        this.numberPicker.setMaxValue(i);
    }

    public void setTitleText(String str) {
        this.headerTitle.setText(str);
    }

    public void setUnitText(String str) {
        this.unitText = str;
        updateHeaderValueText();
    }

    public void setPickerLabel(String str) {
        this.pickerLabel = str;
        this.numberPicker.setLabel(str);
    }

    public void restoreExpandState(boolean z) {
        if (z) {
            this.isExpanded = true;
            this.pickerContainer.setVisibility(0);
            this.headerValue.setSelected(true);
        }
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    public void setOnValueConfirmedListener(OnValueConfirmedListener onValueConfirmedListener) {
        this.onValueConfirmedListener = onValueConfirmedListener;
    }

    public void setOnExpandStateChangedListener(OnExpandStateChangedListener onExpandStateChangedListener) {
        this.onExpandStateChangedListener = onExpandStateChangedListener;
    }
}
