package com.android.internal.telephony;

import android.content.ContentValues;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telephony.uicc.AdnCapacity;
import com.android.internal.telephony.uicc.AdnRecord;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IIccPhoneBook extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.IIccPhoneBook";

    public static class Default implements IIccPhoneBook {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public AdnCapacity getAdnRecordsCapacity() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public AdnCapacity getAdnRecordsCapacityForSubscriber(int i) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public List<AdnRecord> getAdnRecordsInEf(int i) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public List<AdnRecord> getAdnRecordsInEfForSubscriber(int i, int i2) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public int[] getAdnRecordsSize(int i) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public int[] getAdnRecordsSizeForSubscriber(int i, int i2) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public boolean updateAdnRecordsInEfByIndexForSubscriber(int i, int i2, ContentValues contentValues, int i3, String str) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public boolean updateAdnRecordsInEfBySearch(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IIccPhoneBook
        public boolean updateAdnRecordsInEfBySearchForSubscriber(int i, int i2, ContentValues contentValues, String str) throws RemoteException {
            return false;
        }
    }

    AdnCapacity getAdnRecordsCapacity() throws RemoteException;

    AdnCapacity getAdnRecordsCapacityForSubscriber(int i) throws RemoteException;

    List<AdnRecord> getAdnRecordsInEf(int i) throws RemoteException;

    List<AdnRecord> getAdnRecordsInEfForSubscriber(int i, int i2) throws RemoteException;

    int[] getAdnRecordsSize(int i) throws RemoteException;

    int[] getAdnRecordsSizeForSubscriber(int i, int i2) throws RemoteException;

    boolean updateAdnRecordsInEfByIndexForSubscriber(int i, int i2, ContentValues contentValues, int i3, String str) throws RemoteException;

    boolean updateAdnRecordsInEfBySearch(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    boolean updateAdnRecordsInEfBySearchForSubscriber(int i, int i2, ContentValues contentValues, String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IIccPhoneBook {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IIccPhoneBook.DESCRIPTOR);
        }

        public static IIccPhoneBook asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(IIccPhoneBook.DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IIccPhoneBook)) {
                return (IIccPhoneBook) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IIccPhoneBook.DESCRIPTOR);
            }
            switch (i) {
                case 1:
                    int i3 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    List<AdnRecord> adnRecordsInEf = getAdnRecordsInEf(i3);
                    parcel2.writeNoException();
                    parcel2.writeTypedList(adnRecordsInEf, 1);
                    return true;
                case 2:
                    int i4 = parcel.readInt();
                    int i5 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    List<AdnRecord> adnRecordsInEfForSubscriber = getAdnRecordsInEfForSubscriber(i4, i5);
                    parcel2.writeNoException();
                    parcel2.writeTypedList(adnRecordsInEfForSubscriber, 1);
                    return true;
                case 3:
                    int i6 = parcel.readInt();
                    String string = parcel.readString();
                    String string2 = parcel.readString();
                    String string3 = parcel.readString();
                    String string4 = parcel.readString();
                    String string5 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    boolean zUpdateAdnRecordsInEfBySearch = updateAdnRecordsInEfBySearch(i6, string, string2, string3, string4, string5);
                    parcel2.writeNoException();
                    parcel2.writeBoolean(zUpdateAdnRecordsInEfBySearch);
                    return true;
                case 4:
                    int i7 = parcel.readInt();
                    int i8 = parcel.readInt();
                    ContentValues contentValues = (ContentValues) parcel.readTypedObject(ContentValues.CREATOR);
                    String string6 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    boolean zUpdateAdnRecordsInEfBySearchForSubscriber = updateAdnRecordsInEfBySearchForSubscriber(i7, i8, contentValues, string6);
                    parcel2.writeNoException();
                    parcel2.writeBoolean(zUpdateAdnRecordsInEfBySearchForSubscriber);
                    return true;
                case 5:
                    int i9 = parcel.readInt();
                    int i10 = parcel.readInt();
                    ContentValues contentValues2 = (ContentValues) parcel.readTypedObject(ContentValues.CREATOR);
                    int i11 = parcel.readInt();
                    String string7 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    boolean zUpdateAdnRecordsInEfByIndexForSubscriber = updateAdnRecordsInEfByIndexForSubscriber(i9, i10, contentValues2, i11, string7);
                    parcel2.writeNoException();
                    parcel2.writeBoolean(zUpdateAdnRecordsInEfByIndexForSubscriber);
                    return true;
                case 6:
                    int i12 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    int[] adnRecordsSize = getAdnRecordsSize(i12);
                    parcel2.writeNoException();
                    parcel2.writeIntArray(adnRecordsSize);
                    return true;
                case 7:
                    int i13 = parcel.readInt();
                    int i14 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    int[] adnRecordsSizeForSubscriber = getAdnRecordsSizeForSubscriber(i13, i14);
                    parcel2.writeNoException();
                    parcel2.writeIntArray(adnRecordsSizeForSubscriber);
                    return true;
                case 8:
                    AdnCapacity adnRecordsCapacity = getAdnRecordsCapacity();
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(adnRecordsCapacity, 1);
                    return true;
                case 9:
                    int i15 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    AdnCapacity adnRecordsCapacityForSubscriber = getAdnRecordsCapacityForSubscriber(i15);
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(adnRecordsCapacityForSubscriber, 1);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static final class Proxy implements IIccPhoneBook {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IIccPhoneBook.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public List<AdnRecord> getAdnRecordsInEf(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.createTypedArrayList(AdnRecord.CREATOR);
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public List<AdnRecord> getAdnRecordsInEfForSubscriber(int i, int i2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.createTypedArrayList(AdnRecord.CREATOR);
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public boolean updateAdnRecordsInEfBySearch(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeString(str3);
                    parcelObtain.writeString(str4);
                    parcelObtain.writeString(str5);
                    this.mRemote.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public boolean updateAdnRecordsInEfBySearchForSubscriber(int i, int i2, ContentValues contentValues, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeTypedObject(contentValues, 0);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(4, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public boolean updateAdnRecordsInEfByIndexForSubscriber(int i, int i2, ContentValues contentValues, int i3, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    parcelObtain.writeTypedObject(contentValues, 0);
                    parcelObtain.writeInt(i3);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readBoolean();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public int[] getAdnRecordsSize(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.createIntArray();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public int[] getAdnRecordsSizeForSubscriber(int i, int i2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    this.mRemote.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.createIntArray();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public AdnCapacity getAdnRecordsCapacity() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    this.mRemote.transact(8, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return (AdnCapacity) parcelObtain2.readTypedObject(AdnCapacity.CREATOR);
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.android.internal.telephony.IIccPhoneBook
            public AdnCapacity getAdnRecordsCapacityForSubscriber(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain(asBinder());
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(IIccPhoneBook.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(9, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return (AdnCapacity) parcelObtain2.readTypedObject(AdnCapacity.CREATOR);
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
