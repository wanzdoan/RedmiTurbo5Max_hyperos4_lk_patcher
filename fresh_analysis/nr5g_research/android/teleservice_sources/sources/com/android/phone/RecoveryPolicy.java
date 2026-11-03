package com.android.phone;

/* JADX INFO: compiled from: SmartNetworkRecoveryPolicyParser.java */
/* JADX INFO: loaded from: classes.dex */
class RecoveryPolicy {
    private int isSwitchOn = 0;
    private int policy = -1;
    private int retryTimes = -1;
    private int count = 0;
    private String aPCauseSubset = "";
    private String bPCauseSubset = "";

    RecoveryPolicy() {
    }

    public boolean isSwitchOn() {
        return this.isSwitchOn == 1;
    }

    public void setIsSwitchOn(int i) {
        this.isSwitchOn = i;
    }

    public int getRecoveryPolicy() {
        return this.policy;
    }

    public void setRecoveryPolicy(int i) {
        this.policy = i;
    }

    public int getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(int i) {
        this.retryTimes = i;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public String getApCauseSubset() {
        return this.aPCauseSubset;
    }

    public void setApCauseSubset(String str) {
        this.aPCauseSubset = str;
    }

    public String getBpCauseSubset() {
        return this.bPCauseSubset;
    }

    public void setBpCauseSubset(String str) {
        this.bPCauseSubset = str;
    }

    public String toString() {
        return "RecoveryPolicy{isSwitchOn=" + this.isSwitchOn + ", policy=" + this.policy + ", retryTimes=" + this.retryTimes + ", count=" + this.count + ", aPCauseSubset=" + this.aPCauseSubset + ", bPCauseSubset=" + this.bPCauseSubset + '}';
    }
}
