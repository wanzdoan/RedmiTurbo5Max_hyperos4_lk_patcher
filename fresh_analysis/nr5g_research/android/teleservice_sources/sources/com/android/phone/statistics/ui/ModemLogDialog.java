package com.android.phone.statistics.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.WindowManager;
import com.android.phone.R;
import com.android.phone.statistics.utils.Utils;

/* JADX INFO: loaded from: classes.dex */
public class ModemLogDialog extends BaseErrorDialog {
    private int mRuleEventId;

    public interface DialogCallBack {
        void onNagtivePressed();

        void onPositivePressed(int i);
    }

    public ModemLogDialog(Context context, final DialogCallBack dialogCallBack, int i) {
        super(context);
        Utils.logI("ModemLogDialog", "ModemLogDialog enter");
        this.mRuleEventId = i;
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.privateFlags |= 272;
        getWindow().setAttributes(attributes);
        Resources resources = context.getResources();
        setCancelable(false);
        setTitle(resources.getText(R.string.modem_dialog_title));
        setMessage(resources.getText(R.string.modem_dialog_body));
        setButton(-1, resources.getText(R.string.dialog_report), new DialogInterface.OnClickListener() { // from class: com.android.phone.statistics.ui.ModemLogDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                ModemLogDialog.this.dismiss();
                dialogCallBack.onPositivePressed(ModemLogDialog.this.mRuleEventId);
            }
        });
        setButton(-2, resources.getText(R.string.dialog_cancel), new DialogInterface.OnClickListener() { // from class: com.android.phone.statistics.ui.ModemLogDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                ModemLogDialog.this.dismiss();
                dialogCallBack.onNagtivePressed();
            }
        });
    }

    @Override // miuix.appcompat.app.AlertDialog, androidx.appcompat.app.AppCompatDialog, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
    }
}
