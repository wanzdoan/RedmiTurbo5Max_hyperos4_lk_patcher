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
public class HyperActionMenuPresenter extends EndActionMenuPresenter {
    private Map mHyperMenuPrimaryCheckedMap;
    private Map mHyperMenuSecondaryCheckedMap;
    private boolean mSavePrimaryStatusByIdEnabled;

    public HyperActionMenuPresenter(Context context, ActionBarOverlayLayout actionBarOverlayLayout, int i, int i2, int i3, int i4) {
        super(context, actionBarOverlayLayout, i, i2, i3, i4);
        this.mHyperMenuPrimaryCheckedMap = new HashMap();
        this.mHyperMenuSecondaryCheckedMap = new HashMap();
        this.mSavePrimaryStatusByIdEnabled = false;
    }

    @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter
    protected ActionMenuPresenter.OverflowMenu getOverflowMenu() {
        if (shouldShowPopupOverflow()) {
            HyperPopupOverflowMenu hyperPopupOverflowMenu = new HyperPopupOverflowMenu(this.mContext, this.mMenu, this.mOverflowButton, this.mDecorView, true);
            hyperPopupOverflowMenu.restoreHyperMenuPrimaryCheckedData(this.mHyperMenuPrimaryCheckedMap);
            hyperPopupOverflowMenu.restoreHyperMenuSecondaryCheckedData(this.mHyperMenuSecondaryCheckedMap);
            hyperPopupOverflowMenu.setSaveStatusByIdEnabled(this.mSavePrimaryStatusByIdEnabled);
            return hyperPopupOverflowMenu;
        }
        return super.getOverflowMenu();
    }

    public class HyperPopupOverflowMenu extends HyperPopupHelper implements ActionMenuPresenter.OverflowMenu {
        @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public void update(MenuBuilder menuBuilder) {
        }

        public HyperPopupOverflowMenu(Context context, MenuBuilder menuBuilder, View view, View view2, boolean z) {
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
            setCallback(HyperActionMenuPresenter.this.mPopupPresenterCallback);
        }

        @Override // miuix.appcompat.internal.view.menu.HyperPopupHelper, android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            super.onDismiss();
            ((BaseMenuPresenter) HyperActionMenuPresenter.this).mMenu.close();
            HyperActionMenuPresenter.this.mOverflowMenu = null;
        }

        @Override // miuix.appcompat.internal.view.menu.HyperPopupHelper, miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public void dismiss(boolean z) {
            super.dismiss(z);
            View view = HyperActionMenuPresenter.this.mOverflowButton;
            if (view != null) {
                view.setSelected(false);
            }
        }
    }
}
