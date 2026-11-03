package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.MiuiAdnRecord;
import com.android.internal.telephony.uicc.MiuiAdnRecordCache;
import com.android.internal.telephony.uicc.MiuiUiccControllerImpl;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.telephony.MiuiTelephony;
import miui.telephony.PhoneDebug;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class MiuiIccPhoneBookInterfaceManager {
    protected static final boolean DBG = true;
    protected static final int EVENT_BPR_LOADED = 4;
    protected static final int EVENT_GET_SIZE_DONE = 1;
    protected static final int EVENT_LOAD_DONE = 2;
    protected static final int EVENT_UPDATE_DONE = 3;
    private static final String LOG_TAG = "MiuiIccPhoneBookInterfaceManager";
    private static final HandlerThread mHandlerThread = new HandlerThread(LOG_TAG);
    protected MiuiAdnRecordCache mAdnCache;
    protected IccPbHandler mBaseHandler;
    private int mError;
    private int mHasPbr = -1;
    protected Phone mPhone;

    private static final class Request {
        Object mResult;
        AtomicBoolean mStatus;

        private Request() {
            this.mStatus = new AtomicBoolean(false);
            this.mResult = null;
        }
    }

    static {
        mHandlerThread.start();
    }

    protected class IccPbHandler extends Handler {
        public IccPbHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Request request = (Request) asyncResult.userObj;
            int i = message.what;
            boolean z = MiuiIccPhoneBookInterfaceManager.DBG;
            switch (i) {
                case 1:
                    int[] iArr = null;
                    if (asyncResult.exception == null) {
                        iArr = (int[]) asyncResult.result;
                        MiuiIccPhoneBookInterfaceManager.this.mError = 0;
                        MiuiIccPhoneBookInterfaceManager.this.logd("GET_RECORD_SIZE Size " + iArr[0] + " total " + iArr[1] + " #record " + iArr[2]);
                    } else {
                        MiuiIccPhoneBookInterfaceManager.this.mError = MiuiIccProviderException.getErrorCauseFromException(asyncResult.exception);
                    }
                    notifyPending(request, iArr);
                    break;
                case 2:
                    Object obj = null;
                    if (asyncResult.exception == null) {
                        obj = (List) asyncResult.result;
                        MiuiIccPhoneBookInterfaceManager.this.mError = 0;
                    } else {
                        MiuiIccPhoneBookInterfaceManager.this.logd("Cannot load ADN records");
                        MiuiIccPhoneBookInterfaceManager.this.mError = MiuiIccProviderException.getErrorCauseFromException(asyncResult.exception);
                    }
                    notifyPending(request, obj);
                    break;
                case 3:
                    if (asyncResult.exception != null) {
                        z = false;
                    }
                    MiuiIccPhoneBookInterfaceManager miuiIccPhoneBookInterfaceManager = MiuiIccPhoneBookInterfaceManager.this;
                    if (z) {
                        miuiIccPhoneBookInterfaceManager.mError = 0;
                    } else {
                        miuiIccPhoneBookInterfaceManager.mError = MiuiIccProviderException.getErrorCauseFromException(asyncResult.exception);
                    }
                    notifyPending(request, Boolean.valueOf(z));
                    break;
                case 4:
                    MiuiIccPhoneBookInterfaceManager.this.mHasPbr = asyncResult.exception != null ? 0 : 1;
                    MiuiIccPhoneBookInterfaceManager.this.logd("EVENT_BPR_LOADED " + MiuiIccPhoneBookInterfaceManager.this.mHasPbr);
                    break;
            }
        }

        private void notifyPending(Request request, Object result) {
            if (request != null) {
                synchronized (request) {
                    request.mResult = result;
                    request.mStatus.set(MiuiIccPhoneBookInterfaceManager.DBG);
                    request.notifyAll();
                }
            }
        }
    }

    public MiuiIccPhoneBookInterfaceManager(Phone phone) {
        this.mPhone = phone;
        IccRecords r = (IccRecords) phone.mIccRecords.get();
        this.mBaseHandler = new IccPbHandler(mHandlerThread.getLooper());
        if (r != null) {
            MiuiUiccControllerImpl uiccImpl = (MiuiUiccControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiUiccControllerImpl.class, phone.getPhoneId());
            if (uiccImpl != null) {
                this.mAdnCache = uiccImpl.getMiuiAdnCache(phone, r);
            }
            this.mPhone.getIccFileHandler().getEFLinearRecordSize(20272, this.mBaseHandler.obtainMessage(4));
        }
    }

    private void cleanUp() {
        logd("cleanUp");
        if (this.mAdnCache != null) {
            this.mAdnCache.reset();
            this.mAdnCache = null;
        }
        this.mHasPbr = -1;
    }

    public void dispose() {
        cleanUp();
    }

    public void updateIccRecords(IccRecords iccRecords) {
        if (iccRecords != null) {
            logd("updateIccRecords");
            MiuiUiccControllerImpl uiccImpl = (MiuiUiccControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiUiccControllerImpl.class, this.mPhone.getPhoneId());
            if (uiccImpl != null) {
                this.mAdnCache = uiccImpl.getMiuiAdnCache(this.mPhone, iccRecords);
            }
            this.mPhone.getIccFileHandler().getEFLinearRecordSize(20272, this.mBaseHandler.obtainMessage(4));
            return;
        }
        cleanUp();
    }

    protected void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    protected void loge(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    public boolean updateAdnRecordsInEfBySearch(int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) throws Throwable {
        Throwable th;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        logd("updateAdnRecordsInEfBySearch: efid=" + efid + " (" + (PhoneDebug.VDBG ? oldTag : TelephonyUtils.pii(oldTag)) + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + PhoneNumberUtils.toLogSafePhoneNumber(oldPhoneNumber) + ")==> (" + (PhoneDebug.VDBG ? newTag : TelephonyUtils.pii(newTag)) + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + PhoneNumberUtils.toLogSafePhoneNumber(newPhoneNumber) + ") pin2=" + PhoneNumberUtils.toLogSafePhoneNumber(pin2));
        int efid2 = updateEfForIccType(efid);
        checkThread();
        Request updateRequest = new Request();
        synchronized (updateRequest) {
            try {
                try {
                    Message response = this.mBaseHandler.obtainMessage(3, updateRequest);
                    MiuiAdnRecord oldAdn = new MiuiAdnRecord(oldTag, oldPhoneNumber);
                    MiuiAdnRecord newAdn = new MiuiAdnRecord(newTag, newPhoneNumber);
                    if (this.mAdnCache != null) {
                        this.mAdnCache.updateAdnBySearch(efid2, oldAdn, newAdn, pin2, response);
                        waitForResult(updateRequest);
                        return ((Boolean) updateRequest.mResult).booleanValue();
                    }
                    loge("Failure while trying to update by search due to uninitialised adncache");
                    this.mError = MiuiIccProviderException.ERROR_NOT_READY;
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    public boolean updateAdnRecordsInEfByIndex(int efid, String newTag, String newPhoneNumber, int index, String pin2) throws Throwable {
        Throwable th;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        logd("updateAdnRecordsInEfByIndex: efid=" + efid + " Index=" + index + " ==> (" + (PhoneDebug.VDBG ? newTag : TelephonyUtils.pii(newTag)) + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + PhoneNumberUtils.toLogSafePhoneNumber(newPhoneNumber) + ") pin2=" + PhoneNumberUtils.toLogSafePhoneNumber(pin2));
        checkThread();
        Request updateRequest = new Request();
        synchronized (updateRequest) {
            try {
                try {
                    Message response = this.mBaseHandler.obtainMessage(3, updateRequest);
                    MiuiAdnRecord newAdn = new MiuiAdnRecord(newTag, newPhoneNumber);
                    if (this.mAdnCache != null) {
                        this.mAdnCache.updateAdnByIndex(efid, newAdn, index, pin2, response);
                        waitForResult(updateRequest);
                    } else {
                        loge("Failure while trying to update by index due to uninitialised adncache");
                    }
                    return ((Boolean) updateRequest.mResult).booleanValue();
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    public int[] getAdnRecordsSize(int efid) {
        logd("getAdnRecordsSize: efid=" + efid);
        checkThread();
        Request getSizeRequest = new Request();
        synchronized (getSizeRequest) {
            Message response = this.mBaseHandler.obtainMessage(1, getSizeRequest);
            IccFileHandler fh = this.mPhone.getIccFileHandler();
            if (fh != null) {
                fh.getEFLinearRecordSize(efid, response);
                waitForResult(getSizeRequest);
            }
        }
        return getSizeRequest.mResult == null ? new int[3] : (int[]) getSizeRequest.mResult;
    }

    public List<MiuiAdnRecord> getAdnRecordsInEf(int efid) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        int efid2 = updateEfForIccType(efid);
        logd("getAdnRecordsInEF: efid=" + efid2);
        checkThread();
        Request loadRequest = new Request();
        synchronized (loadRequest) {
            Message response = this.mBaseHandler.obtainMessage(2, loadRequest);
            if (this.mAdnCache != null) {
                this.mAdnCache.requestLoadAllAdnLike(efid2, this.mAdnCache.extensionEfForEf(efid2), response);
                waitForResult(loadRequest);
            } else {
                loge("Failure while trying to load from SIM due to uninitialised adncache");
            }
        }
        return (List) loadRequest.mResult;
    }

    protected void checkThread() {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            loge("query() called on the main UI thread!");
            throw new IllegalStateException("You cannot call query on this provder from the main UI thread.");
        }
    }

    protected void waitForResult(Request request) {
        synchronized (request) {
            while (!request.mStatus.get()) {
                try {
                    request.wait();
                } catch (InterruptedException e) {
                    logd("interrupted while trying to update by search");
                }
            }
        }
    }

    private int updateEfForIccType(int efid) {
        if (efid == 28474 && this.mHasPbr == 1) {
            logd("Translate EF_ADN to EF_PBR");
            return 20272;
        }
        return efid;
    }

    public boolean isPhoneBookReady() {
        if (this.mPhone.getIccFileHandler() == null || !this.mPhone.getIccRecordsLoaded() || this.mHasPbr == -1 || this.mAdnCache == null) {
            return false;
        }
        return DBG;
    }

    public boolean isPhoneBookLoaded() {
        if (isPhoneBookReady() && this.mAdnCache.isAllAdnRecordsLoaded()) {
            return DBG;
        }
        return false;
    }

    public boolean isUsimPhoneBookRecords() {
        if (this.mHasPbr == 1) {
            return DBG;
        }
        return false;
    }

    public int getFreeAdn() {
        return this.mAdnCache.getFreeAdn(updateEfForIccType(28474));
    }

    public int getAdnCapacity() {
        return this.mAdnCache.getAdnCapacity(updateEfForIccType(28474));
    }

    public int getLastError() {
        return this.mError;
    }

    public boolean updateUsimPhoneBookRecordsByIndex(int efid, MiuiAdnRecord adn, int index) throws Throwable {
        Throwable th;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        int efid2 = updateEfForIccType(efid);
        checkThread();
        Request updateRequest = new Request();
        synchronized (updateRequest) {
            try {
                try {
                    Message response = this.mBaseHandler.obtainMessage(3, updateRequest);
                    if (this.mAdnCache != null) {
                        this.mAdnCache.updateAdnByIndex(efid2, adn, index, null, response);
                        waitForResult(updateRequest);
                    } else {
                        loge("Failure while trying to update usim phonebook records by index due to uninitialised adncache");
                    }
                    return ((Boolean) updateRequest.mResult).booleanValue();
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    public boolean updateUsimPhoneBookRecordsBySearch(int efid, MiuiAdnRecord oldAdn, MiuiAdnRecord newAdn) throws Throwable {
        Throwable th;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        int efid2 = updateEfForIccType(efid);
        checkThread();
        Request updateRequest = new Request();
        synchronized (updateRequest) {
            try {
                try {
                    Message response = this.mBaseHandler.obtainMessage(3, updateRequest);
                    if (this.mAdnCache != null) {
                        this.mAdnCache.updateAdnBySearch(efid2, oldAdn, newAdn, null, response);
                        waitForResult(updateRequest);
                    } else {
                        loge("Failure while trying to update usim phonebook records by search due to uninitialised adncache");
                    }
                    return ((Boolean) updateRequest.mResult).booleanValue();
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    public boolean updateUsimPhoneBookRecordsBySearchForSprd(int efid, MiuiAdnRecord oldAdn, MiuiAdnRecord newAdn) {
        return false;
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("MiuiIccPhoneBookInterfaceManager: ");
        pw.println(" mHasPbr=" + this.mHasPbr);
        if (this.mAdnCache != null) {
            pw.println(" AdnCapacity=" + getAdnCapacity());
            pw.println(" FreeAdn=" + getFreeAdn());
            this.mAdnCache.dump(fd, pw, args);
            return;
        }
        pw.println(" mAdnCache=null");
    }
}
