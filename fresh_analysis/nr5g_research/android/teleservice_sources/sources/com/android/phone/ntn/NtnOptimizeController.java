package com.android.phone.ntn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.SatelliteManagerBase;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.satellite.SatelliteDialogActivity;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import miui.telephony.TelephonyStatAdapter;
import miuix.util.HapticFeedbackCompat;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes.dex */
public class NtnOptimizeController extends NtnOptimizeControllerBase {
    private static String LOG_TAG = "NtnOptimizeController";
    private static final String PA_THERMAL_STATE_FILE = SystemProperties.get("persist.radio.tt_therm_path") + "/temp";
    private Context mContext;
    private int mCurrentPATemperature;
    private final BaseEvent.IEventListener mDisplayListener;
    private HapticFeedbackCompat mHapticCompat;
    private int mInitSatelliteThermalLevel;
    private int mLastScreenState;
    private int mPALimitThermalLevel;
    private PhoneConstants.State mSatelliteCurrentPhoneState;
    private final BroadcastReceiver mSatelliteStateReceiver;
    private int mSatelliteThermalLevel;
    private SatelliteThermalStateListener mSatelliteThermalStateListener;
    private int mTargetThermalLevel;
    private TelephonyCallBack mTelephonyCallBack;

    private int getTargetThermalLevel(int i) {
        return i == 0 ? 2 : 3;
    }

    public static NtnOptimizeControllerBase make() {
        if (!(NtnOptimizeControllerBase.mInstance instanceof NtnOptimizeController)) {
            HandlerThread handlerThread = new HandlerThread("NtnOptimizeController");
            handlerThread.start();
            NtnOptimizeControllerBase.mInstance = new NtnOptimizeController(handlerThread.getLooper());
        }
        return NtnOptimizeControllerBase.mInstance;
    }

