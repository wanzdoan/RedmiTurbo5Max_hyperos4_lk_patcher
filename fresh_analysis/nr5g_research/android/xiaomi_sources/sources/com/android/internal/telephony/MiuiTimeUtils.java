package com.android.internal.telephony;

import android.text.TextUtils;
import android.util.TimeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public class MiuiTimeUtils {
    private static final boolean DBG = false;
    private static final String TAG = "MiuiTimeUtils";

    public static ArrayList<TimeZone> getTimeZones(String country) {
        List<String> timeZoneIds;
        ArrayList<TimeZone> jdkTimeZones = new ArrayList<>();
        if (!TextUtils.isEmpty(country) && (timeZoneIds = TimeUtils.getTimeZoneIdsForCountryCode(country)) != null && timeZoneIds.size() > 0) {
            for (String zoneId : timeZoneIds) {
                jdkTimeZones.add(TimeZone.getTimeZone(zoneId));
            }
        }
        return jdkTimeZones;
    }
}
