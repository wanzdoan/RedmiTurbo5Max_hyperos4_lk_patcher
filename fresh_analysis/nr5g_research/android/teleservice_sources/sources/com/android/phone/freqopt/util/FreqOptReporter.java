package com.android.phone.freqopt.util;

import com.android.phone.utils.MiStatInterfaceUtil;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class FreqOptReporter {
    public static void reportEvent(String str, String str2, String str3, String str4, int i, String str5) {
        HashMap map = new HashMap();
        map.put("strategy_id", str2);
        putIfNonNull(map, "source_cell", str3);
        putIfNonNull(map, "target_cell", str4);
        if (i > 0) {
            map.put("count", Integer.valueOf(i));
        }
        putIfNonNull(map, "extra", str5);
        doReport(str, str2, map);
    }

    public static void reportStateEvent(String str, String str2, String str3, String str4, int i, String str5) {
        HashMap map = new HashMap();
        map.put("strategy_id", str2);
        map.put("connection_state", i == 0 ? "connected" : "idle");
        putIfNonNull(map, "source_cell", str3);
        putIfNonNull(map, "target_cell", str4);
        putIfNonNull(map, "extra", str5);
        doReport(str, str2, map);
    }

    private static void putIfNonNull(HashMap map, String str, String str2) {
        if (str2 != null) {
            map.put(str, str2);
        }
    }

    private static void doReport(String str, String str2, HashMap map) {
        StringBuilder sb = new StringBuilder("[FreqOpt][");
        sb.append(str2);
        sb.append("] ");
        sb.append(str);
        for (Map.Entry entry : map.entrySet()) {
            if (!"strategy_id".equals(entry.getKey())) {
                sb.append(", ");
                sb.append((String) entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
            }
        }
        LogUtils.d("Freq_Opt_Reporter", sb.toString());
        MiStatInterfaceUtil.recordObjectEvent("telephony", str, map);
    }
}
