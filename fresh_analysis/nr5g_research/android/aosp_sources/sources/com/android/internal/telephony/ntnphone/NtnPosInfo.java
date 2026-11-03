package com.android.internal.telephony.ntnphone;

/* JADX INFO: loaded from: classes.dex */
public class NtnPosInfo implements Comparable<NtnPosInfo> {
    public int beam_id;
    public String lat_beam;
    public String long_beam;

    @Override // java.lang.Comparable
    public int compareTo(NtnPosInfo ntnPosInfo) {
        return 0;
    }

    NtnPosInfo() {
    }

    public String toString() {
        return "long_beam = " + this.long_beam + "lat_beam = " + this.lat_beam + "beam_id = " + this.beam_id;
    }
}
