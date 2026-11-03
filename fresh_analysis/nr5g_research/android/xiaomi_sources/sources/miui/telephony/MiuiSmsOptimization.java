package miui.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.SystemProperties;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiSmsOptimization extends Handler {
    protected static final int EVENT_SMS_OPTIMIZATION_TIMEOUT = 1;
    public static final int OPTIMIZATION_INIT = 0;
    public static final int OPTIMIZATION_READY = 2;
    public static final int OPTIMIZATION_START = 1;
    public static final int SMS_OPTIMIZATION_TYPE_DONE = 2;
    public static final int SMS_OPTIMIZATION_TYPE_ENABLE = 1;
    public static final int SMS_OPTIMIZATION_TYPE_INIT = 0;
    private static final String TAG = "MiuiSmsOptimization";
    protected final Context mContext;
    protected MiuiSmsOptimizationController mMiuiSmsOptimizationController;
    protected Phone mPhone;
    public int mStatus = 0;

    public abstract void init(Phone phone, String str, boolean z);

    protected abstract boolean isNeedAndstartOptimization(Phone phone, String str, boolean z, SMSDispatcher.SmsTracker smsTracker);

    public abstract void onFinish(Phone phone, String str, boolean z);

    public MiuiSmsOptimization(Phone phone, MiuiSmsOptimizationController miuiSmsOptimizationController) {
        this.mPhone = phone;
        this.mContext = phone.getContext();
        this.mMiuiSmsOptimizationController = miuiSmsOptimizationController;
    }

    public static boolean isSupportOptimization(Phone phone) {
        if (TelephonyOptCloudController.CLOUD_CONTROLLER_SMS_SMSC_DISABLED) {
            return false;
        }
        return SystemProperties.getBoolean("persist.sys.miui_optimization", true);
    }

    public static boolean isCuSim(Phone phone) {
        return ServiceProviderUtils.isChinaUnicom(phone.getOperatorNumeric());
    }

    @Override // android.os.Handler
    public String toString() {
        return "[" + getClass().getName() + ", status = " + this.mStatus + "]";
    }
}
