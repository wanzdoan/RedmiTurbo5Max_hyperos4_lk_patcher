package com.android.internal.telephony.ntnphone;

/* JADX INFO: loaded from: classes.dex */
public class NtnTtlogInfo implements Comparable<NtnTtlogInfo> {
    public int foffset;
    public int rError;
    public int rTotal;
    public int sat_id;
    public int tempeture;
    public int tx_valid;
    public int txp_real;
    public int txp_setted;

    @Override // java.lang.Comparable
    public int compareTo(NtnTtlogInfo ntnTtlogInfo) {
        return 0;
    }

    NtnTtlogInfo() {
    }

    public String toString() {
        return "sat_id = " + this.sat_id + ", rTotal = " + this.rTotal + ", rError = " + this.rError + ", foffset = " + this.foffset + ", tx_valid = " + this.tx_valid + ", txp_setted = " + this.txp_setted + ", txp_real = " + this.txp_real + "tempeture = " + this.tempeture;
    }
}
