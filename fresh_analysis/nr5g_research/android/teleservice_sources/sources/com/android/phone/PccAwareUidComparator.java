package com.android.phone;

import android.content.pm.PackageManager;
import android.os.Process;
import android.os.UserHandle;

/* JADX INFO: loaded from: classes.dex */
public abstract class PccAwareUidComparator {
    public static boolean isSameApp(PackageManager packageManager, int i, int i2) {
        if (Process.isPrivateComputeCoreUid(i)) {
            i = packageManager.getAppUidForPrivateComputeCoreUid(i);
        }
        if (Process.isPrivateComputeCoreUid(i2)) {
            i2 = packageManager.getAppUidForPrivateComputeCoreUid(i2);
        }
        return UserHandle.isSameApp(i, i2);
    }
}
