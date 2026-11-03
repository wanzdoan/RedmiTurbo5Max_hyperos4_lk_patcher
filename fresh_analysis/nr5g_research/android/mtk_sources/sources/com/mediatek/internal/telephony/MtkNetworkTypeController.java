package com.mediatek.internal.telephony;

import android.os.PersistableBundle;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityNr;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.Rlog;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.DisplayInfoController;
import com.android.internal.telephony.NetworkTypeController;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public final class MtkNetworkTypeController extends NetworkTypeController {
    public static final String TAG = "MtkNetworkTypeController";
    private int mNrAdvancedPhysicalChannelConfigItemCount;
    private final Set<ArrayList<Integer>> mSaNrAdvancedSpecialBandListSets;

    public MtkNetworkTypeController(Phone phone, DisplayInfoController displayInfoController, FeatureFlags featureFlags) {
        super(phone, displayInfoController, featureFlags);
        this.mNrAdvancedPhysicalChannelConfigItemCount = 0;
        this.mSaNrAdvancedSpecialBandListSets = new HashSet();
    }

    protected boolean isNrAdvanced() {
        if (this.mNrAdvancedPhysicalChannelConfigItemCount > 0) {
            int physicalChannelItemCount = this.mPhysicalChannelConfigs == null ? 0 : this.mPhysicalChannelConfigs.size();
            int dataNetworkType = getDataNetworkType();
            if (dataNetworkType != 20) {
                return false;
            }
            if (physicalChannelItemCount != this.mNrAdvancedPhysicalChannelConfigItemCount) {
                final ArrayList<Integer> bands = new ArrayList<>();
                if (this.mPhysicalChannelConfigs != null) {
                    this.mPhysicalChannelConfigs.forEach(new Consumer() { // from class: com.mediatek.internal.telephony.MtkNetworkTypeController$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            bands.add(Integer.valueOf(((PhysicalChannelConfig) obj).getBand()));
                        }
                    });
                }
                if (!isSpecialSaNrAdvancedBandsSet(bands)) {
                    return false;
                }
            }
        }
        return super.isNrAdvanced();
    }

    private boolean isSpecialSaNrAdvancedBandsSet(ArrayList<Integer> bands) {
        if (bands.size() <= 0) {
            return false;
        }
        Collections.sort(bands);
        for (ArrayList<Integer> list : this.mSaNrAdvancedSpecialBandListSets) {
            if (list.equals(bands)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAdditionalNrAdvancedBand(Set<Integer> bands) {
        boolean result = false;
        if (this.mPhysicalChannelConfigs == null || this.mPhysicalChannelConfigs.isEmpty()) {
            NetworkRegistrationInfo nri = this.mServiceState.getNetworkRegistrationInfo(2, 1);
            if (nri == null || nri.getCellIdentity() == null) {
                return false;
            }
            CellIdentity cellIdentity = nri.getCellIdentity();
            if (cellIdentity.getType() != 6) {
                return false;
            }
            int[] cellBands = ((CellIdentityNr) cellIdentity).getBands();
            for (int band : cellBands) {
                if (this.mAdditionalNrAdvancedBands.contains(Integer.valueOf(band))) {
                    log("use additional NR band[" + band + "] as MMWave");
                    result = true;
                }
            }
            return result;
        }
        boolean result2 = super.isAdditionalNrAdvancedBand(bands);
        return result2;
    }

    protected void onParseCarrierConfigs(PersistableBundle config) {
        this.mNrAdvancedPhysicalChannelConfigItemCount = config.getInt("mtk_nr_advanced_physical_channel_config_items_count_int", 0);
        Rlog.d(TAG, "mNrAdvancedPhysicalChannelConfigItemCount = " + this.mNrAdvancedPhysicalChannelConfigItemCount);
        String[] bandArrays = config.getStringArray("mtk_sa_nr_advanced_special_bands_combinations_array");
        if (bandArrays != null) {
            for (String bandArray : bandArrays) {
                Rlog.d(TAG, "Special SA NR Advanced band combination = " + bandArray);
                String[] bands = bandArray.split(",");
                ArrayList<Integer> bandList = new ArrayList<>();
                for (String bandString : bands) {
                    try {
                        Integer bandInt = Integer.valueOf(Integer.parseInt(bandString.trim()));
                        bandList.add(bandInt);
                    } catch (NumberFormatException e) {
                        Rlog.e(TAG, "NumberFormatException on " + bandString);
                    }
                }
                if (bandList.size() > 0) {
                    Collections.sort(bandList);
                    this.mSaNrAdvancedSpecialBandListSets.add(bandList);
                }
            }
        }
    }
}
