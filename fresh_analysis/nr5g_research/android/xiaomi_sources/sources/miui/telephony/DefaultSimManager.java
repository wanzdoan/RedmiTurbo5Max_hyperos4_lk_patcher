package miui.telephony;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.IccCardActivateHelper;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccSlot;
import com.miui.enterprise.PhoneHelper;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.modem.ModemUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.util.Network;
import vendor.xiaomi.hardware.radio.RadioError;

/* JADX INFO: loaded from: classes.dex */
public class DefaultSimManager implements SubscriptionManager.OnSubscriptionsChangedListener, VirtualSimUtils.VirtualSimListenner {
    private static final int EVENT_DELAYED_MODEM_RESET = 7;
    private static final int EVENT_ICC_CHANGED = 3;
    private static final int EVENT_IMSI_READY = 4;
    private static final int EVENT_INIT_DEFAULT_SLOT = 0;
    private static final int EVENT_MODEM_RESET = 6;
    private static final int EVENT_PRECISE_CALL_STATE_CHANGED = 5;
    private static final int EVENT_SYNC_DEFAULT_DATA_SLOT = 1;
    private static final int EVENT_SYNC_DEFAULT_VOICE_SLOT = 2;
    public static final int EVENT_VOLTE_TEMP_DDS = 130;
    public static final String KEY_IS_USE_PREFRRED = "key_is_use_preferred";
    public static final String KEY_LAST_ICCID = "key_last_iccid";
    public static final String KEY_PREFERRED_DATA_SLOT = "key_preferred_data_slot";
    private static final String LAST_ICC_ID_PROPERTY = "persist.radio.iccid";
    private static final String[] PROPERTY_RIL_CDMA_CARD_TYPE;
    private static final boolean RELEASE = false;
    private static final boolean REQUEST = true;
    public static final String TAG_SIM_ABSENT = "simAbsent";
    public static final String VICE_SLOT_VOLTE_DATA_ENABLED = "vice_slot_volte_data_enabled";
    private static final int WAITING_DS_ACTIVE_MAX_CNT = 8;
    private static final int WAITING_DS_ACTIVE_TIME = 500;
    private static final int WAITING_MODEM_RESET_TIME = 60000;
    private static final int WAITING_PSIM_ESIM_SWAP_MAX_CNT = 5;
    private static final int WAITING_SIM_TIME;
    private static final int WAITING_SIM_TIME_PSIM_ESIM_SWAP = 3000;
    private static boolean mIsModemResetting;
    private static DefaultSimManager sInstance;
    private IccRecords[] m3gpp2IccRecords;
    private IccRecords[] m3gppIccRecords;
    private DefaultSlotSelector mDefaultSlotSelector;
    private String mInitModes;
    private boolean mIsUsePreferred;
    private List<SubscriptionInfo> mLastSubInfos;
    private int mOldDefaultDataSlotId;
    private int mOldDefaultDataSubId;
    private int mPreferredDataSlot;
    private int[] mSimApplicationNum;
    private int[] mSimInsertStates;
    private boolean[] mSimInsertStatesDone;
    private final IccCardStatus.CardState[] mSimStatus;
    private final SubscriptionManager mSm;
    private static String LOG_TAG = "DefaultSimManager";
    public static final boolean IS_CUSTOMIZED_FOR_CM = ModemUtils.PROP_CERT_TYPE_CM.equals(Network.getOperatorType());
    private boolean mCallStateChangeRegistered = RELEASE;
    private boolean mViceSlotVolteDataEnabled = RELEASE;
    private List<DataSlotListener> mDataSlotListener = new ArrayList(1);
    private List<ImsiListener> mImsiListener = new ArrayList(1);
    private int mPropertyDefautlDataSlotId = SystemProperties.getInt("persist.radio.default.data", SubscriptionManager.INVALID_SLOT_ID);
    private int mPropertyDefautlVoiceSlotId = SystemProperties.getInt("persist.radio.default.voice", SubscriptionManager.INVALID_SLOT_ID);
    private int mWaitingPsimEsimSwapCounts = 0;
    private int mWaitingDsActiveCnt = 0;
    private PhoneConstants.State mCallState = PhoneConstants.State.IDLE;
    private PhoneConstants.State mOldCallState = PhoneConstants.State.IDLE;
    private boolean mNeedRestoreTempDdsSwitch = RELEASE;
    private boolean mDataSlotReady = RELEASE;
    private boolean mIsDisableingVirtualSim = RELEASE;
    private Handler mHandler = new Handler() { // from class: miui.telephony.DefaultSimManager.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            AsyncResult ar = (AsyncResult) msg.obj;
            Rlog.d(DefaultSimManager.LOG_TAG, "handleMessage " + msg.what);
            switch (msg.what) {
                case 0:
                    DefaultSimManager.this.onInitDefaultSlot();
                    break;
                case 1:
                    DefaultSimManager.this.syncDataSubscription(SubscriptionManager.getDefault().getAvailableSubscriptionInfoList());
                    break;
                case 2:
                    DefaultSimManager.this.syncVoiceSubscription(SubscriptionManager.getDefault().getAvailableSubscriptionInfoList());
                    break;
                case 3:
                    if (ar.result != null) {
                        int cardIndex = ((Integer) ar.result).intValue();
                        UiccCard card = UiccController.getInstance().getUiccCard(cardIndex);
                        int lastAppNum = DefaultSimManager.this.mSimApplicationNum[cardIndex];
                        DefaultSimManager.this.updateCard(card, cardIndex);
                        if (lastAppNum != 0 && DefaultSimManager.this.mSimApplicationNum[cardIndex] == 0) {
                            if (PhoneDebug.VDBG) {
                                Rlog.d(DefaultSimManager.LOG_TAG, "EVENT_ICC_CHANGED sim" + cardIndex + " application is not OK");
                            }
                            break;
                        } else if (lastAppNum == 0 && DefaultSimManager.this.mSimApplicationNum[cardIndex] != 0) {
                            if (PhoneDebug.VDBG) {
                                Rlog.d(DefaultSimManager.LOG_TAG, "EVENT_ICC_CHANGED sim" + cardIndex + " application is OK");
                            }
                            DefaultSimManager.this.mHandler.removeMessages(0);
                            DefaultSimManager.this.mLastSubInfos = null;
                            DefaultSimManager.this.onSubscriptionsChanged();
                            break;
                        }
                    } else {
                        if (PhoneDebug.VDBG) {
                            Rlog.e(DefaultSimManager.LOG_TAG, "Error: Invalid card index EVENT_ICC_CHANGED ");
                        }
                        break;
                    }
                    break;
                case 4:
                    if (ar.userObj instanceof Integer) {
                        int slotId = ((Integer) ar.userObj).intValue();
                        if ((DefaultSimManager.this.m3gppIccRecords[slotId] == null || DefaultSimManager.this.m3gppIccRecords[slotId].getIMSI() != null) && (DefaultSimManager.this.m3gpp2IccRecords[slotId] == null || DefaultSimManager.this.m3gpp2IccRecords[slotId].getIMSI() != null)) {
                            DefaultSimManager.this.notifyImsiReady(slotId);
                        }
                    }
                    if (DefaultSimManager.this.needListenImsiReady() && !DefaultSimManager.this.isAllInfoReady(DefaultSimManager.this.mLastSubInfos)) {
                        DefaultSimManager.this.mHandler.removeMessages(0);
                        DefaultSimManager.this.mLastSubInfos = null;
                        DefaultSimManager.this.onSubscriptionsChanged();
                        break;
                    }
                    break;
                case 5:
                    DefaultSimManager.this.onCallStateChanged();
                    break;
                case 6:
                    DefaultSimManager.mIsModemResetting = DefaultSimManager.REQUEST;
                    if (hasMessages(7)) {
                        removeMessages(7);
                    }
                    Rlog.d(DefaultSimManager.LOG_TAG, "Event EVENT_MODEM_RESET Received");
                    sendMessageDelayed(obtainMessage(7), 60000L);
                    for (int i = 0; i < DefaultSimManager.this.mSimInsertStatesDone.length; i++) {
                        DefaultSimManager.this.mSimInsertStatesDone[i] = DefaultSimManager.RELEASE;
                    }
                    break;
                case 7:
                    DefaultSimManager.mIsModemResetting = DefaultSimManager.RELEASE;
                    break;
            }
        }
    };

    public interface DataSlotListener {
        void onDataSlotReady(boolean z);
    }

    public interface ImsiListener {
        void onImsiReady(int i);
    }

    static {
        WAITING_SIM_TIME = (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) ? WAITING_SIM_TIME_PSIM_ESIM_SWAP : 1500;
        mIsModemResetting = RELEASE;
        PROPERTY_RIL_CDMA_CARD_TYPE = new String[]{"vendor.ril.cdma.card.type.1", "vendor.ril.cdma.card.type.2", "vendor.ril.cdma.card.type.3", "vendor.ril.cdma.card.type.4"};
    }

    private DefaultSimManager() {
        Context ctx = MiuiTelephony.getInstance().getContext();
        this.mSm = (SubscriptionManager) ctx.getSystemService("telephony_subscription_service");
        Phone[] phones = PhoneFactory.getPhones();
        this.mSimStatus = new IccCardStatus.CardState[phones.length];
        this.mSimApplicationNum = new int[MiuiTelephony.PHONE_COUNT];
        this.m3gppIccRecords = new IccRecords[MiuiTelephony.PHONE_COUNT];
        this.m3gpp2IccRecords = new IccRecords[MiuiTelephony.PHONE_COUNT];
        this.mSimInsertStatesDone = new boolean[MiuiTelephony.PHONE_COUNT];
        this.mSimInsertStates = new int[MiuiTelephony.PHONE_COUNT];
        int i = 0;
        while (true) {
            if (i >= MiuiTelephony.PHONE_COUNT) {
                break;
            }
            this.mSimStatus[i] = IccCardStatus.CardState.CARDSTATE_ERROR;
            this.mSimApplicationNum[i] = 0;
            this.m3gppIccRecords[i] = null;
            this.m3gpp2IccRecords[i] = null;
            this.mSimInsertStatesDone[i] = RELEASE;
            i++;
        }
        int i2 = this.mPropertyDefautlDataSlotId;
        this.mOldDefaultDataSlotId = i2;
        new TelephonyBroadcastReceiver().register(MiuiTelephony.getInstance().getContext());
        this.mOldDefaultDataSubId = SubscriptionManager.getDefaultDataSubscriptionId();
        UiccController.getInstance().registerForIccChanged(this.mHandler, 3, (Object) null);
        phones[0].mCi.registerForModemReset(this.mHandler, 6, (Object) null);
        SubscriptionManager.getDefault().addOnSubscriptionsChangedListener(this);
        this.mInitModes = Settings.Global.getString(MiuiTelephony.getInstance().getContext().getContentResolver(), "preferred_network_mode");
        VirtualSimUtils.init();
        VirtualSimUtils.getInstance().addVirtualSimChangedListener(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MiuiTelephony.getInstance().getContext());
        this.mIsUsePreferred = sp.getBoolean(KEY_IS_USE_PREFRRED, RELEASE);
        this.mPreferredDataSlot = sp.getInt(KEY_PREFERRED_DATA_SLOT, SubscriptionManager.INVALID_SLOT_ID);
        String iccids = SystemProperties.get(LAST_ICC_ID_PROPERTY, (String) null);
        if (!TextUtils.isEmpty(iccids)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(KEY_LAST_ICCID, iccids);
            editor.apply();
            SystemProperties.set(LAST_ICC_ID_PROPERTY, "");
        }
        initViceSlotVolteDataFeature();
    }

    private void initViceSlotVolteDataFeature() {
        boolean isLplTempSwitch = SystemProperties.getBoolean("persist.vendor.radio.enable_temp_dds", RELEASE);
        if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING || isLplTempSwitch) {
            return;
        }
        this.mViceSlotVolteDataEnabled = Settings.Global.getInt(MiuiTelephony.getInstance().getContext().getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0) != 0 ? REQUEST : false;
        if (this.mViceSlotVolteDataEnabled) {
            registerCallStateChange();
        }
        ContentObserver viceSlotVolteDataObserver = new ContentObserver(null) { // from class: miui.telephony.DefaultSimManager.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                if (uri == null || !DefaultSimManager.VICE_SLOT_VOLTE_DATA_ENABLED.equals(uri.getLastPathSegment())) {
                    return;
                }
                DefaultSimManager defaultSimManager = DefaultSimManager.this;
                ContentResolver contentResolver = MiuiTelephony.getInstance().getContext().getContentResolver();
                boolean z = DefaultSimManager.RELEASE;
                if (Settings.Global.getInt(contentResolver, DefaultSimManager.VICE_SLOT_VOLTE_DATA_ENABLED, 0) != 0) {
                    z = DefaultSimManager.REQUEST;
                }
                defaultSimManager.mViceSlotVolteDataEnabled = z;
                if (DefaultSimManager.this.mViceSlotVolteDataEnabled && !DefaultSimManager.this.mCallStateChangeRegistered) {
                    DefaultSimManager.this.registerCallStateChange();
                } else if (!DefaultSimManager.this.mViceSlotVolteDataEnabled && DefaultSimManager.this.mCallStateChangeRegistered) {
                    DefaultSimManager.this.unRegisterCallStateChange();
                }
                Rlog.d(DefaultSimManager.LOG_TAG, "mViceSlotVolteDataEnabled : " + DefaultSimManager.this.mViceSlotVolteDataEnabled);
            }
        };
        MiuiTelephony.getInstance().getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor(VICE_SLOT_VOLTE_DATA_ENABLED), RELEASE, viceSlotVolteDataObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerCallStateChange() {
        this.mCallStateChangeRegistered = REQUEST;
        CallManager.getInstance(MiuiTelephony.getInstance().getContext()).registerForPreciseCallStateChanged(this.mHandler, 5, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterCallStateChange() {
        this.mCallStateChangeRegistered = RELEASE;
        CallManager.getInstance(MiuiTelephony.getInstance().getContext()).unregisterForPreciseCallStateChanged(this.mHandler);
    }

    static void init() {
        sInstance = new DefaultSimManager();
    }

    public static DefaultSimManager getInstance() {
        return sInstance;
    }

    public void setDefaultSlotSelector(DefaultSlotSelector selector) {
        this.mDefaultSlotSelector = selector;
    }

    public void setDefaultVoiceSlotId(int slotId, String opPackageName) throws RemoteException {
        Rlog.d(LOG_TAG, "<" + opPackageName + "> is trying to set default voice slot to " + slotId);
        if (TelephonyManagerEx.getDefault().isSatelliteEnabled()) {
            Rlog.d(LOG_TAG, " setDefaultVoiceSlotId satellite is enabled, return.");
            return;
        }
        MiuiTelephony.getInstance().getContext().enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", null);
        if (slotId != SubscriptionManager.DEFAULT_SLOT_ID) {
            this.mPropertyDefautlVoiceSlotId = SubscriptionManager.isRealSlotId(slotId) ? slotId : SubscriptionManager.INVALID_SLOT_ID;
            SystemProperties.set("persist.radio.default.voice", String.valueOf(this.mPropertyDefautlVoiceSlotId));
            if (this.mPropertyDefautlVoiceSlotId == SubscriptionManager.INVALID_SLOT_ID) {
                if (SubscriptionManager.isValidSubscriptionId(SubscriptionManager.getDefaultVoiceSubscriptionId())) {
                    this.mSm.setDefaultVoiceSubId(SubscriptionManager.INVALID_SUBSCRIPTION_ID);
                }
            } else {
                int subId = SubscriptionManager.getSubscriptionId(this.mPropertyDefautlVoiceSlotId);
                if (!SubscriptionManager.isValidSubscriptionId(subId)) {
                    subId = getSubIdBySlotId(this.mPropertyDefautlVoiceSlotId);
                }
                if (SubscriptionManager.getDefaultVoiceSubscriptionId() != subId) {
                    this.mSm.setDefaultVoiceSubscriptionId(subId);
                }
            }
            syncPhoneAccount();
        }
    }

    public void syncPhoneAccount() {
        TelecomManager telecomManager = TelecomManager.from(MiuiTelephony.getInstance().getContext());
        Rlog.d(LOG_TAG, "syncPhoneAccount with voice slot = " + this.mPropertyDefautlVoiceSlotId);
        PhoneAccountHandle defaultPhoneAccount = telecomManager.getUserSelectedOutgoingPhoneAccount();
        if (this.mPropertyDefautlVoiceSlotId == SubscriptionManager.INVALID_SLOT_ID) {
            if (defaultPhoneAccount != null && defaultPhoneAccount.getComponentName().getClassName().equals("com.android.services.telephony.TelephonyConnectionService")) {
                telecomManager.setUserSelectedOutgoingPhoneAccount(null);
                Rlog.d(LOG_TAG, "syncPhoneAccount clear default phone account");
                return;
            }
            return;
        }
        PhoneAccountHandle phoneAccountHandle = getPhoneAccountHandle(this.mPropertyDefautlVoiceSlotId, MiuiTelephony.getInstance().getContext());
        if (phoneAccountHandle != null) {
            if (defaultPhoneAccount == null || !defaultPhoneAccount.equals(phoneAccountHandle)) {
                telecomManager.setUserSelectedOutgoingPhoneAccount(phoneAccountHandle);
                Rlog.d(LOG_TAG, "syncPhoneAccount set default phone account " + phoneAccountHandle);
            }
        }
    }

    public static PhoneAccountHandle getPhoneAccountHandle(int slot, Context context) {
        TelecomManager telecomManager = TelecomManager.from(context);
        Iterator<PhoneAccountHandle> phoneAccounts = telecomManager.getCallCapablePhoneAccounts().listIterator();
        while (phoneAccounts.hasNext()) {
            PhoneAccountHandle phoneAccountHandle = phoneAccounts.next();
            int subId = SubscriptionManager.getDefault().getSubscriptionIdForSlot(slot);
            if (TextUtils.equals(phoneAccountHandle.getId(), String.valueOf(subId))) {
                return phoneAccountHandle;
            }
        }
        Rlog.d(LOG_TAG, "cannot find PhoneAccountHandle for slot = " + slot);
        return null;
    }

    public boolean setDefaultDataSlotId(int slotId, String opPackageName) throws RemoteException {
        Rlog.d(LOG_TAG, "<" + opPackageName + "> is trying to set default data slot to " + slotId);
        MiuiTelephony.getInstance().getContext().enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", null);
        if (SubscriptionManager.isRealSlotId(slotId)) {
            this.mPropertyDefautlDataSlotId = slotId;
            SystemProperties.set("persist.radio.default.data", String.valueOf(slotId));
            storeValues(KEY_IS_USE_PREFRRED, VirtualSimUtils.getInstance().isVirtualSimEnabled(), KEY_PREFERRED_DATA_SLOT, slotId);
            int subId = SubscriptionManager.getSubscriptionId(slotId);
            if (!SubscriptionManager.isValidSubscriptionId(subId)) {
                subId = getSubIdBySlotId(slotId);
            }
            if (SubscriptionManager.getDefaultDataSubscriptionId() != subId) {
                this.mSm.setDefaultDataSubId(subId);
                return REQUEST;
            }
            if (this.mOldDefaultDataSlotId != slotId) {
                broadcastDataSlotChanged(slotId);
                this.mOldDefaultDataSlotId = slotId;
                return REQUEST;
            }
            return REQUEST;
        }
        return RELEASE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getSlotIdBySubId(int subId) {
        int slotId = SubscriptionManager.INVALID_SLOT_ID;
        List<SubscriptionInfo> sis = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (sis == null || subId == SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
            return slotId;
        }
        for (SubscriptionInfo si : sis) {
            if (si.getSubscriptionId() == subId) {
                slotId = si.getSlotId();
                break;
            }
        }
        Rlog.d(LOG_TAG, "getSlotIdBySubId : subId = " + subId + ", slotId = " + slotId);
        return slotId;
    }

    private static int getSubIdBySlotId(int slotId) {
        int subId = SubscriptionManager.INVALID_SUBSCRIPTION_ID;
        List<SubscriptionInfo> sis = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (sis == null) {
            return subId;
        }
        for (SubscriptionInfo si : sis) {
            if (si.getSlotId() == slotId) {
                subId = si.getSubscriptionId();
                break;
            }
        }
        Rlog.d(LOG_TAG, "getSubIdBySlotId : slotId = " + slotId + ", subId = " + subId);
        return subId;
    }

    public static int getDefaultDataSlotId() {
        if (sInstance != null && sInstance.mPropertyDefautlDataSlotId != SubscriptionManager.INVALID_SLOT_ID) {
            return sInstance.mPropertyDefautlDataSlotId;
        }
        return SubscriptionManager.getDefault().getDefaultDataSlotId();
    }

    public static int getDefaultVoiceSlotId() {
        if (sInstance != null) {
            return sInstance.mPropertyDefautlVoiceSlotId;
        }
        return SystemProperties.getInt("persist.radio.default.voice", SubscriptionManager.INVALID_SLOT_ID);
    }

    public int getSystemDefaultSlotId() {
        for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
            SubscriptionInfo subInfo = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i);
            if (IccCardStatus.CardState.CARDSTATE_ABSENT != this.mSimStatus[i] && subInfo != null && subInfo.isActivated()) {
                return i;
            }
        }
        return 0;
    }

    public static boolean getIsModemResetting() {
        return mIsModemResetting;
    }

    public static void setIsModemResetting(boolean isModemResetting) {
        mIsModemResetting = isModemResetting;
    }

    public void onSubscriptionsChanged() {
        List<SubscriptionInfo> subInfoImpls = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (!isAllInfoReady(subInfoImpls)) {
            this.mDataSlotReady = RELEASE;
            this.mLastSubInfos = null;
            this.mHandler.removeMessages(0);
            if (subInfoImpls == null || subInfoImpls.isEmpty()) {
                for (int i = 0; i < this.mSimInsertStatesDone.length; i++) {
                    this.mSimInsertStatesDone[i] = RELEASE;
                    Rlog.d(LOG_TAG, "onSubscriptionsChanged not ready, mSimInsertStatesDone[" + i + "]=" + this.mSimInsertStatesDone[i]);
                }
                return;
            }
            return;
        }
        if (isSameWithBefore(subInfoImpls) && this.mWaitingPsimEsimSwapCounts == 0) {
            return;
        }
        if (subInfoImpls.size() == 1) {
            Rlog.d(LOG_TAG, "onSubscriptionsChanged for one sub");
            int currentSlotId = subInfoImpls.get(0).getSlotId();
            broadcastDataSlotChanged(currentSlotId);
            for (int i2 = 0; i2 < this.mSimInsertStatesDone.length; i2++) {
                if (currentSlotId != i2) {
                    this.mSimInsertStatesDone[i2] = RELEASE;
                    Rlog.d(LOG_TAG, "onSubscriptionsChanged for one sub, mSimInsertStatesDone[" + i2 + "]=" + this.mSimInsertStatesDone[i2]);
                }
            }
            this.mLastSubInfos = subInfoImpls;
            this.mSimInsertStates = updateNewIccIds(subInfoImpls, RELEASE);
            this.mIsDisableingVirtualSim = VirtualSimUtils.getInstance().isDisablingVirtualSim();
            int delayTime = WAITING_SIM_TIME;
            if (isSupportMep() && ((currentSlotId != getDefaultDataSlotId() || currentSlotId != getDefaultVoiceSlotId()) && (isEsimCard(SubscriptionManager.getDefaultDataSubscriptionId()) || isEsimCard(SubscriptionManager.getDefaultVoiceSubscriptionId())))) {
                delayTime = WAITING_SIM_TIME_PSIM_ESIM_SWAP;
                Rlog.d(LOG_TAG, "onSubscriptionsChanged delayTime =" + WAITING_SIM_TIME_PSIM_ESIM_SWAP);
            }
            this.mHandler.sendEmptyMessageDelayed(0, delayTime);
            return;
        }
        Rlog.d(LOG_TAG, "onSubscriptionsChanged update subSize=" + subInfoImpls.size());
        this.mLastSubInfos = subInfoImpls;
        this.mSimInsertStates = updateNewIccIds(subInfoImpls, REQUEST);
        this.mHandler.sendEmptyMessage(0);
    }

    private boolean isEsimCard(int subId) {
        List<SubscriptionInfo> sis;
        if (SubscriptionManager.isValidSubscriptionId(subId) && (sis = SubscriptionManager.getDefault().getAllSubscriptionInfoListInternal()) != null && sis.size() != 0) {
            for (SubscriptionInfo info : sis) {
                if (subId == info.getSubscriptionId() && info.isEmbedded()) {
                    return REQUEST;
                }
            }
            return RELEASE;
        }
        return RELEASE;
    }

    private class TelephonyBroadcastReceiver extends BroadcastReceiver {
        private TelephonyBroadcastReceiver() {
        }

        void register(Context context) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
            filter.addAction("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED");
            context.registerReceiver(this, filter, 2);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int subId = intent.getIntExtra("subscription", SubscriptionManager.INVALID_SUBSCRIPTION_ID);
            Rlog.d(DefaultSimManager.LOG_TAG, "onReceive: action = " + intent.getAction() + " ,subId = " + subId);
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(intent.getAction())) {
                DefaultSimManager.this.onDefaultDataSubscriptionChanged(DefaultSimManager.getSlotIdBySubId(subId));
            } else if ("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED".equals(intent.getAction())) {
                DefaultSimManager.this.onDefaultVoiceSubscriptionChanged(DefaultSimManager.getSlotIdBySubId(subId));
            }
        }
    }

    private void initDefaultDataSlotId(int slotId, int[] simInsertStates) {
        if (!TelephonyManagerEx.getDefault().isSatelliteEnabled()) {
            this.mPropertyDefautlDataSlotId = slotId;
            SystemProperties.set("persist.radio.default.data", String.valueOf(slotId));
        }
        this.mOldDefaultDataSubId = SubscriptionManager.getDefaultDataSubscriptionId();
        int subId = SubscriptionManager.getSubscriptionId(slotId);
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            subId = getSubIdBySlotId(slotId);
        }
        if (this.mOldDefaultDataSubId != subId) {
            this.mSm.setDefaultDataSubId(subId);
        } else if (this.mOldDefaultDataSlotId != slotId) {
            broadcastDataSlotChanged(slotId);
            this.mOldDefaultDataSlotId = slotId;
        } else {
            broadcastDataSlotReady(slotId, simInsertStates);
        }
    }

    private void broadcastDataSlotReady(int slotId, int[] simInsertStates) {
        String states = simInsertStates == null ? "null" : simInsertStatesToString(simInsertStates);
        Rlog.d(LOG_TAG, "broadcastDataSlotReady dataSlot = " + slotId + ", sim state =[" + states + "]");
        Intent i = new Intent("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
        SubscriptionManager.putSlotIdExtra(i, slotId);
        i.putExtra("sim_insert_state_array", simInsertStates);
        MiuiTelephony.getInstance().getContext().sendBroadcast(i);
        notifyDataSlotReady(RELEASE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncVoiceSubscription(List<SubscriptionInfo> subInfos) {
        if (subInfos == null) {
            return;
        }
        int modemVoiceSubId = SubscriptionManager.getDefaultVoiceSubscriptionId();
        int userVoiceSlotId = this.mPropertyDefautlVoiceSlotId;
        for (SubscriptionInfo si : subInfos) {
            if (si.getSlotId() == userVoiceSlotId && si.getSubscriptionId() != modemVoiceSubId) {
                Rlog.d(LOG_TAG, "syncVoiceSubscription reset");
                this.mSm.setDefaultVoiceSubId(si.getSubscriptionId());
                return;
            }
        }
        Rlog.d(LOG_TAG, "syncVoiceSubscription ignore");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncDataSubscription(List<SubscriptionInfo> subInfos) {
        if (subInfos == null) {
            return;
        }
        int modemDataSubId = SubscriptionManager.getDefaultDataSubscriptionId();
        int userDataSlotId = this.mPropertyDefautlDataSlotId;
        for (SubscriptionInfo si : subInfos) {
            if (si.getSlotId() == userDataSlotId && si.getSubscriptionId() != modemDataSubId) {
                Rlog.d(LOG_TAG, "syncDataSubscription reset");
                this.mSm.setDefaultDataSubId(si.getSubscriptionId());
                return;
            }
        }
        Rlog.d(LOG_TAG, "syncDataSubscription ignore");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAllInfoReady(List<SubscriptionInfo> subInfos) {
        if (subInfos == null || subInfos.isEmpty()) {
            return RELEASE;
        }
        if (!isApplicationReady(subInfos)) {
            Rlog.d(LOG_TAG, "onSubscriptionsChanged return for no sim application");
            return RELEASE;
        }
        if (!isAllSubscriptionInfoCreated(subInfos)) {
            Rlog.d(LOG_TAG, "onSubscriptionsChanged return for some SubscriptionInfo is not created");
            return RELEASE;
        }
        if (!isMccMncReady(subInfos)) {
            Rlog.d(LOG_TAG, "onSubscriptionsChanged return for mcc is not ready");
            return RELEASE;
        }
        if ((MiuiTelephony.IS_MTK || (MiuiTelephony.IS_XRING && !CommenUtils.isXringModem())) && isCardNotReadyFromProperty()) {
            Rlog.d(LOG_TAG, "isCardPresentFromProperty return for card is not present");
            return RELEASE;
        }
        return REQUEST;
    }

    public boolean isAllSubscriptionInfoCreated(List<SubscriptionInfo> subInfos) {
        if (subInfos == null) {
            return RELEASE;
        }
        int cardCount = 0;
        int activeSimCount = 0;
        for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
            IccCardStatus.CardState cardState = getCardState(i);
            if (cardState != null && cardState == IccCardStatus.CardState.CARDSTATE_PRESENT) {
                cardCount++;
            }
        }
        for (SubscriptionInfo subInfo : subInfos) {
            if (subInfo != null && subInfo.isActivated()) {
                activeSimCount++;
            }
        }
        boolean isAllsubCreated = cardCount == subInfos.size();
        List<SubscriptionInfo> activeSubInfoList = SubscriptionManager.getDefault().getActiveSubscriptionInfoList();
        int activeSubInfoSize = activeSubInfoList == null ? 0 : activeSubInfoList.size();
        if (activeSubInfoSize < activeSimCount) {
            isAllsubCreated = RELEASE;
        }
        if (!MiuiTelephony.IS_PROJECT_SUPPORT_ESIM) {
            if (cardCount == 2 && subInfos.size() == 1 && EsimManager.getInstance() != null && EsimManager.getInstance().isSupportEsim() && !EsimManager.getInstance().isEsimActive()) {
                isAllsubCreated = REQUEST;
            }
        } else if (cardCount == 2 && subInfos.size() == 1 && MiuiTelephony.getInstance().getContext() != null && Settings.Secure.getInt(MiuiTelephony.getInstance().getContext().getContentResolver(), "is_enable_esim_for_user", -1) == 1 && !TelephonyManagerEx.getDefault().isEsimActive()) {
            isAllsubCreated = REQUEST;
            if (isSupportMep() && activeSubInfoSize == 2) {
                isAllsubCreated = RELEASE;
            }
        }
        if (cardCount == 2 && subInfos.size() == 1 && SystemProperties.getBoolean("persist.radio.allow_mock_modem", RELEASE)) {
            isAllsubCreated = REQUEST;
        }
        if (cardCount == 2 && subInfos.size() == 2 && activeSimCount == 2 && activeSubInfoSize == 1 && SystemProperties.getBoolean("persist.radio.allow_mock_modem", RELEASE)) {
            isAllsubCreated = REQUEST;
        }
        Rlog.d(LOG_TAG, "isAllSubscriptionInfoCreated cardCount=" + cardCount + ", activeSimCount = " + activeSimCount + ", subinfoSize = " + subInfos.size() + ", activeSubInfoSize = " + activeSubInfoSize + ",isAllsubCreated=" + isAllsubCreated);
        return isAllsubCreated;
    }

    private boolean isApplicationReady(List<SubscriptionInfo> subInfos) {
        if (subInfos == null) {
            return RELEASE;
        }
        for (SubscriptionInfo si : subInfos) {
            int slotId = si.getSlotId();
            if (SubscriptionManager.isValidSlotId(slotId)) {
                Rlog.d(LOG_TAG, "isApplicationReady si.isActivated()=" + si.isActivated() + ", application Number=" + this.mSimApplicationNum[slotId] + ", simStatus=" + this.mSimStatus[slotId]);
                if ((!MiuiTelephony.IS_MTK && !MiuiTelephony.IS_XRING) || this.mSimStatus[slotId] == IccCardStatus.CardState.CARDSTATE_ERROR || si.isActivated()) {
                    if (this.mSimStatus[slotId] != IccCardStatus.CardState.CARDSTATE_ERROR && this.mSimApplicationNum[slotId] < 1) {
                        return RELEASE;
                    }
                }
            }
        }
        return REQUEST;
    }

    private boolean isSupportMep() {
        return MiuiTelephony.getInstance().getContext().getPackageManager().hasSystemFeature("android.hardware.telephony.euicc.mep");
    }

    private boolean isSameWithBefore(List<SubscriptionInfo> newSubInfos) {
        int oldSize;
        if (this.mLastSubInfos == null || (oldSize = this.mLastSubInfos.size()) != newSubInfos.size()) {
            return RELEASE;
        }
        for (int i = 0; i < oldSize; i++) {
            SubscriptionInfo oldSub = this.mLastSubInfos.get(i);
            SubscriptionInfo newSub = newSubInfos.get(i);
            if (oldSub.getSlotId() != newSub.getSlotId() || oldSub.getSubscriptionId() != newSub.getSubscriptionId() || oldSub.isActivated() != newSub.isActivated()) {
                return RELEASE;
            }
        }
        return REQUEST;
    }

    /* JADX WARN: Type inference failed for: r2v29, types: [miui.telephony.DefaultSimManager$3] */
    void onInitDefaultSlot() {
        int newDs;
        if (SystemProperties.getInt("sys.in_shutdown_progress", 0) == 1) {
            Rlog.d(LOG_TAG, "this device is being shut down, ignore set sefault data slot.");
            return;
        }
        List<SubscriptionInfo> sis = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (sis == null) {
            return;
        }
        if (!isAllInfoReady(sis)) {
            onSubscriptionsChanged();
            return;
        }
        if (isSupportMep() && ((getDefaultDataSlotId() == 0 || getDefaultVoiceSlotId() == 0) && sis.size() == 1 && sis.get(0).getPhoneId() == 1 && MiuiTelephony.PHONE_COUNT > 1 && isPsimEsimSwap(0) && this.mWaitingPsimEsimSwapCounts < 5)) {
            this.mWaitingPsimEsimSwapCounts++;
            onSubscriptionsChanged();
            Rlog.d(LOG_TAG, "isPsimEsimSwap need onSubscriptionsChanged");
            return;
        }
        if (sis.size() == 1 && !sis.get(0).isActivated()) {
            if (PhoneHelper.isIccCardDisable() || PhoneHelperStub.getInstance().isIccCardDisable()) {
                Rlog.d(LOG_TAG, "Device is in enterprise mode, init slot is restricted by enterprise!");
                return;
            }
            this.mLastSubInfos = null;
            final int dataSlot = sis.get(0).getSlotId();
            new Thread(this) { // from class: miui.telephony.DefaultSimManager.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Rlog.d(DefaultSimManager.LOG_TAG, "single sim card,and the card is deactivated,so activate this card,data slot = " + dataSlot);
                    IccCardActivateHelper.setIccCardActivate(dataSlot, DefaultSimManager.REQUEST);
                }
            }.start();
            return;
        }
        this.mSimInsertStates = updateNewIccIds(sis, REQUEST);
        this.mIsDisableingVirtualSim = RELEASE;
        int vs = this.mPropertyDefautlVoiceSlotId;
        int ds = this.mPropertyDefautlDataSlotId;
        this.mOldDefaultDataSlotId = ds;
        int ds2 = getDataSlotForVirtualSim(ds, sis);
        if (!SubscriptionManager.isRealSlotId(vs) || IccCardStatus.CardState.CARDSTATE_ABSENT == this.mSimStatus[vs] || ((MiuiTelephony.PHONE_COUNT > 1 && isDcOnlyVirtualSim(vs)) || !IccCardActivateHelper.isActivate(vs))) {
            vs = SubscriptionManager.INVALID_SLOT_ID;
        }
        boolean foundDataSubInfo = RELEASE;
        StringBuilder logSb = new StringBuilder(RadioError.OEM_ERROR_12);
        for (SubscriptionInfo si : sis) {
            logSb.append('[').append(si.getSlotId()).append("]: sub=").append(si.getSubscriptionId()).append(", iccid=").append(PhoneDebug.VDBG ? si.getIccId() : TelephonyUtils.pii(si.getIccId())).append(", mcc=").append(si.getMcc()).append(", mnc=").append(si.getMnc());
            foundDataSubInfo = foundDataSubInfo || si.getSlotId() == ds2;
        }
        if (sis.size() == 1) {
            ds2 = sis.get(0).getSlotId();
            this.mSm.setDefaultSmsSubId(sis.get(0).getSubscriptionId());
        } else if (!foundDataSubInfo) {
            ds2 = getSystemDefaultSlotId();
            logSb.append(" system=").append(ds2);
            setDefaultSmsSubId(sis);
        } else if (!IccCardActivateHelper.isActivate(ds2)) {
            if (this.mWaitingDsActiveCnt <= 8) {
                Rlog.d(LOG_TAG, "ds not active, delay 500ms, count: " + this.mWaitingDsActiveCnt);
                if (this.mHandler.hasMessages(0)) {
                    this.mHandler.removeMessages(0);
                }
                this.mHandler.sendEmptyMessageDelayed(0, 500L);
                this.mWaitingDsActiveCnt++;
                return;
            }
            for (SubscriptionInfo si2 : sis) {
                if (si2.getSlotId() != ds2 && si2.isActivated()) {
                    int ds3 = si2.getSlotId();
                    this.mSm.setDefaultSmsSubId(si2.getSubscriptionId());
                    logSb.append(" activated=").append(ds3);
                    ds2 = ds3;
                    break;
                }
            }
        } else if (!IccCardActivateHelper.isActivate(SubscriptionManager.getDefault().getSlotIdForSubscription(SubscriptionManager.getDefaultSmsSubscriptionId()))) {
            for (SubscriptionInfo si3 : sis) {
                if (si3.getSubscriptionId() != SubscriptionManager.getDefaultSmsSubscriptionId() && si3.isActivated()) {
                    int ss = si3.getSlotId();
                    this.mSm.setDefaultSmsSubId(si3.getSubscriptionId());
                    logSb.append(" ss=").append(ss);
                    break;
                }
            }
        }
        this.mWaitingDsActiveCnt = 0;
        this.mWaitingPsimEsimSwapCounts = 0;
        if (this.mDefaultSlotSelector != null && ds2 != (newDs = this.mDefaultSlotSelector.getDefaultDataSlot(this.mSimInsertStates, ds2)) && SubscriptionManager.isRealSlotId(newDs)) {
            ds2 = newDs;
            logSb.append(" selected=").append(ds2);
        }
        logSb.append(", vs = ").append(vs).append(", ds = ").append(ds2).append(", current_ds = ").append(this.mOldDefaultDataSlotId).append(" simInsertStates=").append(simInsertStatesToString(this.mSimInsertStates));
        Rlog.d(LOG_TAG, logSb.toString());
        try {
            initDefaultDataSlotId(ds2, this.mSimInsertStates);
            setDefaultVoiceSlotId(vs, MiuiTelephony.getInstance().getContext().getOpPackageName());
        } catch (RemoteException e) {
        }
    }

    private void setDefaultSmsSubId(List<SubscriptionInfo> sis) {
        int curSmsSubId = SubscriptionManager.getDefaultSmsSubscriptionId();
        for (SubscriptionInfo si : sis) {
            if (si.getSubscriptionId() == curSmsSubId && si.isActivated()) {
                return;
            }
        }
        for (SubscriptionInfo si2 : sis) {
            if (si2.isActivated()) {
                Rlog.d(LOG_TAG, "setDefaultSmsSubId subId=" + si2.getSubscriptionId());
                this.mSm.setDefaultSmsSubId(si2.getSubscriptionId());
                return;
            }
        }
    }

    void onDefaultDataSubscriptionChanged(int slotId) {
        int userDataSlotId = this.mPropertyDefautlDataSlotId;
        if (userDataSlotId == SubscriptionManager.INVALID_SLOT_ID) {
            Rlog.d(LOG_TAG, "onDefaultDataSubscriptionChanged waiting for valid user data slot");
            return;
        }
        List<SubscriptionInfo> subInfos = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (subInfos == null) {
            return;
        }
        int modemDataSubId = SubscriptionManager.getDefaultDataSubscriptionId();
        int modemDataSlotId = SubscriptionManager.INVALID_SLOT_ID;
        for (SubscriptionInfo subInfo : subInfos) {
            if (subInfo.getSubscriptionId() == modemDataSubId) {
                modemDataSlotId = subInfo.getSlotId();
            }
        }
        if (modemDataSlotId == SubscriptionManager.INVALID_SLOT_ID) {
            Rlog.d(LOG_TAG, "onDefaultDataSubscriptionChanged waiting for valid modem data slot");
            return;
        }
        if (modemDataSlotId == userDataSlotId) {
            if (this.mOldDefaultDataSlotId != modemDataSlotId) {
                broadcastDataSlotChanged(modemDataSlotId);
                this.mOldDefaultDataSlotId = modemDataSlotId;
                this.mOldDefaultDataSubId = modemDataSubId;
                return;
            } else if (this.mOldDefaultDataSubId != modemDataSubId) {
                this.mOldDefaultDataSubId = modemDataSubId;
                broadcastDataSlotReady(modemDataSlotId, this.mSimInsertStates);
                return;
            } else {
                Rlog.d(LOG_TAG, "onDefaultDataSubscriptionChanged ignore");
                return;
            }
        }
        if (this.mHandler.hasMessages(0)) {
            Rlog.d(LOG_TAG, "onDefaultDataSubscriptionChanged inconsistent and wait for init");
            return;
        }
        if (slotId != SubscriptionManager.INVALID_SLOT_ID && !TelephonyManagerEx.getDefault().isSatelliteEnabled()) {
            this.mPropertyDefautlDataSlotId = slotId;
            SystemProperties.set("persist.radio.default.data", String.valueOf(slotId));
            Rlog.d(LOG_TAG, "onDefaultDataSubscriptionChanged update DEFAULT_DATA_SLOT_PROPERTY = " + slotId);
        }
        if (subInfos.size() == MiuiTelephony.PHONE_COUNT) {
            syncDataSubscription(subInfos);
            return;
        }
        Rlog.d(LOG_TAG, "onDefaultDataSubscriptionChanged delay");
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageAtTime(1, WAITING_SIM_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDefaultVoiceSubscriptionChanged(int slotId) {
        if (this.mHandler.hasMessages(0)) {
            Rlog.d(LOG_TAG, "onDefaultVoiceSubscriptionChanged inconsistent and wait for init");
            return;
        }
        int userVoiceSlotId = this.mPropertyDefautlVoiceSlotId;
        List<SubscriptionInfo> subInfos = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (subInfos == null) {
            return;
        }
        int modemVoiceSubId = SubscriptionManager.getDefaultVoiceSubscriptionId();
        int modemVoiceSlotId = SubscriptionManager.INVALID_SLOT_ID;
        for (SubscriptionInfo subInfo : subInfos) {
            if (subInfo.getSubscriptionId() == modemVoiceSubId) {
                modemVoiceSlotId = subInfo.getSlotId();
            }
        }
        if (modemVoiceSlotId != userVoiceSlotId && !TelephonyManagerEx.getDefault().isSatelliteEnabled()) {
            this.mPropertyDefautlVoiceSlotId = slotId;
            SystemProperties.set("persist.radio.default.voice", String.valueOf(slotId));
            Rlog.d(LOG_TAG, "onDefaultVoiceSubscriptionChanged update DEFAULT_VOICE_SLOT_PROPERTY = " + slotId);
        }
        if (subInfos != null && subInfos.size() == MiuiTelephony.PHONE_COUNT) {
            syncVoiceSubscription(subInfos);
            return;
        }
        Rlog.d(LOG_TAG, "onDefaultVoiceSubscriptionChanged delay");
        this.mHandler.removeMessages(2);
        this.mHandler.sendEmptyMessageDelayed(2, WAITING_SIM_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCallStateChanged() {
        int dds = getDefaultDataSlotId();
        int viceSlot = dds == 0 ? 1 : 0;
        Phone vicePhone = PhoneFactory.getPhone(viceSlot);
        this.mOldCallState = this.mCallState;
        this.mCallState = vicePhone.getState();
        if (this.mOldCallState == this.mCallState) {
            return;
        }
        if (this.mCallState != PhoneConstants.State.IDLE && this.mOldCallState == PhoneConstants.State.IDLE) {
            Rlog.d(LOG_TAG, "onCallStateChanged offhook");
            if (!vicePhone.isImsRegistered()) {
                return;
            }
            Settings.Global.putInt(MiuiTelephony.getInstance().getContext().getContentResolver(), "multi_sim_data_call", vicePhone.getSubId());
            PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
            if (phoneSwitcher != null) {
                Message msg = phoneSwitcher.obtainMessage(EVENT_VOLTE_TEMP_DDS, Integer.valueOf(viceSlot));
                msg.sendToTarget();
            }
            notifyPreferredDataSubIdChanged(vicePhone.getSubId());
            this.mNeedRestoreTempDdsSwitch = REQUEST;
            return;
        }
        if (this.mCallState == PhoneConstants.State.IDLE && this.mOldCallState != PhoneConstants.State.IDLE) {
            Rlog.d(LOG_TAG, "onCallStateChanged idle");
            if (this.mNeedRestoreTempDdsSwitch) {
                int ddsSubId = PhoneFactory.getPhone(dds).getSubId();
                Settings.Global.putInt(MiuiTelephony.getInstance().getContext().getContentResolver(), "multi_sim_data_call", ddsSubId);
                PhoneSwitcher phoneSwitcher2 = PhoneSwitcher.getInstance();
                if (phoneSwitcher2 != null) {
                    Message msg2 = phoneSwitcher2.obtainMessage(EVENT_VOLTE_TEMP_DDS, Integer.valueOf(dds));
                    msg2.sendToTarget();
                }
                notifyPreferredDataSubIdChanged(ddsSubId);
                this.mNeedRestoreTempDdsSwitch = RELEASE;
            }
        }
    }

    private void notifyPreferredDataSubIdChanged(int ddsSubId) {
        ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
        try {
            Rlog.d(LOG_TAG, "notifyPreferredDataSubIdChanged to " + ddsSubId);
            tr.notifyActiveDataSubIdChanged(ddsSubId);
        } catch (RemoteException e) {
        }
    }

    private void broadcastDataSlotChanged(int newSlotId) {
        Rlog.d(LOG_TAG, "broadcastDataSlotChanged old=" + this.mOldDefaultDataSlotId + ", new=" + newSlotId);
        Intent i = new Intent("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        i.putExtra("old_data_slot", this.mOldDefaultDataSlotId);
        SubscriptionManager.putSlotIdExtra(i, newSlotId);
        MiuiTelephony.getInstance().getContext().sendBroadcast(i);
        notifyDataSlotReady(REQUEST);
    }

    private int[] updateNewIccIds(List<SubscriptionInfo> sis, boolean needUpdateAllSlots) {
        int[] simInsertStates = new int[MiuiTelephony.PHONE_COUNT];
        String[] simIccIds = new String[simInsertStates.length];
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MiuiTelephony.getInstance().getContext());
        String iccids = sp.getString(KEY_LAST_ICCID, "");
        if (TextUtils.isEmpty(iccids)) {
            Arrays.fill(simIccIds, 0, simIccIds.length, (Object) null);
        } else {
            String[] parts = iccids.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
            int i = 0;
            while (i < simIccIds.length) {
                simIccIds[i] = i < parts.length ? parts[i] : null;
                if ("<null>".equals(simIccIds[i])) {
                    simIccIds[i] = null;
                }
                i++;
            }
        }
        String[] newSimIccIds = new String[simInsertStates.length];
        for (SubscriptionInfo si : sis) {
            int i2 = si.getSlotId();
            if (i2 < 0 || i2 >= newSimIccIds.length) {
                Rlog.e(LOG_TAG, "updateNewIccIds: invalid slotId=" + i2 + ", iccId=" + (PhoneDebug.VDBG ? si.getIccId() : TelephonyUtils.pii(si.getIccId())));
                return this.mSimInsertStates;
            }
            newSimIccIds[i2] = si.getIccId();
        }
        for (int i3 = 0; i3 < simInsertStates.length; i3++) {
            if ((!needUpdateAllSlots && !findSlotInSubinfos(i3, sis)) || this.mSimInsertStatesDone[i3]) {
                newSimIccIds[i3] = simIccIds[i3];
                simInsertStates[i3] = this.mSimInsertStates[i3];
            } else {
                if (simIccIds[i3] == null) {
                    simInsertStates[i3] = newSimIccIds[i3] == null ? 1 : 2;
                } else if (newSimIccIds[i3] == null) {
                    simInsertStates[i3] = 3;
                    if (this.mDefaultSlotSelector != null && !this.mIsDisableingVirtualSim) {
                        this.mDefaultSlotSelector.onSimRemoved(i3, simIccIds);
                    }
                } else {
                    simInsertStates[i3] = newSimIccIds[i3].equalsIgnoreCase(simIccIds[i3]) ? 0 : 4;
                }
                if (newSimIccIds[i3] != null) {
                    this.mSimInsertStatesDone[i3] = REQUEST;
                }
                Rlog.d(LOG_TAG, "updateNewIccIds simInsertStates[" + i3 + "]=" + simInsertStates[i3]);
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i4 = 0; i4 < newSimIccIds.length; i4++) {
            if (i4 != 0) {
                builder.append(',');
            }
            builder.append(newSimIccIds[i4] == null ? "<null>" : newSimIccIds[i4]);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_LAST_ICCID, builder.toString());
        editor.apply();
        return simInsertStates;
    }

    public static boolean findSlotInSubinfos(int slot, List<SubscriptionInfo> sis) {
        for (SubscriptionInfo si : sis) {
            if (slot == si.getSlotId()) {
                return REQUEST;
            }
        }
        return RELEASE;
    }

    private String simInsertStatesToString(int[] simInsertStates) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < simInsertStates.length; i++) {
            if (i != 0) {
                builder.append(", ");
            }
            switch (simInsertStates[i]) {
                case 0:
                    builder.append("NO_CHANGE");
                    break;
                case 1:
                    builder.append("NO CARD");
                    break;
                case 2:
                    builder.append("NEW_CARD");
                    break;
                case 3:
                    builder.append("REMOVED");
                    break;
                case 4:
                    builder.append("CHANGED");
                    break;
            }
        }
        return builder.toString();
    }

    private boolean isMccMncReady(List<SubscriptionInfo> subInfos) {
        String imsi;
        if ((!Build.IS_CM_CUSTOMIZATION && !Build.IS_INTERNATIONAL_BUILD) || subInfos == null || subInfos.size() < 2) {
            return REQUEST;
        }
        for (SubscriptionInfo si : subInfos) {
            if (si.getMcc() == 0 && ((imsi = getSimImsi(si.getSlotId())) == null || imsi.isEmpty())) {
                String imsi2 = getRuimImsi(si.getSlotId());
                if (imsi2 == null || imsi2.isEmpty()) {
                    Rlog.d(LOG_TAG, "isMccMncReady both cdma and gsm imsi is null");
                    return RELEASE;
                }
            }
        }
        return REQUEST;
    }

    public static boolean isDcOnlyVirtualSim(int voiceSlot) {
        return (MiuiSettings.VirtualSim.isVirtualSimEnabled(MiuiTelephony.getInstance().getContext()) && MiuiSettings.VirtualSim.getVirtualSimSlotId(MiuiTelephony.getInstance().getContext()) == voiceSlot && 1 == MiuiSettings.VirtualSim.getVirtualSimType(MiuiTelephony.getInstance().getContext())) ? REQUEST : RELEASE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needListenImsiReady() {
        return (Build.IS_CM_CUSTOMIZATION || IS_CUSTOMIZED_FOR_CM || Build.IS_INTERNATIONAL_BUILD) ? REQUEST : RELEASE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCard(UiccCard card, int slot) {
        UiccPort port = null;
        if (card == null) {
            this.mSimApplicationNum[slot] = 0;
        } else {
            port = card.getUiccPortForPhone(slot);
            int[] iArr = this.mSimApplicationNum;
            if (port == null) {
                iArr[slot] = 0;
            } else {
                iArr[slot] = port.getNumApplications();
            }
        }
        IccCardStatus.CardState state = getCardState(slot);
        this.mSimStatus[slot] = state == null ? IccCardStatus.CardState.CARDSTATE_ERROR : state;
        if (TelephonyManager.isCustFeatureResolveEnabled() && !isCardPresent(slot)) {
            notifyFeatureResolveSimAbsent(slot);
        }
        int[] appTypes = {1, 2};
        for (int i = 0; i < appTypes.length; i++) {
            IccRecords[] oldRecordsArray = appTypes[i] == 1 ? this.m3gppIccRecords : this.m3gpp2IccRecords;
            UiccCardApplication app = port == null ? null : port.getApplication(appTypes[i]);
            IccRecords newRecords = app != null ? app.getIccRecords() : null;
            if (newRecords != oldRecordsArray[slot]) {
                if (oldRecordsArray[slot] != null) {
                    oldRecordsArray[slot].unregisterForImsiReady(this.mHandler);
                }
                if (newRecords != null) {
                    newRecords.registerForImsiReady(this.mHandler, 4, Integer.valueOf(slot));
                }
            }
            oldRecordsArray[slot] = newRecords;
        }
    }

    public static IccCardStatus.CardState getCardState(int phoneId) {
        UiccSlot uiccSlot = UiccController.getInstance().getUiccSlotForPhone(phoneId);
        if (uiccSlot != null) {
            UiccCard card = uiccSlot.getUiccCard();
            if (card != null) {
                return card.getCardState();
            }
            if (!uiccSlot.isStateUnknown()) {
                return IccCardStatus.CardState.CARDSTATE_ABSENT;
            }
            return null;
        }
        return null;
    }

    public static boolean isPsimEsimSwap(int phoneId) {
        UiccSlot uiccSlot = UiccController.getInstance().getUiccSlotForPhone(phoneId);
        SubscriptionInfo subInfo = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(phoneId);
        if (uiccSlot != null && subInfo == null) {
            UiccCard card = uiccSlot.getUiccCard();
            if (card != null) {
                return (card.getCardState() != IccCardStatus.CardState.CARDSTATE_PRESENT || uiccSlot.getEid() == null) ? RELEASE : REQUEST;
            }
            if (!uiccSlot.isStateUnknown()) {
                return RELEASE;
            }
        }
        return RELEASE;
    }

    public String getSimImsi(int slotId) {
        if (this.m3gppIccRecords[slotId] == null) {
            return null;
        }
        return this.m3gppIccRecords[slotId].getIMSI();
    }

    public String getRuimImsi(int slotId) {
        if (this.m3gpp2IccRecords[slotId] == null) {
            return null;
        }
        return this.m3gpp2IccRecords[slotId].getIMSI();
    }

    private int getDataSlotForVirtualSim(int ds, List<SubscriptionInfo> sis) {
        int ret = ds;
        if (VirtualSimUtils.isCloudSimEnabled()) {
            return ret;
        }
        if (sis.size() == 2) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MiuiTelephony.getInstance().getContext());
            boolean usePreferred = sp.getBoolean(KEY_IS_USE_PREFRRED, RELEASE);
            int preferredSlot = SubscriptionManager.INVALID_SLOT_ID;
            if (usePreferred) {
                preferredSlot = sp.getInt(KEY_PREFERRED_DATA_SLOT, SubscriptionManager.INVALID_SLOT_ID);
            }
            int virtualSlot = VirtualSimUtils.getInstance().getVirtualSimSlotId();
            if (preferredSlot != SubscriptionManager.INVALID_SLOT_ID) {
                ret = preferredSlot;
            } else if (virtualSlot != SubscriptionManager.INVALID_SLOT_ID) {
                ret = virtualSlot;
            }
            Rlog.d(LOG_TAG, "getDataSlotForVirtualSim ret=" + ret + " usePreferred=" + usePreferred + " preferredSlot=" + preferredSlot + " virtualSLotId=" + virtualSlot);
        } else if (sis.size() == 1) {
            if (VirtualSimUtils.getInstance().isVirtualSimEnabled()) {
                if (sis.get(0).getSlotId() == MiuiSettings.VirtualSim.getVirtualSimSlotId(MiuiTelephony.getInstance().getContext())) {
                    Rlog.d(LOG_TAG, "getDataSlotForVirtualSim restore for only virtual sim inserted");
                    storeValues(KEY_IS_USE_PREFRRED, RELEASE, KEY_PREFERRED_DATA_SLOT, sis.get(0).getSlotId());
                } else {
                    Rlog.d(LOG_TAG, "getDataSlotForVirtualSim waiting for virtual sim ready");
                }
            } else {
                Rlog.d(LOG_TAG, "getDataSlotForVirtualSim restore for virtual sim disabled");
                storeValues(KEY_IS_USE_PREFRRED, RELEASE, KEY_PREFERRED_DATA_SLOT, SubscriptionManager.INVALID_SLOT_ID);
            }
        }
        return ret;
    }

    @Override // miui.telephony.VirtualSimUtils.VirtualSimListenner
    public void onVirtualSimPreciseStateChanged() {
        if (VirtualSimUtils.getInstance().isDisablingVirtualSim()) {
            storeValues(KEY_IS_USE_PREFRRED, REQUEST, null, 0);
        }
    }

    @Override // miui.telephony.VirtualSimUtils.VirtualSimListenner
    public void onVirtualSimStateChanged() {
        if (VirtualSimUtils.getInstance().isVirtualSimEnabled()) {
            storeValues(KEY_IS_USE_PREFRRED, RELEASE, KEY_PREFERRED_DATA_SLOT, SystemProperties.getInt("persist.radio.default.data", SubscriptionManager.INVALID_SLOT_ID));
        }
    }

    public static void storeValues(String key1, boolean value1, String key2, int value2) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MiuiTelephony.getInstance().getContext());
        SharedPreferences.Editor editor = sp.edit();
        if (key1 != null) {
            editor.putBoolean(key1, value1);
        }
        if (key2 != null) {
            editor.putInt(key2, value2);
        }
        Rlog.d(LOG_TAG, "storeValues " + key1 + "=" + value1 + " " + key2 + "=" + value2);
        editor.apply();
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MiuiTelephony.getInstance().getContext());
        String currentMode = Settings.Global.getString(MiuiTelephony.getInstance().getContext().getContentResolver(), "preferred_network_mode");
        StringBuilder sb = new StringBuilder(RadioError.OEM_ERROR_12).append("DefaultSimManager: PREFERRED_NETWORK_MODE init=").append(this.mInitModes).append(" current=").append(currentMode).append(" mIsUsePreferred=").append(this.mIsUsePreferred).append(" mPreferredDataSlot=" + this.mPreferredDataSlot).append(" isUsePreferred=").append(sp.getBoolean(KEY_IS_USE_PREFRRED, RELEASE)).append(" preferredDataSlot=").append(sp.getInt(KEY_PREFERRED_DATA_SLOT, SubscriptionManager.INVALID_SLOT_ID));
        pw.println(sb.toString());
    }

    public int getSimInsertStates(int slotId) {
        if (this.mSimInsertStates == null) {
            Rlog.d(LOG_TAG, "mSimInsertStates is not initialized,return SIM_NO_CARD");
            return 1;
        }
        return this.mSimInsertStates[slotId];
    }

    public static int getPreferredNetworkModeFromDb(Context c, int slot) {
        if (((MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING || MiuiTelephony.IS_SPRD) && !TelephonyManager.getDefault().isDualVolteSupported()) || MiuiTelephony.IS_PINECONE) {
            SubscriptionInfo subInfo = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(slot);
            if (subInfo != null) {
                return Settings.Global.getInt(c.getContentResolver(), "preferred_network_mode" + subInfo.getSubscriptionId(), RILConstants.PREFERRED_NETWORK_MODE);
            }
            return RILConstants.PREFERRED_NETWORK_MODE;
        }
        int ret = Phone.PREFERRED_NT_MODE;
        try {
            int ret2 = TelephonyManager.getIntAtIndex(c.getContentResolver(), "preferred_network_mode", slot);
            return ret2;
        } catch (Settings.SettingNotFoundException e) {
            Rlog.i(LOG_TAG, "getPreferredNetworkModeFromDb slot=" + slot + " e=" + e);
            return ret;
        }
    }

    public static void setNetworkModeInDb(int slotId, int networkType) {
        StringBuilder logs = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY).append("setNetworkModeInDb slotId=").append(slotId).append(" networkType=").append(networkType);
        if ((((MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING || MiuiTelephony.IS_SPRD) && TelephonyManager.getDefault().isDualVolteSupported()) || MiuiTelephony.IS_QCOM) && isUsableSlotIdValue(slotId)) {
            TelephonyManager.putIntAtIndex(MiuiTelephony.getInstance().getContext().getContentResolver(), "preferred_network_mode", slotId, networkType);
        }
        SubscriptionInfo subInfo = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(slotId);
        if (subInfo != null) {
            Settings.Global.putInt(MiuiTelephony.getInstance().getContext().getContentResolver(), "preferred_network_mode" + subInfo.getSubscriptionId(), networkType);
            logs.append(" sub=").append(subInfo.getSubscriptionId());
        } else {
            logs.append(" sub=").append("invalid");
        }
        Rlog.i(LOG_TAG, logs.toString());
    }

    public void addDataSlotListener(DataSlotListener l) {
        synchronized (this.mDataSlotListener) {
            if (l != null) {
                this.mDataSlotListener.add(l);
            }
        }
    }

    public void removeDataSlotListener(DataSlotListener l) {
        synchronized (this.mDataSlotListener) {
            if (l != null) {
                this.mDataSlotListener.remove(l);
            }
        }
    }

    public void notifyDataSlotReady(boolean isDataSlotChanged) {
        synchronized (this.mDataSlotListener) {
            this.mDataSlotReady = REQUEST;
            for (DataSlotListener l : this.mDataSlotListener) {
                l.onDataSlotReady(isDataSlotChanged);
            }
        }
    }

    public boolean isDataSlotReady() {
        return this.mDataSlotReady;
    }

    public void addImsiListener(ImsiListener l) {
        synchronized (this.mImsiListener) {
            if (l != null) {
                this.mImsiListener.add(l);
            }
        }
    }

    public void removeImsiListener(ImsiListener l) {
        synchronized (this.mImsiListener) {
            if (l != null) {
                this.mImsiListener.remove(l);
            }
        }
    }

    public void notifyImsiReady(int slotId) {
        synchronized (this.mImsiListener) {
            for (ImsiListener l : this.mImsiListener) {
                l.onImsiReady(slotId);
            }
        }
    }

    private void notifyFeatureResolveSimAbsent(int phoneId) {
        Bundle simInfo = new Bundle();
        simInfo.putInt("phoneId", phoneId);
        Rlog.d(LOG_TAG, "notifyFeatureResolveSimAbsent phoneId " + phoneId);
        HyperOSCustFeatureResolve.notifyCustFeatureResolve(TAG_SIM_ABSENT, simInfo);
    }

    public boolean isCardPresent(int slotId) {
        return this.mSimStatus[slotId] == IccCardStatus.CardState.CARDSTATE_PRESENT ? REQUEST : RELEASE;
    }

    public String getImsi(int slotId) {
        String imsi = getRuimImsi(slotId);
        return imsi == null ? getSimImsi(slotId) : imsi;
    }

    public int getPresentCardCount() {
        int count = 0;
        for (IccCardStatus.CardState cs : this.mSimStatus) {
            if (cs == IccCardStatus.CardState.CARDSTATE_PRESENT) {
                count++;
            }
        }
        return count;
    }

    public boolean isCardNotReadyFromProperty() {
        for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
            int validCard = SystemProperties.getInt(PROPERTY_RIL_CDMA_CARD_TYPE[i], -1);
            Rlog.d(LOG_TAG, "isCardNotReadyFromProperty: " + validCard + " mSimStatus: " + this.mSimStatus[i]);
            if (validCard != 255 && this.mSimStatus[i] != IccCardStatus.CardState.CARDSTATE_PRESENT) {
                return REQUEST;
            }
        }
        return RELEASE;
    }

    private static boolean isUsableSlotIdValue(int slotId) {
        return (slotId < 0 || slotId > 2147483646) ? RELEASE : REQUEST;
    }
}
