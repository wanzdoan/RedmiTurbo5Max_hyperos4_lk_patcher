package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class MiuiIccFileHandler extends Handler {
    private static final int EVENT_GET_RECORD_SIZE = 1;
    private IccFileHandler mFh;
    private final HashMap<String, SparseArray<int[]>> mRecordSizes;

    public MiuiIccFileHandler(IccFileHandler fh) {
        super(fh.getLooper());
        this.mRecordSizes = new HashMap<>();
        this.mFh = fh;
    }

    private void loadEFLinearFixed(int recordSize, int recordCount, IccFileHandler.LoadLinearFixedContext lc) {
        if (!lc.mLoadAll && lc.mRecordNum > 0) {
            lc.mRecordSize = recordSize;
        } else if (lc.mLoadAll) {
            lc.mRecordSize = recordSize;
            lc.mCountRecords = recordCount;
            lc.results = new ArrayList(recordCount);
        }
        this.mFh.mCi.iccIOForApp(178, lc.mEfid, lc.mPath, lc.mRecordNum, 4, lc.mRecordSize, (String) null, (String) null, this.mFh.mAid, this.mFh.obtainMessage(7, lc));
    }

    private void loadEFLinearFixed(IccFileHandler.LoadLinearFixedContext lc) {
        int[] recordSize;
        SparseArray<int[]> sa = this.mRecordSizes.get(lc.mPath);
        if (sa == null) {
            this.mRecordSizes.put(lc.mPath, new SparseArray<>());
            recordSize = null;
        } else {
            recordSize = sa.get(lc.mEfid);
        }
        if (recordSize == null) {
            this.mFh.getEFLinearRecordSize(lc.mEfid, lc.mPath, obtainMessage(1, lc));
        } else {
            loadEFLinearFixed(recordSize[0], recordSize[2], lc);
        }
    }

    public void loadEFLinearFixed(int fileid, int recordNum, Message onLoaded) {
        loadEFLinearFixed(fileid, (String) null, recordNum, onLoaded);
    }

    public void loadEFLinearFixed(int fileid, String path, int recordNum, Message onLoaded) {
        if (path == null) {
            path = getEFPath(fileid);
        }
        IccFileHandler.LoadLinearFixedContext lc = new IccFileHandler.LoadLinearFixedContext(fileid, recordNum, path, onLoaded);
        loadEFLinearFixed(lc);
    }

    public void loadEFLinearFixed(int fileid, int recordSize, int recordNum, Message onLoaded) {
        loadEFLinearFixed(fileid, null, recordSize, recordNum, onLoaded);
    }

    public void loadEFLinearFixed(int fileid, String path, int recordSize, int recordNum, Message onLoaded) {
        if (path == null) {
            path = getEFPath(fileid);
        }
        IccFileHandler.LoadLinearFixedContext lc = new IccFileHandler.LoadLinearFixedContext(fileid, recordNum, path, onLoaded);
        loadEFLinearFixed(recordSize, 0, lc);
    }

    public void loadEFLinearFixedAll(int fileid, Message onLoaded) {
        loadEFLinearFixedAll(fileid, null, onLoaded);
    }

    public void loadEFLinearFixedAll(int fileid, String path, Message onLoaded) {
        if (path == null) {
            path = getEFPath(fileid);
        }
        IccFileHandler.LoadLinearFixedContext lc = new IccFileHandler.LoadLinearFixedContext(fileid, path, onLoaded);
        loadEFLinearFixed(lc);
    }

    public void loadEFLinearFixedAll(int fileid, int recordSize, int recordCount, Message onLoaded) {
        loadEFLinearFixedAll(fileid, null, recordSize, recordCount, onLoaded);
    }

    public void loadEFLinearFixedAll(int fileid, String path, int recordSize, int recordCount, Message onLoaded) {
        if (path == null) {
            path = getEFPath(fileid);
        }
        IccFileHandler.LoadLinearFixedContext lc = new IccFileHandler.LoadLinearFixedContext(fileid, path, onLoaded);
        loadEFLinearFixed(recordSize, recordCount, lc);
    }

    public void loadEFImgLinearFixed(int recordNum, Message onLoaded) {
        this.mFh.loadEFImgLinearFixed(recordNum, onLoaded);
    }

    public void getEFLinearRecordSize(int fileid, Message onLoaded) {
        getEFLinearRecordSize(fileid, null, onLoaded);
    }

    public void getEFLinearRecordSize(int fileid, String path, Message onLoaded) {
        if (path == null) {
            path = getEFPath(fileid);
        }
        SparseArray<int[]> sa = this.mRecordSizes.get(path);
        if (sa != null) {
            int[] result = sa.get(fileid);
            if (result != null) {
                AsyncResult.forMessage(onLoaded).result = result;
                onLoaded.sendToTarget();
                return;
            }
        } else {
            this.mRecordSizes.put(path, new SparseArray<>());
        }
        IccFileHandler.LoadLinearFixedContext lc = new IccFileHandler.LoadLinearFixedContext(fileid, (String) null, onLoaded);
        lc.mPath = path;
        lc.mRecordNum = -1;
        lc.mLoadAll = false;
        this.mFh.getEFLinearRecordSize(fileid, path, obtainMessage(1, lc));
    }

    public void loadEFTransparent(int fileid, String path, int offset, int size, Message onLoaded) {
        String path2;
        if (path != null) {
            path2 = path;
        } else {
            path2 = getEFPath(fileid);
        }
        Message response = this.mFh.obtainMessage(5, fileid, 0, onLoaded);
        this.mFh.mCi.iccIOForApp(176, fileid, path2, 0, offset, size, (String) null, (String) null, this.mFh.mAid, response);
    }

    public void loadEFImgTransparent(int fileid, int highOffset, int lowOffset, int length, Message onLoaded) {
        this.mFh.loadEFImgTransparent(fileid, highOffset, lowOffset, length, onLoaded);
    }

    public void updateEFLinearFixed(int fileid, int recordNum, byte[] data, String pin2, Message onComplete) {
        updateEFLinearFixed(fileid, null, recordNum, data, pin2, onComplete);
    }

    public void updateEFLinearFixed(int fileid, String path, int recordNum, byte[] data, String pin2, Message onComplete) {
        String path2;
        if (path != null) {
            path2 = path;
        } else {
            path2 = getEFPath(fileid);
        }
        this.mFh.updateEFLinearFixed(fileid, path2, recordNum, data, pin2, onComplete);
    }

    public void updateEFTransparent(int fileid, byte[] data, Message onComplete) {
        this.mFh.updateEFTransparent(fileid, data, onComplete);
    }

    public IccFileHandler getIccFileHandler() {
        return this.mFh;
    }

    private String getEFPath(int fileid) {
        IccFileHandler iccFileHandler = this.mFh;
        if (fileid == 28474) {
            return iccFileHandler.getCommonIccEFPath(fileid);
        }
        return iccFileHandler.getEFPath(fileid);
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        AsyncResult ar = (AsyncResult) msg.obj;
        IccFileHandler.LoadLinearFixedContext lc = (IccFileHandler.LoadLinearFixedContext) ar.userObj;
        switch (msg.what) {
            case 1:
                if (ar.exception != null || ar.result == null) {
                    ar.userObj = lc.mOnLoaded.obj;
                    lc.mOnLoaded.obj = ar;
                    lc.mOnLoaded.sendToTarget();
                } else {
                    int[] result = (int[]) ar.result;
                    this.mRecordSizes.get(lc.mPath).put(lc.mEfid, result);
                    if (!lc.mLoadAll && lc.mRecordNum < 0) {
                        ar.userObj = lc.mOnLoaded.obj;
                        lc.mOnLoaded.obj = ar;
                        lc.mOnLoaded.sendToTarget();
                    } else {
                        loadEFLinearFixed(result[0], result[2], lc);
                    }
                }
                break;
        }
        super.handleMessage(msg);
    }
}
