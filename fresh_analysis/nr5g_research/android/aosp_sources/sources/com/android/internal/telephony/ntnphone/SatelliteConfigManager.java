package com.android.internal.telephony.ntnphone;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.NtnRIL;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteConfigManager extends Handler {
    protected static final int EVENT_GET_SATELLITE_CAPABILITY = 0;
    private final List mCallbackList;
    private boolean mIsConfigLoaded;
    private NtnPhone mPhone;
    private Map mSupportedCapabilityConfig;

    public interface ConfigUpdateCallback {
        void onConfigUpdate(Map<SatelliteConfig.Type, SatelliteConfig> map);
    }

    public SatelliteConfigManager(NtnPhone ntnPhone) {
        super(Looper.getMainLooper());
        this.mCallbackList = new CopyOnWriteArrayList();
        this.mPhone = ntnPhone;
        this.mIsConfigLoaded = false;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Rlog.d("SatelliteConfigManager", "handleMessage: " + message.what);
        if (message.what == 0) {
            handleSatelliteConfig((AsyncResult) message.obj);
            return;
        }
        Rlog.d("SatelliteConfigManager", "SatelliteConfigManager: not support Event msg.what=" + message.what);
    }

    public void initSatelliteCap() {
        this.mSupportedCapabilityConfig = new HashMap();
        HalVersion ntnHalVersion = this.mPhone.mNtnRil.getNtnHalVersion();
        Rlog.d("SatelliteConfigManager", "initSatelliteCap: halVersion: " + ntnHalVersion);
        if (ntnHalVersion.equals(NtnRIL.NTN_RADIO_HAL_VERSION_1_0)) {
            SatelliteConfig satelliteConfig = new SatelliteConfig();
            SatelliteConfig.Type type = SatelliteConfig.Type.TIANTONG_1;
            satelliteConfig.satelliteType = type;
            satelliteConfig.chipType = SatelliteConfig.Chip.SYLINCOM_701;
            satelliteConfig.capability = 7;
            this.mSupportedCapabilityConfig.put(type, satelliteConfig);
            notifyConfigUpdate(this.mSupportedCapabilityConfig);
            this.mIsConfigLoaded = true;
            return;
        }
        if (ntnHalVersion.equals(NtnRIL.NTN_RADIO_HAL_VERSION_2_0)) {
            SatelliteConfig satelliteConfig2 = new SatelliteConfig();
            SatelliteConfig.Type type2 = SatelliteConfig.Type.TIANTONG_1;
            satelliteConfig2.satelliteType = type2;
            satelliteConfig2.chipType = SatelliteConfig.Chip.CETC54;
            satelliteConfig2.capability = 4;
            this.mSupportedCapabilityConfig.put(type2, satelliteConfig2);
            notifyConfigUpdate(this.mSupportedCapabilityConfig);
            this.mIsConfigLoaded = true;
            return;
        }
        this.mPhone.mNtnRil.initNtnRadio();
        this.mPhone.mNtnRil.queryCurrentSatellteCapability(obtainMessage(0));
    }

    private void handleSatelliteConfig(AsyncResult asyncResult) {
        List arrayList;
        Rlog.d("SatelliteConfigManager", "handleSatelliteConfig");
        Rlog.d("SatelliteConfigManager", "handleSatelliteConfig" + asyncResult);
        Rlog.d("SatelliteConfigManager", "handleSatelliteConfig" + asyncResult.exception);
        Rlog.d("SatelliteConfigManager", "handleSatelliteConfig" + asyncResult.result);
        Throwable th = asyncResult.exception;
        if (th == null) {
            arrayList = (List) asyncResult.result;
        } else if (isCommandExceptionRadioNotAvailable(th)) {
            arrayList = new ArrayList();
        } else {
            notifyConfigUpdate(this.mSupportedCapabilityConfig);
            this.mIsConfigLoaded = true;
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            SatelliteConfig satelliteConfig = (SatelliteConfig) arrayList.get(i);
            Rlog.d("SatelliteConfigManager", "handleSatelliteConfig SatelliteConfig = " + satelliteConfig + ";");
            if (satelliteConfig != null) {
                this.mSupportedCapabilityConfig.put(satelliteConfig.satelliteType, satelliteConfig);
            }
        }
        notifyConfigUpdate(this.mSupportedCapabilityConfig);
        this.mIsConfigLoaded = true;
    }

    public Map getAllSatelliteConfig() {
        return this.mSupportedCapabilityConfig;
    }

    public SatelliteConfig getSatelliteConfig(int i) {
        Map map = this.mSupportedCapabilityConfig;
        if (map == null) {
            return null;
        }
        return (SatelliteConfig) map.get(SatelliteConfig.typeFromConfig(i));
    }

    public SatelliteConfig getSatelliteConfig(SatelliteConfig.Type type) {
        Map map = this.mSupportedCapabilityConfig;
        if (map == null) {
            return null;
        }
        return (SatelliteConfig) map.get(type);
    }

    private boolean isCommandExceptionRadioNotAvailable(Throwable th) {
        return th != null && (th instanceof CommandException) && ((CommandException) th).getCommandError() == CommandException.Error.RADIO_NOT_AVAILABLE;
    }

    public void registerConfigUpdateCallback(ConfigUpdateCallback configUpdateCallback) {
        if (!this.mCallbackList.contains(configUpdateCallback)) {
            this.mCallbackList.add(configUpdateCallback);
        }
        if (this.mIsConfigLoaded) {
            configUpdateCallback.onConfigUpdate(this.mSupportedCapabilityConfig);
        }
    }

    public void unregisterConfigUpdateCallback(ConfigUpdateCallback configUpdateCallback) {
        this.mCallbackList.remove(configUpdateCallback);
    }

    public void notifyConfigUpdate(Map<SatelliteConfig.Type, SatelliteConfig> map) {
        Iterator it = this.mCallbackList.iterator();
        while (it.hasNext()) {
            ((ConfigUpdateCallback) it.next()).onConfigUpdate(map);
        }
    }
}
