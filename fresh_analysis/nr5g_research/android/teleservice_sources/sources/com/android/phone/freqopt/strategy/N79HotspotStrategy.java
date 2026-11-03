package com.android.phone.freqopt.strategy;

import android.telephony.CellIdentity;
import android.telephony.CellIdentityNr;
import com.android.phone.freqopt.core.IFreqOptStrategy;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.util.FreqOptCellListBuilder;
import com.android.phone.freqopt.util.FreqOptUtils;
import com.android.phone.freqopt.util.LogUtils;
import com.android.phone.freqopt.util.NrBandUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class N79HotspotStrategy implements IFreqOptStrategy {
    private long mHandoverTimeoutMs = 1200000;

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public int getLearnCountThreshold(int i) {
        return 2;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public long getTtlMs() {
        return 10368000000L;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean needsPostExecutionMonitoring() {
        return true;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean requiresConnectedStateForLearning() {
        return true;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean shouldRemoveOnFail(FreqOptLearningRecord freqOptLearningRecord) {
        return true;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public String getStrategyId() {
        return "n79_hotspot";
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean isSourceCell(CellIdentity cellIdentity) {
        if (!(cellIdentity instanceof CellIdentityNr)) {
            return false;
        }
        CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
        int nrarfcn = cellIdentityNr.getNrarfcn();
        int[] bands = cellIdentityNr.getBands();
        boolean zIsN79Cell = isN79Cell(nrarfcn, bands);
        LogUtils.d("Freq_Opt_N79Hotspot", "isSourceCell: nrarfcn=" + nrarfcn + ", bands=" + Arrays.toString(bands) + ", isN79=" + zIsN79Cell);
        return !zIsN79Cell;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean isTargetCell(CellIdentity cellIdentity) {
        if (!(cellIdentity instanceof CellIdentityNr)) {
            return false;
        }
        CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
        int nrarfcn = cellIdentityNr.getNrarfcn();
        int[] bands = cellIdentityNr.getBands();
        boolean zIsN79Cell = isN79Cell(nrarfcn, bands);
        LogUtils.d("Freq_Opt_N79Hotspot", "isTargetCell: nrarfcn=" + nrarfcn + ", bands=" + Arrays.toString(bands) + ", isN79=" + zIsN79Cell);
        return zIsN79Cell;
    }

    private static boolean isN79Cell(int i, int[] iArr) {
        int bandFromNrarfcn = NrBandUtils.getBandFromNrarfcn(i);
        if (bandFromNrarfcn > 0) {
            return bandFromNrarfcn == 79;
        }
        LogUtils.d("Freq_Opt_N79Hotspot", "isN79Cell: ARFCN " + i + " unknown, fallback to getBands=" + Arrays.toString(iArr));
        if (iArr != null && iArr.length != 0) {
            for (int i2 : iArr) {
                if (i2 == 79) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int findTargetBand(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return 0;
        }
        int i = iArr[0];
        for (int i2 : iArr) {
            if (i2 == 79) {
                return i2;
            }
        }
        return i;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public Map buildExtFields(CellIdentity cellIdentity) {
        HashMap map = new HashMap();
        if (cellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            String mccString = cellIdentityNr.getMccString();
            String mncString = cellIdentityNr.getMncString();
            if (mccString == null || mncString == null) {
                LogUtils.w("Freq_Opt_N79Hotspot", "buildExtFields: mcc/mnc is null, skip");
            } else {
                map.put("y_plmn", mccString.concat(mncString));
                int nrarfcn = cellIdentityNr.getNrarfcn();
                int bandFromNrarfcn = NrBandUtils.getBandFromNrarfcn(nrarfcn);
                if (bandFromNrarfcn > 0) {
                    map.put("y_band", Integer.valueOf(bandFromNrarfcn));
                } else {
                    int iFindTargetBand = findTargetBand(cellIdentityNr.getBands());
                    if (iFindTargetBand != 0) {
                        map.put("y_band", Integer.valueOf(iFindTargetBand));
                    }
                }
                map.put("y_freq", Integer.valueOf(nrarfcn));
                map.put("y_pci", Integer.valueOf(cellIdentityNr.getPci()));
                return map;
            }
        }
        return map;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public void executeOptimization(FreqOptLearningRecord freqOptLearningRecord, int i) {
        LogUtils.d("Freq_Opt_N79Hotspot", "executeOptimization record=" + freqOptLearningRecord);
        Map extFields = freqOptLearningRecord.getExtFields();
        if (extFields.isEmpty()) {
            LogUtils.w("Freq_Opt_N79Hotspot", "executeOptimization no ext data");
            return;
        }
        Object obj = extFields.get("y_plmn");
        String str = obj instanceof String ? (String) obj : null;
        int iIntValue = extFields.get("y_band") instanceof Number ? ((Number) extFields.get("y_band")).intValue() : 0;
        int iIntValue2 = extFields.get("y_freq") instanceof Number ? ((Number) extFields.get("y_freq")).intValue() : 0;
        int iIntValue3 = extFields.get("y_pci") instanceof Number ? ((Number) extFields.get("y_pci")).intValue() : 0;
        if (str == null || iIntValue2 <= 0 || iIntValue <= 0) {
            LogUtils.w("Freq_Opt_N79Hotspot", "executeOptimization invalid ext: plmn=" + str + " freq=" + iIntValue2 + " band=" + iIntValue);
            return;
        }
        FreqOptUtils.setCellListInfo(buildCellListInfo(str, FreqOptUtils.convertNrToActiveBand(iIntValue), iIntValue2, iIntValue3), "FreqOpt_N79Hotspot", null);
        LogUtils.i("Freq_Opt_N79Hotspot", "executeOptimization setCellListInfo done");
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public void cancelOptimization(String str, int i) {
        LogUtils.i("Freq_Opt_N79Hotspot", "cancelOptimization reason=" + str);
        FreqOptUtils.setCellListInfo(new ArrayList(), "FreqOpt_N79Hotspot_Cancel", null);
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public long getHandoverTimeoutMs() {
        return this.mHandoverTimeoutMs;
    }

    public void setHandoverTimeoutMs(long j) {
        this.mHandoverTimeoutMs = j;
    }

    private List buildCellListInfo(String str, int i, int i2, int i3) {
        return FreqOptCellListBuilder.buildCellListInfo(str, i, i2, i3, 1, 1, 30);
    }
}
