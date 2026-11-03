package com.android.internal.telephony.util;

import java.net.Inet6Address;
import java.net.InetAddress;

/* JADX INFO: loaded from: classes.dex */
public final class ConnectivityUtils {
    public static int saturatedCast(long j) {
        if (j > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return j < -2147483648L ? NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_ROUTER : (int) j;
    }

    public static String addressAndPortToString(InetAddress inetAddress, int i) {
        return String.format(inetAddress instanceof Inet6Address ? "[%s]:%d" : "%s:%d", inetAddress.getHostAddress(), Integer.valueOf(i));
    }

    public static boolean isIPv6ULA(InetAddress inetAddress) {
        return (inetAddress instanceof Inet6Address) && (inetAddress.getAddress()[0] & 254) == 252;
    }
}
