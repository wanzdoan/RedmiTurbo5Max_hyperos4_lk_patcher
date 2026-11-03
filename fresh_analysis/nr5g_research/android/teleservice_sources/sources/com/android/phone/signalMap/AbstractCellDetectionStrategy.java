package com.android.phone.signalMap;

import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.ServiceState;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractCellDetectionStrategy {
    protected AbstractCellDetectionStrategy nextStrategy;

    protected abstract boolean isBadCell(String str);

    public String getCellInfo(int i) {
        long nci;
        int tac;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return "exceptionCell";
        }
        String availableOperatorNumeric = getAvailableOperatorNumeric(phone.getServiceState(), i);
        if ("noSim".equals(availableOperatorNumeric) || phone.getCurrentCellIdentity() == null) {
            return "exceptionCell";
        }
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            nci = cellIdentityLte.getCi();
            tac = cellIdentityLte.getTac();
        } else {
            if (!(currentCellIdentity instanceof CellIdentityNr)) {
                return "exceptionCell";
            }
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            nci = cellIdentityNr.getNci();
            tac = cellIdentityNr.getTac();
        }
        return availableOperatorNumeric + "_" + nci + "_" + tac;
    }

    private String getAvailableOperatorNumeric(ServiceState serviceState, int i) {
        String operatorNumeric = serviceState == null ? "" : serviceState.getOperatorNumeric();
        if (TextUtils.isEmpty(operatorNumeric)) {
            operatorNumeric = TelephonyManager.getDefault().getSimOperatorForSlot(i);
        }
        return TextUtils.isEmpty(operatorNumeric) ? "noSim" : operatorNumeric;
    }
}
