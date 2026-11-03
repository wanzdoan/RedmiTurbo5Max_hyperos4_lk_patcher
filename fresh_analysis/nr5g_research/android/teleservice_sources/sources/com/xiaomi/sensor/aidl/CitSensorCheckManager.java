package com.xiaomi.sensor.aidl;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import vendor.xiaomi.sensor.citsensorservice.ICitSensorService;

/* JADX INFO: loaded from: classes2.dex */
public class CitSensorCheckManager {
    private static volatile CitSensorCheckManager instance;
    private ICitSensorService iCitSensorService = null;

    private CitSensorCheckManager() {
    }

    public static CitSensorCheckManager getInstance() {
        if (instance == null) {
            synchronized (CitSensorCheckManager.class) {
                try {
                    if (instance == null) {
                        instance = new CitSensorCheckManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return instance;
    }

    public int setConfig(int i, float[] fArr) {
        try {
            if (initAndCheckSensorService()) {
                return this.iCitSensorService.setArrayConfig(i, fArr, fArr.length);
            }
            return -1;
        } catch (RemoteException e) {
            Log.e("CitSensorCheckManager", "setConfig exception: " + e.getMessage());
            return -1;
        }
    }

    private boolean initAndCheckSensorService() {
        if (this.iCitSensorService == null) {
            try {
                Class<?> cls = Class.forName("android.os.ServiceManager");
                Class[] clsArr = new Class[0];
                ICitSensorService iCitSensorServiceAsInterface = ICitSensorService.Stub.asInterface((IBinder) cls.getMethod("waitForDeclaredService", String.class).invoke(cls.getConstructor(null).newInstance(null), ICitSensorService.DESCRIPTOR + "/default"));
                this.iCitSensorService = iCitSensorServiceAsInterface;
                return iCitSensorServiceAsInterface != null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
