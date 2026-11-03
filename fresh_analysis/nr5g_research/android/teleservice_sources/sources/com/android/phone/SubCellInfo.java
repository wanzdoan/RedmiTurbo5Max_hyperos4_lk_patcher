package com.android.phone;

import java.util.Objects;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class SubCellInfo {
    public String abnormalPeriod;
    public String cityName;
    public String gci;
    public String neighGci;
    public String prevGci;
    public String projectType;
    public String reason;
    public String scene;
    public String service;
    public String subScene;
    public String tac;
    public String updateTime;
    public int id = 0;
    public int freq = 0;
    public int pci = 0;
    public int rat = 0;

    public void setId(int i) {
        this.id = i;
    }

    public void setScene(String str) {
        this.scene = str;
    }

    public void setSubScene(String str) {
        this.subScene = str;
    }

    public void setCityName(String str) {
        this.cityName = str;
    }

    public void setGci(String str) {
        this.gci = str;
    }

    public void setTac(String str) {
        this.tac = str;
    }

    public void setFreq(int i) {
        this.freq = i;
    }

    public void setPci(int i) {
        this.pci = i;
    }

    public void setRat(int i) {
        this.rat = i;
    }

    public void setPrevGci(String str) {
        this.prevGci = str;
    }

    public void setNeighGci(String str) {
        this.neighGci = str;
    }

    public void setService(String str) {
        this.service = str;
    }

    public void setReason(String str) {
        this.reason = str;
    }

    public void setAbnormalPeriod(String str) {
        this.abnormalPeriod = str;
    }

    public void setUpdateTime(String str) {
        this.updateTime = str;
    }

    public void setProjectType(String str) {
        this.projectType = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.gci, ((SubCellInfo) obj).gci);
    }

    public int hashCode() {
        return Objects.hash(this.gci);
    }

    public String toString() {
        return "SubCellInfo{id=" + this.id + ", scene='" + this.scene + "', subScene='" + this.subScene + "', cityName='" + this.cityName + "', gci='" + TelephonyUtils.pii(this.gci) + "', tac='" + TelephonyUtils.pii(this.tac) + "', freq=" + this.freq + ", pci=" + this.pci + ", rat=" + this.rat + ", prevGci='" + TelephonyUtils.pii(this.prevGci) + "', neighGci='" + TelephonyUtils.pii(this.neighGci) + "', service='" + this.service + "', reason='" + this.reason + "', abnormalPeriod='" + this.abnormalPeriod + "', updateTime='" + this.updateTime + "', projectType='" + this.projectType + "'}";
    }
}
