package com.android.phone.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public abstract class CountableHandler extends Handler {
    private final int MAX_PENDING_MESSAGE_COUNT;
    private final AtomicInteger mMsgCount;

    public CountableHandler(Looper looper) {
        super(looper);
        this.mMsgCount = new AtomicInteger(0);
        this.MAX_PENDING_MESSAGE_COUNT = 1000;
    }

    @Override // android.os.Handler
    public boolean sendMessageAtTime(Message message, long j) {
        if (this.mMsgCount.get() >= 1000) {
            Log.e("CountableHandler", "Pending message count is over 200, drop message.msg.what = " + message.what + " handler = " + toString());
            return false;
        }
        this.mMsgCount.incrementAndGet();
        return super.sendMessageAtTime(message, j);
    }

    public void removeMessageWithCount(int i) {
        if (hasMessages(i)) {
            this.mMsgCount.decrementAndGet();
        }
        super.removeMessages(i);
    }

    public void removeMessageWithCount(int i, Object obj) {
        if (hasMessages(i, obj)) {
            this.mMsgCount.decrementAndGet();
        }
        super.removeMessages(i, obj);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        this.mMsgCount.decrementAndGet();
    }
}
