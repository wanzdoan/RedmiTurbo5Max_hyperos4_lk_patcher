package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CloudModemVolteController {
    private static String LOG_TAG = "CloudModemVolteController";
    private static CloudModemVolteController mInstance;
    private CloudDataHandler mBaseHandler;
    private Context mContext;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.CloudModemVolteController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && "android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(action)) {
                CloudModemVolteController.this.mBaseHandler.obtainMessage(2, intent).sendToTarget();
            }
        }
    };

    public static CloudModemVolteController getInstance() {
        if (mInstance == null) {
            mInstance = new CloudModemVolteController();
        }
        return mInstance;
    }

    private CloudModemVolteController() {
        this.mContext = null;
        this.mBaseHandler = null;
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread(LOG_TAG);
        handlerThread.start();
        this.mBaseHandler = new CloudDataHandler(handlerThread.getLooper());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
        Log.d(LOG_TAG, "init done");
    }

    class CloudDataHandler extends Handler {
        public CloudDataHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d(CloudModemVolteController.LOG_TAG, "handle message what: " + message.what);
            int i = message.what;
            if (i == 1) {
                CloudModemVolteController.this.getCloudvoltecfgInfos((String) message.obj, message.arg1);
            } else {
                if (i != 2) {
                    return;
                }
                CloudModemVolteController.this.onSimStateChanged((Intent) message.obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimStateChanged(Intent intent) {
        int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
        Log.d(LOG_TAG, "onSimStateChanged simState =" + intExtra);
        if (intExtra == 10) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                String simOperator = PhoneProxy.getSimOperator(i);
                if (!TextUtils.isEmpty(simOperator)) {
                    getCloudvoltecfgInfos(simOperator, i);
                }
            }
        }
    }

    public void cloudChanged() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            String simOperator = PhoneProxy.getSimOperator(i);
            if (!TextUtils.isEmpty(simOperator)) {
                CloudDataHandler cloudDataHandler = this.mBaseHandler;
                cloudDataHandler.sendMessage(cloudDataHandler.obtainMessage(1, i, -1, simOperator));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCloudvoltecfgInfos(String str, int i) {
        try {
            List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "TelephonyCommonFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                updateCloudmidvoltecfg(str, i, cloudDataList);
                return;
            }
            setVoltecfgState(i, "1", str, "", "");
            Log.d(LOG_TAG, "getCloudvoltecfgInfos cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCloudmidvoltecfg(String str, int i, List list) {
        Iterator it = list.iterator();
        boolean zUpdatemidvoltecfg = false;
        while (it.hasNext()) {
            MiuiSettings.SettingsCloudData.CloudData cloudData = (MiuiSettings.SettingsCloudData.CloudData) it.next();
            if (cloudData != null) {
                Log.d(LOG_TAG, "updateCloudmidvoltecfg data=" + cloudData.toString());
                if ("voltecfg".equals(cloudData.getString("FeatureType", ""))) {
                    zUpdatemidvoltecfg = updatemidvoltecfg(str, i, cloudData.json().optString("Status"), cloudData.json().optJSONArray("list"));
                    Log.d(LOG_TAG, "updateCloudmidvoltecfg setState=" + zUpdatemidvoltecfg);
                }
            }
        }
        if (zUpdatemidvoltecfg) {
            return;
        }
        setVoltecfgState(i, "1", str, "", "");
    }

    private boolean updatemidvoltecfg(String str, int i, String str2, JSONArray jSONArray) {
        JSONObject jSONObjectOptJSONObject;
        if (jSONArray == null) {
            return false;
        }
        for (int i2 = 0; i2 < jSONArray.length() && (jSONObjectOptJSONObject = jSONArray.optJSONObject(i2)) != null; i2++) {
            String strOptString = jSONObjectOptJSONObject.optString("SimOperator");
            if ("FFFFF".equals(strOptString) || str.equals(strOptString)) {
                setVoltecfgState(i, str2, str, jSONObjectOptJSONObject.optString("Params1"), jSONObjectOptJSONObject.optString("Params2"));
                return true;
            }
        }
        return false;
    }

    public void setVoltecfgState(int i, String str, String str2, String str3, String str4) {
        if (MiuiPhoneUtils.getPhone(i) != null) {
            try {
                FiveGManagerBase.getInstance().sendAtCmdSync(i, new String[]{"MIPC_SET_MDVC_STATE", str, str2, str3, str4}, null);
                SystemProperties.set("persist.radio.midvoltecfg" + i + ".enable", str);
                SystemProperties.set("persist.radio.midvoltecfg" + i + ".plmn", str2);
                SystemProperties.set("persist.radio.midvoltecfg" + i + ".config", str3);
                SystemProperties.set("persist.radio.midvoltecfg" + i + ".timer", str4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
