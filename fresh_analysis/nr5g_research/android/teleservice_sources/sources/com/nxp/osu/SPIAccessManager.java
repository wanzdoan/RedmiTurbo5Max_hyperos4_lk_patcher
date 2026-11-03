package com.nxp.osu;

import android.content.Context;
import android.util.Log;
import com.nxp.osu.channel.OsuOmapiApduChannel;
import com.nxp.osu.channel.OsuOmapiHelper;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class SPIAccessManager {
    private static final byte[] SUCCESS_RESP = {-112, 0};
    private static volatile SPIAccessManager sInstance;
    private final Context mContext;
    private OsuOmapiApduChannel mChannel = null;
    private final Object mLock = new Object();
    private final Object delayLock = new Object();

    private SPIAccessManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static SPIAccessManager getInstance(Context context) {
        SPIAccessManager sPIAccessManager;
        SPIAccessManager sPIAccessManager2 = sInstance;
        if (sPIAccessManager2 != null) {
            return sPIAccessManager2;
        }
        synchronized (SPIAccessManager.class) {
            try {
                sPIAccessManager = sInstance;
                if (sPIAccessManager == null) {
                    sPIAccessManager = new SPIAccessManager(context);
                    sInstance = sPIAccessManager;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return sPIAccessManager;
    }

    private void setOSUConfig(SecOsuAgent secOsuAgent) {
        secOsuAgent.secOsuSetConfig(SecOsuConfigType.NFC_DISABLE_SUPPORT, SecOsuConfigValue.DISABLED);
    }

    /* JADX WARN: Code duplicated, block: B:47:0x00f8 A[Catch: all -> 0x0011, TRY_ENTER, TryCatch #1 {all -> 0x0011, blocks: (B:4:0x0003, B:6:0x0008, B:7:0x000f, B:13:0x0017, B:14:0x001e, B:32:0x007f, B:49:0x0100, B:39:0x00cc, B:51:0x0106, B:52:0x0109, B:47:0x00f8, B:48:0x00fb, B:53:0x010a, B:54:0x0128), top: B:60:0x0003 }] */
    public boolean blockAccessOverSPI(SecOsuAgent secOsuAgent) {
        OsuOmapiApduChannel osuOmapiApduChannel;
        Exception e;
        synchronized (this.mLock) {
            try {
                if (this.mChannel != null) {
                    Log.w("SPIAccessManager", "SPI already blocked by this instance.");
                    return true;
                }
                if (secOsuAgent == null) {
                    Log.w("SPIAccessManager", "OsuAgent not initialized");
                    return false;
                }
                for (int i = 1; i <= 3; i++) {
                    OsuOmapiApduChannel osuOmapiApduChannel2 = null;
                    try {
                        try {
                            osuOmapiApduChannel = OsuOmapiApduChannel.getInstance(this.mContext, false, ISecOsuInterface.TERMINAL_TYPE.SPI.getValue());
                            if (osuOmapiApduChannel == null) {
                                try {
                                    try {
                                        Log.e("SPIAccessManager", "Attempt " + i + ": getInstance returned null.");
                                        if (osuOmapiApduChannel != null) {
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        osuOmapiApduChannel2 = osuOmapiApduChannel;
                                        if (osuOmapiApduChannel2 != null) {
                                            osuOmapiApduChannel2.close();
                                        }
                                        throw th;
                                    }
                                } catch (SecOsuException | IOException e2) {
                                    e = e2;
                                    Log.e("SPIAccessManager", "Attempt " + i + " failed: " + e.getMessage());
                                    if (osuOmapiApduChannel != null) {
                                        osuOmapiApduChannel.close();
                                    }
                                    performDelay(200L);
                                }
                            } else {
                                byte[] bArrOpen = osuOmapiApduChannel.open();
                                byte[] bArr = SUCCESS_RESP;
                                if (Arrays.equals(bArrOpen, bArr)) {
                                    if (Arrays.equals(osuOmapiApduChannel.openChannel(OsuOmapiHelper.OSUTrigger), bArr)) {
                                        this.mChannel = osuOmapiApduChannel;
                                        setOSUConfig(secOsuAgent);
                                        Log.i("SPIAccessManager", "SPI access successfully blocked (attempt " + i + ").");
                                        return true;
                                    }
                                    Log.e("SPIAccessManager", "Attempt " + i + ": openChannel(DummyAID) failed");
                                    Log.e("SPIAccessManager", "Attempt " + i + " failed: " + e.getMessage());
                                    if (osuOmapiApduChannel != null) {
                                        osuOmapiApduChannel.close();
                                    }
                                    performDelay(200L);
                                } else {
                                    Log.e("SPIAccessManager", "Attempt " + i + ": open() failed");
                                }
                            }
                            osuOmapiApduChannel.close();
                        } catch (SecOsuException | IOException e3) {
                            osuOmapiApduChannel = null;
                            e = e3;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                setOSUConfig(secOsuAgent);
                Log.e("OSU", "Failed to block access over SPI after 3 tries.");
                return false;
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }

    private void performDelay(long j) {
        synchronized (this.delayLock) {
            try {
                this.delayLock.wait(j);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void unBlockAccessOverSPI() {
        synchronized (this.mLock) {
            try {
                if (this.mChannel != null) {
                    Log.d("SPIAccessManager", "Unblocking eSE-SPI access");
                    OsuOmapiApduChannel.sIsRefreshRequired = false;
                    this.mChannel.close();
                    this.mChannel = null;
                } else {
                    Log.d("SPIAccessManager", "unBlock called but SPI was not blocked. Doing nothing.");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
