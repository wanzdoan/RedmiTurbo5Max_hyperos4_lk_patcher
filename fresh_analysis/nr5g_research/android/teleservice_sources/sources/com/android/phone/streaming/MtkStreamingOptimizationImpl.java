package com.android.phone.streaming;

import android.os.AsyncResult;
import android.os.Message;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.OffsetCell;
import com.android.phone.utils.ATCommandUtils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
public class MtkStreamingOptimizationImpl extends StreamingOptimizationController {
    private static final String[] DLS_COMMAND_ENABLE = {"1,3,0,0,3000,8,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-112,-3,0xFF,0xFF,0,2000,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,3,0,1,3000,8,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-112,-3,0xFF,0xFF,0,2000,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    private static final String[] DLS_COMMAND_ENABLE_NORMAL = {"1,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,500,1,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,500,1,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    private static final String[] DLS_COMMAND_DISABLE = {"1,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "1,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
    private static final String[] ENABLE_GOOD_LINK_COMMAND = {"5,7,0,0,2000,0,0,0,0xFF,50,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,1,0xFFFFFFFF,0xFFFFFFFF,800,100,10,50", "5,7,0,1,2000,0,0,0,0xFF,50,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,1,0xFFFFFFFF,0xFFFFFFFF,800,100,10,50"};
    private static final String[] DISABLE_GOOD_LINK_COMMAND = {"5,0,0,0,2000,0,0,0,0xFF,50,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,1,0xFFFFFFFF,0xFFFFFFFF,800,100,10,50", "5,0,0,1,2000,0,0,0,0xFF,50,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,1,0xFFFFFFFF,0xFFFFFFFF,800,100,10,50"};
    private final AtomicReference mSbpErrcIgnore2g3gRedirectOriginal = new AtomicReference("0");
    private final AtomicReference mSbp54gIndoorModeOriginal = new AtomicReference("0");
    private final AtomicBoolean mSbpModified = new AtomicBoolean(false);
    private final AtomicBoolean mIsEnableLargeBW = new AtomicBoolean(false);
    private final List mStreamingOffsetCells = new CopyOnWriteArrayList();

    public static void make() {
        if (StreamingOptimizationController.mInstance instanceof MtkStreamingOptimizationImpl) {
            return;
        }
        StreamingOptimizationController.mInstance = new MtkStreamingOptimizationImpl();
    }

    @Override // com.android.phone.streaming.StreamingOptimizationController
    protected void processPlatformMessage(Message message) {
        if (message == null) {
            Log.e("MtkStreamingOptimizationImpl", "processPlatformMessage: message is null");
            return;
        }
        int i = message.what;
        if (i == 3) {
            handleDmfUrcMessage(message);
            return;
        }
        if (i == 7) {
            handleStableCellTimeout();
        } else {
            if (i == 8) {
                disableStableCellStrategy();
                return;
            }
            Log.e("MtkStreamingOptimizationImpl", "Unknown message: " + message.what);
        }
    }

    private void handleStableCellTimeout() {
        if (this.mOptStateMgr.getOptState() != 0) {
            Log.d("MtkStreamingOptimizationImpl", "In optimization state, skip stable cell strategy");
        } else {
            enableStableCellStrategy();
        }
    }

    @Override // com.android.phone.streaming.StreamingOptimizationController
    protected void executeDataStallEscape(int i) {
        Log.d("MtkStreamingOptimizationImpl", "executeDataStallEscape slotId is " + i);
        executeDataStallCommands(DLS_COMMAND_ENABLE, i);
    }

    @Override // com.android.phone.streaming.StreamingOptimizationController
    protected void executeDataStallEscapeNormal(int i) {
        Log.d("MtkStreamingOptimizationImpl", "executeDataStallEscapeNormal slotId is " + i);
        executeDataStallCommands(DLS_COMMAND_ENABLE_NORMAL, i);
    }

    @Override // com.android.phone.streaming.StreamingOptimizationController
    protected void handlerStreamingSceneChanged(boolean z) {
        if (z) {
            enableLargeBW(true);
            regUnRegisterAtUrcInd(true);
        } else {
            enableLargeBW(false);
            regUnRegisterAtUrcInd(false);
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone != null) {
                Iterator it = this.mStreamingOffsetCells.iterator();
                while (it.hasNext()) {
                    doOffsetAction(true, (OffsetCell) it.next(), activeDataPhone.getPhoneId());
                }
                this.mStreamingOffsetCells.clear();
            }
        }
        processSbpOptimization(z);
    }

    private void processSbpOptimization(boolean z) {
        if (MiuiPhoneUtils.isSupportInternationalTravelDataOptimization()) {
            if (z && MiuiPhoneUtils.isDataRoaming(this.mOptStateMgr.getSceneContext().getDdsSlot())) {
                if (this.mSbpModified.get()) {
                    return;
                }
                setSbp("SBP_ERRC_IGNORE_2G_3G_REDIRECT", "1", true);
                setSbp("SBP_MODEM_54G_INDOOR_MODE", "0", true);
                this.mSbpModified.set(true);
                return;
            }
            if (z || !this.mSbpModified.get()) {
                return;
            }
            setSbp("SBP_ERRC_IGNORE_2G_3G_REDIRECT", (String) this.mSbpErrcIgnore2g3gRedirectOriginal.get(), false);
            setSbp("SBP_MODEM_54G_INDOOR_MODE", (String) this.mSbp54gIndoorModeOriginal.get(), false);
            this.mSbpModified.set(false);
        }
    }

    @Override // com.android.phone.streaming.StreamingOptimizationController
    protected void resetDataStallEscape(int i) {
        Log.d("MtkStreamingOptimizationImpl", "resetDataStallEscape slotId is " + i);
        executeDataStallCommands(DLS_COMMAND_DISABLE, i);
    }

    private void enableStableCellStrategy() {
        if (!this.mOptStateMgr.getSceneContext().isDynamicScene() && this.mOptStateMgr.getSceneContext().isResidentCell()) {
            this.mOptStateMgr.getAuxState().setStableCellEnabled(true);
            executeDataStallCommands(ENABLE_GOOD_LINK_COMMAND, this.mOptStateMgr.getSceneContext().getDdsSlot());
            Log.i("MtkStreamingOptimizationImpl", "MTK stable cell strategy enabled (good link)");
            return;
        }
        Log.d("MtkStreamingOptimizationImpl", "In dynamic scene, skip MTK good link");
    }

    private void disableStableCellStrategy() {
        this.mOptStateMgr.getAuxState().setStableCellEnabled(false);
        executeDataStallCommands(DISABLE_GOOD_LINK_COMMAND, this.mOptStateMgr.getSceneContext().getDdsSlot());
        Log.i("MtkStreamingOptimizationImpl", "MTK stable cell strategy disabled");
    }

    private void handleDmfUrcMessage(Message message) {
        Log.d("MtkStreamingOptimizationImpl", "EVENT_DMF_URC_REG");
        try {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Phone phone = (Phone) asyncResult.userObj;
            if (phone == null) {
                return;
            }
            int phoneId = phone.getPhoneId();
            String str = (String) asyncResult.result;
            Log.d("MtkStreamingOptimizationImpl", "PhoneId is " + phoneId + " urcAtString is " + str);
            Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
            if (activeDataPhone != null && phoneId == activeDataPhone.getPhoneId()) {
                if (validateAndProcessUrc(str, activeDataPhone)) {
                    return;
                }
                Log.d("MtkStreamingOptimizationImpl", "not dmf urc ignore");
                return;
            }
            Log.d("MtkStreamingOptimizationImpl", "phoneId is not active data phone");
        } catch (Exception e) {
            Log.e("MtkStreamingOptimizationImpl", "Error handling DMF URC message", e);
        }
    }

    private boolean validateAndProcessUrc(String str, Phone phone) {
        if (!TextUtils.isEmpty(str) && str.startsWith("+EDMFAPP:") && phone != null) {
            String[] strArrSplit = str.split(",");
            if (strArrSplit != null && strArrSplit.length >= 9) {
                try {
                    if (strArrSplit[0].split(":").length >= 2) {
                        int i = Integer.parseInt(strArrSplit[0].split(":")[1].trim());
                        int i2 = Integer.parseInt(strArrSplit[1].trim());
                        Log.d("MtkStreamingOptimizationImpl", "appId is " + i + " data.length is " + strArrSplit.length);
                        if (i == 20006 && i2 == 103) {
                            return processOffsetAction(phone, strArrSplit);
                        }
                        return false;
                    }
                    Log.d("MtkStreamingOptimizationImpl", "at cmd invalid");
                    return false;
                } catch (NumberFormatException e) {
                    Log.e("MtkStreamingOptimizationImpl", "Error parsing URC", e);
                    return false;
                }
            }
            Log.d("MtkStreamingOptimizationImpl", "data length invalid");
        }
        return false;
    }

    private boolean processOffsetAction(Phone phone, String[] strArr) {
        if (phone == null || strArr == null || strArr.length < 9) {
            Log.d("MtkStreamingOptimizationImpl", "dataPhone is null or data is null or data length invalid");
            return false;
        }
        OffsetCell currentCellInfo = getCurrentCellInfo(phone);
        if (currentCellInfo == null) {
            Log.d("MtkStreamingOptimizationImpl", "offsetCell is null");
            return false;
        }
        try {
            currentCellInfo.freq = Integer.parseInt(strArr[4].trim());
            currentCellInfo.pci = Integer.parseInt(strArr[5].trim());
            currentCellInfo.offset = Integer.parseInt(strArr[6].trim()) * 4;
            currentCellInfo.rat = Integer.parseInt(strArr[7].trim());
            doOffsetAction(false, currentCellInfo, phone.getPhoneId());
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    private void executeDataStallCommands(String[] strArr, int i) {
        if (strArr == null || strArr.length == 0) {
            Log.w("MtkStreamingOptimizationImpl", "commands is null or empty");
            return;
        }
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str)) {
                String str2 = "MIPC_SET_DATA_STALL_OPT," + str;
                Log.d("MtkStreamingOptimizationImpl", "executeCommand:".concat(str2));
                Log.d("MtkStreamingOptimizationImpl", "executeCommand result:".concat(sendAtCommandSync(str2.split(","), new Object[1], i) ? "succeeded" : "failed"));
            }
        }
    }

