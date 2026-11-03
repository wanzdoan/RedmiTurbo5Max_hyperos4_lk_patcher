package com.android.internal.telephony;

import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.data.ApnSetting;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.data.TelephonyNetworkRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    private static volatile TelephonyStatAdapter instance;
    static HashSet sCallBack = new HashSet();
    static HashSet sStaisticCallBack = new HashSet();
    static HashSet sDataCallBack = new HashSet();

    public enum DataStatType {
        FAKE_CONNECTION_CN,
        DATA_CONNECTION_OK_CN,
        DATA_SETUP_OK_CN,
        DATA_SETUP_FAIL_CN,
        DATA_APN_RECOVERY_CN,
        DATA_EVALUATION_FAILED_CN,
        DATA_INTERRUPTION_RECOVERY_ACTION,
        DATA_INTERRUPTION_CAUSE,
        DATA_PERMANENT_FAIL,
        DATA_PERMANENT_FAIL_RESET,
        DATA_SETUP_RETRY_MAX,
        DATA_RETRY_RULE_RESET
    }

    public interface ICallBack {
        default void onCellLocationChanged(int i, CellLocation cellLocation) {
        }

        default void onDataConnectionChanged(int i, int i2) {
        }

        default void onDataStatisticChanged(DataStatType dataStatType, int i, Map<String, Object> map) {
        }

        default void onPreciseCallStateChanged(int i, int i2) {
        }

        default void onServiceStateChanged(int i, ServiceState serviceState) {
        }

        default void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
        }
    }

    public interface IDataCallBack {
        void onDataCallListChanged(int i, int i2, String str);
    }

    public interface IStaisticCallBack {
        void onSetupDataFailRecovery(int i);
    }

    private TelephonyStatAdapter() {
    }

    public static TelephonyStatAdapter getIstance() {
        if (instance == null) {
            synchronized (TelephonyStatAdapter.class) {
                try {
                    if (instance == null) {
                        instance = new TelephonyStatAdapter();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return instance;
    }

    public static void registerCallBack(ICallBack iCallBack) {
        if (iCallBack == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sCallBack.add(iCallBack);
        }
    }

    public static void unregisterCallBack(ICallBack iCallBack) {
        if (iCallBack == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sCallBack.remove(iCallBack);
        }
    }

    public static void notifyDataStatistic(DataStatType dataStatType, int i, Map<String, Object> map) {
        synchronized (TelephonyStatAdapter.class) {
            try {
                Iterator it = sCallBack.iterator();
                while (it.hasNext()) {
                    ((ICallBack) it.next()).onDataStatisticChanged(dataStatType, i, map);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void notifyDataConnection(int i, int i2) {
        synchronized (TelephonyStatAdapter.class) {
            try {
                Iterator it = sCallBack.iterator();
                while (it.hasNext()) {
                    ((ICallBack) it.next()).onDataConnectionChanged(i, i2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void notifySignalStrength(int i, SignalStrength signalStrength) {
        synchronized (TelephonyStatAdapter.class) {
            try {
                Iterator it = sCallBack.iterator();
                while (it.hasNext()) {
                    ((ICallBack) it.next()).onSignalStrengthChanged(i, signalStrength);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void notifyServiceState(int i, ServiceState serviceState) {
        synchronized (TelephonyStatAdapter.class) {
            try {
                Iterator it = sCallBack.iterator();
                while (it.hasNext()) {
                    ((ICallBack) it.next()).onServiceStateChanged(i, serviceState);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void notifyCellLocation(int i, CellLocation cellLocation) {
        synchronized (TelephonyStatAdapter.class) {
            try {
                Iterator it = sCallBack.iterator();
                while (it.hasNext()) {
                    ((ICallBack) it.next()).onCellLocationChanged(i, cellLocation);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void notifyPreciseCallState(int i, int i2) {
        synchronized (TelephonyStatAdapter.class) {
            try {
                Iterator it = sCallBack.iterator();
                while (it.hasNext()) {
                    ((ICallBack) it.next()).onPreciseCallStateChanged(i, i2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void notifyDataSetupAction(ApnSetting apnSetting, boolean z, String str, int i, long j) {
        HashMap map = new HashMap();
        map.put(APN_NAME_KEY, apnSetting != null ? apnSetting.getApnName() : "unknown");
        if (apnSetting != null) {
            map.put(APN_TYPE_KEY, ApnSetting.getApnTypesStringFromBitmask(apnSetting.getApnTypeBitmask()));
        }
        map.put(ERROR_CODE_KEY, str);
        map.put(DATA_SETUP_DURATION_KEY, j + "");
        if (z) {
            notifyDataStatistic(DataStatType.DATA_SETUP_FAIL_CN, i, map);
        } else {
            notifyDataStatistic(DataStatType.DATA_SETUP_OK_CN, i, map);
        }
    }

    public static void notifyDataEvaluationFailed(TelephonyNetworkRequest telephonyNetworkRequest, DataEvaluation dataEvaluation, int i, DataEvaluation.DataEvaluationReason dataEvaluationReason) {
        HashMap map = new HashMap();
        map.put(DATA_REQUEST_TYPE, telephonyNetworkRequest.hasCapability(12) ? AccessNetworksManager.IWLAN_OPERATION_MODE_DEFAULT : "unknown");
        StringBuilder sb = new StringBuilder();
        List<DataEvaluation.DataDisallowedReason> dataDisallowedReasons = dataEvaluation.getDataDisallowedReasons();
        if (dataDisallowedReasons.size() > 0) {
            sb.append("Data disallowed reasons:");
            for (DataEvaluation.DataDisallowedReason dataDisallowedReason : dataDisallowedReasons) {
                sb.append(" ");
                sb.append(dataDisallowedReason);
            }
        }
        map.put(DATA_EVALUATION, dataDisallowedReasons.isEmpty() ? "Data Allowed" : sb.toString());
        map.put(DATA_EVALUATION_REASON, dataEvaluationReason.toString());
        notifyDataStatistic(DataStatType.DATA_EVALUATION_FAILED_CN, i, map);
    }

    public static void registerStaisticCallBack(IStaisticCallBack iStaisticCallBack) {
        if (iStaisticCallBack == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sStaisticCallBack.add(iStaisticCallBack);
        }
    }

    public static void unregisterStaisticCallBack(IStaisticCallBack iStaisticCallBack) {
        if (iStaisticCallBack == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sStaisticCallBack.remove(iStaisticCallBack);
        }
    }

    public static void notifySetupDataFailRecovery(int i) {
        synchronized (TelephonyStatAdapter.class) {
            try {
                Iterator it = sStaisticCallBack.iterator();
                while (it.hasNext()) {
                    ((IStaisticCallBack) it.next()).onSetupDataFailRecovery(i);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void registerDataCallBack(IDataCallBack iDataCallBack) {
        if (iDataCallBack == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sDataCallBack.add(iDataCallBack);
        }
    }

    public static void unregisterDataCallBack(IDataCallBack iDataCallBack) {
        if (iDataCallBack == null) {
            return;
        }
        synchronized (TelephonyStatAdapter.class) {
            sDataCallBack.remove(iDataCallBack);
        }
    }

    public static void notifyDataCallList(int i, int i2, String str) {
        synchronized (TelephonyStatAdapter.class) {
            try {
                Iterator it = sDataCallBack.iterator();
                while (it.hasNext()) {
                    ((IDataCallBack) it.next()).onDataCallListChanged(i, i2, str);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
