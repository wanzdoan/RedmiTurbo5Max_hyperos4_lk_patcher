package com.android.internal.telephony.imsphone;

import android.content.Context;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.CallQuality;
import android.telephony.NetworkScanRequest;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.MediaQualityStatus;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
abstract class ImsPhoneBase extends Phone {
    private RegistrantList mOnHoldRegistrants;
    private RegistrantList mRingbackRegistrants;
    private PhoneConstants.State mState;
    private RegistrantList mTtyModeReceivedRegistrants;

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

    public void getAvailableNetworks(Message message) {
    }

    public void getCallBarring(String str, String str2, Message message, int i) {
    }

    @Override // com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void getCallForwardingOption(int i, int i2, Message message) {
    }

    public void getCallForwardingOption(int i, Message message) {
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

    @Override // com.android.internal.telephony.Phone
    public boolean getMessageWaitingIndicator() {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public int getPhoneType() {
        return 5;
    }

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

    @Override // com.android.internal.telephony.Phone
    public boolean isDataAllowed() {
        return false;
    }

    public boolean isUserDataEnabled() {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean needsOtaServiceProvisioning() {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    protected void onUpdateIccAvailability() {
    }

    public void registerForSuppServiceNotification(Handler handler, int i, Object obj) {
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

    public void setDataRoamingEnabled(boolean z) {
    }

    public boolean setLine1Number(String str, String str2, Message message) {
        return false;
    }

    @Override // com.android.internal.telephony.Phone
    public void setNetworkSelectionModeAutomatic(Message message) {
    }

    public void setRadioPower(boolean z) {
    }

    public void startNetworkScan(NetworkScanRequest networkScanRequest, Message message) {
    }

    public void stopNetworkScan(Message message) {
    }

    public void unregisterForSuppServiceNotification(Handler handler) {
    }

    public void updateServiceLocation() {
    }

    public ImsPhoneBase(String str, Context context, PhoneNotifier phoneNotifier, boolean z, FeatureFlags featureFlags) {
        super(str, phoneNotifier, context, new ImsPhoneCommandInterface(context), z, featureFlags);
        this.mRingbackRegistrants = new RegistrantList();
        this.mOnHoldRegistrants = new RegistrantList();
        this.mTtyModeReceivedRegistrants = new RegistrantList();
        this.mState = PhoneConstants.State.IDLE;
    }

    @Override // com.android.internal.telephony.Phone
    public void migrateFrom(Phone phone) {
        super.migrateFrom(phone);
        migrate(this.mRingbackRegistrants, ((ImsPhoneBase) phone).mRingbackRegistrants);
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForRingbackTone(Handler handler, int i, Object obj) {
        this.mRingbackRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForRingbackTone(Handler handler) {
        this.mRingbackRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.Phone
    public void startRingbackTone() {
        this.mRingbackRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.TRUE, (Throwable) null));
    }

    @Override // com.android.internal.telephony.Phone
    public void stopRingbackTone() {
        this.mRingbackRegistrants.notifyRegistrants(new AsyncResult((Object) null, Boolean.FALSE, (Throwable) null));
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForOnHoldTone(Handler handler, int i, Object obj) {
        this.mOnHoldRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForOnHoldTone(Handler handler) {
        this.mOnHoldRegistrants.remove(handler);
    }

    public void startOnHoldTone(Connection connection) {
        this.mOnHoldRegistrants.notifyRegistrants(new AsyncResult((Object) null, new Pair(connection, Boolean.TRUE), (Throwable) null));
    }

    public void stopOnHoldTone(Connection connection) {
        this.mOnHoldRegistrants.notifyRegistrants(new AsyncResult((Object) null, new Pair(connection, Boolean.FALSE), (Throwable) null));
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForTtyModeReceived(Handler handler, int i, Object obj) {
        this.mTtyModeReceivedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForTtyModeReceived(Handler handler) {
        this.mTtyModeReceivedRegistrants.remove(handler);
    }

    public void onTtyModeReceived(int i) {
        this.mTtyModeReceivedRegistrants.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(i), (Throwable) null));
    }

    public void onCallQualityChanged(CallQuality callQuality, int i) {
        this.mNotifier.notifyCallQualityChanged(this, callQuality, i);
    }

    public void onMediaQualityStatusChanged(MediaQualityStatus mediaQualityStatus) {
        this.mNotifier.notifyMediaQualityStatusChanged(this, mediaQualityStatus);
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

    public void notifyPhoneStateChanged() {
        this.mNotifier.notifyPhoneState(this);
    }

    public void notifyPreciseCallStateChanged() {
        this.mPreciseCallStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, this, (Throwable) null));
        notifyPreciseCallStateToNotifier();
    }

    public void notifyPreciseCallStateToNotifier() {
        ImsPhoneCall imsPhoneCall = (ImsPhoneCall) getRingingCall();
        ImsPhoneCall imsPhoneCall2 = (ImsPhoneCall) getForegroundCall();
        ImsPhoneCall imsPhoneCall3 = (ImsPhoneCall) getBackgroundCall();
        if (imsPhoneCall == null || imsPhoneCall2 == null || imsPhoneCall3 == null) {
            return;
        }
        this.mNotifier.notifyPreciseCallState(this, new String[]{imsPhoneCall.getCallSessionId(), imsPhoneCall2.getCallSessionId(), imsPhoneCall3.getCallSessionId()}, new int[]{imsPhoneCall.getServiceType(), imsPhoneCall2.getServiceType(), imsPhoneCall3.getServiceType()}, new int[]{imsPhoneCall.getCallType(), imsPhoneCall2.getCallType(), imsPhoneCall3.getCallType()});
    }

    public void notifyDisconnect(Connection connection) {
        this.mDisconnectRegistrants.notifyResult(connection);
    }

    public void notifyImsReason(ImsReasonInfo imsReasonInfo) {
        this.mNotifier.notifyImsDisconnectCause(this, imsReasonInfo);
    }

    public void notifySuppServiceFailed(PhoneInternalInterface.SuppService suppService) {
        this.mSuppServiceFailedRegistrants.notifyResult(suppService);
    }

    @Override // com.android.internal.telephony.Phone
    public void notifyCallForwardingIndicator() {
        this.mNotifier.notifyCallForwardingChanged(this);
    }

    public boolean canDial() {
        int state = getServiceState().getState();
        Rlog.v("ImsPhoneBase", "canDial(): serviceState = " + state);
        if (state == 3) {
            return false;
        }
        boolean zBooleanValue = TelephonyProperties.disable_call().orElse(Boolean.FALSE).booleanValue();
        Rlog.v("ImsPhoneBase", "canDial(): disableCall = " + zBooleanValue);
        if (zBooleanValue) {
            return false;
        }
        Rlog.v("ImsPhoneBase", "canDial(): ringingCall: " + getRingingCall().getState());
        Rlog.v("ImsPhoneBase", "canDial(): foregndCall: " + getForegroundCall().getState());
        Rlog.v("ImsPhoneBase", "canDial(): backgndCall: " + getBackgroundCall().getState());
        return (getRingingCall().isRinging() || (getForegroundCall().getState().isAlive() && getBackgroundCall().getState().isAlive())) ? false : true;
    }

    public void setVoiceMailNumber(String str, String str2, Message message) {
        AsyncResult.forMessage(message, (Object) null, (Throwable) null);
        message.sendToTarget();
    }

    public void getOutgoingCallerIdDisplay(Message message) {
        AsyncResult.forMessage(message, (Object) null, (Throwable) null);
        message.sendToTarget();
    }

    public void setOutgoingCallerIdDisplay(int i, Message message) {
        AsyncResult.forMessage(message, (Object) null, (Throwable) null);
        message.sendToTarget();
    }

    public void getCallWaiting(Message message) {
        AsyncResult.forMessage(message, (Object) null, (Throwable) null);
        message.sendToTarget();
    }

    public void setCallWaiting(boolean z, Message message) {
        Rlog.e("ImsPhoneBase", "call waiting not supported");
    }

    public void activateCellBroadcastSms(int i, Message message) {
        Rlog.e("ImsPhoneBase", "Error! This functionality is not implemented for Volte.");
    }

    public void getCellBroadcastSmsConfig(Message message) {
        Rlog.e("ImsPhoneBase", "Error! This functionality is not implemented for Volte.");
    }

    public void setCellBroadcastSmsConfig(int[] iArr, Message message) {
        Rlog.e("ImsPhoneBase", "Error! This functionality is not implemented for Volte.");
    }

    @Override // com.android.internal.telephony.Phone
    public int getTerminalBasedCallWaitingState(boolean z) {
        return getDefaultPhone().getTerminalBasedCallWaitingState(z);
    }

    @Override // com.android.internal.telephony.Phone
    public void setTerminalBasedCallWaitingSupported(boolean z) {
        getDefaultPhone().setTerminalBasedCallWaitingSupported(z);
    }
}
