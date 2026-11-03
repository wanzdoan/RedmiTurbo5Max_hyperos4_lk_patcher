package com.android.phone.vvm;

import android.content.Context;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public abstract class VvmDumpHandler {
    public static void dump(Context context, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        TelephonyManager telephonyManagerFrom = TelephonyManager.from(context);
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("******* OmtpVvm *******");
        indentingPrintWriter.println("======= Configs =======");
        indentingPrintWriter.increaseIndent();
        try {
            Iterator<PhoneAccountHandle> it = ((TelecomManager) context.getSystemService(TelecomManager.class)).getCallCapablePhoneAccounts().iterator();
            while (it.hasNext()) {
                int subId = PhoneAccountHandleConverter.toSubId(it.next());
                indentingPrintWriter.println("VisualVoicemailPackageName:" + telephonyManagerFrom.createForSubscriptionId(subId).getVisualVoicemailPackageName());
                indentingPrintWriter.println("VisualVoicemailSmsFilterSettings(" + subId + "):" + telephonyManagerFrom.getActiveVisualVoicemailSmsFilterSettings(subId));
            }
        } catch (SecurityException e) {
            indentingPrintWriter.println("Could not get vvm config " + e);
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("======== Logs =========");
        VvmLog.dump(fileDescriptor, indentingPrintWriter, strArr);
    }
}