    private void enableLargeBW(boolean z) {
        if (this.mIsEnableLargeBW.get() != z) {
            StringBuilder sb = new StringBuilder("AT+EDMFAPP=20006,408,5,");
            sb.append(z ? "1" : "0");
            sb.append(",100,-110,-12,0");
            String[] strArr = {sb.toString(), "+EDMFAPP"};
            if (MiuiPhoneUtils.getActiveDataPhone() != null) {
                this.mIsEnableLargeBW.set(z);
                ATCommandUtils.getInstance().sendAtCommand(MiuiPhoneUtils.getActiveDataPhoneId(), strArr, (Message) null);
            }
        }
    }

    private void setSbp(String str, String str2, boolean z) {
        try {
            int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
            if (activeDataPhoneId < 0) {
                Log.e("MtkStreamingOptimizationImpl", "setSbp: invalid phoneId");
                return;
            }
            saveSbpOriginalValue(str, z, activeDataPhoneId);
            Log.d("MtkStreamingOptimizationImpl", "setSbp: " + str + " value=" + str2 + " ret=" + sendAtCommandSync(new String[]{"AT+ESBP=5,\"" + str + "\"," + str2, "+ESBP"}, new Object[1], activeDataPhoneId));
        } catch (Exception e) {
            Log.e("MtkStreamingOptimizationImpl", "setSbp error: " + e.getMessage(), e);
        }
    }

