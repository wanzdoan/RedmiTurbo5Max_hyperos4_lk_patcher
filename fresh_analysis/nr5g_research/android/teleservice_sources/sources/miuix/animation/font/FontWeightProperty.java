package miuix.animation.font;

import android.view.View;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Objects;
import miuix.animation.property.ISpecificProperty;
import miuix.animation.property.ViewProperty;

/* JADX INFO: loaded from: classes2.dex */
public class FontWeightProperty extends ViewProperty implements ISpecificProperty {
    private static final String NAME = "fontweight";
    private float mCurWeight;
    private int mFontType;
    private WeakReference mTextViewRef;

    public FontWeightProperty(TextView textView, int i) {
        super(NAME);
        this.mCurWeight = Float.MAX_VALUE;
        this.mTextViewRef = new WeakReference(textView);
        this.mFontType = i;
    }

    public TextView getTextView() {
        return (TextView) this.mTextViewRef.get();
    }

    public float getScaledTextSize() {
        TextView textView = (TextView) this.mTextViewRef.get();
        if (textView != null) {
            return textView.getTextSize() / textView.getResources().getDisplayMetrics().scaledDensity;
        }
        return 0.0f;
    }

    @Override // miuix.animation.property.FloatProperty
    public float getValue(View view) {
        return this.mCurWeight;
    }

    @Override // miuix.animation.property.FloatProperty
    public void setValue(View view, float f) {
        this.mCurWeight = f;
        TextView textView = (TextView) this.mTextViewRef.get();
        if (textView != null) {
            VarFontUtils.setVariationFont(textView, (int) f);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || this != obj) {
            return false;
        }
        TextView textView = (TextView) this.mTextViewRef.get();
        return textView != null && textView.equals((TextView) ((FontWeightProperty) obj).mTextViewRef.get());
    }

    public int hashCode() {
        TextView textView = (TextView) this.mTextViewRef.get();
        if (textView != null) {
            return Objects.hash(Integer.valueOf(super.hashCode()), textView);
        }
        return Objects.hash(Integer.valueOf(super.hashCode()), this.mTextViewRef);
    }

    @Override // miuix.animation.property.ISpecificProperty
    public float getSpecificValue(float f) {
        TextView textView = (TextView) this.mTextViewRef.get();
        if (f >= VarFontUtils.MIN_WGHT || textView == null) {
            return f;
        }
        return VarFontUtils.getScaleWeight((int) f, getScaledTextSize(), this.mFontType, VarFontUtils.getSysFontScale(textView.getContext()));
    }
}
