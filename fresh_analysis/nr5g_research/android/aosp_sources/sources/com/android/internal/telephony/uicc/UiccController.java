package com.android.internal.telephony.uicc;

import android.R;
import android.app.BroadcastOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;
import android.telephony.UiccSlotMapping;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CarrierServiceBindHelper;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.IntentBroadcaster;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RadioConfig;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.euicc.EuiccCard;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.telephony.util.WorkerThread;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/* JADX INFO: loaded from: classes.dex */
public class UiccController extends Handler {
    public static final int APP_FAM_3GPP = 1;
    public static final int APP_FAM_3GPP2 = 2;
    public static final int APP_FAM_IMS = 3;
    protected static final boolean DBG = true;
    protected static final int EVENT_GET_ICC_STATUS_DONE = 3;
    protected static final int EVENT_GET_SLOT_STATUS_DONE = 4;
    protected static final int EVENT_ICC_STATUS_CHANGED = 1;
    protected static final int EVENT_MULTI_SIM_CONFIG_CHANGED = 10;
    protected static final int EVENT_RADIO_AVAILABLE = 6;
    protected static final int EVENT_RADIO_ON = 5;
    public static final int INVALID_PHONE_ID = -1;
    public static final int INVALID_SLOT_ID = -1;
    protected static final boolean VDBG = false;
    private static UiccController mInstance;
    protected static final Object mLock = new Object();
    public static ArrayList<IccSlotStatus> sLastSlotStatus;
    private static LocalLog sLocalLog;
    private ArrayList mCardStrings;
    private final CarrierServiceBindHelper mCarrierServiceBindHelper;
    protected CommandsInterface[] mCis;
    public Context mContext;
    private int mDefaultEuiccCardId;
    private final int[] mEuiccSlots;
    private final FeatureFlags mFeatureFlags;
    private boolean mHasBuiltInEuicc;
    private String[] mIccids;
    private UiccStateChangedLauncher mLauncher;
    protected int[] mPhoneIdToSlotId;
    private final PinStorage mPinStorage;
    protected RadioConfig mRadioConfig;
    private final int[] mSimApplicationState;
    private final int[] mSimCardState;
    private final int mSupportedModemCount;
    private final TelephonyManager mTelephonyManager;
    public UiccSlot[] mUiccSlots;
    private boolean mUseRemovableEsimAsDefault;
    private boolean mIsSlotStatusSupported = true;
    private boolean mHasActiveBuiltInEuicc = false;
    protected RegistrantList mIccChangedRegistrants = new RegistrantList();

    public void fetchSimTypeInfo(int i) {
    }

    public IccSlotStatus.MultipleEnabledProfilesMode getCurrentSavedMepMode(int i) {
        return null;
    }

    public boolean ignoreGetSimStatus() {
        return false;
    }

    public void setSimTypeQuereState(int i) {
    }

    public boolean switchSlotsInternal(List<UiccSlotMapping> list, Message message) {
        return false;
    }

    static {
        sLocalLog = new LocalLog(TelephonyUtils.IS_DEBUGGABLE ? CallFailCause.RADIO_UPLINK_FAILURE : 64);
    }

    public static UiccController make(Context context, FeatureFlags featureFlags) {
        UiccController uiccControllerMakeUiccController;
        synchronized (mLock) {
            try {
                if (mInstance != null) {
                    throw new RuntimeException("UiccController.make() should only be called once");
                }
                uiccControllerMakeUiccController = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeUiccController(context, featureFlags);
                mInstance = uiccControllerMakeUiccController;
            } catch (Throwable th) {
                throw th;
            }
        }
        return uiccControllerMakeUiccController;
    }

    public UiccController(Context context, FeatureFlags featureFlags) {
        this.mHasBuiltInEuicc = false;
        this.mUseRemovableEsimAsDefault = false;
        log("Creating UiccController");
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mCis = PhoneFactory.getCommandsInterfaces();
        int iIntValue = TelephonyProperties.sim_slots_count().orElse(Integer.valueOf(context.getResources().getInteger(R.integer.config_screenBrightnessSettingMaximum))).intValue();
        logl("config_num_physical_slots = " + iIntValue);
        int i = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
        logl("current vendor api_level: " + i);
        if (i <= 202404) {
            logl("Adjusting numPhysicalSlots for firstApiLevel = " + i + " based on mCis.length");
            CommandsInterface[] commandsInterfaceArr = this.mCis;
            if (iIntValue < commandsInterfaceArr.length) {
                iIntValue = commandsInterfaceArr.length;
            }
        }
        CommandsInterface[] commandsInterfaceArr2 = this.mCis;
        iIntValue = iIntValue < commandsInterfaceArr2.length ? commandsInterfaceArr2.length : iIntValue;
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        this.mTelephonyManager = telephonyManager;
        this.mUiccSlots = new UiccSlot[iIntValue];
        this.mPhoneIdToSlotId = new int[this.mCis.length];
        int supportedModemCount = telephonyManager.getSupportedModemCount();
        this.mSupportedModemCount = supportedModemCount;
        this.mIccids = new String[supportedModemCount];
        this.mSimCardState = new int[supportedModemCount];
        this.mSimApplicationState = new int[supportedModemCount];
        Arrays.fill(this.mPhoneIdToSlotId, -1);
        RadioConfig radioConfig = RadioConfig.getInstance();
        this.mRadioConfig = radioConfig;
        radioConfig.registerForSimSlotStatusChanged(this, 2, null);
        int i2 = 0;
        while (true) {
            CommandsInterface[] commandsInterfaceArr3 = this.mCis;
            if (i2 >= commandsInterfaceArr3.length) {
                break;
            }
            commandsInterfaceArr3[i2].registerForIccStatusChanged(this, 1, Integer.valueOf(i2));
            this.mCis[i2].registerForAvailable(this, 6, Integer.valueOf(i2));
            this.mCis[i2].registerForNotAvailable(this, 7, Integer.valueOf(i2));
            this.mCis[i2].registerForIccRefresh(this, 8, Integer.valueOf(i2));
            i2++;
        }
        this.mLauncher = new UiccStateChangedLauncher(context, this, this.mFeatureFlags);
        this.mCardStrings = loadCardStrings();
        this.mDefaultEuiccCardId = -2;
        this.mCarrierServiceBindHelper = new CarrierServiceBindHelper(this.mContext);
        this.mEuiccSlots = this.mContext.getResources().getIntArray(17236293);
        this.mHasBuiltInEuicc = hasBuiltInEuicc();
        PhoneConfigurationManager.registerForMultiSimConfigChange(this, 10, null);
        this.mPinStorage = new PinStorage(this.mContext, WorkerThread.getHandler().getLooper(), this.mFeatureFlags);
        if (TelephonyUtils.IS_USER) {
            return;
        }
        this.mUseRemovableEsimAsDefault = PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("removable_esim", false);
    }

    public int getPhoneIdFromSlotPortIndex(int i, int i2) {
        UiccSlot uiccSlot = getUiccSlot(i);
        if (uiccSlot == null) {
            return -1;
        }
        return uiccSlot.getPhoneIdFromPortIndex(i2);
    }

