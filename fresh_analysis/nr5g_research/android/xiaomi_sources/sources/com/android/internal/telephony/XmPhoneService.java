package com.android.internal.telephony;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.telephony.utils.XmPhoneUtils;
import com.xiaomi.internal.telephony.ICommMsgCallback;
import com.xiaomi.internal.telephony.IHstStateCallback;
import com.xiaomi.internal.telephony.IModemExceptionCallback;
import com.xiaomi.internal.telephony.IModemSceneCallback;
import com.xiaomi.internal.telephony.IQosStateCallback;
import com.xiaomi.internal.telephony.IXmTelephony;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class XmPhoneService extends IXmTelephony.Stub {
    private static final int EVENT_COMMON_MESSAGE = 0;
    private static final int INVALID_VERSION = -1;
    private static final String LOG_TAG = "XmPhoneService";
    private static final String XM_TELEPHONY_SERVICE_NAME = "xm.radio.extphone";
    private static XmPhoneService sInstance;
    private Context mContext;
    private final Handler mWorkThreadHandler = new Handler(this, XmPhoneUtils.getInstance().getLooper()) { // from class: com.android.internal.telephony.XmPhoneService.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.i(XmPhoneService.LOG_TAG, "handleMessage: msgId = " + msg.what);
            switch (msg.what) {
                case 0:
                    final WorkThreadRequest request = (WorkThreadRequest) msg.obj;
                    ResultReceiver receiver = new ResultReceiver(this) { // from class: com.android.internal.telephony.XmPhoneService.1.1
                        @Override // android.os.ResultReceiver
                        protected void onReceiveResult(int resultCode, Bundle resultData) {
                            byte[] data = null;
                            if (resultData != null) {
                                data = resultData.getByteArray("byte_array");
                            }
                            request.result = data == null ? new byte[0] : data;
                            notifyRequester(request);
                        }
                    };
                    XmPhone xmPhone = XmPhoneFactory.getXmPhone(request.phoneId.intValue());
                    xmPhone.sendCommMsg(request.msgId.intValue(), (byte[]) request.argument, receiver);
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyRequester(WorkThreadRequest request) {
            synchronized (request) {
                request.notifyAll();
            }
        }
    };

    private final class WorkThreadRequest {
        public Object argument;
        public Integer msgId;
        public Integer phoneId;
        public Object result;

        WorkThreadRequest(XmPhoneService xmPhoneService, Object argument) {
            this.phoneId = -1;
            this.argument = argument;
        }

        WorkThreadRequest(XmPhoneService xmPhoneService, Object argument, Integer phoneId) {
            this.phoneId = -1;
            this.argument = argument;
            this.phoneId = phoneId;
        }

        WorkThreadRequest(XmPhoneService xmPhoneService, Object argument, Integer phoneId, Integer msgId) {
            this.phoneId = -1;
            this.argument = argument;
            this.msgId = msgId;
            if (phoneId != null) {
                this.phoneId = phoneId;
            }
        }
    }

    public static XmPhoneService init(Context context) {
        XmPhoneService xmPhoneService;
        synchronized (XmPhoneService.class) {
            if (sInstance == null) {
                sInstance = new XmPhoneService(context);
            } else {
                Log.wtf(LOG_TAG, "init() called multiple times! sInstance = " + sInstance);
            }
            xmPhoneService = sInstance;
        }
        return xmPhoneService;
    }

    private XmPhoneService(Context context) {
        this.mContext = context;
        ServiceManager.addService(XM_TELEPHONY_SERVICE_NAME, this);
    }

    private void enforceCommMsgAllowList() {
        int uid = Binder.getCallingUid();
        if (uid == 1001 || uid == 1000 || uid == 0) {
            return;
        }
        Log.d(LOG_TAG, "enforceCommMsgAllowList: caller uid = " + uid);
        throw new SecurityException("caller is not allowed");
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean registerCommMsgCallback(int phoneId, int msgId, ICommMsgCallback callback) {
        XmPhone xmPhone;
        enforceCommMsgAllowList();
        Log.d(LOG_TAG, "registerCommMsgCallback: phoneId = " + phoneId + ", msgId = " + msgId + ", callback = " + callback);
        if (!XmPhoneUtils.isValidPhoneId(phoneId) || callback == null || (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) == null) {
            return false;
        }
        return xmPhone.registerCommMsgCallback(msgId, callback);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean unRegisterCommMsgCallback(int phoneId, int msgId, ICommMsgCallback callback) {
        XmPhone xmPhone;
        enforceCommMsgAllowList();
        Log.d(LOG_TAG, "unRegisterCommMsgCallback: phoneId = " + phoneId + ", msgId = " + msgId + ", callback = " + callback);
        if (!XmPhoneUtils.isValidPhoneId(phoneId) || callback == null || (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) == null) {
            return false;
        }
        return xmPhone.unRegisterCommMsgCallback(msgId, callback);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean sendCommMsg(int phoneId, int msgId, byte[] data, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        enforceCommMsgAllowList();
        Log.d(LOG_TAG, "sendCommMsg: phoneId = " + phoneId + ", msgId = " + msgId + ", data = " + Arrays.toString(data) + ", resultReceiver = " + resultReceiver);
        if (data == null) {
            data = new byte[0];
        }
        if (!XmPhoneUtils.isValidPhoneId(phoneId) || (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) == null) {
            return false;
        }
        return xmPhone.sendCommMsg(msgId, data, resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public byte[] sendCommMsgSync(int phoneId, int msgId, byte[] data) {
        byte[] data2;
        enforceCommMsgAllowList();
        Log.d(LOG_TAG, "sendCommMsgSync: phoneId = " + phoneId + ", msgId = " + msgId + ", data = " + Arrays.toString(data));
        if (!XmPhoneUtils.isValidPhoneId(phoneId)) {
            return null;
        }
        if (data != null) {
            data2 = data;
        } else {
            data2 = new byte[0];
        }
        byte[] resultData = (byte[]) sendRequest(0, data2, Integer.valueOf(phoneId), Integer.valueOf(msgId), TimeUnit.SECONDS.toMillis(5L));
        Log.d(LOG_TAG, "sendCommMsgSync: result = " + Arrays.toString(resultData));
        return resultData;
    }

    private Object sendRequest(int command, Object argument, Integer phoneId, Integer msgId, long timeoutInMs) {
        if (XmPhoneFactory.getXmPhone(phoneId.intValue()) == null) {
            return null;
        }
        WorkThreadRequest request = new WorkThreadRequest(this, argument, phoneId, msgId);
        Message msg = this.mWorkThreadHandler.obtainMessage(command, request);
        msg.sendToTarget();
        synchronized (request) {
            if (timeoutInMs >= 0) {
                long now = SystemClock.elapsedRealtime();
                long deadline = now + timeoutInMs;
                while (request.result == null && now < deadline) {
                    try {
                        request.wait(deadline - now);
                    } catch (InterruptedException e) {
                    } catch (Throwable th) {
                        SystemClock.elapsedRealtime();
                        throw th;
                    }
                    now = SystemClock.elapsedRealtime();
                }
            } else {
                while (request.result == null) {
                    try {
                        request.wait();
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }
        if (request.result == null) {
            Log.e(LOG_TAG, "sendRequest: Blocking command timed out. Something has gone terribly wrong.");
        }
        return request.result;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setSarDsi(int dsi, ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.setSarDsi(dsi, resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setSarDsiGroup(int[] dsis, ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.setSarDsiGroup(dsis, resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean getSarDsi(ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.getSarDsi(resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean getSarDsiGroup(ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.getSarDsiGroup(resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setDeprioritizeSA(int phoneId, boolean enable, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.setDeprioritizeSA(enable, resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setNrMode(int phoneId, int nrStatus, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.setNrMode(nrStatus, resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean enableEndc(int phoneId, boolean enable, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.enableEndc(enable, resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean registerHstStateCallback(int phoneId, IHstStateCallback callback) {
        XmPhone xmPhone;
        Log.d(LOG_TAG, "registerHstStateCallback: phoneId = " + phoneId + ", callback = " + callback);
        if (!XmPhoneUtils.isValidPhoneId(phoneId) || callback == null || (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) == null) {
            return false;
        }
        return xmPhone.registerHstStateCallback(callback);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean unRegisterHstStateCallback(int phoneId, IHstStateCallback callback) {
        XmPhone xmPhone;
        Log.d(LOG_TAG, "unRegisterHstStateCallback: phoneId = " + phoneId + ", callback = " + callback);
        if (!XmPhoneUtils.isValidPhoneId(phoneId) || callback == null || (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) == null) {
            return false;
        }
        return xmPhone.unRegisterHstStateCallback(callback);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean registerQosStateCallback(int phoneId, IQosStateCallback callback) {
        XmPhone xmPhone;
        Log.d(LOG_TAG, "registerQosStateCallback: phoneId = " + phoneId + ", callback = " + callback);
        if (!XmPhoneUtils.isValidPhoneId(phoneId) || callback == null || (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) == null) {
            return false;
        }
        return xmPhone.registerQosInfoCallback(callback);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean unRegisterQosStateCallback(int phoneId, IQosStateCallback callback) {
        XmPhone xmPhone;
        Log.d(LOG_TAG, "unRegisterQosStateCallback: phoneId = " + phoneId + ", callback = " + callback);
        if (!XmPhoneUtils.isValidPhoneId(phoneId) || callback == null || (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) == null) {
            return false;
        }
        return xmPhone.unRegisterQosInfoCallback(callback);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean restartRILD(int phoneId, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.restartRILD(resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setDataStallMode(int phoneId, int mode, int operation, int bearerType, int scene, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.setDataStallMode(mode, operation, bearerType, scene, resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setMobilityOptParams(int phoneId, boolean enable, int rat, int featureId, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.setMobilityOptParams(enable, rat, featureId, resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setPreferBandParams(int phoneId, int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.setPreferBandParams(rat, bands, arfcns, rsrpOffsets, preferType, resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean promoteBSR(int phoneId, int level, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.promoteBSR(level, resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setSensorState(int phoneId, int sensorType, int sensorState, ResultReceiver resultReceiver) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.setSensorState(sensorType, sensorState, resultReceiver);
        }
        return false;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean getDdsAssistantInfo(ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.getDdsAssistantInfo(resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean getModemScene(ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.getModemScene(resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean getModemReason(ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.getModemReason(resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setModemModelCfg(int[] modelCfg, ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.setModemModelCfg(modelCfg, resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setModemCallbackSwitch(boolean enable, ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.setModemCallbackSwitch(enable, resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setModemEventIndSwitch(int event, boolean isRegister, ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        return xmPhone.setModemEventIndSwitch(event, isRegister, resultReceiver);
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean registerModemExceptionCallback(IModemExceptionCallback callback) {
        XmPhone[] phones = XmPhoneFactory.getXmPhones();
        if (phones == null || phones.length == 0) {
            return false;
        }
        for (XmPhone phone : phones) {
            phone.registerModemExceptionCallback(callback);
        }
        return true;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean unregisterModemExceptionCallback(IModemExceptionCallback callback) {
        XmPhone[] phones = XmPhoneFactory.getXmPhones();
        if (phones == null || phones.length == 0) {
            return false;
        }
        for (XmPhone phone : phones) {
            phone.unregisterModemExceptionCallback(callback);
        }
        return true;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean registerModemSceneCallback(IModemSceneCallback callback) {
        XmPhone[] phones = XmPhoneFactory.getXmPhones();
        if (phones == null || phones.length == 0) {
            return false;
        }
        for (XmPhone phone : phones) {
            phone.registerModemSceneCallback(callback);
        }
        return true;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean unregisterModemSceneCallback(IModemSceneCallback callback) {
        XmPhone[] phones = XmPhoneFactory.getXmPhones();
        if (phones == null || phones.length == 0) {
            return false;
        }
        for (XmPhone phone : phones) {
            phone.unregisterModemSceneCallback(callback);
        }
        return true;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public int getHalVersion(int phoneId) {
        XmPhone xmPhone;
        if (XmPhoneUtils.isValidPhoneId(phoneId) && (xmPhone = XmPhoneFactory.getXmPhone(phoneId)) != null) {
            return xmPhone.getHalVersion();
        }
        return -1;
    }

    @Override // com.xiaomi.internal.telephony.IXmTelephony
    public boolean setFiveTupleBlackList(String rmnetData, List<String> ipList, int mode, ResultReceiver resultReceiver) {
        XmPhone xmPhone = XmPhoneFactory.getXmPhone(0);
        if (xmPhone == null) {
            return false;
        }
        if (rmnetData == null) {
            rmnetData = "";
        }
        if (ipList == null) {
            ipList = new ArrayList();
        }
        return xmPhone.setFiveTupleBlackList(rmnetData, ipList, mode, resultReceiver);
    }
}
