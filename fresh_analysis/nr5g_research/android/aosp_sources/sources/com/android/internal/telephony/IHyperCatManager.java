package com.android.internal.telephony;

import android.content.Context;
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.CatCmdMessage;
import com.android.internal.telephony.cat.CatResponseMessage;
import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.ComprehensionTlv;
import com.android.internal.telephony.cat.Menu;
import com.android.internal.telephony.cat.ResultException;
import com.android.internal.telephony.common.HyperModuleList$HyperIndex;
import com.android.internal.telephony.common.IHyperCommonModule;

/* JADX INFO: loaded from: classes.dex */
public interface IHyperCatManager extends IHyperCommonModule {
    public static final IHyperCatManager DEFAULT = new IHyperCatManager() { // from class: com.android.internal.telephony.IHyperCatManager.1
    };
    public static final String TAG = "IHyperCatManager";

    default void addItemForSelectItem(AppInterface.CommandType commandType, ComprehensionTlv comprehensionTlv, Menu menu) throws ResultException {
    }

    default CommandDetails getCommandDetails() {
        return null;
    }

    default void registerLocationChange(Context context, CommandsInterface commandsInterface) {
    }

    default CommandDetails retrieveCommandDetails(byte[] bArr, int i, boolean z, CommandDetails commandDetails) throws ResultException {
        return null;
    }

    default CatCmdMessage sendTrForSetUpCall(CatResponseMessage catResponseMessage, CatCmdMessage catCmdMessage) {
        return null;
    }

    default void sendTrForSetUpCallNetworkError(AppInterface.CommandType commandType, CatResponseMessage catResponseMessage, CommandDetails commandDetails) {
    }

    default CatCmdMessage sendTrForSetUpCallUserNotResponse(CommandsInterface commandsInterface, CatResponseMessage catResponseMessage, CatCmdMessage catCmdMessage) {
        return null;
    }

    default void setCommandDetails(CommandDetails commandDetails) {
    }

    default void setGeographicallocationParams(int i, int i2) {
    }

    default void stopReceivingLocationUpdates() {
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default HyperModuleList$HyperIndex index() {
        return HyperModuleList$HyperIndex.IHyperCatManager;
    }

    @Override // com.android.internal.telephony.common.IHyperCommonModule
    default IHyperCommonModule getDefault() {
        return DEFAULT;
    }

    default CatCmdMessage sendTrForSetUpCallUserNotAccept(AppInterface.CommandType commandType, CatResponseMessage catResponseMessage, CommandsInterface commandsInterface, CatCmdMessage catCmdMessage) {
        commandsInterface.handleCallSetupRequestFromSim(false, null);
        return null;
    }
}
