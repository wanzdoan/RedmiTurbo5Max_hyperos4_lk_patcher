package com.android.phone;

import android.net.Uri;
import android.os.Bundle;
import com.android.internal.telephony.Phone;
import com.android.phone.module.ISettingsModule;
import com.android.phone.settings.AutoIpSetting;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class SettingsDelegateImpl implements ISettingsModule {
    private static volatile SettingsDelegateImpl sInstance;

    @Override // com.android.phone.module.ISettingsModule
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public static SettingsDelegateImpl init() {
        if (sInstance == null) {
            synchronized (SettingsDelegateImpl.class) {
                try {
                    if (sInstance == null) {
                        SettingsDelegateImpl settingsDelegateImpl = new SettingsDelegateImpl();
                        ModuleDelegate.register(ISettingsModule.class, settingsDelegateImpl);
                        sInstance = settingsDelegateImpl;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private SettingsDelegateImpl() {
    }

    @Override // com.android.phone.module.ISettingsModule
    public Uri handleIpCall(Uri uri, Phone phone, String str, Bundle bundle) {
        return AutoIpSetting.handleIpCall(uri, phone, str, bundle);
    }
}
