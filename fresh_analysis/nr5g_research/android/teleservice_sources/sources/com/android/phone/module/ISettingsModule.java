package com.android.phone.module;

import android.net.Uri;
import android.os.Bundle;
import com.android.internal.telephony.Phone;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public interface ISettingsModule {
    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default Uri handleIpCall(Uri uri, Phone phone, String str, Bundle bundle) {
        return null;
    }
}
