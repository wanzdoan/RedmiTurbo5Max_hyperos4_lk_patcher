package com.android.phone.satellite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.SatelliteManager;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteCitManager extends Handler {
    private static Context mContext;
    private static SatelliteCitManager sInstance;
    private SatelliteManager mSatelliteManager;
    private BroadcastReceiver mSimStateReceiver;

    public static SatelliteCitManager make(SatelliteManager satelliteManager, Context context) {
        if (sInstance == null) {
            sInstance = new SatelliteCitManager(satelliteManager, context);
        }
        return sInstance;
    }

    public SatelliteCitManager(SatelliteManager satelliteManager, Context context) {
        super(SatelliteManager.getNtnLooper());
        this.mSatelliteManager = satelliteManager;
        mContext = context;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        logd("handleMessage :" + message.what);
        int i = message.what;
        if (i == 1) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            try {
                NtnPhone ntnPhoneWithFallback = getNtnPhoneWithFallback();
                if (ntnPhoneWithFallback == null) {
                    loge("CIT_RADIO_STATE_CHANGED: no ntnPhone available");
                    return;
                } else {
                    if (ntnPhoneWithFallback.mNtnRil.getRadioState() != 2) {
                        loge("CIT_RADIO_STATE_CHANGED radio is available.");
                        ((CountDownLatch) asyncResult.userObj).countDown();
                        return;
                    }
                    return;
                }
            } catch (Exception e) {
                loge("CIT_RADIO_STATE_CHANGED err : " + e);
                return;
            }
        }
        if (i == 2) {
            ResultReceiver resultReceiver = (ResultReceiver) ((AsyncResult) message.obj).userObj;
            try {
                NtnPhone ntnPhoneWithFallback2 = getNtnPhoneWithFallback();
                if (ntnPhoneWithFallback2 == null) {
                    loge("RADIO_STATE_CHANGED: no ntnPhone available");
                    sendErrorToListener(resultReceiver, "no ntnPhone available");
                    return;
                } else if (ntnPhoneWithFallback2.mNtnRil.getRadioState() != 2) {
                    loge("RADIO_STATE_CHANGED radio is available.");
                    sendResultToListener(resultReceiver, "OK");
                    return;
                } else {
                    loge("RADIO_STATE_CHANGED radio is unavailable.");
                    return;
                }
            } catch (Exception e2) {
                loge("RADIO_STATE_CHANGED err : " + e2);
                sendErrorToListener(resultReceiver, e2.getMessage());
                return;
            }
        }
        if (i == 4) {
            NtnPhone ntnPhoneWithFallback3 = getNtnPhoneWithFallback();
            if (ntnPhoneWithFallback3 == null) {
                loge("EVENT_RADIO_POWER_STATE_ON: no ntnPhone available");
                return;
            } else {
                ntnPhoneWithFallback3.initNtnRadio();
                return;
            }
        }
        if (i == 5) {
            AsyncResult asyncResult2 = (AsyncResult) message.obj;
            ResultReceiver resultReceiver2 = (ResultReceiver) asyncResult2.userObj;
            logd("ar.userObj :" + asyncResult2.userObj + "; callback :" + resultReceiver2 + ";");
            try {
                NtnPhone ntnPhoneWithFallback4 = getNtnPhoneWithFallback();
                if (ntnPhoneWithFallback4 == null) {
                    loge("EVENT_RADIO_POWER_STATE_OFF: no ntnPhone available");
                    sendErrorToListener(resultReceiver2, "no ntnPhone available");
                    return;
                } else {
                    ntnPhoneWithFallback4.destroyNtnRadio();
                    NtnPhoneFactory.destroyDefaultPhoneForCitTest();
                    logd("power off satellite for cit success!");
                    sendResultToListener(resultReceiver2, "OK");
                    return;
                }
            } catch (Exception e3) {
                loge("EVENT_RADIO_POWER_STATE_OFF err : " + e3);
                sendErrorToListener(resultReceiver2, e3.getMessage());
                return;
            }
        }
        if (i == 6) {
            AsyncResult asyncResult3 = (AsyncResult) message.obj;
            ResultReceiver resultReceiver3 = (ResultReceiver) asyncResult3.userObj;
            logd("ar.userObj :" + asyncResult3.userObj + "; callback :" + resultReceiver3 + ";");
            sendResultToListener(resultReceiver3, "OK");
            return;
        }
        if (i != 7) {
            return;
        }
        AsyncResult asyncResult4 = (AsyncResult) message.obj;
        ResultReceiver resultReceiver4 = (ResultReceiver) asyncResult4.userObj;
        logd("ar.userObj :" + asyncResult4.userObj + "; callback :" + resultReceiver4 + ";");
        if (asyncResult4.exception == null) {
            Log.i("SatelliteCitManager", "EVENT_GET_CIT_RESULT_DONE : res = " + ((String) asyncResult4.result));
            sendResultToListener(resultReceiver4, (String) asyncResult4.result);
            return;
        }
        Log.i("SatelliteCitManager", "EVENT_GET_CIT_RESULT_DONE : exception = " + asyncResult4.exception);
        sendErrorToListener(resultReceiver4, asyncResult4.exception.getMessage());
    }

    public void setSimPowerStateForCit(int i) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService("phone");
        for (Phone phone : MiuiPhoneUtils.getPhones()) {
            telephonyManager.setSimPowerStateForSlot(phone.getPhoneId(), i);
        }
    }

    public String citRequest(final int i, final int i2, final String str) {
        Log.i("SatelliteCitManager", "citRequest slotId: " + i + ", vendorType: " + i2 + ", ctCmd: " + str);
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.satellite.SatelliteCitManager.1
            @Override // java.lang.Runnable
            public void run() {
                Log.i("SatelliteCitManager", "citRequest getNtnPhone");
                try {
                    NtnPhoneFactory.makeDefaultPhone(SatelliteCitManager.mContext, i);
                    NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
                    if (TelephonyCommonUtils.isNtnChipVendor_54()) {
                        SatelliteCitManager.this.setSimPowerStateForCit(0);
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException unused) {
                        }
                        SystemProperties.set("persist.radio.ntn_stack_on", i == 0 ? "61" : "63");
                        defaultPhone.setPowerSwitch(true);
                        SatelliteCitManager.this.mSatelliteManager.getUIController().sendEnableSatelliteBroadcast(i, true, false);
                    }
                    defaultPhone.initNtnRadio();
                } catch (Exception unused2) {
                    Log.e("SatelliteCitManager", "citRequest getDefaultPhone fail!");
                }
            }
        });
        CompletableFuture completableFutureSupplyAsync = CompletableFuture.supplyAsync(new Supplier() { // from class: com.android.phone.satellite.SatelliteCitManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return this.f$0.lambda$citRequest$0(i, i2, str);
            }
        });
        try {
            Log.i("SatelliteCitManager", "future.get() = " + ((String) completableFutureSupplyAsync.get()));
            return (String) completableFutureSupplyAsync.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.i("SatelliteCitManager", "Cit return null!");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$citRequest$0(int i, int i2, String str) {
        Log.i("SatelliteCitManager", "CompletableFuture: supplyAsync");
        try {
            Thread.sleep(500L);
        } catch (InterruptedException unused) {
        }
        NtnPhone defaultNtnPhone = SatelliteManager.getDefaultNtnPhone();
        if (defaultNtnPhone == null || defaultNtnPhone.getPhoneId() != i) {
            Log.i("SatelliteCitManager", "ntnphone is invalild, return");
            return null;
        }
        if (TelephonyCommonUtils.isNtnChipVendor_54()) {
            boolean z = true;
            if (defaultNtnPhone.mNtnRil.getRadioState() != 1) {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                defaultNtnPhone.mNtnRil.registerForRadioStateChanged(this, 1, countDownLatch);
                try {
                    if (!countDownLatch.await(10000L, TimeUnit.MILLISECONDS)) {
                        Log.i("SatelliteCitManager", "wait for radio available timeout");
                        z = false;
                    }
                } catch (InterruptedException unused2) {
                    Log.i("SatelliteCitManager", "citRequest interrupted exception");
                }
                defaultNtnPhone.mNtnRil.unregisterForRadioStateChanged(this);
            }
            if (!z) {
                Log.i("SatelliteCitManager", "ntn radio is unavailable, return");
                return null;
            }
        }
        Log.i("SatelliteCitManager", "send cit request");
        return defaultNtnPhone.sendCitRequest(i2, str);
    }

    public void stopCitTest() {
        NtnPhone ntnPhoneWithFallback = getNtnPhoneWithFallback();
        if (ntnPhoneWithFallback == null) {
            loge("stopCitTest: no ntnPhone available");
            return;
        }
        if (TelephonyCommonUtils.isNtnChipVendor_54()) {
            ntnPhoneWithFallback.shutdownRadio();
            this.mSatelliteManager.getUIController().sendEnableSatelliteBroadcast(ntnPhoneWithFallback.getPhoneId(), false, false);
        } else {
            ntnPhoneWithFallback.destroyNtnRadio();
            NtnPhoneFactory.destroyDefaultPhoneForCitTest();
        }
        TelephonyCommonUtils.resetNtnStackPropertyValue();
        setSimPowerStateForCit(1);
        this.mSatelliteManager.getController().setExitSatelliteInProgress(false);
        if (SystemProperties.getInt("persist.miui.cit.satellite_mode", -1) == 1) {
            SystemProperties.set("persist.miui.cit.satellite_mode", Integer.toString(0));
        }
    }

    public void sendCitRequest(int i, int i2, String str, ResultReceiver resultReceiver) {
        Log.i("SatelliteCitManager", "citRequest slotId: " + i + ", vendorType: " + i2 + ", atCmd: " + str);
        if (isCitRadioPowerCmd(str)) {
            setCitRadioPower(i, i2, str, resultReceiver);
            return;
        }
        if (isCitSimPowerCmd(str)) {
            setCitSimPower(i, i2, str, resultReceiver);
            return;
        }
        if (isCitCellurlarSimPowerCmd(str)) {
            setCitCellurlarSimPowerCmd(i, i2, str, resultReceiver);
            return;
        }
        NtnPhone ntnPhoneWithFallback = getNtnPhoneWithFallback();
        if (ntnPhoneWithFallback == null) {
            loge("sendCitRequest: no ntnPhone available");
            sendErrorToListener(resultReceiver, "satellite init fail!");
        } else {
            ntnPhoneWithFallback.mNtnRil.sendCustFactAtcmd(i2, str, obtainMessage(7, resultReceiver));
        }
    }

    public boolean isCitCellurlarSimPowerCmd(String str) {
        return "AT+CELLULARSIMPOWERON".equals(str) || "AT+CELLULARSIMPOWEROFF".equals(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r2v9 */
    public void setCitCellurlarSimPowerCmd(final int i, int i2, String str, final ResultReceiver resultReceiver) {
        final ?? r2;
        if ("AT+CELLULARSIMPOWERON".equals(str)) {
            r2 = 1;
        } else {
            if (!"AT+CELLULARSIMPOWEROFF".equals(str)) {
                Log.i("SatelliteCitManager", "setCitCellurlarSimPowerCmd command error!" + str);
                return;
            }
            r2 = 0;
        }
        setSimPowerStateForCit(r2);
        this.mSimStateReceiver = new BroadcastReceiver() { // from class: com.android.phone.satellite.SatelliteCitManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) {
                    SatelliteCitManager.loge("received null intent or action");
                    return;
                }
                String action = intent.getAction();
                SatelliteCitManager.logd("onReceive action = " + action);
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    int slotIdExtra = SubscriptionManager.getSlotIdExtra(intent, -1);
                    if (slotIdExtra == -1 || i != slotIdExtra) {
                        SatelliteCitManager.logd("satellite phone id is invalid, or not current slotId." + slotIdExtra + "," + i);
                        return;
                    }
                    SatelliteCitManager.this.processSimPowerResult(intent.getStringExtra("ss"), r2, resultReceiver);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        mContext.registerReceiver(this.mSimStateReceiver, intentFilter, 2);
    }

    public boolean isCitRadioPowerCmd(String str) {
        return "AT+POWERON".equals(str) || "AT+POWEROFF".equals(str);
    }

    public void setCitRadioPower(int i, int i2, String str, ResultReceiver resultReceiver) {
        Boolean radioPowerCommand = parseRadioPowerCommand(str);
        if (radioPowerCommand == null) {
            Log.i("SatelliteCitManager", "setCitRadioPower command error!" + str);
        } else if (radioPowerCommand.booleanValue()) {
            handleRadioPowerOn(i, i2, resultReceiver);
        } else {
            handleRadioPowerOff(i, i2, resultReceiver);
        }
    }

    private Boolean parseRadioPowerCommand(String str) {
        if ("AT+POWERON".equals(str)) {
            return Boolean.TRUE;
        }
        if ("AT+POWEROFF".equals(str)) {
            return Boolean.FALSE;
        }
        return null;
    }

    private void handleRadioPowerOn(int i, int i2, ResultReceiver resultReceiver) {
        NtnPhoneFactory.makeDefaultPhone(mContext, i);
        NtnPhone ntnPhoneWithFallback = getNtnPhoneWithFallback();
        setNtnStackProperty(i2, i, true);
        if (ntnPhoneWithFallback == null) {
            loge("handleRadioPowerOn: no ntnPhone available");
            sendErrorToListener(resultReceiver, "start ntn stack fail!");
        } else {
            ntnPhoneWithFallback.mNtnRil.setPowerSwitch(true, obtainMessage(4));
            ntnPhoneWithFallback.mNtnRil.registerForRadioStateChanged(this, 2, resultReceiver);
        }
    }

    private void handleRadioPowerOff(int i, int i2, ResultReceiver resultReceiver) {
        NtnPhone ntnPhoneWithFallback = getNtnPhoneWithFallback();
        if (ntnPhoneWithFallback == null) {
            loge("handleRadioPowerOff: no ntnPhone available");
            sendErrorToListener(resultReceiver, "satellite init fail!");
            return;
        }
        setNtnStackProperty(i2, i, false);
        ntnPhoneWithFallback.mNtnRil.unregisterForRadioStateChanged(this);
        ntnPhoneWithFallback.mNtnRil.setPowerSwitch(false, obtainMessage(5, resultReceiver));
        NtnPhoneFactory.BackToCellularForCitTest();
        TelephonyCommonUtils.resetNtnStackPropertyValue();
    }

    private void setNtnStackProperty(int i, int i2, boolean z) {
        String str;
        if (i == 1) {
            if (z) {
                str = i2 == 0 ? "29" : "31";
            } else {
                str = i2 == 0 ? "28" : "30";
            }
        } else {
            if (i != 2) {
                return;
            }
            if (z) {
                str = i2 == 0 ? "89" : "91";
            } else {
                str = i2 == 0 ? "88" : "90";
            }
        }
        SystemProperties.set("persist.radio.ntn_stack_on", str);
    }

    public boolean isCitSimPowerCmd(String str) {
        return "AT+SIMPOWERON".equals(str) || "AT+SIMPOWEROFF".equals(str);
    }

    public void setCitSimPower(int i, int i2, String str, ResultReceiver resultReceiver) {
        boolean z;
        if ("AT+SIMPOWERON".equals(str)) {
            z = true;
        } else {
            if (!"AT+SIMPOWEROFF".equals(str)) {
                sendErrorToListener(resultReceiver, "this is not a power cmd: " + str + "!");
                return;
            }
            z = false;
        }
        NtnPhone ntnPhoneWithFallback = getNtnPhoneWithFallback();
        if (ntnPhoneWithFallback == null) {
            loge("setCitSimPower: no ntnPhone available");
            sendErrorToListener(resultReceiver, "start ntn stack fail!");
        } else {
            ntnPhoneWithFallback.mNtnRil.setSimCardPower(z, obtainMessage(6, resultReceiver));
            this.mSatelliteManager.getUIController().sendEnableSatelliteBroadcast(i, z, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSimPowerResult(String str, boolean z, ResultReceiver resultReceiver) {
        logd("processSimPowerResult sim status is " + str);
        if (!z && "ABSENT".equals(str)) {
            sendResultToListener(resultReceiver, "OK");
            BroadcastReceiver broadcastReceiver = this.mSimStateReceiver;
            if (broadcastReceiver != null) {
                mContext.unregisterReceiver(broadcastReceiver);
                this.mSimStateReceiver = null;
                return;
            }
            return;
        }
        if (z && "READY".equals(str)) {
            sendResultToListener(resultReceiver, "OK");
            BroadcastReceiver broadcastReceiver2 = this.mSimStateReceiver;
            if (broadcastReceiver2 != null) {
                mContext.unregisterReceiver(broadcastReceiver2);
                this.mSimStateReceiver = null;
                return;
            }
            return;
        }
        logd("processSimPowerResult sim status is " + str);
    }

    private NtnPhone getNtnPhoneWithFallback() {
        try {
            NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
            if (defaultPhone != null) {
                return defaultPhone;
            }
        } catch (IllegalStateException e) {
            loge("getDefaultPhone fail, fallback to getNtnPhone: " + e);
        }
        return NtnPhoneFactory.getNtnPhone();
    }

    public void sendResultToListener(ResultReceiver resultReceiver, String str) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("satellite_is_cit_request_success", true);
        bundle.putString("satellite_request_cit_result", str);
        resultReceiver.send(0, bundle);
    }

    public void sendErrorToListener(ResultReceiver resultReceiver, String str) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("satellite_is_cit_request_success", false);
        bundle.putString("satellite_request_cit_result", str);
        resultReceiver.send(1, bundle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("SatelliteCitManager", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("SatelliteCitManager", str);
    }
}
