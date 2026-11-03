package com.android.internal.telephony.util;

import android.util.Log;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DnsSvcbPacket extends DnsPacket {
    private static final String TAG = "DnsSvcbPacket";
    public static final int TYPE_SVCB = 64;

    private DnsSvcbPacket(byte[] bArr) {
        super(bArr);
        int recordCount = this.mHeader.getRecordCount(0);
        if (recordCount != 1) {
            DnsSvcbPacket$$ExternalSyntheticBUOutline0.m("Unexpected question count ", recordCount);
            throw null;
        }
        int i = this.mRecords[0].get(0).nsType;
        if (i == 64) {
            return;
        }
        DnsSvcbPacket$$ExternalSyntheticBUOutline0.m("Unexpected query type ", i);
        throw null;
    }

    public boolean isResponse() {
        return this.mHeader.isResponse();
    }

    public boolean isSupported(String str) {
        return findSvcbRecord(str) != null;
    }

    public String getTargetName(String str) {
        DnsSvcbRecord dnsSvcbRecordFindSvcbRecord = findSvcbRecord(str);
        if (dnsSvcbRecordFindSvcbRecord != null) {
            return dnsSvcbRecordFindSvcbRecord.getTargetName();
        }
        return null;
    }

    public int getPort(String str) {
        DnsSvcbRecord dnsSvcbRecordFindSvcbRecord = findSvcbRecord(str);
        if (dnsSvcbRecordFindSvcbRecord != null) {
            return dnsSvcbRecordFindSvcbRecord.getPort();
        }
        return -1;
    }

    public List<InetAddress> getAddresses(String str) {
        DnsSvcbRecord dnsSvcbRecordFindSvcbRecord = findSvcbRecord(str);
        if (dnsSvcbRecordFindSvcbRecord == null) {
            return Collections.EMPTY_LIST;
        }
        List<InetAddress> addressesFromAdditionalSection = getAddressesFromAdditionalSection();
        return addressesFromAdditionalSection.size() > 0 ? addressesFromAdditionalSection : dnsSvcbRecordFindSvcbRecord.getAddresses();
    }

    public String getDohPath(String str) {
        DnsSvcbRecord dnsSvcbRecordFindSvcbRecord = findSvcbRecord(str);
        if (dnsSvcbRecordFindSvcbRecord != null) {
            return dnsSvcbRecordFindSvcbRecord.getDohPath();
        }
        return null;
    }

    private DnsSvcbRecord findSvcbRecord(String str) {
        for (DnsPacket.DnsRecord dnsRecord : this.mRecords[1]) {
            if (dnsRecord instanceof DnsSvcbRecord) {
                DnsSvcbRecord dnsSvcbRecord = (DnsSvcbRecord) dnsRecord;
                if (dnsSvcbRecord.getAlpns().contains(str)) {
                    return dnsSvcbRecord;
                }
            }
        }
        return null;
    }

    private List getAddressesFromAdditionalSection() {
        ArrayList arrayList = new ArrayList();
        if (this.mHeader.getRecordCount(3) != 0) {
            for (DnsPacket.DnsRecord dnsRecord : this.mRecords[3]) {
                int i = dnsRecord.nsType;
                if (i != 1 && i != 28) {
                    Log.d(TAG, "Found type other than A/AAAA in Additional section: " + dnsRecord.nsType);
                } else {
                    try {
                        arrayList.add(InetAddress.getByAddress(dnsRecord.getRR()));
                    } catch (UnknownHostException unused) {
                        Log.w(TAG, "Failed to parse address");
                    }
                }
            }
        }
        return arrayList;
    }

    public static DnsSvcbPacket fromResponse(byte[] bArr) throws DnsPacket.ParseException {
        DnsSvcbPacket dnsSvcbPacket = new DnsSvcbPacket(bArr);
        if (dnsSvcbPacket.isResponse()) {
            return dnsSvcbPacket;
        }
        throw new DnsPacket.ParseException("Not an answer packet");
    }
}
