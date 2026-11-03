package com.android.internal.telephony;

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.timezone.MobileCountries;
import android.timezone.TelephonyNetworkFinder;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class MccTable {
    public static final Map<Locale, Locale> FALLBACKS;
    static ArrayList sTable;
    private static TelephonyNetworkFinder sTelephonyNetworkFinder;

    public static List<MccEntry> getAllMccEntries() {
        return new ArrayList(sTable);
    }

    public static class MccEntry implements Comparable<MccEntry> {
        public final int mMcc;
        final int mSmallestDigitsMnc;

        MccEntry(int i, int i2) {
            this.mMcc = i;
            this.mSmallestDigitsMnc = i2;
        }

        @Override // java.lang.Comparable
        public int compareTo(MccEntry mccEntry) {
            return this.mMcc - mccEntry.mMcc;
        }
    }

    public static class MccMnc {
        public final String mcc;
        public final String mnc;

        public static MccMnc fromOperatorNumeric(String str) {
            Objects.requireNonNull(str);
            String strSubstring = null;
            try {
                String strSubstring2 = str.substring(0, 3);
                try {
                    strSubstring = str.substring(3);
                } catch (StringIndexOutOfBoundsException unused) {
                }
                return new MccMnc(strSubstring2, strSubstring);
            } catch (StringIndexOutOfBoundsException unused2) {
                return null;
            }
        }

        public MccMnc(String str, String str2) {
            Objects.requireNonNull(str);
            this.mcc = str;
            this.mnc = str2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                MccMnc mccMnc = (MccMnc) obj;
                if (this.mcc.equals(mccMnc.mcc) && Objects.equals(this.mnc, mccMnc.mnc)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.mcc, this.mnc);
        }

        public String toString() {
            return "MccMnc{mcc='" + this.mcc + "', mnc='" + this.mnc + "'}";
        }
    }

    public static MccEntry entryForMcc(int i) {
        int iBinarySearch = Collections.binarySearch(sTable, new MccEntry(i, 0));
        if (iBinarySearch < 0) {
            return null;
        }
        return (MccEntry) sTable.get(iBinarySearch);
    }

    public static String countryCodeForMcc(String str) {
        TelephonyNetworkFinder telephonyNetworkFinder;
        MobileCountries mobileCountriesFindCountriesByMcc;
        if (str == null) {
            return "";
        }
        synchronized (MccTable.class) {
            try {
                telephonyNetworkFinder = sTelephonyNetworkFinder;
                if (telephonyNetworkFinder == null) {
                    telephonyNetworkFinder = TelephonyNetworkFinder.getInstance();
                    sTelephonyNetworkFinder = telephonyNetworkFinder;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (telephonyNetworkFinder == null || (mobileCountriesFindCountriesByMcc = telephonyNetworkFinder.findCountriesByMcc(str)) == null) {
            return "";
        }
        return mobileCountriesFindCountriesByMcc.getDefaultCountryIsoCode();
    }

    public static String geoCountryCodeForMccMnc(MccMnc mccMnc) {
        String strCountryCodeForMccMncNoFallback = mccMnc.mnc != null ? countryCodeForMccMncNoFallback(mccMnc) : null;
        return TextUtils.isEmpty(strCountryCodeForMccMncNoFallback) ? countryCodeForMcc(mccMnc.mcc) : strCountryCodeForMccMncNoFallback;
    }

    private static String countryCodeForMccMncNoFallback(MccMnc mccMnc) {
        MobileCountries mobileCountriesFindCountriesByMccMnc;
        synchronized (MccTable.class) {
            try {
                if (sTelephonyNetworkFinder == null) {
                    sTelephonyNetworkFinder = TelephonyNetworkFinder.getInstance();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        TelephonyNetworkFinder telephonyNetworkFinder = sTelephonyNetworkFinder;
        if (telephonyNetworkFinder == null || (mobileCountriesFindCountriesByMccMnc = telephonyNetworkFinder.findCountriesByMccMnc(mccMnc.mcc, mccMnc.mnc)) == null) {
            return null;
        }
        return mobileCountriesFindCountriesByMccMnc.getDefaultCountryIsoCode();
    }

    public static int smallestDigitsMccForMnc(int i) {
        MccEntry mccEntryEntryForMcc = entryForMcc(i);
        if (mccEntryEntryForMcc == null) {
            return 2;
        }
        return mccEntryEntryForMcc.mSmallestDigitsMnc;
    }

    public static void updateMccMncConfiguration(Context context, String str) {
        Rlog.d("MccTable", "updateMccMncConfiguration mccmnc='" + str);
        if (TelephonyUtils.IS_DEBUGGABLE) {
            String str2 = SystemProperties.get("persist.sys.override_mcc");
            if (!TextUtils.isEmpty(str2)) {
                Rlog.d("MccTable", "updateMccMncConfiguration overriding mccmnc='" + str2 + "'");
                str = str2;
            }
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            if (Integer.parseInt(str.substring(0, 3)) != 0) {
                if (!((ActivityManager) context.getSystemService("activity")).updateMccMncConfiguration(str.substring(0, 3), str.substring(3))) {
                    Rlog.d("MccTable", "updateMccMncConfiguration: update mccmnc=" + str + " failure");
                    return;
                }
                Rlog.d("MccTable", "updateMccMncConfiguration: update mccmnc=" + str + " success");
                return;
            }
            Rlog.d("MccTable", "updateMccMncConfiguration nothing to update");
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            Rlog.e("MccTable", "Error parsing mccmnc: " + str + ". ex=" + e);
        }
    }

    static {
        HashMap map = new HashMap();
        FALLBACKS = map;
        map.put(Locale.ENGLISH, Locale.US);
        ArrayList arrayList = new ArrayList(20);
        sTable = arrayList;
        arrayList.add(new MccEntry(302, 3));
        sTable.add(new MccEntry(310, 3));
        sTable.add(new MccEntry(311, 3));
        sTable.add(new MccEntry(312, 3));
        sTable.add(new MccEntry(313, 3));
        sTable.add(new MccEntry(314, 3));
        sTable.add(new MccEntry(315, 3));
        sTable.add(new MccEntry(316, 3));
        sTable.add(new MccEntry(334, 3));
        sTable.add(new MccEntry(338, 3));
        sTable.add(new MccEntry(342, 3));
        sTable.add(new MccEntry(344, 3));
        sTable.add(new MccEntry(346, 3));
        sTable.add(new MccEntry(348, 3));
        sTable.add(new MccEntry(365, 3));
        sTable.add(new MccEntry(708, 3));
        sTable.add(new MccEntry(722, 3));
        sTable.add(new MccEntry(732, 3));
        Collections.sort(sTable);
    }
}
