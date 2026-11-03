package com.android.phone;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IRemoteCallback;
import android.os.ResultReceiver;
import com.android.internal.telephony.Phone;
import com.android.phone.beidou.BdSmsController;
import com.android.phone.beidou.BdSmsUtils;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.ntn.NtnBoxServiceManager;
import com.android.phone.ntn.NtnLocationManager;
import com.android.phone.ntn.NtnOptimizeControllerBase;
import com.android.phone.ntn.NtnPhoneProxyBase;
import com.android.phone.satellite.SatelliteController;
import com.android.phone.satellite.SatelliteToCellularTracker;
import com.android.services.telephony.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteDelegateImpl implements ISatelliteModule {
    private static volatile SatelliteDelegateImpl sInstance;
    private Lazy mBdSmsController = new Lazy();
    private Lazy mNtnPhoneProxy = new Lazy();
    private Lazy mNtnOptimize = new Lazy();
    private Lazy mNtnBoxService = new Lazy();
    private Lazy mNtnLocationManager = new Lazy();

    @Override // com.android.phone.module.ISatelliteModule
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public static SatelliteDelegateImpl init() {
        if (sInstance == null) {
            synchronized (SatelliteDelegateImpl.class) {
                try {
                    if (sInstance == null) {
                        SatelliteDelegateImpl satelliteDelegateImpl = new SatelliteDelegateImpl();
                        ModuleDelegate.register(ISatelliteModule.class, satelliteDelegateImpl);
                        sInstance = satelliteDelegateImpl;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private SatelliteDelegateImpl() {
    }

    public void setBdSmsController(BdSmsController bdSmsController) {
        this.mBdSmsController.set(bdSmsController);
    }

    public void setNtnPhoneProxy(NtnPhoneProxyBase ntnPhoneProxyBase) {
        this.mNtnPhoneProxy.set(ntnPhoneProxyBase);
    }

    public void setNtnOptimize(NtnOptimizeControllerBase ntnOptimizeControllerBase) {
        this.mNtnOptimize.set(ntnOptimizeControllerBase);
    }

    public void setNtnBoxService(NtnBoxServiceManager ntnBoxServiceManager) {
        this.mNtnBoxService.set(ntnBoxServiceManager);
    }

    public void setNtnLocationManager(NtnLocationManager ntnLocationManager) {
        this.mNtnLocationManager.set(ntnLocationManager);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean getBdSwitchState() {
        return BdSmsUtils.getBdSwitchState();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void setBeiDouEnableByUser(Context context, boolean z) {
        BdSmsUtils.setBeiDouEnableByUser(context, z);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void recoverNtnNetwork() {
        if (this.mNtnBoxService.get() != null) {
            ((NtnBoxServiceManager) this.mNtnBoxService.get()).recoverNtnNetwork();
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean getBdServiceStatus(int i) {
        if (this.mBdSmsController.get() == null) {
            return false;
        }
        return ((BdSmsController) this.mBdSmsController.get()).getBdServiceStatus(i);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getPhoneBdServiceState() {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        return ((BdSmsController) this.mBdSmsController.get()).getPhoneBdServiceState();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getBdSysStatus() {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        return ((BdSmsController) this.mBdSmsController.get()).getBdSysStatus();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int sendBDMessage(int i, int[] iArr, String str, int i2, List list) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        ((BdSmsController) this.mBdSmsController.get()).setSlotId(i2);
        return ((BdSmsController) this.mBdSmsController.get()).sendBDMessage(i, iArr, str, i2, list);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int sendBDMessageWithLocation(int i, int[] iArr, String str, int i2, List list, double d, double d2) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        ((BdSmsController) this.mBdSmsController.get()).setSlotId(i2);
        return ((BdSmsController) this.mBdSmsController.get()).sendBDMessageWithLocation(i, iArr, str, i2, list, d, d2);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int receiveBDMessage(int i) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        ((BdSmsController) this.mBdSmsController.get()).setSlotId(i);
        return ((BdSmsController) this.mBdSmsController.get()).receiveBDMessage(i);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void startConnectSatelliteService(int i) {
        if (this.mBdSmsController.get() == null) {
            return;
        }
        ((BdSmsController) this.mBdSmsController.get()).startConnectSatelliteService(i);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void closeBdService() {
        if (this.mBdSmsController.get() == null) {
            return;
        }
        ((BdSmsController) this.mBdSmsController.get()).closeBdService();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getAllowedBDSmsSendTime() {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        return ((BdSmsController) this.mBdSmsController.get()).getAllowedBDSmsSendTime();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getAllowedBDSmsReceiveTime() {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        return ((BdSmsController) this.mBdSmsController.get()).getAllowedBDSmsReceiveTime();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getRemainBDSmsCount(int i) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        return ((BdSmsController) this.mBdSmsController.get()).getRemainBDSmsCount(i);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void syncBDMail(int i) {
        if (this.mBdSmsController.get() == null) {
            return;
        }
        ((BdSmsController) this.mBdSmsController.get()).syncBDMail(i);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int preLoad(int i) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        return ((BdSmsController) this.mBdSmsController.get()).preLoad(i);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void setSlotId(int i) {
        if (this.mBdSmsController.get() == null) {
            return;
        }
        ((BdSmsController) this.mBdSmsController.get()).setSlotId(i);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void reportStatistics() {
        if (SatelliteToCellularTracker.getInstance() != null) {
            SatelliteToCellularTracker.getInstance().reportStatistics();
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int sendBDAudioMessage(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        ((BdSmsController) this.mBdSmsController.get()).setSlotId(i2);
        return ((BdSmsController) this.mBdSmsController.get()).sendBDAudioMessage(i, iArr, uri, uri2, i2, list);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int sendBDAudioMessageWithLocation(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, double d, double d2) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        ((BdSmsController) this.mBdSmsController.get()).setSlotId(i2);
        return ((BdSmsController) this.mBdSmsController.get()).sendBDAudioMessageWithLocation(i, iArr, uri, uri2, i2, list, d, d2);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int sendBDPictureMessage(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        ((BdSmsController) this.mBdSmsController.get()).setSlotId(i2);
        return ((BdSmsController) this.mBdSmsController.get()).sendBDPictureMessage(i, iArr, uri, uri2, i2, list);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int sendBDPictureMessageWithLocation(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, double d, double d2) {
        if (this.mBdSmsController.get() == null) {
            return 0;
        }
        ((BdSmsController) this.mBdSmsController.get()).setSlotId(i2);
        return ((BdSmsController) this.mBdSmsController.get()).sendBDPictureMessageWithLocation(i, iArr, uri, uri2, i2, list, d, d2);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isSupportedBDPicture() {
        if (this.mBdSmsController.get() == null) {
            return false;
        }
        return ((BdSmsController) this.mBdSmsController.get()).isSupportedBDPicture();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isSupportedBDAudio() {
        if (this.mBdSmsController.get() == null) {
            return false;
        }
        return ((BdSmsController) this.mBdSmsController.get()).isSupportedBDAudio();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getBeanSwitchedStatus() {
        if (SatelliteController.getInstance() != null) {
            return SatelliteController.getInstance().getBeanSwitchedStatus();
        }
        return 0;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public Location getLastKnownLocation() {
        if (SatelliteController.getInstance() != null) {
            return SatelliteController.getInstance().getLastKnownLocation();
        }
        return null;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void updateAddressInfo(Location location, Address address) {
        NtnLocationManager ntnLocationManager = (NtnLocationManager) this.mNtnLocationManager.get();
        if (ntnLocationManager != null) {
            ntnLocationManager.updateAddressInfo(location, address);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public Bundle getNtnLocationInfo() {
        Bundle bundle = new Bundle();
        NtnLocationManager ntnLocationManager = (NtnLocationManager) this.mNtnLocationManager.get();
        if (ntnLocationManager != null) {
            bundle.putString("province", ntnLocationManager.getProvince());
            bundle.putString("city", ntnLocationManager.getCity());
            bundle.putString("areaCode", ntnLocationManager.getAreaCode());
            bundle.putDouble("altitude", ntnLocationManager.getAltitude());
            bundle.putFloat("speed", ntnLocationManager.getSpeed());
        }
        return bundle;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void updateCurrentLocation(String str) {
        NtnLocationManager ntnLocationManager = (NtnLocationManager) this.mNtnLocationManager.get();
        if (ntnLocationManager != null) {
            ntnLocationManager.updateCurrentLocation(str);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public Bundle getNtnModuleInfo() {
        Bundle bundle = new Bundle();
        if (this.mNtnOptimize.get() != null) {
            bundle.putBoolean("is_satellite_thermal_state", ((NtnOptimizeControllerBase) this.mNtnOptimize.get()).isSatelliteInThremalState());
        }
        if (SatelliteManagerBase.getInstance() != null) {
            bundle.putBoolean("satellite_exit_in_progress", SatelliteManagerBase.getInstance().isExitSatelliteInProgress());
        }
        return bundle;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isHardSimSwapForTT() {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isHardSimSwapForTT();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isSatelliteEnabled() {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isSatelliteEnabled();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isHighPenetrationEnabled() {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isHighPenetrationEnabled();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getEnabledSatelliteSlotId() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getEnabledSatelliteSlotId();
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean setSatelliteEnable(int i, boolean z) {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().setSatelliteEnable(i, z);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getSatelliteState(int i) {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getSatelliteState(i);
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void closeSatellite(int i, int i2) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().closeSatellite(i, i2);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void setSarMode(int i) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().setSarMode(i);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void getSarMode() {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().getSarMode();
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void receiveNtnGps(double d, double d2, double d3) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().receiveNtnGps(d, d2, d3);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isSatelliteDataEnabled() {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isSatelliteDataEnabled();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void setSatelliteEnableByUser(Context context, boolean z, int i) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().setSatelliteEnableByUser(context, z, i);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void setSatelliteDataEnableByUser(Context context, boolean z) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().setSatelliteDataEnableByUser(context, z);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void setSatelliteSignalBoostEnable(boolean z, int i) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().setSatelliteSignalBoostEnable(z, i);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isSatelliteSignalBoostEnabled() {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isSatelliteSignalBoostEnabled();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void setCalibrationInformation(Bundle bundle) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().setCalibrationInformation(bundle);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getRetSnr() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getRetSnr();
        }
        return 0;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getRealTxPwr() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getRealTxPwr();
        }
        return 0;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getNtnBeamId() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getNtnBeamId();
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public String citRequest(int i, int i2, String str) {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().citRequest(i, i2, str);
        }
        return null;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void sendCitRequest(int i, int i2, String str, ResultReceiver resultReceiver) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().sendCitRequest(i, i2, str, resultReceiver);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void stopCitTest() {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().stopCitTest();
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean registSatelliteLocationCallback(IRemoteCallback iRemoteCallback, int i, int i2) {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().registSatelliteLocationCallback(iRemoteCallback, i, i2);
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void unRegistSatelliteLocationCallback(IRemoteCallback iRemoteCallback) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().unRegistSatelliteLocationCallback(iRemoteCallback);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public String getSpeechBitrate() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getSpeechBitrate();
        }
        return null;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isSatelliteXingwangEnabled() {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isSatelliteXingwangEnabled();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getRetRssi() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getRetRssi();
        }
        return 0;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void sendSatelliteBusinessEntranceDot() {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().sendSatelliteBusinessEntranceDot();
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public void setSatelliteXingwangEnableByUser(Context context, boolean z, int i) {
        if (SatelliteManagerBase.getInstance() != null) {
            SatelliteManagerBase.getInstance().setSatelliteXingwangEnableByUser(context, z, i);
        }
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isPrivacyStatementAgree() {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isPrivacyStatementAgree();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getTarTxPwr() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getTarTxPwr();
        }
        return 0;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public boolean isCalibrated() {
        return SatelliteManagerBase.getInstance() != null && SatelliteManagerBase.getInstance().isCalibrated();
    }

    @Override // com.android.phone.module.ISatelliteModule
    public ArrayList getCalibrationInformation() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getCalibrationInformation();
        }
        return null;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public String getDataVersion() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getDataVersion();
        }
        return "";
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getSaveBeamId() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getSaveBeamId();
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getUsedCityInfoIndex() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getUsedCityInfoIndex();
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public String getLocationType() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getLocationType();
        }
        return "";
    }

    @Override // com.android.phone.module.ISatelliteModule
    public String getNtnPosInfo() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getNtnPosInfo();
        }
        return null;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getErrorClass() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getErrorClass();
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getErrorCause() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getErrorCause();
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getFer() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getFer();
        }
        return 0;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getSatId() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getSatId();
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getNtnBandId() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getNtnBandId();
        }
        return -1;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public float getAltitude() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getAltitude();
        }
        return 0.0f;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public int getFreqOff() {
        if (SatelliteManagerBase.getInstance() != null) {
            return SatelliteManagerBase.getInstance().getFreqOff();
        }
        return 0;
    }

    @Override // com.android.phone.module.ISatelliteModule
    public Phone getNtnPhone() {
        if (this.mNtnPhoneProxy.get() != null) {
            return ((NtnPhoneProxyBase) this.mNtnPhoneProxy.get()).getNtnPhone();
        }
        return null;
    }
}
