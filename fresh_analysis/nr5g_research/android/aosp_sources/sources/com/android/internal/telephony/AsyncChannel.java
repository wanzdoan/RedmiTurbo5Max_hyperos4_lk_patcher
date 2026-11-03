package com.android.internal.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import java.util.Stack;

/* JADX INFO: loaded from: classes.dex */
public class AsyncChannel {
    public static final int CMD_CHANNEL_DISCONNECT = 69635;
    public static final int CMD_CHANNEL_DISCONNECTED = 69636;
    public static final int CMD_CHANNEL_FULLY_CONNECTED = 69634;
    public static final int CMD_CHANNEL_FULL_CONNECTION = 69633;
    public static final int CMD_CHANNEL_HALF_CONNECTED = 69632;
    public static final int STATUS_BINDING_UNSUCCESSFUL = 1;
    public static final int STATUS_FULL_CONNECTION_REFUSED_ALREADY_CONNECTED = 3;
    public static final int STATUS_REMOTE_DISCONNECTION = 4;
    public static final int STATUS_SEND_UNSUCCESSFUL = 2;
    public static final int STATUS_SUCCESSFUL = 0;
    private static String[] sCmdToString = {"CMD_CHANNEL_HALF_CONNECTED", "CMD_CHANNEL_FULL_CONNECTION", "CMD_CHANNEL_FULLY_CONNECTED", "CMD_CHANNEL_DISCONNECT", "CMD_CHANNEL_DISCONNECTED"};
    private AsyncChannelConnection mConnection;
    private DeathMonitor mDeathMonitor;
    private Messenger mDstMessenger;
    private Context mSrcContext;
    private Handler mSrcHandler;
    private Messenger mSrcMessenger;

    protected static String cmdToString(int i) {
        int i2 = i - CMD_CHANNEL_HALF_CONNECTED;
        if (i2 < 0) {
            return null;
        }
        String[] strArr = sCmdToString;
        if (i2 < strArr.length) {
            return strArr[i2];
        }
        return null;
    }

    public int connectSrcHandlerToPackageSync(Context context, Handler handler, String str, String str2) {
        this.mConnection = new AsyncChannelConnection();
        this.mSrcContext = context;
        this.mSrcHandler = handler;
        this.mSrcMessenger = new Messenger(handler);
        this.mDstMessenger = null;
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClassName(str, str2);
        return !context.bindService(intent, this.mConnection, 1) ? 1 : 0;
    }

    public int connectSync(Context context, Handler handler, Messenger messenger) {
        connected(context, handler, messenger);
        return 0;
    }

    public int connectSync(Context context, Handler handler, Handler handler2) {
        return connectSync(context, handler, new Messenger(handler2));
    }

    public int fullyConnectSync(Context context, Handler handler, Handler handler2) {
        int iConnectSync = connectSync(context, handler, handler2);
        return iConnectSync == 0 ? sendMessageSynchronously(CMD_CHANNEL_FULL_CONNECTION).arg1 : iConnectSync;
    }

    public void connect(Context context, Handler handler, String str, String str2) {
        new Thread(new Runnable(context, handler, str, str2) { // from class: com.android.internal.telephony.AsyncChannel.1ConnectAsync
            String mDstClassName;
            String mDstPackageName;
            Context mSrcCtx;
            Handler mSrcHdlr;

            {
                this.mSrcCtx = context;
                this.mSrcHdlr = handler;
                this.mDstPackageName = str;
                this.mDstClassName = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                AsyncChannel.this.replyHalfConnected(AsyncChannel.this.connectSrcHandlerToPackageSync(this.mSrcCtx, this.mSrcHdlr, this.mDstPackageName, this.mDstClassName));
            }
        }).start();
    }

    public void connect(Context context, Handler handler, Class<?> cls) {
        connect(context, handler, cls.getPackage().getName(), cls.getName());
    }

    public void connect(Context context, Handler handler, Messenger messenger) {
        connected(context, handler, messenger);
        replyHalfConnected(0);
    }

    public void connected(Context context, Handler handler, Messenger messenger) {
        this.mSrcContext = context;
        this.mSrcHandler = handler;
        this.mSrcMessenger = new Messenger(this.mSrcHandler);
        this.mDstMessenger = messenger;
    }

    public void connect(Context context, Handler handler, Handler handler2) {
        connect(context, handler, new Messenger(handler2));
    }

