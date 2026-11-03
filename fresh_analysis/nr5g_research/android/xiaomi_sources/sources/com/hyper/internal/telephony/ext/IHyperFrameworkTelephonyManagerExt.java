package com.hyper.internal.telephony.ext;

import android.content.Context;
import android.telephony.data.DataProfile;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperFrameworkTelephonyManagerExt extends IHyperCommonModule {
    public static final IHyperFrameworkTelephonyManagerExt DEFAULT = new IHyperFrameworkTelephonyManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperFrameworkTelephonyManagerExt.1
    };
    public static final String TAG = "IHyperFrameworkTelephonyManagerExt";

    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default boolean isPayStateSupported(Context context, int subId) {
        return false;
    }

    default int getPayState(int phoneId) {
        return -1;
    }

    default void popupDataEnabledForSubsidy(int oldSubId, int newSubId) {
    }

    default boolean isSmsAvaliable(int phoneId) {
        return true;
    }

    default String replaceIccid(String iccid) {
        return iccid;
    }

    default boolean canFqdnBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean canIpDescriptorBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }

    default boolean canBeSatisfiedBy(DataProfile dataProfile) {
        return false;
    }
}
