package com.android.services.telephony;

import android.content.Context;
import android.telecom.Connection;
import android.telecom.ConnectionService;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.IMiConnectModule;
import com.android.services.telephony.relay.network.NetworkRelayStats;
import com.android.services.telephony.relay.offlinecall.OfflineCallsManager;
import com.android.services.telephony.relay.offlinecall.OfflineConnection;
import com.android.services.telephony.relay.offlinecall.OfflineConnectionsManager;
import com.android.services.telephony.relay.offlinecall.OfflineManager;
import com.android.services.telephony.relay.offlinecall.OfflineUtils;
import com.android.services.telephony.relay.phonecall.MiPhoneContinuityController;
import com.android.services.telephony.relay.phonecall.PhoneContinuityController;
import com.android.services.telephony.relay.phonecall.RelayConnection;
import com.android.services.telephony.relay.phonecall.RelayConnectionsManager;
import com.android.services.telephony.relay.phonecall.RelayManager;
import com.android.services.telephony.relay.phonecall.RelayServiceInfo;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import miui.telephony.FeatureConfiguration;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiConnectDelegateImpl implements IMiConnectModule {
    private static volatile MiConnectDelegateImpl sInstance;
    private Lazy mPhoneContinuity = new Lazy();
    private Lazy mMiPhoneContinuity = new Lazy();
    private Lazy mOfflineCalls = new Lazy();
    private Lazy mNetworkRelayStats = new Lazy();

    @Override // com.android.phone.module.IMiConnectModule
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public static MiConnectDelegateImpl init() {
        if (sInstance == null) {
            synchronized (MiConnectDelegateImpl.class) {
                try {
                    if (sInstance == null) {
                        MiConnectDelegateImpl miConnectDelegateImpl = new MiConnectDelegateImpl();
                        ModuleDelegate.register(IMiConnectModule.class, miConnectDelegateImpl);
                        sInstance = miConnectDelegateImpl;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private MiConnectDelegateImpl() {
    }

    public void setPhoneContinuity(PhoneContinuityController phoneContinuityController) {
        this.mPhoneContinuity.set(phoneContinuityController);
    }

    public void setMiPhoneContinuity(MiPhoneContinuityController miPhoneContinuityController) {
        this.mMiPhoneContinuity.set(miPhoneContinuityController);
    }

    public void setOfflineCalls(OfflineCallsManager offlineCallsManager) {
        this.mOfflineCalls.set(offlineCallsManager);
    }

    public void setNetworkRelayStats(NetworkRelayStats networkRelayStats) {
        this.mNetworkRelayStats.set(networkRelayStats);
    }

    @Override // com.android.phone.module.IMiConnectModule
    public void onAnswer(TelephonyConnection telephonyConnection, int i) {
        if (PhoneDebug.VDBG) {
            android.util.Log.i("MiConnectDelegate", "onAnswer, " + RelayUtils.Telephony.getNumber(telephonyConnection));
        } else {
            android.util.Log.i("MiConnectDelegate", "onAnswer");
        }
        if (!RelayUtils.Relay.isCallRelayAnswered(telephonyConnection)) {
            Connection connectionResolveRelayConnection = resolveRelayConnection(telephonyConnection);
            if (this.mPhoneContinuity.get() != null) {
                ((PhoneContinuityController) this.mPhoneContinuity.get()).forceReleaseRelayCall(connectionResolveRelayConnection, 0);
            }
        }
        releaseRelayCall();
        disconnectOfflineCall();
    }

    private Connection resolveRelayConnection(TelephonyConnection telephonyConnection) {
        RelayManager relayManager;
        RelayConnectionsManager relayConnectionsManager;
        Connection localConnectionCallRelayed;
        return (RelayUtils.Relay.isCallRelayed(telephonyConnection) || this.mPhoneContinuity.get() == null || (relayManager = ((PhoneContinuityController) this.mPhoneContinuity.get()).getRelayManager()) == null || (relayConnectionsManager = relayManager.getRelayConnectionsManager()) == null || (localConnectionCallRelayed = relayConnectionsManager.getLocalConnectionCallRelayed()) == null) ? telephonyConnection : localConnectionCallRelayed;
    }

    private void disconnectOfflineCall() {
        OfflineManager offlineManager;
        OfflineConnectionsManager offlineConnectionsManager;
        OfflineConnection connectionOfflineCall;
        if (this.mOfflineCalls.get() == null || (offlineManager = ((OfflineCallsManager) this.mOfflineCalls.get()).getOfflineManager()) == null || (offlineConnectionsManager = offlineManager.getOfflineConnectionsManager()) == null || (connectionOfflineCall = offlineConnectionsManager.getConnectionOfflineCall()) == null) {
            return;
        }
        connectionOfflineCall.onDisconnect();
    }

    private void releaseRelayCall() {
        RelayManager relayManager;
        RelayConnectionsManager relayConnectionsManager;
        if (this.mMiPhoneContinuity.get() == null || (relayManager = ((MiPhoneContinuityController) this.mMiPhoneContinuity.get()).getRelayManager()) == null || (relayConnectionsManager = relayManager.getRelayConnectionsManager()) == null) {
            return;
        }
        for (RelayConnection relayConnection : relayConnectionsManager.getRelayConnections()) {
            RelayUtils.System.showToast(RelayUtils.System.getContext().getString(R.string.relay_call_fallback_to_original_device, relayConnection.getHostName()));
            relayManager.relayRelease(relayConnection.getAddress().getSchemeSpecificPart(), 7);
        }
    }

    @Override // com.android.phone.module.IMiConnectModule
    public void onRelayCallStateChanged(TelephonyConnection telephonyConnection) {
        if (this.mPhoneContinuity.get() == null) {
            return;
        }
        ((PhoneContinuityController) this.mPhoneContinuity.get()).onRelayCallStateChanged(telephonyConnection);
    }

    @Override // com.android.phone.module.IMiConnectModule
    public void removeLocalConnection(Connection connection) {
        if (this.mPhoneContinuity.get() == null) {
            return;
        }
        ((PhoneContinuityController) this.mPhoneContinuity.get()).removeLocalConnection(connection);
    }

    @Override // com.android.phone.module.IMiConnectModule
    public void onCreateConnectionComplete(Context context, ConnectionService connectionService, Connection connection) {
        android.util.Log.i("MiConnectDelegate", "onCreateConnectionComplete");
        RelayUtils.Relay.setCurrentConnection(connectionService.getAllConnections(), connection);
        RelayUtils.Relay.maybeRelayCall(context, connectionService.getAllConnections(), connection, null);
        OfflineUtils.Offline.maybeDisconnectCall(connection);
    }

    @Override // com.android.phone.module.IMiConnectModule
    public void reportStatistics() {
        if (!TelephonyManager.getDefault().isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_RELAY_NETWORK) || this.mNetworkRelayStats.get() == null) {
            return;
        }
        ((NetworkRelayStats) this.mNetworkRelayStats.get()).reportStatistics();
    }

    @Override // com.android.phone.module.IMiConnectModule
    public void responsePhoneStateIdle(Context context) {
        int i;
        if (this.mPhoneContinuity.get() == null || RelayUtils.Relay.isRelaySide() || !((PhoneContinuityController) this.mPhoneContinuity.get()).getRelayAudioServiceState()) {
            return;
        }
        ((PhoneContinuityController) this.mPhoneContinuity.get()).onRelayEnd(context);
        RelayManager relayManager = ((PhoneContinuityController) this.mPhoneContinuity.get()).getRelayManager();
        if (relayManager != null) {
            for (String str : ((PhoneContinuityController) this.mPhoneContinuity.get()).getConnectedServiceDevices()) {
                RelayServiceInfo relayServiceInfo = ((PhoneContinuityController) this.mPhoneContinuity.get()).getRelayServiceInfo(str);
                if (relayServiceInfo != null && ((i = relayServiceInfo.mDeviceType) == 2 || i == 1)) {
                    relayManager.sendCallStateIdle(str);
                    ((PhoneContinuityController) this.mPhoneContinuity.get()).scheduleDestroyRelayChannel(str);
                }
            }
        }
    }
}
