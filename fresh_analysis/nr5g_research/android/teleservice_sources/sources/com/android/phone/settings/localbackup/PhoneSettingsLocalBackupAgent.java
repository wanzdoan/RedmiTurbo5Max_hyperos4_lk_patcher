package com.android.phone.settings.localbackup;

import android.os.ParcelFileDescriptor;
import com.android.phone.settings.cloudbackup.PhoneSettingsCloudBackupImpl;
import com.xiaomi.settingsdk.backup.SettingsBackupHelper;
import java.io.IOException;
import miui.app.backup.BackupMeta;
import miui.app.backup.FullBackupAgent;

/* JADX INFO: loaded from: classes.dex */
public class PhoneSettingsLocalBackupAgent extends FullBackupAgent {
    protected int getVersion(int i) {
        return 1;
    }

    protected int onFullBackup(ParcelFileDescriptor parcelFileDescriptor, int i) throws IOException {
        SettingsBackupHelper.backupSettings(getApplicationContext(), parcelFileDescriptor, new PhoneSettingsCloudBackupImpl());
        return super.onFullBackup(parcelFileDescriptor, i);
    }

    protected int onDataRestore(BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        SettingsBackupHelper.restoreSettings(getApplicationContext(), parcelFileDescriptor, new PhoneSettingsCloudBackupImpl());
        return super.onDataRestore(backupMeta, parcelFileDescriptor);
    }

    protected int onAttachRestore(BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor, String str) {
        SettingsBackupHelper.restoreOneFile(str, parcelFileDescriptor);
        return super.onAttachRestore(backupMeta, parcelFileDescriptor, str);
    }
}
