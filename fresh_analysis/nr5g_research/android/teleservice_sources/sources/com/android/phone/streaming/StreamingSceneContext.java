package com.android.phone.streaming;

import android.util.Log;
import java.io.PrintWriter;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public class StreamingSceneContext {
    private Supplier mResidentCellProvider;
    private int mDdsSlot = -1;
    private int mAppType = 0;
    private boolean mIsMobileDataConnected = false;
    private boolean mIsOverSeaSubway = false;
    private boolean mIsSubway = false;
    private boolean mIsHighSpeedTrain = false;
    private boolean mIsDynamicScene = false;
    private String mHstLineName = "";
    private final Object mLock = new Object();

    public int getDdsSlot() {
        int i;
        synchronized (this.mLock) {
            i = this.mDdsSlot;
        }
        return i;
    }

    public void setDdsSlot(int i) {
        synchronized (this.mLock) {
            this.mDdsSlot = i;
            Log.d("StreamingSceneContext", "setDdsSlot: " + i);
        }
    }

    public int getAppType() {
        int i;
        synchronized (this.mLock) {
            i = this.mAppType;
        }
        return i;
    }

    public void setAppType(int i) {
        synchronized (this.mLock) {
            this.mAppType = i;
            Log.d("StreamingSceneContext", "setAppType: " + i);
        }
    }

    public boolean isMobileDataConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsMobileDataConnected;
        }
        return z;
    }

    public void setMobileDataConnected(boolean z) {
        synchronized (this.mLock) {
            this.mIsMobileDataConnected = z;
            Log.d("StreamingSceneContext", "setMobileDataConnected: " + z);
        }
    }

    public boolean isOverSeaSubway() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsOverSeaSubway;
        }
        return z;
    }

    public void setOverSeaSubway(boolean z) {
        synchronized (this.mLock) {
            this.mIsOverSeaSubway = z;
            Log.d("StreamingSceneContext", "setOverSeaSubway: " + z);
        }
    }

    public void setSubway(boolean z) {
        synchronized (this.mLock) {
            this.mIsSubway = z;
            Log.d("StreamingSceneContext", "setSubway: " + z);
        }
    }

    public boolean isHighSpeedTrain() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsHighSpeedTrain;
        }
        return z;
    }

    public void setHighSpeedTrain(boolean z) {
        synchronized (this.mLock) {
            this.mIsHighSpeedTrain = z;
            Log.d("StreamingSceneContext", "setHighSpeedTrain: " + z);
        }
    }

    public boolean isDynamicScene() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsDynamicScene;
        }
        return z;
    }

    public void setDynamicScene(boolean z) {
        synchronized (this.mLock) {
            this.mIsDynamicScene = z;
            Log.d("StreamingSceneContext", "setDynamicScene: " + z);
        }
    }

    public void setResidentCellProvider(Supplier supplier) {
        synchronized (this.mLock) {
            this.mResidentCellProvider = supplier;
            StringBuilder sb = new StringBuilder("setResidentCellProvider: ");
            sb.append(supplier != null);
            Log.d("StreamingSceneContext", sb.toString());
        }
    }

    public boolean isResidentCell() {
        boolean z;
        synchronized (this.mLock) {
            try {
                Supplier supplier = this.mResidentCellProvider;
                z = supplier != null && Boolean.TRUE.equals(supplier.get());
            } catch (Throwable th) {
                throw th;
            }
        }
        return z;
    }

    public String getHstLineName() {
        String str;
        synchronized (this.mLock) {
            str = this.mHstLineName;
        }
        return str;
    }

    public void setHstLineName(String str) {
        synchronized (this.mLock) {
            this.mHstLineName = str != null ? str : "";
            Log.d("StreamingSceneContext", "setHstLineName: " + str);
        }
    }

    public void dump(PrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.println("  ddsSlot=" + this.mDdsSlot + ", appType=" + this.mAppType + ", mobileData=" + this.mIsMobileDataConnected);
            printWriter.println("  dynamic=" + this.mIsDynamicScene + ", residentCell=" + isResidentCell() + ", hstLine=" + this.mHstLineName + ", subway=" + this.mIsSubway + ", hst=" + this.mIsHighSpeedTrain + ", overSeaSubway=" + this.mIsOverSeaSubway);
        }
    }
}
