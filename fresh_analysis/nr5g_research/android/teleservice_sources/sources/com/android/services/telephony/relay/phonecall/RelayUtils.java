package com.android.services.telephony.relay.phonecall;

import android.app.KeyguardManager;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.SystemSettings;
import android.telecom.Connection;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.settings.TelephonySettings;
import com.android.phone.utils.VideoCallUtils;
import com.xiaomi.dist.statusbar.ConnectionStatus;
import com.xiaomi.dist.statusbar.StatusBarController;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.os.Build;
import miui.telephony.PhoneDebug;
import miui.yellowpage.YellowPagePhone;
import miui.yellowpage.YellowPageUtils;

/* JADX INFO: loaded from: classes.dex */
public class RelayUtils {
    private static final String TAG = "RelayUtils";

    public class Relay {
        public static final int AUDIO_CLIENT = 1;
        public static final String AUDIO_PACKAGE_NAME = "com.miui.audiomonitor";
        public static final int AUDIO_SERVER = 0;
        public static final String AUDIO_SERVICE_ACTION = "com.miui.audiomonitor.action.DistAudioService";
        public static final String CONNECTION_SERVICE = "pref_key_connectivity_service_state";
        public static final int CPU_NORMAL_HIGHER = 3;
        private static final String DEVICE_LEVEL_NORMAL = "Normal";
        public static final int DEVICE_TYPE_APPLE_WATCH = 16777280;
        public static final int DEVICE_TYPE_IOS = 11;
        public static final int DEVICE_TYPE_PHONE = 1;
        public static final String FROM_APPLE_RELAY_CALL = "telecomm.EXTRA_APPLE_RELAY_CALL";
        public static final String IS_SECOND_SPACE = "is_second_space";
        public static final String KEY_EXTRA_DEVICE = "extra_device";
        public static final String KEY_EXTRA_RELAY = "extra_relay";
        public static final int LYRA_ENABLED = 1;
        public static final int LYRA_UNDEFINED = -1;
        private static final String MILINK_SERVICE_PACKAGE_NAME = "com.milink.service";
        public static final String PRIMARY_LOCATION_TIMEZONE_PROVIDER = "com.miui.timezone.location.provider";
        public static final String RELAY_IOS_SERVICE_PACKAGE_NAME = "com.ios.phone";
        public static final int RELAY_PAD_VERSION = 1;
        public static final String RELAY_PC_SERVICE_PACKAGE_NAME = "com.windows.phone";
        public static final String RELAY_PHONE_SERVICE_NAME = "relayPhoneCall";
        public static final int RELAY_PHONE_VERSION = 2;
        public static final String RELAY_SERVICE_NAME = "relayCall";
        public static final String RELAY_SERVICE_PACKAGE_NAME = "com.android.phone";
        public static final long RETRY_DELAY_IN_MILLIS = 60000;
        public static Connection currentConnection;
        public static Collection currentallConnections;
        private static final boolean IS_NoRil = SystemProperties.getBoolean("ro.radio.noril", false);
        private static ConcurrentHashMap mWantRelayMap = new ConcurrentHashMap();
        private static volatile String mDeviceLevel = null;
        public static boolean isRelayAudioServiceStarted = false;

        public static void addWantRelayForDeviceId(String str) {
            RelayLog.logd(RelayUtils.TAG, "addWantRelayForDeviceId deviceId is " + str);
            mWantRelayMap.put(str, Boolean.TRUE);
        }

        public static boolean getWantRelayForDeviceId(String str) {
            Boolean bool = (Boolean) mWantRelayMap.get(str);
            RelayLog.logd(RelayUtils.TAG, "getWantRelayForDeviceId deviceId is " + str + " value is " + bool);
            if (bool != null) {
                return bool.booleanValue();
            }
            return false;
        }

        public static void clearWantRelayMap() {
            mWantRelayMap.clear();
        }

        public static void setCurrentConnection(Collection collection, Connection connection) {
            currentConnection = connection;
            currentallConnections = collection;
        }

        public static void clearCurrentConnection() {
            currentConnection = null;
            currentallConnections = null;
        }

        public static boolean isRelaySupportedByDevice() {
            if (Build.IS_INTERNATIONAL_BUILD) {
                return (!isInCallUiInstalled(System.getContext()) || isInCallSupportRelay()) && isAudioSupportRelay();
            }
            return isInCallSupportRelay() && isAudioSupportRelay();
        }

