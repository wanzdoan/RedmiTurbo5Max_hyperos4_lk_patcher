package com.android.internal.telephony.satellite;

import android.telephony.satellite.EnableRequestAttributes;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class AutoEnablementController implements SatelliteEnablementStrategy {
    @Override // com.android.internal.telephony.satellite.SatelliteEnablementStrategy
    public void enableSatellite(EnableRequestAttributes enableRequestAttributes, Executor executor, Consumer<Integer> consumer) {
        consumer.accept(0);
    }

    @Override // com.android.internal.telephony.satellite.SatelliteEnablementStrategy
    public void disableSatellite(EnableRequestAttributes enableRequestAttributes, Executor executor, Consumer<Integer> consumer) {
        consumer.accept(0);
    }
}
