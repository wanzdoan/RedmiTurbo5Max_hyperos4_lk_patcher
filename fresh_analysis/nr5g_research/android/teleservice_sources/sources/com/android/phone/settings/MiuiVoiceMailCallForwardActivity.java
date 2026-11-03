package com.android.phone.settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.TelecomManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import gov.nist.javax.sip.address.GenericURI;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiVoiceMailCallForwardActivity extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            loadFragment(new MiuiVoiceMailCallForwardFragment());
        }
    }

    public class MiuiVoiceMailCallForwardFragment extends BaseActivity.BaseFragment implements Preference.OnPreferenceClickListener {
        private PreferenceScreen mButtonCF;
        private PreferenceScreen mButtonVM;

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            super.onCreatePreferences(bundle, str);
            addPreferencesFromResource(R.xml.miui_voicemail_callforward_options);
            this.mButtonVM = (PreferenceScreen) findPreference("button_play_voicemail");
            this.mButtonCF = (PreferenceScreen) findPreference("button_full_forwarding");
            this.mButtonVM.setOnPreferenceClickListener(this);
            this.mButtonCF.setOnPreferenceClickListener(this);
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            Context context = getContext();
            if (context == null) {
                return false;
            }
            if (preference == this.mButtonVM) {
                TelecomManager telecomManager = (TelecomManager) context.getSystemService("telecom");
                Phone phone = MiuiPhoneUtils.getPhone(SubscriptionManager.getDefault().getDefaultSlotId());
                if (telecomManager != null && phone != null) {
                    telecomManager.placeCall(Uri.fromParts(GenericURI.TEL, phone.getVoiceMailNumber(), null), null);
                }
                return true;
            }
            if (preference != this.mButtonCF) {
                return false;
            }
            MiuiFullForwardingActivity.start(context);
            return true;
        }
    }
}
