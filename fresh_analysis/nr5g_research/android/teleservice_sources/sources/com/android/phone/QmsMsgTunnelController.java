package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.utils.CountableHandler;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class QmsMsgTunnelController extends QmsMsgTunnelControllerBase {
    private static QmsMsgControllerHandler mQmsMsgHandler;
    protected final HandlerThread mWorkThread;
    private QmsMsgTunnelStrategy[] mQmsMsgTunnelStrategy = new QmsMsgTunnelStrategy[MiuiPhoneUtils.PHONE_COUNT];
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.QmsMsgTunnelController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            QmsMsgTunnelController.log("receive action".concat(action));
            if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                QmsMsgTunnelController.this.mInsertedSimCn = MiuiPhoneUtils.getInsertedSimCount();
                QmsMsgTunnelController.log("mInsertedSimCn=" + QmsMsgTunnelController.this.mInsertedSimCn);
                QmsMsgTunnelController qmsMsgTunnelController = QmsMsgTunnelController.this;
                if (qmsMsgTunnelController.mInsertedSimCn <= 0 || qmsMsgTunnelController.mHasRegQms) {
                    return;
                }
                qmsMsgTunnelController.mHasRegQms = true;
                QmsMsgTunnelController.mQmsMsgHandler.sendMessageDelayed(QmsMsgTunnelController.mQmsMsgHandler.obtainMessage(1003), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
            }
        }
    };
    protected final Context mContext = PhoneGlobals.getInstance();

    public static QmsMsgTunnelControllerBase make() {
        if (QmsMsgTunnelControllerBase.mInstance == null) {
            QmsMsgTunnelControllerBase.mInstance = new QmsMsgTunnelController();
        }
        return QmsMsgTunnelControllerBase.mInstance;
    }

    private QmsMsgTunnelController() {
        HandlerThread handlerThread = new HandlerThread("QmsMsgTunnelController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        QmsMsgControllerHandler qmsMsgControllerHandler = new QmsMsgControllerHandler(handlerThread.getLooper());
        mQmsMsgHandler = qmsMsgControllerHandler;
        qmsMsgControllerHandler.sendEmptyMessage(1000);
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone != null && MiuiPhoneUtils.isValidatePhoneId(phone.getPhoneId())) {
                int phoneId = phone.getPhoneId();
                this.mQmsMsgTunnelStrategy[phoneId] = new QmsMsgTunnelStrategy(phoneId);
            }
        }
        log("QmsMsgTunnelController: init");
    }

    class QmsMsgControllerHandler extends CountableHandler {
        public QmsMsgControllerHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1000:
                    QmsMsgTunnelController.this.initAction();
                    break;
                case 1001:
                    try {
                        QmsMsgTunnelControllerBase.MessageRequestAsync messageRequestAsync = (QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj;
                        if (messageRequestAsync != null && MiuiPhoneUtils.isValidatePhoneId(messageRequestAsync.slotId)) {
                            QmsMsgTunnelController.this.mQmsMsgTunnelStrategy[messageRequestAsync.slotId].invokeQmsRequestRaw(Message.obtain(QmsMsgTunnelController.mQmsMsgHandler, 1002, messageRequestAsync));
                        }
                        QmsMsgTunnelController.log("Invalid params for send qms request");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case 1002:
                    try {
                        QmsMsgTunnelControllerBase.MessageRequestAsync messageRequestAsync2 = (QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj;
                        if (messageRequestAsync2 != null && MiuiPhoneUtils.isValidatePhoneId(messageRequestAsync2.slotId)) {
                            QmsMsgTunnelController.this.mQmsMsgTunnelStrategy[messageRequestAsync2.slotId].processQmsResponse(message);
                        }
                        QmsMsgTunnelController.log("Invalid params for send qms request callback");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                    break;
                case 1003:
                    QmsMsgTunnelController qmsMsgTunnelController = QmsMsgTunnelController.this;
                    qmsMsgTunnelController.sendQmsRequestRawAsync(1000, 0, null, qmsMsgTunnelController.mTriggerReason);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        this.mInsertedSimCn = MiuiPhoneUtils.getInsertedSimCount();
        log("initAction: mInsertedSimCn=" + this.mInsertedSimCn + ", mHasRegQms=" + this.mHasRegQms);
        if (this.mInsertedSimCn > 0 && !this.mHasRegQms) {
            this.mHasRegQms = true;
            QmsMsgControllerHandler qmsMsgControllerHandler = mQmsMsgHandler;
            qmsMsgControllerHandler.sendMessageDelayed(qmsMsgControllerHandler.obtainMessage(1003), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        }
        registerSimStateReceiver();
    }

    private void registerSimStateReceiver() {
        this.mContext.registerReceiver(this.mIntentReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"), 2);
    }

    @Override // com.android.phone.QmsMsgTunnelControllerBase
    public boolean sendQmsRequestRawAsync(int i, int i2, Object obj, String str) {
        try {
            if (MiuiPhoneUtils.isSnsOptimizationSupported() && isValidRequestId(i) && MiuiPhoneUtils.isValidatePhoneId(i2)) {
                QmsClientBase qmsClientBase = QmsClientBase.getInstance();
                if (qmsClientBase != null && qmsClientBase.mQmsSwitchCloud) {
                    sendRequestAsync(1001, i, i2, obj, str);
                    return true;
                }
                log("sendQmsRequestRawAsync  qms cloud closed");
                return false;
            }
            log("sendQmsRequestRawAsync invalid params or platform");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isValidRequestId(int i) {
        if (i < 1000 || i > 1022) {
            return TelephonyManagerEx.getDefault().isPlatform8750() && i >= 1011 && i <= 1022;
        }
        return true;
    }

    private void sendRequestAsync(int i, int i2, int i3, Object obj, String str) {
        mQmsMsgHandler.obtainMessage(i, new QmsMsgTunnelControllerBase.MessageRequestAsync(i2, i3, obj, str)).sendToTarget();
    }

    public static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        HashMap map = QmsMsgTunnelControllerBase.sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(250));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    @Override // com.android.phone.QmsMsgTunnelControllerBase
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("QmsMsgTunnelController dump start------");
        HashMap map = QmsMsgTunnelControllerBase.sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("QmsMsgTunnelController dump end------");
        printWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("QmsMsgTunnelController", str);
    }
}
