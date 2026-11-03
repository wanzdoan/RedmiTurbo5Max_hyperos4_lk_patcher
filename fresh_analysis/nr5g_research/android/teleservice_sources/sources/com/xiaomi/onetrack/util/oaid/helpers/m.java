package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.xiaomi.onetrack.util.r;

/* JADX INFO: loaded from: classes2.dex */
public class m {
    public String a(Context context) {
        String string = "";
        try {
            Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID"), null, null, null, null);
            if (cursorQuery != null) {
                if (cursorQuery.moveToNext()) {
                    string = cursorQuery.getString(cursorQuery.getColumnIndex(com.ot.pubsub.a.a.p));
                }
                cursorQuery.close();
            }
            return string;
        } catch (Exception e) {
            r.a("VivoDeviceIDHelper", e.getMessage());
            return string;
        }
    }
}
