package com.android.internal.telephony;

import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;

/* JADX INFO: loaded from: classes.dex */
public class QtiSimUtils {
    public static void updatePersoTempUnlockStatusIfRequired(IccCardStatus iccCardStatus) {
        if (!SystemProperties.getBoolean("persist.vendor.radio.temp_unlock_feature", false) || iccCardStatus == null) {
            return;
        }
        Log.d("QtiSimUtils", "updatePersoTempUnlockStatusIfRequired");
        int length = iccCardStatus.mApplications.length;
        for (int i = 0; i < length; i++) {
            IccCardApplicationStatus iccCardApplicationStatus = iccCardStatus.mApplications[i];
            if (iccCardApplicationStatus == null) {
                return;
            }
            if (iccCardApplicationStatus.app_type != IccCardApplicationStatus.AppType.APPTYPE_ISIM && iccCardApplicationStatus.app_state == IccCardApplicationStatus.AppState.APPSTATE_READY) {
                IccCardApplicationStatus.PersoSubState persoSubState = iccCardApplicationStatus.perso_substate;
                if (persoSubState == IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_TEMPORARY_UNLOCKED) {
                    iccCardApplicationStatus.app_state = IccCardApplicationStatus.AppState.APPSTATE_SUBSCRIPTION_PERSO;
                } else if (persoSubState == IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_PERMANENT_UNLOCKED) {
                    iccCardApplicationStatus.perso_substate = IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_READY;
                }
            }
        }
        Log.d("QtiSimUtils", "IccCardStatus: " + iccCardStatus);
    }
}
