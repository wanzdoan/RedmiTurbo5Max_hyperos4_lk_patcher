package com.android.phone.performance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.android.phone.PhoneGlobals;

/* JADX INFO: loaded from: classes.dex */
public class PreferenceUtils {
    private static final Bundle PREFERENCE_VALUES = new Bundle();
    private static volatile PreferenceUtils sInstance;
    private final int MODE = 0;
    private final SharedPreferences mSp;

    public static PreferenceUtils getInstance() {
        if (sInstance == null) {
            synchronized (PreferenceUtils.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new PreferenceUtils(PhoneGlobals.getInstance(), "telephony_performance");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private PreferenceUtils(Context context, String str) {
        this.mSp = context.getSharedPreferences(str, 0);
    }

    public void setStringPreference(String str, String str2) {
        PREFERENCE_VALUES.putString(str, str2);
        this.mSp.edit().putString(str, str2).commit();
    }

    public void setBooleanPreference(String str, boolean z) {
        PREFERENCE_VALUES.putBoolean(str, z);
        this.mSp.edit().putBoolean(str, z).commit();
    }

    public void setIntPreference(String str, int i) {
        PREFERENCE_VALUES.putInt(str, i);
        this.mSp.edit().putInt(str, i).commit();
    }

    public void setLongPref(String str, long j) {
        PREFERENCE_VALUES.putLong(str, j);
        this.mSp.edit().putLong(str, j).commit();
    }

    public String getStringPreference(String str) {
        Bundle bundle = PREFERENCE_VALUES;
        if (bundle.containsKey(str)) {
            return bundle.getString(str);
        }
        return this.mSp.getString(str, "");
    }

    public int getIntPreference(String str) {
        Bundle bundle = PREFERENCE_VALUES;
        if (bundle.containsKey(str)) {
            return bundle.getInt(str);
        }
        return this.mSp.getInt(str, 0);
    }

    public int getIntPreference(String str, int i) {
        Bundle bundle = PREFERENCE_VALUES;
        if (bundle.containsKey(str)) {
            return bundle.getInt(str);
        }
        return this.mSp.getInt(str, i);
    }

    public Boolean getBooleanPreference(String str, Boolean bool) {
        Bundle bundle = PREFERENCE_VALUES;
        if (bundle.containsKey(str)) {
            return Boolean.valueOf(bundle.getBoolean(str));
        }
        return Boolean.valueOf(this.mSp.getBoolean(str, bool.booleanValue()));
    }

    public Boolean getBooleanPreference(String str) {
        Bundle bundle = PREFERENCE_VALUES;
        if (bundle.containsKey(str)) {
            return Boolean.valueOf(bundle.getBoolean(str));
        }
        return Boolean.valueOf(this.mSp.getBoolean(str, false));
    }

    public long getLongPref(String str) {
        Bundle bundle = PREFERENCE_VALUES;
        if (bundle.containsKey(str)) {
            return bundle.getLong(str);
        }
        return this.mSp.getLong(str, 0L);
    }

    public void setLastMobileDataAlwaysOn(int i) {
        setIntPreference("last_mobile_data_always_on", i);
    }

    public int getLastMobileDataAlwaysOn() {
        return getIntPreference("last_mobile_data_always_on");
    }
}
