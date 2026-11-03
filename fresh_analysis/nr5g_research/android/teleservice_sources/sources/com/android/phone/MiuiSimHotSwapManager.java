package com.android.phone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.RadioAccessFamily;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.networkmode.NetworkMode;
import com.android.phone.utils.FiveGUtils;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSimHotSwapManager extends Handler {
    private static String LOG_TAG = "MiuiSimHotSwapManager";
    private static MiuiSimHotSwapManager sInstance;
    private boolean mHasPromptForRestart;
    private boolean[] mIsHotSwap;
    private IccCardStatus.CardState[] mLastCardState;
    private boolean[] mLastIsRadioAvaiable;
    private UiccProfile[] mLastUiccProfile;
    private String[] mOperatorImsi;
    private UiccController mUiccController;
    private AlertDialog mRestartDialog = null;
    private Context mContext = PhoneGlobals.getInstance();

    private boolean isUnSupportHotSwap(String str) {
        return false;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        IccCardStatus.CardState cardState;
        AsyncResult asyncResult = (AsyncResult) message.obj;
        int i = message.what;
        if (i != 0) {
            if (i == 1 || i == 2) {
                if (asyncResult.exception != null) {
                    Log.e(LOG_TAG, "Error: Invalid phoneid EVENT_IMSI_READY ");
                    return;
                }
                int iIntValue = ((Integer) asyncResult.userObj).intValue();
                handleImsiReady(iIntValue, message.what - 1);
                Phone phone = MiuiPhoneUtils.getPhone(iIntValue);
                if (phone != null) {
                    boolean zIsRadioAvailable = phone.isRadioAvailable();
                    Log.d(LOG_TAG, "EVENT_IMSI_READY mLastIsRadioAvaiable[phoneId]=" + this.mLastIsRadioAvaiable[iIntValue] + "; isRadioAvailable=" + zIsRadioAvailable);
                    this.mLastIsRadioAvaiable[iIntValue] = zIsRadioAvailable;
                }
                if (MiuiPhoneUtils.isSupportOtaLteUpdate()) {
                    OtaLteUpdateManager otaLteUpdateManager = OtaLteUpdateManager.getInstance();
                    if (otaLteUpdateManager != null) {
                        otaLteUpdateManager.handleClaroRat(iIntValue);
                    } else {
                        Log.w(LOG_TAG, "OtaLteUpdateManager is not initialized, phoneId = " + iIntValue);
                    }
                }
                if (FiveGUtils.isFiveGNetWorkInVisible() && FiveGUtils.isForcedRecoveryNr() && isOTAUpdate(iIntValue)) {
                    forceRecoveryNrRat(iIntValue);
                    return;
                }
                return;
            }
            return;
        }
        Object obj = asyncResult.result;
        if (obj != null) {
            Integer num = (Integer) obj;
            int iIntValue2 = num.intValue();
            if (iIntValue2 < 0 || iIntValue2 >= this.mLastCardState.length) {
                Log.w(LOG_TAG, "Error: Invalid phoneId " + iIntValue2 + " when receiving EVENT_ICC_CHANGED");
                return;
            }
            Log.d(LOG_TAG, "received EVENT_ICC_CHANGED,phoneId = " + iIntValue2);
            IccCardStatus.CardState cardState2 = PhoneAdapter.getCardState(iIntValue2);
            if (cardState2 == null) {
                Log.w(LOG_TAG, "Error: cardstate is null ,when receiving EVENT_ICC_CHANGED");
                return;
            }
            UiccProfile uiccProfileForPhone = this.mUiccController.getUiccProfileForPhone(iIntValue2);
            Phone phone2 = MiuiPhoneUtils.getPhone(iIntValue2);
            boolean zIsRadioAvailable2 = phone2 != null ? phone2.isRadioAvailable() : false;
            Log.d(LOG_TAG, "mLastCardState: " + this.mLastCardState[iIntValue2] + ", cardState: " + cardState2 + ", mLastIsRadioAvaiable=" + this.mLastIsRadioAvaiable[iIntValue2] + ", isRadioAvailable=" + zIsRadioAvailable2);
            IccCardStatus.CardState cardState3 = this.mLastCardState[iIntValue2];
            if ((cardState3 == null || cardState3 == IccCardStatus.CardState.CARDSTATE_ABSENT || this.mLastUiccProfile[iIntValue2] == null) && cardState2 != IccCardStatus.CardState.CARDSTATE_ABSENT) {
                int numApplications = uiccProfileForPhone == null ? 0 : uiccProfileForPhone.getNumApplications();
                for (int i2 = 0; i2 < numApplications; i2++) {
                    IccRecords iccRecordsForPhoneId = getIccRecordsForPhoneId(iIntValue2, i2);
                    if (iccRecordsForPhoneId != null) {
                        if (zIsRadioAvailable2 && this.mLastIsRadioAvaiable[iIntValue2]) {
                            this.mIsHotSwap[iIntValue2] = true;
                        } else {
                            this.mIsHotSwap[iIntValue2] = false;
                        }
                        iccRecordsForPhoneId.registerForImsiReady(this, i2 + 1, num);
                    }
                }
            } else {
                IccCardStatus.CardState cardState4 = IccCardStatus.CardState.CARDSTATE_ABSENT;
                if ((cardState3 != cardState4 || this.mLastUiccProfile[iIntValue2] != null) && cardState2 == cardState4) {
                    if (zIsRadioAvailable2 && this.mLastIsRadioAvaiable[iIntValue2]) {
                        this.mIsHotSwap[iIntValue2] = true;
                    } else {
                        this.mIsHotSwap[iIntValue2] = false;
                    }
                    this.mHasPromptForRestart = false;
                    handleCardRemoved(iIntValue2);
                    handleDualCardRemoved(iIntValue2);
                }
            }
            Log.d(LOG_TAG, "mIsHotSwap: " + this.mIsHotSwap[iIntValue2] + ", mHasPromptForRestart: " + this.mHasPromptForRestart);
            if (MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getEsimSecureValue() == 1 && !TelephonyManagerEx.getDefault().isPlatform8750() && iIntValue2 == 1 && cardState2 != (cardState = this.mLastCardState[1])) {
                if (cardState2 == IccCardStatus.CardState.CARDSTATE_ABSENT && cardState != null && cardState != IccCardStatus.CardState.CARDSTATE_ERROR && cardState == IccCardStatus.CardState.CARDSTATE_PRESENT && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimStateChanging() && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEuiccPowerOff()) {
                    Log.d(LOG_TAG, "restUimPowerState for REASON_FOR_HOT_SWAP...");
                    MiuiEsimManagerBase.getInstance().restUimPowerState(1002);
                } else if (cardState2 == IccCardStatus.CardState.CARDSTATE_ERROR && !MiuiPhoneUtils.isSupportMep()) {
                    Log.d(LOG_TAG, "setEsimState for CARDSTATE_ERROR...");
                    MiuiEsimManagerBase.getInstance().setEsimState(0);
                }
            }
            this.mLastCardState[iIntValue2] = cardState2;
            this.mLastIsRadioAvaiable[iIntValue2] = zIsRadioAvailable2;
            this.mLastUiccProfile[iIntValue2] = uiccProfileForPhone;
            return;
        }
        Log.w(LOG_TAG, "Error: Invalid card index when receiving EVENT_ICC_CHANGED ");
    }

    private void handleImsiReady(int i, int i2) {
        Log.d(LOG_TAG, "received EVENT_IMSI_READY: phoneid = " + i + "applicationIndex =" + i2 + "mIsHotSwap =" + this.mIsHotSwap[i] + ",mHasPromptForRestart =" + this.mHasPromptForRestart);
        IccRecords iccRecordsForPhoneId = getIccRecordsForPhoneId(i, i2);
        String imsi = iccRecordsForPhoneId != null ? iccRecordsForPhoneId.getIMSI() : null;
        this.mOperatorImsi[i] = imsi;
        if (TextUtils.isEmpty(imsi) || !isUnSupportHotSwap(imsi) || !this.mIsHotSwap[i] || this.mHasPromptForRestart) {
            return;
        }
        promptForRestart(this.mContext, true);
        this.mHasPromptForRestart = true;
    }

    private void handleCardRemoved(int i) {
        if (!TextUtils.isEmpty(this.mOperatorImsi[i]) && "450".equals(this.mOperatorImsi[i].substring(0, 3)) && this.mIsHotSwap[i]) {
            promptForRestart(this.mContext, false);
        }
    }

    private void handleDualCardRemoved(int i) {
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (i2 != i && this.mLastCardState[i2] == IccCardStatus.CardState.CARDSTATE_ABSENT) {
                Log.d(LOG_TAG, "dual sim absent, resetShowDialogWhenIllegalME.");
                MiuiPhoneUtils.resetShowDialogWhenIllegalME();
            }
        }
    }

    private IccRecords getIccRecordsForPhoneId(int i, int i2) {
        UiccCardApplication applicationIndex;
        UiccProfile uiccProfileForPhone = this.mUiccController.getUiccProfileForPhone(i);
        if (uiccProfileForPhone == null || (applicationIndex = uiccProfileForPhone.getApplicationIndex(i2)) == null) {
            return null;
        }
        return applicationIndex.getIccRecords();
    }

    private void promptForRestart(final Context context, boolean z) {
        String string;
        String string2;
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener(this) { // from class: com.android.phone.MiuiSimHotSwapManager.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    Log.i(MiuiSimHotSwapManager.LOG_TAG, "Reboot due to SIM swap");
                    ((PowerManager) context.getSystemService("power")).reboot("SIM is added.");
                } else {
                    dialogInterface.dismiss();
                }
            }
        };
        AlertDialog alertDialog = this.mRestartDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            Log.d(LOG_TAG, "mRestartDialog.dismiss");
            this.mRestartDialog.dismiss();
        }
        Context context2 = this.mContext;
        if (z) {
            string = context2.getString(17042239);
        } else {
            string = context2.getString(17042242);
        }
        Context context3 = this.mContext;
        if (z) {
            string2 = context3.getString(17042238);
        } else {
            string2 = context3.getString(17042241);
        }
        this.mContext.getString(17042243);
        AlertDialog.Builder message = new AlertDialog.Builder(context, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(string).setMessage(string2);
        if (z) {
            message.setPositiveButton(17042243, onClickListener).setNegativeButton(android.R.string.cancel, onClickListener);
        } else {
            message.setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null);
        }
        AlertDialog alertDialogCreate = message.create();
        this.mRestartDialog = alertDialogCreate;
        alertDialogCreate.getWindow().setType(2003);
        this.mRestartDialog.show();
    }

    private MiuiSimHotSwapManager() {
        this.mUiccController = null;
        int length = MiuiPhoneUtils.getPhones().length;
        this.mLastCardState = new IccCardStatus.CardState[length];
        this.mOperatorImsi = new String[length];
        this.mLastIsRadioAvaiable = new boolean[length];
        this.mIsHotSwap = new boolean[length];
        this.mLastUiccProfile = new UiccProfile[length];
        for (int i = 0; i < length; i++) {
            this.mLastCardState[i] = IccCardStatus.CardState.CARDSTATE_ABSENT;
            this.mOperatorImsi[i] = null;
            this.mLastIsRadioAvaiable[i] = false;
            this.mIsHotSwap[i] = false;
            this.mLastUiccProfile[i] = null;
        }
        UiccController uiccController = UiccController.getInstance();
        this.mUiccController = uiccController;
        uiccController.registerForIccChanged(this, 0, (Object) null);
        for (int i2 = 0; i2 < length; i2++) {
            if (PhoneAdapter.getCardState(i2) != null) {
                Log.d(LOG_TAG, "Send initial EVENT_ICC_CHANGED for phoneId = " + i2);
                obtainMessage(0, new AsyncResult((Object) null, Integer.valueOf(i2), (Throwable) null)).sendToTarget();
            }
        }
    }

    public static void init() {
        if (sInstance == null) {
            sInstance = new MiuiSimHotSwapManager();
        }
    }

    private boolean isOTAUpdate(int i) {
        if (!SubscriptionManager.isValidSlotId(i)) {
            Log.d(LOG_TAG, "isOTAUpdate is not vaild phoneId = " + i);
            return false;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        String string = defaultSharedPreferences.getString("key_build_version_incremental" + i, "");
        String str = Build.VERSION.INCREMENTAL;
        boolean zEquals = TextUtils.equals(string, str) ^ true;
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.putString("key_build_version_incremental" + i, str);
        editorEdit.commit();
        Log.d(LOG_TAG, "isOTAUpdate = " + zEquals + ", oldVersion: " + string + ", currentVersion: " + str);
        return zEquals;
    }

    private void forceRecoveryNrRat(int i) {
        if (SubscriptionManager.isValidSlotId(i)) {
            Log.d(LOG_TAG, "forceRecoveryHighRat phoneId: " + i);
            Phone phone = MiuiPhoneUtils.getPhone(i);
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
            if (FiveGManagerBase.getInstance() != null) {
                if (!(i == defaultDataSlotId && FiveGManagerBase.isFiveGCapable()) && (i == defaultDataSlotId || !TelephonyManager.getDefault().isDualNrSupported())) {
                    return;
                }
                if (!FiveGManagerBase.getInstance().isUserFiveGEnabled(i)) {
                    FiveGManagerBase.setFivegEnabled(true, i);
                }
                int nrNetworkModeValue = NetworkMode.getInstance().getNrNetworkModeValue(i);
                Log.d(LOG_TAG, "dbMode: " + preferredNetworkModeFromDb + ",newDbMode=" + nrNetworkModeValue);
                if (nrNetworkModeValue != preferredNetworkModeFromDb) {
                    ModemProvider.getInstance().setNetworkModeInDb(i, nrNetworkModeValue);
                    phone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(nrNetworkModeValue), (Message) null);
                }
            }
        }
    }
}
