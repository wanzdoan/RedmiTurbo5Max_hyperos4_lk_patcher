package com.mediatek.phone;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserManager;
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.telephony.LocationAccessPolicy;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CarrierPrivilegesTracker;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.phone.ImsRcsController$$ExternalSyntheticBUOutline0;
import com.android.phone.PhoneGlobals;
import com.android.services.telephony.TelecomAccountRegistry;
import com.android.services.telephony.TelephonyConnectionService;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.mediatek.internal.telephony.FemtoCellInfo;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.NetworkInfoWithAcT;
import com.mediatek.internal.telephony.PreferredOperatorInfo;
import com.mediatek.internal.telephony.PseudoCellInfo;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.RadioManager;
import com.mediatek.internal.telephony.data.MtkDataHelper;
import com.mediatek.internal.telephony.phb.MtkIccPhoneBookInterfaceManager;
import com.mediatek.internal.telephony.selfactivation.ISelfActivation;
import com.mediatek.internal.telephony.uicc.IMtkRsuSml;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.internal.telephony.uicc.MtkUiccProfile;
import com.mediatek.telephony.IOemHookCallback;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.ot.pubsub.c.d$$ExternalSyntheticBUOutline0;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import java.util.ArrayList;
import java.util.List;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class MtkPhoneInterfaceManagerEx extends IMtkTelephonyEx.Stub {
    private static MtkPhoneInterfaceManagerEx sInstance;
    private PhoneGlobals mApp;
    private AppOpsManager mAppOps;
    private boolean mIsLastEccIms;
    private SharedPreferences mTelephonySharedPreferences;
    private UserManager mUserManager;
    private static final String[] PROPERTY_RIL_TEST_SIM = {"vendor.gsm.sim.ril.testsim", "vendor.gsm.sim.ril.testsim.2", "vendor.gsm.sim.ril.testsim.3", "vendor.gsm.sim.ril.testsim.4"};
    private static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    private static final String[] PROPERTY_UIM_SUBSCRIBER_ID = {"vendor.ril.uim.subscriberid.1", "vendor.ril.uim.subscriberid.2", "vendor.ril.uim.subscriberid.3", "vendor.ril.uim.subscriberid.4"};
    private static final String[] PROPERTY_RIL_CT3G = {"vendor.gsm.ril.ct3g", "vendor.gsm.ril.ct3g.2", "vendor.gsm.ril.ct3g.3", "vendor.gsm.ril.ct3g.4"};
    private static final String[] ICCRECORD_PROPERTY_ICCID = {"vendor.ril.iccid.sim1", "vendor.ril.iccid.sim2", "vendor.ril.iccid.sim3", "vendor.ril.iccid.sim4"};
    private final Object mSimPowerLock = new Object();
    private QueryAdnInfoThread mAdnInfoThread = null;
    private final Object mAdnInfoLock = new Object();
    private String[] PROPERTY_ICCID_SIM = {"vendor.ril.iccid.sim1", "vendor.ril.iccid.sim2", "vendor.ril.iccid.sim3", "vendor.ril.iccid.sim4"};
    private String[] PROPERTY_GSM_MCC_MNC = {"vendor.gsm.ril.uicc.mccmnc", "vendor.gsm.ril.uicc.mccmnc.1", "vendor.gsm.ril.uicc.mccmnc.2", "vendor.gsm.ril.uicc.mccmnc.3"};
    private String[] PROPERTY_CDMA_MCC_MNC = {"vendor.cdma.ril.uicc.mccmnc", "vendor.cdma.ril.uicc.mccmnc.1", "vendor.cdma.ril.uicc.mccmnc.2", "vendor.cdma.ril.uicc.mccmnc.3"};
    private boolean mIsSmlLockMode = SystemProperties.get("ro.vendor.sim_me_lock_mode", "").equals("3");
    private final Object mCallSubAddressLock = new Object();
    private CallSubAddressThread mCallSubAddressThread = null;
    protected MainThreadHandler mMainThreadHandler = new MainThreadHandler();

    public final class MtkIccAPDUArgument {
        public int channel;
        public int cla;
        public int command;
        public int family;
        public int p1;
        public int p2;
        public int p3;
        public String pathId;
        public int slotId;
        public String data = null;
        public String pin2 = null;

        public MtkIccAPDUArgument(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, String str) {
            this.channel = i3;
            this.cla = i4;
            this.command = i5;
            this.p1 = i6;
            this.p2 = i7;
            this.p3 = i8;
            this.pathId = str;
            this.slotId = i;
            this.family = i2;
        }
    }

    final class MainThreadRequest {
        public Object argument;
        public Phone phone;
        public Object result;
        public Integer subId;
        public WorkSource workSource;

        MainThreadRequest(Object obj, Phone phone, WorkSource workSource) {
            this.subId = -1;
            this.argument = obj;
            if (phone != null) {
                this.phone = phone;
            }
            this.workSource = workSource;
        }

        MainThreadRequest(Object obj, Integer num, WorkSource workSource) {
            this.subId = -1;
            this.argument = obj;
            if (num != null) {
                this.subId = num;
            }
            this.workSource = workSource;
        }
    }

    class EcmExitReceiver extends BroadcastReceiver {
        private MainThreadRequest mRequest;
        private int mSubId;

        public EcmExitReceiver(int i, MainThreadRequest mainThreadRequest) {
            this.mSubId = i;
            this.mRequest = mainThreadRequest;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED") && !intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false) && intent.getExtras().getInt("subscription") == this.mSubId) {
                this.mRequest.result = new EcmExitResult(MtkPhoneInterfaceManagerEx.this, this);
                synchronized (this.mRequest) {
                    this.mRequest.notifyAll();
                }
            }
        }
    }

    class EcmExitResult {
        private EcmExitReceiver mReceiver;

        public EcmExitResult(MtkPhoneInterfaceManagerEx mtkPhoneInterfaceManagerEx, EcmExitReceiver ecmExitReceiver) {
            this.mReceiver = ecmExitReceiver;
        }

        public EcmExitReceiver getReceiver() {
            return this.mReceiver;
        }
    }

    public class MainThreadHandler extends Handler {
        public MainThreadHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Object arrayList;
            Object obj;
            Object obj2;
            Phone phone;
            Object obj3;
            Object obj4;
            Object obj5;
            MtkPhoneInterfaceManagerEx.log("MainThreadHandler.handleMessage : " + message.what);
            int i = message.what;
            if (i == 1) {
                RequestOemAtCmd requestOemAtCmd = (RequestOemAtCmd) message.obj;
                Log.i("MtkPhoneIntfMgrEx", "CMD_SEND_AT_CMD:" + requestOemAtCmd.toString());
                PhoneFactory.getPhone(requestOemAtCmd.getSlotId()).invokeOemRilRequestRaw(requestOemAtCmd.getAtCmd().getBytes(), obtainMessage(2, requestOemAtCmd));
                return;
            }
            if (i == 2) {
                AsyncResult asyncResult = (AsyncResult) message.obj;
                RequestOemAtCmd requestOemAtCmd2 = (RequestOemAtCmd) asyncResult.userObj;
                Log.i("MtkPhoneIntfMgrEx", "EVENT_SEND_AT_CMD_DONE:" + requestOemAtCmd2.toString());
                if (asyncResult.exception == null) {
                    Object obj6 = asyncResult.result;
                    if (obj6 != null) {
                        String str = new String((byte[]) obj6);
                        if (requestOemAtCmd2.getCb() != null) {
                            try {
                                requestOemAtCmd2.getCb().onAtCmdResp(requestOemAtCmd2.getSlotId(), requestOemAtCmd2.getToken(), str);
                                return;
                            } catch (RemoteException e) {
                                Log.e("MtkPhoneIntfMgrEx", "RemoteException " + e);
                                return;
                            }
                        }
                        Log.e("MtkPhoneIntfMgrEx", "EVENT_SEND_AT_CMD_DONE cb is null");
                        return;
                    }
                    return;
                }
                if (requestOemAtCmd2.getCb() != null) {
                    try {
                        requestOemAtCmd2.getCb().onError("Fail for " + requestOemAtCmd2.toString());
                        return;
                    } catch (RemoteException e2) {
                        Log.e("MtkPhoneIntfMgrEx", "RemoteException " + e2);
                        return;
                    }
                }
                Log.e("MtkPhoneIntfMgrEx", "EVENT_SEND_AT_CMD_DONE cb is null with exception");
                return;
            }
            if (i == 3) {
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                String str2 = (String) asyncResult2.result;
                if (str2 == null) {
                    Log.e("MtkPhoneIntfMgrEx", "URC is null");
                    return;
                }
                RequestOemAtCmd requestOemAtCmd3 = (RequestOemAtCmd) asyncResult2.userObj;
                if (str2.indexOf(":") != -1) {
                    Log.d("MtkPhoneIntfMgrEx", "EVENT_AT_URC_IND(" + requestOemAtCmd3.getSlotId() + ") AT(prefix): " + str2.substring(0, str2.indexOf(":")));
                } else {
                    StringBuilder sb = new StringBuilder("EVENT_AT_URC_IND(");
                    sb.append(requestOemAtCmd3.getSlotId());
                    sb.append(") AT(first 5): ");
                    sb.append(str2.length() > 5 ? str2.substring(0, 5) : str2);
                    Log.d("MtkPhoneIntfMgrEx", sb.toString());
                }
                IOemHookCallback cb = requestOemAtCmd3.getCb();
                if (cb == null) {
                    Log.e("MtkPhoneIntfMgrEx", "cb == null");
                    return;
                }
                try {
                    cb.onAtUrcInd(requestOemAtCmd3.getSlotId(), str2);
                    return;
                } catch (RemoteException e3) {
                    Log.e("MtkPhoneIntfMgrEx", "RemoteException " + e3);
                    MtkGsmCdmaPhone phone2 = PhoneFactory.getPhone(requestOemAtCmd3.getSlotId());
                    if (phone2 != null) {
                        Log.e("MtkPhoneIntfMgrEx", "unregisterForOemIndication mMainThreadHandler " + requestOemAtCmd3.getSlotId());
                        phone2.unregisterForOemIndication(MtkPhoneInterfaceManagerEx.this.mMainThreadHandler);
                        return;
                    }
                    Log.e("MtkPhoneIntfMgrEx", "unregisterForOemIndication fail");
                    return;
                }
            }
            Object obj7 = null;
            if (i == 44) {
                MainThreadRequest mainThreadRequest = (MainThreadRequest) message.obj;
                Integer num = (Integer) mainThreadRequest.argument;
                if (MtkPhoneInterfaceManagerEx.getPhone(num.intValue()).isInEcm()) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
                    EcmExitReceiver ecmExitReceiver = MtkPhoneInterfaceManagerEx.this.new EcmExitReceiver(num.intValue(), mainThreadRequest);
                    MtkPhoneInterfaceManagerEx.log("Exit ECBM mode receiver " + ecmExitReceiver);
                    MtkPhoneInterfaceManagerEx.this.mApp.registerReceiver(ecmExitReceiver, intentFilter);
                    MtkPhoneInterfaceManagerEx.getPhone(num.intValue()).exitEmergencyCallbackMode();
                    return;
                }
                mainThreadRequest.result = new EcmExitResult(MtkPhoneInterfaceManagerEx.this, null);
                synchronized (mainThreadRequest) {
                    mainThreadRequest.notifyAll();
                }
                return;
            }
            if (i == 104) {
                MainThreadRequest mainThreadRequest2 = (MainThreadRequest) message.obj;
                IccCard uiccCardFromRequest = MtkPhoneInterfaceManagerEx.this.getUiccCardFromRequest(mainThreadRequest2);
                if (uiccCardFromRequest == null) {
                    MtkPhoneInterfaceManagerEx.loge("get ATR: No UICC");
                    mainThreadRequest2.result = "";
                    synchronized (mainThreadRequest2) {
                        mainThreadRequest2.notifyAll();
                    }
                    return;
                }
                uiccCardFromRequest.iccGetAtr(obtainMessage(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, mainThreadRequest2));
                return;
            }
            if (i != 105) {
                switch (i) {
                    case MiRilHookBase.EVENT_OEMHOOK_MODEM_STATS_DATA_GET /* 62 */:
                        MainThreadRequest mainThreadRequest3 = (MainThreadRequest) message.obj;
                        boolean zBooleanValue = ((Boolean) mainThreadRequest3.argument).booleanValue();
                        MtkPhoneInterfaceManagerEx.log("CMD_SET_DISABLE_2G: mode = " + zBooleanValue);
                        mainThreadRequest3.phone.setDisable2G(zBooleanValue, obtainMessage(63, mainThreadRequest3));
                        return;
                    case 63:
                        AsyncResult asyncResult3 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest4 = (MainThreadRequest) asyncResult3.userObj;
                        MtkPhoneInterfaceManagerEx.log("EVENT_SET_DISABLE_2G_DONE ar.exception: " + asyncResult3.exception);
                        if (asyncResult3.exception == null) {
                            mainThreadRequest4.result = Boolean.TRUE;
                        } else {
                            mainThreadRequest4.result = Boolean.FALSE;
                        }
                        synchronized (mainThreadRequest4) {
                            mainThreadRequest4.notifyAll();
                            break;
                        }
                        return;
                    case DynamicSarService.WIFI_DATA_STATE_EXTRA_APP_STATE /* 64 */:
                        MainThreadRequest mainThreadRequest5 = (MainThreadRequest) message.obj;
                        MtkPhoneInterfaceManagerEx.log("CMD_GET_DISABLE_2G");
                        mainThreadRequest5.phone.getDisable2G(obtainMessage(65, mainThreadRequest5));
                        return;
                    case 65:
                        AsyncResult asyncResult4 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest6 = (MainThreadRequest) asyncResult4.userObj;
                        MtkPhoneInterfaceManagerEx.log("EVENT_GET_DISABLE_2G_DONE ar.exception: " + asyncResult4.exception + " ar.result: " + asyncResult4.result);
                        mainThreadRequest6.result = asyncResult4.result;
                        synchronized (mainThreadRequest6) {
                            mainThreadRequest6.notifyAll();
                            break;
                        }
                        return;
                    case 66:
                        MainThreadRequest mainThreadRequest7 = (MainThreadRequest) message.obj;
                        MtkPhoneInterfaceManagerEx.log("CMD_GET_FEMTOCELL_LIST");
                        mainThreadRequest7.phone.getFemtoCellList(obtainMessage(67, mainThreadRequest7));
                        return;
                    case 67:
                        AsyncResult asyncResult5 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest8 = (MainThreadRequest) asyncResult5.userObj;
                        MtkPhoneInterfaceManagerEx.log("EVENT_GET_FEMTOCELL_LIST_DONE ar.exception: " + asyncResult5.exception + " ar.result: " + asyncResult5.result);
                        if (asyncResult5.exception != null || (arrayList = asyncResult5.result) == null) {
                            arrayList = new ArrayList(0);
                        }
                        mainThreadRequest8.result = arrayList;
                        synchronized (mainThreadRequest8) {
                            mainThreadRequest8.notifyAll();
                            break;
                        }
                        return;
                    case 68:
                        MainThreadRequest mainThreadRequest9 = (MainThreadRequest) message.obj;
                        MtkPhoneInterfaceManagerEx.log("CMD_ABORT_FEMTOCELL_LIST");
                        mainThreadRequest9.phone.abortFemtoCellList(obtainMessage(69, mainThreadRequest9));
                        return;
                    case 69:
                        AsyncResult asyncResult6 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest10 = (MainThreadRequest) asyncResult6.userObj;
                        MtkPhoneInterfaceManagerEx.log("EVENT_ABORT_FEMTOCELL_LIST_DONE ar.exception: " + asyncResult6.exception + " ar.result: " + asyncResult6.result);
                        if (asyncResult6.exception == null) {
                            mainThreadRequest10.result = Boolean.TRUE;
                        } else {
                            mainThreadRequest10.result = Boolean.FALSE;
                        }
                        synchronized (mainThreadRequest10) {
                            mainThreadRequest10.notifyAll();
                            break;
                        }
                        return;
                    case 70:
                        MainThreadRequest mainThreadRequest11 = (MainThreadRequest) message.obj;
                        MtkPhoneInterfaceManagerEx.log("CMD_SELECT_FEMTOCELL");
                        mainThreadRequest11.phone.selectFemtoCell((FemtoCellInfo) mainThreadRequest11.argument, obtainMessage(71, mainThreadRequest11));
                        return;
                    case 71:
                        AsyncResult asyncResult7 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest12 = (MainThreadRequest) asyncResult7.userObj;
                        MtkPhoneInterfaceManagerEx.log("EVENT_SELECT_FEMTOCELL_DONE ar.exception: " + asyncResult7.exception + " ar.result: " + asyncResult7.result);
                        if (asyncResult7.exception == null) {
                            mainThreadRequest12.result = Boolean.TRUE;
                        } else {
                            mainThreadRequest12.result = Boolean.FALSE;
                        }
                        synchronized (mainThreadRequest12) {
                            mainThreadRequest12.notifyAll();
                            break;
                        }
                        return;
                    case 72:
                        MainThreadRequest mainThreadRequest13 = (MainThreadRequest) message.obj;
                        MtkPhoneInterfaceManagerEx.log("CMD_QUERY_FEMTOCELL_SELECTION_MODE");
                        mainThreadRequest13.phone.queryFemtoCellSystemSelectionMode(obtainMessage(73, mainThreadRequest13));
                        return;
                    case 73:
                        AsyncResult asyncResult8 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest14 = (MainThreadRequest) asyncResult8.userObj;
                        MtkPhoneInterfaceManagerEx.log("EVENT_QUERY_FEMTOCELL_SELECTION_MODE_DONE ar.exception: " + asyncResult8.exception + " ar.result: " + asyncResult8.result);
                        if (asyncResult8.exception == null && (obj = asyncResult8.result) != null) {
                            mainThreadRequest14.result = obj;
                        } else {
                            mainThreadRequest14.result = -1;
                        }
                        synchronized (mainThreadRequest14) {
                            mainThreadRequest14.notifyAll();
                            break;
                        }
                        return;
                    case 74:
                        MainThreadRequest mainThreadRequest15 = (MainThreadRequest) message.obj;
                        Integer num2 = (Integer) mainThreadRequest15.argument;
                        num2.intValue();
                        MtkPhoneInterfaceManagerEx.log("CMD_SET_FEMTOCELL_SELECTION_MODE");
                        mainThreadRequest15.phone.setFemtoCellSystemSelectionMode(num2.intValue(), obtainMessage(75, mainThreadRequest15));
                        return;
                    case 75:
                        AsyncResult asyncResult9 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest16 = (MainThreadRequest) asyncResult9.userObj;
                        MtkPhoneInterfaceManagerEx.log("EVENT_SET_FEMTOCELL_SELECTION_MODE_DONE ar.exception: " + asyncResult9.exception + " ar.result: " + asyncResult9.result);
                        if (asyncResult9.exception == null) {
                            mainThreadRequest16.result = Boolean.TRUE;
                        } else {
                            mainThreadRequest16.result = Boolean.FALSE;
                        }
                        synchronized (mainThreadRequest16) {
                            mainThreadRequest16.notifyAll();
                            break;
                        }
                        return;
                    case 76:
                        MainThreadRequest mainThreadRequest17 = (MainThreadRequest) message.obj;
                        MtkPhoneInterfaceManagerEx.log("CMD_CANCEL_AVAILABLE_NETWORK");
                        mainThreadRequest17.phone.cancelAvailableNetworks(obtainMessage(77, mainThreadRequest17));
                        return;
                    case 77:
                        AsyncResult asyncResult10 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest18 = (MainThreadRequest) asyncResult10.userObj;
                        MtkPhoneInterfaceManagerEx.log("EVENT_CANCEL_AVAILABLE_NETWORK_DONE ar.exception: " + asyncResult10.exception + " ar.result: " + asyncResult10.result);
                        if (asyncResult10.exception == null) {
                            mainThreadRequest18.result = Boolean.TRUE;
                        } else {
                            mainThreadRequest18.result = Boolean.FALSE;
                        }
                        synchronized (mainThreadRequest18) {
                            mainThreadRequest18.notifyAll();
                            break;
                        }
                        return;
                    case 78:
                        MainThreadRequest mainThreadRequest19 = (MainThreadRequest) message.obj;
                        SuggestedPlmnListRequest suggestedPlmnListRequest = (SuggestedPlmnListRequest) mainThreadRequest19.argument;
                        mainThreadRequest19.phone.getSuggestedPlmnList(suggestedPlmnListRequest.rat, suggestedPlmnListRequest.num, suggestedPlmnListRequest.timer, obtainMessage(79, mainThreadRequest19));
                        return;
                    case 79:
                        AsyncResult asyncResult11 = (AsyncResult) message.obj;
                        MainThreadRequest mainThreadRequest20 = (MainThreadRequest) asyncResult11.userObj;
                        if (asyncResult11.exception != null || (obj2 = asyncResult11.result) == null) {
                            obj2 = new String[0];
                        }
                        mainThreadRequest20.result = obj2;
                        synchronized (mainThreadRequest20) {
                            mainThreadRequest20.notifyAll();
                            break;
                        }
                        return;
                    default:
                        switch (i) {
                            case 108:
                                MainThreadRequest mainThreadRequest21 = (MainThreadRequest) message.obj;
                                MtkIccAPDUArgument mtkIccAPDUArgument = (MtkIccAPDUArgument) mainThreadRequest21.argument;
                                MtkPhoneInterfaceManagerEx.log("CMD_LOAD_EF_TRANSPARENT: slot " + mtkIccAPDUArgument.slotId);
                                IccFileHandler iccFileHandler = UiccController.getInstance().getIccFileHandler(mtkIccAPDUArgument.slotId, mtkIccAPDUArgument.family);
                                if (iccFileHandler == null) {
                                    MtkPhoneInterfaceManagerEx.loge("loadEFTransparent: No UICC");
                                    mainThreadRequest21.result = new AsyncResult((Object) null, (Object) null, (Throwable) null);
                                    synchronized (mainThreadRequest21) {
                                        mainThreadRequest21.notifyAll();
                                        break;
                                    }
                                    return;
                                }
                                iccFileHandler.loadEFTransparent(mtkIccAPDUArgument.cla, mtkIccAPDUArgument.pathId, obtainMessage(109, mainThreadRequest21));
                                return;
                            case 109:
                                MtkPhoneInterfaceManagerEx.log("EVENT_LOAD_EF_TRANSPARENT_DONE");
                                AsyncResult asyncResult12 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest22 = (MainThreadRequest) asyncResult12.userObj;
                                if (asyncResult12.exception == null && asyncResult12.result != null) {
                                    mainThreadRequest22.result = new AsyncResult((Object) null, (byte[]) asyncResult12.result, (Throwable) null);
                                } else {
                                    mainThreadRequest22.result = new AsyncResult((Object) null, (Object) null, (Throwable) null);
                                }
                                synchronized (mainThreadRequest22) {
                                    mainThreadRequest22.notifyAll();
                                    break;
                                }
                                return;
                            case 110:
                                MainThreadRequest mainThreadRequest23 = (MainThreadRequest) message.obj;
                                MtkIccAPDUArgument mtkIccAPDUArgument2 = (MtkIccAPDUArgument) mainThreadRequest23.argument;
                                MtkPhoneInterfaceManagerEx.log("CMD_LOAD_EF_LINEARFIXEDALL: slot " + mtkIccAPDUArgument2.slotId);
                                IccFileHandler iccFileHandler2 = UiccController.getInstance().getIccFileHandler(mtkIccAPDUArgument2.slotId, mtkIccAPDUArgument2.family);
                                if (iccFileHandler2 == null) {
                                    MtkPhoneInterfaceManagerEx.loge("loadEFLinearFixedAll: No UICC");
                                    mainThreadRequest23.result = new AsyncResult((Object) null, (Object) null, (Throwable) null);
                                    synchronized (mainThreadRequest23) {
                                        mainThreadRequest23.notifyAll();
                                        break;
                                    }
                                    return;
                                }
                                iccFileHandler2.loadEFLinearFixedAll(mtkIccAPDUArgument2.cla, mtkIccAPDUArgument2.pathId, obtainMessage(111, mainThreadRequest23));
                                return;
                            case 111:
                                MtkPhoneInterfaceManagerEx.log("EVENT_LOAD_EF_LINEARFIXEDALL_DONE");
                                AsyncResult asyncResult13 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest24 = (MainThreadRequest) asyncResult13.userObj;
                                if (asyncResult13.exception == null && asyncResult13.result != null) {
                                    mainThreadRequest24.result = new AsyncResult((Object) null, (ArrayList) asyncResult13.result, (Throwable) null);
                                } else {
                                    mainThreadRequest24.result = new AsyncResult((Object) null, (Object) null, (Throwable) null);
                                }
                                synchronized (mainThreadRequest24) {
                                    mainThreadRequest24.notifyAll();
                                    break;
                                }
                                return;
                            case 112:
                                MainThreadRequest mainThreadRequest25 = (MainThreadRequest) message.obj;
                                if (mainThreadRequest25.subId.intValue() != -1) {
                                    phone = MtkPhoneInterfaceManagerEx.getPhone(mainThreadRequest25.subId.intValue());
                                } else {
                                    Log.i("MtkPhoneIntfMgrEx", "get main phone for instead");
                                    phone = PhoneFactory.getPhone(MtkPhoneInterfaceManagerEx.this.getMainCapabilityPhoneId());
                                }
                                if (phone == null) {
                                    Log.e("MtkPhoneIntfMgrEx", "MainThreadHandler: phone == null ");
                                    return;
                                } else {
                                    ((MtkGsmCdmaPhone) phone).invokeOemRilRequestRaw((byte[]) mainThreadRequest25.argument, obtainMessage(ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE, mainThreadRequest25));
                                    return;
                                }
                            case ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE /* 113 */:
                                AsyncResult asyncResult14 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest26 = (MainThreadRequest) asyncResult14.userObj;
                                mainThreadRequest26.result = asyncResult14;
                                synchronized (mainThreadRequest26) {
                                    mainThreadRequest26.notifyAll();
                                    break;
                                }
                                return;
                            case ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX /* 114 */:
                                MainThreadRequest mainThreadRequest27 = (MainThreadRequest) message.obj;
                                int iIntValue = ((Integer) mainThreadRequest27.argument).intValue();
                                MtkPhoneInterfaceManagerEx.log("CMD_SET_SIM_POWER: slotId = " + mainThreadRequest27.subId + " simState = " + iIntValue);
                                UiccController.getInstance().setSimPower(mainThreadRequest27.subId.intValue(), iIntValue, obtainMessage(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET, mainThreadRequest27));
                                return;
                            case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET /* 115 */:
                                AsyncResult asyncResult15 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest28 = (MainThreadRequest) asyncResult15.userObj;
                                MtkPhoneInterfaceManagerEx.log("EVENT_SET_SIM_POWER_DONE ar.exception: " + asyncResult15.exception + " ar.result: " + asyncResult15.result);
                                Throwable th = asyncResult15.exception;
                                if (th == null && (obj3 = asyncResult15.result) != null) {
                                    mainThreadRequest28.result = obj3;
                                } else if (th != null) {
                                    mainThreadRequest28.result = 54;
                                } else {
                                    mainThreadRequest28.result = 0;
                                }
                                synchronized (mainThreadRequest28) {
                                    mainThreadRequest28.notifyAll();
                                    break;
                                }
                                return;
                            case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET /* 116 */:
                                MainThreadRequest mainThreadRequest29 = (MainThreadRequest) message.obj;
                                int iIntValue2 = ((Integer) mainThreadRequest29.argument).intValue();
                                MtkPhoneInterfaceManagerEx.log("CMD_SWITCH_SIM_TYPE: slotId = " + mainThreadRequest29.subId + " cardType = " + iIntValue2);
                                UiccController.getInstance().switchSimType(mainThreadRequest29.subId.intValue(), iIntValue2, obtainMessage(117, mainThreadRequest29));
                                return;
                            case 117:
                                AsyncResult asyncResult16 = (AsyncResult) message.obj;
                                MainThreadRequest mainThreadRequest30 = (MainThreadRequest) asyncResult16.userObj;
                                MtkPhoneInterfaceManagerEx.log("EVENT_SWITCH_SIM_TYPE_DONE ar.exception: " + asyncResult16.exception + " ar.result: " + asyncResult16.result);
                                Throwable th2 = asyncResult16.exception;
                                if (th2 == null && (obj4 = asyncResult16.result) != null) {
                                    mainThreadRequest30.result = obj4;
                                } else if (th2 != null) {
                                    mainThreadRequest30.result = 10;
                                } else {
                                    mainThreadRequest30.result = 0;
                                }
                                synchronized (mainThreadRequest30) {
                                    mainThreadRequest30.notifyAll();
                                    break;
                                }
                                return;
                            default:
                                switch (i) {
                                    case 1000:
                                        MainThreadRequest mainThreadRequest31 = (MainThreadRequest) message.obj;
                                        mainThreadRequest31.phone.setRoamingEnable((int[]) mainThreadRequest31.argument, obtainMessage(1001, mainThreadRequest31));
                                        return;
                                    case 1001:
                                        AsyncResult asyncResult17 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest32 = (MainThreadRequest) asyncResult17.userObj;
                                        mainThreadRequest32.result = Boolean.valueOf(asyncResult17.exception == null);
                                        synchronized (mainThreadRequest32) {
                                            mainThreadRequest32.notifyAll();
                                            break;
                                        }
                                        return;
                                    case 1002:
                                        MainThreadRequest mainThreadRequest33 = (MainThreadRequest) message.obj;
                                        mainThreadRequest33.phone.getRoamingEnable(obtainMessage(1003, mainThreadRequest33));
                                        return;
                                    case 1003:
                                        AsyncResult asyncResult18 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest34 = (MainThreadRequest) asyncResult18.userObj;
                                        if (asyncResult18.exception == null && (obj5 = asyncResult18.result) != null) {
                                            obj7 = obj5;
                                        }
                                        mainThreadRequest34.result = obj7;
                                        synchronized (mainThreadRequest34) {
                                            mainThreadRequest34.notifyAll();
                                            break;
                                        }
                                        return;
                                    case 1004:
                                        MainThreadRequest mainThreadRequest35 = (MainThreadRequest) message.obj;
                                        mainThreadRequest35.phone.getPol(obtainMessage(1005, mainThreadRequest35));
                                        return;
                                    case 1005:
                                        AsyncResult asyncResult19 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest36 = (MainThreadRequest) asyncResult19.userObj;
                                        if (asyncResult19.exception == null && asyncResult19.result != null) {
                                            ArrayList arrayList2 = new ArrayList();
                                            ArrayList arrayList3 = (ArrayList) asyncResult19.result;
                                            for (int i2 = 0; i2 < arrayList3.size(); i2++) {
                                                NetworkInfoWithAcT networkInfoWithAcT = (NetworkInfoWithAcT) arrayList3.get(i2);
                                                arrayList2.add(new PreferredOperatorInfo(networkInfoWithAcT.getOperatorAlphaName(), networkInfoWithAcT.getOperatorNumeric(), networkInfoWithAcT.getAccessTechnology(), networkInfoWithAcT.getPriority()));
                                            }
                                            mainThreadRequest36.result = arrayList2;
                                        } else {
                                            mainThreadRequest36.result = new CommandException(CommandException.Error.INVALID_RESPONSE);
                                        }
                                        synchronized (mainThreadRequest36) {
                                            mainThreadRequest36.notifyAll();
                                            break;
                                        }
                                        return;
                                    case 1006:
                                        MainThreadRequest mainThreadRequest37 = (MainThreadRequest) message.obj;
                                        PreferredOperatorInfo preferredOperatorInfo = (PreferredOperatorInfo) mainThreadRequest37.argument;
                                        mainThreadRequest37.phone.setPolEntry(new NetworkInfoWithAcT(preferredOperatorInfo.getOperatorAlphaName(), preferredOperatorInfo.getOperatorNumeric(), preferredOperatorInfo.getAccessTechnology(), preferredOperatorInfo.getPriority()), obtainMessage(1007, mainThreadRequest37));
                                        return;
                                    case 1007:
                                        AsyncResult asyncResult20 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest38 = (MainThreadRequest) asyncResult20.userObj;
                                        mainThreadRequest38.result = Boolean.valueOf(asyncResult20.exception == null);
                                        synchronized (mainThreadRequest38) {
                                            mainThreadRequest38.notifyAll();
                                            break;
                                        }
                                        return;
                                    case 1008:
                                        MainThreadRequest mainThreadRequest39 = (MainThreadRequest) message.obj;
                                        mainThreadRequest39.phone.setPreferredPhysicalSim(((Integer) mainThreadRequest39.argument).intValue(), obtainMessage(1009, mainThreadRequest39));
                                        return;
                                    case 1009:
                                        AsyncResult asyncResult21 = (AsyncResult) message.obj;
                                        MainThreadRequest mainThreadRequest40 = (MainThreadRequest) asyncResult21.userObj;
                                        mainThreadRequest40.result = Boolean.valueOf(asyncResult21.exception == null);
                                        synchronized (mainThreadRequest40) {
                                            mainThreadRequest40.notifyAll();
                                            break;
                                        }
                                        return;
                                    default:
                                        Log.w("MtkPhoneIntfMgrEx", "MainThreadHandler: unexpected message code: " + message.what);
                                        return;
                                }
                        }
                }
            }
            AsyncResult asyncResult22 = (AsyncResult) message.obj;
            MainThreadRequest mainThreadRequest41 = (MainThreadRequest) asyncResult22.userObj;
            if (asyncResult22.exception == null) {
                MtkPhoneInterfaceManagerEx.log("EVENT_GET_ATR_DONE, no exception");
                mainThreadRequest41.result = asyncResult22.result;
            } else {
                MtkPhoneInterfaceManagerEx.loge("EVENT_GET_ATR_DONE, exception happens");
                mainThreadRequest41.result = "";
            }
            synchronized (mainThreadRequest41) {
                mainThreadRequest41.notifyAll();
            }
        }
    }

    private Object sendRequest(int i, Object obj) {
        return sendRequest(i, obj, -1, null, null);
    }

    private Object sendRequest(int i, Object obj, Integer num) {
        return sendRequest(i, obj, num, null, null);
    }

    private Object sendRequest(int i, Object obj, Phone phone, WorkSource workSource) {
        return sendRequest(i, obj, -1, phone, workSource);
    }

    private Object sendRequest(int i, Object obj, Integer num, Phone phone, WorkSource workSource) {
        MainThreadRequest mainThreadRequest;
        Object obj2;
        if (Looper.myLooper() == this.mMainThreadHandler.getLooper()) {
            d$$ExternalSyntheticBUOutline0.m("This method will deadlock if called from the main thread.");
            return null;
        }
        if (num.intValue() != -1 && phone != null) {
            Folme$$ExternalSyntheticBUOutline0.m("subId and phone cannot both be specified!");
            return null;
        }
        if (phone != null) {
            mainThreadRequest = new MainThreadRequest(obj, phone, workSource);
        } else {
            mainThreadRequest = new MainThreadRequest(obj, num, workSource);
        }
        this.mMainThreadHandler.obtainMessage(i, mainThreadRequest).sendToTarget();
        synchronized (mainThreadRequest) {
            while (true) {
                obj2 = mainThreadRequest.result;
                if (obj2 == null) {
                    try {
                        mainThreadRequest.wait();
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }
        return obj2;
    }

    public static MtkPhoneInterfaceManagerEx init(PhoneGlobals phoneGlobals) {
        MtkPhoneInterfaceManagerEx mtkPhoneInterfaceManagerEx;
        synchronized (MtkPhoneInterfaceManagerEx.class) {
            try {
                if (sInstance != null) {
                    Log.wtf("MtkPhoneIntfMgrEx", "init() called multiple times!  sInstance = " + sInstance);
                } else {
                    sInstance = new MtkPhoneInterfaceManagerEx(phoneGlobals);
                }
                mtkPhoneInterfaceManagerEx = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return mtkPhoneInterfaceManagerEx;
    }

    protected MtkPhoneInterfaceManagerEx(PhoneGlobals phoneGlobals) {
        this.mApp = phoneGlobals;
        this.mUserManager = (UserManager) phoneGlobals.getSystemService("user");
        this.mAppOps = (AppOpsManager) phoneGlobals.getSystemService("appops");
        this.mTelephonySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mApp);
        publish();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void publish() {
        ServiceManager.addService("phoneEx", this);
    }

    private Phone getPhoneFromRequest(MainThreadRequest mainThreadRequest) {
        return mainThreadRequest.subId.intValue() == -1 ? getDefaultPhone() : getPhone(mainThreadRequest.subId.intValue());
    }

    private Phone getDefaultPhone() {
        Phone phone = getPhone(getDefaultSubscription());
        return phone != null ? phone : PhoneFactory.getDefaultPhone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Phone getPhone(int i) {
        int phoneId = SubscriptionManagerService.getInstance().getPhoneId(i);
        if (phoneId < 0) {
            phoneId = Integer.MAX_VALUE;
        }
        return PhoneFactory.getPhone(phoneId);
    }

    protected static void log(String str) {
        Log.e("MtkPhoneIntfMgrEx", str);
    }

    protected static void loge(String str) {
        Log.e("MtkPhoneIntfMgrEx", str);
    }

    class UnlockSim extends Thread {
        private boolean mDone;
        private Handler mHandler;
        private boolean mResult;
        private int mRetryCount;
        private int mRsuLockState;
        private int mSIMMELockRetryCount;
        private MtkUiccProfile mSimCard;
        private int mUnlockResult;
        private int mVerifyResult;

        public UnlockSim(MtkPhoneInterfaceManagerEx mtkPhoneInterfaceManagerEx, IccCard iccCard) {
            this.mSimCard = null;
            this.mDone = false;
            this.mResult = false;
            this.mRsuLockState = -1;
            this.mVerifyResult = -1;
            this.mSIMMELockRetryCount = -1;
            this.mRetryCount = -1;
            this.mUnlockResult = 0;
            if (iccCard instanceof MtkUiccProfile) {
                this.mSimCard = (MtkUiccProfile) iccCard;
            } else {
                MtkPhoneInterfaceManagerEx.log("UnlockSim: Not MtkUiccProfile instance.");
            }
        }

        public UnlockSim(MtkPhoneInterfaceManagerEx mtkPhoneInterfaceManagerEx) {
            this.mSimCard = null;
            this.mDone = false;
            this.mResult = false;
            this.mRsuLockState = -1;
            this.mVerifyResult = -1;
            this.mSIMMELockRetryCount = -1;
            this.mRetryCount = -1;
            this.mUnlockResult = 0;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            synchronized (this) {
                this.mHandler = new Handler() { // from class: com.mediatek.phone.MtkPhoneInterfaceManagerEx.UnlockSim.1
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        switch (message.what) {
                            case 100:
                                synchronized (UnlockSim.this) {
                                    try {
                                        int[] iArr = (int[]) asyncResult.result;
                                        if (asyncResult.exception != null) {
                                            MtkPhoneInterfaceManagerEx.log("Query network lock fail");
                                            UnlockSim.this.mResult = false;
                                        } else {
                                            UnlockSim.this.mSIMMELockRetryCount = iArr[2];
                                            MtkPhoneInterfaceManagerEx.log("[SIMQUERY] Category = " + iArr[0] + " ,Network status =" + iArr[1] + " ,Retry count = " + iArr[2]);
                                            UnlockSim.this.mResult = true;
                                        }
                                        UnlockSim.this.mDone = true;
                                        UnlockSim.this.notifyAll();
                                    } catch (Throwable th) {
                                        throw th;
                                    }
                                    break;
                                }
                                return;
                            case 101:
                                MtkPhoneInterfaceManagerEx.log("SUPPLY_NETWORK_LOCK_COMPLETE");
                                synchronized (UnlockSim.this) {
                                    try {
                                        Throwable th2 = asyncResult.exception;
                                        if (th2 != null && (th2 instanceof CommandException)) {
                                            MtkPhoneInterfaceManagerEx.log("ar.exception " + asyncResult.exception);
                                            CommandException.Error commandError = asyncResult.exception.getCommandError();
                                            CommandException.Error error = CommandException.Error.PASSWORD_INCORRECT;
                                            UnlockSim unlockSim = UnlockSim.this;
                                            if (commandError == error) {
                                                unlockSim.mVerifyResult = 1;
                                            } else {
                                                unlockSim.mVerifyResult = 2;
                                            }
                                        } else {
                                            UnlockSim.this.mVerifyResult = 0;
                                        }
                                        UnlockSim.this.mDone = true;
                                        UnlockSim.this.notifyAll();
                                    } catch (Throwable th3) {
                                        throw th3;
                                    }
                                    break;
                                }
                                return;
                            case 102:
                                MtkPhoneInterfaceManagerEx.log("SET_DEVICE_NETWORK_LOCK_COMPLETE");
                                synchronized (UnlockSim.this) {
                                    try {
                                        UnlockSim.this.mRetryCount = message.arg1;
                                        CommandException commandException = asyncResult.exception;
                                        if (commandException != null) {
                                            if ((commandException instanceof CommandException) && commandException.getCommandError() == CommandException.Error.PASSWORD_INCORRECT) {
                                                UnlockSim.this.mUnlockResult = 1;
                                            } else {
                                                UnlockSim.this.mUnlockResult = 2;
                                            }
                                        } else {
                                            UnlockSim.this.mUnlockResult = 3;
                                        }
                                        UnlockSim.this.mDone = true;
                                        UnlockSim.this.notifyAll();
                                    } catch (Throwable th4) {
                                        throw th4;
                                    }
                                    break;
                                }
                                return;
                            case 103:
                                synchronized (UnlockSim.this) {
                                    try {
                                        if (asyncResult.exception != null) {
                                            MtkPhoneInterfaceManagerEx.log("[RSU-SIMLOCK] get RSU lock state fail");
                                            UnlockSim.this.mRsuLockState = -3;
                                        } else {
                                            UnlockSim.this.mRsuLockState = ((Integer) asyncResult.result).intValue();
                                            MtkPhoneInterfaceManagerEx.log("[RSU-SIMLOCK] mRsuLockState = " + UnlockSim.this.mRsuLockState);
                                        }
                                        UnlockSim.this.mDone = true;
                                        UnlockSim.this.notifyAll();
                                    } catch (Throwable th5) {
                                        throw th5;
                                    }
                                    break;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                };
                notifyAll();
            }
            Looper.loop();
        }

        synchronized Bundle queryNetworkLock(int i) {
            Bundle bundle;
            while (this.mHandler == null) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                }
            }
            MtkPhoneInterfaceManagerEx.log("Enter queryNetworkLock");
            this.mSimCard.queryIccNetworkLock(i, Message.obtain(this.mHandler, 100));
            while (!this.mDone) {
                try {
                    MtkPhoneInterfaceManagerEx.log("wait for done");
                    wait();
                } catch (InterruptedException unused2) {
                    Thread.currentThread().interrupt();
                }
            }
            this.mHandler.getLooper().quit();
            if (this.mHandler.getLooper().getThread() != null) {
                this.mHandler.getLooper().getThread().interrupt();
            }
            bundle = new Bundle();
            bundle.putBoolean("com.mediatek.phone.QUERY_SIMME_LOCK_RESULT", this.mResult);
            bundle.putInt("com.mediatek.phone.SIMME_LOCK_LEFT_COUNT", this.mSIMMELockRetryCount);
            MtkPhoneInterfaceManagerEx.log("done");
            return bundle;
        }

        synchronized int supplyNetworkLock(String str) {
            while (this.mHandler == null) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                }
            }
            MtkPhoneInterfaceManagerEx.log("Enter supplyNetworkLock");
            this.mSimCard.supplyNetworkDepersonalization(str, Message.obtain(this.mHandler, 101));
            while (!this.mDone) {
                try {
                    MtkPhoneInterfaceManagerEx.log("wait for done");
                    wait();
                } catch (InterruptedException unused2) {
                    Thread.currentThread().interrupt();
                }
            }
            this.mHandler.getLooper().quit();
            if (this.mHandler.getLooper().getThread() != null) {
                this.mHandler.getLooper().getThread().interrupt();
            }
            MtkPhoneInterfaceManagerEx.log("done");
            return this.mVerifyResult;
        }

        synchronized int[] supplyDeviceNetworkLock(String str) {
            Handler handler;
            int[] iArr;
            while (true) {
                handler = this.mHandler;
                if (handler != null) {
                    break;
                }
                try {
                    wait();
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
            UiccController.getInstance().supplyDeviceNetworkDepersonalization(str, Message.obtain(handler, 102));
            while (!this.mDone) {
                try {
                    Log.d("MtkPhoneIntfMgrEx", "wait for done");
                    wait();
                } catch (InterruptedException unused2) {
                    Thread.currentThread().interrupt();
                }
            }
            iArr = new int[]{this.mUnlockResult, this.mRetryCount};
            MtkPhoneInterfaceManagerEx.log("done, mUnlockResult = " + this.mUnlockResult + ", mRetryCount = " + this.mRetryCount);
            return iArr;
        }

        synchronized int getSimLockStateForRSU(int i) {
            while (this.mHandler == null) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                }
            }
            MtkPhoneInterfaceManagerEx.log("[RSU-SIMLOCK] Enter getSimLockStateForRSU");
            Message messageObtain = Message.obtain(this.mHandler, 103);
            MtkUiccController uiccController = UiccController.getInstance();
            if (uiccController != null) {
                IMtkRsuSml rsuSml = uiccController.getRsuSml();
                if (rsuSml != null) {
                    if (rsuSml.remoteSimlockGetSimlockStatusEx(messageObtain) == 1) {
                        MtkPhoneInterfaceManagerEx.log("[RSU-SIMLOCK] getSimLockStateForRSU, API error!");
                        return -2;
                    }
                    while (!this.mDone) {
                        try {
                            MtkPhoneInterfaceManagerEx.log("[RSU-SIMLOCK] getSimLockStateForRSU, wait for done");
                            wait();
                        } catch (InterruptedException unused2) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    this.mHandler.getLooper().quit();
                    if (this.mHandler.getLooper().getThread() != null) {
                        this.mHandler.getLooper().getThread().interrupt();
                    }
                    MtkPhoneInterfaceManagerEx.log("[RSU-SIMLOCK] getSimLockStateForRSU, done");
                    return this.mRsuLockState;
                }
                MtkPhoneInterfaceManagerEx.loge("[RSU-SIMLOCK] getSimLockStateForRSU, No RSU instance!");
                return -2;
            }
            MtkPhoneInterfaceManagerEx.log("[RSU-SIMLOCK] getSimLockStateForRSU, MtkUiccController not start now!");
            return -1;
        }
    }

    public Bundle queryNetworkLock(int i, int i2) {
        log("queryNetworkLock");
        UnlockSim unlockSim = new UnlockSim(this, getPhone(i).getIccCard());
        if (unlockSim.mSimCard == null) {
            return null;
        }
        unlockSim.start();
        return unlockSim.queryNetworkLock(i2);
    }

    public int supplyNetworkDepersonalization(int i, String str) {
        log("supplyNetworkDepersonalization");
        UnlockSim unlockSim = new UnlockSim(this, getPhone(i).getIccCard());
        if (unlockSim.mSimCard == null) {
            return 2;
        }
        unlockSim.start();
        return unlockSim.supplyNetworkLock(str);
    }

    public int[] supplyDeviceNetworkDepersonalization(String str) {
        log("supplyDeviceNetworkDepersonalization");
        int[] iArr = new int[2];
        if (TextUtils.isEmpty(str) || str.length() < 1) {
            iArr[0] = -1;
            iArr[1] = -1;
            return iArr;
        }
        if (this.mIsSmlLockMode) {
            int simLockPolicy = MtkTelephonyManagerEx.getDefault().getSimLockPolicy();
            if (simLockPolicy == -1) {
                iArr[0] = -1;
                iArr[1] = -1;
                return iArr;
            }
            if (simLockPolicy == 0) {
                iArr[0] = 0;
                iArr[1] = -1;
                return iArr;
            }
            UnlockSim unlockSim = new UnlockSim(this);
            unlockSim.start();
            return unlockSim.supplyDeviceNetworkLock(str);
        }
        iArr[0] = 0;
        iArr[1] = -1;
        return iArr;
    }

    public int getSimLockStateForRSU(int i) {
        log("[RSU-SIMLOCK] getSimLockStateForRSU");
        UnlockSim unlockSim = new UnlockSim(this);
        unlockSim.start();
        return unlockSim.getSimLockStateForRSU(i);
    }

    public void repollIccStateForNetworkLock(int i, boolean z) {
        if (TelephonyManager.getDefault().getPhoneCount() > 1) {
            int phoneId = SubscriptionManagerService.getInstance().getPhoneId(i);
            if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getPhoneCount()) {
                log("[repollIccStateForNetworkLock] phoneId: " + phoneId);
                return;
            } else {
                MtkUiccController uiccController = UiccController.getInstance();
                if (uiccController != null) {
                    uiccController.repollIccStateForModemSmlChangeFeatrue(phoneId, z);
                    return;
                }
                return;
            }
        }
        log("Not Support in Single SIM.");
    }

    public boolean isInHomeNetwork(int i) {
        ServiceState serviceState;
        int phoneId = SubscriptionManager.getPhoneId(i);
        Phone phone = PhoneFactory.getPhone(phoneId);
        boolean zInSameCountry = (phone == null || (serviceState = phone.getServiceState()) == null) ? false : inSameCountry(phoneId, serviceState.getVoiceOperatorNumeric());
        log("isInHomeNetwork, subId=" + i + " ,phoneId=" + phoneId + " ,isInHomeNetwork=" + zInSameCountry);
        return zInSameCountry;
    }

    private static final boolean inSameCountry(int i, String str) {
        boolean z = true;
        if (TextUtils.isEmpty(str) || str.length() < 5 || !TextUtils.isDigitsOnly(str)) {
            log("inSameCountry, Not a valid network, phoneId=" + i + ", operatorNumeric=" + str);
            return true;
        }
        String homeOperatorNumeric = getHomeOperatorNumeric(i);
        if (TextUtils.isEmpty(homeOperatorNumeric) || homeOperatorNumeric.length() < 5 || !TextUtils.isDigitsOnly(homeOperatorNumeric)) {
            log("inSameCountry, Not a valid SIM MCC, phoneId=" + i + ", homeNumeric=" + homeOperatorNumeric);
            return true;
        }
        String strSubstring = str.substring(0, 3);
        String strSubstring2 = homeOperatorNumeric.substring(0, 3);
        String strCountryCodeForMcc = MccTable.countryCodeForMcc(strSubstring);
        String strCountryCodeForMcc2 = MccTable.countryCodeForMcc(strSubstring2);
        log("inSameCountry, phoneId=" + i + ", homeMCC=" + strSubstring2 + ", networkMCC=" + strSubstring + ", homeCountry=" + strCountryCodeForMcc2 + ", networkCountry=" + strCountryCodeForMcc);
        if (!strCountryCodeForMcc.isEmpty() && !strCountryCodeForMcc2.isEmpty()) {
            boolean zEquals = strCountryCodeForMcc2.equals(strCountryCodeForMcc);
            if (zEquals) {
                return zEquals;
            }
            if ((!"us".equals(strCountryCodeForMcc2) || !"vi".equals(strCountryCodeForMcc)) && ((!"vi".equals(strCountryCodeForMcc2) || !"us".equals(strCountryCodeForMcc)) && (!"cn".equals(strCountryCodeForMcc2) || !"mo".equals(strCountryCodeForMcc)))) {
                z = zEquals;
            }
            log("inSameCountry, phoneId=" + i + ", inSameCountry=" + z);
        }
        return z;
    }

    private static final String getHomeOperatorNumeric(int i) {
        String simOperatorNumericForPhone = TelephonyManager.getDefault().getSimOperatorNumericForPhone(i);
        if (TextUtils.isEmpty(simOperatorNumericForPhone)) {
            simOperatorNumericForPhone = SystemProperties.get("ro.cdma.home.operator.numeric", "");
        }
        if (MtkTelephonyManagerEx.getDefault().isCt3gDualMode(i) && "20404".equals(simOperatorNumericForPhone)) {
            simOperatorNumericForPhone = "46003";
        }
        log("getHomeOperatorNumeric, phoneId=" + i + ", numeric=" + simOperatorNumericForPhone);
        return simOperatorNumericForPhone;
    }

    private int getSubIdBySlot(int i) {
        int[] subId = SubscriptionManager.getSubId(i);
        if (subId == null || subId.length == 0) {
            return getDefaultSubscription();
        }
        return subId[0];
    }

    public String getIccAtr(int i) {
        enforceModifyPermissionOrCarrierPrivilege(i);
        return (String) sendRequest(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, null, Integer.valueOf(i));
    }

    public byte[] loadEFTransparent(int i, int i2, int i3, String str) {
        enforceModifyPermissionOrCarrierPrivilege(getSubIdBySlot(i));
        return (byte[]) ((AsyncResult) sendRequest(108, new MtkIccAPDUArgument(i, i2, -1, i3, 176, 0, 0, 0, str))).result;
    }

    public List loadEFLinearFixedAll(int i, int i2, int i3, String str) {
        enforceModifyPermissionOrCarrierPrivilege(getSubIdBySlot(i));
        ArrayList arrayList = (ArrayList) ((AsyncResult) sendRequest(110, new MtkIccAPDUArgument(i, i2, -1, i3, 178, 0, 0, 0, str))).result;
        if (arrayList == null) {
            log("loadEFLinearFixedAll return null");
            return null;
        }
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            if (arrayList.get(i4) != null) {
                arrayList2.add(IccUtils.bytesToHexString((byte[]) arrayList.get(i4)));
            }
        }
        return arrayList2;
    }

    public int setSimPower(int i, int i2) {
        if (i < 0 || i >= TelephonyManager.getDefault().getSimCount()) {
            log("setSimPower error with invalid slotId:" + i);
            return -1;
        }
        enforceModifyPermissionOrCarrierPrivilege(getSubIdBySlot(i));
        log("setSimPower slotId = " + i + " state = " + i2);
        if (!SystemProperties.get("vendor.ril.sim.onoff.support").equals("1")) {
            return -1;
        }
        synchronized (this.mSimPowerLock) {
            try {
                if (getSimOnOffExecutingState(i) == i2) {
                    if (i2 == 11) {
                        return 13;
                    }
                    if (i2 == 10) {
                        return 12;
                    }
                } else if (getSimOnOffExecutingState(i) != -1 || getSimOnOffState(i) != i2) {
                    UiccController.getInstance().setSimOnOffExecutingState(i, i2);
                } else {
                    if (i2 == 11) {
                        return 15;
                    }
                    if (i2 == 10) {
                        return 14;
                    }
                }
                int iIntValue = ((Integer) sendRequest(ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX, new Integer(i2), new Integer(i))).intValue();
                log("setSimPower response = " + iIntValue);
                return iIntValue;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getSimOnOffState(int i) {
        if (i < 0 || i >= TelephonyManager.getDefault().getSimCount()) {
            log("getSimOnOffState error with invalid slotId:" + i);
            return -1;
        }
        if (UiccController.getInstance() instanceof MtkUiccController) {
            return UiccController.getInstance().getSimOnOffState(i);
        }
        log("getSimOnOffState, can't find MtkUiccController.");
        return 11;
    }

    public int getSimOnOffExecutingState(int i) {
        if (i < 0 || i >= TelephonyManager.getDefault().getSimCount()) {
            log("getSimOnOffExecutingState error with invalid slotId:" + i);
            return -1;
        }
        return UiccController.getInstance().getSimOnOffExecutingState(i);
    }

    public String getIccCardType(int i) {
        Phone phone = getPhone(i);
        if (phone == null) {
            return "";
        }
        return phone.getIccCard().getIccCardType();
    }

    public boolean isAppTypeSupported(int i, int i2) {
        UiccPort uiccPort = UiccController.getInstance().getUiccPort(i);
        return (uiccPort == null || uiccPort.getApplicationByType(i2) == null) ? false : true;
    }

    public boolean isTestIccCard(int i) {
        String str = SystemProperties.get(PROPERTY_RIL_TEST_SIM[i], "");
        return str != null && str.equals("1");
    }

    /* JADX WARN: Code duplicated, block: B:38:0x0080  */
    public int getIccAppFamily(int i) {
        int simCount = TelephonyManager.getDefault().getSimCount();
        int i2 = 0;
        if (i < 0 || i >= simCount) {
            log("getIccAppFamily, invalid slotId:" + i);
            return 0;
        }
        String str = SystemProperties.get(PROPERTY_RIL_FULL_UICC_TYPE[i]);
        String[] strArrSplit = str.split(",");
        int i3 = 0;
        for (int i4 = 0; i4 < strArrSplit.length; i4++) {
            if ("USIM".equals(strArrSplit[i4])) {
                i3 |= 2;
            } else if ("SIM".equals(strArrSplit[i4])) {
                i3 |= 1;
            } else if ("CSIM".equals(strArrSplit[i4])) {
                i3 |= 4;
            } else if ("RUIM".equals(strArrSplit[i4])) {
                i3 |= 8;
            }
        }
        if (i3 != 0) {
            int i5 = i3 & 4;
            if (i5 != 0 && (i3 & 2) != 0) {
                i2 = 3;
            } else if (i5 == 0 && (i3 & 8) == 0) {
                i2 = 1;
                if (i3 == 1 && "1".equals(SystemProperties.get(PROPERTY_RIL_CT3G[i]))) {
                    i2 = 2;
                }
            } else {
                i2 = 2;
            }
        }
        log("getIccAppFamily, uiccType[" + i + "] = " + str + "fullType = " + i3 + " iccType = " + i2);
        return i2;
    }

    private void enforceModifyPermissionOrCarrierPrivilege(int i) {
        if (this.mApp.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") == 0) {
            return;
        }
        log("No modify permission, check carrier privilege next.");
        enforceCarrierPrivilege(i);
    }

    private void enforcePrivilegedPhoneStatePermission() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", null);
    }

    private void enforceCarrierPrivilege(int i) {
        if (getCarrierPrivilegeStatus(i) == 1) {
            return;
        }
        loge("No Carrier Privilege.");
        ImsRcsController$$ExternalSyntheticBUOutline0.m("No Carrier Privilege.");
    }

    private int getCarrierPrivilegeStatus(int i) {
        int callingUid = Binder.getCallingUid();
        Phone phone = getPhone(i);
        if (phone == null) {
            loge("getCarrierPrivilegeStatus: Invalid subId");
            return 0;
        }
        CarrierPrivilegesTracker carrierPrivilegesTracker = phone.getCarrierPrivilegesTracker();
        if (carrierPrivilegesTracker == null) {
            loge("getCarrierPrivilegeStatus: No CarrierPrivilegesTracker");
            return -1;
        }
        return carrierPrivilegesTracker.getCarrierPrivilegeStatusForUid(callingUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IccCard getUiccCardFromRequest(MainThreadRequest mainThreadRequest) {
        Phone phoneFromRequest = getPhoneFromRequest(mainThreadRequest);
        if (phoneFromRequest == null) {
            return null;
        }
        return phoneFromRequest.getIccCard();
    }

    private int getDefaultSubscription() {
        return SubscriptionManagerService.getInstance().getDefaultSubId();
    }

    public String getMvnoMatchType(int i) {
        return getPhone(i).getMvnoMatchType();
    }

    public String getMvnoPattern(int i, String str) {
        return getPhone(i).getMvnoPattern(str);
    }

    public boolean isRadioOffBySimManagement(int i) {
        boolean zContains = false;
        try {
            SharedPreferences sharedPreferences = this.mApp.createPackageContext(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, 2).getSharedPreferences("RADIO_STATUS", 0);
            if (SubscriptionManagerService.getInstance() != null) {
                int phoneId = SubscriptionManagerService.getInstance().getPhoneId(i);
                if (phoneId >= 0 && phoneId < TelephonyManager.getDefault().getPhoneCount()) {
                    String iccId = null;
                    if (UiccController.getInstance() instanceof MtkUiccController) {
                        MtkUiccController uiccController = UiccController.getInstance();
                        if (uiccController != null) {
                            iccId = uiccController.getIccid(phoneId);
                        }
                    } else {
                        UiccPort uiccPort = UiccController.getInstance().getUiccPort(phoneId);
                        if (uiccPort != null) {
                            log("[isRadioOffBySimManagement]return AOSP iccid");
                            iccId = uiccPort.getIccId();
                        }
                    }
                    if (iccId != null && sharedPreferences != null) {
                        log("[isRadioOffBySimManagement]SharedPreferences: " + sharedPreferences.getAll().size());
                        zContains = sharedPreferences.contains(RadioManager.getInstance().getHashCode(iccId));
                    } else if (iccId == null) {
                        log("[isRadioOffBySimManagement] mIccId is null");
                    }
                }
                log("[isRadioOffBySimManagement]mSlotId: " + phoneId);
                return false;
            }
            log("[isRadioOffBySimManagement] SubscriptionManagerService is null");
            log("[isRadioOffBySimManagement]result: " + zContains);
            return zContains;
        } catch (PackageManager.NameNotFoundException unused) {
            log("Fail to create com.android.phone createPackageContext");
            return false;
        }
    }

    public boolean isFdnEnabled(int i) {
        log("isFdnEnabled subId=" + i);
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            loge("Error subId: " + i);
            return false;
        }
        Phone phone = getPhone(i);
        return phone != null && phone.getIccCard() != null && phone.getIccCard().getIccFdnAvailable() && phone.getIccCard().getIccFdnEnabled();
    }

    private boolean canReadPhoneState(String str, String str2) {
        try {
            this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", str2);
            return true;
        } catch (SecurityException unused) {
            return false;
        }
    }

    public String getUimSubscriberId(String str, int i) {
        if (!canReadPhoneState(str, "getUimSubscriberId")) {
            log("getUimImsiBySubId: permission denied");
            return null;
        }
        int phoneId = SubscriptionManager.getPhoneId(i);
        if (phoneId >= 0) {
            String[] strArr = PROPERTY_UIM_SUBSCRIBER_ID;
            if (phoneId < strArr.length) {
                return SystemProperties.get(strArr[phoneId], "");
            }
        }
        log("getUimImsiBySubId:invalid phoneId " + phoneId);
        return null;
    }

    public String[] getSimOperatorNumericForPhoneEx(int i) {
        if (i < 0 || i >= TelephonyManager.getDefault().getSimCount()) {
            log("getSimOperatorNumericForPhoneEx with invalid phoneId:" + i);
            return null;
        }
        return new String[]{SystemProperties.get(this.PROPERTY_GSM_MCC_MNC[i], ""), SystemProperties.get(this.PROPERTY_CDMA_MCC_MNC[i], "")};
    }

    public boolean setRadioCapability(RadioAccessFamily[] radioAccessFamilyArr) {
        enforceModifyPermission();
        try {
            ProxyController.getInstance().setRadioCapability(radioAccessFamilyArr);
            return true;
        } catch (RuntimeException e) {
            Log.w("MtkPhoneIntfMgrEx", "setRadioCapability: Runtime Exception");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isCapabilitySwitching() {
        return ProxyController.getInstance().isCapabilitySwitching();
    }

    public int getMainCapabilityPhoneId() {
        return RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
    }

    public boolean isImsRegistered(int i) {
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        return phone.isImsRegistered();
    }

    public boolean isVolteEnabled(int i) {
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        return phone.isVolteEnabled();
    }

    public boolean isWifiCallingEnabled(int i) {
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        return phone.isWifiCallingEnabled();
    }

    public int[] getAdnStorageInfo(int i) {
        int[] iArrGetAdnStorageInfo;
        Log.d("MtkPhoneIntfMgrEx", "getAdnStorageInfo subid:" + i + ",phbstate:" + isPhbReady(i));
        if (SubscriptionManager.isValidSubscriptionId(i) && isPhbReady(i)) {
            synchronized (this.mAdnInfoLock) {
                try {
                    QueryAdnInfoThread queryAdnInfoThread = this.mAdnInfoThread;
                    if (queryAdnInfoThread == null) {
                        Log.d("MtkPhoneIntfMgrEx", "getAdnStorageInfo new thread !");
                        QueryAdnInfoThread queryAdnInfoThread2 = new QueryAdnInfoThread(i);
                        this.mAdnInfoThread = queryAdnInfoThread2;
                        queryAdnInfoThread2.start();
                    } else {
                        queryAdnInfoThread.setSubId(i);
                        Log.d("MtkPhoneIntfMgrEx", "getAdnStorageInfo old thread !");
                    }
                    iArrGetAdnStorageInfo = this.mAdnInfoThread.GetAdnStorageInfo();
                } catch (Throwable th) {
                    throw th;
                }
            }
            return iArrGetAdnStorageInfo;
        }
        Log.d("MtkPhoneIntfMgrEx", "getAdnStorageInfo subId is invalid.");
        return new int[]{0, 0, 0, 0};
    }

    class CallSubAddressThread extends Thread {
        private boolean mDone = false;
        private Handler mHandler;
        private int mPhoneId;
        private boolean mResult;

        public CallSubAddressThread(int i) {
            this.mPhoneId = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            synchronized (this) {
                this.mHandler = new Handler() { // from class: com.mediatek.phone.MtkPhoneInterfaceManagerEx.CallSubAddressThread.1
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (message.what != 100) {
                            return;
                        }
                        synchronized (CallSubAddressThread.this) {
                            try {
                                boolean z = true;
                                CallSubAddressThread.this.mDone = true;
                                if (asyncResult.exception != null) {
                                    Log.d("MtkPhoneIntfMgrEx", "getCallSubAddressEnabled exception");
                                    CallSubAddressThread.this.mResult = false;
                                } else {
                                    Object obj = asyncResult.result;
                                    int[] iArr = obj != null ? (int[]) obj : null;
                                    if (iArr != null && iArr.length > 0) {
                                        CallSubAddressThread callSubAddressThread = CallSubAddressThread.this;
                                        if (iArr[0] != 1) {
                                            z = false;
                                        }
                                        callSubAddressThread.mResult = z;
                                        Log.d("MtkPhoneIntfMgrEx", "EVENT_GET_CALL_SUBADDR_ENABLE " + CallSubAddressThread.this.mResult);
                                    } else {
                                        Log.d("MtkPhoneIntfMgrEx", "EVENT_GET_CALL_SUBADDR_ENABLE");
                                    }
                                }
                                CallSubAddressThread.this.notifyAll();
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                };
                notifyAll();
            }
            Looper.loop();
        }

        public boolean getCallSubAddressEnabled() {
            Handler handler;
            synchronized (this) {
                while (true) {
                    try {
                        handler = this.mHandler;
                        if (handler != null) {
                            break;
                        }
                        try {
                            wait();
                        } catch (InterruptedException unused) {
                            Thread.currentThread().interrupt();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                    throw th;
                }
                Message messageObtain = Message.obtain(handler, 100);
                MtkGsmCdmaPhone phone = PhoneFactory.getPhone(this.mPhoneId);
                if (phone == null) {
                    MtkPhoneInterfaceManagerEx.loge("getCallSubAddressEnabled, phone null (phoneId=" + this.mPhoneId + ")");
                    return false;
                }
                phone.getCallSubAddress(messageObtain);
                while (!this.mDone) {
                    try {
                        Log.d("MtkPhoneIntfMgrEx", "getCallSubAddressEnabled wait for done");
                        wait();
                    } catch (InterruptedException unused2) {
                        Thread.currentThread().interrupt();
                    }
                }
                Log.d("MtkPhoneIntfMgrEx", "getCallSubAddressEnabled done");
                return this.mResult;
            }
        }
    }

    class QueryAdnInfoThread extends Thread {
        private boolean mDone = false;
        private Handler mHandler;
        private int mSubId;
        private int[] recordSize;

        public QueryAdnInfoThread(int i) {
            this.mSubId = i;
        }

        public void setSubId(int i) {
            synchronized (this) {
                this.mSubId = i;
                this.mDone = false;
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            synchronized (this) {
                this.mHandler = new Handler() { // from class: com.mediatek.phone.MtkPhoneInterfaceManagerEx.QueryAdnInfoThread.1
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        if (message.what != 100) {
                            return;
                        }
                        Log.d("MtkPhoneIntfMgrEx", "EVENT_QUERY_PHB_ADN_INFO");
                        synchronized (QueryAdnInfoThread.this) {
                            try {
                                QueryAdnInfoThread.this.mDone = true;
                                int[] iArr = (int[]) asyncResult.result;
                                if (iArr != null && iArr.length == 4) {
                                    QueryAdnInfoThread.this.recordSize = new int[4];
                                    QueryAdnInfoThread.this.recordSize[0] = iArr[0];
                                    QueryAdnInfoThread.this.recordSize[1] = iArr[1];
                                    QueryAdnInfoThread.this.recordSize[2] = iArr[2];
                                    QueryAdnInfoThread.this.recordSize[3] = iArr[3];
                                    Log.d("MtkPhoneIntfMgrEx", "recordSize[0]=" + QueryAdnInfoThread.this.recordSize[0] + ",recordSize[1]=" + QueryAdnInfoThread.this.recordSize[1] + ",recordSize[2]=" + QueryAdnInfoThread.this.recordSize[2] + ",recordSize[3]=" + QueryAdnInfoThread.this.recordSize[3]);
                                } else {
                                    QueryAdnInfoThread.this.recordSize = new int[4];
                                    QueryAdnInfoThread.this.recordSize[0] = 0;
                                    QueryAdnInfoThread.this.recordSize[1] = 0;
                                    QueryAdnInfoThread.this.recordSize[2] = 0;
                                    QueryAdnInfoThread.this.recordSize[3] = 0;
                                }
                                QueryAdnInfoThread.this.notifyAll();
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                };
                notifyAll();
            }
            Looper.loop();
        }

        public int[] GetAdnStorageInfo() {
            Handler handler;
            int[] iArr;
            synchronized (this) {
                while (true) {
                    handler = this.mHandler;
                    if (handler != null) {
                        break;
                    }
                    try {
                        wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                    throw th;
                }
                MtkPhoneInterfaceManagerEx.getPhone(this.mSubId).queryPhbStorageInfo(0, Message.obtain(handler, 100));
                while (!this.mDone) {
                    try {
                        Log.d("MtkPhoneIntfMgrEx", "wait for done");
                        wait();
                    } catch (InterruptedException unused2) {
                        Thread.currentThread().interrupt();
                    }
                }
                Log.d("MtkPhoneIntfMgrEx", "done");
                iArr = this.recordSize;
            }
            return iArr;
        }
    }

    public boolean isPhbReady(int i) {
        Phone phone;
        MtkIccPhoneBookInterfaceManager iccPhoneBookInterfaceManager;
        int phoneId = SubscriptionManager.getPhoneId(i);
        if (!SubscriptionManager.isValidSlotIndex(SubscriptionManager.getSlotIndex(i)) || (phone = PhoneFactory.getPhone(phoneId)) == null || (iccPhoneBookInterfaceManager = phone.getIccPhoneBookInterfaceManager()) == null || !(iccPhoneBookInterfaceManager instanceof MtkIccPhoneBookInterfaceManager)) {
            return false;
        }
        return iccPhoneBookInterfaceManager.isPhbReady();
    }

    public int selfActivationAction(int i, Bundle bundle, int i2) {
        ISelfActivation selfActivationInstance = getPhone(i2).getSelfActivationInstance();
        if (selfActivationInstance != null) {
            return selfActivationInstance.selfActivationAction(i, bundle);
        }
        return 0;
    }

    public int getSelfActivateState(int i) {
        ISelfActivation selfActivationInstance = getPhone(i).getSelfActivationInstance();
        if (selfActivationInstance != null) {
            return selfActivationInstance.getSelfActivateState();
        }
        return 0;
    }

    public int getPCO520State(int i) {
        ISelfActivation selfActivationInstance = getPhone(i).getSelfActivationInstance();
        if (selfActivationInstance != null) {
            return selfActivationInstance.getPCO520State();
        }
        return 0;
    }

    public boolean exitEmergencyCallbackMode(int i) {
        log("exitEmergencyCallbackMode, subId: " + i);
        enforceModifyPermission();
        if (SubscriptionManager.getPhoneId(i) == -1) {
            log("no corresponding phone id");
            return false;
        }
        EcmExitResult ecmExitResult = (EcmExitResult) sendRequest(44, Integer.valueOf(i));
        if (ecmExitResult.getReceiver() == null) {
            return true;
        }
        log("unregisterReceiver " + ecmExitResult.getReceiver());
        this.mApp.unregisterReceiver(ecmExitResult.getReceiver());
        return true;
    }

    public void setApcModeUsingSlotId(int i, int i2, boolean z, int i3) {
        log("setApcModeUsingSlotId, slotId:" + i + ", mode:" + i2 + ", reportOn:" + z + ", reportInterval:" + i3);
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = getPhone(getSubIdBySlot(i));
        if (phone != null) {
            if (phone.getPhoneType() == 1) {
                phone.setApcMode(i2, z, i3);
                return;
            } else {
                log("setApcModeUsingSlotId: phone type is abnormal");
                return;
            }
        }
        log("setApcModeUsingSlotId, phone or subId: is null");
    }

    public PseudoCellInfo getApcInfoUsingSlotId(int i) {
        log("getApcInfoUsingSlotId, slotId:" + i);
        MtkGsmCdmaPhone phone = getPhone(getSubIdBySlot(i));
        if (phone != null) {
            if (phone.getPhoneType() == 1) {
                return phone.getApcInfo();
            }
            log("getApcInfoUsingSlotId: phone type is abnormal");
            return null;
        }
        log("getApcInfoUsingSlotId, phone or subId: is null");
        return null;
    }

    public int getCdmaSubscriptionActStatus(int i) {
        if (getPhone(i) != null) {
            return 0;
        }
        log("fail to getCdmaSubscriptionActStatus due to phone is null");
        return 0;
    }

    public void setIsLastEccIms(boolean z) {
        this.mIsLastEccIms = z;
    }

    public boolean getIsLastEccIms() {
        return this.mIsLastEccIms;
    }

    class RequestOemAtCmd {
        String atCmd;
        IOemHookCallback cb;
        int slotId;
        long token;

        RequestOemAtCmd(MtkPhoneInterfaceManagerEx mtkPhoneInterfaceManagerEx, int i, long j, String str, IOemHookCallback iOemHookCallback) {
            this.slotId = i;
            this.token = j;
            this.atCmd = str;
            this.cb = iOemHookCallback;
        }

        public int getSlotId() {
            return this.slotId;
        }

        public long getToken() {
            return this.token;
        }

        public String getAtCmd() {
            return this.atCmd;
        }

        public IOemHookCallback getCb() {
            return this.cb;
        }

        public String toString() {
            return "slotId=" + this.slotId + " token=" + this.token + " atCmd=" + this.atCmd;
        }
    }

    public void registerAtUrcInd(int i, IOemHookCallback iOemHookCallback) {
        enforceModifyPermission();
        Log.i("MtkPhoneIntfMgrEx", "registerAtUrcInd slotId=" + i);
        if (iOemHookCallback == null) {
            Log.e("MtkPhoneIntfMgrEx", "registerAtUrcInd is null");
            return;
        }
        try {
            iOemHookCallback.onError("Indication of all URC is unsupported");
        } catch (RemoteException e) {
            Log.e("MtkPhoneIntfMgrEx", "RemoteException " + e);
        }
        if (i < 0 || i >= TelephonyManager.getDefault().getPhoneCount()) {
            Log.e("MtkPhoneIntfMgrEx", "invalid slotId=" + i + ", phoneCount=" + TelephonyManager.getDefault().getPhoneCount());
            try {
                iOemHookCallback.onError("unsupport slotId");
                return;
            } catch (RemoteException e2) {
                Log.e("MtkPhoneIntfMgrEx", "RemoteException " + e2);
                return;
            }
        }
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            Log.e("MtkPhoneIntfMgrEx", "phone is null slotId=" + i);
            try {
                iOemHookCallback.onError("Internal Error");
                return;
            } catch (RemoteException e3) {
                Log.e("MtkPhoneIntfMgrEx", "RemoteException " + e3);
                return;
            }
        }
        phone.registerForOemIndication(this.mMainThreadHandler, 3, new RequestOemAtCmd(this, i, -1L, "URC", iOemHookCallback));
    }

    public void sendAtCmd(int i, long j, String str, IOemHookCallback iOemHookCallback) {
        enforceModifyPermission();
        if (iOemHookCallback == null) {
            Log.e("MtkPhoneIntfMgrEx", "sendAtCmd cb = null");
            return;
        }
        if (PhoneFactory.getPhone(i) == null) {
            try {
                iOemHookCallback.onError("unsupport slotId=" + i);
                return;
            } catch (RemoteException e) {
                Log.e("MtkPhoneIntfMgrEx", "RemoteException " + e);
                return;
            }
        }
        if (str == null || TextUtils.isEmpty(str)) {
            try {
                iOemHookCallback.onError("atCmd is null or empty");
                return;
            } catch (RemoteException e2) {
                Log.e("MtkPhoneIntfMgrEx", "RemoteException " + e2);
                return;
            }
        }
        RequestOemAtCmd requestOemAtCmd = new RequestOemAtCmd(this, i, j, str, iOemHookCallback);
        Log.v("MtkPhoneIntfMgrEx", "sendAtCmd " + requestOemAtCmd.toString());
        this.mMainThreadHandler.obtainMessage(1, requestOemAtCmd).sendToTarget();
    }

    public int invokeOemRilRequestRaw(byte[] bArr, byte[] bArr2) {
        enforceModifyPermission();
        try {
            AsyncResult asyncResult = (AsyncResult) sendRequest(112, bArr);
            CommandException commandException = asyncResult.exception;
            if (commandException == null) {
                Object obj = asyncResult.result;
                if (obj == null) {
                    return 0;
                }
                byte[] bArr3 = (byte[]) obj;
                if (bArr3.length > bArr2.length) {
                    Log.w("MtkPhoneIntfMgrEx", "Buffer to copy response too small: Response length is " + bArr3.length + "bytes. Buffer Size is " + bArr2.length + "bytes.");
                }
                System.arraycopy(bArr3, 0, bArr2, 0, bArr3.length);
                return bArr3.length;
            }
            int iOrdinal = commandException.getCommandError().ordinal();
            return iOrdinal > 0 ? iOrdinal * (-1) : iOrdinal;
        } catch (RuntimeException unused) {
            Log.w("MtkPhoneIntfMgrEx", "sendOemRilRequestRaw: Runtime Exception");
            int iOrdinal2 = CommandException.Error.GENERIC_FAILURE.ordinal();
            return iOrdinal2 > 0 ? iOrdinal2 * (-1) : iOrdinal2;
        }
    }

    public int invokeOemRilRequestRawBySlot(int i, byte[] bArr, byte[] bArr2) {
        enforceModifyPermission();
        try {
            if (getSubIdBySlot(i) == -1) {
                Log.w("MtkPhoneIntfMgrEx", "sendOemRilRequestRawBySlot: invalid slot id");
                int iOrdinal = CommandException.Error.GENERIC_FAILURE.ordinal();
                return iOrdinal > 0 ? iOrdinal * (-1) : iOrdinal;
            }
            AsyncResult asyncResult = (AsyncResult) sendRequest(112, bArr, Integer.valueOf(getSubIdBySlot(i)));
            CommandException commandException = asyncResult.exception;
            if (commandException == null) {
                Object obj = asyncResult.result;
                if (obj == null) {
                    return 0;
                }
                byte[] bArr3 = (byte[]) obj;
                if (bArr3.length > bArr2.length) {
                    Log.w("MtkPhoneIntfMgrEx", "Buffer to copy response too small: Response length is " + bArr3.length + "bytes. Buffer Size is " + bArr2.length + "bytes.");
                }
                System.arraycopy(bArr3, 0, bArr2, 0, bArr3.length);
                return bArr3.length;
            }
            int iOrdinal2 = commandException.getCommandError().ordinal();
            return iOrdinal2 > 0 ? iOrdinal2 * (-1) : iOrdinal2;
        } catch (RuntimeException unused) {
            Log.w("MtkPhoneIntfMgrEx", "sendOemRilRequestRawBySlot: Runtime Exception");
            int iOrdinal3 = CommandException.Error.GENERIC_FAILURE.ordinal();
            return iOrdinal3 > 0 ? iOrdinal3 * (-1) : iOrdinal3;
        }
    }

    private void enforceModifyPermission() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", null);
    }

    public boolean isInCsCall(int i) {
        log("[isInCsCall] phoneId:" + i);
        Phone phone = PhoneFactory.getPhone(i);
        return (phone == null || phone.getCallTracker() == null || phone.getCallTracker().getState() == PhoneConstants.State.IDLE) ? false : true;
    }

    public String getLocatedPlmn(int i) {
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone != null) {
            return phone.getLocatedPlmn();
        }
        return null;
    }

    public boolean setDisable2G(int i, boolean z) {
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("setDisable2G, phone == null, phoneId=" + i);
            return false;
        }
        return ((Boolean) sendRequest(62, new Boolean(z), phone, null)).booleanValue();
    }

    public int getDisable2G(int i) {
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("getDisable2G, phone == null, phoneId=" + i);
            return -1;
        }
        return ((int[]) sendRequest(64, null, phone, null))[0];
    }

    public List getFemtoCellList(int i) {
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("getFemtoCellList, phone == null, phoneId=" + i);
            return null;
        }
        ArrayList arrayList = new ArrayList();
        List list = (List) sendRequest(66, null, phone, null);
        if (list != null) {
            arrayList.addAll(list);
        }
        return arrayList;
    }

    public boolean abortFemtoCellList(int i) {
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("abortFemtoCellList, phone == null, phoneId=" + i);
            return false;
        }
        return ((Boolean) sendRequest(68, null, phone, null)).booleanValue();
    }

    public boolean selectFemtoCell(int i, FemtoCellInfo femtoCellInfo) {
        if (femtoCellInfo == null) {
            log("selectFemtoCell, femtocell == null");
            return false;
        }
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("selectFemtoCell, phone == null, phoneId=" + i);
            return false;
        }
        return ((Boolean) sendRequest(70, femtoCellInfo, phone, null)).booleanValue();
    }

    public int queryFemtoCellSystemSelectionMode(int i) {
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("queryFemtoCellSystemSelectionMode, phone == null, phoneId=" + i);
            return -1;
        }
        Object objSendRequest = sendRequest(72, null, phone, null);
        if (objSendRequest instanceof int[]) {
            return ((int[]) objSendRequest)[0];
        }
        return -1;
    }

    public boolean setFemtoCellSystemSelectionMode(int i, int i2) {
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("queryFemtoCellSystemSelectionMode, phone == null, phoneId=" + i);
            return false;
        }
        return ((Boolean) sendRequest(74, new Integer(i2), phone, null)).booleanValue();
    }

    public boolean cancelAvailableNetworks(int i) {
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("cancelAvailableNetworks, phone == null, phoneId=" + i);
            return false;
        }
        return ((Boolean) sendRequest(76, null, phone, null)).booleanValue();
    }

    public ServiceState getServiceStateByPhoneId(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, -1, str, str2, "getServiceStateForSubscriber")) {
            return null;
        }
        LocationAccessPolicy.LocationPermissionResult locationPermissionResultCheckLocationPermission = LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getServiceStateForSubscriber").setLogAsInfo(true).setMinSdkVersionForFine(29).setMinSdkVersionForCoarse(29).setMinSdkVersionForEnforcement(29).build());
        LocationAccessPolicy.LocationPermissionResult locationPermissionResultCheckLocationPermission2 = LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getServiceStateForSubscriber").setLogAsInfo(true).setMinSdkVersionForCoarse(29).setMinSdkVersionForFine(Integer.MAX_VALUE).setMinSdkVersionForEnforcement(29).build());
        LocationAccessPolicy.LocationPermissionResult locationPermissionResult = LocationAccessPolicy.LocationPermissionResult.ALLOWED;
        boolean z = locationPermissionResultCheckLocationPermission == locationPermissionResult;
        boolean z2 = locationPermissionResultCheckLocationPermission2 == locationPermissionResult;
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null) {
                return null;
            }
            ServiceState serviceState = phone.getServiceState();
            if (z) {
                return serviceState;
            }
            return z2 ? serviceState.createLocationInfoSanitizedCopy(false) : serviceState.createLocationInfoSanitizedCopy(true);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean setRoamingEnable(int i, int[] iArr) {
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("setRoamingEnable, phone == null, phoneId=" + i);
            return false;
        }
        return ((Boolean) sendRequest(1000, iArr, phone, null)).booleanValue();
    }

    public int[] getRoamingEnable(int i) {
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("getRoamingEnable, phone == null, phoneId=" + i);
            return null;
        }
        return (int[]) sendRequest(1002, null, phone, null);
    }

    class SuggestedPlmnListRequest {
        int num;
        int rat;
        int timer;

        public SuggestedPlmnListRequest(MtkPhoneInterfaceManagerEx mtkPhoneInterfaceManagerEx, int i, int i2, int i3) {
            this.rat = i;
            this.num = i2;
            this.timer = i3;
        }
    }

    public String[] getSuggestedPlmnList(int i, int i2, int i3, int i4, String str) {
        TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mApp, getSubIdBySlot(i), "getSuggestedPlmnList");
        int i5 = AnonymousClass1.$SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.checkLocationPermission(this.mApp, new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid()).setMethod("getSuggestedPlmnList").setMinSdkVersionForFine(29).setMinSdkVersionForCoarse(29).setMinSdkVersionForEnforcement(29).build()).ordinal()];
        if (i5 == 1) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Not allowed to access scan results -- location");
            return null;
        }
        if (i5 == 2) {
            return null;
        }
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("getSuggestedPlmnList, phone == null, phoneId=" + i);
            return null;
        }
        return (String[]) sendRequest(78, new SuggestedPlmnListRequest(this, i2, i3, i4), phone, null);
    }

    /* JADX INFO: renamed from: com.mediatek.phone.MtkPhoneInterfaceManagerEx$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult;

        static {
            int[] iArr = new int[LocationAccessPolicy.LocationPermissionResult.values().length];
            $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult = iArr;
            try {
                iArr[LocationAccessPolicy.LocationPermissionResult.DENIED_HARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.LocationPermissionResult.DENIED_SOFT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public boolean isEmergencyNumber(int i, String str) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                if (phone.getEmergencyNumberTracker() != null) {
                    return phone.getEmergencyNumberTracker().isEmergencyNumber(str);
                }
                log("isEmergencyNumber return false due to tracker is null");
                return false;
            }
            log("isEmergencyNumber return false due to phone is null");
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getDsdaState() {
        MtkDataHelper mtkDataHelper = MtkDataHelper.getInstance();
        if (mtkDataHelper != null) {
            return mtkDataHelper.getDsdaMode();
        }
        return 0;
    }

    public boolean getCallSubAddressEnabled(int i) {
        boolean callSubAddressEnabled;
        enforceReadPrivilegedPermission("getCallSubAddressEnabled");
        if (PhoneFactory.getPhone(i) == null) {
            return false;
        }
        synchronized (this.mCallSubAddressLock) {
            CallSubAddressThread callSubAddressThread = new CallSubAddressThread(i);
            this.mCallSubAddressThread = callSubAddressThread;
            callSubAddressThread.start();
            callSubAddressEnabled = this.mCallSubAddressThread.getCallSubAddressEnabled();
        }
        return callSubAddressEnabled;
    }

    public void setCallSubAddressEnabled(int i, boolean z) {
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone != null) {
            phone.setCallSubAddress(z, (Message) null);
            return;
        }
        loge("setCallSubAddressEnabled, phone null (phoneId=" + i + ")");
    }

    public boolean isInEmergencyCall() {
        TelephonyConnectionService telephonyConnectionService;
        enforcePrivilegedPhoneStatePermission();
        TelecomAccountRegistry telecomAccountRegistry = TelecomAccountRegistry.getInstance(null);
        if (telecomAccountRegistry == null || (telephonyConnectionService = telecomAccountRegistry.getTelephonyConnectionService()) == null) {
            return false;
        }
        return telephonyConnectionService.isInEmergencyCall();
    }

    private void enforceReadPrivilegedPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", str);
    }

    public List getPreferredOperatorList(int i) {
        enforcePrivilegedPhoneStatePermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("getPreferredOperatorList, phone == null, phoneId=" + i);
            return null;
        }
        Object objSendRequest = sendRequest(1004, null, phone, null);
        if (objSendRequest instanceof CommandException) {
            log("getPreferredOperatorList, Exception = " + objSendRequest + " for phoneId=" + i);
            return null;
        }
        ArrayList arrayList = new ArrayList();
        List list = (List) objSendRequest;
        if (list != null) {
            arrayList.addAll(list);
        }
        return arrayList;
    }

    public boolean setPreferredOperator(int i, PreferredOperatorInfo preferredOperatorInfo) {
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            log("setPreferredOperator, phone == null, phoneId=" + i);
            return false;
        }
        return ((Boolean) sendRequest(1006, preferredOperatorInfo, phone, null)).booleanValue();
    }

    public boolean supportSimTypeSwitch() {
        if (UiccController.getInstance() instanceof MtkUiccController) {
            return UiccController.getInstance().supportSimTypeSwitch();
        }
        log("supportSimTypeSwitch, SIM add-on is not enabled");
        return false;
    }

    public boolean canSwitchSimType(int i) {
        if (i < 0 || i >= TelephonyManager.getDefault().getSimCount()) {
            log("canSwitchSimType error with invalid slotId:" + i);
            return false;
        }
        if (UiccController.getInstance() instanceof MtkUiccController) {
            return UiccController.getInstance().canSwitchSimType(i);
        }
        log("canSwitchSimType, SIM add-on is not enabled");
        return false;
    }

    public int getCurrentSimType(int i) {
        if (i < 0 || i >= TelephonyManager.getDefault().getSimCount()) {
            log("getCurrentSimType error with invalid slotId:" + i);
            return -1;
        }
        if (UiccController.getInstance() instanceof MtkUiccController) {
            return UiccController.getInstance().getCurrentSimType(i);
        }
        log("getCurrentSimType, SIM add-on is not enabled");
        return -1;
    }

    public int switchSimType(int i, int i2) {
        if (i < 0 || i >= TelephonyManager.getDefault().getSimCount()) {
            log("switchSimType error with invalid slotId:" + i);
            return -1;
        }
        enforceModifyPermissionOrCarrierPrivilege(getSubIdBySlot(i));
        if (!canSwitchSimType(i)) {
            return -1;
        }
        synchronized (this.mSimPowerLock) {
            try {
                if (getSimTypeExecutingStatusAll() == i2) {
                    if (1 == i2) {
                        return 11;
                    }
                    if (i2 == 0) {
                        return 12;
                    }
                } else if (getSimTypeExecutingStatusAll() == -1 && getCurrentSimType(i) == i2) {
                    if (1 == i2) {
                        return 13;
                    }
                    if (i2 == 0) {
                        return 14;
                    }
                }
                int iIntValue = ((Integer) sendRequest(MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET, new Integer(i2), new Integer(i))).intValue();
                log("switchSimType response = " + iIntValue);
                return iIntValue;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getSimTypeExecutingStatus(int i) {
        if (i < 0 || i >= TelephonyManager.getDefault().getSimCount()) {
            log("getSimTypeExecutingStatus error with invalid slotId:" + i);
            return -1;
        }
        if (UiccController.getInstance() instanceof MtkUiccController) {
            return UiccController.getInstance().getSimTypeExecutingStatus(i);
        }
        log("getSimTypeExecutingStatus, SIM add-on is not enabled");
        return -1;
    }

    public int getSimTypeExecutingStatusAll() {
        if (UiccController.getInstance() instanceof MtkUiccController) {
            return UiccController.getInstance().getSimTypeExecutingStatus();
        }
        log("getSimTypeExecutingStatus, SIM add-on is not enabled");
        return -1;
    }

    public boolean setPreferredPhysicalSim(int i) {
        enforceModifyPermission();
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(0);
        if (phone == null) {
            log("setPreferredPhysicalSim, phone == null");
            return false;
        }
        return ((Boolean) sendRequest(1008, Integer.valueOf(i), phone, null)).booleanValue();
    }
}
