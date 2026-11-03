package com.android.phone.statistics;

import android.os.Bundle;
import android.util.Log;
import com.android.phone.ims.MiuiImsStatistics;

/* JADX INFO: loaded from: classes.dex */
public class ExternalProjectStatistic {
    public static void recordTelephonyEvent(Bundle bundle) {
        String string = bundle.getString("EVENT");
        if (string == null) {
            return;
        }
        Log.i("ExternalProjectStatistic", "recordTelephonyEvent Event:".concat(string));
        string.hashCode();
        switch (string) {
            case "key_euicc_binding_result":
                EuiccStatistics.getInstance().recordBindingResult(bundle);
                break;
            case "vowifi_enable":
                MiuiImsStatistics.recordOpenVoWiFiEvent(bundle);
                break;
            case "key_euicc_auth_result":
                EuiccStatistics.getInstance().recordAuthResult(bundle);
                break;
            case "data_channel_enable":
                MiDataChannelStatistic.getInstance().recordDataChannelEnable(bundle);
                break;
        }
    }
}
