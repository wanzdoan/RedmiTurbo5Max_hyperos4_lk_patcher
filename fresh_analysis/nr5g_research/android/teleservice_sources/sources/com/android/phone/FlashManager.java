package com.android.phone;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.telephony.Rlog;
import com.android.phone.utils.Utils;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class FlashManager implements BatteryStateManager.BatteryStateListener {
    private static FlashManager INSTANCE = new FlashManager();
    private int mBatteryLevel = -1;
    private Context mContext;

    private FlashManager() {
    }

    public static FlashManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    @Override // com.android.phone.BatteryStateManager.BatteryStateListener
    public void notifyBatteryStateChanged(int i) {
        this.mBatteryLevel = i;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        boolean zIsFlashEnabled = isFlashEnabled();
        Rlog.i("FlashManager", "isFlashEnabled = " + zIsFlashEnabled);
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        if (zIsFlashEnabled) {
            if (defaultSharedPreferences.getBoolean("real_flash_when_ring", false)) {
                Rlog.i("FlashManager", "charging in low battery level or high battery level, open flash");
                MiuiSettings.System.putBoolean(this.mContext.getContentResolver(), "flash_when_ring_enabled", true);
                editorEdit.putBoolean("real_flash_when_ring", false);
                editorEdit.apply();
                return;
            }
            return;
        }
        if (isFlashWhenRingEnabled()) {
            Rlog.i("FlashManager", "no charging in low battery level, close flash");
            MiuiSettings.System.putBoolean(this.mContext.getContentResolver(), "flash_when_ring_enabled", false);
            editorEdit.putBoolean("real_flash_when_ring", true);
            editorEdit.apply();
        }
    }

    public boolean isFlashEnabled() {
        return !Utils.isBCLStrategyEnable() || BatteryStateManager.getInstance().isBatteryCharging() || this.mBatteryLevel > 5;
    }

    public boolean isFlashWhenRingEnabled() {
        return MiuiSettings.System.getBoolean(this.mContext.getContentResolver(), "flash_when_ring_enabled", false);
    }

    public void registerListener() {
        Rlog.i("FlashManager", "registerListener....");
        BatteryStateManager.getInstance().registerBatteryStateListener(this);
    }

    public void unregisterListener() {
        if (isActivityFront("com.android.phone.settings.AnswerStateSetting")) {
            return;
        }
        Rlog.i("FlashManager", "unregisterListener....");
        BatteryStateManager.getInstance().unregisterBatteryStateListener(this);
    }

    private boolean isActivityFront(String str) {
        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) this.mContext.getSystemService("activity")).getRunningTasks(1);
        return runningTasks != null && runningTasks.size() == 1 && runningTasks.get(0).topActivity.getClassName().equals(str);
    }
}
