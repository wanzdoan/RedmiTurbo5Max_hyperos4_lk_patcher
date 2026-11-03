package com.android.internal.telephony;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.emergency.EmergencyNumber;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.miui.misight.MiEventStub;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.io.BufferedInputStream;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCommonUtilsStub {
    public static final long DEFAULT_TIMEOUT_MILLIS = 1000;
    public static final int SMS_EXCEPTION_EVENT_CODE = 915700001;
    private static volatile ITelephonyCommonUtils sInstance;
    private static boolean sIsAutoDevice = SystemProperties.get("ro.build.characteristics").contains("auto");

    public static ITelephonyCommonUtils get() {
        if (sInstance == null) {
            synchronized (TelephonyCommonUtilsStub.class) {
                try {
                    if (sInstance == null) {
                        sInstance = TelephonyCommonProxyFactory.getHyperInterface().makeTelephonyCommonUtilsStub();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public interface ITelephonyCommonUtils {
        default void checkCallingOrSelfPermission(Context context, int i, String str, String str2) {
        }

        default boolean checkCallingOrSelfPermissionGranted(int i) {
            return true;
        }

        default void checkCallingPermission(Context context, int i, String str, String str2) {
        }

        default int disconnectCauseFromCode(int i) {
            return -1;
        }

        default void filterRadioCapabilities(Set<String> set) {
        }

        default String getCallingPackage() {
            return null;
        }

        default BufferedInputStream getEccdataInputStream(BufferedInputStream bufferedInputStream) {
            return bufferedInputStream;
        }

        default String[] handlePii(int i, String[] strArr) {
            return strArr;
        }

        default void init(Context context) {
        }

        default void initMiuiTelephony(Context context, FeatureFlags featureFlags, Object... objArr) {
        }

        default boolean isChinaTestBuild() {
            return false;
        }

        default boolean isCustForJpKd() {
            return false;
        }

        default boolean isCustSingleSimDevice() {
            return false;
        }

        default boolean isDataRatChanged(int i, int i2) {
            return false;
        }

        default boolean isDemoBuild() {
            return false;
        }

        default boolean isInCnRegion() {
            return false;
        }

        default boolean isMiuiRom() {
            return false;
        }

        default boolean isRemoveEccData() {
            return true;
        }

        default boolean isSatelliteEnable(Context context) {
            return false;
        }

        default boolean isSpecialBuild() {
            return false;
        }

        default boolean isSupportDomesticRoaming() {
            return false;
        }

        default void localLog(String str, String str2) {
        }

        default boolean notifySignalStrength(Phone phone) {
            return true;
        }

        default String pii(String str) {
            return str;
        }

        default String piiIP(String str) {
            return str;
        }

        default void processForCustSingleSimDevice() {
        }

        default void processForCustSingleSimDevice(IccCardStatus iccCardStatus, Integer num) {
        }

        default boolean reportDataToCdc(int i, Bundle bundle) {
            return false;
        }

        default void reportMisightEvent(int i, int i2, Bundle bundle) {
        }

        default void restoreSettings(Phone phone) {
        }

        default void sendEvent(MiEventStub miEventStub) {
        }

        default void setEuiccProvisioned(Context context, GetEuiccProfileInfoListResult getEuiccProfileInfoListResult) {
        }

        default boolean setMobileData(Context context, boolean z) {
            return true;
        }

        default void updateCustEmergencyNumber(List<EmergencyNumber> list, String str) {
        }

        default void updateEccdata(List<EmergencyNumber> list) {
        }

        default String handlePii(int i, Object obj) {
            return obj.toString();
        }

        default String getMiuiString(Context context, int i) {
            return context.getResources().getString(i);
        }

        default boolean setSatelliteEnable(Context context, int i, boolean z) {
            return MiuiTelephonyManagerStub.setSatelliteEnable(i, z);
        }

        default SMSDispatcher getNtnSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
            return MiuiSmsControllerStub.get(phone.getPhoneId()).getNtnSmsDispatcher(phone, smsDispatchersController);
        }

        default boolean ntnInboundSmsHandlerIsWaiting(Phone phone) {
            return MiuiSmsControllerStub.get(phone.getPhoneId()).ntnInboundSmsHandlerIsWaiting(phone);
        }
    }
}
