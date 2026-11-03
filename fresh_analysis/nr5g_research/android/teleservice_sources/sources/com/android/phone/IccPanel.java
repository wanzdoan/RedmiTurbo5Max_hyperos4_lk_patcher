package com.android.phone;

import android.app.Dialog;
import android.app.StatusBarManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

/* JADX INFO: loaded from: classes.dex */
public abstract class IccPanel extends Dialog {
    private StatusBarManager mStatusBarManager;

    public IccPanel(Context context) {
        super(context, R.style.IccPanel);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setType(2007);
        window.setLayout(-1, -1);
        window.setGravity(17);
        this.mStatusBarManager = (StatusBarManager) PhoneGlobals.getInstance().getSystemService("statusbar");
        requestWindowFeature(1);
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        this.mStatusBarManager.setExpansionDisabledForSimNetworkLock(true);
    }

    @Override // android.app.Dialog
    public void onStop() {
        super.onStop();
        this.mStatusBarManager.setExpansionDisabledForSimNetworkLock(false);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
