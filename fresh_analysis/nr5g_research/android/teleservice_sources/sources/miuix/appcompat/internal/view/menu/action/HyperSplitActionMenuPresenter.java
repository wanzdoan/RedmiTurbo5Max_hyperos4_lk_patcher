package miuix.appcompat.internal.view.menu.action;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import java.util.HashMap;
import java.util.Map;
import miuix.appcompat.R$attr;
import miuix.appcompat.internal.app.widget.ActionBarOverlayLayout;
import miuix.appcompat.internal.view.menu.BaseMenuPresenter;
import miuix.appcompat.internal.view.menu.HyperPopupHelper;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.internal.util.AttributeResolver;

/* JADX INFO: loaded from: classes2.dex */
public class HyperSplitActionMenuPresenter extends ActionMenuPresenter {
    private Map mHyperSplitMenuPrimaryCheckedMap;
    private Map mHyperSplitMenuSecondaryCheckedMap;
    private boolean mSavePrimaryStatusByIdEnabled;

    public HyperSplitActionMenuPresenter(Context context, ActionBarOverlayLayout actionBarOverlayLayout, int i, int i2) {
        super(context, actionBarOverlayLayout, i, i2);
        this.mHyperSplitMenuPrimaryCheckedMap = new HashMap();
        this.mHyperSplitMenuSecondaryCheckedMap = new HashMap();
        this.mSavePrimaryStatusByIdEnabled = false;
    }

    @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter
    protected ActionMenuPresenter.OverflowMenu getOverflowMenu() {
        if (shouldShowPopupOverflow()) {
            HyperSplitPopupOverflowMenu hyperSplitPopupOverflowMenu = new HyperSplitPopupOverflowMenu(this.mContext, this.mMenu, this.mOverflowButton, this.mDecorView, true);
            hyperSplitPopupOverflowMenu.restoreHyperMenuPrimaryCheckedData(this.mHyperSplitMenuPrimaryCheckedMap);
            hyperSplitPopupOverflowMenu.restoreHyperMenuSecondaryCheckedData(this.mHyperSplitMenuSecondaryCheckedMap);
            hyperSplitPopupOverflowMenu.setSaveStatusByIdEnabled(this.mSavePrimaryStatusByIdEnabled);
            return hyperSplitPopupOverflowMenu;
        }
        return super.getOverflowMenu();
    }

    public class HyperSplitPopupOverflowMenu extends HyperPopupHelper implements ActionMenuPresenter.OverflowMenu {
        @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public void update(MenuBuilder menuBuilder) {
        }

        public HyperSplitPopupOverflowMenu(Context context, MenuBuilder menuBuilder, View view, View view2, boolean z) {
            int iComplexToDimensionPixelSize;
            super(context, menuBuilder, view, view2, z);
            TypedValue typedValueResolveTypedValue = AttributeResolver.resolveTypedValue(context, R$attr.overflowMenuMaxHeight);
            if (typedValueResolveTypedValue == null || typedValueResolveTypedValue.type != 5) {
                iComplexToDimensionPixelSize = 0;
            } else if (typedValueResolveTypedValue.resourceId > 0) {
                iComplexToDimensionPixelSize = context.getResources().getDimensionPixelSize(typedValueResolveTypedValue.resourceId);
            } else {
                iComplexToDimensionPixelSize = TypedValue.complexToDimensionPixelSize(typedValueResolveTypedValue.data, context.getResources().getDisplayMetrics());
            }
            if (iComplexToDimensionPixelSize > 0) {
                setPopupMaxHeight(iComplexToDimensionPixelSize);
            }
            setCallback(HyperSplitActionMenuPresenter.this.mPopupPresenterCallback);
        }

        @Override // miuix.appcompat.internal.view.menu.HyperPopupHelper, android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            super.onDismiss();
            ((BaseMenuPresenter) HyperSplitActionMenuPresenter.this).mMenu.close();
            HyperSplitActionMenuPresenter.this.mOverflowMenu = null;
        }

        @Override // miuix.appcompat.internal.view.menu.HyperPopupHelper, miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public void dismiss(boolean z) {
            super.dismiss(z);
            View view = HyperSplitActionMenuPresenter.this.mOverflowButton;
            if (view != null) {
                view.setSelected(false);
            }
            if (((BaseMenuPresenter) HyperSplitActionMenuPresenter.this).mMenuView instanceof ResponsiveActionMenuView) {
                ((ResponsiveActionMenuView) ((BaseMenuPresenter) HyperSplitActionMenuPresenter.this).mMenuView).hideOverflowSelectedBackground();
            }
        }
    }
}
