package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IsimFileHandler;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccProfile;

/* JADX INFO: loaded from: classes.dex */
public class MtkUiccCardApplication extends UiccCardApplication {
    public static final int CAT_CORPORATE = 3;
    public static final int CAT_NETOWRK_SUBSET = 1;
    public static final int CAT_NETWOEK = 0;
    public static final int CAT_NS_SP = 5;
    public static final int CAT_SERVICE_PROVIDER = 2;
    public static final int CAT_SIM = 4;
    public static final int CAT_SIM_C = 6;
    private static final boolean DBG = true;
    private static final int EVENT_CHANGE_NETWORK_LOCK_DONE = 102;
    private static final int EVENT_QUERY_NETWORK_LOCK_DONE = 101;
    private static final String LOG_TAG_EX = "MtkUiccCardApp";
    public static final int OP_ADD = 2;
    public static final int OP_LOCK = 1;
    public static final int OP_PERMANENT_UNLOCK = 4;
    public static final int OP_REMOVE = 3;
    public static final int OP_UNLOCK = 0;
    static final String[] UICCCARDAPPLICATION_PROPERTY_RIL_UICC_TYPE = {"vendor.gsm.ril.uicctype", "vendor.gsm.ril.uicctype.2", "vendor.gsm.ril.uicctype.3", "vendor.gsm.ril.uicctype.4"};
    private RegistrantList mFdnChangedRegistrants;
    private Handler mHandlerEx;
    protected String mIccType;
    protected int mPhoneId;