        public static boolean isInCallUiInstalled(Context context) {
            boolean z = false;
            try {
                z = context.getPackageManager().getApplicationInfo("com.android.incallui", 0) != null;
                RelayLog.logi(RelayUtils.TAG, "isInCallUiInstalled=" + z);
                return z;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return z;
            }
        }

        public static boolean isConnectDiscAudio() {
            return Build.IS_INTERNATIONAL_BUILD && !isInCallUiInstalled(System.getContext());
        }

        public static boolean supportRelayCallFeature(Context context) {
            boolean z = true;
            try {
                z = context.getResources().getBoolean(R.bool.config_supported_relay_call);
                RelayLog.logi("supportRelayCallFeature: " + z);
                return z;
            } catch (Exception e) {
                e.printStackTrace();
                return z;
            }
        }

        public static boolean supportDualPhoneRelayCallFeature(Context context) {
            return DEVICE_LEVEL_NORMAL.equals(getDeviceLevel(context)) && !Build.IS_INTERNATIONAL_BUILD;
        }

        public static String getDeviceLevel(Context context) {
            Bundle bundle;
            if (mDeviceLevel != null) {
                RelayLog.logi("getDeviceLevel mDeviceLevel is " + mDeviceLevel);
                return mDeviceLevel;
            }
            try {
                if (context == null) {
                    RelayLog.logi("getDeviceLevel context is null");
                    return null;
                }
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(MILINK_SERVICE_PACKAGE_NAME, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                if (applicationInfo != null && (bundle = applicationInfo.metaData) != null) {
                    mDeviceLevel = bundle.getString("miui.deviceLevels");
                }
                RelayLog.logi("getDeviceLevel mDeviceLevel is " + mDeviceLevel);
                return mDeviceLevel;
            } catch (Exception unused) {
                RelayLog.logi("getDeviceLevel, failed to get milink metaData.");
            }
        }

        public static boolean isInCall(Context context) {
            return ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getCallState() != 0;
        }

        public static boolean isRelayConnected() {
            return PhoneContinuityController.getInstance().isContinuityServiceConnected();
        }

        public static boolean isCallRelayAnswered(Connection connection) {
            Bundle extras = connection.getExtras();
            if (extras != null && extras.getBoolean("telecomm.EXTRA_RELAY_ANSWERED")) {
                RelayLog.logi("isCallRelayAnswered: Yes!");
                return true;
            }
            RelayLog.logi("isCallRelayAnswered: No!");
            return false;
        }

        public static boolean hasRelayDeviceId(Connection connection) {
            Bundle extras = connection.getExtras();
            if (extras != null && !TextUtils.isEmpty(extras.getString("telecomm.EXTRA_RELAY_DEVICE_ID"))) {
                RelayLog.logi("hasRelayDeviceId: Yes!");
                return true;
            }
            RelayLog.logi("hasRelayDeviceId: No!");
            return false;
        }

        public static boolean isConnectionInRelay(Connection connection) {
            return isCallRelayAnswered(connection) || hasRelayDeviceId(connection);
        }

        public static boolean isCallRelaying(Connection connection) {
            Bundle extras = connection.getExtras();
            if (extras != null && extras.getBoolean("telecomm.EXTRA_CALL_RELAYING")) {
                RelayLog.logi("isCallRelaying: Yes!");
                return true;
            }
            RelayLog.logi("isCallRelaying: No!");
            return false;
        }

        public static boolean isCallRelayed(Connection connection) {
            Bundle extras = connection.getExtras();
            if (extras != null && extras.getBoolean("telecomm.EXTRA_CALL_RELAYED")) {
                RelayLog.logi("isCallRelayed: Yes!");
                removeRelayLocalConnection(connection);
                return true;
            }
            if (checkRelayLocalConnection(connection)) {
                RelayLog.logi("isCallRelayed: Yes!, from relayLocalConnection");
                Bundle bundle = new Bundle();
                bundle.putBoolean("telecomm.EXTRA_CALL_RELAYING", false);
                bundle.putBoolean("telecomm.EXTRA_CALL_RELAYED", true);
                connection.putExtras(bundle);
                removeRelayLocalConnection(connection);
                return true;
            }
            RelayLog.logi("isCallRelayed: No!");
            return false;
        }

        private static boolean checkRelayLocalConnection(Connection connection) {
            RelayManager relayManager = PhoneContinuityController.getInstance().getRelayManager();
            RelayConnectionsManager relayConnectionsManager = relayManager != null ? relayManager.getRelayConnectionsManager() : null;
            if (relayConnectionsManager != null) {
                return relayConnectionsManager.isRelayedLocalConnection(connection);
            }
            return false;
        }

        private static void removeRelayLocalConnection(Connection connection) {
            RelayManager relayManager = PhoneContinuityController.getInstance().getRelayManager();
            RelayConnectionsManager relayConnectionsManager = relayManager != null ? relayManager.getRelayConnectionsManager() : null;
            if (relayConnectionsManager != null) {
                relayConnectionsManager.removeRelayedLocalConnection(connection);
            }
        }

        public static boolean isRelayCall(Connection connection) {
            Bundle extras = connection.getExtras();
            if (extras != null && extras.getBoolean("telecomm.EXTRA_RELAY_CALL")) {
                RelayLog.logi("isRelayCall: Yes!");
                return true;
            }
            RelayLog.logi("isRelayCall: No!");
            return false;
        }

        private static boolean isRelayFromAppleCall(Connection connection) {
            Bundle extras = connection.getExtras();
            if (extras != null && extras.getBoolean(FROM_APPLE_RELAY_CALL)) {
                RelayLog.logi("isRelayFromAppleCall: Yes!");
                return true;
            }
            RelayLog.logi("isRelayFromAppleCall: No!");
            return false;
        }

        /* JADX WARN: Code duplicated, block: B:91:0x016a  */
        public static void maybeRelayCall(Context context, Collection collection, Connection connection, String str) {
            Connection localConnectionCallRelayed;
            Connection localConnectionCallRelayed2;
            if (!isRelaySupportedByDevice()) {
                RelayLog.logi("maybeRelayCall no - device not support relay");
                return;
            }
            if (!isRelayEnable(context)) {
                RelayLog.logi("maybeRelayCall no - relay call not enable");
                return;
            }
            if (!isRelaySide() && PhoneContinuityController.getInstance().disableRelayInMirror()) {
                RelayLog.logi("maybeRelayCall no - not relay in mirror");
                return;
            }
            PhoneContinuityController.getInstance().updateFilteredService();
            if (!isRelayConnected()) {
                RelayLog.logi("maybeRelayCall no - relay not connected");
                return;
            }
            if (isRelayFromAppleCall(connection)) {
                RelayLog.logi("maybeRelayCall no - is apple relay call");
                return;
            }
            if (TextUtils.isEmpty(Telephony.getNumber(connection))) {
                RelayLog.logi("maybeRelayCall no - number is empty");
                return;
            }
            if (isBlockedCall(context)) {
                RelayLog.logi("maybeRelayCall no - blocked number");
                return;
            }
            if (Telephony.isExcludedCallMode()) {
                RelayLog.logi("maybeRelayCall no - not relay in game/talkback mode");
                return;
            }
            if (isSecondSpace(context)) {
                RelayLog.logi("maybeRelayCall no - not relay in second space");
                return;
            }
            if (checkClientModeReleaseCall(context)) {
                RelayLog.logi("maybeRelayCall no - not relay in client mode");
                return;
            }
            if (connection.getState() == 2) {
                PhoneContinuityController.getInstance().addLocalConnection(connection);
            }
            int size = collection.size();
            RelayLog.logi("maybeRelayCall - size:" + size);
            boolean z = true;
            boolean z2 = false;
            if (size > 1) {
                RelayManager relayManager = PhoneContinuityController.getInstance().getRelayManager();
                RelayConnectionsManager relayConnectionsManager = relayManager != null ? relayManager.getRelayConnectionsManager() : null;
                if (size > 2) {
                    if (relayConnectionsManager != null && (localConnectionCallRelayed2 = relayConnectionsManager.getLocalConnectionCallRelayed()) != null) {
                        relayManager.forceReleaseRelayCall(localConnectionCallRelayed2, 5);
                    }
                    RelayLog.logi("maybeRelayCall no - not relay over two calls.");
                } else if (connection.getState() != 2) {
                    if (relayConnectionsManager != null && (localConnectionCallRelayed = relayConnectionsManager.getLocalConnectionCallRelayed()) != null) {
                        relayManager.forceReleaseRelayCall(localConnectionCallRelayed, 5);
                    }
                    RelayLog.logi("maybeRelayCall no - not relay second dialing call.");
                } else {
                    RelayLog.logi("maybeRelayCall no - not relay second call.");
                }
            } else {
                if (connection.getState() == 2 && !VideoCallUtils.isVideoCall(connection)) {
                    if (!PhoneContinuityController.getInstance().isDeviceInRelay()) {
                        if (str == null || str.isEmpty()) {
                            PhoneContinuityController.getInstance().sendRelayRingCallRequest(connection);
                        } else {
                            PhoneContinuityController.getInstance().sendRelayRingCallRequestToDeviceId(connection, str);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("telecomm.EXTRA_CALL_RELAYING", true);
                        connection.putExtras(bundle);
                        z2 = true;
                    } else {
                        RelayLog.logi("maybeRelayCall no - already in relay.");
                    }
                }
                if (isCallRelayed(connection) && connection.getState() == 3 && !VideoCallUtils.isVideoCall(connection)) {
                    PhoneContinuityController.getInstance().addLocalConnection(connection);
                    RelayLog.logi("maybeRelayCall, place relay call.");
                }
                if (z) {
                    PhoneContinuityController.getInstance().onRelayStart(context, connection);
                }
                RelayLog.logi("maybeRelayCall " + z + ", state:" + connection.getState());
            }
            z = z2;
            if (z) {
                PhoneContinuityController.getInstance().onRelayStart(context, connection);
            }
            RelayLog.logi("maybeRelayCall " + z + ", state:" + connection.getState());
        }

        public static boolean checkClientModeReleaseCall(Context context) {
            RelayManager relayManager = MiPhoneContinuityController.getInstance().getRelayManager();
            RelayConnectionsManager relayConnectionsManager = relayManager != null ? relayManager.getRelayConnectionsManager() : null;
            if (relayConnectionsManager == null || relayConnectionsManager.getRelayConnections().isEmpty()) {
                return false;
            }
            if (currentConnection.getState() != 2) {
                for (RelayConnection relayConnection : relayConnectionsManager.getRelayConnections()) {
                    if (relayConnection.getAddress() != null) {
                        System.showToast(context.getString(R.string.relay_call_fallback_to_original_device, relayConnection.getHostName()));
                        relayManager.relayRelease(relayConnection.getAddress().getSchemeSpecificPart(), 7);
                    }
                }
                RelayLog.logi("maybeRelayCall - release relay call if dial sim call.");
                return true;
            }
            RelayLog.logi("maybeRelayCall no - not relay ring call if client mode.");
            return true;
        }

        public static boolean isRelayEnable(Context context) {
            return TelephonySettings.isRelayCallEnable(context.getContentResolver()) && TelephonySettings.isConnectivityServiceEnable(context.getContentResolver());
        }

        public static boolean isBlockedCall(Context context) throws Throwable {
            boolean z = false;
            ContentProviderClient contentProviderClient = null;
            try {
                try {
                    ContentProviderClient contentProviderClientAcquireUnstableContentProviderClient = context.getContentResolver().acquireUnstableContentProviderClient("content://com.android.server.telecom.provider");
                    if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                        try {
                            Bundle bundleCall = contentProviderClientAcquireUnstableContentProviderClient.call("isBlockedCall", null, null);
                            if (bundleCall != null) {
                                z = bundleCall.getBoolean("isBlockedCall");
                            }
                        } catch (Exception e) {
                            e = e;
                            contentProviderClient = contentProviderClientAcquireUnstableContentProviderClient;
                            e.printStackTrace();
                            if (contentProviderClient != null) {
                                contentProviderClient.close();
                            }
                        } catch (Throwable th) {
                            th = th;
                            contentProviderClient = contentProviderClientAcquireUnstableContentProviderClient;
                            if (contentProviderClient != null) {
                                contentProviderClient.close();
                            }
                            throw th;
                        }
                    }
                    if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                        contentProviderClientAcquireUnstableContentProviderClient.close();
                    }
                } catch (Exception e2) {
                    e = e2;
                }
                RelayLog.logi("isBlockedCall=" + z);
                return z;
            } catch (Throwable th2) {
                th = th2;
            }
        }

