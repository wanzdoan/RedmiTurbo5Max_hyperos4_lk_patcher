package miuix.appcompat.app;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialog;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.DefaultTaskExecutor;
import androidx.arch.core.executor.TaskExecutor;
import androidx.core.view.ViewCompat;
import java.lang.reflect.InvocationTargetException;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$string;
import miuix.appcompat.widget.DialogAnimHelper;
import miuix.autodensity.DensityUtil;
import miuix.core.util.EnvStateManager;
import miuix.internal.widget.ActionSheet;
import miuix.reflect.ReflectionHelper;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes2.dex */
public class AlertDialog extends AppCompatDialog implements DialogInterface {
    public static final int[] DIALOG_CONTENT_LAYOUT = {R$layout.miuix_appcompat_alert_dialog_content, R$layout.miuix_appcompat_alert_dialog_content_land};
    final AlertController mAlert;
    private Runnable mCheckDismissAction;
    protected LifecycleOwnerCompat mLifecycleOwnerCompat;
    protected DialogAnimHelper.OnDismiss mOnDismiss;
    private CharSequence mShowDescription;

    public interface OnConfigurationChangedListener {
    }

    public interface OnDialogShowAnimListener {
        void onShowAnimComplete();

        void onShowAnimStart();
    }

    public interface OnPanelSizeChangedListener {
    }

    protected void onLayoutReload() {
    }

    protected AlertDialog(Context context) {
        this(context, 0);
    }

    protected AlertDialog(Context context, int i) {
        super(context, resolveDialogTheme(context, i));
        this.mCheckDismissAction = null;
        this.mOnDismiss = new DialogAnimHelper.OnDismiss() { // from class: miuix.appcompat.app.AlertDialog$$ExternalSyntheticLambda0
            @Override // miuix.appcompat.widget.DialogAnimHelper.OnDismiss
            public final void end() {
                AlertDialog.m5438$r8$lambda$iUMZrLPak1F3An7zG0mBgdfx4(this.f$0);
            }
        };
        Context context2 = parseContext(context);
        if (DensityUtil.findAutoDensityContextWrapper(context2) != null) {
            EnvStateManager.removeInfoOfContext(context);
        }
        this.mAlert = new AlertController(context2, this, getWindow());
        this.mLifecycleOwnerCompat = new LifecycleOwnerCompat();
        this.mShowDescription = context.getResources().getString(R$string.miuix_appcompat_show_dialog_description);
        prepareCheckDismissAction();
    }

