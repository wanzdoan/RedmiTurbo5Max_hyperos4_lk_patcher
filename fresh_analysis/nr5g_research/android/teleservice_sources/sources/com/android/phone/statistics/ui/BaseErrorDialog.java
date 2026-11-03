package com.android.phone.statistics.ui;

import android.R;
import android.content.Context;
import android.view.WindowManager;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseErrorDialog extends AlertDialog {
    public BaseErrorDialog(Context context) {
        super(context, R$style.AlertDialog_Theme_DayNight);
        getWindow().setType(2003);
        getWindow().setFlags(131072, 131072);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.setTitle("Error Dialog");
        getWindow().setAttributes(attributes);
        setIconAttribute(R.attr.alertDialogIcon);
    }
}
