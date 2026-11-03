package com.mediatek.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.phone.PhoneUtils;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class MobileDataDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    public static void show(Intent intent, CallSettingUtils.DialogType dialogType, int i, FragmentManager fragmentManager) {
        MobileDataDialogFragment mobileDataDialogFragment = new MobileDataDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("intent", intent);
        bundle.putInt("subId", i);
        bundle.putSerializable("type", dialogType);
        mobileDataDialogFragment.setArguments(bundle);
        mobileDataDialogFragment.setCancelable(false);
        mobileDataDialogFragment.show(fragmentManager, "DataTrafficDialog");
    }

    /* JADX INFO: renamed from: com.mediatek.settings.MobileDataDialogFragment$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$mediatek$settings$CallSettingUtils$DialogType;

        static {
            int[] iArr = new int[CallSettingUtils.DialogType.values().length];
            $SwitchMap$com$mediatek$settings$CallSettingUtils$DialogType = iArr;
            try {
                iArr[CallSettingUtils.DialogType.DATA_OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$mediatek$settings$CallSettingUtils$DialogType[CallSettingUtils.DialogType.DATA_TRAFFIC.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$mediatek$settings$CallSettingUtils$DialogType[CallSettingUtils.DialogType.CDMA_DATA_TRAFFIC.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$mediatek$settings$CallSettingUtils$DialogType[CallSettingUtils.DialogType.DATA_ROAMING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private String getMessage(CallSettingUtils.DialogType dialogType, int i) {
        String subDisplayName = PhoneUtils.getSubDisplayName(i);
        int i2 = AnonymousClass1.$SwitchMap$com$mediatek$settings$CallSettingUtils$DialogType[dialogType.ordinal()];
        if (i2 == 1) {
            return getResources().getString(R.string.volte_ss_not_available_tips_data, subDisplayName);
        }
        if (i2 == 2 || i2 == 3) {
            return getResources().getString(R.string.volte_ss_not_available_tips_data_traffic, subDisplayName);
        }
        if (i2 != 4) {
            return null;
        }
        return getResources().getString(R.string.volte_ss_not_available_tips_data_roaming, subDisplayName);
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        int i = getArguments().getInt("subId");
        CallSettingUtils.DialogType dialogType = (CallSettingUtils.DialogType) getArguments().getSerializable("type");
        Log.d("MobileDataDialogFragment", "type = " + dialogType);
        if (dialogType == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getMessage(dialogType, i)).setIconAttribute(android.R.attr.alertDialogIcon).setPositiveButton(R.string.ok, this).setNegativeButton(android.R.string.cancel, this);
        builder.setCancelable(false);
        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        return alertDialogCreate;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        Intent intent;
        if (i != -1 || (intent = (Intent) getArguments().getParcelable("intent")) == null) {
            return;
        }
        int intExtra = intent.getIntExtra("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", -1);
        Log.d("MobileDataDialogFragment", "subId = " + intExtra);
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            Log.e("MobileDataDialogFragment", "Activity is null or finishing");
        } else if (SubscriptionManager.from(activity).isActiveSubId(intExtra)) {
            getActivity().startActivity(intent);
        } else {
            Log.e("MobileDataDialogFragment", "Inactive subId");
        }
    }
}
