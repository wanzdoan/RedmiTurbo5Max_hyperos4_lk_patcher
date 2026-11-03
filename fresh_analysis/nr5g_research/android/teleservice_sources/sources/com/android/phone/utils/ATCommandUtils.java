package com.android.phone.utils;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.Rlog;
import android.util.Log;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.PhoneGlobals;
import java.util.Arrays;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class ATCommandUtils {
    protected static volatile ATCommandUtils mInstance;
    private static final Object sAtCmdLock = new Object();
    private final Handler mAtCmdHandler = new Handler(this, BackgroundThread.get().getLooper()) { // from class: com.android.phone.utils.ATCommandUtils.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Consumer consumer;
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult == null || (consumer = (Consumer) asyncResult.userObj) == null) {
                return;
            }
            consumer.accept(message);
        }
    };
    private Object mRadioInteractor;

    public static ATCommandUtils getInstance() {
        if (mInstance == null) {
            synchronized (ATCommandUtils.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new ATCommandUtils();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    public static ATCommandUtils make() {
        Log.d("ATCommandsUtils", "ATCommandUtils make");
        if (mInstance != null) {
            return mInstance;
        }
        mInstance = new ATCommandUtils();
        return mInstance;
    }

    private ATCommandUtils() {
        if (ModemProvider.IS_SPRD) {
            initRadioInteractor();
        }
    }

    private void initRadioInteractor() {
        try {
            this.mRadioInteractor = Class.forName("com.android.unisoc.telephony.RadioInteractor").getConstructor(Context.class).newInstance(PhoneGlobals.getInstance());
            log("RadioInteractor initialized for SPRD");
        } catch (Exception e) {
            log("Failed to init RadioInteractor: " + e);
        }
    }

    public int sendAtCmdSync(String str, String[] strArr, int i) {
        Object obj = this.mRadioInteractor;
        if (obj == null) {
            return -1;
        }
        try {
            return ((Integer) obj.getClass().getMethod("sendAtCmd", String.class, String[].class, Integer.TYPE).invoke(this.mRadioInteractor, str, strArr, Integer.valueOf(i))).intValue();
        } catch (Exception e) {
            log("sendAtCmdSync error: " + e);
            return -1;
        }
    }

    public boolean atCmdRunSync(int i, String[] strArr, final Object[] objArr) {
        if (Looper.myLooper() == this.mAtCmdHandler.getLooper()) {
            log("error! AtCmdSync from same thread is not allowed");
            return false;
        }
        final Boolean[] boolArr = new Boolean[1];
        Consumer consumer = new Consumer() { // from class: com.android.phone.utils.ATCommandUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ATCommandUtils.$r8$lambda$UCCAsVWkMBD3s8gPYgOwsgz3VUM(objArr, boolArr, (Message) obj);
            }
        };
        log(i + " atCmdRunSync " + strArr[0]);
        sendAtCommandInternal(i, strArr, this.mAtCmdHandler.obtainMessage(0, consumer));
        synchronized (sAtCmdLock) {
            long jElapsedRealtime = SystemClock.elapsedRealtime() + 3000;
            while (boolArr[0] == null && jElapsedRealtime > SystemClock.elapsedRealtime()) {
                try {
                    sAtCmdLock.wait(1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }
        log(i + " atCmdRunSync ret:" + boolArr[0]);
        if (boolArr[0] == null) {
            boolArr[0] = Boolean.FALSE;
        }
        return boolArr[0].booleanValue();
    }

    public static /* synthetic */ void $r8$lambda$UCCAsVWkMBD3s8gPYgOwsgz3VUM(Object[] objArr, Boolean[] boolArr, Message message) {
        AsyncResult asyncResult = (AsyncResult) message.obj;
        Object obj = sAtCmdLock;
        synchronized (obj) {
            if (objArr != null) {
                try {
                    objArr[0] = asyncResult.result;
                } catch (Throwable th) {
                    throw th;
                }
            }
            boolArr[0] = Boolean.valueOf(asyncResult == null || asyncResult.exception == null);
            obj.notifyAll();
        }
    }

    private void sendAtCommandInternal(int i, String[] strArr, Message message) {
        Exception exc;
        if (strArr == null || strArr.length == 0) {
            return;
        }
        if (!ModemProvider.IS_SPRD) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                phone.invokeOemRilRequestStrings(strArr, message);
                return;
            }
            return;
        }
        String[] strArr2 = new String[3];
        int length = strArr.length;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            String str = strArr[i2];
            int iSendAtCmdSync = sendAtCmdSync(str, strArr2, i);
            log(i + " sendAtCommandInternal(SPRD) cmd:" + str + "; ret:" + iSendAtCmdSync + "; response:" + Arrays.toString(strArr2));
            if (iSendAtCmdSync != 0) {
                i3 = iSendAtCmdSync;
                break;
            } else {
                i2++;
                i3 = iSendAtCmdSync;
            }
        }
        if (message != null) {
            try {
                Consumer consumer = (Consumer) message.obj;
                if (i3 == 0) {
                    exc = null;
                } else {
                    exc = new Exception("AT cmd failed, ret=" + i3);
                }
                message.obj = new AsyncResult((Object) null, strArr2, exc);
                consumer.accept(message);
            } catch (Exception e) {
                log("sendAtCommandInternal(SPRD) e = " + e);
            }
        }
    }

    public void sendAtCommand(Phone phone, String[] strArr, Message message) {
        if (phone != null) {
            sendAtCommand(phone.getPhoneId(), strArr, message);
        }
    }

    public void sendAtCommand(int i, String[] strArr, Message message) {
        Exception exc;
        if (strArr == null || strArr.length == 0) {
            return;
        }
        if (!ModemProvider.IS_SPRD) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                phone.invokeOemRilRequestStrings(strArr, message);
                return;
            }
            return;
        }
        String[] strArr2 = new String[3];
        int length = strArr.length;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            String str = strArr[i2];
            int iSendAtCmdSync = sendAtCmdSync(str, strArr2, i);
            log(i + " sendAtCommand(SPRD) cmd:" + str + "; ret:" + iSendAtCmdSync + "; response:" + Arrays.toString(strArr2));
            if (iSendAtCmdSync != 0) {
                i3 = iSendAtCmdSync;
                break;
            } else {
                i2++;
                i3 = iSendAtCmdSync;
            }
        }
        if (message == null || message.getTarget() == null) {
            return;
        }
        if (i3 == 0) {
            exc = null;
        } else {
            exc = new Exception("AT cmd failed, ret=" + i3);
        }
        AsyncResult.forMessage(message, strArr2, exc);
        message.sendToTarget();
    }

    public void regUnRegisterOemAtUrcInd(boolean z, Handler handler, int i) {
        log("regUnRegisterOemAtUrcInd register:" + z + "; event:" + i);
        if (ModemProvider.IS_SPRD) {
            log("SPRD uses RadioInteractorListener directly, skip OemAtUrcInd registration");
            return;
        }
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            Phone phone = PhoneFactory.getPhone(i2);
            if (phone != null) {
                if (z) {
                    phone.registerForOemIndication(handler, i, phone);
                } else {
                    phone.unregisterForOemIndication(handler);
                }
            }
        }
    }

    protected static void log(String str) {
        Rlog.d("ATCommandsUtils", str);
    }
}
