package com.android.internal.telephony;

import android.content.ContentValues;
import android.content.Context;
import android.telephony.SubscriptionInfo;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.io.PrintWriter;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSubscriptionControllerStub {
    private static volatile IMiuiSubscriptionController sInstance;

    public interface IMiuiSubscriptionController {
        default void addActiveSubInfosCache(SubscriptionInfo subscriptionInfo) {
        }

        default void addPhoneNumberToSubInfo(Context context, int i) {
        }

        default boolean carrierNameEqualsCache(int i, String str) {
            return false;
        }

        default void clearActiveSubInfosCache() {
        }

        default boolean displayNameUsingSrcEqualsCache(int i, String str, long j) {
            return false;
        }

        default void dumpDatabaseSubInfoList(PrintWriter printWriter, boolean z, List<SubscriptionInfo> list) {
        }

        default List<SubscriptionInfo> getActiveSubInfoListCache() {
            return null;
        }

        default List<SubscriptionInfo> getOpportunisticSubInfoListCache() {
            return null;
        }

        default boolean isUpdatedPhoneNumberByUser(Context context, int i) {
            return false;
        }

        default boolean mccMncEqualsCache(int i, int i2, int i3) {
            return false;
        }

        default void notifyFeatureResolveSimReady(SubscriptionInfoInternal subscriptionInfoInternal) {
        }

        default void removeActiveSubInfosCache(int i) {
        }

        default void setUiccApplicationsEnabled(Context context, boolean z, int i) {
        }

        default void updateCarrierText(String str, int i) {
        }

        default void updateMccMnc(String str, int i) {
        }

        default void updateSubInfoListCache(Context context, int i, ContentValues contentValues) {
        }
    }

    public static IMiuiSubscriptionController get() {
        if (sInstance == null) {
            synchronized (MiuiSubscriptionControllerStub.class) {
                try {
                    if (sInstance == null) {
                        sInstance = TelephonyCommonProxyFactory.getHyperInterface().makeMiuiSubscriptionControllerStub();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }
}
