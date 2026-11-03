package com.android.phone.settings.cloudbackup;

import com.xiaomi.settingsdk.backup.CloudBackupServiceBase;
import com.xiaomi.settingsdk.backup.ICloudBackup;

/* JADX INFO: loaded from: classes.dex */
public class PhoneSettingsCloudBackupService extends CloudBackupServiceBase {
    @Override // com.xiaomi.settingsdk.backup.CloudBackupServiceBase
    protected ICloudBackup getBackupImpl() {
        return new PhoneSettingsCloudBackupImpl();
    }
}
