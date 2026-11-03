package miuix.androidbasewidget.widget;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Switch;
import androidx.core.content.ContextCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import miuix.androidbasewidget.R$attr;
import miuix.androidbasewidget.R$drawable;
import miuix.androidbasewidget.R$string;
import miuix.internal.util.AttributeResolver;

/* JADX INFO: loaded from: classes2.dex */
public class PasswordWidgetManager extends StateEditText.WidgetManager {
    private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};
    private Context mContext;
    private boolean mIsChecked;
    private StateEditText mMaster;
    private Drawable mWidgetDrawable;

    public void onAttached(StateEditText stateEditText) {
    }

    public PasswordWidgetManager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.mIsChecked = false;
        Drawable drawableResolveDrawable = AttributeResolver.resolveDrawable(context, R$attr.miuixAppcompatVisibilityIcon);
        this.mWidgetDrawable = drawableResolveDrawable;
        if (drawableResolveDrawable == null) {
            if (AttributeResolver.resolveBoolean(context, R.attr.isLightTheme, true)) {
                this.mWidgetDrawable = ContextCompat.getDrawable(context, R$drawable.miuix_appcompat_ic_visibility_selector_light);
            } else {
                this.mWidgetDrawable = ContextCompat.getDrawable(context, R$drawable.miuix_appcompat_ic_visibility_selector_dark);
            }
        }
    }

    @Override // miuix.androidbasewidget.widget.StateEditText.WidgetManager
    protected void onDetached() {
        super.onDetached();
    }

    public Drawable[] getWidgetDrawables() {
        return new Drawable[]{this.mWidgetDrawable};
    }

    public void onWidgetClick(int i) {
        boolean z = this.mIsChecked;
        this.mIsChecked = !z;
        this.mWidgetDrawable.setState(!z ? CHECKED_STATE_SET : new int[0]);
    }

    protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        accessibilityNodeInfoCompat.setCheckable(true);
        accessibilityNodeInfoCompat.setChecked(this.mIsChecked);
        accessibilityNodeInfoCompat.setClassName(Switch.class.getName());
        accessibilityNodeInfoCompat.setContentDescription(this.mContext.getString(R$string.miuix_show_password));
    }
}
