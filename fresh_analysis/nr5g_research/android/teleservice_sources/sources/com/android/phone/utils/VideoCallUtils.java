package com.android.phone.utils;

import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.VideoProfile;
import android.util.Log;
import miui.util.FeatureParser;

/* JADX INFO: loaded from: classes.dex */
public abstract class VideoCallUtils {
    public static boolean hasVideoCrs(Connection connection) {
        if (connection == null) {
            return false;
        }
        if ("mediatek".equals(FeatureParser.getString("vendor"))) {
            return (connection.getConnectionCapabilities() & 1073741824) == 1073741824;
        }
        Bundle extras = connection.getExtras();
        return extras != null && extras.getInt("crsType", 0) == 3;
    }

    public static boolean isVideoCall(Connection connection) {
        if (connection == null) {
            return false;
        }
        boolean z = hasVideoCrs(connection) && connection.getState() == 2;
        boolean zIsVideo = VideoProfile.isVideo(connection.getVideoState());
        if ("mediatek".equals(FeatureParser.getString("vendor"))) {
            Log.d("VideoCallUtils", "hasVideoCrs = " + z + ", isVideoRxTx = " + isVideoRxTx(connection.getVideoState()) + ", isVideo = " + zIsVideo);
            if (z) {
                return isVideoRxTx(connection.getVideoState());
            }
        } else {
            Bundle extras = connection.getExtras();
            if (extras == null) {
                return false;
            }
            int i = extras.getInt("originalCallType", 10);
            Log.d("VideoCallUtils", "hasVideoCrs = " + z + ", originalCallType = " + i + ", isVideo = " + zIsVideo);
            if (z) {
                return i == 3;
            }
        }
        return zIsVideo;
    }

    public static boolean isVideoRxTx(int i) {
        return VideoProfile.isTransmissionEnabled(i) && VideoProfile.isReceptionEnabled(i);
    }
}
