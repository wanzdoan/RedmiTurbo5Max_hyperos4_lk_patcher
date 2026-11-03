package com.android.phone.eventhub.events;

import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public class BroadcastEvent extends BaseEvent {
    private final String mAction;

    public BroadcastEvent(String str, Bundle bundle) {
        super("broadcast", EventTypes$Broadcast.getSubTypeFromAction(str));
        this.mAction = str;
        this.mBundle = bundle == null ? new Bundle() : bundle;
    }
}
