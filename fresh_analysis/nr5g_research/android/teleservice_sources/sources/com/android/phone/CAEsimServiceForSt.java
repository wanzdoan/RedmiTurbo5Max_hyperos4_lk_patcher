package com.android.phone;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import vendor.xiaomi.hardware.esim.IEsimService;

/* JADX INFO: loaded from: classes.dex */
public class CAEsimServiceForSt {
    private static IEsimService mEsimService;
    private static volatile CAEsimServiceForSt mInstance;

    private CAEsimServiceForSt() {
        try {
            IBinder service = ServiceManager.getService("vendor.xiaomi.hardware.esim.IEsimService/default");
            if (service == null) {
                Log.e("CAEsimServiceForSt", "Failed to get CAEsimServiceForSt service binder.");
            } else {
                mEsimService = IEsimService.Stub.asInterface(service);
                Log.i("CAEsimServiceForSt", "CAEsimServiceForSt service initialized successfully.");
            }
        } catch (Exception e) {
            Log.e("CAEsimServiceForSt", "Failed to get CAEsimServiceForSt service", e);
        }
    }

    public static CAEsimServiceForSt getInstance() {
        if (mInstance == null) {
            synchronized (CAEsimServiceForSt.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new CAEsimServiceForSt();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    public byte[] esimBindStepOne(byte[] bArr) {
        Log.i("CAEsimServiceForSt", "esimBindStepOne called with client");
        try {
            return mEsimService.esim_bind_step_1(bArr);
        } catch (RemoteException e) {
            Log.e("CAEsimServiceForSt", "RemoteException in esimBindStepOne", e);
            e.printStackTrace();
            return null;
        }
    }

    public int esimBindStepTwo(byte[] bArr) {
        Log.i("CAEsimServiceForSt", "esimBindStepTwo called with client");
        try {
            return mEsimService.esim_bind_step_2(bArr);
        } catch (RemoteException e) {
            Log.e("CAEsimServiceForSt", "RemoteException in esimBindStepTwo", e);
            e.printStackTrace();
            return -1;
        }
    }

    public byte[] esimPairStepOne(byte[] bArr) {
        Log.i("CAEsimServiceForSt", "esimPairStepOne called with client");
        try {
            return mEsimService.esim_pair_step_1(bArr);
        } catch (RemoteException e) {
            Log.e("CAEsimServiceForSt", "RemoteException in esimPairStepOne", e);
            e.printStackTrace();
            return null;
        }
    }

    public int esimPairStepTwo(byte[] bArr) {
        Log.i("CAEsimServiceForSt", "esimPairStepTwo called with client");
        try {
            return mEsimService.esim_pair_step_2(bArr);
        } catch (RemoteException e) {
            Log.e("CAEsimServiceForSt", "RemoteException in esimPairStepTwo", e);
            e.printStackTrace();
            return -1;
        }
    }

    public byte[] esimReadEid() {
        Log.i("CAEsimServiceForSt", "esimReadEid called with client");
        try {
            return mEsimService.esim_read_eid();
        } catch (RemoteException e) {
            Log.e("CAEsimServiceForSt", "RemoteException in esimReadEid", e);
            e.printStackTrace();
            return null;
        }
    }

    public boolean esimGetBindStatus() {
        Log.i("CAEsimServiceForSt", "esimGetBindStatus called with client");
        try {
            return mEsimService.esim_get_bind_status();
        } catch (RemoteException e) {
            Log.e("CAEsimServiceForSt", "RemoteException in esimGetBindStatus", e);
            e.printStackTrace();
            return false;
        }
    }
}
