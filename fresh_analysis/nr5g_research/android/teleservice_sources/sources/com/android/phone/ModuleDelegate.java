package com.android.phone;

import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.IMiConnectModule;
import com.android.phone.module.IModemModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.module.ISettingsModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModuleDelegate {
    private static final Map MAP = new ConcurrentHashMap();
    private static final Map SUPPLIERS = Map.of(ITelephonyModule.class, new Supplier() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return ModuleDelegate.$r8$lambda$5CIUz6MZY83jMp2MeSo8rlu4Ot4();
        }
    }, INetworkModule.class, new Supplier() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return ModuleDelegate.m1327$r8$lambda$EQx8nn_35Cxt6cudcoaOPzkBzI();
        }
    }, IModemModule.class, new Supplier() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return ModuleDelegate.m1329$r8$lambda$Nzt9nbQK4n0nqZwDcC4OnoGh8M();
        }
    }, ISatelliteModule.class, new Supplier() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return ModuleDelegate.m1328$r8$lambda$JXRBASkbA8Kg3OvW192e_0m6C4();
        }
    }, IUtilityModule.class, new Supplier() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return ModuleDelegate.$r8$lambda$Cn63xhL76JECapKwiHt277eTLso();
        }
    }, ISettingsModule.class, new Supplier() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda6
        @Override // java.util.function.Supplier
        public final Object get() {
            return ModuleDelegate.$r8$lambda$B5vhrRC3GJNWIMc0acnkAmCK_Rg();
        }
    }, IMiConnectModule.class, new Supplier() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda7
        @Override // java.util.function.Supplier
        public final Object get() {
            return ModuleDelegate.$r8$lambda$JXmK3ai0GTxXqdfXQaRR0lIwwFk();
        }
    }, IFiveGPowerModule.class, new Supplier() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda8
        @Override // java.util.function.Supplier
        public final Object get() {
            return ModuleDelegate.$r8$lambda$PDhBonYzOlhje2AoonqwR9ZCzbM();
        }
    });

    public static /* synthetic */ Object $r8$lambda$5CIUz6MZY83jMp2MeSo8rlu4Ot4() {
        return new ITelephonyModule() { // from class: com.android.phone.ModuleDelegate.1
        };
    }

    /* JADX INFO: renamed from: $r8$lambda$EQx8nn_-35Cxt6cudcoaOPzkBzI, reason: not valid java name */
    public static /* synthetic */ Object m1327$r8$lambda$EQx8nn_35Cxt6cudcoaOPzkBzI() {
        return new INetworkModule() { // from class: com.android.phone.ModuleDelegate.2
        };
    }

    /* JADX INFO: renamed from: $r8$lambda$Nzt9nbQK4n0-nqZwDcC4OnoGh8M, reason: not valid java name */
    public static /* synthetic */ Object m1329$r8$lambda$Nzt9nbQK4n0nqZwDcC4OnoGh8M() {
        return new IModemModule() { // from class: com.android.phone.ModuleDelegate.3
        };
    }

    /* JADX INFO: renamed from: $r8$lambda$JXRBASkbA8Kg3O-vW192e_0m6C4, reason: not valid java name */
    public static /* synthetic */ Object m1328$r8$lambda$JXRBASkbA8Kg3OvW192e_0m6C4() {
        return new ISatelliteModule() { // from class: com.android.phone.ModuleDelegate.4
        };
    }

    public static /* synthetic */ Object $r8$lambda$Cn63xhL76JECapKwiHt277eTLso() {
        return new IUtilityModule() { // from class: com.android.phone.ModuleDelegate.5
        };
    }

    public static /* synthetic */ Object $r8$lambda$B5vhrRC3GJNWIMc0acnkAmCK_Rg() {
        return new ISettingsModule() { // from class: com.android.phone.ModuleDelegate.6
        };
    }

    public static /* synthetic */ Object $r8$lambda$JXmK3ai0GTxXqdfXQaRR0lIwwFk() {
        return new IMiConnectModule() { // from class: com.android.phone.ModuleDelegate.7
        };
    }

    public static /* synthetic */ Object $r8$lambda$PDhBonYzOlhje2AoonqwR9ZCzbM() {
        return new IFiveGPowerModule() { // from class: com.android.phone.ModuleDelegate.8
        };
    }

    public static Object get(Class cls) {
        return MAP.computeIfAbsent(cls, new Function() { // from class: com.android.phone.ModuleDelegate$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ModuleDelegate.$r8$lambda$ax7jcvMh4WRjjcz_e__c3cph8n8((Class) obj);
            }
        });
    }

    public static /* synthetic */ Object $r8$lambda$ax7jcvMh4WRjjcz_e__c3cph8n8(Class cls) {
        Supplier supplier = (Supplier) SUPPLIERS.get(cls);
        if (supplier != null) {
            return supplier.get();
        }
        Folme$$ExternalSyntheticBUOutline0.m("Unknown module interface: ".concat(cls.getName()));
        return null;
    }

    public static void register(Class cls, Object obj) {
        MAP.put(cls, obj);
    }
}
