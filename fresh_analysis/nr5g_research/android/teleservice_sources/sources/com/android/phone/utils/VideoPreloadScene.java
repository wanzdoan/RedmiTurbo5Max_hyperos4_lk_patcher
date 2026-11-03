package com.android.phone.utils;

/* JADX INFO: loaded from: classes.dex */
public enum VideoPreloadScene {
    DUMMY(0),
    HST_WEAK_SIGNAL(1),
    SUBWAY_WEAK_SIGNAL(2),
    SUBWAY_SIGNAL_MODEL(3),
    AI_CONGESTION(4),
    SELF_STUDY(5),
    HST_SIGNAL_MAP(6),
    ELEVATOR(7),
    HST_VIDEO_QULITY(8);

    private final int value;

    VideoPreloadScene(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }
}
