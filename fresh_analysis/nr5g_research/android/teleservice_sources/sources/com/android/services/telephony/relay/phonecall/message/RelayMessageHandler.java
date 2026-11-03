package com.android.services.telephony.relay.phonecall.message;

import android.net.Uri;
import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import com.android.phone.R;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.relay.phonecall.MiPhoneContinuityController;
import com.android.services.telephony.relay.phonecall.PhoneContinuityController;
import com.android.services.telephony.relay.phonecall.RelayConnection;
import com.android.services.telephony.relay.phonecall.RelayConnectionsManager;
import com.android.services.telephony.relay.phonecall.RelayLog;
import com.android.services.telephony.relay.phonecall.RelayManager;
import com.android.services.telephony.relay.phonecall.RelayPhoneAccountRegistry;
import com.android.services.telephony.relay.phonecall.RelayServiceFilterUtils;
import com.android.services.telephony.relay.phonecall.RelayServiceInfo;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.api.RelayApi;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestCallInfo;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestCallState;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestCallStateIdle;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestDial;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestOperate;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestRelease;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestRing;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestTransfer;
import com.android.services.telephony.relay.phonecall.message.request.RelayRequestWantRelay;
import com.android.services.telephony.relay.phonecall.message.request.RequestObject;
import com.android.services.telephony.relay.phonecall.message.response.RelayResponseDial;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import gov.nist.javax.sip.address.GenericURI;

/* JADX INFO: loaded from: classes.dex */
public class RelayMessageHandler {
    private static final String TAG = "RelayMessageHandler";
    private final MessagesManager mMessagesManager;
    private final RelayConnectionsManager mRelayConnectionsManager;

    public RelayMessageHandler(RelayConnectionsManager relayConnectionsManager, MessagesManager messagesManager) {
        this.mRelayConnectionsManager = relayConnectionsManager;
        this.mMessagesManager = messagesManager;
    }

    public void onRelayMessageReceived(String str) {
        if (str.contains("request?")) {
            onRequestRelayMessageReceived(str);
        } else {
            onResponseRelayMessageReceived(str);
        }
    }

