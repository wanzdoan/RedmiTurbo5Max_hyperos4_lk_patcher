package com.android.phone.utils;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import com.xiaomi.mirror.RemoteDeviceInfo;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class OneTrackImpl {
    private static OneTrack sOneTrack;

    public static void init(Context context) {
        OneTrack oneTrackCreateInstance = OneTrack.createInstance(context, new Configuration.Builder().setAppId("31000000083").setChannel(RemoteDeviceInfo.MANUFACTURER_XIAOMI).setMode(OneTrack.Mode.APP).setExceptionCatcherEnable(false).setAutoTrackActivityAction(false).build());
        sOneTrack = oneTrackCreateInstance;
        if (oneTrackCreateInstance == null) {
            return;
        }
        OneTrack.setUseSystemNetTrafficOnly();
        OneTrack.setAccessNetworkEnable(context, isProvisionClauseAgreed(context));
    }

    public static void trackObjectEvent(String str, Map map) {
        if (sOneTrack == null) {
            return;
        }
        HashMap map2 = new HashMap();
        if (map != null) {
            map2.putAll(map);
        }
        map2.put("hwLevel", SystemProperties.get("ro.boot.hwlevel", "-1"));
        com.android.phone.statistics.utils.Utils.logD("OneTrackImpl", "trackObjectEvent: " + str + ", params: " + map2.toString());
        sOneTrack.track(str, map2);
    }

    public static void trackEvent(String str, Map map) {
        if (sOneTrack == null) {
            return;
        }
        HashMap map2 = new HashMap();
        if (map != null) {
            map2.putAll(map);
        }
        map2.put("hwLevel", SystemProperties.get("ro.boot.hwlevel", "-1"));
        com.android.phone.statistics.utils.Utils.logD("OneTrackImpl", "trackEvent: " + str + ", params: " + map2.toString());
        sOneTrack.track(str, map2);
    }

    public static void trackEvent(String str) {
        if (sOneTrack == null) {
            return;
        }
        com.android.phone.statistics.utils.Utils.logD("OneTrackImpl", "trackEvent: " + str);
        sOneTrack.track(str, null);
    }

    private static boolean isProvisionClauseAgreed(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "clause_agreed", 0) != 0;
    }

    public static void enableDebugMode() {
        OneTrack.setDebugMode(true);
    }

    public static void enableOnetrackStatAccessNetwork(Context context) {
        OneTrack.setAccessNetworkEnable(context, true);
    }

    public static String getInstanceId() {
        OneTrack oneTrack = sOneTrack;
        if (oneTrack != null) {
            return oneTrack.getInstanceId();
        }
        return null;
    }
}
