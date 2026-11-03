package miuix.appcompat.internal.graphics.drawable;

import android.R;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import java.io.IOException;
import miuix.appcompat.R$styleable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes2.dex */
public class RoundStateDrawable extends Drawable {
    private static final int[] DARK_OVERLAY;
    private static final int DISABLED_ALPHA = 76;
    private static final int FLAG_DISABLED = 8;
    private static final int FLAG_HOVERED = 2;
    private static final int FLAG_PRESSED = 1;
    private static final int FLAG_SELECTED = 4;
    private static final int[] LIGHT_OVERLAY;
    public static final float RADIUS_AUTO = -1.0f;
    private static final SparseIntArray STATE_FLAG_MAP;
    private int baseColor;
    private int colorAlpha;
    private float cornerRadius;
    private int currentBaseAlpha;
    private int globalAlpha;
    private boolean isDarkMode;
    private boolean opaqueBlend;
    private int opaqueBlendBgColor;
    private int overlayAlpha;
    private final Paint overlayPaint;
    private final Paint paint;
    private final RectF rectF;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        STATE_FLAG_MAP = sparseIntArray;
        sparseIntArray.put(R.attr.state_pressed, 1);
        sparseIntArray.put(R.attr.state_hovered, 2);
        sparseIntArray.put(R.attr.state_selected, 4);
        sparseIntArray.put(R.attr.state_checked, 4);
        sparseIntArray.put(-16842910, 8);
        LIGHT_OVERLAY = buildTable(0.04f, 0.1f, 0.06f, 0.14f);
        DARK_OVERLAY = buildTable(0.08f, 0.14f, 0.12f, 0.22f);
    }

    private static int[] buildTable(float f, float f2, float f3, float f4) {
        int i = (int) (f2 * 255.0f);
        return new int[]{0, i, (int) (f * 255.0f), i, (int) (f3 * 255.0f), i, (int) (f4 * 255.0f), i};
    }

    public RoundStateDrawable() {
        this(0, false, -1.0f);
    }

    public RoundStateDrawable(int i, boolean z) {
        this(i, z, -1.0f);
    }

    public RoundStateDrawable(int i, boolean z, float f) {
        this.colorAlpha = 255;
        this.globalAlpha = 255;
        this.currentBaseAlpha = 255;
        this.opaqueBlendBgColor = -1;
        Paint paint = new Paint(1);
        this.paint = paint;
        Paint paint2 = new Paint(1);
        this.overlayPaint = paint2;
        this.rectF = new RectF();
        this.colorAlpha = 255 & (i >>> 24);
        this.baseColor = i & 16777215;
        this.isDarkMode = z;
        this.cornerRadius = f;
        Paint.Style style = Paint.Style.FILL;
        paint.setStyle(style);
        paint2.setStyle(style);
        paint2.setColor(z ? -1 : -16777216);
    }

    private float getEffectiveRadius() {
        float f = this.cornerRadius;
        return f == -1.0f ? Math.min(getBounds().width(), getBounds().height()) / 2.0f : f;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.rectF.set(getBounds());
        float effectiveRadius = getEffectiveRadius();
        int i = ((this.colorAlpha * this.currentBaseAlpha) * this.globalAlpha) / 65025;
        if (!this.opaqueBlend) {
            this.paint.setColor(this.baseColor);
            this.paint.setAlpha(i);
            canvas.drawRoundRect(this.rectF, effectiveRadius, effectiveRadius, this.paint);
        } else if (i > 0) {
            int i2 = this.opaqueBlendBgColor;
            if (i2 == -1) {
                i2 = this.isDarkMode ? -16777216 : -1;
            }
            int i3 = (i2 >> 16) & 255;
            int i4 = (i2 >> 8) & 255;
            int i5 = i2 & 255;
            int i6 = this.baseColor;
            this.paint.setColor((i5 + ((((i6 & 255) - i5) * i) / 255)) | (-16777216) | ((i3 + (((((i6 >> 16) & 255) - i3) * i) / 255)) << 16) | ((i4 + (((((i6 >> 8) & 255) - i4) * i) / 255)) << 8));
            this.paint.setAlpha(255);
            canvas.drawRoundRect(this.rectF, effectiveRadius, effectiveRadius, this.paint);
        }
        int i7 = this.overlayAlpha;
        if (i7 > 0) {
            this.overlayPaint.setAlpha((i7 * this.globalAlpha) / 255);
            canvas.drawRoundRect(this.rectF, effectiveRadius, effectiveRadius, this.overlayPaint);
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        int i = this.currentBaseAlpha;
        int i2 = this.overlayAlpha;
        int i3 = 0;
        for (int i4 : iArr) {
            i3 |= STATE_FLAG_MAP.get(i4, 0);
        }
        if ((i3 & 8) != 0) {
            this.currentBaseAlpha = DISABLED_ALPHA;
            this.overlayAlpha = 0;
        } else {
            this.currentBaseAlpha = 255;
            this.overlayAlpha = (this.isDarkMode ? DARK_OVERLAY : LIGHT_OVERLAY)[i3 & 7];
        }
        if (this.currentBaseAlpha == i && this.overlayAlpha == i2) {
            return false;
        }
        invalidateSelf();
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (this.globalAlpha != i) {
            this.globalAlpha = i;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.overlayPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setColor(int i) {
        int i2 = (i >>> 24) & 255;
        int i3 = i & 16777215;
        if (this.baseColor == i3 && this.colorAlpha == i2) {
            return;
        }
        this.colorAlpha = i2;
        this.baseColor = i3;
        invalidateSelf();
    }

    public void setCornerRadius(float f) {
        if (this.cornerRadius != f) {
            this.cornerRadius = f;
            invalidateSelf();
        }
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public void setDarkMode(boolean z) {
        if (this.isDarkMode != z) {
            this.isDarkMode = z;
            this.overlayPaint.setColor(z ? -1 : -16777216);
            invalidateSelf();
        }
    }

    public void setOpaqueBlend(boolean z) {
        if (this.opaqueBlend != z) {
            this.opaqueBlend = z;
            invalidateSelf();
        }
    }

    public void setOpaqueBlendBgColor(int i) {
        if (this.opaqueBlendBgColor != i) {
            this.opaqueBlendBgColor = i;
            if (this.opaqueBlend) {
                invalidateSelf();
            }
        }
    }

    public int getColor() {
        return this.baseColor | (this.colorAlpha << 24);
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        outline.setRoundRect(getBounds(), getEffectiveRadius());
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray typedArrayObtainAttributes;
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        if (theme != null) {
            typedArrayObtainAttributes = theme.obtainStyledAttributes(attributeSet, R$styleable.RoundStateDrawable, 0, 0);
        } else {
            typedArrayObtainAttributes = resources.obtainAttributes(attributeSet, R$styleable.RoundStateDrawable);
        }
        int color = typedArrayObtainAttributes.getColor(R$styleable.RoundStateDrawable_roundStateColor, 0);
        boolean z = typedArrayObtainAttributes.getBoolean(R$styleable.RoundStateDrawable_roundStateDarkMode, false);
        float dimension = typedArrayObtainAttributes.getDimension(R$styleable.RoundStateDrawable_roundStateCornerRadius, -1.0f);
        typedArrayObtainAttributes.recycle();
        this.colorAlpha = (color >>> 24) & 255;
        this.baseColor = 16777215 & color;
        this.isDarkMode = z;
        this.cornerRadius = dimension;
        this.overlayPaint.setColor(z ? -1 : -16777216);
    }
}
