package com.android.internal.telephony.metrics;

import android.os.SystemClock;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.nano.PersistAtomsProto$DataNetworkValidation;

/* JADX INFO: loaded from: classes.dex */
public class DataNetworkValidationStats {
    private final PersistAtomsStorage mAtomsStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
    private PersistAtomsProto$DataNetworkValidation mDataNetworkValidation;
    private final Phone mPhone;
    private long mRequestedTimeInMillis;

    public DataNetworkValidationStats(Phone phone) {
        this.mPhone = phone;
    }

    public void onRequestNetworkValidation(int i) {
        if (this.mDataNetworkValidation == null) {
            this.mDataNetworkValidation = getDefaultProto(i);
            this.mRequestedTimeInMillis = getTimeMillis();
        }
    }

    public void onHandoverAttempted() {
        PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation = this.mDataNetworkValidation;
        if (persistAtomsProto$DataNetworkValidation != null) {
            persistAtomsProto$DataNetworkValidation.handoverAttempted = true;
        }
    }

    public void onDataNetworkDisconnected(int i) {
        if (this.mDataNetworkValidation == null) {
            return;
        }
        calcElapsedTime();
        PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation = this.mDataNetworkValidation;
        persistAtomsProto$DataNetworkValidation.networkType = i;
        persistAtomsProto$DataNetworkValidation.signalStrength = this.mPhone.getSignalStrength().getLevel();
        PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation2 = this.mDataNetworkValidation;
        persistAtomsProto$DataNetworkValidation2.validationResult = 0;
        this.mAtomsStorage.addDataNetworkValidation(persistAtomsProto$DataNetworkValidation2);
        clear();
    }

    public void onUpdateNetworkValidationState(int i, int i2) {
        PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation = this.mDataNetworkValidation;
        if (persistAtomsProto$DataNetworkValidation == null) {
            return;
        }
        if (i == 0) {
            persistAtomsProto$DataNetworkValidation.validationResult = 1;
        } else {
            if (i == 1 || i == 2) {
                return;
            }
            if (i == 3 || i == 4) {
                persistAtomsProto$DataNetworkValidation.validationResult = i;
            }
        }
        calcElapsedTime();
        PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation2 = this.mDataNetworkValidation;
        persistAtomsProto$DataNetworkValidation2.networkType = i2;
        persistAtomsProto$DataNetworkValidation2.signalStrength = this.mPhone.getSignalStrength().getLevel();
        this.mAtomsStorage.addDataNetworkValidation(this.mDataNetworkValidation);
        clear();
    }

    private void calcElapsedTime() {
        PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation = this.mDataNetworkValidation;
        if (persistAtomsProto$DataNetworkValidation == null || this.mRequestedTimeInMillis == 0) {
            return;
        }
        persistAtomsProto$DataNetworkValidation.elapsedTimeInMillis = getTimeMillis() - this.mRequestedTimeInMillis;
    }

    protected long getTimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    private void clear() {
        this.mDataNetworkValidation = null;
        this.mRequestedTimeInMillis = 0L;
    }

    private PersistAtomsProto$DataNetworkValidation getDefaultProto(int i) {
        PersistAtomsProto$DataNetworkValidation persistAtomsProto$DataNetworkValidation = new PersistAtomsProto$DataNetworkValidation();
        persistAtomsProto$DataNetworkValidation.networkType = 0;
        persistAtomsProto$DataNetworkValidation.apnTypeBitmask = i;
        persistAtomsProto$DataNetworkValidation.signalStrength = 0;
        persistAtomsProto$DataNetworkValidation.validationResult = 0;
        persistAtomsProto$DataNetworkValidation.elapsedTimeInMillis = 0L;
        persistAtomsProto$DataNetworkValidation.handoverAttempted = false;
        persistAtomsProto$DataNetworkValidation.networkValidationCount = 1;
        return persistAtomsProto$DataNetworkValidation;
    }
}
