package com.android.phone;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.statistics.utils.ObjectSerializer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyAction extends TelephonyActionBase {
    private static final TelephonyAction sIns = new TelephonyAction();
    private Object[] mAtResult = new Object[1];

    private TelephonyAction() {
        this.mActionMaps.put(1, new EgmcAction());
        this.mActionMaps.put(2, new OffsetCellAction());
        this.mActionMaps.put(30, new SbpAction());
        this.mActionMaps.put(40, new GeneralAtAction());
        this.mActionMaps.put(5, new DsdaOffAction());
        this.mActionMaps.put(6, new PingpongNrAction());
        this.mActionMaps.put(7, new PingpongLteAction());
        this.mActionMaps.put(8, new PingpongBarAction());
        this.mActionMaps.put(9, new NrA2CustomizeAction());
        this.mActionMaps.put(10, new ThermalActuatorAction());
        this.mActionMaps.put(11, new SmartIdleAction());
        this.mActionMaps.put(12, new FakeRiAction());
        this.mActionMaps.put(13, new RFAction());
    }

    public static TelephonyAction getInstance() {
        return sIns;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("TelephonyAction", str);
    }

    class OffsetCellAction implements TelephonyActionBase.TeleAction {
        private TelephonyActionBase.ActionReason mReason = TelephonyActionBase.ActionReason.REASON_NONE;

        OffsetCellAction() {
        }

        private boolean prioritizeCell(int i, int i2, boolean z, String str, int i3, int i4, String str2) {
            TelephonyAction.this.log("prioritizeCell");
            StringBuilder sb = new StringBuilder(i2 > 0 ? "AT+EGMC=1,\"LTE_high_priority_cell\"," : "AT+EGMC=1,\"NR_high_priority_cell\",");
            sb.append(z ? "1," : "2,");
            sb.append(str);
            if (str.length() > 5) {
                sb.append(",");
            } else {
                sb.append("f,");
            }
            sb.append(i3 + ",");
            sb.append(i4);
            if (z) {
                sb.append("," + str2);
            }
            FiveGManagerBase.getInstance().sendAtCommand(i, new String[]{sb.toString(), "+EGMC"}, (Message) null);
            return true;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            if (this.mReason.value() > actionReason.value()) {
                TelephonyAction.this.log(this.mReason + " over " + actionReason);
                return false;
            }
            this.mReason = TelephonyActionBase.ActionReason.REASON_NONE;
            int iIntValue = ((Integer) objArr[0]).intValue();
            return prioritizeCell(iIntValue, ((Integer) objArr[2]).intValue(), false, (String) objArr[3], ((Integer) objArr[4]).intValue(), ((Integer) objArr[5]).intValue(), "");
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            if (this.mReason != TelephonyActionBase.ActionReason.REASON_NONE) {
                TelephonyAction.this.log(this.mReason + " already actioned, new action:" + actionReason);
                if (actionReason.value() > this.mReason.value()) {
                    this.mReason = actionReason;
                }
                return false;
            }
            return prioritizeCell(((Integer) objArr[0]).intValue(), ((Integer) objArr[2]).intValue(), true, (String) objArr[3], ((Integer) objArr[4]).intValue(), ((Integer) objArr[5]).intValue(), (String) objArr[1]);
        }
    }

    class SbpAction implements TelephonyActionBase.TeleAction {
        private ConcurrentHashMap mPersistDefaultArgMap;
        private TelephonyActionBase.ActionReason mReason = TelephonyActionBase.ActionReason.REASON_NONE;

        SbpAction() {
            try {
                this.mPersistDefaultArgMap = (ConcurrentHashMap) ObjectSerializer.deserialize(PreferenceUtils.getInstance().getStringPreference("TelephonyAction.SbpAction"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.mPersistDefaultArgMap == null) {
                this.mPersistDefaultArgMap = new ConcurrentHashMap();
            }
        }

        private void updatePersistArgs(boolean z, String str, String str2) {
            TelephonyAction telephonyAction = TelephonyAction.this;
            StringBuilder sb = new StringBuilder();
            sb.append(z ? "add " : "remove ");
            sb.append(str);
            telephonyAction.log(sb.toString());
            ConcurrentHashMap concurrentHashMap = this.mPersistDefaultArgMap;
            if (z) {
                concurrentHashMap.put(str, str2);
            } else {
                concurrentHashMap.remove(str);
            }
            try {
                PreferenceUtils.getInstance().setStringPreference("TelephonyAction.SbpAction", ObjectSerializer.serialize(this.mPersistDefaultArgMap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            boolean zAtCmdRunSync;
            if (this.mReason.value() > actionReason.value()) {
                TelephonyAction.this.log(this.mReason + " over " + actionReason);
                return false;
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            String str = (String) objArr[1];
            int iIndexOf = str.indexOf(34, 3);
            String str2 = (String) this.mPersistDefaultArgMap.get(iIntValue + str);
            if (str2 != null) {
                String str3 = "AT+ESBP=" + str.substring(0, iIndexOf + 1) + "," + str2;
                zAtCmdRunSync = FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, new String[]{str3, "+ESBP"}, null);
                TelephonyAction.this.log("revert to original:" + str3 + " ret:" + zAtCmdRunSync);
            } else {
                String str4 = (String) objArr[2];
                if (TextUtils.isEmpty(str4)) {
                    zAtCmdRunSync = false;
                } else {
                    String str5 = "AT+ESBP=" + str4;
                    zAtCmdRunSync = FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, new String[]{str5, "+ESBP"}, null);
                    TelephonyAction.this.log("revert to:" + str5 + " ret:" + zAtCmdRunSync);
                }
            }
            if (zAtCmdRunSync) {
                updatePersistArgs(false, iIntValue + str, null);
            }
            return zAtCmdRunSync;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            boolean zAtCmdRunSync = false;
            if (this.mReason != TelephonyActionBase.ActionReason.REASON_NONE) {
                TelephonyAction.this.log(this.mReason + " already actioned, new action:" + actionReason);
                if (actionReason.value() > this.mReason.value()) {
                    this.mReason = actionReason;
                }
                return false;
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            String str = (String) objArr[1];
            char cCharAt = str.charAt(0);
            if (cCharAt != '5' && cCharAt != '6') {
                TelephonyAction.this.log("invalid args:".concat(str));
                return false;
            }
            String strSubstring = str.substring(2, str.indexOf(34, 3) + 1);
            StringBuilder sb = new StringBuilder("AT+ESBP=");
            sb.append(cCharAt == '5' ? "7," : "8,");
            sb.append(strSubstring);
            Object[] objArr2 = new Object[1];
            if (FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, new String[]{sb.toString(), "+ESBP"}, objArr2)) {
                String[] strArrSplit = ((String[]) objArr2[0])[0].split(":");
                zAtCmdRunSync = FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, new String[]{"AT+ESBP=".concat(str), "+ESBP"}, objArr2);
                if (zAtCmdRunSync) {
                    updatePersistArgs(true, iIntValue + str, strArrSplit[1].strip());
                }
            }
            return zAtCmdRunSync;
        }
    }

    class EgmcAction implements TelephonyActionBase.TeleAction {
        private final ConcurrentHashMap mDefaultArgMap = new ConcurrentHashMap();
        private TelephonyActionBase.ActionReason mReason = TelephonyActionBase.ActionReason.REASON_NONE;

        EgmcAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            boolean zAtCmdRunSync = false;
            if (this.mReason.value() > actionReason.value()) {
                TelephonyAction.this.log(this.mReason + " over " + actionReason);
                return false;
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            String str = (String) objArr[1];
            String str2 = (String) this.mDefaultArgMap.get(iIntValue + str);
            if (str2 != null) {
                String strConcat = "AT+EGMC=1,".concat(str2);
                zAtCmdRunSync = FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, new String[]{strConcat, "+EGMC"}, null);
                TelephonyAction.this.log("revert to original:" + strConcat + " ret:" + zAtCmdRunSync);
            } else {
                String str3 = (String) objArr[2];
                if (!TextUtils.isEmpty(str3)) {
                    String str4 = "AT+EGMC=" + str3;
                    zAtCmdRunSync = FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, new String[]{str4, "+EGMC"}, null);
                    TelephonyAction.this.log("revert to:" + str4 + " ret:" + zAtCmdRunSync);
                }
            }
            if (zAtCmdRunSync) {
                this.mDefaultArgMap.remove(iIntValue + str);
            }
            return zAtCmdRunSync;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            boolean zAtCmdRunSync = false;
            if (this.mReason != TelephonyActionBase.ActionReason.REASON_NONE) {
                TelephonyAction.this.log(this.mReason + " already actioned, new action:" + actionReason);
                if (actionReason.value() > this.mReason.value()) {
                    this.mReason = actionReason;
                }
                return false;
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            String str = (String) objArr[1];
            if (str.charAt(0) != '1') {
                TelephonyAction.this.log("invalid EGMC args:".concat(str));
                return false;
            }
            Object[] objArr2 = new Object[1];
            if (FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, new String[]{"AT+EGMC=0,".concat(str.substring(2, str.indexOf(34, 3) + 1)), "+EGMC"}, objArr2)) {
                String[] strArrSplit = ((String[]) objArr2[0])[0].split(":");
                zAtCmdRunSync = FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, new String[]{"AT+EGMC=".concat(str), "+EGMC"}, objArr2);
                if (zAtCmdRunSync) {
                    this.mDefaultArgMap.put(iIntValue + str, strArrSplit[1].strip());
                }
            }
            return zAtCmdRunSync;
        }
    }

    class GeneralAtAction implements TelephonyActionBase.TeleAction {
        private TelephonyActionBase.ActionReason mReason = TelephonyActionBase.ActionReason.REASON_NONE;

        GeneralAtAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            if (this.mReason.value() > actionReason.value()) {
                TelephonyAction.this.log(this.mReason + " over " + actionReason);
                return false;
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            String str = (String) objArr[2];
            TelephonyAction.this.log("GeneralAtAction revertAction:" + str);
            if (!TextUtils.isEmpty(str)) {
                String[] strArrSplit = str.split("::");
                if (strArrSplit.length > 0) {
                    boolean zAtCmdRunSync = FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, strArrSplit, null);
                    TelephonyAction.this.log("revert to:" + strArrSplit[0] + " ret:" + zAtCmdRunSync);
                    return zAtCmdRunSync;
                }
            }
            return false;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            if (this.mReason != TelephonyActionBase.ActionReason.REASON_NONE) {
                TelephonyAction.this.log(this.mReason + " already actioned, new action:" + actionReason);
                if (actionReason.value() > this.mReason.value()) {
                    this.mReason = actionReason;
                }
                return false;
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            String str = (String) objArr[1];
            String[] strArrSplit = str.split("::");
            TelephonyAction.this.log("GeneralAtAction:".concat(str));
            if (strArrSplit.length > 0) {
                return FiveGManagerBase.getInstance().atCmdRunSync(iIntValue, strArrSplit, null);
            }
            return false;
        }
    }

    class DsdaOffAction implements TelephonyActionBase.TeleAction {
        DsdaOffAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }
    }

    class PingpongNrAction implements TelephonyActionBase.TeleAction {
        PingpongNrAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }
    }

    class PingpongLteAction implements TelephonyActionBase.TeleAction {
        PingpongLteAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }
    }

    class PingpongBarAction implements TelephonyActionBase.TeleAction {
        PingpongBarAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }
    }

    class NrA2CustomizeAction implements TelephonyActionBase.TeleAction {
        NrA2CustomizeAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }
    }

    class ThermalActuatorAction implements TelephonyActionBase.TeleAction {
        ThermalActuatorAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }
    }

    class RFAction implements TelephonyActionBase.TeleAction {
        private TelephonyActionBase.ActionReason mReason = TelephonyActionBase.ActionReason.REASON_NONE;
        private final Object mReasonLock = new Object();

        RFAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            Log.d("RFAction", "[RFAction] revertAction: External revert disabled, using internal auto-revert only");
            return true;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            synchronized (this.mReasonLock) {
                try {
                    RFParams rFParamsRFValidateAndParseParams = RFValidateAndParseParams(objArr);
                    if (rFParamsRFValidateAndParseParams == null) {
                        return false;
                    }
                    boolean zRFExecuteAtCommandAllSlots = RFExecuteAtCommandAllSlots(rFParamsRFValidateAndParseParams.param1, rFParamsRFValidateAndParseParams.param2);
                    if (zRFExecuteAtCommandAllSlots) {
                        this.mReason = actionReason;
                        Log.d("RFAction", "[RFAction] takeAction: Updated mReason to " + actionReason + ", success=" + zRFExecuteAtCommandAllSlots);
                    } else {
                        Log.w("RFAction", "[RFAction] takeAction: AT command execution failed, performing revert operation");
                        if (RFExecuteCompleteRevert()) {
                            Log.i("RFAction", "[RFAction] takeAction: Revert operation completed successfully");
                        } else {
                            Log.e("RFAction", "[RFAction] takeAction: Revert operation failed, system may be in inconsistent state");
                        }
                    }
                    return zRFExecuteAtCommandAllSlots;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        private RFParams RFValidateAndParseParams(Object... objArr) {
            if (objArr == null || objArr.length < 3) {
                StringBuilder sb = new StringBuilder("takeAction: Insufficient parameters args.length=");
                sb.append(objArr == null ? 0 : objArr.length);
                Log.e("RFAction", sb.toString());
                return null;
            }
            try {
                Object obj = objArr[1];
                if (obj != null && objArr[2] != null) {
                    int iIntValue = obj instanceof Integer ? ((Integer) obj).intValue() : Integer.parseInt(obj.toString());
                    Object obj2 = objArr[2];
                    return new RFParams(iIntValue, obj2 instanceof Integer ? ((Integer) obj2).intValue() : Integer.parseInt(obj2.toString()));
                }
                Log.e("RFAction", "takeAction: Parameters contain null values");
                return null;
            } catch (Exception e) {
                Log.e("RFAction", "takeAction: Parameter type error or conversion failure", e);
                return null;
            }
        }

        private boolean RFExecuteAtCommandAllSlots(int i, int i2) {
            Log.d("RFAction", "[RFAction] takeAction: Applying to all slots, param1=" + i + ", param2=" + i2);
            int phoneCount = TelephonyManager.getDefault().getPhoneCount();
            boolean z = true;
            for (int i3 = 0; i3 < phoneCount; i3++) {
                boolean zAtCmdRunSync = FiveGManagerBase.getInstance().atCmdRunSync(i3, new String[]{"MIPC_SET_THERMAL_ACTUATOR", String.valueOf(i), String.valueOf(i2)}, TelephonyAction.this.mAtResult);
                if (!zAtCmdRunSync) {
                    z = false;
                }
                Log.d("RFAction", "[RFAction] takeAction: slot=" + i3 + ", param1=" + i + ", param2=" + i2 + ", ret=" + zAtCmdRunSync);
            }
            RFLogAtResult("takeAction");
            return z;
        }

        private void RFLogAtResult(String str) {
            if (TelephonyAction.this.mAtResult != null && TelephonyAction.this.mAtResult.length > 0 && TelephonyAction.this.mAtResult[0] != null) {
                if (TelephonyAction.this.mAtResult[0] instanceof String[]) {
                    Log.d("RFAction", "[RFAction] " + str + ": mAtResult=" + Arrays.toString((String[]) TelephonyAction.this.mAtResult[0]));
                    return;
                }
                Log.d("RFAction", "[RFAction] " + str + ": mAtResult=" + TelephonyAction.this.mAtResult[0].toString());
                return;
            }
            Log.d("RFAction", "[RFAction] " + str + ": mAtResult is null or empty");
        }

        private boolean RFExecuteCompleteRevert() {
            Log.d("RFAction", "[RFAction] RFExecuteCompleteRevert: Starting complete RF revert process");
            int phoneCount = TelephonyManager.getDefault().getPhoneCount();
            boolean z = true;
            for (int i = 0; i < phoneCount; i++) {
                if (!FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"MIPC_SET_THERMAL_ACTUATOR", "9", "0"}, TelephonyAction.this.mAtResult)) {
                    z = false;
                }
                if (!FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{"MIPC_SET_THERMAL_ACTUATOR", "2", "0"}, TelephonyAction.this.mAtResult)) {
                    z = false;
                }
            }
            RFLogAtResult("RFExecuteCompleteRevert");
            Log.d("RFAction", "[RFAction] RFExecuteCompleteRevert: Complete revert process finished, overall success=" + z);
            return z;
        }

        class RFParams {
            final int param1;
            final int param2;

            RFParams(int i, int i2) {
                this.param1 = i;
                this.param2 = i2;
            }
        }
    }

    class SmartIdleAction implements TelephonyActionBase.TeleAction {
        SmartIdleAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }
    }

    class FakeRiAction implements TelephonyActionBase.TeleAction {
        FakeRiAction() {
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean revertAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }

        @Override // com.android.phone.TelephonyActionBase.TeleAction
        public boolean takeAction(TelephonyActionBase.ActionReason actionReason, Object... objArr) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(objArr[0]);
            throw null;
        }
    }
}
