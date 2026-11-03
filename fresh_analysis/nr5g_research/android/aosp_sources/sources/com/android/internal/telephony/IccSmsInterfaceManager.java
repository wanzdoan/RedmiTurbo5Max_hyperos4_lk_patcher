package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.emergency.EmergencyNumber;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.cdma.CdmaSmsBroadcastConfigInfo;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.uicc.IccConstants;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class IccSmsInterfaceManager {
    protected static final int EVENT_SET_BROADCAST_ACTIVATION_DONE = 3;
    protected static final int EVENT_SET_BROADCAST_CONFIG_DONE = 4;
    public static final int SMS_MESSAGE_PERIOD_NOT_SPECIFIED = -1;
    public static final int SMS_MESSAGE_PRIORITY_NOT_SPECIFIED = -1;
    protected final AppOpsManager mAppOps;
    private CdmaBroadcastRangeManager mCdmaBroadcastRangeManager;
    private final LocalLog mCellBroadcastLocalLog;
    private CellBroadcastRangeManager mCellBroadcastRangeManager;
    protected final Context mContext;
    public SmsDispatchersController mDispatchersController;
    protected Handler mHandler;
    protected Phone mPhone;
    private PhoneFactoryProxy mPhoneFactoryProxy;
    private SmsPermissions mSmsPermissions;

    public interface PhoneFactoryProxy {
        Phone getDefaultPhone();

        Phone getPhone(int i);

        Phone[] getPhones();
    }

    public static final class Request {
        public AtomicBoolean mStatus = new AtomicBoolean(false);
        public Object mResult = null;
    }

    protected IccSmsInterfaceManager(Phone phone, FeatureFlags featureFlags) {
        this(phone, phone.getContext(), (AppOpsManager) phone.getContext().getSystemService("appops"), TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeSmsDispatchersController(phone, phone.mSmsStorageMonitor, phone.mSmsUsageMonitor, featureFlags), new SmsPermissions(phone, phone.getContext(), (AppOpsManager) phone.getContext().getSystemService("appops")));
    }

    public IccSmsInterfaceManager(Phone phone, Context context, AppOpsManager appOpsManager, SmsDispatchersController smsDispatchersController, SmsPermissions smsPermissions) {
        this.mCellBroadcastRangeManager = new CellBroadcastRangeManager();
        this.mCdmaBroadcastRangeManager = new CdmaBroadcastRangeManager();
        this.mCellBroadcastLocalLog = new LocalLog(64);
        this.mHandler = new Handler() { // from class: com.android.internal.telephony.IccSmsInterfaceManager.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                Request request = (Request) asyncResult.userObj;
                Object objBuildValidRawData = null;
                switch (message.what) {
                    case 1:
                        if (asyncResult.exception == null) {
                            objBuildValidRawData = IccSmsInterfaceManager.this.buildValidRawData((ArrayList) asyncResult.result);
                            IccSmsInterfaceManager.this.markMessagesAsRead((ArrayList) asyncResult.result);
                        } else if (Rlog.isLoggable("SMS", 3)) {
                            IccSmsInterfaceManager.this.loge("Cannot load Sms records");
                        }
                        notifyPending(request, objBuildValidRawData);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 6:
                        notifyPending(request, Boolean.valueOf(asyncResult.exception == null));
                        break;
                    case 5:
                        if (asyncResult.exception == null) {
                            objBuildValidRawData = (String) asyncResult.result;
                        } else {
                            IccSmsInterfaceManager.this.loge("Cannot read SMSC");
                        }
                        notifyPending(request, objBuildValidRawData);
                        break;
                }
            }

            private void notifyPending(Request request, Object obj) {
                if (request != null) {
                    synchronized (request) {
                        request.mResult = obj;
                        request.mStatus.set(true);
                        request.notifyAll();
                    }
                }
            }
        };
        this.mPhoneFactoryProxy = new PhoneFactoryProxy(this) { // from class: com.android.internal.telephony.IccSmsInterfaceManager.2
            @Override // com.android.internal.telephony.IccSmsInterfaceManager.PhoneFactoryProxy
            public Phone getPhone(int i) {
                return PhoneFactory.getPhone(i);
            }

            @Override // com.android.internal.telephony.IccSmsInterfaceManager.PhoneFactoryProxy
            public Phone getDefaultPhone() {
                return PhoneFactory.getDefaultPhone();
            }

            @Override // com.android.internal.telephony.IccSmsInterfaceManager.PhoneFactoryProxy
            public Phone[] getPhones() {
                return PhoneFactory.getPhones();
            }
        };
        this.mPhone = phone;
        this.mContext = context;
        this.mAppOps = appOpsManager;
        this.mDispatchersController = smsDispatchersController;
        this.mSmsPermissions = smsPermissions;
    }

    public void setPhoneFactoryProxy(PhoneFactoryProxy phoneFactoryProxy) {
        this.mPhoneFactoryProxy = phoneFactoryProxy;
    }

    private void enforceNotOnHandlerThread(String str) {
        if (Looper.myLooper() != this.mHandler.getLooper()) {
            return;
        }
        throw new RuntimeException("This method " + str + " will deadlock if called from the handler's thread.");
    }

    protected void markMessagesAsRead(ArrayList<byte[]> arrayList) {
        if (arrayList == null) {
            return;
        }
        IccFileHandler iccFileHandler = this.mPhone.getIccFileHandler();
        if (iccFileHandler == null) {
            if (Rlog.isLoggable("SMS", 3)) {
                loge("markMessagesAsRead - aborting, no icc card present.");
                return;
            }
            return;
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            byte[] bArr = arrayList.get(i);
            if ((bArr[0] & 7) == 3) {
                int length = bArr.length - 1;
                byte[] bArr2 = new byte[length];
                System.arraycopy(bArr, 1, bArr2, 0, length);
                int i2 = i + 1;
                iccFileHandler.updateEFLinearFixed(IccConstants.EF_SMS, i2, makeSmsRecordData(1, bArr2), null, null);
                if (Rlog.isLoggable("SMS", 3)) {
                    log("SMS " + i2 + " marked as read");
                }
            }
        }
    }

    protected void enforceReceiveAndSend(String str) {
        if (TelephonyCommonUtilsStub.get().checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECEIVE_SMS", str);
        this.mContext.enforceCallingOrSelfPermission("android.permission.SEND_SMS", str);
    }

    private void enforceAccessMessageOnICC(String str) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_MESSAGES_ON_ICC", str);
    }

    public boolean updateMessageOnIccEf(String str, int i, int i2, byte[] bArr) {
        log("updateMessageOnIccEf: index=" + i + " status=" + i2 + " ==> (" + Arrays.toString(bArr) + ")");
        enforceReceiveAndSend("Updating message on Icc");
        enforceAccessMessageOnICC("Updating message on Icc");
        enforceNotOnHandlerThread("updateMessageOnIccEf");
        if (this.mAppOps.noteOp("android:write_icc_sms", Binder.getCallingUid(), str) != 0) {
            return false;
        }
        Request request = new Request();
        synchronized (request) {
            try {
                Message messageObtainMessage = this.mHandler.obtainMessage(2, request);
                int i3 = i2 & 1;
                Phone phone = this.mPhone;
                if (i3 == 0) {
                    phone.mCi.deleteSmsOnSim(i, messageObtainMessage);
                } else {
                    IccFileHandler iccFileHandler = phone.getIccFileHandler();
                    if (iccFileHandler == null) {
                        messageObtainMessage.recycle();
                        return false;
                    }
                    iccFileHandler.updateEFLinearFixed(IccConstants.EF_SMS, i, makeSmsRecordData(i2, bArr), null, messageObtainMessage);
                }
                waitForResult(request);
                return ((Boolean) request.mResult).booleanValue();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean copyMessageToIccEf(String str, int i, byte[] bArr, byte[] bArr2) {
        log("copyMessageToIccEf: status=" + i + " ==> pdu=(" + Arrays.toString(bArr) + "), smsc=(" + Arrays.toString(bArr2) + ")");
        enforceReceiveAndSend("Copying message to Icc");
        enforceNotOnHandlerThread("copyMessageToIccEf");
        if (this.mAppOps.noteOp("android:write_icc_sms", Binder.getCallingUid(), str) != 0) {
            return false;
        }
        Request request = new Request();
        synchronized (request) {
            this.mPhone.mCi.writeSmsToSim(i, IccUtils.bytesToHexString(bArr2), IccUtils.bytesToHexString(bArr), this.mHandler.obtainMessage(2, request));
            waitForResult(request);
        }
        return ((Boolean) request.mResult).booleanValue();
    }

    public List<SmsRawData> getAllMessagesFromIccEf(String str) {
        log("getAllMessagesFromEF");
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            TelephonyCommonUtilsStub.get().checkCallingOrSelfPermission(this.mContext, Binder.getCallingUid(), "android.permission.RECEIVE_SMS", "Reading messages from Icc");
        } else {
            this.mContext.enforceCallingOrSelfPermission("android.permission.RECEIVE_SMS", "Reading messages from Icc");
        }
        enforceAccessMessageOnICC("Reading messages from Icc");
        enforceNotOnHandlerThread("getAllMessagesFromIccEf");
        if (this.mAppOps.noteOp("android:read_icc_sms", Binder.getCallingUid(), str) != 0) {
            return new ArrayList();
        }
        Request request = new Request();
        synchronized (request) {
            try {
                IccFileHandler iccFileHandler = this.mPhone.getIccFileHandler();
                if (iccFileHandler == null) {
                    loge("Cannot load Sms records. No icc card?");
                    return null;
                }
                iccFileHandler.loadEFLinearFixedAll(IccConstants.EF_SMS, this.mHandler.obtainMessage(1, request));
                waitForResult(request);
                return (List) request.mResult;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void sendDataWithSelfPermissions(String str, int i, String str2, String str3, String str4, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i3) {
        if (!this.mSmsPermissions.checkCallingOrSelfCanSendSms(str, str2, "Sending SMS message")) {
            returnUnspecifiedFailure(pendingIntent);
        } else {
            sendDataInternal(str, i, str3, str4, i2, bArr, pendingIntent, pendingIntent2, z, i3);
        }
    }

    @Deprecated
    public void sendData(String str, int i, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        sendData(str, i, null, str2, str3, i2, bArr, pendingIntent, pendingIntent2, -1);
    }

    public void sendData(String str, int i, String str2, String str3, String str4, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i3) {
        if (!this.mSmsPermissions.checkCallingCanSendSms(str, str2, "Sending SMS message")) {
            returnUnspecifiedFailure(pendingIntent);
        } else {
            sendDataInternal(str, i, str3, str4, i2, bArr, pendingIntent, pendingIntent2, false, i3);
        }
    }

    public void sendRawPdu(String str, int i, String str2, String str3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i2) {
        this.mDispatchersController.sendRawPdu(str, i, str2, str3, bArr, pendingIntent, pendingIntent2, i2);
    }

    protected void sendDataInternal(String str, int i, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i3) {
        if (Rlog.isLoggable("SMS", 2)) {
            log("sendData: destAddr=" + str2 + " scAddr=" + str3 + " destPort=" + i2 + " data='" + HexDump.toHexString(bArr) + "' sentIntent=" + pendingIntent + " deliveryIntent=" + pendingIntent2 + " isForVVM=" + z);
        }
        this.mDispatchersController.sendData(str, i, filterDestAddress(str2), str3, i2, bArr, pendingIntent, pendingIntent2, z, i3);
    }

    public void sendText(String str, int i, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, long j, boolean z2, int i2) {
        sendTextInternal(str, i, str2, str3, str4, pendingIntent, pendingIntent2, z, -1, false, -1, false, j, z2, i2);
    }

    public void sendTextWithSelfPermissions(String str, int i, String str2, String str3, String str4, String str5, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, boolean z2, int i2) {
        if (!this.mSmsPermissions.checkCallingOrSelfCanSendSms(str, str2, "Sending SMS message")) {
            returnUnspecifiedFailure(pendingIntent);
        } else {
            sendTextInternal(str, i, str3, str4, str5, pendingIntent, pendingIntent2, z, -1, false, -1, z2, 0L, i2);
        }
    }

    private void sendTextInternal(String str, int i, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, boolean z2, int i3, boolean z3, long j, int i4) {
        sendTextInternal(str, i, str2, str3, str4, pendingIntent, pendingIntent2, z, i2, z2, i3, z3, j, false, i4);
    }

    protected void sendTextInternal(String str, int i, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, boolean z2, int i3, boolean z3, long j, boolean z4, int i4) {
        String str5;
        String str6;
        PendingIntent pendingIntent3;
        PendingIntent pendingIntent4;
        int i5;
        boolean z5;
        int i6;
        boolean z6;
        if (Rlog.isLoggable("SMS", 2)) {
            StringBuilder sb = new StringBuilder("sendText: destAddr=");
            sb.append(str2);
            sb.append(" scAddr=");
            str5 = str3;
            sb.append(str5);
            sb.append(" text='");
            str6 = str4;
            sb.append(str6);
            sb.append("' sentIntent=");
            pendingIntent3 = pendingIntent;
            sb.append(pendingIntent3);
            sb.append(" deliveryIntent=");
            pendingIntent4 = pendingIntent2;
            sb.append(pendingIntent4);
            sb.append(" priority=");
            i5 = i2;
            sb.append(i5);
            sb.append(" expectMore=");
            z5 = z2;
            sb.append(z5);
            sb.append(" validityPeriod=");
            i6 = i3;
            sb.append(i6);
            sb.append(" isForVVM=");
            z6 = z3;
            sb.append(z6);
            sb.append(" ");
            sb.append(SmsController.formatCrossStackMessageId(j));
            log(sb.toString());
        } else {
            str5 = str3;
            str6 = str4;
            pendingIntent3 = pendingIntent;
            pendingIntent4 = pendingIntent2;
            i5 = i2;
            z5 = z2;
            i6 = i3;
            z6 = z3;
        }
        notifyIfOutgoingEmergencySms(str2);
        this.mDispatchersController.sendText(filterDestAddress(str2), str5, str6, pendingIntent3, pendingIntent4, null, str, i, z, i5, z5, i6, z6, j, z4, i4);
    }

    public void sendTextWithOptions(String str, int i, String str2, String str3, String str4, String str5, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, boolean z2, int i3, int i4) {
        if (!this.mSmsPermissions.checkCallingCanSendText(z, str, str2, "Sending SMS message")) {
            returnUnspecifiedFailure(pendingIntent);
        } else {
            sendTextInternal(str, i, str3, str4, str5, pendingIntent, pendingIntent2, z, i2, z2, i3, false, 0L, i4);
        }
    }

    public void injectSmsPdu(byte[] bArr, String str, final PendingIntent pendingIntent) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0) {
            this.mSmsPermissions.enforceCallerIsImsAppOrCarrierApp("injectSmsPdu");
        }
        if (Rlog.isLoggable("SMS", 2)) {
            log("pdu: " + IccUtils.bytesToHexString(bArr) + "\n format=" + str + "\n receivedIntent=" + pendingIntent);
        }
        this.mDispatchersController.injectSmsPdu(bArr, str, false, new SmsDispatchersController.SmsInjectionCallback() { // from class: com.android.internal.telephony.IccSmsInterfaceManager$$ExternalSyntheticLambda0
            @Override // com.android.internal.telephony.SmsDispatchersController.SmsInjectionCallback
            public final void onSmsInjectedResult(int i) {
                this.f$0.lambda$injectSmsPdu$0(pendingIntent, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$injectSmsPdu$0(PendingIntent pendingIntent, int i) {
        if (pendingIntent != null) {
            try {
                pendingIntent.send(i);
            } catch (PendingIntent.CanceledException unused) {
                loge("receivedIntent cancelled.");
            }
        }
    }

    public void sendMultipartText(String str, int i, String str2, String str3, String str4, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, long j, int i2) {
        sendMultipartTextWithOptions(str, i, str2, str3, str4, list, list2, list3, z, -1, false, -1, j, i2);
    }

    public void sendMultipartTextWithOptions(String str, int i, String str2, String str3, String str4, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, int i2, boolean z2, int i3, long j, int i4) {
        String strConcat;
        String str5 = str;
        boolean z3 = z;
        if (!this.mSmsPermissions.checkCallingCanSendText(z3, str5, str2, "Sending SMS message")) {
            returnUnspecifiedFailure(list2);
            return;
        }
        if (Rlog.isLoggable("SMS", 2)) {
            Iterator<String> it = list.iterator();
            int i5 = 0;
            while (it.hasNext()) {
                log("sendMultipartTextWithOptions: destAddr=" + str3 + ", srAddr=" + str4 + ", part[" + i5 + "]=" + it.next() + " " + SmsController.formatCrossStackMessageId(j));
                i5++;
            }
        }
        notifyIfOutgoingEmergencySms(str3);
        String strFilterDestAddress = filterDestAddress(str3);
        if (list.size() > 1 && list.size() < 10 && !SmsMessage.hasEmsSupport()) {
            int i6 = 0;
            while (i6 < list.size()) {
                String str6 = list.get(i6);
                if (SmsMessage.shouldAppendPageNumberAsPrefix()) {
                    strConcat = String.valueOf(i6 + 1) + '/' + list.size() + ' ' + str6;
                } else {
                    strConcat = str6.concat(" " + String.valueOf(i6 + 1) + '/' + list.size());
                }
                String str7 = strConcat;
                PendingIntent pendingIntent = null;
                PendingIntent pendingIntent2 = (list2 == null || list2.size() <= i6) ? null : list2.get(i6);
                if (list3 != null && list3.size() > i6) {
                    pendingIntent = list3.get(i6);
                }
                this.mDispatchersController.sendText(strFilterDestAddress, str4, str7, pendingIntent2, pendingIntent, null, str5, i, z3, i2, z2, i3, false, j, i4);
                i6++;
                str5 = str;
                z3 = z;
            }
            return;
        }
        this.mDispatchersController.sendMultipartText(strFilterDestAddress, str4, (ArrayList) list, (ArrayList) list2, (ArrayList) list3, null, str, i, z, i2, z2, i3, j, i4);
    }

    public int getPremiumSmsPermission(String str) {
        return this.mDispatchersController.getPremiumSmsPermission(str);
    }

    public void setPremiumSmsPermission(String str, int i) {
        this.mDispatchersController.setPremiumSmsPermission(str, i);
    }

    protected ArrayList<SmsRawData> buildValidRawData(ArrayList<byte[]> arrayList) {
        int size = arrayList.size();
        ArrayList<SmsRawData> arrayList2 = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if ((arrayList.get(i)[0] & 1) == 0) {
                arrayList2.add(null);
            } else {
                arrayList2.add(new SmsRawData(arrayList.get(i)));
            }
        }
        return arrayList2;
    }

    protected byte[] makeSmsRecordData(int i, byte[] bArr) {
        byte[] bArr2 = new byte[176];
        bArr2[0] = (byte) (i & 7);
        System.arraycopy(bArr, 0, bArr2, 1, bArr.length);
        for (int length = bArr.length + 1; length < 176; length++) {
            bArr2[length] = -1;
        }
        return bArr2;
    }

    public String getSmscAddressFromIccEf(String str) {
        if (!this.mSmsPermissions.checkCallingOrSelfCanGetSmscAddress(str, "getSmscAddressFromIccEf")) {
            loge("Caller do not have permission to call GetSmscAddress");
            return null;
        }
        enforceNotOnHandlerThread("getSmscAddressFromIccEf");
        Request request = new Request();
        synchronized (request) {
            this.mPhone.mCi.getSmscAddress(this.mHandler.obtainMessage(5, request));
            waitForResult(request);
        }
        return (String) request.mResult;
    }

    public boolean setSmscAddressOnIccEf(String str, String str2) {
        if (!this.mSmsPermissions.checkCallingOrSelfCanSetSmscAddress(str, "setSmscAddressOnIccEf")) {
            loge("Caller do not have permission to call SetSmscAddress");
            return false;
        }
        enforceNotOnHandlerThread("setSmscAddressOnIccEf");
        Request request = new Request();
        synchronized (request) {
            this.mPhone.mCi.setSmscAddress(str2, this.mHandler.obtainMessage(6, request));
            waitForResult(request);
        }
        return ((Boolean) request.mResult).booleanValue();
    }

    public boolean enableCellBroadcastRange(int i, int i2, int i3) {
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "enabling cell broadcast range [" + i + "-" + i2 + "]. ranType=" + i3);
        if (i3 == 1) {
            return enableGsmBroadcastRange(i, i2);
        }
        if (i3 == 2) {
            return enableCdmaBroadcastRange(i, i2);
        }
        Phone$$ExternalSyntheticBUOutline0.m("Not a supported RAN Type");
        return false;
    }

    public boolean disableCellBroadcastRange(int i, int i2, int i3) {
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "disabling cell broadcast range [" + i + "-" + i2 + "]. ranType=" + i3);
        if (i3 == 1) {
            return disableGsmBroadcastRange(i, i2);
        }
        if (i3 == 2) {
            return disableCdmaBroadcastRange(i, i2);
        }
        Phone$$ExternalSyntheticBUOutline0.m("Not a supported RAN Type");
        return false;
    }

    public synchronized boolean enableGsmBroadcastRange(int i, int i2) {
        try {
            if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                TelephonyCommonUtilsStub.get().checkCallingPermission(this.mContext, Binder.getCallingUid(), "android.permission.RECEIVE_EMERGENCY_BROADCAST", "Enabling cell broadcast SMS");
            } else {
                this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Enabling cell broadcast SMS");
            }
            if (!this.mCellBroadcastRangeManager.enableRange(i, i2, this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid()))) {
                String str = "Failed to add GSM cell broadcast channels range " + i + " to " + i2;
                log(str);
                this.mCellBroadcastLocalLog.log(str);
                return false;
            }
            String str2 = "Added GSM cell broadcast channels range " + i + " to " + i2;
            log(str2);
            this.mCellBroadcastLocalLog.log(str2);
            setCellBroadcastActivation(!this.mCellBroadcastRangeManager.isEmpty());
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized boolean disableGsmBroadcastRange(int i, int i2) {
        try {
            if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                TelephonyCommonUtilsStub.get().checkCallingPermission(this.mContext, Binder.getCallingUid(), "android.permission.RECEIVE_EMERGENCY_BROADCAST", "Disabling cell broadcast SMS");
            } else {
                this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Disabling cell broadcast SMS");
            }
            if (!this.mCellBroadcastRangeManager.disableRange(i, i2, this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid()))) {
                String str = "Failed to remove GSM cell broadcast channels range " + i + " to " + i2;
                log(str);
                this.mCellBroadcastLocalLog.log(str);
                return false;
            }
            String str2 = "Removed GSM cell broadcast channels range " + i + " to " + i2;
            log(str2);
            this.mCellBroadcastLocalLog.log(str2);
            setCellBroadcastActivation(!this.mCellBroadcastRangeManager.isEmpty());
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized boolean enableCdmaBroadcastRange(int i, int i2) {
        try {
            if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                TelephonyCommonUtilsStub.get().checkCallingPermission(this.mContext, Binder.getCallingUid(), "android.permission.RECEIVE_EMERGENCY_BROADCAST", "Enabling cdma broadcast SMS");
            } else {
                this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Enabling cdma broadcast SMS");
            }
            if (!this.mCdmaBroadcastRangeManager.enableRange(i, i2, this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid()))) {
                String str = "Failed to add cdma broadcast channels range " + i + " to " + i2;
                log(str);
                this.mCellBroadcastLocalLog.log(str);
                return false;
            }
            String str2 = "Added cdma broadcast channels range " + i + " to " + i2;
            log(str2);
            this.mCellBroadcastLocalLog.log(str2);
            setCdmaBroadcastActivation(!this.mCdmaBroadcastRangeManager.isEmpty());
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized boolean disableCdmaBroadcastRange(int i, int i2) {
        try {
            if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                TelephonyCommonUtilsStub.get().checkCallingPermission(this.mContext, Binder.getCallingUid(), "android.permission.RECEIVE_EMERGENCY_BROADCAST", "Disabling cell broadcast SMS");
            } else {
                this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Disabling cell broadcast SMS");
            }
            if (!this.mCdmaBroadcastRangeManager.disableRange(i, i2, this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid()))) {
                String str = "Failed to remove cdma broadcast channels range " + i + " to " + i2;
                log(str);
                this.mCellBroadcastLocalLog.log(str);
                return false;
            }
            String str2 = "Removed cdma broadcast channels range " + i + " to " + i2;
            log(str2);
            this.mCellBroadcastLocalLog.log(str2);
            setCdmaBroadcastActivation(!this.mCdmaBroadcastRangeManager.isEmpty());
            return true;
        } catch (Throwable th) {
            throw th;
        }
    }

    public void resetAllCellBroadcastRanges() {
        this.mContext.enforceCallingPermission("android.permission.MODIFY_CELL_BROADCASTS", "resetAllCellBroadcastRanges");
        this.mCdmaBroadcastRangeManager.clearRanges();
        this.mCellBroadcastRangeManager.clearRanges();
        log("Cell broadcast ranges reset.");
    }

    class CellBroadcastRangeManager extends IntRangeManager {
        private ArrayList mConfigList = new ArrayList();

        CellBroadcastRangeManager() {
        }

        @Override // com.android.internal.telephony.IntRangeManager
        protected void startUpdate() {
            this.mConfigList.clear();
        }

        @Override // com.android.internal.telephony.IntRangeManager
        protected void addRange(int i, int i2, boolean z) {
            this.mConfigList.add(new SmsBroadcastConfigInfo(i, i2, 0, 255, z));
        }

        @Override // com.android.internal.telephony.IntRangeManager
        protected boolean finishUpdate() {
            if (this.mConfigList.isEmpty()) {
                return true;
            }
            ArrayList arrayList = this.mConfigList;
            return IccSmsInterfaceManager.this.setCellBroadcastConfig((SmsBroadcastConfigInfo[]) arrayList.toArray(new SmsBroadcastConfigInfo[arrayList.size()]));
        }
    }

    class CdmaBroadcastRangeManager extends IntRangeManager {
        private ArrayList mConfigList = new ArrayList();

        CdmaBroadcastRangeManager() {
        }

        @Override // com.android.internal.telephony.IntRangeManager
        protected void startUpdate() {
            this.mConfigList.clear();
        }

        @Override // com.android.internal.telephony.IntRangeManager
        protected void addRange(int i, int i2, boolean z) {
            this.mConfigList.add(new CdmaSmsBroadcastConfigInfo(i, i2, 1, z));
        }

        @Override // com.android.internal.telephony.IntRangeManager
        protected boolean finishUpdate() {
            if (this.mConfigList.isEmpty()) {
                return true;
            }
            ArrayList arrayList = this.mConfigList;
            return IccSmsInterfaceManager.this.setCdmaBroadcastConfig((CdmaSmsBroadcastConfigInfo[]) arrayList.toArray(new CdmaSmsBroadcastConfigInfo[arrayList.size()]));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setCellBroadcastConfig(SmsBroadcastConfigInfo[] smsBroadcastConfigInfoArr) {
        log("Calling setGsmBroadcastConfig with " + smsBroadcastConfigInfoArr.length + " configurations");
        enforceNotOnHandlerThread("setCellBroadcastConfig");
        Request request = new Request();
        synchronized (request) {
            this.mPhone.mCi.setGsmBroadcastConfig(smsBroadcastConfigInfoArr, this.mHandler.obtainMessage(4, request));
            waitForResult(request);
        }
        return ((Boolean) request.mResult).booleanValue();
    }

    protected boolean setCellBroadcastActivation(boolean z) {
        log("Calling setCellBroadcastActivation(" + z + ')');
        enforceNotOnHandlerThread("setCellBroadcastConfig");
        Request request = new Request();
        synchronized (request) {
            this.mPhone.mCi.setGsmBroadcastActivation(z, this.mHandler.obtainMessage(3, request));
            waitForResult(request);
        }
        return ((Boolean) request.mResult).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setCdmaBroadcastConfig(CdmaSmsBroadcastConfigInfo[] cdmaSmsBroadcastConfigInfoArr) {
        log("Calling setCdmaBroadcastConfig with " + cdmaSmsBroadcastConfigInfoArr.length + " configurations");
        enforceNotOnHandlerThread("setCdmaBroadcastConfig");
        Request request = new Request();
        synchronized (request) {
            this.mPhone.mCi.setCdmaBroadcastConfig(cdmaSmsBroadcastConfigInfoArr, this.mHandler.obtainMessage(4, request));
            waitForResult(request);
        }
        return ((Boolean) request.mResult).booleanValue();
    }

    private boolean setCdmaBroadcastActivation(boolean z) {
        log("Calling setCdmaBroadcastActivation(" + z + ")");
        enforceNotOnHandlerThread("setCdmaBroadcastActivation");
        Request request = new Request();
        synchronized (request) {
            this.mPhone.mCi.setCdmaBroadcastActivation(z, this.mHandler.obtainMessage(3, request));
            waitForResult(request);
        }
        return ((Boolean) request.mResult).booleanValue();
    }

    protected void log(String str) {
        Rlog.d("IccSmsInterfaceManager", str);
    }

    protected void loge(String str) {
        Rlog.e("IccSmsInterfaceManager", str);
    }

    protected void loge(String str, Throwable th) {
        Rlog.e("IccSmsInterfaceManager", str, th);
    }

    public boolean isImsSmsSupported() {
        return this.mDispatchersController.isIms();
    }

    public String getImsSmsFormat() {
        return this.mDispatchersController.getImsSmsFormat();
    }

    @Deprecated
    public void sendStoredText(String str, int i, Uri uri, String str2, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws Throwable {
        sendStoredText(str, i, null, uri, str2, pendingIntent, pendingIntent2, -1);
    }

    public void sendStoredText(String str, int i, String str2, Uri uri, String str3, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i2) throws Throwable {
        String str4;
        PendingIntent pendingIntent3;
        if (!this.mSmsPermissions.checkCallingCanSendSms(str, str2, "Sending SMS message")) {
            returnUnspecifiedFailure(pendingIntent);
            return;
        }
        if (Rlog.isLoggable("SMS", 2)) {
            StringBuilder sb = new StringBuilder("sendStoredText: scAddr=");
            str4 = str3;
            sb.append(str4);
            sb.append(" messageUri=");
            sb.append(uri);
            sb.append(" sentIntent=");
            sb.append(pendingIntent);
            sb.append(" deliveryIntent=");
            pendingIntent3 = pendingIntent2;
            sb.append(pendingIntent3);
            log(sb.toString());
        } else {
            str4 = str3;
            pendingIntent3 = pendingIntent2;
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (!isSendableStoredMessage(contentResolver, uri)) {
            loge("sendStoredText: message not sendable");
            returnUnspecifiedFailure(pendingIntent);
            return;
        }
        String[] strArrLoadTextAndAddress = loadTextAndAddress(contentResolver, uri);
        if (strArrLoadTextAndAddress == null) {
            loge("sendStoredText: can not load text");
            returnUnspecifiedFailure(pendingIntent);
        } else {
            notifyIfOutgoingEmergencySms(strArrLoadTextAndAddress[1]);
            String strFilterDestAddress = filterDestAddress(strArrLoadTextAndAddress[1]);
            strArrLoadTextAndAddress[1] = strFilterDestAddress;
            this.mDispatchersController.sendText(strFilterDestAddress, str4, strArrLoadTextAndAddress[0], pendingIntent, pendingIntent3, uri, str, i, true, -1, false, -1, false, 0L, i2);
        }
    }

    @Deprecated
    public void sendStoredMultipartText(String str, int i, Uri uri, String str2, List<PendingIntent> list, List<PendingIntent> list2) throws Throwable {
        sendStoredMultipartText(str, i, null, uri, str2, list, list2, -1);
    }

    public void sendStoredMultipartText(String str, int i, String str2, Uri uri, String str3, List<PendingIntent> list, List<PendingIntent> list2, int i2) throws Throwable {
        String strConcat;
        Uri uri2 = uri;
        List<PendingIntent> list3 = list;
        String str4 = str;
        if (!this.mSmsPermissions.checkCallingCanSendSms(str4, str2, "Sending SMS message")) {
            returnUnspecifiedFailure(list3);
            return;
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (!isSendableStoredMessage(contentResolver, uri2)) {
            loge("sendStoredMultipartText: message not sendable");
            returnUnspecifiedFailure(list3);
            return;
        }
        String[] strArrLoadTextAndAddress = loadTextAndAddress(contentResolver, uri2);
        if (strArrLoadTextAndAddress == null) {
            loge("sendStoredMultipartText: can not load text");
            returnUnspecifiedFailure(list3);
            return;
        }
        int i3 = 0;
        ArrayList<String> arrayListDivideMessage = SmsManager.getDefault().divideMessage(strArrLoadTextAndAddress[0]);
        if (arrayListDivideMessage != null) {
            char c = 1;
            if (arrayListDivideMessage.size() >= 1) {
                notifyIfOutgoingEmergencySms(strArrLoadTextAndAddress[1]);
                strArrLoadTextAndAddress[1] = filterDestAddress(strArrLoadTextAndAddress[1]);
                if (arrayListDivideMessage.size() > 1 && arrayListDivideMessage.size() < 10 && !SmsMessage.hasEmsSupport()) {
                    while (i3 < arrayListDivideMessage.size()) {
                        String str5 = arrayListDivideMessage.get(i3);
                        if (SmsMessage.shouldAppendPageNumberAsPrefix()) {
                            strConcat = String.valueOf(i3 + 1) + '/' + arrayListDivideMessage.size() + ' ' + str5;
                        } else {
                            strConcat = str5.concat(" " + String.valueOf(i3 + 1) + '/' + arrayListDivideMessage.size());
                        }
                        PendingIntent pendingIntent = null;
                        PendingIntent pendingIntent2 = (list3 == null || list3.size() <= i3) ? null : list3.get(i3);
                        if (list2 != 0 && list2.size() > i3) {
                            pendingIntent = list2.get(i3);
                        }
                        this.mDispatchersController.sendText(strArrLoadTextAndAddress[c], str3, strConcat, pendingIntent2, pendingIntent, uri2, str4, i, true, -1, false, -1, false, 0L, i2);
                        i3++;
                        str4 = str;
                        uri2 = uri;
                        list3 = list;
                        arrayListDivideMessage = arrayListDivideMessage;
                        c = c;
                    }
                    return;
                }
                this.mDispatchersController.sendMultipartText(strArrLoadTextAndAddress[1], str3, arrayListDivideMessage, (ArrayList) list, (ArrayList) list2, uri, str, i, true, -1, false, -1, 0L, i2);
                return;
            }
        }
        loge("sendStoredMultipartText: can not divide text");
        returnUnspecifiedFailure(list3);
    }

    public int getSmsCapacityOnIcc(String str, String str2) {
        boolean zCheckCallingOrSelfReadPhoneState = TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, this.mPhone.getSubId(), str, str2, "getSmsCapacityOnIcc");
        int smsCapacityOnIcc = 0;
        if (!zCheckCallingOrSelfReadPhoneState) {
            return 0;
        }
        if (this.mPhone.getIccRecordsLoaded()) {
            UiccProfile uiccProfileForPhone = UiccController.getInstance().getUiccProfileForPhone(this.mPhone.getPhoneId());
            if (uiccProfileForPhone != null) {
                smsCapacityOnIcc = uiccProfileForPhone.getIccRecords().getSmsCapacityOnIcc();
            } else {
                loge("uiccProfile is null");
            }
        } else {
            loge("getSmsCapacityOnIcc - aborting, no icc card present.");
        }
        log("getSmsCapacityOnIcc().numberOnIcc = " + smsCapacityOnIcc);
        return smsCapacityOnIcc;
    }

    private boolean isSendableStoredMessage(ContentResolver contentResolver, Uri uri) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = contentResolver.query(uri, new String[]{"type"}, null, null, null);
                if (cursorQuery != null && cursorQuery.moveToFirst()) {
                    int i = cursorQuery.getInt(0);
                    boolean z = i == 3 || i == 5 || i == 4;
                    cursorQuery.close();
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return z;
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return false;
            } catch (SQLiteException e) {
                loge("isSendableStoredMessage: query message type failed", e);
                if (cursorQuery != null) {
                }
            }
        } catch (Throwable th) {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:26:0x0054  */
    public String[] loadTextAndAddress(ContentResolver contentResolver, Uri uri) throws Throwable {
        Throwable th;
        SQLiteException sQLiteException;
        Cursor cursorQuery;
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        Cursor cursor = null;
        try {
            cursorQuery = contentResolver.query(uri, new String[]{"body", "address"}, null, null, null);
            if (cursorQuery != null) {
                try {
                    try {
                        if (cursorQuery.moveToFirst()) {
                            String[] strArr = {cursorQuery.getString(0), cursorQuery.getString(1)};
                            cursorQuery.close();
                            Binder.restoreCallingIdentity(jClearCallingIdentity);
                            return strArr;
                        }
                    } catch (SQLiteException e) {
                        sQLiteException = e;
                        loge("loadText: query message text failed", sQLiteException);
                        if (cursorQuery != null) {
                        }
                        Binder.restoreCallingIdentity(jClearCallingIdentity);
                        return null;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = cursorQuery;
                    if (cursor != null) {
                        cursor.close();
                    }
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    throw th;
                }
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (SQLiteException e2) {
            sQLiteException = e2;
            cursorQuery = null;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return null;
    }

    public void notifyIfOutgoingEmergencySms(String str) {
        EmergencyNumber emergencyNumber;
        Phone[] phones = this.mPhoneFactoryProxy.getPhones();
        EmergencyNumber emergencyNumber2 = getEmergencyNumber(this.mPhone, str);
        if (emergencyNumber2 != null) {
            this.mPhone.notifyOutgoingEmergencySms(emergencyNumber2);
            return;
        }
        if (phones.length > 1) {
            for (Phone phone : phones) {
                if (phone.getPhoneId() != this.mPhone.getPhoneId() && (emergencyNumber = getEmergencyNumber(phone, str)) != null) {
                    this.mPhone.notifyOutgoingEmergencySms(emergencyNumber);
                    return;
                }
            }
        }
    }

    private EmergencyNumber getEmergencyNumber(Phone phone, String str) {
        if (!phone.hasCalling() && !phone.hasMessaging()) {
            log("getEmergencyNumber: no calling or messaging capability");
            return null;
        }
        EmergencyNumberTracker emergencyNumberTracker = phone.getEmergencyNumberTracker();
        if (emergencyNumberTracker == null) {
            log("getEmergencyNumber: emergency number tracker is null");
            return null;
        }
        return emergencyNumberTracker.getEmergencyNumber(str);
    }

    private void returnUnspecifiedFailure(PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            try {
                pendingIntent.send(1);
            } catch (PendingIntent.CanceledException unused) {
            }
        }
    }

    private void returnUnspecifiedFailure(List list) {
        if (list == null) {
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            returnUnspecifiedFailure((PendingIntent) it.next());
        }
    }

    private String filterDestAddress(String str) {
        if (MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).smsSendRestricted(this.mPhone, str)) {
            return "";
        }
        String strFilterDestAddr = SmsNumberUtils.filterDestAddr(this.mContext, this.mPhone.getSubId(), str);
        return strFilterDestAddr != null ? strFilterDestAddr : str;
    }

    protected void waitForResult(Request request) {
        synchronized (request) {
            while (!request.mStatus.get()) {
                try {
                    request.wait();
                } catch (InterruptedException unused) {
                    log("Interrupted while waiting for result");
                }
            }
        }
    }

    public InboundSmsHandler getInboundSmsHandler(boolean z) {
        return this.mDispatchersController.getInboundSmsHandler(z);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Enabled GSM channels: " + this.mCellBroadcastRangeManager);
        printWriter.println("Enabled CDMA channels: " + this.mCdmaBroadcastRangeManager);
        printWriter.println("CellBroadcast log:");
        this.mCellBroadcastLocalLog.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("SMS dispatcher controller log:");
        this.mDispatchersController.dump(fileDescriptor, printWriter, strArr);
        printWriter.flush();
    }
}
