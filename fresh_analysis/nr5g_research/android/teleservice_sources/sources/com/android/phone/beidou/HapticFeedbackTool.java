package com.android.phone.beidou;

import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import com.android.phone.PhoneGlobals;
import miuix.util.HapticFeedbackCompat;
import miuix.view.HapticFeedbackConstants;

/* JADX INFO: loaded from: classes.dex */
public class HapticFeedbackTool {
    private static HapticFeedbackTool mInstance;
    private long mCompletedCalibrationTime;
    private final HapticFeedbackCompat mHapticFeedbackCompat = new HapticFeedbackCompat(PhoneGlobals.getInstance());
    private final Vibrator mVibrator = (Vibrator) PhoneGlobals.getInstance().getSystemService("vibrator");

    private HapticFeedbackTool() {
    }

    public static HapticFeedbackTool getInstance() {
        if (mInstance == null) {
            mInstance = new HapticFeedbackTool();
        }
        return mInstance;
    }

    public void performCalibration() {
        this.mVibrator.vibrate(1000L);
        this.mCompletedCalibrationTime = SystemClock.elapsedRealtime();
        Log.v("Phone -> HapticFeedbackTool", "completed calibration and vibrated");
    }

    public void performDirectionGet(View view) {
        if (SystemClock.elapsedRealtime() <= this.mCompletedCalibrationTime + 1000) {
            return;
        }
        if (isSupportLinearMotorVibrate() && this.mHapticFeedbackCompat.supportLinearMotor()) {
            view.performHapticFeedback(HapticFeedbackConstants.MIUI_MESH_HEAVY);
        } else {
            view.performHapticFeedback(0);
        }
    }

    private boolean isSupportLinearMotorVibrate() {
        return this.mHapticFeedbackCompat.supportLinearMotor();
    }
}
