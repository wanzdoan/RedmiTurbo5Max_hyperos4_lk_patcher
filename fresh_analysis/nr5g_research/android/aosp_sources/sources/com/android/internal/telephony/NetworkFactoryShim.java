package com.android.internal.telephony;

import android.net.NetworkCapabilities;
import android.net.NetworkProvider;
import android.net.NetworkRequest;
import android.net.NetworkScore;
import android.os.Looper;
import android.os.Message;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
interface NetworkFactoryShim {
    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    Looper getLooper();

    NetworkProvider getProvider();

    int getRequestCount();

    int getSerialNumber();

    Message obtainMessage(int i, int i2, int i3, Object obj);

    void reevaluateAllRequests();

    void register(String str);

    void releaseRequestAsUnfulfillableByAnyFactory(NetworkRequest networkRequest);

    void setCapabilityFilter(NetworkCapabilities networkCapabilities);

    void setScoreFilter(int i);

    void setScoreFilter(NetworkScore networkScore);

    void terminate();

    default void registerIgnoringScore(String str) {
        throw new UnsupportedOperationException();
    }
}
