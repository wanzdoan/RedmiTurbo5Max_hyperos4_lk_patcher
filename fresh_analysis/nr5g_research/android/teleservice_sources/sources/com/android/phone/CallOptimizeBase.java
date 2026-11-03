package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.internal.telephony.InternalTelephonyUtils;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallOptimizeBase {
    protected static volatile CallOptimizeBase sInstance;
    protected CallOptimizePreferenceUtils mPreferenceUtils;
    public volatile boolean mCloudCallMessageVonrOn = true;
    private ConcurrentHashMap mCallStats = new ConcurrentHashMap();

    public abstract boolean isDisableVoNrByOpt(int i);

    public abstract boolean isModemVonrOptimizeOperator();

    protected abstract void log(String str);

    public abstract void updateVonrSwitchState(int i, boolean z);

    public static CallOptimizeBase getInstance() {
        return sInstance;
    }

    protected CallOptimizeBase() {
    }

    protected void initCommon() {
        log("initCommon");
        this.mPreferenceUtils = new CallOptimizePreferenceUtils(this, PhoneGlobals.getInstance());
        if (MiuiPhoneUtils.isSupportMtkGlobalCallOptimization() || MiuiPhoneUtils.isSupportCallMessageOptimization()) {
            reloadCallStatisticFromSp();
        }
    }

    public boolean shouldDoCallVonrOpt() {
        return MiuiPhoneUtils.isSupportCallMessageOptimization() && InternalTelephonyUtils.isChinaSimOperator(MiuiPhoneUtils.getActiveDataPhoneId()) && this.mCloudCallMessageVonrOn;
    }

    private void reloadCallStatisticFromSp() {
        try {
            CallOptimizePreferenceUtils callOptimizePreferenceUtils = this.mPreferenceUtils;
            if (callOptimizePreferenceUtils == null) {
                Utils.logD("CallOptimizeBase", "reloadCallStatisticFromSp: mPreferenceUtils is null");
                return;
            }
            String string = callOptimizePreferenceUtils.getString("CallQualityStatisticStats", "");
            if (string.isEmpty()) {
                return;
            }
            this.mCallStats = (ConcurrentHashMap) ObjectSerializer.deserialize(string);
        } catch (Exception e) {
            Utils.logD("CallOptimizeBase", "set game statistic stats exception:" + e);
        }
    }

    public void reportCallStatistic() {
        log("reportCallStatistic");
        ConcurrentHashMap concurrentHashMap = this.mCallStats;
        if (concurrentHashMap != null) {
            Iterator it = concurrentHashMap.entrySet().iterator();
            while (it.hasNext()) {
                MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_call_optimization_statistic", (Map) ((Map.Entry) it.next()).getValue());
            }
        }
        clearCallStatistic();
    }

    private void clearCallStatistic() {
        this.mCallStats.clear();
        try {
            CallOptimizePreferenceUtils callOptimizePreferenceUtils = this.mPreferenceUtils;
            if (callOptimizePreferenceUtils != null) {
                callOptimizePreferenceUtils.putString("CallQualityStatisticStats", ObjectSerializer.serialize(this.mCallStats));
            }
        } catch (Exception e) {
            Utils.logD("CallOptimizeBase", "set call statistic stats exception:" + e);
        }
    }

    public void recordCount(String str, String str2, String str3) {
        Object obj;
        ConcurrentHashMap concurrentHashMap = this.mCallStats;
        if (concurrentHashMap == null) {
            return;
        }
        ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) concurrentHashMap.get(str2);
        int iIntValue = (concurrentHashMap2 == null || (obj = concurrentHashMap2.get(str)) == null) ? 0 : ((Integer) obj).intValue();
        if (concurrentHashMap2 == null) {
            concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap2.put("plmn", str3);
        }
        concurrentHashMap2.put(str, Integer.valueOf(iIntValue + 1));
        this.mCallStats.put(str2, concurrentHashMap2);
        log("recordCount key:" + str + " Count:" + iIntValue + "1 mCallStats:" + this.mCallStats.toString());
        try {
            CallOptimizePreferenceUtils callOptimizePreferenceUtils = this.mPreferenceUtils;
            if (callOptimizePreferenceUtils != null) {
                callOptimizePreferenceUtils.putString("CallQualityStatisticStats", ObjectSerializer.serialize(this.mCallStats));
            }
        } catch (Exception e) {
            Utils.logD("CallOptimizeBase", "set call counts stats exception:" + e);
        }
    }

    public void recordStatus(String str, String str2, String str3) {
        ConcurrentHashMap concurrentHashMap = this.mCallStats;
        if (concurrentHashMap == null) {
            return;
        }
        ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) concurrentHashMap.get(str2);
        if (concurrentHashMap2 == null) {
            concurrentHashMap2 = new ConcurrentHashMap();
        }
        concurrentHashMap2.put(str, str3);
        this.mCallStats.put(str2, concurrentHashMap2);
        try {
            CallOptimizePreferenceUtils callOptimizePreferenceUtils = this.mPreferenceUtils;
            if (callOptimizePreferenceUtils != null) {
                callOptimizePreferenceUtils.putString("CallQualityStatisticStats", ObjectSerializer.serialize(this.mCallStats));
            }
        } catch (Exception e) {
            Utils.logD("CallOptimizeBase", "set call status stats exception:" + e);
        }
    }

    public class CallOptimizePreferenceUtils {
        private final int MODE = 0;
        private final SharedPreferences mSp;

        public CallOptimizePreferenceUtils(CallOptimizeBase callOptimizeBase, Context context) {
            this.mSp = context.getSharedPreferences("Call_Optimize", 0);
        }

        public String getString(String str, String str2) {
            return this.mSp.getString(str, str2);
        }

        public void putString(String str, String str2) {
            this.mSp.edit().putString(str, str2).apply();
        }
    }
}