    private void onRequestRelayMessageReceived(String str) {
        MessageCodecUtils.decodeRequestMessage(str, new MessageCodecUtils.DecodeCallback() { // from class: com.android.services.telephony.relay.phonecall.message.RelayMessageHandler.1
            @Override // com.android.services.telephony.relay.phonecall.message.MessageCodecUtils.DecodeCallback
            public void onDecode(String str2, String str3, String str4) {
                ResponseObject defaultSuccessResponse;
                RelayLog.logi(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, methodName:" + str2 + ", methodId:" + str3);
                if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
                    return;
                }
                if (RelayApi.RING.getMethodName().equals(str2)) {
                    RelayMessageHandler.this.mRelayConnectionsManager.clearDisconnectedConnection();
                    RelayRequestRing relayRequestRing = (RelayRequestRing) GsonUtil.fromJson(str4, RelayRequestRing.class);
                    String str5 = relayRequestRing != null ? relayRequestRing.address : "";
                    String strPadSupportRelay = RelayUtils.Relay.padSupportRelay(RelayUtils.System.getContext());
                    String str6 = null;
                    if (!TextUtils.isEmpty(strPadSupportRelay)) {
                        defaultSuccessResponse = new ResponseObject(ResponseObject.RELAY_FAILED_CODE, strPadSupportRelay, str5);
                    } else {
                        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
                        if (relayManager != null) {
                            relayManager.enableRelayPhoneAccount(true);
                        }
                        if (relayRequestRing != null) {
                            str6 = relayRequestRing.requestDeviceId;
                            RelayMessageHandler.this.handleRinging(relayRequestRing);
                        }
                        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_relay_call_ring");
                        defaultSuccessResponse = ResponseObject.getDefaultSuccessResponse(str5);
                        RelayUtils.Relay.onRelayStart();
                    }
                    RelayMessageHandler.this.mMessagesManager.sendResponseMessage(str2, str3, str6, defaultSuccessResponse);
                    return;
                }
                if (RelayApi.OPERATE.getMethodName().equals(str2)) {
                    RelayRequestOperate relayRequestOperate = (RelayRequestOperate) GsonUtil.fromJson(str4, RelayRequestOperate.class);
                    if (relayRequestOperate != null) {
                        boolean zHandleRelayOperate = RelayMessageHandler.this.handleRelayOperate(relayRequestOperate);
                        RelayMessageHandler relayMessageHandler = RelayMessageHandler.this;
                        if (zHandleRelayOperate) {
                            relayMessageHandler.mMessagesManager.phoneSendResponseMessage(str2, str3, relayRequestOperate.requestDeviceId, ResponseObject.getDefaultSuccessResponse(relayRequestOperate.address));
                            return;
                        } else {
                            relayMessageHandler.mMessagesManager.phoneSendResponseMessage(str2, str3, relayRequestOperate.requestDeviceId, ResponseObject.getDefaultFailedResponse(relayRequestOperate.address));
                            return;
                        }
                    }
                    RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, relayRequestOperate is null!");
                    return;
                }
                if (RelayApi.UPDATE_CALL_STATE.getMethodName().equals(str2)) {
                    RelayRequestCallState relayRequestCallState = (RelayRequestCallState) GsonUtil.fromJson(str4, RelayRequestCallState.class);
                    if (relayRequestCallState != null) {
                        RelayMessageHandler.this.handleRelayUpdateCallState(relayRequestCallState);
                        RelayMessageHandler.this.mMessagesManager.sendResponseMessage(str2, str3, relayRequestCallState.requestDeviceId, ResponseObject.getDefaultSuccessResponse(relayRequestCallState.address));
                        return;
                    } else {
                        RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, relayRequestCallState is null!");
                        return;
                    }
                }
                if (RelayApi.RELEASE.getMethodName().equals(str2)) {
                    RelayRequestRelease relayRequestRelease = (RelayRequestRelease) GsonUtil.fromJson(str4, RelayRequestRelease.class);
                    if (relayRequestRelease != null) {
                        RelayMessageHandler.this.handleRelayRelease(relayRequestRelease);
                        int i = relayRequestRelease.releaseType;
                        RelayMessageHandler relayMessageHandler2 = RelayMessageHandler.this;
                        if (i == 7) {
                            relayMessageHandler2.mMessagesManager.phoneSendResponseMessage(str2, str3, relayRequestRelease.requestDeviceId, ResponseObject.getDefaultSuccessResponse(relayRequestRelease.address));
                            return;
                        } else {
                            relayMessageHandler2.mMessagesManager.sendResponseMessage(str2, str3, relayRequestRelease.requestDeviceId, ResponseObject.getDefaultSuccessResponse(relayRequestRelease.address));
                            return;
                        }
                    }
                    RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, relayRequestRelease is null!");
                    return;
                }
                if (RelayApi.TRANSFER.getMethodName().equals(str2)) {
                    RelayRequestTransfer relayRequestTransfer = (RelayRequestTransfer) GsonUtil.fromJson(str4, RelayRequestTransfer.class);
                    if (relayRequestTransfer != null) {
                        RelayMessageHandler.this.handleTransfer(relayRequestTransfer);
                        RelayUtils.Relay.setDeviceInRelay(relayRequestTransfer.requestDeviceId);
                        RelayMessageHandler.this.mMessagesManager.sendResponseMessage(str2, str3, relayRequestTransfer.requestDeviceId, ResponseObject.getDefaultSuccessResponse(relayRequestTransfer.address));
                        RelayUtils.Relay.onRelayStart();
                        return;
                    }
                    RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, relayRequestTransfer is null!");
                    return;
                }
                if (RelayApi.UPDATE_CALL_INFO.getMethodName().equals(str2)) {
                    RelayRequestCallInfo relayRequestCallInfo = (RelayRequestCallInfo) GsonUtil.fromJson(str4, RelayRequestCallInfo.class);
                    if (relayRequestCallInfo != null) {
                        RelayManager relayManager2 = RelayUtils.Relay.getRelayManager();
                        if (relayManager2 != null) {
                            RelayConnection relayConnectionWithNumber = relayManager2.getRelayConnectionsManager().getRelayConnectionWithNumber(relayRequestCallInfo.address);
                            if (relayConnectionWithNumber != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("telecomm.EXTRA_CONTACT", relayRequestCallInfo.contactName);
                                relayConnectionWithNumber.putExtras(bundle);
                                relayConnectionWithNumber.onExtrasChanged(bundle);
                                RelayMessageHandler.this.mMessagesManager.sendResponseMessage(str2, str3, relayRequestCallInfo.requestDeviceId, ResponseObject.getDefaultSuccessResponse(relayRequestCallInfo.address));
                                return;
                            }
                            RelayMessageHandler.this.mMessagesManager.sendResponseMessage(str2, str3, relayRequestCallInfo.requestDeviceId, ResponseObject.getDefaultFailedResponse(relayRequestCallInfo.address));
                            return;
                        }
                        return;
                    }
                    RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, relayRequestCallInfo is null!");
                    return;
                }
                if (RelayApi.DIAL.getMethodName().equals(str2)) {
                    RelayRequestDial relayRequestDial = (RelayRequestDial) GsonUtil.fromJson(str4, RelayRequestDial.class);
                    if (relayRequestDial != null) {
                        RelayMessageHandler.this.handleRelayDialRequest(relayRequestDial);
                        RelayMessageHandler.this.mMessagesManager.sendResponseMessage(str2, str3, relayRequestDial.requestDeviceId, new RelayResponseDial(ResponseObject.getDefaultSuccessResponse(relayRequestDial.address), RelayUtils.System.getDeviceName()));
                        RelayUtils.Relay.onRelayStart();
                        return;
                    }
                    RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, relayRequestDial is null!");
                    return;
                }
                if (RelayApi.CALL_STATE_IDLE.getMethodName().equals(str2)) {
                    RelayRequestCallStateIdle relayRequestCallStateIdle = (RelayRequestCallStateIdle) GsonUtil.fromJson(str4, RelayRequestCallStateIdle.class);
                    if (relayRequestCallStateIdle != null) {
                        RelayManager relayManager3 = RelayUtils.Relay.getRelayManager();
                        if (relayManager3 != null) {
                            relayManager3.enableRelayPhoneAccount(false);
                        }
                        RelayUtils.Relay.onRelayEnd();
                        RelayMessageHandler.this.mMessagesManager.sendResponseMessage(str2, str3, relayRequestCallStateIdle.requestDeviceId, ResponseObject.getDefaultSuccessResponse(relayRequestCallStateIdle.address));
                        return;
                    }
                    RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, relayRequestCallStateIdle is null!");
                    return;
                }
                if (RelayApi.WANT_RELAY.getMethodName().equals(str2)) {
                    RelayLog.logi(RelayMessageHandler.TAG, "ApplePhone or MiPhone device create channel to wantRelay");
                    RelayRequestWantRelay relayRequestWantRelay = (RelayRequestWantRelay) GsonUtil.fromJson(str4, RelayRequestWantRelay.class);
                    if (relayRequestWantRelay != null) {
                        String str7 = relayRequestWantRelay.address;
                        String number = RelayUtils.Telephony.getNumber(RelayUtils.Relay.currentConnection);
                        Connection connection = RelayUtils.Relay.currentConnection;
                        if (connection != null && connection.getState() == 2 && str7.equals(number)) {
                            RelayUtils.Relay.addWantRelayForDeviceId(relayRequestWantRelay.requestDeviceId);
                            RelayLog.logi(RelayMessageHandler.TAG, "send ring request for wantRelay deviceId");
                            RelayUtils.Relay.maybeRelayCall(RelayUtils.System.getContext(), RelayUtils.Relay.currentallConnections, RelayUtils.Relay.currentConnection, relayRequestWantRelay.requestDeviceId);
                            return;
                        }
                        return;
                    }
                    RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, RelayRequestWantRelay is null!");
                    return;
                }
                RelayLog.loge(RelayMessageHandler.TAG, "onRequestRelayMessageReceived, unsupported method " + str2);
            }
        });
    }

