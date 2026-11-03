package com.android.phone.statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.android.phone.FiveGCloudController;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.RilHook;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class ModemComponentStats {
    private static ModemComponentStats sInstance;
    private boolean mQet7100Status = true;
    private int mQet7100DayCount = 3;
    private int mQet7100TotalCount = 5;
    private Context mContext = PhoneGlobals.getInstance();
    private ModemComponentHandler mHandler = new ModemComponentHandler(Looper.myLooper());

    private ModemComponentStats() {
    }

    public static synchronized void init(Context context) {
        if (MiuiPhoneUtils.isModemComponentExceptionSupported()) {
            if (sInstance == null) {
                sInstance = new ModemComponentStats();
            }
        }
    }

    public static synchronized ModemComponentStats getInstance() {
        return sInstance;
    }

    public class ModemComponentHandler extends Handler {
        public ModemComponentHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 301) {
                return;
            }
            ModemComponentStats.this.updateQet7100ComponentCloudData();
        }
    }

    /* JADX WARN: Code duplicated, block: B:7:0x003d  */
    public void processModemReset() {
        String str;
        String modemCrashByNodeFile = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getModemCrashByNodeFile();
        if (!TextUtils.isEmpty(modemCrashByNodeFile)) {
            String[] strArrSplit = modemCrashByNodeFile.split(":");
            if (strArrSplit.length >= 2) {
                str = strArrSplit[0].trim() + ":" + strArrSplit[1].trim();
            } else {
                str = "";
            }
        } else {
            str = "";
        }
        Utils.logI("ModemComponentStats", "reason: ".concat(str));
        recordModemComponentExceptionData(str);
        recordModemReset(str);
    }

    public void notifyQet7100CloudData() {
        Utils.logI("ModemComponentStats", "notify component cloud data");
        this.mHandler.obtainMessage(MiRilHook.HOOK_ID_STOP_URC_BY_AT).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateQet7100ComponentCloudData() {
        if (FiveGCloudController.getQet7100StatusByCloud() == 0) {
            this.mQet7100Status = false;
        }
        int qet7100DayCountByCloud = FiveGCloudController.getQet7100DayCountByCloud();
        if (qet7100DayCountByCloud != -1) {
            this.mQet7100DayCount = qet7100DayCountByCloud;
        }
        int qet7100TotalCountByCloud = FiveGCloudController.getQet7100TotalCountByCloud();
        if (qet7100TotalCountByCloud != -1) {
            this.mQet7100TotalCount = qet7100TotalCountByCloud;
        }
        Utils.logI("ModemComponentStats", "update component 1: " + this.mQet7100Status + ", " + this.mQet7100DayCount + ", " + this.mQet7100TotalCount);
        if (this.mQet7100Status) {
            return;
        }
        deleteModemComponentPropById("1");
        clearModemQet7100ComponentData();
    }

    private void recordModemComponentExceptionData(String str) {
        if ("rfdevice_qet_switcher.cpp:335".equalsIgnoreCase(str) && this.mQet7100Status && updateModemQet7100CompontData()) {
            updateModemComponentPropById("1");
        }
    }

    private void recordModemReset(String str) {
        HashMap map = new HashMap();
        boolean zIsMobileDataEnabled = MiuiPhoneUtils.isMobileDataEnabled(this.mContext);
        map.put("Network", MiuiPhoneUtils.getSimOperatorNumeric());
        map.put("dataSwitch", Boolean.valueOf(zIsMobileDataEnabled));
        map.put("Reason", str);
        map.put("dsda", String.valueOf(Utils.getDsdaState().ordinal()));
        if (MiuiTelephonyStatistic.getInstance() != null) {
            map.put("sceneType", MiuiTelephonyStatistic.getInstance().getSceneType());
        }
        map.put("time", Utils.systemTimeToString(System.currentTimeMillis()));
        Utils.logD("ModemComponentStats", "recordModemReset: " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_modem_crash", map);
    }

    private boolean updateModemQet7100CompontData() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("modem_component", 0);
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        int i = sharedPreferences.getInt("DayCount1", 0);
        int i2 = sharedPreferences.getInt("TotalCount1", 0);
        long j = sharedPreferences.getLong("Time", 0L);
        long jCurrentTimeMillis = System.currentTimeMillis();
        int i3 = (j == 0 || jCurrentTimeMillis - j > v.b) ? 1 : i + 1;
        int i4 = i2 + 1;
        Utils.logI("ModemComponentStats", "dayCount: " + i3 + ", totalCount: " + i4);
        editorEdit.putInt("DayCount1", i3);
        editorEdit.putInt("TotalCount1", i4);
        editorEdit.putLong("Time", jCurrentTimeMillis);
        editorEdit.apply();
        return this.mQet7100Status && (i3 >= this.mQet7100DayCount || i4 >= this.mQet7100TotalCount);
    }

    private boolean updateModemQet7100CompontEfsData(int i) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("modem_component", 0);
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        int i2 = sharedPreferences.getInt("TotalCount1", 0) + i;
        editorEdit.putInt("TotalCount1", i2);
        editorEdit.apply();
        return this.mQet7100Status && (i >= this.mQet7100DayCount || i2 >= this.mQet7100TotalCount);
    }

    private void clearModemQet7100ComponentData() {
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("modem_component", 0).edit();
        editorEdit.remove("DayCount1");
        editorEdit.remove("TotalCount1");
        editorEdit.remove("Time");
        editorEdit.apply();
    }

    private void deleteModemComponentPropById(String str) {
        String[] strArrSplit;
        String str2 = SystemProperties.get("persist.radio.component.id");
        Utils.logI("ModemComponentStats", "delete component id: " + str2);
        if (TextUtils.isEmpty(str2) || (strArrSplit = str2.split(",")) == null || strArrSplit.length <= 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (String str3 : strArrSplit) {
            arrayList.add(str3.trim());
        }
        if (arrayList.remove(str)) {
            String strReplace = arrayList.toString().replace("[", "").replace("]", "");
            Utils.logI("ModemComponentStats", "delete component result: " + strReplace);
            if (strReplace != null && strReplace.length() < 91) {
                SystemProperties.set("persist.radio.component.id", strReplace);
            }
        }
        if (arrayList.isEmpty()) {
            SystemProperties.set("persist.radio.component.time", "");
        }
    }

    private void updateModemComponentPropById(String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        String str2 = SystemProperties.get("persist.radio.component.id");
        if (TextUtils.isEmpty(str2)) {
            SystemProperties.set("persist.radio.component.id", str);
        } else if (!str2.contains(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(",");
            sb.append(str);
            if (sb.length() < 91) {
                SystemProperties.set("persist.radio.component.id", sb.toString());
            }
        }
        SystemProperties.set("persist.radio.component.time", String.valueOf(jCurrentTimeMillis));
    }

    public void recordEfsData() {
        ByteBuffer qetAlarmEfsFile;
        RilHook rilHookInit = RilHook.init(this.mContext);
        if (rilHookInit == null || (qetAlarmEfsFile = rilHookInit.readQetAlarmEfsFile(0)) == null || qetAlarmEfsFile.remaining() <= 4) {
            return;
        }
        try {
            int i = qetAlarmEfsFile.getInt();
            int i2 = i * 12;
            Utils.logI("ModemComponentStats", "efsdata remaining: " + qetAlarmEfsFile.remaining());
            if (i <= 0 || qetAlarmEfsFile.remaining() < i2) {
                return;
            }
            if (updateModemQet7100CompontEfsData(i)) {
                updateModemComponentPropById("1");
            }
            HashMap map = new HashMap();
            ArrayList arrayList = new ArrayList();
            byte[] bArrArray = qetAlarmEfsFile.array();
            Utils.logI("ModemComponentStats", "alarmData length: " + bArrArray.length);
            if (bArrArray.length >= i2) {
                for (int i3 = 0; i3 < i2; i3++) {
                    arrayList.add(Byte.valueOf(bArrArray[i3]));
                }
                map.put("Data", arrayList);
            }
            map.put("Count", Integer.valueOf(i));
            Utils.logD("ModemComponentStats", "alarmDataList data: " + map);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_efs_alarm_data", map);
            rilHookInit.deleteQetAlarmEfsFile(0);
        } catch (Exception e) {
            Utils.logD("ModemComponentStats", "ignore exception: " + e);
        }
    }
}
