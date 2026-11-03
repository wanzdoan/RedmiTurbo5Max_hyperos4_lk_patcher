package com.hyper.internal.telephony.ext;

import android.content.Context;
import android.os.Message;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.CatCmdMessage;
import com.android.internal.telephony.cat.CatResponseMessage;
import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.ComprehensionTlv;
import com.android.internal.telephony.cat.Menu;
import com.android.internal.telephony.cat.ResultException;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public interface IHyperCatManagerExt extends IHyperCommonModule {
    public static final IHyperCatManagerExt DEFAULT = new IHyperCatManagerExt() { // from class: com.hyper.internal.telephony.ext.IHyperCatManagerExt.1
    };
    public static final String TAG = "IHyperCatManagerExt";

    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default CatCmdMessage sendTrForSetUpCall(CatResponseMessage resMsg, CatCmdMessage currntCmd) {
        return null;
    }

    default CatCmdMessage sendTrForSetUpCallUserNotResponse(CommandsInterface cmdIf, CatResponseMessage resMsg, CatCmdMessage currntCmd) {
        return null;
    }

    default void sendTrForSetUpCallNetworkError(AppInterface.CommandType type, CatResponseMessage resMsg, CommandDetails cmdDet) {
    }

    default CatCmdMessage sendTrForSetUpCallUserNotAccept(AppInterface.CommandType type, CatResponseMessage resMsg, CommandsInterface cmdIf, CatCmdMessage currntCmd) {
        cmdIf.handleCallSetupRequestFromSim(false, (Message) null);
        return null;
    }

    default void addItemForSelectItem(AppInterface.CommandType cmdType, ComprehensionTlv ctlv, Menu menu) throws ResultException {
    }

    default CommandDetails retrieveCommandDetails(byte[] data, int curIndex, boolean cr, CommandDetails cmdDet) throws ResultException {
        return null;
    }

    default void setCommandDetails(CommandDetails cmdDet) {
    }

    default CommandDetails getCommandDetails() {
        return null;
    }

    default void setGeographicallocationParams(int getHorizontalSpeed, int gadShapeType) {
    }

    default void registerLocationChange(Context context, CommandsInterface cmdIf) {
    }

    default void stopReceivingLocationUpdates() {
    }
}
