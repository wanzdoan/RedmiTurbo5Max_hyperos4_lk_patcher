package com.android.phone.vvm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telecom.PhoneAccountHandle;

/* JADX INFO: loaded from: classes.dex */
public class VisualVoicemailPreferences {
    private final PhoneAccountHandle mPhoneAccountHandle;
    private final SharedPreferences mPreferences;

    public VisualVoicemailPreferences(Context context, PhoneAccountHandle phoneAccountHandle) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.mPhoneAccountHandle = phoneAccountHandle;
    }

    public boolean getBoolean(String str, boolean z) {
        return ((Boolean) getValue(str, Boolean.valueOf(z))).booleanValue();
    }

    public String getString(String str) {
        return (String) getValue(str, null);
    }

    public boolean contains(String str) {
        return this.mPreferences.contains(getKey(str));
    }

    private Object getValue(String str, Object obj) {
        Object obj2;
        return (contains(str) && (obj2 = this.mPreferences.getAll().get(getKey(str))) != null) ? obj2 : obj;
    }

    private String getKey(String str) {
        return "visual_voicemail_" + str + "_" + this.mPhoneAccountHandle.getId();
    }
}
