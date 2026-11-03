package com.android.internal.telephony.satellite;

import android.telephony.satellite.EnableRequestAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteEnablementController {
    private final AutoEnablementController mAutoSatelliteController;
    private final ManualEnablementController mManualSatelliteController;
    private final Map mSatelliteDisabledMaskMap;

    public SatelliteEnablementController() {
        this(new ManualEnablementController(), new AutoEnablementController());
    }

    public SatelliteEnablementController(ManualEnablementController manualEnablementController, AutoEnablementController autoEnablementController) {
        this.mSatelliteDisabledMaskMap = new ConcurrentHashMap();
        this.mManualSatelliteController = manualEnablementController;
        this.mAutoSatelliteController = autoEnablementController;
    }

    public void requestSatelliteEnabled(int i, EnableRequestAttributes enableRequestAttributes, Executor executor, Consumer<Integer> consumer) {
        setSatelliteEnabledForReason(i, enableRequestAttributes.getConnectType(), enableRequestAttributes.getSatelliteEnablementRequestReason(), enableRequestAttributes.isEnabled());
        SatelliteEnablementStrategy enablementController = getEnablementController(enableRequestAttributes.getConnectType());
        if (enablementController != null) {
            if (isSatelliteEnabled(i, enableRequestAttributes.getConnectType())) {
                enablementController.enableSatellite(enableRequestAttributes, executor, consumer);
                return;
            } else {
                enablementController.disableSatellite(enableRequestAttributes, executor, consumer);
                return;
            }
        }
        consumer.accept(8);
    }

    public boolean isSatelliteEnabled(int i, int i2) {
        Map map = (Map) this.mSatelliteDisabledMaskMap.get(Integer.valueOf(i));
        return map == null || ((Integer) map.getOrDefault(Integer.valueOf(i2), 0)).intValue() == 0;
    }

    public static /* synthetic */ Map $r8$lambda$uimHqhc13w_nbFf4xRl0N4lyQQ8(Integer num) {
        return new ConcurrentHashMap();
    }

    private void setSatelliteEnabledForReason(int i, int i2, int i3, final boolean z) {
        final int i4 = 1 << i3;
        ((Map) this.mSatelliteDisabledMaskMap.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.internal.telephony.satellite.SatelliteEnablementController$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteEnablementController.$r8$lambda$uimHqhc13w_nbFf4xRl0N4lyQQ8((Integer) obj);
            }
        })).compute(Integer.valueOf(i2), new BiFunction() { // from class: com.android.internal.telephony.satellite.SatelliteEnablementController$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return SatelliteEnablementController.m1591$r8$lambda$anqj78vIfYEQxPueOQBSwd3zNw(z, i4, (Integer) obj, (Integer) obj2);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$anqj7-8vIfYEQxPueOQBSwd3zNw, reason: not valid java name */
    public static /* synthetic */ Integer m1591$r8$lambda$anqj78vIfYEQxPueOQBSwd3zNw(boolean z, int i, Integer num, Integer num2) {
        int iIntValue = num2 == null ? 0 : num2.intValue();
        if (z) {
            return Integer.valueOf((~i) & iIntValue);
        }
        return Integer.valueOf(iIntValue | i);
    }

    private SatelliteEnablementStrategy getEnablementController(int i) {
        if (i == 0) {
            return this.mAutoSatelliteController;
        }
        if (i != 1) {
            return null;
        }
        return this.mManualSatelliteController;
    }
}
