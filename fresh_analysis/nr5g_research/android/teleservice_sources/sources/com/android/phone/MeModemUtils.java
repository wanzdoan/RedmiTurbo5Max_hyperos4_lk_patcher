package com.android.phone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.phone.utils.ATCommandUtils;
import com.ot.pubsub.util.s;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public abstract class MeModemUtils {
    private static final Pattern IFACE_NUMBER_PATTERN = Pattern.compile("(\\d+)$");

    public static void setModemPara(byte[] bArr) {
        File file = new File("/proc/modem_para");
        if (!file.exists()) {
            logd("setModemPara failed as proc node does not exist!");
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                fileOutputStream.write(bArr);
                fileOutputStream.close();
                logd("setModemPara num=" + bArr.length);
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Exception e) {
            loge("setModemPara errors", e);
        }
    }

    public static ByteBuffer getModemPara(int i) {
        File file = new File("/proc/modem_para");
        if (!file.exists()) {
            logd("getModemPara failed as proc node does not exist!");
            return null;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                fileOutputStream.write(new byte[]{16, (byte) i});
                fileOutputStream.close();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        byte[] bArr = new byte[100];
                        int i2 = fileInputStream.read(bArr);
                        if (i2 < 24) {
                            loge("getModemPara: insufficient data read: " + i2, null);
                            fileInputStream.close();
                            return null;
                        }
                        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, 0, i2);
                        byteBufferWrap.order(ByteOrder.nativeOrder());
                        fileInputStream.close();
                        return byteBufferWrap;
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (Exception e) {
                    loge("getModemPara read errors", e);
                    return null;
                }
            } catch (Throwable th3) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th4) {
                    th3.addSuppressed(th4);
                }
                throw th3;
            }
        } catch (Exception e2) {
            loge("getModemPara fos write errors", e2);
            return null;
        }
    }

    public static void sendATCommand(Phone phone, String[] strArr, Message message) {
        if (phone == null || message == null) {
            logd("sendATCommand failed as phone or msg is null");
            return;
        }
        try {
            logd("sendATCommand: " + strArr[0]);
            if (ATCommandUtils.getInstance() != null) {
                ATCommandUtils.getInstance().sendAtCommand(phone, strArr, message);
            } else {
                loge("ATCommandUtils is null", null);
            }
        } catch (Exception e) {
            loge("sendATCommand errors", e);
        }
    }

    public static int ipStringToIntBigEndian(String str) {
        if (str != null && !str.isEmpty()) {
            String[] strArrSplit = str.split(s.a);
            if (strArrSplit.length != 4) {
                loge("Only IPv4 addresses are supported: ".concat(str), null);
                return 0;
            }
            try {
                return Integer.parseInt(strArrSplit[3]) | (Integer.parseInt(strArrSplit[0]) << 24) | (Integer.parseInt(strArrSplit[1]) << 16) | (Integer.parseInt(strArrSplit[2]) << 8);
            } catch (NumberFormatException e) {
                loge("Failed to convert IP string: ".concat(str), e);
            }
        }
        return 0;
    }

    public static int getDefaultDataIfaceNumber(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                if (networkCapabilities != null && networkCapabilities.hasTransport(0)) {
                    LinkProperties linkProperties = connectivityManager.getLinkProperties(activeNetwork);
                    if (linkProperties != null && linkProperties.getInterfaceName() != null) {
                        String interfaceName = linkProperties.getInterfaceName();
                        Matcher matcher = IFACE_NUMBER_PATTERN.matcher(interfaceName);
                        if (matcher.find()) {
                            int i = Integer.parseInt(matcher.group(1));
                            if (i < 0 || i > 19) {
                                logd("getDefaultDataIfaceNumber: iface=" + interfaceName + ", number=" + i + " out of valid range [0, 19]");
                                return -1;
                            }
                            logd("getDefaultDataIfaceNumber: iface=" + interfaceName + ", number=" + i);
                            return i;
                        }
                        logd("getDefaultDataIfaceNumber: no trailing number in iface=" + interfaceName);
                    }
                }
                logd("getDefaultDataIfaceNumber: active network is not cellular");
                return -1;
            }
            logd("getDefaultDataIfaceNumber: network or link properties unavailable");
        } catch (Exception e) {
            loge("getDefaultDataIfaceNumber error", e);
        }
        return -1;
    }

    public static void registerForOemIndication(boolean z, Handler handler, int i) {
        try {
            if (ATCommandUtils.getInstance() != null) {
                if (z) {
                    ATCommandUtils.getInstance().regUnRegisterOemAtUrcInd(true, handler, i);
                    logd("reg ind(" + i + ") done");
                    return;
                }
                ATCommandUtils.getInstance().regUnRegisterOemAtUrcInd(false, handler, i);
                logd("unreg ind(" + i + ") done");
            }
        } catch (Exception e) {
            loge("registerForOemIndication errors", e);
        }
    }

    private static void logd(String str) {
        Rlog.d("MeModemUtils", str);
    }

    private static void loge(String str, Exception exc) {
        Rlog.e("MeModemUtils", str, exc);
    }
}
