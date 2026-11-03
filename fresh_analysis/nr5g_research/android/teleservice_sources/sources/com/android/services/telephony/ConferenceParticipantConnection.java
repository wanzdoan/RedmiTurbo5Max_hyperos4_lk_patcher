package com.android.services.telephony;

import android.net.Uri;
import android.os.PersistableBundle;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.ims.internal.ConferenceParticipant;
import com.android.internal.telephony.Phone;
import com.android.phone.PhoneGlobals;
import com.android.telephony.Rlog;
import gov.nist.javax.sip.address.GenericURI;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class ConferenceParticipantConnection extends Connection {
    private final Uri mEndpoint;
    private final com.android.internal.telephony.Connection mParentConnection;
    private final Uri mUserEntity;

    public ConferenceParticipantConnection(com.android.internal.telephony.Connection connection, ConferenceParticipant conferenceParticipant, boolean z) {
        String strStripSeparators;
        this.mParentConnection = connection;
        int participantPresentation = conferenceParticipant.getParticipantPresentation();
        Uri uriFromParts = null;
        if (participantPresentation == 1) {
            Phone phone = connection.getCall().getPhone();
            getCountryIso(phone);
            Uri participantAddress = getParticipantAddress(conferenceParticipant.getHandle());
            uriFromParts = (participantAddress == null || !isNeedParticipantPhoneNumberToNationalFormatForJp(phone, participantAddress) || (strStripSeparators = PhoneNumberUtils.stripSeparators(PhoneNumberUtils.formatNumber(participantAddress.getSchemeSpecificPart(), "JP"))) == null) ? participantAddress : Uri.fromParts(GenericURI.TEL, strStripSeparators, null);
        }
        setAddress(uriFromParts, participantPresentation);
        setVideoState(connection.getVideoState());
        setCallerDisplayName(conferenceParticipant.getDisplayName(), participantPresentation);
        this.mUserEntity = conferenceParticipant.getHandle();
        this.mEndpoint = conferenceParticipant.getEndpoint();
        setCapabilitiesAndProperties(z);
    }

    public void updateState(int i) {
        Log.v(this, "updateState endPoint: %s state: %s", Rlog.pii("ConferenceParticipantConnection", this.mEndpoint), Connection.stateToString(i));
        if (i == getState()) {
            return;
        }
        if (i == 0) {
            setInitializing();
            return;
        }
        if (i == 2) {
            setRinging();
            return;
        }
        if (i == 3) {
            setDialing();
            return;
        }
        if (i == 4) {
            setActive();
            return;
        }
        if (i == 5) {
            setOnHold();
        } else if (i == 6) {
            setDisconnected(new DisconnectCause(4));
            destroy();
        } else {
            setActive();
        }
    }

    @Override // android.telecom.Connection
    public void onDisconnect() {
        this.mParentConnection.onDisconnectConferenceParticipant(this.mUserEntity);
        MiTelephonyServiceStub.get().onParticipantConnectionDisconnect(this, this.mUserEntity);
    }

    public Uri getUserEntity() {
        return this.mUserEntity;
    }

    public Uri getEndpoint() {
        return this.mEndpoint;
    }

    private void setCapabilitiesAndProperties(boolean z) {
        setConnectionCapabilities(8192);
        if (z) {
            setConnectionProperties(2048);
        }
        setConnectionProperties(getConnectionProperties() | 294912);
    }

    private String getCountryIso(Phone phone) {
        SubscriptionInfo activeSubscriptionInfo;
        if (phone == null || MiTelephonyServiceStub.get().dismissCountryISOAfterConference(phone)) {
            return null;
        }
        int subId = phone.getSubId();
        SubscriptionManager subscriptionManager = TelecomAccountRegistry.getInstance(null).getSubscriptionManager();
        if (subscriptionManager == null || (activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(subId)) == null || TextUtils.isEmpty(activeSubscriptionInfo.getCountryIso())) {
            return null;
        }
        return activeSubscriptionInfo.getCountryIso().toUpperCase(Locale.ROOT);
    }

    private boolean isNeedParticipantPhoneNumberToNationalFormatForJp(Phone phone, Uri uri) {
        PersistableBundle carrierConfigForSubId;
        return (phone == null || uri == null || (carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(phone.getSubId())) == null || !carrierConfigForSubId.getBoolean("format_incoming_number_to_national_for_jp_bool") || !uri.getSchemeSpecificPart().startsWith("+81")) ? false : true;
    }

    public String toString() {
        return "[ConferenceParticipantConnection objId:" + System.identityHashCode(this) + " endPoint:" + Rlog.pii("ConferenceParticipantConnection", this.mEndpoint) + " address:" + Rlog.pii("ConferenceParticipantConnection", getAddress()) + " addressPresentation:" + getAddressPresentation() + " parentConnection:" + Rlog.pii("ConferenceParticipantConnection", this.mParentConnection.getAddress()) + " state:" + Connection.stateToString(getState()) + " connectTime:" + getConnectTimeMillis() + " connectElapsedTime:" + getConnectionStartElapsedRealtimeMillis() + "]";
    }

    private Uri getParticipantAddress(Uri uri) {
        if (uri == null) {
            return uri;
        }
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        if (!TextUtils.isEmpty(schemeSpecificPart)) {
            String[] strArrSplit = schemeSpecificPart.split("[@;:]");
            if (strArrSplit.length != 0) {
                return Uri.fromParts(GenericURI.TEL, strArrSplit[0], null);
            }
        }
        return uri;
    }
}
