package com.android.phone.signalMap;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Icon;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Phone;
import com.android.phone.CloudCellDbHelper;
import com.android.phone.FiveGCloudUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkModeRecognition$$ExternalSyntheticLambda0;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapNOMLController {
    private static SignalMapNOMLController mSignalMapNOMLController;
    private List[] mCityCoordinateSet;
    private Context mContext;
    private BroadcastReceiver mDLReceiver;
    private CloudCellDbHelper mDbHelper;
    private final BaseEvent.IEventListener mDisplayListener;
    private int mDownloadRetryCnt;
    private LocationListener mGetCurrentLocationListener;
    private GnssStatus.Callback mGnssCallback;
    public SignalMapNOMLHandler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mIsLastScreenOn;
    private boolean mIsNotificationShow;
    private boolean mIsSatelliteOn;
    private long[] mLastCidSearchTime;
    private long mLastNotifyTime;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private boolean mLocationRequestOn;
    private SignalMapCloudController mSignalMapCloudController;
    private Map mStats;
    private int mStrongCount;
    private TelephonyCallBack mTelephonyCallBack;
    private HandlerThread mWorkThread;
    private static final HashSet mDriveCitys = new HashSet(Arrays.asList("Turpan", "Bayingol", "Haixi", "Jiuquan", "Ngari", "Nagqu", "Yushu", "Hotan"));
    private static final HashSet mHikingCitys = new HashSet(Arrays.asList("Altay", "Ili", "Ngari", "Baishan", "Yanbian", "Baoji", "Xi'an", "Aba", "Ya'an", "Ganzi", "Nyingchi"));
    private static final HashSet mOceanCitys = new HashSet(Arrays.asList("Dalian", "Yingkou", "Jinzhou", "Panjin", "Dandong", "Huludao", "Tianjin", "Tangshan", "Cangzhou", "Qinhuangdao", "Qingdao", "Yantai", "Dongying", "Weihai", "Rizhao", "Nantong", "Yancheng", "Lianyungang", "Shanghai", "Ningbo", "Wenzhou", "Jiaxing", "Zhoushan", "Taizhou", "Fuzhou", "Xiamen", "Quanzhou", "Zhangzhou", "Ningde", "Putian", "Guangzhou", "Shenzhen", "Dongguan", "Huizhou", "Zhanjiang", "Jiangmen", "Shantou", "Maoming", "Yangjiang", "Chaozhou", "Shanwei", "Haikou", "Ganzhou", "Sanya", "Zhuhai", "Zhongshan", "Hong", "Macao"));
    private static boolean DEBUG = false;
    private static String mLocation = "";
    private static String mTopOneRegion = "";
    private static String mCity = "";
    private static String mDistrict = "";

    private SignalMapNOMLController() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mLastCidSearchTime = new long[i];
        this.mCityCoordinateSet = new ArrayList[i];
        this.mLocationRequestOn = false;
        this.mLastNotifyTime = 0L;
        this.mStrongCount = 0;
        this.mIsNotificationShow = false;
        this.mIsLastScreenOn = false;
        this.mIsSatelliteOn = false;
        this.mStats = new HashMap();
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapNOMLController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if ("signal.map.NOML.debug".equals(action)) {
                    SignalMapNOMLController.DEBUG = true;
                    return;
                }
                if ("com.android.phone.SignalMapNOMLController.debug".equals(action)) {
                    SignalMapNOMLController.this.mHandler.obtainMessage(100, intent).sendToTarget();
                } else if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    SignalMapNOMLController.this.mHandler.obtainMessage(5, intent).sendToTarget();
                } else if (SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE.equals(action)) {
                    SignalMapNOMLController.this.mHandler.obtainMessage(8, intent).sendToTarget();
                }
            }
        };
        this.mLocationListener = new LocationListener() { // from class: com.android.phone.signalMap.SignalMapNOMLController.2
            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                SignalMapNOMLController.this.handleLocationChanged(location);
            }
        };
        this.mGetCurrentLocationListener = new LocationListener() { // from class: com.android.phone.signalMap.SignalMapNOMLController.3
            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                SignalMapNOMLController.this.handleLocationChanged(location);
            }
        };
        this.mGnssCallback = new GnssStatus.Callback() { // from class: com.android.phone.signalMap.SignalMapNOMLController.4
            @Override // android.location.GnssStatus.Callback
            public void onSatelliteStatusChanged(GnssStatus gnssStatus) {
                SignalMapNOMLController.this.mStrongCount = 0;
                for (int i2 = 0; i2 < gnssStatus.getSatelliteCount(); i2++) {
                    float cn0DbHz = gnssStatus.getCn0DbHz(i2);
                    SignalMapNOMLController.this.logD("onSatelliteStatusChanged: cn0 = " + cn0DbHz);
                    if (cn0DbHz > 30.0f) {
                        SignalMapNOMLController.this.mStrongCount++;
                    }
                }
                SignalMapNOMLController.this.logD("onSatelliteStatusChanged: mStrongCount = " + SignalMapNOMLController.this.mStrongCount);
            }
        };
        this.mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.signalMap.SignalMapNOMLController.6
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public synchronized void onEvent(BaseEvent baseEvent) {
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (displayEvent.isDefaultDisplay()) {
                        boolean zIsScreenOn = displayEvent.isScreenOn();
                        SignalMapNOMLController signalMapNOMLController = SignalMapNOMLController.this;
                        if (zIsScreenOn) {
                            boolean z = signalMapNOMLController.mIsLastScreenOn;
                            SignalMapNOMLController signalMapNOMLController2 = SignalMapNOMLController.this;
                            if (z) {
                                signalMapNOMLController2.log("onDisplayChanged last state is still on, ignore.");
                                return;
                            }
                            signalMapNOMLController2.mIsLastScreenOn = true;
                            if (SignalMapNOMLController.this.mIsNotificationShow) {
                                SignalMapNOMLController.this.mHandler.removeMessages(10);
                                SignalMapNOMLController.this.mHandler.sendEmptyMessageDelayed(10, 120000L);
                            }
                        } else {
                            signalMapNOMLController.mIsLastScreenOn = false;
                        }
                    }
                }
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("SignalMapNOMLController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new SignalMapNOMLHandler(this.mWorkThread.getLooper());
        this.mSignalMapCloudController = SignalMapCloudController.getInstance();
        this.mHandler.obtainMessage(1).sendToTarget();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.signalMap.SignalMapNOMLController$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            SignalMapNOMLHandler signalMapNOMLHandler = this.mHandler;
            if (signalMapNOMLHandler == null) {
                log("handler is null");
            } else {
                signalMapNOMLHandler.obtainMessage(7).sendToTarget();
            }
        }
    }

    public static synchronized SignalMapNOMLController getInstance() {
        try {
            if (mSignalMapNOMLController == null) {
                mSignalMapNOMLController = new SignalMapNOMLController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mSignalMapNOMLController;
    }

    public class SignalMapNOMLHandler extends Handler {
        SignalMapNOMLHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            SignalMapNOMLController.this.logD("msg.what = " + message.what);
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        SignalMapNOMLController.this.init();
                        return;
                    case 2:
                        SignalMapNOMLController.this.processRegionUpdate();
                        return;
                    case 3:
                        SignalMapNOMLController.this.processCellLocationChanged(message.arg1);
                        return;
                    case 4:
                        SignalMapNOMLController.this.turnoffLocationUpdate();
                        return;
                    case 5:
                        Intent intent = (Intent) message.obj;
                        String stringExtra = intent.getStringExtra("ss");
                        int intExtra = intent.getIntExtra("phone", -1);
                        SignalMapNOMLController.this.log("EVENT_SIM_STATE_CHANGED = phoneId" + intExtra + ", simState=" + stringExtra + ", satState=" + SignalMapNOMLController.this.mIsSatelliteOn);
                        if (SignalMapNOMLController.this.mIsSatelliteOn) {
                            return;
                        }
                        if ("ABSENT".equals(stringExtra) || "LOADED".equals(stringExtra)) {
                            boolean zHasSupportSatelliteSIM = SignalMapNOMLController.this.hasSupportSatelliteSIM();
                            SignalMapNOMLController signalMapNOMLController = SignalMapNOMLController.this;
                            if (zHasSupportSatelliteSIM) {
                                signalMapNOMLController.registerTelephonyCallBack();
                                return;
                            }
                            signalMapNOMLController.cancelSatConNotification();
                            SignalMapNOMLController.this.turnoffLocationUpdate();
                            SignalMapNOMLController.this.unregisterTelephonyCallBack();
                            return;
                        }
                        return;
                    case 6:
                        SignalMapNOMLController.this.log("EVENT_CLOUD_DOWNLOADED");
                        try {
                            if (SignalMapNOMLController.this.checkDownloadStatus(message)) {
                                SignalMapNOMLController.this.mDbHelper.readAndLoadNOMLSignalMap(true);
                                return;
                            }
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    case 7:
                        SignalMapNOMLController.this.updateSignalMapNOMLCloudStatus();
                        return;
                    case 8:
                        SignalMapNOMLController.this.mIsSatelliteOn = ((Intent) message.obj).getBooleanExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, false);
                        boolean z = SignalMapNOMLController.this.mIsSatelliteOn;
                        SignalMapNOMLController signalMapNOMLController2 = SignalMapNOMLController.this;
                        if (z) {
                            signalMapNOMLController2.cancelSatConNotification();
                            SignalMapNOMLController.this.turnoffLocationUpdate();
                            SignalMapNOMLController.this.unregisterTelephonyCallBack();
                            return;
                        } else {
                            if (signalMapNOMLController2.hasSupportSatelliteSIM()) {
                                SignalMapNOMLController.this.registerTelephonyCallBack();
                                return;
                            }
                            return;
                        }
                    case 9:
                        boolean z2 = SignalMapNOMLController.this.mLocationRequestOn;
                        SignalMapNOMLController signalMapNOMLController3 = SignalMapNOMLController.this;
                        if (z2) {
                            signalMapNOMLController3.logD("EVENT_START_LOCATION_UPDATE location update already, return");
                            return;
                        } else {
                            signalMapNOMLController3.startLocationUpdate();
                            return;
                        }
                    case 10:
                        break;
                    case 11:
                        if (SignalMapNOMLController.this.mLocationRequestOn) {
                            SignalMapNOMLController.this.requestGetCurrentLocation();
                            SignalMapNOMLHandler signalMapNOMLHandler = SignalMapNOMLController.this.mHandler;
                            signalMapNOMLHandler.sendMessageDelayed(signalMapNOMLHandler.obtainMessage(11), 300000L);
                            return;
                        }
                        return;
                    default:
                        return;
                }
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    if (SignalMapNOMLController.this.isInService(i2) && SignalMapNOMLController.this.getMiuiLevel(i2) >= 4) {
                        SignalMapNOMLController.this.logD("EVENT_CANCEL_NOTIFICATION");
                        SignalMapNOMLController.this.cancelSatConNotification();
                        return;
                    }
                }
                return;
            }
            Intent intent2 = (Intent) message.obj;
            String stringExtra2 = intent2.getStringExtra("url");
            int intExtra2 = intent2.getIntExtra("version", 0);
            if (!TextUtils.isEmpty(stringExtra2)) {
                SignalMapNOMLController.this.downloadCloudFile(stringExtra2, intExtra2, true);
            }
            String stringExtra3 = intent2.getStringExtra("cid");
            Set nOMLCidByTypeFromDb = SignalMapNOMLController.this.getNOMLCidByTypeFromDb(intent2.getIntExtra("nomlType", 0));
            SignalMapNOMLController.this.logD("EVENT_TEST: , cidList = " + nOMLCidByTypeFromDb);
            SignalMapNOMLController.mTopOneRegion = intent2.getStringExtra("TopOneRegion");
            SignalMapNOMLController.this.logD("EVENT_TEST: , mTopOneRegion = " + SignalMapNOMLController.mTopOneRegion + ", regionRes = " + SignalMapNOMLController.this.regionEncode(SignalMapNOMLController.mTopOneRegion));
            String stringExtra4 = intent2.getStringExtra("NOMLCity");
            SignalMapNOMLController.mCity = stringExtra4;
            SignalMapNOMLController.mDriveCitys.add(stringExtra4);
            SignalMapNOMLController.mHikingCitys.add(stringExtra4);
            SignalMapNOMLController.mOceanCitys.add(stringExtra4);
            List nOMLGPSByCidFromDb = SignalMapNOMLController.this.getNOMLGPSByCidFromDb(stringExtra3);
            SignalMapNOMLController.this.logD("EVENT_TEST: , GPSData = " + nOMLGPSByCidFromDb);
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("com.android.phone.SignalMapNOMLController.debug");
        intentFilter.addAction("signal.map.NOML.debug");
        intentFilter.addAction(SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE);
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() throws Throwable {
        this.mLocationManager = (LocationManager) this.mContext.getSystemService("location");
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener, this.mHandler);
        registerReceiver();
        if (hasSupportSatelliteSIM()) {
            registerTelephonyCallBack();
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mCityCoordinateSet[i] = null;
            this.mLastCidSearchTime[i] = 0;
        }
        processLoadDefaultFile();
    }

    private void processLoadDefaultFile() throws Throwable {
        this.mDbHelper = CloudCellDbHelper.getInstance(this.mContext);
        try {
            int i = Settings.System.getInt(this.mContext.getContentResolver(), "clear_signalmap_db", 0);
            logD("isClearDb = " + i);
            CloudCellDbHelper cloudCellDbHelper = this.mDbHelper;
            if (i == 0) {
                cloudCellDbHelper.readAndLoadDefaultNOMLSignalMap();
            } else {
                cloudCellDbHelper.readAndLoadNOMLSignalMap(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRegionUpdate() {
        mCity = this.mSignalMapCloudController.getCity();
        logD("mCity = " + mCity);
        mDistrict = this.mSignalMapCloudController.getDistrict();
        logD("mDistrict = " + mDistrict);
        if (TextUtils.isEmpty(mCity) || TextUtils.isEmpty(mDistrict)) {
            return;
        }
        mLocation = mCity.replaceAll("[^a-zA-Z]", "") + "_" + mDistrict.replaceAll("[^a-zA-Z]", "");
        updateLatestRegions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processCellLocationChanged(int i) {
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                String ddsLteNrCid = getDdsLteNrCid(i);
                log("processCellLocationChanged curCid:" + ddsLteNrCid + ", phoneId:" + i);
                if (!TextUtils.isEmpty(ddsLteNrCid) && checkCidExistence(ddsLteNrCid)) {
                    this.mLastCidSearchTime[i] = System.currentTimeMillis();
                    this.mCityCoordinateSet[i] = getNOMLGPSByCidFromDb(ddsLteNrCid);
                    this.mHandler.sendEmptyMessage(9);
                    int nOMLTypeByCidFromDb = getNOMLTypeByCidFromDb(ddsLteNrCid);
                    if (nOMLTypeByCidFromDb == 1) {
                        recordCount("driveCellHit");
                    } else if (nOMLTypeByCidFromDb == 2) {
                        recordCount("hikingCellHit");
                    } else if (nOMLTypeByCidFromDb == 3) {
                        recordCount("oceanCellHit");
                    }
                } else {
                    this.mCityCoordinateSet[i] = null;
                    if (this.mLocationRequestOn) {
                        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                            if (i2 != i && this.mCityCoordinateSet[i2] == null) {
                                this.mHandler.sendEmptyMessage(4);
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean checkCidExistence(String str) {
        if (this.mDbHelper == null) {
            return false;
        }
        if (getSensorDriveState() == 10) {
            return this.mDbHelper.isNOMLCidExistsByType(str, String.valueOf(1));
        }
        int sensorHikingState = getSensorHikingState();
        CloudCellDbHelper cloudCellDbHelper = this.mDbHelper;
        if (sensorHikingState == 1) {
            return cloudCellDbHelper.isNOMLCidExistsByType(str, String.valueOf(2));
        }
        return cloudCellDbHelper.isNOMLCidExistsByType(str, String.valueOf(3));
    }

    public String getDdsLteNrCid(int i) {
        Phone phone;
        long nci;
        if (!MiuiPhoneUtils.isValidatePhoneId(i) || (phone = MiuiPhoneUtils.getPhone(i)) == null || phone.getCurrentCellIdentity() == null) {
            return null;
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        if (currentCellIdentity instanceof CellIdentityLte) {
            nci = ((CellIdentityLte) currentCellIdentity).getCi();
        } else if (currentCellIdentity instanceof CellIdentityNr) {
            nci = ((CellIdentityNr) currentCellIdentity).getNci();
        } else {
            return "2G3G";
        }
        return String.valueOf(nci);
    }

    private void updateLatestRegions() {
        String strUpdateRegions;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        String string = defaultSharedPreferences.getString("latest_regions", "");
        logD("spRegions = " + string);
        String strUpdateTopRegion = updateTopRegion(string);
        mTopOneRegion = strUpdateTopRegion;
        if (TextUtils.isEmpty(strUpdateTopRegion)) {
            mTopOneRegion = mLocation;
        }
        logD("updateLatestRegions, mTopOneRegion = " + mTopOneRegion);
        if (!TextUtils.isEmpty(string)) {
            strUpdateRegions = updateRegions(string);
        } else {
            strUpdateRegions = mLocation;
        }
        editorEdit.putString("latest_regions", strUpdateRegions).commit();
    }

    private String updateRegions(String str) {
        if (new ArrayList(Arrays.asList(str.split(","))).size() >= 30) {
            str = str.substring(str.indexOf(",") + 1);
        }
        String str2 = str + "," + mLocation;
        logD("res = ".concat(str2));
        return str2;
    }

    private String updateTopRegion(String str) {
        String str2 = "";
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        HashMap map = new HashMap();
        int iIntValue = 0;
        for (String str3 : str.split(",")) {
            Integer num = (Integer) map.get(str3);
            if (num == null) {
                map.put(str3, 1);
                if (iIntValue < 1) {
                    str2 = str3;
                    iIntValue = 1;
                }
            } else {
                map.put(str3, Integer.valueOf(num.intValue() + 1));
                if (iIntValue < num.intValue() + 1) {
                    iIntValue = num.intValue() + 1;
                    str2 = str3;
                }
            }
        }
        return str2;
    }

    public String getTopOneRegion() {
        logD("mTopOneRegion: " + mTopOneRegion);
        return TextUtils.isEmpty(mTopOneRegion) ? "" : regionEncode(mTopOneRegion);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String regionEncode(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String[] strArrSplit = str.toLowerCase().replaceAll("[^a-z_]", "").split("_");
        if (strArrSplit.length != 2) {
            return "";
        }
        return encodeString(strArrSplit[0]) + "_" + encodeString(strArrSplit[1]);
    }

    private String encodeString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            int i = c - '`';
            sb.append(i);
            if (i >= 10) {
                sb.append('0');
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set getNOMLCidByTypeFromDb(int i) {
        if (i < 1 || i > 3) {
            logD("type is invalid!");
            return null;
        }
        Cursor nOMLCidByType = this.mDbHelper.getNOMLCidByType(String.valueOf(i));
        HashSet hashSet = new HashSet();
        if (nOMLCidByType != null) {
            while (nOMLCidByType.moveToNext()) {
                hashSet.add(getCursorString(nOMLCidByType, "cid"));
            }
        }
        logD("cidList: " + hashSet);
        if (nOMLCidByType != null) {
            nOMLCidByType.close();
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List getNOMLGPSByCidFromDb(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            log("cid is empty!");
            return null;
        }
        Cursor nOMLNameByCid = this.mDbHelper.getNOMLNameByCid(str);
        String cursorString = "";
        if (nOMLNameByCid != null) {
            while (nOMLNameByCid.moveToNext()) {
                cursorString = getCursorString(nOMLNameByCid, "nomlname");
            }
        }
        logD("zoneName: " + cursorString);
        List arrayList = new ArrayList();
        if (!TextUtils.isEmpty(cursorString)) {
            arrayList = this.mDbHelper.getGPSDataByZone(cursorString);
            logD("GPSData: " + arrayList);
        }
        if (nOMLNameByCid != null) {
            nOMLNameByCid.close();
        }
        return arrayList;
    }

    private int getNOMLTypeByCidFromDb(String str) {
        int iIntValue = -1;
        if (TextUtils.isEmpty(str)) {
            log("cid is empty!");
            return -1;
        }
        Cursor nOMLNameByCid = this.mDbHelper.getNOMLNameByCid(str);
        if (nOMLNameByCid != null) {
            while (nOMLNameByCid.moveToNext()) {
                iIntValue = Integer.valueOf(getCursorString(nOMLNameByCid, "nomltype")).intValue();
            }
        }
        logD("nomlType: " + iIntValue);
        return iIntValue;
    }

    private String getCursorString(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndex(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("registerTelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void unregisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            log("unregisterTelephonyCallBack");
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            SignalMapNOMLController.this.logD("onCellLocationChanged phoneId = " + i + ", city = " + SignalMapNOMLController.mCity + ", top region = " + SignalMapNOMLController.mTopOneRegion);
            if (!SignalMapNOMLController.this.isMoreThanOneday() || TextUtils.isEmpty(SignalMapNOMLController.mCity) || SignalMapNOMLController.mLocation == null || SignalMapNOMLController.mLocation.equals(SignalMapNOMLController.mTopOneRegion) || !SignalMapNOMLController.this.isMoreThanOneMinutes(i) || !SignalMapNOMLController.this.isCityMatchSensor()) {
                return;
            }
            SignalMapNOMLController.this.mHandler.obtainMessage(3, i, 0, cellLocation).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCityMatchSensor() {
        if (getSensorDriveState() == 10) {
            return mDriveCitys.contains(mCity);
        }
        if (getSensorHikingState() == 1) {
            return mHikingCitys.contains(mCity);
        }
        return mOceanCitys.contains(mCity);
    }

    public boolean isPointInPolygon(Point2D point2D, List list) {
        int size = list.size();
        int i = 0;
        int i2 = 0;
        while (i < size) {
            Point2D point2D2 = (Point2D) list.get(i);
            i++;
            Point2D point2D3 = (Point2D) list.get(i % size);
            if (point2D2.y != point2D3.y && point2D.y.doubleValue() >= Math.min(point2D2.y.doubleValue(), point2D3.y.doubleValue()) && point2D.y.doubleValue() < Math.max(point2D2.y.doubleValue(), point2D3.y.doubleValue())) {
                try {
                    double dDoubleValue = (((point2D.y.doubleValue() - point2D2.y.doubleValue()) * (point2D3.x.doubleValue() - point2D2.x.doubleValue())) / (point2D3.y.doubleValue() - point2D2.y.doubleValue())) + point2D2.x.doubleValue();
                    if (dDoubleValue > point2D.x.doubleValue()) {
                        i2++;
                    } else if (dDoubleValue == point2D.x.doubleValue()) {
                        return true;
                    }
                } catch (ArithmeticException unused) {
                    return false;
                }
            }
        }
        return i2 % 2 == 1;
    }

    private void showSatConNotification() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        Intent intent = new Intent();
        intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MiuiSatelliteSettings");
        intent.addFlags(268435456);
        intent.putExtra("nomlMessage", "nomlNotification");
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager != null && packageManager.queryIntentActivities(intent, 0).size() < 1) {
            log("activity NOT exist");
        }
        notificationManager.createNotificationChannel(new NotificationChannel("depopulated_zone_satellite", this.mContext.getString(R.string.depopulated_zone_satellite_notification), 4));
        Bundle bundle = new Bundle();
        bundle.putParcelable("miui.appIcon", Icon.createWithResource(this.mContext, R.drawable.settings_satellite));
        Notification notificationBuild = new Notification.Builder(this.mContext, "depopulated_zone_satellite").setOngoing(true).setSmallIcon(R.drawable.settings_satellite).addExtras(bundle).setContentText(this.mContext.getString(R.string.satellite_notification_text)).setContentTitle(this.mContext.getString(R.string.satellite_notification_title)).setContentIntent(PendingIntent.getActivity(this.mContext, 0, intent, 201326592)).setAutoCancel(true).setChannelId("depopulated_zone_satellite").build();
        log("start notification");
        this.mLastNotifyTime = System.currentTimeMillis();
        notificationManager.notify(1, notificationBuild);
        this.mIsNotificationShow = true;
        recordCount("satelliteNotify");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelSatConNotification() {
        this.mHandler.removeMessages(10);
        ((NotificationManager) this.mContext.getSystemService("notification")).cancel(1);
        this.mIsNotificationShow = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLocationUpdate() {
        log("startLocationUpdate");
        LocationManager locationManager = this.mLocationManager;
        if (locationManager != null) {
            String str = "network";
            if (!locationManager.isProviderEnabled("network")) {
                str = "passive";
                if (!this.mLocationManager.isProviderEnabled("passive")) {
                    str = null;
                }
            }
            String str2 = str;
            if (str2 != null) {
                this.mLocationRequestOn = true;
                this.mLocationManager.requestLocationUpdates(str2, 2000L, 10.0f, this.mLocationListener);
            }
            if (this.mLocationManager.isProviderEnabled("gps")) {
                this.mLocationRequestOn = true;
                this.mStrongCount = 0;
                this.mLocationManager.registerGnssStatusCallback(this.mGnssCallback, this.mHandler);
                this.mHandler.sendEmptyMessage(11);
            }
            if (this.mLocationRequestOn) {
                SignalMapNOMLHandler signalMapNOMLHandler = this.mHandler;
                signalMapNOMLHandler.sendMessageDelayed(signalMapNOMLHandler.obtainMessage(4), 900000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestGetCurrentLocation() {
        LocationManager locationManager = this.mLocationManager;
        if (locationManager == null || !locationManager.isProviderEnabled("gps")) {
            return;
        }
        logD("requestGetCurrentLocation");
        LocationRequest locationRequestBuild = new LocationRequest.Builder(1000L).setMinUpdateIntervalMillis(1000L).build();
        LocationManager locationManager2 = this.mLocationManager;
        final SignalMapNOMLHandler signalMapNOMLHandler = this.mHandler;
        Objects.requireNonNull(signalMapNOMLHandler);
        Executor executor = new Executor() { // from class: com.android.phone.signalMap.SignalMapNOMLController$$ExternalSyntheticLambda1
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                signalMapNOMLHandler.post(runnable);
            }
        };
        LocationListener locationListener = this.mGetCurrentLocationListener;
        Objects.requireNonNull(locationListener);
        locationManager2.getCurrentLocation("gps", locationRequestBuild, null, executor, new NetworkModeRecognition$$ExternalSyntheticLambda0(locationListener));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLocationChanged(Location location) {
        List list;
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Point2D point2D = new Point2D(longitude, latitude);
            logD("handleLocationChanged:, provider=" + location.getProvider() + ", accuracy=" + location.getAccuracy() + "longitude=" + longitude + ", latitude=" + latitude);
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (isInService(i) && getMiuiLevel(i) >= 4) {
                    logD("handleLocationChanged: cellular is great, return");
                    return;
                }
            }
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                if (this.mStrongCount >= 4 && (list = this.mCityCoordinateSet[i2]) != null && isPointInPolygon(point2D, list)) {
                    log("isPointInPolygon");
                    showSatConNotification();
                    turnoffLocationUpdate();
                    enterDepopulatedZoneNotify();
                    return;
                }
            }
            return;
        }
        logD("handleLocationChanged location is null, do nothing");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInService(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || phone.getServiceState() == null) {
            logD("phone is null, phoneId = " + i);
            return false;
        }
        ServiceState serviceState = phone.getServiceState();
        boolean z = serviceState.getVoiceRegState() == 0 || serviceState.getDataRegState() == 0;
        logD("isInService: phoneId = " + i + ", result = " + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMiuiLevel(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || phone.getSignalStrength() == null) {
            logD("phone is null, phoneId = " + i);
            return -1;
        }
        int miuiLevel = phone.getSignalStrength().getMiuiLevel();
        logD("getMiuiLevel: phoneId = " + i + ", miuiLevel = " + miuiLevel);
        return miuiLevel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void turnoffLocationUpdate() {
        this.mHandler.removeMessages(11);
        this.mHandler.removeMessages(4);
        if (this.mLocationManager != null) {
            log("turnoffLocationUpdater");
            this.mLocationManager.unregisterGnssStatusCallback(this.mGnssCallback);
            this.mLocationManager.removeUpdates(this.mLocationListener);
            this.mLocationManager.removeUpdates(this.mGetCurrentLocationListener);
            this.mLocationRequestOn = false;
            this.mStrongCount = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasSupportSatelliteSIM() {
        return TelephonyManagerEx.getDefault().isSupportSatelliteByCarrier();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMoreThanOneday() {
        if (this.mLastNotifyTime == 0) {
            return true;
        }
        if ((this.mLastNotifyTime / 1000) + 86400 >= System.currentTimeMillis() / 1000) {
            return false;
        }
        log("isMoreThanOneday");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMoreThanOneMinutes(int i) {
        if (this.mLastCidSearchTime[i] == 0) {
            return true;
        }
        if (this.mLastCidSearchTime[i] + RelayUtils.Relay.RETRY_DELAY_IN_MILLIS >= System.currentTimeMillis()) {
            return false;
        }
        log("isMoreThanOneMinutes phoneId=" + i);
        return true;
    }

    public synchronized void updateSignalMapNOMLCloudStatus() {
        try {
            try {
                List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "SignalMapNOMLCellList");
                if (cloudDataList == null || cloudDataList.size() == 0) {
                    log("CloudCell cloudList is empty");
                } else {
                    for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                        if (cloudData != null) {
                            String string = cloudData.getString("Model", "NA");
                            String string2 = cloudData.getString("SimOperator", "NA");
                            String string3 = cloudData.getString("cell_url", "");
                            int i = cloudData.getInt("cell_ver", 0);
                            int i2 = cloudData.getInt("ForceUpdate", 0);
                            if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2) && !TextUtils.isEmpty(string3)) {
                                downloadCloudFile(string3, i, i2 == 1);
                            }
                            log("updateSignalMapNOMLCloudStatus data=" + cloudData);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadCloudFile(String str, int i, boolean z) {
        long intPreference = PreferenceUtils.getInstance().getIntPreference("nomlcell_version");
        if (!z && i <= intPreference) {
            log("invalid cloud version for cell data");
            return;
        }
        try {
            DownloadManager downloadManager = (DownloadManager) this.mContext.getSystemService("download");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
            request.setAllowedNetworkTypes(2);
            request.setDestinationInExternalFilesDir(this.mContext, "CloudCell", getSignalMapNOMLZipName(i));
            request.setNotificationVisibility(2);
            request.setVisibleInDownloadsUi(false);
            request.setAllowedOverMetered(true);
            request.setBypassRecommendedSizeLimit(true);
            long jEnqueue = downloadManager.enqueue(request);
            log("download cloud file, id = " + jEnqueue);
            registerDownloadCompleteReceiver(i);
            downloadManager.forceDownload(new long[]{jEnqueue});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkDownloadStatus(Message message) {
        int i;
        File file;
        File parentFile;
        File[] fileArrListFiles;
        DownloadManager downloadManager = (DownloadManager) this.mContext.getSystemService("download");
        DownloadManager.Query query = new DownloadManager.Query();
        long jLongValue = ((Long) message.obj).longValue();
        boolean z = true;
        boolean z2 = false;
        try {
            Cursor cursorQuery = downloadManager.query(query.setFilterById(jLongValue));
            try {
                if (cursorQuery == null) {
                    log("null download cursor");
                    if (cursorQuery == null) {
                        return false;
                    }
                    cursorQuery.close();
                    return false;
                }
                int i2 = cursorQuery.moveToFirst() ? cursorQuery.getInt(cursorQuery.getColumnIndex(a.J)) : 1;
                log("checkDownloadStatus status:  " + i2 + ", version: " + message.arg2);
                if (i2 == 8) {
                    this.mDownloadRetryCnt = 0;
                    int i3 = message.arg2;
                    String string = cursorQuery.getString(cursorQuery.getColumnIndex("local_uri"));
                    if (!TextUtils.isEmpty(string) || !string.startsWith("file://")) {
                        file = new File(Uri.parse(string).getPath());
                    } else {
                        file = new File(this.mContext.getExternalFilesDir("CloudCell") + File.separator + getSignalMapNOMLZipName(i3));
                    }
                    if (extractCellZip(file, CloudCellDbHelper.getCellDbDir(this.mContext)) && (parentFile = file.getParentFile()) != null && (fileArrListFiles = parentFile.listFiles()) != null) {
                        for (File file2 : fileArrListFiles) {
                            log(file2 + " deleted:" + file2.delete());
                        }
                    }
                    unregisterDownloadCompleteReceiver();
                } else {
                    if (i2 == 16 && (i = this.mDownloadRetryCnt) < 3) {
                        this.mDownloadRetryCnt = i + 1;
                        log("retry to download retry:" + this.mDownloadRetryCnt);
                        downloadManager.forceDownload(new long[]{jLongValue});
                    } else if (i2 == 16 && this.mDownloadRetryCnt >= 3) {
                        log("download failed too many times");
                        unregisterDownloadCompleteReceiver();
                    }
                    z = false;
                }
                try {
                    cursorQuery.close();
                    return z;
                } catch (Exception e) {
                    e = e;
                    z2 = z;
                    log("check cell file download status error." + e);
                    e.printStackTrace();
                    return z2;
                }
            } catch (Throwable th) {
                if (cursorQuery != null) {
                    try {
                        cursorQuery.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            log("check cell file download status error." + e);
            e.printStackTrace();
            return z2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:71:0x00be A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private boolean extractCellZip(File file, String str) {
        File[] fileArrListFiles;
        try {
            ZipFile zipFile = new ZipFile(file);
            try {
                Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
                if (enumerationEntries.hasMoreElements()) {
                    ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
                    if (zipEntryNextElement.isDirectory()) {
                        log("invalid zip file");
                    } else {
                        File file2 = new File(str + zipEntryNextElement.getName());
                        if (!file2.getParentFile().exists()) {
                            file2.getParentFile().mkdirs();
                        } else {
                            File parentFile = file2.getParentFile();
                            if (parentFile != null && (fileArrListFiles = parentFile.listFiles()) != null) {
                                for (File file3 : fileArrListFiles) {
                                    String name = file3.getName();
                                    if (name != null && name.matches("blackcelldb_noml_\\d+\\.json")) {
                                        log(file3 + " deleted:" + file3.delete());
                                    }
                                }
                            }
                        }
                        try {
                            InputStream inputStream = zipFile.getInputStream(zipEntryNextElement);
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                                try {
                                    byte[] bArr = new byte[8192];
                                    while (true) {
                                        int i = inputStream.read(bArr);
                                        if (i == -1) {
                                            break;
                                        }
                                        fileOutputStream.write(bArr, 0, i);
                                        if (inputStream != null) {
                                            try {
                                                inputStream.close();
                                            } catch (Throwable th) {
                                                th.addSuppressed(th);
                                            }
                                        }
                                        throw th;
                                    }
                                    fileOutputStream.close();
                                    inputStream.close();
                                    log("Done extracting " + file + " to: " + str);
                                    zipFile.close();
                                    return true;
                                } catch (Throwable th2) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Throwable th3) {
                                        th2.addSuppressed(th3);
                                    }
                                    throw th2;
                                }
                            } catch (Throwable th4) {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                throw th4;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    e.printStackTrace();
                    return false;
                }
                zipFile.close();
            } catch (Throwable th5) {
                try {
                    zipFile.close();
                } catch (Throwable th6) {
                    th5.addSuppressed(th6);
                }
                throw th5;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return false;
    }

    private String getSignalMapNOMLZipName(int i) {
        return "blackcelldb_noml_" + i + ".zip";
    }

    private void registerDownloadCompleteReceiver(final int i) {
        if (this.mDLReceiver == null) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapNOMLController.5
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    long longExtra = intent.getLongExtra("extra_download_id", -1L);
                    SignalMapNOMLController.this.log(intent.getAction() + " id:" + longExtra);
                    SignalMapNOMLController.this.mHandler.obtainMessage(6, 0, i, Long.valueOf(longExtra)).sendToTarget();
                }
            };
            this.mDLReceiver = broadcastReceiver;
            this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
        }
    }

    private void unregisterDownloadCompleteReceiver() {
        BroadcastReceiver broadcastReceiver = this.mDLReceiver;
        if (broadcastReceiver != null) {
            this.mContext.unregisterReceiver(broadcastReceiver);
            this.mDLReceiver = null;
        }
    }

    private int getSensorHikingState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_state_hiking", 0);
    }

    private int getSensorDriveState() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "sensor_drive_state", 0);
    }

    public void enterDepopulatedZoneNotify() {
        log("enterDepopulatedZoneNotify");
        Intent intent = new Intent();
        intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
        intent.setAction("com.android.phone.intent.action.ENTER_DEPOPULATED_ZONE");
        this.mContext.sendBroadcast(intent);
    }

    private void recordCount(String str) {
        Integer num = (Integer) this.mStats.get(str);
        logD("recordCount key=" + str + ", value=" + num);
        Map map = this.mStats;
        if (num == null) {
            map.put(str, 1);
        } else {
            map.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public void reportStatistics() {
        Map map = this.mStats;
        if (map == null || map.isEmpty()) {
            return;
        }
        HashMap map2 = new HashMap(this.mStats);
        log("reportStatistics mStats=" + this.mStats);
        this.mStats.clear();
        MiStatInterfaceUtil.recordObjectEvent("signalMapNOML", "event_noml_singalmap", map2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SignalMapNOMLController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (DEBUG) {
            Log.d("SignalMapNOMLController", str);
        }
    }
}
