package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.statistics.utils.DeviceUtil;
import com.ot.pubsub.a.a;
import com.ot.pubsub.b.g;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import miui.telephony.DefaultSimManager;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class MiuiCloudDataManager {
    private static String LOG_TAG = "MiuiCloudDataManager";
    private static final Uri URI_CLOUD_ALL_DATA_NOTIFY = Uri.parse("content://com.android.settings.cloud.CloudSettings/cloud_all_data/notify");
    private static volatile MiuiCloudDataManager sInstance;
    private CloudDataHandler mBaseHandler;
    private CloudApnController mCloudApnController;
    private ContentObserver mCloudDataObserver;
    private CloudEccController mCloudEccListController;
    private CloudModemVolteController mCloudModemVolteController;
    private volatile PersistableBundle[] mConfigFromDefaultApp;
    private Context mContext;
    private HashMap mCloudVolteCarrierConfig = new HashMap();
    private final Object mLock = new Object();

    private boolean isValidVolteConfigValue(int i) {
        return i == 1 || i == 0;
    }

    private static boolean supportVolteClouldConfig() {
        return true;
    }

    public static MiuiCloudDataManager init(PersistableBundle[] persistableBundleArr) {
        if (sInstance == null && supportVolteClouldConfig()) {
            sInstance = new MiuiCloudDataManager(persistableBundleArr);
        }
        return sInstance;
    }

    public static MiuiCloudDataManager getInstance() {
        return sInstance;
    }

    private MiuiCloudDataManager(PersistableBundle[] persistableBundleArr) {
        this.mContext = null;
        this.mBaseHandler = null;
        this.mCloudApnController = null;
        this.mCloudEccListController = null;
        this.mCloudModemVolteController = null;
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread(LOG_TAG);
        handlerThread.start();
        this.mBaseHandler = new CloudDataHandler(handlerThread.getLooper());
        registerCloudDataObserver();
        if (CloudApnController.isUpdateCloudApnSupported()) {
            this.mCloudApnController = new CloudApnController(this.mContext, handlerThread);
        }
        CloudEccController cloudEccController = CloudEccController.getInstance();
        this.mCloudEccListController = cloudEccController;
        cloudEccController.init(this.mContext, handlerThread);
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && (DeviceUtil.isModemVolteEnabled() || MiuiPhoneUtils.isSupportVolteGlobalization())) {
            this.mCloudModemVolteController = CloudModemVolteController.getInstance();
        }
        this.mConfigFromDefaultApp = persistableBundleArr;
        this.mBaseHandler.sendEmptyMessage(1);
        Log.d(LOG_TAG, "init done");
    }

    class CloudDataHandler extends Handler {
        public CloudDataHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                MiuiCloudDataManager.this.initVolteCloudCarrierConfig();
                MiuiCloudDataManager.this.syncCloudData();
            } else {
                if (i != 2) {
                    return;
                }
                MiuiCloudDataManager.this.syncCloudData();
            }
        }
    }

    private void registerCloudDataObserver() {
        if (this.mCloudDataObserver != null) {
            return;
        }
        this.mCloudDataObserver = new ContentObserver(this.mBaseHandler) { // from class: com.android.phone.MiuiCloudDataManager.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                Log.d(MiuiCloudDataManager.LOG_TAG, "cloud data changed");
                if (MiuiCloudDataManager.this.mBaseHandler.hasMessages(2)) {
                    return;
                }
                MiuiCloudDataManager.this.mBaseHandler.sendEmptyMessage(2);
            }
        };
        this.mContext.getContentResolver().registerContentObserver(URI_CLOUD_ALL_DATA_NOTIFY, false, this.mCloudDataObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncCloudData() {
        syncVolteCloudCarrierConfig();
        CloudApnController cloudApnController = this.mCloudApnController;
        if (cloudApnController != null) {
            cloudApnController.updateCloudApn();
        }
        CloudEccController cloudEccController = this.mCloudEccListController;
        if (cloudEccController != null) {
            cloudEccController.updateCloudEccList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initVolteCloudCarrierConfig() {
        synchronized (this.mLock) {
            try {
                SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(g.e, 0);
                Set<String> stringSet = sharedPreferences.getStringSet("volte_enabled_plmn_list", null);
                if (stringSet != null && stringSet.size() > 0) {
                    Iterator<String> it = stringSet.iterator();
                    while (it.hasNext()) {
                        this.mCloudVolteCarrierConfig.put(it.next(), 1);
                    }
                }
                Set<String> stringSet2 = sharedPreferences.getStringSet("volte_disabled_plmn_list", null);
                if (stringSet2 != null && stringSet2.size() > 0) {
                    Iterator<String> it2 = stringSet2.iterator();
                    while (it2.hasNext()) {
                        this.mCloudVolteCarrierConfig.put(it2.next(), 0);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private boolean syncVolteCloudCarrierConfig() {
        try {
            List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(PhoneGlobals.getInstance().getContentResolver(), "volte_carrier_config");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                HashSet hashSet = new HashSet();
                HashSet hashSet2 = new HashSet();
                synchronized (this.mLock) {
                    try {
                        this.mCloudVolteCarrierConfig.clear();
                        Iterator it = cloudDataList.iterator();
                        while (it.hasNext()) {
                            String string = ((MiuiSettings.SettingsCloudData.CloudData) it.next()).toString();
                            if (!TextUtils.isEmpty(string)) {
                                JSONObject jSONObject = new JSONObject(string);
                                String strOptString = jSONObject.optString("plmn");
                                int iOptInt = jSONObject.optInt(a.p);
                                Log.d(LOG_TAG, "clould volte carrier config: plmn=" + strOptString + ", value=" + iOptInt);
                                if (!TextUtils.isEmpty(strOptString) && isValidVolteConfigValue(iOptInt)) {
                                    if (iOptInt == 1) {
                                        hashSet.add(strOptString);
                                    } else {
                                        hashSet2.add(strOptString);
                                    }
                                    this.mCloudVolteCarrierConfig.put(strOptString, Integer.valueOf(iOptInt));
                                }
                            }
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences(g.e, 0).edit();
                editorEdit.putStringSet("volte_enabled_plmn_list", hashSet);
                editorEdit.putStringSet("volte_disabled_plmn_list", hashSet2);
                editorEdit.apply();
                if (this.mConfigFromDefaultApp == null) {
                    Log.d(LOG_TAG, "syncVolteCloudCarrierConfig: mConfigFromDefaultApp is null, skip volte update");
                    return false;
                }
                for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                    int cloudVolteConfig = getCloudVolteConfig(PhoneProxy.getSimOperator(i));
                    if (isValidVolteConfigValue(cloudVolteConfig) && i < this.mConfigFromDefaultApp.length && this.mConfigFromDefaultApp[i] != null) {
                        boolean z = cloudVolteConfig == 1;
                        if (z != PhoneProxy.getVolteCarrierConfigValue(this.mConfigFromDefaultApp[i])) {
                            PhoneProxy.setVolteCarrierConfigValue(this.mConfigFromDefaultApp[i], z);
                            boolean iccRecordsLoaded = MiuiPhoneUtils.getIccRecordsLoaded(i);
                            if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
                                if (iccRecordsLoaded) {
                                    PhoneProxy.updateConfigForPhoneId(this.mContext, i, "LOADED");
                                }
                            } else if (iccRecordsLoaded && i == DefaultSimManager.getDefaultDataSlotId()) {
                                PhoneProxy.updateConfigForPhoneId(this.mContext, i, "LOADED");
                            }
                        }
                    }
                }
                return true;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public int getCloudVolteConfig(String str) {
        synchronized (this.mLock) {
            try {
                Integer num = (Integer) this.mCloudVolteCarrierConfig.get(str);
                if (num == null) {
                    return -1;
                }
                Log.d(LOG_TAG, "getCloudVolteConfig: plmn=" + str + ", value=" + num);
                return num.intValue();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (this.mCloudVolteCarrierConfig != null) {
            StringBuilder sb = new StringBuilder("MiuiCloudDataManager VolteCloudCarrierConfig: ");
            for (Map.Entry entry : this.mCloudVolteCarrierConfig.entrySet()) {
                sb.append(((String) entry.getKey()) + ":" + entry.getValue() + "  ");
            }
            printWriter.println(sb.toString());
        }
        CloudApnController cloudApnController = this.mCloudApnController;
        if (cloudApnController != null) {
            cloudApnController.dump(fileDescriptor, printWriter, strArr);
        }
        CloudEccController cloudEccController = this.mCloudEccListController;
        if (cloudEccController != null) {
            cloudEccController.dump(fileDescriptor, printWriter, strArr);
        }
    }
}
