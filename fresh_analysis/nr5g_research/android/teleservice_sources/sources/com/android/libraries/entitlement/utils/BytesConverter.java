package com.android.libraries.entitlement.utils;

import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public abstract class BytesConverter {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static byte[] convertIntegerTo4Bytes(int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }
}
