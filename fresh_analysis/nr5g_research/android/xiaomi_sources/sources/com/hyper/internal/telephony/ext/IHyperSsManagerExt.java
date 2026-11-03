package com.hyper.internal.telephony.ext;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import java.util.ArrayList;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperSsManagerExt extends IHyperCommonModule {
    public static final IHyperSsManagerExt DEFAULT = new IHyperSsManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperSsManagerExt.1
    };
    public static final String TAG = "IHyperSsManagerExt";

    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default int getCallForwardServiceClass(Context context, int subId, int serviceClass) {
        return 1;
    }

    default void setServiceClass(int serviceClass) {
    }

    default Resources getResourcesForSubId(Context context, int subId) {
        return context.getResources();
    }

    default void updateVideoCFNotification(Context context, boolean isVCFEnable, int subId) {
    }

    default CharSequence getCfType(Context context, int subId, String sc) {
        return context.getText(R.string.CfMmi);
    }

    default ArrayList<AdnRecord> getAllFdnList(AdnRecordCache adnRecordCache, int phoneId) {
        if (adnRecordCache == null) {
            return null;
        }
        return adnRecordCache.getRecordsIfLoaded(28475);
    }

    default void unRegisterForCallBarringPwdRequiredChanged(int phoneId) {
    }

    default void changeCallBarringPassword(int phoneId, String facility, String oldPwd, String newPwd, Message onComplete) {
    }

    default boolean isSkipFdnCheckForUssd(int phoneId, String dialStr) {
        return false;
    }
}
