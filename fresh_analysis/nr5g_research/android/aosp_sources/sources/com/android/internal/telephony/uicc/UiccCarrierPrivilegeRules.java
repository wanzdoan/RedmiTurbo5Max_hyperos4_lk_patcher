package com.android.internal.telephony.uicc;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.telephony.AnomalyReporter;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class UiccCarrierPrivilegeRules extends Handler {
    protected static final int EVENT_OPEN_LOGICAL_CHANNEL_DONE = 1;
    protected static final int STATE_ERROR = 2;
    private static final UUID UNEXPECTED_RESULT_TYPE_UUID = UUID.fromString("c60775d7-463d-4c3e-8212-32b0a2333069");
    private int mAIDInUse;
    private List mAccessRules;
    private int mChannelId;
    private boolean mCheckedRules;
    private Message mLoadedCallback;
    private int mRetryCount;
    private final Runnable mRetryRunnable;
    private String mRules;
    private AtomicInteger mState;
    private LocalLog mStatusMessage;
    private UiccPkcs15 mUiccPkcs15;
    private UiccProfile mUiccProfile;

    protected static void log(String str) {
    }

    public static class TLV {
        private Integer length;
        private String lengthBytes;
        private String tag;
        private String value;

        public TLV(String str) {
            this.tag = str;
        }

        public String getValue() {
            String str = this.value;
            return str == null ? "" : str;
        }

        public String parseLength(String str) {
            int length = this.tag.length();
            int i = length + 2;
            int i2 = Integer.parseInt(str.substring(length, i), 16);
            if (i2 < 128) {
                this.length = Integer.valueOf(i2 * 2);
                this.lengthBytes = str.substring(length, i);
            } else {
                int i3 = ((i2 - 128) * 2) + i;
                this.length = Integer.valueOf(Integer.parseInt(str.substring(i, i3), 16) * 2);
                this.lengthBytes = str.substring(length, i3);
            }
            UiccCarrierPrivilegeRules.log("TLV parseLength length=" + this.length + "lenghtBytes: " + this.lengthBytes);
            return this.lengthBytes;
        }

        public String parse(String str, boolean z) {
            UiccCarrierPrivilegeRules.log("Parse TLV: " + this.tag);
            if (!str.startsWith(this.tag)) {
                Phone$$ExternalSyntheticBUOutline0.m("Tags don't match.");
                return null;
            }
            int length = this.tag.length();
            if (length + 2 > str.length()) {
                Phone$$ExternalSyntheticBUOutline0.m("No length.");
                return null;
            }
            parseLength(str);
            int length2 = length + this.lengthBytes.length();
            UiccCarrierPrivilegeRules.log("index=" + length2 + " length=" + this.length + "data.length=" + str.length());
            int length3 = str.length() - (this.length.intValue() + length2);
            if (length3 < 0) {
                Phone$$ExternalSyntheticBUOutline0.m("Not enough data.");
                return null;
            }
            if (z && length3 != 0) {
                Phone$$ExternalSyntheticBUOutline0.m("Did not consume all.");
                return null;
            }
            this.value = str.substring(length2, this.length.intValue() + length2);
            UiccCarrierPrivilegeRules.log("Got TLV: " + this.tag + "," + this.length + "," + this.value);
            return str.substring(length2 + this.length.intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openChannel(int i) {
        this.mUiccProfile.iccOpenLogicalChannel(i == 0 ? "A00000015144414300" : "A00000015141434C00", 0, obtainMessage(1, 0, i, null));
    }

    public UiccCarrierPrivilegeRules(UiccProfile uiccProfile, Message message) {
        this.mStatusMessage = new LocalLog(64);
        this.mCheckedRules = false;
        this.mRetryRunnable = new Runnable() { // from class: com.android.internal.telephony.uicc.UiccCarrierPrivilegeRules.1
            @Override // java.lang.Runnable
            public void run() {
                UiccCarrierPrivilegeRules uiccCarrierPrivilegeRules = UiccCarrierPrivilegeRules.this;
                uiccCarrierPrivilegeRules.openChannel(uiccCarrierPrivilegeRules.mAIDInUse);
            }
        };
        log("Creating UiccCarrierPrivilegeRules");
        this.mUiccProfile = uiccProfile;
        this.mState = new AtomicInteger(0);
        this.mStatusMessage.log("Not loaded.");
        this.mLoadedCallback = message;
        this.mRules = "";
        this.mAccessRules = new ArrayList();
        this.mAIDInUse = 0;
        openChannel(0);
    }

    public UiccCarrierPrivilegeRules(List<UiccAccessRule> list) {
        this.mStatusMessage = new LocalLog(64);
        this.mCheckedRules = false;
        this.mRetryRunnable = new Runnable() { // from class: com.android.internal.telephony.uicc.UiccCarrierPrivilegeRules.1
            @Override // java.lang.Runnable
            public void run() {
                UiccCarrierPrivilegeRules uiccCarrierPrivilegeRules = UiccCarrierPrivilegeRules.this;
                uiccCarrierPrivilegeRules.openChannel(uiccCarrierPrivilegeRules.mAIDInUse);
            }
        };
        this.mAccessRules = list;
        this.mState = new AtomicInteger(1);
        this.mRules = "";
        this.mStatusMessage.log("Loaded from test rules.");
    }

    public boolean areCarrierPriviligeRulesLoaded() {
        return this.mState.get() != 0;
    }

    public boolean areCarrierPrivilegeRulesLoaded() {
        return this.mState.get() != 0;
    }

    public boolean hasCarrierPrivilegeRules() {
        List list;
        return (this.mState.get() == 0 || (list = this.mAccessRules) == null || list.size() <= 0) ? false : true;
    }

    public List<String> getPackageNames() {
        ArrayList arrayList = new ArrayList();
        List<UiccAccessRule> list = this.mAccessRules;
        if (list != null) {
            for (UiccAccessRule uiccAccessRule : list) {
                if (!TextUtils.isEmpty(uiccAccessRule.getPackageName())) {
                    arrayList.add(uiccAccessRule.getPackageName());
                }
            }
        }
        return arrayList;
    }

    public List<UiccAccessRule> getAccessRules() {
        List list = this.mAccessRules;
        if (list == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(list);
    }

    public int getCarrierPrivilegeStatus(Signature signature, String str) {
        int i = this.mState.get();
        if (i == 0) {
            return -1;
        }
        if (i == 2) {
            return -2;
        }
        Iterator it = this.mAccessRules.iterator();
        while (it.hasNext()) {
            int carrierPrivilegeStatus = ((UiccAccessRule) it.next()).getCarrierPrivilegeStatus(signature, str);
            if (carrierPrivilegeStatus != 0) {
                return carrierPrivilegeStatus;
            }
        }
        return 0;
    }

    public int getCarrierPrivilegeStatus(PackageManager packageManager, String str) {
        try {
            if (!hasCarrierPrivilegeRules()) {
                int i = this.mState.get();
                if (i == 0) {
                    return -1;
                }
                return i == 2 ? -2 : 0;
            }
            return getCarrierPrivilegeStatus(packageManager.getPackageInfo(str, 671121408));
        } catch (PackageManager.NameNotFoundException unused) {
            log("Package " + str + " not found for carrier privilege status check");
            return 0;
        }
    }

    public int getCarrierPrivilegeStatus(PackageInfo packageInfo) {
        int i = this.mState.get();
        if (i == 0) {
            return -1;
        }
        if (i == 2) {
            return -2;
        }
        Iterator it = this.mAccessRules.iterator();
        while (it.hasNext()) {
            int carrierPrivilegeStatus = ((UiccAccessRule) it.next()).getCarrierPrivilegeStatus(packageInfo);
            if (carrierPrivilegeStatus != 0) {
                return carrierPrivilegeStatus;
            }
        }
        return 0;
    }

    public int getCarrierPrivilegeStatusForCurrentTransaction(PackageManager packageManager) {
        return getCarrierPrivilegeStatusForUid(packageManager, Binder.getCallingUid());
    }

    public int getCarrierPrivilegeStatusForUid(PackageManager packageManager, int i) {
        String[] packagesForUid = packageManager.getPackagesForUid(i);
        if (packagesForUid != null) {
            for (String str : packagesForUid) {
                int carrierPrivilegeStatus = getCarrierPrivilegeStatus(packageManager, str);
                if (carrierPrivilegeStatus != 0) {
                    return carrierPrivilegeStatus;
                }
            }
        }
        return 0;
    }

    public static boolean shouldRetry(AsyncResult asyncResult, int i) {
        int i2;
        Throwable th = asyncResult.exception;
        if (!(th instanceof CommandException) || i >= 2) {
            return false;
        }
        CommandException.Error commandError = ((CommandException) th).getCommandError();
        Object obj = asyncResult.result;
        int[] iArr = (int[]) obj;
        if (obj == null || iArr.length != 3) {
            i2 = 0;
        } else {
            i2 = Integer.parseInt(IccUtils.bytesToHexString(new byte[]{(byte) iArr[1], (byte) iArr[2]}), 16);
            log("status code: " + String.valueOf(i2));
        }
        return commandError == CommandException.Error.MISSING_RESOURCE || (commandError == CommandException.Error.NO_SUCH_ELEMENT && i2 == 27013) || (commandError == CommandException.Error.INTERNAL_ERR && i2 == 27033);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Object obj;
        String str;
        Object obj2;
        byte[] bArr;
        this.mAIDInUse = message.arg2;
        int i = message.what;
        if (i == 1) {
            log("EVENT_OPEN_LOGICAL_CHANNEL_DONE");
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception == null && (obj = asyncResult.result) != null && (obj instanceof int[])) {
                int i2 = ((int[]) obj)[0];
                this.mChannelId = i2;
                this.mUiccProfile.iccTransmitApduLogicalChannel(i2, 128, NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA, 255, 64, 0, "", false, obtainMessage(2, i2, this.mAIDInUse));
                return;
            }
            Object obj3 = asyncResult.result;
            if (obj3 != null && !(obj3 instanceof int[])) {
                log("Unexpected result type for EVENT_OPEN_LOGICAL_CHANNEL_DONE : ".concat(obj3.getClass().getName()));
                AnomalyReporter.reportAnomaly(UNEXPECTED_RESULT_TYPE_UUID, "Unexpected result type for EVENT_OPEN_LOGICAL_CHANNEL_DONE : ".concat(asyncResult.result.getClass().getName()));
            }
            if (shouldRetry(asyncResult, this.mRetryCount)) {
                log("should retry");
                this.mRetryCount++;
                removeCallbacks(this.mRetryRunnable);
                postDelayed(this.mRetryRunnable, 5000L);
                return;
            }
            if (this.mAIDInUse == 0) {
                this.mRules = "";
                openChannel(1);
            }
            if (this.mAIDInUse == 1) {
                if (this.mCheckedRules) {
                    updateState(1, "Success!");
                    return;
                }
                log("No ARA, try ARF next.");
                Throwable th = asyncResult.exception;
                if ((th instanceof CommandException) && ((CommandException) th).getCommandError() != CommandException.Error.NO_SUCH_ELEMENT) {
                    updateStatusMessage("No ARA due to " + ((CommandException) asyncResult.exception).getCommandError());
                }
                this.mUiccPkcs15 = new UiccPkcs15(this.mUiccProfile, obtainMessage(4));
                return;
            }
            return;
        }
        if (i != 2) {
            if (i == 3) {
                log("EVENT_CLOSE_LOGICAL_CHANNEL_DONE");
                if (this.mAIDInUse == 0) {
                    this.mRules = "";
                    openChannel(1);
                    return;
                }
                return;
            }
            if (i == 4) {
                log("EVENT_PKCS15_READ_DONE");
                UiccPkcs15 uiccPkcs15 = this.mUiccPkcs15;
                if (uiccPkcs15 == null || uiccPkcs15.getRules() == null) {
                    updateState(2, "No ARA or ARF.");
                    return;
                }
                Iterator<String> it = this.mUiccPkcs15.getRules().iterator();
                while (it.hasNext()) {
                    this.mAccessRules.add(new UiccAccessRule(IccUtils.hexStringToBytes(it.next()), "", 0L));
                }
                updateState(1, "Success!");
                return;
            }
            Rlog.e("UiccCarrierPrivilegeRules", "Unknown event " + message.what);
            return;
        }
        log("EVENT_TRANSMIT_LOGICAL_CHANNEL_DONE");
        AsyncResult asyncResult2 = (AsyncResult) message.obj;
        if (asyncResult2.exception == null && (obj2 = asyncResult2.result) != null) {
            IccIoResult iccIoResult = (IccIoResult) obj2;
            if (iccIoResult.sw1 == 144 && iccIoResult.sw2 == 0 && (bArr = iccIoResult.payload) != null && bArr.length > 0) {
                try {
                    this.mRules += IccUtils.bytesToHexString(iccIoResult.payload).toUpperCase(Locale.US);
                    if (isDataComplete()) {
                        this.mAccessRules.addAll(parseRules(this.mRules));
                        if (this.mAIDInUse == 0) {
                            this.mCheckedRules = true;
                        } else {
                            updateState(1, "Success!");
                        }
                    } else {
                        UiccProfile uiccProfile = this.mUiccProfile;
                        int i3 = this.mChannelId;
                        uiccProfile.iccTransmitApduLogicalChannel(i3, 128, NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA, 255, 96, 0, "", false, obtainMessage(2, i3, this.mAIDInUse));
                        return;
                    }
                } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                    if (this.mAIDInUse == 1) {
                        updateState(2, "Error parsing rules: " + e);
                    }
                }
            } else if (this.mAIDInUse == 1) {
                updateState(2, "Invalid response: payload=" + IccUtils.bytesToHexString(iccIoResult.payload) + " sw1=" + iccIoResult.sw1 + " sw2=" + iccIoResult.sw2);
            }
        } else {
            StringBuilder sb = new StringBuilder("Error reading value from SIM via ");
            sb.append(this.mAIDInUse == 0 ? "ARAD" : "ARAM");
            sb.append(" due to ");
            String string = sb.toString();
            Throwable th2 = asyncResult2.exception;
            if (th2 instanceof CommandException) {
                str = string + "error code : " + ((CommandException) th2).getCommandError();
            } else {
                str = string + "unknown exception : " + asyncResult2.exception.getMessage();
            }
            if (this.mAIDInUse == 0) {
                updateStatusMessage(str);
            } else {
                updateState(2, str);
            }
        }
        this.mUiccProfile.iccCloseLogicalChannel(this.mChannelId, false, obtainMessage(3, 0, this.mAIDInUse));
        this.mChannelId = -1;
    }

    private boolean isDataComplete() {
        log("isDataComplete mRules:" + this.mRules);
        if (this.mRules.startsWith("FF40")) {
            TLV tlv = new TLV("FF40");
            String length = tlv.parseLength(this.mRules);
            log("isDataComplete lengthBytes: " + length);
            if (this.mRules.length() == 4 + length.length() + tlv.length.intValue()) {
                log("isDataComplete yes");
                return true;
            }
            log("isDataComplete no");
            return false;
        }
        Phone$$ExternalSyntheticBUOutline0.m("Tags don't match.");
        return false;
    }

    private static List parseRules(String str) {
        log("Got rules: " + str);
        TLV tlv = new TLV("FF40");
        tlv.parse(str, true);
        String str2 = tlv.value;
        ArrayList arrayList = new ArrayList();
        while (!str2.isEmpty()) {
            TLV tlv2 = new TLV("E2");
            str2 = tlv2.parse(str2, false);
            UiccAccessRule refArdo = parseRefArdo(tlv2.value);
            if (refArdo != null) {
                arrayList.add(refArdo);
            } else {
                Rlog.e("UiccCarrierPrivilegeRules", "Skip unrecognized rule." + tlv2.value);
            }
        }
        return arrayList;
    }

    private static UiccAccessRule parseRefArdo(String str) {
        String str2;
        String str3;
        String str4;
        log("Got rule: " + str);
        String str5 = null;
        String str6 = null;
        while (!str.isEmpty()) {
            if (str.startsWith("E1")) {
                TLV tlv = new TLV("E1");
                str = tlv.parse(str, false);
                TLV tlv2 = new TLV("C1");
                if (tlv.value.startsWith("4F")) {
                    TLV tlv3 = new TLV("4F");
                    String str7 = tlv3.parse(tlv.value, false);
                    if (!tlv3.lengthBytes.equals("06") || !tlv3.value.equals("FFFFFFFFFFFF") || str7.isEmpty() || !str7.startsWith("C1")) {
                        return null;
                    }
                    str2 = tlv2.parse(str7, false);
                    str3 = tlv2.value;
                } else {
                    if (!tlv.value.startsWith("C1")) {
                        return null;
                    }
                    str2 = tlv2.parse(tlv.value, false);
                    str3 = tlv2.value;
                }
                if (str2.isEmpty()) {
                    str4 = null;
                } else {
                    if (!str2.startsWith("CA")) {
                        return null;
                    }
                    TLV tlv4 = new TLV("CA");
                    tlv4.parse(str2, true);
                    str4 = new String(IccUtils.hexStringToBytes(tlv4.value));
                }
                String str8 = str3;
                str6 = str4;
                str5 = str8;
            } else if (str.startsWith("E3")) {
                TLV tlv5 = new TLV("E3");
                str = tlv5.parse(str, false);
                String str9 = tlv5.value;
                while (!str9.isEmpty() && !str9.startsWith("DB")) {
                    str9 = new TLV(str9.substring(0, 2)).parse(str9, false);
                }
                if (str9.isEmpty()) {
                    return null;
                }
                new TLV("DB").parse(str9, true);
            } else {
                Phone$$ExternalSyntheticBUOutline1.m("Invalid Rule type");
                return null;
            }
        }
        return new UiccAccessRule(IccUtils.hexStringToBytes(str5), str6, 0L);
    }

    protected void updateState(int i, String str) {
        this.mState.set(i);
        Message message = this.mLoadedCallback;
        if (message != null) {
            message.sendToTarget();
        }
        updateStatusMessage(str);
    }

    private void updateStatusMessage(String str) {
        this.mStatusMessage.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("UiccCarrierPrivilegeRules:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mState=" + getStateString(this.mState.get()));
        androidUtilIndentingPrintWriter.println("mStatusMessage=");
        this.mStatusMessage.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        if (this.mAccessRules != null) {
            androidUtilIndentingPrintWriter.println("mAccessRules: ");
            androidUtilIndentingPrintWriter.increaseIndent();
            Iterator it = this.mAccessRules.iterator();
            while (it.hasNext()) {
                androidUtilIndentingPrintWriter.println("  rule='" + ((UiccAccessRule) it.next()) + "'");
            }
            androidUtilIndentingPrintWriter.decreaseIndent();
        } else {
            androidUtilIndentingPrintWriter.println(" mAccessRules: null");
        }
        if (this.mUiccPkcs15 != null) {
            androidUtilIndentingPrintWriter.println(" mUiccPkcs15: " + this.mUiccPkcs15);
            this.mUiccPkcs15.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        } else {
            androidUtilIndentingPrintWriter.println(" mUiccPkcs15: null");
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.flush();
    }

    private String getStateString(int i) {
        if (i == 0) {
            return "STATE_LOADING";
        }
        if (i == 1) {
            return "STATE_LOADED";
        }
        if (i == 2) {
            return "STATE_ERROR";
        }
        return SatelliteConstants.DEFAULT_PLMN;
    }
}
