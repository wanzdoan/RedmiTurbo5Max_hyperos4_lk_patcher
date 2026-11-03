package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.telephony.MiuiIccProviderException;
import com.android.internal.telephony.MiuiUsimPhoneBookManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import miui.util.FeatureParser;

/* JADX INFO: loaded from: classes.dex */
public final class MiuiAdnRecordCache extends Handler implements IccConstants {
    static final int EVENT_LOAD_ALL_ADN_LIKE_DONE = 1;
    static final int EVENT_UPDATE_ADN_DONE = 2;
    private static final String LOG_TAG = "MiuiAdnRecordCache";
    private static final int MAX_PHB_ANR_LENGTH = 20;
    private static final int MAX_PHB_NAME_LENGTH = 60;
    private static final int MAX_PHB_NUMBER_LENGTH = 40;
    private MiuiIccFileHandler mFh;
    private MiuiUsimPhoneBookManager mUsimPhoneBookManager;
    private boolean mIsAllAdnRecordsLoaded = false;
    SparseArray<ArrayList<MiuiAdnRecord>> mAdnLikeFiles = new SparseArray<>();
    SparseArray<ArrayList<Message>> mAdnLikeWaiters = new SparseArray<>();
    SparseArray<Message> mUserWriteResponse = new SparseArray<>();
    private Object mLock = new Object();

    MiuiAdnRecordCache(IccFileHandler fh) {
        this.mFh = new MiuiIccFileHandler(fh);
        this.mUsimPhoneBookManager = new MiuiUsimPhoneBookManager(this.mFh, this);
    }

    public void reset() {
        this.mAdnLikeFiles.clear();
        this.mUsimPhoneBookManager.reset();
        synchronized (this.mLock) {
            this.mIsAllAdnRecordsLoaded = false;
        }
        clearWaiters();
        clearUserWriters();
    }

    public void dispose() {
        this.mUsimPhoneBookManager.dispose();
    }

    private void clearWaiters() {
        synchronized (this.mLock) {
            int size = this.mAdnLikeWaiters.size();
            int i = 0;
            while (true) {
                SparseArray<ArrayList<Message>> sparseArray = this.mAdnLikeWaiters;
                if (i < size) {
                    ArrayList<Message> waiters = sparseArray.valueAt(i);
                    AsyncResult ar = new AsyncResult((Object) null, (Object) null, new MiuiIccProviderException(MiuiIccProviderException.ERROR_ICC_CARD_RESET));
                    notifyWaiters(waiters, ar);
                    i++;
                } else {
                    sparseArray.clear();
                }
            }
        }
    }

    private void clearUserWriters() {
        synchronized (this.mLock) {
            int size = this.mUserWriteResponse.size();
            int i = 0;
            while (true) {
                SparseArray<Message> sparseArray = this.mUserWriteResponse;
                if (i < size) {
                    sendErrorResponse(sparseArray.valueAt(i), MiuiIccProviderException.ERROR_ICC_CARD_RESET);
                    i++;
                } else {
                    sparseArray.clear();
                }
            }
        }
    }

    public ArrayList<MiuiAdnRecord> getRecordsIfLoaded(int efid) {
        return this.mAdnLikeFiles.get(efid);
    }

