package com.xiaomi.onetrack.util.oaid;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.SystemClock;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.onetrack.util.oaid.helpers.b;
import com.xiaomi.onetrack.util.oaid.helpers.h;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.r;
import com.xiaomi.onetrack.util.s;
import com.xiaomi.onetrack.util.y;

/* JADX INFO: loaded from: classes2.dex */
public class a {
    private static final String a = "a";
    private static volatile a b;
    private static long d;
    private volatile String c = "";
    private final int e = 3;
    private volatile int f = 0;
    private boolean g = false;

    /* JADX INFO: renamed from: com.xiaomi.onetrack.util.oaid.a.a$a, reason: collision with other inner class name */
    public final class C0022a implements IInterface {
        private IBinder a;

        public C0022a(IBinder iBinder) {
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
                parcelObtain.writeInterfaceToken("com.asus.msa.SupplementaryDID.IDidAidlInterface");
                this.a.transact(3, parcelObtain, parcelObtain2, 0);
                parcelObtain2.readException();
                string = parcelObtain2.readString();
            } catch (Throwable th) {
                parcelObtain.recycle();
                parcelObtain2.recycle();
                th.printStackTrace();
                string = null;
            }
            parcelObtain.recycle();
            parcelObtain2.recycle();
            return string;
        }
    }

    public static a a() {
        if (b == null) {
            synchronized (a.class) {
                try {
                    if (b == null) {
                        b = new a();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return b;
    }

    private boolean d() {
        if (this.f > 3) {
            return true;
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long jAbs = Math.abs(jElapsedRealtime - d);
        if (this.f == 1) {
            if (jAbs < 3000) {
                return true;
            }
        } else if (this.f == 2) {
            if (jAbs < 10000) {
                return true;
            }
        } else if (this.f == 3 && jAbs < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
            return true;
        }
        r.a(a, "get time：" + this.f);
        d = jElapsedRealtime;
        return false;
    }

    public String a(Context context) {
        String strA;
        synchronized (this.c) {
            try {
                if (y.a()) {
                    if (r.a) {
                        throw new IllegalStateException("Don't use it on the main thread");
                    }
                    r.b(a, "getOaid() throw exception : Don't use it on the main thread");
                    return "";
                }
                if (this.c != null && !this.c.equals("")) {
                    return this.c;
                }
                if (d()) {
                    r.a(a, "isNotAllowedGetOaid");
                    return this.c;
                }
                if (s.b()) {
                    this.c = p.a(context);
                    this.f++;
                    return this.c;
                }
                if (!this.g && (strA = new h().a(context)) != null && !strA.equals("")) {
                    this.c = strA;
                    this.f++;
                    return strA;
                }
                String strA2 = new b().a(context);
                if (strA2 != null && !strA2.equals("")) {
                    this.c = strA2;
                    this.f++;
                    return strA2;
                }
                this.f++;
                return this.c;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
