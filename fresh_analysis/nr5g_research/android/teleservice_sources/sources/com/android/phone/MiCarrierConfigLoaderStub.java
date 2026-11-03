package com.android.phone;

import android.content.Context;
import android.os.Message;
import android.os.PersistableBundle;
import com.xiaomi.services.telephony.TelephonyFactory;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiCarrierConfigLoaderStub {
    private static volatile IMiCarrierConfigLoader sInstance;

    public interface IMiCarrierConfigLoader {
        default String getFilenameForConfig(String str, String str2, String str3, int i, String str4) {
            return str4;
        }

        default boolean handleMessage(Message message, int i) {
            return false;
        }

        default void init(CarrierConfigLoader carrierConfigLoader, Context context, PersistableBundle[] persistableBundleArr, Object... objArr) {
        }

        default boolean isLoadedError(int i) {
            return false;
        }

        default void resetCarrierConfig() {
        }

        default void updateConfigForPhoneId(int i, String str) {
        }
    }

    public static IMiCarrierConfigLoader get() {
        if (sInstance == null) {
            synchronized (MiCarrierConfigLoaderStub.class) {
                try {
                    if (sInstance == null) {
                        sInstance = TelephonyFactory.getHyperInterface().makeCcLoaderProxy();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }
}
