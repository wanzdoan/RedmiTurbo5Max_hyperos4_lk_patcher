package com.android.internal.telephony.cat;

import android.content.Context;
import android.util.Log;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.UniTelephonyInjectionFactory;

/* JADX INFO: loaded from: classes.dex */
public class UniCatManager {
    public static final String TAG = "UniCatManager";
    private static UniCatManager sInstance;

    public CommandDetails getCommandDetails() {
        return null;
    }

    public void registerLocationChange(Context context, CommandsInterface commandsInterface) {
    }

    public CommandDetails retrieveCommandDetails(byte[] bArr, int i, boolean z, CommandDetails commandDetails) throws ResultException {
        return null;
    }

    public CatCmdMessage sendTrForSetUpCall(CatResponseMessage catResponseMessage, CatCmdMessage catCmdMessage) {
        return null;
    }

    public void sendTrForSetUpCallNetworkError(AppInterface.CommandType commandType, CatResponseMessage catResponseMessage, CommandDetails commandDetails) {
    }

    public void setCommandDetails(CommandDetails commandDetails) {
    }

    public void setGeographicallocationParams(int i, int i2) {
    }

    public void stopReceivingLocationUpdates() {
    }

    public UniCatManager() {
        log("UniCatManager created");
    }

    public static UniCatManager getInstance() {
        UniCatManager uniCatManager;
        synchronized (UniCatManager.class) {
            try {
                if (sInstance == null) {
                    sInstance = UniTelephonyInjectionFactory.getInstance().getUniCatManager();
                }
                uniCatManager = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return uniCatManager;
    }

    public void addItemForSelectItem(AppInterface.CommandType commandType, ComprehensionTlv comprehensionTlv, Menu menu) throws ResultException {
        menu.items.add(ValueParser.retrieveItem(comprehensionTlv));
    }

    public CatCmdMessage sendTrForSetUpCallUserNotAccept(AppInterface.CommandType commandType, CatResponseMessage catResponseMessage, CommandsInterface commandsInterface, CatCmdMessage catCmdMessage) {
        commandsInterface.handleCallSetupRequestFromSim(false, null);
        return null;
    }

    public CatCmdMessage sendTrForSetUpCallUserNotResponse(CommandsInterface commandsInterface, CatResponseMessage catResponseMessage, CatCmdMessage catCmdMessage) {
        commandsInterface.handleCallSetupRequestFromSim(false, null);
        return null;
    }

    private static void log(String str) {
        Log.d(TAG, str);
    }
}
