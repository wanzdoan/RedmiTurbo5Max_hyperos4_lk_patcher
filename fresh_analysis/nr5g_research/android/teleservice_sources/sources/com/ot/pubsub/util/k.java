package com.ot.pubsub.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.ot.pubsub.PubSubTrack;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class k {
    private static final String a = "NetworkUtil";
    private static final int b = 16;
    private static final int c = 17;
    private static final int d = 18;
    private static final int e = 19;
    private static final int f = 20;
    private static final int g = 20;

    public enum a {
        WIFI,
        MN2G,
        MN3G,
        MN4G,
        NONE
    }

    public enum b {
        NOT_CONNECTED("NONE"),
        MOBILE_2G("2G"),
        MOBILE_3G("3G"),
        MOBILE_4G("4G"),
        MOBILE_5G("5G"),
        WIFI("WIFI"),
        ETHERNET("ETHERNET"),
        UNKNOWN("UNKNOWN");

        private String i;

        b(String str) {
            this.i = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.i;
        }
    }

    public static boolean a() {
        Context contextB = com.ot.pubsub.util.b.b();
        if (contextB == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) contextB.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnectedOrConnecting();
            }
            return false;
        } catch (Exception e2) {
            j.a(a, "isNetworkConnected exception: " + e2.getMessage());
            return false;
        }
    }

    public static a a(Context context) {
        try {
            if (p.a(context, "android.permission.ACCESS_NETWORK_STATE")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                    if (!connectivityManager.isActiveNetworkMetered()) {
                        return a.WIFI;
                    }
                }
                if (activeNetworkInfo == null) {
                    j.a(a, "networkInfo == null");
                } else {
                    j.a(a, "!networkInfo.isConnectedOrConnecting():" + activeNetworkInfo.getState().toString());
                }
                return a.NONE;
            }
            int networkType = ((TelephonyManager) context.getSystemService("phone")).getNetworkType();
            j.a(a, "getNetState networkType: " + networkType);
            return a(networkType);
        } catch (Exception e2) {
            j.b(a, "getNetState", e2);
            return a.NONE;
        }
    }

    public static PubSubTrack.NetType b(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == 1) {
                    return PubSubTrack.NetType.WIFI;
                }
                if (activeNetworkInfo.getType() == 0) {
                    switch (activeNetworkInfo.getSubtype()) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                        case 16:
                            return PubSubTrack.NetType.MOBILE_2G;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                        case 17:
                            return PubSubTrack.NetType.MOBILE_3G;
                        case 13:
                        case 18:
                        case 19:
                            return PubSubTrack.NetType.MOBILE_4G;
                        case 20:
                            return PubSubTrack.NetType.MOBILE_5G;
                        default:
                            return PubSubTrack.NetType.UNKNOWN;
                    }
                }
                if (activeNetworkInfo.getType() == 9) {
                    return PubSubTrack.NetType.ETHERNET;
                }
                return PubSubTrack.NetType.UNKNOWN;
            }
            return PubSubTrack.NetType.NOT_CONNECTED;
        } catch (Exception e2) {
            j.b(a, "getNetworkState error", e2);
        }
    }

    private static int a(Context context, TelephonyManager telephonyManager) {
        try {
            Class[] clsArr = new Class[0];
            Object objInvoke = Class.forName("android.telephony.SubscriptionManager").getMethod("getDefaultDataSubId", null).invoke(null, null);
            Method method = Class.forName("android.telephony.TelephonyManager").getMethod("getNetworkType", Integer.TYPE);
            Integer num = (Integer) objInvoke;
            num.intValue();
            return ((Integer) method.invoke(telephonyManager, num)).intValue();
        } catch (Exception e2) {
            j.b(a, "getNetworkTypeAndroidMandL exception: ", e2);
            return telephonyManager.getNetworkType();
        }
    }

    private static a a(int i) {
        switch (i) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return a.MN2G;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
            case 20:
                return a.MN3G;
            case 13:
            case 18:
            case 19:
                return a.MN4G;
            default:
                return a.NONE;
        }
    }
}
