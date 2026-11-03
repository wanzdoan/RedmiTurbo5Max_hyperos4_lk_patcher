package miuix.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.preference.PreferenceViewHolder;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimSpecialConfig;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;
import miuix.stretchablewidget.WidgetContainer;

/* JADX INFO: loaded from: classes2.dex */
public class StretchableWidgetPreference extends BasePreference {
    private View mButtonLine;
    private WidgetContainer mContainer;
    private String mDetailMsgResId;
    private TextView mDetailMsgView;
    private int mHeight;
    private boolean mIsExpand;
    private ImageView mStateImage;
    private TextView mTitle;
    private View mTopLine;
    private RelativeLayout mTopView;

    public StretchableWidgetPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHeight = 0;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.StretchableWidgetPreference, i, 0);
        this.mDetailMsgResId = typedArrayObtainStyledAttributes.getString(R$styleable.StretchableWidgetPreference_detail_message);
        this.mIsExpand = typedArrayObtainStyledAttributes.getBoolean(R$styleable.StretchableWidgetPreference_expand_state, false);
        typedArrayObtainStyledAttributes.recycle();
    }

    public StretchableWidgetPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.stretchableWidgetPreferenceStyle);
    }

    @Override // miuix.preference.BasePreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.mTopView = (RelativeLayout) view.findViewById(R$id.top_view);
        WidgetContainer widgetContainer = (WidgetContainer) view.findViewById(R.id.widget_frame);
        this.mContainer = widgetContainer;
        widgetContainer.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        this.mHeight = this.mContainer.getMeasuredHeight();
        this.mTitle = (TextView) view.findViewById(R$id.title);
        this.mDetailMsgView = (TextView) view.findViewById(R$id.detail_msg_text);
        ImageView imageView = (ImageView) view.findViewById(R$id.state_image);
        this.mStateImage = imageView;
        imageView.setBackgroundResource(R$drawable.miuix_stretchable_widget_state_collapse);
        this.mButtonLine = view.findViewById(R$id.button_line);
        this.mTopLine = view.findViewById(R$id.top_line);
        setDetailMsgText(this.mDetailMsgResId);
        setState(this.mIsExpand);
        this.mTopView.setOnClickListener(new View.OnClickListener() { // from class: miuix.preference.StretchableWidgetPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                StretchableWidgetPreference.this.stateChangeAnim(view2);
            }
        });
        if (isAccessibilityEnabled()) {
            ViewCompat.setAccessibilityDelegate(this.mTopView, new AccessibilityDelegateCompat() { // from class: miuix.preference.StretchableWidgetPreference.2
                @Override // androidx.core.view.AccessibilityDelegateCompat
                public void onInitializeAccessibilityNodeInfo(View view2, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(view2, accessibilityNodeInfoCompat);
                    accessibilityNodeInfoCompat.setStateDescription(StretchableWidgetPreference.this.getStateInfo());
                    accessibilityNodeInfoCompat.setCheckable(true);
                    accessibilityNodeInfoCompat.setChecked(StretchableWidgetPreference.this.mIsExpand);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getStateInfo() {
        if (this.mIsExpand) {
            return getContext().getString(R$string.miuix_appcompat_accessibility_expand_state);
        }
        return getContext().getString(R$string.miuix_appcompat_accessibility_collapse_state);
    }

    public void setState(boolean z) {
        ImageView imageView = this.mStateImage;
        if (z) {
            imageView.setBackgroundResource(R$drawable.miuix_stretchable_widget_state_expand);
            this.mButtonLine.setVisibility(0);
            this.mTopLine.setVisibility(0);
        } else {
            imageView.setBackgroundResource(R$drawable.miuix_stretchable_widget_state_collapse);
            this.mButtonLine.setVisibility(8);
            this.mTopLine.setVisibility(8);
        }
        setContainerAmin(z);
    }

    private void setContainerAmin(boolean z) {
        String str = "start";
        IStateStyle iStateStyleAdd = Folme.useValue(this.mContainer).setup("start").add("widgetHeight", this.mHeight);
        ViewProperty viewProperty = ViewProperty.ALPHA;
        iStateStyleAdd.add((FloatProperty) viewProperty, 1.0f).setup("end").add("widgetHeight", 0).add((FloatProperty) viewProperty, 0.0f);
        IStateStyle iStateStyleUseValue = Folme.useValue(this.mContainer);
        if (!z) {
            str = "end";
        }
        iStateStyleUseValue.setTo(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stateChangeAnim(View view) {
        boolean z = this.mIsExpand;
        this.mIsExpand = !z;
        if (!z) {
            Folme.useValue(this.mContainer).to("start", new AnimConfig().setFromSpeed(0.0f).setSpecial(ViewProperty.ALPHA, (AnimSpecialConfig) new AnimSpecialConfig().setEase(-2, 1.0f, 0.2f)));
            this.mStateImage.setBackgroundResource(miuix.stretchablewidget.R$drawable.miuix_stretchable_widget_state_expand);
            this.mButtonLine.setVisibility(0);
            this.mTopLine.setVisibility(0);
        } else {
            Folme.useValue(this.mContainer).to("end", new AnimConfig().setFromSpeed(0.0f).setSpecial(ViewProperty.ALPHA, (AnimSpecialConfig) new AnimSpecialConfig().setEase(-2, 1.0f, 0.2f)));
            this.mStateImage.setBackgroundResource(miuix.stretchablewidget.R$drawable.miuix_stretchable_widget_state_collapse);
            this.mButtonLine.setVisibility(8);
            this.mTopLine.setVisibility(8);
        }
        if (isAccessibilityEnabled()) {
            view.announceForAccessibility(getStateInfo());
        }
    }

    public void setDetailMsgText(String str) {
        this.mDetailMsgView.setText(str);
    }
}
