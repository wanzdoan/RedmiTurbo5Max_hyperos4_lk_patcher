package com.android.phone.settings;

import android.app.ComponentCaller;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.telecom.PhoneAccountHandle;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.Phone;
import com.android.phone.MiPhoneAppStub;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.phone.SubscriptionInfoHelper;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.mediatek.settings.CallSettingUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class VoicemailSettingsActivity extends PreferenceActivity implements DialogInterface.OnClickListener, Preference.OnPreferenceChangeListener, com.android.phone.EditPhoneNumberPreference.OnDialogClosedListener, com.android.phone.EditPhoneNumberPreference.GetDefaultNumberListener, PhoneGlobals.SubInfoUpdateListener {
    private static final String LOG_TAG = VoicemailSettingsActivity.class.getSimpleName();
    private boolean mForeground;
    private CallForwardInfo[] mNewFwdSettings;
    private String mNewVMNumber;
    private String mOldVmNumber;
    private Phone mPhone;
    private SubscriptionInfoHelper mSubscriptionInfoHelper;
    private Preference mVoicemailNotificationPreference;
    private VoicemailProviderListPreference mVoicemailProviders;
    private PreferenceScreen mVoicemailSettings;
    private CallForwardInfo[] mForwardingReadResults = null;
    private Map mForwardingChangeResults = null;
    private Collection mExpectedChangeResultReasons = null;
    private AsyncResult mVoicemailChangeResult = null;
    private String mPreviousVMProviderKey = null;
    private int mCurrentDialogId = 0;
    private boolean mVMProviderSettingsForced = false;
    private boolean mChangingVMorFwdDueToProviderChange = false;
    private boolean mVMChangeCompletedSuccessfully = false;
    private boolean mFwdChangesRequireRollback = false;
    private int mVMOrFwdSetError = 0;
    private boolean mShowVoicemailPreference = false;
    private boolean mDisallowedConfig = false;
    private com.android.phone.EditPhoneNumberPreference mSubMenuVoicemailSettings = null;
    private final Handler mGetOptionComplete = new Handler() { // from class: com.android.phone.settings.VoicemailSettingsActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (message.what != 502) {
                return;
            }
            VoicemailSettingsActivity.this.handleForwardingSettingsReadResult(asyncResult, message.arg1);
        }
    };
    private final Handler mSetOptionComplete = new Handler() { // from class: com.android.phone.settings.VoicemailSettingsActivity.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            int i = message.what;
            if (i == 500) {
                VoicemailSettingsActivity.this.mVoicemailChangeResult = asyncResult;
                VoicemailSettingsActivity voicemailSettingsActivity = VoicemailSettingsActivity.this;
                voicemailSettingsActivity.mVMChangeCompletedSuccessfully = voicemailSettingsActivity.isVmChangeSuccess();
                PhoneGlobals.getInstance().refreshMwiIndicator(VoicemailSettingsActivity.this.mSubscriptionInfoHelper.getSubId());
            } else {
                if (i != 501) {
                    return;
                }
                VoicemailSettingsActivity.this.mForwardingChangeResults.put(Integer.valueOf(message.arg1), asyncResult);
                if (asyncResult.exception != null) {
                    Log.w(VoicemailSettingsActivity.LOG_TAG, "Error in setting fwd# " + message.arg1 + ": " + asyncResult.exception.getMessage());
                }
                if (!VoicemailSettingsActivity.this.isForwardingCompleted()) {
                    return;
                }
                if (VoicemailSettingsActivity.this.isFwdChangeSuccess()) {
                    VoicemailSettingsActivity.log("Overall fwd changes completed ok, starting vm change");
                    VoicemailSettingsActivity.this.setVoicemailNumberWithCarrier();
                    return;
                }
                Log.w(VoicemailSettingsActivity.LOG_TAG, "Overall fwd changes completed in failure. Check if we need to try rollback for some settings.");
                VoicemailSettingsActivity.this.mFwdChangesRequireRollback = false;
                Iterator it = VoicemailSettingsActivity.this.mForwardingChangeResults.entrySet().iterator();
                while (it.hasNext()) {
                    if (((AsyncResult) ((Map.Entry) it.next()).getValue()).exception == null) {
                        Log.i(VoicemailSettingsActivity.LOG_TAG, "Rollback will be required");
                        VoicemailSettingsActivity.this.mFwdChangesRequireRollback = true;
                        break;
                    }
                }
                if (!VoicemailSettingsActivity.this.mFwdChangesRequireRollback) {
                    Log.i(VoicemailSettingsActivity.LOG_TAG, "No rollback needed.");
                }
            }
            VoicemailSettingsActivity.log("All VM provider related changes done");
            if (VoicemailSettingsActivity.this.mForwardingChangeResults != null) {
                VoicemailSettingsActivity.this.dismissDialogSafely(601);
            }
            VoicemailSettingsActivity.this.handleSetVmOrFwdMessage();
        }
    };
    private final Handler mRevertOptionComplete = new Handler() { // from class: com.android.phone.settings.VoicemailSettingsActivity.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            int i = message.what;
            if (i == 500) {
                VoicemailSettingsActivity.log("VM revert complete msg");
                VoicemailSettingsActivity.this.mVoicemailChangeResult = asyncResult;
            } else if (i == 501) {
                VoicemailSettingsActivity.log("FWD revert complete msg ");
                VoicemailSettingsActivity.this.mForwardingChangeResults.put(Integer.valueOf(message.arg1), asyncResult);
                if (asyncResult.exception != null) {
                    VoicemailSettingsActivity.log("Error in reverting fwd# " + message.arg1 + ": " + asyncResult.exception.getMessage());
                }
            }
            if (VoicemailSettingsActivity.this.mVMChangeCompletedSuccessfully && VoicemailSettingsActivity.this.mVoicemailChangeResult == null) {
                return;
            }
            if (!VoicemailSettingsActivity.this.mFwdChangesRequireRollback || VoicemailSettingsActivity.this.isForwardingCompleted()) {
                VoicemailSettingsActivity.log("All VM reverts done");
                VoicemailSettingsActivity.this.dismissDialogSafely(603);
                VoicemailSettingsActivity.this.onRevertDone();
            }
        }
    };

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        UserManager userManager = (UserManager) getApplicationContext().getSystemService(UserManager.class);
        boolean z = false;
        if (!userManager.isAdminUser()) {
            Toast.makeText(this, R.string.voice_number_setting_primary_user_only, 0).show();
            finish();
            return;
        }
        if (userManager.hasUserRestriction("no_config_mobile_networks")) {
            this.mDisallowedConfig = true;
            Log.i(LOG_TAG, "Mobile network configs are restricted, disabling voicemail settings");
        }
        if (bundle == null && TextUtils.equals(getIntent().getAction(), "com.android.phone.CallFeaturesSetting.ADD_VOICEMAIL")) {
            z = true;
        }
        this.mShowVoicemailPreference = z;
        PhoneAccountHandle phoneAccountHandle = (PhoneAccountHandle) getIntent().getParcelableExtra("android.telephony.extra.PHONE_ACCOUNT_HANDLE");
        if (phoneAccountHandle != null) {
            getIntent().putExtra("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", PhoneUtils.getSubIdForPhoneAccountHandle(phoneAccountHandle));
        }
        if (MiPhoneAppStub.get().startVoicemailSettingsActivity(this)) {
            return;
        }
        this.mSubscriptionInfoHelper = new SubscriptionInfoHelper(this, getIntent());
        log("onCreate subId: " + this.mSubscriptionInfoHelper.getSubId());
        this.mSubscriptionInfoHelper.setActionBarTitle(getActionBar(), getResources(), R.string.voicemail_settings_with_label);
        this.mPhone = this.mSubscriptionInfoHelper.getPhone();
        addPreferencesFromResource(R.xml.voicemail_settings);
        this.mVoicemailNotificationPreference = findPreference(getString(R.string.voicemail_notifications_key));
        if (this.mSubMenuVoicemailSettings == null) {
            this.mSubMenuVoicemailSettings = (com.android.phone.EditPhoneNumberPreference) findPreference("button_voicemail_key");
        }
        Intent intent = new Intent("android.settings.CHANNEL_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.CHANNEL_ID", "voiceMail");
        intent.putExtra("android.provider.extra.APP_PACKAGE", this.mPhone.getContext().getPackageName());
        this.mVoicemailNotificationPreference.setIntent(intent);
        SettingsConstants.setupEdgeToEdge(this);
        PhoneGlobals.getInstance().addSubInfoUpdateListener(this);
        VoicemailProviderListPreference voicemailProviderListPreference = (VoicemailProviderListPreference) findPreference("button_voicemail_provider_key");
        this.mVoicemailProviders = voicemailProviderListPreference;
        voicemailProviderListPreference.init(this.mPhone, getIntent());
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mForeground = true;
        com.android.phone.EditPhoneNumberPreference editPhoneNumberPreference = this.mSubMenuVoicemailSettings;
        if (editPhoneNumberPreference != null) {
            editPhoneNumberPreference.setParentActivity(this, 1, this);
            this.mSubMenuVoicemailSettings.setDialogOnClosedListener(this);
            this.mSubMenuVoicemailSettings.setDialogTitle(R.string.voicemail_settings_number_label);
            if (!getBooleanCarrierConfig("editable_voicemail_number_setting_bool") || this.mDisallowedConfig) {
                this.mSubMenuVoicemailSettings.setEnabled(false);
            }
        }
        this.mVoicemailProviders.setOnPreferenceChangeListener(this);
        this.mPreviousVMProviderKey = this.mVoicemailProviders.getValue();
        PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference("button_voicemail_setting_key");
        this.mVoicemailSettings = preferenceScreen;
        preferenceScreen.setDialogFitsSystemWindows(true);
        maybeHidePublicSettings();
        updateVMPreferenceWidgets(this.mVoicemailProviders.getValue());
        if (this.mShowVoicemailPreference) {
            log("ACTION_ADD_VOICEMAIL Intent is thrown");
            if (this.mVoicemailProviders.hasMoreThanOneVoicemailProvider()) {
                log("Voicemail data has more than one provider.");
                simulatePreferenceClick(this.mVoicemailProviders);
            } else {
                onPreferenceChange(this.mVoicemailProviders, "");
                this.mVoicemailProviders.setValue("");
            }
            this.mShowVoicemailPreference = false;
        }
        updateVoiceNumberField();
        this.mVMProviderSettingsForced = false;
        Dialog dialog = this.mVoicemailSettings.getDialog();
        if (dialog != null) {
            dialog.getActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void maybeHidePublicSettings() {
        if (getIntent().getBooleanExtra("android.telephony.extra.HIDE_PUBLIC_SETTINGS", false)) {
            log("maybeHidePublicSettings: settings hidden by EXTRA_HIDE_PUBLIC_SETTINGS");
            getPreferenceScreen().removePreference(this.mVoicemailNotificationPreference);
        }
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        this.mForeground = false;
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.preference.PreferenceActivity
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == this.mSubMenuVoicemailSettings) {
            return true;
        }
        if (preference.getKey().equals(this.mVoicemailSettings.getKey())) {
            log("onPreferenceTreeClick: Voicemail Settings Preference is clicked.");
            Dialog dialog = ((PreferenceScreen) preference).getDialog();
            if (dialog != null) {
                dialog.getActionBar().setDisplayHomeAsUpEnabled(false);
            }
            com.android.phone.EditPhoneNumberPreference editPhoneNumberPreference = (com.android.phone.EditPhoneNumberPreference) findPreference("button_voicemail_key");
            this.mSubMenuVoicemailSettings = editPhoneNumberPreference;
            editPhoneNumberPreference.setParentActivity(this, 1, this);
            this.mSubMenuVoicemailSettings.setDialogOnClosedListener(this);
            this.mSubMenuVoicemailSettings.setDialogTitle(R.string.voicemail_settings_number_label);
            updateVoiceNumberField();
            if (preference.getIntent() != null) {
                log("Invoking cfg intent " + preference.getIntent().getPackage());
                startActivityForResult(preference.getIntent(), 2);
                return true;
            }
            log("onPreferenceTreeClick(). No intent; use default behavior in xml.");
            this.mPreviousVMProviderKey = "";
            this.mVMProviderSettingsForced = false;
        }
        return false;
    }

    @Override // android.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        log("onPreferenceChange: \"" + preference + "\" changed to \"" + obj + "\"");
        if (preference == this.mVoicemailProviders) {
            String str = (String) obj;
            if (this.mPreviousVMProviderKey.equals(str)) {
                log("No change is made to the VM provider setting.");
                return true;
            }
            updateVMPreferenceWidgets(str);
            VoicemailProviderSettings voicemailProviderSettingsLoad = VoicemailProviderSettingsUtil.load(this, str);
            if (voicemailProviderSettingsLoad == null) {
                Log.w(LOG_TAG, "Saved preferences not found - invoking config");
                this.mVMProviderSettingsForced = true;
                simulatePreferenceClick(this.mVoicemailSettings);
            } else {
                log("Saved preferences found - switching to them");
                this.mChangingVMorFwdDueToProviderChange = true;
                saveVoiceMailAndForwardingNumber(str, voicemailProviderSettingsLoad);
            }
        }
        return true;
    }

    @Override // com.android.phone.EditPhoneNumberPreference.GetDefaultNumberListener
    public String onGetDefaultNumber(com.android.phone.EditPhoneNumberPreference editPhoneNumberPreference) {
        if (editPhoneNumberPreference == this.mSubMenuVoicemailSettings) {
            log("updating default for voicemail dialog");
            updateVoiceNumberField();
            return null;
        }
        String voiceMailNumber = this.mPhone.getVoiceMailNumber();
        if (TextUtils.isEmpty(voiceMailNumber)) {
            return null;
        }
        log("updating default for call forwarding dialogs");
        return getString(R.string.voicemail_abbreviated) + " " + voiceMailNumber;
    }

    /* JADX WARN: Code duplicated, block: B:24:0x00b1  */
    /* JADX WARN: Code duplicated, block: B:26:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:28:0x00bc  */
    /* JADX WARN: Code duplicated, block: B:71:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        String stringExtra;
        ?? r2;
        log("onActivityResult: requestCode: " + i + ", resultCode: " + i2 + ", data: " + intent);
        ?? r3 = 0;
        r3 = 0;
        boolean z = false;
        if (i != 2) {
            if (i != 1) {
                super.onActivityResult(i, i2, intent);
                return;
            }
            if (i2 != -1) {
                log("onActivityResult: contact picker result not OK.");
                return;
            }
            try {
                ComponentCaller currentCaller = getCurrentCaller();
                if (currentCaller == null) {
                    Log.e(LOG_TAG, "onActivityResult: Current caller is null, cannot check permissions.");
                    return;
                }
                Uri data = intent != null ? intent.getData() : null;
                if (data == null) {
                    Log.w(LOG_TAG, "onActivityResult: Intent data or contact URI is null.");
                    return;
                }
                if (currentCaller.checkContentUriPermission(data, 1) == -1) {
                    EventLog.writeEvent(1397638484, "337785563", Integer.valueOf(currentCaller.getUid()), "Permission denied, cannot access contact");
                    throw new SecurityException(String.format("Permission denial: Caller (uid=%d, pkg=%s) lacks specific permission grant %s to access contact URI %s.", Integer.valueOf(currentCaller.getUid()), currentCaller.getPackage(), "FLAG_GRANT_READ_URI_PERMISSION", data));
                }
                Cursor cursorQuery = getContentResolver().query(intent.getData(), new String[]{"data1"}, null, null, null);
                if (cursorQuery != null && cursorQuery.moveToFirst()) {
                    com.android.phone.EditPhoneNumberPreference editPhoneNumberPreference = this.mSubMenuVoicemailSettings;
                    if (editPhoneNumberPreference != null) {
                        editPhoneNumberPreference.onPickActivityResult(cursorQuery.getString(0));
                    } else {
                        Log.w(LOG_TAG, "VoicemailSettingsActivity destroyed while setting contacts.");
                    }
                    cursorQuery.close();
                    return;
                }
                log("onActivityResult: bad contact data, no results found.");
                if (cursorQuery != null) {
                    cursorQuery.close();
                    return;
                }
                return;
            } catch (Throwable th) {
                if (0 != 0) {
                    r3.close();
                    throw th;
                }
                throw th;
            }
        }
        log("mVMProviderSettingsForced: " + this.mVMProviderSettingsForced);
        boolean z2 = this.mVMProviderSettingsForced;
        this.mVMProviderSettingsForced = false;
        if (i2 != -1) {
            log("onActivityResult: vm provider cfg result not OK.");
        } else if (intent == null) {
            log("onActivityResult: vm provider cfg result has no data");
        } else {
            if (intent.getBooleanExtra("com.android.phone.Signout", false)) {
                log("Provider requested signout");
                if (z2) {
                    log("Going back to previous provider on signout");
                    switchToPreviousVoicemailProvider();
                    return;
                }
                String key = this.mVoicemailProviders.getKey();
                log("Relaunching activity and ignoring " + key);
                Intent intent2 = new Intent("com.android.phone.CallFeaturesSetting.ADD_VOICEMAIL");
                intent2.putExtra("com.android.phone.ProviderToIgnore", key);
                intent2.setFlags(67108864);
                startActivityAsUser(intent2, UserHandle.CURRENT);
                return;
            }
            stringExtra = intent.getStringExtra("com.android.phone.VoicemailNumber");
            if (stringExtra == null || stringExtra.length() == 0) {
                r2 = stringExtra;
                log("onActivityResult: vm provider cfg result has no vmnum");
                r3 = stringExtra;
            }
            if (z) {
                log("Failure in return from voicemail provider.");
                if (z2) {
                    switchToPreviousVoicemailProvider();
                    return;
                }
                return;
            }
            this.mChangingVMorFwdDueToProviderChange = z2;
            String stringExtra2 = intent.getStringExtra("com.android.phone.ForwardingNumber");
            int intExtra = intent.getIntExtra("com.android.phone.ForwardingNumberTime", 20);
            CallSettingUtils.sensitiveLog(LOG_TAG, "onActivityResult: cfg result has forwarding number ", stringExtra2);
            saveVoiceMailAndForwardingNumber(this.mVoicemailProviders.getKey(), new VoicemailProviderSettings(r2, stringExtra2, intExtra));
        }
        z = true;
        r2 = r3;
        if (z) {
            log("Failure in return from voicemail provider.");
            if (z2) {
                switchToPreviousVoicemailProvider();
                return;
            }
            return;
        }
        this.mChangingVMorFwdDueToProviderChange = z2;
        String stringExtra3 = intent.getStringExtra("com.android.phone.ForwardingNumber");
        int intExtra2 = intent.getIntExtra("com.android.phone.ForwardingNumberTime", 20);
        CallSettingUtils.sensitiveLog(LOG_TAG, "onActivityResult: cfg result has forwarding number ", stringExtra3);
        saveVoiceMailAndForwardingNumber(this.mVoicemailProviders.getKey(), new VoicemailProviderSettings(r2, stringExtra3, intExtra2));
    }

    private void simulatePreferenceClick(Preference preference) {
        ListAdapter rootAdapter = getPreferenceScreen().getRootAdapter();
        for (int i = 0; i < rootAdapter.getCount(); i++) {
            if (rootAdapter.getItem(i) == preference) {
                getPreferenceScreen().onItemClick(getListView(), null, i, rootAdapter.getItemId(i));
                return;
            }
        }
    }

    private boolean getBooleanCarrierConfig(String str) {
        PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
        if (carrierConfigForSubId == null) {
            carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfig();
        }
        return carrierConfigForSubId.getBoolean(str);
    }

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
        this.mCurrentDialogId = i;
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        return VoicemailDialogUtil.getDialog(this, i);
    }

    @Override // com.android.phone.EditPhoneNumberPreference.OnDialogClosedListener
    public void onDialogClosed(com.android.phone.EditPhoneNumberPreference editPhoneNumberPreference, int i) {
        log("onDialogClosed: Button clicked is " + i);
        if (i != -2 && editPhoneNumberPreference == this.mSubMenuVoicemailSettings) {
            saveVoiceMailAndForwardingNumber(this.mVoicemailProviders.getKey(), new VoicemailProviderSettings(this.mSubMenuVoicemailSettings.getPhoneNumber(), VoicemailProviderSettings.NO_FORWARDING));
        }
    }

    private void showDialogIfForeground(int i) {
        if (this.mForeground) {
            showDialog(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissDialogSafely(int i) {
        try {
            dismissDialog(i);
        } catch (IllegalArgumentException unused) {
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        log("onClick: button clicked is " + i);
        dialogInterface.dismiss();
        if (i != -2) {
            if (i == -1) {
                if (this.mCurrentDialogId == 502) {
                    setVoicemailNumberWithCarrier();
                    return;
                } else {
                    finish();
                    return;
                }
            }
        } else if (this.mCurrentDialogId == 502) {
            switchToPreviousVoicemailProvider();
        }
        if ("com.android.phone.CallFeaturesSetting.ADD_VOICEMAIL".equals(getIntent() != null ? getIntent().getAction() : null)) {
            finish();
        }
    }

    private void saveVoiceMailAndForwardingNumber(String str, VoicemailProviderSettings voicemailProviderSettings) {
        CallSettingUtils.sensitiveLog(LOG_TAG, "saveVoiceMailAndForwardingNumber: ", voicemailProviderSettings.toString());
        String voicemailNumber = voicemailProviderSettings.getVoicemailNumber();
        this.mNewVMNumber = voicemailNumber;
        if (voicemailNumber == null) {
            voicemailNumber = "";
        }
        this.mNewVMNumber = voicemailNumber;
        this.mNewFwdSettings = voicemailProviderSettings.getForwardingSettings();
        if (this.mNewVMNumber.equals(this.mOldVmNumber) && this.mNewFwdSettings == VoicemailProviderSettings.NO_FORWARDING) {
            showDialogIfForeground(400);
            return;
        }
        VoicemailProviderSettingsUtil.save(this, str, voicemailProviderSettings);
        this.mVMChangeCompletedSuccessfully = false;
        this.mFwdChangesRequireRollback = false;
        this.mVMOrFwdSetError = 0;
        if (this.mNewFwdSettings == VoicemailProviderSettings.NO_FORWARDING || str.equals(this.mPreviousVMProviderKey)) {
            log("Set voicemail number. No changes to forwarding number.");
            setVoicemailNumberWithCarrier();
            return;
        }
        log("Reading current forwarding settings.");
        this.mForwardingReadResults = new CallForwardInfo[VoicemailProviderSettings.FORWARDING_SETTINGS_REASONS.length];
        for (int i = 0; i < this.mForwardingReadResults.length; i++) {
            this.mPhone.getCallForwardingOption(VoicemailProviderSettings.FORWARDING_SETTINGS_REASONS[i], 1, this.mGetOptionComplete.obtainMessage(502, i, 0));
        }
        showDialogIfForeground(602);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleForwardingSettingsReadResult(AsyncResult asyncResult, int i) {
        String str = LOG_TAG;
        Log.d(str, "handleForwardingSettingsReadResult: " + i);
        Throwable th = asyncResult.exception;
        if (th != null) {
            Log.d(str, "FwdRead: ar.exception=" + th.getMessage());
        } else {
            th = null;
        }
        Object obj = asyncResult.userObj;
        if (obj instanceof Throwable) {
            th = (Throwable) obj;
            Log.d(str, "FwdRead: userObj=" + th.getMessage());
        }
        CallForwardInfo[] callForwardInfoArr = this.mForwardingReadResults;
        if (callForwardInfoArr == null) {
            Log.d(str, "Ignoring fwd reading result: " + i);
            return;
        }
        if (th != null) {
            Log.d(str, "Error discovered for fwd read : " + i);
            this.mForwardingReadResults = null;
            dismissDialogSafely(602);
            showDialogIfForeground(502);
            return;
        }
        callForwardInfoArr[i] = CallForwardInfoUtil.getCallForwardInfo((CallForwardInfo[]) asyncResult.result, VoicemailProviderSettings.FORWARDING_SETTINGS_REASONS[i]);
        int i2 = 0;
        while (true) {
            CallForwardInfo[] callForwardInfoArr2 = this.mForwardingReadResults;
            if (i2 < callForwardInfoArr2.length) {
                if (callForwardInfoArr2[i2] == null) {
                    return;
                } else {
                    i2++;
                }
            } else {
                Log.d(LOG_TAG, "Done receiving fwd info");
                dismissDialogSafely(602);
                if (this.mPreviousVMProviderKey.equals("")) {
                    VoicemailProviderSettingsUtil.save(this.mPhone.getContext(), "", new VoicemailProviderSettings(this.mOldVmNumber, this.mForwardingReadResults));
                }
                saveVoiceMailAndForwardingNumberStage2();
                return;
            }
        }
    }

    private void resetForwardingChangeState() {
        this.mForwardingChangeResults = new HashMap();
        this.mExpectedChangeResultReasons = new HashSet();
    }

    private void saveVoiceMailAndForwardingNumberStage2() {
        this.mForwardingChangeResults = null;
        this.mVoicemailChangeResult = null;
        resetForwardingChangeState();
        int i = 0;
        while (true) {
            CallForwardInfo[] callForwardInfoArr = this.mNewFwdSettings;
            if (i < callForwardInfoArr.length) {
                CallForwardInfo callForwardInfo = callForwardInfoArr[i];
                if (CallForwardInfoUtil.isUpdateRequired(CallForwardInfoUtil.infoForReason(this.mForwardingReadResults, callForwardInfo.reason), callForwardInfo)) {
                    log("Setting fwd #: " + i + ": " + callForwardInfo.toString());
                    this.mExpectedChangeResultReasons.add(Integer.valueOf(i));
                    CallForwardInfoUtil.setCallForwardingOption(this.mPhone, callForwardInfo, this.mSetOptionComplete.obtainMessage(501, callForwardInfo.reason, 0));
                }
                i++;
            } else {
                showDialogIfForeground(601);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVoicemailNumberWithCarrier() {
        CallSettingUtils.sensitiveLog(LOG_TAG, "save voicemail #: ", this.mNewVMNumber);
        this.mVoicemailChangeResult = null;
        Phone phone = this.mPhone;
        phone.setVoiceMailNumber(phone.getVoiceMailAlphaTag().toString(), this.mNewVMNumber, Message.obtain(this.mSetOptionComplete, ResponseObject.RELAY_FAILED_CODE));
    }

    private void switchToPreviousVoicemailProvider() {
        log("switchToPreviousVoicemailProvider " + this.mPreviousVMProviderKey);
        if (this.mPreviousVMProviderKey == null) {
            return;
        }
        if (this.mVMChangeCompletedSuccessfully || this.mFwdChangesRequireRollback) {
            showDialogIfForeground(603);
            VoicemailProviderSettings voicemailProviderSettingsLoad = VoicemailProviderSettingsUtil.load(this, this.mPreviousVMProviderKey);
            if (voicemailProviderSettingsLoad == null) {
                Log.e(LOG_TAG, "VoicemailProviderSettings for the key \"" + this.mPreviousVMProviderKey + "\" is null but should be loaded.");
                return;
            }
            if (this.mVMChangeCompletedSuccessfully) {
                this.mNewVMNumber = voicemailProviderSettingsLoad.getVoicemailNumber();
                Log.i(LOG_TAG, "VM change is already completed successfully.Have to revert VM back to " + this.mNewVMNumber + " again.");
                Phone phone = this.mPhone;
                phone.setVoiceMailNumber(phone.getVoiceMailAlphaTag().toString(), this.mNewVMNumber, Message.obtain(this.mRevertOptionComplete, ResponseObject.RELAY_FAILED_CODE));
            }
            if (this.mFwdChangesRequireRollback) {
                Log.i(LOG_TAG, "Requested to rollback forwarding changes.");
                CallForwardInfo[] forwardingSettings = voicemailProviderSettingsLoad.getForwardingSettings();
                if (forwardingSettings != null) {
                    Map map = this.mForwardingChangeResults;
                    resetForwardingChangeState();
                    for (int i = 0; i < forwardingSettings.length; i++) {
                        CallForwardInfo callForwardInfo = forwardingSettings[i];
                        log("Reverting fwd #: " + i + ": " + callForwardInfo.toString());
                        AsyncResult asyncResult = (AsyncResult) map.get(Integer.valueOf(callForwardInfo.reason));
                        if (asyncResult != null && asyncResult.exception == null) {
                            this.mExpectedChangeResultReasons.add(Integer.valueOf(callForwardInfo.reason));
                            CallForwardInfoUtil.setCallForwardingOption(this.mPhone, callForwardInfo, this.mRevertOptionComplete.obtainMessage(501, i, 0));
                        }
                    }
                    return;
                }
                return;
            }
            return;
        }
        log("No need to revert");
        onRevertDone();
    }

    private void updateVMPreferenceWidgets(String str) {
        VoicemailProviderListPreference.VoicemailProvider voicemailProvider = this.mVoicemailProviders.getVoicemailProvider(str);
        if (voicemailProvider == null) {
            log("updateVMPreferenceWidget: key: " + str + " -> null.");
            this.mVoicemailProviders.setSummary(getString(R.string.sum_voicemail_choose_provider));
            this.mVoicemailSettings.setEnabled(false);
            this.mVoicemailSettings.setIntent(null);
            return;
        }
        log("updateVMPreferenceWidget: key: " + str + " -> " + voicemailProvider.toString());
        this.mVoicemailProviders.setSummary(voicemailProvider.name);
        this.mVoicemailSettings.setEnabled(true);
        this.mVoicemailSettings.setIntent(voicemailProvider.intent);
    }

    private void updateVoiceNumberField() {
        String voiceMailNumber = this.mPhone.getVoiceMailNumber();
        this.mOldVmNumber = voiceMailNumber;
        CallSettingUtils.sensitiveLog(LOG_TAG, "updateVoiceNumberField(), mOldVmNumber = ", voiceMailNumber);
        boolean zIsEmpty = TextUtils.isEmpty(this.mOldVmNumber);
        com.android.phone.EditPhoneNumberPreference editPhoneNumberPreference = this.mSubMenuVoicemailSettings;
        if (zIsEmpty) {
            editPhoneNumberPreference.setPhoneNumber("");
            this.mSubMenuVoicemailSettings.setSummary(getString(R.string.voicemail_number_not_set));
        } else {
            editPhoneNumberPreference.setPhoneNumber(this.mOldVmNumber);
            this.mSubMenuVoicemailSettings.setSummary(BidiFormatter.getInstance().unicodeWrap(this.mOldVmNumber, TextDirectionHeuristics.LTR));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetVmOrFwdMessage() {
        log("handleSetVMMessage: set VM request complete");
        if (!isFwdChangeSuccess()) {
            handleVmOrFwdSetError(501);
        } else if (!isVmChangeSuccess()) {
            handleVmOrFwdSetError(ResponseObject.RELAY_FAILED_CODE);
        } else {
            handleVmAndFwdSetSuccess(600);
        }
    }

    private void handleVmOrFwdSetError(int i) {
        if (this.mChangingVMorFwdDueToProviderChange) {
            this.mVMOrFwdSetError = i;
            this.mChangingVMorFwdDueToProviderChange = false;
            switchToPreviousVoicemailProvider();
        } else {
            this.mChangingVMorFwdDueToProviderChange = false;
            showDialogIfForeground(i);
            updateVoiceNumberField();
        }
    }

    private void handleVmAndFwdSetSuccess(int i) {
        log("handleVmAndFwdSetSuccess: key is " + this.mVoicemailProviders.getKey());
        this.mPreviousVMProviderKey = this.mVoicemailProviders.getKey();
        this.mChangingVMorFwdDueToProviderChange = false;
        showDialogIfForeground(i);
        updateVoiceNumberField();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRevertDone() {
        log("onRevertDone: Changing provider key back to " + this.mPreviousVMProviderKey);
        updateVMPreferenceWidgets(this.mPreviousVMProviderKey);
        updateVoiceNumberField();
        int i = this.mVMOrFwdSetError;
        if (i != 0) {
            showDialogIfForeground(i);
            this.mVMOrFwdSetError = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isForwardingCompleted() {
        if (this.mForwardingChangeResults == null) {
            return true;
        }
        Iterator it = this.mExpectedChangeResultReasons.iterator();
        while (it.hasNext()) {
            if (this.mForwardingChangeResults.get((Integer) it.next()) == null) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFwdChangeSuccess() {
        Map map = this.mForwardingChangeResults;
        if (map == null) {
            return true;
        }
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            Throwable th = ((AsyncResult) it.next()).exception;
            if (th != null) {
                String message = th.getMessage();
                if (message == null) {
                    message = "";
                }
                Log.w(LOG_TAG, "Failed to change forwarding setting. Reason: ".concat(message));
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isVmChangeSuccess() {
        Throwable th = this.mVoicemailChangeResult.exception;
        if (th == null) {
            return true;
        }
        String message = th.getMessage();
        if (message == null) {
            message = "";
        }
        Log.w(LOG_TAG, "Failed to change voicemail. Reason: ".concat(message));
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    @Override // android.preference.PreferenceActivity, android.app.ListActivity, android.app.Activity
    protected void onDestroy() {
        PhoneGlobals.getInstance().removeSubInfoUpdateListener(this);
        super.onDestroy();
    }

    @Override // com.android.phone.PhoneGlobals.SubInfoUpdateListener
    public void handleSubInfoUpdate() {
        log("handleSubInfoUpdate");
        finish();
    }
}
