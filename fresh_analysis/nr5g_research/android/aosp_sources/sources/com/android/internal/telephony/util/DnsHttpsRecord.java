package com.android.internal.telephony.util;

import android.util.SparseArray;
import com.android.internal.telephony.LocaleTracker$$ExternalSyntheticBUOutline0;
import java.net.InetAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DnsHttpsRecord extends DnsPacket.DnsRecord {
    public static final int ALIAS_MODE_PRIORITY = 0;
    public static final String DEFAULT_HTTPS_ALPN_ID = "http/1.1";
    public static final int DEFAULT_HTTPS_PORT_VALUE = 443;
    private final SparseArray mSvcParams;
    private final int mSvcPriority;
    private final String mTargetName;

    public DnsHttpsRecord(int i, ByteBuffer byteBuffer) throws IllegalStateException, DnsPacket.ParseException, BufferUnderflowException {
        super(i, byteBuffer);
        this.mSvcParams = new SparseArray();
        if (this.nsType != 65) {
            LocaleTracker$$ExternalSyntheticBUOutline0.m("incorrect nsType: ", this.nsType);
            throw null;
        }
        if (this.nsClass != 1) {
            DnsSvcbRecord$$ExternalSyntheticBUOutline0.m("incorrect nsClass: ", this.nsClass);
            throw null;
        }
        byte[] rr = getRR();
        if (rr == null || rr.length == 0) {
            throw new DnsPacket.ParseException("HTTPS rdata is empty");
        }
        ByteBuffer byteBufferAsReadOnlyBuffer = ByteBuffer.wrap(rr).asReadOnlyBuffer();
        int unsignedInt = Short.toUnsignedInt(byteBufferAsReadOnlyBuffer.getShort());
        this.mSvcPriority = unsignedInt;
        String name = DnsPacketUtils$DnsRecordParser.parseName(byteBufferAsReadOnlyBuffer, 0, true);
        this.mTargetName = name;
        if (name.length() > 255) {
            DnsSvcbRecord$$ExternalSyntheticBUOutline0.m("Failed to parse HTTPS target name, name size is too long: ", name.length());
            throw null;
        }
        if (unsignedInt == 0) {
            return;
        }
        int i2 = -1;
        while (byteBufferAsReadOnlyBuffer.hasRemaining()) {
            SvcParam svcParam = SvcParam.parseSvcParam(byteBufferAsReadOnlyBuffer);
            int key = svcParam.getKey();
            if (this.mSvcParams.contains(key)) {
                throw new DnsPacket.ParseException("Invalid DnsHttpsRecord: key " + key + " is repeated");
            }
            if (key <= i2) {
                throw new DnsPacket.ParseException("Invalid DnsHttpsRecord: keys must be in increasing order");
            }
            this.mSvcParams.put(key, svcParam);
            i2 = key;
        }
    }

    public void verifyMandatoryKeys() throws DnsPacket.ParseException {
        for (short s : getMandatory()) {
            int unsignedInt = Short.toUnsignedInt(s);
            if (!this.mSvcParams.contains(unsignedInt)) {
                throw new DnsPacket.ParseException("Invalid DnsHttpsRecord: mandatory key " + SvcParam.toKeyName(unsignedInt) + " is missing");
            }
        }
    }

    public int getPriority() {
        return this.mSvcPriority;
    }

    public String getTargetName() {
        return this.mTargetName;
    }

    public String getOwnerName() {
        return this.dName;
    }

    public short[] getMandatory() {
        SvcParam svcParam = (SvcParam) this.mSvcParams.get(0);
        if (svcParam == null || !(svcParam instanceof SvcParam.SvcParamMandatory)) {
            return new short[0];
        }
        return ((SvcParam.SvcParamMandatory) svcParam).getValue();
    }

    public List<String> getAlpnIds() {
        boolean z = true;
        SvcParam svcParam = (SvcParam) this.mSvcParams.get(1);
        SvcParam svcParam2 = (SvcParam) this.mSvcParams.get(2);
        if (svcParam2 != null && (svcParam2 instanceof SvcParam.SvcParamNoDefaultAlpn)) {
            z = false;
        }
        if (svcParam == null || !(svcParam instanceof SvcParam.SvcParamAlpn)) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(((SvcParam.SvcParamAlpn) svcParam).getValue());
        if (z && !arrayList.contains(DEFAULT_HTTPS_ALPN_ID)) {
            arrayList.add(DEFAULT_HTTPS_ALPN_ID);
        }
        return Collections.unmodifiableList(arrayList);
    }

    public int getPort() {
        SvcParam svcParam = (SvcParam) this.mSvcParams.get(3);
        return (svcParam == null || !(svcParam instanceof SvcParam.SvcParamPort)) ? DEFAULT_HTTPS_PORT_VALUE : ((SvcParam.SvcParamPort) svcParam).getValue().intValue();
    }

    public List<InetAddress> getIpv4Hints() {
        SvcParam svcParam = (SvcParam) this.mSvcParams.get(4);
        if (svcParam == null || !(svcParam instanceof SvcParam.SvcParamIpv4Hint)) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(((SvcParam.SvcParamIpv4Hint) svcParam).getValue());
    }

    public byte[] getEchConfigList() {
        SvcParam.SvcParamEch svcParamEch = (SvcParam.SvcParamEch) this.mSvcParams.get(5);
        if (svcParamEch != null) {
            return svcParamEch.getValue();
        }
        return null;
    }

    public List<InetAddress> getIpv6Hints() {
        SvcParam svcParam = (SvcParam) this.mSvcParams.get(6);
        if (svcParam == null || !(svcParam instanceof SvcParam.SvcParamIpv6Hint)) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableList(((SvcParam.SvcParamIpv6Hint) svcParam).getValue());
    }

    public String getDohPath() {
        SvcParam svcParam = (SvcParam) this.mSvcParams.get(7);
        if (svcParam == null || !(svcParam instanceof SvcParam.SvcParamDohPath)) {
            return null;
        }
        return ((SvcParam.SvcParamDohPath) svcParam).getValue();
    }
}
