package com.xiaomi.telephony;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.telephony.Rlog;
import com.xiaomi.internal.telephony.IXmTelephony;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class XmTelephonyManagerInner {
    private static final int FAILURE = -1;
    private static final int INVALID_VERSION = -1;
    public static final int SET_FIVE_TUPLE_ADD_FILTER_ERROR = 2;
    public static final int SET_FIVE_TUPLE_DEL_FILTER_ERROR = 4;
    public static final int SET_FIVE_TUPLE_FILTER_OVER_LIMIT_ERROR = 5;
    public static final int SET_FIVE_TUPLE_MODE_FALSE_ERROR = 3;
    public static final int SET_FIVE_TUPLE_MODE_TRUE_ERROR = 1;
    public static final int SET_FIVE_TUPLE_NOT_SUPPORTED_ERROR = 0;
    public static final int SET_FIVE_TUPLE_UNKNOWN_ERROR = -1;
    private static final int SUCCESS = 0;
    private static final String TAG = "XmTelephonyManagerInner";
    private static volatile XmTelephonyManagerInner sInstance;
    private static IXmTelephony sXmTelephony;
    private final DeathRecipient mServiceDeath = new DeathRecipient();
    private HashSet<IDeathCallBack> mDeathCallBackHashSet = new HashSet<>();

    private class DeathRecipient implements IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Rlog.e(XmTelephonyManagerInner.TAG, "XmPhoneService died");
            for (IDeathCallBack callback : XmTelephonyManagerInner.this.mDeathCallBackHashSet) {
                callback.onServiceDeath();
            }
            XmTelephonyManagerInner.sXmTelephony.asBinder().unlinkToDeath(XmTelephonyManagerInner.this.mServiceDeath, 0);
            XmTelephonyManagerInner.sXmTelephony = null;
        }
    }

    public interface IDeathCallBack {
        default void onServiceDeath() {
        }
    }

    public static synchronized XmTelephonyManagerInner getDefault() {
        if (sInstance == null) {
            sInstance = new XmTelephonyManagerInner();
        }
        return sInstance;
    }

    private XmTelephonyManagerInner() {
    }

    private IXmTelephony getIXmTelephony() {
        try {
            if (sXmTelephony == null) {
                sXmTelephony = IXmTelephony.Stub.asInterface(ServiceManager.getService("xm.radio.extphone"));
                sXmTelephony.asBinder().linkToDeath(this.mServiceDeath, 0);
            }
        } catch (Exception e) {
            Rlog.e(TAG, "Failed to get IXmTelephony, Exception: " + e);
            sXmTelephony = null;
        }
        Rlog.i(TAG, "getIXmTelephony, sXmTelephony = " + sXmTelephony);
        return sXmTelephony;
    }

    public boolean sendCommMsg(int slotId, int msgId, byte[] data, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return false;
            }
            return xmTelephony.sendCommMsg(slotId, msgId, data, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "sendCommMsg exception", e);
            return false;
        }
    }

    public byte[] sendCommMsgSync(int slotId, int msgId, byte[] data) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return null;
            }
            return xmTelephony.sendCommMsgSync(slotId, msgId, data);
        } catch (RemoteException e) {
            Rlog.e(TAG, "sendCommMsg exception", e);
            return null;
        }
    }

    public boolean registerCommMsgCallback(int slotId, int msgId, CommMsgCallback callback) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.registerCommMsgCallback(slotId, msgId, callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "registerCommMsgCallback exception", e);
            return false;
        }
    }

    public boolean unRegisterCommMsgCallback(int slotId, int msgId, CommMsgCallback callback) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.unRegisterCommMsgCallback(slotId, msgId, callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "unRegisterCommMsgCallback exception", e);
            return false;
        }
    }

    public boolean registerDeathCallback(IDeathCallBack callback) {
        boolean zAdd;
        if (callback == null) {
            return false;
        }
        synchronized (this) {
            zAdd = this.mDeathCallBackHashSet.add(callback);
        }
        return zAdd;
    }

    public boolean unregisterDeathCallback(IDeathCallBack callback) {
        boolean zRemove;
        if (callback == null) {
            return false;
        }
        synchronized (this) {
            zRemove = this.mDeathCallBackHashSet.remove(callback);
        }
        return zRemove;
    }

    public boolean setSarDsi(int dsi, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return false;
            }
            return xmTelephony.setSarDsi(dsi, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setSarDsi exception", e);
            return false;
        }
    }

    public boolean setSarDsiGroup(int[] dsi, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return false;
            }
            return xmTelephony.setSarDsiGroup(dsi, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setSarDsiGroup exception", e);
            return false;
        }
    }

    public boolean getSarDsi(ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return false;
            }
            return xmTelephony.getSarDsi(resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "getSarDsi exception", e);
            return false;
        }
    }

    public boolean getSarDsiGroup(ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return false;
            }
            return xmTelephony.getSarDsiGroup(resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "getSarDsiGroup exception", e);
            return false;
        }
    }

    public boolean setDeprioritizeSA(int slotId, boolean enable, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return false;
            }
            return xmTelephony.setDeprioritizeSA(slotId, enable, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setDeprioritizeSA exception", e);
            return false;
        }
    }

    public boolean setNrMode(int slotId, int nrStatus, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return false;
            }
            return xmTelephony.setNrMode(slotId, nrStatus, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setNrMode exception", e);
            return false;
        }
    }

    public boolean enableEndc(int slotId, boolean enable, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return false;
            }
            return xmTelephony.enableEndc(slotId, enable, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "enableEndc exception", e);
            return false;
        }
    }

    public boolean registerHstStateCallback(int slotId, HstStateCallback callback) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.registerHstStateCallback(slotId, callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "registerHstStateCallback exception", e);
            return false;
        }
    }

    public boolean unRegisterHstStateCallback(int slotId, HstStateCallback callback) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.unRegisterHstStateCallback(slotId, callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "unRegisterHstStateCallback exception", e);
            return false;
        }
    }

    public boolean registerQosStateCallback(int slotId, QosStateCallback callback) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.registerQosStateCallback(slotId, callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "registerQosStateCallback exception", e);
            return false;
        }
    }

    public boolean unRegisterQosStateCallback(int slotId, QosStateCallback callback) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.unRegisterQosStateCallback(slotId, callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "unRegisterQosStateCallback exception", e);
            return false;
        }
    }

    public boolean restartRILD(int slotId, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.restartRILD(slotId, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "restartRILD exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean setDataStallMode(int slotId, int mode, int operation, int bearerType, int scene, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony != null) {
                return xmTelephony.setDataStallMode(slotId, mode, operation, bearerType, scene, resultReceiver);
            }
            resultReceiver.send(-1, null);
            return false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "setDataStallMode exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean setMobilityOptParams(int slotId, boolean enable, int rat, int featureId, ResultReceiver resultReceiver) {
        ResultReceiver resultReceiver2;
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                try {
                    resultReceiver.send(-1, null);
                    return false;
                } catch (RemoteException e) {
                    e = e;
                    resultReceiver2 = resultReceiver;
                }
            } else {
                resultReceiver2 = resultReceiver;
                try {
                    return xmTelephony.setMobilityOptParams(slotId, enable, rat, featureId, resultReceiver2);
                } catch (RemoteException e2) {
                    e = e2;
                }
            }
        } catch (RemoteException e3) {
            e = e3;
            resultReceiver2 = resultReceiver;
        }
        Rlog.e(TAG, "setMobilityOptParams exception", e);
        resultReceiver2.send(-1, null);
        return false;
    }

    public boolean setPreferBandParams(int slotId, int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony != null) {
                return xmTelephony.setPreferBandParams(slotId, rat, bands, arfcns, rsrpOffsets, preferType, resultReceiver);
            }
            resultReceiver.send(-1, null);
            return false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "setPreferBandParams exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean promoteBSR(int slotId, int level, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.promoteBSR(slotId, level, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "promoteBSR exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean setSensorState(int slotId, int sensorType, int sensorState, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.setSensorState(slotId, sensorType, sensorState, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setSensorState exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean getDdsAssistantInfo(ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.getDdsAssistantInfo(resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "getDdsAssistantInfo exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean getModemScene(ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.getModemScene(resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "getModemScene exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean getModemReason(ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.getModemReason(resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "getModemReason exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean setModemModelCfg(int[] modelCfg, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.setModemModelCfg(modelCfg, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setModemModelCfg exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean setModemCallbackSwitch(boolean enable, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.setModemCallbackSwitch(enable, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setModemCallbackSwitch exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean setModemEventIndSwitch(int event, boolean isRegister, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.setModemEventIndSwitch(event, isRegister, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setModemEventIndSwitch exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }

    public boolean registerModemExceptionCallback(ModemExceptionCallback callback) {
        if (callback == null) {
            return false;
        }
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.registerModemExceptionCallback(callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "registerModemExceptionCallback exception", e);
            return false;
        }
    }

    public boolean unregisterModemExceptionCallback(ModemExceptionCallback callback) {
        if (callback == null) {
            return false;
        }
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.unregisterModemExceptionCallback(callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "registerModemExceptionCallback exception", e);
            return false;
        }
    }

    public boolean registerModemSceneCallback(ModemSceneCallback callback) {
        if (callback == null) {
            return false;
        }
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.registerModemSceneCallback(callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "registerModemSceneCallback exception", e);
            return false;
        }
    }

    public boolean unregisterModemSceneCallback(ModemSceneCallback callback) {
        if (callback == null) {
            return false;
        }
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            return xmTelephony != null ? xmTelephony.unregisterModemSceneCallback(callback.getBinder()) : false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "unregisterModemSceneCallback exception", e);
            return false;
        }
    }

    public int getHalVersion(int slotId) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                return -1;
            }
            return xmTelephony.getHalVersion(slotId);
        } catch (RemoteException e) {
            Rlog.e(TAG, "getHalVersion exception", e);
            return -1;
        }
    }

    public boolean setFiveTupleBlackList(String rmnetData, List<String> ipList, int mode, ResultReceiver resultReceiver) {
        try {
            IXmTelephony xmTelephony = getIXmTelephony();
            if (xmTelephony == null) {
                resultReceiver.send(-1, null);
                return false;
            }
            return xmTelephony.setFiveTupleBlackList(rmnetData, ipList, mode, resultReceiver);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setFiveTupleBlackList exception", e);
            resultReceiver.send(-1, null);
            return false;
        }
    }
}
