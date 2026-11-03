package miuix.miuixbasewidget.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import miuix.miuixbasewidget.R$attr;
import miuix.miuixbasewidget.R$dimen;
import miuix.miuixbasewidget.R$id;
import miuix.miuixbasewidget.R$layout;
import miuix.miuixbasewidget.R$string;
import miuix.miuixbasewidget.R$style;
import miuix.miuixbasewidget.R$styleable;
import miuix.util.HapticFeedbackCompat;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes2.dex */
public abstract class FilterSortView2 extends HorizontalScrollView {

    public class TabView extends FrameLayout {
        private int mActivatedTextAppearanceId;
        private ImageView mArrow;
        private Drawable mArrowIcon;
        private boolean mDescending;
        private boolean mDescendingEnabled;
        private boolean mFiltered;
        private HapticFeedbackCompat mHapticFeedbackCompat;
        private int mIndicatorVisibility;
        private int mOriginalBgAlpha;
        private int mTextAppearanceId;
        private TextView mTextView;

        private void updateMaterialForFilteredChange(FilterSortView2 filterSortView2) {
        }

        public TabView(Context context) {
            this(context, null);
        }

        public TabView(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, R$attr.filterSortTabView2Style);
        }

        public TabView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            this.mDescendingEnabled = true;
            this.mOriginalBgAlpha = -1;
            LayoutInflater.from(context).inflate(getTabLayoutResource(), (ViewGroup) this, true);
            TextView textView = (TextView) findViewById(R.id.text1);
            this.mTextView = textView;
            textView.setMaxLines(1);
            this.mTextView.setEllipsize(TextUtils.TruncateAt.END);
            this.mArrow = (ImageView) findViewById(R$id.arrow);
            if (attributeSet != null) {
                TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.FilterSortTabView2, i, R$style.Widget_FilterSortTabView2_DayNight);
                String string = typedArrayObtainStyledAttributes.getString(R$styleable.FilterSortTabView2_android_text);
                boolean z = typedArrayObtainStyledAttributes.getBoolean(R$styleable.FilterSortTabView2_descending, true);
                this.mIndicatorVisibility = typedArrayObtainStyledAttributes.getInt(R$styleable.FilterSortTabView2_indicatorVisibility, 0);
                this.mArrowIcon = typedArrayObtainStyledAttributes.getDrawable(R$styleable.FilterSortTabView2_arrowFilterSortTabView);
                setBackground(typedArrayObtainStyledAttributes.getDrawable(R$styleable.FilterSortTabView2_filterSortTabViewBackground));
                setForeground(typedArrayObtainStyledAttributes.getDrawable(R$styleable.FilterSortTabView2_filterSortTabViewForeground));
                int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.FilterSortTabView2_filterSortTabViewHorizontalPadding, R$dimen.miuix_appcompat_filter_sort_tab_view2_padding_horizontal);
                int dimensionPixelSize2 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R$styleable.FilterSortTabView2_filterSortTabViewVerticalPadding, R$dimen.miuix_appcompat_filter_sort_tab_view2_padding_vertical);
                findViewById(R$id.container).setPadding(dimensionPixelSize, dimensionPixelSize2, dimensionPixelSize, dimensionPixelSize2);
                this.mTextAppearanceId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.FilterSortTabView2_filterSortTabViewTabTextAppearance, 0);
                this.mActivatedTextAppearanceId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.FilterSortTabView2_filterSortTabViewTabActivatedTextAppearance, 0);
                typedArrayObtainStyledAttributes.recycle();
                initView(string, z);
            }
            if (getId() == -1) {
                setId(FrameLayout.generateViewId());
            }
            setImportantForAccessibility(1);
            ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() { // from class: miuix.miuixbasewidget.widget.FilterSortView2.TabView.1
                @Override // androidx.core.view.AccessibilityDelegateCompat
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                    accessibilityNodeInfoCompat.setSelected(view.isActivated());
                    TextView textView2 = (TextView) view.findViewById(R.id.text1);
                    if (textView2 != null && !TextUtils.isEmpty(textView2.getText())) {
                        accessibilityNodeInfoCompat.setContentDescription(textView2.getText());
                    }
                    if (view.isActivated()) {
                        accessibilityNodeInfoCompat.setClickable(false);
                        accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
                    } else {
                        accessibilityNodeInfoCompat.setClickable(true);
                        accessibilityNodeInfoCompat.setStateDescription(TabView.this.getContext().getResources().getString(R$string.accessibility_tab_state_description_unselect));
                    }
                }
            });
        }

        private void updateTextAppearance() {
            if (this.mTextView != null) {
                boolean zIsFiltered = isFiltered();
                TextView textView = this.mTextView;
                if (zIsFiltered) {
                    TextViewCompat.setTextAppearance(textView, this.mActivatedTextAppearanceId);
                } else {
                    TextViewCompat.setTextAppearance(textView, this.mTextAppearanceId);
                }
                requestLayout();
            }
        }

        protected int getTabLayoutResource() {
            return R$layout.miuix_appcompat_filter_sort_tab_view_2;
        }

        public TextView getTextView() {
            return this.mTextView;
        }

        public ImageView getIconView() {
            return this.mArrow;
        }

        protected void initView(CharSequence charSequence, boolean z) {
            this.mArrow.setBackground(this.mArrowIcon);
            this.mTextView.setText(charSequence);
            this.mArrow.setVisibility(this.mIndicatorVisibility);
            setDescending(z);
            updateTextAppearance();
        }

        private void setFiltered(boolean z) {
            TabView tabView;
            ViewGroup viewGroup = (ViewGroup) getParent();
            findOwnerFilterSortView();
            if (z && viewGroup != null) {
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = viewGroup.getChildAt(i);
                    if ((childAt instanceof TabView) && (tabView = (TabView) childAt) != this && tabView.mFiltered) {
                        tabView.setFiltered(false);
                    }
                }
            }
            this.mFiltered = z;
            updateTextAppearance();
            this.mTextView.setActivated(z);
            this.mArrow.setActivated(z);
            setActivated(z);
            updateMaterialForFilteredChange(null);
            if (viewGroup != null && z) {
                viewGroup.post(new Runnable() { // from class: miuix.miuixbasewidget.widget.FilterSortView2$TabView$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.getClass();
                    }
                });
            }
        }

        private FilterSortView2 findOwnerFilterSortView() {
            for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            }
            return null;
        }

        public boolean isFiltered() {
            return this.mFiltered;
        }

        private void setDescending(boolean z) {
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
            super.setOnClickListener(new View.OnClickListener() { // from class: miuix.miuixbasewidget.widget.FilterSortView2$TabView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    FilterSortView2.TabView.$r8$lambda$kOztkJtwiaKbkpWO95G48q4mePE(this.f$0, onClickListener, view);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$kOztkJtwiaKbkpWO95G48q4mePE(TabView tabView, View.OnClickListener onClickListener, View view) {
            if (tabView.mFiltered) {
                if (tabView.mDescendingEnabled) {
                    tabView.setDescending(!tabView.mDescending);
                }
            } else {
                tabView.setFiltered(true);
            }
            onClickListener.onClick(view);
            if (HapticCompat.doesSupportHaptic("2.0")) {
                tabView.getHapticFeedbackCompat().performExtHapticFeedback(204);
            } else {
                HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_MESH_NORMAL);
            }
        }

        private HapticFeedbackCompat getHapticFeedbackCompat() {
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
}
