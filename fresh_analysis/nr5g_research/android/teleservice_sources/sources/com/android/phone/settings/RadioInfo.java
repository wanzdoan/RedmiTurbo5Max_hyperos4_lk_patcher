package com.android.phone.settings;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ComponentInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Insets;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.os.OutcomeReceiver;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.DataSpecificRegistrationInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseProxy;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.data.NetworkSlicingConfig;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.ims.ImsConfig;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.euicc.EuiccConnector;
import com.android.phone.R;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.QtiImeiInfo;
import com.qti.extphone.ServiceCallback;
import com.xiaomi.internal.telephony.ModemSarUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import miui.enterprise.EnterpriseManagerStub;
import miui.enterprise.PhoneHelperStub;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class RadioInfo extends AppCompatActivity {
    private static final boolean IS_CUST_SINGLE_SIM;
    private static final boolean IS_INTERNATIONAL_BUILD;
    private static String[] sPhoneIndexLabels;
    private TextView mCallState;
    private Button mCarrierProvisioningButton;
    private Switch mCbrsDataSwitch;
    private TextView mCellInfo;
    private int mCellInfoRefreshRateIndex;
    private Spinner mCellInfoRefreshRateSpinner;
    private TextView mCfi;
    private ConnectivityManager mConnectivityManager;
    private TextView mDBm;
    private TextView mDataNetwork;
    private TextView mDcnrRestricted;
    private TextView mDds;
    private TextView mDeviceId;
    private TextView mDnsCheckState;
    private Button mDnsCheckToggleButton;
    private TextView mDownlinkKbps;
    private TextView mDsdaState;
    private Switch mDsdsSwitch;
    private Switch mEabProvisionedSwitch;
    private TextView mEndcAvailable;
    private TextView mGprsState;
    private TextView mGsmState;
    private TextView mHttpClientTest;
    private String mHttpClientTestResult;
    private Switch mImsVolteProvisionedSwitch;
    private Switch mImsVtProvisionedSwitch;
    private Switch mImsWfcProvisionedSwitch;
    private boolean mIsShow5GNsaType;
    private boolean mIsShow5GSaType;
    private TextView mLine1Number;
    private TextView mMwi;
    private TextView mNetworkSlicingConfig;
    private TextView mNrAvailable;
    private TextView mNrFrequency;
    private TextView mNrState;
    private Button mOemInfoButton;
    private TextView mOperatorName;
    private TextView mOverrideNetwork;
    private TextView mPhyChanConfig;
    private String mPingHostnameResultV4;
    private String mPingHostnameResultV6;
    private TextView mPingHostnameV4;
    private TextView mPingHostnameV6;
    private Button mPingTestButton;
    private Spinner mPreferredNetworkType;
    private int mPreferredNetworkTypeResult;
    private ThreadPoolExecutor mQueuedWork;
    private Switch mRadioPowerOnSwitch;
    private TextView mReceived;
    private Button mRefreshSmscButton;
    private Switch mRemovableEsimSwitch;
    private TextView mRoamingState;
    private Spinner mSelectPhoneIndex;
    private int mSelectedPhoneIndex;
    private TextView mSent;
    private int mSlotId;
    private EditText mSmsc;
    private TextView mSubscriberId;
    private TextView mSubscriptionId;
    private TelephonyManager mTelephonyManager;
    private Button mTriggerCarrierProvisioningButton;
    private Button mUpdateSmscButton;
    private TextView mUplinkKbps;
    private TextView mVoiceNetwork;
    private static final boolean IS_USER_BUILD = "user".equals(Build.TYPE);
    private static final String[] PREFERRED_NETWORK_LABELS = {"GSM/WCDMA preferred", "GSM only", "WCDMA only", "GSM/WCDMA auto (PRL)", "CDMA/EvDo auto (PRL)", "CDMA only", "EvDo only", "CDMA/EvDo/GSM/WCDMA (PRL)", "CDMA + LTE/EvDo (PRL)", "GSM/WCDMA/LTE (PRL)", "LTE/CDMA/EvDo/GSM/WCDMA (PRL)", "LTE only", "LTE/WCDMA", "TDSCDMA only", "TDSCDMA/WCDMA", "LTE/TDSCDMA", "TDSCDMA/GSM", "LTE/TDSCDMA/GSM", "TDSCDMA/GSM/WCDMA", "LTE/TDSCDMA/WCDMA", "LTE/TDSCDMA/GSM/WCDMA", "TDSCDMA/CDMA/EvDo/GSM/WCDMA ", "LTE/TDSCDMA/CDMA/EvDo/GSM/WCDMA", "NR only", "NR/LTE", "NR/LTE/CDMA/EvDo", "NR/LTE/GSM/WCDMA", "NR/LTE/CDMA/EvDo/GSM/WCDMA", "NR/LTE/WCDMA", "NR/LTE/TDSCDMA", "NR/LTE/TDSCDMA/GSM", "NR/LTE/TDSCDMA/WCDMA", "NR/LTE/TDSCDMA/GSM/WCDMA", "NR/LTE/TDSCDMA/CDMA/EvDo/GSM/WCDMA", "Unknown"};
    private static final String[] CELL_INFO_REFRESH_RATE_LABELS = {"Disabled", "Immediate", "Min 5s", "Min 10s", "Min 60s"};
    private static final int[] CELL_INFO_REFRESH_RATES = {Integer.MAX_VALUE, 0, 5000, 10000, 60000};
    private static final String CUSTOMIZED_REGION = SystemProperties.get("ro.miui.customized.region", "");
    private static final String REGION = SystemProperties.get("ro.miui.region", "");
    private static final String COTA_CARRIER = SystemProperties.get("persist.sys.cota.carrier", "");
    public static final boolean IS_REGION_MX = "MX".equals(SystemProperties.get("ro.miui.region", "unknown"));
    private ExtTelephonyManager mExtPhone = null;
    private boolean mIsExtPhoneConnected = false;
    private QExtPhoneCallback mExtPhoneCallback = null;
    private ImsManager mImsManager = null;
    private Phone mPhone = null;
    private boolean mRegistedNetworkCallback = false;
    private boolean mMwiValue = false;
    private boolean mCfiValue = false;
    private List mCellInfoResult = null;
    private boolean mIsSupportDsda = false;
    private final NetworkRequest mDefaultNetworkRequest = new NetworkRequest.Builder().addTransportType(0).addCapability(12).build();
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.phone.settings.RadioInfo.1
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            RadioInfo.this.updateBandwidths(networkCapabilities.getLinkDownstreamBandwidthKbps(), networkCapabilities.getLinkUpstreamBandwidthKbps());
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.RadioInfo.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            RadioInfo.log("onReceive action : " + action);
            if ("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED".equals(action)) {
                RadioInfo.this.updateDsdaState();
            } else if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                RadioInfo.this.updateRadioPowerState();
            }
        }
    };
    private TelephonyCallback mTelephonyCallback = new RadioInfoTelephonyCallback();
    private Handler mHandler = new Handler() { // from class: com.android.phone.settings.RadioInfo.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1005:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    Throwable th = asyncResult.exception;
                    RadioInfo radioInfo = RadioInfo.this;
                    if (th != null) {
                        radioInfo.mSmsc.setText("refresh error");
                    } else {
                        radioInfo.mSmsc.setText((String) asyncResult.result);
                    }
                    break;
                case 1006:
                    RadioInfo.this.mUpdateSmscButton.setEnabled(true);
                    if (((AsyncResult) message.obj).exception != null) {
                        RadioInfo.this.mSmsc.setText("update error");
                    }
                    break;
                case 1007:
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    if (asyncResult2.exception != null) {
                        RadioInfo.this.mPhyChanConfig.setText("update error");
                    }
                    RadioInfo.this.updatePhysicalChannelConfiguration((List) asyncResult2.result);
                    break;
                default:
                    super.handleMessage(message);
                    break;
            }
        }
    };
    ContentObserver m5GNetworkTypeObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.android.phone.settings.RadioInfo.4
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (SubscriptionManager.isValidSlotIndex(RadioInfo.this.mSlotId)) {
                RadioInfo radioInfo = RadioInfo.this;
                ContentResolver contentResolver = radioInfo.mPhone.getContext().getContentResolver();
                StringBuilder sb = new StringBuilder("5g_icon_group_mode");
                sb.append(RadioInfo.this.mSlotId);
                radioInfo.mIsShow5GNsaType = Settings.Global.getInt(contentResolver, sb.toString(), 0) == 1;
                RadioInfo radioInfo2 = RadioInfo.this;
                ContentResolver contentResolver2 = radioInfo2.mPhone.getContext().getContentResolver();
                StringBuilder sb2 = new StringBuilder("5g_icon_group_mode");
                sb2.append(RadioInfo.this.mSlotId);
                radioInfo2.mIsShow5GSaType = Settings.Global.getInt(contentResolver2, sb2.toString(), 0) == 2;
                RadioInfo.this.updateNetworkType();
                Log.d("RadioInfo", "onChange mIsShow5GNsaType:" + RadioInfo.this.mIsShow5GNsaType + ", mIsShow5GSaType:" + RadioInfo.this.mIsShow5GSaType + ", mSlotId:" + RadioInfo.this.mSlotId);
            }
        }
    };
    private MenuItem.OnMenuItemClickListener mViewADNCallback = new MenuItem.OnMenuItemClickListener() { // from class: com.android.phone.settings.RadioInfo.11
        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.SimContacts");
            RadioInfo.this.startActivity(intent);
            return true;
        }
    };
    private MenuItem.OnMenuItemClickListener mViewFDNCallback = new MenuItem.OnMenuItemClickListener() { // from class: com.android.phone.settings.RadioInfo.12
        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.fdn.FdnList");
            RadioInfo.this.startActivity(intent);
            return true;
        }
    };
    private MenuItem.OnMenuItemClickListener mViewSDNCallback = new MenuItem.OnMenuItemClickListener() { // from class: com.android.phone.settings.RadioInfo.13
        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("content://icc/sdn"));
            intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.ADNList");
            RadioInfo.this.startActivity(intent);
            return true;
        }
    };
    private MenuItem.OnMenuItemClickListener mGetImsStatus = new MenuItem.OnMenuItemClickListener() { // from class: com.android.phone.settings.RadioInfo.14
        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            String string;
            boolean zIsImsRegistered = RadioInfo.this.mPhone.isImsRegistered();
            boolean zIsVoiceOverCellularImsEnabled = RadioInfo.this.mPhone.isVoiceOverCellularImsEnabled();
            boolean zIsWifiCallingEnabled = RadioInfo.this.mPhone.isWifiCallingEnabled();
            boolean zIsVideoEnabled = RadioInfo.this.mPhone.isVideoEnabled();
            boolean zIsUtEnabled = RadioInfo.this.mPhone.isUtEnabled();
            RadioInfo radioInfo = RadioInfo.this;
            if (zIsImsRegistered) {
                string = radioInfo.getString(R.string.radio_info_ims_reg_status_registered);
            } else {
                string = radioInfo.getString(R.string.radio_info_ims_reg_status_not_registered);
            }
            String string2 = RadioInfo.this.getString(R.string.radio_info_ims_feature_status_available);
            String string3 = RadioInfo.this.getString(R.string.radio_info_ims_feature_status_unavailable);
            RadioInfo radioInfo2 = RadioInfo.this;
            int i = R.string.radio_info_ims_reg_status;
            String str = zIsVoiceOverCellularImsEnabled ? string2 : string3;
            String str2 = zIsWifiCallingEnabled ? string2 : string3;
            String str3 = zIsVideoEnabled ? string2 : string3;
            if (!zIsUtEnabled) {
                string2 = string3;
            }
            new AlertDialog.Builder(RadioInfo.this).setMessage(radioInfo2.getString(i, new Object[]{string, str, str2, str3, string2})).setTitle(RadioInfo.this.getString(R.string.radio_info_ims_reg_status_title)).create().show();
            return true;
        }
    };
    private MenuItem.OnMenuItemClickListener mSelectBandCallback = new MenuItem.OnMenuItemClickListener() { // from class: com.android.phone.settings.RadioInfo.15
        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (!miui.telephony.TelephonyManager.isCustForKrOps()) {
                return true;
            }
            Intent intent = new Intent();
            try {
                long[] jArr = MiuiBandMode.BAND_VALUES;
                intent.setClass(RadioInfo.this, MiuiBandMode.class);
                intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, RadioInfo.this.mPhone.getPhoneId());
                RadioInfo.this.startActivity(intent);
                return true;
            } catch (Exception e) {
                RadioInfo.log("Exception: " + e);
                return true;
            }
        }
    };
    private MenuItem.OnMenuItemClickListener mToggleData = new MenuItem.OnMenuItemClickListener() { // from class: com.android.phone.settings.RadioInfo.16
        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            boolean dataEnabled = RadioInfo.this.mTelephonyManager.getDataEnabled();
            RadioInfo radioInfo = RadioInfo.this;
            if (dataEnabled) {
                radioInfo.mTelephonyManager.setDataEnabled(false);
            } else {
                radioInfo.mTelephonyManager.setDataEnabled(true);
            }
            return true;
        }
    };
    CompoundButton.OnCheckedChangeListener mRadioPowerOnChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.RadioInfo.18
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Phone[] phones = PhoneFactory.getPhones();
            if (phones == null) {
                return;
            }
            StringBuilder sb = new StringBuilder("toggle radio power: phone*");
            sb.append(phones.length);
            sb.append(" ");
            sb.append(RadioInfo.this.isRadioOn() ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF);
            RadioInfo.log(sb.toString());
            for (Phone phone : phones) {
                if (phone != null) {
                    phone.setRadioPower(z);
                }
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mImsVolteCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.RadioInfo.19
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            RadioInfo.this.setImsVolteProvisionedState(z);
        }
    };
    CompoundButton.OnCheckedChangeListener mImsVtCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.RadioInfo.20
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            RadioInfo.this.setImsVtProvisionedState(z);
        }
    };
    CompoundButton.OnCheckedChangeListener mImsWfcCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.RadioInfo.21
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            RadioInfo.this.setImsWfcProvisionedState(z);
        }
    };
    CompoundButton.OnCheckedChangeListener mEabCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.RadioInfo.22
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            RadioInfo.this.setEabProvisionedState(z);
        }
    };
    View.OnClickListener mDnsCheckButtonHandler = new View.OnClickListener() { // from class: com.android.phone.settings.RadioInfo.23
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            RadioInfo.this.mPhone.disableDnsCheck(!RadioInfo.this.mPhone.isDnsCheckDisabled());
            RadioInfo.this.updateDnsCheckState();
        }
    };
    View.OnClickListener mOemInfoButtonHandler = new View.OnClickListener() { // from class: com.android.phone.settings.RadioInfo.24
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            try {
                RadioInfo.this.startActivity(new Intent("com.android.phone.settings.OEM_RADIO_INFO"));
            } catch (ActivityNotFoundException e) {
                RadioInfo.log("OEM-specific Info/Settings Activity Not Found : " + e);
            }
        }
    };
    View.OnClickListener mPingButtonHandler = new View.OnClickListener() { // from class: com.android.phone.settings.RadioInfo.25
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            RadioInfo.this.updatePingState();
        }
    };
    View.OnClickListener mUpdateSmscButtonHandler = new View.OnClickListener() { // from class: com.android.phone.settings.RadioInfo.26
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            RadioInfo.this.mUpdateSmscButton.setEnabled(false);
            RadioInfo.this.mQueuedWork.execute(new Runnable() { // from class: com.android.phone.settings.RadioInfo.26.1
                @Override // java.lang.Runnable
                public void run() {
                    RadioInfo.this.mPhone.setSmscAddress(RadioInfo.this.mSmsc.getText().toString(), RadioInfo.this.mHandler.obtainMessage(1006));
                }
            });
        }
    };
    View.OnClickListener mRefreshSmscButtonHandler = new View.OnClickListener() { // from class: com.android.phone.settings.RadioInfo.27
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            RadioInfo.this.refreshSmsc();
        }
    };
    View.OnClickListener mCarrierProvisioningButtonHandler = new View.OnClickListener() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda4
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            this.f$0.lambda$new$11(view);
        }
    };
    View.OnClickListener mTriggerCarrierProvisioningButtonHandler = new View.OnClickListener() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda5
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            this.f$0.lambda$new$12(view);
        }
    };
    AdapterView.OnItemSelectedListener mPreferredNetworkHandler = new AnonymousClass28();
    AdapterView.OnItemSelectedListener mSelectPhoneIndexHandler = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.settings.RadioInfo.29
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            Phone[] phones;
            if (i < 0 || i > RadioInfo.sPhoneIndexLabels.length - 1 || (phones = PhoneFactory.getPhones()) == null || phones.length <= i) {
                return;
            }
            int[] subId = SubscriptionManager.getSubId(i);
            int i2 = (subId == null || subId.length <= 0) ? -1 : subId[0];
            RadioInfo.this.mSelectedPhoneIndex = i;
            RadioInfo.this.updatePhoneIndex(i, i2);
            RadioInfo.this.updateImei();
        }
    };
    AdapterView.OnItemSelectedListener mCellInfoRefreshRateHandler = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.settings.RadioInfo.30
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            RadioInfo.this.mCellInfoRefreshRateIndex = i;
            RadioInfo.this.mPhone.setCellInfoListRate(RadioInfo.CELL_INFO_REFRESH_RATES[i], (WorkSource) null);
            RadioInfo.this.updateAllCellInfo();
        }
    };
    CompoundButton.OnCheckedChangeListener mCbrsDataSwitchChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.RadioInfo.32
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            RadioInfo.this.updateCbrsDataState(z);
        }
    };
    DialogInterface.OnClickListener mOnDsdsDialogConfirmedListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.RadioInfo.33
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == -1) {
                RadioInfo.this.mDsdsSwitch.toggle();
                RadioInfo.this.performDsdsSwitch();
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mRemovableEsimChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.settings.RadioInfo.34
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            RadioInfo.this.setRemovableEsimAsDefaultEuicc(z);
        }
    };

    static {
        IS_CUST_SINGLE_SIM = SystemProperties.getInt("ro.miui.singlesim", 0) == 1;
        IS_INTERNATIONAL_BUILD = SystemProperties.get(ModemSarUtils.PROPERTY_PRODUCT_MOD_DEVICE, "").contains("_global");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("RadioInfo", str);
    }

    class RadioInfoTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.DataActivityListener, TelephonyCallback.CallStateListener, TelephonyCallback.MessageWaitingIndicatorListener, TelephonyCallback.CallForwardingIndicatorListener, TelephonyCallback.CellInfoListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.ServiceStateListener, TelephonyCallback.DisplayInfoListener {
        private RadioInfoTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.DataConnectionStateListener
        public void onDataConnectionStateChanged(int i, int i2) {
            RadioInfo.this.updateDataState();
            RadioInfo.this.updateNetworkType();
        }

        @Override // android.telephony.TelephonyCallback.DataActivityListener
        public void onDataActivity(int i) {
            RadioInfo.this.updateDataStats2();
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            RadioInfo.this.updateNetworkType();
            RadioInfo.this.updatePhoneState(i);
        }

        @Override // android.telephony.TelephonyCallback.MessageWaitingIndicatorListener
        public void onMessageWaitingIndicatorChanged(boolean z) {
            RadioInfo.this.mMwiValue = z;
            RadioInfo.this.updateMessageWaiting();
        }

        @Override // android.telephony.TelephonyCallback.CallForwardingIndicatorListener
        public void onCallForwardingIndicatorChanged(boolean z) {
            RadioInfo.this.mCfiValue = z;
            RadioInfo.this.updateCallRedirect();
        }

        @Override // android.telephony.TelephonyCallback.CellInfoListener
        public void onCellInfoChanged(List list) {
            RadioInfo.log("onCellInfoChanged: arrayCi=" + list);
            RadioInfo.this.mCellInfoResult = list;
            RadioInfo radioInfo = RadioInfo.this;
            radioInfo.updateCellInfo(radioInfo.mCellInfoResult);
        }

        @Override // android.telephony.TelephonyCallback.SignalStrengthsListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            RadioInfo.log("onSignalStrengthChanged: SignalStrength=" + signalStrength);
            RadioInfo.this.updateSignalStrength(signalStrength);
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            RadioInfo.log("onServiceStateChanged: ServiceState=" + serviceState);
            RadioInfo.this.updateServiceState(serviceState);
            RadioInfo.this.updateRadioPowerState();
            RadioInfo.this.updateNetworkType();
            RadioInfo.this.updateImsProvisionedState();
            RadioInfo.this.updateNrStats(serviceState);
        }

        @Override // android.telephony.TelephonyCallback.DisplayInfoListener
        public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            RadioInfo.this.updateNetworkType();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePhysicalChannelConfiguration(List list) {
        StringBuilder sb = new StringBuilder("{");
        if (list != null) {
            Iterator it = list.iterator();
            String str = "";
            while (it.hasNext()) {
                PhysicalChannelConfig physicalChannelConfig = (PhysicalChannelConfig) it.next();
                sb.append(str);
                sb.append(physicalChannelConfig);
                str = ",";
            }
        }
        sb.append("}");
        this.mPhyChanConfig.setText(sb.toString());
    }

    private void updatePreferredNetworkType(int i) {
        String[] strArr = PREFERRED_NETWORK_LABELS;
        if (i >= strArr.length || i < 0) {
            log("Network type: unknown type value=" + i);
            i = strArr.length - 1;
        }
        this.mPreferredNetworkTypeResult = i;
        this.mPreferredNetworkType.setSelection(i, true);
        if (SystemProperties.getBoolean("ro.sys.mi.ep.activated", false) && TelephonyBaseProxy.get().isNetworkModesRestriction()) {
            this.mPreferredNetworkType.setEnabled(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePhoneIndex(int i, int i2) {
        unregisterPhoneStateListener();
        this.mTelephonyManager.setCellInfoListRate(Integer.MAX_VALUE, i2);
        this.mTelephonyManager = this.mTelephonyManager.createForSubscriptionId(i2);
        this.mImsManager = ImsManager.getInstance(getApplicationContext(), i);
        this.mPhone = PhoneFactory.getPhone(i);
        Log.d("RadioInfo", "updatePhoneIndex phoneIndex：" + i);
        this.mSlotId = i;
        updateAllFields();
        new Thread(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updatePhoneIndex$1();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePhoneIndex$1() {
        final int preferredNetworkTypeBitmask = (int) this.mTelephonyManager.getPreferredNetworkTypeBitmask();
        runOnUiThread(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updatePhoneIndex$0(preferredNetworkTypeBitmask);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePhoneIndex$0(int i) {
        updatePreferredNetworkType(RadioAccessFamily.getNetworkTypeFromRaf(i));
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        Window window;
        View decorView;
        super.onCreate(bundle);
        if (!Process.myUserHandle().isSystem()) {
            Log.e("RadioInfo", "Not run from system user, don't do anything.");
            finish();
            return;
        }
        setContentView(R.layout.miui_radio_info);
        applySystemBarInsets();
        if (!isNightMode(getApplicationContext()) && (window = getWindow()) != null && (decorView = window.getDecorView()) != null) {
            decorView.setSystemUiVisibility(8192);
        }
        log("Started onCreate");
        this.mQueuedWork = new ThreadPoolExecutor(1, 1, 300000L, TimeUnit.MICROSECONDS, new LinkedBlockingDeque());
        this.mConnectivityManager = (ConnectivityManager) getSystemService("connectivity");
        this.mPhone = PhoneFactory.getDefaultPhone();
        this.mTelephonyManager = ((TelephonyManager) getSystemService("phone")).createForSubscriptionId(this.mPhone.getSubId());
        this.mImsManager = ImsManager.getInstance(getApplicationContext(), this.mPhone.getPhoneId());
        if (TelephonyManager.getDefault().isMultiSimEnabled() && getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID).equals("phone1")) {
                this.mPhone = PhoneFactory.getPhone(0);
                this.mSlotId = 0;
                setTitle(((Object) getTitle()) + "1");
            } else {
                this.mPhone = PhoneFactory.getPhone(1);
                this.mSlotId = 1;
                setTitle(((Object) getTitle()) + "2");
            }
        } else {
            this.mPhone = PhoneFactory.getDefaultPhone();
            this.mSlotId = 0;
        }
        sPhoneIndexLabels = getPhoneIndexLabels(this.mTelephonyManager);
        this.mDeviceId = (TextView) findViewById(R.id.imei);
        this.mLine1Number = (TextView) findViewById(R.id.number);
        this.mSubscriptionId = (TextView) findViewById(R.id.subid);
        this.mDds = (TextView) findViewById(R.id.dds);
        this.mSubscriberId = (TextView) findViewById(R.id.imsi);
        this.mCallState = (TextView) findViewById(R.id.call);
        this.mOperatorName = (TextView) findViewById(R.id.operator);
        this.mRoamingState = (TextView) findViewById(R.id.roaming);
        this.mGsmState = (TextView) findViewById(R.id.gsm);
        this.mGprsState = (TextView) findViewById(R.id.gprs);
        this.mVoiceNetwork = (TextView) findViewById(R.id.voice_network);
        this.mDataNetwork = (TextView) findViewById(R.id.data_network);
        this.mOverrideNetwork = (TextView) findViewById(R.id.override_network);
        this.mDBm = (TextView) findViewById(R.id.dbm);
        this.mMwi = (TextView) findViewById(R.id.mwi);
        this.mCfi = (TextView) findViewById(R.id.cfi);
        TextView textView = (TextView) findViewById(R.id.cellinfo);
        this.mCellInfo = textView;
        textView.setTypeface(Typeface.MONOSPACE);
        this.mSent = (TextView) findViewById(R.id.sent);
        this.mReceived = (TextView) findViewById(R.id.received);
        this.mSmsc = (EditText) findViewById(R.id.smsc);
        this.mDnsCheckState = (TextView) findViewById(R.id.dnsCheckState);
        this.mPingHostnameV4 = (TextView) findViewById(R.id.pingHostnameV4);
        this.mPingHostnameV6 = (TextView) findViewById(R.id.pingHostnameV6);
        this.mHttpClientTest = (TextView) findViewById(R.id.httpClientTest);
        this.mEndcAvailable = (TextView) findViewById(R.id.endc_available);
        this.mDcnrRestricted = (TextView) findViewById(R.id.dcnr_restricted);
        this.mNrAvailable = (TextView) findViewById(R.id.nr_available);
        this.mNrState = (TextView) findViewById(R.id.nr_state);
        this.mNrFrequency = (TextView) findViewById(R.id.nr_frequency);
        this.mPhyChanConfig = (TextView) findViewById(R.id.phy_chan_config);
        this.mNetworkSlicingConfig = (TextView) findViewById(R.id.network_slicing_config);
        this.mDsdaState = (TextView) findViewById(R.id.dsda_state);
        this.mIsSupportDsda = isDsdaSupported();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        if (this.mIsSupportDsda) {
            updateDsdaState();
            intentFilter.addAction("org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED");
        } else {
            ((TextView) findViewById(R.id.dsda_state_label)).setVisibility(8);
            this.mDsdaState.setVisibility(8);
        }
        registerReceiver(this.mReceiver, intentFilter, 2);
        if ((this.mTelephonyManager.getSupportedRadioAccessFamily() & 524288) == 0) {
            setNrStatsVisibility(8);
        }
        this.mPreferredNetworkType = (Spinner) findViewById(R.id.preferredNetworkType);
        String[] strArr = PREFERRED_NETWORK_LABELS;
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strArr);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mPreferredNetworkType.setAdapter((SpinnerAdapter) arrayAdapter);
        this.mSelectPhoneIndex = (Spinner) findViewById(R.id.phoneIndex);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sPhoneIndexLabels);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSelectPhoneIndex.setAdapter((SpinnerAdapter) arrayAdapter2);
        this.mCellInfoRefreshRateSpinner = (Spinner) findViewById(R.id.cell_info_rate_select);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CELL_INFO_REFRESH_RATE_LABELS);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mCellInfoRefreshRateSpinner.setAdapter((SpinnerAdapter) arrayAdapter3);
        this.mImsVolteProvisionedSwitch = (Switch) findViewById(R.id.volte_provisioned_switch);
        this.mImsVtProvisionedSwitch = (Switch) findViewById(R.id.vt_provisioned_switch);
        this.mImsWfcProvisionedSwitch = (Switch) findViewById(R.id.wfc_provisioned_switch);
        this.mEabProvisionedSwitch = (Switch) findViewById(R.id.eab_provisioned_switch);
        if (!ImsManager.isImsSupportedOnDevice(this.mPhone.getContext())) {
            this.mImsVolteProvisionedSwitch.setVisibility(8);
            this.mImsVtProvisionedSwitch.setVisibility(8);
            this.mImsWfcProvisionedSwitch.setVisibility(8);
            this.mEabProvisionedSwitch.setVisibility(8);
        }
        Switch r0 = (Switch) findViewById(R.id.cbrs_data_switch);
        this.mCbrsDataSwitch = r0;
        r0.setVisibility(isCbrsSupported() ? 0 : 8);
        Switch r1 = (Switch) findViewById(R.id.dsds_switch);
        this.mDsdsSwitch = r1;
        if (!IS_CUST_SINGLE_SIM) {
            r1.setVisibility(0);
            this.mDsdsSwitch.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.lambda$onCreate$2(view);
                }
            });
            this.mDsdsSwitch.setChecked(isDsdsEnabled());
            this.mDsdsSwitch.setEnabled(isRadioMultisimConfigSS());
        } else {
            r1.setVisibility(8);
        }
        Switch r2 = (Switch) findViewById(R.id.removable_esim_switch);
        this.mRemovableEsimSwitch = r2;
        if (!IS_USER_BUILD) {
            r2.setEnabled(true);
            this.mRemovableEsimSwitch.setChecked(this.mTelephonyManager.isRemovableEsimDefaultEuicc());
            this.mRemovableEsimSwitch.setOnCheckedChangeListener(this.mRemovableEsimChangeListener);
        }
        this.mRadioPowerOnSwitch = (Switch) findViewById(R.id.radio_power);
        this.mDownlinkKbps = (TextView) findViewById(R.id.dl_kbps);
        this.mUplinkKbps = (TextView) findViewById(R.id.ul_kbps);
        updateBandwidths(0, 0);
        Button button = (Button) findViewById(R.id.ping_test);
        this.mPingTestButton = button;
        button.setOnClickListener(this.mPingButtonHandler);
        Button button2 = (Button) findViewById(R.id.update_smsc);
        this.mUpdateSmscButton = button2;
        button2.setEnabled(isSmscAddressEnable(this.mPhone));
        this.mUpdateSmscButton.setOnClickListener(this.mUpdateSmscButtonHandler);
        Button button3 = (Button) findViewById(R.id.refresh_smsc);
        this.mRefreshSmscButton = button3;
        button3.setOnClickListener(this.mRefreshSmscButtonHandler);
        Button button4 = (Button) findViewById(R.id.dns_check_toggle);
        this.mDnsCheckToggleButton = button4;
        button4.setOnClickListener(this.mDnsCheckButtonHandler);
        this.mCarrierProvisioningButton = (Button) findViewById(R.id.carrier_provisioning);
        boolean zIsEmpty = TextUtils.isEmpty(getCarrierProvisioningAppString());
        Button button5 = this.mCarrierProvisioningButton;
        if (!zIsEmpty) {
            button5.setOnClickListener(this.mCarrierProvisioningButtonHandler);
        } else {
            button5.setEnabled(false);
        }
        this.mTriggerCarrierProvisioningButton = (Button) findViewById(R.id.trigger_carrier_provisioning);
        boolean zIsEmpty2 = TextUtils.isEmpty(getCarrierProvisioningAppString());
        Button button6 = this.mTriggerCarrierProvisioningButton;
        if (!zIsEmpty2) {
            button6.setOnClickListener(this.mTriggerCarrierProvisioningButtonHandler);
        } else {
            button6.setEnabled(false);
        }
        Button button7 = (Button) findViewById(R.id.oem_info);
        this.mOemInfoButton = button7;
        button7.setOnClickListener(this.mOemInfoButtonHandler);
        if (getPackageManager().queryIntentActivities(new Intent("com.android.phone.settings.OEM_RADIO_INFO"), 0).size() == 0) {
            this.mOemInfoButton.setEnabled(false);
        }
        this.mCellInfoRefreshRateIndex = 0;
        this.mPreferredNetworkTypeResult = strArr.length - 1;
        this.mSelectedPhoneIndex = this.mSlotId;
        new Thread(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onCreate$4();
            }
        }).start();
        restoreFromBundle(bundle);
        if (MiuiTelephony.IS_QCOM) {
            this.mExtPhoneCallback = new QExtPhoneCallback();
            ExtTelephonyManager extTelephonyManager = ExtTelephonyManager.getInstance(this);
            this.mExtPhone = extTelephonyManager;
            extTelephonyManager.connectService(this.mExtPhoneCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        if (this.mTelephonyManager.doesSwitchMultiSimConfigTriggerReboot()) {
            this.mDsdsSwitch.toggle();
            showDsdsChangeDialog();
        } else {
            performDsdsSwitch();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4() {
        final int preferredNetworkTypeBitmask = (int) this.mTelephonyManager.getPreferredNetworkTypeBitmask();
        runOnUiThread(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onCreate$3(preferredNetworkTypeBitmask);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(int i) {
        updatePreferredNetworkType(RadioAccessFamily.getNetworkTypeFromRaf(i));
    }

    private void applySystemBarInsets() {
        View viewFindViewById = findViewById(R.id.radio_info_root);
        if (viewFindViewById == null) {
            return;
        }
        final int paddingLeft = viewFindViewById.getPaddingLeft();
        final int paddingTop = viewFindViewById.getPaddingTop();
        final int paddingRight = viewFindViewById.getPaddingRight();
        final int paddingBottom = viewFindViewById.getPaddingBottom();
        viewFindViewById.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda6
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return RadioInfo.$r8$lambda$rTS2_v1QGOAvOKPWjK0XKYfHe4I(paddingLeft, paddingTop, paddingRight, paddingBottom, view, windowInsets);
            }
        });
        viewFindViewById.requestApplyInsets();
    }

    public static /* synthetic */ WindowInsets $r8$lambda$rTS2_v1QGOAvOKPWjK0XKYfHe4I(int i, int i2, int i3, int i4, View view, WindowInsets windowInsets) {
        Insets insets = windowInsets.getInsets(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
        view.setPadding(i, Math.max(i2, insets.top), i3, Math.max(i4, insets.bottom));
        return windowInsets;
    }

    public boolean isSmscAddressEnable(Phone phone) {
        boolean booleanForPhoneId;
        if (TelephonyBaseProxy.get().isFeatureResolveEnabled()) {
            booleanForPhoneId = TelephonyBaseProxy.get().getBooleanForPhoneId(phone.getPhoneId(), "disable_smsc_setting_update", false);
        } else {
            booleanForPhoneId = !(phone == null || phone.getIccCard() == null || phone.getIccCard().getIccRecords() == null || !"20820".equals(phone.getIccCard().getIccRecords().getOperatorNumeric())) || TelephonyBaseProxy.get().isCustForMx_Telcel() || TelephonyBaseProxy.get().isCustForHk_H3g() || TelephonyBaseProxy.get().isCustForLm_Claro() || TelephonyBaseProxy.get().isCustForCl_Entel() || TelephonyBaseProxy.get().isCustForEEA_BYG();
        }
        return !booleanForPhoneId;
    }

    private boolean isRadioMultisimConfigSS() {
        String str = SystemProperties.get("persist.radio.multisim.config", "");
        return "ss".equals(str) || "ssss".equals(str);
    }

    private void init5GNetworkType() {
        if (SubscriptionManager.isValidSlotIndex(this.mSlotId)) {
            ContentResolver contentResolver = this.mPhone.getContext().getContentResolver();
            StringBuilder sb = new StringBuilder("5g_icon_group_mode");
            sb.append(this.mSlotId);
            this.mIsShow5GNsaType = Settings.Global.getInt(contentResolver, sb.toString(), 0) == 1;
            ContentResolver contentResolver2 = this.mPhone.getContext().getContentResolver();
            StringBuilder sb2 = new StringBuilder("5g_icon_group_mode");
            sb2.append(this.mSlotId);
            this.mIsShow5GSaType = Settings.Global.getInt(contentResolver2, sb2.toString(), 0) == 2;
        }
    }

    private void register5GNetworkTypeObserver() {
        if (SubscriptionManager.isValidSlotIndex(this.mSlotId)) {
            this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("5g_icon_group_mode" + this.mSlotId), false, this.m5GNetworkTypeObserver);
        }
    }

    private void unregister5GNetworkTypeObserver() {
        if (this.m5GNetworkTypeObserver != null) {
            this.mPhone.getContext().getContentResolver().unregisterContentObserver(this.m5GNetworkTypeObserver);
        }
    }

    @Override // android.app.Activity
    public Intent getParentActivityIntent() {
        if (super.getParentActivityIntent() != null) {
            return null;
        }
        new Intent().setClassName("com.android.settings", "com.android.settings.Settings$TestingSettingsActivity");
        return null;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        int[] subId;
        super.onResume();
        log("Started onResume");
        int i = this.mSlotId;
        if (i >= 0 && i <= sPhoneIndexLabels.length - 1) {
            Phone[] phones = PhoneFactory.getPhones();
            if (phones == null) {
                return;
            }
            int length = phones.length;
            int i2 = this.mSlotId;
            if (length <= i2 || (subId = SubscriptionManager.getSubId(i2)) == null || subId.length < 1) {
                return;
            } else {
                updatePhoneIndex(this.mSlotId, subId[0]);
            }
        }
        updateAllFields();
        updateImei();
    }

    private void updateAllFields() {
        updateMessageWaiting();
        updateCallRedirect();
        updateDataState();
        updateDataStats2();
        updateRadioPowerState();
        updateImsProvisionedState();
        updateProperties();
        updateDnsCheckState();
        init5GNetworkType();
        updateNetworkType();
        updateNrStats(null);
        updateCellInfo(this.mCellInfoResult);
        updateSubscriptionIds();
        this.mPingHostnameV4.setText(this.mPingHostnameResultV4);
        this.mPingHostnameV6.setText(this.mPingHostnameResultV6);
        this.mHttpClientTest.setText(this.mHttpClientTestResult);
        this.mCellInfoRefreshRateSpinner.setOnItemSelectedListener(this.mCellInfoRefreshRateHandler);
        this.mCellInfoRefreshRateSpinner.setSelection(this.mCellInfoRefreshRateIndex);
        this.mTelephonyManager.setCellInfoListRate(CELL_INFO_REFRESH_RATES[this.mCellInfoRefreshRateIndex], this.mPhone.getSubId());
        this.mPreferredNetworkType.setSelection(this.mPreferredNetworkTypeResult, true);
        if (EnterpriseManagerStub.ENTERPRISE_ACTIVATED && PhoneHelperStub.getInstance().isNetworkModesRestriction()) {
            this.mPreferredNetworkType.setEnabled(false);
        } else {
            this.mPreferredNetworkType.setOnItemSelectedListener(this.mPreferredNetworkHandler);
        }
        new Thread(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateAllFields$7();
            }
        }).start();
        this.mSelectPhoneIndex.setSelection(this.mSelectedPhoneIndex, true);
        this.mSelectPhoneIndex.setOnItemSelectedListener(this.mSelectPhoneIndexHandler);
        this.mRadioPowerOnSwitch.setOnCheckedChangeListener(this.mRadioPowerOnChangeListener);
        this.mImsVolteProvisionedSwitch.setOnCheckedChangeListener(this.mImsVolteCheckedChangeListener);
        this.mImsVtProvisionedSwitch.setOnCheckedChangeListener(this.mImsVtCheckedChangeListener);
        this.mImsWfcProvisionedSwitch.setOnCheckedChangeListener(this.mImsWfcCheckedChangeListener);
        this.mEabProvisionedSwitch.setOnCheckedChangeListener(this.mEabCheckedChangeListener);
        if (isCbrsSupported()) {
            this.mCbrsDataSwitch.setChecked(getCbrsDataState());
            this.mCbrsDataSwitch.setOnCheckedChangeListener(this.mCbrsDataSwitchChangeListener);
        }
        unregisterPhoneStateListener();
        registerPhoneStateListener();
        unregister5GNetworkTypeObserver();
        register5GNetworkTypeObserver();
        this.mPhone.registerForPhysicalChannelConfig(this.mHandler, 1007, (Object) null);
        if (!this.mRegistedNetworkCallback) {
            this.mRegistedNetworkCallback = true;
            this.mConnectivityManager.registerNetworkCallback(this.mDefaultNetworkRequest, this.mNetworkCallback, this.mHandler);
        }
        updatePhysicalChannelConfiguration(this.mPhone.getServiceStateTracker().getPhysicalChannelConfigList());
        this.mSmsc.clearFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAllFields$7() {
        final int preferredNetworkTypeBitmask = (int) this.mTelephonyManager.getPreferredNetworkTypeBitmask();
        runOnUiThread(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateAllFields$6(preferredNetworkTypeBitmask);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAllFields$6(int i) {
        updatePreferredNetworkType(RadioAccessFamily.getNetworkTypeFromRaf(i));
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        log("onPause: unregister phone & data intents");
        this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
        this.mTelephonyManager.setCellInfoListRate(Integer.MAX_VALUE, this.mPhone.getSubId());
        if (this.mRegistedNetworkCallback) {
            this.mRegistedNetworkCallback = false;
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        }
        unregister5GNetworkTypeObserver();
    }

    private void restoreFromBundle(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        this.mPingHostnameResultV4 = bundle.getString("mPingHostnameResultV4", "");
        this.mPingHostnameResultV6 = bundle.getString("mPingHostnameResultV6", "");
        this.mHttpClientTestResult = bundle.getString("mHttpClientTestResult", "");
        this.mPingHostnameV4.setText(this.mPingHostnameResultV4);
        this.mPingHostnameV6.setText(this.mPingHostnameResultV6);
        this.mHttpClientTest.setText(this.mHttpClientTestResult);
        this.mPreferredNetworkTypeResult = bundle.getInt("mPreferredNetworkTypeResult", PREFERRED_NETWORK_LABELS.length - 1);
        this.mSelectedPhoneIndex = bundle.getInt("mSelectedPhoneIndex", 0);
        this.mCellInfoRefreshRateIndex = bundle.getInt("mCellInfoRefreshRateIndex", 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("mPingHostnameResultV4", this.mPingHostnameResultV4);
        bundle.putString("mPingHostnameResultV6", this.mPingHostnameResultV6);
        bundle.putString("mHttpClientTestResult", this.mHttpClientTestResult);
        bundle.putInt("mPreferredNetworkTypeResult", this.mPreferredNetworkTypeResult);
        bundle.putInt("mSelectedPhoneIndex", this.mSelectedPhoneIndex);
        bundle.putInt("mCellInfoRefreshRateIndex", this.mCellInfoRefreshRateIndex);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (miui.telephony.TelephonyManager.isCustForKrOps()) {
            menu.add(0, 0, 0, R.string.radio_info_band_mode_label).setOnMenuItemClickListener(this.mSelectBandCallback).setAlphabeticShortcut('b');
        }
        menu.add(1, 1, 0, R.string.radioInfo_menu_viewADN).setOnMenuItemClickListener(this.mViewADNCallback);
        menu.add(1, 2, 0, R.string.radioInfo_menu_viewFDN).setOnMenuItemClickListener(this.mViewFDNCallback);
        menu.add(1, 3, 0, R.string.radioInfo_menu_viewSDN).setOnMenuItemClickListener(this.mViewSDNCallback);
        if (ImsManager.isImsSupportedOnDevice(this.mPhone.getContext())) {
            menu.add(1, 4, 0, R.string.radioInfo_menu_getIMS).setOnMenuItemClickListener(this.mGetImsStatus);
        }
        menu.add(1, 5, 0, R.string.radio_info_data_connection_disable).setOnMenuItemClickListener(this.mToggleData);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemFindItem = menu.findItem(5);
        boolean z = false;
        char c = this.mTelephonyManager.getDataEnabled() ? (char) 2 : (char) 0;
        if (c == 0) {
            menuItemFindItem.setTitle(R.string.radio_info_data_connection_enable);
        } else {
            if (c == 2) {
                menuItemFindItem.setTitle(R.string.radio_info_data_connection_disable);
            }
            menuItemFindItem.setVisible(z);
            return true;
        }
        z = true;
        menuItemFindItem.setVisible(z);
        return true;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        ExtTelephonyManager extTelephonyManager;
        QExtPhoneCallback qExtPhoneCallback;
        super.onDestroy();
        unregisterReceiver(this.mReceiver);
        this.mQueuedWork.shutdown();
        if (!MiuiTelephony.IS_QCOM || (extTelephonyManager = this.mExtPhone) == null || (qExtPhoneCallback = this.mExtPhoneCallback) == null) {
            return;
        }
        extTelephonyManager.disconnectService(qExtPhoneCallback);
    }

    private static String[] getPhoneIndexLabels(TelephonyManager telephonyManager) {
        int phoneCount = telephonyManager.getPhoneCount();
        String[] strArr = new String[phoneCount];
        for (int i = 0; i < phoneCount; i++) {
            strArr[i] = "Phone " + i;
        }
        return strArr;
    }

    private void unregisterPhoneStateListener() {
        this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
        this.mPhone.unregisterForPhysicalChannelConfig(this.mHandler);
        this.mOperatorName.setText("");
        this.mGprsState.setText("");
        this.mDataNetwork.setText("");
        this.mOverrideNetwork.setText("");
        this.mVoiceNetwork.setText("");
        this.mSent.setText("");
        this.mReceived.setText("");
        this.mCallState.setText("");
        this.mMwiValue = false;
        this.mMwi.setText("");
        this.mCfiValue = false;
        this.mCfi.setText("");
        this.mCellInfo.setText("");
        this.mDBm.setText("");
        this.mGsmState.setText("");
        this.mRoamingState.setText("");
        this.mPhyChanConfig.setText("");
    }

    private void registerPhoneStateListener() {
        this.mTelephonyCallback = new RadioInfoTelephonyCallback();
        this.mTelephonyManager.registerTelephonyCallback(new HandlerExecutor(this.mHandler), this.mTelephonyCallback);
    }

    private void setNrStatsVisibility(int i) {
        ((TextView) findViewById(R.id.endc_available_label)).setVisibility(i);
        this.mEndcAvailable.setVisibility(i);
        ((TextView) findViewById(R.id.dcnr_restricted_label)).setVisibility(i);
        this.mDcnrRestricted.setVisibility(i);
        ((TextView) findViewById(R.id.nr_available_label)).setVisibility(i);
        this.mNrAvailable.setVisibility(i);
        ((TextView) findViewById(R.id.nr_state_label)).setVisibility(i);
        this.mNrState.setVisibility(i);
        ((TextView) findViewById(R.id.nr_frequency_label)).setVisibility(i);
        this.mNrFrequency.setVisibility(i);
        ((TextView) findViewById(R.id.network_slicing_config_label)).setVisibility(i);
        this.mNetworkSlicingConfig.setVisibility(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDnsCheckState() {
        this.mDnsCheckState.setText(this.mPhone.isDnsCheckDisabled() ? "0.0.0.0 allowed" : "0.0.0.0 not allowed");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBandwidths(int i, int i2) {
        if (i < 0 || i == Integer.MAX_VALUE) {
            i = -1;
        }
        if (i2 < 0 || i2 == Integer.MAX_VALUE) {
            i2 = -1;
        }
        this.mDownlinkKbps.setText(String.format("%-5d", Integer.valueOf(i)));
        this.mUplinkKbps.setText(String.format("%-5d", Integer.valueOf(i2)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSignalStrength(SignalStrength signalStrength) {
        Resources resources = getResources();
        int dbm = signalStrength.getDbm();
        int asuLevel = signalStrength.getAsuLevel();
        if (-1 == asuLevel) {
            asuLevel = 0;
        }
        this.mDBm.setText(String.valueOf(dbm) + " " + resources.getString(R.string.radioInfo_display_dbm) + "   " + String.valueOf(asuLevel) + " " + resources.getString(R.string.radioInfo_display_asu));
    }

    private String getCellInfoDisplayString(int i) {
        return i != Integer.MAX_VALUE ? Integer.toString(i) : "";
    }

    private String getCellInfoDisplayString(long j) {
        return j != Long.MAX_VALUE ? Long.toString(j) : "";
    }

    private String getConnectionStatusString(CellInfo cellInfo) {
        String str;
        String str2 = "";
        String str3 = cellInfo.isRegistered() ? "R" : "";
        int cellConnectionStatus = cellInfo.getCellConnectionStatus();
        if (cellConnectionStatus == 0) {
            str = "N";
        } else if (cellConnectionStatus == 1) {
            str = "P";
        } else {
            str = cellConnectionStatus != 2 ? "" : "S";
        }
        if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str)) {
            str2 = "+";
        }
        return str3 + str2 + str;
    }

    private String buildCdmaInfoString(CellInfoCdma cellInfoCdma) {
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        CellSignalStrengthCdma cellSignalStrength = cellInfoCdma.getCellSignalStrength();
        return String.format("%-3.3s %-5.5s %-5.5s %-5.5s %-6.6s %-6.6s %-6.6s %-6.6s %-5.5s", getConnectionStatusString(cellInfoCdma), getCellInfoDisplayString(cellIdentity.getSystemId()), getCellInfoDisplayString(cellIdentity.getNetworkId()), getCellInfoDisplayString(cellIdentity.getBasestationId()), getCellInfoDisplayString(cellSignalStrength.getCdmaDbm()), getCellInfoDisplayString(cellSignalStrength.getCdmaEcio()), getCellInfoDisplayString(cellSignalStrength.getEvdoDbm()), getCellInfoDisplayString(cellSignalStrength.getEvdoEcio()), getCellInfoDisplayString(cellSignalStrength.getEvdoSnr()));
    }

    private String buildGsmInfoString(CellInfoGsm cellInfoGsm) {
        CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
        return String.format("%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-6.6s %-4.4s %-4.4s\n", getConnectionStatusString(cellInfoGsm), getCellInfoDisplayString(cellIdentity.getMcc()), getCellInfoDisplayString(cellIdentity.getMnc()), getCellInfoDisplayString(cellIdentity.getLac()), getCellInfoDisplayString(cellIdentity.getCid()), getCellInfoDisplayString(cellIdentity.getArfcn()), getCellInfoDisplayString(cellIdentity.getBsic()), getCellInfoDisplayString(cellInfoGsm.getCellSignalStrength().getDbm()));
    }

    private String buildLteInfoString(CellInfoLte cellInfoLte) {
        CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
        CellSignalStrengthLte cellSignalStrength = cellInfoLte.getCellSignalStrength();
        return String.format("%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-3.3s %-6.6s %-2.2s %-4.4s %-4.4s %-2.2s\n", getConnectionStatusString(cellInfoLte), getCellInfoDisplayString(cellIdentity.getMcc()), getCellInfoDisplayString(cellIdentity.getMnc()), getCellInfoDisplayString(cellIdentity.getTac()), getCellInfoDisplayString(cellIdentity.getCi()), getCellInfoDisplayString(cellIdentity.getPci()), getCellInfoDisplayString(cellIdentity.getEarfcn()), getCellInfoDisplayString(cellIdentity.getBandwidth()), getCellInfoDisplayString(cellSignalStrength.getDbm()), getCellInfoDisplayString(cellSignalStrength.getRsrq()), getCellInfoDisplayString(cellSignalStrength.getTimingAdvance()));
    }

    private String buildNrInfoString(CellInfoNr cellInfoNr) {
        CellIdentityNr cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();
        CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellInfoNr.getCellSignalStrength();
        return String.format("%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-3.3s %-6.6s %-4.4s %-4.4s\n", getConnectionStatusString(cellInfoNr), cellIdentityNr.getMccString(), cellIdentityNr.getMncString(), getCellInfoDisplayString(cellIdentityNr.getTac()), getCellInfoDisplayString(cellIdentityNr.getNci()), getCellInfoDisplayString(cellIdentityNr.getPci()), getCellInfoDisplayString(cellIdentityNr.getNrarfcn()), getCellInfoDisplayString(cellSignalStrengthNr.getSsRsrp()), getCellInfoDisplayString(cellSignalStrengthNr.getSsRsrq()));
    }

    private String buildWcdmaInfoString(CellInfoWcdma cellInfoWcdma) {
        CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
        return String.format("%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-6.6s %-3.3s %-4.4s\n", getConnectionStatusString(cellInfoWcdma), getCellInfoDisplayString(cellIdentity.getMcc()), getCellInfoDisplayString(cellIdentity.getMnc()), getCellInfoDisplayString(cellIdentity.getLac()), getCellInfoDisplayString(cellIdentity.getCid()), getCellInfoDisplayString(cellIdentity.getUarfcn()), getCellInfoDisplayString(cellIdentity.getPsc()), getCellInfoDisplayString(cellInfoWcdma.getCellSignalStrength().getDbm()));
    }

    private String buildCellInfoString(List list) {
        String str = new String();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        StringBuilder sb5 = new StringBuilder();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                CellInfo cellInfo = (CellInfo) it.next();
                if (cellInfo instanceof CellInfoLte) {
                    sb3.append(buildLteInfoString((CellInfoLte) cellInfo));
                } else if (cellInfo instanceof CellInfoWcdma) {
                    sb4.append(buildWcdmaInfoString((CellInfoWcdma) cellInfo));
                } else if (cellInfo instanceof CellInfoGsm) {
                    sb2.append(buildGsmInfoString((CellInfoGsm) cellInfo));
                } else if (cellInfo instanceof CellInfoCdma) {
                    sb.append(buildCdmaInfoString((CellInfoCdma) cellInfo));
                } else if (cellInfo instanceof CellInfoNr) {
                    sb5.append(buildNrInfoString((CellInfoNr) cellInfo));
                }
            }
            if (sb5.length() != 0) {
                str = str.concat(String.format("NR\n%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-3.3s %-6.6s %-4.4s %-4.4s\n", "SRV", "MCC", "MNC", "TAC", "NCI", "PCI", "NRARFCN", "SS-RSRP", "SS-RSRQ")).concat(sb5.toString());
            }
            if (sb3.length() != 0) {
                str = str.concat(String.format("LTE\n%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-3.3s %-6.6s %-2.2s %-4.4s %-4.4s %-2.2s\n", "SRV", "MCC", "MNC", "TAC", "CID", "PCI", "EARFCN", "BW", "RSRP", "RSRQ", "TA")).concat(sb3.toString());
            }
            if (sb4.length() != 0) {
                str = str.concat(String.format("WCDMA\n%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-6.6s %-3.3s %-4.4s\n", "SRV", "MCC", "MNC", "LAC", "CID", "UARFCN", "PSC", "RSCP")).concat(sb4.toString());
            }
            if (sb2.length() != 0) {
                str = str.concat(String.format("GSM\n%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-6.6s %-4.4s %-4.4s\n", "SRV", "MCC", "MNC", "LAC", "CID", "ARFCN", "BSIC", "RSSI")).concat(sb2.toString());
            }
            if (sb.length() != 0) {
                str = str.concat(String.format("CDMA/EVDO\n%-3.3s %-5.5s %-5.5s %-5.5s %-6.6s %-6.6s %-6.6s %-6.6s %-5.5s\n", "SRV", "SID", "NID", "BSID", "C-RSSI", "C-ECIO", "E-RSSI", "E-ECIO", "E-SNR")).concat(sb.toString());
            }
        } else {
            str = "unknown";
        }
        return str.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCellInfo(List list) {
        this.mCellInfo.setText(buildCellInfoString(list));
    }

    private void updateSubscriptionIds() {
        this.mSubscriptionId.setText(Integer.toString(this.mPhone.getSubId()));
        this.mDds.setText(Integer.toString(SubscriptionManager.getDefaultDataSubscriptionId()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMessageWaiting() {
        this.mMwi.setText(String.valueOf(this.mMwiValue));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCallRedirect() {
        this.mCfi.setText(String.valueOf(this.mCfiValue));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateServiceState(ServiceState serviceState) {
        int state = serviceState.getState();
        Resources resources = getResources();
        String string = resources.getString(R.string.radioInfo_unknown);
        if (state == 0) {
            string = resources.getString(R.string.radioInfo_service_in);
        } else if (state == 1) {
            string = resources.getString(R.string.radioInfo_service_out);
        } else if (state == 2) {
            string = resources.getString(R.string.radioInfo_service_emergency);
        } else if (state == 3) {
            string = resources.getString(R.string.radioInfo_service_off);
        }
        this.mGsmState.setText(string);
        boolean roaming = serviceState.getRoaming();
        TextView textView = this.mRoamingState;
        if (roaming) {
            textView.setText(R.string.radioInfo_roaming_in);
        } else {
            textView.setText(R.string.radioInfo_roaming_not);
        }
        this.mOperatorName.setText(serviceState.getOperatorAlphaLong());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePhoneState(int i) {
        Resources resources = getResources();
        String string = resources.getString(R.string.radioInfo_unknown);
        if (i == 0) {
            string = resources.getString(R.string.radioInfo_phone_idle);
        } else if (i == 1) {
            string = resources.getString(R.string.radioInfo_phone_ringing);
        } else if (i == 2) {
            string = resources.getString(R.string.radioInfo_phone_offhook);
        }
        this.mCallState.setText(string);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDataState() {
        char c = this.mTelephonyManager.getDataEnabled() ? (char) 2 : (char) 0;
        Resources resources = getResources();
        String string = resources.getString(R.string.radioInfo_unknown);
        if (c == 0) {
            string = resources.getString(R.string.radioInfo_data_disconnected);
        } else if (c == 1) {
            string = resources.getString(R.string.radioInfo_data_connecting);
        } else if (c == 2) {
            string = resources.getString(R.string.radioInfo_data_connected);
        }
        this.mGprsState.setText(string);
    }

    private boolean isDsdaSupported() {
        if (isCnCustimizationTestBuild()) {
            return false;
        }
        if (IS_INTERNATIONAL_BUILD) {
            return TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform8650() || TelephonyManagerEx.getDefault().isPlatform8635() || TelephonyManagerEx.getDefault().isPlatform8735() || TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform7635();
        }
        Resources resources = getResources();
        int identifier = resources.getIdentifier("config_is_dsda_supported", "bool", "android.miui");
        boolean z = identifier != 0 ? resources.getBoolean(identifier) : false;
        log("isDsdaSupported :" + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDsdaState() {
        boolean zIsConcurrentCallsPossible = TelephonyManagerEx.getDefault().isConcurrentCallsPossible();
        TextView textView = this.mDsdaState;
        if (textView != null) {
            textView.setText(String.valueOf(zIsConcurrentCallsPossible));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkType() {
        if (this.mPhone != null) {
            Log.d("RadioInfo", "updateNetworkType mIsShow5GNsaType: " + this.mIsShow5GNsaType + ", mIsShow5GSaType: " + this.mIsShow5GSaType + ", mSlotId: " + this.mSlotId);
            if (this.mIsShow5GNsaType) {
                this.mDataNetwork.setText("NR_NSA");
                this.mVoiceNetwork.setText("NR_NSA");
            } else if (this.mIsShow5GSaType) {
                this.mDataNetwork.setText("NR_SA");
                this.mVoiceNetwork.setText("NR_SA");
            } else {
                this.mPhone.getServiceState();
                this.mDataNetwork.setText(ServiceState.rilRadioTechnologyToString(this.mPhone.getServiceState().getRilDataRadioTechnology()));
                this.mVoiceNetwork.setText(ServiceState.rilRadioTechnologyToString(this.mPhone.getServiceState().getRilVoiceRadioTechnology()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNrStats(ServiceState serviceState) {
        DataSpecificRegistrationInfo dataSpecificInfo;
        Phone phone;
        if ((this.mTelephonyManager.getSupportedRadioAccessFamily() & 524288) == 0) {
            return;
        }
        if (serviceState == null && (phone = this.mPhone) != null) {
            serviceState = phone.getServiceState();
        }
        if (serviceState != null) {
            NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
            if (networkRegistrationInfo != null && (dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo()) != null) {
                this.mEndcAvailable.setText(String.valueOf(dataSpecificInfo.isEnDcAvailable));
                this.mDcnrRestricted.setText(String.valueOf(dataSpecificInfo.isDcNrRestricted));
                this.mNrAvailable.setText(String.valueOf(dataSpecificInfo.isNrAvailable));
            }
            this.mNrState.setText(NetworkRegistrationInfo.nrStateToString(serviceState.getNrState()));
            this.mNrFrequency.setText(ServiceState.frequencyRangeToString(serviceState.getNrFrequencyRange()));
        }
        new Thread(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateNrStats$10();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateNrStats$10() {
        final CompletableFuture completableFuture = new CompletableFuture();
        this.mTelephonyManager.getNetworkSlicingConfiguration(new ProfileInstallReceiver$$ExternalSyntheticLambda0(), new OutcomeReceiver() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda11
            @Override // android.os.OutcomeReceiver
            public final void onResult(Object obj) {
                completableFuture.complete((NetworkSlicingConfig) obj);
            }
        });
        try {
            final NetworkSlicingConfig networkSlicingConfig = (NetworkSlicingConfig) completableFuture.get(3000L, TimeUnit.MILLISECONDS);
            this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateNrStats$8(networkSlicingConfig);
                }
            });
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("RadioInfo", "Unable to get slicing config: " + e.toString());
            this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.RadioInfo$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateNrStats$9();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateNrStats$8(NetworkSlicingConfig networkSlicingConfig) {
        this.mNetworkSlicingConfig.setText(networkSlicingConfig.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateNrStats$9() {
        this.mNetworkSlicingConfig.setText("Unable to get slicing config.");
    }

    private void updateProperties() {
        Resources resources = getResources();
        String deviceId = this.mPhone.getDeviceId();
        if (deviceId == null) {
            deviceId = resources.getString(R.string.radioInfo_unknown);
        }
        this.mDeviceId.setText(deviceId);
        String subscriberId = this.mPhone.getSubscriberId();
        if (subscriberId == null) {
            subscriberId = resources.getString(R.string.radioInfo_unknown);
        }
        this.mSubscriberId.setText(subscriberId);
        SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(SubscriptionManager.class);
        int subId = this.mPhone.getSubId();
        this.mLine1Number.setText(subscriptionManager.getPhoneNumber(subId) + " { CARRIER:" + subscriptionManager.getPhoneNumber(subId, 2) + ", UICC:" + subscriptionManager.getPhoneNumber(subId, 1) + ", IMS:" + subscriptionManager.getPhoneNumber(subId, 3) + " }");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDataStats2() {
        Resources resources = getResources();
        long mobileTxPackets = TrafficStats.getMobileTxPackets();
        long mobileRxPackets = TrafficStats.getMobileRxPackets();
        long mobileTxBytes = TrafficStats.getMobileTxBytes();
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        String string = resources.getString(R.string.radioInfo_display_packets);
        String string2 = resources.getString(R.string.radioInfo_display_bytes);
        this.mSent.setText(mobileTxPackets + " " + string + ", " + mobileTxBytes + " " + string2);
        this.mReceived.setText(mobileRxPackets + " " + string + ", " + mobileRxBytes + " " + string2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pingHostname() {
        try {
            try {
                int iWaitFor = Runtime.getRuntime().exec("ping -c 1 www.google.com").waitFor();
                if (iWaitFor == 0) {
                    this.mPingHostnameResultV4 = "Pass";
                } else {
                    this.mPingHostnameResultV4 = String.format("Fail(%d)", Integer.valueOf(iWaitFor));
                }
            } catch (IOException unused) {
                this.mPingHostnameResultV4 = "Fail: IOException";
            }
            try {
                int iWaitFor2 = Runtime.getRuntime().exec("ping6 -c 1 www.google.com").waitFor();
                if (iWaitFor2 == 0) {
                    this.mPingHostnameResultV6 = "Pass";
                } else {
                    this.mPingHostnameResultV6 = String.format("Fail(%d)", Integer.valueOf(iWaitFor2));
                }
            } catch (IOException unused2) {
                this.mPingHostnameResultV6 = "Fail: IOException";
            }
        } catch (InterruptedException unused3) {
            this.mPingHostnameResultV6 = "Fail: InterruptedException";
            this.mPingHostnameResultV4 = "Fail: InterruptedException";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void httpClientTest() throws Throwable {
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL("https://www.google.com").openConnection();
                try {
                    if (httpURLConnection2.getResponseCode() == 200) {
                        this.mHttpClientTestResult = "Pass";
                    } else {
                        this.mHttpClientTestResult = "Fail: Code: " + httpURLConnection2.getResponseMessage();
                    }
                    httpURLConnection2.disconnect();
                } catch (IOException unused) {
                    httpURLConnection = httpURLConnection2;
                    this.mHttpClientTestResult = "Fail: IOException";
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                } catch (Throwable th) {
                    th = th;
                    httpURLConnection = httpURLConnection2;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            } catch (IOException unused2) {
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSmsc() {
        this.mQueuedWork.execute(new Runnable() { // from class: com.android.phone.settings.RadioInfo.5
            @Override // java.lang.Runnable
            public void run() {
                RadioInfo.this.mPhone.getSmscAddress(RadioInfo.this.mHandler.obtainMessage(1005));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAllCellInfo() {
        this.mCellInfo.setText("");
        final Runnable runnable = new Runnable() { // from class: com.android.phone.settings.RadioInfo.6
            @Override // java.lang.Runnable
            public void run() {
                RadioInfo radioInfo = RadioInfo.this;
                radioInfo.updateCellInfo(radioInfo.mCellInfoResult);
            }
        };
        this.mQueuedWork.execute(new Runnable() { // from class: com.android.phone.settings.RadioInfo.7
            @Override // java.lang.Runnable
            public void run() {
                RadioInfo radioInfo = RadioInfo.this;
                radioInfo.mCellInfoResult = radioInfo.mTelephonyManager.getAllCellInfo();
                RadioInfo.this.mHandler.post(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePingState() {
        this.mPingHostnameResultV4 = getResources().getString(R.string.radioInfo_unknown);
        this.mPingHostnameResultV6 = getResources().getString(R.string.radioInfo_unknown);
        this.mHttpClientTestResult = getResources().getString(R.string.radioInfo_unknown);
        this.mPingHostnameV4.setText(this.mPingHostnameResultV4);
        this.mPingHostnameV6.setText(this.mPingHostnameResultV6);
        this.mHttpClientTest.setText(this.mHttpClientTestResult);
        final Runnable runnable = new Runnable() { // from class: com.android.phone.settings.RadioInfo.8
            @Override // java.lang.Runnable
            public void run() {
                RadioInfo.this.mPingHostnameV4.setText(RadioInfo.this.mPingHostnameResultV4);
                RadioInfo.this.mPingHostnameV6.setText(RadioInfo.this.mPingHostnameResultV6);
                RadioInfo.this.mHttpClientTest.setText(RadioInfo.this.mHttpClientTestResult);
            }
        };
        new Thread() { // from class: com.android.phone.settings.RadioInfo.9
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                RadioInfo.this.pingHostname();
                RadioInfo.this.mHandler.post(runnable);
            }
        }.start();
        new Thread() { // from class: com.android.phone.settings.RadioInfo.10
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws Throwable {
                RadioInfo.this.httpClientTest();
                RadioInfo.this.mHandler.post(runnable);
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRadioOn() {
        return this.mPhone.getServiceState().getState() != 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRadioPowerState() {
        this.mRadioPowerOnSwitch.setOnCheckedChangeListener(null);
        this.mRadioPowerOnSwitch.setChecked(isRadioOn());
        this.mRadioPowerOnSwitch.setEnabled(!isAirplaneModeOn());
        this.mRadioPowerOnSwitch.setOnCheckedChangeListener(this.mRadioPowerOnChangeListener);
    }

    private boolean isAirplaneModeOn() {
        return Settings.Global.getInt(getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    void setImsVolteProvisionedState(boolean z) {
        Log.d("RadioInfo", "setImsVolteProvisioned state: ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        setImsConfigProvisionedState(10, z);
    }

    void setImsVtProvisionedState(boolean z) {
        Log.d("RadioInfo", "setImsVtProvisioned() state: ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        setImsConfigProvisionedState(11, z);
    }

    void setImsWfcProvisionedState(boolean z) {
        Log.d("RadioInfo", "setImsWfcProvisioned() state: ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        setImsConfigProvisionedState(28, z);
    }

    void setEabProvisionedState(boolean z) {
        Log.d("RadioInfo", "setEabProvisioned() state: ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        setImsConfigProvisionedState(25, z);
    }

    void setImsConfigProvisionedState(final int i, final boolean z) {
        if (this.mPhone == null || this.mImsManager == null) {
            return;
        }
        this.mQueuedWork.execute(new Runnable() { // from class: com.android.phone.settings.RadioInfo.17
            @Override // java.lang.Runnable
            public void run() {
                try {
                    RadioInfo.this.mImsManager.getConfigInterface().setProvisionedValue(i, z ? 1 : 0);
                } catch (ImsException e) {
                    Log.e("RadioInfo", "setImsConfigProvisioned() exception:", e);
                }
            }
        });
    }

    private boolean isCnCustimizationTestBuild() {
        String str = SystemProperties.get("ro.cust.test");
        return "ct".equals(str) || "cu".equals(str) || "cm".equals(str);
    }

    private boolean isImsVolteProvisioned() {
        ImsManager imsManager = this.mImsManager;
        return imsManager != null && imsManager.isVolteEnabledByPlatform() && this.mImsManager.isVolteProvisionedOnDevice();
    }

    private boolean isImsVtProvisioned() {
        ImsManager imsManager = this.mImsManager;
        return imsManager != null && imsManager.isVtEnabledByPlatform() && this.mImsManager.isVtProvisionedOnDevice();
    }

    private boolean isImsWfcProvisioned() {
        ImsManager imsManager = this.mImsManager;
        return imsManager != null && imsManager.isWfcEnabledByPlatform() && this.mImsManager.isWfcProvisionedOnDevice();
    }

    private boolean isEabProvisioned() {
        return isFeatureProvisioned(25, false);
    }

    private boolean isFeatureProvisioned(int i, boolean z) {
        ImsManager imsManager = this.mImsManager;
        if (imsManager != null) {
            try {
                ImsConfig configInterface = imsManager.getConfigInterface();
                if (configInterface != null) {
                    z = true;
                    if (configInterface.getProvisionedValue(i) != 1) {
                        z = false;
                    }
                }
            } catch (ImsException e) {
                Log.e("RadioInfo", "isFeatureProvisioned() exception:", e);
            }
        }
        log("isFeatureProvisioned() featureId=" + i + " provisioned=" + z);
        return z;
    }

    private boolean isEabEnabledByPlatform() {
        PersistableBundle configForSubId;
        Phone phone = this.mPhone;
        if (phone == null || (configForSubId = ((CarrierConfigManager) phone.getContext().getSystemService("carrier_config")).getConfigForSubId(this.mPhone.getSubId())) == null) {
            return false;
        }
        return configForSubId.getBoolean("use_rcs_presence_bool", false) || configForSubId.getBoolean("ims.enable_presence_capability_exchange_bool", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImsProvisionedState() {
        if (ImsManager.isImsSupportedOnDevice(this.mPhone.getContext())) {
            log("updateImsProvisionedState isImsVolteProvisioned()=" + isImsVolteProvisioned());
            this.mImsVolteProvisionedSwitch.setOnCheckedChangeListener(null);
            this.mImsVolteProvisionedSwitch.setChecked(isImsVolteProvisioned());
            this.mImsVolteProvisionedSwitch.setOnCheckedChangeListener(this.mImsVolteCheckedChangeListener);
            Switch r0 = this.mImsVolteProvisionedSwitch;
            boolean z = IS_USER_BUILD;
            boolean z2 = false;
            r0.setEnabled(!z && this.mImsManager.isVolteEnabledByPlatform());
            this.mImsVtProvisionedSwitch.setOnCheckedChangeListener(null);
            this.mImsVtProvisionedSwitch.setChecked(isImsVtProvisioned());
            this.mImsVtProvisionedSwitch.setOnCheckedChangeListener(this.mImsVtCheckedChangeListener);
            this.mImsVtProvisionedSwitch.setEnabled(!z && this.mImsManager.isVtEnabledByPlatform());
            this.mImsWfcProvisionedSwitch.setOnCheckedChangeListener(null);
            this.mImsWfcProvisionedSwitch.setChecked(isImsWfcProvisioned());
            this.mImsWfcProvisionedSwitch.setOnCheckedChangeListener(this.mImsWfcCheckedChangeListener);
            this.mImsWfcProvisionedSwitch.setEnabled(!z && this.mImsManager.isWfcEnabledByPlatform());
            this.mEabProvisionedSwitch.setOnCheckedChangeListener(null);
            this.mEabProvisionedSwitch.setChecked(isEabProvisioned());
            this.mEabProvisionedSwitch.setOnCheckedChangeListener(this.mEabCheckedChangeListener);
            Switch r1 = this.mEabProvisionedSwitch;
            if (!z && isEabEnabledByPlatform()) {
                z2 = true;
            }
            r1.setEnabled(z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11(View view) {
        String carrierProvisioningAppString = getCarrierProvisioningAppString();
        if (TextUtils.isEmpty(carrierProvisioningAppString)) {
            return;
        }
        Intent intent = new Intent("com.android.phone.settings.CARRIER_PROVISIONING");
        intent.setComponent(ComponentName.unflattenFromString(carrierProvisioningAppString));
        sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$12(View view) {
        String carrierProvisioningAppString = getCarrierProvisioningAppString();
        if (TextUtils.isEmpty(carrierProvisioningAppString)) {
            return;
        }
        Intent intent = new Intent("com.android.phone.settings.TRIGGER_CARRIER_PROVISIONING");
        intent.setComponent(ComponentName.unflattenFromString(carrierProvisioningAppString));
        sendBroadcast(intent);
    }

    /* JADX INFO: renamed from: com.android.phone.settings.RadioInfo$28, reason: invalid class name */
    class AnonymousClass28 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        AnonymousClass28() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            if (RadioInfo.this.mPreferredNetworkTypeResult == i || i < 0 || i > RadioInfo.PREFERRED_NETWORK_LABELS.length - 2) {
                return;
            }
            RadioInfo.this.mPreferredNetworkTypeResult = i;
            new Thread(new Runnable() { // from class: com.android.phone.settings.RadioInfo$28$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onItemSelected$0();
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemSelected$0() {
            RadioInfo.this.mTelephonyManager.setAllowedNetworkTypesForReason(0, RadioAccessFamily.getRafFromNetworkType(RadioInfo.this.mPreferredNetworkTypeResult));
        }
    }

    private String getCarrierProvisioningAppString() {
        PersistableBundle configForSubId;
        Phone phone = this.mPhone;
        return (phone == null || (configForSubId = ((CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class)).getConfigForSubId(this.mPhone.getSubId())) == null) ? "" : configForSubId.getString("carrier_provisioning_app_string", "");
    }

    boolean isCbrsSupported() {
        return getResources().getBoolean(android.R.bool.config_cecHdmiCecControlEnabled_allowed);
    }

    void updateCbrsDataState(boolean z) {
        Log.d("RadioInfo", "setCbrsDataSwitchState() state:".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        if (this.mTelephonyManager != null) {
            this.mQueuedWork.execute(new AnonymousClass31(z));
        }
    }

    /* JADX INFO: renamed from: com.android.phone.settings.RadioInfo$31, reason: invalid class name */
    class AnonymousClass31 implements Runnable {
        final /* synthetic */ boolean val$state;

        AnonymousClass31(boolean z) {
            this.val$state = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            RadioInfo.this.mTelephonyManager.setOpportunisticNetworkState(this.val$state);
            RadioInfo.this.mHandler.post(new Runnable() { // from class: com.android.phone.settings.RadioInfo$31$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$run$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            RadioInfo.this.mCbrsDataSwitch.setChecked(RadioInfo.this.getCbrsDataState());
        }
    }

    boolean getCbrsDataState() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        boolean zIsOpportunisticNetworkEnabled = telephonyManager != null ? telephonyManager.isOpportunisticNetworkEnabled() : false;
        Log.d("RadioInfo", "getCbrsDataState() state:".concat(zIsOpportunisticNetworkEnabled ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        return zIsOpportunisticNetworkEnabled;
    }

    private void showDsdsChangeDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.dsds_dialog_title).setMessage(R.string.dsds_dialog_message).setPositiveButton(R.string.dsds_dialog_confirm, this.mOnDsdsDialogConfirmedListener).setNegativeButton(R.string.dsds_dialog_cancel, this.mOnDsdsDialogConfirmedListener).create().show();
    }

    private static boolean isDsdsEnabled() {
        return TelephonyManager.getDefault().getPhoneCount() > 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performDsdsSwitch() {
        this.mTelephonyManager.switchMultiSimConfig(this.mDsdsSwitch.isChecked() ? 2 : 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRemovableEsimAsDefaultEuicc(boolean z) {
        Log.d("RadioInfo", "setRemovableEsimAsDefaultEuicc isChecked: " + z);
        this.mTelephonyManager.setRemovableEsimAsDefaultEuicc(z);
        ComponentInfo componentInfoFindBestComponent = EuiccConnector.findBestComponent(getPackageManager());
        if (componentInfoFindBestComponent == null) {
            Log.d("RadioInfo", "setRemovableEsimAsDefaultEuicc: unable to find suitable component info");
            return;
        }
        Intent intent = new Intent("android.telephony.euicc.action.REMOVABLE_ESIM_AS_DEFAULT");
        intent.setPackage(componentInfoFindBestComponent.packageName);
        intent.putExtra("isDefault", z);
        sendBroadcast(intent);
    }

    public boolean isNightMode(Context context) {
        Configuration configuration;
        return (context == null || (configuration = context.getResources().getConfiguration()) == null || (configuration.uiMode & 48) != 32) ? false : true;
    }

    class QExtPhoneCallback implements ServiceCallback {
        private QExtPhoneCallback() {
        }

        public void onConnected() {
            Log.d("RadioInfo", "ExtTelephony Service connected");
            RadioInfo.this.mIsExtPhoneConnected = true;
            RadioInfo.this.updateImei();
        }

        public void onDisconnected() {
            Log.d("RadioInfo", "ExtTelephony Service disconnected...");
            RadioInfo.this.mIsExtPhoneConnected = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImei() {
        Phone phone;
        if (this.mIsExtPhoneConnected) {
            int phoneId = SubscriptionManager.getPhoneId(this.mPhone.getSubId());
            QtiImeiInfo[] imeiInfo = this.mExtPhone.getImeiInfo();
            String imei = null;
            if (imeiInfo != null) {
                for (int i = 0; i < imeiInfo.length; i++) {
                    QtiImeiInfo qtiImeiInfo = imeiInfo[i];
                    if (qtiImeiInfo != null && qtiImeiInfo.getSlotId() == phoneId) {
                        imei = imeiInfo[i].getImei();
                        Rlog.pii("RadioInfo", "getImei: " + imei + " on slot" + phoneId);
                    }
                }
            }
            if (TextUtils.isEmpty(imei) && (phone = this.mPhone) != null) {
                imei = phone.getImei();
                Rlog.pii("RadioInfo", "phone getImei on slot " + phoneId + ": " + imei);
            }
            boolean zIsEmpty = TextUtils.isEmpty(imei);
            TextView textView = this.mDeviceId;
            if (zIsEmpty) {
                textView.setText(R.string.radioInfo_unknown);
            } else {
                textView.setText(imei);
            }
        }
    }
}
