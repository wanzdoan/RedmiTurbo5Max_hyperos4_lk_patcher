package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class CarrierConfigRepairManager {
    private static volatile CarrierConfigRepairManager sInstance;
    private Context mContext;

    private CarrierConfigRepairManager(Context context) {
        this.mContext = context;
    }

    public static void init(Context context) {
        if (sInstance == null) {
            synchronized (CarrierConfigRepairManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new CarrierConfigRepairManager(context);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public static CarrierConfigRepairManager getInstance() {
        return sInstance;
    }

    public void reset(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editorEdit.remove("error_statistic" + phone.getSubId());
        editorEdit.apply();
    }

    public void tryRepair(PersistableBundle persistableBundle, String str) {
        if (isMonitorSupport(str)) {
            PhoneProxy.setCarrierConfigValue(persistableBundle, "carrier_volte_available_bool", true);
            PhoneProxy.setCarrierConfigValue(persistableBundle, "hide_enhanced_4g_lte_bool", true);
        }
    }

    public void reportConfigErrorStatistics(int i, boolean z, PersistableBundle persistableBundle) {
        Phone phone = PhoneFactory.getPhone(i);
        String simOperatorNumericForPhone = TelephonyManager.from(this.mContext).getSimOperatorNumericForPhone(i);
        if (phone == null || !isMonitorSupport(simOperatorNumericForPhone)) {
            return;
        }
        int subId = phone.getSubId();
        MiuiTelephonyStatistic miuiTelephonyStatisticMake = MiuiTelephonyStatistic.make();
        if (subId == -1 || miuiTelephonyStatisticMake == null) {
            return;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        Log.i("CarrierConfigLoader", "configErrorStatistics");
        if (persistableBundle.getBoolean("carrier_volte_available_bool")) {
            return;
        }
        if (defaultSharedPreferences.getBoolean("error_statistic" + subId, false)) {
            return;
        }
        recordConfigLoadedErrorStatistic(z ? "0" : "1", simOperatorNumericForPhone);
        editorEdit.putBoolean("error_statistic" + subId, true);
        editorEdit.apply();
    }

    public boolean isLoadedError(int i) {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("error_statistic" + i, false);
    }

    public void recordConfigLoadedErrorStatistic(String str, String str2) {
        HashMap map = new HashMap();
        Log.i("CarrierConfigLoader", "recordConfigLoadedErrorStatistic: " + str);
        map.put("error_reason", str);
        map.put("operactor", str2);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "carrier_config_error", map);
    }

    public boolean isMonitorSupport(String str) {
        return str != null && str.startsWith("460");
    }
}