        public static String padSupportRelay(Context context) {
            if (!isRelaySupportedByDevice()) {
                RelayLog.logi("padSupportRelay, current device not support relay!");
                return "Device not support relay!";
            }
            if (!isRelayEnable(context)) {
                RelayLog.logi("padSupportRelay no - relay call not enable");
                return "relay call not enable!";
            }
            if (!isRelaySide() && !supportDualPhoneRelayCallFeature(context)) {
                RelayLog.logi("padSupportRelay, no, not support relay for device");
                return "not support relay for device!";
            }
            if (isRelaySide() && !System.isUnlocked()) {
                RelayLog.logi("padSupportRelay, no, locked!");
                return "Locked!";
            }
            if (Telephony.isExcludedCallMode()) {
                RelayLog.logi("padSupportRelay, no，not relay in game/talk back mode!");
                return "In game/talk back mode!";
            }
            if (isSecondSpace(context)) {
                RelayLog.logi("padSupportRelay no - not relay in second space");
                return "In second space!";
            }
            if (!isRelaySide() && isInCall(context)) {
                RelayLog.logi(RelayUtils.TAG, "padSupportRelay, there is a call on the phone");
                return "Call on the phone!";
            }
            RelayManager relayManager = getRelayManager();
            if (relayManager != null && !relayManager.getRelayConnectionsManager().getRelayConnections().isEmpty()) {
                RelayLog.logi(RelayUtils.TAG, "padSupportRelay, there is a relay call on the pad");
                return "Relay call on the pad!";
            }
            RelayLog.logi(RelayUtils.TAG, "padSupportRelay, ok");
            return "";
        }

