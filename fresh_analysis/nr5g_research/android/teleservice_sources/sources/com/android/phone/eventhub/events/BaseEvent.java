package com.android.phone.eventhub.events;

import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseEvent {
    protected Bundle mBundle;
    private final boolean mIsSticky;
    private final String mSubType;
    private final long mTimestamp;
    private final String mType;

    public interface IEventListener {
        void onEvent(BaseEvent baseEvent);
    }

    public BaseEvent(String str, String str2) {
        this(str, str2, false);
    }

    public BaseEvent(String str, String str2, boolean z) {
        this.mType = str;
        this.mSubType = str2;
        this.mTimestamp = System.currentTimeMillis();
        this.mIsSticky = z;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public String getType() {
        return this.mType;
    }

    public String getSubType() {
        return this.mSubType;
    }

    public boolean isSticky() {
        return this.mIsSticky;
    }

    public String toString() {
        return "Event{type='" + this.mType + "', subType='" + this.mSubType + "', timestamp=" + this.mTimestamp + ", isSticky='" + this.mIsSticky + "'}";
    }
}
