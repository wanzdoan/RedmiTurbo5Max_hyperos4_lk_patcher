package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.xiaomi.onetrack.util.r;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class l {
    public final LinkedBlockingQueue a = new LinkedBlockingQueue(1);
    ServiceConnection b = new ServiceConnection() { // from class: com.xiaomi.onetrack.util.oaid.helpers.SamsungDeviceIDHelper$1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.a.a.offer(iBinder, 1L, TimeUnit.SECONDS);
            } catch (Exception e) {
                r.a("SamsungDeviceIDHelper", e.getMessage());
            }
        }
    };

    /* JADX WARN: Type inference failed for: r3v3, types: [com.xiaomi.onetrack.util.oaid.a.g$a] */
    public String a(Context context) {
        ServiceConnection serviceConnection;
        Intent intent = new Intent();
        intent.setClassName("com.samsung.android.deviceidservice", "com.samsung.android.deviceidservice.DeviceIdService");
        String strA = "";
        if (context.bindService(intent, this.b, 1)) {
            try {
                try {
                    try {
                        final IBinder iBinder = (IBinder) this.a.poll(1L, TimeUnit.SECONDS);
                        if (iBinder != null) {
                            strA = new IInterface(iBinder) { // from class: com.xiaomi.onetrack.util.oaid.a.g$a
                                private IBinder a;

                                {
                                    this.a = iBinder;
                                }

                                @Override // android.os.IInterface
                                public IBinder asBinder() {
                                    return this.a;
                                }

                                public String a() {
                                    String string;
                                    Parcel parcelObtain = Parcel.obtain();
                                    Parcel parcelObtain2 = Parcel.obtain();
                                    try {
                                        parcelObtain.writeInterfaceToken("com.samsung.android.deviceidservice.IDeviceIdService");
                                        this.a.transact(1, parcelObtain, parcelObtain2, 0);
                                        parcelObtain2.readException();
                                        string = parcelObtain2.readString();
                                    } catch (Throwable th) {
                                        parcelObtain2.recycle();
                                        parcelObtain.recycle();
                                        th.printStackTrace();
                                        string = null;
                                    }
                                    parcelObtain2.recycle();
                                    parcelObtain.recycle();
                                    return string;
                                }
                            }.a();
                            serviceConnection = this.b;
                        } else {
                            try {
                                context.unbindService(this.b);
                                return "";
                            } catch (Exception e) {
                                r.a("SamsungDeviceIDHelper", e.getMessage());
                                return "";
                            }
                        }
                    } catch (Exception e2) {
                        r.a("SamsungDeviceIDHelper", e2.getMessage());
                        serviceConnection = this.b;
                    }
                    context.unbindService(serviceConnection);
                } catch (Exception e3) {
                    r.a("SamsungDeviceIDHelper", e3.getMessage());
                }
            } catch (Throwable th) {
                try {
                    context.unbindService(this.b);
                } catch (Exception e4) {
                    r.a("SamsungDeviceIDHelper", e4.getMessage());
                }
                throw th;
            }
        }
        return strA;
    }
}
