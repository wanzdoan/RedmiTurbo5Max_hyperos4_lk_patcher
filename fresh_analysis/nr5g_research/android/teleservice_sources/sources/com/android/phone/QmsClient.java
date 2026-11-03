package com.android.phone;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.IHwBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.qms.DataStallParamS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.Nr5gNflEntryTypeS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;
import miui.telephony.qms.PreferredCellListS;
import vendor.xiaomi.modem.qms.V1_0.CellInfoS;
import vendor.xiaomi.modem.qms.V1_0.DataRecommendationS;
import vendor.xiaomi.modem.qms.V1_0.DubiousCellInfoS;
import vendor.xiaomi.modem.qms.V1_0.IMiQms;
import vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse;
import vendor.xiaomi.modem.qms.V1_0.LocationS;
import vendor.xiaomi.modem.qms.V1_0.PreferredCellInfoS;
import vendor.xiaomi.modem.qms.V1_0.SubmodeInfoS;

/* JADX INFO: loaded from: classes.dex */
public class QmsClient extends QmsClientBase {
    private static final String TAG = "QmsClient";
    ContentObserver mDualDataEnabledObserver;
    private boolean mHasRegisterDualData;
    private IMiQms mIMiQms;
    ContentObserver mQmsCloudObserver;
    private boolean mQmsReged;
    private Message mRegClntDoneMsg;
    private long mServiceCookie;
    private boolean mUserDualDataEnabled;
    private ConcurrentHashMap mTransactionMessageMap = new ConcurrentHashMap();
    private QmsDeathRecipient mQmsDeathRecipient = new QmsDeathRecipient();
    private AtomicInteger mRetryCount = new AtomicInteger();
    private Handler mHandler = new Handler() { // from class: com.android.phone.QmsClient.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 100 && ((Long) message.obj).longValue() == QmsClient.this.mServiceCookie) {
                QmsClient.this.mQmsReged = false;
                QmsClient.log("retry to register Qms client:" + QmsClient.this.mRetryCount.get());
                QmsClient.this.registerQmsClient(null);
            }
        }
    };
    private List mDppdInterfaceListenerList = new ArrayList();
    private List mHstStateChangedListenerList = new ArrayList();

    public static QmsClientBase make() {
        if (QmsClientBase.mInstance == null) {
            QmsClientBase.mInstance = new QmsClient();
        }
        return QmsClientBase.mInstance;
    }

    private QmsClient() {
        Handler handler = null;
        this.mQmsCloudObserver = new ContentObserver(handler) { // from class: com.android.phone.QmsClient.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean zIsQmsSupported = TelephonyManagerEx.getDefault().isQmsSupported();
                QmsClient.log("isQmsSupported=" + zIsQmsSupported);
                QmsClient qmsClient = QmsClient.this;
                if (zIsQmsSupported) {
                    qmsClient.mQmsSwitchCloud = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 1) == 1;
                } else {
                    qmsClient.mQmsSwitchCloud = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 0) == 1;
                }
                QmsClient qmsClient2 = QmsClient.this;
                qmsClient2.mQmsReged = qmsClient2.mQmsSwitchCloud ? qmsClient2.mQmsReged : false;
                QmsClient.log("onChange mQmsSwitchCloud=" + QmsClient.this.mQmsSwitchCloud + ",mQmsReged=" + QmsClient.this.mQmsReged);
            }
        };
        this.mDualDataEnabledObserver = new ContentObserver(handler) { // from class: com.android.phone.QmsClient.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean z2 = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "dual_mobile_data", 0) != 0;
                QmsClient.log("curUserDualDataEnabled = " + z2 + ", mUserDualDataEnabled = " + QmsClient.this.mUserDualDataEnabled);
                if (QmsClient.this.mUserDualDataEnabled != z2) {
                    QmsClient.this.mUserDualDataEnabled = z2;
                    if (MiuiPhoneUtils.isLicenseDualDataSupported() && QmsClient.this.mUserDualDataEnabled && !QmsClient.this.mHasRegisterDualData) {
                        QmsClient.this.mHasRegisterDualData = true;
                        QmsClient.this.registerDppdIndication();
                        QmsClient.this.registerMultiSimSubmodeIndication();
                    }
                }
            }
        };
        try {
            this.mIMiQms = IMiQms.getService();
            log("mIMiQms=" + this.mIMiQms);
            this.mIMiQms.setCallback(new QmsCallback());
            boolean zIsQmsSupported = TelephonyManagerEx.getDefault().isQmsSupported();
            log("isQmsSupported=" + zIsQmsSupported);
            boolean z = true;
            if (zIsQmsSupported) {
                this.mQmsSwitchCloud = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 1) == 1;
            } else {
                this.mQmsSwitchCloud = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 0) == 1;
            }
            PhoneGlobals.getInstance().getContentResolver().registerContentObserver(Settings.Global.getUriFor("qms_switch_status"), false, this.mQmsCloudObserver);
            if (Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "dual_mobile_data", 0) == 0) {
                z = false;
            }
            this.mUserDualDataEnabled = z;
            PhoneGlobals.getInstance().getContentResolver().registerContentObserver(Settings.Global.getUriFor("dual_mobile_data"), false, this.mDualDataEnabledObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Rlog.d(TAG, str);
    }

    @Override // com.android.phone.QmsClientBase
    public void registerQmsClient(Message message) {
        if (!this.mQmsSwitchCloud) {
            log("close by cloud switch");
            return;
        }
        if (this.mQmsReged) {
            log("already registered as client");
            return;
        }
        try {
            this.mIMiQms = IMiQms.getService();
            log("IMiQms service=" + this.mIMiQms);
            this.mIMiQms.setCallback(new QmsCallback());
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.mServiceCookie = jCurrentTimeMillis;
            this.mIMiQms.linkToDeath(this.mQmsDeathRecipient, jCurrentTimeMillis);
            this.mRegClntDoneMsg = message;
            this.mIMiQms.registerQmsClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setDataStallParam(int i, Object obj, Message message) {
        if (this.mQmsReged) {
            try {
                if (obj instanceof DataStallParamS) {
                    int dataStallParameters = this.mIMiQms.setDataStallParameters(i, convertToHalDataStallParamS((DataStallParamS) obj));
                    log("setDataStallParam " + dataStallParameters);
                    this.mTransactionMessageMap.put(Integer.valueOf(dataStallParameters), message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setL2NrSelection(int i, Message message) {
        if (this.mQmsReged) {
            try {
                int l2NrSelection = this.mIMiQms.setL2NrSelection(i);
                log("setL2NrSelection " + l2NrSelection);
                this.mTransactionMessageMap.put(Integer.valueOf(l2NrSelection), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setNfList(byte b, ArrayList arrayList, Message message) {
        if (this.mQmsReged) {
            try {
                ArrayList arrayList2 = new ArrayList();
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    arrayList2.add(convertToHalNr5gNflEntryTypeS((Nr5gNflEntryTypeS) obj));
                }
                int nfList = this.mIMiQms.setNfList(b, (short) arrayList2.size(), arrayList2);
                log("setNfList " + nfList);
                this.mTransactionMessageMap.put(Integer.valueOf(nfList), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void getNfList(int i, Message message) {
        if (this.mQmsReged) {
            try {
                int nfList = this.mIMiQms.getNfList(i);
                log("getNfList " + nfList);
                this.mTransactionMessageMap.put(Integer.valueOf(nfList), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setLteDubiousCellArray(int i, ArrayList arrayList, Message message) {
        if (this.mQmsReged) {
            try {
                ArrayList arrayList2 = new ArrayList();
                int size = arrayList.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj = arrayList.get(i2);
                    i2++;
                    arrayList2.add(convertToHalLteDubiousCellInfoS((LteDubiousCellInfoS) obj));
                }
                int lteDubiousCellArray = this.mIMiQms.setLteDubiousCellArray(i, (short) arrayList2.size(), arrayList2);
                log("setLteDubiousCellArray " + lteDubiousCellArray);
                this.mTransactionMessageMap.put(Integer.valueOf(lteDubiousCellArray), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setNrDubiousCellArray(int i, ArrayList arrayList, Message message) {
        if (this.mQmsReged) {
            try {
                ArrayList arrayList2 = new ArrayList();
                int size = arrayList.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj = arrayList.get(i2);
                    i2++;
                    arrayList2.add(convertToHalNrDubiousCellInfoS((NrDubiousCellInfoS) obj));
                }
                int nrDubiousCellArray = this.mIMiQms.setNrDubiousCellArray(i, (short) arrayList2.size(), arrayList2);
                log("setNrDubiousCellArray " + nrDubiousCellArray);
                this.mTransactionMessageMap.put(Integer.valueOf(nrDubiousCellArray), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void updateDppdUiOption(boolean z, Message message) {
        if (this.mQmsReged) {
            try {
                int iUpdateDppdUiOption = this.mIMiQms.updateDppdUiOption(z);
                log("updateDppdUiOption " + iUpdateDppdUiOption);
                this.mTransactionMessageMap.put(Integer.valueOf(iUpdateDppdUiOption), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void getDppdCapability(Message message) {
        if (this.mQmsReged) {
            try {
                int dppdCapability = this.mIMiQms.getDppdCapability();
                log("getDppdCapability " + dppdCapability);
                this.mTransactionMessageMap.put(Integer.valueOf(dppdCapability), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void getDppdRecommendation(Message message) {
        if (this.mQmsReged) {
            try {
                int dppdRecommendation = this.mIMiQms.getDppdRecommendation();
                log("getDppdRecommendation " + dppdRecommendation);
                this.mTransactionMessageMap.put(Integer.valueOf(dppdRecommendation), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerDppdIndication() {
        if (this.mQmsReged) {
            try {
                log("enter registerDppdIndication ");
                this.mIMiQms.registerDppdIndication();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerMultiSimSubmodeIndication() {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                log("enter registerMultiSimSubmodeIndication ");
                this.mIMiQms.registerMultiSimSubmodeIndication();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void getMultiSimSubmodeInfo(Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                int multiSimSubmodeInfo = this.mIMiQms.getMultiSimSubmodeInfo();
                log("getMultiSimSubmodeInfo " + multiSimSubmodeInfo);
                this.mTransactionMessageMap.put(Integer.valueOf(multiSimSubmodeInfo), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setOutOfServiceState(int i, int i2, Message message) {
        if (this.mQmsReged) {
            try {
                int outOfServiceState = this.mIMiQms.setOutOfServiceState(i, i2);
                log("setOutOfServiceState " + outOfServiceState);
                this.mTransactionMessageMap.put(Integer.valueOf(outOfServiceState), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void registerHstStateReport() {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                log("enter registerHstStateReport ");
                this.mIMiQms.registerHstStateReport();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setHstState(int i, Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                int hstState = this.mIMiQms.setHstState(i);
                log("setHstState tid " + hstState);
                this.mTransactionMessageMap.put(Integer.valueOf(hstState), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void getHstState(Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                int hstState = this.mIMiQms.getHstState();
                log("getHstState tid " + hstState);
                this.mTransactionMessageMap.put(Integer.valueOf(hstState), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setCoverageState(int i, int i2, Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                int coverageState = this.mIMiQms.setCoverageState(i, i2);
                log("setCoverageState " + coverageState);
                this.mTransactionMessageMap.put(Integer.valueOf(coverageState), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setPreferredCellList(int i, Object obj, Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                if (obj instanceof PreferredCellListS) {
                    int preferredCellList = this.mIMiQms.setPreferredCellList(i, convertToHalPreferredCellListS((PreferredCellListS) obj));
                    log("setPreferredCellList tid" + preferredCellList);
                    this.mTransactionMessageMap.put(Integer.valueOf(preferredCellList), message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    class QmsCallback extends IMiQmsResponse.Stub {
        QmsCallback() {
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void serviceStatusResponse(int i) {
            QmsClient.log("qms reg status:" + i);
            if (i == 5) {
                QmsClient.this.mQmsReged = true;
                if (MiuiPhoneUtils.isLicenseDualDataSupported() && QmsClient.this.mUserDualDataEnabled && !QmsClient.this.mHasRegisterDualData) {
                    QmsClient.this.mHasRegisterDualData = true;
                    QmsClient.this.registerDppdIndication();
                    QmsClient.this.registerMultiSimSubmodeIndication();
                }
            }
            if (QmsClient.this.mRegClntDoneMsg != null) {
                QmsClient.this.mRegClntDoneMsg.arg1 = i;
                QmsClient.this.mRegClntDoneMsg.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void statusEvResponse(int i, int i2) {
            QmsClient.log("status_ev_cb " + i + " status:" + i2);
            Message message = (Message) QmsClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void getNfListStatusEvResponse(int i, int i2, int i3, ArrayList arrayList) {
            QmsClient.log("getNfListResponse:" + i + " status:" + i2 + arrayList);
            Message message = (Message) QmsClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ArrayList arrayList2 = new ArrayList();
                int size = arrayList.size();
                int i4 = 0;
                while (i4 < size) {
                    Object obj = arrayList.get(i4);
                    i4++;
                    arrayList2.add(QmsClient.convertFromHalNr5gNflEntryTypeS((vendor.xiaomi.modem.qms.V1_0.Nr5gNflEntryTypeS) obj));
                }
                message.obj = arrayList2;
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void dppdUiStatusEvResponse(int i, int i2, boolean z) {
            QmsClient.log("dppdUiStatusEvResponse:" + i + " status:" + i2 + " result:" + z);
            QmsClient.this.callbackUiOptionStatus(i2, z);
            Message message = (Message) QmsClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = Boolean.valueOf(z);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void dppdCapabilityStatusEvResponse(int i, int i2, boolean z) {
            QmsClient.log("dppdCapabilityStatusEvResponse:" + i + " status:" + i2 + " capability:" + z);
            QmsClient.this.callbackDppdStatusCapability(i2, z);
            Message message = (Message) QmsClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = Boolean.valueOf(z);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void dppdRecommendationStatusEvResponse(int i, int i2, DataRecommendationS dataRecommendationS) {
            QmsClient.log("dppdRecommendationStatusEvResponse:" + i + " status:" + i2 + " action:" + dataRecommendationS.dataRecommendationAction + " subsctiption:" + dataRecommendationS.dataRecommendationSubscription);
            QmsClient.this.callbackDppdStatusRecommendation(i2, dataRecommendationS.dataRecommendationAction, dataRecommendationS.dataRecommendationSubscription);
            Message message = (Message) QmsClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = QmsClient.convertFromHalDataRecommendationS(dataRecommendationS);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void dppdCapabilityChangeResponse(boolean z) {
            QmsClient.log("dppdCapabilityChangeResponse: capability:" + z);
            QmsClient.this.callbackDppdCapability(z);
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void dppdRecommendationChangeResponse(DataRecommendationS dataRecommendationS) {
            int i = dataRecommendationS.dataRecommendationAction;
            int i2 = dataRecommendationS.dataRecommendationSubscription;
            QmsClient.log("dppdRecommendationStatusEvResponse: action:" + i + " subsctiption:" + i2);
            QmsClient.this.callbackDppdRecommendation(i, i2);
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void hstStateReportResponse(int i) {
            QmsClient.log("hstStateReportResponse  hst_state:" + i);
            QmsClient.this.callbackHstStateReportChanged(i);
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void getHstStateRespResponse(int i, int i2, int i3) {
            QmsClient.log("getHstStateRespResponse:" + i + " status:" + i2 + " hst_state:" + i3);
            Message message = (Message) QmsClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            QmsClient.this.callbackGetHstStateChanged(i2, i3);
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = Integer.valueOf(i3);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void dppdMultiSimSubmodeInfoChangeResponse(SubmodeInfoS submodeInfoS) {
            int i = submodeInfoS.submode;
            int i2 = submodeInfoS.txConcurrency;
            QmsClient.log("dppdMultiSimSubmodeInfoChangeResponse: submode:" + i + " txConcurrency:" + i2);
            QmsClient.this.callbackDppdSubmodeInfo(i, i2);
        }

        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public void dppdMultiSimSubmodeInfoStatusEvResponse(int i, int i2, SubmodeInfoS submodeInfoS) {
            QmsClient.log("dppdMultiSimSubmodeInfoStatusEvResponse:" + i + " status:" + i2 + " submode:" + submodeInfoS.submode + " txConcurrency:" + submodeInfoS.txConcurrency);
            QmsClient.this.callbackDppdStatusSubmodeInfo(i2, submodeInfoS.submode, submodeInfoS.txConcurrency);
            Message message = (Message) QmsClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = QmsClient.convertFromHalSubmodeInfoS(submodeInfoS);
                message.sendToTarget();
            }
        }
    }

    class QmsDeathRecipient implements IHwBinder.DeathRecipient {
        QmsDeathRecipient() {
        }

        public void serviceDied(long j) {
            QmsClient.log("qms serviceDied cookie:" + j);
            if (QmsClient.this.mRetryCount.getAndIncrement() < 10) {
                QmsClient.this.mHandler.sendMessageDelayed(QmsClient.this.mHandler.obtainMessage(100, 0, 0, Long.valueOf(j)), 2000L);
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void registerDppdInterfaceListener(QmsClientBase.IDppdInterfaceListener iDppdInterfaceListener) {
        synchronized (this.mDppdInterfaceListenerList) {
            this.mDppdInterfaceListenerList.add(iDppdInterfaceListener);
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void registerHstStateListener(QmsClientBase.IHstStateListener iHstStateListener) {
        synchronized (this.mHstStateChangedListenerList) {
            this.mHstStateChangedListenerList.add(iHstStateListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackUiOptionStatus(int i, boolean z) {
        Iterator it = this.mDppdInterfaceListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IDppdInterfaceListener) it.next()).onUiOptionStatusChanged(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackDppdStatusCapability(int i, boolean z) {
        Iterator it = this.mDppdInterfaceListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IDppdInterfaceListener) it.next()).onDppdStatusCapabilityChanged(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackDppdStatusRecommendation(int i, int i2, int i3) {
        Iterator it = this.mDppdInterfaceListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IDppdInterfaceListener) it.next()).onDppdStatusRecommendationChanged(i, i2, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackDppdCapability(boolean z) {
        Iterator it = this.mDppdInterfaceListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IDppdInterfaceListener) it.next()).onDppdCapabilityChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackDppdRecommendation(int i, int i2) {
        Iterator it = this.mDppdInterfaceListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IDppdInterfaceListener) it.next()).onDppdRecommendationChanged(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackHstStateReportChanged(int i) {
        Iterator it = this.mHstStateChangedListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IHstStateListener) it.next()).onHstStateReportChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackGetHstStateChanged(int i, int i2) {
        Iterator it = this.mHstStateChangedListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IHstStateListener) it.next()).onGetHstStateChanged(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackDppdSubmodeInfo(int i, int i2) {
        Iterator it = this.mDppdInterfaceListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IDppdInterfaceListener) it.next()).onDppdSubmodeInfoChanged(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackDppdStatusSubmodeInfo(int i, int i2, int i3) {
        Iterator it = this.mDppdInterfaceListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.IDppdInterfaceListener) it.next()).onDppdStatusSubmodeInfoChanged(i, i2, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static miui.telephony.qms.DataRecommendationS convertFromHalDataRecommendationS(DataRecommendationS dataRecommendationS) {
        if (dataRecommendationS == null) {
            return null;
        }
        miui.telephony.qms.DataRecommendationS dataRecommendationS2 = new miui.telephony.qms.DataRecommendationS();
        int i = dataRecommendationS.dataRecommendationAction;
        dataRecommendationS2.dataRecommendationAction = i;
        dataRecommendationS2.dataRecommendationSubscription = i;
        return dataRecommendationS2;
    }

    private static vendor.xiaomi.modem.qms.V1_0.DataStallParamS convertToHalDataStallParamS(DataStallParamS dataStallParamS) {
        if (dataStallParamS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.V1_0.DataStallParamS dataStallParamS2 = new vendor.xiaomi.modem.qms.V1_0.DataStallParamS();
        dataStallParamS2.trafficDirection = dataStallParamS.trafficDirection;
        dataStallParamS2.dataStallStatus = dataStallParamS.dataStallStatus;
        dataStallParamS2.appType = dataStallParamS.appType;
        dataStallParamS2.expectedIpPacketCount = dataStallParamS.expectedIpPacketCount;
        dataStallParamS2.expectedDataRate = dataStallParamS.expectedDataRate;
        dataStallParamS2.lostIpPacketCount = dataStallParamS.lostIpPacketCount;
        dataStallParamS2.measurementInterval = dataStallParamS.measurementInterval;
        return dataStallParamS2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Nr5gNflEntryTypeS convertFromHalNr5gNflEntryTypeS(vendor.xiaomi.modem.qms.V1_0.Nr5gNflEntryTypeS nr5gNflEntryTypeS) {
        if (nr5gNflEntryTypeS == null) {
            return null;
        }
        Nr5gNflEntryTypeS nr5gNflEntryTypeS2 = new Nr5gNflEntryTypeS();
        nr5gNflEntryTypeS2.arfcn = nr5gNflEntryTypeS.arfcn;
        nr5gNflEntryTypeS2.activeBand = nr5gNflEntryTypeS.activeBand;
        nr5gNflEntryTypeS2.scsType = nr5gNflEntryTypeS.scsType;
        nr5gNflEntryTypeS2.plmnIdArrayLength = nr5gNflEntryTypeS.plmnIdArrayLength;
        for (int i = 0; i < nr5gNflEntryTypeS2.plmnIdArrayLength; i++) {
            try {
                PlmnIdS plmnIdS = new PlmnIdS();
                plmnIdS.mcc = ((vendor.xiaomi.modem.qms.V1_0.PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i)).mcc;
                plmnIdS.mnc = ((vendor.xiaomi.modem.qms.V1_0.PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i)).mnc;
                plmnIdS.mncIncludesPcsDigit = ((vendor.xiaomi.modem.qms.V1_0.PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i)).mncIncludesPcsDigit;
                nr5gNflEntryTypeS2.plmnIdArray.add(plmnIdS);
            } catch (Exception e) {
                log("convertFromHalNr5gNflEntryTypeS = " + e);
                return nr5gNflEntryTypeS2;
            }
        }
        return nr5gNflEntryTypeS2;
    }

    private static vendor.xiaomi.modem.qms.V1_0.Nr5gNflEntryTypeS convertToHalNr5gNflEntryTypeS(Nr5gNflEntryTypeS nr5gNflEntryTypeS) {
        if (nr5gNflEntryTypeS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.V1_0.Nr5gNflEntryTypeS nr5gNflEntryTypeS2 = new vendor.xiaomi.modem.qms.V1_0.Nr5gNflEntryTypeS();
        nr5gNflEntryTypeS2.arfcn = nr5gNflEntryTypeS.arfcn;
        nr5gNflEntryTypeS2.activeBand = nr5gNflEntryTypeS.activeBand;
        nr5gNflEntryTypeS2.scsType = nr5gNflEntryTypeS.scsType;
        short s = nr5gNflEntryTypeS.plmnIdArrayLength;
        nr5gNflEntryTypeS2.plmnIdArrayLength = s;
        try {
            nr5gNflEntryTypeS2.plmnIdArray = new ArrayList(s);
            for (int i = 0; i < nr5gNflEntryTypeS2.plmnIdArrayLength; i++) {
                vendor.xiaomi.modem.qms.V1_0.PlmnIdS plmnIdS = new vendor.xiaomi.modem.qms.V1_0.PlmnIdS();
                plmnIdS.mcc = ((PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i)).mcc;
                plmnIdS.mnc = ((PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i)).mnc;
                plmnIdS.mncIncludesPcsDigit = ((PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i)).mncIncludesPcsDigit;
                nr5gNflEntryTypeS2.plmnIdArray.add(plmnIdS);
            }
            return nr5gNflEntryTypeS2;
        } catch (Exception e) {
            log("convertToHalNr5gNflEntryTypeS = " + e + " value = " + nr5gNflEntryTypeS);
            return nr5gNflEntryTypeS2;
        }
    }

    private static vendor.xiaomi.modem.qms.V1_0.NrDubiousCellInfoS convertToHalNrDubiousCellInfoS(NrDubiousCellInfoS nrDubiousCellInfoS) {
        if (nrDubiousCellInfoS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.V1_0.NrDubiousCellInfoS nrDubiousCellInfoS2 = new vendor.xiaomi.modem.qms.V1_0.NrDubiousCellInfoS();
        try {
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            nrDubiousCellInfoS2.cellInfo = dubiousCellInfoS;
            dubiousCellInfoS.plmnId = new vendor.xiaomi.modem.qms.V1_0.PlmnIdS();
            DubiousCellInfoS dubiousCellInfoS2 = nrDubiousCellInfoS2.cellInfo;
            vendor.xiaomi.modem.qms.V1_0.PlmnIdS plmnIdS = dubiousCellInfoS2.plmnId;
            miui.telephony.qms.DubiousCellInfoS dubiousCellInfoS3 = nrDubiousCellInfoS.cellInfo;
            PlmnIdS plmnIdS2 = dubiousCellInfoS3.plmnId;
            plmnIdS.mcc = plmnIdS2.mcc;
            plmnIdS.mnc = plmnIdS2.mnc;
            plmnIdS.mncIncludesPcsDigit = plmnIdS2.mncIncludesPcsDigit;
            dubiousCellInfoS2.activeBand = dubiousCellInfoS3.activeBand;
            dubiousCellInfoS2.arfcn = dubiousCellInfoS3.arfcn;
            dubiousCellInfoS2.pci = dubiousCellInfoS3.pci;
            dubiousCellInfoS2.causeCodeBitMask = dubiousCellInfoS3.causeCodeBitMask;
            nrDubiousCellInfoS2.cgi = nrDubiousCellInfoS.cgi;
            nrDubiousCellInfoS2.nrScsType = nrDubiousCellInfoS.nrScsType;
            return nrDubiousCellInfoS2;
        } catch (Exception e) {
            log("convertToHalNrDubiousCellInfoS = " + e + " value = " + nrDubiousCellInfoS);
            return nrDubiousCellInfoS2;
        }
    }

    private static vendor.xiaomi.modem.qms.V1_0.PreferredCellListS convertToHalPreferredCellListS(PreferredCellListS preferredCellListS) {
        if (preferredCellListS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.V1_0.PreferredCellListS preferredCellListS2 = new vendor.xiaomi.modem.qms.V1_0.PreferredCellListS();
        preferredCellListS2.expiry_timer = preferredCellListS.expiry_timer;
        preferredCellListS2.lte_cell_list_len = preferredCellListS.lte_cell_list_len;
        preferredCellListS2.lte_cell_list = new ArrayList(preferredCellListS2.lte_cell_list_len);
        preferredCellListS2.nr_cell_list_len = preferredCellListS.nr_cell_list_len;
        preferredCellListS2.nr_cell_list = new ArrayList(preferredCellListS2.nr_cell_list_len);
        for (int i = 0; i < preferredCellListS2.lte_cell_list_len; i++) {
            try {
                PreferredCellInfoS preferredCellInfoS = new PreferredCellInfoS();
                CellInfoS cellInfoS = new CellInfoS();
                preferredCellInfoS.cell_info = cellInfoS;
                cellInfoS.plmn = new vendor.xiaomi.modem.qms.V1_0.PlmnIdS();
                preferredCellInfoS.Location = new LocationS();
                preferredCellInfoS.cell_info.plmn.mcc = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).cell_info.plmn.mcc;
                preferredCellInfoS.cell_info.plmn.mnc = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).cell_info.plmn.mnc;
                preferredCellInfoS.cell_info.plmn.mncIncludesPcsDigit = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).cell_info.plmn.mncIncludesPcsDigit;
                preferredCellInfoS.cell_info.cgi = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).cell_info.cgi;
                preferredCellInfoS.cell_info.band = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).cell_info.band;
                preferredCellInfoS.cell_info.freq = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).cell_info.freq;
                preferredCellInfoS.cell_info.pci = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).cell_info.pci;
                preferredCellInfoS.cause_bitmask = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).cause_bitmask;
                preferredCellInfoS.priority_valid = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).priority_valid;
                preferredCellInfoS.priority = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).priority;
                preferredCellInfoS.location_valid = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).location_valid;
                preferredCellInfoS.Location.longitude = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).location.longitude;
                preferredCellInfoS.Location.latitude = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i)).location.latitude;
                preferredCellListS2.lte_cell_list.add(preferredCellInfoS);
            } catch (Exception e) {
                log("convertToHalPreferredCellListS = " + e + " value = " + preferredCellListS);
                return preferredCellListS2;
            }
        }
        for (int i2 = 0; i2 < preferredCellListS2.nr_cell_list_len; i2++) {
            PreferredCellInfoS preferredCellInfoS2 = new PreferredCellInfoS();
            CellInfoS cellInfoS2 = new CellInfoS();
            preferredCellInfoS2.cell_info = cellInfoS2;
            cellInfoS2.plmn = new vendor.xiaomi.modem.qms.V1_0.PlmnIdS();
            preferredCellInfoS2.Location = new LocationS();
            preferredCellInfoS2.cell_info.plmn.mcc = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).cell_info.plmn.mcc;
            preferredCellInfoS2.cell_info.plmn.mnc = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).cell_info.plmn.mnc;
            preferredCellInfoS2.cell_info.plmn.mncIncludesPcsDigit = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).cell_info.plmn.mncIncludesPcsDigit;
            preferredCellInfoS2.cell_info.cgi = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).cell_info.cgi;
            preferredCellInfoS2.cell_info.band = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).cell_info.band;
            preferredCellInfoS2.cell_info.freq = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).cell_info.freq;
            preferredCellInfoS2.cell_info.pci = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).cell_info.pci;
            preferredCellInfoS2.cause_bitmask = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).cause_bitmask;
            preferredCellInfoS2.priority_valid = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).priority_valid;
            preferredCellInfoS2.priority = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).priority;
            preferredCellInfoS2.location_valid = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).location_valid;
            preferredCellInfoS2.Location.longitude = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).location.longitude;
            preferredCellInfoS2.Location.latitude = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i2)).location.latitude;
            preferredCellListS2.nr_cell_list.add(preferredCellInfoS2);
        }
        return preferredCellListS2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static miui.telephony.qms.SubmodeInfoS convertFromHalSubmodeInfoS(SubmodeInfoS submodeInfoS) {
        if (submodeInfoS == null) {
            return null;
        }
        return new miui.telephony.qms.SubmodeInfoS(submodeInfoS.submode, submodeInfoS.txConcurrency);
    }

    private static vendor.xiaomi.modem.qms.V1_0.LteDubiousCellInfoS convertToHalLteDubiousCellInfoS(LteDubiousCellInfoS lteDubiousCellInfoS) {
        if (lteDubiousCellInfoS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.V1_0.LteDubiousCellInfoS lteDubiousCellInfoS2 = new vendor.xiaomi.modem.qms.V1_0.LteDubiousCellInfoS();
        try {
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            lteDubiousCellInfoS2.cellInfo = dubiousCellInfoS;
            dubiousCellInfoS.plmnId = new vendor.xiaomi.modem.qms.V1_0.PlmnIdS();
            DubiousCellInfoS dubiousCellInfoS2 = lteDubiousCellInfoS2.cellInfo;
            vendor.xiaomi.modem.qms.V1_0.PlmnIdS plmnIdS = dubiousCellInfoS2.plmnId;
            miui.telephony.qms.DubiousCellInfoS dubiousCellInfoS3 = lteDubiousCellInfoS.cellInfo;
            PlmnIdS plmnIdS2 = dubiousCellInfoS3.plmnId;
            plmnIdS.mcc = plmnIdS2.mcc;
            plmnIdS.mnc = plmnIdS2.mnc;
            plmnIdS.mncIncludesPcsDigit = plmnIdS2.mncIncludesPcsDigit;
            dubiousCellInfoS2.activeBand = dubiousCellInfoS3.activeBand;
            dubiousCellInfoS2.arfcn = dubiousCellInfoS3.arfcn;
            dubiousCellInfoS2.pci = dubiousCellInfoS3.pci;
            dubiousCellInfoS2.causeCodeBitMask = dubiousCellInfoS3.causeCodeBitMask;
            lteDubiousCellInfoS2.cgi = lteDubiousCellInfoS.cgi;
            return lteDubiousCellInfoS2;
        } catch (Exception e) {
            log("convertToHalLteDubiousCellInfoS = " + e + " value = " + lteDubiousCellInfoS);
            return lteDubiousCellInfoS2;
        }
    }
}
