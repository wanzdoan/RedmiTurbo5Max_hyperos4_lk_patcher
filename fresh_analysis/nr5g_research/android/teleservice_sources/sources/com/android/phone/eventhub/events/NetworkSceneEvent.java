package com.android.phone.eventhub.events;

import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public abstract class NetworkSceneEvent extends BaseEvent {
    public NetworkSceneEvent(String str) {
        super("network", str);
    }

    public class GameDelayEvent extends NetworkSceneEvent {
        public GameDelayEvent(String str, Bundle bundle) {
            super(str);
            this.mBundle = bundle;
        }
    }

    public class SelfRecoveryEvent extends NetworkSceneEvent {
        public SelfRecoveryEvent(String str, Bundle bundle) {
            super(str);
            this.mBundle = bundle;
        }
    }
}
