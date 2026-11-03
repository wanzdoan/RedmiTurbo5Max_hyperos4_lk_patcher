package com.mediatek.internal.telephony.phb;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.UsimPhoneBookManager;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccException;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.mediatek.internal.telephony.MtkPhoneNumberUtils;
import com.mediatek.internal.telephony.MtkRIL;
import com.mediatek.internal.telephony.uicc.EFResponseData;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimConstants;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MtkUsimPhoneBookManager extends UsimPhoneBookManager {
    private static final boolean DBG;
    private static final int EVENT_AAS_LOAD_DONE_OPTMZ = 28;
    private static final int EVENT_AAS_UPDATE_DONE = 10;
    private static final int EVENT_ANR_RECORD_LOAD_OPTMZ_DONE = 23;
    private static final int EVENT_ANR_UPDATE_DONE = 9;
    private static final int EVENT_EMAIL_RECORD_LOAD_OPTMZ_DONE = 22;
    private static final int EVENT_EMAIL_UPDATE_DONE = 8;
    private static final int EVENT_EXT1_LOAD_DONE = 1001;
    private static final int EVENT_GAS_LOAD_DONE = 6;
    private static final int EVENT_GAS_UPDATE_DONE = 13;
    private static final int EVENT_GET_RECORDS_SIZE_DONE = 1000;
    private static final int EVENT_GRP_RECORD_LOAD_DONE = 17;
    private static final int EVENT_GRP_UPDATE_DONE = 12;
    private static final int EVENT_QUERY_ANR_AVAILABLE_OPTMZ_DONE = 26;
    private static final int EVENT_QUERY_EMAIL_AVAILABLE_OPTMZ_DONE = 25;
    private static final int EVENT_QUERY_PHB_ADN_INFO = 21;
    private static final int EVENT_QUERY_SNE_AVAILABLE_OPTMZ_DONE = 27;
    private static final int EVENT_SELECT_EF_FILE_DONE = 20;
    private static final int EVENT_SNE_RECORD_LOAD_OPTMZ_DONE = 24;
    private static final int EVENT_SNE_UPDATE_DONE = 11;
    private static final int EVENT_UPB_CAPABILITY_QUERY_DONE = 19;
    private static final String LOG_TAG = "MtkUsimPhoneBookManager";
    private static final int PBR_NOT_NEED_NOTIFY = -1;
    private static final String PROP_FORCE_DEBUG_KEY = "persist.vendor.log.tel_dbg";
    private static final int UPB_EF_AAS = 3;
    private static final int UPB_EF_ANR = 0;
    private static final int UPB_EF_EMAIL = 1;
    private static final int UPB_EF_GAS = 4;
    private static final int UPB_EF_SNE = 2;
    private static final int USIM_DEFAULT_MAX_ADN_FILE_SIZE = 250;
    private static final int USIM_DEFAULT_MAX_EMAIL_FILE_SIZE = 100;
    public static final int USIM_ERROR_CAPACITY_FULL = -30;
    public static final int USIM_ERROR_GROUP_COUNT = -20;
    public static final int USIM_ERROR_NAME_LEN = -10;
    public static final int USIM_ERROR_OTHERS = -50;
    public static final int USIM_ERROR_STRING_TOOLONG = -40;
    private static final int USIM_MAX_AAS_ENTRIES_COUNT = 5;
    private static final int USIM_TYPE2_CONDITIONAL_LENGTH = 2;
    private ArrayList<String> mAasForAnr;
    private final Object mAasLock;
    private int mAdnFileSize;
    private int[] mAdnRecordSize;
    private ArrayList<int[]> mAnrInfo;
    private int mAnrRecordSize;
    protected Handler mBaseHandler;
    private MtkRIL mCi;
    private UiccCardApplication mCurrentApp;
    protected EFResponseData mEfData;
    private int mEmailFileSize;
    private int[] mEmailInfo;
    private int mEmailRecordSize;
    private ArrayList<ArrayList<byte[]>> mExt1FileList;
    private ArrayList<UsimGroup> mGasForGrp;
    private final Object mGasLock;
    private boolean mIsReset;
    private ArrayList<MtkAdnRecord> mMtkPhoneBookRecords;
    private AtomicBoolean mNeedNotify;
    private int mPbrNeedNotify;
    private int mReadEFLinerRecordSizeNum;
    private AtomicInteger mReadingAnrNum;
    private AtomicInteger mReadingEmailNum;
    private AtomicInteger mReadingGrpNum;
    private AtomicInteger mReadingSneNum;
    private SparseArray<int[]> mRecordSize;
    private boolean mRefreshAdnInfo;
    private boolean mRefreshAnrInfo;
    private boolean mRefreshEmailInfo;
    private int mResult;
    private int mSlotId;
    private int[] mSneInfo;
    private final Object mUPBCapabilityLock;
    private int[] mUpbCap;
    private int mUpbDone;

    public static final class Request {
        public AtomicBoolean mStatus = new AtomicBoolean(false);
        public Object mResult = null;
    }

    static {
        boolean z = false;
        if (SystemProperties.getInt(PROP_FORCE_DEBUG_KEY, 0) == 1 && !SystemProperties.get("ro.build.type").equals("user")) {
            z = true;
        }
        DBG = z;
    }

    public MtkUsimPhoneBookManager(IccFileHandler fh, AdnRecordCache cache) {
        super(fh, cache);
        this.mSlotId = -1;
        this.mGasLock = new Object();
        this.mUPBCapabilityLock = new Object();
        this.mAasLock = new Object();
        this.mEmailRecordSize = -1;
        this.mEmailFileSize = 100;
        this.mAdnFileSize = USIM_DEFAULT_MAX_ADN_FILE_SIZE;
        this.mAnrRecordSize = -1;
        this.mUpbDone = -1;
        this.mIsReset = false;
        this.mPbrNeedNotify = -1;
        this.mReadEFLinerRecordSizeNum = 0;
        this.mRefreshEmailInfo = false;
        this.mRefreshAnrInfo = false;
        this.mRefreshAdnInfo = false;
        this.mUpbCap = new int[8];
        this.mResult = -1;
        this.mReadingAnrNum = new AtomicInteger(0);
        this.mReadingEmailNum = new AtomicInteger(0);
        this.mReadingGrpNum = new AtomicInteger(0);
        this.mReadingSneNum = new AtomicInteger(0);
        this.mNeedNotify = new AtomicBoolean(false);
        this.mEfData = null;
        this.mBaseHandler = new Handler() { // from class: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                Request request = (Request) ar.userObj;
                switch (msg.what) {
                    case 1000:
                        int efid = msg.arg1;
                        MtkUsimPhoneBookManager.this.logi("EVENT_GET_RECORDS_SIZE_DONE done, recNum:" + MtkUsimPhoneBookManager.this.mReadEFLinerRecordSizeNum + ", ef_id:" + efid);
                        if (ar.exception == null) {
                            int[] recordSize = (int[]) ar.result;
                            if (recordSize.length == 3) {
                                if (MtkUsimPhoneBookManager.this.mRecordSize == null) {
                                    MtkUsimPhoneBookManager.this.mRecordSize = new SparseArray();
                                }
                                MtkUsimPhoneBookManager.this.mRecordSize.put(efid, recordSize);
                            } else {
                                Rlog.e(MtkUsimPhoneBookManager.LOG_TAG, "get wrong record size format" + ar.exception);
                            }
                        } else {
                            Rlog.e(MtkUsimPhoneBookManager.LOG_TAG, "get EF record size failed" + ar.exception);
                        }
                        if (MtkUsimPhoneBookManager.this.mReadEFLinerRecordSizeNum > 0) {
                            notifyPending(request, MtkUsimPhoneBookManager.this.mRecordSize);
                        }
                        break;
                }
            }

            private void notifyPending(Request request, Object result) {
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

    public MtkUsimPhoneBookManager(IccFileHandler fh, AdnRecordCache cache, CommandsInterface ci, UiccCardApplication app) {
        super(fh, cache);
        this.mSlotId = -1;
        this.mGasLock = new Object();
        this.mUPBCapabilityLock = new Object();
        this.mAasLock = new Object();
        this.mEmailRecordSize = -1;
        this.mEmailFileSize = 100;
        this.mAdnFileSize = USIM_DEFAULT_MAX_ADN_FILE_SIZE;
        this.mAnrRecordSize = -1;
        this.mUpbDone = -1;
        this.mIsReset = false;
        this.mPbrNeedNotify = -1;
        this.mReadEFLinerRecordSizeNum = 0;
        this.mRefreshEmailInfo = false;
        this.mRefreshAnrInfo = false;
        this.mRefreshAdnInfo = false;
        this.mUpbCap = new int[8];
        this.mResult = -1;
        this.mReadingAnrNum = new AtomicInteger(0);
        this.mReadingEmailNum = new AtomicInteger(0);
        this.mReadingGrpNum = new AtomicInteger(0);
        this.mReadingSneNum = new AtomicInteger(0);
        this.mNeedNotify = new AtomicBoolean(false);
        this.mEfData = null;
        this.mBaseHandler = new Handler() { // from class: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                Request request = (Request) ar.userObj;
                switch (msg.what) {
                    case 1000:
                        int efid = msg.arg1;
                        MtkUsimPhoneBookManager.this.logi("EVENT_GET_RECORDS_SIZE_DONE done, recNum:" + MtkUsimPhoneBookManager.this.mReadEFLinerRecordSizeNum + ", ef_id:" + efid);
                        if (ar.exception == null) {
                            int[] recordSize = (int[]) ar.result;
                            if (recordSize.length == 3) {
                                if (MtkUsimPhoneBookManager.this.mRecordSize == null) {
                                    MtkUsimPhoneBookManager.this.mRecordSize = new SparseArray();
                                }
                                MtkUsimPhoneBookManager.this.mRecordSize.put(efid, recordSize);
                            } else {
                                Rlog.e(MtkUsimPhoneBookManager.LOG_TAG, "get wrong record size format" + ar.exception);
                            }
                        } else {
                            Rlog.e(MtkUsimPhoneBookManager.LOG_TAG, "get EF record size failed" + ar.exception);
                        }
                        if (MtkUsimPhoneBookManager.this.mReadEFLinerRecordSizeNum > 0) {
                            notifyPending(request, MtkUsimPhoneBookManager.this.mRecordSize);
                        }
                        break;
                }
            }

            private void notifyPending(Request request, Object result) {
                if (request != null) {
                    synchronized (request) {
                        request.mResult = result;
                        request.mStatus.set(true);
                        request.notifyAll();
                    }
                }
            }
        };
        this.mMtkPhoneBookRecords = new ArrayList<>();
        this.mPhoneBookRecords = this.mMtkPhoneBookRecords;
        this.mGasForGrp = new ArrayList<>();
        this.mCi = (MtkRIL) ci;
        this.mCurrentApp = app;
        this.mSlotId = app == null ? -1 : app.getPhoneId();
        for (int i = 0; i < 8; i++) {
            this.mUpbCap[i] = -1;
        }
        logi("constructor finished. ");
    }

    public void reset() {
        super.reset();
        this.mPhoneBookRecords.clear();
        this.mPbrRecords = null;
        this.mIsReset = true;
        this.mGasForGrp.clear();
        this.mAasForAnr = null;
        this.mExt1FileList = null;
        this.mEmailInfo = null;
        this.mSneInfo = null;
        this.mAnrInfo = null;
        for (int i = 0; i < 8; i++) {
            this.mUpbCap[i] = -1;
        }
        this.mEmailRecordSize = -1;
        this.mAnrRecordSize = -1;
        this.mUpbDone = -1;
        this.mAdnRecordSize = null;
        this.mRefreshEmailInfo = false;
        this.mRefreshAnrInfo = false;
        this.mRefreshAdnInfo = false;
        synchronized (this.mLock) {
            this.mLock.notifyAll();
        }
        this.mPbrNeedNotify = -1;
        logi("reset finished, mPbrNeedNotify = " + this.mPbrNeedNotify);
    }

    public ArrayList<MtkAdnRecord> loadEfFilesFromUsim(Object object) {
        int[] size;
        long prevTime = System.currentTimeMillis();
        synchronized (this.mLock) {
            this.mIsReset = false;
            if (!this.mPhoneBookRecords.isEmpty()) {
                if (this.mRefreshCache) {
                    this.mRefreshCache = false;
                    refreshCache();
                }
                return this.mMtkPhoneBookRecords;
            }
            if (!this.mIsPbrPresent.booleanValue()) {
                return null;
            }
            logi("loadEfFilesFromUsim, mPbrNeedNotify =" + this.mPbrNeedNotify);
            if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
                this.mPbrNeedNotify++;
                readPbrFileAndWait();
            }
            if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
                if (checkIsPhbReady() && !this.mIsReset) {
                    if (true == readAdnFileAndWait(0)) {
                        this.mIsPbrPresent = false;
                        this.mEmailRecordSize = 0;
                        this.mAnrRecordSize = 0;
                        this.mUpbDone = 1;
                    }
                    logi("loadEfFilesFromUsim getRecordIfLoaded EF_ADN pbrP:" + this.mIsPbrPresent);
                    return ((MtkAdnRecordCache) this.mAdnCache).getRecordsIfLoaded(28474, null);
                }
                logi("loadEfFilesFromUsim phb not ready and Reset");
                return null;
            }
            logi("loadEfFilesFromUsim mPbrNeedNotify:" + this.mPbrNeedNotify);
            int adnEf = -1;
            int sneEf = -1;
            int numRecs = this.mPbrRecords.size();
            if (((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds.get(192) != null) {
                adnEf = ((UsimPhoneBookManager.File) ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds.get(192)).getEfid();
            }
            if (((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds.get(195) != null) {
                sneEf = ((UsimPhoneBookManager.File) ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds.get(195)).getEfid();
            }
            logi("loadEfFilesFromUsim adnEf:" + adnEf + ", sneEf:" + sneEf + ", numRecs:" + numRecs + ", mEmailRecordSize:" + this.mEmailRecordSize + ", mAnrRecordSize:" + this.mAnrRecordSize);
            if (this.mEmailRecordSize < 0) {
                readEmailRecordSize();
            }
            if (this.mAnrRecordSize < 0) {
                readAnrRecordSize();
            }
            if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
                logi("loadEfFilesFromUsim mPbrRecords is null");
                return null;
            }
            if (adnEf > 0 && (size = readEFLinearRecordSize(adnEf)) != null && size.length == 3) {
                this.mAdnFileSize = size[2];
            }
            if (sneEf > 0) {
                readEFLinearRecordSize(sneEf);
            }
            readAasFileAndWaitOptmz();
            if (isMtkPhb() == 1) {
                log("loadEfFilesFromUsim: Loading adn by mtk phb");
                readAdnFileAndWait(0);
            } else {
                log("loadEfFilesFromUsim: Loading adn");
                for (int i = 0; i < numRecs; i++) {
                    readAdnFileAndWait(i);
                }
            }
            if (this.mPhoneBookRecords.isEmpty()) {
                logi("loadEfFilesFromUsim mPhoneBookRecords Empty");
                return this.mMtkPhoneBookRecords;
            }
            logi("loadEfFilesFromUsim Speed up read begin");
            readSneFileAndWaitOptmz();
            readAnrFileAndWaitOptmz();
            readEmailFileAndWaitOptmz();
            logi("loadEfFilesFromUsim Speed up read end");
            readGrpIdsAndWait();
            if (this.mPbrRecords != null) {
                this.mUpbDone = 1;
            }
            if (this.mPbrRecords != null) {
                long endTime = System.currentTimeMillis();
                logi("loadEfFilesFromUsim Time: " + (endTime - prevTime) + " AppType: " + this.mCurrentApp.getType());
            } else {
                logi("loadEfFilesFromUsim end");
            }
            return this.mMtkPhoneBookRecords;
        }
    }

    private int isMtkPhb() {
        return SystemProperties.getInt("vendor.ril.mtkversion", 0) <= 15 ? 1 : 0;
    }

    /* JADX WARN: Code duplicated, block: B:32:0x00a5 A[Catch: all -> 0x010c, TRY_ENTER, TryCatch #1 {, blocks: (B:16:0x0030, B:20:0x003a, B:21:0x0043, B:23:0x004d, B:26:0x0074, B:25:0x0057, B:29:0x009b, B:32:0x00a5, B:33:0x00a8, B:35:0x00aa, B:36:0x00d3, B:40:0x00e1, B:39:0x00da, B:28:0x0078), top: B:51:0x0030, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:35:0x00aa A[Catch: all -> 0x010c, TRY_LEAVE, TryCatch #1 {, blocks: (B:16:0x0030, B:20:0x003a, B:21:0x0043, B:23:0x004d, B:26:0x0074, B:25:0x0057, B:29:0x009b, B:32:0x00a5, B:33:0x00a8, B:35:0x00aa, B:36:0x00d3, B:40:0x00e1, B:39:0x00da, B:28:0x0078), top: B:51:0x0030, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:43:0x0106  */
    /* JADX WARN: Code duplicated, block: B:57:? A[RETURN, SYNTHETIC] */
    private void readGrpIdsAndWait() {
        SparseArray<UsimPhoneBookManager.File> files;
        int i;
        AtomicBoolean atomicBoolean;
        logi("readGrpIdsAndWait begin");
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0 || (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds) == null) {
            return;
        }
        UsimPhoneBookManager.File grpFile = files.get(198);
        if (grpFile == null) {
            return;
        }
        synchronized (this.mLock) {
            int totalReadingNum = 0;
            int numAdnRecs = this.mPhoneBookRecords.size();
            for (int i2 = 0; i2 < numAdnRecs; i2++) {
                try {
                    MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(i2);
                    if (rec.getAlphaTag().length() > 0 || rec.getNumber().length() > 0) {
                        this.mReadingGrpNum.incrementAndGet();
                        int adnIndex = rec.getRecId();
                        int[] data = {i2, adnIndex};
                        this.mCi.readUPBGrpEntry(adnIndex, obtainMessage(17, data));
                        totalReadingNum++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Rlog.e(LOG_TAG, "readGrpIdsAndWait: mPhoneBookRecords IndexOutOfBoundsException numAdnRecs is " + numAdnRecs + "index is " + i2);
                    i = this.mReadingGrpNum.get();
                    atomicBoolean = this.mNeedNotify;
                    if (i == 0) {
                        atomicBoolean.set(false);
                        return;
                    }
                    atomicBoolean.set(true);
                    logi("readGrpIdsAndWait before mLock.wait " + this.mNeedNotify.get() + " total:" + totalReadingNum);
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e2) {
                        Rlog.e(LOG_TAG, "Interrupted Exception in readGrpIdsAndWait");
                    }
                    logi("readGrpIdsAndWait after mLock.wait after mLock.wait " + this.mNeedNotify.get());
                    if (true == this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                    }
                }
            }
            i = this.mReadingGrpNum.get();
            atomicBoolean = this.mNeedNotify;
            if (i == 0) {
                atomicBoolean.set(false);
                return;
            }
            atomicBoolean.set(true);
            logi("readGrpIdsAndWait before mLock.wait " + this.mNeedNotify.get() + " total:" + totalReadingNum);
            this.mLock.wait();
            logi("readGrpIdsAndWait after mLock.wait after mLock.wait " + this.mNeedNotify.get());
            if (true == this.mNeedNotify.get()) {
                this.mNeedNotify.set(false);
            }
        }
    }

    private boolean readAdnFileAndWait(int recId) {
        logi("readAdnFileAndWait begin: recId is " + recId + ",mIsReset:" + this.mIsReset);
        SparseArray<UsimPhoneBookManager.File> files = null;
        int extEf = 0;
        boolean isNormalPbr = true;
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
            isNormalPbr = false;
        } else {
            files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(recId)).mFileIds;
        }
        if (isMtkPhb() != 1) {
            if (files == null || files.size() == 0) {
                logi("readAdnFileAndWait end: file is null");
                isNormalPbr = false;
            } else {
                if (files.get(194) != null) {
                    extEf = files.get(194).getEfid();
                } else {
                    logi("readAdnFileAndWait: file USIM_EFEXT1_TAG is null");
                }
                if (files.get(192) == null) {
                    logi("readAdnFileAndWait end: file USIM_EFADN_TAG is null");
                    isNormalPbr = false;
                }
            }
        }
        int previousSize = this.mPhoneBookRecords.size();
        synchronized (this.mLock) {
            if (isMtkPhb() != 1 && isNormalPbr) {
                this.mAdnCache.requestLoadAllAdnLike(files.get(192).getEfid(), extEf, obtainMessage(2));
            } else {
                this.mAdnCache.requestLoadAllAdnLike(28474, this.mAdnCache.extensionEfForEf(28474), obtainMessage(2));
            }
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                Rlog.e(LOG_TAG, "Interrupted Exception in readAdnFileAndWait");
            }
        }
        if (this.mPbrRecords != null && this.mPbrRecords.size() > recId) {
            ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(recId)).mMainFileRecordNum = this.mPhoneBookRecords.size() - previousSize;
        }
        if (isMtkPhb() != 1 && previousSize > 0) {
            for (int i = previousSize; i < this.mPhoneBookRecords.size(); i++) {
                ((AdnRecord) this.mPhoneBookRecords.get(i)).setRecId(((AdnRecord) this.mPhoneBookRecords.get(i)).getRecId() + previousSize);
            }
        }
        logi("readAdnFileAndWait end: recId is " + recId + "extEf:" + extEf + ",mIsReset:" + this.mIsReset + ",previousSize:" + previousSize);
        return !this.mIsReset;
    }

    public int changeIndexByEfid(int adnIndex) {
        if (isMtkPhb() == 1) {
            return adnIndex;
        }
        return ((adnIndex - 1) % this.mAdnFileSize) + 1;
    }

    private void readAasFileAndWaitOptmz() {
        SparseArray<UsimPhoneBookManager.File> files;
        UsimPhoneBookManager.File aasFile;
        logi("readAasFileAndWaitOptmz begin");
        if (this.mAasForAnr == null || this.mAasForAnr.size() == 0) {
            int aasRecNum = 0;
            if (this.mUpbCap[3] < 0) {
                if (this.mPbrRecords == null || this.mPbrRecords.size() == 0 || (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds) == null || (aasFile = files.get(199)) == null) {
                    return;
                }
                int[] size = readEFLinearRecordSize(aasFile.getEfid());
                if (size != null && size.length == 3) {
                    aasRecNum = size[2];
                }
            } else {
                aasRecNum = this.mUpbCap[3];
            }
            if (aasRecNum > 5) {
                aasRecNum = 5;
            }
            synchronized (this.mLock) {
                this.mCi.readUPBAasList(1, aasRecNum, obtainMessage(EVENT_AAS_LOAD_DONE_OPTMZ));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in readAasFileAndWaitOptmz");
                }
            }
        }
        logi("readAasFileAndWaitOptmz end");
    }

    /* JADX WARN: Code duplicated, block: B:30:0x009f A[Catch: all -> 0x0106, TRY_ENTER, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x006e, B:23:0x0052, B:27:0x0095, B:30:0x009f, B:31:0x00a2, B:33:0x00a4, B:34:0x00cd, B:38:0x00db, B:37:0x00d4, B:26:0x0072), top: B:49:0x0035, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:33:0x00a4 A[Catch: all -> 0x0106, TRY_LEAVE, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x006e, B:23:0x0052, B:27:0x0095, B:30:0x009f, B:31:0x00a2, B:33:0x00a4, B:34:0x00cd, B:38:0x00db, B:37:0x00d4, B:26:0x0072), top: B:49:0x0035, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x0100  */
    /* JADX WARN: Code duplicated, block: B:54:? A[RETURN, SYNTHETIC] */
    private void readEmailFileAndWaitOptmz() {
        SparseArray<UsimPhoneBookManager.File> files;
        int i;
        AtomicBoolean atomicBoolean;
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0 || (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds) == null) {
            return;
        }
        UsimPhoneBookManager.File emailFile = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
        if (emailFile == null) {
            return;
        }
        int totalReadingNum = 0;
        int numAdnRecs = this.mPhoneBookRecords.size();
        synchronized (this.mLock) {
            for (int i2 = 0; i2 < numAdnRecs; i2++) {
                try {
                    MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(i2);
                    if (rec.getAlphaTag().length() > 0 || rec.getNumber().length() > 0) {
                        int[] data = {0, i2};
                        this.mReadingEmailNum.incrementAndGet();
                        this.mCi.readUPBEmailEntry(i2 + 1, 1, obtainMessage(EVENT_EMAIL_RECORD_LOAD_OPTMZ_DONE, data));
                        totalReadingNum++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Rlog.e(LOG_TAG, "readEmailFileAndWaitOptmz: mPhoneBookRecords IndexOutOfBoundsnumAdnRecs is " + numAdnRecs + "index is " + i2);
                    i = this.mReadingEmailNum.get();
                    atomicBoolean = this.mNeedNotify;
                    if (i == 0) {
                        atomicBoolean.set(false);
                        return;
                    }
                    atomicBoolean.set(true);
                    logi("readEmailFileAndWaitOptmz before mLock.wait " + this.mNeedNotify.get() + " total:" + totalReadingNum);
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e2) {
                        Rlog.e(LOG_TAG, "Interrupted Exception in readEmailFileAndWaitOptmz");
                    }
                    logi("readEmailFileAndWaitOptmz after mLock.wait: " + this.mNeedNotify.get());
                    if (true == this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                    }
                }
            }
            i = this.mReadingEmailNum.get();
            atomicBoolean = this.mNeedNotify;
            if (i == 0) {
                atomicBoolean.set(false);
                return;
            }
            atomicBoolean.set(true);
            logi("readEmailFileAndWaitOptmz before mLock.wait " + this.mNeedNotify.get() + " total:" + totalReadingNum);
            this.mLock.wait();
            logi("readEmailFileAndWaitOptmz after mLock.wait: " + this.mNeedNotify.get());
            if (true == this.mNeedNotify.get()) {
                this.mNeedNotify.set(false);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:30:0x00a2 A[Catch: all -> 0x0109, TRY_ENTER, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x0071, B:23:0x0052, B:27:0x0098, B:30:0x00a2, B:31:0x00a5, B:33:0x00a7, B:34:0x00d0, B:38:0x00de, B:37:0x00d7, B:26:0x0075), top: B:49:0x0035, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:33:0x00a7 A[Catch: all -> 0x0109, TRY_LEAVE, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x0071, B:23:0x0052, B:27:0x0098, B:30:0x00a2, B:31:0x00a5, B:33:0x00a7, B:34:0x00d0, B:38:0x00de, B:37:0x00d7, B:26:0x0075), top: B:49:0x0035, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x0103  */
    /* JADX WARN: Code duplicated, block: B:54:? A[RETURN, SYNTHETIC] */
    private void readAnrFileAndWaitOptmz() {
        SparseArray<UsimPhoneBookManager.File> files;
        int i;
        AtomicBoolean atomicBoolean;
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0 || (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds) == null) {
            return;
        }
        UsimPhoneBookManager.File anrFile = files.get(196);
        if (anrFile == null) {
            return;
        }
        int totalReadingNum = 0;
        int numAdnRecs = this.mPhoneBookRecords.size();
        synchronized (this.mLock) {
            for (int i2 = 0; i2 < numAdnRecs; i2++) {
                try {
                    MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(i2);
                    if (rec.getAlphaTag().length() > 0 || rec.getNumber().length() > 0) {
                        int[] data = {0, i2, 0};
                        this.mReadingAnrNum.addAndGet(1);
                        this.mCi.readUPBAnrEntry(i2 + 1, 1, obtainMessage(EVENT_ANR_RECORD_LOAD_OPTMZ_DONE, data));
                        totalReadingNum++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Rlog.e(LOG_TAG, "readAnrFileAndWaitOptmz: mPhoneBookRecords IndexOutOfBoundsnumAdnRecs is " + numAdnRecs + "index is " + i2);
                    i = this.mReadingAnrNum.get();
                    atomicBoolean = this.mNeedNotify;
                    if (i == 0) {
                        atomicBoolean.set(false);
                        return;
                    }
                    atomicBoolean.set(true);
                    logi("readAnrFileAndWaitOptmz before mLock.wait " + this.mNeedNotify.get() + " total:" + totalReadingNum);
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e2) {
                        Rlog.e(LOG_TAG, "Interrupted Exception in readAnrFileAndWaitOptmz");
                    }
                    logi("readAnrFileAndWaitOptmz after mLock.wait:" + this.mNeedNotify.get());
                    if (true == this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                    }
                }
            }
            i = this.mReadingAnrNum.get();
            atomicBoolean = this.mNeedNotify;
            if (i == 0) {
                atomicBoolean.set(false);
                return;
            }
            atomicBoolean.set(true);
            logi("readAnrFileAndWaitOptmz before mLock.wait " + this.mNeedNotify.get() + " total:" + totalReadingNum);
            this.mLock.wait();
            logi("readAnrFileAndWaitOptmz after mLock.wait:" + this.mNeedNotify.get());
            if (true == this.mNeedNotify.get()) {
                this.mNeedNotify.set(false);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:30:0x009f A[Catch: all -> 0x0106, TRY_ENTER, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x006e, B:23:0x0052, B:27:0x0095, B:30:0x009f, B:31:0x00a2, B:33:0x00a4, B:34:0x00cd, B:38:0x00db, B:37:0x00d4, B:26:0x0072), top: B:49:0x0035, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:33:0x00a4 A[Catch: all -> 0x0106, TRY_LEAVE, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x006e, B:23:0x0052, B:27:0x0095, B:30:0x009f, B:31:0x00a2, B:33:0x00a4, B:34:0x00cd, B:38:0x00db, B:37:0x00d4, B:26:0x0072), top: B:49:0x0035, inners: #0, #2 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x0100  */
    /* JADX WARN: Code duplicated, block: B:54:? A[RETURN, SYNTHETIC] */
    private void readSneFileAndWaitOptmz() {
        SparseArray<UsimPhoneBookManager.File> files;
        int i;
        AtomicBoolean atomicBoolean;
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0 || (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds) == null) {
            return;
        }
        UsimPhoneBookManager.File sneFile = files.get(195);
        if (sneFile == null) {
            return;
        }
        int totalReadingNum = 0;
        int numAdnRecs = this.mPhoneBookRecords.size();
        synchronized (this.mLock) {
            for (int i2 = 0; i2 < numAdnRecs; i2++) {
                try {
                    MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(i2);
                    if (rec.getAlphaTag().length() > 0 || rec.getNumber().length() > 0) {
                        int[] data = {0, i2};
                        this.mReadingSneNum.incrementAndGet();
                        this.mCi.readUPBSneEntry(i2 + 1, 1, obtainMessage(24, data));
                        totalReadingNum++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Rlog.e(LOG_TAG, "readSneFileAndWaitOptmz: mPhoneBookRecords IndexOutOfBoundsnumAdnRecs is " + numAdnRecs + "index is " + i2);
                    i = this.mReadingSneNum.get();
                    atomicBoolean = this.mNeedNotify;
                    if (i == 0) {
                        atomicBoolean.set(false);
                        return;
                    }
                    atomicBoolean.set(true);
                    logi("readSneFileAndWaitOptmz before mLock.wait " + this.mNeedNotify.get() + " total:" + totalReadingNum);
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e2) {
                        Rlog.e(LOG_TAG, "Interrupted Exception in readSneFileAndWaitOptmz");
                    }
                    logi("readSneFileAndWaitOptmz after mLock.wait: " + this.mNeedNotify.get());
                    if (true == this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                    }
                }
            }
            i = this.mReadingSneNum.get();
            atomicBoolean = this.mNeedNotify;
            if (i == 0) {
                atomicBoolean.set(false);
                return;
            }
            atomicBoolean.set(true);
            logi("readSneFileAndWaitOptmz before mLock.wait " + this.mNeedNotify.get() + " total:" + totalReadingNum);
            this.mLock.wait();
            logi("readSneFileAndWaitOptmz after mLock.wait: " + this.mNeedNotify.get());
            if (true == this.mNeedNotify.get()) {
                this.mNeedNotify.set(false);
            }
        }
    }

    private void updatePhoneAdnRecordWithEmailByIndexOptmz(int emailIndex, int adnIndex, String email) {
        log("updatePhoneAdnRecordWithEmailByIndex emailIndex = " + emailIndex + ",adnIndex = " + adnIndex);
        if (email != null && email != null) {
            try {
                if (!email.equals("")) {
                    MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex);
                    rec.setEmails(new String[]{email});
                }
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "[JE]updatePhoneAdnRecordWithEmailByIndex " + e.getMessage());
            }
        }
    }

    private void updatePhoneAdnRecordWithAnrByIndexOptmz(int recId, int adnIndex, int anrIndex, PhbEntry anrData) {
        String anr;
        log("updatePhoneAdnRecordWithAnrByIndexOptmz the " + adnIndex + " anr record:" + anrData);
        if (anrData != null && anrData.number != null && !anrData.number.equals("")) {
            if (anrData.ton == 145) {
                anr = MtkPhoneNumberUtils.prependPlusToNumber(anrData.number);
            } else {
                anr = anrData.number;
            }
            String anr2 = anr.replace('?', MtkPhoneNumberUtils.WILD).replace('p', MtkPhoneNumberUtils.PAUSE).replace('w', MtkPhoneNumberUtils.WAIT);
            int anrAas = anrData.index;
            if (anr2 != null && !anr2.equals("")) {
                String aas = null;
                if (anrAas > 0 && anrAas != 255 && this.mAasForAnr != null && anrAas <= this.mAasForAnr.size()) {
                    aas = this.mAasForAnr.get(anrAas - 1);
                }
                log(" updatePhoneAdnRecordWithAnrByIndex " + adnIndex + " th anr is " + anr2 + " the anrIndex is " + anrIndex);
                try {
                    MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex);
                    rec.setAnr(anr2, anrIndex);
                    if (aas != null && aas.length() > 0) {
                        rec.setAasIndex(anrAas);
                    }
                    this.mPhoneBookRecords.set(adnIndex, rec);
                } catch (IndexOutOfBoundsException e) {
                    Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithAnrByIndex: mPhoneBookRecords IndexOutOfBoundsException size: " + this.mPhoneBookRecords.size() + "index: " + adnIndex);
                }
            }
        }
    }

    private String[] buildAnrRecordOptmz(String number, int aas) {
        int ton = MtkPhoneNumberUtils.TOA_Unknown;
        if (number.indexOf(43) != -1) {
            if (number.indexOf(43) != number.lastIndexOf(43)) {
                Rlog.w(LOG_TAG, "There are multiple '+' in the number");
            }
            ton = MtkPhoneNumberUtils.TOA_International;
            number = number.replace("+", "");
        }
        String[] res = {number.replace(MtkPhoneNumberUtils.WILD, '?').replace(MtkPhoneNumberUtils.PAUSE, 'p').replace(MtkPhoneNumberUtils.WAIT, 'w'), Integer.toString(ton), Integer.toString(aas)};
        return res;
    }

    private void updatePhoneAdnRecordWithSneByIndexOptmz(int adnIndex, String sne) {
        if (sne == null) {
            return;
        }
        log("updatePhoneAdnRecordWithSneByIndex index " + adnIndex + " recData file is " + sne);
        if (sne != null && !sne.equals("")) {
            try {
                MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex);
                rec.setSne(sne);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithSneByIndex: mPhoneBookRecords IndexOutOfBoundsException size() is " + this.mPhoneBookRecords.size() + "index is " + adnIndex);
            }
        }
    }

    public void handleMessage(Message msg) {
        String[] gasList;
        String[] aasList;
        ArrayList<byte[]> record;
        switch (msg.what) {
            case 1:
                logi("handleMessage: EVENT_PBR_LOAD_DONE:" + this.mPbrNeedNotify);
                if (this.mPbrNeedNotify != -1) {
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar.exception == null) {
                        createPbrFile((ArrayList) ar.result);
                    }
                    synchronized (this.mLock) {
                        this.mLock.notify();
                        break;
                    }
                    this.mPbrNeedNotify--;
                    return;
                }
                return;
            case 2:
                logi("Loading USIM ADN records done");
                AsyncResult ar2 = (AsyncResult) msg.obj;
                if (ar2.exception == null && this.mPhoneBookRecords != null) {
                    if (ar2.result != null) {
                        this.mPhoneBookRecords.addAll((ArrayList) ar2.result);
                        log("Loading USIM ADN records " + this.mPhoneBookRecords.size());
                    } else {
                        log("Loading USIM ADN records ar.result:" + ar2.result);
                    }
                } else {
                    Rlog.w(LOG_TAG, "Loading USIM ADN records fail.");
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 3:
                logi("Loading USIM IAP records done");
                AsyncResult ar3 = (AsyncResult) msg.obj;
                if (ar3.exception == null) {
                    this.mIapFileRecord = (ArrayList) ar3.result;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 4:
                logi("Loading USIM Email records done");
                AsyncResult ar4 = (AsyncResult) msg.obj;
                if (ar4.exception == null) {
                    this.mEmailFileRecord = (ArrayList) ar4.result;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 6:
                logi("Load UPB GAS done");
                AsyncResult ar5 = (AsyncResult) msg.obj;
                if (ar5.exception == null && (gasList = (String[]) ar5.result) != null && gasList.length > 0) {
                    this.mGasForGrp = new ArrayList<>();
                    for (int i = 0; i < gasList.length; i++) {
                        String gas = decodeGas(gasList[i]);
                        UsimGroup uGasEntry = new UsimGroup(i + 1, gas);
                        this.mGasForGrp.add(uGasEntry);
                        log("Load UPB GAS done i is " + i + ", gas is " + gas);
                    }
                }
                synchronized (this.mGasLock) {
                    this.mGasLock.notify();
                    break;
                }
                return;
            case 8:
                logi("Updating USIM Email records done");
                AsyncResult ar6 = (AsyncResult) msg.obj;
                if (ar6.exception == null) {
                    log("Updating USIM Email records successfully!");
                    this.mRefreshEmailInfo = true;
                } else {
                    Rlog.e(LOG_TAG, "EVENT_EMAIL_UPDATE_DONE exception", ar6.exception);
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 9:
                logi("Updating USIM ANR records done");
                AsyncResult ar7 = (AsyncResult) msg.obj;
                IccIoResult res = (IccIoResult) ar7.result;
                if (ar7.exception != null) {
                    Rlog.e(LOG_TAG, "EVENT_ANR_UPDATE_DONE exception", ar7.exception);
                } else if (res != null) {
                    IccException exception = res.getException();
                    if (exception == null) {
                        log("Updating USIM ANR records successfully!");
                        this.mRefreshAnrInfo = true;
                    }
                } else {
                    this.mRefreshAnrInfo = true;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 10:
                logi("EVENT_AAS_UPDATE_DONE done.");
                synchronized (this.mAasLock) {
                    this.mAasLock.notify();
                    break;
                }
                return;
            case 11:
                logi("update UPB SNE done");
                AsyncResult ar8 = (AsyncResult) msg.obj;
                if (ar8.exception != null) {
                    Rlog.e(LOG_TAG, "EVENT_SNE_UPDATE_DONE exception", ar8.exception);
                    CommandException e = ar8.exception;
                    if (e.getCommandError() == CommandException.Error.OEM_ERROR_2) {
                        this.mResult = -40;
                    } else if (e.getCommandError() == CommandException.Error.OEM_ERROR_3) {
                        this.mResult = -30;
                    } else {
                        this.mResult = -50;
                    }
                } else {
                    this.mResult = 0;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 12:
                logi("update UPB GRP done");
                if (((AsyncResult) msg.obj).exception == null) {
                    this.mResult = 0;
                } else {
                    this.mResult = -1;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 13:
                logi("update UPB GAS done");
                AsyncResult ar9 = (AsyncResult) msg.obj;
                if (ar9.exception == null) {
                    this.mResult = 0;
                } else {
                    CommandException e2 = ar9.exception;
                    if (e2.getCommandError() == CommandException.Error.OEM_ERROR_2) {
                        this.mResult = -10;
                    } else if (e2.getCommandError() == CommandException.Error.OEM_ERROR_3) {
                        this.mResult = -20;
                    } else {
                        this.mResult = -1;
                    }
                }
                logi("update UPB GAS done mResult is " + this.mResult);
                synchronized (this.mGasLock) {
                    this.mGasLock.notify();
                    break;
                }
                return;
            case 17:
                AsyncResult ar10 = (AsyncResult) msg.obj;
                int[] userData = (int[]) ar10.userObj;
                boolean isNotify = this.mNeedNotify.get();
                if (ar10.result != null) {
                    int[] grpIds = (int[]) ar10.result;
                    if (grpIds.length > 0) {
                        updatePhoneAdnRecordWithGrpByIndex(userData[0], userData[1], grpIds);
                    }
                }
                this.mReadingGrpNum.decrementAndGet();
                log("haman, mReadingGrpNum when load done after minus: " + this.mReadingGrpNum.get() + ",mNeedNotify:" + isNotify);
                if (this.mReadingGrpNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_GRP_RECORD_LOAD_DONE end mLock.notify:" + isNotify);
                    return;
                }
                return;
            case EVENT_UPB_CAPABILITY_QUERY_DONE /* 19 */:
                logi("Query UPB capability done");
                AsyncResult ar11 = (AsyncResult) msg.obj;
                if (ar11.exception == null) {
                    this.mUpbCap = (int[]) ar11.result;
                }
                synchronized (this.mUPBCapabilityLock) {
                    this.mUPBCapabilityLock.notify();
                    break;
                }
                return;
            case 20:
                AsyncResult ar12 = (AsyncResult) msg.obj;
                if (ar12.exception == null) {
                    this.mEfData = (EFResponseData) ar12.result;
                } else {
                    Rlog.w(LOG_TAG, "Select EF file fail" + ar12.exception);
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_QUERY_PHB_ADN_INFO /* 21 */:
                logi("EVENT_QUERY_PHB_ADN_INFO");
                AsyncResult ar13 = (AsyncResult) msg.obj;
                if (ar13.exception == null) {
                    int[] info = (int[]) ar13.result;
                    if (info != null && info.length == 4) {
                        this.mAdnRecordSize = new int[4];
                        this.mAdnRecordSize[0] = info[0];
                        this.mAdnRecordSize[1] = info[1];
                        this.mAdnRecordSize[2] = info[2];
                        this.mAdnRecordSize[3] = info[3];
                        log("recordSize[0]=" + this.mAdnRecordSize[0] + ",recordSize[1]=" + this.mAdnRecordSize[1] + ",recordSize[2]=" + this.mAdnRecordSize[2] + ",recordSize[3]=" + this.mAdnRecordSize[3]);
                    } else {
                        this.mAdnRecordSize = new int[4];
                        this.mAdnRecordSize[0] = 0;
                        this.mAdnRecordSize[1] = 0;
                        this.mAdnRecordSize[2] = 0;
                        this.mAdnRecordSize[3] = 0;
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_EMAIL_RECORD_LOAD_OPTMZ_DONE /* 22 */:
                AsyncResult ar14 = (AsyncResult) msg.obj;
                int[] userData2 = (int[]) ar14.userObj;
                String emailResult = (String) ar14.result;
                boolean isNotify2 = this.mNeedNotify.get();
                if (emailResult != null && ar14.exception == null) {
                    log("Loading USIM Email record done result is " + emailResult);
                    updatePhoneAdnRecordWithEmailByIndexOptmz(userData2[0], userData2[1], emailResult);
                }
                this.mReadingEmailNum.decrementAndGet();
                log("haman, mReadingEmailNum when load done after minus: " + this.mReadingEmailNum.get() + ", mNeedNotify:" + this.mNeedNotify.get() + ", email index:" + userData2[0] + ", adn i:" + userData2[1]);
                if (this.mReadingEmailNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_EMAIL_RECORD_LOAD_OPTMZ_DONE end mLock.notify:" + isNotify2);
                    return;
                }
                return;
            case EVENT_ANR_RECORD_LOAD_OPTMZ_DONE /* 23 */:
                AsyncResult ar15 = (AsyncResult) msg.obj;
                int[] userData3 = (int[]) ar15.userObj;
                PhbEntry[] anrResult = (PhbEntry[]) ar15.result;
                boolean isNotify3 = this.mNeedNotify.get();
                if (anrResult != null && ar15.exception == null) {
                    log("Loading USIM Anr record done result is " + anrResult[0]);
                    updatePhoneAdnRecordWithAnrByIndexOptmz(userData3[0], userData3[1], userData3[2], anrResult[0]);
                }
                this.mReadingAnrNum.decrementAndGet();
                log("haman, mReadingAnrNum when load done after minus: " + this.mReadingAnrNum.get() + ", mNeedNotify:" + this.mNeedNotify.get() + ", anr index:" + userData3[2] + ", adn i:" + userData3[1]);
                if (this.mReadingAnrNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_ANR_RECORD_LOAD_OPTMZ_DONE end mLock.notify:" + isNotify3);
                    return;
                }
                return;
            case 24:
                AsyncResult ar16 = (AsyncResult) msg.obj;
                int[] userData4 = (int[]) ar16.userObj;
                String sneResult = (String) ar16.result;
                boolean isNotify4 = this.mNeedNotify.get();
                if (sneResult != null && ar16.exception == null) {
                    String sneResult2 = decodeGas(sneResult);
                    log("Loading USIM Sne record done result is " + sneResult2);
                    updatePhoneAdnRecordWithSneByIndexOptmz(userData4[1], sneResult2);
                }
                this.mReadingSneNum.decrementAndGet();
                log("haman, mReadingSneNum when load done after minus: " + this.mReadingSneNum.get() + ", mNeedNotify:" + this.mNeedNotify.get() + ", sne index:" + userData4[0] + ", adn i:" + userData4[1]);
                if (this.mReadingSneNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_SNE_RECORD_LOAD_OPTMZ_DONE end mLock.notify:" + isNotify4);
                    return;
                }
                return;
            case EVENT_QUERY_EMAIL_AVAILABLE_OPTMZ_DONE /* 25 */:
                AsyncResult ar17 = (AsyncResult) msg.obj;
                if (ar17.exception == null) {
                    this.mEmailInfo = (int[]) ar17.result;
                    if (this.mEmailInfo == null) {
                        log("mEmailInfo Null!");
                    } else {
                        logi("mEmailInfo = " + this.mEmailInfo[0] + " " + this.mEmailInfo[1] + " " + this.mEmailInfo[2]);
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_QUERY_ANR_AVAILABLE_OPTMZ_DONE /* 26 */:
                AsyncResult ar18 = (AsyncResult) msg.obj;
                int[] tmpAnrInfo = (int[]) ar18.result;
                if (ar18.exception == null) {
                    if (tmpAnrInfo == null) {
                        log("tmpAnrInfo Null!");
                    } else {
                        logi("tmpAnrInfo = " + tmpAnrInfo[0] + " " + tmpAnrInfo[1] + " " + tmpAnrInfo[2]);
                        if (this.mAnrInfo == null) {
                            this.mAnrInfo = new ArrayList<>();
                        } else if (this.mAnrInfo.size() > 0) {
                            this.mAnrInfo.clear();
                        }
                        this.mAnrInfo.add(tmpAnrInfo);
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_QUERY_SNE_AVAILABLE_OPTMZ_DONE /* 27 */:
                AsyncResult ar19 = (AsyncResult) msg.obj;
                if (ar19.exception == null) {
                    this.mSneInfo = (int[]) ar19.result;
                    if (this.mSneInfo == null) {
                        log("mSneInfo Null!");
                    } else {
                        logi("mSneInfo = " + this.mSneInfo[0] + " " + this.mSneInfo[1] + " " + this.mSneInfo[2]);
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_AAS_LOAD_DONE_OPTMZ /* 28 */:
                logi("Load UPB AAS done");
                AsyncResult ar20 = (AsyncResult) msg.obj;
                if (ar20.exception == null && (aasList = (String[]) ar20.result) != null && aasList.length > 0) {
                    this.mAasForAnr = new ArrayList<>();
                    for (int i2 = 0; i2 < aasList.length; i2++) {
                        String aas = decodeGas(aasList[i2]);
                        this.mAasForAnr.add(aas);
                        log("Load UPB AAS done i is " + i2 + ", aas is " + aas);
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 1001:
                AsyncResult ar21 = (AsyncResult) msg.obj;
                int pbrIndexExt1 = msg.arg1;
                logi("EVENT_EXT1_LOAD_DONE done pbr " + pbrIndexExt1);
                if (ar21.exception == null && (record = (ArrayList) ar21.result) != null) {
                    log("EVENT_EXT1_LOAD_DONE done size " + record.size());
                    if (this.mExt1FileList == null) {
                        this.mExt1FileList = new ArrayList<>();
                    }
                    this.mExt1FileList.add(record);
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            default:
                Rlog.e(LOG_TAG, "UnRecognized Message : " + msg.what);
                return;
        }
    }

    private void queryUpbCapablityAndWait() {
        logi("queryUpbCapablityAndWait begin");
        synchronized (this.mUPBCapabilityLock) {
            for (int i = 0; i < 8; i++) {
                this.mUpbCap[i] = -1;
            }
            if (checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(EVENT_UPB_CAPABILITY_QUERY_DONE));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in queryUpbCapablityAndWait");
                }
            }
        }
        logi("queryUpbCapablityAndWait done:N_Anr :" + this.mUpbCap[0] + ",N_Email:" + this.mUpbCap[1] + ",N_Sne:" + this.mUpbCap[2] + ",N_Aas:" + this.mUpbCap[3] + ",L_Aas:" + this.mUpbCap[4] + ",N_Gas:" + this.mUpbCap[5] + ",L_Gas:" + this.mUpbCap[6] + ",N_Grp:" + this.mUpbCap[7]);
    }

    private void readGasListAndWait() {
        logi("readGasListAndWait begin");
        synchronized (this.mGasLock) {
            if (this.mUpbCap[5] <= 0) {
                log("readGasListAndWait no need to read. return");
                return;
            }
            this.mCi.readUPBGasList(1, this.mUpbCap[5], obtainMessage(6));
            try {
                this.mGasLock.wait();
            } catch (InterruptedException e) {
                Rlog.e(LOG_TAG, "Interrupted Exception in readGasListAndWait");
            }
            logi("readGasListAndWait end");
        }
    }

    private void updatePhoneAdnRecordWithAnrByIndex(int recId, int adnIndex, int anrIndex, byte[] anrRecData) {
        String anr;
        ArrayList<String> aasList;
        log("updatePhoneAdnRecordWithAnrByIndex the " + adnIndex + "th anr record is " + IccUtils.bytesToHexString(anrRecData));
        int anrRecLength = anrRecData[1];
        int anrAas = anrRecData[0];
        if (anrRecLength > 0 && anrRecLength <= 11 && (anr = MtkPhoneNumberUtils.calledPartyBCDToString(anrRecData, 2, anrRecData[1])) != null && !anr.equals("")) {
            String aas = null;
            if (anrAas > 0 && anrAas != 255 && this.mAasForAnr != null && (aasList = this.mAasForAnr) != null && anrAas <= aasList.size()) {
                aas = aasList.get(anrAas - 1);
            }
            logi(" updatePhoneAdnRecordWithAnrByIndex " + adnIndex + " th anr is " + anr + " the anrIndex is " + anrIndex);
            try {
                MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex);
                rec.setAnr(anr, anrIndex);
                if (aas != null && aas.length() > 0) {
                    rec.setAasIndex(anrAas);
                }
                this.mPhoneBookRecords.set(adnIndex, rec);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithAnrByIndex: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + adnIndex);
            }
        }
    }

    public ArrayList<UsimGroup> getUsimGroups() {
        logi("getUsimGroups begin");
        synchronized (this.mGasLock) {
            if (!this.mGasForGrp.isEmpty()) {
                return this.mGasForGrp;
            }
            queryUpbCapablityAndWait();
            readGasListAndWait();
            logi("getUsimGroups end");
            return this.mGasForGrp;
        }
    }

    public String getUsimGroupById(int nGasId) {
        UsimGroup uGas;
        String grpName = null;
        logi("getUsimGroupById nGasId is " + nGasId);
        if (this.mGasForGrp != null && nGasId <= this.mGasForGrp.size() && (uGas = this.mGasForGrp.get(nGasId - 1)) != null) {
            grpName = uGas.getAlphaTag();
            log("getUsimGroupById index is " + uGas.getRecordIndex() + ", name is " + grpName);
        }
        logi("getUsimGroupById grpName is " + grpName);
        return grpName;
    }

    public synchronized boolean removeUsimGroupById(int nGasId) {
        boolean ret;
        ret = false;
        logi("removeUsimGroupById nGasId is " + nGasId);
        synchronized (this.mGasLock) {
            try {
                if (this.mGasForGrp == null) {
                    Rlog.e(LOG_TAG, "removeUsimGroupById fail ");
                } else {
                    try {
                        if (nGasId > this.mGasForGrp.size()) {
                            Rlog.e(LOG_TAG, "removeUsimGroupById fail ");
                        } else {
                            UsimGroup uGas = this.mGasForGrp.get(nGasId - 1);
                            if (uGas != null) {
                                log(" removeUsimGroupById index is " + uGas.getRecordIndex());
                            }
                            if (uGas != null && uGas.getAlphaTag() != null) {
                                this.mCi.deleteUPBEntry(4, 0, nGasId, obtainMessage(13));
                                try {
                                    this.mGasLock.wait();
                                } catch (InterruptedException e) {
                                    Rlog.e(LOG_TAG, "Interrupted Exception in removeUsimGroupById");
                                }
                                if (this.mResult == 0) {
                                    ret = true;
                                    uGas.setAlphaTag(null);
                                    this.mGasForGrp.set(nGasId - 1, uGas);
                                }
                            } else {
                                Rlog.w(LOG_TAG, "removeUsimGroupById fail: this gas doesn't exist ");
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                logi("removeUsimGroupById result is " + ret);
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return ret;
    }

    private String decodeGas(String srcGas) {
        log("[decodeGas] gas string is " + (srcGas == null ? "null" : srcGas));
        if (srcGas == null || TextUtils.isEmpty(srcGas) || srcGas.length() % 2 != 0) {
            return null;
        }
        try {
            byte[] ba = IccUtils.hexStringToBytes(srcGas);
            if (ba == null) {
                Rlog.w(LOG_TAG, "gas string is null");
                return null;
            }
            String retGas = new String(ba, 0, srcGas.length() / 2, "utf-16be");
            return retGas;
        } catch (UnsupportedEncodingException ex) {
            Rlog.e(LOG_TAG, "[decodeGas] implausible UnsupportedEncodingException", ex);
            return null;
        } catch (RuntimeException ex2) {
            Rlog.e(LOG_TAG, "[decodeGas] RuntimeException", ex2);
            return null;
        }
    }

    private String encodeToUcs2(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            String hexInt = Integer.toHexString(input.charAt(i));
            for (int j = 0; j < 4 - hexInt.length(); j++) {
                output.append("0");
            }
            output.append(hexInt);
        }
        return output.toString();
    }

    public synchronized int insertUsimGroup(String grpName) {
        int index;
        UsimGroup gasEntry;
        int index2 = -1;
        logi("insertUsimGroup grpName");
        synchronized (this.mGasLock) {
            if (this.mGasForGrp == null || this.mGasForGrp.size() == 0) {
                Rlog.w(LOG_TAG, "insertUsimGroup fail ");
            } else {
                UsimGroup gasEntry2 = null;
                int i = 0;
                while (true) {
                    if (i >= this.mGasForGrp.size()) {
                        index = -1;
                        gasEntry = gasEntry2;
                        break;
                    }
                    gasEntry2 = this.mGasForGrp.get(i);
                    if (gasEntry2 != null && gasEntry2.getAlphaTag() == null) {
                        int index3 = gasEntry2.getRecordIndex();
                        log("insertUsimGroup index is " + index3);
                        index = index3;
                        gasEntry = gasEntry2;
                        break;
                    }
                    i++;
                }
                try {
                    if (index < 0) {
                        Rlog.w(LOG_TAG, "insertUsimGroup fail: gas file is full.");
                        try {
                            return -20;
                        } catch (Throwable th) {
                            th = th;
                        }
                    } else {
                        String temp = encodeToUcs2(grpName);
                        this.mCi.editUPBEntry(4, 0, index, temp, null, obtainMessage(13));
                        try {
                            this.mGasLock.wait();
                        } catch (InterruptedException e) {
                            Rlog.e(LOG_TAG, "Interrupted Exception in insertUsimGroup");
                        }
                        if (this.mResult < 0) {
                            Rlog.e(LOG_TAG, "result is negative. insertUsimGroup");
                            return this.mResult;
                        }
                        gasEntry.setAlphaTag(grpName);
                        this.mGasForGrp.set(i, gasEntry);
                        index2 = index;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                while (true) {
                    try {
                        throw th;
                    } catch (Throwable th3) {
                        th = th3;
                    }
                }
            }
            return index2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:26:0x0060 A[Catch: all -> 0x0099, TryCatch #2 {all -> 0x0099, blocks: (B:23:0x0055, B:24:0x005c, B:26:0x0060, B:28:0x006d, B:32:0x0096, B:29:0x008b, B:31:0x0093, B:15:0x003f, B:16:0x0042, B:20:0x004a), top: B:51:0x0020, inners: #4 }] */
    /* JADX WARN: Code duplicated, block: B:28:0x006d A[Catch: all -> 0x0099, TryCatch #2 {all -> 0x0099, blocks: (B:23:0x0055, B:24:0x005c, B:26:0x0060, B:28:0x006d, B:32:0x0096, B:29:0x008b, B:31:0x0093, B:15:0x003f, B:16:0x0042, B:20:0x004a), top: B:51:0x0020, inners: #4 }] */
    /* JADX WARN: Code duplicated, block: B:29:0x008b A[Catch: all -> 0x0099, TryCatch #2 {all -> 0x0099, blocks: (B:23:0x0055, B:24:0x005c, B:26:0x0060, B:28:0x006d, B:32:0x0096, B:29:0x008b, B:31:0x0093, B:15:0x003f, B:16:0x0042, B:20:0x004a), top: B:51:0x0020, inners: #4 }] */
    /* JADX WARN: Code duplicated, block: B:31:0x0093 A[Catch: all -> 0x0099, TryCatch #2 {all -> 0x0099, blocks: (B:23:0x0055, B:24:0x005c, B:26:0x0060, B:28:0x006d, B:32:0x0096, B:29:0x008b, B:31:0x0093, B:15:0x003f, B:16:0x0042, B:20:0x004a), top: B:51:0x0020, inners: #4 }] */
    public synchronized int updateUsimGroup(int nGasId, String grpName) {
        Throwable th;
        int ret;
        UsimGroup uGasEntry;
        logi("updateUsimGroup nGasId is " + nGasId);
        synchronized (this.mGasLock) {
            try {
                this.mResult = -1;
                try {
                    if (this.mGasForGrp == null || nGasId > this.mGasForGrp.size()) {
                        Rlog.w(LOG_TAG, "updateUsimGroup fail ");
                        if (this.mResult == 0) {
                            ret = nGasId;
                            uGasEntry = this.mGasForGrp.get(nGasId - 1);
                            if (uGasEntry != null) {
                                log("updateUsimGroup index is " + uGasEntry.getRecordIndex());
                                uGasEntry.setAlphaTag(grpName);
                            } else {
                                Rlog.w(LOG_TAG, "updateUsimGroup the entry doesn't exist ");
                            }
                        } else {
                            int ret2 = this.mResult;
                            ret = ret2;
                        }
                    } else {
                        if (grpName == null) {
                            nGasId = nGasId;
                        } else {
                            String temp = encodeToUcs2(grpName);
                            nGasId = nGasId;
                            this.mCi.editUPBEntry(4, 0, nGasId, temp, null, obtainMessage(13));
                            try {
                                this.mGasLock.wait();
                            } catch (InterruptedException e) {
                                Rlog.e(LOG_TAG, "Interrupted Exception in updateUsimGroup");
                            }
                        }
                        if (this.mResult == 0) {
                            ret = nGasId;
                            uGasEntry = this.mGasForGrp.get(nGasId - 1);
                            if (uGasEntry != null) {
                                log("updateUsimGroup index is " + uGasEntry.getRecordIndex());
                                uGasEntry.setAlphaTag(grpName);
                            } else {
                                Rlog.w(LOG_TAG, "updateUsimGroup the entry doesn't exist ");
                            }
                        } else {
                            int ret3 = this.mResult;
                            ret = ret3;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    while (true) {
                        try {
                            throw th;
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    }
                }
            } catch (Throwable th4) {
                th = th4;
            }
        }
        return ret;
    }

    public boolean addContactToGroup(int adnIndex, int grpIndex) {
        boolean ret = false;
        logi("addContactToGroup begin adnIndex is " + adnIndex + " to grp " + grpIndex);
        if (this.mPhoneBookRecords == null || adnIndex <= 0 || adnIndex > this.mPhoneBookRecords.size()) {
            Rlog.e(LOG_TAG, "addContactToGroup no records or invalid index.");
            return false;
        }
        synchronized (this.mLock) {
            try {
                MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex - 1);
                if (rec != null) {
                    log(" addContactToGroup the adn index is " + rec.getRecId() + " old grpList is " + rec.getGrpIds());
                    String grpList = rec.getGrpIds();
                    boolean bExist = false;
                    int nOrder = -1;
                    int grpCount = this.mUpbCap[7];
                    int i = this.mUpbCap[7];
                    int i2 = this.mUpbCap[5];
                    int[] iArr = this.mUpbCap;
                    int grpMaxCount = i > i2 ? iArr[5] : iArr[7];
                    int[] grpIdArray = new int[grpCount];
                    for (int i3 = 0; i3 < grpCount; i3++) {
                        grpIdArray[i3] = 0;
                    }
                    if (grpList != null) {
                        String[] grpIds = rec.getGrpIds().split(",");
                        for (int i4 = 0; i4 < grpMaxCount; i4++) {
                            grpIdArray[i4] = Integer.parseInt(grpIds[i4]);
                            if (grpIndex == grpIdArray[i4]) {
                                bExist = true;
                                log(" addContactToGroup the adn is already in the group. i is " + i4);
                                break;
                            }
                            if (nOrder < 0 && (grpIdArray[i4] == 0 || grpIdArray[i4] == 255)) {
                                nOrder = i4;
                                log(" addContactToGroup found an unsed position in the group list. i is " + i4);
                            }
                        }
                    } else {
                        nOrder = 0;
                    }
                    if (!bExist && nOrder >= 0) {
                        grpIdArray[nOrder] = grpIndex;
                        this.mCi.writeUPBGrpEntry(adnIndex, grpIdArray, obtainMessage(12));
                        try {
                            this.mLock.wait();
                        } catch (InterruptedException e) {
                            Rlog.e(LOG_TAG, "Interrupted Exception in addContactToGroup");
                        }
                        if (this.mResult == 0) {
                            ret = true;
                            updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdArray);
                            logi(" addContactToGroup the adn index is " + rec.getRecId());
                            this.mResult = -1;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e2) {
                Rlog.e(LOG_TAG, "addContactToGroup: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
                return false;
            }
        }
        logi("addContactToGroup end adnIndex is " + adnIndex + " to grp " + grpIndex);
        return ret;
    }

    public synchronized boolean removeContactFromGroup(int adnIndex, int grpIndex) {
        boolean ret = false;
        logi("removeContactFromGroup begin adnIndex is " + adnIndex + " to grp " + grpIndex);
        if (this.mPhoneBookRecords != null && adnIndex > 0 && adnIndex <= this.mPhoneBookRecords.size()) {
            try {
                synchronized (this.mLock) {
                    try {
                        try {
                            MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex - 1);
                            if (rec != null) {
                                String grpList = rec.getGrpIds();
                                if (grpList == null) {
                                    Rlog.e(LOG_TAG, " the adn is not in any group. ");
                                    return false;
                                }
                                String[] grpIds = grpList.split(",");
                                boolean bExist = false;
                                int nOrder = -1;
                                int[] grpIdArray = new int[grpIds.length];
                                for (int i = 0; i < grpIds.length; i++) {
                                    grpIdArray[i] = Integer.parseInt(grpIds[i]);
                                    if (grpIndex == grpIdArray[i]) {
                                        bExist = true;
                                        nOrder = i;
                                        log(" removeContactFromGroup the adn is in the group. i is " + i);
                                    }
                                }
                                if (bExist && nOrder >= 0) {
                                    grpIdArray[nOrder] = 0;
                                    this.mCi.writeUPBGrpEntry(adnIndex, grpIdArray, obtainMessage(12));
                                    try {
                                        this.mLock.wait();
                                    } catch (InterruptedException e) {
                                        Rlog.e(LOG_TAG, "Interrupted Exception in removeContactFromGroup");
                                    }
                                    if (this.mResult == 0) {
                                        ret = true;
                                        updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdArray);
                                        this.mResult = -1;
                                    }
                                } else {
                                    Rlog.e(LOG_TAG, " removeContactFromGroup the adn is not in the group. ");
                                }
                            }
                            logi("removeContactFromGroup end adnIndex is " + adnIndex + " to grp " + grpIndex);
                            return ret;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (IndexOutOfBoundsException e2) {
                        Rlog.e(LOG_TAG, "removeContactFromGroup: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
                        return false;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        Rlog.e(LOG_TAG, "removeContactFromGroup no records or invalid index.");
        return false;
    }

    public boolean updateContactToGroups(int adnIndex, int[] grpIdList) {
        boolean ret = false;
        if (this.mPhoneBookRecords == null || adnIndex <= 0 || adnIndex > this.mPhoneBookRecords.size() || grpIdList == null) {
            Rlog.e(LOG_TAG, "updateContactToGroups no records or invalid index.");
            return false;
        }
        logi("updateContactToGroups begin grpIdList is " + adnIndex + " to grp list count " + grpIdList.length);
        synchronized (this.mLock) {
            MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex - 1);
            if (rec != null) {
                log(" updateContactToGroups the adn index is " + rec.getRecId() + " old grpList is " + rec.getGrpIds());
                int grpCount = this.mUpbCap[7];
                if (grpIdList.length > grpCount) {
                    Rlog.e(LOG_TAG, "updateContactToGroups length of grpIdList > grpCount.");
                    return false;
                }
                int[] grpIdArray = new int[grpCount];
                int i = 0;
                while (i < grpCount) {
                    grpIdArray[i] = i < grpIdList.length ? grpIdList[i] : 0;
                    log("updateContactToGroups i:" + i + ",grpIdArray[" + i + "]:" + grpIdArray[i]);
                    i++;
                }
                this.mCi.writeUPBGrpEntry(adnIndex, grpIdArray, obtainMessage(12));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in updateContactToGroups");
                }
                if (this.mResult == 0) {
                    ret = true;
                    updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdArray);
                    logi(" updateContactToGroups the adn index is " + rec.getRecId());
                    this.mResult = -1;
                }
            }
            logi("updateContactToGroups end grpIdList is " + adnIndex + " to grp list count " + grpIdList.length);
            return ret;
        }
    }

    public boolean moveContactFromGroupsToGroups(int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) {
        int i;
        boolean z;
        int j;
        boolean ret = false;
        boolean z2 = false;
        if (this.mPhoneBookRecords == null || adnIndex <= 0 || adnIndex > this.mPhoneBookRecords.size()) {
            Rlog.e(LOG_TAG, "moveContactFromGroupsToGroups no records or invalid index.");
            return 0;
        }
        synchronized (this.mLock) {
            MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex - 1);
            if (rec != null) {
                int grpCount = this.mUpbCap[7];
                int i2 = this.mUpbCap[7];
                int i3 = this.mUpbCap[5];
                int[] iArr = this.mUpbCap;
                int grpMaxCount = i2 > i3 ? iArr[5] : iArr[7];
                String grpIds = rec.getGrpIds();
                logi(" moveContactFromGroupsToGroups the adn index is " + rec.getRecId() + " original grpIds is " + grpIds + ", fromGrpIdList: " + (fromGrpIdList == null ? "null" : fromGrpIdList) + ", toGrpIdList: " + (toGrpIdList == null ? "null" : toGrpIdList));
                int[] grpIdIntArray = new int[grpCount];
                for (int i4 = 0; i4 < grpCount; i4++) {
                    grpIdIntArray[i4] = 0;
                }
                if (grpIds != null) {
                    String[] grpIdStrArray = grpIds.split(",");
                    for (int i5 = 0; i5 < grpMaxCount; i5++) {
                        grpIdIntArray[i5] = Integer.parseInt(grpIdStrArray[i5]);
                    }
                }
                if (fromGrpIdList != null) {
                    for (int i6 : fromGrpIdList) {
                        for (int j2 = 0; j2 < grpMaxCount; j2++) {
                            if (grpIdIntArray[j2] == i6) {
                                grpIdIntArray[j2] = 0;
                            }
                        }
                    }
                }
                if (toGrpIdList != null) {
                    int i7 = 0;
                    while (i7 < toGrpIdList.length) {
                        boolean bEmpty = false;
                        boolean bExist = false;
                        int k = 0;
                        while (true) {
                            if (k >= grpMaxCount) {
                                i = i7;
                                z = z2;
                                break;
                            }
                            z = z2;
                            i = i7;
                            if (grpIdIntArray[k] != toGrpIdList[i]) {
                                k++;
                                z2 = z;
                                i7 = i;
                            } else {
                                bExist = true;
                                break;
                            }
                        }
                        if (bExist) {
                            Rlog.w(LOG_TAG, "moveContactFromGroupsToGroups the adn isalready in the group.");
                        } else {
                            while (j < grpMaxCount) {
                                j = (grpIdIntArray[j] == 0 || grpIdIntArray[j] == 255) ? 0 : j + 1;
                                bEmpty = true;
                                grpIdIntArray[j] = toGrpIdList[i];
                                break;
                            }
                            if (!bEmpty) {
                                Rlog.e(LOG_TAG, "moveContactFromGroupsToGroups no empty to add.");
                                return z;
                            }
                        }
                        i7 = i + 1;
                        z2 = z;
                    }
                }
                this.mCi.writeUPBGrpEntry(adnIndex, grpIdIntArray, obtainMessage(12));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in moveContactFromGroupsToGroups");
                }
                if (this.mResult == 0) {
                    ret = true;
                    updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdIntArray);
                    logi("moveContactFromGroupsToGroups the adn index is " + rec.getRecId());
                    this.mResult = -1;
                }
            }
            return ret;
        }
    }

    public boolean removeContactGroup(int adnIndex) {
        boolean ret = false;
        logi("removeContactsGroup adnIndex is " + adnIndex);
        if (this.mPhoneBookRecords == null || this.mPhoneBookRecords.isEmpty()) {
            return false;
        }
        synchronized (this.mLock) {
            try {
                try {
                    MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex - 1);
                    if (rec == null) {
                        return false;
                    }
                    log("removeContactsGroup rec is " + rec);
                    String grpList = rec.getGrpIds();
                    if (grpList == null) {
                        return false;
                    }
                    String[] grpIds = grpList.split(",");
                    boolean hasGroup = false;
                    for (String str : grpIds) {
                        int value = Integer.parseInt(str);
                        if (value > 0 && value < 255) {
                            hasGroup = true;
                            break;
                        }
                    }
                    if (hasGroup) {
                        this.mCi.writeUPBGrpEntry(adnIndex, new int[0], obtainMessage(12));
                        try {
                            this.mLock.wait();
                        } catch (InterruptedException e) {
                            Rlog.e(LOG_TAG, "Interrupted Exception in removeContactGroup");
                        }
                        if (this.mResult == 0) {
                            ret = true;
                            int[] grpIdArray = new int[grpIds.length];
                            for (int i = 0; i < grpIds.length; i++) {
                                grpIdArray[i] = 0;
                            }
                            updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdArray);
                            logi(" removeContactGroup the adn index is " + rec.getRecId());
                            this.mResult = -1;
                        }
                    }
                    return ret;
                } catch (IndexOutOfBoundsException e2) {
                    Rlog.e(LOG_TAG, "removeContactGroup: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int hasExistGroup(String grpName) {
        int grpId = -1;
        logi("hasExistGroup grpName is " + grpName);
        if (grpName == null) {
            return -1;
        }
        if (this.mGasForGrp != null && this.mGasForGrp.size() > 0) {
            for (int i = 0; i < this.mGasForGrp.size(); i++) {
                UsimGroup uGas = this.mGasForGrp.get(i);
                if (uGas != null && grpName.equals(uGas.getAlphaTag())) {
                    log("getUsimGroupById index is " + uGas.getRecordIndex() + ", name is " + grpName);
                    grpId = uGas.getRecordIndex();
                    break;
                }
            }
        }
        logi("hasExistGroup grpId is " + grpId);
        return grpId;
    }

    public int getUsimGrpMaxNameLen() {
        int ret;
        logi("getUsimGrpMaxNameLen begin");
        synchronized (this.mUPBCapabilityLock) {
            if (checkIsPhbReady()) {
                if (this.mUpbCap[6] < 0) {
                    queryUpbCapablityAndWait();
                }
                ret = this.mUpbCap[6];
            } else {
                ret = -1;
            }
            logi("getUsimGrpMaxNameLen done: L_Gas is " + ret);
        }
        return ret;
    }

    public int getUsimGrpMaxCount() {
        int ret;
        logi("getUsimGrpMaxCount begin");
        synchronized (this.mUPBCapabilityLock) {
            if (checkIsPhbReady()) {
                if (this.mUpbCap[5] < 0) {
                    queryUpbCapablityAndWait();
                }
                ret = this.mUpbCap[5];
            } else {
                ret = -1;
            }
            logi("getUsimGrpMaxCount done: N_Gas is " + ret);
        }
        return ret;
    }

    private void log(String msg) {
        if (DBG) {
            Rlog.d(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String msg) {
        Rlog.i(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    public boolean isAnrCapacityFree(String anr, int adnIndex, int anrIndex, MtkAdnRecord oldAdn) {
        String oldAnr = null;
        if (oldAdn != null) {
            oldAnr = oldAdn.getAdditionalNumber(anrIndex);
        }
        if (anr == null || anr.equals("") || anrIndex < 0 || getUsimEfType(196) == 168 || (oldAnr != null && !oldAnr.equals(""))) {
            return true;
        }
        synchronized (this.mLock) {
            if (this.mAnrInfo == null || anrIndex >= this.mAnrInfo.size()) {
                this.mCi.queryUPBAvailable(0, anrIndex + 1, obtainMessage(EVENT_QUERY_ANR_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in isAnrCapacityFree");
                }
            }
        }
        return (this.mAnrInfo == null || this.mAnrInfo.get(anrIndex) == null || this.mAnrInfo.get(anrIndex)[1] <= 0) ? false : true;
    }

    public void updateAnrByAdnIndex(String anr, int adnIndex, int anrIndex, MtkAdnRecord oldAdn) throws Throwable {
        String oldAnr;
        int pbrRecNum = (adnIndex - 1) / this.mAdnFileSize;
        int i = (adnIndex - 1) % this.mAdnFileSize;
        if (this.mPbrRecords != null && this.mPbrRecords.size() != 0) {
            SparseArray<UsimPhoneBookManager.File> fileIds = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(pbrRecNum)).mFileIds;
            if (fileIds == null) {
                log("updateAnrByAdnIndex: No anr tag in pbr record 0");
                return;
            }
            if (this.mPhoneBookRecords == null || this.mPhoneBookRecords.isEmpty()) {
                Rlog.w(LOG_TAG, "updateAnrByAdnIndex: mPhoneBookRecords is empty");
                return;
            }
            UsimPhoneBookManager.File anrFile = fileIds.get(196);
            if (anrFile == null) {
                log("updateAnrByAdnIndex no efFile");
                return;
            }
            logi("updateAnrByAdnIndex begin effile " + anrFile);
            if (oldAdn == null) {
                oldAnr = null;
            } else {
                String oldAnr2 = oldAdn.getAdditionalNumber(anrIndex);
                oldAdn.getAasIndex();
                oldAnr = oldAnr2;
            }
            try {
                MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex - 1);
                int aas = rec.getAasIndex();
                Message msg = obtainMessage(9);
                Object obj = this.mLock;
                synchronized (obj) {
                    try {
                        if (anr != null) {
                            try {
                                if (anr.length() != 0) {
                                    String[] param = buildAnrRecordOptmz(anr, aas);
                                    try {
                                        try {
                                            anrFile = anrFile;
                                            obj = obj;
                                            this.mCi.editUPBEntry(0, anrIndex + 1, adnIndex, param[0], param[1], param[2], msg);
                                        } catch (Throwable th) {
                                            th = th;
                                            obj = obj;
                                            throw th;
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        obj = obj;
                                    }
                                }
                                try {
                                    this.mLock.wait();
                                } catch (InterruptedException e) {
                                    Rlog.e(LOG_TAG, "Interrupted Exception in updateAnrByAdnIndexOptmz");
                                }
                                logi("updateAnrByAdnIndex end effile " + anrFile);
                            } catch (Throwable th3) {
                                th = th3;
                                obj = obj;
                            }
                        }
                        if (oldAnr != null && oldAnr.length() != 0) {
                            this.mCi.deleteUPBEntry(0, anrIndex + 1, adnIndex, msg);
                            this.mLock.wait();
                            logi("updateAnrByAdnIndex end effile " + anrFile);
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        throw th;
                    }
                }
            } catch (IndexOutOfBoundsException e2) {
                Rlog.e(LOG_TAG, "updateAnrByAdnIndexOptmz: mPhoneBookRecords IndexOutOfBoundsException size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
            }
        }
    }

    public boolean checkEmailCapacityFree(int adnIndex, String[] emails, MtkAdnRecord oldAdn) {
        int i;
        if (emails == null || getUsimEfType(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP) == 168 || (oldAdn != null && oldAdn.getEmails() != null)) {
            return true;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= emails.length) {
                i = 0;
                break;
            }
            if (emails[i2] == null || emails[i2].equals("")) {
                i2++;
            } else {
                i = 1;
                break;
            }
        }
        if (i == 0) {
            return true;
        }
        synchronized (this.mLock) {
            if (this.mEmailInfo == null || this.mEmailInfo.length != 3) {
                this.mCi.queryUPBAvailable(1, 1, obtainMessage(EVENT_QUERY_EMAIL_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in CheckEmailCapacityFree");
                }
                if (this.mUpbDone == -1) {
                    return true;
                }
                this.mEmailFileSize = countEmailFileSize();
            }
            int used = countEmailCapacity(adnIndex);
            logi("CheckEmailCapacityFree: mEmailFileSize: " + this.mEmailFileSize + " used: " + used + " adnIndex: " + adnIndex);
            return used < this.mEmailFileSize;
        }
    }

    private int countEmailFileSize() {
        int numAdnRecs = this.mPhoneBookRecords.size();
        int totalPbrRecNum = numAdnRecs / this.mAdnFileSize;
        if (numAdnRecs % this.mAdnFileSize > 0) {
            totalPbrRecNum++;
        }
        if (this.mEmailInfo != null && this.mEmailInfo.length == 3 && totalPbrRecNum > 0) {
            return this.mEmailInfo[0] / totalPbrRecNum;
        }
        return 100;
    }

    private int countEmailCapacity(int adnIndex) {
        String[] emails;
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
            return -1;
        }
        int pbrRecNum = (adnIndex - 1) / this.mAdnFileSize;
        int nOffset = this.mAdnFileSize * pbrRecNum;
        int numAdnRecs = this.mPhoneBookRecords.size();
        int nMax = this.mAdnFileSize + nOffset;
        int nMax2 = numAdnRecs < nMax ? numAdnRecs : nMax;
        SparseArray<UsimPhoneBookManager.File> files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(pbrRecNum)).mFileIds;
        UsimPhoneBookManager.File emailFile = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
        int used = 0;
        if (emailFile == null) {
            return -1;
        }
        MtkAdnRecord rec = null;
        for (int i = nOffset; i < nMax2; i++) {
            try {
                MtkAdnRecord rec2 = (MtkAdnRecord) this.mPhoneBookRecords.get(i);
                rec = rec2;
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "countEmailCapacity: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + i);
            }
            if (rec != null && (emails = rec.getEmails()) != null && emails.length > 0 && emails[0].length() > 0) {
                used++;
            }
        }
        log("countEmailCapacity: email used: " + used);
        return used;
    }

    public boolean checkSneCapacityFree(int adnIndex, String sne, MtkAdnRecord oldAdn) {
        String oldSne = null;
        if (oldAdn != null) {
            oldSne = oldAdn.getSne();
        }
        if (sne == null || sne.equals("") || getUsimEfType(195) == 168 || (oldSne != null && !oldSne.equals(""))) {
            return true;
        }
        synchronized (this.mLock) {
            if (this.mSneInfo == null) {
                this.mCi.queryUPBAvailable(2, 1, obtainMessage(EVENT_QUERY_SNE_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in checkSneCapacityFree");
                }
            }
        }
        if (this.mSneInfo != null && this.mSneInfo[1] > 0) {
            return true;
        }
        return false;
    }

    private int getUsimEfType(int efTag) {
        SparseArray<UsimPhoneBookManager.File> files;
        UsimPhoneBookManager.File efFile;
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0 || (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds) == null || (efFile = files.get(efTag)) == null) {
            return 0;
        }
        Rlog.d(LOG_TAG, "[getUsimEfType] efTag: " + efTag + ", type: " + efFile.getParentTag());
        return efFile.getParentTag();
    }

    public boolean checkEmailLength(String[] emails) {
        SparseArray<UsimPhoneBookManager.File> files;
        UsimPhoneBookManager.File emailFile;
        if (emails == null || emails[0] == null || this.mPbrRecords == null || this.mPbrRecords.size() == 0 || (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds) == null || (emailFile = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP)) == null) {
            return true;
        }
        boolean emailType2 = emailFile.getParentTag() == 169;
        int maxDataLength = (this.mEmailRecordSize == -1 || !emailType2) ? this.mEmailRecordSize : this.mEmailRecordSize - 2;
        byte[] eMailData = GsmAlphabet.stringToGsm8BitPacked(emails[0]);
        logi("checkEmailLength eMailData.length=" + eMailData.length + ", maxDataLength=" + maxDataLength);
        return maxDataLength == -1 || eMailData.length <= maxDataLength;
    }

    /* JADX WARN: Code duplicated, block: B:60:0x0111 A[Catch: all -> 0x0128, DONT_GENERATE, TryCatch #2 {all -> 0x0128, blocks: (B:60:0x0111, B:62:0x0113, B:63:0x0118, B:67:0x0126, B:66:0x011f, B:53:0x00f5, B:70:0x0129), top: B:82:0x00bd, inners: #1 }] */
    /* JADX WARN: Code duplicated, block: B:62:0x0113 A[Catch: all -> 0x0128, TRY_LEAVE, TryCatch #2 {all -> 0x0128, blocks: (B:60:0x0111, B:62:0x0113, B:63:0x0118, B:67:0x0126, B:66:0x011f, B:53:0x00f5, B:70:0x0129), top: B:82:0x00bd, inners: #1 }] */
    public int updateEmailsByAdnIndex(String[] emails, int adnIndex, MtkAdnRecord oldAdn) throws Throwable {
        SparseArray<UsimPhoneBookManager.File> files;
        String[] oldEmails;
        int pbrRecNum = (adnIndex - 1) / this.mAdnFileSize;
        int i = (adnIndex - 1) % this.mAdnFileSize;
        if (this.mPbrRecords != null && this.mPbrRecords.size() != 0 && (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(pbrRecNum)).mFileIds) != null && files.size() != 0 && this.mPhoneBookRecords != null && !this.mPhoneBookRecords.isEmpty()) {
            UsimPhoneBookManager.File efFile = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
            if (efFile == null) {
                log("updateEmailsByAdnIndex: No email tag in pbr record 0");
                return 0;
            }
            String oldEmail = null;
            if (oldAdn == null) {
                oldEmails = null;
            } else {
                String[] oldEmails2 = oldAdn.getEmails();
                oldEmails = oldEmails2;
            }
            if (oldEmails != null && oldEmails.length != 0 && !TextUtils.isEmpty(oldEmails[0])) {
                oldEmail = oldEmails[0];
            }
            int efid = efFile.getEfid();
            boolean z = efFile.getParentTag() == 169;
            efFile.getIndex();
            logi("updateEmailsByAdnIndex: pbrrecNum is " + pbrRecNum + " EF_EMAIL id is " + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
            Message msg = obtainMessage(8);
            Object obj = this.mLock;
            synchronized (obj) {
                try {
                    if (emails != null) {
                        try {
                            if (emails.length == 0 || TextUtils.isEmpty(emails[0])) {
                                if (oldEmail == null) {
                                    return 0;
                                }
                                this.mCi.deleteUPBEntry(1, 1, adnIndex, msg);
                            } else {
                                if (!emails[0].equals(oldEmail)) {
                                    String temp = encodeToUcs2(emails[0]);
                                    try {
                                        obj = obj;
                                        this.mCi.editUPBEntry(1, 1, adnIndex, temp, null, msg);
                                    } catch (Throwable th) {
                                        th = th;
                                        obj = obj;
                                    }
                                } else {
                                    try {
                                        return 0;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        obj = obj;
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            obj = obj;
                        }
                    } else {
                        if (oldEmail == null) {
                            return 0;
                        }
                        this.mCi.deleteUPBEntry(1, 1, adnIndex, msg);
                    }
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e) {
                        Rlog.e(LOG_TAG, "Interrupted Exception in updateEmailsByAdnIndex");
                    }
                    return 0;
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }
        return 0;
    }

    public void updateUsimPhonebookRecordsList(int index, MtkAdnRecord newAdn) {
        logi("updateUsimPhonebookRecordsList update the " + index + "th record.");
        if (index < this.mPhoneBookRecords.size()) {
            MtkAdnRecord oldAdn = (MtkAdnRecord) this.mPhoneBookRecords.get(index);
            if (oldAdn != null && oldAdn.getGrpIds() != null) {
                newAdn.setGrpIds(oldAdn.getGrpIds());
            }
            this.mPhoneBookRecords.set(index, newAdn);
            this.mRefreshAdnInfo = true;
        }
    }

    private void updatePhoneAdnRecordWithGrpByIndex(int recIndex, int adnIndex, int[] grpIds) {
        int grpSize;
        log("updatePhoneAdnRecordWithGrpByIndex the " + recIndex + "th grp ");
        if (recIndex <= this.mPhoneBookRecords.size() && (grpSize = grpIds.length) > 0) {
            try {
                MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(recIndex);
                log("updatePhoneAdnRecordWithGrpByIndex the adnIndex is " + adnIndex + "; the original index is " + rec.getRecId());
                StringBuilder grpIdsSb = new StringBuilder();
                for (int i = 0; i < grpSize - 1; i++) {
                    grpIdsSb.append(grpIds[i]);
                    grpIdsSb.append(",");
                }
                int i2 = grpSize - 1;
                grpIdsSb.append(grpIds[i2]);
                rec.setGrpIds(grpIdsSb.toString());
                log("updatePhoneAdnRecordWithGrpByIndex grpIds is " + grpIdsSb.toString());
                this.mPhoneBookRecords.set(recIndex, rec);
                log("updatePhoneAdnRecordWithGrpByIndex the rec:" + rec);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithGrpByIndex: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + recIndex);
            }
        }
    }

    private void updatePhoneAdnRecordWithEmailByIndex(int emailIndex, int adnIndex, byte[] emailRecData) {
        log("updatePhoneAdnRecordWithEmailByIndex emailIndex = " + emailIndex + ",adnIndex = " + adnIndex);
        if (emailRecData == null || this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
            return;
        }
        boolean emailType2 = false;
        if (((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP) != null) {
            emailType2 = ((UsimPhoneBookManager.File) ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP)).getParentTag() == 169;
        }
        log("updatePhoneAdnRecordWithEmailByIndex: Type2: " + emailType2 + " emailData: " + IccUtils.bytesToHexString(emailRecData));
        int length = emailRecData.length;
        if (emailType2 && emailRecData.length >= 2) {
            length = emailRecData.length - 2;
        }
        log("updatePhoneAdnRecordWithEmailByIndex length = " + length);
        byte[] validEMailData = new byte[length];
        for (int i = 0; i < length; i++) {
            validEMailData[i] = -1;
        }
        System.arraycopy(emailRecData, 0, validEMailData, 0, length);
        try {
            String email = IccUtils.adnStringFieldToString(validEMailData, 0, length);
            if (email != null && !email.equals("")) {
                MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex);
                rec.setEmails(new String[]{email});
            }
        } catch (IndexOutOfBoundsException e) {
            Rlog.e(LOG_TAG, "[JE]updatePhoneAdnRecordWithEmailByIndex " + e.getMessage());
        }
    }

    private void readAnrRecordSize() {
        logi("readAnrRecordSize");
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
            Rlog.w(LOG_TAG, "readAnrRecordSize: PBR null ");
            return;
        }
        SparseArray<UsimPhoneBookManager.File> fileIds = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds;
        if (fileIds == null) {
            Rlog.w(LOG_TAG, "readAnrRecordSize: fileIds null ");
            return;
        }
        UsimPhoneBookManager.File anrFile = fileIds.get(196);
        if (fileIds.size() == 0 || anrFile == null) {
            this.mAnrRecordSize = 0;
            Rlog.w(LOG_TAG, "readAnrRecordSize: No anr tag in pbr file ");
            return;
        }
        int efid = anrFile.getEfid();
        int[] size = readEFLinearRecordSize(efid);
        if (size == null || size.length != 3) {
            Rlog.e(LOG_TAG, "readAnrRecordSize: read record size error.");
        } else {
            this.mAnrRecordSize = size[0];
            logi("readAnrRecordSize end size = " + this.mAnrRecordSize);
        }
    }

    private void readEmailRecordSize() {
        logi("readEmailRecordSize");
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
            Rlog.w(LOG_TAG, "readEmailRecordSize: PBR null");
            return;
        }
        SparseArray<UsimPhoneBookManager.File> fileIds = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds;
        if (fileIds == null) {
            Rlog.w(LOG_TAG, "readEmailRecordSize: fileId null");
            return;
        }
        UsimPhoneBookManager.File emailFile = fileIds.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
        if (fileIds.size() == 0 || emailFile == null) {
            this.mEmailRecordSize = 0;
            Rlog.w(LOG_TAG, "readEmailRecordSize: No email tag in pbr file ");
            return;
        }
        int efid = emailFile.getEfid();
        int[] size = readEFLinearRecordSize(efid);
        if (size == null || size.length != 3) {
            Rlog.e(LOG_TAG, "readEmailRecordSize: read record size error.");
            return;
        }
        this.mEmailFileSize = size[2];
        this.mEmailRecordSize = size[0];
        logi("readEmailRecordSize Size:" + this.mEmailFileSize + "," + this.mEmailRecordSize);
    }

    private boolean loadAasFiles() {
        synchronized (this.mLock) {
            if (this.mAasForAnr == null || this.mAasForAnr.size() == 0) {
                if (!this.mIsPbrPresent.booleanValue()) {
                    Rlog.e(LOG_TAG, "No PBR files");
                    return false;
                }
                loadPBRFiles();
                if (this.mPbrRecords != null && this.mPbrRecords.size() != 0) {
                    int numRecs = this.mPbrRecords.size();
                    if (this.mAasForAnr == null) {
                        this.mAasForAnr = new ArrayList<>();
                    }
                    this.mAasForAnr.clear();
                    logi("loadAasFiles read num:" + numRecs + ", " + this.mPbrNeedNotify);
                    readAasFileAndWaitOptmz();
                }
                return false;
            }
            return true;
        }
    }

    public ArrayList<AlphaTag> getUsimAasList() {
        ArrayList<String> allAas;
        logi("getUsimAasList start mPbrNeedNotify:" + this.mPbrNeedNotify);
        ArrayList<AlphaTag> results = new ArrayList<>();
        if (!loadAasFiles() || (allAas = this.mAasForAnr) == null) {
            return results;
        }
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < allAas.size(); j++) {
                String value = allAas.get(j);
                logi("aasIndex:" + (j + 1) + ",pbrIndex:" + i + ",value:" + value);
                AlphaTag tag = new AlphaTag(j + 1, value, i);
                results.add(tag);
            }
        }
        return results;
    }

    public String getUsimAasById(int index, int pbrIndex) {
        logi("getUsimAasById by id " + index + ",pbrIndex " + pbrIndex + ",mPbrNeedNotify " + this.mPbrNeedNotify);
        if (!loadAasFiles()) {
            return null;
        }
        ArrayList<String> map = this.mAasForAnr;
        if (map != null) {
            logi("getUsimAasById NonNULL by id " + index + ",pbrIndex " + pbrIndex);
            return map.get(index - 1);
        }
        logi("getUsimAasById NULL by id " + index + ",pbrIndex " + pbrIndex);
        return null;
    }

    public boolean removeUsimAasById(int index, int pbrIndex) {
        logi("removeUsimAasById by id " + index + ",pbrIndex " + pbrIndex + ",mPbrNeedNotify " + this.mPbrNeedNotify);
        if (!loadAasFiles()) {
            return false;
        }
        SparseArray<UsimPhoneBookManager.File> files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(pbrIndex)).mFileIds;
        if (files == null || files.get(199) == null) {
            Rlog.e(LOG_TAG, "removeUsimAasById-PBR have no AAS EF file");
            return false;
        }
        int efid = files.get(199).getEfid();
        log("removeUsimAasById result,efid:" + efid);
        if (this.mFh != null) {
            Message msg = obtainMessage(10);
            int len = getUsimAasMaxNameLen();
            byte[] aasString = new byte[len];
            for (int i = 0; i < len; i++) {
                aasString[i] = -1;
            }
            synchronized (this.mAasLock) {
                this.mCi.deleteUPBEntry(3, 1, index, msg);
                try {
                    this.mAasLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in removesimAasById");
                }
            }
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar == null || ar.exception == null) {
                ArrayList<String> list = this.mAasForAnr;
                if (list != null) {
                    log("remove aas done " + list.get(index - 1));
                    list.set(index - 1, null);
                } else {
                    log("remove aas mAasForAnr is null ");
                }
                return true;
            }
            Rlog.e(LOG_TAG, "removeUsimAasById exception " + ar.exception);
            return false;
        }
        Rlog.e(LOG_TAG, "removeUsimAasById-IccFileHandler is null");
        return false;
    }

    public int insertUsimAas(String aasName) {
        int j;
        int aasIndex;
        logi("insertUsimAas begin" + aasName + ",mPbrNeedNotify " + this.mPbrNeedNotify);
        if (aasName == null || aasName.length() == 0) {
            return 0;
        }
        if (!loadAasFiles()) {
            return -1;
        }
        int limit = getUsimAasMaxNameLen();
        int len = aasName.length();
        if (len > limit) {
            return 0;
        }
        synchronized (this.mAasLock) {
            boolean found = false;
            ArrayList<String> allAas = this.mAasForAnr;
            if (allAas != null) {
                while (true) {
                    if (j >= allAas.size()) {
                        aasIndex = 0;
                        break;
                    }
                    String value = allAas.get(j);
                    j = (value == null || value.length() == 0) ? 0 : j + 1;
                    found = true;
                    int aasIndex2 = j + 1;
                    aasIndex = aasIndex2;
                    break;
                }
                log("insertUsimAas aasIndex:" + aasIndex + ",found:" + found);
                if (!found) {
                    return -2;
                }
                String temp = encodeToUcs2(aasName);
                Message msg = obtainMessage(10);
                this.mCi.editUPBEntry(3, 0, aasIndex, temp, null, msg);
                try {
                    this.mAasLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in insertUsimAas");
                }
                AsyncResult ar = (AsyncResult) msg.obj;
                logi("insertUsimAas UPB_EF_AAS: ar " + ar);
                if (ar != null && ar.exception != null) {
                    Rlog.e(LOG_TAG, "insertUsimAas exception " + ar.exception);
                    return -1;
                }
                ArrayList<String> list = this.mAasForAnr;
                if (list == null) {
                    logi("insertUsimAas mAasForAnr is null");
                } else {
                    list.set(aasIndex - 1, aasName);
                    logi("insertUsimAas update mAasForAnr done");
                }
                return aasIndex;
            }
            Rlog.e(LOG_TAG, "insertUsimAas, mAasForAnr is null");
            return -2;
        }
    }

    public boolean updateUsimAas(int index, int pbrIndex, String aasName) throws Throwable {
        Object obj;
        logi("updateUsimAas index " + index + ",pbrIndex " + pbrIndex + ",aasName " + aasName + ",mPbrNeedNotify " + this.mPbrNeedNotify);
        if (!loadAasFiles()) {
            return false;
        }
        ArrayList<String> map = this.mAasForAnr;
        if (map == null) {
            Rlog.e(LOG_TAG, "updateUsimAas, mAasForAnr is null, index " + index);
            return false;
        }
        if (index <= 0 || index > map.size()) {
            Rlog.e(LOG_TAG, "updateUsimAas not found aas index " + index);
            return 0;
        }
        String aas = map.get(index - 1);
        log("updateUsimAas old aas " + aas);
        if (aasName == null || aasName.length() == 0) {
            return removeUsimAasById(index, pbrIndex);
        }
        int limit = getUsimAasMaxNameLen();
        int len = aasName.length();
        log("updateUsimAas aas limit " + limit);
        if (len > limit) {
            return false;
        }
        log("updateUsimAas offset 0");
        int aasIndex = index + 0;
        String temp = encodeToUcs2(aasName);
        Message msg = obtainMessage(10);
        Object obj2 = this.mAasLock;
        synchronized (obj2) {
            try {
                try {
                    obj = obj2;
                    try {
                        this.mCi.editUPBEntry(3, 0, aasIndex, temp, null, msg);
                        try {
                            this.mAasLock.wait();
                        } catch (InterruptedException e) {
                            Rlog.e(LOG_TAG, "Interrupted Exception in updateUsimAas");
                        }
                        AsyncResult ar = (AsyncResult) msg.obj;
                        if (ar == null || ar.exception == null) {
                            ArrayList<String> list = this.mAasForAnr;
                            if (list == null) {
                                logi("updateUsimAas mAasForAnr is null");
                                return true;
                            }
                            list.set(index - 1, aasName);
                            logi("updateUsimAas update mAasForAnr done");
                            return true;
                        }
                        Rlog.e(LOG_TAG, "updateUsimAas exception " + ar.exception);
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                obj = obj2;
            }
        }
    }

    public boolean updateAdnAas(int adnIndex, int aasIndex) throws Throwable {
        int i = (adnIndex - 1) / this.mAdnFileSize;
        int i2 = (adnIndex - 1) % this.mAdnFileSize;
        try {
            MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex - 1);
            rec.setAasIndex(aasIndex);
            for (int i3 = 0; i3 < 3; i3++) {
                String anr = rec.getAdditionalNumber(i3);
                updateAnrByAdnIndex(anr, adnIndex, i3, rec);
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            Rlog.e(LOG_TAG, "updateADNAAS: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
            return false;
        }
    }

    public int getUsimAasMaxNameLen() {
        logi("getUsimAasMaxNameLen begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[4] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(EVENT_UPB_CAPABILITY_QUERY_DONE));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getUsimAasMaxNameLen");
                }
            }
        }
        logi("getUsimAasMaxNameLen done: L_AAS is " + this.mUpbCap[4]);
        return this.mUpbCap[4];
    }

    public int getUsimAasMaxCount() {
        logi("getUsimAasMaxCount begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[3] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(EVENT_UPB_CAPABILITY_QUERY_DONE));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getUsimAasMaxCount");
                }
            }
        }
        logi("getUsimAasMaxCount done: N_AAS is " + this.mUpbCap[3]);
        return this.mUpbCap[3];
    }

    public void loadPBRFiles() {
        if (!this.mIsPbrPresent.booleanValue()) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
                this.mPbrNeedNotify++;
                readPbrFileAndWait();
            }
        }
    }

    public int getAnrCount() {
        logi("getAnrCount begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[0] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(EVENT_UPB_CAPABILITY_QUERY_DONE));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getAnrCount");
                }
            }
        }
        if (this.mAnrRecordSize <= 0) {
            logi("getAnrCount end mAnrRecordSize:" + this.mAnrRecordSize);
            return this.mAnrRecordSize;
        }
        logi("getAnrCount done: N_ANR is " + this.mUpbCap[0]);
        return this.mUpbCap[0] > 0 ? 1 : 0;
    }

    public int getEmailCount() {
        logi("getEmailCount begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[1] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(EVENT_UPB_CAPABILITY_QUERY_DONE));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getEmailCount");
                }
            }
        }
        if (this.mEmailRecordSize <= 0) {
            logi("getEmailCount end mEmailRecordSize:" + this.mEmailRecordSize);
            return this.mEmailRecordSize;
        }
        logi("getEmailCount done: N_EMAIL is " + this.mUpbCap[1]);
        return this.mUpbCap[1] > 0 ? 1 : 0;
    }

    public boolean hasSne() {
        log("hasSne begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[2] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(EVENT_UPB_CAPABILITY_QUERY_DONE));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in hasSne");
                }
            }
        }
        log("hasSne done: N_Sne is " + this.mUpbCap[2]);
        return this.mUpbCap[2] > 0;
    }

    public int getSneRecordLen() {
        SparseArray<UsimPhoneBookManager.File> files;
        int[] size;
        if (!hasSne()) {
            return 0;
        }
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0 || this.mPbrRecords.get(0) == null || (files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(0)).mFileIds) == null) {
            return -1;
        }
        UsimPhoneBookManager.File sneFile = files.get(195);
        if (sneFile == null) {
            return 0;
        }
        int efid = sneFile.getEfid();
        boolean sneType2 = sneFile.getParentTag() == 169;
        logi("getSneRecordLen: EFSNE id is " + efid);
        if (this.mRecordSize != null && this.mRecordSize.get(efid) != null) {
            int[] size2 = this.mRecordSize.get(efid);
            size = size2;
        } else {
            size = readEFLinearRecordSize(efid);
        }
        if (size == null) {
            return 0;
        }
        if (sneType2) {
            int resultSize = size[0] - 2;
            return resultSize;
        }
        int resultSize2 = size[0];
        return resultSize2;
    }

    public int getUpbDone() {
        return this.mUpbDone;
    }

    private void updatePhoneAdnRecordWithSneByIndex(int recNum, int adnIndex, byte[] recData) {
        if (recData == null) {
            return;
        }
        String sne = IccUtils.adnStringFieldToString(recData, 0, recData.length);
        log("updatePhoneAdnRecordWithSneByIndex index " + adnIndex + " recData file is " + sne);
        if (sne != null && !sne.equals("")) {
            try {
                MtkAdnRecord rec = (MtkAdnRecord) this.mPhoneBookRecords.get(adnIndex);
                rec.setSne(sne);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithSneByIndex: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + adnIndex);
            }
        }
    }

    public int updateSneByAdnIndex(String sne, int adnIndex, MtkAdnRecord oldAdn) {
        String oldSne;
        int efid;
        logi("updateSneByAdnIndex begin, adnIndex " + adnIndex);
        int pbrRecNum = (adnIndex - 1) / this.mAdnFileSize;
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
            return -1;
        }
        Message msg = obtainMessage(11);
        SparseArray<UsimPhoneBookManager.File> files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(pbrRecNum)).mFileIds;
        if (files == null || files.get(195) == null) {
            log("updateSneByAdnIndex: No SNE tag in pbr file 0");
            return -1;
        }
        if (this.mPhoneBookRecords == null || this.mPhoneBookRecords.isEmpty()) {
            return -1;
        }
        if (oldAdn == null) {
            oldSne = null;
        } else {
            String oldSne2 = oldAdn.getSne();
            oldSne = oldSne2;
        }
        UsimPhoneBookManager.File sneFile = files.get(195);
        if (sneFile == null) {
            efid = 0;
        } else {
            int efid2 = sneFile.getEfid();
            efid = efid2;
        }
        log("updateSneByAdnIndex: EF_SNE id is " + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
        log("updateSneByAdnIndex: efIndex is 1");
        synchronized (this.mLock) {
            if (sne == null) {
                if (oldSne != null) {
                    this.mCi.deleteUPBEntry(2, 1, adnIndex, msg);
                }
                return 0;
            }
            try {
                if (sne.length() == 0) {
                    if (oldSne != null && oldSne.length() != 0) {
                        this.mCi.deleteUPBEntry(2, 1, adnIndex, msg);
                    }
                    return 0;
                }
                if (sne.equals(oldSne)) {
                    return 0;
                }
                String temp = encodeToUcs2(sne);
                this.mCi.editUPBEntry(2, 1, adnIndex, temp, null, msg);
            } catch (Throwable th) {
                throw th;
            }
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                Rlog.e(LOG_TAG, "Interrupted Exception in updateSneByAdnIndex");
            }
            logi("updateSneByAdnIndex end, adnIndex " + adnIndex);
            return this.mResult;
        }
    }

    public int[] getAdnRecordsCapacityExt() {
        int[] capacity = new int[6];
        if (this.mRefreshAdnInfo || this.mRefreshEmailInfo || this.mRefreshAnrInfo || this.mAdnRecordSize == null || this.mAdnRecordSize.length != 4) {
            getAdnStorageInfo();
            this.mRefreshAdnInfo = false;
        }
        if (this.mAdnRecordSize == null || this.mAdnRecordSize.length != 4) {
            return null;
        }
        capacity[0] = this.mAdnRecordSize[1];
        capacity[1] = this.mAdnRecordSize[0];
        if (this.mRefreshEmailInfo || this.mEmailInfo == null || this.mEmailInfo.length != 3) {
            synchronized (this.mLock) {
                this.mCi.queryUPBAvailable(1, 1, obtainMessage(EVENT_QUERY_EMAIL_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getAdnRecordsCapacityExt");
                }
            }
            this.mRefreshEmailInfo = false;
        }
        if (this.mEmailInfo == null || this.mEmailInfo.length != 3) {
            return null;
        }
        capacity[2] = this.mEmailInfo[0];
        capacity[3] = this.mEmailInfo[0] - this.mEmailInfo[1];
        if (this.mRefreshAnrInfo || this.mAnrInfo == null || this.mAnrInfo.get(0) == null || this.mAnrInfo.get(0).length != 3) {
            synchronized (this.mLock) {
                this.mCi.queryUPBAvailable(0, 1, obtainMessage(EVENT_QUERY_ANR_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e2) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getAdnRecordsCapacityExt");
                }
            }
            this.mRefreshAnrInfo = false;
        }
        if (this.mAnrInfo == null || this.mAnrInfo.get(0) == null || this.mAnrInfo.get(0).length != 3) {
            return null;
        }
        capacity[4] = this.mAnrInfo.get(0)[0];
        capacity[5] = this.mAnrInfo.get(0)[0] - this.mAnrInfo.get(0)[1];
        logi("getAdnRecordsCapacityExt: max adn=" + capacity[0] + ", used adn=" + capacity[1] + ", max email=" + capacity[2] + ", used email=" + capacity[3] + ", max anr=" + capacity[4] + ", used anr=" + capacity[5]);
        return capacity;
    }

    private int[] getAdnStorageInfo() {
        logi("getAdnStorageInfo");
        if (this.mCi != null) {
            synchronized (this.mLock) {
                this.mCi.queryPhbStorageInfo(0, obtainMessage(EVENT_QUERY_PHB_ADN_INFO));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getAdnStorageInfo");
                }
            }
            return this.mAdnRecordSize;
        }
        Rlog.w(LOG_TAG, "GetAdnStorageInfo: filehandle is null.");
        return null;
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x03df */
    /* JADX WARN: Failed to apply debug info
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v8 ??, new type: byte[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyDebugInfo(TypeUpdate.java:77)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:137)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:133)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.searchAndApplyVarDebugInfo(DebugInfoApplyVisitor.java:79)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.lambda$applyDebugInfo$0(DebugInfoApplyVisitor.java:68)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:68)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.visit(DebugInfoApplyVisitor.java:55)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 8 more
     */
    /* JADX WARN: Failed to calculate best type for var: r15v8 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v8 ??, new type: byte[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 7 more
     */
    /* JADX WARN: Failed to set immutable type for var: r15v8 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r15v8 ??, new type: byte[]
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
    	... 6 more
     */
    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r17v7 ??, new type: boolean
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
        	... 5 more
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.mediatek.internal.telephony.phb.UsimPBMemInfo[] getPhonebookMemStorageExt() {
        /*
            Method dump skipped, instruction units count: 1237
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.getPhonebookMemStorageExt():com.mediatek.internal.telephony.phb.UsimPBMemInfo[]");
    }

    private UsimPBMemInfo[] getPhonebookMemStorageExt2G() {
        ArrayList<byte[]> ext1;
        UsimPBMemInfo[] response = {new UsimPBMemInfo()};
        int[] size = readEFLinearRecordSize(28474);
        if (size != null) {
            response[0].setAdnLength(size[0]);
            if (isAdnAccessible()) {
                response[0].setAdnTotal(size[2]);
            } else {
                response[0].setAdnTotal(0);
            }
        }
        response[0].setAdnType(168);
        response[0].setSliceIndex(1);
        int[] size2 = readEFLinearRecordSize(28490);
        if (size2 != null) {
            response[0].setExt1Length(size2[0]);
            response[0].setExt1Total(size2[2]);
        }
        response[0].setExt1Type(170);
        if (this.mFh != null) {
            synchronized (this.mLock) {
                Message msg = obtainMessage(1001);
                msg.arg1 = 0;
                this.mFh.loadEFLinearFixedAll(28490, msg);
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in readExt1FileAndWait");
                }
            }
            int used = 0;
            if (this.mExt1FileList != null && this.mExt1FileList.size() > 0 && (ext1 = this.mExt1FileList.get(0)) != null) {
                int len = ext1.size();
                for (int i = 0; i < len; i++) {
                    byte[] arr = ext1.get(i);
                    log("ext1[" + i + "]=" + IccUtils.bytesToHexString(arr));
                    if (arr != null && arr.length > 0 && (arr[0] == 1 || arr[0] == 2)) {
                        used++;
                    }
                }
            }
            response[0].setExt1Used(used);
            logi("getPhonebookMemStorageExt2G:" + response[0]);
            return response;
        }
        Rlog.e(LOG_TAG, "readExt1FileAndWait-IccFileHandler is null");
        return response;
    }

    public int[] readEFLinearRecordSize(int fileId) {
        int[] recordSize;
        logi("readEFLinearRecordSize fileid:" + Integer.toHexString(fileId).toUpperCase(Locale.ROOT) + ",recordNum:" + this.mReadEFLinerRecordSizeNum);
        Request getSizeRequest = new Request();
        synchronized (getSizeRequest) {
            Message msg = this.mBaseHandler.obtainMessage(1000, getSizeRequest);
            msg.arg1 = fileId;
            if (this.mFh != null) {
                this.mReadEFLinerRecordSizeNum++;
                this.mFh.getEFLinearRecordSize(fileId, msg);
                waitForResult(getSizeRequest);
            } else {
                Rlog.e(LOG_TAG, "readEFLinearRecordSize-IccFileHandler is null");
            }
            if (this.mFh != null) {
                this.mReadEFLinerRecordSizeNum--;
            }
            recordSize = this.mRecordSize != null ? this.mRecordSize.get(fileId) : null;
            if (recordSize != null) {
                logi("readEFLinearRecordSize fileid:" + Integer.toHexString(fileId).toUpperCase(Locale.ROOT) + ",len:" + recordSize[0] + ",total:" + recordSize[1] + ",count:" + recordSize[2] + ",recordNum:" + this.mReadEFLinerRecordSizeNum);
            } else {
                logi("readEFLinearRecordSize fileid:" + Integer.toHexString(fileId).toUpperCase(Locale.ROOT) + ",recordSize: null");
            }
        }
        return recordSize;
    }

    protected void waitForResult(Request request) {
        synchronized (request) {
            while (!request.mStatus.get()) {
                try {
                    request.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "interrupted while trying to update by search");
                }
            }
        }
    }

    private void readExt1FileAndWait(int recId) {
        logi("readExt1FileAndWait " + recId);
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0 || this.mPbrRecords.get(recId) == null) {
            return;
        }
        SparseArray<UsimPhoneBookManager.File> files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(recId)).mFileIds;
        if (files == null || files.get(194) == null) {
            Rlog.e(LOG_TAG, "readExt1FileAndWait-PBR have no Ext1 record");
            return;
        }
        int efid = files.get(194).getEfid();
        log("readExt1FileAndWait-get EXT1 EFID " + efid);
        if (this.mExt1FileList != null && recId < this.mExt1FileList.size()) {
            log("EXT1 has been loaded for Pbr number " + recId);
            return;
        }
        if (this.mFh != null) {
            synchronized (this.mLock) {
                Message msg = obtainMessage(1001);
                msg.arg1 = recId;
                this.mFh.loadEFLinearFixedAll(efid, msg);
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in readExt1FileAndWait");
                }
            }
            return;
        }
        Rlog.e(LOG_TAG, "readExt1FileAndWait-IccFileHandler is null");
    }

    private boolean checkIsPhbReady() {
        String strCurSimState = "";
        if (!SubscriptionManager.isValidSlotIndex(this.mSlotId)) {
            log("[isPhbReady] InvalidSlotId slotId: " + this.mSlotId);
            return false;
        }
        String strPhbReady = TelephonyManager.getTelephonyProperty(this.mSlotId, "vendor.gsm.sim.ril.phbready", "false");
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            logi("[isPhbReady] isPhbReady: " + strPhbReady);
            return strPhbReady.equals("true");
        }
        String strAllSimState = SystemProperties.get("gsm.sim.state");
        if (strAllSimState != null && strAllSimState.length() > 0) {
            String[] values = strAllSimState.split(",");
            if (this.mSlotId >= 0 && this.mSlotId < values.length && values[this.mSlotId] != null) {
                strCurSimState = values[this.mSlotId];
            }
        }
        boolean isSimLocked = strCurSimState.equals("NETWORK_LOCKED") || strCurSimState.equals("PIN_REQUIRED");
        logi("[isPhbReady] isPhbReady: " + strPhbReady + ",strSimState: " + strAllSimState);
        return strPhbReady.equals("true") && !isSimLocked;
    }

    public boolean isAdnAccessible() {
        if (this.mFh != null && this.mCurrentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_SIM) {
            synchronized (this.mLock) {
                Message response = obtainMessage(20);
                this.mFh.selectEFFile(28474, response);
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in isAdnAccessible");
                }
            }
            if (this.mEfData != null) {
                int fs = this.mEfData.getFileStatus();
                return (fs & 5) > 0;
            }
        }
        return true;
    }

    public boolean isUsimPhbEfAndNeedReset(int fileId) {
        logi("isUsimPhbEfAndNeedReset, fileId: " + Integer.toHexString(fileId).toUpperCase(Locale.ROOT));
        if (this.mPbrRecords == null || this.mPbrRecords.size() == 0) {
            Rlog.e(LOG_TAG, "isUsimPhbEfAndNeedReset, No PBR files");
            return false;
        }
        int numRecs = this.mPbrRecords.size();
        for (int i = 0; i < numRecs; i++) {
            SparseArray<UsimPhoneBookManager.File> files = ((UsimPhoneBookManager.PbrRecord) this.mPbrRecords.get(i)).mFileIds;
            for (int j = 192; j <= 203; j++) {
                if (j == 197 || j == 201 || j == 203) {
                    logi("isUsimPhbEfAndNeedReset, not reset EF: " + j);
                } else if (files.get(j) != null && fileId == files.get(j).getEfid()) {
                    logi("isUsimPhbEfAndNeedReset, return true with EF: " + j);
                    return true;
                }
            }
        }
        log("isUsimPhbEfAndNeedReset, return false.");
        return false;
    }
}
