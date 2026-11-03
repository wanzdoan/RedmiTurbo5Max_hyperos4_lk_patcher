package com.android.phone;

import android.telephony.NetworkRegistrationInfo;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.phone.utils.FiveGUtils;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public class QcVoiceLinkController extends VoiceLinkControllerBase {
    @Override // com.android.phone.VoiceLinkControllerBase
    protected void tryEnableFeature() {
        tryEnableFeature(false);
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected void tryEnableFeature(boolean z) {
        log("tryEnableFeature");
        if (this.mIs2SimsReady && isNddsCrossSimEnabled() && !isNddsDataFeatureEnabled()) {
            this.mInNolimit = false;
            regStateMonitor(true);
            updateNolimitState("init", z);
        } else {
            this.mInNolimit = false;
            regStateMonitor(false);
            if (this.mIs2SimsReady && isNddsDataFeatureEnabled()) {
                setVodataFeature(this.mNdds, false, z);
            }
        }
    }

    protected void updateNolimitState(String str, boolean z) {
        if (isNddsDataFeatureEnabled()) {
            log("updateNolimitState:" + str + ", skip: ndds data feature enabled");
            return;
        }
        log("updateNolimitState:" + str + ", mInNolimit " + this.mInNolimit + " userScene " + z);
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        int viceSlotId = FiveGUtils.getViceSlotId();
        if (!this.mInNolimit && shouldEnableNolimit()) {
            setVodataFeature(this.mNdds, true, z);
            recordVoicelinkScene(defaultDataSlotId, viceSlotId, str);
        } else if (shouldReturnToDefault()) {
            this.mInNolimit = false;
            setVodataFeature(this.mNdds, false, z);
            recordVoicelinkScene(defaultDataSlotId, viceSlotId, "ReturnToDefault");
        }
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected void updateNolimitState(String str) {
        updateNolimitState(str, false);
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected boolean isNddsCrossSimEnabled() {
        boolean zIsCrossSimCallingEnabledByUser = PhoneAdapter.getInstance().isCrossSimCallingEnabledByUser(this.mContext, FiveGUtils.getViceSlotId());
        log("isNddsCrossSimEnabled:" + zIsCrossSimCallingEnabledByUser);
        return zIsCrossSimCallingEnabledByUser;
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected boolean shouldEnableForService() {
        if (isMaskSupported(1) && isServiceOOS()) {
            return true;
        }
        return isMaskSupported(8) && isServiceRoaming();
    }

    private boolean isServiceRoaming() {
        ServiceState[] serviceStateArr;
        ServiceState serviceState;
        return SubscriptionManager.isValidPhoneId(this.mDds) && SubscriptionManager.isValidPhoneId(this.mNdds) && (serviceState = (serviceStateArr = this.mSS)[this.mDds]) != null && serviceStateArr[this.mNdds] != null && !serviceState.getRoaming() && this.mSS[this.mNdds].getRoaming();
    }

    private boolean isServiceOOS() {
        if (SubscriptionManager.isValidPhoneId(this.mDds) && SubscriptionManager.isValidPhoneId(this.mNdds)) {
            ServiceState[] serviceStateArr = this.mSS;
            int i = this.mDds;
            if (serviceStateArr[i] != null && serviceStateArr[this.mNdds] != null) {
                Phone phone = MiuiPhoneUtils.getPhone(i);
                Phone phone2 = MiuiPhoneUtils.getPhone(this.mNdds);
                if (phone == null || phone2 == null) {
                    log("isServiceOOS: phone is null");
                } else {
                    ServiceStateTracker serviceStateTracker = phone.getServiceStateTracker();
                    ServiceStateTracker serviceStateTracker2 = phone2.getServiceStateTracker();
                    if (serviceStateTracker == null || serviceStateTracker2 == null) {
                        log("isServiceOOS: sst is null");
                        return false;
                    }
                    NetworkRegistrationInfo networkRegistrationInfo = serviceStateTracker2.getServiceState().getNetworkRegistrationInfo(2, 1);
                    return (serviceStateTracker.getServiceState().getDataRegState() != 0 || serviceStateTracker2.getServiceState().getVoiceRegState() == 0 || (networkRegistrationInfo != null && (networkRegistrationInfo.getNetworkRegistrationState() == 1 || networkRegistrationInfo.getNetworkRegistrationState() == 5))) ? false : true;
                }
            }
        }
        return false;
    }

    protected void setVodataFeature(int i, boolean z, boolean z2) {
        ImsManager imsManager;
        log(i + " VodataFeature enable: " + z + ", isFeatureEnabled:" + this.mIsFeatureEnabled + ", userScene: " + z2);
        if ((this.mIsFeatureEnabled && z && !z2) || (imsManager = ImsManager.getInstance(PhoneGlobals.getInstance(), i)) == null || imsManager.isCrossSimCallingEnabled() == z) {
            return;
        }
        log("setCrossSimCallingEnabled...");
        imsManager.setCrossSimCallingEnabled(z);
        if (z) {
            this.mInNolimit = true;
            this.mIsFeatureEnabled = true;
            this.mHandler.removeMessages(109);
            this.mHandler.sendEmptyMessageDelayed(109, getDelayTimer(z2));
        }
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected boolean isMaskSupported(int i) {
        int viceSlotId = FiveGUtils.getViceSlotId();
        int subscriptionId = SubscriptionManager.getSubscriptionId(viceSlotId);
        if (!SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
            return false;
        }
        int intConfigForSubId = CarrierConfigManagerEx.getIntConfigForSubId(MiuiPhoneUtils.getPhone(viceSlotId).getContext(), subscriptionId, "config_support_voice_link_scene", 1);
        logV("isMaskSupported:" + i + ", CarrierSceneMask:" + intConfigForSubId + ", subId:" + subscriptionId);
        return (intConfigForSubId & i) != 0;
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected boolean shouldReturnToDefault() {
        if (MiuiPhoneUtils.isPhoneInCall()) {
            return false;
        }
        boolean z = isMaskSupported(1) && isServiceOOS();
        if (this.mIsWifiConn || (this.mScreenOffTime < Long.MAX_VALUE && !z)) {
            log("shouldReturnToDefault: need disable");
            return true;
        }
        if (isRoamingScene() && !shouldEnableForService()) {
            log("shouldReturnToDefault: roaming scene need disable");
            return true;
        }
        boolean z2 = checkSignalStrength() > 0;
        boolean zShouldEnableForService = shouldEnableForService();
        boolean zShouldEnableForCall = shouldEnableForCall();
        boolean zShouldEnableForGame = shouldEnableForGame();
        StringBuilder sb = new StringBuilder("shouldReturnToDefault signal：");
        sb.append(z2);
        sb.append(" srv:");
        sb.append(!zShouldEnableForService);
        sb.append(" call:");
        sb.append(!zShouldEnableForCall);
        sb.append(" game:");
        sb.append(!zShouldEnableForGame);
        log(sb.toString());
        return (zShouldEnableForService || !z2 || zShouldEnableForCall || zShouldEnableForGame) ? false : true;
    }

    private static void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("QcVoiceLinkController", str);
        }
    }

    private static void log(String str) {
        Rlog.d("QcVoiceLinkController", str);
    }
}
