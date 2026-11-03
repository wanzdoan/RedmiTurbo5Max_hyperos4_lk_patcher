package com.android.services.telephony;

import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.ims.ImsCallProfile;
import android.text.TextUtils;
import com.android.ims.ImsCall;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.LocaleTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.imsphone.ImsExternalConnection;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.phone.NumberVerificationManager;
import com.android.phone.PhoneUtils;
import com.android.phone.callcomposer.CallComposerPictureManager;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.MtkGsmCdmaConnection;
import com.mediatek.internal.telephony.imsphone.MtkImsPhoneConnection;
import gov.nist.javax.sip.address.GenericURI;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public final class PstnIncomingCallNotifier {
    private final Handler mHandler;
    private final Phone mPhone;

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCdmaCallWaiting(AsyncResult asyncResult) {
    }

    public PstnIncomingCallNotifier(Phone phone) {
        phone.getClass();
        this.mPhone = phone;
        this.mHandler = new Handler(phone.getLooper()) { // from class: com.android.services.telephony.PstnIncomingCallNotifier.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 100:
                        PstnIncomingCallNotifier.this.handleNewRingingConnection((AsyncResult) message.obj);
                        break;
                    case 101:
                        PstnIncomingCallNotifier.this.handleCdmaCallWaiting((AsyncResult) message.obj);
                        break;
                    case 102:
                        PstnIncomingCallNotifier.this.handleNewUnknownConnection((AsyncResult) message.obj);
                        break;
                }
            }

            @Override // android.os.Handler
            public String toString() {
                return String.format("[PstnIncomingCallNotifierHandler; phoneId=[%s]", PstnIncomingCallNotifier.this.getPhoneIdAsString());
            }
        };
        registerForNotifications();
    }

    public void teardown() {
        unregisterForNotifications();
    }

    private void registerForNotifications() {
        if (this.mPhone != null) {
            Log.i(this, "Registering: [%s]", getPhoneIdAsString());
            this.mPhone.registerForNewRingingConnection(this.mHandler, 100, (Object) null);
            this.mPhone.registerForUnknownConnection(this.mHandler, 102, (Object) null);
        }
    }

    private void unregisterForNotifications() {
        if (this.mPhone != null) {
            Log.i(this, "Unregistering: [%s]", getPhoneIdAsString());
            this.mPhone.unregisterForNewRingingConnection(this.mHandler);
            this.mPhone.unregisterForUnknownConnection(this.mHandler);
        }
    }

    private String getNetworkCountryIso() {
        LocaleTracker localeTracker;
        ServiceStateTracker serviceStateTracker = this.mPhone.getServiceStateTracker();
        if (serviceStateTracker == null || (localeTracker = serviceStateTracker.getLocaleTracker()) == null) {
            return null;
        }
        return localeTracker.getCurrentCountry();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNewRingingConnection(AsyncResult asyncResult) {
        Log.i(this, "handleNewRingingConnection: phoneId=[%s]", getPhoneIdAsString());
        final Connection connection = (Connection) asyncResult.result;
        if (connection != null) {
            Call call = connection.getCall();
            if (connection.getAddress() != null && NumberVerificationManager.getInstance().checkIncomingCall(connection.getAddress(), getNetworkCountryIso())) {
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.services.telephony.PstnIncomingCallNotifier$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleNewRingingConnection$0(connection);
                    }
                }, 10000L);
            } else {
                if (call == null || !call.getState().isRinging()) {
                    return;
                }
                sendIncomingCallIntent(connection);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleNewRingingConnection$0(Connection connection) {
        try {
            connection.hangup();
        } catch (CallStateException unused) {
            Log.i(this, "Remote end hung up call verification call", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNewUnknownConnection(AsyncResult asyncResult) {
        Log.i(this, "handleNewUnknownConnection: phoneId=[%s]", getPhoneIdAsString());
        Object obj = asyncResult.result;
        if (!(obj instanceof Connection)) {
            Log.i(this, "handleNewUnknownConnection called with non-Connection object", new Object[0]);
            return;
        }
        Connection connection = (Connection) obj;
        if (connection != null) {
            Call.State state = connection.getState();
            if (state == Call.State.DISCONNECTED || state == Call.State.IDLE) {
                Log.i(this, "Skipping new unknown connection because it is idle. " + connection, new Object[0]);
                return;
            }
            Call call = connection.getCall();
            if (call != null && call.getState().isAlive()) {
                addNewUnknownCall(connection);
                return;
            }
            Log.i(this, "Skipping new unknown connection because its call is null or dead. connection=" + connection, new Object[0]);
        }
    }

    private void addNewUnknownCall(Connection connection) {
        Log.i(this, "addNewUnknownCall, connection is: %s", connection);
        if (!maybeSwapAnyWithUnknownConnection(connection)) {
            Log.i(this, "determined new connection is: %s", connection);
            Bundle bundle = new Bundle();
            if (connection.getNumberPresentation() == 1 && !TextUtils.isEmpty(connection.getAddress())) {
                bundle.putParcelable("android.telecom.extra.UNKNOWN_CALL_HANDLE", Uri.fromParts(GenericURI.TEL, connection.getAddress(), null));
            }
            if (connection instanceof ImsExternalConnection) {
                bundle.putInt("android.telephony.ImsExternalCallTracker.extra.EXTERNAL_CALL_ID", ((ImsExternalConnection) connection).getCallId());
            }
            bundle.putLong("android.telecom.extra.CALL_CREATED_TIME_MILLIS", SystemClock.elapsedRealtime());
            PhoneAccountHandle phoneAccountHandleFindCorrectPhoneAccountHandle = findCorrectPhoneAccountHandle();
            if (phoneAccountHandleFindCorrectPhoneAccountHandle == null) {
                try {
                    connection.hangup();
                    return;
                } catch (CallStateException unused) {
                    return;
                }
            } else {
                ((TelecomManager) this.mPhone.getContext().getSystemService(TelecomManager.class)).addNewUnknownCall(phoneAccountHandleFindCorrectPhoneAccountHandle, bundle);
                return;
            }
        }
        Log.i(this, "swapped an old connection, new one is: %s", connection);
    }

    private void sendIncomingCallIntent(Connection connection) {
        ImsCallProfile callProfile;
        Bundle bundle = new Bundle();
        if (connection.getNumberPresentation() == 1 && !TextUtils.isEmpty(connection.getAddress())) {
            bundle.putParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS", Uri.fromParts(GenericURI.TEL, connection.getAddress(), null));
        }
        bundle.putLong("android.telecom.extra.CALL_CREATED_TIME_MILLIS", SystemClock.elapsedRealtime());
        if (connection instanceof MtkGsmCdmaConnection) {
            boolean zIsIncomingCallGwsd = ((MtkGsmCdmaConnection) connection).isIncomingCallGwsd();
            Log.d(this, "isIncomingGwsd: " + zIsIncomingCallGwsd, new Object[0]);
            bundle.putBoolean("mediatek.telecom.extra.EXTRA_INCOMING_GWSD", zIsIncomingCallGwsd);
        }
        if (connection instanceof MtkImsPhoneConnection) {
            boolean zIsIncomingCallGwsd2 = ((MtkImsPhoneConnection) connection).isIncomingCallGwsd();
            Log.d(this, "isIncomingGwsd: " + zIsIncomingCallGwsd2, new Object[0]);
            bundle.putBoolean("mediatek.telecom.extra.EXTRA_INCOMING_GWSD", zIsIncomingCallGwsd2);
        }
        if (connection.getPhoneType() == 5) {
            ImsPhoneConnection imsPhoneConnection = (ImsPhoneConnection) connection;
            bundle.putInt("android.telecom.extra.INCOMING_VIDEO_STATE", imsPhoneConnection.getVideoState());
            if (imsPhoneConnection.isRttEnabledForCall()) {
                bundle.putBoolean("android.telecom.extra.START_CALL_WITH_RTT", true);
            }
            if (imsPhoneConnection.isIncomingCallAutoRejected()) {
                bundle.putString("android.telecom.extra.CALL_DISCONNECT_MESSAGE", "Call dropped by lower layers");
            }
            ImsCall imsCall = imsPhoneConnection.getImsCall();
            if (imsCall != null && (callProfile = imsCall.getCallProfile()) != null) {
                if (CallComposerPictureManager.sTestMode) {
                    callProfile.setCallExtra("android.telephony.ims.extra.PICTURE_URL", CallComposerPictureManager.FAKE_SERVER_URL);
                    callProfile.setCallExtraInt("android.telephony.ims.extra.PRIORITY", 1);
                    callProfile.setCallExtra("android.telephony.ims.extra.CALL_SUBJECT", CallComposerPictureManager.FAKE_SUBJECT);
                    callProfile.setCallExtraParcelable("android.telephony.ims.extra.LOCATION", CallComposerPictureManager.FAKE_LOCATION);
                }
                bundle.putInt("android.telecom.extra.PRIORITY", callProfile.getCallExtraInt("android.telephony.ims.extra.PRIORITY"));
                bundle.putString("android.telecom.extra.CALL_SUBJECT", callProfile.getCallExtra("android.telephony.ims.extra.CALL_SUBJECT"));
                bundle.putParcelable("android.telecom.extra.LOCATION", callProfile.getCallExtraParcelable("android.telephony.ims.extra.LOCATION"));
                if (!TextUtils.isEmpty(callProfile.getCallExtra("android.telephony.ims.extra.PICTURE_URL"))) {
                    bundle.putBoolean("android.telecom.extra.HAS_PICTURE", true);
                }
            }
        }
        PhoneAccountHandle phoneAccountHandleFindCorrectPhoneAccountHandle = findCorrectPhoneAccountHandle();
        if (phoneAccountHandleFindCorrectPhoneAccountHandle == null) {
            try {
                connection.hangup();
            } catch (CallStateException unused) {
            }
            Log.wtf("PstnIncomingCallNotifier", "sendIncomingCallIntent: failed to add new call because no phone account could be found for the call", new Object[0]);
            return;
        }
        TelecomManager telecomManager = (TelecomManager) this.mPhone.getContext().getSystemService(TelecomManager.class);
        try {
            if (connection.isMultiparty()) {
                telecomManager.addNewIncomingConference(phoneAccountHandleFindCorrectPhoneAccountHandle, bundle);
            } else {
                telecomManager.addNewIncomingCall(phoneAccountHandleFindCorrectPhoneAccountHandle, bundle);
            }
        } catch (SecurityException unused2) {
            String str = (String) telecomManager.getCallCapablePhoneAccounts().stream().map(new Function() { // from class: com.android.services.telephony.PstnIncomingCallNotifier$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return PstnIncomingCallNotifier.$r8$lambda$s5X8T7Ocb53GGyyO_gm1BgngQi0((PhoneAccountHandle) obj);
                }
            }).collect(Collectors.joining(","));
            Log.wtf("PstnIncomingCallNotifier", "sendIncomingCallIntent: failed to add new call " + connection + " because the handle " + ("[" + phoneAccountHandleFindCorrectPhoneAccountHandle.getComponentName() + " " + Rlog.pii("PstnIncomingCallNotifier", phoneAccountHandleFindCorrectPhoneAccountHandle.getId()) + "]") + " is not in the list of registered handles " + str + " - call was rejected.", new Object[0]);
            try {
                connection.hangup();
            } catch (CallStateException unused3) {
            }
        }
    }

    public static /* synthetic */ String $r8$lambda$s5X8T7Ocb53GGyyO_gm1BgngQi0(PhoneAccountHandle phoneAccountHandle) {
        return "[" + phoneAccountHandle.getComponentName() + " " + Rlog.pii("PstnIncomingCallNotifier", phoneAccountHandle.getId()) + "]";
    }

    private PhoneAccountHandle findCorrectPhoneAccountHandle() {
        TelecomAccountRegistry telecomAccountRegistry = TelecomAccountRegistry.getInstance(null);
        PhoneAccountHandle phoneAccountHandleForSubId = telecomAccountRegistry.getPhoneAccountHandleForSubId(this.mPhone.getSubId());
        if (telecomAccountRegistry.hasAccountEntryForPhoneAccount(phoneAccountHandleForSubId)) {
            return phoneAccountHandleForSubId;
        }
        Phone phone = this.mPhone;
        PhoneAccountHandle phoneAccountHandleMakePstnPhoneAccountHandleWithPrefix = PhoneUtils.makePstnPhoneAccountHandleWithPrefix(phone, "", true, phone.getUserHandle());
        if (telecomAccountRegistry.hasAccountEntryForPhoneAccount(phoneAccountHandleMakePstnPhoneAccountHandleWithPrefix)) {
            Log.i(this, "Receiving MT call in ECM. Using Emergency PhoneAccount Instead.", new Object[0]);
            return phoneAccountHandleMakePstnPhoneAccountHandleWithPrefix;
        }
        Log.i(this, "PhoneAccount not found.", new Object[0]);
        return null;
    }

    private boolean maybeSwapAnyWithUnknownConnection(Connection connection) {
        TelecomAccountRegistry telecomAccountRegistry;
        TelephonyConnectionService telephonyConnectionService;
        if (connection.isIncoming() || (telecomAccountRegistry = TelecomAccountRegistry.getInstance(null)) == null || (telephonyConnectionService = telecomAccountRegistry.getTelephonyConnectionService()) == null) {
            return false;
        }
        for (android.telecom.Connection connection2 : telephonyConnectionService.getAllConnections()) {
            if ((connection2 instanceof TelephonyConnection) && maybeSwapWithUnknownConnection((TelephonyConnection) connection2, connection)) {
                return true;
            }
        }
        return false;
    }

    private boolean maybeSwapWithUnknownConnection(TelephonyConnection telephonyConnection, Connection connection) {
        ImsPhoneConnection originalConnection = telephonyConnection.getOriginalConnection();
        if (originalConnection == null || originalConnection.isIncoming() || !Objects.equals(originalConnection.getAddress(), connection.getAddress())) {
            return false;
        }
        if ((connection instanceof ImsExternalConnection) && !(telephonyConnection.getOriginalConnection() instanceof ImsExternalConnection)) {
            Log.i(this, "maybeSwapWithUnknownConnection - not swapping regular connection with external connection.", new Object[0]);
            return false;
        }
        int phoneId = -1;
        int phoneId2 = (connection.getCall() == null || connection.getCall().getPhone() == null) ? -1 : connection.getCall().getPhone().getPhoneId();
        if (originalConnection.getCall() != null && originalConnection.getCall().getPhone() != null) {
            phoneId = originalConnection.getCall().getPhone().getPhoneId();
        }
        if (phoneId2 != phoneId) {
            Log.d(this, "not swapping connection from different phone", new Object[0]);
            return false;
        }
        Log.i(this, "maybeSwapWithUnknownConnection: swapping %s with %s", originalConnection, connection);
        telephonyConnection.setOriginalConnection(connection);
        if (originalConnection instanceof ImsExternalConnection) {
            return true;
        }
        if (originalConnection.getCall() != null && originalConnection.getCall().getPhone() != null && (originalConnection.getCall().getPhone() instanceof GsmCdmaPhone)) {
            originalConnection.getCall().getPhone().getCallTracker().cleanupCalls();
            Log.i(this, "maybeSwapWithUnknownConnection - Invoking call tracker cleanup for connection: " + originalConnection, new Object[0]);
        } else if (originalConnection.getCall() != null && originalConnection.getCall().getPhone() != null && (originalConnection.getCall().getPhone() instanceof ImsPhone) && (originalConnection instanceof ImsPhoneConnection)) {
            originalConnection.getCall().getPhone().getCallTracker().cleanupAndRemoveConnection(originalConnection);
            Log.i(this, "maybeSwapWithUnknownConnection - cleanup/remove: " + originalConnection, new Object[0]);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPhoneIdAsString() {
        Phone phone = this.mPhone;
        if (phone == null) {
            return "-1";
        }
        return String.valueOf(phone.getPhoneId());
    }
}
