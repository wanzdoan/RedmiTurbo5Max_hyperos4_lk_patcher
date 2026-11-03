package com.android.internal.telephony.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DnsHttpsPacket extends DnsPacket {
    public static final int TYPE_HTTPS = 65;
    private final List mHttpsRecords;

    public DnsHttpsPacket(byte[] bArr) throws DnsPacket.ParseException {
        super(bArr);
        this.mHttpsRecords = new ArrayList();
        int recordCount = this.mHeader.getRecordCount(0);
        if (recordCount != 1) {
            DnsSvcbPacket$$ExternalSyntheticBUOutline0.m("Unexpected question count: ", recordCount);
            throw null;
        }
        int i = getRecords(0).get(0).nsType;
        if (i != 65) {
            DnsSvcbPacket$$ExternalSyntheticBUOutline0.m("Unexpected query type: ", i);
            throw null;
        }
        for (DnsPacket.DnsRecord dnsRecord : getRecords(1)) {
            if (dnsRecord instanceof DnsHttpsRecord) {
                this.mHttpsRecords.add((DnsHttpsRecord) dnsRecord);
            }
        }
    }

    public List<DnsHttpsRecord> getAllRecords() {
        return Collections.unmodifiableList(this.mHttpsRecords);
    }
}
