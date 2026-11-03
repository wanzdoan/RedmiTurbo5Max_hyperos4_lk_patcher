package vendor.xiaomi.hardware.satellite;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telephony.AsyncService;

/* JADX INFO: loaded from: classes.dex */
public interface INTNRadio extends IInterface {
    public static final String DESCRIPTOR = "vendor$xiaomi$hardware$satellite$INTNRadio".replace('$', '.');

    void acceptCall(int i);

    void changeIccPin2ForApp(int i, String str, String str2, String str3);

    void deactivateDataCall(int i, int i2, boolean z);

    void dial(int i, Dial dial);

    void getAntennaState(int i);

    void getBasebandVersion(int i);

    void getCurrentCalls(int i);

    void getIccCardStatus(int i);

    void getIccidForApp(int i, String str);

    void getImsiForApp(int i, String str);

    int getInterfaceVersion();

    void getLastCallFailCause(int i);

    void getMute(int i);

    void getSARMode(int i);

    void getSignalStrength(int i);

    void getSpeechBitrate(int i);

    void getVoiceRegistrationState(int i);

    void hangup(int i, int i2);

    void queryCurrentSatellteCapability(int i);

    void rejectCall(int i);

    void requestShutdown(int i);

    void responseAcknowledgement();

    void sendBurstDtmf(int i, String str, int i2, int i3);

    void sendDtmf(int i, String str);

    void sendSms(int i, GsmSmsMessage gsmSmsMessage);

    void sendTemp(int i, int i2);

    void sendcustFactAtcmd(int i, int i2, String str);

    void setAntennaReportMode(int i, int i2);

    void setAntennaState(int i, int i2, int i3);

    void setMute(int i, boolean z);

    void setPowerSwitch(int i, boolean z);

    void setPriorityFrequencySearch(int i, int[] iArr);

    void setRFMode(int i, int i2);

    void setRadioPower(int i, boolean z, boolean z2, boolean z3);

    void setResponseFunctions(INTNRadioResponse iNTNRadioResponse, INTNRadioIndication iNTNRadioIndication);

    void setSARMode(int i, int i2);

    void setSatBeam(int i, int i2, int[] iArr);

    void setScreenState(int i, int i2);

    void setSignalStrengthReportingCriteria(int i, int i2, int[] iArr);

    void setSimCardPower(int i, int i2);

    void setSpeechBitrate(int i, String str);

    void setupDataCall(int i, int i2, DataProfileInfo dataProfileInfo, boolean z, boolean z2, boolean z3);

    void startDtmf(int i, String str);

    void stopDtmf(int i);

    void supplyGpsForApp(int i, String str);

    void supplyIccAuthRsp(int i, String str);

    void supplyIccPin2ForApp(int i, String str, String str2);

    void supplyIccPinForApp(int i, String str, String str2);

    void supplyIccPuk2ForApp(int i, String str, String str2, String str3);

    void supplyIccPukForApp(int i, String str, String str2, String str3);

    void supplyImeiForApp(int i, String str);

    void supplyImsiForApp(int i, String str);

