package miuix.smooth;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

/* JADX INFO: loaded from: classes2.dex */
public class SmoothPathProvider2 {
    private float mSmooth = 0.8f;
    private float mKsi = 0.46f;

    private static boolean isHeightCollapsed(float f, float f2, float f3, double d, float f4) {
        return ((double) f) <= ((double) (f2 + f3)) * ((d * ((double) f4)) + 1.0d);
    }

    private static boolean isWidthCollapsed(float f, float f2, float f3, double d, float f4) {
        return ((double) f) <= ((double) (f2 + f3)) * ((d * ((double) f4)) + 1.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double radToAngle(double d) {
        return (d * 180.0d) / 3.141592653589793d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double thetaForHeight(double d) {
        return (d * 3.141592653589793d) / 4.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double thetaForWidth(double d) {
        return (d * 3.141592653589793d) / 4.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double yForHeight(double d, double d2) {
        return d * d2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double yForWidth(double d, double d2) {
        return d * d2;
    }

    float getSmooth() {
        return this.mSmooth;
    }

    float getKsi() {
        return this.mKsi;
    }

    public SmoothData buildSmoothData(RectF rectF, float f, float f2, float f3) {
        return buildSmoothData(rectF, new float[]{f, f, f, f, f, f, f, f}, f2, f3);
    }

    public SmoothData buildSmoothData(RectF rectF, float[] fArr, float f, float f2) {
        float f3;
        float f4;
        if (fArr == null) {
            return null;
        }
        float ksi = getKsi();
        float smooth = getSmooth();
        float fWidth = rectF.width();
        float fHeight = rectF.height();
        double d = smooth;
        SmoothData smoothData = new SmoothData(fWidth, fHeight, d, ksi);
        float[] fArr2 = new float[8];
        fArr2[0] = 0.0f;
        fArr2[1] = 0.0f;
        fArr2[2] = 0.0f;
        fArr2[3] = 0.0f;
        fArr2[4] = 0.0f;
        fArr2[5] = 0.0f;
        fArr2[6] = 0.0f;
        fArr2[7] = 0.0f;
        for (int i = 0; i < Math.min(8, fArr.length); i++) {
            if (!Float.isNaN(fArr[i])) {
                fArr2[i] = fArr[i];
            }
        }
        float f5 = fArr2[0];
        float f6 = fArr2[1];
        float f7 = fArr2[2];
        float f8 = fArr2[3];
        float f9 = fArr2[4];
        float f10 = fArr2[5];
        float f11 = fArr2[6];
        float f12 = fArr2[7];
        if (f5 + f7 > fWidth) {
            float f13 = (fWidth * f5) / (f5 + f7);
            f7 = (fWidth * f7) / (f5 + f7);
            f5 = f13;
        }
        float f14 = f7;
        if (f8 + f10 > fHeight) {
            f3 = (fHeight * f10) / (f8 + f10);
            f4 = (fHeight * f8) / (f8 + f10);
        } else {
            f3 = f10;
            f4 = f8;
        }
        if (f9 + f11 > fWidth) {
            float f15 = (fWidth * f9) / (f9 + f11);
            f11 = (fWidth * f11) / (f9 + f11);
            f9 = f15;
        }
        if (f12 + f6 > fHeight) {
            float f16 = (fHeight * f12) / (f12 + f6);
            f6 = (fHeight * f6) / (f12 + f6);
            f12 = f16;
        }
        ensureFourCornerData(smoothData);
        smoothData.topLeft.build(Math.min(f5, f6), rectF, f, f2, d, ksi, 0);
        smoothData.topRight.build(Math.min(f14, f4), rectF, f, f2, d, ksi, 1);
        smoothData.bottomRight.build(Math.min(f9, f3), rectF, f, f2, d, ksi, 2);
        smoothData.bottomLeft.build(Math.min(f11, f12), rectF, f, f2, d, ksi, 3);
        return smoothData;
    }

    private void ensureFourCornerData(SmoothData smoothData) {
        if (smoothData.topLeft == null) {
            smoothData.topLeft = new CornerData();
        }
        if (smoothData.topRight == null) {
            smoothData.topRight = new CornerData();
        }
        if (smoothData.bottomRight == null) {
            smoothData.bottomRight = new CornerData();
        }
        if (smoothData.bottomLeft == null) {
            smoothData.bottomLeft = new CornerData();
        }
    }

    public Path getSmoothPath(Path path, SmoothData smoothData) {
        if (path == null) {
            path = new Path();
        }
        Path path2 = path;
        path2.reset();
        if (smoothData == null) {
            return path2;
        }
        if (isFourCornerDataValid(smoothData)) {
            path2.addRect(new RectF(0.0f, 0.0f, smoothData.width, smoothData.height), Path.Direction.CCW);
            return path2;
        }
        CornerData cornerData = smoothData.topLeft;
        if (cornerData.swapAngle != 0.0f) {
            path2.arcTo(cornerData.rect, (float) radToAngle(cornerData.thetaForVertical + 3.141592653589793d), smoothData.topLeft.swapAngle);
        } else {
            PointF pointF = cornerData.bezierAnchorHorizontal[0];
            path2.moveTo(pointF.x, pointF.y);
        }
        CornerData cornerData2 = smoothData.topLeft;
        if (cornerData2.smoothForHorizontal != 0.0d) {
            PointF[] pointFArr = cornerData2.bezierAnchorHorizontal;
            PointF pointF2 = pointFArr[1];
            float f = pointF2.x;
            float f2 = pointF2.y;
            PointF pointF3 = pointFArr[2];
            float f3 = pointF3.x;
            float f4 = pointF3.y;
            PointF pointF4 = pointFArr[3];
            path2.cubicTo(f, f2, f3, f4, pointF4.x, pointF4.y);
        }
        if (!isWidthCollapsed(smoothData.width, smoothData.topLeft.radius, smoothData.topRight.radius, smoothData.smooth, smoothData.ksi)) {
            PointF pointF5 = smoothData.topRight.bezierAnchorHorizontal[0];
            path2.lineTo(pointF5.x, pointF5.y);
        }
        CornerData cornerData3 = smoothData.topRight;
        if (cornerData3.smoothForHorizontal != 0.0d) {
            PointF[] pointFArr2 = cornerData3.bezierAnchorHorizontal;
            PointF pointF6 = pointFArr2[1];
            float f5 = pointF6.x;
            float f6 = pointF6.y;
            PointF pointF7 = pointFArr2[2];
            float f7 = pointF7.x;
            float f8 = pointF7.y;
            PointF pointF8 = pointFArr2[3];
            path2.cubicTo(f5, f6, f7, f8, pointF8.x, pointF8.y);
        }
        CornerData cornerData4 = smoothData.topRight;
        if (cornerData4.swapAngle != 0.0f) {
            path2.arcTo(cornerData4.rect, (float) radToAngle(cornerData4.thetaForHorizontal + 4.71238898038469d), smoothData.topRight.swapAngle);
        }
        CornerData cornerData5 = smoothData.topRight;
        if (cornerData5.smoothForVertical != 0.0d) {
            PointF[] pointFArr3 = cornerData5.bezierAnchorVertical;
            PointF pointF9 = pointFArr3[1];
            float f9 = pointF9.x;
            float f10 = pointF9.y;
            PointF pointF10 = pointFArr3[2];
            float f11 = pointF10.x;
            float f12 = pointF10.y;
            PointF pointF11 = pointFArr3[3];
            path2.cubicTo(f9, f10, f11, f12, pointF11.x, pointF11.y);
        }
        if (!isHeightCollapsed(smoothData.height, smoothData.topRight.radius, smoothData.bottomRight.radius, smoothData.smooth, smoothData.ksi)) {
            PointF pointF12 = smoothData.bottomRight.bezierAnchorVertical[0];
            path2.lineTo(pointF12.x, pointF12.y);
        }
        CornerData cornerData6 = smoothData.bottomRight;
        if (cornerData6.smoothForVertical != 0.0d) {
            PointF[] pointFArr4 = cornerData6.bezierAnchorVertical;
            PointF pointF13 = pointFArr4[1];
            float f13 = pointF13.x;
            float f14 = pointF13.y;
            PointF pointF14 = pointFArr4[2];
            float f15 = pointF14.x;
            float f16 = pointF14.y;
            PointF pointF15 = pointFArr4[3];
            path2.cubicTo(f13, f14, f15, f16, pointF15.x, pointF15.y);
        }
        CornerData cornerData7 = smoothData.bottomRight;
        if (cornerData7.swapAngle != 0.0f) {
            path2.arcTo(cornerData7.rect, (float) radToAngle(cornerData7.thetaForVertical), smoothData.bottomRight.swapAngle);
        }
        CornerData cornerData8 = smoothData.bottomRight;
        if (cornerData8.smoothForHorizontal != 0.0d) {
            PointF[] pointFArr5 = cornerData8.bezierAnchorHorizontal;
            PointF pointF16 = pointFArr5[1];
            float f17 = pointF16.x;
            float f18 = pointF16.y;
            PointF pointF17 = pointFArr5[2];
            float f19 = pointF17.x;
            float f20 = pointF17.y;
            PointF pointF18 = pointFArr5[3];
            path2.cubicTo(f17, f18, f19, f20, pointF18.x, pointF18.y);
        }
        if (!isWidthCollapsed(smoothData.width, smoothData.bottomRight.radius, smoothData.bottomLeft.radius, smoothData.smooth, smoothData.ksi)) {
            PointF pointF19 = smoothData.bottomLeft.bezierAnchorHorizontal[0];
            path2.lineTo(pointF19.x, pointF19.y);
        }
        CornerData cornerData9 = smoothData.bottomLeft;
        if (cornerData9.smoothForHorizontal != 0.0d) {
            PointF[] pointFArr6 = cornerData9.bezierAnchorHorizontal;
            PointF pointF20 = pointFArr6[1];
            float f21 = pointF20.x;
            float f22 = pointF20.y;
            PointF pointF21 = pointFArr6[2];
            float f23 = pointF21.x;
            float f24 = pointF21.y;
            PointF pointF22 = pointFArr6[3];
            path2.cubicTo(f21, f22, f23, f24, pointF22.x, pointF22.y);
        }
        CornerData cornerData10 = smoothData.bottomLeft;
        if (cornerData10.swapAngle != 0.0f) {
            path2.arcTo(cornerData10.rect, (float) radToAngle(cornerData10.thetaForHorizontal + 1.5707963267948966d), smoothData.bottomLeft.swapAngle);
        }
        CornerData cornerData11 = smoothData.bottomLeft;
        if (cornerData11.smoothForVertical != 0.0d) {
            PointF[] pointFArr7 = cornerData11.bezierAnchorVertical;
            PointF pointF23 = pointFArr7[1];
            float f25 = pointF23.x;
            float f26 = pointF23.y;
            PointF pointF24 = pointFArr7[2];
            float f27 = pointF24.x;
            float f28 = pointF24.y;
            PointF pointF25 = pointFArr7[3];
            path2.cubicTo(f25, f26, f27, f28, pointF25.x, pointF25.y);
        }
        if (!isHeightCollapsed(smoothData.height, smoothData.bottomLeft.radius, smoothData.topLeft.radius, smoothData.smooth, smoothData.ksi)) {
            PointF pointF26 = smoothData.topLeft.bezierAnchorVertical[0];
            path2.lineTo(pointF26.x, pointF26.y);
        }
        CornerData cornerData12 = smoothData.topLeft;
        if (cornerData12.smoothForVertical != 0.0d) {
            PointF[] pointFArr8 = cornerData12.bezierAnchorVertical;
            PointF pointF27 = pointFArr8[1];
            float f29 = pointF27.x;
            float f30 = pointF27.y;
            PointF pointF28 = pointFArr8[2];
            float f31 = pointF28.x;
            float f32 = pointF28.y;
            PointF pointF29 = pointFArr8[3];
            path2.cubicTo(f29, f30, f31, f32, pointF29.x, pointF29.y);
        }
        path2.close();
        return path2;
    }

    private boolean isFourCornerDataValid(SmoothData smoothData) {
        return smoothData.topLeft == null || smoothData.topRight == null || smoothData.bottomRight == null || smoothData.bottomLeft == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double smoothForWidth(float f, float f2, double d, float f3) {
        return isWidthCollapsed(f, f2, f2, d, f3) ? Math.max(Math.min(((f / (f2 * 2.0f)) - 1.0f) / f3, 1.0f), 0.0f) : d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double smoothForHeight(float f, float f2, double d, float f3) {
        return isHeightCollapsed(f, f2, f2, d, f3) ? Math.max(Math.min(((f / (f2 * 2.0f)) - 1.0f) / f3, 1.0f), 0.0f) : d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double mForHeight(float f, double d) {
        return ((double) f) * (1.0d - Math.cos(d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double nForHeight(float f, double d) {
        return ((double) f) * (1.0d - Math.sin(d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double mForWidth(float f, double d) {
        return ((double) f) * (1.0d - Math.sin(d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double nForWidth(float f, double d) {
        return ((double) f) * (1.0d - Math.cos(d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double pForWidth(float f, double d) {
        return ((double) f) * (1.0d - Math.tan(d / 2.0d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double pForHeight(float f, double d) {
        return ((double) f) * (1.0d - Math.tan(d / 2.0d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double xForWidth(float f, double d) {
        return ((((double) f) * 1.5d) * Math.tan(d / 2.0d)) / (Math.cos(d) + 1.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double xForHeight(float f, double d) {
        return ((((double) f) * 1.5d) * Math.tan(d / 2.0d)) / (Math.cos(d) + 1.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double kForWidth(double d, double d2) {
        if (d2 == 0.0d) {
            return 0.0d;
        }
        double d3 = d2 / 2.0d;
        return (((((d * 0.46000000834465027d) + Math.tan(d3)) * 2.0d) * (Math.cos(d2) + 1.0d)) / (Math.tan(d3) * 3.0d)) - 1.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double kForHeight(double d, double d2) {
        if (d2 == 0.0d) {
            return 0.0d;
        }
        double d3 = d2 / 2.0d;
        return (((((d * 0.46000000834465027d) + Math.tan(d3)) * 2.0d) * (Math.cos(d2) + 1.0d)) / (Math.tan(d3) * 3.0d)) - 1.0d;
    }

    public class CornerData {
        public PointF[] bezierAnchorHorizontal = new PointF[4];
        public PointF[] bezierAnchorVertical = new PointF[4];
        public float radius;
        public RectF rect;
        public double smoothForHorizontal;
        public double smoothForVertical;
        public float swapAngle;
        public double thetaForHorizontal;
        public double thetaForVertical;

        public void build(float f, RectF rectF, float f2, float f3, double d, float f4, int i) {
            this.radius = f;
            float fWidth = rectF.width();
            float fHeight = rectF.height();
            float f5 = rectF.left;
            float f6 = rectF.top;
            float f7 = rectF.right;
            float f8 = rectF.bottom;
            this.smoothForHorizontal = SmoothPathProvider2.smoothForWidth(fWidth, this.radius, d, f4);
            this.smoothForVertical = SmoothPathProvider2.smoothForHeight(fHeight, this.radius, d, f4);
            this.thetaForHorizontal = SmoothPathProvider2.thetaForWidth(this.smoothForHorizontal);
            double dThetaForHeight = SmoothPathProvider2.thetaForHeight(this.smoothForVertical);
            this.thetaForVertical = dThetaForHeight;
            this.swapAngle = (float) SmoothPathProvider2.radToAngle((1.5707963267948966d - dThetaForHeight) - this.thetaForHorizontal);
            double d2 = f4;
            double dKForWidth = SmoothPathProvider2.kForWidth(this.smoothForHorizontal * d2, this.thetaForHorizontal);
            double dMForWidth = SmoothPathProvider2.mForWidth(this.radius, this.thetaForHorizontal);
            double dNForWidth = SmoothPathProvider2.nForWidth(this.radius, this.thetaForHorizontal);
            double dPForWidth = SmoothPathProvider2.pForWidth(this.radius, this.thetaForHorizontal);
            double dXForWidth = SmoothPathProvider2.xForWidth(this.radius, this.thetaForHorizontal);
            double dYForWidth = SmoothPathProvider2.yForWidth(dKForWidth, dXForWidth);
            double dKForHeight = SmoothPathProvider2.kForHeight(this.smoothForVertical * d2, this.thetaForVertical);
            double dMForHeight = SmoothPathProvider2.mForHeight(this.radius, this.thetaForVertical);
            double dNForHeight = SmoothPathProvider2.nForHeight(this.radius, this.thetaForVertical);
            double dPForHeight = SmoothPathProvider2.pForHeight(this.radius, this.thetaForVertical);
            double dXForHeight = SmoothPathProvider2.xForHeight(this.radius, this.thetaForVertical);
            double dYForHeight = SmoothPathProvider2.yForHeight(dKForHeight, dXForHeight);
            if (i == 0) {
                float f9 = f5 + f2;
                float f10 = f6 + f3;
                float f11 = this.radius;
                this.rect = new RectF(f9, f10, (f11 * 2.0f) + f9, (f11 * 2.0f) + f10);
                double d3 = f9;
                float f12 = (float) (dMForWidth + d3);
                double d4 = f10;
                this.bezierAnchorHorizontal[0] = new PointF(f12, (float) (dNForWidth + d4));
                this.bezierAnchorHorizontal[1] = new PointF((float) (dPForWidth + d3), f10);
                double d5 = dPForWidth + dXForWidth;
                this.bezierAnchorHorizontal[2] = new PointF((float) (d5 + d3), f10);
                this.bezierAnchorHorizontal[3] = new PointF((float) (d5 + dYForWidth + d3), f10);
                double d6 = dPForHeight + dXForHeight;
                this.bezierAnchorVertical[0] = new PointF(f9, (float) (d6 + dYForHeight + d4));
                this.bezierAnchorVertical[1] = new PointF(f9, (float) (d6 + d4));
                this.bezierAnchorVertical[2] = new PointF(f9, (float) (dPForHeight + d4));
                this.bezierAnchorVertical[3] = new PointF((float) (dMForHeight + d3), (float) (dNForHeight + d4));
                return;
            }
            if (i == 1) {
                float f13 = f6 + f3;
                float f14 = this.radius;
                float f15 = f7 - f2;
                this.rect = new RectF((f7 - (f14 * 2.0f)) - f2, f13, f15, (f14 * 2.0f) + f13);
                double d7 = f7;
                double d8 = d7 - dPForWidth;
                double d9 = d8 - dXForWidth;
                double d10 = f2;
                this.bezierAnchorHorizontal[0] = new PointF((float) ((d9 - dYForWidth) - d10), f13);
                this.bezierAnchorHorizontal[1] = new PointF((float) (d9 - d10), f13);
                this.bezierAnchorHorizontal[2] = new PointF((float) (d8 - d10), f13);
                double d11 = f13;
                this.bezierAnchorHorizontal[3] = new PointF((float) ((d7 - dMForWidth) - d10), (float) (dNForWidth + d11));
                this.bezierAnchorVertical[0] = new PointF((float) ((d7 - dMForHeight) - d10), (float) (dNForHeight + d11));
                this.bezierAnchorVertical[1] = new PointF(f15, (float) (dPForHeight + d11));
                double d12 = dPForHeight + dXForHeight;
                this.bezierAnchorVertical[2] = new PointF(f15, (float) (d12 + d11));
                this.bezierAnchorVertical[3] = new PointF(f15, (float) (d12 + dYForHeight + d11));
                return;
            }
            if (i == 2) {
                float f16 = this.radius;
                float f17 = f7 - f2;
                float f18 = f8 - f3;
                this.rect = new RectF((f7 - (f16 * 2.0f)) - f2, (f8 - (f16 * 2.0f)) - f3, f17, f18);
                double d13 = f7;
                double d14 = f2;
                float f19 = (float) ((d13 - dMForWidth) - d14);
                double d15 = f8;
                double d16 = f3;
                this.bezierAnchorHorizontal[0] = new PointF(f19, (float) ((d15 - dNForWidth) - d16));
                double d17 = d13 - dPForWidth;
                this.bezierAnchorHorizontal[1] = new PointF((float) (d17 - d14), f18);
                double d18 = d17 - dXForWidth;
                this.bezierAnchorHorizontal[2] = new PointF((float) (d18 - d14), f18);
                this.bezierAnchorHorizontal[3] = new PointF((float) ((d18 - dYForWidth) - d14), f18);
                double d19 = d15 - dPForHeight;
                double d20 = d19 - dXForHeight;
                this.bezierAnchorVertical[0] = new PointF(f17, (float) ((d20 - dYForHeight) - d16));
                this.bezierAnchorVertical[1] = new PointF(f17, (float) (d20 - d16));
                this.bezierAnchorVertical[2] = new PointF(f17, (float) (d19 - d16));
                this.bezierAnchorVertical[3] = new PointF((float) ((d13 - dMForHeight) - d14), (float) ((d15 - dNForHeight) - d16));
                return;
            }
            if (i == 3) {
                float f20 = f5 + f2;
                float f21 = this.radius;
                float f22 = f8 - f3;
                this.rect = new RectF(f20, (f8 - (f21 * 2.0f)) - f3, (f21 * 2.0f) + f20, f22);
                double d21 = dPForWidth + dXForWidth;
                double d22 = d21 + dYForWidth;
                double d23 = f20;
                this.bezierAnchorHorizontal[0] = new PointF((float) (d22 + d23), f22);
                this.bezierAnchorHorizontal[1] = new PointF((float) (d21 + d23), f22);
                this.bezierAnchorHorizontal[2] = new PointF((float) (dPForWidth + d23), f22);
                double d24 = f8;
                double d25 = f3;
                this.bezierAnchorHorizontal[3] = new PointF((float) (dMForWidth + d23), (float) ((d24 - dNForWidth) - d25));
                this.bezierAnchorVertical[0] = new PointF((float) (dMForHeight + d23), (float) ((d24 - dNForHeight) - d25));
                double d26 = d24 - dPForHeight;
                this.bezierAnchorVertical[1] = new PointF(f20, (float) (d26 - d25));
                double d27 = d26 - dXForHeight;
                this.bezierAnchorVertical[2] = new PointF(f20, (float) (d27 - d25));
                this.bezierAnchorVertical[3] = new PointF(f20, (float) ((d27 - dYForHeight) - d25));
            }
        }
    }

    public class SmoothData {
        public float height;
        public float ksi;
        public double smooth;
        public float width;
        public CornerData topLeft = null;
        public CornerData topRight = null;
        public CornerData bottomRight = null;
        public CornerData bottomLeft = null;

        public SmoothData(float f, float f2, double d, float f3) {
            this.width = f;
            this.height = f2;
            this.smooth = d;
            this.ksi = f3;
        }
    }
}
