package com.android.phone;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.MiuiSettings;
import android.util.Log;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import miui.telephony.SubscriptionManager;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class SmartCFSwitchView {
    public static final String TAG = "SmartCFSwitchView";
    private CheckBoxPreference mButtonSmartCF;
    private Context mContext;
    private boolean mIsReseting = false;
    private Phone mPhone;
    private PreferenceGroup mPrefGroup;
    private PreferenceFragment mPreferenceFragment;

    public SmartCFSwitchView(PreferenceFragment preferenceFragment, PreferenceGroup preferenceGroup, Phone phone) {
        this.mPreferenceFragment = preferenceFragment;
        this.mPrefGroup = preferenceGroup;
        this.mPhone = phone;
        this.mContext = preferenceFragment.getContext();
        this.mButtonSmartCF = (CheckBoxPreference) this.mPrefGroup.findPreference("button_smart_call_forward");
        setOnPreferenceClickListener();
        updateSmartCFButtonUI();
    }

    public void updateSmartCFButtonUI() {
        if (this.mButtonSmartCF == null || this.mPreferenceFragment == null) {
            return;
        }
        boolean zIsSmartCallForwardEnabled = MiuiSettings.Telephony.isSmartCallForwardEnabled(this.mContext.getContentResolver());
        int size = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList().size();
        if (zIsSmartCallForwardEnabled && MiuiPhoneUtils.getInsertedSimCount() == 0) {
            Log.d(TAG, "Skip Smart Call forward update since subscription info is not ready.");
            return;
        }
        if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSmartCallForwardSupported() && !zIsSmartCallForwardEnabled) {
            this.mPrefGroup.removePreference(this.mButtonSmartCF);
            Log.d(TAG, "Remove Smart Call forward since not supported.");
        } else if (TelephonyCloudController.CLOUD_CONTROLLER_SMART_CALL_FORWARD_DISABLED) {
            if (zIsSmartCallForwardEnabled) {
                SmartCallForwardManager.getInstance().setSmartCallForward(this.mContext, false, false, 0, new SmartCallForwardManager.CallBack() { // from class: com.android.phone.SmartCFSwitchView.1
                    @Override // com.android.phone.SmartCallForwardManager.CallBack
                    public void onSetSmartCallForwardResult(boolean z) {
                        if (z) {
                            SmartCFSwitchView.this.mPrefGroup.removePreference(SmartCFSwitchView.this.mButtonSmartCF);
                        }
                    }
                });
            } else {
                this.mPrefGroup.removePreference(this.mButtonSmartCF);
            }
            Log.d(TAG, "Remove Smart Call forward by cloud controller.");
        } else if (this.mPrefGroup.findPreference("button_smart_call_forward") == null) {
            Log.i(TAG, "add mButtonSmartCF");
            this.mPrefGroup.addPreference(this.mButtonSmartCF);
        }
        if (this.mPrefGroup.findPreference("button_smart_call_forward") != null) {
            this.mButtonSmartCF.setChecked(zIsSmartCallForwardEnabled);
            this.mButtonSmartCF.setEnabled(size > 1 || (size == 1 && zIsSmartCallForwardEnabled));
            if (this.mIsReseting) {
                return;
            }
            this.mIsReseting = true;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.android.phone.SmartCFSwitchView.2
                @Override // java.lang.Runnable
                public void run() {
                    if (SmartCFSwitchView.this.mPreferenceFragment != null && SmartCFSwitchView.this.mPreferenceFragment.getActivity() != null && !SmartCFSwitchView.this.mPreferenceFragment.getActivity().isFinishing()) {
                        ArrayList andUpdateOldSimSlotIds = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getAndUpdateOldSimSlotIds(SmartCFSwitchView.this.mContext);
                        SmartCFSwitchView.this.mButtonSmartCF.setEnabled(SubscriptionManager.getDefault().getAvailableSubscriptionInfoList().size() > 1);
                        if (!SmartCFSwitchView.this.mButtonSmartCF.isChecked()) {
                            SmartCFSwitchView.this.mIsReseting = false;
                            return;
                        }
                        if (andUpdateOldSimSlotIds.size() == 2) {
                            SmartCFSwitchView.this.mIsReseting = false;
                            return;
                        }
                        if (andUpdateOldSimSlotIds.isEmpty()) {
                            SmartCFSwitchView.this.mButtonSmartCF.setChecked(false);
                            MiuiSettings.Telephony.setSmartCallForwardEnabled(SmartCFSwitchView.this.mContext.getContentResolver(), false);
                            SmartCFSwitchView.this.mIsReseting = false;
                            return;
                        } else {
                            SmartCallForwardManager.getInstance().setSmartCallForward(SmartCFSwitchView.this.mContext, false, false, ((Integer) andUpdateOldSimSlotIds.get(0)).intValue(), new SmartCallForwardManager.CallBack() { // from class: com.android.phone.SmartCFSwitchView.2.1
                                @Override // com.android.phone.SmartCallForwardManager.CallBack
                                public void onSetSmartCallForwardResult(boolean z) {
                                    SmartCFSwitchView.this.mButtonSmartCF.setChecked(false);
                                    MiuiSettings.Telephony.setSmartCallForwardEnabled(SmartCFSwitchView.this.mContext.getContentResolver(), false);
                                    if (!z) {
                                        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "smart_call_forward_dual_to_single_fail");
                                    }
                                    SmartCFSwitchView.this.mIsReseting = false;
                                }
                            });
                            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "smart_call_forward_dual_to_single");
                            return;
                        }
                    }
                    SmartCFSwitchView.this.mIsReseting = false;
                }
            }, 2000L);
        }
    }

    private void setOnPreferenceClickListener() {
        this.mButtonSmartCF.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.phone.SmartCFSwitchView.3
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                if (SmartCFSwitchView.this.mPreferenceFragment == null) {
                    Log.d(SmartCFSwitchView.TAG, "mPrefActivity == null");
                    return false;
                }
                final boolean zIsChecked = SmartCFSwitchView.this.mButtonSmartCF.isChecked();
                for (Phone phone : PhoneFactory.getPhones()) {
                    if (MiuiPhoneUtils.maybeShowDialogForSuppService(phone, 1)) {
                        SmartCFSwitchView.this.mButtonSmartCF.setChecked(!zIsChecked);
                        return false;
                    }
                }
                if (zIsChecked) {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getAndUpdateOldSimSlotIds(SmartCFSwitchView.this.mContext);
                }
                SmartCallForwardManager.getInstance().setSmartCallForward(SmartCFSwitchView.this.mContext, zIsChecked, true, 0, new SmartCallForwardManager.CallBack() { // from class: com.android.phone.SmartCFSwitchView.3.1
                    @Override // com.android.phone.SmartCallForwardManager.CallBack
                    public void onSetSmartCallForwardResult(boolean z) {
                        if (z) {
                            if (zIsChecked) {
                                return;
                            }
                            SmartCFSwitchView.this.mButtonSmartCF.setEnabled(SubscriptionManager.getDefault().getAvailableSubscriptionInfoList().size() > 1);
                            return;
                        }
                        SmartCFSwitchView.this.mButtonSmartCF.setChecked(!zIsChecked);
                    }
                });
                return true;
            }
        });
    }

    public void destroy() {
        SmartCallForwardManager.getInstance().clear();
    }
}
