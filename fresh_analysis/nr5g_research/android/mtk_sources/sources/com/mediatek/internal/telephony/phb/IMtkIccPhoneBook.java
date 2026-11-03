package com.mediatek.internal.telephony.phb;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkIccPhoneBook extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.internal.telephony.phb.IMtkIccPhoneBook";

    boolean addContactToGroup(int i, int i2, int i3) throws RemoteException;

    int[] getAdnRecordsCapacityExt() throws RemoteException;

    int[] getAdnRecordsCapacityForSubscriber(int i) throws RemoteException;

    List<MtkAdnRecord> getAdnRecordsInEf(int i) throws RemoteException;

    List<MtkAdnRecord> getAdnRecordsInEfForSubscriber(int i, int i2) throws RemoteException;

    int getAnrCount(int i) throws RemoteException;

    int getEmailCount(int i) throws RemoteException;

    UsimPBMemInfo[] getPhonebookMemStorageExt(int i) throws RemoteException;

    int getSneRecordLen(int i) throws RemoteException;

    int getUpbDone(int i) throws RemoteException;

    String getUsimAasById(int i, int i2) throws RemoteException;

    List<AlphaTag> getUsimAasList(int i) throws RemoteException;

    int getUsimAasMaxCount(int i) throws RemoteException;

    int getUsimAasMaxNameLen(int i) throws RemoteException;

    String getUsimGroupById(int i, int i2) throws RemoteException;

    List<UsimGroup> getUsimGroups(int i) throws RemoteException;

    int getUsimGrpMaxCount(int i) throws RemoteException;

    int getUsimGrpMaxNameLen(int i) throws RemoteException;

    int hasExistGroup(int i, String str) throws RemoteException;

    boolean hasSne(int i) throws RemoteException;

    int insertUsimAas(int i, String str) throws RemoteException;

    int insertUsimGroup(int i, String str) throws RemoteException;

    boolean isAdnAccessible(int i) throws RemoteException;

    boolean isPhbReady(int i) throws RemoteException;

    boolean moveContactFromGroupsToGroups(int i, int i2, int[] iArr, int[] iArr2) throws RemoteException;

    boolean removeContactFromGroup(int i, int i2, int i3) throws RemoteException;

    boolean removeUsimAasById(int i, int i2, int i3) throws RemoteException;

    boolean removeUsimGroupById(int i, int i2) throws RemoteException;

    int updateAdnRecordsInEfByIndexWithError(int i, int i2, String str, String str2, int i3, String str3) throws RemoteException;

    int updateAdnRecordsInEfBySearchWithError(int i, int i2, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    boolean updateContactToGroups(int i, int i2, int[] iArr) throws RemoteException;

    boolean updateUsimAas(int i, int i2, int i3, String str) throws RemoteException;

    int updateUsimGroup(int i, int i2, String str) throws RemoteException;

    int updateUsimPBRecordsByIndexWithError(int i, int i2, MtkAdnRecord mtkAdnRecord, int i3) throws RemoteException;

    int updateUsimPBRecordsBySearchWithError(int i, int i2, MtkAdnRecord mtkAdnRecord, MtkAdnRecord mtkAdnRecord2) throws RemoteException;

    int updateUsimPBRecordsInEfByIndexWithError(int i, int i2, String str, String str2, String str3, String str4, String[] strArr, int i3) throws RemoteException;

    int updateUsimPBRecordsInEfBySearchWithError(int i, int i2, String str, String str2, String str3, String str4, String[] strArr, String str5, String str6, String str7, String str8, String[] strArr2) throws RemoteException;

    public static class Default implements IMtkIccPhoneBook {
        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public List<MtkAdnRecord> getAdnRecordsInEf(int efid) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public List<MtkAdnRecord> getAdnRecordsInEfForSubscriber(int subId, int efid) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateAdnRecordsInEfBySearchWithError(int subId, int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimPBRecordsInEfBySearchWithError(int subId, int efid, String oldTag, String oldPhoneNumber, String oldAnr, String oldGrpIds, String[] oldEmails, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateAdnRecordsInEfByIndexWithError(int subId, int efid, String newTag, String newPhoneNumber, int index, String pin2) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimPBRecordsInEfByIndexWithError(int subId, int efid, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails, int index) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimPBRecordsByIndexWithError(int subId, int efid, MtkAdnRecord record, int index) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimPBRecordsBySearchWithError(int subId, int efid, MtkAdnRecord oldAdn, MtkAdnRecord newAdn) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean isPhbReady(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public List<UsimGroup> getUsimGroups(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public String getUsimGroupById(int subId, int nGasId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean removeUsimGroupById(int subId, int nGasId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int insertUsimGroup(int subId, String grpName) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimGroup(int subId, int nGasId, String grpName) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean addContactToGroup(int subId, int adnIndex, int grpIndex) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean removeContactFromGroup(int subId, int adnIndex, int grpIndex) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean updateContactToGroups(int subId, int adnIndex, int[] grpIdList) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean moveContactFromGroupsToGroups(int subId, int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int hasExistGroup(int subId, String grpName) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUsimGrpMaxNameLen(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUsimGrpMaxCount(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public List<AlphaTag> getUsimAasList(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public String getUsimAasById(int subId, int index) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int insertUsimAas(int subId, String aasName) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getAnrCount(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getEmailCount(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUsimAasMaxCount(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUsimAasMaxNameLen(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean updateUsimAas(int subId, int index, int pbrIndex, String aasName) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean removeUsimAasById(int subId, int index, int pbrIndex) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean hasSne(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getSneRecordLen(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean isAdnAccessible(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public UsimPBMemInfo[] getPhonebookMemStorageExt(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUpbDone(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int[] getAdnRecordsCapacityExt() throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int[] getAdnRecordsCapacityForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkIccPhoneBook {
        static final int TRANSACTION_addContactToGroup = 15;
        static final int TRANSACTION_getAdnRecordsCapacityExt = 36;
        static final int TRANSACTION_getAdnRecordsCapacityForSubscriber = 37;
        static final int TRANSACTION_getAdnRecordsInEf = 1;
        static final int TRANSACTION_getAdnRecordsInEfForSubscriber = 2;
        static final int TRANSACTION_getAnrCount = 25;
        static final int TRANSACTION_getEmailCount = 26;
        static final int TRANSACTION_getPhonebookMemStorageExt = 34;
        static final int TRANSACTION_getSneRecordLen = 32;
        static final int TRANSACTION_getUpbDone = 35;
        static final int TRANSACTION_getUsimAasById = 23;
        static final int TRANSACTION_getUsimAasList = 22;
        static final int TRANSACTION_getUsimAasMaxCount = 27;
        static final int TRANSACTION_getUsimAasMaxNameLen = 28;
        static final int TRANSACTION_getUsimGroupById = 11;
        static final int TRANSACTION_getUsimGroups = 10;
        static final int TRANSACTION_getUsimGrpMaxCount = 21;
        static final int TRANSACTION_getUsimGrpMaxNameLen = 20;
        static final int TRANSACTION_hasExistGroup = 19;
        static final int TRANSACTION_hasSne = 31;
        static final int TRANSACTION_insertUsimAas = 24;
        static final int TRANSACTION_insertUsimGroup = 13;
        static final int TRANSACTION_isAdnAccessible = 33;
        static final int TRANSACTION_isPhbReady = 9;
        static final int TRANSACTION_moveContactFromGroupsToGroups = 18;
        static final int TRANSACTION_removeContactFromGroup = 16;
        static final int TRANSACTION_removeUsimAasById = 30;
        static final int TRANSACTION_removeUsimGroupById = 12;
        static final int TRANSACTION_updateAdnRecordsInEfByIndexWithError = 5;
        static final int TRANSACTION_updateAdnRecordsInEfBySearchWithError = 3;
        static final int TRANSACTION_updateContactToGroups = 17;
        static final int TRANSACTION_updateUsimAas = 29;
        static final int TRANSACTION_updateUsimGroup = 14;
        static final int TRANSACTION_updateUsimPBRecordsByIndexWithError = 7;
        static final int TRANSACTION_updateUsimPBRecordsBySearchWithError = 8;
        static final int TRANSACTION_updateUsimPBRecordsInEfByIndexWithError = 6;
        static final int TRANSACTION_updateUsimPBRecordsInEfBySearchWithError = 4;

        public Stub() {
            attachInterface(this, IMtkIccPhoneBook.DESCRIPTOR);
        }

        public static IMtkIccPhoneBook asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IMtkIccPhoneBook.DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkIccPhoneBook)) {
                return (IMtkIccPhoneBook) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IMtkIccPhoneBook.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    data.enforceNoDataAvail();
                    List<MtkAdnRecord> _result = getAdnRecordsInEf(_arg0);
                    reply.writeNoException();
                    reply.writeTypedList(_result, 1);
                    return true;
                case 2:
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    data.enforceNoDataAvail();
                    List<MtkAdnRecord> _result2 = getAdnRecordsInEfForSubscriber(_arg1, _arg2);
                    reply.writeNoException();
                    reply.writeTypedList(_result2, 1);
                    return true;
                case 3:
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    String _arg5 = data.readString();
                    String _arg6 = data.readString();
                    String _arg7 = data.readString();
                    String _arg8 = data.readString();
                    String _arg9 = data.readString();
                    data.enforceNoDataAvail();
                    int _result3 = updateAdnRecordsInEfBySearchWithError(_arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 4:
                    int _arg10 = data.readInt();
                    int _arg11 = data.readInt();
                    String _arg12 = data.readString();
                    String _arg13 = data.readString();
                    String _arg14 = data.readString();
                    String _arg15 = data.readString();
                    String[] _arg16 = data.createStringArray();
                    String _arg17 = data.readString();
                    String _arg18 = data.readString();
                    String _arg19 = data.readString();
                    String _arg110 = data.readString();
                    String[] _arg111 = data.createStringArray();
                    data.enforceNoDataAvail();
                    int _result4 = updateUsimPBRecordsInEfBySearchWithError(_arg10, _arg11, _arg12, _arg13, _arg14, _arg15, _arg16, _arg17, _arg18, _arg19, _arg110, _arg111);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    int _arg20 = data.readInt();
                    int _arg21 = data.readInt();
                    String _arg22 = data.readString();
                    String _arg23 = data.readString();
                    int _arg24 = data.readInt();
                    String _arg25 = data.readString();
                    data.enforceNoDataAvail();
                    int _result5 = updateAdnRecordsInEfByIndexWithError(_arg20, _arg21, _arg22, _arg23, _arg24, _arg25);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 6:
                    int _arg26 = data.readInt();
                    int _arg27 = data.readInt();
                    String _arg28 = data.readString();
                    String _arg29 = data.readString();
                    String _arg30 = data.readString();
                    String _arg31 = data.readString();
                    String[] _arg32 = data.createStringArray();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result6 = updateUsimPBRecordsInEfByIndexWithError(_arg26, _arg27, _arg28, _arg29, _arg30, _arg31, _arg32, _arg33);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 7:
                    int _arg34 = data.readInt();
                    int _arg35 = data.readInt();
                    MtkAdnRecord _arg36 = (MtkAdnRecord) data.readTypedObject(MtkAdnRecord.CREATOR);
                    int _arg37 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result7 = updateUsimPBRecordsByIndexWithError(_arg34, _arg35, _arg36, _arg37);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 8:
                    int _arg38 = data.readInt();
                    int _arg39 = data.readInt();
                    MtkAdnRecord _arg40 = (MtkAdnRecord) data.readTypedObject(MtkAdnRecord.CREATOR);
                    MtkAdnRecord _arg41 = (MtkAdnRecord) data.readTypedObject(MtkAdnRecord.CREATOR);
                    data.enforceNoDataAvail();
                    int _result8 = updateUsimPBRecordsBySearchWithError(_arg38, _arg39, _arg40, _arg41);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 9:
                    int _arg42 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result9 = isPhbReady(_arg42);
                    reply.writeNoException();
                    reply.writeBoolean(_result9);
                    return true;
                case 10:
                    int _arg43 = data.readInt();
                    data.enforceNoDataAvail();
                    List<UsimGroup> _result10 = getUsimGroups(_arg43);
                    reply.writeNoException();
                    reply.writeTypedList(_result10, 1);
                    return true;
                case 11:
                    int _arg44 = data.readInt();
                    int _arg45 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result11 = getUsimGroupById(_arg44, _arg45);
                    reply.writeNoException();
                    reply.writeString(_result11);
                    return true;
                case 12:
                    int _arg46 = data.readInt();
                    int _arg47 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result12 = removeUsimGroupById(_arg46, _arg47);
                    reply.writeNoException();
                    reply.writeBoolean(_result12);
                    return true;
                case 13:
                    int _arg48 = data.readInt();
                    String _arg49 = data.readString();
                    data.enforceNoDataAvail();
                    int _result13 = insertUsimGroup(_arg48, _arg49);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 14:
                    int _arg50 = data.readInt();
                    int _arg51 = data.readInt();
                    String _arg52 = data.readString();
                    data.enforceNoDataAvail();
                    int _result14 = updateUsimGroup(_arg50, _arg51, _arg52);
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 15:
                    int _arg53 = data.readInt();
                    int _arg54 = data.readInt();
                    int _arg55 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result15 = addContactToGroup(_arg53, _arg54, _arg55);
                    reply.writeNoException();
                    reply.writeBoolean(_result15);
                    return true;
                case 16:
                    int _arg56 = data.readInt();
                    int _arg57 = data.readInt();
                    int _arg58 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result16 = removeContactFromGroup(_arg56, _arg57, _arg58);
                    reply.writeNoException();
                    reply.writeBoolean(_result16);
                    return true;
                case 17:
                    int _arg59 = data.readInt();
                    int _arg60 = data.readInt();
                    int[] _arg61 = data.createIntArray();
                    data.enforceNoDataAvail();
                    boolean _result17 = updateContactToGroups(_arg59, _arg60, _arg61);
                    reply.writeNoException();
                    reply.writeBoolean(_result17);
                    return true;
                case 18:
                    int _arg62 = data.readInt();
                    int _arg63 = data.readInt();
                    int[] _arg64 = data.createIntArray();
                    int[] _arg65 = data.createIntArray();
                    data.enforceNoDataAvail();
                    boolean _result18 = moveContactFromGroupsToGroups(_arg62, _arg63, _arg64, _arg65);
                    reply.writeNoException();
                    reply.writeBoolean(_result18);
                    return true;
                case TRANSACTION_hasExistGroup /* 19 */:
                    int _arg66 = data.readInt();
                    String _arg67 = data.readString();
                    data.enforceNoDataAvail();
                    int _result19 = hasExistGroup(_arg66, _arg67);
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 20:
                    int _arg68 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result20 = getUsimGrpMaxNameLen(_arg68);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case TRANSACTION_getUsimGrpMaxCount /* 21 */:
                    int _arg69 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result21 = getUsimGrpMaxCount(_arg69);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case TRANSACTION_getUsimAasList /* 22 */:
                    int _arg70 = data.readInt();
                    data.enforceNoDataAvail();
                    List<AlphaTag> _result22 = getUsimAasList(_arg70);
                    reply.writeNoException();
                    reply.writeTypedList(_result22, 1);
                    return true;
                case TRANSACTION_getUsimAasById /* 23 */:
                    int _arg71 = data.readInt();
                    int _arg72 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result23 = getUsimAasById(_arg71, _arg72);
                    reply.writeNoException();
                    reply.writeString(_result23);
                    return true;
                case 24:
                    int _arg73 = data.readInt();
                    String _arg74 = data.readString();
                    data.enforceNoDataAvail();
                    int _result24 = insertUsimAas(_arg73, _arg74);
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case TRANSACTION_getAnrCount /* 25 */:
                    int _arg75 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result25 = getAnrCount(_arg75);
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case TRANSACTION_getEmailCount /* 26 */:
                    int _arg76 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result26 = getEmailCount(_arg76);
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case TRANSACTION_getUsimAasMaxCount /* 27 */:
                    int _arg77 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result27 = getUsimAasMaxCount(_arg77);
                    reply.writeNoException();
                    reply.writeInt(_result27);
                    return true;
                case TRANSACTION_getUsimAasMaxNameLen /* 28 */:
                    int _arg78 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result28 = getUsimAasMaxNameLen(_arg78);
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case TRANSACTION_updateUsimAas /* 29 */:
                    int _arg79 = data.readInt();
                    int _arg80 = data.readInt();
                    int _arg81 = data.readInt();
                    String _arg82 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result29 = updateUsimAas(_arg79, _arg80, _arg81, _arg82);
                    reply.writeNoException();
                    reply.writeBoolean(_result29);
                    return true;
                case TRANSACTION_removeUsimAasById /* 30 */:
                    int _arg83 = data.readInt();
                    int _arg84 = data.readInt();
                    int _arg85 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result30 = removeUsimAasById(_arg83, _arg84, _arg85);
                    reply.writeNoException();
                    reply.writeBoolean(_result30);
                    return true;
                case TRANSACTION_hasSne /* 31 */:
                    int _arg86 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result31 = hasSne(_arg86);
                    reply.writeNoException();
                    reply.writeBoolean(_result31);
                    return true;
                case 32:
                    int _arg87 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result32 = getSneRecordLen(_arg87);
                    reply.writeNoException();
                    reply.writeInt(_result32);
                    return true;
                case 33:
                    int _arg88 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result33 = isAdnAccessible(_arg88);
                    reply.writeNoException();
                    reply.writeBoolean(_result33);
                    return true;
                case TRANSACTION_getPhonebookMemStorageExt /* 34 */:
                    int _arg89 = data.readInt();
                    data.enforceNoDataAvail();
                    UsimPBMemInfo[] _result34 = getPhonebookMemStorageExt(_arg89);
                    reply.writeNoException();
                    reply.writeTypedArray(_result34, 1);
                    return true;
                case TRANSACTION_getUpbDone /* 35 */:
                    int _arg90 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result35 = getUpbDone(_arg90);
                    reply.writeNoException();
                    reply.writeInt(_result35);
                    return true;
                case 36:
                    int[] _result36 = getAdnRecordsCapacityExt();
                    reply.writeNoException();
                    reply.writeIntArray(_result36);
                    return true;
                case TRANSACTION_getAdnRecordsCapacityForSubscriber /* 37 */:
                    int _arg91 = data.readInt();
                    data.enforceNoDataAvail();
                    int[] _result37 = getAdnRecordsCapacityForSubscriber(_arg91);
                    reply.writeNoException();
                    reply.writeIntArray(_result37);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkIccPhoneBook {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IMtkIccPhoneBook.DESCRIPTOR;
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public List<MtkAdnRecord> getAdnRecordsInEf(int efid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(efid);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<MtkAdnRecord> _result = _reply.createTypedArrayList(MtkAdnRecord.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public List<MtkAdnRecord> getAdnRecordsInEfForSubscriber(int subId, int efid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<MtkAdnRecord> _result = _reply.createTypedArrayList(MtkAdnRecord.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateAdnRecordsInEfBySearchWithError(int subId, int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeString(oldTag);
                    _data.writeString(oldPhoneNumber);
                    _data.writeString(newTag);
                    _data.writeString(newPhoneNumber);
                    _data.writeString(pin2);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimPBRecordsInEfBySearchWithError(int subId, int efid, String oldTag, String oldPhoneNumber, String oldAnr, String oldGrpIds, String[] oldEmails, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails) throws Throwable {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    try {
                        _data.writeString(oldTag);
                        try {
                            _data.writeString(oldPhoneNumber);
                            try {
                                _data.writeString(oldAnr);
                                try {
                                    _data.writeString(oldGrpIds);
                                    try {
                                        _data.writeStringArray(oldEmails);
                                        try {
                                            _data.writeString(newTag);
                                            try {
                                                _data.writeString(newPhoneNumber);
                                                try {
                                                    _data.writeString(newAnr);
                                                    try {
                                                        _data.writeString(newGrpIds);
                                                        try {
                                                            _data.writeStringArray(newEmails);
                                                            try {
                                                                this.mRemote.transact(4, _data, _reply, 0);
                                                                _reply.readException();
                                                                int _result = _reply.readInt();
                                                                _reply.recycle();
                                                                _data.recycle();
                                                                return _result;
                                                            } catch (Throwable th) {
                                                                th = th;
                                                                _reply.recycle();
                                                                _data.recycle();
                                                                throw th;
                                                            }
                                                        } catch (Throwable th2) {
                                                            th = th2;
                                                        }
                                                    } catch (Throwable th3) {
                                                        th = th3;
                                                        _reply.recycle();
                                                        _data.recycle();
                                                        throw th;
                                                    }
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                    _reply.recycle();
                                                    _data.recycle();
                                                    throw th;
                                                }
                                            } catch (Throwable th5) {
                                                th = th5;
                                                _reply.recycle();
                                                _data.recycle();
                                                throw th;
                                            }
                                        } catch (Throwable th6) {
                                            th = th6;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th7) {
                                        th = th7;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (Throwable th8) {
                                    th = th8;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th9) {
                                th = th9;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th10) {
                            th = th10;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th11) {
                        th = th11;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th12) {
                    th = th12;
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateAdnRecordsInEfByIndexWithError(int subId, int efid, String newTag, String newPhoneNumber, int index, String pin2) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeString(newTag);
                    _data.writeString(newPhoneNumber);
                    _data.writeInt(index);
                    _data.writeString(pin2);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimPBRecordsInEfByIndexWithError(int subId, int efid, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails, int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeString(newTag);
                    _data.writeString(newPhoneNumber);
                    _data.writeString(newAnr);
                    _data.writeString(newGrpIds);
                    _data.writeStringArray(newEmails);
                    _data.writeInt(index);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimPBRecordsByIndexWithError(int subId, int efid, MtkAdnRecord record, int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeTypedObject(record, 0);
                    _data.writeInt(index);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimPBRecordsBySearchWithError(int subId, int efid, MtkAdnRecord oldAdn, MtkAdnRecord newAdn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeTypedObject(oldAdn, 0);
                    _data.writeTypedObject(newAdn, 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean isPhbReady(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public List<UsimGroup> getUsimGroups(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    List<UsimGroup> _result = _reply.createTypedArrayList(UsimGroup.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public String getUsimGroupById(int subId, int nGasId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(nGasId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean removeUsimGroupById(int subId, int nGasId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(nGasId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int insertUsimGroup(int subId, String grpName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(grpName);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimGroup(int subId, int nGasId, String grpName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(nGasId);
                    _data.writeString(grpName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean addContactToGroup(int subId, int adnIndex, int grpIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(adnIndex);
                    _data.writeInt(grpIndex);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean removeContactFromGroup(int subId, int adnIndex, int grpIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(adnIndex);
                    _data.writeInt(grpIndex);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean updateContactToGroups(int subId, int adnIndex, int[] grpIdList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(adnIndex);
                    _data.writeIntArray(grpIdList);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean moveContactFromGroupsToGroups(int subId, int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(adnIndex);
                    _data.writeIntArray(fromGrpIdList);
                    _data.writeIntArray(toGrpIdList);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int hasExistGroup(int subId, String grpName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(grpName);
                    this.mRemote.transact(Stub.TRANSACTION_hasExistGroup, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUsimGrpMaxNameLen(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUsimGrpMaxCount(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimGrpMaxCount, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public List<AlphaTag> getUsimAasList(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimAasList, _data, _reply, 0);
                    _reply.readException();
                    List<AlphaTag> _result = _reply.createTypedArrayList(AlphaTag.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public String getUsimAasById(int subId, int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(index);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimAasById, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int insertUsimAas(int subId, String aasName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(aasName);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getAnrCount(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getAnrCount, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getEmailCount(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getEmailCount, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUsimAasMaxCount(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimAasMaxCount, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUsimAasMaxNameLen(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimAasMaxNameLen, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean updateUsimAas(int subId, int index, int pbrIndex, String aasName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(index);
                    _data.writeInt(pbrIndex);
                    _data.writeString(aasName);
                    this.mRemote.transact(Stub.TRANSACTION_updateUsimAas, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean removeUsimAasById(int subId, int index, int pbrIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(index);
                    _data.writeInt(pbrIndex);
                    this.mRemote.transact(Stub.TRANSACTION_removeUsimAasById, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean hasSne(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_hasSne, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getSneRecordLen(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean isAdnAccessible(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public UsimPBMemInfo[] getPhonebookMemStorageExt(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getPhonebookMemStorageExt, _data, _reply, 0);
                    _reply.readException();
                    UsimPBMemInfo[] _result = (UsimPBMemInfo[]) _reply.createTypedArray(UsimPBMemInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUpbDone(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUpbDone, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int[] getAdnRecordsCapacityExt() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int[] getAdnRecordsCapacityForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getAdnRecordsCapacityForSubscriber, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
