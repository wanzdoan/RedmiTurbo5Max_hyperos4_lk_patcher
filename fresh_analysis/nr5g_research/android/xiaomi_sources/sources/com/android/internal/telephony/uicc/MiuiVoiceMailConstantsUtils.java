package com.android.internal.telephony.uicc;

import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiVoiceMailConstantsUtils {
    public static boolean isMiuiCustVoicemailConf() {
        return TelephonyManager.isCustForEsVodafone();
    }
}
