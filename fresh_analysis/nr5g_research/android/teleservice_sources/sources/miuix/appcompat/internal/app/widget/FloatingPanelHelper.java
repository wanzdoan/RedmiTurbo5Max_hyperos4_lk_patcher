package miuix.appcompat.internal.app.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewOutlineProvider;
import miuix.appcompat.R$dimen;

/* JADX INFO: loaded from: classes2.dex */
public class FloatingPanelHelper {
    private int mBorderColor;
    private float mBorderWidth;
    private Path mClipPath;
    private boolean mEnable;
    private View mHost;
    private RectF mLayer;
    private Paint mPaint;
    private float mRadius;

    public FloatingPanelHelper(View view) {
        this.mHost = view;
        init();
    }

    private void init() {
        this.mRadius = this.mHost.getResources().getDimensionPixelSize(R$dimen.miuix_theme_radius_big);
        this.mLayer = new RectF();
        this.mClipPath = new Path();
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(-1);
        this.mPaint.setAntiAlias(true);
    }

    public void setEnable(boolean z) {
        if (this.mEnable != z) {
            this.mEnable = z;
            this.mHost.invalidate();
            this.mHost.invalidateOutline();
        }
    }

    public void setRadius(float f) {
        if (this.mRadius == f) {
            return;
        }
        this.mRadius = f;
        this.mHost.invalidateOutline();
    }

    public float getRadius() {
        return this.mRadius;
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mLayer.set(0.0f, 0.0f, i, i2);
        refreshClipPath();
    }

    private void refreshClipPath() {
        if (this.mEnable) {
            View view = this.mHost;
            int iWidth = (int) this.mLayer.width();
            int iHeight = (int) this.mLayer.height();
            RectF rectF = new RectF();
            rectF.left = view.getPaddingLeft();
            rectF.top = view.getPaddingTop();
            rectF.right = iWidth - view.getPaddingRight();
            rectF.bottom = iHeight - view.getPaddingBottom();
            this.mClipPath.reset();
            Path path = this.mClipPath;
            float f = this.mRadius;
            path.addRoundRect(rectF, new float[]{f, f, f, f, f, f, f, f}, Path.Direction.CW);
        }
    }

    public void onBorderDraw(Canvas canvas) {
        if (!this.mEnable || this.mBorderWidth == 0.0f || Color.alpha(this.mBorderColor) == 0) {
            return;
        }
        View view = this.mHost;
        int iWidth = (int) this.mLayer.width();
        int iHeight = (int) this.mLayer.height();
        RectF rectF = new RectF();
        float f = this.mBorderWidth / 2.0f;
        rectF.left = view.getPaddingLeft() + f;
        rectF.top = view.getPaddingTop() + f;
        rectF.right = (iWidth - view.getPaddingRight()) - f;
        rectF.bottom = (iHeight - view.getPaddingBottom()) - f;
        this.mPaint.reset();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.mBorderColor);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mBorderWidth);
        float f2 = this.mRadius - (f * 2.0f);
        canvas.drawRoundRect(rectF, f2, f2, this.mPaint);
    }

    public class ShapeOutlineProvider extends ViewOutlineProvider {
        private final FloatingPanelHelper mHelper;

        public ShapeOutlineProvider(FloatingPanelHelper floatingPanelHelper) {
            this.mHelper = floatingPanelHelper;
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            if (this.mHelper.isEnable() && this.mHelper.getRadius() > 0.0f) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), this.mHelper.getRadius());
            } else {
                outline.setRect(0, 0, view.getWidth(), view.getHeight());
            }
        }
    }
}