    public int getSlotIdFromPhoneId(int i) {
        try {
            return this.mPhoneIdToSlotId[i];
        } catch (ArrayIndexOutOfBoundsException unused) {
            return -1;
        }
    }

    public static UiccController getInstance() {
        UiccController uiccController = mInstance;
        if (uiccController != null) {
            return uiccController;
        }
        Phone$$ExternalSyntheticBUOutline1.m("UiccController.getInstance can't be called before make()");
        return null;
    }

    public UiccCard getUiccCard(int i) {
        UiccCard uiccCardForPhone;
        synchronized (mLock) {
            uiccCardForPhone = getUiccCardForPhone(i);
        }
        return uiccCardForPhone;
    }

    public UiccPort getUiccPort(int i) {
        UiccPort uiccPortForPhone;
        synchronized (mLock) {
            uiccPortForPhone = getUiccPortForPhone(i);
        }
        return uiccPortForPhone;
    }

    public UiccPort getUiccPortForSlot(int i, int i2) {
        UiccCard uiccCard;
        synchronized (mLock) {
            try {
                UiccSlot uiccSlot = getUiccSlot(i);
                if (uiccSlot == null || (uiccCard = uiccSlot.getUiccCard()) == null) {
                    return null;
                }
                return uiccCard.getUiccPort(i2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public UiccCard getUiccCardForSlot(int i) {
        synchronized (mLock) {
            try {
                UiccSlot uiccSlot = getUiccSlot(i);
                if (uiccSlot == null) {
                    return null;
                }
                return uiccSlot.getUiccCard();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public UiccCard getUiccCardForPhone(int i) {
        UiccSlot uiccSlotForPhone;
        synchronized (mLock) {
            try {
                if (!isValidPhoneIndex(i) || (uiccSlotForPhone = getUiccSlotForPhone(i)) == null) {
                    return null;
                }
                return uiccSlotForPhone.getUiccCard();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public UiccPort getUiccPortForPhone(int i) {
        UiccSlot uiccSlotForPhone;
        UiccCard uiccCard;
        synchronized (mLock) {
            try {
                if (!isValidPhoneIndex(i) || (uiccSlotForPhone = getUiccSlotForPhone(i)) == null || (uiccCard = uiccSlotForPhone.getUiccCard()) == null) {
                    return null;
                }
                return uiccCard.getUiccPortForPhone(i);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public UiccProfile getUiccProfileForPhone(int i) {
        synchronized (mLock) {
            try {
                if (!isValidPhoneIndex(i)) {
                    return null;
                }
                UiccPort uiccPortForPhone = getUiccPortForPhone(i);
                return uiccPortForPhone != null ? uiccPortForPhone.getUiccProfile() : null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public UiccSlot[] getUiccSlots() {
        UiccSlot[] uiccSlotArr;
        synchronized (mLock) {
            uiccSlotArr = this.mUiccSlots;
        }
        return uiccSlotArr;
    }

    public void switchSlots(List<UiccSlotMapping> list, Message message) {
        if (switchSlotsInternal(list, message)) {
            return;
        }
        logl("switchSlots: " + list);
        if (this.mRadioConfig.isGetOrSetSimTypeSupported()) {
            if (isSimTypeChangeRequest(list)) {
                logl("switchSlots: request for physical slot-simtype change");
                int[] iArr = new int[this.mUiccSlots.length];
                for (UiccSlotMapping uiccSlotMapping : list) {
                    iArr[uiccSlotMapping.getPhysicalSlotIndex()] = uiccSlotMapping.getSimType();
                }
                this.mRadioConfig.setSimType(iArr, message);
                return;
            }
            logl("switchSlots: request for logical slot to slot-port mapping");
            this.mRadioConfig.setSimSlotsMapping(list, message);
            return;
        }
        this.mRadioConfig.setSimSlotsMapping(list, message);
    }

    public boolean isSimTypeChangeRequest(List<UiccSlotMapping> list) {
        for (UiccSlotMapping uiccSlotMapping : list) {
            int physicalSlotIndex = uiccSlotMapping.getPhysicalSlotIndex();
            if (uiccSlotMapping.getSimType() != -1 && isValidSlotIndex(physicalSlotIndex) && this.mUiccSlots[physicalSlotIndex].getSimType() != uiccSlotMapping.getSimType()) {
                return true;
            }
        }
        return false;
    }

    public UiccSlot getUiccSlot(int i) {
        synchronized (mLock) {
            try {
                if (!isValidSlotIndex(i)) {
                    return null;
                }
                return this.mUiccSlots[i];
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setUiccSlot(int i, UiccSlot uiccSlot) {
        synchronized (mLock) {
            try {
                if (!isValidSlotIndex(i)) {
                    throw new ArrayIndexOutOfBoundsException("Invalid slot index: " + i);
                }
                UiccSlot uiccSlot2 = this.mUiccSlots[i];
                if (uiccSlot2 != null) {
                    uiccSlot2.dispose();
                }
                UiccSlot[] uiccSlotArr = this.mUiccSlots;
                Objects.requireNonNull(uiccSlot);
                uiccSlotArr[i] = uiccSlot;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public UiccSlot getUiccSlotForPhone(int i) {
        synchronized (mLock) {
            try {
                if (isValidPhoneIndex(i)) {
                    int slotIdFromPhoneId = getSlotIdFromPhoneId(i);
                    if (isValidSlotIndex(slotIdFromPhoneId)) {
                        return this.mUiccSlots[slotIdFromPhoneId];
                    }
                }
                return null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getUiccSlotForCardId(final String str) {
        synchronized (mLock) {
            int i = 0;
            int i2 = 0;
            while (true) {
                try {
                    UiccSlot[] uiccSlotArr = this.mUiccSlots;
                    if (i2 < uiccSlotArr.length) {
                        UiccSlot uiccSlot = uiccSlotArr[i2];
                        if (uiccSlot != null) {
                            UiccCard uiccCard = uiccSlot.getUiccCard();
                            if (uiccCard != null && str.equals(uiccCard.getCardId())) {
                                return i2;
                            }
                            if (uiccCard != null && str.equals(IccUtils.stripTrailingFs(uiccCard.getCardId()))) {
                                return i2;
                            }
                        }
                        i2++;
                    } else {
                        while (true) {
                            UiccSlot[] uiccSlotArr2 = this.mUiccSlots;
                            if (i >= uiccSlotArr2.length) {
                                return -1;
                            }
                            final UiccSlot uiccSlot2 = uiccSlotArr2[i];
                            if (uiccSlot2 != null) {
                                if (IntStream.of(uiccSlot2.getPortList()).anyMatch(new IntPredicate() { // from class: com.android.internal.telephony.uicc.UiccController$$ExternalSyntheticLambda1
                                    @Override // java.util.function.IntPredicate
                                    public final boolean test(int i3) {
                                        return str.equals(uiccSlot2.getIccId(i3));
                                    }
                                })) {
                                    return i;
                                }
                                if (str != null && str.equals(IccUtils.stripTrailingFs(uiccSlot2.getIccId(uiccSlot2.getPortIndexFromPhoneId(i))))) {
                                    return i;
                                }
                            }
                            i++;
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public IccRecords getIccRecords(int i, int i2) {
        synchronized (mLock) {
            try {
                UiccCardApplication uiccCardApplication = getUiccCardApplication(i, i2);
                if (uiccCardApplication == null) {
                    return null;
                }
                return uiccCardApplication.getIccRecords();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public IccFileHandler getIccFileHandler(int i, int i2) {
        synchronized (mLock) {
            try {
                UiccCardApplication uiccCardApplication = getUiccCardApplication(i, i2);
                if (uiccCardApplication == null) {
                    return null;
                }
                return uiccCardApplication.getIccFileHandler();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void registerForIccChanged(Handler handler, int i, Object obj) {
        synchronized (mLock) {
            this.mIccChangedRegistrants.addUnique(handler, i, obj);
        }
        Message.obtain(handler, i, new AsyncResult(obj, (Object) null, (Throwable) null)).sendToTarget();
    }

    public void unregisterForIccChanged(Handler handler) {
        synchronized (mLock) {
            this.mIccChangedRegistrants.remove(handler);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        synchronized (mLock) {
            try {
                Integer ciIndex = getCiIndex(message);
                String strEventToString = eventToString(message.what);
                if (ciIndex.intValue() >= 0 && ciIndex.intValue() < this.mCis.length) {
                    logl("handleMessage: Received " + strEventToString + " for phoneId " + ciIndex);
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    switch (message.what) {
                        case 1:
                            log("Received EVENT_ICC_STATUS_CHANGED, calling getIccCardStatus");
                            if (ignoreGetSimStatus()) {
                                log("FlightMode ON, Modem OFF: ignore get sim status");
                            } else {
                                this.mCis[ciIndex.intValue()].getIccCardStatus(obtainMessage(3, ciIndex));
                            }
                            break;
                        case 2:
                        case 4:
                            log("Received EVENT_SLOT_STATUS_CHANGED or EVENT_GET_SLOT_STATUS_DONE");
                            onGetSlotStatusDone(asyncResult);
                            break;
                        case 3:
                            log("Received EVENT_GET_ICC_STATUS_DONE");
                            onGetIccCardStatusDone(asyncResult, ciIndex);
                            fetchSimTypeInfo(ciIndex.intValue());
                            break;
                        case 5:
                        case 6:
                            log("Received EVENT_RADIO_AVAILABLE/EVENT_RADIO_ON, calling getIccCardStatus");
                            setSimTypeQuereState(ciIndex.intValue());
                            if (ignoreGetSimStatus()) {
                                log("FlightMode ON, Modem OFF: ignore get sim status, phoneId :" + ciIndex);
                            } else {
                                this.mCis[ciIndex.intValue()].getIccCardStatus(obtainMessage(3, ciIndex));
                                if (ciIndex.intValue() == 0) {
                                    log("Received EVENT_RADIO_AVAILABLE/EVENT_RADIO_ON for phoneId 0, calling getIccSlotsStatus");
                                    this.mRadioConfig.getSimSlotsStatus(obtainMessage(4, ciIndex));
                                }
                            }
                            break;
                        case 7:
                            log("EVENT_RADIO_UNAVAILABLE, dispose card");
                            sLastSlotStatus = null;
                            UiccSlot uiccSlotForPhone = getUiccSlotForPhone(ciIndex.intValue());
                            if (uiccSlotForPhone != null) {
                                uiccSlotForPhone.onRadioStateUnavailable(ciIndex.intValue());
                            }
                            this.mIccChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, ciIndex, (Throwable) null));
                            break;
                        case 8:
                            log("Received EVENT_SIM_REFRESH");
                            onSimRefresh(asyncResult, ciIndex);
                            break;
                        case 9:
                            log("Received EVENT_EID_READY");
                            onEidReady(asyncResult, ciIndex);
                            break;
                        case 10:
                            log("Received EVENT_MULTI_SIM_CONFIG_CHANGED");
                            onMultiSimConfigChanged(((Integer) ((AsyncResult) message.obj).result).intValue());
                            break;
                        case 11:
                            log("Received EVENT_GET_SIM_TYPE_INFO");
                            onGetSimTypeInfo(asyncResult);
                            break;
                        default:
                            logel(" Unknown Event " + message.what);
                            break;
                    }
                    return;
                }
                logel("Invalid phoneId : " + ciIndex + " received with event " + strEventToString);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void onGetSimTypeInfo(AsyncResult asyncResult) {
        if (asyncResult.exception != null) {
            logel("onGetSimTypeInfo: Exception during getSimTypeInfo: " + asyncResult.exception);
            return;
        }
        ArrayList arrayList = (ArrayList) asyncResult.result;
        logl("onGetSimTypeInfo: " + arrayList);
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            SimTypeInfo simTypeInfo = (SimTypeInfo) obj;
            if (simTypeInfo == null || !isValidSlotIndex(simTypeInfo.mPhysicalSlotIndex)) {
                logel("onGetSimTypeInfo invalid sim type info: " + simTypeInfo);
            } else {
                this.mUiccSlots[simTypeInfo.mPhysicalSlotIndex].updateSimTypeInfo(simTypeInfo);
            }
        }
    }

    private void onMultiSimConfigChanged(int i) {
        int length = this.mCis.length;
        this.mCis = PhoneFactory.getCommandsInterfaces();
        logl("onMultiSimConfigChanged: prevActiveModemCount " + length + ", newActiveModemCount " + i);
        this.mPhoneIdToSlotId = Arrays.copyOf(this.mPhoneIdToSlotId, i);
        while (length < i) {
            this.mPhoneIdToSlotId[length] = -1;
            this.mCis[length].registerForIccStatusChanged(this, 1, Integer.valueOf(length));
            this.mCis[length].registerForAvailable(this, 6, Integer.valueOf(length));
            this.mCis[length].registerForNotAvailable(this, 7, Integer.valueOf(length));
            this.mCis[length].registerForIccRefresh(this, 8, Integer.valueOf(length));
            length++;
        }
    }

    public void updateUiccCommandsInterface(CommandsInterface[] commandsInterfaceArr, boolean z) {
        if (z) {
            this.mCis = commandsInterfaceArr;
            int i = 0;
            while (true) {
                CommandsInterface[] commandsInterfaceArr2 = this.mCis;
                if (i >= commandsInterfaceArr2.length) {
                    break;
                }
                this.mPhoneIdToSlotId[i] = -1;
                commandsInterfaceArr2[i].registerForIccStatusChanged(this, 1, Integer.valueOf(i));
                i++;
            }
        } else {
            this.mCis = PhoneFactory.getCommandsInterfaces();
        }
        logl("updateUiccCommandsInterface : ntnMode " + z);
    }

    protected Integer getCiIndex(Message message) {
        Object obj;
        Integer num = new Integer(0);
        if (message != null) {
            Object obj2 = message.obj;
            if (obj2 != null && (obj2 instanceof Integer)) {
                return (Integer) obj2;
            }
            if (obj2 != null && (obj2 instanceof AsyncResult) && (obj = ((AsyncResult) obj2).userObj) != null && (obj instanceof Integer)) {
                return (Integer) obj;
            }
        }
        return num;
    }

    private static String eventToString(int i) {
        switch (i) {
            case 1:
                return "ICC_STATUS_CHANGED";
            case 2:
                return "SLOT_STATUS_CHANGED";
            case 3:
                return "GET_ICC_STATUS_DONE";
            case 4:
                return "GET_SLOT_STATUS_DONE";
            case 5:
                return "RADIO_ON";
            case 6:
                return "RADIO_AVAILABLE";
            case 7:
                return "RADIO_UNAVAILABLE";
            case 8:
                return "SIM_REFRESH";
            case 9:
                return "EID_READY";
            case 10:
                return "MULTI_SIM_CONFIG_CHANGED";
            case 11:
                return "GET_SIM_TYPE_INFO";
            default:
                return "UNKNOWN(" + i + ")";
        }
    }

    public UiccCardApplication getUiccCardApplication(int i, int i2) {
        synchronized (mLock) {
            try {
                UiccPort uiccPortForPhone = getUiccPortForPhone(i);
                if (uiccPortForPhone == null) {
                    return null;
                }
                return uiccPortForPhone.getApplication(i2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.uicc.UiccController$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$IccCardConstants$State;

        static {
            int[] iArr = new int[IccCardConstants.State.values().length];
            $SwitchMap$com$android$internal$telephony$IccCardConstants$State = iArr;
            try {
                iArr[IccCardConstants.State.ABSENT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.PIN_REQUIRED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.PUK_REQUIRED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.NETWORK_LOCKED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.READY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.NOT_READY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.PERM_DISABLED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.CARD_IO_ERROR.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.CARD_RESTRICTED.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.LOADED.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.UNKNOWN.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    public static String getIccStateIntentString(IccCardConstants.State state) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$IccCardConstants$State[state.ordinal()]) {
            case 1:
                return "ABSENT";
            case 2:
            case 3:
            case 4:
                return "LOCKED";
            case 5:
                return "READY";
            case 6:
                return "NOT_READY";
            case 7:
                return "LOCKED";
            case 8:
                return "CARD_IO_ERROR";
            case 9:
                return "CARD_RESTRICTED";
            case 10:
                return "LOADED";
            default:
                return SatelliteConstants.DEFAULT_PLMN;
        }
    }

    public void updateSimStateForInactivePort(final int i, final String str) {
        post(new Runnable() { // from class: com.android.internal.telephony.uicc.UiccController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateSimStateForInactivePort$1(i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSimStateForInactivePort$1(int i, String str) {
        if (SubscriptionManager.isValidPhoneId(i)) {
            this.mTelephonyManager.setSimStateForPhone(i, IccCardConstants.State.ABSENT.toString());
        }
        SubscriptionManagerService.getInstance().updateSimStateForInactivePort(i, TextUtils.emptyIfNull(str));
    }

    private void broadcastSimStateChanged(int i, String str, String str2) {
        Intent intent = new Intent("android.intent.action.SIM_STATE_CHANGED");
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        intent.putExtra("phoneName", "Phone");
        intent.putExtra("ss", str);
        intent.putExtra("reason", str2);
        int subscriptionId = SubscriptionManager.getSubscriptionId(i);
        SubscriptionManager.putPhoneIdAndMaybeSubIdExtra(intent, i, subscriptionId);
        log("Broadcasting intent ACTION_SIM_STATE_CHANGED " + str + " reason " + str2 + " for phone: " + i + " sub: " + subscriptionId);
        IntentBroadcaster.getInstance().broadcastStickyIntent(this.mContext, intent, i);
    }

    private void broadcastSimCardStateChanged(int i, int i2) {
        int portIndexFromPhoneId;
        int[] iArr = this.mSimCardState;
        if (i2 != iArr[i]) {
            iArr[i] = i2;
            Intent intent = new Intent("android.telephony.action.SIM_CARD_STATE_CHANGED");
            intent.addFlags(67108864);
            intent.putExtra("android.telephony.extra.SIM_STATE", i2);
            int subscriptionId = SubscriptionManager.getSubscriptionId(i);
            SubscriptionManager.putPhoneIdAndMaybeSubIdExtra(intent, i, subscriptionId);
            UiccSlot uiccSlotForPhone = getInstance().getUiccSlotForPhone(i);
            int slotIdFromPhoneId = getInstance().getSlotIdFromPhoneId(i);
            intent.putExtra("slot", slotIdFromPhoneId);
            if (uiccSlotForPhone != null) {
                portIndexFromPhoneId = uiccSlotForPhone.getPortIndexFromPhoneId(i);
                intent.putExtra("port", portIndexFromPhoneId);
            } else {
                portIndexFromPhoneId = -1;
            }
            log("Broadcasting intent ACTION_SIM_CARD_STATE_CHANGED " + TelephonyManager.simStateToString(i2) + " for phone: " + i + " slot: " + slotIdFromPhoneId + " port: " + portIndexFromPhoneId + " sub: " + subscriptionId);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE");
        }
    }

    private void broadcastSimApplicationStateChanged(int i, int i2) {
        boolean z = false;
        boolean z2 = this.mSimApplicationState[i] == 0 && i2 == 6;
        IccCard iccCard = PhoneFactory.getPhone(i).getIccCard();
        if (iccCard != null && iccCard.isEmptyProfile()) {
            z = true;
        }
        int[] iArr = this.mSimApplicationState;
        if (i2 != iArr[i]) {
            if (!z2 || z) {
                iArr[i] = i2;
                Intent intent = new Intent("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
                intent.addFlags(67108864);
                intent.putExtra("android.telephony.extra.SIM_STATE", i2);
                int subscriptionId = SubscriptionManager.getSubscriptionId(i);
                SubscriptionManager.putPhoneIdAndMaybeSubIdExtra(intent, i, subscriptionId);
                UiccSlot uiccSlotForPhone = getInstance().getUiccSlotForPhone(i);
                int slotIdFromPhoneId = getInstance().getSlotIdFromPhoneId(i);
                intent.putExtra("slot", slotIdFromPhoneId);
                if (uiccSlotForPhone != null) {
                    intent.putExtra("port", uiccSlotForPhone.getPortIndexFromPhoneId(i));
                }
                log("Broadcasting intent ACTION_SIM_APPLICATION_STATE_CHANGED " + TelephonyManager.simStateToString(i2) + " for phone: " + i + " slot: " + slotIdFromPhoneId + " port: " + (uiccSlotForPhone != null ? uiccSlotForPhone.getPortIndexFromPhoneId(i) : -1) + " sub: " + subscriptionId);
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE");
            }
        }
    }

    private static int getSimStateFromLockedReason(String str) {
        str.hashCode();
        switch (str) {
            case "NETWORK":
                return 4;
            case "PIN":
                return 2;
            case "PUK":
                return 3;
            case "PERM_DISABLED":
                return 7;
            default:
                return 0;
        }
    }

    private void broadcastSimStateEvents(int i, IccCardConstants.State state, String str) {
        String iccStateIntentString = getIccStateIntentString(state);
        int simStateFromLockedReason = 6;
        int i2 = 11;
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$IccCardConstants$State[state.ordinal()]) {
            case 1:
                i2 = 1;
                simStateFromLockedReason = 0;
                break;
            case 2:
            case 3:
            case 4:
            case 7:
                simStateFromLockedReason = getSimStateFromLockedReason(str);
                break;
            case 5:
            case 6:
                break;
            case 8:
                i2 = 8;
                break;
            case 9:
                i2 = 9;
                break;
            case 10:
                simStateFromLockedReason = 10;
                break;
            default:
                simStateFromLockedReason = 0;
                i2 = 0;
                break;
        }
        broadcastSimStateChanged(i, iccStateIntentString, str);
        broadcastSimCardStateChanged(i, i2);
        broadcastSimApplicationStateChanged(i, simStateFromLockedReason);
    }

    private void updateCarrierServices(int i, String str) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.updateConfigForPhoneId(i, str);
        }
        this.mCarrierServiceBindHelper.updateForPhoneId(i, str);
    }

    public void updateSimState(final int i, final IccCardConstants.State state, final String str) {
        post(new Runnable() { // from class: com.android.internal.telephony.uicc.UiccController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateSimState$3(i, state, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSimState$3(final int i, final IccCardConstants.State state, final String str) {
        logl("updateSimState: phoneId=" + i + ", state=" + state + ", reason=" + str);
        if (!SubscriptionManager.isValidPhoneId(i)) {
            logel("updateSimState: Invalid phone id " + i);
        } else {
            this.mTelephonyManager.setSimStateForPhone(i, state.toString());
            final String iccStateIntentString = getIccStateIntentString(state);
            final int iOrdinal = state.ordinal();
            SubscriptionManagerService.getInstance().updateSimState(i, iOrdinal, new Executor() { // from class: com.android.internal.telephony.uicc.UiccController$$ExternalSyntheticLambda4
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new Runnable() { // from class: com.android.internal.telephony.uicc.UiccController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateSimState$2(i, state, str, iOrdinal, iccStateIntentString);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSimState$2(int i, IccCardConstants.State state, String str, int i2, String str2) {
        broadcastSimStateEvents(i, state, str);
        UiccProfile uiccProfileForPhone = getUiccProfileForPhone(i);
        if (i2 == 5) {
            return;
        }
        if (i2 == 6 && uiccProfileForPhone != null && !uiccProfileForPhone.isEmptyProfile() && SubscriptionManagerService.getInstance().areUiccAppsEnabledOnCard(i)) {
            log("updateSimState: SIM_STATE_NOT_READY is not a final state.");
            return;
        }
        if (!SubscriptionManager.isValidPhoneId(i)) {
            logel("updateSimState: Cannot update carrier services. Invalid phone id " + i);
        } else {
            logl("updateSimState: resolve carrier id and update carrier services.");
            PhoneFactory.getPhone(i).resolveSubscriptionCarrierId(str2);
            updateCarrierServices(i, str2);
        }
    }

    protected synchronized void onGetIccCardStatusDone(AsyncResult asyncResult, Integer num) {
        String iccId;
        int i;
        if (asyncResult.exception != null) {
            logel("Error getting ICC status. RIL_REQUEST_GET_ICC_STATUS should never return an error " + asyncResult.exception);
            return;
        }
        if (!isValidPhoneIndex(num.intValue())) {
            logel("onGetIccCardStatusDone: invalid index : " + num);
            return;
        }
        if (isShuttingDown()) {
            log("onGetIccCardStatusDone: shutdown in progress ignore event");
            return;
        }
        Object obj = asyncResult.result;
        if (!(obj instanceof IccCardStatus)) {
            logel("onGetIccCardStatusDone: Expected IccCardStatus but got ".concat(obj.getClass().getName()));
            AnomalyReporter.reportAnomaly(UUID.fromString("52a70831-c9e7-465b-944d-bfc7968f31e1"), "Unexpected result type while onGetIccCardStatusDone".concat(asyncResult.result.getClass().getName()));
            return;
        }
        IccCardStatus iccCardStatus = (IccCardStatus) obj;
        String str = iccCardStatus.iccid;
        logl("onGetIccCardStatusDone: phoneId-" + num + " IccCardStatus: " + iccCardStatus);
        int iIntValue = iccCardStatus.mSlotPortMapping.mPhysicalSlotIndex;
        if (iIntValue == -1) {
            iIntValue = num.intValue();
        }
        this.mPhoneIdToSlotId[num.intValue()] = iIntValue;
        if (this.mUiccSlots[iIntValue] == null) {
            setUiccSlot(iIntValue, TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeUiccSlot(this.mContext, true, this.mFeatureFlags));
        }
        this.mUiccSlots[iIntValue].update(this.mCis[num.intValue()], iccCardStatus, num.intValue(), iIntValue);
        UiccCard uiccCard = this.mUiccSlots[iIntValue].getUiccCard();
        if (uiccCard == null) {
            log("mUiccSlots[" + iIntValue + "] has no card. Notifying IccChangedRegistrants");
            this.mIccChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, num, (Throwable) null));
            return;
        }
        if (uiccCard.getUiccPort(iccCardStatus.mSlotPortMapping.mPortIndex) == null) {
            log("mUiccSlots[" + iIntValue + "] has no UiccPort with index[" + iccCardStatus.mSlotPortMapping.mPortIndex + "]. Notifying IccChangedRegistrants");
            this.mIccChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, num, (Throwable) null));
            return;
        }
        boolean zIsEuicc = this.mUiccSlots[iIntValue].isEuicc();
        if (zIsEuicc) {
            iccId = ((EuiccCard) uiccCard).getEid();
        } else {
            iccId = uiccCard.getUiccPort(iccCardStatus.mSlotPortMapping.mPortIndex).getIccId();
        }
        if (iccId != null) {
            addCardId(iccId);
        }
        if (zIsEuicc && (i = this.mDefaultEuiccCardId) != -1) {
            if (iccId == null) {
                ((EuiccCard) uiccCard).registerForEidReady(this, 9, num);
            } else if (i == -2 || i == -3) {
                this.mDefaultEuiccCardId = convertToPublicCardId(iccId);
                logl("IccCardStatus eid=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, iccId) + " slot=" + iIntValue + " mDefaultEuiccCardId=" + this.mDefaultEuiccCardId);
            }
        }
        log("Notifying IccChangedRegistrants");
        this.mIccChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, num, (Throwable) null));
    }

    private void addCardId(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.length() < 32) {
            str = IccUtils.stripTrailingFs(str);
        }
        if (this.mCardStrings.contains(str)) {
            return;
        }
        this.mCardStrings.add(str);
        saveCardStrings();
    }

    public String convertToCardString(int i) {
        if (i < 0 || i >= this.mCardStrings.size()) {
            log("convertToCardString: cardId " + i + " is not valid");
            return null;
        }
        return (String) this.mCardStrings.get(i);
    }

    public int convertToPublicCardId(String str) {
        if (this.mDefaultEuiccCardId == -1) {
            return -1;
        }
        if (TextUtils.isEmpty(str)) {
            return -2;
        }
        if (str.length() < 32) {
            str = IccUtils.stripTrailingFs(str);
        }
        int iIndexOf = this.mCardStrings.indexOf(str);
        if (iIndexOf == -1) {
            return -2;
        }
        return iIndexOf;
    }

    public ArrayList<UiccCardInfo> getAllUiccCardInfos() {
        ArrayList<UiccCardInfo> arrayList;
        int iConvertToPublicCardId;
        synchronized (mLock) {
            try {
                arrayList = new ArrayList<>();
                int i = 0;
                int i2 = 0;
                while (true) {
                    UiccSlot[] uiccSlotArr = this.mUiccSlots;
                    if (i2 < uiccSlotArr.length) {
                        UiccSlot uiccSlot = uiccSlotArr[i2];
                        if (uiccSlot != null) {
                            boolean zIsEuicc = uiccSlot.isEuicc();
                            UiccCard uiccCard = uiccSlot.getUiccCard();
                            boolean zIsRemovable = uiccSlot.isRemovable();
                            String eid = null;
                            if (uiccCard == null) {
                                String iccId = uiccSlot.getIccId(i);
                                iConvertToPublicCardId = (zIsEuicc || TextUtils.isEmpty(iccId)) ? -2 : convertToPublicCardId(iccId);
                            } else if (zIsEuicc) {
                                eid = ((EuiccCard) uiccCard).getEid();
                                iConvertToPublicCardId = convertToPublicCardId(eid);
                            } else {
                                UiccPort uiccPort = uiccCard.getUiccPort(i);
                                if (uiccPort == null) {
                                    AnomalyReporter.reportAnomaly(UUID.fromString("92885ba7-98bb-490a-ba19-987b1c8b2055"), "UiccController: Found UiccPort Null object.");
                                }
                                iConvertToPublicCardId = convertToPublicCardId(uiccPort != null ? uiccPort.getIccId() : null);
                            }
                            ArrayList arrayList2 = new ArrayList();
                            int[] portList = uiccSlot.getPortList();
                            int length = portList.length;
                            int i3 = i;
                            while (i3 < length) {
                                int i4 = portList[i3];
                                arrayList2.add(new UiccPortInfo(IccUtils.stripTrailingFs(uiccSlot.getIccId(i4)), i4, uiccSlot.getPhoneIdFromPortIndex(i4), uiccSlot.isPortActive(i4)));
                                i3++;
                                zIsEuicc = zIsEuicc;
                            }
                            arrayList.add(new UiccCardInfo(zIsEuicc, iConvertToPublicCardId, eid, i2, zIsRemovable, uiccSlot.isMultipleEnabledProfileSupported(), arrayList2));
                        }
                        i2++;
                        i = 0;
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return arrayList;
    }

    public int getCardIdForDefaultEuicc() {
        if (this.mDefaultEuiccCardId == -3) {
            return -1;
        }
        if (this.mUseRemovableEsimAsDefault && !TelephonyUtils.IS_USER) {
            for (UiccSlot uiccSlot : this.mUiccSlots) {
                if (uiccSlot != null && uiccSlot.isRemovable() && uiccSlot.isEuicc() && uiccSlot.isActive()) {
                    int iConvertToPublicCardId = convertToPublicCardId(uiccSlot.getEid());
                    log("getCardIdForDefaultEuicc: Removable eSIM is default, cardId: " + iConvertToPublicCardId);
                    return iConvertToPublicCardId;
                }
            }
            log("getCardIdForDefaultEuicc: No removable eSIM slot is found");
        }
        return this.mDefaultEuiccCardId;
    }

    public PinStorage getPinStorage() {
        return this.mPinStorage;
    }

    private ArrayList loadCardStrings() {
        String string = PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("card_strings", "");
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    private void saveCardStrings() {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editorEdit.putString("card_strings", TextUtils.join(",", this.mCardStrings));
        editorEdit.commit();
    }

    private synchronized void onGetSlotStatusDone(AsyncResult asyncResult) {
        try {
            if (this.mIsSlotStatusSupported) {
                Throwable th = asyncResult.exception;
                if (th != null) {
                    if ((th instanceof CommandException) && ((CommandException) th).getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                        logl("onGetSlotStatusDone: request not supported; marking mIsSlotStatusSupported to false");
                        this.mIsSlotStatusSupported = false;
                    } else {
                        logel("Unexpected error getting slot status: " + asyncResult.exception);
                    }
                    return;
                }
                if (isShuttingDown()) {
                    log("onGetSlotStatusDone: shudown in progress ignore event");
                    return;
                }
                ArrayList<IccSlotStatus> arrayList = (ArrayList) asyncResult.result;
                if (!slotStatusChanged(arrayList)) {
                    log("onGetSlotStatusDone: No change in slot status");
                    return;
                }
                logl("onGetSlotStatusDone: " + arrayList);
                if (this.mRadioConfig.isGetOrSetSimTypeSupported()) {
                    logl("onGetSlotStatusDone: request simType info");
                    this.mRadioConfig.getSimTypeInfo(obtainMessage(11));
                }
                sLastSlotStatus = arrayList;
                this.mHasActiveBuiltInEuicc = false;
                int size = arrayList.size();
                if (this.mUiccSlots.length < size) {
                    logel("The number of the physical slots reported " + size + " is greater than the expectation " + this.mUiccSlots.length);
                    size = this.mUiccSlots.length;
                }
                boolean z = false;
                int i = 0;
                boolean z2 = false;
                for (int i2 = 0; i2 < size; i2++) {
                    IccSlotStatus iccSlotStatus = arrayList.get(i2);
                    boolean zHasActivePort = hasActivePort(iccSlotStatus.mSimPortInfos);
                    if (this.mUiccSlots[i2] == null) {
                        setUiccSlot(i2, TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeUiccSlot(this.mContext, true, this.mFeatureFlags));
                    }
                    if (zHasActivePort) {
                        int i3 = 0;
                        while (true) {
                            IccSimPortInfo[] iccSimPortInfoArr = iccSlotStatus.mSimPortInfos;
                            if (i3 >= iccSimPortInfoArr.length) {
                                break;
                            }
                            IccSimPortInfo iccSimPortInfo = iccSimPortInfoArr[i3];
                            if (iccSimPortInfo.mPortActive) {
                                int i4 = iccSimPortInfo.mLogicalSlotIndex;
                                if (isValidPhoneIndex(i4)) {
                                    this.mPhoneIdToSlotId[i4] = i2;
                                } else {
                                    logel("Skipping slot " + i2 + " portIndex " + i3 + " as phone " + i4 + " is not available to communicate with this slot");
                                }
                                i++;
                            }
                            i3++;
                        }
                    }
                    this.mUiccSlots[i2].update(this.mCis, iccSlotStatus, i2);
                    if (this.mUiccSlots[i2].isEuicc()) {
                        if (zHasActivePort) {
                            if (isBuiltInEuiccSlot(i2)) {
                                this.mHasActiveBuiltInEuicc = true;
                            }
                            z2 = true;
                        }
                        String str = iccSlotStatus.eid;
                        if (!TextUtils.isEmpty(str)) {
                            addCardId(str);
                            if (!this.mUiccSlots[i2].isRemovable() && !z) {
                                this.mDefaultEuiccCardId = convertToPublicCardId(str);
                                logl("Using eid=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str) + " in slot=" + i2 + " to set mDefaultEuiccCardId=" + this.mDefaultEuiccCardId);
                                z = true;
                            }
                        }
                    }
                }
                if (!this.mHasActiveBuiltInEuicc && !z) {
                    for (int i5 = 0; i5 < size; i5++) {
                        if (this.mUiccSlots[i5].isEuicc()) {
                            String str2 = arrayList.get(i5).eid;
                            if (!TextUtils.isEmpty(str2)) {
                                this.mDefaultEuiccCardId = convertToPublicCardId(str2);
                                logl("Using eid=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, str2) + " from removable eUICC in slot=" + i5 + " to set mDefaultEuiccCardId=" + this.mDefaultEuiccCardId);
                                z = true;
                                break;
                            }
                        }
                    }
                }
                if (this.mHasBuiltInEuicc && !z2 && !z) {
                    logl("onGetSlotStatusDone: mDefaultEuiccCardId=TEMPORARILY_UNSUPPORTED_CARD_ID");
                    this.mDefaultEuiccCardId = -3;
                    z = true;
                }
                if (!z) {
                    int i6 = this.mDefaultEuiccCardId;
                    if (i6 >= 0) {
                        String str3 = (String) this.mCardStrings.get(i6);
                        boolean z3 = false;
                        for (UiccSlot uiccSlot : this.mUiccSlots) {
                            if (uiccSlot.getUiccCard() != null && str3.equals(IccUtils.stripTrailingFs(uiccSlot.getUiccCard().getCardId()))) {
                                z3 = true;
                            }
                        }
                        if (!z3) {
                            logl("onGetSlotStatusDone: mDefaultEuiccCardId=" + this.mDefaultEuiccCardId + " is no longer inserted. Setting mDefaultEuiccCardId=UNINITIALIZED");
                            this.mDefaultEuiccCardId = -2;
                        }
                    } else {
                        logl("onGetSlotStatusDone: mDefaultEuiccCardId=UNINITIALIZED");
                        this.mDefaultEuiccCardId = -2;
                    }
                }
                if (i != this.mPhoneIdToSlotId.length) {
                    logel("Number of active ports " + i + " does not match the number of Phones" + this.mPhoneIdToSlotId.length);
                }
                BroadcastOptions broadcastOptionsMakeBasic = BroadcastOptions.makeBasic();
                broadcastOptionsMakeBasic.setBackgroundActivityStartsAllowed(true);
                Intent intent = new Intent("android.telephony.action.SIM_SLOT_STATUS_CHANGED");
                intent.addFlags(67108864);
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE", broadcastOptionsMakeBasic.toBundle());
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    private boolean hasActivePort(IccSimPortInfo[] iccSimPortInfoArr) {
        for (IccSimPortInfo iccSimPortInfo : iccSimPortInfoArr) {
            if (iccSimPortInfo.mPortActive) {
                return true;
            }
        }
        return false;
    }

    public boolean slotStatusChanged(ArrayList<IccSlotStatus> arrayList) {
        ArrayList<IccSlotStatus> arrayList2 = sLastSlotStatus;
        if (arrayList2 == null || arrayList2.size() != arrayList.size()) {
            return true;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (!sLastSlotStatus.get(i).equals(arrayList.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void onSimRefresh(AsyncResult asyncResult, Integer num) {
        if (asyncResult.exception != null) {
            logel("onSimRefresh: Sim REFRESH with exception: " + asyncResult.exception);
            return;
        }
        if (!isValidPhoneIndex(num.intValue())) {
            logel("onSimRefresh: invalid index : " + num);
            return;
        }
        IccRefreshResponse iccRefreshResponse = (IccRefreshResponse) asyncResult.result;
        logl("onSimRefresh: index " + num + ", " + iccRefreshResponse);
        if (iccRefreshResponse == null) {
            logel("onSimRefresh: received without input");
            return;
        }
        if (getUiccCardForPhone(num.intValue()) == null) {
            logel("onSimRefresh: refresh on null card : " + num);
            return;
        }
        UiccPort uiccPortForPhone = getUiccPortForPhone(num.intValue());
        if (uiccPortForPhone == null) {
            logel("onSimRefresh: refresh on null port : " + num);
            return;
        }
        int i = iccRefreshResponse.refreshResult;
        boolean zResetAppWithAid = false;
        if (i == 1) {
            zResetAppWithAid = uiccPortForPhone.resetAppWithAid(iccRefreshResponse.aid, false);
        } else if (i == 2) {
            zResetAppWithAid = uiccPortForPhone.resetAppWithAid(iccRefreshResponse.aid, true);
        } else if (i != 4) {
            return;
        }
        if (zResetAppWithAid && iccRefreshResponse.refreshResult == 2) {
            MiuiUiccControllerStub.get(num.intValue()).onSimRefresh(num.intValue(), getUiccProfileForPhone(num.intValue()), this.mUiccSlots[num.intValue()]);
            ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).updateConfigForPhoneId(num.intValue(), SatelliteConstants.DEFAULT_PLMN);
        }
        this.mCis[num.intValue()].getIccCardStatus(obtainMessage(3, num));
    }

    private void onEidReady(AsyncResult asyncResult, Integer num) {
        if (asyncResult.exception != null) {
            logel("onEidReady: exception: " + asyncResult.exception);
            return;
        }
        if (!isValidPhoneIndex(num.intValue())) {
            logel("onEidReady: invalid index: " + num);
            return;
        }
        int i = this.mPhoneIdToSlotId[num.intValue()];
        EuiccCard euiccCard = (EuiccCard) this.mUiccSlots[i].getUiccCard();
        if (euiccCard == null) {
            logel("onEidReady: UiccCard in slot " + i + " is null");
            return;
        }
        String eid = euiccCard.getEid();
        addCardId(eid);
        int i2 = this.mDefaultEuiccCardId;
        if (i2 == -2 || i2 == -3) {
            if (!this.mUiccSlots[i].isRemovable()) {
                this.mDefaultEuiccCardId = convertToPublicCardId(eid);
                logl("onEidReady: eid=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, eid) + " slot=" + i + " mDefaultEuiccCardId=" + this.mDefaultEuiccCardId);
            } else if (!this.mHasActiveBuiltInEuicc) {
                this.mDefaultEuiccCardId = convertToPublicCardId(eid);
                logl("onEidReady: eid=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, eid) + " from removable eUICC in slot=" + i + " mDefaultEuiccCardId=" + this.mDefaultEuiccCardId);
            }
        }
        euiccCard.unregisterForEidReady(this);
    }

    private boolean hasBuiltInEuicc() {
        int[] iArr = this.mEuiccSlots;
        return iArr != null && iArr.length > 0;
    }

    private boolean isBuiltInEuiccSlot(int i) {
        if (!this.mHasBuiltInEuicc) {
            return false;
        }
        for (int i2 : this.mEuiccSlots) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPhoneIndex(int i) {
        return i >= 0 && i < this.mTelephonyManager.getActiveModemCount();
    }

    private boolean isValidSlotIndex(int i) {
        return i >= 0 && i < this.mUiccSlots.length;
    }

    private boolean isShuttingDown() {
        for (int i = 0; i < this.mTelephonyManager.getActiveModemCount(); i++) {
            if (PhoneFactory.getPhone(i) != null && PhoneFactory.getPhone(i).isShuttingDown()) {
                return true;
            }
        }
        return false;
    }

    private static boolean iccidMatches(String str, String str2) {
        for (String str3 : str.split(",")) {
            if (str2.startsWith(str3)) {
                Log.d("UiccController", "mvno icc id match found");
                return true;
            }
        }
        return false;
    }

    private static boolean imsiMatches(String str, String str2) {
        if (str.contains(",")) {
            for (String str3 : str.split(",")) {
                if (imsiMatches(str3, str2)) {
                    return true;
                }
            }
            return false;
        }
        int length = str.length();
        if (length <= 0 || length > str2.length()) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt != 'x' && cCharAt != 'X' && cCharAt != str2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean mvnoMatches(int i, int i2, String str) {
        String iccId;
        IccRecords iccRecords = getIccRecords(i, 1);
        if (iccRecords == null) {
            Log.d("UiccController", "isMvnoMatched# IccRecords is null");
            return false;
        }
        if (i2 == 0) {
            String serviceProviderNameWithBrandOverride = iccRecords.getServiceProviderNameWithBrandOverride();
            if (serviceProviderNameWithBrandOverride != null && serviceProviderNameWithBrandOverride.equalsIgnoreCase(str)) {
                return true;
            }
        } else if (i2 == 1) {
            String imsi = iccRecords.getIMSI();
            if (imsi != null && imsiMatches(str, imsi)) {
                return true;
            }
        } else if (i2 == 2) {
            if (MiuiUiccControllerStub.get(i).mvnoMatchesCustomized(i, iccRecords, "gid", str)) {
                return true;
            }
            String gid1 = iccRecords.getGid1();
            int length = str.length();
            if (gid1 != null && gid1.length() >= length && gid1.substring(0, length).equalsIgnoreCase(str)) {
                return true;
            }
        } else if (i2 == 3 && (iccId = iccRecords.getIccId()) != null && iccidMatches(str, iccId)) {
            return true;
        }
        return false;
    }

    public void setRemovableEsimAsDefaultEuicc(boolean z) {
        this.mUseRemovableEsimAsDefault = z;
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editorEdit.putBoolean("removable_esim", z);
        editorEdit.apply();
        log("setRemovableEsimAsDefaultEuicc isDefault: " + z);
    }

    public boolean isRemovableEsimDefaultEuicc() {
        log("mUseRemovableEsimAsDefault: " + this.mUseRemovableEsimAsDefault);
        return this.mUseRemovableEsimAsDefault;
    }

    public IccSlotStatus.MultipleEnabledProfilesMode getSupportedMepMode(int i) {
        IccSlotStatus.MultipleEnabledProfilesMode supportedMepMode;
        synchronized (mLock) {
            try {
                UiccSlot uiccSlot = getUiccSlot(i);
                if (uiccSlot != null) {
                    supportedMepMode = uiccSlot.getSupportedMepMode();
                } else {
                    supportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.NONE;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return supportedMepMode;
    }

    private void log(String str) {
        Rlog.d("UiccController", str);
    }

    private void logl(String str) {
        Rlog.d("UiccController", str);
        sLocalLog.log("UiccController: " + str);
    }

    private void logel(String str) {
        Rlog.e("UiccController", str);
        sLocalLog.log("UiccController: " + str);
    }

    public static void addLocalLog(String str) {
        sLocalLog.log(str);
    }

    public IccSlotStatus.MultipleEnabledProfilesMode getSavedOrSupportedMepMode(int i) {
        IccSlotStatus.MultipleEnabledProfilesMode supportedMepMode;
        synchronized (mLock) {
            try {
                IccSlotStatus.MultipleEnabledProfilesMode currentSavedMepMode = getCurrentSavedMepMode(i);
                if (currentSavedMepMode != null) {
                    return currentSavedMepMode;
                }
                UiccSlot uiccSlot = getUiccSlot(i);
                if (uiccSlot != null) {
                    supportedMepMode = uiccSlot.getSupportedMepMode();
                } else {
                    supportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.NONE;
                }
                return supportedMepMode;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private List getPrintableCardStrings() {
        boolean zIsEmpty = ArrayUtils.isEmpty(this.mCardStrings);
        ArrayList arrayList = this.mCardStrings;
        return !zIsEmpty ? (List) arrayList.stream().map(new Function() { // from class: com.android.internal.telephony.uicc.UiccController$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SubscriptionInfo.getPrintableId((String) obj);
            }
        }).collect(Collectors.toList()) : arrayList;
    }

    public void dispose() {
        for (UiccSlot uiccSlot : this.mUiccSlots) {
            uiccSlot.dispose();
        }
        this.mUiccSlots = null;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("mHasBuiltInEuicc=" + this.mHasBuiltInEuicc);
        androidUtilIndentingPrintWriter.println("mHasActiveBuiltInEuicc=" + this.mHasActiveBuiltInEuicc);
        androidUtilIndentingPrintWriter.println("mCardStrings=" + getPrintableCardStrings());
        androidUtilIndentingPrintWriter.println("mDefaultEuiccCardId=" + this.mDefaultEuiccCardId);
        androidUtilIndentingPrintWriter.println("mPhoneIdToSlotId=" + Arrays.toString(this.mPhoneIdToSlotId));
        androidUtilIndentingPrintWriter.println("mUseRemovableEsimAsDefault=" + this.mUseRemovableEsimAsDefault);
        androidUtilIndentingPrintWriter.println("mUiccSlots: size=" + this.mUiccSlots.length);
        androidUtilIndentingPrintWriter.increaseIndent();
        int i = 0;
        while (true) {
            UiccSlot[] uiccSlotArr = this.mUiccSlots;
            if (i < uiccSlotArr.length) {
                if (uiccSlotArr[i] == null) {
                    androidUtilIndentingPrintWriter.println("mUiccSlots[" + i + "]=null");
                } else {
                    androidUtilIndentingPrintWriter.println("mUiccSlots[" + i + "]:");
                    androidUtilIndentingPrintWriter.increaseIndent();
                    this.mUiccSlots[i].dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
                    androidUtilIndentingPrintWriter.decreaseIndent();
                }
                i++;
            } else {
                androidUtilIndentingPrintWriter.decreaseIndent();
                androidUtilIndentingPrintWriter.println();
                this.mCarrierServiceBindHelper.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
                androidUtilIndentingPrintWriter.println();
                androidUtilIndentingPrintWriter.println("sLocalLog= ");
                androidUtilIndentingPrintWriter.increaseIndent();
                this.mPinStorage.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
                sLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
                return;
            }
        }
    }

    public void getSimSlotsStatus() {
        this.mRadioConfig.getSimSlotsStatus(obtainMessage(4, 0));
    }

    public void getIccCardStatus() {
        int i = 0;
        if (!SystemProperties.getBoolean("persist.vendor.radio.tele.optimization", false)) {
            return;
        }
        while (true) {
            CommandsInterface[] commandsInterfaceArr = this.mCis;
            if (i >= commandsInterfaceArr.length) {
                return;
            }
            if (commandsInterfaceArr[i].getRadioState() != 2) {
                this.mCis[i].getIccCardStatus(obtainMessage(3, Integer.valueOf(i)));
            }
            i++;
        }
    }
}
