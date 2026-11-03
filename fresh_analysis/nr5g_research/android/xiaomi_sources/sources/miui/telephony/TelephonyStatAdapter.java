package miui.telephony;

import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.data.ApnSetting;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.data.TelephonyNetworkRequest;
import com.xiaomi.modem.ModemUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyStatAdapter {
    public static final String APN_NAME_KEY = "apnName";
    public static final String APN_TYPE_KEY = "apnType";
    public static final String DATA_EVALUATION = "evalution";
    public static final String DATA_EVALUATION_REASON = "evalution_reason";
    public static final String DATA_REQUEST_TYPE = "requestType";
    public static final String DATA_SETUP_DURATION_KEY = "setupDuration";
    public static final String ERROR_CODE_KEY = "errorCode";
    public static final String REASON_CODE_KEY = "reasonCode";
    private static final String TAG = "TelephonyStatAdapter";
    private static volatile TelephonyStatAdapter instance;
    static HashSet<ICallBack> sCallBack = new HashSet<>();
    static HashSet<IStaisticCallBack> sStaisticCallBack = new HashSet<>();
    static HashSet<IDataCallBack> sDataCallBack = new HashSet<>();

    public enum DataStatType {
        FAKE_CONNECTION_CN,
        DATA_CONNECTION_OK_CN,
        DATA_SETUP_OK_CN,
        DATA_SETUP_FAIL_CN,
        DATA_SETUP_REQUEST_CN,
        DATA_APN_RECOVERY_CN,
        DATA_EVALUATION_FAILED_CN,
        DATA_INTERRUPTION_RECOVERY_ACTION,
        DATA_INTERRUPTION_CAUSE
    }

    public interface IDataCallBack {
        void onDataCallListChanged(int i, int i2, String str);
    }

    public interface IStaisticCallBack {
        void onFakeConnectionRecovery(int i);

        void onFakeConnectionRecoveryTime(int i, long j);

        void onSetupDataFailRecovery(int i, int i2);

        void onSetupDataFailRecoveryTime(int i, int i2, long j);
    }

    private TelephonyStatAdapter() {
    }

    public static TelephonyStatAdapter getIstance() {
        if (instance == null) {
            synchronized (TelephonyStatAdapter.class) {
                if (instance == null) {
                    instance = new TelephonyStatAdapter();
                }
            }
        }
        return instance;
    }

    public interface ICallBack {
        default void onDataStatisticChanged(DataStatType dataStatType, int phoneId, Map<String, String> params) {
        }

        default void onDataConnectionChanged(int phoneId, int dataState) {
        }

        default void onSignalStrengthChanged(int phoneId, SignalStrength signal) {
        }

        default void onServiceStateChanged(int phoneId, ServiceState ss) {
        }

        default void onCellLocationChanged(int phoneId, CellLocation cellLocation) {
        }

        default void onPreciseCallStateChanged(int phoneId, int foregroundCallState) {
        }
    }

    public static void registerCallBack(ICallBack callback) {
        if (callback == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sCallBack.add(callback);
        }
    }

    public static void unregisterCallBack(ICallBack callback) {
        if (callback == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sCallBack.remove(callback);
        }
    }

    public static void notifyDataStatistic(DataStatType dataStatType, int phoneId, Map<String, String> params) {
        synchronized (TelephonyStatAdapter.class) {
            for (ICallBack callback : sCallBack) {
                callback.onDataStatisticChanged(dataStatType, phoneId, params);
            }
        }
    }

    public static void notifyDataConnection(int phoneId, int dataState) {
        synchronized (TelephonyStatAdapter.class) {
            for (ICallBack callback : sCallBack) {
                callback.onDataConnectionChanged(phoneId, dataState);
            }
        }
    }

    public static void notifySignalStrength(int phoneId, SignalStrength signal) {
        synchronized (TelephonyStatAdapter.class) {
            for (ICallBack callback : sCallBack) {
                callback.onSignalStrengthChanged(phoneId, signal);
            }
        }
    }

    public static void notifyServiceState(int phoneId, ServiceState ss) {
        synchronized (TelephonyStatAdapter.class) {
            for (ICallBack callback : sCallBack) {
                callback.onServiceStateChanged(phoneId, ss);
            }
        }
    }

    public static void notifyCellLocation(int phoneId, CellLocation cellLocation) {
        synchronized (TelephonyStatAdapter.class) {
            for (ICallBack callback : sCallBack) {
                callback.onCellLocationChanged(phoneId, cellLocation);
            }
        }
    }

    public static void notifyPreciseCallState(int phoneId, int foregroundCallState) {
        synchronized (TelephonyStatAdapter.class) {
            for (ICallBack callback : sCallBack) {
                callback.onPreciseCallStateChanged(phoneId, foregroundCallState);
            }
        }
    }

    public static void notifyDataSetupAction(ApnSetting apn, boolean errorStatus, String cause, int phoneId, long duration) {
        HashMap<String, String> params = new HashMap<>();
        params.put(APN_NAME_KEY, apn != null ? apn.getApnName() : ModemUtils.SIM_UNKNOWN);
        if (apn != null) {
            params.put(APN_TYPE_KEY, ApnSetting.getApnTypesStringFromBitmask(apn.getApnTypeBitmask()));
        }
        params.put(ERROR_CODE_KEY, cause);
        params.put(DATA_SETUP_DURATION_KEY, duration + "");
        if (errorStatus) {
            notifyDataStatistic(DataStatType.DATA_SETUP_FAIL_CN, phoneId, params);
        } else {
            notifyDataStatistic(DataStatType.DATA_SETUP_OK_CN, phoneId, params);
        }
    }

    public static void notifyDataEvaluationFailed(TelephonyNetworkRequest networkRequest, DataEvaluation evaluation, int phoneId, DataEvaluation.DataEvaluationReason reason) {
        HashMap<String, String> params = new HashMap<>();
        params.put(DATA_REQUEST_TYPE, networkRequest.hasCapability(12) ? ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT : ModemUtils.SIM_UNKNOWN);
        StringBuilder evaluationStr = new StringBuilder();
        List<DataEvaluation.DataDisallowedReason> mDataDisallowedReasons = evaluation.getDataDisallowedReasons();
        if (mDataDisallowedReasons.size() > 0) {
            evaluationStr.append("Data disallowed reasons:");
            for (DataEvaluation.DataDisallowedReason dataDisallowedReason : mDataDisallowedReasons) {
                evaluationStr.append(" ").append(dataDisallowedReason);
            }
        }
        params.put(DATA_EVALUATION, mDataDisallowedReasons.isEmpty() ? "Data Allowed" : evaluationStr.toString());
        params.put(DATA_EVALUATION_REASON, reason.toString());
        notifyDataStatistic(DataStatType.DATA_EVALUATION_FAILED_CN, phoneId, params);
    }

    public static void registerStaisticCallBack(IStaisticCallBack callback) {
        if (callback == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sStaisticCallBack.add(callback);
        }
    }

    public static void unregisterStaisticCallBack(IStaisticCallBack callback) {
        if (callback == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sStaisticCallBack.remove(callback);
        }
    }

    public static void notifySetupDataFailRecovery(int cause, int recoveryAction) {
        synchronized (TelephonyStatAdapter.class) {
            for (IStaisticCallBack callback : sStaisticCallBack) {
                callback.onSetupDataFailRecovery(cause, recoveryAction);
            }
        }
    }

    public static void notifySetupFailRecoveryTime(int cause, int recoveryAction, long recoveryActionTime) {
        synchronized (TelephonyStatAdapter.class) {
            for (IStaisticCallBack callback : sStaisticCallBack) {
                callback.onSetupDataFailRecoveryTime(cause, recoveryAction, recoveryActionTime);
            }
        }
    }

    public static void notifyFakeConnectionRecovery(int recoveryAction) {
        synchronized (TelephonyStatAdapter.class) {
            for (IStaisticCallBack callback : sStaisticCallBack) {
                callback.onFakeConnectionRecovery(recoveryAction);
            }
        }
    }

    public static void notifyFakeConnectionRecoveryTime(int recoveryAction, long recoveryActionTime) {
        synchronized (TelephonyStatAdapter.class) {
            for (IStaisticCallBack callback : sStaisticCallBack) {
                callback.onFakeConnectionRecoveryTime(recoveryAction, recoveryActionTime);
            }
        }
    }

    public static void registerDataCallBack(IDataCallBack callback) {
        if (callback == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sDataCallBack.add(callback);
        }
    }

    public static void unregisterDataCallBack(IDataCallBack callback) {
        if (callback == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sDataCallBack.remove(callback);
        }
    }

    public static void notifyDataCallList(int phoneId, int cause, String dnn) {
        synchronized (TelephonyStatAdapter.class) {
            for (IDataCallBack callback : sDataCallBack) {
                callback.onDataCallListChanged(phoneId, cause, dnn);
            }
        }
    }
}
