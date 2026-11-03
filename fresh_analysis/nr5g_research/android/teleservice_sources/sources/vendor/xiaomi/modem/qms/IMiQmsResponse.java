package vendor.xiaomi.modem.qms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes2.dex */
public interface IMiQmsResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$modem$qms$IMiQmsResponse".replace('$', '.');

    void dppdCapabilityChangeResponse(boolean z);

    void dppdCapabilityStatusEvResponse(int i, int i2, boolean z);

    void dppdMultiSimSubmodeInfoChangeResponse(SubmodeInfoS submodeInfoS);

    void dppdMultiSimSubmodeInfoStatusEvResponse(int i, int i2, SubmodeInfoS submodeInfoS);

    void dppdRecommendationChangeResponse(DataRecommendationS dataRecommendationS);

    void dppdRecommendationStatusEvResponse(int i, int i2, DataRecommendationS dataRecommendationS);

    void dppdUiStatusEvResponse(int i, int i2, boolean z);

    void getHstStateRespResponse(int i, int i2, int i3);

    String getInterfaceHash();

    int getInterfaceVersion();

    void getNfListStatusEvResponse(int i, int i2, int i3, Nr5gNflEntryTypeS[] nr5gNflEntryTypeSArr);

    void hstStateReportResponse(int i);

    void qmsSubwayStateReportResponse(int i);

    void serviceStatusResponse(int i);

    void setCellListInfoStatusEvResponse(int i, int i2);

    void setCellListInfoV2CbResponse(long j, int i);

    void setHlosBorderCrossedStatusEvResponse(long j, int i);

    void setPreferredFrequencyListStatusEvResponse(long j, int i);

    void statusEvResponse(int i, int i2);

    void updateGoodLinkStateStatusEvResponse(long j, int i);

    public abstract class Stub extends Binder implements IMiQmsResponse {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            markVintfStability();
            attachInterface(this, IMiQmsResponse.DESCRIPTOR);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IMiQmsResponse.DESCRIPTOR);
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
                    boolean z = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    dppdCapabilityChangeResponse(z);
                    return true;
                case 2:
                    int i3 = parcel.readInt();
                    int i4 = parcel.readInt();
                    boolean z2 = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    dppdCapabilityStatusEvResponse(i3, i4, z2);
                    return true;
                case 3:
                    SubmodeInfoS submodeInfoS = (SubmodeInfoS) parcel.readTypedObject(SubmodeInfoS.CREATOR);
                    parcel.enforceNoDataAvail();
                    dppdMultiSimSubmodeInfoChangeResponse(submodeInfoS);
                    return true;
                case 4:
                    int i5 = parcel.readInt();
                    int i6 = parcel.readInt();
                    SubmodeInfoS submodeInfoS2 = (SubmodeInfoS) parcel.readTypedObject(SubmodeInfoS.CREATOR);
                    parcel.enforceNoDataAvail();
                    dppdMultiSimSubmodeInfoStatusEvResponse(i5, i6, submodeInfoS2);
                    return true;
                case 5:
                    DataRecommendationS dataRecommendationS = (DataRecommendationS) parcel.readTypedObject(DataRecommendationS.CREATOR);
                    parcel.enforceNoDataAvail();
                    dppdRecommendationChangeResponse(dataRecommendationS);
                    return true;
                case 6:
                    int i7 = parcel.readInt();
                    int i8 = parcel.readInt();
                    DataRecommendationS dataRecommendationS2 = (DataRecommendationS) parcel.readTypedObject(DataRecommendationS.CREATOR);
                    parcel.enforceNoDataAvail();
                    dppdRecommendationStatusEvResponse(i7, i8, dataRecommendationS2);
                    return true;
                case 7:
                    int i9 = parcel.readInt();
                    int i10 = parcel.readInt();
                    boolean z3 = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    dppdUiStatusEvResponse(i9, i10, z3);
                    return true;
                case 8:
                    int i11 = parcel.readInt();
                    int i12 = parcel.readInt();
                    int i13 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    getHstStateRespResponse(i11, i12, i13);
                    return true;
                case 9:
                    int i14 = parcel.readInt();
                    int i15 = parcel.readInt();
                    int i16 = parcel.readInt();
                    Nr5gNflEntryTypeS[] nr5gNflEntryTypeSArr = (Nr5gNflEntryTypeS[]) parcel.createTypedArray(Nr5gNflEntryTypeS.CREATOR);
                    parcel.enforceNoDataAvail();
                    getNfListStatusEvResponse(i14, i15, i16, nr5gNflEntryTypeSArr);
                    return true;
                case 10:
                    int i17 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    hstStateReportResponse(i17);
                    return true;
                case 11:
                    int i18 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    serviceStatusResponse(i18);
                    return true;
                case 12:
                    int i19 = parcel.readInt();
                    int i20 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    statusEvResponse(i19, i20);
                    return true;
                case 13:
                    int i21 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    qmsSubwayStateReportResponse(i21);
                    return true;
                case 14:
                    int i22 = parcel.readInt();
                    int i23 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    setCellListInfoStatusEvResponse(i22, i23);
                    return true;
                case 15:
                    long j = parcel.readLong();
                    int i24 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    setCellListInfoV2CbResponse(j, i24);
                    return true;
                case 16:
                    long j2 = parcel.readLong();
                    int i25 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    updateGoodLinkStateStatusEvResponse(j2, i25);
                    return true;
                case 17:
                    long j3 = parcel.readLong();
                    int i26 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    setPreferredFrequencyListStatusEvResponse(j3, i26);
                    return true;
                case 18:
                    long j4 = parcel.readLong();
                    int i27 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    setHlosBorderCrossedStatusEvResponse(j4, i27);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }
}
