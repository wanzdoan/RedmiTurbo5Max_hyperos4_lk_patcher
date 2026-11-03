package com.android.phone.networkmode;

import android.os.AsyncResult;
import android.os.Message;
import android.provider.MiuiSettings;
import android.telephony.RadioAccessFamily;
import com.android.internal.util.ArrayUtils;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.NetworkModeManager;
import com.android.phone.PhoneGlobals;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.Arrays;
import java.util.Objects;
import miui.telephony.DefaultSimManager;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyUtils;
import miui.telephony.VirtualSimUtils;

/* JADX INFO: loaded from: classes.dex */
public class NetworkModeHandlerDataSlot extends NetworkModeHandlerBase {

    public class SettingArgs {
        int dataSlot;
        int mode;
        int times = 0;

        public String toString() {
            StringBuilder sb = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
            sb.append("SettingArgs: dataSlot=");
            sb.append(this.dataSlot);
            sb.append(" times=");
            sb.append(this.times);
            sb.append(" mode=");
            sb.append(this.mode);
            return sb.toString();
        }

        public SettingArgs(NetworkModeHandlerDataSlot networkModeHandlerDataSlot, int i) {
            this.dataSlot = i;
            this.mode = ModemProvider.getInstance().getPreferredNetworkModeFromDb(this.dataSlot);
        }
    }

    @Override // com.android.phone.networkmode.NetworkModeHandlerBase
    protected void handleSetNetworkTypeDone(Message message) {
        AsyncResult asyncResult = (AsyncResult) message.obj;
        SettingArgs settingArgs = (SettingArgs) asyncResult.userObj;
        if (asyncResult.exception != null) {
            sendMessageDelayed(obtainMessage(2, settingArgs), 2000L);
        }
    }

    @Override // com.android.phone.networkmode.NetworkModeHandlerBase
    protected void handleSetNetworkTypeRetry(Message message) {
        SettingArgs settingArgs = (SettingArgs) message.obj;
        NetworkModeManager.log("handleSetNetworkTypeRetry " + settingArgs);
        if (settingArgs.times >= 3 || DefaultSimManager.getDefaultDataSlotId() != settingArgs.dataSlot) {
            getPreferredNetworkTypes();
            return;
        }
        settingArgs.times++;
        MiuiPhoneUtils.getPhone(settingArgs.dataSlot).setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(settingArgs.mode), obtainMessage(1, settingArgs));
    }

    @Override // com.android.phone.networkmode.NetworkModeHandlerBase
    protected void updateNetworkTypeIfNeeded() {
        int[] iArr;
        if (isReadyToUpdate()) {
            int i = this.mDefaultDataSlotId;
            this.mDefaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            String strPii = this.mVirtualSimIccId;
            String virtualSimIccId = getVirtualSimIccId();
            this.mVirtualSimIccId = virtualSimIccId;
            boolean z = (this.mDefaultDataSlotId == i && Objects.equals(virtualSimIccId, strPii)) ? false : true;
            int virtualSimSlotId = MiuiSettings.VirtualSim.getVirtualSimSlotId(PhoneGlobals.getInstance());
            StringBuilder sb = new StringBuilder(512);
            sb.append("updateNetworkTypeIfNeeded defaultSlotId=");
            sb.append(this.mDefaultDataSlotId);
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
            sb.append(") virtualSlot=");
            sb.append(virtualSimSlotId);
            if (VirtualSimUtils.getInstance().isVirtualSimEnabled()) {
                if (this.mVirtualSimIccId.isEmpty() || this.mDefaultDataSlotId != virtualSimSlotId) {
                    NetworkModeManager.log(sb.toString());
                    return;
                } else if (MiuiSettings.VirtualSim.isSupport4G(PhoneGlobals.getInstance())) {
                    sb.append(" support4G");
                    NetworkModeManager.log(sb.toString());
                    return;
                }
            }
            int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(this.mDefaultDataSlotId);
            int[][] supportModes = NetworkMode.getInstance().getSupportModes();
            int i2 = ((z || !ArrayUtils.contains(supportModes[this.mDefaultDataSlotId], preferredNetworkModeFromDb)) && (iArr = supportModes[this.mDefaultDataSlotId]) != null && iArr.length > 0) ? iArr[0] : preferredNetworkModeFromDb;
            sb.append(" dbMode=");
            sb.append(preferredNetworkModeFromDb);
            sb.append(" newDbMode=");
            sb.append(i2);
            sb.append(" supportModes=");
            sb.append(Arrays.toString(supportModes[this.mDefaultDataSlotId]));
            NetworkModeManager.log(sb.toString());
            ModemProvider.getInstance().setNetworkModeInDb(this.mDefaultDataSlotId, i2);
            for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                FiveGManagerBase.setFivegEnabled(ArrayUtils.contains(NetworkMode.getSupportNetworkMode(9), ModemProvider.getInstance().getPreferredNetworkModeFromDb(i3)), i3);
            }
            SettingArgs settingArgs = new SettingArgs(this, this.mDefaultDataSlotId);
            NetworkModeManager.log("setAllowedNetworkTypes " + settingArgs);
            removeMessages(1);
            removeMessages(2);
            MiuiPhoneUtils.getPhone(this.mDefaultDataSlotId).setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(settingArgs.mode), obtainMessage(1, settingArgs));
        }
    }
}
