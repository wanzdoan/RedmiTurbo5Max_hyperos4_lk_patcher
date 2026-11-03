package miuix.internal.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import miuix.appcompat.R$dimen;
import miuix.appcompat.app.AlertDialog;
import miuix.autodensity.DensityUtil;
import miuix.core.util.EnvStateManager;
import miuix.core.util.MiuixUIUtils;
import miuix.os.Build;
import miuix.os.DeviceHelper;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes2.dex */
public class AlertActionSheet extends AlertDialog implements ActionSheet.IActionSheet {
    final ActionSheetController mActionController;
    private View mArrowActionAnchor;
    private ActionSheet.ArrowMode mArrowMode;
    private ActionSheet.ContentController mContentController;
    protected Context mContext;
    private int mFreeFormPhoneCompatHeight;
    private int mFreeFormTabletCompatHeight;
    private boolean mIsDismissForShift;
    private boolean mIsFlipTinyScreen;
    private boolean mIsFromArrowShape;
    private int mMaxFixedWidth;
    private int mNormalMargin;
    private int mSmallMargin;

    protected AlertActionSheet(Context context) {
        this(context, 0);
    }

    protected AlertActionSheet(Context context, int i) {
        super(context, i);
        this.mContext = context;
        this.mActionController = new ActionSheetController(context, this, getWindow(), ActionSheet.ActionSheetMode.ALERT_MODE);
        init(context);
    }

