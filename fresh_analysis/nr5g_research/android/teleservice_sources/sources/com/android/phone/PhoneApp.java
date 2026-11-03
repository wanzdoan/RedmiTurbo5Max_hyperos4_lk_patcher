package com.android.phone;

import android.app.ActivityThread;
import android.os.UserHandle;
import android.util.Log;
import com.android.services.telephony.MiTelephonyServiceStub;
import com.android.services.telephony.TelecomAccountRegistry;
import com.xiaomi.services.telephony.VendorTelephonyFactory;
import miuix.app.Application;
import miuix.autodensity.IDensity;

/* JADX INFO: loaded from: classes.dex */
public class PhoneApp extends Application implements IDensity {
    PhoneGlobals mPhoneGlobals;

    @Override // miuix.autodensity.IDensity
    public boolean shouldAdaptAutoDensity() {
        return true;
    }

    @Override // android.app.Application
    public void onCreate() {
        Log.i("MiuiX.PhoneApp", "onCreate...");
        if ("com.android.phone:remote".equals(ActivityThread.currentProcessName())) {
            Log.i("MiuiX.PhoneApp", "Skip PhoneApp initialization in remote process");
            return;
        }
        if (UserHandle.myUserId() == 0) {
            VendorTelephonyFactory.get().makeTelephonyGlobals().init(this, new Object[0]);
            PhoneGlobals phoneGlobalsMakePhoneGlobals = VendorTelephonyFactory.get().makePhoneGlobals(this);
            this.mPhoneGlobals = phoneGlobalsMakePhoneGlobals;
            phoneGlobalsMakePhoneGlobals.onCreate();
            MiPhoneAppStub.get().init(this, this.mPhoneGlobals, new Object[0]);
            MiTelephonyServiceStub.get().init(this);
            TelecomAccountRegistry telecomAccountRegistry = TelecomAccountRegistry.getInstance(this);
            if (telecomAccountRegistry != null) {
                telecomAccountRegistry.setupOnBoot();
            }
        }
    }
}
