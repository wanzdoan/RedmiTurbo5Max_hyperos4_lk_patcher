package com.android.phone.settings.cloudbackup;

import android.content.Context;
import android.util.Log;
import com.xiaomi.settingsdk.backup.ICloudBackup;
import com.xiaomi.settingsdk.backup.data.DataPackage;

/* JADX INFO: loaded from: classes.dex */
public class PhoneSettingsCloudBackupImpl implements ICloudBackup {
    private static final String TAG = "PhoneSettingsCloudBackupImpl";

    @Override // com.xiaomi.settingsdk.backup.ICloudBackup
    public int getCurrentVersion(Context context) {
        return 1;
    }

    @Override // com.xiaomi.settingsdk.backup.ICloudBackup
    public void onBackupSettings(Context context, DataPackage dataPackage) {
        String str = TAG;
        Log.i(str, "start phone settings cloud backup");
        if (dataPackage == null) {
            Log.w(str, "onBackupSettings dataPackage is null");
            return;
        }
        CallRecordBackupHelper.backupCallRecord(context, dataPackage);
        TelocationBackupHelper.backupTelocation(context, dataPackage);
        AutoIpBackupHelper.backupAutoIp(context, dataPackage);
        CallAdvancedBackupHelper.backupCallAdvanced(context, dataPackage);
        RespondViaSmsBackupHelper.backupRespondViaSms(context, dataPackage);
        AnswerStateBackupHelper.backupAnswerState(context, dataPackage);
        AutoAnswerBackupHelper.backupAutoAnswer(context, dataPackage);
        CallFeaturesBackupHelper.backupCallFeatures(context, dataPackage);
        CallFoldFeatureBackupHelper.backupCallFoldFeatures(context, dataPackage);
        Log.i(str, "end phone settings cloud backup");
    }

    @Override // com.xiaomi.settingsdk.backup.ICloudBackup
    public void onRestoreSettings(Context context, DataPackage dataPackage, int i) {
        String str = TAG;
        Log.i(str, "start phone settings cloud restore");
        if (dataPackage == null) {
            Log.w(str, "onRestoreSettings dataPackage is null");
            return;
        }
        CallRecordBackupHelper.restoreFromCloud(context, dataPackage);
        TelocationBackupHelper.restoreTelocation(context, dataPackage);
        AutoIpBackupHelper.restoreAutoIp(context, dataPackage);
        CallAdvancedBackupHelper.restoreCallAdvanced(context, dataPackage);
        RespondViaSmsBackupHelper.restoreRespondViaSms(context, dataPackage);
        AnswerStateBackupHelper.restoreAnswerState(context, dataPackage);
        AutoAnswerBackupHelper.restoreAutoAnswer(context, dataPackage);
        CallFeaturesBackupHelper.restoreCallFeatures(context, dataPackage);
        CallFoldFeatureBackupHelper.restoreCallFoldFeatures(context, dataPackage);
        Log.i(str, "end phone settings cloud restore");
    }
}
