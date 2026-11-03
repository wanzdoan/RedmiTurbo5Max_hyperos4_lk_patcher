package miuix.animation.utils;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import android.animation.ArgbEvaluator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import miuix.animation.IAnimTarget;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;

/* JADX INFO: loaded from: classes2.dex */
public class CommonUtils {
    private static final Class[] BUILT_IN;
    public static final String D_TAG = "folme_design";
    public static final String TAG = "miuix_anim";
    public static final int UNIT_MILLIS_SECOND = 1000000000;
    public static final int UNIT_SECOND = 1000;
    public static final ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    private static Method sGetPropMethod;
    private static ArrayMap sTimeStatArray;
    private static float sTouchSlop;

    public static boolean hasFlags(long j, long j2) {
        return (j & j2) != 0;
    }

    static {
        try {
            sGetPropMethod = Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class);
        } catch (Exception unused) {
        }
        BUILT_IN = new Class[]{String.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Short.TYPE, Short.class, Float.TYPE, Float.class, Double.TYPE, Double.class};
        sTimeStatArray = new ArrayMap();
    }

    private CommonUtils() {
    }

    class OnPreDrawTask implements ViewTreeObserver.OnPreDrawListener {
        Runnable mTask;
        WeakReference mView;

        OnPreDrawTask(Runnable runnable) {
            this.mTask = runnable;
        }

        public void start(View view) {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            this.mView = new WeakReference(view);
            viewTreeObserver.addOnPreDrawListener(this);
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            View view = (View) this.mView.get();
            if (view != null) {
                Runnable runnable = this.mTask;
                if (runnable != null) {
                    runnable.run();
                }
                view.getViewTreeObserver().removeOnPreDrawListener(this);
            }
            this.mTask = null;
            return true;
        }
    }

    public static void runOnPreDraw(View view, Runnable runnable) {
        if (view == null) {
            return;
        }
        new OnPreDrawTask(runnable).start(view);
    }

    public static int[] toIntArray(float[] fArr) {
        int[] iArr = new int[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            iArr[i] = (int) fArr[i];
        }
        return iArr;
    }

    public static StringBuilder setToString(Set set) {
        StringBuilder sb = new StringBuilder();
        if (set == null) {
            sb.append("null");
            return sb;
        }
        sb.append('{');
        Object[] array = set.toArray();
        for (int i = 0; i < set.size(); i++) {
            sb.append('\n');
            sb.append(i);
            sb.append('.');
            sb.append(array[i]);
        }
        sb.append("\n}");
        return sb;
    }

    public static String mapsToString(Map[] mapArr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < mapArr.length; i++) {
            sb.append('\n');
            sb.append(i);
            sb.append('.');
            sb.append((CharSequence) mapToString(mapArr[i], "    "));
        }
        sb.append("\n]");
        return sb.toString();
    }

    public static StringBuilder mapToString(Map map, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        if (map != null && map.size() > 0) {
            for (Map.Entry entry : map.entrySet()) {
                sb.append('\n');
                sb.append(str);
                sb.append(entry.getKey());
                sb.append('=');
                sb.append(entry.getValue());
            }
            sb.append('\n');
        }
        sb.append("\t}");
        return sb;
    }

    @SafeVarargs
    public static Object[] mergeArray(Object[] objArr, Object... objArr2) {
        if (objArr == null) {
            return objArr2;
        }
        if (objArr2 == null) {
            return objArr;
        }
        Object objNewInstance = Array.newInstance(objArr.getClass().getComponentType(), objArr.length + objArr2.length);
        System.arraycopy(objArr, 0, objNewInstance, 0, objArr.length);
        System.arraycopy(objArr2, 0, objNewInstance, objArr.length, objArr2.length);
        return (Object[]) objNewInstance;
    }

    public static int toIntValue(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).intValue();
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("toFloat failed, value is ", obj);
        return 0;
    }

    public static float toFloatValue(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).floatValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).floatValue();
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("toFloat failed, value is ", obj);
        return 0.0f;
    }

    public static boolean isArrayEmpty(Object[] objArr) {
        return objArr == null || objArr.length == 0;
    }

    public static boolean inArray(Object[] objArr, Object obj) {
        if (obj != null && objArr != null && objArr.length > 0) {
            for (Object obj2 : objArr) {
                if (obj2.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static float getTouchSlop(View view) {
        if (sTouchSlop == 0.0f && view != null) {
            sTouchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
        }
        return sTouchSlop;
    }

    public static double getDistance(float f, float f2, float f3, float f4) {
        return Math.sqrt(Math.pow(f3 - f, 2.0d) + Math.pow(f4 - f2, 2.0d));
    }

    public static void getRect(IAnimTarget iAnimTarget, RectF rectF) {
        rectF.left = iAnimTarget.getValue(ViewProperty.X);
        rectF.top = iAnimTarget.getValue(ViewProperty.Y);
        rectF.right = rectF.left + iAnimTarget.getValue(ViewProperty.WIDTH);
        rectF.bottom = rectF.top + iAnimTarget.getValue(ViewProperty.HEIGHT);
    }

    public static float getSize(IAnimTarget iAnimTarget, FloatProperty floatProperty) {
        if (floatProperty == ViewProperty.X) {
            floatProperty = ViewProperty.WIDTH;
        } else if (floatProperty == ViewProperty.Y) {
            floatProperty = ViewProperty.HEIGHT;
        } else if (floatProperty != ViewProperty.WIDTH && floatProperty != ViewProperty.HEIGHT) {
            floatProperty = null;
        }
        if (floatProperty == null) {
            return 0.0f;
        }
        return iAnimTarget.getValue(floatProperty);
    }

    public static boolean isBuiltInClass(Class cls) {
        return inArray(BUILT_IN, cls);
    }

    public static void addTo(Collection collection, Collection collection2) {
        for (Object obj : collection) {
            if (!collection2.contains(obj)) {
                collection2.add(obj);
            }
        }
    }

    public static String readProp(String str) {
        Method method = sGetPropMethod;
        if (method == null) {
            return "";
        }
        try {
            return (String) method.invoke(null, str, "");
        } catch (Exception e) {
            Log.i(TAG, "readProp failed", e);
            return "";
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                Log.w(TAG, "close " + closeable + " failed", e);
            }
        }
    }

    public static Object getLocal(ObjectPool objectPool, ThreadLocal threadLocal, Class cls) {
        Object obj = threadLocal.get();
        if (obj != null || cls == null) {
            return obj;
        }
        Object objAcquire = ObjectPool.acquire(objectPool, cls, new Object[0]);
        threadLocal.set(objAcquire);
        return objAcquire;
    }

    /* JADX WARN: Undo finally extract visitor
    java.lang.NullPointerException: Cannot invoke "Object.hashCode()" because "this.second" is null
    	at jadx.core.utils.Pair.hashCode(Pair.java:35)
    	at java.base/java.util.HashMap.hash(HashMap.java:338)
    	at java.base/java.util.HashMap.getNode(HashMap.java:577)
    	at java.base/java.util.HashMap.containsKey(HashMap.java:603)
    	at jadx.core.dex.visitors.finaly.traverser.state.TraverserGlobalCommonState.hasBlocksBeenCached(TraverserGlobalCommonState.java:35)
    	at jadx.core.dex.visitors.finaly.traverser.handlers.MergePathActivePathTraverserHandler.handle(MergePathActivePathTraverserHandler.java:174)
    	at jadx.core.dex.visitors.finaly.traverser.handlers.AbstractActivePathTraverserHandler.process(AbstractActivePathTraverserHandler.java:19)
    	at jadx.core.dex.visitors.finaly.traverser.TraverserController.processHandlerImplementations(TraverserController.java:43)
    	at jadx.core.dex.visitors.finaly.traverser.TraverserController.advance(TraverserController.java:156)
    	at jadx.core.dex.visitors.finaly.traverser.TraverserController.process(TraverserController.java:79)
    	at jadx.core.dex.visitors.finaly.MarkFinallyVisitor.findCommonInsns(MarkFinallyVisitor.java:404)
    	at jadx.core.dex.visitors.finaly.MarkFinallyVisitor.extractFinally(MarkFinallyVisitor.java:284)
    	at jadx.core.dex.visitors.finaly.MarkFinallyVisitor.processTryBlock(MarkFinallyVisitor.java:202)
    	at jadx.core.dex.visitors.finaly.MarkFinallyVisitor.visit(MarkFinallyVisitor.java:135)
     */
    public static Bitmap compressImage(Bitmap bitmap, int i, int i2) {
        StringBuilder sb;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmapDecodeStream = null;
        try {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = i2;
                bitmapDecodeStream = BitmapFactory.decodeStream(parseToInputStream(byteArrayOutputStream), null, options);
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e = e;
                    sb = new StringBuilder("IO close fail, ");
                    sb.append(e);
                    Log.i(TAG, sb.toString());
                }
            } catch (Throwable th) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e2) {
                    Log.i(TAG, "IO close fail, " + e2);
                }
                throw th;
            }
        } catch (Exception e3) {
            Log.w(TAG, "TintDrawable.compressImage failed, " + e3);
            try {
                byteArrayOutputStream.close();
            } catch (IOException e4) {
                e = e4;
                sb = new StringBuilder("IO close fail, ");
                sb.append(e);
                Log.i(TAG, sb.toString());
            }
        }
        return bitmapDecodeStream;
    }

    public static ByteArrayInputStream parseToInputStream(ByteArrayOutputStream byteArrayOutputStream) throws Exception {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public static void timeStatBegin(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        for (String str : strArr) {
            if (TextUtils.isEmpty(str)) {
                sTimeStatArray.put(str, Long.valueOf(jCurrentTimeMillis));
            }
        }
    }

    public static long timeStatEnd(String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = jCurrentTimeMillis - (((Long) sTimeStatArray.get(str)) != null ? jCurrentTimeMillis : 0L);
        if (LogUtils.isLogMainEnabled()) {
            LogUtils.debug("打印当前用时： TAG=" + str + " 用时为 " + j, new Object[0]);
        }
        return j;
    }

    public static void timeStatClear() {
        sTimeStatArray.clear();
    }
}
