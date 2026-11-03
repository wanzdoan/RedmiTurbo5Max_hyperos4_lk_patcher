package com.android.phone;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.internal.telephony.ntnphone.SatelliteConfigManager;
import com.android.phone.firewall.model.AppMonitor;
import com.android.phone.ntn.NtnLocationManager;
import com.android.phone.satellite.SatelliteCitManager;
import com.android.phone.satellite.SatelliteController;
import com.android.phone.satellite.SatelliteDataController;
import com.android.phone.satellite.SatelliteOTAManager;
import com.android.phone.satellite.SatelliteSIMController;
import com.android.phone.satellite.SatelliteToCellularTracker;
import com.android.phone.satellite.SatelliteUIController;
import com.android.services.telephony.SatelliteCoreModule;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteManager extends SatelliteManagerBase {
    private static String LOG_TAG = "SatelliteManager";
    private static Context mContext = null;
    private static boolean mIsSatelliteSignalBoostEnabled = false;
    private SatelliteCitManager mCitManager;
    private SatelliteController mController;
    private SatelliteDataController mDataController;
    private SatelliteOTAManager mOTAManager;
    private SatelliteSIMController mSIMController;
    private SatelliteUIController mUiController;
    protected RegistrantList mSatelliteConfigRegistrantList = new RegistrantList();
    private volatile boolean mHeavyInitialized = false;
    private SatelliteConfigManager.ConfigUpdateCallback mConfigUpdatedCallback = new SatelliteConfigManager.ConfigUpdateCallback() { // from class: com.android.phone.SatelliteManager.1
        public void onConfigUpdate(Map map) {
            SatelliteManager.logd("onConfigUpdate");
            SatelliteManager.this.handleSatelliteConfigUpdate(map);
            RegistrantList registrantList = SatelliteManager.this.mSatelliteConfigRegistrantList;
            if (registrantList == null || map == null) {
                return;
            }
            registrantList.notifyRegistrants();
        }
    };

    @Override // com.android.phone.SatelliteManagerBase
    public int getSatelliteState(int i) {
        return -1;
    }

    public static Looper getNtnLooper() {
        return Looper.getMainLooper();
    }

    public static SatelliteManagerBase make() {
        if (!(SatelliteManagerBase.mInstance instanceof SatelliteManager)) {
            SatelliteManagerBase.mInstance = new SatelliteManager();
        }
        return SatelliteManagerBase.mInstance;
    }

    private SatelliteManager() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        mContext = phoneGlobals;
        this.mUiController = SatelliteUIController.make(phoneGlobals);
        this.mController = SatelliteController.make(this, mContext);
    }

    public void ensureHeavyInitialized() {
        if (this.mHeavyInitialized) {
            return;
        }
        synchronized (this) {
            try {
                if (this.mHeavyInitialized) {
                    return;
                }
                logd("ensureHeavyInitialized start");
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    doHeavyInit();
                } else {
                    final CountDownLatch countDownLatch = new CountDownLatch(1);
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.SatelliteManager$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$ensureHeavyInitialized$0(countDownLatch);
                        }
                    });
                    try {
                        if (!countDownLatch.await(10L, TimeUnit.SECONDS)) {
                            loge("ensureHeavyInitialized timed out after 10s");
                        }
                    } catch (InterruptedException e) {
                        loge("ensureHeavyInitialized interrupted: " + e);
                    }
                }
                logd("ensureHeavyInitialized done, mHeavyInitialized=" + this.mHeavyInitialized);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$ensureHeavyInitialized$0(CountDownLatch countDownLatch) {
        doHeavyInit();
        countDownLatch.countDown();
    }

    private void doHeavyInit() {
        logd("doHeavyInit start on thread=" + Thread.currentThread().getName());
        SatelliteCoreModule.wireHeavyDelegates(SatelliteDelegateImpl.init());
        initNtnPhone();
        this.mDataController = SatelliteDataController.make(this, mContext);
        this.mSIMController = SatelliteSIMController.make(this, mContext);
        this.mOTAManager = SatelliteOTAManager.make(this, mContext);
        this.mCitManager = SatelliteCitManager.make(this, mContext);
        if (MiuiPhoneUtils.isTiantongSatelliteDataSupported()) {
            AppMonitor.getInstance();
        }
        this.mHeavyInitialized = true;
        logd("doHeavyInit done");
    }

    private void initNtnPhone() {
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
            NtnPhoneFactory.makeDefaultPhone(mContext, 0);
            registerSatelliteConfigUpdate();
        }
    }

    private void registerSatelliteConfigUpdate() {
        NtnPhone ntnPhone;
        logd("registerSatelliteConfigUpdate");
        try {
            ntnPhone = (NtnPhone) NtnPhoneFactory.getNtnPhone();
        } catch (Exception e) {
            loge("registerSatelliteConfigUpdate: " + e);
            ntnPhone = null;
        }
        if (ntnPhone == null) {
            logd("registerSatelliteConfigUpdate return");
        } else {
            ntnPhone.getSatelliteConfigManager().registerConfigUpdateCallback(this.mConfigUpdatedCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSatelliteConfigUpdate(Map map) {
        if (map == null) {
            logd("handleSatelliteConfigUpdate configs is null");
        } else if (isSatelliteAndCellularDualStandbySupported()) {
            SatelliteToCellularTracker.make(mContext);
        }
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isHardSimSwapForTT() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return false;
        }
        return defaultNtnPhone.isHardSimSwapForTT();
    }

    public boolean isSatelliteAndCellularDualStandbySupported() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return false;
        }
        return defaultNtnPhone.isSatelliteAndCellularDualStandbySupported();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean setSatelliteEnable(int i, boolean z) {
        return this.mController.setSatelliteEnable(i, z);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isSatelliteSignalBoostEnabled() {
        return mIsSatelliteSignalBoostEnabled;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void setSatelliteSignalBoostEnable(boolean z, int i) {
        logd("setSatelliteSignalBoostEnable : " + z + ", flag = " + i);
        mIsSatelliteSignalBoostEnabled = z;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void closeSatellite(int i, int i2) {
        this.mController.closeSatellite(i, i2);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isSatelliteEnabled() {
        return this.mController.isSatelliteEnabled();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isHighPenetrationEnabled() {
        return this.mController.isHighPenetrationEnabled();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getEnabledSatelliteSlotId() {
        return this.mController.getEnabledSatelliteSlotId();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void setSatelliteEnableByUser(Context context, boolean z, int i) {
        ensureHeavyInitialized();
        this.mController.setSatelliteEnableByUser(context, z, i);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void setSatelliteDataEnableByUser(Context context, boolean z) {
        SatelliteDataController satelliteDataController = this.mDataController;
        if (satelliteDataController != null) {
            satelliteDataController.setSatelliteDataEnableByUser(context, z);
        }
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isSatelliteDataEnabled() {
        SatelliteDataController satelliteDataController = this.mDataController;
        return satelliteDataController != null && satelliteDataController.isSatelliteDataEnabled();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isPrivacyStatementAgree() {
        return this.mUiController.isPrivacyStatementAgree();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void setSarMode(int i) {
        try {
            NtnPhoneFactory.getDefaultPhone().setSarMode(i);
        } catch (Exception e) {
            loge("setSarMode: " + e);
        }
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void getSarMode() {
        try {
            NtnPhoneFactory.getDefaultPhone().getSarMode();
        } catch (Exception e) {
            loge("getSarMode: " + e);
        }
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void sendEnableSatelliteBroadcast(int i, boolean z, boolean z2) {
        this.mUiController.sendEnableSatelliteBroadcast(i, z, z2);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void sendSatelliteBusinessEntranceDot() {
        this.mController.sendSatelliteBusinessEntranceDot();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void setCalibrationInformation(Bundle bundle) {
        this.mController.setCalibrationInformation(bundle);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isCalibrated() {
        return this.mController.isCalibrated();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public float getAltitude() {
        return this.mController.getAltitude();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getRealTxPwr() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return -1;
        }
        return defaultNtnPhone.getRealTxPwr();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getTarTxPwr() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return -1;
        }
        return defaultNtnPhone.getTarTxPwr();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getRetRssi() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return -1;
        }
        return defaultNtnPhone.getRetRssi();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getRetSnr() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return -1;
        }
        return defaultNtnPhone.getRetSnr();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getNtnBeamId() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return -1;
        }
        return defaultNtnPhone.getNtnBeamId();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public String getSpeechBitrate() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return null;
        }
        return defaultNtnPhone.getSpeechBitrate();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getNtnBandId() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            return -1;
        }
        return defaultNtnPhone.getNtnBandId();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getSatId() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null || defaultNtnPhone.getNtnTtlogInfo() == null) {
            return -1;
        }
        return defaultNtnPhone.getNtnTtlogInfo().sat_id;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getFreqOff() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null || defaultNtnPhone.getNtnTtlogInfo() == null) {
            return 0;
        }
        return defaultNtnPhone.getNtnTtlogInfo().foffset;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getFer() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null || defaultNtnPhone.getNtnTtlogInfo() == null) {
            return 0;
        }
        int i = defaultNtnPhone.getNtnTtlogInfo().rTotal;
        int i2 = defaultNtnPhone.getNtnTtlogInfo().rError;
        if (i == 0) {
            return 100;
        }
        return (i2 / i) * 100;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public String getNtnPosInfo() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null || defaultNtnPhone.getNtnPosInfo() == null) {
            return null;
        }
        return Integer.toString(defaultNtnPhone.getNtnPosInfo().beam_id);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getErrorClass() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null || defaultNtnPhone.getHlsErrorInfo() == null) {
            return -1;
        }
        return defaultNtnPhone.getHlsErrorInfo().class_info;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getErrorCause() {
        NtnPhone defaultNtnPhone = getDefaultNtnPhone();
        if (defaultNtnPhone == null || defaultNtnPhone.getHlsErrorInfo() == null) {
            return -1;
        }
        return defaultNtnPhone.getHlsErrorInfo().error_cause;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public String getDataVersion() {
        return this.mController.getDataVersion();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getUsedCityInfoIndex() {
        return this.mController.getUsedCityInfoIndex();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public int getSaveBeamId() {
        return this.mController.getSaveBeamId();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public String getLocationType() {
        return this.mController.getLocationType();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public String citRequest(int i, int i2, String str) {
        ensureHeavyInitialized();
        return this.mCitManager.citRequest(i, i2, str);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void stopCitTest() {
        ensureHeavyInitialized();
        this.mCitManager.stopCitTest();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void sendCitRequest(int i, int i2, String str, ResultReceiver resultReceiver) {
        ensureHeavyInitialized();
        this.mCitManager.sendCitRequest(i, i2, str, resultReceiver);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public ArrayList getCalibrationInformation() {
        return this.mController.getCalibrationInformation();
    }

    public String convertNtnGps(double d, double d2, double d3) {
        if (d < 0.0d) {
            d += 360.0d;
        }
        return String.valueOf((int) ((Math.round(d * 1000.0d) / 1000.0d) * 1000.0d)) + "," + String.valueOf((int) ((Math.round(d2 * 1000.0d) / 1000.0d) * 1000.0d));
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void receiveNtnGps(double d, double d2, double d3) {
        this.mController.sendNtnGps(convertNtnGps(d, d2, d3));
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void satelliteDisplay() {
        this.mController.satelliteDisplay();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void openSatellite(int i, int i2) {
        ensureHeavyInitialized();
        this.mController.openSatellite(i, i2);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isSatelliteXingwangEnabled() {
        return this.mController.isSatelliteXingwangEnabled();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void setSatelliteXingwangEnableByUser(Context context, boolean z, int i) {
        ensureHeavyInitialized();
        this.mController.setSatelliteXingwangEnableByUser(context, z, i);
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean isExitSatelliteInProgress() {
        return this.mController.isExitSatelliteInProgress();
    }

    public static NtnPhone getDefaultNtnPhone() {
        try {
            return NtnPhoneFactory.getDefaultPhone();
        } catch (Exception e) {
            loge("getDefaultNtnPhone: " + e);
            return null;
        }
    }

    public SatelliteDataController getDataController() {
        return this.mDataController;
    }

    public SatelliteUIController getUIController() {
        return this.mUiController;
    }

    public SatelliteController getController() {
        return this.mController;
    }

    public SatelliteSIMController getSIMController() {
        return this.mSIMController;
    }

    public SatelliteOTAManager getOTAManager() {
        return this.mOTAManager;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean hasCellInfoWhenTurnOnSatellite() {
        return this.mController.hasCellInfoWhenTurnOnSatellite();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public Location getCurrentLocationWhenTurnOnSatellite() {
        return this.mController.getCurrentLocationWhenTurnOnSatellite();
    }

    @Override // com.android.phone.SatelliteManagerBase
    public boolean registSatelliteLocationCallback(IRemoteCallback iRemoteCallback, int i, int i2) {
        NtnLocationManager.getInstance().startLocationUpdate(iRemoteCallback, i, i2);
        return true;
    }

    @Override // com.android.phone.SatelliteManagerBase
    public void unRegistSatelliteLocationCallback(IRemoteCallback iRemoteCallback) {
        NtnLocationManager.getInstance().unRegistLocationUpdates(iRemoteCallback);
    }

    public void registerSatelliteConfigState(Handler handler, int i, Object obj) {
        this.mSatelliteConfigRegistrantList.addUnique(handler, i, obj);
    }

    public void unregisterSatelliteConfigState(Handler handler) {
        RegistrantList registrantList = this.mSatelliteConfigRegistrantList;
        if (registrantList != null) {
            registrantList.remove(handler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d(LOG_TAG, str);
    }

    private static void loge(String str) {
        Log.e(LOG_TAG, str);
    }
}
