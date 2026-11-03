package com.android.phone.eventhub.events;

import android.os.Bundle;
import com.android.phone.FiveGServiceInfo;

/* JADX INFO: loaded from: classes.dex */
public abstract class TelephonyEvent extends BaseEvent {
    public TelephonyEvent(String str) {
        super("telephony", str);
    }

    public TelephonyEvent(String str, boolean z) {
        super("telephony", str, z);
    }

    public class FiveGEvent extends TelephonyEvent {
        private final FiveGServiceInfo mFiveGServiceInfo;

        public FiveGEvent(String str, FiveGServiceInfo fiveGServiceInfo, Bundle bundle) {
            super(str);
            this.mFiveGServiceInfo = fiveGServiceInfo;
            this.mBundle = bundle;
        }

        public FiveGServiceInfo getFiveGServiceInfo() {
            return this.mFiveGServiceInfo;
        }
    }

    public class DsdaEvent extends TelephonyEvent {
        private boolean mIsDsdaEnabled;
        private int mOperationCode;

        public DsdaEvent(String str, boolean z, int i) {
            super(str);
            this.mIsDsdaEnabled = z;
            this.mOperationCode = i;
        }

        public int getDsdaOpCode() {
            return this.mOperationCode;
        }

        public boolean isDsdaEnabled() {
            return this.mIsDsdaEnabled;
        }
    }

    public class DataEvent extends TelephonyEvent {
        public DataEvent(String str, Bundle bundle) {
            super(str);
            this.mBundle = bundle;
        }
    }

    public class NetworkEvent extends TelephonyEvent {
        public NetworkEvent(String str, Bundle bundle) {
            super(str);
            this.mBundle = bundle;
        }
    }

    public class PowerEvent extends TelephonyEvent {
        public PowerEvent(String str, Bundle bundle) {
            super(str);
            this.mBundle = bundle;
        }
    }

    public class RilEvent extends TelephonyEvent {
        private boolean mIsReady;

        public RilEvent(String str, boolean z) {
            super(str, true);
            this.mIsReady = z;
        }

        public boolean isRilReady() {
            return this.mIsReady;
        }
    }

    public class SimEvent extends TelephonyEvent {
        public SimEvent(String str, Bundle bundle) {
            super(str);
            this.mBundle = bundle;
        }
    }

    public class CloudEvent extends TelephonyEvent {
        public CloudEvent(String str, Bundle bundle) {
            super(str);
            this.mBundle = bundle;
        }
    }
}
