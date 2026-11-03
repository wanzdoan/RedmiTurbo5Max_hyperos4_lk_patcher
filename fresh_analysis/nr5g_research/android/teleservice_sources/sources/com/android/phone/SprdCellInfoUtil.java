package com.android.phone;

import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.OperatorInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public abstract class SprdCellInfoUtil {
    private static List mForbiddenPlmns;
    private static final Object sLock = new Object();
    private static List operatorInfoList = new ArrayList();
    private static List newOperatorInfoList = new ArrayList();
    private static boolean stateIsCurrer = false;
    private static String mState = "unknown";

    public static List convertCellInfoToOperatorInfo(List list) {
        synchronized (sLock) {
            try {
                if (list == null) {
                    return new ArrayList(operatorInfoList);
                }
                if (operatorInfoList.size() > 0) {
                    if (newOperatorInfoList.size() > 0) {
                        newOperatorInfoList.clear();
                    }
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        OperatorInfo operatorInfoFromCellInfo = getOperatorInfoFromCellInfo((CellInfo) it.next());
                        if (operatorInfoFromCellInfo != null) {
                            int i = 0;
                            while (true) {
                                if (i < operatorInfoList.size()) {
                                    OperatorInfo operatorInfo = (OperatorInfo) operatorInfoList.get(i);
                                    if (isSameOperatorInfo(operatorInfo, operatorInfoFromCellInfo)) {
                                        OperatorInfo.State state = operatorInfoFromCellInfo.getState();
                                        OperatorInfo.State state2 = OperatorInfo.State.CURRENT;
                                        if (state == state2 && operatorInfo.getState() != state2) {
                                            operatorInfoList.set(i, operatorInfoFromCellInfo);
                                            break;
                                        }
                                        break;
                                    }
                                    i++;
                                } else {
                                    newOperatorInfoList.add(operatorInfoFromCellInfo);
                                    operatorInfoList.add(operatorInfoFromCellInfo);
                                    break;
                                }
                            }
                        }
                    }
                    return new ArrayList(operatorInfoList);
                }
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    OperatorInfo operatorInfoFromCellInfo2 = getOperatorInfoFromCellInfo((CellInfo) it2.next());
                    if (operatorInfoFromCellInfo2 != null) {
                        operatorInfoList.add(operatorInfoFromCellInfo2);
                    }
                }
                return new ArrayList(operatorInfoList);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static OperatorInfo getOperatorInfoFromCellInfo(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoLte) {
            Log.d("SprdCellInfoUtil", "CellInfoLte");
            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
            CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
            if (cellIdentity == null) {
                return null;
            }
            OperatorInfo operatorInfo = new OperatorInfo((String) cellIdentity.getOperatorAlphaLong(), (String) cellIdentity.getOperatorAlphaShort(), cellIdentity.getMobileNetworkOperator(), cellInfoLte.isRegistered() ? "current" : "unknown");
            operatorInfo.setRadioTech("14");
            return operatorInfo;
        }
        if (cellInfo instanceof CellInfoWcdma) {
            Log.d("SprdCellInfoUtil", "CellInfoWcdma");
            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
            CellIdentityWcdma cellIdentity2 = cellInfoWcdma.getCellIdentity();
            if (cellIdentity2 == null) {
                return null;
            }
            OperatorInfo operatorInfo2 = new OperatorInfo((String) cellIdentity2.getOperatorAlphaLong(), (String) cellIdentity2.getOperatorAlphaShort(), cellIdentity2.getMobileNetworkOperator(), cellInfoWcdma.isRegistered() ? "current" : "unknown");
            operatorInfo2.setRadioTech("3");
            return operatorInfo2;
        }
        if (cellInfo instanceof CellInfoGsm) {
            Log.d("SprdCellInfoUtil", "CellInfoGsm");
            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
            CellIdentityGsm cellIdentity3 = cellInfoGsm.getCellIdentity();
            if (cellIdentity3 == null) {
                return null;
            }
            OperatorInfo operatorInfo3 = new OperatorInfo((String) cellIdentity3.getOperatorAlphaLong(), (String) cellIdentity3.getOperatorAlphaShort(), cellIdentity3.getMobileNetworkOperator(), cellInfoGsm.isRegistered() ? "current" : "unknown");
            operatorInfo3.setRadioTech("1");
            return operatorInfo3;
        }
        if (cellInfo instanceof CellInfoCdma) {
            Log.d("SprdCellInfoUtil", "CellInfoCdma");
            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
            CellIdentityCdma cellIdentity4 = cellInfoCdma.getCellIdentity();
            if (cellIdentity4 == null) {
                return null;
            }
            OperatorInfo operatorInfo4 = new OperatorInfo((String) cellIdentity4.getOperatorAlphaLong(), (String) cellIdentity4.getOperatorAlphaShort(), cellIdentity4.getPlmn(), cellInfoCdma.isRegistered() ? "current" : "unknown");
            operatorInfo4.setRadioTech("2");
            return operatorInfo4;
        }
        if (cellInfo instanceof CellInfoNr) {
            Log.d("SprdCellInfoUtil", "CellInfoNr");
            CellInfoNr cellInfoNr = (CellInfoNr) cellInfo;
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();
            if (cellIdentityNr == null) {
                return null;
            }
            OperatorInfo operatorInfo5 = new OperatorInfo((String) cellIdentityNr.getOperatorAlphaLong(), (String) cellIdentityNr.getOperatorAlphaShort(), cellIdentityNr.getPlmn(), cellInfoNr.isRegistered() ? "current" : "unknown");
            operatorInfo5.setRadioTech("20");
            return operatorInfo5;
        }
        Log.e("SprdCellInfoUtil", "Invalid CellInfo type");
        return new OperatorInfo("", "", "");
    }

    public static boolean isSameOperatorInfo(OperatorInfo operatorInfo, OperatorInfo operatorInfo2) {
        return operatorInfo != null && operatorInfo2 != null && Objects.equals(operatorInfo.getOperatorAlphaLong(), operatorInfo2.getOperatorAlphaLong()) && Objects.equals(operatorInfo.getOperatorAlphaShort(), operatorInfo2.getOperatorAlphaShort()) && Objects.equals(operatorInfo.getOperatorNumeric(), operatorInfo2.getOperatorNumeric()) && Objects.equals(operatorInfo.getRadioTech(), operatorInfo2.getRadioTech());
    }

    public static boolean containsOperatorInfo(List list, OperatorInfo operatorInfo) {
        synchronized (sLock) {
            if (list != null) {
                try {
                    if (list.size() != 0) {
                        Iterator it = list.iterator();
                        while (it.hasNext()) {
                            if (isSameOperatorInfo((OperatorInfo) it.next(), operatorInfo)) {
                                return true;
                            }
                        }
                        return false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return false;
        }
    }

    public static void clearCache() {
        synchronized (sLock) {
            try {
                if (operatorInfoList.size() > 0) {
                    operatorInfoList.clear();
                }
                if (newOperatorInfoList.size() > 0) {
                    newOperatorInfoList.clear();
                }
                stateIsCurrer = false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static OperatorInfo getSprdOperatorInfo(OperatorInfo operatorInfo) {
        synchronized (sLock) {
            try {
                if (operatorInfo == null) {
                    return null;
                }
                if ("1".equals(operatorInfo.getRadioTech())) {
                    return new OperatorInfo(operatorInfo.getOperatorAlphaLong(), operatorInfo.getOperatorAlphaShort(), operatorInfo.getOperatorNumeric() + " 0");
                }
                if ("2".equals(operatorInfo.getRadioTech())) {
                    return new OperatorInfo(operatorInfo.getOperatorAlphaLong(), operatorInfo.getOperatorAlphaShort(), "");
                }
                if ("3".equals(operatorInfo.getRadioTech())) {
                    return new OperatorInfo(operatorInfo.getOperatorAlphaLong(), operatorInfo.getOperatorAlphaShort(), operatorInfo.getOperatorNumeric() + " 2");
                }
                if ("14".equals(operatorInfo.getRadioTech())) {
                    return new OperatorInfo(operatorInfo.getOperatorAlphaLong(), operatorInfo.getOperatorAlphaShort(), operatorInfo.getOperatorNumeric() + " 7");
                }
                if ("20".equals(operatorInfo.getRadioTech())) {
                    return new OperatorInfo(operatorInfo.getOperatorAlphaLong(), operatorInfo.getOperatorAlphaShort(), operatorInfo.getOperatorNumeric() + " 11");
                }
                return new OperatorInfo("", "", "");
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void getForbiddenList(TelephonyManager telephonyManager) {
        List arrayList;
        synchronized (sLock) {
            try {
                if (telephonyManager == null) {
                    return;
                }
                String[] forbiddenPlmns = telephonyManager.getForbiddenPlmns();
                if (forbiddenPlmns != null) {
                    arrayList = Arrays.asList(forbiddenPlmns);
                } else {
                    arrayList = new ArrayList();
                }
                mForbiddenPlmns = arrayList;
                Log.d("SprdCellInfoUtil", "isForbidden mForbiddenPlmns: " + mForbiddenPlmns.toString());
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static boolean isForbidden(String str) {
        synchronized (sLock) {
            try {
                List list = mForbiddenPlmns;
                if (list != null && list.size() != 0) {
                    Log.d("SprdCellInfoUtil", "isForbidden mForbiddenPlmns: " + mForbiddenPlmns.toString() + ", plmn: " + str + mForbiddenPlmns.contains(str));
                    return mForbiddenPlmns.contains(str);
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static List sortWithCurrentFirst(List list) {
        synchronized (sLock) {
            try {
                if (list == null) {
                    return new ArrayList();
                }
                return (List) list.stream().sorted(Comparator.comparing(new Function() { // from class: com.android.phone.SprdCellInfoUtil$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return Boolean.valueOf(((OperatorInfo) obj).getState() != OperatorInfo.State.CURRENT);
                    }
                })).collect(Collectors.toList());
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
