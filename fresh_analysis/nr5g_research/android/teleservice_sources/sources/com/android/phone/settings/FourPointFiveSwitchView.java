package com.android.phone.settings;

import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.RadioAccessFamily;
import android.telephony.SubscriptionManager;
import android.util.Log;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import com.android.internal.util.ArrayUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.services.telephony.ims.ImsAdapterBase;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class FourPointFiveSwitchView {
    public static final String TAG = "FourPointFiveSwitchView";
    private CheckBoxPreference mButtonFourPointFiveG;
    private Phone mPhone;
    private PreferenceGroup mPrefGroup;
    private PreferenceFragment mPreferenceFragment;
    private VolteSwitchView mVolteSwitchView;
    private boolean mIsAirplaneEnabled = false;
    private final Handler mHandler = new Handler() { // from class: com.android.phone.settings.FourPointFiveSwitchView.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 100) {
                return;
            }
            FourPointFiveSwitchView.this.handleFourPointFiveResponse(message);
        }
    };

    public FourPointFiveSwitchView(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, Phone phone, VolteSwitchView volteSwitchView) {
        this.mPhone = null;
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefGroup = preferenceGroup;
        this.mPhone = phone;
        this.mVolteSwitchView = volteSwitchView;
        initPreferenceUI();
    }

    private void initPreferenceUI() {
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPrefGroup.findPreference("enhanced_four_point_five_lte");
        this.mButtonFourPointFiveG = checkBoxPreference;
        checkBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.settings.FourPointFiveSwitchView.1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (FourPointFiveSwitchView.this.mPreferenceFragment == null) {
                    Log.d(FourPointFiveSwitchView.TAG, "mPrefActivity == null");
                    return false;
                }
                FourPointFiveSwitchView.this.openFourPointFiveButton();
                return true;
            }
        });
        if (this.mButtonFourPointFiveG != null) {
            Resources resourcesForSubId = SubscriptionManager.getResourcesForSubId(PhoneGlobals.getInstance().getApplicationContext(), this.mPhone.getSubId());
            this.mButtonFourPointFiveG.setTitle(resourcesForSubId.getString(R.string.four_point_five_switch_title));
            this.mButtonFourPointFiveG.setSummary(resourcesForSubId.getString(R.string.four_point_five_switch_summary));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFourPointFiveResponse(Message message) {
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult.exception != null) {
            Log.d(TAG, "set preferred network type, exception = " + asyncResult.exception);
            ModemProvider.getInstance().setNetworkModeInDb(this.mPhone.getPhoneId(), message.arg1);
            this.mButtonFourPointFiveG.setChecked(isSupportLte());
            return;
        }
        Log.d(TAG, "set preferred network type success,then handle volte");
        VolteSwitchView volteSwitchView = this.mVolteSwitchView;
        if (volteSwitchView != null) {
            volteSwitchView.setVolteSwitch(this.mButtonFourPointFiveG.isChecked());
        }
        ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(this.mPreferenceFragment.getContext(), this.mButtonFourPointFiveG.isChecked(), this.mPhone.getPhoneId());
    }

    public void updateFourPointFiveUI() {
        if (this.mButtonFourPointFiveG == null || this.mPreferenceFragment == null) {
            return;
        }
        if (this.mPrefGroup.findPreference("enhanced_four_point_five_lte") == null) {
            this.mPrefGroup.addPreference(this.mButtonFourPointFiveG);
        }
        this.mButtonFourPointFiveG.setChecked(isSupportLte());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openFourPointFiveButton() {
        int lteNetworkModeValue = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getLteNetworkModeValue(this.mPhone.getPhoneId());
        int designatedNetworkModeValueForSlot = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getDesignatedNetworkModeValueForSlot(this.mPhone.getPhoneId(), 11);
        int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(this.mPhone.getPhoneId());
        Log.d(TAG, "openFourPointFiveButton: " + this.mButtonFourPointFiveG.isChecked() + ",lteMode: " + lteNetworkModeValue + ",wcdmaMode: " + designatedNetworkModeValueForSlot + ",currentMode: " + preferredNetworkModeFromDb);
        if (!this.mButtonFourPointFiveG.isChecked()) {
            lteNetworkModeValue = designatedNetworkModeValueForSlot;
        }
        ModemProvider.getInstance().setNetworkModeInDb(this.mPhone.getPhoneId(), lteNetworkModeValue);
        this.mPhone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(lteNetworkModeValue), this.mHandler.obtainMessage(100, preferredNetworkModeFromDb, 0));
    }

    private boolean isSupportLte() {
        int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(this.mPhone.getPhoneId());
        int[] supportNetworkMode = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(7);
        Log.d(TAG, "updateFourPointFiveUI,currentMode:" + preferredNetworkModeFromDb);
        return ArrayUtils.contains(supportNetworkMode, preferredNetworkModeFromDb);
    }

    public void destroy() {
        this.mPreferenceFragment = null;
    }
}
