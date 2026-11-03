package com.android.phone.network;

import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class ModemFiveGAScheduleInfo {
    public byte nrCaNum;
    public int[] arfcn = new int[3];
    public int[] pci = new int[3];
    public int[] rsrp = new int[3];
    public int[] snr = new int[3];
    public int[] pdcpDecode = new int[3];
    public int[] crcFail = new int[3];
    public long[] tbBytes = new long[3];

    public String toString() {
        return "ModemFiveGAScheduleInfo:nrCaNum=" + ((int) this.nrCaNum) + ", arfcn=" + Arrays.toString(this.arfcn) + ", pci=" + Arrays.toString(this.pci) + ", rsrp=" + Arrays.toString(this.rsrp) + ", snr=" + Arrays.toString(this.snr) + ", pdcpDecode=" + Arrays.toString(this.pdcpDecode) + ", crcFail=" + Arrays.toString(this.crcFail) + ", tbBytes=" + Arrays.toString(this.tbBytes);
    }
}
