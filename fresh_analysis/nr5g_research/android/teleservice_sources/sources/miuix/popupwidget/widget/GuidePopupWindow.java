package miuix.popupwidget.widget;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import miuix.core.util.WindowUtils;
import miuix.internal.util.ViewUtils;
import miuix.popupwidget.R$attr;
import miuix.popupwidget.R$dimen;
import miuix.popupwidget.R$id;
import miuix.popupwidget.R$layout;
import miuix.popupwidget.internal.widget.ArrowPopupView;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes2.dex */
public class GuidePopupWindow extends ArrowPopupWindow {
    private Runnable mDismissRunnable;
    private LinearLayout mGuideView;
    private int mShowDuration;
    private int mTextViewHeight;
    private int mTextViewWidth;
    private boolean mUseWrapContent;

    private boolean isBottomMode(int i) {
        return i == 16 || i == 18 || i == 17;
    }

    private boolean isTopMode(int i) {
        return i == 8 || i == 9 || i == 10;
    }

    public GuidePopupWindow(Context context) {
        super(context);
        this.mTextViewWidth = 0;
        this.mDismissRunnable = new Runnable() { // from class: miuix.popupwidget.widget.GuidePopupWindow.2
            @Override // java.lang.Runnable
            public void run() {
                GuidePopupWindow.this.dismiss(true);
            }
        };
    }

