package com.xiaomi.services.telephony;

import android.content.Context;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import java.lang.reflect.Constructor;

/* JADX INFO: loaded from: classes2.dex */
public abstract class VendorTelephonyFactory {
    private static volatile IInterface sVendorInstance;

    public static IInterface get() {
        if (sVendorInstance == null) {
            synchronized (IInterface.class) {
                try {
                    if (sVendorInstance == null) {
                        sVendorInstance = injectVendorFactory();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sVendorInstance;
    }

    private static IInterface injectVendorFactory() {
        try {
            Class[] clsArr = new Class[0];
            Constructor<?> declaredConstructor = Class.forName("com.xiaomi.services.telephony.XmTelephonyFactory").getDeclaredConstructor(null);
            declaredConstructor.setAccessible(true);
            sVendorInstance = (IInterface) declaredConstructor.newInstance(null);
        } catch (Exception e) {
            Log.e("VendorTelephonyFactory", "failed to load vendor factory, " + e.getMessage());
            sVendorInstance = new IInterface() { // from class: com.xiaomi.services.telephony.VendorTelephonyFactory.1
            };
        }
        return sVendorInstance;
    }

    public interface IInterface {
        default ITelephonyGlobals makeTelephonyGlobals() {
            return new ITelephonyGlobals(this) { // from class: com.xiaomi.services.telephony.VendorTelephonyFactory.IInterface.1
            };
        }

        default PhoneGlobals makePhoneGlobals(Context context) {
            return new PhoneGlobals(context);
        }
    }
}
