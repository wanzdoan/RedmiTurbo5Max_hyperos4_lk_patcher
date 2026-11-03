package com.android.internal.telephony.util;

import android.text.TextUtils;
import java.net.InetAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

/* JADX INFO: loaded from: classes.dex */
public abstract class SvcParam {
    private final int mKey;

    SvcParam(int i) {
        this.mKey = i;
    }

    int getKey() {
        return this.mKey;
    }

    static SvcParam parseSvcParam(ByteBuffer byteBuffer) {
        try {
            int unsignedInt = Short.toUnsignedInt(byteBuffer.getShort());
            switch (unsignedInt) {
                case 0:
                    return new SvcParamMandatory(byteBuffer);
                case 1:
                    return new SvcParamAlpn(byteBuffer);
                case 2:
                    return new SvcParamNoDefaultAlpn(byteBuffer);
                case 3:
                    return new SvcParamPort(byteBuffer);
                case 4:
                    return new SvcParamIpv4Hint(byteBuffer);
                case 5:
                    return new SvcParamEch(byteBuffer);
                case 6:
                    return new SvcParamIpv6Hint(byteBuffer);
                case 7:
                    return new SvcParamDohPath(byteBuffer);
                default:
                    return new SvcParamGeneric(unsignedInt, byteBuffer);
            }
        } catch (BufferUnderflowException e) {
            throw new DnsPacket.ParseException("Malformed packet", e);
        }
    }

    static class SvcParamMandatory extends SvcParam {
        private final short[] mValue;

        private SvcParamMandatory(ByteBuffer byteBuffer) {
            super(0);
            short[] shortArray = SvcParamUtils.toShortArray(SvcParamUtils.sliceAndAdvance(byteBuffer, Short.toUnsignedInt(byteBuffer.getShort())));
            this.mValue = shortArray;
            if (shortArray.length == 0) {
                throw new DnsPacket.ParseException("mandatory value must be non-empty");
            }
        }

        short[] getValue() {
            return this.mValue;
        }

        public String toString() {
            StringJoiner stringJoiner = new StringJoiner(",");
            for (short s : this.mValue) {
                stringJoiner.add(SvcParam.toKeyName(s));
            }
            return SvcParam.toKeyName(getKey()) + "=" + stringJoiner.toString();
        }
    }

    static class SvcParamAlpn extends SvcParam {
        private final List mValue;

        SvcParamAlpn(ByteBuffer byteBuffer) {
            super(1);
            List stringList = SvcParamUtils.toStringList(SvcParamUtils.sliceAndAdvance(byteBuffer, Short.toUnsignedInt(byteBuffer.getShort())));
            this.mValue = stringList;
            if (stringList.isEmpty()) {
                throw new DnsPacket.ParseException("alpn value must be non-empty");
            }
        }

        List getValue() {
            return Collections.unmodifiableList(this.mValue);
        }

        public String toString() {
            return SvcParam.toKeyName(getKey()) + "=" + TextUtils.join(",", this.mValue);
        }
    }

    static class SvcParamNoDefaultAlpn extends SvcParam {
        SvcParamNoDefaultAlpn(ByteBuffer byteBuffer) {
            super(2);
            if (byteBuffer.getShort() != 0) {
                throw new DnsPacket.ParseException("no-default-alpn value must be empty");
            }
        }

        public String toString() {
            return SvcParam.toKeyName(getKey());
        }
    }

    static class SvcParamPort extends SvcParam {
        private final int mValue;

        SvcParamPort(ByteBuffer byteBuffer) {
            super(3);
            short s = byteBuffer.getShort();
            if (s != 2) {
                DnsSvcbPacket$$ExternalSyntheticBUOutline0.m("key port len is not 2 but ", s);
                throw null;
            }
            this.mValue = Short.toUnsignedInt(byteBuffer.getShort());
        }

        Integer getValue() {
            return Integer.valueOf(this.mValue);
        }

        public String toString() {
            return SvcParam.toKeyName(getKey()) + "=" + this.mValue;
        }
    }

    static class SvcParamIpHint extends SvcParam {
        private final List mValue;

        private SvcParamIpHint(int i, ByteBuffer byteBuffer, int i2) {
            super(i);
            List inetAddressList = SvcParamUtils.toInetAddressList(SvcParamUtils.sliceAndAdvance(byteBuffer, Short.toUnsignedInt(byteBuffer.getShort())), i2);
            this.mValue = inetAddressList;
            if (inetAddressList.isEmpty()) {
                throw new DnsPacket.ParseException(SvcParam.toKeyName(getKey()) + " value must be non-empty");
            }
        }

        List getValue() {
            return Collections.unmodifiableList(this.mValue);
        }

        public String toString() {
            StringJoiner stringJoiner = new StringJoiner(",");
            Iterator it = this.mValue.iterator();
            while (it.hasNext()) {
                stringJoiner.add(((InetAddress) it.next()).getHostAddress());
            }
            return SvcParam.toKeyName(getKey()) + "=" + stringJoiner.toString();
        }
    }

    static class SvcParamIpv4Hint extends SvcParamIpHint {
        /* JADX WARN: Illegal instructions before constructor call */
        SvcParamIpv4Hint(ByteBuffer byteBuffer) {
            int i = 4;
            super(i, byteBuffer, i);
        }
    }

    static class SvcParamIpv6Hint extends SvcParamIpHint {
        SvcParamIpv6Hint(ByteBuffer byteBuffer) {
            super(6, byteBuffer, 16);
        }
    }

    static class SvcParamEch extends SvcParamGeneric {
        SvcParamEch(ByteBuffer byteBuffer) {
            super(5, byteBuffer);
        }

        byte[] getValue() {
            return this.mValue;
        }
    }

    static class SvcParamDohPath extends SvcParam {
        private final String mValue;

        SvcParamDohPath(ByteBuffer byteBuffer) {
            super(7);
            byte[] bArr = new byte[Short.toUnsignedInt(byteBuffer.getShort())];
            byteBuffer.get(bArr);
            this.mValue = new String(bArr, StandardCharsets.UTF_8);
        }

        String getValue() {
            return this.mValue;
        }

        public String toString() {
            return SvcParam.toKeyName(getKey()) + "=" + this.mValue;
        }
    }

    static class SvcParamGeneric extends SvcParam {
        final byte[] mValue;

        SvcParamGeneric(int i, ByteBuffer byteBuffer) {
            super(i);
            byte[] bArr = new byte[Short.toUnsignedInt(byteBuffer.getShort())];
            this.mValue = bArr;
            byteBuffer.get(bArr);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(SvcParam.toKeyName(getKey()));
            byte[] bArr = this.mValue;
            if (bArr != null && bArr.length > 0) {
                sb.append("=");
                sb.append(HexDump.toHexString(this.mValue));
            }
            return sb.toString();
        }
    }

    static String toKeyName(int i) {
        switch (i) {
            case 0:
                return "mandatory";
            case 1:
                return "alpn";
            case 2:
                return "no-default-alpn";
            case 3:
                return "port";
            case 4:
                return "ipv4hint";
            case 5:
                return "ech";
            case 6:
                return "ipv6hint";
            case 7:
                return "dohpath";
            default:
                return "key" + i;
        }
    }
}
