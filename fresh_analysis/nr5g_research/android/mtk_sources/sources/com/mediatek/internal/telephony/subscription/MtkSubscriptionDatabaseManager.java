package com.mediatek.internal.telephony.subscription;

import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.subscription.SubscriptionDatabaseManager;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.util.function.TriConsumer;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class MtkSubscriptionDatabaseManager extends SubscriptionDatabaseManager {
    private static final String LOG_TAG = "MTK_SDM";
    private static final Map<String, Function<MtkSubscriptionInfoInternal, ?>> MTK_SUBSCRIPTION_GET_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("vonr_ui_enabled", new Function() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionDatabaseManager$$ExternalSyntheticLambda1
        @Override // java.util.function.Function
        public final Object apply(Object obj) {
            return Integer.valueOf(((MtkSubscriptionInfoInternal) obj).getEnhancedVonrShowUiEnabled());
        }
    }));
    private static final Map<String, TriConsumer<MtkSubscriptionDatabaseManager, Integer, Integer>> MTK_SUBSCRIPTION_SET_INTEGER_METHOD_MAP = Map.ofEntries(new AbstractMap.SimpleImmutableEntry("vonr_ui_enabled", new TriConsumer() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionDatabaseManager$$ExternalSyntheticLambda2
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((MtkSubscriptionDatabaseManager) obj).setEnhancedVonrShowUiEnabled(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
        }
    }));

    public MtkSubscriptionDatabaseManager(Context context, Looper looper, FeatureFlags featureFlags, SubscriptionDatabaseManager.SubscriptionDatabaseManagerCallback callback) {
        super(context, looper, featureFlags, callback);
    }

    protected SubscriptionInfoInternal.Builder onCreateSubscriptionInfoInternalBuilder(SubscriptionInfoInternal subInfo) {
        return new MtkSubscriptionInfoInternal.Builder(subInfo);
    }

    protected SubscriptionInfoInternal createSubscriptionInfoFromCursor(Cursor cursor) {
        SubscriptionInfoInternal subInfo = super.createSubscriptionInfoFromCursor(cursor);
        MtkSubscriptionInfoInternal.Builder builder = new MtkSubscriptionInfoInternal.Builder(subInfo).setEnhancedVonrShowUiEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("vonr_ui_enabled")));
        return builder.build();
    }

    protected Object getSubscriptionInfoFieldByColumnName(SubscriptionInfoInternal subInfo, String columnName) {
        if (MTK_SUBSCRIPTION_GET_METHOD_MAP.containsKey(columnName) && (subInfo instanceof MtkSubscriptionInfoInternal)) {
            return MTK_SUBSCRIPTION_GET_METHOD_MAP.get(columnName).apply((MtkSubscriptionInfoInternal) subInfo);
        }
        return super.getSubscriptionInfoFieldByColumnName(subInfo, columnName);
    }

    public void setSubscriptionProperty(int subId, String columnName, Object value) {
        int intValue;
        if (MTK_SUBSCRIPTION_SET_INTEGER_METHOD_MAP.containsKey(columnName)) {
            if (value instanceof String) {
                intValue = Integer.parseInt((String) value);
            } else if (value instanceof Integer) {
                intValue = ((Integer) value).intValue();
            } else {
                throw new ClassCastException("columnName=" + columnName + ", cannot cast " + value.getClass() + " to integer.");
            }
            MTK_SUBSCRIPTION_SET_INTEGER_METHOD_MAP.get(columnName).accept(this, Integer.valueOf(subId), Integer.valueOf(intValue));
            return;
        }
        super.setSubscriptionProperty(subId, columnName, value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnhancedVonrShowUiEnabled(int subId, int isEnhancedVonrShowUiEnabled) {
        writeDatabaseAndCacheHelper(subId, "vonr_ui_enabled", Integer.valueOf(isEnhancedVonrShowUiEnabled), new BiFunction() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionDatabaseManager$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((MtkSubscriptionInfoInternal.Builder) ((SubscriptionInfoInternal.Builder) obj)).setEnhancedVonrShowUiEnabled(((Integer) obj2).intValue());
            }
        });
    }
}
