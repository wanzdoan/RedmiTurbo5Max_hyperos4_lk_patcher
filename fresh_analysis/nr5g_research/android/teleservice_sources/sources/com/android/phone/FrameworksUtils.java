package com.android.phone;

import android.app.AlertDialog;
import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public abstract class FrameworksUtils {
    public static AlertDialog.Builder makeAlertDialogBuilder(Context context) {
        return new AlertDialog.Builder(context, (context.getResources().getConfiguration().uiMode & 48) == 32 ? android.R.style.Theme.DeviceDefault.Dialog.Alert : 0);
    }
}
