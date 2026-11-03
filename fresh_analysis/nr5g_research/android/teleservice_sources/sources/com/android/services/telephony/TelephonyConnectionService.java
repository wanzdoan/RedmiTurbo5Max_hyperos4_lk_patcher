package com.android.services.telephony;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.TetheringManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telecom.Conference;
import android.telecom.Conferenceable;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.VideoProfile;
import android.telephony.CarrierConfigManager;
import android.telephony.DomainSelectionService;
import android.telephony.EmergencyRegistrationResult;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.ImsReasonInfo;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.d2d.Communicator;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.domainselection.DomainSelectionConnection;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.domainselection.EmergencyCallDomainSelectionConnection;
import com.android.internal.telephony.domainselection.NormalCallDomainSelectionConnection;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.emergency.RadioOnHelper;
import com.android.internal.telephony.emergency.RadioOnStateListener;
import com.android.internal.telephony.imsphone.ImsExternalCallTracker;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.SatelliteSOSMessageRecommender;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.phone.FrameworksUtils;
import com.android.phone.MMIDialogActivity;
import com.android.phone.MiPhoneAppStub;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.phone.callcomposer.CallComposerPictureManager;
import com.android.phone.settings.SuppServicesUiUtil;
import com.android.services.telephony.domainselection.DynamicRoutingController;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.services.telephony.MtkTelephonyConnectionServiceUtil;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import gov.nist.javax.sip.address.GenericURI;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyConnectionService extends ConnectionService {
    public static final int TIMEOUT_TO_DYNAMIC_ROUTING_MS = 10000;
    private TelephonyConnection mAlternateEmergencyConnection;
    private final DomainSelectionConnection.DomainSelectionConnectionCallback mCallDomainSelectionConnectionCallback;
    private final CdmaConferenceController mCdmaConferenceController;
    private DeviceState mDeviceState;
    private DisconnectCauseFactory mDisconnectCauseFactory;
    private DomainSelectionConnection mDomainSelectionConnection;
    private Executor mDomainSelectionMainExecutor;
    private DomainSelectionResolver mDomainSelectionResolver;
    private DynamicRoutingController mDynamicRoutingController;
    private EmergencyCallDomainSelectionConnection mEmergencyCallDomainSelectionConnection;
    private TelephonyConnection mEmergencyConnection;
    private final TelephonyConnection.TelephonyConnectionListener mEmergencyConnectionListener;
    private final TelephonyConnection.TelephonyConnectionListener mEmergencyConnectionSatelliteListener;
    private final DomainSelectionConnection.DomainSelectionConnectionCallback mEmergencyDomainSelectionConnectionCallback;
    public Pair mEmergencyRetryCache;
    private EmergencyStateTracker mEmergencyStateTracker;
    private EmergencyTonePlayer mEmergencyTonePlayer;
    private ComponentName mExpectedComponentName;
    private FeatureFlags mFeatureFlags;
    private TelephonyConnection mForcedEmergencyRoutingConnection;
    private HoldTracker mHoldTracker;
    private ImsConferenceController mImsConferenceController;
    private volatile boolean mIsEmergencyCallPending;
    private boolean mIsTtyEnabled;
    private TelephonyConnection mNormalCallConnection;
    private final TelephonyConnection.TelephonyConnectionListener mNormalCallConnectionListener;
    private TelephonyConnection mNormalRoutingEmergencyConnection;
    private final TelephonyConnection.TelephonyConnectionListener mNormalRoutingEmergencyConnectionListener;
    private PhoneFactoryProxy mPhoneFactoryProxy;
    private PhoneNumberUtilsProxy mPhoneNumberUtilsProxy;
    private PhoneSwitcherProxy mPhoneSwitcherProxy;
    private PhoneUtilsProxy mPhoneUtilsProxy;
    private RadioOnHelper mRadioOnHelper;
    private SatelliteController mSatelliteController;
    private SatelliteSOSMessageRecommender mSatelliteSOSMessageRecommender;
    private SubscriptionManagerProxy mSubscriptionManagerProxy;
    private final TelephonyConferenceController mTelephonyConferenceController;
    private final TelephonyConferenceBase.TelephonyConferenceListener mTelephonyConferenceListener;
    private final TelephonyConnection.TelephonyConnectionListener mTelephonyConnectionListener;
    private final TelephonyConnectionServiceProxy mTelephonyConnectionServiceProxy;
    private TelephonyManagerProxy mTelephonyManagerProxy;
    private final BroadcastReceiver mTtyBroadcastReceiver;
    private static final String LOG_TAG = TelephonyConnectionService.class.getSimpleName();
    private static final Pattern CDMA_ACTIVATION_CODE_REGEX_PATTERN = Pattern.compile("\\*228[0-9]{0,2}");

    public interface DisconnectCauseFactory {
        DisconnectCause toTelecomDisconnectCause(int i, String str);

        DisconnectCause toTelecomDisconnectCause(int i, String str, int i2);
    }

    public interface PhoneFactoryProxy {
        Phone getDefaultPhone();

        Phone getPhone(int i);

        Phone[] getPhones();
    }

    public interface PhoneNumberUtilsProxy {
        String convertToEmergencyNumber(Context context, String str);
    }

    public interface PhoneSwitcherProxy {
        PhoneSwitcher getPhoneSwitcher();
    }

    public interface PhoneUtilsProxy {
        int getSubIdForPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle);

        PhoneAccountHandle makePstnPhoneAccountHandle(Phone phone);

        PhoneAccountHandle makePstnPhoneAccountHandleWithPrefix(Phone phone, String str, boolean z);
    }

    public interface SubscriptionManagerProxy {
        int getDefaultDataPhoneId();

        int getDefaultVoicePhoneId();

        int getPhoneId(int i);

        int getSimStateForSlotIdx(int i);
    }

    public interface TelephonyManagerProxy {
        Map getCurrentEmergencyNumberList();

        int getMaxNumberOfSimultaneouslyActiveSims();

        int getPhoneCount();

        boolean isConcurrentCallsPossible();

        boolean isCurrentEmergencyNumber(String str);
    }

    private Connection checkAdditionalOutgoingCallLimits(Phone phone) {
        return null;
    }

    public TelephonyConnectionService() {
        TelephonyConnectionServiceProxy telephonyConnectionServiceProxy = new TelephonyConnectionServiceProxy() { // from class: com.android.services.telephony.TelephonyConnectionService.1
            @Override // com.android.services.telephony.TelephonyConnectionServiceProxy
            public Collection getAllConnections() {
                return TelephonyConnectionService.this.getAllConnections();
            }

            @Override // com.android.services.telephony.TelephonyConnectionServiceProxy
            public void addConference(TelephonyConference telephonyConference) {
                TelephonyConnectionService.this.addTelephonyConference(telephonyConference);
            }

            @Override // com.android.services.telephony.TelephonyConnectionServiceProxy
            public void addConference(ImsConference imsConference) {
                Connection conferenceHost = imsConference.getConferenceHost();
                if (conferenceHost instanceof TelephonyConnection) {
                    TelephonyConnection telephonyConnection = (TelephonyConnection) conferenceHost;
                    telephonyConnection.setTelephonyConnectionService(TelephonyConnectionService.this);
                    telephonyConnection.setPhoneAccountHandle(imsConference.getPhoneAccountHandle());
                }
                TelephonyConnectionService.this.addTelephonyConference(imsConference);
            }

            @Override // com.android.services.telephony.TelephonyConnectionServiceProxy
            public void addConferenceFromConnection(ImsConference imsConference, TelephonyConnection telephonyConnection) {
                Connection conferenceHost = imsConference.getConferenceHost();
                if (conferenceHost instanceof TelephonyConnection) {
                    TelephonyConnection telephonyConnection2 = (TelephonyConnection) conferenceHost;
                    telephonyConnection2.setTelephonyConnectionService(TelephonyConnectionService.this);
                    telephonyConnection2.setPhoneAccountHandle(imsConference.getPhoneAccountHandle());
                }
                TelephonyConnectionService.this.addTelephonyConferenceFromConnection(imsConference, telephonyConnection);
            }

            @Override // com.android.services.telephony.TelephonyConnectionServiceProxy
            public void addExistingConnection(PhoneAccountHandle phoneAccountHandle, Connection connection) {
                TelephonyConnectionService.this.addExistingConnection(phoneAccountHandle, connection);
            }

            @Override // com.android.services.telephony.TelephonyConnectionServiceProxy
            public void addExistingConnection(PhoneAccountHandle phoneAccountHandle, Connection connection, Conference conference) {
                TelephonyConnectionService.this.addExistingConnection(phoneAccountHandle, connection, conference);
            }

            @Override // com.android.services.telephony.TelephonyConnectionServiceProxy
            public void addConnectionToConferenceController(TelephonyConnection telephonyConnection) {
                TelephonyConnectionService.this.addConnectionToConferenceController(telephonyConnection);
            }
        };
        this.mTelephonyConnectionServiceProxy = telephonyConnectionServiceProxy;
        this.mTtyBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.TelephonyConnectionService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.v(this, "onReceive, action: %s", action);
                if (action.equals("android.telecom.action.TTY_PREFERRED_MODE_CHANGED")) {
                    boolean z = intent.getIntExtra("android.telecom.extra.TTY_PREFERRED_MODE", 0) != 0;
                    if (z != TelephonyConnectionService.this.mIsTtyEnabled) {
                        TelephonyConnectionService.this.handleTtyModeChange(z);
                    }
                }
            }
        };
        this.mTelephonyConferenceController = new TelephonyConferenceController(telephonyConnectionServiceProxy);
        this.mCdmaConferenceController = new CdmaConferenceController(this);
        this.mFeatureFlags = new FeatureFlagsImpl();
        this.mExpectedComponentName = null;
        this.mDeviceState = new DeviceState();
        this.mSubscriptionManagerProxy = new SubscriptionManagerProxy(this) { // from class: com.android.services.telephony.TelephonyConnectionService.3
            @Override // com.android.services.telephony.TelephonyConnectionService.SubscriptionManagerProxy
            public int getDefaultVoicePhoneId() {
                return SubscriptionManager.getDefaultVoicePhoneId();
            }

            @Override // com.android.services.telephony.TelephonyConnectionService.SubscriptionManagerProxy
            public int getDefaultDataPhoneId() {
                return getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
            }

            @Override // com.android.services.telephony.TelephonyConnectionService.SubscriptionManagerProxy
            public int getSimStateForSlotIdx(int i) {
                return TelephonyManager.getSimStateForSlotIndex(i);
            }

            @Override // com.android.services.telephony.TelephonyConnectionService.SubscriptionManagerProxy
            public int getPhoneId(int i) {
                return SubscriptionManager.getPhoneId(i);
            }
        };
        this.mPhoneFactoryProxy = new PhoneFactoryProxy(this) { // from class: com.android.services.telephony.TelephonyConnectionService.4
            @Override // com.android.services.telephony.TelephonyConnectionService.PhoneFactoryProxy
            public Phone getPhone(int i) {
                return PhoneFactory.getPhone(i);
            }

            @Override // com.android.services.telephony.TelephonyConnectionService.PhoneFactoryProxy
            public Phone getDefaultPhone() {
                return PhoneFactory.getDefaultPhone();
            }

            @Override // com.android.services.telephony.TelephonyConnectionService.PhoneFactoryProxy
            public Phone[] getPhones() {
                return PhoneFactory.getPhones();
            }
        };
        this.mPhoneUtilsProxy = new PhoneUtilsProxy(this) { // from class: com.android.services.telephony.TelephonyConnectionService.5
            @Override // com.android.services.telephony.TelephonyConnectionService.PhoneUtilsProxy
            public int getSubIdForPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle) {
                return PhoneUtils.getSubIdForPhoneAccountHandle(phoneAccountHandle);
            }

            @Override // com.android.services.telephony.TelephonyConnectionService.PhoneUtilsProxy
            public PhoneAccountHandle makePstnPhoneAccountHandle(Phone phone) {
                return PhoneUtils.makePstnPhoneAccountHandle(phone);
            }

            @Override // com.android.services.telephony.TelephonyConnectionService.PhoneUtilsProxy
            public PhoneAccountHandle makePstnPhoneAccountHandleWithPrefix(Phone phone, String str, boolean z) {
                return PhoneUtils.makePstnPhoneAccountHandleWithPrefix(phone, str, z, phone.getUserHandle());
            }
        };
        this.mPhoneNumberUtilsProxy = new PhoneNumberUtilsProxy(this) { // from class: com.android.services.telephony.TelephonyConnectionService.6
            @Override // com.android.services.telephony.TelephonyConnectionService.PhoneNumberUtilsProxy
            public String convertToEmergencyNumber(Context context, String str) {
                return PhoneNumberUtils.convertToEmergencyNumber(context, str);
            }
        };
        this.mPhoneSwitcherProxy = new PhoneSwitcherProxy(this) { // from class: com.android.services.telephony.TelephonyConnectionService.7
            @Override // com.android.services.telephony.TelephonyConnectionService.PhoneSwitcherProxy
            public PhoneSwitcher getPhoneSwitcher() {
                return PhoneSwitcher.getInstance();
            }
        };
        this.mDisconnectCauseFactory = new DisconnectCauseFactory(this) { // from class: com.android.services.telephony.TelephonyConnectionService.8
            @Override // com.android.services.telephony.TelephonyConnectionService.DisconnectCauseFactory
            public DisconnectCause toTelecomDisconnectCause(int i, String str) {
                return DisconnectCauseUtil.toTelecomDisconnectCause(i, str);
            }

            @Override // com.android.services.telephony.TelephonyConnectionService.DisconnectCauseFactory
            public DisconnectCause toTelecomDisconnectCause(int i, String str, int i2) {
                return DisconnectCauseUtil.toTelecomDisconnectCause(i, str, i2);
            }
        };
        this.mNormalRoutingEmergencyConnectionListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.TelephonyConnectionService.9
            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onStateChanged(Connection connection, int i) {
                TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
                Log.i(this, "onStateChanged normal routing callId=" + telephonyConnection.getTelecomCallId() + ", state=" + i, new Object[0]);
                TelephonyConnectionService.this.mEmergencyStateTracker.onNormalRoutingEmergencyCallStateChanged(telephonyConnection, i);
            }
        };
        this.mEmergencyConnectionListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.TelephonyConnectionService.10
            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onOriginalConnectionConfigured(TelephonyConnection telephonyConnection) {
                com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
                if (originalConnection == null || TelephonyConnectionService.this.mEmergencyStateTracker == null) {
                    return;
                }
                TelephonyConnectionService.this.mEmergencyStateTracker.onEmergencyCallDomainUpdated(originalConnection.getPhoneType(), telephonyConnection);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onStateChanged(Connection connection, int i) {
                if (TelephonyConnectionService.this.mEmergencyCallDomainSelectionConnection == null || connection == null) {
                    return;
                }
                TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
                Log.i(this, "onStateChanged callId=" + telephonyConnection.getTelecomCallId() + ", state=" + i, new Object[0]);
                if (telephonyConnection.getState() == 4) {
                    TelephonyConnectionService.this.mEmergencyStateTracker.onEmergencyCallStateChanged(telephonyConnection.getOriginalConnection().getState(), telephonyConnection);
                    TelephonyConnectionService.this.releaseEmergencyCallDomainSelection(false, true);
                }
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onConnectionPropertiesChanged(Connection connection, int i) {
                if (connection == null || TelephonyConnectionService.this.mEmergencyStateTracker == null) {
                    return;
                }
                TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
                com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
                if (originalConnection == null || !originalConnection.getState().isAlive()) {
                    Log.i(this, "onConnectionPropertiesChanged without orig connection alive", new Object[0]);
                    return;
                }
                Log.i(this, "onConnectionPropertiesChanged prop=" + i, new Object[0]);
                TelephonyConnectionService.this.mEmergencyStateTracker.onEmergencyCallPropertiesChanged(i, telephonyConnection);
            }
        };
        this.mEmergencyConnectionSatelliteListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.TelephonyConnectionService.11
            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onStateChanged(Connection connection, int i) {
                if (connection == null) {
                    Log.d(this, "onStateChanged for satellite listener: connection is null", new Object[0]);
                    return;
                }
                if (TelephonyConnectionService.this.mSatelliteSOSMessageRecommender == null) {
                    Log.d(this, "onStateChanged for satellite listener: mSatelliteSOSMessageRecommender is null", new Object[0]);
                    return;
                }
                TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
                TelephonyConnectionService.this.mSatelliteSOSMessageRecommender.onEmergencyCallConnectionStateChanged(telephonyConnection.getTelecomCallId(), i);
                if (i == 6 || i == 4) {
                    telephonyConnection.removeTelephonyConnectionListener(TelephonyConnectionService.this.mEmergencyConnectionSatelliteListener);
                    TelephonyConnectionService.this.mSatelliteSOSMessageRecommender = null;
                }
            }
        };
        this.mNormalCallConnectionListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.TelephonyConnectionService.12
            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onStateChanged(Connection connection, int i) {
                TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
                if (telephonyConnection != null) {
                    int state = telephonyConnection.getState();
                    if (state == 4) {
                        TelephonyConnectionService.this.clearNormalCallDomainSelectionConnection();
                        TelephonyConnectionService.this.mNormalCallConnection = null;
                    } else {
                        if (state != 6) {
                            return;
                        }
                        TelephonyConnectionService.this.clearNormalCallDomainSelectionConnection();
                        telephonyConnection.removeTelephonyConnectionListener(TelephonyConnectionService.this.mNormalCallConnectionListener);
                    }
                }
            }
        };
        this.mEmergencyDomainSelectionConnectionCallback = new AnonymousClass13();
        this.mCallDomainSelectionConnectionCallback = new DomainSelectionConnection.DomainSelectionConnectionCallback() { // from class: com.android.services.telephony.TelephonyConnectionService.14
            public void onSelectionTerminated(int i) {
                TelephonyConnectionService.this.mDomainSelectionMainExecutor.execute(new Runnable(i) { // from class: com.android.services.telephony.TelephonyConnectionService.14.1
                    int mCause;
                    final /* synthetic */ int val$cause;

                    {
                        this.val$cause = i;
                        this.mCause = i;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        Log.v(this, "Call domain selection terminated.", new Object[0]);
                        if (TelephonyConnectionService.this.mDomainSelectionConnection != null) {
                            if (TelephonyConnectionService.this.mNormalCallConnection != null) {
                                NormalCallDomainSelectionConnection normalCallDomainSelectionConnection = TelephonyConnectionService.this.mDomainSelectionConnection;
                                if (this.mCause == -1) {
                                    this.mCause = normalCallDomainSelectionConnection.getDisconnectCause();
                                }
                                Log.d(this, "Call connection closed. PreciseCause: " + normalCallDomainSelectionConnection.getPreciseDisconnectCause() + " DisconnectCause: " + normalCallDomainSelectionConnection.getDisconnectCause() + " Reason: " + normalCallDomainSelectionConnection.getReasonMessage(), new Object[0]);
                                TelephonyConnectionService.this.mNormalCallConnection.setTelephonyConnectionDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(this.mCause, normalCallDomainSelectionConnection.getPreciseDisconnectCause(), normalCallDomainSelectionConnection.getReasonMessage(), normalCallDomainSelectionConnection.getPhoneId(), normalCallDomainSelectionConnection.getImsReasonInfo(), new FlagsAdapterImpl()));
                                TelephonyConnectionService.this.mNormalCallConnection.close();
                                TelephonyConnectionService.this.mNormalCallConnection.updateStateDetails();
                                TelephonyConnectionService.this.mNormalCallConnection = null;
                            } else {
                                Log.v(this, "NormalCallConnection is null.", new Object[0]);
                            }
                            TelephonyConnectionService.this.mDomainSelectionConnection = null;
                            return;
                        }
                        Log.v(this, "DomainSelectionConnection is null.", new Object[0]);
                        if (TelephonyConnectionService.this.mNormalCallConnection != null) {
                            TelephonyConnectionService.this.mNormalCallConnection.updateStateDetails();
                        }
                    }
                });
            }
        };
        this.mTelephonyConnectionListener = new TelephonyConnection.TelephonyConnectionListener() { // from class: com.android.services.telephony.TelephonyConnectionService.15
            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onOriginalConnectionConfigured(TelephonyConnection telephonyConnection) {
                TelephonyConnectionService.this.addConnectionToConferenceController(telephonyConnection);
            }

            @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
            public void onOriginalConnectionRetry(TelephonyConnection telephonyConnection, boolean z) {
                TelephonyConnectionService.this.retryOutgoingOriginalConnection(telephonyConnection, telephonyConnection.getPhone(), z);
            }
        };
        this.mTelephonyConferenceListener = new TelephonyConferenceBase.TelephonyConferenceListener() { // from class: com.android.services.telephony.TelephonyConnectionService.16
            @Override // com.android.services.telephony.TelephonyConferenceBase.TelephonyConferenceListener
            public void onConferenceMembershipChanged(Connection connection) {
                TelephonyConnectionService.this.mHoldTracker.updateHoldCapability();
            }
        };
    }

    public boolean isConferenceActive(PhoneAccountHandle phoneAccountHandle) {
        ImsConferenceController imsConferenceController = this.mImsConferenceController;
        if (imsConferenceController != null) {
            return imsConferenceController.hasActiveConference(phoneAccountHandle);
        }
        return false;
    }

    class SlotStatus {
        public int activeSubId;
        public int capabilities;
        public int simState;
        public int slotId;
        public boolean isLocked = false;
        public boolean hasDialedEmergencyNumber = false;

        public boolean isSubActiveAndSimPresent() {
            return (this.simState == 1 || this.activeSubId == -1) ? false : true;
        }

        public SlotStatus(int i, int i2, int i3) {
            this.slotId = i;
            this.capabilities = i2;
            this.activeSubId = i3;
        }
    }

    class TelephonyManagerProxyImpl implements TelephonyManagerProxy {
        private final TelephonyManager mTelephonyManager;

        TelephonyManagerProxyImpl(TelephonyConnectionService telephonyConnectionService, Context context) {
            this.mTelephonyManager = new TelephonyManager(context);
        }

        @Override // com.android.services.telephony.TelephonyConnectionService.TelephonyManagerProxy
        public int getPhoneCount() {
            return this.mTelephonyManager.getPhoneCount();
        }

        @Override // com.android.services.telephony.TelephonyConnectionService.TelephonyManagerProxy
        public boolean isCurrentEmergencyNumber(String str) {
            try {
                return this.mTelephonyManager.isEmergencyNumber(PhoneNumberUtils.extractNetworkPortionAlt(str));
            } catch (IllegalStateException unused) {
                return false;
            }
        }

        @Override // com.android.services.telephony.TelephonyConnectionService.TelephonyManagerProxy
        public Map getCurrentEmergencyNumberList() {
            try {
                return this.mTelephonyManager.getEmergencyNumberList();
            } catch (IllegalStateException unused) {
                return new HashMap();
            }
        }

        @Override // com.android.services.telephony.TelephonyConnectionService.TelephonyManagerProxy
        public int getMaxNumberOfSimultaneouslyActiveSims() {
            try {
                return this.mTelephonyManager.getMaxNumberOfSimultaneouslyActiveSims();
            } catch (IllegalStateException unused) {
                return 1;
            }
        }

        @Override // com.android.services.telephony.TelephonyConnectionService.TelephonyManagerProxy
        public boolean isConcurrentCallsPossible() {
            try {
                return getMaxNumberOfSimultaneouslyActiveSims() > 1 || this.mTelephonyManager.getPhoneCapability().getMaxActiveVoiceSubscriptions() > 1;
            } catch (IllegalStateException unused) {
                return false;
            }
        }
    }

    public void setSubscriptionManagerProxy(SubscriptionManagerProxy subscriptionManagerProxy) {
        this.mSubscriptionManagerProxy = subscriptionManagerProxy;
    }

    public void setTelephonyManagerProxy(TelephonyManagerProxy telephonyManagerProxy) {
        this.mTelephonyManagerProxy = telephonyManagerProxy;
    }

    public void setPhoneFactoryProxy(PhoneFactoryProxy phoneFactoryProxy) {
        this.mPhoneFactoryProxy = phoneFactoryProxy;
    }

    public void setDeviceState(DeviceState deviceState) {
        this.mDeviceState = deviceState;
    }

    public void setRadioOnHelper(RadioOnHelper radioOnHelper) {
        this.mRadioOnHelper = radioOnHelper;
    }

    public void setPhoneSwitcherProxy(PhoneSwitcherProxy phoneSwitcherProxy) {
        this.mPhoneSwitcherProxy = phoneSwitcherProxy;
    }

    public void setPhoneNumberUtilsProxy(PhoneNumberUtilsProxy phoneNumberUtilsProxy) {
        this.mPhoneNumberUtilsProxy = phoneNumberUtilsProxy;
    }

    public void setPhoneUtilsProxy(PhoneUtilsProxy phoneUtilsProxy) {
        this.mPhoneUtilsProxy = phoneUtilsProxy;
    }

    public void setDisconnectCauseFactory(DisconnectCauseFactory disconnectCauseFactory) {
        this.mDisconnectCauseFactory = disconnectCauseFactory;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearNormalCallDomainSelectionConnection() {
        DomainSelectionConnection domainSelectionConnection = this.mDomainSelectionConnection;
        if (domainSelectionConnection != null) {
            domainSelectionConnection.finishSelection();
            this.mDomainSelectionConnection = null;
        }
    }

    class StateDisconnectListener extends TelephonyConnection.TelephonyConnectionListener {
        private final CompletableFuture mDisconnectFuture;

        StateDisconnectListener(CompletableFuture completableFuture) {
            this.mDisconnectFuture = completableFuture;
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onStateChanged(Connection connection, int i) {
            TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
            if (telephonyConnection == null || telephonyConnection.getState() != 6) {
                return;
            }
            Log.d(TelephonyConnectionService.LOG_TAG, "Connection " + connection.getTelecomCallId() + " changed to STATE_DISCONNECTED!", new Object[0]);
            this.mDisconnectFuture.complete(Boolean.TRUE);
            telephonyConnection.removeTelephonyConnectionListener(this);
        }
    }

    class OnDisconnectListener extends com.android.internal.telephony.Connection.ListenerBase {
        private final CompletableFuture mFuture;

        OnDisconnectListener(CompletableFuture completableFuture) {
            this.mFuture = completableFuture;
        }

        public void onDisconnect(int i) {
            this.mFuture.complete(Boolean.TRUE);
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.TelephonyConnectionService$13, reason: invalid class name */
    class AnonymousClass13 implements DomainSelectionConnection.DomainSelectionConnectionCallback {
        AnonymousClass13() {
        }

        public void onSelectionTerminated(final int i) {
            TelephonyConnectionService.this.mDomainSelectionMainExecutor.execute(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$13$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSelectionTerminated$0(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSelectionTerminated$0(int i) {
            Log.i(this, "onSelectionTerminated cause=" + i, new Object[0]);
            if (TelephonyConnectionService.this.mEmergencyCallDomainSelectionConnection == null) {
                Log.i(this, "onSelectionTerminated no DomainSelectionConnection", new Object[0]);
                return;
            }
            if ((i == 63 || i == 64) && TelephonyConnectionService.this.mEmergencyConnection != null) {
                if (TelephonyConnectionService.this.mEmergencyConnection.getOriginalConnection() != null) {
                    if (TelephonyConnectionService.this.mEmergencyConnection.getOriginalConnection().getState().isAlive()) {
                        TelephonyConnectionService.this.mEmergencyConnection.hangup(i);
                        return;
                    }
                    return;
                }
                boolean z = i == 64;
                Log.i(this, "onSelectionTerminated permanent=" + z, new Object[0]);
                TelephonyConnection telephonyConnection = TelephonyConnectionService.this.mEmergencyConnection;
                Phone phone = TelephonyConnectionService.this.mEmergencyCallDomainSelectionConnection.getPhone();
                TelephonyConnectionService.this.mEmergencyConnection.removeTelephonyConnectionListener(TelephonyConnectionService.this.mEmergencyConnectionListener);
                TelephonyConnectionService.this.releaseEmergencyCallDomainSelection(true, false);
                TelephonyConnectionService.this.mEmergencyStateTracker.endCall(telephonyConnection);
                TelephonyConnectionService.this.retryOutgoingOriginalConnection(telephonyConnection, phone, z);
                return;
            }
            if (TelephonyConnectionService.this.mEmergencyConnection != null) {
                com.android.internal.telephony.Connection originalConnection = TelephonyConnectionService.this.mEmergencyConnection.getOriginalConnection();
                TelephonyConnectionService telephonyConnectionService = TelephonyConnectionService.this;
                if (originalConnection != null) {
                    telephonyConnectionService.mEmergencyConnection.hangup(i);
                    return;
                }
                EmergencyCallDomainSelectionConnection emergencyCallDomainSelectionConnection = telephonyConnectionService.mEmergencyCallDomainSelectionConnection;
                if (i == -1) {
                    i = emergencyCallDomainSelectionConnection.getDisconnectCause();
                }
                TelephonyConnectionService.this.mEmergencyConnection.setTelephonyConnectionDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(i, emergencyCallDomainSelectionConnection.getPreciseDisconnectCause(), emergencyCallDomainSelectionConnection.getReasonMessage(), emergencyCallDomainSelectionConnection.getPhoneId(), emergencyCallDomainSelectionConnection.getImsReasonInfo(), new FlagsAdapterImpl()));
                TelephonyConnectionService.this.mEmergencyConnection.close();
                TelephonyConnectionService.this.mEmergencyConnection.updateStateDetails();
                TelephonyConnection telephonyConnection2 = TelephonyConnectionService.this.mEmergencyConnection;
                TelephonyConnectionService.this.mEmergencyConnection.removeTelephonyConnectionListener(TelephonyConnectionService.this.mEmergencyConnectionListener);
                TelephonyConnectionService.this.releaseEmergencyCallDomainSelection(true, false);
                TelephonyConnectionService.this.mEmergencyStateTracker.endCall(telephonyConnection2);
            }
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mImsConferenceController = new ImsConferenceController(TelecomAccountRegistry.getInstance(this), this.mTelephonyConnectionServiceProxy, new ImsConference.FeatureFlagProxy() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda5
            @Override // com.android.services.telephony.ImsConference.FeatureFlagProxy
            public final boolean isUsingSinglePartyCallEmulation() {
                return TelephonyConnectionService.m4379$r8$lambda$rQv4uG9WRVI_UAG4syM0thkaTI();
            }
        });
        setTelephonyManagerProxy(new TelephonyManagerProxyImpl(this, getApplicationContext()));
        this.mExpectedComponentName = new ComponentName(this, getClass());
        this.mEmergencyTonePlayer = new EmergencyTonePlayer(this);
        TelecomAccountRegistry.getInstance(this).setTelephonyConnectionService(this);
        this.mHoldTracker = new HoldTracker();
        this.mIsTtyEnabled = this.mDeviceState.isTtyModeEnabled(this);
        this.mDomainSelectionMainExecutor = getApplicationContext().getMainExecutor();
        this.mDomainSelectionResolver = DomainSelectionResolver.getInstance();
        this.mSatelliteController = SatelliteController.getInstance();
        registerReceiver(this.mTtyBroadcastReceiver, new IntentFilter("android.telecom.action.TTY_PREFERRED_MODE_CHANGED"), "android.permission.MODIFY_PHONE_STATE", null, 2);
        MtkTelephonyConnectionServiceUtil.getInstance().setService(this);
    }

    /* JADX INFO: renamed from: $r8$lambda$rQv4uG9WRVI_UAG4sy-M0thkaTI, reason: not valid java name */
    public static /* synthetic */ boolean m4379$r8$lambda$rQv4uG9WRVI_UAG4syM0thkaTI() {
        return true;
    }

    @Override // android.app.Service
    public void onDestroy() {
        MtkTelephonyConnectionServiceUtil.getInstance().unsetService();
        RadioOnHelper radioOnHelper = this.mRadioOnHelper;
        if (radioOnHelper != null) {
            radioOnHelper.cleanup();
        }
        super.onDestroy();
    }

    @Override // android.telecom.ConnectionService, android.app.Service
    public boolean onUnbind(Intent intent) {
        unregisterReceiver(this.mTtyBroadcastReceiver);
        return super.onUnbind(intent);
    }

    private Conference placeOutgoingConference(ConnectionRequest connectionRequest, Connection connection, Phone phone) {
        if (connection instanceof TelephonyConnection) {
            return placeOutgoingConference((TelephonyConnection) connection, phone, connectionRequest);
        }
        return null;
    }

    private Conference placeOutgoingConference(TelephonyConnection telephonyConnection, Phone phone, ConnectionRequest connectionRequest) {
        updatePhoneAccount(telephonyConnection, phone);
        try {
            com.android.internal.telephony.Connection connectionStartConference = phone.startConference(getParticipantsToDial(connectionRequest.getParticipants()), new ImsPhone.ImsDialArgs.Builder().setVideoState(connectionRequest.getVideoState()).setRttTextStream(telephonyConnection.getRttTextStream()).build());
            if (connectionStartConference == null) {
                Log.d(this, "placeOutgoingConference, phone.startConference returned null", new Object[0]);
                telephonyConnection.setDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(43, "conferenceHostConnection is null", phone.getPhoneId()));
                telephonyConnection.clearOriginalConnection();
                telephonyConnection.destroy();
            } else {
                telephonyConnection.setOriginalConnection(connectionStartConference);
            }
            return prepareConference(telephonyConnection, connectionRequest.getAccountHandle());
        } catch (CallStateException e) {
            Log.e((Object) this, (Throwable) e, "placeOutgoingConference, phone.startConference exception: " + e, new Object[0]);
            handleCallStateException(e, telephonyConnection, phone);
            return null;
        }
    }

    Conference prepareConference(Connection connection, PhoneAccountHandle phoneAccountHandle) {
        if (!(connection instanceof TelephonyConnection)) {
            Log.w(this, "prepareConference returning NULL conference", new Object[0]);
            return null;
        }
        TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
        ImsConference imsConference = new ImsConference(TelecomAccountRegistry.getInstance(this), this.mTelephonyConnectionServiceProxy, telephonyConnection, phoneAccountHandle, new ImsConference.FeatureFlagProxy() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda22
            @Override // com.android.services.telephony.ImsConference.FeatureFlagProxy
            public final boolean isUsingSinglePartyCallEmulation() {
                return TelephonyConnectionService.m4381$r8$lambda$tKOjzG6pzlfIdW3nLOqSi5X6k();
            }
        }, ImsConferenceController.getCarrierConfig(telephonyConnection.getPhone()));
        this.mImsConferenceController.addConference(imsConference);
        imsConference.setVideoState(telephonyConnection, telephonyConnection.getVideoState());
        imsConference.setVideoProvider(telephonyConnection, telephonyConnection.getVideoProvider());
        imsConference.setStatusHints(telephonyConnection.getStatusHints());
        imsConference.setAddress(telephonyConnection.getAddress(), telephonyConnection.getAddressPresentation());
        imsConference.setCallerDisplayName(telephonyConnection.getCallerDisplayName(), telephonyConnection.getCallerDisplayNamePresentation());
        imsConference.setParticipants(telephonyConnection.getParticipants());
        return imsConference;
    }

    /* JADX INFO: renamed from: $r8$lambda$tKOjz-G6pzlfIdW3n-LOqSi5X6k, reason: not valid java name */
    public static /* synthetic */ boolean m4381$r8$lambda$tKOjzG6pzlfIdW3nLOqSi5X6k() {
        return true;
    }

    @Override // android.telecom.ConnectionService
    public Conference onCreateIncomingConference(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        Log.i(this, "onCreateIncomingConference, request: " + connectionRequest, new Object[0]);
        Connection connectionOnCreateIncomingConnection = onCreateIncomingConnection(phoneAccountHandle, connectionRequest);
        Log.d(this, "onCreateIncomingConference, connection: %s", connectionOnCreateIncomingConnection);
        if (connectionOnCreateIncomingConnection == null) {
            Log.i(this, "onCreateIncomingConference, implementation returned null connection.", new Object[0]);
            return Conference.createFailedConference(new DisconnectCause(1, "IMPL_RETURNED_NULL_CONNECTION"), connectionRequest.getAccountHandle());
        }
        if (getPhoneForAccount(connectionRequest.getAccountHandle(), false, null) == null) {
            Log.d(this, "onCreateIncomingConference, phone is null", new Object[0]);
            return Conference.createFailedConference(DisconnectCauseUtil.toTelecomDisconnectCause(18, "Phone is null"), connectionRequest.getAccountHandle());
        }
        return prepareConference(connectionOnCreateIncomingConnection, connectionRequest.getAccountHandle());
    }

    @Override // android.telecom.ConnectionService
    public Conference onCreateOutgoingConference(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        Log.i(this, "onCreateOutgoingConference, request: " + connectionRequest, new Object[0]);
        Connection connectionOnCreateOutgoingConnection = onCreateOutgoingConnection(phoneAccountHandle, connectionRequest);
        Log.d(this, "onCreateOutgoingConference, connection: %s", connectionOnCreateOutgoingConnection);
        if (connectionOnCreateOutgoingConnection == null) {
            Log.i(this, "onCreateOutgoingConference, implementation returned null connection.", new Object[0]);
            return Conference.createFailedConference(new DisconnectCause(1, "IMPL_RETURNED_NULL_CONNECTION"), connectionRequest.getAccountHandle());
        }
        Phone phoneForAccount = getPhoneForAccount(connectionRequest.getAccountHandle(), false, null);
        if (phoneForAccount == null) {
            Log.d(this, "onCreateOutgoingConference, phone is null", new Object[0]);
            return Conference.createFailedConference(DisconnectCauseUtil.toTelecomDisconnectCause(18, "Phone is null"), connectionRequest.getAccountHandle());
        }
        return placeOutgoingConference(connectionRequest, connectionOnCreateOutgoingConnection, phoneForAccount);
    }

    private String[] getParticipantsToDial(List list) {
        String[] strArr = new String[list.size()];
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            strArr[i] = ((Uri) it.next()).getSchemeSpecificPart();
            i++;
        }
        return strArr;
    }

    /* JADX WARN: Code duplicated, block: B:48:0x0172  */
    /* JADX WARN: Type inference failed for: r0v10, types: [com.android.services.telephony.TelephonyConnectionService$17] */
    @Override // android.telecom.ConnectionService
    public Connection onCreateOutgoingConnection(PhoneAccountHandle phoneAccountHandle, final ConnectionRequest connectionRequest) {
        String schemeSpecificPart;
        Uri uriFromParts;
        String str;
        ServiceStateTracker serviceStateTracker;
        Phone phoneForAccount;
        Log.i(this, "onCreateOutgoingConnection, request: " + connectionRequest, new Object[0]);
        Uri address = connectionRequest.getAddress();
        boolean zIsAdhocConferenceCall = connectionRequest.isAdhocConferenceCall();
        if (!zIsAdhocConferenceCall && address == null) {
            Log.d(this, "onCreateOutgoingConnection, handle is null", new Object[0]);
            return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(38, "No phone number supplied"));
        }
        String scheme = address.getScheme();
        if ("voicemail".equals(scheme)) {
            Phone phoneForAccount2 = getPhoneForAccount(connectionRequest.getAccountHandle(), false, null);
            if (phoneForAccount2 == null) {
                Log.d(this, "onCreateOutgoingConnection, phone is null", new Object[0]);
                return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(18, "Phone is null"));
            }
            schemeSpecificPart = phoneForAccount2.getVoiceMailNumber();
            if (TextUtils.isEmpty(schemeSpecificPart)) {
                Log.d(this, "onCreateOutgoingConnection, no voicemail number set.", new Object[0]);
                return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(40, "Voicemail scheme provided but no voicemail number set.", phoneForAccount2.getPhoneId()));
            }
            address = Uri.fromParts(GenericURI.TEL, schemeSpecificPart, null);
        } else {
            if (!GenericURI.TEL.equals(scheme) && !GenericURI.SIP.equals(scheme)) {
                Log.d(this, "onCreateOutgoingConnection, Handle %s is not type tel", scheme);
                return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(7, "Handle scheme is not type tel"));
            }
            schemeSpecificPart = address.getSchemeSpecificPart();
            if (TextUtils.isEmpty(schemeSpecificPart)) {
                Log.d(this, "onCreateOutgoingConnection, unable to parse number", new Object[0]);
                return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(7, "Unable to parse number"));
            }
            Phone phoneForAccount3 = getPhoneForAccount(connectionRequest.getAccountHandle(), false, null);
            if (phoneForAccount3 != null && CDMA_ACTIVATION_CODE_REGEX_PATTERN.matcher(schemeSpecificPart).matches()) {
                CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phoneForAccount3.getContext().getSystemService("carrier_config");
                if (carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(phoneForAccount3.getSubId()).getBoolean("disable_cdma_activation_code_bool") : false) {
                    return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(49, "Tried to dial *228", phoneForAccount3.getPhoneId()));
                }
            }
        }
        final boolean zIsLocalEmergencyNumber = MiPhoneAppStub.get().isLocalEmergencyNumber(this, schemeSpecificPart, this.mTelephonyManagerProxy.isCurrentEmergencyNumber(schemeSpecificPart));
        final boolean zIsEmergencyNumberTestNumber = isEmergencyNumberTestNumber(schemeSpecificPart);
        if (!zIsLocalEmergencyNumber || ((phoneForAccount = getPhoneForAccount(connectionRequest.getAccountHandle(), false, address.getSchemeSpecificPart())) != null && phoneForAccount.getServiceState().getState() == 0)) {
            uriFromParts = address;
            str = schemeSpecificPart;
        } else {
            String strConvertToEmergencyNumber = this.mPhoneNumberUtilsProxy.convertToEmergencyNumber(this, schemeSpecificPart);
            if (TextUtils.equals(strConvertToEmergencyNumber, schemeSpecificPart)) {
                uriFromParts = address;
                str = schemeSpecificPart;
            } else {
                Log.i(this, "onCreateOutgoingConnection, converted to emergency number", new Object[0]);
                uriFromParts = Uri.fromParts(GenericURI.TEL, strConvertToEmergencyNumber, null);
                str = strConvertToEmergencyNumber;
            }
        }
        boolean zIsAirplaneModeOn = this.mDeviceState.isAirplaneModeOn(this);
        final Phone phoneForAccount4 = getPhoneForAccount(connectionRequest.getAccountHandle(), zIsLocalEmergencyNumber, uriFromParts == null ? null : uriFromParts.getSchemeSpecificPart());
        ImsPhone imsPhone = phoneForAccount4 != null ? (ImsPhone) phoneForAccount4.getImsPhone() : null;
        final boolean zShouldExitSatelliteModeForEmergencyCall = shouldExitSatelliteModeForEmergencyCall(zIsLocalEmergencyNumber);
        boolean z = (zIsLocalEmergencyNumber && (serviceStateTracker = phoneForAccount4.getServiceStateTracker()) != null && serviceStateTracker.hasPendingRadioPowerOff()) ? true : (zIsLocalEmergencyNumber && (!isRadioOn() || zIsAirplaneModeOn)) || (isRadioPowerDownOnBluetooth() && !(phoneForAccount4 != null && phoneForAccount4.isWifiCallingEnabled()));
        if (this.mSatelliteController.isSatelliteEnabledOrBeingEnabled()) {
            Log.d(this, "onCreateOutgoingConnection,  needToTurnOnRadio=" + z + " needToTurnOffSatellite=" + zShouldExitSatelliteModeForEmergencyCall + " isEmergencyNumber=" + zIsLocalEmergencyNumber, new Object[0]);
            if (!zShouldExitSatelliteModeForEmergencyCall) {
                Log.d(this, "onCreateOutgoingConnection, cannot make call in satellite mode.", new Object[0]);
                return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(82, "SATELLITE_ENABLED"));
            }
        }
        if (this.mDomainSelectionResolver.isDomainSelectionSupported()) {
            int emergencyCallRouting = zIsLocalEmergencyNumber ? getEmergencyCallRouting(phoneForAccount4, str, z) : 0;
            if (zIsLocalEmergencyNumber && emergencyCallRouting != 2) {
                String str2 = str;
                Uri uri = uriFromParts;
                Connection connectionPlaceEmergencyConnection = placeEmergencyConnection(phoneForAccount4, connectionRequest, str2, zIsEmergencyNumberTestNumber, uri, z, emergencyCallRouting);
                str = str2;
                uriFromParts = uri;
                if (connectionPlaceEmergencyConnection != null) {
                    phoneForAccount4 = phoneForAccount4;
                    return connectionPlaceEmergencyConnection;
                }
            }
        }
        if (z || zShouldExitSatelliteModeForEmergencyCall) {
            final int phoneType = phoneForAccount4 == null ? 1 : phoneForAccount4.getPhoneType();
            final Connection telephonyConnection = getTelephonyConnection(connectionRequest, str, zIsLocalEmergencyNumber, uriFromParts, phoneForAccount4);
            final String str3 = str;
            if (phoneForAccount4 instanceof MtkGsmCdmaPhone) {
                MtkGsmCdmaPhone mtkGsmCdmaPhone = (MtkGsmCdmaPhone) phoneForAccount4;
                if (mtkGsmCdmaPhone.shouldProcessSelfActivation()) {
                    notifyEccToSelfActivationSM(mtkGsmCdmaPhone);
                }
            }
            if (this.mRadioOnHelper == null) {
                this.mRadioOnHelper = new RadioOnHelper(this);
            }
            if (zIsLocalEmergencyNumber) {
                this.mIsEmergencyCallPending = true;
                if (this.mDomainSelectionResolver.isDomainSelectionSupported() && (telephonyConnection instanceof TelephonyConnection)) {
                    setNormalRoutingEmergencyConnection((TelephonyConnection) telephonyConnection);
                }
            }
            final Phone phone = phoneForAccount4;
            final Phone phone2 = phoneForAccount4;
            final Uri uri2 = uriFromParts;
            this.mRadioOnHelper.triggerRadioOnAndListen((RadioOnStateListener.Callback) new RadioOnStateListener.Callback() { // from class: com.android.services.telephony.TelephonyConnectionService.17
                public void onComplete(RadioOnStateListener radioOnStateListener, boolean z2) {
                    TelephonyConnectionService.this.handleOnComplete(z2, zIsLocalEmergencyNumber, telephonyConnection, connectionRequest, str3, uri2, phoneType, phone2);
                }

                public boolean onTimeout(Phone phone3, int i, boolean z2) {
                    if (TelephonyConnectionService.this.mDomainSelectionResolver.isDomainSelectionSupported()) {
                        return zIsLocalEmergencyNumber;
                    }
                    return false;
                }

                public boolean isOkToCall(Phone phone3, int i, boolean z2) {
                    boolean z3 = zIsEmergencyNumberTestNumber && phone3.getHalVersion(6).less(RIL.RADIO_HAL_VERSION_1_4);
                    if (TelephonyConnectionService.this.mDomainSelectionResolver.isDomainSelectionSupported()) {
                        Connection connection = telephonyConnection;
                        if (connection != null && connection.getState() == 6) {
                            return true;
                        }
                        if (zIsLocalEmergencyNumber && phone3 == phone && !TelephonyConnectionService.this.isVoiceInService(phone3, z2)) {
                            i = 1;
                            z3 = true;
                        }
                    }
                    if (zIsLocalEmergencyNumber && !z3) {
                        return phone3.getState() == PhoneConstants.State.OFFHOOK || (phone3.getServiceStateTracker().isRadioOn() && !TelephonyConnectionService.this.mSatelliteController.isSatelliteEnabledOrBeingEnabled());
                    }
                    SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(phone3.getSubId());
                    return phone3.getState() == PhoneConstants.State.OFFHOOK || (subscriptionInfoInternal != null && subscriptionInfoInternal.isOpportunistic()) || (i == 0 && !zShouldExitSatelliteModeForEmergencyCall);
                }
            }, zIsLocalEmergencyNumber && !zIsEmergencyNumberTestNumber, phone2, zIsEmergencyNumberTestNumber, this.mDomainSelectionResolver.isDomainSelectionSupported() ? 10000 : 0);
            return telephonyConnection;
        }
        if (!canAddCall() && !zIsLocalEmergencyNumber) {
            Log.d(this, "onCreateOutgoingConnection, cannot add call .", new Object[0]);
            return Connection.createFailedConnection(new DisconnectCause(1, getApplicationContext().getText(R.string.incall_error_cannot_add_call), getApplicationContext().getText(R.string.incall_error_cannot_add_call), "Add call restricted due to ongoing video call"));
        }
        if (!zIsLocalEmergencyNumber) {
            if ((isCallDisallowedDueToSatellite(phoneForAccount4) || isCallDisallowedDueToNtnEligibility(phoneForAccount4)) && (imsPhone == null || !imsPhone.canMakeWifiCall())) {
                Log.d(this, "onCreateOutgoingConnection, cannot make call when device is connected to carrier roaming satellite network", new Object[0]);
                this.mSatelliteController.onNonEmergencyDialerDialogDisplayed(phoneForAccount4);
                return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(82, "CARRIER_ROAMING_SATELLITE_MODE"));
            }
            Connection telephonyConnection2 = getTelephonyConnection(connectionRequest, str, false, uriFromParts, phoneForAccount4);
            if (zIsAdhocConferenceCall) {
                if (telephonyConnection2 instanceof TelephonyConnection) {
                    ((TelephonyConnection) telephonyConnection2).setParticipants(connectionRequest.getParticipants());
                }
                return telephonyConnection2;
            }
            return placeOutgoingConnection(connectionRequest, telephonyConnection2, phoneForAccount4);
        }
        final Connection telephonyConnection3 = getTelephonyConnection(connectionRequest, str, true, uriFromParts, phoneForAccount4);
        if (this.mDomainSelectionResolver.isDomainSelectionSupported() && (telephonyConnection3 instanceof TelephonyConnection)) {
            setNormalRoutingEmergencyConnection((TelephonyConnection) telephonyConnection3);
        }
        CompletableFuture completableFutureCheckAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall = checkAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall(connectionRequest, telephonyConnection3, phoneForAccount4);
        final Consumer consumer = new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$onCreateOutgoingConnection$2(connectionRequest, telephonyConnection3, phoneForAccount4, (Boolean) obj);
            }
        };
        completableFutureCheckAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall.thenRun(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onCreateOutgoingConnection$3(phoneForAccount4, consumer);
            }
        });
        return telephonyConnection3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateOutgoingConnection$2(ConnectionRequest connectionRequest, Connection connection, Phone phone, Boolean bool) {
        Log.i(this, "onCreateOutgoingConn emergency- delayDialForDdsSwitch result = " + bool, new Object[0]);
        placeOutgoingConnection(connectionRequest, connection, phone);
    }

    private CompletableFuture checkAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall(ConnectionRequest connectionRequest, Connection connection, Phone phone) {
        PhoneAccountHandle phoneAccountHandle;
        CompletableFuture completableFutureCompletedFuture = CompletableFuture.completedFuture(null);
        if (!this.mTelephonyManagerProxy.isConcurrentCallsPossible()) {
            return completableFutureCompletedFuture;
        }
        if (connection.getPhoneAccountHandle() == null) {
            phoneAccountHandle = connectionRequest.getAccountHandle();
        } else {
            phoneAccountHandle = connection.getPhoneAccountHandle();
        }
        Log.i(this, "checkAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall disconnectAllCallsOnOtherSubs, phoneAccountExcluded: " + phoneAccountHandle, new Object[0]);
        return delayDialForOtherSubDisconnects(phone, disconnectAllConferenceablesOnOtherSubs(phoneAccountHandle), new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$checkAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall$4((Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall$4(Boolean bool) {
        Log.i(this, "checkAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall delayDialForOtherSubDisconnects success = " + bool, new Object[0]);
    }

    private Connection placeOutgoingConnection(ConnectionRequest connectionRequest, Connection connection, Phone phone) {
        if (connection instanceof TelephonyConnection) {
            if (connectionRequest.getExtras() != null && connectionRequest.getExtras().getBoolean("android.telecom.extra.USE_ASSISTED_DIALING", false)) {
                ((TelephonyConnection) connection).setIsUsingAssistedDialing(Boolean.TRUE);
            }
            placeOutgoingConnection((TelephonyConnection) connection, phone, connectionRequest);
        }
        return connection;
    }

    private boolean isEmergencyNumberTestNumber(String str) {
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        Map currentEmergencyNumberList = this.mTelephonyManagerProxy.getCurrentEmergencyNumberList();
        Iterator it = currentEmergencyNumberList.keySet().iterator();
        while (it.hasNext()) {
            for (EmergencyNumber emergencyNumber : (List) currentEmergencyNumberList.get((Integer) it.next())) {
                if (strStripSeparators.equals(emergencyNumber.getNumber()) && emergencyNumber.isFromSources(32)) {
                    Log.i(this, "isEmergencyNumberTestNumber: " + strStripSeparators + " has been detected as a test emergency number.,", new Object[0]);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isEmergencyCallPending() {
        return this.mIsEmergencyCallPending;
    }

    private boolean isRadioPowerDownOnBluetooth() {
        return this.mDeviceState.isRadioPowerDownAllowedOnBluetooth(this) && this.mDeviceState.getCellOnStatus(this) == 1 && !isRadioOn();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnComplete(boolean z, boolean z2, Connection connection, ConnectionRequest connectionRequest, String str, Uri uri, int i, Phone phone) {
        if (connection.getState() != 6) {
            if (z) {
                if (!z2) {
                    adjustAndPlaceOutgoingConnection(phone, connection, connectionRequest, str, uri, i, false);
                    return;
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new AnonymousClass18(connection, z2, phone, connectionRequest, str, uri, i), 2000L);
                    return;
                }
            }
            if (shouldExitSatelliteModeForEmergencyCall(z2)) {
                Log.w(LOG_TAG, "handleOnComplete, failed to turn off satellite modem", new Object[0]);
                closeOrDestroyConnection(connection, this.mDisconnectCauseFactory.toTelecomDisconnectCause(82, "Failed to turn off satellite modem."));
            } else {
                Log.w(LOG_TAG, "handleOnComplete, failed to turn on radio", new Object[0]);
                closeOrDestroyConnection(connection, this.mDisconnectCauseFactory.toTelecomDisconnectCause(17, "Failed to turn on radio."));
            }
            this.mIsEmergencyCallPending = false;
            return;
        }
        Log.i(this, "Call disconnected before the outgoing call was placed. Skipping call placement.", new Object[0]);
        if (z2) {
            if (this.mDomainSelectionResolver.isDomainSelectionSupported() && this.mDeviceState.isAirplaneModeOn(this)) {
                this.mIsEmergencyCallPending = false;
                return;
            }
            for (Phone phone2 : this.mPhoneFactoryProxy.getPhones()) {
                phone2.setRadioPower(true, false, false, true);
            }
            this.mIsEmergencyCallPending = false;
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.TelephonyConnectionService$18, reason: invalid class name */
    class AnonymousClass18 implements Runnable {
        final /* synthetic */ Uri val$handle;
        final /* synthetic */ boolean val$isEmergencyNumber;
        final /* synthetic */ String val$numberToDial;
        final /* synthetic */ Connection val$originalConnection;
        final /* synthetic */ int val$originalPhoneType;
        final /* synthetic */ Phone val$phone;
        final /* synthetic */ ConnectionRequest val$request;

        AnonymousClass18(Connection connection, boolean z, Phone phone, ConnectionRequest connectionRequest, String str, Uri uri, int i) {
            this.val$originalConnection = connection;
            this.val$isEmergencyNumber = z;
            this.val$phone = phone;
            this.val$request = connectionRequest;
            this.val$numberToDial = str;
            this.val$handle = uri;
            this.val$originalPhoneType = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.val$originalConnection.getState() == 6) {
                Log.i(this, "delay dial Call disconnected before the outgoing call was placed. Skipping call placement.", new Object[0]);
                if (this.val$isEmergencyNumber) {
                    for (Phone phone : TelephonyConnectionService.this.mPhoneFactoryProxy.getPhones()) {
                        phone.setRadioPower(true, false, false, true);
                    }
                    TelephonyConnectionService.this.mIsEmergencyCallPending = false;
                    return;
                }
                return;
            }
            TelephonyConnectionService telephonyConnectionService = TelephonyConnectionService.this;
            final Phone phone2 = this.val$phone;
            final Connection connection = this.val$originalConnection;
            final ConnectionRequest connectionRequest = this.val$request;
            final String str = this.val$numberToDial;
            final Uri uri = this.val$handle;
            final int i = this.val$originalPhoneType;
            telephonyConnectionService.lambda$onCreateOutgoingConnection$3(phone2, new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$18$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$run$0(phone2, connection, connectionRequest, str, uri, i, (Boolean) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(Phone phone, Connection connection, ConnectionRequest connectionRequest, String str, Uri uri, int i, Boolean bool) {
            Log.i(this, "handleOnComplete - delayDialForDdsSwitch result = " + bool, new Object[0]);
            TelephonyConnectionService.this.adjustAndPlaceOutgoingConnection(phone, connection, connectionRequest, str, uri, i, true);
            TelephonyConnectionService.this.mIsEmergencyCallPending = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustAndPlaceOutgoingConnection(Phone phone, Connection connection, ConnectionRequest connectionRequest, String str, Uri uri, int i, boolean z) {
        if (phone.getPhoneType() != i) {
            Connection telephonyConnection = getTelephonyConnection(connectionRequest, str, z, uri, phone);
            if (telephonyConnection instanceof TelephonyConnection) {
                placeOutgoingConnection((TelephonyConnection) telephonyConnection, phone, connectionRequest);
            }
            addExistingConnection(this.mPhoneUtilsProxy.makePstnPhoneAccountHandleWithPrefix(phone, "", z && (SubscriptionManagerService.getInstance().getActiveSubInfoCount(phone.getContext().getOpPackageName(), phone.getContext().getAttributionTag(), true) == 0)), telephonyConnection);
            closeOrDestroyConnection(connection, this.mDisconnectCauseFactory.toTelecomDisconnectCause(44, "Reconnecting outgoing Emergency Call.", phone.getPhoneId()));
            return;
        }
        placeOutgoingConnection((TelephonyConnection) connection, phone, connectionRequest);
    }

    private boolean canAddCall() {
        for (Connection connection : getAllConnections()) {
            if (connection.getExtras() != null && connection.getExtras().getBoolean("android.telecom.extra.DISABLE_ADD_CALL", false)) {
                return false;
            }
        }
        return true;
    }

    private Connection getTelephonyConnection(ConnectionRequest connectionRequest, String str, boolean z, Uri uri, Phone phone) {
        boolean z2;
        int dataNetworkType;
        Connection telephonyConnection = MiTelephonyServiceStub.get().getTelephonyConnection(connectionRequest, getApplicationContext(), str, z, uri, phone, this.mDisconnectCauseFactory, this.mDeviceState, this.mPhoneFactoryProxy);
        if (telephonyConnection != null) {
            return telephonyConnection;
        }
        if (phone == null) {
            Context applicationContext = getApplicationContext();
            if (this.mDeviceState.shouldCheckSimStateBeforeOutgoingCall(this)) {
                IccCard iccCard = this.mPhoneFactoryProxy.getDefaultPhone().getIccCard();
                IccCardConstants.State state = IccCardConstants.State.UNKNOWN;
                if (iccCard != null) {
                    state = iccCard.getState();
                }
                if (state == IccCardConstants.State.PIN_REQUIRED) {
                    String string = applicationContext.getResources().getString(R.string.config_simUnlockUiPackage);
                    String string2 = applicationContext.getResources().getString(R.string.config_simUnlockUiClass);
                    if (string != null && string2 != null) {
                        Intent component = new Intent().setComponent(new ComponentName(string, string2));
                        component.addFlags(268435456);
                        try {
                            applicationContext.startActivityAsUser(component, UserHandle.CURRENT);
                        } catch (ActivityNotFoundException e) {
                            Log.e(this, e, "Unable to find SIM unlock UI activity.", new Object[0]);
                        }
                    }
                    return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(18, "SIM_STATE_PIN_REQUIRED"));
                }
            }
            Log.d(this, "onCreateOutgoingConnection, phone is null", new Object[0]);
            Log.d(this, "onCreateOutgoingConnection, use default phone for cellConnMgr", new Object[0]);
            if (MtkTelephonyConnectionServiceUtil.getInstance().cellConnMgrShowAlerting(PhoneFactory.getDefaultPhone().getSubId())) {
                Log.d(this, "onCreateOutgoingConnection, cellConnMgrShowAlerting() check fail", new Object[0]);
                return Connection.createFailedConnection(DisconnectCauseUtil.toTelecomDisconnectCause(1001, "cellConnMgrShowAlerting() check fail"));
            }
            return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(18, "Phone is null"));
        }
        int state2 = phone.getServiceState().getState();
        if (state2 == 1 && ((dataNetworkType = phone.getServiceState().getDataNetworkType()) == 13 || dataNetworkType == 19 || dataNetworkType == 20)) {
            state2 = phone.getServiceState().getDataRegistrationState();
        }
        boolean zIsWifiCallingEnabled = phone.isWifiCallingEnabled();
        Log.d(this, "WFC: phoneId: " + phone.getPhoneId() + " isWfcEnabled: " + zIsWifiCallingEnabled + " isRadioOn: " + phone.isRadioOn(), new Object[0]);
        if (zIsWifiCallingEnabled) {
            state2 = 0;
        }
        Log.d(this, "Service state:" + state2, new Object[0]);
        boolean zIsAirplaneModeOn = this.mDeviceState.isAirplaneModeOn(this);
        if (zIsAirplaneModeOn && !zIsWifiCallingEnabled && VideoProfile.isVideo(connectionRequest.getVideoState()) && phone.isVideoEnabled()) {
            Log.i(this, "allowVideoCallWhenVoWiFiOffAPMOn:true", new Object[0]);
            state2 = 0;
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z && phone.isInEcm()) {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
            if (!(carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("allow_non_emergency_calls_in_ecm_bool") : true)) {
                return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(34, "Cannot make non-emergency call in ECM mode.", phone.getPhoneId()));
            }
        }
        if (!z) {
            if (zIsAirplaneModeOn && zIsWifiCallingEnabled && MtkTelephonyConnectionServiceUtil.getInstance().shouldDisableAirplaneModeWhenUSSD(str, phone)) {
                Log.d(this, "onCreateOutgoingConnection, should open airplane mode dialog", new Object[0]);
                return Connection.createFailedConnection(DisconnectCauseUtil.toTelecomDisconnectCause(1001, "shouldDisableAirplaneModeWhenUSSD() check fail"));
            }
            if (zIsAirplaneModeOn && (phone instanceof MtkGsmCdmaPhone) && ((MtkGsmCdmaPhone) phone).shouldProcessSelfActivation()) {
                Log.d(this, "[Self-activation] Bypass Dial in flightmode.", new Object[0]);
            } else if (!z2 && MtkTelephonyConnectionServiceUtil.getInstance().cellConnMgrShowAlerting(phone.getSubId())) {
                Log.d(this, "onCreateOutgoingConnection, cellConnMgrShowAlerting() check fail", new Object[0]);
                return Connection.createFailedConnection(DisconnectCauseUtil.toTelecomDisconnectCause(1001, "cellConnMgrShowAlerting() check fail"));
            }
            if (state2 != 0) {
                if (state2 != 1) {
                    if (state2 != 2) {
                        if (state2 == 3) {
                            if (!isRadioPowerDownOnBluetooth()) {
                                if ((phone instanceof MtkGsmCdmaPhone) && ((MtkGsmCdmaPhone) phone).shouldProcessSelfActivation()) {
                                    Log.d(this, "POWER_OF and need to do self activation", new Object[0]);
                                } else {
                                    return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(17, "ServiceState.STATE_POWER_OFF", phone.getPhoneId()));
                                }
                            }
                        } else {
                            Log.d(this, "onCreateOutgoingConnection, unknown service state: %d", Integer.valueOf(state2));
                            return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(43, "Unknown service state " + state2, phone.getPhoneId()));
                        }
                    }
                } else if (phone.isUtEnabled() && str.endsWith("#")) {
                    Log.d(this, "onCreateOutgoingConnection dial for UT", new Object[0]);
                } else {
                    return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(18, "ServiceState.STATE_OUT_OF_SERVICE", phone.getPhoneId()));
                }
            }
        }
        boolean zIsTtyModeEnabled = this.mDeviceState.isTtyModeEnabled(this);
        if (VideoProfile.isVideo(connectionRequest.getVideoState()) && zIsTtyModeEnabled && !z) {
            return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(50, null, phone.getPhoneId()));
        }
        Connection connectionCheckAdditionalOutgoingCallLimits = checkAdditionalOutgoingCallLimits(phone);
        if (connectionCheckAdditionalOutgoingCallLimits != null) {
            return connectionCheckAdditionalOutgoingCallLimits;
        }
        if (blockCallForwardingNumberWhileRoaming(phone, str)) {
            return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(57, "Call forwarding while roaming", phone.getPhoneId()));
        }
        PhoneAccountHandle phoneAccountHandleAdjustAccountHandle = adjustAccountHandle(phone, connectionRequest.getAccountHandle());
        TelephonyConnection telephonyConnectionCreateConnectionFor = createConnectionFor(phone, null, true, phoneAccountHandleAdjustAccountHandle, connectionRequest.getTelecomCallId(), connectionRequest.isAdhocConferenceCall());
        if (telephonyConnectionCreateConnectionFor == null) {
            return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(43, "Invalid phone type", phone.getPhoneId()));
        }
        if (!Objects.equals(connectionRequest.getAccountHandle(), phoneAccountHandleAdjustAccountHandle)) {
            Log.i(this, "onCreateOutgoingConnection, update phoneAccountHandle, accountHandle = " + phoneAccountHandleAdjustAccountHandle, new Object[0]);
            telephonyConnectionCreateConnectionFor.setPhoneAccountHandle(phoneAccountHandleAdjustAccountHandle);
        }
        telephonyConnectionCreateConnectionFor.setPhone(phone);
        telephonyConnectionCreateConnectionFor.setAddress(uri, 1);
        telephonyConnectionCreateConnectionFor.setTelephonyConnectionInitializing();
        telephonyConnectionCreateConnectionFor.setTelephonyVideoState(connectionRequest.getVideoState());
        telephonyConnectionCreateConnectionFor.setRttTextStream(connectionRequest.getRttTextStream());
        telephonyConnectionCreateConnectionFor.setTtyEnabled(zIsTtyModeEnabled);
        telephonyConnectionCreateConnectionFor.setIsAdhocConferenceCall(connectionRequest.isAdhocConferenceCall());
        telephonyConnectionCreateConnectionFor.setParticipants(connectionRequest.getParticipants());
        MiTelephonyServiceStub.get().updateConnectionAddress(connectionRequest, telephonyConnectionCreateConnectionFor, uri, phone, str, z);
        return telephonyConnectionCreateConnectionFor;
    }

    @Override // android.telecom.ConnectionService
    public Connection onCreateIncomingConnection(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        boolean z;
        Log.i(this, "onCreateIncomingConnection, request: " + connectionRequest, new Object[0]);
        PhoneAccountHandle accountHandle = connectionRequest.getAccountHandle();
        if (accountHandle == null || !"E".equals(accountHandle.getId())) {
            z = false;
        } else {
            Log.i(this, "Emergency PhoneAccountHandle is being used for incoming call... Treat as an Emergency Call.", new Object[0]);
            z = true;
        }
        String string = null;
        Phone phoneForAccount = getPhoneForAccount(accountHandle, z, connectionRequest.getAddress() == null ? null : connectionRequest.getAddress().getSchemeSpecificPart());
        if (phoneForAccount == null) {
            return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(36, "Phone is null"));
        }
        Bundle extras = connectionRequest.getExtras();
        if (extras.containsKey("android.telecom.extra.CALL_DISCONNECT_MESSAGE")) {
            string = extras.getString("android.telecom.extra.CALL_DISCONNECT_MESSAGE");
            Log.i(this, "onCreateIncomingConnection Disconnect message " + string, new Object[0]);
        }
        Call ringingCall = phoneForAccount.getRingingCall();
        if (!ringingCall.getState().isRinging() || (string != null && string.equals("Call dropped by lower layers"))) {
            Log.i(this, "onCreateIncomingConnection, no ringing call", new Object[0]);
            Connection connectionCreateFailedConnection = Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(1, "Found no ringing call", phoneForAccount.getPhoneId()));
            long j = extras.getLong("android.telephony.extra.CALL_CREATED_EPOCH_TIME_MILLIS");
            if (j != 0) {
                Log.i(this, "onCreateIncomingConnection. Set connect time info.", new Object[0]);
                connectionCreateFailedConnection.setConnectTimeMillis(j);
            }
            Uri uri = (Uri) extras.getParcelable("android.telecom.extra.INCOMING_CALL_ADDRESS");
            if (uri != null) {
                Log.i(this, "onCreateIncomingConnection. Set caller id info.", new Object[0]);
                connectionCreateFailedConnection.setAddress(uri, 1);
            }
            return connectionCreateFailedConnection;
        }
        com.android.internal.telephony.Connection latestConnection = ringingCall.getLatestConnection();
        if (isOriginalConnectionKnown(latestConnection)) {
            Log.i(this, "onCreateIncomingConnection, original connection already registered", new Object[0]);
            return Connection.createCanceledConnection();
        }
        TelephonyConnection telephonyConnectionCreateConnectionFor = createConnectionFor(phoneForAccount, latestConnection, false, connectionRequest.getAccountHandle(), connectionRequest.getTelecomCallId(), connectionRequest.isAdhocConferenceCall());
        handleIncomingRtt(connectionRequest, latestConnection);
        if (telephonyConnectionCreateConnectionFor == null) {
            return Connection.createCanceledConnection();
        }
        telephonyConnectionCreateConnectionFor.setTtyEnabled(this.mDeviceState.isTtyModeEnabled(getApplicationContext()));
        return telephonyConnectionCreateConnectionFor;
    }

    private void handleIncomingRtt(ConnectionRequest connectionRequest, com.android.internal.telephony.Connection connection) {
        if (connection == null || connection.getPhoneType() != 5) {
            if (connectionRequest.isRequestingRtt()) {
                Log.w(this, "Requesting RTT on non-IMS call, ignoring", new Object[0]);
                return;
            }
            return;
        }
        ImsPhoneConnection imsPhoneConnection = (ImsPhoneConnection) connection;
        if (!connectionRequest.isRequestingRtt()) {
            if (imsPhoneConnection.isRttEnabledForCall()) {
                Log.w(this, "Incoming call requested RTT but we did not get a RttTextStream", new Object[0]);
                return;
            }
            return;
        }
        Log.i(this, "Setting RTT stream on ImsPhoneConnection in case we need it later", new Object[0]);
        imsPhoneConnection.setCurrentRttTextStream(connectionRequest.getRttTextStream());
        if (!imsPhoneConnection.isRttEnabledForCall()) {
            if (connectionRequest.isRequestingRtt()) {
                Log.w(this, "Incoming call processed as RTT but did not come in as one. Ignoring", new Object[0]);
            }
        } else {
            Log.i(this, "Setting the call to be answered with RTT on.", new Object[0]);
            imsPhoneConnection.getImsCall().setAnswerWithRtt();
        }
    }

    public void onCreateConnectionComplete(Connection connection) {
        if (connection instanceof TelephonyConnection) {
            maybeSendInternationalCallEvent((TelephonyConnection) connection);
            MiTelephonyServiceStub.get().onCreateConnectionComplete(this, connection);
            if (this.mTelephonyConferenceController.shouldRecalculate()) {
                this.mTelephonyConferenceController.recalculate();
            }
        }
    }

    @Override // android.telecom.ConnectionService
    public void onCreateIncomingConnectionFailed(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        boolean z;
        Log.i(this, "onCreateIncomingConnectionFailed, request: " + connectionRequest, new Object[0]);
        if (connectionRequest.getExtras().containsKey("android.telecom.extra.CALL_DISCONNECT_MESSAGE")) {
            Log.i(this, "onCreateIncomingConnectionFailed: auto-disconnected,ignoring.", new Object[0]);
            return;
        }
        PhoneAccountHandle accountHandle = connectionRequest.getAccountHandle();
        if (accountHandle == null || !"E".equals(accountHandle.getId())) {
            z = false;
        } else {
            Log.w(this, "onCreateIncomingConnectionFailed:Emergency call failed... ", new Object[0]);
            z = true;
        }
        Phone phoneForAccount = getPhoneForAccount(accountHandle, z, connectionRequest.getAddress() == null ? null : connectionRequest.getAddress().getSchemeSpecificPart());
        if (phoneForAccount == null) {
            Log.w(this, "onCreateIncomingConnectionFailed: can not find corresponding phone.", new Object[0]);
            return;
        }
        Call ringingCall = phoneForAccount.getRingingCall();
        if (!ringingCall.getState().isRinging()) {
            Log.w(this, "onCreateIncomingConnectionFailed, no ringing call found for failed call", new Object[0]);
            return;
        }
        com.android.internal.telephony.Connection latestConnection = ringingCall.getState() == Call.State.WAITING ? ringingCall.getLatestConnection() : ringingCall.getEarliestConnection();
        TelephonyConnection connectionForOriginalConnection = getConnectionForOriginalConnection(latestConnection);
        if (connectionForOriginalConnection != null) {
            Log.w(this, "onCreateIncomingConnectionFailed, original connection already registered. Hanging it up.", new Object[0]);
            connectionForOriginalConnection.onAbort();
            return;
        }
        TelephonyConnection telephonyConnectionCreateConnectionFor = createConnectionFor(phoneForAccount, latestConnection, false, connectionRequest.getAccountHandle(), connectionRequest.getTelecomCallId());
        if (telephonyConnectionCreateConnectionFor == null) {
            Log.w(this, "onCreateIncomingConnectionFailed, TelephonyConnection created as null, ignoring.", new Object[0]);
        } else {
            telephonyConnectionCreateConnectionFor.onReject();
        }
    }

    public void onCreateConferenceComplete(Conference conference) {
        if (conference instanceof ImsConference) {
            maybeSendInternationalCallEvent((TelephonyConnection) ((ImsConference) conference).getConferenceHost());
        }
    }

    @Override // android.telecom.ConnectionService
    public void onCreateIncomingConferenceFailed(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        Log.i(this, "onCreateIncomingConferenceFailed, request: " + connectionRequest, new Object[0]);
        onCreateIncomingConnectionFailed(phoneAccountHandle, connectionRequest);
    }

    public Connection onCreateUnknownConnection(PhoneAccountHandle phoneAccountHandle, ConnectionRequest connectionRequest) {
        boolean z;
        com.android.internal.telephony.Connection connection;
        com.android.internal.telephony.Connection connectionById;
        Log.i(this, "onCreateUnknownConnection, request: " + connectionRequest, new Object[0]);
        PhoneAccountHandle accountHandle = connectionRequest.getAccountHandle();
        if (accountHandle == null || !"E".equals(accountHandle.getId())) {
            z = false;
        } else {
            Log.i(this, "Emergency PhoneAccountHandle is being used for unknown call... Treat as an Emergency Call.", new Object[0]);
            z = true;
        }
        Phone phoneForAccount = getPhoneForAccount(accountHandle, z, connectionRequest.getAddress() == null ? null : connectionRequest.getAddress().getSchemeSpecificPart());
        if (phoneForAccount == null) {
            return Connection.createFailedConnection(this.mDisconnectCauseFactory.toTelecomDisconnectCause(36, "Phone is null"));
        }
        Bundle extras = connectionRequest.getExtras();
        ArrayList arrayList = new ArrayList();
        if (phoneForAccount.getImsPhone() != null && extras != null && extras.containsKey("android.telephony.ImsExternalCallTracker.extra.EXTERNAL_CALL_ID")) {
            ImsExternalCallTracker externalCallTracker = phoneForAccount.getImsPhone().getExternalCallTracker();
            int i = extras.getInt("android.telephony.ImsExternalCallTracker.extra.EXTERNAL_CALL_ID", -1);
            if (externalCallTracker != null && (connectionById = externalCallTracker.getConnectionById(i)) != null) {
                arrayList.add(connectionById);
            }
        }
        if (arrayList.isEmpty()) {
            Call ringingCall = phoneForAccount.getRingingCall();
            if (ringingCall.hasConnections()) {
                arrayList.addAll(ringingCall.getConnections());
            }
            Call foregroundCall = phoneForAccount.getForegroundCall();
            Call.State state = foregroundCall.getState();
            Call.State state2 = Call.State.DISCONNECTED;
            if (state != state2 && foregroundCall.hasConnections()) {
                arrayList.addAll(foregroundCall.getConnections());
            }
            if (phoneForAccount.getImsPhone() != null) {
                Call foregroundCall2 = phoneForAccount.getImsPhone().getForegroundCall();
                if (foregroundCall2.getState() != state2 && foregroundCall2.hasConnections()) {
                    arrayList.addAll(foregroundCall2.getConnections());
                }
            }
            if (phoneForAccount.getBackgroundCall().hasConnections()) {
                arrayList.addAll(phoneForAccount.getBackgroundCall().getConnections());
            }
        }
        int size = arrayList.size();
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                connection = null;
                break;
            }
            Object obj = arrayList.get(i2);
            i2++;
            com.android.internal.telephony.Connection connection2 = (com.android.internal.telephony.Connection) obj;
            if (!isOriginalConnectionKnown(connection2)) {
                Log.d(this, "onCreateUnknownConnection: conn = " + connection2, new Object[0]);
                connection = connection2;
                break;
            }
        }
        if (connection == null) {
            Log.i(this, "onCreateUnknownConnection, did not find previously unknown connection.", new Object[0]);
            return Connection.createCanceledConnection();
        }
        TelephonyConnection telephonyConnectionCreateConnectionFor = createConnectionFor(phoneForAccount, connection, !connection.isIncoming(), connectionRequest.getAccountHandle(), connectionRequest.getTelecomCallId());
        if (telephonyConnectionCreateConnectionFor == null) {
            return Connection.createCanceledConnection();
        }
        telephonyConnectionCreateConnectionFor.updateState();
        if (this.mTelephonyConferenceController.shouldRecalculate()) {
            this.mTelephonyConferenceController.recalculate();
        }
        return telephonyConnectionCreateConnectionFor;
    }

    @Override // android.telecom.ConnectionService
    public void onConference(Connection connection, Connection connection2) {
        if (connection instanceof TelephonyConnection) {
            ((TelephonyConnection) connection).performConference(connection2);
        } else if (connection2 instanceof TelephonyConnection) {
            ((TelephonyConnection) connection2).performConference(connection);
        } else {
            Log.w(this, "onConference - cannot merge connections Connection1: %s, Connection2: %2", connection, connection2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onConnectionAdded(Connection connection) {
        if ((connection instanceof Holdable) && !isExternalConnection(connection)) {
            this.mHoldTracker.addHoldable((Holdable) connection);
        }
        if (connection instanceof TelephonyConnection) {
            MtkTelephonyConnectionServiceUtil.getInstance().forceSuppMessageUpdate((TelephonyConnection) connection);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onConnectionRemoved(Connection connection) {
        if ((connection instanceof Holdable) && !isExternalConnection(connection)) {
            this.mHoldTracker.removeHoldable((Holdable) connection);
        }
        MiTelephonyServiceStub.get().onConnectionRemoved(connection);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onConferenceAdded(Conference conference) {
        if (conference instanceof Holdable) {
            this.mHoldTracker.addHoldable((Holdable) conference);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onConferenceRemoved(Conference conference) {
        if (conference instanceof Holdable) {
            this.mHoldTracker.removeHoldable((Holdable) conference);
        }
    }

    private boolean isExternalConnection(Connection connection) {
        return (connection.getConnectionProperties() & 16) == 16;
    }

    private boolean blockCallForwardingNumberWhileRoaming(Phone phone, String str) {
        String[] stringArray;
        if (phone != null && !TextUtils.isEmpty(str) && phone.getServiceState().getRoaming()) {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
            if (carrierConfigManager == null) {
                stringArray = null;
            } else {
                if (carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("support_ims_call_forwarding_while_roaming_bool", true) && useImsForAudioOnlyCall(phone)) {
                    return false;
                }
                stringArray = carrierConfigManager.getConfigForSubId(phone.getSubId()).getStringArray("call_forwarding_blocks_while_roaming_string_array");
            }
            if (stringArray != null) {
                for (String str2 : stringArray) {
                    if (str.startsWith(str2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean useImsForAudioOnlyCall(Phone phone) {
        Phone imsPhone = phone.getImsPhone();
        if (imsPhone != null) {
            return (imsPhone.isVoiceOverCellularImsEnabled() || imsPhone.isWifiCallingEnabled()) && imsPhone.getServiceState().getState() == 0;
        }
        return false;
    }

    private boolean isRadioOn() {
        boolean zIsRadioOn = false;
        for (Phone phone : this.mPhoneFactoryProxy.getPhones()) {
            zIsRadioOn |= phone.isRadioOn();
        }
        return zIsRadioOn;
    }

    private boolean shouldExitSatelliteModeForEmergencyCall(boolean z) {
        if (!this.mSatelliteController.isSatelliteEnabledOrBeingEnabled() || !z || !shouldTurnOffNonEmergencyNbIotNtnSessionForEmergencyCall()) {
            return false;
        }
        if (this.mSatelliteController.isDemoModeEnabled() || !this.mSatelliteController.getRequestIsEmergency()) {
            return true;
        }
        int selectedSatelliteSubId = this.mSatelliteController.getSelectedSatelliteSubId();
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(selectedSatelliteSubId);
        if (subscriptionInfoInternal == null) {
            loge("satellite is/being enabled, but satellite sub " + selectedSatelliteSubId + " is null");
            return false;
        }
        if (subscriptionInfoInternal.getOnlyNonTerrestrialNetwork() == 1) {
            return getTurnOffOemEnabledSatelliteDuringEmergencyCall();
        }
        return this.mSatelliteController.shouldTurnOffCarrierSatelliteForEmergencyCall();
    }

    private Pair makeCachedConnectionPhonePair(TelephonyConnection telephonyConnection) {
        return new Pair(new WeakReference(telephonyConnection), new LinkedList(Arrays.asList(this.mPhoneFactoryProxy.getPhones())));
    }

    private void updateCachedConnectionPhonePair(TelephonyConnection telephonyConnection, Phone phone, boolean z) {
        Pair pair = this.mEmergencyRetryCache;
        if (pair == null) {
            Log.i(this, "updateCachedConnectionPhonePair, cache is null. Generating new cache", new Object[0]);
            this.mEmergencyRetryCache = makeCachedConnectionPhonePair(telephonyConnection);
        } else if (((WeakReference) pair.first).get() != telephonyConnection) {
            Log.i(this, "updateCachedConnectionPhonePair, cache is stale. Regenerating.", new Object[0]);
            this.mEmergencyRetryCache = makeCachedConnectionPhonePair(telephonyConnection);
        }
        Queue queue = (Queue) this.mEmergencyRetryCache.second;
        Phone defaultPhone = phone.getDefaultPhone();
        if (defaultPhone == null) {
            return;
        }
        queue.remove(defaultPhone);
        Log.i(this, "updateCachedConnectionPhonePair, isPermanentFailure:" + z, new Object[0]);
        if (z) {
            return;
        }
        queue.offer(defaultPhone);
    }

    public void retryOutgoingOriginalConnection(TelephonyConnection telephonyConnection, Phone phone, boolean z) {
        if (MiTelephonyServiceStub.get().retryOutgoingOriginalConnection(telephonyConnection, this, this.mRadioOnHelper)) {
            return;
        }
        int phoneId = phone == null ? -1 : phone.getPhoneId();
        updateCachedConnectionPhonePair(telephonyConnection, phone, z);
        Object obj = this.mEmergencyRetryCache.second;
        Phone phone2 = obj != null ? (Phone) ((Queue) obj).peek() : null;
        if (phone2 != null) {
            int videoState = telephonyConnection.getVideoState();
            Bundle extras = telephonyConnection.getExtras();
            Log.i(this, "retryOutgoingOriginalConnection, redialing on Phone Id: " + phone2, new Object[0]);
            telephonyConnection.clearOriginalConnection();
            if (phoneId != phone2.getPhoneId()) {
                if (this.mTelephonyManagerProxy.getMaxNumberOfSimultaneouslyActiveSims() < 2) {
                    disconnectAllCallsOnOtherSubs(this.mPhoneUtilsProxy.makePstnPhoneAccountHandle(phone2));
                }
                updatePhoneAccount(telephonyConnection, phone2);
            }
            if (this.mDomainSelectionResolver.isDomainSelectionSupported()) {
                onEmergencyRedial(telephonyConnection, phone2, false);
                return;
            } else {
                placeOutgoingConnection(telephonyConnection, phone2, videoState, extras);
                return;
            }
        }
        Log.i(this, "retryOutgoingOriginalConnection, no more Phones to use. Disconnecting.", new Object[0]);
        closeOrDestroyConnection(telephonyConnection, new DisconnectCause(1));
    }

    private void updatePhoneAccount(TelephonyConnection telephonyConnection, Phone phone) {
        PhoneAccountHandle phoneAccountHandleMakePstnPhoneAccountHandle = this.mPhoneUtilsProxy.makePstnPhoneAccountHandle(phone);
        Log.i(this, "updatePhoneAccount setPhoneAccountHandle, account = " + phoneAccountHandleMakePstnPhoneAccountHandle, new Object[0]);
        telephonyConnection.setPhoneAccountHandle(phoneAccountHandleMakePstnPhoneAccountHandle);
    }

    protected void placeOutgoingConnection(TelephonyConnection telephonyConnection, Phone phone, ConnectionRequest connectionRequest) {
        placeOutgoingConnection(telephonyConnection, phone, connectionRequest.getVideoState(), connectionRequest.getExtras());
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void placeOutgoingConnection(final TelephonyConnection telephonyConnection, Phone phone, final int i, final Bundle bundle) {
        String schemeSpecificPart;
        Phone phone2;
        String str;
        Bundle bundle2;
        int i2;
        Phone phone3;
        Phone phone4;
        final Phone phone5 = phone;
        final Bundle bundle3 = bundle;
        if (telephonyConnection.getAddress() != null) {
            schemeSpecificPart = telephonyConnection.getAddress().getSchemeSpecificPart();
        } else {
            schemeSpecificPart = "";
        }
        if (showDataDialog(phone5, schemeSpecificPart)) {
            telephonyConnection.setDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(39, "UT is not available"));
            telephonyConnection.close();
            return;
        }
        boolean zIsCurrentEmergencyNumber = this.mTelephonyManagerProxy.isCurrentEmergencyNumber(schemeSpecificPart);
        if (bundle3 != null && bundle3.containsKey("android.telecom.extra.OUTGOING_PICTURE")) {
            CallComposerPictureManager.getInstance(phone5.getContext(), phone5.getSubId()).storeUploadedPictureToCallLog(((ParcelUuid) bundle3.getParcelable("android.telecom.extra.OUTGOING_PICTURE")).getUuid(), new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$placeOutgoingConnection$5(telephonyConnection, (Uri) obj);
                }
            });
        }
        final com.android.internal.telephony.Connection connectionDial = null;
        Phone phone6 = phone5;
        if (phone5 != null) {
            try {
                boolean zIsCurrentEmergencyNumber2 = this.mTelephonyManagerProxy.isCurrentEmergencyNumber(PhoneNumberUtils.extractNetworkPortionAlt(schemeSpecificPart));
                Log.i(this, "placeOutgoingConnection isEmergency=" + zIsCurrentEmergencyNumber2, new Object[0]);
                if (this.mEmergencyConnection == telephonyConnection && !zIsCurrentEmergencyNumber2) {
                    Log.i(this, "placeOutgoingConnection: Dialed emergency number is recognized as a non-emergency number", new Object[0]);
                    onLocalHangup(telephonyConnection);
                    telephonyConnection.unregisterForCallEvents();
                    telephonyConnection.setTelephonyConnectionDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(43, "Emergency call is placed using a non-emergency number", phone5.getPhoneId()));
                    telephonyConnection.close();
                    return;
                }
                if (zIsCurrentEmergencyNumber2) {
                    handleEmergencyCallStartedForSatelliteSOSMessageRecommender(telephonyConnection, phone);
                    try {
                        if (getAllConnections().isEmpty()) {
                            phone4 = phone5;
                        } else {
                            try {
                                if (!shouldHoldForEmergencyCall(phone5)) {
                                    phone5 = phone5;
                                    for (Connection connection : getAllConnections()) {
                                        if (!connection.equals(telephonyConnection) && connection.getState() != 6 && (connection instanceof TelephonyConnection)) {
                                            ((TelephonyConnection) connection).hangup(80);
                                            delayDialForAllConnectionDisconnected(phone5, connection, new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda16
                                                @Override // java.util.function.Consumer
                                                public final void accept(Object obj) {
                                                    this.f$0.lambda$placeOutgoingConnection$6(telephonyConnection, phone5, i, bundle3, (Boolean) obj);
                                                }
                                            });
                                            return;
                                        }
                                        phone5 = phone5;
                                        bundle3 = bundle;
                                        Log.e((Object) this, (Throwable) e, "placeOutgoingConnection, phone.dial exception: " + e, new Object[0]);
                                        if (this.mDomainSelectionResolver.isDomainSelectionSupported()) {
                                            onLocalHangup(telephonyConnection);
                                        }
                                        telephonyConnection.unregisterForCallEvents();
                                        handleCallStateException(e, telephonyConnection, phone2);
                                        return;
                                    }
                                    phone4 = phone5;
                                    for (Conference conference : getAllConferences()) {
                                        if (conference.getState() != 6) {
                                            conference.onDisconnect();
                                        }
                                    }
                                } else {
                                    phone4 = phone5;
                                    if (!isVideoCallHoldAllowed(phone4)) {
                                        for (Connection connection2 : getAllConnections()) {
                                            if (!connection2.equals(telephonyConnection) && connection2.getState() == 4 && VideoProfile.isVideo(connection2.getVideoState()) && (connection2 instanceof TelephonyConnection)) {
                                                ((TelephonyConnection) connection2).hangup(80);
                                                break;
                                            }
                                        }
                                    }
                                }
                            } catch (CallStateException e) {
                                e = e;
                                phone2 = phone5;
                            }
                        }
                        if (this.mDomainSelectionResolver.isDomainSelectionSupported()) {
                            this.mIsEmergencyCallPending = false;
                            if (telephonyConnection == this.mNormalRoutingEmergencyConnection) {
                                if (getEmergencyCallRouting(phone4, schemeSpecificPart, false) != 2) {
                                    Log.i(this, "placeOutgoingConnection dynamic routing", new Object[0]);
                                    setNormalRoutingEmergencyConnection(null);
                                    this.mForcedEmergencyRoutingConnection = telephonyConnection;
                                    onEmergencyRedial(telephonyConnection, phone4, true);
                                    return;
                                }
                                Log.i(this, "placeOutgoingConnection normal routing number", new Object[0]);
                                final String str2 = schemeSpecificPart;
                                final Phone phone7 = phone4;
                                try {
                                    this.mEmergencyStateTracker.startNormalRoutingEmergencyCall(phone7, telephonyConnection, new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda17
                                        @Override // java.util.function.Consumer
                                        public final void accept(Object obj) {
                                            this.f$0.lambda$placeOutgoingConnection$7(telephonyConnection, str2, phone7, i, bundle, (Boolean) obj);
                                        }
                                    });
                                    telephonyConnection.addTelephonyConnectionListener(this.mNormalRoutingEmergencyConnectionListener);
                                    return;
                                } catch (CallStateException e2) {
                                    e = e2;
                                    phone2 = phone7;
                                }
                            }
                        }
                        bundle2 = bundle;
                        str = schemeSpecificPart;
                        phone3 = phone4;
                        i2 = i;
                    } catch (CallStateException e3) {
                        e = e3;
                        phone2 = bundle3;
                    }
                } else {
                    str = schemeSpecificPart;
                    bundle2 = bundle3;
                    i2 = i;
                    if (handleOutgoingCallConnection(str, telephonyConnection, phone5, i2)) {
                        phone3 = phone5;
                        return;
                    }
                }
                if (zIsCurrentEmergencyNumber && SystemProperties.get("persist.vendor.operator.optr", "OM").equals("OP12")) {
                    Context applicationContext = getApplicationContext();
                    if (applicationContext != null) {
                        TetheringManager tetheringManager = (TetheringManager) applicationContext.getSystemService("tethering");
                        if (tetheringManager != null) {
                            Log.i(this, "placeOutgoingConnection, stop hotspot before ECC dial", new Object[0]);
                            tetheringManager.stopTethering(0);
                        } else {
                            Log.i(this, "placeOutgoingConnection, cannot stop wifi-hotspot (tethering service null)", new Object[0]);
                        }
                    } else {
                        Log.i(this, "placeOutgoingConnection, cannot stop wifi-hotspot (context null)", new Object[0]);
                    }
                }
                connectionDial = phone3.dial(str, new ImsPhone.ImsDialArgs.Builder().setVideoState(i2).setIntentExtras(bundle2).setRttTextStream(telephonyConnection.getRttTextStream()).build(), new TelephonyConnectionService$$ExternalSyntheticLambda18(telephonyConnection));
                phone6 = phone3;
            } catch (CallStateException e4) {
                e = e4;
                phone2 = phone5;
            }
        }
        if (connectionDial == null) {
            if (phone6.getPhoneType() == 1 || phone6.isUtEnabled()) {
                Log.d(this, "dialed MMI code", new Object[0]);
                int subId = phone6.getSubId();
                Log.d(this, "subId: " + subId, new Object[0]);
                Intent intent = new Intent(this, (Class<?>) MMIDialogActivity.class);
                intent.setFlags(276824064);
                if (SubscriptionManager.isValidSubscriptionId(subId)) {
                    SubscriptionManager.putSubscriptionIdExtra(intent, subId);
                }
                startActivityAsUser(intent, UserHandle.CURRENT);
            }
            Log.d(this, "placeOutgoingConnection, phone.dial returned null", new Object[0]);
            telephonyConnection.unregisterForCallEvents();
            telephonyConnection.setTelephonyConnectionDisconnected(this.mDisconnectCauseFactory.toTelecomDisconnectCause(handleMmiCode(phone6, 43), "Connection is null", phone6.getPhoneId()));
            telephonyConnection.close();
            return;
        }
        if (!getMainThreadHandler().getLooper().isCurrentThread()) {
            Log.w(this, "placeOriginalConnection - Unexpected, this call should always be on the main thread.", new Object[0]);
            getMainThreadHandler().post(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyConnectionService.$r8$lambda$L4ohVkr5oIdNSvb4ptB4xGpBJ5g(telephonyConnection, connectionDial);
                }
            });
        } else {
            telephonyConnection.setOriginalConnection(connectionDial);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$placeOutgoingConnection$5(TelephonyConnection telephonyConnection, Uri uri) {
        if (uri != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelable("android.telecom.extra.PICTURE_URI", uri);
                telephonyConnection.putTelephonyExtras(bundle);
            } catch (Exception e) {
                Log.e(this, e, "Couldn't set picture extra on outgoing call", new Object[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$placeOutgoingConnection$6(TelephonyConnection telephonyConnection, Phone phone, int i, Bundle bundle, Boolean bool) {
        Log.d(this, "delayEmergencyDialForConfereneableDisconnected success = " + bool, new Object[0]);
        if (bool.booleanValue()) {
            placeOutgoingConnection(telephonyConnection, phone, i, bundle);
        } else {
            telephonyConnection.hangup(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$placeOutgoingConnection$7(TelephonyConnection telephonyConnection, String str, Phone phone, int i, Bundle bundle, Boolean bool) {
        Log.i(this, "placeOutgoingConnection normal routing number: result = " + bool, new Object[0]);
        if (telephonyConnection.getState() == 6) {
            Log.i(this, "placeOutgoingConnection reject incoming, dialing canceled", new Object[0]);
            return;
        }
        if (handleOutgoingCallConnection(str, telephonyConnection, phone, i)) {
            return;
        }
        Log.w(this, "placeOriginalConnection - Unexpected, domain selector not available.", new Object[0]);
        onLocalHangup(telephonyConnection);
        try {
            telephonyConnection.setOriginalConnection(phone.dial(str, new ImsPhone.ImsDialArgs.Builder().setVideoState(i).setIntentExtras(bundle).setRttTextStream(telephonyConnection.getRttTextStream()).build(), new TelephonyConnectionService$$ExternalSyntheticLambda18(telephonyConnection)));
        } catch (CallStateException e) {
            telephonyConnection.unregisterForCallEvents();
            handleCallStateException(e, telephonyConnection, phone);
        }
    }

    public static /* synthetic */ void $r8$lambda$L4ohVkr5oIdNSvb4ptB4xGpBJ5g(TelephonyConnection telephonyConnection, com.android.internal.telephony.Connection connection) {
        if (telephonyConnection.getOriginalConnection() == null) {
            telephonyConnection.setOriginalConnection(connection);
        }
    }

    private int handleMmiCode(Phone phone, int i) {
        Log.d(this, "dialed MMI code", new Object[0]);
        int subId = phone.getSubId();
        Log.d(this, "subId: " + subId, new Object[0]);
        Intent intent = new Intent(this, (Class<?>) MMIDialogActivity.class);
        intent.setFlags(276824064);
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            SubscriptionManager.putSubscriptionIdExtra(intent, subId);
        }
        startActivityAsUser(intent, UserHandle.CURRENT);
        return 39;
    }

    private void handleOutgoingCallConnectionByCallDomainSelection(final int i, final Phone phone, final String str, final int i2, final TelephonyConnection telephonyConnection) {
        if (this.mNormalRoutingEmergencyConnection == telephonyConnection) {
            checkAndRejectIncomingCall(phone, new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda44
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$handleOutgoingCallConnectionByCallDomainSelection$9((Boolean) obj);
                }
            }).thenRun(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$handleOutgoingCallConnectionByCallDomainSelection$10(telephonyConnection, i, phone, str, i2);
                }
            });
        } else {
            handleOutgoingCallConnectionByCallDomainSelection(i, phone, str, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleOutgoingCallConnectionByCallDomainSelection$9(Boolean bool) {
        if (bool.booleanValue()) {
            return;
        }
        Log.i(this, "handleOutgoingCallConnectionByCallDomainSelection reject incoming call failed", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleOutgoingCallConnectionByCallDomainSelection$10(TelephonyConnection telephonyConnection, int i, Phone phone, String str, int i2) {
        if (telephonyConnection.getState() == 6) {
            Log.i(this, "handleOutgoingCallConnectionByCallDomainSelection reject incoming, dialing canceled", new Object[0]);
        } else {
            handleOutgoingCallConnectionByCallDomainSelection(i, phone, str, i2);
        }
    }

    private void handleOutgoingCallConnectionByCallDomainSelection(int i, Phone phone, String str, int i2) {
        Log.d(this, "Call Domain Selected : " + i, new Object[0]);
        try {
            Bundle extras = this.mNormalCallConnection.getExtras();
            if (extras == null) {
                extras = new Bundle();
            }
            extras.putInt("dial_domain", i);
            if (phone != null) {
                Log.v(LOG_TAG, "Call dialing. Domain: " + i, new Object[0]);
                ImsPhone.ImsDialArgs imsDialArgsBuild = new ImsPhone.ImsDialArgs.Builder().setVideoState(i2).setIntentExtras(extras).setRttTextStream(this.mNormalCallConnection.getRttTextStream()).setIsWpsCall(PhoneNumberUtils.isWpsCallNumber(str)).build();
                TelephonyConnection telephonyConnection = this.mNormalCallConnection;
                Objects.requireNonNull(telephonyConnection);
                com.android.internal.telephony.Connection connectionDial = phone.dial(str, imsDialArgsBuild, new TelephonyConnectionService$$ExternalSyntheticLambda18(telephonyConnection));
                if (connectionDial == null) {
                    Log.d(this, "placeOutgoingConnection, phone.dial returned null", new Object[0]);
                    int iHandleMmiCode = handleMmiCode(phone, 43);
                    if (this.mNormalCallConnection.getState() != 6) {
                        this.mNormalCallConnection.setTelephonyConnectionDisconnected(this.mDisconnectCauseFactory.toTelecomDisconnectCause(iHandleMmiCode, "Connection is null", phone.getPhoneId()));
                        this.mNormalCallConnection.close();
                    }
                    clearNormalCallDomainSelectionConnection();
                    return;
                }
                this.mNormalCallConnection.setOriginalConnection(connectionDial);
                this.mNormalCallConnection.addTelephonyConnectionListener(this.mNormalCallConnectionListener);
                return;
            }
            Log.w(this, "placeOutgoingCallConnection. Dialing failed. Phone is null", new Object[0]);
            this.mNormalCallConnection.setTelephonyConnectionDisconnected(this.mDisconnectCauseFactory.toTelecomDisconnectCause(43, "Phone is null", phone.getPhoneId()));
            this.mNormalCallConnection.close();
            clearNormalCallDomainSelectionConnection();
            this.mNormalCallConnection = null;
        } catch (Exception e) {
            Log.e(this, e, "Call exception in placeOutgoingCallConnection:" + e, new Object[0]);
            this.mNormalCallConnection.unregisterForCallEvents();
            this.mNormalCallConnection.setTelephonyConnectionDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(43, e.getMessage(), phone.getPhoneId()));
            this.mNormalCallConnection.close();
        } catch (CallStateException e2) {
            Log.e((Object) this, (Throwable) e2, "Call placeOutgoingCallConnection, phone.dial exception: " + e2, new Object[0]);
            if (e2.getError() == 8) {
                Toast.makeText(getApplicationContext(), R.string.fdn_blocked_mmi, 0).show();
            }
            this.mNormalCallConnection.unregisterForCallEvents();
            handleCallStateException(e2, this.mNormalCallConnection, phone);
        }
    }

    private boolean handleOutgoingCallConnection(final String str, final TelephonyConnection telephonyConnection, final Phone phone, final int i) {
        if (!this.mDomainSelectionResolver.isDomainSelectionSupported() || phone == null) {
            return false;
        }
        String strExtractNetworkPortionAlt = PhoneNumberUtils.extractNetworkPortionAlt(PhoneNumberUtils.stripSeparators(str));
        boolean z = (strExtractNetworkPortionAlt.startsWith("*") || strExtractNetworkPortionAlt.startsWith("#")) && strExtractNetworkPortionAlt.endsWith("#");
        boolean zIsSuppServiceCodes = ImsPhoneMmiCode.isSuppServiceCodes(strExtractNetworkPortionAlt, phone);
        boolean z2 = z && !zIsSuppServiceCodes;
        if (z && zIsSuppServiceCodes) {
            Log.v(LOG_TAG, "UT code not handled by call domain selection.", new Object[0]);
            return false;
        }
        int activeCallDomain = getActiveCallDomain(phone.getSubId());
        if (activeCallDomain != 0 && !PhoneNumberUtils.isWpsCallNumber(str)) {
            Log.d(LOG_TAG, "Selecting same domain as ongoing call on same subId", new Object[0]);
            this.mNormalCallConnection = telephonyConnection;
            handleOutgoingCallConnectionByCallDomainSelection(activeCallDomain, phone, str, i, telephonyConnection);
            return true;
        }
        DomainSelectionConnection domainSelectionConnection = this.mDomainSelectionResolver.getDomainSelectionConnection(phone, 1, false);
        this.mDomainSelectionConnection = domainSelectionConnection;
        if (domainSelectionConnection == null) {
            return false;
        }
        String str2 = LOG_TAG;
        Log.d(str2, "Call Connection created", new Object[0]);
        CompletableFuture completableFutureCreateNormalConnection = this.mDomainSelectionConnection.createNormalConnection(new DomainSelectionService.SelectionAttributes.Builder(phone.getPhoneId(), phone.getSubId(), 1).setAddress(Uri.fromParts(GenericURI.TEL, str, null)).setEmergency(false).setVideoCall(VideoProfile.isVideo(i)).build(), this.mCallDomainSelectionConnectionCallback);
        Log.d(str2, "Call Domain selection triggered.", new Object[0]);
        this.mNormalCallConnection = telephonyConnection;
        completableFutureCreateNormalConnection.thenAcceptAsync(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda40
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$handleOutgoingCallConnection$11(phone, str, i, telephonyConnection, (Integer) obj);
            }
        }, this.mDomainSelectionMainExecutor);
        if (z2) {
            Log.v(str2, "PotentialUssdCode. Closing connection with DisconnectCause.DIALED_MMI", new Object[0]);
            telephonyConnection.setTelephonyConnectionDisconnected(this.mDisconnectCauseFactory.toTelecomDisconnectCause(39, "Dialing USSD", phone.getPhoneId()));
            telephonyConnection.close();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleOutgoingCallConnection$11(Phone phone, String str, int i, TelephonyConnection telephonyConnection, Integer num) {
        handleOutgoingCallConnectionByCallDomainSelection(num.intValue(), phone, str, i, telephonyConnection);
    }

    private Connection placeEmergencyConnection(final Phone phone, final ConnectionRequest connectionRequest, final String str, final boolean z, Uri uri, final boolean z2, int i) {
        final Connection telephonyConnection = getTelephonyConnection(connectionRequest, str, true, uri, phone);
        if (telephonyConnection instanceof TelephonyConnection) {
            Log.i(this, "placeEmergencyConnection", new Object[0]);
            this.mIsEmergencyCallPending = true;
            TelephonyConnection telephonyConnection2 = (TelephonyConnection) telephonyConnection;
            this.mEmergencyConnection = telephonyConnection2;
            if (i == 1) {
                this.mForcedEmergencyRoutingConnection = telephonyConnection2;
            }
            handleEmergencyCallStartedForSatelliteSOSMessageRecommender(telephonyConnection2, phone);
        }
        checkAndHoldOrDisconnectCallsOnOtherSubsForEmergencyCall(connectionRequest, telephonyConnection, phone).thenRun(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$placeEmergencyConnection$12(telephonyConnection, phone, connectionRequest, str, z, z2);
            }
        });
        return telephonyConnection;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: placeEmergencyConnectionInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$placeEmergencyConnection$12(final Connection connection, final Phone phone, final ConnectionRequest connectionRequest, final String str, final boolean z, final boolean z2) {
        if (this.mEmergencyConnection == null) {
            Log.i(this, "placeEmergencyConnectionInternal dialing canceled", new Object[0]);
            return;
        }
        if (connection instanceof TelephonyConnection) {
            Log.i(this, "placeEmergencyConnectionInternal", new Object[0]);
            ((TelephonyConnection) connection).addTelephonyConnectionListener(this.mEmergencyConnectionListener);
            if (this.mEmergencyStateTracker == null) {
                this.mEmergencyStateTracker = EmergencyStateTracker.getInstance();
            }
            this.mEmergencyStateTracker.startEmergencyCall(phone, connection, z).thenAccept(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda46
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$placeEmergencyConnectionInternal$13(phone, connection, str, z, connectionRequest, z2, (Integer) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$placeEmergencyConnectionInternal$13(Phone phone, Connection connection, String str, boolean z, ConnectionRequest connectionRequest, boolean z2, Integer num) {
        String str2;
        Log.d(this, "startEmergencyCall-complete result=" + num, new Object[0]);
        if (this.mEmergencyConnection == null) {
            Log.i(this, "startEmergencyCall-complete dialing canceled", new Object[0]);
            return;
        }
        if (num.intValue() == 0) {
            createEmergencyConnection(phone, (TelephonyConnection) connection, str, z, connectionRequest, z2, this.mEmergencyStateTracker.getEmergencyRegistrationResult());
            return;
        }
        if (num.intValue() == 64) {
            this.mEmergencyConnection.removeTelephonyConnectionListener(this.mEmergencyConnectionListener);
            TelephonyConnection telephonyConnection = this.mEmergencyConnection;
            releaseEmergencyCallDomainSelection(true, false);
            retryOutgoingOriginalConnection(telephonyConnection, phone, true);
            return;
        }
        this.mEmergencyConnection = null;
        this.mAlternateEmergencyConnection = null;
        this.mForcedEmergencyRoutingConnection = null;
        if (num.intValue() != 17) {
            str2 = "Couldn't setup emergency call";
        } else {
            str2 = "Failed to turn on radio.";
        }
        TelephonyConnection telephonyConnection2 = (TelephonyConnection) connection;
        telephonyConnection2.setTelephonyConnectionDisconnected(this.mDisconnectCauseFactory.toTelecomDisconnectCause(num.intValue(), str2));
        telephonyConnection2.close();
        this.mIsEmergencyCallPending = false;
    }

    private void createEmergencyConnection(final Phone phone, final TelephonyConnection telephonyConnection, String str, boolean z, final ConnectionRequest connectionRequest, boolean z2, EmergencyRegistrationResult emergencyRegistrationResult) {
        Log.i(this, "createEmergencyConnection", new Object[0]);
        if (phone.getImsPhone() == null) {
            Log.w(this, "createEmergencyConnection no ImsPhone", new Object[0]);
            dialCsEmergencyCall(phone, telephonyConnection, connectionRequest);
            return;
        }
        EmergencyCallDomainSelectionConnection domainSelectionConnection = this.mDomainSelectionResolver.getDomainSelectionConnection(phone, 1, true);
        if (domainSelectionConnection == null) {
            Log.w(this, "createEmergencyConnection - no selectionConnection", new Object[0]);
            dialCsEmergencyCall(phone, telephonyConnection, connectionRequest);
        } else {
            this.mEmergencyCallDomainSelectionConnection = domainSelectionConnection;
            this.mEmergencyCallDomainSelectionConnection.createEmergencyConnection(EmergencyCallDomainSelectionConnection.getSelectionAttributes(phone.getPhoneId(), phone.getSubId(), z2, connectionRequest.getTelecomCallId(), str, z, 0, (ImsReasonInfo) null, emergencyRegistrationResult), this.mEmergencyDomainSelectionConnectionCallback).thenAcceptAsync(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda47
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$createEmergencyConnection$18(connectionRequest, telephonyConnection, phone, (Integer) obj);
                }
            }, this.mDomainSelectionMainExecutor);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createEmergencyConnection$18(final ConnectionRequest connectionRequest, final TelephonyConnection telephonyConnection, final Phone phone, final Integer num) {
        Log.d(this, "createEmergencyConnection-complete result=" + num, new Object[0]);
        if (this.mEmergencyConnection == null) {
            Log.i(this, "createEmergencyConnection-complete dialing canceled", new Object[0]);
            return;
        }
        Bundle extras = connectionRequest.getExtras();
        extras.putInt("dial_domain", num.intValue());
        if (telephonyConnection == this.mAlternateEmergencyConnection) {
            extras.putInt("emergency_routing_update_cause", 1);
        } else if (telephonyConnection == this.mForcedEmergencyRoutingConnection) {
            extras.putInt("emergency_routing_update_cause", 2);
        }
        checkAndRejectIncomingCall(phone, new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda48
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$createEmergencyConnection$14((Boolean) obj);
            }
        }).thenRun(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createEmergencyConnection$17(telephonyConnection, phone, num, connectionRequest);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createEmergencyConnection$14(Boolean bool) {
        if (bool.booleanValue()) {
            return;
        }
        Log.i(this, "createEmergencyConnection reject incoming call failed", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createEmergencyConnection$17(final TelephonyConnection telephonyConnection, final Phone phone, Integer num, final ConnectionRequest connectionRequest) {
        if (telephonyConnection.getState() == 6) {
            Log.i(this, "createEmergencyConnection reject incoming, dialing canceled", new Object[0]);
        } else {
            maybeDisconnectCallsOnOtherDomain(phone, telephonyConnection, num.intValue(), getAllConnections(), getAllConferences(), new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda51
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$createEmergencyConnection$15((Boolean) obj);
                }
            }).thenRun(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda52
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$createEmergencyConnection$16(telephonyConnection, connectionRequest, phone);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createEmergencyConnection$15(Boolean bool) {
        if (bool.booleanValue()) {
            return;
        }
        Log.i(this, "createEmergencyConnection: disconnecting call on other domain failed", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createEmergencyConnection$16(TelephonyConnection telephonyConnection, ConnectionRequest connectionRequest, Phone phone) {
        if (telephonyConnection.getState() == 6) {
            Log.i(this, "createEmergencyConnection: disconnect call on other domain, dialing canceled", new Object[0]);
        } else {
            placeEmergencyConnectionOnSelectedDomain(connectionRequest, telephonyConnection, phone);
        }
    }

    public static CompletableFuture maybeDisconnectCallsOnOtherDomain(final Phone phone, final Connection connection, final int i, Collection collection, Collection collection2, Consumer consumer) {
        List list = collection.stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyConnectionService.m4374$r8$lambda$hMM5R_zVsfZmxBickvjyeLVemw(connection, phone, i, (Connection) obj);
            }
        }).toList();
        List<Conference> list2 = collection2.stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyConnectionService.isConnectionOnOtherDomain(((Conference) obj).getPrimaryConnection(), phone, i);
            }
        }).toList();
        if (list.isEmpty() && list2.isEmpty()) {
            consumer.accept(Boolean.TRUE);
            return CompletableFuture.completedFuture(null);
        }
        Log.i(LOG_TAG, "maybeDisconnectCallsOnOtherDomain: connections=" + list.size() + ", conferences=" + list2.size(), new Object[0]);
        try {
            Iterator it = list.iterator();
            CompletableFuture completableFuture = null;
            while (it.hasNext()) {
                TelephonyConnection telephonyConnection = (TelephonyConnection) ((Connection) it.next());
                if (telephonyConnection.getState() != 6) {
                    if (completableFuture == null) {
                        completableFuture = new CompletableFuture();
                        telephonyConnection.getOriginalConnection().addListener(new OnDisconnectListener(completableFuture));
                    }
                    telephonyConnection.hangup(80);
                }
            }
            for (Conference conference : list2) {
                if (conference.getState() != 6) {
                    conference.onDisconnect();
                }
            }
            if (completableFuture != null) {
                final CompletableFuture completableFuture2 = new CompletableFuture();
                phone.getContext().getMainThreadHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        completableFuture2.complete(Boolean.FALSE);
                    }
                }, 2000L);
                return completableFuture.acceptEitherAsync((CompletionStage) completableFuture2, consumer, phone.getContext().getMainExecutor()).exceptionally(new Function() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda14
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return TelephonyConnectionService.$r8$lambda$AyuFdtkKNjO5uaLRhVevjJa_Uk8((Throwable) obj);
                    }
                });
            }
            consumer.accept(Boolean.TRUE);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            Log.w(LOG_TAG, "maybeDisconnectCallsOnOtherDomain: exception=" + e.getMessage(), new Object[0]);
            consumer.accept(Boolean.FALSE);
            return CompletableFuture.completedFuture(null);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$hMM5R_zVsfZmxBickvjye-LVemw, reason: not valid java name */
    public static /* synthetic */ boolean m4374$r8$lambda$hMM5R_zVsfZmxBickvjyeLVemw(Connection connection, Phone phone, int i, Connection connection2) {
        return !connection2.equals(connection) && isConnectionOnOtherDomain(connection2, phone, i);
    }

    public static /* synthetic */ Void $r8$lambda$AyuFdtkKNjO5uaLRhVevjJa_Uk8(Throwable th) {
        Log.w(LOG_TAG, "maybeDisconnectCallsOnOtherDomain: exceptionally=" + th, new Object[0]);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:17:0x002c  */
    public static boolean isConnectionOnOtherDomain(Connection connection, Phone phone, int i) {
        if (connection instanceof TelephonyConnection) {
            TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
            Phone phone2 = telephonyConnection.getPhone();
            int i2 = 2;
            if (i == 4) {
                i = 2;
            }
            if (phone2 == null || phone2.getSubId() != phone.getSubId()) {
                i2 = 0;
            } else if (telephonyConnection.isGsmCdmaConnection()) {
                i2 = 1;
            } else if (!telephonyConnection.isImsConnection()) {
                i2 = 0;
            }
            if (i2 != 0 && i2 != i) {
                return true;
            }
        }
        return false;
    }

    private void dialCsEmergencyCall(final Phone phone, final TelephonyConnection telephonyConnection, final ConnectionRequest connectionRequest) {
        Log.d(this, "dialCsEmergencyCall", new Object[0]);
        connectionRequest.getExtras().putInt("dial_domain", 1);
        this.mDomainSelectionMainExecutor.execute(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dialCsEmergencyCall$25(phone, telephonyConnection, connectionRequest);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dialCsEmergencyCall$25(final Phone phone, final TelephonyConnection telephonyConnection, final ConnectionRequest connectionRequest) {
        if (this.mEmergencyConnection == null) {
            Log.i(this, "dialCsEmergencyCall dialing canceled", new Object[0]);
        } else {
            checkAndRejectIncomingCall(phone, new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda53
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$dialCsEmergencyCall$23((Boolean) obj);
                }
            }).thenRun(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$dialCsEmergencyCall$24(telephonyConnection, connectionRequest, phone);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dialCsEmergencyCall$23(Boolean bool) {
        if (bool.booleanValue()) {
            return;
        }
        Log.i(this, "dialCsEmergencyCall reject incoming call failed", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dialCsEmergencyCall$24(TelephonyConnection telephonyConnection, ConnectionRequest connectionRequest, Phone phone) {
        if (telephonyConnection.getState() == 6) {
            Log.i(this, "dialCsEmergencyCall reject incoming, dialing canceled", new Object[0]);
        } else {
            placeEmergencyConnectionOnSelectedDomain(connectionRequest, telephonyConnection, phone);
        }
    }

    private void placeEmergencyConnectionOnSelectedDomain(ConnectionRequest connectionRequest, TelephonyConnection telephonyConnection, Phone phone) {
        if (this.mEmergencyConnection == null) {
            Log.i(this, "placeEmergencyConnectionOnSelectedDomain dialing canceled", new Object[0]);
        } else {
            placeOutgoingConnection(connectionRequest, telephonyConnection, phone);
            this.mIsEmergencyCallPending = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseEmergencyCallDomainSelection(boolean z, boolean z2) {
        EmergencyCallDomainSelectionConnection emergencyCallDomainSelectionConnection = this.mEmergencyCallDomainSelectionConnection;
        if (emergencyCallDomainSelectionConnection != null) {
            if (z) {
                emergencyCallDomainSelectionConnection.cancelSelection();
            } else {
                emergencyCallDomainSelectionConnection.finishSelection();
            }
            this.mEmergencyCallDomainSelectionConnection = null;
        }
        this.mIsEmergencyCallPending = false;
        this.mAlternateEmergencyConnection = null;
        this.mForcedEmergencyRoutingConnection = null;
        if (z2) {
            return;
        }
        this.mEmergencyConnection = null;
    }

    public boolean maybeReselectDomain(TelephonyConnection telephonyConnection, ImsReasonInfo imsReasonInfo, boolean z, int i) {
        if (!this.mDomainSelectionResolver.isDomainSelectionSupported()) {
            return false;
        }
        int preciseDisconnectCause = telephonyConnection.getOriginalConnection().getPreciseDisconnectCause();
        Log.i(this, "maybeReselectDomain csCause=" + preciseDisconnectCause + ", psCause=" + imsReasonInfo, new Object[0]);
        if (this.mEmergencyConnection == telephonyConnection) {
            if (this.mEmergencyCallDomainSelectionConnection != null) {
                return maybeReselectDomainForEmergencyCall(telephonyConnection, preciseDisconnectCause, imsReasonInfo, z, i);
            }
            Log.i(this, "maybeReselectDomain endCall()", new Object[0]);
            telephonyConnection.removeTelephonyConnectionListener(this.mEmergencyConnectionListener);
            releaseEmergencyCallDomainSelection(false, false);
            this.mEmergencyStateTracker.endCall(telephonyConnection);
            return false;
        }
        if (imsReasonInfo != null) {
            int code = imsReasonInfo.getCode();
            int extraCode = imsReasonInfo.getExtraCode();
            if (code == 1514 || (code == 146 && extraCode == 4 && this.mNormalRoutingEmergencyConnection != telephonyConnection)) {
                telephonyConnection.removeTelephonyConnectionListener(this.mNormalCallConnectionListener);
                clearNormalCallDomainSelectionConnection();
                this.mNormalCallConnection = null;
                this.mAlternateEmergencyConnection = telephonyConnection;
                onEmergencyRedial(telephonyConnection, telephonyConnection.getPhone().getDefaultPhone(), false);
                return true;
            }
        }
        return maybeReselectDomainForNormalCall(telephonyConnection, imsReasonInfo, z, i);
    }

    private boolean maybeReselectDomainForEmergencyCall(final TelephonyConnection telephonyConnection, int i, ImsReasonInfo imsReasonInfo, boolean z, int i2) {
        int disconnectCause = i2;
        Log.i(this, "maybeReselectDomainForEmergencyCall csCause=" + i + ", psCause=" + imsReasonInfo + ", showPreciseCause=" + z + ", overrideCause=" + disconnectCause, new Object[0]);
        boolean z2 = (telephonyConnection.getOriginalConnection() == null || telephonyConnection.getOriginalConnection().getDisconnectCause() != 3 || disconnectCause == 63 || disconnectCause == 64) ? false : true;
        if (telephonyConnection.getOriginalConnection() != null && !z2 && telephonyConnection.getOriginalConnection().getDisconnectCause() != 17) {
            if (disconnectCause == -1) {
                disconnectCause = telephonyConnection.getOriginalConnection().getDisconnectCause();
            }
            this.mEmergencyCallDomainSelectionConnection.setDisconnectCause(disconnectCause, z ? i : -1, telephonyConnection.getOriginalConnection().getVendorDisconnectCause());
            CompletableFuture completableFutureReselectDomain = this.mEmergencyCallDomainSelectionConnection.reselectDomain(EmergencyCallDomainSelectionConnection.getSelectionAttributes(telephonyConnection.getPhone().getPhoneId(), telephonyConnection.getPhone().getSubId(), false, telephonyConnection.getTelecomCallId(), telephonyConnection.getAddress().getSchemeSpecificPart(), false, i, imsReasonInfo, (EmergencyRegistrationResult) null));
            final Phone defaultPhone = telephonyConnection.getPhone().getDefaultPhone();
            if (completableFutureReselectDomain != null) {
                completableFutureReselectDomain.thenAcceptAsync(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda21
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$maybeReselectDomainForEmergencyCall$26(telephonyConnection, defaultPhone, (Integer) obj);
                    }
                }, this.mDomainSelectionMainExecutor);
                return true;
            }
        }
        Log.i(this, "maybeReselectDomainForEmergencyCall endCall()", new Object[0]);
        telephonyConnection.removeTelephonyConnectionListener(this.mEmergencyConnectionListener);
        releaseEmergencyCallDomainSelection(true, false);
        this.mEmergencyStateTracker.endCall(telephonyConnection);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeReselectDomainForEmergencyCall$26(TelephonyConnection telephonyConnection, Phone phone, Integer num) {
        Log.d(this, "reselectDomain-complete", new Object[0]);
        if (this.mEmergencyConnection == null) {
            Log.i(this, "reselectDomain-complete dialing canceled", new Object[0]);
        } else {
            onEmergencyRedialOnDomain(telephonyConnection, phone, num.intValue());
        }
    }

    private boolean isEmergencyNumberAllowedOnDialedSim(Phone phone, String str) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            return true;
        }
        PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId(), "use_only_dialed_sim_ecc_list_bool");
        if (configForSubId == null) {
            configForSubId = CarrierConfigManager.getDefaultConfig();
        }
        if (!configForSubId.getBoolean("use_only_dialed_sim_ecc_list_bool", false) || phone.getEmergencyNumberTracker() == null || phone.getEmergencyNumberTracker().isEmergencyNumber(str)) {
            return true;
        }
        Log.i(this, "isEmergencyNumberAllowedOnDialedSim false", new Object[0]);
        return false;
    }

    private int getEmergencyCallRouting(Phone phone, String str, boolean z) {
        if (phone == null) {
            return 0;
        }
        if (this.mDynamicRoutingController == null) {
            this.mDynamicRoutingController = DynamicRoutingController.getInstance();
        }
        if (this.mDynamicRoutingController.isDynamicRoutingEnabled()) {
            return this.mDynamicRoutingController.getEmergencyCallRouting(phone, str, isNormalRoutingNumber(phone, str), isEmergencyNumberAllowedOnDialedSim(phone, str), z);
        }
        return isNormalRouting(phone, str) ? 2 : 0;
    }

    private boolean isNormalRouting(Phone phone, String str) {
        return isNormalRoutingNumber(phone, str) || !isEmergencyNumberAllowedOnDialedSim(phone, str);
    }

    private boolean isNormalRoutingNumber(Phone phone, String str) {
        if (phone.getEmergencyNumberTracker() != null) {
            return phone.getEmergencyNumberTracker().getEmergencyNumbers(str).stream().anyMatch(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda31
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return TelephonyConnectionService.m4377$r8$lambda$kgE0wntphZNLyZoHtTLdJM_rCg((EmergencyNumber) obj);
                }
            });
        }
        return false;
    }

    /* JADX INFO: renamed from: $r8$lambda$kgE0wntphZNLy-ZoHtTLdJM_rCg, reason: not valid java name */
    public static /* synthetic */ boolean m4377$r8$lambda$kgE0wntphZNLyZoHtTLdJM_rCg(EmergencyNumber emergencyNumber) {
        return emergencyNumber.getEmergencyCallRouting() == 2;
    }

    public Phone getPhoneForNormalRoutedEmergencyCall(final String str) {
        return (Phone) Stream.of((Object[]) this.mPhoneFactoryProxy.getPhones()).filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getPhoneForNormalRoutedEmergencyCall$28(str, (Phone) obj);
            }
        }).findFirst().orElse(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getPhoneForNormalRoutedEmergencyCall$28(String str, Phone phone) {
        return phone.shouldPreferInServiceSimForNormalRoutedEmergencyCall() && isNormalRoutingNumber(phone, str) && isAvailableForEmergencyCalls(phone, 2);
    }

    public Phone getPhoneInEmergencyCallbackMode() {
        if (!this.mDomainSelectionResolver.isDomainSelectionSupported()) {
            return null;
        }
        if (this.mEmergencyStateTracker == null) {
            this.mEmergencyStateTracker = EmergencyStateTracker.getInstance();
        }
        return (Phone) Stream.of((Object[]) this.mPhoneFactoryProxy.getPhones()).filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda20
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getPhoneInEmergencyCallbackMode$29((Phone) obj);
            }
        }).findFirst().orElse(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getPhoneInEmergencyCallbackMode$29(Phone phone) {
        return this.mEmergencyStateTracker.isInEcm(phone);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isVoiceInService(Phone phone, boolean z) {
        int registrationState;
        if (phone.isWifiCallingEnabled()) {
            Log.i(this, "isVoiceInService VoWi-Fi available", new Object[0]);
            return true;
        }
        ServiceState serviceState = phone.getServiceStateTracker().getServiceState();
        if (serviceState.getState() != 0) {
            return false;
        }
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo == null || !((registrationState = networkRegistrationInfo.getRegistrationState()) == 1 || registrationState == 5)) {
            return true;
        }
        int accessNetworkTechnology = networkRegistrationInfo.getAccessNetworkTechnology();
        if (accessNetworkTechnology == 13 && networkRegistrationInfo.getDataSpecificInfo().getLteAttachResultType() == 2) {
            Log.i(this, "isVoiceInService combined attach", new Object[0]);
            return true;
        }
        if (accessNetworkTechnology == 20 || accessNetworkTechnology == 13) {
            Log.i(this, "isVoiceInService PS only network, IMS available " + z, new Object[0]);
            return z;
        }
        return true;
    }

    private boolean maybeReselectDomainForNormalCall(final TelephonyConnection telephonyConnection, ImsReasonInfo imsReasonInfo, boolean z, int i) {
        int i2 = i;
        String str = LOG_TAG;
        Log.i(str, "maybeReselectDomainForNormalCall", new Object[0]);
        com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
        if (this.mDomainSelectionConnection != null && originalConnection != null) {
            final Phone defaultPhone = telephonyConnection.getPhone().getDefaultPhone();
            String schemeSpecificPart = telephonyConnection.getAddress().getSchemeSpecificPart();
            final int videoState = originalConnection.getVideoState();
            CompletableFuture completableFutureReselectDomain = this.mDomainSelectionConnection.reselectDomain(NormalCallDomainSelectionConnection.getSelectionAttributes(defaultPhone.getPhoneId(), defaultPhone.getSubId(), telephonyConnection.getTelecomCallId(), schemeSpecificPart, VideoProfile.isVideo(videoState), originalConnection.getPreciseDisconnectCause(), imsReasonInfo));
            if (completableFutureReselectDomain != null) {
                int preciseDisconnectCause = z ? originalConnection.getPreciseDisconnectCause() : -1;
                int disconnectCause = originalConnection.getDisconnectCause();
                if (i2 == -1 || i2 == disconnectCause) {
                    i2 = disconnectCause;
                } else {
                    Log.i(str, "setDisconnected: override cause: " + disconnectCause + " -> " + i2, new Object[0]);
                }
                this.mDomainSelectionConnection.setDisconnectCause(i2, preciseDisconnectCause, originalConnection.getVendorDisconnectCause());
                Log.d(str, "Reselecting the domain for call", new Object[0]);
                this.mNormalCallConnection = telephonyConnection;
                completableFutureReselectDomain.thenAcceptAsync(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$maybeReselectDomainForNormalCall$30(defaultPhone, videoState, telephonyConnection, (Integer) obj);
                    }
                }, this.mDomainSelectionMainExecutor);
                return true;
            }
        }
        telephonyConnection.removeTelephonyConnectionListener(this.mTelephonyConnectionListener);
        clearNormalCallDomainSelectionConnection();
        this.mNormalCallConnection = null;
        Log.d(str, "Reselect call domain not triggered.", new Object[0]);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeReselectDomainForNormalCall$30(Phone phone, int i, TelephonyConnection telephonyConnection, Integer num) {
        onNormalCallRedial(phone, num.intValue(), i, telephonyConnection);
    }

    private void onEmergencyRedialOnDomain(final TelephonyConnection telephonyConnection, final Phone phone, int i) {
        Log.i(this, "onEmergencyRedialOnDomain phoneId=" + phone.getPhoneId() + ", domain=" + DomainSelectionService.getDomainName(i), new Object[0]);
        final Bundle bundle = new Bundle();
        bundle.putInt("dial_domain", i);
        if (telephonyConnection == this.mAlternateEmergencyConnection) {
            bundle.putInt("emergency_routing_update_cause", 1);
            if (telephonyConnection.getEmergencyServiceCategory() != null) {
                bundle.putInt("emergency_service_category", telephonyConnection.getEmergencyServiceCategory().intValue());
            }
            if (telephonyConnection.getEmergencyUrns() != null) {
                bundle.putStringArrayList("emergency_urns", new ArrayList<>(telephonyConnection.getEmergencyUrns()));
            }
        } else if (telephonyConnection == this.mForcedEmergencyRoutingConnection) {
            bundle.putInt("emergency_routing_update_cause", 2);
        }
        checkAndRejectIncomingCall(phone, new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda29
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$onEmergencyRedialOnDomain$31((Boolean) obj);
            }
        }).thenRun(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onEmergencyRedialOnDomain$32(telephonyConnection, phone, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmergencyRedialOnDomain$31(Boolean bool) {
        if (bool.booleanValue()) {
            return;
        }
        Log.i(this, "onEmergencyRedialOnDomain reject incoming call failed", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmergencyRedialOnDomain$32(TelephonyConnection telephonyConnection, Phone phone, Bundle bundle) {
        if (telephonyConnection.getState() == 6) {
            Log.i(this, "onEmergencyRedialOnDomain reject incoming, dialing canceled", new Object[0]);
        } else {
            onEmergencyRedialOnDomainInternal(telephonyConnection, phone, bundle);
        }
    }

    private void onEmergencyRedialOnDomainInternal(TelephonyConnection telephonyConnection, Phone phone, Bundle bundle) {
        int iIntValue;
        boolean z;
        if (this.mEmergencyConnection == null) {
            Log.i(this, "onEmergencyRedialOnDomainInternal dialing canceled", new Object[0]);
            return;
        }
        String schemeSpecificPart = telephonyConnection.getAddress().getSchemeSpecificPart();
        if (telephonyConnection.getEmergencyServiceCategory() != null) {
            iIntValue = telephonyConnection.getEmergencyServiceCategory().intValue();
            Log.i(this, "onEmergencyRedialOnDomainInternal eccCategory=" + iIntValue, new Object[0]);
            z = true;
        } else {
            iIntValue = 0;
            z = false;
        }
        com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
        if (phone != null) {
            try {
                originalConnection = phone.dial(schemeSpecificPart, new ImsPhone.ImsDialArgs.Builder().setVideoState(0).setIntentExtras(bundle).setRttTextStream(telephonyConnection.getRttTextStream()).setIsEmergency(z).setEccCategory(iIntValue).build(), new TelephonyConnectionService$$ExternalSyntheticLambda18(telephonyConnection));
            } catch (CallStateException e) {
                Log.e((Object) this, (Throwable) e, "onEmergencyRedialOnDomainInternal, exception: " + e, new Object[0]);
                onLocalHangup(telephonyConnection);
                telephonyConnection.unregisterForCallEvents();
                handleCallStateException(e, telephonyConnection, phone);
                return;
            }
        }
        if (originalConnection == null) {
            Log.d(this, "onEmergencyRedialOnDomainInternal, phone.dial returned null", new Object[0]);
            onLocalHangup(telephonyConnection);
            telephonyConnection.setTelephonyConnectionDisconnected(this.mDisconnectCauseFactory.toTelecomDisconnectCause(36, "unknown error"));
            telephonyConnection.close();
            return;
        }
        telephonyConnection.setOriginalConnection(originalConnection);
    }

    private void onEmergencyRedial(final TelephonyConnection telephonyConnection, final Phone phone, final boolean z) {
        Log.i(this, "onEmergencyRedial phoneId=" + phone.getPhoneId() + ", ariplaneMode=" + z, new Object[0]);
        final boolean zIsEmergencyNumberTestNumber = isEmergencyNumberTestNumber(telephonyConnection.getAddress().getSchemeSpecificPart());
        this.mIsEmergencyCallPending = true;
        telephonyConnection.addTelephonyConnectionListener(this.mEmergencyConnectionListener);
        handleEmergencyCallStartedForSatelliteSOSMessageRecommender(telephonyConnection, phone);
        if (this.mEmergencyStateTracker == null) {
            this.mEmergencyStateTracker = EmergencyStateTracker.getInstance();
        }
        this.mEmergencyConnection = telephonyConnection;
        this.mEmergencyStateTracker.startEmergencyCall(phone, telephonyConnection, zIsEmergencyNumberTestNumber).thenAccept(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$onEmergencyRedial$35(phone, telephonyConnection, z, zIsEmergencyNumberTestNumber, (Integer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmergencyRedial$35(final Phone phone, final TelephonyConnection telephonyConnection, boolean z, boolean z2, Integer num) {
        Log.d(this, "onEmergencyRedial-complete result=" + num, new Object[0]);
        if (this.mEmergencyConnection == null) {
            Log.i(this, "onEmergencyRedial-complete dialing canceled", new Object[0]);
            return;
        }
        if (num.intValue() == 0) {
            EmergencyCallDomainSelectionConnection domainSelectionConnection = this.mDomainSelectionResolver.getDomainSelectionConnection(phone, 1, true);
            if (domainSelectionConnection == null) {
                Log.w(this, "onEmergencyRedial no selectionConnection, dial CS emergency call", new Object[0]);
                this.mIsEmergencyCallPending = false;
                this.mDomainSelectionMainExecutor.execute(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda36
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onEmergencyRedial$33(telephonyConnection, phone);
                    }
                });
                return;
            } else {
                this.mEmergencyCallDomainSelectionConnection = domainSelectionConnection;
                this.mEmergencyCallDomainSelectionConnection.createEmergencyConnection(EmergencyCallDomainSelectionConnection.getSelectionAttributes(phone.getPhoneId(), phone.getSubId(), z, telephonyConnection.getTelecomCallId(), telephonyConnection.getAddress().getSchemeSpecificPart(), z2, 0, (ImsReasonInfo) null, this.mEmergencyStateTracker.getEmergencyRegistrationResult()), this.mEmergencyDomainSelectionConnectionCallback).thenAcceptAsync(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda37
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$onEmergencyRedial$34(telephonyConnection, phone, (Integer) obj);
                    }
                }, this.mDomainSelectionMainExecutor);
                return;
            }
        }
        if (num.intValue() == 64) {
            this.mEmergencyConnection.removeTelephonyConnectionListener(this.mEmergencyConnectionListener);
            TelephonyConnection telephonyConnection2 = this.mEmergencyConnection;
            releaseEmergencyCallDomainSelection(true, false);
            retryOutgoingOriginalConnection(telephonyConnection2, phone, true);
            return;
        }
        this.mEmergencyConnection = null;
        this.mAlternateEmergencyConnection = null;
        this.mForcedEmergencyRoutingConnection = null;
        telephonyConnection.setTelephonyConnectionDisconnected(this.mDisconnectCauseFactory.toTelecomDisconnectCause(num.intValue(), "unknown error"));
        telephonyConnection.close();
        this.mIsEmergencyCallPending = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmergencyRedial$33(TelephonyConnection telephonyConnection, Phone phone) {
        recreateEmergencyConnection(telephonyConnection, phone, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmergencyRedial$34(TelephonyConnection telephonyConnection, Phone phone, Integer num) {
        Log.d(this, "onEmergencyRedial-createEmergencyConnection-complete domain=" + num, new Object[0]);
        recreateEmergencyConnection(telephonyConnection, phone, num.intValue());
        this.mIsEmergencyCallPending = false;
    }

    private void recreateEmergencyConnection(TelephonyConnection telephonyConnection, Phone phone, int i) {
        Log.d(this, "recreateEmergencyConnection result=" + i, new Object[0]);
        if (this.mEmergencyConnection == null) {
            Log.i(this, "recreateEmergencyConnection dialing canceled", new Object[0]);
            return;
        }
        if (!getAllConnections().isEmpty()) {
            if (!shouldHoldForEmergencyCall(phone)) {
                for (Connection connection : getAllConnections()) {
                    if (!connection.equals(telephonyConnection) && connection.getState() != 6 && (connection instanceof TelephonyConnection)) {
                        ((TelephonyConnection) connection).hangup(80);
                    }
                }
                for (Conference conference : getAllConferences()) {
                    if (conference.getState() != 6) {
                        conference.onDisconnect();
                    }
                }
            } else if (!isVideoCallHoldAllowed(phone)) {
                for (Connection connection2 : getAllConnections()) {
                    if (!connection2.equals(telephonyConnection) && connection2.getState() == 4 && VideoProfile.isVideo(connection2.getVideoState()) && (connection2 instanceof TelephonyConnection)) {
                        ((TelephonyConnection) connection2).hangup(80);
                        break;
                    }
                }
            }
        }
        onEmergencyRedialOnDomain(telephonyConnection, phone, i);
    }

    private void onNormalCallRedial(final Phone phone, final int i, final int i2, final TelephonyConnection telephonyConnection) {
        if (this.mNormalRoutingEmergencyConnection == telephonyConnection) {
            checkAndRejectIncomingCall(phone, new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda33
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$onNormalCallRedial$36((Boolean) obj);
                }
            }).thenRun(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onNormalCallRedial$37(telephonyConnection, phone, i, i2);
                }
            });
        } else {
            onNormalCallRedial(telephonyConnection, phone, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onNormalCallRedial$36(Boolean bool) {
        if (bool.booleanValue()) {
            return;
        }
        Log.i(this, "onNormalCallRedial reject incoming call failed", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onNormalCallRedial$37(TelephonyConnection telephonyConnection, Phone phone, int i, int i2) {
        if (telephonyConnection.getState() == 6) {
            Log.i(this, "onNormalCallRedial reject incoming, dialing canceled", new Object[0]);
        } else {
            onNormalCallRedial(telephonyConnection, phone, i, i2);
        }
    }

    private void onNormalCallRedial(TelephonyConnection telephonyConnection, Phone phone, int i, int i2) {
        String str = LOG_TAG;
        Log.v(str, "Redialing the call in domain:" + DomainSelectionService.getDomainName(i), new Object[0]);
        String schemeSpecificPart = telephonyConnection.getAddress().getSchemeSpecificPart();
        Bundle bundle = new Bundle();
        bundle.putInt("dial_domain", i);
        ImsPhoneConnection originalConnection = telephonyConnection.getOriginalConnection();
        if ((originalConnection instanceof ImsPhoneConnection) && originalConnection.isRttEnabledForCall()) {
            bundle.putBoolean("android.telecom.extra.START_CALL_WITH_RTT", true);
        }
        if (phone != null) {
            try {
                Log.d(str, "Redialing Call.", new Object[0]);
                originalConnection = phone.dial(schemeSpecificPart, new ImsPhone.ImsDialArgs.Builder().setVideoState(i2).setIntentExtras(bundle).setRttTextStream(telephonyConnection.getRttTextStream()).setIsEmergency(false).build(), new TelephonyConnectionService$$ExternalSyntheticLambda18(telephonyConnection));
            } catch (Exception e) {
                Log.e(LOG_TAG, (Throwable) e, "Call redial exception: " + e, new Object[0]);
            }
        }
        if (originalConnection == null) {
            Log.e(LOG_TAG, (Throwable) new Exception("Phone is null"), "Call redial failure due to phone.dial returned null", new Object[0]);
            telephonyConnection.setDisconnected(this.mDisconnectCauseFactory.toTelecomDisconnectCause(43, "connection is null"));
            telephonyConnection.close();
            return;
        }
        telephonyConnection.setOriginalConnection(originalConnection);
    }

    protected void onLocalHangup(TelephonyConnection telephonyConnection) {
        if (this.mEmergencyConnection == telephonyConnection) {
            Log.i(this, "onLocalHangup " + telephonyConnection.getTelecomCallId(), new Object[0]);
            telephonyConnection.removeTelephonyConnectionListener(this.mEmergencyConnectionListener);
            releaseEmergencyCallDomainSelection(true, false);
            this.mEmergencyStateTracker.endCall(telephonyConnection);
        }
        if (this.mNormalRoutingEmergencyConnection == telephonyConnection) {
            Log.i(this, "onLocalHangup normal routing " + telephonyConnection.getTelecomCallId(), new Object[0]);
            this.mNormalRoutingEmergencyConnection = null;
            this.mEmergencyStateTracker.endNormalRoutingEmergencyCall(telephonyConnection);
            this.mIsEmergencyCallPending = false;
        }
    }

    public TelephonyConnection getEmergencyConnection() {
        return this.mEmergencyConnection;
    }

    public void setEmergencyConnection(TelephonyConnection telephonyConnection) {
        this.mEmergencyConnection = telephonyConnection;
    }

    public TelephonyConnection.TelephonyConnectionListener getEmergencyConnectionListener() {
        return this.mEmergencyConnectionListener;
    }

    public TelephonyConnection getNormalRoutingEmergencyConnection() {
        return this.mNormalRoutingEmergencyConnection;
    }

    public void setNormalRoutingEmergencyConnection(TelephonyConnection telephonyConnection) {
        this.mNormalRoutingEmergencyConnection = telephonyConnection;
    }

    public TelephonyConnection.TelephonyConnectionListener getEmergencyConnectionSatelliteListener() {
        return this.mEmergencyConnectionSatelliteListener;
    }

    private boolean isVideoCallHoldAllowed(Phone phone) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            Log.w(this, "isVideoCallHoldAllowed: couldn't get CarrierConfigManager", new Object[0]);
            return true;
        }
        return carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("allow_hold_video_call_bool", true);
    }

    private boolean shouldHoldForEmergencyCall(Phone phone) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            Log.w(this, "shouldHoldForEmergencyCall: couldn't get CarrierConfigManager", new Object[0]);
            return true;
        }
        if (MtkTelephonyConnectionServiceUtil.isTestSim(phone.getPhoneId())) {
            return true;
        }
        return carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("mtk_allow_hold_call_during_emergency_bool", true);
    }

    private void handleCallStateException(CallStateException callStateException, TelephonyConnection telephonyConnection, Phone phone) {
        int i;
        switch (callStateException.getError()) {
            case 1:
                i = 18;
                break;
            case 2:
                i = 17;
                break;
            case 3:
                i = 72;
                break;
            case 4:
                i = 73;
                break;
            case 5:
                i = 74;
                break;
            case 6:
                i = 75;
                break;
            case 7:
                i = 76;
                break;
            case 8:
                i = 21;
                break;
            default:
                i = 43;
                break;
        }
        telephonyConnection.setTelephonyConnectionDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(i, callStateException.getMessage(), phone.getPhoneId()));
        telephonyConnection.close();
    }

    private TelephonyConnection createConnectionFor(Phone phone, com.android.internal.telephony.Connection connection, boolean z, PhoneAccountHandle phoneAccountHandle, String str) {
        return createConnectionFor(phone, connection, z, phoneAccountHandle, str, false);
    }

    private TelephonyConnection createConnectionFor(Phone phone, com.android.internal.telephony.Connection connection, boolean z, PhoneAccountHandle phoneAccountHandle, String str, boolean z2) {
        phone.getPhoneType();
        GsmConnection gsmConnection = new GsmConnection(connection, str, z ? 1 : 0);
        if (!z2) {
            gsmConnection.addTelephonyConnectionListener(this.mTelephonyConnectionListener);
        }
        gsmConnection.setVideoPauseSupported(TelecomAccountRegistry.getInstance(this).isVideoPauseSupported(phoneAccountHandle));
        gsmConnection.setManageImsConferenceCallSupported(TelecomAccountRegistry.getInstance(this).isManageImsConferenceCallSupported(phoneAccountHandle));
        gsmConnection.setShowPreciseFailedCause(TelecomAccountRegistry.getInstance(this).isShowPreciseFailedCause(phoneAccountHandle));
        gsmConnection.setTelephonyConnectionService(this);
        return gsmConnection;
    }

    private boolean isOriginalConnectionKnown(com.android.internal.telephony.Connection connection) {
        return getConnectionForOriginalConnection(connection) != null;
    }

    private TelephonyConnection getConnectionForOriginalConnection(com.android.internal.telephony.Connection connection) {
        for (Connection connection2 : getAllConnections()) {
            if (connection2 instanceof TelephonyConnection) {
                TelephonyConnection telephonyConnection = (TelephonyConnection) connection2;
                if (telephonyConnection.getOriginalConnection() == connection) {
                    return telephonyConnection;
                }
            }
        }
        return null;
    }

    private Phone getPhoneForAccount(PhoneAccountHandle phoneAccountHandle, boolean z, String str) {
        Phone phone;
        Phone phoneInEmergencyCallbackMode;
        Object objValueOf;
        int subIdForPhoneAccountHandle = this.mPhoneUtilsProxy.getSubIdForPhoneAccountHandle(phoneAccountHandle);
        Object objValueOf2 = "null";
        if (subIdForPhoneAccountHandle != -1) {
            phone = this.mPhoneFactoryProxy.getPhone(this.mSubscriptionManagerProxy.getPhoneId(subIdForPhoneAccountHandle));
            if (phone == null) {
                objValueOf = "null";
            } else {
                objValueOf = Integer.valueOf(phone.getSubId());
            }
            Log.i(this, "getPhoneForAccount: handle=%s, subId=%s", phoneAccountHandle, objValueOf);
        } else {
            phone = null;
        }
        if (!z) {
            return phone;
        }
        Phone phoneForNormalRoutedEmergencyCall = getPhoneForNormalRoutedEmergencyCall(str);
        if (phoneForNormalRoutedEmergencyCall != null) {
            Log.i(this, "getPhoneForAccount: normal routed emergency number,using phoneId=%d/subId=%d", Integer.valueOf(phoneForNormalRoutedEmergencyCall.getPhoneId()), Integer.valueOf(phoneForNormalRoutedEmergencyCall.getSubId()));
            return phoneForNormalRoutedEmergencyCall;
        }
        if (this.mDomainSelectionResolver.isDomainSelectionSupported() && (phoneInEmergencyCallbackMode = getPhoneInEmergencyCallbackMode()) != null) {
            Log.i(this, "getPhoneForAccount: in ECBM, using phoneId=%d/subId=%d", Integer.valueOf(phoneInEmergencyCallbackMode.getPhoneId()), Integer.valueOf(phoneInEmergencyCallbackMode.getSubId()));
            return phoneInEmergencyCallbackMode;
        }
        if (phone != null && isAvailableForEmergencyCalls(phone)) {
            return phone;
        }
        Log.d(this, "getPhoneForAccount: phone for phone acct handle %s is out of service or invalid for emergency call.", phoneAccountHandle);
        Phone phoneForEmergencyCall = getPhoneForEmergencyCall(str);
        if (phoneForEmergencyCall != null) {
            objValueOf2 = Integer.valueOf(phoneForEmergencyCall.getSubId());
        }
        Log.i(this, "getPhoneForAccount: emergency call - using subId: %s", objValueOf2);
        return phoneForEmergencyCall;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: delayDialForDdsSwitch, reason: merged with bridge method [inline-methods] */
    public void lambda$onCreateOutgoingConnection$3(Phone phone, Consumer consumer) {
        if (phone == null) {
            consumer.accept(Boolean.FALSE);
            return;
        }
        try {
            CompletableFuture completableFuturePossiblyOverrideDefaultDataForEmergencyCall = possiblyOverrideDefaultDataForEmergencyCall(phone);
            final CompletableFuture completableFuture = new CompletableFuture();
            phone.getContext().getMainThreadHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    completableFuture.complete(Boolean.FALSE);
                }
            }, 2000L);
            completableFuturePossiblyOverrideDefaultDataForEmergencyCall.acceptEitherAsync((CompletionStage) completableFuture, consumer, phone.getContext().getMainExecutor());
        } catch (Exception e) {
            Log.w(this, "delayDialForDdsSwitch - exception= " + e.getMessage(), new Object[0]);
        }
    }

    private CompletableFuture possiblyOverrideDefaultDataForEmergencyCall(Phone phone) {
        int i;
        boolean z = true;
        if (this.mTelephonyManagerProxy.getPhoneCount() <= 1) {
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        if (!this.mDeviceState.isSuplDdsSwitchRequiredForEmergencyCall(this)) {
            Log.d(this, "possiblyOverrideDefaultDataForEmergencyCall: not switching DDS, does not require DDS switch.", new Object[0]);
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            Log.w(this, "possiblyOverrideDefaultDataForEmergencyCall: couldn't getCarrierConfigManager", new Object[0]);
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        if (!isAvailableForEmergencyCalls(phone)) {
            Log.d(this, "possiblyOverrideDefaultDataForEmergencyCall: not switching DDS", new Object[0]);
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        boolean voiceRoaming = phone.getServiceState().getVoiceRoaming();
        String[] stringArray = carrierConfigManager.getConfigForSubId(phone.getSubId()).getStringArray("gps.es_supl_data_plane_only_roaming_plmn_string_array");
        if (stringArray != null && Arrays.asList(stringArray).contains(phone.getServiceState().getOperatorNumeric())) {
            z = false;
        }
        if (voiceRoaming && z) {
            Log.d(this, "possiblyOverrideDefaultDataForEmergencyCall: roaming network is assumed to support CP fallback, not switching DDS.", new Object[0]);
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        if (carrierConfigManager.getConfigForSubId(phone.getSubId()).getInt("gps.es_supl_control_plane_support_int", 0) != 2 && z) {
            Log.d(this, "possiblyOverrideDefaultDataForEmergencyCall: not switching DDS, carrier supports CP fallback.", new Object[0]);
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        try {
            i = Integer.parseInt(carrierConfigManager.getConfigForSubId(phone.getSubId()).getString("gps.es_extension_sec", "0"));
        } catch (NumberFormatException unused) {
            i = 0;
        }
        CompletableFuture completableFuture = new CompletableFuture();
        try {
            Log.d(this, "possiblyOverrideDefaultDataForEmergencyCall: overriding DDS for " + i + "seconds", new Object[0]);
            this.mPhoneSwitcherProxy.getPhoneSwitcher().overrideDefaultDataForEmergency(phone.getPhoneId(), i, completableFuture);
            return completableFuture;
        } catch (Exception e) {
            Log.w(this, "possiblyOverrideDefaultDataForEmergencyCall: exception = " + e.getMessage(), new Object[0]);
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
    }

    private void addTelephonyConnectionListener(Conferenceable conferenceable, TelephonyConnection.TelephonyConnectionListener telephonyConnectionListener) {
        if (conferenceable instanceof TelephonyConnection) {
            ((TelephonyConnection) conferenceable).addTelephonyConnectionListener(telephonyConnectionListener);
        } else if (conferenceable instanceof ImsConference) {
            ((TelephonyConnection) ((ImsConference) conferenceable).getConferenceHost()).addTelephonyConnectionListener(telephonyConnectionListener);
        } else {
            OrtProvider$$ExternalSyntheticBUOutline0.m("addTelephonyConnectionListener(): Unexpected conferenceable! ", conferenceable);
        }
    }

    private CompletableFuture delayDialForOtherSubDisconnects(Phone phone, List list, Consumer consumer) {
        CompletableFuture completableFutureCompletedFuture;
        if (list.isEmpty()) {
            consumer.accept(Boolean.TRUE);
            return CompletableFuture.completedFuture(null);
        }
        if (phone == null) {
            consumer.accept(Boolean.FALSE);
            return CompletableFuture.completedFuture(null);
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Conferenceable conferenceable = (Conferenceable) it.next();
            CompletableFuture completableFutureCompletedFuture2 = CompletableFuture.completedFuture(null);
            if (conferenceable == null) {
                try {
                    try {
                        completableFutureCompletedFuture = CompletableFuture.completedFuture(null);
                    } catch (Exception e) {
                        Log.w(this, "delayDialForOtherSubDisconnects - exception= " + e.getMessage(), new Object[0]);
                        completableFutureCompletedFuture = CompletableFuture.completedFuture(null);
                    }
                } catch (Throwable th) {
                    arrayList.add(completableFutureCompletedFuture2);
                    throw th;
                }
            } else {
                completableFutureCompletedFuture = listenForDisconnectStateChanged(conferenceable).completeOnTimeout(Boolean.FALSE, MirrorDesktopHelper.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            }
            arrayList.add(completableFutureCompletedFuture);
        }
        return CompletableFuture.allOf((CompletableFuture[]) arrayList.toArray(new IntFunction() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda41
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return TelephonyConnectionService.$r8$lambda$7ExViddVOx4vZ5TZPeeJijCIIHs(i);
            }
        }));
    }

    public static /* synthetic */ CompletableFuture[] $r8$lambda$7ExViddVOx4vZ5TZPeeJijCIIHs(int i) {
        return new CompletableFuture[i];
    }

    private CompletableFuture listenForDisconnectStateChanged(Conferenceable conferenceable) {
        CompletableFuture completableFuture = new CompletableFuture();
        addTelephonyConnectionListener(conferenceable, new StateDisconnectListener(completableFuture));
        return completableFuture;
    }

    private CompletableFuture checkAndRejectIncomingCall(Phone phone, Consumer consumer) {
        if (phone == null) {
            Log.i(this, "checkAndRejectIncomingCall phone is null", new Object[0]);
            consumer.accept(Boolean.FALSE);
            return CompletableFuture.completedFuture(null);
        }
        Call ringingCall = phone.getRingingCall();
        if (ringingCall == null || ringingCall.getState() == Call.State.IDLE || ringingCall.getState() == Call.State.DISCONNECTED) {
            Phone imsPhone = phone.getImsPhone();
            if (imsPhone != null) {
                ringingCall = imsPhone.getRingingCall();
            }
            if (imsPhone == null || ringingCall == null || ringingCall.getState() == Call.State.IDLE || ringingCall.getState() == Call.State.DISCONNECTED) {
                consumer.accept(Boolean.TRUE);
                return CompletableFuture.completedFuture(null);
            }
        }
        Log.i(this, "checkAndRejectIncomingCall found a ringing call", new Object[0]);
        try {
            ringingCall.hangup();
            CompletableFuture completableFuture = new CompletableFuture();
            ringingCall.getLatestConnection().addListener(new OnDisconnectListener(completableFuture));
            final CompletableFuture completableFuture2 = new CompletableFuture();
            phone.getContext().getMainThreadHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda42
                @Override // java.lang.Runnable
                public final void run() {
                    completableFuture2.complete(Boolean.FALSE);
                }
            }, 10000L);
            return completableFuture.acceptEitherAsync((CompletionStage) completableFuture2, consumer, phone.getContext().getMainExecutor()).exceptionally(new Function() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda43
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return this.f$0.lambda$checkAndRejectIncomingCall$41((Throwable) obj);
                }
            });
        } catch (Exception e) {
            Log.w(this, "checkAndRejectIncomingCall - exception= " + e.getMessage(), new Object[0]);
            consumer.accept(Boolean.FALSE);
            return CompletableFuture.completedFuture(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Void lambda$checkAndRejectIncomingCall$41(Throwable th) {
        Log.w(this, "checkAndRejectIncomingCall - exceptionally= " + th, new Object[0]);
        return null;
    }

    public Phone getPhoneForEmergencyCall(String str) {
        ArrayList arrayList = new ArrayList();
        int defaultVoicePhoneId = this.mSubscriptionManagerProxy.getDefaultVoicePhoneId();
        for (Phone phone : this.mPhoneFactoryProxy.getPhones()) {
            if (phone.getEmergencyNumberTracker() != null && phone.getEmergencyNumberTracker().isEmergencyNumber(str) && isAvailableForEmergencyCalls(phone)) {
                if (phone.getPhoneId() == defaultVoicePhoneId) {
                    Log.i(this, "getPhoneForEmergencyCall, Phone Id that supports emergency number: " + phone.getPhoneId(), new Object[0]);
                    return phone;
                }
                arrayList.add(phone);
            }
        }
        if (arrayList.size() > 0) {
            Log.i(this, "getPhoneForEmergencyCall, Phone Id that supports emergency number:" + ((Phone) arrayList.get(0)).getPhoneId(), new Object[0]);
            return getFirstPhoneForEmergencyCall(arrayList);
        }
        return getFirstPhoneForEmergencyCall();
    }

    public Phone getFirstPhoneForEmergencyCall() {
        return getFirstPhoneForEmergencyCall(null);
    }

    public Phone getFirstPhoneForEmergencyCall(List list) {
        Phone phone;
        int phoneCount = this.mTelephonyManagerProxy.getPhoneCount();
        for (int i = 0; i < phoneCount; i++) {
            Phone phone2 = this.mPhoneFactoryProxy.getPhone(i);
            if (phone2 != null && phone2.isInEmergencySmsMode() && isAvailableForEmergencyCalls(phone2) && (list == null || list.contains(phone2))) {
                return phone2;
            }
        }
        int defaultVoicePhoneId = this.mSubscriptionManagerProxy.getDefaultVoicePhoneId();
        if (defaultVoicePhoneId == -1) {
            defaultVoicePhoneId = this.mSubscriptionManagerProxy.getDefaultDataPhoneId();
        }
        if (defaultVoicePhoneId != -1 && (phone = this.mPhoneFactoryProxy.getPhone(defaultVoicePhoneId)) != null && isAvailableForEmergencyCalls(phone) && (list == null || list.contains(phone))) {
            return phone;
        }
        ArrayList arrayList = new ArrayList(phoneCount);
        final Phone phone3 = null;
        for (int i2 = 0; i2 < phoneCount; i2++) {
            Phone phone4 = this.mPhoneFactoryProxy.getPhone(i2);
            if (phone4 != null) {
                if (isAvailableForEmergencyCalls(phone4) && (list == null || list.contains(phone4))) {
                    Log.i(this, "getFirstPhoneForEmergencyCall, radio on & in service, Phone Id:" + i2, new Object[0]);
                    return phone4;
                }
                int radioAccessFamily = phone4.getRadioAccessFamily();
                SlotStatus slotStatus = new SlotStatus(i2, radioAccessFamily, phone4.getSubId());
                arrayList.add(slotStatus);
                Log.i(this, "getFirstPhoneForEmergencyCall, RAF:" + Integer.toHexString(radioAccessFamily) + " saved for Phone Id:" + i2 + " subId:" + phone4.getSubId(), new Object[0]);
                int simStateForSlotIdx = this.mSubscriptionManagerProxy.getSimStateForSlotIdx(i2);
                slotStatus.simState = simStateForSlotIdx;
                if (simStateForSlotIdx == 2 || simStateForSlotIdx == 3) {
                    slotStatus.isLocked = true;
                }
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        Phone phone5 = (Phone) it.next();
                        if (phone5 != null && phone5.getPhoneId() == i2) {
                            slotStatus.hasDialedEmergencyNumber = true;
                        }
                    }
                }
                if (phone3 == null && phone4.getSubId() != -1) {
                    Log.i(this, "getFirstPhoneForEmergencyCall, SIM with active sub, Phone Id:" + phone4.getPhoneId(), new Object[0]);
                    phone3 = phone4;
                }
            }
        }
        if (phone3 == null && arrayList.isEmpty()) {
            if (list != null) {
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    Phone phone6 = (Phone) it2.next();
                    if (phone6 != null) {
                        return phone6;
                    }
                }
            }
            Log.i(this, "getFirstPhoneForEmergencyCall, return default phone", new Object[0]);
            return this.mPhoneFactoryProxy.getDefaultPhone();
        }
        final int phoneId = this.mPhoneFactoryProxy.getDefaultPhone().getPhoneId();
        if (arrayList.isEmpty()) {
            return phone3;
        }
        Log.i(this, "getFirstPhoneForEmergencyCall, list size: " + arrayList.size() + " defaultPhoneId: " + phoneId + " firstOccupiedSlot: " + phone3, new Object[0]);
        if (arrayList.size() > 1) {
            Collections.sort(arrayList, new Comparator() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda7
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return TelephonyConnectionService.$r8$lambda$ZyOMcPm9HUtpsWhMKGXKjs6nIHw(phone3, phoneId, (TelephonyConnectionService.SlotStatus) obj, (TelephonyConnectionService.SlotStatus) obj2);
                }
            });
        }
        int i3 = ((SlotStatus) arrayList.get(arrayList.size() - 1)).slotId;
        Log.i(this, "getFirstPhoneForEmergencyCall, Using Phone Id: " + i3 + "with highest capability", new Object[0]);
        return this.mPhoneFactoryProxy.getPhone(i3);
    }

    public static /* synthetic */ int $r8$lambda$ZyOMcPm9HUtpsWhMKGXKjs6nIHw(Phone phone, int i, SlotStatus slotStatus, SlotStatus slotStatus2) {
        if (slotStatus.isSubActiveAndSimPresent() && !slotStatus2.isSubActiveAndSimPresent()) {
            return 1;
        }
        if (slotStatus2.isSubActiveAndSimPresent() && !slotStatus.isSubActiveAndSimPresent()) {
            return -1;
        }
        boolean z = slotStatus.isLocked;
        if (z && !slotStatus2.isLocked) {
            return -1;
        }
        if (slotStatus2.isLocked && !z) {
            return 1;
        }
        boolean z2 = slotStatus.hasDialedEmergencyNumber;
        if (!z2 && slotStatus2.hasDialedEmergencyNumber) {
            return -1;
        }
        if (z2 && !slotStatus2.hasDialedEmergencyNumber) {
            return 1;
        }
        int iCompare = RadioAccessFamily.compare(slotStatus.capabilities, slotStatus2.capabilities);
        if (iCompare == 0) {
            int i2 = slotStatus.slotId;
            if (phone != null) {
                if (i2 == phone.getPhoneId()) {
                    return 1;
                }
                if (slotStatus2.slotId == phone.getPhoneId()) {
                    return -1;
                }
            } else {
                if (i2 == i) {
                    return 1;
                }
                if (slotStatus2.slotId == i) {
                    return -1;
                }
            }
        }
        return iCompare;
    }

    private boolean isAvailableForEmergencyCalls(Phone phone) {
        return isAvailableForEmergencyCalls(phone, 1);
    }

    public boolean isAvailableForEmergencyCalls(Phone phone, int i) {
        MtkTelephonyManagerEx mtkTelephonyManagerEx = MtkTelephonyManagerEx.getDefault();
        if (mtkTelephonyManagerEx != null && mtkTelephonyManagerEx.getSimLockPolicy() == 7 && mtkTelephonyManagerEx.getShouldServiceCapability(phone.getPhoneId()) == 4) {
            Log.i(this, "isAvailableForEmergencyCalls found NO_SERVICE constraints, return false.(ServiceState=" + phone.getServiceState().getState() + ", isEmergencyOnly=" + phone.getServiceState().isEmergencyOnly() + ")", new Object[0]);
            return false;
        }
        if (isCallDisallowedDueToSatellite(phone) && isTerrestrialNetworkAvailable()) {
            return false;
        }
        if (phone.getImsRegistrationTech() == 2) {
            Log.d(this, "isAvailableForEmergencyCalls: skipping over phone " + phone + " as it is registered to CROSS_SIM", new Object[0]);
            return false;
        }
        if (phone.getServiceState().getState() == 0) {
            return true;
        }
        return (i != 2 && phone.getServiceState().isEmergencyOnly()) || (MtkTelephonyConnectionServiceUtil.getInstance().allowEmergencyCallIfOos(phone) && 1 == phone.getServiceState().getState());
    }

    private boolean isTerrestrialNetworkAvailable() {
        int state;
        for (Phone phone : this.mPhoneFactoryProxy.getPhones()) {
            ServiceState serviceState = phone.getServiceState();
            if (serviceState != null && (((state = serviceState.getState()) == 0 || state == 2 || serviceState.isEmergencyOnly()) && !serviceState.isUsingNonTerrestrialNetwork())) {
                Log.d(this, "isTerrestrialNetworkAvailable true", new Object[0]);
                return true;
            }
        }
        Log.d(this, "isTerrestrialNetworkAvailable false", new Object[0]);
        return false;
    }

    TelephonyConnection.TelephonyConnectionListener getTelephonyConnectionListener() {
        return this.mTelephonyConnectionListener;
    }

    public void addConnectionToConferenceController(TelephonyConnection telephonyConnection) {
        if (telephonyConnection.isImsConnection()) {
            Log.d(this, "Adding IMS connection to conference controller: " + telephonyConnection, new Object[0]);
            this.mImsConferenceController.add(telephonyConnection);
            this.mTelephonyConferenceController.remove(telephonyConnection);
            return;
        }
        if (telephonyConnection.getCall() == null || telephonyConnection.getCall().getPhone() == null) {
            Log.d(this, "Connection died, no need to add to conference controller", new Object[0]);
            return;
        }
        telephonyConnection.getCall().getPhone().getPhoneType();
        Log.d(this, "Adding GSM connection to conference controller: " + telephonyConnection, new Object[0]);
        this.mTelephonyConferenceController.add(telephonyConnection);
        Log.d(this, "Removing connection from IMS conference controller: " + telephonyConnection, new Object[0]);
        this.mImsConferenceController.remove(telephonyConnection);
    }

    private void maybeSendInternationalCallEvent(TelephonyConnection telephonyConnection) {
        if (telephonyConnection == null || telephonyConnection.getPhone() == null || telephonyConnection.getPhone().getDefaultPhone() == null) {
            return;
        }
        GsmCdmaPhone defaultPhone = telephonyConnection.getPhone().getDefaultPhone();
        if (defaultPhone instanceof GsmCdmaPhone) {
            GsmCdmaPhone gsmCdmaPhone = defaultPhone;
            if (telephonyConnection.isOutgoingCall() && gsmCdmaPhone.isNotificationOfWfcCallRequired(telephonyConnection.getOriginalConnection().getOrigDialString())) {
                Log.i(this, "placeOutgoingConnection - sending international call on WFC confirmation event", new Object[0]);
                telephonyConnection.sendTelephonyConnectionEvent("android.telephony.event.EVENT_NOTIFY_INTERNATIONAL_CALL_ON_WFC", null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTtyModeChange(boolean z) {
        Log.i(this, "handleTtyModeChange; isTtyEnabled=%b", Boolean.valueOf(z));
        this.mIsTtyEnabled = z;
        for (Connection connection : getAllConnections()) {
            if (connection instanceof TelephonyConnection) {
                ((TelephonyConnection) connection).setTtyEnabled(z);
            }
        }
    }

    private void closeOrDestroyConnection(Connection connection, DisconnectCause disconnectCause) {
        if (connection instanceof TelephonyConnection) {
            TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
            telephonyConnection.setTelephonyConnectionDisconnected(disconnectCause);
            telephonyConnection.close();
        } else {
            connection.setDisconnected(disconnectCause);
            connection.destroy();
        }
    }

    private boolean showDataDialog(final Phone phone, String str) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        final Context applicationContext = getApplicationContext();
        String suppServiceKey = MmiCodeUtil.getSuppServiceKey(str);
        if (suppServiceKey != null) {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
            if (carrierConfigManager != null) {
                z2 = carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("caller_id_over_ut_warning_bool");
                z3 = carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("call_forwarding_over_ut_warning_bool");
                z4 = carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("call_barring_over_ut_warning_bool");
                z = carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("call_waiting_over_ut_warning_bool");
            } else {
                z = false;
                z2 = false;
                z3 = false;
                z4 = false;
            }
            if (!SuppServicesUiUtil.isSsOverUtPrecautions(applicationContext, phone) && !SuppServicesUiUtil.isSsOverUtPrecautionsExt(applicationContext, phone) && !SuppServicesUiUtil.isSsOverUtPrecautionsRoaming(applicationContext, phone)) {
                return false;
            }
            if ((suppServiceKey == "button_clir_key" && z2) || ((suppServiceKey == "call_forwarding_key" && z3) || ((suppServiceKey == "call_barring_key" && z4) || (suppServiceKey == "button_cw_key" && z)))) {
                Log.d(this, "Creating UT Data enable dialog", new Object[0]);
                String strMakeMessage = SuppServicesUiUtil.makeMessage(applicationContext, suppServiceKey, phone);
                AlertDialog alertDialogCreate = FrameworksUtils.makeAlertDialogBuilder(applicationContext).setMessage(strMakeMessage).setNeutralButton(applicationContext.getResources().getString(R.string.settings_label), new DialogInterface.OnClickListener(this) { // from class: com.android.services.telephony.TelephonyConnectionService.19
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.addFlags(268435456);
                        intent.setComponent(new ComponentName(applicationContext.getString(R.string.mobile_network_settings_package), applicationContext.getString(R.string.sims_settings_class)));
                        intent.putExtra("android.provider.extra.SUB_ID", phone.getSubId());
                        applicationContext.startActivityAsUser(intent, UserHandle.CURRENT);
                    }
                }).setPositiveButton(applicationContext.getResources().getString(R.string.supp_service_over_ut_precautions_dialog_dismiss), (DialogInterface.OnClickListener) null).create();
                alertDialogCreate.getWindow().setType(2003);
                alertDialogCreate.show();
                return true;
            }
        }
        return false;
    }

    public void addTelephonyConference(TelephonyConferenceBase telephonyConferenceBase) {
        addConference(telephonyConferenceBase);
        telephonyConferenceBase.addTelephonyConferenceListener(this.mTelephonyConferenceListener);
    }

    public void addTelephonyConferenceFromConnection(TelephonyConferenceBase telephonyConferenceBase, TelephonyConnection telephonyConnection) {
        addConferenceFromConnection(telephonyConferenceBase, telephonyConnection);
        telephonyConferenceBase.addTelephonyConferenceListener(this.mTelephonyConferenceListener);
    }

    public void sendTestDeviceToDeviceMessage(final int i, final int i2) {
        getAllConnections().stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda38
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyConnectionService.$r8$lambda$RznOyqVbVhMBNPik24YFtlH1J_0((Connection) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda39
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$sendTestDeviceToDeviceMessage$44(i, i2, (Connection) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$RznOyqVbVhMBNPik24YFtlH1J_0(Connection connection) {
        return connection instanceof TelephonyConnection;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendTestDeviceToDeviceMessage$44(int i, int i2, Connection connection) {
        TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
        if (!telephonyConnection.isImsConnection()) {
            Log.w(this, "sendTestDeviceToDeviceMessage: not an IMS connection", new Object[0]);
            return;
        }
        Communicator communicator = telephonyConnection.getCommunicator();
        if (communicator == null) {
            Log.w(this, "sendTestDeviceToDeviceMessage: D2D not enabled", new Object[0]);
        } else {
            communicator.sendMessages(Set.of(new Communicator.Message(i, i2)));
        }
    }

    public void setActiveDeviceToDeviceTransport(final String str) {
        getAllConnections().stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda27
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyConnectionService.$r8$lambda$9DgB99kYxUhLXYjNQrkaDxSNEEA((Connection) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda28
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$setActiveDeviceToDeviceTransport$46(str, (Connection) obj);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$9DgB99kYxUhLXYjNQrkaDxSNEEA(Connection connection) {
        return connection instanceof TelephonyConnection;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setActiveDeviceToDeviceTransport$46(String str, Connection connection) {
        TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
        Communicator communicator = telephonyConnection.getCommunicator();
        if (communicator == null) {
            Log.w(this, "setActiveDeviceToDeviceTransport: D2D not enabled", new Object[0]);
        } else {
            Log.i(this, "setActiveDeviceToDeviceTransport: callId=%s, set to: %s", telephonyConnection.getTelecomCallId(), str);
            communicator.setTransportActive(str);
        }
    }

    private PhoneAccountHandle adjustAccountHandle(Phone phone, PhoneAccountHandle phoneAccountHandle) {
        PhoneAccountHandle phoneAccountHandleForSubId;
        int subIdForPhoneAccountHandle = PhoneUtils.getSubIdForPhoneAccountHandle(phoneAccountHandle);
        int subId = phone.getSubId();
        return (subIdForPhoneAccountHandle == -1 || subId == -1 || subIdForPhoneAccountHandle == subId || (phoneAccountHandleForSubId = TelecomAccountRegistry.getInstance(this).getPhoneAccountHandleForSubId(subId)) == null) ? phoneAccountHandle : phoneAccountHandleForSubId;
    }

    public static void maybeDisconnectCallsOnOtherSubs(Collection collection, final PhoneAccountHandle phoneAccountHandle, boolean z, TelephonyManagerProxy telephonyManagerProxy) {
        if (!telephonyManagerProxy.isConcurrentCallsPossible() || z) {
            collection.stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return TelephonyConnectionService.$r8$lambda$YCC4rfUUBq0UM1cpmU3VtI1B6Pc(phoneAccountHandle, (Connection) obj);
                }
            }).forEach(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    TelephonyConnectionService.m4372$r8$lambda$Rn9Uj7MWwDUllG7KA7HZxqP7o((Connection) obj);
                }
            });
        }
    }

    public static /* synthetic */ boolean $r8$lambda$YCC4rfUUBq0UM1cpmU3VtI1B6Pc(PhoneAccountHandle phoneAccountHandle, Connection connection) {
        return (connection.getConnectionProperties() & 16) == 0 && !Objects.equals(connection.getPhoneAccountHandle(), phoneAccountHandle);
    }

    /* JADX INFO: renamed from: $r8$lambda$Rn9Uj7MWwDUllG7KA7HZxqP-7-o, reason: not valid java name */
    public static /* synthetic */ void m4372$r8$lambda$Rn9Uj7MWwDUllG7KA7HZxqP7o(Connection connection) {
        if (connection instanceof TelephonyConnection) {
            TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
            if (telephonyConnection.shouldTreatAsEmergencyCall()) {
                return;
            }
            Log.i(LOG_TAG, "maybeDisconnectCallsOnOtherSubs: disconnect %s due to incoming call on other sub.", telephonyConnection.getTelecomCallId());
            telephonyConnection.hangup(3);
        }
    }

    protected List disconnectAllConferenceablesOnOtherSubs(final PhoneAccountHandle phoneAccountHandle) {
        final ArrayList arrayList = new ArrayList();
        getAllConferences().stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyConnectionService.m4369$r8$lambda$Ka1W_Z1IAzoadRYptlRxdbjpyU(phoneAccountHandle, (Conference) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TelephonyConnectionService.$r8$lambda$dxcNB86jazQ9_TPHH_Ef2USo4_I(arrayList, (Conference) obj);
            }
        });
        arrayList.addAll(disconnectAllCallsOnOtherSubs(phoneAccountHandle));
        return arrayList;
    }

    /* JADX INFO: renamed from: $r8$lambda$Ka1W_Z1I-AzoadRYptlRxdbjpyU, reason: not valid java name */
    public static /* synthetic */ boolean m4369$r8$lambda$Ka1W_Z1IAzoadRYptlRxdbjpyU(PhoneAccountHandle phoneAccountHandle, Conference conference) {
        return (conference.getState() == 4 || conference.getState() == 5) && !Objects.equals(conference.getPhoneAccountHandle(), phoneAccountHandle);
    }

    public static /* synthetic */ void $r8$lambda$dxcNB86jazQ9_TPHH_Ef2USo4_I(List list, Conference conference) {
        if (conference instanceof TelephonyConference) {
            TelephonyConference telephonyConference = (TelephonyConference) conference;
            Log.i(LOG_TAG, "disconnectAllConferenceablesOnOtherSubs: disconnect %s due to redial happened on other sub.", telephonyConference.getTelecomCallId());
            telephonyConference.onDisconnect();
            list.add(conference);
        }
    }

    private List disconnectAllCallsOnOtherSubs(final PhoneAccountHandle phoneAccountHandle) {
        Collection<Connection> allConnections = getAllConnections();
        final ArrayList arrayList = new ArrayList();
        allConnections.stream().filter(new Predicate() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TelephonyConnectionService.m4368$r8$lambda$HasMsldiI89gjkFT4lihvn_F0(phoneAccountHandle, (Connection) obj);
            }
        }).forEach(new Consumer() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TelephonyConnectionService.$r8$lambda$L4ZOYWOvtYAOBvnrW4U8u7MQJh0(arrayList, (Connection) obj);
            }
        });
        return arrayList;
    }

    /* JADX INFO: renamed from: $r8$lambda$HasMsldiI89gjkFT-4-lihvn_F0, reason: not valid java name */
    public static /* synthetic */ boolean m4368$r8$lambda$HasMsldiI89gjkFT4lihvn_F0(PhoneAccountHandle phoneAccountHandle, Connection connection) {
        return (connection.getState() == 4 || connection.getState() == 5) && !Objects.equals(connection.getPhoneAccountHandle(), phoneAccountHandle);
    }

    public static /* synthetic */ void $r8$lambda$L4ZOYWOvtYAOBvnrW4U8u7MQJh0(List list, Connection connection) {
        if (connection instanceof TelephonyConnection) {
            TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
            Log.i(LOG_TAG, "disconnectAllCallsOnOtherSubs: disconnect %s due to redial happened on other sub.", telephonyConnection.getTelecomCallId());
            telephonyConnection.hangup(3);
            list.add(connection);
        }
    }

    private int getActiveCallDomain(int i) {
        Phone phone;
        for (Connection connection : getAllConnections()) {
            if ((connection instanceof TelephonyConnection) && (phone = ((TelephonyConnection) connection).getPhone()) != null && phone.getSubId() == i) {
                if (phone instanceof GsmCdmaPhone) {
                    return 1;
                }
                if (phone instanceof ImsPhone) {
                    return 2;
                }
            }
        }
        return 0;
    }

    private void handleEmergencyCallStartedForSatelliteSOSMessageRecommender(TelephonyConnection telephonyConnection, Phone phone) {
        if (phone.getContext().getPackageManager().hasSystemFeature("android.hardware.telephony.satellite")) {
            if (this.mSatelliteSOSMessageRecommender == null) {
                this.mSatelliteSOSMessageRecommender = new SatelliteSOSMessageRecommender(phone.getContext(), phone.getContext().getMainLooper());
            }
            boolean zIsEmergencyNumberTestNumber = isEmergencyNumberTestNumber(telephonyConnection.getAddress().getSchemeSpecificPart());
            telephonyConnection.addTelephonyConnectionListener(this.mEmergencyConnectionSatelliteListener);
            this.mSatelliteSOSMessageRecommender.onEmergencyCallStarted(telephonyConnection, zIsEmergencyNumberTestNumber);
            this.mSatelliteSOSMessageRecommender.onEmergencyCallConnectionStateChanged(telephonyConnection.getTelecomCallId(), 3);
        }
    }

    public boolean isCallDisallowedDueToSatellite(Phone phone) {
        return (phone == null || !this.mSatelliteController.isInSatelliteModeForCarrierRoaming(phone) || isVoiceSupportedInSatelliteMode(phone)) ? false : true;
    }

    private boolean isCallDisallowedDueToNtnEligibility(Phone phone) {
        if (phone == null) {
            Log.d(this, "isCallDisallowedDueToNtnEligibility: phone is null", new Object[0]);
            return false;
        }
        if (!this.mSatelliteController.getLastNotifiedNtnEligibility(phone)) {
            Log.d(this, "isCallDisallowedDueToNtnEligibility: eligibility is false", new Object[0]);
            return false;
        }
        if (isVoiceSupportedInSatelliteMode(phone)) {
            Log.d(this, "isCallDisallowedDueToNtnEligibility: voice is supported", new Object[0]);
            return false;
        }
        Log.d(this, "isCallDisallowedDueToNtnEligibility: return true", new Object[0]);
        return true;
    }

    private boolean isVoiceSupportedInSatelliteMode(Phone phone) {
        if (this.mSatelliteController.getCapabilitiesForCarrierRoamingSatelliteMode(phone).contains(1)) {
            Log.d(this, "isVoiceSupportedInSatelliteMode: voice is supported", new Object[0]);
            return true;
        }
        Log.d(this, "isVoiceSupportedInSatelliteMode: voice is not supported", new Object[0]);
        return false;
    }

    private boolean getTurnOffOemEnabledSatelliteDuringEmergencyCall() {
        try {
            return getApplicationContext().getResources().getBoolean(R.bool.config_turn_off_oem_enabled_satellite_during_emergency_call);
        } catch (Resources.NotFoundException e) {
            Log.e(this, e, "getTurnOffOemEnabledSatelliteDuringEmergencyCall: ex=" + e, new Object[0]);
            return false;
        }
    }

    private boolean shouldTurnOffNonEmergencyNbIotNtnSessionForEmergencyCall() {
        try {
            return getApplicationContext().getResources().getBoolean(R.bool.config_turn_off_non_emergency_nb_iot_ntn_satellite_for_emergency_call);
        } catch (Resources.NotFoundException e) {
            Log.e(this, e, "shouldTurnOffNonEmergencyNbIotNtnSessionForEmergencyCall: ex=" + e, new Object[0]);
            return false;
        }
    }

    public void setFeatureFlags(FeatureFlags featureFlags, com.android.internal.hidden_from_bootclasspath.com.android.server.telecom.flags.FeatureFlags featureFlags2) {
        this.mFeatureFlags = featureFlags;
    }

    private void loge(String str) {
        Log.d(this, str, new Object[0]);
    }

    void notifyEccToSelfActivationSM(MtkGsmCdmaPhone mtkGsmCdmaPhone) {
        Log.d(this, "notifyEccToSelfActivationSM()", new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putInt("key_mo_call_type", 1);
        mtkGsmCdmaPhone.getSelfActivationInstance().selfActivationAction(1, bundle);
    }

    public boolean isInEmergencyCall() {
        if (this.mIsEmergencyCallPending) {
            return true;
        }
        for (Connection connection : getAllConnections()) {
            if (connection != null && (connection instanceof TelephonyConnection)) {
                TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
                return telephonyConnection.shouldTreatAsEmergencyCall() || telephonyConnection.isNetworkIdentifiedEmergencyCall();
            }
        }
        return false;
    }

    public List getAllPeerPhoneConnections(Connection connection) {
        ArrayList arrayList = new ArrayList();
        for (Connection connection2 : getAllConnections()) {
            if (!connection2.equals(connection) && !connection2.getPhoneAccountHandle().equals(connection.getPhoneAccountHandle()) && connection2.getState() != 6 && (connection2 instanceof TelephonyConnection)) {
                Log.d(this, "getAllPeerPhoneConnections: " + connection2, new Object[0]);
                arrayList.add(connection2);
            }
        }
        return arrayList;
    }

    public List getAllSamePhoneConnections(Connection connection) {
        ArrayList arrayList = new ArrayList();
        for (Connection connection2 : getAllConnections()) {
            if (!connection2.equals(connection) && connection2.getPhoneAccountHandle().equals(connection.getPhoneAccountHandle()) && connection2.getState() != 6 && (connection2 instanceof TelephonyConnection)) {
                Log.d(this, "getAllSamePhoneConnections: " + connection2, new Object[0]);
                arrayList.add(connection2);
            }
        }
        return arrayList;
    }

    class StateDisconnectedListener extends TelephonyConnection.TelephonyConnectionListener {
        private final CompletableFuture mStateDisconnectedFuture;

        StateDisconnectedListener(CompletableFuture completableFuture) {
            this.mStateDisconnectedFuture = completableFuture;
        }

        @Override // com.android.services.telephony.TelephonyConnection.TelephonyConnectionListener
        public void onDisconnected(Connection connection, DisconnectCause disconnectCause) {
            this.mStateDisconnectedFuture.complete(Boolean.TRUE);
            ((TelephonyConnection) connection).removeTelephonyConnectionListener(this);
        }
    }

    private CompletableFuture listenForDisconnectedStateChanged(Conferenceable conferenceable) {
        CompletableFuture completableFuture = new CompletableFuture();
        addTelephonyConnectionListener(conferenceable, new StateDisconnectedListener(completableFuture));
        return completableFuture;
    }

    private void delayDialForAllConnectionDisconnected(Phone phone, Conferenceable conferenceable, Consumer consumer) {
        if (conferenceable == null || phone == null) {
            consumer.accept(Boolean.FALSE);
            return;
        }
        try {
            CompletableFuture completableFutureListenForDisconnectedStateChanged = listenForDisconnectedStateChanged(conferenceable);
            final CompletableFuture completableFuture = new CompletableFuture();
            phone.getContext().getMainThreadHandler().postDelayed(new Runnable() { // from class: com.android.services.telephony.TelephonyConnectionService$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    completableFuture.complete(Boolean.FALSE);
                }
            }, 10000L);
            completableFutureListenForDisconnectedStateChanged.acceptEitherAsync((CompletionStage) completableFuture, consumer, phone.getContext().getMainExecutor());
        } catch (Exception e) {
            Log.w(this, "delayEmergencyDialForConfereneableDisconnected - exception= " + e.getMessage(), new Object[0]);
            consumer.accept(Boolean.FALSE);
        }
    }
}
