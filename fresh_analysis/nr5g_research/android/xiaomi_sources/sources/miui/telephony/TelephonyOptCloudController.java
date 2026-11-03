package miui.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.InternalTelephonyUtils;
import com.android.internal.telephony.MiuiCallControllerImpl;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import miui.os.Build;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyOptCloudController extends Handler {
    private static final String ACTION_CLOUD_TELE_FEATURE_INFO_CHANGED = "com.android.phone.intent.action.CLOUD_TELE_FEATURE_INFO_CHANGED";
    public static boolean CLOUD_CONTROLLER_CALL_VTQUALITY_DISABLED = false;
    public static boolean CLOUD_CONTROLLER_CELL_INFO_CACHE_DISABLED = false;
    public static boolean CLOUD_CONTROLLER_NETWORK_LOCKED_DISABLED = false;
    public static boolean CLOUD_CONTROLLER_NETWORK_NEIGHBOR_DISABLED = false;
    public static boolean CLOUD_CONTROLLER_NITZ_TIMEZONE_PREFERRED_DISABLE = false;
    public static boolean CLOUD_CONTROLLER_OOS_RESET_RILD_DISABLED = false;
    public static boolean CLOUD_CONTROLLER_RILD_STALL_DISABLE = false;
    public static boolean CLOUD_CONTROLLER_SIGNAL_STRENGTH_REPORT_OPTIMIZATION_ENABLED = false;
    public static boolean CLOUD_CONTROLLER_SMS_SMSC_DISABLED = false;
    public static boolean CLOUD_CONTROLLER_UPDATE_SIGNAL_PRIMARY_NSA_DISABLED = false;
    private static final int EVENT_CLOUD_STATE_CHANGED = 1;
    private static final String KEY_NITZ_TIMEZONE_PREFERRED = "nitz_timezone_preferred";
    private static final String KEY_OPTIMIZATIOND_OOS_RESET_RILD = "optimization_oos_reset_rild";
    private static final String KEY_OPTIMIZATIOND_RILD_STALL = "optimization_rild_stall";
    private static final String KEY_OPTIMIZATION_CALL_VTQUALITY = "optimization_call_vtquality";
    private static final String KEY_OPTIMIZATION_CELL_INFO_LIST = "optimization_cell_info_list";
    private static final String KEY_OPTIMIZATION_NETWORK_LOCKED = "optimization_network_locked";
    private static final String KEY_OPTIMIZATION_NETWORK_NEIGHBOR = "optimization_network_neighbor";
    private static final String KEY_OPTIMIZATION_SIGNAL_STRENGTH_REPORT = "optimization_signal_strength_report";
    private static final String KEY_OPTIMIZATION_SMS_SMSC = "optimization_sms_smsc";
    private static final String KEY_OPTIMIZATION_UPDATE_SIGNAL_PRIMARY_NSA = "optimization_update_signal_primary_nsa";
    private static final String TAG = "TelephonyOptCloudController";
    private static TelephonyOptCloudController mInstance;
    private Context mContext;
    private static boolean mSupportNeighboringSignal = false;
    private static boolean mSupportScreenSignal = false;
    private static boolean mSupportUpdateSignalPrimaryInNsa = false;
    private static boolean mSupportOosResetRild = false;
    private static boolean mSupportNitzTimeZonePreferred = false;
    private List<CloudInfoListener> mCloudInfoListener = new ArrayList(1);
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: miui.telephony.TelephonyOptCloudController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TelephonyOptCloudController.log("onReceive intent: " + action);
            if (action.equals(TelephonyOptCloudController.ACTION_CLOUD_TELE_FEATURE_INFO_CHANGED)) {
                TelephonyOptCloudController.this.sendEmptyMessage(1);
            }
        }
    };

    public interface CloudInfoListener {
        void onCloudInfoChange();
    }

    public void init(Context context) {
        this.mContext = context;
        Log.d(TAG, "TelephonyOptCloudController init");
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CLOUD_TELE_FEATURE_INFO_CHANGED);
        this.mContext.registerReceiver(this.mReceiver, filter, 2);
    }

    public static synchronized TelephonyOptCloudController getInstance() {
        if (mInstance == null) {
            mInstance = new TelephonyOptCloudController();
        }
        return mInstance;
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                log("cloud controller changed");
                updateCloudControlInfo();
                break;
            default:
                log("into default");
                break;
        }
    }

    private void updateCloudControlInfo() {
        getOptimizationState();
        if (MiuiCallControllerImpl.Processor.supportVtQualityProperty()) {
            CLOUD_CONTROLLER_CALL_VTQUALITY_DISABLED = isFeatureDisabledByCloud(KEY_OPTIMIZATION_CALL_VTQUALITY);
        }
        if (!Build.IS_INTERNATIONAL_BUILD) {
            CLOUD_CONTROLLER_SMS_SMSC_DISABLED = isFeatureDisabledByCloud(KEY_OPTIMIZATION_SMS_SMSC);
        }
        if (mSupportScreenSignal) {
            CLOUD_CONTROLLER_NETWORK_LOCKED_DISABLED = isFeatureDisabledByCloud(KEY_OPTIMIZATION_NETWORK_LOCKED);
        }
        if (mSupportNeighboringSignal) {
            CLOUD_CONTROLLER_NETWORK_NEIGHBOR_DISABLED = isFeatureDisabledByCloud(KEY_OPTIMIZATION_NETWORK_NEIGHBOR);
        }
        if (!Build.IS_INTERNATIONAL_BUILD) {
            CLOUD_CONTROLLER_CELL_INFO_CACHE_DISABLED = isFeatureDisabledByCloud(KEY_OPTIMIZATION_CELL_INFO_LIST);
            CLOUD_CONTROLLER_SIGNAL_STRENGTH_REPORT_OPTIMIZATION_ENABLED = isFeatureEnabledByCloud(KEY_OPTIMIZATION_SIGNAL_STRENGTH_REPORT);
        }
        if (mSupportUpdateSignalPrimaryInNsa) {
            CLOUD_CONTROLLER_UPDATE_SIGNAL_PRIMARY_NSA_DISABLED = isFeatureDisabledByCloud(KEY_OPTIMIZATION_UPDATE_SIGNAL_PRIMARY_NSA);
        }
        if (mSupportOosResetRild) {
            CLOUD_CONTROLLER_OOS_RESET_RILD_DISABLED = isFeatureDisabledByCloud(KEY_OPTIMIZATIOND_OOS_RESET_RILD);
        }
        if (InternalTelephonyUtils.isSupportRadioStall()) {
            CLOUD_CONTROLLER_RILD_STALL_DISABLE = isFeatureDisabledByCloud(KEY_OPTIMIZATIOND_RILD_STALL);
        }
        if (mSupportNitzTimeZonePreferred) {
            CLOUD_CONTROLLER_NITZ_TIMEZONE_PREFERRED_DISABLE = isFeatureDisabledByCloud(KEY_NITZ_TIMEZONE_PREFERRED);
        }
        notifyCloudInfoChange();
    }

    private static boolean isFeatureDisabledByCloud(String featureType) {
        try {
            Class<?> cls = Class.forName("miui.telephony.TelephonyManagerEx");
            Method declaredMethod = cls.getDeclaredMethod("getDefault", null);
            declaredMethod.setAccessible(true);
            Object object = declaredMethod.invoke(null, null);
            Method method = cls.getDeclaredMethod("isFeatureDisabledByCloud", String.class);
            method.setAccessible(true);
            return ((Boolean) method.invoke(object, featureType)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isFeatureEnabledByCloud(String featureType) {
        try {
            Class<?> cls = Class.forName("miui.telephony.TelephonyManagerEx");
            Method declaredMethod = cls.getDeclaredMethod("getDefault", null);
            declaredMethod.setAccessible(true);
            Object object = declaredMethod.invoke(null, null);
            Method method = cls.getDeclaredMethod("isFeatureEnabledByCloud", String.class);
            method.setAccessible(true);
            return ((Boolean) method.invoke(object, featureType)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void getOptimizationState() {
        Phone phone = PhoneFactory.getPhone(0);
        if (phone == null) {
            return;
        }
        mSupportScreenSignal = !Build.IS_INTERNATIONAL_BUILD && phone.getContext().getResources().getBoolean(285540420);
        mSupportNeighboringSignal = !Build.IS_INTERNATIONAL_BUILD && phone.getContext().getResources().getBoolean(285540427);
        mSupportUpdateSignalPrimaryInNsa = !Build.IS_STABLE_VERSION || FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_NSA_SIGNAL_OPTIMIZATION);
        mSupportOosResetRild = !Build.IS_STABLE_VERSION || FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_OOS_RILD_SELF_RECOVERY);
        mSupportNitzTimeZonePreferred = FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_NITZ_TIMEZONE_PREFERRED);
    }

    public void addCloudInfoListener(CloudInfoListener l) {
        synchronized (this.mCloudInfoListener) {
            if (l != null) {
                this.mCloudInfoListener.add(l);
            }
        }
    }

    public void removeCloudInfoListener(CloudInfoListener l) {
        synchronized (this.mCloudInfoListener) {
            if (l != null) {
                this.mCloudInfoListener.remove(l);
            }
        }
    }

    public void notifyCloudInfoChange() {
        synchronized (this.mCloudInfoListener) {
            for (CloudInfoListener l : this.mCloudInfoListener) {
                l.onCloudInfoChange();
            }
        }
    }
}