    private void init(Context context) {
        setContentController();
        this.mSmallMargin = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_dialog_width_small_margin);
        this.mNormalMargin = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_dialog_width_margin);
        this.mMaxFixedWidth = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_action_sheet_content_alert_max_fixed_width);
        this.mFreeFormPhoneCompatHeight = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_dialog_freeform_bottom_height_phone_t);
        this.mFreeFormTabletCompatHeight = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_dialog_freeform_bottom_height_tablet_t);
        this.mIsFlipTinyScreen = Build.IS_FLIP && DeviceHelper.isTinyScreen(context);
    }

    public void setHapticFeedbackEnabled(boolean z) {
        this.mActionController.mHapticFeedbackEnabled = z;
    }

    public boolean isHapticFeedbackEnabled() {
        return this.mActionController.mHapticFeedbackEnabled;
    }

    private void setContentController() {
        ActionSheet.ContentController contentController = new ActionSheet.ContentController() { // from class: miuix.internal.widget.AlertActionSheet.1
            @Override // miuix.internal.widget.ActionSheet.ContentController
            public int calcContentPanelHeight(Context context, ViewGroup viewGroup, ViewGroup viewGroup2, int i, WindowInsets windowInsets) {
                return -2;
            }

            @Override // miuix.internal.widget.ActionSheet.ContentController
            public View getArrowAnchor() {
                return null;
            }

            @Override // miuix.internal.widget.ActionSheet.ContentController
            public ActionSheet.ArrowMode getArrowMode() {
                return ActionSheet.ArrowMode.ARROW_MODE_NONE;
            }

            @Override // miuix.internal.widget.ActionSheet.ContentController
            public Point computeContentPosition(Rect rect, Point point, Point point2, ViewGroup viewGroup, ViewGroup viewGroup2) {
                int i = (point.x - point2.x) / 2;
                Point point3 = new Point();
                int iMax = Math.max(rect.left, i);
                int i2 = point.x;
                int i3 = point2.x;
                int i4 = (i2 - iMax) - i3;
                int i5 = rect.right;
                if (i4 < i5) {
                    iMax = (i2 - i5) - i3;
                }
                point3.x = iMax;
                point3.y = point.y - (point2.y + rect.bottom);
                return point3;
            }

            @Override // miuix.internal.widget.ActionSheet.ContentController
            public int calcContentPanelWidth(Context context, ViewGroup viewGroup, ViewGroup viewGroup2, int i, WindowInsets windowInsets) {
                int i2;
                int i3;
                int iMax;
                boolean z;
                int iMax2;
                int i4;
                int iMax3;
                int i5 = 0;
                if (windowInsets != null) {
                    Insets insets = windowInsets.getInsets(WindowInsets.Type.systemBars());
                    Insets insets2 = windowInsets.getInsets(WindowInsets.Type.displayCutout());
                    int i6 = insets.left;
                    int i7 = insets.right + i6;
                    int i8 = insets2.left;
                    int i9 = i7 + insets2.right + i8;
                    iMax = Math.max(i6, i8);
                    z = iMax == insets2.left;
                    int iMax4 = Math.max(insets.right, insets2.right);
                    i5 = iMax4 == insets2.right ? 1 : 0;
                    i3 = iMax4;
                    i2 = i5;
                    i5 = i9;
                } else {
                    i2 = 0;
                    i3 = 0;
                    iMax = 0;
                    z = false;
                }
                int iPx2dp = MiuixUIUtils.px2dp(context, i - i5);
                if (iPx2dp > 0 && iPx2dp <= 360) {
                    iMax2 = (!z || AlertActionSheet.this.mIsFlipTinyScreen) ? AlertActionSheet.this.mSmallMargin + iMax : Math.max(iMax, AlertActionSheet.this.mSmallMargin);
                    if (i2 == 0 || AlertActionSheet.this.mIsFlipTinyScreen) {
                        i4 = AlertActionSheet.this.mSmallMargin;
                        iMax3 = i4 + i3;
                    } else {
                        iMax3 = Math.max(i3, AlertActionSheet.this.mSmallMargin);
                    }
                } else {
                    if (360 >= iPx2dp || iPx2dp > 404) {
                        return AlertActionSheet.this.mMaxFixedWidth;
                    }
                    iMax2 = (!z || AlertActionSheet.this.mIsFlipTinyScreen) ? AlertActionSheet.this.mNormalMargin + iMax : Math.max(iMax, AlertActionSheet.this.mNormalMargin);
                    if (i2 == 0 || AlertActionSheet.this.mIsFlipTinyScreen) {
                        i4 = AlertActionSheet.this.mNormalMargin;
                        iMax3 = i4 + i3;
                    } else {
                        iMax3 = Math.max(i3, AlertActionSheet.this.mNormalMargin);
                    }
                }
                return (i - iMax2) - iMax3;
            }

            private int getAvailableWidthDp(Context context, int i, WindowInsets windowInsets) {
                int i2;
                if (windowInsets != null) {
                    Insets insets = windowInsets.getInsets(WindowInsets.Type.systemBars());
                    Insets insets2 = windowInsets.getInsets(WindowInsets.Type.displayCutout());
                    i2 = insets.left + insets.right + insets2.left + insets2.right;
                } else {
                    i2 = 0;
                }
                return MiuixUIUtils.px2dp(context, i - i2);
            }

            private int getBaseMargin(boolean z, int i) {
                if (z) {
                    return AlertActionSheet.this.mSmallMargin;
                }
                if (i > 0 && i <= 360) {
                    return AlertActionSheet.this.mSmallMargin;
                }
                if (360 >= i || i > 404) {
                    return 0;
                }
                return AlertActionSheet.this.mNormalMargin;
            }

            @Override // miuix.internal.widget.ActionSheet.ContentController
            public int[] calcHorizontalMargin(Context context, int i, WindowInsets windowInsets) {
                int iMax;
                int baseMargin = getBaseMargin(Build.IS_FLIP && DeviceHelper.isTinyScreen(context), getAvailableWidthDp(context, i, windowInsets));
                if (windowInsets != null) {
                    Insets insets = windowInsets.getInsets(WindowInsets.Type.systemBars());
                    int iMax2 = Math.max(baseMargin, insets.left);
                    int iMax3 = Math.max(baseMargin, insets.right);
                    Insets insets2 = windowInsets.getInsets(WindowInsets.Type.displayCutout());
                    int iMax4 = Math.max(iMax2, insets2.left);
                    iMax = Math.max(iMax3, insets2.right);
                    boolean z = iMax4 == insets2.left;
                    boolean z2 = iMax == insets2.right;
                    if (z && AlertActionSheet.this.mIsFlipTinyScreen) {
                        iMax4 += baseMargin;
                    }
                    if (z2 && AlertActionSheet.this.mIsFlipTinyScreen) {
                        iMax += baseMargin;
                    }
                    if (!z) {
                        int i2 = insets.left;
                        iMax4 = iMax4 == i2 ? i2 + baseMargin : baseMargin;
                    }
                    if (!z2) {
                        int i3 = insets.right;
                        if (iMax == i3) {
                            baseMargin += i3;
                        }
                        iMax = baseMargin;
                    }
                    baseMargin = iMax4;
                } else {
                    iMax = baseMargin;
                }
                return new int[]{baseMargin, iMax};
            }

            @Override // miuix.internal.widget.ActionSheet.ContentController
            public int[] calcVerticalMargin(Context context, int i, WindowInsets windowInsets) {
                Insets insetsIgnoringVisibility;
                int i2 = (Build.IS_FLIP && DeviceHelper.isTinyScreen(context)) ? AlertActionSheet.this.mSmallMargin : AlertActionSheet.this.mNormalMargin;
                if (windowInsets == null) {
                    return new int[]{i2, i2};
                }
                ActionSheetController actionSheetController = AlertActionSheet.this.mActionController;
                if (actionSheetController != null && actionSheetController.mIsAssociatedActivityNavigationBarHidden) {
                    insetsIgnoringVisibility = windowInsets.getInsets(WindowInsets.Type.systemBars());
                } else {
                    insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
                }
                Insets insets = windowInsets.getInsets(WindowInsets.Type.displayCutout());
                boolean zIsFreeFormMode = EnvStateManager.isFreeFormMode(context);
                int iMax = Math.max(Math.max(insetsIgnoringVisibility.top, insets.top), i2);
                boolean z = Build.IS_TABLET;
                AlertActionSheet alertActionSheet = AlertActionSheet.this;
                int i3 = z ? alertActionSheet.mFreeFormTabletCompatHeight : alertActionSheet.mFreeFormPhoneCompatHeight;
                int i4 = insetsIgnoringVisibility.bottom;
                if (i4 != 0 || !zIsFreeFormMode) {
                    i3 = i4;
                }
                return new int[]{iMax, i2 + Math.max(i3, insets.bottom)};
            }
        };
        this.mContentController = contentController;
        ActionSheetController actionSheetController = this.mActionController;
        if (actionSheetController != null) {
            actionSheetController.setContentController(contentController);
        }
    }

    @Override // miuix.appcompat.app.AlertDialog
    public ListView getListView() {
        return this.mActionController.getListView();
    }

    @Override // miuix.appcompat.app.AlertDialog
    public TextView getMessageView() {
        return this.mActionController.getMessageView();
    }

    @Override // miuix.appcompat.app.AlertDialog
    public void setMessage(CharSequence charSequence) {
        this.mActionController.setMessage(charSequence);
    }

    public void setSeparateButtonText(CharSequence charSequence) {
        this.mActionController.setSeparateButtonText(charSequence);
    }

    public void setActionItems(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
        this.mActionController.setActionItems(charSequenceArr, onClickListener);
    }

    public void setActionItems(CharSequence[] charSequenceArr, ActionSheet.ActionSheetItemType[] actionSheetItemTypeArr, DialogInterface.OnClickListener onClickListener) {
        this.mActionController.setActionItems(charSequenceArr, actionSheetItemTypeArr, onClickListener);
    }

    public void setSeparateClickListener(DialogInterface.OnClickListener onClickListener) {
        this.mActionController.setSeparateButtonClickListener(onClickListener);
    }

    @Override // miuix.appcompat.app.AlertDialog, androidx.appcompat.app.AppCompatDialog, androidx.activity.ComponentDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        AlertDialog.LifecycleOwnerCompat lifecycleOwnerCompat;
        if (isSystemSpecialUiThread() && (lifecycleOwnerCompat = this.mLifecycleOwnerCompat) != null) {
            lifecycleOwnerCompat.onCreate();
        }
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(0);
        }
        superOnCreate(bundle);
        this.mActionController.installContent(bundle);
    }

    protected ArrowActionSheet createArrowActionSheet(View view) {
        this.mArrowActionAnchor = view;
        ArrowActionSheet arrowActionSheet = new ArrowActionSheet(this.mContext, view);
        arrowActionSheet.setArrowMode(this.mArrowMode);
        arrowActionSheet.setHapticFeedbackEnabled(isHapticFeedbackEnabled());
        arrowActionSheet.setCanceledOnTouchOutside(isCanceledOnTouchOutside());
        if (this.mActionController.getMessage() != null) {
            arrowActionSheet.setMessage(this.mActionController.getMessage());
        }
        if (this.mActionController.getActionItems() != null && this.mActionController.getItemClickListener() != null) {
            arrowActionSheet.setActionItems(this.mActionController.getActionItems(), this.mActionController.getItemClickListener());
        }
        if (this.mActionController.getActionItems() != null && this.mActionController.getItemClickListener() != null && this.mActionController.getItemTypes() != null) {
            arrowActionSheet.setActionItems(this.mActionController.getActionItems(), this.mActionController.getItemTypes(), this.mActionController.getItemClickListener());
        }
        if (this.mActionController.getShowAnimListener() != null) {
            arrowActionSheet.setOnShowAnimListener(this.mActionController.getShowAnimListener());
        }
        if (this.mActionController.getOnShowListener() != null) {
            arrowActionSheet.setActionSheetOnShowListener(this.mActionController.getOnShowListener());
        }
        if (this.mActionController.getOnDismissListener() != null) {
            arrowActionSheet.setActionSheetOnDismissListener(this.mActionController.getOnDismissListener());
        }
        if (this.mActionController.getOnKeyListener() != null) {
            arrowActionSheet.setOnKeyListener(this.mActionController.getOnKeyListener());
            arrowActionSheet.setActionSheetOnKeyListener(this.mActionController.getOnKeyListener());
        }
        if (this.mActionController.getListViewAdapter() != null) {
            arrowActionSheet.setListViewAdapter(this.mActionController.getListViewAdapter());
        }
        if (this.mActionController.getOnCancelListener() != null) {
            arrowActionSheet.setOnCancelListener(this.mActionController.getOnCancelListener());
            arrowActionSheet.setActionSheetOnCancelListener(this.mActionController.getOnCancelListener());
        }
        this.mActionController.getConfigurationChangedListener();
        this.mActionController.getItemProvider();
        return arrowActionSheet;
    }

    protected void setIsFromArrowShape(boolean z) {
        this.mIsFromArrowShape = z;
    }

    protected boolean isFromArrowShape() {
        return this.mIsFromArrowShape;
    }

    protected void setIsDismissForShift(boolean z) {
        this.mIsDismissForShift = z;
    }

    protected boolean isDismissForShift() {
        return this.mIsDismissForShift;
    }

    public void setArrowActionAnchor(View view) {
        this.mArrowActionAnchor = view;
    }

    public View getArrowActionAnchor() {
        return this.mArrowActionAnchor;
    }

    public void setArrowMode(ActionSheet.ArrowMode arrowMode) {
        this.mArrowMode = arrowMode;
    }

    public ActionSheet.ArrowMode getArrowMode() {
        return this.mArrowMode;
    }

    public void setListViewAdapter(ListAdapter listAdapter) {
        this.mActionController.setListViewAdapter(listAdapter);
    }

    @Override // miuix.appcompat.app.AlertDialog, android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        superOnAttachedToWindow();
        Window window = getWindow();
        View decorView = window != null ? window.getDecorView() : null;
        if (this.mActionController.mHapticFeedbackEnabled && decorView != null) {
            HapticCompat.performHapticFeedbackAsync(decorView, HapticFeedbackConstants.MIUI_ALERT, HapticFeedbackConstants.MIUI_POPUP_NORMAL);
        }
        this.mActionController.onAttachedToWindow();
        setAccessibilityDelegate(decorView);
    }

    @Override // miuix.appcompat.app.AlertDialog, androidx.appcompat.app.AppCompatDialog, android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (this.mActionController.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        return miuixSuperDispatchKeyEvent(keyEvent);
    }

    @Override // miuix.appcompat.app.AlertDialog, androidx.activity.ComponentDialog, android.app.Dialog
    protected void onStart() {
        superOnStart();
        this.mActionController.onStart();
    }

    @Override // miuix.appcompat.app.AlertDialog, androidx.appcompat.app.AppCompatDialog, androidx.activity.ComponentDialog, android.app.Dialog
    protected void onStop() {
        AlertDialog.LifecycleOwnerCompat lifecycleOwnerCompat;
        AlertDialog.LifecycleOwnerCompat lifecycleOwnerCompat2;
        if (isSystemSpecialUiThread() && (lifecycleOwnerCompat2 = this.mLifecycleOwnerCompat) != null) {
            lifecycleOwnerCompat2.onStopBefore();
        }
        superOnStop();
        if (!isSystemSpecialUiThread() || (lifecycleOwnerCompat = this.mLifecycleOwnerCompat) == null) {
            return;
        }
        lifecycleOwnerCompat.onStopAfter();
    }

    @Override // miuix.appcompat.app.AlertDialog, android.app.Dialog
    public void show() {
        superShow();
    }

    @Override // miuix.appcompat.app.AlertDialog, android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        superOnDetachedFromWindow();
        this.mActionController.onDetachedFromWindow();
    }

    @Override // miuix.appcompat.app.AlertDialog, androidx.appcompat.app.AppCompatDialog, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        Window window = getWindow();
        View decorView = window != null ? window.getDecorView() : null;
        boolean zIsShowingAnimation = this.mActionController.isShowingAnimation();
        ActionSheetController actionSheetController = this.mActionController;
        if (zIsShowingAnimation) {
            actionSheetController.setPendingDismiss(true);
            return;
        }
        actionSheetController.setPendingDismiss(false);
        if ((decorView != null ? DensityUtil.findAutoDensityContextWrapper(decorView.getContext()) : null) != null) {
            EnvStateManager.removeInfoOfContext(decorView.getContext());
        }
        Activity associatedActivity = getAssociatedActivity();
        if (associatedActivity != null && associatedActivity.isFinishing()) {
            dismissIfAttachedToWindow(decorView);
        } else {
            dismissWithAnimationOrNot(decorView);
        }
    }

    @Override // miuix.appcompat.app.AlertDialog
    protected void dismissWithAnimationExistDecorView(View view) {
        if (Thread.currentThread() == view.getHandler().getLooper().getThread()) {
            this.mActionController.dismiss(this.mOnDismiss);
        } else {
            view.post(new Runnable() { // from class: miuix.internal.widget.AlertActionSheet$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AlertActionSheet alertActionSheet = this.f$0;
                    alertActionSheet.mActionController.dismiss(alertActionSheet.mOnDismiss);
                }
            });
        }
    }

    public void setEnableEnterAnim(boolean z) {
        this.mActionController.setEnableEnterAnim(z);
    }

    protected void dismissForShiftWithoutAnimation() {
        setIsDismissForShift(true);
        dismissWithoutAnimation();
    }

    public void dismissWithoutAnimation() {
        Window window = getWindow();
        View decorView = window != null ? window.getDecorView() : null;
        if (decorView == null || decorView.isAttachedToWindow()) {
            boolean zIsShowingAnimation = this.mActionController.isShowingAnimation();
            ActionSheetController actionSheetController = this.mActionController;
            if (zIsShowingAnimation) {
                actionSheetController.setPendingDismiss(true);
                return;
            }
            actionSheetController.setPendingDismiss(false);
            if ((decorView != null ? DensityUtil.findAutoDensityContextWrapper(decorView.getContext()) : null) != null) {
                EnvStateManager.removeInfoOfContext(decorView.getContext());
            }
            realDismiss();
        }
    }

    @Override // miuix.appcompat.app.AlertDialog, android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z) {
        this.mActionController.setCanceledOnTouchOutside(z);
    }

    public boolean isCanceledOnTouchOutside() {
        return this.mActionController.isCanceledOnTouchOutside();
    }

    @Override // miuix.appcompat.app.AlertDialog
    public void setOnShowAnimListener(AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener) {
        this.mActionController.setShowAnimListener(onDialogShowAnimListener);
    }

    public void setActionSheetOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.mActionController.setOnShowListener(onShowListener);
    }

    public void setActionSheetOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mActionController.setOnDismissListener(onDismissListener);
    }

    public void setActionSheetOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        this.mActionController.setOnKeyListener(onKeyListener);
    }

    public void setActionSheetOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.mActionController.setOnCancelListener(onCancelListener);
    }
}
