package com.android.phone.firewall.config;

import android.content.Context;
import com.android.phone.firewall.utils.ThreadPool;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class ConfigFile {
    private static final byte[] HEADER = {119, 97, 108, 105, 109, 105, 110, 105};
    private Context mContext;
    protected ConcurrentHashMap mPairValues;
    private ConcurrentHashMap mTrans;

    protected abstract String getFileName();

    protected ConfigFile() {
    }

    protected void init(Context context) {
        this.mPairValues = new ConcurrentHashMap(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
        this.mContext = context.getApplicationContext();
        loadConfig();
    }

    public void loadConfig() {
        int length;
        this.mPairValues.clear();
        File file = new File(this.mContext.getFilesDir().getAbsoluteFile() + File.separator + getFileName());
        if (file.exists() && (length = (int) file.length()) != 0) {
            byte[] bArr = new byte[length];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(bArr);
                fileInputStream.close();
                byte[] bArrEncrypt = encrypt(bArr, "osughbalvpes.#$%^&*()_+".getBytes());
                int length2 = HEADER.length;
                for (int i = 0; i < length2; i++) {
                    if (bArrEncrypt[i] != HEADER[i]) {
                        return;
                    }
                }
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArrEncrypt));
                try {
                    try {
                        try {
                            dataInputStream.skip(length2);
                            short s = dataInputStream.readShort();
                            for (int i2 = 0; i2 < s; i2++) {
                                String string = readString(dataInputStream);
                                String string2 = readString(dataInputStream);
                                boolean z = true;
                                boolean z2 = string != null;
                                if (string2 == null) {
                                    z = false;
                                }
                                if (z & z2) {
                                    this.mPairValues.put(string, string2);
                                }
                            }
                            dataInputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            dataInputStream.close();
                        }
                    } catch (Throwable th) {
                        try {
                            dataInputStream.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: saveConfig, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$saveNow$0() {
        File file = new File(this.mContext.getFilesDir().getAbsoluteFile() + File.separator + getFileName());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        FileOutputStream fileOutputStream = null;
        try {
            try {
                try {
                    dataOutputStream.write(HEADER);
                    dataOutputStream.writeShort(this.mPairValues.size());
                    for (Map.Entry entry : this.mPairValues.entrySet()) {
                        writeString(dataOutputStream, (String) entry.getKey());
                        writeString(dataOutputStream, (String) entry.getValue());
                    }
                    byte[] bArrEncrypt = encrypt(byteArrayOutputStream.toByteArray(), "osughbalvpes.#$%^&*()_+".getBytes());
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file, false);
                    try {
                        fileOutputStream2.write(bArrEncrypt);
                        fileOutputStream2.flush();
                        byteArrayOutputStream.close();
                        try {
                            dataOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        fileOutputStream2.close();
                    } catch (Exception e2) {
                        e = e2;
                        fileOutputStream = fileOutputStream2;
                        e.printStackTrace();
                        try {
                            dataOutputStream.close();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                        fileOutputStream.close();
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        try {
                            dataOutputStream.close();
                        } catch (Exception e4) {
                            e4.printStackTrace();
                        }
                        try {
                            fileOutputStream.close();
                            throw th;
                        } catch (Exception e5) {
                            e5.printStackTrace();
                            throw th;
                        }
                    }
                } catch (Exception e6) {
                    e = e6;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e7) {
            e7.printStackTrace();
        }
    }

    public synchronized void set(String str, String str2) {
        if (!setToTrans(str, str2)) {
            this.mPairValues.put(str, str2);
        }
    }

    private synchronized String readString(DataInputStream dataInputStream) {
        byte[] bArr;
        bArr = new byte[dataInputStream.readShort()];
        dataInputStream.read(bArr);
        return new String(bArr);
    }

    private synchronized void writeString(DataOutputStream dataOutputStream, String str) {
        byte[] bytes = str.getBytes();
        dataOutputStream.writeShort(bytes.length);
        dataOutputStream.write(bytes);
    }

    public synchronized void saveNow() {
        ThreadPool.execute(new Runnable() { // from class: com.android.phone.firewall.config.ConfigFile$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$saveNow$0();
            }
        });
    }

    private boolean setToTrans(String str, String str2) {
        ConcurrentHashMap concurrentHashMap = this.mTrans;
        if (concurrentHashMap == null) {
            return false;
        }
        concurrentHashMap.put(str, str2);
        return true;
    }

    public byte[] encrypt(byte[] bArr, byte[] bArr2) {
        int i;
        byte[] bArr3 = new byte[bArr.length];
        int length = 0;
        while (length < bArr.length) {
            for (int i2 = 0; i2 < bArr2.length && (i = length + i2) < bArr.length; i2++) {
                bArr3[i] = (byte) (bArr[i] ^ bArr2[i2]);
            }
            length += bArr2.length;
        }
        return bArr3;
    }
}
