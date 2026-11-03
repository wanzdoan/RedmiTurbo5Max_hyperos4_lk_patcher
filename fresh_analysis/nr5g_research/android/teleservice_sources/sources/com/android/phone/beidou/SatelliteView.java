package com.android.phone.beidou;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteView extends View {
    private static final String TAG = SatelliteView.class.getSimpleName();
    private boolean mAlignedAzimuth;
    private boolean mAlignedAzimuthUI;
    private boolean mAlignedElevation;
    private int mAzimuthAlpha;
    private ValueAnimator mAzimuthAnim;
    private int mAzimuthDelta;
    private float mAzimuthUI;
    private Bitmap mBeidouSMSState;
    private int mCircleAlpha;
    private ValueAnimator mCircleAnim;
    private int mCircleX;
    private int mCircleY;
    private int mElevationDelta;
    private boolean mInOpenSpace;
    private BDSatelliteDynInfo mInfo;
    private Bitmap mSatellite;
    private int mSatelliteAlpha;
    private ValueAnimator mSatelliteAnim;
    private float mSatelliteRadius;
    private float mSatelliteX;
    private float mSatelliteY;
    private int mScene;
    private int mTargetAzimuth;
    private int mTrueAzimuth;
    private Bitmap mUser;

    public SatelliteView(Context context) {
        super(context);
        this.mAzimuthAlpha = 255;
        this.mSatelliteAlpha = 0;
        this.mCircleAlpha = 14;
        this.mSatelliteRadius = 0.0f;
    }

    public SatelliteView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAzimuthAlpha = 255;
        this.mSatelliteAlpha = 0;
        this.mCircleAlpha = 14;
        this.mSatelliteRadius = 0.0f;
    }

    public SatelliteView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAzimuthAlpha = 255;
        this.mSatelliteAlpha = 0;
        this.mCircleAlpha = 14;
        this.mSatelliteRadius = 0.0f;
    }

    public boolean isInMoveElevation() {
        return this.mSatelliteAlpha > 0;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        BDSmsSendStatus bDSmsSendStatus;
        int i;
        float dimension;
        float dimension2;
        int i2;
        float dimension3;
        float dimension4;
        super.onDraw(canvas);
        updateUserStyle();
        updateSatelliteStyle();
        this.mCircleX = canvas.getWidth() / 2;
        this.mCircleY = canvas.getHeight() / 2;
        if (this.mInOpenSpace) {
            double width = this.mSatellite.getWidth() / 3;
            BDSatelliteDynInfo bDSatelliteDynInfo = this.mInfo;
            BDSatelliteDynInfo.BDSmsRecInfo bDSmsRecInfo = bDSatelliteDynInfo.mRecInfo;
            if (bDSmsRecInfo != null) {
                if (bDSmsRecInfo.mRecSuccess) {
                    i2 = R.drawable.beidou_sms_success;
                    dimension3 = getResources().getDimension(R.dimen.beidou_sms_success_width);
                    dimension4 = getResources().getDimension(R.dimen.beidou_sms_success_height);
                } else {
                    i2 = R.drawable.beidou_sms_failed;
                    dimension3 = getResources().getDimension(R.dimen.beidou_sms_failed_width);
                    dimension4 = getResources().getDimension(R.dimen.beidou_sms_failed_height);
                }
                Bitmap bitmap = getBitmap(getContext(), i2, (int) dimension3, (int) dimension4);
                this.mBeidouSMSState = bitmap;
                canvas.drawBitmap(bitmap, this.mCircleX - (dimension3 / 2.0f), this.mCircleY - (dimension4 / 2.0f), (Paint) null);
                return;
            }
            BDSatelliteDynInfo.BDSmsSendInfo bDSmsSendInfo = bDSatelliteDynInfo.mSendInfo;
            if (bDSmsSendInfo != null && (bDSmsSendStatus = bDSmsSendInfo.mSmsSendStatus) != BDSmsSendStatus.SEND_STATUS_SENDING) {
                if (bDSmsSendStatus == BDSmsSendStatus.SEND_STATUS_SUCC || bDSmsSendStatus == BDSmsSendStatus.SEND_STATUS_SENT || bDSmsSendInfo.mFailedCause == BDSmsSendFailedCause.NEAR_THRESHOLD) {
                    i = R.drawable.beidou_sms_success;
                    dimension = getResources().getDimension(R.dimen.beidou_sms_success_width);
                    dimension2 = getResources().getDimension(R.dimen.beidou_sms_success_height);
                } else {
                    i = R.drawable.beidou_sms_failed;
                    dimension = getResources().getDimension(R.dimen.beidou_sms_failed_width);
                    dimension2 = getResources().getDimension(R.dimen.beidou_sms_failed_height);
                }
                Bitmap bitmap2 = getBitmap(getContext(), i, (int) dimension, (int) dimension2);
                this.mBeidouSMSState = bitmap2;
                canvas.drawBitmap(bitmap2, this.mCircleX - (dimension / 2.0f), this.mCircleY - (dimension2 / 2.0f), (Paint) null);
                return;
            }
            boolean z = this.mAlignedAzimuth;
            if (z) {
                if (this.mAzimuthAnim == null) {
                    ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(255, 0);
                    this.mAzimuthAnim = valueAnimatorOfInt;
                    valueAnimatorOfInt.setDuration(800L);
                    this.mAzimuthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.phone.beidou.SatelliteView$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            this.f$0.lambda$onDraw$0(valueAnimator);
                        }
                    });
                    this.mAzimuthAnim.setStartDelay(200L);
                    this.mAzimuthAnim.start();
                }
                if (this.mSatelliteAnim == null && this.mAzimuthAlpha == 0) {
                    ValueAnimator valueAnimatorOfInt2 = ValueAnimator.ofInt(0, 255);
                    this.mSatelliteAnim = valueAnimatorOfInt2;
                    valueAnimatorOfInt2.setDuration(500L);
                    this.mSatelliteAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.phone.beidou.SatelliteView$$ExternalSyntheticLambda1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            this.f$0.lambda$onDraw$1(valueAnimator);
                        }
                    });
                    this.mSatelliteAnim.start();
                }
                if (this.mCircleAnim == null) {
                    ValueAnimator valueAnimatorOfInt3 = ValueAnimator.ofInt(14, 0);
                    this.mCircleAnim = valueAnimatorOfInt3;
                    valueAnimatorOfInt3.setDuration(800L);
                    this.mCircleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.phone.beidou.SatelliteView$$ExternalSyntheticLambda2
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            this.f$0.lambda$onDraw$2(valueAnimator);
                        }
                    });
                    this.mCircleAnim.setStartDelay(200L);
                    this.mCircleAnim.start();
                }
            } else if ((!z && !isInMoveElevation()) || !this.mAlignedAzimuthUI) {
                ValueAnimator valueAnimator = this.mAzimuthAnim;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.mAzimuthAnim = null;
                    this.mAzimuthAlpha = 255;
                }
                ValueAnimator valueAnimator2 = this.mSatelliteAnim;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                    this.mSatelliteAnim = null;
                    this.mSatelliteAlpha = 0;
                }
                ValueAnimator valueAnimator3 = this.mCircleAnim;
                if (valueAnimator3 != null) {
                    valueAnimator3.cancel();
                    this.mCircleAnim = null;
                    this.mCircleAlpha = 14;
                }
                this.mSatelliteRadius = (float) (((double) getCircleRadius(1)) - width);
            }
            if (this.mAzimuthAlpha > 0) {
                drawOutCircle(canvas);
                drawAzimuth(canvas);
                drawSatellite(canvas, true);
            }
            drawElevation(canvas, this.mCircleAlpha <= 0);
            if (this.mSatelliteAlpha > 0) {
                drawSatellite(canvas, false);
                return;
            }
            return;
        }
        this.mCircleAlpha = 14;
        drawOutCircle(canvas);
        drawScanner(canvas);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$0(ValueAnimator valueAnimator) {
        this.mAzimuthAlpha = ((Integer) valueAnimator.getAnimatedValue()).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$1(ValueAnimator valueAnimator) {
        this.mSatelliteAlpha = ((Integer) valueAnimator.getAnimatedValue()).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$2(ValueAnimator valueAnimator) {
        this.mCircleAlpha = ((Integer) valueAnimator.getAnimatedValue()).intValue();
    }

    private void drawSatellite(Canvas canvas, boolean z) {
        updateSateLocation(z ? this.mSatelliteRadius : 0.0d);
        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(0, getResources().getColor(R.color.satellite_color)));
        int i = this.mAzimuthAlpha;
        if (z) {
            paint.setAlpha(i);
        } else if (i == 0) {
            paint.setAlpha(this.mSatelliteAlpha);
        }
        drawGuide(canvas);
        canvas.save();
        canvas.rotate(-this.mTrueAzimuth, this.mCircleX, this.mCircleY);
        canvas.rotate(this.mAzimuthUI + 45.0f, this.mSatelliteX + (this.mSatellite.getWidth() / 2), this.mSatelliteY + (this.mSatellite.getHeight() / 2));
        drawSP(canvas);
        canvas.drawBitmap(this.mSatellite, this.mSatelliteX, this.mSatelliteY, paint);
        canvas.restore();
    }

    public void drawSP(Canvas canvas) {
        LightingColorFilter lightingColorFilter;
        Canvas canvas2;
        canvas.save();
        Paint paint = new Paint();
        if (this.mAlignedAzimuth) {
            lightingColorFilter = new LightingColorFilter(0, getResources().getColor(R.color.aux_color_sucess));
        } else {
            lightingColorFilter = new LightingColorFilter(0, getResources().getColor(R.color.aux_color));
        }
        paint.setColorFilter(lightingColorFilter);
        float circleRadius = getCircleRadius(1) - getCircleRadius(3);
        float width = (this.mSatelliteX + (this.mSatellite.getWidth() / 2)) - getCircleRadius(1);
        float width2 = ((this.mSatelliteX + (this.mSatellite.getWidth() / 2)) - getCircleRadius(1)) + circleRadius;
        float width3 = ((this.mSatelliteX + (this.mSatellite.getWidth() / 2)) + getCircleRadius(1)) - circleRadius;
        float width4 = this.mSatelliteX + (this.mSatellite.getWidth() / 2) + getCircleRadius(1);
        float height = (this.mSatelliteY + (this.mSatellite.getHeight() / 2)) - 5.0f;
        float height2 = this.mSatelliteY + (this.mSatellite.getHeight() / 2) + 5.0f;
        if (this.mAzimuthAlpha != 0 || this.mSatelliteAlpha <= 0) {
            canvas2 = canvas;
        } else {
            canvas.rotate(-45.0f, this.mSatelliteX + (this.mSatellite.getWidth() / 2), this.mSatelliteY + (this.mSatellite.getHeight() / 2));
            canvas2 = canvas;
            canvas2.drawRoundRect(width, height, width2, height2, 5.0f, 5.0f, paint);
            canvas2.drawRoundRect(width3, height, width4, height2, 5.0f, 5.0f, paint);
        }
        canvas2.restore();
    }

    public void drawGuide(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getContext().getColor(R.color.satellite_align_guide));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimension(R.dimen.satellite_azimuth_guide_stroke_width));
        paint.setPathEffect(new DashPathEffect(new float[]{20.0f, 20.0f}, 0.0f));
        if (this.mAzimuthAlpha != 0 || this.mSatelliteAlpha <= 0) {
            return;
        }
        canvas.drawRect(new RectF(this.mCircleX - getCircleRadius(1), this.mCircleY, this.mCircleX + getCircleRadius(1), this.mCircleY), paint);
        canvas.drawRect(new RectF(this.mCircleX, this.mCircleY - getCircleRadius(1), this.mCircleX, this.mCircleY + getCircleRadius(1)), paint);
    }

    private void updateSateLocation(double d) {
        if (this.mSatellite != null) {
            if (d != 0.0d) {
                int i = this.mAzimuthDelta;
                if (-15 <= i && i < 0) {
                    this.mAzimuthUI = (i * 30) / 15;
                } else if (i >= 0 && i <= 15) {
                    this.mAzimuthUI = (i * 30) / 15;
                } else if (i < 0) {
                    this.mAzimuthUI = (((i + 15) * 150) / 165) - 30;
                } else {
                    this.mAzimuthUI = (((i - 15) * 150) / 165) + 30;
                }
                this.mAzimuthUI += this.mTrueAzimuth;
            } else {
                this.mAzimuthUI = this.mTargetAzimuth;
            }
            double d2 = (this.mAzimuthUI + 270.0f) % 360.0f;
            double dCos = Math.cos(Math.toRadians(d2));
            double dSin = Math.sin(Math.toRadians(d2));
            double width = this.mSatellite.getWidth() / 2;
            double height = this.mSatellite.getHeight() / 2;
            this.mSatelliteX = (float) ((((double) this.mCircleX) + (dCos * d)) - width);
            this.mSatelliteY = (float) ((((double) this.mCircleY) + (d * dSin)) - height);
        }
    }

    private void drawScanner(Canvas canvas) {
        canvas.save();
        Paint paint = new Paint();
        LinearGradient linearGradient = new LinearGradient(this.mCircleX - getCircleRadius(1), this.mCircleY, this.mCircleX + getCircleRadius(1), this.mCircleY, getResources().getColor(R.color.satellite_scanner_lower), getResources().getColor(R.color.satellite_scanner_higher), Shader.TileMode.CLAMP);
        paint.setAntiAlias(true);
        paint.setShader(linearGradient);
        float fCurrentTimeMillis = (System.currentTimeMillis() / 8) % 360;
        RectF rectF = new RectF(this.mCircleX - (((getCircleRadius(1) * 2.0f) - getCircleStrokeWidth(1)) / 2.0f), this.mCircleY - (((getCircleRadius(1) * 2.0f) - getCircleStrokeWidth(1)) / 2.0f), this.mCircleX + (((getCircleRadius(1) * 2.0f) - getCircleStrokeWidth(1)) / 2.0f), this.mCircleY + (((getCircleRadius(1) * 2.0f) - getCircleStrokeWidth(1)) / 2.0f));
        canvas.rotate(fCurrentTimeMillis, this.mCircleX, this.mCircleY);
        canvas.drawArc(rectF, -165.0f, 75.0f, true, paint);
        Paint paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.satellite_scanner_line));
        float dimension = getResources().getDimension(R.dimen.satellite_scanner_stroke_width);
        int i = this.mCircleX;
        float f = dimension / 2.0f;
        canvas.drawRoundRect(i - f, (this.mCircleY - getCircleRadius(1)) + (getCircleStrokeWidth(1) / 2.0f), i + f, this.mCircleY, 5.0f, 5.0f, paint2);
        canvas.restore();
        invalidate();
    }

    private void drawOutCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.satellite_out_circle));
        paint.setAlpha(this.mCircleAlpha);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getCircleStrokeWidth(1));
        canvas.drawCircle(this.mCircleX, this.mCircleY, getCircleRadius(1), paint);
    }

    private void drawElevation(Canvas canvas, boolean z) {
        float userLocation = getUserLocation();
        float circleRadius = ((this.mCircleY + getCircleRadius(1)) - getCircleRadius(3)) - getCircleStrokeWidth(1);
        int i = this.mCircleY;
        if (userLocation > circleRadius) {
            userLocation = ((i + getCircleRadius(1)) - getCircleRadius(3)) - getCircleStrokeWidth(1);
        } else if (userLocation < (i - getCircleRadius(1)) + getCircleRadius(3) + getCircleStrokeWidth(1)) {
            userLocation = (this.mCircleY - getCircleRadius(1)) + getCircleRadius(3) + getCircleStrokeWidth(1);
        }
        float f = this.mCircleX;
        if (!z) {
            userLocation = this.mCircleY;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int i2 = R.color.satellite_inner_circle;
        if (z) {
            if (this.mAlignedAzimuth && !this.mAlignedElevation) {
                i2 = R.color.satellite_inner_circle_fill_aligning;
            } else if (this.mAlignedElevation) {
                i2 = R.color.satellite_inner_circle_fill_align;
            }
            paint.setColor(getResources().getColor(i2));
            if (this.mAlignedElevation || this.mAlignedAzimuthUI) {
                canvas.drawCircle(f, userLocation, getCircleRadius(3), paint);
            }
            if (this.mAlignedAzimuthUI && !this.mAlignedElevation && this.mScene != 0) {
                Bitmap bitmap = getBitmap(getContext(), R.drawable.satellite_guide_up, (int) getResources().getDimension(R.dimen.satellite_guide_width), (int) getResources().getDimension(R.dimen.satellite_guide_height));
                Bitmap bitmap2 = getBitmap(getContext(), R.drawable.satellite_guide_down, (int) getResources().getDimension(R.dimen.satellite_guide_width), (int) getResources().getDimension(R.dimen.satellite_guide_height));
                float dimension = getResources().getDimension(R.dimen.satellite_guide_space);
                if (this.mElevationDelta < 0) {
                    canvas.drawBitmap(bitmap2, f - (bitmap2.getWidth() / 2), ((getCircleRadius(3) + userLocation) - (bitmap2.getHeight() / 2)) + dimension, (Paint) null);
                } else {
                    canvas.drawBitmap(bitmap, f - (bitmap.getWidth() / 2), ((userLocation - getCircleRadius(3)) - (bitmap.getHeight() / 2)) - dimension, (Paint) null);
                }
            }
        }
        paint.setStyle(Paint.Style.STROKE);
        int i3 = R.color.satellite_inner_circle;
        if (z) {
            if (this.mAlignedAzimuthUI && !this.mAlignedElevation) {
                i3 = R.color.satellite_inner_circle_stroke_aligning;
            } else if (this.mAlignedElevation) {
                i3 = R.color.satellite_inner_circle_stroke_align;
            }
        }
        paint.setColor(getResources().getColor(i3));
        int i4 = this.mCircleAlpha;
        if (i4 > 0) {
            paint.setAlpha(i4);
        }
        paint.setStrokeWidth(getCircleStrokeWidth(3));
        canvas.drawCircle(f, userLocation, getCircleRadius(3), paint);
    }

    private float getCircleRadius(int i) {
        int i2;
        if (i == 1) {
            i2 = this.mScene != 0 ? R.dimen.satellite_out_circle_diam : R.dimen.satellite_out_circle_diam_small;
        } else if (i == 2) {
            i2 = this.mScene != 0 ? R.dimen.satellite_mid_circle_diam : R.dimen.satellite_mid_circle_diam_small;
        } else {
            if (i != 3) {
                return 0.0f;
            }
            int i3 = this.mAzimuthAlpha;
            int i4 = this.mScene;
            if (i3 != 0) {
                i2 = i4 != 0 ? R.dimen.satellite_inner_circle_diam : R.dimen.satellite_inner_circle_diam_small;
            } else {
                i2 = i4 != 0 ? R.dimen.satellite_inner_circle_diam_unstatic : R.dimen.satellite_inner_circle_diam_small_unstatic;
            }
        }
        return getResources().getDimension(i2) / 2.0f;
    }

    private float getCircleStrokeWidth(int i) {
        int i2;
        if (i == 1) {
            i2 = this.mScene != 0 ? R.dimen.satellite_out_circle_stroke_width : R.dimen.satellite_out_circle_stroke_width_small;
        } else if (i == 2) {
            i2 = this.mScene != 0 ? R.dimen.satellite_mid_circle_stroke_width : R.dimen.satellite_mid_circle_stroke_width_small;
        } else {
            if (i != 3) {
                return 0.0f;
            }
            i2 = this.mScene != 0 ? R.dimen.satellite_inner_circle_stroke_width : R.dimen.satellite_inner_circle_stroke_width_small;
        }
        return getResources().getDimension(i2);
    }

    private void drawAzimuth(Canvas canvas) {
        int color;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((getCircleRadius(1) - getCircleRadius(3)) - (getCircleStrokeWidth(1) / 2.0f));
        int i = this.mCircleX;
        float f = i;
        int i2 = this.mCircleY;
        paint.setShader(new LinearGradient(f, i2, i, i2 - getCircleRadius(1), getResources().getColor(this.mAlignedAzimuth ? R.color.satellite_azimuth_lower_success : R.color.satellite_azimuth_lower), getResources().getColor(this.mAlignedAzimuth ? R.color.satellite_azimuth_higher_success : R.color.satellite_azimuth_higher), Shader.TileMode.MIRROR));
        paint.setAlpha(this.mAzimuthAlpha);
        float circleRadius = getCircleRadius(3) + (((getCircleRadius(1) - (getCircleStrokeWidth(1) / 2.0f)) - getCircleRadius(3)) / 2.0f);
        int i3 = this.mCircleX;
        int i4 = this.mCircleY;
        canvas.drawArc(new RectF(i3 - circleRadius, i4 - circleRadius, i3 + circleRadius, i4 + circleRadius), -120.0f, 60.0f, false, paint);
        paint.setStrokeWidth(getResources().getDimension(R.dimen.satellite_azimuth_stroke_width));
        paint.setShader(null);
        if (this.mAlignedAzimuth) {
            color = getResources().getColor(R.color.satellite_inner_circle_stroke_align);
        } else {
            color = getResources().getColor(R.color.satellite_inner_circle_stroke_aligning);
        }
        paint.setColor(color);
        paint.setAlpha(this.mAzimuthAlpha);
        float circleRadius2 = (getCircleRadius(1) - (getCircleStrokeWidth(1) / 2.0f)) - (getResources().getDimension(R.dimen.satellite_azimuth_stroke_width) / 2.0f);
        int i5 = this.mCircleX;
        int i6 = this.mCircleY;
        canvas.drawArc(new RectF(i5 - circleRadius2, i6 - circleRadius2, i5 + circleRadius2, i6 + circleRadius2), -120.0f, 60.0f, false, paint);
    }

    private float getUserLocation() {
        double d;
        float f;
        float f2;
        int circleRadius = (int) getCircleRadius(3);
        float circleRadius2 = this.mCircleY - getCircleRadius(1);
        int i = this.mCircleY;
        float f3 = i - circleRadius;
        float f4 = i + circleRadius;
        float circleRadius3 = i + getCircleRadius(1);
        int i2 = this.mElevationDelta;
        if (i2 >= -20) {
            if (-20 > i2 || i2 >= 0) {
                if (i2 >= 0 && i2 <= 20) {
                    int i3 = this.mCircleY;
                    f2 = i3 + (((f4 - i3) * i2) / 20.0f);
                } else if (20 < i2) {
                    f = this.mCircleY + circleRadius + (((circleRadius3 - f4) * (i2 - 20)) / 70.0f);
                } else {
                    d = 0.0d;
                }
                return (float) d;
            }
            int i4 = this.mCircleY;
            f2 = i4 - (((f3 - i4) * i2) / 20.0f);
            d = f2;
            return (float) d;
        }
        f = (this.mCircleY - circleRadius) - (((circleRadius2 - f3) * (i2 + 20)) / 70.0f);
        d = f;
        return (float) d;
    }

    public Bitmap getBitmap(Context context, int i, int i2, int i3) {
        Drawable drawable = context.getDrawable(i);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    public void refreshAliSatDisplay(BDSatelliteDynInfo bDSatelliteDynInfo) {
        this.mInfo = bDSatelliteDynInfo;
        this.mAzimuthDelta = -bDSatelliteDynInfo.mAzimuthDelta;
        this.mElevationDelta = bDSatelliteDynInfo.mElevationDelta;
        this.mTrueAzimuth = (int) bDSatelliteDynInfo.mTrueAzimuth;
        this.mTargetAzimuth = (int) bDSatelliteDynInfo.mTargetAzimuth;
        this.mInOpenSpace = bDSatelliteDynInfo.mInOpenSpace;
        this.mScene = bDSatelliteDynInfo.mScene;
        this.mAlignedAzimuth = bDSatelliteDynInfo.isAlignedAzimuth(15);
        this.mAlignedAzimuthUI = bDSatelliteDynInfo.isAlignedAzimuth(30);
        this.mAlignedElevation = bDSatelliteDynInfo.isAlignedElevation();
        super.postInvalidateOnAnimation();
    }

    private void updateUserStyle() {
        int i = this.mScene;
        int i2 = i != 0 ? R.dimen.satellite_user_width : R.dimen.satellite_user_width_small;
        int i3 = i != 0 ? R.dimen.satellite_user_height : R.dimen.satellite_user_height_small;
        Bitmap bitmap = this.mUser;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.mUser = getBitmap(getContext(), R.drawable.user, (int) getResources().getDimension(i2), (int) getResources().getDimension(i3));
    }

    private void updateSatelliteStyle() {
        int i = this.mScene;
        int i2 = i != 0 ? R.dimen.satellite_robot_width : R.dimen.satellite_robot_width_small;
        int i3 = i != 0 ? R.dimen.satellite_robot_height : R.dimen.satellite_robot_height_small;
        Bitmap bitmap = this.mSatellite;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.mSatellite = getBitmap(getContext(), R.drawable.satellite, (int) getResources().getDimension(i2), (int) getResources().getDimension(i3));
    }
}
