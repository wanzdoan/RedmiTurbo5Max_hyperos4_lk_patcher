package miuix.appcompat.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$color;
import miuix.appcompat.internal.view.menu.action.EndActionMenuItemView;
import miuix.appcompat.internal.view.menu.action.EndActionMenuView;
import miuix.core.util.MiuixUIUtils;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class BadgeDrawable {
    private View mAnchor;
    private Drawable mBadgeDrawable;
    private Canvas mCanvas;
    private int mColor;
    private BadgeConfig mConfig;
    private Context mContext;
    private int mGravity;
    private boolean mHandleExtraOffset;
    private int mNumber;
    private Paint mPaint;

    public enum BadgeConfig {
        SIZE_SMALL,
        SIZE_MEDIUM,
        SIZE_LARGE,
        EXPAND_INSIDE,
        EXPAND_OUTSIDE
    }

    public BadgeDrawable(Context context) {
        this(context, 2);
    }

    public BadgeDrawable(Context context, int i) {
        this(context, i, BadgeConfig.SIZE_SMALL, 0);
    }

    public BadgeDrawable(Context context, int i, BadgeConfig badgeConfig, int i2) {
        this.mHandleExtraOffset = false;
        setGravity(i);
        this.mContext = context;
        this.mConfig = badgeConfig;
        this.mNumber = i2;
        this.mColor = context.getResources().getColor(R$color.miuix_appcompat_badge_drawable_color);
        this.mBadgeDrawable = getBadgeDrawable(badgeConfig);
    }

    public void setGravity(int i) {
        if (i >= 0 && i <= 3) {
            this.mGravity = i;
        } else {
            Log.d("BadgeDrawable", "set invalid gravity value.");
            this.mGravity = 2;
        }
    }

    public void attachBadgeDrawable(View view) {
        attachBadgeDrawable(view, this.mGravity);
    }

    public void attachBadgeDrawable(View view, int i) {
        setGravity(i);
        Rect badgeRect = getBadgeRect(view);
        if (badgeRect == null) {
            Log.d("BadgeDrawable", "attach failed.");
            return;
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.setClipChildren(false);
            viewGroup.setClipToPadding(false);
            ViewParent parent = viewGroup.getParent();
            if ((viewGroup instanceof EndActionMenuItemView) && (parent instanceof EndActionMenuView)) {
                EndActionMenuView endActionMenuView = (EndActionMenuView) parent;
                endActionMenuView.setClipChildren(false);
                endActionMenuView.setClipToPadding(false);
            }
        }
        this.mBadgeDrawable.setBounds(badgeRect);
        view.getOverlay().add(this.mBadgeDrawable);
        this.mAnchor = view;
    }

    public void detachBadgeDrawable() {
        View view = this.mAnchor;
        if (view != null) {
            view.getOverlay().clear();
        }
    }

    public void setHandleExtraOffset(boolean z) {
        if (this.mHandleExtraOffset != z) {
            this.mHandleExtraOffset = z;
        }
    }

    public void setAlpha(int i) {
        Drawable drawable = this.mBadgeDrawable;
        if (drawable != null) {
            drawable.setAlpha(i);
            this.mBadgeDrawable.invalidateSelf();
        }
    }

    public void animateAlpha(float f) {
        Drawable drawable = this.mBadgeDrawable;
        if (drawable == null || this.mAnchor == null) {
            return;
        }
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(drawable.getAlpha(), (int) (f * 255.0f));
        valueAnimatorOfInt.setDuration(200L);
        valueAnimatorOfInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: miuix.appcompat.widget.BadgeDrawable$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                BadgeDrawable.$r8$lambda$AuusDJsCDW39gfnH5zQ6jfVn0BI(this.f$0, valueAnimator);
            }
        });
        valueAnimatorOfInt.start();
    }

    public static /* synthetic */ void $r8$lambda$AuusDJsCDW39gfnH5zQ6jfVn0BI(BadgeDrawable badgeDrawable, ValueAnimator valueAnimator) {
        badgeDrawable.getClass();
        badgeDrawable.mBadgeDrawable.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
        badgeDrawable.mBadgeDrawable.invalidateSelf();
    }

    private Drawable getBadgeDrawable() {
        return AttributeResolver.resolveDrawable(this.mContext, R$attr.actionBarTabBadgeIcon);
    }

    private Drawable getBadgeDrawable(int i, int i2, float f, int i3) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setSize(i, i2);
        gradientDrawable.setCornerRadius(f);
        gradientDrawable.setColor(i3);
        return gradientDrawable;
    }

    /* JADX INFO: renamed from: miuix.appcompat.widget.BadgeDrawable$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig;

        static {
            int[] iArr = new int[BadgeConfig.values().length];
            $SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig = iArr;
            try {
                iArr[BadgeConfig.EXPAND_INSIDE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig[BadgeConfig.EXPAND_OUTSIDE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig[BadgeConfig.SIZE_MEDIUM.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig[BadgeConfig.SIZE_LARGE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig[BadgeConfig.SIZE_SMALL.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    private Drawable getBadgeDrawable(BadgeConfig badgeConfig) {
        int i = AnonymousClass1.$SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig[badgeConfig.ordinal()];
        if (i == 1) {
            return drawTextOnBadge(4, getBadgeDrawable(normalizeWidth(4), MiuixUIUtils.dp2px(this.mContext, 24.0f), 2.1474836E9f, this.mColor));
        }
        if (i == 2) {
            return drawTextOnBadge(3, getBadgeDrawable(normalizeWidth(3), MiuixUIUtils.dp2px(this.mContext, 16.0f), 2.1474836E9f, this.mColor));
        }
        if (i == 3) {
            int iDp2px = MiuixUIUtils.dp2px(this.mContext, 8.0f);
            return getBadgeDrawable(iDp2px, iDp2px, 2.1474836E9f, this.mColor);
        }
        if (i == 4) {
            int iDp2px2 = MiuixUIUtils.dp2px(this.mContext, 12.0f);
            return getBadgeDrawable(iDp2px2, iDp2px2, 2.1474836E9f, this.mColor);
        }
        return getBadgeDrawable();
    }

    private Drawable drawTextOnBadge(int i, Drawable drawable) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        this.mCanvas = canvas;
        drawable.setBounds(0, 0, canvas.getWidth(), this.mCanvas.getHeight());
        drawable.draw(this.mCanvas);
        initPaint(i);
        this.mCanvas.drawText(normalizeText(), this.mCanvas.getWidth() / 2.0f, (this.mCanvas.getHeight() / 2.0f) - ((this.mPaint.descent() + this.mPaint.ascent()) / 2.0f), this.mPaint);
        return new BitmapDrawable(this.mContext.getResources(), bitmapCreateBitmap);
    }

    private void initPaint(int i) {
        if (this.mPaint == null) {
            Paint paint = new Paint();
            this.mPaint = paint;
            paint.setColor(-1);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setTextAlign(Paint.Align.CENTER);
        }
        this.mPaint.setTextSize(i == 3 ? MiuixUIUtils.dp2px(this.mContext, 12.0f) : MiuixUIUtils.dp2px(this.mContext, 14.0f));
    }

    private int normalizeWidth(int i) {
        int i2 = this.mNumber;
        if (i2 >= 0 && i2 < 10) {
            Context context = this.mContext;
            if (i == 4) {
                return MiuixUIUtils.dp2px(context, 24.0f);
            }
            return MiuixUIUtils.dp2px(context, 16.0f);
        }
        if (i2 >= 10 && i2 < 100) {
            Context context2 = this.mContext;
            if (i == 4) {
                return MiuixUIUtils.dp2px(context2, 29.0f);
            }
            return MiuixUIUtils.dp2px(context2, 21.0f);
        }
        if (i2 < 100) {
            return 0;
        }
        Context context3 = this.mContext;
        if (i == 4) {
            return MiuixUIUtils.dp2px(context3, 38.0f);
        }
        return MiuixUIUtils.dp2px(context3, 31.0f);
    }

    private String normalizeText() {
        int i = this.mNumber;
        if (i > 0 && i < 100) {
            return Integer.toString(i);
        }
        return "99+";
    }

    private boolean handleExtraOffset(View view) {
        View view2;
        return this.mHandleExtraOffset && (view2 = (View) view.getParent()) != null && view.getTop() == 0 && view.getLeft() == 0 && view.getBottom() == view2.getMeasuredHeight() && view.getRight() == view2.getMeasuredWidth();
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0075  */
    /* JADX WARN: Code duplicated, block: B:31:0x008d  */
    /* JADX WARN: Code duplicated, block: B:38:0x00a2 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:39:0x00a4 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:40:0x00a6  */
    /* JADX WARN: Code duplicated, block: B:42:0x00ab  */
    /* JADX WARN: Code duplicated, block: B:44:0x00af  */
    /* JADX WARN: Code duplicated, block: B:46:0x00b4  */
    /* JADX WARN: Code duplicated, block: B:49:0x00c3 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:50:0x00c5  */
    /* JADX WARN: Code duplicated, block: B:51:0x00c8  */
    /* JADX WARN: Code duplicated, block: B:53:0x00cd  */
    /* JADX WARN: Code duplicated, block: B:55:0x00d0  */
    /* JADX WARN: Code duplicated, block: B:57:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:59:0x00dd  */
    /* JADX WARN: Code duplicated, block: B:61:0x00e4  */
    /* JADX WARN: Code duplicated, block: B:63:0x00ea  */
    /* JADX WARN: Code duplicated, block: B:64:0x00ed  */
    /* JADX WARN: Code duplicated, block: B:66:0x00f2  */
    /* JADX WARN: Code duplicated, block: B:69:0x00f7  */
    /* JADX WARN: Code duplicated, block: B:71:0x00fc  */
    /* JADX WARN: Code duplicated, block: B:74:0x0104  */
    /* JADX WARN: Code duplicated, block: B:76:0x0109  */
    /* JADX WARN: Code duplicated, block: B:78:0x0112  */
    /* JADX WARN: Code duplicated, block: B:80:0x0119  */
    /* JADX WARN: Code duplicated, block: B:81:0x011e  */
    private Rect getBadgeRect(View view) {
        int iDp2px;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        if (view == null) {
            Log.d("BadgeDrawable", "can not attach badge on a null object.");
            return null;
        }
        if (this.mBadgeDrawable == null) {
            Log.d("BadgeDrawable", "can not find badge drawable resource.");
            return null;
        }
        Rect rect = new Rect();
        int intrinsicWidth = this.mBadgeDrawable.getCurrent().getIntrinsicWidth();
        int intrinsicHeight = this.mBadgeDrawable.getCurrent().getIntrinsicHeight();
        view.getDrawingRect(rect);
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(view);
        boolean zHandleExtraOffset = handleExtraOffset(view);
        BadgeConfig badgeConfig = this.mConfig;
        if (badgeConfig == BadgeConfig.EXPAND_INSIDE || badgeConfig == BadgeConfig.EXPAND_OUTSIDE) {
            iDp2px = MiuixUIUtils.dp2px(this.mContext, 10.0f);
        } else {
            iDp2px = MiuixUIUtils.dp2px(this.mContext, 2.0f);
        }
        int iDp2px2 = MiuixUIUtils.dp2px(this.mContext, 6.0f);
        int i16 = this.mGravity;
        int i17 = 0;
        if (i16 == 0) {
            if ((zIsLayoutRtl && i16 == 0) || (zIsLayoutRtl && i16 == 2)) {
            }
            i = AnonymousClass1.$SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig[this.mConfig.ordinal()];
            if (i != 1) {
                i2 = rect.top - iDp2px;
                i3 = i2 + intrinsicHeight;
                if (i17 != 0) {
                    i7 = rect.left - iDp2px;
                    i8 = i7 + intrinsicWidth;
                    int i18 = i7;
                    i17 = i2;
                    i5 = i3;
                    i6 = i18;
                } else {
                    int i19 = rect.right + iDp2px;
                    int i20 = i19 - intrinsicWidth;
                    i4 = i19;
                    i17 = i2;
                    i5 = i3;
                    i6 = i20;
                    i8 = i4;
                }
            } else if (i != 2) {
                if (i != 3) {
                    i14 = rect.top;
                    if (i != 5) {
                        i2 = i14 - iDp2px;
                        if (i17 != 0) {
                            i13 = rect.left - iDp2px;
                        } else {
                            i13 = (rect.right + iDp2px) - intrinsicWidth;
                        }
                    } else {
                        if (i17 != 0) {
                            i15 = rect.left;
                        } else {
                            i15 = rect.right - intrinsicWidth;
                        }
                        if (zHandleExtraOffset) {
                            i14 += iDp2px2;
                            if (i17 != 0) {
                                i15 += iDp2px2;
                            } else {
                                i15 -= iDp2px2;
                            }
                        }
                        i17 = i14;
                        i8 = i15 + intrinsicWidth;
                        i6 = i15;
                        i5 = i17 + intrinsicHeight;
                    }
                } else {
                    i2 = rect.top - (intrinsicHeight / 2);
                    if (i17 != 0) {
                        i12 = rect.left;
                    } else {
                        i12 = rect.right;
                    }
                    i13 = i12 - (intrinsicWidth / 2);
                }
                i7 = i13;
                i3 = i2 + intrinsicHeight;
                i8 = i7 + intrinsicWidth;
                int i110 = i7;
                i17 = i2;
                i5 = i3;
                i6 = i110;
            } else {
                i5 = rect.top + iDp2px;
                if (zHandleExtraOffset) {
                    i5 += iDp2px2;
                }
                i9 = i5 - intrinsicHeight;
                if (i17 != 0) {
                    i11 = rect.left + iDp2px;
                    if (zHandleExtraOffset) {
                        i11 += iDp2px2;
                    }
                    int i21 = i11;
                    i6 = i21 - intrinsicWidth;
                    i4 = i21;
                    i17 = i9;
                    i8 = i4;
                } else {
                    i10 = rect.right - iDp2px;
                    if (zHandleExtraOffset) {
                        i10 -= iDp2px2;
                    }
                    int i22 = i10;
                    i8 = i22 + intrinsicWidth;
                    i6 = i22;
                    i17 = i9;
                }
            }
        } else if (i16 == 1) {
            i17 = rect.bottom - intrinsicHeight;
            i5 = i17 + intrinsicHeight;
            i6 = ((zIsLayoutRtl && i16 == 1) || (zIsLayoutRtl && i16 == 3)) ? rect.left : rect.right - intrinsicWidth;
            i8 = i6 + intrinsicWidth;
        } else if (i16 == 2) {
            i17 = zIsLayoutRtl ? 1 : 1;
            i = AnonymousClass1.$SwitchMap$miuix$appcompat$widget$BadgeDrawable$BadgeConfig[this.mConfig.ordinal()];
            if (i != 1) {
                i2 = rect.top - iDp2px;
                i3 = i2 + intrinsicHeight;
                if (i17 != 0) {
                    i7 = rect.left - iDp2px;
                    i8 = i7 + intrinsicWidth;
                    int i111 = i7;
                    i17 = i2;
                    i5 = i3;
                    i6 = i111;
                } else {
                    int i112 = rect.right + iDp2px;
                    int i23 = i112 - intrinsicWidth;
                    i4 = i112;
                    i17 = i2;
                    i5 = i3;
                    i6 = i23;
                    i8 = i4;
                }
            } else if (i != 2) {
                if (i != 3) {
                    i14 = rect.top;
                    if (i != 5) {
                        i2 = i14 - iDp2px;
                        if (i17 != 0) {
                            i13 = rect.left - iDp2px;
                        } else {
                            i13 = (rect.right + iDp2px) - intrinsicWidth;
                        }
                    } else {
                        if (i17 != 0) {
                            i15 = rect.left;
                        } else {
                            i15 = rect.right - intrinsicWidth;
                        }
                        if (zHandleExtraOffset) {
                            i14 += iDp2px2;
                            if (i17 != 0) {
                                i15 += iDp2px2;
                            } else {
                                i15 -= iDp2px2;
                            }
                        }
                        i17 = i14;
                        i8 = i15 + intrinsicWidth;
                        i6 = i15;
                        i5 = i17 + intrinsicHeight;
                    }
                } else {
                    i2 = rect.top - (intrinsicHeight / 2);
                    if (i17 != 0) {
                        i12 = rect.left;
                    } else {
                        i12 = rect.right;
                    }
                    i13 = i12 - (intrinsicWidth / 2);
                }
                i7 = i13;
                i3 = i2 + intrinsicHeight;
                i8 = i7 + intrinsicWidth;
                int i113 = i7;
                i17 = i2;
                i5 = i3;
                i6 = i113;
            } else {
                i5 = rect.top + iDp2px;
                if (zHandleExtraOffset) {
                    i5 += iDp2px2;
                }
                i9 = i5 - intrinsicHeight;
                if (i17 != 0) {
                    i11 = rect.left + iDp2px;
                    if (zHandleExtraOffset) {
                        i11 += iDp2px2;
                    }
                    int i24 = i11;
                    i6 = i24 - intrinsicWidth;
                    i4 = i24;
                    i17 = i9;
                    i8 = i4;
                } else {
                    i10 = rect.right - iDp2px;
                    if (zHandleExtraOffset) {
                        i10 -= iDp2px2;
                    }
                    int i25 = i10;
                    i8 = i25 + intrinsicWidth;
                    i6 = i25;
                    i17 = i9;
                }
            }
        } else if (i16 != 3) {
            Log.d("BadgeDrawable", "invalid gravity value.");
            i5 = 0;
            i6 = 0;
            i8 = 0;
        } else {
            i17 = rect.bottom - intrinsicHeight;
            i5 = i17 + intrinsicHeight;
            if (zIsLayoutRtl) {
            }
            i8 = i6 + intrinsicWidth;
        }
        rect.top = i17;
        rect.left = i6;
        rect.bottom = i5;
        rect.right = i8;
        return rect;
    }
}
