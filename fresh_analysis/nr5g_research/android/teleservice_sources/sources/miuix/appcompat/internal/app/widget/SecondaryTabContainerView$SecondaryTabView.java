package miuix.appcompat.internal.app.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import miuix.appcompat.R$string;
import miuix.appcompat.widget.BadgeDrawable;
import miuix.miuixbasewidget.widget.FilterSortView2;

/* JADX INFO: loaded from: classes2.dex */
public class SecondaryTabContainerView$SecondaryTabView extends FilterSortView2.TabView {
    private final BadgeDrawable mBadge;
    private boolean mBadgeDisappearOnClick;
    private boolean mBadgeNeeded;

    public SecondaryTabContainerView$SecondaryTabView(Context context) {
        this(context, null);
    }

    public SecondaryTabContainerView$SecondaryTabView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SecondaryTabContainerView$SecondaryTabView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBadgeNeeded = false;
        this.mBadgeDisappearOnClick = true;
        this.mBadge = new BadgeDrawable(context, 2);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateBadge();
    }

    private void updateBadge() {
        if (this.mBadgeNeeded) {
            attachBadge();
        } else {
            detachBadge();
        }
    }

    private void detachBadge() {
        BadgeDrawable badgeDrawable = this.mBadge;
        if (badgeDrawable != null) {
            badgeDrawable.detachBadgeDrawable();
        }
    }

    private void attachBadge() {
        BadgeDrawable badgeDrawable = this.mBadge;
        if (badgeDrawable != null) {
            badgeDrawable.attachBadgeDrawable(this);
        }
    }

    private void updateIconView() {
        if (getIconView() != null) {
            throw null;
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateIconView();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mBadgeNeeded) {
            StringBuilder sb = new StringBuilder();
            TextView textView = getTextView();
            if (textView != null && textView.getText() != null) {
                sb.append(textView.getText());
            }
            sb.append(getResources().getString(R$string.miuix_appcompat_accessibility_new_message));
            accessibilityNodeInfo.setContentDescription(sb);
        }
    }
}
