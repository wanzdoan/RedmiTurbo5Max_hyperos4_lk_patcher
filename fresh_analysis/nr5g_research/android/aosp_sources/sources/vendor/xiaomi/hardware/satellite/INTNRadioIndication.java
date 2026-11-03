package vendor.xiaomi.hardware.satellite;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telephony.AsyncService;

/* JADX INFO: loaded from: classes.dex */
public interface INTNRadioIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$hardware$satellite$INTNRadioIndication".replace('$', '.');

    void BandFrequency(int i, int i2, int i3);

    void EPHEMReport(int i, EPHEM ephem);

    void ETWSReport(int i, String str);

    void ImsRegistrationState(int i, int i2);

    void SnrReport(int i, int i2);

    void authQuery(int i, String str);

    void callRing(int i);

    void callStateChanged(int i);

    void currentAntInfoSignalStrength(int i, AntInfoSignalStrength antInfoSignalStrength);

    void currentSignalStrength(int i, SignalStrength signalStrength);

    void dataCallListChanged(int i, SetupDataCallResult setupDataCallResult);

    void ephemCalcRequest(int i, EphemCalcInfo ephemCalcInfo);

    String getInterfaceHash();

    int getInterfaceVersion();

    void gpsQuery(int i);

    void hwaCPSTReport(int i, HWA_CPST hwa_cpst);

    void hwaTraceReport(int i, HWA_Trace hWA_Trace);

    void imeiQuery(int i);

    void imsiQuery(int i);

    void minRangeForGPS(int i, int i2);

    void moDisconnectCause(int i, int i2);

    void networkStateChanged(int i);

    void newSms(int i, byte[] bArr);

    void newSmsSendResult(int i, SmsSendResult smsSendResult);

    void newSmsStatusReport(int i, SmsStatusReport smsStatusReport);

    void nitzTimeReceived(int i, String str, int i2);

    void ntnPosInfo(int i, NtnPosInfo ntnPosInfo);

    void ntnTtlogInfo(int i, NtnTtlogInfo ntnTtlogInfo);

    void onChannelStateChanged(int i, int i2);

    void otaStateChanged(int i, int i2);

    void radioStateChanged(int i, int i2);

    void requestStartEPHEMResult(int i, int i2, int i3);

    void requestStopEPHEMResult(int i, int i2);

    void simStatusChanged(int i);

    void stackErrorEventNotify(int i, HLS_ERROR_INFO hls_error_info);

    void txPwrReport(int i, JS_TXPWR js_txpwr);

    void unsolHighPenetrationStatusChanged(int i, int i2, int i3);

    void unsolNtnCdcMsg(int i, int i2, byte[] bArr);

    void voiceControlClose(int i, int i2);

    void voiceControlOpen(int i, int i2);

    void voiceVersionQuery(int i, int i2);

    public static abstract class Stub extends Binder implements INTNRadioIndication {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, INTNRadioIndication.DESCRIPTOR);
        }

        public static INTNRadioIndication asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(INTNRadioIndication.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof INTNRadioIndication)) {
                return (INTNRadioIndication) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "authQuery";
                case 2:
                    return "callRing";
                case 3:
                    return "callStateChanged";
                case 4:
                    return "currentSignalStrength";
                case 5:
                    return "hwaCPSTReport";
                case 6:
                    return "hwaTraceReport";
                case 7:
                    return "imsiQuery";
                case 8:
                    return "networkStateChanged";
                case 9:
                    return "newSms";
                case 10:
                    return "radioStateChanged";
                case 11:
                    return "simStatusChanged";
                case 12:
                    return "txPwrReport";
                case 13:
                    return "newSmsStatusReport";
                case 14:
                    return "newSmsSendResult";
                case 15:
                    return "SnrReport";
                case 16:
                    return "gpsQuery";
                case 17:
                    return "minRangeForGPS";
                case 18:
                    return "stackErrorEventNotify";
                case 19:
                    return "imeiQuery";
                case 20:
                    return "nitzTimeReceived";
                case 21:
                    return "moDisconnectCause";
                case 22:
                    return "voiceControlOpen";
                case 23:
                    return "voiceControlClose";
                case 24:
                    return "ImsRegistrationState";
                case 25:
                    return "BandFrequency";
                case 26:
                    return "voiceVersionQuery";
                case 27:
                    return "ETWSReport";
                case 28:
                    return "EPHEMReport";
                case 29:
                    return "requestStartEPHEMResult";
                case 30:
                    return "requestStopEPHEMResult";
                case 31:
                    return "ntnTtlogInfo";
                case 32:
                    return "ntnPosInfo";
                case 33:
                    return "dataCallListChanged";
                case 34:
                    return "onChannelStateChanged";
                case 35:
                    return "ephemCalcRequest";
                case 36:
                    return "currentAntInfoSignalStrength";
                case 37:
                    return "unsolHighPenetrationStatusChanged";
                case 38:
                    return "otaStateChanged";
                case 39:
                    return "unsolNtnCdcMsg";
                default:
                    switch (i) {
                        case 16777214:
                            return "getInterfaceHash";
                        case AsyncService.CMD_ASYNC_SERVICE_ON_START_INTENT /* 16777215 */:
                            return "getInterfaceVersion";
                        default:
                            return null;
                    }
            }
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INTNRadioIndication.DESCRIPTOR);
            }
            if (i == 16777215) {
                parcel2.writeNoException();
                parcel2.writeInt(getInterfaceVersion());
                return true;
            }
            if (i == 16777214) {
                parcel2.writeNoException();
                parcel2.writeString(getInterfaceHash());
                return true;
            }
            switch (i) {
                case 1:
                    int i3 = parcel.readInt();
                    String string = parcel.readString();
                    parcel.enforceNoDataAvail();
                    authQuery(i3, string);
                    return true;
                case 2:
                    int i4 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    callRing(i4);
                    return true;
                case 3:
                    int i5 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    callStateChanged(i5);
                    return true;
                case 4:
                    int i6 = parcel.readInt();
                    SignalStrength signalStrength = (SignalStrength) parcel.readTypedObject(SignalStrength.CREATOR);
                    parcel.enforceNoDataAvail();
                    currentSignalStrength(i6, signalStrength);
                    return true;
                case 5:
                    int i7 = parcel.readInt();
                    HWA_CPST hwa_cpst = (HWA_CPST) parcel.readTypedObject(HWA_CPST.CREATOR);
                    parcel.enforceNoDataAvail();
                    hwaCPSTReport(i7, hwa_cpst);
                    return true;
                case 6:
                    int i8 = parcel.readInt();
                    HWA_Trace hWA_Trace = (HWA_Trace) parcel.readTypedObject(HWA_Trace.CREATOR);
                    parcel.enforceNoDataAvail();
                    hwaTraceReport(i8, hWA_Trace);
                    return true;
                case 7:
                    int i9 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    imsiQuery(i9);
                    return true;
                case 8:
                    int i10 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    networkStateChanged(i10);
                    return true;
                case 9:
                    int i11 = parcel.readInt();
                    byte[] bArrCreateByteArray = parcel.createByteArray();
                    parcel.enforceNoDataAvail();
                    newSms(i11, bArrCreateByteArray);
                    return true;
                case 10:
                    int i12 = parcel.readInt();
                    int i13 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    radioStateChanged(i12, i13);
                    return true;
                case 11:
                    int i14 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    simStatusChanged(i14);
                    return true;
                case 12:
                    int i15 = parcel.readInt();
                    JS_TXPWR js_txpwr = (JS_TXPWR) parcel.readTypedObject(JS_TXPWR.CREATOR);
                    parcel.enforceNoDataAvail();
                    txPwrReport(i15, js_txpwr);
                    return true;
                case 13:
                    int i16 = parcel.readInt();
                    SmsStatusReport smsStatusReport = (SmsStatusReport) parcel.readTypedObject(SmsStatusReport.CREATOR);
                    parcel.enforceNoDataAvail();
                    newSmsStatusReport(i16, smsStatusReport);
                    return true;
                case 14:
                    int i17 = parcel.readInt();
                    SmsSendResult smsSendResult = (SmsSendResult) parcel.readTypedObject(SmsSendResult.CREATOR);
                    parcel.enforceNoDataAvail();
                    newSmsSendResult(i17, smsSendResult);
                    return true;
                case 15:
                    int i18 = parcel.readInt();
                    int i19 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    SnrReport(i18, i19);
                    return true;
                case 16:
                    int i20 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    gpsQuery(i20);
                    return true;
                case 17:
                    int i21 = parcel.readInt();
                    int i22 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    minRangeForGPS(i21, i22);
                    return true;
                case 18:
                    int i23 = parcel.readInt();
                    HLS_ERROR_INFO hls_error_info = (HLS_ERROR_INFO) parcel.readTypedObject(HLS_ERROR_INFO.CREATOR);
                    parcel.enforceNoDataAvail();
                    stackErrorEventNotify(i23, hls_error_info);
                    return true;
                case 19:
                    int i24 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    imeiQuery(i24);
                    return true;
                case 20:
                    int i25 = parcel.readInt();
                    String string2 = parcel.readString();
                    int i26 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    nitzTimeReceived(i25, string2, i26);
                    return true;
                case 21:
                    int i27 = parcel.readInt();
                    int i28 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    moDisconnectCause(i27, i28);
                    return true;
                case 22:
                    int i29 = parcel.readInt();
                    int i30 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    voiceControlOpen(i29, i30);
                    return true;
                case 23:
                    int i31 = parcel.readInt();
                    int i32 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    voiceControlClose(i31, i32);
                    return true;
                case 24:
                    int i33 = parcel.readInt();
                    int i34 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    ImsRegistrationState(i33, i34);
                    return true;
                case 25:
                    int i35 = parcel.readInt();
                    int i36 = parcel.readInt();
                    int i37 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    BandFrequency(i35, i36, i37);
                    return true;
                case 26:
                    int i38 = parcel.readInt();
                    int i39 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    voiceVersionQuery(i38, i39);
                    return true;
                case 27:
                    int i40 = parcel.readInt();
                    String string3 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    ETWSReport(i40, string3);
                    return true;
                case 28:
                    int i41 = parcel.readInt();
                    EPHEM ephem = (EPHEM) parcel.readTypedObject(EPHEM.CREATOR);
                    parcel.enforceNoDataAvail();
                    EPHEMReport(i41, ephem);
                    return true;
                case 29:
                    int i42 = parcel.readInt();
                    int i43 = parcel.readInt();
                    int i44 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    requestStartEPHEMResult(i42, i43, i44);
                    return true;
                case 30:
                    int i45 = parcel.readInt();
                    int i46 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    requestStopEPHEMResult(i45, i46);
                    return true;
                case 31:
                    int i47 = parcel.readInt();
                    NtnTtlogInfo ntnTtlogInfo = (NtnTtlogInfo) parcel.readTypedObject(NtnTtlogInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    ntnTtlogInfo(i47, ntnTtlogInfo);
                    return true;
                case 32:
                    int i48 = parcel.readInt();
                    NtnPosInfo ntnPosInfo = (NtnPosInfo) parcel.readTypedObject(NtnPosInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    ntnPosInfo(i48, ntnPosInfo);
                    return true;
                case 33:
                    int i49 = parcel.readInt();
                    SetupDataCallResult setupDataCallResult = (SetupDataCallResult) parcel.readTypedObject(SetupDataCallResult.CREATOR);
                    parcel.enforceNoDataAvail();
                    dataCallListChanged(i49, setupDataCallResult);
                    return true;
                case 34:
                    int i50 = parcel.readInt();
                    int i51 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onChannelStateChanged(i50, i51);
                    return true;
                case 35:
                    int i52 = parcel.readInt();
                    EphemCalcInfo ephemCalcInfo = (EphemCalcInfo) parcel.readTypedObject(EphemCalcInfo.CREATOR);
                    parcel.enforceNoDataAvail();
                    ephemCalcRequest(i52, ephemCalcInfo);
                    return true;
                case 36:
                    int i53 = parcel.readInt();
                    AntInfoSignalStrength antInfoSignalStrength = (AntInfoSignalStrength) parcel.readTypedObject(AntInfoSignalStrength.CREATOR);
                    parcel.enforceNoDataAvail();
                    currentAntInfoSignalStrength(i53, antInfoSignalStrength);
                    return true;
                case 37:
                    int i54 = parcel.readInt();
                    int i55 = parcel.readInt();
                    int i56 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    unsolHighPenetrationStatusChanged(i54, i55, i56);
                    return true;
                case 38:
                    int i57 = parcel.readInt();
                    int i58 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    otaStateChanged(i57, i58);
                    return true;
                case 39:
                    int i59 = parcel.readInt();
                    int i60 = parcel.readInt();
                    byte[] bArrCreateByteArray2 = parcel.createByteArray();
                    parcel.enforceNoDataAvail();
                    unsolNtnCdcMsg(i59, i60, bArrCreateByteArray2);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static final class Proxy implements INTNRadioIndication {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }
        }
    }
}
