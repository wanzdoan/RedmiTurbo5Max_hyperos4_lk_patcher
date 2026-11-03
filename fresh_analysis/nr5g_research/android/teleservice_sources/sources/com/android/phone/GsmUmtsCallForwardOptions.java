package com.android.phone;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.UserHandle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.Phone;
import com.mediatek.settings.vtss.GsmUmtsVTUtils;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class GsmUmtsCallForwardOptions extends TimeConsumingPreferenceActivity implements PhoneGlobals.SubInfoUpdateListener {
    private static final String[] NUM_PROJECTION = {"data1"};
    private CallForwardEditPreference mButtonCFB;
    private CallForwardEditPreference mButtonCFNRc;
    private CallForwardEditPreference mButtonCFNRy;
    private CallForwardEditPreference mButtonCFU;
    private boolean mCallForwardByUssd;
    private boolean mFirstResume;
    private Bundle mIcicle;
    private IntentFilter mIntentFilter;
    private Phone mPhone;
    private boolean mReplaceInvalidCFNumbers;
    private SubscriptionInfoHelper mSubscriptionInfoHelper;
    private final ArrayList mPreferences = new ArrayList();
    private int mInitIndex = 0;
    private boolean mUpdated = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.GsmUmtsCallForwardOptions.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction()) && intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false)) {
                Log.d("GsmUmtsCallForwardOptions", "finish activity in airplane mode");
                GsmUmtsCallForwardOptions.this.finish();
            }
        }
    };

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        PersistableBundle carrierConfig;
        boolean z;
        boolean z2;
        boolean z3;
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        addPreferencesFromResource(R.xml.callforward_options);
        SubscriptionInfoHelper subscriptionInfoHelper = new SubscriptionInfoHelper(this, getIntent());
        this.mSubscriptionInfoHelper = subscriptionInfoHelper;
        subscriptionInfoHelper.setActionBarTitle(getActionBar(), getResources(), R.string.call_forwarding_settings_with_label);
        this.mPhone = this.mSubscriptionInfoHelper.getPhone();
        PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
        registerCallBacks();
        if (this.mPhone == null) {
            Log.d("GsmUmtsCallForwardOptions", "onCreate: mPhone is null, finish!!!");
            finish();
            return;
        }
        if (this.mSubscriptionInfoHelper.hasSubId()) {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mSubscriptionInfoHelper.getSubId());
        } else {
            carrierConfig = PhoneGlobals.getInstance().getCarrierConfig();
        }
        if (carrierConfig != null) {
            this.mReplaceInvalidCFNumbers = carrierConfig.getBoolean("call_forwarding_map_non_number_to_voicemail_bool");
            this.mCallForwardByUssd = carrierConfig.getBoolean("use_call_forwarding_ussd_bool");
            z2 = carrierConfig.getBoolean("call_forwarding_when_busy_supported_bool");
            z3 = carrierConfig.getBoolean("call_forwarding_when_unreachable_supported_bool");
            z = carrierConfig.getBoolean("call_forwarding_when_unanswered_supported_bool");
        } else {
            z = true;
            z2 = true;
            z3 = true;
        }
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        this.mButtonCFU = (CallForwardEditPreference) preferenceScreen.findPreference("button_cfu_key");
        this.mButtonCFB = (CallForwardEditPreference) preferenceScreen.findPreference("button_cfb_key");
        this.mButtonCFNRy = (CallForwardEditPreference) preferenceScreen.findPreference("button_cfnry_key");
        this.mButtonCFNRc = (CallForwardEditPreference) preferenceScreen.findPreference("button_cfnrc_key");
        int intExtra = getIntent().getIntExtra("service_class", 1);
        int i = 0;
        if (intExtra != 1) {
            GsmUmtsVTUtils.setCFServiceClass(preferenceScreen, intExtra);
            this.mSubscriptionInfoHelper.setActionBarTitle(getActionBar(), getResources(), GsmUmtsVTUtils.getActionBarResId(intExtra, 0));
        }
        CallForwardEditPreference callForwardEditPreference = this.mButtonCFU;
        callForwardEditPreference.setParentActivity(this, callForwardEditPreference.reason);
        CallForwardEditPreference callForwardEditPreference2 = this.mButtonCFB;
        callForwardEditPreference2.setParentActivity(this, callForwardEditPreference2.reason);
        CallForwardEditPreference callForwardEditPreference3 = this.mButtonCFNRy;
        callForwardEditPreference3.setParentActivity(this, callForwardEditPreference3.reason);
        CallForwardEditPreference callForwardEditPreference4 = this.mButtonCFNRc;
        callForwardEditPreference4.setParentActivity(this, callForwardEditPreference4.reason);
        this.mPreferences.add(this.mButtonCFU);
        layoutCallForwardItem(z2, this.mButtonCFB, preferenceScreen);
        layoutCallForwardItem(z, this.mButtonCFNRy, preferenceScreen);
        layoutCallForwardItem(z3, this.mButtonCFNRc, preferenceScreen);
        if (this.mCallForwardByUssd) {
            preferenceScreen.removePreference(this.mButtonCFU);
            preferenceScreen.removePreference(this.mButtonCFB);
            preferenceScreen.removePreference(this.mButtonCFNRc);
            this.mPreferences.remove(this.mButtonCFU);
            this.mPreferences.remove(this.mButtonCFB);
            this.mPreferences.remove(this.mButtonCFNRc);
            this.mButtonCFNRy.setDependency(null);
        }
        this.mFirstResume = true;
        this.mIcicle = bundle;
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (this.mIcicle != null) {
            ArrayList arrayList = this.mPreferences;
            int size = arrayList.size();
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                CallForwardEditPreference callForwardEditPreference5 = (CallForwardEditPreference) obj;
                boolean z4 = ((Bundle) this.mIcicle.getParcelable(callForwardEditPreference5.getKey())).getBoolean("toggle");
                callForwardEditPreference5.setToggled(z4);
                if (callForwardEditPreference5.getKey().equals("button_cfu_key") && z4) {
                    Log.d("GsmUmtsCallForwardOptions", "Need update other CF items");
                    this.mUpdated = true;
                }
            }
        }
    }

    private void layoutCallForwardItem(boolean z, CallForwardEditPreference callForwardEditPreference, PreferenceScreen preferenceScreen) {
        if (z) {
            this.mPreferences.add(callForwardEditPreference);
        } else {
            preferenceScreen.removePreference(callForwardEditPreference);
        }
    }

    @Override // com.android.phone.TimeConsumingPreferenceActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.mFirstResume) {
            if (this.mIcicle == null) {
                Log.d("GsmUmtsCallForwardOptions", "start to init ");
                CallForwardEditPreference callForwardEditPreference = (CallForwardEditPreference) this.mPreferences.get(this.mInitIndex);
                callForwardEditPreference.init(this, this.mPhone, this.mReplaceInvalidCFNumbers, this.mCallForwardByUssd);
                callForwardEditPreference.startCallForwardOptionsQuery();
            } else {
                this.mInitIndex = this.mPreferences.size();
                ArrayList arrayList = this.mPreferences;
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    CallForwardEditPreference callForwardEditPreference2 = (CallForwardEditPreference) obj;
                    Bundle bundle = (Bundle) this.mIcicle.getParcelable(callForwardEditPreference2.getKey());
                    callForwardEditPreference2.setToggled(bundle.getBoolean("toggle"));
                    callForwardEditPreference2.setEnabled(bundle.getBoolean("enable"));
                    CallForwardInfo callForwardInfo = new CallForwardInfo();
                    callForwardInfo.number = bundle.getString("number");
                    callForwardInfo.status = bundle.getInt(a.J);
                    callForwardEditPreference2.init(this, this.mPhone, this.mReplaceInvalidCFNumbers, this.mCallForwardByUssd);
                    callForwardEditPreference2.restoreCallForwardInfo(callForwardInfo);
                }
            }
            this.mFirstResume = false;
            this.mIcicle = null;
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
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
            bundle2.putBoolean("enable", callForwardEditPreference.isEnabled());
            CallForwardInfo callForwardInfo = callForwardEditPreference.callForwardInfo;
            if (callForwardInfo != null) {
                bundle2.putString("number", callForwardInfo.number);
                bundle2.putInt(a.J, callForwardEditPreference.callForwardInfo.status);
            }
            bundle.putParcelable(callForwardEditPreference.getKey(), bundle2);
        }
    }

    @Override // com.android.phone.TimeConsumingPreferenceActivity, com.android.phone.TimeConsumingPreferenceListener
    public void onFinished(Preference preference, boolean z) {
        if (preference instanceof CallForwardEditPreference) {
        }
        if (this.mInitIndex < this.mPreferences.size() - 1 && !isFinishing()) {
            int i = this.mInitIndex + 1;
            this.mInitIndex = i;
            CallForwardEditPreference callForwardEditPreference = (CallForwardEditPreference) this.mPreferences.get(i);
            callForwardEditPreference.init(this, this.mPhone, this.mReplaceInvalidCFNumbers, this.mCallForwardByUssd);
            callForwardEditPreference.startCallForwardOptionsQuery();
        }
        super.onFinished(preference, z);
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        Log.d("GsmUmtsCallForwardOptions", "onActivityResult: done");
        if (i2 != -1) {
            Log.d("GsmUmtsCallForwardOptions", "onActivityResult: contact picker result not OK.");
            return;
        }
        Cursor cursor = null;
        try {
            int userId = UserHandle.getUserId(Process.myUid());
            if (userId != ContentProvider.getUserIdFromUri(intent.getData(), userId)) {
                Log.w("GsmUmtsCallForwardOptions", "onActivityResult: Contact data of different user, cannot access");
                return;
            }
            Cursor cursorQuery = getContentResolver().query(intent.getData(), NUM_PROJECTION, null, null, null);
            if (cursorQuery != null && cursorQuery.moveToFirst()) {
                if (i == 0) {
                    this.mButtonCFU.onPickActivityResult(cursorQuery.getString(0));
                } else if (i == 1) {
                    this.mButtonCFB.onPickActivityResult(cursorQuery.getString(0));
                } else if (i == 2) {
                    this.mButtonCFNRy.onPickActivityResult(cursorQuery.getString(0));
                } else if (i == 3) {
                    this.mButtonCFNRc.onPickActivityResult(cursorQuery.getString(0));
                }
                cursorQuery.close();
                return;
            }
            Log.d("GsmUmtsCallForwardOptions", "onActivityResult: bad contact data, no results found.");
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
                throw th;
            }
            throw th;
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            CallFeaturesSetting.goUpToTopLevelSetting(this, this.mSubscriptionInfoHelper);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // com.android.phone.TimeConsumingPreferenceActivity, com.android.phone.TimeConsumingPreferenceListener
    public void onUpdated(Preference preference) {
        CallForwardInfo callForwardInfo;
        Log.d("GsmUmtsCallForwardOptions", "onUpdated, preference=" + preference.getKey());
        if ((preference instanceof CallForwardEditPreference) && this.mUpdated) {
            CallForwardEditPreference callForwardEditPreference = (CallForwardEditPreference) preference;
            if (callForwardEditPreference.reason == this.mButtonCFU.reason && (callForwardInfo = callForwardEditPreference.callForwardInfo) != null && callForwardInfo.status == 0) {
                Log.d("GsmUmtsCallForwardOptions", "Update other CF items");
                this.mButtonCFB.setEnabled(true);
                this.mButtonCFNRy.setEnabled(true);
                this.mButtonCFNRc.setEnabled(true);
                this.mUpdated = false;
            }
        }
        super.onUpdated(preference);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        boolean z = configuration.orientation == 1;
        Log.d("GsmUmtsCallForwardOptions", "onConfigurationChanged: shouldDismissDialog=" + z + ", mIsForeground=" + this.mIsForeground);
        if (!z || this.mIsForeground) {
            return;
        }
        Dialog dialog = this.mButtonCFU.getDialog();
        if (dialog != null && dialog.isShowing()) {
            Log.d("GsmUmtsCallForwardOptions", "onConfigurationChanged: showPhoneNumberDialog for CFU");
            dialog.dismiss();
            this.mButtonCFU.showPhoneNumberDialog();
            return;
        }
        Dialog dialog2 = this.mButtonCFB.getDialog();
        if (dialog2 != null && dialog2.isShowing()) {
            Log.d("GsmUmtsCallForwardOptions", "onConfigurationChanged: showPhoneNumberDialog for CFB");
            dialog2.dismiss();
            this.mButtonCFB.showPhoneNumberDialog();
            return;
        }
        Dialog dialog3 = this.mButtonCFNRy.getDialog();
        if (dialog3 != null && dialog3.isShowing()) {
            Log.d("GsmUmtsCallForwardOptions", "onConfigurationChanged: showPhoneNumberDialog for CFNRy");
            dialog3.dismiss();
            this.mButtonCFNRy.showPhoneNumberDialog();
            return;
        }
        Dialog dialog4 = this.mButtonCFNRc.getDialog();
        if (dialog4 == null || !dialog4.isShowing()) {
            return;
        }
        Log.d("GsmUmtsCallForwardOptions", "onConfigurationChanged: showPhoneNumberDialog for mButtonCFNRc");
        dialog4.dismiss();
        this.mButtonCFNRc.showPhoneNumberDialog();
    }

    private void registerCallBacks() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        this.mIntentFilter = intentFilter;
        registerReceiver(this.mReceiver, intentFilter);
    }

    @Override // android.preference.PreferenceActivity, android.app.ListActivity, android.app.Activity
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        PhoneGlobals.getInstance().removeSubInfoUpdateListener(this);
        super.onDestroy();
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        finish();
    }
}
