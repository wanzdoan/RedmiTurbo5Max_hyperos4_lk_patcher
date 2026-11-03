package com.android.phone;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.PhoneFactory;
import java.lang.ref.WeakReference;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class HighPriorityCellSelection {
    private static final String[] CLEAR_LTE_HIGH_PRIORITY_CELL = {"AT+EGMC=1,\"LTE_high_priority_cell\",0", ""};
    private static final String[] CLEAR_NR_HIGH_PRIORITY_CELL = {"AT+EGMC=1,\"NR_high_priority_cell\",0", ""};
    private static volatile HighPriorityCellSelection mHighPriorityCellSelection;
    private volatile HighPriorityCellSelectionHandler mHandler;
    private HandlerThread mWorkThread;
    private volatile boolean mDestroyed = false;
    private Context mContext = PhoneGlobals.getInstance();

    public class HighPriorityCellInfo {
        public final int freq;
        public final boolean isNrCell;
        public final short mcc;
        public final short mnc;
        public final boolean mncIncludesPcsDigit;
        public final short pci;

        public HighPriorityCellInfo(short s, short s2, boolean z, int i, short s3, boolean z2) {
            this.mcc = s;
            this.mnc = s2;
            this.mncIncludesPcsDigit = z;
            this.freq = i;
            this.pci = s3;
            this.isNrCell = z2;
        }
    }

    private HighPriorityCellSelection() {
        HandlerThread handlerThread = new HandlerThread("HighPriorityCellSelection", 10);
        this.mWorkThread = handlerThread;
        try {
            handlerThread.start();
            Looper looper = this.mWorkThread.getLooper();
            if (looper == null) {
                throw new IllegalStateException("Failed to initialize Looper");
            }
            this.mHandler = new HighPriorityCellSelectionHandler(looper, this);
        } catch (Exception unused) {
            HandlerThread handlerThread2 = this.mWorkThread;
            if (handlerThread2 != null) {
                handlerThread2.quitSafely();
                try {
                    this.mWorkThread.join(1000L);
                } catch (InterruptedException unused2) {
                    Thread.currentThread().interrupt();
                }
            }
            log("Failed to initialize HighPriorityCellSelection");
        }
    }

    public static HighPriorityCellSelection getInstance() {
        if (mHighPriorityCellSelection == null) {
            synchronized (HighPriorityCellSelection.class) {
                try {
                    if (mHighPriorityCellSelection == null) {
                        mHighPriorityCellSelection = new HighPriorityCellSelection();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mHighPriorityCellSelection;
    }

    class HighPriorityCellSelectionHandler extends Handler {
        private final WeakReference mOuter;

        HighPriorityCellSelectionHandler(Looper looper, HighPriorityCellSelection highPriorityCellSelection) {
            super(looper);
            this.mOuter = new WeakReference(highPriorityCellSelection);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            HighPriorityCellSelection highPriorityCellSelection = (HighPriorityCellSelection) this.mOuter.get();
            if (highPriorityCellSelection == null || highPriorityCellSelection.mDestroyed || message.what != 1) {
                return;
            }
            highPriorityCellSelection.clearHighPriorityCell(message.arg1);
        }
    }

    private void sendAtCommand(int i, String[] strArr, Message message) {
        if (this.mDestroyed || strArr == null) {
            log("sendAtCommand: cmd is null or object destroyed");
            if (message != null) {
                message.obj = "Error: ".concat(this.mDestroyed ? "Object destroyed" : "cmd is null");
                if (this.mHandler != null) {
                    this.mHandler.sendMessage(message);
                    return;
                }
                return;
            }
            return;
        }
        try {
            if (PhoneFactory.getPhone(i) != null) {
                FiveGManagerBase.getInstance().sendAtCommand(i, strArr, message);
                log("sendAtCommand: " + String.join(" ", strArr));
            }
        } catch (Exception e) {
            log("Error sending AT command: " + e.getMessage());
            if (message != null) {
                message.obj = "Error: " + e.getMessage();
                if (this.mHandler != null) {
                    this.mHandler.sendMessage(message);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearHighPriorityCell(int i) {
        sendAtCommand(i, CLEAR_LTE_HIGH_PRIORITY_CELL, null);
        sendAtCommand(i, CLEAR_NR_HIGH_PRIORITY_CELL, null);
        log("clearHighPriorityCell");
    }

    private void sendHighPriorityCellCommand(int i, HighPriorityCellInfo highPriorityCellInfo) {
        if (highPriorityCellInfo == null) {
            log("cellInfo is null or object destroyed");
            return;
        }
        String str = highPriorityCellInfo.isNrCell ? "AT+EGMC=1,\"NR_high_priority_cell\",1,\"" : "AT+EGMC=1,\"LTE_high_priority_cell\",1,\"";
        String[] strArr = {str + (formatPlmn(highPriorityCellInfo.mcc, highPriorityCellInfo.mnc, highPriorityCellInfo.mncIncludesPcsDigit) + "\"," + String.valueOf(highPriorityCellInfo.freq) + "," + String.valueOf((int) highPriorityCellInfo.pci)) + ",12", ""};
        sendAtCommand(i, strArr, null);
        StringBuilder sb = new StringBuilder("sendHighPriorityCellCommand");
        sb.append(strArr[0]);
        log(sb.toString());
    }

    private String formatPlmn(short s, short s2, boolean z) {
        return String.format("%03d", Short.valueOf(s)).concat(z ? String.format("%03d", Short.valueOf(s2)) : String.format("%02d", Short.valueOf(s2)));
    }

    public synchronized void setHighPriorityCell(int i, List list, long j) {
        if (shouldSkipSetHighPriorityCell(i, list, j)) {
            return;
        }
        handleExpiryMessage(i, j);
        sendAllHighPriorityCellCommands(i, list);
    }

    private boolean shouldSkipSetHighPriorityCell(int i, List list, long j) {
        if (this.mDestroyed || this.mHandler == null) {
            log("mHandler is null or object destroyed");
            return true;
        }
        if (list == null || list.isEmpty() || list.size() > 64) {
            log("setHighPriorityCell, cellInfoS is null or empty");
            return true;
        }
        if (j <= 0) {
            log("setHighPriorityCell, expiryTime is invalid");
            return true;
        }
        if (i < 0 || i > 1) {
            log("invalid slot");
            return true;
        }
        for (Object obj : list) {
            if (obj == null || !(obj instanceof HighPriorityCellInfo)) {
                log("setHighPriorityCell, invalid element type");
                return true;
            }
        }
        return false;
    }

    private void sendAllHighPriorityCellCommands(int i, List list) {
        for (Object obj : list) {
            if (obj instanceof HighPriorityCellInfo) {
                sendHighPriorityCellCommand(i, (HighPriorityCellInfo) obj);
            }
        }
    }

    private void handleExpiryMessage(int i, long j) {
        if (this.mHandler == null) {
            return;
        }
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
            clearHighPriorityCell(i);
        }
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, i, 0), j * 1000);
    }

    private void log(String str) {
        Log.d("HighPriorityCellSelection", str);
    }
}
