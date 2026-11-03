package com.android.internal.telephony.data;

import android.content.Context;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyMultiInternetStub {
    private static volatile ITelephonyMultiInternet sInstance;

    public static ITelephonyMultiInternet get() {
        if (sInstance == null) {
            synchronized (TelephonyMultiInternetStub.class) {
                try {
                    if (sInstance == null) {
                        sInstance = TelephonyCommonProxyFactory.getHyperInterface().makeTelephonyMultiInternetStub();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public interface ITelephonyMultiInternet {
        default void init(Context context) {
        }

        default boolean isDataEnabledForApn(int i, int i2) {
            return false;
        }

        default boolean isMultiInternetApnTypes(int[] iArr, int i) {
            return false;
        }

        default boolean isMultiInternetCapabilities(int[] iArr, int i) {
            return false;
        }

        default boolean isMultiInternetEnabled() {
            return false;
        }

        default void setPhoneNumber(String str, int i) {
        }

        default String getPhoneNumber(String str, int i) {
            return "";
        }
    }
}
