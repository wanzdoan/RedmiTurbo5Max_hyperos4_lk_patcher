package com.android.phone;

/* JADX INFO: loaded from: classes.dex */
public abstract class SensorHelper {
    private static float MIN_FLT_TO_AVOID_SINGULARITY = 1.0E-4f;

    public static void quatToRotMat(float[] fArr, float[] fArr2) {
        float f = fArr2[0];
        float f2 = fArr2[1];
        float f3 = fArr2[2];
        float f4 = fArr2[3];
        float f5 = f * f;
        float f6 = f * f2;
        float f7 = f * f3;
        float f8 = f * f4;
        float f9 = f2 * f2;
        float f10 = f2 * f3;
        float f11 = f2 * f4;
        float f12 = f3 * f3;
        float f13 = f3 * f4;
        fArr[0] = 1.0f - ((f9 + f12) * 2.0f);
        fArr[1] = (f6 - f13) * 2.0f;
        fArr[2] = (f7 + f11) * 2.0f;
        fArr[3] = (f6 + f13) * 2.0f;
        fArr[4] = 1.0f - ((f12 + f5) * 2.0f);
        fArr[5] = (f10 - f8) * 2.0f;
        fArr[6] = (f7 - f11) * 2.0f;
        fArr[7] = (f10 + f8) * 2.0f;
        fArr[8] = 1.0f - ((f5 + f9) * 2.0f);
    }

    public static void rotMatToOrient(float[] fArr, float[] fArr2) {
        float[] fArr3 = {fArr2[0], fArr2[3], fArr2[6]};
        float[] fArr4 = {fArr2[1], fArr2[4], fArr2[7]};
        float[] fArr5 = {fArr2[2], fArr2[5], fArr2[8]};
        float f = fArr3[0];
        float f2 = fArr3[1];
        float fSqrt = (float) Math.sqrt((f * f) + (f2 * f2));
        float fAbs = Math.abs(fArr5[2]);
        float f3 = MIN_FLT_TO_AVOID_SINGULARITY;
        if (fAbs < f3) {
            fArr5[2] = f3 * (fArr5[2] < 0.0f ? -1 : 1);
        }
        float fAbs2 = Math.abs(fArr3[0]);
        float f4 = MIN_FLT_TO_AVOID_SINGULARITY;
        if (fAbs2 < f4) {
            fArr3[0] = f4 * (fArr3[0] < 0.0f ? -1 : 1);
        }
        float fAbs3 = Math.abs(fSqrt);
        float f5 = MIN_FLT_TO_AVOID_SINGULARITY;
        if (fAbs3 < f5) {
            fSqrt = (fSqrt >= 0.0f ? 1 : -1) * f5;
        }
        float fAtan2 = (float) (Math.atan2(fArr3[1], fArr3[0]) * 57.295780181884766d);
        fArr[0] = fAtan2;
        fArr[0] = 360.0f - (fAtan2 % 360.0f);
        fArr[1] = (float) (Math.atan2(fArr4[2], fArr5[2]) * (-57.295780181884766d));
        fArr[2] = (float) (Math.atan2(fArr3[2], fSqrt) * 57.295780181884766d);
    }
}
