package com.android.internal.telephony;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.ResultReceiver;
import com.android.internal.telephony.utils.XmPhoneUtils;
import com.android.telephony.Rlog;
import com.xiaomi.internal.telephony.ICommMsgCallback;
import com.xiaomi.internal.telephony.IHstStateCallback;
import com.xiaomi.internal.telephony.IModemExceptionCallback;
import com.xiaomi.internal.telephony.IModemSceneCallback;
import com.xiaomi.internal.telephony.IQosStateCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class XmPhone {
    private static final String LOG_TAG = "XmPhone";
    private Context mContext;
    private int mPhoneId;
    private HashMap<Integer, List<ICommMsgCallback>> mXmPhoneCallbackMap = new HashMap<>();
    private List<IHstStateCallback> mHstStateCallbackList = new ArrayList();
    private List<IQosStateCallback> mQosStateCallbackList = new ArrayList();
    private List<IModemExceptionCallback> mModemExceptionCbList = new ArrayList();
    private List<IModemSceneCallback> mModemSceneCbList = new ArrayList();
    private IXmRadioAdapter mXmRadioAdapter = new XmRadioAdapterImpl(this);

    public XmPhone(Context context, int phoneId) {
        this.mContext = context;
        this.mPhoneId = phoneId;
    }

    public Context getContext() {
        return this.mContext;
    }

    public int getPhoneId() {
        return this.mPhoneId;
    }

    public boolean sendCommMsg(int msgId, byte[] data, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.sendCommMsg(msgId, data, resultReceiver);
        return true;
    }

    public boolean registerCommMsgCallback(int msgId, ICommMsgCallback callback) {
        if (!this.mXmPhoneCallbackMap.containsKey(Integer.valueOf(msgId))) {
            this.mXmPhoneCallbackMap.put(Integer.valueOf(msgId), new ArrayList());
        }
        List<ICommMsgCallback> callbackList = this.mXmPhoneCallbackMap.get(Integer.valueOf(msgId));
        synchronized (callbackList) {
            XmPhoneUtils.addCallbackLocked(callbackList, callback);
        }
        return true;
    }

    public boolean unRegisterCommMsgCallback(int msgId, ICommMsgCallback callback) {
        List<ICommMsgCallback> callbackList = this.mXmPhoneCallbackMap.get(Integer.valueOf(msgId));
        if (callbackList == null) {
            return true;
        }
        synchronized (callbackList) {
            XmPhoneUtils.removeCallbackLocked(callbackList, callback);
        }
        return true;
    }

    public boolean setSarDsi(int dsi, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setSarDsi(dsi, resultReceiver);
        return true;
    }

    public boolean setSarDsiGroup(int[] dsi, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setSarDsiGroup(dsi, resultReceiver);
        return true;
    }

    public boolean getSarDsi(ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.getSarDsi(resultReceiver);
        return true;
    }

    public boolean getSarDsiGroup(ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.getSarDsiGroup(resultReceiver);
        return true;
    }

    public boolean setDeprioritizeSA(boolean enable, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setDeprioritizeSA(enable, resultReceiver);
        return true;
    }

    public boolean setNrMode(int nrStatus, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setNrMode(nrStatus, resultReceiver);
        return true;
    }

    public boolean enableEndc(boolean enable, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.enableEndc(enable, resultReceiver);
        return true;
    }

    public boolean registerHstStateCallback(IHstStateCallback callback) {
        synchronized (this.mHstStateCallbackList) {
            XmPhoneUtils.addCallbackLocked(this.mHstStateCallbackList, callback);
        }
        return true;
    }

    public boolean unRegisterHstStateCallback(IHstStateCallback callback) {
        synchronized (this.mHstStateCallbackList) {
            XmPhoneUtils.removeCallbackLocked(this.mHstStateCallbackList, callback);
        }
        return true;
    }

    public boolean registerQosInfoCallback(IQosStateCallback callback) {
        synchronized (this.mQosStateCallbackList) {
            XmPhoneUtils.addCallbackLocked(this.mQosStateCallbackList, callback);
        }
        return true;
    }

    public boolean unRegisterQosInfoCallback(IQosStateCallback callback) {
        synchronized (this.mQosStateCallbackList) {
            XmPhoneUtils.removeCallbackLocked(this.mQosStateCallbackList, callback);
        }
        return true;
    }

    public void notifyUsolCommonMsg(int msgId, byte[] data) {
        List<ICommMsgCallback> callbackList = this.mXmPhoneCallbackMap.get(Integer.valueOf(msgId));
        if (callbackList != null) {
            synchronized (callbackList) {
                logd("notifyCommonMessage: msgId = " + msgId + ", data = " + Arrays.toString(data) + ", callbackList = " + callbackList + ", callbackListSize = " + callbackList.size());
                Iterator<ICommMsgCallback> iterator = callbackList.iterator();
                while (iterator.hasNext()) {
                    ICommMsgCallback callback = iterator.next();
                    try {
                        try {
                            callback.onUnsolCommMsg(this.mPhoneId, msgId, data);
                        } catch (DeadObjectException e) {
                            loge("notifyUsolCommonMsg, DeadObjectException while unregistering callback = " + callback + " binder = " + callback.asBinder());
                            iterator.remove();
                        }
                    } catch (Exception e2) {
                        loge("notifyUsolCommonMsg, Exception while unregistering callback = " + callback + " binder = " + callback.asBinder());
                    }
                }
            }
        }
    }

    public void notifyHstState(boolean hstState, int hstRat) {
        synchronized (this.mHstStateCallbackList) {
            logd("notifyHstState: hstState = " + hstState + ", hstRat = " + hstRat);
            Iterator<IHstStateCallback> iterator = this.mHstStateCallbackList.iterator();
            while (iterator.hasNext()) {
                IHstStateCallback callback = iterator.next();
                try {
                    callback.onHstStateChange(this.mPhoneId, hstState, hstRat);
                } catch (DeadObjectException e) {
                    loge("notifyHstState, DeadObjectException while unregistering callback = " + callback + " binder = " + callback.asBinder());
                    iterator.remove();
                } catch (Exception e2) {
                    loge("notifyHstState, Exception while unregistering callback = " + callback + " binder = " + callback.asBinder());
                }
            }
        }
    }

    public void notifyQosState(int qosId, Bundle dataBundle) {
        synchronized (this.mQosStateCallbackList) {
            try {
                logd("notifyQosState: dataBundle = " + dataBundle.toString());
                Iterator<IQosStateCallback> iterator = this.mQosStateCallbackList.iterator();
                while (iterator.hasNext()) {
                    IQosStateCallback callback = iterator.next();
                    try {
                        callback.onQosStateChange(this.mPhoneId, dataBundle);
                    } catch (DeadObjectException e) {
                        loge("notifyQosState, DeadObjectException while unregistering callback = " + callback + " binder = " + callback.asBinder());
                        iterator.remove();
                    } catch (Exception e2) {
                        loge("notifyQosState, Exception while unregistering callback = " + callback + " binder = " + callback.asBinder());
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean restartRILD(ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.restartRILD(resultReceiver);
        return true;
    }

    public boolean setDataStallMode(int mode, int operation, int bearerType, int scene, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setDataStallMode(mode, operation, bearerType, scene, resultReceiver);
        return true;
    }

    public boolean setMobilityOptParams(boolean enable, int rat, int featureId, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setMobilityOptParams(enable, rat, featureId, resultReceiver);
        return true;
    }

    public boolean setPreferBandParams(int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setPreferBandParams(rat, bands, arfcns, rsrpOffsets, preferType, resultReceiver);
        return true;
    }

    public boolean promoteBSR(int level, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.promoteBSR(level, resultReceiver);
        return true;
    }

    public boolean setSensorState(int sensorType, int sensorState, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setSensorState(sensorType, sensorState, resultReceiver);
        return true;
    }

    public boolean getDdsAssistantInfo(ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.getDdsAssistantInfo(resultReceiver);
        return true;
    }

    public boolean getModemScene(ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.getModemScene(resultReceiver);
        return true;
    }

    public boolean getModemReason(ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.getModemReason(resultReceiver);
        return true;
    }

    public boolean setModemModelCfg(int[] modelCfg, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setModemModelCfg(modelCfg, resultReceiver);
        return true;
    }

    public boolean setModemCallbackSwitch(boolean enable, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setModemCallbackSwitch(enable, resultReceiver);
        return true;
    }

    public boolean setModemEventIndSwitch(int event, boolean isRegister, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setModemEventIndSwitch(event, isRegister, resultReceiver);
        return true;
    }

    public int getHalVersion() {
        return this.mXmRadioAdapter.getHalVersion();
    }

    public boolean registerModemExceptionCallback(IModemExceptionCallback callback) {
        synchronized (this.mModemExceptionCbList) {
            XmPhoneUtils.addCallbackLocked(this.mModemExceptionCbList, callback);
        }
        return true;
    }

    public boolean unregisterModemExceptionCallback(IModemExceptionCallback callback) {
        synchronized (this.mModemExceptionCbList) {
            XmPhoneUtils.removeCallbackLocked(this.mModemExceptionCbList, callback);
        }
        return true;
    }

    public boolean registerModemSceneCallback(IModemSceneCallback callback) {
        synchronized (this.mModemSceneCbList) {
            XmPhoneUtils.addCallbackLocked(this.mModemSceneCbList, callback);
        }
        return true;
    }

    public boolean unregisterModemSceneCallback(IModemSceneCallback callback) {
        synchronized (this.mModemSceneCbList) {
            XmPhoneUtils.removeCallbackLocked(this.mModemSceneCbList, callback);
        }
        return true;
    }

    public void notifyModemExceptionChanged(int[] modemException) {
        synchronized (this.mModemExceptionCbList) {
            Iterator<IModemExceptionCallback> iterator = this.mModemExceptionCbList.iterator();
            while (iterator.hasNext()) {
                IModemExceptionCallback callback = iterator.next();
                try {
                    callback.onModemExceptionChanged(this.mPhoneId, modemException);
                } catch (DeadObjectException e) {
                    loge("notifyModemExceptionChanged, DeadObjectException while unregistering callback = " + callback + " binder = " + callback.asBinder());
                    iterator.remove();
                } catch (Exception e2) {
                    loge("notifyModemExceptionChanged, Exception while unregistering callback = " + callback + " binder = " + callback.asBinder());
                }
            }
        }
    }

    public void notifyModemSceneChanged(int[] sceneInfo) {
        synchronized (this.mModemSceneCbList) {
            Iterator<IModemSceneCallback> iterator = this.mModemSceneCbList.iterator();
            while (iterator.hasNext()) {
                IModemSceneCallback callback = iterator.next();
                try {
                    callback.onModemSceneChanged(this.mPhoneId, sceneInfo);
                } catch (DeadObjectException e) {
                    loge("notifyModemSceneChanged, DeadObjectException while unregistering callback = " + callback + " binder = " + callback.asBinder());
                    iterator.remove();
                } catch (Exception e2) {
                    loge("notifyModemSceneChanged, Exception while unregistering callback = " + callback + " binder = " + callback.asBinder());
                }
            }
        }
    }

    public boolean setFiveTupleBlackList(String rmnetData, List<String> ipList, int mode, ResultReceiver resultReceiver) {
        this.mXmRadioAdapter.setFiveTupleBlackList(rmnetData, ipList, mode, resultReceiver);
        return true;
    }

    private void logd(String s) {
        Rlog.d(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }

    private void logi(String s) {
        Rlog.i(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }

    private void loge(String s) {
        Rlog.e(LOG_TAG, "[" + this.mPhoneId + "] " + s);
    }
}
