package com.mediatek.services.telephony;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.phone.PhoneUtils;
import com.android.services.telephony.Log;
import com.android.services.telephony.TelephonyConnection;
import com.android.services.telephony.TelephonyConnectionService;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.ratconfiguration.RatConfiguration;
import com.mediatek.phone.MtkSimErrorDialog;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.ot.pubsub.c.d$$ExternalSyntheticBUOutline0;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class MtkTelephonyConnectionServiceUtil {
    private static final MtkTelephonyConnectionServiceUtil INSTANCE = new MtkTelephonyConnectionServiceUtil();
    private static final boolean MTK_CT_VOLTE_SUPPORT = "1".equals(SystemProperties.get("persist.vendor.mtk_ct_volte_support", "0"));
    private CellConnMgr mCellConnMgr;
    private int mCellConnMgrCurrentRun;
    private int mCellConnMgrState;
    private ArrayList mCellConnMgrStringArray;
    private int mCellConnMgrTargetRun;
    private int mCurrentDialSlotId;
    private int mCurrentDialSubId;
    private final BroadcastReceiver mCellConnMgrReceiver = new TcsBroadcastReceiver();
    private List mNeedDataWhenUSSDList = Arrays.asList("37002");
    private TelephonyConnectionService mService = null;
    private Context mContext = null;
    private MtkSuppMessageManager mSuppMessageManager = null;
    private MtkSimErrorDialog mSimErrorDialog = null;
    private ConcurrentHashMap mSuppMsgPhoneConnections = new ConcurrentHashMap();

    MtkTelephonyConnectionServiceUtil() {
    }

    public static MtkTelephonyConnectionServiceUtil getInstance() {
        return INSTANCE;
    }

    public void setService(TelephonyConnectionService telephonyConnectionService) {
        this.mService = telephonyConnectionService;
        this.mContext = telephonyConnectionService.getApplicationContext();
        enableSuppMessage(telephonyConnectionService);
    }

    public void unsetService() {
        this.mService = null;
        disableSuppMessage();
    }

    private void enableSuppMessage(TelephonyConnectionService telephonyConnectionService) {
        Log.d(this, "enableSuppMessage for " + telephonyConnectionService, new Object[0]);
        if (this.mSuppMessageManager == null) {
            MtkSuppMessageManager mtkSuppMessageManager = new MtkSuppMessageManager(telephonyConnectionService);
            this.mSuppMessageManager = mtkSuppMessageManager;
            mtkSuppMessageManager.registerSuppMessageForPhones();
        }
    }

    private void disableSuppMessage() {
        Log.d(this, "disableSuppMessage", new Object[0]);
        MtkSuppMessageManager mtkSuppMessageManager = this.mSuppMessageManager;
        if (mtkSuppMessageManager != null) {
            mtkSuppMessageManager.unregisterSuppMessageForPhones();
            this.mSuppMessageManager = null;
        }
    }

    public void forceSuppMessageUpdate(TelephonyConnection telephonyConnection) {
        Phone phone;
        if (this.mSuppMessageManager == null || (phone = telephonyConnection.getPhone()) == null) {
            return;
        }
        Log.d(this, "forceSuppMessageUpdate for " + telephonyConnection + ", " + phone + " phone " + phone.getPhoneId(), new Object[0]);
        this.mSuppMessageManager.forceSuppMessageUpdate(telephonyConnection, phone);
    }

    public void registerSuppMessageForImsPhone(Phone phone, Connection connection) {
        if (this.mSuppMessageManager == null) {
            return;
        }
        if (!this.mSuppMsgPhoneConnections.containsKey(phone)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(connection);
            this.mSuppMsgPhoneConnections.put(phone, arrayList);
            this.mSuppMessageManager.registerSuppMessageForPhone(phone);
            return;
        }
        ArrayList arrayList2 = (ArrayList) this.mSuppMsgPhoneConnections.get(phone);
        if (arrayList2.size() == 0) {
            Log.d(this, "registerSuppMessageForImsPhone: error, empty connection list", new Object[0]);
        } else {
            arrayList2.add(connection);
            Log.d(this, "registerSuppMessageForImsPhone: phone registered, add connection to list", new Object[0]);
        }
    }

    public void unregisterSuppMessageForImsPhone(Phone phone, Connection connection) {
        if (!this.mSuppMsgPhoneConnections.containsKey(phone)) {
            Log.d(this, "unregisterSuppMessageForImsPhone: error, phone not registered yet", new Object[0]);
            return;
        }
        ArrayList arrayList = (ArrayList) this.mSuppMsgPhoneConnections.get(phone);
        if (arrayList.isEmpty()) {
            Log.d(this, "unregisterSuppMessageForImsPhone: error, empty list", new Object[0]);
            return;
        }
        if (!arrayList.contains(connection)) {
            Log.d(this, "unregisterSuppMessageForImsPhone: error, Connection not in list", new Object[0]);
            return;
        }
        arrayList.remove(connection);
        if (arrayList.isEmpty()) {
            this.mSuppMsgPhoneConnections.remove(phone);
            MtkSuppMessageManager mtkSuppMessageManager = this.mSuppMessageManager;
            if (mtkSuppMessageManager != null) {
                mtkSuppMessageManager.unregisterSuppMessageForPhone(phone);
            }
        }
    }

    class TcsBroadcastReceiver extends BroadcastReceiver {
        private TcsBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (isInitialStickyBroadcast()) {
                Log.d(this, "Skip initial sticky broadcast", new Object[0]);
                return;
            }
            String action = intent.getAction();
            action.hashCode();
            if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                Log.d(this, "MtkSimErrorDialog finish due to ACTION_AIRPLANE_MODE_CHANGED", new Object[0]);
                MtkTelephonyConnectionServiceUtil.this.mSimErrorDialog.dismiss();
                return;
            }
            if (action.equals("android.telephony.action.SIM_CARD_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                int intExtra2 = intent.getIntExtra("slot", -1);
                Log.d(this, "slotId: " + intExtra2 + " simState: " + intExtra, new Object[0]);
                if (intExtra2 != -1 && intExtra2 == MtkTelephonyConnectionServiceUtil.this.mCurrentDialSlotId && intExtra == 1) {
                    Log.d(this, "MtkSimErrorDialog finish due hot plug out of SIM " + (intExtra2 + 1), new Object[0]);
                    MtkTelephonyConnectionServiceUtil.this.mSimErrorDialog.dismiss();
                }
            }
        }
    }

    private void cellConnMgrRegisterForSubEvent() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        this.mContext.registerReceiver(this.mCellConnMgrReceiver, intentFilter);
        int i = this.mCurrentDialSlotId;
        if (i != -1) {
            int simCardState = MtkTelephonyManagerEx.getDefault().getSimCardState(i);
            Log.d(this, "slotId: " + i + " simState: " + simCardState, new Object[0]);
            if (simCardState == 1) {
                Log.d(this, "MtkSimErrorDialog finish due hot plug out of SIM " + (i + 1), new Object[0]);
                this.mSimErrorDialog.dismiss();
            }
        }
    }

    private void cellConnMgrUnregisterForSubEvent() {
        this.mContext.unregisterReceiver(this.mCellConnMgrReceiver);
    }

    public void cellConnMgrSetSimErrorDialogActivity(MtkSimErrorDialog mtkSimErrorDialog) {
        if (this.mContext == null) {
            Log.d(this, "cellConnMgrSetSimErrorDialogActivity, mContext is null", new Object[0]);
            return;
        }
        if (this.mSimErrorDialog == mtkSimErrorDialog) {
            Log.d(this, "cellConnMgrSetSimErrorDialogActivity, skip duplicate", new Object[0]);
            return;
        }
        this.mSimErrorDialog = mtkSimErrorDialog;
        if (mtkSimErrorDialog != null) {
            cellConnMgrRegisterForSubEvent();
            Log.d(this, "cellConnMgrRegisterForSubEvent for setSimErrorDialogActivity", new Object[0]);
        } else {
            cellConnMgrUnregisterForSubEvent();
            Log.d(this, "cellConnMgrUnregisterForSubEvent for setSimErrorDialogActivity", new Object[0]);
        }
    }

    public boolean cellConnMgrShowAlerting(int i) {
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            return false;
        }
        if (this.mContext == null) {
            Log.d(this, "cellConnMgrShowAlerting, mContext is null", new Object[0]);
            return false;
        }
        if (MtkTelephonyManagerEx.getDefault().isWifiCallingEnabled(i)) {
            Log.d(this, "cellConnMgrShowAlerting: WiFi calling is enabled, return directly.", new Object[0]);
            return false;
        }
        this.mCellConnMgr = new CellConnMgr(this.mContext);
        this.mCurrentDialSubId = i;
        this.mCurrentDialSlotId = SubscriptionManagerService.getInstance().getSlotIndex(i);
        int currentState = this.mCellConnMgr.getCurrentState(this.mCurrentDialSubId, 7);
        this.mCellConnMgrState = currentState;
        if (currentState != 0) {
            ArrayList stringUsingState = this.mCellConnMgr.getStringUsingState(this.mCurrentDialSubId, currentState);
            this.mCellConnMgrStringArray = stringUsingState;
            this.mCellConnMgrCurrentRun = 0;
            this.mCellConnMgrTargetRun = stringUsingState.size() / 4;
            Log.d(this, "cellConnMgrShowAlerting, slotId: " + this.mCurrentDialSlotId + " state: " + this.mCellConnMgrState + " size: " + this.mCellConnMgrStringArray.size(), new Object[0]);
            if (this.mCellConnMgrTargetRun > 0) {
                cellConnMgrShowAlertingInternal();
                return true;
            }
        }
        return false;
    }

    public void cellConnMgrHandleEvent() {
        this.mCellConnMgr.handleRequest(this.mCurrentDialSubId, this.mCellConnMgrState);
        int i = this.mCellConnMgrCurrentRun + 1;
        this.mCellConnMgrCurrentRun = i;
        if (i != this.mCellConnMgrTargetRun) {
            cellConnMgrShowAlertingInternal();
        } else {
            cellConnMgrShowAlertingFinalize();
        }
    }

    private void cellConnMgrShowAlertingInternal() {
        ArrayList arrayList = new ArrayList();
        arrayList.add((String) this.mCellConnMgrStringArray.get(this.mCellConnMgrCurrentRun * 4));
        arrayList.add((String) this.mCellConnMgrStringArray.get((this.mCellConnMgrCurrentRun * 4) + 1));
        arrayList.add((String) this.mCellConnMgrStringArray.get((this.mCellConnMgrCurrentRun * 4) + 2));
        arrayList.add((String) this.mCellConnMgrStringArray.get((this.mCellConnMgrCurrentRun * 4) + 3));
        for (int i = 0; i < arrayList.size(); i++) {
            Log.d(this, "cellConnMgrShowAlertingInternal, string(" + i + ")=" + ((String) arrayList.get(i)), new Object[0]);
        }
        Log.d(this, "cellConnMgrShowAlertingInternal", new Object[0]);
        if (arrayList.size() < 4) {
            Log.d(this, "cellConnMgrShowAlertingInternal, stringArray is illegle, do nothing.", new Object[0]);
            return;
        }
        if (this.mSimErrorDialog != null) {
            Log.w(this, "cellConnMgrShowAlertingInternal, There's an existing error dialog: " + this.mSimErrorDialog + ", ignore displaying the new error.", new Object[0]);
            return;
        }
        this.mSimErrorDialog = new MtkSimErrorDialog(this.mContext, arrayList);
        Log.d(this, "cellConnMgrShowAlertingInternal, show SimErrorDialog: " + this.mSimErrorDialog, new Object[0]);
        this.mSimErrorDialog.show();
    }

    public void cellConnMgrShowAlertingFinalize() {
        Log.d(this, "cellConnMgrShowAlertingFinalize", new Object[0]);
        this.mCellConnMgrCurrentRun = -1;
        this.mCellConnMgrTargetRun = 0;
        this.mCurrentDialSubId = -1;
        this.mCurrentDialSlotId = -1;
        this.mCellConnMgrState = -1;
        this.mCellConnMgr = null;
    }

    public boolean isCellConnMgrAlive() {
        return this.mCellConnMgr != null;
    }

    class CellConnMgr {
        private Context mContext;

        public CellConnMgr(Context context) {
            this.mContext = context;
            if (context != null) {
                return;
            }
            d$$ExternalSyntheticBUOutline0.m("CellConnMgr must be created by indicated context");
            throw null;
        }

        public int getCurrentState(int i, int i2) {
            int i3;
            int i4 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", -1);
            boolean z = !isRadioOn(i) && isRadioOffBySimManagement(i);
            int slotIndex = SubscriptionManager.getSlotIndex(i);
            TelephonyManager telephonyManager = TelephonyManager.getDefault();
            Log.d(this, "[getCurrentState]subId: " + i + ", requestType:" + i2 + "; (flight mode, radio off, locked, roaming) = (" + i4 + "," + z + "," + (2 == telephonyManager.getSimState(slotIndex) || 3 == telephonyManager.getSimState(slotIndex) || 4 == telephonyManager.getSimState(slotIndex)) + ",false)", new Object[0]);
            if (i2 == 1) {
                i3 = i4 != 1 ? 0 : 1;
            } else if (i2 != 2) {
                i3 = (i4 != 1 ? 0 : 1) | (z ? 2 : 0);
            } else {
                i3 = z ? 2 : 0;
            }
            if (i3 == 0 && (i2 & 4) == 4) {
                i3 = isImsUnavailableForCTVolte(i) ? 4 : 0;
            }
            Log.d(this, "[getCurrentState] state:" + i3, new Object[0]);
            return i3;
        }

        public ArrayList getStringUsingState(int i, int i2) {
            ArrayList arrayList = new ArrayList();
            Log.d(this, "[getStringUsingState] subId: " + i + ", state:" + i2, new Object[0]);
            if ((i2 & 3) == 3) {
                arrayList.add(Resources.getSystem().getString(R.string.ext_media_checking_notification_message));
                arrayList.add(Resources.getSystem().getString(R.string.ext_media_browse_action));
                arrayList.add(Resources.getSystem().getString(R.string.expand_button_content_description_expanded));
                arrayList.add(Resources.getSystem().getString(R.string.expand_button_content_description_collapsed));
                Log.d(this, "[getStringUsingState] STATE_FLIGHT_MODE + STATE_RADIO_OFF", new Object[0]);
            } else if ((i2 & 1) == 1) {
                arrayList.add(Resources.getSystem().getString(R.string.ext_media_checking_notification_title));
                arrayList.add(Resources.getSystem().getString(R.string.ext_media_badremoval_notification_title));
                arrayList.add(Resources.getSystem().getString(R.string.expires_on));
                arrayList.add(Resources.getSystem().getString(R.string.expand_button_content_description_collapsed));
                Log.d(this, "[getStringUsingState] STATE_FLIGHT_MODE", new Object[0]);
            } else if ((i2 & 2) == 2) {
                arrayList.add(Resources.getSystem().getString(R.string.ext_media_missing_title));
                arrayList.add(Resources.getSystem().getString(R.string.ext_media_missing_message));
                arrayList.add(Resources.getSystem().getString(R.string.ext_media_badremoval_notification_message));
                arrayList.add(Resources.getSystem().getString(R.string.expand_button_content_description_collapsed));
                Log.d(this, "[getStringUsingState] STATE_RADIO_OFF", new Object[0]);
            } else if ((i2 & 4) == 4) {
                arrayList.add(Resources.getSystem().getString(R.string.ext_media_init_action));
                arrayList.add(this.mContext.getApplicationContext().getString(com.android.phone.R.string.alert_volte_no_service, PhoneUtils.getSubDisplayName(i)));
                arrayList.add(Resources.getSystem().getString(R.string.expand_button_content_description_expanded));
                arrayList.add(Resources.getSystem().getString(R.string.expand_button_content_description_collapsed));
                Log.d(this, "[getStringUsingState] STATE_NOIMSREG_FOR_CTVOLTE", new Object[0]);
            }
            Log.d(this, "[getStringUsingState]stringList size: " + arrayList.size(), new Object[0]);
            return (ArrayList) arrayList.clone();
        }

        public void handleRequest(int i, int i2) {
            Log.d(this, "[handleRequest] subId: " + i + ", state:" + i2, new Object[0]);
            if ((i2 & 1) == 1) {
                Settings.Global.putInt(this.mContext.getContentResolver(), "airplane_mode_on", 0);
                this.mContext.sendBroadcastAsUser(new Intent("android.intent.action.AIRPLANE_MODE").putExtra(MEModuleSignalT1s.EXTRA_STATE, false), UserHandle.ALL);
                Log.d(this, "[handleRequest] Turn off flight mode.", new Object[0]);
            }
            if ((i2 & 2) == 2) {
                int i3 = 0;
                for (int i4 = 0; i4 < TelephonyManager.getDefault().getSimCount(); i4++) {
                    int[] subId = SubscriptionManager.getSubId(i4);
                    if ((subId != null && isRadioOn(subId[0])) || i4 == SubscriptionManager.getSlotIndex(i)) {
                        i3 |= 1 << i4;
                    }
                }
                Intent intent = new Intent("com.mediatek.internal.telephony.RadioManager.intent.action.FORCE_SET_RADIO_POWER");
                intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
                intent.putExtra("mode", i3);
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
                Log.d(this, "[handleRequest] Turn radio on, MSIM mode:" + i3, new Object[0]);
            }
            if ((i2 & 4) == 4) {
                ImsManager.getInstance(this.mContext, SubscriptionManager.getPhoneId(i)).setEnhanced4gLteModeSetting(false);
                Log.d(this, "[handleRequest] Turn off ct volte", new Object[0]);
            }
        }

        private boolean isRadioOffBySimManagement(int i) {
            boolean zIsRadioOffBySimManagement;
            try {
                IMtkTelephonyEx iMtkTelephonyExAsInterface = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
                if (iMtkTelephonyExAsInterface == null) {
                    Log.d(this, "[isRadioOffBySimManagement] iTelEx is null", new Object[0]);
                    return false;
                }
                zIsRadioOffBySimManagement = iMtkTelephonyExAsInterface.isRadioOffBySimManagement(i);
                Log.d(this, "[isRadioOffBySimManagement]  subId " + i + ", result = " + zIsRadioOffBySimManagement, new Object[0]);
                return zIsRadioOffBySimManagement;
            } catch (RemoteException e) {
                e.printStackTrace();
                zIsRadioOffBySimManagement = true;
            }
        }

        private boolean isRadioOn(int i) {
            boolean zIsRadioOnForSubscriber;
            Log.d(this, "isRadioOff verify subId " + i, new Object[0]);
            try {
                ITelephony iTelephonyAsInterface = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
                if (iTelephonyAsInterface == null) {
                    Log.d(this, "isRadioOff iTel is null", new Object[0]);
                    return false;
                }
                zIsRadioOnForSubscriber = iTelephonyAsInterface.isRadioOnForSubscriber(i, this.mContext.getOpPackageName());
                Log.d(this, "isRadioOff subId " + i + " radio on? " + zIsRadioOnForSubscriber, new Object[0]);
                return zIsRadioOnForSubscriber;
            } catch (RemoteException e) {
                e.printStackTrace();
                zIsRadioOnForSubscriber = true;
            }
        }

        private int getNetworkType(int i) {
            int dataNetworkType = TelephonyManager.getDefault().getDataNetworkType(i);
            int voiceNetworkType = TelephonyManager.getDefault().getVoiceNetworkType(i);
            Log.d(this, "updateNetworkType(), dataNetworkType = " + dataNetworkType + ", voiceNetworkType = " + voiceNetworkType, new Object[0]);
            if (dataNetworkType != 0) {
                return dataNetworkType;
            }
            if (voiceNetworkType != 0) {
                return voiceNetworkType;
            }
            return 0;
        }

        private boolean isInEcbmMode(int i) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                Log.d(this, "[isInEcbmMode] phoneId = " + i + ", ecbmString = " + phone.isInEcm(), new Object[0]);
                return phone.isInEcm();
            }
            Log.w(this, "[isInEcbmMode] phoneId = " + i + ", phone is null", new Object[0]);
            return false;
        }

        private boolean isMainPhoneId(int i) {
            return SystemProperties.getInt("persist.vendor.radio.simswitch", 1) - 1 == i;
        }

        private boolean isDualVoLTESupport() {
            return SystemProperties.getInt("persist.vendor.mims_support", 1) != 1;
        }

        private boolean isDualCTCard() {
            for (int i = 0; i < TelephonyManager.getDefault().getSimCount(); i++) {
                int[] subId = SubscriptionManager.getSubId(i);
                if (subId == null || !isCTCard(subId[0])) {
                    return false;
                }
            }
            return true;
        }

        private boolean isDataOn(int i) {
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            if (!SubscriptionManager.isValidSubscriptionId(defaultDataSubscriptionId) || defaultDataSubscriptionId != i) {
                return false;
            }
            Log.d(this, "[isDataOn] default data is on it", new Object[0]);
            return true;
        }

        private boolean isLteNetworkTypeEnabled(int i) {
            boolean z;
            int i2 = Settings.Global.getInt(this.mContext.getContentResolver(), "preferred_network_mode" + i, Phone.PREFERRED_NT_MODE);
            switch (i2) {
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 15:
                case 17:
                case 19:
                case 20:
                case 22:
                case 24:
                case 25:
                case 26:
                case 27:
                case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
                case 29:
                case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
                case 31:
                case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
                case 33:
                    z = true;
                    break;
                case 13:
                case 14:
                case 16:
                case 18:
                case 21:
                case 23:
                default:
                    z = false;
                    break;
            }
            Log.d(this, "isLteNetworkTypeEnabled, enabled=" + z + ", networkType=" + i2, new Object[0]);
            return z;
        }

        private boolean isImsUnavailableForCTVolte(int i) {
            boolean zIsLteNetworkTypeEnabled;
            if (MtkTelephonyConnectionServiceUtil.MTK_CT_VOLTE_SUPPORT && RatConfiguration.isC2kSupported() && !RatConfiguration.isNrSupported()) {
                int phoneId = SubscriptionManager.getPhoneId(i);
                if ("OP09".equals(SystemProperties.get("persist.vendor.operator.optr"))) {
                    zIsLteNetworkTypeEnabled = true;
                } else {
                    zIsLteNetworkTypeEnabled = isLteNetworkTypeEnabled(i);
                    Log.d(this, "[isImsUnavailableForCTVolte] enable 4g = " + zIsLteNetworkTypeEnabled, new Object[0]);
                }
                if (!isMainPhoneId(phoneId) && isDualCTCard() && !isDataOn(i)) {
                    Log.d(this, "isImsUnavailableForCTVolte, dual ct case.", new Object[0]);
                    return false;
                }
                if (zIsLteNetworkTypeEnabled && ((isMainPhoneId(phoneId) || isDualVoLTESupport()) && isCTCard(i) && !TelephonyManager.getDefault().isNetworkRoaming(i) && ImsManager.getInstance(this.mContext, phoneId).isEnhanced4gLteModeSettingEnabledByUser() && ((isInEcbmMode(phoneId) || getNetworkType(i) == 13 || getNetworkType(i) == 19 || getNetworkType(i) == 0) && !isImsReg(i)))) {
                    Log.d(this, "isImsUnavailableForCTVolte ture", new Object[0]);
                    return true;
                }
            }
            return false;
        }

        private boolean isImsReg(int i) {
            boolean zIsImsRegistered = MtkTelephonyManagerEx.getDefault().isImsRegistered(i);
            Log.d(this, "[isImsReg] isImsReg = " + zIsImsRegistered, new Object[0]);
            return zIsImsRegistered;
        }

        private boolean isCTCard(int i) {
            String simSerialNumber = ((TelephonyManager) this.mContext.getSystemService("phone")).getSimSerialNumber(i);
            if (TextUtils.isEmpty(simSerialNumber)) {
                Log.d(this, "[isCTCard] iccid is empty, , subId = " + i, new Object[0]);
                return false;
            }
            if (!simSerialNumber.startsWith("898603") && !simSerialNumber.startsWith("898611") && !simSerialNumber.startsWith("8985302") && !simSerialNumber.startsWith("8985307")) {
                return false;
            }
            Log.d(this, "[isCTCard] iccid matches, subId = " + i, new Object[0]);
            return true;
        }
    }

    public static boolean isTestSim(int i) {
        if (i == 0) {
            return "1".equals(SystemProperties.get("vendor.gsm.sim.ril.testsim", "0"));
        }
        if (i == 1) {
            return "1".equals(SystemProperties.get("vendor.gsm.sim.ril.testsim.2", "0"));
        }
        if (i == 2) {
            return "1".equals(SystemProperties.get("vendor.gsm.sim.ril.testsim.3", "0"));
        }
        if (i != 3) {
            return false;
        }
        return "1".equals(SystemProperties.get("vendor.gsm.sim.ril.testsim.4", "0"));
    }

    public boolean shouldDisableAirplaneModeWhenUSSD(String str, Phone phone) {
        if (PhoneNumberUtils.isUriNumber(str)) {
            return false;
        }
        String strExtractNetworkPortionAlt = PhoneNumberUtils.extractNetworkPortionAlt(PhoneNumberUtils.stripSeparators(str));
        if ((!strExtractNetworkPortionAlt.startsWith("*") && !strExtractNetworkPortionAlt.startsWith("#")) || !strExtractNetworkPortionAlt.endsWith("#")) {
            return false;
        }
        int subId = phone.getSubId();
        String simOperator = TelephonyManager.getDefault().getSimOperator(subId);
        Log.d(this, "operatorNumeric = " + simOperator, new Object[0]);
        if (simOperator != null && this.mNeedDataWhenUSSDList.contains(simOperator)) {
            Log.d(this, "Operator doesn't support USSD in airplane mode", new Object[0]);
            Context context = this.mContext;
            if (context == null) {
                Log.d(this, "mContext is null", new Object[0]);
                return false;
            }
            this.mCellConnMgr = new CellConnMgr(context);
            this.mCurrentDialSubId = subId;
            this.mCurrentDialSlotId = SubscriptionManagerService.getInstance().getSlotIndex(subId);
            int currentState = this.mCellConnMgr.getCurrentState(this.mCurrentDialSubId, 7);
            this.mCellConnMgrState = currentState;
            if (currentState != 0) {
                ArrayList stringUsingState = this.mCellConnMgr.getStringUsingState(this.mCurrentDialSubId, currentState);
                this.mCellConnMgrStringArray = stringUsingState;
                this.mCellConnMgrCurrentRun = 0;
                this.mCellConnMgrTargetRun = stringUsingState.size() / 4;
                Log.d(this, "shouldDisableAirplaneModeWhenUSSD, slotId: " + this.mCurrentDialSlotId + " state: " + this.mCellConnMgrState + " size: " + this.mCellConnMgrStringArray.size(), new Object[0]);
                if (this.mCellConnMgrTargetRun > 0) {
                    cellConnMgrShowAlertingInternal();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean allowEmergencyCallIfOos(Phone phone) {
        CarrierConfigManager carrierConfigManager;
        PersistableBundle configForSubId;
        if (phone == null || (carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config")) == null || (configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId())) == null) {
            return false;
        }
        boolean z = configForSubId.getBoolean("mtk_allow_emergency_call_if_oos");
        Log.i(this, "allow_emergency_call_when_oos: " + z, new Object[0]);
        return z;
    }
}
