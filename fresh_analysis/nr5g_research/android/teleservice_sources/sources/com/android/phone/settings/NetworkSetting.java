package com.android.phone.settings;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.AccessNetworkConstants;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.satellite.SatelliteManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.MiuiNetworkControllerImpl;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.internal.util.ArrayUtils;
import com.android.phone.FiveGManagerBase;
import com.android.phone.IMiuiNetworkQueryService;
import com.android.phone.IMiuiNetworkQueryServiceCallback;
import com.android.phone.MiuiNetworkQueryService;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneProxy;
import com.android.phone.R;
import com.android.phone.SprdCellInfoUtil;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.PhoneDebug;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;
import miuix.preference.R$layout;
import miuix.preference.RadioButtonPreference;
import miuix.preference.RadioButtonPreferenceCategory;
import miuix.reflect.Reflects$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class NetworkSetting extends BaseActivity {
    private static final boolean DBG = PhoneDebug.VDBG;
    private static final String LOG_TAG = "NetworkSetting";
    private ProgressDialog mDialog;
    private NetworkSettingFragment mNetworkSettingFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        NetworkSettingFragment networkSettingFragment = (NetworkSettingFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mNetworkSettingFragment = networkSettingFragment;
        if (bundle == null && networkSettingFragment == null) {
            NetworkSettingFragment networkSettingFragment2 = new NetworkSettingFragment();
            this.mNetworkSettingFragment = networkSettingFragment2;
            loadFragment(networkSettingFragment2);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        ProgressDialog progressDialog = this.mDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mNetworkSettingFragment.getSelectCheckBox().setChecked(false);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        ProgressDialog progressDialog = this.mDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mDialog = null;
        }
        this.mNetworkSettingFragment.dismissDialog();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        Log.i(LOG_TAG, "onNewIntent()");
        setIntent(intent);
        this.mNetworkSettingFragment.clearList();
        this.mNetworkSettingFragment.internalResolveIntent();
        if (this.mNetworkSettingFragment.mNetworkQueryService != null) {
            this.mNetworkSettingFragment.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.settings.NetworkSetting.1
                @Override // java.lang.Runnable
                public void run() {
                    NetworkSetting.this.mNetworkSettingFragment.onNetworkQueryServiceConnected();
                }
            }, 1000L);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed()");
        if (!this.mNetworkSettingFragment.isNetworkListChecked()) {
            this.mNetworkSettingFragment.showDialogSafely(900);
        } else {
            super.onBackPressed();
        }
    }

    @Override // com.android.phone.settings.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i == 100) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            this.mDialog = progressDialog;
            progressDialog.setMessage(this.mNetworkSettingFragment.mNetworkSelectMsg);
            this.mDialog.setCancelable(false);
            this.mDialog.setIndeterminate(true);
            return this.mDialog;
        }
        if (i == 300) {
            ProgressDialog progressDialog2 = new ProgressDialog(this);
            this.mDialog = progressDialog2;
            progressDialog2.setMessage(getResources().getString(R.string.register_automatically));
            this.mDialog.setCancelable(false);
            this.mDialog.setIndeterminate(true);
            return this.mDialog;
        }
        if (i == 400) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.network_settings_warn_message).setTitle(R.string.network_settings_auto_select_title).setIconAttribute(android.R.attr.alertDialogIcon).setNegativeButton(getResources().getString(android.R.string.cancel), (DialogInterface.OnClickListener) null).setPositiveButton(getResources().getString(R.string.network_settings_next), this.mNetworkSettingFragment);
            AlertDialog.Builder titleIfNeeded = setTitleIfNeeded(builder, this);
            this.mNetworkSettingFragment.mTurnOffAutoSelectWarningDialog = titleIfNeeded.create();
            return this.mNetworkSettingFragment.mTurnOffAutoSelectWarningDialog;
        }
        if (i == 500) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setMessage(R.string.network_settings_confirm_message).setTitle(R.string.network_settings_auto_select_title).setIconAttribute(android.R.attr.alertDialogIcon).setNegativeButton(getResources().getString(android.R.string.cancel), (DialogInterface.OnClickListener) null).setPositiveButton(getResources().getString(android.R.string.ok), this.mNetworkSettingFragment);
            AlertDialog.Builder titleIfNeeded2 = setTitleIfNeeded(builder2, this);
            this.mNetworkSettingFragment.mTurnOffAutoSelectConfirmDialog = titleIfNeeded2.create();
            return this.mNetworkSettingFragment.mTurnOffAutoSelectConfirmDialog;
        }
        if (i == 600) {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            builder3.setMessage(R.string.network_settings_query_failed_message).setTitle(R.string.network_settings_auto_select_title).setIconAttribute(android.R.attr.alertDialogIcon).setNegativeButton(getResources().getString(android.R.string.cancel), this.mNetworkSettingFragment).setPositiveButton(getResources().getString(R.string.retry_btn), this.mNetworkSettingFragment);
            AlertDialog.Builder titleIfNeeded3 = setTitleIfNeeded(builder3, this);
            this.mNetworkSettingFragment.mNetworkQueryFailedDialog = titleIfNeeded3.create();
            return this.mNetworkSettingFragment.mNetworkQueryFailedDialog;
        }
        if (i == 700) {
            AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
            builder4.setTitle(getResources().getString(R.string.network_settings_information)).setCancelable(false).setMessage(getResources().getString(R.string.network_settings_duringcall)).setNegativeButton(R.string.ok, this.mNetworkSettingFragment.mAlertDialogListener);
            AlertDialog alertDialogCreate = builder4.create();
            alertDialogCreate.setCanceledOnTouchOutside(false);
            return alertDialogCreate;
        }
        if (i == 800) {
            AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
            builder5.setTitle(getResources().getString(R.string.network_settings_information)).setCancelable(false).setMessage(getResources().getString(R.string.network_settings_data_connected_change)).setNegativeButton(android.R.string.cancel, this.mNetworkSettingFragment.mAlertDialogListener).setPositiveButton(R.string.ok, this.mNetworkSettingFragment.mAlertDialogListener);
            AlertDialog alertDialogCreate2 = builder5.create();
            alertDialogCreate2.setCanceledOnTouchOutside(false);
            return alertDialogCreate2;
        }
        if (i == 900) {
            AlertDialog.Builder builder6 = new AlertDialog.Builder(this);
            builder6.setTitle(getResources().getString(R.string.networks_title)).setCancelable(true).setMessage(getResources().getString(R.string.no_checked_network)).setNegativeButton(R.string.alert_dialog_no, this.mNetworkSettingFragment.mAlertDialogListener).setPositiveButton(R.string.alert_dialog_yes, (DialogInterface.OnClickListener) null);
            AlertDialog alertDialogCreate3 = builder6.create();
            alertDialogCreate3.setCanceledOnTouchOutside(false);
            return alertDialogCreate3;
        }
        ProgressDialog progressDialog3 = new ProgressDialog(this);
        this.mDialog = progressDialog3;
        progressDialog3.setMessage(getResources().getString(R.string.load_networks_progress));
        this.mDialog.setCanceledOnTouchOutside(false);
        this.mDialog.setOnCancelListener(this.mNetworkSettingFragment);
        return this.mDialog;
    }

    private AlertDialog.Builder setTitleIfNeeded(AlertDialog.Builder builder, Context context) {
        if (!getResources().getConfiguration().locale.getLanguage().equals("ko")) {
            return builder;
        }
        TextView textView = new TextView(context);
        textView.setText(R.string.network_settings_auto_select_title);
        textView.setTextSize(18.0f);
        textView.setTypeface(Typeface.defaultFromStyle(1));
        textView.setSingleLine(false);
        builder.setCustomTitle(textView);
        return builder;
    }

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog) {
        if (i == 100 || i == 200 || i == 300) {
            this.mNetworkSettingFragment.getPreferenceScreen().setEnabled(false);
        }
    }

    public class NetworkSettingFragment extends BaseActivity.BaseFragment implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener {
        private CheckBoxPreference mAutoSelect;
        private RadioButtonPreferenceCategory mAvailableNetworkList;
        private Handler mBackgroundHandler;
        private HandlerThread mHandlerThread;
        private PreferenceGroup mNetworkList;
        private HashMap mNetworkMap;
        private AlertDialog mNetworkQueryFailedDialog;
        String mNetworkSelectMsg;
        ServiceState mOldSS;
        Phone mPhone;
        private Preference mSearchButton;
        private int mSlotId;
        private Toast mToast;
        private boolean mTryAutoSelectNetwork;
        private AlertDialog mTurnOffAutoSelectConfirmDialog;
        private AlertDialog mTurnOffAutoSelectWarningDialog;
        protected boolean mIsForeground = false;
        private boolean mNeedDataReEnable = false;
        private final Handler mHandler = new Handler() { // from class: com.android.phone.settings.NetworkSetting.NetworkSettingFragment.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 100) {
                    NetworkSettingFragment.this.networksListLoaded((List) message.obj, message.arg1);
                    return;
                }
                if (i == 200) {
                    if (NetworkSetting.DBG) {
                        NetworkSettingFragment.this.log("hideProgressPanel");
                    }
                    MiuiDataControllerImpl.Processor.setManualNetworkSelected(NetworkSettingFragment.this.mPhone, false);
                    if (NetworkSettingFragment.this.getActivity() != null) {
                        NetworkSettingFragment.this.getActivity().removeDialog(100);
                    }
                    NetworkSettingFragment.this.getPreferenceScreen().setEnabled(true);
                    boolean z = ModemProvider.IS_SPRD;
                    Object obj = message.obj;
                    if (z) {
                        boolean zEquals = Boolean.TRUE.equals(obj);
                        NetworkSettingFragment networkSettingFragment = NetworkSettingFragment.this;
                        if (zEquals) {
                            networkSettingFragment.displayNetworkSelectionSucceeded();
                        } else {
                            networkSettingFragment.displayNetworkSelectionFailedForSprd();
                        }
                    } else {
                        AsyncResult asyncResult = (AsyncResult) obj;
                        if (asyncResult == null) {
                            if (NetworkSetting.DBG) {
                                NetworkSettingFragment.this.log("manual network selection: AsyncResult is null");
                            }
                            NetworkSettingFragment.this.displayNetworkSelectionFailed(null);
                        } else if (asyncResult.exception != null) {
                            if (NetworkSetting.DBG) {
                                NetworkSettingFragment.this.log("manual network selection: failed!");
                            }
                            NetworkSettingFragment.this.displayNetworkSelectionFailed(asyncResult.exception);
                            if (TelephonyManager.isCustForEsVodafone()) {
                                NetworkSettingFragment.this.log("manual network selection failed, switch to automatic network selection");
                                NetworkSettingFragment.this.mPhone.setNetworkSelectionModeAutomatic((Message) null);
                            }
                        } else {
                            if (NetworkSetting.DBG) {
                                NetworkSettingFragment.this.log("manual network selection: succeeded!");
                            }
                            NetworkSettingFragment.this.displayNetworkSelectionSucceeded();
                        }
                    }
                    sendMessageDelayed(obtainMessage(800), 2000L);
                    return;
                }
                if (i == 300) {
                    if (NetworkSetting.DBG) {
                        NetworkSettingFragment.this.log("hideProgressPanel");
                    }
                    try {
                        if (NetworkSettingFragment.this.getActivity() != null) {
                            NetworkSettingFragment.this.getActivity().dismissDialog(MiRilHook.HOOK_ID_START_URC_BY_AT);
                        }
                    } catch (IllegalArgumentException e) {
                        Log.w(NetworkSetting.LOG_TAG, "[NetworksList] Fail to dismiss auto select dialog", e);
                    }
                    NetworkSettingFragment.this.getPreferenceScreen().setEnabled(true);
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    if (asyncResult2.exception != null) {
                        if (NetworkSetting.DBG) {
                            NetworkSettingFragment.this.log("automatic network selection: failed!");
                        }
                        NetworkSettingFragment.this.displayNetworkSelectionFailed(asyncResult2.exception);
                        return;
                    } else {
                        if (NetworkSetting.DBG) {
                            NetworkSettingFragment.this.log("automatic network selection: succeeded!");
                        }
                        NetworkSettingFragment.this.displayNetworkSelectionSucceeded();
                        NetworkSettingFragment.this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.settings.NetworkSetting.NetworkSettingFragment.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                NetworkSettingFragment.this.mAutoSelect.setSummaryOn(NetworkSettingFragment.this.getAutoSelectionSummary(true));
                            }
                        }, 3000L);
                        return;
                    }
                }
                if (i == 400) {
                    NetworkSettingFragment.this.loadNetworksList();
                    return;
                }
                if (i == 500) {
                    NetworkSettingFragment.this.doDataReEnable();
                    return;
                }
                if (i == 600) {
                    NetworkSettingFragment.this.mTryAutoSelectNetwork = true;
                    NetworkSettingFragment.this.selectNetworkAutomatic();
                    return;
                }
                if (i == 700) {
                    NetworkSettingFragment.this.startNetworkQuery();
                    return;
                }
                if (i != 800) {
                    if (i != 900) {
                        return;
                    }
                    NetworkSettingFragment.this.onNetworksListLoadResult((String[]) message.obj, message.arg1);
                } else if (MiuiPhoneUtils.isPamRequired(NetworkSettingFragment.this.mPhone.getPhoneId())) {
                    NetworkSettingFragment.this.mPhone.setNetworkSelectionModeAutomatic((Message) null);
                }
            }
        };
        private IMiuiNetworkQueryService mNetworkQueryService = null;
        private final ServiceConnection mNetworkQueryServiceConnection = new ServiceConnection() { // from class: com.android.phone.settings.NetworkSetting.NetworkSettingFragment.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (NetworkSetting.DBG) {
                    NetworkSettingFragment.this.log("connection created, binding local service.");
                }
                if (iBinder instanceof MiuiNetworkQueryService.LocalBinder) {
                    NetworkSettingFragment.this.mNetworkQueryService = ((MiuiNetworkQueryService.LocalBinder) iBinder).getService();
                } else {
                    NetworkSettingFragment.this.mNetworkQueryService = IMiuiNetworkQueryService.Stub.asInterface(iBinder);
                }
                NetworkSettingFragment.this.onNetworkQueryServiceConnected();
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                if (NetworkSetting.DBG) {
                    NetworkSettingFragment.this.log("connection disconnected, cleaning local binding.");
                }
                NetworkSettingFragment.this.mNetworkQueryService = null;
            }
        };
        private final IMiuiNetworkQueryServiceCallback mCallback = new IMiuiNetworkQueryServiceCallback.Stub() { // from class: com.android.phone.settings.NetworkSetting.NetworkSettingFragment.3
            @Override // com.android.phone.IMiuiNetworkQueryServiceCallback
            public void onQueryComplete(List list, int i) {
                if (NetworkSetting.DBG) {
                    NetworkSettingFragment.this.log("notifying message loop of query completion.");
                }
                NetworkSettingFragment.this.mHandler.obtainMessage(100, i, 0, list).sendToTarget();
            }

            @Override // com.android.phone.IMiuiNetworkQueryServiceCallback
            public void onIncrementalManualScanResult(String[] strArr, int i) {
                if (NetworkSetting.DBG) {
                    NetworkSettingFragment.this.log("onIncrementalManualScanResult result: " + i);
                }
                NetworkSettingFragment.this.mHandler.obtainMessage(900, i, 0, strArr).sendToTarget();
            }
        };
        private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.NetworkSetting.NetworkSettingFragment.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                int intExtra;
                if (NetworkSetting.DBG) {
                    NetworkSettingFragment.this.log("receive action = " + intent.getAction());
                }
                if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                    if (NetworkSetting.DBG) {
                        NetworkSettingFragment.this.log("receive ACTION_SHUTDOWN");
                    }
                    NetworkSettingFragment.this.doDataReEnable();
                    NetworkSettingFragment.this.getActivity().finish();
                }
                if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction()) && intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false)) {
                    NetworkSettingFragment.this.getActivity().finish();
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                    intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_SLOT_ID);
                } else {
                    intExtra = intent.getIntExtra("slot", SubscriptionManager.INVALID_SLOT_ID);
                }
                if (intExtra == SubscriptionManager.INVALID_SLOT_ID) {
                    intExtra = SubscriptionManager.getDefault().getSlotIdForSubscription(intent.getIntExtra("subscription", SubscriptionManager.INVALID_SUBSCRIPTION_ID));
                }
                if (intExtra != NetworkSettingFragment.this.mSlotId) {
                    return;
                }
                if ("android.intent.action.SERVICE_STATE".equals(intent.getAction())) {
                    ServiceState serviceStateNewFromBundle = ServiceState.newFromBundle(intent.getExtras());
                    if (NetworkSettingFragment.this.mAutoSelect.isChecked()) {
                        if (serviceStateNewFromBundle.getState() == 0) {
                            NetworkSettingFragment.this.mAutoSelect.setSummaryOn(NetworkSettingFragment.this.getAutoSelectionSummary(true));
                            if (NetworkSettingFragment.this.mTryAutoSelectNetwork) {
                                NetworkSettingFragment.this.mTryAutoSelectNetwork = false;
                                NetworkSettingFragment.this.displayNetworkSelectionSucceeded();
                            }
                        }
                    } else {
                        ServiceState serviceState = NetworkSettingFragment.this.mOldSS;
                        if (serviceState != null && serviceState.getIsManualSelection() && !serviceStateNewFromBundle.getIsManualSelection() && (NetworkSettingFragment.this.mTurnOffAutoSelectWarningDialog != null || NetworkSettingFragment.this.mTurnOffAutoSelectConfirmDialog != null)) {
                            NetworkSettingFragment.this.refreshView();
                        }
                    }
                    NetworkSettingFragment.this.mOldSS = serviceStateNewFromBundle;
                    return;
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction()) && "ABSENT".equals(intent.getStringExtra("ss")) && NetworkSettingFragment.this.getActivity() != null) {
                    NetworkSettingFragment.this.getActivity().finish();
                }
            }
        };
        private DialogInterface.OnClickListener mAlertDialogListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.NetworkSetting.NetworkSettingFragment.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -2) {
                    if (NetworkSettingFragment.this.getActivity() != null) {
                        NetworkSettingFragment.this.getActivity().finish();
                    }
                } else {
                    if (i != -1) {
                        return;
                    }
                    android.telephony.TelephonyManager.from(NetworkSettingFragment.this.getContext().getApplicationContext()).setDataEnabled(false);
                    NetworkSettingFragment.this.mNeedDataReEnable = true;
                    NetworkSettingFragment.this.getPreferenceScreen().setEnabled(false);
                    NetworkSettingFragment.this.mHandler.sendMessageDelayed(NetworkSettingFragment.this.mHandler.obtainMessage(400), 1000L);
                }
            }
        };

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
        public boolean onPreferenceTreeClick(Preference preference) {
            OperatorInfo operatorInfo;
            final OperatorInfo operatorInfo2;
            if (preference == this.mSearchButton) {
                loadNetworksList();
            } else {
                CheckBoxPreference checkBoxPreference = this.mAutoSelect;
                if (preference == checkBoxPreference) {
                    if (!checkBoxPreference.isChecked() && getActivity() != null) {
                        this.mAutoSelect.setChecked(true);
                        getActivity().showDialog(400);
                    } else {
                        this.mHandler.sendEmptyMessage(600);
                    }
                } else {
                    HashMap map = this.mNetworkMap;
                    if (map == null || map.size() == 0) {
                        log("mNetworkMap had been erased");
                        return true;
                    }
                    String string = preference.getTitle().toString();
                    if (NetworkSetting.DBG) {
                        log("selected network: " + string);
                    }
                    if (ModemProvider.IS_SPRD) {
                        if (this.mBackgroundHandler == null || (operatorInfo2 = (OperatorInfo) this.mNetworkMap.get(preference)) == null) {
                            return true;
                        }
                        this.mBackgroundHandler.post(new Runnable() { // from class: com.android.phone.settings.NetworkSetting$NetworkSettingFragment$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onPreferenceTreeClick$0(operatorInfo2);
                            }
                        });
                    } else {
                        if (this.mPhone == null || (operatorInfo = (OperatorInfo) this.mNetworkMap.get(preference)) == null) {
                            return true;
                        }
                        PhoneProxy.selectNetworkManually(operatorInfo, this.mHandler.obtainMessage(200), this.mPhone);
                    }
                    MiuiDataControllerImpl.Processor.setManualNetworkSelected(this.mPhone, true);
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("button_auto_select_key" + this.mSlotId, false).apply();
                    displayNetworkSeletionInProgress(string);
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPreferenceTreeClick$0(OperatorInfo operatorInfo) {
            android.telephony.TelephonyManager telephonyManager;
            android.telephony.TelephonyManager telephonyManagerCreateForSubscriptionId;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(200, Boolean.valueOf((getContext() == null || this.mPhone == null || (telephonyManager = (android.telephony.TelephonyManager) getContext().getSystemService(android.telephony.TelephonyManager.class)) == null || (telephonyManagerCreateForSubscriptionId = telephonyManager.createForSubscriptionId(this.mPhone.getSubId())) == null) ? false : telephonyManagerCreateForSubscriptionId.setNetworkSelectionModeManual(SprdCellInfoUtil.getSprdOperatorInfo(operatorInfo), true))));
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            try {
                this.mNetworkQueryService.stopNetworkQuery(this.mCallback, this.mSlotId);
                if (!ModemProvider.IS_SPRD) {
                    doDataReEnable();
                }
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } catch (RemoteException e) {
                Reflects$$ExternalSyntheticBUOutline0.m(e);
            }
        }

        private void notifyNetworkSelection(CharSequence charSequence) {
            Toast toast = this.mToast;
            if (toast != null) {
                toast.cancel();
            }
            Toast toastMakeText = Toast.makeText(requireContext(), charSequence, 1);
            this.mToast = toastMakeText;
            toastMakeText.show();
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            getActivity().getWindow().addFlags(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            if (ModemProvider.IS_SPRD) {
                HandlerThread handlerThread = new HandlerThread("SetNetworkSelectionModeManual");
                this.mHandlerThread = handlerThread;
                handlerThread.start();
                this.mBackgroundHandler = new Handler(this.mHandlerThread.getLooper());
            }
            addPreferencesFromResource(R.xml.miui_carrier_select);
            this.mNetworkList = (PreferenceGroup) getPreferenceScreen().findPreference("list_networks_key");
            this.mNetworkMap = new HashMap();
            this.mSearchButton = getPreferenceScreen().findPreference("button_srch_netwrks_key");
            this.mAutoSelect = (CheckBoxPreference) getPreferenceScreen().findPreference("button_auto_select_key");
            internalResolveIntent();
            this.mAutoSelect.setSummaryOn(getAutoSelectionSummary(true));
            this.mAutoSelect.setSummaryOff(getAutoSelectionSummary(false));
            getContext().bindService(new Intent(getContext(), (Class<?>) MiuiNetworkQueryService.class).setAction("com.android.phone.intent.action.LOCAL_BINDER"), this.mNetworkQueryServiceConnection, 1);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SERVICE_STATE");
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
            getContext().registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void internalResolveIntent() {
            Intent intent = getActivity().getIntent();
            this.mSlotId = SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.getDefault().getDefaultSlotId());
            Bundle extras = intent != null ? intent.getExtras() : null;
            if (this.mSlotId == SubscriptionManager.INVALID_SLOT_ID && extras != null) {
                this.mSlotId = Integer.parseInt(extras.getString(SubscriptionManager.SLOT_KEY, "-1"));
            }
            this.mPhone = MiuiPhoneUtils.getPhone(this.mSlotId);
            if (NetworkSetting.DBG) {
                Log.i(NetworkSetting.LOG_TAG, "mSlotId:" + this.mSlotId + ",mPhone:" + this.mPhone);
            }
            boolean zHasExtra = getActivity().getIntent().hasExtra("restore_to_automatic_mode");
            if (zHasExtra) {
                this.mHandler.sendEmptyMessage(600);
            }
            this.mAutoSelect.setChecked(getNetworkAutoSelectionMode() || zHasExtra);
        }

        protected String getAutoSelectionSummary(boolean z) {
            if (getActivity() == null) {
                return "";
            }
            Resources resources = getResources();
            if (z) {
                String networkOperatorName = getNetworkOperatorName();
                Phone phone = this.mPhone;
                ServiceState serviceState = phone != null ? phone.getServiceState() : null;
                if (TextUtils.isEmpty(networkOperatorName) || serviceState == null || serviceState.getState() != 0) {
                    return resources.getString(R.string.sum_select_automatically);
                }
                return resources.getString(R.string.network_settings_automatically_option, networkOperatorName);
            }
            return resources.getString(R.string.sum_select_automatically);
        }

        private String getNetworkOperatorName() {
            return TelephonyManager.getDefault().getNetworkOperatorNameForSlot(this.mSlotId);
        }

        private String getSimOperatorName() {
            return TelephonyManager.getDefault().getSimOperatorNameForSlot(this.mSlotId);
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            this.mIsForeground = true;
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            this.mIsForeground = false;
        }

        public void dismissDialog() {
            AlertDialog alertDialog = this.mTurnOffAutoSelectWarningDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.mTurnOffAutoSelectWarningDialog = null;
            }
            AlertDialog alertDialog2 = this.mTurnOffAutoSelectConfirmDialog;
            if (alertDialog2 != null) {
                alertDialog2.dismiss();
                this.mTurnOffAutoSelectConfirmDialog = null;
            }
            AlertDialog alertDialog3 = this.mNetworkQueryFailedDialog;
            if (alertDialog3 != null) {
                alertDialog3.dismiss();
                this.mNetworkQueryFailedDialog = null;
            }
        }

        boolean isNetworkListChecked() {
            Log.d(NetworkSetting.LOG_TAG, "isNetworkListChecked: " + this.mAvailableNetworkList);
            RadioButtonPreferenceCategory radioButtonPreferenceCategory = this.mAvailableNetworkList;
            if (radioButtonPreferenceCategory == null) {
                return true;
            }
            int preferenceCount = radioButtonPreferenceCategory.getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                RadioButtonPreference radioButtonPreference = (RadioButtonPreference) this.mAvailableNetworkList.getPreference(i);
                Log.d(NetworkSetting.LOG_TAG, "isChecked():" + radioButtonPreference.isChecked());
                if (radioButtonPreference.isChecked()) {
                    return true;
                }
            }
            return false;
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            try {
                this.mNetworkQueryService.stopNetworkQuery(this.mCallback, this.mSlotId);
            } catch (RemoteException unused) {
                log("stopNetworkQuery RemoteException");
            } catch (IllegalArgumentException unused2) {
                log("stopNetworkQuery IllegalArgumentException");
            }
            doDataReEnable();
            getContext().unbindService(this.mNetworkQueryServiceConnection);
            getContext().unregisterReceiver(this.mBroadcastReceiver);
            this.mHandler.removeCallbacksAndMessages(null);
            MiuiDataControllerImpl.Processor.setManualNetworkSelected(this.mPhone, false);
            if (this.mHandlerThread != null) {
                Handler handler = this.mBackgroundHandler;
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                this.mHandlerThread.quitSafely();
                this.mBackgroundHandler = null;
            }
            super.onDestroy();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void refreshView() {
            if (NetworkSetting.DBG) {
                log("refreshView");
            }
            clearList();
            this.mAutoSelect.setSummaryOn(getAutoSelectionSummary(true));
            this.mAutoSelect.setChecked(true);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("button_auto_select_key" + this.mSlotId, true).apply();
        }

        void onNetworkQueryServiceConnected() {
            if (this.mAutoSelect.isChecked()) {
                return;
            }
            checkDataConnection();
        }

        protected boolean getNetworkAutoSelectionMode() {
            Phone phone = this.mPhone;
            ServiceState serviceState = phone != null ? phone.getServiceState() : null;
            return (serviceState == null || serviceState.getIsManualSelection()) ? false : true;
        }

        private void checkDataConnection() {
            if (getActivity() == null) {
                return;
            }
            if (!MiuiPhoneUtils.isPhoneInCall()) {
                android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) getContext().getSystemService("phone");
                if (telephonyManager != null && !telephonyManager.isDataEnabled()) {
                    loadNetworksList();
                    return;
                } else {
                    getActivity().showDialog(800);
                    return;
                }
            }
            getActivity().showDialog(700);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void doDataReEnable() {
            if (this.mNeedDataReEnable) {
                android.telephony.TelephonyManager.from(getContext().getApplicationContext()).setDataEnabled(true);
                this.mNeedDataReEnable = false;
            }
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (dialogInterface == this.mTurnOffAutoSelectWarningDialog && getActivity() != null) {
                getActivity().dismissDialog(400);
                getActivity().showDialog(ResponseObject.RELAY_FAILED_CODE);
            } else if (dialogInterface == this.mTurnOffAutoSelectConfirmDialog) {
                this.mAutoSelect.setChecked(false);
                checkDataConnection();
            } else if (dialogInterface == this.mNetworkQueryFailedDialog) {
                if (i == -1) {
                    checkDataConnection();
                } else {
                    this.mHandler.sendEmptyMessage(600);
                }
            }
        }

        private void displayEmptyNetworkList(boolean z) {
            this.mNetworkList.setTitle(z ? R.string.empty_networks_list : R.string.label_available);
        }

        private void displayNetworkSeletionInProgress(String str) {
            this.mNetworkSelectMsg = getResources().getString(R.string.register_on_network, str);
            if (!this.mIsForeground || getActivity() == null) {
                return;
            }
            getActivity().showDialog(100);
        }

        protected void showDialogSafely(int i) {
            if (!this.mIsForeground || getActivity() == null || getActivity().isFinishing()) {
                return;
            }
            getActivity().showDialog(i);
        }

        private void displayNetworkQueryFailed(int i) {
            showDialogSafely(600);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearList() {
            RadioButtonPreferenceCategory radioButtonPreferenceCategory = this.mAvailableNetworkList;
            if (radioButtonPreferenceCategory != null) {
                this.mNetworkList.removePreference(radioButtonPreferenceCategory);
                this.mAvailableNetworkList = null;
            }
            this.mNetworkMap.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void displayNetworkSelectionFailed(Throwable th) {
            String string;
            if (isAdded()) {
                if (th != null && (th instanceof CommandException) && ((CommandException) th).getCommandError() == CommandException.Error.ILLEGAL_SIM_OR_ME) {
                    string = getResources().getString(R.string.not_allowed);
                } else {
                    string = getResources().getString(R.string.connect_later);
                }
                notifyNetworkSelection(string);
                RadioButtonPreferenceCategory radioButtonPreferenceCategory = this.mAvailableNetworkList;
                if (radioButtonPreferenceCategory != null) {
                    radioButtonPreferenceCategory.setCheckedPreference(null);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void displayNetworkSelectionFailedForSprd() {
            if (isAdded()) {
                notifyNetworkSelection(getResources().getString(R.string.connect_later));
                RadioButtonPreferenceCategory radioButtonPreferenceCategory = this.mAvailableNetworkList;
                if (radioButtonPreferenceCategory != null) {
                    radioButtonPreferenceCategory.setCheckedPreference(null);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void displayNetworkSelectionSucceeded() {
            if (isAdded() && !this.mTryAutoSelectNetwork) {
                notifyNetworkSelection(getResources().getString(R.string.registration_done));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void loadNetworksList() {
            if (NetworkSetting.DBG) {
                log("load networks list...");
            }
            if (!this.mPhone.getIccCard().hasIccCard()) {
                Log.w(NetworkSetting.LOG_TAG, "loadNetworksList: SIM card absent, return.");
                getPreferenceScreen().setEnabled(true);
                displayEmptyNetworkList(true);
            } else {
                if (this.mIsForeground && getActivity() != null) {
                    getActivity().showDialog(200);
                }
                this.mHandler.sendEmptyMessage(700);
                displayEmptyNetworkList(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startNetworkQuery() {
            try {
                this.mNetworkQueryService.startNetworkQuery(this.mCallback, this.mSlotId);
            } catch (RemoteException e) {
                Log.e(NetworkSetting.LOG_TAG, "startNetworkQuery error:" + e.getMessage());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onNetworksListLoadResult(String[] strArr, int i) {
            int i2;
            if (NetworkSetting.DBG) {
                log("result: " + i + " length: " + strArr.length);
            }
            ArrayList arrayList = null;
            if (i == 0) {
                if (strArr != null) {
                    if (strArr.length >= 4 && strArr.length % 4 == 0) {
                        if (NetworkSetting.DBG) {
                            log("QUERY_OK result.length: " + strArr.length);
                        }
                        arrayList = new ArrayList();
                        for (int i3 = 0; i3 < strArr.length / 4; i3++) {
                            int i4 = i3 * 4;
                            arrayList.add(new OperatorInfo(strArr[i4], strArr[i4 + 1], strArr[i4 + 2], strArr[i4 + 3]));
                        }
                    } else if (NetworkSetting.DBG) {
                        log("result.length is: " + strArr.length);
                    }
                }
                i2 = 0;
            } else {
                if (i == 1) {
                    log("FiveGNetworkScan reseult status is QUERY_PARTIAL");
                    return;
                }
                i2 = -1;
            }
            networksListLoaded(arrayList, i2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void networksListLoaded(List list, int i) {
            if (NetworkSetting.DBG) {
                log("networks list loaded");
            }
            boolean z = list != null && list.size() > 0;
            if (i != 0 || z) {
                try {
                    if (getActivity() != null) {
                        getActivity().removeDialog(200);
                    }
                } catch (RemoteException unused) {
                    if (NetworkSetting.DBG) {
                        log("stopNetworkQuery exception");
                    }
                } catch (IllegalArgumentException unused2) {
                    if (NetworkSetting.DBG) {
                        log(" DIALOG_NETWORK_LIST_LOAD dismissed already");
                    }
                }
            }
            this.mNetworkQueryService.stopNetworkQuery(this.mCallback, this.mSlotId);
            if (!ModemProvider.IS_SPRD) {
                doDataReEnable();
            }
            getPreferenceScreen().setEnabled(true);
            clearList();
            if (i != 0) {
                if (NetworkSetting.DBG) {
                    log("error while querying available networks");
                }
                displayNetworkQueryFailed(i);
            } else if (isAdded()) {
                if (z) {
                    createList(list);
                    return;
                }
                if (getActivity() != null) {
                    getActivity().removeDialog(200);
                }
                displayEmptyNetworkList(true);
            }
        }

        private boolean removePlmnJpKd(Phone phone, String str) {
            CarrierConfigManager carrierConfigManager;
            PersistableBundle configForSubId;
            if (str == null || !str.equals("44055") || !TelephonyManager.isCustForJpKd() || phone == null || phone.getContext() == null) {
                return false;
            }
            String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(phone.getPhoneId());
            if ((!"44051".equals(simOperatorForSlot) && !"44054".equals(simOperatorForSlot)) || (carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class)) == null || (configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId(), "satellite_attach_supported_bool")) == null) {
                return false;
            }
            return configForSubId.getBoolean("satellite_attach_supported_bool", false);
        }

        private List filterPlmnSatellite(Phone phone, List list) {
            SatelliteManager satelliteManager;
            if (phone != null) {
                try {
                    if (phone.getContext() != null && (satelliteManager = (SatelliteManager) phone.getContext().getSystemService(SatelliteManager.class)) != null) {
                        final List satellitePlmnsForCarrier = satelliteManager.getSatellitePlmnsForCarrier(phone.getSubId());
                        if (!satellitePlmnsForCarrier.isEmpty() && PhoneGlobals.getInstance() != null && PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId()).getBoolean("remove_satellite_plmn_in_manual_network_scan_bool", true)) {
                            return (List) list.stream().filter(new Predicate() { // from class: com.android.phone.settings.NetworkSetting$NetworkSettingFragment$$ExternalSyntheticLambda1
                                @Override // java.util.function.Predicate
                                public final boolean test(Object obj) {
                                    return NetworkSetting.NetworkSettingFragment.$r8$lambda$YGTnG9tojUfAtkMgG1WdfrCzvcc(satellitePlmnsForCarrier, (OperatorInfo) obj);
                                }
                            }).collect(Collectors.toList());
                        }
                    }
                } catch (IllegalStateException e) {
                    log("e1:" + e);
                } catch (Exception e2) {
                    log("e2:" + e2);
                }
            }
            return list;
        }

        public static /* synthetic */ boolean $r8$lambda$YGTnG9tojUfAtkMgG1WdfrCzvcc(List list, OperatorInfo operatorInfo) {
            return (operatorInfo == null || list.contains(operatorInfo.getOperatorNumeric())) ? false : true;
        }

        protected void createList(List list) {
            RadioButtonPreferenceCategory radioButtonPreferenceCategory = new RadioButtonPreferenceCategory(getPreferenceManager().getContext(), null);
            this.mAvailableNetworkList = radioButtonPreferenceCategory;
            radioButtonPreferenceCategory.setTitle(R.string.label_available);
            this.mNetworkList.addPreference(this.mAvailableNetworkList);
            if (MiuiPhoneUtils.isDelRatForManualList(this.mPhone.getPhoneId())) {
                HashMap map = new HashMap();
                for (int i = 0; i < list.size(); i++) {
                    String operatorNumeric = ((OperatorInfo) list.get(i)).getOperatorNumeric();
                    if (map.containsKey(operatorNumeric)) {
                        if (((OperatorInfo) list.get(i)).getState() == OperatorInfo.State.CURRENT) {
                            ((OperatorInfo) list.get(i)).setRadioTech("");
                            map.put(operatorNumeric, MiuiPhoneUtils.delRatForMTK((OperatorInfo) list.get(i)));
                        }
                    } else {
                        ((OperatorInfo) list.get(i)).setRadioTech("");
                        map.put(operatorNumeric, MiuiPhoneUtils.delRatForMTK((OperatorInfo) list.get(i)));
                    }
                }
                list = new ArrayList(map.values());
            }
            for (OperatorInfo operatorInfo : filterPlmnSatellite(this.mPhone, list)) {
                if (!operatorInfo.getOperatorNumeric().equals("46020") && (!getRadioTechText(operatorInfo).equals(" 5G") || FiveGManagerBase.getInstance().isUserFiveGSaEnabled(this.mSlotId))) {
                    if (!removePlmnJpKd(this.mPhone, operatorInfo.getOperatorNumeric())) {
                        RadioButtonPreference radioButtonPreference = new RadioButtonPreference(getPreferenceManager().getContext());
                        radioButtonPreference.setLayoutResource(R$layout.miuix_preference_radiobutton_two_state_background);
                        String networkSpn = getNetworkSpn(operatorInfo.getOperatorNumeric(), operatorInfo.getOperatorAlphaLong());
                        String networkTitle = getNetworkTitle(operatorInfo, networkSpn);
                        OperatorInfo.State state = operatorInfo.getState();
                        if (ModemProvider.IS_SPRD) {
                            if (SprdCellInfoUtil.isForbidden(operatorInfo.getOperatorNumeric()) || isShowForbidden(operatorInfo)) {
                                state = OperatorInfo.State.FORBIDDEN;
                            }
                        } else if (isShowForbidden(operatorInfo)) {
                            state = OperatorInfo.State.FORBIDDEN;
                        }
                        if (!TextUtils.isEmpty(networkSpn)) {
                            if (!MiuiPhoneUtils.isDelRatForManualList(this.mPhone.getPhoneId()) && ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && !networkSpn.equals(operatorInfo.getOperatorAlphaLong()) && !TextUtils.isEmpty(operatorInfo.getOperatorAlphaLong()))) {
                                String[] strArrSplit = operatorInfo.getOperatorAlphaLong().split(" ");
                                networkSpn = networkSpn + " " + strArrSplit[strArrSplit.length - 1];
                            }
                            radioButtonPreference.setTitle(networkSpn + getRadioTechText(operatorInfo));
                            if (state == OperatorInfo.State.FORBIDDEN) {
                                networkTitle = getString(R.string.network_settings_forbidden, networkTitle);
                            }
                            if (HyperOSCustFeatureResolve.getBooleanForPhoneId(this.mSlotId, "display_network_title", true)) {
                                radioButtonPreference.setSummary(networkTitle);
                            }
                        } else {
                            String string = networkTitle + getRadioTechText(operatorInfo);
                            if (state == OperatorInfo.State.FORBIDDEN) {
                                string = getString(R.string.network_settings_forbidden, string);
                            }
                            radioButtonPreference.setTitle(string);
                        }
                        replaceOperatorTitleAndSummary(radioButtonPreference, operatorInfo);
                        if (Build.IS_CU_CUSTOMIZATION_TEST && state == OperatorInfo.State.FORBIDDEN) {
                            radioButtonPreference.setEnabled(false);
                        }
                        radioButtonPreference.setPersistent(false);
                        if (operatorInfo.getState() == OperatorInfo.State.CURRENT) {
                            radioButtonPreference.setChecked(true);
                        }
                        this.mAvailableNetworkList.addPreference(radioButtonPreference);
                        if (ModemProvider.IS_QCOM && !MiuiPhoneUtils.isQcSaNetwork() && !TextUtils.isEmpty(operatorInfo.getRadioTech())) {
                            operatorInfo.setRadioTech(Integer.toString(ServiceState.rilRadioTechnologyToAccessNetworkType(Integer.parseInt(operatorInfo.getRadioTech()))));
                        }
                        this.mNetworkMap.put(radioButtonPreference, operatorInfo);
                        if (NetworkSetting.DBG) {
                            log("   " + operatorInfo);
                        }
                    }
                }
            }
        }

        public void replaceOperatorTitleAndSummary(RadioButtonPreference radioButtonPreference, OperatorInfo operatorInfo) {
            String string = getResources().getString(R.string.replace_4g_template);
            String operatorNumeric = operatorInfo.getOperatorNumeric();
            PersistableBundle carrierConfigMap = MiuiPhoneUtils.getCarrierConfigMap(this.mPhone.getSubId(), "manual_scan_network_prefix_list");
            String string2 = "";
            String string3 = carrierConfigMap == null ? "" : carrierConfigMap.getString(operatorNumeric);
            PersistableBundle carrierConfigMap2 = MiuiPhoneUtils.getCarrierConfigMap(this.mPhone.getSubId(), "manual_scan_network_suffix_list");
            if (carrierConfigMap2 != null) {
                string2 = carrierConfigMap2.getString(operatorNumeric);
            }
            if (!TextUtils.isEmpty(string2)) {
                string = string2;
            }
            CharSequence title = radioButtonPreference.getTitle();
            if (!TextUtils.isEmpty(title)) {
                String strReplaceLastStr = replaceLastStr(replaceNetworkName(title.toString(), string3), "4G", string);
                if (TelephonyManager.isCustForJpSb() && "44000".equals(operatorNumeric)) {
                    strReplaceLastStr = operatorNumeric;
                }
                radioButtonPreference.setTitle(strReplaceLastStr);
            }
            CharSequence summary = radioButtonPreference.getSummary();
            if (TextUtils.isEmpty(summary)) {
                return;
            }
            String strReplaceNetworkName = replaceNetworkName(summary.toString(), string3);
            if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                strReplaceNetworkName = replaceLastStr(strReplaceNetworkName, "4G", string);
            }
            if (!TelephonyManager.isCustForJpSb() || !"44000".equals(operatorNumeric)) {
                operatorNumeric = strReplaceNetworkName;
            }
            radioButtonPreference.setSummary(operatorNumeric);
        }

        protected String getNetworkSpn(String str, String str2) {
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            int iIndexOf = str.indexOf(44);
            if (iIndexOf > 0) {
                str = str.substring(0, iIndexOf);
            }
            String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(this.mPhone.getPhoneId());
            String simOperatorName = MiuiPhoneUtils.getSimOperatorName(this.mPhone.getPhoneId());
            if (TelephonyManager.isCustFeatureResolveEnabled()) {
                if ((!ModemProvider.IS_MTK && !ModemProvider.IS_XRING) || !"22299".equals(str) || "22299".equals(simOperatorForSlot)) {
                    Map mapForPhoneId = HyperOSCustFeatureResolve.getMapForPhoneId(this.mPhone.getPhoneId(), "manual_network_spn_rule");
                    Log.d(NetworkSetting.LOG_TAG, "getNetworkSpn() data = " + mapForPhoneId);
                    if (!ArrayUtils.isEmpty(mapForPhoneId) && mapForPhoneId.containsKey(str)) {
                        String str3 = (String) mapForPhoneId.get(str);
                        if (!TextUtils.isEmpty(str3)) {
                            String[] strArrSplit = str3.split(",");
                            if (strArrSplit.length > 0 && strArrSplit.length < 3) {
                                int i = Integer.parseInt(strArrSplit[0]);
                                Log.d(NetworkSetting.LOG_TAG, "getNetworkSpn() spnRule = " + i);
                                if (i != 1) {
                                    if (i == 2) {
                                        return getSimOperatorName();
                                    }
                                    if (i != 3) {
                                        if (i == 4) {
                                            if (strArrSplit.length == 2 && TextUtils.equals(strArrSplit[1], simOperatorName)) {
                                                return strArrSplit[1];
                                            }
                                        } else {
                                            Log.d(NetworkSetting.LOG_TAG, "spnRule is invalid.");
                                            return "";
                                        }
                                    } else if (strArrSplit.length == 2) {
                                        return strArrSplit[1];
                                    }
                                }
                                return str2;
                            }
                            Log.d(NetworkSetting.LOG_TAG, "get network spn failed.");
                        }
                        return "";
                    }
                    String str4 = ServiceProviderUtils.get(str, this.mSlotId, str2);
                    Log.d(NetworkSetting.LOG_TAG, "get default spn, networkSpn = " + str4);
                    return str4;
                }
                return str;
            }
            if ((!"46697".equals(str) || !"46605".equals(simOperatorForSlot) || TextUtils.isEmpty(str2)) && ((!"46601".equals(str) && !"46602".equals(str) && !"46603".equals(str)) || ((!"46605".equals(simOperatorForSlot) && !"46612".equals(simOperatorForSlot)) || TextUtils.isEmpty(str2)))) {
                if (("73023".equals(str) || "73022".equals(str)) && "73003".equals(simOperatorForSlot) && !TextUtils.isEmpty(getSimOperatorName())) {
                    return getSimOperatorName();
                }
                if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                    if (!"22299".equals(str) || "22299".equals(simOperatorForSlot)) {
                        if ("50219".equals(str) && "502153".equals(simOperatorForSlot)) {
                            return "MY CELCOM";
                        }
                        if ("27203".equals(str) && "27202".equals(simOperatorForSlot)) {
                            return "Eir";
                        }
                        if ("20201".equals(str) && "20201".equals(simOperatorForSlot)) {
                            return "GR COSMOTE";
                        }
                    }
                    return str;
                }
                if ("22207".equals(simOperatorForSlot) && "22201".equals(str) && TextUtils.equals("Kena Mobile", simOperatorName)) {
                    return "Kena Mobile";
                }
                if ("21670".equals(simOperatorForSlot) && "21670".equals(str)) {
                    return "One HU";
                }
                if (((!"45416".equals(str) && !"45419".equals(str)) || !"45400".equals(simOperatorForSlot)) && (!"23430".equals(str) || !"23438".equals(simOperatorForSlot) || TextUtils.isEmpty(str2))) {
                    return ServiceProviderUtils.get(str, this.mSlotId, str2);
                }
            }
            return str2;
        }

        private String getNetworkTitle(OperatorInfo operatorInfo, String str) {
            String operatorNumeric = operatorInfo.getOperatorNumeric();
            String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(this.mPhone.getPhoneId());
            String simOperatorName = MiuiPhoneUtils.getSimOperatorName(this.mPhone.getPhoneId());
            if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && "22299".equals(operatorNumeric) && !"22299".equals(simOperatorForSlot)) {
                return operatorNumeric;
            }
            if (TelephonyManager.isCustFeatureResolveEnabled()) {
                Map mapForPhoneId = HyperOSCustFeatureResolve.getMapForPhoneId(this.mPhone.getPhoneId(), "manual_network_title_rule");
                Log.d(NetworkSetting.LOG_TAG, "getNetworkTitle() data = " + mapForPhoneId);
                if (!ArrayUtils.isEmpty(mapForPhoneId) && mapForPhoneId.containsKey(operatorNumeric)) {
                    String str2 = (String) mapForPhoneId.get(operatorNumeric);
                    if (!TextUtils.isEmpty(str2)) {
                        String[] strArrSplit = str2.split(",");
                        if (strArrSplit.length > 0 && strArrSplit.length < 3) {
                            int i = Integer.parseInt(strArrSplit[0]);
                            Log.d(NetworkSetting.LOG_TAG, "getNetworkTitle() titleRule = " + i);
                            if (i != 1) {
                                if (i == 2) {
                                    if (strArrSplit.length == 2) {
                                        return strArrSplit[1];
                                    }
                                } else {
                                    Log.d(NetworkSetting.LOG_TAG, "titleRule is invalid.");
                                    return "";
                                }
                            } else if (strArrSplit.length == 2 && TextUtils.equals(strArrSplit[1], simOperatorName)) {
                                return strArrSplit[1];
                            }
                        } else {
                            Log.d(NetworkSetting.LOG_TAG, "get network title failed.");
                        }
                    }
                    return "";
                }
                if (TextUtils.isEmpty(str) || !isUsingSpnAsNetworkTitle(simOperatorForSlot, operatorNumeric)) {
                    if (!TextUtils.isEmpty(operatorInfo.getOperatorAlphaLong())) {
                        str = operatorInfo.getOperatorAlphaLong();
                    } else {
                        str = !TextUtils.isEmpty(operatorInfo.getOperatorAlphaShort()) ? operatorInfo.getOperatorAlphaShort() : operatorNumeric;
                    }
                }
                Log.d(NetworkSetting.LOG_TAG, "get default title, networkTitle = " + str);
                return str;
            }
            if ("22207".equals(simOperatorForSlot) && "22201".equals(operatorNumeric) && TextUtils.equals("Kena Mobile", simOperatorName)) {
                return str;
            }
            if (TextUtils.isEmpty(str) || !isUsingSpnAsNetworkTitle(simOperatorForSlot, operatorNumeric)) {
                if (!TextUtils.isEmpty(operatorInfo.getOperatorAlphaLong())) {
                    str = operatorInfo.getOperatorAlphaLong();
                } else {
                    str = !TextUtils.isEmpty(operatorInfo.getOperatorAlphaShort()) ? operatorInfo.getOperatorAlphaShort() : operatorNumeric;
                }
            }
            if ("46697".equals(operatorNumeric) && "46697".equals(simOperatorForSlot)) {
                str = "TW Mobile";
            }
            return ("21670".equals(operatorNumeric) && "21670".equals(simOperatorForSlot)) ? "One HU" : str;
        }

        private boolean isUsingSpnAsNetworkTitle(String str, String str2) {
            if (TelephonyManager.isCustFeatureResolveEnabled()) {
                List stringArrayForPhoneId = HyperOSCustFeatureResolve.getStringArrayForPhoneId(this.mPhone.getPhoneId(), "manual_network_using_spn");
                if (ArrayUtils.isEmpty(stringArrayForPhoneId)) {
                    return false;
                }
                boolean zContains = stringArrayForPhoneId.contains(str2);
                Log.d(NetworkSetting.LOG_TAG, "isUsingSpnAsNetworkTitle() isMatched = " + zContains);
                return zContains;
            }
            Log.d(NetworkSetting.LOG_TAG, "isUsingSpnAsNetworkTitle() use old logic");
            if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                if ("502153".equals(str) && ("50212".equals(str2) || "50219".equals(str2))) {
                    return true;
                }
                if ("60204".equals(str) && "60203".equals(str2)) {
                    return true;
                }
                if ("27203".equals(str2) && "27202".equals(str)) {
                    return true;
                }
                if ("62120".equals(str2) && "62120".equals(str)) {
                    return true;
                }
                if ("62124".equals(str) && "62124".equals(str2)) {
                    return true;
                }
                if ("50212".equals(str) && "502152".equals(str2)) {
                    return true;
                }
            }
            return "20201".equals(str2);
        }

        private boolean isShowForbidden(OperatorInfo operatorInfo) {
            boolean zStartsWith;
            String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(this.mPhone.getPhoneId());
            String operatorNumeric = operatorInfo.getOperatorNumeric();
            if (operatorNumeric != null) {
                zStartsWith = operatorNumeric.startsWith("460");
                if (MiuiNetworkControllerImpl.Processor.isCnSharedPlmn(operatorNumeric)) {
                    return false;
                }
            } else {
                zStartsWith = false;
            }
            if (ServiceProviderUtils.isChinaMobile(simOperatorForSlot) && !ServiceProviderUtils.isChinaMobile(operatorNumeric) && zStartsWith) {
                return true;
            }
            if (ServiceProviderUtils.isChinaUnicom(simOperatorForSlot) && !ServiceProviderUtils.isChinaUnicom(operatorNumeric) && zStartsWith) {
                return true;
            }
            if (ServiceProviderUtils.isChinaTelecom(simOperatorForSlot) && !ServiceProviderUtils.isChinaTelecom(operatorNumeric) && zStartsWith) {
                return true;
            }
            return MiuiPhoneUtils.isCbnSim(simOperatorForSlot) && !MiuiPhoneUtils.isCbnSim(operatorNumeric) && zStartsWith;
        }

        private String getRadioTechText(OperatorInfo operatorInfo) {
            if (MiuiPhoneUtils.isQcSaNetwork()) {
                return getRadioTechTextFromIncrementalQuery(operatorInfo);
            }
            return getRadioTechTextFromNormalQuery(operatorInfo);
        }

        private String getRadioTechTextFromIncrementalQuery(OperatorInfo operatorInfo) {
            String radioTech = operatorInfo.getRadioTech();
            if (TextUtils.isEmpty(radioTech)) {
                return "";
            }
            int iConvertRanToAnt = AccessNetworkConstants.AccessNetworkType.convertRanToAnt(Integer.parseInt(radioTech));
            if (iConvertRanToAnt == 2) {
                return " 3G";
            }
            if (iConvertRanToAnt == 3) {
                return getRadioTechTextFor4g(operatorInfo);
            }
            if (iConvertRanToAnt == 6) {
                return " 5G";
            }
            return " 2G";
        }

        private String getRadioTechTextFor4g(OperatorInfo operatorInfo) {
            if (operatorInfo != null) {
                String operatorNumeric = operatorInfo.getOperatorNumeric();
                if (TelephonyManager.isCustFeatureResolveEnabled()) {
                    Map mapForPhoneId = HyperOSCustFeatureResolve.getMapForPhoneId(this.mPhone.getPhoneId(), "manual_network_rat_text");
                    if (!ArrayUtils.isEmpty(mapForPhoneId)) {
                        if (mapForPhoneId.containsKey(operatorNumeric)) {
                            String str = (String) mapForPhoneId.get(operatorNumeric);
                            log("getRadioTechTextFor4g() mccmnc = " + operatorNumeric + ", radioText = " + str);
                            if (TextUtils.isEmpty(str)) {
                                return str;
                            }
                            return " " + str;
                        }
                        if (mapForPhoneId.containsKey("radio_text_config")) {
                            String str2 = (String) mapForPhoneId.get("radio_text_config");
                            log("getRadioTechTextFor4g() RADIO_TECH_TEXT_KEY, radioText = " + str2);
                            if (TextUtils.isEmpty(str2)) {
                                return str2;
                            }
                            return " " + str2;
                        }
                    }
                } else {
                    if (TelephonyManager.isCustForMxAt() && ("334050".equals(operatorNumeric) || "334090".equals(operatorNumeric))) {
                        return " LTE";
                    }
                    if (!Build.checkRegion("TR")) {
                        return " 4G";
                    }
                    return " 4.5G";
                }
            }
            return " 4G";
        }

        private String getRadioTechTextFromNormalQuery(OperatorInfo operatorInfo) {
            String radioTech = operatorInfo.getRadioTech();
            if (TextUtils.isEmpty(radioTech)) {
                return "";
            }
            operatorInfo.getOperatorNumeric();
            try {
                int i = Integer.parseInt(radioTech);
                if (i <= 2) {
                    return " 2G";
                }
                if (i == 20) {
                    return " 5G";
                }
                if (i == 14) {
                    return " 4G";
                }
                return " 3G";
            } catch (NumberFormatException unused) {
                log("parse radio tech error: " + radioTech);
                return " 2G";
            }
        }

        private String replaceLastStr(String str, String str2, String str3) {
            String strTrim;
            int iLastIndexOf;
            if (!TextUtils.isEmpty(str3) && !TextUtils.equals(str2, str3) && (iLastIndexOf = (strTrim = str.trim()).lastIndexOf(" ")) > -1) {
                int i = iLastIndexOf + 1;
                if (TextUtils.equals(strTrim.substring(i), str2)) {
                    return strTrim.substring(0, i) + str3;
                }
            }
            return str;
        }

        private String replaceNetworkName(String str, String str2) {
            String strRemoveUnexceptedChar = removeUnexceptedChar(str);
            if (TextUtils.isEmpty(str2)) {
                return strRemoveUnexceptedChar;
            }
            String strTrim = strRemoveUnexceptedChar.trim();
            int iLastIndexOf = strTrim.lastIndexOf(" ");
            if (iLastIndexOf <= -1) {
                return str2;
            }
            return str2 + strTrim.substring(iLastIndexOf);
        }

        private String removeUnexceptedChar(String str) {
            if (TextUtils.isEmpty(str)) {
                return str;
            }
            String strReplaceAll = str.replaceAll("[\n\r]", "");
            log("removeUnexceptedChar networkName = " + strReplaceAll);
            return strReplaceAll;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void selectNetworkAutomatic() {
            if (NetworkSetting.DBG) {
                log("select network automatically...");
            }
            if (this.mIsForeground && getActivity() != null) {
                getActivity().showDialog(MiRilHook.HOOK_ID_START_URC_BY_AT);
            }
            this.mPhone.setNetworkSelectionModeAutomatic(this.mHandler.obtainMessage(MiRilHook.HOOK_ID_START_URC_BY_AT));
            clearList();
            this.mAutoSelect.setSummaryOn(getAutoSelectionSummary(true));
            this.mAutoSelect.setChecked(true);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("button_auto_select_key" + this.mSlotId, true).apply();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CheckBoxPreference getSelectCheckBox() {
            return this.mAutoSelect;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void log(String str) {
            Log.d(NetworkSetting.LOG_TAG, "[NetworksList] " + str);
        }
    }
}
