package com.android.phone;

import android.content.Context;
import android.util.Log;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.xiaomi.hyperradio.HyperRadioManager;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class SensorStateManager {
    private static volatile SensorStateManager sInstance;
    private Context mContext;

    private SensorStateManager(Context context) {
        if (context == null) {
            Folme$$ExternalSyntheticBUOutline0.m("Context cannot be null in SensorStateManager constructor");
            throw null;
        }
        this.mContext = context.getApplicationContext();
    }

    public static SensorStateManager getInstance(Context context) {
        if (sInstance == null) {
            validateContext(context);
            synchronized (SensorStateManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = createInstance(context);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private static void validateContext(Context context) {
        if (context != null) {
            return;
        }
        Folme$$ExternalSyntheticBUOutline0.m("Context cannot be null when creating instance");
    }

    private static SensorStateManager createInstance(Context context) {
        return new SensorStateManager(context.getApplicationContext());
    }

    public void setSensorState(final int i, final int i2) {
        if (MiuiTelephony.IS_XRING) {
            HyperRadioManager.getInstance(this.mContext).setSensorState(getDefaultDataSlotId(), i, i2, new ProfileInstallReceiver$$ExternalSyntheticLambda0(), new HyperRadioManager.GenericResponse() { // from class: com.android.phone.SensorStateManager.1
                public void onResponse(int i3) {
                    SensorStateManager.this.logResult(i, i2, i3);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logResult(int i, int i2, int i3) {
        String str = String.format("场景类型:%d,状态:%d,设置:%s", Integer.valueOf(i), Integer.valueOf(i2), i3 == 0 ? "成功" : String.format("失败，错误码: %d", Integer.valueOf(i3)));
        if (i3 == 0) {
            Log.d("SensorStateManager", str);
        } else {
            Log.e("SensorStateManager", str);
        }
    }

    private int getDefaultDataSlotId() {
        DefaultSimManager.getInstance();
        return DefaultSimManager.getDefaultDataSlotId();
    }

    public void setSubwayState(boolean z) {
        setSensorState(1, z ? 1 : 0);
    }

    public void setSubwayRunningState(boolean z) {
        setSensorState(1, z ? 2 : 3);
    }

    public void setMovementState(boolean z) {
        setSensorState(5, z ? 1 : 0);
    }

    public void setAirportState(boolean z) {
        setSensorState(2, z ? 1 : 0);
    }

    public void setBasementState(boolean z) {
        setSensorState(4, z ? 1 : 0);
    }

    public void setElevatorState(boolean z) {
        setSensorState(3, z ? 1 : 0);
    }
}
