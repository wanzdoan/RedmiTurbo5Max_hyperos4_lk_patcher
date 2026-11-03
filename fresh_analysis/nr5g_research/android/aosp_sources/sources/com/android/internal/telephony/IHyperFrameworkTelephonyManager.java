package com.android.internal.telephony;

import android.content.Context;
import android.telephony.data.DataProfile;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperFrameworkTelephonyManager extends IHyperCommonModule {
    public static final IHyperFrameworkTelephonyManager DEFAULT = new IHyperFrameworkTelephonyManager() { // from class: com.android.internal.telephony.IHyperFrameworkTelephonyManager.1
    };
    public static final int MVNO_TYPE_PNN = 4;
    public static final int PAY_STATE_UNKNOWN = -1;
    public static final int POST_STATE = 1;
    public static final int PRE_STATE = 0;
    public static final String TAG = "IHyperFrameworkTelephonyManager";

    default boolean canBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean canFqdnBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean canIpDescriptorBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean isPayStateSupported(Context context, int i) {
        return false;
    }

    default boolean isSmsAvaliable(int i) {
        return true;
    }

    default void popupDataEnabledForSubsidy(int i, int i2) {
    }

    default String replaceIccid(String str) {
        return str;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperFrameworkTelephonyManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }
}
