package com.android.phone;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.telephony.CellIdentity;
import android.telephony.MiStubParam;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.phone.settings.VoicemailSettingsActivity;
import com.xiaomi.services.telephony.TelephonyFactory;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiPhoneAppStub {
    private static volatile IMiPhoneApp sInstance;

    public interface IMiPhoneApp {
        default void cleanUpAllowedNetworkTypes(Phone phone, int i) {
        }

        default boolean createUssdDialog(PhoneGlobals phoneGlobals, Context context, CharSequence charSequence, Phone phone, int i, MiStubParam miStubParam) {
            return false;
        }

        default void dismissUssdDialog() {
        }

        default boolean displayMMIComplete(Phone phone, Context context, MmiCode mmiCode, Message message, AlertDialog alertDialog, MiStubParam miStubParam) {
            return false;
        }

        default boolean displayMMIInitiate(Phone phone, Context context, MmiCode mmiCode, Message message, Dialog dialog, MiStubParam miStubParam) {
            return false;
        }

        default void dump(Context context, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        }

        default void dumpDebugService(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        }

        default boolean enforceCallPermission() {
            return false;
        }

        default boolean factoryResetByFiveGCapable(Phone phone) {
            return false;
        }

        default List getBlurCellInfo(Context context, AppOpsManager appOpsManager, List list, String str, int i) {
            return list;
        }

        default CellIdentity getBlurCellLocation(Context context, AppOpsManager appOpsManager, CellIdentity cellIdentity, String str, int i) {
            return cellIdentity;
        }

        default String getMmsUserAgent(Context context, int i, String str) {
            return str;
        }

        default int getPreferredVoiceSubscription() {
            return -1;
        }

        default void init(PhoneApp phoneApp, PhoneGlobals phoneGlobals, Object... objArr) {
        }

        default void initCellInfoLogger() {
        }

        default boolean isActivityInfoOptimizeSupported() {
            return false;
        }

        default boolean isDataEnabled(Context context, int i) {
            return false;
        }

        default boolean isDataOnlySlot(Context context, Phone phone) {
            return false;
        }

        default boolean isIgnoreEmergencyNumber(Phone phone, String str) {
            return false;
        }

        default boolean isLocalEmergencyNumber(Context context, String str, boolean z) {
            return z;
        }

        default boolean isNotReadyForApplication(Context context) {
            return false;
        }

        default boolean isUserDataEnabled(Context context, int i) {
            return false;
        }

        default void recordCallerInfo(int i, String str, String str2) {
        }

        default boolean setAllPhonesDataEnabled(PhoneGlobals phoneGlobals, boolean z, String str) {
            return false;
        }

        default void setAllowedNetworkTypesForReason(int i, int i2, long j) {
        }

        default boolean setDataEnabledForReason(Context context, int i, boolean z, String str) {
            return false;
        }

        default boolean startVoicemailSettingsActivity(VoicemailSettingsActivity voicemailSettingsActivity) {
            return false;
        }

        default void syncPhoneAccounts() {
        }

        default void updateDataRoamingStatus(Phone phone, String str, int i) {
        }
    }

    public static IMiPhoneApp get() {
        if (sInstance == null) {
            synchronized (MiPhoneAppStub.class) {
                try {
                    if (sInstance == null) {
                        sInstance = TelephonyFactory.getHyperInterface().makePhoneAppProxy();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }
}
