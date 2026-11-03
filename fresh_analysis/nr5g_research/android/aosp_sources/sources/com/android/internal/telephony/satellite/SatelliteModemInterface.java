package com.android.internal.telephony.satellite;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.radio.network.PrioritizedNetworkScanRequest;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.IBooleanConsumer;
import android.telephony.IIntegerConsumer;
import android.telephony.PersistentLogger;
import android.telephony.satellite.SatelliteManager;
import android.telephony.satellite.SatelliteModemEnableRequestAttributes;
import android.telephony.satellite.SystemSelectionSpecifier;
import android.telephony.satellite.stub.INtnSignalStrengthConsumer;
import android.telephony.satellite.stub.ISatellite;
import android.telephony.satellite.stub.ISatelliteCapabilitiesConsumer;
import android.telephony.satellite.stub.ISatelliteListener;
import android.telephony.satellite.stub.NtnSignalStrength;
import android.telephony.satellite.stub.PointingInfo;
import android.telephony.satellite.stub.SatelliteCapabilities;
import android.telephony.satellite.stub.SatelliteDatagram;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.ExponentialBackoff;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.util.FunctionalUtils;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteModemInterface {
    private static SatelliteModemInterface sInstance;
    private final Context mContext;
    private final DemoSimulator mDemoSimulator;
    protected final ExponentialBackoff mExponentialBackoff;
    private final FeatureFlags mFeatureFlags;
    private PersistentLogger mPersistentLogger;
    private final SatelliteController mSatelliteController;
    private ISatellite mSatelliteService;
    private SatelliteServiceConnection mSatelliteServiceConnection;
    private AtomicBoolean mIsBound = new AtomicBoolean(false);
    private AtomicBoolean mIsBinding = new AtomicBoolean(false);
    private AtomicBoolean mIsSatelliteServiceSupported = new AtomicBoolean(false);
    private final Object mLock = new Object();
    private String mVendorSatellitePackageName = "";
    private final RegistrantList mSatellitePositionInfoChangedRegistrants = new RegistrantList();
    private final RegistrantList mDatagramTransferStateChangedRegistrants = new RegistrantList();
    private final RegistrantList mSatelliteModemStateChangedRegistrants = new RegistrantList();
    private final RegistrantList mPendingDatagramsRegistrants = new RegistrantList();
    private final RegistrantList mSatelliteDatagramsReceivedRegistrants = new RegistrantList();
    private final RegistrantList mNtnSignalStrengthChangedRegistrants = new RegistrantList();
    private final RegistrantList mSatelliteCapabilitiesChangedRegistrants = new RegistrantList();
    private final RegistrantList mSatelliteSupportedStateChangedRegistrants = new RegistrantList();
    private final RegistrantList mSatelliteRegistrationFailureRegistrants = new RegistrantList();
    private final RegistrantList mTerrestrialNetworkAvailableChangedRegistrants = new RegistrantList();
    private final SatelliteListener mVendorListener = new SatelliteListener(false);
    private final SatelliteListener mDemoListener = new SatelliteListener(true);

    private class SatelliteListener extends ISatelliteListener.Stub {
        private final boolean mIsDemoListener;

        SatelliteListener(boolean z) {
            this.mIsDemoListener = z;
        }

        public void onSatelliteDatagramReceived(SatelliteDatagram satelliteDatagram, int i) {
            if (notifyResultIfExpectedListener()) {
                SatelliteModemInterface.this.plogd("onSatelliteDatagramReceived: pendingCount=" + i);
                SatelliteModemInterface.this.mSatelliteDatagramsReceivedRegistrants.notifyResult(new Pair(SatelliteServiceUtils.fromSatelliteDatagram(satelliteDatagram), Integer.valueOf(i)));
            }
        }

        public void onPendingDatagrams() {
            if (notifyResultIfExpectedListener()) {
                SatelliteModemInterface.this.plogd("onPendingDatagrams");
                SatelliteModemInterface.this.mPendingDatagramsRegistrants.notifyResult(null);
            }
        }

        public void onSatellitePositionChanged(PointingInfo pointingInfo) {
            SatelliteModemInterface.this.mSatellitePositionInfoChangedRegistrants.notifyResult(SatelliteServiceUtils.fromPointingInfo(pointingInfo));
        }

        public void onSatelliteModemStateChanged(int i) {
            int i2;
            if (notifyModemStateChanged(i)) {
                SatelliteModemInterface.this.mSatelliteModemStateChangedRegistrants.notifyResult(Integer.valueOf(SatelliteServiceUtils.fromSatelliteModemState(i)));
                if (i != 0) {
                    i2 = 1;
                    if (i == 1) {
                        i2 = 4;
                    } else if (i != 2) {
                        i2 = -1;
                    }
                } else {
                    i2 = 0;
                }
                SatelliteModemInterface.this.mDatagramTransferStateChangedRegistrants.notifyResult(Integer.valueOf(i2));
            }
        }

        public void onNtnSignalStrengthChanged(NtnSignalStrength ntnSignalStrength) {
            if (notifyResultIfExpectedListener()) {
                SatelliteModemInterface.this.mNtnSignalStrengthChangedRegistrants.notifyResult(SatelliteServiceUtils.fromNtnSignalStrength(ntnSignalStrength));
            }
        }

        public void onSatelliteCapabilitiesChanged(SatelliteCapabilities satelliteCapabilities) {
            SatelliteModemInterface.this.mSatelliteCapabilitiesChangedRegistrants.notifyResult(SatelliteServiceUtils.fromSatelliteCapabilities(satelliteCapabilities));
        }

        public void onSatelliteSupportedStateChanged(boolean z) {
            SatelliteModemInterface.this.mSatelliteSupportedStateChangedRegistrants.notifyResult(Boolean.valueOf(z));
        }

        public void onRegistrationFailure(int i) {
            SatelliteModemInterface.this.mSatelliteRegistrationFailureRegistrants.notifyResult(Integer.valueOf(i));
        }

        public void onTerrestrialNetworkAvailableChanged(boolean z) {
            SatelliteModemInterface.this.mTerrestrialNetworkAvailableChangedRegistrants.notifyResult(Boolean.valueOf(z));
        }

        public void prioritizedScanModeChanged(boolean z) {
            Log.d("SatelliteModemInterface", "prioritizedScanModeChanged: " + z);
        }

        private boolean notifyResultIfExpectedListener() {
            return this.mIsDemoListener == SatelliteModemInterface.this.mSatelliteController.isDemoModeEnabled();
        }

        private boolean notifyModemStateChanged(int i) {
            return notifyResultIfExpectedListener() || i == 4 || i == 5;
        }
    }

    public static SatelliteModemInterface getInstance() {
        if (sInstance == null) {
            loge("SatelliteModemInterface was not yet initialized.");
        }
        return sInstance;
    }

    public static SatelliteModemInterface make(Context context, SatelliteController satelliteController, FeatureFlags featureFlags) {
        if (sInstance == null) {
            sInstance = TelephonyComponentFactory.getInstance().inject(SatelliteModemInterface.class.getName()).makeSatelliteInterface(context, satelliteController, Looper.getMainLooper(), featureFlags);
        }
        return sInstance;
    }

    public SatelliteModemInterface(Context context, SatelliteController satelliteController, Looper looper, FeatureFlags featureFlags) {
        this.mPersistentLogger = null;
        this.mPersistentLogger = SatelliteServiceUtils.getPersistentLogger(context);
        this.mContext = context;
        this.mDemoSimulator = DemoSimulator.make(context, satelliteController);
        this.mIsSatelliteServiceSupported.set(getSatelliteServiceSupport());
        this.mSatelliteController = satelliteController;
        this.mFeatureFlags = featureFlags;
        ExponentialBackoff exponentialBackoff = new ExponentialBackoff(2000L, 64000L, 2, looper, new Runnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
        this.mExponentialBackoff = exponentialBackoff;
        exponentialBackoff.start();
        plogd("Created SatelliteModemInterface. Attempting to bind to SatelliteService.");
        bindService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if ((!this.mIsBound.get() || this.mSatelliteService == null) && !this.mIsBinding.get()) {
            if (this.mSatelliteServiceConnection != null) {
                this.mIsBound.set(false);
                this.mIsBinding.set(false);
                unbindService();
            }
            bindService();
        }
    }

    public ISatellite getService() {
        return this.mSatelliteService;
    }

    private String getSatellitePackageName() {
        if (!TextUtils.isEmpty(this.mVendorSatellitePackageName)) {
            return this.mVendorSatellitePackageName;
        }
        return TextUtils.emptyIfNull(this.mContext.getResources().getString(R.string.dump_heap_notification_detail));
    }

    private boolean getSatelliteServiceSupport() {
        return !TextUtils.isEmpty(getSatellitePackageName());
    }

    protected void bindService() {
        if (this.mIsBinding.get() || this.mIsBound.get()) {
            return;
        }
        this.mIsBinding.set(true);
        String satellitePackageName = getSatellitePackageName();
        if (TextUtils.isEmpty(satellitePackageName)) {
            ploge("Unable to bind to the satellite service because the package is undefined.");
            this.mIsBinding.set(false);
            this.mExponentialBackoff.stop();
            return;
        }
        Intent intent = new Intent("android.telephony.satellite.SatelliteService");
        intent.setPackage(satellitePackageName);
        this.mSatelliteServiceConnection = new SatelliteServiceConnection();
        plogd("Binding to " + satellitePackageName);
        try {
            if (this.mContext.bindService(intent, this.mSatelliteServiceConnection, 1)) {
                plogd("Successfully bound to the satellite service.");
                return;
            }
            this.mIsBinding.set(false);
            this.mExponentialBackoff.notifyFailed();
            ploge("Error binding to the satellite service. Retrying in " + this.mExponentialBackoff.getCurrentDelay() + " ms.");
        } catch (Exception e) {
            this.mIsBinding.set(false);
            this.mExponentialBackoff.notifyFailed();
            ploge("Exception binding to the satellite service. Retrying in " + this.mExponentialBackoff.getCurrentDelay() + " ms. Exception: " + e);
        }
    }

    protected void unbindService() {
        disconnectSatelliteService();
        this.mContext.unbindService(this.mSatelliteServiceConnection);
        this.mSatelliteServiceConnection = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnectSatelliteService() {
        this.mSatelliteService = null;
    }

    private class SatelliteServiceConnection implements ServiceConnection {
        private SatelliteServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SatelliteModemInterface.this.plogd("onServiceConnected: ComponentName=" + componentName);
            SatelliteModemInterface.this.mIsBound.set(true);
            SatelliteModemInterface.this.mIsBinding.set(false);
            SatelliteModemInterface.this.mSatelliteService = ISatellite.Stub.asInterface(iBinder);
            SatelliteModemInterface.this.mExponentialBackoff.stop();
            try {
                SatelliteModemInterface.this.mSatelliteService.setSatelliteListener(SatelliteModemInterface.this.mVendorListener);
                SatelliteModemInterface.this.mDemoSimulator.setSatelliteListener(SatelliteModemInterface.this.mDemoListener);
            } catch (RemoteException e) {
                SatelliteModemInterface.this.plogd("setSatelliteListener: RemoteException " + e);
            }
            SatelliteModemInterface.this.mSatelliteController.onSatelliteServiceConnected();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            SatelliteModemInterface.this.ploge("onServiceDisconnected: Waiting for reconnect.");
            SatelliteModemInterface.this.mIsBinding.set(false);
            SatelliteModemInterface.this.disconnectSatelliteService();
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            SatelliteModemInterface.this.ploge("onBindingDied: Unbinding and rebinding service.");
            SatelliteModemInterface.this.mIsBound.set(false);
            SatelliteModemInterface.this.mIsBinding.set(false);
            SatelliteModemInterface.this.unbindService();
            SatelliteModemInterface.this.mExponentialBackoff.start();
        }
    }

    public void registerForSatellitePositionInfoChanged(Handler handler, int i, Object obj) {
        this.mSatellitePositionInfoChangedRegistrants.add(handler, i, obj);
    }

    public void unregisterForSatellitePositionInfoChanged(Handler handler) {
        this.mSatellitePositionInfoChangedRegistrants.remove(handler);
    }

    public void registerForDatagramTransferStateChanged(Handler handler, int i, Object obj) {
        this.mDatagramTransferStateChangedRegistrants.add(handler, i, obj);
    }

    public void unregisterForDatagramTransferStateChanged(Handler handler) {
        this.mDatagramTransferStateChangedRegistrants.remove(handler);
    }

    public void registerForSatelliteModemStateChanged(Handler handler, int i, Object obj) {
        this.mSatelliteModemStateChangedRegistrants.add(handler, i, obj);
    }

    public void unregisterForSatelliteModemStateChanged(Handler handler) {
        this.mSatelliteModemStateChangedRegistrants.remove(handler);
    }

    public void registerForPendingDatagrams(Handler handler, int i, Object obj) {
        this.mPendingDatagramsRegistrants.add(handler, i, obj);
    }

    public void unregisterForPendingDatagrams(Handler handler) {
        this.mPendingDatagramsRegistrants.remove(handler);
    }

    public void registerForSatelliteDatagramsReceived(Handler handler, int i, Object obj) {
        this.mSatelliteDatagramsReceivedRegistrants.add(handler, i, obj);
    }

    public void unregisterForSatelliteDatagramsReceived(Handler handler) {
        this.mSatelliteDatagramsReceivedRegistrants.remove(handler);
    }

    public void registerForNtnSignalStrengthChanged(Handler handler, int i, Object obj) {
        this.mNtnSignalStrengthChangedRegistrants.add(handler, i, obj);
    }

    public void unregisterForNtnSignalStrengthChanged(Handler handler) {
        this.mNtnSignalStrengthChangedRegistrants.remove(handler);
    }

    public void registerForSatelliteCapabilitiesChanged(Handler handler, int i, Object obj) {
        this.mSatelliteCapabilitiesChangedRegistrants.add(handler, i, obj);
    }

    public void unregisterForSatelliteCapabilitiesChanged(Handler handler) {
        this.mSatelliteCapabilitiesChangedRegistrants.remove(handler);
    }

    public void registerForSatelliteSupportedStateChanged(Handler handler, int i, Object obj) {
        this.mSatelliteSupportedStateChangedRegistrants.add(handler, i, obj);
    }

    public void unregisterForSatelliteSupportedStateChanged(Handler handler) {
        this.mSatelliteSupportedStateChangedRegistrants.remove(handler);
    }

    public void registerForSatelliteRegistrationFailure(Handler handler, int i, Object obj) {
        this.mSatelliteRegistrationFailureRegistrants.add(handler, i, obj);
    }

    public void unregisterForSatelliteRegistrationFailure(Handler handler) {
        this.mSatelliteRegistrationFailureRegistrants.remove(handler);
    }

    public void registerForTerrestrialNetworkAvailableChanged(Handler handler, int i, Object obj) {
        this.mTerrestrialNetworkAvailableChangedRegistrants.add(handler, i, obj);
    }

    public void unregisterForTerrestrialNetworkAvailableChanged(Handler handler) {
        this.mTerrestrialNetworkAvailableChangedRegistrants.remove(handler);
    }

    public void requestSatelliteListeningEnabled(boolean z, int i, Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestSatelliteListeningEnabled(z, i, new AnonymousClass1(message));
                return;
            } catch (RemoteException e) {
                ploge("requestSatelliteListeningEnabled: RemoteException " + e);
                if (message != null) {
                    sendMessageWithResult(message, null, 3);
                    return;
                }
                return;
            }
        }
        ploge("requestSatelliteListeningEnabled: Satellite service is unavailable.");
        if (message != null) {
            sendMessageWithResult(message, null, 10);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$1, reason: invalid class name */
    class AnonymousClass1 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass1(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestSatelliteListeningEnabled: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$1$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.AnonymousClass1.m1614$r8$lambda$ipGJhyElQT1tUhH8avBW6eaEo(message, iFromSatelliteError);
                }
            });
        }

        /* JADX INFO: renamed from: $r8$lambda$ipGJ--hyElQT1tUhH8avBW6eaEo, reason: not valid java name */
        public static /* synthetic */ void m1614$r8$lambda$ipGJhyElQT1tUhH8avBW6eaEo(Message message, int i) {
            if (message != null) {
                SatelliteModemInterface.sendMessageWithResult(message, null, i);
            }
        }
    }

    public void enableCellularModemWhileSatelliteModeIsOn(boolean z, Message message) {
        if (this.mSatelliteService != null) {
            try {
                IIntegerConsumer anonymousClass2 = new AnonymousClass2(message);
                if (this.mSatelliteController.isDemoModeEnabled()) {
                    this.mDemoSimulator.enableTerrestrialNetworkScanWhileSatelliteModeIsOn(z, anonymousClass2);
                    return;
                } else {
                    this.mSatelliteService.enableTerrestrialNetworkScanWhileSatelliteModeIsOn(z, anonymousClass2);
                    return;
                }
            } catch (RemoteException e) {
                ploge("enableTerrestrialNetworkScanWhileSatelliteModeIsOn: RemoteException " + e);
                if (message != null) {
                    sendMessageWithResult(message, null, 3);
                    return;
                }
                return;
            }
        }
        ploge("enableCellularModemWhileSatelliteModeIsOn: Satellite service is unavailable.");
        if (message != null) {
            sendMessageWithResult(message, null, 10);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$2, reason: invalid class name */
    class AnonymousClass2 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass2(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("enableCellularModemWhileSatelliteModeIsOn: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$2$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.AnonymousClass2.$r8$lambda$mRYVud4NImvUUkv9tb_u3dgTJtg(message, iFromSatelliteError);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$mRYVud4NImvUUkv9tb_u3dgTJtg(Message message, int i) {
            if (message != null) {
                SatelliteModemInterface.sendMessageWithResult(message, null, i);
            }
        }
    }

    public void requestSatelliteSuspended(boolean z, Message message) {
        if (this.mSatelliteService != null) {
            try {
                IIntegerConsumer anonymousClass3 = new AnonymousClass3(message);
                if (this.mSatelliteController.isDemoModeEnabled()) {
                    this.mDemoSimulator.requestSatelliteSuspended(z, anonymousClass3);
                    return;
                } else {
                    this.mSatelliteService.requestSatelliteSuspended(z, anonymousClass3);
                    return;
                }
            } catch (RemoteException e) {
                ploge("requestSatelliteSuspended: RemoteException " + e);
                if (message != null) {
                    sendMessageWithResult(message, null, 3);
                    return;
                }
                return;
            }
        }
        ploge("requestSatelliteSuspended: Satellite service is unavailable.");
        if (message != null) {
            sendMessageWithResult(message, null, 10);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$3, reason: invalid class name */
    class AnonymousClass3 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass3(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestSatelliteSuspended: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$3$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.AnonymousClass3.$r8$lambda$t9sPK82xoQn7R6CjBgg9xH10yzc(message, iFromSatelliteError);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$t9sPK82xoQn7R6CjBgg9xH10yzc(Message message, int i) {
            if (message != null) {
                SatelliteModemInterface.sendMessageWithResult(message, null, i);
            }
        }
    }

    public void requestSatelliteEnabled(SatelliteModemEnableRequestAttributes satelliteModemEnableRequestAttributes, Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestSatelliteEnabled(SatelliteServiceUtils.toSatelliteModemEnableRequestAttributes(satelliteModemEnableRequestAttributes), new AnonymousClass4(message));
                return;
            } catch (RemoteException e) {
                ploge("setSatelliteEnabled: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("setSatelliteEnabled: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$4, reason: invalid class name */
    class AnonymousClass4 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass4(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("setSatelliteEnabled: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$4$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void requestIsSatelliteEnabled(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestIsSatelliteEnabled(new AnonymousClass5(message), new AnonymousClass6(message));
                return;
            } catch (RemoteException e) {
                ploge("requestIsSatelliteEnabled: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("requestIsSatelliteEnabled: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$5, reason: invalid class name */
    class AnonymousClass5 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass5(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestIsSatelliteEnabled: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$5$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$6, reason: invalid class name */
    class AnonymousClass6 extends IBooleanConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass6(Message message) {
            this.val$message = message;
        }

        public void accept(boolean z) {
            final int[] iArr = {z ? 1 : 0};
            SatelliteModemInterface.this.plogd("requestIsSatelliteEnabled: " + Arrays.toString(iArr));
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$6$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, iArr, 0);
                }
            });
        }
    }

    public void requestIsSatelliteSupported(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestIsSatelliteSupported(new AnonymousClass7(message), new AnonymousClass8(message));
                return;
            } catch (RemoteException e) {
                ploge("requestIsSatelliteSupported: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("requestIsSatelliteSupported: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$7, reason: invalid class name */
    class AnonymousClass7 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass7(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestIsSatelliteSupported: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$7$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$8, reason: invalid class name */
    class AnonymousClass8 extends IBooleanConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass8(Message message) {
            this.val$message = message;
        }

        public void accept(final boolean z) {
            SatelliteModemInterface.this.plogd("requestIsSatelliteSupported: " + z);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$8$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, Boolean.valueOf(z), 0);
                }
            });
        }
    }

    public void requestSatelliteCapabilities(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestSatelliteCapabilities(new AnonymousClass9(message), new AnonymousClass10(message));
                return;
            } catch (RemoteException e) {
                ploge("requestSatelliteCapabilities: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("requestSatelliteCapabilities: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$9, reason: invalid class name */
    class AnonymousClass9 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass9(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestSatelliteCapabilities: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$9$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$10, reason: invalid class name */
    class AnonymousClass10 extends ISatelliteCapabilitiesConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass10(Message message) {
            this.val$message = message;
        }

        public void accept(SatelliteCapabilities satelliteCapabilities) {
            final android.telephony.satellite.SatelliteCapabilities satelliteCapabilitiesFromSatelliteCapabilities = SatelliteServiceUtils.fromSatelliteCapabilities(satelliteCapabilities);
            SatelliteModemInterface.this.plogd("requestSatelliteCapabilities: " + satelliteCapabilitiesFromSatelliteCapabilities);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$10$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, satelliteCapabilitiesFromSatelliteCapabilities, 0);
                }
            });
        }
    }

    public void startSendingSatellitePointingInfo(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.startSendingSatellitePointingInfo(new AnonymousClass11(message));
                return;
            } catch (RemoteException e) {
                ploge("startSendingSatellitePointingInfo: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("startSendingSatellitePointingInfo: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$11, reason: invalid class name */
    class AnonymousClass11 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass11(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("startSendingSatellitePointingInfo: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$11$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void stopSendingSatellitePointingInfo(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.stopSendingSatellitePointingInfo(new AnonymousClass12(message));
                return;
            } catch (RemoteException e) {
                ploge("stopSendingSatellitePointingInfo: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("stopSendingSatellitePointingInfo: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$12, reason: invalid class name */
    class AnonymousClass12 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass12(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("stopSendingSatellitePointingInfo: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$12$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void pollPendingSatelliteDatagrams(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.pollPendingSatelliteDatagrams(new AnonymousClass13(message));
                return;
            } catch (RemoteException e) {
                ploge("pollPendingDatagrams: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("pollPendingDatagrams: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$13, reason: invalid class name */
    class AnonymousClass13 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass13(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("pollPendingDatagrams: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$13$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void sendSatelliteDatagram(android.telephony.satellite.SatelliteDatagram satelliteDatagram, boolean z, boolean z2, Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.sendSatelliteDatagram(SatelliteServiceUtils.toSatelliteDatagram(satelliteDatagram), z, new AnonymousClass14(message));
                return;
            } catch (RemoteException e) {
                ploge("sendDatagram: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("sendDatagram: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$14, reason: invalid class name */
    class AnonymousClass14 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass14(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("sendDatagram: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$14$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void requestSatelliteModemState(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestSatelliteModemState(new AnonymousClass15(message), new AnonymousClass16(message));
                return;
            } catch (RemoteException e) {
                ploge("requestSatelliteModemState: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("requestSatelliteModemState: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$15, reason: invalid class name */
    class AnonymousClass15 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass15(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestSatelliteModemState: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$15$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$16, reason: invalid class name */
    class AnonymousClass16 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass16(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteModemState = SatelliteServiceUtils.fromSatelliteModemState(i);
            SatelliteModemInterface.this.plogd("requestSatelliteModemState: " + iFromSatelliteModemState);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$16$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, Integer.valueOf(iFromSatelliteModemState), 0);
                }
            });
        }
    }

    public void requestTimeForNextSatelliteVisibility(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestTimeForNextSatelliteVisibility(new AnonymousClass17(message), new AnonymousClass18(message));
                return;
            } catch (RemoteException e) {
                ploge("requestTimeForNextSatelliteVisibility: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("requestTimeForNextSatelliteVisibility: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$17, reason: invalid class name */
    class AnonymousClass17 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass17(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestTimeForNextSatelliteVisibility: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$17$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$18, reason: invalid class name */
    class AnonymousClass18 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass18(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int[] iArr = {i};
            SatelliteModemInterface.this.plogd("requestTimeForNextSatelliteVisibility: " + Arrays.toString(iArr));
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$18$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, iArr, 0);
                }
            });
        }
    }

    public void setSatellitePlmn(int i, List<String> list, List<String> list2, Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.setSatellitePlmn(i, list, list2, new AnonymousClass19(message));
                return;
            } catch (RemoteException e) {
                ploge("setSatellitePlmn: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("setSatellitePlmn: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 11);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$19, reason: invalid class name */
    class AnonymousClass19 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass19(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("setSatellitePlmn: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$19$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void requestSetSatelliteEnabledForCarrier(int i, boolean z, Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.setSatelliteEnabledForCarrier(i, z, new AnonymousClass20(message));
                return;
            } catch (RemoteException e) {
                ploge("requestSetSatelliteEnabledForCarrier: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("requestSetSatelliteEnabledForCarrier: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 11);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$20, reason: invalid class name */
    class AnonymousClass20 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass20(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestSetSatelliteEnabledForCarrier: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$20$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void requestIsSatelliteEnabledForCarrier(int i, Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestIsSatelliteEnabledForCarrier(i, new AnonymousClass21(message), new AnonymousClass22(message));
                return;
            } catch (RemoteException e) {
                ploge("requestIsSatelliteEnabledForCarrier: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("requestIsSatelliteEnabledForCarrier: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 11);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$21, reason: invalid class name */
    class AnonymousClass21 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass21(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestIsSatelliteEnabledForCarrier: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$21$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$22, reason: invalid class name */
    class AnonymousClass22 extends IBooleanConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass22(Message message) {
            this.val$message = message;
        }

        public void accept(final boolean z) {
            SatelliteModemInterface.this.plogd("requestIsSatelliteEnabledForCarrier: " + z);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$22$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, Boolean.valueOf(z), 0);
                }
            });
        }
    }

    public void requestNtnSignalStrength(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.requestSignalStrength(new AnonymousClass23(message), new AnonymousClass24(message));
                return;
            } catch (RemoteException e) {
                ploge("requestNtnSignalStrength: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("requestNtnSignalStrength: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$23, reason: invalid class name */
    class AnonymousClass23 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass23(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("requestNtnSignalStrength: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$23$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$24, reason: invalid class name */
    class AnonymousClass24 extends INtnSignalStrengthConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass24(Message message) {
            this.val$message = message;
        }

        public void accept(NtnSignalStrength ntnSignalStrength) {
            final android.telephony.satellite.NtnSignalStrength ntnSignalStrengthFromNtnSignalStrength = SatelliteServiceUtils.fromNtnSignalStrength(ntnSignalStrength);
            SatelliteModemInterface.this.plogd("requestNtnSignalStrength: " + ntnSignalStrengthFromNtnSignalStrength);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$24$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, ntnSignalStrengthFromNtnSignalStrength, 0);
                }
            });
        }
    }

    public void startSendingNtnSignalStrength(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.startSendingNtnSignalStrength(new AnonymousClass25(message));
                return;
            } catch (RemoteException e) {
                ploge("startSendingNtnSignalStrength: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("startSendingNtnSignalStrength: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$25, reason: invalid class name */
    class AnonymousClass25 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass25(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("startSendingNtnSignalStrength: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$25$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void stopSendingNtnSignalStrength(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.stopSendingNtnSignalStrength(new AnonymousClass26(message));
                return;
            } catch (RemoteException e) {
                ploge("stopSendingNtnSignalStrength: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("stopSendingNtnSignalStrength: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$26, reason: invalid class name */
    class AnonymousClass26 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass26(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("stopSendingNtnSignalStrength: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$26$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void abortSendingSatelliteDatagrams(Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.abortSendingSatelliteDatagrams(new AnonymousClass27(message));
                return;
            } catch (RemoteException e) {
                ploge("abortSendingSatelliteDatagrams: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("abortSendingSatelliteDatagrams: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$27, reason: invalid class name */
    class AnonymousClass27 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass27(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("abortSendingSatelliteDatagrams: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$27$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public boolean isSatelliteServiceSupported() {
        return this.mIsSatelliteServiceSupported.get();
    }

    public boolean isSatelliteServiceConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSatelliteService != null;
        }
        return z;
    }

    public void updateSatelliteSubscription(String str, Message message) {
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.updateSatelliteSubscription(str, new AnonymousClass28(message));
                return;
            } catch (RemoteException e) {
                ploge("updateSatelliteSubscription: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("updateSatelliteSubscription: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$28, reason: invalid class name */
    class AnonymousClass28 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass28(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("updateSatelliteSubscription: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$28$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void setSatelliteServicePackageName(String str) {
        plogd("setSatelliteServicePackageName: config_satellite_service_package is updated, new packageName=" + str);
        this.mExponentialBackoff.stop();
        if (this.mSatelliteServiceConnection != null) {
            this.mIsBound.set(false);
            this.mIsBinding.set(false);
            unbindService();
        }
        if (str == null || str.equals("null")) {
            this.mVendorSatellitePackageName = "";
        } else {
            this.mVendorSatellitePackageName = str;
        }
        this.mIsSatelliteServiceSupported.set(getSatelliteServiceSupport());
        bindService();
        this.mExponentialBackoff.start();
    }

    public void updateSystemSelectionChannels(List<SystemSelectionSpecifier> list, Message message) {
        plogd("updateSystemSelectionChannels: SystemSelectionSpecifier: " + list.toString());
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.updateSystemSelectionChannels(SatelliteServiceUtils.toSystemSelectionSpecifier(list), new AnonymousClass29(message));
                return;
            } catch (RemoteException e) {
                ploge("updateSystemSelectionChannels: RemoteException " + e);
                sendMessageWithResult(message, null, 3);
                return;
            }
        }
        ploge("updateSystemSelectionChannels: Satellite service is unavailable.");
        sendMessageWithResult(message, null, 10);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$29, reason: invalid class name */
    class AnonymousClass29 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass29(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("updateSystemSelectionChannels: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$29$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.sendMessageWithResult(message, null, iFromSatelliteError);
                }
            });
        }
    }

    public void setSatelliteNetworkInfo(int i, android.hardware.radio.network.SatelliteNetworkInfo satelliteNetworkInfo, Message message) {
        plogd("setSatelliteNetworkInfo: simSlot=" + i);
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.setSatelliteNetworkInfo(i, SatelliteServiceUtils.toStubSatelliteNetworkInfo(satelliteNetworkInfo), new AnonymousClass30(message));
                return;
            } catch (RemoteException e) {
                ploge("setSatelliteNetworkInfo: RemoteException " + e);
                if (message != null) {
                    sendMessageWithResult(message, null, 3);
                    return;
                }
                return;
            }
        }
        ploge("setSatelliteNetworkInfo: Satellite service is unavailable.");
        if (message != null) {
            sendMessageWithResult(message, null, 11);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$30, reason: invalid class name */
    class AnonymousClass30 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass30(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("setSatelliteNetworkInfo: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$30$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.AnonymousClass30.$r8$lambda$e0xR8QF_icCEElVHclyQh8azC9g(message, iFromSatelliteError);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$e0xR8QF_icCEElVHclyQh8azC9g(Message message, int i) {
            if (message != null) {
                SatelliteModemInterface.sendMessageWithResult(message, null, i);
            }
        }
    }

    public void enablePrioritizedNetworkScan(int i, PrioritizedNetworkScanRequest prioritizedNetworkScanRequest, Message message) {
        plogd("enablePrioritizedNetworkScan: simSlot=" + i);
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.enablePrioritizedNetworkScan(i, SatelliteServiceUtils.toStubPrioritizedNetworkScanRequest(prioritizedNetworkScanRequest), new AnonymousClass31(message));
                return;
            } catch (RemoteException e) {
                ploge("enablePrioritizedNetworkScan: RemoteException " + e);
                if (message != null) {
                    sendMessageWithResult(message, null, 3);
                    return;
                }
                return;
            }
        }
        ploge("enablePrioritizedNetworkScan: Satellite service is unavailable.");
        if (message != null) {
            sendMessageWithResult(message, null, 11);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$31, reason: invalid class name */
    class AnonymousClass31 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass31(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("enablePrioritizedNetworkScan: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$31$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.AnonymousClass31.m1621$r8$lambda$rxitBO5jRA1Ozl7dOpre4fC6UU(message, iFromSatelliteError);
                }
            });
        }

        /* JADX INFO: renamed from: $r8$lambda$r-xitBO5jRA1Ozl7dOpre4fC6UU, reason: not valid java name */
        public static /* synthetic */ void m1621$r8$lambda$rxitBO5jRA1Ozl7dOpre4fC6UU(Message message, int i) {
            if (message != null) {
                SatelliteModemInterface.sendMessageWithResult(message, null, i);
            }
        }
    }

    public void disablePrioritizedNetworkScan(int i, Message message) {
        plogd("disablePrioritizedNetworkScan: simSlot=" + i);
        ISatellite iSatellite = this.mSatelliteService;
        if (iSatellite != null) {
            try {
                iSatellite.disablePrioritizedNetworkScan(i, new AnonymousClass32(message));
                return;
            } catch (RemoteException e) {
                ploge("disablePrioritizedNetworkScan: RemoteException " + e);
                if (message != null) {
                    sendMessageWithResult(message, null, 3);
                    return;
                }
                return;
            }
        }
        ploge("disablePrioritizedNetworkScan: Satellite service is unavailable.");
        if (message != null) {
            sendMessageWithResult(message, null, 11);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteModemInterface$32, reason: invalid class name */
    class AnonymousClass32 extends IIntegerConsumer.Stub {
        final /* synthetic */ Message val$message;

        AnonymousClass32(Message message) {
            this.val$message = message;
        }

        public void accept(int i) {
            final int iFromSatelliteError = SatelliteServiceUtils.fromSatelliteError(i);
            SatelliteModemInterface.this.plogd("disablePrioritizedNetworkScan: " + iFromSatelliteError);
            final Message message = this.val$message;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.satellite.SatelliteModemInterface$32$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    SatelliteModemInterface.AnonymousClass32.$r8$lambda$ml0OyZAT4wlmuNZLhPAY74PrpLE(message, iFromSatelliteError);
                }
            });
        }

        public static /* synthetic */ void $r8$lambda$ml0OyZAT4wlmuNZLhPAY74PrpLE(Message message, int i) {
            if (message != null) {
                SatelliteModemInterface.sendMessageWithResult(message, null, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void sendMessageWithResult(Message message, Object obj, int i) {
        AsyncResult.forMessage(message, obj, i == 0 ? null : new SatelliteManager.SatelliteException(i));
        message.sendToTarget();
    }

    private static void loge(String str) {
        Log.e("SatelliteModemInterface", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void plogd(String str) {
        Log.d("SatelliteModemInterface", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.debug("SatelliteModemInterface", str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ploge(String str) {
        Log.e("SatelliteModemInterface", str);
        PersistentLogger persistentLogger = this.mPersistentLogger;
        if (persistentLogger != null) {
            persistentLogger.error("SatelliteModemInterface", str);
        }
    }
}
