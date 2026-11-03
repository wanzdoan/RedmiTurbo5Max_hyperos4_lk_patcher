package com.android.phone.statistics;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class MiDataChannelStatistic {
    private static MiDataChannelStatistic sInstance;
    private final ConcurrentHashMap mBdcStatus = new ConcurrentHashMap();
    private final ConcurrentHashMap mAdcRequests = new ConcurrentHashMap();
    private final ConcurrentHashMap mAdcResponses = new ConcurrentHashMap();

    public void processBdcRequest(String str) {
        Log.i("MiDataChannelStatistic", "processBdcRequest " + str);
        this.mBdcStatus.put(str, Boolean.FALSE);
    }

    public void processAdcRequest(String str, String str2) {
        Log.i("MiDataChannelStatistic", "processAdcRequest " + str + " " + str2);
        ((Set) this.mAdcRequests.computeIfAbsent(str, new Function() { // from class: com.android.phone.statistics.MiDataChannelStatistic$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return MiDataChannelStatistic.$r8$lambda$Z8AcXYuDjtkxp0iFkeuNih4MSDs((String) obj);
            }
        })).add(str2);
    }

    public static /* synthetic */ Set $r8$lambda$Z8AcXYuDjtkxp0iFkeuNih4MSDs(String str) {
        return new HashSet();
    }

    public static synchronized MiDataChannelStatistic getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new MiDataChannelStatistic();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private void remove(String str) {
        Log.i("MiDataChannelStatistic", "remove " + str);
        this.mBdcStatus.remove(str);
        this.mAdcRequests.remove(str);
        this.mAdcResponses.remove(str);
    }

    public void recordDataChannelInfoCallStatistics(HashMap map, String str) {
        if (str == null) {
            return;
        }
        int iIndexOf = str.indexOf(95);
        if (iIndexOf != -1) {
            str = str.substring(0, iIndexOf);
        }
        if (this.mBdcStatus.containsKey(str)) {
            Log.i("MiDataChannelStatistic", "recordDcCallStatistics telecomId ".concat(str));
            map.put("bootstrap_data_channel_success", String.valueOf(this.mBdcStatus.get(str)));
            Log.i("MiDataChannelStatistic", "bootstrap data channel status " + this.mBdcStatus.get(str));
            Set<String> set = (Set) this.mAdcRequests.getOrDefault(str, new ConcurrentSkipListSet());
            Set set2 = (Set) this.mAdcResponses.getOrDefault(str, new ConcurrentSkipListSet());
            StringBuilder sb = new StringBuilder();
            for (String str2 : set) {
                String str3 = String.format("DC Label: %s, Success: %b", str2, Boolean.valueOf(set2.contains(str2)));
                sb.append(str3);
                sb.append(";");
                Log.i("MiDataChannelStatistic", str3);
            }
            map.put("application_data_channel_info", sb.toString());
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "data_channel_info", map);
            remove(str);
        }
    }

    public void recordDataChannelEnable(Bundle bundle) {
        int i;
        if (bundle == null || (i = bundle.getInt("phoneId", -1)) == -1) {
            return;
        }
        HashMap map = new HashMap();
        boolean z = bundle.getBoolean(a.J, false);
        map.put("SimOperator", TelephonyManager.from(PhoneGlobals.getInstance()).getSimOperatorNumericForPhone(i));
        map.put("dataChannelStatus", String.valueOf(z));
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "data_channel_enable", map);
    }

    public boolean isDataChannelCall(String str) {
        if (str == null) {
            return false;
        }
        int iIndexOf = str.indexOf(95);
        if (iIndexOf != -1) {
            str = str.substring(0, iIndexOf);
        }
        if (!this.mBdcStatus.containsKey(str)) {
            return false;
        }
        Log.i("MiDataChannelStatistic", "isDataChannelCall telecomId " + str + " " + this.mBdcStatus.get(str));
        return ((Boolean) this.mBdcStatus.get(str)).booleanValue();
    }
}
