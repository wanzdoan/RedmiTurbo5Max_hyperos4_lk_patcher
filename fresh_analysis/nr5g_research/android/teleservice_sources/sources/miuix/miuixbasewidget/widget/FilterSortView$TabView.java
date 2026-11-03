package miuix.miuixbasewidget.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import miuix.miuixbasewidget.R$drawable;
import miuix.miuixbasewidget.R$id;
import miuix.miuixbasewidget.R$layout;
import miuix.miuixbasewidget.R$string;
import miuix.miuixbasewidget.R$style;
import miuix.miuixbasewidget.R$styleable;
import miuix.util.HapticFeedbackCompat;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes2.dex */
public class FilterSortView$TabView extends LinearLayout {
    private ImageView mArrow;
    private Drawable mArrowIcon;
    private boolean mDescending;
    private boolean mDescendingEnabled;
    private boolean mFiltered;
    private HapticFeedbackCompat mHapticFeedbackCompat;
    private int mIndicatorVisibility;
    private int mSelectedTextAppearanceId;
    private int mTextAppearanceId;
    private ColorStateList mTextColor;
    private TextView mTextView;

    public static /* synthetic */ boolean $r8$lambda$shXBe2vVndEFyEYzvzz0ARNSJEs(FilterSortView$TabView filterSortView$TabView, View view, MotionEvent motionEvent) {
        filterSortView$TabView.getClass();
        return false;
    }

    public FilterSortView$TabView(Context context) {
        this(context, null);
    }

    public FilterSortView$TabView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FilterSortView$TabView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDescendingEnabled = true;
        int tabLayoutResource = getTabLayoutResource();
        LayoutInflater.from(context).inflate(tabLayoutResource, (ViewGroup) this, true);
        this.mTextView = (TextView) findViewById(R.id.text1);
        this.mArrow = (ImageView) findViewById(R$id.arrow);
        this.mTextView.setImportantForAccessibility(2);
        this.mArrow.setImportantForAccessibility(2);
        if (tabLayoutResource == R$layout.miuix_appcompat_filter_sort_tab_view) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.FilterSortTabView, i, R$style.Widget_FilterSortTabView_DayNight);
            String string = typedArrayObtainStyledAttributes.getString(R$styleable.FilterSortTabView_android_text);
            boolean z = typedArrayObtainStyledAttributes.getBoolean(R$styleable.FilterSortTabView_descending, true);
            this.mIndicatorVisibility = typedArrayObtainStyledAttributes.getInt(R$styleable.FilterSortTabView_indicatorVisibility, 0);
            this.mArrowIcon = typedArrayObtainStyledAttributes.getDrawable(R$styleable.FilterSortTabView_arrowFilterSortTabView);
            this.mTextColor = typedArrayObtainStyledAttributes.getColorStateList(R$styleable.FilterSortTabView_filterSortTabViewTextColor);
            this.mTextAppearanceId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.FilterSortTabView_filterSortTabViewTabTextAppearance, 0);
            this.mSelectedTextAppearanceId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.FilterSortTabView_filterSortTabViewTabActivatedTextAppearance, 0);
            typedArrayObtainStyledAttributes.recycle();
            initView(string, z);
        }
        this.mArrow.setVisibility(this.mIndicatorVisibility);
        if (getId() == -1) {
            setId(LinearLayout.generateViewId());
        }
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() { // from class: miuix.miuixbasewidget.widget.FilterSortView$TabView.1
            @Override // androidx.core.view.AccessibilityDelegateCompat
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                if (FilterSortView$TabView.this.mTextView != null && !TextUtils.isEmpty(FilterSortView$TabView.this.mTextView.getText())) {
                    accessibilityNodeInfoCompat.setContentDescription(FilterSortView$TabView.this.mTextView.getText());
                }
                accessibilityNodeInfoCompat.setSelected(view.isSelected());
                if (view.isSelected()) {
                    accessibilityNodeInfoCompat.setClickable(false);
                    accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
                } else {
                    accessibilityNodeInfoCompat.setClickable(true);
                    accessibilityNodeInfoCompat.setStateDescription(FilterSortView$TabView.this.getContext().getResources().getString(R$string.accessibility_tab_state_description_unselect));
                }
            }
        });
    }

    protected int getTabLayoutResource() {
        return R$layout.miuix_appcompat_filter_sort_tab_view;
    }

    protected void initView(CharSequence charSequence, boolean z) {
        updateTextAppearance(false);
        setGravity(17);
        if (getBackground() == null) {
            setBackground(parseBackground());
        }
        this.mArrow.setBackground(this.mArrowIcon);
        ColorStateList colorStateList = this.mTextColor;
        if (colorStateList != null) {
            this.mTextView.setTextColor(colorStateList);
        }
        this.mTextView.setText(charSequence);
        setDescending(z);
        setOnHoverListener(new View.OnHoverListener() { // from class: miuix.miuixbasewidget.widget.FilterSortView$TabView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnHoverListener
            public final boolean onHover(View view, MotionEvent motionEvent) {
                return FilterSortView$TabView.$r8$lambda$shXBe2vVndEFyEYzvzz0ARNSJEs(this.f$0, view, motionEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFiltered(boolean z) {
        setFiltered(z, true);
    }

    private void setFiltered(boolean z, boolean z2) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(getParent());
        this.mFiltered = z;
        updateTextAppearance(z);
        this.mTextView.setSelected(z);
        this.mArrow.setSelected(z);
        setSelected(z);
    }

    private void updateTextAppearance(boolean z) {
        TextView textView = this.mTextView;
        if (textView != null) {
            if (z) {
                TextViewCompat.setTextAppearance(textView, this.mSelectedTextAppearanceId);
            } else {
                TextViewCompat.setTextAppearance(textView, this.mTextAppearanceId);
            }
        }
    }

    private Drawable parseBackground() {
        return getResources().getDrawable(R$drawable.miuix_appcompat_filter_sort_tab_view_bg_normal);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDescending(boolean z) {
        this.mDescending = z;
        ImageView imageView = this.mArrow;
        if (z) {
            imageView.setRotationX(0.0f);
        } else {
            imageView.setRotationX(180.0f);
        }
    }

    @Override // android.view.View
    public void setOnClickListener(final View.OnClickListener onClickListener) {
        super.setOnClickListener(new View.OnClickListener() { // from class: miuix.miuixbasewidget.widget.FilterSortView$TabView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                boolean z = FilterSortView$TabView.this.mFiltered;
                FilterSortView$TabView filterSortView$TabView = FilterSortView$TabView.this;
                if (z) {
                    if (filterSortView$TabView.mDescendingEnabled) {
                        FilterSortView$TabView filterSortView$TabView2 = FilterSortView$TabView.this;
                        filterSortView$TabView2.setDescending(!filterSortView$TabView2.mDescending);
                    }
                } else {
                    filterSortView$TabView.setFiltered(true);
                }
                onClickListener.onClick(view);
                if (HapticCompat.doesSupportHaptic("2.0")) {
                    FilterSortView$TabView.this.getHapticFeedbackCompat().performExtHapticFeedback(204);
                } else {
                    HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_MESH_NORMAL);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HapticFeedbackCompat getHapticFeedbackCompat() {
        if (this.mHapticFeedbackCompat == null) {
            this.mHapticFeedbackCompat = new HapticFeedbackCompat(getContext());
        }
        return this.mHapticFeedbackCompat;
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mTextView.setEnabled(z);
    }
}
