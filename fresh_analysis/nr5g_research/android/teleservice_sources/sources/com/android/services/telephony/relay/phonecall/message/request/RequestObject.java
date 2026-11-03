package com.android.services.telephony.relay.phonecall.message.request;

import android.os.SystemClock;
import com.android.services.telephony.relay.phonecall.message.api.RelayApi;

/* JADX INFO: loaded from: classes.dex */
public class RequestObject {
    private final String mMethodId = generateMessageId();
    private String mParamJson;
    private RelayApi mRelayApi;

    public String getMethodName() {
        RelayApi relayApi = this.mRelayApi;
        if (relayApi != null) {
            return relayApi.getMethodName();
        }
        return null;
    }

    public String getMethodId() {
        return this.mMethodId;
    }

    public void setRelayApi(RelayApi relayApi) {
        this.mRelayApi = relayApi;
    }

    public String getParamJson() {
        return this.mParamJson;
    }

    public void setParamJson(String str) {
        this.mParamJson = str;
    }

    public static String generateMessageId() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime > 2147483647L) {
            while (jElapsedRealtime > 2147483647L) {
                jElapsedRealtime -= 2147483647L;
            }
        }
        return String.valueOf(jElapsedRealtime);
    }
}