    private void saveSbpOriginalValue(String str, boolean z, int i) {
        String[] modemResponse;
        String str2;
        if (z) {
            Object[] objArr = new Object[1];
            if (!sendAtCommandSync(new String[]{"AT+ESBP=7,\"" + str + "\"", "+ESBP"}, objArr, i) || (modemResponse = parseModemResponse(objArr[0])) == null || modemResponse.length <= 1) {
                return;
            }
            String[] strArrSplit = modemResponse[1].split(":");
            if (strArrSplit.length <= 1 || (str2 = strArrSplit[1]) == null) {
                return;
            }
            String strTrim = str2.trim();
            if ("SBP_ERRC_IGNORE_2G_3G_REDIRECT".equals(str)) {
                this.mSbpErrcIgnore2g3gRedirectOriginal.set(strTrim);
            } else if ("SBP_MODEM_54G_INDOOR_MODE".equals(str)) {
                this.mSbp54gIndoorModeOriginal.set(strTrim);
            }
            Log.d("MtkStreamingOptimizationImpl", "setSbp: " + str + " original value=" + strTrim);
        }
    }

    private void regUnRegisterAtUrcInd(boolean z) {
        ATCommandUtils aTCommandUtils;
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && (aTCommandUtils = ATCommandUtils.getInstance()) != null) {
            aTCommandUtils.regUnRegisterOemAtUrcInd(z, this.mWorkHandler, 3);
        }
    }

    private void doOffsetAction(boolean z, OffsetCell offsetCell, int i) {
        if (offsetCell != null) {
            try {
                if (TextUtils.isEmpty(offsetCell.plmn)) {
                    return;
                }
                String str = offsetCell.plmn + "_" + offsetCell.freq + "_" + offsetCell.pci;
                if (z) {
                    Log.d("MtkStreamingOptimizationImpl", "doOffsetAction: reset offset for cell ".concat(str));
                    ATCommandUtils.getInstance().sendAtCommand(i, new String[]{getStringBuilder(true, offsetCell).toString(), "+EGMC"}, (Message) null);
                    return;
                }
                if (this.mStreamingOffsetCells.contains(offsetCell)) {
                    Log.d("MtkStreamingOptimizationImpl", "doOffsetAction: cell " + str + " already optimized, skip");
                    return;
                }
                if (MiuiPhoneUtils.isSupportCongestionOptimization() && StreamingCloudConfigHelper.isCongestionEnabled() && !MiuiPhoneUtils.isDataRoaming(i)) {
                    if (!this.mOptStateMgr.getSceneContext().isDynamicScene()) {
                        Log.d("MtkStreamingOptimizationImpl", "doOffsetAction: not in dynamic scene, skip offset optimization");
                        return;
                    }
                    Log.d("MtkStreamingOptimizationImpl", "doOffsetAction: congestion feature enabled, check dynamic scene passed");
                }
                Log.d("MtkStreamingOptimizationImpl", "doOffsetAction: execute offset optimization for cell ".concat(str));
                ATCommandUtils.getInstance().sendAtCommand(i, new String[]{getStringBuilder(false, offsetCell).toString(), "+EGMC"}, (Message) null);
                this.mStreamingOffsetCells.add(offsetCell);
            } catch (Exception e) {
                Log.d("MtkStreamingOptimizationImpl", "doOffsetFromModem error: " + e.getMessage());
            }
        }
    }

    private static StringBuilder getStringBuilder(boolean z, OffsetCell offsetCell) {
        StringBuilder sb = new StringBuilder((offsetCell.rat != 1 ? (char) 0 : (char) 1) > 0 ? "AT+EGMC=1,\"LTE_high_priority_cell\"," : "AT+EGMC=1,\"NR_high_priority_cell\",");
        sb.append(!z ? "1," : "2,");
        sb.append(offsetCell.plmn);
        if (offsetCell.plmn.length() > 5) {
            sb.append(",");
        } else {
            sb.append("f,");
        }
        sb.append(offsetCell.freq);
        sb.append(",");
        sb.append(offsetCell.pci);
        if (!z) {
            sb.append(",");
            sb.append(offsetCell.offset);
        }
        return sb;
    }

    private OffsetCell getCurrentCellInfo(Phone phone) {
        CellIdentity currentCellIdentity;
        int pci;
        int nrarfcn;
        int i;
        if (phone == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null) {
            return null;
        }
        String globalCellId = currentCellIdentity.getGlobalCellId();
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            pci = cellIdentityLte.getPci();
            nrarfcn = cellIdentityLte.getEarfcn();
            i = 1;
        } else {
            if (!(currentCellIdentity instanceof CellIdentityNr)) {
                return null;
            }
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            pci = cellIdentityNr.getPci();
            nrarfcn = cellIdentityNr.getNrarfcn();
            i = 2;
        }
        return new OffsetCell(nrarfcn, pci, 0, currentCellIdentity.getPlmn(), i, globalCellId);
    }

    private boolean sendAtCommandSync(String[] strArr, Object[] objArr, int i) {
        try {
            return ATCommandUtils.getInstance().atCmdRunSync(i, strArr, objArr);
        } catch (Exception e) {
            Log.e("MtkStreamingOptimizationImpl", "Error sending AT command", e);
            return false;
        }
    }

    private String[] parseModemResponse(Object obj) {
        String str;
        if (!(obj instanceof String[])) {
            return null;
        }
        String[] strArr = (String[]) obj;
        Log.d("MtkStreamingOptimizationImpl", "lines length:" + strArr.length);
        if (strArr.length <= 0 || (str = strArr[0]) == null || str.length() <= 1) {
            return null;
        }
        return strArr[0].split(",");
    }
}
