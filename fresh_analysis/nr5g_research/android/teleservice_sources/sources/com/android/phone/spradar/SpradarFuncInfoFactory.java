package com.android.phone.spradar;

import android.telephony.Rlog;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import miui.os.HyperOSCustFeatureResolve;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class SpradarFuncInfoFactory {
    private static final Map FUNC_MAP = new HashMap();
    private static final Set FUNC_TYPE_WHITE_LIST;
    private static List funcTypeWhiteList;

    static {
        List stringArray = HyperOSCustFeatureResolve.getStringArray("config_sprd_support_func_list");
        funcTypeWhiteList = stringArray;
        FUNC_TYPE_WHITE_LIST = parseWhiteList(stringArray);
        register(SpradarCallExceptionReportFuncInfo.class);
        register(SpradarLTEMessageExceptionFuncInfo.class);
        register(SpradarLTERegisterRejectExceptionFuncInfo.class);
        register(SpradarDataExceptionReportFuncInfo.class);
        register(SpradarRecent10SDataFuncInfo.class);
        register(SpradarLTERRCRecreateFuncInfo.class);
        register(SpradarLTERACHFailFuncInfo.class);
        register(SpradarPagingFailFuncInfo.class);
        register(SpradarVoiceQualityStatisticsFuncInfo.class);
        register(SpradarRLFFuncInfo.class);
    }

    private static Set parseWhiteList(List list) {
        HashSet hashSet = new HashSet();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (str != null) {
                    try {
                        hashSet.add(Integer.valueOf(Integer.parseInt(str.trim())));
                    } catch (NumberFormatException e) {
                        loge("" + e.getMessage());
                    }
                }
            }
        } else {
            loge("whiteList is null");
        }
        logd("config_sprd_support_func_list = " + hashSet);
        return hashSet;
    }

    private static void register(Class cls) {
        try {
            int funcId = ((SpradarFuncInfo) cls.newInstance()).getFuncId();
            if (funcId == -1 || !FUNC_TYPE_WHITE_LIST.contains(Integer.valueOf(funcId))) {
                return;
            }
            FUNC_MAP.put(Integer.valueOf(funcId), cls);
        } catch (Exception e) {
            loge("Failed to register class: " + cls.getName() + e);
        }
    }

    public static Set getSpradarFuncIds(int... iArr) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Class cls : FUNC_MAP.values()) {
            try {
                Class[] clsArr = new Class[0];
                SpradarFuncInfo spradarFuncInfo = (SpradarFuncInfo) cls.getDeclaredConstructor(null).newInstance(null);
                int funcId = spradarFuncInfo.getFuncId();
                int funcType = spradarFuncInfo.getFuncType();
                int responseType = spradarFuncInfo.getResponseType();
                if (funcId == -1 || funcType == -1 || responseType == -1) {
                    loge("getSpradarFuncIds: params definition error, " + cls.getName());
                } else {
                    for (int i : iArr) {
                        if (funcType == i) {
                            linkedHashSet.add(Integer.valueOf(funcId));
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                loge("getSpradarFuncIds: " + cls.getName() + " " + e.getMessage());
            }
        }
        return linkedHashSet;
    }

    public static List createFuncInfo(int i, int i2, String str) {
        Class cls = (Class) FUNC_MAP.get(Integer.valueOf(i2));
        if (cls == null) {
            loge("createFuncInfo: no class for funcId: " + i2);
            return null;
        }
        ArrayList arrayList = new ArrayList();
        try {
            Class[] clsArr = new Class[0];
            SpradarFuncInfo spradarFuncInfo = (SpradarFuncInfo) cls.getDeclaredConstructor(null).newInstance(null);
            int responseType = spradarFuncInfo.getResponseType();
            if (responseType == 1) {
                JSONArray jSONArray = new JSONArray(str);
                for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                    SpradarFuncInfo spradarFuncInfoFromJson = spradarFuncInfo.fromJson(i, jSONArray.getJSONObject(i3));
                    if (spradarFuncInfoFromJson != null) {
                        arrayList.add(spradarFuncInfoFromJson);
                    }
                }
            } else if (responseType == 0) {
                SpradarFuncInfo spradarFuncInfoFromJson2 = spradarFuncInfo.fromJson(i, new JSONObject(str));
                if (spradarFuncInfoFromJson2 != null) {
                    arrayList.add(spradarFuncInfoFromJson2);
                }
            } else {
                loge("createFuncInfo: info is null");
                return null;
            }
            logd("createFuncInfo: returning list with size = " + arrayList.size());
            return arrayList;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | JSONException e) {
            loge("createFunc: " + e);
            return null;
        }
    }

    public static String timeLongToStringEx(long j) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(j));
    }

    public static String timeLongToStringEx(String str) {
        if (str == null || str.trim().isEmpty()) {
            return timeLongToStringEx(0L);
        }
        try {
            return timeLongToStringEx(new SimpleDateFormat("yyMMddHHmmssSSS").parse(str.trim()).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return timeLongToStringEx(0L);
        }
    }

    private static void loge(String str) {
        Rlog.e("SpradarFuncInfoFactory", str);
    }

    private static void logd(String str) {
        Rlog.d("SpradarFuncInfoFactory", str);
    }
}
