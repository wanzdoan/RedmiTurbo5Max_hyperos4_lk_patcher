package miui.telephony;

import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSmsSmscOptimization extends MiuiSmsOptimization {
    protected static final int MIN_SMSC_LENGTH = 7;
    private static final String TAG = "MiuiSmsSmscOptimization";

    public MiuiSmsSmscOptimization(Phone phone, MiuiSmsOptimizationController miuiSmsOptimizationController) {
        super(phone, miuiSmsOptimizationController);
        this.mStatus = 0;
    }

    @Override // miui.telephony.MiuiSmsOptimization
    public void init(Phone phone, String nowSmscAddress, boolean isIms) {
        if (isCuSim(phone) && isIms) {
            return;
        }
        String saveSmsc = SystemProperties.get("persist.radio.support.sendsmsc" + phone.getSubId(), "");
        if (!TextUtils.isEmpty(nowSmscAddress) && TextUtils.isEmpty(saveSmsc)) {
            Log.d(TAG, "saveSuccessSmsc.");
            SystemProperties.set("persist.radio.support.sendsmsc" + phone.getSubId(), nowSmscAddress);
        }
    }

    @Override // miui.telephony.MiuiSmsOptimization
    public boolean isNeedAndstartOptimization(Phone phone, String nowSmscAddress, boolean isIms, SMSDispatcher.SmsTracker smsTracker) {
        if (!isSupportSmsSmscOptimization(phone)) {
            return false;
        }
        if (isCuSim(phone) && isIms) {
            return false;
        }
        String saveSmsc = SystemProperties.get("persist.radio.support.sendsmsc" + phone.getSubId(), "");
        if (TextUtils.isEmpty(saveSmsc) || saveSmsc.length() <= 7 || (!TextUtils.isEmpty(nowSmscAddress) && saveSmsc.equals(nowSmscAddress))) {
            return false;
        }
        this.mStatus = 2;
        return true;
    }

    @Override // miui.telephony.MiuiSmsOptimization
    public void onFinish(Phone phone, String nowSmscAddress, boolean isSuccessed) {
        if (this.mStatus != 0) {
            if (isSuccessed) {
                String saveSmsc = SystemProperties.get("persist.radio.support.sendsmsc" + phone.getSubId(), "");
                if (!TextUtils.isEmpty(saveSmsc) && !saveSmsc.equals(nowSmscAddress)) {
                    Log.d(TAG, "setSmscAddress.");
                    phone.setSmscAddress(saveSmsc, (Message) null);
                }
            }
            this.mStatus = 0;
        }
    }

    public static String replaceSmscAddr(Phone phone, String nowSmsc, String scAddr) {
        String saveSmsc = SystemProperties.get("persist.radio.support.sendsmsc" + phone.getSubId(), "");
        if (!TextUtils.isEmpty(saveSmsc) && (TextUtils.isEmpty(nowSmsc) || !saveSmsc.equals(nowSmsc))) {
            Log.d(TAG, "replaceSmscAddr saveSmsc = " + saveSmsc + ", nowSmsc = " + nowSmsc + ", scAddr = " + scAddr);
            return saveSmsc;
        }
        return scAddr;
    }

    public static boolean isSupportSmsSmscOptimization(Phone phone) {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_miui_sms_optimization");
    }

    public boolean containsSmscOptimization(Phone phone, SMSDispatcher.SmsTracker tracker) {
        if (tracker.mSmsOptimizationStatus != 0 && tracker.mSmsOptimizationList != null && !tracker.mSmsOptimizationList.isEmpty()) {
            for (int i = 0; i < tracker.mSmsOptimizationList.size(); i++) {
                MiuiSmsOptimization smsOptimization = (MiuiSmsOptimization) tracker.mSmsOptimizationList.get(i);
                if (smsOptimization instanceof MiuiSmsSmscOptimization) {
                    return true;
                }
            }
        }
        return false;
    }
}
