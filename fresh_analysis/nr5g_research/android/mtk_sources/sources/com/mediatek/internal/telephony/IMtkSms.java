package com.mediatek.internal.telephony;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telephony.SmsRawData;
import java.util.List;
import mediatek.telephony.MtkSimSmsInsertStatus;
import mediatek.telephony.MtkSmsParameters;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkSms extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.internal.telephony.IMtkSms";

    boolean activateCellBroadcastSmsForSubscriber(int i, boolean z) throws RemoteException;

    int copyTextMessageToIccCardForSubscriber(int i, String str, String str2, String str3, List<String> list, int i2, long j) throws RemoteException;

    List<SmsRawData> getAllMessagesFromIccEfByModeForSubscriber(int i, String str, int i2) throws RemoteException;

    String getCellBroadcastLangsForSubscriber(int i) throws RemoteException;

    String getCellBroadcastRangesForSubscriber(int i) throws RemoteException;

    SmsRawData getMessageFromIccEfForSubscriber(int i, String str, int i2) throws RemoteException;

    String getScAddressForSubscriber(int i) throws RemoteException;

    Bundle getScAddressWithErrorCodeForSubscriber(int i) throws RemoteException;

    MtkSmsParameters getSmsParametersForSubscriber(int i, String str) throws RemoteException;

    MtkIccSmsStorageStatus getSmsSimMemoryStatusForSubscriber(int i, String str) throws RemoteException;

    MtkSimSmsInsertStatus insertRawMessageToIccCardForSubscriber(int i, String str, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    MtkSimSmsInsertStatus insertTextMessageToIccCardForSubscriber(int i, String str, String str2, String str3, List<String> list, int i2, long j) throws RemoteException;

    boolean isSmsReadyForSubscriber(int i) throws RemoteException;

    boolean queryCellBroadcastSmsActivationForSubscriber(int i) throws RemoteException;

    boolean removeCellBroadcastMsgForSubscriber(int i, int i2, int i3) throws RemoteException;

    void sendDataWithOriginalPortForSubscriber(int i, String str, String str2, String str3, int i2, int i3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    void sendMultipartTextWithEncodingTypeForSubscriber(int i, String str, String str2, String str3, List<String> list, int i2, List<PendingIntent> list2, List<PendingIntent> list3, boolean z) throws RemoteException;

    void sendMultipartTextWithExtraParamsForSubscriber(int i, String str, String str2, String str3, List<String> list, Bundle bundle, List<PendingIntent> list2, List<PendingIntent> list3, boolean z) throws RemoteException;

    void sendTextWithEncodingTypeForSubscriber(int i, String str, String str2, String str3, String str4, int i2, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z) throws RemoteException;

    void sendTextWithExtraParamsForSubscriber(int i, String str, String str2, String str3, String str4, Bundle bundle, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z) throws RemoteException;

    boolean setCellBroadcastLangsForSubscriber(int i, String str) throws RemoteException;

    boolean setEtwsConfigForSubscriber(int i, int i2) throws RemoteException;

    boolean setScAddressForSubscriber(int i, String str) throws RemoteException;

    void setSmsMemoryStatusForSubscriber(int i, boolean z) throws RemoteException;

    boolean setSmsParametersForSubscriber(int i, String str, MtkSmsParameters mtkSmsParameters) throws RemoteException;

    public static class Default implements IMtkSms {
        @Override // com.mediatek.internal.telephony.IMtkSms
        public List<SmsRawData> getAllMessagesFromIccEfByModeForSubscriber(int subId, String callingPkg, int mode) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public int copyTextMessageToIccCardForSubscriber(int subId, String callingPkg, String scAddress, String address, List<String> text, int status, long timestamp) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public void sendDataWithOriginalPortForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public boolean isSmsReadyForSubscriber(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public void setSmsMemoryStatusForSubscriber(int subId, boolean status) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public MtkIccSmsStorageStatus getSmsSimMemoryStatusForSubscriber(int subId, String callingPkg) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public void sendTextWithEncodingTypeForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public void sendMultipartTextWithEncodingTypeForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, List<String> parts, int encodingType, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public MtkSimSmsInsertStatus insertTextMessageToIccCardForSubscriber(int subId, String callingPkg, String scAddress, String address, List<String> text, int status, long timestamp) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public MtkSimSmsInsertStatus insertRawMessageToIccCardForSubscriber(int subId, String callingPkg, int status, byte[] pdu, byte[] smsc) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public void sendTextWithExtraParamsForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, Bundle extraParams, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public void sendMultipartTextWithExtraParamsForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, List<String> parts, Bundle extraParams, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public MtkSmsParameters getSmsParametersForSubscriber(int subId, String callingPkg) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public boolean setSmsParametersForSubscriber(int subId, String callingPkg, MtkSmsParameters params) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public SmsRawData getMessageFromIccEfForSubscriber(int subId, String callingPkg, int index) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public boolean queryCellBroadcastSmsActivationForSubscriber(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public boolean activateCellBroadcastSmsForSubscriber(int subId, boolean activate) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public boolean removeCellBroadcastMsgForSubscriber(int subId, int channelId, int serialId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public boolean setEtwsConfigForSubscriber(int subId, int mode) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public String getCellBroadcastRangesForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public boolean setCellBroadcastLangsForSubscriber(int subId, String lang) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public String getCellBroadcastLangsForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public String getScAddressForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public Bundle getScAddressWithErrorCodeForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.IMtkSms
        public boolean setScAddressForSubscriber(int subId, String address) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkSms {
        static final int TRANSACTION_activateCellBroadcastSmsForSubscriber = 17;
        static final int TRANSACTION_copyTextMessageToIccCardForSubscriber = 2;
        static final int TRANSACTION_getAllMessagesFromIccEfByModeForSubscriber = 1;
        static final int TRANSACTION_getCellBroadcastLangsForSubscriber = 22;
        static final int TRANSACTION_getCellBroadcastRangesForSubscriber = 20;
        static final int TRANSACTION_getMessageFromIccEfForSubscriber = 15;
        static final int TRANSACTION_getScAddressForSubscriber = 23;
        static final int TRANSACTION_getScAddressWithErrorCodeForSubscriber = 24;
        static final int TRANSACTION_getSmsParametersForSubscriber = 13;
        static final int TRANSACTION_getSmsSimMemoryStatusForSubscriber = 6;
        static final int TRANSACTION_insertRawMessageToIccCardForSubscriber = 10;
        static final int TRANSACTION_insertTextMessageToIccCardForSubscriber = 9;
        static final int TRANSACTION_isSmsReadyForSubscriber = 4;
        static final int TRANSACTION_queryCellBroadcastSmsActivationForSubscriber = 16;
        static final int TRANSACTION_removeCellBroadcastMsgForSubscriber = 18;
        static final int TRANSACTION_sendDataWithOriginalPortForSubscriber = 3;
        static final int TRANSACTION_sendMultipartTextWithEncodingTypeForSubscriber = 8;
        static final int TRANSACTION_sendMultipartTextWithExtraParamsForSubscriber = 12;
        static final int TRANSACTION_sendTextWithEncodingTypeForSubscriber = 7;
        static final int TRANSACTION_sendTextWithExtraParamsForSubscriber = 11;
        static final int TRANSACTION_setCellBroadcastLangsForSubscriber = 21;
        static final int TRANSACTION_setEtwsConfigForSubscriber = 19;
        static final int TRANSACTION_setScAddressForSubscriber = 25;
        static final int TRANSACTION_setSmsMemoryStatusForSubscriber = 5;
        static final int TRANSACTION_setSmsParametersForSubscriber = 14;

        public Stub() {
            attachInterface(this, IMtkSms.DESCRIPTOR);
        }

        public static IMtkSms asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IMtkSms.DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkSms)) {
                return (IMtkSms) iin;
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
                data.enforceInterface(IMtkSms.DESCRIPTOR);
            }
            switch (code) {
                case 1:
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    data.enforceNoDataAvail();
                    List<SmsRawData> _result = getAllMessagesFromIccEfByModeForSubscriber(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeTypedList(_result, 1);
                    return true;
                case 2:
                    int _arg3 = data.readInt();
                    String _arg4 = data.readString();
                    String _arg5 = data.readString();
                    String _arg6 = data.readString();
                    List<String> _arg7 = data.createStringArrayList();
                    int _arg8 = data.readInt();
                    long _arg9 = data.readLong();
                    data.enforceNoDataAvail();
                    int _result2 = copyTextMessageToIccCardForSubscriber(_arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    int _arg10 = data.readInt();
                    String _arg11 = data.readString();
                    String _arg12 = data.readString();
                    String _arg13 = data.readString();
                    int _arg14 = data.readInt();
                    int _arg15 = data.readInt();
                    byte[] _arg16 = data.createByteArray();
                    PendingIntent _arg17 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                    PendingIntent _arg18 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                    data.enforceNoDataAvail();
                    sendDataWithOriginalPortForSubscriber(_arg10, _arg11, _arg12, _arg13, _arg14, _arg15, _arg16, _arg17, _arg18);
                    reply.writeNoException();
                    return true;
                case 4:
                    int _arg19 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result3 = isSmsReadyForSubscriber(_arg19);
                    reply.writeNoException();
                    reply.writeBoolean(_result3);
                    return true;
                case 5:
                    int _arg20 = data.readInt();
                    boolean _arg21 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setSmsMemoryStatusForSubscriber(_arg20, _arg21);
                    reply.writeNoException();
                    return true;
                case 6:
                    int _arg22 = data.readInt();
                    String _arg23 = data.readString();
                    data.enforceNoDataAvail();
                    MtkIccSmsStorageStatus _result4 = getSmsSimMemoryStatusForSubscriber(_arg22, _arg23);
                    reply.writeNoException();
                    reply.writeTypedObject(_result4, 1);
                    return true;
                case 7:
                    int _arg24 = data.readInt();
                    String _arg25 = data.readString();
                    String _arg26 = data.readString();
                    String _arg27 = data.readString();
                    String _arg28 = data.readString();
                    int _arg29 = data.readInt();
                    PendingIntent _arg30 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                    PendingIntent _arg31 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                    boolean _arg32 = data.readBoolean();
                    data.enforceNoDataAvail();
                    sendTextWithEncodingTypeForSubscriber(_arg24, _arg25, _arg26, _arg27, _arg28, _arg29, _arg30, _arg31, _arg32);
                    reply.writeNoException();
                    return true;
                case 8:
                    int _arg33 = data.readInt();
                    String _arg34 = data.readString();
                    String _arg35 = data.readString();
                    String _arg36 = data.readString();
                    List<String> _arg37 = data.createStringArrayList();
                    int _arg38 = data.readInt();
                    List<PendingIntent> _arg39 = data.createTypedArrayList(PendingIntent.CREATOR);
                    List<PendingIntent> _arg40 = data.createTypedArrayList(PendingIntent.CREATOR);
                    boolean _arg41 = data.readBoolean();
                    data.enforceNoDataAvail();
                    sendMultipartTextWithEncodingTypeForSubscriber(_arg33, _arg34, _arg35, _arg36, _arg37, _arg38, _arg39, _arg40, _arg41);
                    reply.writeNoException();
                    return true;
                case 9:
                    int _arg42 = data.readInt();
                    String _arg43 = data.readString();
                    String _arg44 = data.readString();
                    String _arg45 = data.readString();
                    List<String> _arg46 = data.createStringArrayList();
                    int _arg47 = data.readInt();
                    long _arg48 = data.readLong();
                    data.enforceNoDataAvail();
                    MtkSimSmsInsertStatus _result5 = insertTextMessageToIccCardForSubscriber(_arg42, _arg43, _arg44, _arg45, _arg46, _arg47, _arg48);
                    reply.writeNoException();
                    reply.writeTypedObject(_result5, 1);
                    return true;
                case 10:
                    int _arg49 = data.readInt();
                    String _arg50 = data.readString();
                    int _arg51 = data.readInt();
                    byte[] _arg52 = data.createByteArray();
                    byte[] _arg53 = data.createByteArray();
                    data.enforceNoDataAvail();
                    MtkSimSmsInsertStatus _result6 = insertRawMessageToIccCardForSubscriber(_arg49, _arg50, _arg51, _arg52, _arg53);
                    reply.writeNoException();
                    reply.writeTypedObject(_result6, 1);
                    return true;
                case 11:
                    int _arg54 = data.readInt();
                    String _arg55 = data.readString();
                    String _arg56 = data.readString();
                    String _arg57 = data.readString();
                    String _arg58 = data.readString();
                    Bundle _arg59 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                    PendingIntent _arg60 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                    PendingIntent _arg61 = (PendingIntent) data.readTypedObject(PendingIntent.CREATOR);
                    boolean _arg62 = data.readBoolean();
                    data.enforceNoDataAvail();
                    sendTextWithExtraParamsForSubscriber(_arg54, _arg55, _arg56, _arg57, _arg58, _arg59, _arg60, _arg61, _arg62);
                    reply.writeNoException();
                    return true;
                case 12:
                    int _arg63 = data.readInt();
                    String _arg64 = data.readString();
                    String _arg65 = data.readString();
                    String _arg66 = data.readString();
                    List<String> _arg67 = data.createStringArrayList();
                    Bundle _arg68 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                    List<PendingIntent> _arg69 = data.createTypedArrayList(PendingIntent.CREATOR);
                    List<PendingIntent> _arg70 = data.createTypedArrayList(PendingIntent.CREATOR);
                    boolean _arg71 = data.readBoolean();
                    data.enforceNoDataAvail();
                    sendMultipartTextWithExtraParamsForSubscriber(_arg63, _arg64, _arg65, _arg66, _arg67, _arg68, _arg69, _arg70, _arg71);
                    reply.writeNoException();
                    return true;
                case 13:
                    int _arg72 = data.readInt();
                    String _arg73 = data.readString();
                    data.enforceNoDataAvail();
                    MtkSmsParameters _result7 = getSmsParametersForSubscriber(_arg72, _arg73);
                    reply.writeNoException();
                    reply.writeTypedObject(_result7, 1);
                    return true;
                case 14:
                    int _arg74 = data.readInt();
                    String _arg75 = data.readString();
                    MtkSmsParameters _arg76 = (MtkSmsParameters) data.readTypedObject(MtkSmsParameters.CREATOR);
                    data.enforceNoDataAvail();
                    boolean _result8 = setSmsParametersForSubscriber(_arg74, _arg75, _arg76);
                    reply.writeNoException();
                    reply.writeBoolean(_result8);
                    return true;
                case 15:
                    int _arg77 = data.readInt();
                    String _arg78 = data.readString();
                    int _arg79 = data.readInt();
                    data.enforceNoDataAvail();
                    SmsRawData _result9 = getMessageFromIccEfForSubscriber(_arg77, _arg78, _arg79);
                    reply.writeNoException();
                    reply.writeTypedObject(_result9, 1);
                    return true;
                case 16:
                    int _arg80 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result10 = queryCellBroadcastSmsActivationForSubscriber(_arg80);
                    reply.writeNoException();
                    reply.writeBoolean(_result10);
                    return true;
                case 17:
                    int _arg81 = data.readInt();
                    boolean _arg82 = data.readBoolean();
                    data.enforceNoDataAvail();
                    boolean _result11 = activateCellBroadcastSmsForSubscriber(_arg81, _arg82);
                    reply.writeNoException();
                    reply.writeBoolean(_result11);
                    return true;
                case 18:
                    int _arg83 = data.readInt();
                    int _arg84 = data.readInt();
                    int _arg85 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result12 = removeCellBroadcastMsgForSubscriber(_arg83, _arg84, _arg85);
                    reply.writeNoException();
                    reply.writeBoolean(_result12);
                    return true;
                case TRANSACTION_setEtwsConfigForSubscriber /* 19 */:
                    int _arg86 = data.readInt();
                    int _arg87 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result13 = setEtwsConfigForSubscriber(_arg86, _arg87);
                    reply.writeNoException();
                    reply.writeBoolean(_result13);
                    return true;
                case 20:
                    int _arg88 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result14 = getCellBroadcastRangesForSubscriber(_arg88);
                    reply.writeNoException();
                    reply.writeString(_result14);
                    return true;
                case TRANSACTION_setCellBroadcastLangsForSubscriber /* 21 */:
                    int _arg89 = data.readInt();
                    String _arg90 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result15 = setCellBroadcastLangsForSubscriber(_arg89, _arg90);
                    reply.writeNoException();
                    reply.writeBoolean(_result15);
                    return true;
                case TRANSACTION_getCellBroadcastLangsForSubscriber /* 22 */:
                    int _arg91 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result16 = getCellBroadcastLangsForSubscriber(_arg91);
                    reply.writeNoException();
                    reply.writeString(_result16);
                    return true;
                case TRANSACTION_getScAddressForSubscriber /* 23 */:
                    int _arg92 = data.readInt();
                    data.enforceNoDataAvail();
                    String _result17 = getScAddressForSubscriber(_arg92);
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case 24:
                    int _arg93 = data.readInt();
                    data.enforceNoDataAvail();
                    Bundle _result18 = getScAddressWithErrorCodeForSubscriber(_arg93);
                    reply.writeNoException();
                    reply.writeTypedObject(_result18, 1);
                    return true;
                case TRANSACTION_setScAddressForSubscriber /* 25 */:
                    int _arg94 = data.readInt();
                    String _arg95 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result19 = setScAddressForSubscriber(_arg94, _arg95);
                    reply.writeNoException();
                    reply.writeBoolean(_result19);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static final class Proxy implements IMtkSms {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public final String getInterfaceDescriptor() {
                return IMtkSms.DESCRIPTOR;
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public List<SmsRawData> getAllMessagesFromIccEfByModeForSubscriber(int subId, String callingPkg, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeInt(mode);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<SmsRawData> _result = _reply.createTypedArrayList(SmsRawData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public int copyTextMessageToIccCardForSubscriber(int subId, String callingPkg, String scAddress, String address, List<String> text, int status, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(scAddress);
                    _data.writeString(address);
                    _data.writeStringList(text);
                    _data.writeInt(status);
                    _data.writeLong(timestamp);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public void sendDataWithOriginalPortForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeInt(destPort);
                    _data.writeInt(originalPort);
                    _data.writeByteArray(data);
                    _data.writeTypedObject(sentIntent, 0);
                    _data.writeTypedObject(deliveryIntent, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public boolean isSmsReadyForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public void setSmsMemoryStatusForSubscriber(int subId, boolean status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeBoolean(status);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public MtkIccSmsStorageStatus getSmsSimMemoryStatusForSubscriber(int subId, String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    MtkIccSmsStorageStatus _result = (MtkIccSmsStorageStatus) _reply.readTypedObject(MtkIccSmsStorageStatus.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public void sendTextWithEncodingTypeForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    _data.writeInt(encodingType);
                    _data.writeTypedObject(sentIntent, 0);
                    _data.writeTypedObject(deliveryIntent, 0);
                    _data.writeBoolean(persistMessageForNonDefaultSmsApp);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public void sendMultipartTextWithEncodingTypeForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, List<String> parts, int encodingType, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeStringList(parts);
                    _data.writeInt(encodingType);
                    _data.writeTypedList(sentIntents, 0);
                    _data.writeTypedList(deliveryIntents, 0);
                    _data.writeBoolean(persistMessageForNonDefaultSmsApp);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public MtkSimSmsInsertStatus insertTextMessageToIccCardForSubscriber(int subId, String callingPkg, String scAddress, String address, List<String> text, int status, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(scAddress);
                    _data.writeString(address);
                    _data.writeStringList(text);
                    _data.writeInt(status);
                    _data.writeLong(timestamp);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    MtkSimSmsInsertStatus _result = (MtkSimSmsInsertStatus) _reply.readTypedObject(MtkSimSmsInsertStatus.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public MtkSimSmsInsertStatus insertRawMessageToIccCardForSubscriber(int subId, String callingPkg, int status, byte[] pdu, byte[] smsc) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeInt(status);
                    _data.writeByteArray(pdu);
                    _data.writeByteArray(smsc);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    MtkSimSmsInsertStatus _result = (MtkSimSmsInsertStatus) _reply.readTypedObject(MtkSimSmsInsertStatus.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public void sendTextWithExtraParamsForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, Bundle extraParams, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    _data.writeTypedObject(extraParams, 0);
                    _data.writeTypedObject(sentIntent, 0);
                    _data.writeTypedObject(deliveryIntent, 0);
                    _data.writeBoolean(persistMessageForNonDefaultSmsApp);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public void sendMultipartTextWithExtraParamsForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, List<String> parts, Bundle extraParams, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeStringList(parts);
                    _data.writeTypedObject(extraParams, 0);
                    _data.writeTypedList(sentIntents, 0);
                    _data.writeTypedList(deliveryIntents, 0);
                    _data.writeBoolean(persistMessageForNonDefaultSmsApp);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public MtkSmsParameters getSmsParametersForSubscriber(int subId, String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    MtkSmsParameters _result = (MtkSmsParameters) _reply.readTypedObject(MtkSmsParameters.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public boolean setSmsParametersForSubscriber(int subId, String callingPkg, MtkSmsParameters params) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeTypedObject(params, 0);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public SmsRawData getMessageFromIccEfForSubscriber(int subId, String callingPkg, int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeInt(index);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    SmsRawData _result = (SmsRawData) _reply.readTypedObject(SmsRawData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public boolean queryCellBroadcastSmsActivationForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public boolean activateCellBroadcastSmsForSubscriber(int subId, boolean activate) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeBoolean(activate);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public boolean removeCellBroadcastMsgForSubscriber(int subId, int channelId, int serialId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(channelId);
                    _data.writeInt(serialId);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public boolean setEtwsConfigForSubscriber(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    this.mRemote.transact(Stub.TRANSACTION_setEtwsConfigForSubscriber, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public String getCellBroadcastRangesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public boolean setCellBroadcastLangsForSubscriber(int subId, String lang) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(lang);
                    this.mRemote.transact(Stub.TRANSACTION_setCellBroadcastLangsForSubscriber, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public String getCellBroadcastLangsForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getCellBroadcastLangsForSubscriber, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public String getScAddressForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getScAddressForSubscriber, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public Bundle getScAddressWithErrorCodeForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.IMtkSms
            public boolean setScAddressForSubscriber(int subId, String address) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkSms.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_setScAddressForSubscriber, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
