package com.android.phone.beidou;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import com.android.phone.R;

/* JADX INFO: loaded from: classes.dex */
public class RotateView extends View {
    private final int ANGLE_BALL_FIRST_LOCATION;
    private final int BIG_CIRCLE_COLOR;
    private final int DIMEN_BIG_CIRCLE_STROKE_WIDTH;
    private final int FIRST_DELAY_UPDATE_TIME;
    private final int FOOT_PRINT_COLOR;
    private final int INDICATOR_BALL_COLOR;
    private final int LEAST_TILT_ANGLE;
    private final int LIMITED_ANGULAR_DIFF_ANGLE_UPDATE;
    private final int LIMITED_TIME_INTERVAL_ANGLE_UPDATE;
    private final RectF OVAL;
    private float circleRadius;
    private int dimenSpaceOfCircles;
    private int indicatorCircleRadius;
    private float largeCircleRadius;
    private Paint mBigCirclePaint;
    private float mCircleX;
    private float mCircleY;
    private long mFirstUpdateTime;
    private Paint mFootprintPaint;
    private Paint mIndicatorBallPaint;
    private long mLastAddAngleTime;
    private int mLastSituatedAngle;
    private final boolean[] mRolledAngles;
    public int mRolledAnglesCount;
    private float mVY;
    private float mVZ;
    private int viewHeight;
    private int viewWidth;

    public RotateView(Context context) {
        this(context, null);
    }

