package com.android.services.telephony;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import android.os.PersistableBundle;
import android.telecom.Conference;
import android.telecom.Conferenceable;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.VideoProfile;
import android.telephony.CarrierConfigManager;
import com.android.internal.hidden_from_bootclasspath.android.telecom.flags.Flags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.Phone;
import com.android.phone.PhoneUtils;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class ImsConferenceController {
    private final TelephonyConnectionServiceProxy mConnectionService;
    private final ImsConference.FeatureFlagProxy mFeatureFlagProxy;
    private TelecomAccountRegistry mTelecomAccountRegistry;
    private final TelephonyConferenceBase.TelephonyConferenceListener mConferenceListener = new TelephonyConferenceBase.TelephonyConferenceListener() { // from class: com.android.services.telephony.ImsConferenceController.1
        @Override // com.android.services.telephony.TelephonyConferenceBase.TelephonyConferenceListener
        public void onConferenceCapacityChanged() {
            Log.i(ImsConferenceController.this, "onConferenceCapacityChanged: recalc", new Object[0]);
            ImsConferenceController.this.recalculateConferenceable();
        }

        @Override // com.android.services.telephony.TelephonyConferenceBase.TelephonyConferenceListener
        public void onDestroyed(Conference conference) {
            if (Log.VERBOSE) {
                Log.v(ImsConferenceController.class, "onDestroyed: %s", conference);
            }
            if (conference instanceof ImsConference) {
                ImsConferenceController.this.mTelecomAccountRegistry.refreshAdhocConference(true, ImsConferenceController.this.getPhoneAccountHandle(conference));
            }
            ImsConferenceController.this.mImsConferences.remove(conference);
        }

        @Override // com.android.services.telephony.TelephonyConferenceBase.TelephonyConferenceListener
        public void onStateChanged(Conference conference, int i, int i2) {
            Log.v(this, "onStateChanged: Conference = " + conference, new Object[0]);
            ImsConferenceController.this.recalculateConferenceable();
        }

        @Override // com.android.services.telephony.TelephonyConferenceBase.TelephonyConferenceListener
        public void onConferenceMembershipChanged(Connection connection) {
            Log.d(this, "onConferenceMembershipChanged: %s", connection);
            ImsConferenceController.this.recalculateConferenceable();
        }
    };
    private final TelephonyConnection.TelephonyConnectionListener mTelephonyConnectionListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.ImsConferenceController.2
        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onConferenceStarted() {
            Log.v(this, "onConferenceStarted", new Object[0]);
            ImsConferenceController.this.recalculate();
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onConferenceSupportedChanged(Connection connection, boolean z) {
            Log.v(this, "onConferenceSupportedChanged", new Object[0]);
            ImsConferenceController.this.recalculate();
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onStateChanged(Connection connection, int i) {
            Log.v(this, "onStateChanged: %s", Rlog.pii("ImsConferenceController", connection.getAddress()));
            ImsConferenceController.this.recalculate();
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onDisconnected(Connection connection, DisconnectCause disconnectCause) {
            Log.v(this, "onDisconnected: %s", Rlog.pii("ImsConferenceController", connection.getAddress()));
            ImsConferenceController.this.recalculate();
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onDestroyed(Connection connection) {
            ImsConferenceController.this.remove(connection);
        }
    };
    private FeatureFlags mFeatureFlags = new FeatureFlagsImpl();
    private final ArrayList mTelephonyConnections = new ArrayList();
    private final List mImsConferences = new CopyOnWriteArrayList();
    private boolean mSpecialConstraintForVideoConference = false;
    private int mMaximumVideoConferenceSize = 5;

    public ImsConferenceController(TelecomAccountRegistry telecomAccountRegistry, TelephonyConnectionServiceProxy telephonyConnectionServiceProxy, ImsConference.FeatureFlagProxy featureFlagProxy) {
        Objects.requireNonNull(telephonyConnectionServiceProxy);
        this.mConnectionService = telephonyConnectionServiceProxy;
        Objects.requireNonNull(telecomAccountRegistry);
        this.mTelecomAccountRegistry = telecomAccountRegistry;
        Objects.requireNonNull(featureFlagProxy);
        this.mFeatureFlagProxy = featureFlagProxy;
    }

    void addConference(ImsConference imsConference) {
        if (this.mImsConferences.contains(imsConference)) {
            Log.w(this, "addConference - conference already tracked; conference=%s", imsConference);
            return;
        }
        this.mImsConferences.add(imsConference);
        imsConference.addTelephonyConferenceListener(this.mConferenceListener);
        recalculateConferenceable();
    }

    void add(TelephonyConnection telephonyConnection) {
        if ((telephonyConnection.getConnectionProperties() & 16) == 16) {
            return;
        }
        if (this.mTelephonyConnections.contains(telephonyConnection)) {
            Log.w(this, "add - connection already tracked; connection=%s", telephonyConnection);
            return;
        }
        if (Log.VERBOSE) {
            Log.v(this, "add connection %s", telephonyConnection);
        }
        this.mTelephonyConnections.add(telephonyConnection);
        telephonyConnection.addTelephonyConnectionListener(this.mTelephonyConnectionListener);
        recalculateConference();
        recalculateConferenceable();
    }

    void remove(Connection connection) {
        if ((connection.getConnectionProperties() & 16) == 16) {
            return;
        }
        if (!this.mTelephonyConnections.contains(connection)) {
            Log.d(this, "remove - connection not tracked; connection=%s", connection);
            return;
        }
        if (Log.VERBOSE) {
            Log.v(this, "remove connection: %s", connection);
        }
        if (connection instanceof TelephonyConnection) {
            ((TelephonyConnection) connection).removeTelephonyConnectionListener(this.mTelephonyConnectionListener);
        }
        this.mTelephonyConnections.remove(connection);
        recalculateConferenceable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recalculate() {
        recalculateConferenceable();
        recalculateConference();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PhoneAccountHandle getPhoneAccountHandle(Conferenceable conferenceable) {
        if (conferenceable instanceof Connection) {
            return ((Connection) conferenceable).getPhoneAccountHandle();
        }
        if (conferenceable instanceof Conference) {
            return ((Conference) conferenceable).getPhoneAccountHandle();
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("Unrecognized Conferenceable!", conferenceable);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: isSamePhoneAccountHandle, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public boolean lambda$recalculateConferenceable$2(Conferenceable conferenceable, Conferenceable conferenceable2) {
        return Objects.equals(getPhoneAccountHandle(conferenceable), getPhoneAccountHandle(conferenceable2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:11:0x003e  */
    public void recalculateConferenceable() {
        boolean z;
        Log.v(this, "recalculateConferenceable : %d", Integer.valueOf(this.mTelephonyConnections.size()));
        if (this.mImsConferences.size() <= 0 || !this.mSpecialConstraintForVideoConference) {
            z = false;
        } else {
            ImsConference imsConference = (ImsConference) this.mImsConferences.get(0);
            if (!VideoProfile.isVideo(imsConference.getVideoState()) || imsConference.getNumberOfParticipants() < this.mMaximumVideoConferenceSize) {
                z = false;
            } else {
                z = true;
            }
        }
        HashSet<Conferenceable> hashSet = new HashSet(this.mTelephonyConnections.size() + this.mImsConferences.size());
        HashSet hashSet2 = new HashSet();
        ArrayList arrayList = this.mTelephonyConnections;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            TelephonyConnection telephonyConnection = (TelephonyConnection) obj;
            if (Log.DEBUG) {
                Log.d(this, "recalc - %s %s supportsConf? %s", Integer.valueOf(telephonyConnection.getState()), telephonyConnection, Boolean.valueOf(telephonyConnection.isConferenceSupported()));
            }
            if (isMemberOfPeerConference(telephonyConnection) && (!Flags.multiPartyAnchorConf() || !checkCanMergeOnParticipant(telephonyConnection))) {
                if (Log.VERBOSE) {
                    Log.v(this, "Skipping connection in peer conference: %s", telephonyConnection);
                }
            } else if (!telephonyConnection.isConferenceSupported()) {
                if (!telephonyConnection.getConferenceables().isEmpty()) {
                    telephonyConnection.setConferenceables(Collections.EMPTY_LIST);
                }
            } else if (!z || !VideoProfile.isVideo(telephonyConnection.getVideoState())) {
                int state = telephonyConnection.getState();
                if (state == 4 || state == 5) {
                    hashSet.add(telephonyConnection);
                } else if (!telephonyConnection.getConferenceables().isEmpty()) {
                    telephonyConnection.setConferenceables(Collections.EMPTY_LIST);
                }
            }
        }
        for (ImsConference imsConference2 : this.mImsConferences) {
            if (Log.DEBUG) {
                Log.d(this, "recalc - %s %s", Integer.valueOf(imsConference2.getState()), imsConference2);
            }
            boolean z2 = Flags.multiPartyAnchorConf() && imsConference2.getCarrierConfig() != null && imsConference2.getCarrierConfig().isMultiPartyAnchorConfSupported();
            if (!imsConference2.isConferenceHost() && (!z2 || !checkCanMergeOnParticipant((TelephonyConnection) imsConference2.getConferenceHost()))) {
                if (Log.VERBOSE) {
                    Log.v(this, "skipping conference (not hosted on this device): %s", imsConference2);
                }
            } else {
                this.mTelecomAccountRegistry.refreshAdhocConference(false, getPhoneAccountHandle(imsConference2));
                int state2 = imsConference2.getState();
                if (state2 == 4 || state2 == 5) {
                    if (!imsConference2.isFullConference()) {
                        hashSet2.addAll(imsConference2.getConnections());
                        hashSet.add(imsConference2);
                    }
                }
            }
        }
        Log.v(this, "conferenceableSet size: " + hashSet.size(), new Object[0]);
        for (final Conferenceable conferenceable : hashSet) {
            if (conferenceable instanceof Connection) {
                List<Conferenceable> list = (List) hashSet2.stream().filter(new Predicate() { // from class: com.android.services.telephony.ImsConferenceController$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj2) {
                        return this.f$0.lambda$recalculateConferenceable$0(conferenceable, (Connection) obj2);
                    }
                }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.services.telephony.ImsConferenceController$$ExternalSyntheticLambda1
                    @Override // java.util.function.Supplier
                    public final Object get() {
                        return new ArrayList();
                    }
                }));
                list.addAll(hashSet.stream().filter(new Predicate() { // from class: com.android.services.telephony.ImsConferenceController$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj2) {
                        return this.f$0.lambda$recalculateConferenceable$1(conferenceable, (Conferenceable) obj2);
                    }
                }).toList());
                ((Connection) conferenceable).setConferenceables(list);
            } else if (conferenceable instanceof ImsConference) {
                ImsConference imsConference3 = (ImsConference) conferenceable;
                if (imsConference3.isFullConference()) {
                    if (Flags.multiPartyAnchorConf()) {
                        imsConference3.setConferenceables(Collections.EMPTY_LIST);
                    } else if (!imsConference3.getConferenceableConnections().isEmpty()) {
                        imsConference3.setConferenceableConnections(Collections.EMPTY_LIST);
                    }
                }
                if (Flags.multiPartyAnchorConf() && imsConference3.getCarrierConfig() != null && imsConference3.getCarrierConfig().isMultiPartyAnchorConfSupported() && checkCanMergeOnParticipant((TelephonyConnection) imsConference3.getConferenceHost())) {
                    List list2 = (List) hashSet.stream().filter(new Predicate() { // from class: com.android.services.telephony.ImsConferenceController$$ExternalSyntheticLambda3
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj2) {
                            return this.f$0.lambda$recalculateConferenceable$2(conferenceable, (Conferenceable) obj2);
                        }
                    }).collect(Collectors.toList());
                    if (!imsConference3.getConferenceables().equals(list2)) {
                        imsConference3.setConferenceables(list2);
                    }
                } else {
                    imsConference3.setConferenceableConnections((List) hashSet.stream().filter(new Predicate() { // from class: com.android.services.telephony.ImsConferenceController$$ExternalSyntheticLambda4
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj2) {
                            return this.f$0.lambda$recalculateConferenceable$3(conferenceable, (Conferenceable) obj2);
                        }
                    }).map(new Function() { // from class: com.android.services.telephony.ImsConferenceController$$ExternalSyntheticLambda5
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            return ImsConferenceController.m4286$r8$lambda$j8SYSzgJttwaTRkAIgKr4Hg9_U((Conferenceable) obj2);
                        }
                    }).collect(Collectors.toList()));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$recalculateConferenceable$1(Conferenceable conferenceable, Conferenceable conferenceable2) {
        return conferenceable != conferenceable2 && lambda$recalculateConferenceable$2(conferenceable, conferenceable2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$recalculateConferenceable$3(Conferenceable conferenceable, Conferenceable conferenceable2) {
        return (conferenceable2 instanceof Connection) && lambda$recalculateConferenceable$2(conferenceable, conferenceable2);
    }

    /* JADX INFO: renamed from: $r8$lambda$j8SYSzgJttwaTR-kAIgKr4Hg9_U, reason: not valid java name */
    public static /* synthetic */ Connection m4286$r8$lambda$j8SYSzgJttwaTRkAIgKr4Hg9_U(Conferenceable conferenceable) {
        return (Connection) conferenceable;
    }

    private boolean isMemberOfPeerConference(Connection connection) {
        com.android.internal.telephony.Connection originalConnection;
        return (connection instanceof TelephonyConnection) && (originalConnection = ((TelephonyConnection) connection).getOriginalConnection()) != null && originalConnection.isMultiparty() && originalConnection.isMemberOfPeerConference();
    }

    private void recalculateConference() {
        Log.v(this, "recalculateConference", new Object[0]);
        Iterator it = this.mTelephonyConnections.iterator();
        while (it.hasNext()) {
            TelephonyConnection telephonyConnection = (TelephonyConnection) it.next();
            if (telephonyConnection.isImsConnection() && telephonyConnection.getOriginalConnection() != null && telephonyConnection.getOriginalConnection().isMultiparty()) {
                startConference(telephonyConnection);
                it.remove();
            }
        }
    }

    private void startConference(TelephonyConnection telephonyConnection) {
        PhoneAccountHandle phoneAccountHandle;
        ImsConference.CarrierConfiguration carrierConfig;
        if (Log.VERBOSE) {
            Log.v(this, "Start new ImsConference - connection: %s", telephonyConnection);
        }
        if (telephonyConnection.isAdhocConferenceCall()) {
            Log.w(this, "start new ImsConference - control should never come here", new Object[0]);
            return;
        }
        TelephonyConnection telephonyConnectionCloneConnection = telephonyConnection.cloneConnection();
        telephonyConnectionCloneConnection.setVideoPauseSupported(telephonyConnection.getVideoPauseSupported());
        telephonyConnectionCloneConnection.setManageImsConferenceCallSupported(telephonyConnection.isManageImsConferenceCallSupported());
        if (telephonyConnection.getPhone() == null || telephonyConnection.getPhone().getPhoneType() != 5) {
            phoneAccountHandle = null;
            carrierConfig = null;
        } else {
            Phone phone = telephonyConnection.getPhone();
            PhoneAccountHandle phoneAccountHandleMakePstnPhoneAccountHandle = PhoneUtils.makePstnPhoneAccountHandle(phone.getDefaultPhone());
            carrierConfig = getCarrierConfig(phone);
            phoneAccountHandle = phoneAccountHandleMakePstnPhoneAccountHandle;
        }
        ImsConference imsConference = new ImsConference(this.mTelecomAccountRegistry, this.mConnectionService, telephonyConnectionCloneConnection, phoneAccountHandle, this.mFeatureFlagProxy, carrierConfig);
        imsConference.setState(telephonyConnectionCloneConnection.getState());
        imsConference.setCallDirection(telephonyConnectionCloneConnection.getCallDirection());
        imsConference.addTelephonyConferenceListener(this.mConferenceListener);
        imsConference.updateConferenceParticipantsAfterCreation();
        if (imsConference.isRemotelyHosted()) {
            if (phoneAccountHandle == null || !this.mTelecomAccountRegistry.isUsingSimCallManager(phoneAccountHandle)) {
                Log.i("ImsConferenceController", "startConference: Converting original connection into a conference", new Object[0]);
                this.mConnectionService.addConferenceFromConnection(imsConference, telephonyConnection);
                telephonyConnectionCloneConnection.setTelecomCallId(imsConference.getTelecomCallId());
                telephonyConnectionCloneConnection.setTelephonyConnectionProperties(2048);
            } else {
                Log.i("ImsConferenceController", "startConference: SIM call manager is in use so ignoring the request to initiate remotely hosted conference", new Object[0]);
                return;
            }
        } else {
            telephonyConnection.sendTelephonyConnectionEvent("android.telecom.event.MERGE_COMPLETE", null);
            this.mConnectionService.addConference(imsConference);
            telephonyConnectionCloneConnection.setTelecomCallId(imsConference.getTelecomCallId());
            telephonyConnection.removeTelephonyConnectionListener(this.mTelephonyConnectionListener);
            telephonyConnection.setTelephonyConnectionDisconnected(new DisconnectCause(9, android.telephony.DisconnectCause.toString(45)));
            telephonyConnection.close();
        }
        this.mSpecialConstraintForVideoConference = imsConference.specialConstraintForVideoConference();
        this.mMaximumVideoConferenceSize = imsConference.getMaximumVideoConferenceSize();
        this.mImsConferences.add(imsConference);
        recalculateConferenceable();
    }

    public static ImsConference.CarrierConfiguration getCarrierConfig(Phone phone) {
        ImsConference.CarrierConfiguration.Builder builder = new ImsConference.CarrierConfiguration.Builder();
        if (phone == null) {
            return builder.build();
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager != null) {
            PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId());
            boolean z = configForSubId.getBoolean("is_ims_conference_size_enforced_bool");
            int i = configForSubId.getInt("ims_conference_size_limit_int");
            boolean z2 = configForSubId.getBoolean("allow_hold_in_ims_call");
            builder.setIsMaximumConferenceSizeEnforced(z).setMaximumConferenceSize(i).setIsHoldAllowed(z2).setShouldLocalDisconnectEmptyConference(configForSubId.getBoolean("local_disconnect_empty_ims_conference_bool")).setIsMultiPartyAnchorConfSupported(Flags.multiPartyAnchorConf() ? configForSubId.getBoolean("support_multi_anchor_conf_bool") : false);
        }
        return builder.build();
    }

    public boolean hasActiveConference(PhoneAccountHandle phoneAccountHandle) {
        Iterator it = this.mImsConferences.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                return false;
            }
            ImsConference imsConference = (ImsConference) it.next();
            if (Flags.multiPartyAnchorConf() && imsConference.getCarrierConfig() != null && imsConference.getCarrierConfig().isMultiPartyAnchorConfSupported()) {
                z = true;
            }
            if (!imsConference.isConferenceHost() && !z) {
                if (Log.VERBOSE) {
                    Log.v(this, "skipping conference (not hosted on this device): %s", imsConference);
                }
            } else if (Objects.equals(getPhoneAccountHandle(imsConference), phoneAccountHandle)) {
                return true;
            }
        }
    }

    public void setFeatureFlags(FeatureFlags featureFlags) {
        this.mFeatureFlags = featureFlags;
    }

    private boolean checkCanMergeOnParticipant(TelephonyConnection telephonyConnection) {
        if (telephonyConnection == null) {
            return false;
        }
        boolean zIsVideo = VideoProfile.isVideo(telephonyConnection.getVideoState());
        boolean zIsWifi = telephonyConnection.isWifi();
        boolean z = !zIsVideo;
        Log.d(this, "checkCanMergeOnParticipant canMergeOnParticipant " + z + "isCurrentVideoCall " + zIsVideo + "isWifiCall " + zIsWifi, new Object[0]);
        return z;
    }
}
