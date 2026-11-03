package com.android.services.telephony.relay.phonecall;

import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.DisconnectCauseUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;

/* JADX INFO: loaded from: classes.dex */
public class RelayConnection extends Connection {
    private static final int NOT_VALID = -1;
    private static final String TAG = "RelayConnection";
    private String mHostName;
    private RelayConnectionService mRelayConnectionService;
    private int mAnswerCount = 0;
    private int mDisconnectCount = 0;
    private int mDisconnectCause = -1;

    public RelayConnection(boolean z, String str) {
        setCallDirection(z ? 1 : 0);
        setTelecomCallId(str);
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            relayManager.getRelayConnectionsManager().addRelayConnection(this);
        }
    }

    public void updateState(int i, int i2) {
        if (getState() == i) {
            return;
        }
        if (i == 4) {
            setActive();
            return;
        }
        if (i == 5) {
            setOnHold();
        } else {
            if (i != 6) {
                return;
            }
            this.mDisconnectCause = i2;
            if (i2 == -1) {
                i2 = 3;
            }
            forceDisconnected(DisconnectCauseUtil.toTelecomDisconnectCause(i2));
        }
    }

    @Override // android.telecom.Connection
    public void onDisconnect() {
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            relayManager.sendOperateRequest(this, 2);
        }
        this.mDisconnectCount++;
        maybeClearCallsWhenError();
        RelayLog.logi(TAG, "onDisconnect");
    }

    @Override // android.telecom.Connection
    public void onAbort() {
        super.onAbort();
    }

    @Override // android.telecom.Connection
    public void onHold() {
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            relayManager.sendOperateRequest(this, 3);
        }
    }

    @Override // android.telecom.Connection
    public void onUnhold() {
        super.onUnhold();
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            relayManager.sendOperateRequest(this, 4);
        }
    }

    @Override // android.telecom.Connection
    public void onAnswer(int i) {
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            relayManager.sendOperateRequest(this, 0);
        }
        this.mAnswerCount++;
        maybeClearCallsWhenError();
        RelayLog.logi(TAG, "onAnswer videoState:" + i);
    }

    @Override // android.telecom.Connection
    public void onReject() {
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            relayManager.sendOperateRequest(this, 1);
        }
        this.mDisconnectCount++;
        maybeClearCallsWhenError();
        RelayLog.logi(TAG, "onReject");
    }

    @Override // android.telecom.Connection
    public void onReject(String str) {
        super.onReject(str);
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            relayManager.sendOperateRequest(this, 1);
        }
        this.mDisconnectCount++;
        maybeClearCallsWhenError();
        RelayLog.logi(TAG, "onReject replyMessage:" + str);
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

    public final void setRelayConnectionService(RelayConnectionService relayConnectionService) {
        this.mRelayConnectionService = relayConnectionService;
    }

    public final RelayConnectionService getRelayConnectionService() {
        return this.mRelayConnectionService;
    }

    public void tearDown() {
        setRelayConnectionService(null);
        if (getState() != 6) {
            setDisconnected(getDisconnectCause("Local Disconnect for relay disconnected!"));
            destroy();
        }
    }

    public DisconnectCause getDisconnectCause(String str) {
        return DisconnectCauseUtil.toTelecomDisconnectCause(3, str);
    }

    public final void forceDisconnected(DisconnectCause disconnectCause) {
        super.setDisconnected(disconnectCause);
        RelayManager relayManager = RelayUtils.Relay.getRelayManager();
        if (relayManager != null) {
            relayManager.getRelayConnectionsManager().removeRelayConnection(this);
        }
        destroy();
    }

    public void maybeClearCallsWhenError() {
        if (this.mDisconnectCount == 3 || this.mAnswerCount == 3) {
            forceDisconnected(getDisconnectCause("Local operate faied, so clear relay call!"));
            this.mDisconnectCount = 0;
            this.mAnswerCount = 0;
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_relay_call_operator_stuck");
        }
    }

    public synchronized void setHostName(String str) {
        this.mHostName = str;
    }

    public synchronized String getHostName() {
        return this.mHostName;
    }
}
