package miuix.autodensity;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Display;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import miuix.os.DeviceHelper;

/* JADX INFO: loaded from: classes2.dex */
public class DensityConfigManager {
    private static DensityConfigManager sInstance;
    private ConcurrentHashMap mDisplayConfigMap = new ConcurrentHashMap();
    private boolean mUseStableDensityLogic = false;
    private boolean mUseDeprecatedDensityLogic = false;
    private double mUserDeviceScale = 0.0d;
    private int mUserPPI = 0;
    private float mUserAccessibilityDpiDelta = 0.0f;
    private int mUserForcedDpi = 0;

    public static DensityConfigManager getInstance() {
        if (sInstance == null) {
            sInstance = new DensityConfigManager();
        }
        return sInstance;
    }

    private DensityConfigManager() {
    }

    public void init(Context context) {
        Display currentDisplay = DensityUtil.getCurrentDisplay(context);
        if (DeviceHelper.isCarWithScreen(context, currentDisplay) || DeviceHelper.isXiaomiSynergy(context)) {
            return;
        }
        getOrCreateDisplayConfig(context, currentDisplay);
    }

    public DisplayDensityConfig getOrCreateDisplayConfig(Context context, Display display) {
        int iValueOf;
        if (context == null || display == null) {
            return null;
        }
        if (display.getDisplayId() == 0) {
            iValueOf = 0;
        } else {
            iValueOf = Integer.valueOf(display.hashCode());
        }
        DisplayDensityConfig displayDensityConfig = (DisplayDensityConfig) this.mDisplayConfigMap.get(iValueOf);
        if (displayDensityConfig == null) {
            displayDensityConfig = new DisplayDensityConfig(context, display);
            if (DebugUtil.isEnableDebug()) {
                DebugUtil.printDensityLog("DisplayDensityConfig create DisplayConfig display:  display: " + display + " context: " + context);
            }
            displayDensityConfig.setUserPPI(this.mUserPPI);
            displayDensityConfig.setUserDeviceScale((float) this.mUserDeviceScale);
            displayDensityConfig.setUseStableDensityLogic(this.mUseStableDensityLogic);
            displayDensityConfig.setUseDeprecatedDensityLogic(this.mUseDeprecatedDensityLogic);
            this.mDisplayConfigMap.put(iValueOf, displayDensityConfig);
        }
        return displayDensityConfig;
    }

    public DisplayDensityConfig getDefaultConfig() {
        return (DisplayDensityConfig) this.mDisplayConfigMap.get(0);
    }

    public void setUseStableDensityLogic(boolean z) {
        this.mUseStableDensityLogic = z;
        Iterator it = this.mDisplayConfigMap.entrySet().iterator();
        while (it.hasNext()) {
            ((DisplayDensityConfig) ((Map.Entry) it.next()).getValue()).setUseStableDensityLogic(z);
        }
    }

    public void setUseDeprecatedDensityLogic(boolean z) {
        this.mUseDeprecatedDensityLogic = z;
        Iterator it = this.mDisplayConfigMap.entrySet().iterator();
        while (it.hasNext()) {
            ((DisplayDensityConfig) ((Map.Entry) it.next()).getValue()).setUseDeprecatedDensityLogic(z);
        }
    }

    public void setUserDeviceScale(float f) {
        this.mUserDeviceScale = f;
        Iterator it = this.mDisplayConfigMap.entrySet().iterator();
        while (it.hasNext()) {
            ((DisplayDensityConfig) ((Map.Entry) it.next()).getValue()).setUserDeviceScale(f);
        }
    }

    public void setUserPPI(int i) {
        this.mUserPPI = i;
        Iterator it = this.mDisplayConfigMap.entrySet().iterator();
        while (it.hasNext()) {
            ((DisplayDensityConfig) ((Map.Entry) it.next()).getValue()).setUserPPI(i);
        }
    }

