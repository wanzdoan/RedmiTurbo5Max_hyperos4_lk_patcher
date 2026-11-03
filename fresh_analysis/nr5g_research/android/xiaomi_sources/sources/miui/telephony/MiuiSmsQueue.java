package miui.telephony;

import android.telephony.Rlog;
import com.android.internal.telephony.MiuiSmsControllerImpl;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsDispatchersController;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSmsQueue {
    private static final String TAG = "SMSDispatcher";
    private SMSDispatcher mDispatcher;
    private SmsDispatchersController mDispatcherController;
    private long mLastTimeMills;
    private MiuiSmsControllerImpl mMiuiSmsImpl;
    private boolean mIsSynced = false;
    private LinkedList<SMSDispatcher.SmsTracker> mQueue = new LinkedList<>();

    public MiuiSmsQueue(SMSDispatcher dispatcher, MiuiSmsControllerImpl miuisms) {
        this.mLastTimeMills = 0L;
        this.mDispatcher = dispatcher;
        this.mLastTimeMills = 0L;
        this.mMiuiSmsImpl = miuisms;
    }

    public MiuiSmsQueue(SmsDispatchersController controller, MiuiSmsControllerImpl miuisms) {
        this.mLastTimeMills = 0L;
        this.mDispatcherController = controller;
        this.mLastTimeMills = 0L;
        this.mMiuiSmsImpl = miuisms;
    }

    public void enableSynced(boolean value) {
        this.mIsSynced = value;
        this.mLastTimeMills = 0L;
        if (value) {
            this.mLastTimeMills = System.currentTimeMillis();
        }
    }

    public void processNextSms() {
        synchronized (this.mQueue) {
            if (this.mQueue.size() > 0) {
                enableSynced(true);
                SMSDispatcher.SmsTracker tracker = this.mQueue.poll();
                if (this.mDispatcher != null) {
                    this.mMiuiSmsImpl.sendSms(this.mDispatcher, tracker);
                } else {
                    this.mDispatcherController.sendRetrySms(tracker);
                }
            } else {
                enableSynced(false);
            }
        }
    }

    public void enqueueOrSendSms(SMSDispatcher.SmsTracker tracker) {
        synchronized (this.mQueue) {
            this.mQueue.add(tracker);
            Rlog.d(TAG, "Added message to queue. Queue size is " + this.mQueue.size());
            if (this.mQueue.size() >= 1) {
                long time = System.currentTimeMillis() - this.mLastTimeMills;
                if (!this.mIsSynced || time > 10000 || time <= 0) {
                    enableSynced(true);
                    this.mQueue.poll();
                    if (this.mDispatcher != null) {
                        this.mMiuiSmsImpl.sendSms(this.mDispatcher, tracker);
                    } else {
                        this.mDispatcherController.sendRetrySms(tracker);
                    }
                }
            }
        }
    }
}
