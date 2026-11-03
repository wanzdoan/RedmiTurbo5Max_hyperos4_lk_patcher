package com.android.phone;

import android.content.Context;
import android.database.ContentObserver;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.phone.utils.FiveGUtils;
import com.android.services.telephony.ims.ImsAdapterBase;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public class VoiceLinkController extends VoiceLinkControllerBase {
    private int mCarrierSceneMask;

    public static VoiceLinkControllerBase init() {
        if (ModemProvider.IS_QCOM) {
            if (VoiceLinkControllerBase.sInstance == null) {
                VoiceLinkControllerBase.sInstance = new QcVoiceLinkController();
            }
        } else if (ModemProvider.IS_MTK && VoiceLinkControllerBase.sInstance == null) {
            VoiceLinkControllerBase.sInstance = new VoiceLinkController();
        }
        return VoiceLinkControllerBase.sInstance;
    }

    public VoiceLinkController() {
        this.mContext.getContentResolver().registerContentObserver(SubscriptionManager.CONTENT_URI, true, new ContentObserver(this.mHandler) { // from class: com.android.phone.VoiceLinkController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                VoiceLinkController.logV("siminfo onChange");
                VoiceLinkController.this.mHandler.sendEmptyMessage(100);
            }
        });
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected void tryEnableFeature() {
        logV("tryEnableFeature");
        if (this.mIs2SimsReady && isNddsCrossSimEnabled() && !isNddsDataFeatureEnabled()) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                setVodataFeature(i, true, true);
            }
            this.mInNolimit = false;
            regStateMonitor(true);
            updateNolimitState("init");
            return;
        }
        regStateMonitor(false);
        if (!isNddsCrossSimEnabled()) {
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                setVodataFeature(i2, false, true);
            }
        } else {
            for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                setVodataFeature(i3, true, true);
            }
        }
        this.mInNolimit = false;
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected void updateNolimitState(String str) {
        logV("updateNolimitState:" + str);
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        int viceSlotId = FiveGUtils.getViceSlotId();
        if (!this.mInNolimit && shouldEnableNolimit()) {
            this.mInNolimit = true;
            setVodataFeature(this.mNdds, true, false);
            recordVoicelinkScene(defaultDataSlotId, viceSlotId, str);
        } else if (this.mInNolimit && shouldReturnToDefault()) {
            this.mInNolimit = false;
            setVodataFeature(this.mNdds, true, true);
            recordVoicelinkScene(defaultDataSlotId, viceSlotId, "ReturnToDefault");
        }
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected boolean isNddsCrossSimEnabled() {
        int viceSlotId = FiveGUtils.getViceSlotId();
        boolean z = SubscriptionManager.isValidPhoneId(viceSlotId) && ImsAdapterBase.getInstance().isCrossSimCallingEnabled(this.mContext, viceSlotId);
        log("isNddsCrossSimEnabled:" + z);
        return z;
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected boolean shouldEnableForService() {
        ServiceState[] serviceStateArr;
        ServiceState serviceState;
        if (SubscriptionManager.isValidPhoneId(this.mDds) && SubscriptionManager.isValidPhoneId(this.mNdds) && (serviceState = (serviceStateArr = this.mSS)[this.mDds]) != null && serviceStateArr[this.mNdds] != null) {
            boolean z = serviceState.getDataRegState() == 0 && this.mSS[this.mNdds].getDataRegState() != 0;
            boolean z2 = !this.mSS[this.mDds].getRoaming() && this.mSS[this.mNdds].getRoaming();
            if (isMaskSupported(1) && z) {
                return true;
            }
            if (isMaskSupported(8) && z2) {
                return true;
            }
        }
        return false;
    }

    private void setVodataFeature(int i, boolean z, boolean z2) {
        log(i + " VodataFeature OOS:" + z2 + " enable:" + z);
        StringBuilder sb = new StringBuilder("AT+EVODATA=");
        sb.append(z ? "1," : "0,");
        sb.append(z2 ? "1," : "0,");
        sb.append(SystemProperties.get("persist.vendor.mtk.vodata.allow_rat", "0"));
        FiveGManagerBase.getInstance().atCmdRunSync(i, new String[]{sb.toString(), ""}, null);
    }

    @Override // com.android.phone.VoiceLinkControllerBase
    protected boolean isMaskSupported(int i) {
        Context context = this.mContext;
        if ((context == null || context.getResources() == null) ? false : this.mContext.getResources().getBoolean(R.bool.config_voice_link_supported_scenes_by_operator)) {
            int viceSlotId = FiveGUtils.getViceSlotId();
            int subscriptionId = SubscriptionManager.getSubscriptionId(viceSlotId);
            if (!SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
                return false;
            }
            this.mCarrierSceneMask = CarrierConfigManagerEx.getIntConfigForSubId(MiuiPhoneUtils.getPhone(viceSlotId).getContext(), subscriptionId, "config_support_voice_link_scene", 1);
            logV("isMaskSupported:" + i + ", mCarrierSceneMask:" + this.mCarrierSceneMask + ", subId:" + subscriptionId);
            return (this.mCarrierSceneMask & i) != 0;
        }
        logV("isMaskSupported:" + i + ", mSceneMask:" + this.mSceneMask);
        return (this.mSceneMask & i) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logV(String str) {
        if (PhoneDebug.VDBG) {
            Rlog.d("VoiceLinkController", str);
        }
    }

    private static void log(String str) {
        Rlog.d("VoiceLinkController", str);
    }
}
