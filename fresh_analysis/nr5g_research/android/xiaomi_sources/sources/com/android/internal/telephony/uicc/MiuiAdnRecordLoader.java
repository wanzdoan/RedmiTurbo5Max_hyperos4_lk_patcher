package com.android.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.internal.telephony.MiuiIccProviderException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MiuiAdnRecordLoader extends Handler {
    private static final int EVENT_ADN_LOAD_DONE = 2;
    private static final int EVENT_ADN_SIZE_LOAD_DONE = 1;
    private static final int EVENT_EXT_RECORD_LOAD_DONE = 4;
    private static final int EVENT_EXT_SIZE_LOAD_DONE = 3;
    static final String LOG_TAG = AdnRecordLoader.class.getSimpleName();

    private static class Context {
        private static final MiuiAdnRecordLoader LOADER = new MiuiAdnRecordLoader();
        static final int LOAD_ALL = 1;
        static final int LOAD_SINGLE = 0;
        static final int UPDATE_SINGLE = 2;
        MiuiAdnRecord adn;
        final int efid;
        final int extensionEfid;
        int extentionRecordSize;
        final MiuiIccFileHandler iccFile;
        ArrayList<MiuiAdnRecord> loadedAdns;
        final Message onLoaded;
        final String pin2;
        int recordIndex;
        Object result;
        final int type;

        Context(MiuiIccFileHandler fh, int efid, int extensionEfid, int recordIndex, Message response) {
            this.iccFile = fh;
            this.efid = efid;
            this.extensionEfid = extensionEfid;
            this.onLoaded = response;
            this.pin2 = null;
            this.type = 0;
            this.recordIndex = recordIndex;
        }

        Context(MiuiIccFileHandler fh, int efid, int extensionEfid, Message response) {
            this.iccFile = fh;
            this.efid = efid;
            this.extensionEfid = extensionEfid;
            this.onLoaded = response;
            this.pin2 = null;
            this.type = 1;
        }

        Context(MiuiIccFileHandler fh, int efid, int extensionEfid, int recordIndex, MiuiAdnRecord adn, String pin2, Message response) {
            this.iccFile = fh;
            this.efid = efid;
            this.extensionEfid = extensionEfid;
            this.onLoaded = response;
            this.pin2 = pin2;
            this.type = 2;
            this.recordIndex = recordIndex;
            this.adn = adn;
        }
    }

    private MiuiAdnRecordLoader() {
        super(Looper.getMainLooper());
    }

    public static MiuiAdnRecordLoader getLoader() {
        return Context.LOADER;
    }

    public void loadFromEF(MiuiIccFileHandler iccFile, int efid, int extensionEfid, int recordIndex, Message response) {
        Context context = new Context(iccFile, efid, extensionEfid, recordIndex, response);
        context.iccFile.getEFLinearRecordSize(efid, obtainMessage(1, context));
    }

    public void loadAllFromEF(MiuiIccFileHandler iccFile, int efid, int extensionEfid, Message response) {
        Context context = new Context(iccFile, efid, extensionEfid, response);
        context.iccFile.getEFLinearRecordSize(efid, obtainMessage(1, context));
    }

    public void updateEF(MiuiIccFileHandler iccFile, MiuiAdnRecord adn, int efid, int extensionEfid, int recordIndex, String pin2, Message response) {
        Context context = new Context(iccFile, efid, extensionEfid, recordIndex, adn, pin2, response);
        context.iccFile.getEFLinearRecordSize(efid, obtainMessage(1, context));
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        AsyncResult ar = (AsyncResult) msg.obj;
        Context context = (Context) ar.userObj;
        switch (msg.what) {
            case 1:
                if (ar.exception != null) {
                    context.result = ar.exception;
                    break;
                } else {
                    int[] recordSize = (int[]) ar.result;
                    if (recordSize.length != 3) {
                        context.result = new MiuiIccProviderException(MiuiIccProviderException.ERROR_UNKNOWN);
                        break;
                    } else {
                        switch (context.type) {
                            case 0:
                                context.iccFile.loadEFLinearFixed(context.efid, recordSize[0], context.recordIndex, obtainMessage(2, context));
                                break;
                            case 1:
                                context.loadedAdns = new ArrayList<>(recordSize[2]);
                                context.iccFile.loadEFLinearFixedAll(context.efid, recordSize[0], recordSize[2], obtainMessage(2, context));
                                break;
                            case 2:
                                MiuiAdnRecord.BuildAdnResult buildAdnResult = context.adn.buildAdnString(recordSize[0]);
                                if (buildAdnResult.result != 0) {
                                    context.result = new MiuiIccProviderException(buildAdnResult.result);
                                } else {
                                    context.iccFile.updateEFLinearFixed(context.efid, context.recordIndex, buildAdnResult.adnStr, context.pin2, obtainMessage(2, context));
                                }
                                break;
                        }
                    }
                }
                break;
            case 2:
                if (ar.exception != null) {
                    context.result = ar.exception;
                    break;
                } else {
                    switch (context.type) {
                        case 0:
                            if (ar.result == null) {
                                context.result = new MiuiIccProviderException(MiuiIccProviderException.ERROR_UNKNOWN);
                            } else {
                                context.adn = new MiuiAdnRecord(context.efid, context.recordIndex, (byte[]) ar.result);
                                if (context.adn.hasExtendedRecord()) {
                                    context.iccFile.getEFLinearRecordSize(context.extensionEfid, obtainMessage(3, context));
                                } else {
                                    context.result = context.adn;
                                }
                            }
                            break;
                        case 1:
                            if (ar.result == null) {
                                context.result = new MiuiIccProviderException(MiuiIccProviderException.ERROR_UNKNOWN);
                            } else {
                                List<byte[]> datas = (List) ar.result;
                                boolean hasExtendedRecord = false;
                                int s = datas.size();
                                for (int i = 0; i < s; i++) {
                                    MiuiAdnRecord adn = new MiuiAdnRecord(context.efid, i + 1, datas.get(i));
                                    context.loadedAdns.add(adn);
                                    if (!hasExtendedRecord && adn.hasExtendedRecord()) {
                                        hasExtendedRecord = true;
                                    }
                                }
                                if (hasExtendedRecord) {
                                    context.iccFile.getEFLinearRecordSize(context.extensionEfid, obtainMessage(3, context));
                                } else {
                                    context.result = context.loadedAdns;
                                }
                            }
                            break;
                        case 2:
                            context.result = context;
                            break;
                    }
                }
                break;
            case 3:
                if (ar.exception != null) {
                    context.result = ar.exception;
                    break;
                } else {
                    int[] recordSize2 = (int[]) ar.result;
                    if (recordSize2.length != 3) {
                        context.result = new MiuiIccProviderException(MiuiIccProviderException.ERROR_UNKNOWN);
                        break;
                    } else {
                        context.extentionRecordSize = recordSize2[0];
                        switch (context.type) {
                            case 0:
                                context.iccFile.loadEFLinearFixed(context.extensionEfid, recordSize2[0], context.adn.mExtRecord, obtainMessage(4, context));
                                break;
                            case 1:
                                context.recordIndex = 0;
                                int l = context.loadedAdns.size();
                                for (int i2 = context.recordIndex; i2 < l; i2++) {
                                    MiuiAdnRecord adn2 = context.loadedAdns.get(i2);
                                    if (adn2.hasExtendedRecord()) {
                                        context.iccFile.loadEFLinearFixed(context.extensionEfid, recordSize2[0], adn2.mExtRecord, obtainMessage(4, adn2.mRecordNumber - 1, 0, context));
                                        context.recordIndex = i2;
                                    }
                                    break;
                                }
                                break;
                        }
                    }
                }
                break;
            case 4:
                if (ar.exception != null) {
                    context.result = ar.exception;
                    break;
                } else if (ar.result == null) {
                    context.result = new MiuiIccProviderException(MiuiIccProviderException.ERROR_UNKNOWN);
                    break;
                } else {
                    byte[] data = (byte[]) ar.result;
                    switch (context.type) {
                        case 0:
                            context.adn.appendExtRecord(data);
                            context.result = context.adn;
                            break;
                        case 1:
                            context.loadedAdns.get(context.recordIndex).appendExtRecord(data);
                            context.result = context.loadedAdns;
                            int l2 = context.loadedAdns.size();
                            for (int i3 = context.recordIndex + 1; i3 < l2; i3++) {
                                MiuiAdnRecord adn3 = context.loadedAdns.get(i3);
                                if (adn3.hasExtendedRecord()) {
                                    context.iccFile.loadEFLinearFixed(context.extensionEfid, context.extentionRecordSize, adn3.mExtRecord, obtainMessage(4, adn3.mRecordNumber - 1, 0, context));
                                    context.recordIndex = i3;
                                    context.result = null;
                                }
                                break;
                            }
                            break;
                    }
                }
                break;
        }
        if (context.result != null) {
            if (context.result instanceof Throwable) {
                AsyncResult.forMessage(context.onLoaded).exception = (Throwable) context.result;
            } else {
                AsyncResult.forMessage(context.onLoaded).result = context.result != context ? context.result : null;
            }
            context.onLoaded.sendToTarget();
        }
    }
}
