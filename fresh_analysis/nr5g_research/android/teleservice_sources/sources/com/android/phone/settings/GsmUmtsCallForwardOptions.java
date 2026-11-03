package com.android.phone.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.ot.pubsub.a.a;
import gov.nist.javax.sip.address.GenericURI;
import java.util.ArrayList;
import miui.enterprise.EnterpriseManagerStub;
import miui.enterprise.PhoneHelperStub;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class GsmUmtsCallForwardOptions extends TimeConsumingPreferenceActivity {
    private static final String[] NUM_PROJECTION = {"data1"};

    @Override // com.android.phone.settings.TimeConsumingPreferenceActivity, com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!SimPickerPreference.showSimPicker(this) && bundle == null) {
            loadFragment(new GsmUmtsCallForwardOptionsFragment());
        }
    }

    public class GsmUmtsCallForwardOptionsFragment extends TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment {
        private boolean DBG;
        private CallForwardEditPreference mButtonCFB;
        private CallForwardEditPreference mButtonCFNRc;
        private CallForwardEditPreference mButtonCFNRy;
        private CallForwardEditPreference mButtonCFU;
        private AlertDialog mDialog;
        private boolean mFirstResume;
        private boolean mHasShowSuppServiceDialog;
        private Bundle mIcicle;
        private int mInitIndex;
        private Phone mPhone;
        private final ArrayList mPreferences;
        private int mServiceClass;
        private int mSubscription;

        public GsmUmtsCallForwardOptionsFragment() {
            this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
            this.mPreferences = new ArrayList();
            this.mInitIndex = 0;
            this.mHasShowSuppServiceDialog = false;
            this.mSubscription = 0;
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            super.onCreatePreferences(bundle, str);
            addPreferencesFromResource(R.xml.miui_callforward_options);
            int slotIdExtra = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            this.mSubscription = slotIdExtra;
            this.mPhone = MiuiPhoneUtils.getPhone(slotIdExtra);
            Log.d("GsmUmtsCallForwardOptions", "Call Forwarding options, subscription =" + this.mSubscription);
            this.mServiceClass = getActivity().getIntent().getIntExtra("service_class", 1);
            if (this.DBG) {
                Log.d("GsmUmtsCallForwardOptions", "serviceClass: " + this.mServiceClass);
            }
            PreferenceCategory preferenceCategory = (PreferenceCategory) getPreferenceScreen().findPreference("miui_call_forwarding_setting");
            this.mButtonCFU = (CallForwardEditPreference) preferenceCategory.findPreference("button_cfu_key");
            this.mButtonCFB = (CallForwardEditPreference) preferenceCategory.findPreference("button_cfb_key");
            this.mButtonCFNRy = (CallForwardEditPreference) preferenceCategory.findPreference("button_cfnry_key");
            this.mButtonCFNRc = (CallForwardEditPreference) preferenceCategory.findPreference("button_cfnrc_key");
            CallForwardEditPreference callForwardEditPreference = this.mButtonCFU;
            callForwardEditPreference.setIntentFragment(this, callForwardEditPreference.reason);
            CallForwardEditPreference callForwardEditPreference2 = this.mButtonCFB;
            callForwardEditPreference2.setIntentFragment(this, callForwardEditPreference2.reason);
            CallForwardEditPreference callForwardEditPreference3 = this.mButtonCFNRy;
            callForwardEditPreference3.setIntentFragment(this, callForwardEditPreference3.reason);
            CallForwardEditPreference callForwardEditPreference4 = this.mButtonCFNRc;
            callForwardEditPreference4.setIntentFragment(this, callForwardEditPreference4.reason);
            this.mPreferences.add(this.mButtonCFU);
            this.mPreferences.add(this.mButtonCFB);
            this.mPreferences.add(this.mButtonCFNRy);
            this.mPreferences.add(this.mButtonCFNRc);
            this.mButtonCFU.setServiceClass(this.mServiceClass);
            this.mButtonCFB.setServiceClass(this.mServiceClass);
            this.mButtonCFNRy.setServiceClass(this.mServiceClass);
            this.mButtonCFNRc.setServiceClass(this.mServiceClass);
            this.mFirstResume = true;
            this.mIcicle = bundle;
            if (!MiuiPhoneUtils.isShowCallForwardingCFNRc(this.mPhone)) {
                preferenceCategory.removePreference(this.mButtonCFNRc);
                this.mPreferences.remove(this.mButtonCFNRc);
            }
            Phone phone = this.mPhone;
            if (HyperOSCustFeatureResolve.getBooleanForPhoneId(phone != null ? phone.getPhoneId() : SubscriptionManager.INVALID_PHONE_ID, "call_forwarding_display_only_cfu", false)) {
                preferenceCategory.removePreference(this.mButtonCFB);
                this.mPreferences.remove(this.mButtonCFB);
                preferenceCategory.removePreference(this.mButtonCFNRc);
                this.mPreferences.remove(this.mButtonCFNRc);
                preferenceCategory.removePreference(this.mButtonCFNRy);
                this.mPreferences.remove(this.mButtonCFNRy);
            }
            this.mButtonCFU.setDialogOnClosedListener(new EditPhoneNumberPreference.OnDialogClosedListener() { // from class: com.android.phone.settings.GsmUmtsCallForwardOptions.GsmUmtsCallForwardOptionsFragment.1
                @Override // com.android.phone.settings.EditPhoneNumberPreference.OnDialogClosedListener
                public void onDialogClosed(EditPhoneNumberPreference editPhoneNumberPreference, int i) {
                    if (GsmUmtsCallForwardOptionsFragment.this.mButtonCFU.getText() == null || !GsmUmtsCallForwardOptionsFragment.this.mButtonCFU.getText().split(":")[0].equals("0")) {
                        return;
                    }
                    GsmUmtsCallForwardOptionsFragment.this.mButtonCFB.setEnabled(true);
                    GsmUmtsCallForwardOptionsFragment.this.mButtonCFNRc.setEnabled(true);
                    GsmUmtsCallForwardOptionsFragment.this.mButtonCFNRy.setEnabled(true);
                }
            });
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            if (!this.mFirstResume || getActivity() == null) {
                return;
            }
            if (this.mHasShowSuppServiceDialog) {
                getActivity().finish();
                return;
            }
            if (MiuiPhoneUtils.maybeShowDialogForSuppService(this.mPhone, 1)) {
                this.mHasShowSuppServiceDialog = true;
            } else if (MiuiPhoneUtils.maybeShowDataInUseForViceCard(this.mPhone, 1)) {
                showViceDataInUseDialog();
            } else {
                initCallforwarding();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initCallforwarding() {
            if (this.mIcicle == null) {
                if (this.DBG) {
                    Log.d("GsmUmtsCallForwardOptions", "start to init ");
                }
                if (!this.mPhone.getIccCard().getIccFdnEnabled()) {
                    ((CallForwardEditPreference) this.mPreferences.get(this.mInitIndex)).init(this, false, this.mSubscription);
                }
            } else {
                this.mInitIndex = this.mPreferences.size();
                ArrayList arrayList = this.mPreferences;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    CallForwardEditPreference callForwardEditPreference = (CallForwardEditPreference) obj;
                    Bundle bundle = (Bundle) this.mIcicle.getParcelable(callForwardEditPreference.getKey());
                    callForwardEditPreference.setToggled(bundle.getBoolean("toggle"));
                    CallForwardInfo callForwardInfo = new CallForwardInfo();
                    callForwardInfo.number = bundle.getString("number");
                    callForwardInfo.status = bundle.getInt(a.J);
                    callForwardEditPreference.handleCallForwardResult(callForwardInfo);
                    callForwardEditPreference.handleCallForwardResult(callForwardInfo, bundle.getBoolean("enabled"));
                    callForwardEditPreference.init(this, true, this.mSubscription);
                }
            }
            this.mFirstResume = false;
            this.mIcicle = null;
        }

        @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
        public void onSaveInstanceState(Bundle bundle) {
            super.onSaveInstanceState(bundle);
            ArrayList arrayList = this.mPreferences;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                CallForwardEditPreference callForwardEditPreference = (CallForwardEditPreference) obj;
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("toggle", callForwardEditPreference.isToggled());
                CallForwardInfo callForwardInfo = callForwardEditPreference.callForwardInfo;
                if (callForwardInfo != null) {
                    bundle2.putString("number", callForwardInfo.number);
                    bundle2.putInt(a.J, callForwardEditPreference.callForwardInfo.status);
                    bundle2.putBoolean("enabled", callForwardEditPreference.isEnabled());
                }
                bundle.putParcelable(callForwardEditPreference.getKey(), bundle2);
            }
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.TimeConsumingPreferenceListener
        public void onFinished(Preference preference, boolean z) {
            if (this.mInitIndex < this.mPreferences.size() - 1 && getActivity() != null && !getActivity().isFinishing()) {
                int i = this.mInitIndex + 1;
                this.mInitIndex = i;
                ((CallForwardEditPreference) this.mPreferences.get(i)).init(this, false, this.mSubscription);
            }
            super.onFinished(preference, z);
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.TimeConsumingPreferenceListener
        public void onError(Preference preference, int i) {
            super.onError(preference, i);
            preference.setEnabled(true);
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, com.android.phone.settings.TimeConsumingPreferenceListener
        public void onException(Preference preference, CommandException commandException) {
            super.onException(preference, commandException);
            preference.setEnabled(true);
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment, androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            ArrayList arrayList = this.mPreferences;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                ((CallForwardEditPreference) obj).onDestroy();
            }
            AlertDialog alertDialog = this.mDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                return;
            }
            this.mDialog.dismiss();
        }

        private void showViceDataInUseDialog() {
            if (this.mDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.vice_card_data_in_use_alert).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.GsmUmtsCallForwardOptions.GsmUmtsCallForwardOptionsFragment.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        GsmUmtsCallForwardOptionsFragment.this.initCallforwarding();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.GsmUmtsCallForwardOptions.GsmUmtsCallForwardOptionsFragment.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (GsmUmtsCallForwardOptionsFragment.this.getActivity() != null) {
                            GsmUmtsCallForwardOptionsFragment.this.getActivity().finish();
                        }
                    }
                }).setCancelable(false);
                this.mDialog = builder.create();
            }
            this.mDialog.show();
        }

        @Override // androidx.fragment.app.Fragment
        public void onActivityResult(int i, int i2, Intent intent) {
            String string;
            if (this.DBG) {
                Log.d("GsmUmtsCallForwardOptions", "onActivityResult: done");
            }
            if (i2 != -1) {
                Log.d("GsmUmtsCallForwardOptions", "onActivityResult: contact picker result not OK.");
                return;
            }
            Cursor cursorQuery = null;
            try {
                Uri data = intent.getData();
                if (data.getScheme().equals(GenericURI.TEL)) {
                    string = data.getSchemeSpecificPart();
                } else {
                    cursorQuery = getContext().getContentResolver().query(intent.getData(), GsmUmtsCallForwardOptions.NUM_PROJECTION, null, null, null);
                    if (cursorQuery != null && cursorQuery.moveToFirst()) {
                        string = cursorQuery.getString(0);
                    }
                    Log.d("GsmUmtsCallForwardOptions", "onActivityResult: bad contact data, no results found.");
                    if (cursorQuery != null) {
                        return;
                    } else {
                        return;
                    }
                }
                if (EnterpriseManagerStub.ENTERPRISE_ACTIVATED && PhoneHelperStub.getInstance().getPhoneNumberHideState() == 1) {
                    string = PhoneHelperStub.getInstance().getEpPhoneNumber(string);
                }
                if (i == 0) {
                    this.mButtonCFU.onPickActivityResult(string);
                } else if (i == 1) {
                    this.mButtonCFB.onPickActivityResult(string);
                } else if (i == 2) {
                    this.mButtonCFNRy.onPickActivityResult(string);
                } else if (i == 3) {
                    this.mButtonCFNRc.onPickActivityResult(string);
                }
            } catch (IllegalArgumentException unused) {
            } catch (Exception unused2) {
            } finally {
                if (0 != 0) {
                    cursorQuery.close();
                }
            }
        }
    }
}
