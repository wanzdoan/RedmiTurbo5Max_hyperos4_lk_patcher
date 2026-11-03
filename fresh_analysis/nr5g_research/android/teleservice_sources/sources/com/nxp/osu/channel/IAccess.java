package com.nxp.osu.channel;

/* JADX INFO: loaded from: classes.dex */
public interface IAccess {
    void close();

    byte[] getAtr();

    byte[] open();

    byte[] openChannel(byte[] bArr);

    void reset();

    byte[] send(byte[] bArr);
}
