package com.android.phone.settings;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.AttributeSet;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class TtyModeListPreference extends ListPreference implements Preference.OnPreferenceChangeListener {
    private static final String LOG_TAG = TtyModeListPreference.class.getSimpleName();

    public TtyModeListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void init() {
        setOnPreferenceChangeListener(this);
        int i = Settings.Secure.getInt(getContext().getContentResolver(), "preferred_tty_mode", 0);
        if (shouldHideHcoAndVco()) {
            setEntries((CharSequence[]) Arrays.copyOfRange(getContext().getResources().getTextArray(R.array.tty_mode_entries), 0, 2));
            setEntryValues((CharSequence[]) Arrays.copyOfRange(getContext().getResources().getTextArray(R.array.tty_mode_values), 0, 2));
            if (i == 2 || i == 3) {
                Settings.Secure.putInt(getContext().getContentResolver(), "preferred_tty_mode", 1);
                i = 1;
            }
        }
        setValue(Integer.toString(i));
        updatePreferredTtyModeSummary(i);
    }

    @Override // android.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference == this) {
            int i = Integer.parseInt((String) obj);
            int i2 = Settings.Secure.getInt(getContext().getContentResolver(), "preferred_tty_mode", 0);
            log("handleTTYChange: requesting set TTY mode enable (TTY) to" + Integer.toString(i));
            if (i != i2) {
                if (i == 0 || i == 1 || i == 2 || i == 3) {
                    Settings.Secure.putInt(getContext().getContentResolver(), "preferred_tty_mode", i);
                } else {
                    i = 0;
                }
                setValue(Integer.toString(i));
                updatePreferredTtyModeSummary(i);
                Intent intent = new Intent("android.telecom.action.TTY_PREFERRED_MODE_CHANGED");
                intent.putExtra("android.telecom.extra.TTY_PREFERRED_MODE", i);
                getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
            }
        }
        return true;
    }

    private void updatePreferredTtyModeSummary(int i) {
        String[] stringArray = getContext().getResources().getStringArray(R.array.tty_mode_entries);
        if (i == 0 || i == 1 || i == 2 || i == 3) {
            setSummary(stringArray[i]);
        } else {
            setEnabled(false);
            setSummary(stringArray[0]);
        }
    }

    private boolean shouldHideHcoAndVco() {
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService(CarrierConfigManager.class)).getConfigForSubId(SubscriptionManager.getDefaultVoiceSubscriptionId());
        return (configForSubId != null && configForSubId.getBoolean("hide_tty_hco_vco_with_rtt", false)) && PhoneGlobals.getInstance().phoneMgr.isRttSupported(SubscriptionManager.getDefaultVoiceSubscriptionId());
    }

    private static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
