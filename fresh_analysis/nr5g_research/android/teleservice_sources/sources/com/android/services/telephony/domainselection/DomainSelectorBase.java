package com.android.services.telephony.domainselection;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.telephony.DomainSelectionService;
import android.telephony.DomainSelector;
import android.telephony.TransportSelectorCallback;
import android.telephony.WwanSelectorCallback;
import android.util.Log;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public abstract class DomainSelectorBase extends Handler implements DomainSelector {
    protected final Context mContext;
    private final DestroyListener mDestroyListener;
    protected final LocalLog mEventLog;
    protected final ImsStateTracker mImsStateTracker;
    private final String mLogTag;
    protected DomainSelectionService.SelectionAttributes mSelectionAttributes;
    private final int mSlotId;
    private final int mSubId;
    protected TransportSelectorCallback mTransportSelectorCallback;
    protected WwanSelectorCallback mWwanSelectorCallback;

    public interface DestroyListener {
        void onDomainSelectorDestroyed(DomainSelectorBase domainSelectorBase);
    }

    public abstract void selectDomain(DomainSelectionService.SelectionAttributes selectionAttributes, TransportSelectorCallback transportSelectorCallback);

    public DomainSelectorBase(Context context, int i, int i2, Looper looper, ImsStateTracker imsStateTracker, DestroyListener destroyListener, String str) {
        super(looper);
        this.mEventLog = new LocalLog(30);
        this.mContext = context;
        this.mImsStateTracker = imsStateTracker;
        this.mSlotId = i;
        this.mSubId = i2;
        this.mDestroyListener = destroyListener;
        this.mLogTag = str;
    }

    protected void destroy() {
        removeCallbacksAndMessages(null);
        notifyDomainSelectorDestroyed();
    }

    protected void notifyDomainSelectorDestroyed() {
        DestroyListener destroyListener = this.mDestroyListener;
        if (destroyListener != null) {
            destroyListener.onDomainSelectorDestroyed(this);
        }
    }

    protected int getSlotId() {
        return this.mSlotId;
    }

    protected int getSubId() {
        return this.mSubId;
    }

    protected void dump(PrintWriter printWriter) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println(this.mLogTag + ":");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("SlotId: " + getSlotId());
        androidUtilIndentingPrintWriter.println("SubId: " + getSubId());
        this.mEventLog.dump(androidUtilIndentingPrintWriter);
        androidUtilIndentingPrintWriter.decreaseIndent();
    }

    protected void logd(String str) {
        Log.d(this.mLogTag, "[" + getSlotId() + "|" + getSubId() + "] " + str);
    }

    protected void logi(String str) {
        Log.i(this.mLogTag, "[" + getSlotId() + "|" + getSubId() + "] " + str);
        this.mEventLog.log("[" + getSlotId() + "|" + getSubId() + "] " + str);
    }

    protected void loge(String str) {
        Log.e(this.mLogTag, "[" + getSlotId() + "|" + getSubId() + "] " + str);
        this.mEventLog.log("[" + getSlotId() + "|" + getSubId() + "] " + str);
    }
}
