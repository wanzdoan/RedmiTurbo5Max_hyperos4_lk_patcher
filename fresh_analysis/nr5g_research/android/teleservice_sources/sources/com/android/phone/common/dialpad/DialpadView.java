package com.android.phone.common.dialpad;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.phone.common.R$dimen;
import com.android.phone.common.R$drawable;
import com.android.phone.common.R$id;
import com.android.phone.common.R$string;
import com.android.phone.common.R$styleable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class DialpadView extends LinearLayout {
    private static final String TAG = DialpadView.class.getSimpleName();
    private final int[] mButtonIds;
    private ImageButton mDelete;
    private EditText mDigits;
    private TextView mIldCountry;
    private TextView mIldRate;
    private final boolean mIsLandscape;
    private final boolean mIsRtl;
    private View mOverflowMenuButton;
    private ViewGroup mRateContainer;
    private ColorStateList mRippleColor;
    private int mTranslateDistance;

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        return true;
    }

    public DialpadView(Context context) {
        this(context, null);
    }

    public DialpadView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DialpadView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mButtonIds = new int[]{R$id.zero, R$id.one, R$id.two, R$id.three, R$id.four, R$id.five, R$id.six, R$id.seven, R$id.eight, R$id.nine, R$id.star, R$id.pound};
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Dialpad);
        this.mRippleColor = typedArrayObtainStyledAttributes.getColorStateList(R$styleable.Dialpad_dialpad_key_button_touch_tint);
        typedArrayObtainStyledAttributes.recycle();
        this.mTranslateDistance = getResources().getDimensionPixelSize(R$dimen.dialpad_key_button_translate_y);
        this.mIsLandscape = getResources().getConfiguration().orientation == 2;
        this.mIsRtl = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        setupKeypad();
        this.mDigits = (EditText) findViewById(R$id.digits);
        this.mDelete = (ImageButton) findViewById(R$id.deleteButton);
        this.mOverflowMenuButton = findViewById(R$id.dialpad_overflow);
        ViewGroup viewGroup = (ViewGroup) findViewById(R$id.rate_container);
        this.mRateContainer = viewGroup;
        this.mIldCountry = (TextView) viewGroup.findViewById(R$id.ild_country);
        this.mIldRate = (TextView) this.mRateContainer.findViewById(R$id.ild_rate);
        if (((AccessibilityManager) getContext().getSystemService("accessibility")).isEnabled()) {
            this.mDigits.setSelected(true);
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x00d7  */
    /* JADX WARN: Code duplicated, block: B:22:0x00e8  */
    /* JADX WARN: Code duplicated, block: B:28:0x00f1 A[SYNTHETIC] */
    private void setupKeypad() {
        NumberFormat decimalFormat;
        String string;
        CharSequence charSequence;
        RippleDrawable rippleDrawable;
        ColorStateList colorStateList;
        int[] iArr = {R$string.dialpad_0_letters, R$string.dialpad_1_letters, R$string.dialpad_2_letters, R$string.dialpad_3_letters, R$string.dialpad_4_letters, R$string.dialpad_5_letters, R$string.dialpad_6_letters, R$string.dialpad_7_letters, R$string.dialpad_8_letters, R$string.dialpad_9_letters, R$string.dialpad_star_letters, R$string.dialpad_pound_letters};
        Resources resources = getContext().getResources();
        if ("fa".equals(resources.getConfiguration().locale.getLanguage())) {
            decimalFormat = DecimalFormat.getInstance(resources.getConfiguration().locale);
        } else {
            decimalFormat = DecimalFormat.getInstance(Locale.ENGLISH);
        }
        int i = 0;
        while (true) {
            int[] iArr2 = this.mButtonIds;
            if (i < iArr2.length) {
                DialpadKeyButton dialpadKeyButton = (DialpadKeyButton) findViewById(iArr2[i]);
                TextView textView = (TextView) dialpadKeyButton.findViewById(R$id.dialpad_key_number);
                TextView textView2 = (TextView) dialpadKeyButton.findViewById(R$id.dialpad_key_letters);
                int i2 = this.mButtonIds[i];
                if (i2 == R$id.pound) {
                    string = resources.getString(R$string.dialpad_pound_number);
                } else {
                    if (i2 == R$id.star) {
                        string = resources.getString(R$string.dialpad_star_number);
                    } else {
                        string = decimalFormat.format(i);
                        String string2 = resources.getString(iArr[i]);
                        Spannable spannableNewSpannable = Spannable.Factory.getInstance().newSpannable(string + "," + string2);
                        spannableNewSpannable.setSpan(new TtsSpan.VerbatimBuilder(string2).build(), string.length() + 1, string.length() + 1 + string2.length(), 33);
                        charSequence = spannableNewSpannable;
                    }
                    rippleDrawable = (RippleDrawable) getDrawableCompat(getContext(), R$drawable.btn_dialpad_key);
                    colorStateList = this.mRippleColor;
                    if (colorStateList != null) {
                        rippleDrawable.setColor(colorStateList);
                    }
                    textView.setText(string);
                    textView.setElegantTextHeight(false);
                    dialpadKeyButton.setContentDescription(charSequence);
                    dialpadKeyButton.setBackground(rippleDrawable);
                    if (textView2 != null) {
                        textView2.setText(resources.getString(iArr[i]));
                    }
                    i++;
                }
                charSequence = string;
                rippleDrawable = (RippleDrawable) getDrawableCompat(getContext(), R$drawable.btn_dialpad_key);
                colorStateList = this.mRippleColor;
                if (colorStateList != null) {
                    rippleDrawable.setColor(colorStateList);
                }
                textView.setText(string);
                textView.setElegantTextHeight(false);
                dialpadKeyButton.setContentDescription(charSequence);
                dialpadKeyButton.setBackground(rippleDrawable);
                if (textView2 != null) {
                    textView2.setText(resources.getString(iArr[i]));
                }
                i++;
            } else {
                ((DialpadKeyButton) findViewById(R$id.one)).setLongHoverContentDescription(resources.getText(R$string.description_voicemail_button));
                ((DialpadKeyButton) findViewById(R$id.zero)).setLongHoverContentDescription(resources.getText(R$string.description_image_button_plus));
                return;
            }
        }
    }

    private Drawable getDrawableCompat(Context context, int i) {
        return context.getDrawable(i);
    }
}
