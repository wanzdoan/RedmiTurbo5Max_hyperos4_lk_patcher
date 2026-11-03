package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import com.xiaomi.onetrack.util.r;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class n {
    String a = "com.mdid.msa";
    public final LinkedBlockingQueue b = new LinkedBlockingQueue(1);
    ServiceConnection c = new ServiceConnection() { // from class: com.xiaomi.onetrack.util.oaid.helpers.ZTEDeviceIDHelper$1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.a.b.offer(iBinder, 1L, TimeUnit.SECONDS);
            } catch (Exception e) {
                r.a("ZTEDeviceIDHelper", e.getMessage());
            }
        }
    };

    private void a(String str, Context context) {
        Intent intent = new Intent();
        intent.setClassName(this.a, "com.mdid.msa.service.MsaKlService");
        intent.setAction("com.bun.msa.action.start.service");
        intent.putExtra("com.bun.msa.param.pkgname", str);
        try {
            intent.putExtra("com.bun.msa.param.runinset", true);
            context.startService(intent);
        } catch (Exception e) {
            r.a("ZTEDeviceIDHelper", e.getMessage());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v13 */
    /* JADX WARN: Type inference failed for: r6v14 */
    /* JADX WARN: Type inference failed for: r6v8, types: [android.content.ServiceConnection] */
    public String a(Context context) {
        try {
            context.getPackageManager().getPackageInfo(this.a, 0);
        } catch (Exception e) {
            r.a("ZTEDeviceIDHelper", e.getMessage());
        }
        String packageName = context.getPackageName();
        a(packageName, context);
        Intent intent = new Intent();
        intent.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaIdService");
        intent.setAction("com.bun.msa.action.bindto.service");
        intent.putExtra("com.bun.msa.param.pkgname", packageName);
        boolean zBindService = context.bindService(intent, this.c, 1);
        String strB = "";
        try {
            try {
                if (zBindService) {
                    try {
                        final IBinder iBinder = (IBinder) this.b.poll(1L, TimeUnit.SECONDS);
                        if (iBinder != null) {
                            strB = new com.xiaomi.onetrack.util.oaid.a.h(iBinder) { // from class: com.xiaomi.onetrack.util.oaid.a.h$a$a
                                private IBinder a;

                                {
                                    this.a = iBinder;
                                }

                                @Override // android.os.IInterface
                                public IBinder asBinder() {
                                    return this.a;
                                }

                                @Override // com.xiaomi.onetrack.util.oaid.a.h
                                public String b() {
                                    String string;
                                    Parcel parcelObtain = Parcel.obtain();
                                    Parcel parcelObtain2 = Parcel.obtain();
                                    try {
                                        parcelObtain.writeInterfaceToken("com.bun.lib.MsaIdInterface");
                                        this.a.transact(3, parcelObtain, parcelObtain2, 0);
                                        parcelObtain2.readException();
                                        string = parcelObtain2.readString();
                                    } catch (Throwable unused) {
                                        parcelObtain2.recycle();
                                        parcelObtain.recycle();
                                        string = null;
                                    }
                                    parcelObtain2.recycle();
                                    parcelObtain.recycle();
                                    return string;
                                }
                            }.b();
                            this = this.c;
                        } else {
                            try {
                                context.unbindService(this.c);
                            } catch (Exception e2) {
                                r.a("ZTEDeviceIDHelper", e2.getMessage());
                            }
                            return "";
                        }
                    } catch (Exception e3) {
                        r.a("ZTEDeviceIDHelper", e3.getMessage());
                        this = this.c;
                    }
                    context.unbindService(this);
                }
            } catch (Throwable th) {
                try {
                    context.unbindService(this.c);
                } catch (Exception e4) {
                    r.a("ZTEDeviceIDHelper", e4.getMessage());
                }
                throw th;
            }
        } catch (Exception e5) {
            r.a("ZTEDeviceIDHelper", e5.getMessage());
        }
        return strB;
    }
}
