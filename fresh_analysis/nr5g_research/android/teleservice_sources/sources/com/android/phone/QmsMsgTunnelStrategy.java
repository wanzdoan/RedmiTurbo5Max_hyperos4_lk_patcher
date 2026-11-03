package com.android.phone;

import android.content.Context;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.Log;
import com.android.phone.statistics.MiuiQmsStats;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.HashMap;
import miui.os.Build;
import miui.telephony.qms.CellBarringInfoS;
import miui.telephony.qms.CellListS;
import miui.telephony.qms.DataStallParamS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.Nr5gNflEntryTypeS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PreferredCellListS;
import miui.telephony.qms.SetCellListInfoV2S;

/* JADX INFO: loaded from: classes.dex */
public class QmsMsgTunnelStrategy {
    private static final boolean IS_INDIA = "in".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region"));
    private static String LOG_TAG = "QmsMsgTunnelStrategy";
    private final Context mContext;
    private int mPhoneId;
    private final QmsHandler mQmsHandler;
    private int mRequestMessagesWaiting;
    private PowerManager.WakeLock mWakeLock;
    private int mWakeLockTimeout;
    ArrayList mRequestsList = new ArrayList();
    private long mLastTriggerQmsTime = 0;

    public QmsMsgTunnelStrategy(int i) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mQmsHandler = new QmsHandler(Looper.getMainLooper());
        this.mPhoneId = i;
        PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) phoneGlobals.getSystemService("power")).newWakeLock(1, LOG_TAG);
        this.mWakeLock = wakeLockNewWakeLock;
        wakeLockNewWakeLock.setReferenceCounted(false);
        this.mWakeLockTimeout = ((Integer) TelephonyProperties.wake_lock_timeout().orElse(10000)).intValue();
        this.mRequestMessagesWaiting = 0;
        log("QmsMsgTunnelStrategy: init");
    }

    class QmsHandler extends Handler {
        public QmsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            synchronized (QmsMsgTunnelStrategy.this.mWakeLock) {
                if (QmsMsgTunnelStrategy.this.mWakeLock.isHeld()) {
                    if (QmsMsgTunnelStrategy.this.mRequestMessagesWaiting != 0) {
                        QmsMsgTunnelStrategy.this.log("NOTE: mReqWaiting is NOT 0 but" + QmsMsgTunnelStrategy.this.mRequestMessagesWaiting + " at TIMEOUT, reset! There still msg waitng for response");
                        QmsMsgTunnelStrategy.this.mRequestMessagesWaiting = 0;
                        synchronized (QmsMsgTunnelStrategy.this.mRequestsList) {
                            int size = QmsMsgTunnelStrategy.this.mRequestsList.size();
                            QmsMsgTunnelStrategy.this.log("WAKE_LOCK_TIMEOUT  mRequestList=" + size);
                            if (size >= 50) {
                                QmsMsgTunnelStrategy.this.resetWakeLockAndRequestList();
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                QmsRequest qmsRequest = (QmsRequest) QmsMsgTunnelStrategy.this.mRequestsList.get(i);
                                QmsMsgTunnelStrategy.this.log(i + ": [" + qmsRequest.mSerial + "] " + QmsMsgTunnelStrategy.requestToString(qmsRequest.mRequest));
                            }
                        }
                    }
                    QmsMsgTunnelStrategy.this.mWakeLock.release();
                }
            }
        }
    }

    public synchronized void invokeQmsRequestRaw(Message message) {
        try {
            QmsRequest qmsRequestObtain = QmsRequest.obtain(1000, message);
            log("invokeQmsRequestRaw: serial=" + qmsRequestObtain.mSerial);
            try {
                if (System.currentTimeMillis() - this.mLastTriggerQmsTime < 2000) {
                    wait(2000L);
                }
                acquireWakeLock();
                queueRequest(qmsRequestObtain);
                message.arg2 = qmsRequestObtain.mSerial;
                processMatchQmsMethod(message);
                this.mLastTriggerQmsTime = System.currentTimeMillis();
            } catch (InterruptedException e) {
                log("invokeQmsRequestRaw interrupted exception error serial=" + qmsRequestObtain.mSerial + " " + e);
                processQmsException(qmsRequestObtain);
                Thread.currentThread().interrupt();
            } catch (Exception e2) {
                log("invokeQmsRequestRaw exception error serial=" + qmsRequestObtain.mSerial + " " + e2);
                processQmsException(qmsRequestObtain);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void processQmsException(QmsRequest qmsRequest) {
        sendResponse(findAndRemoveRequestFromList(qmsRequest.mSerial), -1, null);
        releaseWakeLockIfDone();
    }

    private void processMatchQmsMethod(Message message) {
        QmsMsgTunnelControllerBase.MessageRequestAsync messageRequestAsync = (QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj;
        int i = messageRequestAsync.requestId;
        if (!Build.IS_INTERNATIONAL_BUILD || IS_INDIA || i == 1000 || i == 1001) {
            int i2 = messageRequestAsync.slotId;
            Object obj = messageRequestAsync.obj;
            String str = messageRequestAsync.triggerReason;
            QmsClientBase qmsClientBase = QmsClientBase.getInstance();
            if (qmsClientBase == null) {
                log("processMatchQmsMethod qmsClient is null");
                return;
            }
            localLog("processMatchQmsMethod", "requestId=" + i + ", slotId=" + i2 + ", triggerReason=" + str);
            MiuiQmsStats.getInstance().recordTriggerTimes(i, str);
            switch (i) {
                case 1000:
                    qmsClientBase.registerQmsClient(message);
                    break;
                case 1001:
                    qmsClientBase.setDataStallParam(i2, (DataStallParamS) obj, message);
                    break;
                case 1002:
                    qmsClientBase.setL2NrSelection(i2, message);
                    break;
                case 1003:
                    qmsClientBase.setNfList((byte) i2, objToList(obj, Nr5gNflEntryTypeS.class), message);
                    break;
                case 1004:
                    qmsClientBase.setLteDubiousCellArray(i2, objToList(obj, LteDubiousCellInfoS.class), message);
                    break;
                case 1005:
                    qmsClientBase.setNrDubiousCellArray(i2, objToList(obj, NrDubiousCellInfoS.class), message);
                    break;
                case 1006:
                    qmsClientBase.getNfList(i2, message);
                    break;
                case 1007:
                    boolean zBooleanValue = Boolean.valueOf(obj.toString()).booleanValue();
                    log("option = " + zBooleanValue);
                    qmsClientBase.updateDppdUiOption(zBooleanValue, message);
                    break;
                case 1008:
                    qmsClientBase.getDppdCapability(message);
                    break;
                case 1009:
                    qmsClientBase.getDppdRecommendation(message);
                    break;
                case 1010:
                    qmsClientBase.setOutOfServiceState(i2, Integer.parseInt(obj.toString()), message);
                    break;
                case 1011:
                    qmsClientBase.registerHstStateReport();
                    break;
                case 1012:
                    qmsClientBase.setHstState(Integer.parseInt(obj.toString()), message);
                    break;
                case 1013:
                    qmsClientBase.getHstState(message);
                    break;
                case 1014:
                    qmsClientBase.getMultiSimSubmodeInfo(message);
                    break;
                case 1015:
                    qmsClientBase.setCoverageState(i2, Integer.parseInt(obj.toString()), message);
                    break;
                case 1016:
                    qmsClientBase.setPreferredCellList(i2, (PreferredCellListS) obj, message);
                    break;
                case 1017:
                    qmsClientBase.enableCellBarringFeature(i2, Boolean.valueOf(obj.toString()).booleanValue(), message);
                    break;
                case 1018:
                    qmsClientBase.setCellBarringInfo(i2, objToList(obj, CellBarringInfoS.class), message);
                    break;
                case 1019:
                    qmsClientBase.registerSubwayStateReport();
                    break;
                case 1020:
                    qmsClientBase.setSubwayState(Integer.parseInt(obj.toString()), message);
                    break;
                case 1021:
                    qmsClientBase.setCellListInfo(objToList(obj, CellListS.class), message);
                    break;
                case 1022:
                    qmsClientBase.setCellListInfoV2(objToList(obj, SetCellListInfoV2S.class), message);
                    break;
            }
        }
    }

    private static ArrayList objToList(Object obj, Class cls) {
        ArrayList arrayList = new ArrayList();
        try {
            if (obj instanceof ArrayList) {
                ArrayList arrayList2 = (ArrayList) obj;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    Object obj2 = arrayList2.get(i);
                    i++;
                    arrayList.add(cls.cast(obj2));
                }
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return arrayList;
        }
    }

    private void acquireWakeLock() {
        synchronized (this.mWakeLock) {
            this.mWakeLock.acquire();
            this.mQmsHandler.removeMessages(1);
            this.mQmsHandler.sendMessageDelayed(this.mQmsHandler.obtainMessage(1), this.mWakeLockTimeout);
        }
    }

    private void releaseWakeLockIfDone() {
        synchronized (this.mWakeLock) {
            try {
                if (this.mWakeLock.isHeld() && this.mRequestMessagesWaiting == 0) {
                    this.mQmsHandler.removeMessages(1);
                    this.mWakeLock.release();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void queueRequest(QmsRequest qmsRequest) {
        synchronized (this.mRequestsList) {
            this.mRequestsList.add(qmsRequest);
            this.mRequestMessagesWaiting++;
        }
    }

    public void processQmsResponse(Message message) {
        releaseWakeLockIfDone();
        int i = message.arg1;
        int i2 = message.arg2;
        Object obj = message.obj;
        log("processQmsResponse serial: " + i2 + " errorCode: " + i);
        QmsRequest qmsRequestFindAndRemoveRequestFromList = findAndRemoveRequestFromList(i2);
        if (qmsRequestFindAndRemoveRequestFromList == null) {
            log("Unexpected serial number in solicited response!serial: " + i2 + " error: " + i);
            return;
        }
        sendResponse(qmsRequestFindAndRemoveRequestFromList, i, obj);
    }

    private void sendResponse(QmsRequest qmsRequest, int i, Object obj) {
        if (qmsRequest == null) {
            return;
        }
        if (i != 5 && i != 0) {
            log("sendResponse serial: " + qmsRequest.mSerial + " errorCode: " + i + ", failed");
            qmsRequest.release();
            if (obj != null && (obj instanceof QmsMsgTunnelControllerBase.MessageRequestAsync)) {
                recordQmsResponseErrorCode(((QmsMsgTunnelControllerBase.MessageRequestAsync) obj).requestId, i);
                return;
            } else {
                recordQmsResponseErrorCode(-1, i);
                return;
            }
        }
        if (obj instanceof QmsMsgTunnelControllerBase.MessageRequestAsync) {
            if (((QmsMsgTunnelControllerBase.MessageRequestAsync) obj).requestId == 1000) {
                resetWakeLockAndRequestList();
                return;
            }
            log("sendResponse serial: " + qmsRequest.mSerial + ", successed");
            return;
        }
        if (obj instanceof ArrayList) {
            log("sendResponse serial: " + qmsRequest.mSerial + " nr5gNfArray: " + objToList(obj, Nr5gNflEntryTypeS.class) + ", successed");
        }
    }

    private QmsRequest findAndRemoveRequestFromList(int i) {
        synchronized (this.mRequestsList) {
            try {
                int size = this.mRequestsList.size();
                for (int i2 = 0; i2 < size; i2++) {
                    QmsRequest qmsRequest = (QmsRequest) this.mRequestsList.get(i2);
                    if (qmsRequest.mSerial == i) {
                        this.mRequestsList.remove(i2);
                        int i3 = this.mRequestMessagesWaiting;
                        if (i3 > 0) {
                            this.mRequestMessagesWaiting = i3 - 1;
                        }
                        return qmsRequest;
                    }
                }
                return null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetWakeLockAndRequestList() {
        log("resetWakeLockAndRequestList");
        clearRequestsList(-1);
        releaseWakeLockIfDone();
    }

    private void clearRequestsList(int i) {
        synchronized (this.mRequestsList) {
            try {
                int size = this.mRequestsList.size();
                log("mRequestList count=" + size);
                int i2 = 0;
                while (true) {
                    ArrayList arrayList = this.mRequestsList;
                    if (i2 < size) {
                        QmsRequest qmsRequest = (QmsRequest) arrayList.get(i2);
                        log(i2 + ": [" + qmsRequest.mSerial + "] " + requestToString(qmsRequest.mRequest));
                        sendResponse(qmsRequest, i, null);
                        i2++;
                    } else {
                        arrayList.clear();
                        this.mRequestMessagesWaiting = 0;
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String requestToString(int i) {
        if (i == 1000) {
            return "REQUEST_QMS_RAW";
        }
        return "<unknown request>";
    }

    private void recordQmsResponseErrorCode(int i, int i2) {
        HashMap map = new HashMap();
        map.put("responseRequestId", String.valueOf(i));
        map.put("responseErrorCode", String.valueOf(i2));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_qms_response", map);
    }

    private void localLog(String str, String str2) {
        QmsMsgTunnelController.localLog(LOG_TAG, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d(LOG_TAG + "[" + this.mPhoneId + "] ", str);
    }
}
