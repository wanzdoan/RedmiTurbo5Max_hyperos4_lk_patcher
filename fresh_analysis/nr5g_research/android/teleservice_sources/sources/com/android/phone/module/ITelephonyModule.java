package com.android.phone.module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telecom.Connection;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.utils.Utils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ITelephonyModule {
    default void addParticipantsLocalCache(Connection connection, Connection connection2, Phone phone) {
    }

    default void addParticipantsLocalCache(Connection connection, Phone phone) {
    }

    default void cancelAvailableNetworks(Handler handler, int i, int i2) {
    }

    default void clearLocalCache() {
    }

    default void clearUserSelectedVolteState(int i) {
    }

    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void dumpNetworkMode(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void enableVoiceService(Phone phone, boolean z) {
    }

    default IccCardStatus.CardState getCardState(int i) {
        return null;
    }

    default int getDataSlotForCmcc() {
        return 0;
    }

    default int getDesignatedNetworkModeValueForSlot(int i, int i2) {
        return 0;
    }

    default boolean getEnable2GState(Phone phone) {
        return false;
    }

    default int getEsimSecureValue() {
        return 0;
    }

    default FiveGServiceInfo getFiveGServiceInfo(int i) {
        return null;
    }

    default int getForceSelectedVolteState(int i) {
        return 0;
    }

    default int getLteNetworkModeValue(int i) {
        return 0;
    }

    default int getNeedShowNotificationValue() {
        return 0;
    }

    default int getNrNetworkModeValue(int i) {
        return 0;
    }

    default Phone getNtnPhone(Context context) {
        return null;
    }

    default int getNtnVoiceRate(boolean z) {
        return 0;
    }

    default int getPhoneType(int i) {
        return 0;
    }

    default int getRoamingModeForPhoneId(int i) {
        return 0;
    }

    default int getUserSelectedVolteState(int i) {
        return 0;
    }

    default int getVideoCFServiceClass() {
        return 0;
    }

    default boolean isCallOptimizeInjected() {
        return false;
    }

    default boolean isCrossSimCallingEnabledByUser(Context context, int i) {
        return false;
    }

    default boolean isDataEnabled(Phone phone) {
        return false;
    }

    default boolean isDeviceLockViceNetworkModeForCmcc() {
        return false;
    }

    default boolean isDisableLte() {
        return false;
    }

    default boolean isDisableVoNrByOptimization(int i) {
        return false;
    }

    default boolean isDsdaEnabled() {
        return false;
    }

    default boolean isDual4GEnabled() {
        return false;
    }

    default boolean isDualEsimActive() {
        return false;
    }

    default boolean isEsimActive() {
        return false;
    }

    default boolean isEsimIsActiveChangeToNoActive() {
        return false;
    }

    default boolean isEsimStateChanging() {
        return false;
    }

    default boolean isEuiccPowerOff() {
        return false;
    }

    default boolean isHikingState() {
        return false;
    }

    default boolean isIccFdnAvailable(Phone phone) {
        return false;
    }

    default boolean isLoadedError(int i) {
        return false;
    }

    default boolean isManualNetSelAllowed(Phone phone) {
        return false;
    }

    default boolean isModemVonrOptimizeOperator() {
        return false;
    }

    default boolean isNetworkModeLteOnly(int i) {
        return false;
    }

    default boolean isNoNotificationOnDomesticRoaming() {
        return false;
    }

    default boolean isNrNetworkModeKey(String str) {
        return false;
    }

    default boolean isPsim2EsimAvailable(int i) {
        return false;
    }

    default boolean isRemove2GOnly(int i) {
        return false;
    }

    default boolean isRemoveCdma(int i) {
        return false;
    }

    default boolean isRemoveEsimSwitch() {
        return false;
    }

    default boolean isRemoveNetworkModeSettings(Phone phone) {
        return false;
    }

    default boolean isRemovePrefer3G(int i) {
        return false;
    }

    default boolean isRemoveTdScdma(int i) {
        return false;
    }

    default boolean isRoamingCFUCall(int i, String str) {
        return false;
    }

    default boolean isShowCdmaUnavailable() {
        return false;
    }

    default boolean isSmartCallForwardSupported() {
        return false;
    }

    default boolean isSmartCallForwardedCall(String str) {
        return false;
    }

    default boolean isSupportEsim() {
        return false;
    }

    default boolean isSupportForRegion() {
        return false;
    }

    default boolean isSupportImsRegOptByDevice(Context context) {
        return false;
    }

    default boolean isSupportSmartCallForwardByDevice() {
        return false;
    }

    default boolean isSwitchDsdaModeByResetHwMbn() {
        return false;
    }

    default boolean isUTError(CommandException commandException) {
        return false;
    }

    default boolean isUtEnabled(Phone phone) {
        return false;
    }

    default boolean isVoiceLinkRegistered(Context context, int i) {
        return false;
    }

    default boolean maybeShowDialogForSuppService(Phone phone, int i) {
        return false;
    }

    default boolean maybeShowTurnOffVolteForCall(Phone phone) {
        return false;
    }

    default void openedDual4GConfirmPrompt() {
    }

    default void openedDual4GConfirmPromptWithSlot(int i) {
    }

    default void recordCallerInfo(int i, String str, String str2) {
    }

    default void recoveryForInit() {
    }

    default void removeParticipantsNum(Uri uri) {
    }

    default void reportCallOptimizeStatistic() {
    }

    default void reportConfigErrorStatistics(int i, boolean z, PersistableBundle persistableBundle) {
    }

    default void reset(int i) {
    }

    default void reset2GManager() {
    }

    default void resetImsSignalOptimizationToDefault() {
    }

    default void resetPortAndStateForSlot(int i) {
    }

    default boolean revertTeleAction(int i, int i2, Object... objArr) {
        return false;
    }

    default void saveForceSelectedVolteState(int i, boolean z) {
    }

    default void saveUserSelectedVolteState(int i, boolean z) {
    }

    default void setCallCommandMessage(String str, int i, int i2, int i3) {
    }

    default void setCrossSimCallingEnabled(Context context, int i, boolean z) {
    }

    default boolean setDsdaEnabled(boolean z, int i) {
        return false;
    }

    default void setDual4GMode(boolean z, Utils.Dual4GCallBack dual4GCallBack) {
    }

    default void setEsimIsActiveChangeToNoActive(boolean z) {
    }

    default void setEsimSecureValue(int i) {
    }

    default int setEsimState(int i) {
        return 0;
    }

    default void setEsimStateChanging(boolean z) {
    }

    default boolean shouldAddNrNetworkModes(int i) {
        return false;
    }

    default boolean shouldDoCallVonrOptimization() {
        return false;
    }

    default boolean shouldShowOsuDialog(long j) {
        return false;
    }

    default boolean shouldShowViceNetworkTypePref() {
        return false;
    }

    default void showArrearsSimDialog(int i) {
    }

    default void showNoFiveGDialog(int i) {
    }

    default void showOsuDialog() {
    }

    default void showSatelliteAlertDialog(int i) {
    }

    default boolean showWcdmaOnlyNetworkModes(int i) {
        return false;
    }

    default boolean teleAction(int i, int i2, Object... objArr) {
        return false;
    }

    default int toTelecomDisconnectCauseCode(int i) {
        return 0;
    }

    default Integer toTelecomDisconnectCauseDescription(Context context, int i, int i2, Integer num) {
        return num;
    }

    default void trigerNoCallServiceDialogChecker(boolean z) {
    }

    default void tryRepair(PersistableBundle persistableBundle, String str) {
    }

    default void updateConfigForVolteGlobalization(String str, PersistableBundle persistableBundle) {
    }

    default void updateRoamingCFUStatus(Phone phone, int i, int i2, String str, Throwable th) {
    }

    default void updateVoNrSwitchState(int i, boolean z) {
    }

    default void writeProductEid(byte[] bArr) {
    }

    default Intent getErrorDialogActivityIntent() {
        return new Intent();
    }

    default HashMap getGlobalizationCarrierConfigMap() {
        return new HashMap();
    }

    default ArrayList getAndUpdateOldSimSlotIds(Context context) {
        return new ArrayList();
    }

    default String queryFdn(String str) {
        return "";
    }

    default void setEnable2G(int i, boolean z, Message message) {
        replyMessageError(message);
    }

    default List getAllEsimProfiles() {
        return new ArrayList();
    }

    default String getProductEid() {
        return "";
    }

    default String getEidFromSlotId(int i) {
        return "";
    }

    default void getCallWaitingOption(Phone phone, Message message) {
        replyMessageError(message);
    }

    default void setCallWaitingOption(Phone phone, boolean z, Message message) {
        replyMessageError(message);
    }

    default void getCallForwardingOption(Phone phone, int i, int i2, Message message) {
        replyMessageError(message);
    }

    default void setCallForwardingOption(Phone phone, int i, int i2, String str, int i3, int i4, Message message) {
        replyMessageError(message);
    }

    default void getCallBarringOption(Phone phone, String str, String str2, Message message) {
        replyMessageError(message);
    }

    default void setCallBarringOption(Phone phone, String str, boolean z, String str2, Message message) {
        replyMessageError(message);
    }

    default void getPlmnList(Phone phone, IccFileHandler iccFileHandler, Message message) {
        replyMessageError(message);
    }

    default ArrayList convertPlmnList(int i, Object obj) {
        return new ArrayList();
    }

    default void setPlmnList(Phone phone, IccFileHandler iccFileHandler, ArrayList arrayList, Message message) {
        replyMessageError(message);
    }

    default int[] getSupportNetworkMode(int i) {
        return new int[0];
    }

    default int[][] getSupportModes() {
        return (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 0, 0);
    }

    static void replyMessageError(Message message) {
        if (message == null || message.getTarget() == null) {
            return;
        }
        AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
        message.sendToTarget();
    }
}
