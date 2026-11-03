package com.mediatek.internal.telephony.cat;

import android.content.Context;
import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.CommandParamsFactory;
import com.android.internal.telephony.cat.ComprehensionTlv;
import com.android.internal.telephony.cat.ComprehensionTlvTag;
import com.android.internal.telephony.cat.Menu;
import com.android.internal.telephony.cat.ResultException;
import com.android.internal.telephony.cat.RilMessageDecoder;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkCommandParamsFactory extends CommandParamsFactory {
    public static final int BATTERY_STATE = 10;
    private Context mContext;
    int tlvIndex;

    public MtkCommandParamsFactory(RilMessageDecoder caller, IccFileHandler fh, Context context) {
        super(caller, fh, context);
        this.tlvIndex = -1;
        this.mContext = context;
    }

    protected Menu onCreatMenu() {
        return new MtkMenu();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    protected String onGetAlphaId(ComprehensionTlv ctlv) throws ResultException {
        String text = null;
        try {
            text = MtkValueParser.retrieveAlphaId(ctlv, this.mNoAlphaUsrCnf);
        } catch (ResultException e) {
            MtkCatLog.e(this, "retrieveAlphaId ResultException: " + e.result());
        }
        MtkCatLog.d(this, "add AlphaId: " + text);
        return text;
    }

    protected void onSetNextActionIndicator(List<ComprehensionTlv> ctlvs, Menu menu) {
        if (menu instanceof MtkMenu) {
            MtkMenu mtkMenu = (MtkMenu) menu;
            ComprehensionTlv ctlv = searchForTag(ComprehensionTlvTag.NEXT_ACTION_INDICATOR, ctlvs);
            if (ctlv != null) {
                try {
                    mtkMenu.nextActionIndicator = MtkValueParser.retrieveNextActionIndicator(ctlv);
                } catch (ResultException e) {
                    MtkCatLog.e(this, "retrieveNextActionIndicator ResultException: " + e.result());
                }
                try {
                    if (mtkMenu.nextActionIndicator.length != mtkMenu.items.size()) {
                        MtkCatLog.d(this, "nextActionIndicator.length != number of menu items");
                        mtkMenu.nextActionIndicator = null;
                    }
                } catch (NullPointerException e2) {
                    MtkCatLog.e(this, "nextActionIndicator is null.");
                }
            }
        }
    }

    protected boolean handleBatteryStateInfo(CommandDetails cmdDet) {
        if (cmdDet.commandQualifier == 10) {
            this.mCmdParams = new CommandParams(cmdDet);
            return true;
        }
        return false;
    }

    protected void onGetEventList(byte[] rawValue, int valueIndex, int valueLen, int[] eventList) {
        int index = 0;
        while (index < valueLen) {
            eventList[index] = rawValue[valueIndex];
            MtkCatLog.v(this, "CPF-processSetUpEventList: eventList[" + index + "] = " + eventList[index]);
            index++;
            valueIndex++;
        }
    }
}
