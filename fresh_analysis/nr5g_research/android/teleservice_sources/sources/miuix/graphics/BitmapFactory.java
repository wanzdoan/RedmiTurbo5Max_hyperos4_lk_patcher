package miuix.graphics;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BitmapFactory extends android.graphics.BitmapFactory {
    private static final String[] APPELLATION_SUFFIX;
    private static final Pattern ASIALANGPATTERN;
    static RenderScript sRsContext;
    private static final Paint sSrcInPaint;
    static Object sLockForRsContext = new Object();
    private static byte[] PNG_HEAD_FORMAT = {-119, 80, 78, 71, 13, 10, 26, 10};
    private static final ThreadLocal sCanvasCache = new ThreadLocal();

    static {
        Paint paint = new Paint(1);
        sSrcInPaint = paint;
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ASIALANGPATTERN = Pattern.compile("[\u3100-ㄭㆠ-ㆺ一-鿌㐀-䶵豈-龎⼀-⿕⺀-⻳㇀-㇣ᄀ-ᇿꥠ-ꥼힰ-ퟻㄱ-ㆎ가-힣\u3040-ゟ゠-ヿㇰ-ㇿ㆐-㆟ꀀ-ꒌ꒐-꓆]");
        APPELLATION_SUFFIX = new String[]{"老师", "先生", "老板", "仔", "手机", "叔", "阿姨", "宅", "伯", "伯母", "伯父", "哥", "姐", "弟", "妹", "舅", "姑", "父", "主任", "经理", "工作", "同事", "律师", "司机", "师傅", "师父", "爷", "奶", "中介", "董", "总", "太太", "保姆", "某", "秘书", "处长", "局长", "班长", "兄", "助理"};
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap.getWidth() == i && bitmap.getHeight() == i2) {
            return bitmap;
        }
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        if (bitmap.getConfig() != null) {
            config = bitmap.getConfig();
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i, i2, config);
        scaleBitmap(bitmap, bitmapCreateBitmap);
        return bitmapCreateBitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap2 == null) {
            return null;
        }
        if (bitmap.getWidth() == bitmap2.getWidth() && bitmap.getHeight() == bitmap2.getHeight()) {
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()), paint);
        return bitmap2;
    }

    private static Bitmap copyToEmpty(Bitmap bitmap) {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        if (bitmap.getConfig() != null) {
            config = bitmap.getConfig();
        }
        return Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
    }

    public static Bitmap fastBlur(Context context, Bitmap bitmap, int i) {
        Bitmap bitmapCopyToEmpty = copyToEmpty(bitmap);
        fastBlur(context, bitmap, bitmapCopyToEmpty, i);
        return bitmapCopyToEmpty;
    }

    public static Bitmap fastBlur(Context context, Bitmap bitmap, Bitmap bitmap2, int i) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap2 == null || bitmap.getWidth() != bitmap2.getWidth() || bitmap.getHeight() != bitmap2.getHeight()) {
            bitmap2 = copyToEmpty(bitmap);
        }
        fastblur_v17(context, bitmap, bitmap2, i);
        return bitmap2;
    }

    private static Bitmap fastblur_v17(Context context, Bitmap bitmap, Bitmap bitmap2, int i) {
        int i2 = 1;
        while (i > 25) {
            i2 *= 2;
            i /= 2;
        }
        Bitmap bitmapScaleBitmap = i2 == 1 ? bitmap : scaleBitmap(bitmap, Math.max(bitmap.getWidth() / i2, 1), Math.max(bitmap.getHeight() / i2, 1));
        if (context.getApplicationContext() == null) {
            context = new ContextWrapper(context) { // from class: miuix.graphics.BitmapFactory.1
                @Override // android.content.ContextWrapper, android.content.Context
                public Context getApplicationContext() {
                    return this;
                }
            };
        }
        try {
            Object[] enumConstants = context.getClassLoader().loadClass("android.graphics.Bitmap$Config").getEnumConstants();
            if (enumConstants != null) {
                for (Object obj : enumConstants) {
                    Enum r6 = (Enum) obj;
                    if ("RGBA_F16".equals(r6.name()) && bitmapScaleBitmap.getConfig() == r6) {
                        bitmapScaleBitmap = transferF16ToARGB(bitmapScaleBitmap);
                        break;
                    }
                }
            }
        } catch (Exception unused) {
        }
        synchronized (sLockForRsContext) {
            try {
                if (sRsContext == null) {
                    sRsContext = RenderScript.create(context);
                }
                Bitmap bitmap3 = i2 == 1 ? bitmap2 : bitmapScaleBitmap;
                if (bitmapScaleBitmap.getRowBytes() != bitmap3.getRowBytes()) {
                    bitmapScaleBitmap = bitmapScaleBitmap.copy(Bitmap.Config.ARGB_8888, true);
                }
                Allocation allocationCreateFromBitmap = Allocation.createFromBitmap(sRsContext, bitmapScaleBitmap);
                Allocation allocationCreateTyped = Allocation.createTyped(sRsContext, allocationCreateFromBitmap.getType());
                RenderScript renderScript = sRsContext;
                ScriptIntrinsicBlur scriptIntrinsicBlurCreate = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
                scriptIntrinsicBlurCreate.setRadius(i);
                scriptIntrinsicBlurCreate.setInput(allocationCreateFromBitmap);
                scriptIntrinsicBlurCreate.forEach(allocationCreateTyped);
                allocationCreateTyped.copyTo(bitmap3);
                if (bitmap3 != bitmap2) {
                    scaleBitmap(bitmap3, bitmap2);
                }
                if (bitmapScaleBitmap != bitmap) {
                    bitmapScaleBitmap.recycle();
                }
                if (bitmap3 != bitmap2) {
                    bitmap3.recycle();
                }
                allocationCreateFromBitmap.destroy();
                allocationCreateTyped.destroy();
                scriptIntrinsicBlurCreate.destroy();
            } catch (Throwable th) {
                throw th;
            }
        }
        return bitmap2;
    }

    private static Bitmap transferF16ToARGB(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == 0 || height == 0) {
            return bitmap;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint();
        paint.setFlags(3);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap, float f) {
        return getRoundBitmap(bitmap, f, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap, float f, Bitmap.Config config) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return bitmapCreateBitmap;
    }
}
