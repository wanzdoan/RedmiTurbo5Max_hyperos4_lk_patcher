package com.android.phone;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.phone.module.INetworkModule;
import java.util.Arrays;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class MtkCallOptimzerAction implements TelephonyStatAdapter.ICallBack {
    public static CallActionHandler mHandler;
    private String mLastMcc = "";
    CallOptimizeManager mgr;

    public class ActionContext {
        public int phoneId = 0;
        public int event_id = 0;
        public int scene = 0;
        public int rat = 0;
        public int bar_cell_num = 0;
        public int freq = 0;
        public int pci = 0;
        public boolean isReset = true;
    }

    public MtkCallOptimzerAction(CallOptimizeManager callOptimizeManager) {
        this.mgr = callOptimizeManager;
        HandlerThread handlerThread = new HandlerThread("MtkCallOptimzerAction");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        if (looper == null) {
            log("Error: HandlerThread Looper is null after start");
        } else {
            mHandler = new CallActionHandler(looper);
            TelephonyStatAdapter.registerCallBack(this);
        }
    }

    class CallActionHandler extends Handler {
        public CallActionHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MtkCallOptimzerAction.this.log("CallActionHandler handleMessage what=" + message.what);
            if (message.what != 1001) {
                return;
            }
            MtkCallOptimzerAction.this.processServiceStateChange(message.arg1, (ServiceState) message.obj);
        }
    }

    public void onServiceStateChanged(int i, ServiceState serviceState) {
        mHandler.obtainMessage(1001, i, 0, serviceState).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processServiceStateChange(int i, ServiceState serviceState) {
        CallOptimizeManager callOptimizeManager;
        if (i != MiuiPhoneUtils.getActiveDataPhoneId() || !MiuiPhoneUtils.isValidatePhoneId(i) || serviceState == null || (callOptimizeManager = this.mgr) == null) {
            return;
        }
        callOptimizeManager.processServiceStateChange(i, serviceState);
    }

    public void performActionOnCurrentCell(ActionContext actionContext) {
        try {
            boolean z = actionContext.isReset;
            String[] strArr = {String.format("AT+EDMFAPP=%s,%d,%d,%d,%d,%d,0,%d,%d,%d,%d,%d", "20109", Integer.valueOf(actionContext.phoneId), Integer.valueOf(actionContext.event_id), Integer.valueOf(actionContext.scene), Integer.valueOf(z ? 1 : 0), Integer.valueOf(actionContext.rat), 0, Integer.valueOf(actionContext.bar_cell_num), Integer.valueOf(actionContext.freq), Integer.valueOf(actionContext.pci), Integer.valueOf(z ? 0 : 3600)), "+EDMFAPP"};
            log("performActionOnCurrentCell atStudyCmd: " + Arrays.toString(strArr));
            if (actionContext.event_id == 105) {
                FiveGManagerBase.getInstance().sendAtCommand(0, strArr, (Message) null);
                return;
            }
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone == null) {
                return;
            }
            FiveGManagerBase.getInstance().sendAtCommand(activeDataPhone.getPhoneId(), strArr, (Message) null);
        } catch (Exception e) {
            log("performActionOnCurrentCell error " + e.getMessage());
        }
    }

    public void setSingleSimVonrOpt() {
        if (SubscriptionManager.getDefault() == null || FiveGManagerBase.getInstance() == null) {
            return;
        }
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(activeDataPhoneId);
        boolean zIsVoNREnabled = FiveGManagerBase.getInstance().isVoNREnabled(subscriptionIdForSlot);
        log("activePhoneId: " + activeDataPhoneId + " activeSubId: " + subscriptionIdForSlot + " isVoNrEnable: " + zIsVoNREnabled);
        if (!zIsVoNREnabled && !this.mgr.isDisableVoNrByOpt(subscriptionIdForSlot)) {
            ActionContext actionContext = new ActionContext();
            actionContext.phoneId = activeDataPhoneId;
            actionContext.event_id = 103;
            performActionOnCurrentCell(actionContext);
            return;
        }
        if (this.mgr.isDisableVoNrByOpt(subscriptionIdForSlot)) {
            this.mgr.setVoNrState(activeDataPhoneId, true);
            updateResidentCellDbForVonrOpt("0", 0, true, "vonrLimit", 0L);
        }
    }

    public void setDualSimVonrOpt() {
        MtkCallOptimzerAction mtkCallOptimzerAction;
        if (SubscriptionManager.getDefault() == null || FiveGManagerBase.getInstance() == null) {
            log("Error: SubscriptionManager or FiveGManager is null");
            return;
        }
        int i = 0;
        while (i < 2) {
            int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
            boolean zIsVoNREnabled = FiveGManagerBase.getInstance().isVoNREnabled(subscriptionIdForSlot);
            this.log("Slot: " + i + " activeSubId: " + subscriptionIdForSlot + " isVoNrEnable: " + zIsVoNREnabled);
            if (!zIsVoNREnabled && !this.mgr.isDisableVoNrByOpt(subscriptionIdForSlot)) {
                ActionContext actionContext = new ActionContext();
                actionContext.phoneId = i;
                actionContext.event_id = 103;
                this.performActionOnCurrentCell(actionContext);
            } else {
                if (this.mgr.isDisableVoNrByOpt(subscriptionIdForSlot)) {
                    this.mgr.setVoNrState(i, true);
                    mtkCallOptimzerAction = this;
                    mtkCallOptimzerAction.updateResidentCellDbForVonrOpt("0", 0, true, "vonrLimit", 0L);
                }
                i++;
                this = mtkCallOptimzerAction;
            }
            mtkCallOptimzerAction = this;
            i++;
            this = mtkCallOptimzerAction;
        }
    }

    public void updateResidentCellDbForVonrOpt(String str, int i, boolean z, String str2, long j) {
        try {
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).updateResidentCellDbForVonrOpt(str, i, z, str2, j);
        } catch (Exception e) {
            log("Exception: " + e);
        }
    }

    public void reSetBaredCells() {
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).reSetBaredCells();
    }

    public boolean isResidentCell() {
        INetworkModule iNetworkModule = (INetworkModule) ModuleDelegate.get(INetworkModule.class);
        return iNetworkModule.isResidentCellScene() || iNetworkModule.isInCompanyScence();
    }

    public void setModemCallOptimize(int i, String str) {
        String str2;
        try {
            log("setModemCallOptimize:  phoneid: " + i + " state: " + str);
            if (str.equalsIgnoreCase("ACTIVE")) {
                str2 = "AT+EDMFAPP=20007,10";
            } else {
                str2 = "AT+EDMFAPP=20007,11";
            }
            FiveGManagerBase.getInstance().sendAtCommand(i, new String[]{str2, "+EDMFAPP"}, (Message) null);
        } catch (Exception e) {
            log("Exception: " + e);
        }
    }

    public String[] getCallStallCmds(int i) {
        if (i == 1) {
            return new String[]{"1,1,1,0,3000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-110,-5,0xFF,0xFF,10,15,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,1,1,0,3000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-110,-5,99,50,0xFFFFFFFF,0xFFFFFFFF,3000,40,0xFFFFFFFF", "3,1,1,0,3000,300,300,0xFFFFFFFF,0xFF,0xFF,26,-100,0,-110,-5,99,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,1,1,1,3000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-110,-5,0xFF,0xFF,10,15,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,1,1,1,3000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-110,-5,99,50,0xFFFFFFFF,0xFFFFFFFF,3000,40,0xFFFFFFFF", "3,1,1,1,3000,300,300,0xFFFFFFFF,0xFF,0xFF,26,-100,0,-110,-5,99,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
        }
        if (i != 2) {
            return null;
        }
        return new String[]{"1,0,1,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,0,1,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "3,0,1,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,1,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "2,0,1,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "3,0,1,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    }

    public String getCountryMcc() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            this.mLastMcc = "";
            return "";
        }
        int phoneId = activeDataPhone.getPhoneId();
        TelephonyManager telephonyManager = this.mgr.mTelephonyManager;
        if (telephonyManager == null) {
            log("getCountryMcc: mTelephonyManager is null");
            return getSimMcc(phoneId);
        }
        String networkOperatorForPhone = telephonyManager.getNetworkOperatorForPhone(phoneId);
        if (TextUtils.isEmpty(networkOperatorForPhone) || networkOperatorForPhone.length() < 3) {
            log("getCountryMcc plmn is empty");
            if (TextUtils.isEmpty(this.mLastMcc)) {
                return getSimMcc(phoneId);
            }
            return this.mLastMcc;
        }
        String strSubstring = networkOperatorForPhone.substring(0, 3);
        this.mLastMcc = strSubstring;
        log("getCountryMcc mcc = ".concat(strSubstring));
        return strSubstring;
    }

    private String getSimMcc(int i) {
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(i);
        if (TextUtils.isEmpty(simOperatorNumeric) || simOperatorNumeric.length() < 3) {
            log("getSimMcc simNumeric is empty");
            return "";
        }
        String strSubstring = simOperatorNumeric.substring(0, 3);
        log("getSimMcc mcc = ".concat(strSubstring));
        return strSubstring;
    }

    public boolean isModemCallOptimizeOperator() {
        try {
            if (!MiuiPhoneUtils.isSupportMtkGlobalCallOptimization()) {
                return false;
            }
            String countryMcc = getCountryMcc();
            if (TextUtils.isEmpty(countryMcc)) {
                return false;
            }
            return this.mgr.MODME_CALL_OPTIMIZATION_MCC_SET.contains(countryMcc);
        } catch (Exception e) {
            log("isModemCallOptimizeOperator error " + e.getMessage());
            return false;
        }
    }

    public void processVonrCallOpt(String[] strArr) {
        if (this.mgr.shouldDoCallVonrOpt()) {
            if (strArr == null || strArr.length < 10) {
                log("onAtUrcInd parse data length < 10");
                return;
            }
            int i = Integer.parseInt(strArr[2].trim());
            log("processCallQualityOpt data = " + Arrays.toString(strArr) + " eventId: " + i);
            if (i == 100) {
                return;
            }
            if (i == 101 || i == 102) {
                this.mgr.processVonrOpt(strArr, i);
            }
        }
    }

    protected void log(String str) {
        Rlog.d("MtkCallOptimzerAction", str);
    }
}
