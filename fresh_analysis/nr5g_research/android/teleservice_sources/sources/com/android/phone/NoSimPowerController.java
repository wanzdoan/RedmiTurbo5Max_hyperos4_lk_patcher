package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;

/* JADX INFO: loaded from: classes.dex */
public class NoSimPowerController extends Handler {
    private static volatile NoSimPowerController sInstance;
    private final Context mContext;
    private DisableReason mDisableReason;
    private final BaseEvent.IEventListener mEventListener;
    private final NoSimPowerExecutor mExecutor;
    private boolean mIsScreenOn;
    private boolean mIsSleepMode;
    private final BroadcastReceiver mReceiver;
    private volatile boolean mStarted;

    enum DisableReason {
        NONE,
        SLEEP,
        SCREEN_OFF
    }

    public static void init(Context context) throws Throwable {
        if (context == null) {
            log("init failed: context is null");
            return;
        }
        if (sInstance == null) {
            synchronized (NoSimPowerController.class) {
                try {
                    if (sInstance == null) {
                        HandlerThread handlerThread = new HandlerThread("NoSimPowerThread");
                        handlerThread.start();
                        sInstance = new NoSimPowerController(context, handlerThread.getLooper());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        sInstance.start();
    }

    private NoSimPowerController(Context context, Looper looper) {
        super(looper);
        this.mDisableReason = DisableReason.NONE;
        this.mEventListener = new AnonymousClass1();
        this.mReceiver = new AnonymousClass2();
        this.mContext = context;
        this.mExecutor = NoSimPowerExecutor.make();
        this.mIsScreenOn = MiuiPhoneUtils.isScreenOn(context);
    }

    private static void log(String str) {
        Rlog.d("NoSimPowerController", str);
    }

    private synchronized void start() throws Throwable {
        try {
            try {
                if (this.mStarted) {
                    return;
                }
                this.mStarted = true;
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.miui.powerkeeper_sleep_changed");
                this.mContext.registerReceiver(this.mReceiver, intentFilter, null, this, 2);
                registerRilEvents();
                registerCommonEvents();
                post(new Runnable() { // from class: com.android.phone.NoSimPowerController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$start$0();
                    }
                });
                log("started");
            } catch (Throwable th) {
                th = th;
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0() {
        reevaluateNoSimPolicy("start");
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            handleModemReset();
        } else if (i == 2) {
            reevaluateNoSimPolicy("radio available");
        } else {
            if (i != 3) {
                return;
            }
            handleDelayedScreenOffPolicy((String) message.obj);
        }
    }

    /* JADX INFO: renamed from: com.android.phone.NoSimPowerController$1, reason: invalid class name */
    class AnonymousClass1 implements BaseEvent.IEventListener {
        AnonymousClass1() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                final DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                if (displayEvent.isDefaultDisplay()) {
                    NoSimPowerController.this.post(new Runnable() { // from class: com.android.phone.NoSimPowerController$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onEvent$0(displayEvent);
                        }
                    });
                    return;
                }
                return;
            }
            if (baseEvent instanceof TelephonyEvent.SimEvent) {
                NoSimPowerController.this.post(new Runnable() { // from class: com.android.phone.NoSimPowerController$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onEvent$1();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEvent$0(DeviceStateEvent.DisplayEvent displayEvent) {
            NoSimPowerController.this.handleScreenChanged(displayEvent.isScreenOn());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEvent$1() {
            NoSimPowerController.this.handleSimStateChanged();
        }
    }

    /* JADX INFO: renamed from: com.android.phone.NoSimPowerController$2, reason: invalid class name */
    class AnonymousClass2 extends BroadcastReceiver {
        AnonymousClass2() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || !"com.miui.powerkeeper_sleep_changed".equals(intent.getAction())) {
                return;
            }
            final boolean z = 1 == intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
            NoSimPowerController.this.post(new Runnable() { // from class: com.android.phone.NoSimPowerController$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(boolean z) {
            NoSimPowerController.this.handleSleepModeChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSleepModeChanged(boolean z) {
        if (this.mStarted) {
            this.mIsSleepMode = z;
            if (!isNoSim()) {
                resetAll("sim present on sleep changed");
                return;
            }
            if (z) {
                cancelDelayedScreenOffPolicy();
                disableNoSimSearch(DisableReason.SLEEP, "sleep enter");
                return;
            }
            if (this.mDisableReason == DisableReason.SLEEP) {
                restoreNoSimSearch("sleep exit");
            }
            if (this.mIsScreenOn) {
                return;
            }
            scheduleDelayedScreenOffPolicy("sleep exit");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenChanged(boolean z) {
        if (this.mStarted) {
            this.mIsScreenOn = z;
            if (!isNoSim()) {
                resetAll("sim present on screen changed");
                return;
            }
            if (z) {
                cancelDelayedScreenOffPolicy();
                if (this.mIsSleepMode) {
                    this.mIsSleepMode = false;
                    if (this.mDisableReason == DisableReason.SLEEP) {
                        restoreNoSimSearch("screen on sleep exit");
                    }
                }
                if (this.mDisableReason == DisableReason.SCREEN_OFF) {
                    restoreNoSimSearch("screen on");
                    return;
                }
                return;
            }
            if (this.mIsSleepMode) {
                return;
            }
            scheduleDelayedScreenOffPolicy("screen off");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimStateChanged() {
        if (this.mStarted) {
            if (!isNoSim()) {
                resetAll("sim inserted");
            } else {
                reevaluateNoSimPolicy("sim removed");
            }
        }
    }

    private void handleModemReset() {
        if (this.mStarted) {
            this.mDisableReason = DisableReason.NONE;
            cancelDelayedScreenOffPolicy();
            log("handleModemReset, wait radio available");
        }
    }

    private void reevaluateNoSimPolicy(String str) {
        if (this.mStarted) {
            if (!isNoSim()) {
                resetAll(str);
                return;
            }
            if (this.mIsSleepMode) {
                cancelDelayedScreenOffPolicy();
                disableNoSimSearch(DisableReason.SLEEP, str);
            } else if (!this.mIsScreenOn) {
                scheduleDelayedScreenOffPolicy(str);
            } else {
                resetAll(str);
            }
        }
    }

    private void disableNoSimSearch(DisableReason disableReason, String str) {
        DisableReason disableReason2 = this.mDisableReason;
        if (disableReason2 == disableReason) {
            return;
        }
        if (disableReason2 != DisableReason.NONE && disableReason2 != disableReason) {
            restoreNoSimSearch("switch reason from " + this.mDisableReason + " to " + disableReason);
        }
        Phone defaultPhone = PhoneFactory.getDefaultPhone();
        if (disableReason == DisableReason.SLEEP) {
            this.mExecutor.applyNoSimSleepSearchPolicy(defaultPhone);
        } else if (disableReason == DisableReason.SCREEN_OFF) {
            this.mExecutor.setNoSimRecoveryEnabled(defaultPhone, true);
        }
        this.mExecutor.setNoSimSkipImsEccSearch(defaultPhone, true);
        this.mDisableReason = disableReason;
        log("disableNoSimSearch reason=" + disableReason + ", logReason=" + str);
    }

    private void restoreNoSimSearch(String str) {
        DisableReason disableReason = this.mDisableReason;
        DisableReason disableReason2 = DisableReason.NONE;
        if (disableReason == disableReason2) {
            return;
        }
        Phone defaultPhone = PhoneFactory.getDefaultPhone();
        this.mExecutor.setNoSimSkipImsEccSearch(defaultPhone, false);
        DisableReason disableReason3 = this.mDisableReason;
        if (disableReason3 == DisableReason.SLEEP) {
            this.mExecutor.restoreNoSimSleepSearchPolicy(defaultPhone);
        } else if (disableReason3 == DisableReason.SCREEN_OFF) {
            this.mExecutor.setNoSimRecoveryEnabled(defaultPhone, false);
        }
        log("restoreNoSimSearch reason=" + str + ", disableReason=" + this.mDisableReason);
        this.mDisableReason = disableReason2;
    }

    private void resetAll(String str) {
        cancelDelayedScreenOffPolicy();
        restoreNoSimSearch(str);
    }

    private void scheduleDelayedScreenOffPolicy(String str) {
        if (hasMessages(3)) {
            log("delayed screen off policy already scheduled reason=" + str);
        } else {
            sendMessageDelayed(obtainMessage(3, str), MirrorDesktopHelper.TIMEOUT_MILLIS);
            log("scheduleDelayedScreenOffPolicy reason=" + str);
        }
    }

    private void cancelDelayedScreenOffPolicy() {
        removeMessages(3);
    }

    private void handleDelayedScreenOffPolicy(String str) {
        if (!this.mStarted || !isNoSim() || this.mIsScreenOn || this.mIsSleepMode) {
            log("skip delayed screen off policy reason=" + str);
            return;
        }
        disableNoSimSearch(DisableReason.SCREEN_OFF, str);
    }

    private void registerRilEvents() {
        Phone defaultPhone = PhoneFactory.getDefaultPhone();
        if (defaultPhone == null) {
            log("registerRilEvents phone is null");
            return;
        }
        CommandsInterface commandsInterface = defaultPhone.mCi;
        if (commandsInterface != null) {
            commandsInterface.registerForModemReset(this, 1, (Object) null);
            commandsInterface.registerForAvailable(this, 2, (Object) null);
        }
    }

    private void registerCommonEvents() {
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mEventListener, this);
        EventHubManager.getInstance().registerListener("telephony", "icc_changed", this.mEventListener, this);
    }

    private boolean isNoSim() {
        return MiuiPhoneUtils.getInsertedSimCount() == 0;
    }
}
