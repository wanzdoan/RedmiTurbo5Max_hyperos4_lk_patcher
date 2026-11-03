package com.android.phone.beidou;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.WindowManager;
import androidx.fragment.app.DialogFragment;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class BdDialogActivity extends AppCompatActivity {
    public static String AGREE_BEIDOU_PRIVACY_STATEMENT = "agree_beidou_privacy_statement";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() == null) {
            return;
        }
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.layoutInDisplayCutoutMode = 1;
        getWindow().setAttributes(attributes);
        int intExtra = getIntent().getIntExtra("type", 0);
        getIntent().getIntExtra("flag_id", 0);
        getIntent().getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, 0);
        BdDialogFragment.newInstance(intExtra).show(getSupportFragmentManager(), "dialog " + intExtra);
    }

    public class BdDialogFragment extends DialogFragment {
        public static BdDialogFragment newInstance(int i) {
            BdDialogFragment bdDialogFragment = new BdDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            bdDialogFragment.setArguments(bundle);
            return bdDialogFragment;
        }

        @Override // androidx.fragment.app.DialogFragment
        public Dialog onCreateDialog(Bundle bundle) {
            int i;
            super.onCreateDialog(bundle);
            int i2 = getArguments().getInt("type");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R$style.AlertDialog_Theme_DayNight);
            if (i2 != 0) {
                return null;
            }
            if (MiuiPhoneUtils.isOnlySupportCmccBeiDou()) {
                i = R.string.beidiue_privacy_statement_message;
            } else {
                i = R.string.beidiue_privacy_statement_message_all;
            }
            AlertDialog alertDialogCreate = builder.setTitle(R.string.beidiu_privacy_statement_title).setMessage(i).setPositiveButton(R.string.beidiu_privacy_statement_positive, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.beidou.BdDialogActivity.BdDialogFragment.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    BdSmsUtils.setBdSwitchState(true);
                    dialogInterface.dismiss();
                }
            }).setNegativeButton(R.string.beidiu_privacy_statement_negative, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.beidou.BdDialogActivity.BdDialogFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i3) {
                    BdSmsUtils.setBdSwitchState(false);
                    dialogInterface.dismiss();
                }
            }).setCancelable(false).create();
            alertDialogCreate.setCanceledOnTouchOutside(false);
            alertDialogCreate.show();
            alertDialogCreate.getMessageView().setMovementMethod(LinkMovementMethod.getInstance());
            return alertDialogCreate;
        }

        @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            dialogInterface.dismiss();
        }

        @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            super.onDismiss(dialogInterface);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }
}
