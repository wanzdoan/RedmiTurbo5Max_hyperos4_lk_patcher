package com.android.phone.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.CellSignalStrengthNr;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneProxy;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.NetworkSceneEvent;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.ModemUtils;
import com.ot.pubsub.a.b;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SpecialNetworkStat {
    private static SpecialNetworkStat sInstance;
    private Context mContext;
    private SpecialNetworkHandler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    private final BaseEvent.IEventListener mListener;
    private ModemUtils mModemUtils;
    private TelephonyManager mTelephonyManager;
    private HashMap mLinkMetricCause = new HashMap();
    private HashMap mRecoveryAction = new HashMap();
    private ArrayList mRecords = new ArrayList();
    private final DecimalFormat mFloatDecimalFormat = new DecimalFormat("0.00");

    public static SpecialNetworkStat getInstance() {
        return sInstance;
    }

    public static void init(Context context) {
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported() && sInstance == null) {
            sInstance = new SpecialNetworkStat(context);
        }
    }

    private SpecialNetworkStat(Context context) {
        this.mHandler = null;
        this.mModemUtils = null;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.SpecialNetworkStat.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if ("com.android.phone.intent.action.LINK_RELIABILITY".equals(action)) {
                    SpecialNetworkStat.this.mHandler.obtainMessage(MiRilHook.HOOK_ID_STOP_URC_BY_AT, intent).sendToTarget();
                } else if ("com.miui.geolocation.intent.action.GET_ALL_GEO_LOCATION_LIST".equals(action)) {
                    SpecialNetworkStat.this.mHandler.obtainMessage(302, intent).sendToTarget();
                }
            }
        };
        this.mIntentReceiver = broadcastReceiver;
        this.mListener = new BaseEvent.IEventListener() { // from class: com.android.phone.statistics.SpecialNetworkStat.2
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                Bundle bundle;
                if (!(baseEvent instanceof NetworkSceneEvent.SelfRecoveryEvent) || (bundle = ((NetworkSceneEvent.SelfRecoveryEvent) baseEvent).getBundle()) == null) {
                    return;
                }
                int i = bundle.getInt("recovery_action");
                Utils.logD("SpecialNetworkStat", "onRecoveryActionDone action:" + i);
                SpecialNetworkStat.this.mHandler.obtainMessage(303, i, 0).sendToTarget();
            }
        };
        this.mContext = context;
        this.mHandler = new SpecialNetworkHandler(Looper.myLooper());
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.LINK_RELIABILITY");
        intentFilter.addAction("com.miui.geolocation.intent.action.GET_ALL_GEO_LOCATION_LIST");
        this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        initNetworkRecoveryListener();
        if (MiuiPhoneUtils.isModemHydraStatSupported()) {
            this.mModemUtils = ModemUtils.init(context);
        }
    }

    public class SpecialNetworkHandler extends Handler {
        public SpecialNetworkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case MiRilHook.HOOK_ID_STOP_URC_BY_AT /* 301 */:
                    SpecialNetworkStat.this.processLinkReliability((Intent) message.obj);
                    break;
                case 302:
                    SpecialNetworkStat.this.processGeoLocaionList((Intent) message.obj);
                    break;
                case 303:
                    SpecialNetworkStat.this.processNetworkRecoveryAction(message.arg1);
                    break;
            }
        }
    }

    public void reportSpecialNetworkEvent() {
        reportSpecialNetworkDataEvent();
        reportGeoLocationListEvent();
    }

    public void reloadSpecialNetworkDataFromSp(SharedPreferences sharedPreferences) {
        if (sharedPreferences != null) {
            reloadSpecialNetworkLinkCauseFromSP(sharedPreferences);
            reloadSpecialNetworkRecoveryActionFromSP(sharedPreferences);
            reloadSpecialNetworkRadioInfoFromSP(sharedPreferences);
        }
    }

    public void recordSpecialNetworkData() {
        try {
            Utils.logD("SpecialNetworkStat", "record linkCause: " + this.mLinkMetricCause);
            if (!this.mLinkMetricCause.isEmpty()) {
                saveStringDataToSP("LinkCause", new JSONObject(this.mLinkMetricCause).toString());
            }
            Utils.logD("SpecialNetworkStat", "record recoveryAction: " + this.mRecoveryAction);
            if (!this.mRecoveryAction.isEmpty()) {
                saveStringDataToSP("RecoveryAction", new JSONObject(this.mRecoveryAction).toString());
            }
            Utils.logD("SpecialNetworkStat", "mRecords: " + this.mRecords);
            if (this.mRecords.size() > 0) {
                saveStringDataToSP("RadioInfo", new JSONArray((Collection) this.mRecords).toString());
            }
        } catch (Exception e) {
            Rlog.d("SpecialNetworkStat", "record Exception: " + e);
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processGeoLocaionList(Intent intent) {
        String stringExtra = intent.getStringExtra("GEOLocationList");
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        saveStringDataToSP("LocationList", stringExtra);
    }

    private void reportGeoLocationListEvent() {
        JSONArray jSONArrayOptJSONArray;
        String string = this.mContext.getSharedPreferences("telephony_statistic_data", 0).getString("LocationList", "");
        if (TextUtils.isEmpty(string)) {
            return;
        }
        try {
            JSONObject jSONObjectOptJSONObject = new JSONObject(string).optJSONObject("m");
            if (jSONObjectOptJSONObject != null && (jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray("geolocationInfo")) != null) {
                HashMap map = new HashMap();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    JSONObject jSONObjectOptJSONObject2 = jSONArrayOptJSONArray.optJSONObject(i);
                    if (jSONObjectOptJSONObject2 != null) {
                        JSONObject jSONObjectOptJSONObject3 = jSONObjectOptJSONObject2.optJSONObject("f");
                        if (jSONObjectOptJSONObject3 != null) {
                            HashMap map2 = new HashMap();
                            map2.put("a", jSONObjectOptJSONObject3.optString("a"));
                            map2.put("la", jSONObjectOptJSONObject3.optString("la"));
                            map2.put("lo", jSONObjectOptJSONObject3.optString("lo"));
                            map2.put("n", jSONObjectOptJSONObject3.optString("n"));
                            map2.put("r", jSONObjectOptJSONObject3.optString("r"));
                            arrayList.add(map2);
                        }
                        map.put("Params", arrayList);
                    }
                }
                Utils.logD("SpecialNetworkStat", "reportGeoLocationListEvent data=" + map);
                if (arrayList.size() > 0) {
                    MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_geo_location_list", map);
                }
            }
        } catch (Exception e) {
            Rlog.d("SpecialNetworkStat", "report geoList Exception: " + e);
            e.printStackTrace();
        }
    }

    private void initNetworkRecoveryListener() {
        EventHubManager.getInstance().registerListener("network", "self_recovery_done", this.mListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkRecoveryAction(int i) {
        if (i != 0) {
            String strValueOf = String.valueOf(i);
            this.mRecoveryAction.put(strValueOf, Integer.valueOf((this.mRecoveryAction.get(strValueOf) != null ? ((Integer) this.mRecoveryAction.get(strValueOf)).intValue() : 0) + 1));
            Utils.logD("SpecialNetworkStat", "processNetworkRecoveryAction action:" + this.mRecoveryAction);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLinkReliability(Intent intent) {
        int intExtra = intent.getIntExtra("metricsCause", -1);
        Utils.logD("SpecialNetworkStat", "processLinkReliability cause:" + intExtra);
        if (intExtra == 0 || intExtra == -1) {
            return;
        }
        String strValueOf = String.valueOf(intExtra);
        this.mLinkMetricCause.put(strValueOf, Integer.valueOf((this.mLinkMetricCause.get(strValueOf) != null ? ((Integer) this.mLinkMetricCause.get(strValueOf)).intValue() : 0) + 1));
        recordRadioInfo(intExtra);
    }

    private void reloadSpecialNetworkLinkCauseFromSP(SharedPreferences sharedPreferences) {
        try {
            String string = sharedPreferences.getString("LinkCause", "");
            if (!TextUtils.isEmpty(string)) {
                JSONObject jSONObject = new JSONObject(string);
                this.mLinkMetricCause.clear();
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    this.mLinkMetricCause.put(next, Integer.valueOf(jSONObject.optInt(next)));
                }
            }
            Utils.logD("SpecialNetworkStat", "reload linkCause: " + this.mLinkMetricCause);
        } catch (Exception e) {
            Rlog.d("SpecialNetworkStat", "reload linkCause exception: " + e);
            e.printStackTrace();
        }
    }

    private void reloadSpecialNetworkRecoveryActionFromSP(SharedPreferences sharedPreferences) {
        try {
            String string = sharedPreferences.getString("RecoveryAction", "");
            if (!TextUtils.isEmpty(string)) {
                JSONObject jSONObject = new JSONObject(string);
                this.mRecoveryAction.clear();
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    this.mRecoveryAction.put(next, Integer.valueOf(jSONObject.optInt(next)));
                }
            }
            Utils.logD("SpecialNetworkStat", "reload recoveryAction: " + this.mRecoveryAction);
        } catch (Exception e) {
            Rlog.d("SpecialNetworkStat", "reload recoveryAction exception: " + e);
            e.printStackTrace();
        }
    }

    private void reloadSpecialNetworkRadioInfoFromSP(SharedPreferences sharedPreferences) {
        try {
            String string = sharedPreferences.getString("RadioInfo", "");
            if (!TextUtils.isEmpty(string)) {
                JSONArray jSONArray = new JSONArray(string);
                this.mRecords.clear();
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                    if (jSONObjectOptJSONObject != null) {
                        HashMap map = new HashMap();
                        Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
                        while (itKeys.hasNext()) {
                            String next = itKeys.next();
                            map.put(next, jSONObjectOptJSONObject.optString(next));
                        }
                        this.mRecords.add(map);
                    }
                }
            }
            Utils.logD("SpecialNetworkStat", "reload radioInfo: " + this.mRecords);
        } catch (Exception e) {
            Rlog.d("SpecialNetworkStat", "reload radioInfo exception: " + e);
            e.printStackTrace();
        }
    }

    private void reportSpecialNetworkDataEvent() {
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        map.put("SpecialNetwork", MiuiPhoneUtils.isSpecialNetworkModeEnabled(this.mContext) ? "1" : "0");
        if (!this.mLinkMetricCause.isEmpty()) {
            for (String str : this.mLinkMetricCause.keySet()) {
                HashMap map2 = new HashMap();
                map2.put("LinkCause", str);
                map2.put("Count", this.mLinkMetricCause.get(str));
                arrayList.add(map2);
            }
        }
        if (!this.mRecoveryAction.isEmpty()) {
            for (String str2 : this.mRecoveryAction.keySet()) {
                HashMap map3 = new HashMap();
                map3.put("RecoveryAction", str2);
                map3.put("Count", this.mRecoveryAction.get(str2));
                arrayList.add(map3);
            }
        }
        if (arrayList.size() > 0) {
            map.put("Params", arrayList);
        }
        if (this.mRecords.size() > 0) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(this.mRecords);
            map.put("RadioInfo", arrayList2);
        }
        Utils.logD("SpecialNetworkStat", "reportSpecialNetworkDataEvent data=" + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "special_network_link", map);
        clear();
    }

    private void clear() {
        this.mLinkMetricCause.clear();
        this.mRecoveryAction.clear();
        this.mRecords.clear();
    }

    private void saveStringDataToSP(String str, String str2) {
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("telephony_statistic_data", 0).edit();
        editorEdit.putString(str, str2);
        editorEdit.apply();
    }

    private void recordRadioInfo(int i) {
        int activeDataPhoneId = getActiveDataPhoneId();
        Utils.logD("SpecialNetworkStat", "recordRadioInfo ddsPhoneId=" + activeDataPhoneId);
        HashMap radioInfo = getRadioInfo(activeDataPhoneId);
        if (radioInfo.size() <= 0 || this.mRecords.size() > 20) {
            return;
        }
        radioInfo.put("LinkCause", String.valueOf(i));
        Utils.logD("SpecialNetworkStat", "recordRadioInfo radioInfo=" + radioInfo);
        this.mRecords.add(radioInfo);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v12, types: [boolean] */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v16 */
    /* JADX WARN: Type inference failed for: r4v20 */
    /* JADX WARN: Type inference failed for: r4v21 */
    /* JADX WARN: Type inference failed for: r4v25 */
    /* JADX WARN: Type inference failed for: r4v26 */
    /* JADX WARN: Type inference failed for: r4v27 */
    /* JADX WARN: Type inference failed for: r4v28 */
    /* JADX WARN: Type inference failed for: r4v29 */
    /* JADX WARN: Type inference failed for: r4v30 */
    /* JADX WARN: Type inference failed for: r4v7 */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r4v9, types: [int] */
    private HashMap getRadioInfo(int i) {
        ServiceState serviceState;
        int lteSnr;
        String str;
        int i2;
        int iCallQcrilHookGet;
        ?? r4;
        ?? r5;
        float fCallQcrilHookGetFloat;
        int iCallQcrilHookGet2;
        HashMap map = new HashMap();
        Phone phone = PhoneFactory.getPhone(i);
        if (phone != null && MiuiPhoneUtils.isValidatePhoneId(i) && (serviceState = phone.getServiceState()) != null) {
            map.put("Network", getNetworkOperator(serviceState, i));
            int dataNetworkType = Utils.getDataNetworkType(serviceState);
            Utils.logD("SpecialNetworkStat", "getRadioInfo nwType = " + dataNetworkType);
            map.put("RAT", String.valueOf(dataNetworkType));
            int dbm = getDbm(phone);
            if (Utils.isNr(dataNetworkType)) {
                lteSnr = getNrSnr(phone);
                str = "N" + Utils.getOperatingBandForNrarfcn(serviceState);
            } else {
                lteSnr = Utils.isLte(dataNetworkType) ? getLteSnr(phone) : -200;
                str = b.a + PhoneProxy.getOperatingBandForEarfcn(serviceState);
            }
            map.put("Rsrp", String.valueOf(dbm));
            map.put("Snr", String.valueOf(lteSnr));
            map.put("Band", str);
            ModemUtils modemUtils = this.mModemUtils;
            if (modemUtils != null) {
                int i3 = -1;
                try {
                    iCallQcrilHookGet = modemUtils.callQcrilHookGet("getAntPath");
                    try {
                        ?? IsNr = Utils.isNr(dataNetworkType);
                        try {
                            try {
                                try {
                                    if (IsNr != 0) {
                                        dataNetworkType = this.mModemUtils.callQcrilHookGet("getNr5gPuschTx", i);
                                        int iCallQcrilHookGet3 = this.mModemUtils.callQcrilHookGet("getNr5gRbNum", i);
                                        fCallQcrilHookGetFloat = this.mModemUtils.callQcrilHookGetFloat("getNr5gBler", i);
                                        iCallQcrilHookGet2 = this.mModemUtils.callQcrilHookGet("getNr5gUlMcs", i);
                                        IsNr = iCallQcrilHookGet3;
                                    } else {
                                        if (Utils.isLte(dataNetworkType)) {
                                            dataNetworkType = this.mModemUtils.callQcrilHookGet("getLtePuschTx", i);
                                            int iCallQcrilHookGet4 = this.mModemUtils.callQcrilHookGet("getLtePdschRb", i);
                                            fCallQcrilHookGetFloat = this.mModemUtils.callQcrilHookGetFloat("getLteBler", i);
                                            iCallQcrilHookGet2 = this.mModemUtils.callQcrilHookGet("getLtePuschMcs", i);
                                            IsNr = iCallQcrilHookGet4;
                                        } else {
                                            fCallQcrilHookGetFloat = -1.0f;
                                            i2 = -1;
                                            r4 = -1;
                                        }
                                        map.put("Bler", this.mFloatDecimalFormat.format(fCallQcrilHookGetFloat));
                                        r5 = r4;
                                        map.put("PushchTx", String.valueOf(i3));
                                        map.put("PdschRb", String.valueOf((int) r5));
                                        map.put("AntPath", String.valueOf(iCallQcrilHookGet));
                                        map.put("Mcs", String.valueOf(i2));
                                    }
                                    map.put("Bler", this.mFloatDecimalFormat.format(fCallQcrilHookGetFloat));
                                    r5 = r4;
                                } catch (Exception e) {
                                    e = e;
                                    Rlog.d("SpecialNetworkStat", "ModemUtils exception: " + e);
                                    r5 = r4;
                                }
                                i2 = iCallQcrilHookGet2;
                                i3 = dataNetworkType;
                                r4 = IsNr;
                            } catch (Exception e2) {
                                e = e2;
                                i2 = i3;
                                r4 = IsNr;
                                i3 = dataNetworkType;
                                Rlog.d("SpecialNetworkStat", "ModemUtils exception: " + e);
                                r5 = r4;
                                map.put("PushchTx", String.valueOf(i3));
                                map.put("PdschRb", String.valueOf((int) r5));
                                map.put("AntPath", String.valueOf(iCallQcrilHookGet));
                                map.put("Mcs", String.valueOf(i2));
                                return map;
                            }
                        } catch (Exception e3) {
                            e = e3;
                            i2 = i3;
                            r4 = i2;
                            i3 = dataNetworkType;
                            Rlog.d("SpecialNetworkStat", "ModemUtils exception: " + e);
                            r5 = r4;
                            map.put("PushchTx", String.valueOf(i3));
                            map.put("PdschRb", String.valueOf((int) r5));
                            map.put("AntPath", String.valueOf(iCallQcrilHookGet));
                            map.put("Mcs", String.valueOf(i2));
                            return map;
                        }
                    } catch (Exception e4) {
                        e = e4;
                        i2 = -1;
                        r4 = -1;
                    }
                } catch (Exception e5) {
                    e = e5;
                    i2 = -1;
                    iCallQcrilHookGet = -1;
                    r4 = -1;
                }
                map.put("PushchTx", String.valueOf(i3));
                map.put("PdschRb", String.valueOf((int) r5));
                map.put("AntPath", String.valueOf(iCallQcrilHookGet));
                map.put("Mcs", String.valueOf(i2));
            }
        }
        return map;
    }

    private int getDbm(Phone phone) {
        SignalStrength signalStrength;
        if (phone == null || (signalStrength = phone.getSignalStrength()) == null) {
            return -140;
        }
        return signalStrength.getDbm();
    }

    private int getLteSnr(Phone phone) {
        SignalStrength signalStrength;
        if (phone == null || (signalStrength = phone.getSignalStrength()) == null) {
            return -200;
        }
        return signalStrength.getLteRssnr();
    }

    private int getNrSnr(Phone phone) {
        SignalStrength signalStrength;
        if (phone == null || (signalStrength = phone.getSignalStrength()) == null) {
            return -200;
        }
        List cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
        CellSignalStrengthNr cellSignalStrengthNr = (cellSignalStrengths == null || cellSignalStrengths.size() <= 0) ? null : (CellSignalStrengthNr) cellSignalStrengths.get(0);
        if (cellSignalStrengthNr != null) {
            return cellSignalStrengthNr.getSsSinr();
        }
        return -200;
    }

    private String getNetworkOperator(ServiceState serviceState, int i) {
        TelephonyManager telephonyManager;
        if (serviceState == null) {
            return "";
        }
        String operatorNumeric = serviceState.getOperatorNumeric();
        return (!TextUtils.isEmpty(operatorNumeric) || (telephonyManager = this.mTelephonyManager) == null) ? operatorNumeric : telephonyManager.getSimOperatorNumericForPhone(i);
    }

    private int getActiveDataPhoneId() {
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        if (SubscriptionManager.isValidSubscriptionId(activeDataSubscriptionId)) {
            return SubscriptionManager.getPhoneId(activeDataSubscriptionId);
        }
        return -1;
    }
}
