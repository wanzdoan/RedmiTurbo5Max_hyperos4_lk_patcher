package com.android.phone;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class QmsMsgTunnelControllerBase {
    protected static volatile QmsMsgTunnelControllerBase mInstance;
    protected static final HashMap sLocalLogs = new HashMap();
    protected String mTriggerReason = "";
    protected boolean mHasRegQms = false;
    protected int mInsertedSimCn = 0;

    public abstract void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract boolean sendQmsRequestRawAsync(int i, int i2, Object obj, String str);

    public static QmsMsgTunnelControllerBase getInstance() {
        return mInstance;
    }

    public static boolean sendQmsRequestSafe(int i, int i2, Object obj, String str) {
        QmsMsgTunnelControllerBase qmsMsgTunnelControllerBase = getInstance();
        if (qmsMsgTunnelControllerBase == null) {
            return false;
        }
        return qmsMsgTunnelControllerBase.sendQmsRequestRawAsync(i, i2, obj, str);
    }

    public final class MessageRequestAsync {
        public Object obj;
        public int requestId;
        public int slotId;
        public String triggerReason;

        public MessageRequestAsync(int i, int i2, Object obj, String str) {
            this.requestId = i;
            this.slotId = i2;
            this.obj = obj;
            this.triggerReason = str;
        }
    }
}
