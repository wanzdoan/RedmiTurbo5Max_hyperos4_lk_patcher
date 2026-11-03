package com.android.phone.satellite;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkModeRecognition$$ExternalSyntheticLambda0;
import com.android.phone.R;
import com.android.phone.SatelliteManager;
import com.android.phone.SatelliteManagerBase;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteToCellularTracker {
    private static boolean DEBUG = false;
    private static SatelliteToCellularTracker sInstance;
    private CancellationSignal mCancelSignal;
    private Context mContext;
    private Location mLastLocation;
    private LocationManager mLocationManager;
    private NtnPhone mNtnPhone;
    private Phone mPhone;
    private SettingsObserver mSettingsObserver;
    private boolean mIsSatelliteOn = false;
    private boolean mHasCellInfo = false;
    private int mCellularRadioState = 2;
    private int mCellInfoRetryCount = 0;
    private TelephonyCallBack mTelephonyCallBack = null;
    private long mLastUpdateTime = 0;
    private long mModifyThreshold = 3000;
    private boolean mExitLowRachModeFailed = false;
    private int DEFAULT_PHONE_ID = 0;
    private Map mStats = new HashMap();
    private LocationListener mPassiveListener = new LocationListener() { // from class: com.android.phone.satellite.SatelliteToCellularTracker.1
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (!SatelliteToCellularTracker.this.mIsSatelliteOn) {
                if (SatelliteToCellularTracker.DEBUG) {
                    SatelliteToCellularTracker.this.logd("passive onLocationChanged satellite off, return");
                }
            } else {
                if (SatelliteToCellularTracker.DEBUG) {
                    SatelliteToCellularTracker.this.logd("mPassiveListener onLocationChanged");
                }
                SatelliteToCellularTracker.this.handleLocationChanged(location);
                SatelliteToCellularTracker.this.stopGetCurrentLocation();
                SatelliteToCellularTracker.this.scheduleNextGetCurrentLocation();
            }
        }
    };
    private LocationListener mGetCurrentLocationListener = new LocationListener() { // from class: com.android.phone.satellite.SatelliteToCellularTracker.2
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (!SatelliteToCellularTracker.this.mIsSatelliteOn) {
                if (SatelliteToCellularTracker.DEBUG) {
                    SatelliteToCellularTracker.this.logd("get current location onLocationChanged satellite off, return");
                }
            } else {
                if (SatelliteToCellularTracker.DEBUG) {
                    SatelliteToCellularTracker.this.logd("mGetCurrentLocationListener onLocationChanged");
                }
                SatelliteToCellularTracker.this.handleLocationChanged(location);
                SatelliteToCellularTracker.this.stopGetCurrentLocation();
                SatelliteToCellularTracker.this.scheduleNextGetCurrentLocation();
            }
        }
    };
    private Runnable mGetCurrentLocationTask = new Runnable() { // from class: com.android.phone.satellite.SatelliteToCellularTracker.3
        @Override // java.lang.Runnable
        public void run() {
            if (SatelliteToCellularTracker.DEBUG) {
                SatelliteToCellularTracker.this.logd("mGetCurrentLocationTask");
            }
            SatelliteToCellularTracker.this.requestGetCurrentLocation();
        }
    };
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.satellite.SatelliteToCellularTracker.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            if ("com.android.phone.satellte2cell.test".equals(action)) {
                SatelliteToCellularTracker.DEBUG = true;
                SatelliteToCellularTracker.this.mModifyThreshold = intent.getLongExtra("threshold", 3000L);
                boolean booleanExtra = intent.getBooleanExtra("triggerOnce", false);
                SatelliteToCellularTracker.this.logd("onReceive test action: modify threshold = " + SatelliteToCellularTracker.this.mModifyThreshold + ", triggerOnce = " + booleanExtra);
                if (!booleanExtra || SatelliteToCellularTracker.this.mNtnPhone == null) {
                    return;
                }
                SatelliteToCellularTracker.this.mNtnPhone.setRFMode(3, SatelliteToCellularTracker.this.mHandler.obtainMessage(2));
                return;
            }
            if ("com.android.phone.ACTION_TURN_OFF_SATELLITE".equals(action)) {
                SatelliteToCellularTracker.this.logd("onReceive turn off satellite action");
                SatelliteToCellularTracker.this.recordCount("satellite2CellularTurnOff");
                if (SatelliteToCellularTracker.this.mNtnPhone == null || SatelliteManagerBase.getInstance() == null) {
                    return;
                }
                SatelliteManagerBase.getInstance().closeSatellite(0, SatelliteToCellularTracker.this.mNtnPhone.getPhoneId());
                return;
            }
            if ("com.android.phone.ACTION_NOTIFICATION_REMOVED".equals(action)) {
                SatelliteToCellularTracker.this.logd("onReceive notification removed action");
                SatelliteToCellularTracker.this.mHandler.removeCallbacksAndMessages(null);
                SatelliteToCellularTracker.this.stopPassiveLocationUpdates();
                SatelliteToCellularTracker.this.stopGetCurrentLocation();
            }
        }
    };
    private WorkHandler mHandler = new WorkHandler(BackgroundThread.get().getLooper());

    public SatelliteToCellularTracker(Context context) {
        this.mContext = context;
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        this.mSettingsObserver = new SettingsObserver(context, this.mHandler);
        registerSettingsObserver();
        registerReceiver(context);
        setCellularIndicationAllowed(true);
    }

    public static synchronized SatelliteToCellularTracker make(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new SatelliteToCellularTracker(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public static SatelliteToCellularTracker getInstance() {
        return sInstance;
    }

    private void registerSettingsObserver() {
        this.mSettingsObserver.observe(Settings.System.getUriFor("satellite_state"), 1);
    }

    class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SatelliteToCellularTracker.this.logd("handleMessage received event " + message.what);
            switch (message.what) {
                case 1:
                    SatelliteToCellularTracker.this.handleSatelliteStateChanged();
                    break;
                case 2:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (asyncResult == null || asyncResult.exception != null) {
                        SatelliteToCellularTracker.this.loge("enter low rach mode exception =" + asyncResult.exception);
                    } else {
                        SatelliteToCellularTracker.this.recordCount("triggerCellularNetworkScan");
                        SatelliteToCellularTracker.this.logd("enter low rach mode");
                        if (SatelliteToCellularTracker.this.mNtnPhone != null) {
                            SatelliteToCellularTracker.this.mNtnPhone.mNtnRil.registerChannelState(this, 8, (Object) null);
                        }
                        if (SatelliteToCellularTracker.this.mPhone != null) {
                            SatelliteToCellularTracker.this.mCellularRadioState = 2;
                            SatelliteToCellularTracker.this.setCellularIndicationAllowed(false);
                            SatelliteToCellularTracker.this.logd("turn on cellular radio");
                            SatelliteToCellularTracker.this.mPhone.mCi.setRadioPower(true, obtainMessage(4));
                        }
                    }
                    break;
                case 3:
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    if (asyncResult2 == null || asyncResult2.exception != null) {
                        SatelliteToCellularTracker.this.mExitLowRachModeFailed = true;
                        SatelliteToCellularTracker.this.loge("exit low rach mode exception =" + asyncResult2.exception);
                    } else {
                        SatelliteToCellularTracker.this.mExitLowRachModeFailed = false;
                        if (SatelliteToCellularTracker.this.mNtnPhone != null) {
                            SatelliteToCellularTracker.this.mNtnPhone.mNtnRil.unregisterChannelState(this);
                        }
                        SatelliteToCellularTracker.this.logd("exit low rach mode done");
                    }
                    SatelliteToCellularTracker.this.setCellularIndicationAllowed(true);
                    break;
                case 4:
                    AsyncResult asyncResult3 = (AsyncResult) message.obj;
                    if (asyncResult3 == null || asyncResult3.exception != null) {
                        SatelliteToCellularTracker.this.loge("cell radio on exception =" + asyncResult3.exception);
                        if (SatelliteToCellularTracker.this.mNtnPhone != null) {
                            SatelliteToCellularTracker.this.mExitLowRachModeFailed = false;
                            SatelliteToCellularTracker.this.mNtnPhone.setRFMode(2, obtainMessage(3));
                        }
                    } else {
                        SatelliteToCellularTracker.this.mCellularRadioState = 1;
                        SatelliteToCellularTracker.this.logd("cell radio on");
                        if (SatelliteToCellularTracker.this.mNtnPhone != null) {
                            SatelliteToCellularTracker.this.mNtnPhone.notifyStartCellularNetworkScan(true);
                        }
                        SatelliteToCellularTracker.this.mCellInfoRetryCount = 0;
                        sendMessageDelayed(obtainMessage(6), 500L);
                    }
                    break;
                case 5:
                    AsyncResult asyncResult4 = (AsyncResult) message.obj;
                    if (asyncResult4 == null || asyncResult4.exception != null) {
                        SatelliteToCellularTracker.this.loge("cell radio off exception =" + asyncResult4.exception);
                    } else {
                        SatelliteToCellularTracker.this.mCellularRadioState = 0;
                        SatelliteToCellularTracker.this.logd("cell radio off");
                        if (SatelliteToCellularTracker.this.mNtnPhone != null) {
                            SatelliteToCellularTracker.this.mNtnPhone.notifyStartCellularNetworkScan(false);
                            SatelliteToCellularTracker.this.mNtnPhone.setRFMode(2, obtainMessage(3));
                        }
                    }
                    break;
                case 6:
                    SatelliteToCellularTracker.this.mCellInfoRetryCount++;
                    SatelliteToCellularTracker.this.logd("try get cell info, count =" + SatelliteToCellularTracker.this.mCellInfoRetryCount);
                    boolean zShouldRetryCellInfoCheck = SatelliteToCellularTracker.this.shouldRetryCellInfoCheck();
                    SatelliteToCellularTracker satelliteToCellularTracker = SatelliteToCellularTracker.this;
                    if (zShouldRetryCellInfoCheck) {
                        satelliteToCellularTracker.mPhone.mCi.getCellInfoList(obtainMessage(7), (WorkSource) null);
                    } else {
                        satelliteToCellularTracker.handleCellInfoFailure("searched cell info failed");
                    }
                    break;
                case 7:
                    AsyncResult asyncResult5 = (AsyncResult) message.obj;
                    if (asyncResult5 == null || asyncResult5.exception != null) {
                        SatelliteToCellularTracker.this.handleCellInfoFailure("cell info exception");
                    } else {
                        boolean zIsCellInfoValid = SatelliteToCellularTracker.this.isCellInfoValid((List) asyncResult5.result);
                        SatelliteToCellularTracker satelliteToCellularTracker2 = SatelliteToCellularTracker.this;
                        if (zIsCellInfoValid) {
                            satelliteToCellularTracker2.handleCellInfoSuccess();
                        } else {
                            satelliteToCellularTracker2.logd("cell info list is empty, try again");
                            sendMessageDelayed(obtainMessage(6), 500L);
                        }
                    }
                    break;
                case 8:
                    SatelliteToCellularTracker.this.logd("sat channel state changed received");
                    AsyncResult asyncResult6 = (AsyncResult) message.obj;
                    if (asyncResult6 == null || asyncResult6.exception != null) {
                        SatelliteToCellularTracker.this.logd("sat channel state changed exception");
                    } else {
                        int iIntValue = ((Integer) asyncResult6.result).intValue();
                        SatelliteToCellularTracker satelliteToCellularTracker3 = SatelliteToCellularTracker.this;
                        if (iIntValue != 2) {
                            satelliteToCellularTracker3.logd("sat channel state is not RACH, return");
                        } else {
                            satelliteToCellularTracker3.recordCount("stopCellularNetworkScan");
                            removeMessages(6);
                            int i = SatelliteToCellularTracker.this.mCellularRadioState;
                            SatelliteToCellularTracker satelliteToCellularTracker4 = SatelliteToCellularTracker.this;
                            if (i == 1) {
                                satelliteToCellularTracker4.logd("turn off cellular radio");
                                if (SatelliteToCellularTracker.this.mPhone != null) {
                                    SatelliteToCellularTracker.this.mPhone.mCi.setRadioPower(false, obtainMessage(5));
                                }
                            } else {
                                boolean z = satelliteToCellularTracker4.mExitLowRachModeFailed;
                                SatelliteToCellularTracker satelliteToCellularTracker5 = SatelliteToCellularTracker.this;
                                if (z) {
                                    satelliteToCellularTracker5.logd("exit low rach mode");
                                    if (SatelliteToCellularTracker.this.mNtnPhone != null) {
                                        SatelliteToCellularTracker.this.mExitLowRachModeFailed = false;
                                        SatelliteToCellularTracker.this.mNtnPhone.setRFMode(2, obtainMessage(3));
                                    }
                                } else {
                                    satelliteToCellularTracker5.logd("something wrong, return");
                                }
                            }
                        }
                    }
                    break;
                case 9:
                    SatelliteToCellularTracker.this.removeNotification();
                    removeMessages(10);
                    break;
                case 10:
                    SatelliteToCellularTracker.this.recordCount("satellite2CellularNotify");
                    SatelliteToCellularTracker.this.showNotification();
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCellularIndicationAllowed(boolean z) {
        logd("setCellularIndicationAllowed allowed : " + z);
        if (z) {
            SystemProperties.set("ril.satellite.cellular_ind_allowed", "1");
        } else {
            SystemProperties.set("ril.satellite.cellular_ind_allowed", "0");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldRetryCellInfoCheck() {
        Phone phone;
        return (this.mCellInfoRetryCount > 20 || (phone = this.mPhone) == null || phone.mCi == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCellInfoFailure(String str) {
        logd(str);
        this.mHasCellInfo = false;
        recordCount("cellularScanFail");
        removeNotification();
        turnOffCellRadio();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCellInfoSuccess() {
        logd("Successfully retrieved cell info, try to show notification");
        this.mHasCellInfo = true;
        recordCount("cellularScanSuccess");
        WorkHandler workHandler = this.mHandler;
        workHandler.sendMessageDelayed(workHandler.obtainMessage(10), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
        turnOffCellRadio();
    }

    private void turnOffCellRadio() {
        CommandsInterface commandsInterface;
        Phone phone = this.mPhone;
        if (phone == null || (commandsInterface = phone.mCi) == null) {
            return;
        }
        commandsInterface.setRadioPower(false, this.mHandler.obtainMessage(5));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCellInfoValid(List list) {
        return (list == null || list.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSatelliteStateChanged() {
        this.mIsSatelliteOn = Settings.System.getInt(this.mContext.getContentResolver(), "satellite_state", 0) == 1;
        logd("handleSatelliteStateChanged: satellite on = " + this.mIsSatelliteOn);
        if (this.mIsSatelliteOn) {
            NtnPhone defaultNtnPhone = SatelliteManager.getDefaultNtnPhone();
            this.mNtnPhone = defaultNtnPhone;
            if (defaultNtnPhone == null) {
                logd("ntn phone is null, return");
                return;
            }
            Phone phone = MiuiPhoneUtils.getPhone(this.DEFAULT_PHONE_ID);
            this.mPhone = phone;
            if (phone == null) {
                logd("cell phone is null, return");
                return;
            }
            if (SatelliteManagerBase.getInstance() == null) {
                logd("SatelliteManager is null, return");
                return;
            }
            this.mCellularRadioState = 2;
            this.mHasCellInfo = SatelliteManagerBase.getInstance().hasCellInfoWhenTurnOnSatellite();
            this.mLastLocation = SatelliteManagerBase.getInstance().getCurrentLocationWhenTurnOnSatellite();
            this.mLastUpdateTime = SystemClock.elapsedRealtime();
            requestPassiveLocationUpdates();
            scheduleNextGetCurrentLocation();
            registerTelephonyCallBack();
            return;
        }
        this.mNtnPhone = null;
        this.mPhone = null;
        this.mCellInfoRetryCount = 0;
        this.mLastUpdateTime = 0L;
        this.mHasCellInfo = false;
        removeNotification();
        this.mHandler.removeCallbacksAndMessages(null);
        unRegisterTelephonyCallBack();
        stopPassiveLocationUpdates();
        stopGetCurrentLocation();
        setCellularIndicationAllowed(true);
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            logd("register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void unRegisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            logd("unregister TelephonyCallBack");
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                SatelliteToCellularTracker.this.logd("onPreciseCallStateChanged call state : " + i2);
                SatelliteToCellularTracker.this.mHandler.removeMessages(9);
                SatelliteToCellularTracker.this.mHandler.sendMessageDelayed(SatelliteToCellularTracker.this.mHandler.obtainMessage(9, Integer.valueOf(i)), 500L);
            }
        }
    }

    private void requestPassiveLocationUpdates() {
        if (this.mLocationManager.isProviderEnabled("passive")) {
            if (DEBUG) {
                logd("requestPassiveLocationUpdates");
            }
            this.mLocationManager.requestLocationUpdates("passive", 300000L, 100.0f, this.mPassiveListener, this.mHandler.getLooper());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPassiveLocationUpdates() {
        if (DEBUG) {
            logd("stopPassiveLocationUpdates");
        }
        this.mLocationManager.removeUpdates(this.mPassiveListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestGetCurrentLocation() {
        if (this.mLocationManager.isProviderEnabled("gps")) {
            if (DEBUG) {
                logd("requestGetCurrentLocation");
            }
            LocationRequest locationRequestBuild = new LocationRequest.Builder(1000L).setMinUpdateIntervalMillis(1000L).build();
            CancellationSignal cancellationSignal = new CancellationSignal();
            this.mCancelSignal = cancellationSignal;
            LocationManager locationManager = this.mLocationManager;
            final WorkHandler workHandler = this.mHandler;
            Objects.requireNonNull(workHandler);
            Executor executor = new Executor() { // from class: com.android.phone.satellite.SatelliteToCellularTracker$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    workHandler.post(runnable);
                }
            };
            LocationListener locationListener = this.mGetCurrentLocationListener;
            Objects.requireNonNull(locationListener);
            locationManager.getCurrentLocation("gps", locationRequestBuild, cancellationSignal, executor, new NetworkModeRecognition$$ExternalSyntheticLambda0(locationListener));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopGetCurrentLocation() {
        if (DEBUG) {
            logd("stopGetCurrentLocation");
        }
        CancellationSignal cancellationSignal = this.mCancelSignal;
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            this.mCancelSignal.cancel();
            this.mCancelSignal = null;
        }
        this.mHandler.removeCallbacks(this.mGetCurrentLocationTask);
        this.mLocationManager.removeUpdates(this.mGetCurrentLocationListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleNextGetCurrentLocation() {
        if (DEBUG) {
            logd("scheduleNextGetCurrentLocation");
        }
        this.mHandler.postDelayed(this.mGetCurrentLocationTask, 900000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLocationChanged(Location location) {
        if (location == null) {
            logd("handleLocationChanged location is null, return");
            return;
        }
        logd("handleLocationChanged provider : " + location.getProvider());
        if (this.mLastLocation == null || SystemClock.elapsedRealtime() - this.mLastUpdateTime < 120000) {
            logd("handleLocationChanged location changes frequently, return");
            return;
        }
        this.mLastUpdateTime = SystemClock.elapsedRealtime();
        long jDistanceTo = (long) location.distanceTo(this.mLastLocation);
        if (jDistanceTo >= 3000 || jDistanceTo >= this.mModifyThreshold) {
            if (DEBUG) {
                logd("outside the distance threshold");
            }
            NtnPhone ntnPhone = this.mNtnPhone;
            if (ntnPhone != null) {
                ntnPhone.setRFMode(3, this.mHandler.obtainMessage(2));
                recordCount("triggerEnterLowRachMode");
                this.mLastLocation = location;
                return;
            }
            return;
        }
        if (DEBUG) {
            logd("within the distance threshold");
        }
        if (!this.mHasCellInfo) {
            removeNotification();
            return;
        }
        this.mHandler.removeMessages(10);
        if (DEBUG) {
            logd("last time has cellinfo, try to show notification");
        }
        WorkHandler workHandler = this.mHandler;
        workHandler.sendMessageDelayed(workHandler.obtainMessage(10), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
    }

    private void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.satellte2cell.test");
        intentFilter.addAction("com.android.phone.ACTION_TURN_OFF_SATELLITE");
        intentFilter.addAction("com.android.phone.ACTION_NOTIFICATION_REMOVED");
        context.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNotification() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        notificationManager.createNotificationChannel(new NotificationChannel("satellite_under_cellular_coverage", this.mContext.getString(R.string.cellular_coverage_satellite_notification), 4));
        PendingIntent broadcast = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.phone.ACTION_TURN_OFF_SATELLITE").setPackage(this.mContext.getPackageName()), 67108864);
        PendingIntent broadcast2 = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.phone.ACTION_NOTIFICATION_REMOVED").setPackage(this.mContext.getPackageName()), 67108864);
        Bundle bundle = new Bundle();
        bundle.putBoolean("miui.showAction", true);
        bundle.putParcelable("miui.appIcon", Icon.createWithResource(this.mContext, R.drawable.settings_satellite));
        Notification notificationBuild = new Notification.Builder(this.mContext, "satellite_under_cellular_coverage").setOngoing(true).setSmallIcon(R.drawable.settings_satellite).addExtras(bundle).setContentText(this.mContext.getString(R.string.cellular_coverage_satellite_notification_text)).setContentTitle(this.mContext.getString(R.string.cellular_coverage_satellite_notification_title)).setDeleteIntent(broadcast2).addAction(0, this.mContext.getString(R.string.cellular_coverage_satellite_notification_action_text), broadcast).setAutoCancel(true).setChannelId("satellite_under_cellular_coverage").build();
        logd("show notification");
        notificationManager.notify(1, notificationBuild);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeNotification() {
        ((NotificationManager) this.mContext.getSystemService("notification")).cancel(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordCount(String str) {
        Integer num = (Integer) this.mStats.get(str);
        if (DEBUG) {
            logd("recordCount key=" + str + ", value=" + num);
        }
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
        if (DEBUG) {
            logd("reportStatistics mStats=" + this.mStats);
        }
        this.mStats.clear();
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_satellite2cellular_tracker", map2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d("SatelliteToCellularTracker", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Log.e("SatelliteToCellularTracker", str);
    }
}
