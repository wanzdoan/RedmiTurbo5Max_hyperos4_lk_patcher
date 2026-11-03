package com.android.internal.telephony.satellite;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.PersistentLogger;
import android.telephony.satellite.ISatelliteTransmissionUpdateCallback;
import android.telephony.satellite.PointingInfo;
import android.telephony.satellite.PointingUiAppLaunchIntentAttributes;
import android.telephony.satellite.SatelliteManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.SomeArgs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class PointingAppController extends Handler {
    private static final boolean DEBUG = !"user".equals(Build.TYPE);
    private static PointingAppController sInstance;
    private ActivityManager mActivityManager;
    private final Context mContext;
    private final FeatureFlags mFeatureFlags;
    private PersistentLogger mPersistentLogger;
    private AtomicBoolean mStartedSatelliteTransmissionUpdates = new AtomicBoolean(false);
    private AtomicBoolean mLastNeedFullScreenPointingUI = new AtomicBoolean(false);
    private AtomicBoolean mLastIsDemoMode = new AtomicBoolean(false);
    private AtomicBoolean mLastIsEmergency = new AtomicBoolean(false);
    private AtomicBoolean mListenerForPointingUIRegistered = new AtomicBoolean(false);
    private final ConcurrentHashMap mSatelliteTransmissionUpdateHandlers = new ConcurrentHashMap();
    private String mPointingUiPackageName = "";
    private String mPointingUiClassName = "";
    public UidImportanceListener mUidImportanceListener = new UidImportanceListener();

    static PointingAppController make(Context context, FeatureFlags featureFlags) {
        if (sInstance == null) {
            sInstance = new PointingAppController(context, featureFlags);
        }
        return sInstance;
    }

    protected PointingAppController(Context context, FeatureFlags featureFlags) {
        this.mPersistentLogger = null;
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mListenerForPointingUIRegistered.set(false);
        this.mActivityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            plogd("REQUEST_START_POINTING_UI");
            SomeArgs someArgs = (SomeArgs) message.obj;
            try {
                handleRequestStartPointingUI(((Boolean) someArgs.arg1).booleanValue(), ((Boolean) someArgs.arg2).booleanValue(), ((Boolean) someArgs.arg3).booleanValue());
                return;
            } finally {
                someArgs.recycle();
            }
        }
        if (i == 2) {
            handleRequestRemoveListenerForPointingUI();
            return;
        }
        ploge("PointingAppControllerHandler: unexpected message code: " + message.what);
    }

    protected void setStartedSatelliteTransmissionUpdates(boolean z) {
        this.mStartedSatelliteTransmissionUpdates.set(z);
    }

    protected boolean getStartedSatelliteTransmissionUpdates() {
        return this.mStartedSatelliteTransmissionUpdates.get();
    }

    public class UidImportanceListener implements ActivityManager.OnUidImportanceListener {
        public UidImportanceListener() {
        }

        public void onUidImportance(int i, int i2) {
            if (i2 != 1000) {
                return;
            }
            String[] packagesForUid = PointingAppController.this.mContext.getPackageManager().getPackagesForUid(i);
            final String pointingUiPackageName = PointingAppController.this.getPointingUiPackageName();
            if (packagesForUid != null) {
                Stream stream = Arrays.stream(packagesForUid);
                Objects.requireNonNull(pointingUiPackageName);
                if (stream.anyMatch(new Predicate() { // from class: com.android.internal.telephony.satellite.PointingAppController$UidImportanceListener$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return pointingUiPackageName.contains((String) obj);
                    }
                })) {
                    PointingAppController.this.plogd("Restarting pointingUI");
                    PointingAppController pointingAppController = PointingAppController.this;
                    pointingAppController.startPointingUI(pointingAppController.mLastNeedFullScreenPointingUI.get(), PointingAppController.this.mLastIsDemoMode.get(), PointingAppController.this.mLastIsEmergency.get());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class DatagramTransferStateHandlerRequest {
        public int datagramTransferState;
        public int datagramType;
        public int errorCode;
        public int pendingCount;

        DatagramTransferStateHandlerRequest(int i, int i2, int i3, int i4) {
            this.datagramType = i;
            this.datagramTransferState = i2;
            this.pendingCount = i3;
            this.errorCode = i4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SatelliteTransmissionUpdateHandler extends Handler {
        public static final int EVENT_DATAGRAM_TRANSFER_STATE_CHANGED = 4;
        public static final int EVENT_POSITION_INFO_CHANGED = 1;
        public static final int EVENT_RECEIVE_DATAGRAM_STATE_CHANGED = 3;
        public static final int EVENT_SEND_DATAGRAM_REQUESTED = 5;
        public static final int EVENT_SEND_DATAGRAM_STATE_CHANGED = 2;
        private final ConcurrentHashMap mListeners;

        SatelliteTransmissionUpdateHandler(Looper looper) {
            super(looper);
            this.mListeners = new ConcurrentHashMap();
        }

        public void addListener(ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
            this.mListeners.put(iSatelliteTransmissionUpdateCallback.asBinder(), iSatelliteTransmissionUpdateCallback);
        }

        public void removeListener(ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
            this.mListeners.remove(iSatelliteTransmissionUpdateCallback.asBinder());
        }

        public boolean hasListeners() {
            return !this.mListeners.isEmpty();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                final PointingInfo pointingInfo = (PointingInfo) ((AsyncResult) message.obj).result;
                final ArrayList arrayList = new ArrayList();
                this.mListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.PointingAppController$SatelliteTransmissionUpdateHandler$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PointingAppController.SatelliteTransmissionUpdateHandler.$r8$lambda$REbvDwlblbSaxAJeNWZNkuAhJmU(pointingInfo, arrayList, (ISatelliteTransmissionUpdateCallback) obj);
                    }
                });
                arrayList.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.PointingAppController$SatelliteTransmissionUpdateHandler$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$handleMessage$1((IBinder) obj);
                    }
                });
                return;
            }
            if (i == 2) {
                PointingAppController.logd("Received EVENT_SEND_DATAGRAM_STATE_CHANGED");
                final DatagramTransferStateHandlerRequest datagramTransferStateHandlerRequest = (DatagramTransferStateHandlerRequest) message.obj;
                final ArrayList arrayList2 = new ArrayList();
                this.mListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.PointingAppController$SatelliteTransmissionUpdateHandler$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PointingAppController.SatelliteTransmissionUpdateHandler.m1561$r8$lambda$fYdUkv3CmOKmJreppzjsSyNH_M(datagramTransferStateHandlerRequest, arrayList2, (ISatelliteTransmissionUpdateCallback) obj);
                    }
                });
                arrayList2.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.PointingAppController$SatelliteTransmissionUpdateHandler$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$handleMessage$3((IBinder) obj);
                    }
                });
                return;
            }
            if (i == 3) {
                PointingAppController.logd("Received EVENT_RECEIVE_DATAGRAM_STATE_CHANGED");
                final DatagramTransferStateHandlerRequest datagramTransferStateHandlerRequest2 = (DatagramTransferStateHandlerRequest) message.obj;
                final ArrayList arrayList3 = new ArrayList();
                this.mListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.PointingAppController$SatelliteTransmissionUpdateHandler$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PointingAppController.SatelliteTransmissionUpdateHandler.$r8$lambda$tg99K65PYy7W2QCgpciGawR4umA(datagramTransferStateHandlerRequest2, arrayList3, (ISatelliteTransmissionUpdateCallback) obj);
                    }
                });
                arrayList3.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.PointingAppController$SatelliteTransmissionUpdateHandler$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$handleMessage$5((IBinder) obj);
                    }
                });
                return;
            }
            if (i == 4) {
                PointingAppController.logd("Receive EVENT_DATAGRAM_TRANSFER_STATE_CHANGED state=" + ((Integer) ((AsyncResult) message.obj).result).intValue());
                return;
            }
            if (i == 5) {
                PointingAppController.logd("Received EVENT_SEND_DATAGRAM_REQUESTED");
                final int iIntValue = ((Integer) message.obj).intValue();
                final ArrayList arrayList4 = new ArrayList();
                this.mListeners.values().forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.PointingAppController$SatelliteTransmissionUpdateHandler$$ExternalSyntheticLambda6
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PointingAppController.SatelliteTransmissionUpdateHandler.m1559$r8$lambda$5faRvxSwBUQojLXhkZfNyngZpk(iIntValue, arrayList4, (ISatelliteTransmissionUpdateCallback) obj);
                    }
                });
                arrayList4.forEach(new Consumer() { // from class: com.android.internal.telephony.satellite.PointingAppController$SatelliteTransmissionUpdateHandler$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$handleMessage$7((IBinder) obj);
                    }
                });
                return;
            }
            PointingAppController.loge("SatelliteTransmissionUpdateHandler unknown event: " + message.what);
        }

        public static /* synthetic */ void $r8$lambda$REbvDwlblbSaxAJeNWZNkuAhJmU(PointingInfo pointingInfo, List list, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
            try {
                iSatelliteTransmissionUpdateCallback.onSatellitePositionChanged(pointingInfo);
            } catch (RemoteException e) {
                PointingAppController.logd("EVENT_POSITION_INFO_CHANGED RemoteException: " + e);
                list.add(iSatelliteTransmissionUpdateCallback.asBinder());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$1(IBinder iBinder) {
            this.mListeners.remove(iBinder);
        }

        /* JADX INFO: renamed from: $r8$lambda$fYdUkv3CmOKmJre-ppzjsSyNH_M, reason: not valid java name */
        public static /* synthetic */ void m1561$r8$lambda$fYdUkv3CmOKmJreppzjsSyNH_M(DatagramTransferStateHandlerRequest datagramTransferStateHandlerRequest, List list, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
            try {
                iSatelliteTransmissionUpdateCallback.onSendDatagramStateChanged(datagramTransferStateHandlerRequest.datagramType, datagramTransferStateHandlerRequest.datagramTransferState, datagramTransferStateHandlerRequest.pendingCount, datagramTransferStateHandlerRequest.errorCode);
            } catch (RemoteException e) {
                PointingAppController.logd("EVENT_SEND_DATAGRAM_STATE_CHANGED RemoteException: " + e);
                list.add(iSatelliteTransmissionUpdateCallback.asBinder());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$3(IBinder iBinder) {
            this.mListeners.remove(iBinder);
        }

        public static /* synthetic */ void $r8$lambda$tg99K65PYy7W2QCgpciGawR4umA(DatagramTransferStateHandlerRequest datagramTransferStateHandlerRequest, List list, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
            try {
                iSatelliteTransmissionUpdateCallback.onReceiveDatagramStateChanged(datagramTransferStateHandlerRequest.datagramTransferState, datagramTransferStateHandlerRequest.pendingCount, datagramTransferStateHandlerRequest.errorCode);
            } catch (RemoteException e) {
                PointingAppController.logd("EVENT_RECEIVE_DATAGRAM_STATE_CHANGED RemoteException: " + e);
                list.add(iSatelliteTransmissionUpdateCallback.asBinder());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$5(IBinder iBinder) {
            this.mListeners.remove(iBinder);
        }

        /* JADX INFO: renamed from: $r8$lambda$5faRvxSw-BUQojLXhkZfNyngZpk, reason: not valid java name */
        public static /* synthetic */ void m1559$r8$lambda$5faRvxSwBUQojLXhkZfNyngZpk(int i, List list, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
            try {
                iSatelliteTransmissionUpdateCallback.onSendDatagramRequested(i);
            } catch (RemoteException e) {
                PointingAppController.logd("EVENT_SEND_DATAGRAM_REQUESTED RemoteException: " + e);
                list.add(iSatelliteTransmissionUpdateCallback.asBinder());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$7(IBinder iBinder) {
            this.mListeners.remove(iBinder);
        }
    }

    protected void registerForSatelliteTransmissionUpdates(int i, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
        SatelliteTransmissionUpdateHandler satelliteTransmissionUpdateHandler = (SatelliteTransmissionUpdateHandler) this.mSatelliteTransmissionUpdateHandlers.get(Integer.MAX_VALUE);
        if (satelliteTransmissionUpdateHandler != null) {
            satelliteTransmissionUpdateHandler.addListener(iSatelliteTransmissionUpdateCallback);
            return;
        }
        SatelliteTransmissionUpdateHandler satelliteTransmissionUpdateHandler2 = new SatelliteTransmissionUpdateHandler(Looper.getMainLooper());
        satelliteTransmissionUpdateHandler2.addListener(iSatelliteTransmissionUpdateCallback);
        this.mSatelliteTransmissionUpdateHandlers.put(Integer.MAX_VALUE, satelliteTransmissionUpdateHandler2);
        SatelliteModemInterface.getInstance().registerForSatellitePositionInfoChanged(satelliteTransmissionUpdateHandler2, 1, null);
        SatelliteModemInterface.getInstance().registerForDatagramTransferStateChanged(satelliteTransmissionUpdateHandler2, 4, null);
    }

    protected void unregisterForSatelliteTransmissionUpdates(int i, Consumer<Integer> consumer, ISatelliteTransmissionUpdateCallback iSatelliteTransmissionUpdateCallback) {
        SatelliteTransmissionUpdateHandler satelliteTransmissionUpdateHandler = (SatelliteTransmissionUpdateHandler) this.mSatelliteTransmissionUpdateHandlers.get(Integer.MAX_VALUE);
        if (satelliteTransmissionUpdateHandler != null) {
            satelliteTransmissionUpdateHandler.removeListener(iSatelliteTransmissionUpdateCallback);
            if (!satelliteTransmissionUpdateHandler.hasListeners()) {
                this.mSatelliteTransmissionUpdateHandlers.remove(Integer.MAX_VALUE);
                SatelliteModemInterface satelliteModemInterface = SatelliteModemInterface.getInstance();
                satelliteModemInterface.unregisterForSatellitePositionInfoChanged(satelliteTransmissionUpdateHandler);
                satelliteModemInterface.unregisterForDatagramTransferStateChanged(satelliteTransmissionUpdateHandler);
                return;
            }
            consumer.accept(0);
        }
    }

    protected void startSatelliteTransmissionUpdates(Message message) {
        if (this.mStartedSatelliteTransmissionUpdates.get()) {
            plogd("startSatelliteTransmissionUpdates: already started");
            AsyncResult.forMessage(message, (Object) null, new SatelliteManager.SatelliteException(0));
            message.sendToTarget();
        } else {
            SatelliteModemInterface.getInstance().startSendingSatellitePointingInfo(message);
            this.mStartedSatelliteTransmissionUpdates.set(true);
        }
    }

    protected void stopSatelliteTransmissionUpdates(Message message) {
        setStartedSatelliteTransmissionUpdates(false);
        SatelliteModemInterface.getInstance().stopSendingSatellitePointingInfo(message);
    }

    protected void startPointingUI(boolean z, boolean z2, boolean z3) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = Boolean.valueOf(z);
        someArgsObtain.arg2 = Boolean.valueOf(z2);
        someArgsObtain.arg3 = Boolean.valueOf(z3);
        sendMessage(obtainMessage(1, someArgsObtain));
    }

    private void handleRequestStartPointingUI(boolean z, boolean z2, boolean z3) {
        Intent intentCreateLaunchIntentForPointingUiApp = createLaunchIntentForPointingUiApp(z, z2, z3);
        if (intentCreateLaunchIntentForPointingUiApp == null) {
            plogd("handleRequestStartPointingUI: launchIntent is null");
            return;
        }
        try {
            if (!this.mListenerForPointingUIRegistered.get()) {
                this.mActivityManager.addOnUidImportanceListener(this.mUidImportanceListener, 1000);
                this.mListenerForPointingUIRegistered.set(true);
            }
            this.mLastNeedFullScreenPointingUI.set(z);
            this.mLastIsDemoMode.set(z2);
            this.mLastIsEmergency.set(z3);
            this.mContext.startActivityAsUser(intentCreateLaunchIntentForPointingUiApp, UserHandle.CURRENT);
        } catch (ActivityNotFoundException | IllegalArgumentException e) {
            ploge("startPointingUI: Unable to start Pointing UI activity due to an exception, ex=" + e);
        }
    }

    protected void removeListenerForPointingUI() {
        sendMessage(obtainMessage(2));
    }

    private void handleRequestRemoveListenerForPointingUI() {
        plogd("handleRequestRemoveListenerForPointingUI");
        if (this.mListenerForPointingUIRegistered.get()) {
            this.mActivityManager.removeOnUidImportanceListener(this.mUidImportanceListener);
            this.mListenerForPointingUIRegistered.set(false);
        }
    }

    protected void updateSendDatagramTransferState(int i, int i2, int i3, int i4, int i5) {
        DatagramTransferStateHandlerRequest datagramTransferStateHandlerRequest = new DatagramTransferStateHandlerRequest(i2, i3, i4, i5);
        SatelliteTransmissionUpdateHandler satelliteTransmissionUpdateHandler = (SatelliteTransmissionUpdateHandler) this.mSatelliteTransmissionUpdateHandlers.get(Integer.MAX_VALUE);
        if (satelliteTransmissionUpdateHandler != null) {
            satelliteTransmissionUpdateHandler.obtainMessage(2, datagramTransferStateHandlerRequest).sendToTarget();
        } else {
            ploge("SatelliteTransmissionUpdateHandler not found for subId: 2147483647");
        }
    }

    void onSendDatagramRequested(int i, int i2) {
        SatelliteTransmissionUpdateHandler satelliteTransmissionUpdateHandler = (SatelliteTransmissionUpdateHandler) this.mSatelliteTransmissionUpdateHandlers.get(Integer.MAX_VALUE);
        if (satelliteTransmissionUpdateHandler != null) {
            satelliteTransmissionUpdateHandler.obtainMessage(5, Integer.valueOf(i2)).sendToTarget();
        } else {
            ploge("SatelliteTransmissionUpdateHandler not found for subId: 2147483647");
        }
    }

    protected void updateReceiveDatagramTransferState(int i, int i2, int i3, int i4) {
        DatagramTransferStateHandlerRequest datagramTransferStateHandlerRequest = new DatagramTransferStateHandlerRequest(0, i2, i3, i4);
        SatelliteTransmissionUpdateHandler satelliteTransmissionUpdateHandler = (SatelliteTransmissionUpdateHandler) this.mSatelliteTransmissionUpdateHandlers.get(Integer.MAX_VALUE);
        if (satelliteTransmissionUpdateHandler != null) {
            satelliteTransmissionUpdateHandler.obtainMessage(3, datagramTransferStateHandlerRequest).sendToTarget();
        }
    }

    boolean setSatellitePointingUiClassName(String str, String str2) {
        if (!isMockModemAllowed()) {
            ploge("setSatellitePointingUiClassName: modifying satellite pointing UI package and class name is not allowed");
            return false;
        }
        plogd("setSatellitePointingUiClassName: config_pointing_ui_package is updated, new packageName=" + str + ", config_pointing_ui_class new className=" + str2);
        if (str == null || str.equals("null")) {
            this.mPointingUiPackageName = "";
            this.mPointingUiClassName = "";
            return true;
        }
        this.mPointingUiPackageName = str;
        if (str2 == null || str2.equals("null")) {
            this.mPointingUiClassName = "";
            return true;
        }
        this.mPointingUiClassName = str2;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPointingUiPackageName() {
        if (!TextUtils.isEmpty(this.mPointingUiPackageName)) {
            return this.mPointingUiPackageName;
        }
        return TextUtils.emptyIfNull(this.mContext.getResources().getString(R.string.deprecated_target_sdk_message));
    }

    private String getPointingUiClassName() {
        if (!TextUtils.isEmpty(this.mPointingUiClassName)) {
            return this.mPointingUiClassName;
        }
        return TextUtils.emptyIfNull(this.mContext.getResources().getString(R.string.deprecated_target_sdk_app_store));
    }

    private Intent createLaunchIntentForPointingUiApp(boolean z, boolean z2, boolean z3) {
        Intent launchIntentForPackage;
        String pointingUiPackageName = getPointingUiPackageName();
        if (TextUtils.isEmpty(pointingUiPackageName)) {
            plogd("createLaunchIntentForPointingUiApp: config_pointing_ui_package is not set");
            return null;
        }
        String pointingUiClassName = getPointingUiClassName();
        if (!TextUtils.isEmpty(pointingUiClassName)) {
            launchIntentForPackage = new Intent().setComponent(new ComponentName(pointingUiPackageName, pointingUiClassName));
        } else {
            launchIntentForPackage = this.mContext.getPackageManager().getLaunchIntentForPackage(pointingUiPackageName);
        }
        if (launchIntentForPackage == null) {
            ploge("createLaunchIntentForPointingUiApp: launchIntent is null");
            return null;
        }
        plogd("createLaunchIntentForPointingUiApp: needFullScreenPointingUI: " + z + ", isDemoMode: " + z2 + ", isEmergency: " + z3);
        launchIntentForPackage.putExtra("needFullScreen", z);
        launchIntentForPackage.putExtra("isDemoMode", z2);
        launchIntentForPackage.putExtra("isEmergency", z3);
        launchIntentForPackage.setFlags(67371008);
        return launchIntentForPackage;
    }

    public PendingIntent createPointingUiAppPendingIntent(PointingUiAppLaunchIntentAttributes pointingUiAppLaunchIntentAttributes) {
        Intent intentCreateLaunchIntentForPointingUiApp = createLaunchIntentForPointingUiApp(pointingUiAppLaunchIntentAttributes.isFullScreen(), pointingUiAppLaunchIntentAttributes.isDemoMode(), pointingUiAppLaunchIntentAttributes.isEmergencyMode());
        if (intentCreateLaunchIntentForPointingUiApp == null) {
            plogd("createPointingUiAppPendingIntent: failed to create launchIntent");
            return null;
        }
        return PendingIntent.getActivity(this.mContext, 0, intentCreateLaunchIntentForPointingUiApp, 1140850688, ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(3).toBundle());
    }

    private boolean isMockModemAllowed() {
        return DEBUG || SystemProperties.getBoolean("persist.radio.allow_mock_modem", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("PointingAppController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("PointingAppController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        Log.d("PointingAppController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("PointingAppController", str);
        }
    }

    private void ploge(String str) {
        Log.e("PointingAppController", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("PointingAppController", str);
        }
    }
}
