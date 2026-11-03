package com.android.phone;

import android.os.Message;
import gov.nist.core.GenericObject$$ExternalSyntheticBUOutline0;

/* JADX INFO: compiled from: QmsMsgTunnelStrategy.java */
/* JADX INFO: loaded from: classes.dex */
class QmsRequest {
    static int sNextSerial = 1000;
    private static QmsRequest sPool;
    private static int sPoolSize;
    QmsRequest mNext;
    int mRequest;
    Message mResult;
    int mSerial;
    static Object sSerialMonitor = new Object();
    private static Object sPoolSync = new Object();

    static QmsRequest obtain(int i, Message message) {
        QmsRequest qmsRequest;
        synchronized (sPoolSync) {
            try {
                qmsRequest = sPool;
                if (qmsRequest != null) {
                    sPool = qmsRequest.mNext;
                    qmsRequest.mNext = null;
                    sPoolSize--;
                } else {
                    qmsRequest = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (qmsRequest == null) {
            qmsRequest = new QmsRequest();
        }
        synchronized (sSerialMonitor) {
            int i2 = sNextSerial;
            sNextSerial = i2 + 1;
            qmsRequest.mSerial = i2;
        }
        qmsRequest.mRequest = i;
        qmsRequest.mResult = message;
        if (message == null || message.getTarget() != null) {
            return qmsRequest;
        }
        GenericObject$$ExternalSyntheticBUOutline0.m("Message target must not be null");
        return null;
    }

    void release() {
        synchronized (sPoolSync) {
            try {
                int i = sPoolSize;
                if (i < 4) {
                    this.mNext = sPool;
                    sPool = this;
                    sPoolSize = i + 1;
                    this.mResult = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private QmsRequest() {
    }
}
