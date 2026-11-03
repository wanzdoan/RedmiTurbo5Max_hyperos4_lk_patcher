package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.gsm.SimTlv;
import com.android.internal.telephony.uicc.IccConstants;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.MiuiAdnRecord;
import com.android.internal.telephony.uicc.MiuiAdnRecordCache;
import com.android.internal.telephony.uicc.MiuiAdnRecordLoader;
import com.android.internal.telephony.uicc.MiuiIccFileHandler;
import com.xiaomi.modem.ModemUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MiuiUsimPhoneBookManager extends Handler implements IccConstants {
    static final boolean DBG = true;
    static final int EVENT_ANR_RECORD_LOAD_DONE = 4;
    static final int EVENT_EMAIL_RECORD_LOAD_DONE = 5;
    static final int EVENT_IAP_RECORD_LOAD_DONE = 6;
    static final int EVENT_PBR_LOAD_DONE = 1;
    static final int EVENT_RECORD_SIZE_READ_DONE = 2;
    static final int EVENT_REQUEST_PBR_LOAD = 8;
    static final int EVENT_UPDATE_DONE = 7;
    static final int EVENT_USIM_ADN_LOAD_DONE = 3;
    static final String LOG_TAG = "UsimPhoneBookManager";
    static final int MAX_ANR_DATA_LENGTH = 11;
    static final int MAX_ANR_LENGTH = 20;
    static final int USIM_EFAAS_TAG = 199;
    static final int USIM_EFADN_TAG = 192;
    static final int USIM_EFANR_TAG = 196;
    static final int USIM_EFCCP1_TAG = 203;
    static final int USIM_EFEMAIL_TAG = 202;
    static final int USIM_EFEXT1_TAG = 194;
    static final int USIM_EFGRP_TAG = 198;
    static final int USIM_EFGSD_TAG = 200;
    static final int USIM_EFIAP_TAG = 193;
    static final int USIM_EFPBC_TAG = 197;
    static final int USIM_EFSNE_TAG = 195;
    static final int USIM_EFUID_TAG = 201;
    static final int USIM_TYPE1_TAG = 168;
    static final int USIM_TYPE2_CONDITIONAL_LENGTH = 2;
    static final int USIM_TYPE2_TAG = 169;
    static final int USIM_TYPE3_TAG = 170;
    MiuiAdnRecordCache mAdnCache;
    MiuiIccFileHandler mFh;
    private Handler mHandler;
    byte[][][] mIapFiles;
    boolean[] mSupportAnr;
    boolean[] mSupportEmail;
    AsyncResult mUpdateResult;
    Object mLock = new Object();
    Object mReadLock = new Object();
    AtomicInteger mReadingAnrNum = new AtomicInteger(0);
    AtomicInteger mReadingEmailNum = new AtomicInteger(0);
    AtomicInteger mReadingIapNum = new AtomicInteger(0);
    ArrayList<MiuiAdnRecord> mPhoneBookRecords = new ArrayList<>();
    PbrFile mPbrFile = null;
    HashMap<Integer, RecordSize> mRecordSize = new HashMap<>();
    private HandlerThread mHandlerThread = new HandlerThread(LOG_TAG);

    public MiuiUsimPhoneBookManager(MiuiIccFileHandler fh, MiuiAdnRecordCache cache) {
        this.mFh = fh;
        this.mAdnCache = cache;
        this.mHandlerThread.start();
        this.mHandler = new MyHandler(this.mHandlerThread.getLooper());
    }

    public void reset() {
        synchronized (this.mLock) {
            this.mPhoneBookRecords.clear();
            this.mPbrFile = null;
            this.mIapFiles = null;
            this.mRecordSize.clear();
        }
    }

    public void dispose() {
        if (this.mHandler != null) {
            this.mHandler.getLooper().quitSafely();
            this.mHandler = null;
        }
        this.mHandlerThread = null;
    }

    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 8:
                    ArrayList<MiuiAdnRecord> result = null;
                    try {
                        result = MiuiUsimPhoneBookManager.this.loadEfFilesFromUsim();
                    } catch (Exception e) {
                        Log.w(MiuiUsimPhoneBookManager.LOG_TAG, "loadEfFilesFromUsim has error.");
                    }
                    if (msg.obj != null) {
                        Message response = (Message) msg.obj;
                        AsyncResult.forMessage(response, result, (Throwable) null);
                        response.sendToTarget();
                    }
                    break;
            }
        }
    }

    public ArrayList<MiuiAdnRecord> getAdnRecordsIfLoaded() {
        if (this.mPhoneBookRecords.isEmpty()) {
            return null;
        }
        return this.mPhoneBookRecords;
    }

    public void requestLoadEfFilesFromUsim(Message response) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(8, response));
    }

    public ArrayList<MiuiAdnRecord> loadEfFilesFromUsim() {
        synchronized (this.mLock) {
            if (!this.mPhoneBookRecords.isEmpty()) {
                return this.mPhoneBookRecords;
            }
            if (this.mPbrFile == null) {
                readPbrFileAndWait();
            }
            if (this.mPbrFile == null) {
                return null;
            }
            readRecordSizeAndWait(USIM_EFADN_TAG);
            readRecordSizeAndWait(USIM_EFEMAIL_TAG);
            readRecordSizeAndWait(USIM_EFIAP_TAG);
            readRecordSizeAndWait(USIM_EFANR_TAG);
            if (this.mPbrFile == null) {
                return null;
            }
            int numRecs = this.mPbrFile.mFileIds.size();
            initCapabilitySupported(numRecs);
            for (int i = 0; i < numRecs; i++) {
                if (this.mPbrFile == null) {
                    return null;
                }
                readAdnFileAndWait(i);
            }
            if (this.mPhoneBookRecords.isEmpty()) {
                return this.mPhoneBookRecords;
            }
            for (int i2 = 0; i2 < numRecs; i2++) {
                if (this.mPbrFile == null) {
                    return null;
                }
                readAnrFileAndWait(i2);
                if (this.mPbrFile == null) {
                    return null;
                }
                readEmailFileAndWait(i2);
            }
            return this.mPhoneBookRecords;
        }
    }

    public int update(int recordNumber, MiuiAdnRecord adn) {
        synchronized (this.mLock) {
            int pbrIndex = (recordNumber - 1) / this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
            if (MiuiAdnRecord.isAdnAlphaTagTooLong(adn.getAlphaTag(), this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordSize)) {
                return MiuiIccProviderException.ERROR_TEXT_TOO_LONG;
            }
            if (MiuiAdnRecord.isAdnNumberTooLong(adn.getNumber())) {
                return MiuiIccProviderException.ERROR_NUMBER_TOO_LONG;
            }
            if (this.mSupportEmail[pbrIndex]) {
                if (!checkEmailCapacityFree(recordNumber, adn.getEmails())) {
                    return MiuiIccProviderException.ERROR_EMAIL_FULL;
                }
                if (!checkEmailLength(adn.getEmails())) {
                    return MiuiIccProviderException.ERROR_EMAIL_TOO_LONG;
                }
            }
            if (this.mSupportAnr[pbrIndex]) {
                if (!checkAnrCapacityFree(recordNumber, adn.getAnr())) {
                    return MiuiIccProviderException.ERROR_ANR_FULL;
                }
                if (!checkAnrLength(adn.getAnr())) {
                    return MiuiIccProviderException.ERROR_ANR_TOO_LONG;
                }
            }
            int result = updateAdnAndWait(adn, recordNumber);
            if (result != 0) {
                return result;
            }
            if (!this.mSupportEmail[pbrIndex] || (result = updateEmailAndWait(adn, recordNumber)) == 0) {
                return (!this.mSupportAnr[pbrIndex] || (result = updateAnrAndWait(adn, recordNumber)) == 0) ? result : result;
            }
            return result;
        }
    }

    public int update(MiuiAdnRecord oldAdn, int oldRecordNumber, MiuiAdnRecord newAdn) {
        synchronized (this.mLock) {
            int adnCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
            int pbrIndex = (oldRecordNumber - 1) / adnCount;
            int[] validPbr = new int[this.mPbrFile.mFileIds.size()];
            Arrays.fill(validPbr, 0);
            int validPbrCount = validPbr.length;
            if (this.mSupportEmail[pbrIndex]) {
                EfRecord record = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFEMAIL_TAG)).get(pbrIndex);
                if (hasEmail(newAdn.getEmails()) && !hasEmail(oldAdn.getEmails()) && record.mType == USIM_TYPE2_TAG) {
                    int s = this.mPbrFile.mFileIds.size();
                    for (int i = 0; i < s; i++) {
                        if (!checkEmailCapacityFree((i * adnCount) + 1, newAdn.getEmails())) {
                            validPbr[i] = -1011;
                            validPbrCount--;
                        }
                    }
                }
            }
            if (validPbrCount == 0) {
                return validPbr[pbrIndex];
            }
            if (this.mSupportAnr[pbrIndex]) {
                EfRecord record2 = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFANR_TAG)).get(pbrIndex);
                if (!TextUtils.isEmpty(newAdn.getAnr()) && TextUtils.isEmpty(oldAdn.getAnr()) && record2.mType == USIM_TYPE2_TAG) {
                    int s2 = this.mPbrFile.mFileIds.size();
                    for (int i2 = 0; i2 < s2; i2++) {
                        if (!checkAnrCapacityFree((i2 * adnCount) + 1, newAdn.getAnr())) {
                            validPbr[i2] = -1008;
                            validPbrCount--;
                        }
                    }
                }
            }
            if (validPbrCount == 0) {
                return validPbr[pbrIndex];
            }
            if (validPbr[pbrIndex] == 0) {
                return update(oldRecordNumber, newAdn);
            }
            int recordNumber = -1;
            for (int i3 = 0; i3 < validPbr.length; i3++) {
                if (validPbr[i3] == 0) {
                    int j = adnCount * i3;
                    int s3 = j + adnCount;
                    while (j < s3) {
                        if (!this.mPhoneBookRecords.get(j).isEmpty()) {
                            j++;
                        } else {
                            recordNumber = j + 1;
                            break;
                        }
                    }
                }
            }
            if (recordNumber < 0) {
                return validPbr[pbrIndex];
            }
            if (!oldAdn.isEmpty()) {
                update(oldRecordNumber, new MiuiAdnRecord("", "", (String[]) null, ""));
            }
            return update(recordNumber, newAdn);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                readPbrFileDone((AsyncResult) msg.obj);
                return;
            case 2:
                readRecordSizeDone(msg.arg1, (AsyncResult) msg.obj);
                return;
            case 3:
                readAdnFileDone((AsyncResult) msg.obj);
                return;
            case 4:
                readAnrRecordDone(msg.arg1, msg.arg2, (AsyncResult) msg.obj);
                return;
            case 5:
                readEmailRecordDone(msg.arg1, msg.arg2, (AsyncResult) msg.obj);
                return;
            case 6:
                readIapRecordDone(msg.arg1, msg.arg2, (AsyncResult) msg.obj);
                return;
            case 7:
                synchronized (this.mLock) {
                    this.mUpdateResult = (AsyncResult) msg.obj;
                    notifyPending(this.mUpdateResult);
                    break;
                }
                return;
            default:
                return;
        }
    }

    void readPbrFileAndWait() {
        AtomicBoolean status = new AtomicBoolean(false);
        this.mFh.loadEFLinearFixedAll(20272, obtainMessage(1, status));
        waitForResult(status, "readPbrFileAndWait");
    }

    void readPbrFileDone(AsyncResult ar) {
        if (ar.exception == null) {
            ArrayList<byte[]> data = (ArrayList) ar.result;
            createPbrFile(data);
        } else {
            Log.e(LOG_TAG, "readPbrFile failed", ar.exception);
        }
        synchronized (this.mLock) {
            notifyPending(ar);
        }
    }

    void createPbrFile(ArrayList<byte[]> records) {
        if (records == null) {
            this.mPbrFile = null;
        } else {
            this.mPbrFile = new PbrFile(records);
        }
    }

    void readRecordSizeAndWait(int efTag) {
        if (this.mPbrFile != null && this.mPbrFile.mFileIds.get(0).get(Integer.valueOf(efTag)) != null) {
            readRecordSizeAndWait(efTag, this.mPbrFile.mFileIds.get(0).get(Integer.valueOf(efTag)).intValue());
        }
    }

    RecordSize readRecordSizeAndWait(int type, int fileId) {
        AtomicBoolean status = new AtomicBoolean(false);
        this.mFh.getEFLinearRecordSize(fileId, obtainMessage(2, type, 0, status));
        waitForResult(status, "readRecordSizeAndWait");
        return this.mRecordSize.get(Integer.valueOf(type));
    }

    void readRecordSizeDone(int fileId, AsyncResult ar) {
        if (ar.exception == null) {
            int[] recordSize = (int[]) ar.result;
            if (recordSize.length == 3) {
                this.mRecordSize.put(Integer.valueOf(fileId), new RecordSize(this, recordSize));
            }
        } else {
            Log.e(LOG_TAG, "readRecordSize failed", ar.exception);
        }
        synchronized (this.mLock) {
            notifyPending(ar);
        }
    }

    void readAdnFileAndWait(int pbrIndex) {
        Map<Integer, Integer> fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(pbrIndex));
        if (fileIds == null || !fileIds.containsKey(Integer.valueOf(USIM_EFADN_TAG))) {
            Log.d(LOG_TAG, "readAdnFileAndWait: no ADN tag in PBR " + pbrIndex);
            return;
        }
        int extEf = fileIds.containsKey(Integer.valueOf(USIM_EFEXT1_TAG)) ? fileIds.get(Integer.valueOf(USIM_EFEXT1_TAG)).intValue() : 0;
        this.mAdnCache.requestLoadAllAdnLike(fileIds.get(Integer.valueOf(USIM_EFADN_TAG)).intValue(), extEf, obtainMessage(3));
        try {
            this.mLock.wait();
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "Interrupted Exception in readAdnFileAndWait");
        }
    }

    void readAdnFileDone(AsyncResult ar) {
        if (ar.exception == null) {
            ArrayList<MiuiAdnRecord> records = (ArrayList) ar.result;
            this.mPhoneBookRecords.addAll(records);
        } else {
            Log.e(LOG_TAG, "readAdnFileDone failed", ar.exception);
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    void readAnrFileAndWait(int pbrIndex) {
        Map<Integer, Integer> fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(pbrIndex));
        if (fileIds == null || !fileIds.containsKey(Integer.valueOf(USIM_EFANR_TAG))) {
            Log.d(LOG_TAG, "readAnrFileAndWait: no ANR tag in PBR " + pbrIndex);
            this.mSupportAnr[pbrIndex] = false;
            return;
        }
        if (!this.mRecordSize.containsKey(Integer.valueOf(USIM_EFANR_TAG))) {
            Log.e(LOG_TAG, "readAnrFileAndWait: size of ANR tag in PBR is incorrect");
            this.mSupportAnr[pbrIndex] = false;
            return;
        }
        EfRecord record = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFANR_TAG)).get(pbrIndex);
        if (record.mType == USIM_TYPE2_TAG) {
            if (this.mRecordSize.get(Integer.valueOf(USIM_EFIAP_TAG)) == null) {
                Log.d(LOG_TAG, "readAnrFileAndWait: no AIP size in PBR and anr type is type2");
                this.mSupportAnr[pbrIndex] = false;
                return;
            } else {
                readType2Ef(record);
                return;
            }
        }
        if (record.mType == USIM_TYPE1_TAG) {
            readType1Ef(record);
        }
    }

    void readAnrRecordDone(int pbrIndex, int adnIndex, AsyncResult ar) {
        if (ar.exception == null) {
            setPhoneAdnRecordAnr(pbrIndex, adnIndex, (byte[]) ar.result);
        } else {
            Log.e(LOG_TAG, "readAnrRecord failed for pbrIndex=" + pbrIndex + ", adnIndex=" + adnIndex, ar.exception);
        }
        synchronized (this.mLock) {
            this.mReadingAnrNum.decrementAndGet();
            if (this.mReadingAnrNum.get() == 0) {
                this.mLock.notify();
            }
        }
    }

    void readEmailRecordDone(int pbrIndex, int adnIndex, AsyncResult ar) {
        if (ar.exception == null) {
            setPhoneAdnRecordEmail(pbrIndex, adnIndex, (byte[]) ar.result);
        } else {
            Log.e(LOG_TAG, "readAnrRecord failed for pbrIndex=" + pbrIndex + ", adnIndex=" + adnIndex, ar.exception);
        }
        synchronized (this.mLock) {
            this.mReadingEmailNum.decrementAndGet();
            if (this.mReadingEmailNum.get() == 0) {
                this.mLock.notify();
            }
        }
    }

    void setPhoneAdnRecordAnr(int pbrIndex, int adnIndex, byte[] anrData) {
        int length;
        if (anrData != null && anrData.length >= 2 && (length = anrData[1]) > 0 && length < 11) {
            String anr = PhoneNumberUtils.calledPartyBCDToString(anrData, 2, length);
            if (!TextUtils.isEmpty(anr) && adnIndex < this.mPhoneBookRecords.size()) {
                this.mPhoneBookRecords.get(adnIndex).setAnr(anr);
            }
        }
    }

    void setPhoneAdnRecordEmail(int pbrIndex, int adnIndex, byte[] emailData) {
        if (emailData == null) {
            return;
        }
        int length = emailData.length;
        if (length >= 2) {
            length -= 2;
        }
        String email = IccUtils.adnStringFieldToString(emailData, 0, length);
        if (!TextUtils.isEmpty(email) && adnIndex < this.mPhoneBookRecords.size()) {
            this.mPhoneBookRecords.get(adnIndex).setEmails(new String[]{email});
        }
    }

    void readType1Ef(EfRecord record) {
        int what;
        AtomicInteger readingNum;
        if (record.mType != USIM_TYPE1_TAG) {
            return;
        }
        int adnRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
        int offset = record.mPbrIndex * adnRecordCount;
        int max = offset + adnRecordCount;
        if (this.mPhoneBookRecords.size() < max) {
            max = this.mPhoneBookRecords.size();
        }
        switch (record.mTag) {
            case USIM_EFANR_TAG /* 196 */:
                what = 4;
                readingNum = this.mReadingAnrNum;
                break;
            case USIM_EFEMAIL_TAG /* 202 */:
                what = 5;
                readingNum = this.mReadingEmailNum;
                break;
            default:
                Log.e(LOG_TAG, "readType1Ef: unsupported tag " + record.mTag);
                return;
        }
        int recordSize = this.mRecordSize.get(Integer.valueOf(record.mTag)).mRecordSize;
        for (int adnIndex = offset; adnIndex < max; adnIndex++) {
            MiuiAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
            if (!rec.isEmpty()) {
                readingNum.incrementAndGet();
                this.mFh.loadEFLinearFixed(record.mEfTag, recordSize, (adnIndex + 1) - offset, obtainMessage(what, record.mPbrIndex, adnIndex));
            }
        }
        int adnIndex2 = readingNum.get();
        if (adnIndex2 != 0) {
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "Interrupted Exception in readType1Ef");
            }
        }
    }

    void readType2Ef(EfRecord record) {
        Map<Integer, Integer> fileIds;
        AtomicInteger readingNum;
        int what;
        int max;
        if (record.mType == USIM_TYPE2_TAG && (fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(record.mPbrIndex))) != null && fileIds.containsKey(Integer.valueOf(USIM_EFIAP_TAG))) {
            readIapFileAndWait(record.mPbrIndex);
            if (this.mIapFiles == null || this.mIapFiles.length <= record.mPbrIndex || this.mIapFiles[record.mPbrIndex] == null) {
                Log.e(LOG_TAG, "readType2Ef: IAP file is empty");
                return;
            }
            switch (record.mTag) {
                case USIM_EFANR_TAG /* 196 */:
                    AtomicInteger readingNum2 = this.mReadingAnrNum;
                    readingNum = readingNum2;
                    what = 4;
                    break;
                case USIM_EFEMAIL_TAG /* 202 */:
                    AtomicInteger readingNum3 = this.mReadingEmailNum;
                    readingNum = readingNum3;
                    what = 5;
                    break;
                default:
                    Log.e(LOG_TAG, "readType2EF: no implementation for " + record.mTag);
                    return;
            }
            byte[][] iaps = this.mIapFiles[record.mPbrIndex];
            int adnRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
            int offset = record.mPbrIndex * adnRecordCount;
            int max2 = offset + adnRecordCount;
            if (this.mPhoneBookRecords.size() >= max2) {
                max = max2;
            } else {
                max = this.mPhoneBookRecords.size();
            }
            int recordSize = this.mRecordSize.get(Integer.valueOf(record.mTag)).mRecordSize;
            int adnIndex = offset;
            while (adnIndex < max) {
                MiuiAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
                if (rec.isEmpty()) {
                    fileIds = fileIds;
                    readingNum = readingNum;
                } else {
                    byte[] iap = iaps[adnIndex - offset];
                    if (iap == null || iap.length <= record.mType2Record) {
                        Log.e(LOG_TAG, "readType2Ef: No IAP for ADN " + (adnIndex + 1));
                    } else {
                        int recordNumber = iap[record.mType2Record];
                        if (recordNumber == -1) {
                            fileIds = fileIds;
                            Log.e(LOG_TAG, "readType2Ef: NO IAP index for ADN " + (adnIndex + 1));
                            readingNum = readingNum;
                        } else {
                            fileIds = fileIds;
                            readingNum.incrementAndGet();
                            readingNum = readingNum;
                            this.mFh.loadEFLinearFixed(record.mEfTag, recordSize, recordNumber, obtainMessage(what, record.mPbrIndex, adnIndex));
                        }
                    }
                }
                adnIndex++;
                fileIds = fileIds;
                readingNum = readingNum;
            }
            int adnIndex2 = readingNum.get();
            if (adnIndex2 != 0) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Log.e(LOG_TAG, "Interrupted Exception in readType2Ef");
                }
            }
        }
    }

    void readIapFileAndWait(int pbrIndex) {
        if (this.mIapFiles == null) {
            this.mIapFiles = new byte[this.mPbrFile.mFileIds.size()][][];
        }
        if (this.mIapFiles[pbrIndex] != null) {
            return;
        }
        int adnRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
        byte[][] iapList = new byte[adnRecordCount][];
        int iapRecordSize = this.mRecordSize.get(Integer.valueOf(USIM_EFIAP_TAG)).mRecordSize;
        for (int i = 0; i < adnRecordCount; i++) {
            byte[] value = new byte[iapRecordSize];
            Arrays.fill(value, (byte) -1);
            iapList[i] = value;
        }
        this.mIapFiles[pbrIndex] = iapList;
        int offset = pbrIndex * adnRecordCount;
        int max = offset + adnRecordCount;
        if (this.mPhoneBookRecords.size() < max) {
            max = this.mPhoneBookRecords.size();
        }
        int efid = this.mPbrFile.mFileIds.get(Integer.valueOf(pbrIndex)).get(Integer.valueOf(USIM_EFIAP_TAG)).intValue();
        for (int adnIndex = offset; adnIndex < max; adnIndex++) {
            MiuiAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
            if (!rec.isEmpty()) {
                this.mReadingIapNum.incrementAndGet();
                this.mFh.loadEFLinearFixed(efid, iapRecordSize, (adnIndex + 1) - offset, obtainMessage(6, pbrIndex, adnIndex - offset));
            }
        }
        if (this.mReadingIapNum.get() != 0) {
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "Interrupted Exception in readIapFileAndWait");
            }
        }
    }

    void readIapRecordDone(int pbrIndex, int iapIndex, AsyncResult ar) {
        if (ar.exception == null && this.mIapFiles != null) {
            this.mIapFiles[pbrIndex][iapIndex] = (byte[]) ar.result;
        } else {
            Log.e(LOG_TAG, "readIapRecordDone failed for pbrIndex=" + pbrIndex + ", iapIndex=" + iapIndex, ar.exception);
        }
        synchronized (this.mLock) {
            this.mReadingIapNum.decrementAndGet();
            if (this.mReadingIapNum.get() == 0) {
                this.mLock.notify();
            }
        }
    }

    void readEmailFileAndWait(int pbrIndex) {
        Map<Integer, Integer> fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(pbrIndex));
        if (fileIds == null || !fileIds.containsKey(Integer.valueOf(USIM_EFEMAIL_TAG))) {
            Log.d(LOG_TAG, "readEmailFileAndWait: no EMAIL tag in PBR " + pbrIndex);
            this.mSupportEmail[pbrIndex] = false;
            return;
        }
        if (!this.mRecordSize.containsKey(Integer.valueOf(USIM_EFEMAIL_TAG))) {
            Log.e(LOG_TAG, "readEmailFileAndWait: size of EMAIL tag in PBR is incorrect");
            this.mSupportEmail[pbrIndex] = false;
            return;
        }
        EfRecord record = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFEMAIL_TAG)).get(pbrIndex);
        if (record.mType == USIM_TYPE2_TAG) {
            if (this.mRecordSize.get(Integer.valueOf(USIM_EFIAP_TAG)) == null) {
                Log.d(LOG_TAG, "readEmailFileAndWait: no AIP size in PBR and anr type is type2");
                this.mSupportEmail[pbrIndex] = false;
                return;
            } else {
                readType2Ef(record);
                return;
            }
        }
        if (record.mType == USIM_TYPE1_TAG) {
            readType1Ef(record);
        }
    }

    boolean hasEmail(String[] emails) {
        if (emails == null || TextUtils.isEmpty(emails[0])) {
            return false;
        }
        return DBG;
    }

    boolean checkEmailCapacityFree(int adnRecordNumber, String[] emails) {
        int pbrIndex = (adnRecordNumber - 1) / this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
        if (!this.mSupportEmail[pbrIndex]) {
            return false;
        }
        if (!hasEmail(emails)) {
            return DBG;
        }
        int emailRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFEMAIL_TAG)).mRecordCount;
        EfRecord record = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFEMAIL_TAG)).get(pbrIndex);
        if (record.mType != USIM_TYPE2_TAG || getRecordNumber(record, emailRecordCount) > 0) {
            return DBG;
        }
        return false;
    }

    boolean checkAnrCapacityFree(int adnRecordNumber, String anr) {
        int pbrIndex = (adnRecordNumber - 1) / this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
        if (!this.mSupportAnr[pbrIndex]) {
            return false;
        }
        if (TextUtils.isEmpty(anr)) {
            return DBG;
        }
        int anrRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFANR_TAG)).mRecordCount;
        EfRecord record = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFANR_TAG)).get(pbrIndex);
        if (record.mType != USIM_TYPE2_TAG || getRecordNumber(record, anrRecordCount) > 0) {
            return DBG;
        }
        return false;
    }

    boolean checkEmailLength(String[] emails) {
        HashMap<Integer, RecordSize> map = this.mRecordSize;
        Integer numValueOf = Integer.valueOf(USIM_EFEMAIL_TAG);
        int emailRecordSize = map.get(numValueOf).mRecordSize;
        int maxDataLength = (this.mRecordSize.get(numValueOf).mRecordCount == -1 || this.mPbrFile.mEfRecords.get(numValueOf).get(0).mType != USIM_TYPE2_TAG) ? emailRecordSize : emailRecordSize - 2;
        if (emails != null && emails[0] != null) {
            byte[] emailData = GsmAlphabet.stringToGsm8BitPacked(emails[0]);
            if (maxDataLength == -1 || emailData.length <= maxDataLength) {
                return DBG;
            }
            return false;
        }
        return DBG;
    }

    boolean checkAnrLength(String anr) {
        String updatedAnr = PhoneNumberUtils.convertPreDial(anr);
        if (TextUtils.isEmpty(updatedAnr) || updatedAnr.length() <= 20) {
            return DBG;
        }
        return false;
    }

    int updateEmailAndWait(MiuiAdnRecord adn, int adnRecordNumber) {
        int adnRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
        int pbrIndex = (adnRecordNumber - 1) / adnRecordCount;
        Map<Integer, Integer> fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(pbrIndex));
        if (fileIds == null || !fileIds.containsKey(Integer.valueOf(USIM_EFEMAIL_TAG))) {
            return MiuiIccProviderException.ERROR_UNKNOWN_EF;
        }
        EfRecord record = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFEMAIL_TAG)).get(pbrIndex);
        if (record.mType == USIM_TYPE2_TAG) {
            return updateType2Ef(adn.getEmails() != null ? adn.getEmails()[0] : null, adnRecordNumber, record);
        }
        if (record.mType == USIM_TYPE1_TAG) {
            return updateType1Ef(adn.getEmails() != null ? adn.getEmails()[0] : null, adnRecordNumber, record);
        }
        return MiuiIccProviderException.ERROR_UNKNOWN;
    }

    int updateAnrAndWait(MiuiAdnRecord adn, int adnRecordNumber) {
        int adnRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
        int pbrIndex = (adnRecordNumber - 1) / adnRecordCount;
        Map<Integer, Integer> fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(pbrIndex));
        if (fileIds == null || !fileIds.containsKey(Integer.valueOf(USIM_EFANR_TAG))) {
            return MiuiIccProviderException.ERROR_UNKNOWN_EF;
        }
        EfRecord record = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFANR_TAG)).get(pbrIndex);
        if (record.mType == USIM_TYPE2_TAG) {
            return updateType2Ef(adn.getAnr(), adnRecordNumber, record);
        }
        if (record.mType == USIM_TYPE1_TAG) {
            return updateType1Ef(adn.getAnr(), adnRecordNumber, record);
        }
        return MiuiIccProviderException.ERROR_UNKNOWN;
    }

    int updateType1Ef(String data, int adnRecordNumber, EfRecord record) {
        byte[] recordData;
        if (record.mType != USIM_TYPE1_TAG) {
            return MiuiIccProviderException.ERROR_UNKNOWN_EF;
        }
        switch (record.mTag) {
            case USIM_EFANR_TAG /* 196 */:
            case USIM_EFEMAIL_TAG /* 202 */:
                switch (record.mTag) {
                    case USIM_EFANR_TAG /* 196 */:
                        boolean zIsEmpty = TextUtils.isEmpty(data);
                        HashMap<Integer, RecordSize> map = this.mRecordSize;
                        if (!zIsEmpty) {
                            byte[] recordData2 = buildAnrRecord(data, map.get(Integer.valueOf(USIM_EFANR_TAG)).mRecordSize);
                            if (recordData2 != null) {
                                recordData = recordData2;
                            } else {
                                return MiuiIccProviderException.ERROR_ANR_TOO_LONG;
                            }
                        } else {
                            byte[] recordData3 = new byte[map.get(Integer.valueOf(USIM_EFANR_TAG)).mRecordSize];
                            Arrays.fill(recordData3, (byte) -1);
                            recordData = recordData3;
                        }
                        break;
                    case USIM_EFEMAIL_TAG /* 202 */:
                        boolean zIsEmpty2 = TextUtils.isEmpty(data);
                        HashMap<Integer, RecordSize> map2 = this.mRecordSize;
                        if (!zIsEmpty2) {
                            byte[] recordData4 = buildEmailRecord(data, adnRecordNumber, map2.get(Integer.valueOf(USIM_EFEMAIL_TAG)).mRecordSize);
                            if (recordData4 != null) {
                                recordData = recordData4;
                            } else {
                                return MiuiIccProviderException.ERROR_EMAIL_TOO_LONG;
                            }
                        } else {
                            byte[] recordData5 = new byte[map2.get(Integer.valueOf(USIM_EFEMAIL_TAG)).mRecordSize];
                            Arrays.fill(recordData5, (byte) -1);
                            recordData = recordData5;
                        }
                        break;
                    default:
                        recordData = null;
                        break;
                }
                int adnRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
                int adnIndex = (adnRecordNumber - 1) % adnRecordCount;
                AtomicBoolean status = new AtomicBoolean(false);
                this.mFh.updateEFLinearFixed(record.mEfTag, adnIndex + 1, recordData, null, obtainMessage(7, status));
                waitForResult(status, "updateType1Ef");
                if (this.mUpdateResult.exception == null) {
                    MiuiAdnRecord oldAdn = this.mPhoneBookRecords.get(adnRecordNumber - 1);
                    switch (record.mTag) {
                        case USIM_EFANR_TAG /* 196 */:
                            if (TextUtils.isEmpty(data)) {
                                oldAdn.setAnr("");
                            } else {
                                oldAdn.setAnr(data);
                            }
                            break;
                        case USIM_EFEMAIL_TAG /* 202 */:
                            if (TextUtils.isEmpty(data)) {
                                oldAdn.setEmails(null);
                            } else {
                                oldAdn.setEmails(new String[]{data});
                            }
                            break;
                    }
                }
                return MiuiIccProviderException.getErrorCauseFromException(this.mUpdateResult.exception);
            default:
                return MiuiIccProviderException.ERROR_UNKNOWN_EF;
        }
    }

    int updateType2Ef(String data, int adnRecordNumber, EfRecord record) {
        Map<Integer, Integer> fileIds;
        byte[] recordData;
        int recordNumber;
        int recordNumber2;
        byte[] recordData2;
        if (record.mType == USIM_TYPE2_TAG && (fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(record.mPbrIndex))) != null && fileIds.containsKey(Integer.valueOf(USIM_EFIAP_TAG))) {
            switch (record.mTag) {
                case USIM_EFANR_TAG /* 196 */:
                case USIM_EFEMAIL_TAG /* 202 */:
                    readIapFileAndWait(record.mPbrIndex);
                    int adnRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
                    int iapIndex = (adnRecordNumber - 1) % adnRecordCount;
                    byte[][] iaps = this.mIapFiles[record.mPbrIndex];
                    byte[] iap = iaps[iapIndex];
                    int recordCount = this.mRecordSize.get(Integer.valueOf(record.mTag)).mRecordCount;
                    int dataIndex = iap[record.mType2Record];
                    if (!TextUtils.isEmpty(data)) {
                        if (dataIndex > 0 && dataIndex <= recordCount) {
                            recordNumber2 = dataIndex;
                        } else {
                            int recordNumber3 = getRecordNumber(record, recordCount);
                            if (recordNumber3 >= 0) {
                                recordNumber2 = recordNumber3;
                            } else {
                                return MiuiIccProviderException.ERROR_STORAGE_FULL;
                            }
                        }
                        switch (record.mTag) {
                            case USIM_EFANR_TAG /* 196 */:
                                byte[] recordData3 = buildAnrRecord(data, this.mRecordSize.get(Integer.valueOf(USIM_EFANR_TAG)).mRecordSize);
                                if (recordData3 != null) {
                                    recordData2 = recordData3;
                                } else {
                                    return MiuiIccProviderException.ERROR_ANR_TOO_LONG;
                                }
                                break;
                            case USIM_EFEMAIL_TAG /* 202 */:
                                byte[] recordData4 = buildEmailRecord(data, adnRecordNumber, this.mRecordSize.get(Integer.valueOf(USIM_EFEMAIL_TAG)).mRecordSize);
                                if (recordData4 != null) {
                                    recordData2 = recordData4;
                                } else {
                                    return MiuiIccProviderException.ERROR_EMAIL_TOO_LONG;
                                }
                                break;
                            default:
                                recordData2 = null;
                                break;
                        }
                        AtomicBoolean status = new AtomicBoolean(false);
                        this.mFh.updateEFLinearFixed(fileIds.get(Integer.valueOf(record.mTag)).intValue(), recordNumber2, recordData2, null, obtainMessage(7, status));
                        waitForResult(status, "updateType2Ef");
                        recordNumber = recordNumber2;
                    } else if (dataIndex > 0 && dataIndex <= recordCount) {
                        switch (record.mTag) {
                            case USIM_EFANR_TAG /* 196 */:
                                byte[] recordData5 = buildAnrRecord(null, this.mRecordSize.get(Integer.valueOf(USIM_EFANR_TAG)).mRecordSize);
                                recordData = recordData5;
                                break;
                            case USIM_EFEMAIL_TAG /* 202 */:
                                byte[] recordData6 = buildEmailRecord(null, adnRecordNumber, this.mRecordSize.get(Integer.valueOf(USIM_EFEMAIL_TAG)).mRecordSize);
                                recordData = recordData6;
                                break;
                            default:
                                recordData = null;
                                break;
                        }
                        AtomicBoolean status2 = new AtomicBoolean(false);
                        this.mFh.updateEFLinearFixed(fileIds.get(Integer.valueOf(record.mTag)).intValue(), dataIndex, recordData, null, obtainMessage(7, status2));
                        waitForResult(status2, "updateType2Ef");
                        recordNumber = 255;
                    } else {
                        return 0;
                    }
                    if (this.mUpdateResult.exception == null) {
                        if (recordNumber != dataIndex) {
                            iap[record.mType2Record] = (byte) recordNumber;
                            AtomicBoolean status3 = new AtomicBoolean(false);
                            this.mFh.updateEFLinearFixed(fileIds.get(Integer.valueOf(USIM_EFIAP_TAG)).intValue(), iapIndex + 1, iap, null, obtainMessage(7, status3));
                            waitForResult(status3, "updateType2Ef");
                        }
                        if (this.mUpdateResult.exception == null) {
                            MiuiAdnRecord oldAdn = this.mPhoneBookRecords.get(adnRecordNumber - 1);
                            switch (record.mTag) {
                                case USIM_EFANR_TAG /* 196 */:
                                    if (TextUtils.isEmpty(data)) {
                                        oldAdn.setAnr("");
                                    } else {
                                        oldAdn.setAnr(data);
                                    }
                                    break;
                                case USIM_EFEMAIL_TAG /* 202 */:
                                    if (TextUtils.isEmpty(data)) {
                                        oldAdn.setEmails(null);
                                    } else {
                                        oldAdn.setEmails(new String[]{data});
                                    }
                                    break;
                            }
                        }
                    }
                    return MiuiIccProviderException.getErrorCauseFromException(this.mUpdateResult.exception);
                default:
                    return MiuiIccProviderException.ERROR_UNKNOWN_EF;
            }
        }
        return MiuiIccProviderException.ERROR_UNKNOWN_EF;
    }

    private int getRecordNumber(EfRecord record, int recordCount) {
        int tempIndex;
        BitSet validIndex = new BitSet(recordCount + 1);
        validIndex.set(1, recordCount + 1);
        for (EfRecord r : this.mPbrFile.mEfRecords.get(Integer.valueOf(record.mTag))) {
            if (r.mEfTag == record.mEfTag) {
                byte[][] iaps = this.mIapFiles[r.mPbrIndex];
                for (byte[] value : iaps) {
                    if (value != null && (tempIndex = value[r.mType2Record]) > 0 && tempIndex < 255 && tempIndex <= recordCount) {
                        validIndex.clear(tempIndex);
                    }
                }
            }
        }
        int recordNumber = validIndex.nextSetBit(1);
        return recordNumber;
    }

    byte[] buildEmailRecord(String str, int adnRecordNumber, int recordSize) {
        byte[] emailRecordData = new byte[recordSize];
        Arrays.fill(emailRecordData, (byte) -1);
        boolean emailInIap = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFEMAIL_TAG)).get(0).mType == USIM_TYPE2_TAG;
        if (!TextUtils.isEmpty(str)) {
            byte[] emailData = GsmAlphabet.stringToGsm8BitPacked(str);
            int maxDataLength = emailInIap ? emailRecordData.length - 2 : emailRecordData.length;
            if (emailData.length > maxDataLength) {
                return null;
            }
            System.arraycopy(emailData, 0, emailRecordData, 0, emailData.length);
            if (emailInIap) {
                int adnRecordCount = this.mRecordSize.get(Integer.valueOf(USIM_EFADN_TAG)).mRecordCount;
                int pbrIndex = (adnRecordNumber - 1) / adnRecordCount;
                EfRecord record = this.mPbrFile.mEfRecords.get(Integer.valueOf(USIM_EFADN_TAG)).get(pbrIndex);
                emailRecordData[recordSize - 2] = record.mSfi;
                emailRecordData[recordSize - 1] = (byte) (((adnRecordNumber - 1) % adnRecordCount) + 1);
            }
        }
        return emailRecordData;
    }

    byte[] buildAnrRecord(String anr, int recordSize) {
        byte[] data = new byte[recordSize];
        Arrays.fill(data, (byte) -1);
        String updatedAnr = PhoneNumberUtils.convertPreDial(anr);
        if (TextUtils.isEmpty(updatedAnr)) {
            return data;
        }
        if (updatedAnr.length() > 20) {
            return null;
        }
        byte[] bcdNumber = PhoneNumberUtils.numberToCalledPartyBCD(updatedAnr);
        if (bcdNumber != null) {
            System.arraycopy(bcdNumber, 0, data, 2, bcdNumber.length);
            data[1] = (byte) bcdNumber.length;
        }
        return data;
    }

    int updateAdnAndWait(MiuiAdnRecord adn, int adnRecordNumber) {
        HashMap<Integer, RecordSize> map = this.mRecordSize;
        Integer numValueOf = Integer.valueOf(USIM_EFADN_TAG);
        int pbrIndex = (adnRecordNumber - 1) / map.get(numValueOf).mRecordCount;
        int adnIndex = (adnRecordNumber - 1) % this.mRecordSize.get(numValueOf).mRecordCount;
        Map<Integer, Integer> fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(pbrIndex));
        if (fileIds == null || !fileIds.containsKey(numValueOf)) {
            return MiuiIccProviderException.ERROR_UNKNOWN_EF;
        }
        int extensionEF = fileIds.containsKey(Integer.valueOf(USIM_EFEXT1_TAG)) ? fileIds.get(Integer.valueOf(USIM_EFEXT1_TAG)).intValue() : 0;
        AtomicBoolean status = new AtomicBoolean(false);
        MiuiAdnRecordLoader.getLoader().updateEF(this.mFh, adn, fileIds.get(numValueOf).intValue(), extensionEF, adnIndex + 1, null, obtainMessage(7, status));
        waitForResult(status, "updateAdnAndWait");
        if (this.mUpdateResult.exception == null) {
            MiuiAdnRecord oldAdn = this.mPhoneBookRecords.get(adnRecordNumber - 1);
            oldAdn.setAlphaTag(adn.getAlphaTag());
            oldAdn.setNumber(adn.getNumber());
        }
        return MiuiIccProviderException.getErrorCauseFromException(this.mUpdateResult.exception);
    }

    protected void waitForResult(AtomicBoolean status, String functionName) {
        while (!status.get()) {
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "Interrupted Exception in " + functionName);
            }
        }
    }

    private void notifyPending(AsyncResult ar) {
        if (ar.userObj != null) {
            AtomicBoolean status = (AtomicBoolean) ar.userObj;
            status.set(DBG);
        }
        this.mLock.notify();
    }

    class RecordSize {
        int mRecordCount;
        int mRecordSize;
        int mTotalSize;

        public RecordSize(MiuiUsimPhoneBookManager this$0, int[] recordSize) {
            this.mRecordSize = recordSize[0];
            this.mTotalSize = recordSize[1];
            this.mRecordCount = recordSize[2];
        }

        public String toString() {
            return "{RecordSize=" + this.mRecordSize + ",RecordCount=" + this.mRecordCount + ",TotalSize=" + this.mTotalSize + "}";
        }
    }

    class EfRecord {
        public int mEfTag;
        public int mPbrIndex;
        public int mTag;
        public int mType;
        public byte mSfi = -1;
        public int mType2Record = -1;

        EfRecord(MiuiUsimPhoneBookManager this$0) {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{Efid=" + String.format("%x", Integer.valueOf(this.mEfTag)));
            sb.append(",Tag=" + String.format("%x", Integer.valueOf(this.mTag)));
            sb.append(",Type=" + String.format("%x", Integer.valueOf(this.mType)));
            sb.append(",Type2Record=" + this.mType2Record);
            sb.append(",PbrIndex=" + this.mPbrIndex + "}");
            return sb.toString();
        }
    }

    class PbrFile {
        HashMap<Integer, Map<Integer, Integer>> mFileIds = new HashMap<>();
        HashMap<Integer, ArrayList<EfRecord>> mEfRecords = new HashMap<>();

        PbrFile(ArrayList<byte[]> records) {
            int sliceCount = 0;
            for (byte[] record : records) {
                SimTlv recTlv = new SimTlv(record, 0, record.length);
                parseTag(recTlv, sliceCount);
                sliceCount++;
            }
        }

        void parseTag(SimTlv tlv, int recIndex) {
            Map<Integer, Integer> val = new HashMap<>();
            do {
                int tag = tlv.getTag();
                switch (tag) {
                    case MiuiUsimPhoneBookManager.USIM_TYPE1_TAG /* 168 */:
                    case MiuiUsimPhoneBookManager.USIM_TYPE2_TAG /* 169 */:
                    case MiuiUsimPhoneBookManager.USIM_TYPE3_TAG /* 170 */:
                        byte[] data = tlv.getData();
                        SimTlv tlvEf = new SimTlv(data, 0, data.length);
                        parseEf(tlvEf, val, tag, recIndex);
                        break;
                }
            } while (tlv.nextObject());
            this.mFileIds.put(Integer.valueOf(recIndex), val);
        }

        void parseEf(SimTlv tlvEf, Map<Integer, Integer> val, int parentTag, int recIndex) {
            int tagNumberWithinParentTag = 0;
            do {
                int tag = tlvEf.getTag();
                switch (tag) {
                    case MiuiUsimPhoneBookManager.USIM_EFADN_TAG /* 192 */:
                    case MiuiUsimPhoneBookManager.USIM_EFIAP_TAG /* 193 */:
                    case MiuiUsimPhoneBookManager.USIM_EFEXT1_TAG /* 194 */:
                    case MiuiUsimPhoneBookManager.USIM_EFANR_TAG /* 196 */:
                    case MiuiUsimPhoneBookManager.USIM_EFEMAIL_TAG /* 202 */:
                        byte[] data = tlvEf.getData();
                        if (data.length < 2 || data.length > 3) {
                            Log.d(MiuiUsimPhoneBookManager.LOG_TAG, "Invalid TLV length: " + data.length);
                        } else {
                            int efid = (data[0] << 8) | (data[1] & ModemUtils.EFS_VALUE_ASDIV_73841_DYNAMIC_SWITCH);
                            EfRecord object = new EfRecord(MiuiUsimPhoneBookManager.this);
                            object.mTag = tag;
                            object.mPbrIndex = recIndex;
                            object.mEfTag = efid;
                            object.mType = parentTag;
                            if (parentTag == MiuiUsimPhoneBookManager.USIM_TYPE2_TAG) {
                                object.mType2Record = tagNumberWithinParentTag;
                            }
                            if (data.length == 3) {
                                object.mSfi = data[2];
                            }
                            if (!this.mEfRecords.containsKey(Integer.valueOf(tag))) {
                                this.mEfRecords.put(Integer.valueOf(tag), new ArrayList<>());
                            }
                            if (!val.containsKey(Integer.valueOf(tag))) {
                                val.put(Integer.valueOf(tag), Integer.valueOf(efid));
                                this.mEfRecords.get(Integer.valueOf(tag)).add(object);
                            } else {
                                Log.d(MiuiUsimPhoneBookManager.LOG_TAG, "parseEf:  skip duplicate efId:" + efid + " with efTag:" + tag + ", pbrIndex=" + recIndex);
                            }
                        }
                        break;
                }
                tagNumberWithinParentTag++;
            } while (tlvEf.nextObject());
        }
    }

    private void initCapabilitySupported(int numRecs) {
        this.mSupportEmail = new boolean[numRecs];
        this.mSupportAnr = new boolean[numRecs];
        for (int i = 0; i < numRecs; i++) {
            this.mSupportEmail[i] = DBG;
            this.mSupportAnr[i] = DBG;
        }
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("MiuiUsimPhoneBookManager: ");
        if (this.mPbrFile != null) {
            pw.println(" mSupportEmail=" + Arrays.toString(this.mSupportEmail));
            pw.println(" mSupportAnr=" + Arrays.toString(this.mSupportAnr));
            pw.println(" mPhoneBookRecords.size=" + this.mPhoneBookRecords.size());
            pw.println(" mRecordSize=" + this.mRecordSize);
            pw.println(" mPbrFile.mFileIds=" + this.mPbrFile.mFileIds);
            pw.println(" mPbrFile.mEfRecords=" + this.mPbrFile.mEfRecords);
            return;
        }
        pw.println(" mPbrFile=null");
    }
}
