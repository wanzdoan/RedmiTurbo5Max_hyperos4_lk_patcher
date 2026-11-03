package com.android.internal.telephony.data;

import android.content.Context;
import android.os.Message;
import com.android.internal.telephony.Phone;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPhoneSwitcherStub {
    private static volatile IMiuiPhoneSwitcher sInstance;

    public interface IMiuiPhoneSwitcher {
        default void dispose() {
        }

        default boolean getTempDataSwitching() {
            return false;
        }

        default void handleMessage(Message message) {
        }

        default void init(Context context, Object... objArr) {
        }

        default boolean isPhoneInVoiceCall(Phone phone, boolean z) {
            return z;
        }

        default void onDdsSwitchResponse() {
        }

        default void onNeedNetworkFor(TelephonyNetworkRequest telephonyNetworkRequest, Phone phone) {
        }

        default boolean onNetworkNeeded(TelephonyNetworkRequest telephonyNetworkRequest) {
            return false;
        }

        default boolean updatePreferredDataPhoneId() {
            return false;
        }
    }

    public static IMiuiPhoneSwitcher get() {
        if (sInstance == null) {
            synchronized (MiuiPhoneSwitcherStub.class) {
                try {
                    if (sInstance == null) {
                        sInstance = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiPhoneSwitcherStub();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }
}