    @Override // miuix.popupwidget.widget.ArrowPopupWindow
    protected void onPrepareWindow() {
        super.onPrepareWindow();
        this.mShowDuration = 5000;
        setFocusable(true);
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R$layout.miuix_appcompat_guide_popup_content_view, (ViewGroup) null, false);
        this.mGuideView = linearLayout;
        setContentView(linearLayout);
        this.mArrowPopupView.enableShowingAnimation(false);
        setAccessibilityDelegate();
    }

    private void setAccessibilityDelegate() {
        this.mGuideView.setImportantForAccessibility(1);
        this.mGuideView.setFocusableInTouchMode(true);
        ViewCompat.setAccessibilityDelegate(this.mGuideView, new AccessibilityDelegateCompat() { // from class: miuix.popupwidget.widget.GuidePopupWindow.1
            @Override // androidx.core.view.AccessibilityDelegateCompat
            public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                boolean zDispatchPopulateAccessibilityEvent = super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
                if (shouldDismissForAccessibilityEvent(accessibilityEvent)) {
                    GuidePopupWindow.this.dismiss(true);
                }
                return zDispatchPopulateAccessibilityEvent;
            }

            @Override // androidx.core.view.AccessibilityDelegateCompat
            public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                super.onInitializeAccessibilityEvent(view, accessibilityEvent);
                if (shouldDismissForAccessibilityEvent(accessibilityEvent)) {
                    GuidePopupWindow.this.dismiss(true);
                }
            }

            private boolean shouldDismissForAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
                int eventType = accessibilityEvent.getEventType();
                return eventType == 256 || eventType == 65536;
            }
        });
    }

    public void setGuideText(String str) {
        addGuideTextView(str);
    }

    public void setGuideText(int i) {
        setGuideText(getContext().getString(i));
    }

    private void addGuideTextView(String str) {
        String[] strArrSplit;
        if (TextUtils.isEmpty(str) || (strArrSplit = str.split("\n")) == null || strArrSplit.length == 0) {
            return;
        }
        Point point = new Point();
        WindowUtils.getWindowSize(getContext(), point);
        for (String str2 : strArrSplit) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(getContext(), null, R$attr.guidePopupTextStyle);
            appCompatTextView.setMaxWidth(getContext().getResources().getDimensionPixelSize(R$dimen.miuix_popup_guide_text_view_max_width));
            appCompatTextView.setText(str2);
            appCompatTextView.setTextDirection(5);
            int[] textViewHeightAndWidth = getTextViewHeightAndWidth(appCompatTextView, point);
            this.mTextViewHeight += textViewHeightAndWidth[0];
            this.mTextViewWidth = Math.max(this.mTextViewWidth, textViewHeightAndWidth[1]);
            this.mGuideView.addView(appCompatTextView);
        }
    }

    private int[] getTextViewHeightAndWidth(View view, Point point) {
        view.measure(View.MeasureSpec.makeMeasureSpec(point.x, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(point.y, Integer.MIN_VALUE));
        return new int[]{view.getMeasuredHeight(), view.getMeasuredWidth()};
    }

    @Override // miuix.popupwidget.widget.ArrowPopupWindow
    public void show(View view, int i, int i2) {
        if (this.mUseWrapContent) {
            showWithWrapContent(view, i, i2);
        } else {
            super.show(view, i, i2);
        }
    }

    private void showWithWrapContent(View view, int i, int i2) {
        super.setSuperHeight(-2);
        int arrowMode = getArrowMode();
        super.setSuperWidth((isTopMode(arrowMode) || isBottomMode(arrowMode)) ? -1 : -2);
        int dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_guide_popup_horizontal_padding);
        int dimensionPixelOffset2 = getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_guide_popup_vertical_padding);
        int dimensionPixelOffset3 = getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_view_paddingStart);
        int dimensionPixelOffset4 = getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_view_paddingTop);
        Point point = new Point();
        WindowUtils.getWindowSize(getContext(), point);
        this.mArrowPopupView.measure(View.MeasureSpec.makeMeasureSpec(point.x, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(point.y, Integer.MIN_VALUE));
        LinearLayout linearLayout = (LinearLayout) this.mArrowPopupView.findViewById(R$id.content_wrapper);
        int measuredWidth = linearLayout.getMeasuredWidth();
        int measuredHeight = linearLayout.getMeasuredHeight();
        if (this.mTextViewWidth <= 0) {
            Math.max(0, (measuredWidth - (dimensionPixelOffset3 * 2)) - (dimensionPixelOffset * 2));
        }
        int iMax = this.mTextViewHeight;
        if (iMax <= 0) {
            iMax = Math.max(0, (measuredHeight - (dimensionPixelOffset4 * 2)) - (dimensionPixelOffset2 * 2));
        }
        int i3 = iMax;
        this.mArrowPopupView.setAnchor(view);
        this.mArrowPopupView.setOffset(i, i2);
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        int[] iArr2 = {this.mArrowPopupView.getArrowHeight(getArrowMode()), this.mArrowPopupView.getArrowWidth(getArrowMode())};
        int dimensionPixelOffset5 = getContext().getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_guide_popup_margin_horizontal);
        int[] iArrComputeWrapContentXY = computeWrapContentXY(view, iArr, i, i2, measuredWidth, measuredHeight, i3, iArr2[0], dimensionPixelOffset2, dimensionPixelOffset5);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
        applyWrapperMargins(layoutParams, i3, iArr2[0], dimensionPixelOffset4, dimensionPixelOffset2, dimensionPixelOffset5);
        linearLayout.setLayoutParams(layoutParams);
        showAtLocation(view, 8388659, iArrComputeWrapContentXY[0] - layoutParams.leftMargin, iArrComputeWrapContentXY[1] - layoutParams.topMargin);
        applyDisplayCutoutMode();
        this.mArrowPopupView.setAutoDismiss(getAutoDismiss());
        this.mArrowPopupView.animateToShow();
    }

    private int[] computeWrapContentXY(View view, int[] iArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = iArr[0] + i;
        int measuredHeight = iArr[1] + i2;
        int arrowMode = getArrowMode();
        if (isTopMode(arrowMode)) {
            measuredHeight += view.getMeasuredHeight() + i6;
        } else if (isBottomMode(arrowMode)) {
            measuredHeight -= (i5 + i6) + i7;
        } else if (arrowMode == 32 || arrowMode == 64) {
            i8 = i9 + computeSideXDelta(arrowMode, view, i3);
            measuredHeight -= (i4 - view.getMeasuredHeight()) / 2;
        } else {
            i8 = i9;
        }
        return new int[]{i8, measuredHeight};
    }

    private int computeSideXDelta(int i, View view, int i2) {
        int i3 = this.mRtlMode;
        return (i3 == 1 || (i3 == 2 && ViewUtils.isLayoutRtl(view))) ^ (i == 32) ? view.getMeasuredWidth() : -i2;
    }

    private void applyWrapperMargins(FrameLayout.LayoutParams layoutParams, int i, int i2, int i3, int i4, int i5) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int i6 = i3 * 2;
        int i7 = i4 * 2;
        if (displayMetrics.heightPixels >= (i * 5) + i2 + i6 + i7) {
            int i8 = i * 2;
            layoutParams.setMargins(i5, i8, i5, i8);
        } else {
            boolean zIsSideMode = isSideMode();
            ArrowPopupView arrowPopupView = this.mArrowPopupView;
            layoutParams.setMargins(i5, isSideMode() ? ((((displayMetrics.heightPixels - i) - i2) - i6) - i7) / 2 : this.mArrowPopupView.getPopupElevation(), i5, zIsSideMode ? arrowPopupView.getPopupElevation() : arrowPopupView.getPopupElevation() + i2);
        }
    }

    private boolean isSideMode() {
        return getArrowMode() == 32 || getArrowMode() == 64;
    }

    public void show(View view, int i, int i2, boolean z) {
        setAutoDismiss(z);
        show(view, i, i2);
        if (z) {
            this.mArrowPopupView.postDelayed(this.mDismissRunnable, this.mShowDuration);
        }
        if (HapticCompat.doesSupportHaptic("2.0")) {
            return;
        }
        HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_POPUP_LIGHT);
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        ArrowPopupView arrowPopupView = this.mArrowPopupView;
        if (arrowPopupView != null) {
            arrowPopupView.removeCallbacks(this.mDismissRunnable);
        }
        super.dismiss();
    }
}
