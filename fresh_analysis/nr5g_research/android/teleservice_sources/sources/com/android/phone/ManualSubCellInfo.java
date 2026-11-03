package com.android.phone;

import java.util.Objects;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class ManualSubCellInfo {
    public String gci1;
    public String gci2;
    public int id = 0;
    public int freq1 = 0;
    public int pci1 = 0;
    public int rsrp1 = 0;
    public int sinr1 = 0;
    public int rat1 = 0;
    public int service1 = 0;
    public int action1 = 0;
    public int rsrp2 = 0;
    public int sinr2 = 0;
    public int freq2 = 0;
    public int rat2 = 0;
    public int pci2 = 0;
    public int service2 = 0;
    public int action2 = 0;
    public int type = 0;
    public int time_extra = 0;
    public long update_time = 0;

    public void setId(int i) {
        this.id = i;
    }

    public void setGci1(String str) {
        this.gci1 = str;
    }

    public void setFreq1(int i) {
        this.freq1 = i;
    }

    public void setPci1(int i) {
        this.pci1 = i;
    }

    public void setRsrp1(int i) {
        this.rsrp1 = i;
    }

    public void setSinr1(int i) {
        this.sinr1 = i;
    }

    public void setRat1(int i) {
        this.rat1 = i;
    }

    public void setService1(int i) {
        this.service1 = i;
    }

    public void setAction1(int i) {
        this.action1 = i;
    }

    public void setGci2(String str) {
        this.gci2 = str;
    }

    public void setRsrp2(int i) {
        this.rsrp2 = i;
    }

    public void setSinr2(int i) {
        this.sinr2 = i;
    }

    public int getFreq2() {
        return this.freq2;
    }

    public void setFreq2(int i) {
        this.freq2 = i;
    }

    public int getRat2() {
        return this.rat2;
    }

    public void setRat2(int i) {
        this.rat2 = i;
    }

    public int getPci2() {
        return this.pci2;
    }

    public void setPci2(int i) {
        this.pci2 = i;
    }

    public int getService2() {
        return this.service2;
    }

    public void setService2(int i) {
        this.service2 = i;
    }

    public void setAction2(int i) {
        this.action2 = i;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public void setTime_extra(int i) {
        this.time_extra = i;
    }

    public void setUpdate_time(long j) {
        this.update_time = j;
    }

    public boolean equals(Object obj) {
        String str;
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            ManualSubCellInfo manualSubCellInfo = (ManualSubCellInfo) obj;
            String str2 = this.gci1;
            if (str2 != null && this.gci2 != null && (str = manualSubCellInfo.gci1) != null && manualSubCellInfo.gci2 != null && str2.equals(str) && this.gci2.equals(manualSubCellInfo.gci2)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        String str;
        String str2 = this.gci1;
        if (str2 == null || (str = this.gci2) == null) {
            return 0;
        }
        return Objects.hash(str2, str);
    }

    public String toString() {
        return "ManualSubCellInfo{id=" + this.id + "gci1='" + TelephonyUtils.pii(this.gci1) + "', freq1=" + this.freq1 + ", pci1=" + this.pci1 + ", rsrp1=" + this.rsrp1 + ", sinr1=" + this.sinr1 + ", rat1=" + this.rat1 + ", service1=" + this.service1 + ", action1=" + this.action1 + ", gci2='" + TelephonyUtils.pii(this.gci2) + "', rsrp2=" + this.rsrp2 + ", sinr2=" + this.sinr2 + ", freq2=" + this.freq2 + ", rat2=" + this.rat2 + ", pci2=" + this.pci2 + ", service2=" + this.service2 + ", action2=" + this.action2 + ", type=" + this.type + ", time_extra=" + this.time_extra + ", update_time=" + this.update_time + '}';
    }
}
