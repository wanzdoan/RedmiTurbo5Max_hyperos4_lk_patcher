package com.android.internal.telephony.util;

import android.util.SparseArray;
import com.android.internal.telephony.LocaleTracker$$ExternalSyntheticBUOutline0;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/* JADX INFO: loaded from: classes.dex */
public final class DnsSvcbRecord extends DnsPacket.DnsRecord {
    private final SparseArray mAllSvcParams;
    private final int mSvcPriority;
    private final String mTargetName;

    public DnsSvcbRecord(int i, ByteBuffer byteBuffer) throws IllegalStateException, DnsPacket.ParseException {
        super(i, byteBuffer);
        this.mAllSvcParams = new SparseArray();
        if (this.nsType != 64) {
            LocaleTracker$$ExternalSyntheticBUOutline0.m("incorrect nsType: ", this.nsType);
            throw null;
        }
        if (this.nsClass != 1) {
            DnsSvcbRecord$$ExternalSyntheticBUOutline0.m("incorrect nsClass: ", this.nsClass);
            throw null;
        }
        if (i == 0) {
            this.mSvcPriority = 0;
            this.mTargetName = "";
            return;
        }
        byte[] rr = getRR();
        if (rr == null) {
            throw new DnsPacket.ParseException("SVCB rdata is empty");
        }
        ByteBuffer byteBufferAsReadOnlyBuffer = ByteBuffer.wrap(rr).asReadOnlyBuffer();
        this.mSvcPriority = Short.toUnsignedInt(byteBufferAsReadOnlyBuffer.getShort());
        String name = DnsPacketUtils$DnsRecordParser.parseName(byteBufferAsReadOnlyBuffer, 0, false);
        this.mTargetName = name;
        if (name.length() > 255) {
            DnsSvcbRecord$$ExternalSyntheticBUOutline0.m("Failed to parse SVCB target name, name size is too long: ", name.length());
            throw null;
        }
        while (byteBufferAsReadOnlyBuffer.remaining() >= 4) {
            SvcParam svcParam = SvcParam.parseSvcParam(byteBufferAsReadOnlyBuffer);
            int key = svcParam.getKey();
            if (this.mAllSvcParams.get(key) != null) {
                throw new DnsPacket.ParseException("Invalid DnsSvcbRecord, key " + key + " is repeated");
            }
            this.mAllSvcParams.put(key, svcParam);
        }
        if (byteBufferAsReadOnlyBuffer.hasRemaining()) {
            throw new DnsPacket.ParseException("Invalid DnsSvcbRecord. Got " + byteBufferAsReadOnlyBuffer.remaining() + " remaining bytes after parsing");
        }
    }

    public String getTargetName() {
        return this.mTargetName;
    }

    public List<String> getAlpns() {
        SvcParam.SvcParamAlpn svcParamAlpn = (SvcParam.SvcParamAlpn) this.mAllSvcParams.get(1);
        return Collections.unmodifiableList(svcParamAlpn != null ? svcParamAlpn.getValue() : Collections.EMPTY_LIST);
    }

    public int getPort() {
        SvcParam.SvcParamPort svcParamPort = (SvcParam.SvcParamPort) this.mAllSvcParams.get(3);
        if (svcParamPort != null) {
            return svcParamPort.getValue().intValue();
        }
        return -1;
    }

    public List<InetAddress> getAddresses() {
        ArrayList arrayList = new ArrayList();
        SvcParam.SvcParamIpHint svcParamIpHint = (SvcParam.SvcParamIpHint) this.mAllSvcParams.get(4);
        if (svcParamIpHint != null) {
            arrayList.addAll(svcParamIpHint.getValue());
        }
        SvcParam.SvcParamIpHint svcParamIpHint2 = (SvcParam.SvcParamIpHint) this.mAllSvcParams.get(6);
        if (svcParamIpHint2 != null) {
            arrayList.addAll(svcParamIpHint2.getValue());
        }
        return arrayList;
    }

    public String getDohPath() {
        SvcParam.SvcParamDohPath svcParamDohPath = (SvcParam.SvcParamDohPath) this.mAllSvcParams.get(7);
        return svcParamDohPath != null ? svcParamDohPath.getValue() : "";
    }

    @Override // com.android.internal.telephony.util.DnsPacket.DnsRecord
    public String toString() {
        if (this.rType == 0) {
            return this.dName + " IN SVCB";
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (int i = 0; i < this.mAllSvcParams.size(); i++) {
            stringJoiner.add(((SvcParam) this.mAllSvcParams.valueAt(i)).toString());
        }
        return this.dName + " " + this.ttl + " IN SVCB " + this.mSvcPriority + " " + this.mTargetName + " " + stringJoiner.toString();
    }
}
