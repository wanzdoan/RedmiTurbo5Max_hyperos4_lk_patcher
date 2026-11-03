package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MiuiSettings;
import android.provider.Telephony;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CloudApnController {
    public static final Uri CONTENT_URI = Uri.parse("content://telephony/carriers");
    private static String LOG_TAG = "CloudApnController";
    private CloudContentObserVer cloudContentObserver;
    private Context mContext;
    private CloudApnHandler mHandler;
    private BroadcastReceiver mIntentReceiver;
    private final TelCallback mTelCallback;
    public int mDdsId = -1;
    private int mLastDdsSub = Integer.MAX_VALUE;

    public static boolean isUpdateCloudApnSupported() {
        return true;
    }

    protected CloudApnController(Context context, HandlerThread handlerThread) {
        this.mContext = null;
        this.mHandler = null;
        TelCallback telCallback = new TelCallback();
        this.mTelCallback = telCallback;
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.CloudApnController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action != null && "android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(action)) {
                    CloudApnController.this.mHandler.obtainMessage(1, intent).sendToTarget();
                }
            }
        };
        this.mContext = context;
        this.mHandler = new CloudApnHandler(handlerThread.getLooper());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        ((TelephonyManager) this.mContext.getSystemService("phone")).registerTelephonyCallback(new HandlerExecutor(this.mHandler), telCallback);
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
        logd("CloudApnController init");
    }

    class CloudContentObserVer extends ContentObserver {
        public CloudContentObserVer() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            CloudApnController.this.logd("onChange selfChange=" + z + ", uri=" + uri);
            if (!TextUtils.isEmpty(CloudApnController.this.getLastCloudApn())) {
                if (Uri.withAppendedPath(CloudApnController.CONTENT_URI, "restore/subId/" + CloudApnController.this.mLastDdsSub).equals(uri) && CloudApnController.this.mHandler != null) {
                    CloudApnController.this.mHandler.removeMessages(2);
                    CloudApnController.this.mHandler.sendEmptyMessageDelayed(2, 50L);
                    return;
                }
            }
            CloudApnController.this.logd("registerRestoreApnObserver: cloud apn list is null or uri is not restore apn");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRestoreApnObserver(int i) {
        if (this.cloudContentObserver == null) {
            this.cloudContentObserver = new CloudContentObserVer();
        }
        logd("registerRestoreApnObserver: subId=" + i);
        this.mContext.getContentResolver().registerContentObserver(Uri.withAppendedPath(CONTENT_URI, "restore/subId/" + i), false, this.cloudContentObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterRestoreApnObserver() {
        if (this.cloudContentObserver != null) {
            logd("unregisterRestoreApnObserver");
            this.mContext.getContentResolver().unregisterContentObserver(this.cloudContentObserver);
        }
    }

    class TelCallback extends TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener {
        private TelCallback() {
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            CloudApnController.this.logd("onActiveDataSubscriptionIdChanged " + i);
            if (CloudApnController.this.mLastDdsSub == Integer.MAX_VALUE || !(CloudApnController.this.mLastDdsSub == i || CloudApnController.this.mLastDdsSub == Integer.MAX_VALUE)) {
                CloudApnController.this.unregisterRestoreApnObserver();
                CloudApnController.this.registerRestoreApnObserver(i);
                CloudApnController.this.mLastDdsSub = i;
            }
        }
    }

    class CloudApnHandler extends Handler {
        public CloudApnHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                CloudApnController.this.onSimStateChanged((Intent) message.obj);
                return;
            }
            if (i == 2 && !TextUtils.isEmpty(CloudApnController.this.getLastCloudApn())) {
                SharedPreferences.Editor editorEdit = CloudApnController.this.mContext.getSharedPreferences("preference_cloud_apn", 0).edit();
                editorEdit.clear();
                editorEdit.apply();
                CloudApnController.this.updateCloudApn();
                CloudApnController.this.logd("update cloud apn complete");
                CloudApnController.this.mContext.sendBroadcast(new Intent("miui.intent.action.ACTION_UPDATE_CLOUD_APN_COMPLETE"));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimStateChanged(Intent intent) {
        int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
        int intExtra2 = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
        logd("onSimStateChanged subId =" + intExtra2 + ", simState =" + intExtra);
        if (SubscriptionManager.isValidSubscriptionId(intExtra2) && intExtra == 10) {
            updateApn(intExtra2, getCloudApnInfos());
        }
    }

    public void updateCloudApn() {
        List cloudApnInfos = getCloudApnInfos();
        boolean zIsWifiConnected = MiuiPhoneUtils.isWifiConnected();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && phone.getIccCard() != null && phone.getIccCard().getState() == IccCardConstants.State.LOADED) {
                int subscriptionId = SubscriptionManager.getSubscriptionId(i);
                logd("updateCloudApn subId = " + subscriptionId + "isWifiConnected =" + zIsWifiConnected);
                if (SubscriptionManager.isValidSubscriptionId(subscriptionId) && (zIsWifiConnected || !isMobileConnected(i))) {
                    updateApn(subscriptionId, cloudApnInfos);
                }
            }
        }
    }

    private boolean isMobileConnected(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null && phone.getDataConnectionState("default") == PhoneConstants.DataState.CONNECTED) {
            return true;
        }
        logd("mobile connected false");
        return false;
    }

    private synchronized void updateApn(int i, List list) {
        try {
            String simOperator = TelephonyManager.getDefault().getSimOperator(i);
            if (TextUtils.isEmpty(simOperator)) {
                return;
            }
            logd("updateApn operator = " + simOperator);
            boolean alreadyUpdatedValue = getAlreadyUpdatedValue(simOperator);
            boolean zIsCloudApnChanged = isCloudApnChanged(list);
            logd("alreadyUpdated =" + alreadyUpdatedValue + ", apnChanged =" + zIsCloudApnChanged);
            if (!alreadyUpdatedValue || zIsCloudApnChanged) {
                if (zIsCloudApnChanged) {
                    updateLastCloudApn(list);
                }
                if (list != null && list.size() > 0) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        JSONObject jSONObject = (JSONObject) it.next();
                        if (isValidCloudApn(jSONObject) && isCloudApnMatched(i, jSONObject)) {
                            List currentApns = getCurrentApns(i);
                            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("apns");
                            JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("delete_apns");
                            if (jSONArrayOptJSONArray != null) {
                                logd("need insert apn, insertApn length =" + jSONArrayOptJSONArray.length());
                                if (insertCloudApn(i, jSONObject)) {
                                    deleteCurrentApns(i, currentApns, jSONObject);
                                }
                            } else if (jSONArrayOptJSONArray2 != null) {
                                logd("just delete apn, deleteApn length=" + jSONArrayOptJSONArray2.length());
                                deleteCurrentApns(i, currentApns, jSONObject);
                            }
                        } else {
                            logd("invalid cloud apn or not matched");
                        }
                    }
                    setAlreadyUpdatedValue(simOperator, true);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private List getCloudApnInfos() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(PhoneGlobals.getInstance().getContentResolver(), "apn_update");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                ArrayList arrayList = new ArrayList();
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    if (cloudData != null) {
                        arrayList.add(cloudData.json());
                    }
                }
                logd("getCloudApnInfos apnInfos =" + arrayList.size());
                return arrayList;
            }
            logd("get update apns no cloud dataList.");
            return null;
        } catch (Exception e) {
            loge("get cloud apn error!!!", e);
            return null;
        }
    }

    private boolean isCloudApnChanged(List list) {
        String lastCloudApn = getLastCloudApn();
        logd("isCloudApnChanged lastCloudApn =" + lastCloudApn);
        if (list == null && TextUtils.isEmpty(lastCloudApn)) {
            return false;
        }
        if ((list == null && !TextUtils.isEmpty(lastCloudApn)) || (list != null && TextUtils.isEmpty(lastCloudApn))) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            JSONObject jSONObject = (JSONObject) it.next();
            if (jSONObject != null) {
                sb.append(jSONObject.toString());
            }
        }
        boolean z = !TextUtils.equals(lastCloudApn, sb.toString());
        logd("cloud apn changed =" + z);
        return z;
    }

    private boolean isCloudApnMatched(int i, JSONObject jSONObject) {
        if (jSONObject == null) {
            return false;
        }
        String strOptString = jSONObject.optString("model");
        String strOptString2 = jSONObject.optString("customizedRegion");
        logd("isCloudApnMatched model = " + strOptString + "CustomizedRegion =" + strOptString2);
        if (FiveGCloudUtils.modelMatched(strOptString) && FiveGCloudUtils.customizedRegionMatched(strOptString2)) {
            String strOptString3 = jSONObject.optString("mcc");
            String strOptString4 = jSONObject.optString("mnc");
            String simOperator = TelephonyManager.getDefault().getSimOperator(i);
            logd("isCloudApnMatched cloudMcc=" + strOptString3 + ", cloudMnc=" + strOptString4);
            if (!TextUtils.isEmpty(strOptString3) && !TextUtils.isEmpty(strOptString4) && !TextUtils.isEmpty(simOperator)) {
                StringBuilder sb = new StringBuilder();
                sb.append(strOptString3);
                sb.append(strOptString4);
                return simOperator.equals(sb.toString());
            }
        }
        return false;
    }

    private List getCurrentApns(int i) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = this.mContext.getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.SIM_APN_URI, "filtered/subId/" + i), null, "edited=0 OR edited=4", null, "_id");
                if (cursorQuery != null) {
                    while (cursorQuery.moveToNext()) {
                        ApnSetting apnSettingMakeApnSetting = ApnSetting.makeApnSetting(cursorQuery);
                        if (apnSettingMakeApnSetting != null) {
                            arrayList.add(apnSettingMakeApnSetting);
                        }
                    }
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (Exception e) {
                loge("query apn exception", e);
                if (cursorQuery != null) {
                }
            }
            logd("getCurrentApns apns=" + arrayList);
            return arrayList;
        } catch (Throwable th) {
            if (cursorQuery != null) {
                cursorQuery.close();
                throw th;
            }
            throw th;
        }
    }

    private void deleteCurrentApns(int i, List list, JSONObject jSONObject) {
        int size = list.size();
        if (size > 0) {
            try {
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < size; i2++) {
                    ApnSetting apnSetting = (ApnSetting) list.get(i2);
                    if (apnSetting != null && shouldDeleteApn(apnSetting, jSONObject)) {
                        arrayList.add(String.valueOf(apnSetting.getId()));
                        logd("deleteCurrentApns apn=" + apnSetting);
                    }
                }
                String str = "_id IN (" + String.join(",", arrayList) + ")";
                logd("deleteCurrentApns where=" + str);
                ContentValues contentValues = new ContentValues();
                contentValues.put("edited", (Integer) 4);
                ContentResolver contentResolver = this.mContext.getContentResolver();
                Uri uri = Telephony.Carriers.CONTENT_URI;
                logd("updateCurrentApns result=" + contentResolver.update(Uri.withAppendedPath(uri, "subId/" + i), contentValues, str, null));
                logd("deleteCurrentApns result=" + this.mContext.getContentResolver().delete(Uri.withAppendedPath(uri, "subId/" + i), str, null));
            } catch (Exception e) {
                loge("delete apn exception", e);
            }
        }
    }

    private boolean insertCloudApn(int i, JSONObject jSONObject) {
        try {
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("apns");
            String strOptString = jSONObject.optString("mcc");
            String strOptString2 = jSONObject.optString("mnc");
            int i2 = 0;
            for (int i3 = 0; i3 < jSONArrayOptJSONArray.length(); i3++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("mcc", strOptString);
                contentValues.put("mnc", strOptString2);
                if (!TextUtils.isEmpty(strOptString) && !TextUtils.isEmpty(strOptString2)) {
                    contentValues.put("numeric", strOptString + strOptString2);
                }
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i3);
                logd("insertCloudApn: apn = " + jSONObjectOptJSONObject);
                if (jSONObjectOptJSONObject != null) {
                    Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
                    while (itKeys.hasNext()) {
                        String next = itKeys.next();
                        contentValues.put(next, jSONObjectOptJSONObject.getString(next));
                    }
                    contentValues.put("edited", (Integer) 4);
                    Uri uriInsert = this.mContext.getContentResolver().insert(Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "subId/" + i), contentValues);
                    logd("insertCloudApn uri=" + uriInsert);
                    if (uriInsert != null) {
                        i2++;
                    }
                }
            }
            return i2 == jSONArrayOptJSONArray.length();
        } catch (Exception e) {
            loge("insert apn exception", e);
            return false;
        }
    }

    private boolean shouldDeleteApn(ApnSetting apnSetting, JSONObject jSONObject) {
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("delete_apns");
        if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() != 0) {
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject != null) {
                    String strOptString = jSONObjectOptJSONObject.optString("apn");
                    String strOptString2 = jSONObjectOptJSONObject.optString("protocol");
                    String strOptString3 = jSONObjectOptJSONObject.optString("roaming_protocol");
                    String strOptString4 = jSONObjectOptJSONObject.optString("type");
                    String strOptString5 = jSONObjectOptJSONObject.optString("mvno_type");
                    String strOptString6 = jSONObjectOptJSONObject.optString("mvno_match_data");
                    int apnTypesBitmaskFromString = ApnSetting.getApnTypesBitmaskFromString(strOptString4);
                    if (TextUtils.equals(apnSetting.getApnName(), strOptString) && TextUtils.equals(ApnSetting.getProtocolStringFromInt(apnSetting.getProtocol()), strOptString2) && TextUtils.equals(ApnSetting.getProtocolStringFromInt(apnSetting.getRoamingProtocol()), strOptString3) && (apnSetting.getApnTypeBitmask() & apnTypesBitmaskFromString) == apnTypesBitmaskFromString && TextUtils.equals(ApnSetting.getMvnoTypeStringFromInt(apnSetting.getMvnoType()), strOptString5) && TextUtils.equals(apnSetting.getMvnoMatchData(), strOptString6)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLastCloudApn() {
        return this.mContext.getSharedPreferences("preference_cloud_apn", 0).getString("cloud_apn", "");
    }

    private void updateLastCloudApn(List list) {
        String string;
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("preference_cloud_apn", 0).edit();
        if (list != null && list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                sb.append(((JSONObject) it.next()).toString());
            }
            string = sb.toString();
        } else {
            string = "";
        }
        logd("updateLastCloudApn=".concat(string));
        editorEdit.putString("cloud_apn", string);
        editorEdit.apply();
    }

    private boolean getAlreadyUpdatedValue(String str) {
        return this.mContext.getSharedPreferences("preference_cloud_apn", 0).getBoolean("operator" + str, false);
    }

    private void setAlreadyUpdatedValue(String str, boolean z) {
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("preference_cloud_apn", 0).edit();
        editorEdit.putBoolean("operator" + str, z);
        editorEdit.apply();
    }

    private boolean isValidCloudApn(JSONObject jSONObject) {
        if (jSONObject == null) {
            return false;
        }
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("apns");
        JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("delete_apns");
        logd("insertApn =" + jSONArrayOptJSONArray);
        logd("deleteApn = " + jSONArrayOptJSONArray2);
        if ((jSONArrayOptJSONArray == null || jSONArrayOptJSONArray.length() == 0) && (jSONArrayOptJSONArray2 == null || jSONArrayOptJSONArray2.length() == 0)) {
            return false;
        }
        if (jSONArrayOptJSONArray == null) {
            return true;
        }
        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
            JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
            if (jSONObjectOptJSONObject == null) {
                return false;
            }
            String strOptString = jSONObjectOptJSONObject.optString("name");
            String strOptString2 = jSONObjectOptJSONObject.optString("apn");
            if (TextUtils.isEmpty(strOptString) || TextUtils.isEmpty(strOptString2)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d(LOG_TAG, str);
    }

    private void loge(String str, Exception exc) {
        Log.e(LOG_TAG, str, exc);
    }

    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("CloudApnController: ");
    }
}
