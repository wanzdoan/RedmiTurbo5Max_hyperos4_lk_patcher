package com.android.phone;

import android.content.Context;
import android.telephony.TelephonyLocalConnection;
import com.android.phone.callcomposer.CallComposerPictureManager;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class LocalConnectionImpl implements TelephonyLocalConnection.ConnectionImpl {
    private Context mContext;

    public LocalConnectionImpl(Context context) {
        this.mContext = context;
    }

    public String getCallComposerServerUrlForHandle(int i, UUID uuid) {
        return CallComposerPictureManager.getInstance(this.mContext, i).getServerUrlForImageId(uuid);
    }
}
