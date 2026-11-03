package miuix.appcompat.internal.view.menu;

import android.content.Context;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import java.util.Map;
import miuix.appcompat.internal.view.menu.action.HyperSplitActionMenuPresenter;
import miuix.appcompat.view.menu.HyperMenuAdapter;
import miuix.appcompat.widget.HyperPopupWindow;
import miuix.popupwidget.internal.strategy.BottomMenuStrategy;

/* JADX INFO: loaded from: classes2.dex */
public abstract class HyperPopupHelper implements HyperPopupWindow.OnMenuItemClickListener, View.OnKeyListener, PopupWindow.OnDismissListener, MenuPresenter {
    private View mAnchorView;
    private Context mContext;
    private View mFenceDecor;
    private HyperMenuAdapter mHyperMenuAdapter;
    private HyperPopupWindow mHyperPopup;
    private MenuBuilder mMenu;
    private boolean mOverflowOnly;
    private int mPopupVerticalOffset;
    private MenuPresenter.Callback mPresenterCallback;
    private Map mPrimaryCheckedMap;
    private Map mSecondaryCheckedMap;
    private int mPopupHorizontalOffset = 0;
    private int mPopupAnimationGravity = -1;
    private int mPopupMaxHeight = 0;
    private boolean mSavePrimaryStatusByIdEnabled = false;

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public int getId() {
        return 0;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void onRestoreInstanceState(Parcelable parcelable) {
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public Parcelable onSaveInstanceState() {
        return null;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        return true;
    }

    public HyperPopupHelper(Context context, MenuBuilder menuBuilder, View view, View view2, boolean z) {
        this.mContext = context;
        this.mMenu = menuBuilder;
        this.mOverflowOnly = z;
        this.mAnchorView = view;
        this.mFenceDecor = view2;
        menuBuilder.addMenuPresenter(this);
    }

    public void setSaveStatusByIdEnabled(boolean z) {
        this.mSavePrimaryStatusByIdEnabled = z;
    }

    public void setPopupMaxHeight(int i) {
        this.mPopupMaxHeight = i;
    }

    public void restoreHyperMenuPrimaryCheckedData(Map map) {
        this.mPrimaryCheckedMap = map;
    }

    public void restoreHyperMenuSecondaryCheckedData(Map map) {
        this.mSecondaryCheckedMap = map;
    }

    public boolean tryShow() {
        this.mHyperPopup = new HyperPopupWindow(this.mContext, this.mFenceDecor);
        if (this instanceof HyperSplitActionMenuPresenter.HyperSplitPopupOverflowMenu) {
            this.mHyperPopup.setPopupWindowStrategy(new BottomMenuStrategy(this.mContext));
        }
        this.mHyperPopup.setDropDownGravity(49);
        this.mHyperPopup.setOnDismissListener(this);
        this.mHyperPopup.setOnMenuItemClickListener(this);
        this.mHyperPopup.setWindowAnimationEnabled(false);
        this.mHyperPopup.setMenuBlurOptionType(1);
        HyperMenuAdapter hyperMenuAdapter = new HyperMenuAdapter(this.mContext, null, this.mOverflowOnly);
        this.mHyperMenuAdapter = hyperMenuAdapter;
        hyperMenuAdapter.setOptionalIconsVisible(this.mMenu.getOptionalIconsVisible());
        this.mHyperMenuAdapter.setSavePrimaryStatusByIdEnabled(this.mSavePrimaryStatusByIdEnabled);
        Map map = this.mPrimaryCheckedMap;
        if (map != null) {
            this.mHyperMenuAdapter.preCheckPrimaryItem(map);
        }
        Map map2 = this.mSecondaryCheckedMap;
        if (map2 != null) {
            this.mHyperMenuAdapter.preCheckSecondaryItem(map2);
        }
        this.mHyperMenuAdapter.update(this.mMenu);
        this.mHyperPopup.setAdapter(this.mHyperMenuAdapter);
        this.mHyperPopup.setSecondaryMenuEnabled(this.mHyperMenuAdapter.hasSubMenu());
        this.mHyperPopup.setHorizontalOffset(this.mPopupHorizontalOffset);
        this.mHyperPopup.setVerticalOffset(this.mPopupVerticalOffset);
        int i = this.mPopupMaxHeight;
        if (i > 0) {
            this.mHyperPopup.setMaxAllowedHeight(i);
        }
        this.mHyperPopup.show(this.mAnchorView);
        this.mHyperPopup.getContentView().setOnKeyListener(this);
        return true;
    }

    public void dismiss(boolean z) {
        if (isShowing()) {
            this.mHyperPopup.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: miuix.appcompat.internal.view.menu.HyperPopupHelper$$ExternalSyntheticLambda0
                @Override // android.widget.PopupWindow.OnDismissListener
                public final void onDismiss() {
                    this.f$0.saveData();
                }
            });
            this.mHyperPopup.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveData() {
        HyperMenuAdapter hyperMenuAdapter = this.mHyperMenuAdapter;
        if (hyperMenuAdapter != null) {
            hyperMenuAdapter.copyPrimaryCheckedData(this.mPrimaryCheckedMap);
            this.mHyperMenuAdapter.copySecondaryCheckedData(this.mSecondaryCheckedMap);
        }
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        saveData();
        this.mHyperPopup = null;
        this.mMenu.close();
    }

    public boolean isShowing() {
        HyperPopupWindow hyperPopupWindow = this.mHyperPopup;
        return (hyperPopupWindow == null || !hyperPopupWindow.isShowing() || this.mHyperPopup.isInDismissAnimation()) ? false : true;
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i != 82) {
            return false;
        }
        dismiss(false);
        return true;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void updateMenuView(boolean z) {
        HyperMenuAdapter hyperMenuAdapter = this.mHyperMenuAdapter;
        if (hyperMenuAdapter != null) {
            hyperMenuAdapter.notifyDataSetChanged();
        }
    }

    public void setCallback(MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        if (menuBuilder != this.mMenu) {
            return;
        }
        dismiss(true);
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, z);
        }
    }

    @Override // miuix.appcompat.widget.HyperPopupWindow.OnMenuItemClickListener
    public void onMenuItemClick(MenuItem menuItem) {
        this.mMenu.performItemAction(menuItem, 0);
    }
}
