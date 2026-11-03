package com.android.internal.telephony.ntnphone;

import android.content.Context;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.telephony.NetworkScanRequest;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
abstract class NtnPhoneBase extends Phone {
    private PhoneConstants.State mState;

    public void activateCellBroadcastSms(int i, Message message) {
    }

    public boolean canConference() {
        return true;
    }

    public boolean canTransfer() {
        return false;
    }

    public void conference() {
    }

    public boolean disableDataConnectivity() {
        return false;
    }

    public void disableLocationUpdates() {
    }

    public boolean enableDataConnectivity() {
        return false;
    }

    public void enableLocationUpdates() {
    }

    public void explicitCallTransfer() {
    }

    public void getAvailableNetworks(Message message) {
    }

    public abstract Call getBackgroundCall();

    public void getCallBarring(String str, String str2, Message message, int i) {
    }

    @Override // com.android.internal.telephony.Phone
    public boolean getCallForwardingIndicator() {
        return false;
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void getCallForwardingOption(int i, int i2, Message message) {
    }

    public void getCallForwardingOption(int i, Message message) {
    }

    public void getCallWaiting(Message message) {
    }

    public void getCellBroadcastSmsConfig(Message message) {
    }

    public int getDataActivityState() {
        return 0;
    }

    public boolean getDataRoamingEnabled() {
        return false;
    }

    public String getDeviceId() {
        return null;
    }

    public String getDeviceSvn() {
        return null;
    }

    public abstract Call getForegroundCall();

    public String getGroupIdLevel1() {
        return null;
    }

    public String getGroupIdLevel2() {
        return null;
    }

    @Override // com.android.internal.telephony.Phone
    public IccCard getIccCard() {
        return null;
    }

    @Override // com.android.internal.telephony.Phone
    public IccFileHandler getIccFileHandler() {
        return null;
    }

    public IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager() {
        return null;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean getIccRecordsLoaded() {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public String getIccSerialNumber() {
        return null;
    }

    public String getImei() {
        return null;
    }

    public int getImeiType() {
        return -1;
    }

    public String getLine1AlphaTag() {
        return null;
    }

    public String getLine1Number() {
        return null;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean getMessageWaitingIndicator() {
        return false;
    }

    public boolean getMute() {
        return false;
    }

    public void getOutgoingCallerIdDisplay(Message message) {
    }

    @Override // com.android.internal.telephony.Phone
    public int getPhoneType() {
        return 7;
    }

    public abstract Call getRingingCall();

    public String getSubscriberId() {
        return null;
    }

    public String getVoiceMailAlphaTag() {
        return null;
    }

    public String getVoiceMailNumber() {
        return null;
    }

    public boolean handleInCallMmiCommands(String str) {
        return false;
    }

    public boolean handlePinMmi(String str) {
        return false;
    }

    public boolean handleUssdRequest(String str, ResultReceiver resultReceiver) {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isDataAllowed() {
        return false;
    }

    public boolean isUserDataEnabled() {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isVideoEnabled() {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean needsOtaServiceProvisioning() {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public void notifyCallForwardingIndicator() {
    }

    void notifySuppServiceFailed(PhoneInternalInterface.SuppService suppService) {
    }

    @Override // com.android.internal.telephony.Phone
    protected void onUpdateIccAvailability() {
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForRingbackTone(Handler handler, int i, Object obj) {
    }

    public void registerForSuppServiceNotification(Handler handler, int i, Object obj) {
    }

    @Override // com.android.internal.telephony.Phone
    public void saveClirSetting(int i) {
    }

    @Override // com.android.internal.telephony.Phone
    public void selectNetworkManually(OperatorInfo operatorInfo, boolean z, Message message) {
    }

    public void sendUssdResponse(String str) {
    }

    public void setCallBarring(String str, boolean z, String str2, Message message, int i) {
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void setCallForwardingOption(int i, int i2, String str, int i3, int i4, Message message) {
    }

    public void setCallForwardingOption(int i, int i2, String str, int i3, Message message) {
    }

    public void setCellBroadcastSmsConfig(int[] iArr, Message message) {
    }

    public void setDataRoamingEnabled(boolean z) {
    }

    public boolean setLine1Number(String str, String str2, Message message) {
        return false;
    }

    public void setMute(boolean z) {
    }

    @Override // com.android.internal.telephony.Phone
    public void setNetworkSelectionModeAutomatic(Message message) {
    }

    @Override // com.android.internal.telephony.Phone
    public void setOnPostDialCharacter(Handler handler, int i, Object obj) {
    }

    public void setOutgoingCallerIdDisplay(int i, Message message) {
    }

    public void setRadioPower(boolean z) {
    }

    public void setVoiceMailNumber(String str, String str2, Message message) {
    }

    public Connection startConference(String[] strArr, PhoneInternalInterface.DialArgs dialArgs) {
        return null;
    }

    public void startNetworkScan(NetworkScanRequest networkScanRequest, Message message) {
    }

    @Override // com.android.internal.telephony.Phone
    public void startRingbackTone() {
    }

    public void stopNetworkScan(Message message) {
    }

    @Override // com.android.internal.telephony.Phone
    public void stopRingbackTone() {
    }

    public void switchHoldingAndActive() {
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForRingbackTone(Handler handler) {
    }

    public void unregisterForSuppServiceNotification(Handler handler) {
    }

    public void updateServiceLocation() {
    }

    public NtnPhoneBase(String str, Context context, CommandsInterface commandsInterface, PhoneNotifier phoneNotifier, FeatureFlags featureFlags) {
        super(str, phoneNotifier, context, commandsInterface, false, featureFlags);
        this.mState = PhoneConstants.State.IDLE;
        setPhoneName(str);
    }

    public ServiceState getServiceState() {
        ServiceState serviceState = new ServiceState();
        serviceState.setVoiceRegState(0);
        return serviceState;
    }

    @Override // com.android.internal.telephony.Phone
    public PhoneConstants.State getState() {
        return this.mState;
    }

    @Override // com.android.internal.telephony.Phone
    public SignalStrength getSignalStrength() {
        return new SignalStrength();
    }

    public List<? extends MmiCode> getPendingMmiCodes() {
        return new ArrayList(0);
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public PhoneConstants.DataState getDataConnectionState(String str) {
        return PhoneConstants.DataState.DISCONNECTED;
    }

    public boolean canDial() {
        int state = getServiceState().getState();
        Rlog.v("NtnPhoneBase", "canDial(): serviceState = " + state);
        if (state == 3) {
            return false;
        }
        boolean zBooleanValue = TelephonyProperties.disable_call().orElse(Boolean.FALSE).booleanValue();
        Rlog.v("NtnPhoneBase", "canDial(): disableCall = " + zBooleanValue);
        if (zBooleanValue) {
            return false;
        }
        Rlog.v("NtnPhoneBase", "canDial(): ringingCall: " + getRingingCall().getState());
        Rlog.v("NtnPhoneBase", "canDial(): foregndCall: " + getForegroundCall().getState());
        return (getRingingCall().isRinging() || getForegroundCall().getState().isAlive()) ? false : true;
    }

    public void setCallWaiting(boolean z, Message message) {
        Rlog.e("NtnPhoneBase", "call waiting not supported");
    }
}
