package miuix.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.preference.PreferenceViewHolder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import miuix.core.util.MiuixUIUtils;
import miuix.flexible.template.IHyperCellTemplate;
import miuix.flexible.view.HyperCellLayout;
import miuix.preference.flexible.AbstractBaseTemplate;

/* JADX INFO: loaded from: classes2.dex */
public class TextPreference extends BasePreference {
    private CharSequence mText;
    private int mTextRes;

    public interface TextProvider {
    }

    public final TextProvider getTextProvider() {
        return null;
    }

    public TextPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.TextPreference, i, i2);
        CharSequence text = typedArrayObtainStyledAttributes.getText(R$styleable.TextPreference_android_text);
        String string = typedArrayObtainStyledAttributes.getString(R$styleable.TextPreference_textProvider);
        typedArrayObtainStyledAttributes.recycle();
        if (!TextUtils.isEmpty(text)) {
            setText(text.toString());
        }
        createTextProvider(context, string);
        setTextProvider(null);
    }

    private TextProvider createTextProvider(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                Class[] clsArr = new Class[0];
                Constructor constructor = context.getClassLoader().loadClass(str).asSubclass(TextProvider.class).getConstructor(null);
                constructor.setAccessible(true);
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(constructor.newInstance(null));
                return null;
            } catch (ClassNotFoundException e) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Can't find provider: ", str, e);
            } catch (IllegalAccessException e2) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Can't access non-public constructor ", str, e2);
                return null;
            } catch (InstantiationException e3) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Could not instantiate the TextProvider: ", str, e3);
                return null;
            } catch (NoSuchMethodException e4) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Error creating TextProvider ", str, e4);
                return null;
            } catch (InvocationTargetException e5) {
                TextPreference$$ExternalSyntheticBUOutline0.m("Could not instantiate the TextProvider: ", str, e5);
                return null;
            }
        }
        return null;
    }

    public TextPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, R$style.Miuix_Preference_TextPreference);
    }

    public TextPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.textPreferenceStyle);
    }

    public TextPreference(Context context) {
        this(context, null);
    }

    public final void setTextProvider(TextProvider textProvider) {
        notifyChanged();
    }

    public void setText(String str) {
        getTextProvider();
        if (TextUtils.equals(str, this.mText)) {
            return;
        }
        this.mTextRes = 0;
        this.mText = str;
        notifyChanged();
    }

    public void setText(int i) {
        setText(getContext().getString(i));
        this.mTextRes = i;
    }

    public CharSequence getText() {
        getTextProvider();
        return this.mText;
    }

    @Override // miuix.preference.BasePreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        TextView textView = (TextView) view.findViewById(R$id.text_right);
        if (textView != null) {
            int visibility = textView.getVisibility();
            CharSequence text = getText();
            if (!TextUtils.isEmpty(text)) {
                changeTextAlignmentAndMaxWidth(textView);
                textView.setText(text);
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
            if (visibility == textView.getVisibility() || !(view instanceof HyperCellLayout)) {
                return;
            }
            IHyperCellTemplate template = ((HyperCellLayout) view).getTemplate();
            if (template instanceof AbstractBaseTemplate) {
                ((AbstractBaseTemplate) template).refreshLayout((ViewGroup) view);
            }
        }
    }

    private void changeTextAlignmentAndMaxWidth(TextView textView) {
        boolean z = false;
        boolean z2 = MiuixUIUtils.getFontLevel(getContext()) == 2;
        if (getLayoutResource() == R$layout.miuix_preference_flexible_text && getWidgetLayoutResource() == R$layout.miuix_preference_widget_text) {
            z = true;
        }
        int dimensionPixelOffset = z2 ? Integer.MAX_VALUE : getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_preference_widget_layout_max_width);
        int i = z2 ? 5 : 6;
        if (z) {
            textView.setTextAlignment(i);
            textView.setMaxWidth(dimensionPixelOffset);
        }
    }
}
