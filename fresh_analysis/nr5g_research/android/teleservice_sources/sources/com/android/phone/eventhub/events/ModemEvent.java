package com.android.phone.eventhub.events;

import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemEvent extends BaseEvent {
    public ModemEvent(String str) {
        super("modem", str);
    }

    public class HydraEvent extends ModemEvent {
        public HydraEvent(String str, Bundle bundle) {
            super(str);
            this.mBundle = bundle;
        }
    }
}
