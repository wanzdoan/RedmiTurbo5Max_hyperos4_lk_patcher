package com.android.phone.module;

import android.content.Context;
import android.telecom.Connection;
import android.telecom.ConnectionService;
import com.android.services.telephony.TelephonyConnection;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public interface IMiConnectModule {
    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void onAnswer(TelephonyConnection telephonyConnection, int i) {
    }

    default void onCreateConnectionComplete(Context context, ConnectionService connectionService, Connection connection) {
    }

    default void onRelayCallStateChanged(TelephonyConnection telephonyConnection) {
    }

    default void removeLocalConnection(Connection connection) {
    }

    default void reportStatistics() {
    }

    default void responsePhoneStateIdle(Context context) {
    }
}
