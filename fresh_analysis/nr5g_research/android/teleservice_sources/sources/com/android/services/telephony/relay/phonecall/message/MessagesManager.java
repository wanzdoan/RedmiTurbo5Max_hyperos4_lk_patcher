package com.android.services.telephony.relay.phonecall.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.services.telephony.relay.phonecall.MiPhoneContinuityController;
import com.android.services.telephony.relay.phonecall.PadContinuityController;
import com.android.services.telephony.relay.phonecall.PhoneContinuityController;
import com.android.services.telephony.relay.phonecall.RelayLog;
import com.android.services.telephony.relay.phonecall.RelayManager;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestBase;
import com.android.services.telephony.relay.phonecall.message.request.RequestObject;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class MessagesManager {
    private static final int MESSAGE_TIMER = 0;
    private static final int REQUEST_FROM_CLIENT = 2;
    private static final int REQUEST_FROM_SERVER = 1;
    private static final String TAG = "MessagesManager";
    private static final long TIMEOUT_MILLIS = 10000;
    private final Map mRequestMessages = new HashMap();
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.services.telephony.relay.phonecall.message.MessagesManager.1
        @Override // android.os.Handler
        public void handleMessage(final Message message) {
            if (message.what == 0) {
                Object obj = message.obj;
                if (obj instanceof String) {
                    String str = (String) obj;
                    if (MessagesManager.this.hasMessage(str)) {
                        MessageCodecUtils.decodeRequestMessage(MessagesManager.this.getMessage(str), new MessageCodecUtils.DecodeCallback(this) { // from class: com.android.services.telephony.relay.phonecall.message.MessagesManager.1.1
                            @Override // com.android.services.telephony.relay.phonecall.message.MessageCodecUtils.DecodeCallback
                            public void onDecode(String str2, String str3, String str4) {
                                RelayManager relayManager;
                                RelayRequestBase relayRequestBase = (RelayRequestBase) GsonUtil.fromJson(str4, RelayRequestBase.class);
                                if (relayRequestBase == null || TextUtils.isEmpty(relayRequestBase.address) || TextUtils.isEmpty(relayRequestBase.requestDeviceId)) {
                                    RelayLog.loge(MessagesManager.TAG, "Timeout parse error");
                                    return;
                                }
                                String strEncodeResponseMessage = MessageCodecUtils.encodeResponseMessage(str2, str3, ResponseObject.getDefaultTimeoutResponse(relayRequestBase.address));
                                int i = message.arg1;
                                if (i == 1) {
                                    relayManager = PhoneContinuityController.getInstance().getRelayManager();
                                } else {
                                    relayManager = i == 2 ? RelayUtils.Relay.getRelayManager() : null;
                                }
                                if (relayManager != null) {
                                    relayManager.onRelayMessageReceived(strEncodeResponseMessage);
                                }
                                RelayLog.logi(MessagesManager.TAG, "Timeout messageId:" + str3);
                            }
                        });
                    }
                }
            }
        }
    };

    public void addRequestMessage(String str, String str2) {
        if (this.mRequestMessages.containsKey(str)) {
            return;
        }
        this.mRequestMessages.put(str, str2);
        RelayLog.logi(TAG, "addRequestMessage messageId:" + str);
    }

    public void removeRequestMessage(String str) {
        if (this.mRequestMessages.containsKey(str)) {
            this.mRequestMessages.remove(str);
            RelayLog.logi(TAG, "removeRequestMessage messageId:" + str);
        }
    }

    public String getMessage(String str) {
        return (String) this.mRequestMessages.get(str);
    }

    public boolean hasMessage(String str) {
        return this.mRequestMessages.containsKey(str);
    }

    public void padSendRequestMessage(RequestObject requestObject) {
        RelayRequestBase relayRequestBase = (RelayRequestBase) GsonUtil.fromJson(requestObject.getParamJson(), RelayRequestBase.class);
        if (relayRequestBase == null || TextUtils.isEmpty(relayRequestBase.address) || TextUtils.isEmpty(relayRequestBase.requestDeviceId)) {
            RelayLog.loge(TAG, "padSendRequestMessage missing parameter");
            return;
        }
        String strEncodeRequestMessage = MessageCodecUtils.encodeRequestMessage(requestObject);
        RelayUtils.Relay.sendRelayMessage(strEncodeRequestMessage);
        addRequestMessage(requestObject.getMethodId(), strEncodeRequestMessage);
        startMessageTimer(requestObject.getMethodId(), 2);
    }

    public void sendWantRelayRequestMessage(String str, RequestObject requestObject) {
        if (TextUtils.isEmpty(str)) {
            RelayLog.loge(TAG, "sendWantRelayRequestMessage device id is empty");
            return;
        }
        if (requestObject == null) {
            RelayLog.loge(TAG, "sendWantRelayRequestMessage requestObject is null");
            return;
        }
        RelayRequestBase relayRequestBase = (RelayRequestBase) GsonUtil.fromJson(requestObject.getParamJson(), RelayRequestBase.class);
        if (relayRequestBase == null || TextUtils.isEmpty(relayRequestBase.address) || TextUtils.isEmpty(relayRequestBase.requestDeviceId)) {
            RelayLog.loge(TAG, "sendWantRelayRequestMessage missing parameter");
            return;
        }
        String strEncodeRequestMessage = MessageCodecUtils.encodeRequestMessage(requestObject);
        MiPhoneContinuityController.getInstance().sendWantRelayRequest(strEncodeRequestMessage, str);
        addRequestMessage(requestObject.getMethodId(), strEncodeRequestMessage);
        startMessageTimer(requestObject.getMethodId(), 2);
    }

    public void phoneSendRequestMessage(String str, RequestObject requestObject) {
        if (TextUtils.isEmpty(str)) {
            RelayLog.loge(TAG, "phoneSendRequestMessage device id is empty");
            return;
        }
        RelayRequestBase relayRequestBase = (RelayRequestBase) GsonUtil.fromJson(requestObject.getParamJson(), RelayRequestBase.class);
        if (relayRequestBase == null || TextUtils.isEmpty(relayRequestBase.address) || TextUtils.isEmpty(relayRequestBase.requestDeviceId)) {
            RelayLog.loge(TAG, "phoneSendRequestMessage missing parameter");
            return;
        }
        String strEncodeRequestMessage = MessageCodecUtils.encodeRequestMessage(requestObject);
        PhoneContinuityController.getInstance().sendRelayMessage(strEncodeRequestMessage, str);
        addRequestMessage(requestObject.getMethodId(), strEncodeRequestMessage);
        startMessageTimer(requestObject.getMethodId(), 1);
    }

    public void phoneSendRequestMessage(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            RelayLog.loge(TAG, "phoneSendRequestMessage device id is empty");
        } else {
            if (TextUtils.isEmpty(str3)) {
                RelayLog.loge(TAG, "phoneSendRequestMessage messageString is empty");
                return;
            }
            PhoneContinuityController.getInstance().sendRelayMessage(str3, str);
            addRequestMessage(str2, str3);
            startMessageTimer(str2, 1);
        }
    }

    public void sendResponseMessage(String str, String str2, String str3, ResponseObject responseObject) {
        String strEncodeResponseMessage = MessageCodecUtils.encodeResponseMessage(str, str2, responseObject);
        if (RelayUtils.Relay.isRelaySide()) {
            PadContinuityController.getInstance().sendRelayMessage(strEncodeResponseMessage);
        } else {
            MiPhoneContinuityController.getInstance().sendRelayMessage(strEncodeResponseMessage);
        }
    }

    public void phoneSendResponseMessage(String str, String str2, String str3, ResponseObject responseObject) {
        PhoneContinuityController.getInstance().sendRelayMessage(MessageCodecUtils.encodeResponseMessage(str, str2, responseObject), str3);
    }

    private void startMessageTimer(String str, int i) {
        Message messageObtain = Message.obtain();
        messageObtain.what = 0;
        messageObtain.obj = str;
        messageObtain.arg1 = i;
        this.mHandler.sendMessageDelayed(messageObtain, TIMEOUT_MILLIS);
    }
}
