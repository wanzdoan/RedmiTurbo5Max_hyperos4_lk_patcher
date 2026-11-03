package miuix;

import android.util.Log;
import miuix.core.util.SystemProperties;

/* JADX INFO: loaded from: classes2.dex */
public abstract class HapticLog {
    private static final boolean sLogHapticEnabled = SystemProperties.getBoolean("log.miuix.haptic.enabled", false);

    public static void printTrace(String str) {
        if (sLogHapticEnabled) {
            Log.d("MiuixHaptic", str, new Throwable());
        }
    }
}
