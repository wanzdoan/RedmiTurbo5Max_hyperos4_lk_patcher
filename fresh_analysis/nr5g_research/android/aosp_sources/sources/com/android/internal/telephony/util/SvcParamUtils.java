package com.android.internal.telephony.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SvcParamUtils {
    public static ByteBuffer sliceAndAdvance(ByteBuffer byteBuffer, int i) throws BufferUnderflowException {
        if (byteBuffer.remaining() < i) {
            throw new BufferUnderflowException();
        }
        int iPosition = byteBuffer.position();
        ByteBuffer byteBufferSlice = ((ByteBuffer) byteBuffer.slice().limit(i)).slice();
        byteBuffer.position(iPosition + i);
        return byteBufferSlice.asReadOnlyBuffer();
    }

    static List toStringList(ByteBuffer byteBuffer) {
        ArrayList arrayList = new ArrayList();
        while (byteBuffer.hasRemaining()) {
            int unsignedInt = Byte.toUnsignedInt(byteBuffer.get());
            if (unsignedInt == 0) {
                throw new DnsPacket.ParseException("alpn should not be an empty string");
            }
            byte[] bArr = new byte[unsignedInt];
            byteBuffer.get(bArr);
            arrayList.add(new String(bArr, StandardCharsets.UTF_8));
        }
        return arrayList;
    }

    static short[] toShortArray(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() % 2 != 0) {
            throw new DnsPacket.ParseException("Can't parse whole byte array");
        }
        ShortBuffer shortBufferAsShortBuffer = byteBuffer.asShortBuffer();
        short[] sArr = new short[shortBufferAsShortBuffer.remaining()];
        shortBufferAsShortBuffer.get(sArr);
        return sArr;
    }

    static List toInetAddressList(ByteBuffer byteBuffer, int i) {
        if (byteBuffer.remaining() % i != 0) {
            throw new DnsPacket.ParseException("Can't parse whole byte array");
        }
        ArrayList arrayList = new ArrayList();
        byte[] bArr = new byte[i];
        while (byteBuffer.remaining() >= i) {
            byteBuffer.get(bArr);
            try {
                arrayList.add(InetAddress.getByAddress(bArr));
            } catch (UnknownHostException unused) {
                throw new DnsPacket.ParseException("Can't parse byte array as an IP address");
            }
        }
        return arrayList;
    }
}
