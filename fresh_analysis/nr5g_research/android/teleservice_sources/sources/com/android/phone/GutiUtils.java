package com.android.phone;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class GutiUtils {
    private static volatile GutiUtils mInstance;
    private final Context mContext;
    private RilHook mRilHook;

    public static GutiUtils getInstance() {
        if (mInstance == null) {
            synchronized (GutiUtils.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new GutiUtils();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    public GutiUtils() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mRilHook = RilHook.init(phoneGlobals);
    }
}
