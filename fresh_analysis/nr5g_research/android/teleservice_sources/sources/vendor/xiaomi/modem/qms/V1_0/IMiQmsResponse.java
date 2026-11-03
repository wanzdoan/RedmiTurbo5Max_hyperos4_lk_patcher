package vendor.xiaomi.modem.qms.V1_0;

import android.internal.hidl.base.V1_0.DebugInfo;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import java.util.ArrayList;
import java.util.Arrays;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public interface IMiQmsResponse extends IHwInterface {
    IHwBinder asBinder();

    void dppdCapabilityChangeResponse(boolean z);

    void dppdCapabilityStatusEvResponse(int i, int i2, boolean z);

    void dppdMultiSimSubmodeInfoChangeResponse(SubmodeInfoS submodeInfoS);

    void dppdMultiSimSubmodeInfoStatusEvResponse(int i, int i2, SubmodeInfoS submodeInfoS);

    void dppdRecommendationChangeResponse(DataRecommendationS dataRecommendationS);

    void dppdRecommendationStatusEvResponse(int i, int i2, DataRecommendationS dataRecommendationS);

    void dppdUiStatusEvResponse(int i, int i2, boolean z);

    void getHstStateRespResponse(int i, int i2, int i3);

    void getNfListStatusEvResponse(int i, int i2, int i3, ArrayList arrayList);

    void hstStateReportResponse(int i);

    void serviceStatusResponse(int i);

    void statusEvResponse(int i, int i2);

    public abstract class Stub extends HwBinder implements IMiQmsResponse {
        @Override // vendor.xiaomi.modem.qms.V1_0.IMiQmsResponse
        public IHwBinder asBinder() {
            return this;
        }

        public void debug(NativeHandle nativeHandle, ArrayList arrayList) {
        }

        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        public final void ping() {
        }

        public final void setHALInstrumentation() {
        }

        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        public final ArrayList interfaceChain() {
            return new ArrayList(Arrays.asList("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse", "android.hidl.base@1.0::IBase"));
        }

        public final String interfaceDescriptor() {
            return "vendor.xiaomi.modem.qms@1.0::IMiQmsResponse";
        }

        public final ArrayList getHashChain() {
            return new ArrayList(Arrays.asList(new byte[]{99, 87, -112, 76, 1, -118, 35, -43, 25, -76, -45, -116, 33, 47, -124, 109, -81, -67, 87, -87, 100, 46, 112, -38, -66, 12, -126, 47, 98, -81, 106, 34}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        public final DebugInfo getDebugInfo() {
            DebugInfo debugInfo = new DebugInfo();
            debugInfo.pid = HidlSupport.getPidIfSharable();
            debugInfo.ptr = 0L;
            debugInfo.arch = 0;
            return debugInfo;
        }

        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public IHwInterface queryLocalInterface(String str) {
            if ("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse".equals(str)) {
                return this;
            }
            return null;
        }

        public String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        public void onTransact(int i, HwParcel hwParcel, HwParcel hwParcel2, int i2) {
            switch (i) {
                case 1:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    serviceStatusResponse(hwParcel.readInt32());
                    break;
                case 2:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    statusEvResponse(hwParcel.readInt32(), hwParcel.readInt32());
                    break;
                case 3:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    getNfListStatusEvResponse(hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readInt32(), Nr5gNflEntryTypeS.readVectorFromParcel(hwParcel));
                    break;
                case 4:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    dppdUiStatusEvResponse(hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readBool());
                    break;
                case 5:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    dppdCapabilityStatusEvResponse(hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readBool());
                    break;
                case 6:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    dppdRecommendationStatusEvResponse(hwParcel.readInt32(), hwParcel.readInt32(), DataRecommendationS.readFromParcel(hwParcel));
                    break;
                case 7:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    dppdCapabilityChangeResponse(hwParcel.readBool());
                    break;
                case 8:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    dppdRecommendationChangeResponse(DataRecommendationS.readFromParcel(hwParcel));
                    break;
                case 9:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    hstStateReportResponse(hwParcel.readInt32());
                    break;
                case 10:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    getHstStateRespResponse(hwParcel.readInt32(), hwParcel.readInt32(), hwParcel.readInt32());
                    break;
                case 11:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    dppdMultiSimSubmodeInfoStatusEvResponse(hwParcel.readInt32(), hwParcel.readInt32(), SubmodeInfoS.readFromParcel(hwParcel));
                    break;
                case 12:
                    hwParcel.enforceInterface("vendor.xiaomi.modem.qms@1.0::IMiQmsResponse");
                    dppdMultiSimSubmodeInfoChangeResponse(SubmodeInfoS.readFromParcel(hwParcel));
                    break;
                default:
                    switch (i) {
                        case 256067662:
                            hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                            ArrayList arrayListInterfaceChain = interfaceChain();
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeStringVector(arrayListInterfaceChain);
                            hwParcel2.send();
                            break;
                        case 256131655:
                            hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                            debug(hwParcel.readNativeHandle(), hwParcel.readStringVector());
                            hwParcel2.writeStatus(0);
                            hwParcel2.send();
                            break;
                        case 256136003:
                            hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                            String strInterfaceDescriptor = interfaceDescriptor();
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeString(strInterfaceDescriptor);
                            hwParcel2.send();
                            break;
                        case 256398152:
                            hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                            ArrayList hashChain = getHashChain();
                            hwParcel2.writeStatus(0);
                            HwBlob hwBlob = new HwBlob(16);
                            int size = hashChain.size();
                            hwBlob.putInt32(8L, size);
                            hwBlob.putBool(12L, false);
                            HwBlob hwBlob2 = new HwBlob(size * 32);
                            for (int i3 = 0; i3 < size; i3++) {
                                long j = i3 * 32;
                                byte[] bArr = (byte[]) hashChain.get(i3);
                                if (bArr == null || bArr.length != 32) {
                                    Folme$$ExternalSyntheticBUOutline0.m("Array element is not of the expected length");
                                } else {
                                    hwBlob2.putInt8Array(j, bArr);
                                }
                                break;
                            }
                            hwBlob.putBlob(0L, hwBlob2);
                            hwParcel2.writeBuffer(hwBlob);
                            hwParcel2.send();
                            break;
                        case 256462420:
                            hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                            setHALInstrumentation();
                            break;
                        case 256921159:
                            hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                            ping();
                            hwParcel2.writeStatus(0);
                            hwParcel2.send();
                            break;
                        case 257049926:
                            hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                            DebugInfo debugInfo = getDebugInfo();
                            hwParcel2.writeStatus(0);
                            debugInfo.writeToParcel(hwParcel2);
                            hwParcel2.send();
                            break;
                        case 257120595:
                            hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                            notifySyspropsChanged();
                            break;
                    }
                    break;
            }
        }
    }
}
