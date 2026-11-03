package com.android.phone.satellite.accesscontrol;

import android.net.ParseException;
import android.os.Build;
import android.telephony.satellite.EarfcnRange;
import android.telephony.satellite.SatelliteAccessConfiguration;
import android.telephony.satellite.SatelliteInfo;
import android.telephony.satellite.SatellitePosition;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import miuix.reflect.Reflects$$ExternalSyntheticBUOutline0;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class SatelliteAccessConfigurationParser {
    private static boolean isValidAltitude(double d) {
        return d >= 0.0d;
    }

    private static boolean isValidLongitude(double d) {
        return d >= -180.0d && d <= 180.0d;
    }

    public static Map parse(String str, FeatureFlags featureFlags) {
        logd("SatelliteAccessConfigurationParser: parse: " + str);
        try {
            JSONArray jSONArrayOptJSONArray = new JSONObject(readJsonStringFromFile(str)).optJSONArray("access_control_configs");
            if (jSONArrayOptJSONArray == null) {
                loge("parse : failed to parse satellite access configurations json");
                return null;
            }
            Map satelliteAccessConfigurations = parseSatelliteAccessConfigurations(jSONArrayOptJSONArray, featureFlags);
            logd("satelliteAccessConfigurationMap= " + satelliteAccessConfigurations);
            return satelliteAccessConfigurations;
        } catch (ParseException | JSONException e) {
            loge("Failed to parse satellite access configurations: " + e.getMessage());
            Reflects$$ExternalSyntheticBUOutline0.m(e);
            return null;
        }
    }

    private static void logd(String str) {
        if (Build.TYPE.equals("user")) {
            return;
        }
        Log.d("SatelliteAccessConfigurationParser", str);
    }

    private static void loge(String str) {
        Log.e("SatelliteAccessConfigurationParser", str);
    }

    protected static List parseSatelliteTagIdList(JSONObject jSONObject) {
        ArrayList arrayList = new ArrayList();
        try {
            List integerList = parseIntegerList(jSONObject.optJSONArray("tag_ids"));
            logd("parseSatelliteBandList: " + integerList);
            return integerList;
        } catch (JSONException unused) {
            loge("parseSatelliteTagIdList:  parsing is error");
            return arrayList;
        }
    }

    protected static List parseSatelliteCarrierIdList(JSONObject jSONObject) {
        ArrayList arrayList = new ArrayList();
        try {
            List integerList = parseIntegerList(jSONObject.optJSONArray("carrier_ids"));
            logd("parseSatelliteCarrierIdList: " + integerList);
            return integerList;
        } catch (JSONException unused) {
            loge("parseSatelliteCarrierIdList:  parsing is error");
            return arrayList;
        }
    }

    private static Map parseSatelliteAccessConfigurations(JSONArray jSONArray, FeatureFlags featureFlags) throws JSONException {
        HashMap map = new HashMap();
        if (jSONArray == null) {
            loge("parseSatelliteAccessConfigurations: jsonArray is null, return null");
            return null;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            int iOptInt = jSONObject.optInt("config_id", -1);
            if (!isRegionalConfigIdValid(Integer.valueOf(iOptInt))) {
                loge("parseAccessControlConfigs: invalid config_id, return null");
                return null;
            }
            List satelliteInfoList = parseSatelliteInfoList(jSONObject.getJSONArray("satellite_infos"));
            if (satelliteInfoList.isEmpty()) {
                logd("parseAccessControlConfigs: satelliteInfoList is empty");
            }
            List satelliteTagIdList = parseSatelliteTagIdList(jSONObject);
            if (satelliteInfoList.isEmpty() && satelliteTagIdList.isEmpty()) {
                loge("parseAccessControlConfigs: satelliteInfoList is empty and tagId is null");
                return null;
            }
            map.put(Integer.valueOf(iOptInt), new SatelliteAccessConfiguration(satelliteInfoList, satelliteTagIdList, parseSatelliteCarrierIdList(jSONObject)));
        }
        logd("parseSatelliteAccessConfigurations: " + map);
        return map;
    }

    public static boolean isRegionalConfigIdValid(Integer num) {
        return num != null && num.intValue() >= 0;
    }

    protected static UUID parseSatelliteId(JSONObject jSONObject) {
        String strOptString = jSONObject.optString("satellite_id", null);
        if (strOptString != null) {
            try {
                UUID uuidFromString = UUID.fromString(strOptString);
                logd("getSatelliteId: satellite uuid is " + uuidFromString);
                return uuidFromString;
            } catch (IllegalArgumentException e) {
                loge("getSatelliteId: invalid UUID format: " + strOptString + " | " + e.getMessage());
                return null;
            }
        }
        loge("getSatelliteId: satellite uuid is missing");
        return null;
    }

    protected static SatellitePosition parseSatellitePosition(JSONObject jSONObject) {
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("satellite_position");
        SatellitePosition satellitePosition = new SatellitePosition(Double.NaN, Double.NaN);
        if (jSONObjectOptJSONObject == null) {
            loge("parseSatellitePosition: jsonObject is null");
            return satellitePosition;
        }
        try {
            double d = jSONObjectOptJSONObject.getDouble("longitude");
            double d2 = jSONObjectOptJSONObject.getDouble("altitude");
            if (isValidLongitude(d) && isValidAltitude(d2)) {
                SatellitePosition satellitePosition2 = new SatellitePosition(d, d2);
                logd("parseSatellitePosition: " + satellitePosition2);
                return satellitePosition2;
            }
            loge("parseSatellitePosition: invalid value: " + d + " | " + d2);
            return satellitePosition;
        } catch (JSONException e) {
            loge("parseSatellitePosition: json parsing error " + e.getMessage());
            return satellitePosition;
        }
    }

    protected static List parseSatelliteEarfcnRangeList(JSONObject jSONObject) {
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("earfcn_ranges");
        ArrayList arrayList = new ArrayList();
        if (jSONArrayOptJSONArray == null) {
            loge("parseSatelliteEarfcnRangeList: earfcn_ranges is missing");
            return arrayList;
        }
        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
            try {
                EarfcnRange earfcnRange = parseEarfcnRange(jSONArrayOptJSONArray.getJSONObject(i));
                if (earfcnRange == null) {
                    loge("parseSatelliteEarfcnRangeList: earfcnRange is null, return empty list");
                    arrayList.clear();
                    return arrayList;
                }
                arrayList.add(earfcnRange);
            } catch (JSONException unused) {
                loge("parseSatelliteEarfcnRangeList: earfcnRange json parsing error");
                arrayList.clear();
                return arrayList;
            }
        }
        logd("parseSatelliteEarfcnRangeList: " + arrayList);
        return arrayList;
    }

    protected static List parseSatelliteBandList(JSONObject jSONObject) {
        ArrayList arrayList = new ArrayList();
        try {
            List integerList = parseIntegerList(jSONObject.getJSONArray("bands"));
            logd("parseSatelliteBandList: " + integerList);
            return integerList;
        } catch (JSONException unused) {
            loge("parseSatelliteInfo: bands parsing is error");
            return arrayList;
        }
    }

    protected static List parseSatelliteInfoList(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            if (jSONObject == null) {
                arrayList.clear();
                break;
            }
            UUID satelliteId = parseSatelliteId(jSONObject);
            SatellitePosition satellitePosition = parseSatellitePosition(jSONObject);
            List satelliteEarfcnRangeList = parseSatelliteEarfcnRangeList(jSONObject);
            List satelliteBandList = parseSatelliteBandList(jSONObject);
            if (satelliteId == null || (satelliteBandList.isEmpty() && satelliteEarfcnRangeList.isEmpty())) {
                loge("parseSatelliteInfo: id is " + satelliteId + " or both band list and earfcn range list are empty");
                arrayList.clear();
                return arrayList;
            }
            arrayList.add(new SatelliteInfo(satelliteId, satellitePosition, satelliteBandList, satelliteEarfcnRangeList));
        }
        logd("parseSatelliteInfoList: " + arrayList);
        return arrayList;
    }

    public static String readJsonStringFromFile(String str) {
        StringBuilder sb;
        logd("jsonFilePath is " + str);
        try {
            try {
                try {
                    FileInputStream fileInputStream = new FileInputStream(str);
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            byte[] bArr = new byte[SmartNetworkRelayScore.TRAFFIC_UNIT];
                            while (true) {
                                int i = fileInputStream.read(bArr);
                                if (i != -1) {
                                    byteArrayOutputStream.write(bArr, 0, i);
                                } else {
                                    String string = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
                                    byteArrayOutputStream.close();
                                    fileInputStream.close();
                                    logd("jsonString is " + string);
                                    return string;
                                }
                                try {
                                    fileInputStream.close();
                                } catch (Throwable th) {
                                    th.addSuppressed(th);
                                }
                                throw th;
                            }
                        } catch (Throwable th2) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                            throw th2;
                        }
                    } catch (Throwable th4) {
                        fileInputStream.close();
                        throw th4;
                    }
                } catch (FileNotFoundException e) {
                    loge("Error file " + str + " is not founded: " + e.getMessage());
                    sb = new StringBuilder("jsonString is ");
                    sb.append((String) null);
                    logd(sb.toString());
                    return null;
                }
            } catch (IOException | NullPointerException e2) {
                loge("Error reading file " + str + ": " + e2);
                sb = new StringBuilder("jsonString is ");
                sb.append((String) null);
                logd(sb.toString());
                return null;
            }
        } catch (Throwable th5) {
            logd("jsonString is " + ((String) null));
            throw th5;
        }
    }

    private static boolean isValidEarfcn(int i) {
        if (i >= 0) {
            return true;
        }
        loge("isValidEarfcn: earfcn value is out of valid range: " + i);
        return false;
    }

    private static boolean isValidEarfcnRange(int i, int i2) {
        if (i <= i2) {
            return true;
        }
        loge("isValidEarfcnRange: earfcn range start " + i + " is bigger than end " + i2);
        return false;
    }

    private static EarfcnRange parseEarfcnRange(JSONObject jSONObject) {
        logd("parseEarfcnRange");
        if (jSONObject == null) {
            loge("parseEarfcnRange: jsonObject is null");
            return null;
        }
        try {
            int i = jSONObject.getInt("start_earfcn");
            int i2 = jSONObject.getInt("end_earfcn");
            if (isValidEarfcn(i) && isValidEarfcn(i2) && isValidEarfcnRange(i, i2)) {
                return new EarfcnRange(i, i2);
            }
            loge("parseEarfcnRange: earfcn value is not valid, return null");
            return null;
        } catch (JSONException e) {
            loge("parseEarfcnRange: json parsing error: " + e.getMessage());
            return null;
        }
    }

    private static List parseIntegerList(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        if (jSONArray == null) {
            loge("parseIntegerList: jsonArray is null, return IntArray with empty");
            return arrayList;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                arrayList.add(Integer.valueOf(jSONArray.getInt(i)));
            } catch (JSONException e) {
                loge("parseIntegerList: jsonArray parsing error: " + e.getMessage());
                arrayList.clear();
            }
        }
        logd("parseIntegerList: " + arrayList);
        return arrayList;
    }
}
