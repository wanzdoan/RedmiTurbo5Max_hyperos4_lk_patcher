package miuix.mimotion;

import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
public class MiMotionCloudConfig {
    private static MiMotionCloudConfig sInstance;
    Object mMimotionModeService;
    private IBinder mService = ServiceManager.getService("MimotionModeService");

    private MiMotionCloudConfig() {
        try {
            this.mMimotionModeService = Class.forName("com.xiaomi.mimotion.IMimotionModeService$Stub").getMethod("asInterface", IBinder.class).invoke(null, this.mService);
        } catch (Exception unused) {
            Log.e("MiMotionCloudConfig", "M2CloudConfig init failed... ");
        }
    }

    public static MiMotionCloudConfig getInstance() {
        if (sInstance == null) {
            sInstance = new MiMotionCloudConfig();
        }
        return sInstance;
    }

    public int[] getRefreshRateSpeedLimitsDp() {
        Object obj = this.mMimotionModeService;
        if (obj == null) {
            return null;
        }
        try {
            Class[] clsArr = new Class[0];
            return (int[]) obj.getClass().getMethod("getRefreshRateSpeedLimitsDp", null).invoke(this.mMimotionModeService, null);
        } catch (Exception e) {
            Log.e("MiMotionCloudConfig", "get refresh rate speed limits from cloud failed " + Log.getStackTraceString(e));
            return null;
        }
    }
}
