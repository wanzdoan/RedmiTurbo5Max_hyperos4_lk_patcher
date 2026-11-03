package com.android.services.telephony;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telecom.CallAudioState;
import android.telecom.CallScreeningService;
import android.telecom.Conference;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.StatusHints;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.RtpHeaderExtension;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Pair;
import android.widget.Toast;
import com.android.ims.ImsCall;
import com.android.ims.ImsException;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.d2d.Communicator;
import com.android.internal.telephony.d2d.DtmfAdapter;
import com.android.internal.telephony.d2d.DtmfTransport;
import com.android.internal.telephony.d2d.MessageTypeAndValueHelper;
import com.android.internal.telephony.d2d.RtpAdapter;
import com.android.internal.telephony.d2d.RtpTransport;
import com.android.internal.telephony.d2d.Timeouts;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.phone.ImsUtil;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.phone.callcomposer.CallComposerPictureManager;
import com.android.phone.callcomposer.CallComposerPictureTransfer;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.telephony.Rlog;
import com.mediatek.ims.MtkImsCall;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.imsphone.MtkImsPhone;
import com.mediatek.internal.telephony.imsphone.MtkImsPhoneConnection;
import com.mediatek.services.telephony.MtkTelephonyConnectionServiceUtil;
import com.xiaomi.internal.telephony.DynamicSarService;
import gov.nist.javax.sip.address.GenericURI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import mediatek.telecom.MtkConnection;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelephonyConnection extends Connection implements Holdable, Communicator.Callback {
    private static final Map sExtrasMap = createExtrasMap();
    private Communicator mCommunicator;
    private Call.State mConnectionOverriddenState;
    private Call.State mConnectionState;
    private D2DCallStateAdapter mD2DCallStateAdapter;
    private DtmfTransport mDtmfTransport;
    private Integer mEmergencyServiceCategory;
    private List mEmergencyUrns;
    private final Handler mHandler;
    private final Messenger mHandlerMessenger;
    private int mHangupDisconnectCause;
    protected boolean mHasHighDefAudio;
    private StatusHints mIncomingWifiCallStatusHints;
    private boolean mIsAdhocConferenceCall;
    private boolean mIsCarrierVideoConferencingSupported;
    private boolean mIsCdmaVoicePrivacyEnabled;
    private boolean mIsConferenceSupported;
    private boolean mIsDomainReselectionInProgress;
    private boolean mIsHoldable;
    private boolean mIsManageImsConferenceCallSupported;
    boolean mIsMultiParty;
    private boolean mIsNetworkIdentifiedEmergencyCall;
    private boolean mIsStateOverridden;
    private boolean mIsTtyEnabled;
    private boolean mIsUsingAssistedDialing;
    private boolean mIsVideoPauseSupported;
    protected com.android.internal.telephony.Connection mOriginalConnection;
    private int mOriginalConnectionCapabilities;
    private Bundle mOriginalConnectionExtras;
    protected com.android.internal.telephony.Connection.Listener mOriginalConnectionListener;
    private Call.State mOriginalConnectionState;
    private StatusHints mOutgoingWifiCallStatusHints;
    private List mParticipants;
    private Phone mPhoneForEvents;
    private final com.android.internal.telephony.Connection.PostDialListener mPostDialListener;
    private RtpTransport mRtpTransport;
    private Connection.RttTextStream mRttTextStream;
    private boolean mShowPreciseFailedCause;
    private StatusHints mStatusHints;
    private TelephonyConnectionService mTelephonyConnectionService;
    private DisconnectCause mTelephonyDisconnectCause;
    private final Set mTelephonyListeners;
    private boolean mTreatAsEmergencyCall;
    private boolean mWasCrossSim;
    private boolean mWasImsConnection;
    private SrvccPendingAction mPendingAction = SrvccPendingAction.SRVCC_PENDING_NONE;
    private Phone mPhone = null;
    private boolean mDelayRegisterRadioState = false;

    enum SrvccPendingAction {
        SRVCC_PENDING_NONE,
        SRVCC_PENDING_ANSWER_CALL,
        SRVCC_PENDING_HOLD_CALL,
        SRVCC_PENDING_UNHOLD_CALL,
        SRVCC_PENDING_HANGUP_CALL
    }

    public abstract class TelephonyConnectionListener {
        public void onConferenceParticipantsChanged(Connection connection, List list) {
        }

        public void onConferenceParticipantsInvited(boolean z) {
        }

        public void onConferenceStarted() {
        }

        public void onConferenceSupportedChanged(Connection connection, boolean z) {
        }

        public void onConnectionCapabilitiesChanged(Connection connection, int i) {
        }

        public void onConnectionEvent(Connection connection, String str, Bundle bundle) {
        }

        public void onConnectionPropertiesChanged(Connection connection, int i) {
        }

        public void onDestroyed(Connection connection) {
        }

        public void onDisconnected(Connection connection, DisconnectCause disconnectCause) {
        }

        public void onExtrasChanged(Connection connection, Bundle bundle) {
        }

        public void onExtrasRemoved(Connection connection, List list) {
        }

        public void onOriginalConnectionConfigured(TelephonyConnection telephonyConnection) {
        }

        public void onOriginalConnectionRetry(TelephonyConnection telephonyConnection, boolean z) {
        }

        public void onRingbackRequested(Connection connection, boolean z) {
        }

        public void onStateChanged(Connection connection, int i) {
        }

        public void onStatusHintsChanged(Connection connection, StatusHints statusHints) {
        }

        public void onVideoProviderChanged(Connection connection, Connection.VideoProvider videoProvider) {
        }

        public void onVideoStateChanged(Connection connection, int i) {
        }
    }

    private int changeBitmask(int i, int i2, boolean z) {
        return z ? i | i2 : (~i2) & i;
    }

    private int transformCodec(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            default:
                return 0;
        }
    }

    public abstract TelephonyConnection cloneConnection();

    public void onOriginalConnectionRedialed(com.android.internal.telephony.Connection connection) {
        if (connection == null) {
            setDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(11, "handover failure, no connection"));
            close();
            return;
        }
        if (this.mOriginalConnection != null) {
            if ((connection.getAddress() == null || this.mOriginalConnection.getAddress() == null || !this.mOriginalConnection.getAddress().equals(connection.getAddress())) && connection.getState() != this.mOriginalConnection.getStateBeforeHandover()) {
                return;
            }
            Log.i(this, "Setting original connection after handover or redial, current original connection=" + this.mOriginalConnection.toString() + ", new original connection=" + connection.toString(), new Object[0]);
            removePropertyVoLte();
            if (this.mOriginalConnection.getVideoState() != 0 && connection.getVideoState() == 0) {
                Context context = getPhone().getContext();
                Toast.makeText(context, context.getString(R.string.vilte_srvcc_tip), 1).show();
                Log.d(this, "Video call change to vocie call during SRVCC", new Object[0]);
            }
            setOriginalConnection(connection);
            this.mWasImsConnection = false;
            trySrvccPendingAction();
            if (this.mHangupDisconnectCause != -1) {
                try {
                    Log.i(this, "user has tried to hangup during handover, retrying hangup.", new Object[0]);
                    connection.hangup();
                    return;
                } catch (CallStateException e) {
                    Log.w(this, "hangup during handover or redial resulted in an exception:" + e, new Object[0]);
                    return;
                }
            }
            return;
        }
        Log.w(this, " mOriginalConnection==null -- invalid state (not cleaned up)", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSuppServiceNotification(SuppServiceNotification suppServiceNotification) {
        com.android.internal.telephony.Connection connection;
        Log.i(this, "handleSuppServiceNotification: type=%d, code=%d, index=%d", Integer.valueOf(suppServiceNotification.notificationType), Integer.valueOf(suppServiceNotification.code), Integer.valueOf(suppServiceNotification.index));
        if (suppServiceNotification.notificationType == 0 && suppServiceNotification.code == 2 && (connection = this.mOriginalConnection) != null && !connection.isIncoming() && Call.State.HOLDING != this.mConnectionState) {
            sendTelephonyConnectionEvent("android.telephony.event.EVENT_CALL_FORWARDED", null);
        }
        sendSuppServiceNotificationEvent(suppServiceNotification.notificationType, suppServiceNotification.code);
        MiTelephonyServiceStub.get().notifySSNotificationToast(this, suppServiceNotification.notificationType, suppServiceNotification.type, suppServiceNotification.code);
    }

    private void sendSuppServiceNotificationEvent(int i, int i2) {
        CharSequence suppServiceMessage = getSuppServiceMessage(i, i2);
        if (suppServiceMessage == null || suppServiceMessage.length() == 0) {
            Log.i(this, "sendSuppServiceNotificationEvent unknown supp service message", new Object[0]);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("android.telephony.extra.NOTIFICATION_TYPE", i);
        bundle.putInt("android.telephony.extra.NOTIFICATION_CODE", i2);
        bundle.putCharSequence("android.telephony.extra.NOTIFICATION_MESSAGE", suppServiceMessage);
        sendTelephonyConnectionEvent("android.telephony.event.EVENT_SUPPLEMENTARY_SERVICE_NOTIFICATION", bundle);
        MiTelephonyServiceStub.get().ShowSuppToastIfNeed(this, i, i2);
    }

    protected CharSequence getSuppServiceMessage(int i, int i2) {
        int i3;
        if (i == 0) {
            switch (i2) {
                case 0:
                case 1:
                    i3 = R.string.supp_service_call_forwarding_active;
                    break;
                case 2:
                    i3 = R.string.supp_service_notification_call_forwarded;
                    break;
                case 3:
                    i3 = R.string.supp_service_notification_call_waiting;
                    break;
                case 4:
                    i3 = R.string.supp_service_closed_user_group_call;
                    break;
                case 5:
                    i3 = R.string.supp_service_outgoing_calls_barred;
                    break;
                case 6:
                    i3 = R.string.supp_service_incoming_calls_barred;
                    break;
                case 7:
                    i3 = R.string.supp_service_clir_suppression_rejected;
                    break;
                case 8:
                    i3 = R.string.supp_service_notification_call_deflected;
                    break;
                default:
                    i3 = -1;
                    break;
            }
        } else if (i == 1) {
            switch (i2) {
                case 0:
                    i3 = R.string.supp_service_forwarded_call;
                    break;
                case 1:
                    i3 = R.string.supp_service_closed_user_group_call;
                    break;
                case 2:
                    i3 = R.string.supp_service_call_on_hold;
                    break;
                case 3:
                    i3 = R.string.supp_service_call_resumed;
                    break;
                case 4:
                    i3 = R.string.supp_service_conference_call;
                    break;
                case 5:
                    i3 = R.string.supp_service_held_call_released;
                    break;
                case 6:
                default:
                    i3 = -1;
                    break;
                case 7:
                    i3 = R.string.supp_service_additional_ect_connecting;
                    break;
                case 8:
                    i3 = R.string.supp_service_additional_ect_connected;
                    break;
                case 9:
                    i3 = R.string.supp_service_deflected_call;
                    break;
                case 10:
                    i3 = R.string.supp_service_additional_call_forwarded;
                    break;
            }
        } else {
            i3 = -1;
        }
        if (i3 == -1 || getPhone() == null || getPhone().getContext() == null) {
            return null;
        }
        return getResourceText(i3);
    }

    public CharSequence getResourceText(int i) {
        return SubscriptionManager.getResourcesForSubId(getPhone().getContext(), getPhone().getSubId()).getText(i);
    }

    public String getResourceString(int i) {
        return SubscriptionManager.getResourcesForSubId(getPhone().getContext(), getPhone().getSubId()).getString(i);
    }

    public boolean isCarrierVideoConferencingSupported() {
        return this.mIsCarrierVideoConferencingSupported;
    }

    public class D2DCallStateAdapter extends TelephonyConnectionListener {
        private Communicator mCommunicator;

        D2DCallStateAdapter(Communicator communicator) {
            this.mCommunicator = communicator;
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onStateChanged(Connection connection, int i) {
            this.mCommunicator.onStateChanged(connection.getTelecomCallId(), i);
        }
    }

    protected TelephonyConnection(com.android.internal.telephony.Connection connection, String str, int i) {
        Handler handler = new Handler(Looper.getMainLooper()) { // from class: com.android.services.telephony.TelephonyConnection.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                String string;
                int i2 = message.what;
                if (i2 != 1000) {
                    boolean z = true;
                    switch (i2) {
                        case 1:
                            Log.v(TelephonyConnection.this, "MSG_PRECISE_CALL_STATE_CHANGED", new Object[0]);
                            TelephonyConnection.this.updateState();
                            break;
                        case 2:
                            Log.v(TelephonyConnection.this, "MSG_RINGBACK_TONE", new Object[0]);
                            if (TelephonyConnection.this.getOriginalConnection() != TelephonyConnection.this.getForegroundConnection()) {
                                Log.v(TelephonyConnection.this, "handleMessage, original connection is not foreground connection, skipping", new Object[0]);
                                return;
                            }
                            boolean zBooleanValue = ((Boolean) ((AsyncResult) message.obj).result).booleanValue();
                            TelephonyConnection.this.setRingbackRequested(zBooleanValue);
                            TelephonyConnection.this.notifyRingbackRequested(zBooleanValue);
                            break;
                            break;
                        case 3:
                        case 20:
                            Log.i(TelephonyConnection.this, "Connection changed due to: %s", i2 == 3 ? "MSG_HANDOVER_STATE_CHANGED" : "MSG_REDIAL_CONNECTION_CHANGED");
                            TelephonyConnection.this.onOriginalConnectionRedialed((com.android.internal.telephony.Connection) ((AsyncResult) message.obj).result);
                            break;
                        case 4:
                            com.android.internal.telephony.Connection connection2 = TelephonyConnection.this.mOriginalConnection;
                            if (connection2 != null && connection2.getDisconnectCause() == 380) {
                                try {
                                    TelephonyConnection.this.setOriginalConnection(TelephonyConnection.this.getPhone().dial(TelephonyConnection.this.mOriginalConnection.getOrigDialString(), new PhoneInternalInterface.DialArgs.Builder().build()));
                                    TelephonyConnection.this.notifyEcc();
                                } catch (CallStateException e) {
                                    Log.e((Object) this, (Throwable) e, "Fail to redial as ECC", new Object[0]);
                                }
                            }
                            TelephonyConnection.this.updateState();
                            break;
                        case 5:
                            boolean zBooleanValue2 = ((Boolean) message.obj).booleanValue();
                            Log.i(this, "Update multiparty state to %s", zBooleanValue2 ? "Y" : "N");
                            TelephonyConnection telephonyConnection = TelephonyConnection.this;
                            telephonyConnection.mIsMultiParty = zBooleanValue2;
                            if (zBooleanValue2) {
                                telephonyConnection.notifyConferenceStarted();
                            }
                            break;
                        case 6:
                            TelephonyConnection.this.notifyConferenceMergeFailed();
                            break;
                        case 7:
                            Phone phone = TelephonyConnection.this.getPhone();
                            TelephonyConnection telephonyConnection2 = TelephonyConnection.this;
                            StringBuilder sb = new StringBuilder("MSG_SUPP_SERVICE_NOTIFY on phoneId : ");
                            if (phone != null) {
                                string = Integer.toString(phone.getPhoneId());
                            } else {
                                string = "null";
                            }
                            sb.append(string);
                            Log.v(telephonyConnection2, sb.toString(), new Object[0]);
                            Object obj = message.obj;
                            if (obj != null && ((AsyncResult) obj).result != null) {
                                SuppServiceNotification suppServiceNotification = (SuppServiceNotification) ((AsyncResult) obj).result;
                                TelephonyConnection telephonyConnection3 = TelephonyConnection.this;
                                if (telephonyConnection3.mOriginalConnection != null) {
                                    telephonyConnection3.handleSuppServiceNotification(suppServiceNotification);
                                }
                            }
                            break;
                        case 8:
                            int iIntValue = ((Integer) message.obj).intValue();
                            String str2 = SystemProperties.get("persist.vendor.operator.optr");
                            Log.i(this, "operator: " + str2 + " mWasImsConnection: " + TelephonyConnection.this.mWasImsConnection, new Object[0]);
                            if (TelephonyConnection.this.mWasImsConnection && str2 != null && str2.equals("OP01") && iIntValue == 0 && TelephonyConnection.this.getVideoState() != 0) {
                                TelephonyConnection.this.getPhone().getContext();
                                Log.d(this, "Video call change to vocie call", new Object[0]);
                            }
                            TelephonyConnection.this.setTelephonyVideoState(iIntValue);
                            TelephonyConnection.this.refreshConferenceSupported();
                            TelephonyConnection.this.refreshDisableAddCall();
                            TelephonyConnection.this.refreshHoldSupported();
                            TelephonyConnection.this.updateConnectionProperties();
                            break;
                        case 9:
                            TelephonyConnection.this.setTelephonyVideoProvider((Connection.VideoProvider) message.obj);
                            break;
                        case 10:
                            TelephonyConnection.this.setAudioQuality(((Integer) message.obj).intValue());
                            break;
                        case 11:
                            TelephonyConnection.this.updateConferenceParticipants((List) message.obj);
                            break;
                        case 12:
                            TelephonyConnection.this.updateExtras((Bundle) message.obj);
                            break;
                        case 13:
                            TelephonyConnection.this.setOriginalConnectionCapabilities(message.arg1);
                            break;
                        case 14:
                            Pair pair = (Pair) ((AsyncResult) message.obj).result;
                            boolean zBooleanValue3 = ((Boolean) pair.second).booleanValue();
                            com.android.internal.telephony.Connection connection3 = (com.android.internal.telephony.Connection) pair.first;
                            TelephonyConnection telephonyConnection4 = TelephonyConnection.this;
                            if (connection3 == telephonyConnection4.mOriginalConnection) {
                                if (zBooleanValue3) {
                                    telephonyConnection4.sendTelephonyConnectionEvent("android.telecom.event.ON_HOLD_TONE_START", null);
                                } else {
                                    telephonyConnection4.sendTelephonyConnectionEvent("android.telecom.event.ON_HOLD_TONE_END", null);
                                }
                            }
                            break;
                        case 15:
                            Log.d(this, "MSG_CDMA_VOICE_PRIVACY_ON received", new Object[0]);
                            TelephonyConnection.this.setCdmaVoicePrivacy(true);
                            break;
                        case 16:
                            Log.d(this, "MSG_CDMA_VOICE_PRIVACY_OFF received", new Object[0]);
                            TelephonyConnection.this.setCdmaVoicePrivacy(false);
                            break;
                        case 17:
                            TelephonyConnection.this.hangup(((Integer) message.obj).intValue());
                            break;
                        case 18:
                            int iIntValue2 = ((Integer) message.obj).intValue();
                            if (TelephonyConnection.this.getCallRadioTech() == iIntValue2 || (!TelephonyConnection.this.isWifi() && iIntValue2 != 18)) {
                                z = false;
                            }
                            TelephonyConnection.this.setCallRadioTech(iIntValue2);
                            if (z) {
                                TelephonyConnection.this.updateConnectionProperties();
                                TelephonyConnection.this.updateStatusHints();
                                TelephonyConnection.this.refreshDisableAddCall();
                            }
                            break;
                        case 19:
                            SomeArgs someArgs = (SomeArgs) message.obj;
                            try {
                                TelephonyConnection.this.sendTelephonyConnectionEvent((String) someArgs.arg1, (Bundle) someArgs.arg2);
                                someArgs.recycle();
                            } catch (Throwable th) {
                                someArgs.recycle();
                                throw th;
                            }
                            break;
                        case 21:
                            TelephonyConnection.this.reject(((Integer) message.obj).intValue());
                            break;
                        case 22:
                            Log.i(this, "MSG_DTMF_DONE", new Object[0]);
                            break;
                        case 23:
                            TelephonyConnection.this.refreshCodec();
                            break;
                        case 24:
                            TelephonyConnection telephonyConnection5 = TelephonyConnection.this;
                            if (telephonyConnection5.mOriginalConnection != null) {
                                telephonyConnection5.updateConnectionProperties();
                                TelephonyConnection.this.refreshConferenceSupported();
                            }
                            TelephonyConnection.this.updateConnectionCapabilities();
                            TelephonyConnection.this.sendRttInitiationSuccess();
                            break;
                        case 25:
                            TelephonyConnection.this.performHold();
                            break;
                        case 26:
                            TelephonyConnection.this.performUnhold();
                            break;
                    }
                } else {
                    TelephonyConnection.this.handleRadioNotAvailable();
                }
                MiTelephonyServiceStub.get().handleMessage(TelephonyConnection.this, this, message);
            }
        };
        this.mHandler = handler;
        this.mHandlerMessenger = new Messenger(handler);
        this.mPostDialListener = new com.android.internal.telephony.Connection.PostDialListener() { // from class: com.android.services.telephony.TelephonyConnection.2
            public void onPostDialWait() {
                Log.v(TelephonyConnection.this, "onPostDialWait", new Object[0]);
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                com.android.internal.telephony.Connection connection2 = telephonyConnection.mOriginalConnection;
                if (connection2 != null) {
                    telephonyConnection.setPostDialWait(connection2.getRemainingPostDialString());
                }
            }

            public void onPostDialChar(char c) {
                Log.v(TelephonyConnection.this, "onPostDialChar: %s", Character.valueOf(c));
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                if (telephonyConnection.mOriginalConnection != null) {
                    telephonyConnection.setNextPostDialChar(c);
                }
            }
        };
        this.mOriginalConnectionListener = new MtkImsPhoneConnection.MtkListenerBase() { // from class: com.android.services.telephony.TelephonyConnection.3
            public void onVideoStateChanged(int i2) {
                TelephonyConnection.this.mHandler.obtainMessage(8, Integer.valueOf(i2)).sendToTarget();
            }

            public void onConnectionCapabilitiesChanged(int i2) {
                TelephonyConnection.this.mHandler.obtainMessage(13, i2, 0).sendToTarget();
            }

            public void onVideoProviderChanged(Connection.VideoProvider videoProvider) {
                TelephonyConnection.this.mHandler.obtainMessage(9, videoProvider).sendToTarget();
            }

            public void onCallRadioTechChanged(int i2) {
                TelephonyConnection.this.mHandler.obtainMessage(18, Integer.valueOf(i2)).sendToTarget();
            }

            public void onAudioQualityChanged(int i2) {
                TelephonyConnection.this.mHandler.obtainMessage(10, Integer.valueOf(i2)).sendToTarget();
            }

            public void onMediaAttributesChanged() {
                TelephonyConnection.this.mHandler.obtainMessage(23).sendToTarget();
            }

            public void onConferenceParticipantsChanged(List list) {
                TelephonyConnection.this.mHandler.obtainMessage(11, list).sendToTarget();
            }

            public void onMultipartyStateChanged(boolean z) {
                TelephonyConnection.this.handleMultipartyStateChange(z);
            }

            public void onConferenceMergedFailed() {
                TelephonyConnection.this.handleConferenceMergeFailed();
            }

            public void onExtrasChanged(Bundle bundle) {
                TelephonyConnection.this.mHandler.obtainMessage(12, bundle).sendToTarget();
            }

            public void onExitedEcmMode() {
                TelephonyConnection.this.handleExitedEcmMode();
            }

            public void onCallPullFailed(com.android.internal.telephony.Connection connection2) {
                if (connection2 == null) {
                    return;
                }
                Log.i(this, "onCallPullFailed - pull failed; swapping back to call: %s", connection2);
                TelephonyConnection.this.sendTelephonyConnectionEvent("android.telecom.event.CALL_PULL_FAILED", null);
                TelephonyConnection.this.setOriginalConnection(connection2);
                TelephonyConnection.this.setActiveInternal();
            }

            public void onHandoverToWifiFailed() {
                TelephonyConnection.this.sendTelephonyConnectionEvent("android.telephony.event.EVENT_HANDOVER_TO_WIFI_FAILED", null);
            }

            public void onConnectionEvent(String str2, Bundle bundle) {
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = str2;
                someArgsObtain.arg2 = bundle;
                boolean zEquals = "android.telecom.event.MERGE_COMPLETE".equals(str2);
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                if (zEquals) {
                    telephonyConnection.sendTelephonyConnectionEvent(str2, bundle);
                } else {
                    telephonyConnection.mHandler.obtainMessage(19, someArgsObtain).sendToTarget();
                }
            }

            public void onRttModifyRequestReceived() {
                TelephonyConnection.this.sendRemoteRttRequest();
            }

            public void onRttModifyResponseReceived(int i2) {
                TelephonyConnection.this.updateConnectionProperties();
                TelephonyConnection.this.refreshConferenceSupported();
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                if (i2 == 1) {
                    telephonyConnection.sendRttInitiationSuccess();
                } else {
                    telephonyConnection.sendRttInitiationFailure(i2);
                }
            }

            public void onDisconnect(int i2) {
                Log.i(this, "onDisconnect: callId=%s, cause=%s", TelephonyConnection.this.getTelecomCallId(), android.telephony.DisconnectCause.toString(i2));
                TelephonyConnection.this.mHandler.obtainMessage(4).sendToTarget();
            }

            public void onRttInitiated() {
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                Log.i(telephonyConnection, "onRttInitiated: callId=%s", telephonyConnection.getTelecomCallId());
                TelephonyConnection.this.mHandler.obtainMessage(24).sendToTarget();
            }

            public void onRttTerminated() {
                TelephonyConnection.this.updateConnectionCapabilities();
                TelephonyConnection.this.updateConnectionProperties();
                TelephonyConnection.this.refreshConferenceSupported();
                TelephonyConnection.this.sendRttSessionRemotelyTerminated();
            }

            public void onOriginalConnectionReplaced(com.android.internal.telephony.Connection connection2) {
                Log.i(TelephonyConnection.this, "onOriginalConnectionReplaced; newConn=%s", connection2);
                TelephonyConnection.this.setOriginalConnection(connection2);
            }

            public void onIsNetworkEmergencyCallChanged(boolean z) {
                TelephonyConnection.this.setIsNetworkIdentifiedEmergencyCall(z);
            }

            public void onReceivedRtpHeaderExtensions(Set set) {
                if (TelephonyConnection.this.mRtpTransport == null) {
                    return;
                }
                Log.i(this, "onReceivedRtpHeaderExtensions: received %d extensions", Integer.valueOf(set.size()));
                TelephonyConnection.this.mRtpTransport.onRtpHeaderExtensionsReceived(set);
            }

            public void onReceivedDtmfDigit(char c) {
                if (TelephonyConnection.this.mDtmfTransport == null) {
                    return;
                }
                Log.i(this, "onReceivedDtmfDigit: digit=%c", Character.valueOf(c));
                TelephonyConnection.this.mDtmfTransport.onDtmfReceived(c);
            }

            public void onAudioModeIsVoipChanged(int i2) {
                boolean z = i2 == 0;
                Log.i(this, "onAudioModeIsVoipChanged isVoip =" + z, new Object[0]);
                TelephonyConnection.this.setAudioModeIsVoip(z);
            }

            public void onConferenceParticipantsInvited(boolean z) {
                TelephonyConnection.this.notifyConferenceParticipantsInvited(z);
            }

            public void onRedialEcc(boolean z) {
                Log.i(this, "onRedialEcc: callId=" + TelephonyConnection.this.getTelecomCallId() + " isNeedUserConfirm=" + z, new Object[0]);
                if (z) {
                    return;
                }
                TelephonyConnection.this.notifyEcc();
            }

            public void onAddressDisplayChanged() {
                TelephonyConnection.this.updateAddress();
            }
        };
        Call.State state = Call.State.IDLE;
        this.mConnectionState = state;
        this.mOriginalConnectionExtras = new Bundle();
        this.mIsStateOverridden = false;
        this.mOriginalConnectionState = state;
        this.mConnectionOverriddenState = state;
        this.mRttTextStream = null;
        this.mIsMultiParty = false;
        this.mHangupDisconnectCause = -1;
        this.mTelephonyListeners = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));
        this.mEmergencyServiceCategory = null;
        this.mEmergencyUrns = null;
        this.mOutgoingWifiCallStatusHints = null;
        this.mIncomingWifiCallStatusHints = null;
        this.mStatusHints = null;
        setCallDirection(i);
        setTelecomCallId(str);
        if (connection != null) {
            setOriginalConnection(connection);
        }
        MiTelephonyServiceStub.get().newTelephonyConnection(this);
    }

    protected TelephonyConnection() {
        Handler handler = new Handler(Looper.getMainLooper()) { // from class: com.android.services.telephony.TelephonyConnection.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                String string;
                int i2 = message.what;
                if (i2 != 1000) {
                    boolean z = true;
                    switch (i2) {
                        case 1:
                            Log.v(TelephonyConnection.this, "MSG_PRECISE_CALL_STATE_CHANGED", new Object[0]);
                            TelephonyConnection.this.updateState();
                            break;
                        case 2:
                            Log.v(TelephonyConnection.this, "MSG_RINGBACK_TONE", new Object[0]);
                            if (TelephonyConnection.this.getOriginalConnection() != TelephonyConnection.this.getForegroundConnection()) {
                                Log.v(TelephonyConnection.this, "handleMessage, original connection is not foreground connection, skipping", new Object[0]);
                                return;
                            }
                            boolean zBooleanValue = ((Boolean) ((AsyncResult) message.obj).result).booleanValue();
                            TelephonyConnection.this.setRingbackRequested(zBooleanValue);
                            TelephonyConnection.this.notifyRingbackRequested(zBooleanValue);
                            break;
                            break;
                        case 3:
                        case 20:
                            Log.i(TelephonyConnection.this, "Connection changed due to: %s", i2 == 3 ? "MSG_HANDOVER_STATE_CHANGED" : "MSG_REDIAL_CONNECTION_CHANGED");
                            TelephonyConnection.this.onOriginalConnectionRedialed((com.android.internal.telephony.Connection) ((AsyncResult) message.obj).result);
                            break;
                        case 4:
                            com.android.internal.telephony.Connection connection2 = TelephonyConnection.this.mOriginalConnection;
                            if (connection2 != null && connection2.getDisconnectCause() == 380) {
                                try {
                                    TelephonyConnection.this.setOriginalConnection(TelephonyConnection.this.getPhone().dial(TelephonyConnection.this.mOriginalConnection.getOrigDialString(), new PhoneInternalInterface.DialArgs.Builder().build()));
                                    TelephonyConnection.this.notifyEcc();
                                } catch (CallStateException e) {
                                    Log.e((Object) this, (Throwable) e, "Fail to redial as ECC", new Object[0]);
                                }
                            }
                            TelephonyConnection.this.updateState();
                            break;
                        case 5:
                            boolean zBooleanValue2 = ((Boolean) message.obj).booleanValue();
                            Log.i(this, "Update multiparty state to %s", zBooleanValue2 ? "Y" : "N");
                            TelephonyConnection telephonyConnection = TelephonyConnection.this;
                            telephonyConnection.mIsMultiParty = zBooleanValue2;
                            if (zBooleanValue2) {
                                telephonyConnection.notifyConferenceStarted();
                            }
                            break;
                        case 6:
                            TelephonyConnection.this.notifyConferenceMergeFailed();
                            break;
                        case 7:
                            Phone phone = TelephonyConnection.this.getPhone();
                            TelephonyConnection telephonyConnection2 = TelephonyConnection.this;
                            StringBuilder sb = new StringBuilder("MSG_SUPP_SERVICE_NOTIFY on phoneId : ");
                            if (phone != null) {
                                string = Integer.toString(phone.getPhoneId());
                            } else {
                                string = "null";
                            }
                            sb.append(string);
                            Log.v(telephonyConnection2, sb.toString(), new Object[0]);
                            Object obj = message.obj;
                            if (obj != null && ((AsyncResult) obj).result != null) {
                                SuppServiceNotification suppServiceNotification = (SuppServiceNotification) ((AsyncResult) obj).result;
                                TelephonyConnection telephonyConnection3 = TelephonyConnection.this;
                                if (telephonyConnection3.mOriginalConnection != null) {
                                    telephonyConnection3.handleSuppServiceNotification(suppServiceNotification);
                                }
                            }
                            break;
                        case 8:
                            int iIntValue = ((Integer) message.obj).intValue();
                            String str2 = SystemProperties.get("persist.vendor.operator.optr");
                            Log.i(this, "operator: " + str2 + " mWasImsConnection: " + TelephonyConnection.this.mWasImsConnection, new Object[0]);
                            if (TelephonyConnection.this.mWasImsConnection && str2 != null && str2.equals("OP01") && iIntValue == 0 && TelephonyConnection.this.getVideoState() != 0) {
                                TelephonyConnection.this.getPhone().getContext();
                                Log.d(this, "Video call change to vocie call", new Object[0]);
                            }
                            TelephonyConnection.this.setTelephonyVideoState(iIntValue);
                            TelephonyConnection.this.refreshConferenceSupported();
                            TelephonyConnection.this.refreshDisableAddCall();
                            TelephonyConnection.this.refreshHoldSupported();
                            TelephonyConnection.this.updateConnectionProperties();
                            break;
                        case 9:
                            TelephonyConnection.this.setTelephonyVideoProvider((Connection.VideoProvider) message.obj);
                            break;
                        case 10:
                            TelephonyConnection.this.setAudioQuality(((Integer) message.obj).intValue());
                            break;
                        case 11:
                            TelephonyConnection.this.updateConferenceParticipants((List) message.obj);
                            break;
                        case 12:
                            TelephonyConnection.this.updateExtras((Bundle) message.obj);
                            break;
                        case 13:
                            TelephonyConnection.this.setOriginalConnectionCapabilities(message.arg1);
                            break;
                        case 14:
                            Pair pair = (Pair) ((AsyncResult) message.obj).result;
                            boolean zBooleanValue3 = ((Boolean) pair.second).booleanValue();
                            com.android.internal.telephony.Connection connection3 = (com.android.internal.telephony.Connection) pair.first;
                            TelephonyConnection telephonyConnection4 = TelephonyConnection.this;
                            if (connection3 == telephonyConnection4.mOriginalConnection) {
                                if (zBooleanValue3) {
                                    telephonyConnection4.sendTelephonyConnectionEvent("android.telecom.event.ON_HOLD_TONE_START", null);
                                } else {
                                    telephonyConnection4.sendTelephonyConnectionEvent("android.telecom.event.ON_HOLD_TONE_END", null);
                                }
                            }
                            break;
                        case 15:
                            Log.d(this, "MSG_CDMA_VOICE_PRIVACY_ON received", new Object[0]);
                            TelephonyConnection.this.setCdmaVoicePrivacy(true);
                            break;
                        case 16:
                            Log.d(this, "MSG_CDMA_VOICE_PRIVACY_OFF received", new Object[0]);
                            TelephonyConnection.this.setCdmaVoicePrivacy(false);
                            break;
                        case 17:
                            TelephonyConnection.this.hangup(((Integer) message.obj).intValue());
                            break;
                        case 18:
                            int iIntValue2 = ((Integer) message.obj).intValue();
                            if (TelephonyConnection.this.getCallRadioTech() == iIntValue2 || (!TelephonyConnection.this.isWifi() && iIntValue2 != 18)) {
                                z = false;
                            }
                            TelephonyConnection.this.setCallRadioTech(iIntValue2);
                            if (z) {
                                TelephonyConnection.this.updateConnectionProperties();
                                TelephonyConnection.this.updateStatusHints();
                                TelephonyConnection.this.refreshDisableAddCall();
                            }
                            break;
                        case 19:
                            SomeArgs someArgs = (SomeArgs) message.obj;
                            try {
                                TelephonyConnection.this.sendTelephonyConnectionEvent((String) someArgs.arg1, (Bundle) someArgs.arg2);
                                someArgs.recycle();
                            } catch (Throwable th) {
                                someArgs.recycle();
                                throw th;
                            }
                            break;
                        case 21:
                            TelephonyConnection.this.reject(((Integer) message.obj).intValue());
                            break;
                        case 22:
                            Log.i(this, "MSG_DTMF_DONE", new Object[0]);
                            break;
                        case 23:
                            TelephonyConnection.this.refreshCodec();
                            break;
                        case 24:
                            TelephonyConnection telephonyConnection5 = TelephonyConnection.this;
                            if (telephonyConnection5.mOriginalConnection != null) {
                                telephonyConnection5.updateConnectionProperties();
                                TelephonyConnection.this.refreshConferenceSupported();
                            }
                            TelephonyConnection.this.updateConnectionCapabilities();
                            TelephonyConnection.this.sendRttInitiationSuccess();
                            break;
                        case 25:
                            TelephonyConnection.this.performHold();
                            break;
                        case 26:
                            TelephonyConnection.this.performUnhold();
                            break;
                    }
                } else {
                    TelephonyConnection.this.handleRadioNotAvailable();
                }
                MiTelephonyServiceStub.get().handleMessage(TelephonyConnection.this, this, message);
            }
        };
        this.mHandler = handler;
        this.mHandlerMessenger = new Messenger(handler);
        this.mPostDialListener = new com.android.internal.telephony.Connection.PostDialListener() { // from class: com.android.services.telephony.TelephonyConnection.2
            public void onPostDialWait() {
                Log.v(TelephonyConnection.this, "onPostDialWait", new Object[0]);
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                com.android.internal.telephony.Connection connection2 = telephonyConnection.mOriginalConnection;
                if (connection2 != null) {
                    telephonyConnection.setPostDialWait(connection2.getRemainingPostDialString());
                }
            }

            public void onPostDialChar(char c) {
                Log.v(TelephonyConnection.this, "onPostDialChar: %s", Character.valueOf(c));
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                if (telephonyConnection.mOriginalConnection != null) {
                    telephonyConnection.setNextPostDialChar(c);
                }
            }
        };
        this.mOriginalConnectionListener = new MtkImsPhoneConnection.MtkListenerBase() { // from class: com.android.services.telephony.TelephonyConnection.3
            public void onVideoStateChanged(int i2) {
                TelephonyConnection.this.mHandler.obtainMessage(8, Integer.valueOf(i2)).sendToTarget();
            }

            public void onConnectionCapabilitiesChanged(int i2) {
                TelephonyConnection.this.mHandler.obtainMessage(13, i2, 0).sendToTarget();
            }

            public void onVideoProviderChanged(Connection.VideoProvider videoProvider) {
                TelephonyConnection.this.mHandler.obtainMessage(9, videoProvider).sendToTarget();
            }

            public void onCallRadioTechChanged(int i2) {
                TelephonyConnection.this.mHandler.obtainMessage(18, Integer.valueOf(i2)).sendToTarget();
            }

            public void onAudioQualityChanged(int i2) {
                TelephonyConnection.this.mHandler.obtainMessage(10, Integer.valueOf(i2)).sendToTarget();
            }

            public void onMediaAttributesChanged() {
                TelephonyConnection.this.mHandler.obtainMessage(23).sendToTarget();
            }

            public void onConferenceParticipantsChanged(List list) {
                TelephonyConnection.this.mHandler.obtainMessage(11, list).sendToTarget();
            }

            public void onMultipartyStateChanged(boolean z) {
                TelephonyConnection.this.handleMultipartyStateChange(z);
            }

            public void onConferenceMergedFailed() {
                TelephonyConnection.this.handleConferenceMergeFailed();
            }

            public void onExtrasChanged(Bundle bundle) {
                TelephonyConnection.this.mHandler.obtainMessage(12, bundle).sendToTarget();
            }

            public void onExitedEcmMode() {
                TelephonyConnection.this.handleExitedEcmMode();
            }

            public void onCallPullFailed(com.android.internal.telephony.Connection connection2) {
                if (connection2 == null) {
                    return;
                }
                Log.i(this, "onCallPullFailed - pull failed; swapping back to call: %s", connection2);
                TelephonyConnection.this.sendTelephonyConnectionEvent("android.telecom.event.CALL_PULL_FAILED", null);
                TelephonyConnection.this.setOriginalConnection(connection2);
                TelephonyConnection.this.setActiveInternal();
            }

            public void onHandoverToWifiFailed() {
                TelephonyConnection.this.sendTelephonyConnectionEvent("android.telephony.event.EVENT_HANDOVER_TO_WIFI_FAILED", null);
            }

            public void onConnectionEvent(String str2, Bundle bundle) {
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = str2;
                someArgsObtain.arg2 = bundle;
                boolean zEquals = "android.telecom.event.MERGE_COMPLETE".equals(str2);
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                if (zEquals) {
                    telephonyConnection.sendTelephonyConnectionEvent(str2, bundle);
                } else {
                    telephonyConnection.mHandler.obtainMessage(19, someArgsObtain).sendToTarget();
                }
            }

            public void onRttModifyRequestReceived() {
                TelephonyConnection.this.sendRemoteRttRequest();
            }

            public void onRttModifyResponseReceived(int i2) {
                TelephonyConnection.this.updateConnectionProperties();
                TelephonyConnection.this.refreshConferenceSupported();
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                if (i2 == 1) {
                    telephonyConnection.sendRttInitiationSuccess();
                } else {
                    telephonyConnection.sendRttInitiationFailure(i2);
                }
            }

            public void onDisconnect(int i2) {
                Log.i(this, "onDisconnect: callId=%s, cause=%s", TelephonyConnection.this.getTelecomCallId(), android.telephony.DisconnectCause.toString(i2));
                TelephonyConnection.this.mHandler.obtainMessage(4).sendToTarget();
            }

            public void onRttInitiated() {
                TelephonyConnection telephonyConnection = TelephonyConnection.this;
                Log.i(telephonyConnection, "onRttInitiated: callId=%s", telephonyConnection.getTelecomCallId());
                TelephonyConnection.this.mHandler.obtainMessage(24).sendToTarget();
            }

            public void onRttTerminated() {
                TelephonyConnection.this.updateConnectionCapabilities();
                TelephonyConnection.this.updateConnectionProperties();
                TelephonyConnection.this.refreshConferenceSupported();
                TelephonyConnection.this.sendRttSessionRemotelyTerminated();
            }

            public void onOriginalConnectionReplaced(com.android.internal.telephony.Connection connection2) {
                Log.i(TelephonyConnection.this, "onOriginalConnectionReplaced; newConn=%s", connection2);
                TelephonyConnection.this.setOriginalConnection(connection2);
            }

            public void onIsNetworkEmergencyCallChanged(boolean z) {
                TelephonyConnection.this.setIsNetworkIdentifiedEmergencyCall(z);
            }

            public void onReceivedRtpHeaderExtensions(Set set) {
                if (TelephonyConnection.this.mRtpTransport == null) {
                    return;
                }
                Log.i(this, "onReceivedRtpHeaderExtensions: received %d extensions", Integer.valueOf(set.size()));
                TelephonyConnection.this.mRtpTransport.onRtpHeaderExtensionsReceived(set);
            }

            public void onReceivedDtmfDigit(char c) {
                if (TelephonyConnection.this.mDtmfTransport == null) {
                    return;
                }
                Log.i(this, "onReceivedDtmfDigit: digit=%c", Character.valueOf(c));
                TelephonyConnection.this.mDtmfTransport.onDtmfReceived(c);
            }

            public void onAudioModeIsVoipChanged(int i2) {
                boolean z = i2 == 0;
                Log.i(this, "onAudioModeIsVoipChanged isVoip =" + z, new Object[0]);
                TelephonyConnection.this.setAudioModeIsVoip(z);
            }

            public void onConferenceParticipantsInvited(boolean z) {
                TelephonyConnection.this.notifyConferenceParticipantsInvited(z);
            }

            public void onRedialEcc(boolean z) {
                Log.i(this, "onRedialEcc: callId=" + TelephonyConnection.this.getTelecomCallId() + " isNeedUserConfirm=" + z, new Object[0]);
                if (z) {
                    return;
                }
                TelephonyConnection.this.notifyEcc();
            }

            public void onAddressDisplayChanged() {
                TelephonyConnection.this.updateAddress();
            }
        };
        Call.State state = Call.State.IDLE;
        this.mConnectionState = state;
        this.mOriginalConnectionExtras = new Bundle();
        this.mIsStateOverridden = false;
        this.mOriginalConnectionState = state;
        this.mConnectionOverriddenState = state;
        this.mRttTextStream = null;
        this.mIsMultiParty = false;
        this.mHangupDisconnectCause = -1;
        this.mTelephonyListeners = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));
        this.mEmergencyServiceCategory = null;
        this.mEmergencyUrns = null;
        this.mOutgoingWifiCallStatusHints = null;
        this.mIncomingWifiCallStatusHints = null;
        this.mStatusHints = null;
    }

    @Override // android.telecom.Connection
    public void onCallEvent(String str, Bundle bundle) {
        str.hashCode();
        switch (str) {
            case "mediatek.telecom.event.CALL_EVENT_VIDEO_SCREEN_OPERATION":
                ImsPhone phone = getPhone();
                if (phone != null) {
                    int i = bundle.getInt("type");
                    String string = bundle.getString("data");
                    String string2 = bundle.getString("duration");
                    if (phone instanceof ImsPhone) {
                        ImsPhone imsPhone = phone;
                        if (imsPhone.getForegroundCall() != null && imsPhone.getForegroundCall().getImsCall() != null) {
                            Log.d(this, "video screen onCallEvent: " + i, new Object[0]);
                            MtkImsCall imsCall = imsPhone.getForegroundCall().getImsCall();
                            if (imsCall instanceof MtkImsCall) {
                                imsCall.videoScreenOperation(i, string, string2);
                            }
                            break;
                        }
                    }
                }
                break;
            case "android.telecom.event.DEVICE_TO_DEVICE_MESSAGE":
                handleOutgoingDeviceToDeviceMessage(bundle);
                break;
            case "mediatek.telecom.event.CALL_EVENT_VIDEO_RINGTONE_OPERATION":
                ImsPhone phone2 = getPhone();
                if (phone2 != null) {
                    int i2 = bundle.getInt("type");
                    String string3 = bundle.getString("data");
                    if (phone2 instanceof ImsPhone) {
                        ImsPhone imsPhone2 = phone2;
                        if (imsPhone2.getForegroundCall() != null && imsPhone2.getForegroundCall().getImsCall() != null) {
                            Log.d(this, "MO video ringtone onCallEvent: " + i2, new Object[0]);
                            MtkImsCall imsCall2 = imsPhone2.getForegroundCall().getImsCall();
                            if (imsCall2 instanceof MtkImsCall) {
                                imsCall2.videoScreenOperation(i2, string3, (String) null);
                            }
                            break;
                        } else if (imsPhone2.getRingingCall() != null && imsPhone2.getRingingCall().getImsCall() != null) {
                            Log.d(this, "MT video ringtone onCallEvent: " + i2, new Object[0]);
                            MtkImsCall imsCall3 = imsPhone2.getRingingCall().getImsCall();
                            if (imsCall3 instanceof MtkImsCall) {
                                imsCall3.videoScreenOperation(i2, string3, (String) null);
                            }
                            break;
                        }
                    }
                }
                break;
            case "mediatek.telecom.event.REQUEST_HANGUPALL":
                handleHangupAll();
                break;
        }
    }

    @Override // android.telecom.Connection
    public void onCallAudioStateChanged(CallAudioState callAudioState) {
        if (getPhone() != null) {
            getPhone().setEchoSuppressionEnabled();
        }
    }

    @Override // android.telecom.Connection
    public void onStateChanged(int i) {
        Log.v(this, "onStateChanged, state: " + Connection.stateToString(i), new Object[0]);
        updateStatusHints();
    }

    @Override // android.telecom.Connection
    public void onDisconnect() {
        Log.v(this, "onDisconnect", new Object[0]);
        TelephonyConnectionService telephonyConnectionService = this.mTelephonyConnectionService;
        if (telephonyConnectionService != null) {
            List allPeerPhoneConnections = telephonyConnectionService.getAllPeerPhoneConnections(this);
            if (!allPeerPhoneConnections.isEmpty() && ((Connection) allPeerPhoneConnections.get(0)).getState() == 2) {
                Log.v(this, "onDisconnect: hangup quickly to avoid timing issue in DSDA Case", new Object[0]);
                hangup(3);
                return;
            }
        }
        this.mHandler.obtainMessage(17, 3).sendToTarget();
        MiTelephonyServiceStub.get().setLocalDisconnecting(this, true);
    }

    @Override // android.telecom.Connection
    public void onSeparate() {
        Log.v(this, "onSeparate", new Object[0]);
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection != null) {
            try {
                connection.separate();
            } catch (CallStateException e) {
                Log.e((Object) this, (Throwable) e, "Call to Connection.separate failed with exception", new Object[0]);
            }
        }
    }

    @Override // android.telecom.Connection
    public void onAddConferenceParticipants(List list) {
        performAddConferenceParticipants(list);
    }

    @Override // android.telecom.Connection
    public void onAbort() {
        Log.v(this, "onAbort", new Object[0]);
        this.mHandler.obtainMessage(17, 3).sendToTarget();
    }

    @Override // android.telecom.Connection
    public void onHold() {
        this.mHandler.obtainMessage(25).sendToTarget();
    }

    @Override // android.telecom.Connection
    public void onUnhold() {
        this.mHandler.obtainMessage(26).sendToTarget();
    }

    @Override // android.telecom.Connection
    public void onAnswer(int i) {
        performAnswer(i);
        MiTelephonyServiceStub.get().onAnswer(this, i);
    }

    @Override // android.telecom.Connection
    public void onDeflect(Uri uri) {
        Log.v(this, "onDeflect", new Object[0]);
        if (this.mOriginalConnection == null || !isValidRingingCall()) {
            return;
        }
        if (uri == null) {
            Log.w(this, "call deflect address uri is null", new Object[0]);
            return;
        }
        String scheme = uri.getScheme();
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        if (!"voicemail".equals(scheme)) {
            if (!GenericURI.TEL.equals(scheme)) {
                Log.w(this, "onDeflect, address scheme is not of type tel instead: " + scheme, new Object[0]);
                return;
            } else {
                if (PhoneNumberUtils.isUriNumber(schemeSpecificPart)) {
                    Log.w(this, "Invalid deflect address. Not a legal PSTN number.", new Object[0]);
                    return;
                }
                String strConvertAndStrip = PhoneNumberUtils.convertAndStrip(schemeSpecificPart);
                if (TextUtils.isEmpty(strConvertAndStrip)) {
                    Log.w(this, "Empty deflect number obtained from address uri", new Object[0]);
                    return;
                }
                try {
                    this.mOriginalConnection.deflect(strConvertAndStrip);
                    return;
                } catch (CallStateException e) {
                    Log.e((Object) this, (Throwable) e, "Failed to deflect call.", new Object[0]);
                    return;
                }
            }
        }
        Log.w(this, "Cannot deflect to voicemail uri", new Object[0]);
    }

    @Override // android.telecom.Connection
    public void onReject() {
        performReject(1);
    }

    @Override // android.telecom.Connection
    public void onReject(int i) {
        performReject(i);
    }

    public void performReject(int i) {
        Log.v(this, "performReject", new Object[0]);
        if (isValidRingingCall()) {
            this.mHandler.obtainMessage(21, Integer.valueOf(i)).sendToTarget();
        }
        super.onReject();
    }

    public void onTransfer(Uri uri, boolean z) {
        Log.v(this, "onTransfer", new Object[0]);
        if (this.mOriginalConnection != null) {
            if (uri == null) {
                Log.w(this, "call transfer uri is null", new Object[0]);
                return;
            }
            String scheme = uri.getScheme();
            String schemeSpecificPart = uri.getSchemeSpecificPart();
            if (!"voicemail".equals(scheme)) {
                if (!GenericURI.TEL.equals(scheme)) {
                    Log.w(this, "onTransfer, number scheme is not of type tel instead: " + scheme, new Object[0]);
                    return;
                } else {
                    if (PhoneNumberUtils.isUriNumber(schemeSpecificPart)) {
                        Log.w(this, "Invalid transfer address. Not a legal PSTN number.", new Object[0]);
                        return;
                    }
                    String strConvertAndStrip = PhoneNumberUtils.convertAndStrip(schemeSpecificPart);
                    if (TextUtils.isEmpty(strConvertAndStrip)) {
                        Log.w(this, "Empty transfer number obtained from uri", new Object[0]);
                        return;
                    }
                    try {
                        this.mOriginalConnection.transfer(strConvertAndStrip, z);
                        return;
                    } catch (CallStateException e) {
                        Log.e((Object) this, (Throwable) e, "Failed to transfer call.", new Object[0]);
                        return;
                    }
                }
            }
            Log.w(this, "Cannot transfer to voicemail uri", new Object[0]);
        }
    }

    public void onTransfer(Connection connection) {
        Log.v(this, "onConsultativeTransfer", new Object[0]);
        com.android.internal.telephony.Connection connection2 = this.mOriginalConnection;
        if (connection2 == null || !(connection instanceof TelephonyConnection)) {
            return;
        }
        try {
            connection2.consultativeTransfer(((TelephonyConnection) connection).getOriginalConnection());
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "Failed to transfer call.", new Object[0]);
        }
    }

    @Override // android.telecom.Connection
    public void onPostDialContinue(boolean z) {
        Log.v(this, "onPostDialContinue, proceed: " + z, new Object[0]);
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection != null) {
            if (z) {
                connection.proceedAfterWaitChar();
            } else {
                connection.cancelPostDial();
            }
        }
    }

    @Override // android.telecom.Connection
    public void onPullExternalCall() {
        if ((getConnectionProperties() & 16) != 16) {
            Log.w(this, "onPullExternalCall - cannot pull non-external call", new Object[0]);
            return;
        }
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection != null) {
            connection.pullExternalCall();
        }
    }

    @Override // android.telecom.Connection
    public void onStartRtt(Connection.RttTextStream rttTextStream) {
        if (isImsConnection()) {
            ImsPhoneConnection imsPhoneConnection = this.mOriginalConnection;
            if (imsPhoneConnection.isRttEnabledForCall()) {
                imsPhoneConnection.setCurrentRttTextStream(rttTextStream);
                return;
            } else {
                imsPhoneConnection.startRtt(rttTextStream);
                return;
            }
        }
        Log.w(this, "onStartRtt - not in IMS, so RTT cannot be enabled.", new Object[0]);
    }

    @Override // android.telecom.Connection
    public void onStopRtt() {
        if (isImsConnection()) {
            ImsPhoneConnection imsPhoneConnection = this.mOriginalConnection;
            if (imsPhoneConnection.isRttEnabledForCall()) {
                imsPhoneConnection.stopRtt();
                return;
            } else {
                Log.w(this, "onStopRtt - not in RTT call, ignoring", new Object[0]);
                return;
            }
        }
        Log.w(this, "onStopRtt - not in IMS, ignoring", new Object[0]);
    }

    public void onCallFilteringCompleted(Connection.CallFilteringCompletionInfo callFilteringCompletionInfo) {
        boolean zIsInContacts;
        String callExtra;
        boolean zEquals = Objects.equals(((TelecomManager) getPhone().getContext().getSystemService(TelecomManager.class)).getSystemDialerPackage(), callFilteringCompletionInfo.getCallScreeningComponent() == null ? null : callFilteringCompletionInfo.getCallScreeningComponent().getPackageName());
        CallScreeningService.CallResponse callResponse = callFilteringCompletionInfo.getCallResponse();
        if (zEquals && callResponse != null && callResponse.getCallComposerAttachmentsToShow() >= 0) {
            zIsInContacts = (callResponse.getCallComposerAttachmentsToShow() & 1) != 0;
        } else {
            zIsInContacts = callFilteringCompletionInfo.isInContacts();
        }
        if (!zIsInContacts) {
            zIsInContacts = isBusinessCall();
        }
        if (isImsConnection()) {
            ImsPhone imsPhone = getPhone() instanceof ImsPhone ? (ImsPhone) getPhone() : null;
            if (imsPhone == null || imsPhone.getCallComposerStatus() != 1 || callFilteringCompletionInfo.isBlocked() || !zIsInContacts) {
                return;
            }
            ImsCallProfile callProfile = this.mOriginalConnection.getImsCall().getCallProfile();
            if (CallComposerPictureManager.sTestMode) {
                callExtra = CallComposerPictureManager.FAKE_SERVER_URL;
            } else {
                callExtra = callProfile.getCallExtra("android.telephony.ims.extra.PICTURE_URL");
            }
            if (callProfile == null || TextUtils.isEmpty(callExtra)) {
                return;
            }
            CallComposerPictureManager.getInstance(getPhone().getContext(), getPhone().getSubId()).handleDownloadFromServer(new CallComposerPictureTransfer.Factory(this) { // from class: com.android.services.telephony.TelephonyConnection.4
            }, callExtra, new Consumer() { // from class: com.android.services.telephony.TelephonyConnection$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$onCallFilteringCompleted$0((Pair) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCallFilteringCompleted$0(Pair pair) {
        if (pair.first != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("android.telecom.extra.PICTURE_URI", (Parcelable) pair.first);
            putTelephonyExtras(bundle);
        } else {
            Log.i(this, "Call composer picture download: error=" + pair.second, new Object[0]);
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("android.telecom.extra.HAS_PICTURE", false);
            putTelephonyExtras(bundle2);
        }
    }

    private boolean isBusinessCall() {
        Bundle extras = getExtras();
        if (extras == null) {
            return false;
        }
        return extras.getBoolean("android.telephony.ims.extra.IS_BUSINESS_CALL", false);
    }

    @Override // android.telecom.Connection
    public void handleRttUpgradeResponse(Connection.RttTextStream rttTextStream) {
        if (!isImsConnection()) {
            Log.w(this, "handleRttUpgradeResponse - not in IMS, so RTT cannot be enabled.", new Object[0]);
        } else {
            this.mOriginalConnection.sendRttModifyResponse(rttTextStream);
        }
    }

    public void performAnswer(int i) {
        Log.v(this, "performAnswer", new Object[0]);
        if (!isValidRingingCall() || getPhone() == null) {
            return;
        }
        getExtras().getBoolean("android.telecom.extra.ANSWERING_DROPS_FG_CALL");
        try {
            getPhone().acceptCall(i);
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "Failed to accept call.", new Object[0]);
            if (e.getError() == 3) {
                this.mPendingAction = SrvccPendingAction.SRVCC_PENDING_ANSWER_CALL;
            }
        }
    }

    public void performHold() {
        Log.v(this, "performHold", new Object[0]);
        if (Call.State.ACTIVE == this.mConnectionState) {
            Log.v(this, "Holding active call", new Object[0]);
            try {
                ImsPhone phone = this.mOriginalConnection.getCall().getPhone();
                Call ringingCall = phone.getRingingCall();
                if (phone.getPhoneType() == 5) {
                    phone.holdActiveCall();
                    return;
                }
                if (ringingCall.getState() != Call.State.WAITING && this.mOriginalConnection.getState() != Call.State.DISCONNECTING) {
                    phone.switchHoldingAndActive();
                    return;
                }
                return;
            } catch (CallStateException e) {
                Log.e((Object) this, (Throwable) e, "Exception occurred while trying to put call on hold.", new Object[0]);
                if (e.getError() == 3) {
                    this.mPendingAction = SrvccPendingAction.SRVCC_PENDING_HOLD_CALL;
                    return;
                }
                return;
            }
        }
        Log.w(this, "Cannot put a call that is not currently active on hold.", new Object[0]);
    }

    public void performUnhold() {
        Log.v(this, "performUnhold", new Object[0]);
        if (Call.State.HOLDING == this.mConnectionState) {
            sendUnhold();
        } else {
            Log.w(this, "Cannot release a call that is not already on hold from hold.", new Object[0]);
        }
    }

    private void sendUnhold() {
        try {
            ImsPhone phone = this.mOriginalConnection.getCall().getPhone();
            if (phone.getPhoneType() == 5) {
                phone.unholdHeldCall();
                return;
            }
            if (!hasMultipleTopLevelCalls()) {
                if (phone.getPhoneType() == 5) {
                    phone.unholdHeldCall();
                    return;
                } else {
                    this.mOriginalConnection.getCall().getPhone().switchHoldingAndActive();
                    return;
                }
            }
            Log.i(this, "Skipping unhold command for %s", this);
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "Exception occurred while trying to release call from hold.", new Object[0]);
            if (e.getError() == 3) {
                this.mPendingAction = SrvccPendingAction.SRVCC_PENDING_UNHOLD_CALL;
            }
        }
    }

    public void performConference(Connection connection) {
        Log.d(this, "performConference - %s", this);
        if (getPhone() != null) {
            try {
                getPhone().conference();
                MiTelephonyServiceStub.get().performConference(this, connection);
            } catch (CallStateException e) {
                Log.e((Object) this, (Throwable) e, "Failed to conference call.", new Object[0]);
            }
        }
    }

    private String[] getAddConferenceParticipants(List list) {
        String[] strArr = new String[list.size()];
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            strArr[i] = ((Uri) it.next()).getSchemeSpecificPart();
            i++;
        }
        return strArr;
    }

    public void performAddConferenceParticipants(List list) {
        Log.v(this, "performAddConferenceParticipants", new Object[0]);
        if (this.mOriginalConnection.getCall() instanceof ImsPhoneCall) {
            try {
                this.mOriginalConnection.getCall().getImsCall().inviteParticipants(getAddConferenceParticipants(list));
            } catch (ImsException e) {
                Log.e((Object) this, (Throwable) e, "failed to add conference participants", new Object[0]);
            }
        }
    }

    protected int buildConnectionCapabilities() {
        boolean zCanHoldImsCalls;
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        int i = (connection == null || !connection.isIncoming()) ? 0 : 262144;
        if (shouldTreatAsEmergencyCall() || !isImsConnection()) {
            zCanHoldImsCalls = false;
        } else if (isRtt()) {
            Log.d(this, "buildConnectionCapabilities: Call is RTT, evaluating hold capabilities.", new Object[0]);
            zCanHoldImsCalls = canHoldImsCalls() && canHoldRttCalls();
            Log.d(this, "buildConnectionCapabilities: RTT hold allowed = " + zCanHoldImsCalls, new Object[0]);
        } else {
            zCanHoldImsCalls = canHoldImsCalls();
        }
        if (zCanHoldImsCalls) {
            i = (this.mIsHoldable && (getState() == 4 || getState() == 5)) ? i | 3 : i | 2;
        }
        Log.d(this, "buildConnectionCapabilities: isHoldable = " + this.mIsHoldable + " State = " + getState() + " capabilities = " + i, new Object[0]);
        return i;
    }

    protected final void updateConnectionCapabilities() {
        int iChangeBitmask;
        boolean z = false;
        int iChangeBitmask2 = changeBitmask(applyAddParticipantCapabilities(changeBitmask(applyConferenceTerminationCapabilities(changeBitmask(changeBitmask(applyOriginalConnectionCapabilities(buildConnectionCapabilities()), 1048576, this.mIsVideoPauseSupported && isVideoCapable()), 16777216, isExternalConnection() && isPullable())), 33554432, isImsConnection() && canDeflectImsCalls())), 268435456, (isImsConnection() || isGsmCdmaConnection()) && canConsultativeTransfer());
        if (TelephonyBaseUtilsStub.isMiuiRom()) {
            if ((isImsConnection() || isGsmCdmaConnection()) && canTransferToNumber()) {
                z = true;
            }
            iChangeBitmask = changeBitmask(iChangeBitmask2, 134217728, z);
        } else {
            if (isImsConnection() && canTransferToNumber()) {
                z = true;
            }
            iChangeBitmask = changeBitmask(iChangeBitmask2, 134217728, z);
        }
        if (getConnectionCapabilities() != iChangeBitmask) {
            setConnectionCapabilities(iChangeBitmask);
            notifyConnectionCapabilitiesChanged(iChangeBitmask);
        }
    }

    protected int buildConnectionProperties() {
        Phone phone = getPhone();
        return (phone == null || !phone.isInEcm()) ? 0 : 1;
    }

    protected final void updateConnectionProperties() {
        int iUpdatePropertyVoLte = updatePropertyVoLte(changeBitmask(changeBitmask(changeBitmask(changeBitmask(changeBitmask(changeBitmask(changeBitmask(changeBitmask(changeBitmask(buildConnectionProperties(), 4, hasHighDefAudioProperty()), 8, isWifi() && !isCrossSimCall()), 16, isExternalConnection()), 32, this.mIsCdmaVoicePrivacyEnabled), 512, this.mIsUsingAssistedDialing), DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER, isRtt()), SmartNetworkRelayScore.TRAFFIC_UNIT, isNetworkIdentifiedEmergencyCall()), 4096, isAdhocConferenceCall()), 8192, isCrossSimCall()));
        if (getConnectionProperties() != iUpdatePropertyVoLte) {
            setTelephonyConnectionProperties(iUpdatePropertyVoLte);
        }
    }

    public void setTelephonyConnectionProperties(int i) {
        setConnectionProperties(i);
        notifyConnectionPropertiesChanged(i);
    }

    protected final void updateAddress() {
        Uri addressFromNumber;
        updateConnectionCapabilities();
        updateConnectionProperties();
        if (this.mOriginalConnection != null) {
            if (isShowingOriginalDialString() && this.mOriginalConnection.getOrigDialString() != null) {
                addressFromNumber = getAddressFromNumber(PhoneNumberUtils.extractNetworkPortionAlt(this.mOriginalConnection.getOrigDialString()));
            } else {
                boolean zIsNeededToFormatIncomingNumberForJp = isNeededToFormatIncomingNumberForJp();
                com.android.internal.telephony.Connection connection = this.mOriginalConnection;
                if (zIsNeededToFormatIncomingNumberForJp) {
                    addressFromNumber = getAddressFromNumber(formatIncomingNumberForJp(connection.getAddress()));
                } else {
                    addressFromNumber = getAddressFromNumber(connection.getAddress());
                }
            }
            String strFilterCnapName = filterCnapName(this.mOriginalConnection.getCnapName());
            int cnapNamePresentation = this.mOriginalConnection.getCnapNamePresentation();
            if (!Objects.equals(strFilterCnapName, getCallerDisplayName()) || cnapNamePresentation != getCallerDisplayNamePresentation()) {
                Log.v(this, "updateAddress, caller display name changed", new Object[0]);
                setCallerDisplayName(strFilterCnapName, cnapNamePresentation);
            }
            MiTelephonyServiceStub.get().updateAddress(this, addressFromNumber);
            if (((TelephonyManager) getPhone().getContext().getSystemService("phone")).isEmergencyNumber(this.mOriginalConnection.getAddress())) {
                this.mTreatAsEmergencyCall = true;
            }
            refreshConferenceSupported();
        }
    }

    public void registerForCallEvents(Phone phone) {
        Phone phone2;
        if (this.mPhoneForEvents == phone) {
            Log.i(this, "registerForCallEvents - same phone requested forregistration, ignoring.", new Object[0]);
            return;
        }
        Log.i(this, "registerForCallEvents; phone=%s", phone);
        unregisterForCallEvents();
        phone.registerForPreciseCallStateChanged(this.mHandler, 1, (Object) null);
        phone.registerForHandoverStateChanged(this.mHandler, 3, (Object) null);
        phone.registerForRedialConnectionChanged(this.mHandler, 20, (Object) null);
        phone.registerForRingbackTone(this.mHandler, 2, (Object) null);
        phone.registerForSuppServiceNotification(this.mHandler, 7, (Object) null);
        phone.registerForOnHoldTone(this.mHandler, 14, (Object) null);
        this.mPhoneForEvents = phone;
        if (this.mDelayRegisterRadioState && (phone2 = this.mPhone) != null) {
            phone2.mCi.registerForNotAvailable(this.mHandler, 1000, (Object) null);
            this.mDelayRegisterRadioState = false;
        }
        MiTelephonyServiceStub.get().registerForCallEvents(this, phone, this.mHandler);
    }

    void setOriginalConnection(com.android.internal.telephony.Connection connection) {
        ArrayList<String> forwardedNumber;
        Log.i(this, "setOriginalConnection: TelephonyConnection, originalConnection: " + connection, new Object[0]);
        if (this.mOriginalConnection != null && connection != null && !connection.isIncoming() && connection.getOrigDialString() == null && isShowingOriginalDialString()) {
            Log.i(this, "new original dial string is null, convert to: " + this.mOriginalConnection.getOrigDialString(), new Object[0]);
            connection.restoreDialedNumberAfterConversion(this.mOriginalConnection.getOrigDialString());
        }
        com.android.internal.telephony.Connection connection2 = this.mOriginalConnection;
        boolean zIsNetworkIdentifiedEmergencyCall = connection2 != null ? connection2.isNetworkIdentifiedEmergencyCall() : false;
        clearOriginalConnection();
        this.mOriginalConnectionExtras.clear();
        this.mOriginalConnection = connection;
        connection.setTelecomCallId(getTelecomCallId());
        registerForCallEvents(getPhone());
        this.mOriginalConnection.addPostDialListener(this.mPostDialListener);
        this.mOriginalConnection.addListener(this.mOriginalConnectionListener);
        registerSuppMessageManagerForImsPhone(getPhone(), this.mOriginalConnection);
        setTelephonyVideoState(this.mOriginalConnection.getVideoState());
        setOriginalConnectionCapabilities(this.mOriginalConnection.getConnectionCapabilities());
        setIsNetworkIdentifiedEmergencyCall(zIsNetworkIdentifiedEmergencyCall || this.mOriginalConnection.isNetworkIdentifiedEmergencyCall());
        setIsAdhocConferenceCall(this.mOriginalConnection.isAdhocConference());
        setAudioModeIsVoip(this.mOriginalConnection.getAudioModeIsVoip());
        setTelephonyVideoProvider(this.mOriginalConnection.getVideoProvider());
        setAudioQuality(this.mOriginalConnection.getAudioQuality());
        setTechnologyTypeExtra();
        setCallRadioTech(this.mOriginalConnection.getCallRadioTech());
        Bundle connectionExtras = this.mOriginalConnection.getConnectionExtras();
        this.mHandler.obtainMessage(12, connectionExtras == null ? null : new Bundle(connectionExtras)).sendToTarget();
        TelephonyManager telephonyManager = (TelephonyManager) getPhone().getContext().getSystemService("phone");
        if (zIsNetworkIdentifiedEmergencyCall || telephonyManager.isEmergencyNumber(this.mOriginalConnection.getAddress())) {
            this.mTreatAsEmergencyCall = true;
        }
        setCallerNumberVerificationStatus(this.mOriginalConnection.getNumberVerificationStatus());
        if (isImsConnection()) {
            this.mWasImsConnection = true;
        }
        if (connection instanceof ImsPhoneConnection) {
            maybeConfigureDeviceToDeviceCommunication();
        }
        this.mIsMultiParty = this.mOriginalConnection.isMultiparty();
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        boolean zMaybePersistDropsFgCallExtra = maybePersistDropsFgCallExtra();
        Log.i(this, "persistDropsFgCallExtra? " + zMaybePersistDropsFgCallExtra, new Object[0]);
        if (this.mOriginalConnection.isActiveCallDisconnectedOnAnswer()) {
            bundle.putBoolean("android.telecom.extra.ANSWERING_DROPS_FG_CALL", true);
        } else if (!zMaybePersistDropsFgCallExtra) {
            arrayList.add("android.telecom.extra.ANSWERING_DROPS_FG_CALL");
        }
        if (shouldSetDisableAddCallExtra()) {
            bundle.putBoolean("android.telecom.extra.DISABLE_ADD_CALL", true);
        } else {
            arrayList.add("android.telecom.extra.DISABLE_ADD_CALL");
        }
        com.android.internal.telephony.Connection connection3 = this.mOriginalConnection;
        if (connection3 != null && (forwardedNumber = connection3.getForwardedNumber()) != null) {
            bundle.putStringArrayList("android.telecom.extra.LAST_FORWARDED_NUMBER", forwardedNumber);
        }
        putTelephonyExtras(bundle);
        removeTelephonyExtras(arrayList);
        updateState();
        if (this.mOriginalConnection == null) {
            Log.w(this, "original Connection was nulled out as part of setOriginalConnection. " + connection, new Object[0]);
        }
        fireOnOriginalConnectionConfigured();
    }

    private boolean maybePersistDropsFgCallExtra() {
        return getCarrierConfig().getBoolean("show_vowifi_drop_dialog_on_dsds_bool") && getExtras().getBoolean("android.telecom.extra.ANSWERING_DROPS_FG_CALL", false);
    }

    protected String filterCnapName(final String str) {
        if (str == null) {
            return null;
        }
        PersistableBundle carrierConfig = getCarrierConfig();
        String[] stringArray = carrierConfig != null ? carrierConfig.getStringArray("filtered_cnap_names_string_array") : null;
        if (stringArray == null || Arrays.asList(stringArray).stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnection$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((String) obj).equals(str.toUpperCase(Locale.ROOT));
            }
        }).count() <= 0) {
            return str;
        }
        Log.i(this, "filterCnapName: Filtered CNAP Name: ".concat(str), new Object[0]);
        return "";
    }

    private void setTechnologyTypeExtra() {
        if (getPhone() != null) {
            Bundle extras = getExtras();
            if (extras == null) {
                extras = new Bundle();
            }
            extras.putInt("android.telecom.extra.CALL_TECHNOLOGY_TYPE", getPhone().getPhoneType());
            putTelephonyExtras(extras);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshHoldSupported() {
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection == null) {
            Log.w(this, "refreshHoldSupported org conn is null", new Object[0]);
        } else {
            if (connection.shouldAllowHoldingVideoCall()) {
                return;
            }
            if (canHoldImsCalls() != ((getConnectionCapabilities() & 3) != 0)) {
                updateConnectionCapabilities();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshDisableAddCall() {
        if (shouldSetDisableAddCallExtra()) {
            Bundle extras = getExtras();
            if (extras == null) {
                extras = new Bundle();
            }
            if (extras.getBoolean("android.telecom.extra.DISABLE_ADD_CALL")) {
                Log.v(this, "filter duplicate update for disalbe add call", new Object[0]);
                return;
            } else {
                extras.putBoolean("android.telecom.extra.DISABLE_ADD_CALL", true);
                putTelephonyExtras(extras);
                return;
            }
        }
        Bundle extras2 = getExtras();
        if (extras2 == null || !extras2.containsKey("android.telecom.extra.DISABLE_ADD_CALL")) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("android.telecom.extra.DISABLE_ADD_CALL");
        removeTelephonyExtras(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshCodec() {
        boolean z;
        Bundle extras = getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        int iTransformCodec = isImsConnection() ? transformCodec(getOriginalConnection().getAudioCodec()) : 0;
        int i = extras.getInt("android.telecom.extra.AUDIO_CODEC", 0);
        boolean z2 = true;
        if (iTransformCodec != i) {
            extras.putInt("android.telecom.extra.AUDIO_CODEC", iTransformCodec);
            Log.i(this, "refreshCodec: codec changed; old=%d, new=%d", Integer.valueOf(i), Integer.valueOf(iTransformCodec));
            z = true;
        } else {
            z = false;
        }
        if (isImsConnection()) {
            float audioCodecBitrateKbps = getOriginalConnection().getAudioCodecBitrateKbps();
            float f = extras.getFloat("android.telecom.extra.AUDIO_CODEC_BITRATE_KBPS", 0.0f);
            if (Math.abs(audioCodecBitrateKbps - f) > 0.01f) {
                extras.putFloat("android.telecom.extra.AUDIO_CODEC_BITRATE_KBPS", audioCodecBitrateKbps);
                Log.i(this, "refreshCodec: bitrate changed; old=%f, new=%f", Float.valueOf(f), Float.valueOf(audioCodecBitrateKbps));
                z = true;
            }
            float audioCodecBandwidthKhz = getOriginalConnection().getAudioCodecBandwidthKhz();
            float f2 = extras.getFloat("android.telecom.extra.AUDIO_CODEC_BANDWIDTH_KHZ", 0.0f);
            if (Math.abs(audioCodecBandwidthKhz - f2) > 0.01f) {
                extras.putFloat("android.telecom.extra.AUDIO_CODEC_BANDWIDTH_KHZ", audioCodecBandwidthKhz);
                Log.i(this, "refreshCodec: bandwidth changed; old=%f, new=%f", Float.valueOf(f2), Float.valueOf(audioCodecBandwidthKhz));
            } else {
                z2 = z;
            }
            z = z2;
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.add("android.telecom.extra.AUDIO_CODEC_BITRATE_KBPS");
            arrayList.add("android.telecom.extra.AUDIO_CODEC_BANDWIDTH_KHZ");
            removeTelephonyExtras(arrayList);
        }
        if (z) {
            Log.i(this, "refreshCodec: Codec:" + extras.getInt("android.telecom.extra.AUDIO_CODEC", 0) + ", Bitrate:" + extras.getFloat("android.telecom.extra.AUDIO_CODEC_BITRATE_KBPS", 0.0f) + ", Bandwidth:" + extras.getFloat("android.telecom.extra.AUDIO_CODEC_BANDWIDTH_KHZ", 0.0f), new Object[0]);
            putTelephonyExtras(extras);
        }
    }

    private boolean shouldSetDisableAddCallExtra() {
        ImsPhone phone;
        boolean zIsWfcEnabled;
        boolean zWasVideoCall;
        boolean zIsVideoCall;
        ImsCall imsCall;
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection == null) {
            return false;
        }
        boolean zShouldAllowAddCallDuringVideoCall = connection.shouldAllowAddCallDuringVideoCall();
        Log.d(this, "carrierShouldAllowAddCall: " + zShouldAllowAddCallDuringVideoCall, new Object[0]);
        if (zShouldAllowAddCallDuringVideoCall || (phone = getPhone()) == null) {
            return false;
        }
        if (phone instanceof ImsPhone) {
            ImsPhoneCall foregroundCall = phone.getForegroundCall();
            if (foregroundCall == null || (imsCall = foregroundCall.getImsCall()) == null) {
                zWasVideoCall = false;
                zIsVideoCall = false;
            } else {
                zIsVideoCall = imsCall.isVideoCall();
                zWasVideoCall = imsCall.wasVideoCall();
            }
            zIsWfcEnabled = isWfcEnabled(phone);
        } else {
            zIsWfcEnabled = false;
            zWasVideoCall = false;
            zIsVideoCall = false;
        }
        Log.d(this, "isCurrentVideoCall: " + zIsVideoCall + ", wasVideoCall: " + zWasVideoCall + ", isVowifiEnabled: " + zIsWfcEnabled, new Object[0]);
        if (zIsVideoCall) {
            return true;
        }
        return zWasVideoCall && isWifi() && !zIsWfcEnabled;
    }

    private boolean hasHighDefAudioProperty() {
        if (!this.mHasHighDefAudio) {
            return false;
        }
        boolean zIsVideo = VideoProfile.isVideo(getVideoState());
        PersistableBundle carrierConfig = getCarrierConfig();
        boolean z = carrierConfig != null && carrierConfig.getBoolean("wifi_calls_can_be_hd_audio");
        boolean z2 = carrierConfig != null && carrierConfig.getBoolean("video_calls_can_be_hd_audio");
        boolean z3 = carrierConfig != null && carrierConfig.getBoolean("gsm_cdma_calls_can_be_hd_audio");
        if (carrierConfig == null || !carrierConfig.getBoolean("display_hd_audio_property_bool")) {
            return false;
        }
        if (isGsmCdmaConnection() && !z3) {
            return false;
        }
        if (!zIsVideo || z2) {
            return !isWifi() || z;
        }
        return false;
    }

    public final List getParticipants() {
        return this.mParticipants;
    }

    public final void setParticipants(List list) {
        this.mParticipants = list;
    }

    public final boolean isAdhocConferenceCall() {
        return this.mIsAdhocConferenceCall;
    }

    public void setIsAdhocConferenceCall(boolean z) {
        this.mIsAdhocConferenceCall = z;
        updateConnectionProperties();
    }

    private boolean canHoldImsCalls() {
        PersistableBundle carrierConfig = getCarrierConfig();
        if (doesDeviceRespectHoldCarrierConfig() && carrierConfig != null && !carrierConfig.getBoolean("allow_hold_in_ims_call")) {
            return false;
        }
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        return (connection != null && connection.shouldAllowHoldingVideoCall()) || !VideoProfile.isVideo(getVideoState());
    }

    private boolean canHoldRttCalls() {
        PersistableBundle carrierConfig = getCarrierConfig();
        if (carrierConfig == null) {
            Log.w(this, "canHoldRttCalls: CarrierConfig is unavailable. Defaulting to true.", new Object[0]);
            return true;
        }
        boolean z = carrierConfig.getBoolean("allow_hold_in_rtt_call_bool");
        Log.d(this, "canHoldRttCalls: KEY_ALLOW_HOLD_IN_RTT_CALL_BOOL=" + z, new Object[0]);
        return z;
    }

    private boolean isConferenceHosted() {
        if (getTelephonyConnectionService() == null) {
            return false;
        }
        for (Conference conference : getTelephonyConnectionService().getAllConferences()) {
            if (conference instanceof ImsConference) {
                ImsConference imsConference = (ImsConference) conference;
                if (getState() != conference.getState() && imsConference.isConferenceHost()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAddParticipantCapable() {
        Call.State state;
        com.android.internal.telephony.Connection connection;
        boolean z = false;
        if (getPhone() != null && getPhone().getPhoneType() == 5) {
            if (!getCarrierConfig().getBoolean("support_add_conference_participants_bool")) {
                return false;
            }
            if (!this.mTreatAsEmergencyCall && (((state = this.mConnectionState) == Call.State.ACTIVE || state == Call.State.HOLDING) && (((connection = this.mOriginalConnection) != null && connection.isConferenceHost()) || !isConferenceHosted()))) {
                z = true;
            }
            if (z && this.mOriginalConnection != null && !this.mIsMultiParty) {
                return this.mOriginalConnectionExtras.getBoolean("android.telephony.ims.extra.EXTENDING_TO_CONFERENCE_SUPPORTED", this.mOriginalConnectionExtras.getBoolean("conference_avail", z));
            }
        }
        return z;
    }

    private int applyAddParticipantCapabilities(int i) {
        if (isAddParticipantCapable()) {
            return changeBitmask(i, 67108864, true);
        }
        return changeBitmask(i, 67108864, false);
    }

    public PersistableBundle getCarrierConfig() {
        Phone phone = getPhone();
        try {
            if (phone == null) {
                Log.w(this, "getCarrierConfig: phone is null. Returning CarrierConfigManager.getDefaultConfig()", new Object[0]);
                return CarrierConfigManager.getDefaultConfig();
            }
            try {
                PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(phone.getSubId());
                return carrierConfigForSubId == null ? CarrierConfigManager.getDefaultConfig() : carrierConfigForSubId;
            } catch (Exception e) {
                Log.e(this, e, "getCarrierConfig: caught Exception when calling PhoneGlobals.getCarrierConfigForSubId(phone.getSubId()). Returning CarrierConfigManager.getDefaultConfig()", new Object[0]);
                return CarrierConfigManager.getDefaultConfig();
            }
        } catch (Throwable th) {
            CarrierConfigManager.getDefaultConfig();
            throw th;
        }
    }

    public boolean isRttMergeSupported(PersistableBundle persistableBundle) {
        return persistableBundle.getBoolean("allow_merging_rtt_calls_bool");
    }

    private boolean canDeflectImsCalls() {
        return getCarrierConfig().getBoolean("carrier_allow_deflect_ims_call_bool") && isValidRingingCall();
    }

    private boolean isCallTransferSupported() {
        Phone phone;
        if (TelephonyBaseUtilsStub.isMiuiRom()) {
            return MiTelephonyServiceStub.get().isCallTransferSupported(this, isImsConnection(), isGsmCdmaConnection());
        }
        if (isImsConnection()) {
            return getCarrierConfig().getBoolean("carrier_allow_transfer_ims_call_bool");
        }
        return isGsmCdmaConnection() && (phone = getPhone()) != null && phone.getPhoneType() == 1;
    }

    private boolean canTransfer(TelephonyConnection telephonyConnection) {
        com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
        if (originalConnection == null || originalConnection.isMultiparty()) {
            return false;
        }
        return telephonyConnection.getState() == 4 || telephonyConnection.getState() == 5;
    }

    private boolean canTransferToNumber() {
        if (isCallTransferSupported()) {
            return canTransfer(this);
        }
        return false;
    }

    private boolean canConsultativeTransfer() {
        if (isCallTransferSupported() && canTransfer(this) && getTelephonyConnectionService() != null) {
            for (Connection connection : getTelephonyConnectionService().getAllSamePhoneConnections(this)) {
                if (connection != this && (connection instanceof TelephonyConnection) && canTransfer((TelephonyConnection) connection)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean doesDeviceRespectHoldCarrierConfig() {
        Phone phone = getPhone();
        if (phone == null) {
            return true;
        }
        return phone.getContext().getResources().getBoolean(android.R.bool.config_disableWeaverOnUnsecuredUsers);
    }

    protected boolean shouldTreatAsEmergencyCall() {
        return this.mTreatAsEmergencyCall;
    }

    public void setShouldTreatAsEmergencyCall(boolean z) {
        this.mTreatAsEmergencyCall = z;
    }

    void clearOriginalConnection() {
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection != null) {
            Log.i(this, "clearOriginalConnection; clearing=%s", connection);
            Phone phone = this.mPhoneForEvents;
            if (phone != null) {
                unregisterSuppMessageManagerForImsPhone(phone, this.mOriginalConnection);
            }
            unregisterForCallEvents();
            this.mOriginalConnection.removePostDialListener(this.mPostDialListener);
            this.mOriginalConnection.removeListener(this.mOriginalConnectionListener);
            this.mOriginalConnection = null;
        }
    }

    public void unregisterForCallEvents() {
        Phone phone = this.mPhoneForEvents;
        if (phone == null) {
            return;
        }
        phone.unregisterForPreciseCallStateChanged(this.mHandler);
        this.mPhoneForEvents.unregisterForRingbackTone(this.mHandler);
        this.mPhoneForEvents.unregisterForHandoverStateChanged(this.mHandler);
        this.mPhoneForEvents.unregisterForRedialConnectionChanged(this.mHandler);
        this.mPhoneForEvents.unregisterForDisconnect(this.mHandler);
        this.mPhoneForEvents.unregisterForSuppServiceNotification(this.mHandler);
        this.mPhoneForEvents.unregisterForOnHoldTone(this.mHandler);
        this.mPhoneForEvents.unregisterForInCallVoicePrivacyOn(this.mHandler);
        this.mPhoneForEvents.unregisterForInCallVoicePrivacyOff(this.mHandler);
        MiTelephonyServiceStub.get().unregisterForCallEvents(this, this.mPhoneForEvents, this.mHandler);
        this.mPhoneForEvents = null;
        Phone phone2 = this.mPhone;
        if (phone2 != null) {
            phone2.mCi.unregisterForNotAvailable(this.mHandler);
            this.mPhone = null;
        }
    }

    public void hangup(int i) {
        if (this.mOriginalConnection != null) {
            this.mHangupDisconnectCause = i;
            try {
                if (isValidRingingCall()) {
                    Call call = getCall();
                    if (call != null) {
                        call.hangup();
                        return;
                    } else {
                        Log.w(this, "Attempting to hangup a connection without backing call.", new Object[0]);
                        return;
                    }
                }
                this.mOriginalConnection.hangup();
                return;
            } catch (CallStateException e) {
                Log.e((Object) this, (Throwable) e, "Call to Connection.hangup failed with exception", new Object[0]);
                this.mPendingAction = SrvccPendingAction.SRVCC_PENDING_HANGUP_CALL;
                return;
            }
        }
        this.mTelephonyConnectionService.onLocalHangup(this);
        if (getState() == 6) {
            Log.i(this, "hangup called on an already disconnected call!", new Object[0]);
            close();
        } else {
            setTelephonyConnectionDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(3, "Local Disconnect before connection established."));
            close();
        }
    }

    protected void reject(int i) {
        if (this.mOriginalConnection != null) {
            this.mHangupDisconnectCause = 16;
            try {
                if (isValidRingingCall()) {
                    Call call = getCall();
                    if (call != null) {
                        call.hangup(i);
                        return;
                    } else {
                        Log.w(this, "Attempting to hangup a connection without backing call.", new Object[0]);
                        return;
                    }
                }
                this.mOriginalConnection.hangup();
                return;
            } catch (CallStateException e) {
                Log.e((Object) this, (Throwable) e, "Call to Connection.hangup failed with exception", new Object[0]);
                return;
            }
        }
        if (getState() == 6) {
            Log.i(this, "hangup called on an already disconnected call!", new Object[0]);
            close();
        } else {
            setTelephonyConnectionDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(3, "Local Disconnect before connection established."));
            close();
        }
    }

    public com.android.internal.telephony.Connection getOriginalConnection() {
        return this.mOriginalConnection;
    }

    protected Call getCall() {
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection != null) {
            return connection.getCall();
        }
        return null;
    }

    public Phone getPhone() {
        Call call = getCall();
        if (call != null) {
            return call.getPhone();
        }
        return null;
    }

    private boolean hasMultipleTopLevelCalls() {
        int i;
        Phone phone = getPhone();
        if (phone != null) {
            i = !phone.getRingingCall().isIdle() ? 1 : 0;
            if (!phone.getForegroundCall().isIdle()) {
                i++;
            }
            if (!phone.getBackgroundCall().isIdle()) {
                i++;
            }
        } else {
            i = 0;
        }
        return i > 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.internal.telephony.Connection getForegroundConnection() {
        if (getPhone() != null) {
            try {
                return getPhone().getForegroundCall().getEarliestConnection();
            } catch (IndexOutOfBoundsException e) {
                Log.e(this, e, "IndexOutOfBoundsException in getEarliestConnection()", new Object[0]);
            }
        }
        return null;
    }

    public List getConferenceParticipants() {
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection == null) {
            Log.w(this, "Null mOriginalConnection, cannot get conf participants.", new Object[0]);
            return null;
        }
        return connection.getConferenceParticipants();
    }

    protected boolean isValidRingingCall() {
        if (getPhone() == null) {
            Log.v(this, "isValidRingingCall, phone is null", new Object[0]);
            return false;
        }
        Call ringingCall = getPhone().getRingingCall();
        if (!ringingCall.getState().isRinging()) {
            Log.v(this, "isValidRingingCall, ringing call is not in ringing state", new Object[0]);
            return false;
        }
        if (ringingCall.getEarliestConnection() != this.mOriginalConnection) {
            Log.v(this, "isValidRingingCall, ringing call connection does not match", new Object[0]);
            return false;
        }
        Log.v(this, "isValidRingingCall, returning true", new Object[0]);
        return true;
    }

    protected void updateExtras(Bundle bundle) {
        if (this.mOriginalConnection != null) {
            if (bundle != null) {
                if (!areBundlesEqual(this.mOriginalConnectionExtras, bundle)) {
                    if (Log.DEBUG) {
                        Log.d(this, "Updating extras:", new Object[0]);
                        for (String str : bundle.keySet()) {
                            Object obj = bundle.get(str);
                            if (obj instanceof String) {
                                Log.d(this, "updateExtras Key=" + Rlog.pii("TelephonyConnection", str) + " value=" + Rlog.pii("TelephonyConnection", obj), new Object[0]);
                            }
                        }
                    }
                    this.mOriginalConnectionExtras.clear();
                    this.mOriginalConnectionExtras.putAll(bundle);
                    for (String str2 : this.mOriginalConnectionExtras.keySet()) {
                        Map map = sExtrasMap;
                        if (map.containsKey(str2)) {
                            this.mOriginalConnectionExtras.putString((String) map.get(str2), bundle.getString(str2));
                            this.mOriginalConnectionExtras.remove(str2);
                        }
                    }
                    putTelephonyExtras(this.mOriginalConnectionExtras);
                    if (this.mOriginalConnectionExtras.containsKey("android.telephony.ims.extra.EXTENDING_TO_CONFERENCE_SUPPORTED") || this.mOriginalConnectionExtras.containsKey("conference_avail")) {
                        updateConnectionCapabilities();
                    }
                    boolean zContainsKey = this.mWasCrossSim | this.mOriginalConnectionExtras.containsKey("android.telephony.ims.extra.IS_CROSS_SIM_CALL");
                    this.mWasCrossSim = zContainsKey;
                    if (zContainsKey) {
                        updateStatusHints();
                        updateConnectionProperties();
                    }
                    this.mOriginalConnectionExtras.clear();
                    this.mOriginalConnectionExtras.putAll(bundle);
                    return;
                }
                Log.d(this, "Extras update not required", new Object[0]);
                return;
            }
            Log.d(this, "updateExtras extras: " + Rlog.pii("TelephonyConnection", bundle), new Object[0]);
        }
    }

    private static boolean areBundlesEqual(Bundle bundle, Bundle bundle2) {
        if (bundle == null || bundle2 == null) {
            return bundle == bundle2;
        }
        if (bundle.size() != bundle2.size()) {
            return false;
        }
        for (String str : bundle.keySet()) {
            if (str != null) {
                Object obj = bundle.get(str);
                Object obj2 = bundle2.get(str);
                if (obj != null && (obj instanceof Bundle) && obj2 != null && (obj2 instanceof Bundle)) {
                    return areBundlesEqual((Bundle) obj, (Bundle) obj2);
                }
                if (!Objects.equals(obj, obj2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:47:0x00fa  */
    /* JADX WARN: Code duplicated, block: B:56:0x0111  */
    /* JADX WARN: Code duplicated, block: B:58:0x0116  */
    /* JADX WARN: Code duplicated, block: B:59:0x011e  */
    /* JADX WARN: Code duplicated, block: B:62:0x0123 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:66:0x014e  */
    /* JADX WARN: Code duplicated, block: B:69:0x0181  */
    void updateStateInternal() {
        Call.State state;
        int preciseDisconnectCause;
        int i;
        ImsReasonInfo imsReasonInfo;
        if (this.mOriginalConnection == null) {
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) getPhone().getContext().getSystemService("phone");
        if (isNetworkIdentifiedEmergencyCall() || telephonyManager.isEmergencyNumber(this.mOriginalConnection.getAddress())) {
            this.mTreatAsEmergencyCall = true;
        }
        if (this.mIsStateOverridden && this.mOriginalConnectionState == this.mOriginalConnection.getState()) {
            state = this.mConnectionOverriddenState;
        } else {
            state = this.mOriginalConnection.getState();
        }
        int disconnectCause = this.mOriginalConnection.getDisconnectCause();
        Log.v(this, "Update state from %s to %s for %s", this.mConnectionState, state, getTelecomCallId());
        if (this.mConnectionState != state) {
            this.mIsDomainReselectionInProgress = false;
            this.mConnectionState = state;
            switch (AnonymousClass6.$SwitchMap$com$android$internal$telephony$Call$State[state.ordinal()]) {
                case 2:
                    setActiveInternal();
                    break;
                case 3:
                    setTelephonyConnectionOnHold();
                    break;
                case 4:
                case 5:
                    com.android.internal.telephony.Connection connection = this.mOriginalConnection;
                    if (connection != null && connection.isPulledCall()) {
                        setTelephonyConnectionPulling();
                    } else {
                        setTelephonyConnectionDialing();
                    }
                    break;
                case 6:
                case 7:
                    setTelephonyConnectionRinging();
                    break;
                case 8:
                    if (this.mTelephonyConnectionService != null) {
                        if (isImsConnection()) {
                            ImsPhoneConnection imsPhoneConnection = this.mOriginalConnection;
                            imsReasonInfo = imsPhoneConnection.getImsReasonInfo();
                            if (imsReasonInfo != null) {
                                int code = imsReasonInfo.getCode();
                                int extraCode = imsReasonInfo.getExtraCode();
                                if (code == 1514 || (code == 146 && extraCode == 4)) {
                                    EmergencyNumber emergencyNumberInfo = imsPhoneConnection.getEmergencyNumberInfo();
                                    if (emergencyNumberInfo != null) {
                                        this.mEmergencyServiceCategory = Integer.valueOf(emergencyNumberInfo.getEmergencyServiceCategoryBitmask());
                                        this.mEmergencyUrns = emergencyNumberInfo.getEmergencyUrns();
                                    } else {
                                        Log.i(this, "mEmergencyServiceCategory no EmergencyNumber", new Object[0]);
                                    }
                                    if (this.mEmergencyServiceCategory != null) {
                                        Log.i(this, "mEmergencyServiceCategory=" + this.mEmergencyServiceCategory, new Object[0]);
                                    }
                                    if (this.mEmergencyUrns != null) {
                                        Log.i(this, "mEmergencyUrns=" + this.mEmergencyUrns, new Object[0]);
                                    }
                                }
                            }
                        } else {
                            imsReasonInfo = null;
                        }
                        if (this.mTelephonyConnectionService.maybeReselectDomain(this, imsReasonInfo, this.mShowPreciseFailedCause, this.mHangupDisconnectCause)) {
                            clearOriginalConnection();
                            this.mIsDomainReselectionInProgress = true;
                        } else if (!shouldTreatAsEmergencyCall()) {
                            if (this.mShowPreciseFailedCause) {
                                preciseDisconnectCause = this.mOriginalConnection.getPreciseDisconnectCause();
                            } else {
                                preciseDisconnectCause = -1;
                            }
                            i = this.mHangupDisconnectCause;
                            if (i != -1) {
                                Log.i("TelephonyConnection", "setDisconnected: override cause: " + disconnectCause + " -> " + this.mHangupDisconnectCause, new Object[0]);
                                disconnectCause = this.mHangupDisconnectCause;
                            }
                            DisconnectCause telecomDisconnectCause = DisconnectCauseUtil.toTelecomDisconnectCause(disconnectCause, preciseDisconnectCause, this.mOriginalConnection.getVendorDisconnectCause(), getPhone().getPhoneId(), isImsConnection() ? this.mOriginalConnection.getImsReasonInfo() : null, new FlagsAdapterImpl(), shouldTreatAsEmergencyCall());
                            setTelephonyConnectionDisconnected(telecomDisconnectCause);
                            if (getCarrierConfig().getBoolean("auto_unhold_on_remote_disconnect_bool")) {
                                sendUnhold();
                            }
                            close();
                        } else {
                            if (this.mShowPreciseFailedCause) {
                                preciseDisconnectCause = this.mOriginalConnection.getPreciseDisconnectCause();
                            } else {
                                preciseDisconnectCause = -1;
                            }
                            i = this.mHangupDisconnectCause;
                            if (i != -1) {
                                Log.i("TelephonyConnection", "setDisconnected: override cause: " + disconnectCause + " -> " + this.mHangupDisconnectCause, new Object[0]);
                                disconnectCause = this.mHangupDisconnectCause;
                            }
                            DisconnectCause telecomDisconnectCause2 = DisconnectCauseUtil.toTelecomDisconnectCause(disconnectCause, preciseDisconnectCause, this.mOriginalConnection.getVendorDisconnectCause(), getPhone().getPhoneId(), isImsConnection() ? this.mOriginalConnection.getImsReasonInfo() : null, new FlagsAdapterImpl(), shouldTreatAsEmergencyCall());
                            setTelephonyConnectionDisconnected(telecomDisconnectCause2);
                            if (getCarrierConfig().getBoolean("auto_unhold_on_remote_disconnect_bool")) {
                                sendUnhold();
                            }
                            close();
                        }
                    } else if (!shouldTreatAsEmergencyCall() && (disconnectCause == 63 || disconnectCause == 64)) {
                        fireOnOriginalConnectionRetryDial(disconnectCause == 64);
                    } else {
                        if (this.mShowPreciseFailedCause) {
                            preciseDisconnectCause = this.mOriginalConnection.getPreciseDisconnectCause();
                        } else {
                            preciseDisconnectCause = -1;
                        }
                        i = this.mHangupDisconnectCause;
                        if (i != -1 && i != disconnectCause) {
                            Log.i("TelephonyConnection", "setDisconnected: override cause: " + disconnectCause + " -> " + this.mHangupDisconnectCause, new Object[0]);
                            disconnectCause = this.mHangupDisconnectCause;
                        }
                        DisconnectCause telecomDisconnectCause3 = DisconnectCauseUtil.toTelecomDisconnectCause(disconnectCause, preciseDisconnectCause, this.mOriginalConnection.getVendorDisconnectCause(), getPhone().getPhoneId(), isImsConnection() ? this.mOriginalConnection.getImsReasonInfo() : null, new FlagsAdapterImpl(), shouldTreatAsEmergencyCall());
                        setTelephonyConnectionDisconnected(telecomDisconnectCause3);
                        if (getCarrierConfig().getBoolean("auto_unhold_on_remote_disconnect_bool") && telecomDisconnectCause3.getCode() == 3) {
                            sendUnhold();
                        }
                        close();
                    }
                    break;
            }
            Communicator communicator = this.mCommunicator;
            if (communicator != null) {
                communicator.onStateChanged(getTelecomCallId(), getState());
            }
        }
        MiTelephonyServiceStub.get().updateStateForRelayCall(this);
    }

    /* JADX INFO: renamed from: com.android.services.telephony.TelephonyConnection$6, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$Call$State;

        static {
            int[] iArr = new int[Call.State.values().length];
            $SwitchMap$com$android$internal$telephony$Call$State = iArr;
            try {
                iArr[Call.State.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.ACTIVE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.HOLDING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.DIALING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.ALERTING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.INCOMING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.WAITING.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.DISCONNECTED.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$Call$State[Call.State.DISCONNECTING.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    void updateState() {
        if (this.mOriginalConnection == null) {
            return;
        }
        updateStateInternal();
        if (this.mIsDomainReselectionInProgress) {
            Log.d(this, "updateState: ignore state details update for domain reselection", new Object[0]);
        } else {
            updateStateDetails();
        }
    }

    void updateStateDetails() {
        updateStatusHints();
        updateConnectionCapabilities();
        updateConnectionProperties();
        updateAddress();
        updateMultiparty();
        refreshDisableAddCall();
        refreshCodec();
    }

    private void updateMultiparty() {
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection == null) {
            return;
        }
        if (this.mIsMultiParty != connection.isMultiparty()) {
            boolean zIsMultiparty = this.mOriginalConnection.isMultiparty();
            this.mIsMultiParty = zIsMultiparty;
            if (zIsMultiparty) {
                notifyConferenceStarted();
            }
        }
        MiTelephonyServiceStub.get().updateMultiparty(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConferenceMergeFailed() {
        this.mHandler.obtainMessage(6).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMultipartyStateChange(boolean z) {
        Log.i(this, "Update multiparty state to %s", z ? "Y" : "N");
        this.mHandler.obtainMessage(5, Boolean.valueOf(z)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActiveInternal() {
        if (getState() == 4) {
            Log.w(this, "Should not be called if this is already ACTIVE", new Object[0]);
            return;
        }
        if (getTelephonyConnectionService() != null) {
            for (Connection connection : getTelephonyConnectionService().getAllConnections()) {
                if (connection != this && (connection instanceof TelephonyConnection)) {
                    TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
                    if (telephonyConnection.getState() == 4) {
                        telephonyConnection.updateState();
                    }
                }
            }
        }
        setTelephonyConnectionActive();
    }

    public void close() {
        Log.v(this, "close", new Object[0]);
        MiTelephonyServiceStub.get().setSrvccConnectionTimeReset(this, false);
        MiTelephonyServiceStub.get().setLocalDisconnecting(this, false);
        this.mIsDomainReselectionInProgress = false;
        clearOriginalConnection();
        destroy();
        TelephonyConnectionService telephonyConnectionService = this.mTelephonyConnectionService;
        if (telephonyConnectionService != null) {
            removeTelephonyConnectionListener(telephonyConnectionService.getTelephonyConnectionListener());
        }
        notifyDestroyed();
    }

    private boolean isVideoCapable() {
        int i = this.mOriginalConnectionCapabilities;
        return (i & 4) == 4 && (i & 8) == 8;
    }

    private boolean isExternalConnection() {
        return (this.mOriginalConnectionCapabilities & 16) == 16;
    }

    private boolean isRtt() {
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection == null || connection.getPhoneType() != 5) {
            return false;
        }
        ImsPhoneConnection imsPhoneConnection = this.mOriginalConnection;
        return (imsPhoneConnection instanceof ImsPhoneConnection) && imsPhoneConnection.isRttEnabledForCall();
    }

    protected boolean isCrossSimCall() {
        com.android.internal.telephony.Connection connection = this.mOriginalConnection;
        if (connection == null || connection.getPhoneType() != 5) {
            return false;
        }
        ImsPhoneConnection imsPhoneConnection = this.mOriginalConnection;
        return (imsPhoneConnection instanceof ImsPhoneConnection) && imsPhoneConnection.isCrossSimCall();
    }

    private boolean isPullable() {
        int i = this.mOriginalConnectionCapabilities;
        return (i & 16) == 16 && (i & 32) == 32;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCdmaVoicePrivacy(boolean z) {
        if (this.mIsCdmaVoicePrivacyEnabled != z) {
            this.mIsCdmaVoicePrivacyEnabled = z;
            updateConnectionProperties();
        }
    }

    private int applyConferenceTerminationCapabilities(int i) {
        return !isImsConnection() ? i | 12288 : i;
    }

    public void setOriginalConnectionCapabilities(int i) {
        this.mOriginalConnectionCapabilities = i;
        updateConnectionCapabilities();
        updateConnectionProperties();
    }

    public int applyOriginalConnectionCapabilities(int i) {
        return changeBitmask(changeBitmask(changeBitmask(changeBitmask(i, 8388608, !((this.mOriginalConnectionCapabilities & 3) == 3)), 3072, (this.mOriginalConnectionCapabilities & 8) == 8), 768, (this.mOriginalConnectionCapabilities & 4) == 4 && !this.mIsTtyEnabled), 536870912, (this.mOriginalConnectionCapabilities & 64) == 64);
    }

    boolean isWifi() {
        return getCallRadioTech() == 18;
    }

    public void setIsNetworkIdentifiedEmergencyCall(boolean z) {
        Log.d(this, "setIsNetworkIdentifiedEmergencyCall; callId=%s, isNetworkIdentifiedEmergencyCall=%b", getTelecomCallId(), Boolean.valueOf(z));
        if (MiTelephonyServiceStub.get().disableNetworkIdentifiedEmergencyCall(this)) {
            Log.d(this, "setIsNetworkIdentifiedEmergencyCall disabled by miui", new Object[0]);
        } else {
            this.mIsNetworkIdentifiedEmergencyCall = z;
            updateConnectionProperties();
        }
    }

    public boolean isNetworkIdentifiedEmergencyCall() {
        return this.mIsNetworkIdentifiedEmergencyCall;
    }

    public boolean isOutgoingCall() {
        return getCallDirection() == 1;
    }

    public void setAudioQuality(int i) {
        this.mHasHighDefAudio = i == 2;
        this.mHasHighDefAudio = MiTelephonyServiceStub.get().updateHighDefAudioValue(this, this.mHasHighDefAudio);
        updateConnectionProperties();
    }

    public void setRttTextStream(Connection.RttTextStream rttTextStream) {
        this.mRttTextStream = rttTextStream;
    }

    public Connection.RttTextStream getRttTextStream() {
        return this.mRttTextStream;
    }

    public void setVideoPauseSupported(boolean z) {
        this.mIsVideoPauseSupported = z;
    }

    public boolean getVideoPauseSupported() {
        return this.mIsVideoPauseSupported;
    }

    public void setConferenceSupported(boolean z) {
        this.mIsConferenceSupported = z;
    }

    public boolean isConferenceSupported() {
        return this.mIsConferenceSupported;
    }

    public void setManageImsConferenceCallSupported(boolean z) {
        this.mIsManageImsConferenceCallSupported = z;
    }

    public boolean isManageImsConferenceCallSupported() {
        return this.mIsManageImsConferenceCallSupported;
    }

    public void setShowPreciseFailedCause(boolean z) {
        this.mShowPreciseFailedCause = z;
    }

    public void setTtyEnabled(boolean z) {
        this.mIsTtyEnabled = z;
        updateConnectionCapabilities();
    }

    protected boolean isImsConnection() {
        com.android.internal.telephony.Connection originalConnection = getOriginalConnection();
        return originalConnection != null && originalConnection.getPhoneType() == 5 && (originalConnection instanceof ImsPhoneConnection);
    }

    protected boolean isGsmCdmaConnection() {
        Phone phone = getPhone();
        if (phone == null) {
            return false;
        }
        int phoneType = phone.getPhoneType();
        return phoneType == 1 || phoneType == 2;
    }

    public boolean wasImsConnection() {
        return this.mWasImsConnection;
    }

    void setIsUsingAssistedDialing(Boolean bool) {
        this.mIsUsingAssistedDialing = bool.booleanValue();
        updateConnectionProperties();
    }

    private static Uri getAddressFromNumber(String str) {
        if (str == null) {
            str = "";
        }
        return Uri.fromParts(GenericURI.TEL, str, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatusHints() {
        StatusHints statusHints;
        if (MiTelephonyServiceStub.get().updateStatusHints(this)) {
            return;
        }
        if (isWifi() && !isCrossSimCall() && getPhone() != null) {
            if (this.mIncomingWifiCallStatusHints == null) {
                Context context = getPhone().getContext();
                this.mIncomingWifiCallStatusHints = new StatusHints(getResourceString(R.string.status_hint_label_incoming_wifi_call), Icon.createWithResource(context, R.drawable.ic_signal_wifi_4_bar_24dp), null);
                this.mOutgoingWifiCallStatusHints = new StatusHints(getResourceString(R.string.status_hint_label_wifi_call), Icon.createWithResource(context, R.drawable.ic_signal_wifi_4_bar_24dp), null);
            }
            if (isValidRingingCall()) {
                statusHints = this.mIncomingWifiCallStatusHints;
            } else {
                statusHints = this.mOutgoingWifiCallStatusHints;
            }
            if (statusHints == this.mStatusHints) {
                return;
            }
            setTelephonyStatusHints(statusHints);
            this.mStatusHints = statusHints;
            return;
        }
        if (this.mStatusHints == null) {
            return;
        }
        setTelephonyStatusHints(null);
        this.mStatusHints = null;
    }

    public final TelephonyConnection addTelephonyConnectionListener(TelephonyConnectionListener telephonyConnectionListener) {
        this.mTelephonyListeners.add(telephonyConnectionListener);
        if (this.mOriginalConnection != null) {
            fireOnOriginalConnectionConfigured();
        }
        return this;
    }

    public final TelephonyConnection removeTelephonyConnectionListener(TelephonyConnectionListener telephonyConnectionListener) {
        if (telephonyConnectionListener != null) {
            this.mTelephonyListeners.remove(telephonyConnectionListener);
        }
        return this;
    }

    @Override // com.android.services.telephony.Holdable
    public void setHoldable(boolean z) {
        this.mIsHoldable = z;
        updateConnectionCapabilities();
    }

    @Override // com.android.services.telephony.Holdable
    public boolean isChildHoldable() {
        return getConference() != null;
    }

    public boolean isHoldable() {
        return this.mIsHoldable;
    }

    private final void fireOnOriginalConnectionConfigured() {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onOriginalConnectionConfigured(this);
        }
    }

    private final void fireOnOriginalConnectionRetryDial(boolean z) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onOriginalConnectionRetry(this, z);
        }
    }

    protected void handleExitedEcmMode() {
        updateConnectionProperties();
        updateConnectionCapabilities();
    }

    void refreshConferenceSupported() {
        PhoneAccountHandle phoneAccountHandleMakePstnPhoneAccountHandle;
        PersistableBundle configForSubId;
        boolean zIsVideo = VideoProfile.isVideo(getVideoState());
        Phone phone = getPhone();
        boolean z = false;
        if (phone == null) {
            Log.w(this, "refreshConferenceSupported = false; phone is null", new Object[0]);
            if (isConferenceSupported()) {
                setConferenceSupported(false);
                notifyConferenceSupportedChanged(false);
                return;
            }
            return;
        }
        boolean z2 = phone.getPhoneType() == 5;
        boolean zIsWfcEnabled = z2 ? isWfcEnabled(phone) : false;
        if (z2) {
            phoneAccountHandleMakePstnPhoneAccountHandle = PhoneUtils.makePstnPhoneAccountHandle(phone.getDefaultPhone());
        } else {
            phoneAccountHandleMakePstnPhoneAccountHandle = PhoneUtils.makePstnPhoneAccountHandle(phone);
        }
        TelecomAccountRegistry telecomAccountRegistry = getTelecomAccountRegistry(phone.getContext());
        boolean zIsMergeCallSupported = telecomAccountRegistry.isMergeCallSupported(phoneAccountHandleMakePstnPhoneAccountHandle);
        boolean zIsMergeImsCallSupported = telecomAccountRegistry.isMergeImsCallSupported(phoneAccountHandleMakePstnPhoneAccountHandle);
        this.mIsCarrierVideoConferencingSupported = telecomAccountRegistry.isVideoConferencingSupported(phoneAccountHandleMakePstnPhoneAccountHandle);
        boolean zIsMergeOfWifiCallsAllowedWhenVoWifiOff = telecomAccountRegistry.isMergeOfWifiCallsAllowedWhenVoWifiOff(phoneAccountHandleMakePstnPhoneAccountHandle);
        ImsCall imsCall = isImsConnection() ? getOriginalConnection().getImsCall() : null;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        boolean z3 = (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId())) == null) ? false : configForSubId.getBoolean("treat_downgraded_video_calls_as_video_calls_bool");
        Log.v(this, "refreshConferenceSupported : isConfSupp=%b, isImsConfSupp=%b, isVidConfSupp=%b, isMergeOfWifiAllowed=%b, isWifi=%b, isVoWifiEnabled=%b", Boolean.valueOf(zIsMergeCallSupported), Boolean.valueOf(zIsMergeImsCallSupported), Boolean.valueOf(this.mIsCarrierVideoConferencingSupported), Boolean.valueOf(zIsMergeOfWifiCallsAllowedWhenVoWifiOff), Boolean.valueOf(isWifi()), Boolean.valueOf(zIsWfcEnabled));
        if (this.mTreatAsEmergencyCall) {
            Log.d(this, "refreshConferenceSupported = false; emergency call", new Object[0]);
        } else if (isRtt() && !isRttMergeSupported(getCarrierConfig())) {
            Log.d(this, "refreshConferenceSupported = false; rtt call", new Object[0]);
        } else if (!zIsMergeCallSupported || (z2 && !zIsMergeImsCallSupported)) {
            Log.d(this, "refreshConferenceSupported = false; carrier doesn't support conf.", new Object[0]);
        } else if (zIsVideo && !this.mIsCarrierVideoConferencingSupported && !SystemProperties.get("persist.vendor.vt.video_conference_support", "0").equals("1")) {
            Log.d(this, "refreshConferenceSupported = false; video conf not supported.", new Object[0]);
        } else if (imsCall != null && imsCall.wasVideoCall() && z3 && !this.mIsCarrierVideoConferencingSupported) {
            Log.d(this, "refreshConferenceSupported = false; video conf not supported for downgraded audio call.", new Object[0]);
        } else if (!zIsMergeOfWifiCallsAllowedWhenVoWifiOff && isWifi() && !zIsWfcEnabled) {
            Log.d(this, "refreshConferenceSupported = false; can't merge wifi calls when voWifi off.", new Object[0]);
        } else {
            Log.d(this, "refreshConferenceSupported = true.", new Object[0]);
            z = true;
        }
        if (z != isConferenceSupported()) {
            setConferenceSupported(z);
            notifyConferenceSupportedChanged(z);
        }
    }

    boolean isWfcEnabled(Phone phone) {
        return ImsUtil.isWfcEnabled(phone.getContext(), phone.getPhoneId());
    }

    private static Map createExtrasMap() {
        HashMap map = new HashMap();
        map.put("ChildNum", "android.telecom.extra.CHILD_ADDRESS");
        map.put("DisplayText", "android.telecom.extra.CALL_SUBJECT");
        map.put("android.telephony.ims.extra.ADDITIONAL_SIP_INVITE_FIELDS", "android.telecom.extra.SIP_INVITE");
        map.put("mediatek:wasVideoCall", "mediatek.telecom.extra.WAS_VIDEO_CALL");
        return Collections.unmodifiableMap(map);
    }

    private boolean isShowingOriginalDialString() {
        getPhone();
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[TelephonyConnection objId:");
        sb.append(System.identityHashCode(this));
        sb.append(" telecomCallID:");
        sb.append(getTelecomCallId());
        sb.append(" type:");
        if (isImsConnection()) {
            sb.append("ims");
        } else if (this instanceof GsmConnection) {
            sb.append("gsm");
        }
        sb.append(" state:");
        sb.append(Connection.stateToString(getState()));
        sb.append(" capabilities:");
        sb.append(MtkConnection.capabilitiesToString(getConnectionCapabilities()));
        sb.append(" properties:");
        sb.append(MtkConnection.propertiesToString(getConnectionProperties()));
        sb.append(" address:");
        sb.append(Rlog.pii("TelephonyConnection", getAddress()));
        sb.append(" originalConnection:");
        sb.append(this.mOriginalConnection);
        sb.append(" partOfConf:");
        if (getConference() == null) {
            sb.append("N");
        } else {
            sb.append("Y");
        }
        sb.append(" confSupported:");
        sb.append(this.mIsConferenceSupported ? "Y" : "N");
        sb.append(" isAdhocConf:");
        sb.append(isAdhocConferenceCall() ? "Y" : "N");
        sb.append("]");
        return sb.toString();
    }

    public final void setTelephonyConnectionService(TelephonyConnectionService telephonyConnectionService) {
        this.mTelephonyConnectionService = telephonyConnectionService;
    }

    public final TelephonyConnectionService getTelephonyConnectionService() {
        return this.mTelephonyConnectionService;
    }

    public void setTelephonyConnectionActive() {
        setActive();
        notifyStateChanged(getState());
    }

    public void setTelephonyConnectionRinging() {
        setRinging();
        notifyStateChanged(getState());
    }

    public void setTelephonyConnectionInitializing() {
        setInitializing();
        notifyStateChanged(getState());
    }

    public void setTelephonyConnectionDialing() {
        setDialing();
        notifyStateChanged(getState());
    }

    public void setTelephonyConnectionPulling() {
        setPulling();
        notifyStateChanged(getState());
    }

    public void setTelephonyConnectionOnHold() {
        setOnHold();
        notifyStateChanged(getState());
    }

    public void setTelephonyConnectionDisconnected(DisconnectCause disconnectCause) {
        PersistableBundle carrierConfig;
        if (disconnectCause.getCode() == 11 && (carrierConfig = getCarrierConfig()) != null && !carrierConfig.getBoolean("log_calls_answered_elsewhere_bool", true)) {
            Log.i(this, "setTelephonyConnectionDisconnected: skip logging call answered elsewhere based on carrier requirements.", new Object[0]);
            Bundle bundle = new Bundle();
            bundle.putBoolean("android.telecom.extra.DO_NOT_LOG_CALL", true);
            putTelephonyExtras(bundle);
        }
        this.mTelephonyDisconnectCause = disconnectCause;
        setDisconnected(disconnectCause);
        notifyDisconnected(disconnectCause);
        notifyStateChanged(getState());
    }

    public void sendTelephonyConnectionEvent(String str, Bundle bundle) {
        sendConnectionEvent(str, bundle);
        notifyTelephonyConnectionEvent(str, bundle);
    }

    public void putTelephonyExtras(Bundle bundle) {
        putExtras(bundle);
        notifyPutExtras(bundle);
    }

    public void removeTelephonyExtras(List list) {
        removeExtras((List<String>) list);
        notifyRemoveExtras(list);
    }

    public void setTelephonyVideoState(int i) {
        setVideoState(i);
        notifyVideoStateChanged(i);
    }

    public void setTelephonyVideoProvider(Connection.VideoProvider videoProvider) {
        setVideoProvider(videoProvider);
        notifyVideoProviderChanged(videoProvider);
    }

    public void setTelephonyStatusHints(StatusHints statusHints) {
        setStatusHints(statusHints);
        notifyStatusHintsChanged(statusHints);
    }

    public final void setCallRadioTech(int i) {
        Bundle extras = getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putInt("android.telecom.extra.CALL_NETWORK_TYPE", ServiceState.rilRadioTechnologyToNetworkType(i));
        putExtras(extras);
        if (getConference() != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("android.telecom.extra.CALL_NETWORK_TYPE", ServiceState.rilRadioTechnologyToNetworkType(i));
            getConference().putExtras(bundle);
        }
    }

    public final int getCallRadioTech() {
        Bundle extras = getExtras();
        return ServiceState.networkTypeToRilRadioTechnology(extras != null ? extras.getInt("android.telecom.extra.CALL_NETWORK_TYPE", 0) : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConferenceParticipants(List list) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onConferenceParticipantsChanged(this, list);
        }
    }

    private void maybeConfigureDeviceToDeviceCommunication() {
        if (!getPhone().getContext().getResources().getBoolean(R.bool.config_use_device_to_device_communication)) {
            Log.i(this, "maybeConfigureDeviceToDeviceCommunication: not using D2D.", new Object[0]);
            return;
        }
        if (!isImsConnection()) {
            Log.i(this, "maybeConfigureDeviceToDeviceCommunication: not an IMS connection.", new Object[0]);
            if (this.mCommunicator != null) {
                this.mCommunicator = null;
                return;
            }
            return;
        }
        if (this.mTreatAsEmergencyCall || this.mIsNetworkIdentifiedEmergencyCall) {
            Log.i(this, "maybeConfigureDeviceToDeviceCommunication: emergency call; no D2D", new Object[0]);
            return;
        }
        ArrayList arrayList = new ArrayList(2);
        if (supportsD2DUsingRtp()) {
            Log.i(this, "maybeConfigureDeviceToDeviceCommunication: carrier supports RTP.", new Object[0]);
            RtpTransport rtpTransport = new RtpTransport(new AnonymousClass5(), (Timeouts.Adapter) null, this.mHandler, supportsSdpNegotiationOfRtpHeaderExtensions());
            this.mRtpTransport = rtpTransport;
            arrayList.add(rtpTransport);
        }
        if (supportsD2DUsingDtmf()) {
            Log.i(this, "maybeConfigureDeviceToDeviceCommunication: carrier supports DTMF.", new Object[0]);
            DtmfTransport dtmfTransport = new DtmfTransport(new DtmfAdapter() { // from class: com.android.services.telephony.TelephonyConnection$$ExternalSyntheticLambda0
                public final void sendDtmf(char c) {
                    this.f$0.lambda$maybeConfigureDeviceToDeviceCommunication$2(c);
                }
            }, new Timeouts.Adapter(getPhone().getContext().getContentResolver()), Executors.newSingleThreadScheduledExecutor());
            this.mDtmfTransport = dtmfTransport;
            arrayList.add(dtmfTransport);
        }
        if (arrayList.size() > 0) {
            Communicator communicator = new Communicator(arrayList, this);
            this.mCommunicator = communicator;
            D2DCallStateAdapter d2DCallStateAdapter = new D2DCallStateAdapter(communicator);
            this.mD2DCallStateAdapter = d2DCallStateAdapter;
            addTelephonyConnectionListener(d2DCallStateAdapter);
            return;
        }
        Log.i(this, "maybeConfigureDeviceToDeviceCommunication: no transports; disabled.", new Object[0]);
    }

    /* JADX INFO: renamed from: com.android.services.telephony.TelephonyConnection$5, reason: invalid class name */
    class AnonymousClass5 implements RtpAdapter {
        AnonymousClass5() {
        }

        public Set getAcceptedRtpHeaderExtensions() {
            return TelephonyConnection.this.mOriginalConnection.getAcceptedRtpHeaderExtensions();
        }

        public void sendRtpHeaderExtensions(Set set) {
            Log.i(TelephonyConnection.this, "sendRtpHeaderExtensions: sending: %s", set.stream().map(new Function() { // from class: com.android.services.telephony.TelephonyConnection$5$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((RtpHeaderExtension) obj).toString();
                }
            }).collect(Collectors.joining(",")));
            TelephonyConnection.this.mOriginalConnection.sendRtpHeaderExtensions(set);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeConfigureDeviceToDeviceCommunication$2(char c) {
        Log.i(this, "sendDtmf: send digit %c", Character.valueOf(c));
        ImsPhoneConnection imsPhoneConnection = this.mOriginalConnection;
        Message messageObtainMessage = this.mHandler.obtainMessage(22);
        messageObtainMessage.replyTo = this.mHandlerMessenger;
        imsPhoneConnection.getImsCall().sendDtmf(c, messageObtainMessage);
    }

    public Communicator getCommunicator() {
        return this.mCommunicator;
    }

    public void onMessagesReceived(Set set) {
        Integer num;
        Log.i(this, "onMessagesReceived: got d2d messages: %s", set);
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Communicator.Message message = (Communicator.Message) it.next();
            Integer num2 = (Integer) MessageTypeAndValueHelper.MSG_TYPE_TO_DC_MSG_TYPE.getValue(Integer.valueOf(message.getType()));
            if (num2 != null) {
                int type = message.getType();
                if (type == 1) {
                    num = (Integer) MessageTypeAndValueHelper.RAT_TYPE_TO_DC_NETWORK_TYPE.getValue(Integer.valueOf(message.getValue()));
                } else if (type == 2) {
                    num = (Integer) MessageTypeAndValueHelper.CODEC_TO_DC_CODEC.getValue(Integer.valueOf(message.getValue()));
                } else if (type == 3) {
                    num = (Integer) MessageTypeAndValueHelper.BATTERY_STATE_TO_DC_BATTERY_STATE.getValue(Integer.valueOf(message.getValue()));
                } else if (type == 4) {
                    num = (Integer) MessageTypeAndValueHelper.COVERAGE_TO_DC_COVERAGE.getValue(Integer.valueOf(message.getValue()));
                } else {
                    Log.w(this, "onMessagesReceived: msg=%d - invalid msg", Integer.valueOf(message.getValue()));
                }
                if (num == null) {
                    Log.w(this, "onMessagesReceived: msg=%d/%d - invalid msg value", Integer.valueOf(message.getType()), Integer.valueOf(message.getValue()));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("android.telecom.extra.DEVICE_TO_DEVICE_MESSAGE_TYPE", num2.intValue());
                    bundle.putInt("android.telecom.extra.DEVICE_TO_DEVICE_MESSAGE_VALUE", num.intValue());
                    sendConnectionEvent("android.telecom.event.DEVICE_TO_DEVICE_MESSAGE", bundle);
                }
            }
        }
    }

    protected void notifyConferenceStarted() {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onConferenceStarted();
        }
    }

    private void notifyConferenceSupportedChanged(boolean z) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onConferenceSupportedChanged(this, z);
        }
    }

    private void notifyConnectionCapabilitiesChanged(int i) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onConnectionCapabilitiesChanged(this, i);
        }
    }

    private void notifyConnectionPropertiesChanged(int i) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onConnectionPropertiesChanged(this, i);
        }
    }

    private void notifyDestroyed() {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onDestroyed(this);
        }
    }

    private void notifyDisconnected(DisconnectCause disconnectCause) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onDisconnected(this, disconnectCause);
        }
    }

    private void notifyStateChanged(int i) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onStateChanged(this, i);
        }
    }

    private void notifyTelephonyConnectionEvent(String str, Bundle bundle) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onConnectionEvent(this, str, bundle);
        }
    }

    private void notifyPutExtras(Bundle bundle) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onExtrasChanged(this, bundle);
        }
    }

    private void notifyRemoveExtras(List list) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onExtrasRemoved(this, list);
        }
    }

    private void notifyVideoStateChanged(int i) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onVideoStateChanged(this, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyRingbackRequested(boolean z) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onRingbackRequested(this, z);
        }
    }

    private void notifyVideoProviderChanged(Connection.VideoProvider videoProvider) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onVideoProviderChanged(this, videoProvider);
        }
    }

    private void notifyStatusHintsChanged(StatusHints statusHints) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onStatusHintsChanged(this, statusHints);
        }
    }

    private boolean isNeededToFormatIncomingNumberForJp() {
        if (this.mOriginalConnection.isIncoming() && !TextUtils.isEmpty(this.mOriginalConnection.getAddress()) && this.mOriginalConnection.getAddress().startsWith("+81")) {
            return getCarrierConfig().getBoolean("format_incoming_number_to_national_for_jp_bool");
        }
        return false;
    }

    private String formatIncomingNumberForJp(String str) {
        return PhoneNumberUtils.stripSeparators(PhoneNumberUtils.formatNumber(str, "JP"));
    }

    public TelecomAccountRegistry getTelecomAccountRegistry(Context context) {
        return TelecomAccountRegistry.getInstance(context);
    }

    private boolean supportsD2DUsingRtp() {
        return getCarrierConfig().getBoolean("supports_device_to_device_communication_using_rtp_bool");
    }

    private boolean supportsD2DUsingDtmf() {
        return getCarrierConfig().getBoolean("supports_device_to_device_communication_using_dtmf_bool");
    }

    private boolean supportsSdpNegotiationOfRtpHeaderExtensions() {
        return getCarrierConfig().getBoolean("supports_sdp_negotiation_of_d2d_rtp_header_extensions_bool");
    }

    private void handleOutgoingDeviceToDeviceMessage(Bundle bundle) {
        Integer num;
        int i = bundle.getInt("android.telecom.extra.DEVICE_TO_DEVICE_MESSAGE_TYPE");
        int i2 = bundle.getInt("android.telecom.extra.DEVICE_TO_DEVICE_MESSAGE_VALUE");
        if (i == 1) {
            num = (Integer) MessageTypeAndValueHelper.RAT_TYPE_TO_DC_NETWORK_TYPE.getKey(Integer.valueOf(i2));
        } else if (i == 2) {
            num = (Integer) MessageTypeAndValueHelper.CODEC_TO_DC_CODEC.getKey(Integer.valueOf(i2));
        } else if (i == 3) {
            num = (Integer) MessageTypeAndValueHelper.BATTERY_STATE_TO_DC_BATTERY_STATE.getKey(Integer.valueOf(i2));
        } else if (i == 4) {
            num = (Integer) MessageTypeAndValueHelper.COVERAGE_TO_DC_COVERAGE.getKey(Integer.valueOf(i2));
        } else {
            Log.w(this, "handleOutgoingDeviceToDeviceMessage: msg=%d - invalid msg", Integer.valueOf(i));
            return;
        }
        Integer num2 = (Integer) MessageTypeAndValueHelper.MSG_TYPE_TO_DC_MSG_TYPE.getKey(Integer.valueOf(i));
        if (num == null) {
            Log.w(this, "handleOutgoingDeviceToDeviceMessage: msg=%d/%d - invalid value", Integer.valueOf(i), Integer.valueOf(i2));
        } else if (this.mCommunicator != null) {
            Log.w(this, "handleOutgoingDeviceToDeviceMessage: msg=%d/%d - sending", num2, num);
            ArraySet arraySet = new ArraySet();
            arraySet.add(new Communicator.Message(num2.intValue(), num.intValue()));
            this.mCommunicator.sendMessages(arraySet);
        }
    }

    public List getTelephonyConnectionListeners() {
        return new ArrayList(this.mTelephonyListeners);
    }

    public Integer getEmergencyServiceCategory() {
        return this.mEmergencyServiceCategory;
    }

    public void setEmergencyServiceCategory(int i) {
        this.mEmergencyServiceCategory = Integer.valueOf(i);
    }

    public List getEmergencyUrns() {
        return this.mEmergencyUrns;
    }

    public void setEmergencyUrns(List list) {
        this.mEmergencyUrns = list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRadioNotAvailable() {
        Log.d(this, "handleRadioNotAvailable", new Object[0]);
        setTelephonyConnectionDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(3, "Local Disconnect due to radio off."));
        close();
    }

    private void trySrvccPendingAction() {
        Log.d(this, "trySrvccPendingAction(): " + this.mPendingAction, new Object[0]);
        int iOrdinal = this.mPendingAction.ordinal();
        if (iOrdinal == 1) {
            onAnswer(0);
        } else if (iOrdinal == 2) {
            performHold();
        } else if (iOrdinal == 3) {
            performUnhold();
        } else if (iOrdinal == 4) {
            hangup(3);
        }
        this.mPendingAction = SrvccPendingAction.SRVCC_PENDING_NONE;
    }

    private int updatePropertyVoLte(int i) {
        int iChangeBitmask = changeBitmask(i, 32768, isImsConnection());
        Log.d(this, "updatePropertyVoLte: " + MtkConnection.propertiesToString(iChangeBitmask), new Object[0]);
        return iChangeBitmask;
    }

    private void removePropertyVoLte() {
        int iChangeBitmask = changeBitmask(getConnectionProperties(), 32768, false);
        Log.d(this, "removePropertyVoLte: %s", MtkConnection.propertiesToString(iChangeBitmask));
        if (getConnectionProperties() != iChangeBitmask) {
            setConnectionProperties(iChangeBitmask);
        }
    }

    protected void notifyConferenceParticipantsInvited(boolean z) {
        Iterator it = this.mTelephonyListeners.iterator();
        while (it.hasNext()) {
            ((TelephonyConnectionListener) it.next()).onConferenceParticipantsInvited(z);
        }
    }

    private void registerSuppMessageManagerForImsPhone(Phone phone, com.android.internal.telephony.Connection connection) {
        Log.d(this, "registerSuppMessageManagerForImsPhone: " + phone, new Object[0]);
        if (!(phone instanceof MtkImsPhone)) {
            return;
        }
        MtkTelephonyConnectionServiceUtil.getInstance().registerSuppMessageForImsPhone(phone, connection);
    }

    private void unregisterSuppMessageManagerForImsPhone(Phone phone, com.android.internal.telephony.Connection connection) {
        Log.d(this, "unregisterSuppMessageManagerForImsPhone: " + phone, new Object[0]);
        if (!(phone instanceof MtkImsPhone)) {
            return;
        }
        MtkTelephonyConnectionServiceUtil.getInstance().unregisterSuppMessageForImsPhone(phone, connection);
    }

    public void notifyEcc() {
        sendTelephonyConnectionEvent("mediatek.telecom.event.EVENT_VOLTE_MARKED_AS_EMERGENCY", null);
    }

    public DisconnectCause getTelephonyDisconnectCause() {
        return this.mTelephonyDisconnectCause;
    }

    public void setPhone(Phone phone) {
        this.mPhone = phone;
        if (phone != null) {
            Log.d(this, "setPhone " + this.mPhone.getPhoneId(), new Object[0]);
            if (this.mPhone.mCi.getRadioState() == 1) {
                this.mPhone.mCi.registerForNotAvailable(this.mHandler, 1000, (Object) null);
            } else {
                this.mDelayRegisterRadioState = true;
            }
        }
    }

    private void handleHangupAll() {
        try {
            for (MtkGsmCdmaPhone mtkGsmCdmaPhone : PhoneFactory.getPhones()) {
                MtkImsPhone imsPhone = mtkGsmCdmaPhone.getImsPhone();
                if (imsPhone != null && imsPhone.getState() != PhoneConstants.State.IDLE) {
                    if (imsPhone instanceof MtkImsPhone) {
                        Log.d(this, "Hangup all calls for MtkImsPhone %d", Integer.valueOf(imsPhone.getPhoneId()));
                        imsPhone.hangupAll();
                    }
                } else if (mtkGsmCdmaPhone.getState() != PhoneConstants.State.IDLE && (mtkGsmCdmaPhone instanceof MtkGsmCdmaPhone)) {
                    Log.d(this, "Hangup all calls for MtkGsmCdmaPhone %d", Integer.valueOf(mtkGsmCdmaPhone.getPhoneId()));
                    mtkGsmCdmaPhone.hangupAll();
                }
            }
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "HangupAll exception", new Object[0]);
        }
    }
}
