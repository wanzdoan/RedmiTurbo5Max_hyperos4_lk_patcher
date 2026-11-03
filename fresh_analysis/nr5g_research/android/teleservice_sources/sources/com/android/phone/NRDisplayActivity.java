package com.android.phone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.DataSpecificRegistrationInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.view.WindowInsetsCompat;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.qti.extphone.BearerAllocationStatus;
import com.qti.extphone.Client;
import com.qti.extphone.DcParam;
import com.qti.extphone.ExtPhoneCallbackBase;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.IExtPhoneCallback;
import com.qti.extphone.NrConfigType;
import com.qti.extphone.NrIconType;
import com.qti.extphone.ServiceCallback;
import com.qti.extphone.SignalStrength;
import com.qti.extphone.Status;
import com.qti.extphone.Token;
import com.qti.extphone.UpperLayerIndInfo;
import java.util.Iterator;
import java.util.List;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NRDisplayActivity extends Activity {
    private static final String TAG = "NRDisplayActivity";
    private Client mClient;
    private TextView mConfigType;
    private Context mContext;
    private TextView mDataConnection;
    private TextView mEndcAvailable;
    private TextView mEndcStatus;
    private ExtTelephonyManager mExtTelephonyManager;
    private TextView mIconType;
    private boolean mIsBound;
    private TextView mNrAvailStatus;
    private TextView mRestrictDcnr;
    private TextView mSignalStrength;
    private TextView mStatus;
    private int mDdsPhoneId = 0;
    private TelephonyCallBack mTelephonyCallBack = null;
    private Handler mMainThreadHandler = new Handler() { // from class: com.android.phone.NRDisplayActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String str;
            switch (message.what) {
                case 1:
                    Boolean bool = (Boolean) message.obj;
                    Log.d(NRDisplayActivity.TAG, "EVENT_ON_5G_STATUS_IND: slotId = " + message.arg1 + ". onEnable = " + bool);
                    break;
                case 2:
                    NRDisplayActivity.log("5G_SIGNAL_STRENGTH: slotId = " + message.arg1 + ",signaStrength=" + ((SignalStrength) message.obj));
                    break;
                case 3:
                    Log.d(NRDisplayActivity.TAG, "EVENT NR_DUAL_CONNECTIVITY PARAM : slotId = " + message.arg1);
                    break;
                case 4:
                    Log.d(NRDisplayActivity.TAG, "BEARER_ALLOCATION_STATUS: slotId=" + message.arg1 + "allocated = " + message.obj);
                    break;
                case 5:
                    UpperLayerIndInfo upperLayerIndInfo = (UpperLayerIndInfo) message.obj;
                    Log.d(NRDisplayActivity.TAG, "UPPER_LAYER = " + message.arg1 + "UpperLayerIndInfo = " + upperLayerIndInfo);
                    break;
                case 6:
                    NRDisplayActivity.log("5G_CONFIG_INFO: slotId = " + message.arg1 + "nrConfigType = " + ((NrConfigType) message.obj));
                    break;
                case 7:
                    NrIconType nrIconType = (NrIconType) message.obj;
                    NRDisplayActivity.log("NR_ICON_TYPE: slotId = " + message.arg1 + "icontype = " + nrIconType);
                    if (nrIconType.get() == 0) {
                        str = "NONE";
                    } else if (nrIconType.get() == 1) {
                        str = "BASIC";
                    } else if (nrIconType.get() == 2) {
                        str = "UWB";
                    } else {
                        str = "INVALID";
                    }
                    NRDisplayActivity.this.mIconType.setText(str);
                    break;
                case 8:
                    boolean zBooleanValue = ((Boolean) message.obj).booleanValue();
                    Log.d(NRDisplayActivity.TAG, "ENDC_STATUS = " + message.arg1 + "endcstatus = " + zBooleanValue);
                    NRDisplayActivity.this.mEndcStatus.setText(zBooleanValue ? "ENABLED" : "DISABLED");
                    break;
                case 9:
                    Log.d(NRDisplayActivity.TAG, "signal strength changed");
                    NRDisplayActivity.this.updateNrSignalStrength((android.telephony.SignalStrength) message.obj);
                    break;
                case 10:
                    NRDisplayActivity.this.updateNrState(null);
                    break;
                case 11:
                    Log.d(NRDisplayActivity.TAG, "physical channel config changed");
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (asyncResult.exception == null) {
                        NRDisplayActivity.this.updateNrStateFromPhysicalChannelConfigs((List) asyncResult.result);
                    }
                    break;
            }
        }
    };
    private ServiceCallback mNrServiceConnection = new ServiceCallback() { // from class: com.android.phone.NRDisplayActivity.2
        public void onConnected() {
            NRDisplayActivity.log("nr-onServiceConnected.");
            try {
                NRDisplayActivity nRDisplayActivity = NRDisplayActivity.this;
                nRDisplayActivity.mClient = nRDisplayActivity.mExtTelephonyManager.registerCallback(NRDisplayActivity.this.mContext.getPackageName(), NRDisplayActivity.this.mCallback);
                NRDisplayActivity.this.mIsBound = true;
                NRDisplayActivity.this.queryNrStatus();
            } catch (Exception e) {
                NRDisplayActivity.log("onServiceConnected: Exception = " + e);
            }
        }

        public void onDisconnected() {
            NRDisplayActivity.log("nr-onServiceDisconnected.");
            cleanup();
        }

        private void cleanup() {
            NRDisplayActivity.log("cleanup");
            NRDisplayActivity.this.mIsBound = false;
            NRDisplayActivity.this.mClient = null;
        }
    };
    private IExtPhoneCallback mCallback = new ExtPhoneCallbackBase() { // from class: com.android.phone.NRDisplayActivity.3
        public void on5gStatus(int i, Token token, Status status, boolean z) {
            NRDisplayActivity.log("on5gStatus  status=" + status + ",enabled=" + z + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.sendMessage(NRDisplayActivity.this.mMainThreadHandler.obtainMessage(1, i, -1, Boolean.valueOf(z)));
            }
        }

        public void onNrDcParam(int i, Token token, Status status, DcParam dcParam) {
            NRDisplayActivity.log("onNrDcParam:status=" + status + "dcParam=" + dcParam + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (status.get() == 1 && NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.sendMessage(NRDisplayActivity.this.mMainThreadHandler.obtainMessage(3, i, -1, dcParam));
            }
        }

        public void onAnyNrBearerAllocation(int i, Token token, Status status, BearerAllocationStatus bearerAllocationStatus) {
            NRDisplayActivity.log("onNrBearerAllocationChange:status=" + status + ",bearerStatus=" + bearerAllocationStatus + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (status.get() == 1 && NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.sendMessage(NRDisplayActivity.this.mMainThreadHandler.obtainMessage(4, i, -1, bearerAllocationStatus));
            }
        }

        public void onUpperLayerIndInfo(int i, Token token, Status status, UpperLayerIndInfo upperLayerIndInfo) {
            NRDisplayActivity.log("onUpperLayerIndInfo:status=" + status + "UpperLayerIndInfo=" + upperLayerIndInfo + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (status.get() == 1 && NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.sendMessage(NRDisplayActivity.this.mMainThreadHandler.obtainMessage(5, i, -1, upperLayerIndInfo));
            }
        }

        public void onSignalStrength(int i, Token token, Status status, SignalStrength signalStrength) {
            NRDisplayActivity.log("onSignalStrength:status=" + status + " signalStrength=" + signalStrength + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (status.get() == 1 && NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.sendMessage(NRDisplayActivity.this.mMainThreadHandler.obtainMessage(2, i, -1, signalStrength));
            }
        }

        public void on5gConfigInfo(int i, Token token, Status status, NrConfigType nrConfigType) {
            NRDisplayActivity.log("on5gConfigInfo: status=" + status + " NrConfigType=" + nrConfigType + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (status.get() == 1 && NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.sendMessage(NRDisplayActivity.this.mMainThreadHandler.obtainMessage(6, i, -1, nrConfigType));
            }
        }

        public void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) {
            NRDisplayActivity.log("onNrIconType: status=" + status + "NrIconType=" + nrIconType + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (status.get() == 1 && NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.sendMessage(NRDisplayActivity.this.mMainThreadHandler.obtainMessage(7, i, -1, nrIconType));
            }
        }

        public void onEnableEndc(int i, Token token, Status status) {
            NRDisplayActivity.log("onEnableEndc:status=" + status + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (status.get() != 1 || NRDisplayActivity.this.mClient == null || NRDisplayActivity.this.mExtTelephonyManager == null || NRDisplayActivity.this.mDdsPhoneId != i) {
                return;
            }
            NRDisplayActivity.this.mExtTelephonyManager.queryEndcStatus(i, NRDisplayActivity.this.mClient);
        }

        public void onEndcStatus(int i, Token token, Status status, boolean z) {
            NRDisplayActivity.log("onEndcStatus:status=" + status + " endcStatus = " + z + ",dds=" + NRDisplayActivity.this.mDdsPhoneId + ",slotId=" + i);
            if (status.get() == 1 && NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.sendMessage(NRDisplayActivity.this.mMainThreadHandler.obtainMessage(8, i, -1, Boolean.valueOf(z)));
            }
        }
    };
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.NRDisplayActivity.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int defaultDataSlotId;
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            NRDisplayActivity.log("mIntentReceiver action=".concat(action));
            if (!action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED") || NRDisplayActivity.this.mDdsPhoneId == (defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId())) {
                return;
            }
            NRDisplayActivity.log("Dds had been changed oldDds=" + NRDisplayActivity.this.mDdsPhoneId + ",currentDds=" + defaultDataSlotId);
            NRDisplayActivity.this.mDdsPhoneId = defaultDataSlotId;
            if (NRDisplayActivity.this.mIsBound) {
                NRDisplayActivity.this.queryNrStatus();
            }
            NRDisplayActivity.this.update5gStatus();
            NRDisplayActivity.this.updateNrState(null);
            NRDisplayActivity.this.updateNrSignalStrength(null);
        }
    };

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.nr_signal_display);
        log("onCreate");
        this.mContext = this;
        this.mDdsPhoneId = DefaultSimManager.getDefaultDataSlotId();
        initResource();
        this.mExtTelephonyManager = ExtTelephonyManager.getInstance(this.mContext);
        registerReceiver();
        bindNrService();
        registerTelephonyCallBack();
        final ScrollView scrollView = (ScrollView) findViewById(R.id.nr_signal_display_view);
        scrollView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.phone.NRDisplayActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return NRDisplayActivity.$r8$lambda$E9GowcDVVGnw18znSWtDfaDvMYo(scrollView, view, windowInsets);
            }
        });
    }

    public static /* synthetic */ WindowInsets $r8$lambda$E9GowcDVVGnw18znSWtDfaDvMYo(ScrollView scrollView, View view, WindowInsets windowInsets) {
        scrollView.setPadding(scrollView.getPaddingLeft(), WindowInsetsCompat.toWindowInsetsCompat(windowInsets).getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()).top, scrollView.getPaddingRight(), scrollView.getPaddingBottom());
        return WindowInsetsCompat.CONSUMED.toWindowInsets();
    }

    private void initResource() {
        this.mStatus = (TextView) findViewById(R.id.value5gStateSlot1);
        this.mDataConnection = (TextView) findViewById(R.id.value5gDataConnectionStatusSlot1);
        this.mEndcAvailable = (TextView) findViewById(R.id.valueEndcAvailableSlot1);
        this.mRestrictDcnr = (TextView) findViewById(R.id.valueRestrictDcnrSlot1);
        this.mNrAvailStatus = (TextView) findViewById(R.id.valueNrAvailStatusSlot1);
        this.mSignalStrength = (TextView) findViewById(R.id.value5gSignalStrengthSlot1);
        this.mConfigType = (TextView) findViewById(R.id.valueConfigTypeSlot1);
        this.mIconType = (TextView) findViewById(R.id.valueIconTypeSlot1);
        this.mEndcStatus = (TextView) findViewById(R.id.valueEndcStatusSlot1);
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        log("onResume");
        if (this.mIsBound) {
            queryNrStatus();
        }
        update5gStatus();
        updateNrState(null);
        updateNrSignalStrength(null);
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
        unbindNrService();
        BroadcastReceiver broadcastReceiver = this.mIntentReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
        }
        unregisterTelephonyCallBack();
    }

    private void bindNrService() {
        log("bindNrService mIsBound: " + this.mIsBound);
        boolean z = this.mIsBound;
        ExtTelephonyManager extTelephonyManager = this.mExtTelephonyManager;
        if (!z) {
            extTelephonyManager.connectService(this.mNrServiceConnection);
        } else {
            if (extTelephonyManager == null || this.mClient == null) {
                return;
            }
            queryNrStatus();
        }
    }

    private void unbindNrService() {
        log("unbindNrService");
        try {
            this.mExtTelephonyManager.unRegisterCallback(this.mCallback);
        } catch (Exception e) {
            log("unbindFivegService: Exception = " + e);
        }
        this.mExtTelephonyManager.disconnectService();
        this.mClient = null;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queryNrStatus() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            try {
                this.mExtTelephonyManager.queryNrIconType(i, this.mClient);
                this.mExtTelephonyManager.queryEndcStatus(i, this.mClient);
            } catch (Exception e) {
                log("queryNrStatus: Exception = " + e);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update5gStatus() {
        this.mStatus.setText(FiveGManagerBase.getInstance().isUserFiveGEnabled(this.mDdsPhoneId) ? "ON" : "OFF");
    }

    private void updateNrDcParam(ServiceState serviceState) {
        String str;
        String str2;
        NetworkRegistrationInfo networkRegistrationInfo;
        DataSpecificRegistrationInfo dataSpecificInfo;
        if (serviceState != null && (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) != null && (dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo()) != null) {
            str = dataSpecificInfo.isEnDcAvailable ? "AVAILABLE" : "UNAVAILABLE";
            str2 = dataSpecificInfo.isDcNrRestricted ? "RESTRICTED" : "UNRESTRICTED";
        } else {
            str = "";
            str2 = "";
        }
        this.mEndcAvailable.setText(str);
        this.mRestrictDcnr.setText(str2);
    }

    private void updateNrAvailableStatus(ServiceState serviceState) {
        String str;
        NetworkRegistrationInfo networkRegistrationInfo;
        DataSpecificRegistrationInfo dataSpecificInfo;
        if (serviceState != null && (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1)) != null && (dataSpecificInfo = networkRegistrationInfo.getDataSpecificInfo()) != null) {
            str = dataSpecificInfo.isNrAvailable ? "AVAILABLE" : "UNAVAILABLE";
        } else {
            str = "";
        }
        this.mNrAvailStatus.setText(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNrSignalStrength(android.telephony.SignalStrength signalStrength) {
        Phone phone = PhoneFactory.getPhone(this.mDdsPhoneId);
        ServiceState serviceState = phone != null ? phone.getServiceState() : null;
        if (signalStrength == null) {
            signalStrength = phone != null ? phone.getSignalStrength() : null;
        }
        String str = "";
        if (signalStrength == null || serviceState == null) {
            log("ss or signal is null, return.");
            this.mSignalStrength.setText("");
            return;
        }
        log("updateNrSignalStrength ss = " + serviceState);
        log("updateNrSignalStrength signalstrength = " + signalStrength);
        int nrState = serviceState.getNrState();
        int i = serviceState.getRilDataRadioTechnology() == 20 ? 1 : (nrState == 3 || nrState == 2) ? 0 : -1;
        log("updateNrSignalStrength nrConfigType is " + i);
        String str2 = "";
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellSignalStrength;
                int rsrp = cellSignalStrengthLte.getRsrp();
                int rssnr = cellSignalStrengthLte.getRssnr();
                log("updateNrSignalStrength lteRsrp=" + rsrp + ", lteRssnr=" + rssnr);
                str2 = "CellSignalStrengthLte: lteRsrp: " + rsrp + " lteRssnr: " + rssnr;
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
                int ssRsrp = cellSignalStrengthNr.getSsRsrp();
                int ssSinr = cellSignalStrengthNr.getSsSinr();
                log("updateNrSignalStrength nrRsrp=" + ssRsrp + ", nrSinr=" + ssSinr);
                str = "CellSignalStrengthNr: nrRsrp: " + ssRsrp + " nrSinr: " + ssSinr;
            }
        }
        if (i == 0 && TextUtils.isEmpty(str)) {
            str = str2;
        }
        this.mSignalStrength.setText(str);
    }

    private void update5gConfigInfo(ServiceState serviceState) {
        String str = "";
        String str2 = "NOT-ALLOCATED";
        if (serviceState != null) {
            int nrState = serviceState.getNrState();
            if (serviceState.getRilDataRadioTechnology() == 20) {
                str = "SA";
            } else {
                if (nrState == 3 || nrState == 2) {
                    str = "NSA";
                }
                if (nrState == 3) {
                }
            }
            str2 = "ALLOCATED";
        }
        this.mConfigType.setText(str);
        this.mDataConnection.setText(str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNrState(ServiceState serviceState) {
        if (serviceState == null) {
            Phone phone = PhoneFactory.getPhone(this.mDdsPhoneId);
            serviceState = phone != null ? phone.getServiceState() : null;
            log("updateNrState ss : " + serviceState);
        }
        updateNrDcParam(serviceState);
        updateNrAvailableStatus(serviceState);
        update5gConfigInfo(serviceState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNrStateFromPhysicalChannelConfigs(List list) {
        Phone phone = PhoneFactory.getPhone(this.mDdsPhoneId);
        ServiceState serviceState = phone != null ? phone.getServiceState() : null;
        if (list == null || serviceState == null) {
            return;
        }
        String str = "ALLOCATED";
        if (serviceState.getRilDataRadioTechnology() != 20) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PhysicalChannelConfig physicalChannelConfig = (PhysicalChannelConfig) it.next();
                if (!isNrPhysicalChannelConfig(physicalChannelConfig) || physicalChannelConfig.getConnectionStatus() != 2) {
                }
            }
            str = "NOT-ALLOCATED";
        }
        this.mDataConnection.setText(str);
    }

    private boolean isNrPhysicalChannelConfig(PhysicalChannelConfig physicalChannelConfig) {
        return physicalChannelConfig.getNetworkType() == 20;
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void unregisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            log("unregister TelephonyCallBack");
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, android.telephony.SignalStrength signalStrength) {
            if (NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.obtainMessage(9, i, 0, signalStrength).sendToTarget();
            }
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (NRDisplayActivity.this.mDdsPhoneId == i) {
                NRDisplayActivity.this.mMainThreadHandler.obtainMessage(10, i, 0, null).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(TAG, str);
    }
}
