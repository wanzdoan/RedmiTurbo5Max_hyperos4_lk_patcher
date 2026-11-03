package com.android.phone;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.SwitchPreference;

/* JADX INFO: loaded from: classes.dex */
public class NrcaSwitchView implements Preference.OnPreferenceChangeListener {
    private SwitchPreference mButtonNrca;
    private Context mContext;
    private Handler mHandler;
    private PreferenceGroup mPrefGroup;
    private int mSlotId;

    public NrcaSwitchView(PreferenceGroup preferenceGroup, Context context, int i) {
        this.mHandler = null;
        this.mPrefGroup = preferenceGroup;
        this.mSlotId = i;
        this.mContext = context;
        SwitchPreference switchPreference = (SwitchPreference) preferenceGroup.findPreference("nrca_switch" + this.mSlotId);
        this.mButtonNrca = switchPreference;
        switchPreference.setOnPreferenceChangeListener(this);
        this.mHandler = new Handler();
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        try {
            SwitchPreference switchPreference = this.mButtonNrca;
            if (preference == switchPreference) {
                setUserNrCAEnabled(!switchPreference.isChecked() ? 1 : 0, this.mSlotId);
            }
            if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                Toast.makeText(this.mContext, R.string.nrca_need_reset, 1).show();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateNrcaButtonUI() {
        if (this.mButtonNrca == null) {
            return;
        }
        if (this.mPrefGroup.findPreference("nrca_switch" + this.mSlotId) == null) {
            this.mPrefGroup.addPreference(this.mButtonNrca);
        }
        new Thread(new Runnable() { // from class: com.android.phone.NrcaSwitchView.1
            @Override // java.lang.Runnable
            public void run() {
                final boolean zShouldEnableNrcaSwitch = NrcaSwitchView.this.shouldEnableNrcaSwitch();
                NrcaSwitchView nrcaSwitchView = NrcaSwitchView.this;
                final boolean zIsNrCAEnabled = nrcaSwitchView.isNrCAEnabled(nrcaSwitchView.mSlotId);
                NrcaSwitchView.this.mHandler.post(new Runnable() { // from class: com.android.phone.NrcaSwitchView.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        NrcaSwitchView.this.mButtonNrca.setEnabled(zShouldEnableNrcaSwitch);
                        NrcaSwitchView.this.mButtonNrca.setChecked(zIsNrCAEnabled);
                    }
                });
            }
        }).start();
    }

    public boolean shouldEnableNrcaSwitch() {
        boolean z = Settings.System.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        boolean zIsIccCardActivated = MiuiPhoneUtils.isIccCardActivated(this.mSlotId);
        if (z || !zIsIccCardActivated) {
            return false;
        }
        if (isUserFiveGEnabled(this.mSlotId)) {
            return true;
        }
        Log.d("NrcaSwitchView ", "disable vonr button because sa is turned off");
        return false;
    }

    private boolean isUserFiveGEnabled(int i) {
        return FiveGManagerBase.getInstance().isUserFiveGEnabled(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNrCAEnabled(int i) {
        return FiveGManagerBase.getInstance().getFiveGCarrierAggregationStatus(i);
    }

    private void setUserNrCAEnabled(int i, int i2) {
        int subId = getSubId(i2);
        if (subId != -1) {
            Settings.Global.putInt(this.mContext.getContentResolver(), "button_5G_carrier_aggregation" + subId, i);
            FiveGManagerBase.getInstance().execSetFiveGCarrierAggregationMode(i, i2);
        }
    }

    private int getSubId(int i) {
        int[] subId = SubscriptionManager.getSubId(i);
        if (subId == null || subId.length < 1) {
            return -1;
        }
        return subId[0];
    }
}