    public void handleRinging(RelayRequestRing relayRequestRing) {
        if (relayRequestRing == null) {
            return;
        }
        String str = relayRequestRing.address;
        String str2 = relayRequestRing.hostDeviceName;
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS", Uri.fromParts(GenericURI.TEL, str, null));
        }
        bundle.putBoolean("telecomm.EXTRA_RELAY_CALL", true);
        bundle.putString("telecomm.EXTRA_RELAY_DEVICE_NAME", str2);
        if (RelayUtils.Relay.supportDualPhoneRelayCallFeature(RelayUtils.System.getContext())) {
            bundle.putBoolean("telecomm.EXTRA_REMOTE_TYPE", MiPhoneContinuityController.getInstance().getRemoteType());
        }
        try {
            ((TelecomManager) RelayUtils.System.getContext().getSystemService(TelecomManager.class)).addNewIncomingCall(RelayPhoneAccountRegistry.PHONE_ACCOUNT_HANDLE, bundle);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        RelayLog.logi(TAG, "handleRinging");
    }

    public void handleTransfer(RelayRequestTransfer relayRequestTransfer) {
        if (relayRequestTransfer == null) {
            return;
        }
        String str = relayRequestTransfer.address;
        String str2 = relayRequestTransfer.hostDeviceName;
        long j = relayRequestTransfer.connectTimeMillis;
        long j2 = relayRequestTransfer.connectDuration;
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS", Uri.fromParts(GenericURI.TEL, str, null));
        }
        bundle.putBoolean("telecomm.EXTRA_RELAY_CALL", true);
        bundle.putString("telecomm.EXTRA_RELAY_DEVICE_NAME", str2);
        bundle.putBoolean("telecomm.TRANSFER_CALL", true);
        bundle.putLong("telecomm.EXTRA_CONNECT_TIME", j);
        bundle.putLong("telecomm.EXTRA_CONNECT_DURATION", j2);
        try {
            ((TelecomManager) RelayUtils.System.getContext().getSystemService(TelecomManager.class)).addNewIncomingCall(RelayPhoneAccountRegistry.PHONE_ACCOUNT_HANDLE, bundle);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        RelayLog.logi(TAG, "handleTransfer");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleRelayOperate(RelayRequestOperate relayRequestOperate) {
        Connection localConnectionCallRelayAnswered;
        int i = relayRequestOperate.operateType;
        String str = relayRequestOperate.address;
        RelayManager relayManager = PhoneContinuityController.getInstance().getRelayManager();
        RelayLog.logi(TAG, "handleRelayOperate operate:" + i + " relayManager:" + relayManager);
        if (relayManager == null) {
            return false;
        }
        Connection localConnectionWithNumber = relayManager.getRelayConnectionsManager().getLocalConnectionWithNumber(str);
        RelayLog.logi(TAG, "handleRelayOperate operate:" + i + " connection:" + localConnectionWithNumber);
        if (localConnectionWithNumber == null) {
            return false;
        }
        if (!RelayUtils.Relay.isCallRelayed(localConnectionWithNumber)) {
            RelayLog.logi(TAG, "handleRelayOperate operate:" + i + " , not relayed call.");
            return false;
        }
        Bundle bundle = new Bundle();
        if (i == 0) {
            if (PhoneContinuityController.getInstance().isDeviceInRelay()) {
                for (String str2 : PhoneContinuityController.getInstance().getConnectedServiceDevices()) {
                    RelayLog.logi(TAG, "handleRelayOperate, already in relay, deviceId:" + str2 + " relayRequestOperate.requestDeviceId:" + relayRequestOperate.requestDeviceId);
                    if (str2.equals(PhoneContinuityController.getInstance().getDeviceInRelay())) {
                        if (relayManager.getRelayConnectionsManager() != null && (localConnectionCallRelayAnswered = relayManager.getRelayConnectionsManager().getLocalConnectionCallRelayAnswered()) != null) {
                            relayManager.sendReleaseCallRequest(str2, 5, localConnectionCallRelayAnswered);
                            relayManager.releaseRelayExtra(localConnectionCallRelayAnswered);
                        }
                    } else {
                        relayManager.sendReleaseCallRequest(str2, 5, localConnectionWithNumber);
                    }
                }
                relayManager.releaseRelayExtra(localConnectionWithNumber);
                return false;
            }
            for (String str3 : PhoneContinuityController.getInstance().getConnectedServiceDevices()) {
                RelayLog.logi(TAG, "handleRelayOperate deviceId:" + str3 + " relayRequestOperate.requestDeviceId:" + relayRequestOperate.requestDeviceId);
                if (!str3.equals(relayRequestOperate.requestDeviceId)) {
                    relayManager.sendReleaseCallRequest(str3, 3, localConnectionWithNumber);
                }
            }
            bundle.putBoolean("telecomm.EXTRA_RELAY_ANSWERED", true);
            bundle.putString("telecomm.EXTRA_RELAY_DEVICE_ID", relayRequestOperate.requestDeviceId);
            localConnectionWithNumber.putExtras(bundle);
            localConnectionWithNumber.onAnswer();
            PhoneContinuityController.getInstance().setDeviceInRelay(relayRequestOperate.requestDeviceId);
            RelayServiceFilterUtils.saveDeviceAnswered(relayRequestOperate.requestDeviceId);
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_relay_call_answered_by_remote");
        } else if (i == 1) {
            localConnectionWithNumber.onReject();
            for (String str4 : PhoneContinuityController.getInstance().getConnectedServiceDevices()) {
                if (!str4.equals(relayRequestOperate.requestDeviceId)) {
                    relayManager.sendReleaseCallRequest(str4, 4, localConnectionWithNumber);
                }
            }
        } else if (i == 2) {
            localConnectionWithNumber.onDisconnect();
        } else if (i == 3) {
            localConnectionWithNumber.onHold();
        } else if (i == 4) {
            localConnectionWithNumber.onUnhold();
        } else if (i == 5) {
            localConnectionWithNumber.onSilence();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRelayUpdateCallState(RelayRequestCallState relayRequestCallState) {
        RelayConnectionsManager relayConnectionsManager;
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            RelayConnection relayConnectionWithNumber = relayManager.getRelayConnectionsManager().getRelayConnectionWithNumber(relayRequestCallState.address);
            int i = relayRequestCallState.callState;
            if (relayConnectionWithNumber != null) {
                relayConnectionWithNumber.updateState(i, relayRequestCallState.disconnectCode);
                if (relayRequestCallState.callState == 6) {
                    RelayUtils.Relay.setDeviceInRelay(null);
                    return;
                }
                return;
            }
            if (i != 6 || (relayConnectionsManager = relayManager.getRelayConnectionsManager()) == null) {
                return;
            }
            RelayLog.logi(TAG, "Mark connection as disconnected");
            relayConnectionsManager.addDisconnectedConnection(relayRequestCallState.address);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRelayRelease(RelayRequestRelease relayRequestRelease) {
        RelayManager relayManager;
        RelayConnectionsManager relayConnectionsManager;
        int i = relayRequestRelease.releaseType;
        String str = relayRequestRelease.address;
        RelayLog.logi(TAG, "handleRelayRelease releaseType:" + i);
        if (i == 7) {
            if (!PhoneContinuityController.getInstance().isDeviceInRelay() || (relayManager = PhoneContinuityController.getInstance().getRelayManager()) == null || (relayConnectionsManager = relayManager.getRelayConnectionsManager()) == null) {
                return;
            }
            relayManager.releaseRelayExtra(relayConnectionsManager.getLocalConnectionWithNumber(str));
            PhoneContinuityController.getInstance().setDeviceInRelay(null);
            return;
        }
        RelayManager relayManager2 = RelayUtils.Relay.getRelayManager();
        if (relayManager2 == null) {
            return;
        }
        RelayConnection relayConnectionWithNumber = relayManager2.getRelayConnectionsManager().getRelayConnectionWithNumber(str);
        if (relayConnectionWithNumber != null) {
            relayConnectionWithNumber.forceDisconnected(relayConnectionWithNumber.getDisconnectCause(RelayRequestRelease.getDisconnectCause(i)));
            if (i == 1 || i == 2 || (i == 0 && RelayUtils.System.isUnlocked())) {
                RelayUtils.System.showToast(R.string.relay_call_answered_by_other_devices);
            }
        }
        RelayUtils.Relay.setDeviceInRelay(null);
    }

    private void onResponseRelayMessageReceived(String str) {
        MessageCodecUtils.decodeResponseMessage(str, new MessageCodecUtils.DecodeCallback() { // from class: com.android.services.telephony.relay.phonecall.message.RelayMessageHandler.2
            @Override // com.android.services.telephony.relay.phonecall.message.MessageCodecUtils.DecodeCallback
            public void onDecode(String str2, final String str3, String str4) {
                RelayServiceInfo relayServiceInfo;
                RelayManager relayManager;
                RelayManager relayManager2;
                RelayConnectionsManager relayConnectionsManager;
                RelayLog.logi(RelayMessageHandler.TAG, "onResponseRelayMessageReceived methodName:" + str2 + ", methodId:" + str3);
                if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
                    RelayMessageHandler.this.mMessagesManager.removeRequestMessage(str3);
                    return;
                }
                final ResponseObject responseObject = (ResponseObject) GsonUtil.fromJson(str4, ResponseObject.class);
                if (responseObject == null) {
                    RelayLog.logi(RelayMessageHandler.TAG, "onResponseRelayMessageReceived responseObject:" + responseObject);
                    RelayMessageHandler.this.mMessagesManager.removeRequestMessage(str3);
                    return;
                }
                if (!RelayMessageHandler.this.mMessagesManager.hasMessage(str3)) {
                    RelayLog.logi(RelayMessageHandler.TAG, "onResponseRelayMessageReceived， the message has been processed");
                    return;
                }
                int i = responseObject.code;
                boolean z = false;
                if (i != 200) {
                    if (i == 408) {
                        if (RelayApi.TRANSFER.getMethodName().equals(str2) && (relayManager2 = PhoneContinuityController.getInstance().getRelayManager()) != null && (relayConnectionsManager = relayManager2.getRelayConnectionsManager()) != null) {
                            relayManager2.releaseRelayExtra(relayConnectionsManager.getLocalConnectionWithNumber(responseObject.address));
                        }
                        RelayMessageHandler.this.mMessagesManager.removeRequestMessage(str3);
                        return;
                    }
                    if (i == 500) {
                        if (RelayApi.RING.getMethodName().equals(str2)) {
                            RelayLog.logd(RelayMessageHandler.TAG, "Relay failed, device:" + responseObject.responseDeviceId);
                        } else if (RelayApi.DIAL.getMethodName().equals(str2)) {
                            RelayLog.logd(RelayMessageHandler.TAG, "Place relay call failed, device:" + responseObject.responseDeviceId);
                        } else if (RelayApi.UPDATE_CALL_INFO.getMethodName().equals(str2)) {
                            RelayLog.logd(RelayMessageHandler.TAG, "Update contact info failed, deviceId" + responseObject.responseDeviceId);
                            z = true;
                        } else if (RelayApi.WANT_RELAY.getMethodName().equals(str2)) {
                            RelayLog.logd(RelayMessageHandler.TAG, "Want relay call failed, device:" + responseObject.responseDeviceId);
                        }
                        final String message = RelayMessageHandler.this.mMessagesManager.getMessage(str3);
                        if (z && !TextUtils.isEmpty(message)) {
                            RelayUtils.MainThread.runOnUiThreadDelay(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.message.RelayMessageHandler.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Connection localConnectionWithNumber = RelayMessageHandler.this.mRelayConnectionsManager.getLocalConnectionWithNumber(responseObject.address);
                                    RelayMessageHandler.this.mMessagesManager.removeRequestMessage(str3);
                                    if (localConnectionWithNumber != null) {
                                        String strGenerateMessageId = RequestObject.generateMessageId();
                                        RelayLog.logd(RelayMessageHandler.TAG, "Resend message, old:" + str3 + ", new:" + strGenerateMessageId);
                                        RelayMessageHandler.this.mMessagesManager.phoneSendRequestMessage(responseObject.responseDeviceId, strGenerateMessageId, message.replaceFirst(str3, strGenerateMessageId));
                                    }
                                }
                            }, 500L);
                            return;
                        } else {
                            RelayMessageHandler.this.mMessagesManager.removeRequestMessage(str3);
                            return;
                        }
                    }
                    RelayMessageHandler.this.mMessagesManager.removeRequestMessage(str3);
                    return;
                }
                RelayMessageHandler.this.mMessagesManager.removeRequestMessage(str3);
                if (RelayApi.RING.getMethodName().equals(str2)) {
                    Connection localConnectionWithNumber = RelayMessageHandler.this.mRelayConnectionsManager.getLocalConnectionWithNumber(responseObject.address);
                    if (localConnectionWithNumber != null) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("telecomm.EXTRA_CALL_RELAYING", false);
                        bundle.putBoolean("telecomm.EXTRA_CALL_RELAYED", true);
                        localConnectionWithNumber.putExtras(bundle);
                        RelayMessageHandler.this.mRelayConnectionsManager.addRelayedLocalConnection(localConnectionWithNumber);
                        return;
                    }
                    return;
                }
                if (RelayApi.DIAL.getMethodName().equals(str2)) {
                    RelayMessageHandler.this.handleRelayDialResponse((RelayResponseDial) GsonUtil.fromJson(str4, RelayResponseDial.class));
                    RelayUtils.Relay.setDeviceInRelay(responseObject.responseDeviceId);
                    return;
                }
                if (RelayApi.TRANSFER.getMethodName().equals(str2)) {
                    Connection localConnectionWithNumber2 = RelayMessageHandler.this.mRelayConnectionsManager.getLocalConnectionWithNumber(responseObject.address);
                    if (PhoneContinuityController.getInstance().isDeviceInRelay() && localConnectionWithNumber2 != null && (relayManager = PhoneContinuityController.getInstance().getRelayManager()) != null) {
                        relayManager.sendReleaseCallRequest(PhoneContinuityController.getInstance().getDeviceInRelay(), 2, localConnectionWithNumber2);
                    }
                    if (localConnectionWithNumber2 == null || !RelayUtils.Relay.isCallRelayed(localConnectionWithNumber2)) {
                        return;
                    }
                    Bundle bundle2 = new Bundle();
                    bundle2.putBoolean("telecomm.EXTRA_RELAY_ANSWERED", true);
                    localConnectionWithNumber2.putExtras(bundle2);
                    PhoneContinuityController.getInstance().setDeviceInRelay(responseObject.responseDeviceId);
                    RelayUtils.System.showToast(R.string.relay_call_answered_by_other_devices);
                    return;
                }
                if (RelayApi.RELEASE.getMethodName().equals(str2) && (relayServiceInfo = PhoneContinuityController.getInstance().getRelayServiceInfo(responseObject.responseDeviceId)) != null && relayServiceInfo.mDeviceType == 11) {
                    RelayLog.logi(RelayMessageHandler.TAG, "Remove ApplePhone device for turn to miphone answer");
                    PhoneContinuityController.getInstance().removeDevice(responseObject.responseDeviceId);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRelayDialRequest(RelayRequestDial relayRequestDial) {
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("telecomm.EXTRA_CALL_RELAYED", true);
        bundle2.putBoolean("telecomm.EXTRA_RELAY_ANSWERED", true);
        bundle2.putString("telecomm.EXTRA_RELAY_DEVICE_ID", relayRequestDial.requestDeviceId);
        bundle.putBundle("android.telecom.extra.OUTGOING_CALL_EXTRAS", bundle2);
        try {
            ((TelecomManager) RelayUtils.System.getContext().getSystemService(TelecomManager.class)).placeCall(Uri.fromParts(GenericURI.TEL, relayRequestDial.address, null), bundle);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        PhoneContinuityController.getInstance().setDeviceInRelay(relayRequestDial.requestDeviceId);
        RelayLog.logi(TAG, "handleRelayDialRequest");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRelayDialResponse(RelayResponseDial relayResponseDial) {
        if (relayResponseDial == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", RelayPhoneAccountRegistry.PHONE_ACCOUNT_HANDLE);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("telecomm.EXTRA_RELAY_CALL", true);
        bundle2.putString("telecomm.EXTRA_RELAY_DEVICE_NAME", relayResponseDial.hostDeviceName);
        bundle.putBundle("android.telecom.extra.OUTGOING_CALL_EXTRAS", bundle2);
        bundle.putBoolean("telecomm.RELAY_DIAL", true);
        Uri uriFromParts = Uri.fromParts(GenericURI.TEL, relayResponseDial.address, null);
        bundle2.putParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS", uriFromParts);
        try {
            ((TelecomManager) RelayUtils.System.getContext().getSystemService(TelecomManager.class)).placeCall(uriFromParts, bundle);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        RelayLog.logi(TAG, "handleRelayDialResponse");
    }
}
