package com.android.internal.telephony.ntndata;

/* JADX INFO: loaded from: classes.dex */
public class TestDataCallResult {
    public int active;
    public String addresses;
    public int cid;
    public String dnses;
    public String gateways;
    public String ifname;
    public int mtu;
    public String pcscf;
    public int status;
    public int suggestedRetryTime;
    public String type;

    public TestDataCallResult(int i, int i2, int i3, int i4, String str, String str2, String str3, String str4, String str5, String str6, int i5) {
        this.status = i;
        this.suggestedRetryTime = i2;
        this.cid = i3;
        this.active = i4;
        this.type = str;
        this.ifname = str2;
        this.addresses = str3;
        this.dnses = str4;
        this.gateways = str5;
        this.pcscf = str6;
        this.mtu = i5;
    }
}
