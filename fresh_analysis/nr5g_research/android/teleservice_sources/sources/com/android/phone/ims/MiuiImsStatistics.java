package com.android.phone.ims;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.google.gson.Gson;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiImsStatistics {
    private static long[] mLastVoWiFiRegisteredTime;
    private static String[] mPlmn;
    private static HashMap mVoDataRegisteredDuration;
    private static HashMap mVoWiFiRegisteredDuration;
    private static boolean mWiFiConnected;
    public static final Context sContext = PhoneGlobals.getInstance();

    static {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        mPlmn = new String[i];
        mLastVoWiFiRegisteredTime = new long[i];
        mVoWiFiRegisteredDuration = new HashMap();
        mVoDataRegisteredDuration = new HashMap();
        mWiFiConnected = false;
    }

    public static void recordOpenVoWiFiEvent(int i) {
        String simOperatorNumericForPhone = TelephonyManager.from(sContext).getSimOperatorNumericForPhone(i);
        if (MiuiPhoneUtils.isCnOperator(simOperatorNumericForPhone)) {
            Log.i("MiuiImsStatistics", "recordOpenVoWiFiEvent phoneId:" + i);
            HashMap map = new HashMap();
            map.put("SimOperator", simOperatorNumericForPhone);
            MiStatInterfaceUtil.recordObjectEvent("vowifi", "vowifi_enable", map);
        }
    }

    public static void recordOpenVoWiFiEvent(Bundle bundle) {
        int i;
        if (bundle == null || (i = bundle.getInt("phoneId", -1)) == -1) {
            return;
        }
        recordOpenVoWiFiEvent(i);
    }

    public static void init() {
        Log.i("MiuiImsStatistics", "init");
        loadDataFromSP("vowifi_registered_duration");
        loadDataFromSP("vodata_registered_duration");
    }

    private static void loadDataFromSP(String str) {
        String string = PreferenceManager.getDefaultSharedPreferences(sContext).getString(str, null);
        if (string != null && !string.isEmpty()) {
            try {
                HashMap map = (HashMap) ObjectSerializer.deserialize(string);
                int iHashCode = str.hashCode();
                if (iHashCode != -1974589888) {
                    if (iHashCode == 2066500181 && str.equals("vodata_registered_duration")) {
                        mVoDataRegisteredDuration = map;
                    }
                } else if (str.equals("vowifi_registered_duration")) {
                    mVoWiFiRegisteredDuration = map;
                }
                Log.d("MiuiImsStatistics", "loadDataFromSP " + str + ":" + map);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Log.d("MiuiImsStatistics", "init " + str + " duration is null or empty");
    }

    public static void processVoWiFiRegisteredStateChange(int i, boolean z) {
        Log.i("MiuiImsStatistics", "processVoWiFiRegisteredStateChange state:" + z + " phoneId:" + i);
        String simOperatorNumericForPhone = TelephonyManager.from(sContext).getSimOperatorNumericForPhone(i);
        if (z) {
            mPlmn[i] = simOperatorNumericForPhone;
            mLastVoWiFiRegisteredTime[i] = SystemClock.elapsedRealtime();
        } else if (mLastVoWiFiRegisteredTime[i] != 0) {
            calculateDurationByPhoneId(i);
            saveDurationToSP("vowifi_registered_duration", mVoWiFiRegisteredDuration);
            saveDurationToSP("vodata_registered_duration", mVoDataRegisteredDuration);
            mPlmn[i] = null;
            mLastVoWiFiRegisteredTime[i] = 0;
        }
    }

    private static void saveDurationToSP(String str, HashMap map) {
        try {
            String strSerialize = ObjectSerializer.serialize(map);
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(sContext).edit();
            editorEdit.putString(str, strSerialize);
            editorEdit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clear() {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(sContext).edit();
        editorEdit.remove("vowifi_registered_duration");
        editorEdit.remove("vodata_registered_duration");
        editorEdit.apply();
        mVoWiFiRegisteredDuration.clear();
        mVoDataRegisteredDuration.clear();
    }

    private static void calculateDurationByPhoneId(int i) {
        long jElapsedRealtime = (SystemClock.elapsedRealtime() - mLastVoWiFiRegisteredTime[i]) / 1000;
        Log.i("MiuiImsStatistics", "calculateDurationByPhoneId duration:" + Long.valueOf(jElapsedRealtime));
        HashMap map = mVoWiFiRegisteredDuration;
        String str = mPlmn[i];
        map.put(str, Long.valueOf(((Long) map.getOrDefault(str, 0L)).longValue() + jElapsedRealtime));
        if (mWiFiConnected) {
            return;
        }
        HashMap map2 = mVoDataRegisteredDuration;
        String str2 = mPlmn[i];
        map2.put(str2, Long.valueOf(jElapsedRealtime + ((Long) map2.getOrDefault(str2, 0L)).longValue()));
    }

    public static void recordVoWiFiRedisteredDuration() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (mLastVoWiFiRegisteredTime[i] != 0) {
                calculateDurationByPhoneId(i);
                mLastVoWiFiRegisteredTime[i] = SystemClock.elapsedRealtime();
            }
        }
        if (mVoWiFiRegisteredDuration.isEmpty()) {
            return;
        }
        HashMap map = new HashMap();
        Log.i("MiuiImsStatistics", "recordVoWiFiRegisteredDuration");
        map.put("VoWiFiRegistered", new Gson().toJson(mVoWiFiRegisteredDuration).toString());
        if (!mVoDataRegisteredDuration.isEmpty()) {
            map.put("VoDataRegistered", new Gson().toJson(mVoDataRegisteredDuration).toString());
        }
        MiStatInterfaceUtil.recordObjectEvent("vowifi", "vowifi_registered_duration", map);
        clear();
    }

    public static void processNetworkInfoChanged(int i, NetworkInfo.State state) {
        if (i == 1) {
            Log.i("MiuiImsStatistics", "processNetworkInfoChanged WiFi connect changed newState = " + state);
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                if (mLastVoWiFiRegisteredTime[i2] != 0) {
                    calculateDurationByPhoneId(i2);
                    mLastVoWiFiRegisteredTime[i2] = SystemClock.elapsedRealtime();
                }
            }
            mWiFiConnected = state == NetworkInfo.State.CONNECTED;
        }
    }
}
