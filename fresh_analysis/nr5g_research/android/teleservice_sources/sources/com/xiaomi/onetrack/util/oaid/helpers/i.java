package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.xiaomi.onetrack.util.r;

/* JADX INFO: loaded from: classes2.dex */
public class i {
    public String a(Context context) {
        try {
            ContentProviderClient contentProviderClientAcquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(Uri.parse("content://cn.nubia.identity/identity"));
            Bundle bundleCall = contentProviderClientAcquireContentProviderClient.call("getOAID", null, null);
            contentProviderClientAcquireContentProviderClient.close();
            if (bundleCall.getInt(com.ot.pubsub.i.a.a.d, -1) != 0) {
                return "";
            }
            return bundleCall.getString("id");
        } catch (Exception e) {
            r.a("NubiaDeviceIDHelper", e.getMessage());
            return "";
        }
    }
}
