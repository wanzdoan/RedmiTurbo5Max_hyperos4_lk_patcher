package com.android.phone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.telecom.Connection;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.phone.dsda.MiuiDsdaManager;
import com.android.phone.ims.ImsSignalQualityHelper;
import com.android.phone.ims.MiuiImsRegControllerBase;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.networkmode.NetworkMode;
import com.android.phone.roaming.RoamingCFManager;
import com.android.phone.roaming.RoamingManager;
import com.android.phone.roaming.utils.RoamingUtils;
import com.android.phone.utils.ImsConferenceHandler;
import com.android.phone.utils.Utils;
import com.android.services.telephony.Lazy;
import com.android.services.telephony.MiuiCallStateMonitor;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.telephony.EuiccOsuManagerBase;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyDelegateImpl implements ITelephonyModule {
    private static volatile TelephonyDelegateImpl sInstance;
    private Lazy mPhoneAdapter = new Lazy();
    private Lazy mVolteEnable = new Lazy();
    private Lazy mRoaming = new Lazy();
    private Lazy mDsda = new Lazy();
    private Lazy mCellInfoLogger = new Lazy();
    private Lazy mFiveGPower = new Lazy();
    private Lazy mFdnQuery = new Lazy();
    private Lazy mEnable2G = new Lazy();
    private Lazy mDual4G = new Lazy();
    private Lazy mImsSignal = new Lazy();
    private Lazy mCarrierConfigRepair = new Lazy();
    private Lazy mImsGlobalization = new Lazy();
    private Lazy mImsConferenceHandler = new Lazy();
    private Lazy mRoamingCF = new Lazy();
    private Lazy mTeleAction = new Lazy();
    private Lazy mNetworkMode = new Lazy();

    public static TelephonyDelegateImpl init() {
        if (sInstance == null) {
            synchronized (TelephonyDelegateImpl.class) {
                try {
                    if (sInstance == null) {
                        TelephonyDelegateImpl telephonyDelegateImpl = new TelephonyDelegateImpl();
                        ModuleDelegate.register(ITelephonyModule.class, telephonyDelegateImpl);
                        sInstance = telephonyDelegateImpl;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private TelephonyDelegateImpl() {
    }

    public void setPhoneAdapter(PhoneAdapter phoneAdapter) {
        this.mPhoneAdapter.set(phoneAdapter);
    }

    public void setVolteEnable(VolteEnableManager volteEnableManager) {
        this.mVolteEnable.set(volteEnableManager);
    }

    public void setRoaming(RoamingManager roamingManager) {
        this.mRoaming.set(roamingManager);
    }

    public void setDsda(MiuiDsdaManager miuiDsdaManager) {
        this.mDsda.set(miuiDsdaManager);
    }

    public void setCellInfoLogger(CellInfoLogger cellInfoLogger) {
        this.mCellInfoLogger.set(cellInfoLogger);
    }

    public void setFiveGPower(FiveGPowerController fiveGPowerController) {
        this.mFiveGPower.set(fiveGPowerController);
    }

    public void setFdnQuery(FdnQueryManager fdnQueryManager) {
        this.mFdnQuery.set(fdnQueryManager);
    }

    public void setEnable2G(Enable2GManager enable2GManager) {
        this.mEnable2G.set(enable2GManager);
    }

    public void setDual4G(Dual4GManager dual4GManager) {
        this.mDual4G.set(dual4GManager);
    }

    public void setImsSignal(ImsSignalQualityHelper imsSignalQualityHelper) {
        this.mImsSignal.set(imsSignalQualityHelper);
    }

    public void setCarrierConfigRepair(CarrierConfigRepairManager carrierConfigRepairManager) {
        this.mCarrierConfigRepair.set(carrierConfigRepairManager);
    }

    public void setImsGlobalization(MiuiImsGlobalizationManager miuiImsGlobalizationManager) {
        this.mImsGlobalization.set(miuiImsGlobalizationManager);
    }

    public void setImsConferenceHandler(ImsConferenceHandler imsConferenceHandler) {
        this.mImsConferenceHandler.set(imsConferenceHandler);
    }

    public void setRoamingCF(RoamingCFManager roamingCFManager) {
        this.mRoamingCF.set(roamingCFManager);
    }

    public void setTelephonyAction(TelephonyAction telephonyAction) {
        this.mTeleAction.set(telephonyAction);
    }

    public void setNetworkMode(NetworkMode networkMode) {
        this.mNetworkMode.set(networkMode);
    }

    private void sendErrorToCallback(Message message, String str) {
        if (message != null) {
            AsyncResult.forMessage(message, (Object) null, new IllegalStateException(str));
            message.sendToTarget();
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isEsimStateChanging() {
        return MiuiEsimManagerBase.isEsimStateChanging;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setEsimStateChanging(boolean z) {
        MiuiEsimManagerBase.isEsimStateChanging = z;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isEuiccPowerOff() {
        return MiuiEsimManagerBase.mIsEuiccPowerOff;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isEsimIsActiveChangeToNoActive() {
        return MiuiEsimManagerBase.isEsimIsActiveChangeToNoActive;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setEsimIsActiveChangeToNoActive(boolean z) {
        MiuiEsimManagerBase.isEsimIsActiveChangeToNoActive = z;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getDataSlotForCmcc() {
        return DefaultSlotSelectorImpl.getDataSlotForCmcc();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getUserSelectedVolteState(int i) {
        VolteEnableManager volteEnableManager = (VolteEnableManager) this.mVolteEnable.get();
        if (volteEnableManager == null) {
            return 0;
        }
        return volteEnableManager.getUserSelectedVolteState(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean maybeShowTurnOffVolteForCall(Phone phone) {
        MiuiCtVolteServiceManager.getInstance();
        return false;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void showNoFiveGDialog(int i) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", 20);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        phoneGlobals.startActivity(intent);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void showArrearsSimDialog(int i) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", 19);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        phoneGlobals.startActivity(intent);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void showSatelliteAlertDialog(int i) {
        if (i == 21 || i == 22) {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
            intent.setFlags(268435456);
            intent.putExtra("dialog_type", i);
            phoneGlobals.startActivity(intent);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void openedDual4GConfirmPrompt() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", 4);
        phoneGlobals.startActivity(intent);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void openedDual4GConfirmPromptWithSlot(int i) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", 5);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        phoneGlobals.startActivity(intent);
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0038  */
    /* JADX WARN: Code duplicated, block: B:20:0x0050  */
    /* JADX WARN: Code duplicated, block: B:23:0x005a  */
    /* JADX WARN: Code duplicated, block: B:26:0x0068 A[RETURN] */
    @Override // com.android.phone.module.ITelephonyModule
    public boolean maybeShowDialogForSuppService(Phone phone, int i) {
        int i2;
        boolean z;
        Intent intent;
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        int i3 = 3;
        if (MiuiPhoneUtils.isDataEnableNeededForUt(phone, i)) {
            i2 = R.string.cmcc_ut_data_enable_prompt;
        } else {
            if (MiuiCtVolteServiceManager.isCdmaVolteNotInService(phone)) {
                if (MiuiPhoneUtils.isDeviceNotSupportCDMA()) {
                    i2 = R.string.prompt_to_suggest_no_call_service_notification_content;
                } else {
                    i2 = R.string.prompt_to_turnoff_volte_when_failed;
                }
                z = !MiuiPhoneUtils.isDeviceNotSupportCDMA();
            } else if (MiuiPhoneUtils.maybeShowSwitchDDSDialog(phone, i)) {
                i2 = R.string.switch_dds_to_sub_alert;
                i3 = 2;
                z = true;
            } else {
                i2 = -1;
            }
            if (i2 != -1) {
                return false;
            }
            intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
            intent.setFlags(268435456);
            intent.putExtra("dialog_type", i3);
            intent.putExtra("dialog_message_id", i2);
            if (z) {
                intent.putExtra("show_negative", z);
            }
            if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
                intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, phone.getPhoneId());
            }
            phoneGlobals.startActivity(intent);
            return true;
        }
        z = false;
        if (i2 != -1) {
            return false;
        }
        intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", i3);
        intent.putExtra("dialog_message_id", i2);
        if (z) {
            intent.putExtra("show_negative", z);
        }
        if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, phone.getPhoneId());
        }
        phoneGlobals.startActivity(intent);
        return true;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (MiuiPhoneReceiver.getInstance() != null) {
            MiuiPhoneReceiver.getInstance().dump(fileDescriptor, printWriter, strArr);
        }
        if (this.mFiveGPower.get() != null) {
            ((FiveGPowerController) this.mFiveGPower.get()).dump(fileDescriptor, printWriter, strArr);
        }
        MiuiImsGlobalizationManager miuiImsGlobalizationManager = (MiuiImsGlobalizationManager) this.mImsGlobalization.get();
        if (miuiImsGlobalizationManager != null) {
            miuiImsGlobalizationManager.dump(fileDescriptor, printWriter, strArr);
        }
        if (this.mCellInfoLogger.get() != null) {
            ((CellInfoLogger) this.mCellInfoLogger.get()).dump(fileDescriptor, printWriter, strArr);
        }
        if (FiveGManagerBase.getInstance() != null) {
            FiveGManagerBase.getInstance().dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setCallCommandMessage(String str, int i, int i2, int i3) {
        MiuiCallStateMonitor.setCallCommandMessage(str, i, i2, i3);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void clearLocalCache() {
        if (this.mImsConferenceHandler.get() == null) {
            return;
        }
        ((ImsConferenceHandler) this.mImsConferenceHandler.get()).clearLocalCache();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void addParticipantsLocalCache(Connection connection, Phone phone) {
        if (this.mImsConferenceHandler.get() == null) {
            return;
        }
        ((ImsConferenceHandler) this.mImsConferenceHandler.get()).addParticipantsLocalCache(connection, phone);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void addParticipantsLocalCache(Connection connection, Connection connection2, Phone phone) {
        addParticipantsLocalCache(connection, phone);
        addParticipantsLocalCache(connection2, phone);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isNoNotificationOnDomesticRoaming() {
        return MiuiNotificationMgr.isNoNotificationOnDomesticRoaming();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void removeParticipantsNum(Uri uri) {
        if (this.mImsConferenceHandler.get() == null) {
            return;
        }
        ((ImsConferenceHandler) this.mImsConferenceHandler.get()).removeParticipantsNum(uri);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void updateConfigForVolteGlobalization(String str, PersistableBundle persistableBundle) {
        MiuiImsGlobalizationManager miuiImsGlobalizationManager = (MiuiImsGlobalizationManager) this.mImsGlobalization.get();
        if (miuiImsGlobalizationManager == null) {
            return;
        }
        miuiImsGlobalizationManager.updateConfigForVolteGlobalization(str, persistableBundle);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isLoadedError(int i) {
        CarrierConfigRepairManager carrierConfigRepairManager = (CarrierConfigRepairManager) this.mCarrierConfigRepair.get();
        if (carrierConfigRepairManager == null) {
            return false;
        }
        return carrierConfigRepairManager.isLoadedError(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void reportConfigErrorStatistics(int i, boolean z, PersistableBundle persistableBundle) {
        CarrierConfigRepairManager carrierConfigRepairManager = (CarrierConfigRepairManager) this.mCarrierConfigRepair.get();
        if (carrierConfigRepairManager == null) {
            return;
        }
        carrierConfigRepairManager.reportConfigErrorStatistics(i, z, persistableBundle);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void tryRepair(PersistableBundle persistableBundle, String str) {
        CarrierConfigRepairManager carrierConfigRepairManager = (CarrierConfigRepairManager) this.mCarrierConfigRepair.get();
        if (carrierConfigRepairManager == null) {
            return;
        }
        carrierConfigRepairManager.tryRepair(persistableBundle, str);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void reset(int i) {
        CarrierConfigRepairManager carrierConfigRepairManager = (CarrierConfigRepairManager) this.mCarrierConfigRepair.get();
        if (carrierConfigRepairManager == null) {
            return;
        }
        carrierConfigRepairManager.reset(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void saveUserSelectedVolteState(int i, boolean z) {
        VolteEnableManager volteEnableManager = (VolteEnableManager) this.mVolteEnable.get();
        if (volteEnableManager == null) {
            return;
        }
        volteEnableManager.saveUserSelectedVolteState(i, z);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getForceSelectedVolteState(int i) {
        VolteEnableManager volteEnableManager = (VolteEnableManager) this.mVolteEnable.get();
        if (volteEnableManager == null) {
            return 0;
        }
        return volteEnableManager.getForceSelectedVolteState(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void saveForceSelectedVolteState(int i, boolean z) {
        VolteEnableManager volteEnableManager = (VolteEnableManager) this.mVolteEnable.get();
        if (volteEnableManager == null) {
            return;
        }
        volteEnableManager.saveForceSelectedVolteState(i, z);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void clearUserSelectedVolteState(int i) {
        VolteEnableManager volteEnableManager = (VolteEnableManager) this.mVolteEnable.get();
        if (volteEnableManager == null) {
            return;
        }
        volteEnableManager.clearUserSelectedVolteState(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void trigerNoCallServiceDialogChecker(boolean z) {
        MiuiCtVolteServiceManager.getInstance();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public Intent getErrorDialogActivityIntent() {
        return new Intent(PhoneGlobals.getInstance(), (Class<?>) MiuiErrorDialogActivity.class);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void updateRoamingCFUStatus(Phone phone, int i, int i2, String str, Throwable th) {
        if (this.mRoamingCF.get() == null) {
            return;
        }
        ((RoamingCFManager) this.mRoamingCF.get()).updateRoamingCFUStatus(phone, i, i2, str, th);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void enableVoiceService(Phone phone, boolean z) {
        VoiceServiceMgr.getInstance().enableVoiceService(phone, z);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public HashMap getGlobalizationCarrierConfigMap() {
        MiuiImsGlobalizationManager miuiImsGlobalizationManager = (MiuiImsGlobalizationManager) this.mImsGlobalization.get();
        if (miuiImsGlobalizationManager != null) {
            return miuiImsGlobalizationManager.getGlobalizationCarrierConfigMap();
        }
        return new HashMap();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void resetImsSignalOptimizationToDefault() {
        ImsSignalQualityHelper imsSignalQualityHelper = (ImsSignalQualityHelper) this.mImsSignal.get();
        if (imsSignalQualityHelper != null) {
            imsSignalQualityHelper.resetImsSignalOptimizationToDefault(-1, 63);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isHikingState() {
        if (this.mFiveGPower.get() != null) {
            return ((FiveGPowerController) this.mFiveGPower.get()).getHikingState();
        }
        return false;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isDsdaEnabled() {
        MiuiDsdaManager miuiDsdaManager = (MiuiDsdaManager) this.mDsda.get();
        if (miuiDsdaManager != null) {
            return miuiDsdaManager.isDsdaEnabled();
        }
        return false;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean setDsdaEnabled(boolean z, int i) {
        MiuiDsdaManager miuiDsdaManager = (MiuiDsdaManager) this.mDsda.get();
        if (miuiDsdaManager == null) {
            return false;
        }
        miuiDsdaManager.setDsdaEnabled(z, i);
        return true;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getRoamingModeForPhoneId(int i) {
        RoamingManager roamingManager = (RoamingManager) this.mRoaming.get();
        if (roamingManager != null) {
            return roamingManager.getRoamingModeForPhoneId(i);
        }
        return -1;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isSmartCallForwardedCall(String str) {
        return SmartCallForwardUtils.isSmartCallForwardedCall(str);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isRoamingCFUCall(int i, String str) {
        return RoamingUtils.isRoamingCFUCall(i, str);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void recordCallerInfo(int i, String str, String str2) {
        if (this.mCellInfoLogger.get() != null) {
            ((CellInfoLogger) this.mCellInfoLogger.get()).recordCallerInfo(1, str);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isSupportSmartCallForwardByDevice() {
        return SmartCallForwardUtils.isSupportSmartCallForwardByDevice();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isSmartCallForwardSupported() {
        return SmartCallForwardUtils.isSmartCallForwardSupported();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public ArrayList getAndUpdateOldSimSlotIds(Context context) {
        return SmartCallForwardUtils.getAndUpdateOldSimSlotIds(context);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isSwitchDsdaModeByResetHwMbn() {
        MiuiDsdaManager miuiDsdaManager = (MiuiDsdaManager) this.mDsda.get();
        if (miuiDsdaManager != null) {
            return miuiDsdaManager.isSwitchDsdaModeByResetHwMbn();
        }
        return false;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public String queryFdn(String str) {
        if (this.mFdnQuery.get() == null) {
            return null;
        }
        return ((FdnQueryManager) this.mFdnQuery.get()).queryFdn(str);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void reset2GManager() {
        if (this.mEnable2G.get() != null) {
            ((Enable2GManager) this.mEnable2G.get()).reset();
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isDual4GEnabled() {
        return Dual4GManager.isDual4GEnabled();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setDual4GMode(boolean z, Utils.Dual4GCallBack dual4GCallBack) {
        if (this.mDual4G.get() != null) {
            ((Dual4GManager) this.mDual4G.get()).setDual4GMode(z, dual4GCallBack);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean getEnable2GState(Phone phone) {
        if (this.mEnable2G.get() != null) {
            return ((Enable2GManager) this.mEnable2G.get()).getEnable2GState(phone);
        }
        return false;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setEnable2G(int i, boolean z, Message message) {
        if (this.mEnable2G.get() == null) {
            sendErrorToCallback(message, "Enable2G not initialized");
        } else {
            ((Enable2GManager) this.mEnable2G.get()).setEnable2G(i, z, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isSupportImsRegOptByDevice(Context context) {
        return MiuiImsRegControllerBase.isSupportImsRegOptByDevice(context);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public FiveGServiceInfo getFiveGServiceInfo(int i) {
        return FiveGServiceInfoTracker.getFiveGServiceInfo(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isEsimActive() {
        return MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getInstance().isEsimActive();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isSupportForRegion() {
        return MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getInstance().isSupportForRegion();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isSupportEsim() {
        return MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getInstance().isSupportEsim();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getEsimSecureValue() {
        return MiuiEsimManagerBase.getEsimSecureValue();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setEsimSecureValue(int i) {
        MiuiEsimManagerBase.setEsimSecureValue(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isRemoveEsimSwitch() {
        return MiuiEsimManagerBase.isRemoveEsimSwitch();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getNeedShowNotificationValue() {
        return MiuiEsimManagerBase.getNeedShowNotificationValue();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public List getAllEsimProfiles() {
        if (MiuiEsimManagerBase.getInstance() != null) {
            return MiuiEsimManagerBase.getInstance().getAllEsimProfiles();
        }
        return new ArrayList();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isPsim2EsimAvailable(int i) {
        return MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getInstance().isPsim2EsimAvailable(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int setEsimState(int i) {
        if (MiuiEsimManagerBase.getInstance() != null) {
            return MiuiEsimManagerBase.getInstance().setEsimState(i);
        }
        return 0;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public String getProductEid() {
        return MiuiEsimManagerBase.getInstance() != null ? MiuiEsimManagerBase.getInstance().getProductEid() : "";
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void writeProductEid(byte[] bArr) {
        if (MiuiEsimManagerBase.getInstance() != null) {
            MiuiEsimManagerBase.getInstance().writeProductEid(bArr);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public String getEidFromSlotId(int i) {
        return MiuiEsimManagerBase.getInstance() != null ? MiuiEsimManagerBase.getInstance().getEidFromSlotId(i) : "";
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void recoveryForInit() {
        if (MiuiEsimManagerBase.getInstance() != null) {
            MiuiEsimManagerBase.getInstance().recoveryForInit();
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isDualEsimActive() {
        return MiuiEsimManagerBase.getInstance() != null && MiuiEsimManagerBase.getInstance().isDualEsimActive();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void resetPortAndStateForSlot(int i) {
        if (MiuiEsimManagerBase.getInstance() != null) {
            MiuiEsimManagerBase.getInstance().resetPortAndStateForSlot(i);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean shouldShowOsuDialog(long j) {
        if (EuiccOsuManagerBase.getInstance() == null || !EuiccOsuManagerBase.getInstance().isOsuFileExisted()) {
            return false;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance());
        long j2 = defaultSharedPreferences.getLong("osu_dialog_last_shown_time", 0L);
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (j2 != 0 && jCurrentTimeMillis - j2 < j * 1000) {
            return false;
        }
        defaultSharedPreferences.edit().putLong("osu_dialog_last_shown_time", jCurrentTimeMillis).apply();
        return true;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void showOsuDialog() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiOsuDialogActivity.class);
        intent.setFlags(268435456);
        phoneGlobals.startActivity(intent);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public IccCardStatus.CardState getCardState(int i) {
        return PhoneAdapter.getCardState(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isUtEnabled(Phone phone) {
        return this.mPhoneAdapter.get() != null && ((PhoneAdapter) this.mPhoneAdapter.get()).isUtEnabled(phone);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isDataEnabled(Phone phone) {
        return this.mPhoneAdapter.get() != null && ((PhoneAdapter) this.mPhoneAdapter.get()).isDataEnabled(phone);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isVoiceLinkRegistered(Context context, int i) {
        return this.mPhoneAdapter.get() != null && ((PhoneAdapter) this.mPhoneAdapter.get()).isVoiceLinkRegistered(context, i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isCrossSimCallingEnabledByUser(Context context, int i) {
        return this.mPhoneAdapter.get() != null && ((PhoneAdapter) this.mPhoneAdapter.get()).isCrossSimCallingEnabledByUser(context, i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setCrossSimCallingEnabled(Context context, int i, boolean z) {
        if (this.mPhoneAdapter.get() != null) {
            ((PhoneAdapter) this.mPhoneAdapter.get()).setCrossSimCallingEnabled(context, i, z);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public Phone getNtnPhone(Context context) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnPhone();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getNtnVoiceRate(boolean z) {
        if (this.mPhoneAdapter.get() != null) {
            return ((PhoneAdapter) this.mPhoneAdapter.get()).getNtnVoiceRate(z);
        }
        return 0;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getPhoneType(int i) {
        if (this.mPhoneAdapter.get() != null) {
            return ((PhoneAdapter) this.mPhoneAdapter.get()).getPhoneType(i);
        }
        return 0;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isManualNetSelAllowed(Phone phone) {
        return this.mPhoneAdapter.get() != null && ((PhoneAdapter) this.mPhoneAdapter.get()).isManualNetSelAllowed(phone);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isIccFdnAvailable(Phone phone) {
        return this.mPhoneAdapter.get() != null && ((PhoneAdapter) this.mPhoneAdapter.get()).getIccFdnAvailable(phone);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void getCallWaitingOption(Phone phone, Message message) {
        if (this.mPhoneAdapter.get() == null) {
            sendErrorToCallback(message, "PhoneAdapter not initialized");
        } else {
            ((PhoneAdapter) this.mPhoneAdapter.get()).getCallWaitingOption(phone, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setCallWaitingOption(Phone phone, boolean z, Message message) {
        if (this.mPhoneAdapter.get() == null) {
            sendErrorToCallback(message, "PhoneAdapter not initialized");
        } else {
            ((PhoneAdapter) this.mPhoneAdapter.get()).setCallWaitingOption(phone, z, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void getCallForwardingOption(Phone phone, int i, int i2, Message message) {
        if (this.mPhoneAdapter.get() == null) {
            sendErrorToCallback(message, "PhoneAdapter not initialized");
        } else {
            ((PhoneAdapter) this.mPhoneAdapter.get()).getCallForwardingOption(phone, i, i2, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setCallForwardingOption(Phone phone, int i, int i2, String str, int i3, int i4, Message message) {
        if (this.mPhoneAdapter.get() == null) {
            sendErrorToCallback(message, "PhoneAdapter not initialized");
        } else {
            ((PhoneAdapter) this.mPhoneAdapter.get()).setCallForwardingOption(phone, i, i2, str, i3, i4, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void getCallBarringOption(Phone phone, String str, String str2, Message message) {
        if (this.mPhoneAdapter.get() == null) {
            sendErrorToCallback(message, "PhoneAdapter not initialized");
        } else {
            ((PhoneAdapter) this.mPhoneAdapter.get()).getCallBarringOption(phone, str, str2, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setCallBarringOption(Phone phone, String str, boolean z, String str2, Message message) {
        if (this.mPhoneAdapter.get() == null) {
            sendErrorToCallback(message, "PhoneAdapter not initialized");
        } else {
            ((PhoneAdapter) this.mPhoneAdapter.get()).setCallBarringOption(phone, str, z, str2, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void getPlmnList(Phone phone, IccFileHandler iccFileHandler, Message message) {
        if (this.mPhoneAdapter.get() == null) {
            sendErrorToCallback(message, "PhoneAdapter not initialized");
        } else {
            ((PhoneAdapter) this.mPhoneAdapter.get()).getPlmnList(phone, iccFileHandler, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public ArrayList convertPlmnList(int i, Object obj) {
        if (this.mPhoneAdapter.get() != null) {
            return ((PhoneAdapter) this.mPhoneAdapter.get()).convertPlmnList(i, obj);
        }
        return null;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void setPlmnList(Phone phone, IccFileHandler iccFileHandler, ArrayList arrayList, Message message) {
        if (this.mPhoneAdapter.get() == null) {
            sendErrorToCallback(message, "PhoneAdapter not initialized");
        } else {
            ((PhoneAdapter) this.mPhoneAdapter.get()).setPlmnList(phone, iccFileHandler, arrayList, message);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void cancelAvailableNetworks(Handler handler, int i, int i2) {
        if (this.mPhoneAdapter.get() != null) {
            ((PhoneAdapter) this.mPhoneAdapter.get()).cancelAvailableNetworks(handler, i, i2);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isCallOptimizeInjected() {
        return CallOptimizeBase.getInstance() != null;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void reportCallOptimizeStatistic() {
        if (CallOptimizeBase.getInstance() != null) {
            CallOptimizeBase.getInstance().reportCallStatistic();
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isModemVonrOptimizeOperator() {
        return CallOptimizeBase.getInstance() != null && CallOptimizeBase.getInstance().isModemVonrOptimizeOperator();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean shouldDoCallVonrOptimization() {
        return CallOptimizeBase.getInstance() != null && CallOptimizeBase.getInstance().shouldDoCallVonrOpt();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isDisableVoNrByOptimization(int i) {
        return CallOptimizeBase.getInstance() != null && CallOptimizeBase.getInstance().isDisableVoNrByOpt(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void updateVoNrSwitchState(int i, boolean z) {
        if (CallOptimizeBase.getInstance() != null) {
            CallOptimizeBase.getInstance().updateVonrSwitchState(i, z);
        }
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isUTError(CommandException commandException) {
        return this.mPhoneAdapter.get() != null && ((PhoneAdapter) this.mPhoneAdapter.get()).isUTError(commandException);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getVideoCFServiceClass() {
        if (this.mPhoneAdapter.get() != null) {
            return ((PhoneAdapter) this.mPhoneAdapter.get()).getVideoCFServiceClass();
        }
        return 0;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public Integer toTelecomDisconnectCauseDescription(Context context, int i, int i2, Integer num) {
        if (this.mPhoneAdapter.get() != null) {
            return ((PhoneAdapter) this.mPhoneAdapter.get()).toTelecomDisconnectCauseDescription(context, i, i2, num);
        }
        return null;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int toTelecomDisconnectCauseCode(int i) {
        if (this.mPhoneAdapter.get() != null) {
            return ((PhoneAdapter) this.mPhoneAdapter.get()).toTelecomDisconnectCauseCode(i);
        }
        return 0;
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean teleAction(int i, int i2, Object... objArr) {
        if (this.mTeleAction.get() == null) {
            return false;
        }
        return ((TelephonyAction) this.mTeleAction.get()).teleAction(i, TelephonyActionBase.ActionReason.fromValue(i2), objArr);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean revertTeleAction(int i, int i2, Object... objArr) {
        if (this.mTeleAction.get() == null) {
            return false;
        }
        return ((TelephonyAction) this.mTeleAction.get()).revertTeleAction(i, TelephonyActionBase.ActionReason.fromValue(i2), objArr);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isRemoveNetworkModeSettings(Phone phone) {
        return NetworkModeManager.isRemoveNetworkModeSettings(phone);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isDisableLte() {
        return NetworkModeManager.isDisableLte();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isRemove2GOnly(int i) {
        return NetworkModeManager.isRemove2GOnly(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isRemovePrefer3G(int i) {
        return NetworkModeManager.isRemovePrefer3G(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isRemoveTdScdma(int i) {
        return NetworkModeManager.isRemoveTdScdma(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isRemoveCdma(int i) {
        return NetworkModeManager.isRemoveCdma(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean showWcdmaOnlyNetworkModes(int i) {
        return NetworkModeManager.showWcdmaOnlyNetworkModes(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isShowCdmaUnavailable() {
        return NetworkModeManager.isShowCdmaUnavailable();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean shouldShowViceNetworkTypePref() {
        return NetworkModeManager.shouldShowViceNetworkTypePref();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isNetworkModeLteOnly(int i) {
        return NetworkModeManager.isNetworkModeLteOnly(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isDeviceLockViceNetworkModeForCmcc() {
        return NetworkModeManager.isDeviceLockViceNetworkModeForCmcc();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public void dumpNetworkMode(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        NetworkModeManager.dump(fileDescriptor, printWriter, strArr);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int[] getSupportNetworkMode(int i) {
        return NetworkMode.getSupportNetworkMode(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getNrNetworkModeValue(int i) {
        NetworkMode networkMode = (NetworkMode) this.mNetworkMode.get();
        if (networkMode != null) {
            return networkMode.getNrNetworkModeValue(i);
        }
        return super.getNrNetworkModeValue(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getLteNetworkModeValue(int i) {
        NetworkMode networkMode = (NetworkMode) this.mNetworkMode.get();
        if (networkMode != null) {
            return networkMode.getLteNetworkModeValue(i);
        }
        return super.getLteNetworkModeValue(i);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int[][] getSupportModes() {
        NetworkMode networkMode = (NetworkMode) this.mNetworkMode.get();
        return networkMode != null ? networkMode.getSupportModes() : super.getSupportModes();
    }

    @Override // com.android.phone.module.ITelephonyModule
    public int getDesignatedNetworkModeValueForSlot(int i, int i2) {
        NetworkMode networkMode = (NetworkMode) this.mNetworkMode.get();
        if (networkMode != null) {
            return networkMode.getDesignatedNetworkModeValueForSlot(i, i2);
        }
        return super.getDesignatedNetworkModeValueForSlot(i, i2);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean isNrNetworkModeKey(String str) {
        return NetworkMode.isNrNetworkModeKey(str);
    }

    @Override // com.android.phone.module.ITelephonyModule
    public boolean shouldAddNrNetworkModes(int i) {
        return NetworkMode.shouldAddNrNetworkModes(i);
    }
}
