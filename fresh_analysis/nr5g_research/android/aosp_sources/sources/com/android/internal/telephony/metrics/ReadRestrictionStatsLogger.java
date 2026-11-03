package com.android.internal.telephony.metrics;

import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.nano.PersistAtomsProto$MessagingReadRestrictionEvent;
import com.android.telephony.Rlog;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public class ReadRestrictionStatsLogger {
    private static ReadRestrictionStatsLogger sInstance;
    private Supplier mAtomsStorage = new Supplier<PersistAtomsStorage>(this) { // from class: com.android.internal.telephony.metrics.ReadRestrictionStatsLogger.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public PersistAtomsStorage get() {
            MetricsCollector metricsCollector = PhoneFactory.getMetricsCollector();
            if (metricsCollector == null) {
                Rlog.w("ReadRestrictionStatsLogger", "MetricsCollector is not available. Skipping logging.");
                return null;
            }
            return metricsCollector.getAtomsStorage();
        }
    };

    public enum ContentProvider {
        SMS,
        MMS
    }

    public static ReadRestrictionStatsLogger getInstance() {
        if (sInstance == null) {
            synchronized (ReadRestrictionStatsLogger.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new ReadRestrictionStatsLogger();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private ReadRestrictionStatsLogger() {
    }

    public void onMessageInserted(ContentProvider contentProvider, int i, boolean z) {
        PersistAtomsStorage persistAtomsStorage = (PersistAtomsStorage) this.mAtomsStorage.get();
        if (persistAtomsStorage == null) {
            return;
        }
        PersistAtomsProto$MessagingReadRestrictionEvent persistAtomsProto$MessagingReadRestrictionEvent = new PersistAtomsProto$MessagingReadRestrictionEvent();
        persistAtomsProto$MessagingReadRestrictionEvent.callerUid = i;
        persistAtomsProto$MessagingReadRestrictionEvent.contentProvider = getContentProviderId(contentProvider);
        if (z) {
            persistAtomsProto$MessagingReadRestrictionEvent.eventType = 1;
        } else {
            persistAtomsProto$MessagingReadRestrictionEvent.eventType = 2;
        }
        persistAtomsStorage.addMessagingReadRestrictionEvent(persistAtomsProto$MessagingReadRestrictionEvent);
    }

    public void onMessageUnrestricted(ContentProvider contentProvider, int i) {
        PersistAtomsStorage persistAtomsStorage = (PersistAtomsStorage) this.mAtomsStorage.get();
        if (persistAtomsStorage == null) {
            return;
        }
        PersistAtomsProto$MessagingReadRestrictionEvent persistAtomsProto$MessagingReadRestrictionEvent = new PersistAtomsProto$MessagingReadRestrictionEvent();
        persistAtomsProto$MessagingReadRestrictionEvent.callerUid = i;
        persistAtomsProto$MessagingReadRestrictionEvent.contentProvider = getContentProviderId(contentProvider);
        persistAtomsProto$MessagingReadRestrictionEvent.eventType = 3;
        persistAtomsStorage.addMessagingReadRestrictionEvent(persistAtomsProto$MessagingReadRestrictionEvent);
    }

    public void onRestrictedMessagesQueried(ContentProvider contentProvider, int i, boolean z) {
        PersistAtomsStorage persistAtomsStorage = (PersistAtomsStorage) this.mAtomsStorage.get();
        if (persistAtomsStorage == null) {
            return;
        }
        PersistAtomsProto$MessagingReadRestrictionEvent persistAtomsProto$MessagingReadRestrictionEvent = new PersistAtomsProto$MessagingReadRestrictionEvent();
        persistAtomsProto$MessagingReadRestrictionEvent.callerUid = i;
        persistAtomsProto$MessagingReadRestrictionEvent.contentProvider = getContentProviderId(contentProvider);
        persistAtomsProto$MessagingReadRestrictionEvent.eventType = 4;
        persistAtomsProto$MessagingReadRestrictionEvent.readRestrictedMessagesAppOpMode = z ? 1 : 2;
        persistAtomsStorage.addMessagingReadRestrictionEvent(persistAtomsProto$MessagingReadRestrictionEvent);
    }

    private int getContentProviderId(ContentProvider contentProvider) {
        int iOrdinal = contentProvider.ordinal();
        if (iOrdinal == 0) {
            return 1;
        }
        if (iOrdinal == 1) {
            return 2;
        }
        throw new RuntimeException(null, null);
    }

    public static void setInstance(ReadRestrictionStatsLogger readRestrictionStatsLogger) {
        sInstance = readRestrictionStatsLogger;
    }
}
