package com.xiaomi.bdsms;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IBDSMSservice extends IInterface {
    void SetCallback(IBDSMSCallback iBDSMSCallback);

    int checkEphData();

    void interruptSendingFlow();

    int preLoad(int i);

    int queryAllowedReceiveTime();

    int queryAllowedSendTime();

    int queryRemainMsgCount(int i);

    int queryServiceStatus(int i);

    int receiveBDMessage(String str, int i);

    int sendBDAudio(int i, Uri uri, Uri uri2, String str, int i2, List list);

    int sendBDAudioWithLocation(int i, Uri uri, Uri uri2, String str, int i2, List list, double d, double d2);

    int sendBDMessage(int i, String str, String str2, int i2, List list);

    int sendBDMessageWithLocation(int i, String str, String str2, int i2, List list, double d, double d2);

    int sendBDPicture(int i, Uri uri, Uri uri2, String str, int i2, List list);

    int sendBDPictureWithLocation(int i, Uri uri, Uri uri2, String str, int i2, List list, double d, double d2);

    void syncBDMail(int i);

    void updateAuthKey(int i);

    void updateEphData(int i);

    public abstract class Stub extends Binder implements IBDSMSservice {
        public static IBDSMSservice asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.xiaomi.bdsms.IBDSMSservice");
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IBDSMSservice)) {
                return (IBDSMSservice) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        final class Proxy implements IBDSMSservice {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public void SetCallback(IBDSMSCallback iBDSMSCallback) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeStrongInterface(iBDSMSCallback);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int queryServiceStatus(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int sendBDAudioWithLocation(int i, Uri uri, Uri uri2, String str, int i2, List list, double d, double d2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(uri, 0);
                    parcelObtain.writeTypedObject(uri2, 0);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeStringList(list);
                    parcelObtain.writeDouble(d);
                    parcelObtain.writeDouble(d2);
                    this.mRemote.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int sendBDAudio(int i, Uri uri, Uri uri2, String str, int i2, List list) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(uri, 0);
                    parcelObtain.writeTypedObject(uri2, 0);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeStringList(list);
                    this.mRemote.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int sendBDPictureWithLocation(int i, Uri uri, Uri uri2, String str, int i2, List list, double d, double d2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(uri, 0);
                    parcelObtain.writeTypedObject(uri2, 0);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeStringList(list);
                    parcelObtain.writeDouble(d);
                    parcelObtain.writeDouble(d2);
                    this.mRemote.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int sendBDPicture(int i, Uri uri, Uri uri2, String str, int i2, List list) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    parcelObtain.writeTypedObject(uri, 0);
                    parcelObtain.writeTypedObject(uri2, 0);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeStringList(list);
                    this.mRemote.transact(8, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int sendBDMessageWithLocation(int i, String str, String str2, int i2, List list, double d, double d2) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeStringList(list);
                    parcelObtain.writeDouble(d);
                    parcelObtain.writeDouble(d2);
                    this.mRemote.transact(9, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int sendBDMessage(int i, String str, String str2, int i2, List list) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeStringList(list);
                    this.mRemote.transact(10, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int receiveBDMessage(String str, int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(11, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public void interruptSendingFlow() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    this.mRemote.transact(12, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int queryAllowedSendTime() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    this.mRemote.transact(13, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int queryAllowedReceiveTime() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    this.mRemote.transact(14, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int queryRemainMsgCount(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(15, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public void syncBDMail(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(16, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public void updateAuthKey(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(18, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public void updateEphData(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(19, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int checkEphData() {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    this.mRemote.transact(20, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.xiaomi.bdsms.IBDSMSservice
            public int preLoad(int i) {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.xiaomi.bdsms.IBDSMSservice");
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(21, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
