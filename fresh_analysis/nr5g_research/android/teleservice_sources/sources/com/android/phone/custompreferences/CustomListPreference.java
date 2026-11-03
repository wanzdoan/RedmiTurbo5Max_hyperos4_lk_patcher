package com.android.phone.custompreferences;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.ListPreference;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.ListPreferenceDialogFragmentCompat;

/* JADX INFO: loaded from: classes.dex */
public class CustomListPreference extends ListPreference {
    private CustomListPreferenceDialogFragment mFragment;

    protected void onBindDialogView(View view) {
    }

    public void onClick(DialogInterface dialogInterface, int i) {
    }

    protected void onDialogClosed(boolean z) {
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
    }

    public CustomListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomListPreference(Context context) {
        super(context);
    }

    public Dialog getDialog() {
        CustomListPreferenceDialogFragment customListPreferenceDialogFragment = this.mFragment;
        if (customListPreferenceDialogFragment != null) {
            return customListPreferenceDialogFragment.getDialog();
        }
        return null;
    }

    protected void showDialog(Bundle bundle) {
        if (getDialog() != null) {
            getDialog().show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFragment(CustomListPreferenceDialogFragment customListPreferenceDialogFragment) {
        this.mFragment = customListPreferenceDialogFragment;
    }

    public class CustomListPreferenceDialogFragment extends ListPreferenceDialogFragmentCompat {
        public static CustomListPreferenceDialogFragment newInstance(String str) {
            CustomListPreferenceDialogFragment customListPreferenceDialogFragment = new CustomListPreferenceDialogFragment();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", str);
            customListPreferenceDialogFragment.setArguments(bundle);
            return customListPreferenceDialogFragment;
        }

        private CustomListPreference getCustomizablePreference() {
            return (CustomListPreference) getPreference();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.preference.PreferenceDialogFragmentCompat
        public void onBindDialogView(View view) {
            super.onBindDialogView(view);
            getCustomizablePreference().onBindDialogView(view);
        }

        @Override // miuix.preference.ListPreferenceDialogFragmentCompat
        protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
            super.onPrepareDialogBuilder(builder);
            getCustomizablePreference().setFragment(this);
            getCustomizablePreference().onPrepareDialogBuilder(builder, this);
        }

        @Override // androidx.preference.ListPreferenceDialogFragmentCompat, androidx.preference.PreferenceDialogFragmentCompat
        public void onDialogClosed(boolean z) {
            super.onDialogClosed(z);
            getCustomizablePreference().onDialogClosed(z);
        }

        @Override // androidx.preference.PreferenceDialogFragmentCompat, android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            super.onClick(dialogInterface, i);
            getCustomizablePreference().onClick(dialogInterface, i);
        }
    }
}
