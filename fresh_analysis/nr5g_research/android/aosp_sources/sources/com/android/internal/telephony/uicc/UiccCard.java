package com.android.internal.telephony.uicc;

import android.content.Context;
import android.text.TextUtils;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.euicc.EuiccCard;
import com.android.internal.telephony.uicc.euicc.EuiccPort;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.IntFunction;

/* JADX INFO: loaded from: classes.dex */
public class UiccCard {
    protected static final boolean DBG = true;
    public static final String EXTRA_ICC_CARD_ADDED = "com.android.internal.telephony.uicc.ICC_CARD_ADDED";
    protected static final String LOG_TAG = "UiccCard";
    protected String mCardId;
    private IccCardStatus.CardState mCardState;
    protected final Object mLock;
    protected IccSlotStatus.MultipleEnabledProfilesMode mSupportedMepMode;
    protected LinkedHashMap<Integer, UiccPort> mUiccPorts = new LinkedHashMap<>();
    private HashMap mPhoneIdToPortIdx = new HashMap();

    public UiccCard(Context context, CommandsInterface commandsInterface, IccCardStatus iccCardStatus, int i, Object obj, IccSlotStatus.MultipleEnabledProfilesMode multipleEnabledProfilesMode) {
        log("Creating UiccCard for phoneId:" + i + " CardState:" + iccCardStatus.mCardState);
        this.mCardState = iccCardStatus.mCardState;
        this.mLock = obj;
        this.mSupportedMepMode = multipleEnabledProfilesMode;
        update(context, commandsInterface, iccCardStatus, i);
    }

    public void dispose() {
        synchronized (this.mLock) {
            try {
                log("Disposing card");
                for (UiccPort uiccPort : this.mUiccPorts.values()) {
                    if (uiccPort != null) {
                        uiccPort.dispose();
                    }
                }
                this.mUiccPorts.clear();
                this.mUiccPorts = null;
                this.mPhoneIdToPortIdx.clear();
                this.mPhoneIdToPortIdx = null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void disposePort(int i) {
        synchronized (this.mLock) {
            try {
                UiccPort uiccPort = getUiccPort(i);
                if (uiccPort != null) {
                    this.mPhoneIdToPortIdx.remove(Integer.valueOf(uiccPort.getPhoneId()));
                    log("Disposing port for index " + i + " on PhoneId : " + uiccPort.getPhoneId());
                    uiccPort.dispose();
                } else {
                    log("Disposing port for index " + i + " , But port is null");
                }
                this.mUiccPorts.remove(Integer.valueOf(i));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void update(Context context, CommandsInterface commandsInterface, IccCardStatus iccCardStatus, int i) {
        UiccCard uiccCard;
        int i2;
        UiccPort uiccPort;
        synchronized (this.mLock) {
            try {
                this.mCardState = iccCardStatus.mCardState;
                updateCardId(iccCardStatus.iccid);
                if (this.mCardState != IccCardStatus.CardState.CARDSTATE_ABSENT) {
                    int i3 = iccCardStatus.mSlotPortMapping.mPortIndex;
                    UiccPort uiccPort2 = this.mUiccPorts.get(Integer.valueOf(i3));
                    if (uiccPort2 == null) {
                        if (this instanceof EuiccCard) {
                            uiccCard = this;
                            i2 = i;
                            uiccPort = new EuiccPort(context, commandsInterface, iccCardStatus, i2, this.mLock, uiccCard, this.mSupportedMepMode);
                        } else {
                            i2 = i;
                            uiccCard = this;
                            uiccPort = new UiccPort(context, commandsInterface, iccCardStatus, i, this.mLock, this);
                        }
                        uiccCard.mUiccPorts.put(Integer.valueOf(i3), uiccPort);
                    } else {
                        uiccCard = this;
                        i2 = i;
                        uiccPort2.update(context, commandsInterface, iccCardStatus, uiccCard);
                    }
                    uiccCard.mPhoneIdToPortIdx.put(Integer.valueOf(i2), Integer.valueOf(i3));
                } else {
                    throw new RuntimeException("Card state is absent when updating!");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected void finalize() {
        log("UiccCard finalized");
    }

    protected void updateCardId(String str) {
        this.mCardId = str;
    }

    public void updateSupportedMepMode(IccSlotStatus.MultipleEnabledProfilesMode multipleEnabledProfilesMode) {
        this.mSupportedMepMode = multipleEnabledProfilesMode;
    }

    public IccCardStatus.CardState getCardState() {
        IccCardStatus.CardState cardState;
        synchronized (this.mLock) {
            log("cardId = " + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.mCardId) + " CardState = " + this.mCardState);
            cardState = this.mCardState;
        }
        return cardState;
    }

    public String getCardId() {
        UiccProfile uiccProfile;
        if (!TextUtils.isEmpty(this.mCardId)) {
            return this.mCardId;
        }
        UiccPort uiccPort = this.mUiccPorts.get(0);
        if (uiccPort == null || (uiccProfile = uiccPort.getUiccProfile()) == null) {
            return null;
        }
        return uiccProfile.getIccId();
    }

    public UiccPort[] getUiccPortList() {
        UiccPort[] uiccPortArr;
        synchronized (this.mLock) {
            uiccPortArr = (UiccPort[]) this.mUiccPorts.values().stream().toArray(new IntFunction() { // from class: com.android.internal.telephony.uicc.UiccCard$$ExternalSyntheticLambda0
                @Override // java.util.function.IntFunction
                public final Object apply(int i) {
                    return UiccCard.m1976$r8$lambda$5Z2xWklsK3YubuBSBV_MnHsms(i);
                }
            });
        }
        return uiccPortArr;
    }

    /* JADX INFO: renamed from: $r8$lambda$5Z2xWklsK-3YubuBSBV_M-nHsms, reason: not valid java name */
    public static /* synthetic */ UiccPort[] m1976$r8$lambda$5Z2xWklsK3YubuBSBV_MnHsms(int i) {
        return new UiccPort[i];
    }

    public UiccPort getUiccPortForPhone(int i) {
        UiccPort uiccPort;
        synchronized (this.mLock) {
            uiccPort = this.mUiccPorts.get(this.mPhoneIdToPortIdx.get(Integer.valueOf(i)));
        }
        return uiccPort;
    }

    public UiccPort getUiccPort(int i) {
        UiccPort uiccPort;
        synchronized (this.mLock) {
            uiccPort = this.mUiccPorts.get(Integer.valueOf(i));
        }
        return uiccPort;
    }

    private void log(String str) {
        Rlog.d(LOG_TAG, str);
    }

    private void loge(String str) {
        Rlog.e(LOG_TAG, str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("UiccCard:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mCardState=" + this.mCardState);
        androidUtilIndentingPrintWriter.println("mCardId=" + Rlog.pii(TelephonyUtils.IS_DEBUGGABLE, this.mCardId));
        androidUtilIndentingPrintWriter.println("mNumberOfPorts=" + this.mUiccPorts.size());
        androidUtilIndentingPrintWriter.println("mSupportedMepMode=" + this.mSupportedMepMode);
        androidUtilIndentingPrintWriter.println("mUiccPorts= size=" + this.mUiccPorts.size());
        androidUtilIndentingPrintWriter.increaseIndent();
        Iterator<UiccPort> it = this.mUiccPorts.values().iterator();
        while (it.hasNext()) {
            it.next().dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
    }
}