    public void connect(AsyncService asyncService, Messenger messenger) {
        connect(asyncService, asyncService.getHandler(), messenger);
    }

    public void disconnected() {
        this.mSrcContext = null;
        this.mSrcHandler = null;
        this.mSrcMessenger = null;
        this.mDstMessenger = null;
        this.mDeathMonitor = null;
        this.mConnection = null;
    }

    public void disconnect() {
        Messenger messenger;
        Context context;
        AsyncChannelConnection asyncChannelConnection = this.mConnection;
        if (asyncChannelConnection != null && (context = this.mSrcContext) != null) {
            context.unbindService(asyncChannelConnection);
            this.mConnection = null;
        }
        try {
            Message messageObtain = Message.obtain();
            messageObtain.what = CMD_CHANNEL_DISCONNECTED;
            messageObtain.replyTo = this.mSrcMessenger;
            this.mDstMessenger.send(messageObtain);
        } catch (Exception unused) {
        }
        replyDisconnected(0);
        this.mSrcHandler = null;
        if (this.mConnection != null || (messenger = this.mDstMessenger) == null || this.mDeathMonitor == null) {
            return;
        }
        messenger.getBinder().unlinkToDeath(this.mDeathMonitor, 0);
        this.mDeathMonitor = null;
    }

    public void sendMessage(Message message) {
        message.replyTo = this.mSrcMessenger;
        try {
            this.mDstMessenger.send(message);
        } catch (RemoteException unused) {
            replyDisconnected(2);
        }
    }

