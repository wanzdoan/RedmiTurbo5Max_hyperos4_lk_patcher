package com.android.phone.settings;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.mediatek.settings.CallSettingUtils;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilitySettingsFragment extends PreferenceFragment {
    private static final String LOG_TAG = AccessibilitySettingsFragment.class.getSimpleName();
    private AudioManager mAudioManager;
    private SwitchPreference mButtonDualMic;
    private SwitchPreference mButtonHac;
    private SwitchPreference mButtonRtt;
    private TtyModeListPreference mButtonTty;
    private Context mContext;
    private final TelephonyCallback mTelephonyCallback = new AccessibilityTelephonyCallback();
    private final ContentObserver mTtyObserver = new ContentObserver(new Handler()) { // from class: com.android.phone.settings.AccessibilitySettingsFragment.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (AccessibilitySettingsFragment.this.mButtonTty != null) {
                Log.d(AccessibilitySettingsFragment.LOG_TAG, " --- TTY mode changed ---");
                AccessibilitySettingsFragment.this.mButtonTty.init();
            }
        }
    };

    final class AccessibilityTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private AccessibilityTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            Log.d(AccessibilitySettingsFragment.LOG_TAG, "PhoneStateListener.onCallStateChanged: state=" + i);
            Preference preferenceFindPreference = AccessibilitySettingsFragment.this.getPreferenceScreen().findPreference("button_tty_mode_key");
            if (preferenceFindPreference != null) {
                preferenceFindPreference.setEnabled((AccessibilitySettingsFragment.this.isVolteTtySupportedInAnySlot() && !AccessibilitySettingsFragment.this.isVideoCallOrConferenceInProgress()) || ((TelephonyManager) AccessibilitySettingsFragment.this.mContext.getSystemService("phone")).getCallState() == 0);
            }
        }
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Context applicationContext = getActivity().getApplicationContext();
        this.mContext = applicationContext;
        this.mAudioManager = (AudioManager) applicationContext.getSystemService("audio");
        addPreferencesFromResource(R.xml.accessibility_settings);
        this.mButtonTty = (TtyModeListPreference) findPreference(getResources().getString(R.string.tty_mode_key));
        this.mButtonHac = (SwitchPreference) findPreference("button_hac_key");
        this.mButtonRtt = (SwitchPreference) findPreference("button_rtt_key");
        if (PhoneGlobals.getInstance().phoneMgr.isTtyModeSupported() && isTtySupportedByCarrier()) {
            this.mButtonTty.init();
        } else {
            getPreferenceScreen().removePreference(this.mButtonTty);
            this.mButtonTty = null;
        }
        if (CallSettingUtils.isHacSupport()) {
            this.mButtonHac.setChecked(Settings.System.getInt(this.mContext.getContentResolver(), "hearing_aid", 0) == 1);
        } else {
            getPreferenceScreen().removePreference(this.mButtonHac);
            this.mButtonHac = null;
        }
        if (shouldShowRttSetting()) {
            boolean zIsNetworkRoaming = ((TelephonyManager) this.mContext.getSystemService("phone")).isNetworkRoaming(SubscriptionManager.getDefaultVoiceSubscriptionId());
            boolean zIsCarrierAllowRttWhenRoaming = isCarrierAllowRttWhenRoaming(SubscriptionManager.getDefaultVoiceSubscriptionId());
            if (zIsNetworkRoaming && !isOnWfc() && !zIsCarrierAllowRttWhenRoaming) {
                this.mButtonRtt.setSummary(TextUtils.concat(getText(R.string.rtt_mode_summary), "\n", getText(R.string.no_rtt_when_roaming)));
            }
            boolean z = Settings.Secure.getInt(this.mContext.getContentResolver(), "rtt_calling_mode", 0) != 0;
            Log.d(LOG_TAG, "is rtt on: " + z);
            this.mButtonRtt.setChecked(z);
        } else {
            getPreferenceScreen().removePreference(this.mButtonRtt);
            getPreferenceScreen().removePreference(findPreference("button_rtt_more_information_key"));
            this.mButtonRtt = null;
        }
        initUi(getPreferenceScreen());
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        ((TelephonyManager) this.mContext.getSystemService("phone")).registerTelephonyCallback(new HandlerExecutor(new Handler(Looper.getMainLooper())), this.mTelephonyCallback);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("preferred_tty_mode"), false, this.mTtyObserver);
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        ((TelephonyManager) this.mContext.getSystemService("phone")).unregisterTelephonyCallback(this.mTelephonyCallback);
        this.mContext.getContentResolver().unregisterContentObserver(this.mTtyObserver);
    }

    @Override // android.preference.PreferenceFragment
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == this.mButtonTty) {
            return true;
        }
        SwitchPreference switchPreference = this.mButtonHac;
        if (preference == switchPreference) {
            boolean zIsChecked = switchPreference.isChecked();
            Settings.System.putInt(this.mContext.getContentResolver(), "hearing_aid", zIsChecked ? 1 : 0);
            this.mAudioManager.setParameters("HACSetting=".concat(zIsChecked ? "ON" : "OFF"));
            return true;
        }
        if (preference == this.mButtonRtt) {
            Log.i(LOG_TAG, "RTT setting changed -- now " + this.mButtonRtt.isChecked());
            Settings.Secure.putInt(this.mContext.getContentResolver(), "rtt_calling_mode", this.mButtonRtt.isChecked() ? 1 : 0);
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
            for (int i : SubscriptionManagerService.getInstance().getActiveSubIdList(true)) {
                if (!carrierConfigManager.getConfigForSubId(i).getBoolean("ignore_rtt_mode_setting_bool", false)) {
                    ImsManager.getInstance(getContext(), SubscriptionManager.getPhoneId(i)).setRttEnabled(this.mButtonRtt.isChecked());
                }
            }
            return true;
        }
        return onPreferenceTreeClick(preference);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isVolteTtySupportedInAnySlot() {
        Phone[] phones = PhoneFactory.getPhones();
        if (phones == null) {
            Log.d(LOG_TAG, "isVolteTtySupportedInAnySlot: No phones found.");
            return false;
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        for (Phone phone : phones) {
            ImsManager imsManager = ImsManager.getInstance(this.mContext, phone.getPhoneId());
            boolean zIsVolteEnabledByPlatform = imsManager != null ? imsManager.isVolteEnabledByPlatform() : false;
            PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId());
            boolean z = configForSubId != null ? configForSubId.getBoolean("carrier_volte_tty_supported_bool") : false;
            if (zIsVolteEnabledByPlatform && z) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isVideoCallOrConferenceInProgress() {
        Phone[] phones = PhoneFactory.getPhones();
        if (phones == null) {
            Log.d(LOG_TAG, "isVideoCallOrConferenceInProgress: No phones found.");
            return false;
        }
        for (Phone phone : phones) {
            if (phone.isImsVideoCallOrConferencePresent()) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnWfc() {
        final LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(1);
        ((android.telephony.ims.ImsManager) this.mContext.getSystemService(android.telephony.ims.ImsManager.class)).getImsMmTelManager(SubscriptionManager.getDefaultSubscriptionId()).getRegistrationTransportType(Executors.newSingleThreadExecutor(), new Consumer() { // from class: com.android.phone.settings.AccessibilitySettingsFragment$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                linkedBlockingQueue.offer((Integer) obj);
            }
        });
        try {
            Integer num = (Integer) linkedBlockingQueue.poll(20L, TimeUnit.MILLISECONDS);
            return num != null && num.intValue() == 2;
        } catch (InterruptedException unused) {
        }
    }

    private boolean shouldShowRttSetting() {
        int[] activeSubIdList = SubscriptionManagerService.getInstance().getActiveSubIdList(true);
        if (activeSubIdList == null || activeSubIdList.length <= 0) {
            Log.d(LOG_TAG, "shouldShowRttSetting: no active subscriptions.");
            if (PhoneGlobals.getInstance().phoneMgr.isRttSupported(SubscriptionManager.getDefaultVoiceSubscriptionId())) {
                return true;
            }
        } else {
            for (int i : activeSubIdList) {
                if (PhoneGlobals.getInstance().phoneMgr.isRttSupported(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTtySupportedByCarrier() {
        return ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfig().getBoolean("tty_supported_bool");
    }

    private boolean isCarrierAllowRttWhenRoaming(int i) {
        return PhoneGlobals.getInstance().getCarrierConfigForSubId(i).getBoolean("rtt_supported_while_roaming_bool");
    }

    private void initUi(PreferenceScreen preferenceScreen) {
        this.mButtonDualMic = (SwitchPreference) preferenceScreen.findPreference("button_dual_mic_key");
        boolean zIsMtkDualMicSupport = CallSettingUtils.isMtkDualMicSupport();
        SwitchPreference switchPreference = this.mButtonDualMic;
        if (zIsMtkDualMicSupport) {
            switchPreference.setChecked(CallSettingUtils.isDualMicModeEnabled());
        } else {
            preferenceScreen.removePreference(switchPreference);
            this.mButtonDualMic = null;
        }
    }

    private boolean onPreferenceTreeClick(Preference preference) {
        if (preference != this.mButtonDualMic) {
            return false;
        }
        Log.d(LOG_TAG, "mButtonDualmic turn on: " + this.mButtonDualMic.isChecked());
        CallSettingUtils.setDualMicMode(this.mButtonDualMic.isChecked() ? "1" : "0");
        return true;
    }
}
