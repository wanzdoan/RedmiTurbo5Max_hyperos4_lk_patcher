package com.android.phone;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.phone.utils.ModemUtils;
import com.xiaomi.mirilhook.MiRilHook;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class SpecialSimEx extends SpecialSimBase {
    private ModemUtils mModemUtils;
    private volatile boolean mIsSucessSendEnable = false;
    private final Handler mChildWorkHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.SpecialSimEx.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SpecialSimBase.log("msg.what = " + message.what);
            if (message.what == 200 && !SpecialSimEx.this.mIsSucessSendEnable) {
                SpecialSimEx specialSimEx = SpecialSimEx.this;
                specialSimEx.mIsSucessSendEnable = specialSimEx.sendEnableAtCmd();
            }
        }
    };

    public static SpecialSimBase make() {
        if (!(SpecialSimBase.mInstance instanceof SpecialSimEx)) {
            SpecialSimBase.mInstance = new SpecialSimEx();
        }
        return SpecialSimBase.mInstance;
    }

    protected SpecialSimEx() {
        if (ModemProvider.IS_QCOM) {
            this.mModemUtils = ModemUtils.init(SpecialSimBase.mContext);
        }
    }

    private synchronized void registerAtUrc() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                phone.registerForOemIndication(this.mWorkHandler, 101, phone);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendEnableAtCmd() {
        return FiveGManagerBase.getInstance().sendAtCmdSync(0, new String[]{"AT+EDMFAPP=20107,100,1,0,0,0,0", "+EDMFAPP"}, null);
    }

    @Override // com.android.phone.SpecialSimBase
    protected void initAction() {
        super.initAction();
        if (ModemProvider.IS_MTK) {
            registerAtUrc();
            this.mIsSucessSendEnable = sendEnableAtCmd();
            if (this.mIsSucessSendEnable) {
                return;
            }
            registerForRilConnected();
            return;
        }
        if (ModemProvider.IS_QCOM) {
            EventHubManager.getInstance().registerListener("telephony", "ril_ready", new RilHookStateCallback(), this.mWorkHandler);
        }
    }

    private void registerForRilConnected() {
        CommandsInterface commandsInterface;
        Phone defaultPhone = PhoneFactory.getDefaultPhone();
        if (defaultPhone == null || (commandsInterface = defaultPhone.mCi) == null) {
            return;
        }
        commandsInterface.registerForRilConnected(this.mChildWorkHandler, 200, (Object) null);
    }

    @Override // com.android.phone.SpecialSimBase
    protected void onAtUrcInd(Object obj) {
        AsyncResult asyncResult = (AsyncResult) obj;
        if (asyncResult.exception != null) {
            return;
        }
        int phoneId = ((Phone) asyncResult.userObj).getPhoneId();
        String str = (String) asyncResult.result;
        SpecialSimBase.log("curPhoneId " + phoneId + " onAtUrcInd:" + str);
        if (str.startsWith("+EDMFAPP:")) {
            String[] strArrSplit = str.split(",");
            SpecialSimBase.log("urc ind data: " + Arrays.toString(strArrSplit));
            if ("20107".equals(strArrSplit[0].split(":")[1].trim())) {
                String strTrim = strArrSplit[1].trim();
                if ("104".equals(strTrim)) {
                    notifyArrearsApnResult(Integer.parseInt(strArrSplit[2].trim()));
                } else if ("105".equals(strTrim)) {
                    notify5GRegisterFailResult(Integer.parseInt(strArrSplit[2].trim()), Integer.parseInt(strArrSplit[3].trim()));
                }
            }
        }
    }

    @Override // com.android.phone.SpecialSimBase
    public void sendInvalidSimState(int i, int i2) {
        String[] strArr;
        String str;
        if (ModemProvider.IS_MTK && this.mIsDoAction && this.setState) {
            SpecialSimBase.log("sendInvalidSimState phoneId=" + i + " state=" + i2);
            if (i2 == 1) {
                strArr = new String[]{"AT+ERSCFG=120,300,300,300,300,600,600,600,600,1800,1800,1800,1800,3600,3600,600,600,1800,1800,1800,3600,3600,3600", ""};
                str = "AT+ESRVREC=2,7200,100,1,180,600,180,600,";
            } else {
                strArr = new String[]{"AT+ERSCFG=20,20,60,60,60,60,90,90,90,90,90,90,180,180,180,180,180,180,360,360,360,360,360", ""};
                str = "AT+ESRVREC=5,200,30,0,600,180,600,180,";
            }
            SpecialSimBase.log("setInvalidSimState cmd: " + Arrays.toString(strArr));
            if (FiveGManagerBase.getInstance().sendAtCmdSync(i, strArr, null)) {
                strArr[0] = str;
                SpecialSimBase.log("setInvalidSimState cmd: " + Arrays.toString(strArr));
                FiveGManagerBase.getInstance().sendAtCmdSync(i, strArr, null);
            }
        }
    }

    @Override // com.android.phone.SpecialSimBase
    protected void registerSpecialSimRecognition() {
        if (ModemProvider.IS_QCOM) {
            if (this.mModemUtils == null) {
                SpecialSimBase.log("registerSpecialSimRecognition: mModemUtils is null");
                return;
            } else {
                this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.SpecialSimEx$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$registerSpecialSimRecognition$0();
                    }
                });
                return;
            }
        }
        super.registerSpecialSimRecognition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerSpecialSimRecognition$0() {
        this.mModemUtils.registerSpecialSimRecognition(2, this.mWorkHandler, 103, null);
    }

    @Override // com.android.phone.SpecialSimBase
    protected void process5GRegisterFailInd(Object obj) {
        if (!ModemProvider.IS_QCOM) {
            super.process5GRegisterFailInd(obj);
            return;
        }
        try {
            AsyncResult asyncResult = (AsyncResult) obj;
            SpecialSimBase.log("process5GRegisterFailInd ar=" + asyncResult);
            if (asyncResult != null && asyncResult.exception == null) {
                ByteBuffer byteBufferWrap = ByteBuffer.wrap((byte[]) asyncResult.result);
                byteBufferWrap.order(ByteOrder.nativeOrder());
                byte b = byteBufferWrap.get();
                byte b2 = byteBufferWrap.get();
                short s = byteBufferWrap.getShort();
                short s2 = byteBufferWrap.getShort();
                int i = byteBufferWrap.getInt();
                SpecialSimBase.localLog("process5GRegisterFailInd", "version=" + ((int) b) + ", cause=" + ((int) b2) + ", mnc=" + ((int) s) + ", mcc=" + ((int) s2) + ", sub=" + i);
                if (i >= 0 && i <= 1) {
                    notify5GRegisterFailResult(i, b2);
                    return;
                }
                SpecialSimBase.log("process5GRegisterFailInd error phone id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RilHookStateCallback implements BaseEvent.IEventListener {
        private RilHookStateCallback() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if ((baseEvent instanceof TelephonyEvent.RilEvent) && ((TelephonyEvent.RilEvent) baseEvent).isRilReady()) {
                SpecialSimBase.log("qcrilhookReady");
                SpecialSimEx.this.registerSpecialSimRecognition();
                if (SpecialSimEx.this.mModemUtils != null) {
                    SpecialSimEx.this.mModemUtils.registerOemHookRegistrant(SpecialSimEx.this.mWorkHandler, MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, null, "qualcomm.intent.action.ACTION_HYDRA_READY_IND");
                } else {
                    SpecialSimBase.log("RilHookStateCallback: mModemUtils is null");
                }
            }
        }
    }
}
