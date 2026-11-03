package com.android.internal.telephony.configupdate;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public abstract class ConfigParser<T> {
    public static final int VERSION_UNKNOWN = -1;
    protected T mConfig;
    protected int mVersion = -1;

    public byte[] getData(String str) {
        return null;
    }

    public abstract String getDomain();

    protected abstract void parseData(byte[] bArr);

    public ConfigParser(byte[] bArr) {
        parseData(bArr);
    }

    public ConfigParser(InputStream inputStream) throws IOException {
        parseData(inputStream.readAllBytes());
    }

    public ConfigParser(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            parseData(fileInputStream.readAllBytes());
            fileInputStream.close();
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public int getVersion() {
        return this.mVersion;
    }

    public T getConfig() {
        return this.mConfig;
    }

    protected void overrideVersion(int i) {
        this.mVersion = i;
        Log.d("ConfigParser", "overrideVersion: mVersion=" + this.mVersion);
    }

    public void overrideConfig(T t) {
        Log.d("ConfigParser", "overrideConfig: overriding config to " + t);
        this.mConfig = t;
    }
}
