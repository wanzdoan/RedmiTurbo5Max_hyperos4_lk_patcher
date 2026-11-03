package com.hyper.internal.telephony;

import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.common.HyperModuleList;
import com.android.internal.telephony.common.IHyperCommonFactory;
import com.android.internal.telephony.common.IHyperCommonModule;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class HyperModuleCache {
    private static final String TAG = "HyperModuleCache";
    private static Map<Integer, Object> sModuleMap = new ConcurrentHashMap();
    private static ArrayList<IHyperCommonFactory> sFactoryCache = new ArrayList<>();

    static {
        int numPhones = TelephonyManager.getDefault().getPhoneCount();
        for (int i = 0; i < numPhones; i++) {
            Object[] moduleCache = new Object[HyperModuleList.HyperIndex.End.ordinal()];
            sModuleMap.put(Integer.valueOf(i), moduleCache);
            Rlog.d(TAG, "sModuleMap(" + i + ") = " + sModuleMap.get(Integer.valueOf(i)));
        }
        sFactoryCache.add(HyperTelephonyFactory.getInstance());
    }

    private void printModuleMap(int slotId) {
        try {
            Object[] moduleCache = (Object[]) sModuleMap.get(Integer.valueOf(slotId));
            for (int j = 0; j < HyperModuleList.HyperIndex.End.ordinal(); j++) {
                Rlog.d(TAG, "sModuleMap(" + slotId + ") has moduleCache [" + j + "] = " + moduleCache[j]);
            }
        } catch (Exception e) {
            Rlog.d(TAG, "e = " + e);
        }
    }

    public static <T extends IHyperCommonModule> T get(int i, T t) {
        if (!SubscriptionManager.isValidSlotIndex(i)) {
            return t;
        }
        try {
            Object[] objArr = (Object[]) sModuleMap.get(Integer.valueOf(i));
            int index = getIndex(t);
            IHyperCommonModule iHyperCommonModule = (IHyperCommonModule) objArr[t.index().ordinal()];
            if (iHyperCommonModule == null) {
                synchronized (t.getDefault()) {
                    try {
                        iHyperCommonModule = (IHyperCommonModule) objArr[index];
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return iHyperCommonModule != null ? (T) iHyperCommonModule : t;
        } catch (Exception e) {
            Rlog.d(TAG, "e = " + e);
            return t;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13, types: [T extends com.android.internal.telephony.common.IHyperCommonModule, com.android.internal.telephony.common.IHyperCommonModule] */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v17 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Type inference failed for: r3v4 */
    public static <T extends IHyperCommonModule> T getOrCreate(int i, T t, Object... objArr) {
        if (!SubscriptionManager.isValidSlotIndex(i)) {
            return t;
        }
        try {
            Object[] objArr2 = (Object[]) sModuleMap.get(Integer.valueOf(i));
            int index = getIndex(t);
            T t2 = (T) objArr2[t.index().ordinal()];
            ?? r2 = t2;
            if (t2 == false) {
                synchronized (t.getDefault()) {
                    try {
                        ?? r3 = (IHyperCommonModule) objArr2[index];
                        ?? r4 = r3;
                        if (r3 == 0) {
                            synchronized (sFactoryCache) {
                                for (IHyperCommonFactory iHyperCommonFactory : sFactoryCache) {
                                    if (iHyperCommonFactory.isValid(index)) {
                                        IHyperCommonModule iHyperCommonModuleInitModule = iHyperCommonFactory.initModule(t, objArr);
                                        r3 = (T) iHyperCommonModuleInitModule;
                                        if (iHyperCommonModuleInitModule != null) {
                                            set(i, r3);
                                            return r3;
                                        }
                                    }
                                }
                                r4 = r3;
                            }
                        }
                        r2 = r4;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            return r2 != 0 ? (T) r2 : t;
        } catch (Exception e) {
            Rlog.d(TAG, "e = " + e);
            return t;
        }
    }

    public static <T extends IHyperCommonModule> void set(int slotId, T impl) {
        if (!SubscriptionManager.isValidSlotIndex(slotId)) {
            return;
        }
        try {
            Object[] moduleCache = (Object[]) sModuleMap.get(Integer.valueOf(slotId));
            int index = getIndex(impl);
            Rlog.d(TAG, "sModuleMap(" + slotId + ") set moduleCache[" + index + "] = " + impl);
            synchronized (impl.getDefault()) {
                try {
                    moduleCache[index] = impl;
                    sModuleMap.replace(Integer.valueOf(slotId), moduleCache);
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (Exception e) {
            Rlog.d(TAG, "e = " + e);
        }
    }

    public static <T extends IHyperCommonFactory> void addFactory(T factory) {
        if (factory != null) {
            synchronized (sFactoryCache) {
                sFactoryCache.add(factory);
            }
        }
    }

    private static <T extends IHyperCommonModule> int getIndex(T service) throws IllegalArgumentException {
        if (service == null) {
            throw new IllegalArgumentException("dummy must not be null");
        }
        int index = service.index().ordinal();
        if (index >= HyperModuleList.HyperIndex.End.ordinal()) {
            throw new IllegalArgumentException("index = " + index + " size = " + HyperModuleList.HyperIndex.End.ordinal());
        }
        return index;
    }
}