    public MtkUiccCardApplication(UiccProfile uiccProfile, IccCardApplicationStatus as, Context c, CommandsInterface ci, FeatureFlags featureFlags) {
        super(uiccProfile, as, c, ci, featureFlags);
        this.mIccType = null;
        this.mHandlerEx = new Handler() { // from class: com.mediatek.internal.telephony.uicc.MtkUiccCardApplication.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (MtkUiccCardApplication.this.mDestroyed) {
                    if (101 == msg.what) {
                        Message response = (Message) ((AsyncResult) msg.obj).userObj;
                        AsyncResult.forMessage(response).exception = CommandException.fromRilErrno(1);
                        MtkUiccCardApplication.this.mtkLoge("Received message " + msg + "[" + msg.what + "] while being destroyed. return exception.");
                        response.arg1 = -1;
                        response.sendToTarget();
                        return;
                    }
                    MtkUiccCardApplication.this.mtkLoge("Received message " + msg + "[" + msg.what + "] while being destroyed. Ignoring.");
                }
                switch (msg.what) {
                    case 101:
                        MtkUiccCardApplication.this.mtkLog("handleMessage (EVENT_QUERY_NETWORK_LOCK)");
                        AsyncResult ar = (AsyncResult) msg.obj;
                        if (ar.exception != null) {
                            Rlog.e(MtkUiccCardApplication.LOG_TAG_EX, "Error query network lock with exception " + ar.exception);
                        }
                        AsyncResult.forMessage((Message) ar.userObj, ar.result, ar.exception);
                        ((Message) ar.userObj).sendToTarget();
                        break;
                    case 102:
                        MtkUiccCardApplication.this.mtkLog("handleMessage (EVENT_CHANGE_NETWORK_LOCK)");
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        if (ar2.exception != null) {
                            Rlog.e(MtkUiccCardApplication.LOG_TAG_EX, "Error change network lock with exception " + ar2.exception);
                        }
                        AsyncResult.forMessage((Message) ar2.userObj).exception = ar2.exception;
                        ((Message) ar2.userObj).sendToTarget();
                        break;
                    default:
                        MtkUiccCardApplication.this.mtkLoge("Unknown Event " + msg.what);
                        break;
                }
            }
        };
        this.mFdnChangedRegistrants = new RegistrantList();
        this.mPhoneId = getPhoneId();
    }

    protected void onChangeFdnDone(AsyncResult ar) {
        super.onChangeFdnDone(ar);
        if (ar.exception == null) {
            mtkLog("notifyFdnChangedRegistrants");
            notifyFdnChangedRegistrants();
        }
    }

    public void queryIccNetworkLock(int category, Message onComplete) {
        mtkLog("queryIccNetworkLock(): category =  " + category);
        switch (category) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                this.mCi.queryNetworkLock(category, this.mHandlerEx.obtainMessage(101, onComplete));
                break;
            default:
                Rlog.e(LOG_TAG_EX, "queryIccNetworkLock unknown category = " + category);
                break;
        }
    }

    public void setIccNetworkLockEnabled(int category, int lockop, String password, String data_imsi, String gid1, String gid2, Message onComplete) {
        mtkLog("SetIccNetworkEnabled(): category = " + category + " lockop = " + lockop);
        switch (lockop) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                this.mCi.setNetworkLock(category, lockop, password, data_imsi, gid1, gid2, this.mHandlerEx.obtainMessage(102, onComplete));
                break;
            default:
                Rlog.e(LOG_TAG_EX, "SetIccNetworkEnabled unknown operation" + lockop);
                break;
        }
    }

    public void registerForFdnChanged(Handler h, int what, Object obj) {
        synchronized (this.mLock) {
            Registrant r = new Registrant(h, what, obj);
            this.mFdnChangedRegistrants.add(r);
        }
    }

    public void unregisterForFdnChanged(Handler h) {
        synchronized (this.mLock) {
            this.mFdnChangedRegistrants.remove(h);
        }
    }

    private void notifyFdnChangedRegistrants() {
        if (this.mDestroyed) {
            return;
        }
        this.mFdnChangedRegistrants.notifyRegistrants();
    }

    public String getIccCardType() {
        if (this.mIccType == null || this.mIccType.equals("")) {
            this.mIccType = SystemProperties.get(UICCCARDAPPLICATION_PROPERTY_RIL_UICC_TYPE[this.mPhoneId]);
        }
        mtkLog("getIccCardType(): mIccType = " + this.mIccType);
        return this.mIccType;
    }

    public boolean getIccFdnAvailable() {
        IccServiceInfo.IccServiceStatus iccSerStatus;
        if (this.mIccRecords == null) {
            mtkLoge("isFdnExist mIccRecords == null");
            return false;
        }
        IccServiceInfo.IccServiceStatus iccServiceStatus = IccServiceInfo.IccServiceStatus.NOT_EXIST_IN_USIM;
        boolean isPhbReady = false;
        if (this.mIccRecords instanceof MtkSIMRecords) {
            iccSerStatus = this.mIccRecords.getSIMServiceStatus(IccServiceInfo.IccService.FDN);
            isPhbReady = this.mIccRecords.isPhbReady();
        } else if (this.mIccRecords instanceof RuimRecords) {
            iccSerStatus = this.mIccRecords.getSIMServiceStatus(IccServiceInfo.IccService.FDN);
            isPhbReady = this.mIccRecords.isPhbReady();
        } else {
            iccSerStatus = IccServiceInfo.IccServiceStatus.NOT_EXIST_IN_USIM;
        }
        log("getIccFdnAvailable status iccSerStatus:" + iccSerStatus);
        if (iccSerStatus == IccServiceInfo.IccServiceStatus.ACTIVATED && isPhbReady) {
            return DBG;
        }
        return false;
    }

    protected IccRecords onCreateSimRecords(Context c, CommandsInterface ci) {
        return new MtkSIMRecords(this, c, ci);
    }

    protected IccRecords onCreateRuimRecords(Context c, CommandsInterface ci) {
        return new MtkRuimRecords(this, c, ci);
    }

    protected IccRecords onCreateIsimUiccRecords(Context c, CommandsInterface ci, FeatureFlags featureFlags) {
        return new MtkIsimUiccRecords(this, c, ci, featureFlags);
    }

    protected IccFileHandler onCreateSIMFileHandler() {
        return new MtkSIMFileHandler(this, this.mAid, this.mCi);
    }

    protected IccFileHandler onCreateRuimFileHandler() {
        return new MtkRuimFileHandler(this, this.mAid, this.mCi);
    }

    protected IccFileHandler onCreateUsimFileHandler() {
        return new MtkUsimFileHandler(this, this.mAid, this.mCi);
    }

    protected IccFileHandler onCreateCsimFileHandler() {
        return new MtkCsimFileHandler(this, this.mAid, this.mCi);
    }

    protected IccFileHandler onCreateIsimFileHandler() {
        return new IsimFileHandler(this, this.mAid, this.mCi);
    }

    protected void log(String msg) {
        Rlog.d("UiccCardApplication", msg + " (slot " + this.mPhoneId + ")");
    }

    protected void loge(String msg) {
        Rlog.e("UiccCardApplication", msg + " (slot " + this.mPhoneId + ")");
    }

    protected void mtkLog(String msg) {
        Rlog.d(LOG_TAG_EX, msg + " (slot " + this.mPhoneId + ")");
    }

    protected void mtkLoge(String msg) {
        Rlog.e(LOG_TAG_EX, msg + " (slot " + this.mPhoneId + ")");
    }
}