        private static boolean isInCallSupportRelay() {
            return getInCallUiMetaData() >= 1;
        }

        private static int getInCallUiMetaData() {
            Bundle bundle;
            int i = 0;
            try {
                ApplicationInfo applicationInfo = System.getContext().getPackageManager().getApplicationInfo("com.android.incallui", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                if (applicationInfo != null && (bundle = applicationInfo.metaData) != null) {
                    i = bundle.getInt("incall.supportRelay", 0);
                }
                RelayLog.logi("getInCallUiMetaData =" + i);
                return i;
            } catch (PackageManager.NameNotFoundException unused) {
                RelayLog.logi("getInCallUiMetaData, failed to get incall relay code.");
                return i;
            }
        }

        private static boolean isAudioSupportRelay() {
            Bundle bundle;
            boolean z = false;
            try {
                ApplicationInfo applicationInfo = System.getContext().getPackageManager().getApplicationInfo(AUDIO_PACKAGE_NAME, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                z = ((applicationInfo == null || (bundle = applicationInfo.metaData) == null) ? 0 : bundle.getInt("audio.supportRelay", 0)) >= 1;
                RelayLog.logi("isAudioSupportRelay =" + z);
                return z;
            } catch (PackageManager.NameNotFoundException unused) {
                RelayLog.logi("isAudioSupportRelay, failed to get audio relay code.");
                return z;
            }
        }

        public static boolean isRelayService(String str) {
            return RELAY_SERVICE_NAME.equals(str);
        }

        public static boolean isRelayPhoneService(String str) {
            return RELAY_PHONE_SERVICE_NAME.equals(str);
        }

        public static boolean isRelayDialEnable(Context context) {
            return TelephonySettings.isRelayDialEnable(context.getContentResolver());
        }

        public static void startRelayAudioService(Context context, int i) {
            try {
                Intent intent = new Intent();
                intent.putExtra(KEY_EXTRA_DEVICE, i);
                intent.putExtra(KEY_EXTRA_RELAY, true);
                intent.setPackage(AUDIO_PACKAGE_NAME);
                intent.setAction(AUDIO_SERVICE_ACTION);
                context.startForegroundService(intent);
                isRelayAudioServiceStarted = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            RelayLog.logi("startRelayAudioService");
        }

        public static void stopRelayAudioService(Context context) {
            try {
                Intent intent = new Intent();
                intent.setPackage(AUDIO_PACKAGE_NAME);
                intent.setAction(AUDIO_SERVICE_ACTION);
                context.stopService(intent);
                isRelayAudioServiceStarted = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            RelayLog.logi("stopRelayAudioService");
        }

        public static boolean isLyraSupportRelay() {
            Bundle bundle;
            Context context = System.getContext();
            boolean z = false;
            try {
                int i = Settings.Secure.getInt(context.getContentResolver(), CONNECTION_SERVICE, -1);
                RelayLog.logi("Lyra switch state = " + i);
                if (i != 1) {
                    return false;
                }
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.xiaomi.mi_connect_service", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                z = ((applicationInfo == null || (bundle = applicationInfo.metaData) == null) ? 0 : bundle.getInt("com.xiaomi.continuity.VERSION_CODE", 0)) != 0;
                RelayLog.logi("isLyraSupportRelay =" + z);
                return z;
            } catch (PackageManager.NameNotFoundException unused) {
                RelayLog.logi("isLyraSupportRelay, failed to get switch state or audio relay code.");
                return z;
            }
        }

        public static String hideSensitiveInfo(String str) {
            if (PhoneDebug.VDBG) {
                return str;
            }
            Matcher matcher = Pattern.compile("address\":\".*?\"").matcher(str);
            if (matcher.find()) {
                String strGroup = matcher.group();
                str = str.replace(strGroup, "address\":\"" + hideChars(strGroup.substring(10, strGroup.length() - 1)) + "\"");
            }
            Matcher matcher2 = Pattern.compile("contactName\":\".*?\"").matcher(str);
            if (!matcher2.find()) {
                return str;
            }
            String strGroup2 = matcher2.group();
            return str.replace(strGroup2, "contactName\":\"" + hideChars(strGroup2.substring(14, strGroup2.length() - 1)) + "\"");
        }

        private static String hideChars(String str) {
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                charArray[i] = '*';
            }
            return new String(charArray);
        }

        public static void setIconVisibility(Context context, String str, String str2, int i, boolean z) {
            boolean z2;
            try {
                z2 = z;
                try {
                    new StatusBarController(context).updateDeviceIcon(new ConnectionStatus(str, str2, i, ConnectionStatus.getDeviceTypeName(i), z2 ? 1 : 0, "callRelay"));
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e = e2;
                z2 = z;
            }
            RelayLog.logi("setIconVisibility, visible:" + z2);
        }

        public static boolean isRelaySide() {
            return MiuiPhoneUtils.isPad() && IS_NoRil;
        }

        public static RelayManager getRelayManager() {
            if (isRelaySide()) {
                return PadContinuityController.getInstance().getRelayManager();
            }
            return MiPhoneContinuityController.getInstance().getRelayManager();
        }

        public static void setDeviceInRelay(String str) {
            if (isRelaySide()) {
                PadContinuityController.getInstance().setDeviceInRelay(str);
            } else {
                MiPhoneContinuityController.getInstance().setDeviceInRelay(str);
            }
        }

        public static void onRelayStart() {
            if (isRelaySide()) {
                PadContinuityController.getInstance().onRelayStart();
            } else {
                MiPhoneContinuityController.getInstance().onRelayStart();
            }
        }

        public static void onRelayEnd() {
            if (isRelaySide()) {
                PadContinuityController.getInstance().onRelayEnd();
            } else {
                MiPhoneContinuityController.getInstance().onRelayEnd();
            }
        }

        public static void sendRelayMessage(String str) {
            if (isRelaySide()) {
                PadContinuityController.getInstance().sendRelayMessage(str);
            } else {
                MiPhoneContinuityController.getInstance().sendRelayMessage(str);
            }
        }

        public static RelayChannelInfo getChannelInRelay() {
            if (isRelaySide()) {
                return PadContinuityController.getInstance().getChannelInRelay();
            }
            return MiPhoneContinuityController.getInstance().getChannelInRelay();
        }

        public static boolean isSecondSpace(Context context) {
            try {
                boolean z = Settings.Secure.getIntForUser(context.getContentResolver(), IS_SECOND_SPACE, 0, -2) != 0;
                RelayLog.logi("isSecondSpace:" + z);
                return z;
            } catch (Exception e) {
                RelayLog.loge(RelayUtils.TAG, "isSecondSpace has exception");
                e.printStackTrace();
                return false;
            } catch (NoSuchMethodError e2) {
                RelayLog.loge(RelayUtils.TAG, "isSecondSpace has error");
                e2.printStackTrace();
                return false;
            }
        }

        public static boolean isPrimaryLocationTimeZoneProviderValid(Context context) {
            try {
                return PRIMARY_LOCATION_TIMEZONE_PROVIDER.equals(context.getString(android.R.string.device_storage_monitor_notification_channel));
            } catch (Exception unused) {
                return false;
            }
        }
    }

    public class System {
        private static final String CALLER_ID_SELECTION = "PHONE_NUMBERS_EQUAL(data1,?) AND mimetype='vnd.android.cursor.item/phone_v2' AND raw_contact_id IN (SELECT raw_contact_id  FROM phone_lookup WHERE min_match = '+')";
        private static Context mContext;

        public static boolean isUnlocked() {
            return ((PowerManager) getContext().getSystemService("power")).isScreenOn() && !((KeyguardManager) getContext().getSystemService("keyguard")).isKeyguardLocked();
        }

        public static String getDeviceName() {
            String deviceName = SystemSettings.System.getDeviceName(getContext());
            if (TextUtils.isEmpty(deviceName)) {
                RelayLog.logi("getDeviceName name is empty!!");
            }
            return deviceName;
        }

        public static void showToast(final int i) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.RelayUtils.System.1
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(System.getContext().getApplicationContext(), i, 0).show();
                }
            });
        }

