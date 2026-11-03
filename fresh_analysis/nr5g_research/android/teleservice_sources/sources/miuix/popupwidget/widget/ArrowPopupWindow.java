package miuix.popupwidget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import miuix.popupwidget.R$dimen;
import miuix.popupwidget.R$layout;
import miuix.popupwidget.internal.widget.ArrowPopupView;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ArrowPopupWindow extends android.widget.PopupWindow {
    protected ArrowPopupView mArrowPopupView;
    private boolean mAutoDismiss;
    private Context mContext;
    private int mListViewMaxHeight;
    private int mMaxAvailableHeight;
    protected int mRtlMode;

    protected void onPrepareWindow() {
    }

    public ArrowPopupWindow(Context context) {
        this(context, null);
    }

    public ArrowPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ArrowPopupWindow(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ArrowPopupWindow(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mRtlMode = 2;
        this.mContext = context;
        this.mAutoDismiss = true;
        setupPopupWindow();
        this.mArrowPopupView.setLayoutRtlMode(this.mRtlMode);
    }

    public void setAutoDismiss(boolean z) {
        this.mAutoDismiss = z;
    }

    public boolean getAutoDismiss() {
        return this.mAutoDismiss;
    }

    public Context getContext() {
        return this.mContext;
    }

    private void setupPopupWindow() {
        this.mListViewMaxHeight = this.mContext.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_arrow_popup_window_list_max_height);
        ArrowPopupView arrowPopupView = (ArrowPopupView) getLayoutInflater().inflate(R$layout.miuix_appcompat_arrow_popup_view, (ViewGroup) null, false);
        this.mArrowPopupView = arrowPopupView;
        super.setContentView(arrowPopupView);
        super.setWidth(-1);
        super.setHeight(-1);
        setSoftInputMode(3);
        this.mArrowPopupView.setArrowPopupWindow(this);
        super.setTouchInterceptor(getDefaultOnTouchListener());
        onPrepareWindow();
        update();
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(this.mContext);
    }

    @Override // android.widget.PopupWindow
    public final void setContentView(View view) {
        this.mArrowPopupView.setContentView(view);
    }

    @Override // android.widget.PopupWindow
    public View getContentView() {
        return this.mArrowPopupView.getContentView();
    }

    public int getArrowMode() {
        return this.mArrowPopupView.getArrowMode();
    }

    public void setArrowMode(int i) {
        this.mArrowPopupView.setArrowMode(i);
    }

    public void show(View view, int i, int i2) {
        this.mArrowPopupView.setAnchor(view);
        this.mArrowPopupView.setOffset(i, i2);
        showAtLocation(view, 8388659, 0, 0);
        applyDisplayCutoutMode();
        this.mArrowPopupView.setAutoDismiss(this.mAutoDismiss);
        this.mArrowPopupView.animateToShow();
    }

    protected void applyDisplayCutoutMode() {
        View rootView = this.mArrowPopupView.getRootView();
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        if (layoutParams instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams layoutParams2 = (WindowManager.LayoutParams) layoutParams;
            layoutParams2.layoutInDisplayCutoutMode = 3;
            ((WindowManager) this.mContext.getSystemService(WindowManager.class)).updateViewLayout(rootView, layoutParams2);
        }
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i, int i2) {
        show(view, i, i2);
    }

    public void dismiss(boolean z) {
        if (z) {
            this.mArrowPopupView.animateToDismiss();
        } else {
            dismiss();
        }
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i, int i2, int i3) {
        show(view, i, i2);
    }

    @Override // android.widget.PopupWindow
    public void update(int i, int i2, int i3, int i4, boolean z) {
        super.update(0, 0, -2, -2, z);
        if (i4 >= 0) {
            setContentHeight(i4);
        }
    }

    @Override // android.widget.PopupWindow
    public void setTouchInterceptor(View.OnTouchListener onTouchListener) {
        this.mArrowPopupView.setTouchInterceptor(onTouchListener);
    }

    @Override // android.widget.PopupWindow
    public int getWidth() {
        return getContentWidth();
    }

    public int getContentWidth() {
        View contentView = getContentView();
        if (contentView != null) {
            return contentView.getWidth();
        }
        return 0;
    }

    @Override // android.widget.PopupWindow
    public void setWidth(int i) {
        setContentWidth(i);
    }

    protected void setSuperWidth(int i) {
        super.setWidth(i);
    }

    public void setContentWidth(int i) {
        View contentView = getContentView();
        if (contentView != null) {
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            layoutParams.width = i;
            contentView.setLayoutParams(layoutParams);
        }
    }

    @Override // android.widget.PopupWindow
    public int getHeight() {
        return getContentHeight();
    }

    public int getContentHeight() {
        View contentView = getContentView();
        if (contentView != null) {
            return contentView.getHeight();
        }
        return 0;
    }

    @Override // android.widget.PopupWindow
    public void setHeight(int i) {
        setContentHeight(i);
    }

    protected void setSuperHeight(int i) {
        super.setHeight(i);
    }

    public void setContentHeight(int i) {
        int i2;
        if (i == this.mMaxAvailableHeight) {
            i -= this.mArrowPopupView.getContentFrameWrapperBottomPadding() + this.mArrowPopupView.getContentFrameWrapperTopPadding();
        }
        if (!this.mArrowPopupView.isTitleEmpty()) {
            i -= this.mArrowPopupView.getTitleHeight();
        }
        View contentView = getContentView();
        if ((contentView instanceof ListView) && i > (i2 = this.mListViewMaxHeight)) {
            i = i2;
        }
        if (contentView != null) {
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            layoutParams.height = i;
            contentView.setLayoutParams(layoutParams);
        }
    }

    public View.OnTouchListener getDefaultOnTouchListener() {
        return this.mArrowPopupView;
    }
}
