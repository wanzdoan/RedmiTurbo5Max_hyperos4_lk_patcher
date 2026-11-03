package com.android.phone;

/* JADX INFO: loaded from: classes.dex */
public class FiveGFakeStation {
    private int dlarfcn;
    private int mcc;
    private int mnc;
    private int pci;

    public FiveGFakeStation(int i, int i2, int i3, int i4) {
        this.mcc = i;
        this.mnc = i2;
        this.dlarfcn = i3;
        this.pci = i4;
    }

    public int getMcc() {
        return this.mcc;
    }

    public int getMnc() {
        return this.mnc;
    }

    public int getDlarfcn() {
        return this.dlarfcn;
    }

    public int getPci() {
        return this.pci;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return isContentEqual((FiveGFakeStation) obj);
    }

    private boolean isContentEqual(FiveGFakeStation fiveGFakeStation) {
        return this.mcc == fiveGFakeStation.mcc && this.mnc == fiveGFakeStation.mnc && this.dlarfcn == fiveGFakeStation.dlarfcn && this.pci == fiveGFakeStation.pci;
    }

    public String toString() {
        return "FiveGFakeStation[mcc=" + this.mcc + ", mnc=" + this.mnc + ", dlarfcn=" + this.dlarfcn + ", pci=" + this.pci + "]";
    }
}
