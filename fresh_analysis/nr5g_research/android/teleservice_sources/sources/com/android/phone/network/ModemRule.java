package com.android.phone.network;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class ModemRule {
    private boolean isSwitchOn = false;
    private int event = -1;
    private int timer = 5;
    private int modemRuleCause = -1;
    private int ulGrantRatio = -1;
    private int dlGrantRatio = -1;
    private int ulSpeed = -1;
    private int dlSpeed = -1;
    private int ulBler = -1;
    private int dlBler = -1;
    private int[] weakRsrp = {Integer.MIN_VALUE, Integer.MIN_VALUE};
    private int[] weakSnr = {Integer.MIN_VALUE, Integer.MIN_VALUE};

    public boolean isSwitchOn() {
        return this.isSwitchOn;
    }

    public int getEvent() {
        return this.event;
    }

    public int getTimer() {
        return this.timer;
    }

    public int getUlGrantRatio() {
        return this.ulGrantRatio;
    }

    public int getDlGrantRatio() {
        return this.dlGrantRatio;
    }

    public int getUlSpeed() {
        return this.ulSpeed;
    }

    public int getDlSpeed() {
        return this.dlSpeed;
    }

    public int getUlBler() {
        return this.ulBler;
    }

    public int getDlBler() {
        return this.dlBler;
    }

    public int[] getWeakRsrp() {
        return this.weakRsrp;
    }

    public int[] getWeakSnr() {
        return this.weakSnr;
    }

    public void setSwitchOn(boolean z) {
        this.isSwitchOn = z;
    }

    public void setEvent(int i) {
        this.event = i;
    }

    public void setTimer(int i) {
        this.timer = i;
    }

    public void setModemRuleCause(int i) {
        this.modemRuleCause = i;
    }

    public void setUlGrantRatio(int i) {
        this.ulGrantRatio = i;
    }

    public void setDlGrantRatio(int i) {
        this.dlGrantRatio = i;
    }

    public void setUlSpeed(int i) {
        this.ulSpeed = i;
    }

    public void setDlSpeed(int i) {
        this.dlSpeed = i;
    }

    public void setUlBler(int i) {
        this.ulBler = i;
    }

    public void setDlBler(int i) {
        this.dlBler = i;
    }

    public void setWeakRsrp(int[] iArr) {
        this.weakRsrp = iArr;
    }

    public void setWeakSnr(int[] iArr) {
        this.weakSnr = iArr;
    }

    public String toString() {
        return "ModemRule{isSwitchOn=" + this.isSwitchOn + ", event=" + this.event + ", timer=" + this.timer + ", modemRuleCause=" + this.modemRuleCause + ", ulGrantRatio=" + this.ulGrantRatio + ", dlGrantRatio=" + this.dlGrantRatio + ", ulSpeed=" + this.ulSpeed + ", dlSpeed=" + this.dlSpeed + ", ulBler=" + this.ulBler + ", dlBler=" + this.dlBler + ", weakRsrp=" + Arrays.toString(this.weakRsrp) + ", weakSnr=" + Arrays.toString(this.weakSnr) + '}';
    }
}
