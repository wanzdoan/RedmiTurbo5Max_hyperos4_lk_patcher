package com.xiaomi.bdsms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface IBDSMSCallback extends IInterface {
    void BDnotifyEnableBDSms(boolean z);

    void BDnotifyEphStatus(boolean z, int i, int i2);

    void BDnotifySateInfo(int i, int i2, int i3, int i4, int i5);

    void BDnotifySendStatus(int i, int i2, int i3, int i4, String str, int i5);

    void BDnotifySysStatus(int i, int i2);

    void BDnotifyUpdateAuthKey(boolean z, int i, int i2);

    void BDnotifyUpdateEphData(boolean z, int i, int i2);

    void BDreceiveMessage(boolean z, int i, String str, String str2, String str3, int i2, boolean z2);

    public abstract class Stub extends Binder implements IBDSMSCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.xiaomi.bdsms.IBDSMSCallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.xiaomi.bdsms.IBDSMSCallback");
            }
            switch (i) {
                case 1:
                    int i3 = parcel.readInt();
                    int i4 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    BDnotifySysStatus(i3, i4);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    int i5 = parcel.readInt();
                    int i6 = parcel.readInt();
                    int i7 = parcel.readInt();
                    int i8 = parcel.readInt();
                    int i9 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    BDnotifySateInfo(i5, i6, i7, i8, i9);
                    parcel2.writeNoException();
                    return true;
                case 3:
                    int i10 = parcel.readInt();
                    int i11 = parcel.readInt();
                    int i12 = parcel.readInt();
                    int i13 = parcel.readInt();
                    String string = parcel.readString();
                    int i14 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    BDnotifySendStatus(i10, i11, i12, i13, string, i14);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    boolean z = parcel.readBoolean();
                    int i15 = parcel.readInt();
                    String string2 = parcel.readString();
                    String string3 = parcel.readString();
                    String string4 = parcel.readString();
                    int i16 = parcel.readInt();
                    boolean z2 = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    BDreceiveMessage(z, i15, string2, string3, string4, i16, z2);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    boolean z3 = parcel.readBoolean();
                    int i17 = parcel.readInt();
                    int i18 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    BDnotifyEphStatus(z3, i17, i18);
                    parcel2.writeNoException();
                    return true;
                case 6:
                    boolean z4 = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    BDnotifyEnableBDSms(z4);
                    parcel2.writeNoException();
                    return true;
                case 7:
                    boolean z5 = parcel.readBoolean();
                    int i19 = parcel.readInt();
                    int i20 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    BDnotifyUpdateAuthKey(z5, i19, i20);
                    parcel2.writeNoException();
                    return true;
                case 8:
                    boolean z6 = parcel.readBoolean();
                    int i21 = parcel.readInt();
                    int i22 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    BDnotifyUpdateEphData(z6, i21, i22);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }
}
