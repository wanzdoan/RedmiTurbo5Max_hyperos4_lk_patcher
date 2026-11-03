package com.android.phone.networkmode;

import android.content.res.Resources;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MtkNetworkMode extends NetworkMode {
    @Override // com.android.phone.networkmode.NetworkMode
    public int[][] getDeviceSupportModes() {
        int[][] iArr = new int[MiuiPhoneUtils.PHONE_COUNT][];
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (NetworkMode.loadCustomRegionNetworkMode(iArr)) {
            MiuiPhoneUtils.printArray(iArr, "CustomRegion getDeviceSupportModes");
            return iArr;
        }
        PhoneGlobals.getInstance().getResources();
        int[] phoneTypesAccordingSimType = NetworkMode.getPhoneTypesAccordingSimType();
        int[] gsmViceSupportModes = getGsmViceSupportModes();
        int[] cdmaViceSupportModes = getCdmaViceSupportModes();
        int[] gsmMasterSupportModes = getGsmMasterSupportModes();
        int[] cdmaMasterSupportModes = getCdmaMasterSupportModes();
        if (phoneTypesAccordingSimType[defaultDataSlotId] == 2) {
            iArr[defaultDataSlotId] = cdmaMasterSupportModes;
            for (int i = 0; i < phoneTypesAccordingSimType.length; i++) {
                if (i != defaultDataSlotId) {
                    if (phoneTypesAccordingSimType[i] == 2 && TelephonyManager.getDefault().isDualNrSupported()) {
                        iArr[i] = cdmaViceSupportModes;
                    } else {
                        iArr[i] = gsmViceSupportModes;
                    }
                }
            }
        } else {
            iArr[defaultDataSlotId] = gsmMasterSupportModes;
            for (int i2 = 0; i2 < phoneTypesAccordingSimType.length; i2++) {
                if (i2 != defaultDataSlotId) {
                    iArr[i2] = phoneTypesAccordingSimType[i2] == 2 ? cdmaViceSupportModes : gsmViceSupportModes;
                }
            }
        }
        for (int i3 = 0; i3 < phoneTypesAccordingSimType.length; i3++) {
            int[] iArr2 = iArr[i3];
            if (iArr2 != null) {
                iArr[i3] = NetworkMode.removeDeviceNrMode(iArr2, i3);
            }
        }
        MiuiPhoneUtils.printArray(iArr, "MtkNetworkMode getDeviceSupportModes");
        return iArr;
    }

    @Override // com.android.phone.networkmode.NetworkMode
    public int[] getCdmaMasterSupportModes() {
        Resources resources = PhoneGlobals.getInstance().getResources();
        if (MiuiPhoneUtils.isMtk5gDevice() || ModemProvider.IS_XRING) {
            return resources.getIntArray(R.array.support_networktypes_cdma_master_all);
        }
        return resources.getIntArray(R.array.support_networktypes_cdma_master);
    }

    @Override // com.android.phone.networkmode.NetworkMode
    public int[] getGsmMasterSupportModes() {
        Resources resources = PhoneGlobals.getInstance().getResources();
        if (MiuiPhoneUtils.isMtk5gDevice() || ModemProvider.IS_XRING) {
            return resources.getIntArray(R.array.support_networktypes_gsm_nr_master_mtk);
        }
        return resources.getIntArray(R.array.support_networktypes_gsm_master);
    }
}
