package com.android.internal.telephony.satellite;

import android.telephony.satellite.EnableRequestAttributes;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public interface SatelliteEnablementStrategy {
    void disableSatellite(EnableRequestAttributes enableRequestAttributes, Executor executor, Consumer<Integer> consumer);

    void enableSatellite(EnableRequestAttributes enableRequestAttributes, Executor executor, Consumer<Integer> consumer);
}
