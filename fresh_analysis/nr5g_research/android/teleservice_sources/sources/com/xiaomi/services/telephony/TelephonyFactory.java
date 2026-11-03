package com.xiaomi.services.telephony;

import android.telephony.TelephonyBaseUtilsStub;
import android.util.Log;
import com.android.phone.MiCarrierConfigLoaderStub;
import com.android.phone.MiPhoneAppStub;
import com.android.services.telephony.MiTelephonyServiceStub;
import java.lang.reflect.Constructor;

/* JADX INFO: loaded from: classes2.dex */
public abstract class TelephonyFactory {
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
            Constructor declaredConstructor = HyperTelephonyFactory.class.getDeclaredConstructor(null);
            declaredConstructor.setAccessible(true);
            sHyperInstance = (IHyperInterface) declaredConstructor.newInstance(null);
        } catch (Exception e) {
            Log.e("TelephonyFactory", "failed to load hyperos factory, " + e.getMessage());
            sHyperInstance = new IHyperInterface() { // from class: com.xiaomi.services.telephony.TelephonyFactory.1
            };
        }
        return sHyperInstance;
    }

    private static IHyperInterface injectAutoFactory() {
        try {
            Class[] clsArr = new Class[0];
            Constructor<?> declaredConstructor = Class.forName("com.xiaomi.services.telephony.AutoTelephonyFactory").getDeclaredConstructor(null);
            declaredConstructor.setAccessible(true);
            sHyperInstance = (IHyperInterface) declaredConstructor.newInstance(null);
        } catch (Exception e) {
            Log.e("TelephonyFactory", "failed to load auto factory, " + e.getMessage());
            sHyperInstance = new IHyperInterface() { // from class: com.xiaomi.services.telephony.TelephonyFactory.2
            };
        }
        return sHyperInstance;
    }

    public interface IHyperInterface {
        default MiTelephonyServiceStub.IMiTelephonyService makeTelephonyServiceProxy() {
            return new MiTelephonyServiceStub.IMiTelephonyService(this) { // from class: com.xiaomi.services.telephony.TelephonyFactory.IHyperInterface.1
            };
        }

        default MiCarrierConfigLoaderStub.IMiCarrierConfigLoader makeCcLoaderProxy() {
            return new MiCarrierConfigLoaderStub.IMiCarrierConfigLoader(this) { // from class: com.xiaomi.services.telephony.TelephonyFactory.IHyperInterface.2
            };
        }

        default MiPhoneAppStub.IMiPhoneApp makePhoneAppProxy() {
            return new MiPhoneAppStub.IMiPhoneApp(this) { // from class: com.xiaomi.services.telephony.TelephonyFactory.IHyperInterface.3
            };
        }
    }
}