    private void prepareCheckDismissAction() {
        final View decorView;
        Window window = getWindow();
        if (window == null || (decorView = window.getDecorView()) == null) {
            return;
        }
        this.mCheckDismissAction = new Runnable() { // from class: miuix.appcompat.app.AlertDialog$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                AlertDialog.$r8$lambda$jo_FCVjpKmd5ScvHMNTrT_iCcPc(this.f$0, decorView);
            }
        };
    }

    public static /* synthetic */ void $r8$lambda$jo_FCVjpKmd5ScvHMNTrT_iCcPc(AlertDialog alertDialog, View view) {
        boolean z;
        if (alertDialog.mAlert.mIsDebugEnabled) {
            Log.d("MiuixAlertDialog", "prepareCheckDismissAction: dialog isShowing = " + alertDialog.isShowing() + ", decorView.isAttachedToWindow = " + view.isAttachedToWindow());
        }
        if (view.isAttachedToWindow()) {
            super.dismiss();
            z = true;
        } else {
            z = false;
        }
        if (alertDialog.mAlert.mIsDebugEnabled && z) {
            Log.d("MiuixAlertDialog", "prepareCheckDismissAction: ==> run super.dismiss()");
        }
    }

    private Context parseContext(Context context) {
        if (context == null) {
            return getContext();
        }
        return context.getClass() == ContextThemeWrapper.class ? context : getContext();
    }

    static int resolveDialogTheme(Context context, int i) {
        if (((i >>> 24) & 255) >= 1) {
            return i;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R$attr.miuiAlertDialogTheme, typedValue, true);
        return typedValue.resourceId;
    }

    public Button getButton(int i) {
        return this.mAlert.getButton(i);
    }

    public ListView getListView() {
        return this.mAlert.getListView();
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        this.mAlert.setTitle(charSequence);
    }

    public void setCustomTitle(View view) {
        this.mAlert.setCustomTitle(view);
    }

    public void setMessage(CharSequence charSequence) {
        this.mAlert.setMessage(charSequence);
    }

    public TextView getMessageView() {
        return this.mAlert.getMessageView();
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }

    public void setButton(int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        this.mAlert.setButton(i, charSequence, onClickListener, null);
    }

    public void setIcon(Drawable drawable) {
        this.mAlert.setIcon(drawable);
    }

    public void setIconAttribute(int i) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(i, typedValue, true);
        this.mAlert.setIcon(typedValue.resourceId);
    }

    @Override // androidx.appcompat.app.AppCompatDialog, androidx.activity.ComponentDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        LifecycleOwnerCompat lifecycleOwnerCompat;
        if (isSystemSpecialUiThread() && (lifecycleOwnerCompat = this.mLifecycleOwnerCompat) != null) {
            lifecycleOwnerCompat.onCreate();
        }
        if (this.mAlert.isDialogImmersive() || !this.mAlert.mEnableEnterAnim) {
            getWindow().setWindowAnimations(0);
        }
        super.onCreate(bundle);
        this.mAlert.installContent(bundle);
    }

    protected void superOnCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public boolean isChecked() {
        return this.mAlert.isChecked();
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (this.mAlert.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean miuixSuperDispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // androidx.activity.ComponentDialog, android.app.Dialog
    protected void onStart() {
        super.onStart();
        this.mAlert.onStart();
    }

    protected void superOnStart() {
        super.onStart();
    }

    @Override // androidx.appcompat.app.AppCompatDialog, androidx.activity.ComponentDialog, android.app.Dialog
    protected void onStop() {
        LifecycleOwnerCompat lifecycleOwnerCompat;
        LifecycleOwnerCompat lifecycleOwnerCompat2;
        if (isSystemSpecialUiThread() && (lifecycleOwnerCompat2 = this.mLifecycleOwnerCompat) != null) {
            lifecycleOwnerCompat2.onStopBefore();
        }
        super.onStop();
        this.mAlert.onStop();
        if (!isSystemSpecialUiThread() || (lifecycleOwnerCompat = this.mLifecycleOwnerCompat) == null) {
            return;
        }
        lifecycleOwnerCompat.onStopAfter();
    }

    protected void superOnStop() {
        super.onStop();
    }

    @Override // android.app.Dialog
    public void show() {
        removeCheckDismissAction("show");
        this.mAlert.mShowUpTimeMillis = SystemClock.uptimeMillis();
        super.show();
        if (getWindow() == null || this.mAlert.isDialogImmersive()) {
            return;
        }
        getWindow().getDecorView().postDelayed(new Runnable() { // from class: miuix.appcompat.app.AlertDialog$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AlertDialog.m5437$r8$lambda$UVc2xUPAgOOfKzUqly8vz_ZEBM(this.f$0);
            }
        }, this.mAlert.mNonImmersiveDialogShowAnimDuration);
    }

    /* JADX INFO: renamed from: $r8$lambda$UVc2xUPAgOOfKzUqly8vz_ZEB-M, reason: not valid java name */
    public static /* synthetic */ void m5437$r8$lambda$UVc2xUPAgOOfKzUqly8vz_ZEBM(AlertDialog alertDialog) {
        if (alertDialog.mAlert.hasPendingDismiss()) {
            alertDialog.dismiss();
        }
    }

    public void superShow() {
        super.show();
    }

    public class Builder {
        private final AlertController.AlertParams P;
        private boolean mActionSheetEnabled;
        private final int mTheme;

        public Builder(Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(Context context, int i) {
            this.mActionSheetEnabled = false;
            this.P = new AlertController.AlertParams(new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, i)));
            this.mTheme = i;
        }

        public Context getContext() {
            return this.P.mContext;
        }

        public Builder setTitle(int i) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mTitle = alertParams.mContext.getText(i);
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.P.mTitle = charSequence;
            return this;
        }

        public Builder setCustomTitle(View view) {
            this.P.mCustomTitleView = view;
            return this;
        }

        public Builder setMessage(int i) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mMessage = alertParams.mContext.getText(i);
            return this;
        }

        public Builder setMessage(CharSequence charSequence) {
            this.P.mMessage = charSequence;
            return this;
        }

        public Builder setCheckBox(boolean z, CharSequence charSequence) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mIsChecked = z;
            alertParams.mCheckBoxMessage = charSequence;
            return this;
        }

        public Builder setIcon(int i) {
            this.P.mIconId = i;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.P.mIcon = drawable;
            return this;
        }

        public Builder setIconAttribute(int i) {
            TypedValue typedValue = new TypedValue();
            this.P.mContext.getTheme().resolveAttribute(i, typedValue, true);
            this.P.mIconId = typedValue.resourceId;
            return this;
        }

        public Builder setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mPositiveButtonText = alertParams.mContext.getText(i);
            this.P.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mPositiveButtonText = charSequence;
            alertParams.mPositiveButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mNegativeButtonText = alertParams.mContext.getText(i);
            this.P.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mNegativeButtonText = charSequence;
            alertParams.mNegativeButtonListener = onClickListener;
            return this;
        }

        public Builder setNeutralButton(int i, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mNeutralButtonText = alertParams.mContext.getText(i);
            this.P.mNeutralButtonListener = onClickListener;
            return this;
        }

        public Builder setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mNeutralButtonText = charSequence;
            alertParams.mNeutralButtonListener = onClickListener;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.P.mCancelable = z;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            this.P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setAdapter(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mAdapter = listAdapter;
            alertParams.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] charSequenceArr, boolean[] zArr, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mItems = charSequenceArr;
            alertParams.mOnCheckboxClickListener = onMultiChoiceClickListener;
            alertParams.mCheckedItems = zArr;
            alertParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] charSequenceArr, int i, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mItems = charSequenceArr;
            alertParams.mOnClickListener = onClickListener;
            alertParams.mCheckedItem = i;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter listAdapter, int i, DialogInterface.OnClickListener onClickListener) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mAdapter = listAdapter;
            alertParams.mOnClickListener = onClickListener;
            alertParams.mCheckedItem = i;
            alertParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setEnableDialogImmersive(boolean z) {
            this.P.mEnableDialogImmersive = z;
            return this;
        }

        public Builder setButtonForceVertical(boolean z) {
            this.P.mButtonForceVertical = z;
            return this;
        }

        public Builder setView(View view) {
            AlertController.AlertParams alertParams = this.P;
            alertParams.mView = view;
            alertParams.mViewLayoutResId = 0;
            return this;
        }

        public AlertDialog create() {
            AlertController.AlertParams alertParams = this.P;
            boolean z = false;
            boolean z2 = (alertParams.mItems == null || alertParams.mIsSingleChoice || alertParams.mIsMultiChoice) ? false : true;
            if (alertParams.mView == null && alertParams.mViewLayoutResId == 0) {
                z = true;
            }
            if (this.mActionSheetEnabled && z2 && z) {
                return createActionSheet();
            }
            return createAlertDialog();
        }

        private AlertDialog createActionSheet() {
            ActionSheet.Builder builder = new ActionSheet.Builder(this.P.mContext);
            if (!TextUtils.isEmpty(this.P.mMessage)) {
                builder.setMessage(this.P.mMessage);
            }
            if (!TextUtils.isEmpty(this.P.mTitle)) {
                builder.setMessage(this.P.mTitle);
            }
            AlertController.AlertParams alertParams = this.P;
            builder.setActionItems(alertParams.mItems, alertParams.mOnClickListener);
            if (!TextUtils.isEmpty(this.P.mNegativeButtonText)) {
                builder.setSeparateText(this.P.mNegativeButtonText);
            }
            DialogInterface.OnClickListener onClickListener = this.P.mNegativeButtonListener;
            if (onClickListener != null) {
                builder.setSeparateClickListener(onClickListener);
            }
            OnDialogShowAnimListener onDialogShowAnimListener = this.P.mOnDialogShowAnimListener;
            if (onDialogShowAnimListener != null) {
                builder.setShowAnimListener(onDialogShowAnimListener);
            }
            DialogInterface.OnShowListener onShowListener = this.P.mOnShowListener;
            if (onShowListener != null) {
                builder.setOnShowListener(onShowListener);
            }
            DialogInterface.OnDismissListener onDismissListener = this.P.mOnDismissListener;
            if (onDismissListener != null) {
                builder.setOnDismissListener(onDismissListener);
            }
            DialogInterface.OnKeyListener onKeyListener = this.P.mOnKeyListener;
            if (onKeyListener != null) {
                builder.setOnKeyListener(onKeyListener);
            }
            ListAdapter listAdapter = this.P.mAdapter;
            if (listAdapter != null) {
                builder.setListViewAdapter(listAdapter);
            }
            this.P.getClass();
            AlertDialog alertDialog = (AlertDialog) builder.create();
            if (this.P.mCancelable) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            DialogInterface.OnCancelListener onCancelListener = this.P.mOnCancelListener;
            if (onCancelListener != null) {
                alertDialog.setOnCancelListener(onCancelListener);
            }
            this.P.getClass();
            return alertDialog;
        }

        private AlertDialog createAlertDialog() {
            AlertDialog alertDialog = new AlertDialog(this.P.mContext, this.mTheme);
            this.P.apply(alertDialog.mAlert);
            alertDialog.setCancelable(this.P.mCancelable);
            if (this.P.mCancelable) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            alertDialog.setOnCancelListener(this.P.mOnCancelListener);
            alertDialog.setOnDismissListener(this.P.mOnDismissListener);
            alertDialog.setOnShowListener(this.P.mOnShowListener);
            alertDialog.setOnShowAnimListener(this.P.mOnDialogShowAnimListener);
            DialogInterface.OnKeyListener onKeyListener = this.P.mOnKeyListener;
            if (onKeyListener != null) {
                alertDialog.setOnKeyListener(onKeyListener);
            }
            return alertDialog;
        }

        public AlertDialog show() {
            AlertDialog alertDialogCreate = create();
            alertDialogCreate.show();
            return alertDialogCreate;
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        View decorView = getWindow().getDecorView();
        if (decorView != null && this.mAlert.mHapticFeedbackEnabled) {
            HapticCompat.performHapticFeedbackAsync(decorView, HapticFeedbackConstants.MIUI_ALERT, HapticFeedbackConstants.MIUI_POPUP_NORMAL);
        }
        this.mAlert.onAttachedToWindow();
        setAccessibilityDelegate(decorView);
    }

    protected void superOnAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void setAccessibilityDelegate(View view) {
        if (view == null) {
            return;
        }
        ViewCompat.setAccessibilityPaneTitle(view, this.mShowDescription);
    }

    @Override // android.app.Dialog
    public void setCancelable(boolean z) {
        super.setCancelable(z);
        this.mAlert.setCancelable(z);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAlert.onDetachedFromWindow();
        removeCheckDismissAction("onDetachedFromWindow");
    }

    private void removeCheckDismissAction(String str) {
        View decorView;
        if (this.mAlert.mIsDebugEnabled) {
            Log.d("MiuixAlertDialog", "removeCheckDismissAction: ==> remove action at: " + str);
        }
        Window window = getWindow();
        if (this.mCheckDismissAction == null || window == null || (decorView = window.getDecorView()) == null) {
            return;
        }
        decorView.removeCallbacks(this.mCheckDismissAction);
    }

    public void superOnDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /* JADX INFO: renamed from: $r8$lambda$iUMZrLP-ak-1F3An7zG0mBgdfx4, reason: not valid java name */
    public static /* synthetic */ void m5438$r8$lambda$iUMZrLPak1F3An7zG0mBgdfx4(AlertDialog alertDialog) {
        View decorView;
        if (alertDialog.getWindow() == null || (decorView = alertDialog.getWindow().getDecorView()) == null || !decorView.isAttachedToWindow()) {
            return;
        }
        alertDialog.realDismiss();
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (!this.mAlert.isDialogImmersive() && this.mAlert.isAsyncInflatePanelEnabled()) {
            this.mAlert.safeRemovePanelFromParent();
        }
        View decorView = getWindow().getDecorView();
        boolean zIsShowingAnimation = this.mAlert.isShowingAnimation();
        AlertController alertController = this.mAlert;
        if (zIsShowingAnimation) {
            alertController.setPendingDismiss(true);
            return;
        }
        alertController.setPendingDismiss(false);
        if (DensityUtil.findAutoDensityContextWrapper(decorView.getContext()) != null) {
            EnvStateManager.removeInfoOfContext(decorView.getContext());
        }
        if (this.mAlert.isDialogImmersive()) {
            Activity associatedActivity = getAssociatedActivity();
            if (associatedActivity != null && associatedActivity.isFinishing()) {
                dismissIfAttachedToWindow(decorView);
                return;
            } else {
                dismissWithAnimationOrNot(decorView);
                return;
            }
        }
        dismissIfAttachedToWindow(decorView);
    }

    protected void dismissWithAnimationOrNot(View view) {
        if (view != null) {
            if (view.getHandler() != null) {
                dismissWithAnimationExistDecorView(view);
                return;
            } else {
                dismissIfAttachedToWindow(view);
                return;
            }
        }
        super.dismiss();
    }

    protected void dismissWithAnimationExistDecorView(View view) {
        if (Thread.currentThread() == view.getHandler().getLooper().getThread()) {
            this.mAlert.dismiss(this.mOnDismiss);
        } else {
            view.post(new Runnable() { // from class: miuix.appcompat.app.AlertDialog$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AlertDialog alertDialog = this.f$0;
                    alertDialog.mAlert.dismiss(alertDialog.mOnDismiss);
                }
            });
        }
    }

    protected void dismissIfAttachedToWindow(View view) {
        if (view != null && !view.isAttachedToWindow()) {
            postCheckAndDismissSafely(view);
        } else {
            super.dismiss();
        }
    }

    private void postCheckAndDismissSafely(View view) {
        if (view == null) {
            return;
        }
        try {
            if (this.mAlert.mIsDebugEnabled) {
                Log.d("MiuixAlertDialog", "postCheckAndDismissSafely: mCheckDismissAction = " + this.mCheckDismissAction);
            }
            Runnable runnable = this.mCheckDismissAction;
            if (runnable != null) {
                view.post(runnable);
            }
        } catch (Exception unused) {
            Log.d("MiuixAlertDialog", "postCheckAndDismissSafely: decorView.post dismiss failed");
        }
    }

    public void realDismiss() {
        super.dismiss();
    }

    public Activity getAssociatedActivity() {
        Activity ownerActivity = getOwnerActivity();
        Context context = getContext();
        while (ownerActivity == null && context != null) {
            if (context instanceof Activity) {
                ownerActivity = (Activity) context;
            } else {
                context = context instanceof ContextWrapper ? ((ContextWrapper) context).getBaseContext() : null;
            }
        }
        return ownerActivity;
    }

    public void setEnableImmersive(boolean z) {
        this.mAlert.setEnableImmersive(z);
    }

    @Override // android.app.Dialog
    public void setCanceledOnTouchOutside(boolean z) {
        super.setCanceledOnTouchOutside(z);
        this.mAlert.setCanceledOnTouchOutside(z);
    }

    public void setOnShowAnimListener(OnDialogShowAnimListener onDialogShowAnimListener) {
        this.mAlert.setShowAnimListener(onDialogShowAnimListener);
    }

    protected boolean isSystemSpecialUiThread() {
        return TextUtils.equals("android.ui", Thread.currentThread().getName()) || TextUtils.equals("android.imms", Thread.currentThread().getName()) || TextUtils.equals("system_server", Thread.currentThread().getName());
    }

    public class LifecycleOwnerCompat {
        private Object mOriginalExecutor;
        private TaskExecutor mSpecialUiExecutor;

        protected LifecycleOwnerCompat() {
        }

        public void onCreate() {
            try {
                Object fieldValue = ReflectionHelper.getFieldValue(ArchTaskExecutor.class, ArchTaskExecutor.getInstance(), "mDelegate");
                if (fieldValue != null) {
                    this.mOriginalExecutor = fieldValue;
                }
            } catch (IllegalAccessException e) {
                Log.d("MiuixDialog", "onCreate() taskExecutor get failed IllegalAccessException " + e);
            } catch (NoSuchMethodException e2) {
                Log.d("MiuixDialog", "onCreate() taskExecutor get failed NoSuchMethodException " + e2);
            } catch (InvocationTargetException e3) {
                Log.d("MiuixDialog", "onCreate() taskExecutor get failed InvocationTargetException " + e3);
            } finally {
                this.mSpecialUiExecutor = createSpecialUiTaskExecutor();
                ArchTaskExecutor.getInstance().setDelegate(this.mSpecialUiExecutor);
            }
        }

        /* JADX WARN: Code duplicated, block: B:27:0x0059  */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r7v10 */
        /* JADX WARN: Type inference failed for: r7v6 */
        /* JADX WARN: Type inference failed for: r7v7 */
        /* JADX WARN: Type inference failed for: r7v9 */
        public void onStopBefore() {
            try {
                try {
                    try {
                        Object fieldValue = ReflectionHelper.getFieldValue(ArchTaskExecutor.class, ArchTaskExecutor.getInstance(), "mDelegate");
                        if (fieldValue != null && fieldValue != this.mOriginalExecutor) {
                            this.mOriginalExecutor = fieldValue;
                        }
                        if (fieldValue != this.mSpecialUiExecutor || !ArchTaskExecutor.getInstance().isMainThread()) {
                            this = this;
                            ArchTaskExecutor.getInstance().setDelegate(this.mSpecialUiExecutor);
                            return;
                        }
                    } catch (IllegalAccessException e) {
                        Log.d("MiuixDialog", "onStop() taskExecutor get failed IllegalAccessException " + e);
                        if (this.mSpecialUiExecutor != null || !ArchTaskExecutor.getInstance().isMainThread()) {
                            this = this;
                            this = this;
                            this = this;
                            ArchTaskExecutor archTaskExecutor = ArchTaskExecutor.getInstance();
                            TaskExecutor taskExecutor = this.mSpecialUiExecutor;
                            archTaskExecutor.setDelegate(taskExecutor);
                            this = taskExecutor;
                        }
                    }
                } catch (NoSuchMethodException e2) {
                    Log.d("MiuixDialog", "onStop() taskExecutor get failed NoSuchMethodException " + e2);
                    if (this.mSpecialUiExecutor != null || !ArchTaskExecutor.getInstance().isMainThread()) {
                        this = this;
                        this = this;
                        this = this;
                        ArchTaskExecutor archTaskExecutor2 = ArchTaskExecutor.getInstance();
                        TaskExecutor taskExecutor2 = this.mSpecialUiExecutor;
                        archTaskExecutor2.setDelegate(taskExecutor2);
                        this = taskExecutor2;
                    }
                } catch (InvocationTargetException e3) {
                    Log.d("MiuixDialog", "onStop() taskExecutor get failed InvocationTargetException " + e3);
                    if (this.mSpecialUiExecutor != null || !ArchTaskExecutor.getInstance().isMainThread()) {
                        this = this;
                        this = this;
                        this = this;
                        ArchTaskExecutor archTaskExecutor3 = ArchTaskExecutor.getInstance();
                        TaskExecutor taskExecutor3 = this.mSpecialUiExecutor;
                        archTaskExecutor3.setDelegate(taskExecutor3);
                        this = taskExecutor3;
                    }
                }
                this = this;
                this = this;
                this = this;
                this = this;
            } catch (Throwable th) {
                if (this.mSpecialUiExecutor != null || !ArchTaskExecutor.getInstance().isMainThread()) {
                    ArchTaskExecutor.getInstance().setDelegate(this.mSpecialUiExecutor);
                }
                throw th;
            }
        }

        public void onStopAfter() {
            if (this.mOriginalExecutor instanceof TaskExecutor) {
                ArchTaskExecutor.getInstance().setDelegate((TaskExecutor) this.mOriginalExecutor);
            }
        }

        private TaskExecutor createSpecialUiTaskExecutor() {
            return new DefaultTaskExecutor() { // from class: miuix.appcompat.app.AlertDialog.LifecycleOwnerCompat.1
                private final Object mLock = new Object();
                private volatile Handler mSpecialMainHandler;

                @Override // androidx.arch.core.executor.DefaultTaskExecutor, androidx.arch.core.executor.TaskExecutor
                public boolean isMainThread() {
                    return true;
                }

                @Override // androidx.arch.core.executor.DefaultTaskExecutor, androidx.arch.core.executor.TaskExecutor
                public void postToMainThread(Runnable runnable) {
                    if (this.mSpecialMainHandler == null) {
                        synchronized (this.mLock) {
                            try {
                                if (this.mSpecialMainHandler == null) {
                                    this.mSpecialMainHandler = createAsync(Looper.myLooper());
                                }
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                    this.mSpecialMainHandler.post(runnable);
                }

                private Handler createAsync(Looper looper) {
                    return Handler.createAsync(looper);
                }
            };
        }
    }
}