    public void sendMessage(int i) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        sendMessage(messageObtain);
    }

    public void sendMessage(int i, int i2) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        sendMessage(messageObtain);
    }

    public void sendMessage(int i, int i2, int i3) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        messageObtain.arg2 = i3;
        sendMessage(messageObtain);
    }

    public void sendMessage(int i, int i2, int i3, Object obj) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        messageObtain.arg2 = i3;
        messageObtain.obj = obj;
        sendMessage(messageObtain);
    }

    public void sendMessage(int i, Object obj) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.obj = obj;
        sendMessage(messageObtain);
    }

    public void replyToMessage(Message message, Message message2) {
        try {
            message2.replyTo = this.mSrcMessenger;
            message.replyTo.send(message2);
        } catch (RemoteException e) {
            log("TODO: handle replyToMessage RemoteException" + e);
            e.printStackTrace();
        }
    }

    public void replyToMessage(Message message, int i) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        replyToMessage(message, messageObtain);
    }

    public void replyToMessage(Message message, int i, int i2) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        replyToMessage(message, messageObtain);
    }

    public void replyToMessage(Message message, int i, int i2, int i3) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        messageObtain.arg2 = i3;
        replyToMessage(message, messageObtain);
    }

    public void replyToMessage(Message message, int i, int i2, int i3, Object obj) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        messageObtain.arg2 = i3;
        messageObtain.obj = obj;
        replyToMessage(message, messageObtain);
    }

    public void replyToMessage(Message message, int i, Object obj) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.obj = obj;
        replyToMessage(message, messageObtain);
    }

    public Message sendMessageSynchronously(Message message) {
        return SyncMessenger.sendMessageSynchronously(this.mDstMessenger, message);
    }

    public Message sendMessageSynchronously(int i) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        return sendMessageSynchronously(messageObtain);
    }

    public Message sendMessageSynchronously(int i, int i2) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        return sendMessageSynchronously(messageObtain);
    }

    public Message sendMessageSynchronously(int i, int i2, int i3) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        messageObtain.arg2 = i3;
        return sendMessageSynchronously(messageObtain);
    }

    public Message sendMessageSynchronously(int i, int i2, int i3, Object obj) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        messageObtain.arg2 = i3;
        messageObtain.obj = obj;
        return sendMessageSynchronously(messageObtain);
    }

    public Message sendMessageSynchronously(int i, Object obj) {
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.obj = obj;
        return sendMessageSynchronously(messageObtain);
    }

    private static class SyncMessenger {
        private SyncHandler mHandler;
        private HandlerThread mHandlerThread;
        private Messenger mMessenger;
        private static Stack sStack = new Stack();
        private static int sCount = 0;

        private SyncMessenger() {
        }

        private class SyncHandler extends Handler {
            private Object mLockObject;
            private Message mResultMsg;

            private SyncHandler(SyncMessenger syncMessenger, Looper looper) {
                super(looper);
                this.mLockObject = new Object();
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                Message messageObtain = Message.obtain();
                messageObtain.copyFrom(message);
                synchronized (this.mLockObject) {
                    this.mResultMsg = messageObtain;
                    this.mLockObject.notify();
                }
            }
        }

        private static SyncMessenger obtain() {
            SyncMessenger syncMessenger;
            synchronized (sStack) {
                try {
                    if (sStack.isEmpty()) {
                        syncMessenger = new SyncMessenger();
                        StringBuilder sb = new StringBuilder("SyncHandler-");
                        int i = sCount;
                        sCount = i + 1;
                        sb.append(i);
                        HandlerThread handlerThread = new HandlerThread(sb.toString());
                        syncMessenger.mHandlerThread = handlerThread;
                        handlerThread.start();
                        syncMessenger.mHandler = new SyncHandler(syncMessenger.mHandlerThread.getLooper());
                        syncMessenger.mMessenger = new Messenger(syncMessenger.mHandler);
                    } else {
                        syncMessenger = (SyncMessenger) sStack.pop();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return syncMessenger;
        }

        private void recycle() {
            synchronized (sStack) {
                sStack.push(this);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:11:0x002a -> B:18:0x0048). Please report as a decompilation issue!!! */
        public static Message sendMessageSynchronously(Messenger messenger, Message message) throws Throwable {
            SyncMessenger syncMessengerObtain = obtain();
            Message message2 = null;
            if (messenger != null && message != null) {
                try {
                    message.replyTo = syncMessengerObtain.mMessenger;
                    synchronized (syncMessengerObtain.mHandler.mLockObject) {
                        try {
                            if (syncMessengerObtain.mHandler.mResultMsg != null) {
                                Log.wtf("AsyncChannel", "mResultMsg should be null here");
                                syncMessengerObtain.mHandler.mResultMsg = null;
                            }
                            messenger.send(message);
                            syncMessengerObtain.mHandler.mLockObject.wait();
                            Message message3 = syncMessengerObtain.mHandler.mResultMsg;
                            try {
                                syncMessengerObtain.mHandler.mResultMsg = null;
                                message2 = message3;
                            } catch (Throwable th) {
                                message2 = message3;
                                th = th;
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                } catch (RemoteException e) {
                    Log.e("AsyncChannel", "error in sendMessageSynchronously", e);
                } catch (InterruptedException e2) {
                    Log.e("AsyncChannel", "error in sendMessageSynchronously", e2);
                }
            }
            syncMessengerObtain.recycle();
            return message2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void replyHalfConnected(int i) {
        Message messageObtainMessage = this.mSrcHandler.obtainMessage(CMD_CHANNEL_HALF_CONNECTED);
        messageObtainMessage.arg1 = i;
        messageObtainMessage.obj = this;
        messageObtainMessage.replyTo = this.mDstMessenger;
        if (!linkToDeathMonitor()) {
            messageObtainMessage.arg1 = 1;
        }
        this.mSrcHandler.sendMessage(messageObtainMessage);
    }

    private boolean linkToDeathMonitor() {
        if (this.mConnection != null || this.mDeathMonitor != null) {
            return true;
        }
        this.mDeathMonitor = new DeathMonitor();
        try {
            this.mDstMessenger.getBinder().linkToDeath(this.mDeathMonitor, 0);
            return true;
        } catch (RemoteException unused) {
            this.mDeathMonitor = null;
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void replyDisconnected(int i) {
        Handler handler = this.mSrcHandler;
        if (handler == null) {
            return;
        }
        Message messageObtainMessage = handler.obtainMessage(CMD_CHANNEL_DISCONNECTED);
        messageObtainMessage.arg1 = i;
        messageObtainMessage.obj = this;
        messageObtainMessage.replyTo = this.mDstMessenger;
        this.mSrcHandler.sendMessage(messageObtainMessage);
    }

    class AsyncChannelConnection implements ServiceConnection {
        AsyncChannelConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AsyncChannel.this.mDstMessenger = new Messenger(iBinder);
            AsyncChannel.this.replyHalfConnected(0);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            AsyncChannel.this.replyDisconnected(0);
        }
    }

    private static void log(String str) {
        Log.d("AsyncChannel", str);
    }

    private final class DeathMonitor implements IBinder.DeathRecipient {
        DeathMonitor() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            AsyncChannel.this.replyDisconnected(4);
        }
    }
}
