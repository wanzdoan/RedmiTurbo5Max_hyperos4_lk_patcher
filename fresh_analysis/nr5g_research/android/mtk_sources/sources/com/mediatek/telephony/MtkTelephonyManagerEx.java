package com.mediatek.telephony;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.IPhoneSubInfo;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephonyRegistry;
import com.mediatek.gwsd.GwsdListener;
import com.mediatek.gwsd.IGwsdService;
import com.mediatek.internal.telephony.FemtoCellInfo;
import com.mediatek.internal.telephony.IMtkPhoneSubInfoEx;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.MtkIccCardConstants;
import com.mediatek.internal.telephony.PreferredOperatorInfo;
import com.mediatek.internal.telephony.PseudoCellInfo;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class MtkTelephonyManagerEx {
    public static final String ACTION_SIM_ME_LOCK_STATE_CHANGED = "com.mediatek.intent.SIM_ME_LOCK_STATE_CHANGE";
    public static final int APP_FAM_3GPP = 1;
    public static final int APP_FAM_3GPP2 = 2;
    public static final int APP_FAM_NONE = 0;
    public static final int CARD_TYPE_CSIM = 4;
    public static final int CARD_TYPE_NONE = 0;
    public static final int CARD_TYPE_RUIM = 8;
    public static final int CARD_TYPE_SIM = 1;
    public static final int CARD_TYPE_USIM = 2;
    public static final int DSDA_STATUS_DIVISOR = 100;
    public static final int FAIL_RETRY = -1;
    public static final int FAIL_RETRY_NO_NEED = -2;
    public static final String INTENT_KEY_LOCK_STATE = "LOCK_STATE";
    public static final String INTENT_KEY_SIM_TYPE = "SIM_TYPE";
    public static final String INTENT_KEY_SIM_TYPE_RESULT = "SIM_TYPE_RESULT";
    public static final String INTENT_KEY_SIM_TYPE_SLOT = "SIM_TYPE_SLOT";
    public static final String INTENT_KEY_SIM_TYPE_SRC_SLOT = "SIM_TYPE_SRC_SLOT";
    private static final String MOCK_MODEM_BASEBAND = "mock-modem-service";
    private static final String PRLVERSION = "vendor.cdma.prl.version";
    public static final String PROPERTY_RADIO_DSDA_STATE = "vendor.radio.dsda.state";
    public static final String PROPERTY_SIM_CARD_ONOFF = "ro.vendor.mtk_sim_card_onoff";
    public static final String PROPERTY_SIM_ONOFF_SUPPORT = "vendor.ril.sim.onoff.support";
    private static final String PROPERTY_SIM_SLOT_LOCK_POLICY = "vendor.gsm.sim.slot.lock.policy";
    private static final String PROPERTY_SIM_SLOT_LOCK_STATE = "vendor.gsm.sim.slot.lock.state";
    private static final String PROPERTY_SML_MODE = "ro.vendor.sim_me_lock_mode";
    public static final int SET_SIM_POWER_ERROR_ALREADY_SIM_OFF = 14;
    public static final int SET_SIM_POWER_ERROR_ALREADY_SIM_ON = 15;
    public static final int SET_SIM_POWER_ERROR_EXECUTING_SIM_OFF = 12;
    public static final int SET_SIM_POWER_ERROR_EXECUTING_SIM_ON = 13;
    public static final int SET_SIM_POWER_ERROR_NOT_ALLOWED = 54;
    public static final int SET_SIM_POWER_ERROR_NOT_SUPPORT = -1;
    public static final int SET_SIM_POWER_ERROR_SIM_ABSENT = 11;
    public static final int SET_SIM_POWER_SUCCESS = 0;
    public static final int SIM_POWER_STATE_EXECUTING_SIM_OFF = 10;
    public static final int SIM_POWER_STATE_EXECUTING_SIM_ON = 11;
    public static final int SIM_POWER_STATE_SIM_OFF = 10;
    public static final int SIM_POWER_STATE_SIM_ON = 11;
    public static final int SIM_SWITCH_MODE_AOSP_SIM_ONOFF = 3;
    public static final int SIM_SWITCH_MODE_MTK_SIM_ONOFF = 2;
    public static final int SIM_SWITCH_MODE_RADIO_ONOFF = 1;
    public static final int SIM_TYPE_ESIM = 1;
    public static final int SIM_TYPE_PSIM = 0;
    public static final int SIM_TYPE_SWITCHING_ESIM = 1;
    public static final int SIM_TYPE_SWITCHING_PSIM = 0;
    public static final int STATE_DSDA_NOT_POSSIBLE = 4;
    public static final int STATE_DSDA_ONGOING = 2;
    public static final int STATE_DSDA_POSSIBLE = 3;
    public static final int SWITCH_SIM_TYPE_ERROR_ALREADY_ESIM = 13;
    public static final int SWITCH_SIM_TYPE_ERROR_ALREADY_PSIM = 14;
    public static final int SWITCH_SIM_TYPE_ERROR_EXECUTING_ESIM = 11;
    public static final int SWITCH_SIM_TYPE_ERROR_EXECUTING_PSIM = 12;
    public static final int SWITCH_SIM_TYPE_ERROR_NOT_ALLOWED = 10;
    public static final int SWITCH_SIM_TYPE_ERROR_NOT_SUPPORT = -1;
    public static final int SWITCH_SIM_TYPE_SUCCESS = 0;
    private static final String TAG = "MtkTelephonyManagerEx";
    private Context mContext;
    private boolean mIsSmlLockMode;
    private ITelephonyRegistry mRegistry;
    private static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    private static final String[] PROPERTY_RIL_CT3G = {"vendor.gsm.ril.ct3g", "vendor.gsm.ril.ct3g.2", "vendor.gsm.ril.ct3g.3", "vendor.gsm.ril.ct3g.4"};
    private static final String[] PROPERTY_RIL_CDMA_CARD_TYPE = {"vendor.ril.cdma.card.type.1", "vendor.ril.cdma.card.type.2", "vendor.ril.cdma.card.type.3", "vendor.ril.cdma.card.type.4"};
    private static final String[] PROPERTY_SIM_SLOT_LOCK_SERVICE_CAPABILITY = {"vendor.gsm.sim.slot.lock.service.capability", "vendor.gsm.sim.slot.lock.service.capability.2", "vendor.gsm.sim.slot.lock.service.capability.3", "vendor.gsm.sim.slot.lock.service.capability.4"};
    private static final String[] PROPERTY_SIM_SLOT_LOCK_CARD_VALID = {"vendor.gsm.sim.slot.lock.card.valid", "vendor.gsm.sim.slot.lock.card.valid.2", "vendor.gsm.sim.slot.lock.card.valid.3", "vendor.gsm.sim.slot.lock.card.valid.4"};
    public static final String[] PROPERTY_SIM_ONOFF_STATE = {"vendor.ril.sim.onoff.state1", "vendor.ril.sim.onoff.state2", "vendor.ril.sim.onoff.state3", "vendor.ril.sim.onoff.state4"};
    private static MtkTelephonyManagerEx sInstance = new MtkTelephonyManagerEx();

    public MtkTelephonyManagerEx(Context context) {
        this.mContext = null;
        this.mIsSmlLockMode = SystemProperties.get(PROPERTY_SML_MODE, "").equals("3");
        this.mContext = context;
        this.mRegistry = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
    }

    private MtkTelephonyManagerEx() {
        this.mContext = null;
        this.mIsSmlLockMode = SystemProperties.get(PROPERTY_SML_MODE, "").equals("3");
        this.mRegistry = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
    }

    public static MtkTelephonyManagerEx getDefault() {
        return sInstance;
    }

    private int getSubIdBySlot(int slot) {
        int[] subId = SubscriptionManager.getSubId(slot);
        Rlog.d(TAG, "getSubIdBySlot, simId " + slot + "subId " + (subId != null ? Integer.valueOf(subId[0]) : "invalid!"));
        return subId != null ? subId[0] : SubscriptionManager.getDefaultSubscriptionId();
    }

    private ITelephony getITelephony() {
        return ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
    }

    private IMtkTelephonyEx getIMtkTelephonyEx() {
        return IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
    }

    private IPhoneSubInfo getSubscriberInfo() {
        return IPhoneSubInfo.Stub.asInterface(ServiceManager.getService("iphonesubinfo"));
    }

    public int getDsdaState() {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, "getDsdaState : telephony is null");
                return -1;
            }
            return telephonyEx.getDsdaState();
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getDsdaState : RemoteException " + ex);
            return -1;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "getDsdaState : NullPointerException " + ex2);
            return -1;
        }
    }

    public boolean isInDsdaMode() {
        int value = getDsdaState();
        if (value <= 0) {
            return false;
        }
        if (value == 1) {
            return true;
        }
        int mode = value;
        if (value > 100) {
            mode = value / 100;
        }
        if (mode != 2 && mode != 3) {
            return false;
        }
        return true;
    }

    public boolean isOutgoingCallOnSecondarySimAllowed() {
        int value = getDsdaState();
        if (value <= 100) {
            return false;
        }
        int allowed = value / 100;
        return allowed == 2 || allowed == 3;
    }

    public boolean isInHomeNetwork(int subId) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                return false;
            }
            return telephonyEx.isInHomeNetwork(subId);
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    private IMtkPhoneSubInfoEx getMtkSubscriberInfoEx() {
        return IMtkPhoneSubInfoEx.Stub.asInterface(ServiceManager.getService("iphonesubinfoEx"));
    }

    public boolean getUsimService(int service) {
        return getUsimService(SubscriptionManager.getDefaultSubscriptionId(), service);
    }

    public boolean getUsimService(int subId, int service) {
        try {
            return getMtkSubscriberInfoEx().getUsimServiceForSubscriber(subId, service, getOpPackageName());
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public int getIccAppFamily(int slotId) {
        try {
            return getIMtkTelephonyEx().getIccAppFamily(slotId);
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public String getIccCardType(int subId) throws RemoteException {
        String type = null;
        try {
            type = getIMtkTelephonyEx().getIccCardType(subId);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex2) {
            ex2.printStackTrace();
        }
        Rlog.d(TAG, "getIccCardType sub " + subId + " ,icc type " + (type != null ? type : "null"));
        return type;
    }

    private String getOpPackageName() {
        if (this.mContext != null) {
            return this.mContext.getOpPackageName();
        }
        return ActivityThread.currentOpPackageName();
    }

    public byte[] loadEFTransparent(int slotId, int family, int fileID, String filePath) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony != null) {
                return telephony.loadEFTransparent(slotId, family, fileID, filePath);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public List<String> loadEFLinearFixedAll(int slotId, int family, int fileID, String filePath) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony != null) {
                return telephony.loadEFLinearFixedAll(slotId, family, fileID, filePath);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getUimSubscriberId(int subId) {
        try {
            String uimImsi = getIMtkTelephonyEx().getUimSubscriberId(getOpPackageName(), subId);
            return uimImsi;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return "";
        } catch (NullPointerException ex2) {
            ex2.printStackTrace();
            return "";
        }
    }

    public String[] getSupportCardType(int slotId) {
        String[] values = null;
        if (slotId < 0 || slotId >= PROPERTY_RIL_FULL_UICC_TYPE.length) {
            Rlog.e(TAG, "getSupportCardType: invalid slotId " + slotId);
            return null;
        }
        String prop = SystemProperties.get(PROPERTY_RIL_FULL_UICC_TYPE[slotId], "");
        if (!prop.equals("") && prop.length() > 0) {
            values = prop.split(",");
        }
        if (values != null && values.length > 0) {
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(values));
            arrayList.remove("RUIM");
            arrayList.remove("CSIM");
            String[] newValue = (String[]) arrayList.toArray(new String[0]);
            Rlog.d(TAG, "getSupportCardType slotId " + slotId + ", prop value= " + prop + ", size= " + (newValue != null ? newValue.length : 0));
            return newValue;
        }
        Rlog.d(TAG, "getSupportCardType slotId " + slotId + ", prop value= " + prop + ", size= " + (values != null ? values.length : 0));
        return values;
    }

    public boolean isCt3gDualMode(int slotId) {
        return false;
    }

    public MtkIccCardConstants.CardType getCdmaCardType(int slotId) {
        if (slotId < 0 || slotId >= PROPERTY_RIL_CT3G.length) {
            Rlog.e(TAG, "getCdmaCardType: invalid slotId " + slotId);
            return null;
        }
        MtkIccCardConstants.CardType mCdmaCardType = MtkIccCardConstants.CardType.UNKNOW_CARD;
        String result = SystemProperties.get(PROPERTY_RIL_CDMA_CARD_TYPE[slotId], "");
        if (!result.equals("")) {
            int cardtype = Integer.parseInt(result);
            mCdmaCardType = MtkIccCardConstants.CardType.getCardTypeFromInt(cardtype);
        }
        Rlog.d(TAG, "getCdmaCardType slotId: " + slotId + " result: " + result + "  mCdmaCardType: " + mCdmaCardType);
        return mCdmaCardType;
    }

    public String[] getSimOperatorNumericForPhoneEx(int phoneId) throws RemoteException {
        if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "getSimOperatorNumericForPhoneEx with invalid phoneId:" + phoneId);
            return null;
        }
        String[] values = null;
        try {
            values = getIMtkTelephonyEx().getSimOperatorNumericForPhoneEx(phoneId);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex2) {
            ex2.printStackTrace();
        }
        Rlog.d(TAG, "getSimOperatorNumericForPhoneEx phoneId " + phoneId + " values = " + (values != null ? values[0] + ", " + values[1] : "null"));
        return values;
    }

    public String getIsimImpi(int subId) {
        try {
            return getMtkSubscriberInfoEx().getIsimImpiForSubscriber(subId);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getIsimDomain(int subId) {
        try {
            return getMtkSubscriberInfoEx().getIsimDomainForSubscriber(subId);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String[] getIsimImpu(int subId) {
        try {
            return getMtkSubscriberInfoEx().getIsimImpuForSubscriber(subId);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getIsimIst(int subId) {
        try {
            return getMtkSubscriberInfoEx().getIsimIstForSubscriber(subId);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String[] getIsimPcscf(int subId) {
        try {
            return getMtkSubscriberInfoEx().getIsimPcscfForSubscriber(subId);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public boolean isImsRegistered(int subId) {
        try {
            return getIMtkTelephonyEx().isImsRegistered(subId);
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean isVolteEnabled(int subId) {
        try {
            return getIMtkTelephonyEx().isVolteEnabled(subId);
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean isWifiCallingEnabled(int subId) {
        try {
            return getIMtkTelephonyEx().isWifiCallingEnabled(subId);
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public String getPrlVersion(int subId) {
        int slotId = SubscriptionManager.getSlotIndex(subId);
        String prlVersion = SystemProperties.get(PRLVERSION + slotId, "");
        Rlog.d(TAG, "getPrlversion PRLVERSION subId = " + subId + " key = " + PRLVERSION + slotId + " value = " + prlVersion);
        return prlVersion;
    }

    public boolean exitEmergencyCallbackMode(int subId) {
        try {
            return getIMtkTelephonyEx().exitEmergencyCallbackMode(subId);
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public void setApcMode(int slotId, int mode, boolean reportOn, int reportInterval) {
        if (slotId < 0 || slotId >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "setApcMode error with invalid slotId " + slotId);
            return;
        }
        if (mode < 0 || mode > 2) {
            Rlog.e(TAG, "setApcMode error with invalid mode " + mode);
            return;
        }
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Rlog.e(TAG, "setApcMode error because telephony is null");
            } else {
                telephony.setApcModeUsingSlotId(slotId, mode, reportOn, reportInterval);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setApcMode error due to RemoteException " + ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setApcMode error due to NullPointerException " + ex2);
        }
    }

    public PseudoCellInfo getApcInfo(int slotId) {
        if (slotId < 0 || slotId >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "getApcInfo with invalid slotId " + slotId);
            return null;
        }
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Rlog.e(TAG, "getApcInfo return null because telephony is null");
                return null;
            }
            return telephony.getApcInfoUsingSlotId(slotId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getApcInfo returning null due to RemoteException " + ex);
            return null;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "getApcInfo returning null due to NullPointerException " + ex2);
            return null;
        }
    }

    public int getCdmaSubscriptionActStatus(int subId) {
        try {
            int actStatus = getIMtkTelephonyEx().getCdmaSubscriptionActStatus(subId);
            return actStatus;
        } catch (RemoteException e) {
            Rlog.d(TAG, "fail to getCdmaSubscriptionActStatus due to RemoteException");
            return 0;
        } catch (NullPointerException e2) {
            Rlog.d(TAG, "fail to getCdmaSubscriptionActStatus due to NullPointerException");
            return 0;
        }
    }

    public static class OemHookCallback {
        public void onAtCmdResp(int slotId, long token, String atCmd) {
            Rlog.d(MtkTelephonyManagerEx.TAG, "OemHookCallback Not override onAtCmdResp");
        }

        public void onAtUrcInd(int slotId, String atCmd) {
            Rlog.d(MtkTelephonyManagerEx.TAG, "OemHookCallback Not override onAtUrcInd");
        }

        public void onError(String e) {
            Rlog.d(MtkTelephonyManagerEx.TAG, "OemHookCallback onError:" + e);
        }
    }

    public void registerAtUrcInd(int slotId, final Handler h, final int event_id) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Rlog.e(TAG, "registerAtUrcInd service is null");
            } else {
                telephony.registerAtUrcInd(slotId, new IOemHookCallback.Stub(this) { // from class: com.mediatek.telephony.MtkTelephonyManagerEx.1
                    @Override // com.mediatek.telephony.IOemHookCallback
                    public void onAtCmdResp(int slotId2, long token, String atCmd) {
                    }

                    @Override // com.mediatek.telephony.IOemHookCallback
                    public void onAtUrcInd(int slotId2, String atCmd) {
                        if (h != null) {
                            ArrayList<String> ret = new ArrayList<>();
                            ret.add(String.valueOf(slotId2));
                            ret.add(atCmd);
                            Message m = h.obtainMessage(event_id, ret);
                            h.sendMessage(m);
                        }
                    }

                    @Override // com.mediatek.telephony.IOemHookCallback
                    public void onError(String e) {
                        Rlog.e(MtkTelephonyManagerEx.TAG, "registerAtUrcInd onError " + e);
                        if (h != null) {
                            Exception except = new Exception(e);
                            Message m = h.obtainMessage(event_id, except);
                            h.sendMessage(m);
                            return;
                        }
                        Rlog.e(MtkTelephonyManagerEx.TAG, "registerAtUrcInd onError h == null");
                    }
                });
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "exception " + ex);
        }
    }

    public void registerAtUrcInd(int slotId, Executor executor, OemHookCallback cb) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Rlog.e(TAG, "registerAtUrcInd service is null");
            } else {
                telephony.registerAtUrcInd(slotId, new AnonymousClass2(this, executor, cb));
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "exception " + ex);
        }
    }

    /* JADX INFO: renamed from: com.mediatek.telephony.MtkTelephonyManagerEx$2, reason: invalid class name */
    class AnonymousClass2 extends IOemHookCallback.Stub {
        final /* synthetic */ OemHookCallback val$cb;
        final /* synthetic */ Executor val$executor;

        AnonymousClass2(MtkTelephonyManagerEx this$0, Executor executor, OemHookCallback oemHookCallback) {
            this.val$executor = executor;
            this.val$cb = oemHookCallback;
        }

        @Override // com.mediatek.telephony.IOemHookCallback
        public void onAtCmdResp(int slotId, long token, String atCmd) {
        }

        @Override // com.mediatek.telephony.IOemHookCallback
        public void onAtUrcInd(final int slotId, final String atCmd) {
            Executor executor = this.val$executor;
            final OemHookCallback oemHookCallback = this.val$cb;
            executor.execute(new Runnable() { // from class: com.mediatek.telephony.MtkTelephonyManagerEx$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    oemHookCallback.onAtUrcInd(slotId, atCmd);
                }
            });
        }

        @Override // com.mediatek.telephony.IOemHookCallback
        public void onError(final String e) {
            Executor executor = this.val$executor;
            final OemHookCallback oemHookCallback = this.val$cb;
            executor.execute(new Runnable() { // from class: com.mediatek.telephony.MtkTelephonyManagerEx$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    oemHookCallback.onError(e);
                }
            });
        }
    }

    public void sendAtCmd(int slotId, long token, String atCmd, final Handler h, final int event_id) {
        RemoteException ex;
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            try {
                if (telephony == null) {
                    Rlog.e(TAG, "sendAtCmd service is null");
                } else {
                    telephony.sendAtCmd(slotId, token, atCmd, new IOemHookCallback.Stub(this) { // from class: com.mediatek.telephony.MtkTelephonyManagerEx.3
                        @Override // com.mediatek.telephony.IOemHookCallback
                        public void onAtCmdResp(int slotId2, long token2, String atCmd2) {
                            Rlog.e(MtkTelephonyManagerEx.TAG, "sendAtCmd onAtCmdResp token=" + token2);
                            if (h != null) {
                                ArrayList<String> ret = new ArrayList<>();
                                ret.add(String.valueOf(slotId2));
                                ret.add(String.valueOf(token2));
                                ret.add(atCmd2);
                                Message m = h.obtainMessage(event_id, ret);
                                h.sendMessage(m);
                                return;
                            }
                            Rlog.e(MtkTelephonyManagerEx.TAG, "sendAtCmd onAtCmdResp h == null");
                        }

                        @Override // com.mediatek.telephony.IOemHookCallback
                        public void onAtUrcInd(int slotId2, String atCmd2) {
                        }

                        @Override // com.mediatek.telephony.IOemHookCallback
                        public void onError(String e) {
                            Rlog.e(MtkTelephonyManagerEx.TAG, "sendAtCmd onError " + e);
                            if (h != null) {
                                Exception except = new Exception(e);
                                Message m = h.obtainMessage(event_id, except);
                                h.sendMessage(m);
                                return;
                            }
                            Rlog.e(MtkTelephonyManagerEx.TAG, "sendAtCmd onError h == null");
                        }
                    });
                }
            } catch (RemoteException e) {
                ex = e;
                Rlog.e(TAG, "exception " + ex);
            }
        } catch (RemoteException e2) {
            ex = e2;
        }
    }

    public void sendAtCmd(int slotId, long token, String atCmd, Executor executor, OemHookCallback cb) {
        RemoteException ex;
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            try {
                if (telephony == null) {
                    Rlog.e(TAG, "sendAtCmd service is null");
                } else {
                    telephony.sendAtCmd(slotId, token, atCmd, new AnonymousClass4(this, executor, cb));
                }
            } catch (RemoteException e) {
                ex = e;
                Rlog.e(TAG, "exception " + ex);
            }
        } catch (RemoteException e2) {
            ex = e2;
        }
    }

    /* JADX INFO: renamed from: com.mediatek.telephony.MtkTelephonyManagerEx$4, reason: invalid class name */
    class AnonymousClass4 extends IOemHookCallback.Stub {
        final /* synthetic */ OemHookCallback val$cb;
        final /* synthetic */ Executor val$executor;

        AnonymousClass4(MtkTelephonyManagerEx this$0, Executor executor, OemHookCallback oemHookCallback) {
            this.val$executor = executor;
            this.val$cb = oemHookCallback;
        }

        @Override // com.mediatek.telephony.IOemHookCallback
        public void onAtCmdResp(final int slotId, final long token, final String atCmd) {
            Executor executor = this.val$executor;
            final OemHookCallback oemHookCallback = this.val$cb;
            executor.execute(new Runnable() { // from class: com.mediatek.telephony.MtkTelephonyManagerEx$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    oemHookCallback.onAtCmdResp(slotId, token, atCmd);
                }
            });
        }

        @Override // com.mediatek.telephony.IOemHookCallback
        public void onAtUrcInd(int slotId, String atCmd) {
        }

        @Override // com.mediatek.telephony.IOemHookCallback
        public void onError(final String e) {
            Executor executor = this.val$executor;
            final OemHookCallback oemHookCallback = this.val$cb;
            executor.execute(new Runnable() { // from class: com.mediatek.telephony.MtkTelephonyManagerEx$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    oemHookCallback.onError(e);
                }
            });
        }
    }

    public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony != null) {
                return telephony.invokeOemRilRequestRaw(oemReq, oemResp);
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    public int invokeOemRilRequestRawBySlot(int slotId, byte[] oemReq, byte[] oemResp) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony != null) {
                return telephony.invokeOemRilRequestRawBySlot(slotId, oemReq, oemResp);
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    public boolean isInCsCall(int phoneId) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Rlog.e(TAG, "[isInCsCall] telephony = null");
                return false;
            }
            return telephony.isInCsCall(phoneId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "[isInCsCall] RemoteException " + ex);
            return false;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "[isInCsCall] NullPointerException " + ex2);
            return false;
        }
    }

    public int getSimCardState(int slotId) {
        int simCardState = TelephonyManager.getSimStateForSlotIndex(slotId);
        switch (simCardState) {
            case 0:
            case 1:
            case 8:
            case 9:
                return simCardState;
            default:
                return 11;
        }
    }

    public int getSimApplicationState(int slotId) {
        int simApplicationState = TelephonyManager.getSimStateForSlotIndex(slotId);
        switch (simApplicationState) {
            case 0:
            case 1:
            case 8:
            case 9:
                return 0;
            case 5:
                return 6;
            default:
                return simApplicationState;
        }
    }

    public String getLocatedPlmn(int phoneId) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                return null;
            }
            String plmn = telephonyEx.getLocatedPlmn(phoneId);
            return plmn;
        } catch (RemoteException e) {
            Rlog.e(TAG, "fail to getLocatedPlmn due to RemoteException");
            return null;
        } catch (NullPointerException e2) {
            Rlog.e(TAG, "fail to getLocatedPlmn due to NullPointerException");
            return null;
        }
    }

    public boolean setDisable2G(int phoneId, boolean mode) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, "setDisable2G error because telephony is null");
                return false;
            }
            return telephonyEx.setDisable2G(phoneId, mode);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setDisable2G error due to RemoteException " + ex);
            return false;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setDisable2G error due to NullPointerException " + ex2);
            return false;
        }
    }

    public int getDisable2G(int phoneId) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, "getDisable2G error because telephony is null");
                return -1;
            }
            return telephonyEx.getDisable2G(phoneId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getDisable2G error due to RemoteException " + ex);
            return -1;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "getDisable2G error due to NullPointerException " + ex2);
            return -1;
        }
    }

    public List<FemtoCellInfo> getFemtoCellList(int phoneId) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, "getFemtoCellList error because telephony is null");
                return null;
            }
            return telephonyEx.getFemtoCellList(phoneId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getFemtoCellList error due to RemoteException " + ex);
            return null;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "getFemtoCellList error due to NullPointerException " + ex2);
            return null;
        }
    }

    public boolean abortFemtoCellList(int phoneId) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, "abortFemtoCellList error because telephony is null");
                return false;
            }
            return telephonyEx.abortFemtoCellList(phoneId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "abortFemtoCellList error due to RemoteException " + ex);
            return false;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "abortFemtoCellList error due to NullPointerException " + ex2);
            return false;
        }
    }

    public boolean selectFemtoCell(int phoneId, FemtoCellInfo femtocell) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, "selectFemtoCell error because telephony is null");
                return false;
            }
            return telephonyEx.selectFemtoCell(phoneId, femtocell);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "selectFemtoCell error due to RemoteException " + ex);
            return false;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "selectFemtoCell error due to NullPointerException " + ex2);
            return false;
        }
    }

    public int queryFemtoCellSystemSelectionMode(int phoneId) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, "queryFemtoCellSystemSelectionMode error because telephony is null");
                return -1;
            }
            return telephonyEx.queryFemtoCellSystemSelectionMode(phoneId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "queryFemtoCellSystemSelectionMode error due to RemoteException " + ex);
            return -1;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "queryFemtoCellSystemSelectionMode due to NullPointerException " + ex2);
            return -1;
        }
    }

    public boolean setFemtoCellSystemSelectionMode(int phoneId, int mode) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, "setFemtoCellSystemSelectionMode error because telephony is null");
                return false;
            }
            return telephonyEx.setFemtoCellSystemSelectionMode(phoneId, mode);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setFemtoCellSystemSelectionMode error due to RemoteException " + ex);
            return false;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setFemtoCellSystemSelectionMode due to NullPointerException " + ex2);
            return false;
        }
    }

    public boolean cancelAvailableNetworks(int phoneId) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                Rlog.e(TAG, " cancelAvailableNetworks error because telephony is null");
                return false;
            }
            return telephonyEx.cancelAvailableNetworks(phoneId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, " cancelAvailableNetworks error due to RemoteException " + ex);
            return false;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, " cancelAvailableNetworks error due to NullPointerException " + ex2);
            return false;
        }
    }

    public boolean isDssNoResetSupport() {
        if (SystemProperties.get("vendor.ril.simswitch.no_reset_support").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            Rlog.d(TAG, "return true for isDssNoResetSupport");
            return true;
        }
        Rlog.d(TAG, "return false for isDssNoResetSupport");
        return false;
    }

    public int getProtocolStackId(int slot) throws RemoteException {
        int majorSlot = 0;
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx != null) {
                majorSlot = telephonyEx.getMainCapabilityPhoneId();
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "fail to getMainCapabilityPhoneId due to RemoteException");
        } catch (NullPointerException e2) {
            Rlog.e(TAG, "fail to getMainCapabilityPhoneId due to NullPointerException");
        }
        if (slot == majorSlot) {
            return 1;
        }
        if (isDssNoResetSupport()) {
            if (slot < majorSlot) {
                return slot + 2;
            }
        } else if (slot == 0) {
            return majorSlot + 1;
        }
        return slot + 1;
    }

    public int getSimLockPolicy() {
        if (this.mIsSmlLockMode && !isMockModemBinded()) {
            int policy = SystemProperties.getInt(PROPERTY_SIM_SLOT_LOCK_POLICY, -1);
            Rlog.d(TAG, "getSimLockPolicy: " + policy);
            return policy;
        }
        return 0;
    }

    public int getShouldServiceCapability(int slotId) {
        if (!this.mIsSmlLockMode || isMockModemBinded()) {
            return 0;
        }
        if (slotId < 0 || slotId >= PROPERTY_SIM_SLOT_LOCK_SERVICE_CAPABILITY.length) {
            Rlog.e(TAG, "getShouldServiceCapability: invalid slotId: " + slotId);
            return 4;
        }
        int capability = SystemProperties.getInt(PROPERTY_SIM_SLOT_LOCK_SERVICE_CAPABILITY[slotId], -1);
        Rlog.d(TAG, "getShouldServiceCapability: " + capability + ",slotId: " + slotId);
        return capability;
    }

    public int checkValidCard(int slotId) {
        if (!this.mIsSmlLockMode || isMockModemBinded()) {
            return 0;
        }
        if (slotId < 0 || slotId >= PROPERTY_SIM_SLOT_LOCK_CARD_VALID.length) {
            Rlog.e(TAG, "checkValidCard: invalid slotId " + slotId);
            return 2;
        }
        int validCard = SystemProperties.getInt(PROPERTY_SIM_SLOT_LOCK_CARD_VALID[slotId], -1);
        Rlog.d(TAG, "checkValidCard: " + validCard + ",slotId: " + slotId);
        return validCard;
    }

    public int getSimLockState() {
        if (this.mIsSmlLockMode && !isMockModemBinded()) {
            int lockState = SystemProperties.getInt(PROPERTY_SIM_SLOT_LOCK_STATE, -1);
            Rlog.d(TAG, "getSimLockState: " + lockState);
            return lockState;
        }
        return 1;
    }

    private boolean isMockModemBinded() {
        String modem = Build.getRadioVersion();
        if (modem == null) {
            return false;
        }
        boolean isMockModem = modem.contains(MOCK_MODEM_BASEBAND);
        return isMockModem;
    }

    public int getSimLockStateForRSU(int phoneId) {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                return -1;
            }
            return telephonyEx.getSimLockStateForRSU(phoneId);
        } catch (RemoteException e) {
            return -2;
        } catch (NullPointerException e2) {
            return -2;
        }
    }

    public String getLine1PhoneNumber(int subId) {
        String number = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                number = telephony.getLine1NumberForDisplay(subId, this.mContext.getOpPackageName(), this.mContext.getFeatureId());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
        if (number != null) {
            return number;
        }
        try {
            IMtkPhoneSubInfoEx info = getMtkSubscriberInfoEx();
            if (info == null) {
                return null;
            }
            return info.getLine1PhoneNumberForSubscriber(subId, getOpPackageName(), this.mContext.getFeatureId());
        } catch (RemoteException e3) {
            return null;
        } catch (NullPointerException e4) {
            return null;
        }
    }

    public int simSwitchMode() {
        int result;
        if (SystemProperties.get(PROPERTY_SIM_CARD_ONOFF).equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN)) {
            result = 2;
        } else if (SystemProperties.get(PROPERTY_SIM_CARD_ONOFF).equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            result = 1;
        } else {
            result = 3;
        }
        Rlog.d(TAG, "simSwitchMode result = " + result);
        return result;
    }

    public boolean supportSimTypeSwitch() throws RemoteException {
        boolean result = false;
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx != null) {
                result = telephonyEx.supportSimTypeSwitch();
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#supportSimTypeSwitch", ex);
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#supportSimTypeSwitch", ex2);
        }
        Rlog.d(TAG, "supportSimTypeSwitch result = " + result);
        return result;
    }

    public boolean canSwitchSimType(int slotId) throws RemoteException {
        boolean result = false;
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx != null) {
                result = telephonyEx.canSwitchSimType(slotId);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#canSwitchSimType", ex);
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#canSwitchSimType", ex2);
        }
        Rlog.d(TAG, "canSwitchSimType result = " + result);
        return result;
    }

    public int getCurrentSimType(int slotId) throws RemoteException {
        int result = -1;
        if (slotId < 0 || slotId >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "getCurrentSimType error with invalid slotId " + slotId);
            return -1;
        }
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx != null) {
                result = telephonyEx.getCurrentSimType(slotId);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#getCurrentSimType", ex);
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#getCurrentSimType", ex2);
        }
        Rlog.d(TAG, "getCurrentSimType slotId = " + slotId + " result = " + result);
        return result;
    }

    public int switchSimType(int slotIndex, int mode) {
        if (slotIndex < 0 || slotIndex >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "setSimPower error with invalid slotIndex " + slotIndex);
            return -1;
        }
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                return -1;
            }
            int result = telephonyEx.switchSimType(slotIndex, mode);
            return result;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#switchSimType", ex);
            return -1;
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#switchSimType", ex2);
            return -1;
        }
    }

    public int getSimTypeExecutingStatus(int slotId) {
        if (slotId < 0 || slotId >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "getSimTypeExecutingStatus error with invalid slotId " + slotId);
            return -1;
        }
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                return -1;
            }
            int result = telephonyEx.getSimTypeExecutingStatus(slotId);
            return result;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#getSimTypeExecutingStatus", ex);
            return -1;
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#getSimTypeExecutingStatus", ex2);
            return -1;
        }
    }

    public int getSimTypeExecutingStatusAll() {
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                return -1;
            }
            int result = telephonyEx.getSimTypeExecutingStatusAll();
            return result;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#getSimTypeExecutingStatus", ex);
            return -1;
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#getSimTypeExecutingStatus", ex2);
            return -1;
        }
    }

    public boolean isSimOnOffEnabled() {
        boolean result = SystemProperties.get(PROPERTY_SIM_CARD_ONOFF).equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN);
        Rlog.d(TAG, "isSimOnOffEnabled result = " + result);
        return result;
    }

    public int getSimOnOffState(int slotId) {
        int result = -1;
        if (slotId < 0 || slotId >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "getSimOnOffState error with invalid slotId " + slotId);
            return -1;
        }
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx != null) {
                result = telephonyEx.getSimOnOffState(slotId);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#getSimOnOffState", ex);
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#getSimOnOffState", ex2);
        }
        Rlog.d(TAG, "getSimOnOffState slotId = " + slotId + " result = " + result);
        return result;
    }

    public int setSimPower(int slotIndex, int state) {
        if (slotIndex < 0 || slotIndex >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "setSimPower error with invalid slotIndex " + slotIndex);
            return -1;
        }
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                return -1;
            }
            int result = telephonyEx.setSimPower(slotIndex, state);
            return result;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#setSimOnOffState", ex);
            return -1;
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#setSimOnOffState", ex2);
            return -1;
        }
    }

    public int getSimOnOffExecutingState(int slotId) {
        if (slotId < 0 || slotId >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "getSimOnOffExecutingState error with invalid slotId " + slotId);
            return -1;
        }
        try {
            IMtkTelephonyEx telephonyEx = getIMtkTelephonyEx();
            if (telephonyEx == null) {
                return -1;
            }
            int result = telephonyEx.getSimOnOffExecutingState(slotId);
            return result;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error calling ITelephony#getSimOnOffExecutingState", ex);
            return -1;
        } catch (SecurityException ex2) {
            Rlog.e(TAG, "Permission error calling ITelephony#getSimOnOffExecutingState", ex2);
            return -1;
        }
    }

    public void addGwsdListener(GwsdListener listener) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        addGwsdListener(defDataPhoneId, listener);
    }

    public void addGwsdListener(int phoneId, GwsdListener listener) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.addListener(phoneId, listener.callback);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void removeGwsdListener() {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        removeGwsdListener(defDataPhoneId);
    }

    public void removeGwsdListener(int phoneId) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.removeListener(phoneId);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setGwsdEnabled(boolean action) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        setGwsdEnabled(defDataPhoneId, action);
    }

    public void setGwsdEnabled(int phoneId, boolean action) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setUserModeEnabled(phoneId, action);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setGwsdAutoRejectEnabled(boolean action) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        setGwsdAutoRejectEnabled(defDataPhoneId, action);
    }

    public void setGwsdAutoRejectEnabled(int phoneId, boolean action) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setAutoRejectModeEnabled(phoneId, action);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void syncGwsdInfo(boolean userEnable, boolean autoReject) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        syncGwsdInfo(defDataPhoneId, userEnable, autoReject);
    }

    public void syncGwsdInfo(int phoneId, boolean userEnable, boolean autoReject) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.syncGwsdInfo(phoneId, userEnable, autoReject);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setCallValidTimer(int timer) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        setCallValidTimer(defDataPhoneId, timer);
    }

    public void setCallValidTimer(int phoneId, int timer) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setCallValidTimer(phoneId, timer);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setIgnoreSameNumberInterval(int internal) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        setIgnoreSameNumberInterval(defDataPhoneId, internal);
    }

    public void setIgnoreSameNumberInterval(int phoneId, int internal) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setIgnoreSameNumberInterval(phoneId, internal);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public ServiceState getServiceStateByPhoneId(int phoneId) {
        try {
            String attributionTag = this.mContext == null ? null : this.mContext.getAttributionTag();
            return getIMtkTelephonyEx().getServiceStateByPhoneId(phoneId, getOpPackageName(), attributionTag);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public boolean setRoamingEnable(int phoneId, int[] config) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Rlog.e(TAG, "setRoamingEnable error because telephony is null");
                return false;
            }
            return telephony.setRoamingEnable(phoneId, config);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setRoamingEnable error due to RemoteException " + ex);
            return false;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "setRoamingEnable error due to NullPointerException " + ex2);
            return false;
        }
    }

    public int[] getRoamingEnable(int phoneId) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Rlog.e(TAG, "getRoamingEnable error because telephony is null");
                return null;
            }
            return telephony.getRoamingEnable(phoneId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getRoamingEnable error due to RemoteException " + ex);
            return null;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "getRoamingEnable error due to NullPointerException " + ex2);
            return null;
        }
    }

    public String[] getSuggestedPlmnList(int phoneId, int rat, int num, int timer) throws RemoteException {
        int phoneId2;
        NullPointerException ex;
        RemoteException ex2;
        if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getSimCount()) {
            Rlog.e(TAG, "getSuggestedPlmnList with invalid phoneId:" + phoneId);
            return null;
        }
        if (rat != 0 && rat != 1 && rat != 3) {
            Rlog.e(TAG, "getSuggestedPlmnList with invalid rat:" + rat);
            return null;
        }
        if (num <= 0) {
            Rlog.e(TAG, "getSuggestedPlmnList with invalid num:" + num);
            return null;
        }
        if (timer <= 0) {
            Rlog.e(TAG, "getSuggestedPlmnList with invalid timer:" + timer);
            return null;
        }
        String[] values = null;
        try {
            phoneId2 = phoneId;
            try {
                values = getIMtkTelephonyEx().getSuggestedPlmnList(phoneId2, rat, num, timer, getOpPackageName());
            } catch (RemoteException e) {
                ex2 = e;
                ex2.printStackTrace();
            } catch (NullPointerException e2) {
                ex = e2;
                ex.printStackTrace();
            }
        } catch (RemoteException e3) {
            phoneId2 = phoneId;
            ex2 = e3;
        } catch (NullPointerException e4) {
            phoneId2 = phoneId;
            ex = e4;
        }
        Rlog.d(TAG, "getSuggestedPlmnList phoneId " + phoneId2 + " values = " + (values != null ? Arrays.toString(values) : "null"));
        return values;
    }

    public boolean isEmergencyNumber(int phoneId, String number) {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Log.e(TAG, "isEmergencyNumber IMtkTelephonyEx is null");
                return false;
            }
            return telephony.isEmergencyNumber(phoneId, number);
        } catch (RemoteException ex) {
            Log.e(TAG, "isEmergencyNumber RemoteException", ex);
            return false;
        }
    }

    public void setGwsdDualSimEnabled(boolean action) {
        Rlog.d(TAG, "setGwsdDualSimEnabled: " + action);
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setGwsdDualSimEnabled(action);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public boolean isDataAvailableForGwsdDualSim(boolean gwsdDualSimStatus) {
        Rlog.d(TAG, "isDataAvailableForGwsdDualSim");
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                return iGwsdService.isDataAvailableForGwsdDualSim(gwsdDualSimStatus);
            }
            return false;
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    public boolean getCallSubAddressEnabled() {
        int defaultPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultSubscriptionId());
        return getCallSubAddressEnabled(defaultPhoneId);
    }

    public void setCallSubAddressEnabled(boolean mode) {
        int defaultPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultSubscriptionId());
        setCallSubAddressEnabled(defaultPhoneId, mode);
    }

    public boolean getCallSubAddressEnabled(int phoneId) {
        try {
            return getIMtkTelephonyEx().getCallSubAddressEnabled(phoneId);
        } catch (RemoteException ex) {
            Log.e(TAG, "getCallSubAddressEnabled IMtkTelephonyEx re: " + ex.getMessage());
            return false;
        } catch (NullPointerException ex2) {
            Log.e(TAG, "getCallSubAddressEnabled IMtkTelephonyEx npe: " + ex2.getMessage());
            return false;
        }
    }

    public void setCallSubAddressEnabled(int phoneId, boolean mode) {
        try {
            getIMtkTelephonyEx().setCallSubAddressEnabled(phoneId, mode);
        } catch (RemoteException ex) {
            Log.e(TAG, "setCallSubAddressEnabled IMtkTelephonyEx re: " + ex.getMessage());
        } catch (NullPointerException ex2) {
            Log.e(TAG, "setCallSubAddressEnabled IMtkTelephonyEx npe: " + ex2.getMessage());
        }
    }

    public boolean isInEmergencyCall() {
        try {
            IMtkTelephonyEx telephony = getIMtkTelephonyEx();
            if (telephony == null) {
                Log.e(TAG, "isInEmergencyCall IMtkTelephonyEx is null");
                return false;
            }
            return telephony.isInEmergencyCall();
        } catch (RemoteException ex) {
            Log.e(TAG, "isInEmergencyCall RemoteException: " + ex);
            return false;
        }
    }

    public List<PreferredOperatorInfo> getPreferredOperatorList(int phoneId) {
        try {
            List<PreferredOperatorInfo> list = getIMtkTelephonyEx().getPreferredOperatorList(phoneId);
            return list;
        } catch (RemoteException ex) {
            Log.e(TAG, "getPreferredOperatorList IMtkTelephonyEx re: " + ex.getMessage());
            return null;
        } catch (NullPointerException ex2) {
            Log.e(TAG, "getPreferredOperatorList IMtkTelephonyEx npe: " + ex2.getMessage());
            return null;
        }
    }

    public boolean setPreferredOperator(int phoneId, PreferredOperatorInfo info) {
        try {
            return getIMtkTelephonyEx().setPreferredOperator(phoneId, info);
        } catch (RemoteException ex) {
            Log.e(TAG, "setPreferredOperator IMtkTelephonyEx re: " + ex.getMessage());
            return false;
        } catch (NullPointerException ex2) {
            Log.e(TAG, "setPreferredOperator IMtkTelephonyEx npe: " + ex2.getMessage());
            return false;
        }
    }

    public boolean setPreferredPhysicalSim(int slotId) {
        if (slotId < -1 || slotId > 2) {
            Log.e(TAG, "setPreferredPhysicalSim invalid slotId: " + slotId);
            return false;
        }
        try {
            return getIMtkTelephonyEx().setPreferredPhysicalSim(slotId);
        } catch (RemoteException ex) {
            Log.e(TAG, "setPreferredPhysicalSim IMtkTelephonyEx re: " + ex.getMessage());
            return false;
        } catch (NullPointerException ex2) {
            Log.e(TAG, "setPreferredPhysicalSim IMtkTelephonyEx npe: " + ex2.getMessage());
            return false;
        }
    }
}
