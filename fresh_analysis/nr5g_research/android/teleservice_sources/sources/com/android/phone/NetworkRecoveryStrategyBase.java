package com.android.phone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class NetworkRecoveryStrategyBase {
    protected static volatile NetworkRecoveryStrategyBase mInstance;
    protected Context mContext = null;
    protected RilHook mRilHook = null;

    public void alleviateDataStall(int i, int i2, int i3, boolean z, String str) {
    }

    public void enableCellBarringFeature(int i, boolean z, String str) {
    }

    public void enableCongestionOpt(int i, boolean z) {
    }

    public void enableHsrOpt(int i, boolean z) {
    }

    public abstract void enableNsa(boolean z, int i);

    public abstract void enableNsa(boolean z, int i, INetworkRecoveryStrategy$RecoveryResultCallback iNetworkRecoveryStrategy$RecoveryResultCallback);

    public abstract void enableSa(boolean z, int i);

    public abstract void enableSa(boolean z, int i, INetworkRecoveryStrategy$RecoveryResultCallback iNetworkRecoveryStrategy$RecoveryResultCallback);

    public void enableWeakOpt(int i, boolean z) {
    }

    public abstract void forceLte(int i);

    public abstract void forceLte2Nr(int i);

    public void getHstState(String str) {
    }

    public abstract int getNrCaCallCount();

    public abstract boolean getNrCaStatus(int i);

    public abstract void reAttach(int i);

    public abstract void restartRadio(int i);

    public void setCoverageState(int i, int i2, String str) {
    }

    public void setHstState(int i, String str) {
    }

    public abstract boolean setNrCaStatus(int i, boolean z, String str);

    public abstract void setNrDubiousCell(int i, ArrayList arrayList, int i2, String str);

    public abstract void setOutOfServiceState(int i, int i2, String str);

    public void setPreferredCell(int i, Map map, String str, String str2) {
    }

    public void setPreferredCellForSNSV4(int i, Map map, String str, int i2, Map map2, String str2) {
    }

    public void setSbpOosHighRatOptState(boolean z) {
    }

    public void setSubwayState(int i, String str) {
    }

    public static NetworkRecoveryStrategyBase getInstance() {
        return mInstance;
    }

    public synchronized void setLteDubiousCell(int i, ArrayList arrayList, int i2, String str) {
    }

    public synchronized void setLteNrCellBarringInfo(int i, List list, List list2, String str) {
    }

    public synchronized void setCellListInfo(List list, String str) {
    }

    public synchronized void setCellListInfo(int i, List list, long j) {
    }

    public synchronized void setCellListInfoV2(Map map, int i, long j, String str) {
    }

    public void videoAppsPolicyNotify(int i, int i2, int i3, int i4) {
        log("videoAppsPolicyNotify type=" + i + ", duration=" + i2 + ", length=" + i3);
        if (this.mContext == null) {
            log("videoAppsPolicyNotify: mContext is null");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("com.android.phone.action.VIDEO_APPS_POLICY_NOTIFY");
        intent.putExtra("type", i);
        intent.putExtra("duration", i2);
        intent.putExtra("length", i3);
        intent.putExtra("scene", i4);
        this.mContext.sendBroadcast(intent);
    }

    private static void log(String str) {
        Log.d("NetworkRecoveryStrategyBase", str);
    }
}
