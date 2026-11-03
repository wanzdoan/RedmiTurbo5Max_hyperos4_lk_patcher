package com.android.phone;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* JADX INFO: compiled from: SmartNetworkRecoveryPolicyParser.java */
/* JADX INFO: loaded from: classes.dex */
class RecoveryPolicyGroup {
    private String aPCauseSet = "";
    private String bPCauseSet = "";
    private int aPStatusLevel = -1;
    private int bPStatusLevel = -1;
    private int isSwitchOn = 0;
    private List recoveryPolicies = new ArrayList();

    public boolean isSwitchOn() {
        return this.isSwitchOn == 1;
    }

    public void setIsSwitchOn(int i) {
        this.isSwitchOn = i;
    }

    public void addRecoveryPolicy(RecoveryPolicy recoveryPolicy) {
        this.recoveryPolicies.add(recoveryPolicy);
    }

    public List getRecoveryPolicyList() {
        return this.recoveryPolicies;
    }

    public String getApCauseSet() {
        return this.aPCauseSet;
    }

    public void setApCauseSet(String str) {
        this.aPCauseSet = str;
    }

    public String getBpCauseSet() {
        return this.bPCauseSet;
    }

    public void setBpCauseSet(String str) {
        this.bPCauseSet = str;
    }

    public int getApStatusLevel() {
        return this.aPStatusLevel;
    }

    public void setApStatusLevel(int i) {
        this.aPStatusLevel = i;
    }

    public int getBpStatusLevel() {
        return this.bPStatusLevel;
    }

    public void setBpStatusLevel(int i) {
        this.bPStatusLevel = i;
    }

    public String toString() {
        return "RecoveryPolicyGroup{isSwitchOn=" + this.isSwitchOn + ", APCauseSet='" + this.aPCauseSet + "', BPCauseSet='" + this.bPCauseSet + "', bPstatusLevel=" + this.bPStatusLevel + ", aPstatusLevel=" + this.aPStatusLevel + "recoveryPolicies=" + this.recoveryPolicies + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            RecoveryPolicyGroup recoveryPolicyGroup = (RecoveryPolicyGroup) obj;
            if (this.isSwitchOn == recoveryPolicyGroup.isSwitchOn && this.aPStatusLevel == recoveryPolicyGroup.aPStatusLevel && this.bPStatusLevel == recoveryPolicyGroup.bPStatusLevel && this.aPCauseSet.equals(recoveryPolicyGroup.aPCauseSet) && this.bPCauseSet.equals(recoveryPolicyGroup.bPCauseSet)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.isSwitchOn), this.aPCauseSet, this.bPCauseSet, Integer.valueOf(this.bPStatusLevel), Integer.valueOf(this.aPStatusLevel));
    }
}
