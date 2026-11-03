package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class SelectItemParams extends CommandParams {
    boolean mLoadTitleIcon;
    Menu mMenu;

    public SelectItemParams(CommandDetails commandDetails, Menu menu, boolean z) {
        super(commandDetails);
        this.mMenu = menu;
        this.mLoadTitleIcon = z;
    }

    @Override // com.android.internal.telephony.cat.CommandParams
    boolean setIcon(Bitmap bitmap) {
        Menu menu;
        if (bitmap == null || (menu = this.mMenu) == null) {
            return false;
        }
        if (this.mLoadTitleIcon && menu.titleIcon == null) {
            menu.titleIcon = bitmap;
            return true;
        }
        for (Item item : menu.items) {
            if (item.icon == null) {
                item.icon = bitmap;
                return true;
            }
        }
        return true;
    }
}
