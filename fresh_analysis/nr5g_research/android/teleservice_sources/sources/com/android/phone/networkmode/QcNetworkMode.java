package com.android.phone.networkmode;

import android.content.res.Resources;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class QcNetworkMode extends NetworkMode {
    @Override // com.android.phone.networkmode.NetworkMode
    public int[][] getDeviceSupportModes() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        int[][] iArr = new int[i][];
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (NetworkMode.loadCustomRegionNetworkMode(iArr)) {
            MiuiPhoneUtils.printArray(iArr, "CustomRegion getDeviceSupportModes");
            return iArr;
        }
        if (i == 1) {
            iArr[0] = getDeviceMasterSupportModes();
            MiuiPhoneUtils.printArray(iArr, "QcNetworkMode getDeviceSupportModes");
            return iArr;
        }
        PhoneGlobals.getInstance().getResources();
        int[] gsmViceSupportModes = getGsmViceSupportModes();
        int[] cdmaViceSupportModes = getCdmaViceSupportModes();
        int[] gsmMasterSupportModes = getGsmMasterSupportModes();
        int[] cdmaMasterSupportModes = getCdmaMasterSupportModes();
        if (MiuiPhoneUtils.isHMH2xCmForThreeMode()) {
            gsmMasterSupportModes = NetworkMode.getSupportNetworkMode(4);
            cdmaMasterSupportModes = gsmMasterSupportModes;
        }
        int[] phoneTypesAccordingSimType = NetworkMode.getPhoneTypesAccordingSimType();
        if (phoneTypesAccordingSimType[defaultDataSlotId] == 2) {
            iArr[defaultDataSlotId] = cdmaMasterSupportModes;
            for (int i2 = 0; i2 < phoneTypesAccordingSimType.length; i2++) {
                if (i2 != defaultDataSlotId) {
                    if (phoneTypesAccordingSimType[i2] == 2 && TelephonyManager.getDefault().isDualNrSupported()) {
                        iArr[i2] = cdmaViceSupportModes;
                    } else {
                        iArr[i2] = gsmViceSupportModes;
                    }
                }
            }
        } else {
            iArr[defaultDataSlotId] = gsmMasterSupportModes;
            for (int i3 = 0; i3 < phoneTypesAccordingSimType.length; i3++) {
                if (i3 != defaultDataSlotId) {
                    iArr[i3] = phoneTypesAccordingSimType[i3] == 2 ? cdmaViceSupportModes : gsmViceSupportModes;
                }
            }
        }
        for (int i4 = 0; i4 < phoneTypesAccordingSimType.length; i4++) {
            iArr[i4] = NetworkMode.removeDeviceNrMode(iArr[i4], i4);
        }
        MiuiPhoneUtils.printArray(iArr, "QcNetworkMode getDeviceSupportModes");
        return iArr;
    }

    public int[] getDeviceMasterSupportModes() {
        PhoneGlobals.getInstance().getResources();
        int[] gsmMasterSupportModes = getGsmMasterSupportModes();
        int[] cdmaMasterSupportModes = getCdmaMasterSupportModes();
        if (NetworkMode.getPhoneTypesAccordingSimType()[0] == 2) {
            gsmMasterSupportModes = cdmaMasterSupportModes;
        }
        return NetworkMode.removeDeviceNrMode(gsmMasterSupportModes, DefaultSimManager.getDefaultDataSlotId());
    }

    @Override // com.android.phone.networkmode.NetworkMode
    public boolean isShowCdmaUnvailable() {
        return HyperOSCustFeatureResolve.getBoolean("support_cdma_unvailable", false);
    }

    @Override // com.android.phone.networkmode.NetworkMode
    public int[] getCdmaMasterSupportModes() {
        Resources resources = PhoneGlobals.getInstance().getResources();
        if (MiuiPhoneUtils.isDeviceNotSupportCDMA()) {
            return resources.getIntArray(R.array.support_networktypes_cdma_master);
        }
        return resources.getIntArray(R.array.support_networktypes_cdma_master_all);
    }

    @Override // com.android.phone.networkmode.NetworkMode
    public int[] getGsmMasterSupportModes() {
        return PhoneGlobals.getInstance().getResources().getIntArray(R.array.support_networktypes_gsm_master);
    }
}
