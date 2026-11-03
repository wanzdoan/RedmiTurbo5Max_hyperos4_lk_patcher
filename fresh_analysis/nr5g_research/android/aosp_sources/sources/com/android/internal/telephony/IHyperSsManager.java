package com.android.internal.telephony;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccConstants;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperSsManager extends IHyperCommonModule {
    public static final IHyperSsManager DEFAULT = new IHyperSsManager() { // from class: com.android.internal.telephony.IHyperSsManager.1
    };
    public static final String TAG = "IHyperSsManager";

    default void changeCallBarringPassword(int i, String str, String str2, String str3, Message message) {
    }

    default int getCallForwardServiceClass(Context context, int i, int i2) {
        return 1;
    }

    default boolean isSkipFdnCheckForUssd(int i, String str) {
        return false;
    }

    default void setServiceClass(int i) {
    }

    default void unRegisterForCallBarringPwdRequiredChanged(int i) {
    }

    default void updateVideoCFNotification(Context context, boolean z, int i) {
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperSsManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default Resources getResourcesForSubId(Context context, int i) {
        return context.getResources();
    }

    default CharSequence getCfType(Context context, int i, String str) {
        return context.getText(R.string.CfMmi);
    }

    default ArrayList<AdnRecord> getAllFdnList(AdnRecordCache adnRecordCache, int i) {
        if (adnRecordCache == null) {
            return null;
        }
        return adnRecordCache.getRecordsIfLoaded(IccConstants.EF_FDN);
    }
}