        public static void showToast(final String str) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.RelayUtils.System.2
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(System.getContext().getApplicationContext(), str, 0).show();
                }
            });
        }

        public static void showLongToast(final String str) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.services.telephony.relay.phonecall.RelayUtils.System.3
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(System.getContext().getApplicationContext(), str, 1).show();
                }
            });
        }

        public static Context getContext() {
            return mContext;
        }

        public static void setContext(Context context) {
            mContext = context;
        }

        public static String queryContactName(Context context, String str) {
            Cursor cursorQuery = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{"display_name"}, CALLER_ID_SELECTION.replace("+", PhoneNumberUtils.toCallerIDMinMatch(str)), new String[]{str}, null);
            if (cursorQuery == null || !cursorQuery.moveToFirst()) {
                RelayLog.logi(RelayUtils.TAG, "No contact matched with number");
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return null;
            }
            try {
                return cursorQuery.getString(0);
            } catch (Exception unused) {
                RelayLog.loge(RelayUtils.TAG, " Cursor get string error");
                return null;
            } finally {
                cursorQuery.close();
            }
        }

        public static void queryYellowPage(final Context context, final String str) {
            if (TextUtils.isEmpty(str) || !YellowPageUtils.isYellowPageAvailable(context)) {
                return;
            }
            new AsyncTask() { // from class: com.android.services.telephony.relay.phonecall.RelayUtils.System.4
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public String doInBackground(Void... voidArr) {
                    YellowPagePhone phoneInfo;
                    try {
                        phoneInfo = YellowPageUtils.getPhoneInfo(context, str, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        RelayLog.loge(RelayUtils.TAG, "queryYellowPage: getPhoneInfo has error");
                        phoneInfo = null;
                    }
                    if (phoneInfo == null) {
                        return null;
                    }
                    String yellowPageName = phoneInfo.getYellowPageName();
                    return TextUtils.isEmpty(yellowPageName) ? phoneInfo.getTag() : yellowPageName;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public void onPostExecute(String str2) {
                    if (TextUtils.isEmpty(str2)) {
                        return;
                    }
                    RelayManager relayManager = PhoneContinuityController.getInstance().getRelayManager();
                    if (relayManager != null) {
                        relayManager.updateContact(str, str2);
                    }
                    RelayLog.logi("Update yellow page name.");
                }
            }.execute(new Void[0]);
        }
    }

    public class Telephony {
        public static String getNumber(Connection connection) {
            Uri address;
            if (connection == null || (address = connection.getAddress()) == null) {
                return "";
            }
            return address.getSchemeSpecificPart();
        }

        public static boolean isExcludedCallMode() {
            return Settings.Secure.getInt(System.getContext().getContentResolver(), "gb_handsfree", 0) == 1;
        }
    }

    public class MainThread {
        public static final Executor MAIN_EXECUTOR;
        public static final Handler MAIN_HANDLER;

        static {
            Handler handler = new Handler(Looper.getMainLooper());
            MAIN_HANDLER = handler;
            MAIN_EXECUTOR = new RelayHandlerExecutor(handler);
        }

        public static void runOnUiThread(Runnable runnable) {
            MAIN_HANDLER.post(runnable);
        }

        public static void runOnUiThreadDelay(Runnable runnable, long j) {
            MAIN_HANDLER.postDelayed(runnable, j);
        }

        public static Executor getMainExecutor() {
            return MAIN_EXECUTOR;
        }
    }

    public class WorkThread extends HandlerThread {
        private static final WorkThread INSTANCE = new WorkThread();

        private WorkThread() {
            super("WorkThread", 10);
            start();
        }

        public static Handler getWorkHandler() {
            return new Handler(INSTANCE.getLooper());
        }
    }
}
