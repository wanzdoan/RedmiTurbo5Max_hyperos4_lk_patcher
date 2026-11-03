package com.android.services.telephony.relay.offlinecall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.telecom.Connection;
import android.widget.Toast;
import com.xiaomi.internal.telephony.DynamicSarService;

/* JADX INFO: loaded from: classes.dex */
public class OfflineUtils {

    public class System {
        private static Context mContext;

        public static void showToast(final int i) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.services.telephony.relay.offlinecall.OfflineUtils.System.1
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(System.getContext().getApplicationContext(), i, 0).show();
                }
            });
        }

        public static void showToast(final String str) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.services.telephony.relay.offlinecall.OfflineUtils.System.2
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(System.getContext().getApplicationContext(), str, 0).show();
                }
            });
        }

        public static Context getContext() {
            return mContext;
        }

        public static void setContext(Context context) {
            mContext = context;
        }
    }

    public class Offline {
        private static final String ACTION_CONNECTION_COMPLETE = "miui.intent.action.OFFLINE_CONNECTION_COMPLETE";
        private static final String SAMPLE_RATE_16KHZ = "1";
        private static final String SAMPLE_RATE_8KHZ = "0";
        public static final int SAMPLE_RATE_VALUE_16KHZ = 16000;
        public static final int SAMPLE_RATE_VALUE_8KHZ = 8000;
        private static final String VIRTUALCALL_SAMPLE_RATE_PROP = "log.tag.SAMPLE_RATE";

        public static int getSampleRate() {
            return "1".equals(SystemProperties.get(VIRTUALCALL_SAMPLE_RATE_PROP, "0")) ? SAMPLE_RATE_VALUE_16KHZ : SAMPLE_RATE_VALUE_8KHZ;
        }

        public static boolean isOfflineSupportedByDevice() {
            return isInCallSupportOffline();
        }

        public static boolean isOfflineAnswered(Connection connection) {
            Bundle extras = connection.getExtras();
            if (extras != null && extras.getBoolean("telecomm.EXTRA_OFFLINE_ANSWERED")) {
                OfflineCallLog.logi("isOfflineAnswered: Yes!");
                return true;
            }
            OfflineCallLog.logi("isOfflineAnswered: No!");
            return false;
        }

        private static boolean isInCallSupportOffline() {
            return getInCallUiMetaData() >= 1;
        }

        private static int getInCallUiMetaData() {
            Bundle bundle;
            int i = 0;
            try {
                ApplicationInfo applicationInfo = System.getContext().getPackageManager().getApplicationInfo("com.android.incallui", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                if (applicationInfo != null && (bundle = applicationInfo.metaData) != null) {
                    i = bundle.getInt("incall.supportOffline", 0);
                }
                OfflineCallLog.logi("getInCallUiMetaData =" + i);
                return i;
            } catch (PackageManager.NameNotFoundException unused) {
                OfflineCallLog.logi("getInCallUiMetaData, failed to get incall offline code.");
                return i;
            }
        }

        public static void maybeDisconnectCall(Connection connection) {
            OfflineManager offlineManager;
            OfflineConnectionsManager offlineConnectionsManager;
            OfflineConnection connectionOfflineCall;
            if (connection == null) {
                return;
            }
            OfflineCallLog.logi("maybeDisconnectCall, state=" + connection.getState());
            if (connection.getState() == 2 || (offlineManager = OfflineCallsManager.getInstance().getOfflineManager()) == null || (offlineConnectionsManager = offlineManager.getOfflineConnectionsManager()) == null || (connectionOfflineCall = offlineConnectionsManager.getConnectionOfflineCall()) == null) {
                return;
            }
            connectionOfflineCall.onDisconnect();
            OfflineCallLog.logi("maybeDisconnectCall, disconnect");
        }

        public static void notifyConnectionComplete() {
            Intent intent = new Intent();
            intent.setAction(ACTION_CONNECTION_COMPLETE);
            intent.addFlags(268435488);
            System.getContext().sendBroadcast(intent);
            OfflineCallLog.logi("notifyConnectionComplete");
        }
    }
}
