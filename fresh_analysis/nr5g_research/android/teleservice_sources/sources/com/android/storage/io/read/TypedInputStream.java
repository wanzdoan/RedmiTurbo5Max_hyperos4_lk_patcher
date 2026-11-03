package com.android.storage.io.read;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.InputStream;
import miuix.animation.internal.TransitionInfo;

/* JADX INFO: loaded from: classes.dex */
public final class TypedInputStream implements Closeable {
    private final DataInputStream mDataInputStream;

    public TypedInputStream(InputStream inputStream) {
        this.mDataInputStream = new DataInputStream(new BufferedInputStream(inputStream, 8192));
    }

    public int skipBytes(int i) {
        return this.mDataInputStream.skipBytes(i);
    }

    public int readUnsignedByte() {
        return this.mDataInputStream.readByte() & TransitionInfo.INIT;
    }

    public byte[] readTinyVarByteArray() {
        int unsignedByte = readUnsignedByte();
        byte[] bArr = new byte[unsignedByte];
        for (int i = 0; i < unsignedByte; i++) {
            bArr[i] = (byte) this.mDataInputStream.readUnsignedByte();
        }
        return bArr;
    }

    public char readChar() {
        return this.mDataInputStream.readChar();
    }

    public int readInt() {
        return this.mDataInputStream.readInt();
    }

    public long readLong() {
        return this.mDataInputStream.readLong();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.mDataInputStream.close();
    }
}
