package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import android.util.SparseArray;
import com.android.internal.telephony.gsm.UsimPhoneBookManager;
import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class AdnRecordCache extends Handler implements IccConstants {
    protected static final int EVENT_LOAD_ALL_ADN_LIKE_DONE = 1;
    protected static final int EVENT_UPDATE_ADN_DONE = 2;
    protected IccFileHandler mFh;
    protected UsimPhoneBookManager mUsimPhoneBookManager;
    protected SparseArray<ArrayList<AdnRecord>> mAdnLikeFiles = new SparseArray<>();
    SparseArray mAdnLikeFilesLoadingIssues = new SparseArray();
    protected SparseArray<ArrayList<Message>> mAdnLikeWaiters = new SparseArray<>();
    protected SparseArray<Message> mUserWriteResponse = new SparseArray<>();

    public int extensionEfForEf(int i) {
        if (i == 20272) {
            return 0;
        }
        if (i == 28480) {
            return IccConstants.EF_EXT1;
        }
        if (i == 28489) {
            return IccConstants.EF_EXT3;
        }
        if (i == 28615) {
            return IccConstants.EF_EXT6;
        }
        if (i == 28474) {
            return IccConstants.EF_EXT1;
        }
        if (i != 28475) {
            return (i & 20479) == i ? 0 : -1;
        }
        return IccConstants.EF_EXT2;
    }

    public AdnRecordCache(IccFileHandler iccFileHandler) {
        this.mFh = iccFileHandler;
        this.mUsimPhoneBookManager = new UsimPhoneBookManager(iccFileHandler, this);
    }

    public AdnRecordCache(IccFileHandler iccFileHandler, UsimPhoneBookManager usimPhoneBookManager) {
        this.mFh = iccFileHandler;
        this.mUsimPhoneBookManager = usimPhoneBookManager;
    }

    public void reset() {
        this.mAdnLikeFiles.clear();
        this.mAdnLikeFilesLoadingIssues.clear();
        this.mUsimPhoneBookManager.reset();
        clearWaiters();
        clearUserWriters();
    }

    protected void clearWaiters() {
        int size = this.mAdnLikeWaiters.size();
        int i = 0;
        while (true) {
            SparseArray<ArrayList<Message>> sparseArray = this.mAdnLikeWaiters;
            if (i < size) {
                notifyWaiters(sparseArray.valueAt(i), new AsyncResult((Object) null, (Object) null, new RuntimeException("AdnCache reset")));
                i++;
            } else {
                sparseArray.clear();
                return;
            }
        }
    }

    private void clearUserWriters() {
        int size = this.mUserWriteResponse.size();
        int i = 0;
        while (true) {
            SparseArray<Message> sparseArray = this.mUserWriteResponse;
            if (i < size) {
                sendErrorResponse(sparseArray.valueAt(i), "AdnCace reset");
                i++;
            } else {
                sparseArray.clear();
                return;
            }
        }
    }

    public ArrayList<AdnRecord> getRecordsIfLoaded(int i) {
        return this.mAdnLikeFiles.get(i);
    }

    private void sendErrorResponse(Message message, String str) {
        if (message != null) {
            AsyncResult.forMessage(message).exception = new RuntimeException(str);
            message.sendToTarget();
        }
    }

    public void updateAdnByIndex(int i, AdnRecord adnRecord, int i2, String str, Message message) {
        int iExtensionEfForEf = extensionEfForEf(i);
        if (iExtensionEfForEf < 0) {
            sendErrorResponse(message, "EF is not known ADN-like EF:0x" + Integer.toHexString(i).toUpperCase(Locale.ROOT));
        } else if (this.mUserWriteResponse.get(i) != null) {
            sendErrorResponse(message, "Have pending update for EF:0x" + Integer.toHexString(i).toUpperCase(Locale.ROOT));
        } else {
            this.mUserWriteResponse.put(i, message);
            new AdnRecordLoader(this.mFh).updateEF(adnRecord, i, iExtensionEfForEf, i2, str, obtainMessage(2, i, i2, adnRecord));
        }
    }

    public void updateAdnBySearch(int i, AdnRecord adnRecord, AdnRecord adnRecord2, String str, Message message) {
        ArrayList<AdnRecord> recordsIfLoaded;
        int i2;
        int i3;
        int iExtensionEfForEf = extensionEfForEf(i);
        if (iExtensionEfForEf < 0) {
            sendErrorResponse(message, "EF is not known ADN-like EF:0x" + Integer.toHexString(i).toUpperCase(Locale.ROOT));
            return;
        }
        if (i == 20272) {
            recordsIfLoaded = this.mUsimPhoneBookManager.loadEfFilesFromUsim();
        } else {
            recordsIfLoaded = getRecordsIfLoaded(i);
        }
        if (recordsIfLoaded == null) {
            sendErrorResponse(message, "Adn list not exist for EF:0x" + Integer.toHexString(i).toUpperCase(Locale.ROOT));
            return;
        }
        int size = recordsIfLoaded.size();
        int i4 = 1;
        int i5 = 0;
        while (true) {
            if (i5 >= size) {
                i4 = -1;
                break;
            }
            AdnRecord adnRecord3 = recordsIfLoaded.get(i5);
            i5++;
            if (adnRecord.isEqual(adnRecord3)) {
                break;
            } else {
                i4++;
            }
        }
        if (i4 == -1) {
            sendErrorResponse(message, "Adn record don't exist for " + adnRecord);
            return;
        }
        if (i == 20272) {
            AdnRecord adnRecord4 = recordsIfLoaded.get(i4 - 1);
            int i6 = adnRecord4.mEfid;
            int i7 = adnRecord4.mExtRecord;
            i4 = adnRecord4.mRecordNumber;
            adnRecord2.mEfid = i6;
            adnRecord2.mExtRecord = i7;
            adnRecord2.mRecordNumber = i4;
            i2 = i7;
            i3 = i6;
        } else {
            i2 = iExtensionEfForEf;
            i3 = i;
        }
        int i8 = i4;
        if (this.mUserWriteResponse.get(i3) != null) {
            sendErrorResponse(message, "Have pending update for EF:0x" + Integer.toHexString(i3).toUpperCase(Locale.ROOT));
        } else {
            this.mUserWriteResponse.put(i3, message);
            new AdnRecordLoader(this.mFh).updateEF(adnRecord2, i3, i2, i8, str, obtainMessage(2, i3, i8, adnRecord2));
        }
    }

    public void requestLoadAllAdnLike(int i, int i2, Message message) {
        ArrayList<AdnRecord> recordsIfLoaded;
        if (i == 20272) {
            recordsIfLoaded = this.mUsimPhoneBookManager.loadEfFilesFromUsim();
        } else {
            recordsIfLoaded = getRecordsIfLoaded(i);
        }
        if (recordsIfLoaded != null) {
            if (message != null) {
                AsyncResult.forMessage(message).result = recordsIfLoaded;
                message.sendToTarget();
                return;
            }
            return;
        }
        if (this.mAdnLikeFilesLoadingIssues.indexOfKey(i) >= 0) {
            if (message != null) {
                AsyncResult.forMessage(message).exception = (Throwable) this.mAdnLikeFilesLoadingIssues.get(i);
                Rlog.e("AdnRecordCache", "Error(cached) loading ADN records for efid: " + i);
                message.sendToTarget();
                return;
            }
            return;
        }
        ArrayList<Message> arrayList = this.mAdnLikeWaiters.get(i);
        if (arrayList != null) {
            arrayList.add(message);
            return;
        }
        ArrayList<Message> arrayList2 = new ArrayList<>();
        arrayList2.add(message);
        this.mAdnLikeWaiters.put(i, arrayList2);
        if (i2 >= 0) {
            new AdnRecordLoader(this.mFh).loadAllFromEF(i, i2, obtainMessage(1, i, 0));
            return;
        }
        if (message != null) {
            AsyncResult.forMessage(message).exception = new RuntimeException("EF is not known ADN-like EF:0x" + Integer.toHexString(i).toUpperCase(Locale.ROOT));
            message.sendToTarget();
        }
    }

    protected void notifyWaiters(ArrayList<Message> arrayList, AsyncResult asyncResult) {
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Message message = arrayList.get(i);
            AsyncResult.forMessage(message, asyncResult.result, asyncResult.exception);
            message.sendToTarget();
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            int i2 = message.arg1;
            ArrayList<Message> arrayList = this.mAdnLikeWaiters.get(i2);
            this.mAdnLikeWaiters.delete(i2);
            Throwable th = asyncResult.exception;
            if (th == null) {
                this.mAdnLikeFiles.put(i2, (ArrayList) asyncResult.result);
            } else {
                this.mAdnLikeFilesLoadingIssues.put(i2, th);
            }
            notifyWaiters(arrayList, asyncResult);
            return;
        }
        if (i != 2) {
            return;
        }
        AsyncResult asyncResult2 = (AsyncResult) message.obj;
        int i3 = message.arg1;
        int i4 = message.arg2;
        AdnRecord adnRecord = (AdnRecord) asyncResult2.userObj;
        if (asyncResult2.exception == null) {
            this.mAdnLikeFiles.get(i3).set(i4 - 1, adnRecord);
            this.mUsimPhoneBookManager.invalidateCache();
        }
        Message message2 = this.mUserWriteResponse.get(i3);
        this.mUserWriteResponse.delete(i3);
        if (message2 != null) {
            AsyncResult.forMessage(message2, (Object) null, asyncResult2.exception);
            message2.sendToTarget();
        }
    }

    protected void setAdnLikeWriters(int i, ArrayList<Message> arrayList) {
        this.mAdnLikeWaiters.put(IccConstants.EF_MBDN, arrayList);
    }

    protected void setAdnLikeFiles(int i, ArrayList<AdnRecord> arrayList) {
        this.mAdnLikeFiles.put(IccConstants.EF_MBDN, arrayList);
    }

    protected void setUserWriteResponse(int i, Message message) {
        this.mUserWriteResponse.put(IccConstants.EF_MBDN, message);
    }

    protected UsimPhoneBookManager getUsimPhoneBookManager() {
        return this.mUsimPhoneBookManager;
    }
}
