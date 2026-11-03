package miuix.autodensity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import miuix.core.util.EnvStateManager;
import miuix.core.util.RomUtils;
import miuix.core.util.SystemProperties;
import miuix.os.Build;
import miuix.os.DeviceHelper;
import miuix.provider.ExtraSettings$Secure;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayDensityConfig {
    private final int mDisplayId;
    private DisplayMetrics mDisplayMetrics;
    private final String mDisplayName;
    private boolean mIsRearDisplay;
    private float mMaxSizeInch;
    private float mMinSizeInch;
    private DensityConfig mOriginConfig;
    private final DensityConfig mTargetConfig;
    private boolean mUseStableDensityLogic = false;
    private boolean mUseDeprecatedDensityLogic = false;
    private int mCurrentDefaultDpi = 160;
    private int mCurrentAccessibilityDpi = 160;
    private int mCurrentForcedDpi = 160;
    private float mCurrentAccessibilityDpiDelta = 1.0f;
    private double mDeviceScale = 0.0d;
    private double mPPI = 0.0d;
    private float mScreenInches = 1.0f;
    private double mUserDeviceScale = 0.0d;
    private int mUserPPI = 0;
    private float mUserAccessibilityDpiDelta = 0.0f;
    private int mUserForcedDpi = 0;
    private final Point mPhysicalScreenSize = new Point();
    private final Point mScreenSize = new Point();
    private boolean mAutoDensityEnable = true;

    public DisplayDensityConfig(Context context, Display display) {
        this.mDisplayMetrics = null;
        int displayId = display.getDisplayId();
        this.mDisplayId = displayId;
        this.mDisplayName = display.getName();
        boolean zIsInRearDisplay = DeviceHelper.isInRearDisplay(display);
        this.mIsRearDisplay = zIsInRearDisplay;
        if (zIsInRearDisplay) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.mDisplayMetrics = displayMetrics;
            display.getRealMetrics(displayMetrics);
            this.mOriginConfig = new DensityConfig(this.mDisplayMetrics);
        }
        this.mTargetConfig = new DensityConfig(context.getResources().getConfiguration());
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("DisplayDensityConfig init id:" + displayId);
        }
        updateConfig(context, display, context.getResources().getConfiguration());
    }

    public void setUseStableDensityLogic(boolean z) {
        this.mUseStableDensityLogic = z;
    }

    public void setUseDeprecatedDensityLogic(boolean z) {
        this.mUseDeprecatedDensityLogic = z;
    }

    public boolean isEnableLogicMetrics() {
        return this.mUseDeprecatedDensityLogic || RomUtils.getMiuiVersion() < 14 || !this.mUseStableDensityLogic;
    }

    public void setUserDeviceScale(float f) {
        this.mUserDeviceScale = f;
    }

    public void setUserPPI(int i) {
        this.mUserPPI = i;
    }

    public DensityConfig getTargetConfig() {
        return this.mTargetConfig;
    }

    public DensityConfig getOriginConfig() {
        return this.mOriginConfig;
    }

    public boolean isAutoDensityEnabled() {
        return this.mAutoDensityEnable;
    }

    public void updateConfig(Context context, Display display, Configuration configuration) {
        if (this.mTargetConfig == null) {
            Log.w("AutoDensity", "AutoDensity doesn't init, updateConfig failed id:" + this.mDisplayId);
            return;
        }
        updateDeviceDisplayInfo(context, display);
        DisplayMetrics displayMetrics = this.mDisplayMetrics;
        if (this.mOriginConfig == null && displayMetrics != null) {
            this.mOriginConfig = new DensityConfig(displayMetrics);
        }
        if (!DeviceHelper.isInRearDisplay(display) && !isLocalOriginDpi(configuration.densityDpi)) {
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog(" <- DisplayDensityConfig id:" + this.mDisplayId + " name:" + this.mDisplayName + " updateConfig return: newConfig may has been modified by autodensity newConfig.densityDpi=" + configuration.densityDpi + " accessibilityDpi=" + this.mCurrentAccessibilityDpi + " forcedDpi=" + this.mCurrentForcedDpi);
                return;
            }
            return;
        }
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("DisplayDensityConfig id:" + this.mDisplayId + " name:" + this.mDisplayName + " updateConfig " + configuration + " context " + context);
        }
        int i = context.getResources().getDisplayMetrics().densityDpi;
        updateOriginConfigByDisplayMetrics(displayMetrics);
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("DisplayDensityConfig id:" + this.mDisplayId + " name:" + this.mDisplayName + " updateConfig newConfig.densityDpi=" + configuration.densityDpi + " defaultDpi=" + this.mCurrentDefaultDpi + " forceDpi=" + this.mCurrentForcedDpi + " accessibilityDpi=" + this.mCurrentAccessibilityDpi);
        }
        updateOriginConfigByNewConfig(configuration);
        EnvStateManager.updateOriginConfig(this.mOriginConfig);
        double dUpdatePPIOfDevice = updatePPIOfDevice(context, this.mPhysicalScreenSize, this.mScreenSize);
        double dUpdateDeviceScale = updateDeviceScale(context);
        double d = (Build.IS_AUTOMOTIVE ? 211.0d : (dUpdatePPIOfDevice * 1.1398963928222656d) * dUpdateDeviceScale) / ((double) this.mCurrentAccessibilityDpi);
        DensityConfig densityConfig = this.mOriginConfig;
        if (densityConfig != null) {
            int iRound = (int) Math.round(((double) densityConfig.densityDpi) * d);
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("DisplayDensityConfig id:" + this.mDisplayId + "updateConfig deviceScale:" + dUpdateDeviceScale + " scale:" + d);
            }
            updateTargetConfig(iRound, d);
        }
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("  Config changed. OriginConfig(" + this.mOriginConfig + ")\n\tTargetConfig(" + this.mTargetConfig + ")");
        }
    }

    public boolean tryUpdateConfig(Context context, Display display, Configuration configuration) {
        if (this.mTargetConfig == null) {
            Log.w("AutoDensity", "AutoDensity doesn't init, tryUpdateConfig failed id:" + this.mDisplayId);
            return false;
        }
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("tryUpdateConfig id:" + this.mDisplayId + " newConfig " + configuration + " context " + context);
        }
        DensityConfig densityConfig = this.mOriginConfig;
        if (densityConfig != null) {
            if (configuration.screenWidthDp != densityConfig.windowWidthDp || configuration.screenHeightDp != densityConfig.windowHeightDp || configuration.densityDpi != densityConfig.densityDpi || configuration.fontScale != densityConfig.fontScale) {
                updateConfig(context, display, configuration);
                return true;
            }
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("tryUpdateConfig failed");
            }
            return false;
        }
        updateConfig(context, display, configuration);
        return true;
    }

    private void updateDeviceDisplayInfo(Context context, Display display) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = displayMetrics;
        display.getRealMetrics(displayMetrics);
        updateDeviceDisplayInfo(context, display, this.mDisplayMetrics);
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("DisplayDensityConfig updateDeviceDisplayInfo display-displayMetrics " + this.mDisplayMetrics);
            DebugUtil.printDensityLog("\t\t\tdisplay:" + display);
        }
    }

    public boolean isLocalOriginDpi(int i) {
        return i == this.mCurrentAccessibilityDpi || i == this.mCurrentForcedDpi;
    }

    private void updateDeviceDisplayInfo(Context context, Display display, DisplayMetrics displayMetrics) {
        int i;
        updatePhysicalSizeFromDisplay(display);
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("\tupdateDeviceDisplayInfo context.densityDpi " + context.getResources().getConfiguration().densityDpi);
        }
        int deviceDefaultDpi = getDeviceDefaultDpi();
        if (deviceDefaultDpi == -1) {
            deviceDefaultDpi = displayMetrics.densityDpi;
            Log.w("AutoDensity", "warning!! can not get default dpi!! use defaultDisplayMetrics.densityDpi instead of it: " + deviceDefaultDpi);
        }
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("\tupdateDeviceDisplayInfo getDeviceDefaultDpi " + deviceDefaultDpi);
        }
        this.mCurrentDefaultDpi = deviceDefaultDpi;
        this.mCurrentAccessibilityDpiDelta = 1.0f;
        Point point = this.mScreenSize;
        Point point2 = this.mPhysicalScreenSize;
        point.set(point2.x, point2.y);
        if (DensityUtil.isSupportSwitchResolution()) {
            String str = SystemProperties.get("persist.sys.miui_resolution", null);
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("screenResolution: " + str);
            }
            if (!TextUtils.isEmpty(str)) {
                String[] strArrSplit = str.split(",");
                this.mScreenSize.set(Integer.parseInt(strArrSplit[0]), Integer.parseInt(strArrSplit[1]));
            }
            Point point3 = this.mScreenSize;
            int i2 = point3.y;
            Point point4 = this.mPhysicalScreenSize;
            if (i2 != point4.y) {
                this.mCurrentDefaultDpi = (deviceDefaultDpi * point3.x) / point4.x;
            }
        }
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("\tupdateDeviceDisplayInfo getDeviceDefaultDisplayDpi " + displayMetrics.densityDpi);
        }
        if (this.mUserAccessibilityDpiDelta > 0.0f) {
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("\tupdateDeviceDisplayInfo mUserCurrentAccessibilityDpiDelta " + this.mUserAccessibilityDpiDelta);
            }
            if (Build.IS_FLIP && DeviceHelper.isTinyScreen(context)) {
                this.mCurrentAccessibilityDpiDelta = 1.0f;
            } else {
                this.mCurrentAccessibilityDpiDelta = this.mUserAccessibilityDpiDelta;
                i = this.mUserForcedDpi;
                if (i <= 0) {
                    try {
                        i = ExtraSettings$Secure.getInt(context.getContentResolver(), "display_density_forced");
                    } catch (Exception e) {
                        Log.d("AutoDensity", "\tgetAccessibilityDpi on userCurrentDpiDelta Exception: " + e);
                        i = -1;
                    }
                }
            }
            i = -1;
        } else {
            if (Build.IS_FLIP && DeviceHelper.isTinyScreen(context)) {
                this.mCurrentAccessibilityDpiDelta = 1.0f;
            } else {
                try {
                    int i3 = Settings.System.getInt(context.getContentResolver(), "key_screen_zoom_level", 1);
                    if (i3 > 1) {
                        this.mCurrentAccessibilityDpiDelta = 1.05f;
                    } else if (i3 < 1) {
                        this.mCurrentAccessibilityDpiDelta = AutoDensityPolicy.ACCESSIBILITY_ZOOM_SMALL;
                    } else {
                        this.mCurrentAccessibilityDpiDelta = 1.0f;
                    }
                    i = this.mUserForcedDpi;
                    if (i <= 0) {
                        i = ExtraSettings$Secure.getInt(context.getContentResolver(), "display_density_forced");
                    }
                } catch (Exception e2) {
                    Log.d("AutoDensity", "\tgetAccessibilityDpi Exception: " + e2);
                    i = -1;
                }
            }
            i = -1;
        }
        if (i == -1) {
            i = this.mCurrentDefaultDpi;
        }
        this.mCurrentForcedDpi = i;
        this.mCurrentAccessibilityDpi = (int) Math.floor(this.mCurrentDefaultDpi * this.mCurrentAccessibilityDpiDelta);
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("\tupdateDisplayInfo currentDefaultDpi=" + this.mCurrentDefaultDpi + " mCurrentForcedDpi=" + this.mCurrentForcedDpi + " mCurrentAccessibilityDpi=" + this.mCurrentAccessibilityDpi + " delta=" + this.mCurrentAccessibilityDpiDelta + " logicSize=" + this.mScreenSize + " physicalSize=" + this.mPhysicalScreenSize);
        }
    }

    private double updatePPIOfDevice(Context context, Point point, Point point2) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("physical size: " + point + " cur size: " + point2 + ", display xdpi: " + displayMetrics.xdpi + ", ydpi: " + displayMetrics.ydpi);
        }
        float fMax = Math.max(displayMetrics.xdpi, displayMetrics.ydpi);
        float fMin = Math.min(displayMetrics.xdpi, displayMetrics.ydpi);
        float fMax2 = Math.max(point.x, point.y);
        float fMin2 = Math.min(point.x, point.y);
        float fMax3 = Math.max(point2.x, point2.y);
        float fMin3 = Math.min(point2.x, point2.y);
        if (isEnableLogicMetrics()) {
            fMin2 = fMin3;
            fMax2 = fMax3;
        }
        float f = fMax2 / fMax;
        float f2 = fMin2 / fMin;
        this.mMaxSizeInch = Math.max(f2, f);
        this.mMinSizeInch = Math.min(f2, f);
        float fSqrt = (float) Math.sqrt(Math.pow(f, 2.0d) + Math.pow(f2, 2.0d));
        this.mScreenInches = fSqrt;
        int i = this.mUserPPI;
        if (i > 0) {
            this.mPPI = i;
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("Screen inches : " + fSqrt + ", ppi-user:" + this.mUserPPI + ", physicalX:" + f + " physicalY:" + f2 + ", logicalX:" + this.mScreenSize.x + " logicalY:" + this.mScreenSize.y + ",min size inches: " + (Math.min(f2, f) / 2.8f));
            }
            return this.mUserPPI;
        }
        double dSqrt = Math.sqrt(Math.pow(fMax3, 2.0d) + Math.pow(fMin3, 2.0d)) / ((double) fSqrt);
        if (Build.IS_FLIP && fMax3 / displayMetrics.density <= 600.0f && SkuScale.hasSkuPPI()) {
            dSqrt = SkuScale.getSkuPPI(context, false);
        }
        this.mPPI = dSqrt;
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("Screen inches : " + fSqrt + ", ppi:" + dSqrt + ", physicalX:" + f + " physicalY:" + f2 + ", logicalX:" + this.mScreenSize.x + " logicalY:" + this.mScreenSize.y + ",min size inches: " + (Math.min(f2, f) / 2.8f));
        }
        return dSqrt;
    }

    private double updateDeviceScale(Context context) {
        double debugScale = getDebugScale();
        if (debugScale < 0.0d) {
            this.mAutoDensityEnable = false;
            Log.d("AutoDensity", "disable auto density in debug mode");
        } else {
            this.mAutoDensityEnable = true;
        }
        double d = this.mUserDeviceScale;
        if (d > 0.0d) {
            this.mDeviceScale = d;
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("updateDeviceScale by userDeviceScale " + this.mUserDeviceScale);
            }
        } else {
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("updateDeviceScale by calcu " + this.mDeviceScale);
            }
            this.mDeviceScale = AutoDensityPolicy.getDeviceScale(context, this.mMaxSizeInch, this.mMinSizeInch, this.mIsRearDisplay);
        }
        if (debugScale <= 0.0d) {
            debugScale = this.mDeviceScale;
        }
        return debugScale * ((double) getAccessibilityDelta(context));
    }

    private float getDebugScale() {
        if (RootUtil.isDeviceRooted()) {
            return DebugUtil.getAutoDensityScaleInDebugMode();
        }
        return 0.0f;
    }

    private float getAccessibilityDelta(Context context) {
        if (Build.IS_FLIP && DeviceHelper.isTinyScreen(context)) {
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("in flip external screen delta: 1.0f");
            }
            return 1.0f;
        }
        int i = this.mCurrentDefaultDpi;
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("default dpi: " + i);
        }
        if (Process.isIsolated()) {
            Log.d("AutoDensity", "getAccessibilityDelta failed reason: this process is isolated");
            return 1.0f;
        }
        if (i == -1) {
            return 1.0f;
        }
        float f = this.mCurrentAccessibilityDpiDelta;
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("accessibility dpi: " + this.mCurrentAccessibilityDpi + ", delta: " + f);
        }
        return f;
    }

    private void updatePhysicalSizeFromDisplay(Display display) {
        this.mPhysicalScreenSize.set(0, 0);
        Display.Mode[] supportedModes = display.getSupportedModes();
        for (int i = 0; i < supportedModes.length; i++) {
            Display.Mode mode = supportedModes[i];
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("\tupdatePhysicalSizeFromDisplay mode" + i + " " + mode);
            }
            this.mPhysicalScreenSize.x = Math.max(mode.getPhysicalWidth(), this.mPhysicalScreenSize.x);
            this.mPhysicalScreenSize.y = Math.max(mode.getPhysicalHeight(), this.mPhysicalScreenSize.y);
        }
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("\tupdatePhysicalSizeFromDisplay mPhysicalScreenSize " + this.mPhysicalScreenSize);
        }
    }

    private void updateOriginConfigByDisplayMetrics(DisplayMetrics displayMetrics) {
        DensityConfig densityConfig = this.mOriginConfig;
        if (densityConfig == null || displayMetrics == null) {
            return;
        }
        float f = displayMetrics.density;
        densityConfig.density = f;
        float f2 = displayMetrics.scaledDensity;
        densityConfig.scaledDensity = f2;
        densityConfig.densityDpi = displayMetrics.densityDpi;
        densityConfig.fontScale = f2 / f;
        densityConfig.windowWidthDp = (int) ((displayMetrics.widthPixels / f) + 0.5f);
        densityConfig.windowHeightDp = (int) ((displayMetrics.heightPixels / f) + 0.5f);
    }

    private void updateOriginConfigByNewConfig(Configuration configuration) {
        this.mOriginConfig = new DensityConfig(configuration);
    }

    private void updateTargetConfig(int i, double d) {
        DensityConfig densityConfig = this.mOriginConfig;
        if (densityConfig == null) {
            return;
        }
        DensityConfig densityConfig2 = this.mTargetConfig;
        densityConfig2.windowWidthDp = densityConfig.windowWidthDp;
        densityConfig2.windowHeightDp = densityConfig.windowHeightDp;
        densityConfig2.defaultBitmapDensity = i;
        densityConfig2.densityDpi = i;
        float f = i / 160.0f;
        densityConfig2.density = f;
        densityConfig2.fontScale = (float) (((double) densityConfig.fontScale) * d);
        densityConfig2.scaledDensity = f * densityConfig.fontScale;
    }

    public int getCurrentAccessibilityDpi() {
        return this.mCurrentAccessibilityDpi;
    }

    public int getDeviceDefaultDpi() {
        DensityConfig densityConfig;
        if (this.mDisplayId == DeviceHelper.SUB_BUILTIN_DISPLAY && (densityConfig = this.mOriginConfig) != null) {
            try {
                return SystemProperties.getInt("ro.sf.lcd_sec_density", densityConfig.densityDpi);
            } catch (Exception unused) {
                return this.mOriginConfig.densityDpi;
            }
        }
        DensityConfig densityConfig2 = this.mOriginConfig;
        return SystemProperties.getInt("ro.sf.lcd_density", densityConfig2 != null ? densityConfig2.densityDpi : -1);
    }
}
