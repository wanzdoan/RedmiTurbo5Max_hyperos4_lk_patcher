package com.android.internal.telephony.ntnphone;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telephony.NetworkScanRequest;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.NtnRIL;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.metrics.VoiceCallSessionStats;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.telephony.Rlog;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import vendor.xiaomi.hardware.satellite.JS_TXPWR;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhone extends NtnPhoneBase {
    protected static final int EVENT_ANT_INFO_CHANGED = 1023;
    protected static final int EVENT_BASE = 1000;
    protected static final int EVENT_GET_ANTENNA_STATE_DONE = 1022;
    protected static final int EVENT_GET_BASEBAND_VERSION_DONE = 1007;
    protected static final int EVENT_GET_CIT_RESULT_DONE = 1012;
    protected static final int EVENT_GET_SAR_MODE = 1008;
    protected static final int EVENT_GET_SPEECH_BITRATE = 1017;
    protected static final int EVENT_GPS_MIN_RANGE_REQUEST = 1014;
    protected static final int EVENT_GPS_REQUEST = 1011;
    protected static final int EVENT_HLS_ERROR_REPORT = 1019;
    protected static final int EVENT_IMEI_REQUEST_DONE = 1006;
    protected static final int EVENT_IMSI_QUERY_REQUEST = 1002;
    protected static final int EVENT_NTN_POS_INFO_REPORT = 1021;
    protected static final int EVENT_OPERATION_COMPLETE = 1001;
    protected static final int EVENT_OTA_STATE_CHANGED = 1024;
    protected static final int EVENT_SET_SAT_BEAM_REQUEST_DONE = 1016;
    protected static final int EVENT_SHUT_DOWN_CHECK = 1013;
    protected static final int EVENT_SHUT_DOWN_DONE = 1020;
    protected static final int EVENT_SIM_AUTH_REQUEST = 1003;
    protected static final int EVENT_SIM_AUTH_RESPONSE_TO_NTN = 1005;
    protected static final int EVENT_SIM_AUTH_TO_MODEM = 1004;
    protected static final int EVENT_SNR_REPORT = 1010;
    protected static final int EVENT_TTLOG_INFO_REPORT = 1018;
    protected static final int EVENT_TXPWR_REPORT = 1009;
    protected static final int EVENT_UNSOL_NTN_CDC_MESSAGE = 1025;
    protected static final int EVENT_VOICE_CONTROL_REPORT = 1015;
    public static final String EXTRA_ACTION_SEND_CPST_INFO = "com.xiaomi.satellite.extra.EXTRA_ACTION_SEND_CPST_INFO";
    public static final String EXTRA_ACTION_SEND_TRACE_INFO = "com.xiaomi.satellite.extra.EXTRA_ACTION_SEND_TRACE_INFO";
    protected static final int SHUT_DOWN_CHECK_DELAY_TIME = 1500;
    protected static final int WAIT_CIT_TIME_MS = 2000;
    private boolean isReceiveGpsQuery;
    private final List listeners;
    private final List mAntInfolisteners;
    private final List mBeamSwitchCallbackList;
    private final Object mCITLock;
    public NtnPhoneCallTracker mCT;
    private final List mCellularCallbackList;
    private String mCitResult;
    private int mGPSMinRange;
    public GsmCdmaPhone mGsmCdmaPhone;
    private HlsErrorInfo mHlsErrorInfo;
    private final List mHlsErrorListeners;
    protected String mImei;
    private int mLPMState;
    protected Message mLastRelevantPoll;
    protected boolean mNeedsPoll;
    private String mNtnGps;
    private NtnInboundSmsHandler mNtnInboundSmsHandler;
    private NtnPosInfo mNtnPosInfo;
    public NtnRIL mNtnRil;
    private NtnTtlogInfo mNtnTtlogInfo;
    protected int mPendingOperations;
    public PowerManagerTracker mPowerManagerTracker;
    private int mRetSnr;
    public NtnServiceStateTracker mSST;
    public SatelliteConfigManager mSatelliteConfigManager;
    private SMSDispatcher mSmsDispatcher;
    private String mSpeechBitrate;
    private int mTelecomVoiceServiceStateOverride;
    private final ConcurrentHashMap mUnsolNtnCdcMsgListenersMap;
    private int mVoiceRate;
    private int oldRealTxPwr;
    private int oldTarTxPwr;

    public interface AntInfoChangedListener {
        void onAntInfoSignalStrengthChanged(ArrayList<AntIdentitySignalStrength> arrayList);
    }

    public interface BeamswitchingCallback {
        void startBeamswitching(int i, int i2);
    }

    public interface CellularNetworkScanCallback {
        void startCellularNetworkScan(boolean z);
    }

    public interface HlsErrorListener {
        void onHlsError(int i, int i2);
    }

    public interface UnsolNtnCdcMsgListener {
        void unsolNtnCdcMsg(byte[] bArr);
    }

    public interface onNtnPhyParmChangeListener {
        void onSnrChange(int i);

        void onTxPwrChange(JS_TXPWR js_txpwr);
    }

    public int getCsCallRadioTech() {
        return 16;
    }

    public boolean isPhoneTypeGsm() {
        return true;
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void activateCellBroadcastSms(int i, Message message) {
        super.activateCellBroadcastSms(i, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean canConference() {
        return super.canConference();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase
    public /* bridge */ /* synthetic */ boolean canDial() {
        return super.canDial();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean canTransfer() {
        return super.canTransfer();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void conference() {
        super.conference();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase
    public /* bridge */ /* synthetic */ boolean disableDataConnectivity() {
        return super.disableDataConnectivity();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void disableLocationUpdates() {
        super.disableLocationUpdates();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase
    public /* bridge */ /* synthetic */ boolean enableDataConnectivity() {
        return super.enableDataConnectivity();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void enableLocationUpdates() {
        super.enableLocationUpdates();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void explicitCallTransfer() {
        super.explicitCallTransfer();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getAvailableNetworks(Message message) {
        super.getAvailableNetworks(message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getCallBarring(String str, String str2, Message message, int i) {
        super.getCallBarring(str, str2, message, i);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean getCallForwardingIndicator() {
        return super.getCallForwardingIndicator();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getCallForwardingOption(int i, int i2, Message message) {
        super.getCallForwardingOption(i, i2, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getCallForwardingOption(int i, Message message) {
        super.getCallForwardingOption(i, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getCallWaiting(Message message) {
        super.getCallWaiting(message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getCellBroadcastSmsConfig(Message message) {
        super.getCellBroadcastSmsConfig(message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ int getDataActivityState() {
        return super.getDataActivityState();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ PhoneConstants.DataState getDataConnectionState(String str) {
        return super.getDataConnectionState(str);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean getDataRoamingEnabled() {
        return super.getDataRoamingEnabled();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getDeviceId() {
        return super.getDeviceId();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getDeviceSvn() {
        return super.getDeviceSvn();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getGroupIdLevel1() {
        return super.getGroupIdLevel1();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getGroupIdLevel2() {
        return super.getGroupIdLevel2();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ IccCard getIccCard() {
        return super.getIccCard();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ IccFileHandler getIccFileHandler() {
        return super.getIccFileHandler();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager() {
        return super.getIccPhoneBookInterfaceManager();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean getIccRecordsLoaded() {
        return super.getIccRecordsLoaded();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ String getIccSerialNumber() {
        return super.getIccSerialNumber();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ int getImeiType() {
        return super.getImeiType();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getLine1AlphaTag() {
        return super.getLine1AlphaTag();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getLine1Number() {
        return super.getLine1Number();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean getMessageWaitingIndicator() {
        return super.getMessageWaitingIndicator();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean getMute() {
        return super.getMute();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getOutgoingCallerIdDisplay(Message message) {
        super.getOutgoingCallerIdDisplay(message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ List getPendingMmiCodes() {
        return super.getPendingMmiCodes();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ int getPhoneType() {
        return super.getPhoneType();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getVoiceMailAlphaTag() {
        return super.getVoiceMailAlphaTag();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getVoiceMailNumber() {
        return super.getVoiceMailNumber();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean handleInCallMmiCommands(String str) {
        return super.handleInCallMmiCommands(str);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean handlePinMmi(String str) {
        return super.handlePinMmi(str);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean handleUssdRequest(String str, ResultReceiver resultReceiver) {
        return super.handleUssdRequest(str, resultReceiver);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean isDataAllowed() {
        return super.isDataAllowed();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean isUserDataEnabled() {
        return super.isUserDataEnabled();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean isVideoEnabled() {
        return super.isVideoEnabled();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean needsOtaServiceProvisioning() {
        return super.needsOtaServiceProvisioning();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void notifyCallForwardingIndicator() {
        super.notifyCallForwardingIndicator();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void registerForRingbackTone(Handler handler, int i, Object obj) {
        super.registerForRingbackTone(handler, i, obj);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void registerForSuppServiceNotification(Handler handler, int i, Object obj) {
        super.registerForSuppServiceNotification(handler, i, obj);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void saveClirSetting(int i) {
        super.saveClirSetting(i);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void selectNetworkManually(OperatorInfo operatorInfo, boolean z, Message message) {
        super.selectNetworkManually(operatorInfo, z, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void sendUssdResponse(String str) {
        super.sendUssdResponse(str);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setCallBarring(String str, boolean z, String str2, Message message, int i) {
        super.setCallBarring(str, z, str2, message, i);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setCallForwardingOption(int i, int i2, String str, int i3, int i4, Message message) {
        super.setCallForwardingOption(i, i2, str, i3, i4, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setCallForwardingOption(int i, int i2, String str, int i3, Message message) {
        super.setCallForwardingOption(i, i2, str, i3, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setCallWaiting(boolean z, Message message) {
        super.setCallWaiting(z, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setCellBroadcastSmsConfig(int[] iArr, Message message) {
        super.setCellBroadcastSmsConfig(iArr, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setDataRoamingEnabled(boolean z) {
        super.setDataRoamingEnabled(z);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean setLine1Number(String str, String str2, Message message) {
        return super.setLine1Number(str, str2, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setMute(boolean z) {
        super.setMute(z);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void setNetworkSelectionModeAutomatic(Message message) {
        super.setNetworkSelectionModeAutomatic(message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void setOnPostDialCharacter(Handler handler, int i, Object obj) {
        super.setOnPostDialCharacter(handler, i, obj);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setOutgoingCallerIdDisplay(int i, Message message) {
        super.setOutgoingCallerIdDisplay(i, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setRadioPower(boolean z) {
        super.setRadioPower(z);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setVoiceMailNumber(String str, String str2, Message message) {
        super.setVoiceMailNumber(str, str2, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ Connection startConference(String[] strArr, PhoneInternalInterface.DialArgs dialArgs) {
        return super.startConference(strArr, dialArgs);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void startNetworkScan(NetworkScanRequest networkScanRequest, Message message) {
        super.startNetworkScan(networkScanRequest, message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void startRingbackTone() {
        super.startRingbackTone();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void stopNetworkScan(Message message) {
        super.stopNetworkScan(message);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void stopRingbackTone() {
        super.stopRingbackTone();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void switchHoldingAndActive() {
        super.switchHoldingAndActive();
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void unregisterForRingbackTone(Handler handler) {
        super.unregisterForRingbackTone(handler);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void unregisterForSuppServiceNotification(Handler handler) {
        super.unregisterForSuppServiceNotification(handler);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void updateServiceLocation() {
        super.updateServiceLocation();
    }

    public NtnPhone(Context context, NtnRIL ntnRIL, PhoneNotifier phoneNotifier, int i, FeatureFlags featureFlags) {
        super("NTN", context, ntnRIL, phoneNotifier, featureFlags);
        this.mTelecomVoiceServiceStateOverride = 1;
        this.oldTarTxPwr = -1;
        this.oldRealTxPwr = -1;
        this.mRetSnr = -1;
        this.mVoiceRate = -1;
        this.mGPSMinRange = -1;
        this.isReceiveGpsQuery = false;
        this.listeners = new CopyOnWriteArrayList();
        this.mHlsErrorListeners = new CopyOnWriteArrayList();
        this.mCellularCallbackList = new CopyOnWriteArrayList();
        this.mAntInfolisteners = new CopyOnWriteArrayList();
        this.mBeamSwitchCallbackList = new CopyOnWriteArrayList();
        this.mUnsolNtnCdcMsgListenersMap = new ConcurrentHashMap();
        this.mLPMState = -1;
        this.mCITLock = new Object();
        this.mCi = ntnRIL;
        this.mNtnRil = ntnRIL;
        this.mPhoneId = i;
        this.mSatelliteConfigManager = new SatelliteConfigManager(this);
        initSatelliteCap();
        this.mGsmCdmaPhone = (GsmCdmaPhone) PhoneFactory.getPhone(i);
        this.mSST = new NtnServiceStateTracker(this, ntnRIL);
        NtnInboundSmsHandler ntnInboundSmsHandlerMakeInboundSmsHandler = NtnInboundSmsHandler.makeInboundSmsHandler(getContext(), this.mSmsStorageMonitor, this, getLooper(), featureFlags);
        this.mNtnInboundSmsHandler = ntnInboundSmsHandlerMakeInboundSmsHandler;
        ntnInboundSmsHandlerMakeInboundSmsHandler.sendMessage(6);
        this.mCT = new NtnPhoneCallTracker(this, featureFlags);
        this.mVoiceCallSessionStats = new NtnVoiceCallSessionStats(this.mPhoneId, this, featureFlags);
        this.mPowerManagerTracker = new PowerManagerTracker(this);
        CallManager.getInstance(context).registerPhone(this);
        this.mNtnRil.registerForImsiQuery(this, 1002, null);
        this.mNtnRil.registerForSimAuto(this, 1003, null);
        this.mNtnRil.registerTxPwr(this, 1009, null);
        this.mNtnRil.registerSnr(this, 1010, null);
        this.mNtnRil.registerGpsQuery(this, 1011, null);
        this.mNtnRil.registerGpsMinRangeQuery(this, 1014, null);
        this.mNtnRil.registerVoiceControl(this, 1015, null);
        this.mNtnRil.registerTtlogInfoReport(this, 1018, null);
        this.mNtnRil.registerHlserrorReport(this, 1019, null);
        this.mNtnRil.registerNtnPosInfoReport(this, 1021, null);
        this.mNtnRil.setOnAntInfoSignalStrengthUpdate(this, 1023, null);
        this.mNtnRil.registerForOtaStateChanged(this, 1024, null);
        this.mNtnRil.registerForUnsolNtnCdcMsg(this, 1025, null);
        Rlog.d("NtnPhone", "NtnPhone created");
    }

    @Override // com.android.internal.telephony.Phone, android.os.Handler
    public void handleMessage(Message message) {
        Object obj;
        Rlog.d("NtnPhone", "handleMessage: " + message.what);
        switch (message.what) {
            case 1002:
                Rlog.d("NtnPhone", "Event EVENT_IMSI_QUERY_REQUEST Received");
                if (((AsyncResult) message.obj).exception == null) {
                    setImsiForModem();
                    return;
                }
                return;
            case 1003:
                Rlog.d("NtnPhone", "Event EVENT_SIM_AUTH_REQUEST Received");
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception == null) {
                    log("EVENT_SIM_AUTH_REQUEST ar.result =" + asyncResult.result);
                    sendMessage(obtainMessage(1004, asyncResult.result));
                    return;
                }
                return;
            case 1004:
                Rlog.d("NtnPhone", "Event EVENT_SIM_AUTH_TO_MODEM Received");
                if (message.obj != null) {
                    log("EVENT_SIM_AUTH_TO_MODEM msg.obj =" + message.obj);
                    requestSimAuth((String) message.obj);
                    return;
                }
                return;
            case 1005:
                Rlog.d("NtnPhone", "Event EVENT_SIM_AUTH_RESPONSE_TO_NTN Received");
                if (message.obj != null) {
                    log("EVENT_SIM_AUTH_RESPONSE_TO_NTN msg.obj =" + message.obj);
                    this.mNtnRil.supplyIccAuthForApp((String) message.obj, null);
                    return;
                }
                return;
            case 1006:
                Rlog.d("NtnPhone", "Event EVENT_IMEI_REQUEST_DONE Received");
                handleSimAuthDone();
                return;
            case 1007:
                Rlog.d("NtnPhone", "Event EVENT_GET_BASEBAND_VERSION_DONE Received");
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                if (asyncResult2.exception != null) {
                    return;
                }
                log("Baseband version: " + asyncResult2.result);
                String str = (String) asyncResult2.result;
                if (str != null) {
                    SystemProperties.set("persist.radio.ntn_version", str);
                    return;
                }
                return;
            case 1008:
                Rlog.d("NtnPhone", "Event EVENT_GET_SAR_MODE Received");
                AsyncResult asyncResult3 = (AsyncResult) message.obj;
                if (asyncResult3.exception != null) {
                    return;
                }
                log("SAR Mode: " + asyncResult3.result);
                return;
            case 1009:
                AsyncResult asyncResult4 = (AsyncResult) message.obj;
                if (asyncResult4.exception == null) {
                    JS_TXPWR js_txpwr = (JS_TXPWR) asyncResult4.result;
                    if (this.oldTarTxPwr == js_txpwr.tarTxPwr && this.oldRealTxPwr == js_txpwr.realTxPwr) {
                        return;
                    }
                    notifyTxpwrChange(js_txpwr);
                    this.oldTarTxPwr = js_txpwr.tarTxPwr;
                    this.oldRealTxPwr = js_txpwr.realTxPwr;
                    return;
                }
                return;
            case 1010:
                AsyncResult asyncResult5 = (AsyncResult) message.obj;
                if (asyncResult5.exception == null) {
                    int iIntValue = ((Integer) asyncResult5.result).intValue();
                    this.mRetSnr = iIntValue;
                    notifySnrChange(iIntValue);
                    return;
                }
                return;
            case 1011:
                Rlog.d("NtnPhone", "EVENT_GPS_REQUEST Received");
                this.isReceiveGpsQuery = true;
                if (TextUtils.isEmpty(this.mNtnGps)) {
                    return;
                }
                this.mNtnRil.supplyGpsForApp(this.mNtnGps, null);
                return;
            case 1012:
                Rlog.d("NtnPhone", "EVENT_GET_CIT_RESULT_DONE Received");
                AsyncResult asyncResult6 = (AsyncResult) message.obj;
                synchronized (this.mCITLock) {
                    try {
                        if (asyncResult6.exception == null) {
                            this.mCitResult = (String) asyncResult6.result;
                        }
                        this.mCITLock.notify();
                    } catch (Throwable th) {
                        throw th;
                    }
                    break;
                }
                return;
            case 1013:
                break;
            case 1014:
                Rlog.d("NtnPhone", "EVENT_GPS_MIN_RANGE_REQUEST Received");
                AsyncResult asyncResult7 = (AsyncResult) message.obj;
                this.isReceiveGpsQuery = true;
                this.mNtnRil.supplyGpsForApp("11.111111,22.222222,33333", null);
                if (asyncResult7.exception == null) {
                    this.mGPSMinRange = ((Integer) asyncResult7.result).intValue();
                    return;
                }
                return;
            case 1015:
                Rlog.d("NtnPhone", "Event EVENT_VOICE_CONTROL_REPORT Received");
                AsyncResult asyncResult8 = (AsyncResult) message.obj;
                if (asyncResult8.exception == null) {
                    log("EVENT_VOICE_CONTROL_REPORT ar.result =" + asyncResult8.result);
                    this.mVoiceRate = ((Integer) asyncResult8.result).intValue();
                    return;
                }
                return;
            case 1016:
                Rlog.d("NtnPhone", "EVENT_SET_SAT_BEAM_REQUEST_DONE Received");
                setRadioPower(true, false, false, true);
                return;
            case 1017:
                Rlog.d("NtnPhone", "EVENT_GET_SPEECH_BITRATE Received");
                AsyncResult asyncResult9 = (AsyncResult) message.obj;
                if (asyncResult9 == null || asyncResult9.exception != null) {
                    return;
                }
                this.mSpeechBitrate = (String) asyncResult9.result;
                return;
            case 1018:
                Rlog.d("NtnPhone", "EVENT_TTLOG_INFO_REPORT Received");
                AsyncResult asyncResult10 = (AsyncResult) message.obj;
                Rlog.d("NtnPhone", "EVENT_TTLOG_INFO_REPORT ar = " + asyncResult10);
                if (asyncResult10 != null) {
                    Rlog.d("NtnPhone", "EVENT_TTLOG_INFO_REPORT ar.exception  = " + asyncResult10.exception);
                }
                if (asyncResult10 == null || asyncResult10.exception != null) {
                    return;
                }
                this.mNtnTtlogInfo = (NtnTtlogInfo) asyncResult10.result;
                return;
            case 1019:
                Rlog.d("NtnPhone", "EVENT_HLS_ERROR_REPORT Received");
                AsyncResult asyncResult11 = (AsyncResult) message.obj;
                if (asyncResult11 == null || asyncResult11.exception != null) {
                    return;
                }
                HlsErrorInfo hlsErrorInfo = (HlsErrorInfo) asyncResult11.result;
                this.mHlsErrorInfo = hlsErrorInfo;
                checkHlsError(hlsErrorInfo);
                return;
            case 1020:
                AsyncResult asyncResult12 = (AsyncResult) message.obj;
                if (asyncResult12 == null || asyncResult12.exception != null) {
                    Rlog.d("NtnPhone", "EVENT_SHUT_DOWN_DONE exception.");
                    return;
                }
                removeMessages(1013);
                break;
            case 1021:
                Rlog.d("NtnPhone", "EVENT_NTN_POS_INFO_REPORT Received");
                AsyncResult asyncResult13 = (AsyncResult) message.obj;
                if (asyncResult13 == null || asyncResult13.exception != null) {
                    return;
                }
                this.mNtnPosInfo = (NtnPosInfo) asyncResult13.result;
                return;
            case 1022:
                Rlog.d("NtnPhone", "EVENT_GET_ANTENNA_STATE_DONE Received");
                AsyncResult asyncResult14 = (AsyncResult) message.obj;
                if (asyncResult14.exception == null && (obj = asyncResult14.result) != null) {
                    int[] iArr = (int[]) obj;
                    if (iArr.length == 2) {
                        int i = iArr[0];
                        int i2 = iArr[1];
                        NtnAntConstants ntnAntConstants = new NtnAntConstants();
                        asyncResult14.result = new Pair(ntnAntConstants.TxAntEnableTypeFromRILInt(i), ntnAntConstants.RxAntEnableTypeFromRILInt(i2));
                    }
                } else {
                    if (asyncResult14.result == null) {
                        loge("getAntennaState: Empty response");
                    }
                    if (asyncResult14.exception != null) {
                        loge("getAntennaState: Exception: " + asyncResult14.exception);
                    }
                }
                Message message2 = (Message) asyncResult14.userObj;
                if (message2 != null) {
                    AsyncResult.forMessage(message2, asyncResult14.result, asyncResult14.exception);
                    message2.sendToTarget();
                    return;
                }
                return;
            case 1023:
                Rlog.d("NtnPhone", "EVENT_ANT_INFO_CHANGED Received");
                AsyncResult asyncResult15 = (AsyncResult) message.obj;
                if (asyncResult15.exception != null || asyncResult15.result == null) {
                    return;
                }
                log("EVENT_ANT_INFO_CHANGED notifyAntInfoChanged");
                notifyAntInfoChanged((ArrayList) asyncResult15.result);
                return;
            case 1024:
                log("EVENT_OTA_STATE_CHANGED Received");
                AsyncResult asyncResult16 = (AsyncResult) message.obj;
                if (asyncResult16 == null || asyncResult16.exception != null) {
                    return;
                }
                log("EVENT_OTA_STATE_CHANGED otaState = " + ((Integer) asyncResult16.result).intValue());
                return;
            case 1025:
                log("EVENT_UNSOL_NTN_CDC_MESSAGE Received");
                AsyncResult asyncResult17 = (AsyncResult) message.obj;
                if (asyncResult17 == null || asyncResult17.exception != null) {
                    return;
                }
                Bundle bundle = (Bundle) asyncResult17.result;
                int i3 = bundle.getInt(NtnRILUtils.NTN_CDC_MESSAGE_ID, -1);
                byte[] byteArray = bundle.getByteArray(NtnRILUtils.NTN_CDC_MESSAGE_DATA);
                log("msgId = " + i3 + ", data = " + Arrays.toString(byteArray));
                notifyUnsolNtnCdcMsg(i3, byteArray);
                return;
            default:
                log("ntnPhone: Event msg.what=" + message.what);
                return;
        }
        try {
            if (NtnPhoneFactory.getDefaultPhone() != null) {
                this.mNtnRil.shutDownNtnProxyAndRequestList();
                TelephonyCommonUtils.resetNtnStackPropertyValue();
                NtnPhoneFactory.BackToCellular();
                Rlog.d("NtnPhone", "shut down done, destroy default phone.");
            }
        } catch (Exception e) {
            loge("getDefaultNtnPhone: " + e);
        }
    }

    private void initSatelliteCap() {
        new Thread(new Runnable() { // from class: com.android.internal.telephony.ntnphone.NtnPhone.1
            @Override // java.lang.Runnable
            public void run() {
                NtnPhone.this.log("initSatelliteCap start");
                if (!NtnPhone.this.initNtnRadio()) {
                    Rlog.d("NtnPhone", "product not support satellite!");
                } else {
                    NtnPhone.this.mSatelliteConfigManager.initSatelliteCap();
                }
            }
        }).start();
    }

    public boolean initNtnRadio() {
        return this.mNtnRil.initNtnRadio();
    }

    public boolean destroyNtnRadio() {
        return this.mNtnRil.destroyNtnRadio();
    }

    public Map getAllSatelliteConfig() {
        return this.mSatelliteConfigManager.getAllSatelliteConfig();
    }

    public SatelliteConfig getSatelliteConfig(SatelliteConfig.Type type) {
        return this.mSatelliteConfigManager.getSatelliteConfig(type);
    }

    public boolean isHardSimSwapForTT() {
        SatelliteConfig satelliteConfig = getSatelliteConfig(SatelliteConfig.Type.TIANTONG_1);
        Rlog.d("NtnPhone", "isHardSimSwap() SatelliteConfig: " + satelliteConfig);
        if (satelliteConfig != null) {
            return satelliteConfig.getSimSwitchType() == SatelliteConfig.SimSwitchType.HARD_SWITCH;
        }
        Rlog.e("NtnPhone", "SatelliteConfig is null");
        return false;
    }

    public SatelliteConfigManager getSatelliteConfigManager() {
        return this.mSatelliteConfigManager;
    }

    public boolean isSatelliteAndCellularDualStandbySupported() {
        SatelliteConfig satelliteConfig = getSatelliteConfig(SatelliteConfig.Type.TIANTONG_1);
        Rlog.d("NtnPhone", "isSatelliteAndCellularDualStandbySupported() SatelliteConfig: " + satelliteConfig);
        if (satelliteConfig == null) {
            Rlog.e("NtnPhone", "SatelliteConfig is null");
            return false;
        }
        return satelliteConfig.isSeparateBBICPower();
    }

    public int getChipType() {
        SatelliteConfig satelliteConfig = getSatelliteConfig(SatelliteConfig.Type.TIANTONG_1);
        Rlog.d("NtnPhone", "getSatelliteVerdorChip() SatelliteConfig: " + satelliteConfig);
        if (satelliteConfig == null) {
            Rlog.e("NtnPhone", "SatelliteConfig is null");
            return 0;
        }
        return satelliteConfig.getChipType();
    }

    public void checkHlsError(HlsErrorInfo hlsErrorInfo) {
        final int i = hlsErrorInfo.class_info;
        final int i2 = hlsErrorInfo.error_cause;
        Rlog.d("NtnPhone", "checkHlsError Received = " + i + ", " + i2);
        this.mHlsErrorListeners.forEach(new Consumer() { // from class: com.android.internal.telephony.ntnphone.NtnPhone$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((NtnPhone.HlsErrorListener) obj).onHlsError(i, i2);
            }
        });
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public Connection dial(String str, PhoneInternalInterface.DialArgs dialArgs, Consumer<Phone> consumer) throws CallStateException {
        consumer.accept(this);
        return dialInternal(str, dialArgs);
    }

    @Override // com.android.internal.telephony.Phone
    protected Connection dialInternal(String str, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        return this.mCT.dial(str, dialArgs);
    }

    public void notifyPhoneStateChanged() {
        this.mNotifier.notifyPhoneState(this);
    }

    public void notifyPreciseCallStateChanged() {
        this.mPreciseCallStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, this, (Throwable) null));
        this.mNotifier.notifyPreciseCallState(this, null, null, null);
    }

    public void notifyNewRingingConnection(Connection connection) {
        super.notifyNewRingingConnectionP(connection);
    }

    public void notifyDisconnect(Connection connection) {
        this.mDisconnectRegistrants.notifyResult(connection);
        this.mNotifier.notifyDisconnectCause(this, connection.getDisconnectCause(), connection.getPreciseDisconnectCause());
    }

    public void notifyUnknownConnection(Connection connection) {
        super.notifyUnknownConnectionP(connection);
    }

    public void hangup(NtnPhoneConnection ntnPhoneConnection) throws CallStateException {
        this.mCi.hangupConnection(-1, obtainCompleteMessage());
        notifyPreciseCallStateChanged();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void clearDisconnected() {
        this.mCT.clearDisconnected();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void acceptCall(int i) throws CallStateException {
        this.mCT.acceptCall();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void rejectCall() throws CallStateException {
        this.mCi.rejectCall(obtainCompleteMessage());
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void sendDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c)) {
            loge("sendDtmf called with invalid character '" + c + "'");
            return;
        }
        if (this.mCT.mState == PhoneConstants.State.OFFHOOK) {
            this.mCi.sendDtmf(c, null);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void startDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c)) {
            loge("startDtmf called with invalid character '" + c + "'");
            return;
        }
        this.mCi.startDtmf(c, null);
    }

    public void setSatBeam(int i, int[] iArr) {
        this.mNtnRil.setSatBeam(i, iArr, obtainMessage(1016));
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void stopDtmf() {
        this.mCi.stopDtmf(null);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public Call getForegroundCall() {
        return this.mCT.mForegroundCall;
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public Call getBackgroundCall() {
        return this.mCT.mBackgroundCall;
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public Call getRingingCall() {
        if (!this.mCT.mRingingCall.isRinging() && this.mCT.getRingingHandoverConnection() != null && this.mCT.getRingingHandoverConnection().getCall() != null && this.mCT.getRingingHandoverConnection().getCall().isRinging()) {
            return this.mCT.getRingingHandoverConnection().getCall();
        }
        return this.mCT.mRingingCall;
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public String getSubscriberId() {
        return this.mGsmCdmaPhone.getSubscriberId();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setRadioPower(boolean z, boolean z2, boolean z3, boolean z4) {
        this.mCi.setRadioPower(z, z2, z3, null);
    }

    @Override // com.android.internal.telephony.Phone
    public void shutdownRadio() {
        this.mCi.requestShutdown(obtainMessage(1020));
        sendMessageDelayed(obtainMessage(1013), 1500L);
    }

    public void setPowerSwitch(boolean z) {
        if (!isSatelliteAndCellularDualStandbySupported()) {
            this.mNtnRil.setPowerSwitch(z, null);
        } else {
            this.mPowerManagerTracker.setPowerSwitch(z, 0);
        }
    }

    public void setPowerSwitch(boolean z, int i) {
        Rlog.d("NtnPhone", "setPowerSwitch isSatelliteAndCellularDualStandbySupported = " + isSatelliteAndCellularDualStandbySupported() + ", delayTimerPowerOff:" + i + "; on = " + z);
        if (isSatelliteAndCellularDualStandbySupported()) {
            this.mPowerManagerTracker.setPowerSwitch(z, i);
        } else if (z) {
            setPowerSwitch(true);
        } else {
            shutdownRadio();
        }
    }

    public boolean isSatellitePowerOn() {
        return this.mPowerManagerTracker.isSatellitePowerOn();
    }

    public boolean isSatelliteDelayPowerOff() {
        return this.mPowerManagerTracker.isSatelliteDelayPowerOff();
    }

    @Override // com.android.internal.telephony.Phone
    public IccSmsInterfaceManager getIccSmsInterfaceManager() {
        return this.mGsmCdmaPhone.getIccSmsInterfaceManager();
    }

    public SMSDispatcher getSmsDispatcher() {
        return this.mSmsDispatcher;
    }

    public void getSarMode() {
        this.mNtnRil.getSARMode(obtainMessage(1008));
        Rlog.d("NtnPhone", "getSarMode");
    }

    public void setSarMode(int i) {
        this.mNtnRil.setSARMode(i, null);
        Rlog.d("NtnPhone", "setSarMode: " + i);
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public String getImei() {
        String str = SystemProperties.get("ro.vendor.ril.imeitt");
        this.mImei = str;
        return str;
    }

    public InboundSmsHandler getNtnInboundSmsHandler() {
        return this.mNtnInboundSmsHandler;
    }

    public GsmCdmaPhone getGsmCdmaPhone() {
        return this.mGsmCdmaPhone;
    }

    private void setImsiForModem() {
        String subscriberId = this.mGsmCdmaPhone.getSubscriberId();
        if (TextUtils.isEmpty(subscriberId)) {
            return;
        }
        this.mNtnRil.supplyImsiForApp(subscriberId, obtainMessage(1006));
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public ServiceState getServiceState() {
        NtnServiceStateTracker ntnServiceStateTracker = this.mSST;
        if (ntnServiceStateTracker != null) {
            return ntnServiceStateTracker.mSS;
        }
        return new ServiceState();
    }

    @Override // com.android.internal.telephony.Phone
    public void notifySignalStrength() {
        this.mSST.notifySignalStrength();
    }

    @Override // com.android.internal.telephony.Phone
    public void setVoiceServiceStateOverride(boolean z) {
        this.mTelecomVoiceServiceStateOverride = !z ? 1 : 0;
    }

    public void notifyServiceStateChanged(ServiceState serviceState) {
        super.notifyServiceStateChangedP(serviceState);
    }

    private void requestSimAuth(final String str) {
        new Thread(new Runnable() { // from class: com.android.internal.telephony.ntnphone.NtnPhone.2
            @Override // java.lang.Runnable
            public void run() {
                NtnPhone.this.log("requestSimAuth data =" + str);
                boolean zIsEmpty = TextUtils.isEmpty(str);
                NtnPhone ntnPhone = NtnPhone.this;
                if (zIsEmpty) {
                    ntnPhone.loge("requestSimAuth, data is empty, data =" + str);
                    return;
                }
                IccRecords iccRecords = ntnPhone.mGsmCdmaPhone.getIccRecords();
                NtnPhone ntnPhone2 = NtnPhone.this;
                if (iccRecords == null) {
                    ntnPhone2.loge("requestSimAuth, IccRecords is null.");
                    return;
                }
                String strBase64Encode = ntnPhone2.base64Encode(str);
                NtnPhone.this.log("requestSimAuth after convert encodeData =" + strBase64Encode);
                String iccAuthentication = TelephonyManager.from(((Phone) NtnPhone.this).mContext).getIccAuthentication(NtnPhone.this.getSubId(), 2, 129, strBase64Encode);
                NtnPhone.this.log("requestSimAuth rsp =" + iccAuthentication);
                if (TextUtils.isEmpty(iccAuthentication)) {
                    return;
                }
                NtnPhone.this.log("requestSimAuth base64Decode(rsp) =" + NtnPhone.this.base64Decode(iccAuthentication));
                NtnPhone ntnPhone3 = NtnPhone.this;
                ntnPhone3.sendMessage(ntnPhone3.obtainMessage(1005, ntnPhone3.base64Decode(iccAuthentication)));
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String base64Encode(String str) {
        if (TextUtils.isEmpty(str)) {
            loge("base64Encode, input is empty, input =" + str);
            return "";
        }
        byte[] bArrHexStringToByteArray = ByteArrayConverter.hexStringToByteArray(str);
        log("base64Encode textByte =" + bArrHexStringToByteArray);
        return Base64.encodeToString(bArrHexStringToByteArray, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String base64Decode(String str) {
        if (TextUtils.isEmpty(str)) {
            loge("base64Encode, input is empty, input =" + str);
            return "";
        }
        StringBuilder sb = new StringBuilder("base64Decode decode text =");
        Charset charset = StandardCharsets.UTF_8;
        sb.append(Base64.decode(str.getBytes(charset), 0));
        log(sb.toString());
        return ByteArrayConverter.byteArrayToHexString(Base64.decode(str.getBytes(charset), 0));
    }

    private void handleSimAuthDone() {
        this.mCi.getBasebandVersion(obtainMessage(1007));
    }

    private Message obtainCompleteMessage() {
        return obtainCompleteMessage(1001);
    }

    private Message obtainCompleteMessage(int i) {
        this.mPendingOperations++;
        this.mLastRelevantPoll = null;
        this.mNeedsPoll = true;
        log("obtainCompleteMessage: pendingOperations=" + this.mPendingOperations + ", needsPoll=" + this.mNeedsPoll);
        return obtainMessage(i);
    }

    public String getDtmfToneDelayKey() {
        return "gsm_dtmf_tone_delay_int";
    }

    @Override // com.android.internal.telephony.Phone
    public VoiceCallSessionStats getVoiceCallSessionStats() {
        return this.mVoiceCallSessionStats;
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public SignalStrength getSignalStrength() {
        NtnServiceStateTracker ntnServiceStateTracker = this.mSST;
        if (ntnServiceStateTracker == null) {
            return new SignalStrength();
        }
        return ntnServiceStateTracker.getSignalStrength();
    }

    public int getRealTxPwr() {
        return this.oldRealTxPwr;
    }

    public int getTarTxPwr() {
        return this.oldTarTxPwr;
    }

    public int getRetRssi() {
        return getSignalStrength().getDbm();
    }

    public int getRetSnr() {
        return this.mRetSnr;
    }

    public int getVoiceRate(boolean z) {
        int i = this.mVoiceRate;
        if (z) {
            this.mVoiceRate = -1;
        }
        return i;
    }

    public void registerNtnPhyParmChangeListener(onNtnPhyParmChangeListener onntnphyparmchangelistener) {
        this.listeners.add(onntnphyparmchangelistener);
    }

    public void unRegisterNtnPhyParmChangeListene(onNtnPhyParmChangeListener onntnphyparmchangelistener) {
        this.listeners.remove(onntnphyparmchangelistener);
    }

    public void registerHlsErrorListener(HlsErrorListener hlsErrorListener) {
        this.mHlsErrorListeners.add(hlsErrorListener);
    }

    public void unregisterHlsErrorListener(HlsErrorListener hlsErrorListener) {
        this.mHlsErrorListeners.remove(hlsErrorListener);
    }

    public void notifyTxpwrChange(JS_TXPWR js_txpwr) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((onNtnPhyParmChangeListener) it.next()).onTxPwrChange(js_txpwr);
        }
    }

    public void notifySnrChange(int i) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((onNtnPhyParmChangeListener) it.next()).onSnrChange(i);
        }
    }

    public void setNtnGps(String str) {
        this.mNtnGps = str;
        if (TextUtils.isEmpty(str) || !this.isReceiveGpsQuery) {
            return;
        }
        this.mNtnRil.supplyGpsForApp(this.mNtnGps, null);
    }

    public String sendCitRequest(int i, String str) {
        String str2;
        synchronized (this.mCITLock) {
            this.mCitResult = null;
            log("sendCitRequest vendorType = " + i + ";atCmd = " + str);
            this.mNtnRil.sendCustFactAtcmd(i, str, obtainMessage(1012));
            try {
                this.mCITLock.wait(2000L);
            } catch (InterruptedException e) {
                loge("sendCitRequest exception: " + e);
            }
            log("sendCitRequest mCitResult = " + this.mCitResult);
            str2 = this.mCitResult;
        }
        return str2;
    }

    public void destroy() {
        NtnSmsDispatcher ntnSmsDispatcher = NtnSmsDispatcher.getInstance();
        if (ntnSmsDispatcher != null) {
            ntnSmsDispatcher.destroy();
        }
        NtnServiceStateTracker ntnServiceStateTracker = this.mSST;
        if (ntnServiceStateTracker != null) {
            ntnServiceStateTracker.dispose();
        }
        SmsStorageMonitor smsStorageMonitor = this.mSmsStorageMonitor;
        if (smsStorageMonitor != null) {
            smsStorageMonitor.dispose();
        }
    }

    @Override // com.android.internal.telephony.ntnphone.NtnPhoneBase, com.android.internal.telephony.Phone
    public PhoneConstants.State getState() {
        return this.mCT.mState;
    }

    public void sendTemp(int i) {
        this.mNtnRil.sendTemp(i, null);
    }

    public void setScreenState(int i) {
        this.mNtnRil.setScreenState(i, null);
    }

    public void querySpeechBitrate() {
        this.mNtnRil.getSpeechBitrate(obtainMessage(1017));
    }

    public String getSpeechBitrate() {
        return this.mSpeechBitrate;
    }

    public NtnTtlogInfo getNtnTtlogInfo() {
        return this.mNtnTtlogInfo;
    }

    public HlsErrorInfo getHlsErrorInfo() {
        return this.mHlsErrorInfo;
    }

    public NtnPosInfo getNtnPosInfo() {
        return this.mNtnPosInfo;
    }

    public void setRFMode(int i, Message message) {
        this.mNtnRil.setRFMode(i, message);
        log("setRFMode: " + i);
    }

    public int getNtnBeamId() {
        NtnServiceStateTracker ntnServiceStateTracker = this.mSST;
        if (ntnServiceStateTracker != null) {
            return ntnServiceStateTracker.getNtnBeamId();
        }
        return -1;
    }

    public int getNtnBandId() {
        if (getServiceState() != null) {
            return getServiceState().getChannelNumber();
        }
        return -1;
    }

    public void getAntennaState(Message message) {
        this.mNtnRil.getAntennaState(obtainMessage(1022, message));
    }

    private void notifyAntInfoChanged(ArrayList arrayList) {
        Iterator it = this.mAntInfolisteners.iterator();
        while (it.hasNext()) {
            ((AntInfoChangedListener) it.next()).onAntInfoSignalStrengthChanged(arrayList);
        }
    }

    public void registerAntInfoChangedListener(AntInfoChangedListener antInfoChangedListener) {
        this.mAntInfolisteners.add(antInfoChangedListener);
    }

    public void unRegisterAntInfoChangedListener(AntInfoChangedListener antInfoChangedListener) {
        this.mAntInfolisteners.remove(antInfoChangedListener);
    }

    public void setPhoneId(int i) {
        this.mPhoneId = i;
        this.mGsmCdmaPhone = (GsmCdmaPhone) PhoneFactory.getPhone(i);
    }

    public void notifyStartCellularNetworkScan(boolean z) {
        Iterator it = this.mCellularCallbackList.iterator();
        while (it.hasNext()) {
            ((CellularNetworkScanCallback) it.next()).startCellularNetworkScan(z);
        }
    }

    public void registerForCellularNetworkScanCallback(CellularNetworkScanCallback cellularNetworkScanCallback) {
        if (this.mCellularCallbackList.contains(cellularNetworkScanCallback)) {
            return;
        }
        this.mCellularCallbackList.add(cellularNetworkScanCallback);
    }

    public void unregisterForCellularNetworkScanCallback(CellularNetworkScanCallback cellularNetworkScanCallback) {
        this.mCellularCallbackList.remove(cellularNetworkScanCallback);
    }

    public void notifyStartBeamswitching(int i, int i2) {
        Iterator it = this.mBeamSwitchCallbackList.iterator();
        while (it.hasNext()) {
            ((BeamswitchingCallback) it.next()).startBeamswitching(i, i2);
        }
    }

    public void registerForBeamswitchingCallback(BeamswitchingCallback beamswitchingCallback) {
        if (this.mBeamSwitchCallbackList.contains(beamswitchingCallback)) {
            return;
        }
        this.mBeamSwitchCallbackList.add(beamswitchingCallback);
    }

    public void unregisterFoBeamswitchingCallback(BeamswitchingCallback beamswitchingCallback) {
        this.mBeamSwitchCallbackList.remove(beamswitchingCallback);
    }

    private void notifyUnsolNtnCdcMsg(int i, byte[] bArr) {
        if (i == 1 && bArr != null && bArr.length > 0) {
            this.mLPMState = bArr[0];
        }
        List list = (List) this.mUnsolNtnCdcMsgListenersMap.get(Integer.valueOf(i));
        if (list == null || list.isEmpty()) {
            if (list != null) {
                this.mUnsolNtnCdcMsgListenersMap.remove(Integer.valueOf(i));
                return;
            }
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            try {
                ((UnsolNtnCdcMsgListener) it.next()).unsolNtnCdcMsg(bArr);
            } catch (Exception e) {
                loge("notifyUnsolNtnCdcMsg exception: " + e);
            }
        }
    }

    public void registerUnsolNtnCdcMsgListener(int i, UnsolNtnCdcMsgListener unsolNtnCdcMsgListener) {
        if (unsolNtnCdcMsgListener != null) {
            ((CopyOnWriteArrayList) this.mUnsolNtnCdcMsgListenersMap.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.ntnphone.NtnPhone$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return NtnPhone.$r8$lambda$g0sC4cpQ4iMHMX0WBskC4Z8KCUY((Integer) obj);
                }
            })).add(unsolNtnCdcMsgListener);
        }
    }

    public static /* synthetic */ CopyOnWriteArrayList $r8$lambda$g0sC4cpQ4iMHMX0WBskC4Z8KCUY(Integer num) {
        return new CopyOnWriteArrayList();
    }

    public void unregisterUnsolNtnCdcMsgListener(int i, UnsolNtnCdcMsgListener unsolNtnCdcMsgListener) {
        CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) this.mUnsolNtnCdcMsgListenersMap.get(Integer.valueOf(i));
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(unsolNtnCdcMsgListener);
            if (copyOnWriteArrayList.isEmpty()) {
                this.mUnsolNtnCdcMsgListenersMap.remove(Integer.valueOf(i), copyOnWriteArrayList);
            }
        }
    }

    public int getLPMState() {
        return this.mLPMState;
    }

    public static String hidePii(String str) {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d("NtnPhone", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Rlog.e("NtnPhone", "[" + this.mPhoneId + "] " + str);
    }
}
