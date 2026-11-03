package com.android.cellbroadcastservice;

import android.util.StatsEvent;
import android.util.StatsLog;

/* JADX INFO: loaded from: classes.dex */
public abstract class CellBroadcastStatsLog {
    public static void write(int i, int i2, int i3) {
        StatsEvent.Builder builderNewBuilder = StatsEvent.newBuilder();
        builderNewBuilder.setAtomId(i);
        builderNewBuilder.writeInt(i2);
        builderNewBuilder.writeInt(i3);
        builderNewBuilder.usePooledBuffer();
        StatsLog.write(builderNewBuilder.build());
    }

    public static void write(int i, int i2, String str) {
        StatsEvent.Builder builderNewBuilder = StatsEvent.newBuilder();
        builderNewBuilder.setAtomId(i);
        builderNewBuilder.writeInt(i2);
        builderNewBuilder.writeString(str);
        builderNewBuilder.usePooledBuffer();
        StatsLog.write(builderNewBuilder.build());
    }
}
