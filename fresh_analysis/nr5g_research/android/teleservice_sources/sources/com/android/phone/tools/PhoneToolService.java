package com.android.phone.tools;

import com.aios.apptoolsdk.AnnotatedToolService;
import com.aios.apptoolsdk.AppResult;

/* JADX INFO: loaded from: classes.dex */
public class PhoneToolService extends AnnotatedToolService {
    public AppResult callRecording(String str) {
        return CallRecordingTools.recordCall(getApplicationContext(), str);
    }

    public AppResult getCallerInfo() {
        return CallNumberInfoTools.getCallerInfo(getApplicationContext());
    }

    public AppResult phoneSettings(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        Integer numValueOf;
        if (str7 == null || str7.trim().isEmpty()) {
            numValueOf = null;
        } else {
            try {
                numValueOf = Integer.valueOf(Integer.parseInt(str7.trim()));
            } catch (NumberFormatException unused) {
                numValueOf = null;
            }
        }
        return CallSettingsTools.phoneSettings(getApplicationContext(), str, str2, str3, str4, str5, str6, numValueOf);
    }

    public AppResult networkSettings(String str, String str2, String str3) {
        Integer numValueOf;
        if (str3 == null || str3.trim().isEmpty()) {
            numValueOf = null;
        } else {
            try {
                numValueOf = Integer.valueOf(Integer.parseInt(str3.trim()));
            } catch (NumberFormatException unused) {
                numValueOf = null;
            }
        }
        return NetworkSettingsTools.networkSettings(getApplicationContext(), str, str2, numValueOf);
    }
}
