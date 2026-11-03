package com.android.phone.settings.fdn;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class FdnErrorDialogFragment extends DialogFragment {
    private int mId;
    private Listener mListener;

    interface Listener {
        void onRequestPuk2(int i);
    }

    public static /* synthetic */ void $r8$lambda$BHBZie6ZFAwzjgXEp_WMuVNHJDE(DialogInterface dialogInterface, int i) {
    }

    public static /* synthetic */ void $r8$lambda$Hr0BswB0qaBawcL26ARVd1UiqUw(DialogInterface dialogInterface, int i) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        super.onCreateDialog(bundle);
        Activity activity = getActivity();
        if (!(activity instanceof Listener)) {
            return null;
        }
        this.mListener = (Listener) activity;
        int i = getArguments().getInt("key_dialog_id");
        this.mId = i;
        switch (i) {
            case 10:
                AlertDialog alertDialogCreate = new AlertDialog.Builder(activity).setMessage(R.string.puk2_locked).setCancelable(true).create();
                alertDialogCreate.getWindow().addFlags(2);
                alertDialogCreate.setButton(-3, getText(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.fdn.FdnErrorDialogFragment$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        FdnErrorDialogFragment.$r8$lambda$BHBZie6ZFAwzjgXEp_WMuVNHJDE(dialogInterface, i2);
                    }
                });
                return alertDialogCreate;
            case 11:
            case 12:
            case 17:
                AlertDialog alertDialogCreate2 = new AlertDialog.Builder(activity).setMessage(this.mId == 17 ? R.string.pin2_blocked : R.string.puk2_requested).setCancelable(true).create();
                alertDialogCreate2.getWindow().addFlags(2);
                alertDialogCreate2.setButton(-3, getText(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.fdn.FdnErrorDialogFragment$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        this.f$0.lambda$onCreateDialog$1(dialogInterface, i2);
                    }
                });
                return alertDialogCreate2;
            case 13:
            case 14:
            case 15:
            case 16:
                AlertDialog alertDialogCreate3 = new AlertDialog.Builder(activity).setMessage(getDisplayMessage(getArguments().getInt("key_error_msg"), getArguments().getInt("key_attempts_remaining"))).setCancelable(true).create();
                alertDialogCreate3.getWindow().addFlags(2);
                alertDialogCreate3.setButton(-3, getText(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.fdn.FdnErrorDialogFragment$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        FdnErrorDialogFragment.$r8$lambda$Hr0BswB0qaBawcL26ARVd1UiqUw(dialogInterface, i2);
                    }
                });
                return alertDialogCreate3;
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateDialog$1(DialogInterface dialogInterface, int i) {
        this.mListener.onRequestPuk2(this.mId);
        dialogInterface.dismiss();
    }

    private String getDisplayMessage(int i, int i2) {
        String string = getString(i);
        if (i != R.string.badPin2 && i != R.string.badPuk2 && i != R.string.pin2_invalid) {
            return string;
        }
        if (i2 >= 0) {
            return getString(i) + getString(R.string.pin2_attempts, Integer.valueOf(i2));
        }
        return getString(i);
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        int i = this.mId;
        if (i == 12 || i == 11 || i == 17) {
            this.mListener.onRequestPuk2(i);
        }
        dialogInterface.dismiss();
    }
}