    public RotateView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RotateView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.FIRST_DELAY_UPDATE_TIME = 1000;
        this.DIMEN_BIG_CIRCLE_STROKE_WIDTH = 10;
        this.ANGLE_BALL_FIRST_LOCATION = 60;
        this.LEAST_TILT_ANGLE = 25;
        this.LIMITED_TIME_INTERVAL_ANGLE_UPDATE = 100;
        this.LIMITED_ANGULAR_DIFF_ANGLE_UPDATE = 20;
        this.BIG_CIRCLE_COLOR = getResources().getColor(R.color.calibrate_big_circle_grey_color);
        this.FOOT_PRINT_COLOR = getResources().getColor(R.color.big_circle_foot_print_color);
        this.INDICATOR_BALL_COLOR = getResources().getColor(R.color.indicator_ball_color);
        updateRotateView(context);
        this.mRolledAngles = new boolean[360];
        this.OVAL = new RectF();
        this.mVZ = (float) Math.cos(1.0471975511965976d);
        this.mVY = (float) Math.sin(1.0471975511965976d);
        initPaint();
        clearRolledAngels();
    }

    public void updateRotateView(Context context) {
        this.indicatorCircleRadius = getResources().getDimensionPixelSize(R.dimen.indicator_circle_radius);
        this.circleRadius = getResources().getDimensionPixelSize(R.dimen.circle_radius);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.dimen_space_of_circles);
        this.dimenSpaceOfCircles = dimensionPixelSize;
        int iCeil = ((int) Math.ceil((this.circleRadius + this.indicatorCircleRadius + dimensionPixelSize) * 2.0f)) + 10;
        this.viewWidth = iCeil;
        this.viewHeight = iCeil;
        this.largeCircleRadius = this.circleRadius + this.indicatorCircleRadius + this.dimenSpaceOfCircles;
    }

    private void initPaint() {
        Paint paint = new Paint();
        this.mBigCirclePaint = paint;
        paint.setAntiAlias(true);
        this.mBigCirclePaint.setColor(this.BIG_CIRCLE_COLOR);
        Paint paint2 = this.mBigCirclePaint;
        Paint.Style style = Paint.Style.STROKE;
        paint2.setStyle(style);
        this.mBigCirclePaint.setStrokeWidth(10.0f);
        Paint paint3 = new Paint();
        this.mFootprintPaint = paint3;
        paint3.setAntiAlias(true);
        this.mFootprintPaint.setColor(this.FOOT_PRINT_COLOR);
        this.mFootprintPaint.setStyle(style);
        this.mFootprintPaint.setStrokeWidth(10.0f);
        Paint paint4 = new Paint();
        this.mIndicatorBallPaint = paint4;
        paint4.setAntiAlias(true);
        this.mIndicatorBallPaint.setColor(this.INDICATOR_BALL_COLOR);
        this.mIndicatorBallPaint.setStyle(Paint.Style.FILL);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(getViewSize(i, this.viewWidth), getViewSize(i2, this.viewHeight));
        this.mCircleY = getMeasuredHeight() / 2;
        float measuredWidth = getMeasuredWidth() / 2;
        this.mCircleX = measuredWidth;
        RectF rectF = this.OVAL;
        float f = this.largeCircleRadius;
        float f2 = this.mCircleY;
        rectF.set(measuredWidth - f, f2 - f, measuredWidth + f, f2 + f);
    }

    private int getViewSize(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        return (mode == Integer.MIN_VALUE || mode == 0) ? i2 : View.MeasureSpec.getSize(i);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBigCircle(canvas);
        drawIndicatorBall(canvas);
        drawFootprints(canvas);
    }

    private void drawBigCircle(Canvas canvas) {
        canvas.drawCircle(this.mCircleX, this.mCircleY, this.largeCircleRadius, this.mBigCirclePaint);
    }

    private void drawIndicatorBall(Canvas canvas) {
        float f = this.circleRadius;
        canvas.drawCircle((this.mVZ * f) + this.mCircleX, (this.mVY * f) + this.mCircleY, this.indicatorCircleRadius, this.mIndicatorBallPaint);
    }

    private void drawFootprints(Canvas canvas) {
        int i = -1;
        while (i < 360) {
            do {
                i++;
                if (i >= 360) {
                    break;
                }
            } while (!this.mRolledAngles[i]);
            if (i == 360) {
                return;
            }
            int i2 = i;
            do {
                i2++;
                if (i2 >= 360) {
                    break;
                }
            } while (this.mRolledAngles[i2]);
            canvas.drawArc(this.OVAL, i, i2 - i, false, this.mFootprintPaint);
            i = i2;
        }
    }

    /* JADX WARN: Code duplicated, block: B:17:0x004a  */
    /* JADX WARN: Code duplicated, block: B:20:0x004e A[LOOP:2: B:18:0x004b->B:20:0x004e, LOOP_END] */
    private void addToFootprintAngles(int i) {
        int i2;
        if (saveAngle(i)) {
            long jCurrentTimeMillis = System.currentTimeMillis() - this.mLastAddAngleTime;
            int i3 = i - this.mLastSituatedAngle;
            if (jCurrentTimeMillis < 100) {
                int iAbs = Math.abs(i3);
                int i4 = 1;
                if (iAbs < 20) {
                    int iMin = Math.min(i, this.mLastSituatedAngle);
                    while (i4 < iAbs) {
                        saveAngle((iMin + i4) % 360);
                        i4++;
                    }
                } else if (iAbs > 340) {
                    int iMax = Math.max(i, this.mLastSituatedAngle);
                    while (i4 < 360 - iAbs) {
                        saveAngle((iMax + i4) % 360);
                        i4++;
                    }
                } else {
                    for (i2 = 0; i2 < 5; i2++) {
                        saveAngle((i + i2) % 360);
                    }
                }
            } else {
                while (i2 < 5) {
                    saveAngle((i + i2) % 360);
                }
            }
            this.mLastSituatedAngle = i;
            this.mLastAddAngleTime = System.currentTimeMillis();
        }
    }

    private int calculateAngle() {
        int i;
        float f = this.mVZ;
        float f2 = this.mVY;
        if (f == 0.0f) {
            i = f2 > 0.0f ? 90 : 270;
        } else {
            int iAtan = (int) (Math.atan(f2 / f) * 57.29577951308232d);
            if (this.mVZ < 0.0f) {
                i = iAtan + 180;
            } else {
                i = this.mVY < 0.0f ? iAtan + 360 : iAtan;
            }
        }
        return i % 360;
    }

    private boolean saveAngle(int i) {
        boolean[] zArr = this.mRolledAngles;
        if (zArr[i]) {
            return false;
        }
        zArr[i] = true;
        this.mRolledAnglesCount++;
        return true;
    }

    public void update(float f, float f2) {
        if (this.mFirstUpdateTime == 0) {
            this.mFirstUpdateTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - this.mFirstUpdateTime > 1000) {
            float f3 = -Math.min(Math.max(f, -90.0f), 90.0f);
            float f4 = -Math.min(Math.max(f2, -90.0f), 90.0f);
            double dAbs = f3 / Math.abs(90.0f);
            double dAbs2 = f4 / Math.abs(90.0f);
            double dSqrt = Math.sqrt((dAbs * dAbs) + (dAbs2 * dAbs2));
            if (dSqrt == 0.0d) {
                this.mVY = 0.0f;
                this.mVZ = 1.0f;
            } else {
                this.mVY = (float) (dAbs / dSqrt);
                this.mVZ = (float) (dAbs2 / dSqrt);
            }
            if (Math.abs(f3) >= 25.0f || Math.abs(f4) >= 25.0f) {
                addToFootprintAngles(calculateAngle());
            }
            if ((this.mRolledAnglesCount * 100) / 360 == 100 && getVisibility() == 0) {
                Toast.makeText(getContext(), R.string.calibrate_rotate_again, 1).show();
                clearRolledAngels();
            }
        }
        invalidate();
    }

    private void clearRolledAngels() {
        for (int i = 0; i < 360; i++) {
            this.mRolledAngles[i] = false;
        }
        this.mRolledAnglesCount = 0;
    }

    public void reset() {
        this.mFirstUpdateTime = 0L;
        this.mLastAddAngleTime = 0L;
        this.mVZ = (float) Math.cos(1.0471975511965976d);
        this.mVY = (float) Math.sin(1.0471975511965976d);
        clearRolledAngels();
    }
}
