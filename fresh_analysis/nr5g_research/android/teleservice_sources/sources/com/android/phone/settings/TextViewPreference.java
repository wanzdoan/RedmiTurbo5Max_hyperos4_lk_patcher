package com.android.phone.settings;

import android.content.Context;
import android.preference.Preference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class TextViewPreference extends Preference {
    private CharSequence mText;
    private int mTextResourceId;
    private TextView mTextView;

    public TextViewPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTextResourceId = 0;
        setLayoutResource(R.layout.text_view_preference);
    }

    public TextViewPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public TextViewPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.preferenceStyle, 0);
    }

    public TextViewPreference(Context context) {
        super(context, null);
        this.mTextResourceId = 0;
        setLayoutResource(R.layout.text_view_preference);
    }

    @Override // android.preference.Preference
    protected void onBindView(View view) {
        super.onBindView(view);
        this.mTextView = (TextView) view.findViewById(R.id.text);
        int i = this.mTextResourceId;
        if (i != 0) {
            setTitle(i);
            return;
        }
        CharSequence charSequence = this.mText;
        if (charSequence != null) {
            setTitle(charSequence);
        } else if (getTitleRes() != 0) {
            setTitle(getTitleRes());
        }
    }

    @Override // android.preference.Preference
    public void setTitle(CharSequence charSequence) {
        this.mTextResourceId = 0;
        this.mText = charSequence;
        TextView textView = this.mTextView;
        if (textView == null) {
            return;
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        this.mTextView.setText(charSequence);
    }

    @Override // android.preference.Preference
    public void setTitle(int i) {
        this.mTextResourceId = i;
        setTitle(Html.fromHtml(getContext().getString(i), 63));
    }
}
