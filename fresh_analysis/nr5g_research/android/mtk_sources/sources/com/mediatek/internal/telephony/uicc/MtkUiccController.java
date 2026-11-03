package com.mediatek.internal.telephony.uicc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotMapping;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccException;
import com.android.internal.telephony.uicc.IccSlotStatus;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.MtkIccCardConstants;
import com.mediatek.internal.telephony.MtkTelephonyProperties;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.RadioManager;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.rsu.RsuSml;
import com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkUiccController extends UiccController {
    protected static final String DECRYPT_STATE = "trigger_restart_framework";
    protected static final int EVENT_BASE_ID = 100;
    protected static final int EVENT_CARD_DETECTED_IND = 115;
    protected static final int EVENT_GET_ICC_STATUS_DONE_FOR_SIM_MISSING = 105;
    protected static final int EVENT_GET_ICC_STATUS_DONE_FOR_SIM_RECOVERY = 106;
    protected static final int EVENT_GET_SIM_TYPE_DONE = 119;
    protected static final int EVENT_ICCID_CHANGED_IND = 116;
    protected static final int EVENT_INVALID_SIM_DETECTED = 109;
    protected static final int EVENT_REPOLL_SML_STATE = 110;
    protected static final int EVENT_RSU_SIM_LOCK_CHANGED = 117;
    protected static final int EVENT_SIM_MISSING = 103;
    protected static final int EVENT_SIM_PLUG_IN = 108;
    protected static final int EVENT_SIM_PLUG_OUT = 107;
    protected static final int EVENT_SIM_POWER_CHANGED = 114;
    protected static final int EVENT_SIM_RECOVERY = 104;
    protected static final int EVENT_SIM_TYPE_CHANGED_IND = 118;
    protected static final int EVENT_SML_SLOT_LOCK_INFO_CHANGED = 112;
    protected static final int EVENT_SUPPLY_DEVICE_LOCK_DONE = 113;
    protected static final int EVENT_VIRTUAL_SIM_OFF = 102;
    protected static final int EVENT_VIRTUAL_SIM_ON = 101;
    private static final String LOG_TAG_EX = "MtkUiccCtrl";
    private static final String MEP_MODE_STATUS = "mep_mode_status";
    private static final String MOCK_MODEM_BASEBAND = "mock-modem-service";
    private static final String PROPERTY_SIM_CARD_ONOFF = "ro.vendor.mtk_sim_card_onoff";
    private static final String[] PROPERTY_SIM_ONOFF_STATE = {"vendor.ril.sim.onoff.state1", "vendor.ril.sim.onoff.state2", "vendor.ril.sim.onoff.state3", "vendor.ril.sim.onoff.state4"};
    private static final String PROPERTY_SIM_ONOFF_SUPPORT = "vendor.ril.sim.onoff.support";
    private static final int RSU_EVENT_LOCK_STATE_CHANGE = 2;
    private static final String SLOT_CURRENT_ESIM_ID = "slot_current_esim_id";
    private static final String SLOT_MAPPING_STATUS = "slot_mapping_status";
    private static final int SML_FEATURE_NEED_BROADCAST_INTENT = 1;
    private static final int SML_FEATURE_NO_NEED_BROADCAST_INTENT = 0;
    private static final String SOURCE_SLOT_KEY_STATUS = "slot_mapping_source_eslot_index";
    private int[] mCanChangeType;
    private String[] mIccid;
    private int mLastSimType;
    int mMajorVersion;
    private BroadcastReceiver mMdStateReceiver;
    int mMinorVersion;
    private IMtkRsuSml mMtkRsuSml;
    private int[] mSimPower;
    private int[] mSimPowerExecutingState;
    private int[] mSimType;
    private int[] mSimTypeExecutingState;
    private int[] mSimTypeQueryState;
    private int prevActiveModemCount;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkUiccController(Context c, FeatureFlags featureFlags) {
        super(c, featureFlags);
        this.mMtkRsuSml = null;
        this.prevActiveModemCount = 0;
        Rlog.d(LOG_TAG_EX, "Creating MtkUiccController");
        this.prevActiveModemCount = this.mCis.length;
        for (int i = 0; i < this.mCis.length; i++) {
            Integer index = new Integer(i);
            this.mCis[i].unregisterForAvailable(this);
            this.mCis[i].unregisterForOn(this);
            if (SystemProperties.get("ro.crypto.state").equals("unencrypted") || SystemProperties.get("ro.crypto.state").equals("unsupported") || SystemProperties.get("ro.crypto.type").equals("file") || DECRYPT_STATE.equals(SystemProperties.get("vold.decrypt"))) {
                this.mCis[i].registerForAvailable(this, 6, index);
            } else {
                this.mCis[i].registerForOn(this, 5, index);
            }
            this.mCis[i].registerForSimMissing(this, 103, index);
            this.mCis[i].registerForSimRecovery(this, 104, index);
            this.mCis[i].registerForSimPlugOut(this, EVENT_SIM_PLUG_OUT, index);
            this.mCis[i].registerForSimPlugIn(this, EVENT_SIM_PLUG_IN, index);
            this.mCis[i].registerForSmlSlotLockInfoChanged(this, EVENT_SML_SLOT_LOCK_INFO_CHANGED, index);
            this.mCis[i].registerForRsuSimLockChanged(this, EVENT_RSU_SIM_LOCK_CHANGED, index);
            this.mCis[i].registerForSimPower(this, EVENT_SIM_POWER_CHANGED, index);
            this.mCis[i].registerForCardDetectedInd(this, EVENT_CARD_DETECTED_IND, index);
            this.mCis[i].registerForIccidChanged(this, EVENT_ICCID_CHANGED_IND, index);
            this.mCis[i].registerForSimTypeChanged(this, EVENT_SIM_TYPE_CHANGED_IND, index);
        }
        if (SystemProperties.getInt("ro.vendor.mtk_external_sim_support", 0) == 1) {
            ExternalSimManager.make(c, this.mCis);
        }
        this.mMdStateReceiver = new ModemStateChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RadioManager.ACTION_MODEM_POWER_NO_CHANGE);
        this.mContext.registerReceiver(this.mMdStateReceiver, filter, 4);
        this.mSimPower = new int[this.mCis.length];
        this.mSimPowerExecutingState = new int[this.mCis.length];
        Arrays.fill(this.mSimPower, -1);
        Arrays.fill(this.mSimPowerExecutingState, -1);
        this.mIccid = new String[this.mCis.length];
        Arrays.fill(this.mIccid, "");
        this.mCanChangeType = new int[this.mCis.length];
        this.mSimType = new int[this.mCis.length];
        this.mSimTypeExecutingState = new int[this.mCis.length];
        this.mSimTypeQueryState = new int[this.mCis.length];
        Arrays.fill(this.mCanChangeType, -1);
        Arrays.fill(this.mSimType, -1);
        Arrays.fill(this.mSimTypeExecutingState, -1);
        Arrays.fill(this.mSimTypeQueryState, -1);
        try {
            this.mMtkRsuSml = new RsuSml(this.mContext, this.mCis);
            Rlog.d(LOG_TAG_EX, "[RSU-SIMLOCK] Create RsuSml, mCis=" + this.mCis);
        } catch (Exception e) {
            Rlog.e(LOG_TAG_EX, "[RSU-SIMLOCK] e = " + e);
        }
    }

    public IMtkRsuSml getRsuSml() {
        if (this.mMtkRsuSml == null) {
            Rlog.e(LOG_TAG_EX, "getRsuSml : [RSU-SIMLOCK] Sml not supported");
        }
        return this.mMtkRsuSml;
    }

    /* JADX WARN: Code duplicated, block: B:40:0x00d9 A[Catch: all -> 0x043b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:9:0x0018, B:11:0x001d, B:13:0x0023, B:14:0x0028, B:15:0x002e, B:16:0x0031, B:128:0x0413, B:17:0x0036, B:19:0x0056, B:22:0x005c, B:24:0x006a, B:27:0x0071, B:29:0x00a7, B:32:0x00b0, B:34:0x00b5, B:36:0x00bb, B:37:0x00c6, B:38:0x00c9, B:40:0x00d9, B:42:0x00e4, B:44:0x012c, B:41:0x00df, B:46:0x0131, B:47:0x0153, B:49:0x0155, B:50:0x015c, B:52:0x015e, B:54:0x0166, B:57:0x016b, B:59:0x018d, B:60:0x01c6, B:61:0x01cd, B:63:0x01cf, B:65:0x01d7, B:68:0x01dc, B:70:0x01e5, B:71:0x01ec, B:73:0x01ee, B:74:0x0229, B:75:0x0230, B:77:0x0232, B:78:0x025d, B:80:0x0265, B:83:0x026a, B:85:0x0273, B:86:0x027a, B:88:0x027c, B:90:0x0283, B:92:0x0293, B:94:0x0299, B:95:0x02a1, B:96:0x02dd, B:97:0x02e4, B:99:0x02e6, B:101:0x02f4, B:102:0x02f9, B:103:0x030c, B:104:0x0321, B:107:0x032f, B:109:0x0334, B:111:0x033e, B:112:0x0345, B:114:0x034d, B:116:0x037e, B:117:0x038c, B:118:0x039a, B:119:0x03c0, B:120:0x03d7, B:121:0x03e9, B:123:0x03ed, B:125:0x03f7, B:126:0x03fa, B:127:0x040c, B:130:0x0415, B:131:0x0439), top: B:136:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:41:0x00df A[Catch: all -> 0x043b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:9:0x0018, B:11:0x001d, B:13:0x0023, B:14:0x0028, B:15:0x002e, B:16:0x0031, B:128:0x0413, B:17:0x0036, B:19:0x0056, B:22:0x005c, B:24:0x006a, B:27:0x0071, B:29:0x00a7, B:32:0x00b0, B:34:0x00b5, B:36:0x00bb, B:37:0x00c6, B:38:0x00c9, B:40:0x00d9, B:42:0x00e4, B:44:0x012c, B:41:0x00df, B:46:0x0131, B:47:0x0153, B:49:0x0155, B:50:0x015c, B:52:0x015e, B:54:0x0166, B:57:0x016b, B:59:0x018d, B:60:0x01c6, B:61:0x01cd, B:63:0x01cf, B:65:0x01d7, B:68:0x01dc, B:70:0x01e5, B:71:0x01ec, B:73:0x01ee, B:74:0x0229, B:75:0x0230, B:77:0x0232, B:78:0x025d, B:80:0x0265, B:83:0x026a, B:85:0x0273, B:86:0x027a, B:88:0x027c, B:90:0x0283, B:92:0x0293, B:94:0x0299, B:95:0x02a1, B:96:0x02dd, B:97:0x02e4, B:99:0x02e6, B:101:0x02f4, B:102:0x02f9, B:103:0x030c, B:104:0x0321, B:107:0x032f, B:109:0x0334, B:111:0x033e, B:112:0x0345, B:114:0x034d, B:116:0x037e, B:117:0x038c, B:118:0x039a, B:119:0x03c0, B:120:0x03d7, B:121:0x03e9, B:123:0x03ed, B:125:0x03f7, B:126:0x03fa, B:127:0x040c, B:130:0x0415, B:131:0x0439), top: B:136:0x0003 }] */
    /* JADX WARN: Code duplicated, block: B:44:0x012c A[Catch: all -> 0x043b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:9:0x0018, B:11:0x001d, B:13:0x0023, B:14:0x0028, B:15:0x002e, B:16:0x0031, B:128:0x0413, B:17:0x0036, B:19:0x0056, B:22:0x005c, B:24:0x006a, B:27:0x0071, B:29:0x00a7, B:32:0x00b0, B:34:0x00b5, B:36:0x00bb, B:37:0x00c6, B:38:0x00c9, B:40:0x00d9, B:42:0x00e4, B:44:0x012c, B:41:0x00df, B:46:0x0131, B:47:0x0153, B:49:0x0155, B:50:0x015c, B:52:0x015e, B:54:0x0166, B:57:0x016b, B:59:0x018d, B:60:0x01c6, B:61:0x01cd, B:63:0x01cf, B:65:0x01d7, B:68:0x01dc, B:70:0x01e5, B:71:0x01ec, B:73:0x01ee, B:74:0x0229, B:75:0x0230, B:77:0x0232, B:78:0x025d, B:80:0x0265, B:83:0x026a, B:85:0x0273, B:86:0x027a, B:88:0x027c, B:90:0x0283, B:92:0x0293, B:94:0x0299, B:95:0x02a1, B:96:0x02dd, B:97:0x02e4, B:99:0x02e6, B:101:0x02f4, B:102:0x02f9, B:103:0x030c, B:104:0x0321, B:107:0x032f, B:109:0x0334, B:111:0x033e, B:112:0x0345, B:114:0x034d, B:116:0x037e, B:117:0x038c, B:118:0x039a, B:119:0x03c0, B:120:0x03d7, B:121:0x03e9, B:123:0x03ed, B:125:0x03f7, B:126:0x03fa, B:127:0x040c, B:130:0x0415, B:131:0x0439), top: B:136:0x0003 }] */
    public void handleMessage(Message msg) {
        Intent simTypeIntent;
        synchronized (mLock) {
            Integer index = getCiIndex(msg);
            if (index.intValue() >= 0 && index.intValue() < this.mCis.length) {
                if (msg.obj != null && (msg.obj instanceof AsyncResult)) {
                }
                switch (msg.what) {
                    case 10:
                        int activeModemCount = ((Integer) ((AsyncResult) msg.obj).result).intValue();
                        this.prevActiveModemCount = this.mCis.length;
                        for (int i = activeModemCount; i < this.prevActiveModemCount; i++) {
                            UiccSlot uiccSlot = getUiccSlot(getSlotIdFromPhoneId(i));
                            if (uiccSlot != null) {
                                uiccSlot.onRadioStateUnavailable(i);
                            }
                            this.mIccChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(i), (Throwable) null));
                        }
                        super.handleMessage(msg);
                        onMultiSimConfigChangedEx(activeModemCount);
                        break;
                    case 103:
                        mtkLog("handleMessage (EVENT_SIM_MISSING)");
                        this.mCis[index.intValue()].getIccCardStatus(obtainMessage(105, index));
                        break;
                    case 104:
                        mtkLog("handleMessage (EVENT_SIM_RECOVERY)");
                        this.mCis[index.intValue()].getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE_FOR_SIM_RECOVERY, index));
                        Intent intent = new Intent();
                        intent.setAction("com.mediatek.phone.ACTION_SIM_RECOVERY_DONE");
                        this.mContext.sendBroadcast(intent);
                        break;
                    case 105:
                        mtkLog("Received EVENT_GET_ICC_STATUS_DONE_FOR_SIM_MISSING");
                        AsyncResult ar = (AsyncResult) msg.obj;
                        onGetIccCardStatusDone(ar, index);
                        break;
                    case EVENT_GET_ICC_STATUS_DONE_FOR_SIM_RECOVERY /* 106 */:
                        mtkLog("Received EVENT_GET_ICC_STATUS_DONE_FOR_SIM_RECOVERY");
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        onGetIccCardStatusDone(ar2, index);
                        break;
                    case 110:
                        mtkLog("Received EVENT_REPOLL_SML_STATE");
                        AsyncResult ar3 = (AsyncResult) msg.obj;
                        boolean needIntent = msg.arg1 == 1;
                        onGetIccCardStatusDone(ar3, index);
                        if (needIntent) {
                            UiccCardApplication app = getUiccCardApplication(index.intValue(), 1);
                            if (app == null) {
                                mtkLog("UiccCardApplication = null");
                            } else if (app.getState() == IccCardApplicationStatus.AppState.APPSTATE_SUBSCRIPTION_PERSO) {
                                Intent lockIntent = new Intent();
                                mtkLog("Broadcast ACTION_UNLOCK_SIM_LOCK");
                                lockIntent.setAction("com.mediatek.phone.ACTION_UNLOCK_SIM_LOCK");
                                lockIntent.putExtra("ss", "LOCKED");
                                lockIntent.putExtra(DataSubConstants.EXTRA_MOBILE_DATA_ENABLE_REASON, parsePersoType(app.getPersoSubState()));
                                SubscriptionManager.putPhoneIdAndSubIdExtra(lockIntent, index.intValue());
                                this.mContext.sendBroadcast(lockIntent);
                            }
                        }
                        break;
                    case EVENT_SML_SLOT_LOCK_INFO_CHANGED /* 112 */:
                        mtkLog("handleMessage (EVENT_SML_SLOT_LOCK_INFO_CHANGED)");
                        AsyncResult ar4 = (AsyncResult) msg.obj;
                        onSmlSlotLoclInfoChaned(ar4, index);
                        triggerUpdateInternalSimMountState(index.intValue());
                        break;
                    case EVENT_SUPPLY_DEVICE_LOCK_DONE /* 113 */:
                        mtkLog("handleMessage (EVENT_SUPPLY_DEVICE_LOCK_DONE)");
                        int attemptsRemaining = -1;
                        AsyncResult ar5 = (AsyncResult) msg.obj;
                        if (ar5.result != null) {
                            attemptsRemaining = parseUnlockDeviceResult(ar5);
                        }
                        Message response = (Message) ar5.userObj;
                        AsyncResult.forMessage(response).exception = ar5.exception;
                        response.arg1 = attemptsRemaining;
                        response.sendToTarget();
                        break;
                    case EVENT_SIM_POWER_CHANGED /* 114 */:
                        AsyncResult ar6 = (AsyncResult) msg.obj;
                        if (ar6.exception == null && ar6.result != null) {
                            if (index.intValue() >= this.mSimPower.length) {
                                Rlog.e(LOG_TAG_EX, "EVENT_SIM_POWER_CHANGED index error");
                                return;
                            }
                            int[] state = (int[]) ar6.result;
                            if (state.length == 1) {
                                this.mSimPower[index.intValue()] = state[0];
                                if (state[0] == 10 || state[0] == 11) {
                                    this.mSimPowerExecutingState[index.intValue()] = -1;
                                }
                            }
                            mtkLog("Received EVENT_SIM_POWER_CHANGED, index: " + index + " simPower: " + this.mSimPower[index.intValue()] + " mSimPowerExecutingState = " + this.mSimPowerExecutingState[index.intValue()]);
                            break;
                        }
                        Rlog.e(LOG_TAG_EX, "EVENT_SIM_POWER_CHANGED exception");
                        return;
                    case EVENT_CARD_DETECTED_IND /* 115 */:
                        Intent cardDetectedInd = new Intent("com.mediatek.phone.ACTION_CARD_DETECTED");
                        int slotId = index.intValue();
                        SubscriptionManager.putPhoneIdAndSubIdExtra(cardDetectedInd, slotId);
                        mtkLog("Broadcasting intent ACTION_CARD_DETECTED, mSlotId : " + slotId);
                        this.mContext.sendBroadcast(cardDetectedInd);
                        break;
                    case EVENT_ICCID_CHANGED_IND /* 116 */:
                        AsyncResult ar7 = (AsyncResult) msg.obj;
                        if (ar7.exception == null && ar7.result != null) {
                            if (index.intValue() >= this.mIccid.length) {
                                Rlog.e(LOG_TAG_EX, "EVENT_ICCID_CHANGED_IND index error");
                                return;
                            }
                            updateAbsentFromIccid((String) ar7.result, index.intValue());
                            this.mIccid[index.intValue()] = (String) ar7.result;
                            mtkLog("Received EVENT_ICCID_CHANGED_IND, index: " + index + ", iccid: " + this.mIccid);
                            break;
                        }
                        Rlog.e(LOG_TAG_EX, "EVENT_ICCID_CHANGED_IND exception");
                        return;
                    case EVENT_RSU_SIM_LOCK_CHANGED /* 117 */:
                        AsyncResult ar8 = (AsyncResult) msg.obj;
                        if (ar8.exception == null && ar8.result != null) {
                            int[] event = (int[]) ar8.result;
                            mtkLog("EVENT_RSU_SIM_LOCK_CHANGED eventId = " + event[0]);
                            int eventId = event[0] & 255;
                            if (eventId == 2) {
                                int lockState = (event[0] >> 8) & 255;
                                Intent rsuInd = new Intent(MtkTelephonyManagerEx.ACTION_SIM_ME_LOCK_STATE_CHANGED);
                                rsuInd.putExtra(MtkTelephonyManagerEx.INTENT_KEY_LOCK_STATE, lockState);
                                mtkLog("Broadcasting intent ACTION_SIM_ME_LOCK_STATE_CHANGED, index: " + index + ", lockState: " + lockState);
                                this.mContext.sendBroadcast(rsuInd);
                            }
                            break;
                        }
                        Rlog.e(LOG_TAG_EX, "EVENT_RSU_SIM_LOCK_CHANGED exception");
                        return;
                    case EVENT_SIM_TYPE_CHANGED_IND /* 118 */:
                    case EVENT_GET_SIM_TYPE_DONE /* 119 */:
                        mtkLog("MtkUiccController.handleMessage, msgid is " + msg.what);
                        AsyncResult ar9 = (AsyncResult) msg.obj;
                        if (ar9.exception == null && ar9.result != null) {
                            SimTypeInfoExt simTypeInfoExt = (SimTypeInfoExt) ar9.result;
                            int slotIndex = this.mPhoneIdToSlotId[index.intValue()];
                            if (slotIndex >= 0 && slotIndex < this.mSimType.length) {
                                this.mCanChangeType[slotIndex] = simTypeInfoExt.getSupport();
                                this.mSimType[slotIndex] = simTypeInfoExt.getCurrentType();
                                mtkLog("simTypeInfoExt info is " + simTypeInfoExt + ", in slot " + slotIndex);
                                if (simTypeInfoExt.getSource() == 1 || simTypeInfoExt.getSource() == 2) {
                                    int sourceSlot = -1;
                                    int executingMode = -1;
                                    for (int i2 = 0; i2 < this.mSimTypeExecutingState.length; i2++) {
                                        if (this.mSimTypeExecutingState[i2] != -1) {
                                            sourceSlot = i2;
                                            executingMode = this.mSimTypeExecutingState[i2];
                                            this.mSimTypeExecutingState[i2] = -1;
                                            simTypeIntent = new Intent();
                                            simTypeIntent.setAction("com.mediatek.intent.action.ACTION_SIM_TYPE_CHANGED");
                                            if (simTypeInfoExt.getSupport() != 1) {
                                                simTypeIntent.putExtra("SIM_TYPE_RESULT", false);
                                            } else {
                                                simTypeIntent.putExtra("SIM_TYPE_RESULT", true);
                                            }
                                            simTypeIntent.putExtra("SIM_TYPE", simTypeInfoExt.getCurrentType());
                                            simTypeIntent.putExtra("SIM_TYPE_SLOT", index);
                                            simTypeIntent.putExtra("SIM_TYPE_SRC_SLOT", sourceSlot);
                                            this.mContext.sendBroadcast(simTypeIntent);
                                            mtkLog("Broadcasting sim type changed for slot: " + index + ", currentType is: " + simTypeInfoExt.getCurrentType() + ", sourceSlot: " + sourceSlot);
                                            if (executingMode == 1) {
                                                saveSourceEsimSlotIndex(sourceSlot);
                                            }
                                        }
                                        break;
                                    }
                                    simTypeIntent = new Intent();
                                    simTypeIntent.setAction("com.mediatek.intent.action.ACTION_SIM_TYPE_CHANGED");
                                    if (simTypeInfoExt.getSupport() != 1) {
                                        simTypeIntent.putExtra("SIM_TYPE_RESULT", false);
                                    } else {
                                        simTypeIntent.putExtra("SIM_TYPE_RESULT", true);
                                    }
                                    simTypeIntent.putExtra("SIM_TYPE", simTypeInfoExt.getCurrentType());
                                    simTypeIntent.putExtra("SIM_TYPE_SLOT", index);
                                    simTypeIntent.putExtra("SIM_TYPE_SRC_SLOT", sourceSlot);
                                    this.mContext.sendBroadcast(simTypeIntent);
                                    mtkLog("Broadcasting sim type changed for slot: " + index + ", currentType is: " + simTypeInfoExt.getCurrentType() + ", sourceSlot: " + sourceSlot);
                                    if (executingMode == 1) {
                                        saveSourceEsimSlotIndex(sourceSlot);
                                    }
                                }
                                break;
                            }
                            Rlog.e(LOG_TAG_EX, "SIM_TYPE info incorrect slotIndex with: " + slotIndex + ", index: " + index);
                            return;
                        }
                        Rlog.e(LOG_TAG_EX, "SIM_TYPE info got exception");
                        return;
                    default:
                        super.handleMessage(msg);
                        break;
                }
                return;
            }
            Rlog.e(LOG_TAG_EX, "Invalid index : " + index + " received with event " + msg.what);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onMultiSimConfigChangedEx(int newActiveModemCount) {
        int prevActualActiveModemCount = this.mIccid.length;
        this.mCis = PhoneFactory.getCommandsInterfaces();
        this.mSimPowerExecutingState = Arrays.copyOf(this.mSimPowerExecutingState, newActiveModemCount);
        this.mSimPower = Arrays.copyOf(this.mSimPower, newActiveModemCount);
        this.mCanChangeType = Arrays.copyOf(this.mCanChangeType, newActiveModemCount);
        this.mSimType = Arrays.copyOf(this.mSimType, newActiveModemCount);
        this.mSimTypeExecutingState = Arrays.copyOf(this.mSimTypeExecutingState, newActiveModemCount);
        this.mSimTypeQueryState = Arrays.copyOf(this.mSimTypeQueryState, newActiveModemCount);
        this.mIccid = (String[]) Arrays.copyOf(this.mIccid, newActiveModemCount);
        for (int i = prevActualActiveModemCount; i < newActiveModemCount; i++) {
            this.mPhoneIdToSlotId[i] = -1;
            this.mSimPowerExecutingState[i] = -1;
            this.mSimPower[i] = -1;
            this.mIccid[i] = "";
            this.mCanChangeType[i] = -1;
            this.mSimType[i] = -1;
            this.mSimTypeExecutingState[i] = -1;
            this.mSimTypeQueryState[i] = -1;
        }
        for (int i2 = this.prevActiveModemCount; i2 < newActiveModemCount; i2++) {
            Integer index = new Integer(i2);
            this.mSimPowerExecutingState[i2] = -1;
            this.mSimPower[i2] = -1;
            this.mIccid[i2] = "";
            this.mCanChangeType[i2] = -1;
            this.mSimType[i2] = -1;
            this.mSimTypeExecutingState[i2] = -1;
            this.mSimTypeQueryState[i2] = -1;
            this.mCis[i2].registerForSimMissing(this, 103, index);
            this.mCis[i2].registerForSimRecovery(this, 104, index);
            this.mCis[i2].registerForSimPlugOut(this, EVENT_SIM_PLUG_OUT, index);
            this.mCis[i2].registerForSimPlugIn(this, EVENT_SIM_PLUG_IN, index);
            this.mCis[i2].registerForSmlSlotLockInfoChanged(this, EVENT_SML_SLOT_LOCK_INFO_CHANGED, index);
            this.mCis[i2].registerForSimPower(this, EVENT_SIM_POWER_CHANGED, index);
            this.mCis[i2].registerForCardDetectedInd(this, EVENT_CARD_DETECTED_IND, index);
            this.mCis[i2].registerForIccidChanged(this, EVENT_ICCID_CHANGED_IND, index);
        }
    }

    private void onSmlSlotLoclInfoChaned(AsyncResult ar, Integer index) {
        if (ar.exception != null || ar.result == null) {
            Rlog.e(LOG_TAG_EX, "onSmlSlotLoclInfoChaned exception");
            return;
        }
        int[] info = (int[]) ar.result;
        if (info.length != 4) {
            Rlog.e(LOG_TAG_EX, "onSmlSlotLoclInfoChaned exception");
            return;
        }
        mtkLog("onSmlSlotLoclInfoChaned, infomation:,lock policy:" + info[0] + ",lock state:" + info[1] + ",service capability:" + info[2] + ",sim valid:" + info[3]);
        Intent smlLockInfoChanged = new Intent("com.mediatek.phone.ACTION_SIM_SLOT_LOCK_POLICY_INFORMATION");
        int slotId = index.intValue();
        SubscriptionManager.putPhoneIdAndSubIdExtra(smlLockInfoChanged, slotId);
        smlLockInfoChanged.putExtra("slot", slotId);
        smlLockInfoChanged.putExtra(MtkIccCardConstants.INTENT_KEY_SML_SLOT_DEVICE_LOCK_POLICY, info[0]);
        smlLockInfoChanged.putExtra(MtkIccCardConstants.INTENT_KEY_SML_SLOT_DEVICE_LOCK_STATE, info[1]);
        smlLockInfoChanged.putExtra(MtkIccCardConstants.INTENT_KEY_SML_SLOT_SIM_SERVICE_CAPABILITY, info[2]);
        smlLockInfoChanged.putExtra(MtkIccCardConstants.INTENT_KEY_SML_SLOT_SIM_VALID, info[3]);
        mtkLog("Broadcasting intent ACTION_SIM_SLOT_LOCK_POLICY_INFORMATION for mSlotId : " + slotId);
        this.mContext.sendBroadcastAsUser(smlLockInfoChanged, UserHandle.ALL);
    }

    private void triggerUpdateInternalSimMountState(int phoneId) {
        mtkLog("triggerUpdateInternalSimMountState.");
        MtkSubscriptionManagerService.getInstance().triggerUpdateInternalSimMountState(phoneId);
    }

    public void supplyDeviceNetworkDepersonalization(String pwd, Message onComplete) {
        this.mCis[0].supplyDeviceNetworkDepersonalization(pwd, obtainMessage(EVENT_SUPPLY_DEVICE_LOCK_DONE, onComplete));
    }

    private int parseUnlockDeviceResult(AsyncResult ar) {
        int[] result = (int[]) ar.result;
        if (result == null) {
            return -1;
        }
        int length = result.length;
        int attemptsRemaining = -1;
        if (length > 0) {
            attemptsRemaining = result[0];
        }
        mtkLog("parseUnlockDeviceResult: attemptsRemaining=" + attemptsRemaining);
        return attemptsRemaining;
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.uicc.MtkUiccController$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState = new int[IccCardApplicationStatus.PersoSubState.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK_SUBSET.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_CORPORATE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SERVICE_PROVIDER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private String parsePersoType(IccCardApplicationStatus.PersoSubState state) {
        mtkLog("parsePersoType, state = " + state);
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[state.ordinal()]) {
            case 1:
                return "NETWORK";
            case 2:
                return MtkIccCardConstants.INTENT_VALUE_LOCKED_NETWORK_SUBSET;
            case 3:
                return MtkIccCardConstants.INTENT_VALUE_LOCKED_CORPORATE;
            case 4:
                return MtkIccCardConstants.INTENT_VALUE_LOCKED_SERVICE_PROVIDER;
            case 5:
                return MtkIccCardConstants.INTENT_VALUE_LOCKED_SIM;
            default:
                return "UNKNOWN";
        }
    }

    public void repollIccStateForModemSmlChangeFeatrue(int slotId, boolean needIntent) {
        mtkLog("repollIccStateForModemSmlChangeFeatrue, needIntent = " + needIntent);
        int arg1 = !needIntent ? 0 : 1;
        this.mCis[slotId].getIccCardStatus(obtainMessage(110, arg1, 0, Integer.valueOf(slotId)));
    }

    public boolean ignoreGetSimStatus() {
        int airplaneMode = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0);
        mtkLog("ignoreGetSimStatus(): airplaneMode - " + airplaneMode);
        if (!RadioManager.isFlightModePowerOffModemEnabled() || airplaneMode != 1) {
            return false;
        }
        mtkLog("ignoreGetSimStatus(): return true");
        return true;
    }

    protected void mtkLog(String string) {
        Rlog.d(LOG_TAG_EX, string);
    }

    public boolean isAllRadioAvailable() {
        boolean isRadioReady = true;
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            if (2 == this.mCis[i].getRadioState()) {
                isRadioReady = false;
            }
        }
        mtkLog("isAllRadioAvailable = " + isRadioReady);
        return isRadioReady;
    }

    public void resetRadioForVsim() {
        mtkLog("resetRadioForVsim...resetRadio");
        int mainPhoneId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
        this.mCis[mainPhoneId].restartRILD(null);
    }

    public static MtkIccCardConstants.VsimType getVsimCardType(int slotId) {
        int rSim = SystemProperties.getInt(MtkTelephonyProperties.PROPERTY_PREFERED_REMOTE_SIM, -1);
        int akaSim = SystemProperties.getInt(MtkTelephonyProperties.PROPERTY_PREFERED_AKA_SIM, -1);
        boolean isVsim = false;
        TelephonyManager.getDefault();
        String inserted = TelephonyManager.getTelephonyProperty(slotId, MtkTelephonyProperties.PROPERTY_EXTERNAL_SIM_INSERTED, "0");
        if (inserted != null && inserted.length() > 0 && !"0".equals(inserted)) {
            isVsim = true;
        }
        if (slotId == rSim && isVsim) {
            return MtkIccCardConstants.VsimType.REMOTE_SIM;
        }
        if (slotId == akaSim) {
            if (isVsim) {
                return MtkIccCardConstants.VsimType.SOFT_AKA_SIM;
            }
            return MtkIccCardConstants.VsimType.PHYSICAL_AKA_SIM;
        }
        if (rSim == -1 && akaSim == -1 && isVsim) {
            return MtkIccCardConstants.VsimType.LOCAL_SIM;
        }
        return MtkIccCardConstants.VsimType.PHYSICAL_SIM;
    }

    private class ModemStateChangedReceiver extends BroadcastReceiver {
        private ModemStateChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context content, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RadioManager.ACTION_MODEM_POWER_NO_CHANGE)) {
                for (int i = 0; i < MtkUiccController.this.mCis.length; i++) {
                    Integer index = new Integer(i);
                    Message msg = MtkUiccController.this.obtainMessage(1, index);
                    MtkUiccController.this.sendMessage(msg);
                    MtkUiccController.this.mtkLog("Trigger GET_SIM_STATUS due to modem state changed for slot " + i);
                }
            }
        }
    }

    public void switchSimType(int phoneId, int mode, Message onComplete) {
        if (TelephonyManager.getDefault().getActiveModemCount() <= 1) {
            mtkLog("switchSimType: may not support in SS mode.");
            AsyncResult.forMessage(onComplete, (Object) null, new IccException("switchSimType isn't supported"));
            onComplete.sendToTarget();
            return;
        }
        int srcSlotId = getSlotIdFromPhoneId(phoneId);
        int slotId = resetCorrectSlotId(srcSlotId, mode);
        if (slotId != srcSlotId) {
            phoneId = findPhoneIdBySlotId(slotId);
        }
        if (phoneId < 0 || phoneId >= this.mCis.length) {
            mtkLog("switchSimType: invalid phoneId: " + phoneId);
            AsyncResult.forMessage(onComplete, (Object) null, new IccException("switchSimType isn't supported"));
            onComplete.sendToTarget();
        } else {
            this.mSimTypeExecutingState[slotId] = mode;
            SwitchSimTypeHandler switchSimTypeHandler = new SwitchSimTypeHandler(mode, phoneId, onComplete);
            switchSimTypeHandler.switchSlotsOrSimType();
        }
    }

    public int getCurrentSimType(int phoneId) {
        int slotId = getSlotIdFromPhoneId(phoneId);
        if (slotId < 0 || slotId >= this.mSimType.length) {
            mtkLog("getCurrentIccCardType: invalid slotId " + slotId);
            return -1;
        }
        return this.mSimType[slotId];
    }

    public int getSimTypeExecutingStatus(int phoneId) {
        int slotId = getSlotIdFromPhoneId(phoneId);
        if (slotId < 0 || slotId >= this.mSimTypeExecutingState.length) {
            mtkLog("getSimTypeExecutingStatus, invalid slotId: " + slotId);
            return -1;
        }
        return this.mSimTypeExecutingState[slotId];
    }

    public int getSimTypeExecutingStatus() {
        for (int i = 0; i < this.mSimTypeExecutingState.length; i++) {
            if (this.mSimTypeExecutingState[i] >= 0) {
                return this.mSimTypeExecutingState[i];
            }
        }
        return -1;
    }

    public boolean supportSimTypeSwitch() {
        if (TelephonyManager.getDefault().getActiveModemCount() <= 1) {
            mtkLog("canSwitchSimType: may not support in SS mode.");
            return false;
        }
        for (int i = 0; i < this.mCis.length; i++) {
            if (this.mCanChangeType[i] == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean canSwitchSimType(int phoneId) {
        int slotId = getSlotIdFromPhoneId(phoneId);
        if (TelephonyManager.getDefault().getActiveModemCount() <= 1) {
            mtkLog("canSwitchSimType: may not support in SS mode.");
            return false;
        }
        if (slotId < 0 || slotId >= this.mCanChangeType.length) {
            mtkLog("canSwitchSimType: invalid slotId " + slotId);
            return false;
        }
        for (int i = 0; i < this.mSimType.length; i++) {
            if (i != slotId && this.mSimType[i] == 1 && this.mCanChangeType[i] == 1) {
                mtkLog("canSwitchSimType: already switch to eSIM in slot: " + i);
                return false;
            }
        }
        return this.mCanChangeType[slotId] == 1;
    }

    public void setSimPower(int slotId, int state, Message onComplete) {
        this.mCis[slotId].setSimPower(state, onComplete);
    }

    public int getSimOnOffState(int slotId) {
        if (slotId < 0 || slotId >= this.mSimPower.length) {
            mtkLog("getSimOnOffState: invalid slotId " + slotId);
            return -1;
        }
        boolean onoffAPSupport = SystemProperties.get("ro.vendor.mtk_sim_card_onoff").equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN);
        boolean onoffMDSupport = SystemProperties.get("vendor.ril.sim.onoff.support").equals(RadioCapabilitySwitchUtil.IMSI_READY);
        int onoffState = SystemProperties.getInt(PROPERTY_SIM_ONOFF_STATE[slotId], -1);
        mtkLog("getSimOnOffState slotId = " + slotId + " onoffAPSupport = " + onoffAPSupport + " onoffMDSupport = " + onoffMDSupport + " mSimPower = " + this.mSimPower[slotId] + " onoffState = " + onoffState);
        if (onoffAPSupport && onoffMDSupport) {
            if (this.mSimPower[slotId] == -1) {
                return onoffState;
            }
            return this.mSimPower[slotId];
        }
        return 11;
    }

    public void setSimOnOffExecutingState(int slotId, int state) {
        this.mSimPowerExecutingState[slotId] = state;
    }

    public int getSimOnOffExecutingState(int slotId) {
        if (slotId < 0 || slotId >= this.mSimPowerExecutingState.length) {
            mtkLog("getSimOnOffExecutingState: invalid slotId " + slotId);
            return -1;
        }
        mtkLog("getSimOnOffExecutingState slotId = " + slotId + " mSimPowerExecutingState = " + this.mSimPowerExecutingState[slotId]);
        return this.mSimPowerExecutingState[slotId];
    }

    public String getIccid(int phoneId) {
        if (phoneId < 0 || phoneId >= this.mIccid.length) {
            mtkLog("getIccid: invalid phoneId " + phoneId);
            return "";
        }
        if (isMockModemBinded()) {
            int slotId = getSlotIdFromPhoneId(phoneId);
            if (slotId < 0 || slotId >= this.mSimType.length) {
                mtkLog("getCurrentIccCardType: invalid slotId " + slotId);
                return "";
            }
            UiccSlot uiccSlot = getUiccSlot(slotId);
            if (uiccSlot != null) {
                int protIdx = uiccSlot.getPortIndexFromPhoneId(phoneId);
                return uiccSlot.getIccId(protIdx);
            }
        }
        mtkLog("getIccid: phoneId = " + phoneId);
        return this.mIccid[phoneId];
    }

    private void updateAbsentFromIccid(String iccid, int phoneId) {
        int slotIndex = getSlotIdFromPhoneId(phoneId);
        UiccSlot uiccSlot = getUiccSlot(slotIndex);
        if (uiccSlot == null || uiccSlot.getCardState() != IccCardStatus.CardState.CARDSTATE_ABSENT) {
            return;
        }
        if ((this.mIccid[phoneId] != null && (this.mIccid[phoneId].equals(DataSubConstants.NO_SIM_VALUE) || this.mIccid[phoneId].equals(""))) || !iccid.equals(DataSubConstants.NO_SIM_VALUE)) {
            return;
        }
        mtkLog("updateAbsentFromIccid");
        updateSimState(phoneId, IccCardConstants.State.ABSENT, null);
    }

    public void setSimTypeQuereState(int phoneId) {
        if (phoneId >= this.mSimTypeQueryState.length) {
            return;
        }
        this.mSimTypeQueryState[phoneId] = 1;
    }

    public void fetchSimTypeInfo(int phoneId) {
        Pair<Integer, Integer> halVersion;
        if (phoneId == 0 && this.mSimTypeQueryState[phoneId] == 1 && (halVersion = TelephonyManager.getDefault().getHalVersion(5)) != null) {
            this.mMajorVersion = ((Integer) halVersion.first).intValue();
            this.mMinorVersion = ((Integer) halVersion.second).intValue();
            mtkLog("fetchSimTypeInfo, mMajorVersion = " + this.mMajorVersion + ", mMinorVersion = " + this.mMinorVersion);
        }
        if (phoneId < this.mSimTypeQueryState.length && this.mSimTypeQueryState[phoneId] == 1 && this.mPhoneIdToSlotId[phoneId] >= 0) {
            mtkLog("fetchSimTypeInfo with phoneId: " + phoneId);
            this.mCis[phoneId].getSimTypeInfo(obtainMessage(EVENT_GET_SIM_TYPE_DONE, Integer.valueOf(phoneId)));
            this.mSimTypeQueryState[phoneId] = 0;
        }
    }

    public boolean switchSlotsInternal(List<UiccSlotMapping> slotMapping, Message response) {
        boolean simTypeSwitch = isSimTypeChangeRequest(slotMapping);
        mtkLog("switchSlotsInternal: " + slotMapping + ", simTypeSwitch = " + simTypeSwitch);
        if (simTypeSwitch) {
            return false;
        }
        SwitchSlotsHandler switchSlotsHandler = new SwitchSlotsHandler(slotMapping, response);
        switchSlotsHandler.startToSwitchSlots();
        return true;
    }

    public IccSlotStatus.MultipleEnabledProfilesMode getCurrentSavedMepMode(int slotId) {
        if (this.mSimType[slotId] == -1) {
            return null;
        }
        int mepModeValue = getSavedMepModeValue(slotId) >> (slotId * 4);
        mtkLog("getCurrentSavedMepMode, mepModeValue = " + mepModeValue);
        if ((mepModeValue & 15) == 1) {
            return IccSlotStatus.MultipleEnabledProfilesMode.MEP_A1;
        }
        return null;
    }

    private int findPhoneIdBySlotId(int slotId) {
        int phoneId = getPhoneIdFromSlotPortIndex(slotId, 0);
        if (phoneId == -1) {
            return getPhoneIdFromSlotPortIndex(slotId, 1);
        }
        return phoneId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needSaveSlotMappingValue() {
        if (TelephonyManager.getDefault().getActiveModemCount() <= 1 || this.mSimType == null || this.mSimType.length < 2) {
            return false;
        }
        boolean hasEsimCard = false;
        boolean canSwitchSim = false;
        for (int i = 0; i < this.mSimType.length; i++) {
            if (this.mSimType[i] == 1) {
                hasEsimCard = true;
            }
            if (this.mCanChangeType[i] == 1) {
                canSwitchSim = true;
            }
        }
        return hasEsimCard && canSwitchSim;
    }

    private void saveSourceEsimSlotIndex(int sourceSlotValue) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editor.putInt(SOURCE_SLOT_KEY_STATUS, sourceSlotValue);
        editor.commit();
        mtkLog("saveSourceEsimSlotIndex, sourceSlotKey = " + SOURCE_SLOT_KEY_STATUS + ", sourceSlotValue = " + sourceSlotValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveSlotMappingValue(int slotMappingValue) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        if (slotMappingValue != 0) {
            editor.putInt(SLOT_MAPPING_STATUS, slotMappingValue);
            editor.commit();
            mtkLog("saveSlotMappingValue, slotMappingKey = " + SLOT_MAPPING_STATUS + ", slotMappingValue = " + slotMappingValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCurrentMepMode() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        int mepModeValue = 0;
        int count = TelephonyManager.getDefault().getActiveModemCount();
        for (int i = 0; i < count; i++) {
            UiccSlot slot = getUiccSlot(i);
            if (slot != null) {
                IccSlotStatus.MultipleEnabledProfilesMode mepMode = slot.getSupportedMepMode();
                if (mepMode.isMepAMode()) {
                    mepModeValue |= 1 << (i * 4);
                }
            }
        }
        if (mepModeValue != 0) {
            editor.putInt(MEP_MODE_STATUS, mepModeValue);
            editor.commit();
            mtkLog("saveCurrentMepMode, mepModeKey = " + MEP_MODE_STATUS + ", mepModeValue = " + mepModeValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCurrentEsimSlot() {
        int currSlotValue = getSourceEsimSlotIndex();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editor.putInt(SLOT_CURRENT_ESIM_ID, currSlotValue);
        editor.commit();
        mtkLog("saveSourceEsimSlotIndex, currSlotKey = " + SLOT_CURRENT_ESIM_ID + ", currSlotValue = " + currSlotValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSavedCurrentEsimSlot() {
        int value = PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt(SLOT_CURRENT_ESIM_ID, -1);
        return value;
    }

    private int getSavedMepModeValue(int slotId) {
        int value = PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt(MEP_MODE_STATUS, 0);
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSavedSlotMappingValue() {
        int value = PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt(SLOT_MAPPING_STATUS, 0);
        return value;
    }

    private int getSourceEsimSlotIndex() {
        int value = PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt(SOURCE_SLOT_KEY_STATUS, -1);
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runSlotMapping(Message onComplete, List<UiccSlotMapping> slotMapping) {
        if (slotMapping == null || slotMapping.isEmpty()) {
            return;
        }
        mtkLog("runSlotMapping: " + slotMapping);
        this.mRadioConfig.setSimSlotsMapping(slotMapping, onComplete);
    }

    public class SwitchSlotsHandler extends Handler {
        protected static final int EVENT_INTERNAL_SWITCH_SLOTS_DONE = 1001;
        private Message callbackMessage;
        List<UiccSlotMapping> slotMappingList;

        public SwitchSlotsHandler(List<UiccSlotMapping> slotMapping, Message response) {
            this.slotMappingList = slotMapping;
            this.callbackMessage = response;
        }

        public void startToSwitchSlots() {
            MtkUiccController.this.mRadioConfig.setSimSlotsMapping(this.slotMappingList, obtainMessage(1001));
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    MtkUiccController.this.mtkLog("SwitchSlotsHandler.H.EVENT_INTERNAL_SWITCH_SLOTS_DONE.");
                    AsyncResult ar = (AsyncResult) msg.obj;
                    AsyncResult.forMessage(this.callbackMessage, ar.result, ar.exception);
                    this.callbackMessage.sendToTarget();
                    if (ar.exception == null && MtkUiccController.this.needSaveSlotMappingValue()) {
                        MtkUiccController.this.saveSlotMappingValue(getSlotMappingValueFromParas(this.slotMappingList));
                        MtkUiccController.this.saveCurrentMepMode();
                        MtkUiccController.this.saveCurrentEsimSlot();
                        break;
                    }
                    break;
            }
        }

        private int getSlotMappingValueFromParas(List<UiccSlotMapping> slotMappingList) {
            int currentMappingValue = 0;
            for (int i = 0; i < slotMappingList.size(); i++) {
                UiccSlotMapping slotMapping = slotMappingList.get(i);
                int physicalSlot = slotMapping.getPhysicalSlotIndex();
                int port = slotMapping.getPortIndex();
                int logicalSlot = slotMapping.getLogicalSlotIndex();
                if (physicalSlot != -1 && port != -1 && logicalSlot != -1) {
                    int size = slotMappingList.size();
                    int shiftPort = ((size - logicalSlot) - 1) * 8;
                    int shiftPhysicalSlot = shiftPort + 4;
                    MtkUiccController.this.mtkLog("getSlotMappingValueFromParas: shiftPort = " + shiftPort + ", shiftPhysicalSlot = " + shiftPhysicalSlot + " with index: " + i);
                    currentMappingValue |= ((physicalSlot + 1) << shiftPhysicalSlot) | ((port + 1) << shiftPort);
                }
            }
            MtkUiccController.this.mtkLog("getSlotMappingValueFromParas value = " + currentMappingValue);
            return currentMappingValue;
        }
    }

    public class SwitchSimTypeHandler extends Handler {
        protected static final int EVENT_INTERNAL_SLOT_MAPPING_DONE = 2002;
        protected static final int EVENT_INTERNAL_SWITCH_SIM_TYPE_DONE = 2001;
        protected static final int EVENT_WAITING_SLOT_MAPPING_FINISH = 2003;
        protected static final int SIM_TYPE_MAX_RETRY_COUNT = 6;
        private Message callbackMessage;
        int mode;
        int phoneId;
        int simTypeRetryCount = 0;
        List<UiccSlotMapping> slotMappingList;

        public SwitchSimTypeHandler(int mode, int phoneId, Message onComplete) {
            this.mode = mode;
            this.phoneId = phoneId;
            this.callbackMessage = onComplete;
            this.slotMappingList = getSlotMappingList(mode);
        }

        public void switchSimType() {
            MtkUiccController.this.mCis[this.phoneId].switchSimType(this.mode, obtainMessage(2001));
        }

        public void switchSimTypeAfterSlotChanged() {
            int phoneId = this.phoneId;
            MtkUiccController.this.mtkLog("SwitchSimTypeHandler.switchSimTypeAfterSlotChanged, phoneId = " + phoneId);
            MtkUiccController.this.mCis[1].switchSimType(this.mode, obtainMessage(2001));
        }

        public void switchSlotsOrSimType() {
            MtkUiccController.this.mtkLog("SwitchSimTypeHandler.switchSlotsOrSimType, mode = " + this.mode);
            if (this.mode == 1) {
                switchSimType();
                return;
            }
            if (this.mode == 0) {
                if (this.slotMappingList == null || this.slotMappingList.isEmpty()) {
                    switchSimType();
                } else {
                    MtkUiccController.this.runSlotMapping(obtainMessage(2002), this.slotMappingList);
                }
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2001:
                    MtkUiccController.this.mtkLog("SwitchSimTypeHandler.H.EVENT_INTERNAL_SWITCH_SIM_TYPE_DONE.");
                    MtkUiccController.this.mLastSimType = this.mode;
                    AsyncResult ar = (AsyncResult) msg.obj;
                    AsyncResult.forMessage(this.callbackMessage, ar.result, ar.exception);
                    this.callbackMessage.sendToTarget();
                    if (ar.exception == null) {
                        if (this.mode == 0) {
                            MtkUiccSlot slot = (MtkUiccSlot) MtkUiccController.this.getUiccSlot(1);
                            if (slot != null) {
                                slot.resetPortAndState();
                            }
                            break;
                        } else if (this.mode == 1 && this.slotMappingList != null && !this.slotMappingList.isEmpty()) {
                            MtkUiccController.this.runSlotMapping(obtainMessage(2002), this.slotMappingList);
                            break;
                        }
                    }
                    break;
                case 2002:
                    MtkUiccController.this.mtkLog("SwitchSimTypeHandler.H.EVENT_INTERNAL_SLOT_MAPPING_DONE.");
                    waitingAndSwitchSimType();
                    break;
                case 2003:
                    MtkUiccController.this.mtkLog("SwitchSimTypeHandler.H.EVENT_WAITING_SLOT_MAPPING_FINISH.");
                    waitingAndSwitchSimType();
                    break;
            }
        }

        private void waitingAndSwitchSimType() {
            if (this.mode == 0) {
                if (MtkUiccController.this.getSlotIdFromPhoneId(this.phoneId) == this.phoneId || this.simTypeRetryCount > 6) {
                    switchSimTypeAfterSlotChanged();
                    return;
                }
                Message slotMsg = obtainMessage();
                slotMsg.what = 2003;
                sendMessageDelayed(slotMsg, 400L);
                this.simTypeRetryCount++;
            }
        }

        private List<UiccSlotMapping> getSlotMappingList(int simType) {
            int mappingValue = MtkUiccController.this.getSavedSlotMappingValue();
            int eSlotIdId = MtkUiccController.this.getSavedCurrentEsimSlot();
            MtkUiccController.this.mtkLog("getSlotMappingList value = " + mappingValue);
            if (mappingValue == 0) {
                if (MtkUiccController.this.isInTsTsProject()) {
                    return MtkUiccController.this.getSlotMappingInTsTs(this.phoneId, simType);
                }
                return null;
            }
            List<UiccSlotMapping> uicccSlotMappings = new ArrayList<>();
            int i = 1;
            int size = ((32 - Integer.numberOfLeadingZeros(mappingValue)) / 8) + 1;
            if (simType == 0) {
                if (MtkUiccController.this.isInTsTsProject()) {
                    return MtkUiccController.this.getSlotMappingInTsTs(this.phoneId, simType);
                }
                int physicalSlot0 = (61440 & mappingValue) >> 12;
                int physicalSlot1 = (mappingValue & 240) >> 4;
                int portId1 = mappingValue & 15;
                if (physicalSlot0 >= 2) {
                    uicccSlotMappings.add(new UiccSlotMapping(0, 0, 0));
                    uicccSlotMappings.add(new UiccSlotMapping(portId1 - 1, physicalSlot1 - 1, 1));
                }
            } else if (simType == 1) {
                boolean covertMappingValue = false;
                if (MtkUiccController.this.isInTsTsProject() && eSlotIdId != -1 && eSlotIdId != this.phoneId) {
                    MtkUiccController.this.mtkLog("getSlotMappingList,  eSlotIdId = " + eSlotIdId + ", phoneId = " + this.phoneId);
                    covertMappingValue = true;
                }
                int i2 = 0;
                while (i2 < size) {
                    int shiftPort = ((size - i2) - i) * 8;
                    int shitPhysicalSlot = shiftPort + 4;
                    int physicalSlot = (mappingValue >> shitPhysicalSlot) & 15;
                    int portId = (mappingValue >> shiftPort) & 15;
                    if (covertMappingValue && portId == 0) {
                        if (physicalSlot == i) {
                            physicalSlot = 2;
                        } else if (physicalSlot == 2) {
                            physicalSlot = 1;
                        }
                    }
                    uicccSlotMappings.add(new UiccSlotMapping(portId - 1, physicalSlot - 1, i2));
                    MtkUiccController.this.mtkLog("getSlotMappingList, shitPhysicalSlot = " + shitPhysicalSlot + ", shiftPort = " + shiftPort + "; physicalSlot = " + physicalSlot + ", portId = " + portId);
                    i2++;
                    i = 1;
                }
            }
            return uicccSlotMappings;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInTsTsProject() {
        String simConfig = SystemProperties.get("persist.radio.multisim.config");
        if (simConfig.equals("tsts")) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<UiccSlotMapping> getSlotMappingInTsTs(int phoneId, int mode) {
        List<UiccSlotMapping> uicccSlotMappings = new ArrayList<>();
        int slotId = getSlotIdFromPhoneId(phoneId);
        IccSlotStatus.MultipleEnabledProfilesMode mepMode = getSavedOrSupportedMepMode(slotId);
        if (mode == 0) {
            uicccSlotMappings.add(new UiccSlotMapping(0, 0, 0));
            uicccSlotMappings.add(new UiccSlotMapping(0, 1, 1));
            uicccSlotMappings.add(new UiccSlotMapping(0, 2, 2));
        } else if (mode == 1) {
            if (mepMode.isMepAMode()) {
                uicccSlotMappings.add(new UiccSlotMapping(0, 0, 0));
                uicccSlotMappings.add(new UiccSlotMapping(1, 1, 1));
                uicccSlotMappings.add(new UiccSlotMapping(1, 2, 2));
            } else if (mepMode.isMepMode()) {
                uicccSlotMappings.add(new UiccSlotMapping(0, 0, 0));
                uicccSlotMappings.add(new UiccSlotMapping(0, 1, 1));
                uicccSlotMappings.add(new UiccSlotMapping(0, 2, 2));
            } else {
                mtkLog("getSlotMappingInTsTs, return NULL");
                return null;
            }
        }
        mtkLog("getSlotMappingInTsTs, mode = " + mode);
        return uicccSlotMappings;
    }

    private int resetCorrectSlotId(int slotId, int mode) {
        if (mode == 0) {
            int eSlotId = -1;
            for (int i = 0; i < this.mSimType.length; i++) {
                if (this.mSimType[i] == 1) {
                    eSlotId = i;
                    break;
                }
            }
            mtkLog("resetCorrectSlotId, eSlotId = " + eSlotId);
            if (eSlotId != -1) {
                return eSlotId;
            }
        }
        return slotId;
    }

    private boolean isHalVersion24() {
        if (this.mMajorVersion == 2 && this.mMinorVersion == 4) {
            return true;
        }
        return false;
    }

    private boolean isMockModemBinded() {
        String modem = Build.getRadioVersion();
        if (modem == null) {
            return false;
        }
        boolean isMockModem = modem.contains(MOCK_MODEM_BASEBAND);
        return isMockModem;
    }
}
