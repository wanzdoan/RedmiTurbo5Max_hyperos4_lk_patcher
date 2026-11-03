package com.android.phone.freqopt.strategy;

import android.content.Context;
import android.os.Handler;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityNr;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.R;
import com.android.phone.freqopt.core.IFreqOptStrategy;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.util.FreqOptCellListBuilder;
import com.android.phone.freqopt.util.FreqOptReporter;
import com.android.phone.freqopt.util.FreqOptUtils;
import com.android.phone.freqopt.util.LogUtils;
import com.android.phone.freqopt.util.NrBandUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class FddTddStrategy implements IFreqOptStrategy {
    private Runnable mConnectedHdrRunnable;
    private final Context mContext;
    private boolean mDebugSkipSourceCellCheck;
    private final Handler mHandler;
    private volatile boolean mHdrActive;
    private Runnable mIdleOutRunnable;
    private final Set mFddBands = new HashSet();
    private final Set mTddBands = new HashSet();
    private int mLastCoverageSlotId = -1;

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public int getLearnCountThreshold(int i) {
        return 3;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public long getTtlMs() {
        return 10368000000L;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean needsConnectedRateCheck() {
        return true;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean shouldRemoveOnFail(FreqOptLearningRecord freqOptLearningRecord) {
        return false;
    }

    public FddTddStrategy(Handler handler, Context context) {
        this.mHandler = handler;
        this.mContext = context;
        initBandData();
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public String getStrategyId() {
        return "fdd_tdd";
    }

    private void initBandData() {
        loadBandSet(R.array.band_fdd, this.mFddBands);
        loadBandSet(R.array.band_tdd, this.mTddBands);
        LogUtils.d("Freq_Opt_FddTdd", "initBandData");
    }

    private void loadBandSet(int i, Set set) {
        String[] stringArray = this.mContext.getResources().getStringArray(i);
        if (stringArray != null) {
            Collections.addAll(set, stringArray);
        }
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean isSourceCell(CellIdentity cellIdentity) {
        return isCellOfType(cellIdentity, 1, this.mFddBands, "Source");
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public boolean isTargetCell(CellIdentity cellIdentity) {
        return isCellOfType(cellIdentity, 2, this.mTddBands, "Target");
    }

    private boolean isCellOfType(CellIdentity cellIdentity, int i, Set set, String str) {
        if (!(cellIdentity instanceof CellIdentityNr)) {
            return false;
        }
        CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
        int nrarfcn = cellIdentityNr.getNrarfcn();
        int duplexTypeFromNrarfcn = NrBandUtils.getDuplexTypeFromNrarfcn(nrarfcn);
        if (duplexTypeFromNrarfcn == i) {
            LogUtils.d("Freq_Opt_FddTdd", "is" + str + "Cell: true (NRARFCN=" + nrarfcn + ")");
            return true;
        }
        if (duplexTypeFromNrarfcn != 0) {
            LogUtils.d("Freq_Opt_FddTdd", "is" + str + "Cell: false (NRARFCN=" + nrarfcn + ")");
            return false;
        }
        int[] bands = cellIdentityNr.getBands();
        LogUtils.d("Freq_Opt_FddTdd", "is" + str + "Cell fallback to getBands: nrarfcn=" + nrarfcn);
        if (bands != null && bands.length != 0) {
            for (int i2 : bands) {
                if (set.contains("N" + i2)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public Map buildExtFields(CellIdentity cellIdentity) {
        HashMap map = new HashMap();
        if (cellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            map.put("y_plmn", cellIdentityNr.getMccString() + cellIdentityNr.getMncString());
            int nrarfcn = cellIdentityNr.getNrarfcn();
            int bandFromNrarfcn = NrBandUtils.getBandFromNrarfcn(nrarfcn);
            if (bandFromNrarfcn > 0) {
                map.put("y_band", Integer.valueOf(bandFromNrarfcn));
            } else {
                int[] bands = cellIdentityNr.getBands();
                if (bands != null && bands.length > 0) {
                    int i = bands[0];
                    for (int i2 : bands) {
                        if (this.mTddBands.contains("N" + i2)) {
                            i = i2;
                            break;
                        }
                    }
                    map.put("y_band", Integer.valueOf(i));
                }
            }
            map.put("y_freq", Integer.valueOf(nrarfcn));
            map.put("y_pci", Integer.valueOf(cellIdentityNr.getPci()));
        }
        return map;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public Map buildExtFields(CellIdentity cellIdentity, long j) {
        if (j > 0) {
            return buildExtFieldsWithDwell(cellIdentity, j);
        }
        return buildExtFields(cellIdentity);
    }

    public Map buildExtFieldsWithDwell(CellIdentity cellIdentity, long j) {
        Map mapBuildExtFields = buildExtFields(cellIdentity);
        if (j > 0) {
            mapBuildExtFields.put("t_min", Long.valueOf(j));
        }
        return mapBuildExtFields;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public void executeOptimization(FreqOptLearningRecord freqOptLearningRecord, int i) {
        executeOptimization(freqOptLearningRecord, i, 0);
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public void executeOptimization(FreqOptLearningRecord freqOptLearningRecord, int i, int i2) {
        LogUtils.d("Freq_Opt_FddTdd", "executeOptimization record=" + freqOptLearningRecord + ", slotId=" + i + ", connectionState=" + i2);
        Map extFields = freqOptLearningRecord.getExtFields();
        if (i2 == 0) {
            executeConnected(freqOptLearningRecord, extFields, i);
        } else {
            executeIdle(freqOptLearningRecord, extFields, i);
        }
        LogUtils.d("Freq_Opt_FddTdd", "executeOptimization done");
    }

    private void executeConnected(final FreqOptLearningRecord freqOptLearningRecord, final Map map, final int i) {
        FreqOptUtils.setCoverageState(i, 1, "FreqOpt_FddTdd_Connected", this.mHandler);
        this.mLastCoverageSlotId = i;
        reportFddTddEvent("fdd_tdd_connected_coverage", freqOptLearningRecord.getSourceCellId(), freqOptLearningRecord.getTargetCellId(), 0);
        Handler handler = this.mHandler;
        if (handler != null) {
            Runnable runnable = this.mConnectedHdrRunnable;
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: com.android.phone.freqopt.strategy.FddTddStrategy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$executeConnected$0(freqOptLearningRecord, map, i);
                }
            };
            this.mConnectedHdrRunnable = runnable2;
            this.mHandler.postDelayed(runnable2, 2000L);
            return;
        }
        LogUtils.w("Freq_Opt_FddTdd", "executeConnected: handler is null, skip delayed HDR");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: sendHdrIfStillOnSource, reason: merged with bridge method [inline-methods] */
    public void lambda$executeConnected$0(FreqOptLearningRecord freqOptLearningRecord, Map map, int i) {
        if (!this.mDebugSkipSourceCellCheck) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null) {
                LogUtils.w("Freq_Opt_FddTdd", "sendHdrIfStillOnSource: phone is null for slotId=" + i);
                return;
            } else {
                CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
                if (currentCellIdentity == null || !isSourceCell(currentCellIdentity)) {
                    LogUtils.d("Freq_Opt_FddTdd", "executeConnected HDR skipped, UE left FDD source cell");
                    FreqOptUtils.setCoverageState(i, 2, "FreqOpt_FddTdd_Connected_Out_CellChanged", this.mHandler);
                    this.mLastCoverageSlotId = -1;
                    return;
                }
            }
        } else {
            LogUtils.d("Freq_Opt_FddTdd", "[DEBUG] skipping source cell check");
        }
        try {
            sendHdrCellList(freqOptLearningRecord, map);
        } finally {
            FreqOptUtils.setCoverageState(i, 2, "FreqOpt_FddTdd_Connected_Out", this.mHandler);
            this.mLastCoverageSlotId = -1;
        }
    }

    private void sendHdrCellList(FreqOptLearningRecord freqOptLearningRecord, Map map) {
        Object obj = map.get("y_plmn");
        String str = obj instanceof String ? (String) obj : null;
        int iIntValue = map.get("y_band") instanceof Number ? ((Number) map.get("y_band")).intValue() : 0;
        int iIntValue2 = map.get("y_freq") instanceof Number ? ((Number) map.get("y_freq")).intValue() : 0;
        int iIntValue3 = map.get("y_pci") instanceof Number ? ((Number) map.get("y_pci")).intValue() : 0;
        if (str == null || iIntValue2 <= 0) {
            LogUtils.d("Freq_Opt_FddTdd", "executeConnected HDR skipped, insufficient TDD cell info");
            return;
        }
        int iConvertNrToActiveBand = FreqOptUtils.convertNrToActiveBand(iIntValue);
        FreqOptUtils.setCellListInfo(buildCellListInfo(str, iConvertNrToActiveBand, iIntValue2, iIntValue3), "FreqOpt_FddTdd_HDR", this.mHandler);
        this.mHdrActive = true;
        LogUtils.d("Freq_Opt_FddTdd", "executeConnected HDR sent, plmn=" + str + ", band=" + iIntValue + ", activeBand=" + iConvertNrToActiveBand + ", freq=" + iIntValue2 + ", pci=" + iIntValue3);
        reportFddTddEvent("fdd_tdd_connected_hdr", freqOptLearningRecord.getSourceCellId(), freqOptLearningRecord.getTargetCellId(), 0);
    }

    private void executeIdle(FreqOptLearningRecord freqOptLearningRecord, Map map, final int i) {
        FreqOptUtils.setCoverageState(i, 1, "FreqOpt_FddTdd_Idle", this.mHandler);
        this.mLastCoverageSlotId = i;
        reportFddTddEvent("fdd_tdd_idle_coverage", freqOptLearningRecord.getSourceCellId(), freqOptLearningRecord.getTargetCellId(), 1);
        Handler handler = this.mHandler;
        if (handler != null) {
            Runnable runnable = this.mIdleOutRunnable;
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: com.android.phone.freqopt.strategy.FddTddStrategy$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$executeIdle$1(i);
                }
            };
            this.mIdleOutRunnable = runnable2;
            this.mHandler.postDelayed(runnable2, 2000L);
            return;
        }
        LogUtils.w("Freq_Opt_FddTdd", "executeIdle: handler is null, skip delayed OUT restore");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$executeIdle$1(int i) {
        FreqOptUtils.setCoverageState(i, 2, "FreqOpt_FddTdd_Idle_Out", this.mHandler);
        this.mLastCoverageSlotId = -1;
    }

    @Override // com.android.phone.freqopt.core.IFreqOptStrategy
    public void cancelOptimization(String str, int i) {
        LogUtils.i("Freq_Opt_FddTdd", "cancelOptimization reason=" + str);
        clearPendingRunnables();
        FreqOptUtils.setCoverageState(i, 2, "FreqOpt_FddTdd_Cancel_" + str, this.mHandler);
        this.mLastCoverageSlotId = -1;
        if (this.mHdrActive) {
            this.mHdrActive = false;
            FreqOptUtils.setCellListInfo(new ArrayList(), "FreqOpt_FddTdd_Cancel_HDR", this.mHandler);
        }
    }

    public void cancelPendingActions() {
        clearPendingRunnables();
        int i = this.mLastCoverageSlotId;
        if (i >= 0) {
            FreqOptUtils.setCoverageState(i, 2, "FreqOpt_FddTdd_DdsChanged_Out", this.mHandler);
            this.mLastCoverageSlotId = -1;
        }
        if (this.mHdrActive) {
            this.mHdrActive = false;
            FreqOptUtils.setCellListInfo(new ArrayList(), "FreqOpt_FddTdd_DdsChanged_HDR", this.mHandler);
        }
        LogUtils.d("Freq_Opt_FddTdd", "cancelPendingActions: DDS changed cleanup");
    }

    private void clearPendingRunnables() {
        Handler handler = this.mHandler;
        if (handler == null) {
            return;
        }
        Runnable runnable = this.mConnectedHdrRunnable;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            this.mConnectedHdrRunnable = null;
        }
        Runnable runnable2 = this.mIdleOutRunnable;
        if (runnable2 != null) {
            this.mHandler.removeCallbacks(runnable2);
            this.mIdleOutRunnable = null;
        }
    }

    private List buildCellListInfo(String str, int i, int i2, int i3) {
        return FreqOptCellListBuilder.buildCellListInfo(str, i, i2, i3, 1, 1, 30);
    }

    private void reportFddTddEvent(String str, String str2, String str3, int i) {
        FreqOptReporter.reportStateEvent(str, "fdd_tdd", str2, str3, i, null);
    }

    public void setDebugSkipSourceCellCheck(boolean z) {
        this.mDebugSkipSourceCellCheck = z;
    }
}
