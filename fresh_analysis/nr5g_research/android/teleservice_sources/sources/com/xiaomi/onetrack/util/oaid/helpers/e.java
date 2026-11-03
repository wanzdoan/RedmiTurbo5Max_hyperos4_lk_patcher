package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.util.Log;
import com.xiaomi.onetrack.util.oaid.a.c$a;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class e {
    public final LinkedBlockingQueue a = new LinkedBlockingQueue(1);
    public final LinkedBlockingQueue b = new LinkedBlockingQueue(1);
    ServiceConnection c = new ServiceConnection() { // from class: com.xiaomi.onetrack.util.oaid.helpers.HonorDeviceIDHelper$1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder == null) {
                return;
            }
            try {
                this.a.a.offer(iBinder, 1L, TimeUnit.SECONDS);
            } catch (Exception e) {
                Log.d("HonorDeviceIDHelper", "onServiceConnected get exception: " + e.getMessage());
            }
        }
    };
    private a e;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v0, types: [com.xiaomi.onetrack.util.oaid.a.c$b] */
    /* JADX WARN: Type inference failed for: r7v3, types: [android.content.ServiceConnection] */
    public String a(Context context) {
        String str = "";
        try {
            try {
                Intent intent = new Intent("com.hihonor.id.HnOaIdService");
                intent.setPackage("com.hihonor.id");
                if (context.bindService(intent, this.c, 1)) {
                    LinkedBlockingQueue linkedBlockingQueue = this.a;
                    TimeUnit timeUnit = TimeUnit.SECONDS;
                    final IBinder iBinder = (IBinder) linkedBlockingQueue.poll(1L, timeUnit);
                    if (iBinder != null) {
                        ?? r6 = new IInterface(iBinder) { // from class: com.xiaomi.onetrack.util.oaid.a.c$b
                            private IBinder a;

                            {
                                this.a = iBinder;
                            }

                            @Override // android.os.IInterface
                            public IBinder asBinder() {
                                return this.a;
                            }

                            public void a(c$a.a aVar) {
                                Parcel parcelObtain = Parcel.obtain();
                                Parcel parcelObtain2 = Parcel.obtain();
                                try {
                                    parcelObtain.writeInterfaceToken("com.hihonor.cloudservice.oaid.IOAIDService");
                                    parcelObtain.writeStrongBinder(aVar);
                                    this.a.transact(2, parcelObtain, parcelObtain2, 0);
                                    parcelObtain2.readException();
                                    parcelObtain2.recycle();
                                    parcelObtain.recycle();
                                } catch (Throwable th) {
                                    parcelObtain2.recycle();
                                    parcelObtain.recycle();
                                    th.printStackTrace();
                                }
                            }
                        };
                        a aVar = new a();
                        this.e = aVar;
                        r6.a(aVar);
                        str = (String) this.b.poll(1L, timeUnit);
                    }
                }
            } catch (Exception e) {
                Log.e("HonorDeviceIDHelper", "aidl getOaid error: " + e.getMessage());
            }
            return str;
        } finally {
            context.unbindService(this.c);
        }
    }

    public class a extends c$a.a {
        @Override // com.xiaomi.onetrack.util.oaid.a.c$a
        public void a(int i, long j, boolean z, float f, double d, String str) {
        }

        public a() {
        }

        @Override // com.xiaomi.onetrack.util.oaid.a.c$a
        public void a(int i, Bundle bundle) {
            if (bundle == null || !bundle.containsKey("oa_id_flag")) {
                return;
            }
            try {
                e.this.b.offer(bundle.getString("oa_id_flag"), 1L, TimeUnit.SECONDS);
            } catch (Exception e) {
                Log.d("HonorDeviceIDHelper", "callback exception: " + e.getMessage());
            }
        }
    }
}
