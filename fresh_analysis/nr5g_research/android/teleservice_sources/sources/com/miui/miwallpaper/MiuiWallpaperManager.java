package com.miui.miwallpaper;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.WindowManager;
import com.ot.pubsub.g.f;
import miui.os.Build;

/* JADX INFO: loaded from: classes.dex */
public class MiuiWallpaperManager {
    private static final boolean IS_FOLD_DEVICE;
    public static final int MI_WALLPAPER_WHICH_ALL;
    private static volatile MiuiWallpaperManager sInstance;
    private MiuiWallpaperManagerCallback mCallback;
    private final Context mContext;
    private IMiuiWallpaperManagerService mService;
    public static final ComponentName DEFAULT_WALLPAPER_COMPONENT = new ComponentName("com.miui.miwallpaper", "com.miui.miwallpaper.wallpaperservice.ImageWallpaper");
    public static final ComponentName MAML_WALLPAPER_COMPONENT = new ComponentName("com.miui.miwallpaper", "com.miui.miwallpaper.MiWallpaper");
    private final IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.miui.miwallpaper.MiuiWallpaperManager.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.i("MiuiWallpaperManager", "linkToDeath:MiuiWallpaperManagerService died, try rebind");
            MiuiWallpaperManager.this.bindService();
        }
    };
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.miui.miwallpaper.MiuiWallpaperManager.2
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("MiuiWallpaperManager", "onServiceConnected::componentName = " + componentName + ", instance = " + MiuiWallpaperManager.sInstance);
            MiuiWallpaperManager.this.mService = IMiuiWallpaperManagerService.Stub.asInterface(iBinder);
            MiuiWallpaperManager.this.mCallback.onMiuiWallpaperInitialized(MiuiWallpaperManager.sInstance);
            try {
                iBinder.linkToDeath(MiuiWallpaperManager.this.mDeathRecipient, 0);
            } catch (Throwable th) {
                Log.e("MiuiWallpaperManager", "linkToDeath fail : ", th);
            }
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.miui.miwallpaper.MiuiWallpaperManager.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Uri data = intent.getData();
            if (!"android.intent.action.PACKAGE_ADDED".equals(action) || data == null) {
                return;
            }
            String schemeSpecificPart = data.getSchemeSpecificPart();
            if ("com.miui.miwallpaper".equals(schemeSpecificPart)) {
                Log.i("MiuiWallpaperManager", "package update: action = " + action + " packageName = " + schemeSpecificPart);
                MiuiWallpaperManager.this.bindService();
            }
        }
    };

    public interface MiuiWallpaperManagerCallback {
        void onMiuiWallpaperInitialized(MiuiWallpaperManager miuiWallpaperManager);
    }

    static {
        boolean zIsFoldDevices = isFoldDevices();
        IS_FOLD_DEVICE = zIsFoldDevices;
        MI_WALLPAPER_WHICH_ALL = zIsFoldDevices ? 15 : 3;
    }

    private MiuiWallpaperManager(Context context, MiuiWallpaperManagerCallback miuiWallpaperManagerCallback) {
        this.mContext = context;
        this.mCallback = miuiWallpaperManagerCallback;
    }

    public static void init(Context context, MiuiWallpaperManagerCallback miuiWallpaperManagerCallback) {
        if (sInstance == null) {
            synchronized (MiuiWallpaperManager.class) {
                try {
                    if (sInstance == null) {
                        Log.i("MiuiWallpaperManager", "init...");
                        sInstance = new MiuiWallpaperManager(context, miuiWallpaperManagerCallback);
                        sInstance.registerBroadcast();
                        sInstance.bindService();
                    } else {
                        miuiWallpaperManagerCallback.onMiuiWallpaperInitialized(sInstance);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return;
        }
        miuiWallpaperManagerCallback.onMiuiWallpaperInitialized(sInstance);
    }

    private void unBindService() {
        if (isServiceReady()) {
            Log.e("MiuiWallpaperManager", "unBindService...");
            this.mService.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            this.mContext.unbindService(this.mServiceConnection);
            this.mCallback = null;
        }
    }

    public static void staticDestroy() {
        if (sInstance != null) {
            sInstance.unBindService();
            Log.e("MiuiWallpaperManager", "staticDestroy, set sInstance null", new Exception());
            sInstance = null;
        }
    }

    public Bitmap getMiuiWallpaperPreview(int i) {
        Bitmap fromFile = null;
        if (isServiceReady() && isSingleWhich(i)) {
            try {
                ParcelFileDescriptor miuiWallpaperPreview = this.mService.getMiuiWallpaperPreview(i);
                try {
                    fromFile = MiuiWallpaperFileUtils.readFromFile(miuiWallpaperPreview);
                    if (miuiWallpaperPreview == null) {
                        return fromFile;
                    }
                    miuiWallpaperPreview.close();
                    return fromFile;
                } catch (Throwable th) {
                    if (miuiWallpaperPreview != null) {
                        try {
                            miuiWallpaperPreview.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                Log.e("MiuiWallpaperManager", "getMiuiWallpaperPath fail : ", th3);
            }
        }
        return fromFile;
    }

    public Bitmap getFitScreenWallpaperPreview(Context context, int i) {
        int iWidth;
        int iHeight;
        Bitmap miuiWallpaperPreview = getMiuiWallpaperPreview(i);
        if (miuiWallpaperPreview != null) {
            Rect bounds = ((WindowManager) context.getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
            if (IS_FOLD_DEVICE || Build.IS_TABLET) {
                iWidth = bounds.width();
                iHeight = bounds.height();
            } else {
                int iWidth2 = bounds.width();
                int iHeight2 = bounds.height();
                iWidth = Math.min(iWidth2, iHeight2);
                iHeight = Math.max(iWidth2, iHeight2);
            }
            try {
                int width = miuiWallpaperPreview.getWidth();
                int height = miuiWallpaperPreview.getHeight();
                float fMax = Math.max(iWidth / width, iHeight / height);
                Matrix matrix = new Matrix();
                matrix.setScale(fMax, fMax);
                Bitmap bitmapCreateBitmap = Bitmap.createBitmap(miuiWallpaperPreview, 0, 0, width, height, matrix, true);
                int width2 = bitmapCreateBitmap.getWidth();
                int height2 = bitmapCreateBitmap.getHeight();
                return Bitmap.createBitmap(bitmapCreateBitmap, width2 > iWidth ? (width2 - iWidth) / 2 : 0, height2 > iHeight ? (height2 - iHeight) / 2 : 0, iWidth, iHeight);
            } catch (Throwable th) {
                Log.e("MiuiWallpaperManager", "getScreenCenterCropWallpaperPreview fail", th);
            }
        }
        return miuiWallpaperPreview;
    }

    public static boolean isSingleWhich(int i) {
        boolean z = i == 1 || i == 2;
        if (IS_FOLD_DEVICE) {
            z = z || i == 4 || i == 8;
        }
        if (!z) {
            Log.e("MiuiWallpaperManager", "is not single which: which = " + i);
        }
        return z;
    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme(f.a.e);
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindService() {
        Intent intent = new Intent("android.service.wallpaper.WallPaperControllerService");
        intent.setPackage("com.miui.miwallpaper");
        this.mContext.bindService(intent, this.mServiceConnection, 1);
    }

    private boolean isServiceReady() {
        if (this.mService != null) {
            return true;
        }
        Log.e("MiuiWallpaperManager", "mService is null.");
        return false;
    }

    private static boolean isFoldDevices() {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return ((Integer) cls.getMethod("getInt", String.class, Integer.TYPE).invoke(cls, "persist.sys.muiltdisplay_type", 0)).intValue() == 2;
        } catch (Throwable th) {
            Log.e("MiuiWallpaperManager", "isFoldDevices fail : ", th);
            return false;
        }
    }
}