    public DensityConfig getTargetConfig(Display display) {
        if (display == null) {
            return getTargetConfig();
        }
        DisplayDensityConfig displayDensityConfig = (DisplayDensityConfig) this.mDisplayConfigMap.get(Integer.valueOf(display.getDisplayId() == 0 ? 0 : display.hashCode()));
        if (displayDensityConfig == null) {
            return null;
        }
        return displayDensityConfig.getTargetConfig();
    }

    public DensityConfig getTargetConfig() {
        DisplayDensityConfig defaultConfig = getDefaultConfig();
        if (defaultConfig == null) {
            return null;
        }
        return defaultConfig.getTargetConfig();
    }

    public DensityConfig getOriginConfig(Display display) {
        if (display == null) {
            return getOriginConfig();
        }
        DisplayDensityConfig displayDensityConfig = (DisplayDensityConfig) this.mDisplayConfigMap.get(Integer.valueOf(display.getDisplayId() == 0 ? 0 : display.hashCode()));
        if (displayDensityConfig == null) {
            return null;
        }
        return displayDensityConfig.getOriginConfig();
    }

    public DensityConfig getOriginConfig() {
        DisplayDensityConfig defaultConfig = getDefaultConfig();
        if (defaultConfig == null) {
            return null;
        }
        return defaultConfig.getOriginConfig();
    }

    public boolean isAutoDensityEnabled() {
        DisplayDensityConfig defaultConfig = getDefaultConfig();
        if (defaultConfig == null) {
            return false;
        }
        return defaultConfig.isAutoDensityEnabled();
    }

    public boolean tryUpdateConfig(Context context, Configuration configuration) {
        Display currentDisplay = DensityUtil.getCurrentDisplay(context);
        DisplayDensityConfig orCreateDisplayConfig = getOrCreateDisplayConfig(context, currentDisplay);
        if (orCreateDisplayConfig == null) {
            Log.w("AutoDensity", " -> tryUpdateConfig failed: displayConfig is null, ");
            return false;
        }
        if (DebugUtil.isEnableDebug()) {
            DebugUtil.printDensityLog("tryUpdateConfig newConfig " + configuration + " context " + context);
        }
        return orCreateDisplayConfig.tryUpdateConfig(context, currentDisplay, configuration);
    }

    public void updateConfig(Context context, Configuration configuration) {
        Display currentDisplay = DensityUtil.getCurrentDisplay(context);
        DisplayDensityConfig orCreateDisplayConfig = getOrCreateDisplayConfig(context, currentDisplay);
        if (orCreateDisplayConfig == null) {
            Log.w("AutoDensity", " -> updateConfig failed: displayConfig is null");
        } else {
            orCreateDisplayConfig.updateConfig(context, currentDisplay, configuration);
        }
    }

    public void updateConfig(Context context, Configuration configuration, Display display) {
        if (display == null) {
            updateConfig(context, configuration);
            return;
        }
        DisplayDensityConfig orCreateDisplayConfig = getOrCreateDisplayConfig(context, display);
        if (orCreateDisplayConfig == null) {
            Log.w("AutoDensity", " -> updateConfig failed: displayConfig is null");
        } else {
            orCreateDisplayConfig.updateConfig(context, display, configuration);
        }
    }

    public int getDeviceCurrentAccessibilityDpi(Display display) {
        if (display == null) {
            return getDeviceCurrentAccessibilityDpi();
        }
        DisplayDensityConfig displayDensityConfig = (DisplayDensityConfig) this.mDisplayConfigMap.get(Integer.valueOf(display.getDisplayId() == 0 ? 0 : display.hashCode()));
        if (displayDensityConfig == null) {
            return 160;
        }
        return displayDensityConfig.getCurrentAccessibilityDpi();
    }

    public int getDeviceCurrentAccessibilityDpi() {
        DisplayDensityConfig defaultConfig = getDefaultConfig();
        if (defaultConfig == null) {
            return 160;
        }
        return defaultConfig.getCurrentAccessibilityDpi();
    }
}
