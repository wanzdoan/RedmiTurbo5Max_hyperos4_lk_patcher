package com.mediatek.internal.telephony;

import android.content.Context;
import android.hardware.radio.config.IRadioConfig;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.RadioConfig;
import com.android.telephony.Rlog;
import com.mediatek.powerhalmgr.PowerHalMgrImpl;
import java.util.NoSuchElementException;
import mediatek.telephony.MtkDisconnectCause;
import mediatek.telephony.MtkTelephony;
import vendor.mediatek.hardware.nvram.INvram;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioConfig extends RadioConfig {
    private static final int CAPABILITY_MODEM_EXPRESS = 268435456;
    private static final int EVENT_FLUSH_PRIORITY_RULES = 1001;
    static final int EVENT_RETRY_GET_SERVICE = 1001;
    private static final int EVENT_SET_PRIORITY_BY_UID = 1000;
    static final int GET_SERVICE_DELAY_MILLIS = 1000;
    private static String NVRAM_SERVICE = INvram.DESCRIPTOR + "/default";
    private static final String TAG = "MtkRadioConfig";
    private INvram mAidlNvram;

    public MtkRadioConfig(Context context, HalVersion radioHalVersion) {
        super(context, radioHalVersion);
        this.mAidlNvram = null;
    }

    public void handleMessage(Message message) {
        if (message.what == 1001) {
            Rlog.d(TAG, "handleMessage: EVENT_RETRY_GET_SERVICE");
            getRadioConfigProxy(null);
        } else {
            super.handleMessage(message);
        }
    }

    protected void updateRadioConfigProxy() {
        removeMessages(1001);
        if (!isRildReady()) {
            sendMessageDelayed(obtainMessage(1001), 1000L);
        } else {
            super.updateRadioConfigProxy();
        }
    }

    private boolean isRildReady() {
        String aidlServiceName = IRadioConfig.DESCRIPTOR + "/default";
        if (ServiceManager.isDeclared(aidlServiceName)) {
            if (ServiceManager.checkService(aidlServiceName) == null) {
                Rlog.i(TAG, "AIDL Service not ready: " + aidlServiceName);
                return false;
            }
            return true;
        }
        try {
            if (android.hardware.radio.config.V1_1.IRadioConfig.getService(false) == null) {
                Rlog.i(TAG, "HIDL Service V1_1 not ready.");
                return false;
            }
            return true;
        } catch (RemoteException | NoSuchElementException e) {
            Rlog.e(TAG, "HIDL Service not ready, exception: " + e);
            return false;
        }
    }

    protected boolean isGetHidlServiceSync() {
        return false;
    }

    private void initNvramIdl() {
        if (ServiceManager.checkService(NVRAM_SERVICE) == null) {
            Rlog.e(TAG, "Nvram service is not available");
            return;
        }
        try {
            IBinder binder = Binder.allowBlocking(ServiceManager.waitForDeclaredService(NVRAM_SERVICE));
            if (binder != null) {
                this.mAidlNvram = INvram.Stub.asInterface(binder);
            }
            if (this.mAidlNvram == null) {
                Rlog.e(TAG, "mAidlNvram == null");
            }
        } catch (Exception e) {
            Rlog.e(TAG, "Exception happened when get INvram service" + e.getMessage());
        }
    }

    public String readNvramByName(String filename, int size) {
        try {
            if (this.mAidlNvram == null) {
                initNvramIdl();
            }
            if (this.mAidlNvram == null) {
                return null;
            }
            return this.mAidlNvram.readFileByName(filename, size);
        } catch (RemoteException e) {
            Rlog.e(TAG, "Exception happened when readNvramByName" + e.getMessage());
            return null;
        }
    }

    public byte writeNvramByNamevec(String filename, int size, byte[] data) {
        try {
            if (this.mAidlNvram == null) {
                initNvramIdl();
            }
            if (this.mAidlNvram == null) {
                return (byte) -1;
            }
            return this.mAidlNvram.writeFileByNamevec(filename, size, data);
        } catch (RemoteException e) {
            Rlog.e(TAG, "Exception happened when writeNvramByNamevec" + e.getMessage());
            return (byte) -1;
        }
    }

    public boolean hasVendorCapability(int capability) {
        return capability == CAPABILITY_MODEM_EXPRESS && PowerHalMgrImpl.getInstance() != null;
    }

    public void configureParameters(int event, Bundle extra) {
        switch (event) {
            case MtkDisconnectCause.MTK_DISCONNECTED_CAUSE_BASE /* 1000 */:
                int action = extra.getInt(MtkTelephony.WapPush.ACTION);
                int uid = extra.getInt("uid");
                PowerHalMgrImpl.getInstance().setPriorityByUid(action, uid);
                Rlog.d(TAG, "EVENT_SET_PRIORITY_BY_UID: action=" + action + ", uid=" + uid);
                break;
            case 1001:
                int type = extra.getInt("type");
                PowerHalMgrImpl.getInstance().flushPriorityRules(type);
                Rlog.d(TAG, "EVENT_FLUSH_PRIORITY_RULES: type=" + type);
                break;
            default:
                Rlog.e(TAG, "Unknown event: " + event);
                break;
        }
    }
}
