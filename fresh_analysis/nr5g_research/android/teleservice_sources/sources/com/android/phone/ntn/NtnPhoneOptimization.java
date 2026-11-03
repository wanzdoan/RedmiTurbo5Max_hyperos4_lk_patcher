package com.android.phone.ntn;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.telephony.Rlog;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhoneOptimization {
    private List mCityInfoList = new ArrayList();
    private Context mContext;
    private double mMaxDistance;
    private CityInfo mUsedCityInfo;
    private String mVersionNumber;
    public static final int[] DEFAULT_BEAM = {-1};
    public static final int[] DEFAULT_FREQUENCY = {0};
    private static final double[] DATA_LOCATION = {116.18037d, 40.02256d};

    public NtnPhoneOptimization(Context context) throws Throwable {
        this.mContext = context;
        loadJsonFile(context);
    }

    private void loadJsonFile(Context context) throws Throwable {
        try {
            String jsonFromAssets = getJsonFromAssets(context, "NtnMap.json", "UTF-8");
            if (TextUtils.isEmpty(jsonFromAssets)) {
                log("loadJsonFile: json is null");
                return;
            }
            JSONObject jSONObject = new JSONObject(jsonFromAssets).getJSONObject("info");
            this.mVersionNumber = jSONObject.optString("ver");
            this.mMaxDistance = jSONObject.optInt("max");
            JSONArray jSONArray = jSONObject.getJSONArray("cities");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                JSONArray jSONArrayOptJSONArray = jSONObject2.optJSONArray("loc");
                double[] dArr = new double[jSONArrayOptJSONArray.length()];
                for (int i2 = 0; i2 < jSONArrayOptJSONArray.length(); i2++) {
                    dArr[i2] = jSONArrayOptJSONArray.optDouble(i2);
                }
                JSONArray jSONArrayOptJSONArray2 = jSONObject2.optJSONArray("id");
                int[] iArr = new int[jSONArrayOptJSONArray2.length()];
                for (int i3 = 0; i3 < jSONArrayOptJSONArray2.length(); i3++) {
                    iArr[i3] = jSONArrayOptJSONArray2.optInt(i3);
                }
                JSONArray jSONArrayOptJSONArray3 = jSONObject2.optJSONArray("freq");
                int[] iArr2 = new int[jSONArrayOptJSONArray3.length()];
                for (int i4 = 0; i4 < jSONArrayOptJSONArray3.length(); i4++) {
                    iArr2[i4] = jSONArrayOptJSONArray3.optInt(i4);
                }
                this.mCityInfoList.add(new CityInfo(i, dArr[0], dArr[1], iArr, iArr2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Code duplicated, block: B:31:0x0065 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private static String getJsonFromAssets(Context context, String str, String str2) throws Throwable {
        InputStream inputStreamOpen;
        InputStream inputStream = null;
        try {
            inputStreamOpen = context.getAssets().open(str);
            try {
                try {
                    byte[] bArr = new byte[inputStreamOpen.available()];
                    inputStreamOpen.read(bArr);
                    inputStreamOpen.close();
                    String str3 = new String(bArr, str2);
                    try {
                        inputStreamOpen.close();
                    } catch (IOException e) {
                        log("getJsonFromAssets ipStream: Exception = " + e);
                    }
                    return str3;
                } catch (IOException e2) {
                    e = e2;
                    log("getJsonFromAssets: Exception = " + e);
                    if (inputStreamOpen == null) {
                        return null;
                    }
                    try {
                        inputStreamOpen.close();
                        return null;
                    } catch (IOException e3) {
                        log("getJsonFromAssets ipStream: Exception = " + e3);
                        return null;
                    }
                }
            } catch (Throwable th) {
                th = th;
                inputStream = inputStreamOpen;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                        log("getJsonFromAssets ipStream: Exception = " + e4);
                    }
                }
                throw th;
            }
        } catch (IOException e5) {
            e = e5;
            inputStreamOpen = null;
        } catch (Throwable th2) {
            th = th2;
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    public List getCityInfoList() {
        return this.mCityInfoList;
    }

    public double getMaxDistance() {
        return this.mMaxDistance;
    }

    public String getDataVersion() {
        return this.mVersionNumber;
    }

    public CityInfo getUsedCityInfo() {
        return this.mUsedCityInfo;
    }

    public class CityInfo {
        private int[] beamArr;
        private int[] freqArr;
        private int index;
        private double lat;
        private double lon;

        public CityInfo(int i, double d, double d2, int[] iArr, int[] iArr2) {
            this.index = i;
            this.lon = d;
            this.lat = d2;
            this.beamArr = iArr;
            this.freqArr = iArr2;
        }

        public int getIndex() {
            return this.index;
        }

        public double getLon() {
            return this.lon;
        }

        public double getLat() {
            return this.lat;
        }

        public int[] getBeamArray() {
            return this.beamArr;
        }

        public int[] getFreqArray() {
            return this.freqArr;
        }

        public String toString() {
            return "CityInfo{lon=" + this.lon + ", lat=" + this.lat + ", beamArr=" + Arrays.toString(this.beamArr) + ", freqArr=" + Arrays.toString(this.freqArr) + '}';
        }
    }

    public static double[] calculateNewLongitude(double d, double d2, double d3, double d4) {
        double radians = Math.toRadians(d2);
        double radians2 = Math.toRadians(d);
        double radians3 = Math.toRadians(d4);
        double d5 = d3 / 6378137.0d;
        double dAsin = Math.asin((Math.sin(radians) * Math.cos(d5)) + (Math.cos(radians) * Math.sin(d5) * Math.cos(radians3)));
        double[] dArr = {Math.toDegrees(radians2 + Math.atan2(Math.sin(radians3) * Math.sin(d5) * Math.cos(radians), Math.cos(d5) - (Math.sin(radians) * Math.sin(dAsin)))), Math.toDegrees(dAsin)};
        dArr[0] = ((dArr[0] + 180.0d) % 360.0d) - 180.0d;
        return dArr;
    }

    public void saveCityCoverInfo(Location location, int i, int i2) {
        log("saveCityCoverInfo.");
        if (location == null) {
            log("saveCityCoverInfo location is null.");
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editorEdit.putString("satellite.city.cover.save.lon", String.valueOf(location.getLongitude()));
        editorEdit.putString("satellite.city.cover.save.lat", String.valueOf(location.getLatitude()));
        editorEdit.putInt("satellite.city.cover.save.beam", i);
        editorEdit.putInt("satellite.city.cover.save.frequency", i2);
        editorEdit.apply();
    }

    public void removeSaveCityCoverInfo() {
        log("removeSaveCityCoverInfo.");
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editorEdit.remove("satellite.city.cover.save.lon");
        editorEdit.remove("satellite.city.cover.save.lat");
        editorEdit.remove("satellite.city.cover.save.beam");
        editorEdit.remove("satellite.city.cover.save.frequency");
        editorEdit.apply();
    }

    public int[] getBeamList(Location location) {
        CityInfo closestCity = getClosestCity(location);
        if (closestCity != null) {
            log("setSatBeam freq = " + Arrays.toString(closestCity.getBeamArray()));
            return closestCity.getBeamArray();
        }
        return DEFAULT_BEAM;
    }

    public int[] getFreqList(Location location) {
        CityInfo closestCity = getClosestCity(location);
        if (closestCity != null) {
            log("setPrioriFrequency freq = " + Arrays.toString(closestCity.getFreqArray()));
            return closestCity.getFreqArray();
        }
        return DEFAULT_FREQUENCY;
    }

    private CityInfo getClosestCity(Location location) {
        if (!checkSatelliteOptimizationEnable()) {
            return null;
        }
        if (location == null) {
            log("currentLocation is null.");
            return null;
        }
        log("loadCityInfo start.");
        return findClosestCity(location);
    }

    private boolean checkSatelliteOptimizationEnable() {
        return MiuiSettings.Telephony.isSatelliteNetworkOptimizationEnable(this.mContext.getContentResolver());
    }

    private CityInfo findClosestCity(Location location) {
        CityInfo cityInfoLoadSavedCityInfo = loadSavedCityInfo(location);
        if (cityInfoLoadSavedCityInfo != null) {
            log("used save city info.");
            return cityInfoLoadSavedCityInfo;
        }
        List cityInfoList = getCityInfoList();
        if (cityInfoList == null) {
            log("cityinfoList is null!");
            return null;
        }
        return findClosestCityInFilteredCities(location, filterCitiesByBoundaries(location, cityInfoList));
    }

    private CityInfo loadSavedCityInfo(Location location) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        Location location2 = new Location("");
        location2.setLongitude(Double.parseDouble(defaultSharedPreferences.getString("satellite.city.cover.save.lon", Double.toString(0.0d))));
        location2.setLatitude(Double.parseDouble(defaultSharedPreferences.getString("satellite.city.cover.save.lat", Double.toString(0.0d))));
        if (location.distanceTo(location2) >= 3000.0d) {
            return null;
        }
        return new CityInfo(1000, 0.0d, 0.0d, new int[]{defaultSharedPreferences.getInt("satellite.city.cover.save.beam", DEFAULT_BEAM[0])}, new int[]{defaultSharedPreferences.getInt("satellite.city.cover.save.frequency", DEFAULT_FREQUENCY[0])});
    }

    private List filterCitiesByBoundaries(Location location, List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        double maxDistance = getMaxDistance();
        double[] dArrCalculateNewLongitude = calculateNewLongitude(longitude, latitude, maxDistance, 0.0d);
        double[] dArrCalculateNewLongitude2 = calculateNewLongitude(longitude, latitude, maxDistance, 90.0d);
        double[] dArrCalculateNewLongitude3 = calculateNewLongitude(longitude, latitude, maxDistance, 270.0d);
        double[] dArrCalculateNewLongitude4 = calculateNewLongitude(longitude, latitude, maxDistance, 180.0d);
        while (it.hasNext()) {
            CityInfo cityInfo = (CityInfo) it.next();
            double lon = cityInfo.getLon();
            double lat = cityInfo.getLat();
            if (lon >= dArrCalculateNewLongitude3[0] && lon <= dArrCalculateNewLongitude2[0] && lat >= dArrCalculateNewLongitude4[1] && lat <= dArrCalculateNewLongitude[1]) {
                arrayList.add(cityInfo);
            }
        }
        return arrayList;
    }

    private CityInfo findClosestCityInFilteredCities(Location location, List list) {
        double maxDistance = getMaxDistance();
        Iterator it = list.iterator();
        CityInfo cityInfo = null;
        while (it.hasNext()) {
            CityInfo cityInfo2 = (CityInfo) it.next();
            Location location2 = new Location("");
            location2.setLongitude(cityInfo2.getLon());
            location2.setLatitude(cityInfo2.getLat());
            double dDistanceTo = location.distanceTo(location2);
            if (dDistanceTo < 1.0E-6d + maxDistance) {
                cityInfo = cityInfo2;
                maxDistance = dDistanceTo;
            }
        }
        return cityInfo;
    }

    public boolean isNeedOpenSatelliteData(Location location) {
        if (location == null) {
            log("isNeedOpenSatelliteData currentLocation is null.");
            return false;
        }
        Location location2 = new Location("");
        double[] dArr = DATA_LOCATION;
        location2.setLongitude(dArr[0]);
        location2.setLatitude(dArr[1]);
        if (location2.distanceTo(location) <= 250000.0d) {
            log("isNeedOpenSatelliteData currentLocation is In range.");
            return true;
        }
        log("isNeedOpenSatelliteData currentLocation is Out of range.");
        return false;
    }

    protected static void log(String str) {
        Rlog.d("NtnPhoneOptimization", str);
    }
}
