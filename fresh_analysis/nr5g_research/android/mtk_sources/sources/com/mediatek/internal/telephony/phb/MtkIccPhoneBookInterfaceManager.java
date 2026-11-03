package com.mediatek.internal.telephony.phb;

import android.content.ContentValues;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.mediatek.internal.telephony.uicc.MtkRuimRecords;
import com.mediatek.internal.telephony.uicc.MtkSIMRecords;
import java.util.List;
import java.util.Locale;
import mediatek.telephony.MtkTelephony;

/* JADX INFO: loaded from: classes.dex */
public class MtkIccPhoneBookInterfaceManager extends IccPhoneBookInterfaceManager {
    protected static final boolean DBG = !SystemProperties.get("ro.build.type").equals("user");
    static final String LOG_TAG = "MtkIccPhoneBookIM";
    private int mErrorCause;
    private IccRecords mIccRecords;
    private volatile MtkAdnRecordCache mMtkAdnCache;
    protected Handler mMtkBaseHandler;
    private List<MtkAdnRecord> mRecords;
    private int mSlotId;

    public MtkIccPhoneBookInterfaceManager(Phone phone) {
        super(phone);
        this.mSlotId = -1;
        this.mMtkBaseHandler = new Handler() { // from class: com.mediatek.internal.telephony.phb.MtkIccPhoneBookInterfaceManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                IccPhoneBookInterfaceManager.Request request = (IccPhoneBookInterfaceManager.Request) ar.userObj;
                switch (msg.what) {
                    case 2:
                        List<MtkAdnRecord> records = null;
                        if (ar.exception == null) {
                            records = (List) ar.result;
                        } else {
                            MtkIccPhoneBookInterfaceManager.this.mtkLoge("EVENT_LOAD_DONE: Cannot load ADN records; ex=" + ar.exception);
                        }
                        notifyPending(request, records);
                        break;
                    case 3:
                        MtkIccPhoneBookInterfaceManager.this.mtkLogd("EVENT_UPDATE_DONE");
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        boolean success = ar2.exception == null;
                        MtkIccPhoneBookInterfaceManager.this.mtkLogd("EVENT_UPDATE_DONE success:" + success);
                        if (!success) {
                            boolean z = ar2.exception instanceof CommandException;
                            MtkIccPhoneBookInterfaceManager mtkIccPhoneBookInterfaceManager = MtkIccPhoneBookInterfaceManager.this;
                            if (z) {
                                mtkIccPhoneBookInterfaceManager.mErrorCause = MtkIccPhoneBookInterfaceManager.this.getErrorCauseFromException(ar2.exception);
                            } else {
                                mtkIccPhoneBookInterfaceManager.mtkLoge("Error : Unknow exception instance");
                                MtkIccPhoneBookInterfaceManager.this.mErrorCause = -10;
                            }
                        } else {
                            MtkIccPhoneBookInterfaceManager.this.mErrorCause = 1;
                        }
                        MtkIccPhoneBookInterfaceManager.this.mtkLogi("update done result: " + MtkIccPhoneBookInterfaceManager.this.mErrorCause);
                        notifyPending(request, Boolean.valueOf(success));
                        break;
                    default:
                        MtkIccPhoneBookInterfaceManager.this.mBaseHandler.handleMessage(msg);
                        break;
                }
            }

            private void notifyPending(IccPhoneBookInterfaceManager.Request request, Object result) {
                if (request != null) {
                    synchronized (request) {
                        request.mResult = result;
                        request.mStatus.set(true);
                        request.notifyAll();
                    }
                }
            }
        };
    }

    public void updateIccRecords(IccRecords iccRecords) {
        super.updateIccRecords(iccRecords);
        this.mIccRecords = iccRecords;
        if (iccRecords != null) {
            this.mMtkAdnCache = (MtkAdnRecordCache) iccRecords.getAdnCache();
            if (this.mMtkAdnCache != null) {
                this.mSlotId = this.mMtkAdnCache.getSlotId();
            } else {
                mtkLogi("[updateIccRecords] mMtkAdnCache == null");
                this.mSlotId = -1;
            }
            mtkLogi("[updateIccRecords] Set mMtkAdnCache value slotid:" + this.mSlotId);
            return;
        }
        this.mMtkAdnCache = null;
        mtkLogi("[updateIccRecords] Set mMtkAdnCache value to null");
        this.mSlotId = -1;
    }

    protected void mtkLogd(String msg) {
        Rlog.d(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    protected void mtkLoge(String msg) {
        Rlog.e(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    protected void mtkLogi(String msg) {
        Rlog.i(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    public boolean updateAdnRecordsInEfBySearch(int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) {
        int result = updateAdnRecordsInEfBySearchWithError(efid, oldTag, oldPhoneNumber, newTag, newPhoneNumber, pin2);
        return result == 1;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    public synchronized int updateAdnRecordsInEfBySearchWithError(int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) {
        int index = -1;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateAdnRecordsInEfBySearchWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateAdnRecordsInEfBySearch: efid=0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
        }
        int efid2 = updateEfForIccType(efid);
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        try {
            synchronized (updateRequest) {
                try {
                    Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
                    MtkAdnRecord oldAdn = new MtkAdnRecord(oldTag, oldPhoneNumber);
                    if (newPhoneNumber == null) {
                        newPhoneNumber = "";
                    }
                    if (newTag == null) {
                        newTag = "";
                    }
                    MtkAdnRecord newAdn = new MtkAdnRecord(newTag, newPhoneNumber);
                    if (this.mMtkAdnCache != null) {
                        index = this.mMtkAdnCache.updateAdnBySearch(efid2, oldAdn, newAdn, pin2, response, null);
                        waitForResult(updateRequest);
                    } else {
                        mtkLoge("Failure while trying to update by search due to uninitialised adncache");
                    }
                    if (this.mErrorCause == 1) {
                        mtkLogi("updateAdnRecordsInEfBySearchWithError success index is " + index);
                        return index;
                    }
                    return this.mErrorCause;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public boolean updateAdnRecordsInEfBySearchForSubscriber(int efid, ContentValues values, String pin2) {
        if (values == null) {
            return false;
        }
        String newTag = values.getAsString("newTag");
        String newPhoneNumber = values.getAsString("newNumber");
        String oldTag = values.getAsString("tag");
        String oldPhoneNumber = values.getAsString(MtkTelephony.SmsCb.CbChannel.NUMBER);
        int result = updateAdnRecordsInEfBySearchWithError(efid, oldTag, oldPhoneNumber, newTag, newPhoneNumber, pin2);
        if (result != 1) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public synchronized int updateUsimPBRecordsInEfBySearchWithError(int efid, String oldTag, String oldPhoneNumber, String oldAnr, String oldGrpIds, String[] oldEmails, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails) {
        int i;
        String str;
        String str2;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateUsimPBRecordsInEfBySearchWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            i = efid;
            str = newAnr;
            str2 = newGrpIds;
            mtkLogd("updateUsimPBRecordsInEfBySearchWithError: efid=" + i + "oldAnr" + oldAnr + " oldGrpIds " + oldGrpIds + "==> newAnr= " + str + " newGrpIds = " + str2);
        } else {
            i = efid;
            str = newAnr;
            str2 = newGrpIds;
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        try {
            synchronized (updateRequest) {
                try {
                    Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
                    MtkAdnRecord oldAdn = new MtkAdnRecord(oldTag, oldPhoneNumber);
                    try {
                        MtkAdnRecord newAdn = new MtkAdnRecord(0, 0, newTag == null ? "" : newTag, newPhoneNumber == null ? "" : newPhoneNumber, str, newEmails, str2);
                        int index = this.mMtkAdnCache.updateAdnBySearch(i, oldAdn, newAdn, null, response, null);
                        waitForResult(updateRequest);
                        if (this.mErrorCause != 1) {
                            return this.mErrorCause;
                        }
                        mtkLogi("updateUsimPBRecordsInEfBySearchWithError success index is " + index);
                        return index;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public synchronized int updateUsimPBRecordsBySearchWithError(int efid, MtkAdnRecord oldAdn, MtkAdnRecord newAdn) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateUsimPBRecordsBySearchWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateUsimPBRecordsBySearchWithError: efid=" + efid + " (" + oldAdn + ")==>(" + newAdn + ")");
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        try {
            synchronized (updateRequest) {
                try {
                    Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
                    if (newAdn.getNumber() == null) {
                        try {
                            newAdn.setNumber("");
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    int index = this.mMtkAdnCache.updateAdnBySearch(efid, oldAdn, newAdn, null, response, null);
                    waitForResult(updateRequest);
                    if (this.mErrorCause == 1) {
                        mtkLogi("updateUsimPBRecordsBySearchWithError success index is " + index);
                        return index;
                    }
                    return this.mErrorCause;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public boolean updateAdnRecordsInEfByIndex(int efid, String newTag, String newPhoneNumber, int index, String pin2) {
        int result = updateAdnRecordsInEfByIndexWithError(efid, newTag, newPhoneNumber, index, pin2);
        return result == 1;
    }

    public boolean updateAdnRecordsInEfByIndex(int efid, ContentValues values, int index, String pin2) {
        String newTag;
        String newPhoneNumber;
        if (values == null) {
            newTag = null;
            newPhoneNumber = null;
        } else {
            String newTag2 = values.getAsString("newTag");
            String newPhoneNumber2 = values.getAsString("newNumber");
            newTag = newTag2;
            newPhoneNumber = newPhoneNumber2;
        }
        int result = updateAdnRecordsInEfByIndexWithError(efid, newTag, newPhoneNumber, index, pin2);
        return result == 1;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 5 */
    public synchronized int updateAdnRecordsInEfByIndexWithError(int efid, String newTag, String newPhoneNumber, int index, String pin2) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateAdnRecordsInEfByIndex mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateAdnRecordsInEfByIndex: efid=0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT) + " Index=" + index);
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        try {
            synchronized (updateRequest) {
                try {
                    Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
                    if (newPhoneNumber == null) {
                        newPhoneNumber = "";
                    }
                    if (newTag == null) {
                        newTag = "";
                    }
                    MtkAdnRecord newAdn = new MtkAdnRecord(newTag, newPhoneNumber);
                    if (this.mMtkAdnCache != null) {
                        this.mMtkAdnCache.updateAdnByIndex(efid, newAdn, index, pin2, response);
                        waitForResult(updateRequest);
                    } else {
                        mtkLoge("Failure while trying to update by index due to uninitialised adncache");
                    }
                    return this.mErrorCause;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public synchronized int updateUsimPBRecordsInEfByIndexWithError(int efid, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails, int index) {
        int i;
        String newPhoneNumber2;
        String newTag2;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateUsimPBRecordsInEfByIndexWithError mMtkAdnCache is null");
            return 0;
        }
        if (!DBG) {
            i = index;
        } else {
            i = index;
            mtkLogd("updateUsimPBRecordsInEfByIndexWithError: efid=" + efid + " Index=" + i + " ==>  newAnr= " + newAnr + " newGrpIds = " + newGrpIds);
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (updateRequest) {
            try {
                Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
                if (newPhoneNumber != null) {
                    newPhoneNumber2 = newPhoneNumber;
                } else {
                    newPhoneNumber2 = "";
                }
                if (newTag != null) {
                    newTag2 = newTag;
                } else {
                    newTag2 = "";
                }
                try {
                    MtkAdnRecord newAdn = new MtkAdnRecord(efid, i, newTag2, newPhoneNumber2, newAnr, newEmails, newGrpIds);
                    this.mMtkAdnCache.updateAdnByIndex(efid, newAdn, index, null, response);
                    waitForResult(updateRequest);
                    return this.mErrorCause;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public synchronized int updateUsimPBRecordsByIndexWithError(int efid, MtkAdnRecord record, int index) {
        Throwable th;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateUsimPBRecordsByIndexWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateUsimPBRecordsByIndexWithError: efid=" + efid + " Index=" + index + " ==> " + record);
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        try {
            synchronized (updateRequest) {
                try {
                    Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
                    this.mMtkAdnCache.updateAdnByIndex(efid, record, index, null, response);
                    waitForResult(updateRequest);
                    return this.mErrorCause;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public synchronized int[] getAdnRecordsSize(int efid) {
        mtkLogd("getAdnRecordsSize: efid=" + efid);
        checkThread();
        IccPhoneBookInterfaceManager.Request getSizeRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (getSizeRequest) {
            try {
                Message response = this.mBaseHandler.obtainMessage(1, getSizeRequest);
                IccFileHandler fh = this.mPhone.getIccFileHandler();
                if (fh != null) {
                    try {
                        fh.getEFLinearRecordSize(efid, response);
                        waitForResult(getSizeRequest);
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                if (efid == 28474 && getSizeRequest.mResult != null) {
                    int[] capabilityResult = getAdnRecordsCapacityExt();
                    int[] result = new int[3];
                    result[0] = ((int[]) getSizeRequest.mResult)[0];
                    if (capabilityResult != null) {
                        result[2] = capabilityResult[0];
                        result[1] = result[0] * result[2];
                    }
                    mtkLogd("getAdnRecordsSize ADN " + result[0] + "," + result[1] + "," + result[2]);
                    return result;
                }
                if (efid == 28489 && getSizeRequest.mResult != null) {
                    int[] result2 = new int[3];
                    result2[0] = ((int[]) getSizeRequest.mResult)[0];
                    result2[2] = ((int[]) getSizeRequest.mResult)[2];
                    if (result2[0] < 25) {
                        result2[0] = 26;
                    }
                    result2[1] = result2[0] * result2[2];
                    mtkLogd("getAdnRecordsSize SDN " + result2[0] + "," + result2[1] + "," + result2[2]);
                    return result2;
                }
                return getSizeRequest.mResult == null ? new int[3] : (int[]) getSizeRequest.mResult;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public synchronized List<MtkAdnRecord> getAdnRecordsInEf(int efid, Object object) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        int efid2 = updateEfForIccType(efid);
        if (DBG) {
            mtkLogd("getAdnRecordsInEF: efid=0x" + Integer.toHexString(efid2).toUpperCase(Locale.ROOT));
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("getAdnRecordsInEF mMtkAdnCache is null");
            return null;
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request loadRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (loadRequest) {
            Message response = this.mMtkBaseHandler.obtainMessage(2, loadRequest);
            if (this.mMtkAdnCache != null) {
                this.mMtkAdnCache.requestLoadAllAdnLike(efid2, this.mMtkAdnCache.extensionEfForEf(efid2), response);
                waitForResult(loadRequest);
            } else {
                mtkLoge("Failure while trying to load from SIM due to uninitialised adncache");
            }
        }
        return (List) loadRequest.mResult;
    }

    protected void checkThread() {
        if (this.mMtkBaseHandler.getLooper().equals(Looper.myLooper())) {
            mtkLoge("query() called on the main UI thread!");
            throw new IllegalStateException("You cannot call query on this provder from the main UI thread.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getErrorCauseFromException(CommandException e) {
        if (e == null) {
            return 1;
        }
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$CommandException$Error[e.getCommandError().ordinal()]) {
            case 1:
                return -10;
            case 2:
                return -1;
            case 3:
            case 4:
                return -5;
            case 5:
                return -2;
            case 6:
                return -3;
            case 7:
                return -4;
            case 8:
                return -6;
            case 9:
                return -14;
            case 10:
            case 11:
                return -11;
            case 12:
                return -12;
            case 13:
                return -13;
            case 14:
                return -16;
            case 15:
                return -17;
            default:
                return 0;
        }
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.phb.MtkIccPhoneBookInterfaceManager$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error = new int[CommandException.Error.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.GENERIC_FAILURE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_1.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_PUK2.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.PASSWORD_INCORRECT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_2.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_3.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_4.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_5.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_6.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.RADIO_NOT_AVAILABLE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_7.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_8.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_9.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_10.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_11.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
        }
    }

    public void onPhbReady() {
        if (this.mMtkAdnCache != null) {
            this.mMtkAdnCache.requestLoadAllAdnLike(28474, this.mMtkAdnCache.extensionEfForEf(28474), null);
        }
    }

    public boolean isPhbReady() {
        if (this.mMtkAdnCache == null || !SubscriptionManager.isValidSlotIndex(this.mSlotId) || this.mIccRecords == null) {
            return false;
        }
        boolean z = this.mIccRecords instanceof MtkSIMRecords;
        MtkSIMRecords mtkSIMRecords = this.mIccRecords;
        if (z) {
            boolean phbReady = mtkSIMRecords.isPhbReady();
            return phbReady;
        }
        if (!(mtkSIMRecords instanceof MtkRuimRecords)) {
            return false;
        }
        boolean phbReady2 = this.mIccRecords.isPhbReady();
        return phbReady2;
    }

    public List<UsimGroup> getUsimGroups() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return null;
        }
        return phbMgr.getUsimGroups();
    }

    public String getUsimGroupById(int nGasId) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return null;
        }
        return phbMgr.getUsimGroupById(nGasId);
    }

    public boolean removeUsimGroupById(int nGasId) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.removeUsimGroupById(nGasId);
    }

    public int insertUsimGroup(String grpName) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.insertUsimGroup(grpName);
    }

    public int updateUsimGroup(int nGasId, String grpName) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.updateUsimGroup(nGasId, grpName);
    }

    public boolean addContactToGroup(int adnIndex, int grpIndex) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.addContactToGroup(adnIndex, grpIndex);
    }

    public boolean removeContactFromGroup(int adnIndex, int grpIndex) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.removeContactFromGroup(adnIndex, grpIndex);
    }

    public boolean updateContactToGroups(int adnIndex, int[] grpIdList) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.updateContactToGroups(adnIndex, grpIdList);
    }

    public boolean moveContactFromGroupsToGroups(int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.moveContactFromGroupsToGroups(adnIndex, fromGrpIdList, toGrpIdList);
    }

    public int hasExistGroup(String grpName) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.hasExistGroup(grpName);
    }

    public int getUsimGrpMaxNameLen() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.getUsimGrpMaxNameLen();
    }

    public int getUsimGrpMaxCount() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.getUsimGrpMaxCount();
    }

    public List<AlphaTag> getUsimAasList() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return null;
        }
        return phbMgr.getUsimAasList();
    }

    public String getUsimAasById(int index) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return null;
        }
        return phbMgr.getUsimAasById(index, 0);
    }

    public boolean removeUsimAasById(int index, int pbrIndex) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.removeUsimAasById(index, pbrIndex);
    }

    public int insertUsimAas(String aasName) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.insertUsimAas(aasName);
    }

    public boolean updateUsimAas(int index, int pbrIndex, String aasName) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.updateUsimAas(index, pbrIndex, aasName);
    }

    public boolean updateAdnAas(int adnIndex, int aasIndex) {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.updateAdnAas(adnIndex, aasIndex);
    }

    public int getAnrCount() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return 0;
        }
        return phbMgr.getAnrCount();
    }

    public int getEmailCount() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return 0;
        }
        return phbMgr.getEmailCount();
    }

    public int getUsimAasMaxCount() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.getUsimAasMaxCount();
    }

    public int getUsimAasMaxNameLen() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.getUsimAasMaxNameLen();
    }

    public boolean hasSne() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.hasSne();
    }

    public int getSneRecordLen() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.getSneRecordLen();
    }

    public boolean isAdnAccessible() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return false;
        }
        return phbMgr.isAdnAccessible();
    }

    public synchronized UsimPBMemInfo[] getPhonebookMemStorageExt() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        UsimPBMemInfo[] phonebookMemStorageExt = null;
        if (this.mMtkAdnCache == null) {
            return null;
        }
        MtkUsimPhoneBookManager phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager();
        if (phbMgr != null) {
            phonebookMemStorageExt = phbMgr.getPhonebookMemStorageExt();
        }
        return phonebookMemStorageExt;
    }

    public int getUpbDone() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return -1;
        }
        return phbMgr.getUpbDone();
    }

    public int[] getAdnRecordsCapacityExt() {
        MtkUsimPhoneBookManager phbMgr;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null || (phbMgr = this.mMtkAdnCache.getUsimPhoneBookManager()) == null) {
            return null;
        }
        return phbMgr.getAdnRecordsCapacityExt();
    }
}
