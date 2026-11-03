package miui.telephony;

/* JADX INFO: loaded from: classes.dex */
public class IccUtils {
    public static String parseIccIdToString(byte[] data, int offset, int length) {
        StringBuilder ret = new StringBuilder(length * 2);
        for (int i = offset; i < offset + length; i++) {
            int v = data[i] & 15;
            if (v >= 0 && v <= 9) {
                ret.append((char) (v + 48));
            } else {
                ret.append((char) ((v + 65) - 10));
            }
            int v2 = (data[i] >> 4) & 15;
            if (v2 >= 0 && v2 <= 9) {
                ret.append((char) (v2 + 48));
            } else {
                ret.append((char) ((v2 + 65) - 10));
            }
        }
        return ret.toString();
    }
}
