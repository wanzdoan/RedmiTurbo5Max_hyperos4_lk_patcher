package com.android.services.telephony;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telecom.Conference;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.StatusHints;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;
import com.android.ims.internal.ConferenceParticipant;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Phone;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.telephony.Rlog;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class ImsConference extends TelephonyConferenceBase implements Holdable {
    private final CarrierConfiguration mCarrierConfig;
    private TelephonyConnection mConferenceHost;
    private Uri[] mConferenceHostAddress;
    private PhoneAccountHandle mConferenceHostPhoneAccountHandle;
    private final HashMap mConferenceParticipantConnections;
    private boolean mCouldManageConference;
    private FeatureFlagProxy mFeatureFlagProxy;
    private int mHostCallState;
    private Pair mHostParticipantIdentity;
    private boolean mIsDuringAddingParticipants;
    private boolean mIsHoldable;
    private boolean mIsUsingSimCallManager;
    private Pair mLoneParticipantIdentity;
    private List mParticipants;
    private TelecomAccountRegistry mTelecomAccountRegistry;
    private final TelephonyConnection.TelephonyConnectionListener mTelephonyConnectionListener;
    private TelephonyConnectionServiceProxy mTelephonyConnectionService;
    private final Object mUpdateSyncRoot;
    private boolean mWasRemotelyHosted;

    public interface FeatureFlagProxy {
        boolean isUsingSinglePartyCallEmulation();
    }

    private int changeBitmask(int i, int i2, boolean z) {
        return z ? i | i2 : (~i2) & i;
    }

    public Connection getPrimaryConnection() {
        return null;
    }

    @Override // com.android.services.telephony.Holdable
    public boolean isChildHoldable() {
        return false;
    }

    public class CarrierConfiguration {
        private boolean mIsHoldAllowed;
        private boolean mIsMaximumConferenceSizeEnforced;
        private boolean mIsMultiPartyAnchorConfSupported;
        private int mMaximumConferenceSize;
        private boolean mShouldLocalDisconnectEmptyConference;

        public class Builder {
            private boolean mIsMaximumConferenceSizeEnforced = false;
            private int mMaximumConferenceSize = 5;
            private boolean mShouldLocalDisconnectEmptyConference = false;
            private boolean mIsHoldAllowed = false;
            private boolean mIsMultiPartyAnchorConfSupported = false;

            public Builder setIsMaximumConferenceSizeEnforced(boolean z) {
                this.mIsMaximumConferenceSizeEnforced = z;
                return this;
            }

            public Builder setMaximumConferenceSize(int i) {
                this.mMaximumConferenceSize = i;
                return this;
            }

            public Builder setShouldLocalDisconnectEmptyConference(boolean z) {
                this.mShouldLocalDisconnectEmptyConference = z;
                return this;
            }

            public Builder setIsHoldAllowed(boolean z) {
                this.mIsHoldAllowed = z;
                return this;
            }

            public Builder setIsMultiPartyAnchorConfSupported(boolean z) {
                this.mIsMultiPartyAnchorConfSupported = z;
                return this;
            }

            public CarrierConfiguration build() {
                return new CarrierConfiguration(this.mIsMaximumConferenceSizeEnforced, this.mMaximumConferenceSize, this.mShouldLocalDisconnectEmptyConference, this.mIsHoldAllowed, this.mIsMultiPartyAnchorConfSupported);
            }
        }

        private CarrierConfiguration(boolean z, int i, boolean z2, boolean z3, boolean z4) {
            this.mIsMaximumConferenceSizeEnforced = z;
            this.mMaximumConferenceSize = i;
            this.mShouldLocalDisconnectEmptyConference = z2;
            this.mIsHoldAllowed = z3;
            this.mIsMultiPartyAnchorConfSupported = z4;
        }

        public boolean isMaximumConferenceSizeEnforced() {
            return this.mIsMaximumConferenceSizeEnforced;
        }

        public int getMaximumConferenceSize() {
            return this.mMaximumConferenceSize;
        }

        public boolean shouldLocalDisconnectEmptyConference() {
            return this.mShouldLocalDisconnectEmptyConference;
        }

        public boolean isHoldAllowed() {
            return this.mIsHoldAllowed;
        }

        public boolean isMultiPartyAnchorConfSupported() {
            return this.mIsMultiPartyAnchorConfSupported;
        }
    }

    public void updateConferenceParticipantsAfterCreation() {
        if (this.mConferenceHost != null) {
            Log.v(this, "updateConferenceStateAfterCreation :: process participant update", new Object[0]);
            TelephonyConnection telephonyConnection = this.mConferenceHost;
            handleConferenceParticipantsUpdate(telephonyConnection, telephonyConnection.getConferenceParticipants());
            return;
        }
        Log.v(this, "updateConferenceStateAfterCreation :: null mConferenceHost", new Object[0]);
    }

    public ImsConference(TelecomAccountRegistry telecomAccountRegistry, TelephonyConnectionServiceProxy telephonyConnectionServiceProxy, TelephonyConnection telephonyConnection, PhoneAccountHandle phoneAccountHandle, FeatureFlagProxy featureFlagProxy, CarrierConfiguration carrierConfiguration) {
        long connectTime;
        long connectTimeReal;
        int i;
        super(phoneAccountHandle);
        this.mTelephonyConnectionListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.ImsConference.1
            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onStateChanged(Connection connection, int i2) {
                ImsConference.this.setState(i2);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onDisconnected(Connection connection, DisconnectCause disconnectCause) {
                ImsConference.this.setDisconnected(disconnectCause);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onVideoStateChanged(Connection connection, int i2) {
                Log.d(this, "onVideoStateChanged video state %d", Integer.valueOf(i2));
                ImsConference.this.setVideoState(connection, i2);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onVideoProviderChanged(Connection connection, Connection.VideoProvider videoProvider) {
                Log.d(this, "onVideoProviderChanged: Connection: %s, VideoProvider: %s", connection, videoProvider);
                ImsConference.this.setVideoProvider(connection, videoProvider);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onConnectionCapabilitiesChanged(Connection connection, int i2) {
                Log.d(this, "onConnectionCapabilitiesChanged: Connection: %s, connectionCapabilities: %s", connection, Integer.valueOf(i2));
                int connectionCapabilities = ImsConference.this.getConnectionCapabilities();
                boolean zIsCarrierVideoConferencingSupported = ImsConference.this.mConferenceHost == null ? false : ImsConference.this.mConferenceHost.isCarrierVideoConferencingSupported();
                ImsConference imsConference = ImsConference.this;
                imsConference.setConnectionCapabilities(imsConference.applyHostCapabilities(connectionCapabilities, i2, zIsCarrierVideoConferencingSupported));
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onConnectionPropertiesChanged(Connection connection, int i2) {
                Log.i(ImsConference.this, "onConnectionPropertiesChanged: Connection: %s, connectionProperties: %s", connection, Integer.valueOf(i2));
                ImsConference.this.updateConnectionProperties(i2);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onStatusHintsChanged(Connection connection, StatusHints statusHints) {
                Log.v(this, "onStatusHintsChanged", new Object[0]);
                ImsConference.this.updateStatusHints();
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onExtrasChanged(Connection connection, Bundle bundle) {
                Log.v(this, "onExtrasChanged: c=" + connection + " Extras=" + Rlog.pii("ImsConference", bundle), new Object[0]);
                if (MiTelephonyServiceStub.get().onExtrasChanged(connection, bundle)) {
                    return;
                }
                ImsConference.this.updateExtras(bundle);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onExtrasRemoved(Connection connection, List list) {
                Log.v(this, "onExtrasRemoved: c=" + connection + " key=" + list, new Object[0]);
                ImsConference.this.removeExtras((List<String>) list);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onConnectionEvent(Connection connection, String str, Bundle bundle) {
                if ("android.telecom.event.MERGE_START".equals(str)) {
                    return;
                }
                ImsConference.this.sendConferenceEvent(str, bundle);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onOriginalConnectionConfigured(TelephonyConnection telephonyConnection2) {
                if (telephonyConnection2 == ImsConference.this.mConferenceHost) {
                    ImsConference.this.handleOriginalConnectionChange();
                }
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onConferenceParticipantsChanged(Connection connection, List list) {
                if (connection == null || list == null) {
                    return;
                }
                Log.v(this, "onConferenceParticipantsChanged: %d participants", Integer.valueOf(list.size()));
                ImsConference.this.handleConferenceParticipantsUpdate((TelephonyConnection) connection, list);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onRingbackRequested(Connection connection, boolean z) {
                Log.d(this, "onRingbackRequested ringback %s", z ? "Y" : "N");
                ImsConference.this.setRingbackRequested(z);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onConferenceParticipantsInvited(boolean z) {
                ImsConference.this.mIsDuringAddingParticipants = false;
            }
        };
        this.mConferenceParticipantConnections = new HashMap();
        this.mUpdateSyncRoot = new Object();
        this.mIsUsingSimCallManager = false;
        this.mWasRemotelyHosted = false;
        this.mLoneParticipantIdentity = null;
        this.mHostParticipantIdentity = null;
        this.mHostCallState = 1;
        this.mIsDuringAddingParticipants = false;
        Objects.requireNonNull(telecomAccountRegistry);
        this.mTelecomAccountRegistry = telecomAccountRegistry;
        Objects.requireNonNull(featureFlagProxy);
        this.mFeatureFlagProxy = featureFlagProxy;
        Objects.requireNonNull(carrierConfiguration);
        this.mCarrierConfig = carrierConfiguration;
        if (telephonyConnection.getOriginalConnection() != null) {
            connectTime = telephonyConnection.getOriginalConnection().getConnectTime();
            connectTimeReal = telephonyConnection.getOriginalConnection().getConnectTimeReal();
        } else {
            connectTime = 0;
            connectTimeReal = 0;
        }
        setConnectionTime(connectTime);
        setConnectionStartElapsedRealtimeMillis(connectTimeReal);
        telephonyConnection.setConnectTimeMillis(connectTime);
        telephonyConnection.setConnectionStartElapsedRealtimeMillis(connectTimeReal);
        this.mTelephonyConnectionService = telephonyConnectionServiceProxy;
        setConferenceHost(telephonyConnection);
        setVideoProvider(telephonyConnection, telephonyConnection.getVideoProvider());
        if (MiTelephonyServiceStub.get().isHoldAllowed(this, carrierConfiguration.isHoldAllowed())) {
            this.mIsHoldable = true;
            i = 2097219;
        } else {
            i = 2097216;
        }
        setConnectionCapabilities(applyHostCapabilities(isTestSim() ? i | DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY : i, this.mConferenceHost.getConnectionCapabilities(), this.mConferenceHost.isCarrierVideoConferencingSupported()));
        int iApplyHostProperties = applyHostProperties(32768, this.mConferenceHost.getConnectionProperties());
        Log.d(this, "MtkImsConference: properties=" + iApplyHostProperties, new Object[0]);
        setConnectionProperties(iApplyHostProperties);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int applyHostCapabilities(int i, int i2, boolean z) {
        int iChangeBitmask;
        int iChangeBitmask2 = changeBitmask(i, 768, (i2 & 768) != 0);
        if (z) {
            iChangeBitmask = changeBitmask(changeBitmask(iChangeBitmask2, 3072, (i2 & 3072) != 0), 524288, (i2 & 524288) != 0);
        } else {
            Log.v(this, "applyHostCapabilities : video conferencing not supported", new Object[0]);
            iChangeBitmask = changeBitmask(changeBitmask(iChangeBitmask2, 3072, false), 524288, false);
        }
        return changeBitmask(changeBitmask(changeBitmask(changeBitmask(iChangeBitmask, 8388608, (i2 & 8388608) != 0), 1048576, this.mConferenceHost.getVideoPauseSupported() && isVideoCapable()), 67108864, (i2 & 67108864) != 0), 1, (i2 & 1) != 0);
    }

    private int applyHostProperties(int i, int i2) {
        int iChangeBitmask = changeBitmask(changeBitmask(changeBitmask(changeBitmask(changeBitmask(i, 4, (i2 & 4) != 0), 8, (i2 & 8) != 0), 16, (i2 & 16) != 0), 2048, isRemotelyHosted()), 4096, (i2 & 4096) != 0);
        Log.i(this, "applyHostProperties: confProp=%s", Integer.valueOf(iChangeBitmask));
        return changeBitmask(changeBitmask(iChangeBitmask, 8192, (i2 & 8192) != 0), 32768, (i2 & 32768) != 0);
    }

    @Override // android.telecom.Conference
    public Connection.VideoProvider getVideoProvider() {
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection != null) {
            return telephonyConnection.getVideoProvider();
        }
        return null;
    }

    @Override // android.telecom.Conference
    public int getVideoState() {
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection != null) {
            return telephonyConnection.getVideoState();
        }
        return 0;
    }

    public Connection getConferenceHost() {
        return this.mConferenceHost;
    }

    public final CarrierConfiguration getCarrierConfig() {
        return this.mCarrierConfig;
    }

    public final void setParticipants(List list) {
        this.mParticipants = list;
    }

    @Override // android.telecom.Conference
    public void onDisconnect() {
        Log.v(this, "onDisconnect: hanging up conference host.", new Object[0]);
        if (this.mConferenceHost == null) {
            return;
        }
        disconnectConferenceParticipants();
        Call call = this.mConferenceHost.getCall();
        if (call != null) {
            try {
                call.hangup();
                return;
            } catch (CallStateException e) {
                Log.e((Object) this, (Throwable) e, "Exception thrown trying to hangup conference", new Object[0]);
                return;
            }
        }
        Log.w(this, "onDisconnect - null call", new Object[0]);
    }

    @Override // android.telecom.Conference
    public void onSeparate(Connection connection) {
        Log.wtf(this, "Cannot separate connections from an IMS conference.", new Object[0]);
    }

    @Override // android.telecom.Conference
    public void onMerge(Connection connection) {
        Log.v(this, "onMerge()", new Object[0]);
        if (this.mConferenceHost == null) {
            Log.w(this, "mConferenceHost is null!", new Object[0]);
            return;
        }
        if (this.mIsDuringAddingParticipants) {
            toastWhenIsAddingParticipants();
            return;
        }
        if (getNumberOfParticipants() >= this.mCarrierConfig.getMaximumConferenceSize()) {
            Log.v(this, "Conference is full!", new Object[0]);
            if (this.mConferenceHost.getPhone() != null && isShowToastWhenConferenceFull()) {
                toastWhenConferenceIsFull(this.mConferenceHost.getPhone().getContext());
            }
            if (ignoreAddRequestToFullConference()) {
                Log.v(this, "Ignore the add request", new Object[0]);
                return;
            }
        }
        try {
            Phone phone = this.mConferenceHost.getPhone();
            if (phone != null) {
                phone.conference();
                MiTelephonyServiceStub.get().onMerge(connection, phone);
            }
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "Exception thrown trying to merge call into a conference", new Object[0]);
        }
    }

    public void onMerge(Conference conference) {
        Log.i(this, "onMerge: merging two conferences together", new Object[0]);
        try {
            Phone phone = this.mConferenceHost.getPhone();
            if (phone != null) {
                if (this.mCarrierConfig.isMultiPartyAnchorConfSupported()) {
                    phone.conference();
                } else {
                    Log.wtf(this, "An attempt was made to merge two conferences but this carrier does not support multi-party anchor conferences", new Object[0]);
                }
            }
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "Exception thrown trying to merge conference into a conference", new Object[0]);
        }
    }

    @Override // android.telecom.Conference
    public void onAddConferenceParticipants(List list) {
        if (this.mConferenceHost == null) {
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Uri uri = (Uri) it.next();
            if (hasExistedInConference(uri.getSchemeSpecificPart()) || isParticipantHost(this.mConferenceHostAddress, uri)) {
                it.remove();
            }
        }
        if (list.size() == 0) {
            return;
        }
        if (getNumberOfParticipants() >= this.mCarrierConfig.getMaximumConferenceSize()) {
            Log.v(this, "Conference is full!", new Object[0]);
            if (this.mConferenceHost.getPhone() != null && isShowToastWhenConferenceFull()) {
                toastWhenConferenceIsFull(this.mConferenceHost.getPhone().getContext());
            }
            if (ignoreAddRequestToFullConference()) {
                Log.v(this, "Ignore the add request", new Object[0]);
                return;
            }
        }
        this.mConferenceHost.performAddConferenceParticipants(list);
        this.mIsDuringAddingParticipants = true;
    }

    @Override // android.telecom.Conference
    public void onAnswer(int i) {
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            return;
        }
        telephonyConnection.performAnswer(i);
    }

    @Override // android.telecom.Conference
    public void onReject() {
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            return;
        }
        telephonyConnection.performReject(1);
    }

    @Override // android.telecom.Conference
    public void onHold() {
        if (this.mIsDuringAddingParticipants) {
            toastWhenIsAddingParticipants();
            return;
        }
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            return;
        }
        telephonyConnection.onHold();
    }

    @Override // android.telecom.Conference
    public void onUnhold() {
        if (this.mIsDuringAddingParticipants) {
            toastWhenIsAddingParticipants();
            return;
        }
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            return;
        }
        telephonyConnection.onUnhold();
    }

    @Override // android.telecom.Conference
    public void onPlayDtmfTone(char c) {
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            return;
        }
        telephonyConnection.onPlayDtmfTone(c);
    }

    @Override // android.telecom.Conference
    public void onStopDtmfTone() {
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            return;
        }
        telephonyConnection.onStopDtmfTone();
    }

    @Override // android.telecom.Conference
    public void onConnectionAdded(Connection connection) {
        Log.d(this, "connection added: " + connection + ", time: " + connection.getConnectTimeMillis(), new Object[0]);
    }

    @Override // com.android.services.telephony.Holdable
    public void setHoldable(boolean z) {
        this.mIsHoldable = z;
        boolean holdable = MiTelephonyServiceStub.get().setHoldable(this, this.mIsHoldable);
        this.mIsHoldable = holdable;
        if (!holdable) {
            removeCapability(1);
        } else if (getState() == 4 || getState() == 5) {
            addCapability(1);
        }
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection != null) {
            telephonyConnection.setHoldable(this.mIsHoldable);
        }
    }

    public boolean isRemotelyHosted() {
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null || telephonyConnection.getOriginalConnection() == null) {
            return this.mWasRemotelyHosted;
        }
        com.android.internal.telephony.Connection originalConnection = this.mConferenceHost.getOriginalConnection();
        boolean z = originalConnection.isMultiparty() && !originalConnection.isConferenceHost();
        this.mWasRemotelyHosted = z;
        return z;
    }

    public boolean isConferenceHost() {
        com.android.internal.telephony.Connection originalConnection;
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        return telephonyConnection != null && (originalConnection = telephonyConnection.getOriginalConnection()) != null && originalConnection.isMultiparty() && originalConnection.isConferenceHost();
    }

    private void updateManageConference() {
        boolean z = false;
        boolean z2 = (getConnectionCapabilities() & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) != 0;
        if (!this.mFeatureFlagProxy.isUsingSinglePartyCallEmulation() || isMultiparty() ? this.mConferenceParticipantConnections.size() != 0 : this.mConferenceParticipantConnections.size() > 1) {
            z = true;
        }
        Log.v(this, "updateManageConference was :%s is:%s", z2 ? "Y" : "N", z ? "Y" : "N");
        if (z2 != z) {
            int connectionCapabilities = getConnectionCapabilities();
            setConnectionCapabilities(z ? (connectionCapabilities | DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) & (-2097153) : (connectionCapabilities & (-129)) | 2097152);
        }
    }

    private void setConferenceHost(TelephonyConnection telephonyConnection) {
        Log.i(this, "setConferenceHost " + telephonyConnection, new Object[0]);
        this.mConferenceHost = telephonyConnection;
        if (telephonyConnection.getPhone() != null && this.mConferenceHost.getPhone().getPhoneType() == 5) {
            Phone phone = this.mConferenceHost.getPhone();
            this.mConferenceHostPhoneAccountHandle = PhoneUtils.makePstnPhoneAccountHandle(phone.getDefaultPhone());
            ArrayList arrayList = new ArrayList();
            if (phone.getCurrentSubscriberUris() != null) {
                arrayList.addAll(new ArrayList(Arrays.asList(phone.getCurrentSubscriberUris())));
            }
            Uri[] uriArr = new Uri[arrayList.size()];
            this.mConferenceHostAddress = uriArr;
            Uri[] uriArr2 = (Uri[]) arrayList.toArray(uriArr);
            this.mConferenceHostAddress = uriArr2;
            for (Uri uri : uriArr2) {
                Log.d(this, "hostAddress: " + Rlog.pii("ImsConference", uri), new Object[0]);
            }
            Log.i(this, "setConferenceHost: hosts are " + ((String) Arrays.stream(this.mConferenceHostAddress).map(new Function() { // from class: com.android.services.telephony.ImsConference$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((Uri) obj).getSchemeSpecificPart();
                }
            }).map(new Function() { // from class: com.android.services.telephony.ImsConference$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return Rlog.pii("ImsConference", (String) obj);
                }
            }).collect(Collectors.joining(", "))), new Object[0]);
            this.mIsUsingSimCallManager = this.mTelecomAccountRegistry.isUsingSimCallManager(this.mConferenceHostPhoneAccountHandle);
        }
        if (!isConferenceHost()) {
            setAddress(this.mConferenceHost.getAddress(), this.mConferenceHost.getAddressPresentation());
            setCallerDisplayName(this.mConferenceHost.getCallerDisplayName(), this.mConferenceHost.getCallerDisplayNamePresentation());
            setConnectionStartElapsedRealtimeMillis(this.mConferenceHost.getConnectionStartElapsedRealtimeMillis());
            setConnectionTime(this.mConferenceHost.getConnectTimeMillis());
        }
        this.mConferenceHost.addTelephonyConnectionListener(this.mTelephonyConnectionListener);
        Log.d(this, "setConferenceHost: PhoneAccount is %s", this.mConferenceHostPhoneAccountHandle);
        this.mConferenceHost.setPhoneAccountHandle(this.mConferenceHostPhoneAccountHandle);
        setConnectionCapabilities(applyHostCapabilities(getConnectionCapabilities(), this.mConferenceHost.getConnectionCapabilities(), this.mConferenceHost.isCarrierVideoConferencingSupported()));
        setConnectionProperties(applyHostProperties(getConnectionProperties(), this.mConferenceHost.getConnectionProperties()));
        setState(this.mConferenceHost.getState());
        updateStatusHints();
        putExtras(this.mConferenceHost.getExtras());
    }

    public void handleConferenceParticipantsUpdate(TelephonyConnection telephonyConnection, List list) {
        boolean z;
        boolean z2;
        if (list == null) {
            return;
        }
        if (telephonyConnection != null && !telephonyConnection.isManageImsConferenceCallSupported()) {
            Log.i(this, "handleConferenceParticipantsUpdate: manage conference is disallowed", new Object[0]);
            return;
        }
        Log.i(this, "handleConferenceParticipantsUpdate: size=%d", Integer.valueOf(list.size()));
        synchronized (this.mUpdateSyncRoot) {
            try {
                int size = this.mConferenceParticipantConnections.size();
                boolean zIsFullConference = isFullConference();
                ArrayList arrayList = new ArrayList(list.size());
                HashSet hashSet = new HashSet(list.size());
                long jCount = list.stream().filter(new Predicate() { // from class: com.android.services.telephony.ImsConference$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return this.f$0.lambda$handleConferenceParticipantsUpdate$1((ConferenceParticipant) obj);
                    }
                }).count();
                boolean z3 = jCount <= 1;
                if ((isMultiparty() || z3) && !isMultiparty()) {
                    z = false;
                    z2 = false;
                } else {
                    Iterator it = list.iterator();
                    z2 = false;
                    while (it.hasNext()) {
                        ConferenceParticipant conferenceParticipant = (ConferenceParticipant) it.next();
                        Pair pair = new Pair(conferenceParticipant.getHandle(), conferenceParticipant.getEndpoint());
                        if (conferenceParticipant.getState() != 6) {
                            hashSet.add(pair);
                        }
                        if (!this.mConferenceParticipantConnections.containsKey(pair)) {
                            if (!isParticipantHost(this.mConferenceHostAddress, conferenceParticipant.getHandle())) {
                                createConferenceParticipantConnection(telephonyConnection, conferenceParticipant);
                                arrayList.add(conferenceParticipant);
                                z2 = true;
                            } else {
                                this.mHostParticipantIdentity = pair;
                            }
                        } else {
                            ConferenceParticipantConnection conferenceParticipantConnection = (ConferenceParticipantConnection) this.mConferenceParticipantConnections.get(pair);
                            Log.i(this, "handleConferenceParticipantsUpdate: updateState, participant = %s", conferenceParticipant);
                            conferenceParticipantConnection.updateState(conferenceParticipant.getState());
                            if (conferenceParticipant.getState() == 6) {
                                handleConnectionDestruction(conferenceParticipantConnection);
                            }
                            conferenceParticipantConnection.setVideoState(telephonyConnection.getVideoState());
                        }
                    }
                    if (z2) {
                        int size2 = arrayList.size();
                        int i = 0;
                        while (i < size2) {
                            Object obj = arrayList.get(i);
                            i++;
                            ConferenceParticipant conferenceParticipant2 = (ConferenceParticipant) obj;
                            int i2 = size2;
                            ConferenceParticipantConnection conferenceParticipantConnection2 = (ConferenceParticipantConnection) this.mConferenceParticipantConnections.get(new Pair(conferenceParticipant2.getHandle(), conferenceParticipant2.getEndpoint()));
                            conferenceParticipantConnection2.updateState(conferenceParticipant2.getState());
                            if (conferenceParticipant2.getState() == 6) {
                                handleConnectionDestruction(conferenceParticipantConnection2);
                            }
                            conferenceParticipantConnection2.setVideoState(telephonyConnection.getVideoState());
                            size2 = i2;
                        }
                    }
                    Iterator it2 = this.mConferenceParticipantConnections.entrySet().iterator();
                    z = false;
                    while (it2.hasNext()) {
                        Map.Entry entry = (Map.Entry) it2.next();
                        if (!hashSet.contains(entry.getKey())) {
                            ConferenceParticipantConnection conferenceParticipantConnection3 = (ConferenceParticipantConnection) entry.getValue();
                            conferenceParticipantConnection3.setDisconnected(new DisconnectCause(4));
                            it2.remove();
                            removeTelephonyConnection(conferenceParticipantConnection3);
                            conferenceParticipantConnection3.destroy();
                            z = true;
                        }
                    }
                }
                int size3 = this.mConferenceParticipantConnections.size();
                Log.v(this, "handleConferenceParticipantsUpdate: oldParticipantCount=%d, newParticipantCount=%d, isMultiPty=%b, cepParticipantCt=%d", Integer.valueOf(size), Integer.valueOf(size3), Boolean.valueOf(isMultiparty()), Long.valueOf(jCount));
                if (this.mFeatureFlagProxy.isUsingSinglePartyCallEmulation() && !MiTelephonyServiceStub.get().isAdhocConferenceCall(this, telephonyConnection.getPhone(), this.mConferenceHost.isAdhocConferenceCall())) {
                    if (size != 1 && size3 == 1) {
                        startEmulatingSinglePartyCall();
                    } else if (!isMultiparty() && !z3) {
                        stopEmulatingSinglePartyCall();
                    }
                }
                if (z2 || z) {
                    updateManageConference();
                }
                if (zIsFullConference != isFullConference()) {
                    notifyConferenceCapacityChanged();
                }
                if (this.mCarrierConfig.shouldLocalDisconnectEmptyConference() && ((size > 0 || !isMultiparty()) && jCount == 0)) {
                    Log.i(this, "handleConferenceParticipantsUpdate: empty conference; local disconnect.", new Object[0]);
                    onDisconnect();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$handleConferenceParticipantsUpdate$1(ConferenceParticipant conferenceParticipant) {
        return (Objects.equals(this.mHostParticipantIdentity, new Pair(conferenceParticipant.getHandle(), conferenceParticipant.getEndpoint())) || conferenceParticipant.getState() == 6) ? false : true;
    }

    private void stopEmulatingSinglePartyCall() {
        if (this.mIsUsingSimCallManager) {
            Log.i(this, "stopEmulatingSinglePartyCall: using sim call manager; skip.", new Object[0]);
            return;
        }
        Log.i(this, "stopEmulatingSinglePartyCall: conference now has more than one participant; make it look conference-like again.", new Object[0]);
        if (this.mCouldManageConference) {
            setConnectionCapabilities(getConnectionCapabilities() | DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        }
        setAddress(null, 3);
        setCallerDisplayName(null, 3);
        ConferenceParticipantConnection conferenceParticipantConnection = (ConferenceParticipantConnection) this.mConferenceParticipantConnections.get(this.mLoneParticipantIdentity);
        if (conferenceParticipantConnection != null) {
            Log.d(this, "stopEmulatingSinglePartyCall: restored lone participant connect time", new Object[0]);
            conferenceParticipantConnection.setConnectTimeMillis(getConnectionTime());
            conferenceParticipantConnection.setConnectionStartElapsedRealtimeMillis(getConnectionStartElapsedRealtimeMillis());
        }
        setConferenceState(true);
    }

    private void startEmulatingSinglePartyCall() {
        if (this.mIsUsingSimCallManager) {
            Log.i(this, "startEmulatingSinglePartyCall: using sim call manager; skip.", new Object[0]);
            return;
        }
        Log.i(this, "startEmulatingSinglePartyCall: conference has a single participant; downgrade to single party call.", new Object[0]);
        Iterator it = this.mConferenceParticipantConnections.values().iterator();
        if (it.hasNext()) {
            ConferenceParticipantConnection conferenceParticipantConnection = (ConferenceParticipantConnection) it.next();
            setAddress(conferenceParticipantConnection.getAddress(), conferenceParticipantConnection.getAddressPresentation());
            setCallerDisplayName(conferenceParticipantConnection.getCallerDisplayName(), conferenceParticipantConnection.getCallerDisplayNamePresentation());
            setConnectionStartElapsedRealtimeMillis(conferenceParticipantConnection.getConnectionStartElapsedRealtimeMillis());
            setConnectionTime(conferenceParticipantConnection.getConnectTimeMillis());
            setCallDirection(conferenceParticipantConnection.getCallDirection());
            this.mLoneParticipantIdentity = new Pair(conferenceParticipantConnection.getUserEntity(), conferenceParticipantConnection.getEndpoint());
            Bundle extras = conferenceParticipantConnection.getExtras();
            if (extras != null && extras.getBoolean("android.telecom.extra.DO_NOT_LOG_CALL")) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("android.telecom.extra.DO_NOT_LOG_CALL", true);
                putExtras(bundle);
            }
            conferenceParticipantConnection.setDisconnected(new DisconnectCause(4, "EMULATING_SINGLE_CALL"));
            removeTelephonyConnection(conferenceParticipantConnection);
            conferenceParticipantConnection.destroy();
            it.remove();
        }
        setConferenceState(false);
        this.mCouldManageConference = (getConnectionCapabilities() & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) != 0;
        setConnectionCapabilities(getConnectionCapabilities() & (-129));
    }

    private void createConferenceParticipantConnection(TelephonyConnection telephonyConnection, ConferenceParticipant conferenceParticipant) {
        ConferenceParticipantConnection conferenceParticipantConnection = new ConferenceParticipantConnection(telephonyConnection.getOriginalConnection(), conferenceParticipant, !isConferenceHost());
        if (conferenceParticipant.getConnectTime() == 0) {
            conferenceParticipantConnection.setConnectTimeMillis(telephonyConnection.getConnectTimeMillis());
            conferenceParticipantConnection.setConnectionStartElapsedRealtimeMillis(telephonyConnection.getConnectionStartElapsedRealtimeMillis());
        } else {
            conferenceParticipantConnection.setConnectTimeMillis(conferenceParticipant.getConnectTime());
            conferenceParticipantConnection.setConnectionStartElapsedRealtimeMillis(conferenceParticipant.getConnectElapsedTime());
        }
        conferenceParticipantConnection.setCallDirection(conferenceParticipant.getCallDirection());
        conferenceParticipantConnection.setConnectionProperties(applyHostPropertiesToChild(conferenceParticipantConnection.getConnectionProperties(), telephonyConnection.getConnectionProperties()));
        conferenceParticipantConnection.setStatusHints(telephonyConnection.getStatusHints());
        Bundle childExtrasFromHostBundle = getChildExtrasFromHostBundle(telephonyConnection.getExtras());
        if (isParticipantHost(this.mConferenceHostAddress, conferenceParticipant.getHandle())) {
            Log.i(this, "createCallForExistingConnection: Adding TelecomManager.EXTRA_DO_NOT_LOG_CALL", new Object[0]);
            childExtrasFromHostBundle.putBoolean("android.telecom.extra.DO_NOT_LOG_CALL", true);
        }
        conferenceParticipantConnection.setExtras(childExtrasFromHostBundle);
        Log.i(this, "createConferenceParticipantConnection: participant=%s, connection=%s", conferenceParticipant, conferenceParticipantConnection);
        synchronized (this.mUpdateSyncRoot) {
            this.mConferenceParticipantConnections.put(new Pair(conferenceParticipant.getHandle(), conferenceParticipant.getEndpoint()), conferenceParticipantConnection);
        }
        this.mTelephonyConnectionService.addExistingConnection(this.mConferenceHostPhoneAccountHandle, conferenceParticipantConnection, this);
        addTelephonyConnection(conferenceParticipantConnection);
    }

    private void removeConferenceParticipant(ConferenceParticipantConnection conferenceParticipantConnection) {
        Log.i(this, "removeConferenceParticipant: %s", conferenceParticipantConnection);
        synchronized (this.mUpdateSyncRoot) {
            this.mConferenceParticipantConnections.remove(new Pair(conferenceParticipantConnection.getUserEntity(), conferenceParticipantConnection.getEndpoint()));
        }
        conferenceParticipantConnection.destroy();
    }

    private void disconnectConferenceParticipants() {
        Log.v(this, "disconnectConferenceParticipants", new Object[0]);
        synchronized (this.mUpdateSyncRoot) {
            try {
                for (ConferenceParticipantConnection conferenceParticipantConnection : this.mConferenceParticipantConnections.values()) {
                    conferenceParticipantConnection.setDisconnected(new DisconnectCause(4));
                    conferenceParticipantConnection.destroy();
                }
                this.mConferenceParticipantConnections.clear();
                updateManageConference();
                MiTelephonyServiceStub.get().disconnectConferenceParticipants(this);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static String extractPhoneNumber(Uri uri) {
        String[] strArrSplit = uri.getSchemeSpecificPart().split("[@;:]");
        if (strArrSplit.length == 0) {
            Log.v("ImsConference", "extractPhoneNumber(N) : no number in handle", new Object[0]);
            return "";
        }
        return strArrSplit[0];
    }

    public static boolean isParticipantHost(Uri[] uriArr, Uri uri) {
        if (uriArr == null || uriArr.length == 0 || uri == null) {
            Log.v("ImsConference", "isParticipantHost(N) : host or participant uri null", new Object[0]);
            return false;
        }
        String strExtractPhoneNumber = extractPhoneNumber(uri);
        if (TextUtils.isEmpty(strExtractPhoneNumber)) {
            return false;
        }
        for (Uri uri2 : uriArr) {
            if (uri2 != null) {
                String strExtractPhoneNumber2 = extractPhoneNumber(uri2);
                boolean zCompare = PhoneNumberUtils.compare(strExtractPhoneNumber2, strExtractPhoneNumber);
                Log.v("ImsConference", "isParticipantHost(%s) : host: %s, participant %s", zCompare ? "Y" : "N", Rlog.pii("ImsConference", strExtractPhoneNumber2), Rlog.pii("ImsConference", strExtractPhoneNumber));
                if (zCompare) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOriginalConnectionChange() {
        PhoneAccountHandle phoneAccountHandleMakePstnPhoneAccountHandle;
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            Log.w(this, "handleOriginalConnectionChange; conference host missing.", new Object[0]);
            return;
        }
        com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
        if (originalConnection != null && originalConnection.getPhoneType() != 5) {
            Log.i(this, "handleOriginalConnectionChange : handover from IMS connection to new connection: %s", originalConnection);
            if (this.mConferenceHost.getPhone() != null) {
                int phoneType = this.mConferenceHost.getPhone().getPhoneType();
                TelephonyConnection telephonyConnection2 = this.mConferenceHost;
                if (phoneType == 5) {
                    phoneAccountHandleMakePstnPhoneAccountHandle = PhoneUtils.makePstnPhoneAccountHandle(telephonyConnection2.getPhone().getDefaultPhone());
                } else {
                    phoneAccountHandleMakePstnPhoneAccountHandle = PhoneUtils.makePstnPhoneAccountHandle(telephonyConnection2.getPhone());
                }
            } else {
                phoneAccountHandleMakePstnPhoneAccountHandle = null;
            }
            GsmConnection gsmConnection = new GsmConnection(originalConnection, getTelecomCallId(), this.mConferenceHost.getCallDirection());
            Log.i(this, "handleOriginalConnectionChange : SRVCC to GSM. Created new GsmConnection with objId=" + System.identityHashCode(gsmConnection) + " and originalConnection objId=" + System.identityHashCode(originalConnection), new Object[0]);
            gsmConnection.setConferenceSupported(true);
            MiTelephonyServiceStub.get().setAfterSrvccConnection(gsmConnection, true);
            if (singleCallAfterConferenceSRVCC()) {
                gsmConnection.setTelephonyConnectionProperties(gsmConnection.getConnectionProperties() | 64);
            }
            gsmConnection.updateState();
            gsmConnection.setConnectTimeMillis(this.mConferenceHost.getConnectTimeMillis());
            gsmConnection.setConnectionStartElapsedRealtimeMillis(this.mConferenceHost.getConnectionStartElapsedRealtimeMillis());
            this.mTelephonyConnectionService.addExistingConnection(phoneAccountHandleMakePstnPhoneAccountHandle, gsmConnection);
            this.mTelephonyConnectionService.addConnectionToConferenceController(gsmConnection);
            this.mConferenceHost.removeTelephonyConnectionListener(this.mTelephonyConnectionListener);
            this.mConferenceHost = null;
            setDisconnected(new DisconnectCause(9));
            disconnectConferenceParticipants();
            destroyTelephonyConference();
        }
        updateStatusHints();
    }

    public void setState(int i) {
        DisconnectCause telecomDisconnectCause;
        Log.v(this, "setState %s", Connection.stateToString(i));
        if (i == 2) {
            setConferenceOnRinging();
            return;
        }
        if (i == 3) {
            setConferenceOnDialing();
            return;
        }
        if (i == 4) {
            setConferenceOnActive();
            return;
        }
        if (i == 5) {
            setConferenceOnHold();
            return;
        }
        if (i != 6) {
            return;
        }
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            telecomDisconnectCause = new DisconnectCause(4);
        } else {
            com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
            TelephonyConnection telephonyConnection2 = this.mConferenceHost;
            if (originalConnection == null) {
                telecomDisconnectCause = telephonyConnection2.getTelephonyDisconnectCause();
                if (telecomDisconnectCause == null) {
                    telecomDisconnectCause = new DisconnectCause(4);
                }
            } else {
                Phone phone = telephonyConnection2.getPhone();
                TelephonyConnection telephonyConnection3 = this.mConferenceHost;
                if (phone != null) {
                    telecomDisconnectCause = DisconnectCauseUtil.toTelecomDisconnectCause(telephonyConnection3.getOriginalConnection().getDisconnectCause(), (String) null, this.mConferenceHost.getPhone().getPhoneId());
                } else {
                    telecomDisconnectCause = DisconnectCauseUtil.toTelecomDisconnectCause(telephonyConnection3.getOriginalConnection().getDisconnectCause());
                }
            }
        }
        setDisconnected(telecomDisconnectCause);
        disconnectConferenceParticipants();
        destroyTelephonyConference();
    }

    private boolean isVideoCapable() {
        int connectionCapabilities = this.mConferenceHost.getConnectionCapabilities();
        return ((connectionCapabilities & 768) == 0 || (connectionCapabilities & 3072) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatusHints() {
        if (MiTelephonyServiceStub.get().updateStatusHints(this)) {
            return;
        }
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null) {
            setStatusHints(null);
            return;
        }
        if (telephonyConnection.isWifi()) {
            Phone phone = this.mConferenceHost.getPhone();
            if (phone != null) {
                Context context = phone.getContext();
                StatusHints statusHints = new StatusHints(context.getString(R.string.status_hint_label_wifi_call), Icon.createWithResource(context, R.drawable.ic_signal_wifi_4_bar_24dp), null);
                setStatusHints(statusHints);
                Iterator<Connection> it = getConnections().iterator();
                while (it.hasNext()) {
                    it.next().setStatusHints(statusHints);
                }
                return;
            }
            return;
        }
        setStatusHints(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConnectionProperties(int i) {
        setConnectionProperties(applyHostProperties(getConnectionProperties(), i));
        for (Connection connection : getConnections()) {
            connection.setConnectionProperties(applyHostPropertiesToChild(connection.getConnectionProperties(), i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateExtras(Bundle bundle) {
        putExtras(bundle);
        if (bundle == null) {
            return;
        }
        Bundle childExtrasFromHostBundle = getChildExtrasFromHostBundle(bundle);
        Iterator<Connection> it = getConnections().iterator();
        while (it.hasNext()) {
            it.next().putExtras(childExtrasFromHostBundle);
        }
    }

    private Bundle getChildExtrasFromHostBundle(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        if (bundle != null && bundle.containsKey("android.telecom.extra.CALL_NETWORK_TYPE")) {
            bundle2.putInt("android.telecom.extra.CALL_NETWORK_TYPE", bundle.getInt("android.telecom.extra.CALL_NETWORK_TYPE"));
        }
        return bundle2;
    }

    private int applyHostPropertiesToChild(int i, int i2) {
        return changeBitmask(i, 8, (i2 & 8) != 0);
    }

    @Override // android.telecom.Conference
    public String toString() {
        return "[ImsConference objId:" + System.identityHashCode(this) + " telecomCallID:" + getTelecomCallId() + " state:" + Connection.stateToString(getState()) + " hostConnection:" + this.mConferenceHost + " participants:" + this.mConferenceParticipantConnections.size() + "]";
    }

    public int getNumberOfParticipants() {
        return this.mConferenceParticipantConnections.size();
    }

    public boolean isFullConference() {
        return this.mCarrierConfig.isMaximumConferenceSizeEnforced() && getNumberOfParticipants() >= this.mCarrierConfig.getMaximumConferenceSize();
    }

    private void handleConnectionDestruction(ConferenceParticipantConnection conferenceParticipantConnection) {
        removeConferenceParticipant(conferenceParticipantConnection);
        updateManageConference();
    }

    private void toastWhenIsAddingParticipants() {
        Phone phone;
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null || (phone = telephonyConnection.getPhone()) == null) {
            return;
        }
        Context context = phone.getContext();
        Toast.makeText(context, context.getString(R.string.volte_is_adding_participants), 0).show();
    }

    static void toastWhenConferenceIsFull(Context context) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, context.getString(R.string.volte_conf_member_reach_max), 0).show();
    }

    private boolean isShowToastWhenConferenceFull() {
        PersistableBundle carrierConfigBundle = getCarrierConfigBundle();
        boolean z = carrierConfigBundle != null ? carrierConfigBundle.getBoolean("mtk_show_toast_when_conference_full") : true;
        Log.d(this, "showToast: %s", Boolean.valueOf(z));
        return z;
    }

    private boolean ignoreAddRequestToFullConference() {
        PersistableBundle carrierConfigBundle = getCarrierConfigBundle();
        if (carrierConfigBundle != null) {
            return carrierConfigBundle.getBoolean("mtk_no_merge_req_after_max_connection");
        }
        return true;
    }

    public boolean specialConstraintForVideoConference() {
        if (isTestSim() && "OP08".equals(SystemProperties.get("persist.vendor.operator.optr", "OM"))) {
            return true;
        }
        PersistableBundle carrierConfigBundle = getCarrierConfigBundle();
        boolean z = carrierConfigBundle != null ? carrierConfigBundle.getBoolean("mtk_special_constraint_for_ims_video_conference") : false;
        Log.d(this, "specialConstraintForVideoConference: " + z, new Object[0]);
        return z;
    }

    public int getMaximumVideoConferenceSize() {
        if (isTestSim() && "OP08".equals(SystemProperties.get("persist.vendor.operator.optr", "OM"))) {
            return 3;
        }
        PersistableBundle carrierConfigBundle = getCarrierConfigBundle();
        if (carrierConfigBundle == null) {
            Log.w(this, "getMaximumVideoConferenceSize - failed to get video conference size", new Object[0]);
            return 5;
        }
        return carrierConfigBundle.getInt("mtk_ims_video_conference_size_limit_int");
    }

    private boolean singleCallAfterConferenceSRVCC() {
        PersistableBundle carrierConfigBundle = getCarrierConfigBundle();
        boolean z = carrierConfigBundle != null ? carrierConfigBundle.getBoolean("mtk_single_call_after_conference_srvcc") : true;
        Log.d(this, "singleCallAfterConferenceSRVCC: " + z, new Object[0]);
        return z;
    }

    private PersistableBundle getCarrierConfigBundle() {
        Phone phone;
        TelephonyConnection telephonyConnection = this.mConferenceHost;
        if (telephonyConnection == null || (phone = telephonyConnection.getPhone()) == null) {
            return null;
        }
        return PhoneGlobals.getInstance().getCarrierConfigForSubId(phone.getSubId());
    }

    private boolean isTestSim() {
        boolean z = SystemProperties.get("vendor.gsm.sim.ril.testsim").equals("1") || SystemProperties.get("vendor.gsm.sim.ril.testsim.2").equals("1") || SystemProperties.get("vendor.gsm.sim.ril.testsim.3").equals("1") || SystemProperties.get("vendor.gsm.sim.ril.testsim.4").equals("1");
        Log.d(this, "isTestSim: %s", Boolean.valueOf(z));
        return z;
    }

    private boolean hasExistedInConference(String str) {
        Iterator it = this.mConferenceParticipantConnections.entrySet().iterator();
        while (it.hasNext()) {
            String schemeSpecificPart = ((Uri) ((Pair) ((Map.Entry) it.next()).getKey()).first).getSchemeSpecificPart();
            Log.w(this, "The invited number is %s and participant number is %s", Rlog.pii("ImsConference", str), Rlog.pii("ImsConference", schemeSpecificPart));
            if (PhoneNumberUtils.compare(str, schemeSpecificPart)) {
                Log.d(this, "The invited number has already existed in the conference", new Object[0]);
                return true;
            }
        }
        return false;
    }
}
