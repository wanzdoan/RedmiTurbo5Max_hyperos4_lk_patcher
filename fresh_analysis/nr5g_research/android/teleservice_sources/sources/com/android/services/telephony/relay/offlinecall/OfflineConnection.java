package com.android.services.telephony.relay.offlinecall;

import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import com.android.services.telephony.DisconnectCauseUtil;

/* JADX INFO: loaded from: classes.dex */
public class OfflineConnection extends Connection {
    private static final String TAG = "OfflineConnection";
    private OfflineConnectionService mOfflineConnectionService;

    @Override // android.telecom.Connection
    public void onHold() {
    }

    public OfflineConnection(boolean z, String str) {
        setCallDirection(z ? 1 : 0);
        setTelecomCallId(str);
        OfflineManager offlineManager = OfflineCallsManager.getInstance().getOfflineManager();
        if (offlineManager != null) {
            offlineManager.getOfflineConnectionsManager().addOfflineConnection(this);
        }
    }

    public void updateState(int i) {
        if (getState() == i) {
            return;
        }
        if (i == 4) {
            setActive();
        } else if (i == 5) {
            setOnHold();
        } else {
            if (i != 6) {
                return;
            }
            localDisconnected();
        }
    }

    @Override // android.telecom.Connection
    public void onDisconnect() {
        OfflineManager offlineManager = OfflineCallsManager.getInstance().getOfflineManager();
        if (offlineManager != null) {
            offlineManager.endCall(this);
        }
        localDisconnected();
        OfflineCallLog.logi(TAG, "onDisconnect");
    }

    @Override // android.telecom.Connection
    public void onAbort() {
        super.onAbort();
    }

    @Override // android.telecom.Connection
    public void onUnhold() {
        super.onUnhold();
    }

    @Override // android.telecom.Connection
    public void onAnswer(int i) {
        OfflineManager offlineManager = OfflineCallsManager.getInstance().getOfflineManager();
        if (offlineManager != null) {
            offlineManager.answerCall(this);
        }
        OfflineCallLog.logi(TAG, "onAnswer videoState:" + i);
    }

    @Override // android.telecom.Connection
    public void onReject() {
        super.onReject();
        OfflineManager offlineManager = OfflineCallsManager.getInstance().getOfflineManager();
        if (offlineManager != null) {
            offlineManager.rejectCall(this);
        }
        localDisconnected();
        OfflineCallLog.logi(TAG, "onReject");
    }

    @Override // android.telecom.Connection
    public void onReject(String str) {
        super.onReject(str);
        OfflineManager offlineManager = OfflineCallsManager.getInstance().getOfflineManager();
        if (offlineManager != null) {
            offlineManager.rejectCall(this);
        }
        localDisconnected();
        OfflineCallLog.logi(TAG, "onReject replyMessage:" + str);
    }

    @Override // android.telecom.Connection
    public void onSilence() {
        super.onSilence();
    }

    @Override // android.telecom.Connection
    public void onPostDialContinue(boolean z) {
        super.onPostDialContinue(z);
    }

    @Override // android.telecom.Connection
    public void onCallEvent(String str, Bundle bundle) {
        super.onCallEvent(str, bundle);
    }

    @Override // android.telecom.Connection
    public void sendConnectionEvent(String str, Bundle bundle) {
        super.sendConnectionEvent(str, bundle);
    }

    public final void setOfflineConnectionService(OfflineConnectionService offlineConnectionService) {
        this.mOfflineConnectionService = offlineConnectionService;
    }

    public final OfflineConnectionService getOfflineConnectionService() {
        return this.mOfflineConnectionService;
    }

    public void tearDown() {
        setOfflineConnectionService(null);
        if (getState() != 6) {
            setDisconnected(getDisconnectCause("Local Disconnect for relay disconnected!"));
            destroy();
        }
    }

    public DisconnectCause getDisconnectCause(String str) {
        return DisconnectCauseUtil.toTelecomDisconnectCause(2, str);
    }

    public final void forceDisconnected(DisconnectCause disconnectCause) {
        super.setDisconnected(disconnectCause);
        OfflineManager offlineManager = OfflineCallsManager.getInstance().getOfflineManager();
        if (offlineManager != null) {
            offlineManager.getOfflineConnectionsManager().removeOfflineConnection(this);
        }
        destroy();
    }

    public final void localDisconnected() {
        forceDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(2));
        OfflineCallLog.logi(TAG, "localDisconnected");
    }
}
