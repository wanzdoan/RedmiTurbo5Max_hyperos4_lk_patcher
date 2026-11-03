package miuix.util;

import android.content.Context;
import android.os.Looper;
import android.os.VibrationAttributes;
import android.util.Log;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import miui.util.HapticFeedbackUtil;
import miuix.HapticLog;
import miuix.view.HapticCompat;
import miuix.view.PlatformConstants;

/* JADX INFO: loaded from: classes2.dex */
public class HapticFeedbackCompat {
    private static boolean mAvailable;
    private static boolean mCanCheckExtHaptic;
    private static boolean mCanStop;
    private static boolean mExtHapticAlways;
    private static boolean mIsSupportExtHapticWithReason;
    private static boolean mIsSupportHapticWithReason;
    private static boolean mPerformExtHapticFeedbackThreeParamsMethodExist;
    private static boolean mPerformExtHapticFeedbackTwoParamsMethodExist;
    private static boolean mPerformHapticFeedbackFourParamsMethod1Exist;
    private static boolean mPerformHapticFeedbackFourParamsMethod2Exist;
    private static final Executor sSingleThread = Executors.newSingleThreadExecutor();
    private HapticFeedbackUtil mHapticFeedbackUtil;

    static {
        if (PlatformConstants.VERSION >= 1) {
            try {
                mAvailable = HapticFeedbackUtil.isSupportLinearMotorVibrate();
            } catch (Throwable th) {
                Log.w("HapticFeedbackCompat", "MIUI Haptic Implementation is not available", th);
                mAvailable = false;
            }
            if (mAvailable) {
                try {
                    HapticFeedbackUtil.class.getMethod("performHapticFeedback", Integer.TYPE, Double.TYPE, String.class);
                    mIsSupportHapticWithReason = true;
                } catch (Throwable th2) {
                    Log.w("HapticFeedbackCompat", "Not support haptic with reason", th2);
                    mIsSupportHapticWithReason = false;
                }
                try {
                    HapticFeedbackUtil.class.getMethod("isSupportExtHapticFeedback", Integer.TYPE);
                    mCanCheckExtHaptic = true;
                } catch (Throwable unused) {
                    mCanCheckExtHaptic = false;
                }
                try {
                    HapticFeedbackUtil.class.getMethod("performExtHapticFeedback", Integer.TYPE, Boolean.TYPE);
                    mExtHapticAlways = true;
                } catch (Throwable unused2) {
                    mExtHapticAlways = false;
                }
                try {
                    Class[] clsArr = new Class[0];
                    HapticFeedbackUtil.class.getMethod("stop", null);
                    mCanStop = true;
                } catch (Throwable unused3) {
                    mCanStop = false;
                }
                try {
                    HapticFeedbackUtil.class.getMethod("performExtHapticFeedback", Integer.TYPE, Double.TYPE, String.class);
                    mIsSupportExtHapticWithReason = true;
                } catch (Throwable th3) {
                    Log.w("HapticFeedbackCompat", "Not support ext haptic with reason", th3);
                    mIsSupportExtHapticWithReason = false;
                }
            }
        }
        if (PlatformConstants.romHapticVersion >= 1.2d) {
            try {
                HapticFeedbackUtil.class.getMethod("performExtHapticFeedback", VibrationAttributes.class, Integer.TYPE);
                mPerformExtHapticFeedbackTwoParamsMethodExist = true;
            } catch (Exception unused4) {
            }
            try {
                HapticFeedbackUtil.class.getMethod("performExtHapticFeedback", VibrationAttributes.class, Integer.TYPE, Boolean.TYPE);
                mPerformExtHapticFeedbackThreeParamsMethodExist = true;
            } catch (Exception unused5) {
            }
            try {
                Class cls = Integer.TYPE;
                HapticFeedbackUtil.class.getMethod("performHapticFeedback", VibrationAttributes.class, cls, Boolean.TYPE, cls);
                mPerformHapticFeedbackFourParamsMethod1Exist = true;
            } catch (Exception unused6) {
            }
            try {
                HapticFeedbackUtil.class.getMethod("performHapticFeedback", VibrationAttributes.class, Integer.TYPE, Double.TYPE, String.class);
                mPerformHapticFeedbackFourParamsMethod2Exist = true;
            } catch (Exception unused7) {
            }
        }
    }

    public HapticFeedbackCompat(Context context, boolean z) {
        if (PlatformConstants.VERSION < 1) {
            Log.w("HapticFeedbackCompat", "MiuiHapticFeedbackConstants not found or not compatible for LinearVibrator.");
        } else if (!mAvailable) {
            Log.w("HapticFeedbackCompat", "linear motor is not supported in this platform.");
        } else {
            this.mHapticFeedbackUtil = new HapticFeedbackUtil(context, z);
        }
    }

    public HapticFeedbackCompat(Context context) {
        this(context, true);
    }

    public boolean supportLinearMotor() {
        return mAvailable;
    }

    public boolean performExtHapticFeedback(int i) {
        if (this.mHapticFeedbackUtil == null) {
            return false;
        }
        HapticLog.printTrace("performExtHapticFeedback: " + i);
        return this.mHapticFeedbackUtil.performExtHapticFeedback(i);
    }

    public void performExtHapticFeedbackAsync(final int i) {
        if (this.mHapticFeedbackUtil == null) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            sSingleThread.execute(new Runnable() { // from class: miuix.util.HapticFeedbackCompat$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.performExtHapticFeedback(i);
                }
            });
        } else {
            performExtHapticFeedback(i);
        }
    }

    public boolean performExtHapticFeedback(int i, boolean z) {
        if (this.mHapticFeedbackUtil == null) {
            return false;
        }
        HapticLog.printTrace("performExtHapticFeedback: effectId: " + i + ", always: " + z);
        if (mExtHapticAlways && z) {
            return this.mHapticFeedbackUtil.performExtHapticFeedback(i, true);
        }
        return this.mHapticFeedbackUtil.performExtHapticFeedback(i);
    }

    public boolean performHapticFeedback(int i, int i2, boolean z) {
        return performHapticFeedback(null, i, i2, z);
    }

    public boolean performHapticFeedback(VibrationAttributes vibrationAttributes, int i, int i2, boolean z) {
        int iObtainFeedBack;
        if (this.mHapticFeedbackUtil == null || (iObtainFeedBack = HapticCompat.obtainFeedBack(i)) == -1) {
            return false;
        }
        try {
            HapticLog.printTrace("performHapticFeedback: attributes: " + vibrationAttributes + ", effectId: " + i + ", effectStrength: " + i2 + ", always: " + z);
            if (PlatformConstants.romHapticVersion >= 1.2d && mPerformHapticFeedbackFourParamsMethod1Exist) {
                return this.mHapticFeedbackUtil.performHapticFeedback(vibrationAttributes, i, z, i2);
            }
            return this.mHapticFeedbackUtil.performHapticFeedback(iObtainFeedBack, z, i2);
        } catch (Exception e) {
            Log.e("HapticFeedbackCompat", "Failed to perform haptic!", e);
            return false;
        }
    }
}
