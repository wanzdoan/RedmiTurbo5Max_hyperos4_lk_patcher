package com.android.internal.telephony.imsphone;

import android.os.AsyncResult;
import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
public interface IMiuiImsMmiCodeCallBack {
    void onSetComplete(Message message, AsyncResult asyncResult);

    void onSuppSvcQueryComplete(AsyncResult asyncResult);
}
