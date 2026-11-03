package com.android.internal.telephony.security;

import android.content.Context;
import android.telephony.SecurityAlgorithmUpdate;
import com.android.telephony.Rlog;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class NullCipherNotifier {
    private static NullCipherNotifier sInstance;
    private final CellularNetworkSecuritySafetySource mSafetySource;
    private ScheduledExecutorService mSerializedWorkQueue;
    private final HashMap mSubscriptionState = new HashMap();
    private final HashMap mActiveSubscriptions = new HashMap();
    private final Object mEnabledLock = new Object();
    private boolean mEnabled = false;

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isNullCipher(int i) {
        return i == 0 || i == 14 || i == 29 || i == 41 || i == 55 || i == 86 || i == 67 || i == 68;
    }

    public static synchronized NullCipherNotifier getInstance(CellularNetworkSecuritySafetySource cellularNetworkSecuritySafetySource) {
        try {
            if (sInstance == null) {
                sInstance = new NullCipherNotifier(Executors.newSingleThreadScheduledExecutor(), cellularNetworkSecuritySafetySource);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public NullCipherNotifier(ScheduledExecutorService scheduledExecutorService, CellularNetworkSecuritySafetySource cellularNetworkSecuritySafetySource) {
        this.mSerializedWorkQueue = scheduledExecutorService;
        this.mSafetySource = cellularNetworkSecuritySafetySource;
    }

    public void onSecurityAlgorithmUpdate(final Context context, final int i, final int i2, final SecurityAlgorithmUpdate securityAlgorithmUpdate) {
        Rlog.d("NullCipherNotifier", "Security algorithm update: subId = " + i2 + " " + securityAlgorithmUpdate);
        if (shouldIgnoreUpdate(securityAlgorithmUpdate)) {
            return;
        }
        if (!isEnabled()) {
            Rlog.i("NullCipherNotifier", "Ignoring onSecurityAlgorithmUpdate. Notifier is disabled.");
            return;
        }
        try {
            this.mSerializedWorkQueue.execute(new Runnable() { // from class: com.android.internal.telephony.security.NullCipherNotifier$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSecurityAlgorithmUpdate$0(context, i, i2, securityAlgorithmUpdate);
                }
            });
        } catch (RejectedExecutionException e) {
            Rlog.e("NullCipherNotifier", "Failed to schedule onSecurityAlgorithmUpdate: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSecurityAlgorithmUpdate$0(Context context, int i, int i2, SecurityAlgorithmUpdate securityAlgorithmUpdate) {
        try {
            maybeUpdateSubscriptionMapping(context, i, i2);
            SubscriptionState subscriptionState = (SubscriptionState) this.mSubscriptionState.get(Integer.valueOf(i2));
            if (subscriptionState == null) {
                subscriptionState = new SubscriptionState();
                this.mSubscriptionState.put(Integer.valueOf(i2), subscriptionState);
            }
            this.mSafetySource.setNullCipherState(context, i2, subscriptionState.update(securityAlgorithmUpdate));
        } catch (Throwable th) {
            Rlog.e("NullCipherNotifier", "Failed to execute onSecurityAlgorithmUpdate " + th.getMessage());
        }
    }

    public void setSubscriptionMapping(final Context context, final int i, final int i2) {
        if (!isEnabled()) {
            Rlog.i("NullCipherNotifier", "Ignoring setSubscriptionMapping. Notifier is disabled.");
        }
        try {
            this.mSerializedWorkQueue.execute(new Runnable() { // from class: com.android.internal.telephony.security.NullCipherNotifier$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setSubscriptionMapping$1(context, i, i2);
                }
            });
        } catch (RejectedExecutionException e) {
            Rlog.e("NullCipherNotifier", "Failed to schedule setSubscriptionMapping: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setSubscriptionMapping$1(Context context, int i, int i2) {
        try {
            maybeUpdateSubscriptionMapping(context, i, i2);
        } catch (Throwable th) {
            Rlog.e("NullCipherNotifier", "Failed to update subId mapping. phoneId: " + i + " subId: " + i2 + ". " + th.getMessage());
        }
    }

    private void maybeUpdateSubscriptionMapping(Context context, int i, int i2) {
        Integer num = (Integer) this.mActiveSubscriptions.put(Integer.valueOf(i), Integer.valueOf(i2));
        if (num == null || num.intValue() == i2) {
            return;
        }
        this.mSubscriptionState.remove(num);
        this.mSafetySource.clearNullCipherState(context, num.intValue());
    }

    public void enable(Context context) {
        synchronized (this.mEnabledLock) {
            Rlog.d("NullCipherNotifier", "enabled");
            this.mEnabled = true;
            scheduleOnEnabled(context, true);
        }
    }

    public void disable(Context context) {
        synchronized (this.mEnabledLock) {
            Rlog.d("NullCipherNotifier", "disabled");
            this.mEnabled = false;
            scheduleOnEnabled(context, false);
        }
    }

    public boolean isEnabled() {
        boolean z;
        synchronized (this.mEnabledLock) {
            z = this.mEnabled;
        }
        return z;
    }

    private void scheduleOnEnabled(final Context context, final boolean z) {
        try {
            this.mSerializedWorkQueue.execute(new Runnable() { // from class: com.android.internal.telephony.security.NullCipherNotifier$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleOnEnabled$2(z, context);
                }
            });
        } catch (RejectedExecutionException e) {
            Rlog.e("NullCipherNotifier", "Failed to schedule onEnableNotifier: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleOnEnabled$2(boolean z, Context context) {
        Rlog.i("NullCipherNotifier", "On enable notifier. Enable value: " + z);
        this.mSafetySource.setNullCipherIssueEnabled(context, z);
    }

    private static boolean shouldIgnoreUpdate(SecurityAlgorithmUpdate securityAlgorithmUpdate) {
        if (securityAlgorithmUpdate.isUnprotectedEmergency()) {
            return true;
        }
        switch (securityAlgorithmUpdate.getConnectionEvent()) {
            case 6:
            case 7:
            case 8:
            case 9:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
                return true;
            case 10:
            case 11:
            case 17:
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SubscriptionState {
        private int mActiveNetworkClass;
        private final HashMap mState;

        private static int getNetworkClass(int i) {
            if (i == 0 || i == 1) {
                return 2;
            }
            if (i == 2 || i == 3) {
                return 3;
            }
            if (i == 4 || i == 5) {
                return 4;
            }
            return (i == 10 || i == 11) ? 5 : 0;
        }

        private SubscriptionState() {
            this.mActiveNetworkClass = 0;
            this.mState = new HashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int update(SecurityAlgorithmUpdate securityAlgorithmUpdate) {
            int encryption;
            int integrity;
            boolean zHasNullCipher = hasNullCipher();
            int networkClass = getNetworkClass(securityAlgorithmUpdate.getConnectionEvent());
            if (networkClass != this.mActiveNetworkClass || networkClass == 0) {
                this.mState.clear();
                this.mActiveNetworkClass = networkClass;
            }
            ConnectionState connectionState = (ConnectionState) this.mState.getOrDefault(Integer.valueOf(securityAlgorithmUpdate.getConnectionEvent()), ConnectionState.UNKNOWN);
            if (securityAlgorithmUpdate.getEncryption() == 113) {
                encryption = connectionState.getEncryption();
            } else {
                encryption = securityAlgorithmUpdate.getEncryption();
            }
            if (securityAlgorithmUpdate.getIntegrity() == 113) {
                integrity = connectionState.getIntegrity();
            } else {
                integrity = securityAlgorithmUpdate.getIntegrity();
            }
            this.mState.put(Integer.valueOf(securityAlgorithmUpdate.getConnectionEvent()), new ConnectionState(encryption, integrity));
            if (hasNullCipher()) {
                return 2;
            }
            return (!zHasNullCipher || this.mActiveNetworkClass == 0) ? 0 : 1;
        }

        private boolean hasNullCipher() {
            return this.mState.values().stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.security.NullCipherNotifier$SubscriptionState$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((NullCipherNotifier.ConnectionState) obj).hasNullCipher();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class ConnectionState {
        private static final ConnectionState UNKNOWN = new ConnectionState(113, 113);
        private final int mEncryption;
        private final int mIntegrity;

        private ConnectionState(int i, int i2) {
            this.mEncryption = i;
            this.mIntegrity = i2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getEncryption() {
            return this.mEncryption;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getIntegrity() {
            return this.mIntegrity;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasNullCipher() {
            return NullCipherNotifier.isNullCipher(this.mEncryption) || NullCipherNotifier.isNullCipher(this.mIntegrity);
        }
    }
}
