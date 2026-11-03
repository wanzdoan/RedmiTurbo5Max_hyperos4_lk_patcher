package com.xiaomi.dist.statusbar;

import android.app.StatusBarManager;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import com.ot.pubsub.a.a;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class StatusBarController {
    private static final IBinder sToken = new Binder();
    private final Context mContext;
    private final StatusBarManager mStatusBarManager;

    public StatusBarController(Context context) {
        Objects.requireNonNull(context);
        this.mContext = context.getApplicationContext();
        this.mStatusBarManager = (StatusBarManager) context.getSystemService("statusbar");
    }

    public void updateDeviceIcon(ConnectionStatus connectionStatus) {
        Objects.requireNonNull(connectionStatus);
        Uri uri = Uri.parse("content://com.xiaomi.lyra/conn_status");
        ContentValues contentValues = new ContentValues();
        contentValues.put("device_id", connectionStatus.deviceId);
        contentValues.put("device_name", connectionStatus.deviceName);
        contentValues.put("device_type", Integer.valueOf(connectionStatus.deviceType));
        contentValues.put("device_type_name", connectionStatus.deviceTypeName);
        contentValues.put(a.J, Integer.valueOf(connectionStatus.status));
        contentValues.put(CallMethod.ARG_SHARE_CALLBACK_TAG, connectionStatus.tag);
        Bundle bundle = new Bundle();
        bundle.putBinder("token", sToken);
        try {
            this.mContext.getContentResolver().update(uri, contentValues, bundle);
        } catch (IllegalArgumentException | NullPointerException unused) {
        }
    }
}
