package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.UserHandle;
import android.telephony.RadioAccessFamily;
import android.telephony.TelephonyManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class ProxyController {
    protected static final int EVENT_APPLY_RC_RESPONSE = 3;
    public static final int EVENT_FINISH_RC_RESPONSE = 4;
    public static final int EVENT_MULTI_SIM_CONFIG_CHANGED = 6;
    protected static final int EVENT_NOTIFICATION_RC_CHANGED = 1;
    static final int EVENT_START_RC_RESPONSE = 2;
    public static final int EVENT_TIMEOUT = 5;
    protected static final int SET_RC_STATUS_APPLYING = 3;
    protected static final int SET_RC_STATUS_FAIL = 5;
    protected static final int SET_RC_STATUS_IDLE = 0;
    protected static final int SET_RC_STATUS_STARTED = 2;
    protected static final int SET_RC_STATUS_STARTING = 1;
    protected static final int SET_RC_STATUS_SUCCESS = 4;
    protected static final int SET_RC_TIMEOUT_WAITING_MSEC = 45000;
    private static ProxyController sProxyController;
    protected Context mContext;
    protected String[] mCurrentLogicalModemIds;
    private final FeatureFlags mFlags;
    protected String[] mNewLogicalModemIds;
    protected int[] mNewRadioAccessFamily;
    protected int[] mOldRadioAccessFamily;
    protected PhoneSubInfoController mPhoneSubInfoController;
    protected PhoneSwitcher mPhoneSwitcher;
    protected Phone[] mPhones;
    protected int mRadioAccessFamilyStatusCounter;
    protected int mRadioCapabilitySessionId;
    protected int[] mSetRadioAccessFamilyStatus;
    private SmsController mSmsController;
    protected UiccPhoneBookController mUiccPhoneBookController;
    protected PowerManager.WakeLock mWakeLock;
    protected boolean mTransactionFailed = false;
    protected AtomicInteger mUniqueIdGenerator = new AtomicInteger(new Random().nextInt());
    protected RadioAccessFamily[] mPendingRequest = null;
    protected boolean mIsExecutingUiccAppRequest = false;
    protected final Handler mHandler = new Handler() { // from class: com.android.internal.telephony.ProxyController.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ProxyController.this.logd("handleMessage msg.what=" + message.what);
            switch (message.what) {
                case 1:
                    ProxyController.this.onNotificationRadioCapabilityChanged(message);
                    break;
                case 2:
                    ProxyController.this.onStartRadioCapabilityResponse(message);
                    break;
                case 3:
                    ProxyController.this.onApplyRadioCapabilityResponse(message);
                    break;
                case 4:
                    ProxyController.this.onFinishRadioCapabilityResponse(message);
                    break;
                case 5:
                    ProxyController.this.onTimeoutRadioCapability(message);
                    break;
                case 6:
                    ProxyController.this.onMultiSimConfigChanged();
                    break;
            }
        }
    };

    protected void onApplyExceptionHandler(Message message) {
    }

    protected void onApplyRadioCapabilityErrorHandler(Message message) {
    }

    public static ProxyController getInstance(Context context, FeatureFlags featureFlags) {
        if (sProxyController == null) {
            sProxyController = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeProxyController(context, featureFlags);
        }
        return sProxyController;
    }

    public static ProxyController getInstance() {
        return sProxyController;
    }

    public ProxyController(Context context, FeatureFlags featureFlags) {
        int i = 0;
        logd("Constructor - Enter");
        this.mContext = context;
        this.mFlags = featureFlags;
        this.mPhones = PhoneFactory.getPhones();
        this.mPhoneSwitcher = PhoneSwitcher.getInstance();
        this.mUiccPhoneBookController = UiccPhoneBookController.init(featureFlags);
        this.mPhoneSubInfoController = PhoneSubInfoController.init(this.mContext);
        this.mSmsController = SmsController.init(this.mContext, featureFlags);
        Phone[] phoneArr = this.mPhones;
        this.mSetRadioAccessFamilyStatus = new int[phoneArr.length];
        this.mNewRadioAccessFamily = new int[phoneArr.length];
        this.mOldRadioAccessFamily = new int[phoneArr.length];
        this.mCurrentLogicalModemIds = new String[phoneArr.length];
        this.mNewLogicalModemIds = new String[phoneArr.length];
        PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "ProxyController");
        this.mWakeLock = wakeLockNewWakeLock;
        wakeLockNewWakeLock.setReferenceCounted(false);
        clearTransaction();
        while (true) {
            Phone[] phoneArr2 = this.mPhones;
            if (i < phoneArr2.length) {
                phoneArr2[i].registerForRadioCapabilityChanged(this.mHandler, 1, null);
                i++;
            } else {
                PhoneConfigurationManager.registerForMultiSimConfigChange(this.mHandler, 6, null);
                logd("Constructor - Exit");
                return;
            }
        }
    }

    public int getRadioAccessFamily(int i) {
        Phone[] phoneArr = this.mPhones;
        if (i >= phoneArr.length) {
            return 0;
        }
        return phoneArr[i].getRadioAccessFamily();
    }

    public boolean setRadioCapability(RadioAccessFamily[] radioAccessFamilyArr) {
        if (radioAccessFamilyArr.length != this.mPhones.length) {
            return false;
        }
        synchronized (this.mSetRadioAccessFamilyStatus) {
            for (int i = 0; i < this.mPhones.length; i++) {
                try {
                    if (this.mSetRadioAccessFamilyStatus[i] != 0) {
                        loge("setRadioCapability: Phone[" + i + "] is not idle. Rejecting request.");
                        return false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            boolean z = true;
            int i2 = 0;
            while (true) {
                Phone[] phoneArr = this.mPhones;
                if (i2 >= phoneArr.length) {
                    break;
                }
                if (phoneArr[i2].getRadioAccessFamily() != radioAccessFamilyArr[i2].getRadioAccessFamily()) {
                    z = false;
                }
                i2++;
            }
            if (z) {
                logd("setRadioCapability: Already in requested configuration, nothing to do.");
                return true;
            }
            clearTransaction();
            logd("Acquiring wake lock for setting radio capability");
            this.mWakeLock.acquire();
            return doSetRadioCapabilities(radioAccessFamilyArr);
        }
    }

    public SmsController getSmsController() {
        return this.mSmsController;
    }

    protected boolean doSetRadioCapabilities(RadioAccessFamily[] radioAccessFamilyArr) {
        this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
        synchronized (this.mSetRadioAccessFamilyStatus) {
            try {
                logd("setRadioCapability: new request session id=" + this.mRadioCapabilitySessionId);
                resetRadioAccessFamilyStatusCounter();
                int i = 0;
                while (i < radioAccessFamilyArr.length) {
                    int phoneId = radioAccessFamilyArr[i].getPhoneId();
                    this.logd("setRadioCapability: phoneId=" + phoneId + " status=STARTING");
                    this.mSetRadioAccessFamilyStatus[phoneId] = 1;
                    this.mOldRadioAccessFamily[phoneId] = this.mPhones[phoneId].getRadioAccessFamily();
                    int radioAccessFamily = radioAccessFamilyArr[i].getRadioAccessFamily();
                    this.mNewRadioAccessFamily[phoneId] = radioAccessFamily;
                    this.mCurrentLogicalModemIds[phoneId] = this.mPhones[phoneId].getModemUuId();
                    this.mNewLogicalModemIds[phoneId] = this.getLogicalModemIdFromRaf(radioAccessFamily);
                    this.logd("setRadioCapability: mOldRadioAccessFamily[" + phoneId + "]=" + this.mOldRadioAccessFamily[phoneId]);
                    this.logd("setRadioCapability: mNewRadioAccessFamily[" + phoneId + "]=" + this.mNewRadioAccessFamily[phoneId]);
                    ProxyController proxyController = this;
                    proxyController.sendRadioCapabilityRequest(phoneId, this.mRadioCapabilitySessionId, 1, this.mOldRadioAccessFamily[phoneId], this.mCurrentLogicalModemIds[phoneId], 0, 2);
                    i++;
                    this = proxyController;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiSimConfigChanged() {
        int length = this.mPhones.length;
        Phone[] phones = PhoneFactory.getPhones();
        this.mPhones = phones;
        this.mSetRadioAccessFamilyStatus = Arrays.copyOf(this.mSetRadioAccessFamilyStatus, phones.length);
        this.mNewRadioAccessFamily = Arrays.copyOf(this.mNewRadioAccessFamily, this.mPhones.length);
        this.mOldRadioAccessFamily = Arrays.copyOf(this.mOldRadioAccessFamily, this.mPhones.length);
        this.mCurrentLogicalModemIds = (String[]) Arrays.copyOf(this.mCurrentLogicalModemIds, this.mPhones.length);
        this.mNewLogicalModemIds = (String[]) Arrays.copyOf(this.mNewLogicalModemIds, this.mPhones.length);
        clearTransaction();
        while (true) {
            Phone[] phoneArr = this.mPhones;
            if (length >= phoneArr.length) {
                return;
            }
            phoneArr[length].registerForRadioCapabilityChanged(this.mHandler, 1, null);
            length++;
        }
    }

    protected void onStartRadioCapabilityResponse(Message message) {
        synchronized (this.mSetRadioAccessFamilyStatus) {
            try {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                Throwable th = asyncResult.exception;
                if (th != null) {
                    boolean z = (th instanceof CommandException) && ((CommandException) th).getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED;
                    if (TelephonyManager.getDefault().getPhoneCount() == 1 || z) {
                        logd("onStartRadioCapabilityResponse got exception=" + asyncResult.exception);
                        this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
                        this.mContext.sendBroadcastAsUser(new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED"), UserHandle.ALL);
                        clearTransaction();
                        return;
                    }
                }
                RadioCapability radioCapability = (RadioCapability) ((AsyncResult) message.obj).result;
                if (asyncResult.exception == null && (radioCapability == null || radioCapability.getSession() != this.mRadioCapabilitySessionId)) {
                    logd("onStartRadioCapabilityResponse: Ignore session=" + this.mRadioCapabilitySessionId + " rc=" + radioCapability);
                    return;
                }
                this.mRadioAccessFamilyStatusCounter--;
                if (asyncResult.exception != null) {
                    logd("onStartRadioCapabilityResponse got exception=" + asyncResult.exception);
                    this.mTransactionFailed = true;
                } else {
                    int phoneId = radioCapability.getPhoneId();
                    logd("onStartRadioCapabilityResponse: phoneId=" + phoneId + " status=STARTED");
                    this.mSetRadioAccessFamilyStatus[phoneId] = 2;
                }
                if (this.mRadioAccessFamilyStatusCounter == 0) {
                    StringBuilder sb = new StringBuilder("onStartRadioCapabilityResponse: success=");
                    sb.append(!this.mTransactionFailed);
                    logd(sb.toString());
                    if (this.mTransactionFailed) {
                        issueFinish(this.mRadioCapabilitySessionId);
                    } else {
                        resetRadioAccessFamilyStatusCounter();
                        int i = 0;
                        while (i < this.mPhones.length) {
                            ProxyController proxyController = this;
                            proxyController.sendRadioCapabilityRequest(i, this.mRadioCapabilitySessionId, 2, this.mNewRadioAccessFamily[i], this.mNewLogicalModemIds[i], 0, 3);
                            proxyController.logd("onStartRadioCapabilityResponse: phoneId=" + i + " status=APPLYING");
                            proxyController.mSetRadioAccessFamilyStatus[i] = 3;
                            i++;
                            this = proxyController;
                        }
                    }
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }

    protected void onApplyRadioCapabilityResponse(Message message) {
        RadioCapability radioCapability = (RadioCapability) ((AsyncResult) message.obj).result;
        if (radioCapability == null || radioCapability.getSession() != this.mRadioCapabilitySessionId) {
            logd("onApplyRadioCapabilityResponse: Ignore session=" + this.mRadioCapabilitySessionId + " rc=" + radioCapability);
            onApplyRadioCapabilityErrorHandler(message);
            return;
        }
        logd("onApplyRadioCapabilityResponse: rc=" + radioCapability);
        if (((AsyncResult) message.obj).exception != null) {
            synchronized (this.mSetRadioAccessFamilyStatus) {
                logd("onApplyRadioCapabilityResponse: Error response session=" + radioCapability.getSession());
                int phoneId = radioCapability.getPhoneId();
                onApplyExceptionHandler(message);
                logd("onApplyRadioCapabilityResponse: phoneId=" + phoneId + " status=FAIL");
                this.mSetRadioAccessFamilyStatus[phoneId] = 5;
                this.mTransactionFailed = true;
            }
            return;
        }
        logd("onApplyRadioCapabilityResponse: Valid start expecting notification rc=" + radioCapability);
    }

    protected void onNotificationRadioCapabilityChanged(Message message) {
        RadioCapability radioCapability = (RadioCapability) ((AsyncResult) message.obj).result;
        if (radioCapability == null || radioCapability.getSession() != this.mRadioCapabilitySessionId) {
            logd("onNotificationRadioCapabilityChanged: Ignore session=" + this.mRadioCapabilitySessionId + " rc=" + radioCapability);
            return;
        }
        synchronized (this.mSetRadioAccessFamilyStatus) {
            try {
                logd("onNotificationRadioCapabilityChanged: rc=" + radioCapability);
                if (radioCapability.getSession() != this.mRadioCapabilitySessionId) {
                    logd("onNotificationRadioCapabilityChanged: Ignore session=" + this.mRadioCapabilitySessionId + " rc=" + radioCapability);
                    return;
                }
                int phoneId = radioCapability.getPhoneId();
                if (((AsyncResult) message.obj).exception != null || radioCapability.getStatus() == 2) {
                    logd("onNotificationRadioCapabilityChanged: phoneId=" + phoneId + " status=FAIL");
                    this.mSetRadioAccessFamilyStatus[phoneId] = 5;
                    this.mTransactionFailed = true;
                } else {
                    logd("onNotificationRadioCapabilityChanged: phoneId=" + phoneId + " status=SUCCESS");
                    this.mPhoneSwitcher.onRadioCapChanged(phoneId);
                    this.mPhones[phoneId].radioCapabilityUpdated(radioCapability, true);
                }
                int i = this.mRadioAccessFamilyStatusCounter - 1;
                this.mRadioAccessFamilyStatusCounter = i;
                if (i == 0) {
                    logd("onNotificationRadioCapabilityChanged: APPLY URC success=" + this.mTransactionFailed);
                    issueFinish(this.mRadioCapabilitySessionId);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected void onFinishRadioCapabilityResponse(Message message) {
        synchronized (this.mSetRadioAccessFamilyStatus) {
            try {
                Object obj = message.obj;
                AsyncResult asyncResult = (AsyncResult) obj;
                RadioCapability radioCapability = (RadioCapability) ((AsyncResult) obj).result;
                if (asyncResult.exception != null || (radioCapability != null && radioCapability.getSession() == this.mRadioCapabilitySessionId)) {
                    if (asyncResult.exception != null) {
                        logd("onFinishRadioCapabilityResponse got exception=" + asyncResult.exception);
                    }
                    logd(" onFinishRadioCapabilityResponse mRadioAccessFamilyStatusCounter=" + this.mRadioAccessFamilyStatusCounter);
                    int i = this.mRadioAccessFamilyStatusCounter + (-1);
                    this.mRadioAccessFamilyStatusCounter = i;
                    if (i == 0) {
                        completeRadioCapabilityTransaction();
                    }
                    return;
                }
                logd("onFinishRadioCapabilityResponse: Ignore session=" + this.mRadioCapabilitySessionId + " rc=" + radioCapability);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected void onTimeoutRadioCapability(Message message) {
        if (message.arg1 != this.mRadioCapabilitySessionId) {
            logd("RadioCapability timeout: Ignore msg.arg1=" + message.arg1 + "!= mRadioCapabilitySessionId=" + this.mRadioCapabilitySessionId);
            return;
        }
        synchronized (this.mSetRadioAccessFamilyStatus) {
            for (int i = 0; i < this.mPhones.length; i++) {
                try {
                    logd("RadioCapability timeout: mSetRadioAccessFamilyStatus[" + i + "]=" + this.mSetRadioAccessFamilyStatus[i]);
                } catch (Throwable th) {
                    throw th;
                }
            }
            int andIncrement = this.mUniqueIdGenerator.getAndIncrement();
            this.mRadioCapabilitySessionId = andIncrement;
            this.mRadioAccessFamilyStatusCounter = 0;
            this.mTransactionFailed = true;
            issueFinish(andIncrement);
        }
    }

    protected void issueFinish(int i) {
        String str;
        synchronized (this.mSetRadioAccessFamilyStatus) {
            int i2 = 0;
            while (i2 < this.mPhones.length) {
                try {
                    this.logd("issueFinish: phoneId=" + i2 + " sessionId=" + i + " mTransactionFailed=" + this.mTransactionFailed);
                    this.mRadioAccessFamilyStatusCounter = this.mRadioAccessFamilyStatusCounter + 1;
                    boolean z = this.mTransactionFailed;
                    int i3 = z ? this.mOldRadioAccessFamily[i2] : this.mNewRadioAccessFamily[i2];
                    if (z) {
                        str = this.mCurrentLogicalModemIds[i2];
                    } else {
                        str = this.mNewLogicalModemIds[i2];
                    }
                    String str2 = str;
                    ProxyController proxyController = this;
                    int i4 = i;
                    proxyController.sendRadioCapabilityRequest(i2, i4, 4, i3, str2, z ? 2 : 1, 4);
                    if (proxyController.mTransactionFailed) {
                        proxyController.logd("issueFinish: phoneId: " + i2 + " status: FAIL");
                        proxyController.mSetRadioAccessFamilyStatus[i2] = 5;
                    }
                    i2++;
                    this = proxyController;
                    i = i4;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    protected void completeRadioCapabilityTransaction() {
        Intent intent;
        StringBuilder sb = new StringBuilder("onFinishRadioCapabilityResponse: success=");
        sb.append(!this.mTransactionFailed);
        logd(sb.toString());
        int i = 0;
        if (!this.mTransactionFailed) {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
            while (true) {
                Phone[] phoneArr = this.mPhones;
                if (i >= phoneArr.length) {
                    break;
                }
                int radioAccessFamily = phoneArr[i].getRadioAccessFamily();
                logd("radioAccessFamily[" + i + "]=" + radioAccessFamily);
                arrayList.add(new RadioAccessFamily(i, radioAccessFamily));
                i++;
            }
            intent = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
            intent.putParcelableArrayListExtra("rafs", arrayList);
            this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
            clearTransaction();
        } else {
            Intent intent2 = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
            this.mTransactionFailed = false;
            RadioAccessFamily[] radioAccessFamilyArr = new RadioAccessFamily[this.mPhones.length];
            while (i < this.mPhones.length) {
                radioAccessFamilyArr[i] = new RadioAccessFamily(i, this.mOldRadioAccessFamily[i]);
                i++;
            }
            doSetRadioCapabilities(radioAccessFamilyArr);
            intent = intent2;
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PHONE_STATE");
    }

    protected void clearTransaction() {
        logd("clearTransaction");
        synchronized (this.mSetRadioAccessFamilyStatus) {
            for (int i = 0; i < this.mPhones.length; i++) {
                try {
                    logd("clearTransaction: phoneId=" + i + " status=IDLE");
                    this.mSetRadioAccessFamilyStatus[i] = 0;
                    this.mOldRadioAccessFamily[i] = 0;
                    this.mNewRadioAccessFamily[i] = 0;
                    this.mTransactionFailed = false;
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (isWakeLockHeld()) {
                logd("clearTransaction:checking wakelock held and releasing");
                this.mWakeLock.release();
            }
        }
    }

    public boolean isWakeLockHeld() {
        boolean zIsHeld;
        synchronized (this.mSetRadioAccessFamilyStatus) {
            zIsHeld = this.mWakeLock.isHeld();
        }
        return zIsHeld;
    }

    protected void resetRadioAccessFamilyStatusCounter() {
        this.mRadioAccessFamilyStatusCounter = this.mPhones.length;
    }

    public int getRadioAccessFamilyStatusforPhone(int i) {
        int[] iArr = this.mSetRadioAccessFamilyStatus;
        if (iArr == null || iArr.length <= i) {
            return 0;
        }
        return iArr[i];
    }

    protected void sendRadioCapabilityRequest(int i, int i2, int i3, int i4, String str, int i5, int i6) {
        this.mPhones[i].setRadioCapability(new RadioCapability(i, i2, i3, i4, str, i5), this.mHandler.obtainMessage(i6));
    }

    public int getMaxRafSupported() {
        int[] iArr = new int[this.mPhones.length];
        int i = 0;
        int radioAccessFamily = 0;
        int i2 = 0;
        while (true) {
            Phone[] phoneArr = this.mPhones;
            if (i >= phoneArr.length) {
                return radioAccessFamily;
            }
            int iBitCount = Integer.bitCount(phoneArr[i].getRadioAccessFamily());
            iArr[i] = iBitCount;
            if (i2 < iBitCount) {
                radioAccessFamily = this.mPhones[i].getRadioAccessFamily();
                i2 = iBitCount;
            }
            i++;
        }
    }

    public int getMinRafSupported() {
        int[] iArr = new int[this.mPhones.length];
        int i = 0;
        int radioAccessFamily = 0;
        int i2 = 0;
        while (true) {
            Phone[] phoneArr = this.mPhones;
            if (i >= phoneArr.length) {
                return radioAccessFamily;
            }
            int iBitCount = Integer.bitCount(phoneArr[i].getRadioAccessFamily());
            iArr[i] = iBitCount;
            if (i2 == 0 || i2 > iBitCount) {
                radioAccessFamily = this.mPhones[i].getRadioAccessFamily();
                i2 = iBitCount;
            }
            i++;
        }
    }

    protected String getLogicalModemIdFromRaf(int i) {
        int i2 = 0;
        while (true) {
            Phone[] phoneArr = this.mPhones;
            if (i2 >= phoneArr.length) {
                return null;
            }
            if (phoneArr[i2].getRadioAccessFamily() == i) {
                return this.mPhones[i2].getModemUuId();
            }
            i2++;
        }
    }

    protected void logd(String str) {
        Rlog.d("ProxyController", str);
    }

    protected void loge(String str) {
        Rlog.e("ProxyController", str);
    }
}
