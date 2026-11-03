package com.android.phone.freqopt.engine;

import android.os.Handler;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.freqopt.core.FreqOptController;
import com.android.phone.freqopt.util.FreqOptReporter;
import com.android.phone.freqopt.util.LogUtils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.Objects;
import java.util.concurrent.Executor;
import xiaomi.hypercomm.HyperCommManager;
import xiaomi.hypercomm.SceneAwareCallback;

/* JADX INFO: loaded from: classes.dex */
public class NrLtePostExecVerifier {
    private final Handler mHandler;
    private Runnable mRatCheckRunnable;
    private SceneAwareCallback mStutterCallback;
    private volatile boolean mStutterMonitoring;
    private volatile String mStutterSourceCellId;
    private Runnable mStutterTimeoutRunnable;
    private long mRatCheckDelayMs = 10000;
    private int mRatCheckMaxRetries = 3;
    private long mStutterCheckWindowMs = RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
    private int mQoeStutterThreshold = 80;

    public NrLtePostExecVerifier(Handler handler) {
        this.mHandler = handler;
    }

    public void updateConfig(long j, int i, long j2, int i2) {
        this.mRatCheckDelayMs = j;
        this.mRatCheckMaxRetries = i;
        this.mStutterCheckWindowMs = j2;
        this.mQoeStutterThreshold = i2;
    }

    public void startVerification(int i, String str, int i2, int i3) {
        startRatCheck(i, str, i2, i3);
    }

    public void cancelAll() {
        Runnable runnable;
        Handler handler = this.mHandler;
        if (handler != null && (runnable = this.mRatCheckRunnable) != null) {
            handler.removeCallbacks(runnable);
            this.mRatCheckRunnable = null;
        }
        stopStutterCheck();
        LogUtils.d("Freq_Opt_NrLteVerifier", "cancelAll");
    }

    private void startRatCheck(int i, String str, int i2, int i3) {
        Runnable runnable;
        Handler handler = this.mHandler;
        if (handler != null && (runnable = this.mRatCheckRunnable) != null) {
            handler.removeCallbacks(runnable);
            this.mRatCheckRunnable = null;
        }
        if (this.mHandler == null) {
            LogUtils.d("Freq_Opt_NrLteVerifier", "startRatCheck handler is null, skip");
            return;
        }
        LogUtils.d("Freq_Opt_NrLteVerifier", "startRatCheck slotId=" + i + ", source=" + str);
        scheduleRatCheck(i, str, i2, i3, 0);
    }

    private void scheduleRatCheck(final int i, final String str, final int i2, final int i3, final int i4) {
        Runnable runnable = new Runnable() { // from class: com.android.phone.freqopt.engine.NrLtePostExecVerifier$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRatCheck$0(i, i4, str, i2, i3);
            }
        };
        this.mRatCheckRunnable = runnable;
        this.mHandler.postDelayed(runnable, this.mRatCheckDelayMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRatCheck$0(int i, int i2, String str, int i3, int i4) {
        CellIdentity currentCellIdentity;
        try {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                currentCellIdentity = phone.getCurrentCellIdentity();
            } else {
                LogUtils.w("Freq_Opt_NrLteVerifier", "ratCheck: phone is null for slotId=" + i);
                currentCellIdentity = null;
            }
        } catch (Exception e) {
            LogUtils.w("Freq_Opt_NrLteVerifier", "getCurrentCell failed", e);
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            LogUtils.i("Freq_Opt_NrLteVerifier", "ratCheck PASS: fallback to LTE after " + (i2 + 1) + " check(s)");
            FreqOptReporter.reportEvent("nr_lte_rat_check_fallback_ok", "nr_lte", str, null, 0, "retries=" + i2 + ",band=" + i3 + ",freq=" + i4);
            this.mRatCheckRunnable = null;
            startStutterCheck(str);
            return;
        }
        int i5 = i2 + 1;
        if (i5 >= this.mRatCheckMaxRetries) {
            LogUtils.i("Freq_Opt_NrLteVerifier", "ratCheck TIMEOUT: still not on LTE after " + this.mRatCheckMaxRetries + " checks");
            this.mRatCheckRunnable = null;
            FreqOptController freqOptController = FreqOptController.getInstance();
            if (freqOptController != null && str != null) {
                freqOptController.getDbManager().deleteBySourceCell("nr_lte", str);
                LogUtils.i("Freq_Opt_NrLteVerifier", "ratCheck TIMEOUT: removed record for source=".concat(str));
            } else {
                LogUtils.w("Freq_Opt_NrLteVerifier", "ratCheck TIMEOUT: cannot remove record, controller=" + freqOptController + ", sourceCellId=" + str);
            }
            FreqOptReporter.reportEvent("nr_lte_rat_check_timeout", "nr_lte", str, null, 0, "maxRetries=" + this.mRatCheckMaxRetries);
            return;
        }
        LogUtils.d("Freq_Opt_NrLteVerifier", "ratCheck retry=" + i5 + ", current cell is not LTE, scheduling next check");
        scheduleRatCheck(i, str, i3, i4, i5);
    }

