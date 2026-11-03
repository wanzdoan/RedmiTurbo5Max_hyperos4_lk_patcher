package com.android.phone;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.qms.CellBarringInfoS;
import miui.telephony.qms.CellListS;
import miui.telephony.qms.DataStallParamS;
import miui.telephony.qms.LteDubiousCellInfoS;
import miui.telephony.qms.Nr5gNflEntryTypeS;
import miui.telephony.qms.NrDubiousCellInfoS;
import miui.telephony.qms.PlmnIdS;
import miui.telephony.qms.PreferredCellListS;
import vendor.xiaomi.modem.qms.CellInfoS;
import vendor.xiaomi.modem.qms.CellListInfoIndS;
import vendor.xiaomi.modem.qms.CellListInfoS;
import vendor.xiaomi.modem.qms.CellPropertyCategoryCommonS;
import vendor.xiaomi.modem.qms.CellPropertyCategoryHdrRatTypeS;
import vendor.xiaomi.modem.qms.CellPropertyCategoryHdrS;
import vendor.xiaomi.modem.qms.CellPropertyCategoryHstTypeS;
import vendor.xiaomi.modem.qms.CellPropertyCategoryRsS;
import vendor.xiaomi.modem.qms.CellPropertyCategoryS;
import vendor.xiaomi.modem.qms.CellPropertyCategoryTypeV2S;
import vendor.xiaomi.modem.qms.DataRecommendationS;
import vendor.xiaomi.modem.qms.DubiousCellInfoS;
import vendor.xiaomi.modem.qms.IMiQms;
import vendor.xiaomi.modem.qms.IMiQmsIndication;
import vendor.xiaomi.modem.qms.IMiQmsResponse;
import vendor.xiaomi.modem.qms.LocationS;
import vendor.xiaomi.modem.qms.PreferredCellInfoS;
import vendor.xiaomi.modem.qms.PreferredFrequencyInfoS;
import vendor.xiaomi.modem.qms.SetCellListInfoTypeV2S;
import vendor.xiaomi.modem.qms.SetCellListInfoV2S;
import vendor.xiaomi.modem.qms.SetPreferredFrequencyListS;
import vendor.xiaomi.modem.qms.SubmodeInfoS;
import vendor.xiaomi.modem.qms.UniqueCellInfoS;

