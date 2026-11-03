package com.android.internal.telephony.uicc;

import android.R;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.UserHandle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RadioConfig$$ExternalSyntheticLambda0;
import com.android.internal.telephony.data.TelephonyNetworkRequest$$ExternalSyntheticLambda1;
import com.android.internal.telephony.nano.TelephonyProto$TelephonyEvent;
import com.android.internal.telephony.uicc.euicc.EuiccCard;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class UiccSlot extends Handler {
    public static final String EXTRA_ICC_CARD_ADDED = "com.android.internal.telephony.uicc.ICC_CARD_ADDED";
    public static final int INVALID_PHONE_ID = -1;
    public static final int VOLTAGE_CLASS_A = 1;
    public static final int VOLTAGE_CLASS_B = 2;
    public static final int VOLTAGE_CLASS_C = 3;
    public static final int VOLTAGE_CLASS_UNKNOWN = 0;
    private boolean mActive;
    private AnswerToReset mAtr;
    private Context mContext;
    private String mEid;
    private final FeatureFlags mFeatureFlags;
    private boolean mIsEuicc;
    private boolean mIsRemovable;
    private int mMinimumVoltageClass;
    private IccSlotStatus.MultipleEnabledProfilesMode mSupportedMepMode;
    private int[] mSupportedSimTypes;
    protected UiccCard mUiccCard;
    private final Object mLock = new Object();
    private boolean mStateIsUnknown = true;
    private HashMap mPortIdxToPhoneId = new HashMap();
    protected HashMap<Integer, Integer> mLastRadioState = new HashMap<>();
    private HashMap mIccIds = new HashMap();
    protected HashMap<Integer, IccCardStatus.CardState> mCardState = new HashMap<>();
    private int mActiveSimType = -1;

    /* JADX INFO: Access modifiers changed from: private */
    public int getMappedSimType(int i) {
        if (i == 0) {
            return -1;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return -1;
            }
        }
        return i2;
    }

    public void resetPortAndState() {
    }

    public UiccSlot(Context context, boolean z, FeatureFlags featureFlags) {
        log("Creating");
        this.mContext = context;
        this.mActive = z;
        this.mFeatureFlags = featureFlags;
        this.mSupportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.NONE;
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0107  */
    /* JADX WARN: Code duplicated, block: B:50:0x01d0 A[Catch: all -> 0x0082, TryCatch #0 {all -> 0x0082, blocks: (B:4:0x0015, B:6:0x0071, B:8:0x0077, B:11:0x0085, B:13:0x00d9, B:51:0x01d5, B:52:0x01e6, B:16:0x00ea, B:18:0x00fe, B:20:0x0102, B:48:0x01cc, B:50:0x01d0, B:24:0x010b, B:28:0x0121, B:30:0x013b, B:31:0x0153, B:33:0x0157, B:35:0x015b, B:37:0x0165, B:38:0x017b, B:40:0x017f, B:41:0x018f, B:43:0x0197, B:44:0x01ae, B:46:0x01b2, B:47:0x01c4), top: B:56:0x0015 }] */
    public void update(CommandsInterface commandsInterface, IccCardStatus iccCardStatus, int i, int i2) {
        UiccCard uiccCard;
        synchronized (this.mLock) {
            try {
                int iIntValue = ((Integer) this.mPortIdxToPhoneId.getOrDefault(Integer.valueOf(iccCardStatus.mSlotPortMapping.mPortIndex), -1)).intValue();
                this.mPortIdxToPhoneId.put(Integer.valueOf(iccCardStatus.mSlotPortMapping.mPortIndex), Integer.valueOf(i));
                IccCardStatus.CardState cardState = this.mCardState.get(Integer.valueOf(iccCardStatus.mSlotPortMapping.mPortIndex));
                this.mCardState.put(Integer.valueOf(iccCardStatus.mSlotPortMapping.mPortIndex), iccCardStatus.mCardState);
                this.mIccIds.put(Integer.valueOf(iccCardStatus.mSlotPortMapping.mPortIndex), iccCardStatus.iccid);
                IccCardStatus.CardState cardState2 = iccCardStatus.mCardState;
                if (cardState2 != null && cardState2.isCardPresent()) {
                    parseAtr(iccCardStatus.atr);
                    updateSupportedMepMode(iccCardStatus.mSupportedMepMode);
                }
                this.mIsRemovable = isSlotRemovable(i2);
                int radioState = commandsInterface.getRadioState();
                log(i, "update: radioState=" + radioState + " mLastRadioState=" + this.mLastRadioState + " on physicalSlotIndex=" + i2 + " portIndex=" + iccCardStatus.mSlotPortMapping.mPortIndex + " oldPhoneId=" + iIntValue + " phoneId=" + i + " slotPortSwitchFailureFix=true");
                if (absentStateUpdateNeeded(cardState, iccCardStatus.mSlotPortMapping.mPortIndex)) {
                    updateCardStateAbsent(commandsInterface.getRadioState(), i, iccCardStatus.mSlotPortMapping.mPortIndex);
                } else if (iIntValue != -1 && iIntValue != i) {
                    log("update IccSlotStatus: phoneId is remapped for the port but CARDSTATE_ABSENT is not received. Handle port disposal to avoid stale phoneId and port mapping");
                    updateCardStateAbsent(commandsInterface.getRadioState(), iIntValue, iccCardStatus.mSlotPortMapping.mPortIndex);
                } else if (cardState == null || cardState == IccCardStatus.CardState.CARDSTATE_ABSENT || this.mUiccCard == null) {
                    if (this.mCardState.get(Integer.valueOf(iccCardStatus.mSlotPortMapping.mPortIndex)) != IccCardStatus.CardState.CARDSTATE_ABSENT) {
                        if (radioState != 2 && this.mLastRadioState.getOrDefault(Integer.valueOf(iccCardStatus.mSlotPortMapping.mPortIndex), 2).intValue() != 2) {
                            log(i, "update: notify card added on slotIndex=" + i2);
                            sendMessage(obtainMessage(14, null));
                        }
                        UiccCard uiccCard2 = this.mUiccCard;
                        if (uiccCard2 != null && (!this.mIsEuicc || ArrayUtils.isEmpty(uiccCard2.getUiccPortList()))) {
                            loge(i, "update: mUiccCard != null when card was present; disposing it now on slotIndex=" + i2);
                            this.mUiccCard.dispose();
                            this.mUiccCard = null;
                        }
                        if (!this.mIsEuicc) {
                            this.mUiccCard = new UiccCard(this.mContext, commandsInterface, iccCardStatus, i, this.mLock, IccSlotStatus.MultipleEnabledProfilesMode.NONE);
                        } else {
                            if (TextUtils.isEmpty(iccCardStatus.eid)) {
                                loge(i, "update: eid is missing. ics.eid=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, iccCardStatus.eid));
                            }
                            UiccCard uiccCard3 = this.mUiccCard;
                            if (uiccCard3 == null) {
                                this.mUiccCard = new EuiccCard(this.mContext, commandsInterface, iccCardStatus, i, this.mLock, getSupportedMepMode());
                            } else {
                                uiccCard3.update(this.mContext, commandsInterface, iccCardStatus, i);
                            }
                        }
                    } else {
                        uiccCard = this.mUiccCard;
                        if (uiccCard != null) {
                            uiccCard.update(this.mContext, commandsInterface, iccCardStatus, i);
                        }
                    }
                } else {
                    uiccCard = this.mUiccCard;
                    if (uiccCard != null) {
                        uiccCard.update(this.mContext, commandsInterface, iccCardStatus, i);
                    }
                }
                this.mLastRadioState.put(Integer.valueOf(iccCardStatus.mSlotPortMapping.mPortIndex), Integer.valueOf(radioState));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void update(CommandsInterface[] commandsInterfaceArr, IccSlotStatus iccSlotStatus, int i) {
        synchronized (this.mLock) {
            try {
                IccSimPortInfo[] iccSimPortInfoArr = iccSlotStatus.mSimPortInfos;
                parseAtr(iccSlotStatus.atr);
                this.mEid = iccSlotStatus.eid;
                this.mIsRemovable = isSlotRemovable(i);
                for (int i2 = 0; i2 < iccSimPortInfoArr.length; i2++) {
                    int i3 = iccSlotStatus.mSimPortInfos[i2].mLogicalSlotIndex;
                    int iIntValue = ((Integer) this.mPortIdxToPhoneId.getOrDefault(Integer.valueOf(i2), -1)).intValue();
                    IccCardStatus.CardState cardState = this.mCardState.get(Integer.valueOf(i2));
                    this.mCardState.put(Integer.valueOf(i2), iccSlotStatus.cardState);
                    this.mIccIds.put(Integer.valueOf(i2), iccSimPortInfoArr[i2].mIccId);
                    log(i3, "update: oldCardState=" + cardState + " CardState=" + iccSlotStatus.cardState + " on slotIndex=" + i + " isPortActive=" + iccSlotStatus.mSimPortInfos[i2].mPortActive + " oldPhoneId=" + iIntValue + " phoneId=" + i3 + " slotPortSwitchFailureFix=true");
                    if (!iccSlotStatus.mSimPortInfos[i2].mPortActive) {
                        UiccController.getInstance().updateSimStateForInactivePort(((Integer) this.mPortIdxToPhoneId.getOrDefault(Integer.valueOf(i2), -1)).intValue(), iccSlotStatus.mSimPortInfos[i2].mIccId);
                        this.mLastRadioState.put(Integer.valueOf(i2), 2);
                        UiccCard uiccCard = this.mUiccCard;
                        if (uiccCard != null) {
                            uiccCard.disposePort(i2);
                        }
                    } else if (absentStateUpdateNeeded(cardState, i2)) {
                        updateCardStateAbsent(SubscriptionManager.isValidPhoneId(i3) ? commandsInterfaceArr[i3].getRadioState() : 2, i3, i2);
                    } else if (iIntValue != -1 && iIntValue != i3) {
                        log("update IccSlotStatus: phoneId is remapped for the port but CARDSTATE_ABSENT is not received. Handle port disposal to avoid stale phoneId and port mapping");
                        updateCardStateAbsent(SubscriptionManager.isValidPhoneId(iIntValue) ? commandsInterfaceArr[iIntValue].getRadioState() : 2, iIntValue, i2);
                    }
                }
                if (!hasActivePort(iccSimPortInfoArr)) {
                    if (this.mActive) {
                        this.mActive = false;
                        nullifyUiccCard(true);
                    }
                } else {
                    this.mActive = true;
                }
                this.mPortIdxToPhoneId.clear();
                for (int i4 = 0; i4 < iccSimPortInfoArr.length; i4++) {
                    HashMap map = this.mPortIdxToPhoneId;
                    Integer numValueOf = Integer.valueOf(i4);
                    IccSimPortInfo iccSimPortInfo = iccSimPortInfoArr[i4];
                    map.put(numValueOf, Integer.valueOf(iccSimPortInfo.mPortActive ? iccSimPortInfo.mLogicalSlotIndex : -1));
                }
                updateSupportedMepMode(iccSlotStatus.mSupportedMepMode);
                UiccCard uiccCard2 = this.mUiccCard;
                if (uiccCard2 != null) {
                    uiccCard2.updateSupportedMepMode(getSupportedMepMode());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void updateSupportedMepMode(IccSlotStatus.MultipleEnabledProfilesMode multipleEnabledProfilesMode) {
        AnswerToReset answerToReset;
        this.mSupportedMepMode = multipleEnabledProfilesMode;
        if (multipleEnabledProfilesMode != IccSlotStatus.MultipleEnabledProfilesMode.NONE || this.mPortIdxToPhoneId.size() <= 1 || (answerToReset = this.mAtr) == null || !answerToReset.isMultipleEnabledProfilesSupported()) {
            return;
        }
        Log.i("UiccSlot", "Modem does not send proper supported MEP mode or older HAL version");
        this.mSupportedMepMode = IccSlotStatus.MultipleEnabledProfilesMode.MEP_B;
    }

    private boolean hasActivePort(IccSimPortInfo[] iccSimPortInfoArr) {
        for (IccSimPortInfo iccSimPortInfo : iccSimPortInfoArr) {
            if (iccSimPortInfo.mPortActive) {
                return true;
            }
        }
        return false;
    }

    private int getAnyValidPhoneId() {
        Iterator it = this.mPortIdxToPhoneId.values().iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            if (SubscriptionManager.isValidPhoneId(iIntValue)) {
                return iIntValue;
            }
        }
        return -1;
    }

    public int[] getPortList() {
        int[] array;
        synchronized (this.mLock) {
            array = this.mPortIdxToPhoneId.keySet().stream().mapToInt(new RadioConfig$$ExternalSyntheticLambda0()).toArray();
        }
        return array;
    }

    public boolean isValidPortIndex(int i) {
        return this.mPortIdxToPhoneId.containsKey(Integer.valueOf(i));
    }

    public int getPortIndexFromPhoneId(int i) {
        synchronized (this.mLock) {
            try {
                for (Map.Entry entry : this.mPortIdxToPhoneId.entrySet()) {
                    if (((Integer) entry.getValue()).intValue() == i) {
                        return ((Integer) entry.getKey()).intValue();
                    }
                }
                return 0;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getPortIndexFromIccId(String str) {
        synchronized (this.mLock) {
            try {
                for (Map.Entry entry : this.mIccIds.entrySet()) {
                    if (IccUtils.compareIgnoreTrailingFs((String) entry.getValue(), str)) {
                        return ((Integer) entry.getKey()).intValue();
                    }
                }
                return -1;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getPhoneIdFromPortIndex(int i) {
        int iIntValue;
        synchronized (this.mLock) {
            iIntValue = ((Integer) this.mPortIdxToPhoneId.getOrDefault(Integer.valueOf(i), -1)).intValue();
        }
        return iIntValue;
    }

    public boolean isPortActive(int i) {
        boolean zIsValidPhoneId;
        synchronized (this.mLock) {
            zIsValidPhoneId = SubscriptionManager.isValidPhoneId(((Integer) this.mPortIdxToPhoneId.getOrDefault(Integer.valueOf(i), -1)).intValue());
        }
        return zIsValidPhoneId;
    }

    public boolean isMultipleEnabledProfileSupported() {
        boolean zIsMepMode;
        synchronized (this.mLock) {
            zIsMepMode = this.mSupportedMepMode.isMepMode();
        }
        return zIsMepMode;
    }

    private boolean absentStateUpdateNeeded(IccCardStatus.CardState cardState, int i) {
        IccCardStatus.CardState cardState2 = IccCardStatus.CardState.CARDSTATE_ABSENT;
        return !(cardState == cardState2 && this.mUiccCard == null) && this.mCardState.get(Integer.valueOf(i)) == cardState2;
    }

    private void updateCardStateAbsent(int i, int i2, int i3) {
        if (i != 2 && this.mLastRadioState.getOrDefault(Integer.valueOf(i3), 2).intValue() != 2) {
            log(i2, "update: notify card removed on portIndex=" + i3);
            sendMessage(obtainMessage(13, null));
        }
        UiccController.getInstance().updateSimState(i2, IccCardConstants.State.ABSENT, null);
        disposeUiccCardIfNeeded(false, i3);
        this.mPortIdxToPhoneId.put(Integer.valueOf(i3), -1);
        this.mLastRadioState.put(Integer.valueOf(i3), 2);
    }

    protected void nullifyUiccCard(boolean z) {
        UiccCard uiccCard = this.mUiccCard;
        if (uiccCard != null) {
            uiccCard.dispose();
        }
        this.mStateIsUnknown = z;
        this.mUiccCard = null;
    }

    private void disposeUiccCardIfNeeded(boolean z, int i) {
        UiccCard uiccCard = this.mUiccCard;
        if (uiccCard != null) {
            uiccCard.disposePort(i);
            if (ArrayUtils.isEmpty(this.mUiccCard.getUiccPortList())) {
                nullifyUiccCard(z);
                return;
            }
            return;
        }
        this.mStateIsUnknown = z;
    }

    public void dispose() {
        nullifyUiccCard(false);
    }

    public boolean isStateUnknown() {
        IccCardStatus.CardState cardState = this.mCardState.get(0);
        if (cardState == null || cardState == IccCardStatus.CardState.CARDSTATE_ABSENT) {
            return this.mStateIsUnknown;
        }
        return this.mUiccCard == null;
    }

    private boolean isSlotRemovable(int i) {
        int[] intArray = this.mContext.getResources().getIntArray(17236293);
        if (intArray == null) {
            return true;
        }
        for (int i2 : intArray) {
            if (i2 == i && this.mIsEuicc) {
                return false;
            }
        }
        return true;
    }

    private void checkIsEuiccSupported() {
        AnswerToReset answerToReset = this.mAtr;
        if (answerToReset == null) {
            this.mIsEuicc = false;
            return;
        }
        this.mIsEuicc = answerToReset.isEuiccSupported();
        log(" checkIsEuiccSupported : " + this.mIsEuicc);
    }

    private void checkMinimumVoltageClass() {
        this.mMinimumVoltageClass = 0;
        AnswerToReset answerToReset = this.mAtr;
        if (answerToReset == null) {
            return;
        }
        List<AnswerToReset.InterfaceByte> interfaceBytes = answerToReset.getInterfaceBytes();
        for (int i = 0; i < interfaceBytes.size() - 1; i++) {
            if (interfaceBytes.get(i).getTD() != null && (interfaceBytes.get(i).getTD().byteValue() & 15) == 15) {
                int i2 = i + 1;
                if (interfaceBytes.get(i2).getTA() != null) {
                    byte bByteValue = interfaceBytes.get(i2).getTA().byteValue();
                    if ((bByteValue & 1) != 0) {
                        this.mMinimumVoltageClass = 1;
                    }
                    if ((bByteValue & 2) != 0) {
                        this.mMinimumVoltageClass = 2;
                    }
                    if ((bByteValue & 4) != 0) {
                        this.mMinimumVoltageClass = 3;
                        return;
                    }
                    return;
                }
            }
        }
        this.mMinimumVoltageClass = 1;
    }

    private void parseAtr(String str) {
        this.mAtr = AnswerToReset.parseAtr(str);
        checkIsEuiccSupported();
        checkMinimumVoltageClass();
    }

    public boolean isEuicc() {
        return this.mIsEuicc;
    }

    public int getMinimumVoltageClass() {
        return this.mMinimumVoltageClass;
    }

    public boolean isActive() {
        return this.mActive;
    }

    public boolean isRemovable() {
        return this.mIsRemovable;
    }

    public String getIccId(int i) {
        String str;
        synchronized (this.mLock) {
            str = (String) this.mIccIds.get(Integer.valueOf(i));
        }
        return str;
    }

    public String getEid() {
        return this.mEid;
    }

    public boolean isExtendedApduSupported() {
        AnswerToReset answerToReset = this.mAtr;
        return answerToReset != null && answerToReset.isExtendedApduSupported();
    }

    protected void finalize() {
        log("UiccSlot finalized");
    }

    private void onIccSwap(boolean z) {
        if (this.mContext.getResources().getBoolean(R.bool.config_mainBuiltInDisplayIsRound)) {
            log("onIccSwap: isHotSwapSupported is true, don't prompt for rebooting");
            return;
        }
        Phone phone = PhoneFactory.getPhone(getAnyValidPhoneId());
        if (phone != null && phone.isShuttingDown()) {
            log(phone.getPhoneId(), "onIccSwap: already doing shutdown, no need to prompt");
        } else {
            log("onIccSwap: isHotSwapSupported is false, prompt for rebooting");
            promptForRestart(z);
        }
    }

    private void promptForRestart(boolean z) {
        String string;
        String string2;
        synchronized (this.mLock) {
            try {
                ComponentName componentNameUnflattenFromString = ComponentName.unflattenFromString(this.mContext.getResources().getString(R.string.date_and_time));
                if (componentNameUnflattenFromString != null) {
                    try {
                        this.mContext.startActivityAsUser(new Intent().setComponent(componentNameUnflattenFromString).addFlags(268435456).putExtra("com.android.internal.telephony.uicc.ICC_CARD_ADDED", z), UserHandle.CURRENT);
                        return;
                    } catch (ActivityNotFoundException e) {
                        loge("Unable to find ICC hotswap prompt for restart activity: " + e);
                    }
                }
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.internal.telephony.uicc.UiccSlot.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        synchronized (UiccSlot.this.mLock) {
                            if (i == -1) {
                                try {
                                    UiccSlot.this.log("Reboot due to SIM swap");
                                    ((PowerManager) UiccSlot.this.mContext.getSystemService("power")).reboot("SIM is added.");
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                        }
                    }
                };
                Resources system = Resources.getSystem();
                if (z) {
                    string = system.getString(17042239);
                } else {
                    string = system.getString(17042242);
                }
                if (z) {
                    string2 = system.getString(17042238);
                } else {
                    string2 = system.getString(17042241);
                }
                AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext).setTitle(string).setMessage(string2).setPositiveButton(system.getString(17042243), onClickListener).create();
                alertDialogCreate.getWindow().setType(TelephonyProto$TelephonyEvent.RilSetupDataCallResponse.RilDataCallFailCause.PDP_FAIL_MIP_FA_MOBILE_NODE_AUTHENTICATION_FAILURE);
                alertDialogCreate.show();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 13) {
            onIccSwap(false);
        } else {
            if (i == 14) {
                onIccSwap(true);
                return;
            }
            loge("Unknown Event " + message.what);
        }
    }

    public IccCardStatus.CardState getCardState() {
        IccCardStatus.CardState cardState;
        synchronized (this.mLock) {
            try {
                cardState = this.mCardState.get(0);
                if (cardState == null) {
                    cardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return cardState;
    }

    public UiccCard getUiccCard() {
        UiccCard uiccCard;
        synchronized (this.mLock) {
            uiccCard = this.mUiccCard;
        }
        return uiccCard;
    }

    public IccSlotStatus.MultipleEnabledProfilesMode getSupportedMepMode() {
        IccSlotStatus.MultipleEnabledProfilesMode multipleEnabledProfilesMode;
        synchronized (this.mLock) {
            multipleEnabledProfilesMode = this.mSupportedMepMode;
        }
        return multipleEnabledProfilesMode;
    }

    public void onRadioStateUnavailable(int i) {
        int portIndexFromPhoneId = getPortIndexFromPhoneId(i);
        disposeUiccCardIfNeeded(true, portIndexFromPhoneId);
        if (i != -1) {
            UiccController.getInstance().updateSimState(i, IccCardConstants.State.UNKNOWN, null);
        }
        this.mLastRadioState.put(Integer.valueOf(portIndexFromPhoneId), 2);
        this.mCardState.put(Integer.valueOf(portIndexFromPhoneId), null);
    }

    public void updateSimTypeInfo(SimTypeInfo simTypeInfo) {
        if (simTypeInfo == null) {
            loge("simTypeInfo is null");
            return;
        }
        this.mActiveSimType = getMappedSimType(simTypeInfo.mCurrentSimType);
        this.mSupportedSimTypes = convertSupportedSimTypesBitMaskToArray(simTypeInfo);
        log("updateSimTypeInfo for slotIndex=" + simTypeInfo.mPhysicalSlotIndex + " mActiveSimType: " + this.mActiveSimType + " mSupportedSimTypes: " + Arrays.toString(this.mSupportedSimTypes));
    }

    private int[] convertSupportedSimTypesBitMaskToArray(final SimTypeInfo simTypeInfo) {
        return Arrays.stream(new int[]{1, 2}).filter(new IntPredicate() { // from class: com.android.internal.telephony.uicc.UiccSlot$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return UiccSlot.$r8$lambda$wV6BZKTCb0e3yqHZLozalSEz4tk(simTypeInfo, i);
            }
        }).map(new IntUnaryOperator() { // from class: com.android.internal.telephony.uicc.UiccSlot$$ExternalSyntheticLambda1
            @Override // java.util.function.IntUnaryOperator
            public final int applyAsInt(int i) {
                return this.f$0.getMappedSimType(i);
            }
        }).toArray();
    }

    public static /* synthetic */ boolean $r8$lambda$wV6BZKTCb0e3yqHZLozalSEz4tk(SimTypeInfo simTypeInfo, int i) {
        return (simTypeInfo.mSupportedSimTypes & i) != 0;
    }

    public int getSimType() {
        return this.mActiveSimType;
    }

    public int[] getSupportedSimTypes() {
        int[] iArr = this.mSupportedSimTypes;
        return iArr != null ? iArr : new int[]{-1};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d("UiccSlot", str);
    }

    private void log(int i, String str) {
        Rlog.d("UiccSlot [" + i + "]", str);
    }

    private void loge(String str) {
        Rlog.e("UiccSlot", str);
    }

    private void loge(int i, String str) {
        Rlog.e("UiccSlot [" + i + "]", str);
    }

    private Map getPrintableIccIds() {
        HashMap map;
        synchronized (this.mLock) {
            map = new HashMap(this.mIccIds);
        }
        return (Map) map.entrySet().stream().collect(Collectors.toMap(new TelephonyNetworkRequest$$ExternalSyntheticLambda1(), new Function() { // from class: com.android.internal.telephony.uicc.UiccSlot$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SubscriptionInfo.getPrintableId((String) ((Map.Entry) obj).getValue());
            }
        }));
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("mActive=" + this.mActive);
        androidUtilIndentingPrintWriter.println("mIsEuicc=" + this.mIsEuicc);
        androidUtilIndentingPrintWriter.println("isEuiccSupportsMultipleEnabledProfiles=" + isMultipleEnabledProfileSupported());
        androidUtilIndentingPrintWriter.println("mIsRemovable=" + this.mIsRemovable);
        androidUtilIndentingPrintWriter.println("mLastRadioState=" + this.mLastRadioState);
        androidUtilIndentingPrintWriter.println("mIccIds=" + getPrintableIccIds());
        androidUtilIndentingPrintWriter.println("mPortIdxToPhoneId=" + this.mPortIdxToPhoneId);
        androidUtilIndentingPrintWriter.println("mEid=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.mEid));
        androidUtilIndentingPrintWriter.println("mCardState=" + this.mCardState);
        androidUtilIndentingPrintWriter.println("mSupportedMepMode=" + this.mSupportedMepMode);
        androidUtilIndentingPrintWriter.println("mActiveSimType=" + this.mActiveSimType);
        androidUtilIndentingPrintWriter.println("mSupportedSimTypes=" + Arrays.toString(this.mSupportedSimTypes));
        if (this.mUiccCard != null) {
            androidUtilIndentingPrintWriter.println("mUiccCard=");
            this.mUiccCard.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        } else {
            androidUtilIndentingPrintWriter.println("mUiccCard=null");
        }
        androidUtilIndentingPrintWriter.println();
        androidUtilIndentingPrintWriter.flush();
    }

    @Override // android.os.Handler
    public String toString() {
        return ("[UiccSlot: mActive=" + this.mActive + ", mIccId=" + getPrintableIccIds() + ", mIsEuicc=" + this.mIsEuicc + ", MEP=" + isMultipleEnabledProfileSupported() + ", mPortIdxToPhoneId=" + this.mPortIdxToPhoneId + ", mEid=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.mEid) + ", mCardState=" + this.mCardState + " mSupportedMepMode=" + this.mSupportedMepMode) + ", mActiveSimType=" + this.mActiveSimType + ", mSupportedSimTypes=" + Arrays.toString(this.mSupportedSimTypes) + "]";
    }
}
