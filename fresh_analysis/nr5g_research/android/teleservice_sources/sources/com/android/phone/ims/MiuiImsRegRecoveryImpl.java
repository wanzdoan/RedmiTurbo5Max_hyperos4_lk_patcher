package com.android.phone.ims;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.phone.FiveGManagerBase;
import com.android.phone.utils.ModemUtils;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class MiuiImsRegRecoveryImpl extends MiuiImsRegRecoveryBase {
    private static String LOG_TAG = "MiuiImsRegRecoveryImpl";
    private ModemUtils mModemUtils;

    MiuiImsRegRecoveryImpl(Context context, ImsPhone imsPhone) {
        super(context, imsPhone);
    }

    public void setModemUtils(ModemUtils modemUtils) {
        this.mModemUtils = modemUtils;
    }

    public int setNvConfig() {
        ByteBuffer byteBuffer;
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onHookNVCMD", 3, 0, new byte[0]);
            if (objCallHook instanceof ByteBuffer) {
                byteBuffer = (ByteBuffer) objCallHook;
            } else {
                log("setNvConfig: onHookNVCMD return type not ByteBuffer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                byteBuffer = null;
            }
        } else {
            byteBuffer = null;
        }
        if (byteBuffer != null) {
            int i = byteBuffer.getInt();
            try {
                if (i == -1) {
                    log("NV71527_SET_AUTH_SCHEME, read failed");
                } else if (i == 0) {
                    log("NV71527_SET_AUTH_SCHEME, read success but set failed");
                } else if (i == 1) {
                    log("NV71527_SET_AUTH_SCHEME, set success");
                    FiveGManagerBase.getInstance().refreshMbn(this.mImsPhone.getPhoneId());
                } else if (i == 2) {
                    log("NV71527_SET_AUTH_SCHEME, iAuth_SecType = 136");
                } else {
                    log("NV71527_SET_AUTH_SCHEME, Unexpected value");
                }
            } catch (Exception unused) {
                log("NV71527_SET_AUTH_SCHEME, Exception in LteReAttach");
            }
            return i;
        }
        log("NV71527_SET_AUTH_SCHEME, fail");
        return -2;
    }

    public int setNVConfigForVoPS() {
        ByteBuffer byteBuffer;
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils != null) {
            Object objCallHook = modemUtils.callHook("onHookNVCMD", 4, 0, new byte[0]);
            if (objCallHook instanceof ByteBuffer) {
                byteBuffer = (ByteBuffer) objCallHook;
            } else {
                log("setNVConfigForVoPS: onHookNVCMD return type not ByteBuffer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
                byteBuffer = null;
            }
        } else {
            byteBuffer = null;
        }
        if (byteBuffer != null) {
            int i = byteBuffer.getInt();
            try {
                if (i == -1) {
                    log("NV71527_ADD_NO_VOPS_RAT, read failed");
                } else if (i == 0) {
                    log("NV71527_ADD_NO_VOPS_RAT read succeeded, VoPS not configured, add_no_vops_rat failed");
                } else if (i == 1) {
                    log("NV71527_ADD_NO_VOPS_RAT, set success");
                    FiveGManagerBase.getInstance().refreshMbn(this.mImsPhone.getPhoneId());
                } else if (i == 2) {
                    log("NV71527_ADD_NO_VOPS_RAT read succeeded, VoPS configured, no add_no_vops_rat needed");
                } else {
                    log("NV71527_ADD_NO_VOPS_RAT, Unexpected value");
                }
            } catch (Exception unused) {
                log("NV71527_ADD_NO_VOPS_RAT, Exception in LteReAttach");
            }
            return i;
        }
        log("NV71527_ADD_NO_VOPS_RAT, fail");
        return -2;
    }

    public boolean recoveryMBN() {
        ModemUtils modemUtils = this.mModemUtils;
        if (modemUtils == null) {
            log("recoveryMBN onHookMbnReactivateSync failed: mModemUtils is null");
            return false;
        }
        Object objCallHook = modemUtils.callHook("onHookMbnReactivateSync", Integer.valueOf(this.mImsPhone.getPhoneId() + 1));
        if (objCallHook instanceof ByteBuffer) {
            log("recoveryMBN onHookMbnReactivateSync return: " + ((ByteBuffer) objCallHook).getInt());
            return true;
        }
        log("recoveryMBN: onHookMbnReactivateSync return type not ByteBuffer, actual: ".concat(objCallHook != null ? objCallHook.getClass().getName() : "null"));
        log("recoveryMBN onHookMbnReactivateSync return: false");
        return false;
    }

    protected void log(String str) {
        Rlog.d(LOG_TAG + "[" + this.mImsPhone.getPhoneId() + "]", str);
    }
}