    public boolean isAllAdnRecordsLoaded() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsAllAdnRecordsLoaded;
        }
        return z;
    }

    public int extensionEfForEf(int efid) {
        switch (efid) {
            case 20272:
                return 0;
            case 28474:
                return 28490;
            case 28475:
                return 28491;
            case 28480:
                return 28490;
            case 28489:
                return 28492;
            case 28615:
                return 28616;
            default:
                return -1;
        }
    }

    private void sendErrorResponse(Message response, int error) {
        if (response != null) {
            Log.w(LOG_TAG, "sendErrorResponse response=" + response.what + ", error=" + error);
            AsyncResult.forMessage(response).exception = new MiuiIccProviderException(error);
            response.sendToTarget();
        }
    }

    public void updateAdnByIndex(int efid, MiuiAdnRecord adn, int recordIndex, String pin2, Message response) throws Throwable {
        int length;
        Log.d(LOG_TAG, "updateAdnByIndex efid=" + efid + ", recordIndex=" + recordIndex);
        int extensionEf = extensionEfForEf(efid);
        if (extensionEf < 0) {
            sendErrorResponse(response, MiuiIccProviderException.ERROR_UNKNOWN_EF);
            return;
        }
        if (adn.mAlphaTag.length() > 60) {
            sendErrorResponse(response, MiuiIccProviderException.ERROR_TEXT_TOO_LONG);
            return;
        }
        int length2 = adn.mNumber.length();
        if (length2 > 0 && adn.mNumber.charAt(0) == '+') {
            length = length2 - 1;
        } else {
            length = length2;
        }
        if (length > 40) {
            sendErrorResponse(response, MiuiIccProviderException.ERROR_NUMBER_TOO_LONG);
            return;
        }
        if (efid == 20272) {
            if (adn.mAnr != null && adn.mAnr.length() > 20) {
                sendErrorResponse(response, MiuiIccProviderException.ERROR_ANR_TOO_LONG);
                return;
            }
            int ret = this.mUsimPhoneBookManager.update(recordIndex, adn);
            if (ret == 0) {
                AsyncResult.forMessage(response, (Object) null, (Throwable) null);
                response.sendToTarget();
                return;
            } else {
                sendErrorResponse(response, ret);
                return;
            }
        }
        synchronized (this.mLock) {
            try {
                if (this.mUserWriteResponse.get(efid) != null) {
                    sendErrorResponse(response, MiuiIccProviderException.ERROR_NOT_READY);
                    return;
                }
                this.mUserWriteResponse.put(efid, response);
                if (efid == 28475 && (isMTKPlatform() || isXRINGPlatform())) {
                    AdnRecord oriAdn = new AdnRecordChineseCompat(efid, recordIndex, adn.getAlphaTag(), adn.getNumber());
                    new AdnRecordLoader(this.mFh.getIccFileHandler()).updateEF(oriAdn, efid, extensionEf, recordIndex, pin2, obtainMessage(2, efid, recordIndex, adn));
                } else {
                    try {
                        MiuiAdnRecordLoader.getLoader().updateEF(this.mFh, adn, efid, extensionEf, recordIndex, pin2, obtainMessage(2, efid, recordIndex, adn));
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    private boolean isMTKPlatform() {
        return "mediatek".equals(FeatureParser.getString("vendor"));
    }

    private boolean isXRINGPlatform() {
        return "xring".equals(FeatureParser.getString("vendor"));
    }

    public void updateAdnBySearch(int efid, MiuiAdnRecord oldAdn, MiuiAdnRecord newAdn, String pin2, Message response) throws Throwable {
        int index;
        Log.d(LOG_TAG, "updateAdnByIndex efid=" + efid);
        ArrayList<MiuiAdnRecord> list = loadAllAdn(efid);
        int i = MiuiIccProviderException.ERROR_ADN_LIST_NOT_EXIST;
        if (list == null) {
            sendErrorResponse(response, MiuiIccProviderException.ERROR_ADN_LIST_NOT_EXIST);
            return;
        }
        Iterator<MiuiAdnRecord> it = list.iterator();
        int count = 1;
        while (true) {
            if (!it.hasNext()) {
                index = -1;
                break;
            } else {
                if (oldAdn.isEqual(it.next())) {
                    int index2 = count;
                    index = index2;
                    break;
                }
                count++;
            }
        }
        if (index == -1) {
            if (oldAdn.isEmpty()) {
                i = MiuiIccProviderException.ERROR_STORAGE_FULL;
            }
            sendErrorResponse(response, i);
            return;
        }
        int extensionEf = extensionEfForEf(efid);
        if (extensionEf < 0) {
            sendErrorResponse(response, MiuiIccProviderException.ERROR_UNKNOWN_EF);
            return;
        }
        if (efid == 20272) {
            if (newAdn.mAlphaTag.length() > 60) {
                sendErrorResponse(response, MiuiIccProviderException.ERROR_TEXT_TOO_LONG);
                return;
            }
            int length = newAdn.mNumber.length();
            if (length > 0 && newAdn.mNumber.charAt(0) == '+') {
                length--;
            }
            if (length > 40) {
                sendErrorResponse(response, MiuiIccProviderException.ERROR_NUMBER_TOO_LONG);
                return;
            }
            if (newAdn.mAnr != null && newAdn.mAnr.length() > 20) {
                sendErrorResponse(response, MiuiIccProviderException.ERROR_ANR_TOO_LONG);
                return;
            }
            int ret = this.mUsimPhoneBookManager.update(oldAdn, index, newAdn);
            if (ret != 0) {
                sendErrorResponse(response, ret);
                return;
            } else {
                AsyncResult.forMessage(response, (Object) null, (Throwable) null);
                response.sendToTarget();
                return;
            }
        }
        updateAdnByIndex(efid, newAdn, index, pin2, response);
    }

    public void requestLoadAllAdnLike(int efid, int extensionEf, Message response) {
        ArrayList<MiuiAdnRecord> result;
        Log.d(LOG_TAG, "requestLoadAllAdnLike efid=" + efid);
        if (efid == 20272) {
            result = this.mUsimPhoneBookManager.getAdnRecordsIfLoaded();
        } else {
            result = this.mAdnLikeFiles.get(efid);
        }
        if (result != null) {
            if (response != null) {
                AsyncResult.forMessage(response).result = result;
                response.sendToTarget();
            }
            Log.d(LOG_TAG, "requestLoadAllAdnLike: already loaded, return the result directly");
            return;
        }
        if (extensionEf < 0) {
            if (response != null) {
                AsyncResult.forMessage(response).exception = new MiuiIccProviderException(MiuiIccProviderException.ERROR_UNKNOWN_EF);
                response.sendToTarget();
                return;
            }
            return;
        }
        synchronized (this.mLock) {
            ArrayList<Message> waiters = this.mAdnLikeWaiters.get(efid);
            if (waiters != null) {
                waiters.add(response);
                return;
            }
            ArrayList<Message> waiters2 = new ArrayList<>();
            waiters2.add(response);
            this.mAdnLikeWaiters.put(efid, waiters2);
            if (efid == 20272) {
                this.mUsimPhoneBookManager.requestLoadEfFilesFromUsim(obtainMessage(1, efid, 0));
            } else {
                MiuiAdnRecordLoader.getLoader().loadAllFromEF(this.mFh, efid, extensionEf, obtainMessage(1, efid, 0));
            }
        }
    }

    public int getFreeAdn(int efid) {
        ArrayList<MiuiAdnRecord> list = loadAllAdn(efid);
        if (list == null) {
            return 0;
        }
        int count = 0;
        for (MiuiAdnRecord adn : list) {
            if (adn.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public int getAdnCapacity(int efid) {
        ArrayList<MiuiAdnRecord> list = loadAllAdn(efid);
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    private void notifyWaiters(ArrayList<Message> waiters, AsyncResult ar) {
        if (waiters == null) {
            return;
        }
        int s = waiters.size();
        for (int i = 0; i < s; i++) {
            Message waiter = waiters.get(i);
            if (waiter != null) {
                AsyncResult.forMessage(waiter, ar.result, ar.exception);
                waiter.sendToTarget();
            }
        }
    }

    private ArrayList<MiuiAdnRecord> loadAllAdn(int efid) {
        if (efid == 20272) {
            ArrayList<MiuiAdnRecord> result = this.mUsimPhoneBookManager.getAdnRecordsIfLoaded();
            return result;
        }
        ArrayList<MiuiAdnRecord> result2 = this.mAdnLikeFiles.get(efid);
        return result2;
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                AsyncResult ar = (AsyncResult) msg.obj;
                int efid = msg.arg1;
                synchronized (this.mLock) {
                    ArrayList<Message> waiters = this.mAdnLikeWaiters.get(efid);
                    this.mAdnLikeWaiters.delete(efid);
                    if (ar.exception == null) {
                        ArrayList<MiuiAdnRecord> records = (ArrayList) ar.result;
                        this.mAdnLikeFiles.put(efid, records);
                        this.mIsAllAdnRecordsLoaded = true;
                    }
                    notifyWaiters(waiters, ar);
                    break;
                }
                return;
            case 2:
                AsyncResult ar2 = (AsyncResult) msg.obj;
                int efid2 = msg.arg1;
                synchronized (this.mLock) {
                    int index = msg.arg2;
                    MiuiAdnRecord adn = (MiuiAdnRecord) ar2.userObj;
                    if (ar2.exception == null) {
                        if (adn != null) {
                            adn.mRecordNumber = index;
                            if (adn.mEfid <= 0) {
                                adn.mEfid = efid2;
                            }
                        }
                        if (this.mAdnLikeFiles.get(efid2) != null) {
                            adn.setEmails(null);
                            adn.setAnr("");
                            this.mAdnLikeFiles.get(efid2).set(index - 1, adn);
                        }
                    }
                    Message response = this.mUserWriteResponse.get(efid2);
                    this.mUserWriteResponse.delete(efid2);
                    if (response != null) {
                        AsyncResult.forMessage(response, (Object) null, ar2.exception);
                        response.sendToTarget();
                    }
                    break;
                }
                return;
            default:
                return;
        }
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("MiuiAdnRecordCache: ");
        for (int i = 0; i < this.mAdnLikeFiles.size(); i++) {
            int key = this.mAdnLikeFiles.keyAt(i);
            ArrayList<MiuiAdnRecord> value = this.mAdnLikeFiles.get(key);
            pw.println(" mAdnLikeFiles[" + i + "]: efId=" + key + ", size=" + (value != null ? value.size() : 0));
        }
        this.mUsimPhoneBookManager.dump(fd, pw, args);
    }
}