    private void startStutterCheck(String str) {
        stopStutterCheck();
        if (this.mHandler == null) {
            LogUtils.d("Freq_Opt_NrLteVerifier", "startStutterCheck handler is null, skip");
            return;
        }
        LogUtils.d("Freq_Opt_NrLteVerifier", "startStutterCheck source=" + str);
        this.mStutterSourceCellId = str;
        this.mStutterMonitoring = true;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        this.mStutterCallback = new StutterSceneAwareCallback(new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), this);
        try {
            HyperCommManager.getDefault().registerSceneAwareCallback(this.mStutterCallback);
            Runnable runnable = new Runnable() { // from class: com.android.phone.freqopt.engine.NrLtePostExecVerifier$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startStutterCheck$1();
                }
            };
            this.mStutterTimeoutRunnable = runnable;
            this.mHandler.postDelayed(runnable, this.mStutterCheckWindowMs);
        } catch (Exception e) {
            LogUtils.w("Freq_Opt_NrLteVerifier", "startStutterCheck registerSceneAwareCallback failed", e);
            this.mStutterMonitoring = false;
            this.mStutterCallback = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startStutterCheck$1() {
        LogUtils.i("Freq_Opt_NrLteVerifier", "stutterCheck PASS: no stutter detected within " + this.mStutterCheckWindowMs + "ms");
        FreqOptReporter.reportEvent("nr_lte_stutter_pass", "nr_lte", this.mStutterSourceCellId, null, 0, null);
        refreshRecordValidity(this.mStutterSourceCellId);
        stopStutterCheck();
    }

    private void stopStutterCheck() {
        Runnable runnable;
        if (this.mStutterCallback != null) {
            try {
                HyperCommManager.getDefault().unregisterSceneAwareCallback(this.mStutterCallback);
            } catch (Exception e) {
                LogUtils.w("Freq_Opt_NrLteVerifier", "stopStutterCheck unregisterSceneAwareCallback failed", e);
            }
            this.mStutterCallback = null;
        }
        Handler handler = this.mHandler;
        if (handler != null && (runnable = this.mStutterTimeoutRunnable) != null) {
            handler.removeCallbacks(runnable);
            this.mStutterTimeoutRunnable = null;
        }
        this.mStutterMonitoring = false;
        this.mStutterSourceCellId = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStutterDetected(int i, int i2) {
        String str = this.mStutterSourceCellId;
        LogUtils.w("Freq_Opt_NrLteVerifier", "stutterCheck FAIL: stutter detected, source=" + str + ", score=" + i2);
        StringBuilder sb = new StringBuilder("score=");
        sb.append(i2);
        FreqOptReporter.reportEvent("nr_lte_stutter_detected", "nr_lte", str, null, 0, sb.toString());
        FreqOptController freqOptController = FreqOptController.getInstance();
        if (freqOptController != null && str != null) {
            freqOptController.getDbManager().deleteBySourceCell("nr_lte", str);
            LogUtils.d("Freq_Opt_NrLteVerifier", "stutterCheck removed record for source=".concat(str));
        } else {
            LogUtils.w("Freq_Opt_NrLteVerifier", "stutterCheck: cannot remove record, controller=" + freqOptController + ", sourceCellId=" + str);
        }
        stopStutterCheck();
    }

    private void refreshRecordValidity(String str) {
        if (str == null) {
            LogUtils.w("Freq_Opt_NrLteVerifier", "refreshRecordValidity: sourceCellId is null");
            return;
        }
        FreqOptController freqOptController = FreqOptController.getInstance();
        if (freqOptController == null || freqOptController.getDbManager() == null) {
            LogUtils.w("Freq_Opt_NrLteVerifier", "refreshRecordValidity: controller or dbManager is null");
        } else {
            freqOptController.getDbManager().refreshBySourceCell("nr_lte", str);
            LogUtils.d("Freq_Opt_NrLteVerifier", "refreshRecordValidity: refreshed record for source=".concat(str));
        }
    }

    class StutterSceneAwareCallback extends SceneAwareCallback implements SceneAwareCallback.NetworkQoeListener {
        private final NrLtePostExecVerifier mVerifier;

        StutterSceneAwareCallback(Executor executor, NrLtePostExecVerifier nrLtePostExecVerifier) {
            super(executor);
            this.mVerifier = nrLtePostExecVerifier;
        }

        public void onNetworkQoeChanged(int i, int i2, int i3, int i4, int i5) {
            LogUtils.d("Freq_Opt_NrLteVerifier", "onNetworkQoeChanged: slotId=" + i + ", pci=" + i2 + ", appType=" + i3 + ", reason=" + i4 + ", networkScore=" + i5);
            if (this.mVerifier.mStutterMonitoring && i5 < this.mVerifier.mQoeStutterThreshold) {
                this.mVerifier.onStutterDetected(i, i5);
            }
        }
    }

    public void debugStartStutterCheck(String str) {
        startStutterCheck(str);
    }

    public void debugStopStutterCheck() {
        stopStutterCheck();
    }

    public void debugRefreshRecordValidity(String str) {
        refreshRecordValidity(str);
    }

    public void debugSimulateLteStutter(int i, int i2) {
        if (!this.mStutterMonitoring) {
            LogUtils.w("Freq_Opt_NrLteVerifier", "debugSimulateLteStutter: stutter check not active");
            return;
        }
        LogUtils.d("Freq_Opt_NrLteVerifier", "debugSimulateLteStutter: slotId=" + i + ", score=" + i2 + ", threshold=" + this.mQoeStutterThreshold);
        if (i2 < this.mQoeStutterThreshold) {
            onStutterDetected(i, i2);
            return;
        }
        LogUtils.w("Freq_Opt_NrLteVerifier", "debugSimulateLteStutter: score=" + i2 + " >= threshold=" + this.mQoeStutterThreshold + ", no stutter triggered");
    }
}
