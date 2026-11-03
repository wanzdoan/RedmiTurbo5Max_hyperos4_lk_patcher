package com.android.internal.telephony;

import android.os.Handler;
import android.telephony.emergency.EmergencyNumber;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperEmergencyNumberTracker extends IHyperCommonModule {
    public static final IHyperEmergencyNumberTracker DEFAULT = new IHyperEmergencyNumberTracker() { // from class: com.android.internal.telephony.IHyperEmergencyNumberTracker.1
    };
    public static final String TAG = "IHyperEmergencyNumberTracker";

    default List<EmergencyNumber> filterDeprecatedEmergencyNumbers(List<EmergencyNumber> list) {
        return list;
    }

    default void registerForEmergencyNumberListChanged(Handler handler, int i, Object obj) {
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperEmergencyNumberTracker;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperEmergencyNumberTracker getDefault() {
        return DEFAULT;
    }
}
