package com.android.phone;

import android.text.TextUtils;
import android.util.Log;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SmartNetworkRecoveryPolicyController {
    private RecoveryPolicyGroup mCurrentPolicyGroup;
    private List mCurrentRecoveryPolicyGroups;
    private RecoveryPolicyGroup mPrePolicyGroup;

    public SmartNetworkRecoveryPolicyController() {
        init();
    }

    public void init() {
        this.mCurrentRecoveryPolicyGroups = SmartNetworkRecoveryPolicyParser.getRecoveryPolicyCroup();
        log("mCurrentRecoveryPolicyGroups:" + this.mCurrentRecoveryPolicyGroups);
    }

    public synchronized void reset(boolean z) {
        RecoveryPolicyGroup recoveryPolicyGroup;
        try {
            log("reset isAll:" + z);
            RecoveryPolicyGroup recoveryPolicyGroup2 = this.mPrePolicyGroup;
            if (recoveryPolicyGroup2 != null) {
                for (RecoveryPolicy recoveryPolicy : recoveryPolicyGroup2.getRecoveryPolicyList()) {
                    recoveryPolicy.setRetryTimes(recoveryPolicy.getRetryTimes() + recoveryPolicy.getCount());
                    recoveryPolicy.setCount(0);
                }
                this.mPrePolicyGroup = null;
            }
            if (z && (recoveryPolicyGroup = this.mCurrentPolicyGroup) != null) {
                for (RecoveryPolicy recoveryPolicy2 : recoveryPolicyGroup.getRecoveryPolicyList()) {
                    recoveryPolicy2.setRetryTimes(recoveryPolicy2.getRetryTimes() + recoveryPolicy2.getCount());
                    recoveryPolicy2.setCount(0);
                }
                this.mCurrentPolicyGroup = null;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private static int sumString(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        int i = 0;
        try {
            String[] strArrSplit = str.split(",");
            int length = strArrSplit.length;
            int i2 = 0;
            while (i < length) {
                try {
                    i2 += Integer.parseInt(strArrSplit[i].trim());
                    i++;
                } catch (Exception e) {
                    e = e;
                    i = i2;
                    e.printStackTrace();
                    return i;
                }
            }
            return i2;
        } catch (Exception e2) {
            e = e2;
        }
    }

    public synchronized int getNetworkRecoveryPolicyByCauseCode(int i, int i2, int i3, int i4) {
        int recoveryPolicy;
        try {
            RecoveryPolicyGroup networkPolicyGroup = getNetworkPolicyGroup(i, i2, i3, i4);
            this.mCurrentPolicyGroup = networkPolicyGroup;
            RecoveryPolicyGroup recoveryPolicyGroup = this.mPrePolicyGroup;
            if (recoveryPolicyGroup != null && !recoveryPolicyGroup.equals(networkPolicyGroup)) {
                log("network policy group has been changed dut to different network cause...");
                reset(false);
            }
            RecoveryPolicyGroup recoveryPolicyGroup2 = this.mCurrentPolicyGroup;
            recoveryPolicy = -1;
            if (recoveryPolicyGroup2 != null) {
                for (RecoveryPolicy recoveryPolicy2 : recoveryPolicyGroup2.getRecoveryPolicyList()) {
                    int iSumString = sumString(recoveryPolicy2.getApCauseSubset());
                    int iSumString2 = sumString(recoveryPolicy2.getBpCauseSubset());
                    if (recoveryPolicy2.isSwitchOn() && recoveryPolicy2.getRetryTimes() > 0 && ((iSumString & i) == i || iSumString == -1)) {
                        if ((iSumString2 & i3) == i3 || iSumString2 == -1) {
                            recoveryPolicy2.setRetryTimes(recoveryPolicy2.getRetryTimes() - 1);
                            recoveryPolicy2.setCount(recoveryPolicy2.getCount() + 1);
                            recoveryPolicy = recoveryPolicy2.getRecoveryPolicy();
                            break;
                        }
                    }
                }
                RecoveryPolicyGroup recoveryPolicyGroup3 = this.mPrePolicyGroup;
                RecoveryPolicyGroup recoveryPolicyGroup4 = this.mCurrentPolicyGroup;
                if (recoveryPolicyGroup3 != recoveryPolicyGroup4) {
                    this.mPrePolicyGroup = recoveryPolicyGroup4;
                }
            }
            log("getNetworkRecoveryPolicyByCauseCode policy:" + recoveryPolicy + " aPCause:" + i + " aPStatusLevel:" + i2 + " bPCause:" + i3 + " bPStatusLevel:" + i4);
        } catch (Throwable th) {
            throw th;
        }
        return recoveryPolicy;
    }

    private RecoveryPolicyGroup getNetworkPolicyGroup(int i, int i2, int i3, int i4) {
        for (RecoveryPolicyGroup recoveryPolicyGroup : this.mCurrentRecoveryPolicyGroups) {
            int iSumString = sumString(recoveryPolicyGroup.getApCauseSet());
            int iSumString2 = sumString(recoveryPolicyGroup.getBpCauseSet());
            int apStatusLevel = recoveryPolicyGroup.getApStatusLevel();
            int bpStatusLevel = recoveryPolicyGroup.getBpStatusLevel();
            if (recoveryPolicyGroup.isSwitchOn() && (apStatusLevel == i2 || apStatusLevel == -1)) {
                if (bpStatusLevel == i4 || bpStatusLevel == -1) {
                    if ((iSumString & i) == i || iSumString == -1) {
                        if ((iSumString2 & i3) == i3 || iSumString2 == -1) {
                            return recoveryPolicyGroup;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void log(String str) {
        Log.d("SmartNetworkRecoveryPolicyController", str);
    }
}
