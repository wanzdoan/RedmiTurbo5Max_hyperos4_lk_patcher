package com.android.phone.settings;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import gov.nist.javax.sip.address.GenericURI;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class CdmaCallForwardOptions extends BaseActivity {
    private static final String[] NUM_PROJECTION = {"data1"};

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new CdmaCallForwardOptionsFragment());
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        finish();
    }

    public class CdmaCallForwardOptionsFragment extends BaseActivity.BaseFragment {
        private boolean DBG;
        private Preference mButtonAll;
        private CdmaCallForwardEditPreference mButtonCFB;
        private CdmaCallForwardEditPreference mButtonCFNRc;
        private CdmaCallForwardEditPreference mButtonCFNRy;
        private CdmaCallForwardEditPreference mButtonCFU;
        private int mSlotId;

        public CdmaCallForwardOptionsFragment() {
            this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            String string;
            Integer numValueOf = Integer.valueOf(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX);
            addPreferencesFromResource(R.xml.cdma_callforward_options);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            this.mSlotId = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            Log.d("CdmaCallForwardOptions", "onCreate mSlotId=" + this.mSlotId);
            this.mButtonCFU = (CdmaCallForwardEditPreference) preferenceScreen.findPreference("button_cfu");
            this.mButtonCFB = (CdmaCallForwardEditPreference) preferenceScreen.findPreference("button_cfb");
            this.mButtonCFNRy = (CdmaCallForwardEditPreference) preferenceScreen.findPreference("button_cfnry");
            this.mButtonCFNRc = (CdmaCallForwardEditPreference) preferenceScreen.findPreference("button_cfnrc");
            Preference preferenceFindPreference = preferenceScreen.findPreference("button_disable_all");
            this.mButtonAll = preferenceFindPreference;
            SubscriptionManager.putSlotIdExtra(preferenceFindPreference.getIntent(), this.mSlotId);
            CdmaCallForwardEditPreference cdmaCallForwardEditPreference = this.mButtonCFU;
            cdmaCallForwardEditPreference.setIntentFragment(this, cdmaCallForwardEditPreference.mReason);
            CdmaCallForwardEditPreference cdmaCallForwardEditPreference2 = this.mButtonCFB;
            cdmaCallForwardEditPreference2.setIntentFragment(this, cdmaCallForwardEditPreference2.mReason);
            CdmaCallForwardEditPreference cdmaCallForwardEditPreference3 = this.mButtonCFNRy;
            cdmaCallForwardEditPreference3.setIntentFragment(this, cdmaCallForwardEditPreference3.mReason);
            CdmaCallForwardEditPreference cdmaCallForwardEditPreference4 = this.mButtonCFNRc;
            cdmaCallForwardEditPreference4.setIntentFragment(this, cdmaCallForwardEditPreference4.mReason);
            this.mButtonCFU.setSlotId(this.mSlotId);
            this.mButtonCFB.setSlotId(this.mSlotId);
            this.mButtonCFNRy.setSlotId(this.mSlotId);
            this.mButtonCFNRc.setSlotId(this.mSlotId);
            if (HyperOSCustFeatureResolve.getBooleanForPhoneId(this.mSlotId, "set_cdma_call_forward_pref", false)) {
                preferenceScreen.removePreference(this.mButtonCFB);
                preferenceScreen.removePreference(this.mButtonCFNRy);
                preferenceScreen.removePreference(this.mButtonCFNRc);
                preferenceScreen.removePreference(this.mButtonAll);
                if (MiuiKoreaOperatorCustimizedUtils.isKrKtSim(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                    string = getActivity().getString(R.string.messageCFU_kr, new Object[]{numValueOf, "*71", "*710"});
                } else if (!MiuiKoreaOperatorCustimizedUtils.isKrSktSim(MiuiPhoneUtils.getPhone(this.mSlotId))) {
                    string = "";
                } else {
                    string = getActivity().getString(R.string.messageCFU_kr, new Object[]{numValueOf, "*71", "*73"});
                }
                if (TextUtils.isEmpty(string)) {
                    return;
                }
                this.mButtonCFU.setSummary(string);
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            this.mButtonCFU.setEnabled(true);
            this.mButtonCFB.setEnabled(true);
            this.mButtonCFNRy.setEnabled(true);
            this.mButtonCFNRc.setEnabled(true);
            Phone phone = PhoneFactory.getPhone(this.mSlotId);
            if (shouldDisableCF(phone)) {
                this.mButtonCFU.setEnabled(false);
                this.mButtonCFB.setEnabled(false);
                this.mButtonCFNRy.setEnabled(false);
                this.mButtonCFNRc.setEnabled(false);
                return;
            }
            if (MiuiPhoneUtils.isShowCallForwardingCFNRc(phone)) {
                return;
            }
            getPreferenceScreen().removePreference(this.mButtonCFNRc);
        }

        /* JADX WARN: Code duplicated, block: B:33:0x0073  */
        @Override // androidx.fragment.app.Fragment
        public void onActivityResult(int i, int i2, Intent intent) {
            String string;
            if (this.DBG) {
                Log.d("CdmaCallForwardOptions", "onActivityResult: done");
            }
            if (i2 != -1) {
                if (this.DBG) {
                    Log.d("CdmaCallForwardOptions", "onActivityResult: contact picker result not OK.");
                    return;
                }
                return;
            }
            Cursor cursorQuery = null;
            try {
                Uri data = intent.getData();
                if (data.getScheme().equals(GenericURI.TEL)) {
                    string = data.getSchemeSpecificPart();
                } else {
                    cursorQuery = getContext().getContentResolver().query(intent.getData(), CdmaCallForwardOptions.NUM_PROJECTION, null, null, null);
                    if (cursorQuery != null && cursorQuery.moveToFirst()) {
                        string = cursorQuery.getString(0);
                    }
                    Log.d("CdmaCallForwardOptions", "onActivityResult: bad contact data, no results found.");
                    if (cursorQuery != null) {
                        cursorQuery.close();
                        return;
                    }
                    return;
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (IllegalArgumentException unused) {
                if (0 != 0) {
                    cursorQuery.close();
                }
                string = "";
            } catch (Exception unused2) {
                if (0 != 0) {
                    cursorQuery.close();
                }
                string = "";
            } catch (Throwable th) {
                if (0 != 0) {
                    cursorQuery.close();
                    throw th;
                }
                throw th;
            }
            if (i == 0) {
                this.mButtonCFU.onPickActivityResult(string);
                return;
            }
            if (i == 1) {
                this.mButtonCFB.onPickActivityResult(string);
            } else if (i == 2) {
                this.mButtonCFNRy.onPickActivityResult(string);
            } else {
                if (i != 3) {
                    return;
                }
                this.mButtonCFNRc.onPickActivityResult(string);
            }
        }

        private boolean shouldDisableCF(Phone phone) {
            if (!MiuiPhoneUtils.isSimReady(this.mSlotId) || phone.getIccCard().getIccFdnEnabled()) {
                return true;
            }
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(this.mSlotId, "disable_call_forward", true) && phone.getPhoneType() != 2;
        }
    }
}
