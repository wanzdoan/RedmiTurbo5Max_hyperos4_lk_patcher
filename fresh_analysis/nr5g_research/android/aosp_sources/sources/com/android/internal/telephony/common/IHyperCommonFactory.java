package com.android.internal.telephony.common;

import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperCommonFactory {
    boolean isValid(int i);

    default <T extends IHyperCommonModule> T initModule(T t, Object... objArr) {
        verityParams(t);
        return t;
    }

    default <T extends IHyperCommonModule> T getModuleInCache(int i, T t) {
        verityParams(t);
        return t;
    }

    default <T extends IHyperCommonModule> void verityParams(T t) throws IllegalArgumentException {
        if (t == null) {
            Phone$$ExternalSyntheticBUOutline0.m("def can not be null");
        } else {
            if (t.index() != HyperModuleList$HyperIndex.End) {
                return;
            }
            throw new IllegalArgumentException(t + "must override index() method");
        }
    }

    default void verityParamsType(String str, Object[] objArr, int i, Class... clsArr) throws IllegalArgumentException {
        if (objArr == null || clsArr == null || objArr.length != i || clsArr.length != i) {
            throw new IllegalArgumentException(str + " need +" + i + " params");
        }
        for (int i2 = 0; i2 < i; i2++) {
            Object obj = objArr[i2];
            if (obj != null && !clsArr[i2].isInstance(obj)) {
                throw new IllegalArgumentException(clsArr[i2].getName() + " is not instance " + objArr[i2]);
            }
        }
    }
}
