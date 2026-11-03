package miuix.appcompat.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import miuix.appcompat.R$dimen;
import miuix.core.util.MiuixUIUtils;
import miuix.internal.util.ViewUtils;
import miuix.view.DensityChangedHelper;

/* JADX INFO: loaded from: classes2.dex */
public class GroupButtonsPanel extends FrameLayout {
    private int mButtonGroupDividerSize;
    private int mButtonGroupMaxWidth;
    private LinearLayout mContentView;
    private boolean mHandleWindowInsetsEnabled;
    private int mOriginPaddingBottom;
    private int mOriginPaddingLeft;
    private int mOriginPaddingRight;
    private Runnable mResetPanelPaddingBottomRunnable;

    public GroupButtonsPanel(Context context) {
        super(context);
        this.mHandleWindowInsetsEnabled = true;
        init(context);
    }

    public GroupButtonsPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHandleWindowInsetsEnabled = true;
        init(context);
    }

    public GroupButtonsPanel(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHandleWindowInsetsEnabled = true;
        init(context);
    }

    public GroupButtonsPanel(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mHandleWindowInsetsEnabled = true;
        init(context);
    }

    private void init(Context context) {
        this.mButtonGroupMaxWidth = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_button_max_width);
        this.mButtonGroupDividerSize = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_group_buttons_divider_size);
        this.mOriginPaddingBottom = getPaddingBottom();
        this.mOriginPaddingLeft = getPaddingLeft();
        this.mOriginPaddingRight = getPaddingRight();
        this.mResetPanelPaddingBottomRunnable = new Runnable() { // from class: miuix.appcompat.app.GroupButtonsPanel$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GroupButtonsPanel.$r8$lambda$tY8zeean15UZnkow_PyE_S7bwLo(this.f$0);
            }
        };
        applyWindowInsets();
    }

    public static /* synthetic */ void $r8$lambda$tY8zeean15UZnkow_PyE_S7bwLo(GroupButtonsPanel groupButtonsPanel) {
        groupButtonsPanel.getClass();
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(groupButtonsPanel);
        ViewUtils.resetPaddingBottom(groupButtonsPanel, groupButtonsPanel.mOriginPaddingBottom + ((rootWindowInsets == null || !groupButtonsPanel.isLayoutHideNavigation()) ? 0 : rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom));
    }

    private boolean isLayoutHideNavigation() {
        return MiuixUIUtils.isLayoutHideNavigation(this);
    }

    private void applyWindowInsets() {
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() { // from class: miuix.appcompat.app.GroupButtonsPanel$$ExternalSyntheticLambda1
            @Override // miuix.internal.util.ViewUtils.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                return GroupButtonsPanel.m5444$r8$lambda$BcRXkBjOjxXF2_TF8hA0gyUSHc(this.f$0, view, windowInsetsCompat, relativePadding);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$BcRXkB-jOjxXF2_TF8hA0gyUSHc, reason: not valid java name */
    public static /* synthetic */ WindowInsetsCompat m5444$r8$lambda$BcRXkBjOjxXF2_TF8hA0gyUSHc(GroupButtonsPanel groupButtonsPanel, View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
        if (groupButtonsPanel.mHandleWindowInsetsEnabled) {
            groupButtonsPanel.post(groupButtonsPanel.mResetPanelPaddingBottomRunnable);
        }
        return windowInsetsCompat;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContentView = (LinearLayout) getChildAt(0);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        boolean z = this.mContentView.getOrientation() == 1;
        int size = (View.MeasureSpec.getSize(i) - this.mOriginPaddingLeft) - this.mOriginPaddingRight;
        int iMin = Math.min(this.mButtonGroupMaxWidth, size);
        int i3 = this.mButtonGroupMaxWidth;
        int i4 = i3 < size ? (size - i3) / 2 : 0;
        if (z) {
            resizeButtonTextSize(iMin);
        } else {
            int contentVisibleChildCount = getContentVisibleChildCount();
            if (contentVisibleChildCount >= 1) {
                resizeButtonTextSize((iMin - (this.mButtonGroupDividerSize * (contentVisibleChildCount - 1))) / contentVisibleChildCount);
            }
        }
        super.onMeasure(i, i2);
        if (i4 > 0) {
            measureChild(this.mContentView, View.MeasureSpec.makeMeasureSpec((size - (i4 * 2)) + this.mOriginPaddingLeft + this.mOriginPaddingRight, View.MeasureSpec.getMode(i)), i2);
        }
    }

    public int getContentVisibleChildCount() {
        int i = 0;
        for (int i2 = 0; i2 < this.mContentView.getChildCount(); i2++) {
            if (this.mContentView.getChildAt(i2).getVisibility() != 8) {
                i++;
            }
        }
        return i;
    }

    private void resizeButtonTextSize(int i) {
        for (int i2 = 0; i2 < this.mContentView.getChildCount(); i2++) {
            View childAt = this.mContentView.getChildAt(i2);
            if ((childAt instanceof Button) && childAt.getVisibility() == 0) {
                Button button = (Button) childAt;
                DensityChangedHelper.updateTextSizeSpUnit(button, 17.0f);
                if (isEllipsized(button, i)) {
                    for (int i3 = 0; i3 < this.mContentView.getChildCount(); i3++) {
                        View childAt2 = this.mContentView.getChildAt(i3);
                        if (childAt2 instanceof Button) {
                            DensityChangedHelper.updateTextSizeSpUnit((Button) childAt2, 14.0f);
                        }
                    }
                    return;
                }
            }
        }
    }

    private boolean isEllipsized(Button button, int i) {
        return ((int) button.getPaint().measureText(button.getText().toString())) > Math.min(i, Math.max(0, (button.getMaxWidth() - button.getPaddingStart()) - button.getPaddingEnd()));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.mResetPanelPaddingBottomRunnable;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.mResetPanelPaddingBottomRunnable = null;
        }
    }
}
