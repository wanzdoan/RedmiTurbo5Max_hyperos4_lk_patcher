package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.Signature;
import android.os.IBinder;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.onetrack.util.r;
import java.security.MessageDigest;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes2.dex */
public class k {
    com.xiaomi.onetrack.util.oaid.a.f a;
    public final LinkedBlockingQueue b = new LinkedBlockingQueue(1);
    ServiceConnection c = new ServiceConnection() { // from class: com.xiaomi.onetrack.util.oaid.helpers.OppoDeviceIDHelper$1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.a.b.offer(iBinder, 1L, TimeUnit.SECONDS);
            } catch (Exception e) {
                r.a("OppoDeviceIDHelper", e.getMessage());
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            this.a.a = null;
        }
    };
    private String e;

    public String a(Context context) {
        ServiceConnection serviceConnection;
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.heytap.openid", "com.heytap.openid.IdentifyService"));
        intent.setAction("action.com.heytap.openid.OPEN_ID_SERVICE");
        String strA = "";
        try {
            if (context.bindService(intent, this.c, 1)) {
                try {
                    try {
                        IBinder iBinder = (IBinder) this.b.poll(1L, TimeUnit.SECONDS);
                        if (iBinder != null) {
                            com.xiaomi.onetrack.util.oaid.a.f fVarA = com.xiaomi.onetrack.util.oaid.a.f.a.a(iBinder);
                            this.a = fVarA;
                            if (fVarA != null) {
                                strA = a("OUID", context);
                            }
                            serviceConnection = this.c;
                        } else {
                            try {
                                context.unbindService(this.c);
                                return "";
                            } catch (Exception e) {
                                r.a("OppoDeviceIDHelper", e.getMessage());
                                return "";
                            }
                        }
                    } catch (Exception e2) {
                        r.a("OppoDeviceIDHelper", e2.getMessage());
                        serviceConnection = this.c;
                    }
                    context.unbindService(serviceConnection);
                } catch (Throwable th) {
                    try {
                        context.unbindService(this.c);
                    } catch (Exception e3) {
                        r.a("OppoDeviceIDHelper", e3.getMessage());
                    }
                    throw th;
                }
            }
        } catch (Exception e4) {
            r.a("OppoDeviceIDHelper", e4.getMessage());
        }
        return strA;
    }

    private String a(String str, Context context) {
        Signature[] signatureArr;
        String packageName = context.getPackageName();
        if (this.e == null) {
            String string = null;
            try {
                signatureArr = context.getPackageManager().getPackageInfo(packageName, 64).signatures;
            } catch (Exception e) {
                e.printStackTrace();
                signatureArr = null;
            }
            if (signatureArr != null && signatureArr.length > 0) {
                byte[] byteArray = signatureArr[0].toByteArray();
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                    if (messageDigest != null) {
                        byte[] bArrDigest = messageDigest.digest(byteArray);
                        StringBuilder sb = new StringBuilder();
                        for (byte b : bArrDigest) {
                            sb.append(Integer.toHexString((b & TransitionInfo.INIT) | DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER).substring(1, 3));
                        }
                        string = sb.toString();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            this.e = string;
        }
        return ((com.xiaomi.onetrack.util.oaid.a.f.a.C0024a) this.a).a(packageName, this.e, str);
    }
}
