package com.android.services.telephony;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MiuiTelephonyGlobals {
    private final Context mContext;
    private List mTtyManagers = new ArrayList();

    public void onCreate() {
    }

    public MiuiTelephonyGlobals(Context context) {
        this.mContext = context.getApplicationContext();
    }
}
