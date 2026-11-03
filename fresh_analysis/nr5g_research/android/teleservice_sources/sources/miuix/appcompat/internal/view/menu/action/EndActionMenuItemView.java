package miuix.appcompat.internal.view.menu.action;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.ITouchStyle;
import miuix.animation.ViewHoverListener;
import miuix.animation.base.AnimConfig;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$string;
import miuix.appcompat.internal.util.FloatingEffectHelper;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuItemImpl;
import miuix.appcompat.internal.view.menu.MenuView;
import miuix.core.util.MiuixUIUtils;
import miuix.view.ITransformableView;

/* JADX INFO: loaded from: classes2.dex */
public class EndActionMenuItemView extends LinearLayout implements MenuView.ItemView, ViewHoverListener, ITransformableView {
    private boolean mFloatingMode;
    private ImageView mImageView;
    private boolean mIsCheckable;
    private boolean mIsHover;
    private boolean mIsLightTheme;
    private MenuItemImpl mItemData;
    private MenuBuilder.ItemInvoker mItemInvoker;
    private boolean mMaterialEnabled;
    private int mOpaqueBlendBgColor;
    private Drawable mPreBackgroundRef;

    @Override // miuix.view.ITransformableView
    public View getContainerView() {
        return this;
    }

    @Override // miuix.view.ITransformableView
    public float getCornerRadius() {
        return 99.0f;
    }

    @Override // miuix.animation.ViewHoverListener
    public void onMoveHover() {
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView.ItemView
    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setTitle(CharSequence charSequence) {
    }

    public EndActionMenuItemView(Context context) {
        this(context, null, 0);
    }

    public EndActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public EndActionMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOpaqueBlendBgColor = -1;
        this.mIsLightTheme = MiuixUIUtils.isLightTheme(context);
        setOrientation(1);
        setGravity(17);
        LinearLayout.inflate(context, R$layout.miuix_appcompat_action_end_menu_item_child_layout, this);
        ImageView imageView = (ImageView) findViewById(R$id.action_menu_item_child_icon);
        this.mImageView = imageView;
        imageView.setForceDarkAllowed(false);
        Folme.use((View) this.mImageView).touch().setScale(1.0f, new ITouchStyle.TouchType[0]).setAlpha(0.6f, ITouchStyle.TouchType.DOWN).setAlpha(1.0f, ITouchStyle.TouchType.UP).handleTouchOf(this, new AnimConfig[0]);
        Folme.use((View) this.mImageView).hover().setFeedbackRadius(60.0f);
        Folme.use((View) this.mImageView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(this, new AnimConfig[0]);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIsLightTheme = MiuixUIUtils.isLightTheme(getContext());
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView.ItemView
    public void initialize(MenuItemImpl menuItemImpl, int i) {
        this.mItemData = menuItemImpl;
        setSelected(false);
        setTitle(menuItemImpl.getTitle());
        setIcon(menuItemImpl.getIcon());
        setCheckable(menuItemImpl.isCheckable());
        setChecked(menuItemImpl.isChecked());
        setEnabled(menuItemImpl.isEnabled());
        setClickable(true);
        if (!TextUtils.isEmpty(menuItemImpl.getContentDescription())) {
            setContentDescription(menuItemImpl.getContentDescription());
        } else {
            setContentDescription(menuItemImpl.getTitle());
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mImageView.setEnabled(z);
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView.ItemView
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void setCheckable(boolean z) {
        this.mIsCheckable = z;
    }

    public void setChecked(boolean z) {
        if (this.mIsCheckable) {
            setSelected(z);
        }
    }

    public void setIcon(Drawable drawable) {
        if (this.mImageView.getDrawable() != drawable) {
            this.mImageView.setImageDrawable(drawable);
        }
    }

    @Override // android.view.View
    public boolean performClick() {
        if (super.performClick()) {
            return true;
        }
        int i = this.mItemData.getItemId() == R$id.more ? 1 : 0;
        MenuBuilder.ItemInvoker itemInvoker = this.mItemInvoker;
        if (itemInvoker == null || !itemInvoker.invokeItem(this.mItemData, i)) {
            return false;
        }
        playSoundEffect(0);
        return true;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView.ItemView
    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    @Override // miuix.animation.ViewHoverListener
    public boolean isHover() {
        return this.mIsHover;
    }

    @Override // miuix.animation.ViewHoverListener
    public void onEnterHover() {
        this.mIsHover = true;
    }

    @Override // miuix.animation.ViewHoverListener
    public void onExitHover() {
        this.mIsHover = false;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        MenuItemImpl menuItemImpl = this.mItemData;
        if (menuItemImpl == null || !menuItemImpl.hasAttachBadge()) {
            return;
        }
        accessibilityNodeInfo.setContentDescription(((Object) getContentDescription()) + getResources().getString(R$string.miuix_appcompat_accessibility_new_message));
    }

    @Override // miuix.view.ITransformableView
    public View getContentView() {
        return this.mImageView;
    }

    @Override // miuix.view.ITransformableView
    public void captureAppearance() {
        this.mPreBackgroundRef = getBackground();
        setSelected(false);
        MenuItemImpl menuItemImpl = this.mItemData;
        if (menuItemImpl != null) {
            menuItemImpl.setBadgeAlpha(0.0f);
        }
    }

    @Override // miuix.view.ITransformableView
    public void clearAppearance() {
        FloatingEffectHelper.clearElevation(this);
        setBackground(null);
    }

    @Override // miuix.view.ITransformableView
    public void restoreAppearance() {
        FloatingEffectHelper.setFloatingState(this, this.mFloatingMode, this.mIsLightTheme, this.mMaterialEnabled, this.mOpaqueBlendBgColor);
        setSelected(false);
        MenuItemImpl menuItemImpl = this.mItemData;
        if (menuItemImpl != null) {
            menuItemImpl.setBadgeAlpha(0.0f);
            post(new Runnable() { // from class: miuix.appcompat.internal.view.menu.action.EndActionMenuItemView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.mItemData.animateBadgeAlpha(1.0f);
                }
            });
        }
    }

    @Override // miuix.view.ITransformableView
    public boolean isFloating() {
        return this.mFloatingMode;
    }

    public void setFloatingMode(boolean z, boolean z2, boolean z3, int i) {
        this.mFloatingMode = z;
        this.mIsLightTheme = z2;
        this.mMaterialEnabled = z3;
        this.mOpaqueBlendBgColor = i;
        FloatingEffectHelper.updateViewFloatingState(this, z, z2, z3, i);
    }
}
