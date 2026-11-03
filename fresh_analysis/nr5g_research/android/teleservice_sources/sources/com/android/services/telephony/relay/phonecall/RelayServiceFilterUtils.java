package com.android.services.telephony.relay.phonecall;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class RelayServiceFilterUtils {
    private static final String KEY_DEVICE_ANSWERED = "pref_device_answered";
    private static final String TAG = "RelayServiceFilterUtils";
    private static final int[] TARGET_DEVICE_TYPE = {2, 4};

    private RelayServiceFilterUtils() {
    }

    public static void updateFilteredServiceInfo(Map map, Map map2) {
        if (map == null) {
            map = new HashMap();
        }
        if (map2 == null) {
            map2 = new HashMap();
        }
        map.clear();
        String deviceAnswered = getDeviceAnswered();
        RelayLog.logi(TAG, "Answered device is " + deviceAnswered);
        if (map2.containsKey(deviceAnswered)) {
            map.put(deviceAnswered, (RelayServiceInfo) map2.get(deviceAnswered));
        }
        for (String str : map2.keySet()) {
            if (map.size() == 10) {
                break;
            }
            RelayServiceInfo relayServiceInfo = (RelayServiceInfo) map2.get(str);
            int i = relayServiceInfo.mDeviceType;
            if (!hasDeviceType(map, i)) {
                int[] iArr = TARGET_DEVICE_TYPE;
                if (i == iArr[0] || i == iArr[1] || i == 11 || i == 1 || i == 16777280) {
                    map.put(str, relayServiceInfo);
                }
            }
        }
        RelayLog.logi(TAG, "Filtered device size " + map.size());
    }

    private static boolean hasDeviceType(Map map, int i) {
        if (i == 11 || i == 1 || i == 16777280) {
            return false;
        }
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            if (i == ((RelayServiceInfo) map.get((String) it.next())).mDeviceType) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasRelaying() {
        RelayConnectionsManager relayConnectionsManager;
        RelayManager relayManager = PhoneContinuityController.getInstance().getRelayManager();
        boolean z = false;
        if (relayManager != null && (relayConnectionsManager = relayManager.getRelayConnectionsManager()) != null && relayConnectionsManager.getLocalConnectionCallRelaying() != null) {
            z = true;
        }
        RelayLog.logi(TAG, "hasRelaying, " + z);
        return z;
    }

    private static boolean hasRelayed() {
        RelayConnectionsManager relayConnectionsManager;
        RelayManager relayManager = PhoneContinuityController.getInstance().getRelayManager();
        boolean z = false;
        if (relayManager != null && (relayConnectionsManager = relayManager.getRelayConnectionsManager()) != null && relayConnectionsManager.getLocalConnectionCallRelayed() != null) {
            z = true;
        }
        RelayLog.logi(TAG, "hasRelayed, " + z);
        return z;
    }

    public static void saveDeviceAnswered(String str) {
        getSharedPreferences(RelayUtils.System.getContext()).edit().putString(KEY_DEVICE_ANSWERED, str).apply();
        RelayLog.logi(TAG, "Save device answered " + str);
    }

    private static String getDeviceAnswered() {
        return getSharedPreferences(RelayUtils.System.getContext()).getString(KEY_DEVICE_ANSWERED, "");
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(getContext(context));
    }

    private static Context getContext(Context context) {
        Context contextCreateDeviceProtectedStorageContext = context.createDeviceProtectedStorageContext();
        return contextCreateDeviceProtectedStorageContext != null ? contextCreateDeviceProtectedStorageContext : context;
    }
}
