package com.mediatek.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.RadioCapability;
import com.mediatek.internal.telephony.devreg.DeviceRegisterController;
import com.mediatek.internal.telephony.phb.MtkUiccPhoneBookController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MtkProxyController extends ProxyController {
    private static final int EVENT_ON_REQUEST = 6;
    private CommandsInterface[] mCi;
    RadioAccessFamily[] mCurrRafs;
    private DeviceRegisterController mDeviceRegisterController;
    private Handler mMtkHandler;
    private MtkUiccPhoneBookController mMtkUiccPhoneBookController;
    private MtkUiccSmsController mMtkUiccSmsController;
    RadioAccessFamily[] mNextRafs;

    public MtkProxyController(Context context, FeatureFlags flags) {
        super(context, flags);
        this.mNextRafs = null;
        this.mCurrRafs = null;
        this.mMtkHandler = new Handler() { // from class: com.mediatek.internal.telephony.MtkProxyController.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                MtkProxyController.this.mtkLogd("mtkHandleMessage msg.what=" + msg.what);
                switch (msg.what) {
                    case 6:
                        MtkProxyController.this.onSetRadioCapabilityRequest(MtkProxyController.this.mCurrRafs);
                        break;
                }
            }
        };
        this.mCi = PhoneFactory.getCommandsInterfaces();
        mtkLogd("Constructor - Enter");
        this.mMtkUiccPhoneBookController = new MtkUiccPhoneBookController();
        this.mMtkUiccSmsController = new MtkUiccSmsController();
        mtkLogd("Constructor - Exit");
        this.mDeviceRegisterController = new DeviceRegisterController(this.mContext, this.mMtkUiccSmsController);
    }

    public DeviceRegisterController getDeviceRegisterController() {
        return this.mDeviceRegisterController;
    }

    public boolean setRadioCapability(RadioAccessFamily[] rafs) {
        if (SystemProperties.getBoolean("ro.vendor.mtk_disable_cap_switch", false)) {
            completeRadioCapabilityTransaction();
            mtkLogd("skip switching because mtk_disable_cap_switch is true");
        } else {
            Message tmsg = this.mMtkHandler.obtainMessage(6);
            this.mCurrRafs = rafs;
            this.mMtkHandler.sendMessage(tmsg);
        }
        return true;
    }

    public boolean onSetRadioCapabilityRequest(RadioAccessFamily[] rafs) {
        boolean ret = super.setRadioCapability(rafs);
        if (!ret) {
            this.mNextRafs = rafs;
        }
        return ret;
    }

    protected void onStartRadioCapabilityResponse(Message msg) {
        synchronized (this.mSetRadioAccessFamilyStatus) {
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar.exception != null) {
                mtkLogd("onStartRadioCapabilityResponse got exception=" + ar.exception);
                this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
                sendCapabilityFailBroadcast();
                resetSimSwitchState();
                return;
            }
            RadioCapability rc = (RadioCapability) ((AsyncResult) msg.obj).result;
            if (rc != null && rc.getSession() == this.mRadioCapabilitySessionId) {
                boolean z = true;
                this.mRadioAccessFamilyStatusCounter--;
                int id = rc.getPhoneId();
                if (((AsyncResult) msg.obj).exception != null) {
                    mtkLogd("onStartRadioCapabilityResponse: Error response session=" + rc.getSession());
                    mtkLogd("onStartRadioCapabilityResponse: phoneId=" + id + " status=FAIL");
                    this.mSetRadioAccessFamilyStatus[id] = 5;
                    this.mTransactionFailed = true;
                } else {
                    mtkLogd("onStartRadioCapabilityResponse: phoneId=" + id + " status=STARTED");
                    this.mSetRadioAccessFamilyStatus[id] = 2;
                }
                if (this.mRadioAccessFamilyStatusCounter == 0) {
                    StringBuilder sbAppend = new StringBuilder().append("onStartRadioCapabilityResponse: success=");
                    if (this.mTransactionFailed) {
                        z = false;
                    }
                    mtkLogd(sbAppend.append(z).toString());
                    if (this.mTransactionFailed) {
                        issueFinish(this.mRadioCapabilitySessionId);
                    } else {
                        resetRadioAccessFamilyStatusCounter();
                        for (int i = 0; i < this.mPhones.length; i++) {
                            sendRadioCapabilityRequest(i, this.mRadioCapabilitySessionId, 2, this.mNewRadioAccessFamily[i], this.mNewLogicalModemIds[i], 0, 3);
                            mtkLogd("onStartRadioCapabilityResponse: phoneId=" + i + " status=APPLYING");
                            this.mSetRadioAccessFamilyStatus[i] = 3;
                        }
                    }
                }
                return;
            }
            mtkLogd("onStartRadioCapabilityResponse: Ignore session=" + this.mRadioCapabilitySessionId + " rc=" + rc);
        }
    }

    protected void onApplyRadioCapabilityErrorHandler(Message msg) {
        this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
        sendCapabilityFailBroadcast();
        resetSimSwitchState();
    }

    protected void onNotificationRadioCapabilityChanged(Message msg) {
        RadioCapability rc = (RadioCapability) ((AsyncResult) msg.obj).result;
        if (rc == null) {
            logd("onNotificationRadioCapabilityChanged: rc == null");
            return;
        }
        logd("onNotificationRadioCapabilityChanged: rc=" + rc);
        int id = rc.getPhoneId();
        if (((AsyncResult) msg.obj).exception == null && (rc.getStatus() == 2 || rc.getSession() != this.mRadioCapabilitySessionId)) {
            logd("onNotificationRadioCapabilityChanged: update phone capability");
            this.mPhones[id].radioCapabilityUpdated(rc, true);
        }
        super.onNotificationRadioCapabilityChanged(msg);
    }

    protected void onFinishRadioCapabilityResponse(Message msg) {
        RadioCapability rc = (RadioCapability) ((AsyncResult) msg.obj).result;
        if ((rc == null || rc.getSession() != this.mRadioCapabilitySessionId) && ((AsyncResult) msg.obj).exception != null) {
            synchronized (this.mSetRadioAccessFamilyStatus) {
                mtkLogd("onFinishRadioCapabilityResponse C2K mRadioAccessFamilyStatusCounter=" + this.mRadioAccessFamilyStatusCounter);
                this.mRadioAccessFamilyStatusCounter--;
                if (this.mRadioAccessFamilyStatusCounter == 0) {
                    completeRadioCapabilityTransaction();
                }
                mtkLoge("onFinishRadioCapabilityResponse: exception=" + ((AsyncResult) msg.obj).exception);
            }
            return;
        }
        super.onFinishRadioCapabilityResponse(msg);
    }

    protected void onTimeoutRadioCapability(Message msg) {
        Message tmsg = this.mHandler.obtainMessage(5, this.mRadioCapabilitySessionId, 0);
        this.mHandler.sendMessageDelayed(tmsg, 45000L);
    }

    protected void completeRadioCapabilityTransaction() {
        Intent intent;
        mtkLogd("onFinishRadioCapabilityResponse: success=" + (!this.mTransactionFailed));
        if (!this.mTransactionFailed) {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
            for (int i = 0; i < this.mPhones.length; i++) {
                int raf = this.mPhones[i].getRadioAccessFamily();
                mtkLogd("radioAccessFamily[" + i + "]=" + raf);
                RadioAccessFamily phoneRC = new RadioAccessFamily(i, raf);
                arrayList.add(phoneRC);
            }
            intent = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
            intent.putParcelableArrayListExtra("rafs", arrayList);
            this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
            resetSimSwitchState();
        } else {
            intent = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
            this.mTransactionFailed = false;
            resetSimSwitchState();
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PHONE_STATE");
        if (this.mNextRafs != null) {
            mtkLogd("has next request, trigger it");
            try {
                if (!setRadioCapability(this.mNextRafs)) {
                    sendCapabilityFailBroadcast();
                } else {
                    this.mNextRafs = null;
                }
            } catch (RuntimeException e) {
                sendCapabilityFailBroadcast();
            }
        }
    }

    private void resetSimSwitchState() {
        if (isCapabilitySwitching()) {
            this.mHandler.removeMessages(5);
        }
        clearTransaction();
    }

    protected void mtkLogd(String string) {
        Rlog.d("MtkProxyController", string);
    }

    protected void mtkLoge(String string) {
        Rlog.e("MtkProxyController", string);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        try {
            this.mPhoneSwitcher.dump(fd, pw, args);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean isCapabilitySwitching() {
        int i;
        synchronized (this.mSetRadioAccessFamilyStatus) {
            while (i < this.mPhones.length) {
                i = (this.mSetRadioAccessFamilyStatus[i] == 2 || this.mSetRadioAccessFamilyStatus[i] == 3 || this.mSetRadioAccessFamilyStatus[i] == 4) ? 0 : i + 1;
                mtkLogd("isCapabilitySwitching: Phone[" + i + "] status is " + this.mSetRadioAccessFamilyStatus[i]);
                return true;
            }
            return false;
        }
    }

    private void sendCapabilityFailBroadcast() {
        if (this.mContext != null) {
            Intent intent = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }
}
