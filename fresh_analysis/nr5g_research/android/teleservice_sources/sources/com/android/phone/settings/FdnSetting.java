package com.android.phone.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;

/* JADX INFO: loaded from: classes.dex */
public class FdnSetting extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!SimPickerPreference.showSimPicker(this) && bundle == null) {
            loadFragment(new FdnSettingFragment());
        }
    }

    public class FdnSettingFragment extends BaseActivity.BaseFragment implements EditPinDialog.DialogClosedListener {
        private Preference mButtonFDNList;
        private Preference mChangePin2;
        private EditPinDialog mEditPinDialog;
        private CheckBoxPreference mEnableFdn;
        private boolean mIsRegister;
        Phone mPhone;
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.FdnSetting.FdnSettingFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("mediatek.intent.action.PHB_STATE_CHANGED".equals(intent.getAction())) {
                    Log.d("FdnSetting", "PHB_STATE_CHANGED, update screen");
                    FdnSettingFragment.this.updateFdnAvailable();
                    FdnSettingFragment.this.updateEnableFDN();
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction()) && FdnSettingFragment.this.mSlotId == SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.getDefault().getDefaultSlotId()) && "ABSENT".equals(intent.getStringExtra("ss")) && FdnSettingFragment.this.getActivity() != null) {
                    FdnSettingFragment.this.getActivity().finish();
                }
            }
        };
        private int mSlotId;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            int slotIdExtra = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            this.mSlotId = slotIdExtra;
            this.mPhone = MiuiPhoneUtils.getPhone(slotIdExtra);
            addPreferencesFromResource(R.xml.miui_fdn_setting);
            this.mEnableFdn = (CheckBoxPreference) findPreference("button_fdn_enable");
            this.mChangePin2 = findPreference("button_change_pin2");
            this.mButtonFDNList = findPreference("button_fdn_list");
            SubscriptionManager.putSlotIdExtra(findPreference("button_fdn_list").getIntent(), this.mSlotId);
            EditPinDialog editPinDialog = new EditPinDialog(getContext(), EditPinDialog.Mode.FDN, this, this.mPhone);
            this.mEditPinDialog = editPinDialog;
            editPinDialog.registerHotSwapReceiver(getContext());
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            if (shouldWaitPhoneBookReady()) {
                intentFilter.addAction("mediatek.intent.action.PHB_STATE_CHANGED");
            }
            if (this.mIsRegister) {
                return;
            }
            getContext().registerReceiver(this.mReceiver, intentFilter, 2);
            this.mIsRegister = true;
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            updateFdnAvailable();
            updateEnableFDN();
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            EditPinDialog editPinDialog = this.mEditPinDialog;
            if (editPinDialog != null) {
                editPinDialog.dismiss();
                this.mEditPinDialog.unregisterHotSwapReceiver(getContext());
                this.mEditPinDialog = null;
            }
            if (this.mIsRegister) {
                getContext().unregisterReceiver(this.mReceiver);
                this.mIsRegister = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateFdnAvailable() {
            if (shouldWaitPhoneBookReady() && this.mPhone.getIccCard() != null) {
                boolean zIsIccFdnAvailable = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isIccFdnAvailable(this.mPhone);
                Log.d("FdnSetting", "updateFdnAvailable = " + zIsIccFdnAvailable);
                this.mEnableFdn.setEnabled(zIsIccFdnAvailable);
                this.mButtonFDNList.setEnabled(zIsIccFdnAvailable);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateEnableFDN() {
            boolean iccFdnEnabled = this.mPhone.getIccCard().getIccFdnEnabled();
            CheckBoxPreference checkBoxPreference = this.mEnableFdn;
            if (iccFdnEnabled) {
                checkBoxPreference.setChecked(true);
            } else {
                checkBoxPreference.setChecked(false);
            }
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            CheckBoxPreference checkBoxPreference = this.mEnableFdn;
            if (preference == checkBoxPreference) {
                checkBoxPreference.setChecked(!checkBoxPreference.isChecked());
                this.mEditPinDialog.setMode(EditPinDialog.Mode.FDN);
                this.mEditPinDialog.show();
                return true;
            }
            if (preference == this.mChangePin2) {
                this.mEditPinDialog.setMode(EditPinDialog.Mode.PIN2);
                this.mEditPinDialog.show();
                return true;
            }
            Preference preference2 = this.mButtonFDNList;
            if (preference != preference2) {
                return false;
            }
            Intent intent = preference2.getIntent();
            if (getActivity() instanceof AppCompatActivity) {
                MultiAppFloatingActivitySwitcher.configureFloatingService(intent, (AppCompatActivity) getActivity());
            }
            startActivity(intent);
            return true;
        }

        @Override // com.android.phone.settings.EditPinDialog.DialogClosedListener
        public void OnDialogClose(EditPinDialog editPinDialog, boolean z) {
            if (z || editPinDialog.getMode() != EditPinDialog.Mode.FDN) {
                return;
            }
            updateEnableFDN();
        }

        private boolean shouldWaitPhoneBookReady() {
            return ModemProvider.IS_MTK || ModemProvider.IS_XRING;
        }
    }
}