/* JADX INFO: loaded from: classes.dex */
public class QmsAidlClient extends QmsClientBase {
    public static final String QMS_SERVICE_INTERFACE = IMiQms.DESCRIPTOR + "/default";
    private static final String TAG = "QmsAidlClient";
    private IMiQms mIMiQms;
    private boolean mQmsReged;
    private Message mRegClntDoneMsg;
    private ConcurrentHashMap mTransactionMessageMap = new ConcurrentHashMap();
    private QmsDeathRecipient mQmsDeathRecipient = new QmsDeathRecipient();
    private AtomicInteger mRetryCount = new AtomicInteger();
    private Handler mHandler = new Handler() { // from class: com.android.phone.QmsAidlClient.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 100) {
                QmsAidlClient.this.mIMiQms = null;
                QmsAidlClient.this.mQmsReged = false;
                QmsAidlClient.log("retry to register Qms client:" + QmsAidlClient.this.mRetryCount.get());
                QmsAidlClient.this.registerQmsClient(null);
            }
        }
    };
    private List mDppdInterfaceListenerList = new ArrayList();
    private List mHstStateChangedListenerList = new ArrayList();
    private List mSubwayStateChangedListenerList = new ArrayList();
    private ContentObserver mQmsCloudObserver = new ContentObserver(0 == true ? 1 : 0) { // from class: com.android.phone.QmsAidlClient.2
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            boolean zIsQmsSupported = TelephonyManagerEx.getDefault().isQmsSupported();
            QmsAidlClient.log("isQmsSupported=" + zIsQmsSupported);
            QmsAidlClient qmsAidlClient = QmsAidlClient.this;
            if (zIsQmsSupported) {
                qmsAidlClient.mQmsSwitchCloud = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 1) == 1;
            } else {
                qmsAidlClient.mQmsSwitchCloud = Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 0) == 1;
            }
            QmsAidlClient qmsAidlClient2 = QmsAidlClient.this;
            qmsAidlClient2.mQmsReged = qmsAidlClient2.mQmsSwitchCloud ? qmsAidlClient2.mQmsReged : false;
            QmsAidlClient.log("onChange mQmsSwitchCloud=" + QmsAidlClient.this.mQmsSwitchCloud + ",mQmsReged=" + QmsAidlClient.this.mQmsReged);
        }
    };

    public static QmsClientBase make() {
        if (QmsClientBase.mInstance == null) {
            QmsClientBase.mInstance = new QmsAidlClient();
        }
        return QmsClientBase.mInstance;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private QmsAidlClient() {
        Object[] objArr = 0;
        Object[] objArr2 = 0;
        Object[] objArr3 = 0;
        try {
            this.mIMiQms = IMiQms.Stub.asInterface(ServiceManager.waitForDeclaredService(QMS_SERVICE_INTERFACE));
            log("mIMiQms =" + this.mIMiQms);
            IMiQms iMiQms = this.mIMiQms;
            if (iMiQms != null) {
                iMiQms.setCallback(new QmsCallback());
                if (MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
                    this.mIMiQms.setResponseFunctions(new QmsCallback(), new QmsIndication());
                }
            }
            boolean zIsQmsSupported = TelephonyManagerEx.getDefault().isQmsSupported();
            log("isQmsSupported=" + zIsQmsSupported);
            boolean z = true;
            if (zIsQmsSupported) {
                if (Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 1) != 1) {
                    z = false;
                }
                this.mQmsSwitchCloud = z;
            } else {
                if (Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 0) != 1) {
                    z = false;
                }
                this.mQmsSwitchCloud = z;
            }
            PhoneGlobals.getInstance().getContentResolver().registerContentObserver(Settings.Global.getUriFor("qms_switch_status"), false, this.mQmsCloudObserver);
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
            if (this.mIMiQms == null) {
                this.mIMiQms = IMiQms.Stub.asInterface(ServiceManager.waitForDeclaredService(QMS_SERVICE_INTERFACE));
                log("IMiQms service =" + this.mIMiQms);
            }
            IMiQms iMiQms = this.mIMiQms;
            if (iMiQms == null) {
                log("mIMiQms is null, cannot register QMS client");
                return;
            }
            iMiQms.setCallback(new QmsCallback());
            if (MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
                this.mIMiQms.setResponseFunctions(new QmsCallback(), new QmsIndication());
            }
            this.mIMiQms.asBinder().linkToDeath(this.mQmsDeathRecipient, 0);
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
                int nfList = this.mIMiQms.setNfList(b, (char) arrayList2.size(), (vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS[]) arrayList2.toArray(new vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS[0]));
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
                int lteDubiousCellArray = this.mIMiQms.setLteDubiousCellArray(i, (char) arrayList2.size(), (vendor.xiaomi.modem.qms.LteDubiousCellInfoS[]) arrayList2.toArray(new vendor.xiaomi.modem.qms.LteDubiousCellInfoS[0]));
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
                int nrDubiousCellArray = this.mIMiQms.setNrDubiousCellArray(i, (char) arrayList2.size(), (vendor.xiaomi.modem.qms.NrDubiousCellInfoS[]) arrayList2.toArray(new vendor.xiaomi.modem.qms.NrDubiousCellInfoS[0]));
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

    @Override // com.android.phone.QmsClientBase
    public void enableCellBarringFeature(int i, boolean z, Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                int iEnableCellBarringFeature = this.mIMiQms.enableCellBarringFeature(i, z);
                log("enableCellBarringFeature " + iEnableCellBarringFeature);
                this.mTransactionMessageMap.put(Integer.valueOf(iEnableCellBarringFeature), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setCellBarringInfo(int i, List list, Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV3SupportedByQcom()) {
            try {
                for (Object obj : list) {
                    if (obj instanceof CellBarringInfoS) {
                        int cellBarringInfo = this.mIMiQms.setCellBarringInfo(i, convertToHalCellBarringInfoS((CellBarringInfoS) obj));
                        log("setCellBarringInfo tid" + cellBarringInfo);
                        this.mTransactionMessageMap.put(Integer.valueOf(cellBarringInfo), message);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void registerSubwayStateReport() {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
            try {
                log("enter registerSubwayStateReport ");
                this.mIMiQms.registerSubwayStateReport();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setCellListInfo(List list, Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
            try {
                for (Object obj : list) {
                    if (obj instanceof CellListS) {
                        long cellListInfo = this.mIMiQms.setCellListInfo(convertToHalCellListS((CellListS) obj));
                        log("setCellListInfo tid" + cellListInfo);
                        this.mTransactionMessageMap.put(Integer.valueOf((int) cellListInfo), message);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setSubwayState(int i, Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV4SupportedByQcom()) {
            try {
                int subwayState = this.mIMiQms.setSubwayState(i);
                log("setSubwayState tid " + subwayState);
                this.mTransactionMessageMap.put(Integer.valueOf(subwayState), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setCellListInfoV2(List list, Message message) {
        SetCellListInfoV2S setCellListInfoV2SConvertToHalSetCellListInfoV2S;
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV5SupportedByQcom() && list != null) {
            try {
                IMiQms iMiQms = this.mIMiQms;
                if (iMiQms == null) {
                    return;
                }
                for (Object obj : list) {
                    if ((obj instanceof miui.telephony.qms.SetCellListInfoV2S) && (setCellListInfoV2SConvertToHalSetCellListInfoV2S = convertToHalSetCellListInfoV2S((miui.telephony.qms.SetCellListInfoV2S) obj)) != null) {
                        long cellListInfoV2 = iMiQms.setCellListInfoV2(setCellListInfoV2SConvertToHalSetCellListInfoV2S);
                        log("setCellListInfoV2 tid " + cellListInfoV2);
                        this.mTransactionMessageMap.put(Integer.valueOf((int) cellListInfoV2), message);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.android.phone.QmsClientBase
    public void setPreferredFrequencyList(int i, int i2, int i3, List list, List list2, Message message) {
        if (this.mQmsReged && MiuiPhoneUtils.isSNSV5SupportedByQcom()) {
            if (i2 == 0 || i2 == 1) {
                Rlog.w(TAG, "setPreferredFrequencyList blocked: unsupported usageType=" + i2);
                return;
            }
            try {
                IMiQms iMiQms = this.mIMiQms;
                if (iMiQms == null) {
                    return;
                }
                long preferredFrequencyList = iMiQms.setPreferredFrequencyList(convertToHalSetPreferredFrequencyListS(i, i2, i3, list, list2));
                log("setPreferredFrequencyList tid " + preferredFrequencyList);
                this.mTransactionMessageMap.put(Integer.valueOf((int) preferredFrequencyList), message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    class QmsCallback extends IMiQmsResponse.Stub {
        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public int getInterfaceVersion() {
            return 2;
        }

        private QmsCallback() {
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public String getInterfaceHash() {
            return "8f37e814769bd21e17ad99ce5bea9ba0a2b2b992";
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void serviceStatusResponse(int i) {
            QmsAidlClient.log("qms reg status:" + i);
            if (i == 5) {
                QmsAidlClient.this.mQmsReged = true;
                if (MiuiPhoneUtils.isLicenseDualDataSupported()) {
                    QmsAidlClient.this.registerDppdIndication();
                    QmsAidlClient.this.registerMultiSimSubmodeIndication();
                }
                QmsAidlClient.this.registerSubwayStateReport();
                QmsAidlClient.this.registerHstStateReport();
            }
            if (QmsAidlClient.this.mRegClntDoneMsg != null) {
                QmsAidlClient.this.mRegClntDoneMsg.arg1 = i;
                QmsAidlClient.this.mRegClntDoneMsg.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void statusEvResponse(int i, int i2) {
            QmsAidlClient.log("status_ev_cb " + i + " status:" + i2);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void setCellListInfoStatusEvResponse(int i, int i2) {
            QmsAidlClient.log("setCellListInfoStatusEvResponse " + i + " status:" + i2);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void getNfListStatusEvResponse(int i, int i2, int i3, vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS[] nr5gNflEntryTypeSArr) {
            QmsAidlClient.log("getNfListResponse:" + i + " status:" + i2 + nr5gNflEntryTypeSArr);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ArrayList arrayList = new ArrayList();
                for (vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS nr5gNflEntryTypeS : nr5gNflEntryTypeSArr) {
                    arrayList.add(QmsAidlClient.convertFromHalNr5gNflEntryTypeS(nr5gNflEntryTypeS));
                }
                message.obj = arrayList;
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void dppdUiStatusEvResponse(int i, int i2, boolean z) {
            QmsAidlClient.log("dppdUiStatusEvResponse:" + i + " status:" + i2 + " result:" + z);
            QmsAidlClient.this.callbackUiOptionStatus(i2, z);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = Boolean.valueOf(z);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void dppdCapabilityStatusEvResponse(int i, int i2, boolean z) {
            QmsAidlClient.log("dppdCapabilityStatusEvResponse:" + i + " status:" + i2 + " capability:" + z);
            QmsAidlClient.this.callbackDppdStatusCapability(i2, z);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = Boolean.valueOf(z);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void dppdRecommendationStatusEvResponse(int i, int i2, DataRecommendationS dataRecommendationS) {
            QmsAidlClient.log("dppdRecommendationStatusEvResponse:" + i + " status:" + i2 + " action:" + dataRecommendationS.action + " subsctiption:" + dataRecommendationS.subscription);
            QmsAidlClient.this.callbackDppdStatusRecommendation(i2, dataRecommendationS.action, dataRecommendationS.subscription);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = QmsAidlClient.convertFromHalDataRecommendationS(dataRecommendationS);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void dppdCapabilityChangeResponse(boolean z) {
            QmsAidlClient.log("dppdCapabilityChangeResponse: capability:" + z);
            QmsAidlClient.this.callbackDppdCapability(z);
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void dppdRecommendationChangeResponse(DataRecommendationS dataRecommendationS) {
            int i = dataRecommendationS.action;
            int i2 = dataRecommendationS.subscription;
            QmsAidlClient.log("dppdRecommendationStatusEvResponse: action:" + i + " subsctiption:" + i2);
            QmsAidlClient.this.callbackDppdRecommendation(i, i2);
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void hstStateReportResponse(int i) {
            QmsAidlClient.log("hstStateReportResponse  hst_state:" + i);
            QmsAidlClient.this.callbackHstStateReportChanged(i);
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void getHstStateRespResponse(int i, int i2, int i3) {
            QmsAidlClient.log("getHstStateRespResponse:" + i + " status:" + i2 + " hst_state:" + i3);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            QmsAidlClient.this.callbackGetHstStateChanged(i2, i3);
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = Integer.valueOf(i3);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void dppdMultiSimSubmodeInfoChangeResponse(SubmodeInfoS submodeInfoS) {
            int i = submodeInfoS.submode;
            int i2 = submodeInfoS.txConcurrency;
            QmsAidlClient.log("dppdMultiSimSubmodeInfoChangeResponse: submode:" + i + " txConcurrency:" + i2);
            QmsAidlClient.this.callbackDppdSubmodeInfo(i, i2);
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void dppdMultiSimSubmodeInfoStatusEvResponse(int i, int i2, SubmodeInfoS submodeInfoS) {
            QmsAidlClient.log("dppdMultiSimSubmodeInfoStatusEvResponse:" + i + " status:" + i2 + " submode:" + submodeInfoS.submode + " txConcurrency:" + submodeInfoS.txConcurrency);
            QmsAidlClient.this.callbackDppdStatusSubmodeInfo(i2, submodeInfoS.submode, submodeInfoS.txConcurrency);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf(i));
            if (message != null) {
                message.arg1 = i2;
                ((QmsMsgTunnelControllerBase.MessageRequestAsync) message.obj).obj = QmsAidlClient.convertFromHalSubmodeInfoS(submodeInfoS);
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void qmsSubwayStateReportResponse(int i) {
            QmsAidlClient.log("qmsSubwayStateReportResponse subway_state:" + i);
            QmsAidlClient.this.callbackSubwayStateReportChanged(i);
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void setCellListInfoV2CbResponse(long j, int i) {
            QmsAidlClient.log("setCellListInfoV2CbResponse " + j + " status:" + i);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf((int) j));
            if (message != null) {
                message.arg1 = i;
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void updateGoodLinkStateStatusEvResponse(long j, int i) {
            QmsAidlClient.log("updateGoodLinkStateStatusEvResponse " + j + " status:" + i);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf((int) j));
            if (message != null) {
                message.arg1 = i;
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void setPreferredFrequencyListStatusEvResponse(long j, int i) {
            QmsAidlClient.log("setPreferredFrequencyListStatusEvResponse " + j + " status:" + i);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf((int) j));
            if (message != null) {
                message.arg1 = i;
                message.sendToTarget();
            }
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsResponse
        public void setHlosBorderCrossedStatusEvResponse(long j, int i) {
            QmsAidlClient.log("setHlosBorderCrossedStatusEvResponse " + j + " status:" + i);
            Message message = (Message) QmsAidlClient.this.mTransactionMessageMap.remove(Integer.valueOf((int) j));
            if (message != null) {
                message.arg1 = i;
                message.sendToTarget();
            }
        }
    }

    class QmsIndication extends IMiQmsIndication.Stub {
        @Override // vendor.xiaomi.modem.qms.IMiQmsIndication
        public int getInterfaceVersion() {
            return 2;
        }

        private QmsIndication(QmsAidlClient qmsAidlClient) {
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsIndication
        public String getInterfaceHash() {
            return "8f37e814769bd21e17ad99ce5bea9ba0a2b2b992";
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsIndication
        public void cell_list_info_ind(CellListInfoIndS cellListInfoIndS) {
            QmsAidlClient.log("cellListInfoIndication received");
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsIndication
        public void serviceStatusInd(int i) {
            QmsAidlClient.log("serviceStatusInd status:" + i);
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsIndication
        public void serviceStatusV2Ind(int i) {
            QmsAidlClient.log("serviceStatusV2Ind status:" + i);
        }

        @Override // vendor.xiaomi.modem.qms.IMiQmsIndication
        public void cellListInfoV2Ind(CellListInfoIndS cellListInfoIndS) {
            QmsAidlClient.log("cellListInfoV2Ind received");
        }
    }

    class QmsDeathRecipient implements IBinder.DeathRecipient {
        private QmsDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            QmsAidlClient.log("qms serviceDied");
            QmsAidlClient.this.mTransactionMessageMap.clear();
            if (QmsAidlClient.this.mRetryCount.getAndIncrement() < 10) {
                QmsAidlClient.this.mHandler.sendEmptyMessageDelayed(100, 2000L);
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

    @Override // com.android.phone.QmsClientBase
    public void registerSubwayStateListener(QmsClientBase.ISubwayStateListener iSubwayStateListener) {
        synchronized (this.mSubwayStateChangedListenerList) {
            this.mSubwayStateChangedListenerList.add(iSubwayStateListener);
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
    public void callbackSubwayStateReportChanged(int i) {
        Iterator it = this.mSubwayStateChangedListenerList.iterator();
        while (it.hasNext()) {
            ((QmsClientBase.ISubwayStateListener) it.next()).onSubwayStateReportChanged(i);
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
        dataRecommendationS2.dataRecommendationAction = dataRecommendationS.action;
        dataRecommendationS2.dataRecommendationSubscription = dataRecommendationS.subscription;
        return dataRecommendationS2;
    }

    private static vendor.xiaomi.modem.qms.DataStallParamS convertToHalDataStallParamS(DataStallParamS dataStallParamS) {
        if (dataStallParamS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.DataStallParamS dataStallParamS2 = new vendor.xiaomi.modem.qms.DataStallParamS();
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
    public static Nr5gNflEntryTypeS convertFromHalNr5gNflEntryTypeS(vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS nr5gNflEntryTypeS) {
        if (nr5gNflEntryTypeS == null) {
            return null;
        }
        Nr5gNflEntryTypeS nr5gNflEntryTypeS2 = new Nr5gNflEntryTypeS();
        nr5gNflEntryTypeS2.arfcn = nr5gNflEntryTypeS.arfcn;
        nr5gNflEntryTypeS2.activeBand = nr5gNflEntryTypeS.activeBand;
        nr5gNflEntryTypeS2.scsType = nr5gNflEntryTypeS.scsType;
        nr5gNflEntryTypeS2.plmnIdArrayLength = (short) nr5gNflEntryTypeS.plmnIdArrayLength;
        for (int i = 0; i < nr5gNflEntryTypeS2.plmnIdArrayLength; i++) {
            try {
                PlmnIdS plmnIdS = new PlmnIdS();
                vendor.xiaomi.modem.qms.PlmnIdS plmnIdS2 = nr5gNflEntryTypeS.plmnIdArray[i];
                plmnIdS.mcc = (short) plmnIdS2.mcc;
                plmnIdS.mnc = (short) plmnIdS2.mnc;
                plmnIdS.mncIncludesPcsDigit = plmnIdS2.mncIncludesPcsDigit;
                nr5gNflEntryTypeS2.plmnIdArray.add(plmnIdS);
            } catch (Exception e) {
                log("convertFromHalNr5gNflEntryTypeS = " + e);
                return nr5gNflEntryTypeS2;
            }
        }
        return nr5gNflEntryTypeS2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS convertToHalNr5gNflEntryTypeS(Nr5gNflEntryTypeS nr5gNflEntryTypeS) {
        if (nr5gNflEntryTypeS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS nr5gNflEntryTypeS2 = new vendor.xiaomi.modem.qms.Nr5gNflEntryTypeS();
        nr5gNflEntryTypeS2.arfcn = nr5gNflEntryTypeS.arfcn;
        nr5gNflEntryTypeS2.activeBand = nr5gNflEntryTypeS.activeBand;
        nr5gNflEntryTypeS2.scsType = nr5gNflEntryTypeS.scsType;
        int i = (char) nr5gNflEntryTypeS.plmnIdArrayLength;
        nr5gNflEntryTypeS2.plmnIdArrayLength = i;
        try {
            nr5gNflEntryTypeS2.plmnIdArray = new vendor.xiaomi.modem.qms.PlmnIdS[i];
            for (int i2 = 0; i2 < nr5gNflEntryTypeS2.plmnIdArrayLength; i2++) {
                vendor.xiaomi.modem.qms.PlmnIdS plmnIdS = new vendor.xiaomi.modem.qms.PlmnIdS();
                plmnIdS.mcc = (char) ((PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i2)).mcc;
                plmnIdS.mnc = (char) ((PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i2)).mnc;
                plmnIdS.mncIncludesPcsDigit = ((PlmnIdS) nr5gNflEntryTypeS.plmnIdArray.get(i2)).mncIncludesPcsDigit;
                nr5gNflEntryTypeS2.plmnIdArray[i2] = plmnIdS;
            }
            return nr5gNflEntryTypeS2;
        } catch (Exception e) {
            log("convertToHalNr5gNflEntryTypeS = " + e + " value = " + nr5gNflEntryTypeS);
            return nr5gNflEntryTypeS2;
        }
    }

    private static vendor.xiaomi.modem.qms.NrDubiousCellInfoS convertToHalNrDubiousCellInfoS(NrDubiousCellInfoS nrDubiousCellInfoS) {
        if (nrDubiousCellInfoS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.NrDubiousCellInfoS nrDubiousCellInfoS2 = new vendor.xiaomi.modem.qms.NrDubiousCellInfoS();
        try {
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            nrDubiousCellInfoS2.cellInfo = dubiousCellInfoS;
            dubiousCellInfoS.plmnId = new vendor.xiaomi.modem.qms.PlmnIdS();
            DubiousCellInfoS dubiousCellInfoS2 = nrDubiousCellInfoS2.cellInfo;
            vendor.xiaomi.modem.qms.PlmnIdS plmnIdS = dubiousCellInfoS2.plmnId;
            miui.telephony.qms.DubiousCellInfoS dubiousCellInfoS3 = nrDubiousCellInfoS.cellInfo;
            PlmnIdS plmnIdS2 = dubiousCellInfoS3.plmnId;
            plmnIdS.mcc = (char) plmnIdS2.mcc;
            plmnIdS.mnc = (char) plmnIdS2.mnc;
            plmnIdS.mncIncludesPcsDigit = plmnIdS2.mncIncludesPcsDigit;
            dubiousCellInfoS2.activeBand = dubiousCellInfoS3.activeBand;
            dubiousCellInfoS2.arfcn = dubiousCellInfoS3.arfcn;
            dubiousCellInfoS2.pci = (char) dubiousCellInfoS3.pci;
            dubiousCellInfoS2.causeCodeBitMask = dubiousCellInfoS3.causeCodeBitMask;
            nrDubiousCellInfoS2.cgi = nrDubiousCellInfoS.cgi;
            nrDubiousCellInfoS2.nrScsType = nrDubiousCellInfoS.nrScsType;
            return nrDubiousCellInfoS2;
        } catch (Exception e) {
            log("convertToHalNrDubiousCellInfoS = " + e + " value = " + nrDubiousCellInfoS);
            return nrDubiousCellInfoS2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static vendor.xiaomi.modem.qms.PreferredCellListS convertToHalPreferredCellListS(PreferredCellListS preferredCellListS) {
        if (preferredCellListS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.PreferredCellListS preferredCellListS2 = new vendor.xiaomi.modem.qms.PreferredCellListS();
        preferredCellListS2.expiry_timer = preferredCellListS.expiry_timer;
        int i = preferredCellListS.lte_cell_list_len;
        preferredCellListS2.lte_cell_list_len = i;
        preferredCellListS2.lte_cell_list = new PreferredCellInfoS[i];
        int i2 = preferredCellListS.nr_cell_list_len;
        preferredCellListS2.nr_cell_list_len = i2;
        preferredCellListS2.nr_cell_list = new PreferredCellInfoS[i2];
        for (int i3 = 0; i3 < preferredCellListS2.lte_cell_list_len; i3++) {
            try {
                PreferredCellInfoS preferredCellInfoS = new PreferredCellInfoS();
                CellInfoS cellInfoS = new CellInfoS();
                preferredCellInfoS.cell_info = cellInfoS;
                cellInfoS.plmn = new vendor.xiaomi.modem.qms.PlmnIdS();
                preferredCellInfoS.Location = new LocationS();
                preferredCellInfoS.cell_info.plmn.mcc = (char) ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).cell_info.plmn.mcc;
                preferredCellInfoS.cell_info.plmn.mnc = (char) ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).cell_info.plmn.mnc;
                preferredCellInfoS.cell_info.plmn.mncIncludesPcsDigit = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).cell_info.plmn.mncIncludesPcsDigit;
                preferredCellInfoS.cell_info.cgi = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).cell_info.cgi;
                preferredCellInfoS.cell_info.band = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).cell_info.band;
                preferredCellInfoS.cell_info.freq = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).cell_info.freq;
                preferredCellInfoS.cell_info.pci = (char) ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).cell_info.pci;
                preferredCellInfoS.cause_bitmask = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).cause_bitmask;
                preferredCellInfoS.priority_valid = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).priority_valid;
                preferredCellInfoS.priority = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).priority;
                preferredCellInfoS.location_valid = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).location_valid;
                preferredCellInfoS.Location.longitude = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).location.longitude;
                preferredCellInfoS.Location.latitude = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.lte_cell_list.get(i3)).location.latitude;
                preferredCellListS2.lte_cell_list[i3] = preferredCellInfoS;
            } catch (Exception e) {
                log("convertToHalPreferredCellListS = " + e + " value = " + preferredCellListS);
                return preferredCellListS2;
            }
        }
        for (int i4 = 0; i4 < preferredCellListS2.nr_cell_list_len; i4++) {
            PreferredCellInfoS preferredCellInfoS2 = new PreferredCellInfoS();
            CellInfoS cellInfoS2 = new CellInfoS();
            preferredCellInfoS2.cell_info = cellInfoS2;
            cellInfoS2.plmn = new vendor.xiaomi.modem.qms.PlmnIdS();
            preferredCellInfoS2.Location = new LocationS();
            preferredCellInfoS2.cell_info.plmn.mcc = (char) ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).cell_info.plmn.mcc;
            preferredCellInfoS2.cell_info.plmn.mnc = (char) ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).cell_info.plmn.mnc;
            preferredCellInfoS2.cell_info.plmn.mncIncludesPcsDigit = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).cell_info.plmn.mncIncludesPcsDigit;
            preferredCellInfoS2.cell_info.cgi = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).cell_info.cgi;
            preferredCellInfoS2.cell_info.band = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).cell_info.band;
            preferredCellInfoS2.cell_info.freq = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).cell_info.freq;
            preferredCellInfoS2.cell_info.pci = (char) ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).cell_info.pci;
            preferredCellInfoS2.cause_bitmask = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).cause_bitmask;
            preferredCellInfoS2.priority_valid = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).priority_valid;
            preferredCellInfoS2.priority = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).priority;
            preferredCellInfoS2.location_valid = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).location_valid;
            preferredCellInfoS2.Location.longitude = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).location.longitude;
            preferredCellInfoS2.Location.latitude = ((miui.telephony.qms.PreferredCellInfoS) preferredCellListS.nr_cell_list.get(i4)).location.latitude;
            preferredCellListS2.nr_cell_list[i4] = preferredCellInfoS2;
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

    private static vendor.xiaomi.modem.qms.LteDubiousCellInfoS convertToHalLteDubiousCellInfoS(LteDubiousCellInfoS lteDubiousCellInfoS) {
        if (lteDubiousCellInfoS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.LteDubiousCellInfoS lteDubiousCellInfoS2 = new vendor.xiaomi.modem.qms.LteDubiousCellInfoS();
        try {
            DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
            lteDubiousCellInfoS2.cellInfo = dubiousCellInfoS;
            dubiousCellInfoS.plmnId = new vendor.xiaomi.modem.qms.PlmnIdS();
            DubiousCellInfoS dubiousCellInfoS2 = lteDubiousCellInfoS2.cellInfo;
            vendor.xiaomi.modem.qms.PlmnIdS plmnIdS = dubiousCellInfoS2.plmnId;
            miui.telephony.qms.DubiousCellInfoS dubiousCellInfoS3 = lteDubiousCellInfoS.cellInfo;
            PlmnIdS plmnIdS2 = dubiousCellInfoS3.plmnId;
            plmnIdS.mcc = (char) plmnIdS2.mcc;
            plmnIdS.mnc = (char) plmnIdS2.mnc;
            plmnIdS.mncIncludesPcsDigit = plmnIdS2.mncIncludesPcsDigit;
            dubiousCellInfoS2.activeBand = dubiousCellInfoS3.activeBand;
            dubiousCellInfoS2.arfcn = dubiousCellInfoS3.arfcn;
            dubiousCellInfoS2.pci = (char) dubiousCellInfoS3.pci;
            dubiousCellInfoS2.causeCodeBitMask = dubiousCellInfoS3.causeCodeBitMask;
            lteDubiousCellInfoS2.cgi = lteDubiousCellInfoS.cgi;
            return lteDubiousCellInfoS2;
        } catch (Exception e) {
            log("convertToHalLteDubiousCellInfoS = " + e + " value = " + lteDubiousCellInfoS);
            return lteDubiousCellInfoS2;
        }
    }

    private static vendor.xiaomi.modem.qms.CellBarringInfoS convertToHalCellBarringInfoS(CellBarringInfoS cellBarringInfoS) {
        if (cellBarringInfoS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.CellBarringInfoS cellBarringInfoS2 = new vendor.xiaomi.modem.qms.CellBarringInfoS();
        cellBarringInfoS2.expiry_timer = cellBarringInfoS.expiry_timer;
        int i = cellBarringInfoS.lte_cell_list_len;
        cellBarringInfoS2.lte_cell_list_len = i;
        cellBarringInfoS2.lte_cell_list = new vendor.xiaomi.modem.qms.LteDubiousCellInfoS[i];
        int i2 = cellBarringInfoS.nr_cell_list_len;
        cellBarringInfoS2.nr_cell_list_len = i2;
        cellBarringInfoS2.nr_cell_list = new vendor.xiaomi.modem.qms.NrDubiousCellInfoS[i2];
        for (int i3 = 0; i3 < cellBarringInfoS2.lte_cell_list_len; i3++) {
            try {
                vendor.xiaomi.modem.qms.LteDubiousCellInfoS lteDubiousCellInfoS = new vendor.xiaomi.modem.qms.LteDubiousCellInfoS();
                DubiousCellInfoS dubiousCellInfoS = new DubiousCellInfoS();
                lteDubiousCellInfoS.cellInfo = dubiousCellInfoS;
                dubiousCellInfoS.plmnId = new vendor.xiaomi.modem.qms.PlmnIdS();
                lteDubiousCellInfoS.cellInfo.plmnId.mcc = (char) ((LteDubiousCellInfoS) cellBarringInfoS.lte_cell_list.get(i3)).cellInfo.plmnId.mcc;
                lteDubiousCellInfoS.cellInfo.plmnId.mnc = (char) ((LteDubiousCellInfoS) cellBarringInfoS.lte_cell_list.get(i3)).cellInfo.plmnId.mnc;
                lteDubiousCellInfoS.cellInfo.plmnId.mncIncludesPcsDigit = ((LteDubiousCellInfoS) cellBarringInfoS.lte_cell_list.get(i3)).cellInfo.plmnId.mncIncludesPcsDigit;
                lteDubiousCellInfoS.cellInfo.activeBand = ((LteDubiousCellInfoS) cellBarringInfoS.lte_cell_list.get(i3)).cellInfo.activeBand;
                lteDubiousCellInfoS.cellInfo.arfcn = ((LteDubiousCellInfoS) cellBarringInfoS.lte_cell_list.get(i3)).cellInfo.arfcn;
                lteDubiousCellInfoS.cellInfo.pci = (char) ((LteDubiousCellInfoS) cellBarringInfoS.lte_cell_list.get(i3)).cellInfo.pci;
                lteDubiousCellInfoS.cellInfo.causeCodeBitMask = ((LteDubiousCellInfoS) cellBarringInfoS.lte_cell_list.get(i3)).cellInfo.causeCodeBitMask;
                lteDubiousCellInfoS.cgi = ((LteDubiousCellInfoS) cellBarringInfoS.lte_cell_list.get(i3)).cgi;
                cellBarringInfoS2.lte_cell_list[i3] = lteDubiousCellInfoS;
            } catch (Exception e) {
                log("convertToHalCellBarringInfoS = " + e + " value = " + cellBarringInfoS);
                return cellBarringInfoS2;
            }
        }
        for (int i4 = 0; i4 < cellBarringInfoS2.nr_cell_list_len; i4++) {
            vendor.xiaomi.modem.qms.NrDubiousCellInfoS nrDubiousCellInfoS = new vendor.xiaomi.modem.qms.NrDubiousCellInfoS();
            DubiousCellInfoS dubiousCellInfoS2 = new DubiousCellInfoS();
            nrDubiousCellInfoS.cellInfo = dubiousCellInfoS2;
            dubiousCellInfoS2.plmnId = new vendor.xiaomi.modem.qms.PlmnIdS();
            nrDubiousCellInfoS.cellInfo.plmnId.mcc = (char) ((NrDubiousCellInfoS) cellBarringInfoS.nr_cell_list.get(i4)).cellInfo.plmnId.mcc;
            nrDubiousCellInfoS.cellInfo.plmnId.mnc = (char) ((NrDubiousCellInfoS) cellBarringInfoS.nr_cell_list.get(i4)).cellInfo.plmnId.mnc;
            nrDubiousCellInfoS.cellInfo.plmnId.mncIncludesPcsDigit = ((NrDubiousCellInfoS) cellBarringInfoS.nr_cell_list.get(i4)).cellInfo.plmnId.mncIncludesPcsDigit;
            nrDubiousCellInfoS.cellInfo.activeBand = ((NrDubiousCellInfoS) cellBarringInfoS.nr_cell_list.get(i4)).cellInfo.activeBand;
            nrDubiousCellInfoS.cellInfo.arfcn = ((NrDubiousCellInfoS) cellBarringInfoS.nr_cell_list.get(i4)).cellInfo.arfcn;
            nrDubiousCellInfoS.cellInfo.pci = (char) ((NrDubiousCellInfoS) cellBarringInfoS.nr_cell_list.get(i4)).cellInfo.pci;
            nrDubiousCellInfoS.cellInfo.causeCodeBitMask = ((NrDubiousCellInfoS) cellBarringInfoS.nr_cell_list.get(i4)).cellInfo.causeCodeBitMask;
            nrDubiousCellInfoS.cgi = ((NrDubiousCellInfoS) cellBarringInfoS.nr_cell_list.get(i4)).cgi;
            cellBarringInfoS2.nr_cell_list[i4] = nrDubiousCellInfoS;
        }
        return cellBarringInfoS2;
    }

    private static vendor.xiaomi.modem.qms.CellListS convertToHalCellListS(CellListS cellListS) {
        if (cellListS == null) {
            return null;
        }
        vendor.xiaomi.modem.qms.CellListS cellListS2 = new vendor.xiaomi.modem.qms.CellListS();
        cellListS2.mitigation_type = cellListS.mitigation_type;
        cellListS2.reset_flag = cellListS.reset_flag;
        int i = cellListS.lte_cell_list_len;
        cellListS2.lte_cell_list_len = i;
        int i2 = cellListS.nr_cell_list_len;
        cellListS2.nr_cell_list_len = i2;
        cellListS2.lte_cell_list = new CellListInfoS[i];
        cellListS2.nr_cell_list = new CellListInfoS[i2];
        for (int i3 = 0; i3 < cellListS2.lte_cell_list_len; i3++) {
            try {
                CellListInfoS cellListInfoS = new CellListInfoS();
                UniqueCellInfoS uniqueCellInfoS = new UniqueCellInfoS();
                cellListInfoS.cell_info = uniqueCellInfoS;
                uniqueCellInfoS.plmn = new vendor.xiaomi.modem.qms.PlmnIdS();
                CellPropertyCategoryS cellPropertyCategoryS = new CellPropertyCategoryS();
                cellListInfoS.property = cellPropertyCategoryS;
                cellPropertyCategoryS.common = new CellPropertyCategoryCommonS();
                cellListInfoS.property.common.location = new LocationS();
                cellListInfoS.property.hdr = new CellPropertyCategoryHdrS();
                cellListInfoS.property.rs = new CellPropertyCategoryRsS();
                cellListInfoS.cell_info.plmn.mcc = (char) ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).cell_info.plmn.mcc;
                cellListInfoS.cell_info.plmn.mnc = (char) ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).cell_info.plmn.mnc;
                cellListInfoS.cell_info.plmn.mncIncludesPcsDigit = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).cell_info.plmn.mncIncludesPcsDigit;
                cellListInfoS.cell_info.cell_identity = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).cell_info.cell_identity;
                cellListInfoS.cell_info.band = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).cell_info.band;
                cellListInfoS.cell_info.freq = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).cell_info.freq;
                cellListInfoS.cell_info.pci = (char) ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).cell_info.pci;
                cellListInfoS.neighbor_cluster_id = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).neighbor_cluster_id;
                cellListInfoS.anchor_cell = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).anchor_cell;
                cellListInfoS.cause_bitmask = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).cause_bitmask;
                cellListInfoS.expiry_timer = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).expiry_timer;
                cellListInfoS.property.common_valid = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.common_valid;
                cellListInfoS.property.common.priority = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.common.priority;
                cellListInfoS.property.common.loc_valid = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.common.location_valid;
                cellListInfoS.property.common.location.longitude = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.common.location.longitude;
                cellListInfoS.property.common.location.latitude = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.common.location.latitude;
                cellListInfoS.property.hdr_valid = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.hdr_valid;
                cellListInfoS.property.hdr.service_type = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.hdr.service_type;
                cellListInfoS.property.hdr.load = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.hdr.load;
                cellListInfoS.property.hdr.expiry_timer = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.hdr.expiry_timer;
                cellListInfoS.property.rs_valid = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.rs_valid;
                cellListInfoS.property.rs.shared_plmn_len = ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.rs.shared_plmn_len;
                CellPropertyCategoryRsS cellPropertyCategoryRsS = cellListInfoS.property.rs;
                cellPropertyCategoryRsS.shared_plmn = new vendor.xiaomi.modem.qms.PlmnIdS[cellPropertyCategoryRsS.shared_plmn_len];
                int i4 = 0;
                while (true) {
                    CellPropertyCategoryRsS cellPropertyCategoryRsS2 = cellListInfoS.property.rs;
                    if (i4 < cellPropertyCategoryRsS2.shared_plmn_len) {
                        cellPropertyCategoryRsS2.shared_plmn[i4] = new vendor.xiaomi.modem.qms.PlmnIdS();
                        cellListInfoS.property.rs.shared_plmn[i4].mcc = (char) ((PlmnIdS) ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.rs.shared_plmn.get(i4)).mcc;
                        cellListInfoS.property.rs.shared_plmn[i4].mnc = (char) ((PlmnIdS) ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.rs.shared_plmn.get(i4)).mnc;
                        cellListInfoS.property.rs.shared_plmn[i4].mncIncludesPcsDigit = ((PlmnIdS) ((miui.telephony.qms.CellListInfoS) cellListS.lte_cell_list.get(i3)).property.rs.shared_plmn.get(i4)).mncIncludesPcsDigit;
                        i4++;
                    }
                }
                cellListS2.lte_cell_list[i3] = cellListInfoS;
            } catch (Exception e) {
                log("convertToHalCellListS = " + e + " value = " + cellListS);
                return cellListS2;
            }
        }
        for (int i5 = 0; i5 < cellListS2.nr_cell_list_len; i5++) {
            CellListInfoS cellListInfoS2 = new CellListInfoS();
            UniqueCellInfoS uniqueCellInfoS2 = new UniqueCellInfoS();
            cellListInfoS2.cell_info = uniqueCellInfoS2;
            uniqueCellInfoS2.plmn = new vendor.xiaomi.modem.qms.PlmnIdS();
            CellPropertyCategoryS cellPropertyCategoryS2 = new CellPropertyCategoryS();
            cellListInfoS2.property = cellPropertyCategoryS2;
            cellPropertyCategoryS2.common = new CellPropertyCategoryCommonS();
            cellListInfoS2.property.common.location = new LocationS();
            cellListInfoS2.property.hdr = new CellPropertyCategoryHdrS();
            cellListInfoS2.property.rs = new CellPropertyCategoryRsS();
            cellListInfoS2.cell_info.plmn.mcc = (char) ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).cell_info.plmn.mcc;
            cellListInfoS2.cell_info.plmn.mnc = (char) ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).cell_info.plmn.mnc;
            cellListInfoS2.cell_info.plmn.mncIncludesPcsDigit = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).cell_info.plmn.mncIncludesPcsDigit;
            cellListInfoS2.cell_info.cell_identity = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).cell_info.cell_identity;
            cellListInfoS2.cell_info.band = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).cell_info.band;
            cellListInfoS2.cell_info.freq = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).cell_info.freq;
            cellListInfoS2.cell_info.pci = (char) ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).cell_info.pci;
            cellListInfoS2.neighbor_cluster_id = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).neighbor_cluster_id;
            cellListInfoS2.anchor_cell = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).anchor_cell;
            cellListInfoS2.cause_bitmask = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).cause_bitmask;
            cellListInfoS2.expiry_timer = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).expiry_timer;
            cellListInfoS2.property.common_valid = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.common_valid;
            cellListInfoS2.property.common.priority = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.common.priority;
            cellListInfoS2.property.common.loc_valid = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.common.location_valid;
            cellListInfoS2.property.common.location.longitude = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.common.location.longitude;
            cellListInfoS2.property.common.location.latitude = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.common.location.latitude;
            cellListInfoS2.property.hdr_valid = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.hdr_valid;
            cellListInfoS2.property.hdr.service_type = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.hdr.service_type;
            cellListInfoS2.property.hdr.load = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.hdr.load;
            cellListInfoS2.property.hdr.expiry_timer = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.hdr.expiry_timer;
            cellListInfoS2.property.rs_valid = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.rs_valid;
            cellListInfoS2.property.rs.shared_plmn_len = ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.rs.shared_plmn_len;
            CellPropertyCategoryRsS cellPropertyCategoryRsS3 = cellListInfoS2.property.rs;
            cellPropertyCategoryRsS3.shared_plmn = new vendor.xiaomi.modem.qms.PlmnIdS[cellPropertyCategoryRsS3.shared_plmn_len];
            int i6 = 0;
            while (true) {
                CellPropertyCategoryRsS cellPropertyCategoryRsS4 = cellListInfoS2.property.rs;
                if (i6 >= cellPropertyCategoryRsS4.shared_plmn_len) {
                    break;
                }
                cellPropertyCategoryRsS4.shared_plmn[i6] = new vendor.xiaomi.modem.qms.PlmnIdS();
                cellListInfoS2.property.rs.shared_plmn[i6].mcc = (char) ((PlmnIdS) ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.rs.shared_plmn.get(i6)).mcc;
                cellListInfoS2.property.rs.shared_plmn[i6].mnc = (char) ((PlmnIdS) ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.rs.shared_plmn.get(i6)).mnc;
                cellListInfoS2.property.rs.shared_plmn[i6].mncIncludesPcsDigit = ((PlmnIdS) ((miui.telephony.qms.CellListInfoS) cellListS.nr_cell_list.get(i5)).property.rs.shared_plmn.get(i6)).mncIncludesPcsDigit;
                i6++;
            }
            cellListS2.nr_cell_list[i5] = cellListInfoS2;
        }
        return cellListS2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static SetCellListInfoV2S convertToHalSetCellListInfoV2S(miui.telephony.qms.SetCellListInfoV2S setCellListInfoV2S) {
        if (setCellListInfoV2S == null) {
            return null;
        }
        SetCellListInfoV2S setCellListInfoV2S2 = new SetCellListInfoV2S();
        setCellListInfoV2S2.mitigation_type = setCellListInfoV2S.mitigation_type;
        setCellListInfoV2S2.reset_flag = setCellListInfoV2S.reset_flag;
        ArrayList arrayList = setCellListInfoV2S.lte_cell_list;
        int iMin = arrayList == null ? 0 : Math.min((int) setCellListInfoV2S.lte_cell_list_len, arrayList.size());
        ArrayList arrayList2 = setCellListInfoV2S.nr_cell_list;
        int iMin2 = arrayList2 == null ? 0 : Math.min((int) setCellListInfoV2S.nr_cell_list_len, arrayList2.size());
        int i = (byte) iMin;
        setCellListInfoV2S2.lte_cell_list_len = i;
        int i2 = (byte) iMin2;
        setCellListInfoV2S2.nr_cell_list_len = i2;
        setCellListInfoV2S2.lte_cell_list = new SetCellListInfoTypeV2S[i];
        setCellListInfoV2S2.nr_cell_list = new SetCellListInfoTypeV2S[i2];
        for (int i3 = 0; i3 < setCellListInfoV2S2.lte_cell_list_len; i3++) {
            try {
                setCellListInfoV2S2.lte_cell_list[i3] = convertToHalSetCellListInfoTypeV2S((miui.telephony.qms.SetCellListInfoTypeV2S) setCellListInfoV2S.lte_cell_list.get(i3));
            } catch (Exception e) {
                log("convertToHalSetCellListInfoV2S = " + e + " value = " + setCellListInfoV2S);
                return setCellListInfoV2S2;
            }
        }
        for (int i4 = 0; i4 < setCellListInfoV2S2.nr_cell_list_len; i4++) {
            setCellListInfoV2S2.nr_cell_list[i4] = convertToHalSetCellListInfoTypeV2S((miui.telephony.qms.SetCellListInfoTypeV2S) setCellListInfoV2S.nr_cell_list.get(i4));
        }
        return setCellListInfoV2S2;
    }

    private static SetCellListInfoTypeV2S convertToHalSetCellListInfoTypeV2S(miui.telephony.qms.SetCellListInfoTypeV2S setCellListInfoTypeV2S) {
        if (setCellListInfoTypeV2S == null) {
            return null;
        }
        SetCellListInfoTypeV2S setCellListInfoTypeV2S2 = new SetCellListInfoTypeV2S();
        UniqueCellInfoS uniqueCellInfoS = new UniqueCellInfoS();
        setCellListInfoTypeV2S2.cell_info = uniqueCellInfoS;
        uniqueCellInfoS.plmn = new vendor.xiaomi.modem.qms.PlmnIdS();
        CellPropertyCategoryTypeV2S cellPropertyCategoryTypeV2S = new CellPropertyCategoryTypeV2S();
        setCellListInfoTypeV2S2.property = cellPropertyCategoryTypeV2S;
        cellPropertyCategoryTypeV2S.common = new CellPropertyCategoryCommonS();
        setCellListInfoTypeV2S2.property.common.location = new LocationS();
        setCellListInfoTypeV2S2.property.hdr = new CellPropertyCategoryHdrS();
        setCellListInfoTypeV2S2.property.rs = new CellPropertyCategoryRsS();
        setCellListInfoTypeV2S2.property.hst = new CellPropertyCategoryHstTypeS();
        setCellListInfoTypeV2S2.property.hdr_rat = new CellPropertyCategoryHdrRatTypeS();
        try {
            UniqueCellInfoS uniqueCellInfoS2 = setCellListInfoTypeV2S2.cell_info;
            vendor.xiaomi.modem.qms.PlmnIdS plmnIdS = uniqueCellInfoS2.plmn;
            miui.telephony.qms.UniqueCellInfoS uniqueCellInfoS3 = setCellListInfoTypeV2S.cell_info;
            PlmnIdS plmnIdS2 = uniqueCellInfoS3.plmn;
            plmnIdS.mcc = (char) plmnIdS2.mcc;
            plmnIdS.mnc = (char) plmnIdS2.mnc;
            plmnIdS.mncIncludesPcsDigit = plmnIdS2.mncIncludesPcsDigit;
            uniqueCellInfoS2.cell_identity = uniqueCellInfoS3.cell_identity;
            uniqueCellInfoS2.band = uniqueCellInfoS3.band;
            uniqueCellInfoS2.freq = uniqueCellInfoS3.freq;
            uniqueCellInfoS2.pci = (char) uniqueCellInfoS3.pci;
            setCellListInfoTypeV2S2.neighbor_cluster_id = setCellListInfoTypeV2S.neighbor_cluster_id;
            setCellListInfoTypeV2S2.anchor_cell = setCellListInfoTypeV2S.anchor_cell;
            setCellListInfoTypeV2S2.cause_bitmask = setCellListInfoTypeV2S.cause_bitmask;
            setCellListInfoTypeV2S2.expiry_timer = setCellListInfoTypeV2S.expiry_timer;
            CellPropertyCategoryTypeV2S cellPropertyCategoryTypeV2S2 = setCellListInfoTypeV2S2.property;
            miui.telephony.qms.CellPropertyCategoryTypeV2S cellPropertyCategoryTypeV2S3 = setCellListInfoTypeV2S.property;
            cellPropertyCategoryTypeV2S2.common_valid = cellPropertyCategoryTypeV2S3.common_valid;
            CellPropertyCategoryCommonS cellPropertyCategoryCommonS = cellPropertyCategoryTypeV2S2.common;
            miui.telephony.qms.CellPropertyCategoryCommonS cellPropertyCategoryCommonS2 = cellPropertyCategoryTypeV2S3.common;
            cellPropertyCategoryCommonS.priority = cellPropertyCategoryCommonS2.priority;
            cellPropertyCategoryCommonS.loc_valid = cellPropertyCategoryCommonS2.location_valid;
            LocationS locationS = cellPropertyCategoryCommonS.location;
            miui.telephony.qms.LocationS locationS2 = cellPropertyCategoryCommonS2.location;
            locationS.longitude = locationS2.longitude;
            locationS.latitude = locationS2.latitude;
            cellPropertyCategoryTypeV2S2.hdr_valid = cellPropertyCategoryTypeV2S3.hdr_valid;
            CellPropertyCategoryHdrS cellPropertyCategoryHdrS = cellPropertyCategoryTypeV2S2.hdr;
            miui.telephony.qms.CellPropertyCategoryHdrS cellPropertyCategoryHdrS2 = cellPropertyCategoryTypeV2S3.hdr;
            cellPropertyCategoryHdrS.service_type = cellPropertyCategoryHdrS2.service_type;
            cellPropertyCategoryHdrS.load = cellPropertyCategoryHdrS2.load;
            cellPropertyCategoryHdrS.expiry_timer = cellPropertyCategoryHdrS2.expiry_timer;
            cellPropertyCategoryTypeV2S2.rs_valid = cellPropertyCategoryTypeV2S3.rs_valid;
            CellPropertyCategoryRsS cellPropertyCategoryRsS = cellPropertyCategoryTypeV2S2.rs;
            int i = cellPropertyCategoryTypeV2S3.rs.shared_plmn_len;
            cellPropertyCategoryRsS.shared_plmn_len = i;
            cellPropertyCategoryRsS.shared_plmn = new vendor.xiaomi.modem.qms.PlmnIdS[i];
            int i2 = 0;
            while (true) {
                CellPropertyCategoryTypeV2S cellPropertyCategoryTypeV2S4 = setCellListInfoTypeV2S2.property;
                if (i2 < i) {
                    cellPropertyCategoryTypeV2S4.rs.shared_plmn[i2] = new vendor.xiaomi.modem.qms.PlmnIdS();
                    setCellListInfoTypeV2S2.property.rs.shared_plmn[i2].mcc = (char) ((PlmnIdS) setCellListInfoTypeV2S.property.rs.shared_plmn.get(i2)).mcc;
                    setCellListInfoTypeV2S2.property.rs.shared_plmn[i2].mnc = (char) ((PlmnIdS) setCellListInfoTypeV2S.property.rs.shared_plmn.get(i2)).mnc;
                    setCellListInfoTypeV2S2.property.rs.shared_plmn[i2].mncIncludesPcsDigit = ((PlmnIdS) setCellListInfoTypeV2S.property.rs.shared_plmn.get(i2)).mncIncludesPcsDigit;
                    i2++;
                } else {
                    miui.telephony.qms.CellPropertyCategoryTypeV2S cellPropertyCategoryTypeV2S5 = setCellListInfoTypeV2S.property;
                    cellPropertyCategoryTypeV2S4.hst_valid = cellPropertyCategoryTypeV2S5.hst_valid;
                    CellPropertyCategoryHstTypeS cellPropertyCategoryHstTypeS = cellPropertyCategoryTypeV2S4.hst;
                    miui.telephony.qms.CellPropertyCategoryHstTypeS cellPropertyCategoryHstTypeS2 = cellPropertyCategoryTypeV2S5.hst;
                    cellPropertyCategoryHstTypeS.priority = cellPropertyCategoryHstTypeS2.priority;
                    cellPropertyCategoryHstTypeS.expiry_timer = cellPropertyCategoryHstTypeS2.expiry_timer;
                    cellPropertyCategoryTypeV2S4.hdr_rat_valid = cellPropertyCategoryTypeV2S5.hdr_rat_valid;
                    CellPropertyCategoryHdrRatTypeS cellPropertyCategoryHdrRatTypeS = cellPropertyCategoryTypeV2S4.hdr_rat;
                    miui.telephony.qms.CellPropertyCategoryHdrRatTypeS cellPropertyCategoryHdrRatTypeS2 = cellPropertyCategoryTypeV2S5.hdr_rat;
                    cellPropertyCategoryHdrRatTypeS.sub_id_mask = cellPropertyCategoryHdrRatTypeS2.sub_id_mask;
                    cellPropertyCategoryHdrRatTypeS.expiry_timer = cellPropertyCategoryHdrRatTypeS2.expiry_timer;
                    return setCellListInfoTypeV2S2;
                }
            }
        } catch (Exception e) {
            log("convertToHalSetCellListInfoTypeV2S = " + e + " value = " + setCellListInfoTypeV2S);
            return setCellListInfoTypeV2S2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0046 A[Catch: Exception -> 0x0042, LOOP:1: B:19:0x0046->B:22:0x0058, LOOP_START, PHI: r2
  0x0046: PHI (r2v2 int) = (r2v1 int), (r2v3 int) binds: [B:18:0x0044, B:22:0x0058] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {Exception -> 0x0042, blocks: (B:13:0x002d, B:15:0x0031, B:19:0x0046, B:21:0x004a), top: B:25:0x002d }] */
    /* JADX WARN: Code duplicated, block: B:21:0x004a A[Catch: Exception -> 0x0042, TRY_LEAVE, TryCatch #0 {Exception -> 0x0042, blocks: (B:13:0x002d, B:15:0x0031, B:19:0x0046, B:21:0x004a), top: B:25:0x002d }] */
    /* JADX WARN: Multi-variable type inference failed */
    private static SetPreferredFrequencyListS convertToHalSetPreferredFrequencyListS(int i, int i2, int i3, List list, List list2) {
        SetPreferredFrequencyListS setPreferredFrequencyListS = new SetPreferredFrequencyListS();
        setPreferredFrequencyListS.slot_id = i;
        setPreferredFrequencyListS.usage_type = i2;
        setPreferredFrequencyListS.action = i3;
        int size = (byte) (list != null ? list.size() : 0);
        setPreferredFrequencyListS.lte_freq_list_len = size;
        setPreferredFrequencyListS.lte_freq_list = new PreferredFrequencyInfoS[size];
        int size2 = (byte) (list2 != null ? list2.size() : 0);
        setPreferredFrequencyListS.nr_freq_list_len = size2;
        setPreferredFrequencyListS.nr_freq_list = new PreferredFrequencyInfoS[size2];
        if (list != null) {
            for (int i4 = 0; i4 < setPreferredFrequencyListS.lte_freq_list_len; i4++) {
                try {
                    setPreferredFrequencyListS.lte_freq_list[i4] = convertToHalPreferredFrequencyInfoS((miui.telephony.qms.PreferredFrequencyInfoS) list.get(i4));
                } catch (Exception e) {
                    log("convertToHalSetPreferredFrequencyListS = " + e);
                }
            }
            if (list2 != null) {
                for (int i5 = 0; i5 < setPreferredFrequencyListS.nr_freq_list_len; i5++) {
                    setPreferredFrequencyListS.nr_freq_list[i5] = convertToHalPreferredFrequencyInfoS((miui.telephony.qms.PreferredFrequencyInfoS) list2.get(i5));
                }
            }
        } else if (list2 != null) {
            while (i5 < setPreferredFrequencyListS.nr_freq_list_len) {
                setPreferredFrequencyListS.nr_freq_list[i5] = convertToHalPreferredFrequencyInfoS((miui.telephony.qms.PreferredFrequencyInfoS) list2.get(i5));
            }
        }
        return setPreferredFrequencyListS;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static PreferredFrequencyInfoS convertToHalPreferredFrequencyInfoS(miui.telephony.qms.PreferredFrequencyInfoS preferredFrequencyInfoS) {
        if (preferredFrequencyInfoS == null) {
            return null;
        }
        PreferredFrequencyInfoS preferredFrequencyInfoS2 = new PreferredFrequencyInfoS();
        preferredFrequencyInfoS2.freq = preferredFrequencyInfoS.freq;
        preferredFrequencyInfoS2.band = preferredFrequencyInfoS.band;
        preferredFrequencyInfoS2.bandwidth = preferredFrequencyInfoS.bandwidth;
        int i = (byte) preferredFrequencyInfoS.plmn_list_len;
        preferredFrequencyInfoS2.plmn_list_len = i;
        preferredFrequencyInfoS2.plmn = new vendor.xiaomi.modem.qms.PlmnIdS[i];
        for (int i2 = 0; i2 < preferredFrequencyInfoS2.plmn_list_len; i2++) {
            try {
                preferredFrequencyInfoS2.plmn[i2] = new vendor.xiaomi.modem.qms.PlmnIdS();
                preferredFrequencyInfoS2.plmn[i2].mcc = (char) ((PlmnIdS) preferredFrequencyInfoS.plmn.get(i2)).mcc;
                preferredFrequencyInfoS2.plmn[i2].mnc = (char) ((PlmnIdS) preferredFrequencyInfoS.plmn.get(i2)).mnc;
                preferredFrequencyInfoS2.plmn[i2].mncIncludesPcsDigit = ((PlmnIdS) preferredFrequencyInfoS.plmn.get(i2)).mncIncludesPcsDigit;
            } catch (Exception e) {
                log("convertToHalPreferredFrequencyInfoS = " + e + " value = " + preferredFrequencyInfoS);
                return preferredFrequencyInfoS2;
            }
        }
        return preferredFrequencyInfoS2;
    }
}