    private NtnOptimizeController(Looper looper) {
        super(looper);
        this.mLastScreenState = -1;
        this.mTelephonyCallBack = null;
        this.mContext = null;
        this.mSatelliteThermalStateListener = null;
        this.mInitSatelliteThermalLevel = 0;
        this.mSatelliteThermalLevel = 0;
        this.mTargetThermalLevel = 3;
        this.mPALimitThermalLevel = 10000;
        this.mCurrentPATemperature = -129000;
        this.mSatelliteCurrentPhoneState = PhoneConstants.State.IDLE;
        this.mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.ntn.NtnOptimizeController$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        };
        this.mSatelliteStateReceiver = new BroadcastReceiver() { // from class: com.android.phone.ntn.NtnOptimizeController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) throws Throwable {
                if (intent.getAction().equals(SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE)) {
                    boolean booleanExtra = intent.getBooleanExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, false);
                    NtnOptimizeController ntnOptimizeController = NtnOptimizeController.this;
                    if (booleanExtra) {
                        ntnOptimizeController.registerTelephonyCallBack();
                        NtnOptimizeController.this.processPAThermalOptimizeForSatellite();
                        NtnOptimizeController.this.registerDeviceState();
                    } else {
                        ntnOptimizeController.unRegisterTelephonyCallBack();
                        NtnOptimizeController.this.mSatelliteCurrentPhoneState = PhoneConstants.State.IDLE;
                        NtnOptimizeController.this.removeMessages(3);
                        NtnOptimizeController.this.mCurrentPATemperature = -129000;
                        NtnOptimizeController.this.unregisterDeviceState();
                    }
                    NtnOptimizeController.log("satelliteState = " + booleanExtra);
                }
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        this.mHapticCompat = new HapticFeedbackCompat(this.mContext);
        SatelliteThermalStateListener satelliteThermalStateListener = new SatelliteThermalStateListener("sys/class/thermal/thermal_message/ntn_limit");
        this.mSatelliteThermalStateListener = satelliteThermalStateListener;
        satelliteThermalStateListener.startWatching();
        registerSatelliteState();
    }

    @Override // com.android.phone.ntn.NtnOptimizeControllerBase, android.os.Handler
    public void handleMessage(Message message) throws Throwable {
        NtnPhone ntnPhone;
        super.handleMessage(message);
        int i = message.what;
        if (i == 1) {
            processPhoneStateChanged();
            return;
        }
        if (i == 2) {
            hangupSatellitePhone();
            return;
        }
        if (i == 3) {
            processPAThermalOptimizeForSatellite();
            return;
        }
        if (i != 4) {
            return;
        }
        log("setScreenState  curState = " + message.arg1 + " mLastScreenState = " + this.mLastScreenState);
        if (message.arg1 == this.mLastScreenState || (ntnPhone = getNtnPhone()) == null) {
            return;
        }
        ntnPhone.setScreenState(message.arg1);
        this.mLastScreenState = message.arg1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
            DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
            if (displayEvent.isDefaultDisplay()) {
                if (displayEvent.isScreenOn()) {
                    log("onDisplayChanged to STATE ON");
                    sendScreenStateMsg(1);
                } else if (displayEvent.isScreenOff() && this.mLastScreenState == 1) {
                    log("onDisplayChanged to STATE OFF");
                    sendScreenStateMsg(0);
                }
            }
        }
    }

    private synchronized void sendScreenStateMsg(int i) {
        removeMessages(4);
        sendMessage(obtainMessage(4, i, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            log("unregister TelephonyCallBack");
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                NtnOptimizeController.log("onPreciseCallStateChanged call state : " + i2);
                NtnOptimizeController.this.removeMessages(1);
                NtnOptimizeController ntnOptimizeController = NtnOptimizeController.this;
                ntnOptimizeController.sendMessageDelayed(ntnOptimizeController.obtainMessage(1, Integer.valueOf(i)), 500L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDeviceState() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        log("registerDeviceState");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterDeviceState() {
        EventHubManager.getInstance().unregisterListener(this.mDisplayListener);
    }

    private void registerSatelliteState() {
        if (this.mContext == null) {
            log("registerSatelliteObserver, context is null");
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE);
        this.mContext.registerReceiver(this.mSatelliteStateReceiver, intentFilter, 2);
    }

    private void processPhoneStateChanged() {
        PhoneConstants.State state;
        NtnPhone ntnPhone = getNtnPhone();
        if (ntnPhone == null) {
            return;
        }
        PhoneConstants.State state2 = ntnPhone.getState();
        log("processPhoneStateChanged : currentPhoneState = " + this.mSatelliteCurrentPhoneState + ", newPhoneState = " + state2);
        if (this.mSatelliteThermalLevel >= this.mTargetThermalLevel && (state = this.mSatelliteCurrentPhoneState) == PhoneConstants.State.IDLE && state != state2) {
            if (!hasMessages(2)) {
                showToast(this.mContext.getResources().getString(R.string.satellite_thermal_hangup_delay, 30));
                sendMessageDelayed(obtainMessage(2), 30000L);
            }
        } else {
            PhoneConstants.State state3 = this.mSatelliteCurrentPhoneState;
            PhoneConstants.State state4 = PhoneConstants.State.IDLE;
            if (state3 != state4 && state2 == state4) {
                removeMessages(2);
                int i = this.mSatelliteThermalLevel;
                this.mInitSatelliteThermalLevel = i;
                this.mTargetThermalLevel = getTargetThermalLevel(i);
            }
        }
        this.mSatelliteCurrentPhoneState = state2;
    }

    private void hangupSatellitePhone() {
        NtnPhone ntnPhone = getNtnPhone();
        if (ntnPhone == null) {
            return;
        }
        log("hangupSatellitePhone : satelliteThermalLevel = " + this.mSatelliteThermalLevel + ", currentPhoneState = " + this.mSatelliteCurrentPhoneState);
        if (this.mSatelliteThermalLevel < this.mTargetThermalLevel || this.mSatelliteCurrentPhoneState == PhoneConstants.State.IDLE) {
            return;
        }
        try {
            ntnPhone.rejectCall();
            NtnPhoneInfoMonitor.getInstance().setHangUpReason("1");
        } catch (CallStateException unused) {
            log("rejectCall fail");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processThermalPowerOptimizeForSatellite() throws Throwable {
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    FileInputStream fileInputStream2 = new FileInputStream(new File("sys/class/thermal/thermal_message/ntn_limit"));
                    try {
                        String strTrim = new String(readInputStream(fileInputStream2)).trim();
                        int i = Integer.parseInt(strTrim);
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("value = " + strTrim);
                        if (i < 10000) {
                            stringBuffer.append(", mSatelliteThermalLevel = " + this.mSatelliteThermalLevel + ", newThermalLevel = " + i);
                            this.mSatelliteThermalLevel = i;
                            PhoneConstants.State state = this.mSatelliteCurrentPhoneState;
                            PhoneConstants.State state2 = PhoneConstants.State.IDLE;
                            if (state == state2) {
                                this.mInitSatelliteThermalLevel = i;
                                this.mTargetThermalLevel = getTargetThermalLevel(i);
                                stringBuffer.append(", mInitSatelliteThermalLevel = " + this.mInitSatelliteThermalLevel + ", mTargetThermalLevel = " + this.mTargetThermalLevel);
                            }
                            if (this.mSatelliteThermalLevel >= this.mTargetThermalLevel && this.mSatelliteCurrentPhoneState != state2 && !hasMessages(2)) {
                                showToast(this.mContext.getResources().getString(R.string.satellite_thermal_hangup_delay, 30));
                                sendMessageDelayed(obtainMessage(2), 30000L);
                            }
                        } else if (i < 20000) {
                            stringBuffer.append(", mPALimitThermalLevel = " + this.mPALimitThermalLevel + ", newThermalLevel = " + i);
                            this.mPALimitThermalLevel = i;
                            if (SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isSatelliteEnabled() && isSatellitePaMegathermal()) {
                                vibrateWhenPaCloseSatellite();
                                Intent intent = new Intent(this.mContext, (Class<?>) SatelliteDialogActivity.class);
                                intent.setFlags(402718720);
                                intent.putExtra("type", 11);
                                this.mContext.startActivity(intent);
                            }
                        }
                        log(stringBuffer.toString());
                        fileInputStream2.close();
                    } catch (Exception e) {
                        e = e;
                        fileInputStream = fileInputStream2;
                        log("read temp state failed" + e);
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException unused) {
                                log("fileInputStream close failed");
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (IOException unused2) {
            log("fileInputStream close failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:57:0x009c A[EXC_TOP_SPLITTER, PHI: r8
  0x009c: PHI (r8v5 java.io.FileInputStream) = (r8v25 java.io.FileInputStream), (r8v26 java.io.FileInputStream), (r8v27 java.io.FileInputStream) binds: [B:33:0x009a, B:39:0x00b5, B:41:0x00c5] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v19 */
    /* JADX WARN: Type inference failed for: r8v2, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r8v20 */
    /* JADX WARN: Type inference failed for: r8v21 */
    /* JADX WARN: Type inference failed for: r8v22 */
    /* JADX WARN: Type inference failed for: r8v23 */
    /* JADX WARN: Type inference failed for: r8v24 */
    /* JADX WARN: Type inference failed for: r8v7 */
    /* JADX WARN: Type inference failed for: r8v8 */
    public void processPAThermalOptimizeForSatellite() throws Throwable {
        FileInputStream fileInputStream;
        ?? r0 = "PA thermal fileInputStream close failed";
        ?? r8 = 0;
        FileInputStream fileInputStream2 = null;
        FileInputStream fileInputStream3 = null;
        FileInputStream fileInputStream4 = null;
        try {
            try {
                FileInputStream fileInputStream5 = new FileInputStream(new File(PA_THERMAL_STATE_FILE));
                try {
                    int iIntValue = Integer.valueOf(new String(readInputStream(fileInputStream5)).trim()).intValue();
                    log("newPATemperature = " + iIntValue + ", currentPATemperature = " + this.mCurrentPATemperature);
                    if (iIntValue >= 0 && iIntValue <= 127000 && Math.abs(iIntValue - this.mCurrentPATemperature) >= 2000) {
                        this.mCurrentPATemperature = iIntValue;
                        sendPATemp(iIntValue);
                    }
                    try {
                        fileInputStream5.close();
                    } catch (IOException unused) {
                        log("PA thermal fileInputStream close failed");
                    }
                    Message messageObtainMessage = obtainMessage(3);
                    sendMessageDelayed(messageObtainMessage, 1500L);
                    r0 = messageObtainMessage;
                    r8 = iIntValue;
                } catch (FileNotFoundException e) {
                    e = e;
                    fileInputStream2 = fileInputStream5;
                    log("read PA thermal state failed." + e);
                    r0 = r0;
                    r8 = fileInputStream2;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream.close();
                            r0 = r0;
                            r8 = fileInputStream;
                        } catch (IOException unused2) {
                            log("PA thermal fileInputStream close failed");
                            r0 = r0;
                            r8 = fileInputStream;
                        }
                    }
                } catch (NumberFormatException e2) {
                    e = e2;
                    fileInputStream3 = fileInputStream5;
                    log("Conversion of String type to int type failed." + e);
                    r0 = r0;
                    r8 = fileInputStream3;
                    if (fileInputStream3 != null) {
                        fileInputStream = fileInputStream3;
                        fileInputStream.close();
                        r0 = r0;
                        r8 = fileInputStream;
                    }
                } catch (Exception e3) {
                    e = e3;
                    fileInputStream4 = fileInputStream5;
                    log("processPAThermalOptimizeForSatellite exception: " + e);
                    e.printStackTrace();
                    r0 = r0;
                    r8 = fileInputStream4;
                    fileInputStream = fileInputStream4;
                    if (fileInputStream4 != null) {
                        fileInputStream.close();
                        r0 = r0;
                        r8 = fileInputStream;
                    }
                } catch (Throwable th) {
                    th = th;
                    r8 = fileInputStream5;
                    if (r8 != 0) {
                        try {
                            r8.close();
                        } catch (IOException unused3) {
                            log(r0);
                        }
                    }
                    sendMessageDelayed(obtainMessage(3), 1500L);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (FileNotFoundException e4) {
            e = e4;
        } catch (NumberFormatException e5) {
            e = e5;
        } catch (Exception e6) {
            e = e6;
        }
    }

    class SatelliteThermalStateListener extends FileObserver {
        public SatelliteThermalStateListener(String str) {
            super(str);
        }

        @Override // android.os.FileObserver
        public void onEvent(int i, String str) throws Throwable {
            if (i != 2) {
                return;
            }
            NtnOptimizeController.this.processThermalPowerOptimizeForSatellite();
        }
    }

    private NtnPhone getNtnPhone() {
        try {
            NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
            if (defaultPhone == null) {
                return null;
            }
            return defaultPhone;
        } catch (Exception e) {
            log("getNtnPhone " + e);
            return null;
        }
    }

    @Override // com.android.phone.ntn.NtnOptimizeControllerBase
    public boolean isSatelliteInThremalState() {
        return Settings.System.getInt(this.mContext.getContentResolver(), "satellite_state", 0) == 1 && this.mSatelliteThermalLevel >= this.mTargetThermalLevel;
    }

    private void sendPATemp(int i) {
        NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
        if (defaultPhone == null) {
            log("sendPATemp fail");
        } else {
            defaultPhone.sendTemp(i);
        }
    }

    private void showToast(final String str) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.ntn.NtnOptimizeController.2
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(NtnOptimizeController.this.mContext, str, 1).show();
            }
        });
    }

    private static byte[] readInputStream(FileInputStream fileInputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[512];
        while (true) {
            try {
                try {
                    int i = fileInputStream.read(bArr, 0, 512);
                    if (i > 0) {
                        byteArrayOutputStream.write(bArr, 0, i);
                    } else {
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        try {
                            byteArrayOutputStream.close();
                            return byteArray;
                        } catch (IOException e) {
                            log("readInputStream " + e);
                            return byteArray;
                        }
                    }
                } catch (Exception e2) {
                    log("readInputStream " + e2);
                    try {
                        byteArrayOutputStream.close();
                        return null;
                    } catch (IOException e3) {
                        log("readInputStream " + e3);
                        return null;
                    }
                }
            } catch (Throwable th) {
                byteArrayOutputStream.close();
                throw th;
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e4) {
                log("readInputStream " + e4);
            }
            throw th;
        }
    }

    private void vibrateWhenPaCloseSatellite() {
        int i = Integer.parseInt(MiuiSettings.Telephony.getVibrateKey(this.mContext.getContentResolver()));
        if (i <= 0 || !this.mHapticCompat.supportLinearMotor()) {
            return;
        }
        boolean zDoesSupportHaptic = HapticCompat.doesSupportHaptic("2.0");
        log("Linear motor device, use miui haptic feedback " + zDoesSupportHaptic);
        HapticFeedbackCompat hapticFeedbackCompat = this.mHapticCompat;
        if (zDoesSupportHaptic) {
            hapticFeedbackCompat.performExtHapticFeedback(206, true);
        } else {
            hapticFeedbackCompat.performHapticFeedback(HapticFeedbackConstants.MIUI_LONG_PRESS, i > 50 ? 2 : 0, true);
        }
    }

    @Override // com.android.phone.ntn.NtnOptimizeControllerBase
    public boolean isSatellitePaMegathermal() {
        return 10001 == this.mPALimitThermalLevel;
    }

    protected static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
