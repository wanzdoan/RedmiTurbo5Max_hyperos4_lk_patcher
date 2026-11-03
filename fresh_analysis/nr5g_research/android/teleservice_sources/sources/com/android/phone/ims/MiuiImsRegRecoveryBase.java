package com.android.phone.ims;

import android.content.Context;
import android.os.SystemProperties;
import com.android.ims.ImsManager;
import com.android.internal.telephony.imsphone.ImsPhone;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiImsRegRecoveryBase {
    protected Context mContext;
    protected ImsManager mImsManager = null;
    protected ImsPhone mImsPhone;

    public MiuiImsRegRecoveryBase(Context context, ImsPhone imsPhone) {
        this.mContext = context;
        this.mImsPhone = imsPhone;
    }

    public int resetEnhanced4gLteModeSetting() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            imsManager = ImsManager.getInstance(this.mContext, this.mImsPhone.getPhoneId());
        }
        if (imsManager == null || !imsManager.isEnhanced4gLteModeSettingEnabledByUser()) {
            return 0;
        }
        SystemProperties.set("ril.ims.opt.reset.volte", "true");
        imsManager.setEnhanced4gLteModeSetting(false);
        imsManager.setEnhanced4gLteModeSetting(true);
        SystemProperties.set("ril.ims.opt.reset.volte", "false");
        return 1;
    }
}
