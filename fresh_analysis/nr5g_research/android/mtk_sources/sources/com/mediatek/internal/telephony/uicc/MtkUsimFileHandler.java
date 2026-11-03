package com.mediatek.internal.telephony.uicc;

import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UsimFileHandler;

/* JADX INFO: loaded from: classes.dex */
public final class MtkUsimFileHandler extends UsimFileHandler {
    static final String LOG_TAG_EX = "MtkUsimFH";
    static final int MIN_COUNT = 3;
    static final int VALID_OPL_LENGTH = 8;
    MtkIccFileHandler mMtkIccFh;

    public MtkUsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        super(app, aid, ci);
        this.mMtkIccFh = null;
        this.mMtkIccFh = new MtkIccFileHandler(app, aid, ci);
    }

    private boolean isPayloadValid(byte[] byteArray) {
        if (byteArray == null) {
            return false;
        }
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 255) != 255) {
                logd("isPayloadValid, byteArray = " + ((int) byteArray[i]) + " , with id: " + i);
                return true;
            }
        }
        return false;
    }

    protected String getEFPath(int efid) {
        switch (efid) {
            case MtkIccConstants.EF_RAT /* 20278 */:
                return "7FFF7F665F30";
            case MtkIccConstants.EF_SMSP /* 28482 */:
            case 28489:
            case MtkIccConstants.EF_ECC /* 28599 */:
                return "3F007FFF";
            case 28630:
            case MtkIccConstants.EF_USIM_GBANL /* 28634 */:
                return "3F007FFF";
            default:
                Rlog.d(LOG_TAG_EX, "Usim aosp default getEFPath.");
                return super.getEFPath(efid);
        }
    }

    public void loadEFLinearFixedAll(int fileid, Message onLoaded, boolean is7FFF) {
        this.mMtkIccFh.loadEFLinearFixedAllByPath(fileid, onLoaded, is7FFF);
    }

    public void loadEFLinearFixedAll(int fileid, int mode, Message onLoaded) {
        this.mMtkIccFh.loadEFLinearFixedAllByMode(fileid, mode, onLoaded);
    }

    public void loadEFTransparent(int fileid, String path, Message onLoaded) {
        this.mMtkIccFh.loadEFTransparent(fileid, path, onLoaded);
    }

    public void updateEFTransparent(int fileid, String path, byte[] data, Message onComplete) {
        this.mMtkIccFh.updateEFTransparent(fileid, path, data, onComplete);
    }

    public void readEFLinearFixed(int fileid, int recordNum, int recordSize, Message onLoaded) {
        this.mMtkIccFh.readEFLinearFixed(fileid, recordNum, recordSize, onLoaded);
    }

    public void selectEFFile(int fileid, Message onLoaded) {
        this.mMtkIccFh.selectEFFile(fileid, onLoaded);
    }

    public boolean continueIoRequest(IccFileHandler.LoadLinearFixedContext lc) {
        if (lc.mEfid != 28613 && lc.mEfid != 28614) {
            return true;
        }
        logd("continueIoRequest to check, lc.mEfid: " + lc.mEfid);
        if (lc.results.size() < 3) {
            return true;
        }
        for (int i = lc.results.size() - 1; i >= 0; i--) {
            if (isPayloadValid((byte[]) lc.results.get(i))) {
                return true;
            }
            if (i <= lc.results.size() - 3) {
                return false;
            }
        }
        return false;
    }

    public void createDummyRecordsAndSendResult(IccFileHandler.LoadLinearFixedContext lc, IccIoResult result, Message response) {
        logd("EVENT_READ_RECORD_DONE, create invalid record from index: " + lc.mRecordNum + " , total count: " + lc.mCountRecords);
        for (int i = lc.mRecordNum; i <= lc.mCountRecords; i++) {
            lc.results.add(result.payload);
            lc.mRecordNum++;
        }
        sendResult(response, lc.results, null);
    }

    public void addIccIoResult(IccFileHandler.LoadLinearFixedContext lc, IccIoResult result) {
        if (lc.mEfid == 28614 && result.payload != null && result.payload.length > 8) {
            logd("addResult, normalize OPL result for valid length");
            byte[] copyByte = new byte[8];
            System.arraycopy(result.payload, 0, copyByte, 0, 8);
            lc.results.add(copyByte);
        } else {
            lc.results.add(result.payload);
        }
        lc.mRecordNum++;
    }
}
