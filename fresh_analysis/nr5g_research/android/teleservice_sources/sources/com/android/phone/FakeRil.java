package com.android.phone;

import android.content.Context;
import android.net.KeepalivePacketData;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.data.DataProfile;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.TrafficDescriptor;
import android.telephony.emergency.EmergencyNumber;
import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.RadioCapability;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cdma.CdmaSmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class FakeRil extends BaseCommands {
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private Registrant mProactiveCmdRegistrant;

    public void acceptCall(Message message) {
    }

    public void acknowledgeIncomingGsmSmsWithPdu(boolean z, String str, Message message) {
    }

    public void acknowledgeLastIncomingCdmaSms(boolean z, int i, Message message) {
    }

    public void acknowledgeLastIncomingGsmSms(boolean z, int i, Message message) {
    }

    public void allocatePduSessionId(Message message) {
    }

    public void cancelHandover(Message message, int i) {
    }

    public void cancelPendingUssd(Message message) {
    }

    public void changeBarringPassword(String str, String str2, String str3, Message message) {
    }

    public void changeIccPin(String str, String str2, Message message) {
    }

    public void changeIccPin2ForApp(String str, String str2, String str3, Message message) {
    }

    public void changeIccPinForApp(String str, String str2, String str3, Message message) {
    }

    public void conference(Message message) {
    }

    public void deactivateDataCall(int i, int i2, Message message) {
    }

    public void deleteSmsOnSim(int i, Message message) {
    }

    public void dial(String str, boolean z, EmergencyNumber emergencyNumber, boolean z2, int i, Message message) {
    }

    public void dial(String str, boolean z, EmergencyNumber emergencyNumber, boolean z2, int i, UUSInfo uUSInfo, Message message) {
    }

    public void exitEmergencyCallbackMode(Message message) {
    }

    public void explicitCallTransfer(Message message) {
    }

    public void getAllowedNetworkTypesBitmap(Message message) {
    }

    public void getAvailableNetworks(Message message) {
    }

    public void getBasebandVersion(Message message) {
    }

    public void getCLIR(Message message) {
    }

    public void getCdmaBroadcastConfig(Message message) {
    }

    public void getCurrentCalls(Message message) {
    }

    public void getDataCallList(Message message) {
    }

    public void getDataRegistrationState(Message message) {
    }

    public void getDeviceIdentity(Message message) {
    }

    public void getGsmBroadcastConfig(Message message) {
    }

    public void getHardwareConfig(Message message) {
    }

    public void getIMSI(Message message) {
    }

    public void getIMSIForApp(String str, Message message) {
    }

    public void getIccCardStatus(Message message) {
    }

    public void getImei(Message message) {
    }

    public void getImsRegistrationState(Message message) {
    }

    public void getLastCallFailCause(Message message) {
    }

    public void getMute(Message message) {
    }

    public void getNetworkSelectionMode(Message message) {
    }

    public void getOperator(Message message) {
    }

    public void getPreferredNetworkType(Message message) {
    }

    public void getPreferredVoicePrivacy(Message message) {
    }

    public void getRadioCapability(Message message) {
    }

    public void getSignalStrength(Message message) {
    }

    public void getSmscAddress(Message message) {
    }

    public void getVoiceRadioTechnology(Message message) {
    }

    public void getVoiceRegistrationState(Message message) {
    }

    public void handleCallSetupRequestFromSim(boolean z, Message message) {
    }

    public void hangupConnection(int i, Message message) {
    }

    public void hangupForegroundResumeBackground(Message message) {
    }

    public void hangupWaitingOrBackground(Message message) {
    }

    public void iccCloseLogicalChannel(int i, boolean z, Message message) {
    }

    public void iccIO(int i, int i2, String str, int i3, int i4, int i5, String str2, String str3, Message message) {
    }

    public void iccIOForApp(int i, int i2, String str, int i3, int i4, int i5, String str2, String str3, String str4, Message message) {
    }

    public void iccOpenLogicalChannel(String str, int i, Message message) {
    }

    public void iccTransmitApduBasicChannel(int i, int i2, int i3, int i4, int i5, String str, Message message) {
    }

    public void iccTransmitApduLogicalChannel(int i, int i2, int i3, int i4, int i5, int i6, String str, boolean z, Message message) {
    }

    public void nvResetConfig(int i, Message message) {
    }

    public void queryCLIP(Message message) {
    }

    public void queryCallForwardStatus(int i, int i2, String str, Message message) {
    }

    public void queryCallWaiting(int i, Message message) {
    }

    public void queryFacilityLock(String str, String str2, int i, Message message) {
    }

    public void queryFacilityLockForApp(String str, String str2, int i, String str3, Message message) {
    }

    public void queryTTYMode(Message message) {
    }

    public void rejectCall(Message message) {
    }

    public void releasePduSessionId(Message message, int i) {
    }

    public void reportSmsMemoryStatus(boolean z, Message message) {
    }

    public void reportStkServiceIsRunning(Message message) {
    }

    public void requestIccSimAuthentication(int i, String str, String str2, Message message) {
    }

    public void requestShutdown(Message message) {
    }

    public void sendBurstDtmf(String str, int i, int i2, Message message) {
    }

    public void sendCdmaSMSExpectMore(byte[] bArr, Message message) {
    }

    public void sendCdmaSms(byte[] bArr, Message message) {
    }

    public void sendDeviceState(int i, boolean z, Message message) {
    }

    public void sendDtmf(char c, Message message) {
    }

    public void sendEnvelope(String str, Message message) {
    }

    public void sendEnvelopeWithStatus(String str, Message message) {
    }

    public void sendImsCdmaSms(byte[] bArr, int i, int i2, Message message) {
    }

    public void sendImsGsmSms(String str, String str2, int i, int i2, Message message) {
    }

    public void sendSMS(String str, String str2, Message message) {
    }

    public void sendSMSExpectMore(String str, String str2, Message message) {
    }

    public void sendTerminalResponse(String str, Message message) {
    }

    public void sendUSSD(String str, Message message) {
    }

    public void separateConnection(int i, Message message) {
    }

    public void setAllowedNetworkTypesBitmap(int i, Message message) {
    }

    public void setCLIR(int i, Message message) {
    }

    public void setCallForward(int i, int i2, int i3, String str, int i4, Message message) {
    }

    public void setCallWaiting(boolean z, int i, Message message) {
    }

    public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo imsiEncryptionInfo, Message message) {
    }

    public void setCdmaBroadcastActivation(boolean z, Message message) {
    }

    public void setCdmaBroadcastConfig(CdmaSmsBroadcastConfigInfo[] cdmaSmsBroadcastConfigInfoArr, Message message) {
    }

    public void setDataProfile(DataProfile[] dataProfileArr, Message message) {
    }

    public void setFacilityLock(String str, boolean z, String str2, int i, Message message) {
    }

    public void setFacilityLockForApp(String str, boolean z, String str2, int i, String str3, Message message) {
    }

    public void setGsmBroadcastActivation(boolean z, Message message) {
    }

    public void setGsmBroadcastConfig(SmsBroadcastConfigInfo[] smsBroadcastConfigInfoArr, Message message) {
    }

    public void setInitialAttachApn(DataProfile dataProfile, Message message) {
    }

    public void setLinkCapacityReportingCriteria(int i, int i2, int i3, int[] iArr, int[] iArr2, int i4, Message message) {
    }

    public void setLocationUpdates(boolean z, Message message) {
    }

    public void setMute(boolean z, Message message) {
    }

    public void setNetworkSelectionModeAutomatic(Message message) {
    }

    public void setNetworkSelectionModeManual(String str, int i, Message message) {
    }

    public void setOnNITZTime(Handler handler, int i, Object obj) {
    }

    public void setPhoneType(int i) {
    }

    public void setPreferredNetworkType(int i, Message message) {
    }

    public void setPreferredVoicePrivacy(boolean z, Message message) {
    }

    public void setRadioCapability(RadioCapability radioCapability, Message message) {
    }

    public void setRadioPower(boolean z, Message message) {
    }

    public void setSignalStrengthReportingCriteria(List list, Message message) {
    }

    public void setSmscAddress(String str, Message message) {
    }

    public void setTTYMode(int i, Message message) {
    }

    public void setUnsolResponseFilter(int i, Message message) {
    }

    public void setupDataCall(int i, DataProfile dataProfile, boolean z, int i2, LinkProperties linkProperties, int i3, NetworkSliceInfo networkSliceInfo, TrafficDescriptor trafficDescriptor, boolean z2, Message message) {
    }

    public void startDtmf(char c, Message message) {
    }

    public void startHandover(Message message, int i) {
    }

    public void startNattKeepalive(int i, KeepalivePacketData keepalivePacketData, int i2, Message message) {
    }

    public void startNetworkScan(NetworkScanRequest networkScanRequest, Message message) {
    }

    public void stopDtmf(Message message) {
    }

    public void stopNattKeepalive(int i, Message message) {
    }

    public void stopNetworkScan(Message message) {
    }

    public void supplyIccPin(String str, Message message) {
    }

    public void supplyIccPin2ForApp(String str, String str2, Message message) {
    }

    public void supplyIccPinForApp(String str, String str2, Message message) {
    }

    public void supplyIccPuk2ForApp(String str, String str2, String str3, Message message) {
    }

    public void supplyIccPukForApp(String str, String str2, String str3, Message message) {
    }

    public void supplyNetworkDepersonalization(String str, Message message) {
    }

    public void supplySimDepersonalization(IccCardApplicationStatus.PersoSubState persoSubState, String str, Message message) {
    }

    public void switchWaitingOrHoldingAndActive(Message message) {
    }

    public void writeSmsToRuim(int i, byte[] bArr, Message message) {
    }

    public void writeSmsToSim(int i, String str, String str2, Message message) {
    }

    FakeRil(Context context) {
        super(context);
        HandlerThread handlerThread = new HandlerThread("FakeRilThread");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
    }

    public void sendProactiveCmdToCatService(final int i, final String str) {
        final UiccProfile uiccProfileForPhone = UiccController.getInstance().getUiccProfileForPhone(i);
        this.mHandler.post(new Runnable() { // from class: com.android.phone.FakeRil.1
            @Override // java.lang.Runnable
            public void run() {
                CatService catService = CatService.getInstance((CommandsInterface) null, (Context) null, (UiccProfile) null, i);
                if (catService == null) {
                    return;
                }
                catService.dispose();
                FakeRil fakeRil = FakeRil.this;
                CatService.getInstance(fakeRil, ((BaseCommands) fakeRil).mContext, uiccProfileForPhone, i);
                FakeRil.this.mProactiveCmdRegistrant.notifyRegistrant(new AsyncResult((Object) null, str, (Throwable) null));
            }
        });
    }

    public void setOnCatProactiveCmd(Handler handler, int i, Object obj) {
        this.mProactiveCmdRegistrant = new Registrant(handler, i, obj);
    }
}
