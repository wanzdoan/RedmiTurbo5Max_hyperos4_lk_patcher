package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Message;
import android.os.UserHandle;
import android.telephony.SubscriptionManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.gsm.SimTlv;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class IsimUiccRecords extends IccRecords implements IsimRecords {
    protected static final boolean DUMP_RECORDS = false;
    public static final String INTENT_ISIM_REFRESH = "com.android.intent.isim_refresh";
    protected static final String LOG_TAG = "IsimUiccRecords";
    private static final boolean VDBG = Rlog.isLoggable(LOG_TAG, 2);
    private String auth_rsp;
    private final FeatureFlags mFeatureFlags;
    private String mIsimDomain;
    private String[] mIsimIari;
    private String mIsimImpi;
    private String[] mIsimImpu;
    protected String mIsimIst;
    private String[] mIsimPcscf;

    @Override // com.android.internal.telephony.uicc.IccRecords
    public int getVoiceMessageCount() {
        return 0;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void setVoiceMailNumber(String str, String str2, Message message) {
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void setVoiceMessageWaiting(int i, int i2) {
    }

    @Override // com.android.internal.telephony.uicc.IccRecords, android.os.Handler
    public String toString() {
        return "IsimUiccRecords: " + super.toString() + "";
    }

    public IsimUiccRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        super(uiccCardApplication, context, commandsInterface);
        this.mFeatureFlags = featureFlags;
        this.mAdnCache = TelephonyComponentFactory.getInstance().inject(AdnRecordCache.class.getName()).makeAdnRecordCache(this.mFh);
        this.mRecordsRequested = false;
        this.mLockedRecordsReqReason = 0;
        this.mRecordsToLoad = 0;
        resetRecords();
        log("IsimUiccRecords X ctor this=" + this);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void dispose() {
        log("Disposing " + this);
        resetRecords();
        super.dispose();
    }

    @Override // com.android.internal.telephony.uicc.IccRecords, android.os.Handler
    public void handleMessage(Message message) {
        if (this.mDestroyed.get()) {
            Rlog.e(LOG_TAG, "Received message " + message + "[" + message.what + "] while being destroyed. Ignoring.");
            return;
        }
        loge("IsimUiccRecords: handleMessage " + message + "[" + message.what + "] ");
        try {
            if (message.what == 31) {
                broadcastRefresh();
                super.handleMessage(message);
            } else {
                super.handleMessage(message);
            }
        } catch (RuntimeException e) {
            Rlog.w(LOG_TAG, "Exception parsing SIM record", e);
        }
    }

    protected void fetchIsimRecords() {
        this.mRecordsRequested = true;
        log("fetchIsimRecords " + this.mRecordsToLoad);
        this.mFh.loadEFTransparent(IccConstants.EF_IMPI, obtainMessage(100, new EfIsimImpiLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixedAll(IccConstants.EF_IMPU, obtainMessage(100, new EfIsimImpuLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_DOMAIN, obtainMessage(100, new EfIsimDomainLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(IccConstants.EF_IST, obtainMessage(100, createEfIsimIstLoaded()));
        this.mRecordsToLoad++;
        fetchPcscfRecord();
        this.mFh.loadEFTransparent(IccConstants.EF_SMSS, obtainMessage(100, new EfIsimSmssLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixed(IccConstants.EF_PSISMSC, 1, obtainMessage(100, new EfIsimPsiSmscLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixedAll(IccConstants.EF_IARI, obtainMessage(100, new EfIsimIariLoaded()));
        this.mRecordsToLoad++;
        log("fetchIsimRecords " + this.mRecordsToLoad + " requested: " + this.mRecordsRequested);
    }

    protected void resetRecords() {
        this.mIsimImpi = null;
        this.mIsimDomain = null;
        this.mIsimImpu = null;
        this.mIsimIst = null;
        this.mIsimPcscf = null;
        this.auth_rsp = null;
        this.mIsimIari = null;
        this.mRecordsRequested = false;
        this.mLockedRecordsReqReason = 0;
        this.mLoaded.set(false);
    }

    protected void fetchPcscfRecord() {
        this.mFh.loadEFLinearFixedAll(IccConstants.EF_PCSCF, obtainMessage(100, new EfIsimPcscfLoaded()));
        this.mRecordsToLoad++;
    }

    protected IccRecords.IccRecordLoaded createEfIsimIstLoaded() {
        return new EfIsimIstLoaded();
    }

    private class EfIsimImpiLoaded implements IccRecords.IccRecordLoaded {
        private EfIsimImpiLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_ISIM_IMPI";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            if (asyncResult.exception != null) {
                IsimUiccRecords.this.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            IsimUiccRecords.this.mIsimImpi = IsimUiccRecords.isimTlvToString((byte[]) asyncResult.result);
        }
    }

    private class EfIsimImpuLoaded implements IccRecords.IccRecordLoaded {
        private EfIsimImpuLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_ISIM_IMPU";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            if (asyncResult.exception != null) {
                IsimUiccRecords.this.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            ArrayList arrayList = (ArrayList) asyncResult.result;
            IsimUiccRecords.this.log("EF_IMPU record count: " + arrayList.size());
            IsimUiccRecords.this.mIsimImpu = new String[arrayList.size()];
            int size = arrayList.size();
            int i = 0;
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                IsimUiccRecords.this.mIsimImpu[i] = IsimUiccRecords.isimTlvToString((byte[]) obj);
                i++;
            }
        }
    }

    private class EfIsimDomainLoaded implements IccRecords.IccRecordLoaded {
        private EfIsimDomainLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_ISIM_DOMAIN";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            if (asyncResult.exception != null) {
                IsimUiccRecords.this.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            IsimUiccRecords.this.mIsimDomain = IsimUiccRecords.isimTlvToString((byte[]) asyncResult.result);
        }
    }

    private class EfIsimIstLoaded implements IccRecords.IccRecordLoaded {
        private EfIsimIstLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_ISIM_IST";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            if (asyncResult.exception != null) {
                IsimUiccRecords.this.loge("Record Load Exception: " + asyncResult.exception);
                return;
            }
            IsimUiccRecords.this.mIsimIst = IccUtils.bytesToHexString((byte[]) asyncResult.result);
        }
    }

    public EfIsimIstLoaded getIsimIstObject() {
        return new EfIsimIstLoaded();
    }

    private class EfIsimSmssLoaded implements IccRecords.IccRecordLoaded {
        private EfIsimSmssLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_ISIM_SMSS";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            IsimUiccRecords.this.mSmssValues = (byte[]) asyncResult.result;
            if (IsimUiccRecords.VDBG) {
                IsimUiccRecords.this.log("IsimUiccRecords - EF_SMSS TPMR value = " + IsimUiccRecords.this.getSmssTpmrValue());
            }
        }
    }

    public class EfIsimPcscfLoaded implements IccRecords.IccRecordLoaded {
        public EfIsimPcscfLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_ISIM_PCSCF";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            ArrayList arrayList = (ArrayList) asyncResult.result;
            IsimUiccRecords.this.log("EF_PCSCF record count: " + arrayList.size());
            IsimUiccRecords.this.mIsimPcscf = new String[arrayList.size()];
            int size = arrayList.size();
            int i = 0;
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                IsimUiccRecords.this.mIsimPcscf[i] = IsimUiccRecords.isimTlvToString((byte[]) obj);
                i++;
            }
        }
    }

    private class EfIsimPsiSmscLoaded implements IccRecords.IccRecordLoaded {
        private EfIsimPsiSmscLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_ISIM_PSISMSC";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            byte[] bArr = (byte[]) asyncResult.result;
            if (bArr == null || bArr.length <= 0) {
                return;
            }
            IsimUiccRecords isimUiccRecords = IsimUiccRecords.this;
            isimUiccRecords.mPsiSmsc = isimUiccRecords.parseEfPsiSmsc(bArr);
            if (IsimUiccRecords.VDBG) {
                IsimUiccRecords.this.log("IsimUiccRecords - EF_PSISMSC value = " + IsimUiccRecords.this.mPsiSmsc);
            }
        }
    }

    public EfIsimIariLoaded getIsimIariObject() {
        return new EfIsimIariLoaded();
    }

    private class EfIsimIariLoaded implements IccRecords.IccRecordLoaded {
        private EfIsimIariLoaded() {
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public String getEfName() {
            return "EF_ISIM_IARI";
        }

        @Override // com.android.internal.telephony.uicc.IccRecords.IccRecordLoaded
        public void onRecordLoaded(AsyncResult asyncResult) {
            ArrayList arrayList = (ArrayList) asyncResult.result;
            if (arrayList == null || arrayList.size() <= 0) {
                return;
            }
            IsimUiccRecords.this.log("EF_IARI record count: " + arrayList.size());
            IsimUiccRecords.this.mIsimIari = new String[arrayList.size()];
            int size = arrayList.size();
            int i = 0;
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                IsimUiccRecords.this.mIsimIari[i] = IsimUiccRecords.isimTlvToString((byte[]) obj);
                i++;
            }
        }
    }

    public EfIsimPsiSmscLoaded getPsiSmscObject() {
        return new EfIsimPsiSmscLoaded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String isimTlvToString(byte[] bArr) {
        SimTlv simTlv = new SimTlv(bArr, 0, bArr.length);
        while (simTlv.getTag() != 128) {
            if (!simTlv.nextObject()) {
                if (!VDBG) {
                    return null;
                }
                Rlog.d(LOG_TAG, "[ISIM] can't find TLV. record = " + IccUtils.bytesToHexString(bArr));
                return null;
            }
        }
        return new String(simTlv.getData(), Charset.forName("UTF-8"));
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onRecordLoaded() {
        this.mRecordsToLoad--;
        log("onRecordLoaded " + this.mRecordsToLoad + " requested: " + this.mRecordsRequested);
        if (getEssentialRecordsLoaded() && !this.mEssentialRecordsListenerNotified) {
            onAllEssentialRecordsLoaded();
        }
        if (getRecordsLoaded()) {
            onAllRecordsLoaded();
            return;
        }
        if (getLockedRecordsLoaded() || getNetworkLockedRecordsLoaded()) {
            onLockedAllRecordsLoaded();
        } else if (this.mRecordsToLoad < 0 || this.mEssentialRecordsToLoad < 0) {
            loge("recordsToLoad <0, programmer error suspected");
            this.mRecordsToLoad = 0;
            this.mEssentialRecordsToLoad = 0;
        }
    }

    private void onLockedAllRecordsLoaded() {
        log("SIM locked; record load complete");
        int i = this.mLockedRecordsReqReason;
        if (i == 1) {
            this.mLockedRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        } else {
            if (i == 2) {
                this.mNetworkLockedRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
                return;
            }
            loge("onLockedAllRecordsLoaded: unexpected mLockedRecordsReqReason " + this.mLockedRecordsReqReason);
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onAllEssentialRecordsLoaded() {
        log("Essential record load complete");
        this.mEssentialRecordsListenerNotified = true;
        this.mEssentialRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void onAllRecordsLoaded() {
        log("record load complete");
        this.mLoaded.set(true);
        this.mRecordsLoadedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void handleFileUpdate(int i) {
        if (i == 28423) {
            this.mFh.loadEFTransparent(IccConstants.EF_IST, obtainMessage(100, new EfIsimIstLoaded()));
            this.mRecordsToLoad++;
            return;
        }
        if (i != 28425) {
            switch (i) {
                case IccConstants.EF_IMPI /* 28418 */:
                    this.mFh.loadEFTransparent(IccConstants.EF_IMPI, obtainMessage(100, new EfIsimImpiLoaded()));
                    this.mRecordsToLoad++;
                    break;
                case IccConstants.EF_DOMAIN /* 28419 */:
                    this.mFh.loadEFTransparent(IccConstants.EF_DOMAIN, obtainMessage(100, new EfIsimDomainLoaded()));
                    this.mRecordsToLoad++;
                    break;
                case IccConstants.EF_IMPU /* 28420 */:
                    this.mFh.loadEFLinearFixedAll(IccConstants.EF_IMPU, obtainMessage(100, new EfIsimImpuLoaded()));
                    this.mRecordsToLoad++;
                    break;
            }
            return;
        }
        this.mFh.loadEFLinearFixedAll(IccConstants.EF_PCSCF, obtainMessage(100, new EfIsimPcscfLoaded()));
        this.mRecordsToLoad++;
        this.mLoaded.set(false);
        fetchIsimRecords();
    }

    private void broadcastRefresh() {
        Intent intent = new Intent(INTENT_ISIM_REFRESH);
        log("send ISim REFRESH: com.android.intent.isim_refresh");
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mParentApp.getPhoneId());
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    @Override // com.android.internal.telephony.uicc.IsimRecords
    public String getIsimImpi() {
        return this.mIsimImpi;
    }

    @Override // com.android.internal.telephony.uicc.IsimRecords
    public String getIsimDomain() {
        return this.mIsimDomain;
    }

    @Override // com.android.internal.telephony.uicc.IsimRecords
    public String[] getIsimImpu() {
        String[] strArr = this.mIsimImpu;
        if (strArr != null) {
            return (String[]) strArr.clone();
        }
        return null;
    }

    @Override // com.android.internal.telephony.uicc.IsimRecords
    public String getIsimIst() {
        return this.mIsimIst;
    }

    @Override // com.android.internal.telephony.uicc.IsimRecords
    public String[] getIsimPcscf() {
        String[] strArr = this.mIsimPcscf;
        if (strArr != null) {
            return (String[]) strArr.clone();
        }
        return null;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void onReady() {
        fetchIsimRecords();
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void onRefresh(boolean z, int[] iArr) {
        if (z) {
            fetchIsimRecords();
        }
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public String[] getUiccIari() {
        String[] strArr = this.mIsimIari;
        if (strArr != null) {
            return (String[]) strArr.clone();
        }
        return null;
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void log(String str) {
        if (this.mParentApp != null) {
            Rlog.d(LOG_TAG, "[ISIM-" + this.mParentApp.getPhoneId() + "] " + str);
            return;
        }
        Rlog.d(LOG_TAG, "[ISIM] " + str);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    protected void loge(String str) {
        if (this.mParentApp != null) {
            Rlog.e(LOG_TAG, "[ISIM-" + this.mParentApp.getPhoneId() + "] " + str);
            return;
        }
        Rlog.e(LOG_TAG, "[ISIM] " + str);
    }

    @Override // com.android.internal.telephony.uicc.IccRecords
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("IsimRecords: " + this);
        printWriter.println(" extends:");
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println(" mIsimServiceTable=" + getIsimServiceTable());
        printWriter.flush();
    }

    private IsimServiceTable getIsimServiceTable() {
        String str = this.mIsimIst;
        if (str != null) {
            return new IsimServiceTable(IccUtils.hexStringToBytes(str));
        }
        return null;
    }
}
