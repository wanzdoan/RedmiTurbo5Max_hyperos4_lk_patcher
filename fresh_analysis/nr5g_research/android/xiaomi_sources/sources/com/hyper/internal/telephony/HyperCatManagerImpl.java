package com.hyper.internal.telephony;

import android.content.Context;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IHyperCatManager;
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.CatCmdMessage;
import com.android.internal.telephony.cat.CatResponseMessage;
import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.ComprehensionTlv;
import com.android.internal.telephony.cat.Menu;
import com.android.internal.telephony.cat.ResultException;

/* JADX INFO: loaded from: classes.dex */
public class HyperCatManagerImpl implements IHyperCatManager {
    private static HyperCatManagerImpl sInstance;

    private HyperCatManagerImpl() {
    }

    public static HyperCatManagerImpl getInstance() {
        HyperCatManagerImpl hyperCatManagerImpl;
        synchronized (HyperCatManagerImpl.class) {
            if (sInstance == null) {
                sInstance = new HyperCatManagerImpl();
            }
            hyperCatManagerImpl = sInstance;
        }
        return hyperCatManagerImpl;
    }

    public CatCmdMessage sendTrForSetUpCall(CatResponseMessage resMsg, CatCmdMessage currntCmd) {
        return HyperTelephonyInternalManager.getInstance().getHyperCatManager().sendTrForSetUpCall(resMsg, currntCmd);
    }

    public CatCmdMessage sendTrForSetUpCallUserNotResponse(CommandsInterface cmdIf, CatResponseMessage resMsg, CatCmdMessage currntCmd) {
        return HyperTelephonyInternalManager.getInstance().getHyperCatManager().sendTrForSetUpCallUserNotResponse(cmdIf, resMsg, currntCmd);
    }

    public void sendTrForSetUpCallNetworkError(AppInterface.CommandType type, CatResponseMessage resMsg, CommandDetails cmdDet) {
        HyperTelephonyInternalManager.getInstance().getHyperCatManager().sendTrForSetUpCallNetworkError(type, resMsg, cmdDet);
    }

    public CatCmdMessage sendTrForSetUpCallUserNotAccept(AppInterface.CommandType type, CatResponseMessage resMsg, CommandsInterface cmdIf, CatCmdMessage currntCmd) {
        return HyperTelephonyInternalManager.getInstance().getHyperCatManager().sendTrForSetUpCallUserNotAccept(type, resMsg, cmdIf, currntCmd);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    public void addItemForSelectItem(AppInterface.CommandType cmdType, ComprehensionTlv ctlv, Menu menu) throws ResultException {
        HyperTelephonyInternalManager.getInstance().getHyperCatManager().addItemForSelectItem(cmdType, ctlv, menu);
    }

    public CommandDetails retrieveCommandDetails(byte[] data, int curIndex, boolean cr, CommandDetails cmdDet) throws ResultException {
        return HyperTelephonyInternalManager.getInstance().getHyperCatManager().retrieveCommandDetails(data, curIndex, cr, cmdDet);
    }

    public void setCommandDetails(CommandDetails cmdDet) {
        HyperTelephonyInternalManager.getInstance().getHyperCatManager().setCommandDetails(cmdDet);
    }

    public CommandDetails getCommandDetails() {
        return HyperTelephonyInternalManager.getInstance().getHyperCatManager().getCommandDetails();
    }

    public void setGeographicallocationParams(int getHorizontalSpeed, int gadShapeType) {
        HyperTelephonyInternalManager.getInstance().getHyperCatManager().setGeographicallocationParams(getHorizontalSpeed, gadShapeType);
    }

    public void registerLocationChange(Context context, CommandsInterface cmdIf) {
        HyperTelephonyInternalManager.getInstance().getHyperCatManager().registerLocationChange(context, cmdIf);
    }

    public void stopReceivingLocationUpdates() {
        HyperTelephonyInternalManager.getInstance().getHyperCatManager().stopReceivingLocationUpdates();
    }
}
