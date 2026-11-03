package com.android.services.telephony.relay.phonecall.message.request;

import android.util.Log;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.GsonUtil;
import com.xiaomi.continuity.networking.NetworkingManager;

/* JADX INFO: loaded from: classes.dex */
public class RelayRequestBase {
    public String address;
    public String requestDeviceId;

    public RelayRequestBase() {
        try {
            NetworkingManager networkingManager = NetworkingManager.getInstance(RelayUtils.System.getContext());
            if (networkingManager != null && networkingManager.getLocalDeviceInfo() != null) {
                Log.i("RelayRequestBase", "getLocalDeviceInfo() is not null");
                this.requestDeviceId = networkingManager.getLocalDeviceInfo().getDeviceId();
            } else {
                Log.w("RelayRequestBase", "NetworkingManager or NetworkingManager.getLocalDeviceInfo() is null");
                this.requestDeviceId = "";
            }
        } catch (Exception e) {
            this.requestDeviceId = "";
            Log.w("RelayRequestBase", "Exception when getting deviceId", e);
        }
    }

    public String toJson() {
        return GsonUtil.toJson(this);
    }
}
