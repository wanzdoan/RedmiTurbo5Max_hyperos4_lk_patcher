package com.android.phone.performance;

/* JADX INFO: loaded from: classes.dex */
public class CellInfo {
    public int action;
    public int actionValue;
    public int bitmask;
    public int cellType;
    public String globalCellId;
    public int rsrp;
    public int sceneDataType;
    public long cid = 0;
    public int freq = 0;
    public int pci = 0;
    public int rat = 0;
    public byte scs = 90;
    public int isBadCell = 0;
    public int currentCellCount = 0;
    public int rrcExceptionCount = 0;
    public int highBlerCount = 0;
    public int lowUlGrantCount = 0;
    public int rlfCount = 0;
    public int badRsrpDuration = 0;
    public int badRsrqDuration = 0;
    public int duration = 0;
    public int dataType = 0;
    public int lowDlGrantCount = 0;
    public int isWorkDay = 0;
    public int isPeak = 0;
    public long updateTimeMills = 0;
    public float wechatDataStallCount = 0.0f;
    public float wechatDataStallDur = 0.0f;
    public float gameDataStallCount = 0.0f;
    public float gameDataStallDur = 0.0f;
    public float gameDataStallLagDur = 0.0f;

    public long getCid() {
        return this.cid;
    }

    public void setCid(long j) {
        this.cid = j;
    }

    public byte getScs() {
        return this.scs;
    }

    public void setScs(byte b) {
        this.scs = b;
    }

    public int getDataType() {
        return this.dataType;
    }

    public void setDataType(int i) {
        this.dataType = i;
    }

    public int getLowDlGrantCount() {
        return this.lowDlGrantCount;
    }

    public void setLowDlGrantCount(int i) {
        this.lowDlGrantCount = i;
    }

    public int getFreq() {
        return this.freq;
    }

    public void setFreq(int i) {
        this.freq = i;
    }

    public int getPci() {
        return this.pci;
    }

    public void setPci(int i) {
        this.pci = i;
    }

    public int getRat() {
        return this.rat;
    }

    public void setRat(int i) {
        this.rat = i;
    }

    public int getIsBadCell() {
        return this.isBadCell;
    }

    public void setIsBadCell(int i) {
        this.isBadCell = i;
    }

    public int getCurrentCellCount() {
        return this.currentCellCount;
    }

    public int getRrcExceptionCount() {
        return this.rrcExceptionCount;
    }

    public void setRrcExceptionCount(int i) {
        this.rrcExceptionCount = i;
    }

    public int getHighBlerCount() {
        return this.highBlerCount;
    }

    public void setHighBlerCount(int i) {
        this.highBlerCount = i;
    }

    public int getLowUlGrantCount() {
        return this.lowUlGrantCount;
    }

    public void setLowUlGrantCount(int i) {
        this.lowUlGrantCount = i;
    }

    public int getRlfCount() {
        return this.rlfCount;
    }

    public void setRlfCount(int i) {
        this.rlfCount = i;
    }

    public int getBadRsrpDuration() {
        return this.badRsrpDuration;
    }

    public void setBadRsrpDuration(int i) {
        this.badRsrpDuration = i;
    }

    public int getBadRsrqDuration() {
        return this.badRsrqDuration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public void setGlobalCellId(String str) {
        this.globalCellId = str;
    }

    public int getBitmask() {
        return this.bitmask;
    }

    public void setRsrp(int i) {
        this.rsrp = i;
    }

    public void setAction(int i) {
        this.action = i;
    }

    public void setActionValue(int i) {
        this.actionValue = i;
    }

    public void setBitmask(int i) {
        this.bitmask = i;
    }

    public void setSceneDataType(int i) {
        this.sceneDataType = i;
    }

    public void setCellType(int i) {
        this.cellType = i;
    }

    public String toString() {
        return " freq is " + this.freq + " pci is " + this.pci + " rat is " + this.rat + " isBadCell is " + this.isBadCell + " currentCellCount is " + this.currentCellCount + " rrcExceptionCount is " + this.rrcExceptionCount + " highBlerCount is " + this.highBlerCount + " lowUlGrantCount is " + this.lowUlGrantCount + " rlfCount is " + this.rlfCount + " badRsrpDuration is " + this.badRsrpDuration + " badRsrqDuration is " + this.badRsrqDuration + " duration is " + this.duration + "dataType is " + this.dataType + " lowDlGrantCount is " + this.lowDlGrantCount + " cid is " + this.cid + "scs is " + ((int) this.scs);
    }
}