    public static abstract class Stub extends Binder implements INTNRadio {
        public static INTNRadio asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(INTNRadio.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof INTNRadio)) {
                return (INTNRadio) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        private static final class Proxy implements INTNRadio {
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

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void acceptCall(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(1, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void changeIccPin2ForApp(int i, String str, String str2, String str3) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeString(str3);
                    if (!this.mRemote.transact(2, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void dial(int i, Dial dial) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(dial, 0);
                    if (!this.mRemote.transact(4, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getBasebandVersion(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(5, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getCurrentCalls(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(6, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getIccCardStatus(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(7, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getIccidForApp(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(8, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getImsiForApp(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(9, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getLastCallFailCause(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(10, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getMute(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(11, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getSARMode(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(12, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getSignalStrength(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(13, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getVoiceRegistrationState(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(14, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void hangup(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(15, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void rejectCall(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(16, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void requestShutdown(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(17, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void responseAcknowledgement() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    if (!this.mRemote.transact(18, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void sendBurstDtmf(int i, String str, int i2, int i3) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeInt(i3);
                    if (!this.mRemote.transact(19, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void sendDtmf(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(20, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void sendSms(int i, GsmSmsMessage gsmSmsMessage) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(gsmSmsMessage, 0);
                    if (!this.mRemote.transact(21, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setMute(int i, boolean z) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeBoolean(z);
                    if (!this.mRemote.transact(22, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setRadioPower(int i, boolean z, boolean z2, boolean z3) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeBoolean(z);
                    parcelObtain.writeBoolean(z2);
                    parcelObtain.writeBoolean(z3);
                    if (!this.mRemote.transact(23, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setResponseFunctions(INTNRadioResponse iNTNRadioResponse, INTNRadioIndication iNTNRadioIndication) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeStrongInterface(iNTNRadioResponse);
                    parcelObtain.writeStrongInterface(iNTNRadioIndication);
                    if (!this.mRemote.transact(24, parcelObtain, parcelObtain2, 0)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain2.readException();
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setSARMode(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(25, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void startDtmf(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(26, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void stopDtmf(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(27, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void supplyIccAuthRsp(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(28, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void supplyIccPin2ForApp(int i, String str, String str2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    if (!this.mRemote.transact(29, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void supplyIccPinForApp(int i, String str, String str2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    if (!this.mRemote.transact(30, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void supplyIccPuk2ForApp(int i, String str, String str2, String str3) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeString(str3);
                    if (!this.mRemote.transact(31, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void supplyIccPukForApp(int i, String str, String str2, String str3) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeString(str3);
                    if (!this.mRemote.transact(32, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void supplyImsiForApp(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(33, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setSignalStrengthReportingCriteria(int i, int i2, int[] iArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(34, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void supplyGpsForApp(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(35, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void sendTemp(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(36, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setScreenState(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(37, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void sendcustFactAtcmd(int i, int i2, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(38, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void supplyImeiForApp(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(43, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setRFMode(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(47, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setSatBeam(int i, int i2, int[] iArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(49, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getSpeechBitrate(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(50, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setSpeechBitrate(int i, String str) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(51, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setPriorityFrequencySearch(int i, int[] iArr) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(54, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setPowerSwitch(int i, boolean z) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeBoolean(z);
                    if (!this.mRemote.transact(55, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setupDataCall(int i, int i2, DataProfileInfo dataProfileInfo, boolean z, boolean z2, boolean z3) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeTypedObject(dataProfileInfo, 0);
                    parcelObtain.writeBoolean(z);
                    parcelObtain.writeBoolean(z2);
                    parcelObtain.writeBoolean(z3);
                    if (!this.mRemote.transact(56, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void deactivateDataCall(int i, int i2, boolean z) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeBoolean(z);
                    if (!this.mRemote.transact(57, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setSimCardPower(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(59, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setAntennaState(int i, int i2, int i3) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeInt(i3);
                    if (!this.mRemote.transact(62, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void getAntennaState(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(63, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void setAntennaReportMode(int i, int i2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    if (!this.mRemote.transact(64, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public void queryCurrentSatellteCapability(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                try {
                    parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(65, parcelObtain, null, 1)) {
                        throw new RemoteException("Unimplemented");
                    }
                    parcelObtain.recycle();
                } catch (Throwable th) {
                    parcelObtain.recycle();
                    throw th;
                }
            }

            @Override // vendor.xiaomi.hardware.satellite.INTNRadio
            public int getInterfaceVersion() {
                if (this.mCachedVersion == -1) {
                    Parcel parcelObtain = Parcel.obtain(asBinder());
                    Parcel parcelObtain2 = Parcel.obtain();
                    try {
                        parcelObtain.writeInterfaceToken(INTNRadio.DESCRIPTOR);
                        this.mRemote.transact(AsyncService.CMD_ASYNC_SERVICE_ON_START_INTENT, parcelObtain, parcelObtain2, 0);
                        parcelObtain2.readException();
                        this.mCachedVersion = parcelObtain2.readInt();
                    } finally {
                        parcelObtain2.recycle();
                        parcelObtain.recycle();
                    }
                }
                return this.mCachedVersion;
            }
        }
    }
}
