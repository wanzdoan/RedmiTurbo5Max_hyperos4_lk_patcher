package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.telephony.Rlog;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class AdnRecordLoader extends Handler {
    protected static final int EVENT_ADN_LOAD_ALL_DONE = 3;
    protected static final int EVENT_ADN_LOAD_DONE = 1;
    protected static final int EVENT_EF_LINEAR_RECORD_SIZE_DONE = 4;
    protected static final int EVENT_EXT_RECORD_LOAD_DONE = 2;
    protected static final int EVENT_UPDATE_RECORD_DONE = 5;
    protected static final String LOG_TAG = "AdnRecordLoader";
    protected static final boolean VDBG = false;
    protected ArrayList<AdnRecord> mAdns;
    protected int mEf;
    protected int mExtensionEF;
    protected IccFileHandler mFh;
    protected int mPendingExtLoads;
    protected String mPin2;
    protected int mRecordNumber;
    protected Object mResult;
    protected Message mUserResponse;

    public AdnRecordLoader(IccFileHandler iccFileHandler) {
        super(Looper.getMainLooper());
        this.mFh = iccFileHandler;
    }

    protected String getEFPath(int i) {
        if (i == 28474) {
            return "3F007F10";
        }
        return null;
    }

    public void loadFromEF(int i, int i2, int i3, Message message) {
        this.mEf = i;
        this.mExtensionEF = i2;
        this.mRecordNumber = i3;
        this.mUserResponse = message;
        this.mFh.loadEFLinearFixed(i, getEFPath(i), i3, obtainMessage(1));
    }

    public void loadAllFromEF(int i, int i2, Message message) {
        this.mEf = i;
        this.mExtensionEF = i2;
        this.mUserResponse = message;
        this.mFh.loadEFLinearFixedAll(i, getEFPath(i), obtainMessage(3));
    }

    public void updateEF(AdnRecord adnRecord, int i, int i2, int i3, String str, Message message) {
        this.mEf = i;
        this.mExtensionEF = i2;
        this.mRecordNumber = i3;
        this.mUserResponse = message;
        this.mPin2 = str;
        this.mFh.getEFLinearRecordSize(i, getEFPath(i), obtainMessage(4, adnRecord));
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        try {
            int i = message.what;
            if (i == 1) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                byte[] bArr = (byte[]) asyncResult.result;
                Throwable th = asyncResult.exception;
                if (th != null) {
                    throw new RuntimeException("load failed", th);
                }
                AdnRecord adnRecord = new AdnRecord(this.mEf, this.mRecordNumber, bArr);
                this.mResult = adnRecord;
                if (adnRecord.hasExtendedRecord()) {
                    this.mPendingExtLoads = 1;
                    this.mFh.loadEFLinearFixed(this.mExtensionEF, adnRecord.mExtRecord, obtainMessage(2, adnRecord));
                }
            } else if (i != 2) {
                int i2 = 0;
                if (i == 3) {
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    ArrayList arrayList = (ArrayList) asyncResult2.result;
                    Throwable th2 = asyncResult2.exception;
                    if (th2 != null) {
                        throw new RuntimeException("load failed", th2);
                    }
                    ArrayList<AdnRecord> arrayList2 = new ArrayList<>(arrayList.size());
                    this.mAdns = arrayList2;
                    this.mResult = arrayList2;
                    this.mPendingExtLoads = 0;
                    int size = arrayList.size();
                    while (i2 < size) {
                        int i3 = i2 + 1;
                        AdnRecord adnRecord2 = new AdnRecord(this.mEf, i3, (byte[]) arrayList.get(i2));
                        this.mAdns.add(adnRecord2);
                        if (adnRecord2.hasExtendedRecord()) {
                            this.mPendingExtLoads++;
                            this.mFh.loadEFLinearFixed(this.mExtensionEF, adnRecord2.mExtRecord, obtainMessage(2, adnRecord2));
                        }
                        i2 = i3;
                    }
                } else if (i == 4) {
                    AsyncResult asyncResult3 = (AsyncResult) message.obj;
                    AdnRecord adnRecord3 = (AdnRecord) asyncResult3.userObj;
                    Throwable th3 = asyncResult3.exception;
                    if (th3 != null) {
                        throw new RuntimeException("get EF record size failed", th3);
                    }
                    int[] iArr = (int[]) asyncResult3.result;
                    if (iArr.length != 3 || this.mRecordNumber > iArr[2]) {
                        throw new RuntimeException("get wrong EF record size format", th3);
                    }
                    byte[] bArrBuildAdnString = adnRecord3.buildAdnString(iArr[0]);
                    if (bArrBuildAdnString == null) {
                        if (this.mUserResponse.arg1 == 1) {
                            adnRecord3.mAlphaTag = null;
                            bArrBuildAdnString = adnRecord3.buildAdnString(iArr[0]);
                        }
                        if (bArrBuildAdnString == null) {
                            throw new RuntimeException("wrong ADN format", asyncResult3.exception);
                        }
                    }
                    byte[] bArr2 = bArrBuildAdnString;
                    int i4 = this.mUserResponse.arg1;
                    IccFileHandler iccFileHandler = this.mFh;
                    if (i4 == 1) {
                        int i5 = this.mEf;
                        iccFileHandler.updateEFLinearFixed(i5, getEFPath(i5), this.mRecordNumber, bArr2, this.mPin2, obtainMessage(5, adnRecord3));
                    } else {
                        int i6 = this.mEf;
                        iccFileHandler.updateEFLinearFixed(i6, getEFPath(i6), this.mRecordNumber, bArr2, this.mPin2, obtainMessage(5));
                    }
                    this.mPendingExtLoads = 1;
                } else if (i == 5) {
                    AsyncResult asyncResult4 = (AsyncResult) message.obj;
                    Throwable th4 = asyncResult4.exception;
                    if (th4 != null) {
                        throw new RuntimeException("update EF adn record failed", th4);
                    }
                    this.mPendingExtLoads = 0;
                    if (this.mUserResponse.arg1 == 1) {
                        this.mResult = asyncResult4.userObj;
                    } else {
                        this.mResult = null;
                    }
                }
            } else {
                AsyncResult asyncResult5 = (AsyncResult) message.obj;
                byte[] bArr3 = (byte[]) asyncResult5.result;
                AdnRecord adnRecord4 = (AdnRecord) asyncResult5.userObj;
                if (asyncResult5.exception == null) {
                    Rlog.d(LOG_TAG, "ADN extension EF: 0x" + Integer.toHexString(this.mExtensionEF) + ":" + adnRecord4.mExtRecord + "\n" + IccUtils.bytesToHexString(bArr3));
                    adnRecord4.appendExtRecord(bArr3);
                } else {
                    Rlog.e(LOG_TAG, "Failed to read ext record. Clear the number now.");
                    adnRecord4.setNumber("");
                }
                this.mPendingExtLoads--;
            }
            Message message2 = this.mUserResponse;
            if (message2 == null || this.mPendingExtLoads != 0) {
                return;
            }
            AsyncResult.forMessage(message2).result = this.mResult;
            this.mUserResponse.sendToTarget();
            this.mUserResponse = null;
        } catch (RuntimeException e) {
            Message message3 = this.mUserResponse;
            if (message3 != null) {
                AsyncResult.forMessage(message3).exception = e;
                this.mUserResponse.sendToTarget();
                this.mUserResponse = null;
            }
        }
    }
}
