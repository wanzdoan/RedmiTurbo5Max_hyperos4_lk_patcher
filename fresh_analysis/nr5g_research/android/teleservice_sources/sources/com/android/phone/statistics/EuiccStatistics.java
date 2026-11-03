package com.android.phone.statistics;

import android.os.Bundle;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class EuiccStatistics {

    abstract class Holder {
        static final EuiccStatistics INSTANCE = new EuiccStatistics();
    }

    public static EuiccStatistics getInstance() {
        return Holder.INSTANCE;
    }

    private void recordIntResult(Bundle bundle, String str, String str2) {
        if (bundle == null || !bundle.containsKey(str)) {
            return;
        }
        Object obj = bundle.get(str);
        if (obj instanceof Integer) {
            HashMap map = new HashMap();
            map.put(str, String.valueOf(obj));
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, str2, map);
        }
    }

    public void recordAuthResult(Bundle bundle) {
        recordIntResult(bundle, "auth_result", "key_euicc_auth_result");
    }

    public void recordBindingResult(Bundle bundle) {
        recordIntResult(bundle, "binding_result", "key_euicc_binding_result");
    }
}
