package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class Registrant {
    WeakReference refH;
    Object userObj;
    int what;

    public Registrant(Handler handler, int i, Object obj) {
        this.refH = new WeakReference(handler);
        this.what = i;
        this.userObj = obj;
    }

    public void clear() {
        this.refH = null;
        this.userObj = null;
    }

    public void notifyRegistrant() {
        internalNotifyRegistrant(null, null);
    }

    public void notifyRegistrant(AsyncResult asyncResult) {
        internalNotifyRegistrant(asyncResult.result, asyncResult.exception);
    }

    void internalNotifyRegistrant(Object obj, Throwable th) {
        Handler handler = getHandler();
        if (handler == null) {
            clear();
            return;
        }
        Message messageObtain = Message.obtain();
        messageObtain.what = this.what;
        messageObtain.obj = new AsyncResult(this.userObj, obj, th);
        handler.sendMessage(messageObtain);
    }

    public Handler getHandler() {
        WeakReference weakReference = this.refH;
        if (weakReference == null) {
            return null;
        }
        return (Handler) weakReference.get();
    }
}
