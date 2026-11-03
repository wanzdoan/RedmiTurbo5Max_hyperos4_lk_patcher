package com.android.internal.telephony.uicc;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class UiccCardApplication {
    public static final int AUTHTYPE_GBA_NAF_KEY_EXTERNAL = 133;
    public static final int AUTH_CONTEXT_EAP_AKA = 129;
    public static final int AUTH_CONTEXT_EAP_SIM = 128;
    public static final int AUTH_CONTEXT_GBA_BOOTSTRAP = 132;
    public static final int AUTH_CONTEXT_UNDEFINED = -1;
    protected static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 7;
    protected static final int EVENT_PIN1_PUK1_DONE = 1;
    protected static final int EVENT_QUERY_FACILITY_FDN_DONE = 4;
    protected static final int EVENT_QUERY_FACILITY_LOCK_DONE = 6;
    protected static final String LOG_TAG = "UiccCardApplication";
    protected String mAid;
    protected String mAppLabel;
    protected IccCardApplicationStatus.AppState mAppState;
    protected IccCardApplicationStatus.AppType mAppType;
    protected int mAuthContext;
    protected CommandsInterface mCi;
    protected Context mContext;
    private boolean mDesiredFdnEnabled;
    protected boolean mDesiredPinLocked;
    protected boolean mDestroyed;
    private final FeatureFlags mFeatureFlags;
    private boolean mIccFdnEnabled;
    protected IccFileHandler mIccFh;
    private boolean mIccLockEnabled;
    protected IccRecords mIccRecords;
    private boolean mIgnoreApp;
    protected IccCardApplicationStatus.PersoSubState mPersoSubState;
    protected boolean mPin1Replaced;
    protected IccCardStatus.PinState mPin1State;
    protected IccCardStatus.PinState mPin2State;
    private UiccProfile mUiccProfile;
    protected final Object mLock = new Object();
    private boolean mIccFdnAvailable = true;
    private RegistrantList mReadyRegistrants = new RegistrantList();
    private RegistrantList mDetectedRegistrants = new RegistrantList();
    private RegistrantList mPinLockedRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedRegistrants = new RegistrantList();
    protected Handler mHandler = new Handler() { // from class: com.android.internal.telephony.uicc.UiccCardApplication.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult;
            UiccCardApplication uiccCardApplication = UiccCardApplication.this;
            if (uiccCardApplication.mDestroyed) {
                uiccCardApplication.loge("Received message " + message + "[" + message.what + "] while being destroyed. Ignoring.");
                int i = message.what;
                if ((i == 1 || i == 7) && (asyncResult = (AsyncResult) message.obj) != null) {
                    asyncResult.exception = new CommandException(CommandException.Error.ABORTED);
                    Message message2 = (Message) asyncResult.userObj;
                    if (message2 != null) {
                        AsyncResult.forMessage(message2).exception = asyncResult.exception;
                        message2.sendToTarget();
                    }
                    return;
                }
                return;
            }
            switch (message.what) {
                case 1:
                case 2:
                case 3:
                case 8:
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    int pinPukErrorResult = (asyncResult2.exception == null || asyncResult2.result == null) ? -1 : uiccCardApplication.parsePinPukErrorResult(asyncResult2);
                    Message message3 = (Message) asyncResult2.userObj;
                    AsyncResult.forMessage(message3).exception = asyncResult2.exception;
                    message3.arg1 = pinPukErrorResult;
                    message3.sendToTarget();
                    break;
                case 4:
                    uiccCardApplication.onQueryFdnEnabled((AsyncResult) message.obj);
                    break;
                case 5:
                    uiccCardApplication.onChangeFdnDone((AsyncResult) message.obj);
                    break;
                case 6:
                    uiccCardApplication.onQueryFacilityLock((AsyncResult) message.obj);
                    break;
                case 7:
                    uiccCardApplication.onChangeFacilityLock((AsyncResult) message.obj);
                    break;
                case 9:
                    uiccCardApplication.log("handleMessage (EVENT_RADIO_UNAVAILABLE)");
                    UiccCardApplication.this.mAppState = IccCardApplicationStatus.AppState.APPSTATE_UNKNOWN;
                    break;
                default:
                    uiccCardApplication.loge("Unknown Event " + message.what);
                    break;
            }
        }
    };

    public UiccCardApplication(UiccProfile uiccProfile, IccCardApplicationStatus iccCardApplicationStatus, Context context, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        this.mFeatureFlags = featureFlags;
        this.mUiccProfile = uiccProfile;
        this.mAppState = iccCardApplicationStatus.app_state;
        IccCardApplicationStatus.AppType appType = iccCardApplicationStatus.app_type;
        this.mAppType = appType;
        this.mAuthContext = getAuthContext(appType);
        this.mPersoSubState = iccCardApplicationStatus.perso_substate;
        this.mAid = iccCardApplicationStatus.aid;
        this.mAppLabel = iccCardApplicationStatus.app_label;
        this.mPin1Replaced = iccCardApplicationStatus.pin1_replaced;
        this.mPin1State = iccCardApplicationStatus.pin1;
        this.mPin2State = iccCardApplicationStatus.pin2;
        this.mIgnoreApp = false;
        this.mContext = context;
        this.mCi = commandsInterface;
        log("Creating UiccApp: " + iccCardApplicationStatus);
        this.mIccFh = createIccFileHandler(iccCardApplicationStatus.app_type);
        this.mIccRecords = createIccRecords(iccCardApplicationStatus.app_type, this.mContext, this.mCi);
        if (this.mAppState == IccCardApplicationStatus.AppState.APPSTATE_READY) {
            queryFdn();
            queryPin1State();
        }
        this.mCi.registerForNotAvailable(this.mHandler, 9, null);
    }

    public void update(IccCardApplicationStatus iccCardApplicationStatus, Context context, CommandsInterface commandsInterface) {
        IccCardApplicationStatus.AppType appType;
        IccCardApplicationStatus.AppType appType2;
        synchronized (this.mLock) {
            try {
                if (this.mDestroyed) {
                    loge("Application updated after destroyed! Fix me!");
                    return;
                }
                log(this.mAppType + " update. New " + iccCardApplicationStatus);
                this.mContext = context;
                this.mCi = commandsInterface;
                IccCardApplicationStatus.AppType appType3 = this.mAppType;
                IccCardApplicationStatus.AppState appState = this.mAppState;
                IccCardApplicationStatus.PersoSubState persoSubState = this.mPersoSubState;
                IccCardStatus.PinState pinState = this.mPin1State;
                IccCardApplicationStatus.AppType appType4 = iccCardApplicationStatus.app_type;
                this.mAppType = appType4;
                this.mAuthContext = getAuthContext(appType4);
                this.mAppState = iccCardApplicationStatus.app_state;
                this.mPersoSubState = iccCardApplicationStatus.perso_substate;
                this.mAid = iccCardApplicationStatus.aid;
                this.mAppLabel = iccCardApplicationStatus.app_label;
                this.mPin1Replaced = iccCardApplicationStatus.pin1_replaced;
                this.mPin1State = iccCardApplicationStatus.pin1;
                this.mPin2State = iccCardApplicationStatus.pin2;
                if (this.mAppType != appType3) {
                    IccFileHandler iccFileHandler = this.mIccFh;
                    if (iccFileHandler != null) {
                        iccFileHandler.dispose();
                    }
                    IccRecords iccRecords = this.mIccRecords;
                    if (iccRecords != null) {
                        iccRecords.dispose();
                    }
                    this.mIccFh = createIccFileHandler(iccCardApplicationStatus.app_type);
                    this.mIccRecords = createIccRecords(iccCardApplicationStatus.app_type, context, commandsInterface);
                }
                IccCardApplicationStatus.PersoSubState persoSubState2 = this.mPersoSubState;
                if (persoSubState2 != persoSubState && IccCardApplicationStatus.PersoSubState.isPersoLocked(persoSubState2)) {
                    notifyNetworkLockedRegistrantsIfNeeded(null);
                }
                IccCardApplicationStatus.AppState appState2 = this.mAppState;
                if (appState2 != appState) {
                    log(appType3 + " changed state: " + appState + " -> " + this.mAppState);
                    if (this.mAppState == IccCardApplicationStatus.AppState.APPSTATE_READY) {
                        queryFdn();
                        queryPin1State();
                    }
                    notifyPinLockedRegistrantsIfNeeded(null);
                    notifyReadyRegistrantsIfNeeded(null);
                    notifyDetectedRegistrantsIfNeeded(null);
                } else if (appState2 == IccCardApplicationStatus.AppState.APPSTATE_READY && (((appType = this.mAppType) == (appType2 = IccCardApplicationStatus.AppType.APPTYPE_SIM) && appType3 == IccCardApplicationStatus.AppType.APPTYPE_RUIM) || (appType == IccCardApplicationStatus.AppType.APPTYPE_RUIM && appType3 == appType2))) {
                    queryFdn();
                    queryPin1State();
                } else if (this.mPin1State != pinState) {
                    queryPin1State();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    void dispose() {
        synchronized (this.mLock) {
            try {
                log(this.mAppType + " being Disposed");
                this.mDestroyed = true;
                IccRecords iccRecords = this.mIccRecords;
                if (iccRecords != null) {
                    iccRecords.dispose();
                }
                IccFileHandler iccFileHandler = this.mIccFh;
                if (iccFileHandler != null) {
                    iccFileHandler.dispose();
                }
                this.mIccRecords = null;
                this.mIccFh = null;
                this.mCi.unregisterForNotAvailable(this.mHandler);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected IccRecords createIccRecords(IccCardApplicationStatus.AppType appType, Context context, CommandsInterface commandsInterface) {
        log("UiccCardApp createIccRecords, AppType = " + appType);
        if (appType == IccCardApplicationStatus.AppType.APPTYPE_USIM || appType == IccCardApplicationStatus.AppType.APPTYPE_SIM) {
            return onCreateSimRecords(context, commandsInterface);
        }
        if (appType == IccCardApplicationStatus.AppType.APPTYPE_ISIM) {
            return onCreateIsimUiccRecords(context, commandsInterface, this.mFeatureFlags);
        }
        return null;
    }

    protected IccFileHandler createIccFileHandler(IccCardApplicationStatus.AppType appType) {
        int i = AnonymousClass2.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType[appType.ordinal()];
        if (i == 1) {
            return onCreateSIMFileHandler();
        }
        if (i == 3) {
            return onCreateUsimFileHandler();
        }
        if (i != 5) {
            return null;
        }
        return onCreateIsimFileHandler();
    }

    public void queryFdn() {
        if (getType() == IccCardApplicationStatus.AppType.APPTYPE_ISIM) {
            log("queryFdn(): do nothing for ISIM.");
        } else {
            this.mCi.queryFacilityLockForApp(CommandsInterface.CB_FACILITY_BA_FD, "", 7, this.mAid, this.mHandler.obtainMessage(4));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onQueryFdnEnabled(AsyncResult asyncResult) {
        synchronized (this.mLock) {
            try {
                if (asyncResult.exception != null) {
                    log("Error in querying facility lock:" + asyncResult.exception);
                    return;
                }
                int[] iArr = (int[]) asyncResult.result;
                boolean z = false;
                if (iArr.length != 0) {
                    int i = iArr[0];
                    if (i == 2) {
                        boolean z2 = this.mIccFdnEnabled;
                        this.mIccFdnEnabled = false;
                        this.mIccFdnAvailable = false;
                        z = z2;
                    } else {
                        boolean z3 = (i == 1 && !this.mIccFdnEnabled) || (i != 1 && this.mIccFdnEnabled);
                        this.mIccFdnEnabled = i == 1;
                        this.mIccFdnAvailable = true;
                        z = z3;
                    }
                    log("Query facility FDN : FDN service available: " + this.mIccFdnAvailable + " enabled: " + this.mIccFdnEnabled);
                } else {
                    loge("Bogus facility lock response");
                }
                if (this.mIccFdnEnabled && this.mIccFdnAvailable) {
                    this.mIccRecords.loadFdnRecords();
                }
                log("fdnEnableChanged = " + z);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected void onChangeFdnDone(AsyncResult asyncResult) {
        int i;
        synchronized (this.mLock) {
            try {
                if (asyncResult.exception == null) {
                    this.mIccFdnEnabled = this.mDesiredFdnEnabled;
                    log("EVENT_CHANGE_FACILITY_FDN_DONE: mIccFdnEnabled=" + this.mIccFdnEnabled);
                    i = -1;
                } else {
                    int pinPukErrorResult = parsePinPukErrorResult(asyncResult);
                    loge("Error change facility fdn with exception " + asyncResult.exception);
                    i = pinPukErrorResult;
                }
                Message message = (Message) asyncResult.userObj;
                message.arg1 = i;
                AsyncResult.forMessage(message).exception = asyncResult.exception;
                message.sendToTarget();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void queryPin1State() {
        if (getType() == IccCardApplicationStatus.AppType.APPTYPE_ISIM) {
            log("queryPin1State(): do nothing for ISIM.");
        } else {
            this.mCi.queryFacilityLockForApp(CommandsInterface.CB_FACILITY_BA_SIM, "", 7, this.mAid, this.mHandler.obtainMessage(6));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onQueryFacilityLock(AsyncResult asyncResult) {
        synchronized (this.mLock) {
            try {
                if (asyncResult.exception != null) {
                    log("Error in querying facility lock:" + asyncResult.exception);
                    return;
                }
                int[] iArr = (int[]) asyncResult.result;
                if (iArr.length != 0) {
                    log("Query facility lock : " + iArr[0]);
                    this.mIccLockEnabled = iArr[0] != 0;
                    int i = AnonymousClass2.$SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState[this.mPin1State.ordinal()];
                    if (i == 1) {
                        if (this.mIccLockEnabled) {
                            loge("QUERY_FACILITY_LOCK:enabled GET_SIM_STATUS.Pin1:disabled. Fixme");
                        }
                    } else {
                        if ((i == 2 || i == 3 || i == 4 || i == 5) && !this.mIccLockEnabled) {
                            loge("QUERY_FACILITY_LOCK:disabled GET_SIM_STATUS.Pin1:enabled. Fixme");
                        }
                        log("Ignoring: pin1state=" + this.mPin1State);
                    }
                } else {
                    loge("Bogus facility lock response");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.uicc.UiccCardApplication$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState;

        static {
            int[] iArr = new int[IccCardStatus.PinState.values().length];
            $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState = iArr;
            try {
                iArr[IccCardStatus.PinState.PINSTATE_DISABLED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState[IccCardStatus.PinState.PINSTATE_ENABLED_NOT_VERIFIED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState[IccCardStatus.PinState.PINSTATE_ENABLED_VERIFIED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState[IccCardStatus.PinState.PINSTATE_ENABLED_BLOCKED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState[IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardStatus$PinState[IccCardStatus.PinState.PINSTATE_UNKNOWN.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr2 = new int[IccCardApplicationStatus.AppType.values().length];
            $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType = iArr2;
            try {
                iArr2[IccCardApplicationStatus.AppType.APPTYPE_SIM.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType[IccCardApplicationStatus.AppType.APPTYPE_RUIM.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType[IccCardApplicationStatus.AppType.APPTYPE_USIM.ordinal()] = 3;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType[IccCardApplicationStatus.AppType.APPTYPE_CSIM.ordinal()] = 4;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType[IccCardApplicationStatus.AppType.APPTYPE_ISIM.ordinal()] = 5;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    protected void onChangeFacilityLock(AsyncResult asyncResult) {
        int i;
        synchronized (this.mLock) {
            try {
                if (asyncResult.exception == null) {
                    this.mIccLockEnabled = this.mDesiredPinLocked;
                    log("EVENT_CHANGE_FACILITY_LOCK_DONE: mIccLockEnabled= " + this.mIccLockEnabled);
                    i = -1;
                } else {
                    int pinPukErrorResult = parsePinPukErrorResult(asyncResult);
                    loge("Error change facility lock with exception " + asyncResult.exception);
                    i = pinPukErrorResult;
                }
                Message message = (Message) asyncResult.userObj;
                AsyncResult.forMessage(message).exception = asyncResult.exception;
                message.arg1 = i;
                message.sendToTarget();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected int parsePinPukErrorResult(AsyncResult asyncResult) {
        int[] iArr = (int[]) asyncResult.result;
        if (iArr == null) {
            return -1;
        }
        int i = iArr.length > 0 ? iArr[0] : -1;
        log("parsePinPukErrorResult: attemptsRemaining=" + i);
        return i;
    }

    public void registerForReady(Handler handler, int i, Object obj) {
        synchronized (this.mLock) {
            Registrant registrant = new Registrant(handler, i, obj);
            this.mReadyRegistrants.add(registrant);
            notifyReadyRegistrantsIfNeeded(registrant);
        }
    }

    public void unregisterForReady(Handler handler) {
        synchronized (this.mLock) {
            this.mReadyRegistrants.remove(handler);
        }
    }

    public void registerForDetected(Handler handler, int i, Object obj) {
        synchronized (this.mLock) {
            Registrant registrant = new Registrant(handler, i, obj);
            this.mDetectedRegistrants.add(registrant);
            notifyDetectedRegistrantsIfNeeded(registrant);
        }
    }

    public void unregisterForDetected(Handler handler) {
        synchronized (this.mLock) {
            this.mDetectedRegistrants.remove(handler);
        }
    }

    protected void registerForLocked(Handler handler, int i, Object obj) {
        synchronized (this.mLock) {
            Registrant registrant = new Registrant(handler, i, obj);
            this.mPinLockedRegistrants.add(registrant);
            notifyPinLockedRegistrantsIfNeeded(registrant);
        }
    }

    protected void unregisterForLocked(Handler handler) {
        synchronized (this.mLock) {
            this.mPinLockedRegistrants.remove(handler);
        }
    }

    protected void registerForNetworkLocked(Handler handler, int i, Object obj) {
        synchronized (this.mLock) {
            Registrant registrant = new Registrant(handler, i, obj);
            this.mNetworkLockedRegistrants.add(registrant);
            notifyNetworkLockedRegistrantsIfNeeded(registrant);
        }
    }

    protected void unregisterForNetworkLocked(Handler handler) {
        synchronized (this.mLock) {
            this.mNetworkLockedRegistrants.remove(handler);
        }
    }

    protected void notifyReadyRegistrantsIfNeeded(Registrant registrant) {
        if (!this.mDestroyed && this.mAppState == IccCardApplicationStatus.AppState.APPSTATE_READY) {
            IccCardStatus.PinState pinState = this.mPin1State;
            if (pinState == IccCardStatus.PinState.PINSTATE_ENABLED_NOT_VERIFIED || pinState == IccCardStatus.PinState.PINSTATE_ENABLED_BLOCKED || pinState == IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
                loge("Sanity check failed! APPSTATE is ready while PIN1 is not verified!!!");
            } else if (registrant == null) {
                log("Notifying registrants: READY");
                this.mReadyRegistrants.notifyRegistrants();
            } else {
                log("Notifying 1 registrant: READY");
                registrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }
    }

    private void notifyDetectedRegistrantsIfNeeded(Registrant registrant) {
        if (!this.mDestroyed && this.mAppState == IccCardApplicationStatus.AppState.APPSTATE_DETECTED) {
            if (registrant == null) {
                log("Notifying registrants: DETECTED");
                this.mDetectedRegistrants.notifyRegistrants();
            } else {
                log("Notifying 1 registrant: DETECTED");
                registrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }
    }

    protected void notifyPinLockedRegistrantsIfNeeded(Registrant registrant) {
        if (this.mDestroyed) {
            return;
        }
        IccCardApplicationStatus.AppState appState = this.mAppState;
        if (appState == IccCardApplicationStatus.AppState.APPSTATE_PIN || appState == IccCardApplicationStatus.AppState.APPSTATE_PUK) {
            IccCardStatus.PinState pinState = this.mPin1State;
            if (pinState == IccCardStatus.PinState.PINSTATE_ENABLED_VERIFIED || pinState == IccCardStatus.PinState.PINSTATE_DISABLED) {
                loge("Sanity check failed! APPSTATE is locked while PIN1 is not!!!");
            } else if (registrant == null) {
                log("Notifying registrants: LOCKED");
                this.mPinLockedRegistrants.notifyRegistrants();
            } else {
                log("Notifying 1 registrant: LOCKED");
                registrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            }
        }
    }

    private void notifyNetworkLockedRegistrantsIfNeeded(Registrant registrant) {
        if (!this.mDestroyed && this.mAppState == IccCardApplicationStatus.AppState.APPSTATE_SUBSCRIPTION_PERSO && IccCardApplicationStatus.PersoSubState.isPersoLocked(this.mPersoSubState)) {
            AsyncResult asyncResult = new AsyncResult((Object) null, Integer.valueOf(this.mPersoSubState.ordinal()), (Throwable) null);
            IccCardApplicationStatus.PersoSubState persoSubState = this.mPersoSubState;
            if (registrant == null) {
                log("Notifying registrants: NETWORK_LOCKED with mPersoSubState" + persoSubState);
                this.mNetworkLockedRegistrants.notifyRegistrants(asyncResult);
                return;
            }
            log("Notifying 1 registrant: NETWORK_LOCKED with mPersoSubState" + persoSubState);
            registrant.notifyRegistrant(asyncResult);
        }
    }

    public IccCardApplicationStatus.AppState getState() {
        IccCardApplicationStatus.AppState appState;
        synchronized (this.mLock) {
            appState = this.mAppState;
        }
        return appState;
    }

    public IccCardApplicationStatus.AppType getType() {
        IccCardApplicationStatus.AppType appType;
        synchronized (this.mLock) {
            appType = this.mAppType;
        }
        return appType;
    }

    public int getAuthContext() {
        int i;
        synchronized (this.mLock) {
            i = this.mAuthContext;
        }
        return i;
    }

    private static int getAuthContext(IccCardApplicationStatus.AppType appType) {
        int i = AnonymousClass2.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppType[appType.ordinal()];
        if (i != 1) {
            return i != 3 ? -1 : 129;
        }
        return 128;
    }

    public IccCardApplicationStatus.PersoSubState getPersoSubState() {
        IccCardApplicationStatus.PersoSubState persoSubState;
        synchronized (this.mLock) {
            persoSubState = this.mPersoSubState;
        }
        return persoSubState;
    }

    public String getAid() {
        String str;
        synchronized (this.mLock) {
            str = this.mAid;
        }
        return str;
    }

    public String getAppLabel() {
        return this.mAppLabel;
    }

    public IccCardStatus.PinState getPin1State() {
        synchronized (this.mLock) {
            try {
                if (this.mPin1Replaced) {
                    return this.mUiccProfile.getUniversalPinState();
                }
                return this.mPin1State;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public IccFileHandler getIccFileHandler() {
        IccFileHandler iccFileHandler;
        synchronized (this.mLock) {
            iccFileHandler = this.mIccFh;
        }
        return iccFileHandler;
    }

    public IccRecords getIccRecords() {
        IccRecords iccRecords;
        synchronized (this.mLock) {
            iccRecords = this.mIccRecords;
        }
        return iccRecords;
    }

    public void supplyPin(String str, Message message) {
        synchronized (this.mLock) {
            this.mCi.supplyIccPinForApp(str, this.mAid, this.mHandler.obtainMessage(1, message));
        }
    }

    public void supplyPuk(String str, String str2, Message message) {
        synchronized (this.mLock) {
            this.mCi.supplyIccPukForApp(str, str2, this.mAid, this.mHandler.obtainMessage(1, message));
        }
    }

    public void supplyPin2(String str, Message message) {
        synchronized (this.mLock) {
            this.mCi.supplyIccPin2ForApp(str, this.mAid, this.mHandler.obtainMessage(8, message));
        }
    }

    public void supplyPuk2(String str, String str2, Message message) {
        synchronized (this.mLock) {
            this.mCi.supplyIccPuk2ForApp(str, str2, this.mAid, this.mHandler.obtainMessage(8, message));
        }
    }

    public void supplyNetworkDepersonalization(String str, Message message) {
        synchronized (this.mLock) {
            log("supplyNetworkDepersonalization");
            this.mCi.supplyNetworkDepersonalization(str, message);
        }
    }

    public void supplySimDepersonalization(IccCardApplicationStatus.PersoSubState persoSubState, String str, Message message) {
        synchronized (this.mLock) {
            log("supplySimDepersonalization");
            this.mCi.supplySimDepersonalization(persoSubState, str, message);
        }
    }

    public boolean getIccLockEnabled() {
        return this.mIccLockEnabled;
    }

    public boolean getIccFdnEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIccFdnEnabled;
        }
        return z;
    }

    public boolean getIccFdnAvailable() {
        return this.mIccFdnAvailable;
    }

    public void setIccLockEnabled(boolean z, String str, Message message) {
        synchronized (this.mLock) {
            this.mDesiredPinLocked = z;
            this.mCi.setFacilityLockForApp(CommandsInterface.CB_FACILITY_BA_SIM, z, str, 7, this.mAid, this.mHandler.obtainMessage(7, message));
        }
    }

    public void setIccFdnEnabled(boolean z, String str, Message message) {
        synchronized (this.mLock) {
            this.mDesiredFdnEnabled = z;
            this.mCi.setFacilityLockForApp(CommandsInterface.CB_FACILITY_BA_FD, z, str, 15, this.mAid, this.mHandler.obtainMessage(5, message));
        }
    }

    public void changeIccLockPassword(String str, String str2, Message message) {
        synchronized (this.mLock) {
            log("changeIccLockPassword");
            this.mCi.changeIccPinForApp(str, str2, this.mAid, this.mHandler.obtainMessage(2, message));
        }
    }

    public void changeIccFdnPassword(String str, String str2, Message message) {
        synchronized (this.mLock) {
            log("changeIccFdnPassword");
            this.mCi.changeIccPin2ForApp(str, str2, this.mAid, this.mHandler.obtainMessage(3, message));
        }
    }

    public boolean isReady() {
        synchronized (this.mLock) {
            try {
                if (this.mAppState != IccCardApplicationStatus.AppState.APPSTATE_READY) {
                    return false;
                }
                IccCardStatus.PinState pinState = this.mPin1State;
                if (pinState != IccCardStatus.PinState.PINSTATE_ENABLED_NOT_VERIFIED && pinState != IccCardStatus.PinState.PINSTATE_ENABLED_BLOCKED && pinState != IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED) {
                    return true;
                }
                loge("Sanity check failed! APPSTATE is ready while PIN1 is not verified!!!");
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean getIccPin2Blocked() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPin2State == IccCardStatus.PinState.PINSTATE_ENABLED_BLOCKED;
        }
        return z;
    }

    public boolean getIccPuk2Blocked() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPin2State == IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED;
        }
        return z;
    }

    public int getPhoneId() {
        return this.mUiccProfile.getPhoneId();
    }

    public boolean isAppIgnored() {
        return this.mIgnoreApp;
    }

    public void setAppIgnoreState(boolean z) {
        this.mIgnoreApp = z;
    }

    protected UiccProfile getUiccProfile() {
        return this.mUiccProfile;
    }

    protected IccRecords onCreateSimRecords(Context context, CommandsInterface commandsInterface) {
        return TelephonyComponentFactory.getInstance().inject(SIMRecords.class.getName()).makeSIMRecords(this, context, commandsInterface);
    }

    protected IccRecords onCreateRuimRecords(Context context, CommandsInterface commandsInterface) {
        return TelephonyComponentFactory.getInstance().inject(RuimRecords.class.getName()).makeRuimRecords(this, context, commandsInterface);
    }

    protected IccRecords onCreateIsimUiccRecords(Context context, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        return TelephonyComponentFactory.getInstance().inject(IsimUiccRecords.class.getName()).makeIsimUiccRecords(this, context, commandsInterface, featureFlags);
    }

    protected IccFileHandler onCreateSIMFileHandler() {
        return TelephonyComponentFactory.getInstance().inject(SIMFileHandler.class.getName()).makeSIMFileHandler(this, this.mAid, this.mCi);
    }

    protected IccFileHandler onCreateRuimFileHandler() {
        return new RuimFileHandler(this, this.mAid, this.mCi);
    }

    protected IccFileHandler onCreateUsimFileHandler() {
        return TelephonyComponentFactory.getInstance().inject(UsimFileHandler.class.getName()).makeUsimFileHandler(this, this.mAid, this.mCi);
    }

    protected IccFileHandler onCreateCsimFileHandler() {
        return new CsimFileHandler(this, this.mAid, this.mCi);
    }

    protected IccFileHandler onCreateIsimFileHandler() {
        return TelephonyComponentFactory.getInstance().inject(IsimFileHandler.class.getName()).makeIsimFileHandler(this, this.mAid, this.mCi);
    }

    protected void log(String str) {
        Rlog.d("UiccCardApplication [" + getPhoneId() + "]", str);
    }

    protected void loge(String str) {
        Rlog.e("UiccCardApplication [" + getPhoneId() + "]", str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("UiccCardApplication: ");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mUiccProfile=" + this.mUiccProfile);
        androidUtilIndentingPrintWriter.println("mAppState=" + this.mAppState);
        androidUtilIndentingPrintWriter.println("mAppType=" + this.mAppType);
        androidUtilIndentingPrintWriter.println("mPersoSubState=" + this.mPersoSubState);
        androidUtilIndentingPrintWriter.println("mAid=" + this.mAid);
        androidUtilIndentingPrintWriter.println("mAppLabel=" + this.mAppLabel);
        androidUtilIndentingPrintWriter.println("mPin1Replaced=" + this.mPin1Replaced);
        androidUtilIndentingPrintWriter.println("mPin1State=" + this.mPin1State);
        androidUtilIndentingPrintWriter.println("mPin2State=" + this.mPin2State);
        androidUtilIndentingPrintWriter.println("mIccFdnAvailable=" + this.mIccFdnAvailable);
        androidUtilIndentingPrintWriter.println("mIccFdnEnabled=" + this.mIccFdnEnabled);
        androidUtilIndentingPrintWriter.println("mDesiredFdnEnabled=" + this.mDesiredFdnEnabled);
        androidUtilIndentingPrintWriter.println("mIccLockEnabled=" + this.mIccLockEnabled);
        androidUtilIndentingPrintWriter.println("mDesiredPinLocked=" + this.mDesiredPinLocked);
        androidUtilIndentingPrintWriter.println("mIccRecords=" + this.mIccRecords);
        androidUtilIndentingPrintWriter.println("mIccFh=" + this.mIccFh);
        androidUtilIndentingPrintWriter.println("mDestroyed=" + this.mDestroyed);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.flush();
    }
}
