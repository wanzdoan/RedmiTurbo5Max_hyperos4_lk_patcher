package com.xiaomi.internal.telephony;

import android.telephony.Rlog;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.MiuiCallControllerStub;
import com.android.internal.telephony.MiuiDefaultPhoneNotifierStub;
import com.android.internal.telephony.MiuiNetworkControllerStub;
import com.android.internal.telephony.MiuiPhoneStub;
import com.android.internal.telephony.MiuiRadioControllerStub;
import com.android.internal.telephony.MiuiSmsControllerStub;
import com.android.internal.telephony.MiuiSubscriptionControllerStub;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.data.MiuiDataControllerStub;
import com.android.internal.telephony.data.MiuiDataServiceManagerStub;
import com.android.internal.telephony.data.MiuiPhoneSwitcherStub;
import com.android.internal.telephony.data.TelephonyMultiInternetStub;
import com.android.internal.telephony.imsphone.MiuiImsControllerStub;
import com.android.internal.telephony.uicc.MiuiUiccControllerStub;
import java.lang.reflect.Constructor;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelephonyCommonProxyFactory {
    public static int PHONE_COUNT = TelephonyManager.getDefault().getActiveModemCount();
    private static volatile IHyperInterface sHyperInstance;

    public static IHyperInterface getHyperInterface() {
        if (sHyperInstance == null) {
            synchronized (IHyperInterface.class) {
                try {
                    if (sHyperInstance == null) {
                        sHyperInstance = TelephonyBaseUtilsStub.isAutoDevice() ? injectAutoFactory() : injectHyperFactory();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sHyperInstance;
    }

    private static IHyperInterface injectHyperFactory() {
        try {
            Class[] clsArr = new Class[0];
            Constructor<?> declaredConstructor = Class.forName("com.xiaomi.internal.telephony.HyperTelephonyCommonProxyFactory").getDeclaredConstructor(null);
            declaredConstructor.setAccessible(true);
            sHyperInstance = (IHyperInterface) declaredConstructor.newInstance(null);
        } catch (Exception e) {
            loge("failed to load hyperos factory, " + e.getMessage());
            sHyperInstance = new IHyperInterface() { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.1
            };
        }
        return sHyperInstance;
    }

    private static IHyperInterface injectAutoFactory() {
        try {
            Class[] clsArr = new Class[0];
            Constructor<?> declaredConstructor = Class.forName("com.xiaomi.internal.telephony.AutoTelephonyCommonProxyFactory").getDeclaredConstructor(null);
            declaredConstructor.setAccessible(true);
            sHyperInstance = (IHyperInterface) declaredConstructor.newInstance(null);
        } catch (Exception e) {
            loge("failed to load auto factory, " + e.getMessage());
            sHyperInstance = new IHyperInterface() { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.2
            };
        }
        return sHyperInstance;
    }

    public interface IHyperInterface {
        default TelephonyCommonUtilsStub.ITelephonyCommonUtils makeTelephonyCommonUtilsStub() {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make ITelephonyCommonUtils");
            return new TelephonyCommonUtilsStub.ITelephonyCommonUtils(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.1
            };
        }

        default MiuiSubscriptionControllerStub.IMiuiSubscriptionController makeMiuiSubscriptionControllerStub() {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiSubscriptionController");
            return new MiuiSubscriptionControllerStub.IMiuiSubscriptionController(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.2
            };
        }

        default MiuiSmsControllerStub.IMiuiSmsController makeMiuiSmsControllerStub() {
            return new MiuiSmsControllerStub.IMiuiSmsController(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.3
            };
        }

        default MiuiSmsControllerStub.IMiuiSmsController makeMiuiSmsControllerStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiSmsController");
            return makeMiuiSmsControllerStub();
        }

        default MiuiRadioControllerStub.IMiuiRadioController makeMiuiRadioControllerStub() {
            return new MiuiRadioControllerStub.IMiuiRadioController(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.4
            };
        }

        default MiuiRadioControllerStub.IMiuiRadioController makeMiuiRadioControllerStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiRadioController");
            return makeMiuiRadioControllerStub();
        }

        default MiuiPhoneStub.IMiuiPhone makeMiuiPhoneStub() {
            return new MiuiPhoneStub.IMiuiPhone(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.5
            };
        }

        default MiuiPhoneStub.IMiuiPhone makeMiuiPhoneStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiPhone");
            return makeMiuiPhoneStub();
        }

        default MiuiNetworkControllerStub.IMiuiNetworkController makeMiuiNetworkControllerStub() {
            return new MiuiNetworkControllerStub.IMiuiNetworkController(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.6
            };
        }

        default MiuiNetworkControllerStub.IMiuiNetworkController makeMiuiNetworkControllerStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiNetworkController");
            return makeMiuiNetworkControllerStub();
        }

        default MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier makeMiuiDefaultPhoneNotifierStub() {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiDefaultPhoneNotifier");
            return new MiuiDefaultPhoneNotifierStub.IMiuiDefaultPhoneNotifier(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.7
            };
        }

        default MiuiCallControllerStub.IMiuiCallController makeMiuiCallControllerStub() {
            return new MiuiCallControllerStub.IMiuiCallController(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.8
            };
        }

        default MiuiCallControllerStub.IMiuiCallController makeMiuiCallControllerStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiCallController");
            return makeMiuiCallControllerStub();
        }

        default MiuiUiccControllerStub.IMiuiUiccController makeMiuiUiccControllerStub() {
            return new MiuiUiccControllerStub.IMiuiUiccController(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.9
            };
        }

        default MiuiUiccControllerStub.IMiuiUiccController makeMiuiUiccControllerStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiUiccController");
            return makeMiuiUiccControllerStub();
        }

        default MiuiImsControllerStub.IMiuiImsController makeMiuiImsControllerStub() {
            return new MiuiImsControllerStub.IMiuiImsController(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.10
            };
        }

        default MiuiImsControllerStub.IMiuiImsController makeMiuiImsControllerStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiImsController");
            return makeMiuiImsControllerStub();
        }

        default TelephonyMultiInternetStub.ITelephonyMultiInternet makeTelephonyMultiInternetStub() {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make ITelephonyMultiInternet");
            return new TelephonyMultiInternetStub.ITelephonyMultiInternet(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.11
            };
        }

        default MiuiPhoneSwitcherStub.IMiuiPhoneSwitcher makeMiuiPhoneSwitcherStub() {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiPhoneSwitcher");
            return new MiuiPhoneSwitcherStub.IMiuiPhoneSwitcher(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.12
            };
        }

        default MiuiDataControllerStub.IMiuiDataController makeMiuiDataControllerStub() {
            return new MiuiDataControllerStub.IMiuiDataController(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.13
            };
        }

        default MiuiDataControllerStub.IMiuiDataController makeMiuiDataControllerStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiDataController");
            return makeMiuiDataControllerStub();
        }

        default MiuiDataServiceManagerStub.IMiuiDataServiceManager makeMiuiDataServiceManagerStub() {
            return new MiuiDataServiceManagerStub.IMiuiDataServiceManager(this) { // from class: com.xiaomi.internal.telephony.TelephonyCommonProxyFactory.IHyperInterface.14
            };
        }

        default MiuiDataServiceManagerStub.IMiuiDataServiceManager makeMiuiDataServiceManagerStub(int i) {
            TelephonyCommonProxyFactory.logd("IHyperInterface, make IMiuiDataServiceManager");
            return makeMiuiDataServiceManagerStub();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Rlog.d("TelephonyCommonProxyFactory", str);
    }

    private static void loge(String str) {
        Rlog.e("TelephonyCommonProxyFactory", str);
    }
}
