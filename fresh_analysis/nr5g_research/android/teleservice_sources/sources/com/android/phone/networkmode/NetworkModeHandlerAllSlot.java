package com.android.phone.networkmode;

import android.os.AsyncResult;
import android.os.Message;
import android.telephony.RadioAccessFamily;
import com.android.internal.util.ArrayUtils;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkModeManager;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.utils.FiveGUtils;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.Arrays;
import java.util.Objects;
import miui.telephony.DefaultSimManager;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class NetworkModeHandlerAllSlot extends NetworkModeHandlerBase {

    public class SettingArgs {
        int dataSlot;
        int index;
        int[] modes;
        int[] times;

        public String toString() {
            int slot = getSlot();
            StringBuilder sb = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
            sb.append("SettingArgs: currentSlot=");
            sb.append(slot);
            sb.append(" mode=");
            sb.append(this.modes[slot]);
            sb.append(" dataSlot=");
            sb.append(this.dataSlot);
            sb.append(" index=");
            sb.append(this.index);
            sb.append(" times=");
            sb.append(this.times[slot]);
            return sb.toString();
        }

        SettingArgs(NetworkModeHandlerAllSlot networkModeHandlerAllSlot, int i) {
            this.dataSlot = i;
            TelephonyManager telephonyManager = TelephonyManager.getDefault();
            int i2 = this.dataSlot + 1;
            int i3 = MiuiPhoneUtils.PHONE_COUNT;
            boolean zHasIccCard = telephonyManager.hasIccCard(i2 % i3);
            int i4 = this.dataSlot;
            if (zHasIccCard) {
                this.index = i4 + 1;
            } else {
                this.index = i4;
            }
            this.times = new int[i3];
            this.modes = new int[i3];
            for (int i5 = 0; i5 < MiuiPhoneUtils.PHONE_COUNT; i5++) {
                this.times[i5] = 0;
                this.modes[i5] = ModemProvider.getInstance().getPreferredNetworkModeFromDb(i5);
            }
        }

        void updateMode(int i) {
            if (i >= 0) {
                int[] iArr = this.modes;
                if (i < iArr.length) {
                    iArr[i] = ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
                }
            }
        }

        int getSlot() {
            return this.index % MiuiPhoneUtils.PHONE_COUNT;
        }
    }

    @Override // com.android.phone.networkmode.NetworkModeHandlerBase
    protected void handleSetNetworkTypeDone(Message message) {
        AsyncResult asyncResult = (AsyncResult) message.obj;
        Object obj = asyncResult.userObj;
        SettingArgs settingArgs = (SettingArgs) obj;
        if (asyncResult.exception != null) {
            sendMessageDelayed(obtainMessage(2, obj), 2000L);
            return;
        }
        int i = settingArgs.index;
        if (i == settingArgs.dataSlot + MiuiPhoneUtils.PHONE_COUNT) {
            return;
        }
        settingArgs.index = i + 1;
        settingArgs.updateMode(settingArgs.getSlot());
        NetworkModeManager.log("setAllowedNetworkTypes " + settingArgs);
        MiuiPhoneUtils.getPhone(settingArgs.getSlot()).setAllowedNetworkTypes(0, (long) RadioAccessFamily.getRafFromNetworkType(settingArgs.modes[settingArgs.getSlot()]), obtainMessage(1, settingArgs));
    }

    @Override // com.android.phone.networkmode.NetworkModeHandlerBase
    protected void handleSetNetworkTypeRetry(Message message) {
        SettingArgs settingArgs = (SettingArgs) message.obj;
        NetworkModeManager.log("handleSetNetworkTypeRetry " + settingArgs);
        if (settingArgs.times[settingArgs.getSlot()] >= 3 || DefaultSimManager.getDefaultDataSlotId() != settingArgs.dataSlot) {
            getPreferredNetworkTypes();
            return;
        }
        settingArgs.updateMode(settingArgs.getSlot());
        int[] iArr = settingArgs.times;
        int slot = settingArgs.getSlot();
        iArr[slot] = iArr[slot] + 1;
        MiuiPhoneUtils.getPhone(settingArgs.getSlot()).setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(settingArgs.modes[settingArgs.getSlot()]), obtainMessage(1, settingArgs));
    }

    @Override // com.android.phone.networkmode.NetworkModeHandlerBase
    protected void updateNetworkTypeIfNeeded() {
        int[] iArr;
        int nrNetworkModeValue;
        FiveGManagerBase fiveGManagerBase;
        if (isReadyToUpdate()) {
            int i = this.mDefaultDataSlotId;
            this.mDefaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            String strPii = this.mVirtualSimIccId;
            String virtualSimIccId = getVirtualSimIccId();
            this.mVirtualSimIccId = virtualSimIccId;
            boolean z = (this.mDefaultDataSlotId == i && Objects.equals(virtualSimIccId, strPii)) ? false : true;
            StringBuilder sb = new StringBuilder(512);
            sb.append("updateNetworkTypeIfNeeded defaultSlotId=");
            sb.append(this.mDefaultDataSlotId);
            sb.append(" isUpdate=");
            sb.append(z);
            sb.append(" (old=");
            sb.append(i);
            sb.append(") virtualSimIccId=");
            boolean z2 = PhoneDebug.VDBG;
            String strPii2 = this.mVirtualSimIccId;
            if (!z2) {
                strPii2 = TelephonyUtils.pii(strPii2);
            }
            sb.append(strPii2);
            sb.append(" (old=");
            if (!PhoneDebug.VDBG) {
                strPii = TelephonyUtils.pii(strPii);
            }
            sb.append(strPii);
            sb.append(')');
            int[][] supportModes = NetworkMode.getInstance().getSupportModes();
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(i2);
                if ((z || !ArrayUtils.contains(supportModes[i2], preferredNetworkModeFromDb) || MiuiPhoneUtils.hideNetmodeMenuAndUseDefaultRat(i2)) && (iArr = supportModes[i2]) != null && iArr.length > 0) {
                    if (iArr.length == 0) {
                        sb.append(" No valid networkMode, skip update dbMode");
                    } else {
                        nrNetworkModeValue = iArr[0];
                    }
                } else {
                    nrNetworkModeValue = preferredNetworkModeFromDb;
                }
                if (FiveGUtils.isFiveGNetWorkInVisible() && NetworkModeManager.isRemoveNetworkModeSettings(i2) && (fiveGManagerBase = FiveGManagerBase.getInstance()) != null && ((i2 == this.mDefaultDataSlotId && FiveGManagerBase.isFiveGCapable()) || (i2 != this.mDefaultDataSlotId && TelephonyManager.getDefault().isDualNrSupported()))) {
                    boolean zIsCmccSim = MiuiPhoneUtils.isCmccSim(MiuiPhoneUtils.getPhone(i2));
                    if (((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isFiveGEnableByCloud(i2) || ((FiveGUtils.isForcedRecoveryNr() && zIsCmccSim) || FiveGUtils.isFiveGInvisableByDefault())) {
                        sb.append(" isCmccSim=");
                        sb.append(zIsCmccSim);
                        sb.append(" forceNrByCloud=true");
                        if (!fiveGManagerBase.isUserFiveGEnabled(i2)) {
                            FiveGManagerBase.setFivegEnabled(true, i2);
                            nrNetworkModeValue = NetworkMode.getInstance().getNrNetworkModeValue(i2);
                        }
                    }
                }
                sb.append(" slot=");
                sb.append(i2);
                sb.append(" dbMode=");
                sb.append(preferredNetworkModeFromDb);
                sb.append(" newDbMode=");
                sb.append(nrNetworkModeValue);
                sb.append(" supportModes=");
                sb.append(Arrays.toString(supportModes[i2]));
                ModemProvider.getInstance().setNetworkModeInDb(i2, nrNetworkModeValue);
            }
            NetworkModeManager.log(sb.toString());
            SettingArgs settingArgs = new SettingArgs(this, this.mDefaultDataSlotId);
            NetworkModeManager.log("setAllowedNetworkTypes " + settingArgs);
            removeMessages(1);
            removeMessages(2);
            MiuiPhoneUtils.getPhone(settingArgs.getSlot()).setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(settingArgs.modes[settingArgs.getSlot()]), obtainMessage(1, settingArgs));
            if (NetworkModeManager.isShowCdmaUnavailable()) {
                ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).showCdmaUnavailableAlertDialog();
            }
        }
    }
}
