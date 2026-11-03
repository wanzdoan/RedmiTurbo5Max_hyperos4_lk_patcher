package com.android.phone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.telephony.ntnphone.AntIdentitySignalStrength;
import com.android.internal.telephony.ntnphone.NtnAntConstants;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SatDisplayActivity extends Activity implements SensorEventListener {
    private Button mBtnGetCurrentAntSettings;
    private TextView mCBCHInd;
    private TextView mCPST;
    private TextView mCallStatus;
    private TextView mChQual;
    private Context mContext;
    private Spinner mLogLevel;
    private Sensor mMagnetometer;
    private TextView mPhoneAngle;
    private TextView mPlmn;
    private TextView mPwr;
    private TextView mRegState;
    private Sensor mRotationer;
    private TextView mRssi;
    private TextView mSatAngle;
    private TextView mSatId;
    private SensorManager mSensorManager;
    private TextView mSnr;
    private Spinner mSpinnerDualBeamSwitching;
    private Spinner mSpinnerSetRxAnt;
    private Spinner mSpinnerSetTxAnt;
    private TextView mSubId;
    private Switch mSwitchLPM;
    private Switch mSwitchModem;
    private TextView mTXPWR;
    private signalStrengthServiceState mTelephonyCallBack;
    private TextView mTextCurLpmState;
    private TextView mTextCurRxAnt;
    private TextView mTextCurTxAnt;
    private TextView mTextRssiAnt1;
    private TextView mTextRssiAnt1Ant2Combine;
    private TextView mTextRssiAnt2;
    private TextView mTextSnrAnt1;
    private TextView mTextSnrAnt1Ant2Combine;
    private TextView mTextSnrAnt2;
    private TextView mUSIMPre;
    private TextView mUSIMStartUp;
    private TextView mUSIMType;
    private TextView mVersion;
    private float phoneAzimuth;
    private float phonePitch;
    private static final String TAG = SatDisplayActivity.class.getSimpleName();
    private static final String[] BEAM_SWITCHING_LABELS = {"Auto", "Front-of-screen beam", "Back-of-screen beam"};
    private static final String[] TX_ANT_LABELS = {"TX_ANT1", "TX_ANT2"};
    private static final String[] RX_ANT_LABELS = {"RX_ANT1", "RX_ANT2", "RX_ANT1_ANT2_COMBINE"};
    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private int mPhoneId = 0;
    private boolean mIsButton = false;
    private int mCurrentTxAntResult = 0;
    private int mCurrentRxAntResult = 0;
    private List mListRssiAnt1 = new ArrayList();
    private List mListSnrAnt1 = new ArrayList();
    private List mListRssiAnt2 = new ArrayList();
    private List mListSnrAnt2 = new ArrayList();
    private List mListRssiAnt1Ant2Combine = new ArrayList();
    private List mListSnrAnt1Ant2Combine = new ArrayList();
    private long mRefreshTime = 0;
    private Handler mMainThreadHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.phone.SatDisplayActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Object obj;
            Pair pair;
            int i = message.what;
            if (i == 0) {
                SatDisplayActivity.log("switch modem");
                return;
            }
            if (i != 5) {
                if (i == 2) {
                    SatDisplayActivity.log("cpst info upload & update");
                    return;
                } else {
                    if (i != 3) {
                        return;
                    }
                    SatDisplayActivity.log("trace info upload & update");
                    return;
                }
            }
            SatDisplayActivity.log("get antenna state done");
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception == null && (obj = asyncResult.result) != null && (pair = (Pair) obj) != null) {
                SatDisplayActivity.log("antState tx = " + pair.first + ", antState rx = " + pair.second);
                SatDisplayActivity.this.mCurrentTxAntResult = ((NtnAntConstants.TxAntEnableType) pair.first).getType();
                SatDisplayActivity.this.mCurrentRxAntResult = ((NtnAntConstants.RxAntEnableType) pair.second).getType();
                SatDisplayActivity.this.mTextCurTxAnt.setText(((NtnAntConstants.TxAntEnableType) pair.first).name());
                SatDisplayActivity.this.mTextCurRxAnt.setText(((NtnAntConstants.RxAntEnableType) pair.second).name());
            }
            if (SatDisplayActivity.this.mCurrentTxAntResult >= 0 && SatDisplayActivity.this.mCurrentTxAntResult < SatDisplayActivity.TX_ANT_LABELS.length) {
                SatDisplayActivity.this.mSpinnerSetTxAnt.setSelection(SatDisplayActivity.this.mCurrentTxAntResult);
            }
            if (SatDisplayActivity.this.mCurrentRxAntResult < 0 || SatDisplayActivity.this.mCurrentRxAntResult >= SatDisplayActivity.RX_ANT_LABELS.length) {
                return;
            }
            SatDisplayActivity.this.mSpinnerSetRxAnt.setSelection(SatDisplayActivity.this.mCurrentRxAntResult);
        }
    };
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.SatDisplayActivity.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            SatDisplayActivity.log("mIntentReceiver action = ".concat(action));
            if (action.equals("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED")) {
                if (SatDisplayActivity.this.mPhoneId != -1) {
                    SatDisplayActivity.log("Sub id had been changed oldPhone = " + SatDisplayActivity.this.mPhoneId + ", currentPhone = -1");
                    SatDisplayActivity.this.mPhoneId = -1;
                    SatDisplayActivity.this.updateSubId();
                    return;
                }
                return;
            }
            if (action.equals("com.xiaomi.satellite.intent.action.ACTION_SEND_CPST")) {
                SatDisplayActivity.log("receive cpst broadcast");
                SatDisplayActivity.this.updateCPSTInfo(intent.getStringExtra("com.xiaomi.satellite.extra.EXTRA_ACTION_SEND_CPST_INFO"));
                SatDisplayActivity.log("has success updatecpstinfo");
                return;
            }
            if (action.equals("com.xiaomi.satellite.intent.action.ACTION_SEND_TRACE")) {
                SatDisplayActivity.log("receive trace broadcast");
                SatDisplayActivity.this.updateTraceInfo(intent.getStringExtra("com.xiaomi.satellite.extra.EXTRA_ACTION_SEND_TRACE_INFO"));
                SatDisplayActivity.log("has success updatetraceinfo");
            }
        }
    };
    View.OnClickListener mGetCurrentAntSettingsListener = new View.OnClickListener() { // from class: com.android.phone.SatDisplayActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SatDisplayActivity.this.updateCurrentAntSettings();
        }
    };
    AdapterView.OnItemSelectedListener mSetTxAntListener = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.SatDisplayActivity.6
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            NtnPhone defaultNtnPhone;
            if (i < 0 || i > SatDisplayActivity.TX_ANT_LABELS.length - 1 || (defaultNtnPhone = SatDisplayActivity.this.getDefaultNtnPhone()) == null) {
                return;
            }
            StringBuilder sb = new StringBuilder("mSetTxAntListener: pos = ");
            sb.append(i);
            sb.append(" tx ant = ");
            int i2 = i + 1;
            sb.append(NtnAntConstants.TxAntEnableType.values()[i2].name());
            SatDisplayActivity.log(sb.toString());
            defaultNtnPhone.mNtnRil.setAntennaState(NtnAntConstants.TxAntEnableType.values()[i2], NtnAntConstants.RxAntEnableType.RX_UNSET, (Message) null);
        }
    };
    AdapterView.OnItemSelectedListener mSetRxAntListener = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.SatDisplayActivity.7
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            NtnPhone defaultNtnPhone;
            if (i < 0 || i > SatDisplayActivity.RX_ANT_LABELS.length - 1 || (defaultNtnPhone = SatDisplayActivity.this.getDefaultNtnPhone()) == null) {
                return;
            }
            StringBuilder sb = new StringBuilder("mSetRxAntListener: pos = ");
            sb.append(i);
            sb.append(" rx ant = ");
            int i2 = i + 1;
            sb.append(NtnAntConstants.RxAntEnableType.values()[i2].name());
            SatDisplayActivity.log(sb.toString());
            defaultNtnPhone.mNtnRil.setAntennaState(NtnAntConstants.TxAntEnableType.TX_UNSET, NtnAntConstants.RxAntEnableType.values()[i2], (Message) null);
        }
    };
    NtnPhone.AntInfoChangedListener mAntInfolistener = new NtnPhone.AntInfoChangedListener() { // from class: com.android.phone.SatDisplayActivity.8
        public void onAntInfoSignalStrengthChanged(ArrayList arrayList) {
            SatDisplayActivity.log("onAntInfoSignalStrengthChanged: antInfo size = " + arrayList.size());
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                AntIdentitySignalStrength antIdentitySignalStrength = (AntIdentitySignalStrength) obj;
                SatDisplayActivity.log("mAntInfolistener: ant id = " + antIdentitySignalStrength.getAntId() + ", rssi = " + antIdentitySignalStrength.getRssi() + ", snr = " + antIdentitySignalStrength.getSnr());
                SatDisplayActivity.this.updateDisplayAntSignalStrength(antIdentitySignalStrength);
            }
        }
    };
    AdapterView.OnItemSelectedListener mBeamSwitchingListener = new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.SatDisplayActivity.9
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
            if (i < 0 || i > SatDisplayActivity.BEAM_SWITCHING_LABELS.length - 1) {
                return;
            }
            SatDisplayActivity.log("set beam switching mode, pos = " + i + ", mode = " + SatDisplayActivity.BEAM_SWITCHING_LABELS[i]);
            try {
                SystemProperties.set("debug.satellite.beam_switching_mode", Integer.toString(i));
                SatDisplayActivity.this.mSpinnerDualBeamSwitching.setSelection(i, true);
            } catch (Exception e) {
                SatDisplayActivity.loge("update beam switching failed:" + e);
            }
        }
    };
    CompoundButton.OnCheckedChangeListener mLPMListener = new CompoundButton.OnCheckedChangeListener(this) { // from class: com.android.phone.SatDisplayActivity.10
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            try {
                SystemProperties.set("ril.satellite.low_power_mode", z ? "1" : "0");
            } catch (Exception e) {
                SatDisplayActivity.loge("update satellite lpm prop failed:" + e);
            }
            SatDisplayActivity.log("toggle satellite lpm : ".concat(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF));
        }
    };
    NtnPhone.UnsolNtnCdcMsgListener mLPMStateChangedlistener = new NtnPhone.UnsolNtnCdcMsgListener() { // from class: com.android.phone.SatDisplayActivity.11
        public void unsolNtnCdcMsg(byte[] bArr) {
            if (bArr == null || bArr.length < 1) {
                return;
            }
            SatDisplayActivity.this.updateCurrentLPMState(bArr[0]);
        }
    };

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private void initResource() {
        this.mSwitchModem = (Switch) findViewById(R.id.setting_modem_sw);
        this.mSatId = (TextView) findViewById(R.id.valueSatId);
        this.mPlmn = (TextView) findViewById(R.id.valuePLMN);
        this.mCallStatus = (TextView) findViewById(R.id.valueCallStatus);
        this.mSatAngle = (TextView) findViewById(R.id.valueSatLocation);
        this.mVersion = (TextView) findViewById(R.id.valueVersion);
        this.mSubId = (TextView) findViewById(R.id.valueSubId);
        this.mCPST = (TextView) findViewById(R.id.valueCPST);
        this.mUSIMType = (TextView) findViewById(R.id.valueUSIMType);
        this.mUSIMPre = (TextView) findViewById(R.id.valueUSIMPre);
        this.mUSIMStartUp = (TextView) findViewById(R.id.valueUSIMStartUp);
        this.mSnr = (TextView) findViewById(R.id.valueSnr);
        this.mChQual = (TextView) findViewById(R.id.valueChQual);
        this.mTXPWR = (TextView) findViewById(R.id.valueTXPWR);
        this.mCBCHInd = (TextView) findViewById(R.id.valueCBCHInd);
        this.mPwr = (TextView) findViewById(R.id.valuePwr);
        this.mPhoneAngle = (TextView) findViewById(R.id.valuePhoneAngle);
        this.mRegState = (TextView) findViewById(R.id.valueRegState);
        this.mRssi = (TextView) findViewById(R.id.valueRssi);
        this.mLogLevel = (Spinner) findViewById(R.id.valueLogLevel);
        this.mSpinnerDualBeamSwitching = (Spinner) findViewById(R.id.sat_dual_beam_switching_value);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, BEAM_SWITCHING_LABELS);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSpinnerDualBeamSwitching.setAdapter((SpinnerAdapter) arrayAdapter);
        this.mSpinnerDualBeamSwitching.setOnItemSelectedListener(this.mBeamSwitchingListener);
        Switch r0 = (Switch) findViewById(R.id.lpm_switch);
        this.mSwitchLPM = r0;
        r0.setOnCheckedChangeListener(this.mLPMListener);
        this.mTextCurLpmState = (TextView) findViewById(R.id.current_sat_lpm_state);
        Button button = (Button) findViewById(R.id.current_ant_settings);
        this.mBtnGetCurrentAntSettings = button;
        button.setOnClickListener(this.mGetCurrentAntSettingsListener);
        this.mTextCurTxAnt = (TextView) findViewById(R.id.current_tx_ant_info);
        this.mTextCurRxAnt = (TextView) findViewById(R.id.current_rx_ant_info);
        this.mSpinnerSetTxAnt = (Spinner) findViewById(R.id.tx_ant_values);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, TX_ANT_LABELS);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSpinnerSetTxAnt.setAdapter((SpinnerAdapter) arrayAdapter2);
        this.mSpinnerSetTxAnt.setSelection(this.mCurrentTxAntResult, true);
        this.mSpinnerSetTxAnt.setOnItemSelectedListener(this.mSetTxAntListener);
        this.mSpinnerSetRxAnt = (Spinner) findViewById(R.id.rx_ant_values);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, RX_ANT_LABELS);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSpinnerSetRxAnt.setAdapter((SpinnerAdapter) arrayAdapter3);
        this.mSpinnerSetRxAnt.setSelection(this.mCurrentRxAntResult, true);
        this.mSpinnerSetRxAnt.setOnItemSelectedListener(this.mSetRxAntListener);
        this.mTextRssiAnt1 = (TextView) findViewById(R.id.ant1_rssi);
        this.mTextSnrAnt1 = (TextView) findViewById(R.id.ant1_snr);
        this.mTextRssiAnt2 = (TextView) findViewById(R.id.ant2_rssi);
        this.mTextSnrAnt2 = (TextView) findViewById(R.id.ant2_snr);
        this.mTextRssiAnt1Ant2Combine = (TextView) findViewById(R.id.ant1_ant2_combine_rssi);
        this.mTextSnrAnt1Ant2Combine = (TextView) findViewById(R.id.ant1_ant2_combine_snr);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = this;
        setContentView(R.layout.sat_info_display);
        initResource();
        switchOnSat();
        selectLogLevel();
        log("onCreate");
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        phoneAngle(this);
        updateCurrentLPMState();
        unRegisterLPMStateChanged();
        registerLPMStateChanged();
        updateCurrentAntSettings();
        unRegisterAntInfoChanged();
        registerAntInfoChanged();
        this.mListRssiAnt1.clear();
        this.mListSnrAnt1.clear();
        this.mListRssiAnt2.clear();
        this.mListSnrAnt2.clear();
        this.mListRssiAnt1Ant2Combine.clear();
        this.mListSnrAnt1Ant2Combine.clear();
        this.mRefreshTime = SystemClock.elapsedRealtime();
        updateBeamSwitchingSettings();
        updateSatelliteLPMState();
        log("onResume success");
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.mIsButton) {
            BroadcastReceiver broadcastReceiver = this.mIntentReceiver;
            if (broadcastReceiver != null) {
                this.mContext.unregisterReceiver(broadcastReceiver);
            }
            unRegisterSatState();
            unregisterSensorListener();
            unregisterTelephonyCallBack();
        }
        unRegisterLPMStateChanged();
        unRegisterAntInfoChanged();
        log("onDestroy success");
    }

    public void switchOnSat() {
        this.mSwitchModem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.phone.SatDisplayActivity.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SatDisplayActivity satDisplayActivity = SatDisplayActivity.this;
                if (z) {
                    try {
                        satDisplayActivity.openSatelliteModem();
                        SatDisplayActivity.this.updateFixInfo();
                        SatDisplayActivity.this.mMainThreadHandler.sendMessage(SatDisplayActivity.this.mMainThreadHandler.obtainMessage(0));
                        SatDisplayActivity.this.registerReceiver();
                        SatDisplayActivity.this.updateSatVersion();
                        SatDisplayActivity.this.registerTelephonyCallBack();
                        if (SatDisplayActivity.this.mSensorManager != null) {
                            SatDisplayActivity.this.registerSensorListener();
                        }
                        SatDisplayActivity.this.updateSubId();
                        SatDisplayActivity.this.mIsButton = true;
                        Toast.makeText(SatDisplayActivity.this, "switch satellite modem on", 1).show();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                try {
                    satDisplayActivity.closeSatelliteModem();
                    SatDisplayActivity.this.unregisterSensorListener();
                    if (SatDisplayActivity.this.mIntentReceiver != null) {
                        SatDisplayActivity.this.mContext.unregisterReceiver(SatDisplayActivity.this.mIntentReceiver);
                    }
                    SatDisplayActivity.this.unRegisterSatState();
                    SatDisplayActivity.this.unregisterTelephonyCallBack();
                    SatDisplayActivity.this.unRegisterSatState();
                    SatDisplayActivity.this.mIsButton = false;
                    Toast.makeText(SatDisplayActivity.this, "switch satellite modem off ", 1).show();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    public void unRegisterSatState() {
        this.mSwitchModem.setText("");
        this.mSatId.setText("");
        this.mPlmn.setText("");
        this.mCallStatus.setText("");
        this.mSatAngle.setText("");
        this.mVersion.setText("");
        this.mSubId.setText("");
        this.mCPST.setText("");
        this.mUSIMType.setText("");
        this.mUSIMPre.setText("");
        this.mUSIMStartUp.setText("");
        this.mSnr.setText("");
        this.mChQual.setText("");
        this.mTXPWR.setText("");
        this.mCBCHInd.setText("");
        this.mPwr.setText("");
        this.mPhoneAngle.setText("");
        this.mRegState.setText("");
        this.mRssi.setText("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("com.xiaomi.satellite.intent.action.ACTION_SEND_CPST");
        intentFilter.addAction("com.xiaomi.satellite.intent.action.ACTION_SEND_TRACE");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openSatelliteModem() {
        if (Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0) == 0) {
            log("satellite state is 0");
            if (SatelliteManagerBase.getInstance() == null) {
                log("satellite manager is null");
                return;
            } else {
                SatelliteManagerBase.getInstance().setSatelliteEnableByUser(this.mContext, true, 0);
                this.mMainThreadHandler.sendEmptyMessageDelayed(4, 10000L);
                return;
            }
        }
        this.mMainThreadHandler.sendEmptyMessageDelayed(4, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSatelliteModem() {
        if (Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0) != 0) {
            if (SatelliteManagerBase.getInstance() == null) {
                log("satellite manager is null");
            } else {
                SatelliteManagerBase.getInstance().setSatelliteEnableByUser(this.mContext, false, 0);
                log("close satellite");
            }
        }
    }

    class signalStrengthServiceState extends TelephonyCallback implements TelephonyCallback.ServiceStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.CallStateListener {
        signalStrengthServiceState() {
        }

        @Override // android.telephony.TelephonyCallback.SignalStrengthsListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            SatDisplayActivity.this.mRssi.setText(String.valueOf(signalStrength.getDbm()));
            SatDisplayActivity.this.updateSnr();
            SatDisplayActivity.this.updateTxPwr();
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            SatDisplayActivity.this.mRegState.setText(serviceState.getState() == 0 ? "REG_HOME" : "NO_REG");
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int i) {
            if (i == -1) {
                SatDisplayActivity.this.mCallStatus.setText("UNKNOWN");
                return;
            }
            if (i == 0) {
                SatDisplayActivity.this.mCallStatus.setText("IDLE");
            } else if (i == 1) {
                SatDisplayActivity.this.mCallStatus.setText("RINGING");
            } else {
                if (i != 2) {
                    return;
                }
                SatDisplayActivity.this.mCallStatus.setText("OFFHOOK");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerTelephonyCallBack() {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        if (this.mTelephonyCallBack == null) {
            this.mTelephonyCallBack = new signalStrengthServiceState();
        }
        telephonyManager.registerTelephonyCallback(this.mContext.getMainExecutor(), this.mTelephonyCallBack);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterTelephonyCallBack() {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        if (this.mTelephonyCallBack == null) {
            this.mTelephonyCallBack = new signalStrengthServiceState();
        }
        telephonyManager.unregisterTelephonyCallback(this.mTelephonyCallBack);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCPSTInfo(String str) {
        if (str.length() == 1 || str.length() == 2) {
            this.mCPST.setText("Rssi:" + str + "  Freq:   Procedure:   Message: ");
            return;
        }
        String[] strArrSplit = str.split(", ");
        log("receive cpstInfo is:".concat(str));
        Map mapCPSTInfoDir = ParserHexStr.CPSTInfoDir();
        this.mCPST.setText("Rssi:" + strArrSplit[0] + "  Freq:" + strArrSplit[1] + "  Procedure:" + ((String) mapCPSTInfoDir.get(strArrSplit[2])) + "  Message:" + strArrSplit[3]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTraceInfo(String str) {
        String str2;
        log("receive traceInfo is:" + str);
        String strSubstring = str.substring(0, str.indexOf(","));
        String strTrim = str.substring(str.lastIndexOf(",") + 1).trim();
        log("receive envId is:" + strSubstring + " receive envHexStr is:" + strTrim);
        try {
            if (strSubstring.equals("0")) {
                this.mUSIMType.setText(ParserHexStr.hexStr0(strTrim) == 0 ? "Real card" : "Fake card");
                return;
            }
            if (strSubstring.equals("1")) {
                this.mUSIMPre.setText(ParserHexStr.hexStr0(strTrim) == 0 ? "Not pre" : "Pre");
                return;
            }
            if (strSubstring.equals("2")) {
                int[] iArrHexStr2 = ParserHexStr.hexStr2(strTrim);
                int i = iArrHexStr2[0];
                TextView textView = this.mUSIMStartUp;
                if (i == 0) {
                    int i2 = iArrHexStr2[1];
                    if (i2 == 0) {
                        str2 = " Vol:Unknown";
                    } else {
                        str2 = i2 == 1 ? " Vol:1.8V" : " Vol:3.0V";
                    }
                    textView.setText("Start success".concat(str2));
                    return;
                }
                textView.setText("Start failed");
                return;
            }
            if (strSubstring.equals("14")) {
                this.mSnr.setText(String.valueOf(ParserHexStr.hexStr14(strTrim)));
                return;
            }
            if (strSubstring.equals("44")) {
                int[] iArrHexStr3 = ParserHexStr.hexStr2(strTrim);
                TextView textView2 = this.mChQual;
                StringBuilder sb = new StringBuilder("ChnType:");
                sb.append(iArrHexStr3[0] == 0 ? "DCH" : "PDCH");
                sb.append(" PackerLossRate:");
                sb.append(iArrHexStr3[1]);
                textView2.setText(sb.toString());
                return;
            }
            if (strSubstring.equals("58")) {
                int[] iArrHexStr58 = ParserHexStr.hexStr58(strTrim);
                this.mTXPWR.setText("FrameNum:" + iArrHexStr58[0] + " Par:" + iArrHexStr58[1] + " Pan:" + iArrHexStr58[2] + " Pd:" + iArrHexStr58[3] + " Temp:" + iArrHexStr58[4] + " MS:" + iArrHexStr58[5]);
                return;
            }
            if (strSubstring.equals("59")) {
                int[] iArrHexStr4 = ParserHexStr.hexStr2(strTrim);
                this.mCBCHInd.setText("TotalPage:" + iArrHexStr4[0] + " CurPage:" + iArrHexStr4[1]);
                return;
            }
            if (strSubstring.equals("60")) {
                int[] iArrHexStr60 = ParserHexStr.hexStr60(strTrim);
                this.mPwr.setText("TargetPower:" + iArrHexStr60[0] + " PdStartPower:" + iArrHexStr60[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSubId() {
        this.mSubId.setText(String.valueOf(SubscriptionManager.getDefaultSubscriptionId()));
    }

    public void updateFixInfo() {
        this.mRssi.setText("0");
        this.mRegState.setText("NO_REG");
        this.mPlmn.setText("460-03");
        this.mSatId.setText("1");
        this.mSatAngle.setText("East longitude：101.4°  Latitude：0°");
    }

    public void updateSatVersion() {
        this.mVersion.setText("XX.XX.XX");
    }

    public void updateSnr() {
        try {
            this.mSnr.setText(String.format("%.2f", Double.valueOf(((double) SatelliteManagerBase.getInstance().getRetSnr()) / 10.0d)));
        } catch (Exception e) {
            log("updateSnr: " + e);
        }
    }

    public void updateTxPwr() {
        try {
            double tarTxPwr = ((double) SatelliteManagerBase.getInstance().getTarTxPwr()) / 100.0d;
            double realTxPwr = ((double) SatelliteManagerBase.getInstance().getRealTxPwr()) / 100.0d;
            this.mTXPWR.setText("tar_TxPwr:" + String.format("%.2f", Double.valueOf(tarTxPwr)) + ", real_TxPwr:" + String.format("%.2f", Double.valueOf(realTxPwr)));
        } catch (Exception e) {
            log("updateTxPwr: " + e);
        }
    }

    private void selectLogLevel() {
        ArrayAdapter<CharSequence> arrayAdapterCreateFromResource = ArrayAdapter.createFromResource(this, R.array.js_ntn_log_level, android.R.layout.simple_spinner_item);
        arrayAdapterCreateFromResource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mLogLevel.setAdapter((SpinnerAdapter) arrayAdapterCreateFromResource);
        final SharedPreferences sharedPreferences = getSharedPreferences("preItem", 0);
        this.mLogLevel.setSelection(sharedPreferences.getInt("spinnerSelection", 0));
        this.mLogLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.android.phone.SatDisplayActivity.4
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                editorEdit.putInt("spinnerSelection", i);
                editorEdit.apply();
                int selectedItemPosition = SatDisplayActivity.this.mLogLevel.getSelectedItemPosition();
                if (selectedItemPosition == 0) {
                    SystemProperties.set("persist.radio.ntn.log.level", Integer.toString(0));
                } else {
                    SystemProperties.set("persist.radio.ntn.log.level", Integer.toString(2));
                }
                SatDisplayActivity.log("select item is:" + selectedItemPosition);
            }
        });
    }

    public void phoneAngle(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensorManager = sensorManager;
        this.mRotationer = sensorManager.getDefaultSensor(11);
        this.mMagnetometer = this.mSensorManager.getDefaultSensor(2);
    }

    public void registerSensorListener() {
        this.mSensorManager.registerListener(this, this.mRotationer, 3);
        this.mSensorManager.registerListener(this, this.mMagnetometer, 3);
    }

    public void unregisterSensorListener() {
        this.mSensorManager.unregisterListener(this);
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 11) {
            float[] fArr = sensorEvent.values;
            float[] fArr2 = new float[9];
            SensorHelper.quatToRotMat(fArr2, (float[]) fArr.clone());
            SensorHelper.rotMatToOrient(fArr, fArr2);
            this.phoneAzimuth = (fArr[0] + 720.0f) % 360.0f;
            this.phonePitch = fArr[1];
            this.mPhoneAngle.setText("Azimuth: " + this.phoneAzimuth + " Pitch: " + this.phonePitch);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCurrentAntSettings() {
        this.mTextCurTxAnt.setText("");
        this.mTextCurRxAnt.setText("");
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            Toast.makeText(this, "satellite switch off now!", 1).show();
        } else {
            defaultNtnPhone.getAntennaState(this.mMainThreadHandler.obtainMessage(5));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NtnPhone getDefaultNtnPhone() {
        try {
            return NtnPhoneFactory.getDefaultPhone();
        } catch (Exception e) {
            loge("getDefaultNtnPhone: " + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayAntSignalStrength(AntIdentitySignalStrength antIdentitySignalStrength) {
        if (antIdentitySignalStrength == null) {
            return;
        }
        if (SystemClock.elapsedRealtime() - this.mRefreshTime >= MirrorDesktopHelper.TIMEOUT_MILLIS) {
            int iCalculateAverage = (int) calculateAverage(this.mListRssiAnt1);
            float fCalculateAverage = (float) calculateAverage(this.mListSnrAnt1);
            int iCalculateAverage2 = (int) calculateAverage(this.mListRssiAnt2);
            float fCalculateAverage2 = (float) calculateAverage(this.mListSnrAnt2);
            int iCalculateAverage3 = (int) calculateAverage(this.mListRssiAnt1Ant2Combine);
            float fCalculateAverage3 = (float) calculateAverage(this.mListSnrAnt1Ant2Combine);
            log("updateDisplayAntSignalStrength: ant1 avgRssi = " + iCalculateAverage + ", avgSnr = " + fCalculateAverage + ", ant2 avgRssi = " + iCalculateAverage2 + ", ant2 avgSnr = " + fCalculateAverage2 + ", combine avgRssi = " + iCalculateAverage3 + ", combine avgSnr = " + fCalculateAverage3);
            this.mTextRssiAnt1.setText(String.valueOf(iCalculateAverage));
            this.mTextSnrAnt1.setText(String.valueOf(fCalculateAverage));
            this.mTextRssiAnt2.setText(String.valueOf(iCalculateAverage2));
            this.mTextSnrAnt2.setText(String.valueOf(fCalculateAverage2));
            this.mTextRssiAnt1Ant2Combine.setText(String.valueOf(iCalculateAverage3));
            this.mTextSnrAnt1Ant2Combine.setText(String.valueOf(fCalculateAverage3));
            this.mListRssiAnt1.clear();
            this.mListSnrAnt1.clear();
            this.mListRssiAnt2.clear();
            this.mListSnrAnt2.clear();
            this.mListRssiAnt1Ant2Combine.clear();
            this.mListSnrAnt1Ant2Combine.clear();
            this.mRefreshTime = SystemClock.elapsedRealtime();
            return;
        }
        if (antIdentitySignalStrength.getAntId() == NtnAntConstants.RxAntEnableType.RX_ANT1.getType()) {
            this.mListRssiAnt1.add(Integer.valueOf(antIdentitySignalStrength.getRssi()));
            this.mListSnrAnt1.add(Float.valueOf(antIdentitySignalStrength.getSnr()));
        } else if (antIdentitySignalStrength.getAntId() == NtnAntConstants.RxAntEnableType.RX_ANT2.getType()) {
            this.mListRssiAnt2.add(Integer.valueOf(antIdentitySignalStrength.getRssi()));
            this.mListSnrAnt2.add(Float.valueOf(antIdentitySignalStrength.getSnr()));
        } else if (antIdentitySignalStrength.getAntId() == NtnAntConstants.RxAntEnableType.RX_ANT_COMBINED.getType()) {
            this.mListRssiAnt1Ant2Combine.add(Integer.valueOf(antIdentitySignalStrength.getRssi()));
            this.mListSnrAnt1Ant2Combine.add(Float.valueOf(antIdentitySignalStrength.getSnr()));
        }
    }

    private static double calculateAverage(List list) {
        double dDoubleValue = 0.0d;
        if (list == null || list.isEmpty()) {
            return 0.0d;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            dDoubleValue += ((Number) it.next()).doubleValue();
        }
        return dDoubleValue / ((double) list.size());
    }

    public void registerAntInfoChanged() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            log("registerAntInfoChanged error, ntnPhone is null");
        } else {
            defaultNtnPhone.registerAntInfoChangedListener(this.mAntInfolistener);
            log("registerAntInfoChanged");
        }
    }

    public void unRegisterAntInfoChanged() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            log("unRegisterAntInfoChanged error, ntnPhone is null");
        } else {
            defaultNtnPhone.unRegisterAntInfoChangedListener(this.mAntInfolistener);
            log("registerAntInfoChanged");
        }
    }

    private void updateBeamSwitchingSettings() {
        int i;
        try {
            i = Integer.parseInt(SystemProperties.get("debug.satellite.beam_switching_mode", "0"));
        } catch (Exception e) {
            loge("update beam switching failed:" + e);
            i = 0;
        }
        if (i < 0 || i > BEAM_SWITCHING_LABELS.length - 1) {
            return;
        }
        this.mSpinnerDualBeamSwitching.setSelection(i, true);
    }

    private void updateSatelliteLPMState() {
        this.mSwitchLPM.setOnCheckedChangeListener(null);
        this.mSwitchLPM.setChecked(isSatelliteLPM());
        this.mSwitchLPM.setOnCheckedChangeListener(this.mLPMListener);
    }

    private int getSatelliteDefaultLPMState() {
        try {
            return SystemProperties.getInt("ro.vendor.satellite.enable_lpm", 0);
        } catch (Exception e) {
            loge("getSatelliteDefaultLPMState failed:" + e);
            return 0;
        }
    }

    private boolean isSatelliteLPM() {
        int i;
        try {
            i = SystemProperties.getInt("ril.satellite.low_power_mode", getSatelliteDefaultLPMState());
        } catch (Exception e) {
            loge("isSatelliteLPM failed:" + e);
            i = 0;
        }
        return i == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCurrentLPMState(int i) {
        final String str;
        if (i == 0) {
            str = "OFF";
        } else {
            str = i == 1 ? "ON" : "UNKNOWN";
        }
        runOnUiThread(new Runnable() { // from class: com.android.phone.SatDisplayActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateCurrentLPMState$0(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentLPMState$0(String str) {
        this.mTextCurLpmState.setText(str);
    }

    private void updateCurrentLPMState() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone != null) {
            updateCurrentLPMState(defaultNtnPhone.getLPMState());
        }
    }

    public void registerLPMStateChanged() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone != null) {
            defaultNtnPhone.registerUnsolNtnCdcMsgListener(1, this.mLPMStateChangedlistener);
            log("registerLPMStateChanged");
        }
    }

    public void unRegisterLPMStateChanged() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone != null) {
            defaultNtnPhone.unregisterUnsolNtnCdcMsgListener(1, this.mLPMStateChangedlistener);
            log("unRegisterLPMStateChanged");
        }
    }

    public static void log(String str) {
        Log.d(TAG, str);
    }

    public static void loge(String str) {
        Log.e(TAG, str);
    }
}
