package com.android.services.telephony.relay.network;

import android.util.Log;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRelayStats {
    private static final String KEY_NETWORK_RELAY_DELAY = "network_relay_delay";
    private static final String KEY_PREFIX_FOR_CLIENT = "client_";
    private static final String KEY_PREFIX_FOR_SERVER = "server_";
    private static final String LOG_TAG = "NetworkRelayStats";
    private static NetworkRelayStats sInstance;
    private Map mStats = new HashMap();

    public static synchronized NetworkRelayStats getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new NetworkRelayStats();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private NetworkRelayStats() {
        Log.d(LOG_TAG, "NetworkRelayStats init");
    }

    public void recordErrorCode(boolean z, int i) {
        String str;
        if (z) {
            str = KEY_PREFIX_FOR_SERVER + i;
        } else {
            str = KEY_PREFIX_FOR_CLIENT + i;
        }
        Long l = (Long) this.mStats.get(str);
        Map map = this.mStats;
        if (l == null) {
            map.put(str, 1L);
        } else {
            map.put(str, Long.valueOf(l.longValue() + 1));
        }
    }

    public void recordRelayDelay(long j) {
        this.mStats.put(KEY_NETWORK_RELAY_DELAY, Long.valueOf(j));
    }

    public void reportStatistics() {
        Map map = this.mStats;
        if (map == null || map.isEmpty()) {
            return;
        }
        HashMap map2 = new HashMap(this.mStats);
        Log.d(LOG_TAG, "reportStatistics mStats=" + this.mStats);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_network_relay", map2);
        this.mStats.clear();
    }
}
