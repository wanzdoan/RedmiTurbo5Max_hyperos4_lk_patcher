package com.xiaomi.internal.telephony;

import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.MiuiCallControllerImpl;
import com.android.internal.telephony.MiuiCallControllerStub;
import com.android.internal.telephony.MiuiDefaultPhoneNotifierImpl;
import com.android.internal.telephony.MiuiDefaultPhoneNotifierStub;
import com.android.internal.telephony.MiuiNetworkControllerImpl;
import com.android.internal.telephony.MiuiNetworkControllerStub;
import com.android.internal.telephony.MiuiPhoneImpl;
import com.android.internal.telephony.MiuiPhoneStub;
import com.android.internal.telephony.MiuiRadioControllerImpl;
import com.android.internal.telephony.MiuiRadioControllerStub;
import com.android.internal.telephony.MiuiSmsControllerImpl;
import com.android.internal.telephony.MiuiSmsControllerStub;
import com.android.internal.telephony.MiuiSubscriptionControllerImpl;
import com.android.internal.telephony.MiuiSubscriptionControllerStub;
import com.android.internal.telephony.TelephonyCommonUtilsImpl;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.internal.telephony.data.MiuiDataControllerStub;
import com.android.internal.telephony.data.MiuiDataServiceManagerStub;
import com.android.internal.telephony.data.MiuiPhoneSwitcherImpl;
import com.android.internal.telephony.data.MiuiPhoneSwitcherStub;
import com.android.internal.telephony.imsphone.MiuiImsControllerImpl;
import com.android.internal.telephony.imsphone.MiuiImsControllerStub;
import com.android.internal.telephony.ntndata.MiuiDataServiceManagerImpl;
import com.android.internal.telephony.uicc.MiuiUiccControllerImpl;
import com.android.internal.telephony.uicc.MiuiUiccControllerStub;

/* JADX INFO: loaded from: classes.dex */
public class HyperTelephonyCommonProxyFactory implements TelephonyCommonProxyFactory.IHyperInterface {
    private static final String LOG_TAG = "HyperTelephonyCommonProxyFactory";
    private static boolean sIsAutoDevice = SystemProperties.get("ro.build.characteristics").contains("auto");

    public HyperTelephonyCommonProxyFactory() {
        logd("HyperTelephonyCommonProxyFactory instance created");
    }

    public TelephonyCommonUtilsStub.ITelephonyCommonUtils makeTelephonyCommonUtilsStub() {
        logd("make TelephonyCommonUtilsImpl");
        return new TelephonyCommonUtilsImpl();
    }

    public MiuiSubscriptionControllerStub.IMiuiSubscriptionController makeMiuiSubscriptionControllerStub() {
        logd("make MiuiSubscriptionControllerImpl");
        return new MiuiSubscriptionControllerImpl();
    }

    public MiuiSmsControllerStub.IMiuiSmsController makeMiuiSmsControllerStub() {
        logd("make MiuiSmsControllerImpl");
        return new MiuiSmsControllerImpl();
    }

    public MiuiRadioControllerStub.IMiuiRadioController makeMiuiRadioControllerStub() {
        logd("make MiuiRadioControllerImpl");
        return new MiuiRadioControllerImpl();
    }

    public MiuiPhoneStub.IMiuiPhone makeMiuiPhoneStub() {
        logd("make MiuiPhoneImpl");
        return new MiuiPhoneImpl();
    }

    public MiuiNetworkControllerStub.IMiuiNetworkController makeMiuiNetworkControllerStub() {
        logd("make MiuiNetworkControllerImpl");
        return new MiuiNetworkControllerImpl();
    }

    public MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier makeMiuiDefaultPhoneNotifierStub() {
        logd("make MiuiDefaultPhoneNotifierImpl");
        return new MiuiDefaultPhoneNotifierImpl();
    }

    public MiuiCallControllerStub.IMiuiCallController makeMiuiCallControllerStub() {
        logd("make MiuiCallControllerImpl");
        return new MiuiCallControllerImpl();
    }

    public MiuiUiccControllerStub.IMiuiUiccController makeMiuiUiccControllerStub() {
        logd("make MiuiUiccControllerImpl");
        return new MiuiUiccControllerImpl();
    }

    public MiuiImsControllerStub.IMiuiImsController makeMiuiImsControllerStub() {
        logd("make MiuiImsControllerImpl");
        return new MiuiImsControllerImpl();
    }

    public MiuiPhoneSwitcherStub.IMiuiPhoneSwitcher makeMiuiPhoneSwitcherStub() {
        logd("make MiuiPhoneSwitcherImpl");
        return new MiuiPhoneSwitcherImpl();
    }

    public MiuiDataControllerStub.IMiuiDataController makeMiuiDataControllerStub() {
        logd("make MiuiDataControllerImpl");
        return new MiuiDataControllerImpl();
    }

    public MiuiDataServiceManagerStub.IMiuiDataServiceManager makeMiuiDataServiceManagerStub() {
        logd("make MiuiDataServiceManagerImpl");
        return new MiuiDataServiceManagerImpl();
    }

    private static void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }
}
