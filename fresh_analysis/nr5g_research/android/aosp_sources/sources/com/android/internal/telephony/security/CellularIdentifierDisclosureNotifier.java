package com.android.internal.telephony.security;

import android.content.Context;
import android.telephony.CellularIdentifierDisclosure;
import com.android.internal.telephony.metrics.CellularSecurityTransparencyStats;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.telephony.Rlog;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class CellularIdentifierDisclosureNotifier {
    private static CellularIdentifierDisclosureNotifier sInstance;
    private CellularSecurityTransparencyStats mCellularSecurityTransparencyStats;
    private boolean mEnabled;
    private final Object mEnabledLock;
    private final CellularNetworkSecuritySafetySource mSafetySource;
    private ScheduledExecutorService mSerializedWorkQueue;
    private SubscriptionManagerService mSubscriptionManagerService;
    private final long mWindowCloseDuration;
    private final TimeUnit mWindowCloseUnit;
    private Map mWindows;

    public CellularIdentifierDisclosureNotifier(CellularNetworkSecuritySafetySource cellularNetworkSecuritySafetySource) {
        this(Executors.newSingleThreadScheduledExecutor(), 15L, TimeUnit.MINUTES, cellularNetworkSecuritySafetySource, SubscriptionManagerService.getInstance(), new CellularSecurityTransparencyStats());
    }

    public CellularIdentifierDisclosureNotifier(ScheduledExecutorService scheduledExecutorService, long j, TimeUnit timeUnit, CellularNetworkSecuritySafetySource cellularNetworkSecuritySafetySource, SubscriptionManagerService subscriptionManagerService, CellularSecurityTransparencyStats cellularSecurityTransparencyStats) {
        this.mEnabledLock = new Object();
        this.mEnabled = false;
        this.mSerializedWorkQueue = scheduledExecutorService;
        this.mWindowCloseDuration = j;
        this.mWindowCloseUnit = timeUnit;
        this.mWindows = new HashMap();
        this.mSafetySource = cellularNetworkSecuritySafetySource;
        this.mSubscriptionManagerService = subscriptionManagerService;
        this.mCellularSecurityTransparencyStats = cellularSecurityTransparencyStats;
    }

    public void addDisclosure(Context context, int i, CellularIdentifierDisclosure cellularIdentifierDisclosure) {
        Rlog.d("CellularIdentifierDisclosureNotifier", "Identifier disclosure reported: " + cellularIdentifierDisclosure);
        logDisclosure(i, cellularIdentifierDisclosure);
        synchronized (this.mEnabledLock) {
            try {
                if (!this.mEnabled) {
                    Rlog.d("CellularIdentifierDisclosureNotifier", "Skipping disclosure because notifier was disabled.");
                    return;
                }
                if (cellularIdentifierDisclosure.isEmergency()) {
                    Rlog.i("CellularIdentifierDisclosureNotifier", "Ignoring identifier disclosure associated with an emergency.");
                    return;
                }
                if (cellularIdentifierDisclosure.isBenign()) {
                    Rlog.i("CellularIdentifierDisclosureNotifier", "Ignoring identifier disclosure that is claimed to be benign.");
                    return;
                }
                try {
                    this.mSerializedWorkQueue.execute(incrementAndNotify(context, i, cellularIdentifierDisclosure));
                } catch (RejectedExecutionException e) {
                    Rlog.e("CellularIdentifierDisclosureNotifier", "Failed to schedule incrementAndNotify: " + e.getMessage());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void logDisclosure(int i, CellularIdentifierDisclosure cellularIdentifierDisclosure) {
        try {
            this.mSerializedWorkQueue.execute(runLogDisclosure(i, cellularIdentifierDisclosure));
        } catch (RejectedExecutionException e) {
            Rlog.e("CellularIdentifierDisclosureNotifier", "Failed to schedule runLogDisclosure: " + e.getMessage());
        }
    }

    private Runnable runLogDisclosure(final int i, final CellularIdentifierDisclosure cellularIdentifierDisclosure) {
        return new Runnable() { // from class: com.android.internal.telephony.security.CellularIdentifierDisclosureNotifier$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$runLogDisclosure$0(i, cellularIdentifierDisclosure);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runLogDisclosure$0(int i, CellularIdentifierDisclosure cellularIdentifierDisclosure) {
        String mcc;
        String mnc;
        SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(i);
        if (subscriptionInfoInternal != null) {
            mcc = subscriptionInfoInternal.getMcc();
            mnc = subscriptionInfoInternal.getMnc();
        } else {
            mcc = null;
            mnc = null;
        }
        this.mCellularSecurityTransparencyStats.logIdentifierDisclosure(cellularIdentifierDisclosure, mcc, mnc, isEnabled());
    }

    public void enable(Context context) {
        synchronized (this.mEnabledLock) {
            Rlog.d("CellularIdentifierDisclosureNotifier", "enabled");
            this.mEnabled = true;
            try {
                this.mSerializedWorkQueue.execute(onEnableNotifier(context));
            } catch (RejectedExecutionException e) {
                Rlog.e("CellularIdentifierDisclosureNotifier", "Failed to schedule onEnableNotifier: " + e.getMessage());
            }
        }
    }

    public void disable(Context context) {
        Rlog.d("CellularIdentifierDisclosureNotifier", "disabled");
        synchronized (this.mEnabledLock) {
            this.mEnabled = false;
            try {
                this.mSerializedWorkQueue.execute(onDisableNotifier(context));
            } catch (RejectedExecutionException e) {
                Rlog.e("CellularIdentifierDisclosureNotifier", "Failed to schedule onDisableNotifier: " + e.getMessage());
            }
        }
    }

    public boolean isEnabled() {
        boolean z;
        synchronized (this.mEnabledLock) {
            z = this.mEnabled;
        }
        return z;
    }

    public static synchronized CellularIdentifierDisclosureNotifier getInstance(CellularNetworkSecuritySafetySource cellularNetworkSecuritySafetySource) {
        try {
            if (sInstance == null) {
                sInstance = new CellularIdentifierDisclosureNotifier(cellularNetworkSecuritySafetySource);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private Runnable incrementAndNotify(final Context context, final int i, final CellularIdentifierDisclosure cellularIdentifierDisclosure) {
        return new Runnable() { // from class: com.android.internal.telephony.security.CellularIdentifierDisclosureNotifier$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$incrementAndNotify$1(i, context, cellularIdentifierDisclosure);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$incrementAndNotify$1(int i, Context context, CellularIdentifierDisclosure cellularIdentifierDisclosure) {
        DisclosureWindow disclosureWindow = (DisclosureWindow) this.mWindows.get(Integer.valueOf(i));
        if (disclosureWindow == null) {
            disclosureWindow = new DisclosureWindow(i);
            this.mWindows.put(Integer.valueOf(i), disclosureWindow);
        }
        disclosureWindow.increment(context, this);
        int disclosureCount = disclosureWindow.getDisclosureCount();
        Rlog.d("CellularIdentifierDisclosureNotifier", "Emitting notification for subId: " + i + ". New disclosure count " + disclosureCount);
        this.mSafetySource.setIdentifierDisclosure(context, i, cellularIdentifierDisclosure, disclosureCount, disclosureWindow.getFirstOpen(), disclosureWindow.getCurrentEnd());
    }

    private Runnable onDisableNotifier(final Context context) {
        return new Runnable() { // from class: com.android.internal.telephony.security.CellularIdentifierDisclosureNotifier$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onDisableNotifier$2(context);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisableNotifier$2(Context context) {
        Rlog.d("CellularIdentifierDisclosureNotifier", "On disable notifier");
        Iterator it = this.mWindows.values().iterator();
        while (it.hasNext()) {
            ((DisclosureWindow) it.next()).close();
        }
        this.mSafetySource.setIdentifierDisclosureIssueEnabled(context, false);
    }

    private Runnable onEnableNotifier(final Context context) {
        return new Runnable() { // from class: com.android.internal.telephony.security.CellularIdentifierDisclosureNotifier$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onEnableNotifier$3(context);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnableNotifier$3(Context context) {
        Rlog.i("CellularIdentifierDisclosureNotifier", "On enable notifier");
        this.mSafetySource.setIdentifierDisclosureIssueEnabled(context, true);
    }

    public int getCurrentDisclosureCount(int i) {
        DisclosureWindow disclosureWindow = (DisclosureWindow) this.mWindows.get(Integer.valueOf(i));
        if (disclosureWindow != null) {
            return disclosureWindow.getDisclosureCount();
        }
        return 0;
    }

    public Instant getFirstOpen(int i) {
        DisclosureWindow disclosureWindow = (DisclosureWindow) this.mWindows.get(Integer.valueOf(i));
        if (disclosureWindow != null) {
            return disclosureWindow.getFirstOpen();
        }
        return null;
    }

    public Instant getCurrentEnd(int i) {
        DisclosureWindow disclosureWindow = (DisclosureWindow) this.mWindows.get(Integer.valueOf(i));
        if (disclosureWindow != null) {
            return disclosureWindow.getCurrentEnd();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DisclosureWindow {
        private int mSubId;
        private int mDisclosureCount = 0;
        private Instant mWindowFirstOpen = null;
        private Instant mLastEvent = null;
        private ScheduledFuture mWhenWindowCloses = null;

        DisclosureWindow(int i) {
            this.mSubId = i;
        }

        void increment(Context context, CellularIdentifierDisclosureNotifier cellularIdentifierDisclosureNotifier) {
            this.mDisclosureCount++;
            Instant instantNow = Instant.now();
            if (this.mDisclosureCount == 1) {
                this.mWindowFirstOpen = instantNow;
            }
            this.mLastEvent = instantNow;
            cancelWindowCloseFuture();
            try {
                this.mWhenWindowCloses = cellularIdentifierDisclosureNotifier.mSerializedWorkQueue.schedule(closeWindowRunnable(context), cellularIdentifierDisclosureNotifier.mWindowCloseDuration, cellularIdentifierDisclosureNotifier.mWindowCloseUnit);
            } catch (RejectedExecutionException e) {
                Rlog.e("CellularIdentifierDisclosureNotifier", "Failed to schedule closeWindow for subId " + this.mSubId + " :  " + e.getMessage());
            }
        }

        int getDisclosureCount() {
            return this.mDisclosureCount;
        }

        Instant getFirstOpen() {
            return this.mWindowFirstOpen;
        }

        Instant getCurrentEnd() {
            return this.mLastEvent;
        }

        void close() {
            this.mDisclosureCount = 0;
            this.mWindowFirstOpen = null;
            this.mLastEvent = null;
            if (this.mWhenWindowCloses == null) {
                return;
            }
            this.mWhenWindowCloses = null;
        }

        private Runnable closeWindowRunnable(final Context context) {
            return new Runnable() { // from class: com.android.internal.telephony.security.CellularIdentifierDisclosureNotifier$DisclosureWindow$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$closeWindowRunnable$0(context);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$closeWindowRunnable$0(Context context) {
            Rlog.i("CellularIdentifierDisclosureNotifier", "Disclosure window closing for subId " + this.mSubId + ". Disclosure count was " + getDisclosureCount());
            close();
            CellularIdentifierDisclosureNotifier.this.mSafetySource.clearIdentifierDisclosure(context, this.mSubId);
        }

        private boolean cancelWindowCloseFuture() {
            ScheduledFuture scheduledFuture = this.mWhenWindowCloses;
            if (scheduledFuture == null) {
                return false;
            }
            return scheduledFuture.cancel(false);
        }
    }
}
