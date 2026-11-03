package com.android.phone.roaming;

import android.content.Context;
import com.android.phone.PhoneGlobals;
import com.ot.pubsub.a.a;
import miui.os.Build;

/* JADX INFO: loaded from: classes.dex */
public abstract class RoamingParametersManager {
    private static final byte[] NV74319_ROAMING_BYTE_ARRAY = {1, 60, 0};
    private static final byte[] NV73824_ROAMING_BYTE_ARRAY = {3, 0, 0, 0};
    private static final String[] ARRAY_KEY_ROAMING_PARAMETERS_INFORMATION_LIST = {a.L, "plmn", "isroaming", "nvpath", a.p};
    private static Context mContext = PhoneGlobals.getInstance();

    public static RoamingParametersManager getInstance() {
        boolean z = Build.IS_INTERNATIONAL_BUILD;
        return null;
    }
}
